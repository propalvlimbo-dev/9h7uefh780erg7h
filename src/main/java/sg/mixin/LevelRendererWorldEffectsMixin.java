package sg.mixin;

import sg.mixinbridge.WorldEffectsRendererAccess;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.CustomCrystal;
import sg.modules.impl.render.EntityESP;
import sg.modules.impl.render.ImpactNova;
import sg.client.RoseClient;
import sg.modules.impl.misc.Optimization;
import sg.render.pipeline.EntityEspSurface;

@Mixin(LevelRenderer.class)
public class LevelRendererWorldEffectsMixin {
   @Shadow
   private RenderTarget entityOutlineTarget;
   @Unique
   private TextureTarget gizmoDepthBackup;
   @Unique
   private boolean restoreGizmoDepth;
   private static final String ВуД = "rose-gizmo-depth";

   @ModifyArg(
      method = "invalidateCompiledGeometry",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ViewArea;<init>(Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher;IIIIILnet/minecraft/client/renderer/SectionOcclusionGraph;)V"
      ),
      index = 5,
      require = 0
   )
   private int capBuiltChunkStorageDistance(int var1) {
      return Optimization.Ж(var1);
   }

   @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true, require = 0)
   private void skipClouds(CallbackInfo var1) {
      if (Optimization.method_76()) {
         var1.cancel();
      }
   }

   @Inject(
      method = "lambda$addMainPass$0",
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;TRANSLUCENT:Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;"
      )
   )
   private void renderWorldEffectsBeforeWater(CallbackInfo var1) {
      ((WorldEffectsRendererAccess)Minecraft.getInstance().gameRenderer).rose$renderWorldEffects();
   }

   @ModifyExpressionValue(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ShaderManager;getPostChain(Lnet/minecraft/resources/Identifier;Ljava/util/Set;)Lnet/minecraft/client/renderer/PostChain;"
      )
   )
   private PostChain keepRawEntityEspMask(PostChain var1) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         EntityESP var2 = RoseClient.getInstance().getServices().getModules().getEntityESP();
         EntityEspSurface var3 = var2 != null ? var2.getSurfaceRenderer() : null;
         ImpactNova var4 = RoseClient.getInstance().getServices().getModules().getImpactNova();
         CustomCrystal var5 = RoseClient.getInstance().getServices().getModules().getCustomCrystal();
         boolean var6 = var2 != null && var2.isEnabled() && var3 != null && var3.shouldUseCustomOutline();
         boolean var7 = var4 != null && var4.фо();
         boolean var8 = var5 != null && var5.isEnabled();
         return !var6 && !var7 && !var8 ? var1 : null;
      } else {
         return var1;
      }
   }

   @Inject(method = "doEntityOutline", at = @At("HEAD"), cancellable = true)
   private void drawEntityEspEffects(CallbackInfo var1) {
      if (RoseClient.getInstance() != null
         && RoseClient.getInstance().getServices() != null
         && RoseClient.getInstance().getServices().getModules() != null
         && this.entityOutlineTarget != null) {
         EntityESP var2 = RoseClient.getInstance().getServices().getModules().getEntityESP();
         EntityEspSurface var3 = var2 != null ? var2.getSurfaceRenderer() : null;
         ImpactNova var4 = RoseClient.getInstance().getServices().getModules().getImpactNova();
         CustomCrystal var5 = RoseClient.getInstance().getServices().getModules().getCustomCrystal();
         boolean var6 = var2 != null && var2.isEnabled() && var3 != null && var3.shouldUseCustomOutline();
         boolean var7 = var6 && var3.hasRenderedEntities();
         boolean var8 = var4 != null && var4.фо();
         boolean var9 = var5 != null && var5.isEnabled() && var5.З2();
         if (var7 || var8 || var9) {
            RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (var10 != null && var10.getColorTextureView() != null && this.entityOutlineTarget.getColorTextureView() != null) {
               var1.cancel();
               if (var7) {
                  var3.compositeWorldEffects(this.entityOutlineTarget, var10);
               }

               if (var8) {
                  var4.Ш(this.entityOutlineTarget, var10);
               }

               if (var9) {
                  var5.л(this.entityOutlineTarget, var10);
               }
            }
         }
      }
   }

   @WrapOperation(
      method = "lambda$addAlwaysOnTopPass$0",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
   )
   private void saveWorldDepthBeforeGizmoClear(CommandEncoder var1, GpuTexture var2, double var3, Operation<Void> var5) {
      RenderTarget var6 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      if (var6 != null && var6.getDepthTexture() == var2 && !var2.isClosed()) {
         this.ensureGizmoDepthBackup(var6);
         if (this.gizmoDepthBackup != null && this.gizmoDepthBackup.getDepthTexture() != null && !this.gizmoDepthBackup.getDepthTexture().isClosed()) {
            this.gizmoDepthBackup.copyDepthFrom(var6);
            this.restoreGizmoDepth = true;
         }
      }

      var5.call(var1, var2, var3);
   }

   @Inject(method = "lambda$addAlwaysOnTopPass$0", at = @At("RETURN"))
   private void restoreWorldDepthAfterGizmos(CallbackInfo var1) {
      if (this.restoreGizmoDepth && this.gizmoDepthBackup != null) {
         this.restoreGizmoDepth = false;
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var2 != null && var2.getDepthTexture() != null && !var2.getDepthTexture().isClosed()) {
            var2.copyDepthFrom(this.gizmoDepthBackup);
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void releaseGizmoDepthBackup(CallbackInfo var1) {
      this.restoreGizmoDepth = false;
      if (this.gizmoDepthBackup != null) {
         this.gizmoDepthBackup.destroyBuffers();
         this.gizmoDepthBackup = null;
      }
   }

   @Unique
   private void ensureGizmoDepthBackup(RenderTarget var1) {
      int var2 = Math.max(1, var1.width);
      int var3 = Math.max(1, var1.height);
      if (this.gizmoDepthBackup == null) {
         this.gizmoDepthBackup = new TextureTarget(ВуД, var2, var3, true, GpuFormat.RGBA8_UNORM);
      } else {
         if (this.gizmoDepthBackup.width != var2 || this.gizmoDepthBackup.height != var3) {
            this.gizmoDepthBackup.resize(var2, var3);
         }
      }
   }
}
