package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.EntityRenderStateEvent;
import sg.mixin.RenderPipelinesAccessor;
import sg.render.WorldProjection;
import sg.settings.EntityEspStyleSettings;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public final class EntityEspSurface implements MinecraftAccess, AutoCloseable {
   private static final double с弟 = 22500.0;
   private static final int Вя = 48;
   private static final int Яс = 144;
   private static final int ЯВ = 6;
   private static final int ЯЯ = 96;
   private static final int ЯХ = 16;
   private static final int Яе = 80;
   private static final int ЯА = 64;
   private static final int Я4 = 1056;
   private static final int Яп = 1088;
   private static final float Я0 = 1.5F;
   private static final RenderPipeline CATEGORY_MASK_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.CATEGORY_MASK_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.CATEGORY_MASK_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.CATEGORY_MASK_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.CATEGORY_MASK_PIPELINE_CATEGORY_MASK_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.CATEGORY_MASK_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline MODEL_MASK_COPY_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.MODEL_MASK_COPY_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_MASK_COPY_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_MASK_COPY_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withSampler(EntityEspSurface.MODEL_MASK_COPY_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline PREVIEW_MASK_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.PREVIEW_MASK_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_MASK_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_MASK_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.PREVIEW_MASK_PIPELINE_PREVIEW_MASK_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.PREVIEW_MASK_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline SCENE_BLUR_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.SCENE_BLUR_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.SCENE_BLUR_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.SCENE_BLUR_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.SCENE_BLUR_PIPELINE_ESP_BLUR_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.SCENE_BLUR_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline WORLD_GLOW_COMPOSITE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.WORLD_GLOW_COMPOSITE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline GLOW_SEED_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.GLOW_SEED_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_SEED_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_SEED_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withColorTargetFormat(GpuFormat.RGBA16_FLOAT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.GLOW_SEED_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.GLOW_SEED_PIPELINE_MASK_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.GLOW_SEED_PIPELINE_SURFACE_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.GLOW_SEED_PIPELINE_RADIUS_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline GLOW_ACCUMULATE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.GLOW_ACCUMULATE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_ACCUMULATE_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_ACCUMULATE_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(new BlendFunction(BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ONE))
      .withColorTargetFormat(GpuFormat.RGBA16_FLOAT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withSampler(EntityEspSurface.GLOW_ACCUMULATE_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline PREVIEW_GLOW_COMPOSITE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(new BlendFunction(BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ONE))
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.PREVIEW_GLOW_COMPOSITE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline GLOW_BLUR_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.GLOW_BLUR_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_BLUR_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_BLUR_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withColorTargetFormat(GpuFormat.RGBA16_FLOAT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.GLOW_BLUR_PIPELINE_GLOW_BLUR_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.GLOW_BLUR_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline GLOW_DOWNSAMPLE_PIPELINE = createPyramidPipeline(EntityEspSurface.GLOW_DOWNSAMPLE_SHADER);
   private static final RenderPipeline GLOW_UPSAMPLE_PIPELINE = createPyramidPipeline(EntityEspSurface.GLOW_UPSAMPLE_SHADER);
   private static final RenderPipeline PREVIEW_SURFACE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_PREVIEW_SURFACE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_DEPTH_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.PREVIEW_SURFACE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME)
      .build();
   private static final RenderPipeline GLOW_SURFACE_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelinesAccessor.getOutlineSnippet())
         .withLocation(EntityEspSurface.GLOW_SURFACE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_SURFACE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.GLOW_SURFACE_PIPELINE_FRAGMENT_SHADER))
         .withUniform(EntityEspSurface.GLOW_SURFACE_PIPELINE_ESP_GLOW_SURFACE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(true)
         .withCull(false)
         .withoutBlend()
         .build()
   );
   private static final RenderPipeline MODEL_OUTLINE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.MODEL_OUTLINE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_OUTLINE_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_OUTLINE_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withColorWrite(true, true)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.MODEL_OUTLINE_PIPELINE_ESP_GLOW_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.MODEL_OUTLINE_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
      .withBlend(BlendFunction.TRANSLUCENT)
      .build();
   private static final RenderPipeline MODEL_FILL_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(EntityEspSurface.MODEL_FILL_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_FILL_PIPELINE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_FILL_PIPELINE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspSurface.MODEL_FILL_PIPELINE_ESP_CHAMS_FILL_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspSurface.MODEL_FILL_PIPELINE_MASK_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.MODEL_FILL_PIPELINE_SURFACE_SAMPLER_SAMPLER_NAME)
      .withSampler(EntityEspSurface.MODEL_FILL_PIPELINE_SCENE_SAMPLER_SAMPLER_NAME)
      .build();
   @SerializedName("Я")
   private final Function<Entity, EntityEspStyleSettings> styleResolver;
   @SerializedName("Я")
   private final Supplier<? extends Collection<EntityEspStyleSettings>> stylesSupplier;
   @SerializedName("9")
   private final List<EntityEspStyleSettings> styles;
   @SerializedName("В")
   private final boolean[] activeCategories;
   private final @com.google.gson.annotations.SerializedName("п")  int[] activeDistanceBuckets;
   @SerializedName("В")
   private final float[][][] categoryBucketBounds;
   @SerializedName("Ю")
   private Matrix4f viewRotationProjectionMatrix;
   private @com.google.gson.annotations.SerializedName("Ях")  float distanceBucketProjectionScale;
   @SerializedName("А")
   private final Vector4f clipPositionScratch = new Vector4f();
   @SerializedName("Ю")
   private TextureTarget glowAccumulatorTarget;
   private @com.google.gson.annotations.SerializedName("Ч")  TextureTarget categoryMaskTarget;
   private @com.google.gson.annotations.SerializedName("ж")  GpuBuffer categoryMaskUniforms;
   private @com.google.gson.annotations.SerializedName("Л")  TextureTarget sceneBlurHorizontalTarget;
   private @com.google.gson.annotations.SerializedName("诶")  TextureTarget sceneBlurVerticalTarget;
   @SerializedName("В")
   private final TextureTarget[][] glowPyramidTargets = new TextureTarget[2][3];
   @SerializedName("Я")
   private final TextureTarget[][] glowBlurScratchTargets = new TextureTarget[2][3];
   private @com.google.gson.annotations.SerializedName("弟")  GpuBuffer glowHorizontalUniforms;
   private @com.google.gson.annotations.SerializedName("б")  GpuBuffer glowVerticalUniforms;
   private @com.google.gson.annotations.SerializedName("ЯД")  int cachedGlowBlurWidth = -1;
   private @com.google.gson.annotations.SerializedName("ЯЪ")  int cachedGlowBlurHeight = -1;
   private @com.google.gson.annotations.SerializedName("ЯУ")  float cachedGlowBlurSigma = Float.NaN;
   @SerializedName("1")
   private TextureTarget previewMaskTarget;
   private @com.google.gson.annotations.SerializedName("Ь")  TextureTarget previewOutputTarget;
   private @com.google.gson.annotations.SerializedName("衣")  TextureTarget previewSurfaceTarget;
   private @com.google.gson.annotations.SerializedName("Ы")  GpuBuffer previewSurfaceUniforms;
   private @com.google.gson.annotations.SerializedName("сы")  boolean previewSurfaceReady;
   private @com.google.gson.annotations.SerializedName("l")  GpuBuffer sceneBlurHorizontalUniforms;
   @SerializedName("8")
   private GpuBuffer sceneBlurVerticalUniforms;
   @SerializedName("5")
   private GpuBuffer glowUniforms;
   private @com.google.gson.annotations.SerializedName("в")  GpuBuffer outlineUniforms;
   private @com.google.gson.annotations.SerializedName("ъ")  GpuBuffer fillUniforms;
   private @com.google.gson.annotations.SerializedName("ш")  TextureTarget modelSurfaceTarget;
   private @com.google.gson.annotations.SerializedName("сй")  boolean modelSurfaceReady;
   @SerializedName("В")
   private final double[][] worldAnchors = new double[64][4];
   private @com.google.gson.annotations.SerializedName("Ят")  int anchorCount;
   private @com.google.gson.annotations.SerializedName("М")  GpuBuffer surfaceAnchorUniforms;
   private @com.google.gson.annotations.SerializedName("И")  TextureTarget glowSurfaceTarget;
   private @com.google.gson.annotations.SerializedName("Н")  GpuBuffer glowSurfaceUniforms;
   private @com.google.gson.annotations.SerializedName("сЖ")  boolean glowSurfaceReady;
   private @com.google.gson.annotations.SerializedName("см")  boolean needsGlowSurfaceThisFrame;
   private static final RenderPipeline MODEL_SURFACE_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelinesAccessor.getOutlineSnippet())
         .withLocation(EntityEspSurface.MODEL_SURFACE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_SURFACE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(EntityEspSurface.MODEL_SURFACE_PIPELINE_FRAGMENT_SHADER))
         .withUniform(EntityEspSurface.MODEL_SURFACE_PIPELINE_ESP_SURFACE_ANCHORS_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(true)
         .withCull(false)
         .withoutBlend()
         .build()
   );
   private @com.google.gson.annotations.SerializedName("сК")  boolean frameInitialized;
   private @com.google.gson.annotations.SerializedName("сI")  boolean hasRenderedEntitiesThisFrame;
   private @com.google.gson.annotations.SerializedName("сТ")  boolean hasFillThisFrame;
   private final @com.google.gson.annotations.SerializedName("с9")  long animationStartNanos = System.nanoTime();
   @SerializedName("В")
   private final ByteBuffer cachedGlowUniformData = ByteBuffer.allocate(144).limit(0);
   private @com.google.gson.annotations.SerializedName("ЯЗ")  float firstFillOpacity;
   private @com.google.gson.annotations.SerializedName("Яа")  int firstFillColor;
   private @com.google.gson.annotations.SerializedName("ЯЮ")  int firstFillMode;
   private @com.google.gson.annotations.SerializedName("ЯЧ")  int cachedSceneBlurWidth = -1;
   private @com.google.gson.annotations.SerializedName("ЯЛ")  int cachedSceneBlurHeight = -1;
   private @com.google.gson.annotations.SerializedName("ЯЕ")  float cachedSceneBlurRadius = Float.NaN;
   private @com.google.gson.annotations.SerializedName("Я诶")  int cachedOutlineTargetWidth = -1;
   private @com.google.gson.annotations.SerializedName("Я1")  int cachedOutlineTargetHeight = -1;
   private @com.google.gson.annotations.SerializedName("ЯП")  float cachedOutlineWidth = Float.NaN;
   private static final String CREATE_PYRAMID_PIPELINE_HANDS_FULLSCREEN_VERTEX_SHADER = "hands_fullscreen";
   private static final String CREATE_PYRAMID_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String CAPTURE_ENTITY_SURFACES_ENTITY_ESP_SURFACE_TARGET_LABEL = "entityEspSurface";
   private static final String CAPTURE_ENTITY_SURFACES_DYNAMIC_TRANSFORMS_UNIFORM_NAME = "DynamicTransforms";
   private static final String CAPTURE_ENTITY_SURFACES_ESP_SURFACE_ANCHORS_UNIFORM_NAME = "EspSurfaceAnchors";
   private static final String CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_TARGET_LABEL = "entityEspGlowSurface";
   private static final String CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_BUFFER_LABEL = "entityEspGlowSurface";
   private static final String CAPTURE_GLOW_SURFACE_DYNAMIC_TRANSFORMS_UNIFORM_NAME = "DynamicTransforms";
   private static final String CAPTURE_GLOW_SURFACE_ESP_GLOW_SURFACE_UNIFORM_NAME = "EspGlowSurface";
   private static final String UPDATE_SURFACE_ANCHOR_UNIFORMS_ENTITY_ESP_SURFACE_ANCHORS_BUFFER_LABEL = "entityEspSurfaceAnchors";
   private static final double ВНЦ = 0.5;
   private static final double ВНч = 0.5;
   private static final int ВНЭ = -16777216;
   private static final String RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_TARGET_LABEL = "entity-esp-preview-surface";
   private static final String RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_BUFFER_LABEL = "entityEspPreviewSurface";
   private static final String RENDER_PREVIEW_SURFACE_PREVIEW_SURFACE_UNIFORM_NAME = "PreviewSurface";
   private static final String RENDER_PREVIEW_SURFACE_DEPTH_SAMPLER_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_PREVIEW_SURFACE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String COPY_PREVIEW_TEXTURE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final float ВНЩ = 255.0F;
   private static final float ВНу = 255.0F;
   private static final float ВН2 = 255.0F;
   private static final float ВНф = 255.0F;
   private static final String RECOLOR_PREVIEW_MASK_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String RECOLOR_PREVIEW_MASK_PREVIEW_MASK_UNIFORM_NAME = "PreviewMask";
   private static final double MAX_ENTITY_DISTANCE_SQUARED = 22500.0;
   private static final int CATEGORY_TAG_BASE_ARGB = -16711939;
   private static final float ВН7 = 0.1F;
   private static final float ВНР = 0.5F;
   private static final float ВН3 = 0.05F;
   private static final float ВНр = 0.03125F;
   private static final float ВН6 = 0.75F;
   private static final float ВН_ = 0.25F;
   private static final float ВНШ = 1.25F;
   private static final float ВНы = 0.05F;
   private static final float ВНй = 0.5F;
   private static final float ВНЖ = 0.5F;
   private static final float ВНм = 0.5F;
   private static final float ВНК = 0.5F;
   private static final double GLOW_SCALE_LOG_BASE = 2.0;
   private static final double GLOW_DISTANCE_CODE_UNITS_PER_OCTAVE = 50.0;
   private static final String EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_TARGET_LABEL = "entity-esp-category-mask";
   private static final String EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_BUFFER_LABEL = "entityEspCategoryMask";
   private static final String EXTRACT_CATEGORY_MASK_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String EXTRACT_CATEGORY_MASK_CATEGORY_MASK_UNIFORM_NAME = "CategoryMask";
   private static final float MIN_FILL_OPACITY = 0.002F;
   private static final float ВН0 = 8.0F;
   private static final String DRAW_MODEL_FILL_ENTITY_ESP_MODEL_FILL_UNIFORMS_BUFFER_LABEL = "entityEspModelFillUniforms";
   private static final long FILL_ANIMATION_PERIOD_MILLIS = 120000L;
   private static final float MILLIS_PER_SECOND = 1000.0F;
   private static final String DRAW_MODEL_FILL_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String DRAW_MODEL_FILL_SCENE_SAMPLER_SAMPLER_NAME = "SceneSampler";
   private static final String DRAW_MODEL_FILL_SURFACE_SAMPLER_SAMPLER_NAME = "SurfaceSampler";
   private static final String DRAW_MODEL_FILL_ESP_CHAMS_FILL_UNIFORM_NAME = "EspChamsFill";
   private static final float ВНБ = 25.0F;
   private static final float ВНС = 6.0F;
   private static final float ВНз = 12.0F;
   private static final String COMPOSITE_DISTANCE_BUCKET_GLOW_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final float ВНя = 0.01F;
   private static final float Внс = 25.0F;
   private static final float ВнВ = 12.0F;
   private static final float ВнЯ = 6.0F;
   private static final float ВнХ = 45.0F;
   private static final float Вне = 9.0F;
   private static final float ВнА = 0.2025F;
   private static final float Вн4 = 1.0E-4F;
   private static final float Внп = 0.81F;
   private static final String FILTER_MODEL_GLOW_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String FILTER_MODEL_GLOW_RADIUS_SAMPLER_SAMPLER_NAME = "RadiusSampler";
   private static final String FILTER_MODEL_GLOW_SURFACE_SAMPLER_SAMPLER_NAME = "SurfaceSampler";
   private static final String FILTER_MODEL_GLOW_ESP_OUTER_GLOW_UNIFORM_NAME = "EspOuterGlow";
   private static final String COMPOSITE_GLOW_BLOOM_SAMPLER_SAMPLER_NAME = "BloomSampler";
   private static final String COMPOSITE_GLOW_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String COMPOSITE_GLOW_ESP_OUTER_GLOW_UNIFORM_NAME = "EspOuterGlow";
   private static final String ENSURE_FILTERED_GLOW_TARGET_ENTITY_ESP_FILTERED_GLOW_TARGET_LABEL = "entity-esp-filtered-glow";
   private static final String DRAW_GLOW_FILTER_PASS_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String DRAW_GLOW_FILTER_PASS_GLOW_BLUR_UNIFORM_NAME = "GlowBlur";
   private static final String UPDATE_GLOW_BLUR_UNIFORMS_ENTITY_ESP_GLOW_HORIZONTAL_BUFFER_LABEL = "entityEspGlowHorizontal";
   private static final String UPDATE_GLOW_BLUR_UNIFORMS_ENTITY_ESP_GLOW_VERTICAL_BUFFER_LABEL = "entityEspGlowVertical";
   private static final float ВнИ = Float.MIN_NORMAL;
   private static final float Внь = 3.0F;
   private static final float Внж = Float.MIN_NORMAL;
   private static final float Вн弟 = 5.0F;
   private static final float Внб = Float.MIN_NORMAL;
   private static final float ВнЫ = 7.0F;
   private static final float Внl = Float.MIN_NORMAL;
   private static final String DRAW_MODEL_OUTLINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String DRAW_MODEL_OUTLINE_ESP_GLOW_UNIFORM_NAME = "EspGlow";
   private static final String DRAW_SCENE_BLUR_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String DRAW_SCENE_BLUR_ESP_BLUR_UNIFORM_NAME = "EspBlur";
   private static final String UPDATE_SCENE_BLUR_UNIFORMS_ENTITY_ESP_MODEL_BLUR_HORIZONTAL_UNIFORMS_BUFFER_LABEL = "entityEspModelBlurHorizontalUniforms";
   private static final String UPDATE_SCENE_BLUR_UNIFORMS_ENTITY_ESP_MODEL_BLUR_VERTICAL_UNIFORMS_BUFFER_LABEL = "entityEspModelBlurVerticalUniforms";
   private static final float SCENE_BLUR_SAMPLE_STEP_RADIUS_FACTOR = 0.2F;
   private static final float SCENE_BLUR_SIGMA_RADIUS_FACTOR = 0.5F;
   private static final String UPDATE_GLOW_UNIFORMS_ENTITY_ESP_MODEL_GLOW_UNIFORMS_BUFFER_LABEL = "entityEspModelGlowUniforms";
   private static final String ENERGY_GLOW_STYLE_OPTION = "Энергопоток";
   private static final String SMOKY_GLOW_STYLE_OPTION = "Дымчатая аура";
   private static final String SOFT_NEON_GLOW_STYLE_OPTION = "Мягкий неон";
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float Внл = 1.5F;
   private static final String UPDATE_OUTLINE_UNIFORMS_ENTITY_ESP_MODEL_OUTLINE_UNIFORMS_BUFFER_LABEL = "entityEspModelOutlineUniforms";
   private static final float Вню = 1.5F;
   private static final String ENSURE_SCENE_BLUR_TARGETS_ENTITY_ESP_MODEL_BLUR_HORIZONTAL_TARGET_LABEL = "entity-esp-model-blur-horizontal";
   private static final String ENSURE_SCENE_BLUR_TARGETS_ENTITY_ESP_MODEL_BLUR_VERTICAL_TARGET_LABEL = "entity-esp-model-blur-vertical";
   private static final String ENSURE_PREVIEW_TARGETS_ENTITY_ESP_PREVIEW_SOURCE_TARGET_LABEL = "entity-esp-preview-source";
   private static final String ENSURE_PREVIEW_TARGETS_ENTITY_ESP_PREVIEW_OUTPUT_TARGET_LABEL = "entity-esp-preview-output";
   private static final String COSMOS_FILL_OPTION = "Космос";
   private static final String PLASMA_FILL_OPTION = "Плазма";
   private static final String GLASS_FILL_OPTION = "Стекло";
   private static final String SMOKY_GLOW_SURFACE_OPTION = "Дымчатая аура";
   private static final String ENERGY_GLOW_SURFACE_OPTION = "Энергопоток";
   private static final double CAMERA_INTERSECTION_PADDING = 0.25;
   private static final float GLOW_BUCKET_PROJECTION_DIVISOR = 360.0F;
   private static final float COLOR_CHANNEL_MAX = 255.0F;
   private static final String DRAW_SCENE_BLUR_ENTITY_ESP_MODEL_BLUR_PASS_LABEL = "entityEspModelBlur";
   private static final String DRAW_MODEL_OUTLINE_ENTITY_ESP_MODEL_OUTLINE_PASS_LABEL = "entityEspModelOutline";
   private static final String DRAW_GLOW_FILTER_PASS_ENTITY_ESP_GLOW_FILTER_PASS_LABEL = "entityEspGlowFilter";
   private static final String COMPOSITE_GLOW_ENTITY_ESP_MODEL_GLOW_PASS_LABEL = "entityEspModelGlow";
   private static final String FILTER_MODEL_GLOW_ENTITY_ESP_MODEL_MASK_PASS_LABEL = "entityEspModelMask";
   private static final String COMPOSITE_DISTANCE_BUCKET_GLOW_ENTITY_ESP_DISTANCE_GLOW_PASS_LABEL = "entityEspDistanceGlow";
   private static final String DRAW_MODEL_FILL_ENTITY_ESP_MODEL_FILL_PASS_LABEL = "entityEspModelFill";
   private static final String EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_PASS_LABEL = "entityEspCategoryMask";
   private static final String RECOLOR_PREVIEW_MASK_ENTITY_ESP_PREVIEW_MASK_PASS_LABEL = "entityEspPreviewMask";
   private static final String RECOLOR_PREVIEW_MASK_ENTITY_ESP_PREVIEW_MASK_UNIFORMS_BUFFER_LABEL = "entityEspPreviewMaskUniforms";
   private static final String COPY_PREVIEW_TEXTURE_ENTITY_ESP_PREVIEW_COPY_PASS_LABEL = "entityEspPreviewCopy";
   private static final String RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_PASS_LABEL = "entityEspPreviewSurface";
   private static final String CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_PASS_LABEL = "entityEspGlowSurface";
   private static final String CAPTURE_ENTITY_SURFACES_ENTITY_ESP_SURFACE_PASS_LABEL = "entityEspSurface";
   private static final String CATEGORY_MASK_PIPELINE_LOCATION = "rose/entity_esp_category_mask";
   private static final String CATEGORY_MASK_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String CATEGORY_MASK_PIPELINE_FRAGMENT_SHADER = "entity_esp_category_mask";
   private static final String CATEGORY_MASK_PIPELINE_CATEGORY_MASK_UNIFORM_NAME = "CategoryMask";
   private static final String CATEGORY_MASK_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String MODEL_MASK_COPY_PIPELINE_LOCATION = "rose/entity_esp_model_mask";
   private static final String MODEL_MASK_COPY_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String MODEL_MASK_COPY_PIPELINE_FRAGMENT_SHADER = "entity_esp_mask";
   private static final String MODEL_MASK_COPY_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String PREVIEW_MASK_PIPELINE_LOCATION = "rose/entity_esp_preview_mask";
   private static final String PREVIEW_MASK_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String PREVIEW_MASK_PIPELINE_FRAGMENT_SHADER = "entity_esp_preview_mask";
   private static final String PREVIEW_MASK_PIPELINE_PREVIEW_MASK_UNIFORM_NAME = "PreviewMask";
   private static final String PREVIEW_MASK_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String SCENE_BLUR_PIPELINE_LOCATION = "rose/entity_esp_model_blur";
   private static final String SCENE_BLUR_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String SCENE_BLUR_PIPELINE_FRAGMENT_SHADER = "entity_esp_gaussian_blur";
   private static final String SCENE_BLUR_PIPELINE_ESP_BLUR_UNIFORM_NAME = "EspBlur";
   private static final String SCENE_BLUR_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_LOCATION = "rose/entity_esp_model_glow";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER = "entity_esp_model_glow";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME = "EspOuterGlow";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_SAMPLER_NAME = "BloomSampler";
   private static final String WORLD_GLOW_COMPOSITE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String GLOW_SEED_PIPELINE_LOCATION = "rose/entity_esp_glow_seed";
   private static final String GLOW_SEED_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String GLOW_SEED_PIPELINE_FRAGMENT_SHADER = "entity_esp_glow_seed";
   private static final String GLOW_SEED_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME = "EspOuterGlow";
   private static final String GLOW_SEED_PIPELINE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String GLOW_SEED_PIPELINE_SURFACE_SAMPLER_SAMPLER_NAME = "SurfaceSampler";
   private static final String GLOW_SEED_PIPELINE_RADIUS_SAMPLER_SAMPLER_NAME = "RadiusSampler";
   private static final String GLOW_ACCUMULATE_PIPELINE_LOCATION = "rose/entity_esp_glow_accumulate";
   private static final String GLOW_ACCUMULATE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String GLOW_ACCUMULATE_PIPELINE_FRAGMENT_SHADER = "entity_esp_glow_accumulate";
   private static final String GLOW_ACCUMULATE_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_LOCATION = "rose/entity_esp_preview_glow";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER = "entity_esp_model_glow";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_ESP_OUTER_GLOW_UNIFORM_NAME = "EspOuterGlow";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_SAMPLER_NAME = "BloomSampler";
   private static final String PREVIEW_GLOW_COMPOSITE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String GLOW_BLUR_PIPELINE_LOCATION = "rose/entity_esp_glow_blur";
   private static final String GLOW_BLUR_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String GLOW_BLUR_PIPELINE_FRAGMENT_SHADER = "entity_esp_glow_blur";
   private static final String GLOW_BLUR_PIPELINE_GLOW_BLUR_UNIFORM_NAME = "GlowBlur";
   private static final String GLOW_BLUR_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String GLOW_DOWNSAMPLE_SHADER = "entity_esp_glow_downsample";
   private static final String GLOW_UPSAMPLE_SHADER = "entity_esp_glow_upsample";
   private static final String PREVIEW_SURFACE_PIPELINE_LOCATION = "rose/entity_esp_preview_surface";
   private static final String PREVIEW_SURFACE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String PREVIEW_SURFACE_PIPELINE_FRAGMENT_SHADER = "entity_esp_preview_surface";
   private static final String PREVIEW_SURFACE_PIPELINE_PREVIEW_SURFACE_UNIFORM_NAME = "PreviewSurface";
   private static final String PREVIEW_SURFACE_PIPELINE_DEPTH_SAMPLER_SAMPLER_NAME = "DepthSampler";
   private static final String PREVIEW_SURFACE_PIPELINE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String GLOW_SURFACE_PIPELINE_LOCATION = "rose/entity_esp_glow_surface";
   private static final String GLOW_SURFACE_PIPELINE_VERTEX_SHADER = "entity_esp_glow_surface";
   private static final String GLOW_SURFACE_PIPELINE_FRAGMENT_SHADER = "entity_esp_glow_surface";
   private static final String GLOW_SURFACE_PIPELINE_ESP_GLOW_SURFACE_UNIFORM_NAME = "EspGlowSurface";
   private static final String MODEL_OUTLINE_PIPELINE_LOCATION = "rose/entity_esp_model_outline";
   private static final String MODEL_OUTLINE_PIPELINE_VERTEX_SHADER = "entity_esp_glow_blit";
   private static final String MODEL_OUTLINE_PIPELINE_FRAGMENT_SHADER = "entity_esp_glow_blit";
   private static final String MODEL_OUTLINE_PIPELINE_ESP_GLOW_UNIFORM_NAME = "EspGlow";
   private static final String MODEL_OUTLINE_PIPELINE_IN_SAMPLER_SAMPLER_NAME = "InSampler";
   private static final String MODEL_FILL_PIPELINE_LOCATION = "rose/entity_esp_model_fill";
   private static final String MODEL_FILL_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String MODEL_FILL_PIPELINE_FRAGMENT_SHADER = "entity_esp_chams_fill";
   private static final String MODEL_FILL_PIPELINE_ESP_CHAMS_FILL_UNIFORM_NAME = "EspChamsFill";
   private static final String MODEL_FILL_PIPELINE_MASK_SAMPLER_SAMPLER_NAME = "MaskSampler";
   private static final String MODEL_FILL_PIPELINE_SURFACE_SAMPLER_SAMPLER_NAME = "SurfaceSampler";
   private static final String MODEL_FILL_PIPELINE_SCENE_SAMPLER_SAMPLER_NAME = "SceneSampler";
   private static final String MODEL_SURFACE_PIPELINE_LOCATION = "rose/entity_esp_surface";
   private static final String MODEL_SURFACE_PIPELINE_VERTEX_SHADER = "entity_esp_surface";
   private static final String MODEL_SURFACE_PIPELINE_FRAGMENT_SHADER = "entity_esp_surface";
   private static final String MODEL_SURFACE_PIPELINE_ESP_SURFACE_ANCHORS_UNIFORM_NAME = "EspSurfaceAnchors";

   private TextureTarget ensureFilteredGlowTarget(TextureTarget target, int width, int height) {
      if (target == null) {
         return new TextureTarget(ENSURE_FILTERED_GLOW_TARGET_ENTITY_ESP_FILTERED_GLOW_TARGET_LABEL, width, height, false, GpuFormat.RGBA16_FLOAT);
      }

      if (target.width != width || target.height != height) {
         target.resize(width, height);
      }

      return target;
   }

   private boolean hasSurfaceEffects(EntityEspStyleSettings style) {
      return style != null && (style.getOutlineEnabled().getValue() || style.getFillEnabled().getValue() || style.getGlowEnabled().getValue());
   }

   private static float normalizeColorChannel(int argb, int shift) {
      return (argb >>> shift & 0xFF) / COLOR_CHANNEL_MAX;
   }

   private void ensureGlowPyramidTargets(int width, int height, int deepestLevel, boolean preview) {
      int chainIndex = preview ? 1 : 0;

      for (int level = 0; level <= deepestLevel; level++) {
         int divisor = 2 << level;
         int levelWidth = Math.max(1, (width + divisor - 1) / divisor);
         int levelHeight = Math.max(1, (height + divisor - 1) / divisor);
         this.glowPyramidTargets[chainIndex][level] = this.ensureFilteredGlowTarget(this.glowPyramidTargets[chainIndex][level], levelWidth, levelHeight);
         if (level == deepestLevel) {
            this.glowBlurScratchTargets[chainIndex][level] = this.ensureFilteredGlowTarget(this.glowBlurScratchTargets[chainIndex][level], levelWidth, levelHeight);
         }
      }
   }

   private void applyNormalizedScissor(RenderPass renderPass, RenderTarget target, float[] normalizedBounds) {
      if (normalizedBounds != null) {
         int left = Math.max(0, (int)Math.floor(normalizedBounds[0] * target.width));
         int bottom = Math.max(0, (int)Math.floor(normalizedBounds[1] * target.height));
         int right = Math.min(target.width, (int)Math.ceil(normalizedBounds[2] * target.width));
         int top = Math.min(target.height, (int)Math.ceil(normalizedBounds[3] * target.height));
         renderPass.enableScissor(left, bottom, Math.max(1, right - left), Math.max(1, top - bottom));
      }
   }

   private int getFillModeCode(EntityEspStyleSettings style) {
      if (style.getFillMode().isSelected(COSMOS_FILL_OPTION)) {
         return 2;
      } else if (style.getFillMode().isSelected(PLASMA_FILL_OPTION)) {
         return 3;
      } else {
         return style.getFillMode().isSelected(GLASS_FILL_OPTION) ? 4 : 1;
      }
   }

   private boolean isStyleActive(EntityEspStyleSettings style) {
      return style.getEnabled().getValue() && this.hasSurfaceEffects(style);
   }

   public void onEntityRenderState(EntityRenderStateEvent event) {
      if (this.canRender()) {
         Entity entity = event.getEntity();
         EntityEspStyleSettings style = this.styleResolver.apply(entity);
         if (style != null) {
            if (!this.shouldRenderEntity(entity, style)) {
               event.getRenderState().outlineColor = 0;
            } else {
               this.beginSurfaceFrameIfNeeded();
               this.hasRenderedEntitiesThisFrame = true;
               int styleIndex = this.styles.indexOf(style);
               if (styleIndex >= 0) {
                  this.activeCategories[styleIndex] = true;
               }

               this.needsGlowSurfaceThisFrame = this.needsGlowSurfaceThisFrame | (style.getGlowEnabled().getValue() && this.needsGlowSurface(style));
               if (this.anchorCount < 64) {
                  double[] anchor = this.worldAnchors[this.anchorCount++];
                  EntityRenderState renderState = event.getRenderState();
                  anchor[0] = renderState.x;
                  anchor[1] = renderState.y + entity.getBbHeight() * ВНЦ;
                  anchor[2] = renderState.z;
                  anchor[3] = Math.max(ВНч, entity.getBbHeight());
               }

               if (style.getFillEnabled().getValue()) {
                  if (!this.hasFillThisFrame) {
                     this.firstFillOpacity = style.getFillOpacity().getValue();
                     this.firstFillColor = this.getFillColor(style);
                     this.firstFillMode = this.getFillModeCode(style);
                  }

                  this.hasFillThisFrame = true;
               }

               int categoryColor = this.getEncodedCategoryColor(style);
               if (styleIndex >= 0 && style.getGlowEnabled().getValue()) {
                  int distanceCode = this.recordGlowDistanceBuckets(entity, event.getRenderState(), styleIndex);
                  categoryColor = categoryColor & -256 | 253 - distanceCode;
               }

               event.getRenderState().outlineColor = categoryColor;
            }
         }
      }
   }

   public GpuTextureView renderPreview(GpuTextureView inputTexture, int width, int height, EntityEspStyleSettings style, GpuTextureView depthTexture, Matrix4f inverseModelViewProjection, float modelHeight) {
      if (inputTexture != null && width > 0 && height > 0 && style != null && this.hasSurfaceEffects(style)) {
         this.ensurePreviewTargets(width, height);
         CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
         this.copyPreviewTexture(commandEncoder, inputTexture, this.previewOutputTarget);
         this.recolorPreviewMask(commandEncoder, inputTexture, this.previewMaskTarget, this.getOutlineColor(style) | ВНЭ);
         this.previewSurfaceReady = false;
         if (style.getGlowEnabled().getValue() && this.needsGlowSurface(style)) {
            this.renderPreviewSurface(commandEncoder, depthTexture, inverseModelViewProjection, modelHeight, width, height);
         }

         if (style.getFillEnabled().getValue()) {
            this.drawModelFill(this.previewMaskTarget, this.previewOutputTarget, style.getFillOpacity().getValue(), this.getFillColor(style), this.getFillModeCode(style), true);
         }

         if (style.getOutlineEnabled().getValue() || style.getGlowEnabled().getValue()) {
            if (style.getGlowEnabled().getValue()) {
               this.drawModelGlow(this.previewMaskTarget, this.previewOutputTarget, style.getGlowRadius().getValue(), style.getInnerGlow().getValue(), style.getOutlineEnabled().getValue() ? style.getOutlineWidth().getValue() : 0.0F, style, true);
            } else {
               this.drawModelOutline(this.previewMaskTarget, this.previewOutputTarget, style.getOutlineWidth().getValue());
            }
         }

         return this.previewOutputTarget.getColorTextureView();
      } else {
         return null;
      }
   }

   private void captureGlowSurface(PreparedRenderType preparedType, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int baseVertex, int firstIndex, int indexCount) {
      Matrix4f viewMatrix = WorldProjection.getViewMatrix();
      if (viewMatrix != null && this.anchorCount != 0) {
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (this.glowSurfaceTarget == null) {
            this.glowSurfaceTarget = new TextureTarget(CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_TARGET_LABEL, mainTarget.width, mainTarget.height, true, GpuFormat.RGBA8_UNORM);
         } else if (this.glowSurfaceTarget.width != mainTarget.width || this.glowSurfaceTarget.height != mainTarget.height) {
            this.glowSurfaceTarget.resize(mainTarget.width, mainTarget.height);
            this.glowSurfaceReady = false;
         }

         GpuTextureView colorView = this.glowSurfaceTarget.getColorTextureView();
         GpuTextureView depthView = this.glowSurfaceTarget.getDepthTextureView();
         if (colorView != null && !colorView.isClosed() && depthView != null && !depthView.isClosed()) {
            CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
            if (!this.glowSurfaceReady) {
               this.glowSurfaceUniforms = this.ensureUniformBuffer(this.glowSurfaceUniforms, 1088, CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_BUFFER_LABEL);
               Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
               Vector4f worldRightInView = new Vector4f(1.0F, 0.0F, 0.0F, 0.0F).mul(viewMatrix);
               Vector4f worldUpInView = new Vector4f(0.0F, 1.0F, 0.0F, 0.0F).mul(viewMatrix);
               Vector4f worldForwardInView = new Vector4f(0.0F, 0.0F, 1.0F, 0.0F).mul(viewMatrix);
               Vector4f viewPosition = new Vector4f();

               try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                  Std140Builder uniformBuilder = Std140Builder.onStack(memoryStack, 1088)
                     .putVec4(this.anchorCount, 0.0F, 0.0F, 0.0F)
                     .putVec4(worldRightInView.x, worldRightInView.y, worldRightInView.z, 0.0F)
                     .putVec4(worldUpInView.x, worldUpInView.y, worldUpInView.z, 0.0F)
                     .putVec4(worldForwardInView.x, worldForwardInView.y, worldForwardInView.z, 0.0F);

                  for (int anchorIndex = 0; anchorIndex < 64; anchorIndex++) {
                     if (anchorIndex < this.anchorCount) {
                        double[] anchor = this.worldAnchors[anchorIndex];
                        viewPosition.set((float)(anchor[0] - cameraPosition.x), (float)(anchor[1] - cameraPosition.y), (float)(anchor[2] - cameraPosition.z), 1.0F).mul(viewMatrix);
                        uniformBuilder.putVec4(viewPosition.x, viewPosition.y, viewPosition.z, (float)anchor[3]);
                     } else {
                        uniformBuilder.putVec4(0.0F, 0.0F, 0.0F, 1.0F);
                     }
                  }

                  commandEncoder.writeToBuffer(this.glowSurfaceUniforms.slice(), uniformBuilder.get());
               }
            }

            try (RenderPass renderPass = commandEncoder.createRenderPass(
                  () -> CAPTURE_GLOW_SURFACE_ENTITY_ESP_GLOW_SURFACE_PASS_LABEL,
                  colorView,
                  this.glowSurfaceReady ? Optional.empty() : Optional.of(new Vector4f(0.0F)),
                  depthView,
                  this.glowSurfaceReady ? OptionalDouble.empty() : OptionalDouble.of(0.0)
               )) {
               renderPass.setPipeline(GLOW_SURFACE_PIPELINE);
               ScissorState scissor = preparedType.scissorState();
               if (scissor.enabled()) {
                  renderPass.enableScissor(scissor.x(), scissor.y(), scissor.width(), scissor.height());
               }

               RenderSystem.bindDefaultUniforms(renderPass);
               renderPass.setUniform(CAPTURE_GLOW_SURFACE_DYNAMIC_TRANSFORMS_UNIFORM_NAME, preparedType.dynamicTransforms());
               renderPass.setUniform(CAPTURE_GLOW_SURFACE_ESP_GLOW_SURFACE_UNIFORM_NAME, this.glowSurfaceUniforms);
               renderPass.setVertexBuffer(0, vertexBuffer.slice());
               renderPass.setIndexBuffer(indexBuffer, indexType);
               preparedType.textures().forEach(textureBinding -> renderPass.bindTexture(textureBinding.name(), textureBinding.textureView(), textureBinding.sampler()));
               renderPass.drawIndexed(indexCount, 1, firstIndex, baseVertex, 0);
            }

            this.glowSurfaceReady = true;
         }
      }
   }

   private void compositeDistanceBucketGlow(RenderTarget maskTarget, RenderTarget outputTarget, int styleIndex, float outlineWidth, EntityEspStyleSettings style) {
      int bucketMask = this.activeDistanceBuckets[styleIndex];
      if (bucketMask != 0) {
         this.glowAccumulatorTarget = this.ensureFilteredGlowTarget(this.glowAccumulatorTarget, Math.max(1, (maskTarget.width + 1) / 2), Math.max(1, (maskTarget.height + 1) / 2));
         boolean clearAccumulator = true;

         for (int bucketIndex = 0; bucketIndex < 6; bucketIndex++) {
            if ((bucketMask & 1 << bucketIndex) != 0) {
               float bucketRadius = Math.clamp(style.getGlowRadius().getValue(), 1.0F, ВНБ) / (1 << bucketIndex);
               float[] bucketBounds = this.categoryBucketBounds[styleIndex][bucketIndex];
               float padding = bucketRadius * ВНС + ВНз;
               bucketBounds[0] = Math.clamp(bucketBounds[0] - padding / maskTarget.width, 0.0F, 1.0F);
               bucketBounds[1] = Math.clamp(bucketBounds[1] - padding / maskTarget.height, 0.0F, 1.0F);
               bucketBounds[2] = Math.clamp(bucketBounds[2] + padding / maskTarget.width, 0.0F, 1.0F);
               bucketBounds[3] = Math.clamp(bucketBounds[3] + padding / maskTarget.height, 0.0F, 1.0F);
               if (!(bucketBounds[2] <= bucketBounds[0]) && !(bucketBounds[3] <= bucketBounds[1])) {
                  TextureTarget filteredGlow = this.filterModelGlow(this.categoryMaskTarget, outputTarget, bucketRadius, style.getInnerGlow().getValue(), outlineWidth, style, false, maskTarget, bucketIndex, styleIndex, bucketBounds);
                  if (filteredGlow != null) {
                     try (RenderPass renderPass = RenderSystem.getDevice()
                           .createCommandEncoder()
                           .createRenderPass(() -> COMPOSITE_DISTANCE_BUCKET_GLOW_ENTITY_ESP_DISTANCE_GLOW_PASS_LABEL, this.glowAccumulatorTarget.getColorTextureView(), clearAccumulator ? Optional.of(new Vector4f(0.0F)) : Optional.empty())) {
                        renderPass.setPipeline(GLOW_ACCUMULATE_PIPELINE);
                        renderPass.bindTexture(COMPOSITE_DISTANCE_BUCKET_GLOW_IN_SAMPLER_SAMPLER_NAME, filteredGlow.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                        this.applyNormalizedScissor(renderPass, this.glowAccumulatorTarget, bucketBounds);
                        renderPass.draw(3, 1, 0, 0);
                     }

                     clearAccumulator = false;
                  }
               }
            }
         }

         if (!clearAccumulator) {
            this.compositeGlow(this.categoryMaskTarget, outputTarget, this.glowAccumulatorTarget, false);
         }
      }
   }

   private int getFillColor(EntityEspStyleSettings style) {
      return style.getUseThemeColors().getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : style.getFillColor().getValue();
   }

   private int recordGlowDistanceBuckets(Entity entity, EntityRenderState renderState, int styleIndex) {
      float minU = 0.0F;
      float minV = 0.0F;
      float maxU = 1.0F;
      float maxV = 1.0F;
      float projectedEntityScale = 1.0F;
      if (this.viewRotationProjectionMatrix != null) {
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         float relativeX = (float)(renderState.x - cameraPosition.x);
         float relativeY = (float)(renderState.y - cameraPosition.y);
         float relativeZ = (float)(renderState.z - cameraPosition.z);
         float entityHeight = Math.max(ВН7, entity.getBbHeight());
         this.clipPositionScratch.set(relativeX, relativeY + entityHeight * ВНР, relativeZ, 1.0F).mul(this.viewRotationProjectionMatrix);
         projectedEntityScale = Math.clamp(entityHeight * this.distanceBucketProjectionScale / Math.max(this.clipPositionScratch.w, ВН3), ВНр, 1.0F);
         float paddedHalfWidth = Math.max(entity.getBbWidth(), entityHeight * ВН6);
         minV = 1.0F;
         minU = 1.0F;
         maxV = 0.0F;
         maxU = 0.0F;

         for (int cornerIndex = 0; cornerIndex < 8; cornerIndex++) {
            this.clipPositionScratch
               .set(
                  relativeX + ((cornerIndex & 1) == 0 ? -paddedHalfWidth : paddedHalfWidth),
                  relativeY + ((cornerIndex & 2) == 0 ? -entityHeight * ВН_ : entityHeight * ВНШ),
                  relativeZ + ((cornerIndex & 4) == 0 ? -paddedHalfWidth : paddedHalfWidth),
                  1.0F
               )
               .mul(this.viewRotationProjectionMatrix);
            if (this.clipPositionScratch.w <= ВНы) {
               minV = 0.0F;
               minU = 0.0F;
               maxV = 1.0F;
               maxU = 1.0F;
               break;
            }

            float u = this.clipPositionScratch.x / this.clipPositionScratch.w * ВНй + ВНЖ;
            float v = this.clipPositionScratch.y / this.clipPositionScratch.w * ВНм + ВНК;
            minU = Math.min(minU, u);
            minV = Math.min(minV, v);
            maxU = Math.max(maxU, u);
            maxV = Math.max(maxV, v);
         }
      }

      int distanceCode = Math.clamp((int)Math.round(-Math.log(projectedEntityScale) / Math.log(GLOW_SCALE_LOG_BASE) * GLOW_DISTANCE_CODE_UNITS_PER_OCTAVE), 0, 250);
      int firstBucket = distanceCode / 50;
      int lastBucket = Math.min(5, (distanceCode + 49) / 50);

      for (int bucketIndex = firstBucket; bucketIndex <= lastBucket; bucketIndex++) {
         this.activeDistanceBuckets[styleIndex] = this.activeDistanceBuckets[styleIndex] | 1 << bucketIndex;
         float[] bucketBounds = this.categoryBucketBounds[styleIndex][bucketIndex];
         bucketBounds[0] = Math.min(bucketBounds[0], minU);
         bucketBounds[1] = Math.min(bucketBounds[1], minV);
         bucketBounds[2] = Math.max(bucketBounds[2], maxU);
         bucketBounds[3] = Math.max(bucketBounds[3], maxV);
      }

      return distanceCode;
   }

   public boolean shouldRenderEntity(Entity entity, EntityEspStyleSettings style) {
      if (entity == null || style == null || !this.isStyleActive(style)) {
         return false;
      } else if (!entity.isAlive() && !(entity instanceof ItemEntity)) {
         return false;
      } else if (!this.isFirstPersonCameraEntity(entity) && !this.isCameraInsideEntity(entity)) {
         Entity cameraEntity = MINECRAFT_CLIENT.getCameraEntity();
         return cameraEntity != null && entity.distanceToSqr(cameraEntity) <= MAX_ENTITY_DISTANCE_SQUARED;
      } else {
         return false;
      }
   }

   private void updateSceneBlurUniforms(CommandEncoder commandEncoder, int width, int height, float radius) {
      GpuBuffer previousHorizontalBuffer = this.sceneBlurHorizontalUniforms;
      GpuBuffer previousVerticalBuffer = this.sceneBlurVerticalUniforms;
      this.sceneBlurHorizontalUniforms = this.ensureUniformBuffer(this.sceneBlurHorizontalUniforms, 48, UPDATE_SCENE_BLUR_UNIFORMS_ENTITY_ESP_MODEL_BLUR_HORIZONTAL_UNIFORMS_BUFFER_LABEL);
      this.sceneBlurVerticalUniforms = this.ensureUniformBuffer(this.sceneBlurVerticalUniforms, 48, UPDATE_SCENE_BLUR_UNIFORMS_ENTITY_ESP_MODEL_BLUR_VERTICAL_UNIFORMS_BUFFER_LABEL);
      boolean requiresUpdate = previousHorizontalBuffer != this.sceneBlurHorizontalUniforms || previousVerticalBuffer != this.sceneBlurVerticalUniforms || this.cachedSceneBlurWidth != width || this.cachedSceneBlurHeight != height || Float.compare(this.cachedSceneBlurRadius, radius) != 0;
      if (requiresUpdate) {
         float sampleSpacing = Math.max(radius, 1.0F) * SCENE_BLUR_SAMPLE_STEP_RADIUS_FACTOR;
         float sigma = Math.max(Math.max(radius, 1.0F) * SCENE_BLUR_SIGMA_RADIUS_FACTOR, 1.0F);
         float[] weights = new float[6];
         weights[0] = 1.0F;
         float normalizationSum = 1.0F;

         for (int sampleIndex = 1; sampleIndex < weights.length; sampleIndex++) {
            float sampleDistance = sampleSpacing * sampleIndex;
            weights[sampleIndex] = (float)Math.exp(-(sampleDistance * sampleDistance) / (2.0F * sigma * sigma));
            normalizationSum += weights[sampleIndex] * 2.0F;
         }

         for (int normalizeIndex = 0; normalizeIndex < weights.length; normalizeIndex++) {
            weights[normalizeIndex] /= normalizationSum;
         }

         try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer horizontalData = Std140Builder.onStack(memoryStack, 48)
               .putVec2(1.0F / width, 1.0F / height)
               .putVec2(1.0F, 0.0F)
               .putVec4(sampleSpacing, weights[0], weights[1], weights[2])
               .putVec4(weights[3], weights[4], weights[5], 0.0F)
               .get();
            commandEncoder.writeToBuffer(this.sceneBlurHorizontalUniforms.slice(), horizontalData);
            ByteBuffer verticalData = Std140Builder.onStack(memoryStack, 48)
               .putVec2(1.0F / width, 1.0F / height)
               .putVec2(0.0F, 1.0F)
               .putVec4(sampleSpacing, weights[0], weights[1], weights[2])
               .putVec4(weights[3], weights[4], weights[5], 0.0F)
               .get();
            commandEncoder.writeToBuffer(this.sceneBlurVerticalUniforms.slice(), verticalData);
         }

         this.cachedSceneBlurWidth = width;
         this.cachedSceneBlurHeight = height;
         this.cachedSceneBlurRadius = radius;
      }
   }

   public int getEncodedCategoryColor(EntityEspStyleSettings style) {
      if (style == null) {
         return 0;
      }

      int styleIndex = this.styles.indexOf(style);
      return styleIndex < 0 ? 0 : CATEGORY_TAG_BASE_ARGB | styleIndex + 1 << 16;
   }

   private boolean needsGlowSurface(EntityEspStyleSettings style) {
      return style.getGlowStyle().isSelected(SMOKY_GLOW_SURFACE_OPTION) || style.getGlowStyle().isSelected(ENERGY_GLOW_SURFACE_OPTION) || style.getTwoColorGlow().getValue();
   }

   private void recolorPreviewMask(CommandEncoder commandEncoder, GpuTextureView inputTexture, TextureTarget target, int argb) {
      GpuTextureView colorView = target.getColorTextureView();
      if (colorView != null && !colorView.isClosed()) {
         GpuBuffer temporaryUniformBuffer = null;

         try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer uniformData = Std140Builder.onStack(memoryStack, 16)
               .putVec4((argb >> 16 & 0xFF) / ВНЩ, (argb >> 8 & 0xFF) / ВНу, (argb & 0xFF) / ВН2, (argb >>> 24 & 0xFF) / ВНф)
               .get();
            temporaryUniformBuffer = RenderSystem.getDevice().createBuffer(() -> RECOLOR_PREVIEW_MASK_ENTITY_ESP_PREVIEW_MASK_UNIFORMS_BUFFER_LABEL, 128, uniformData);

            try (RenderPass renderPass = commandEncoder.createRenderPass(() -> RECOLOR_PREVIEW_MASK_ENTITY_ESP_PREVIEW_MASK_PASS_LABEL, colorView, Optional.of(new Vector4f(0.0F)))) {
               renderPass.setPipeline(PREVIEW_MASK_PIPELINE);
               renderPass.bindTexture(RECOLOR_PREVIEW_MASK_IN_SAMPLER_SAMPLER_NAME, inputTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               renderPass.setUniform(RECOLOR_PREVIEW_MASK_PREVIEW_MASK_UNIFORM_NAME, temporaryUniformBuffer);
               renderPass.draw(3, 1, 0, 0);
            }
         } finally {
            if (temporaryUniformBuffer != null) {
               temporaryUniformBuffer.close();
            }
         }
      }
   }

   private void invalidateUniformCaches() {
      this.cachedGlowUniformData.clear().limit(0);
      this.cachedSceneBlurWidth = -1;
      this.cachedSceneBlurHeight = -1;
      this.cachedSceneBlurRadius = Float.NaN;
      this.cachedOutlineTargetWidth = -1;
      this.cachedOutlineTargetHeight = -1;
      this.cachedOutlineWidth = Float.NaN;
   }

   private void updateGlowBlurUniforms(CommandEncoder commandEncoder, int width, int height, float sigma) {
      GpuBuffer previousHorizontalBuffer = this.glowHorizontalUniforms;
      GpuBuffer previousVerticalBuffer = this.glowVerticalUniforms;
      this.glowHorizontalUniforms = this.ensureUniformBuffer(this.glowHorizontalUniforms, 48, UPDATE_GLOW_BLUR_UNIFORMS_ENTITY_ESP_GLOW_HORIZONTAL_BUFFER_LABEL);
      this.glowVerticalUniforms = this.ensureUniformBuffer(this.glowVerticalUniforms, 48, UPDATE_GLOW_BLUR_UNIFORMS_ENTITY_ESP_GLOW_VERTICAL_BUFFER_LABEL);
      if (previousHorizontalBuffer != this.glowHorizontalUniforms || previousVerticalBuffer != this.glowVerticalUniforms || this.cachedGlowBlurWidth != width || this.cachedGlowBlurHeight != height || Float.compare(this.cachedGlowBlurSigma, sigma) != 0) {
         float[] weights = new float[9];
         weights[0] = 1.0F;
         float normalizationSum = 1.0F;

         for (int sampleIndex = 1; sampleIndex < weights.length; sampleIndex++) {
            weights[sampleIndex] = (float)Math.exp(-sampleIndex * sampleIndex / (2.0F * sigma * sigma));
            normalizationSum += weights[sampleIndex] * 2.0F;
         }

         for (int normalizeIndex = 0; normalizeIndex < weights.length; normalizeIndex++) {
            weights[normalizeIndex] /= normalizationSum;
         }

         float pairWeight12 = weights[1] + weights[2];
         float pairWeight34 = weights[3] + weights[4];
         float pairWeight56 = weights[5] + weights[6];
         float pairWeight78 = weights[7] + weights[8];
         float pairOffset12 = 1.0F + weights[2] / Math.max(pairWeight12, ВнИ);
         float pairOffset34 = Внь + weights[4] / Math.max(pairWeight34, Внж);
         float pairOffset56 = Вн弟 + weights[6] / Math.max(pairWeight56, Внб);
         float pairOffset78 = ВнЫ + weights[8] / Math.max(pairWeight78, Внl);

         try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer horizontalData = Std140Builder.onStack(memoryStack, 48)
               .putVec4(1.0F / width, 0.0F, weights[0], 0.0F)
               .putVec4(pairOffset12, pairWeight12, pairOffset34, pairWeight34)
               .putVec4(pairOffset56, pairWeight56, pairOffset78, pairWeight78)
               .get();
            ByteBuffer verticalData = Std140Builder.onStack(memoryStack, 48)
               .putVec4(0.0F, 1.0F / height, weights[0], 0.0F)
               .putVec4(pairOffset12, pairWeight12, pairOffset34, pairWeight34)
               .putVec4(pairOffset56, pairWeight56, pairOffset78, pairWeight78)
               .get();
            commandEncoder.writeToBuffer(this.glowHorizontalUniforms.slice(), horizontalData);
            commandEncoder.writeToBuffer(this.glowVerticalUniforms.slice(), verticalData);
         }

         this.cachedGlowBlurWidth = width;
         this.cachedGlowBlurHeight = height;
         this.cachedGlowBlurSigma = sigma;
      }
   }

   private void ensurePreviewTargets(int width, int height) {
      if (this.previewMaskTarget == null || this.previewOutputTarget == null) {
         this.previewMaskTarget = new TextureTarget(ENSURE_PREVIEW_TARGETS_ENTITY_ESP_PREVIEW_SOURCE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         this.previewOutputTarget = new TextureTarget(ENSURE_PREVIEW_TARGETS_ENTITY_ESP_PREVIEW_OUTPUT_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.previewMaskTarget.width != width || this.previewMaskTarget.height != height) {
         this.previewMaskTarget.resize(width, height);
         this.previewOutputTarget.resize(width, height);
         this.invalidateUniformCaches();
      }
   }

   private boolean isFirstPersonCameraEntity(Entity entity) {
      return (entity == MINECRAFT_CLIENT.player || entity == MINECRAFT_CLIENT.getCameraEntity()) && MINECRAFT_CLIENT.options.getCameraType().isFirstPerson();
   }

   private void drawSceneBlur(CommandEncoder commandEncoder, RenderTarget inputTarget, RenderTarget outputTarget, GpuBuffer uniformBuffer) {
      GpuTextureView inputView = inputTarget.getColorTextureView();
      GpuTextureView outputView = outputTarget.getColorTextureView();
      if (inputView != null && !inputView.isClosed() && outputView != null && !outputView.isClosed()) {
         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> DRAW_SCENE_BLUR_ENTITY_ESP_MODEL_BLUR_PASS_LABEL, outputView, Optional.of(new Vector4f(0.0F)))) {
            renderPass.setPipeline(SCENE_BLUR_PIPELINE);
            renderPass.bindTexture(DRAW_SCENE_BLUR_IN_SAMPLER_SAMPLER_NAME, inputView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(DRAW_SCENE_BLUR_ESP_BLUR_UNIFORM_NAME, uniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private void beginSurfaceFrameIfNeeded() {
      if (!this.frameInitialized) {
         Camera camera = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         this.viewRotationProjectionMatrix = camera.isInitialized() ? camera.getViewRotationProjectionMatrix(new Matrix4f()) : null;
         if (this.viewRotationProjectionMatrix != null) {
            float projectionYRowX = this.viewRotationProjectionMatrix.m01();
            float projectionYRowY = this.viewRotationProjectionMatrix.m11();
            float projectionYRowZ = this.viewRotationProjectionMatrix.m21();
            this.distanceBucketProjectionScale = (float)Math.sqrt(projectionYRowX * projectionYRowX + projectionYRowY * projectionYRowY + projectionYRowZ * projectionYRowZ) * MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().height / GLOW_BUCKET_PROJECTION_DIVISOR;
         } else {
            this.distanceBucketProjectionScale = 1.0F;
         }

         Arrays.fill(this.activeDistanceBuckets, 0);

         for (float[][] categoryBounds : this.categoryBucketBounds) {
            for (float[] bucketBounds : categoryBounds) {
               bucketBounds[0] = bucketBounds[1] = 1.0F;
               bucketBounds[2] = bucketBounds[3] = 0.0F;
            }
         }

         this.anchorCount = 0;
         this.modelSurfaceReady = false;
         this.frameInitialized = true;
         this.glowSurfaceReady = false;
         this.needsGlowSurfaceThisFrame = false;
         this.hasRenderedEntitiesThisFrame = false;
         this.hasFillThisFrame = false;
         Arrays.fill(this.activeCategories, false);
         this.firstFillOpacity = 0.0F;
         this.firstFillColor = -1;
         this.firstFillMode = 1;
      }
   }

   public boolean canRender() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && this.hasActiveStyles();
   }

   public EntityEspSurface(Function<Entity, EntityEspStyleSettings> styleResolver, Supplier<? extends Collection<EntityEspStyleSettings>> stylesSupplier) {
      this.styleResolver = styleResolver;
      this.stylesSupplier = stylesSupplier;
      this.styles = List.copyOf((Collection<? extends EntityEspStyleSettings>)stylesSupplier.get());
      this.activeCategories = new boolean[this.styles.size()];
      this.activeDistanceBuckets = new int[this.styles.size()];
      this.categoryBucketBounds = new float[this.styles.size()][6][4];
   }

   private int getOutlineColor(EntityEspStyleSettings style) {
      return style.getUseThemeColors().getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : style.getOutlineColor().getValue();
   }

   private void drawModelOutline(RenderTarget maskTarget, RenderTarget outputTarget, float outlineWidth) {
      GpuTextureView maskView = maskTarget.getColorTextureView();
      GpuTextureView outputView = outputTarget.getColorTextureView();
      if (maskView != null && !maskView.isClosed() && outputView != null && !outputView.isClosed()) {
         CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
         this.updateOutlineUniforms(commandEncoder, maskTarget.width, maskTarget.height, outlineWidth);

         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> DRAW_MODEL_OUTLINE_ENTITY_ESP_MODEL_OUTLINE_PASS_LABEL, outputView, Optional.empty())) {
            renderPass.setPipeline(MODEL_OUTLINE_PIPELINE);
            renderPass.bindTexture(DRAW_MODEL_OUTLINE_IN_SAMPLER_SAMPLER_NAME, maskView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.setUniform(DRAW_MODEL_OUTLINE_ESP_GLOW_UNIFORM_NAME, this.outlineUniforms);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   public boolean compositeWorldEffects(RenderTarget maskTarget, RenderTarget outputTarget) {
      if (this.hasRenderedEntitiesThisFrame && maskTarget != null && outputTarget != null && maskTarget.getColorTextureView() != null && outputTarget.getColorTextureView() != null) {
         float fillOpacity = this.firstFillOpacity;
         int fillColor = this.firstFillColor;
         int fillMode = this.firstFillMode;
         boolean hasFill = this.hasFillThisFrame;

         try {
            if (hasFill) {
               this.drawModelFill(maskTarget, outputTarget, fillOpacity, fillColor, fillMode, false);
            }

            for (int styleIndex = 0; styleIndex < this.activeCategories.length; styleIndex++) {
               if (this.activeCategories[styleIndex]) {
                  EntityEspStyleSettings style = this.styles.get(styleIndex);
                  if (style.getOutlineEnabled().getValue() || style.getGlowEnabled().getValue()) {
                     this.extractCategoryMask(maskTarget, style, styleIndex);
                     float outlineWidth = style.getOutlineEnabled().getValue() ? style.getOutlineWidth().getValue() : 0.0F;
                     if (style.getGlowEnabled().getValue()) {
                        this.compositeDistanceBucketGlow(maskTarget, outputTarget, styleIndex, outlineWidth, style);
                     } else {
                        this.drawModelOutline(this.categoryMaskTarget, outputTarget, outlineWidth);
                     }
                  }
               }
            }

            return true;
         } finally {
            this.clearSurfaceFrame();
         }
      } else {
         this.clearSurfaceFrame();
         return false;
      }
   }

   @Override
   public void close() {
      this.clearSurfaceFrame();
      if (this.glowAccumulatorTarget != null) {
         this.glowAccumulatorTarget.destroyBuffers();
         this.glowAccumulatorTarget = null;
      }

      this.previewSurfaceReady = false;
      if (this.previewSurfaceTarget != null) {
         this.previewSurfaceTarget.destroyBuffers();
         this.previewSurfaceTarget = null;
      }

      if (this.previewSurfaceUniforms != null) {
         this.previewSurfaceUniforms.close();
         this.previewSurfaceUniforms = null;
      }

      if (this.glowSurfaceTarget != null) {
         this.glowSurfaceTarget.destroyBuffers();
         this.glowSurfaceTarget = null;
      }

      if (this.glowSurfaceUniforms != null) {
         this.glowSurfaceUniforms.close();
         this.glowSurfaceUniforms = null;
      }

      if (this.categoryMaskTarget != null) {
         this.categoryMaskTarget.destroyBuffers();
         this.categoryMaskTarget = null;
      }

      if (this.categoryMaskUniforms != null) {
         this.categoryMaskUniforms.close();
         this.categoryMaskUniforms = null;
      }

      if (this.surfaceAnchorUniforms != null) {
         this.surfaceAnchorUniforms.close();
         this.surfaceAnchorUniforms = null;
      }

      if (this.modelSurfaceTarget != null) {
         this.modelSurfaceTarget.destroyBuffers();
         this.modelSurfaceTarget = null;
      }

      if (this.sceneBlurHorizontalTarget != null) {
         this.sceneBlurHorizontalTarget.destroyBuffers();
         this.sceneBlurHorizontalTarget = null;
      }

      if (this.sceneBlurVerticalTarget != null) {
         this.sceneBlurVerticalTarget.destroyBuffers();
         this.sceneBlurVerticalTarget = null;
      }

      for (int chainIndex = 0; chainIndex < this.glowPyramidTargets.length; chainIndex++) {
         for (int levelIndex = 0; levelIndex < this.glowPyramidTargets[chainIndex].length; levelIndex++) {
            if (this.glowPyramidTargets[chainIndex][levelIndex] != null) {
               this.glowPyramidTargets[chainIndex][levelIndex].destroyBuffers();
            }

            if (this.glowBlurScratchTargets[chainIndex][levelIndex] != null) {
               this.glowBlurScratchTargets[chainIndex][levelIndex].destroyBuffers();
            }

            this.glowPyramidTargets[chainIndex][levelIndex] = null;
            this.glowBlurScratchTargets[chainIndex][levelIndex] = null;
         }
      }

      if (this.glowHorizontalUniforms != null) {
         this.glowHorizontalUniforms.close();
         this.glowHorizontalUniforms = null;
      }

      if (this.glowVerticalUniforms != null) {
         this.glowVerticalUniforms.close();
         this.glowVerticalUniforms = null;
      }

      if (this.previewMaskTarget != null) {
         this.previewMaskTarget.destroyBuffers();
         this.previewMaskTarget = null;
      }

      if (this.previewOutputTarget != null) {
         this.previewOutputTarget.destroyBuffers();
         this.previewOutputTarget = null;
      }

      if (this.sceneBlurHorizontalUniforms != null) {
         this.sceneBlurHorizontalUniforms.close();
         this.sceneBlurHorizontalUniforms = null;
      }

      if (this.sceneBlurVerticalUniforms != null) {
         this.sceneBlurVerticalUniforms.close();
         this.sceneBlurVerticalUniforms = null;
      }

      if (this.glowUniforms != null) {
         this.glowUniforms.close();
         this.glowUniforms = null;
      }

      if (this.outlineUniforms != null) {
         this.outlineUniforms.close();
         this.outlineUniforms = null;
      }

      if (this.fillUniforms != null) {
         this.fillUniforms.close();
         this.fillUniforms = null;
      }

      this.invalidateUniformCaches();
   }

   private void copyPreviewTexture(CommandEncoder commandEncoder, GpuTextureView inputTexture, TextureTarget target) {
      GpuTextureView colorView = target.getColorTextureView();
      if (colorView != null && !colorView.isClosed()) {
         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> COPY_PREVIEW_TEXTURE_ENTITY_ESP_PREVIEW_COPY_PASS_LABEL, colorView, Optional.of(new Vector4f(0.0F)))) {
            renderPass.setPipeline(MODEL_MASK_COPY_PIPELINE);
            renderPass.bindTexture(COPY_PREVIEW_TEXTURE_IN_SAMPLER_SAMPLER_NAME, inputTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private void renderPreviewSurface(CommandEncoder commandEncoder, GpuTextureView depthTexture, Matrix4f inverseModelViewProjection, float modelHeight, int width, int height) {
      if (depthTexture != null && !depthTexture.isClosed() && inverseModelViewProjection != null) {
         if (this.previewSurfaceTarget == null) {
            this.previewSurfaceTarget = new TextureTarget(RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         } else if (this.previewSurfaceTarget.width != width || this.previewSurfaceTarget.height != height) {
            this.previewSurfaceTarget.resize(width, height);
         }

         this.previewSurfaceUniforms = this.ensureUniformBuffer(this.previewSurfaceUniforms, 80, RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_BUFFER_LABEL);

         try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer uniformData = Std140Builder.onStack(memoryStack, 80).putMat4f(inverseModelViewProjection).putVec4(modelHeight, 0.0F, 0.0F, 0.0F).get();
            commandEncoder.writeToBuffer(this.previewSurfaceUniforms.slice(), uniformData);
         }

         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> RENDER_PREVIEW_SURFACE_ENTITY_ESP_PREVIEW_SURFACE_PASS_LABEL, this.previewSurfaceTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(PREVIEW_SURFACE_PIPELINE);
            renderPass.setUniform(RENDER_PREVIEW_SURFACE_PREVIEW_SURFACE_UNIFORM_NAME, this.previewSurfaceUniforms);
            renderPass.bindTexture(RENDER_PREVIEW_SURFACE_DEPTH_SAMPLER_SAMPLER_NAME, depthTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.bindTexture(RENDER_PREVIEW_SURFACE_MASK_SAMPLER_SAMPLER_NAME, this.previewMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.draw(3, 1, 0, 0);
         }

         this.previewSurfaceReady = true;
      }
   }

   private void extractCategoryMask(RenderTarget maskTarget, EntityEspStyleSettings style, int styleIndex) {
      if (this.categoryMaskTarget == null) {
         this.categoryMaskTarget = new TextureTarget(EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_TARGET_LABEL, maskTarget.width, maskTarget.height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.categoryMaskTarget.width != maskTarget.width || this.categoryMaskTarget.height != maskTarget.height) {
         this.categoryMaskTarget.resize(maskTarget.width, maskTarget.height);
      }

      CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
      this.categoryMaskUniforms = this.ensureUniformBuffer(this.categoryMaskUniforms, 32, EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_BUFFER_LABEL);
      int outlineColor = this.getOutlineColor(style);

      try (MemoryStack memoryStack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(memoryStack, 32).putVec4(normalizeColorChannel(outlineColor, 16), normalizeColorChannel(outlineColor, 8), normalizeColorChannel(outlineColor, 0), 1.0F).putVec4(styleIndex + 1, 0.0F, 0.0F, 0.0F).get();
         commandEncoder.writeToBuffer(this.categoryMaskUniforms.slice(), uniformData);
      }

      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> EXTRACT_CATEGORY_MASK_ENTITY_ESP_CATEGORY_MASK_PASS_LABEL, this.categoryMaskTarget.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(CATEGORY_MASK_PIPELINE);
         renderPass.bindTexture(EXTRACT_CATEGORY_MASK_IN_SAMPLER_SAMPLER_NAME, maskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.setUniform(EXTRACT_CATEGORY_MASK_CATEGORY_MASK_UNIFORM_NAME, this.categoryMaskUniforms);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private static RenderPipeline createPyramidPipeline(String shaderName) {
      return RenderPipelineBuilder.builder()
         .withLocation("rose/" + shaderName)
         .withVertexShader(ClientResourceIds.getCoreId(CREATE_PYRAMID_PIPELINE_HANDS_FULLSCREEN_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(shaderName))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withColorTargetFormat(GpuFormat.RGBA16_FLOAT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(CREATE_PYRAMID_PIPELINE_IN_SAMPLER_SAMPLER_NAME)
         .build();
   }

   private void compositeGlow(RenderTarget maskTarget, RenderTarget outputTarget, RenderTarget bloomTarget, boolean preview) {
      CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
      GpuTextureView outputView = outputTarget.getColorTextureView();
      GpuTextureView maskView = maskTarget.getColorTextureView();
      GpuTextureView bloomView = bloomTarget.getColorTextureView();

      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> COMPOSITE_GLOW_ENTITY_ESP_MODEL_GLOW_PASS_LABEL, outputView, Optional.empty())) {
         renderPass.setPipeline(preview ? PREVIEW_GLOW_COMPOSITE_PIPELINE : WORLD_GLOW_COMPOSITE_PIPELINE);
         renderPass.bindTexture(COMPOSITE_GLOW_BLOOM_SAMPLER_SAMPLER_NAME, bloomView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.bindTexture(COMPOSITE_GLOW_MASK_SAMPLER_SAMPLER_NAME, maskView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.setUniform(COMPOSITE_GLOW_ESP_OUTER_GLOW_UNIFORM_NAME, this.glowUniforms);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private boolean isCameraInsideEntity(Entity entity) {
      Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      return entity.getBoundingBox().inflate(CAMERA_INTERSECTION_PADDING).contains(cameraPosition);
   }

   private void updateOutlineUniforms(CommandEncoder commandEncoder, int width, int height, float outlineWidth) {
      GpuBuffer previousBuffer = this.outlineUniforms;
      this.outlineUniforms = this.ensureUniformBuffer(this.outlineUniforms, 96, UPDATE_OUTLINE_UNIFORMS_ENTITY_ESP_MODEL_OUTLINE_UNIFORMS_BUFFER_LABEL);
      boolean requiresUpdate = previousBuffer != this.outlineUniforms || this.cachedOutlineTargetWidth != width || this.cachedOutlineTargetHeight != height || Float.compare(this.cachedOutlineWidth, outlineWidth) != 0;
      if (requiresUpdate) {
         try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer uniformData = Std140Builder.onStack(memoryStack, 96)
               .putVec4(0.0F, 0.0F, outlineWidth, 0.0F)
               .putVec4(1.0F, Вню, 2.0F, 0.0F)
               .putVec4(1.0F, 1.0F, 1.0F, 1.0F)
               .putVec4(1.0F, 1.0F, 1.0F, 1.0F)
               .putVec4(0.0F, 0.0F, 0.0F, 0.0F)
               .putVec2(width, height)
               .get();
            commandEncoder.writeToBuffer(this.outlineUniforms.slice(), uniformData);
         }

         this.cachedOutlineTargetWidth = width;
         this.cachedOutlineTargetHeight = height;
         this.cachedOutlineWidth = outlineWidth;
      }
   }

   private GpuBuffer ensureUniformBuffer(GpuBuffer buffer, int sizeBytes, String label) {
      return buffer != null && !buffer.isClosed() ? buffer : RenderSystem.getDevice().createBuffer(() -> label, 136, sizeBytes);
   }

   private void clearSurfaceFrame() {
      this.viewRotationProjectionMatrix = null;
      Arrays.fill(this.activeDistanceBuckets, 0);
      this.anchorCount = 0;
      this.modelSurfaceReady = false;
      this.frameInitialized = false;
      this.glowSurfaceReady = false;
      this.needsGlowSurfaceThisFrame = false;
      this.hasRenderedEntitiesThisFrame = false;
      this.hasFillThisFrame = false;
      Arrays.fill(this.activeCategories, false);
      this.firstFillOpacity = 0.0F;
      this.firstFillColor = -1;
      this.firstFillMode = 1;
   }

   public void captureEntitySurfaces(PreparedRenderType preparedType, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int baseVertex, int firstIndex, int indexCount) {
      if (this.needsGlowSurfaceThisFrame) {
         this.captureGlowSurface(preparedType, vertexBuffer, indexBuffer, indexType, baseVertex, firstIndex, indexCount);
      }

      if (this.hasFillThisFrame && (this.firstFillMode == 2 || this.firstFillMode == 3)) {
         if (!this.modelSurfaceReady) {
            this.updateSurfaceAnchorUniforms();
         }

         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (this.modelSurfaceTarget == null) {
            this.modelSurfaceTarget = new TextureTarget(CAPTURE_ENTITY_SURFACES_ENTITY_ESP_SURFACE_TARGET_LABEL, mainTarget.width, mainTarget.height, true, GpuFormat.RGBA8_UNORM);
         } else if (this.modelSurfaceTarget.width != mainTarget.width || this.modelSurfaceTarget.height != mainTarget.height) {
            this.modelSurfaceTarget.resize(mainTarget.width, mainTarget.height);
            this.modelSurfaceReady = false;
         }

         GpuTextureView colorView = this.modelSurfaceTarget.getColorTextureView();
         GpuTextureView depthView = this.modelSurfaceTarget.getDepthTextureView();
         if (colorView != null && !colorView.isClosed() && depthView != null && !depthView.isClosed()) {
            try (RenderPass renderPass = RenderSystem.getDevice()
                  .createCommandEncoder()
                  .createRenderPass(
                     () -> CAPTURE_ENTITY_SURFACES_ENTITY_ESP_SURFACE_PASS_LABEL,
                     colorView,
                     this.modelSurfaceReady ? Optional.empty() : Optional.of(new Vector4f(0.0F)),
                     depthView,
                     this.modelSurfaceReady ? OptionalDouble.empty() : OptionalDouble.of(0.0)
                  )) {
               renderPass.setPipeline(MODEL_SURFACE_PIPELINE);
               ScissorState scissor = preparedType.scissorState();
               if (scissor.enabled()) {
                  renderPass.enableScissor(scissor.x(), scissor.y(), scissor.width(), scissor.height());
               }

               RenderSystem.bindDefaultUniforms(renderPass);
               renderPass.setUniform(CAPTURE_ENTITY_SURFACES_DYNAMIC_TRANSFORMS_UNIFORM_NAME, preparedType.dynamicTransforms());
               renderPass.setUniform(CAPTURE_ENTITY_SURFACES_ESP_SURFACE_ANCHORS_UNIFORM_NAME, this.surfaceAnchorUniforms);
               renderPass.setVertexBuffer(0, vertexBuffer.slice());
               renderPass.setIndexBuffer(indexBuffer, indexType);
               preparedType.textures().forEach(textureBinding -> renderPass.bindTexture(textureBinding.name(), textureBinding.textureView(), textureBinding.sampler()));
               renderPass.drawIndexed(indexCount, 1, firstIndex, baseVertex, 0);
            }

            this.modelSurfaceReady = true;
         }
      }
   }

   public boolean hasRenderedEntities() {
      return this.hasRenderedEntitiesThisFrame;
   }

   private void drawModelFill(RenderTarget maskTarget, RenderTarget outputTarget, float opacity, int fillColor, int fillMode, boolean preview) {
      if (!(opacity <= MIN_FILL_OPACITY)) {
         GpuTextureView maskView = maskTarget.getColorTextureView();
         GpuTextureView outputView = outputTarget.getColorTextureView();
         if (maskView != null && !maskView.isClosed() && outputView != null && !outputView.isClosed()) {
            CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
            GpuTextureView sceneView = maskView;
            if (fillMode == 4) {
               int blurWidth = Math.max(1, outputTarget.width / 2);
               int blurHeight = Math.max(1, outputTarget.height / 2);
               this.ensureSceneBlurTargets(blurWidth, blurHeight);
               this.updateSceneBlurUniforms(commandEncoder, blurWidth, blurHeight, ВН0);
               this.drawSceneBlur(commandEncoder, outputTarget, this.sceneBlurHorizontalTarget, this.sceneBlurHorizontalUniforms);
               this.drawSceneBlur(commandEncoder, this.sceneBlurHorizontalTarget, this.sceneBlurVerticalTarget, this.sceneBlurVerticalUniforms);
               sceneView = this.sceneBlurVerticalTarget.getColorTextureView();
               if (sceneView == null || sceneView.isClosed()) {
                  return;
               }
            }

            this.fillUniforms = this.ensureUniformBuffer(this.fillUniforms, 80, DRAW_MODEL_FILL_ENTITY_ESP_MODEL_FILL_UNIFORMS_BUFFER_LABEL);
            GpuTextureView surfaceView = !preview && this.modelSurfaceReady && this.modelSurfaceTarget != null ? this.modelSurfaceTarget.getColorTextureView() : null;
            boolean hasSurface = surfaceView != null && !surfaceView.isClosed();

            try (MemoryStack memoryStack = MemoryStack.stackPush()) {
               float red = normalizeColorChannel(fillColor, 16);
               float green = normalizeColorChannel(fillColor, 8);
               float blue = normalizeColorChannel(fillColor, 0);
               Std140Builder uniformBuilder = Std140Builder.onStack(memoryStack, 80)
                  .putVec4(Math.clamp(opacity, 0.0F, 1.0F), preview ? 0.0F : 1.0F, 0.0F, fillMode)
                  .putVec4(red, green, blue, 1.0F)
                  .putVec4(red, green, blue, 1.0F)
                  .putVec4(red, green, blue, 1.0F)
                  .putVec4(maskTarget.width, maskTarget.height, hasSurface ? 1.0F : 0.0F, (float)(System.currentTimeMillis() % FILL_ANIMATION_PERIOD_MILLIS) / MILLIS_PER_SECOND);
               commandEncoder.writeToBuffer(this.fillUniforms.slice(), uniformBuilder.get());
            }

            try (RenderPass renderPass = commandEncoder.createRenderPass(() -> DRAW_MODEL_FILL_ENTITY_ESP_MODEL_FILL_PASS_LABEL, outputView, Optional.empty())) {
               renderPass.setPipeline(MODEL_FILL_PIPELINE);
               renderPass.bindTexture(DRAW_MODEL_FILL_MASK_SAMPLER_SAMPLER_NAME, maskView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               renderPass.bindTexture(DRAW_MODEL_FILL_SCENE_SAMPLER_SAMPLER_NAME, sceneView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.bindTexture(DRAW_MODEL_FILL_SURFACE_SAMPLER_SAMPLER_NAME, hasSurface ? surfaceView : maskView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               renderPass.setUniform(DRAW_MODEL_FILL_ESP_CHAMS_FILL_UNIFORM_NAME, this.fillUniforms);
               renderPass.draw(3, 1, 0, 0);
            }
         }
      }
   }

   private void drawModelGlow(RenderTarget maskTarget, RenderTarget outputTarget, float radius, float innerGlow, float outlineWidth, EntityEspStyleSettings style, boolean preview) {
      TextureTarget filteredGlow = this.filterModelGlow(maskTarget, outputTarget, radius, innerGlow, outlineWidth, style, preview, maskTarget, -1, -1, null);
      if (filteredGlow != null) {
         this.compositeGlow(maskTarget, outputTarget, filteredGlow, preview);
      }
   }

   private void ensureSceneBlurTargets(int width, int height) {
      if (this.sceneBlurHorizontalTarget == null || this.sceneBlurVerticalTarget == null) {
         this.sceneBlurHorizontalTarget = new TextureTarget(ENSURE_SCENE_BLUR_TARGETS_ENTITY_ESP_MODEL_BLUR_HORIZONTAL_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         this.sceneBlurVerticalTarget = new TextureTarget(ENSURE_SCENE_BLUR_TARGETS_ENTITY_ESP_MODEL_BLUR_VERTICAL_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         this.invalidateUniformCaches();
      } else if (this.sceneBlurHorizontalTarget.width != width || this.sceneBlurHorizontalTarget.height != height) {
         this.sceneBlurHorizontalTarget.resize(width, height);
         this.sceneBlurVerticalTarget.resize(width, height);
         this.invalidateUniformCaches();
      }
   }

   private TextureTarget filterModelGlow(
      RenderTarget maskTarget,
      RenderTarget outputTarget,
      float radius,
      float innerGlow,
      float outlineWidth,
      EntityEspStyleSettings style,
      boolean preview,
      RenderTarget radiusMaskTarget,
      int bucketIndex,
      int styleIndex,
      float[] normalizedBounds
   ) {
      GpuTextureView maskView = maskTarget.getColorTextureView();
      GpuTextureView outputView = outputTarget.getColorTextureView();
      if (maskView != null && !maskView.isClosed() && outputView != null && !outputView.isClosed()) {
         radius = Math.clamp(radius, preview ? 1.0F : ВНя, Внс);
         int deepestLevel = radius > ВнВ ? 2 : (radius > ВнЯ ? 1 : 0);
         this.ensureGlowPyramidTargets(maskTarget.width, maskTarget.height, deepestLevel, preview);
         TextureTarget[] downsampleTargets = this.glowPyramidTargets[preview ? 1 : 0];
         TextureTarget blurScratchTarget = this.glowBlurScratchTargets[preview ? 1 : 0][deepestLevel];
         GpuTextureView seedView = downsampleTargets[0].getColorTextureView();
         CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
         float downsampleFactor = 2 << deepestLevel;
         float varianceOffset = deepestLevel == 2 ? ВнХ : (deepestLevel == 1 ? Вне : 0.0F);
         float sigma = (float)Math.sqrt(Math.max(preview ? ВнА : Вн4, (radius * radius * Внп - varianceOffset) / (downsampleFactor * downsampleFactor)));
         this.updateGlowBlurUniforms(commandEncoder, downsampleTargets[deepestLevel].width, downsampleTargets[deepestLevel].height, sigma);
         this.updateGlowUniforms(commandEncoder, maskTarget.width, maskTarget.height, innerGlow, outlineWidth, style, preview, bucketIndex, styleIndex, Math.min(1.0F, radius));

         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> FILTER_MODEL_GLOW_ENTITY_ESP_MODEL_MASK_PASS_LABEL, seedView, normalizedBounds != null ? Optional.of(new Vector4f(0.0F)) : Optional.empty())) {
            renderPass.setPipeline(GLOW_SEED_PIPELINE);
            renderPass.bindTexture(FILTER_MODEL_GLOW_MASK_SAMPLER_SAMPLER_NAME, maskView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.bindTexture(FILTER_MODEL_GLOW_RADIUS_SAMPLER_SAMPLER_NAME, radiusMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            GpuTextureView surfaceView = preview ? (this.previewSurfaceReady ? this.previewSurfaceTarget.getColorTextureView() : maskView) : (this.glowSurfaceReady ? this.glowSurfaceTarget.getColorTextureView() : maskView);
            renderPass.bindTexture(FILTER_MODEL_GLOW_SURFACE_SAMPLER_SAMPLER_NAME, surfaceView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(FILTER_MODEL_GLOW_ESP_OUTER_GLOW_UNIFORM_NAME, this.glowUniforms);
            this.applyNormalizedScissor(renderPass, downsampleTargets[0], normalizedBounds);
            renderPass.draw(3, 1, 0, 0);
         }

         for (int downsampleLevel = 1; downsampleLevel <= deepestLevel; downsampleLevel++) {
            this.drawGlowFilterPass(commandEncoder, downsampleTargets[downsampleLevel - 1], downsampleTargets[downsampleLevel], GLOW_DOWNSAMPLE_PIPELINE, null, normalizedBounds);
         }

         this.drawGlowFilterPass(commandEncoder, downsampleTargets[deepestLevel], blurScratchTarget, GLOW_BLUR_PIPELINE, this.glowHorizontalUniforms, normalizedBounds);
         this.drawGlowFilterPass(commandEncoder, blurScratchTarget, downsampleTargets[deepestLevel], GLOW_BLUR_PIPELINE, this.glowVerticalUniforms, normalizedBounds);

         for (int upsampleLevel = deepestLevel - 1; upsampleLevel >= 0; upsampleLevel--) {
            this.drawGlowFilterPass(commandEncoder, downsampleTargets[upsampleLevel + 1], downsampleTargets[upsampleLevel], GLOW_UPSAMPLE_PIPELINE, null, normalizedBounds);
         }

         return downsampleTargets[0];
      } else {
         return null;
      }
   }

   private void drawGlowFilterPass(CommandEncoder commandEncoder, RenderTarget inputTarget, RenderTarget outputTarget, RenderPipeline pipeline, GpuBuffer uniformBuffer, float[] normalizedBounds) {
      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> DRAW_GLOW_FILTER_PASS_ENTITY_ESP_GLOW_FILTER_PASS_LABEL, outputTarget.getColorTextureView(), normalizedBounds != null ? Optional.of(new Vector4f(0.0F)) : Optional.empty())) {
         renderPass.setPipeline(pipeline);
         renderPass.bindTexture(DRAW_GLOW_FILTER_PASS_IN_SAMPLER_SAMPLER_NAME, inputTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         if (uniformBuffer != null) {
            renderPass.setUniform(DRAW_GLOW_FILTER_PASS_GLOW_BLUR_UNIFORM_NAME, uniformBuffer);
         }

         this.applyNormalizedScissor(renderPass, outputTarget, normalizedBounds);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   public boolean compositePreviewEffects(RenderTarget maskTarget, RenderTarget outputTarget, EntityEspStyleSettings style) {
      this.previewSurfaceReady = false;
      if (style == null || !this.hasSurfaceEffects(style)) {
         return false;
      }

      if (maskTarget != null && outputTarget != null && maskTarget.getColorTextureView() != null && outputTarget.getColorTextureView() != null) {
         if (style.getFillEnabled().getValue()) {
            this.drawModelFill(maskTarget, outputTarget, style.getFillOpacity().getValue(), this.getFillColor(style), this.getFillModeCode(style), true);
         }

         if (style.getOutlineEnabled().getValue() || style.getGlowEnabled().getValue()) {
            if (style.getGlowEnabled().getValue()) {
               this.drawModelGlow(maskTarget, outputTarget, style.getGlowRadius().getValue(), style.getInnerGlow().getValue(), style.getOutlineEnabled().getValue() ? style.getOutlineWidth().getValue() : 0.0F, style, true);
            } else {
               this.drawModelOutline(maskTarget, outputTarget, style.getOutlineWidth().getValue());
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void resetFrame() {
      this.clearSurfaceFrame();
   }

   private void updateGlowUniforms(CommandEncoder commandEncoder, int width, int height, float innerGlow, float outlineWidth, EntityEspStyleSettings style, boolean preview, int bucketIndex, int styleIndex, float radiusWeight) {
      GpuBuffer previousBuffer = this.glowUniforms;
      this.glowUniforms = this.ensureUniformBuffer(this.glowUniforms, 144, UPDATE_GLOW_UNIFORMS_ENTITY_ESP_MODEL_GLOW_UNIFORMS_BUFFER_LABEL);
      int glowStyleCode = style.getGlowStyle().isSelected(ENERGY_GLOW_STYLE_OPTION) ? 3 : (style.getGlowStyle().isSelected(SMOKY_GLOW_STYLE_OPTION) ? 2 : (style.getGlowStyle().isSelected(SOFT_NEON_GLOW_STYLE_OPTION) ? 1 : 0));
      boolean twoColors = style.getTwoColorGlow().getValue();
      boolean useThemeColors = style.getUseThemeColors().getValue();
      int primaryColor = useThemeColors ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : style.getFirstGlowColor().getValue();
      int secondaryColor = useThemeColors ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : style.getSecondGlowColor().getValue();
      int outlineColor = this.getOutlineColor(style);
      float auraSpeed = style.getAuraSpeed().getValue();
      float blendSpeed = style.getGlowBlendSpeed().getValue();
      float flowSpeed = style.getFlowSpeed().getValue();
      float elapsedSeconds = (glowStyleCode != 2 || !(auraSpeed > 0.0F)) && (glowStyleCode != 3 || !(flowSpeed > 0.0F)) && (!twoColors || !(blendSpeed > 0.0F))
         ? 0.0F
         : (float)(System.nanoTime() - this.animationStartNanos) / NANOS_PER_SECOND;

      try (MemoryStack memoryStack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(memoryStack, 144)
            .putVec4(style.getGlowIntensity().getValue(), 2.0F, Внл, innerGlow)
            .putVec4(normalizeColorChannel(primaryColor, 16), normalizeColorChannel(primaryColor, 8), normalizeColorChannel(primaryColor, 0), normalizeColorChannel(primaryColor, 24))
            .putVec4(normalizeColorChannel(secondaryColor, 16), normalizeColorChannel(secondaryColor, 8), normalizeColorChannel(secondaryColor, 0), normalizeColorChannel(secondaryColor, 24))
            .putVec4(normalizeColorChannel(outlineColor, 16), normalizeColorChannel(outlineColor, 8), normalizeColorChannel(outlineColor, 0), 1.0F)
            .putVec4(width, height, outlineWidth, (preview ? !this.previewSurfaceReady : !this.glowSurfaceReady) ? 0.0F : 1.0F)
            .putVec4(glowStyleCode, style.getGlowSoftness().getValue(), elapsedSeconds, auraSpeed)
            .putVec4(style.getAuraSwirlScale().getValue(), style.getAuraDensity().getValue(), twoColors ? 1.0F : 0.0F, blendSpeed)
            .putVec4(flowSpeed, style.getWaveWidth().getValue(), style.getWaveBend().getValue(), preview ? 1.0F : 0.0F)
            .putVec4(bucketIndex, bucketIndex >= 0 ? 1.0F : 0.0F, radiusWeight, styleIndex + 1)
            .get();
         if (previousBuffer == this.glowUniforms && this.cachedGlowUniformData.equals(uniformData)) {
            return;
         }

         this.cachedGlowUniformData.clear().put(uniformData).flip();
         uniformData.rewind();
         commandEncoder.writeToBuffer(this.glowUniforms.slice(), uniformData);
      }
   }

   private void updateSurfaceAnchorUniforms() {
      this.surfaceAnchorUniforms = this.ensureUniformBuffer(this.surfaceAnchorUniforms, 1056, UPDATE_SURFACE_ANCHOR_UNIFORMS_ENTITY_ESP_SURFACE_ANCHORS_BUFFER_LABEL);
      Matrix4f viewMatrix = WorldProjection.getViewMatrix();
      Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      Vector4f viewPosition = new Vector4f();
      Vector4f worldUpInView = new Vector4f(0.0F, 1.0F, 0.0F, 0.0F);
      if (viewMatrix != null) {
         worldUpInView.mul(viewMatrix);
      }

      try (MemoryStack memoryStack = MemoryStack.stackPush()) {
         Std140Builder uniformBuilder = Std140Builder.onStack(memoryStack, 1056)
            .putVec4(viewMatrix == null ? 0.0F : this.anchorCount, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().width, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().height, 0.0F)
            .putVec4(worldUpInView.x, worldUpInView.y, worldUpInView.z, 0.0F);

         for (int anchorIndex = 0; anchorIndex < 64; anchorIndex++) {
            if (viewMatrix != null && anchorIndex < this.anchorCount) {
               double[] anchor = this.worldAnchors[anchorIndex];
               viewPosition.set((float)(anchor[0] - cameraPosition.x), (float)(anchor[1] - cameraPosition.y), (float)(anchor[2] - cameraPosition.z), 1.0F).mul(viewMatrix);
               uniformBuilder.putVec4(viewPosition.x, viewPosition.y, viewPosition.z, (float)anchor[3]);
            } else {
               uniformBuilder.putVec4(0.0F, 0.0F, 0.0F, 1.0F);
            }
         }

         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.surfaceAnchorUniforms.slice(), uniformBuilder.get());
      }
   }

   private boolean hasActiveStyles() {
      Collection<EntityEspStyleSettings> styles = this.stylesSupplier.get();
      if (styles != null && !styles.isEmpty()) {
         for (EntityEspStyleSettings style : styles) {
            if (this.isStyleActive(style)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean shouldUseCustomOutline() {
      return this.canRender();
   }
}
