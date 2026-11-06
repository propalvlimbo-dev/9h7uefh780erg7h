package sg.gui.widgets;

import java.util.Locale;
import settings.render.ClientLocalization;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.modules.impl.init.KeybindConflictResolver;
import sg.modules.impl.misc.ClientSounds;
import sg.client.RoseClient;
import sg.settings.KeybindSetting;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class KeybindSettingWidget extends SettingWidget<KeybindSetting> {
   private @com.google.gson.annotations.SerializedName("еб")  boolean capturingKeybind;
   private final @com.google.gson.annotations.SerializedName("св")  FloatAnimation captureAnimation;
   private final @com.google.gson.annotations.SerializedName("съ")  FloatAnimation buttonWidthAnimation;
   private final @com.google.gson.annotations.SerializedName("сМ")  FloatAnimation buttonScaleAnimation;
   private final @com.google.gson.annotations.SerializedName("сН")  FloatAnimation commitFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("сн")  FloatAnimation commitRingAnimation;
   private static final float INITIAL_HEIGHT = 22.0F;
   private static final float CAPTURE_ANIMATION_SPEED = 6.0F;
   private static final float BUTTON_WIDTH_ANIMATION_SPEED = 10.0F;
   private static final float BUTTON_SCALE_ANIMATION_SPEED = 4.0F;
   private static final float COMMIT_FLASH_ANIMATION_SPEED = 3.0F;
   private static final float COMMIT_RING_ANIMATION_SPEED = 2.5F;
   private static final double CAPTURE_PULSE_TIME_DIVISOR_MILLIS = 240.0;
   private static final double CAPTURE_PULSE_AMPLITUDE = 0.5;
   private static final double CAPTURE_PULSE_OFFSET = 0.5;
   private static final String CAPTURE_MEASURE_TEXT = "...";
   private static final float MIN_BUTTON_WIDTH = 28.0F;
   private static final float BUTTON_TEXT_WIDTH_PADDING = 12.0F;
   private static final float CAPTURE_BUTTON_WIDTH_GAIN = 10.0F;
   private static final float BUTTON_SCALE_EXPANSION_FACTOR = 6.0F;
   private static final float CAPTURE_EXPANSION_BASE = 0.5F;
   private static final float BUTTON_EXPANSION_Y_FACTOR = 0.6F;
   private static final float BUTTON_BASE_HEIGHT = 19.0F;
   private static final float BUTTON_HEIGHT_EXPANSION_FACTOR = 1.2F;
   private static final float BUTTON_BASE_CORNER_RADIUS = 4.0F;
   private static final float BUTTON_RADIUS_EXPANSION_FACTOR = 0.4F;
   private static final float MIN_LABEL_WIDTH = 24.0F;
   private static final float LABEL_BUTTON_GAP = 10.0F;
   private static final float LABEL_MEASURE_FONT_SIZE = 13.0F;
   private static final float LABEL_OFFSET_Y = 4.0F;
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
   private static final float TEXT_RIGHT_MARGIN = 4.0F;
   private static final float CAPTURE_TEXT_BASE_ALPHA = 180.0F;
   private static final float CAPTURE_TEXT_PULSE_ALPHA = 75.0F;
   private static final float TEXT_ALPHA_DIVISOR = 255.0F;
   private static final float IDLE_TEXT_ALPHA_FACTOR = 0.8F;
   private static final float KEY_TEXT_OFFSET_Y = 4.0F;
   private static final float ELEMENT_HEIGHT = 22.0F;
   private static final String HITBOX_CAPTURE_MEASURE_TEXT = "...";
   private static final float HITBOX_MIN_WIDTH = 28.0F;
   private static final float HITBOX_TEXT_WIDTH_PADDING = 12.0F;
   private static final double HITBOX_HEIGHT = 19.0;
   private static final float CAPTURE_START_SCALE = 0.72F;
   private static final float CAPTURE_CANCEL_SCALE = 0.9F;
   private static final float COMMIT_SCALE = 0.7F;
   private static final long CAPTURE_DOT_INTERVAL_MILLIS = 280L;
   private static final long CAPTURE_DOT_COUNT = 3L;
   private static final String CAPTURE_DOT = ".";
   private static final String LABEL_ELLIPSIS = "...";
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
   }

   private String getCaptureDots() {
      int dotCount = (int)(System.currentTimeMillis() / CAPTURE_DOT_INTERVAL_MILLIS % CAPTURE_DOT_COUNT) + 1;
      return CAPTURE_DOT.repeat(dotCount);
   }

   private String ellipsizeLabel(String text, float maxWidth, float fontSize) {
      if (text != null && !text.isEmpty()) {
         if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), text, (int)fontSize) <= maxWidth) {
            return text;
         }

         String ellipsis = LABEL_ELLIPSIS;
         float ellipsisWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), ellipsis, (int)fontSize);
         if (ellipsisWidth >= maxWidth) {
            return ellipsis;
         }

         int endIndex = text.length();

         while (endIndex > 0) {
            endIndex = text.offsetByCodePoints(endIndex, -1);
            String candidate = text.substring(0, endIndex) + ellipsis;
            if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), candidate, (int)fontSize) <= maxWidth) {
               return candidate;
            }
         }

         return ellipsis;
      } else {
         return "";
      }
   }

   private void saveModuleConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.capturingKeybind) {
         if (keyCode == 256) {
            this.capturingKeybind = false;
            this.buttonScaleAnimation.setImmediately(CAPTURE_CANCEL_SCALE);
         } else if (keyCode != 261 && keyCode != 259) {
            this.setting.setValue(keyCode);
            this.capturingKeybind = false;
            this.animateKeybindCommit();
            KeybindConflictResolver.б(keyCode);
            this.saveModuleConfig();
         } else {
            this.setting.setValue(-1);
            this.capturingKeybind = false;
            this.animateKeybindCommit();
            this.saveModuleConfig();
         }
      }
   }

   public KeybindSettingWidget(KeybindSetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.captureAnimation = new FloatAnimation(0.0F, CAPTURE_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.buttonWidthAnimation = new FloatAnimation(0.0F, BUTTON_WIDTH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.buttonScaleAnimation = new FloatAnimation(1.0F, BUTTON_SCALE_ANIMATION_SPEED, Easings.ELASTIC_OUT);
      this.commitFlashAnimation = new FloatAnimation(0.0F, COMMIT_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.commitRingAnimation = new FloatAnimation(1.0F, COMMIT_RING_ANIMATION_SPEED, Easings.CUBIC_OUT);
   }

   private void animateKeybindCommit() {
      ClientSounds.к(this.setting.getValue() < 0 ? ClientSounds.GuiSound.BIND_RESET : ClientSounds.GuiSound.BIND_SET);
      this.commitFlashAnimation.setImmediately(1.0F);
      this.commitRingAnimation.setImmediately(0.0F);
      this.buttonScaleAnimation.setImmediately(COMMIT_SCALE);
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      String keybindLabel = this.capturingKeybind ? HITBOX_CAPTURE_MEASURE_TEXT : this.formatKeybind(this.setting.getValue());
      float keybindTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindLabel, 12);
      float buttonWidth = Math.max(this.buttonWidthAnimation.getValue(), Math.max(HITBOX_MIN_WIDTH, keybindTextWidth + HITBOX_TEXT_WIDTH_PADDING));
      float buttonX = this.x + this.width - buttonWidth;
      if (this.capturingKeybind) {
         if (button != 0) {
            this.setting.setValue(button);
            this.capturingKeybind = false;
            this.animateKeybindCommit();
            KeybindConflictResolver.б(button);
            this.saveModuleConfig();
         }
      } else {
         if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, buttonX, this.y + 1.0F, buttonWidth, HITBOX_HEIGHT)) {
            this.capturingKeybind = true;
            ClientSounds.к(ClientSounds.GuiSound.BIND_START);
            this.buttonScaleAnimation.setImmediately(CAPTURE_START_SCALE);
         }
      }
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.captureAnimation.updateTarget(this.capturingKeybind ? 1.0F : 0.0F);
      this.buttonScaleAnimation.updateTarget(1.0F);
      this.commitFlashAnimation.updateTarget(0.0F);
      this.commitRingAnimation.updateTarget(1.0F);
      float captureProgress = this.captureAnimation.getValue();
      float pulse = (float)(Math.sin(System.currentTimeMillis() / CAPTURE_PULSE_TIME_DIVISOR_MILLIS) * CAPTURE_PULSE_AMPLITUDE + CAPTURE_PULSE_OFFSET);
      String keybindLabel = this.capturingKeybind ? this.getCaptureDots() : this.formatKeybind(this.setting.getValue());
      float keybindTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.capturingKeybind ? CAPTURE_MEASURE_TEXT : keybindLabel, 12);
      float targetButtonWidth = Math.max(MIN_BUTTON_WIDTH, keybindTextWidth + BUTTON_TEXT_WIDTH_PADDING) + captureProgress * CAPTURE_BUTTON_WIDTH_GAIN;
      if (this.buttonWidthAnimation.getValue() <= 0.0F) {
         this.buttonWidthAnimation.setImmediately(targetButtonWidth);
      }

      this.buttonWidthAnimation.updateTarget(targetButtonWidth);
      float buttonWidth = this.buttonWidthAnimation.getValue();
      float expansion = (this.buttonScaleAnimation.getValue() - 1.0F) * BUTTON_SCALE_EXPANSION_FACTOR + captureProgress * (CAPTURE_EXPANSION_BASE + pulse);
      float buttonX = this.x + this.width - buttonWidth;
      float expandedButtonX = buttonX - expansion * 2.0F;
      float expandedButtonY = this.y + 1.0F - expansion * BUTTON_EXPANSION_Y_FACTOR;
      float expandedButtonWidth = buttonWidth + expansion * 2.0F;
      float expandedButtonHeight = BUTTON_BASE_HEIGHT + expansion * BUTTON_HEIGHT_EXPANSION_FACTOR;
      float cornerRadius = BUTTON_BASE_CORNER_RADIUS + expansion * BUTTON_RADIUS_EXPANSION_FACTOR;
      int accentColor = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int textColor = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      float availableLabelWidth = Math.max(MIN_LABEL_WIDTH, buttonX - this.x - LABEL_BUTTON_GAP);
      String label = this.ellipsizeLabel(ClientLocalization.translate(this.setting.getName()), availableLabelWidth, LABEL_MEASURE_FONT_SIZE);
      UiDrawing.drawText(ClientFonts.getMediumFont(), label, this.x, this.y + LABEL_OFFSET_Y, 13, textColor);
      float shadowAlpha = Math.max(captureProgress * (CAPTURE_SHADOW_BASE_ALPHA + CAPTURE_SHADOW_PULSE_ALPHA * pulse), this.commitFlashAnimation.getValue() * COMMIT_SHADOW_ALPHA_FACTOR);
      if (shadowAlpha > SHADOW_VISIBILITY_THRESHOLD) {
         UiDrawing.drawShadow(expandedButtonX, expandedButtonY, expandedButtonWidth, expandedButtonHeight, BUTTON_SHADOW_RADIUS, ColorMath.withAlpha(accentColor, shadowAlpha));
      }

      int backgroundColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), accentColor, captureProgress);
      if (!this.capturingKeybind && KeybindConflictResolver.в(this.setting.getValue(), this.setting)) {
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

      float textReveal = Math.clamp((buttonWidth - keybindTextWidth) / TEXT_REVEAL_PADDING_DIVISOR, 0.0F, 1.0F);
      float keybindTextX = Math.min(buttonX + buttonWidth / 2.0F - keybindTextWidth / 2.0F, this.x + this.width - keybindTextWidth - TEXT_RIGHT_MARGIN);
      int keybindTextColor = this.capturingKeybind
         ? ColorMath.packArgb(255, 255, 255, (int)((CAPTURE_TEXT_BASE_ALPHA + CAPTURE_TEXT_PULSE_ALPHA * pulse) * textReveal))
         : ColorMath.withAlpha(textColor, ThemeEditor.getColorAlpha(ThemeColorSlot.ACTIVE_TEXT) / TEXT_ALPHA_DIVISOR * IDLE_TEXT_ALPHA_FACTOR * textReveal);
      UiDrawing.drawText(ClientFonts.getMediumFont(), keybindLabel, keybindTextX, this.y + KEY_TEXT_OFFSET_Y, 12, keybindTextColor);
      this.height = ELEMENT_HEIGHT;
   }

   @Override
   public boolean isCapturingKeybind() {
      return this.capturingKeybind;
   }

   private String formatKeybind(int keyCode) {
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
}
