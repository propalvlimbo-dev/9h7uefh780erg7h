package sg.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.render.pipeline.PreviewFade;
import sg.gui.cosmetics.CosmeticsPanel;

@Mixin(PictureInPictureRenderer.class)
public abstract class PictureInPictureRendererEntityEspMixin {
   @Shadow
   private GpuTexture texture;
   @Shadow
   private GpuTextureView textureView;
   @Shadow
   private GpuTextureView depthTextureView;
   @Shadow
   @Final
   private Projection projection;
   @Unique
   private GpuTextureView originalTextureView;
   @Unique
   private int entityEspPreviewColor = -1;
   private static final long Я弟5 = 2147483647L;
   private static final long Я弟в = 2147483647L;
   private static final float Я弟ъ = 255.0F;
   private static final float Я弟М = 0.999F;

   @Inject(method = "prepare", at = @At("HEAD"), cancellable = true)
   private void skipInvalidPreview(PictureInPictureRenderState var1, GuiRenderState var2, FeatureRenderDispatcher var3, int var4, CallbackInfo var5) {
      this.entityEspPreviewColor = -1;
      Window var6 = Minecraft.getInstance().getWindow();
      long var7 = (long)var1.x1() - var1.x0();
      long var9 = (long)var1.y1() - var1.y0();
      if (var6.isIconified()
         || var6.getScreenWidth() <= 0
         || var6.getScreenHeight() <= 0
         || var4 <= 0
         || var7 <= 0L
         || var9 <= 0L
         || var7 * var4 > Я弟5
         || var9 * var4 > Я弟в
         || !Float.isFinite(var1.scale())
         || var1.scale() <= 0.0F) {
         if (var1 instanceof GuiEntityRenderState var11) {
            PreviewFade.discardPreview(var11.renderState());
            CosmeticsPanel.ЬО();
         }

         var5.cancel();
      }
   }

   @Inject(
      method = "prepare",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderAllFeatures(Lnet/minecraft/client/renderer/SubmitNodeStorage;)V",
         shift = Shift.AFTER
      )
   )
   private void renderEntityEspPreview(PictureInPictureRenderState var1, GuiRenderState var2, FeatureRenderDispatcher var3, int var4, CallbackInfo var5) {
      if (var1 instanceof GuiEntityRenderState var6) {
         float var7 = PreviewFade.getPreviewAlpha(var6.renderState());
         if (var7 >= 0.0F) {
            int var8 = Math.clamp(Math.round(var7 * Я弟ъ), 0, 255);
            this.entityEspPreviewColor = var8 << 24 | var8 << 16 | var8 << 8 | var8;
         } else {
            this.entityEspPreviewColor = -1;
         }

         GpuTextureView var11 = var7 >= 0.0F
            ? PreviewFade.renderRegisteredPreview(var6, this.texture, this.textureView, this.depthTextureView, this.projection.getMatrix(new Matrix4f()), var4)
            : null;
         if (var11 != null) {
            this.originalTextureView = this.textureView;
            this.textureView = var11;
         }

         float var9 = CosmeticsPanel.ЬО();
         if (var9 >= 0.0F && var9 < Я弟М) {
            if (this.originalTextureView == null) {
               this.originalTextureView = this.textureView;
            }

            GpuTextureView var10 = PreviewFade.fadeTexture(this.texture, this.textureView, var9);
            if (var10 != null) {
               this.textureView = var10;
            }
         }
      }
   }

   @ModifyArg(
      method = "blitTexture",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/BlitRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lorg/joml/Matrix3x2fc;IIIIFFFFILnet/minecraft/client/gui/navigation/ScreenRectangle;Lnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
      ),
      index = 11
   )
   private int applyEntityEspPreviewAlphaToModel(int var1) {
      return this.entityEspPreviewColor;
   }

   @Inject(
      method = "prepare",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/render/pip/PictureInPictureRenderer;blitTexture(Lnet/minecraft/client/renderer/state/gui/pip/PictureInPictureRenderState;Lnet/minecraft/client/renderer/state/gui/GuiRenderState;)V",
         ordinal = 1,
         shift = Shift.AFTER
      )
   )
   private void restoreEntityPreviewTexture(PictureInPictureRenderState var1, GuiRenderState var2, FeatureRenderDispatcher var3, int var4, CallbackInfo var5) {
      if (this.originalTextureView != null) {
         this.textureView = this.originalTextureView;
         this.originalTextureView = null;
      }

      this.entityEspPreviewColor = -1;
   }
}
