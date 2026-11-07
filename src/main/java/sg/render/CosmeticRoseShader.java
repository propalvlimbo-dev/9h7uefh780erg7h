package sg.render;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import settings.render.ClientResourceIds;
import sg.mixin.RenderPipelinesAccessor;
import sg.mixin.RenderTypeAccessor;
import sg.render.pipeline.RenderPipelineBuilder;

/**
 * Шейдерный рендер косметики «Роза»: цвет материала берётся из палитровых блоков UV,
 * поверх процедурно рисуются прожилки, блик по Френелю, бегущая волна и искры.
 * Время анимации приходит из uniform-блока Globals (GameTime), как в шейдерах ChinaHat.
 */
public final class CosmeticRoseShader {
   private static final String NAMESPACE = "rose";
   private static final String PIPELINE_PATH = "pipeline/cosmetic_rose";
   private static final String RENDER_TYPE_NAME = "rose_cosmetic";
   private static final String VERTEX_SHADER = "rose_cosmetic";
   private static final String FRAGMENT_SHADER = "rose_cosmetic";

   private static final RenderPipeline PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
         .withLocation(Identifier.fromNamespaceAndPath(NAMESPACE, PIPELINE_PATH))
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withVertexFormat(DefaultVertexFormat.ENTITY, PrimitiveTopology.QUADS)
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build()
   );

   public static final RenderType RENDER_TYPE = RenderTypeAccessor.create(RENDER_TYPE_NAME, RenderSetup.builder(PIPELINE).createRenderSetup());

   private CosmeticRoseShader() {
   }
}
