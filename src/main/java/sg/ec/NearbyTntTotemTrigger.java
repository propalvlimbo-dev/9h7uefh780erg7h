package sg.ec;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;

public class NearbyTntTotemTrigger extends TotemTrigger {
   private static final String В0Й = "Динамит";

   @Override
   public boolean ац() {
      if (!this.В.Ы().isSelected(В0Й)) {
         return false;
      }

      for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var2 instanceof PrimedTnt var3 && var3.distanceTo(MINECRAFT_CLIENT.player) <= this.В.ч_989fed9a().getValue()) {
            return true;
         }

         if (var2 instanceof MinecartTNT var4 && var4.distanceTo(MINECRAFT_CLIENT.player) <= this.В.ч_989fed9a().getValue()) {
            return true;
         }
      }

      return false;
   }
}
