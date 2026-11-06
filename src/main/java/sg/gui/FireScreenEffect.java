package sg.gui;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import sg.render.pipeline.RenderPipelineBuilder;

public class FireScreenEffect {
   private static final Function<Identifier, RenderType> е = Util.memoize(
      var0 -> RenderType.create(FireScreenEffect.Яу9, RenderSetup.builder(FireScreenEffect.сК).withTexture(FireScreenEffect.Яуо, var0).createRenderSetup())
   );
   static BlendFunction field_4 = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE);
   static RenderPipeline сК = RenderPipelines.register(
      RenderPipelineBuilder.builder(RenderPipelines.GUI_TEXTURED_SNIPPET).withLocation(FireScreenEffect.ЯуO).withDepthTest(CompareOp.ALWAYS_PASS).withDepthWrite(false).withBlend(field_4).build()
   );
   private static final String Яу9 = "fire_screen_effect";
   private static final String Яуо = "Sampler0";
   private static final String ЯуO = "pipeline/additive_particle_effect";

   public static RenderType method_5(Identifier var0) {
      return е.apply(var0);
   }
}
