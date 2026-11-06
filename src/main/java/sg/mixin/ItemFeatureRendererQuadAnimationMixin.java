package sg.mixin;

import sg.mixinbridge.HmiScriptState;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer.Submit;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settings.render.HmiIntegration;
import settings.render.HmiHeldItemQueue;
import sg.ec.HmiHandSwitchQueue;
import sg.events.impl.HmiHandRenderContext;
import sg.manager.ItemQuadAnimationScript;
import sg.manager.HmiScriptRegistry;

@Mixin(ItemFeatureRenderer.class)
public class ItemFeatureRendererQuadAnimationMixin {
   @Unique
   private final Map<Submit, Map<BakedQuad, Pose>> rose$poses = new IdentityHashMap<>();

   @WrapOperation(
      method = {"prepareMainSubmit", "prepareOutlineSubmit", "prepareFoilSubmit"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBakedQuad(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/resources/model/geometry/BakedQuad;Lcom/mojang/blaze3d/vertex/QuadInstance;)V"
      )
   )
   private void rose$animateQuad(VertexConsumer var1, Pose var2, BakedQuad var3, QuadInstance var4, Operation<Void> var5, Submit var6) {
      if (!HmiIntegration.п弟()) {
         var5.call(var1, var2, var3, var4);
      } else {
         Map var7 = this.rose$poses.computeIfAbsent(var6, this::rose$preparePoses);
         var5.call(var1, var7.getOrDefault(var3, var2), var3, var4);
      }
   }

   @Unique
   private Map<BakedQuad, Pose> rose$preparePoses(Submit var1) {
      Minecraft var2 = Minecraft.getInstance();
      ItemDisplayContext var3 = var1.displayContext();
      if (var2.player != null
         && var2.options.getCameraType().isFirstPerson()
         && (var3 == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || var3 == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
         HmiHandRenderContext var4 = HmiHandSwitchQueue.弟();
         ItemStack var5 = HmiHeldItemQueue.н();
         HmiScriptRegistry.В_673b7c17.с(var4, var5, var2.player, HmiScriptState.В_95877909);

         for (ItemQuadAnimationScript var7 : HmiScriptRegistry.Ю) {
            var7.с(var4, var5, var2.player, HmiScriptState.В_95877909);
         }
      }

      IdentityHashMap var8 = new IdentityHashMap();
      PoseStack var9 = new PoseStack();
      var9.last().set(var1.pose());

      for (int var10 = 0; var10 < var1.quads().size(); var10++) {
         var9.pushPose();
         HmiScriptState.В_95877909.method_8(var10, var9);
         var8.put(var1.quads().get(var10), var9.last().copy());
         var9.popPose();
      }

      return var8;
   }

   @Inject(method = "buildGroup", at = @At("TAIL"))
   private void rose$finishAnimations(FeatureFrameContext var1, List<Submit> var2, CallbackInfo var3) {
      this.rose$poses.clear();
      HmiScriptState.В_95877909.Ну();
   }
}
