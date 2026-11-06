package sg.modules.impl.combat;

import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.HitResult;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;

public class CrystalOptimizer extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Хм")  EventListener<AttackEntityEvent> attackListener = this::е;
   private static final String MODULE_NAME = "CrystalOptimizer";
   private static final String MODULE_DESCRIPTION = "Мгновенно убирает кристалл на клиенте после удара";

   private void е(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST) {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
            if (var1.getAttacker() == MINECRAFT_CLIENT.player) {
               if (var1.getTarget() instanceof EndCrystal var3 && !var3.isRemoved()) {
                  if (!(MINECRAFT_CLIENT.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0)) {
                     var3.remove(RemovalReason.KILLED);
                     this.ь(var3);
                  }
               }
            }
         }
      }
   }

   public CrystalOptimizer() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
   }

   private void ь(EndCrystal var1) {
      if (MINECRAFT_CLIENT.crosshairPickEntity == var1) {
         HitResult var2 = MINECRAFT_CLIENT.player.pick(MINECRAFT_CLIENT.player.blockInteractionRange(), 1.0F, false);
         MINECRAFT_CLIENT.crosshairPickEntity = null;
         MINECRAFT_CLIENT.hitResult = var2;
      }
   }
}
