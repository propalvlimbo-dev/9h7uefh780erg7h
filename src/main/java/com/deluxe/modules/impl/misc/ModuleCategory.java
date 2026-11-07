package com.deluxe.modules.impl.misc;

public enum ModuleCategory {
   MOVEMENT("Movement"),
   RENDER("Visuals"),
   MISC("Misc"),
   AUTOBUY("AutoBuy"),
   COSMETICS("Cosmetic"),
   THEMES("Themes");

   private final String displayName;

   ModuleCategory(String displayName) {
      this.displayName = displayName;
   }

   public String displayName() {
      return this.displayName;
   }

   public String iconGlyph() {
      return "";
   }

   public boolean isPanelCategory() {
      return this == AUTOBUY || this == COSMETICS || this == THEMES;
   }

   public sg.modules.ModuleCategory clientCategory() {
      return sg.modules.ModuleCategory.valueOf(this.name());
   }

   public static ModuleCategory fromClientCategory(sg.modules.ModuleCategory category) {
      return switch (category) {
         case MOVEMENT -> MOVEMENT;
         case RENDER -> RENDER;
         case AUTOBUY -> AUTOBUY;
         case COSMETICS -> COSMETICS;
         case THEMES -> THEMES;
         case MISC -> MISC;
      };
   }
}
