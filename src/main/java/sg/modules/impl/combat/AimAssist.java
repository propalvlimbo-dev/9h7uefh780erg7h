package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.WorldProjection;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.CameraRotationEvent;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.PlayerTurnEvent;
import sg.rotation.RotationController;
import sg.rotation.RotationStrategy;
import sg.settings.SingleChoiceSetting;

public final class AimAssist extends ClientModule {
   @SerializedName("Я")
   private final FloatSetting distanceSetting;
   @SerializedName("Х")
   private final FloatSetting captureFovSetting;
   @SerializedName("е")
   private final FloatSetting aimStrengthSetting;
   @SerializedName("Я")
   private final SingleChoiceSetting aimPointSetting;
   @SerializedName("В")
   private final BooleanSetting onlyWhileAttackingSetting;
   @SerializedName("Я")
   private final BooleanSetting ignoreFriendsSetting;
   @SerializedName("Х")
   private final BooleanSetting ignoreBotsSetting;
   @SerializedName("е")
   private final BooleanSetting throughWallsSetting;
   @SerializedName("А")
   private final BooleanSetting targetBoxSetting;
   @SerializedName("4")
   private final BooleanSetting targetLineSetting;
   @SerializedName("п")
   private final BooleanSetting targetMarkerSetting;
   @SerializedName("Д")
   private final BooleanSetting clientLookSetting;
   @SerializedName("В")
   private final ColorSetting highlightColorSetting;
   @SerializedName("В")
   private final AimAssist.AssistRotation assistRotation;
   @SerializedName("4")
   private final EventListener<PlayerTickEvent> tickListener;
   @SerializedName("п")
   private final EventListener<WorldEffectsEvent> worldEffectsListener;
   @SerializedName("Д")
   private final EventListener<HudRenderEvent> hudRenderListener;
   private final @com.google.gson.annotations.SerializedName("Ъ")  EventListener<CameraRotationEvent> cameraRotationListener;
   private final @com.google.gson.annotations.SerializedName("т")  EventListener<PlayerTurnEvent> playerTurnListener;
   @SerializedName("Я")
   private Player currentTarget;
   @SerializedName("В")
   private Vec3 aimPosition;
   @SerializedName("в")
   private boolean clientLookActive;
   private @com.google.gson.annotations.SerializedName("н")  long lastCameraUpdateNanos;
   private @com.google.gson.annotations.SerializedName("се")  float smoothedCameraYaw;
   private @com.google.gson.annotations.SerializedName("сА")  float smoothedCameraPitch;
   private static final String MODULE_NAME = "AimAssist";
   private static final String MODULE_DESCRIPTION = "Агрессивно наводит прицел на ближайшего игрока в зоне захвата";
   private static final String DISTANCE_LABEL = "Дистанция";
   private static final float DEFAULT_DISTANCE = 8.0F;
   private static final float MAX_DISTANCE = 16.0F;
   private static final float DISTANCE_STEP = 0.5F;
   private static final String CAPTURE_FOV_LABEL = "FOV захвата";
   private static final float DEFAULT_CAPTURE_FOV = 90.0F;
   private static final float MIN_CAPTURE_FOV = 5.0F;
   private static final float MAX_CAPTURE_FOV = 180.0F;
   private static final String AIM_STRENGTH_LABEL = "Сила наводки";
   private static final float DEFAULT_AIM_STRENGTH = 60.0F;
   private static final float MIN_AIM_STRENGTH = 5.0F;
   private static final float MAX_AIM_STRENGTH = 180.0F;
   private static final String AIM_POINT_LABEL = "Точка прицела";
   private static final String HEAD_OPTION = "Голова";
   private static final String BODY_OPTION = "Тело";
   private static final String FEET_OPTION = "Ноги";
   private static final String ONLY_WHILE_ATTACKING_LABEL = "Только при ЛКМ";
   private static final String IGNORE_FRIENDS_LABEL = "Игнорировать друзей";
   private static final String IGNORE_BOTS_LABEL = "Игнорировать ботов";
   private static final String THROUGH_WALLS_LABEL = "Сквозь стены";
   private static final String TARGET_BOX_LABEL = "Бокс цели";
   private static final String TARGET_LINE_LABEL = "Линия к цели";
   private static final String TARGET_MARKER_LABEL = "Маркер";
   private static final String CLIENT_LOOK_LABEL = "ClientLook (камера)";
   private static final String COLOR_LABEL = "Цвет";
   private static final int DEFAULT_HIGHLIGHT_COLOR = -11486721;
   private static final double AIM_HORIZONTAL_EPSILON = 1.0E-6;
   private static final float MIN_TARGET_PITCH = -90.0F;
   private static final float MAX_TARGET_PITCH = 90.0F;
   private static final double INITIAL_BEST_ANGULAR_DISTANCE = Double.MAX_VALUE;
   private static final double TARGET_SEARCH_HORIZONTAL_EPSILON = 1.0E-6;
   private static final String HEAD_AIM_OPTION = "Голова";
   private static final double HEAD_CENTER_X_FACTOR = 0.5;
   private static final double HEAD_CENTER_Z_FACTOR = 0.5;
   private static final double HEAD_EYE_HEIGHT_OFFSET_FACTOR = 0.15;
   private static final String FEET_AIM_OPTION = "Ноги";
   private static final double FEET_CENTER_X_FACTOR = 0.5;
   private static final double FEET_CENTER_Z_FACTOR = 0.5;
   private static final double FEET_HEIGHT_OFFSET = 0.1;
   private static final double TARGET_BOX_INFLATION = 0.1;
   private static final double CAMERA_HORIZONTAL_EPSILON = 1.0E-6;
   private static final float MIN_CAMERA_PITCH = -90.0F;
   private static final float MAX_CAMERA_PITCH = 90.0F;
   private static final float NANOS_TO_SECONDS = 1.0E-9F;
   private static final float MAX_CAMERA_DELTA_SECONDS = 0.05F;
   private static final float CAMERA_SMOOTHING_RATE = 12.0F;
   private static final float CAMERA_SMOOTHING_EXPONENT = -12.0F;
   private static final float MIN_SMOOTHED_CAMERA_PITCH = -90.0F;
   private static final float MAX_SMOOTHED_CAMERA_PITCH = 90.0F;

   private void Д(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.onlyWhileAttackingSetting.getValue() && !MINECRAFT_CLIENT.options.keyAttack.isDown()) {
            this.currentTarget = null;
            this.Л();
         } else {
            Player var2 = this.Ж_d012b85c();
            this.currentTarget = var2;
            if (var2 == null) {
               this.Л();
               this.aimPosition = null;
            } else {
               Vec3 var3 = this.Ж(var2);
               this.aimPosition = var3;
               Vec3 var4 = MINECRAFT_CLIENT.player.getEyePosition();
               Vec3 var5 = var3.subtract(var4);
               double var6 = Math.hypot(var5.x, var5.z);
               float var8 = (float)Math.toDegrees(Math.atan2(-var5.x, var5.z));
               float var9 = (float)(-Math.toDegrees(Math.atan2(var5.y, Math.max(var6, AIM_HORIZONTAL_EPSILON))));
               var9 = Mth.clamp(var9, MIN_TARGET_PITCH, MAX_TARGET_PITCH);
               this.assistRotation.Ы(var8, var9);
               this.assistRotation.setTargetPosition(var3);
               RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.assistRotation, 1, 5);
            }
         }
      }
   }

   private Player Ж_d012b85c() {
      float var1 = MINECRAFT_CLIENT.player.getYRot();
      float var2 = MINECRAFT_CLIENT.player.getXRot();
      Player var3 = null;
      double var4 = INITIAL_BEST_ANGULAR_DISTANCE;

      for (Player var7 : MINECRAFT_CLIENT.level.players()) {
         if (this.method_8(var7)) {
            Vec3 var8 = this.Ж(var7);
            Vec3 var9 = var8.subtract(MINECRAFT_CLIENT.player.getEyePosition());
            double var10 = Math.hypot(var9.x, var9.z);
            float var12 = (float)Math.toDegrees(Math.atan2(-var9.x, var9.z));
            float var13 = (float)(-Math.toDegrees(Math.atan2(var9.y, Math.max(var10, TARGET_SEARCH_HORIZONTAL_EPSILON))));
            float var14 = Mth.wrapDegrees(var12 - var1);
            float var15 = Mth.wrapDegrees(var13 - var2);
            double var16 = Math.hypot(var14, var15);
            if (!(var16 > this.captureFovSetting.getValue().floatValue()) && var16 < var4) {
               var4 = var16;
               var3 = var7;
            }
         }
      }

      return var3;
   }

   @Generated
   public BooleanSetting Д_3276742a() {
      return this.ignoreBotsSetting;
   }

   public AimAssist() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.distanceSetting = new FloatSetting(DISTANCE_LABEL, DEFAULT_DISTANCE, 1.0F, MAX_DISTANCE).withStep(DISTANCE_STEP);
      this.captureFovSetting = new FloatSetting(CAPTURE_FOV_LABEL, DEFAULT_CAPTURE_FOV, MIN_CAPTURE_FOV, MAX_CAPTURE_FOV).withStep(1.0F);
      this.aimStrengthSetting = new FloatSetting(AIM_STRENGTH_LABEL, DEFAULT_AIM_STRENGTH, MIN_AIM_STRENGTH, MAX_AIM_STRENGTH).withStep(1.0F);
      this.aimPointSetting = new SingleChoiceSetting(AIM_POINT_LABEL, new ChoiceOption(HEAD_OPTION), new ChoiceOption(BODY_OPTION), new ChoiceOption(FEET_OPTION));
      this.onlyWhileAttackingSetting = new BooleanSetting(ONLY_WHILE_ATTACKING_LABEL, false);
      this.ignoreFriendsSetting = new BooleanSetting(IGNORE_FRIENDS_LABEL, true);
      this.ignoreBotsSetting = new BooleanSetting(IGNORE_BOTS_LABEL, true);
      this.throughWallsSetting = new BooleanSetting(THROUGH_WALLS_LABEL, true);
      this.targetBoxSetting = new BooleanSetting(TARGET_BOX_LABEL, true);
      this.targetLineSetting = new BooleanSetting(TARGET_LINE_LABEL, true);
      this.targetMarkerSetting = new BooleanSetting(TARGET_MARKER_LABEL, true);
      this.clientLookSetting = new BooleanSetting(CLIENT_LOOK_LABEL, true);
      this.highlightColorSetting = new ColorSetting(COLOR_LABEL, false, DEFAULT_HIGHLIGHT_COLOR);
      this.assistRotation = new AimAssist.AssistRotation();
      this.tickListener = this::Д;
      this.worldEffectsListener = this::Ж;
      this.hudRenderListener = this::Ж;
      this.cameraRotationListener = this::Ж;
      this.playerTurnListener = this::Ж;
      this.addSettings(
         this.distanceSetting,
         this.captureFovSetting,
         this.aimStrengthSetting,
         this.aimPointSetting,
         this.onlyWhileAttackingSetting,
         this.ignoreFriendsSetting,
         this.ignoreBotsSetting,
         this.throughWallsSetting,
         this.targetBoxSetting,
         this.targetLineSetting,
         this.targetMarkerSetting,
         this.clientLookSetting,
         this.highlightColorSetting
      );
   }

   @Generated
   public FloatSetting Ж_989fed9a() {
      return this.distanceSetting;
   }

   @Generated
   public SingleChoiceSetting Ж_f1816549() {
      return this.aimPointSetting;
   }

   @Generated
   public boolean Б_bbeebd87() {
      return this.clientLookActive;
   }

   private void Ж(WorldEffectsEvent var1) {
      if (this.isEnabled() && this.currentTarget != null && this.aimPosition != null) {
         int var2 = this.highlightColorSetting.getValue();
         VertexConsumer var3 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         if (this.targetBoxSetting.getValue()) {
            AABB var4 = this.currentTarget.getBoundingBox().inflate(TARGET_BOX_INFLATION);
            WorldBoxGeometry.appendBoxOutline(var1.getPoses(), var3, var1.getCameraPosition(), var4.minX, var4.minY, var4.minZ, var4.maxX, var4.maxY, var4.maxZ, var2);
         }

         if (this.targetLineSetting.getValue()) {
            Vec3 var5 = var1.getCameraPosition();
            WorldBoxGeometry.appendLineWithNormals(
               var1.getPoses().last(),
               var3,
               (float)(var5.x - var5.x),
               (float)(var5.y - var5.y),
               (float)(var5.z - var5.z),
               (float)(this.aimPosition.x - var5.x),
               (float)(this.aimPosition.y - var5.y),
               (float)(this.aimPosition.z - var5.z),
               var2
            );
         }
      }
   }

   @Generated
   public ColorSetting Ж_f4ff5ac1() {
      return this.highlightColorSetting;
   }

   @Generated
   public BooleanSetting И_3276742a() {
      return this.targetBoxSetting;
   }

   private boolean Ф() {
      return this.isEnabled() && this.clientLookSetting.getValue() && this.currentTarget != null && this.aimPosition != null
         ? !RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()
         : false;
   }

   @Generated
   public FloatSetting Д_989fed9a() {
      return this.aimStrengthSetting;
   }

   @Generated
   public float я() {
      return this.smoothedCameraPitch;
   }

   @Generated
   public EventListener<PlayerTickEvent> Ж_f8db0d09() {
      return this.tickListener;
   }

   private void Ж(HudRenderEvent var1) {
      if (this.isEnabled() && this.currentTarget != null && this.aimPosition != null) {
         if (this.targetMarkerSetting.getValue()) {
            Vector2f var2 = WorldProjection.projectToScreen(this.aimPosition);
            if (var2 != null) {
               int var3 = this.highlightColorSetting.getValue();
               int var4 = (int)var2.x;
               int var5 = (int)var2.y;
               var1.getGraphics().fill(var4 - 3, var5 - 3, var4 + 3, var5 + 3, var3);
            }
         }
      }
   }

   private void Л() {
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.assistRotation);
   }

   @Generated
   public long method_8_6da43b94() {
      return this.lastCameraUpdateNanos;
   }

   @Generated
   public AimAssist.AssistRotation Ж_df902c3e() {
      return this.assistRotation;
   }

   @Generated
   public FloatSetting method_8_989fed9a() {
      return this.captureFovSetting;
   }

   private void Ж(CameraRotationEvent var1) {
      if (!this.Ф()) {
         this.clientLookActive = false;
         this.lastCameraUpdateNanos = 0L;
      } else {
         float var2 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = MINECRAFT_CLIENT.player.getEyePosition(var2);
         Vec3 var4 = this.aimPosition.subtract(var3);
         double var5 = Math.sqrt(var4.x * var4.x + var4.z * var4.z);
         float var7 = (float)Math.toDegrees(Math.atan2(-var4.x, var4.z));
         float var8 = (float)Math.toDegrees(-Math.atan2(var4.y, Math.max(var5, CAMERA_HORIZONTAL_EPSILON)));
         var8 = Mth.clamp(var8, MIN_CAMERA_PITCH, MAX_CAMERA_PITCH);
         long var9 = System.nanoTime();
         RotationController var11 = RoseClient.getInstance().getControllers().getRotations();
         if (!this.clientLookActive) {
            this.smoothedCameraYaw = MINECRAFT_CLIENT.player.getYRot();
            this.smoothedCameraPitch = MINECRAFT_CLIENT.player.getXRot();
            this.clientLookActive = true;
            this.lastCameraUpdateNanos = var9;
         }

         float var12 = (float)(var9 - this.lastCameraUpdateNanos) * NANOS_TO_SECONDS;
         this.lastCameraUpdateNanos = var9;
         var12 = Mth.clamp(var12, 0.0F, MAX_CAMERA_DELTA_SECONDS);
         float var13 = CAMERA_SMOOTHING_RATE;
         float var14 = 1.0F - (float)Math.exp(CAMERA_SMOOTHING_EXPONENT * var12);
         this.smoothedCameraYaw = this.smoothedCameraYaw + Mth.wrapDegrees(var7 - this.smoothedCameraYaw) * var14;
         this.smoothedCameraPitch = this.smoothedCameraPitch + (var8 - this.smoothedCameraPitch) * var14;
         this.smoothedCameraPitch = Mth.clamp(this.smoothedCameraPitch, MIN_SMOOTHED_CAMERA_PITCH, MAX_SMOOTHED_CAMERA_PITCH);
         var11.getCameraRotation().set(this.smoothedCameraYaw, this.smoothedCameraPitch);
         var1.setYaw(this.smoothedCameraYaw);
         var1.setPitch(this.smoothedCameraPitch);
      }
   }

   @Generated
   public BooleanSetting ы_3276742a() {
      return this.throughWallsSetting;
   }

   @Generated
   public BooleanSetting э_3276742a() {
      return this.targetMarkerSetting;
   }

   @Generated
   public Player method_8_d012b85c() {
      return this.currentTarget;
   }

   @Generated
   public BooleanSetting Ж_3276742a() {
      return this.onlyWhileAttackingSetting;
   }

   @Override
   public void onDisable() {
      this.currentTarget = null;
      this.aimPosition = null;
      this.clientLookActive = false;
      this.lastCameraUpdateNanos = 0L;
      this.Л();
   }

   private void Ж(PlayerTurnEvent var1) {
      if (this.Ф()) {
         var1.setCancelled(true);
      }
   }

   @Generated
   public BooleanSetting ю_3276742a() {
      return this.clientLookSetting;
   }

   @Generated
   public EventListener<PlayerTurnEvent> И_f8db0d09() {
      return this.playerTurnListener;
   }

   @Generated
   public EventListener<HudRenderEvent> Д_f8db0d09() {
      return this.hudRenderListener;
   }

   @Generated
   public float у() {
      return this.smoothedCameraYaw;
   }

   private boolean method_8(Player var1) {
      if (var1 != null && var1 != MINECRAFT_CLIENT.player && var1.isAlive() && !var1.isSpectator()) {
         if (MINECRAFT_CLIENT.player.distanceTo(var1) > this.distanceSetting.getValue()) {
            return false;
         }

         if (!this.throughWallsSetting.getValue() && !MINECRAFT_CLIENT.player.hasLineOfSight(var1)) {
            return false;
         }

         RoseClient var2 = RoseClient.getInstance();
         return this.ignoreFriendsSetting.getValue() && var2.getServices().getFriends().О(var1.getGameProfile().name())
            ? false
            : !this.ignoreBotsSetting.getValue() || !var2.getServices().getModules().getAntiBot().detectedBots.contains(var1);
      } else {
         return false;
      }
   }

   @Generated
   public Vec3 method_8_2f96ac28() {
      return this.aimPosition;
   }

   @Generated
   public BooleanSetting Е() {
      return this.targetLineSetting;
   }

   @Generated
   public BooleanSetting method_8_3276742a() {
      return this.ignoreFriendsSetting;
   }

   @Generated
   public EventListener<CameraRotationEvent> ы_f8db0d09() {
      return this.cameraRotationListener;
   }

   @Generated
   public EventListener<WorldEffectsEvent> method_8_f8db0d09() {
      return this.worldEffectsListener;
   }

   private Vec3 Ж(Player var1) {
      AABB var2 = var1.getBoundingBox();
      if (this.aimPointSetting.isSelected(HEAD_AIM_OPTION)) {
         double var9 = (var2.minX + var2.maxX) * HEAD_CENTER_X_FACTOR;
         double var10 = (var2.minZ + var2.maxZ) * HEAD_CENTER_Z_FACTOR;
         double var7 = var2.maxY - var1.getEyeHeight(var1.getPose()) * HEAD_EYE_HEIGHT_OFFSET_FACTOR;
         return new Vec3(var9, var7, var10);
      } else if (this.aimPointSetting.isSelected(FEET_AIM_OPTION)) {
         double var3 = (var2.minX + var2.maxX) * FEET_CENTER_X_FACTOR;
         double var5 = (var2.minZ + var2.maxZ) * FEET_CENTER_Z_FACTOR;
         return new Vec3(var3, var2.minY + FEET_HEIGHT_OFFSET, var5);
      } else {
         return var2.getCenter();
      }
   }

   private final class AssistRotation extends RotationStrategy {
      private final @com.google.gson.annotations.SerializedName("Ь")  Vector2f targetAngles = new Vector2f();
      private static final float MIN_DIRECT_PITCH = -90.0F;
      private static final float MAX_DIRECT_PITCH = 90.0F;
      private static final float MIN_STEPPED_PITCH = -90.0F;
      private static final float MAX_STEPPED_PITCH = 90.0F;

      @Override
      public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
         return this.у(var1, var2, AimAssist.this.aimStrengthSetting.getValue());
      }

      @Override
      public Vector2f я(Vector2f var1, Vector2f var2) {
         return this.у(var1, var2, AimAssist.this.aimStrengthSetting.getValue());
      }

      @Override
      public Vector2f getTargetRotation() {
         return new Vector2f(this.targetAngles);
      }

      private AssistRotation() {
         super(Vec3.ZERO);
      }

      private Vector2f у(Vector2f var1, Vector2f var2, float var3) {
         float var4 = Mth.wrapDegrees(var2.x - var1.x);
         float var5 = var2.y - var1.y;
         double var6 = Math.hypot(var4, var5);
         if (!(var6 <= var3) && var6 != 0.0) {
            float var8 = var3 / (float)var6;
            return new Vector2f(var1.x + var4 * var8, Mth.clamp(var1.y + var5 * var8, MIN_STEPPED_PITCH, MAX_STEPPED_PITCH));
         } else {
            return new Vector2f(var2.x, Mth.clamp(var2.y, MIN_DIRECT_PITCH, MAX_DIRECT_PITCH));
         }
      }

      private void Ы(float var1, float var2) {
         this.targetAngles.set(var1, var2);
      }
   }
}
