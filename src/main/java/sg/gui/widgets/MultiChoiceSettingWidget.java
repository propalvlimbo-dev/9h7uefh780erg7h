package sg.gui.widgets;

import com.google.gson.annotations.SerializedName;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import settings.render.ClientResourceIds;
import settings.render.ClientLocalization;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
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
import sg.modules.impl.init.KeybindConflictResolver;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public class MultiChoiceSettingWidget extends SettingWidget<MultiChoiceSetting> {
   private static final int О = 5;
   private static final float сн = 26.0F;
   private static final float сЦ = 22.0F;
   private static final float сч = 4.0F;
   private static final float сЭ = 42.0F;
   private final @com.google.gson.annotations.SerializedName("Ь")  FloatAnimation popupOpenAnimation;
   private final @com.google.gson.annotations.SerializedName("衣")  FloatAnimation scrollAnimation;
   @SerializedName("В")
   private final IdentityHashMap<ChoiceOption, FloatAnimation> optionSelectionAnimations;
   private @com.google.gson.annotations.SerializedName("э")  boolean popupOpen;
   @SerializedName("В")
   private ChoiceOption pendingKeybindOption;
   private @com.google.gson.annotations.SerializedName("с必")  float scrollOffset;
   private @com.google.gson.annotations.SerializedName("сщ")  float targetScrollOffset;
   private @com.google.gson.annotations.SerializedName("ю")  boolean scrollbarDragging;
   private @com.google.gson.annotations.SerializedName("сГ")  float scrollbarGrabOffset;
   private static final float INITIAL_HEIGHT = 24.0F;
   private static final float POPUP_OPEN_ANIMATION_SPEED = 10.0F;
   private static final float SCROLL_ANIMATION_SPEED = 14.0F;
   private static final float SCROLL_VISIBLE_ROW_STRIDE = 26.0F;
   private static final float SCROLL_CONTENT_ROW_STRIDE = 26.0F;
   private static final float SCROLL_POPUP_ANCHOR_RIGHT_OFFSET = 50.0F;
   private static final float SCROLL_POPUP_GAP = 5.0F;
   private static final float SCROLL_CLAMP_HEIGHT_PADDING = 4.0F;
   private static final float SCROLL_CLAMP_ROW_STRIDE = 26.0F;
   private static final float SCROLL_POPUP_HEIGHT_PADDING = 4.0F;
   private static final float SCROLL_INITIAL_POPUP_SCALE = 1.25F;
   private static final float SCROLL_POPUP_SCALE_PROGRESS_DIVISOR = 4.0F;
   private static final float SCROLL_STEP = 26.0F;
   private static final float ELEMENT_HEIGHT = 24.0F;
   private static final float SETTING_LABEL_OFFSET_Y = 5.0F;
   private static final float VALUE_BOX_RIGHT_OFFSET = 100.0F;
   private static final float VALUE_BOX_WIDTH = 100.0F;
   private static final float VALUE_BOX_CORNER_RADIUS = 6.0F;
   private static final String PICK_OPTIONS_LABEL = "Выбрать";
   private static final float VALUE_LABEL_RIGHT_OFFSET = 92.0F;
   private static final float VALUE_LABEL_OFFSET_Y = 5.0F;
   private static final float ARROW_RIGHT_OFFSET = 22.0F;
   private static final float ARROW_OFFSET_Y = 5.0F;
   private static final float ARROW_WIDTH = 16.0F;
   private static final float ARROW_HEIGHT = 16.0F;
   private static final String ARROW_RESOURCE_PATH = "gui/arrow";
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
   private static final float POPUP_OUTLINE_CORNER_RADIUS = 14.0F;
   private static final float OVERLAY_ROW_WIDTH_REDUCTION = 8.0F;
   private static final float OVERLAY_CONTENT_OFFSET_X = 4.0F;
   private static final float OPTION_ROW_POSITION_STRIDE = 26.0F;
   private static final float OPTION_CULL_HEIGHT = 26.0F;
   private static final float SELECTED_ICON_LABEL_SHIFT = 16.0F;
   private static final String SELECTION_ICON_GLYPH = "r";
   private static final float SELECTION_ICON_OFFSET_X = 11.0F;
   private static final float SELECTION_ICON_UNSELECTED_SHIFT = 4.0F;
   private static final float SELECTION_ICON_OFFSET_Y = 6.0F;
   private static final float SELECTION_ICON_ALPHA_DIVISOR = 255.0F;
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
   private static final String SELECTION_ICON_MEASURE_GLYPH = "r";
   private static final float MIN_POPUP_WIDTH = 159.0F;
   private static final String PENDING_KEYBIND_MEASURE_LABEL = "...";
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
   private static final float OPTION_SELECTION_ANIMATION_SPEED = 7.0F;

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
            int visibleRowCount = Math.min(5, options.size());
            float popupHeight = CLICK_POPUP_HEIGHT_PADDING + visibleRowCount * CLICK_POPUP_ROW_STRIDE;
            float popupX = this.clampPopupX(this.x + this.width - CLICK_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
            float popupY = this.clampPopupY(this.y + this.height + CLICK_POPUP_GAP, CLICK_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * CLICK_CLAMP_ROW_STRIDE);
            if (!WorldProjection.containsPoint(mouseX, mouseY, popupX, popupY, popupWidth, popupHeight)) {
               this.popupOpen = false;
               this.pendingKeybindOption = null;
               this.scrollbarDragging = false;
            }
         }
      }
   }

   private float calculateScrollbarThumbHeight(float visibleHeight, float contentHeight) {
      return Math.max(MIN_SCROLLBAR_THUMB_HEIGHT, visibleHeight * visibleHeight / contentHeight);
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
   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
      List options = this.getVisibleOptions();
      if (options.isEmpty()) {
         this.scrollOffset = 0.0F;
         this.targetScrollOffset = 0.0F;
         this.scrollAnimation.setImmediately(0.0F);
         this.scrollbarDragging = false;
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
            this.scrollOffset = 0.0F;
            this.targetScrollOffset = 0.0F;
            this.scrollAnimation.setImmediately(0.0F);
            this.scrollbarDragging = false;
         }

         float popupScale = Math.max(1.0F, OVERLAY_INITIAL_POPUP_SCALE * (1.0F - this.popupOpenAnimation.getValue() / OVERLAY_POPUP_SCALE_PROGRESS_DIVISOR));
         float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
         WorldProjection.scaleAroundRect(popupX, popupY, popupWidth, popupHeight, popupScale);
         UiDrawing.setGlobalAlpha(previousGlobalAlpha * this.popupOpenAnimation.getValue());
         UiDrawing.drawShadow(popupX, popupY, popupWidth, popupHeight, POPUP_SHADOW_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW));
         UiDrawing.drawBlur(popupX, popupY, popupWidth, popupHeight, POPUP_BLUR_RADIUS, POPUP_BLUR_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, POPUP_BLUR_ALPHA_SCALE * this.popupOpenAnimation.getValue()));
         UiDrawing.drawOutline(popupX, popupY, popupWidth, popupHeight, 1.0F, POPUP_OUTLINE_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
         boolean scrollbarNeeded = maxScrollOffset > 0.0F;
         float contentWidth = popupWidth - OVERLAY_ROW_WIDTH_REDUCTION - (scrollbarNeeded ? 8 : 0);
         UiDrawing.pushScissor(popupX + OVERLAY_CONTENT_OFFSET_X, contentY, contentWidth, visibleHeight);

         for (int rowIndex = 0; rowIndex < options.size(); rowIndex++) {
            ChoiceOption option = (ChoiceOption)options.get(rowIndex);
            float rowY = contentY + rowIndex * OPTION_ROW_POSITION_STRIDE - this.scrollOffset;
            if (!(rowY + OPTION_CULL_HEIGHT < contentY) && !(rowY > contentY + visibleHeight)) {
               FloatAnimation selectionAnimation = this.optionSelectionAnimations.computeIfAbsent(option, optionIdentity -> new FloatAnimation(0.0F, OPTION_SELECTION_ANIMATION_SPEED, Easings.CUBIC_IN_OUT));
               selectionAnimation.updateTarget(option.isSelected() ? 1.0F : 0.0F);
               float selectionProgress = selectionAnimation.getValue();
               int optionLabelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), selectionProgress);
               float selectionLabelShift = SELECTED_ICON_LABEL_SHIFT * selectionProgress;
               UiDrawing.drawText(
                  ClientFonts.getIconsFont(),
                  SELECTION_ICON_GLYPH,
                  popupX + SELECTION_ICON_OFFSET_X + SELECTION_ICON_UNSELECTED_SHIFT * (1.0F - selectionProgress),
                  rowY + SELECTION_ICON_OFFSET_Y,
                  11,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.ACTIVE_TEXT) / SELECTION_ICON_ALPHA_DIVISOR * selectionProgress)
               );
               UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(option.getName()), popupX + OPTION_LABEL_OFFSET_X + selectionLabelShift, rowY + OPTION_LABEL_OFFSET_Y, 11, optionLabelColor);
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
         UiDrawing.setGlobalAlpha(previousGlobalAlpha);
         WorldProjection.unscaleAroundRect(popupX, popupY, popupWidth, popupHeight, popupScale);
      }
   }

   private void saveModuleConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
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
      int visibleRowCount = Math.min(5, options.size());
      float visibleHeight = visibleRowCount * PRIORITY_VISIBLE_ROW_STRIDE;
      float contentHeight = options.size() * PRIORITY_CONTENT_ROW_STRIDE;
      float maxScrollOffset = Math.max(0.0F, contentHeight - visibleHeight);
      this.scrollOffset = Math.clamp(this.scrollOffset, 0.0F, maxScrollOffset);
      float popupX = this.clampPopupX(this.x + this.width - PRIORITY_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
      float popupY = this.clampPopupY(this.y + this.height + PRIORITY_POPUP_GAP, PRIORITY_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * PRIORITY_CLAMP_ROW_STRIDE);
      float contentY = popupY + PRIORITY_CONTENT_OFFSET_Y;
      float popupHeight = PRIORITY_POPUP_HEIGHT_PADDING + visibleHeight;
      boolean scrollbarNeeded = maxScrollOffset > 0.0F;
      float rowHitWidth = popupWidth - PRIORITY_ROW_WIDTH_REDUCTION - (scrollbarNeeded ? 8 : 0);
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
         if (button == 0 && scrollbarNeeded) {
            float scrollbarX = popupX + popupWidth - SCROLLBAR_HITBOX_RIGHT_OFFSET;
            if (WorldProjection.containsPoint(mouseX, mouseY, scrollbarX - SCROLLBAR_HITBOX_LEFT_PADDING, contentY, SCROLLBAR_HITBOX_WIDTH, visibleHeight)) {
               this.beginScrollbarDrag(mouseY, contentY, visibleHeight, contentHeight, maxScrollOffset);
               return true;
            }
         }

         if (WorldProjection.containsPoint(mouseX, mouseY, popupX + PRIORITY_CONTENT_OFFSET_X, contentY, rowHitWidth, visibleHeight)) {
            float contentMouseY = mouseY - contentY + this.scrollOffset;
            int rowIndex = (int)(contentMouseY / HIT_ROW_INDEX_DIVISOR);
            if (rowIndex >= 0 && rowIndex < options.size()) {
               ChoiceOption option = (ChoiceOption)options.get(rowIndex);
               if (button == 1) {
                  this.pendingKeybindOption = option;
                  ClientSounds.к(ClientSounds.GuiSound.BIND_START);
               } else if (button == 0) {
                  option.setSelected(!option.isSelected());
                  ClientSounds.к(option.isSelected() ? ClientSounds.GuiSound.MULTI_ON : ClientSounds.GuiSound.MULTI_OFF);
               }
            }

            return true;
         } else {
            return button == 0 && WorldProjection.containsPoint(mouseX, mouseY, popupX, popupY, popupWidth, popupHeight);
         }
      }
   }

   @Override
   public boolean isCapturingKeybind() {
      return this.pendingKeybindOption != null;
   }

   @Override
   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      if (!this.popupOpen) {
         return false;
      }

      List options = this.getVisibleOptions();
      float popupWidth = this.calculatePopupWidth(options);
      int visibleRowCount = Math.min(5, options.size());
      float visibleHeight = visibleRowCount * SCROLL_VISIBLE_ROW_STRIDE;
      float contentHeight = options.size() * SCROLL_CONTENT_ROW_STRIDE;
      float maxScrollOffset = contentHeight - visibleHeight;
      float popupX = this.clampPopupX(this.x + this.width - SCROLL_POPUP_ANCHOR_RIGHT_OFFSET, popupWidth);
      float popupY = this.clampPopupY(this.y + this.height + SCROLL_POPUP_GAP, SCROLL_CLAMP_HEIGHT_PADDING + Math.min(5, options.size()) * SCROLL_CLAMP_ROW_STRIDE);
      float popupHeight = SCROLL_POPUP_HEIGHT_PADDING + visibleHeight;
      float popupScale = Math.max(1.0F, SCROLL_INITIAL_POPUP_SCALE * (1.0F - this.popupOpenAnimation.getValue() / SCROLL_POPUP_SCALE_PROGRESS_DIVISOR));
      float popupCenterX = popupX + popupWidth / 2.0F;
      float popupCenterY = popupY + popupHeight / 2.0F;
      float unscaledMouseX = popupCenterX + (mouseX - popupCenterX) / popupScale;
      float unscaledMouseY = popupCenterY + (mouseY - popupCenterY) / popupScale;
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

   private void updateScrollbarDrag(float mouseY, float trackY, float visibleHeight, float contentHeight, float maxScrollOffset) {
      float thumbHeight = this.calculateScrollbarThumbHeight(visibleHeight, contentHeight);
      float thumbTravel = visibleHeight - thumbHeight;
      if (!(thumbTravel <= 0.0F) && !(maxScrollOffset <= 0.0F)) {
         float thumbTopFromMouse = mouseY - this.scrollbarGrabOffset;
         float scrollFraction = Math.clamp((thumbTopFromMouse - trackY) / thumbTravel, 0.0F, 1.0F);
         this.scrollOffset = scrollFraction * maxScrollOffset;
         this.targetScrollOffset = this.scrollOffset;
         this.scrollAnimation.setImmediately(this.scrollOffset);
      } else {
         this.scrollOffset = 0.0F;
      }
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (button == 0) {
         this.scrollbarDragging = false;
      }
   }

   public MultiChoiceSettingWidget(MultiChoiceSetting setting) {
      super(setting, INITIAL_HEIGHT);
      this.popupOpenAnimation = new FloatAnimation(0.0F, POPUP_OPEN_ANIMATION_SPEED);
      this.scrollAnimation = new FloatAnimation(0.0F, SCROLL_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.optionSelectionAnimations = new IdentityHashMap<>();
   }

   @Override
   public void tick() {
      for (ChoiceOption option : this.setting.getValue()) {
         option.getVisibilityAnimation().tick(option.getVisibilityCondition().getAsBoolean());
         option.getSelectionAnimation().updateTarget(option.isSelected() ? 1.0F : 0.0F);
      }
   }

   @Override
   public void resetTransientState() {
      this.popupOpen = false;
      this.pendingKeybindOption = null;
      this.scrollbarDragging = false;
   }

   private String getAnimatedBindingDots() {
      int dotCount = (int)(System.currentTimeMillis() / CAPTURE_DOT_INTERVAL_MILLIS % CAPTURE_DOT_CYCLE_LENGTH) + 1;
      return CAPTURE_DOT.repeat(dotCount);
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

   private float calculatePopupWidth(List<ChoiceOption> options) {
      boolean scrollbarNeeded = options.size() > 5;
      float selectionIconWidth = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), SELECTION_ICON_MEASURE_GLYPH, 11);
      float popupWidth = MIN_POPUP_WIDTH;

      for (ChoiceOption option : options) {
         String keybindLabel = option == this.pendingKeybindOption ? PENDING_KEYBIND_MEASURE_LABEL : this.formatCompactKeybindLabel(option.getKeybind().getKeyCode());
         float keybindLabelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindLabel, 10);
         popupWidth = Math.max(popupWidth, UiDrawing.getTextWidth(ClientFonts.getMediumFont(), ClientLocalization.translate(option.getName()), 11) + selectionIconWidth + keybindLabelWidth + (scrollbarNeeded ? 56 : 48));
      }

      return popupWidth;
   }

   private List<ChoiceOption> getVisibleOptions() {
      return this.setting.getValue().stream().filter(option -> option.getVisibilityCondition().getAsBoolean()).toList();
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.height = ELEMENT_HEIGHT;
      this.popupOpenAnimation.updateTarget(this.popupOpen ? 1.0F : 0.0F);
      int valueColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), this.popupOpenAnimation.getValue());
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y + SETTING_LABEL_OFFSET_Y, 13, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      UiDrawing.drawOutline(this.x + this.width - VALUE_BOX_RIGHT_OFFSET, this.y, VALUE_BOX_WIDTH, this.height, 1.0F, VALUE_BOX_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(PICK_OPTIONS_LABEL), this.x + this.width - VALUE_LABEL_RIGHT_OFFSET, this.y + VALUE_LABEL_OFFSET_Y, 11, valueColor);
      UiDrawing.drawTexture(this.x + this.width - ARROW_RIGHT_OFFSET, this.y + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, 0.0F, valueColor, ClientResourceIds.getImageId(ARROW_RESOURCE_PATH));
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

   private void beginScrollbarDrag(float mouseY, float trackY, float visibleHeight, float contentHeight, float maxScrollOffset) {
      float thumbHeight = this.calculateScrollbarThumbHeight(visibleHeight, contentHeight);
      float thumbTravel = visibleHeight - thumbHeight;
      float scrollFraction = maxScrollOffset <= 0.0F ? 0.0F : this.scrollOffset / maxScrollOffset;
      float thumbY = trackY + thumbTravel * scrollFraction;
      if (mouseY >= thumbY && mouseY <= thumbY + thumbHeight) {
         this.scrollbarGrabOffset = mouseY - thumbY;
      } else {
         this.scrollbarGrabOffset = thumbHeight / 2.0F;
      }

      this.scrollbarDragging = true;
      this.updateScrollbarDrag(mouseY, trackY, visibleHeight, contentHeight, maxScrollOffset);
   }
}
