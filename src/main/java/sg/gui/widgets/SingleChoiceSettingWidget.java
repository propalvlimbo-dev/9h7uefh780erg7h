package sg.gui.widgets;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Vector3f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import settings.render.ClientLocalization;
import settings.render.ReallyWorldRankTextures;
import settings.render.ReallyWorldRankGlyphs;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.network.NameProtectionTextRewriter;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.impl.misc.ClientSounds;
import sg.gui.click.ClickGuiScreen;
import sg.modules.impl.init.KeybindConflictResolver;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class SingleChoiceSettingWidget extends SettingWidget<SingleChoiceSetting> {
   private static final int ДИ = 5;
   private static final float Ъ_ = 26.0F;
   private static final float ЪШ = 22.0F;
   private static final float Ъы = 4.0F;
   private static final float Ъй = 42.0F;
   private final @com.google.gson.annotations.SerializedName("сР")  FloatAnimation popupOpenAnimation;
   private final @com.google.gson.annotations.SerializedName("с3")  FloatAnimation scrollAnimation;
   private @com.google.gson.annotations.SerializedName("ею")  boolean popupOpen;
   private @com.google.gson.annotations.SerializedName("Х")  ChoiceOption pendingKeybindOption;
   private @com.google.gson.annotations.SerializedName("ЪЖ")  float scrollOffset;
   private @com.google.gson.annotations.SerializedName("Ъм")  float targetScrollOffset;
   private @com.google.gson.annotations.SerializedName("еЩ")  boolean scrollbarDragging;
   private @com.google.gson.annotations.SerializedName("ЪК")  float scrollbarGrabOffset;
   private static final float INITIAL_HEIGHT = 24.0F;
   private static final float POPUP_OPEN_ANIMATION_SPEED = 10.0F;
   private static final float SCROLL_ANIMATION_SPEED = 14.0F;
   private static final float ELEMENT_HEIGHT = 24.0F;
   private static final float SETTING_LABEL_OFFSET_Y = 5.0F;
   private static final float SELECTED_VALUE_RIGHT_OFFSET = 100.0F;
   private static final float SELECTED_VALUE_BOX_WIDTH = 100.0F;
   private static final float SELECTED_VALUE_BOX_CORNER_RADIUS = 6.0F;
   private static final float SELECTED_RANK_SCISSOR_RIGHT_OFFSET = 96.0F;
   private static final float SELECTED_RANK_SCISSOR_WIDTH = 72.0F;
   private static final float SELECTED_RANK_SCISSOR_HEIGHT_REDUCTION = 4.0F;
   private static final float SELECTED_RANK_RIGHT_OFFSET = 92.0F;
   private static final float SELECTED_RANK_OFFSET_Y = 6.0F;
   private static final float SELECTED_RANK_SIZE = 16.0F;
   private static final String NO_SELECTION_LABEL = "Не выбрано";
   private static final float SELECTED_LABEL_MAX_WIDTH = 60.0F;
   private static final float SELECTED_LABEL_MEASURE_FONT_SIZE = 12.0F;
   private static final float SELECTED_LABEL_RIGHT_OFFSET = 92.0F;
   private static final float SELECTED_LABEL_OFFSET_Y = 4.5F;
   private static final float ARROW_RIGHT_OFFSET = 22.0F;
   private static final float ARROW_OFFSET_Y = 5.0F;
   private static final float ARROW_WIDTH = 16.0F;
   private static final float ARROW_HEIGHT = 16.0F;
   private static final String ARROW_RESOURCE_PATH = "gui/arrow";
   private static final String LABEL_ELLIPSIS = "...";
   private static final float OVERLAY_VISIBLE_ROW_STRIDE = 26.0F;
   private static final float OVERLAY_CONTENT_ROW_STRIDE = 26.0F;
   private static final float OVERLAY_POPUP_ANCHOR_RIGHT_OFFSET = 50.0F;
   private static final float OVERLAY_POPUP_GAP = 5.0F;
   private static final float OVERLAY_CLAMP_HEIGHT_PADDING = 4.0F;
   private static final float OVERLAY_CLAMP_ROW_STRIDE = 26.0F;
   private static final float OVERLAY_CONTENT_OFFSET_Y = 4.0F;
   private static final float OVERLAY_POPUP_HEIGHT_PADDING = 4.0F;
   private static final float OVERLAY_INITIAL_POPUP_SCALE = 1.25F;
   private static final float OVERLAY_POPUP_SCALE_PROGRESS_DIVISOR = 4.0F;
   private static final float POPUP_SHADOW_RADIUS = 24.0F;
   private static final float POPUP_BLUR_RADIUS = 22.0F;
   private static final float POPUP_BLUR_CORNER_RADIUS = 14.0F;
   private static final float POPUP_BLUR_ALPHA_SCALE = 255.0F;
   private static final float POPUP_BACKGROUND_CORNER_RADIUS = 14.0F;
   private static final float POPUP_OUTLINE_CORNER_RADIUS = 14.0F;
   private static final float OVERLAY_ROW_WIDTH_REDUCTION = 8.0F;
   private static final float OVERLAY_CONTENT_OFFSET_X = 4.0F;
   private static final float OPTION_ROW_POSITION_STRIDE = 26.0F;
   private static final float OPTION_CULL_HEIGHT = 26.0F;
   private static final float OPTION_RANK_OFFSET_X = 12.0F;
   private static final float OPTION_RANK_OFFSET_Y = 3.0F;
   private static final float OPTION_RANK_SIZE = 16.0F;
   private static final float OPTION_LABEL_OFFSET_X = 12.0F;
   private static final float OPTION_LABEL_OFFSET_Y = 4.0F;
   private static final float INACTIVE_KEYBIND_ALPHA_FACTOR = 0.85F;
   private static final float KEYBIND_ROW_OFFSET_X = 4.0F;
   private static final float KEYBIND_RIGHT_PADDING = 4.0F;
   private static final float KEYBIND_OFFSET_Y = 5.0F;
   private static final float CLICK_VALUE_BOX_RIGHT_OFFSET = 100.0F;
   private static final double CLICK_VALUE_BOX_WIDTH = 100.0;
   private static final float CLICK_POPUP_HEIGHT_PADDING = 4.0F;
   private static final float CLICK_POPUP_ROW_STRIDE = 26.0F;
   private static final float CLICK_POPUP_ANCHOR_RIGHT_OFFSET = 50.0F;
   private static final float CLICK_POPUP_GAP = 5.0F;
   private static final float CLICK_CLAMP_HEIGHT_PADDING = 4.0F;
   private static final float CLICK_CLAMP_ROW_STRIDE = 26.0F;
   private static final float PRIORITY_VISIBLE_ROW_STRIDE = 26.0F;
   private static final float PRIORITY_CONTENT_ROW_STRIDE = 26.0F;
   private static final float PRIORITY_POPUP_ANCHOR_RIGHT_OFFSET = 50.0F;
   private static final float PRIORITY_POPUP_GAP = 5.0F;
   private static final float PRIORITY_CLAMP_HEIGHT_PADDING = 4.0F;
   private static final float PRIORITY_CLAMP_ROW_STRIDE = 26.0F;
   private static final float PRIORITY_CONTENT_OFFSET_Y = 4.0F;
   private static final float PRIORITY_POPUP_HEIGHT_PADDING = 4.0F;
   private static final float PRIORITY_ROW_WIDTH_REDUCTION = 8.0F;
   private static final float SCROLLBAR_HITBOX_RIGHT_OFFSET = 15.0F;
   private static final float SCROLLBAR_HITBOX_LEFT_PADDING = 4.0F;
   private static final double SCROLLBAR_HITBOX_WIDTH = 10.0;
   private static final float PRIORITY_CONTENT_OFFSET_X = 4.0F;
   private static final float HIT_ROW_INDEX_DIVISOR = 26.0F;
   private static final float HIT_ROW_REMAINDER_STRIDE = 26.0F;
   private static final float OPTION_CONTENT_HIT_HEIGHT = 22.0F;
   private static final float INITIAL_POPUP_SCALE = 1.25F;
   private static final float POPUP_SCALE_PROGRESS_DIVISOR = 4.0F;
   private static final float SCROLL_VISIBLE_ROW_STRIDE = 26.0F;
   private static final float SCROLL_CONTENT_ROW_STRIDE = 26.0F;
   private static final float SCROLL_POPUP_ANCHOR_RIGHT_OFFSET = 50.0F;
   private static final float SCROLL_POPUP_GAP = 5.0F;
   private static final float SCROLL_CLAMP_HEIGHT_PADDING = 4.0F;
   private static final float SCROLL_CLAMP_ROW_STRIDE = 26.0F;
   private static final float SCROLL_POPUP_HEIGHT_PADDING = 4.0F;
   private static final float SCROLL_STEP = 26.0F;
   private static final float MIN_POPUP_WIDTH = 159.0F;
   private static final String PENDING_KEYBIND_MEASURE_LABEL = "...";
   private static final float RANK_GLYPH_MEASURE_SIZE = 16.0F;
   private static final String DONATION_SETTING_NAME = "Донат";
   private static final String DEFAULT_RANK_NAME = "Default";
   private static final float RANK_ALPHA_DIVISOR = 255.0F;
   private static final int RANK_GLYPH_RGB_MASK = 16777215;
   private static final float RANK_GLYPH_SCALE_SIZE_DIVISOR = 10.0F;
   private static final float LEFT_NDC_ORIGIN = -1.0F;
   private static final float RIGHT_NDC_ORIGIN = -1.0F;
   private static final float SCREEN_LEFT_HALF_FACTOR = 0.5F;
   private static final float SCREEN_TOP_HALF_FACTOR = 0.5F;
   private static final float SCREEN_RIGHT_HALF_FACTOR = 0.5F;
   private static final float SCREEN_BOTTOM_HALF_FACTOR = 0.5F;
   private static final long CAPTURE_DOT_INTERVAL_MILLIS = 280L;
   private static final long CAPTURE_DOT_CYCLE_LENGTH = 3L;
   private static final String CAPTURE_DOT = ".";
   private static final String KEY_SPACE_LABEL = "SPC";
   private static final String KEY_LEFT_SHIFT_LABEL = "LSH";
   private static final String KEY_RIGHT_SHIFT_LABEL = "RSH";
   private static final String KEY_LEFT_CONTROL_LABEL = "LCT";
   private static final String KEY_RIGHT_CONTROL_LABEL = "RCT";
   private static final String KEY_LEFT_ALT_LABEL = "LAL";
   private static final String KEY_RIGHT_ALT_LABEL = "RAL";
   private static final String KEY_ENTER_LABEL = "ENT";
   private static final String KEY_TAB_LABEL = "TAB";
   private static final String KEY_CAPS_LOCK_LABEL = "CAP";
   private static final String KEY_UP_LABEL = "UP";
   private static final String KEY_DOWN_LABEL = "DN";
   private static final String KEY_LEFT_LABEL = "LT";
   private static final String KEY_RIGHT_LABEL = "RT";
   private static final String KEY_DELETE_LABEL = "DEL";
   private static final String KEY_BACKSPACE_LABEL = "BS";
   private static final float SCROLLBAR_RIGHT_OFFSET = 15.0F;
   private static final float SCROLLBAR_TRACK_WIDTH = 3.5F;
   private static final float SCROLLBAR_THUMB_WIDTH = 3.5F;
   private static final float SCROLLBAR_ALPHA_DIVISOR = 255.0F;
   private static final float SCROLLBAR_DRAG_ALPHA_FACTOR = 0.9F;
   private static final float SCROLLBAR_IDLE_ALPHA_FACTOR = 0.7F;
   private static final float MIN_SCROLLBAR_THUMB_HEIGHT = 22.0F;

   @Override
   public void tick() {
      for (ChoiceOption option : this.setting.getValue()) {
         option.getVisibilityAnimation().tick(option.getVisibilityCondition().getAsBoolean());
         option.getSelectionAnimation().updateTarget(option == this.setting.getSelectedOption() ? 1.0F : 0.0F);
      }
   }

   private String formatCompactKeybindLabel(int keyCode) {
      if (keyCode < 0) {
         return "";
      }

      if (keyCode >= 0 && keyCode <= 7) {
         return "M" + (keyCode + 1);
      }

      return switch (keyCode) {
         case 32 -> KEY_SPACE_LABEL;
         case 257 -> KEY_ENTER_LABEL;
         case 258 -> KEY_TAB_LABEL;
         case 259 -> KEY_BACKSPACE_LABEL;
         case 261 -> KEY_DELETE_LABEL;
         case 262 -> KEY_RIGHT_LABEL;
         case 263 -> KEY_LEFT_LABEL;
         case 264 -> KEY_DOWN_LABEL;
         case 265 -> KEY_UP_LABEL;
         case 280 -> KEY_CAPS_LOCK_LABEL;
         case 340 -> KEY_LEFT_SHIFT_LABEL;
         case 341 -> KEY_LEFT_CONTROL_LABEL;
         case 342 -> KEY_LEFT_ALT_LABEL;
         case 344 -> KEY_RIGHT_SHIFT_LABEL;
         case 345 -> KEY_RIGHT_CONTROL_LABEL;
         case 346 -> KEY_RIGHT_ALT_LABEL;
         default -> {
            String keyName = KeyDisplayNames.О(keyCode);
            yield keyName.isEmpty() ? "K" + keyCode : keyName.toUpperCase(Locale.ROOT);
         }
      };
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.pendingKeybindOption != null) {
         if (keyCode == 256) {
            this.pendingKeybindOption = null;
         } else if (keyCode != 261 && keyCode != 259) {
            this.pendingKeybindOption.getKeybind().setKeyCode(keyCode);
            ClientSounds.к(ClientSounds.GuiSound.BIND_SET);
            KeybindConflictResolver.б(keyCode);
            this.pendingKeybindOption = null;
            this.saveModuleConfig();
         } else {
            this.pendingKeybindOption.getKeybind().setKeyCode(-1);
            ClientSounds.к(ClientSounds.GuiSound.BIND_RESET);
            this.pendingKeybindOption = null;
            this.saveModuleConfig();
         }
      }
   }

   @Override
   public boolean isCapturingKeybind() {
      return this.pendingKeybindOption != null;
   }

   private float inversePopupScaleCoordinate(float coordinate, float rectStart, float rectSize, float scale) {
      float center = rectStart + rectSize / 2.0F;
      return center + (coordinate - center) / scale;
   }

   @Override
   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      if (!this.popupOpen) {
         return false;
      }

      List options = this.getVisibleOptions();
      if (options.isEmpty()) {
         return false;
      }

      float popupWidth = this.calculatePopupWidth(options);
      float visibleHeight = Math.min(5, options.size()) * PRIORITY_VISIBLE_ROW_STRIDE;
      float contentHeight = options.size() * PRIORITY_CONTENT_ROW_STRIDE;
      float maxScrollOffset = Math.max(0.0F, contentHeight - visibleHeight);
      this.scrollOffset = Math.clamp(this.scrollOffset, 0.0F, maxScrollOffset);
      float popupX = this.clampPopupX(this.x + this.width - PRIORITY_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
      float popupY = this.clampPopupY(this.y + this.height + PRIORITY_POPUP_GAP, PRIORITY_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * PRIORITY_CLAMP_ROW_STRIDE);
      float contentY = popupY + PRIORITY_CONTENT_OFFSET_Y;
      float popupHeight = PRIORITY_POPUP_HEIGHT_PADDING + visibleHeight;
      float popupScale = this.getPopupScale();
      float unscaledMouseX = this.inversePopupScaleCoordinate(mouseX, popupX, popupWidth, popupScale);
      float unscaledMouseY = this.inversePopupScaleCoordinate(mouseY, popupY, popupHeight, popupScale);
      if (!WorldProjection.containsPoint(unscaledMouseX, unscaledMouseY, popupX, popupY, popupWidth, popupHeight) && this.pendingKeybindOption == null) {
         return false;
      }

      if (this.pendingKeybindOption != null) {
         if (button != 0) {
            this.pendingKeybindOption.getKeybind().setKeyCode(button);
            ClientSounds.к(ClientSounds.GuiSound.BIND_SET);
            KeybindConflictResolver.б(button);
            this.pendingKeybindOption = null;
            this.saveModuleConfig();
         }

         return true;
      } else {
         boolean scrollbarNeeded = maxScrollOffset > 0.0F;
         float rowHitWidth = popupWidth - PRIORITY_ROW_WIDTH_REDUCTION - (scrollbarNeeded ? 8 : 0);
         if (button == 0 && scrollbarNeeded) {
            float scrollbarX = popupX + popupWidth - SCROLLBAR_HITBOX_RIGHT_OFFSET;
            if (WorldProjection.containsPoint(unscaledMouseX, unscaledMouseY, scrollbarX - SCROLLBAR_HITBOX_LEFT_PADDING, contentY, SCROLLBAR_HITBOX_WIDTH, visibleHeight)) {
               this.beginScrollbarDrag(unscaledMouseY, contentY, visibleHeight, contentHeight, maxScrollOffset);
               return true;
            }
         }

         if (WorldProjection.containsPoint(unscaledMouseX, unscaledMouseY, popupX + PRIORITY_CONTENT_OFFSET_X, contentY, rowHitWidth, visibleHeight)) {
            float contentMouseY = unscaledMouseY - contentY + this.scrollOffset;
            int rowIndex = (int)(contentMouseY / HIT_ROW_INDEX_DIVISOR);
            float rowOffsetY = contentMouseY - rowIndex * HIT_ROW_REMAINDER_STRIDE;
            if (rowIndex >= 0 && rowIndex < options.size() && rowOffsetY <= OPTION_CONTENT_HIT_HEIGHT) {
               ChoiceOption option = (ChoiceOption)options.get(rowIndex);
               if (button == 1) {
                  this.pendingKeybindOption = option;
                  ClientSounds.к(ClientSounds.GuiSound.BIND_START);
               } else if (button == 0 && this.setting.getSelectedOption() != option) {
                  this.setting.setSelectedOption(option);
                  ClientSounds.к(ClientSounds.GuiSound.MODE, (float)rowIndex / Math.max(1, options.size() - 1));
               }
            }

            return true;
         } else {
            return button == 0;
         }
      }
   }

   private void saveModuleConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   @Override
   public void resetTransientState() {
      this.popupOpen = false;
      this.pendingKeybindOption = null;
      this.scrollbarDragging = false;
   }

   private String ellipsizeLabelByCodePoint(String label, float maxWidth, float fontSize) {
      if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), label, (int)fontSize) <= maxWidth) {
         return label;
      }

      String ellipsis = LABEL_ELLIPSIS;
      int endIndex = label.length();

      while (endIndex > 0) {
         endIndex = label.offsetByCodePoints(endIndex, -1);
         String candidateLabel = label.substring(0, endIndex) + ellipsis;
         if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), candidateLabel, (int)fontSize) <= maxWidth) {
            return candidateLabel;
         }
      }

      return ellipsis;
   }

   private void updateScrollbarDrag(float mouseY, float trackY, float visibleHeight, float contentHeight, float maxScrollOffset) {
      float thumbHeight = this.calculateScrollbarThumbHeight(visibleHeight, contentHeight);
      float thumbTravel = visibleHeight - thumbHeight;
      if (!(thumbTravel <= 0.0F) && !(maxScrollOffset <= 0.0F)) {
         float scrollFraction = Math.clamp((mouseY - this.scrollbarGrabOffset - trackY) / thumbTravel, 0.0F, 1.0F);
         this.scrollOffset = scrollFraction * maxScrollOffset;
         this.targetScrollOffset = this.scrollOffset;
         this.scrollAnimation.setImmediately(this.scrollOffset);
      } else {
         this.resetScrollState();
      }
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.height = ELEMENT_HEIGHT;
      this.popupOpenAnimation.updateTarget(this.popupOpen ? 1.0F : 0.0F);
      int valueColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.OPEN_SETTING_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.DEOPEN_SETTING_TEXT), this.popupOpenAnimation.getValue());
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y + SETTING_LABEL_OFFSET_Y, 13, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      UiDrawing.drawOutline(this.x + this.width - SELECTED_VALUE_RIGHT_OFFSET, this.y, SELECTED_VALUE_BOX_WIDTH, this.height, 1.0F, SELECTED_VALUE_BOX_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      ChoiceOption selectedOption = this.setting.getSelectedOption();
      if (this.shouldDrawDonationRankGlyph(selectedOption)) {
         UiDrawing.pushScissor(this.x + this.width - SELECTED_RANK_SCISSOR_RIGHT_OFFSET, this.y + 2.0F, SELECTED_RANK_SCISSOR_WIDTH, this.height - SELECTED_RANK_SCISSOR_HEIGHT_REDUCTION);
         this.drawDonationRankGlyph(selectedOption, this.x + this.width - SELECTED_RANK_RIGHT_OFFSET, this.y + SELECTED_RANK_OFFSET_Y, SELECTED_RANK_SIZE, valueColor);
         UiDrawing.popScissor();
      } else {
         String selectedLabel = selectedOption == null ? ClientLocalization.translate(NO_SELECTION_LABEL) : ClientLocalization.translate(selectedOption.getName());
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.ellipsizeLabelByCodePoint(selectedLabel, SELECTED_LABEL_MAX_WIDTH, SELECTED_LABEL_MEASURE_FONT_SIZE), this.x + this.width - SELECTED_LABEL_RIGHT_OFFSET, this.y + SELECTED_LABEL_OFFSET_Y, 12, valueColor);
      }

      UiDrawing.drawTexture(this.x + this.width - ARROW_RIGHT_OFFSET, this.y + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, 0.0F, valueColor, ClientResourceIds.getImageId(ARROW_RESOURCE_PATH));
   }

   private void beginScrollbarDrag(float mouseY, float trackY, float visibleHeight, float contentHeight, float maxScrollOffset) {
      float thumbHeight = this.calculateScrollbarThumbHeight(visibleHeight, contentHeight);
      float thumbTravel = visibleHeight - thumbHeight;
      float thumbY = trackY + thumbTravel * (maxScrollOffset <= 0.0F ? 0.0F : this.scrollOffset / maxScrollOffset);
      this.scrollbarGrabOffset = mouseY >= thumbY && mouseY <= thumbY + thumbHeight ? mouseY - thumbY : thumbHeight / 2.0F;
      this.scrollbarDragging = true;
      this.updateScrollbarDrag(mouseY, trackY, visibleHeight, contentHeight, maxScrollOffset);
   }

   private String getAnimatedBindingDots() {
      int dotCount = (int)(System.currentTimeMillis() / CAPTURE_DOT_INTERVAL_MILLIS % CAPTURE_DOT_CYCLE_LENGTH) + 1;
      return CAPTURE_DOT.repeat(dotCount);
   }

   private float getPopupScale() {
      return Math.max(1.0F, INITIAL_POPUP_SCALE * (1.0F - this.popupOpenAnimation.getValue() / POPUP_SCALE_PROGRESS_DIVISOR));
   }

   private void drawDonationRankGlyph(ChoiceOption option, float x, float y, float glyphSize, int color) {
      char rankGlyph = ReallyWorldRankGlyphs.必(option.getName());
      int alpha = Math.clamp(Math.round((color >>> 24) * UiDrawing.getGlobalAlpha() / RANK_ALPHA_DIVISOR), 0, 255);
      if (alpha != 0) {
         int glyphColor = alpha << 24 | RANK_GLYPH_RGB_MASK;
         Minecraft client = Minecraft.getInstance();
         if (client.gui.screen() instanceof ClickGuiScreen guiScreen) {
            GuiGraphicsExtractor graphics = guiScreen.getFrameContext();
            if (graphics != null) {
               float guiScale = client.getWindow().getGuiScale();
               Vector4f screenBounds = this.transformRectToScreenBounds(x, y, glyphSize, glyphSize);
               boolean scissorEnabled = UiDrawing.isScissorEnabled();
               if (scissorEnabled) {
                  Vector4f scissor = UiDrawing.getScissor();
                  Vector4f screenScissorBounds = this.transformRectToScreenBounds(scissor.x, scissor.y, scissor.z, scissor.w);
                  int scissorMinX = (int)Math.floor(screenScissorBounds.x / guiScale);
                  int scissorMinY = (int)Math.floor(screenScissorBounds.y / guiScale);
                  int scissorMaxX = (int)Math.ceil((screenScissorBounds.x + screenScissorBounds.z) / guiScale);
                  int scissorMaxY = (int)Math.ceil((screenScissorBounds.y + screenScissorBounds.w) / guiScale);
                  if (scissorMaxX <= scissorMinX || scissorMaxY <= scissorMinY) {
                     return;
                  }

                  graphics.enableScissor(scissorMinX, scissorMinY, scissorMaxX, scissorMaxY);
               }

               graphics.pose().pushMatrix();

               try {
                  float glyphScale = screenBounds.w / (RANK_GLYPH_SCALE_SIZE_DIVISOR * guiScale);
                  graphics.pose().translate(screenBounds.x / guiScale, screenBounds.y / guiScale);
                  graphics.pose().scale(glyphScale, glyphScale);
                  NameProtectionTextRewriter.ж3();

                  try {
                     graphics.text(client.font, String.valueOf(rankGlyph), 0, 0, glyphColor, false);
                  } finally {
                     NameProtectionTextRewriter.жд();
                  }
               } finally {
                  graphics.pose().popMatrix();
                  if (scissorEnabled) {
                     graphics.disableScissor();
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      if (!this.popupOpen) {
         return false;
      }

      List options = this.getVisibleOptions();
      float popupWidth = this.calculatePopupWidth(options);
      float visibleHeight = Math.min(5, options.size()) * SCROLL_VISIBLE_ROW_STRIDE;
      float contentHeight = options.size() * SCROLL_CONTENT_ROW_STRIDE;
      float maxScrollOffset = Math.max(0.0F, contentHeight - visibleHeight);
      float popupX = this.clampPopupX(this.x + this.width - SCROLL_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
      float popupY = this.clampPopupY(this.y + this.height + SCROLL_POPUP_GAP, SCROLL_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * SCROLL_CLAMP_ROW_STRIDE);
      float popupHeight = SCROLL_POPUP_HEIGHT_PADDING + visibleHeight;
      float popupScale = this.getPopupScale();
      float unscaledMouseX = this.inversePopupScaleCoordinate(mouseX, popupX, popupWidth, popupScale);
      float unscaledMouseY = this.inversePopupScaleCoordinate(mouseY, popupY, popupHeight, popupScale);
      if (!WorldProjection.containsPoint(unscaledMouseX, unscaledMouseY, popupX, popupY, popupWidth, popupHeight)) {
         return false;
      }

      float previousTargetScrollOffset = this.targetScrollOffset;
      this.targetScrollOffset = this.targetScrollOffset - (float)scrollDelta * SCROLL_STEP;
      this.targetScrollOffset = Math.clamp(this.targetScrollOffset, 0.0F, maxScrollOffset);
      if (previousTargetScrollOffset != this.targetScrollOffset) {
         ClientSounds.к(ClientSounds.GuiSound.SCROLL);
      }

      return true;
   }

   public SingleChoiceSettingWidget(SingleChoiceSetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.popupOpenAnimation = new FloatAnimation(0.0F, POPUP_OPEN_ANIMATION_SPEED);
      this.scrollAnimation = new FloatAnimation(0.0F, SCROLL_ANIMATION_SPEED, Easings.CUBIC_OUT);
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - CLICK_VALUE_BOX_RIGHT_OFFSET, this.y, CLICK_VALUE_BOX_WIDTH, this.height)) {
         this.popupOpen = !this.popupOpen;
         ClientSounds.к(this.popupOpen ? ClientSounds.GuiSound.PICKER_OPEN : ClientSounds.GuiSound.PICKER_CLOSE);
         if (!this.popupOpen) {
            this.scrollbarDragging = false;
         }
      } else if (this.popupOpen) {
         List options = this.getVisibleOptions();
         if (options.isEmpty()) {
            this.popupOpen = false;
         } else {
            float popupWidth = this.calculatePopupWidth(options);
            float popupHeight = CLICK_POPUP_HEIGHT_PADDING + Math.min(5, options.size()) * CLICK_POPUP_ROW_STRIDE;
            float popupX = this.clampPopupX(this.x + this.width - CLICK_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
            float popupY = this.clampPopupY(this.y + this.height + CLICK_POPUP_GAP, CLICK_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * CLICK_CLAMP_ROW_STRIDE);
            float popupScale = this.getPopupScale();
            float unscaledMouseX = this.inversePopupScaleCoordinate(mouseX, popupX, popupWidth, popupScale);
            float unscaledMouseY = this.inversePopupScaleCoordinate(mouseY, popupY, popupHeight, popupScale);
            if (!WorldProjection.containsPoint(unscaledMouseX, unscaledMouseY, popupX, popupY, popupWidth, popupHeight)) {
               this.popupOpen = false;
               this.pendingKeybindOption = null;
               this.scrollbarDragging = false;
            }
         }
      }
   }

   public boolean isPopupOpen() {
      return this.popupOpen;
   }

   private void resetScrollState() {
      this.scrollOffset = 0.0F;
      this.targetScrollOffset = 0.0F;
      this.scrollAnimation.setImmediately(0.0F);
      this.scrollbarDragging = false;
   }

   private List<ChoiceOption> getVisibleOptions() {
      return this.setting.getValue().stream().filter(option -> option.getVisibilityCondition().getAsBoolean()).toList();
   }

   private boolean shouldDrawDonationRankGlyph(ChoiceOption option) {
      return option != null && DONATION_SETTING_NAME.equals(this.setting.getName()) && !DEFAULT_RANK_NAME.equals(option.getName());
   }

   private float calculateScrollbarThumbHeight(float visibleHeight, float contentHeight) {
      return Math.max(MIN_SCROLLBAR_THUMB_HEIGHT, visibleHeight * visibleHeight / contentHeight);
   }

   private Vector4f transformRectToScreenBounds(float x, float y, float width, float height) {
      Minecraft client = Minecraft.getInstance();
      float screenWidth = client.getWindow().getScreenWidth();
      float screenHeight = client.getWindow().getScreenHeight();
      float ndcLeft = LEFT_NDC_ORIGIN + x * 2.0F / screenWidth;
      float ndcTop = 1.0F - y * 2.0F / screenHeight;
      float ndcRight = RIGHT_NDC_ORIGIN + (x + width) * 2.0F / screenWidth;
      float ndcBottom = 1.0F - (y + height) * 2.0F / screenHeight;
      Vector3f transformedTopLeft = UiDrawing.getTransform().transformPosition(ndcLeft, ndcTop, 0.0F, new Vector3f());
      Vector3f transformedBottomRight = UiDrawing.getTransform().transformPosition(ndcRight, ndcBottom, 0.0F, new Vector3f());
      float screenMinX = (Math.min(transformedTopLeft.x, transformedBottomRight.x) + 1.0F) * screenWidth * SCREEN_LEFT_HALF_FACTOR;
      float screenMinY = (1.0F - Math.max(transformedTopLeft.y, transformedBottomRight.y)) * screenHeight * SCREEN_TOP_HALF_FACTOR;
      float screenMaxX = (Math.max(transformedTopLeft.x, transformedBottomRight.x) + 1.0F) * screenWidth * SCREEN_RIGHT_HALF_FACTOR;
      float screenMaxY = (1.0F - Math.min(transformedTopLeft.y, transformedBottomRight.y)) * screenHeight * SCREEN_BOTTOM_HALF_FACTOR;
      return new Vector4f(screenMinX, screenMinY, screenMaxX - screenMinX, screenMaxY - screenMinY);
   }

   private void drawScrollbar(float popupX, float popupWidth, float trackY, float visibleHeight, float contentHeight, float maxScrollOffset) {
      float trackX = popupX + popupWidth - SCROLLBAR_RIGHT_OFFSET;
      float thumbHeight = this.calculateScrollbarThumbHeight(visibleHeight, contentHeight);
      float thumbTravel = visibleHeight - thumbHeight;
      float thumbY = trackY + thumbTravel * (this.scrollOffset / maxScrollOffset) - 1.0F;
      UiDrawing.drawRoundedRect(trackX, trackY, SCROLLBAR_TRACK_WIDTH, visibleHeight, 1.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawRoundedRect(
         trackX, thumbY, SCROLLBAR_THUMB_WIDTH, thumbHeight, 1.0F, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SCROLL), ThemeEditor.getColorAlpha(ThemeColorSlot.SCROLL) / SCROLLBAR_ALPHA_DIVISOR * (this.scrollbarDragging ? SCROLLBAR_DRAG_ALPHA_FACTOR : SCROLLBAR_IDLE_ALPHA_FACTOR))
      );
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (button == 0) {
         this.scrollbarDragging = false;
      }
   }

   private float calculatePopupWidth(List<ChoiceOption> options) {
      boolean scrollbarNeeded = options.size() > 5;
      float popupWidth = MIN_POPUP_WIDTH;

      for (ChoiceOption option : options) {
         String keybindLabel = option == this.pendingKeybindOption ? PENDING_KEYBIND_MEASURE_LABEL : this.formatCompactKeybindLabel(option.getKeybind().getKeyCode());
         float keybindLabelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindLabel, 10);
         float optionLabelWidth = this.shouldDrawDonationRankGlyph(option) ? ReallyWorldRankTextures.Я(ReallyWorldRankGlyphs.必(option.getName()), RANK_GLYPH_MEASURE_SIZE) : UiDrawing.getTextWidth(ClientFonts.getMediumFont(), ClientLocalization.translate(option.getName()), 11);
         popupWidth = Math.max(popupWidth, optionLabelWidth + keybindLabelWidth + (scrollbarNeeded ? 56 : 48));
      }

      return popupWidth;
   }

   @Override
   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
      List options = this.getVisibleOptions();
      if (options.isEmpty()) {
         this.resetScrollState();
      } else {
         float popupWidth = this.calculatePopupWidth(options);
         int visibleRowCount = Math.min(5, options.size());
         float visibleHeight = visibleRowCount * OVERLAY_VISIBLE_ROW_STRIDE;
         float contentHeight = options.size() * OVERLAY_CONTENT_ROW_STRIDE;
         float maxScrollOffset = Math.max(0.0F, contentHeight - visibleHeight);
         this.targetScrollOffset = Math.clamp(this.targetScrollOffset, 0.0F, maxScrollOffset);
         this.scrollAnimation.updateTarget(this.targetScrollOffset);
         this.scrollOffset = this.scrollAnimation.getValue();
         float popupX = this.clampPopupX(this.x + this.width - OVERLAY_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
         float popupY = this.clampPopupY(this.y + this.height + OVERLAY_POPUP_GAP, OVERLAY_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * OVERLAY_CLAMP_ROW_STRIDE);
         float contentY = popupY + OVERLAY_CONTENT_OFFSET_Y;
         float popupHeight = OVERLAY_POPUP_HEIGHT_PADDING + visibleHeight;
         if (this.scrollbarDragging && maxScrollOffset > 0.0F) {
            this.updateScrollbarDrag(mouseY, contentY, visibleHeight, contentHeight, maxScrollOffset);
         }

         if (maxScrollOffset <= 0.0F) {
            this.resetScrollState();
         }

         float popupScale = Math.max(1.0F, OVERLAY_INITIAL_POPUP_SCALE * (1.0F - this.popupOpenAnimation.getValue() / OVERLAY_POPUP_SCALE_PROGRESS_DIVISOR));
         float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
         WorldProjection.scaleAroundRect(popupX, popupY, popupWidth, popupHeight, popupScale);
         UiDrawing.setGlobalAlpha(previousGlobalAlpha * this.popupOpenAnimation.getValue());
         UiDrawing.drawShadow(popupX, popupY, popupWidth, popupHeight, POPUP_SHADOW_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW));
         UiDrawing.drawBlur(popupX, popupY, popupWidth, popupHeight, POPUP_BLUR_RADIUS, POPUP_BLUR_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, POPUP_BLUR_ALPHA_SCALE * this.popupOpenAnimation.getValue()));
         UiDrawing.drawRoundedRect(popupX, popupY, popupWidth, popupHeight, POPUP_BACKGROUND_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
         UiDrawing.drawOutline(popupX, popupY, popupWidth, popupHeight, 1.0F, POPUP_OUTLINE_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
         boolean scrollbarNeeded = maxScrollOffset > 0.0F;
         float contentWidth = popupWidth - OVERLAY_ROW_WIDTH_REDUCTION - (scrollbarNeeded ? 8 : 0);
         UiDrawing.pushScissor(popupX + OVERLAY_CONTENT_OFFSET_X, contentY, contentWidth, visibleHeight);

         for (int rowIndex = 0; rowIndex < options.size(); rowIndex++) {
            ChoiceOption option = (ChoiceOption)options.get(rowIndex);
            float rowY = contentY + rowIndex * OPTION_ROW_POSITION_STRIDE - this.scrollOffset;
            if (!(rowY + OPTION_CULL_HEIGHT < contentY) && !(rowY > contentY + visibleHeight)) {
               float selectionProgress = option.getSelectionAnimation().getValue();
               int optionLabelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), selectionProgress);
               if (this.shouldDrawDonationRankGlyph(option)) {
                  this.drawDonationRankGlyph(option, popupX + OPTION_RANK_OFFSET_X, rowY + OPTION_RANK_OFFSET_Y, OPTION_RANK_SIZE, optionLabelColor);
               } else {
                  UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(option.getName()), popupX + OPTION_LABEL_OFFSET_X, rowY + OPTION_LABEL_OFFSET_Y, 11, optionLabelColor);
               }

               String keybindLabel = option == this.pendingKeybindOption ? this.getAnimatedBindingDots() : this.formatCompactKeybindLabel(option.getKeybind().getKeyCode());
               float keybindLabelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindLabel, 10);
               int keybindColor = KeybindConflictResolver.в(
                  option.getKeybind().getKeyCode(),
                  option == this.pendingKeybindOption,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), INACTIVE_KEYBIND_ALPHA_FACTOR),
                  ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT),
                  this.setting
               );
               UiDrawing.drawText(ClientFonts.getMediumFont(), keybindLabel, popupX + KEYBIND_ROW_OFFSET_X + contentWidth - keybindLabelWidth - KEYBIND_RIGHT_PADDING, rowY + KEYBIND_OFFSET_Y, 10, keybindColor);
            }
         }

         UiDrawing.popScissor();
         if (scrollbarNeeded) {
            this.drawScrollbar(popupX, popupWidth, contentY, visibleHeight, contentHeight, maxScrollOffset);
         }

         UiDrawing.setGlobalAlpha(previousGlobalAlpha);
         WorldProjection.unscaleAroundRect(popupX, popupY, popupWidth, popupHeight, popupScale);
      }
   }
}
