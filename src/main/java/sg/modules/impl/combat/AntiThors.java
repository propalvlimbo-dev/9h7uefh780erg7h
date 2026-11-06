package sg.modules.impl.combat;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageTypes;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class AntiThors extends ClientModule {
   private @com.google.gson.annotations.SerializedName("Х弟")  long lastThornsDamageMillis = 0L;
   private @com.google.gson.annotations.SerializedName("ЯУ")  EventListener<PacketReceiveEvent> packetReceiveListener = this::д;
   private static final String MODULE_NAME = "AntiThors";
   private static final String MODULE_DESCRIPTION = "Убирает отбрасывание от шипов при полёте на элитрах";
   private static final long KNOCKBACK_CANCEL_WINDOW_MILLIS = 500L;
   private static final long NO_RECENT_THORNS_DAMAGE = -1L;

   private void д(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Packet var2 = var1.getPacket();
         long var3 = Util.getMillis();
         if (var2 instanceof ClientboundDamageEventPacket var5 && var5.entityId() == MINECRAFT_CLIENT.player.getId() && var5.sourceType().is(DamageTypes.THORNS)) {
            this.lastThornsDamageMillis = var3;
         } else {
            if (var2 instanceof ClientboundSetEntityMotionPacket var6
               && var6.id() == MINECRAFT_CLIENT.player.getId()
               && MINECRAFT_CLIENT.player.isFallFlying()
               && this.lastThornsDamageMillis >= 0L
               && var3 - this.lastThornsDamageMillis <= KNOCKBACK_CANCEL_WINDOW_MILLIS) {
               var1.setCancelled(true);
               this.lastThornsDamageMillis = NO_RECENT_THORNS_DAMAGE;
            }
         }
      }
   }

   public AntiThors() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
   }
}
