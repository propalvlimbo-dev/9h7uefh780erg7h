package dev.rose.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.Rose;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

/** Custom render pipelines and render types used by Rose. */
public final class RoseRenderTypes {

	/** Procedural ruby clouds (world space, no textures, all shape comes from the fragment shader). */
	public static final RenderPipeline RUBY_CLOUDS_PIPELINE = RenderPipelines.register(
		RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
			.withLocation(Rose.id("pipeline/ruby_clouds"))
			.withVertexShader(Rose.id("core/ruby_clouds"))
			.withFragmentShader(Rose.id("core/ruby_clouds"))
			.withBlend(BlendFunction.TRANSLUCENT)
			.withCull(false)
			.withDepthWrite(false)
			.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
			.build()
	);

	/**
	 * Drawn straight into the main level framebuffer (no {@code setOutputTarget} on purpose:
	 * the default target is the main one, and pointing at ITEM_ENTITY_TARGET made the clouds vanish).
	 */
	public static final RenderType RUBY_CLOUDS = RenderType.create(
		"rose:ruby_clouds",
		RenderSetup.builder(RUBY_CLOUDS_PIPELINE)
			.createRenderSetup()
	);

	private RoseRenderTypes() {
	}

	/** Forces class initialisation so pipelines get registered during client startup. */
	public static void init() {
	}
}
