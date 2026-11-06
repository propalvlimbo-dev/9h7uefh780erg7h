package dev.rose.client.render.gfx;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.Rose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Immediate-style GUI renderer for Rose.
 *
 * <p>Vanilla's deferred GUI render state only knows its own pipelines, so every Rose surface,
 * glass panel and glyph is recorded here instead and drawn in one short list of render passes
 * at the very end of {@code GuiRenderer#render} (see {@code GuiRendererMixin}). That gives us
 * our own shaders, our own textures and exact control over the draw order.
 */
public final class RoseGfx {
	/** GUI view matrix: vanilla renders the GUI at z ≈ -11000 in its own ortho projection. */
	private static final Matrix4f GUI_VIEW = new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F);
	private static final Vector4f WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
	private static final Vector3f ZERO = new Vector3f();
	private static final Matrix4f IDENTITY = new Matrix4f();

	private static GpuSampler linearSampler;

	/** A shader + the vertex layout it expects. */
	public record Pipe(RenderPipeline pipeline, VertexFormat format, VertexFormat.Mode mode) {
	}

	public static final Pipe SHAPE = build("shape", false);
	public static final Pipe TEXT = build("text", true);
	public static final Pipe IMAGE = build("image", true);

	private static final List<Chunk> chunks = new ArrayList<>();
	private static Pipe active;
	private static AbstractTexture activeTexture;
	private static BufferBuilder builder;

	private RoseGfx() {
	}

	private static Pipe build(String name, boolean sampler) {
		RenderPipeline.Builder builder = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(Rose.id("pipeline/ui_" + name))
			.withVertexShader(Rose.id("core/rose_" + name))
			.withFragmentShader(Rose.id("core/rose_" + name))
			.withVertexFormat(sampler ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR,
				VertexFormat.Mode.QUADS)
			.withBlend(BlendFunction.TRANSLUCENT)
			.withCull(false)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST);

		if (sampler) {
			builder = builder.withSampler("Sampler0");
		}

		return new Pipe(RenderPipelines.register(builder.build()),
			sampler ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.QUADS);
	}

	/** Forces class initialisation so the pipelines are registered during client startup. */
	public static void init() {
	}

	// ------------------------------------------------------------------ batching

	public static BufferBuilder begin(Pipe pipe, @Nullable AbstractTexture texture) {
		if (builder != null && active == pipe && activeTexture == texture) {
			return builder;
		}

		close();

		active = pipe;
		activeTexture = texture;
		builder = Tesselator.getInstance().begin(pipe.mode(), pipe.format());
		return builder;
	}

	public static void close() {
		if (builder == null) {
			return;
		}

		MeshData mesh = builder.build();
		Pipe pipe = active;
		AbstractTexture texture = pipe == SHAPE ? null : activeTexture;

		builder = null;
		active = null;
		activeTexture = null;

		if (mesh != null) {
			chunks.add(new Chunk(pipe, texture, mesh));

			// hard safety net: a frame that is never flushed must not grow without bound
			if (chunks.size() > 8192) {
				flush();
			}
		}
	}

	// ------------------------------------------------------------------- shapes

	/** 2D GUI pose (as used by {@code GuiGraphics}) to a 4x4 matrix. */
	public static Matrix4f pose(Matrix3x2fc m) {
		return new Matrix4f(
			m.m00(), m.m01(), 0.0F, 0.0F,
			m.m10(), m.m11(), 0.0F, 0.0F,
			0.0F, 0.0F, 1.0F, 0.0F,
			m.m20(), m.m21(), 0.0F, 1.0F
		);
	}

	public static void shape(Matrix4f pose, float[] xy, int[] colors) {
		BufferBuilder b = begin(SHAPE, null);

		for (int i = 0; i < 4; i++) {
			b.addVertex(pose, xy[i * 2], xy[i * 2 + 1], 0.0F).setColor(colors[i]);
		}
	}

	public static void textured(Pipe pipe, Matrix4f pose, AbstractTexture texture, float[] xy, float[] uv, int[] colors) {
		BufferBuilder b = begin(pipe, texture);

		for (int i = 0; i < 4; i++) {
			b.addVertex(pose, xy[i * 2], xy[i * 2 + 1], 0.0F)
				.setUv(uv[i * 2], uv[i * 2 + 1])
				.setColor(colors[i]);
		}
	}

	// -------------------------------------------------------------------- flush

	/** Draws everything that was recorded since the last flush. Called once per frame. */
	public static void flush() {
		close();

		if (chunks.isEmpty()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		RenderTarget target = mc.getMainRenderTarget();
		GpuTextureView color = RenderSystem.outputColorTextureOverride != null
			? RenderSystem.outputColorTextureOverride
			: target.getColorTextureView();

		if (color == null) {
			return;
		}

		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(GUI_VIEW, WHITE, ZERO, IDENTITY);

		for (Chunk chunk : chunks) {
			try {
				draw(chunk, color, transforms);
			} catch (Throwable t) {
				Rose.LOG.error("[rose] GUI draw failed", t);
			} finally {
				chunk.mesh.close();
			}
		}

		chunks.clear();
	}

	private static void draw(Chunk chunk, GpuTextureView color, GpuBufferSlice transforms) {
		MeshData.DrawState state = chunk.mesh.drawState();
		GpuBuffer vertices = state.format().uploadImmediateVertexBuffer(chunk.mesh.vertexBuffer());
		RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(state.mode());
		GpuBuffer indices = sequential.getBuffer(state.indexCount());

		RenderPass pass = RenderSystem.getDevice()
			.createCommandEncoder()
			.createRenderPass(() -> "rose gui", color, OptionalInt.empty());

		try {
			pass.setPipeline(chunk.pipe.pipeline());
			RenderSystem.bindDefaultUniforms(pass);
			pass.setUniform("DynamicTransforms", transforms);

			if (chunk.texture != null) {
				GpuTextureView view = chunk.texture.getTextureView();

				if (view != null) {
					pass.bindTexture("Sampler0", view, sampler());
				}
			}

			pass.setVertexBuffer(0, vertices);
			pass.setIndexBuffer(indices, sequential.type());
			pass.drawIndexed(0, 0, state.indexCount(), 1);
		} finally {
			pass.close();
		}
	}

	private static GpuSampler sampler() {
		if (linearSampler == null) {
			linearSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
		}

		return linearSampler;
	}

	private record Chunk(Pipe pipe, @Nullable AbstractTexture texture, MeshData mesh) {
	}
}
