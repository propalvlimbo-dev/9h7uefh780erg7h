package sg.gui.third;

import sg.modules.ModuleCategory;

/**
 * Module entries followed by a separate group of client editors in the Third dock.
 */
public enum ThirdGuiCategory {
   COMBAT(ModuleCategory.COMBAT, "Combat", ThirdGuiIcons.sword()),
   MOVEMENT(ModuleCategory.MOVEMENT, "Movement", ThirdGuiIcons.sneaker()),
   VISUALS(ModuleCategory.RENDER, "Visuals", ThirdGuiIcons.sparkle()),
   MISC(ModuleCategory.MISC, "Misc", ThirdGuiIcons.dots()),
   THEMES(ModuleCategory.THEMES, "Themes", ThirdGuiIcons.globe()),
   COSMETICS(ModuleCategory.COSMETICS, "Cosmetics", ThirdGuiIcons.shirt()),
   AUTOBUY(ModuleCategory.AUTOBUY, "AutoBuy", ThirdGuiIcons.folder());

   private final ModuleCategory category;
   private final String title;
   private final String icon;

   ThirdGuiCategory(ModuleCategory category, String title, String icon) {
      this.category = category;
      this.title = title;
      this.icon = icon;
   }

   public ModuleCategory category() {
      return this.category;
   }

   public boolean isEditor() {
      return this == THEMES || this == COSMETICS || this == AUTOBUY;
   }

   public String title() {
      return this.title;
   }

   public String icon() {
      return this.icon;
   }
}
