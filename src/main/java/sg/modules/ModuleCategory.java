package sg.modules;

import lombok.Generated;
import sg.animation.FloatAnimation;

public enum ModuleCategory {
   MOVEMENT("Movement", "c"),
   RENDER("Render", "d"),
   MISC("Misc", "f"),
   AUTOBUY("AutoBuy", "F"),
   COSMETICS("Cosmetics", "o"),
   THEMES("Themes", "e");

   private final String ХГ;
   private final String Хл;
   private final FloatAnimation с0 = new FloatAnimation(0.0F, 10.0F);

   @Generated
   public String _А() {
      return this.ХГ;
   }

   @Generated
   public String _Н() {
      return this.Хл;
   }

   @Generated
   public FloatAnimation ц() {
      return this.с0;
   }

   @Generated
   ModuleCategory(String var3, String var4) {
      this.ХГ = var3;
      this.Хл = var4;
   }
}
