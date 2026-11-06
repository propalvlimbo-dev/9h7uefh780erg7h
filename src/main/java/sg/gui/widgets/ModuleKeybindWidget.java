package sg.gui.widgets;

import java.util.Locale;
import lombok.Generated;
import settings.render.ClientLocalization;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.modules.impl.init.KeybindConflictResolver;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.KeybindMode;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.util.math.EasingFunctions;
import sg.modules.impl.init.TickAnimation;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class ModuleKeybindWidget extends ClickGuiWidget {
   private final @com.google.gson.annotations.SerializedName("В")  ClientModule module;
   private final @com.google.gson.annotations.SerializedName("Я")  TickAnimation visibilityAnimation = new TickAnimation();
   private final @com.google.gson.annotations.SerializedName("т")  FloatAnimation captureAnimation;
   private final @com.google.gson.annotations.SerializedName("а")  FloatAnimation buttonWidthAnimation;
   private final @com.google.gson.annotations.SerializedName("Ю")  FloatAnimation buttonScaleAnimation;
   private final @com.google.gson.annotations.SerializedName("Ч")  FloatAnimation commitFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("Л")  FloatAnimation commitRingAnimation;
   private @com.google.gson.annotations.SerializedName("И")  boolean open;
   private @com.google.gson.annotations.SerializedName("ь")  boolean capturingKeybind;
   private static final float CAPTURE_ANIMATION_SPEED = 6.0F;
   private static final float BUTTON_WIDTH_ANIMATION_SPEED = 10.0F;
   private static final float BUTTON_SCALE_ANIMATION_SPEED = 4.0F;
   private static final float COMMIT_FLASH_ANIMATION_SPEED = 3.0F;
   private static final float COMMIT_RING_ANIMATION_SPEED = 2.5F;
   private static final float VISIBILITY_TICK_STEP = 0.3F;
   private static final float OPEN_SCALE_FACTOR = 1.25F;
   private static final float OPEN_SCALE_VISIBILITY_DIVISOR = 4.0F;
   private static final float POPUP_SHADOW_RADIUS = 12.0F;
   private static final float POPUP_BLUR_RADIUS = 16.0F;
   private static final float POPUP_BLUR_CORNER_RADIUS = 10.0F;
   private static final double POPUP_BLUR_ALPHA = 255.0;
   private static final float POPUP_CORNER_RADIUS = 10.0F;
   private static final float POPUP_OUTLINE_CORNER_RADIUS = 10.0F;
   private static final float MODULE_LABEL_OFFSET_X = 12.0F;
   private static final float MODULE_LABEL_OFFSET_Y = 9.0F;
   private static final String KEY_ROW_LABEL = "Кнопка";
   private static final float KEY_ROW_LABEL_OFFSET_X = 12.0F;
   private static final float KEY_ROW_LABEL_OFFSET_Y = 35.0F;
   private static final String MODE_ROW_LABEL = "Мод бинда";
   private static final float MODE_ROW_LABEL_OFFSET_X = 12.0F;
   private static final float MODE_ROW_LABEL_OFFSET_Y = 59.0F;
   private static final double CAPTURE_PULSE_TIME_DIVISOR_MILLIS = 240.0;
   private static final double CAPTURE_PULSE_AMPLITUDE = 0.5;
   private static final double CAPTURE_PULSE_OFFSET = 0.5;
   private static final String CAPTURE_MEASURE_TEXT = "...";
   private static final float MIN_BUTTON_WIDTH = 28.0F;
   private static final float BUTTON_TEXT_WIDTH_PADDING = 12.0F;
   private static final float CAPTURE_BUTTON_WIDTH_GAIN = 10.0F;
   private static final float MIN_MODE_BUTTON_WIDTH = 40.0F;
   private static final float MODE_TEXT_WIDTH_PADDING = 14.0F;
   private static final float KEY_BUTTON_RIGHT_MARGIN = 12.0F;
   private static final float MODE_BUTTON_RIGHT_MARGIN = 12.0F;
   private static final float BUTTON_SCALE_EXPANSION_FACTOR = 6.0F;
   private static final float CAPTURE_EXPANSION_BASE = 0.5F;
   private static final float KEY_BUTTON_OFFSET_Y = 30.0F;
   private static final float BUTTON_EXPANSION_Y_FACTOR = 0.6F;
   private static final float BUTTON_BASE_HEIGHT = 19.0F;
   private static final float BUTTON_HEIGHT_EXPANSION_FACTOR = 1.2F;
   private static final float BUTTON_BASE_CORNER_RADIUS = 4.0F;
   private static final float BUTTON_RADIUS_EXPANSION_FACTOR = 0.4F;
   private static final float CAPTURE_SHADOW_BASE_ALPHA = 0.35F;
   private static final float CAPTURE_SHADOW_PULSE_ALPHA = 0.4F;
   private static final float COMMIT_SHADOW_ALPHA_FACTOR = 0.7F;
   private static final float SHADOW_VISIBILITY_THRESHOLD = 0.01F;
   private static final float BUTTON_SHADOW_RADIUS = 10.0F;
   private static final double CONFLICT_COLOR_ALPHA = 255.0;
   private static final float CONFLICT_COLOR_MIX = 0.45F;
   private static final float CAPTURE_OUTLINE_VISIBILITY_THRESHOLD = 0.01F;
   private static final float CAPTURE_OUTLINE_BASE_ALPHA = 0.25F;
   private static final float CAPTURE_OUTLINE_PULSE_ALPHA = 0.45F;
   private static final float COMMIT_RING_EXPANSION_GAIN = 5.0F;
   private static final float COMMIT_RING_THICKNESS = 1.5F;
   private static final float COMMIT_RING_ALPHA_FACTOR = 0.7F;
   private static final float COMMIT_FLASH_VISIBILITY_THRESHOLD = 0.01F;
   private static final double COMMIT_FLASH_COLOR_ALPHA = 255.0;
   private static final float COMMIT_FLASH_ALPHA_FACTOR = 0.6F;
   private static final float TEXT_REVEAL_PADDING_DIVISOR = 10.0F;
   private static final float KEY_TEXT_RIGHT_MARGIN = 16.0F;
   private static final float CAPTURE_TEXT_BASE_ALPHA = 180.0F;
   private static final float CAPTURE_TEXT_PULSE_ALPHA = 75.0F;
   private static final float TEXT_ALPHA_DIVISOR = 255.0F;
   private static final float IDLE_TEXT_ALPHA_FACTOR = 0.8F;
   private static final float KEY_TEXT_OFFSET_Y = 33.0F;
   private static final float MODE_BUTTON_OFFSET_Y = 54.0F;
   private static final float MODE_BUTTON_HEIGHT = 20.0F;
   private static final float MODE_BUTTON_CORNER_RADIUS = 5.0F;
   private static final float MODE_TEXT_CENTER_FACTOR = 0.5F;
   private static final float MODE_TEXT_OFFSET_Y = 57.0F;
   private static final String HITBOX_CAPTURE_MEASURE_TEXT = "...";
   private static final float HITBOX_MIN_KEY_WIDTH = 28.0F;
   private static final float HITBOX_KEY_TEXT_WIDTH_PADDING = 12.0F;
   private static final float HITBOX_KEY_RIGHT_MARGIN = 12.0F;
   private static final float HITBOX_MIN_MODE_WIDTH = 40.0F;
   private static final float HITBOX_MODE_TEXT_WIDTH_PADDING = 14.0F;
   private static final float KEY_HITBOX_OFFSET_Y = 30.0F;
   private static final double KEY_HITBOX_HEIGHT = 19.0;
   private static final float CAPTURE_START_SCALE = 0.72F;
   private static final float MODE_HITBOX_RIGHT_MARGIN = 12.0F;
   private static final float MODE_HITBOX_OFFSET_Y = 54.0F;
   private static final double MODE_HITBOX_HEIGHT = 20.0;
   private static final float CAPTURE_CANCEL_SCALE = 0.9F;
   private static final float COMMIT_SCALE = 0.7F;
   private static final long CAPTURE_DOT_INTERVAL_MILLIS = 280L;
   private static final long CAPTURE_DOT_COUNT = 3L;
   private static final String CAPTURE_DOT = ".";
   private static final String UNBOUND_KEY_LABEL = "None";
   private static final String KEY_SPACE_LABEL = "SPACE";
   private static final String KEY_LEFT_SHIFT_LABEL = "L-SHIFT";
   private static final String KEY_RIGHT_SHIFT_LABEL = "R-SHIFT";
   private static final String KEY_LEFT_CONTROL_LABEL = "L-CTRL";
   private static final String KEY_RIGHT_CONTROL_LABEL = "R-CTRL";
   private static final String KEY_LEFT_ALT_LABEL = "L-ALT";
   private static final String KEY_RIGHT_ALT_LABEL = "R-ALT";
   private static final String KEY_ENTER_LABEL = "ENTER";
   private static final String KEY_TAB_LABEL = "TAB";
   private static final String KEY_CAPS_LOCK_LABEL = "CAPS";
   private static final String KEY_UP_LABEL = "UP";
   private static final String KEY_DOWN_LABEL = "DOWN";
   private static final String KEY_LEFT_LABEL = "LEFT";
   private static final String KEY_RIGHT_LABEL = "RIGHT";
   private static final String KEY_HOME_LABEL = "HOME";
   private static final String KEY_END_LABEL = "END";
   private static final String KEY_INSERT_LABEL = "INSERT";
   private static final String KEY_PAGE_UP_LABEL = "PAGE UP";
   private static final String KEY_PAGE_DOWN_LABEL = "PAGE DOWN";

   @Override
   public void tick() {
      this.visibilityAnimation.tick(this.open);
      if (!this.visibilityAnimation.isVisible() && !this.open) {
         this.screen.getBinds().remove(this);
      }
   }

   @Generated
   public ClientModule getModule() {
      return this.module;
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.visibilityAnimation.configureAndInterpolate(0.0F, 1.0F, VISIBILITY_TICK_STEP, EasingFunctions.LINEAR, tickDelta);
      this.captureAnimation.updateTarget(this.capturingKeybind ? 1.0F : 0.0F);
      this.buttonScaleAnimation.updateTarget(1.0F);
      this.commitFlashAnimation.updateTarget(0.0F);
      this.commitRingAnimation.updateTarget(1.0F);
      float popupScale = Math.max(1.0F, OPEN_SCALE_FACTOR * (1.0F - this.visibilityAnimation.getInterpolatedValue() / OPEN_SCALE_VISIBILITY_DIVISOR));
      float previousAlpha = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(previousAlpha * this.visibilityAnimation.getInterpolatedValue());
      WorldProjection.scaleAroundRect(this.x, this.y, this.width, this.height, popupScale);
      UiDrawing.drawShadow(this.x, this.y, this.width, this.height, POPUP_SHADOW_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW));
      UiDrawing.drawBlur(this.x, this.y, this.width, this.height, POPUP_BLUR_RADIUS, POPUP_BLUR_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, POPUP_BLUR_ALPHA));
      UiDrawing.drawRoundedRect(this.x, this.y, this.width, this.height, POPUP_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
      UiDrawing.drawOutline(this.x, this.y, this.width, this.height, 1.0F, POPUP_OUTLINE_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.module.getName()), this.x + MODULE_LABEL_OFFSET_X, this.y + MODULE_LABEL_OFFSET_Y, 13, ThemeEditor.getColorArgb(ThemeColorSlot.TEXT));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(KEY_ROW_LABEL), this.x + KEY_ROW_LABEL_OFFSET_X, this.y + KEY_ROW_LABEL_OFFSET_Y, 12, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(MODE_ROW_LABEL), this.x + MODE_ROW_LABEL_OFFSET_X, this.y + MODE_ROW_LABEL_OFFSET_Y, 12, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      float captureProgress = this.captureAnimation.getValue();
      float pulse = (float)(Math.sin(System.currentTimeMillis() / CAPTURE_PULSE_TIME_DIVISOR_MILLIS) * CAPTURE_PULSE_AMPLITUDE + CAPTURE_PULSE_OFFSET);
      String keybindLabel = this.capturingKeybind ? this.getCaptureDots() : this.getKeybindLabel();
      String modeLabel = this.module.getKeybind().getMode().getDisplayName();
      float keybindTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.capturingKeybind ? CAPTURE_MEASURE_TEXT : keybindLabel, 12);
      float modeTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), modeLabel, 12);
      float targetKeyButtonWidth = Math.max(MIN_BUTTON_WIDTH, keybindTextWidth + BUTTON_TEXT_WIDTH_PADDING) + captureProgress * CAPTURE_BUTTON_WIDTH_GAIN;
      if (this.buttonWidthAnimation.getValue() <= 0.0F) {
         this.buttonWidthAnimation.setImmediately(targetKeyButtonWidth);
      }

      this.buttonWidthAnimation.updateTarget(targetKeyButtonWidth);
      float keyButtonWidth = this.buttonWidthAnimation.getValue();
      float modeButtonWidth = Math.max(MIN_MODE_BUTTON_WIDTH, modeTextWidth + MODE_TEXT_WIDTH_PADDING);
      float keyButtonX = this.x + this.width - KEY_BUTTON_RIGHT_MARGIN - keyButtonWidth;
      float modeButtonX = this.x + this.width - modeButtonWidth - MODE_BUTTON_RIGHT_MARGIN;
      float expansion = (this.buttonScaleAnimation.getValue() - 1.0F) * BUTTON_SCALE_EXPANSION_FACTOR + captureProgress * (CAPTURE_EXPANSION_BASE + pulse);
      float expandedButtonX = keyButtonX - expansion * 2.0F;
      float expandedButtonY = this.y + KEY_BUTTON_OFFSET_Y - expansion * BUTTON_EXPANSION_Y_FACTOR;
      float expandedButtonWidth = keyButtonWidth + expansion * 2.0F;
      float expandedButtonHeight = BUTTON_BASE_HEIGHT + expansion * BUTTON_HEIGHT_EXPANSION_FACTOR;
      float cornerRadius = BUTTON_BASE_CORNER_RADIUS + expansion * BUTTON_RADIUS_EXPANSION_FACTOR;
      int accentColor = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int textColor = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      float shadowAlpha = Math.max(captureProgress * (CAPTURE_SHADOW_BASE_ALPHA + CAPTURE_SHADOW_PULSE_ALPHA * pulse), this.commitFlashAnimation.getValue() * COMMIT_SHADOW_ALPHA_FACTOR);
      if (shadowAlpha > SHADOW_VISIBILITY_THRESHOLD) {
         UiDrawing.drawShadow(expandedButtonX, expandedButtonY, expandedButtonWidth, expandedButtonHeight, BUTTON_SHADOW_RADIUS, ColorMath.withAlpha(accentColor, shadowAlpha));
      }

      int backgroundColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), accentColor, captureProgress);
      if (!this.capturingKeybind && KeybindConflictResolver.в(this.module.getKeybind().getKeyCode(), this.module)) {
         backgroundColor = ColorMath.lerpArgb(backgroundColor, ColorMath.packArgb(255, 78, 78, CONFLICT_COLOR_ALPHA), CONFLICT_COLOR_MIX);
      }

      UiDrawing.drawRoundedRect(expandedButtonX, expandedButtonY, expandedButtonWidth, expandedButtonHeight, cornerRadius, backgroundColor);
      if (captureProgress > CAPTURE_OUTLINE_VISIBILITY_THRESHOLD) {
         UiDrawing.drawOutline(expandedButtonX, expandedButtonY, expandedButtonWidth, expandedButtonHeight, 1.0F, cornerRadius, ColorMath.withAlpha(textColor, (CAPTURE_OUTLINE_BASE_ALPHA + CAPTURE_OUTLINE_PULSE_ALPHA * pulse) * captureProgress));
      }

      float commitRingProgress = this.commitRingAnimation.getValue();
      if (commitRingProgress < 1.0F) {
         float ringExpansion = 2.0F + commitRingProgress * COMMIT_RING_EXPANSION_GAIN;
         UiDrawing.drawOutline(
            expandedButtonX - ringExpansion, expandedButtonY - ringExpansion, expandedButtonWidth + ringExpansion * 2.0F, expandedButtonHeight + ringExpansion * 2.0F, COMMIT_RING_THICKNESS, cornerRadius + ringExpansion, ColorMath.withAlpha(accentColor, (1.0F - commitRingProgress) * COMMIT_RING_ALPHA_FACTOR)
         );
      }

      float commitFlashProgress = this.commitFlashAnimation.getValue();
      if (commitFlashProgress > COMMIT_FLASH_VISIBILITY_THRESHOLD) {
         UiDrawing.drawRoundedRect(expandedButtonX, expandedButtonY, expandedButtonWidth, expandedButtonHeight, cornerRadius, ColorMath.withAlpha(ColorMath.packArgb(255, 255, 255, COMMIT_FLASH_COLOR_ALPHA), commitFlashProgress * COMMIT_FLASH_ALPHA_FACTOR));
      }

      float textReveal = Math.clamp((keyButtonWidth - keybindTextWidth) / TEXT_REVEAL_PADDING_DIVISOR, 0.0F, 1.0F);
      float keybindTextX = Math.min(keyButtonX + keyButtonWidth / 2.0F - keybindTextWidth / 2.0F, this.x + this.width - KEY_TEXT_RIGHT_MARGIN - keybindTextWidth);
      int keybindTextColor = this.capturingKeybind
         ? ColorMath.packArgb(255, 255, 255, (int)((CAPTURE_TEXT_BASE_ALPHA + CAPTURE_TEXT_PULSE_ALPHA * pulse) * textReveal))
         : ColorMath.withAlpha(textColor, ThemeEditor.getColorAlpha(ThemeColorSlot.ACTIVE_TEXT) / TEXT_ALPHA_DIVISOR * IDLE_TEXT_ALPHA_FACTOR * textReveal);
      UiDrawing.drawText(ClientFonts.getMediumFont(), keybindLabel, keybindTextX, this.y + KEY_TEXT_OFFSET_Y, 12, keybindTextColor);
      UiDrawing.drawRoundedRect(modeButtonX, this.y + MODE_BUTTON_OFFSET_Y, modeButtonWidth, MODE_BUTTON_HEIGHT, MODE_BUTTON_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED));
      UiDrawing.drawText(ClientFonts.getMediumFont(), modeLabel, modeButtonX + (modeButtonWidth - modeTextWidth) * MODE_TEXT_CENTER_FACTOR, this.y + MODE_TEXT_OFFSET_Y, 12, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      WorldProjection.unscaleAroundRect(this.x, this.y, this.width, this.height, popupScale);
      UiDrawing.setGlobalAlpha(previousAlpha);
   }

   private String getCaptureDots() {
      int dotCount = (int)(System.currentTimeMillis() / CAPTURE_DOT_INTERVAL_MILLIS % CAPTURE_DOT_COUNT) + 1;
      return CAPTURE_DOT.repeat(dotCount);
   }

   private String getKeybindLabel() {
      int keyCode = this.module.getKeybind().getKeyCode();
      if (keyCode == -1) {
         return UNBOUND_KEY_LABEL;
      }

      if (keyCode >= 0 && keyCode <= 7) {
         return "MOUSE " + (keyCode + 1);
      }

      return switch (keyCode) {
         case 32 -> KEY_SPACE_LABEL;
         case 257 -> KEY_ENTER_LABEL;
         case 258 -> KEY_TAB_LABEL;
         case 260 -> KEY_INSERT_LABEL;
         case 262 -> KEY_RIGHT_LABEL;
         case 263 -> KEY_LEFT_LABEL;
         case 264 -> KEY_DOWN_LABEL;
         case 265 -> KEY_UP_LABEL;
         case 266 -> KEY_PAGE_UP_LABEL;
         case 267 -> KEY_PAGE_DOWN_LABEL;
         case 268 -> KEY_HOME_LABEL;
         case 269 -> KEY_END_LABEL;
         case 280 -> KEY_CAPS_LOCK_LABEL;
         case 340 -> KEY_LEFT_SHIFT_LABEL;
         case 341 -> KEY_LEFT_CONTROL_LABEL;
         case 342 -> KEY_LEFT_ALT_LABEL;
         case 344 -> KEY_RIGHT_SHIFT_LABEL;
         case 345 -> KEY_RIGHT_CONTROL_LABEL;
         case 346 -> KEY_RIGHT_ALT_LABEL;
         default -> {
            String keyName = KeyDisplayNames.О(keyCode);
            yield !keyName.isEmpty() ? keyName.toUpperCase(Locale.ROOT) : "KEY " + keyCode;
         }
      };
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      String keybindMeasureText = this.capturingKeybind ? HITBOX_CAPTURE_MEASURE_TEXT : this.getKeybindLabel();
      float keybindTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindMeasureText, 12);
      float keyButtonWidth = Math.max(this.buttonWidthAnimation.getValue(), Math.max(HITBOX_MIN_KEY_WIDTH, keybindTextWidth + HITBOX_KEY_TEXT_WIDTH_PADDING));
      float keyButtonX = this.x + this.width - HITBOX_KEY_RIGHT_MARGIN - keyButtonWidth;
      if (this.capturingKeybind) {
         if (button != 0) {
            this.module.getKeybind().setKeyCode(button);
            this.capturingKeybind = false;
            this.animateKeybindCommit();
            KeybindConflictResolver.б(button);
            this.saveModuleConfig();
         }
      } else {
         String modeLabel = this.module.getKeybind().getMode().getDisplayName();
         float modeButtonWidth = Math.max(HITBOX_MIN_MODE_WIDTH, UiDrawing.getTextWidth(ClientFonts.getMediumFont(), modeLabel, 12) + HITBOX_MODE_TEXT_WIDTH_PADDING);
         if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, keyButtonX, this.y + KEY_HITBOX_OFFSET_Y, keyButtonWidth, KEY_HITBOX_HEIGHT)) {
            this.capturingKeybind = true;
            ClientSounds.к(ClientSounds.GuiSound.BIND_START);
            this.buttonScaleAnimation.setImmediately(CAPTURE_START_SCALE);
         } else {
            if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - modeButtonWidth - MODE_HITBOX_RIGHT_MARGIN, this.y + MODE_HITBOX_OFFSET_Y, modeButtonWidth, MODE_HITBOX_HEIGHT)) {
               KeybindMode nextMode = this.module.getKeybind().getMode() == KeybindMode.TOGGLE ? KeybindMode.HOLD : KeybindMode.TOGGLE;
               this.module.getKeybind().setMode(nextMode);
               ClientSounds.к(ClientSounds.GuiSound.MODE);
               this.saveModuleConfig();
            }
         }
      }
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.capturingKeybind) {
         if (keyCode == 256) {
            this.capturingKeybind = false;
            this.buttonScaleAnimation.setImmediately(CAPTURE_CANCEL_SCALE);
            return;
         }

         if (keyCode == 261 || keyCode == 259) {
            this.module.getKeybind().setKeyCode(-1);
            this.capturingKeybind = false;
            this.animateKeybindCommit();
            this.saveModuleConfig();
            return;
         }

         this.module.getKeybind().setKeyCode(keyCode);
         this.capturingKeybind = false;
         this.animateKeybindCommit();
         KeybindConflictResolver.б(keyCode);
         this.saveModuleConfig();
      }
   }

   public void close() {
      this.open = false;
   }

   private void saveModuleConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   public ModuleKeybindWidget(ClientModule module, float x, float y, float width, float height) {
      super(RoseClient.getInstance().getSelfcodeService().У());
      this.captureAnimation = new FloatAnimation(0.0F, CAPTURE_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.buttonWidthAnimation = new FloatAnimation(0.0F, BUTTON_WIDTH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.buttonScaleAnimation = new FloatAnimation(1.0F, BUTTON_SCALE_ANIMATION_SPEED, Easings.ELASTIC_OUT);
      this.commitFlashAnimation = new FloatAnimation(0.0F, COMMIT_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.commitRingAnimation = new FloatAnimation(1.0F, COMMIT_RING_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.open = true;
      this.capturingKeybind = false;
      this.module = module;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   private void animateKeybindCommit() {
      ClientSounds.к(this.module.getKeybind().getKeyCode() < 0 ? ClientSounds.GuiSound.BIND_RESET : ClientSounds.GuiSound.BIND_SET);
      this.commitFlashAnimation.setImmediately(1.0F);
      this.commitRingAnimation.setImmediately(0.0F);
      this.buttonScaleAnimation.setImmediately(COMMIT_SCALE);
   }
}
