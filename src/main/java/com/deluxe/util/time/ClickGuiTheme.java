package com.deluxe.util.time;

import com.deluxe.modules.impl.misc.ModuleCategory;
import java.awt.Color;

public final class ClickGuiTheme {
   public static int accent = new Color(71, 123, 255, 255).getRGB();
   public static int panelBackground = new Color(9, 16, 32, 194).getRGB();
   public static int panelOutline = new Color(71, 123, 255, 15).getRGB();
   public static int headerText = new Color(255, 255, 255, 128).getRGB();
   public static int enabledModuleBackground = new Color(71, 123, 255, 13).getRGB();
   public static int disabledModuleBackground = new Color(255, 255, 255, 5).getRGB();
   public static int mutedText = new Color(255, 255, 255, 89).getRGB();
   public static int disabledControlOutline = new Color(255, 255, 255, 26).getRGB();
   public static int enabledControlOutline = new Color(71, 123, 255, 51).getRGB();
   public static int disabledSettingLabel = new Color(255, 255, 255, 77).getRGB();
   public static int enabledSettingLabel = new Color(71, 123, 255, 128).getRGB();
   public static int disabledValueText = new Color(255, 255, 255, 153).getRGB();
   public static int disabledSettingIcon = new Color(255, 255, 255, 51).getRGB();
   public static int enabledSettingIcon = new Color(71, 123, 255, 51).getRGB();
   public static int disabledModuleCheckbox = new Color(255, 255, 255, 26).getRGB();
   public static int disabledModuleText = new Color(255, 255, 255, 51).getRGB();
   public static int enabledModuleCheckbox = new Color(71, 123, 255, 26).getRGB();
   public static int disabledSwitchTrack = new Color(255, 255, 255, 15).getRGB();
   public static int disabledSwitchOutline = new Color(255, 255, 255, 20).getRGB();
   public static int enabledSwitchTrack = new Color(71, 123, 255, 26).getRGB();
   public static int enabledSwitchOutline = new Color(71, 123, 255, 41).getRGB();
   public static int disabledSwitchThumb = new Color(255, 255, 255, 38).getRGB();
   public static int hoveredChoiceFill = new Color(255, 255, 255, 38).getRGB();
   public static int hoveredChoiceOutline = new Color(255, 255, 255, 20).getRGB();
   public static int choiceFill = new Color(255, 255, 255, 15).getRGB();
   public static int choiceOutline = new Color(255, 255, 255, 5).getRGB();
   public static int activeChoiceText = new Color(255, 255, 255, 153).getRGB();
   public static int choiceText = new Color(255, 255, 255, 51).getRGB();
   public static int disabledSliderTrack = new Color(255, 255, 255, 26).getRGB();
   public static int disabledSliderFill = new Color(255, 255, 255, 153).getRGB();
   public static int enabledSliderTrack = new Color(71, 123, 255, 26).getRGB();
   public static int popupBackground = new Color(255, 255, 255, 5).getRGB();
   public static int popupOutline = new Color(255, 255, 255, 5).getRGB();
   public static int actionButtonOutline = new Color(0, 0, 0, 33).getRGB();
   public static int searchIconBackground = new Color(0, 0, 0, 26).getRGB();
   public static int tooltipText = new Color(188, 188, 188, 255).getRGB();
   public static int basePanelTint = new Color(15, 14, 17, 96).getRGB();
   public static int opaquePanelTint = new Color(15, 14, 17, 245).getRGB();
   public static int flatPanelOverlay = new Color(15, 14, 17, 80).getRGB();
   public static int panelHighlight = new Color(255, 255, 255, 3).getRGB();
   public static int strongPanelTint = new Color(15, 14, 17, 110).getRGB();
   public static int blurTint = new Color(16, 15, 19, 204).getRGB();
   public static final String SEARCH_ICON = "clickgui/search";
   public static final String CHEVRON_ICON = "clickgui/chev";
   public static final String RIGHT_CHEVRON_ICON = "clickgui/chevright";
   public static final String CHECK_ICON = "clickgui/check";
   public static final String COLOR_PICKER_ICON = "clickgui/picker";
   public static final String BIND_KEY_ICON = "clickgui/bindkey";

   private ClickGuiTheme() {
   }

   public static String categoryIcon(ModuleCategory category) {
      return switch (category) {
         case COMBAT -> ("clickgui/cat_combat");
         case MOVEMENT -> ("clickgui/cat_movement");
         case RENDER -> ("clickgui/cat_visuals");
         case MISC -> ("clickgui/cat_misc");
         case AUTOBUY -> ("clickgui/cat_misc");
         case COSMETICS -> ("clickgui/cat_player");
         case THEMES -> ("clickgui/cat_visuals");
      };
   }

   public static void updateAccent(int color) {
      int accentColor = (-16777216) | color & (16777215);
      accent = accentColor;
      panelBackground = withAlpha(GuiVisuals.lerpColor((-16777216), accentColor, (0.126F)), 194);
      panelOutline = withAlpha(accentColor, 15);
      enabledModuleBackground = withAlpha(accentColor, 13);
      enabledControlOutline = withAlpha(accentColor, 51);
      enabledSettingLabel = withAlpha(accentColor, 128);
      enabledSettingIcon = withAlpha(accentColor, 51);
      enabledModuleCheckbox = withAlpha(accentColor, 26);
      enabledSwitchTrack = withAlpha(accentColor, 26);
      enabledSwitchOutline = withAlpha(accentColor, 41);
      enabledSliderTrack = withAlpha(accentColor, 26);
      GuiVisuals.accentColor = accentColor;
      GuiVisuals.accentGradientColor = GuiVisuals.lerpColor(accentColor, -1, (0.14F));
      GuiVisuals.accentLightColor = GuiVisuals.lerpColor(accentColor, -1, (0.28F));
      GuiVisuals.accentOutlineColor = GuiVisuals.lerpColor(accentColor, -1, (0.17F));
      GuiVisuals.accentAlpha20 = withAlpha(accentColor, 20);
   }

   private static int withAlpha(int color, int alpha) {
      return alpha << 24 | color & (16777215);
   }
}
