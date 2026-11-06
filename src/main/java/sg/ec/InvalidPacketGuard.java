package sg.ec;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import sg.events.EventListener;
import sg.events.impl.PacketReceiveEvent;

public final class InvalidPacketGuard extends ClientService {
   private static final double с0 = 1.0E9;
   private static final double сх = 1000000.0;
   private static final int ХЭ = 100000;
   private static final int Х必 = 1000000;
   private final EventListener<PacketReceiveEvent> сг = this::С;
   private static final double ВЦЙ = 1.0E9;
   private static final double ВЦк = 1.0E9;
   private static final int ВЦг = 1000000;
   private static final double ВЦ0 = 1.0E9;
   private static final double ВЦх = 1.0E9;
   private static final double ВЦУ = 1.0E9;
   private static final double ВЦЗ = 1.0E9;
   private static final double ВЦЕ = 1.0E9;
   private static final double ВЦП = 1.0E9;
   private static final double ВЦ西 = 1.0E9;
   private static final int ВЦд = 100000;
   private static final double ВЦБ = 1.0E9;
   private static final double ВЦС = 1000000.0;
   private static final double ВЦз = 1000000.0;
   private static final double ВЦц = 1.0E9;

   private void С(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Packet var2 = var1.getPacket();
         if (var2 instanceof ClientboundExplodePacket var8) {
            if (this.Р(var8.center(), ВЦЙ)
               || this.х(var8.radius(), ВЦк)
               || var8.blockCount() < 0
               || var8.blockCount() > ВЦг
               || var8.playerKnockback().filter(var1x -> this.Р(var1x, ВЦц)).isPresent()) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundLevelParticlesPacket var7) {
            if (this.х(var7.getX(), ВЦ0)
               || this.х(var7.getY(), ВЦх)
               || this.х(var7.getZ(), ВЦУ)
               || this.х(var7.getXDist(), ВЦЗ)
               || this.х(var7.getYDist(), ВЦЕ)
               || this.х(var7.getZDist(), ВЦП)
               || this.х(var7.getMaxSpeed(), ВЦ西)
               || var7.getCount() < 0
               || var7.getCount() > ВЦд) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundPlayerPositionPacket var6) {
            if (this.Р(var6.change(), ВЦБ)) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundGameEventPacket var5) {
            if (var5.getEvent() == ClientboundGameEventPacket.DEMO_EVENT) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundTeleportEntityPacket var4) {
            if (this.Р(var4.change(), ВЦС)) {
               var1.setCancelled(true);
            }
         } else {
            if (var2 instanceof ClientboundEntityPositionSyncPacket var3 && this.Р(var3.values(), ВЦз)) {
               var1.setCancelled(true);
            }
         }
      }
   }

   private boolean х(double var1, double var3) {
      return !Double.isFinite(var1) || Math.abs(var1) > var3;
   }

   private boolean Р(PositionMoveRotation var1, double var2) {
      return var1 == null || this.Р(var1.position(), var2) || this.Р(var1.deltaMovement(), var2) || this.х(var1.yRot(), var2) || this.х(var1.xRot(), var2);
   }

   private boolean Р(Vec3 var1, double var2) {
      return var1 == null || this.х(var1.x, var2) || this.х(var1.y, var2) || this.х(var1.z, var2);
   }
}
