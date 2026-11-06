package sg.modules.impl.init;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import sg.ec.TotemTrigger;

public class CrystalTotemCondition extends TotemTrigger {
   private final TickTimer Ъ = new TickTimer();
   private boolean А_ = false;
   private static final String сIи = "Кристаллы";
   private static final String сI9 = "Золотые сердца";

   private float ю5() {
      float var1 = MINECRAFT_CLIENT.player.getHealth();
      if (this.В.Ы().isSelected(сI9) && MINECRAFT_CLIENT.player.hasEffect(MobEffects.ABSORPTION)) {
         var1 += MINECRAFT_CLIENT.player.getAbsorptionAmount();
      }

      return var1;
   }

   @Override
   public boolean ац() {
      if (!this.В.Ы().isSelected(сIи)) {
         return false;
      }

      if (!this.ФЖ()) {
         for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var2 instanceof EndCrystal var3 && var3.distanceTo(MINECRAFT_CLIENT.player) <= this.В.getCrystalDistanceSetting().getValue()) {
               this.Ъ.reset();
               this.А_ = true;
               return true;
            }
         }
      }

      return this.А_ && !this.Ъ.hasElapsed(this.В.У_989fed9a().getValue().intValue());
   }

   private boolean ФЖ() {
      return this.В.ш_3276742a().getValue()
         && MINECRAFT_CLIENT.player.getOffhandItem().getItem() == Items.PLAYER_HEAD
         && (this.ю5() > this.В.西_989fed9a().getValue() || !this.В.м_3276742a().getValue());
   }
}
