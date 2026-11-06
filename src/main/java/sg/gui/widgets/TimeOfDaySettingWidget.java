package sg.gui.widgets;

import net.minecraft.client.Minecraft;
import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.ChoiceOption;
import sg.settings.TimeOfDaySetting;

public class TimeOfDaySettingWidget extends SettingWidget<TimeOfDaySetting> {
   private final @com.google.gson.annotations.SerializedName("сь")  FloatAnimation timeFractionAnimation;
   private final @com.google.gson.annotations.SerializedName("сж")  FloatAnimation hoverAnimation;
   private final @com.google.gson.annotations.SerializedName("с弟")  FloatAnimation dragAnimation;
   private @com.google.gson.annotations.SerializedName("ХЗ")  boolean dragging;
   private @com.google.gson.annotations.SerializedName("ДЭ")  float dragTimeFraction;
   private @com.google.gson.annotations.SerializedName("Д必")  float dragFractionOffset;
   private @com.google.gson.annotations.SerializedName("Дщ")  float dialCenterX;
   private @com.google.gson.annotations.SerializedName("ДГ")  float dialCenterY;
   private @com.google.gson.annotations.SerializedName("Дл")  float dialX;
   private @com.google.gson.annotations.SerializedName("Дэ")  float dialWidth;
   private static final float INITIAL_HEIGHT = 31.0F;
   private static final float TIME_ANIMATION_SPEED = 9.0F;
   private static final float HOVER_ANIMATION_SPEED = 11.0F;
   private static final float DRAG_ANIMATION_SPEED = 14.0F;
   private static final float DIAL_WIDTH = 90.0F;
   private static final float DIAL_CENTER_X_FACTOR = 0.69F;
   private static final float DIAL_CENTER_OFFSET_Y = 28.0F;
   private static final float DIAL_HOVER_OFFSET_Y = 3.0F;
   private static final double DIAL_HOVER_HEIGHT = 26.0;
   private static final long MISSING_WORLD_CLOCK_TICKS = 6000L;
   private static final float ANIMATION_MAX_FORWARD_FRACTION = 0.5F;
   private static final float ANIMATION_MIN_BACKWARD_FRACTION = -0.5F;
   private static final float LABEL_OFFSET_Y = 8.0F;
   private static final float OPTION_LABEL_WIDTH_PADDING = 16.0F;
   private static final float OPTION_LABEL_DIAL_GAP = 7.0F;
   private static final float OPTION_LABEL_OFFSET_X = 8.0F;
   private static final float OPTION_LABEL_OFFSET_Y = 8.0F;
   private static final float DIAL_DRAW_OFFSET_Y = 3.0F;
   private static final float DIAL_DRAW_HEIGHT = 26.0F;
   private static final float ELEMENT_HEIGHT = 31.0F;
   private static final double DIAL_HITBOX_HEIGHT = 28.0;
   private static final String DEFAULT_TIME_OPTION_NAME = "Обычное";
   private static final float DRAG_HITBOX_CENTER_X_OFFSET = 22.0F;
   private static final double DRAG_HITBOX_WIDTH = 44.0;
   private static final double DRAG_HITBOX_HEIGHT = 28.0;
   private static final float MAX_DRAG_FRACTION_OFFSET = 0.5F;
   private static final float MIN_DRAG_FRACTION_OFFSET = -0.5F;
   private static final double SCROLL_HITBOX_HEIGHT = 28.0;
   private static final float ANGLE_OFFSET_RADIANS = (float) Math.PI;
   private static final float FULL_TURN_RADIANS = (float) (Math.PI * 2);

   public TimeOfDaySettingWidget(TimeOfDaySetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.timeFractionAnimation = new FloatAnimation(0.0F, TIME_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.hoverAnimation = new FloatAnimation(0.0F, HOVER_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.dragAnimation = new FloatAnimation(0.0F, DRAG_ANIMATION_SPEED, Easings.BACK_OUT);
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (button == 0) {
         this.dragging = false;
      }
   }

   @Override
   public void tick() {
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (WorldProjection.containsPoint(mouseX, mouseY, this.dialX, this.y + 2.0F, this.dialWidth, DIAL_HITBOX_HEIGHT)) {
         if (button == 1) {
            for (ChoiceOption option : this.setting.getValue()) {
               if (option.getName().equals(DEFAULT_TIME_OPTION_NAME)) {
                  ChoiceOption previousOptionBeforeDefaultReset = this.setting.getSelectedOption();
                  this.setting.setSelectedOption(option);
                  this.playChangedOptionSound(previousOptionBeforeDefaultReset);
                  return;
               }
            }
         }

         if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, this.dialCenterX - DRAG_HITBOX_CENTER_X_OFFSET, this.y + 2.0F, DRAG_HITBOX_WIDTH, DRAG_HITBOX_HEIGHT)) {
            float currentTimeFraction = this.timeFractionAnimation.getValue() - (float)Math.floor(this.timeFractionAnimation.getValue());
            this.dragFractionOffset = currentTimeFraction - this.timeFractionFromMouse(mouseX, mouseY);
            if (this.dragFractionOffset > MAX_DRAG_FRACTION_OFFSET) {
               this.dragFractionOffset--;
            }

            if (this.dragFractionOffset < MIN_DRAG_FRACTION_OFFSET) {
               this.dragFractionOffset++;
            }

            this.dragging = true;
            this.updateDraggedTime(mouseX, mouseY);
         } else if (button == 0) {
            ChoiceOption previousOption = this.setting.getSelectedOption();
            this.setting.cycleSelection(1);
            this.playChangedOptionSound(previousOption);
         }
      }
   }

   private void playChangedOptionSound(ChoiceOption previousOption) {
      if (this.setting.getSelectedOption() != previousOption) {
         ClientSounds.к(ClientSounds.GuiSound.MODE);
      }
   }

   private void updateDraggedTime(float mouseX, float mouseY) {
      this.dragTimeFraction = this.timeFractionFromMouse(mouseX, mouseY) + this.dragFractionOffset;
      this.dragTimeFraction = this.dragTimeFraction - (float)Math.floor(this.dragTimeFraction);
      ChoiceOption previousOption = this.setting.getSelectedOption();
      this.setting.selectNearestTimeOfDay(this.dragTimeFraction);
      this.playChangedOptionSound(previousOption);
   }

   private float timeFractionFromMouse(float mouseX, float mouseY) {
      float angleRadians = (float)Math.atan2(mouseY - this.dialCenterY, mouseX - this.dialCenterX);
      return (angleRadians + ANGLE_OFFSET_RADIANS) / FULL_TURN_RADIANS;
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.dialWidth = DIAL_WIDTH;
      this.dialX = this.x + this.width - this.dialWidth;
      this.dialCenterX = this.dialX + this.dialWidth * DIAL_CENTER_X_FACTOR;
      this.dialCenterY = this.y + DIAL_CENTER_OFFSET_Y;
      if (this.dragging) {
         this.updateDraggedTime(mouseX, mouseY);
      }

      boolean dialHovered = WorldProjection.containsPoint(mouseX, mouseY, this.dialX, this.y + DIAL_HOVER_OFFSET_Y, this.dialWidth, DIAL_HOVER_HEIGHT);
      this.hoverAnimation.updateTarget(dialHovered ? 1.0F : 0.0F);
      this.dragAnimation.updateTarget(this.dragging ? 1.0F : 0.0F);
      Minecraft client = Minecraft.getInstance();
      long worldClockTicks = client.level == null ? MISSING_WORLD_CLOCK_TICKS : client.level.getOverworldClockTime();
      float targetTimeFraction = this.setting.getTimeOfDayFraction(worldClockTicks);
      if (this.dragging) {
         this.timeFractionAnimation.setImmediately(this.dragTimeFraction);
      } else {
         float currentTimeFraction = this.timeFractionAnimation.getValue();

         while (targetTimeFraction - currentTimeFraction > ANIMATION_MAX_FORWARD_FRACTION) {
            targetTimeFraction--;
         }

         while (targetTimeFraction - currentTimeFraction < ANIMATION_MIN_BACKWARD_FRACTION) {
            targetTimeFraction++;
         }

         this.timeFractionAnimation.updateTarget(targetTimeFraction);
      }

      float renderTimeFraction = this.timeFractionAnimation.getValue() - (float)Math.floor(this.timeFractionAnimation.getValue());
      float hoverProgress = Math.clamp(this.hoverAnimation.getValue(), 0.0F, 1.0F);
      float interactionProgress = Math.clamp(Math.max(hoverProgress, this.dragAnimation.getValue()), 0.0F, 1.0F);
      int labelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), hoverProgress);
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y + LABEL_OFFSET_Y, 13, labelColor);
      String optionLabel = ClientLocalization.translate(this.setting.getDisplayLabel());
      float optionLabelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), optionLabel, 12) + OPTION_LABEL_WIDTH_PADDING;
      float optionLabelX = this.dialX - optionLabelWidth - OPTION_LABEL_DIAL_GAP;
      int optionLabelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), hoverProgress);
      UiDrawing.drawText(ClientFonts.getMediumFont(), optionLabel, optionLabelX + OPTION_LABEL_OFFSET_X, this.y + OPTION_LABEL_OFFSET_Y, 12, optionLabelColor);
      UiDrawing.drawTimeDial(this.dialX, this.y + DIAL_DRAW_OFFSET_Y, this.dialWidth, DIAL_DRAW_HEIGHT, renderTimeFraction, interactionProgress);
      this.height = ELEMENT_HEIGHT;
   }

   @Override
   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      if (!WorldProjection.containsPoint(mouseX, mouseY, this.dialX, this.y + 2.0F, this.dialWidth, SCROLL_HITBOX_HEIGHT)) {
         return false;
      }

      if (scrollDelta == 0.0) {
         return true;
      }

      ChoiceOption previousOption = this.setting.getSelectedOption();
      this.setting.cycleSelection(scrollDelta > 0.0 ? -1 : 1);
      this.playChangedOptionSound(previousOption);
      return true;
   }
}
