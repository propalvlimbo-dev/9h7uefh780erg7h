package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.rotation.AimGeometryMath;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;

public class Backtrack extends ClientModule {
   @SerializedName("г")
   private final FloatSetting targetDistanceSetting;
   @SerializedName("0")
   private final FloatSetting packetDelaySetting;
   private final @com.google.gson.annotations.SerializedName("Ж")  BooleanSetting showTargetBoxSetting;
   private final @com.google.gson.annotations.SerializedName("ю")  List<Backtrack.DelayedPacket> delayedPackets;
   private @com.google.gson.annotations.SerializedName("В")  Entity targetEntity;
   private @com.google.gson.annotations.SerializedName("衣")  Vec3 serverTargetPosition;
   private @com.google.gson.annotations.SerializedName("ш")  Vec3 renderedTargetPosition;
   private final @com.google.gson.annotations.SerializedName("са")  EventListener<AttackEntityEvent> attackListener;
   private final @com.google.gson.annotations.SerializedName("сЮ")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private final @com.google.gson.annotations.SerializedName("сЧ")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("сЛ")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String MODULE_NAME = "Backtrack";
   private static final String MODULE_DESCRIPTION = "Задерживает пакеты цели для удара по прошлой позиции";
   private static final String DISTANCE_LABEL = "Дистанция";
   private static final float DEFAULT_TARGET_DISTANCE = 3.0F;
   private static final float MIN_TARGET_DISTANCE = 3.0F;
   private static final float MAX_TARGET_DISTANCE = 6.0F;
   private static final float TARGET_DISTANCE_STEP = 0.1F;
   private static final String DELAY_LABEL = "Задержка";
   private static final float DEFAULT_DELAY_MILLIS = 500.0F;
   private static final float MIN_DELAY_MILLIS = 100.0F;
   private static final float MAX_DELAY_MILLIS = 5000.0F;
   private static final float DELAY_STEP_MILLIS = 50.0F;
   private static final String SHOW_BOXES_LABEL = "Отображать боксы";
   private static final float BOX_POSITION_SMOOTHING = 15.0F;
   private static final int HURT_COLOR_ARGB = -65536;
   private static final float MAX_HURT_TIME_TICKS = 10.0F;
   private static final float BOX_FILL_ALPHA = 0.11764706F;
   private static final float BOX_OUTLINE_ALPHA = 0.7058824F;
   private static final double RELATIVE_MOVE_X_SCALE = 4096.0;
   private static final double RELATIVE_MOVE_Y_SCALE = 4096.0;
   private static final double RELATIVE_MOVE_Z_SCALE = 4096.0;

   @Override
   public void onEnable() {
      this.С8();
   }

   private boolean method_7_bbeebd87(Packet<?> var1) {
      return var1 instanceof ClientboundSoundPacket
         || var1 instanceof ClientboundEntityEventPacket
         || var1 instanceof ClientboundSetTimePacket
         || var1 instanceof ClientboundLevelParticlesPacket
         || var1 instanceof ClientboundAnimatePacket
         || var1 instanceof ClientboundSystemChatPacket
         || var1 instanceof ClientboundAddEntityPacket
         || var1 instanceof ClientboundRemoveEntitiesPacket
         || var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundRespawnPacket
         || var1 instanceof ClientboundLoginPacket
         || var1 instanceof ClientboundDisconnectPacket;
   }

   private void method_7(PoseStack var1, VertexConsumer var2, Vec3 var3, AABB var4, int var5) {
      float var6 = (float)(var4.minX - var3.x);
      float var7 = (float)(var4.minY - var3.y);
      float var8 = (float)(var4.minZ - var3.z);
      float var9 = (float)(var4.maxX - var3.x);
      float var10 = (float)(var4.maxY - var3.y);
      float var11 = (float)(var4.maxZ - var3.z);
      Pose var12 = var1.last();
      this.method_7(var12, var2, var6, var7, var8, var9, var7, var8, var9, var7, var11, var6, var7, var11, var5);
      this.method_7(var12, var2, var6, var10, var8, var6, var10, var11, var9, var10, var11, var9, var10, var8, var5);
      this.method_7(var12, var2, var6, var7, var8, var6, var10, var8, var9, var10, var8, var9, var7, var8, var5);
      this.method_7(var12, var2, var9, var7, var8, var9, var10, var8, var9, var10, var11, var9, var7, var11, var5);
      this.method_7(var12, var2, var6, var7, var11, var9, var7, var11, var9, var10, var11, var6, var10, var11, var5);
      this.method_7(var12, var2, var6, var7, var8, var6, var7, var11, var6, var10, var11, var6, var10, var8, var5);
   }

   private boolean method_7O() {
      return this.targetEntity == null && this.serverTargetPosition == null;
   }

   private void method_0(AttackEntityEvent var1) {
      if (!MINECRAFT_CLIENT.isLocalServer() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPhase() == AttackEntityEvent.Phase.PRE && var1.getAttacker() == MINECRAFT_CLIENT.player) {
            Entity var2 = var1.getTarget();
            if (var2 != null && var2 != this.targetEntity && !var2.isInvulnerable()) {
               this.targetEntity = var2;
               this.renderedTargetPosition = this.serverTargetPosition = var2.position();
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.С8();
   }

   private void method_7_de5a6f78(Packet<?> var1) {
      if (var1 instanceof ClientboundEntityPositionSyncPacket var2 && var2.id() == this.targetEntity.getId()) {
         this.serverTargetPosition = var2.values().position();
      } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.targetEntity.getId()) {
         this.serverTargetPosition = var4.change().position();
      } else {
         if (var1 instanceof ClientboundMoveEntityPacket var5 && this.serverTargetPosition != null) {
            Entity var3 = var5.getEntity(MINECRAFT_CLIENT.level);
            if (var3 == this.targetEntity) {
               this.serverTargetPosition = this.serverTargetPosition.add(var5.getXa() / RELATIVE_MOVE_X_SCALE, var5.getYa() / RELATIVE_MOVE_Y_SCALE, var5.getZa() / RELATIVE_MOVE_Z_SCALE);
            }
         }
      }
   }

   private void г(boolean var1) {
      synchronized (this.delayedPackets) {
         Iterator var3 = this.delayedPackets.iterator();

         while (var3.hasNext()) {
            Backtrack.DelayedPacket var4 = (Backtrack.DelayedPacket)var3.next();
            if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.getConnection() == null) {
               return;
            }

            if (this.serverTargetPosition == null) {
               this.С(var4.packet);
               var3.remove();
            } else {
               double var5 = MINECRAFT_CLIENT.player.position().distanceTo(this.serverTargetPosition) / this.targetDistanceSetting.getValue().floatValue();
               if (var1 || !(var4.timestamp + this.packetDelaySetting.getValue().floatValue() * var5 > System.currentTimeMillis())) {
                  this.С(var4.packet);
                  var3.remove();
                  continue;
               }
               break;
            }
         }
      }
   }

   private void С(Packet<?> var1) {
      try {
         ((Packet<net.minecraft.client.multiplayer.ClientPacketListener>)var1).handle(MINECRAFT_CLIENT.getConnection());
      } catch (Exception var3) {
      }
   }

   private void С8() {
      this.г(true);
      this.targetEntity = null;
      this.serverTargetPosition = null;
      this.renderedTargetPosition = null;
   }

   private void method_7(
      Pose var1,
      VertexConsumer var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      int var15
   ) {
      var2.addVertex(var1, var3, var4, var5).setColor(var15);
      var2.addVertex(var1, var6, var7, var8).setColor(var15);
      var2.addVertex(var1, var9, var10, var11).setColor(var15);
      var2.addVertex(var1, var12, var13, var14).setColor(var15);
   }

   public Backtrack() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.targetDistanceSetting = new FloatSetting(DISTANCE_LABEL, DEFAULT_TARGET_DISTANCE, MIN_TARGET_DISTANCE, MAX_TARGET_DISTANCE).withStep(TARGET_DISTANCE_STEP);
      this.packetDelaySetting = new FloatSetting(DELAY_LABEL, DEFAULT_DELAY_MILLIS, MIN_DELAY_MILLIS, MAX_DELAY_MILLIS).withStep(DELAY_STEP_MILLIS);
      this.showTargetBoxSetting = new BooleanSetting(SHOW_BOXES_LABEL, true);
      this.delayedPackets = new LinkedList<>();
      this.attackListener = this::method_0;
      this.packetReceiveListener = this::Х;
      this.playerTickListener = this::ъ;
      this.worldEffectsListener = this::и;
      this.addSettings(this.targetDistanceSetting, this.packetDelaySetting, this.showTargetBoxSetting);
   }

   private void Х(PacketReceiveEvent var1) {
      if (!MINECRAFT_CLIENT.isLocalServer() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Packet var2 = var1.getPacket();
         if (this.method_7л()) {
            if (!this.method_7_bbeebd87(var2)) {
               if (var2 instanceof ClientboundSetEntityMotionPacket var3 && var3.id() == MINECRAFT_CLIENT.player.getId()) {
                  this.С8();
               } else if (this.targetEntity != null) {
                  this.method_7_de5a6f78(var2);
                  var1.setCancelled(true);
                  synchronized (this.delayedPackets) {
                     this.delayedPackets.add(new Backtrack.DelayedPacket(var2, System.currentTimeMillis()));
                  }
               }
            }
         }
      }
   }

   private void ъ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if ((!this.delayedPackets.isEmpty() || !this.method_7O()) && !MINECRAFT_CLIENT.isLocalServer()) {
            if (this.method_7л()) {
               this.г(false);
            } else {
               this.С8();
            }
         }
      }
   }

   private boolean method_7л() {
      return !this.method_7O() && MINECRAFT_CLIENT.player != null && this.targetEntity != null && this.serverTargetPosition != null
         ? this.targetEntity.isAlive() && !this.targetEntity.isInvulnerable() && MINECRAFT_CLIENT.player.position().distanceTo(this.serverTargetPosition) <= this.targetDistanceSetting.getValue().floatValue()
         : false;
   }

   private void и(WorldEffectsEvent var1) {
      if (this.showTargetBoxSetting.getValue() && this.serverTargetPosition != null && this.targetEntity != null && !MINECRAFT_CLIENT.isLocalServer()) {
         if (this.targetEntity instanceof LivingEntity var2) {
            double var10 = this.targetEntity.getBbWidth() / 2.0F;
            if (this.renderedTargetPosition == null || this.serverTargetPosition.distanceTo(this.renderedTargetPosition) >= 1.0) {
               this.renderedTargetPosition = this.serverTargetPosition;
            }

            this.renderedTargetPosition = AimGeometryMath.lerpVectorComponents(this.renderedTargetPosition, this.serverTargetPosition, BOX_POSITION_SMOOTHING);
            int var5 = ColorMath.blendArgb(HURT_COLOR_ARGB, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), 1.0F - var2.hurtTime / MAX_HURT_TIME_TICKS);
            AABB var6 = new AABB(this.renderedTargetPosition.x - var10, this.renderedTargetPosition.y, this.renderedTargetPosition.z - var10, this.renderedTargetPosition.x + var10, this.renderedTargetPosition.y + this.targetEntity.getBbHeight(), this.renderedTargetPosition.z + var10);
            Vec3 var7 = var1.getCameraPosition();
            VertexConsumer var8 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.QUADS);
            this.method_7(var1.getPoses(), var8, var7, var6, ColorMath.withAlpha(var5, BOX_FILL_ALPHA));
            VertexConsumer var9 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
            WorldBoxGeometry.appendBoxOutline(
               var1.getPoses(), var9, var7, var6.minX, var6.minY, var6.minZ, var6.maxX, var6.maxY, var6.maxZ, ColorMath.withAlpha(var5, BOX_OUTLINE_ALPHA)
            );
         }
      }
   }

   private record DelayedPacket(Packet<?> packet, long timestamp) {
   }
}
