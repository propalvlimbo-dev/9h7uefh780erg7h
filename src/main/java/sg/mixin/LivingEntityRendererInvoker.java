package sg.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererInvoker {
   @Invoker("addLayer")
   boolean invokeAddFeature(RenderLayer<?, ?> var1);

   @Invoker("setupRotations")
   void invokeSetupTransforms(LivingEntityRenderState var1, PoseStack var2, float var3, float var4);

   @Invoker("scale")
   void invokeScale(LivingEntityRenderState var1, PoseStack var2);
}
