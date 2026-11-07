package sg.mixin;

import sg.mixinbridge.HmiClientInitializer;
import sg.mixinbridge.HmiBlockItemRenderer;
import sg.mixinbridge.HmiScriptState;
import sg.mixinbridge.HeldItemRenderOverrideEvent;
import sg.mixinbridge.HmiSwingStateAccess;
import sg.mixinbridge.HmiItemStackAccess;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BellRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settings.render.HmiIntegration;
import settings.render.HmiHeldItemQueue;
import sg.ec.HmiHandSwitchQueue;
import sg.events.impl.ArmRenderEvent;
import sg.ec.HandParticleRenderer;
import sg.ec.WeaponSkinStacks;
import sg.events.impl.HmiHandRenderContext;
import sg.manager.HeldItemAnimationScript;
import sg.manager.HmiScriptRegistry;
import sg.modules.impl.render.Hands;
import sg.client.RoseClient;
import sg.events.impl.HeldItemPoseEvent;
import sg.events.impl.EatBobbingEvent;
import sg.modules.impl.render.SwingAnimations;
import sg.modules.impl.render.ViewModel;
import sg.render.ScriptedParticle;

@Mixin(value = ItemInHandRenderer.class, priority = 1100)
public abstract class ItemInHandRendererAnimationMixin {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Unique
   private boolean hmiMainHandSwitchEvent;
   @Unique
   private boolean hmiOffHandSwitchEvent;
   @Unique
   private Item hmiPrevMainHand = Items.AIR;
   @Unique
   private Item hmiPrevOffHand = Items.AIR;
   @Unique
   private final ArrayList<ScriptedParticle> hmiParticles = new ArrayList<>();
   @Unique
   private boolean hmiSwingMHand;
   @Unique
   private boolean hmiSwingOHand;
   @Unique
   private float hmiMainHandSwingProgress;
   @Unique
   private float hmiOffHandSwingProgress;
   private static final double сЦЧ = 0.22;
   private static final double сЦЛ = 0.25;
   private static final double сЦ诶 = 0.2;
   private static final String сЦ1 = "torch";
   private static final double сЦЬ = -0.05;
   private static final float сЦ衣 = 1.75F;
   private static final float сЦш = 1.75F;
   private static final float сЦИ = 1.75F;
   private static final double сЦь = -0.25;
   private static final double сЦж = -0.05;
   private static final float сЦ弟 = 0.3F;
   private static final float сЦб = 0.3F;
   private static final float сЦЫ = 0.3F;
   private static final double сЦl = -0.9;
   private static final double сЦ8 = -0.45;
   private static final double сЦ5 = -0.7;
   private static final float сЦв = -0.3F;
   private static final float сЦъ = 0.3F;
   private static final float сЦМ = 0.3F;
   private static final float сЦН = 0.3F;
   private static final double сЦн = -0.9;
   private static final double сЦЦ = -0.45;
   private static final double сЦч = -0.7;
   private static final String сЦЭ = "ITEM";
   private static final double сЦ必 = -0.05;
   private static final double сЦщ = 0.2;
   private static final double сЦГ = 0.1;
   private static final float сЦл = -12.0F;
   private static final String сЦэ = "ITEM";
   private static final String сЦю = "SCREEN";
   private static final double сЦЩ = -0.35;
   private static final double сЦу = 0.2;
   private static final double сЦ2 = 1.5;
   private static final double сЦф = -0.3;
   private static final double сЦи = -0.6;
   private static final float сЦ9 = 15.0F;
   private static final float сЦо = 0.5F;
   private static final float сЦO = 0.5F;
   private static final float сЦ7 = 0.5F;
   private static final float сЦР = 35.0F;
   private static final float сЦ3 = 0.5F;
   private static final float сЦр = 0.5F;
   private static final float сЦ6 = 0.5F;
   private static final float сЦ_ = -65.0F;
   private static final float сЦШ = 0.5F;
   private static final float сЦы = 0.5F;
   private static final float сЦй = 0.5F;
   private static final float сЦЖ = 0.9F;
   private static final float сЦм = 0.9F;
   private static final float сЦК = 0.9F;
   private static final double сЦI = 0.5;
   private static final double сЦТ = -0.15;
   private static final double сЦО = -0.85;
   private static final float сЦФ = 15.0F;
   private static final float сЦЙ = 0.5F;
   private static final float сЦк = 0.5F;
   private static final float сЦг = 0.5F;
   private static final float сЦ0 = 0.9F;
   private static final float сЦх = 0.9F;
   private static final float сЦУ = 0.9F;
   private static final float сЦЗ = -1.0F;
   private static final float сЦЕ = -0.3F;
   private static final float сЦП = (float) Math.PI;
   private static final float сЦ西 = 0.4F;
   private static final float сЦд = (float) (Math.PI * 2);
   private static final float сЦБ = -0.4F;
   private static final float сЦС = (float) Math.PI;
   private static final float сЦз = 0.64000005F;
   private static final float сЦц = 0.6F;
   private static final float сЦя = -0.6F;
   private static final float счс = 0.71999997F;
   private static final float счВ = 45.0F;
   private static final float счЯ = (float) Math.PI;
   private static final float счХ = (float) Math.PI;
   private static final float сче = 70.0F;
   private static final float счА = -20.0F;
   private static final float сч4 = -1.0F;
   private static final float счп = 3.6F;
   private static final float счД = 3.5F;
   private static final float счЪ = 120.0F;
   private static final float счт = 200.0F;
   private static final float сча = -135.0F;
   private static final float счЮ = 5.6F;
   private static final String счЧ = "Обычный";
   private static final String счЛ = "HMI";

   @Shadow
   protected abstract void renderPlayerArm(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6);

   @Shadow
   protected abstract void submitArmWithItem(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10
   );

   @Shadow
   protected abstract void renderMap(PoseStack var1, SubmitNodeCollector var2, int var3, ItemStack var4);

   @Shadow
   public abstract void renderItem(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, PoseStack var4, SubmitNodeCollector var5, int var6);

   @ModifyVariable(method = "submitArmWithItem", at = @At("HEAD"), argsOnly = true)
   private ItemStack replaceCosmeticItem(ItemStack var1) {
      return WeaponSkinStacks.applySelectedSkin(var1);
   }

   @Inject(
      method = "submitArmWithItem",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0, shift = Shift.AFTER)
   )
   private void applyViewModel(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      CallbackInfo var11
   ) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RoseClient.getInstance().getEventBus().post(new HeldItemPoseEvent(var8, var6, var4));
      }

      ViewModel var12 = this.hmiViewModel();
      if (var12 != null && !HmiIntegration.п弟()) {
         var12.Ъ(var4, !var6.isEmpty(), var8);
      }
   }

   @Inject(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
      )
   )
   private void applyViewModelScale(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      CallbackInfo var11
   ) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         ViewModel var12 = RoseClient.getInstance().getServices().getModules().getViewModel();
         if (var12 != null && var12.isEnabled()) {
            var12.л(var4, var8);
         }
      }
   }

   @WrapOperation(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderPlayerArm(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFLnet/minecraft/world/entity/HumanoidArm;)V"
      )
   )
   private void applyVanillaHandPartOffset(
      ItemInHandRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, float var5, float var6, HumanoidArm var7, Operation<Void> var8
   ) {
      ViewModel var9 = this.hmiViewModel();
      if (var9 != null && !HmiIntegration.п弟()) {
         if (!ViewModel.弟O()) {
            var8.call(var1, var2, var3, var4, var5, var6, var7);
         }
      } else {
         var8.call(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   @WrapOperation(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
      )
   )
   private void applyVanillaItemPartOffset(
      ItemInHandRenderer var1,
      LivingEntity var2,
      ItemStack var3,
      ItemDisplayContext var4,
      PoseStack var5,
      SubmitNodeCollector var6,
      int var7,
      Operation<Void> var8
   ) {
      ViewModel var9 = this.hmiViewModel();
      if (var9 != null && !HmiIntegration.п弟()) {
         if (!ViewModel.弟Ю()) {
            var8.call(var1, var2, var3, var4, var5, var6, var7);
         }
      } else {
         var8.call(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   @Unique
   private ViewModel hmiViewModel() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         ViewModel var1 = RoseClient.getInstance().getServices().getModules().getViewModel();
         return var1 != null && var1.isEnabled() ? var1 : null;
      } else {
         return null;
      }
   }

   @Unique
   private InteractionHand hmiHand(HumanoidArm var1) {
      return this.minecraft.player != null && var1 == this.minecraft.player.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
   }

   @Redirect(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
      )
   )
   private void hmiRenderOverhaul(
      ItemInHandRenderer var1,
      AbstractClientPlayer var2,
      float var3,
      float var4,
      InteractionHand var5,
      float var6,
      ItemStack var7,
      float var8,
      PoseStack var9,
      SubmitNodeCollector var10,
      int var11
   ) {
      if (!HmiIntegration.п弟()) {
         this.submitArmWithItem(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      } else if (!var2.isScoping()) {
         var7 = WeaponSkinStacks.applySelectedSkin(var7);
         ((HmiItemStackAccess)(Object)var7).hMI5_0$setTransform(-1);
         boolean var12 = var5 == InteractionHand.MAIN_HAND;
         boolean var13 = false;
         boolean var14 = false;
         if (var12 && var2.isUsingItem() && var2.getUsedItemHand() != var5) {
            ItemStack var15 = var2.getOffhandItem();
            if (var15.getUseAnimation() == ItemUseAnimation.BOW
               || var15.getUseAnimation() == ItemUseAnimation.BOW
               || var15.getUseAnimation() == ItemUseAnimation.CROSSBOW && !CrossbowItem.isCharged(var15)) {
               var7 = Items.AIR.getDefaultInstance();
            }
         }

         if (!var12 && var2.isUsingItem() && var2.getUsedItemHand() != var5) {
            ItemStack var32 = var2.getMainHandItem();
            if (var32.getUseAnimation() == ItemUseAnimation.BOW || var32.getUseAnimation() == ItemUseAnimation.CROSSBOW && !CrossbowItem.isCharged(var32)) {
               var7 = Items.AIR.getDefaultInstance();
            }
         }

         if (var12) {
            this.hmiMainHandSwitchEvent = var7.getItem() != this.hmiPrevMainHand;
         } else {
            this.hmiOffHandSwitchEvent = var7.getItem() != this.hmiPrevOffHand;
         }

         var9.pushPose();
         ViewModel var33 = RoseClient.getInstance().getServices().getModules().getViewModel();
         boolean var16 = var33 != null && var33.isEnabled();
         if (var16) {
            var33.л(var5, var7, var9);
         }

         if (var2 instanceof HmiSwingStateAccess var17) {
            this.hmiMainHandSwingProgress = var17.hMI5_0$getMainHandSwingProgress(var3);
            this.hmiOffHandSwingProgress = var17.hMI5_0$getOffHandSwingProgress(var3);
            var6 = var12 ? this.hmiMainHandSwingProgress : this.hmiOffHandSwingProgress;
            this.hmiSwingMHand = var17.hMI5_0$getMHandEvent();
            this.hmiSwingOHand = var17.hMI5_0$getOHandEvent();
            var13 = var12 ? var17.hMI5_0$getMInteract() : var17.hMI5_0$getOInteract();
            var14 = var17.hMI5_0$getBlockBreak();
         }

         HumanoidArm var34 = var12 ? var2.getMainArm() : var2.getMainArm().getOpposite();
         boolean var18 = var34 == HumanoidArm.RIGHT;
         int var19 = var18 ? 1 : -1;
         this.hmiScenePose(
            var9,
            var7,
            var18,
            var6,
            var8,
            var2,
            var12,
            var5,
            this.hmiMainHandSwingProgress,
            this.hmiOffHandSwingProgress,
            this.hmiMainHandSwitchEvent,
            this.hmiOffHandSwitchEvent,
            this.hmiSwingMHand,
            this.hmiSwingOHand,
            var13,
            var14,
            this.hmiParticles
         );
         var9.pushPose();
         this.hmiHandRelativePose(
            var9,
            var7,
            var18,
            var6,
            var8,
            var2,
            var12,
            var5,
            this.hmiMainHandSwingProgress,
            this.hmiOffHandSwingProgress,
            this.hmiMainHandSwitchEvent,
            this.hmiOffHandSwitchEvent,
            this.hmiSwingMHand,
            this.hmiSwingOHand,
            var13,
            var14,
            this.hmiParticles
         );
         int var20 = LightCoordsUtil.lightCoordsWithEmission(var11, Block.byItem(var7.getItem()).defaultBlockState().getLightEmission());
         if (!ViewModel.弟O()) {
            if (var16) {
               var33.л(var5, false, var9);
            }

            if (var2.isInvisible()) {
               this.hmiApplyArmMatrices(var9, var20, 0.0F, 0.0F, var34);
            } else {
               this.renderPlayerArm(var9, var10, var20, 0.0F, 0.0F, var34);
            }
         }

         var9.popPose();
         var9.pushPose();
         if (var16) {
            var33.л(var5, true, var9);
         }

         Block var21 = Block.byItem(var7.getItem());
         BlockState var22 = var21.defaultBlockState();
         boolean var23 = var21 != Blocks.AIR
            && !var22.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
            && !var22.is(BlockTags.CROPS)
            && var7.getUseAnimation() != ItemUseAnimation.EAT
            && !var7.is(Items.REDSTONE)
            && !var7.is(ItemTags.BANNERS)
            && !var7.is(ItemTags.SKULLS)
            && HmiScriptState.Я_e12d7b95.getOrDefault(var7.getItem().toString(), true);
         if (!ViewModel.弟Ю() && var23) {
            var6 = 0.0F;
            BlockState var36 = var22;
            this.hmiItemPose(
               var9,
               var7,
               var18,
               var6,
               var2,
               var12,
               var5,
               var8,
               this.hmiMainHandSwingProgress,
               this.hmiOffHandSwingProgress,
               this.hmiMainHandSwitchEvent,
               this.hmiOffHandSwitchEvent,
               this.hmiSwingMHand,
               this.hmiSwingOHand,
               var13,
               var14,
               this.hmiParticles
            );
            var9.translate(сЦЧ * var19, сЦЛ, сЦ诶);
            if (var7.is(Items.LEVER) || var36.is(BlockTags.BUTTONS)) {
               var36 = var36.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);
            }

            String var37 = var7.getHoverName().toString().toLowerCase();
            if (!var37.contains(сЦ1) && !(var21 instanceof LanternBlock) && !var36.is(BlockTags.ALL_HANGING_SIGNS)) {
               var9.translate(сЦь * var19, сЦж, 0.0);
            } else {
               var9.translate(сЦЬ * var19, 0.0, 0.0);
               var9.scale(сЦ衣, сЦш, сЦИ);
            }

            var9.pushPose();
            var9.scale(сЦ弟, сЦб, сЦЫ);
            var9.translate(сЦl * var19, сЦ8, сЦ5);
            var9.popPose();
            if (!var18) {
               var9.translate(сЦв, 0.0F, 0.0F);
            }

            var9.scale(сЦъ, сЦМ, сЦН);
            var9.translate(сЦн * var19, сЦЦ, сЦч);
            if (var16) {
               var33.л(var5, var9);
            }

            if (var7.is(Items.BELL)) {
               BellBlockEntity var26 = new BellBlockEntity(BlockPos.ZERO, Blocks.BELL.defaultBlockState());
               BellRenderState var27 = new BellRenderState();
               var27.lightCoords = var11;
               BlockEntityRenderer var28 = this.minecraft.getBlockEntityRenderDispatcher().getRenderer(var26);
               var28.submit(var27, var9, var10, new CameraRenderState());
               BlockState var29 = Blocks.BELL.defaultBlockState().setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.CEILING);
               HmiBlockItemRenderer.В_ccb1b0c5.Й(var29, var9, var10, var11, this.minecraft.level, var2);
            } else {
               if (var36.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                  var9.pushPose();
                  var9.translate(0.0F, 1.0F, 0.0F);
                  HmiBlockItemRenderer.В_ccb1b0c5
                     .Й(var36.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), var9, var10, var11, this.minecraft.level, var2);
                  var9.popPose();
               }

               HmiBlockItemRenderer.В_ccb1b0c5.Й(var36, var9, var10, var11, this.minecraft.level, var2);
            }

            var9.pushPose();
            if (!var18) {
               var9.translate(1.0F, 0.0F, 0.0F);
            }

            HandParticleRenderer.ю(this.hmiParticles, var9, var10, сЦЭ, var5, var11, var2, var3);
            var9.popPose();
         } else if (!ViewModel.弟Ю()
            && (
               var7.getUseAnimation() == ItemUseAnimation.BLOCK
                  || var7.getUseAnimation() == ItemUseAnimation.BRUSH
                  || var7.getUseAnimation() == ItemUseAnimation.TRIDENT
            )) {
            this.hmiItemPose(
               var9,
               var7,
               var18,
               var6,
               var2,
               var12,
               var5,
               var8,
               this.hmiMainHandSwingProgress,
               this.hmiOffHandSwingProgress,
               this.hmiMainHandSwitchEvent,
               this.hmiOffHandSwitchEvent,
               this.hmiSwingMHand,
               this.hmiSwingOHand,
               var13,
               var14,
               this.hmiParticles
            );
            if (var16) {
               var33.л(var5, var9);
            }

            ItemStack var35 = new ItemStack(var7.getItem(), var7.getCount());
            this.hmiCopyAppearanceComponents(var7, var35);
            ItemStack var25 = var12 ? HmiScriptState.В_a4db5d8e : HmiScriptState.Я_a4db5d8e;
            if (!var25.is(Items.AIR)) {
               var35 = var25;
            }

            HmiHeldItemQueue.н(var7);
            HmiHandSwitchQueue.弟(this.hmiContext(var18, var6, var2, var5, var12, var8, var13, var14, var7), var7);
            this.renderItem(var2, var35, var18 ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND, var9, var10, var11);
         } else if (!ViewModel.弟Ю()) {
            this.hmiItemPose(
               var9,
               var7,
               var18,
               var6,
               var2,
               var12,
               var5,
               var8,
               this.hmiMainHandSwingProgress,
               this.hmiOffHandSwingProgress,
               this.hmiMainHandSwitchEvent,
               this.hmiOffHandSwitchEvent,
               this.hmiSwingMHand,
               this.hmiSwingOHand,
               var13,
               var14,
               this.hmiParticles
            );
            if (var7.has(DataComponents.MAP_ID)) {
               var9.pushPose();
               var9.translate(сЦ必 * var19, сЦщ, сЦГ);
               var9.mulPose(Axis.YP.rotationDegrees(сЦл * var19));
               if (var16) {
                  var33.л(var5, var9);
               }

               this.renderMap(var9, var10, var11, var7);
               var9.popPose();
            } else {
               ItemStack var24 = var12 ? HmiScriptState.В_a4db5d8e : HmiScriptState.Я_a4db5d8e;
               if (var24.is(Items.AIR)) {
                  var24 = var7;
               }

               HmiHeldItemQueue.н(var7);
               HmiHandSwitchQueue.弟(this.hmiContext(var18, var6, var2, var5, var12, var8, var13, var14, var7), var7);
               if (var16) {
                  var33.л(var5, var9);
               }

               this.renderItem(var2, var24, var18 ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND, var9, var10, var11);
            }

            if (var18) {
               HmiClientInitializer.field__.set(var9.last().pose());
            } else {
               HmiClientInitializer.Ш.set(var9.last().pose());
            }

            var9.pushPose();
            HandParticleRenderer.ю(this.hmiParticles, var9, var10, сЦэ, var5, var11, var2, var3);
            var9.popPose();
         }

         var9.popPose();
         var9.popPose();
         if (var12) {
            this.hmiPrevMainHand = var7.getItem();
         } else {
            this.hmiPrevOffHand = var7.getItem();
         }

         if (!ViewModel.АI && !Hands.captureActive) {
            var9.pushPose();
            HandParticleRenderer.ю(this.hmiParticles, var9, var10, сЦю, var5, var11, var2, var3);
            var9.popPose();
         }

         HmiClientInitializer.ЮР = var3;
         HmiScriptState.Я_a4db5d8e = Items.AIR.getDefaultInstance();
         HmiScriptState.В_a4db5d8e = Items.AIR.getDefaultInstance();
      }
   }

   @Unique
   private HmiHandRenderContext hmiContext(
      boolean var1, float var2, AbstractClientPlayer var3, InteractionHand var4, boolean var5, float var6, boolean var7, boolean var8, ItemStack var9
   ) {
      return new HmiHandRenderContext(
         var1,
         var2,
         var3,
         var4,
         var5,
         HmiClientInitializer.Юр,
         var6,
         this.hmiMainHandSwingProgress,
         this.hmiOffHandSwingProgress,
         this.hmiMainHandSwitchEvent,
         this.hmiOffHandSwitchEvent,
         this.hmiSwingMHand,
         this.hmiSwingOHand,
         var7,
         var8,
         var9
      );
   }

   @Unique
   private void hmiScenePose(
      PoseStack var1,
      ItemStack var2,
      boolean var3,
      float var4,
      float var5,
      AbstractClientPlayer var6,
      boolean var7,
      InteractionHand var8,
      float var9,
      float var10,
      boolean var11,
      boolean var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      List<ScriptedParticle> var17
   ) {
      this.hmiExecute(HmiScriptRegistry.В_464ec6e0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);

      for (HeldItemAnimationScript var19 : HmiScriptRegistry.Ъ) {
         this.hmiExecute(var19, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      }

      if (!var2.isEmpty()) {
         var1.translate(0.0, сЦЩ, сЦу);
      }
   }

   @Unique
   private void hmiHandRelativePose(
      PoseStack var1,
      ItemStack var2,
      boolean var3,
      float var4,
      float var5,
      AbstractClientPlayer var6,
      boolean var7,
      InteractionHand var8,
      float var9,
      float var10,
      boolean var11,
      boolean var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      List<ScriptedParticle> var17
   ) {
      int var18 = var3 ? 1 : -1;
      this.hmiExecute(HmiScriptRegistry.Я, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);

      for (HeldItemAnimationScript var20 : HmiScriptRegistry.т) {
         this.hmiExecute(var20, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      }

      if (!var2.isEmpty()) {
         var1.translate(сЦ2 * var18, сЦф, сЦи);
         var1.rotateAround(Axis.XP.rotationDegrees(сЦ9), сЦо * var18, сЦO, сЦ7);
         var1.rotateAround(Axis.YP.rotationDegrees(сЦР * var18), сЦ3 * var18, сЦр, сЦ6);
         var1.rotateAround(Axis.ZP.rotationDegrees(сЦ_ * var18), сЦШ * var18, сЦы, сЦй);
         var1.scale(сЦЖ, сЦм, сЦК);
      }
   }

   @Unique
   private void hmiItemPose(
      PoseStack var1,
      ItemStack var2,
      boolean var3,
      float var4,
      AbstractClientPlayer var5,
      boolean var6,
      InteractionHand var7,
      float var8,
      float var9,
      float var10,
      boolean var11,
      boolean var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      List<ScriptedParticle> var17
   ) {
      int var18 = var3 ? 1 : -1;
      var1.translate(сЦI * var18, сЦТ, сЦО);
      var1.rotateAround(Axis.XP.rotationDegrees(сЦФ), сЦЙ, сЦк, сЦг);
      var1.scale(сЦ0, сЦх, сЦУ);
      this.hmiExecute(HmiScriptRegistry.Х, var1, var2, var3, var4, var8, var5, var6, var7, var9, var10, var11, var12, var13, var14, var15, var16, var17);

      for (HeldItemAnimationScript var20 : HmiScriptRegistry.а) {
         this.hmiExecute(var20, var1, var2, var3, var4, var8, var5, var6, var7, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      }
   }

   @Unique
   private void hmiExecute(
      HeldItemAnimationScript var1,
      PoseStack var2,
      ItemStack var3,
      boolean var4,
      float var5,
      float var6,
      AbstractClientPlayer var7,
      boolean var8,
      InteractionHand var9,
      float var10,
      float var11,
      boolean var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      boolean var17,
      List<ScriptedParticle> var18
   ) {
      if (var1 != null) {
         var1.ф(
            var2,
            var4,
            HmiScriptState.Д,
            var5,
            var3,
            var7,
            var9,
            var8,
            HmiClientInitializer.Юр,
            var6,
            var10,
            var11,
            var12,
            var13,
            var14,
            var15,
            var16,
            var17,
            var18
         );
      }
   }

   @Unique
   private void hmiApplyArmMatrices(PoseStack var1, int var2, float var3, float var4, HumanoidArm var5) {
      boolean var6 = var5 != HumanoidArm.LEFT;
      float var7 = var6 ? 1.0F : сЦЗ;
      float var8 = Mth.sqrt(var4);
      float var9 = сЦЕ * Mth.sin(var8 * сЦП);
      float var10 = сЦ西 * Mth.sin(var8 * сЦд);
      float var11 = сЦБ * Mth.sin(var4 * сЦС);
      var1.translate(var7 * (var9 + сЦз), var10 - сЦц + var3 * сЦя, var11 - счс);
      var1.mulPose(Axis.YP.rotationDegrees(var7 * счВ));
      float var12 = Mth.sin(var4 * var4 * счЯ);
      float var13 = Mth.sin(var8 * счХ);
      var1.mulPose(Axis.YP.rotationDegrees(var7 * var13 * сче));
      var1.mulPose(Axis.ZP.rotationDegrees(var7 * var12 * счА));
      var1.translate(var7 * сч4, счп, счД);
      var1.mulPose(Axis.ZP.rotationDegrees(var7 * счЪ));
      var1.mulPose(Axis.XP.rotationDegrees(счт));
      var1.mulPose(Axis.YP.rotationDegrees(var7 * сча));
      var1.translate(var7 * счЮ, 0.0F, 0.0F);
   }

   @Unique
   private void hmiCopyAppearanceComponents(ItemStack var1, ItemStack var2) {
      hmiCopyComponent(var1, var2, DataComponents.ENCHANTMENTS);
      hmiCopyComponent(var1, var2, DataComponents.ITEM_MODEL);
      hmiCopyComponent(var1, var2, DataComponents.CUSTOM_MODEL_DATA);
      hmiCopyComponent(var1, var2, DataComponents.CUSTOM_DATA);
   }

   @Unique
   private static <T> void hmiCopyComponent(ItemStack var0, ItemStack var1, DataComponentType<T> var2) {
      if (var0.has(var2)) {
         var1.set(var2, var0.get(var2));
      }
   }

   @ModifyExpressionValue(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;renderMainHand:Z")
   )
   private boolean hmiRenderMainHand(boolean var1) {
      return HmiIntegration.п弟() || var1;
   }

   @ModifyExpressionValue(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;renderOffHand:Z")
   )
   private boolean hmiRenderOffHand(boolean var1) {
      return HmiIntegration.п弟() || var1;
   }

   @Redirect(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewYRot(F)F")
   )
   public float redirectYaw(LocalPlayer var1, float var2) {
      return RoseClient.getInstance().getControllers().getRotations().getHandRotation().x;
   }

   @Redirect(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewXRot(F)F")
   )
   public float redirectPitch(LocalPlayer var1, float var2) {
      return RoseClient.getInstance().getControllers().getRotations().getHandRotation().y;
   }

   @Redirect(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F", ordinal = 0)
   )
   public float redirectRenderPitch(float var1, float var2, float var3) {
      return Mth.lerp(var1, RoseClient.getInstance().getControllers().getRotations().getPreviousSmoothedHandRotation().y, RoseClient.getInstance().getControllers().getRotations().getSmoothedHandRotation().y);
   }

   @Redirect(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F", ordinal = 1)
   )
   public float redirectRenderYaw(float var1, float var2, float var3) {
      return Mth.lerp(var1, RoseClient.getInstance().getControllers().getRotations().getPreviousSmoothedHandRotation().x, RoseClient.getInstance().getControllers().getRotations().getSmoothedHandRotation().x);
   }

   @Inject(
      method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V",
      at = @At("HEAD")
   )
   private void beginHandsCapture(float var1, PoseStack var2, SubmitNodeCollector var3, LocalPlayer var4, int var5, CallbackInfo var6) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Hands var7 = RoseClient.getInstance().getServices().getModules().getHands();
         ViewModel var8 = RoseClient.getInstance().getServices().getModules().getViewModel();
         boolean var9 = var8 != null && var8.弟К();
         if (!var9 && var7 != null) {
            var7.beginCapture();
         }
      }
   }

   @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
   private void swingArm(float var1, PoseStack var2, int var3, HumanoidArm var4, CallbackInfo var5) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         ArmRenderEvent var6 = new ArmRenderEvent(var2, var4, var1);
         RoseClient.getInstance().getEventBus().post(var6);
         if (var6.isCancelled()) {
            var5.cancel();
         }
      }
   }

   @Inject(method = "applyItemArmAttackTransform", at = @At("HEAD"), cancellable = true)
   private void cancelVanillaSwingOffset(PoseStack var1, HumanoidArm var2, float var3, CallbackInfo var4) {
      if (this.isSwingAnimationsEnabled()) {
         var4.cancel();
      }
   }

   @ModifyVariable(method = "applyItemArmTransform", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private float removeEquipProgress(float var1) {
      return this.isSwingAnimationsEnabled() ? 0.0F : var1;
   }

   @Unique
   private boolean isSwingAnimationsEnabled() {
      RoseClient var1 = RoseClient.getInstance();
      if (var1 != null && var1.getServices() != null && var1.getServices().getModules() != null) {
         SwingAnimations var2 = var1.getServices().getModules().getSwingAnimations();
         if (var2 == null || !var2.isEnabled() || var2.сГ.isSelected(счЧ)) {
            return false;
         } else if (var2.сГ.isSelected(счЛ)) {
            return true;
         } else {
            return !var2.Чг() && !var2.Я9.getValue();
         }
      } else {
         return false;
      }
   }

   @ModifyConstant(method = "applyEatTransform", constant = @Constant(floatValue = 0.1F))
   private float modifyEatBobbing(float var1) {
      EatBobbingEvent var2 = new EatBobbingEvent(var1);
      RoseClient.getInstance().getEventBus().post(var2);
      return var2.getBobbingScale();
   }

   @ModifyExpressionValue(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack replaceMainHandStack(ItemStack var1) {
      if (this.minecraft.player == null) {
         return var1;
      }

      HeldItemRenderOverrideEvent var2 = HeldItemRenderOverrideEvent.INSTANCE.resetForMainHandStack(this.minecraft.player, var1);
      RoseClient.getInstance().getEventBus().post(var2);
      return var2.getStack();
   }
}
