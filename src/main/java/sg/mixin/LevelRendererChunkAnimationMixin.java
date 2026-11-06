package sg.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sg.annotation.RequiresMods;
import sg.modules.impl.render.BetterMinecraft;
import sg.client.RoseClient;

@RequiresMods("!sodium")
@Mixin(LevelRenderer.class)
public class LevelRendererChunkAnimationMixin {
   private static final double НП = 1.0E-12;

   @ModifyArgs(
      method = "prepareChunkRenders",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;<init>(Lorg/joml/Matrix4fc;IIIFII)V")
   )
   private void animateSection(Args var1, @Local RenderSection var2) {
      BetterMinecraft var3 = module();
      if (var3 != null) {
         Vec3 var4 = var3.chunkRevealAnimation.Б(var2, var3);
         if (var4.lengthSqr() > НП) {
            Matrix4fc var5 = var1.get(0);
            var1.set(0, new Matrix4f(var5).translate((float)var4.x, (float)var4.y, (float)var4.z));
         }
      }
   }

   @Unique
   private static BetterMinecraft module() {
      RoseClient var0 = RoseClient.getInstance();
      return var0 != null && var0.getServices() != null && var0.getServices().getModules() != null ? var0.getServices().getModules().getBetterMinecraft() : null;
   }
}
