package sg.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
   @Accessor("DEBUG_FILLED_SNIPPET")
   static Snippet getPositionColorSnippet() {
      throw new AssertionError();
   }

   @Accessor("OUTLINE_SNIPPET")
   static Snippet getOutlineSnippet() {
      throw new AssertionError();
   }

   @Invoker("register")
   static RenderPipeline register(RenderPipeline var0) {
      throw new AssertionError();
   }
}
