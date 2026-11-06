package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererSuppressionMixin {
   @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
   private static void submitFireOverlay(PoseStack var0, SubmitNodeCollector var1, TextureAtlasSprite var2, CallbackInfo var3) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.FIRE)) {
         var3.cancel();
      }
   }

   @Inject(method = "submitBlockSprite", at = @At("HEAD"), cancellable = true)
   private static void renderInWallOverlay(TextureAtlasSprite var0, PoseStack var1, SubmitNodeCollector var2, int var3, CallbackInfo var4) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.CAMERA_CLIP)) {
         var4.cancel();
      }
   }

   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void renderUnderwaterOverlay(Minecraft var0, PoseStack var1, SubmitNodeCollector var2, CallbackInfo var3) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.UNDERWATER_BLUR)) {
         var3.cancel();
      }
   }
}
