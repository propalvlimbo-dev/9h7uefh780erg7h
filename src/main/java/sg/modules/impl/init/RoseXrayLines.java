package sg.modules.impl.init;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import sg.mixin.RenderTypeAccessor;
import sg.mixin.RenderPipelinesAccessor;
import sg.render.pipeline.RenderPipelineBuilder;

public final class RoseXrayLines {
   public static final RenderPipeline LINES_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelinesAccessor.getPositionColorSnippet())
         .withLocation(Identifier.fromNamespaceAndPath(RoseXrayLines.LINES_NAMESPACE, RoseXrayLines.LINES_PIPELINE_PATH))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.DEBUG_LINES)
         .build()
   );
   public static final RenderType LINES = RenderTypeAccessor.create(RoseXrayLines.LINES_RENDER_TYPE_NAME, RenderSetup.builder(LINES_PIPELINE).createRenderSetup());
   public static final RenderPipeline QUADS_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelinesAccessor.getPositionColorSnippet())
         .withLocation(Identifier.fromNamespaceAndPath(RoseXrayLines.QUADS_NAMESPACE, RoseXrayLines.QUADS_PIPELINE_PATH))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.QUADS)
         .build()
   );
   public static final RenderType QUADS = RenderTypeAccessor.create(RoseXrayLines.QUADS_RENDER_TYPE_NAME, RenderSetup.builder(QUADS_PIPELINE).createRenderSetup());
   private static final String LINES_NAMESPACE = "rose";
   private static final String LINES_PIPELINE_PATH = "pipeline/xray_lines";
   private static final String LINES_RENDER_TYPE_NAME = "rose_xray_lines";
   private static final String QUADS_NAMESPACE = "rose";
   private static final String QUADS_PIPELINE_PATH = "pipeline/xray_quads";
   private static final String QUADS_RENDER_TYPE_NAME = "rose_xray_quads";

   private RoseXrayLines() {
   }
}
