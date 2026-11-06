package sg.modules.impl.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.GlidingVelocityEvent;
import sg.ec.ElytraMotionPredictor;
import sg.rotation.AimGeometryMath;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.mixinbridge.ResolvedEntityPoseAccess;
import sg.network.InventoryInteractions;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.modules.impl.init.RoseXrayLines;
import sg.events.impl.FireworkBoostEvent;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;
import sg.modules.impl.movement.ElytraBooster;

public class ElytraTarget extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Я8")  FloatSetting attackDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("Я5")  FloatSetting detectionDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("ВВ")  BooleanSetting elytraLagSetting;
   private final @com.google.gson.annotations.SerializedName("Яв")  FloatSetting flyAwayDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("ВЯ")  BooleanSetting elytraOvertakeSetting;
   private final @com.google.gson.annotations.SerializedName("З")  SingleChoiceSetting predictionModeSetting;
   private final @com.google.gson.annotations.SerializedName("Яъ")  FloatSetting predictionStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Е")  SingleChoiceSetting attackWhenSetting;
   private final @com.google.gson.annotations.SerializedName("ЯМ")  FloatSetting predictFromBpsSetting;
   private final @com.google.gson.annotations.SerializedName("ВХ")  BooleanSetting autoFireworkSetting;
   private final @com.google.gson.annotations.SerializedName("Ве")  BooleanSetting immediateResponseSetting;
   private final @com.google.gson.annotations.SerializedName("ВА")  BooleanSetting renderPredictionSetting;
   final @com.google.gson.annotations.SerializedName("衣")  Stopwatch fireworkTimer;
   private @com.google.gson.annotations.SerializedName("Яы")  boolean fireworkBoostPending;
   private @com.google.gson.annotations.SerializedName("ВБ")  EventListener<PlayerTickEvent> playerTickListener;
   private @com.google.gson.annotations.SerializedName("ВС")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private final @com.google.gson.annotations.SerializedName("Вз")  EventListener<FireworkBoostEvent> fireworkBoostListener;
   private final @com.google.gson.annotations.SerializedName("Вц")  EventListener<GlidingVelocityEvent> glidingVelocityListener;
   private static final String В_д = "ElytraTarget";
   private static final String В_Б = "Преследует игрока на элитрах";
   private static final String В_С = "Дистанция атаки";
   private static final float В_з = 3.0F;
   private static final float В_ц = 5.0F;
   private static final float В_я = 0.1F;
   private static final String ВШс = "Дистанция обнаружения";
   private static final float ВШВ = 30.0F;
   private static final float ВШЯ = 15.0F;
   private static final float ВШХ = 120.0F;
   private static final float ВШе = 0.1F;
   private static final String ВША = "Элитра лаг";
   private static final String ВШ4 = "Дистанция отлета";
   private static final float ВШп = 7.5F;
   private static final float ВШД = 5.0F;
   private static final float ВШЪ = 15.0F;
   private static final float ВШт = 0.5F;
   private static final String ВШа = "Элитра перегон";
   private static final String ВШЮ = "Режим предикта";
   private static final String ВШЧ = "От ротации";
   private static final String ВШЛ = "От скорости";
   private static final String ВШ诶 = "Сила предикта";
   private static final float ВШ1 = 3.0F;
   private static final float ВШЬ = 1.5F;
   private static final float ВШ衣 = 10.0F;
   private static final float ВШш = 0.5F;
   private static final String ВШИ = "Бить когда";
   private static final String ВШь = "Перегнал";
   private static final String ВШж = "Всегда";
   private static final String ВШ弟 = "Предиктить от бпс";
   private static final float ВШб = 15.0F;
   private static final float ВШЫ = 10.0F;
   private static final float ВШl = 30.0F;
   private static final String ВШ8 = "Автоматически использовать фейверк";
   private static final String ВШ5 = "Моментальный отклик";
   private static final String ВШв = "Рендерить предикт";
   private static final double ВШъ = 1000.0;
   private static final double ВШМ = 2.6F;
   private static final float ВШН = 1.7F;
   private static final float ВШн = 0.17F;
   private static final double ВШЦ = 2.0;
   private static final double ВШч = 3.2;
   private static final double ВШЭ = 1.0E-5F;
   private static final double ВШ必 = 1.0E-5F;
   private static final double ВШщ = 1.0E-5F;
   private static final double ВШГ = 4.5;
   private static final float ВШл = (float) (Math.PI / 180.0);
   private static final double ВШэ = 0.01;
   private static final double ВШю = -1.0;
   private static final double ВШЩ = 0.75;
   private static final double ВШу = -0.1;
   private static final double ВШ2 = 0.04;
   private static final double ВШф = 0.1;
   private static final double ВШи = 0.1;
   private static final double ВШ9 = 0.99F;
   private static final double ВШо = 0.98F;
   private static final double ВШO = 0.99F;
   private static final float ВШ7 = 0.5F;
   private static final double ВШР = 0.75;
   private static final double ВШ3 = 0.75;
   private static final double ВШр = 0.75;
   private static final String ВШ6 = "От ротации";

   public ElytraTarget() {
      super(В_д, В_Б, ModuleCategory.COMBAT);
      this.attackDistanceSetting = new FloatSetting(В_С, В_з, 1.0F, В_ц).withStep(В_я);
      this.detectionDistanceSetting = new FloatSetting(ВШс, ВШВ, ВШЯ, ВШХ).withStep(ВШе);
      this.elytraLagSetting = new BooleanSetting(ВША, false);
      this.flyAwayDistanceSetting = new FloatSetting(ВШ4, ВШп, ВШД, ВШЪ).withStep(ВШт).visibleWhen(() -> this.elytraLagSetting.getValue());
      this.elytraOvertakeSetting = new BooleanSetting(ВШа, false);
      this.predictionModeSetting = new SingleChoiceSetting(ВШЮ, new ChoiceOption(ВШЧ), new ChoiceOption(ВШЛ)).visibleWhen(() -> this.elytraOvertakeSetting.getValue());
      this.predictionStrengthSetting = new FloatSetting(ВШ诶, ВШ1, ВШЬ, ВШ衣).withStep(ВШш).visibleWhen(() -> this.elytraOvertakeSetting.getValue());
      this.attackWhenSetting = new SingleChoiceSetting(ВШИ, new ChoiceOption(ВШь), new ChoiceOption(ВШж));
      this.predictFromBpsSetting = new FloatSetting(ВШ弟, ВШб, ВШЫ, ВШl).withStep(1.0F);
      this.autoFireworkSetting = new BooleanSetting(ВШ8, true);
      this.immediateResponseSetting = new BooleanSetting(ВШ5, true);
      this.renderPredictionSetting = new BooleanSetting(ВШв, true);
      this.fireworkTimer = new Stopwatch();
      this.playerTickListener = this::РЬ;
      this.worldEffectsListener = this::Ч;
      this.fireworkBoostListener = this::method_3;
      this.glidingVelocityListener = this::method_3;
      this.addSettings(this.attackDistanceSetting, this.detectionDistanceSetting, this.elytraLagSetting, this.flyAwayDistanceSetting, this.elytraOvertakeSetting, this.predictionModeSetting, this.predictionStrengthSetting, this.attackWhenSetting, this.predictFromBpsSetting, this.autoFireworkSetting, this.immediateResponseSetting, this.renderPredictionSetting);
   }

   @Generated
   public Stopwatch getFireworkTimer() {
      return this.fireworkTimer;
   }

   @Generated
   public FloatSetting В() {
      return this.flyAwayDistanceSetting;
   }

   @Generated
   public boolean а1() {
      return this.fireworkBoostPending;
   }

   @Generated
   public FloatSetting РР() {
      return this.predictFromBpsSetting;
   }

   @Generated
   public EventListener<FireworkBoostEvent> р() {
      return this.fireworkBoostListener;
   }

   @Generated
   public SingleChoiceSetting И_f1816549() {
      return this.attackWhenSetting;
   }

   @Generated
   public BooleanSetting Ч() {
      return this.elytraLagSetting;
   }

   @Generated
   public SingleChoiceSetting getPredictionModeSetting() {
      return this.predictionModeSetting;
   }

   public void РЬ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (!MINECRAFT_CLIENT.player.isFallFlying()) {
            this.fireworkBoostPending = false;
         }

         Aura var2 = RoseClient.getInstance().getServices().getModules().getAura();
         if (var2.isEnabled() && MINECRAFT_CLIENT.player.isFallFlying() && var2.Ж_95bedd6e() != null && this.autoFireworkSetting.getValue() && this.fireworkTimer.Т(ВШъ)) {
            InventoryInteractions.Ъ_de5a6f78(Items.FIREWORK_ROCKET);
            this.fireworkTimer.ВД();
         }
      }
   }

   @Generated
   public BooleanSetting 诶_3276742a() {
      return this.renderPredictionSetting;
   }

   private void method_3(GlidingVelocityEvent var1) {
      if (MINECRAFT_CLIENT.player != null && this.immediateResponseSetting.getValue()) {
         LivingEntity var2 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
         if (this.fireworkBoostPending) {
            float var3 = ВШН;
            ElytraBooster var4 = RoseClient.getInstance().getServices().getModules().getElytraBooster();
            if (var4.isEnabled()) {
               var3 = var4.М4() + ВШн;
            }

            if (var2 != null && MINECRAFT_CLIENT.player.getEyePosition().distanceTo(this.п(var2)) < ВШЦ) {
               var3 = (float)var2.getDeltaMovement().length();
            }

            var1.setVelocity(this.а(Vec3.directionFromRotation(MINECRAFT_CLIENT.player.getXRot(), MINECRAFT_CLIENT.player.getYRot()).scale(var3), 1.0));
            this.fireworkBoostPending = false;
         } else if (var2 != null) {
            Vec3 var5 = this.а(MINECRAFT_CLIENT.player.getDeltaMovement(), ВШч);
            Vec3 var6 = new Vec3(var2.getDeltaMovement().x * ВШЭ, var2.getDeltaMovement().y * ВШ必, var2.getDeltaMovement().z * ВШщ);
            if (MINECRAFT_CLIENT.player.getEyePosition().distanceTo(this.п(var2)) < ВШГ
               && var2.isFallFlying()
               && Math.hypot(var2.getDeltaMovement().x, var2.getDeltaMovement().z) != 0.0) {
               var1.setVelocity(var5.add(var6));
            }
         }
      }
   }

   public boolean method_7(LivingEntity var1) {
      return MINECRAFT_CLIENT.player.isFallFlying();
   }

   private void Ч(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.renderPredictionSetting.getValue()) {
            LivingEntity var2 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
            if (var2 != null) {
               Aura var3 = RoseClient.getInstance().getServices().getModules().getAura();
               if (var3.isEnabled() && MINECRAFT_CLIENT.player.isFallFlying() && var2.isFallFlying()) {
                  PoseStack var4 = var1.getPoses();
                  VertexConsumer var5 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
                  Vec3 var6 = var1.getCameraPosition();
                  Vec3 var7 = var2.position().add(0.0, var2.getBbHeight() / 2.0F + (MINECRAFT_CLIENT.player.isFallFlying() ? ВШ7 : 0.0F), 0.0);
                  Vec3 var8 = this.К(var2, var7);
                  AABB var9 = AABB.ofSize(var8, ВШР, ВШ3, ВШр);
                  WorldBoxGeometry.appendBoxOutline(var4, var5, var6, var9.minX, var9.minY, var9.minZ, var9.maxX, var9.maxY, var9.maxZ, -1);
               }
            }
         }
      }
   }

   @Generated
   public BooleanSetting е_3276742a() {
      return this.elytraOvertakeSetting;
   }

   @Generated
   public FloatSetting Т_989fed9a() {
      return this.detectionDistanceSetting;
   }

   private Vec3 п(LivingEntity var1) {
      return var1.getEyePosition().add(var1.getDeltaMovement());
   }

   @Generated
   public FloatSetting getPredictionStrengthSetting() {
      return this.predictionStrengthSetting;
   }

   @Generated
   public FloatSetting с_989fed9a() {
      return this.attackDistanceSetting;
   }

   private void method_3(FireworkBoostEvent var1) {
      if (MINECRAFT_CLIENT.player != null && this.immediateResponseSetting.getValue()) {
         LivingEntity var2 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
         Vec3 var3 = var2 != null ? AimGeometryMath.clampPointToBox(MINECRAFT_CLIENT.player.getEyePosition(), var2.getBoundingBox()) : Vec3.ZERO;
         if (var1.getEntity() == MINECRAFT_CLIENT.player && (var2 == null || !MINECRAFT_CLIENT.player.isFallFlying() || var2.isFallFlying() || !(MINECRAFT_CLIENT.player.getEyePosition().distanceTo(var3) < ВШМ))) {
            this.fireworkBoostPending = true;
         }
      }
   }

   @Override
   public void onDisable() {
      this.fireworkBoostPending = false;
   }

   @Generated
   public BooleanSetting getAutoFireworkSetting() {
      return this.autoFireworkSetting;
   }

   @Generated
   public EventListener<WorldEffectsEvent> э_f8db0d09() {
      return this.worldEffectsListener;
   }

   private Vec3 а(Vec3 var1, double var2) {
      Vec3 var4 = MINECRAFT_CLIENT.player.getLookAngle();
      float var5 = MINECRAFT_CLIENT.player.getXRot() * ВШл;
      double var6 = Math.sqrt(var4.x * var4.x + var4.z * var4.z);
      double var8 = var1.horizontalDistance();
      boolean var10 = MINECRAFT_CLIENT.player.getDeltaMovement().y <= 0.0;
      double var11 = var10 && MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING) ? Math.min(MINECRAFT_CLIENT.player.getGravity(), ВШэ) : MINECRAFT_CLIENT.player.getGravity();
      double var13 = Mth.square(Math.cos(var5));
      var1 = var1.add(0.0, var11 * (ВШю + var13 * ВШЩ), 0.0);
      if (var1.y < 0.0 && var6 > 0.0) {
         double var15 = var1.y * ВШу * var13;
         var1 = var1.add(var4.x * var15 / var6, var15, var4.z * var15 / var6);
      }

      if (var5 < 0.0F && var6 > 0.0) {
         double var18 = var8 * -Mth.sin(var5) * ВШ2;
         var1 = var1.add(-var4.x * var18 / var6, var18 * var2, -var4.z * var18 / var6);
      }

      if (var6 > 0.0) {
         var1 = var1.add((var4.x / var6 * var8 - var1.x) * ВШф, 0.0, (var4.z / var6 * var8 - var1.z) * ВШи);
      }

      return var1.multiply(ВШ9, ВШо, ВШO);
   }

   @Generated
   public EventListener<PlayerTickEvent> Ф() {
      return this.playerTickListener;
   }

   @Generated
   public BooleanSetting С() {
      return this.immediateResponseSetting;
   }

   public Vec3 К(LivingEntity var1, Vec3 var2) {
      if (this.isEnabled() && var1 != null && MINECRAFT_CLIENT.player != null) {
         if (!this.method_7(var1) || !this.elytraOvertakeSetting.getValue() || !var1.isFallFlying() || !(AimGeometryMath.getEntityMovementPerSecond(var1) > this.predictFromBpsSetting.getValue().floatValue())) {
            return var2;
         } else {
            return this.predictionModeSetting.isSelected(ВШ6)
               ? var2.add(
                  var1.calculateViewVector(((ResolvedEntityPoseAccess)var1).getResolvedPitch(), ((ResolvedEntityPoseAccess)var1).getResolvedYaw())
                     .scale(this.predictionStrengthSetting.getValue().floatValue())
               )
               : var2.add(ElytraMotionPredictor.method_9(var1, this.predictionStrengthSetting.getValue()));
         }
      } else {
         return var2;
      }
   }

   @Generated
   public EventListener<GlidingVelocityEvent> т() {
      return this.glidingVelocityListener;
   }
}
