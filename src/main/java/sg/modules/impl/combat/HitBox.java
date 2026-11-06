package sg.modules.impl.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public final class HitBox extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("сР")  FloatSetting widthExpansionSetting;
   private final @com.google.gson.annotations.SerializedName("с3")  FloatSetting heightExpansionSetting;
   private final @com.google.gson.annotations.SerializedName("сВ")  BooleanSetting visibleOnlySetting;
   private static final String ХНФ = "HitBox";
   private static final String ХНЙ = "Расширяет хитбоксы игроков по X и Y";
   private static final String ХНк = "Размер X";
   private static final float ХНг = 0.25F;
   private static final float ХН0 = 0.05F;
   private static final String ХНх = "Размер Y";
   private static final float ХНУ = 0.05F;
   private static final String ХНЗ = "Видимые";

   public AABB й(Entity var1, AABB var2) {
      return this.visibleOnlySetting.getValue() ? var2 : this.к(var1, var2);
   }

   public AABB Ц(Entity var1, AABB var2) {
      return !this.visibleOnlySetting.getValue() ? var2 : this.к(var1, var2);
   }

   private boolean И(Entity var1) {
      return var1 instanceof Player && var1 != MINECRAFT_CLIENT.player && var1.isAlive() && !var1.isSpectator();
   }

   private AABB к(Entity var1, AABB var2) {
      if (this.isEnabled() && var2 != null && this.И(var1)) {
         double var3 = Math.max(0.0, this.widthExpansionSetting.getValue().floatValue());
         double var5 = Math.max(0.0, this.heightExpansionSetting.getValue().floatValue());
         return var3 == 0.0 && var5 == 0.0
            ? var2
            : new AABB(var2.minX - var3, var2.minY - var5, var2.minZ - var3, var2.maxX + var3, var2.maxY + var5, var2.maxZ + var3);
      } else {
         return var2;
      }
   }

   public HitBox() {
      super(ХНФ, ХНЙ, ModuleCategory.COMBAT);
      this.widthExpansionSetting = new FloatSetting(ХНк, ХНг, 0.0F, 2.0F).withStep(ХН0);
      this.heightExpansionSetting = new FloatSetting(ХНх, 0.0F, 0.0F, 2.0F).withStep(ХНУ);
      this.visibleOnlySetting = new BooleanSetting(ХНЗ, false);
      this.addSettings(this.widthExpansionSetting, this.heightExpansionSetting, this.visibleOnlySetting);
   }
}
