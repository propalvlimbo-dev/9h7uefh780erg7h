package sg.mixin;

import sg.mixinbridge.EntityRenderStateIdAccess;
import sg.mixinbridge.RenderSuppressionQueries;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.events.impl.EntityRenderStateEvent;
import sg.modules.impl.render.FullBright;
import sg.client.RoseClient;
import sg.modules.impl.misc.Optimization;
import sg.events.impl.RenderSuppressionQuery;
import sg.network.NameProtectionTextRewriter;

@Mixin(EntityRenderer.class)
public class EntityRendererVisibilityMixin {
   @Inject(method = "extractShadow", at = @At("HEAD"), cancellable = true, require = 0)
   private void skipShadow(CallbackInfo var1) {
      if (Optimization.method_7Д()) {
         var1.cancel();
      }
   }

   @Inject(method = "getPackedLightCoords", at = @At("RETURN"), cancellable = true)
   private void applyAdaptiveFullBright(Entity var1, float var2, CallbackInfoReturnable<Integer> var3) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         FullBright var4 = RoseClient.getInstance().getServices().getModules().getFullBright();
         if (var4 != null) {
            var3.setReturnValue(var4.е((Integer)var3.getReturnValue(), var1.getLightProbePosition(var2)));
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void updateEntityEspState(Entity var1, EntityRenderState var2, float var3, CallbackInfo var4) {
      ((EntityRenderStateIdAccess)var2).rose$setEntityId(var1.getId());
      if (var2.nameTag != null) {
         var2.nameTag = NameProtectionTextRewriter.Е(var2.nameTag);
      }

      if (var2 instanceof AvatarRenderState var5 && var5.scoreText != null) {
         var5.scoreText = NameProtectionTextRewriter.Е(var5.scoreText);
      }

      EntityRenderStateEvent var6 = new EntityRenderStateEvent(var1, var2);
      RoseClient.getInstance().getEventBus().post(var6);
   }

   @Inject(
      method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderLabelIfPresent(EntityRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.HOLOGRAMS)) {
         var5.cancel();
      }
   }
}
