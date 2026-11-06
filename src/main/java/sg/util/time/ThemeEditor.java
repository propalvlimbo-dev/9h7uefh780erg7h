package sg.util.time;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import settings.render.ClientLocalization;
import sg.theme.ThemePresets;
import sg.ec.ClientFonts;
import sg.modules.impl.misc.ClientSounds;
import sg.client.RoseClient;
import sg.theme.ColorThemeRegistry;
import sg.gui.click.ClickGuiScreen;
import sg.gui.ClickGuiPanelHost;
import sg.settings.ColorSetting;
import sg.theme.ColorTheme;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.ThemeWave;
import sg.gui.widgets.ColorSettingWidget;
import sg.theme.ThemeColorSlot;
import sg.theme.ThemePresetCatalog;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public class ThemeEditor {
   public static final EnumMap<ThemeColorSlot, ColorSetting> COLOR_SETTING_CACHE = new EnumMap<>(ThemeColorSlot.class);
   private final @com.google.gson.annotations.SerializedName("сО")  List<ColorSettingWidget> colorSettingWidgets = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("сг")  Map<String, int[]> namedIntArrays = new LinkedHashMap<>();
   private final @com.google.gson.annotations.SerializedName("п")  ClickGuiPanelHost guiScreen;
   private final @com.google.gson.annotations.SerializedName("сЦ")  FloatAnimation themeListScrollAnimation;
   @SerializedName("В")
   private final NewThemePresetDialog newPresetDialog;
   @SerializedName("В")
   private final ThemePresetPicker presetPicker;
   private final @com.google.gson.annotations.SerializedName("сч")  FloatAnimation newPresetDialogAnimation;
   private final @com.google.gson.annotations.SerializedName("сЭ")  FloatAnimation presetPickerAnimation;
   private final @com.google.gson.annotations.SerializedName("с必")  FloatAnimation colorSettingsScrollAnimation;
   public @com.google.gson.annotations.SerializedName("е8")  boolean newPresetDialogOpen;
   public @com.google.gson.annotations.SerializedName("е5")  boolean presetPickerOpen;
   @SerializedName("е")
   private ColorThemeRegistry themeRegistry;
   private @com.google.gson.annotations.SerializedName("Ъб")  float moduleViewportHeight;
   private @com.google.gson.annotations.SerializedName("ЪЫ")  float themeListScrollOffset;
   private @com.google.gson.annotations.SerializedName("Ъl")  float colorSettingsScrollOffset;
   private @com.google.gson.annotations.SerializedName("Ъ8")  float colorSettingsContentHeight;
   private @com.google.gson.annotations.SerializedName("ев")  boolean colorSettingsScrollbarDragging;
   private @com.google.gson.annotations.SerializedName("Ъ5")  float colorSettingsScrollbarGrabOffset;
   private @com.google.gson.annotations.SerializedName("ХГ")  long themeListTransitionStartNanos;
   private @com.google.gson.annotations.SerializedName("Хл")  long colorPanelTransitionStartNanos;
   private static final float ЯиШ = 10.0F;
   private static final float Яиы = 10.0F;
   private static final float Яий = 5.0F;
   private static final float ЯиЖ = 10.0F;
   private static final float Яим = 18.0F;
   private static final float ЯиК = 56.0F;
   private static final float ЯиI = 200.0F;
   private static final float ЯиТ = 574.0F;
   private static final float ЯиО = 12.0F;
   private static final float ЯиФ = 26.0F;
   private static final float ЯиЙ = 114.0F;
   private static final float Яик = 56.0F;
   private static final float Яиг = 122.0F;
   private static final float Яи0 = 0.08F;
   private static final float Яих = 0.34F;
   private static final float ЯиУ = 34.0F;
   private static final float ЯиЗ = 78.0F;
   private static final float ЯиЕ = 118.0F;
   private static final float ЯиП = 24.0F;
   private static final float Яи西 = 12.0F;
   private static final float Яид = 6.0F;
   private static final String ЯиБ = "Кастомизируй свой стиль";
   private static final float ЯиС = 12.0F;
   private static final float Яиз = 8.0F;
   private static final double Яиц = 255.0;
   private static final float Яия = 14.0F;
   private static final String Я9с = "Кастомизируй свой стиль";
   private static final float Я9В = 14.0F;
   private static final float Я9Я = 8.0F;
   private static final float Я9Х = 12.0F;
   private static final float Я9е = 10.0F;
   private static final double Я9А = 255.0;
   private static final float Я94 = 8.0F;
   private static final double Я9п = 28.0;
   private static final double Я9Д = 13.0;
   private static final String Я9Ъ = "Готовые темы";
   private static final String Я9т = "Готовые темы";
   private static final float Я9а = 12.0F;
   private static final float Я9Ю = 4.5F;
   private static final double Я9Ч = 255.0;
   private static final double Я9Л = 255.0;
   private static final float Я9诶 = 10.0F;
   private static final double Я91 = 13.0;
   private static final float Я9Ь = 4.0F;
   private static final float Я9衣 = 24.0F;
   private static final float Я9ш = 36.0F;
   private static final float Я9И = 48.0F;
   private static final double Я9ь = 13.0;
   private static final float Я9ж = 44.0F;
   private static final float Я9弟 = 6.0F;
   private static final float Я9б = 6.0F;
   private static final float Я9Ы = 0.13F;
   private static final float Я9l = 0.026F;
   private static final float Я98 = 0.26F;
   private static final float Я95 = 12.0F;
   private static final float Я9в = 18.0F;
   private static final float Я9ъ = 6.0F;
   private static final float Я9М = 8.0F;
   private static final float Я9Н = 32.0F;
   private static final float Я9н = 22.0F;
   private static final float Я9Ц = 6.0F;
   private static final float Я9ч = 9.0F;
   private static final float Я9Э = 3.0F;
   private static final float Я9必 = 1.5F;
   private static final float Я9щ = 0.01F;
   private static final float Я9Г = 255.0F;
   private static final float Я9л = 0.5F;
   private static final float Я9э = 150.0F;
   private static final float Я9ю = 0.5F;
   private static final float Я9Щ = 75.0F;
   private static final float Я9у = 0.01F;
   private static final float Я92 = 255.0F;
   private static final float Я9ф = 0.3F;
   private static final float Я9и = 34.0F;
   private static final float Я99 = 124.0F;
   private static final float Я9о = 12.0F;
   private static final float Я9O = 46.0F;
   private static final float Я97 = 30.0F;
   private static final float Я9Р = 10.0F;
   private static final double Я93 = 13.0;
   private static final String Я9р = "Создать пресет";
   private static final float Я96 = 12.0F;
   private static final float Я9_ = 40.0F;
   private static final double Я9Ш = 255.0;
   private static final String Я9ы = "}";
   private static final float Я9й = 12.0F;
   private static final String Я9Ж = "}";
   private static final float Я9м = 14.0F;
   private static final float Я9К = 39.0F;
   private static final double Я9I = 255.0;
   private static final float Я9Т = 0.07F;
   private static final float Я9О = 0.045F;
   private static final float Я9Ф = 0.3F;
   private static final float Я9Й = -20.0F;
   private static final float Я9к = 20.0F;
   private static final float Я9г = 18.0F;
   private static final float Я90 = 52.0F;
   private static final float Я9х = 52.0F;
   private static final float Я9У = 10.0F;
   private static final double Я9З = 13.0;
   private static final float Я9Е = 12.0F;
   private static final float Я9П = 8.0F;
   private static final double Я9西 = 255.0;
   private static final double Я9д = 255.0;
   private static final float Я9Б = 12.0F;
   private static final float Я9С = 28.0F;
   private static final double Я9з = 255.0;
   private static final String Я9ц = "{";
   private static final float Я9я = 12.0F;
   private static final String Яос = "{";
   private static final float ЯоВ = 12.0F;
   private static final float ЯоЯ = 8.0F;
   private static final double ЯоХ = 255.0;
   private static final float Яое = 60.0F;
   private static final float ЯоА = 0.3F;
   private static final float Яо4 = 34.0F;
   private static final float Яоп = 124.0F;
   private static final float ЯоД = 78.0F;
   private static final double ЯоЪ = 30.0;
   private static final float Яот = 0.08F;
   private static final float Яоа = 0.34F;
   private static final float ЯоЮ = 34.0F;
   private static final float ЯоЧ = 118.0F;
   private static final float ЯоЛ = 24.0F;
   private static final float Яо诶 = 12.0F;
   private static final float Яо1 = 78.0F;
   private static final float ЯоЬ = 6.0F;
   private static final float Яо衣 = 12.0F;
   private static final float Яош = 0.07F;
   private static final float ЯоИ = 0.045F;
   private static final float Яоь = 0.3F;
   private static final float Яож = -20.0F;
   private static final float Яо弟 = 20.0F;
   private static final float Яоб = 18.0F;
   private static final float ЯоЫ = 52.0F;
   private static final double Яоl = 52.0;
   private static final float Яо8 = 60.0F;
   private static final float Яо5 = 122.0F;
   private static final float Яов = 13.0F;
   private static final float Яоъ = 122.0F;
   private static final double ЯоМ = 12.0;
   private static final float ЯоН = 1.0E9F;
   private static final String Яон = "...";
   private static final float ЯоЦ = 0.5F;
   private static final float Яоч = 0.5F;
   private static final float ЯоЭ = 0.5F;
   private static final float Яо必 = 0.5F;
   private static final float Яощ = 0.3F;
   private static final float ЯоГ = 34.0F;
   private static final float Яол = 124.0F;
   private static final float Яоэ = 12.0F;
   private static final float Яою = 124.0F;
   private static final float ЯоЩ = 12.0F;
   private static final float Яоу = 124.0F;
   private static final float Яо2 = 42.0F;
   private static final float Яоф = 0.08F;
   private static final float Яои = 0.34F;
   private static final float Яо9 = 34.0F;
   private static final float Яоо = 122.0F;
   private static final float ЯоO = 42.0F;
   private static final float Яо7 = 28.0F;
   private static final float ЯоР = 122.0F;
   private static final float Яо3 = 122.0F;
   private static final float Яор = 0.5F;
   private static final int Яо6 = 16777215;
   private static final int Яо_ = 16777215;
   private static final String ЯоШ = "Interface";
   private static final String Яоы = "Ocean";

   @Generated
   public NewThemePresetDialog getNewPresetDialog() {
      return this.newPresetDialog;
   }

   @Generated
   public FloatAnimation getColorSettingsScrollAnimation() {
      return this.colorSettingsScrollAnimation;
   }

   @Generated
   public boolean isNewPresetDialogOpen() {
      return this.newPresetDialogOpen;
   }

   public static boolean isTwoColorGradientEnabled() {
      return isRainbowEnabled() ? false : (getBaseColorArgb(ThemeColorSlot.MAIN) & Яо6) != (getBaseColorArgb(ThemeColorSlot.MAIN_TWO) & Яо_);
   }

   public boolean mouseClicked(float mouseX, float mouseY, int button) {
      float guiY = this.guiScreen.getGuiY();
      float themeListProgress = this.computeEntranceProgress(this.themeListTransitionStartNanos, 0.0F, ЯоА);
      float themeListX = this.getThemeListX() - Яо4 * (1.0F - themeListProgress);
      float themeListTop = guiY + Яоп;
      if (this.presetPickerOpen) {
         return this.presetPicker.Ю(mouseX, mouseY, button);
      }

      if (WorldProjection.containsPoint(mouseX, mouseY, themeListX, guiY + ЯоД, this.getThemeListWidth(), ЯоЪ)) {
         this.newPresetDialogOpen = true;
      }

      float colorPanelProgress = this.computeEntranceProgress(this.colorPanelTransitionStartNanos, Яот, Яоа);
      float colorPanelX = this.getColorPanelX() + ЯоЮ * (1.0F - colorPanelProgress);
      float presetButtonWidth = ЯоЧ;
      float presetButtonHeight = ЯоЛ;
      float presetButtonX = colorPanelX + this.getColorPanelWidth() - Яо诶 - presetButtonWidth;
      float presetButtonY = guiY + Яо1 + ЯоЬ;
      if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, presetButtonX, presetButtonY, presetButtonWidth, presetButtonHeight)) {
         this.presetPicker.л();
         this.presetPickerOpen = true;
         return true;
      }

      if (this.newPresetDialogOpen && this.newPresetDialog.г(mouseX, mouseY, button)) {
         return true;
      }

      if (this.newPresetDialogOpen) {
         return false;
      }

      float themeListViewportHeight = guiY + this.guiScreen.getGuiHeight() - Яо衣 - themeListTop;
      float animatedThemeListScroll = this.themeListScrollAnimation.getValue();
      float rowOffsetY = animatedThemeListScroll;
      int themeIndex = 0;

      for (ColorTheme theme : this.themeRegistry) {
         float rowProgress = this.computeEntranceProgress(this.themeListTransitionStartNanos, Яош + Math.min(themeIndex, 8) * ЯоИ, Яоь);
         float rowX = themeListX + (themeIndex % 2 == 0 ? Яож : Яо弟) * (1.0F - rowProgress);
         float rowY = themeListTop + rowOffsetY + Яоб * (1.0F - rowProgress);
         boolean rowVisible = rowY + ЯоЫ >= themeListTop && rowY <= themeListTop + themeListViewportHeight;
         if (rowVisible && WorldProjection.containsPoint(mouseX, mouseY, rowX, rowY, this.getThemeListWidth(), Яоl)) {
            if (button == 0) {
               ClientSounds.к(ClientSounds.GuiSound.THEME, (float)themeIndex / Math.max(1, this.themeRegistry.size() - 1));
               this.runWithThemeWave(mouseX, mouseY, () -> this.themeRegistry.selectTheme(theme));
               this.colorPanelTransitionStartNanos = System.nanoTime();
            } else if (button == 1 && theme.isUserCreated()) {
               this.themeRegistry.deleteUserTheme(theme);
               float themeListContentHeight = this.themeRegistry.isEmpty() ? 0.0F : this.themeRegistry.size() * 60 - 8;
               float themeListOverflow = Math.max(0.0F, themeListContentHeight - themeListViewportHeight);
               this.themeListScrollOffset = Math.clamp(this.themeListScrollOffset, -themeListOverflow, 0.0F);
            }

            return true;
         }

         rowOffsetY += Яо8;
         themeIndex++;
      }

      boolean colorPanelHovered = WorldProjection.containsPoint(mouseX, mouseY, colorPanelX, guiY + Яо5, this.getColorPanelWidth(), this.getColorSettingsViewportHeight());
      if (this.themeRegistry.isUserThemeEditable()) {
         for (int widgetIndex = this.colorSettingWidgets.size() - 1; widgetIndex >= 0; widgetIndex--) {
            ColorSettingWidget widget = this.colorSettingWidgets.get(widgetIndex);
            if (this.isColorSettingWidgetVisible(widget) && widget.handlePriorityMouseButton(mouseX, mouseY, button)) {
               return true;
            }
         }
      }

      if (button == 0 && this.colorSettingsContentHeight > this.getColorSettingsViewportHeight() && WorldProjection.containsPoint(mouseX, mouseY, colorPanelX + this.getColorPanelWidth() - Яов, guiY + Яоъ, ЯоМ, this.getColorSettingsViewportHeight())) {
         this.beginColorSettingsScrollbarDrag(mouseY);
         return true;
      }

      if (this.themeRegistry.isUserThemeEditable() && colorPanelHovered) {
         boolean widgetHovered = false;

         for (ColorSettingWidget widget : this.colorSettingWidgets) {
            boolean widgetVisible = this.isColorSettingWidgetVisible(widget);
            if (widgetVisible) {
               if (widget.isPreviewHovered(mouseX, mouseY)) {
                  widgetHovered = true;
               }

               widget.mouseClicked(mouseX, mouseY, button);
            }
         }

         return widgetHovered;
      } else {
         return false;
      }
   }

   @Generated
   public float getModuleViewportHeight() {
      return this.moduleViewportHeight;
   }

   private void runWithThemeWave(float mouseX, float mouseY, Runnable mutation) {
      int[] previousColorsSnapshot = this.themeRegistry.captureColorsSnapshot();
      mutation.run();
      this.guiScreen.startThemeTransition(mouseX, mouseY, previousColorsSnapshot, this.themeRegistry.captureColorsSnapshot());
   }

   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (button == 0) {
         this.colorSettingsScrollbarDragging = false;
      }

      if (this.themeRegistry.isUserThemeEditable()) {
         float guiY = this.guiScreen.getGuiY();

         for (ColorSettingWidget widget : this.colorSettingWidgets) {
            if (this.isColorSettingWidgetVisible(widget)) {
               widget.mouseReleased(mouseX, mouseY, button);
            }
         }

         this.themeRegistry.commitUserThemeColors();
      }
   }

   @Generated
   public FloatAnimation getPresetPickerAnimation() {
      return this.presetPickerAnimation;
   }

   private void updateColorSettingsScrollbar(float mouseY) {
      float scrollOverflow = Math.max(0.0F, this.colorSettingsContentHeight - this.getColorSettingsViewportHeight());
      float thumbTravel = this.getColorSettingsViewportHeight() - this.getColorSettingsScrollbarThumbHeight();
      if (!(scrollOverflow <= 0.0F) && !(thumbTravel <= 0.0F)) {
         float scrollProgress = Math.clamp((mouseY - this.colorSettingsScrollbarGrabOffset - (this.guiScreen.getGuiY() + Яо3)) / thumbTravel, 0.0F, 1.0F);
         float previousScrollOffset = this.colorSettingsScrollOffset;
         this.colorSettingsScrollOffset = -scrollProgress * scrollOverflow;
         this.colorSettingsScrollAnimation.setImmediately(this.colorSettingsScrollOffset);
         if (previousScrollOffset != this.colorSettingsScrollOffset) {
            ClientSounds.к(ClientSounds.GuiSound.SCROLL);
         }
      } else {
         this.colorSettingsScrollOffset = 0.0F;
         this.colorSettingsScrollAnimation.setImmediately(0.0F);
         this.colorSettingsScrollbarDragging = false;
      }
   }

   @Generated
   public boolean isPresetPickerOpen() {
      return this.presetPickerOpen;
   }

   private float getColorSettingsScrollbarThumbHeight() {
      float viewportHeight = this.getColorSettingsViewportHeight();
      return Math.min(viewportHeight, Math.max(Яо7, viewportHeight * viewportHeight / Math.max(1.0F, this.colorSettingsContentHeight)));
   }

   @Generated
   public float getColorSettingsScrollOffset() {
      return this.colorSettingsScrollOffset;
   }

   public static int getColorArgb(ThemeColorSlot slot) {
      int baseArgb = getBaseColorArgb(slot);
      int alpha = ColorMath.getAlpha(baseArgb);
      if (isAnimatedColorSlot(slot) && isRainbowEnabled()) {
         return ThemePresetCatalog.getRainbowColor(slot, alpha);
      } else if (isAnimatedColorSlot(slot) && isTwoColorGradientEnabled()) {
         float gradientPhase = slot == ThemeColorSlot.MAIN_TWO ? Яор : 0.0F;
         return ColorMath.oscillatingGradient(getBaseColorArgb(ThemeColorSlot.MAIN), getBaseColorArgb(ThemeColorSlot.MAIN_TWO), gradientPhase, alpha);
      } else {
         return baseArgb;
      }
   }

   private float getThemeListX() {
      return this.guiScreen.getGuiX() + this.guiScreen.getSidebarWidth() + Яим;
   }

   @Generated
   public FloatAnimation getNewPresetDialogAnimation() {
      return this.newPresetDialogAnimation;
   }

   @Generated
   public FloatAnimation getThemeListScrollAnimation() {
      return this.themeListScrollAnimation;
   }

   @Generated
   public long getColorPanelTransitionStartNanos() {
      return this.colorPanelTransitionStartNanos;
   }

   @Generated
   public void setNewPresetDialogOpen(boolean open) {
      this.newPresetDialogOpen = open;
   }

   @Generated
   public boolean isColorSettingsScrollbarDragging() {
      return this.colorSettingsScrollbarDragging;
   }

   public ColorTheme createUserTheme(String name) {
      return this.themeRegistry.createUserTheme(name);
   }

   private static ColorSetting resolveColorSetting(ThemeColorSlot slot) {
      RoseClient client = RoseClient.getInstance();
      if (client != null && client.getServices() != null && client.getServices().getThemes() != null) {
         ColorSetting setting = client.getServices().getThemes().getColorSetting(slot);
         if (setting != null) {
            COLOR_SETTING_CACHE.put(slot, setting);
            return setting;
         }
      }

      return COLOR_SETTING_CACHE.get(slot);
   }

   public void render(GuiGraphicsExtractor graphics, float mouseX, float mouseY, float guiScrollOffset) {
      float guiY = this.guiScreen.getGuiY();
      float colorPanelProgress = this.computeEntranceProgress(this.colorPanelTransitionStartNanos, Яи0, Яих);
      float colorPanelX = this.getColorPanelX() + ЯиУ * (1.0F - colorPanelProgress);
      float panelTop = guiY + ЯиЗ;
      float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(previousGlobalAlpha * colorPanelProgress);
      ColorTheme activeTheme = this.themeRegistry.getActiveTheme();
      String localizedThemeName = activeTheme == null ? "" : ClientLocalization.translate(activeTheme.getName());
      float presetButtonWidth = ЯиЕ;
      float presetButtonHeight = ЯиП;
      float presetButtonX = colorPanelX + this.getColorPanelWidth() - Яи西 - presetButtonWidth;
      float presetButtonY = panelTop + Яид;
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(ЯиБ), colorPanelX + ЯиС, panelTop + Яиз, 14, ColorMath.packArgb(135, 135, 135, Яиц));
      float themeNameX = colorPanelX + Яия + ClientFonts.getMediumFont().getFontFace().measureTextWidth(ClientLocalization.translate(Я9с), Я9В, 0.0F);
      float themeNameAvailableWidth = Math.max(0.0F, presetButtonX - Я9Я - themeNameX);
      String truncatedThemeName = this.truncateTextWithEllipsis("{" + localizedThemeName + "}", themeNameAvailableWidth, Я9Х);
      UiDrawing.drawText(ClientFonts.getMediumFont(), truncatedThemeName, themeNameX, panelTop + Я9е, 12, ColorMath.packArgb(90, 90, 90, Я9А));
      boolean presetButtonHovered = WorldProjection.containsPoint(mouseX, mouseY, presetButtonX, presetButtonY, presetButtonWidth, presetButtonHeight);
      UiDrawing.drawOutline(presetButtonX, presetButtonY, presetButtonWidth, presetButtonHeight, 1.0F, Я94, ColorMath.packArgb(255, 255, 255, presetButtonHovered ? Я9п : Я9Д));
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         ClientLocalization.translate(Я9Ъ),
         presetButtonX + (presetButtonWidth - ClientFonts.getMediumFont().getFontFace().measureTextWidth(ClientLocalization.translate(Я9т), Я9а, 0.0F)) / 2.0F,
         presetButtonY + Я9Ю,
         12,
         presetButtonHovered ? ColorMath.packArgb(220, 220, 220, Я9Ч) : ColorMath.packArgb(135, 135, 135, Я9Л)
      );
      UiDrawing.drawOutline(colorPanelX, panelTop, this.getColorPanelWidth(), this.getColorPanelHeight(), 1.0F, Я9诶, ColorMath.packArgb(255, 255, 255, Я91));
      float var20 = Я9Ь * colorPanelProgress * (1.0F - colorPanelProgress);
      UiDrawing.drawRoundedRect(colorPanelX + Я9衣, panelTop + Я9ш, this.getColorPanelWidth() - Я9И, 1.0F, 0.0F, ColorMath.packArgb(255, 255, 255, Я9ь));
      float settingsTop = panelTop + Я9ж;
      float settingsViewportHeight = this.getColorSettingsViewportHeight();
      this.colorSettingsContentHeight = this.colorSettingWidgets.isEmpty() ? 0.0F : Я9弟;

      for (ColorSettingWidget widget : this.colorSettingWidgets) {
         this.colorSettingsContentHeight = this.colorSettingsContentHeight + (widget.getHeight() + Я9б);
      }

      float scrollOverflow = Math.max(0.0F, this.colorSettingsContentHeight - settingsViewportHeight);
      this.colorSettingsScrollOffset = Math.clamp(this.colorSettingsScrollOffset, -scrollOverflow, 0.0F);
      if (this.isAnyDialogOpen()) {
         this.colorSettingsScrollbarDragging = false;
      }

      if (this.colorSettingsScrollbarDragging) {
         this.updateColorSettingsScrollbar(mouseY);
      }

      this.colorSettingsScrollAnimation.updateTarget(this.colorSettingsScrollOffset);
      float animatedSettingsScroll = this.colorSettingsScrollAnimation.getValue();
      float widgetOffsetY = animatedSettingsScroll;
      UiDrawing.setScissor(colorPanelX, settingsTop, this.getColorPanelWidth(), settingsViewportHeight);
      int widgetIndex = 0;

      for (ColorSettingWidget widget : this.colorSettingWidgets) {
         float widgetProgress = this.computeEntranceProgress(this.colorPanelTransitionStartNanos, Я9Ы + Math.min(widgetIndex, 8) * Я9l, Я98);
         widget.setX(colorPanelX + Я95 + Я9в * (1.0F - widgetProgress));
         widget.setY(settingsTop + widgetOffsetY + Я9ъ + Я9М * (1.0F - widgetProgress));
         widget.setWidth(this.getColorPanelWidth() - (scrollOverflow > 0.0F ? Я9Н : Я9н));
         if (widget.getY() + widget.getHeight() >= settingsTop && widget.getY() <= settingsTop + settingsViewportHeight) {
            UiDrawing.setGlobalAlpha(previousGlobalAlpha * colorPanelProgress * widgetProgress);
            widget.render(mouseX, mouseY, 0.0F);
            UiDrawing.setGlobalAlpha(previousGlobalAlpha * colorPanelProgress);
         }

         widgetOffsetY += widget.getHeight() + Я9Ц;
         widgetIndex++;
      }

      UiDrawing.disableScissor();
      if (scrollOverflow > 0.0F) {
         float thumbHeight = this.getColorSettingsScrollbarThumbHeight();
         float thumbTravel = settingsViewportHeight - thumbHeight;
         float scrollProgress = Math.clamp(-animatedSettingsScroll / scrollOverflow, 0.0F, 1.0F);
         UiDrawing.drawRoundedRect(colorPanelX + this.getColorPanelWidth() - Я9ч, settingsTop + thumbTravel * scrollProgress, Я9Э, thumbHeight, Я9必, getColorArgb(ThemeColorSlot.SCROLL));
      }

      UiDrawing.setGlobalAlpha(previousGlobalAlpha);
      this.renderThemeList(graphics, mouseX, mouseY, guiScrollOffset);
      if (this.themeRegistry.isUserThemeEditable()) {
         UiDrawing.setGlobalAlpha(previousGlobalAlpha * colorPanelProgress);

         for (ColorSettingWidget widget : this.colorSettingWidgets) {
            if (this.isColorSettingWidgetVisible(widget)) {
               widget.renderOverlay(mouseX, mouseY, 0.0F);
            }
         }

         UiDrawing.setGlobalAlpha(previousGlobalAlpha);
      }

      this.moduleViewportHeight = this.guiScreen.getModuleViewportHeight();
   }

   public void tick() {
      for (ColorSettingWidget widget : this.colorSettingWidgets) {
         widget.tick();
      }

      this.themeRegistry.commitChangedUserThemeColors();
   }

   private void rebuildColorSettingWidgets() {
      this.colorSettingWidgets.clear();
      COLOR_SETTING_CACHE.clear();
      this.themeRegistry = RoseClient.getInstance().getServices().getThemes();

      for (ThemeColorSlot slot : ThemeColorSlot.values()) {
         ColorSetting setting = this.themeRegistry.getColorSetting(slot);
         if (setting == null) {
            throw new IllegalStateException("Не найден ColorSetting для " + slot);
         }

         COLOR_SETTING_CACHE.put(slot, setting);
         this.colorSettingWidgets.add(new ColorSettingWidget(setting));
      }
   }

   public void renderOverlays(GuiGraphicsExtractor graphics, float mouseX, float mouseY, float guiScrollOffset) {
      float guiY = this.guiScreen.getGuiY();
      this.newPresetDialogAnimation.updateTarget(this.newPresetDialogOpen ? 1.0F : 0.0F);
      if (this.newPresetDialogAnimation.getValue() > Я9щ) {
         UiDrawing.setGlobalAlpha(Я9Г * this.newPresetDialogAnimation.getValue());
         this.newPresetDialog.Ь(graphics, mouseX, mouseY, guiScrollOffset, this.guiScreen.getGuiX() + this.guiScreen.getGuiWidth() * Я9л - Я9э, guiY + this.guiScreen.getGuiHeight() * Я9ю - Я9Щ);
         UiDrawing.resetGlobalAlpha();
      }

      this.presetPickerAnimation.updateTarget(this.presetPickerOpen ? 1.0F : 0.0F);
      if (this.presetPickerAnimation.getValue() > Я9у) {
         ThemeWave.INSTANCE.pushUiRectSuppression();
         UiDrawing.setGlobalAlpha(Я92 * this.presetPickerAnimation.getValue());
         this.presetPicker.Ю(graphics, mouseX, mouseY, guiScrollOffset);
         UiDrawing.resetGlobalAlpha();
         ThemeWave.INSTANCE.popUiRectSuppression();
      }
   }

   public boolean deleteUserTheme(String name) {
      return this.themeRegistry.deleteUserTheme(name);
   }

   public boolean selectThemeByNameWithWave(String name) {
      boolean[] selectionResult = new boolean[1];
      this.runWithThemeWave(this.guiScreen.getGuiX() + this.guiScreen.getGuiWidth() * ЯоЭ, this.guiScreen.getGuiY() + this.guiScreen.getGuiHeight() * Яо必, () -> selectionResult[0] = this.themeRegistry.selectThemeByName(name));
      return selectionResult[0];
   }

   private float getColorPanelWidth() {
      return this.guiScreen.getGuiX() + this.guiScreen.getGuiWidth() - ЯиФ - this.getColorPanelX();
   }

   public void restartEntranceTransitions() {
      this.colorSettingsScrollbarDragging = false;
      long nowNanos = System.nanoTime();
      this.themeListTransitionStartNanos = nowNanos;
      this.colorPanelTransitionStartNanos = nowNanos;
   }

   private static int resolveOpaqueInterfaceFallback(ThemeColorSlot slot, int argb) {
      if (slot != null && argb >>> 24 == 0) {
         if (!ЯоШ.equals(slot.getGroupName())) {
            return argb;
         } else {
            int[] fallbackColors = ThemePresets.getBuiltinPalettes().get(Яоы);
            if (fallbackColors == null) {
               return argb;
            } else {
               int slotIndex = slot.ordinal();
               if (slotIndex >= 0 && slotIndex < fallbackColors.length) {
                  int fallbackArgb = fallbackColors[slotIndex];
                  return fallbackArgb >>> 24 != 0 ? fallbackArgb : argb;
               } else {
                  return argb;
               }
            }
         }
      } else {
         return argb;
      }
   }

   @Generated
   public Map<String, int[]> getNamedIntArrays() {
      return this.namedIntArrays;
   }

   private String truncateTextWithEllipsis(String text, float availableWidth, float fontSize) {
      if (availableWidth <= 0.0F) {
         return "";
      }

      if (ClientFonts.getMediumFont().getFontFace().measureTextWidth(text, fontSize, 0.0F) <= availableWidth) {
         return text;
      }

      String ellipsis = Яон;
      float ellipsisWidth = ClientFonts.getMediumFont().getFontFace().measureTextWidth(ellipsis, fontSize, 0.0F);
      if (ellipsisWidth >= availableWidth) {
         return ellipsis;
      }

      int cutIndex = text.length();

      while (cutIndex > 0) {
         cutIndex = text.offsetByCodePoints(cutIndex, -1);
         String candidate = text.substring(0, cutIndex) + ellipsis;
         if (ClientFonts.getMediumFont().getFontFace().measureTextWidth(candidate, fontSize, 0.0F) <= availableWidth) {
            return candidate;
         }
      }

      return ellipsis;
   }

   public float getHeight() {
      return this.moduleViewportHeight;
   }

   public static boolean isRainbowEnabled() {
      RoseClient client = RoseClient.getInstance();
      return client != null && client.getServices() != null && client.getServices().getThemes() != null ? client.getServices().getThemes().isRainbowEnabled() : false;
   }

   @Generated
   public List<ColorSettingWidget> getColorSettingWidgets() {
      return this.colorSettingWidgets;
   }

   private float getColorSettingsViewportHeight() {
      return this.getColorPanelHeight() - Яик;
   }

   public static int getBaseColorArgb(ThemeColorSlot slot) {
      ColorSetting setting = resolveColorSetting(slot);
      int argb = setting != null ? setting.getValue() : 0;
      return resolveOpaqueInterfaceFallback(slot, argb);
   }

   @Generated
   public ColorThemeRegistry getThemeRegistry() {
      return this.themeRegistry;
   }

   @Generated
   public float getColorSettingsContentHeight() {
      return this.colorSettingsContentHeight;
   }

   public ColorTheme createUserTheme(String name, String author) {
      return this.themeRegistry.createUserTheme(name, author);
   }

   @Generated
   public float getThemeListScrollOffset() {
      return this.themeListScrollOffset;
   }

   public boolean mouseScrolled(float mouseX, float mouseY, double amount) {
      if (this.presetPickerOpen) {
         return this.presetPicker.Ю(mouseX, mouseY, amount);
      }

      if (this.newPresetDialogOpen) {
         return true;
      }

      float guiY = this.guiScreen.getGuiY();
      float themeListProgress = this.computeEntranceProgress(this.themeListTransitionStartNanos, 0.0F, Яощ);
      float themeListX = this.getThemeListX() - ЯоГ * (1.0F - themeListProgress);
      if (WorldProjection.containsPoint(mouseX, mouseY, themeListX, guiY + Яол, this.getThemeListWidth(), guiY + this.guiScreen.getGuiHeight() - Яоэ - (guiY + Яою))) {
         float themeListViewportHeight = guiY + this.guiScreen.getGuiHeight() - ЯоЩ - (guiY + Яоу);
         float themeListContentHeight = this.themeRegistry.isEmpty() ? 0.0F : this.themeRegistry.size() * 60 - 8;
         float themeListOverflow = Math.max(0.0F, themeListContentHeight - themeListViewportHeight);
         if (themeListOverflow <= 0.0F) {
            this.themeListScrollOffset = 0.0F;
            return true;
         } else {
            this.themeListScrollOffset = this.themeListScrollOffset + (float)amount * Яо2;
            this.themeListScrollOffset = Math.clamp(this.themeListScrollOffset, -themeListOverflow, 0.0F);
            return true;
         }
      } else {
         float colorPanelProgress = this.computeEntranceProgress(this.colorPanelTransitionStartNanos, Яоф, Яои);
         float colorPanelX = this.getColorPanelX() + Яо9 * (1.0F - colorPanelProgress);
         if (WorldProjection.containsPoint(mouseX, mouseY, colorPanelX, guiY + Яоо, this.getColorPanelWidth(), this.getColorSettingsViewportHeight())) {
            float colorSettingsOverflow = Math.max(0.0F, this.colorSettingsContentHeight - this.getColorSettingsViewportHeight());
            if (colorSettingsOverflow <= 0.0F) {
               this.colorSettingsScrollOffset = 0.0F;
               return true;
            } else {
               this.colorSettingsScrollOffset = this.colorSettingsScrollOffset + (float)amount * ЯоO;
               this.colorSettingsScrollOffset = Math.clamp(this.colorSettingsScrollOffset, -colorSettingsOverflow, 0.0F);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isAnyDialogOpen() {
      return this.newPresetDialogOpen || this.presetPickerOpen;
   }

   public static float getColorAlpha(ThemeColorSlot slot) {
      return getColorArgb(slot) >>> 24 & 0xFF;
   }

   @Generated
   public float getColorSettingsScrollbarGrabOffset() {
      return this.colorSettingsScrollbarGrabOffset;
   }

   private static boolean isAnimatedColorSlot(ThemeColorSlot slot) {
      return slot == ThemeColorSlot.MAIN || slot == ThemeColorSlot.MAIN_TWO || slot == ThemeColorSlot.ACTIVE_STATE || slot == ThemeColorSlot.CIRCLE || slot == ThemeColorSlot.SCROLL;
   }

   private void renderThemeList(GuiGraphicsExtractor graphics, float mouseX, float mouseY, float guiScrollOffset) {
      float guiY = this.guiScreen.getGuiY();
      float themeListProgress = this.computeEntranceProgress(this.themeListTransitionStartNanos, 0.0F, Я9ф);
      float themeListX = this.getThemeListX() - Я9и * (1.0F - themeListProgress);
      float listTop = guiY + Я99;
      float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
      float listViewportHeight = guiY + this.guiScreen.getGuiHeight() - Я9о - listTop;
      float listContentHeight = this.themeRegistry.isEmpty() ? 0.0F : this.themeRegistry.size() * 60 - 8;
      float scrollOverflow = Math.max(0.0F, listContentHeight - listViewportHeight);
      this.themeListScrollOffset = Math.clamp(this.themeListScrollOffset, -scrollOverflow, 0.0F);
      this.themeListScrollAnimation.updateTarget(this.themeListScrollOffset);
      float animatedScrollOffset = this.themeListScrollAnimation.getValue();
      UiDrawing.setGlobalAlpha(previousGlobalAlpha * themeListProgress);
      UiDrawing.drawOutline(themeListX, listTop - Я9O, this.getThemeListWidth(), Я97, 1.0F, Я9Р, ColorMath.packArgb(255, 255, 255, Я93));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(Я9р), themeListX + Я96, listTop - Я9_, 14, ColorMath.packArgb(135, 135, 135, Я9Ш));
      UiDrawing.drawText(
         ClientFonts.getIconsFont(),
         Я9ы,
         themeListX + this.getThemeListWidth() - Я9й - ClientFonts.getIconsFont().getFontFace().measureTextWidth(Я9Ж, Я9м, 0.0F),
         listTop - Я9К,
         14,
         ColorMath.packArgb(135, 135, 135, Я9I)
      );
      UiDrawing.setGlobalAlpha(previousGlobalAlpha);
      UiDrawing.setScissor(themeListX, listTop, this.getThemeListWidth(), listViewportHeight);
      float rowOffsetY = animatedScrollOffset;
      int themeIndex = 0;

      for (ColorTheme theme : this.themeRegistry) {
         float rowProgress = this.computeEntranceProgress(this.themeListTransitionStartNanos, Я9Т + Math.min(themeIndex, 8) * Я9О, Я9Ф);
         float rowX = themeListX + (themeIndex % 2 == 0 ? Я9Й : Я9к) * (1.0F - rowProgress);
         float rowY = listTop + rowOffsetY + Я9г * (1.0F - rowProgress);
         if (rowY + Я90 >= listTop && rowY <= listTop + listViewportHeight) {
            boolean selected = theme == this.themeRegistry.getActiveTheme();
            UiDrawing.setGlobalAlpha(previousGlobalAlpha * rowProgress);
            UiDrawing.drawOutline(rowX, rowY, this.getThemeListWidth(), Я9х, 1.0F, Я9У, ColorMath.packArgb(255, 255, 255, Я9З));
            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               ClientLocalization.translate(theme.getName()),
               rowX + Я9Е,
               rowY + Я9П,
               14,
               selected ? ColorMath.packArgb(255, 255, 255, Я9西) : ColorMath.packArgb(175, 175, 175, Я9д)
            );
            UiDrawing.drawText(ClientFonts.getMediumFont(), "Author: " + theme.getAuthor(), rowX + Я9Б, rowY + Я9С, 14, ColorMath.packArgb(135, 135, 135, Я9з));
            if (!theme.isUserCreated()) {
               UiDrawing.drawText(
                  ClientFonts.getIconsFont(),
                  Я9ц,
                  rowX + this.getThemeListWidth() - Я9я - ClientFonts.getIconsFont().getFontFace().measureTextWidth(Яос, ЯоВ, 0.0F),
                  rowY + ЯоЯ,
                  12,
                  ColorMath.packArgb(135, 135, 135, ЯоХ)
               );
            }

            UiDrawing.setGlobalAlpha(previousGlobalAlpha);
         }

         rowOffsetY += Яое;
         themeIndex++;
      }

      UiDrawing.disableScissor();
   }

   public ThemeEditor(ClickGuiScreen guiScreen) {
      this(ClickGuiPanelHost.of(guiScreen));
   }

   public ThemeEditor(ClickGuiPanelHost guiScreen) {
      this.themeListScrollAnimation = new FloatAnimation(0.0F, ЯиШ);
      this.newPresetDialog = new NewThemePresetDialog(this);
      this.presetPicker = new ThemePresetPicker(this);
      this.newPresetDialogAnimation = new FloatAnimation(0.0F, Яиы);
      this.presetPickerAnimation = new FloatAnimation(0.0F, Яий, Easings.CUBIC_OUT);
      this.colorSettingsScrollAnimation = new FloatAnimation(0.0F, ЯиЖ);
      this.newPresetDialogOpen = false;
      this.presetPickerOpen = false;
      this.themeListTransitionStartNanos = System.nanoTime();
      this.colorPanelTransitionStartNanos = this.themeListTransitionStartNanos;
      this.guiScreen = guiScreen;
      this.rebuildColorSettingWidgets();
   }

   public ColorTheme applyPresetWithWave(ThemePresetCatalog.ThemePreset preset, float mouseX, float mouseY) {
      if (preset == null) {
         return null;
      }

      ColorTheme[] appliedTheme = new ColorTheme[1];
      this.runWithThemeWave(mouseX, mouseY, () -> appliedTheme[0] = this.themeRegistry.applyPreset(preset.getName(), preset.getColors(), preset.isRainbow()));
      this.colorPanelTransitionStartNanos = System.nanoTime();
      return appliedTheme[0];
   }

   private void beginColorSettingsScrollbarDrag(float mouseY) {
      float scrollOverflow = this.colorSettingsContentHeight - this.getColorSettingsViewportHeight();
      float thumbHeight = this.getColorSettingsScrollbarThumbHeight();
      float thumbTravel = this.getColorSettingsViewportHeight() - thumbHeight;
      float scrollProgress = Math.clamp(-this.colorSettingsScrollAnimation.getValue() / scrollOverflow, 0.0F, 1.0F);
      float thumbTop = this.guiScreen.getGuiY() + ЯоР + thumbTravel * scrollProgress;
      this.colorSettingsScrollbarGrabOffset = mouseY >= thumbTop && mouseY <= thumbTop + thumbHeight ? mouseY - thumbTop : thumbHeight / 2.0F;
      this.colorSettingsScrollbarDragging = true;
      this.updateColorSettingsScrollbar(mouseY);
   }

   private float getColorPanelHeight() {
      return this.guiScreen.getGuiHeight() - ЯиЗ - this.guiScreen.getThemeColorPanelBottomInset();
   }

   @Generated
   public void setPresetPickerOpen(boolean open) {
      this.presetPickerOpen = open;
   }

   @Generated
   public ClickGuiPanelHost getGuiScreen() {
      return this.guiScreen;
   }

   public ColorTheme applyPresetWithWave(ThemePresetCatalog.ThemePreset preset) {
      return this.applyPresetWithWave(preset, this.guiScreen.getGuiX() + this.guiScreen.getGuiWidth() * ЯоЦ, this.guiScreen.getGuiY() + this.guiScreen.getGuiHeight() * Яоч);
   }

   @Generated
   public ThemePresetPicker getPresetPicker() {
      return this.presetPicker;
   }

   private float getThemeListWidth() {
      return (this.guiScreen.getGuiWidth() - this.guiScreen.getSidebarWidth() - ЯиК) * ЯиI / ЯиТ;
   }

   @Generated
   public long getThemeListTransitionStartNanos() {
      return this.themeListTransitionStartNanos;
   }

   private float getColorPanelX() {
      return this.getThemeListX() + this.getThemeListWidth() + ЯиО;
   }

   private float computeEntranceProgress(long startNanos, float delaySeconds, float durationSeconds) {
      float elapsedAfterDelaySeconds = (float)(System.nanoTime() - startNanos) / ЯоН - delaySeconds;
      float progress = Math.clamp(elapsedAfterDelaySeconds / durationSeconds, 0.0F, 1.0F);
      return (float)Easings.CUBIC_OUT.apply(progress);
   }

   private boolean isColorSettingWidgetVisible(ColorSettingWidget widget) {
      float settingsTop = this.guiScreen.getGuiY() + Яиг;
      return widget.getY() + widget.getHeight() >= settingsTop && widget.getY() <= settingsTop + this.getColorSettingsViewportHeight();
   }
}
