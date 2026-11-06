package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Vector2f;
import org.joml.Vector4i;
import sg.ec.MinecraftAccess;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.ec.PlayerMotionPredictor;
import sg.mixin.FireworkRocketEntityAccessor;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.client.ClientServices;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.EntityMotionSimulator;
import sg.modules.impl.init.EntityMotionTracker;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.init.ProjectileInterceptSolver;
import sg.rotation.RotationStrategy;

public final class Aimbot extends ClientModule {
   @SerializedName("Я")
   private final EntityMotionTracker motionTracker = new EntityMotionTracker();
   @SerializedName("В")
   private final ProjectileInterceptSolver interceptSolver = new ProjectileInterceptSolver();
   @SerializedName("В")
   private final Aimbot.InterceptRotation interceptRotation = new Aimbot.InterceptRotation();
   @SerializedName("В")
   private ProjectileInterceptSolver.Solution currentSolution;
   @SerializedName("В")
   private Aimbot.PendingBowRelease pendingBowRelease;
   private @com.google.gson.annotations.SerializedName("Н")  ClientLevel enabledLevel;
   private @com.google.gson.annotations.SerializedName("т")  Player enabledPlayer;
   private @com.google.gson.annotations.SerializedName("Ъ7")  int lastObservedPlayerTick = -1;
   private @com.google.gson.annotations.SerializedName("ЪР")  int lastReleaseTick = -1;
   private @com.google.gson.annotations.SerializedName("АO")  boolean releaseOnPostSync;
   private @com.google.gson.annotations.SerializedName("а")  Player selectedTarget;
   private final @com.google.gson.annotations.SerializedName("ес")  EventListener<PlayerTickEvent> playerTickListener = this::OЪ;
   private final @com.google.gson.annotations.SerializedName("еВ")  EventListener<PacketSendEvent> packetSendListener = this::Ы;
   private final @com.google.gson.annotations.SerializedName("еЯ")  EventListener<PlayerMovementSyncEvent> movementSyncListener = new EventListener<PlayerMovementSyncEvent>() {
      @Override
      public int getPriority() {
         return -120;
      }

      public void onEvent(PlayerMovementSyncEvent var1) {
         Aimbot.this.method_5(var1);
      }
   };
   private final @com.google.gson.annotations.SerializedName("еХ")  EventListener<PlayerMovementSyncEvent.Post> postMovementSyncListener = new EventListener<PlayerMovementSyncEvent.Post>() {
      public void onEvent(PlayerMovementSyncEvent.Post var1) {
         if (Aimbot.this.releaseOnPostSync) {
            Aimbot.this.Ыщ();
         }
      }

      @Override
      public int getPriority() {
         return 100;
      }
   };
   private final @com.google.gson.annotations.SerializedName("ее")  EventListener<HudRenderEvent> hudRenderListener = this::Oч;
   private static final String Въ7 = "Aimbot";
   private static final String ВъР = "Автоматически наводится на энтити при спуске стрелы";
   private static final double Въ3 = -0.1;
   private static final double Вър = -0.1;
   private static final double Въ6 = 0.1;
   private static final double Въ_ = 0.3;
   private static final double ВъШ = 256.0;
   private static final double Въы = 0.91F;
   private static final double Въй = 0.98F;
   private static final double ВъЖ = 50.0;
   private static final double Въм = 6.0;
   private static final double ВъК = 4.0;
   private static final double ВъI = 1024.0;
   private static final double ВъТ = 1024.0;
   private static final float ВъО = 0.1F;
   private static final float ВъФ = 3.0F;
   private static final float ВъЙ = 16.0F;
   private static final float Вък = 16.0F;
   private static final float Въг = 32.0F;
   private static final float Въ0 = 32.0F;
   private static final String Въх = "rose";
   private static final String ВъУ = "textures/world/cube.png";

   private Player ф(Player var1) {
      if (this.э(var1)) {
         return var1;
      }

      Player var2 = null;
      double var3 = ВъI;

      for (Player var6 : MINECRAFT_CLIENT.level.players()) {
         if (this.э(var6)) {
            double var7 = MINECRAFT_CLIENT.player.distanceToSqr(var6);
            if (var7 < var3 || var7 == var3 && (var2 == null || var6.getId() < var2.getId())) {
               var2 = var6;
               var3 = var7;
            }
         }
      }

      if (var2 != var1) {
         this.motionTracker.l1();
      }

      return var2;
   }

   private void Oч(HudRenderEvent var1) {
      if (!this.щ0() && this.currentSolution != null && this.selectedTarget != null && (this.щЖ() || this.pendingBowRelease != null)) {
         Vector2f var2 = WorldProjection.projectToScreen(this.currentSolution.targetPosition());
         if (var2 != null) {
            UiDrawing.drawTexture(
               var2.x - ВъЙ,
               var2.y - Вък,
               Въг,
               Въ0,
               0.0F,
               new Vector4i(
                  ThemeEditor.getColorArgb(ThemeColorSlot.MAIN),
                  ThemeEditor.getColorArgb(ThemeColorSlot.MAIN),
                  ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO),
                  ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO)
               ),
               Identifier.fromNamespaceAndPath(Въх, ВъУ)
            );
         }
      }
   }

   private double Ш_3f39d5c3() {
      if (MINECRAFT_CLIENT.getConnection() == null) {
         return 0.0;
      }

      PlayerInfo var1 = MINECRAFT_CLIENT.getConnection().getPlayerInfo(MINECRAFT_CLIENT.player.getUUID());
      return var1 == null ? 0.0 : Math.clamp(var1.getLatency() / ВъЖ, 0.0, Въм);
   }

   private void Ы(PacketSendEvent var1) {
      if (!var1.isCancelled()) {
         if (var1.getPacket() instanceof ServerboundPlayerActionPacket || var1.getPacket() instanceof ServerboundSetCarriedItemPacket) {
            this.Ыч();
            if (this.pendingBowRelease != null && var1.getPacket() instanceof ServerboundSetCarriedItemPacket) {
               this.Ыщ();
            }

            if (var1.getPacket() instanceof ServerboundPlayerActionPacket var2
               && var2.getAction() == Action.RELEASE_USE_ITEM
               && this.щЖ()
               && !this.щ0()
               && this.pendingBowRelease == null) {
               this.selectedTarget = this.ф(this.selectedTarget);
               int var4 = MINECRAFT_CLIENT.player.getTicksUsingItem();
               if (this.ф(this.selectedTarget, Б(var4), MINECRAFT_CLIENT.player.getEyePosition().add(0.0, Вър, 0.0), MINECRAFT_CLIENT.player.onGround())) {
                  this.pendingBowRelease = new Aimbot.PendingBowRelease(
                     var2, MINECRAFT_CLIENT.level, MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getUsedItemHand(), var4, MINECRAFT_CLIENT.player.tickCount, MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot()
                  );
                  this.releaseOnPostSync = false;
                  var1.setCancelled(true);
                  this.interceptRotation.ь(this.currentSolution);
                  RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.interceptRotation, 2, 45);
               }
            }
         }
      }
   }

   private boolean щ0() {
      return RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled();
   }

   private void Ыщ() {
      Aimbot.PendingBowRelease var1 = this.pendingBowRelease;
      boolean var2 = this.щЪ();
      this.pendingBowRelease = null;
      this.releaseOnPostSync = false;

      try {
         if (var2) {
            this.lastReleaseTick = MINECRAFT_CLIENT.player.tickCount;
            PacketEventBypass.А(var1.packet());
         }
      } finally {
         this.Ы西();
      }
   }

   public boolean щж() {
      return this.isEnabled() && MINECRAFT_CLIENT.player != null && this.enabledLevel == MINECRAFT_CLIENT.level && this.enabledPlayer == MINECRAFT_CLIENT.player && (this.pendingBowRelease != null || this.lastReleaseTick == MINECRAFT_CLIENT.player.tickCount);
   }

   private void OЪ(PlayerTickEvent var1) {
      this.Ыч();
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.lastObservedPlayerTick = MINECRAFT_CLIENT.player.tickCount;
         if (this.pendingBowRelease == null) {
            if (!this.щ0() && this.щЖ()) {
               this.selectedTarget = this.ф(this.selectedTarget);
               this.ф(this.selectedTarget, Б(MINECRAFT_CLIENT.player.getTicksUsingItem()), MINECRAFT_CLIENT.player.getEyePosition().add(0.0, Въ3, 0.0), MINECRAFT_CLIENT.player.onGround());
            } else {
               this.ЫЫ();
            }
         } else {
            if (this.щ0() || MINECRAFT_CLIENT.player.tickCount - this.pendingBowRelease.tick() > 2) {
               this.Ыщ();
            }
         }
      }
   }

   @Generated
   public Player щ() {
      return this.selectedTarget;
   }

   private boolean ф(Player var1, double var2, Vec3 var4, boolean var5) {
      this.currentSolution = null;
      if (this.э(var1) && !(var2 < Въ_)) {
         Vec3 var6 = var1.getPositionCodec().getBase();
         if (var6.distanceToSqr(var1.position()) > ВъШ) {
            var6 = var1.position();
         }

         float var7 = var1.getInterpolation().yRot();
         float var8 = var1.getInterpolation().xRot();
         EntityMotionTracker.Sample var9 = this.motionTracker.д(var1.getId(), MINECRAFT_CLIENT.player.tickCount, var6, var1.getKnownMovement(), var7, var8);
         double var10 = this.Ш_3f39d5c3() + var9.age();
         AABB var12 = var1.getBoundingBox().move(var9.position().subtract(var1.position()));
         MobEffectInstance var13 = var1.getEffect(MobEffects.LEVITATION);
         EntityMotionSimulator.MotionProfile var14 = new EntityMotionSimulator.MotionProfile(
            var1.onGround(),
            var1.isFallFlying(),
            var1.getAbilities().flying,
            var1.isInWater() || var1.isInLava(),
            var1.onClimbable(),
            var1.hasEffect(MobEffects.SLOW_FALLING),
            var13 == null ? -1 : var13.getAmplifier(),
            this.Щ(var1),
            var1.isNoGravity() ? 0.0 : var1.getGravity(),
            Въы,
            Въй,
            var1.maxUpStep(),
            var7,
            var8
         );
         List var15 = EntityMotionSimulator.К(
            var12,
            var9,
            var14,
            64 + (int)Math.ceil(var10) + 1,
            (var1x, var2x) -> Entity.collideBoundingBox(var1, var2x, var1x, MINECRAFT_CLIENT.level, List.of()),
            PlayerMotionPredictor::й
         );
         Vec3 var16 = MINECRAFT_CLIENT.player.getKnownMovement();
         Vec3 var17 = new Vec3(var16.x, var5 ? 0.0 : var16.y, var16.z);
         final HashMap<BlockPos, Boolean> var18 = new HashMap<>();
         ProjectileInterceptSolver.Environment var19 = new ProjectileInterceptSolver.Environment() {
            @Override
            public double O(Vec3 var1, Vec3 var2x) {
               BlockHitResult var3 = MinecraftAccess.MINECRAFT_CLIENT
                  .level
                  .clipIncludingBorder(new ClipContext(var1, var2x, Block.COLLIDER, Fluid.NONE, MinecraftAccess.MINECRAFT_CLIENT.player));
               return var3.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var1.distanceTo(var3.getLocation());
            }

            @Override
            public boolean method_3(Vec3 var1) {
               BlockPos var2x = BlockPos.containing(var1);
               return var18.computeIfAbsent(var2x, var0 -> MinecraftAccess.MINECRAFT_CLIENT.level.getFluidState(var0).is(FluidTags.WATER));
            }
         };
         this.currentSolution = this.interceptSolver.Ю(var4, var17, var2, var3 -> EntityMotionSimulator.К(var15, var10 + var3), var19);
         return this.currentSolution != null;
      } else {
         return false;
      }
   }

   private void method_5(PlayerMovementSyncEvent var1) {
      if (this.pendingBowRelease != null && !var1.isCancelled()) {
         if (!this.щЪ()) {
            this.pendingBowRelease = null;
            this.releaseOnPostSync = false;
            this.Ы西();
         } else {
            this.releaseOnPostSync = true;
            var1.setYaw(this.pendingBowRelease.originalYaw());
            var1.setPitch(this.pendingBowRelease.originalPitch());
            if (!this.щ0() && MINECRAFT_CLIENT.player.getItemInHand(this.pendingBowRelease.hand()).is(Items.BOW)) {
               int var2 = Math.max(0, MINECRAFT_CLIENT.player.tickCount - this.pendingBowRelease.tick());
               Vec3 var3 = var1.getPosition().add(0.0, MINECRAFT_CLIENT.player.getEyeHeight() - Въ6, 0.0);
               if (this.ф(this.selectedTarget, Б(this.pendingBowRelease.drawTicks() + var2), var3, var1.isOnGround())) {
                  this.interceptRotation.ь(this.currentSolution);
                  RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.interceptRotation, 2, 45);
                  var1.setYaw(this.currentSolution.yaw());
                  var1.setPitch(this.currentSolution.pitch());
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.enabledLevel = MINECRAFT_CLIENT.level;
      this.enabledPlayer = MINECRAFT_CLIENT.player;
      this.lastObservedPlayerTick = MINECRAFT_CLIENT.player == null ? -1 : MINECRAFT_CLIENT.player.tickCount;
      this.pendingBowRelease = null;
      this.releaseOnPostSync = false;
      this.lastReleaseTick = -1;
      this.ЫЫ();
   }

   private boolean щЪ() {
      return this.pendingBowRelease != null && this.pendingBowRelease.world() == MINECRAFT_CLIENT.level && this.pendingBowRelease.shooter() == MINECRAFT_CLIENT.player && MINECRAFT_CLIENT.getConnection() != null;
   }

   private void Ыч() {
      if (this.enabledLevel != MINECRAFT_CLIENT.level || this.enabledPlayer != MINECRAFT_CLIENT.player || MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.tickCount < this.lastObservedPlayerTick) {
         this.pendingBowRelease = null;
         this.releaseOnPostSync = false;
         this.lastReleaseTick = -1;
         this.ЫЫ();
         this.Ы西();
         this.enabledLevel = MINECRAFT_CLIENT.level;
         this.enabledPlayer = MINECRAFT_CLIENT.player;
         this.lastObservedPlayerTick = MINECRAFT_CLIENT.player == null ? -1 : MINECRAFT_CLIENT.player.tickCount;
      }
   }

   private void Ы西() {
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.interceptRotation);
      RoseClient.getInstance().getControllers().getRotations().releaseStrategy(this.interceptRotation);
   }

   private boolean э(Player var1) {
      if (MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && var1 != null
         && var1 != MINECRAFT_CLIENT.player
         && var1.level() == MINECRAFT_CLIENT.level
         && var1.isAlive()
         && !var1.isRemoved()
         && !var1.isSpectator()
         && !(MINECRAFT_CLIENT.player.distanceToSqr(var1) > ВъТ)) {
         ClientServices var2 = RoseClient.getInstance().getServices();
         return !var2.getFriends().О(var1.getGameProfile().name()) && !var2.getModules().getAntiBot().detectedBots.contains(var1);
      } else {
         return false;
      }
   }

   @Override
   public void onDisable() {
      this.Ыщ();
      this.ЫЫ();
   }

   private boolean щЖ() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player.isUsingItem() && MINECRAFT_CLIENT.player.getUseItem().is(Items.BOW);
   }

   private boolean Щ(Player var1) {
      if (!var1.isFallFlying()) {
         return false;
      }

      for (FireworkRocketEntity var3 : MINECRAFT_CLIENT.level.getEntitiesOfClass(FireworkRocketEntity.class, var1.getBoundingBox().inflate(ВъК), Entity::isAlive)) {
         if (((FireworkRocketEntityAccessor)var3).rose$getAttachedEntity() == var1) {
            return true;
         }
      }

      return false;
   }

   private void ЫЫ() {
      this.selectedTarget = null;
      this.currentSolution = null;
      this.motionTracker.l1();
   }

   private static double Б(int var0) {
      float var1 = BowItem.getPowerForTime(var0);
      return var1 < ВъО ? 0.0 : var1 * ВъФ;
   }

   public Aimbot() {
      super(Въ7, ВъР, ModuleCategory.COMBAT);
   }

   private static final class InterceptRotation extends RotationStrategy {
      private final @com.google.gson.annotations.SerializedName("衣")  Vector2f targetAngles = new Vector2f();

      @Override
      public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
         return new Vector2f(var2);
      }

      private void ь(ProjectileInterceptSolver.Solution var1) {
         this.targetAngles.set(var1.yaw(), var1.pitch());
         this.setTargetPosition(var1.targetPosition());
      }

      private InterceptRotation() {
         super(Vec3.ZERO);
      }

      @Override
      public Vector2f getTargetRotation() {
         return this.targetAngles;
      }
   }

   private record PendingBowRelease(
      ServerboundPlayerActionPacket packet,
      ClientLevel world,
      Player shooter,
      InteractionHand hand,
      int drawTicks,
      int tick,
      float originalYaw,
      float originalPitch
   ) {
   }
}
