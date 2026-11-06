package sg.modules.impl.movement;

import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class ElytraFly extends ClientModule {
   private boolean еэ;
   private final EventListener<PlayerTickEvent> Хп = this::МЛ;
   private static final String сюЯ = "ElytraFly";
   private static final String сюХ = "Управляет полётом на элитрах";
   private static final String сюе = "ElytraFly работает только с протоколом 1.17.1 через ViaFabric";
   private static final String сюА = "ElytraFly работает только при включении на земле";
   private static final float сю4 = -30.0F;
   private static final float сюп = 30.0F;
   private static final double сюД = 0.08;
   private static final double сюЪ = 0.29;
   private static final double сют = 0.301;

   private void МЛ(PlayerTickEvent var1) {
      if (this.еэ) {
         this.еэ = false;
         this.disable();
      } else if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            MINECRAFT_CLIENT.player.setXRot(Mth.clamp(MINECRAFT_CLIENT.player.getXRot(), сю4, сюп));
            if (MINECRAFT_CLIENT.player.onGround()) {
               MINECRAFT_CLIENT.player.jumpFromGround();
            } else {
               if (!MINECRAFT_CLIENT.player.isFallFlying()) {
                  MINECRAFT_CLIENT.player.startFallFlying();
                  MINECRAFT_CLIENT.player.connection.send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
               }

               if (MINECRAFT_CLIENT.player.isFallFlying()) {
                  double var2 = сюД;
                  double var4 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
                  double var6 = -Math.sin(var4) * var2;
                  double var8 = Math.cos(var4) * var2;
                  double var10 = MINECRAFT_CLIENT.player.tickCount % 2 == 0 ? сюЪ : сют;
                  MINECRAFT_CLIENT.player.setDeltaMovement(var6, var10, var8);
               }
            }
         }
      }
   }

   public ElytraFly() {
      super(сюЯ, сюХ, ModuleCategory.MOVEMENT);
   }

   @Override
   public void onEnable() {
      this.еэ = false;
      if (MINECRAFT_CLIENT.player != null) {
         ClientChat.З(сюе);
         if (!MINECRAFT_CLIENT.player.onGround()) {
            ClientChat.З(сюА);
            this.еэ = true;
         }
      }
   }
}
