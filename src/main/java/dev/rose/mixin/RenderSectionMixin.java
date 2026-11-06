package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.compat.Compat;
import dev.rose.module.Modules;
import dev.rose.module.impl.world.ChunkFadeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionMixin {
   @Shadow
   public abstract long getSectionNode();

   @ModifyReturnValue(method = "getVisibility", at = @At("RETURN"))
   private float rose$visibility(float vanilla) {
      if (vanilla >= 1.0F) {
         return vanilla;
      } else {
         ChunkFadeModule fade = Modules.chunkFade();
         if (fade != null && fade.active() && !Compat.shaderPackActive()) {
            Vec3 eye = Minecraft.getInstance().gameRenderer.getMainCamera().position();
            long node = this.getSectionNode();
            double dx = SectionPos.sectionToBlockCoord(SectionPos.x(node), 8) - eye.x;
            double dy = SectionPos.sectionToBlockCoord(SectionPos.y(node), 8) - eye.y;
            double dz = SectionPos.sectionToBlockCoord(SectionPos.z(node), 8) - eye.z;
            return fade.visibility(vanilla, dx * dx + dy * dy + dz * dz);
         } else {
            return vanilla;
         }
      }
   }
}
