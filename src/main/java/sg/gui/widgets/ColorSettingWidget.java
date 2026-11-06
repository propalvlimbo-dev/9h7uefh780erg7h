package sg.gui.widgets;

import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientResourceIds;
import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.ColorSetting;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class ColorSettingWidget extends SettingWidget<ColorSetting> {
   private @com.google.gson.annotations.SerializedName("В1")  boolean pickerOpen;
   private @com.google.gson.annotations.SerializedName("ВЬ")  boolean draggingSaturationBrightness;
   private @com.google.gson.annotations.SerializedName("В衣")  boolean draggingHue;
   private @com.google.gson.annotations.SerializedName("Вш")  boolean draggingAlpha;
   private @com.google.gson.annotations.SerializedName("Хн")  float hue;
   private @com.google.gson.annotations.SerializedName("ХЦ")  float saturation;
   private @com.google.gson.annotations.SerializedName("Хч")  float brightness;
   private @com.google.gson.annotations.SerializedName("ХЭ")  float alphaFraction;
   private @com.google.gson.annotations.SerializedName("Я_")  int lastColorArgb;
   @SerializedName("6")
   private final FloatAnimation pickerVisibilityAnimation;
   @SerializedName("_")
   private final FloatAnimation previewHoverOrPickerOpenAnimation;
   private final @com.google.gson.annotations.SerializedName("Ш")  FloatAnimation previewScaleAnimation;
   private final @com.google.gson.annotations.SerializedName("ы")  FloatAnimation colorChangeFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("й")  FloatAnimation resetFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("Ж")  FloatAnimation copyButtonFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("м")  FloatAnimation pasteButtonFlashAnimation;
   private final @com.google.gson.annotations.SerializedName("К")  FloatAnimation saturationBrightnessDragAnimation;
   private final @com.google.gson.annotations.SerializedName("I")  FloatAnimation hueDragAnimation;
   private final @com.google.gson.annotations.SerializedName("Т")  FloatAnimation alphaDragAnimation;
   private static final float INITIAL_HEIGHT = 20.0F;
   private static final float PICKER_VISIBILITY_ANIMATION_SPEED = 10.0F;
   private static final float PREVIEW_HOVER_OR_PICKER_OPEN_ANIMATION_SPEED = 10.0F;
   private static final float PREVIEW_SCALE_ANIMATION_SPEED = 4.0F;
   private static final float COLOR_CHANGE_FLASH_ANIMATION_SPEED = 3.0F;
   private static final float RESET_FLASH_ANIMATION_SPEED = 3.0F;
   private static final float COPY_FLASH_ANIMATION_SPEED = 4.0F;
   private static final float PASTE_FLASH_ANIMATION_SPEED = 4.0F;
   private static final float SATURATION_BRIGHTNESS_DRAG_ANIMATION_SPEED = 8.0F;
   private static final float HUE_DRAG_ANIMATION_SPEED = 8.0F;
   private static final float ALPHA_DRAG_ANIMATION_SPEED = 8.0F;
   private static final String DISPLAY_RGBA_HEX_FORMAT = "#%02X%02X%02X%02X";
   private static final int DISPLAY_OPAQUE_ALPHA_MASK = -16777216;
   private static final float DISPLAY_HEX_COLOR_MIX_FACTOR = 0.65F;
   private static final double DISPLAY_HEX_FLASH_WHITE_ALPHA = 255.0;
   private static final float DISPLAY_HEX_FLASH_MIX_FACTOR = 0.8F;
   private static final float HEX_TEXT_RIGHT_MARGIN = 22.0F;
   private static final float HEX_TEXT_MEASURE_FONT_SIZE = 12.0F;
   private static final double PREVIEW_PULSE_TIME_DIVISOR_MILLIS = 300.0;
   private static final double PREVIEW_PULSE_AMPLITUDE = 0.5;
   private static final double PREVIEW_PULSE_OFFSET = 0.5;
   private static final float PREVIEW_HOVER_OR_PICKER_OPEN_EXPANSION_GAIN = 1.5F;
   private static final float PREVIEW_SCALE_EXPANSION_GAIN = 5.0F;
   private static final float PREVIEW_BASE_SIZE = 16.0F;
   private static final float PREVIEW_BASE_CORNER_RADIUS = 7.0F;
   private static final float PREVIEW_EXPANSION_RADIUS_FACTOR = 0.3F;
   private static final float PREVIEW_HOVER_OR_PICKER_OPEN_SHADOW_ALPHA_FACTOR = 0.35F;
   private static final float PREVIEW_OPEN_SHADOW_BASE_ALPHA = 0.3F;
   private static final float PREVIEW_OPEN_SHADOW_PULSE_ALPHA = 0.25F;
   private static final float PREVIEW_CHANGE_SHADOW_ALPHA_FACTOR = 0.55F;
   private static final float PREVIEW_SHADOW_VISIBILITY_THRESHOLD = 0.01F;
   private static final float PREVIEW_SHADOW_RADIUS = 8.0F;
   private static final float RESET_FLASH_VISIBILITY_THRESHOLD = 0.01F;
   private static final double RESET_FLASH_WHITE_ALPHA = 255.0;
   private static final float RESET_FLASH_ALPHA_FACTOR = 0.7F;
   private static final float ELEMENT_HEIGHT = 20.0F;
   private static final float PICKER_VISIBILITY_THRESHOLD = 0.01F;
   private static final float PICKER_GLOBAL_ALPHA_SCALE = 255.0F;
   private static final float PICKER_OPEN_OFFSET_Y = 10.0F;
   private static final float PICKER_FOOTER_HEIGHT = 25.0F;
   private static final int HUE_GRADIENT_OPAQUE_ALPHA_MASK = -16777216;
   private static final int HUE_GRADIENT_RGB_MASK = 16777215;
   private static final int PICKER_COLOR_OPAQUE_ALPHA_MASK = -16777216;
   private static final float PICKER_SHADOW_WIDTH = 150.0F;
   private static final float PICKER_SHADOW_RADIUS = 16.0F;
   private static final float PICKER_SHADOW_ALPHA_FACTOR = 0.3F;
   private static final float PICKER_BACKGROUND_WIDTH = 150.0F;
   private static final float PICKER_BACKGROUND_CORNER_RADIUS = 6.0F;
   private static final float PICKER_OUTLINE_WIDTH = 150.0F;
   private static final float PICKER_OUTLINE_CORNER_RADIUS = 6.0F;
   private static final float PICKER_OUTLINE_COLOR_MIX_FACTOR = 0.35F;
   private static final float SATURATION_BRIGHTNESS_OFFSET_X = 6.0F;
   private static final float SATURATION_BRIGHTNESS_OFFSET_Y = 6.0F;
   private static final float SATURATION_BRIGHTNESS_WIDTH = 138.0F;
   private static final float SATURATION_BRIGHTNESS_HEIGHT = 80.0F;
   private static final float SATURATION_BRIGHTNESS_CORNER_RADIUS = 3.0F;
   private static final int SATURATION_BRIGHTNESS_BOTTOM_LEFT_COLOR = -16777216;
   private static final int SATURATION_BRIGHTNESS_BOTTOM_RIGHT_COLOR = -16777216;
   private static final float HUE_TRACK_BASE_HEIGHT = 6.0F;
   private static final float HUE_TRACK_OFFSET_X = 6.0F;
   private static final float HUE_TRACK_OFFSET_Y = 92.0F;
   private static final float HUE_TRACK_CENTERING_BASE_HEIGHT = 6.0F;
   private static final float HUE_TRACK_WIDTH = 138.0F;
   private static final double HUE_TRACK_TEXTURE_ALPHA = 255.0;
   private static final String HUE_TEXTURE_PATH = "hud/hue";
   private static final int ALPHA_TRACK_TRANSPARENT_RGB_MASK = 16777215;
   private static final float ALPHA_TRACK_BASE_HEIGHT = 6.0F;
   private static final float ALPHA_TRACK_OFFSET_X = 6.0F;
   private static final float ALPHA_TRACK_OFFSET_Y = 103.0F;
   private static final float ALPHA_TRACK_CENTERING_BASE_HEIGHT = 6.0F;
   private static final float ALPHA_TRACK_WIDTH = 138.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_OFFSET_X = 6.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_X_TRAVEL = 138.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_OFFSET_Y = 6.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_Y_TRAVEL = 80.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_BASE_SIZE = 12.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_DRAG_SIZE_GAIN = 4.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_SHADOW_RADIUS = 6.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_SHADOW_BASE_ALPHA = 0.4F;
   private static final float SATURATION_BRIGHTNESS_MARKER_SHADOW_DRAG_ALPHA = 0.3F;
   private static final float SATURATION_BRIGHTNESS_MARKER_INNER_WIDTH_INSET = 4.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_INNER_HEIGHT_INSET = 4.0F;
   private static final float SATURATION_BRIGHTNESS_MARKER_OUTLINE_THICKNESS = 1.5F;
   private static final float HUE_MARKER_OFFSET_X = 6.0F;
   private static final float HUE_MARKER_X_TRAVEL = 138.0F;
   private static final float HUE_MARKER_BASE_WIDTH = 3.0F;
   private static final float HUE_MARKER_BASE_HEIGHT = 10.0F;
   private static final float HUE_MARKER_SHADOW_VISIBILITY_THRESHOLD = 0.01F;
   private static final float HUE_MARKER_SHADOW_OFFSET_Y = 90.0F;
   private static final float HUE_MARKER_SHADOW_RADIUS = 5.0F;
   private static final float HUE_MARKER_SHADOW_ALPHA_FACTOR = 0.6F;
   private static final float HUE_MARKER_OFFSET_Y = 90.0F;
   private static final float ALPHA_MARKER_OFFSET_X = 6.0F;
   private static final float ALPHA_MARKER_X_TRAVEL = 138.0F;
   private static final float ALPHA_MARKER_BASE_WIDTH = 3.0F;
   private static final float ALPHA_MARKER_BASE_HEIGHT = 10.0F;
   private static final float ALPHA_MARKER_SHADOW_VISIBILITY_THRESHOLD = 0.01F;
   private static final float ALPHA_MARKER_SHADOW_OFFSET_Y = 101.0F;
   private static final float ALPHA_MARKER_SHADOW_RADIUS = 5.0F;
   private static final float ALPHA_MARKER_SHADOW_ALPHA_FACTOR = 0.6F;
   private static final float ALPHA_MARKER_OFFSET_Y = 101.0F;
   private static final float COPY_BUTTON_OFFSET_X = 6.0F;
   private static final float COPY_BUTTON_WIDTH = 66.0F;
   private static final String COPY_BUTTON_LABEL = "Копировать";
   private static final float PASTE_BUTTON_OFFSET_X = 78.0F;
   private static final float PASTE_BUTTON_WIDTH = 66.0F;
   private static final String PASTE_BUTTON_LABEL = "Вставить";
   private static final float PICKER_TOGGLE_PREVIEW_SCALE = 0.72F;
   private static final float RESET_PREVIEW_SCALE = 0.82F;
   private static final float SATURATION_BRIGHTNESS_HITBOX_OFFSET_X = 6.0F;
   private static final float SATURATION_BRIGHTNESS_HITBOX_OFFSET_Y = 6.0F;
   private static final double SATURATION_BRIGHTNESS_HITBOX_WIDTH = 138.0;
   private static final double SATURATION_BRIGHTNESS_HITBOX_HEIGHT = 80.0;
   private static final float HUE_HITBOX_OFFSET_X = 6.0F;
   private static final float HUE_HITBOX_OFFSET_Y = 92.0F;
   private static final double HUE_HITBOX_WIDTH = 138.0;
   private static final double HUE_HITBOX_HEIGHT = 6.0;
   private static final float ALPHA_HITBOX_OFFSET_X = 6.0F;
   private static final float ALPHA_HITBOX_OFFSET_Y = 103.0F;
   private static final double ALPHA_HITBOX_WIDTH = 138.0;
   private static final double ALPHA_HITBOX_HEIGHT = 6.0;
   private static final float COPY_BUTTON_HITBOX_OFFSET_X = 6.0F;
   private static final double COPY_BUTTON_HITBOX_WIDTH = 66.0;
   private static final double COPY_BUTTON_HITBOX_HEIGHT = 18.0;
   private static final float PASTE_BUTTON_HITBOX_OFFSET_X = 78.0F;
   private static final double PASTE_BUTTON_HITBOX_WIDTH = 66.0;
   private static final double PASTE_BUTTON_HITBOX_HEIGHT = 18.0;
   private static final float PREVIEW_HITBOX_RIGHT_OFFSET = 16.0F;
   private static final double PREVIEW_HITBOX_WIDTH = 16.0;
   private static final double PREVIEW_HITBOX_HEIGHT = 16.0;
   private static final float SATURATION_INPUT_OFFSET_X = 6.0F;
   private static final float SATURATION_INPUT_WIDTH = 138.0F;
   private static final float BRIGHTNESS_INPUT_OFFSET_Y = 6.0F;
   private static final float BRIGHTNESS_INPUT_HEIGHT = 80.0F;
   private static final float HUE_INPUT_OFFSET_X = 6.0F;
   private static final float HUE_INPUT_WIDTH = 138.0F;
   private static final float ALPHA_INPUT_OFFSET_X = 6.0F;
   private static final float ALPHA_INPUT_WIDTH = 138.0F;
   private static final int APPLY_COLOR_RGB_MASK = 16777215;
   private static final float ALPHA_CHANNEL_SCALE = 255.0F;
   private static final float ALPHA_FRACTION_DIVISOR = 255.0F;
   private static final float PICKER_RIGHT_OFFSET = 112.0F;
   private static final float PICKER_CLAMP_WIDTH = 150.0F;
   private static final float PICKER_BELOW_WIDGET_OFFSET = 24.0F;
   private static final float PICKER_CLAMP_FOOTER_HEIGHT = 25.0F;
   private static final float ALPHA_ENABLED_FOOTER_OFFSET_Y = 114.0F;
   private static final float RGB_ONLY_FOOTER_OFFSET_Y = 103.0F;
   private static final double BUTTON_HOVER_HEIGHT = 18.0;
   private static final float BUTTON_HOVER_BACKGROUND_MIX = 0.65F;
   private static final float BUTTON_HOVER_TEXT_MIX = 0.8F;
   private static final float BUTTON_HEIGHT = 18.0F;
   private static final float BUTTON_CORNER_RADIUS = 4.0F;
   private static final float BUTTON_TEXT_OFFSET_Y = 4.0F;
   private static final String COPY_RGBA_HEX_FORMAT = "#%02X%02X%02X%02X";
   private static final String COPY_RGB_HEX_FORMAT = "#%02X%02X%02X";
   private static final float PASTE_PREVIEW_SCALE = 0.88F;
   private static final String PASTE_IGNORED_UNDERSCORE = "_";
   private static final String PASTE_IGNORED_SPACE = " ";
   private static final String PASTE_ARGB_PREFIX = "0X";
   private static final String PASTE_ARGB_HEX_PATTERN = "[0-9A-F]{8}";
   private static final int PASTE_OPAQUE_ALPHA_MASK = -16777216;
   private static final int PASTE_RGB_MASK = 16777215;
   private static final String PASTE_HASH_PREFIX = "#";
   private static final String PASTE_RGB_OR_RGBA_HEX_PATTERN = "[0-9A-F]{6}|[0-9A-F]{8}";
   private static final double PICKER_HITBOX_WIDTH = 150.0;
   private static final float PICKER_HITBOX_FOOTER_HEIGHT = 25.0F;

   @Override
   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
      this.pickerVisibilityAnimation.updateTarget(this.pickerOpen ? 1.0F : 0.0F);
      float pickerVisibility = this.pickerVisibilityAnimation.getValue();
      if (!(pickerVisibility <= PICKER_VISIBILITY_THRESHOLD)) {
         UiDrawing.setGlobalAlpha(PICKER_GLOBAL_ALPHA_SCALE * pickerVisibility);
         if (this.draggingSaturationBrightness) {
            this.updateSaturationBrightnessFromMouse(mouseX, mouseY);
         }

         if (this.draggingHue) {
            this.updateHueFromMouse(mouseX);
         }

         if (this.draggingAlpha) {
            this.updateAlphaFromMouse(mouseX);
         }

         this.saturationBrightnessDragAnimation.updateTarget(this.draggingSaturationBrightness ? 1.0F : 0.0F);
         this.hueDragAnimation.updateTarget(this.draggingHue ? 1.0F : 0.0F);
         this.alphaDragAnimation.updateTarget(this.draggingAlpha ? 1.0F : 0.0F);
         float popupX = this.getPickerX();
         float animatedPopupY = this.getPickerY() - PICKER_OPEN_OFFSET_Y * (1.0F - pickerVisibility);
         float footerY = animatedPopupY + this.getPickerFooterOffsetY();
         float popupHeight = this.getPickerFooterOffsetY() + PICKER_FOOTER_HEIGHT;
         int hueArgb = HUE_GRADIENT_OPAQUE_ALPHA_MASK | Color.HSBtoRGB(this.hue, 1.0F, 1.0F) & HUE_GRADIENT_RGB_MASK;
         int opaqueColorArgb = this.setting.getValue() | PICKER_COLOR_OPAQUE_ALPHA_MASK;
         UiDrawing.drawShadow(popupX, animatedPopupY, PICKER_SHADOW_WIDTH, popupHeight, PICKER_SHADOW_RADIUS, ColorMath.withAlpha(opaqueColorArgb, PICKER_SHADOW_ALPHA_FACTOR * pickerVisibility));
         UiDrawing.drawRoundedRect(popupX, animatedPopupY, PICKER_BACKGROUND_WIDTH, popupHeight, PICKER_BACKGROUND_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
         UiDrawing.drawOutline(popupX, animatedPopupY, PICKER_OUTLINE_WIDTH, popupHeight, 1.0F, PICKER_OUTLINE_CORNER_RADIUS, ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE), opaqueColorArgb, PICKER_OUTLINE_COLOR_MIX_FACTOR));
         UiDrawing.drawRoundedRect(popupX + SATURATION_BRIGHTNESS_OFFSET_X, animatedPopupY + SATURATION_BRIGHTNESS_OFFSET_Y, SATURATION_BRIGHTNESS_WIDTH, SATURATION_BRIGHTNESS_HEIGHT, SATURATION_BRIGHTNESS_CORNER_RADIUS, new Vector4i(SATURATION_BRIGHTNESS_BOTTOM_LEFT_COLOR, -1, SATURATION_BRIGHTNESS_BOTTOM_RIGHT_COLOR, hueArgb));
         float hueTrackHeight = HUE_TRACK_BASE_HEIGHT + this.hueDragAnimation.getValue() * 2.0F;
         UiDrawing.drawTexture(
            popupX + HUE_TRACK_OFFSET_X,
            animatedPopupY + HUE_TRACK_OFFSET_Y - (hueTrackHeight - HUE_TRACK_CENTERING_BASE_HEIGHT) / 2.0F,
            HUE_TRACK_WIDTH,
            hueTrackHeight,
            new Vector4f(0.0F),
            ColorMath.packArgb(255, 255, 255, HUE_TRACK_TEXTURE_ALPHA),
            ClientResourceIds.getImageId(HUE_TEXTURE_PATH)
         );
         if (this.setting.isAlphaEnabled()) {
            int transparentColorRgb = this.setting.getValue() & ALPHA_TRACK_TRANSPARENT_RGB_MASK;
            float alphaTrackHeight = ALPHA_TRACK_BASE_HEIGHT + this.alphaDragAnimation.getValue() * 2.0F;
            UiDrawing.drawRoundedRect(popupX + ALPHA_TRACK_OFFSET_X, animatedPopupY + ALPHA_TRACK_OFFSET_Y - (alphaTrackHeight - ALPHA_TRACK_CENTERING_BASE_HEIGHT) / 2.0F, ALPHA_TRACK_WIDTH, alphaTrackHeight, 2.0F, new Vector4i(opaqueColorArgb, opaqueColorArgb, transparentColorRgb, transparentColorRgb));
         }

         float saturationBrightnessMarkerX = popupX + SATURATION_BRIGHTNESS_MARKER_OFFSET_X + this.saturation * SATURATION_BRIGHTNESS_MARKER_X_TRAVEL;
         float saturationBrightnessMarkerY = animatedPopupY + SATURATION_BRIGHTNESS_MARKER_OFFSET_Y + (1.0F - this.brightness) * SATURATION_BRIGHTNESS_MARKER_Y_TRAVEL;
         float saturationBrightnessMarkerSize = SATURATION_BRIGHTNESS_MARKER_BASE_SIZE + this.saturationBrightnessDragAnimation.getValue() * SATURATION_BRIGHTNESS_MARKER_DRAG_SIZE_GAIN;
         float saturationBrightnessMarkerHalfSize = saturationBrightnessMarkerSize / 2.0F;
         UiDrawing.drawShadow(saturationBrightnessMarkerX - saturationBrightnessMarkerHalfSize, saturationBrightnessMarkerY - saturationBrightnessMarkerHalfSize, saturationBrightnessMarkerSize, saturationBrightnessMarkerSize, SATURATION_BRIGHTNESS_MARKER_SHADOW_RADIUS, ColorMath.withAlpha(opaqueColorArgb, SATURATION_BRIGHTNESS_MARKER_SHADOW_BASE_ALPHA + SATURATION_BRIGHTNESS_MARKER_SHADOW_DRAG_ALPHA * this.saturationBrightnessDragAnimation.getValue()));
         UiDrawing.drawRoundedRect(saturationBrightnessMarkerX - saturationBrightnessMarkerHalfSize + 2.0F, saturationBrightnessMarkerY - saturationBrightnessMarkerHalfSize + 2.0F, saturationBrightnessMarkerSize - SATURATION_BRIGHTNESS_MARKER_INNER_WIDTH_INSET, saturationBrightnessMarkerSize - SATURATION_BRIGHTNESS_MARKER_INNER_HEIGHT_INSET, saturationBrightnessMarkerHalfSize - 2.0F, opaqueColorArgb);
         UiDrawing.drawOutline(saturationBrightnessMarkerX - saturationBrightnessMarkerHalfSize, saturationBrightnessMarkerY - saturationBrightnessMarkerHalfSize, saturationBrightnessMarkerSize, saturationBrightnessMarkerSize, SATURATION_BRIGHTNESS_MARKER_OUTLINE_THICKNESS, saturationBrightnessMarkerHalfSize - 1.0F, -1);
         float hueMarkerX = popupX + HUE_MARKER_OFFSET_X + this.hue * HUE_MARKER_X_TRAVEL;
         float hueMarkerWidth = HUE_MARKER_BASE_WIDTH + this.hueDragAnimation.getValue() * 2.0F;
         float hueMarkerHeight = HUE_MARKER_BASE_HEIGHT + this.hueDragAnimation.getValue() * 2.0F;
         if (this.hueDragAnimation.getValue() > HUE_MARKER_SHADOW_VISIBILITY_THRESHOLD) {
            UiDrawing.drawShadow(
               hueMarkerX - hueMarkerWidth / 2.0F, animatedPopupY + HUE_MARKER_SHADOW_OFFSET_Y - this.hueDragAnimation.getValue(), hueMarkerWidth, hueMarkerHeight, HUE_MARKER_SHADOW_RADIUS, ColorMath.withAlpha(hueArgb, HUE_MARKER_SHADOW_ALPHA_FACTOR * this.hueDragAnimation.getValue())
            );
         }

         UiDrawing.drawRoundedRect(hueMarkerX - hueMarkerWidth / 2.0F, animatedPopupY + HUE_MARKER_OFFSET_Y - this.hueDragAnimation.getValue(), hueMarkerWidth, hueMarkerHeight, hueMarkerWidth / 2.0F, -1);
         if (this.setting.isAlphaEnabled()) {
            float alphaMarkerX = popupX + ALPHA_MARKER_OFFSET_X + (1.0F - this.alphaFraction) * ALPHA_MARKER_X_TRAVEL;
            float alphaMarkerWidth = ALPHA_MARKER_BASE_WIDTH + this.alphaDragAnimation.getValue() * 2.0F;
            float alphaMarkerHeight = ALPHA_MARKER_BASE_HEIGHT + this.alphaDragAnimation.getValue() * 2.0F;
            if (this.alphaDragAnimation.getValue() > ALPHA_MARKER_SHADOW_VISIBILITY_THRESHOLD) {
               UiDrawing.drawShadow(
                  alphaMarkerX - alphaMarkerWidth / 2.0F, animatedPopupY + ALPHA_MARKER_SHADOW_OFFSET_Y - this.alphaDragAnimation.getValue(), alphaMarkerWidth, alphaMarkerHeight, ALPHA_MARKER_SHADOW_RADIUS, ColorMath.withAlpha(opaqueColorArgb, ALPHA_MARKER_SHADOW_ALPHA_FACTOR * this.alphaDragAnimation.getValue())
               );
            }

            UiDrawing.drawRoundedRect(alphaMarkerX - alphaMarkerWidth / 2.0F, animatedPopupY + ALPHA_MARKER_OFFSET_Y - this.alphaDragAnimation.getValue(), alphaMarkerWidth, alphaMarkerHeight, alphaMarkerWidth / 2.0F, -1);
         }

         this.renderClipboardButton(mouseX, mouseY, popupX + COPY_BUTTON_OFFSET_X, footerY, COPY_BUTTON_WIDTH, ClientLocalization.translate(COPY_BUTTON_LABEL), this.copyButtonFlashAnimation.getValue());
         this.renderClipboardButton(mouseX, mouseY, popupX + PASTE_BUTTON_OFFSET_X, footerY, PASTE_BUTTON_WIDTH, ClientLocalization.translate(PASTE_BUTTON_LABEL), this.pasteButtonFlashAnimation.getValue());
         UiDrawing.resetGlobalAlpha();
      }
   }

   private float getPickerY() {
      return this.clampPopupY(this.y + PICKER_BELOW_WIDGET_OFFSET, this.getPickerFooterOffsetY() + PICKER_CLAMP_FOOTER_HEIGHT);
   }

   public boolean isPreviewHovered(float mouseX, float mouseY) {
      return WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - PREVIEW_HITBOX_RIGHT_OFFSET, this.y + 1.0F, PREVIEW_HITBOX_WIDTH, PREVIEW_HITBOX_HEIGHT);
   }

   private void updateSaturationBrightnessFromMouse(float mouseX, float mouseY) {
      this.saturation = Math.clamp((mouseX - this.getPickerX() - SATURATION_INPUT_OFFSET_X) / SATURATION_INPUT_WIDTH, 0.0F, 1.0F);
      this.brightness = 1.0F - Math.clamp((mouseY - this.getPickerY() - BRIGHTNESS_INPUT_OFFSET_Y) / BRIGHTNESS_INPUT_HEIGHT, 0.0F, 1.0F);
      this.applyComponentsToSetting();
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.previewHoverOrPickerOpenAnimation.updateTarget(!this.isPreviewHovered(mouseX, mouseY) && !this.pickerOpen ? 0.0F : 1.0F);
      this.previewScaleAnimation.updateTarget(1.0F);
      this.colorChangeFlashAnimation.updateTarget(0.0F);
      this.resetFlashAnimation.updateTarget(0.0F);
      this.copyButtonFlashAnimation.updateTarget(0.0F);
      this.pasteButtonFlashAnimation.updateTarget(0.0F);
      if (this.setting.getValue() != this.lastColorArgb) {
         this.lastColorArgb = this.setting.getValue();
         this.colorChangeFlashAnimation.setImmediately(1.0F);
      }

      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y + 2.0F, 12, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      Color color = new Color(this.setting.getValue(), true);
      String rgbaHexText = String.format(DISPLAY_RGBA_HEX_FORMAT, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
      int opaqueColorArgb = this.setting.getValue() | DISPLAY_OPAQUE_ALPHA_MASK;
      int hexTextArgb = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), opaqueColorArgb, DISPLAY_HEX_COLOR_MIX_FACTOR * this.pickerVisibilityAnimation.getValue());
      hexTextArgb = ColorMath.lerpArgb(hexTextArgb, ColorMath.packArgb(255, 255, 255, DISPLAY_HEX_FLASH_WHITE_ALPHA), this.colorChangeFlashAnimation.getValue() * DISPLAY_HEX_FLASH_MIX_FACTOR);
      UiDrawing.drawText(
         ClientFonts.getMediumFont(), rgbaHexText, this.x + this.width - HEX_TEXT_RIGHT_MARGIN - ClientFonts.getMediumFont().getFontFace().measureTextWidth(rgbaHexText, HEX_TEXT_MEASURE_FONT_SIZE, 0.0F), this.y + 2.0F, 12, hexTextArgb
      );
      float previewPulse = (float)(Math.sin(System.currentTimeMillis() / PREVIEW_PULSE_TIME_DIVISOR_MILLIS) * PREVIEW_PULSE_AMPLITUDE + PREVIEW_PULSE_OFFSET);
      float previewExpansion = this.previewHoverOrPickerOpenAnimation.getValue() * PREVIEW_HOVER_OR_PICKER_OPEN_EXPANSION_GAIN + (this.previewScaleAnimation.getValue() - 1.0F) * PREVIEW_SCALE_EXPANSION_GAIN;
      float previewSize = PREVIEW_BASE_SIZE + previewExpansion * 2.0F;
      float previewX = this.x + this.width - previewSize;
      float previewY = this.y + 1.0F - previewExpansion;
      float previewCornerRadius = PREVIEW_BASE_CORNER_RADIUS + previewExpansion * PREVIEW_EXPANSION_RADIUS_FACTOR;
      float previewShadowAlpha = Math.max(this.previewHoverOrPickerOpenAnimation.getValue() * PREVIEW_HOVER_OR_PICKER_OPEN_SHADOW_ALPHA_FACTOR, this.pickerVisibilityAnimation.getValue() * (PREVIEW_OPEN_SHADOW_BASE_ALPHA + PREVIEW_OPEN_SHADOW_PULSE_ALPHA * previewPulse));
      previewShadowAlpha = Math.max(previewShadowAlpha, this.colorChangeFlashAnimation.getValue() * PREVIEW_CHANGE_SHADOW_ALPHA_FACTOR);
      if (previewShadowAlpha > PREVIEW_SHADOW_VISIBILITY_THRESHOLD) {
         UiDrawing.drawShadow(previewX, previewY, previewSize, previewSize, PREVIEW_SHADOW_RADIUS, ColorMath.withAlpha(opaqueColorArgb, previewShadowAlpha));
      }

      UiDrawing.drawRoundedRect(previewX, previewY, previewSize, previewSize, previewCornerRadius, this.setting.getValue());
      if (this.resetFlashAnimation.getValue() > RESET_FLASH_VISIBILITY_THRESHOLD) {
         UiDrawing.drawRoundedRect(previewX, previewY, previewSize, previewSize, previewCornerRadius, ColorMath.withAlpha(ColorMath.packArgb(255, 255, 255, RESET_FLASH_WHITE_ALPHA), this.resetFlashAnimation.getValue() * RESET_FLASH_ALPHA_FACTOR));
      }

      this.height = ELEMENT_HEIGHT;
   }

   private void renderClipboardButton(float mouseX, float mouseY, float x, float y, float width, String label, float flashProgress) {
      boolean hovered = WorldProjection.containsPoint(mouseX, mouseY, x, y, width, BUTTON_HOVER_HEIGHT);
      float backgroundMix = Math.max(hovered ? BUTTON_HOVER_BACKGROUND_MIX : 0.0F, flashProgress);
      int backgroundArgb = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_STATE), backgroundMix);
      int textArgb = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), Math.max(hovered ? BUTTON_HOVER_TEXT_MIX : 0.0F, flashProgress));
      UiDrawing.drawRoundedRect(x, y, width, BUTTON_HEIGHT, BUTTON_CORNER_RADIUS, backgroundArgb);
      float labelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), label, 9);
      UiDrawing.drawText(ClientFonts.getMediumFont(), label, x + (width - labelWidth) / 2.0F, y + BUTTON_TEXT_OFFSET_Y, 9, textArgb);
   }

   private boolean isControlDown() {
      long windowHandle = Minecraft.getInstance().getWindow().handle();
      return GLFW.glfwGetKey(windowHandle, 341) == 1 || GLFW.glfwGetKey(windowHandle, 345) == 1;
   }

   @Override
   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      if (!this.pickerOpen) {
         return false;
      }

      if (!this.isPickerHovered(mouseX, mouseY)) {
         this.resetTransientState();
         return true;
      }

      if (button != 0) {
         return true;
      }

      float popupX = this.getPickerX();
      float popupY = this.getPickerY();
      this.draggingSaturationBrightness = false;
      this.draggingHue = false;
      this.draggingAlpha = false;
      if (WorldProjection.containsPoint(mouseX, mouseY, popupX + SATURATION_BRIGHTNESS_HITBOX_OFFSET_X, popupY + SATURATION_BRIGHTNESS_HITBOX_OFFSET_Y, SATURATION_BRIGHTNESS_HITBOX_WIDTH, SATURATION_BRIGHTNESS_HITBOX_HEIGHT)) {
         this.draggingSaturationBrightness = true;
         this.updateSaturationBrightnessFromMouse(mouseX, mouseY);
      } else if (WorldProjection.containsPoint(mouseX, mouseY, popupX + HUE_HITBOX_OFFSET_X, popupY + HUE_HITBOX_OFFSET_Y, HUE_HITBOX_WIDTH, HUE_HITBOX_HEIGHT)) {
         this.draggingHue = true;
         this.updateHueFromMouse(mouseX);
      } else if (this.setting.isAlphaEnabled() && WorldProjection.containsPoint(mouseX, mouseY, popupX + ALPHA_HITBOX_OFFSET_X, popupY + ALPHA_HITBOX_OFFSET_Y, ALPHA_HITBOX_WIDTH, ALPHA_HITBOX_HEIGHT)) {
         this.draggingAlpha = true;
         this.updateAlphaFromMouse(mouseX);
      } else if (WorldProjection.containsPoint(mouseX, mouseY, popupX + COPY_BUTTON_HITBOX_OFFSET_X, popupY + this.getPickerFooterOffsetY(), COPY_BUTTON_HITBOX_WIDTH, COPY_BUTTON_HITBOX_HEIGHT)) {
         this.copyColorToClipboard();
      } else if (WorldProjection.containsPoint(mouseX, mouseY, popupX + PASTE_BUTTON_HITBOX_OFFSET_X, popupY + this.getPickerFooterOffsetY(), PASTE_BUTTON_HITBOX_WIDTH, PASTE_BUTTON_HITBOX_HEIGHT)) {
         this.pasteColorFromClipboard();
      }

      return true;
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (this.draggingSaturationBrightness || this.draggingHue || this.draggingAlpha) {
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_RELEASE);
      }

      if (button == 0) {
         this.draggingSaturationBrightness = false;
         this.draggingHue = false;
         this.draggingAlpha = false;
      }
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.pickerOpen && this.isControlDown()) {
         if (keyCode == 67) {
            this.copyColorToClipboard();
         } else if (keyCode == 86) {
            this.pasteColorFromClipboard();
         }
      }
   }

   private void copyColorToClipboard() {
      Color color = new Color(this.setting.getValue(), true);
      String hexText = this.setting.isAlphaEnabled()
         ? String.format(COPY_RGBA_HEX_FORMAT, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
         : String.format(COPY_RGB_HEX_FORMAT, color.getRed(), color.getGreen(), color.getBlue());
      GLFW.glfwSetClipboardString(Minecraft.getInstance().getWindow().handle(), hexText);
      this.copyButtonFlashAnimation.setImmediately(1.0F);
   }

   private void syncComponentsFromSetting() {
      Color color = new Color(this.setting.getValue(), true);
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      this.hue = hsb[0];
      this.saturation = hsb[1];
      this.brightness = hsb[2];
      this.alphaFraction = this.setting.isAlphaEnabled() ? color.getAlpha() / ALPHA_FRACTION_DIVISOR : 1.0F;
   }

   private Integer parseClipboardColor(String clipboardText) {
      if (clipboardText == null) {
         return null;
      }

      String normalizedText = clipboardText.trim().replace(PASTE_IGNORED_UNDERSCORE, "").replace(PASTE_IGNORED_SPACE, "").toUpperCase(Locale.ROOT);
      if (normalizedText.startsWith(PASTE_ARGB_PREFIX)) {
         String argbHex = normalizedText.substring(2);
         if (!argbHex.matches(PASTE_ARGB_HEX_PATTERN)) {
            return null;
         }

         int argb = (int)Long.parseLong(argbHex, 16);
         return this.setting.isAlphaEnabled() ? argb : PASTE_OPAQUE_ALPHA_MASK | argb & PASTE_RGB_MASK;
      } else {
         if (normalizedText.startsWith(PASTE_HASH_PREFIX)) {
            normalizedText = normalizedText.substring(1);
         }

         if (!normalizedText.matches(PASTE_RGB_OR_RGBA_HEX_PATTERN)) {
            return null;
         } else if (normalizedText.length() == 6) {
            int rgb = Integer.parseInt(normalizedText, 16);
            int retainedAlpha = this.setting.isAlphaEnabled() ? this.setting.getValue() >>> 24 & 0xFF : 255;
            return retainedAlpha << 24 | rgb;
         } else {
            int red = Integer.parseInt(normalizedText.substring(0, 2), 16);
            int green = Integer.parseInt(normalizedText.substring(2, 4), 16);
            int blue = Integer.parseInt(normalizedText.substring(4, 6), 16);
            int pastedAlpha = Integer.parseInt(normalizedText.substring(6, 8), 16);
            int effectiveAlpha = this.setting.isAlphaEnabled() ? pastedAlpha : 255;
            return effectiveAlpha << 24 | red << 16 | green << 8 | blue;
         }
      }
   }

   private void applyComponentsToSetting() {
      int rgb = Color.HSBtoRGB(this.hue, this.saturation, this.brightness) & APPLY_COLOR_RGB_MASK;
      int alphaChannel = this.setting.isAlphaEnabled() ? Math.round(this.alphaFraction * ALPHA_CHANNEL_SCALE) : 255;
      int previousColorArgb = this.setting.getValue();
      this.setting.setValue(alphaChannel << 24 | rgb);
      if (previousColorArgb != this.setting.getValue()) {
         ClientSounds.к(ClientSounds.GuiSound.COLOR, this.draggingHue ? this.hue : (this.draggingAlpha ? 1.0F - this.alphaFraction : this.saturation));
      }
   }

   @Override
   public void resetTransientState() {
      this.pickerOpen = false;
      this.draggingSaturationBrightness = false;
      this.draggingHue = false;
      this.draggingAlpha = false;
   }

   private void updateHueFromMouse(float mouseX) {
      this.hue = Math.clamp((mouseX - this.getPickerX() - HUE_INPUT_OFFSET_X) / HUE_INPUT_WIDTH, 0.0F, 1.0F);
      this.applyComponentsToSetting();
   }

   @Override
   public void tick() {
   }

   private boolean isPickerHovered(float mouseX, float mouseY) {
      return WorldProjection.containsPoint(mouseX, mouseY, this.getPickerX(), this.getPickerY(), PICKER_HITBOX_WIDTH, this.getPickerFooterOffsetY() + PICKER_HITBOX_FOOTER_HEIGHT);
   }

   private void pasteColorFromClipboard() {
      String clipboardText = GLFW.glfwGetClipboardString(Minecraft.getInstance().getWindow().handle());
      Integer parsedColorArgb = this.parseClipboardColor(clipboardText);
      if (parsedColorArgb != null) {
         this.setting.setValue(parsedColorArgb);
         this.syncComponentsFromSetting();
         this.lastColorArgb = parsedColorArgb;
         this.colorChangeFlashAnimation.setImmediately(1.0F);
         this.pasteButtonFlashAnimation.setImmediately(1.0F);
         this.previewScaleAnimation.setImmediately(PASTE_PREVIEW_SCALE);
      }
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (this.isPreviewHovered(mouseX, mouseY)) {
         if (button == 0) {
            this.pickerOpen = !this.pickerOpen;
            ClientSounds.к(this.pickerOpen ? ClientSounds.GuiSound.PICKER_OPEN : ClientSounds.GuiSound.PICKER_CLOSE);
            this.draggingSaturationBrightness = false;
            this.draggingHue = false;
            this.draggingAlpha = false;
            this.previewScaleAnimation.setImmediately(PICKER_TOGGLE_PREVIEW_SCALE);
            if (this.pickerOpen) {
               this.syncComponentsFromSetting();
            }
         } else if (button == 1) {
            this.setting.setValue(this.setting.getDefaultColor());
            this.syncComponentsFromSetting();
            this.resetFlashAnimation.setImmediately(1.0F);
            this.previewScaleAnimation.setImmediately(RESET_PREVIEW_SCALE);
         }
      } else {
         if (this.pickerOpen && !this.isPickerHovered(mouseX, mouseY)) {
            this.pickerOpen = false;
            this.draggingSaturationBrightness = false;
            this.draggingHue = false;
            this.draggingAlpha = false;
         }
      }
   }

   private float getPickerX() {
      return this.clampPopupX(this.x + this.width - PICKER_RIGHT_OFFSET, PICKER_CLAMP_WIDTH);
   }

   private float getPickerFooterOffsetY() {
      return this.setting.isAlphaEnabled() ? ALPHA_ENABLED_FOOTER_OFFSET_Y : RGB_ONLY_FOOTER_OFFSET_Y;
   }

   private void updateAlphaFromMouse(float mouseX) {
      this.alphaFraction = 1.0F - Math.clamp((mouseX - this.getPickerX() - ALPHA_INPUT_OFFSET_X) / ALPHA_INPUT_WIDTH, 0.0F, 1.0F);
      this.applyComponentsToSetting();
   }

   public ColorSettingWidget(ColorSetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.pickerVisibilityAnimation = new FloatAnimation(0.0F, PICKER_VISIBILITY_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.previewHoverOrPickerOpenAnimation = new FloatAnimation(0.0F, PREVIEW_HOVER_OR_PICKER_OPEN_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.previewScaleAnimation = new FloatAnimation(1.0F, PREVIEW_SCALE_ANIMATION_SPEED, Easings.ELASTIC_OUT);
      this.colorChangeFlashAnimation = new FloatAnimation(0.0F, COLOR_CHANGE_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.resetFlashAnimation = new FloatAnimation(0.0F, RESET_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.copyButtonFlashAnimation = new FloatAnimation(0.0F, COPY_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.pasteButtonFlashAnimation = new FloatAnimation(0.0F, PASTE_FLASH_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.saturationBrightnessDragAnimation = new FloatAnimation(0.0F, SATURATION_BRIGHTNESS_DRAG_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.hueDragAnimation = new FloatAnimation(0.0F, HUE_DRAG_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.alphaDragAnimation = new FloatAnimation(0.0F, ALPHA_DRAG_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.syncComponentsFromSetting();
      this.lastColorArgb = setting.getValue();
   }
}
