package sg.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.BackpackRenderLayer;
import sg.events.impl.PlayerNameTagRenderEvent;
import sg.ec.ForcedPlayerTarget;
import sg.ec.WingsRenderLayer;
import sg.client.RoseClient;
import sg.network.NameProtectionTextRewriter;
import sg.render.HeadCosmeticsRenderLayer;
import sg.render.AttachedCosmeticsRenderLayer;
import sg.render.PlayerEmoteController;

@Mixin(AvatarRenderer.class)
public class AvatarRendererCosmeticsMixin {
   @Inject(method = "<init>", at = @At("TAIL"))
   private void addCosmeticWings(Context var1, boolean var2, CallbackInfo var3) {
      ((LivingEntityRendererInvoker)this).invokeAddFeature(new WingsRenderLayer((RenderLayerParent<AvatarRenderState, PlayerModel>)this));
      ((LivingEntityRendererInvoker)this).invokeAddFeature(new HeadCosmeticsRenderLayer((RenderLayerParent<AvatarRenderState, PlayerModel>)this));
      ((LivingEntityRendererInvoker)this).invokeAddFeature(new BackpackRenderLayer((RenderLayerParent<AvatarRenderState, PlayerModel>)this));
      ((LivingEntityRendererInvoker)this).invokeAddFeature(new AttachedCosmeticsRenderLayer((RenderLayerParent<AvatarRenderState, PlayerModel>)this));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("RETURN")
   )
   private void protectPlayerName(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      String var5 = var1 instanceof Player var6 ? var6.getGameProfile().name() : var1.getName().getString();
      if (var2.nameTag != null) {
         Minecraft var8 = Minecraft.getInstance();
         boolean var7 = var8.player != null && var1.getUUID().equals(var8.player.getUUID());
         var2.nameTag = var7 ? NameProtectionTextRewriter.必(var2.nameTag) : NameProtectionTextRewriter.Ь(var2.nameTag, var5);
      }

      if (var2.scoreText != null) {
         Minecraft var9 = Minecraft.getInstance();
         boolean var10 = var9.player != null && var1.getUUID().equals(var9.player.getUUID());
         var2.scoreText = var10 ? NameProtectionTextRewriter.必(var2.scoreText) : NameProtectionTextRewriter.Ь(var2.scoreText, var5);
      }

      if (ForcedPlayerTarget.ло_bbeebd87(var5)) {
         if (var2.nameTag != null) {
            var2.nameTag = ForcedPlayerTarget.о(var2.nameTag);
         }

         if (var2.scoreText != null) {
            var2.scoreText = ForcedPlayerTarget.о(var2.scoreText);
         }
      }

      PlayerEmoteController.衣(var2, var1.getUUID());
   }

   @Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("TAIL"))
   private void applyEmoteBodyTransform(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      PlayerEmoteController.衣(var1, var2);
   }

   @Inject(
      method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderLabel(AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      PlayerNameTagRenderEvent var6 = new PlayerNameTagRenderEvent();
      RoseClient.getInstance().getEventBus().post(var6);
      if (var6.isCancelled()) {
         var5.cancel();
      }
   }
}
