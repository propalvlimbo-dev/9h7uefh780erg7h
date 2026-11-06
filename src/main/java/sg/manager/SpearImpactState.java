package sg.manager;

import sg.annotation.LuaExport;

public class SpearImpactState {
   @LuaExport("canDamage")
   public @com.google.gson.annotations.SerializedName("Яв")  boolean canDamage;
   @LuaExport("canKnockback")
   public @com.google.gson.annotations.SerializedName("Яъ")  boolean canKnockback;
   @LuaExport("canDismount")
   public @com.google.gson.annotations.SerializedName("ЯМ")  boolean canDismount;
   @LuaExport("hitImpact")
   public @com.google.gson.annotations.SerializedName("ЯН")  boolean hitImpact;

   public SpearImpactState(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.canDamage = var1;
      this.canKnockback = var2;
      this.canDismount = var3;
      this.hitImpact = var4;
   }
}
