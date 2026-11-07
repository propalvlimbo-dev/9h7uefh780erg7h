package sg.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.modules.impl.init.KeybindConflictResolver;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.client.RoseClient;
import sg.gui.widgets.TextSettingWidget;
import sg.modules.ClientModule;
import sg.gui.widgets.FloatSettingWidget;
import sg.gui.widgets.BooleanSettingWidget;
import sg.gui.widgets.ActionSettingWidget;
import sg.util.math.EasingFunctions;
import sg.gui.widgets.RangeSettingWidget;
import sg.gui.widgets.MultiChoiceSettingWidget;
import sg.gui.widgets.TimeOfDaySettingWidget;
import sg.gui.widgets.SectionHeaderSettingWidget;
import sg.gui.click.ClickGuiScreen;
import sg.settings.ValueSetting;
import sg.settings.TextSetting;
import sg.settings.ActionSetting;
import sg.settings.ColorSetting;
import sg.settings.KeybindSetting;
import sg.settings.SectionHeaderSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.RangeSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.TimeOfDaySetting;
import sg.gui.widgets.SingleChoiceSettingWidget;
import sg.settings.FloatSetting;
import com.deluxe.modules.impl.player.InputBinding;
import org.joml.Vector2f;
import sg.settings.ChoiceOption;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class ModuleCard extends ClickGuiWidget {
   private static final long е弟 = 1000L;
   private final @com.google.gson.annotations.SerializedName("衣")  ArrayList<SettingWidget<? extends ValueSetting<?>>> settingWidgets = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("сО")  FloatAnimation enabledAnimation;
   private final @com.google.gson.annotations.SerializedName("сФ")  FloatAnimation settingsExpansionAnimation;
   private final @com.google.gson.annotations.SerializedName("сЙ")  FloatAnimation settingsOvershootAnimation;
   private final @com.google.gson.annotations.SerializedName("ск")  FloatAnimation hoverAnimation;
   private final @com.google.gson.annotations.SerializedName("сг")  FloatAnimation favoriteAnimation;
   private final @com.google.gson.annotations.SerializedName("Я")  ClientModule module;
   private @com.google.gson.annotations.SerializedName("Юм")  float descriptionMarqueeOffset;
   private @com.google.gson.annotations.SerializedName("еб")  long lastDescriptionRenderNanos;
   private @com.google.gson.annotations.SerializedName("еЫ")  long focusGlareStartMillis;
   private @com.google.gson.annotations.SerializedName("АС")  boolean settingsExpanded;
   private @com.google.gson.annotations.SerializedName("Х必")  String settingsFingerprint;
   private static final float ENABLED_ANIMATION_SPEED = 10.0F;
   private static final float SETTINGS_EXPANSION_ANIMATION_SPEED = 7.0F;
   private static final float SETTINGS_OVERSHOOT_ANIMATION_SPEED = 6.0F;
   private static final float HOVER_ANIMATION_SPEED = 8.0F;
   private static final float FAVORITE_ANIMATION_SPEED = 8.0F;
   private static final float INITIAL_HEIGHT = 33.0F;
   private static final float INITIAL_WIDTH = 296.0F;
   private static final float SCROLL_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float LAYOUT_EXPANSION_THRESHOLD = 0.01F;
   private static final float SETTING_VISIBILITY_TICK_STEP = 0.2F;
   private static final float LAYOUT_SETTING_GAP = 5.0F;
   private static final float SETTINGS_BOTTOM_PADDING = 8.0F;
   private static final float BASE_LAYOUT_HEIGHT = 58.0F;
   private static final double HEADER_HOVER_HEIGHT = 50.0;
   private static final float CARD_BACKGROUND_CORNER_RADIUS = 12.0F;
   private static final float CARD_BACKGROUND_ALPHA_FACTOR = 0.2F;
   private static final float CARD_OUTLINE_CORNER_RADIUS = 12.0F;
   private static final float MODULE_NAME_OFFSET_X = 12.0F;
   private static final float MODULE_NAME_OFFSET_Y = 8.5F;
   private static final float KEYBIND_CONFLICT_OFFSET_X = 6.0F;
   private static final float KEYBIND_CONFLICT_OFFSET_Y = 12.0F;
   private static final float KEYBIND_CONFLICT_WIDTH = 3.0F;
   private static final float KEYBIND_CONFLICT_HEIGHT = 12.0F;
   private static final float KEYBIND_CONFLICT_CORNER_RADIUS = 1.5F;
   private static final double KEYBIND_CONFLICT_ALPHA = 220.0;
   private static final double DISABLED_KNOB_ALPHA = 255.0;
   private static final double ENABLED_KNOB_ALPHA = 255.0;
   private static final float ENABLED_KNOB_TRAVEL = 12.0F;
   private static final double UNFAVORITED_ICON_ALPHA = 255.0;
   private static final double FAVORITED_ICON_ALPHA = 255.0;
   private static final float FAVORITE_ICON_RIGHT_OFFSET = 58.0F;
   private static final float FAVORITE_ICON_OFFSET_Y = 19.0F;
   private static final float FAVORITE_ICON_SIZE = 6.2F;
   private static final float ENABLE_TRACK_RIGHT_OFFSET = 42.0F;
   private static final float ENABLE_TRACK_OFFSET_Y = 10.0F;
   private static final float ENABLE_TRACK_WIDTH = 30.0F;
   private static final float ENABLE_TRACK_HEIGHT = 18.0F;
   private static final float ENABLE_TRACK_CORNER_RADIUS = 7.0F;
   private static final float ENABLE_KNOB_RIGHT_OFFSET = 40.0F;
   private static final float ENABLE_KNOB_OFFSET_Y = 12.0F;
   private static final float ENABLE_KNOB_WIDTH = 14.0F;
   private static final float ENABLE_KNOB_HEIGHT = 14.0F;
   private static final float ENABLE_KNOB_CORNER_RADIUS = 6.0F;
   private static final float SETTINGS_RENDER_EXPANSION_THRESHOLD = 0.01F;
   private static final float SETTING_REVEAL_GAIN = 1.5F;
   private static final float SETTING_REVEAL_STAGGER = 0.5F;
   private static final float SETTING_REVEAL_OFFSET_X = 9.0F;
   private static final float SETTING_REVEAL_BASE_OFFSET_Y = 10.0F;
   private static final float SETTING_REVEAL_STAGGER_OFFSET_Y = 8.0F;
   private static final float SETTING_OVERSHOOT_BASE_OFFSET_Y = 4.0F;
   private static final float SETTING_OVERSHOOT_STAGGER_OFFSET_Y = 3.0F;
   private static final float SETTINGS_LEFT_PADDING = 12.0F;
   private static final float SETTINGS_TOP_OFFSET = 58.0F;
   private static final float SETTINGS_HORIZONTAL_PADDING = 24.0F;
   private static final float RENDER_SETTING_GAP = 5.0F;
   private static final float INITIAL_DESCRIPTION_FRAME_SECONDS = 0.016666668F;
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MAX_DESCRIPTION_FRAME_SECONDS = 0.05F;
   private static final float MARQUEE_HOVER_RESET_THRESHOLD = 0.001F;
   private static final float DESCRIPTION_MARQUEE_PIXELS_PER_SECOND = 38.0F;
   private static final float DESCRIPTION_OFFSET_X = 13.0F;
   private static final float DESCRIPTION_CLIP_OFFSET_Y = 29.0F;
   private static final float DESCRIPTION_WIDTH_PADDING = 26.0F;
   private static final float DESCRIPTION_CLIP_HEIGHT = 18.0F;
   private static final float STATIC_DESCRIPTION_ALPHA_GUARD_SCALE = 255.0F;
   private static final float STATIC_DESCRIPTION_HOVER_OFFSET_X = 8.0F;
   private static final float STATIC_DESCRIPTION_OFFSET_Y = 32.0F;
   private static final float STATIC_DESCRIPTION_ALPHA_DIVISOR = 255.0F;
   private static final float MARQUEE_DESCRIPTION_ALPHA_GUARD_SCALE = 255.0F;
   private static final float DESCRIPTION_MARQUEE_GAP = 42.0F;
   private static final float MARQUEE_DESCRIPTION_OFFSET_Y = 32.0F;
   private static final float MARQUEE_DESCRIPTION_ALPHA_DIVISOR = 255.0F;
   private static final float UNCLIPPED_DESCRIPTION_OFFSET_Y = 32.0F;
   private static final float OVERLAY_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float FAVORITE_HITBOX_RIGHT_OFFSET = 68.0F;
   private static final float FAVORITE_HITBOX_OFFSET_Y = 8.0F;
   private static final double FAVORITE_HITBOX_WIDTH = 20.0;
   private static final double FAVORITE_HITBOX_HEIGHT = 22.0;
   private static final double HEADER_CLICK_HEIGHT = 50.0;
   private static final float KEYBIND_POPUP_LEFT_OFFSET = 173.0F;
   private static final float KEYBIND_POPUP_WIDTH = 167.0F;
   private static final float KEYBIND_POPUP_HEIGHT = 84.0F;
   private static final float CLICK_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float MILLIS_PER_SECOND = 1000.0F;
   private static final float FOCUS_GLARE_TRAVEL_PHASE_FRACTION = 0.9F;
   private static final float FOCUS_GLARE_INITIAL_ALPHA = 0.5F;
   private static final float FOCUS_GLARE_FADE_ALPHA_BASE = 1.5F;
   private static final float GLOBAL_ALPHA_DIVISOR = 255.0F;
   private static final float FOCUS_GLARE_VISIBILITY_THRESHOLD = 0.001F;
   private static final float FOCUS_GLARE_WIDTH_FRACTION = 0.45F;
   private static final float RELEASE_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float KEY_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float CHAR_DISPATCH_EXPANSION_THRESHOLD = 0.01F;
   private static final float TICK_DISPATCH_EXPANSION_THRESHOLD = 0.01F;

   public boolean areSettingsVisible() {
      return !this.settingWidgets.isEmpty() && (this.settingsExpanded || this.screen.areSettingsPinned());
   }

   public void setSettingsExpanded(boolean expanded) {
      this.settingsExpanded = expanded && !this.settingWidgets.isEmpty();
      if (!this.areSettingsVisible()) {
         this.resetChildTransientState();
      }
   }

   public void focusSettings() {
      if (!this.settingWidgets.isEmpty()) {
         this.focusGlareStartMillis = System.currentTimeMillis();
         if (!this.screen.areSettingsPinned()) {
            this.settingsExpanded = true;
            this.screen.focusModule(this);
         }
      }
   }

   public void rebuildSettingWidgets() {
      this.settingWidgets.clear();
      boolean hideOtherSettings = this.module.getSettingsList().stream().filter(ActionSetting.class::isInstance).map(ActionSetting.class::cast).anyMatch(ActionSetting::hidesOtherSettings);

      for (ValueSetting setting : this.module.getSettingsList()) {
         if (setting instanceof ActionSetting actionSetting) {
            Runnable action = actionSetting.getAction();
            this.settingWidgets.add(new ActionSettingWidget(actionSetting, action));
         } else if (!hideOtherSettings) {
            if (setting instanceof BooleanSetting booleanSetting) {
               this.settingWidgets.add(new BooleanSettingWidget(booleanSetting));
            } else if (setting instanceof KeybindSetting keybindSetting) {
               this.settingWidgets.add(new KeybindSettingWidget(keybindSetting));
            } else if (setting instanceof ColorSetting colorSetting) {
               this.settingWidgets.add(new ColorSettingWidget(colorSetting));
            } else if (setting instanceof FloatSetting floatSetting) {
               this.settingWidgets.add(new FloatSettingWidget(floatSetting));
            } else if (setting instanceof TextSetting textSetting) {
               this.settingWidgets.add(new TextSettingWidget(textSetting));
            } else if (setting instanceof TimeOfDaySetting timeOfDaySetting) {
               this.settingWidgets.add(new TimeOfDaySettingWidget(timeOfDaySetting));
            } else if (setting instanceof SingleChoiceSetting singleChoiceSetting) {
               this.settingWidgets.add(new SingleChoiceSettingWidget(singleChoiceSetting));
            } else if (setting instanceof MultiChoiceSetting multiChoiceSetting) {
               this.settingWidgets.add(new MultiChoiceSettingWidget(multiChoiceSetting));
            } else if (setting instanceof RangeSetting rangeSetting) {
               this.settingWidgets.add(new RangeSettingWidget(rangeSetting));
            } else if (setting instanceof SectionHeaderSetting sectionHeaderSetting) {
               this.settingWidgets.add(new SectionHeaderSettingWidget(sectionHeaderSetting));
            }
         }
      }

      this.settingsFingerprint = this.computeSettingsFingerprint();
      if (this.settingWidgets.isEmpty()) {
         this.settingsExpanded = false;
      }
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.areSettingsVisible() && this.settingsExpansionAnimation.getValue() > KEY_DISPATCH_EXPANSION_THRESHOLD) {
         for (SettingWidget widget : this.settingWidgets) {
            widget.keyPressed(keyCode);
         }
      }
   }

   public void resetSettingWidgetTransientState() {
      this.resetChildTransientState();
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (this.areSettingsVisible() && this.settingsExpansionAnimation.getValue() > RELEASE_DISPATCH_EXPANSION_THRESHOLD) {
         for (SettingWidget widget : this.settingWidgets) {
            widget.mouseReleased(mouseX, mouseY, button);
         }
      }
   }

   @Generated
   public boolean isSettingsExpanded() {
      return this.settingsExpanded;
   }

   @Generated
   public FloatAnimation getSettingsExpansionAnimation() {
      return this.settingsExpansionAnimation;
   }

   @Override
   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
      if (this.settingsExpansionAnimation.getValue() > OVERLAY_DISPATCH_EXPANSION_THRESHOLD) {
         float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(previousGlobalAlpha * this.settingsExpansionAnimation.getValue());

         for (SettingWidget widget : this.settingWidgets) {
            if (!this.screen.intersectsModuleViewport(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight())) {
               widget.resetTransientState();
            } else {
               widget.renderOverlay(mouseX, mouseY, tickDelta);
            }
         }

         UiDrawing.setGlobalAlpha(previousGlobalAlpha);
      }
   }

   @Override
   public void keyReleased(int keyCode) {
      for (SettingWidget widget : this.settingWidgets) {
         widget.keyReleased(keyCode);
      }
   }

   private void resetChildTransientState() {
      for (SettingWidget widget : this.settingWidgets) {
         widget.resetTransientState();
      }
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - FAVORITE_HITBOX_RIGHT_OFFSET, this.y + FAVORITE_HITBOX_OFFSET_Y, FAVORITE_HITBOX_WIDTH, FAVORITE_HITBOX_HEIGHT) && button == 0) {
         this.module.toggleFavorite();
         RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
      } else {
         if (WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, HEADER_CLICK_HEIGHT)) {
            if (button == 0) {
               this.module.toggle();
            } else if (button == 1 && !this.settingWidgets.isEmpty() && !this.screen.areSettingsPinned()) {
               this.settingsExpanded = !this.settingsExpanded;
               if (this.settingsExpanded) {
                  this.screen.focusModule(this);
               } else {
                  this.resetChildTransientState();
                  this.screen.clearFocusedModule();
               }
            } else if (button == 2) {
               for (ModuleKeybindWidget keybindWidget : this.screen.getBinds()) {
                  keybindWidget.close();
                  if (keybindWidget.getModule() == this.module) {
                     return;
                  }
               }

               this.screen.getBinds().add(new ModuleKeybindWidget(this.module, this.screen.getGuiX() - KEYBIND_POPUP_LEFT_OFFSET, this.screen.getGuiY(), KEYBIND_POPUP_WIDTH, KEYBIND_POPUP_HEIGHT));
            }
         }

         if (this.areSettingsVisible() && this.settingsExpansionAnimation.getValue() > CLICK_DISPATCH_EXPANSION_THRESHOLD) {
            for (SettingWidget widget : this.settingWidgets) {
               if (widget.getVisibilityAnimation().isVisible()) {
                  widget.mouseClicked(mouseX, mouseY, button);
               }
            }
         }
      }
   }

   @Generated
   public FloatAnimation getHoverAnimation() {
      return this.hoverAnimation;
   }

   private void renderDescription(boolean hovered) {
      if (ClickGuiPreferences.йЩ()) {
         long nowNanos = System.nanoTime();
         float frameSeconds = this.lastDescriptionRenderNanos == 0L ? INITIAL_DESCRIPTION_FRAME_SECONDS : Math.clamp((float)(nowNanos - this.lastDescriptionRenderNanos) / NANOS_PER_SECOND, 0.0F, MAX_DESCRIPTION_FRAME_SECONDS);
         this.lastDescriptionRenderNanos = nowNanos;
         float hoverProgress = Math.clamp(this.hoverAnimation.getValue(), 0.0F, 1.0F);
         if (!hovered && !(hoverProgress > MARQUEE_HOVER_RESET_THRESHOLD)) {
            this.descriptionMarqueeOffset = 0.0F;
         } else {
            this.descriptionMarqueeOffset = this.descriptionMarqueeOffset + frameSeconds * DESCRIPTION_MARQUEE_PIXELS_PER_SECOND;
         }

         float descriptionX = this.x + DESCRIPTION_OFFSET_X;
         float clipY = this.y + DESCRIPTION_CLIP_OFFSET_Y;
         float clipWidth = this.width - DESCRIPTION_WIDTH_PADDING;
         float clipHeight = DESCRIPTION_CLIP_HEIGHT;
         String description = ClientLocalization.translate(this.module.getDescription());
         if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), description, 13) > clipWidth) {
            UiDrawing.pushScissor(descriptionX, clipY, clipWidth, clipHeight);
            int staticAlphaGuard = (int)(STATIC_DESCRIPTION_ALPHA_GUARD_SCALE * (1.0F - hoverProgress));
            if (staticAlphaGuard > 0) {
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  description,
                  descriptionX + hoverProgress * STATIC_DESCRIPTION_HOVER_OFFSET_X,
                  this.y + STATIC_DESCRIPTION_OFFSET_Y,
                  13,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.SUR_TEXT) / STATIC_DESCRIPTION_ALPHA_DIVISOR * (1.0F - hoverProgress))
               );
            }

            int marqueeAlphaGuard = (int)(MARQUEE_DESCRIPTION_ALPHA_GUARD_SCALE * hoverProgress);
            if (marqueeAlphaGuard > 0) {
               float descriptionWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), description, 13);
               float repeatWidth = Math.max(1.0F, descriptionWidth + DESCRIPTION_MARQUEE_GAP);
               float marqueeStartX = descriptionX - this.descriptionMarqueeOffset % repeatWidth;
               int repeatCount = (int)Math.ceil(clipWidth / repeatWidth) + 3;

               for (int repeatIndex = 0; repeatIndex <= repeatCount; repeatIndex++) {
                  UiDrawing.drawText(
                     ClientFonts.getMediumFont(),
                     description,
                     marqueeStartX + repeatIndex * repeatWidth,
                     this.y + MARQUEE_DESCRIPTION_OFFSET_Y,
                     13,
                     ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.SUR_TEXT) / MARQUEE_DESCRIPTION_ALPHA_DIVISOR * hoverProgress)
                  );
               }
            }

            UiDrawing.popScissor();
         } else {
            UiDrawing.drawText(ClientFonts.getMediumFont(), description, descriptionX, this.y + UNCLIPPED_DESCRIPTION_OFFSET_Y, 13, ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT));
         }
      }
   }

   /** Что показать при наведении: подсказка модуля или его настройки. */
   public HoverHint.Content hintAt(float mouseX, float mouseY) {
      if (!WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
         return null;
      }

      if (this.areSettingsVisible() && this.settingsExpansionAnimation.getValue() > CLICK_DISPATCH_EXPANSION_THRESHOLD) {
         for (SettingWidget<? extends ValueSetting<?>> widget : this.settingWidgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
               ValueSetting<?> setting = widget.getSetting();
               return new HoverHint.Content(
                  "setting:" + this.module.getName() + ":" + setting.getName(),
                  ClientLocalization.translate(setting.getName()),
                  settingHint(setting, this.module)
               );
            }
         }
      }

      StringBuilder body = new StringBuilder(ClientLocalization.translate(this.module.getDescription()));
      body.append("\nЛКМ — включить или выключить");
      if (!this.module.getSettingsList().isEmpty()) {
         body.append("\nПКМ — открыть настройки");
      }

      int keyCode = this.module.getKeybind().getKeyCode();
      if (keyCode != -1) {
         body.append("\nКлавиша: ").append(InputBinding.keyboard(keyCode).getDisplayName());
      }

      return new HoverHint.Content(
         "module:" + this.module.getName(), ClientLocalization.translate(this.module.getName()), body.toString()
      );
   }

   private static String settingHint(ValueSetting<?> setting, ClientModule module) {
      if (setting instanceof FloatSetting floatSetting) {
         Vector2f bounds = floatSetting.getBounds();
         return "Ползунок: от " + trimNumber(bounds.x) + " до " + trimNumber(bounds.y) + "\nЛКМ — потянуть, ПКМ — сбросить";
      }

      if (setting instanceof BooleanSetting) {
         return "Переключатель: ЛКМ — включить или выключить";
      }

      if (setting instanceof SingleChoiceSetting choice) {
         List<ChoiceOption> options = choice.getValue();
         if (options != null && !options.isEmpty()) {
            StringBuilder text = new StringBuilder("Варианты:");

            for (ChoiceOption option : options) {
               text.append(' ').append(ClientLocalization.translate(option.getName())).append(',');
            }

            text.setLength(text.length() - 1);
            return text.toString();
         }
      }

      if (setting instanceof KeybindSetting) {
         return "Привязка клавиши: назначь кнопку, которая включает настройку";
      }

      if (setting instanceof ColorSetting) {
         return "Цвет: ЛКМ — открыть палитру";
      }

      if (setting instanceof ActionSetting) {
         return "Кнопка действия: ЛКМ — выполнить";
      }

      if (setting instanceof RangeSetting) {
         return "Два ползунка: нижняя и верхняя граница";
      }

      return "Настройка модуля «" + ClientLocalization.translate(module.getName()) + "»";
   }

   private static String trimNumber(float value) {
      return value == Math.floor(value) ? Long.toString((long)value) : String.format(Locale.ROOT, "%.1f", value);
   }

   @Override
   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      if (!this.areSettingsVisible()) {
         return false;
      }

      boolean consumed = false;

      for (SettingWidget widget : this.settingWidgets) {
         if (widget.handlePriorityMouseButton(mouseX, mouseY, button)) {
            consumed = true;
            break;
         }
      }

      return consumed;
   }

   @Generated
   public FloatAnimation getFavoriteAnimation() {
      return this.favoriteAnimation;
   }

   @Generated
   public ArrayList<SettingWidget<? extends ValueSetting<?>>> getSettingWidgets() {
      return this.settingWidgets;
   }

   @Generated
   public float getDescriptionMarqueeOffset() {
      return this.descriptionMarqueeOffset;
   }

   @Generated
   public ClientModule getModule() {
      return this.module;
   }

   @Override
   public void charTyped(String text, int codePoint, int modifiers) {
      if (this.areSettingsVisible() && this.settingsExpansionAnimation.getValue() > CHAR_DISPATCH_EXPANSION_THRESHOLD) {
         for (SettingWidget widget : this.settingWidgets) {
            widget.charTyped(text, codePoint, modifiers);
         }
      }
   }

   @Generated
   public long getLastDescriptionRenderNanos() {
      return this.lastDescriptionRenderNanos;
   }

   @Generated
   public FloatAnimation getSettingsOvershootAnimation() {
      return this.settingsOvershootAnimation;
   }

   public void updateLayout(float tickDelta) {
      this.refreshSettingWidgetsIfChanged();
      this.settingsExpansionAnimation.updateTarget(this.areSettingsVisible() ? 1.0F : 0.0F);
      this.settingsOvershootAnimation.updateTarget(this.areSettingsVisible() ? 1.0F : 0.0F);
      float settingsHeight = 0.0F;
      if (this.settingsExpansionAnimation.getValue() > LAYOUT_EXPANSION_THRESHOLD) {
         for (SettingWidget widget : this.settingWidgets) {
            widget.getVisibilityAnimation().configureAndInterpolate(0.0F, 1.0F, SETTING_VISIBILITY_TICK_STEP, EasingFunctions.SINE_OUT, tickDelta);
            settingsHeight += (widget.getHeight() + LAYOUT_SETTING_GAP) * widget.getVisibilityAnimation().getInterpolatedValue();
         }

         settingsHeight += SETTINGS_BOTTOM_PADDING;
      }

      this.height = BASE_LAYOUT_HEIGHT + settingsHeight * Math.clamp(this.settingsExpansionAnimation.getValue(), 0.0F, 1.0F);
   }

   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      if (this.areSettingsVisible() && !(this.settingsExpansionAnimation.getValue() <= SCROLL_DISPATCH_EXPANSION_THRESHOLD)) {
         for (SettingWidget widget : this.settingWidgets) {
            if (widget.mouseScrolled(mouseX, mouseY, scrollDelta)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Generated
   public String getSettingsFingerprint() {
      return this.settingsFingerprint;
   }

   public ModuleCard(ClientModule module, ClickGuiScreen screen) {
      super(screen);
      this.enabledAnimation = new FloatAnimation(0.0F, ENABLED_ANIMATION_SPEED);
      this.settingsExpansionAnimation = new FloatAnimation(0.0F, SETTINGS_EXPANSION_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.settingsOvershootAnimation = new FloatAnimation(0.0F, SETTINGS_OVERSHOOT_ANIMATION_SPEED, Easings.BACK_OUT);
      this.hoverAnimation = new FloatAnimation(0.0F, HOVER_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.favoriteAnimation = new FloatAnimation(0.0F, FAVORITE_ANIMATION_SPEED, Easings.CUBIC_OUT);
      this.settingsExpanded = false;
      this.settingsFingerprint = "";
      this.module = module;
      this.height = INITIAL_HEIGHT;
      this.width = INITIAL_WIDTH;
      this.rebuildSettingWidgets();
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.enabledAnimation.updateTarget(this.module.isEnabled() ? 1.0F : 0.0F);
      this.favoriteAnimation.updateTarget(this.module.isFavorite() ? 1.0F : 0.0F);
      boolean headerHovered = WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, HEADER_HOVER_HEIGHT);
      this.hoverAnimation.updateTarget(headerHovered ? 1.0F : 0.0F);
      float settingsExpansion = Math.clamp(this.settingsExpansionAnimation.getValue(), 0.0F, 1.0F);
      float settingsOvershoot = this.settingsOvershootAnimation.getValue();
      UiDrawing.drawRoundedRect(this.x, this.y, this.width, this.height, CARD_BACKGROUND_CORNER_RADIUS, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), CARD_BACKGROUND_ALPHA_FACTOR));
      UiDrawing.drawOutline(this.x, this.y, this.width, this.height, 1.0F, CARD_OUTLINE_CORNER_RADIUS, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      this.renderFocusGlare();
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.module.getName()), this.x + MODULE_NAME_OFFSET_X, this.y + MODULE_NAME_OFFSET_Y, 15, ThemeEditor.getColorArgb(ThemeColorSlot.TEXT));
      if (KeybindConflictResolver.в(this.module)) {
         UiDrawing.drawRoundedRect(this.x + KEYBIND_CONFLICT_OFFSET_X, this.y + KEYBIND_CONFLICT_OFFSET_Y, KEYBIND_CONFLICT_WIDTH, KEYBIND_CONFLICT_HEIGHT, KEYBIND_CONFLICT_CORNER_RADIUS, ColorMath.packArgb(255, 92, 92, KEYBIND_CONFLICT_ALPHA));
      }

      this.renderDescription(headerHovered);
      int toggleTrackArgb = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_STATE), this.enabledAnimation.getValue());
      int toggleKnobArgb = ColorMath.lerpArgb(ColorMath.packArgb(125, 135, 145, DISABLED_KNOB_ALPHA), ColorMath.packArgb(255, 255, 255, ENABLED_KNOB_ALPHA), this.enabledAnimation.getValue());
      float toggleKnobOffsetX = ENABLED_KNOB_TRAVEL * this.enabledAnimation.getValue();
      float favoriteProgress = this.favoriteAnimation.getValue();
      int favoriteIconArgb = ColorMath.lerpArgb(ColorMath.packArgb(186, 190, 200, UNFAVORITED_ICON_ALPHA), ColorMath.packArgb(255, 210, 64, FAVORITED_ICON_ALPHA), favoriteProgress);
      UiDrawing.drawCenteredAsteriskIcon(this.x + this.width - FAVORITE_ICON_RIGHT_OFFSET, this.y + FAVORITE_ICON_OFFSET_Y, FAVORITE_ICON_SIZE, favoriteIconArgb);
      UiDrawing.drawRoundedRect(this.x + this.width - ENABLE_TRACK_RIGHT_OFFSET, this.y + ENABLE_TRACK_OFFSET_Y, ENABLE_TRACK_WIDTH, ENABLE_TRACK_HEIGHT, ENABLE_TRACK_CORNER_RADIUS, toggleTrackArgb);
      UiDrawing.drawRoundedRect(this.x + this.width - ENABLE_KNOB_RIGHT_OFFSET + toggleKnobOffsetX, this.y + ENABLE_KNOB_OFFSET_Y, ENABLE_KNOB_WIDTH, ENABLE_KNOB_HEIGHT, ENABLE_KNOB_CORNER_RADIUS, toggleKnobArgb);
      float previousScissorX = UiDrawing.getScissor().x;
      float previousScissorY = UiDrawing.getScissor().y;
      float previousScissorWidth = UiDrawing.getScissor().z;
      float previousScissorHeight = UiDrawing.getScissor().w;
      float clipLeft = Math.max(this.x, previousScissorX);
      float clipTop = Math.max(this.y, previousScissorY);
      float clipRight = Math.min(this.x + this.width, previousScissorX + previousScissorWidth);
      float clipBottom = Math.min(this.y + this.height, previousScissorY + previousScissorHeight);
      UiDrawing.setScissor(clipLeft, clipTop, Math.max(0.0F, clipRight - clipLeft), Math.max(0.0F, clipBottom - clipTop));
      float settingOffsetY = 0.0F;
      if (this.settingsExpansionAnimation.getValue() > SETTINGS_RENDER_EXPANSION_THRESHOLD) {
         float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
         int settingIndex = 0;

         for (SettingWidget widget : this.settingWidgets) {
            float settingIndexFraction = this.settingWidgets.size() <= 1 ? 0.0F : (float)settingIndex / (this.settingWidgets.size() - 1);
            float revealProgress = Math.clamp(settingsExpansion * SETTING_REVEAL_GAIN - settingIndexFraction * SETTING_REVEAL_STAGGER, 0.0F, 1.0F);
            float revealOffsetX = (1.0F - revealProgress) * SETTING_REVEAL_OFFSET_X;
            float revealOffsetY = (1.0F - revealProgress) * (SETTING_REVEAL_BASE_OFFSET_Y + settingIndexFraction * SETTING_REVEAL_STAGGER_OFFSET_Y);
            float overshootOffsetY = (settingsOvershoot - settingsExpansion) * (SETTING_OVERSHOOT_BASE_OFFSET_Y + settingIndexFraction * SETTING_OVERSHOOT_STAGGER_OFFSET_Y);
            widget.setX(this.x + SETTINGS_LEFT_PADDING + revealOffsetX);
            widget.setY(this.y + SETTINGS_TOP_OFFSET + settingOffsetY * settingsExpansion + revealOffsetY - overshootOffsetY);
            widget.setWidth(this.width - SETTINGS_HORIZONTAL_PADDING - revealOffsetX * 2.0F);
            UiDrawing.setGlobalAlpha(previousGlobalAlpha * revealProgress * widget.getVisibilityAnimation().getInterpolatedValue());
            if (widget.getVisibilityAnimation().isVisible() && this.screen.intersectsModuleViewport(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight())) {
               widget.render(mouseX, mouseY, tickDelta);
            }

            settingOffsetY += (widget.getHeight() + RENDER_SETTING_GAP) * widget.getVisibilityAnimation().getInterpolatedValue();
            settingIndex++;
         }

         UiDrawing.setGlobalAlpha(previousGlobalAlpha);
      }

      UiDrawing.setScissor(previousScissorX, previousScissorY, previousScissorWidth, previousScissorHeight);
   }

   @Override
   public void tick() {
      if (this.settingsExpansionAnimation.getValue() > TICK_DISPATCH_EXPANSION_THRESHOLD) {
         for (SettingWidget widget : this.settingWidgets) {
            widget.getVisibilityAnimation().tick(widget.getSetting().getVisibilityCondition().getAsBoolean());
            widget.tick();
         }
      }
   }

   private void refreshSettingWidgetsIfChanged() {
      String fingerprint = this.computeSettingsFingerprint();
      if (!fingerprint.equals(this.settingsFingerprint)) {
         this.rebuildSettingWidgets();
      }
   }

   private void renderFocusGlare() {
      if (this.focusGlareStartMillis != 0L) {
         float elapsedSeconds = (float)(System.currentTimeMillis() - this.focusGlareStartMillis) / MILLIS_PER_SECOND;
         if (elapsedSeconds >= 1.0F) {
            this.focusGlareStartMillis = 0L;
         } else {
            float travelPhaseFraction = FOCUS_GLARE_TRAVEL_PHASE_FRACTION;
            float travelProgress = Math.clamp(elapsedSeconds / travelPhaseFraction, 0.0F, 1.0F);
            float glareProgress = travelProgress;
            float glareAlpha = FOCUS_GLARE_INITIAL_ALPHA;
            if (elapsedSeconds > travelPhaseFraction) {
               float fadeProgress = Math.clamp((elapsedSeconds - travelPhaseFraction) / (1.0F - travelPhaseFraction), 0.0F, 1.0F);
               glareProgress = 1.0F;
               glareAlpha = FOCUS_GLARE_FADE_ALPHA_BASE - fadeProgress;
            }

            glareAlpha *= UiDrawing.getGlobalAlpha() / GLOBAL_ALPHA_DIVISOR;
            if (glareAlpha > FOCUS_GLARE_VISIBILITY_THRESHOLD) {
               UiDrawing.drawGlare(this.x, this.y, this.width, this.height, glareProgress, glareAlpha, FOCUS_GLARE_WIDTH_FRACTION);
            }
         }
      }
   }

   private String computeSettingsFingerprint() {
      StringBuilder fingerprint = new StringBuilder();

      for (ValueSetting setting : this.module.getSettingsList()) {
         fingerprint.append(setting.getName()).append('\u0000').append(setting.getClass().getName()).append('\n');
      }

      return fingerprint.toString();
   }

   @Generated
   public long getFocusGlareStartMillis() {
      return this.focusGlareStartMillis;
   }

   @Generated
   public FloatAnimation getEnabledAnimation() {
      return this.enabledAnimation;
   }
}
