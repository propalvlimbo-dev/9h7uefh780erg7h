package sg.ec;

import net.minecraft.world.effect.MobEffects;

public class HealthTotemTrigger extends TotemTrigger {
   private static final String ВсР = "Золотые сердца";

   @Override
   public boolean ац() {
      float var1 = MINECRAFT_CLIENT.player.isFallFlying() ? this.В.х_989fed9a().getValue() : this.В.м_989fed9a().getValue();
      return this.西К() <= var1;
   }

   private float 西К() {
      float var1 = MINECRAFT_CLIENT.player.getHealth();
      if (this.В.Ы().isSelected(ВсР) && MINECRAFT_CLIENT.player.hasEffect(MobEffects.ABSORPTION)) {
         var1 += MINECRAFT_CLIENT.player.getAbsorptionAmount();
      }

      return var1;
   }
}
