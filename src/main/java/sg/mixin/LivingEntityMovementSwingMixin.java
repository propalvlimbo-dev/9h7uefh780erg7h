package sg.mixin;

import sg.mixinbridge.HmiScriptState;
import sg.mixinbridge.HmiSwingStateAccess;
import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.HmiIntegration;
import sg.events.impl.GlidingVelocityEvent;
import sg.events.impl.PlayerJumpEvent;
import sg.ec.ViaProtocolVersion;
import sg.client.RoseClient;
import sg.events.impl.EntityTravelEvent;
import sg.events.impl.RenderSuppressionQuery;
import sg.events.impl.SwingDurationEvent;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMovementSwingMixin implements HmiSwingStateAccess {
   @Shadow
   private InteractionHand swingingArm;
   @Unique
   private boolean hmiInteractOffhand;
   @Unique
   private boolean hmiInteractMainHand;
   @Unique
   private boolean hmiBlockBreaking;
   @Unique
   private int hmiMainSwingCount;
   @Unique
   private boolean hmiSwingMHand;
   @Unique
   private boolean hmiSwingOHand;
   @Unique
   private int hmiMainHandSwingTicks;
   @Unique
   private int hmiOffHandSwingTicks;
   @Unique
   private boolean hmiMainHandSwinging;
   @Unique
   private boolean hmiOffHandSwinging;
   @Unique
   private float hmiMainHandSwingProgress;
   @Unique
   private float hmiLastMainHandSwingProgress;
   @Unique
   private float hmiOffHandSwingProgress;
   @Unique
   private float hmiLastOffHandSwingProgress;
   private static final double ЯхП = 3.0;

   @Inject(method = "jumpFromGround", at = @At("HEAD"))
   private void onLocalJump(CallbackInfo var1) {
      if ((Object)this == Minecraft.getInstance().player && RoseClient.getInstance() != null) {
         RoseClient.getInstance().getEventBus().post(new PlayerJumpEvent());
      }
   }

   @ModifyReturnValue(method = "isCurrentlyGlowing", at = @At("RETURN"))
   private boolean removePlayerGlowing(boolean var1) {
      return var1 && !RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.GLOWING);
   }

   @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
   private void onGetHandSwingDuration(CallbackInfoReturnable<Integer> var1) {
      SwingDurationEvent var2 = new SwingDurationEvent(this.swingingArm, (Integer)var1.getReturnValue());
      RoseClient.getInstance().getEventBus().post(var2);
      if (var2.isCancelled()) {
         int var3 = var2.getDurationControlValue();
         var1.setReturnValue(Math.max(1, 25 - var3 * 2));
      }
   }

   @ModifyReturnValue(method = "updateFallFlyingMovement", at = @At("RETURN"))
   private Vec3 onCalcGlidingVelocity(Vec3 var1) {
      if ((Object)this instanceof LocalPlayer) {
         GlidingVelocityEvent var2 = new GlidingVelocityEvent(var1);
         RoseClient.getInstance().getEventBus().post(var2);
         return var2.getVelocity();
      } else {
         return var1;
      }
   }

   @Redirect(
      method = "travelInWater",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D")
   )
   private double applyLegacyWaterMovement(LivingEntity var1, Holder<Attribute> var2) {
      double var3 = var1.getAttributeValue(var2);
      ProtocolVersion var5 = ViaProtocolVersion.Г();
      if (!var2.is(Attributes.WATER_MOVEMENT_EFFICIENCY) || var5 == null || !var5.olderThanOrEqualTo(ProtocolVersion.v1_21_4)) {
         return var3;
      }

      if (var1 != Minecraft.getInstance().player) {
         return var3;
      }

      ItemStack var6 = var1.getItemBySlot(EquipmentSlot.FEET);
      if (var6.isEmpty()) {
         return var3;
      }

      Reference var7 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.DEPTH_STRIDER);
      int var8 = Math.min(3, EnchantmentHelper.getItemEnchantmentLevel(var7, var6));
      return var8 <= 0 ? var3 : Math.min(var3, Math.min(3, var8) / ЯхП);
   }

   @Inject(method = "baseTick", at = @At("HEAD"))
   private void hmiTick(CallbackInfo var1) {
      if (HmiIntegration.п弟()) {
         LivingEntity var2 = (LivingEntity)(Object)this;
         this.hmiLastOffHandSwingProgress = this.hmiOffHandSwingProgress;
         this.hmiLastMainHandSwingProgress = this.hmiMainHandSwingProgress;
         int var3 = HmiScriptState.А.getOrDefault(var2.getMainHandItem().getItem().toString(), 10);
         if (this.hmiMainHandSwinging) {
            this.hmiMainHandSwingTicks++;
            if (this.hmiMainHandSwingTicks >= var3) {
               this.hmiMainHandSwingTicks = 0;
               this.hmiMainHandSwinging = false;
            }
         } else {
            if (this.hmiInteractMainHand) {
               this.hmiInteractMainHand = false;
            }

            if (this.hmiBlockBreaking) {
               this.hmiBlockBreaking = false;
            }

            this.hmiMainHandSwingTicks = 0;
         }

         this.hmiMainHandSwingProgress = (float)this.hmiMainHandSwingTicks / var3;
         int var4 = HmiScriptState.А.getOrDefault(var2.getOffhandItem().getItem().toString(), 10);
         if (this.hmiOffHandSwinging) {
            this.hmiOffHandSwingTicks++;
            if (this.hmiOffHandSwingTicks >= var4) {
               this.hmiOffHandSwingTicks = 0;
               this.hmiOffHandSwinging = false;
            }
         } else {
            if (this.hmiInteractOffhand) {
               this.hmiInteractOffhand = false;
            }

            this.hmiOffHandSwingTicks = 0;
         }

         this.hmiOffHandSwingProgress = (float)this.hmiOffHandSwingTicks / var4;
      }
   }

   @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
   private void hmiSwingHand(InteractionHand var1, boolean var2, CallbackInfo var3) {
      if (HmiIntegration.п弟()) {
         LivingEntity var4 = (LivingEntity)(Object)this;
         if (var1 == InteractionHand.OFF_HAND) {
            int var5 = HmiScriptState.А.getOrDefault(var4.getOffhandItem().getItem().toString(), 10);
            if (!this.hmiOffHandSwinging || this.hmiOffHandSwingTicks >= var5 / 2 || this.hmiOffHandSwingTicks < 0) {
               this.hmiOffHandSwingTicks = -1;
               this.hmiOffHandSwinging = true;
            }
         } else {
            int var6 = HmiScriptState.А.getOrDefault(var4.getMainHandItem().getItem().toString(), 10);
            if (!this.hmiMainHandSwinging || this.hmiMainHandSwingTicks >= var6 / 2 || this.hmiMainHandSwingTicks < 0) {
               this.hmiMainHandSwingTicks = -1;
               this.hmiMainHandSwinging = true;
               if (Minecraft.getInstance().gameMode.isDestroying()) {
                  this.hmiBlockBreaking = true;
               }
            }
         }
      }
   }

   @Override
   public float hMI5_0$getMainHandSwingProgress(float var1) {
      float var2 = this.hmiMainHandSwingProgress - this.hmiLastMainHandSwingProgress;
      if (var2 < 0.0F) {
         var2++;
      }

      return this.hmiLastMainHandSwingProgress + var2 * var1;
   }

   @Override
   public float hMI5_0$getOffHandSwingProgress(float var1) {
      float var2 = this.hmiOffHandSwingProgress - this.hmiLastOffHandSwingProgress;
      if (var2 < 0.0F) {
         var2++;
      }

      return this.hmiLastOffHandSwingProgress + var2 * var1;
   }

   @Override
   public boolean hMI5_0$getMHandEvent() {
      return this.hmiSwingMHand;
   }

   @Override
   public boolean hMI5_0$getOHandEvent() {
      return this.hmiSwingOHand;
   }

   @Override
   public boolean hMI5_0$getMInteract() {
      return this.hmiInteractMainHand;
   }

   @Override
   public boolean hMI5_0$getOInteract() {
      return this.hmiInteractOffhand;
   }

   @Override
   public boolean hMI5_0$getBlockBreak() {
      return this.hmiBlockBreaking;
   }

   @Override
   public int hMI5_0$getSwingCount() {
      return this.hmiMainSwingCount;
   }

   @Override
   public void hMI5_0$resetOffHandSwing(boolean var1) {
      this.hmiOffHandSwingTicks = 0;
      this.hmiOffHandSwinging = true;
      this.hmiSwingOHand = !this.hmiSwingOHand;
      this.hmiInteractOffhand = var1;
   }

   @Override
   public void hMI5_0$resetMainHandSwing(boolean var1) {
      this.hmiMainHandSwingTicks = 0;
      this.hmiMainHandSwinging = true;
      this.hmiSwingMHand = !this.hmiSwingMHand;
      this.hmiMainSwingCount++;
      this.hmiInteractMainHand = var1;
      if (Minecraft.getInstance().gameMode.isDestroying()) {
         this.hmiBlockBreaking = true;
      }
   }

   @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
   private void hookTravel(Vec3 var1, CallbackInfo var2) {
      LivingEntity var3 = (LivingEntity)(Object)this;
      EntityTravelEvent var4 = new EntityTravelEvent(var3);
      RoseClient.getInstance().getEventBus().post(var4);
      if (var4.isCancelled()) {
         var2.cancel();
      }
   }
}
