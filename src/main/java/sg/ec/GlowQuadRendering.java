package sg.ec;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.render.pipeline.RenderPipelineBuilder;

public final class GlowQuadRendering {
   private static final String ВO3 = "dead_chams";
   private static final String ВOр = "private_glow";
   private static final String ВO6 = "Sampler0";

   private GlowQuadRendering() {
   }

   public static void Х(BufferBuilder var0, Matrix4f var1, float var2, int var3) {
      float var4 = var2 / 2.0F;
      var0.addVertex(var1, -var4, var4, 0.0F).setUv(0.0F, 1.0F).setColor(var3);
      var0.addVertex(var1, var4, var4, 0.0F).setUv(1.0F, 1.0F).setColor(var3);
      var0.addVertex(var1, var4, -var4, 0.0F).setUv(1.0F, 0.0F).setColor(var3);
      var0.addVertex(var1, -var4, -var4, 0.0F).setUv(0.0F, 0.0F).setColor(var3);
   }

   public static RenderPipeline Г(String var0, CompareOp var1) {
      return RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(var0)
         .withVertexShader(ClientResourceIds.getCoreId(ВO3))
         .withFragmentShader(ClientResourceIds.getCoreId(ВOр))
         .withDepthTest(var1)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.OVERLAY)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ВO6)
         .build();
   }
}
