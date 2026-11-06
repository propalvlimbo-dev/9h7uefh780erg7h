package sg.gui.widgets;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import sg.rotation.AimGeometryMath;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.TextInputState;
import sg.gui.widgets.SettingWidget;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.FloatSetting;

public class FloatSettingWidget extends SettingWidget<FloatSetting> {
   @SerializedName("В")
   private final TextInputState valueInput = new TextInputState(TextInputState.InputType.NUMBER);
   private final @com.google.gson.annotations.SerializedName("и")  FloatAnimation valueFractionAnimation;
   @SerializedName("9")
   private final FloatAnimation hoverOrFocusAnimation;
   private final @com.google.gson.annotations.SerializedName("о")  FloatAnimation dragAnimation;
   private final @com.google.gson.annotations.SerializedName("O")  FloatAnimation caretBlinkAnimation;
   private @com.google.gson.annotations.SerializedName("су")  boolean dragging;
   private @com.google.gson.annotations.SerializedName("сВ")  double scrollRemainder;
   private static final float INITIAL_HEIGHT = 34.0F;
   private static final float VALUE_ANIMATION_SPEED = 14.0F;
   private static final float HOVER_FOCUS_ANIMATION_SPEED = 12.0F;
   private static final float DRAG_ANIMATION_SPEED = 16.0F;
   private static final float CARET_ANIMATION_SPEED = 6.0F;
   private static final float SLIDER_HOVER_OFFSET_Y = 18.0F;
   private static final double SLIDER_HOVER_HEIGHT = 18.0;
   private static final long CARET_BLINK_PERIOD_MILLIS = 800L;
   private static final long CARET_VISIBLE_PHASE_MILLIS = 400L;
   private static final float CARET_HEIGHT = 12.0F;
   private static final float TRACK_OFFSET_Y = 23.0F;
   private static final float TRACK_HEIGHT = 5.0F;
   private static final float FILLED_TRACK_OFFSET_Y = 23.0F;
   private static final float FILLED_TRACK_HEIGHT = 5.0F;
   private static final float KNOB_BASE_SIZE = 12.0F;
   private static final float KNOB_HOVER_OR_FOCUS_SIZE_GAIN = 1.2F;
   private static final float KNOB_DRAG_SIZE_GAIN = 1.8F;
   private static final float KNOB_BASE_CORNER_RADIUS = 4.5F;
   private static final float KNOB_HOVER_OR_FOCUS_RADIUS_GAIN = 0.8F;
   private static final float KNOB_INACTIVE_ALPHA_DIVISOR = 255.0F;
   private static final float KNOB_INACTIVE_ALPHA_FACTOR = 0.9F;
   private static final float KNOB_ACTIVE_ALPHA_DIVISOR = 255.0F;
   private static final float KNOB_CENTER_OFFSET_Y = 25.5F;
   private static final float ELEMENT_HEIGHT = 34.0F;
   private static final float SLIDER_HITBOX_OFFSET_Y = 22.0F;
   private static final double SLIDER_HITBOX_HEIGHT = 8.0;
   private static final float VALUE_INPUT_HEIGHT = 12.0F;
   private static final float SCROLL_HITBOX_OFFSET_Y = 18.0F;
   private static final double SCROLL_HITBOX_HEIGHT = 18.0;
   private static final float DEFAULT_SCROLL_STEP_DIVISOR = 100.0F;

   @Override
   public void charTyped(String text, int codePoint, int modifiers) {
      this.valueInput.й衣(text);
   }

   @Override
   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      if (this.isControlDown() && WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y + SCROLL_HITBOX_OFFSET_Y, this.width, SCROLL_HITBOX_HEIGHT)) {
         this.scrollRemainder += scrollDelta;
         int wholeSteps = (int)this.scrollRemainder;
         if (wholeSteps == 0) {
            return true;
         }

         this.scrollRemainder -= wholeSteps;
         this.applyValue(this.setting.getValue() + this.getScrollStep() * wholeSteps, false);
         return true;
      } else {
         this.scrollRemainder = 0.0;
         return false;
      }
   }

   private float getScrollStep() {
      if (this.setting.getStep() > 0.0F) {
         return this.setting.getStep();
      }

      float valueRange = this.setting.getMaximum() - this.setting.getMinimum();
      return valueRange > 0.0F ? valueRange / DEFAULT_SCROLL_STEP_DIVISOR : 1.0F;
   }

   @Override
   public void tick() {
   }

   private float valueFromMouseX(float sliderX, float mouseX) {
      float value = (mouseX - sliderX) / this.width * (this.setting.getMaximum() - this.setting.getMinimum()) + this.setting.getMinimum();
      value = (float)AimGeometryMath.roundToStep(value, this.setting.getStep());
      return Math.clamp(value, this.setting.getMinimum(), this.setting.getMaximum());
   }

   private boolean isControlDown() {
      long windowHandle = Minecraft.getInstance().getWindow().handle();
      return GLFW.glfwGetKey(windowHandle, 341) == 1 || GLFW.glfwGetKey(windowHandle, 345) == 1;
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y + SLIDER_HITBOX_OFFSET_Y, this.width, SLIDER_HITBOX_HEIGHT)) {
         this.dragging = true;
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_GRAB);
      }

      String valueText = this.valueInput.БЦ() ? this.valueInput.ш西() : this.setting.getValue() + "";
      this.valueInput.ь(() -> this.applyValue(this.valueInput.йО(this.setting.getValue()), true));
      this.valueInput
         .method_8(
            mouseX,
            mouseY,
            this.x + this.width - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), valueText, 12),
            this.y,
            UiDrawing.getTextWidth(ClientFonts.getMediumFont(), valueText, 12),
            VALUE_INPUT_HEIGHT,
            this.setting.getValue() + "",
            button
         );
   }

   private void applyValue(float value, boolean fromTextInput) {
      float previousValue = this.setting.getValue();
      if (fromTextInput) {
         this.setting.setClampedValue(value);
      } else {
         this.setting.setValue(value);
      }

      if (!fromTextInput && Float.compare(previousValue, this.setting.getValue()) != 0) {
         ClientSounds.к(ClientSounds.GuiSound.SLIDER, this.getValueFraction());
      }
   }

   public FloatSettingWidget(FloatSetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.valueFractionAnimation = new FloatAnimation(0.0F, VALUE_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.hoverOrFocusAnimation = new FloatAnimation(0.0F, HOVER_FOCUS_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.dragAnimation = new FloatAnimation(0.0F, DRAG_ANIMATION_SPEED, Easings.BACK_OUT);
      this.caretBlinkAnimation = new FloatAnimation(0.0F, CARET_ANIMATION_SPEED, Easings.CUBIC_OUT);
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      if (this.dragging) {
         this.applyValue(this.valueFromMouseX(this.x, mouseX), false);
      }

      boolean sliderHovered = WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y + SLIDER_HOVER_OFFSET_Y, this.width, SLIDER_HOVER_HEIGHT);
      this.valueFractionAnimation.updateTarget(this.getValueFraction());
      this.hoverOrFocusAnimation.updateTarget(!sliderHovered && !this.valueInput.БЦ() ? 0.0F : 1.0F);
      this.dragAnimation.updateTarget(this.dragging ? 1.0F : 0.0F);
      this.caretBlinkAnimation.updateTarget(this.valueInput.БЦ() && System.currentTimeMillis() % CARET_BLINK_PERIOD_MILLIS <= CARET_VISIBLE_PHASE_MILLIS ? 1.0F : 0.0F);
      float valueFraction = Math.clamp(this.valueFractionAnimation.getValue(), 0.0F, 1.0F);
      float hoverOrFocusProgress = this.hoverOrFocusAnimation.getValue();
      float dragProgress = this.dragAnimation.getValue();
      float interactionProgress = Math.clamp(Math.max(hoverOrFocusProgress, dragProgress), 0.0F, 1.0F);
      String valueText = this.valueInput.БЦ() ? this.valueInput.ш西() : this.setting.getValue() + "";
      int labelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), hoverOrFocusProgress);
      int valueColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), interactionProgress);
      int trackColor = ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND);
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y, 13, labelColor);
      UiDrawing.drawText(ClientFonts.getMediumFont(), valueText, this.x + this.width - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), valueText, 12), this.y, 12, valueColor);
      UiDrawing.drawRoundedRect(
         this.x + this.width - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), valueText, 12) + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), valueText, 12),
         this.y,
         1.0F,
         CARET_HEIGHT,
         0.0F,
         ColorMath.withAlpha(valueColor, this.caretBlinkAnimation.getValue())
      );
      UiDrawing.drawRoundedRect(this.x, this.y + TRACK_OFFSET_Y, this.width, TRACK_HEIGHT, 2.0F, trackColor);
      UiDrawing.drawRoundedRect(this.x, this.y + FILLED_TRACK_OFFSET_Y, this.width * valueFraction, FILLED_TRACK_HEIGHT, 2.0F, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      float knobSize = KNOB_BASE_SIZE + hoverOrFocusProgress * KNOB_HOVER_OR_FOCUS_SIZE_GAIN + dragProgress * KNOB_DRAG_SIZE_GAIN;
      float knobRadius = KNOB_BASE_CORNER_RADIUS + hoverOrFocusProgress * KNOB_HOVER_OR_FOCUS_RADIUS_GAIN;
      float knobX = this.x + this.width * valueFraction - knobSize / 2.0F;
      int knobColor = ColorMath.lerpArgb(
         ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.CIRCLE), ThemeEditor.getColorAlpha(ThemeColorSlot.CIRCLE) / KNOB_INACTIVE_ALPHA_DIVISOR * KNOB_INACTIVE_ALPHA_FACTOR),
         ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.CIRCLE), ThemeEditor.getColorAlpha(ThemeColorSlot.CIRCLE) / KNOB_ACTIVE_ALPHA_DIVISOR),
         interactionProgress
      );
      UiDrawing.drawRoundedRect(knobX, this.y + KNOB_CENTER_OFFSET_Y - knobSize / 2.0F, knobSize, knobSize, knobRadius, knobColor);
      this.height = ELEMENT_HEIGHT;
   }

   @Override
   public void keyPressed(int keyCode) {
      this.valueInput.йУ(keyCode);
   }

   @Override
   public void keyReleased(int keyCode) {
      this.valueInput.йЯ(keyCode);
   }

   private float getValueFraction() {
      float valueRange = this.setting.getMaximum() - this.setting.getMinimum();
      return valueRange == 0.0F ? 0.0F : (this.setting.getValue() - this.setting.getMinimum()) / valueRange;
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (this.dragging) {
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_RELEASE);
      }

      this.dragging = false;
   }
}
