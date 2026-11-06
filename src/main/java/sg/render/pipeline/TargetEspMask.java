package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import settings.render.TargetRingGeometry;
import sg.ec.MinecraftAccess;
import sg.ec.GlowQuadRendering;
import sg.events.impl.WorldEffectsEvent;
import sg.mixin.RenderPipelinesAccessor;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class TargetEspMask implements MinecraftAccess {
   private static final Identifier TARGET_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.TARGET_TEXTURE_NAMESPACE, TargetEspMask.TARGET_TEXTURE_PATH);
   private static final Identifier TARGET_ALTERNATE_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.TARGET_ALTERNATE_TEXTURE_NAMESPACE, TargetEspMask.TARGET_ALTERNATE_TEXTURE_PATH);
   private static final Identifier GHOST_FIRE_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.GHOST_FIRE_TEXTURE_NAMESPACE, TargetEspMask.GHOST_FIRE_TEXTURE_PATH);
   private static final Identifier GHOST_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.GHOST_TEXTURE_NAMESPACE, TargetEspMask.GHOST_TEXTURE_PATH);
   private static final Identifier WORLD_BLOOM_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.WORLD_BLOOM_TEXTURE_NAMESPACE, TargetEspMask.WORLD_BLOOM_TEXTURE_PATH);
   private static final Identifier GHOST_PARTICLE_BLOOM_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.GHOST_PARTICLE_BLOOM_TEXTURE_NAMESPACE, TargetEspMask.GHOST_PARTICLE_BLOOM_TEXTURE_PATH);
   private static final Identifier CRYSTAL_PARTICLE_BLOOM_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.CRYSTAL_PARTICLE_BLOOM_TEXTURE_NAMESPACE, TargetEspMask.CRYSTAL_PARTICLE_BLOOM_TEXTURE_PATH);
   private static final Identifier TARGET_GLOW_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.TARGET_GLOW_TEXTURE_NAMESPACE, TargetEspMask.TARGET_GLOW_TEXTURE_PATH);
   private static final Identifier LIGHTNING_ARC_TEXTURE = Identifier.fromNamespaceAndPath(TargetEspMask.LIGHTNING_ARC_TEXTURE_NAMESPACE, TargetEspMask.LIGHTNING_ARC_TEXTURE_PATH);
   private static final RenderPipeline TARGET_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.TARGET_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.TARGET_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.TARGET_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.TARGET_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline GHOST_NO_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.GHOST_NO_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.GHOST_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.GHOST_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(true)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.GHOST_NO_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline GHOST_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.GHOST_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.GHOST_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.GHOST_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(true)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.GHOST_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline CRYSTAL_COLOR_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.CRYSTAL_COLOR_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_COLOR_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_COLOR_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(true)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline CRYSTAL_COLOR_NO_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.CRYSTAL_COLOR_NO_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_COLOR_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_COLOR_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(true)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline CRYSTAL_GLOW_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.CRYSTAL_GLOW_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_GLOW_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.CRYSTAL_GLOW_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.CRYSTAL_GLOW_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline ORBIT_CRYSTAL_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.ORBIT_CRYSTAL_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_CRYSTAL_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_CRYSTAL_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.TRIANGLES)
      .build();
   private static final RenderPipeline ORBIT_CRYSTAL_NO_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.TRIANGLES)
      .build();
   private static final RenderPipeline ORBIT_GLOW_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.ORBIT_GLOW_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_GLOW_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_GLOW_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.ORBIT_GLOW_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline ORBIT_GLOW_NO_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.ORBIT_GLOW_NO_DEPTH_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_GLOW_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.ORBIT_GLOW_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.ORBIT_GLOW_NO_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline RING_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.RING_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.RING_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.RING_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline HOOP_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.HOOP_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.HOOP_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.HOOP_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline RING_WAVE_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.RING_WAVE_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.RING_WAVE_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.RING_WAVE_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.OVERLAY)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline CUBE_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.CUBE_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.CUBE_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.CUBE_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline CUBE_COMPOSITE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(TargetEspMask.CUBE_COMPOSITE_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.CUBE_COMPOSITE_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.CUBE_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withSampler(TargetEspMask.CUBE_COMPOSITE_PIPELINE_IN_SAMPLER_NAME)
      .build();
   private static final RenderPipeline LIGHTNING_ARC_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.LIGHTNING_ARC_PIPELINE_LOCATION_ID)
      .withDepthBias(1.0F, 1.0F)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_ARC_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_ARC_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.LIGHTNING_ARC_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline LIGHTNING_ARC_DEPTH_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.LIGHTNING_ARC_DEPTH_PIPELINE_LOCATION_ID)
      .withDepthBias(1.0F, 1.0F)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_ARC_DEPTH_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_ARC_DEPTH_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(true)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .withSampler(TargetEspMask.LIGHTNING_ARC_DEPTH_PIPELINE_SAMPLER0_NAME)
      .build();
   private static final RenderPipeline LIGHTNING_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.LIGHTNING_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final RenderPipeline LIGHTNING_CORE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(TargetEspMask.LIGHTNING_CORE_PIPELINE_LOCATION_ID)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_CORE_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.LIGHTNING_CORE_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withSampler(TargetEspMask.LIGHTNING_CORE_PIPELINE_IN_SAMPLER_NAME)
      .build();
   private static final RenderPipeline TRIANGLE_LINES_PIPELINE = RenderPipelineBuilder.builder(RenderPipelinesAccessor.getPositionColorSnippet())
      .withLocation(TargetEspMask.TRIANGLE_LINES_PIPELINE_LOCATION_ID)
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.DEBUG_LINES)
      .build();
   private static final RenderPipeline PENTAGRAM_PIPELINE = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
      .withLocation(TargetEspMask.PENTAGRAM_PIPELINE_LOCATION_ID)
      .withUniform(TargetEspMask.PENTAGRAM_PIPELINE_GLOBALS_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
      .withVertexShader(ClientResourceIds.getCoreId(TargetEspMask.PENTAGRAM_PIPELINE_VERTEX_SHADER_ID))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetEspMask.PENTAGRAM_PIPELINE_FRAGMENT_SHADER_ID))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.ADDITIVE)
      .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
      .build();
   private static final float ХЪ = 0.82F;
   private static final float Хт = 0.7F;
   private static LivingEntity fadingTarget;
   private static float targetVisibility;
   private static float effectElapsedSeconds;
   private static long lastRenderMillis;
   private static String previousStyle = "";
   private static final List<Vector3f> crystalType2Offsets = new ArrayList<>();
   private static final float[][] OCTAHEDRON_VERTICES = new float[][]{
      {0.0F, TargetEspMask.field_07, 0.0F},
      {0.0F, TargetEspMask.field_0Р, 0.0F},
      {1.0F, 0.0F, 0.0F},
      {TargetEspMask.field_03, 0.0F, 0.0F},
      {0.0F, 0.0F, 1.0F},
      {0.0F, 0.0F, TargetEspMask.field_0р}
   };
   private static final int[][] OCTAHEDRON_FACES = new int[][]{{0, 4, 2}, {0, 3, 4}, {0, 5, 3}, {0, 2, 5}, {1, 2, 4}, {1, 4, 3}, {1, 3, 5}, {1, 5, 2}};
   private static final float[] OCTAHEDRON_FACE_SHADING = new float[]{
      1.0F,
      TargetEspMask.field_06,
      TargetEspMask.field_0_,
      TargetEspMask.field_0Ш,
      TargetEspMask.field_0ы,
      TargetEspMask.field_0й,
      TargetEspMask.field_0Ж,
      TargetEspMask.field_0м
   };
   private static int crystalType2EntityId = TargetEspMask.field_0К;
   private static float crystalType2Radius;
   private static float crystalType2Height;
   private static final List<Vector3f> crystalType1Offsets = new ArrayList<>();
   private static int crystalType1EntityId = TargetEspMask.field_0I;
   private static final Random EFFECT_RANDOM = new Random();
   private static final List<TargetEspMask.LightningEffect> lightningEffects = new ArrayList<>();
   private static long lastLightningSpawnMillis;
   private static TextureTarget lightningMask;
   private static final LinkedList<Vec3>[] ghostTrails = new LinkedList[]{
      new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList()
   };
   private static final List<TargetEspMask.FadingOrbitParticle> fadingOrbitParticles = new ArrayList<>();
   private static double orbitEmissionAngle;
   private static final long GHOST_ANIMATION_START_MILLIS = System.currentTimeMillis();
   private static float ghostType5HurtSpeedBoost;
   private static long lastGhostType5UpdateNanos = System.nanoTime();
   private static long lastOrbitParticleEmissionMillis;
   private static int previousPrimaryColor = -1;
   private static int previousSecondaryColor = TargetEspMask.field_0Т;
   private static float effectSpeed = TargetEspMask.field_0О;
   private static String ringType = TargetEspMask.INITIAL_RING_TYPE_LABEL;
   private static String squareType = TargetEspMask.INITIAL_SQUARE_TYPE_LABEL;
   private static final double[] CIRCLE_COSINES = createCircleLookup(true);
   private static final double[] CIRCLE_SINES = createCircleLookup(false);
   private static final int Ян = 3;
   private static final int ЯЦ = 20;
   private static final float ХЬ = 1.0F;
   private static final double св_3f39d5c3 = 0.0035;
   private static final double съ_3f39d5c3 = 0.1;
   private static final float Х衣 = 1.0F;
   private static final double сМ_3f39d5c3 = 1.15;
   private static final double сН_3f39d5c3 = 9.0;
   private static final float Хш = 0.18F;
   private static final double сн_3f39d5c3 = 1.7;
   private static final float ХИ = 0.25F;
   private static final float Хь = 0.22F;
   private static final float Хж = 0.12F;
   private static final int Яч = 26;
   private static final double сЦ_3f39d5c3 = 3.1;
   private static float ghostHurtCycleProgress = 1.0F;
   private static float ghostHurtPulse;
   private static double ghostOrbitalPhase;
   private static boolean ghostHurtLatched;
   private static int ghostPulseEntityId = TargetEspMask.field_0к;
   private static int ghostSegments = 3;
   private static boolean hurtRecolorEnabled = true;
   private static String ghostType = TargetEspMask.INITIAL_GHOST_TYPE_LABEL;
   private static long lastCrystalType4UpdateMillis;
   private static float crystalType4Visibility;
   private static float crystalType4HurtBlend;
   private static String crystalType = TargetEspMask.INITIAL_CRYSTAL_TYPE_LABEL;
   private static boolean crystalThroughWalls;
   private static String lightningType = TargetEspMask.INITIAL_LIGHTNING_TYPE_LABEL;
   private static boolean lightningThroughWalls;
   private static boolean lightingEnabled = true;
   private static String previousGhostType = "";
   private static String previousCrystalType = "";
   private static String previousLightningType = "";
   private static final LinkedList<TargetEspMask.TrailSample> orbitTrail = new LinkedList<>();
   private static long lastOrbitTrailSampleMillis;
   private static int orbitTrailEntityId = TargetEspMask.field_0У;
   private static final int ЯГ = 3;
   private static final long сК = 200L;
   private static final int Ял = 28;
   private static final int Яэ = 4;
   private static final long сI = 90L;
   private static final List<TargetEspMask.AnimatedArc> animatedArcs = new ArrayList<>();
   private static long lastArcSpawnMillis;
   private static final int Яю = 18;
   private static final int ЯЩ = 2;
   private static final long сО = 60L;
   private static final int Яу = 3;
   private static final List<TargetEspMask.RotatingPolyline> rotatingPolylines = new ArrayList<>();
   private static long lastPolylineSpawnMillis;
   private static int phaseEntityId = TargetEspMask.field_0З;
   private static float targetPhase;
   private static TextureTarget cubeMask;
   private static boolean distortionEnabled;
   private static float distortionStrength = TargetEspMask.field_0Е;
   private static boolean ghostBloomEnabled;
   private static boolean ghostThroughWalls = true;
   private static boolean cubeCompositePending;
   private static int cubeGlowColor = -1;
   private static float cubeGlowStrength = 1.0F;
   private static float cubeRotationProgress;
   private static boolean cubeHurtLatched;
   private static boolean cubeRotationActive;
   private static double cubeXRotationRadians;
   private static double cubeYRotationRadians;
   private static double cubeStartXRotationRadians;
   private static double cubeStartYRotationRadians;
   private static double cubeEndXRotationRadians;
   private static double cubeEndYRotationRadians;
   private static boolean deferredRenderActive;
   private static WorldEffectsEvent deferredEvent;
   private static LivingEntity deferredTarget;
   private static String deferredStyle;
   private static int deferredPrimaryColor;
   private static int deferredSecondaryColor;
   private static GpuBufferSlice deferredProjectionBuffer;
   private static ProjectionType deferredProjectionType;
   private static final Identifier RING_PRIVATE_GLOW_TEXTURE = ClientResourceIds.getImageId(TargetEspMask.RING_PRIVATE_GLOW_TEXTURE_IMAGE_PATH);
   private static final RenderPipeline PRIVATE_DEPTH_PIPELINE = GlowQuadRendering.Г(TargetEspMask.PRIVATE_DEPTH_PIPELINE_LOCATION_ID, CompareOp.GREATER_THAN_OR_EQUAL);
   private static final RenderPipeline PRIVATE_NO_DEPTH_PIPELINE = GlowQuadRendering.Г(TargetEspMask.PRIVATE_NO_DEPTH_PIPELINE_LOCATION_ID, CompareOp.ALWAYS_PASS);
   private static float ringParticleSize = TargetEspMask.field_0Б;
   private static int ringLayers = 14;
   private static boolean ringThroughWalls = true;
   private static float ringHurtBlend;
   private static long ringHurtTriggerMillis;
   private static int ringHurtEntityId = TargetEspMask.field_0С;
   private static int previousRingHurtTime;
   private static final float рК = 0.1F;
   private static final float рI = 5.0F;
   private static final String REQUESTED_RING_TYPE_FALLBACK_LABEL = "Тип 1";
   private static final String REQUESTED_SQUARE_TYPE_FALLBACK_LABEL = "Тип 1";
   private static final String REQUESTED_GHOST_TYPE_FALLBACK_LABEL = "Тип 1";
   private static final String REQUESTED_CRYSTAL_TYPE_FALLBACK_LABEL = "Тип 1";
   private static final String REQUESTED_LIGHTNING_TYPE_FALLBACK_LABEL = "Тип 2";
   private static final int рг = Integer.MIN_VALUE;
   private static final int р0 = Integer.MIN_VALUE;
   private static final int рх = Integer.MIN_VALUE;
   private static final int рУ = Integer.MIN_VALUE;
   private static final float рЗ = 0.01F;
   private static final float рЕ = 0.01F;
   private static final float рП = 5.5F;
   private static final String COMPOSITE_CUBE_MASK_IN_SAMPLER_NAME = "InSampler";
   private static final String SCENE_CAPTURE_GHOST_STYLE_LABEL = "Призраки";
   private static final String SCENE_CAPTURE_CRYSTAL_STYLE_LABEL = "Кристалики";
   private static final String SCENE_CAPTURE_LIGHTNING_STYLE_LABEL = "Молнии";
   private static final float рз = 0.1F;
   private static final float рц = 1000.0F;
   private static final String VISIBILITY_RING_STYLE_LABEL = "Кольцо";
   private static final String VISIBILITY_RING_TYPE3_LABEL = "Тип 3";
   private static final String VISIBILITY_CUBE_STYLE_LABEL = "Куб";
   private static final float field_6Я = 1.05F;
   private static final float field_6Х = 0.3F;
   private static final String PREVIOUS_LIGHTNING_STYLE_RESET_LABEL = "Молнии";
   private static final String PREVIOUS_LIGHTNING_TYPE3_STYLE_RESET_LABEL = "Тип 3";
   private static final int field_64 = Integer.MIN_VALUE;
   private static final int field_6п = Integer.MIN_VALUE;
   private static final int field_6Д = Integer.MIN_VALUE;
   private static final int field_6Ъ = Integer.MIN_VALUE;
   private static final String CRYSTAL_PULSE_STYLE_LABEL = "Кристалики";
   private static final String CRYSTAL_PULSE_TYPE4_LABEL = "Тип 4";
   private static final float field_6Ю = 1000.0F;
   private static final float field_6Ч = 0.15F;
   private static final double field_6Л = Math.PI;
   private static final double field_6诶 = 2.0;
   private static final float field_61 = 0.001F;
   private static final float field_6Ь = 0.01F;
   private static final int field_6衣 = -65536;
   private static final int field_6ш = -65536;
   private static final String LIGHT_COLLECTION_GHOST_STYLE_LABEL = "Призраки";
   private static final String LIGHT_COLLECTION_CRYSTAL_STYLE_LABEL = "Кристалики";
   private static final String LIGHT_COLLECTION_LIGHTNING_STYLE_LABEL = "Молнии";
   private static final String RING_RENDER_STYLE_LABEL = "Кольцо";
   private static final String HOOP_RENDER_STYLE_LABEL = "Обруч";
   private static final String GHOST_RENDER_STYLE_LABEL = "Призраки";
   private static final String CRYSTAL_RENDER_STYLE_LABEL = "Кристалики";
   private static final String RHOMBS_RENDER_STYLE_LABEL = "Ромбики";
   private static final String LIGHTNING_RENDER_STYLE_LABEL = "Молнии";
   private static final String CUBE_RENDER_STYLE_LABEL = "Куб";
   private static final String SQUARE_RENDER_STYLE_LABEL = "Квадрат";
   private static final String TRIANGLE_RENDER_STYLE_LABEL = "Треугольник";
   private static final String PENTAGRAM_RENDER_STYLE_LABEL = "Пентограмма";
   private static final String RING_RENDER_TYPE3_LABEL = "Тип 3";
   private static final String RING_RENDER_TYPE2_LABEL = "Тип 2";
   private static final int field_6ч = Integer.MIN_VALUE;
   private static final String GHOST_RENDER_TYPE2_LABEL = "Тип 2";
   private static final String GHOST_RENDER_TYPE3_LABEL = "Тип 3";
   private static final String GHOST_RENDER_TYPE4_LABEL = "Тип 4";
   private static final String GHOST_RENDER_TYPE5_LABEL = "Тип 5";
   private static final String GHOST_RENDER_TYPE6_LABEL = "Тип 6";
   private static final String GHOST_RENDER_TYPE7_LABEL = "Тип 7";
   private static final String GHOST_RENDER_TYPE8_LABEL = "Тип 8";
   private static final int field_6Щ = Integer.MIN_VALUE;
   private static final int field_6у = Integer.MIN_VALUE;
   private static final String CRYSTAL_RENDER_TYPE2_LABEL = "Тип 2";
   private static final String CRYSTAL_RENDER_TYPE3_LABEL = "Тип 3";
   private static final String CRYSTAL_RENDER_TYPE4_LABEL = "Тип 4";
   private static final String PREVIOUS_LIGHTNING_TYPE3_VARIANT_RESET_LABEL = "Тип 3";
   private static final String LIGHTNING_RENDER_TYPE1_LABEL = "Тип 1";
   private static final String LIGHTNING_RENDER_TYPE3_LABEL = "Тип 3";
   private static final String LIGHTNING_THROUGH_WALLS_OCCLUSION_NEVER_LABEL = "Never";
   private static final String LIGHTNING_DEPTH_OCCLUSION_AUTO_LABEL = "Auto";
   private static final String DEFER_LIGHTNING_STYLE_LABEL = "Молнии";
   private static final String DEFER_CRYSTAL_STYLE_LABEL = "Кристалики";
   private static final long field_66 = 360000L;
   private static final float field_6_ = 11.5F;
   private static final float field_6Ш = 0.001F;
   private static final float field_6ы = 1.18F;
   private static final float field_6й = 0.18F;
   private static final double field_6Ж = 0.035;
   private static final double field_6м = 1.7;
   private static final double field_6К = 2.41;
   private static final float field_6I = 0.68F;
   private static final float field_6Т = 0.3F;
   private static final float field_6О = 0.36F;
   private static final float field_6Ф = 0.28F;
   private static final float field_6Й = 0.42F;
   private static final double field_6к = 0.035;
   private static final double field_6г = 1.7;
   private static final double field_60 = 2.41;
   private static final float field_6х = 1.15F;
   private static final float field_6У = -0.9F;
   private static final float field_6З = 37.7F;
   private static final float field_6Е = 91.345F;
   private static final float field_6П = 12.17F;
   private static final float field_6西 = 43758.547F;
   private static final float field_6д = 0.075F;
   private static final float field_6Б = 0.115F;
   private static final float field_6С = 0.03F;
   private static final float field_6з = 0.8F;
   private static final float field_6ц = 1.73F;
   private static final float field_6я = 0.06F;
   private static final float _с = 255.0F;
   private static final float _В = 155.0F;
   private static final float _Я = 0.7F;
   private static final float _Х = 1.08F;
   private static final long _е = 360000L;
   private static final float _А = 40.0F;
   private static final long _4 = 360000L;
   private static final float _п = 11.5F;
   private static final float _Д = 0.001F;
   private static final float _Ъ = 1.27F;
   private static final float _т = 0.08F;
   private static final float _а = 0.85F;
   private static final float _Ю = 0.55F;
   private static final float _Ч = 1.18F;
   private static final float _Л = 0.18F;
   private static final float _诶 = 0.18F;
   private static final float _1 = 3.6F;
   private static final float _Ь = 0.09F;
   private static final float _衣 = 1.8F;
   private static final float _ш = 0.24F;
   private static final float _И = 0.28F;
   private static final float _ь = 0.34F;
   private static final float _ж = 255.0F;
   private static final float _弟 = 155.0F;
   private static final float _б = 1.08F;
   private static final float _Ы = 0.82F;
   private static final float _l = 1.18F;
   private static final float _8 = 1.0E-5F;
   private static final float _5 = 1.7F;
   private static final float _в = 2.41F;
   private static final float _ъ = 0.035F;
   private static final float _М = 6.8F;
   private static final float _Н = 0.88F;
   private static final float _н = 0.5F;
   private static final float _Ц = 1.15F;
   private static final float _ч = -0.9F;
   private static final float _Э = 37.7F;
   private static final float _必 = 360.0F;
   private static final float _щ = 0.82F;
   private static final float _Г = 0.42F;
   private static final float _л = 0.18F;
   private static final float _э = 1.75F;
   private static final float _ю = 0.02F;
   private static final float _Щ = 0.08F;
   private static final float _у = 0.82F;
   private static final float _2 = 0.82F;
   private static final float _ф = 1.35F;
   private static final float _и = 0.45F;
   private static final float _9 = 1.5F;
   private static final float _о = 14.0F;
   private static final float _O = 0.85F;
   private static final float _7 = 0.12F;
   private static final float _Р = 18.0F;
   private static final float _3 = 0.16F;
   private static final float _р = 0.42F;
   private static final float _6 = 205.0F;
   private static final float __ = 0.78F;
   private static final float _Ш = 0.95F;
   private static final float _ы = 112.0F;
   private static final float _й = 0.35F;
   private static final float _Ж = 0.6F;
   private static final float _м = 300.0F;
   private static final float _К = 0.58F;
   private static final float _I = 0.82F;
   private static final float _Т = 55.0F;
   private static final float _О = 0.48F;
   private static final float _Ф = 0.35F;
   private static final float _Й = 238.0F;
   private static final float _к = 0.2F;
   private static final float _г = 1.05F;
   private static final float _0 = 150.0F;
   private static final float _х = 0.68F;
   private static final float _У = 0.58F;
   private static final float _З = 342.0F;
   private static final float _Е = 0.88F;
   private static final float _П = 0.9F;
   private static final float _西 = 88.0F;
   private static final float _д = 0.55F;
   private static final float _Б = 0.48F;
   private static final float _С = 275.0F;
   private static final float _з = 0.4F;
   private static final float _ц = 1.12F;
   private static final float _я = 24.0F;
   private static final float Шс = 0.72F;
   private static final float ШВ = 0.68F;
   private static final float ШЯ = 190.0F;
   private static final float ШХ = 0.3F;
   private static final float Ше = 0.38F;
   private static final float ША = 128.0F;
   private static final float Ш4 = 0.62F;
   private static final float Шп = 0.94F;
   private static final float ШД = 318.0F;
   private static final float ШЪ = 0.83F;
   private static final float Шт = 0.55F;
   private static final float Ша = 63.0F;
   private static final float ШЮ = 0.12F;
   private static final float ШЧ = 0.76F;
   private static final float ШЛ = 224.0F;
   private static final float Ш诶 = 0.5F;
   private static final float Ш1 = 1.08F;
   private static final float ШЬ = 165.0F;
   private static final float Ш衣 = 0.92F;
   private static final float Шш = 0.45F;
   private static final float ШИ = 355.0F;
   private static final float Шь = 0.26F;
   private static final float Шж = 0.87F;
   private static final float Ш弟 = 98.0F;
   private static final float Шб = 0.66F;
   private static final float ШЫ = 0.62F;
   private static final float Шl = 260.0F;
   private static final float Ш8 = 0.44F;
   private static final float Ш5 = 38.0F;
   private static final float Шв = 0.76F;
   private static final float Шъ = 0.52F;
   private static final float ШМ = 208.0F;
   private static final float ШН = 0.57F;
   private static final float Шн = 0.72F;
   private static final float ШЦ = 330.0F;
   private static final float Шч = 0.14F;
   private static final float ШЭ = 0.58F;
   private static final float Ш必 = 82.0F;
   private static final float Шщ = 0.32F;
   private static final float ШГ = 0.5F;
   private static final float Шл = 176.0F;
   private static final float Шэ = 0.5F;
   private static final float Шю = 0.68F;
   private static final float ШЩ = 286.0F;
   private static final float Шу = 0.7F;
   private static final float Ш2 = 0.56F;
   private static final float Шф = 44.0F;
   private static final float Ши = 0.88F;
   private static final float Ш9 = 0.74F;
   private static final float Шо = 0.08F;
   private static final float ШO = 0.04F;
   private static final double Ш7 = Math.PI / 2;
   private static final double ШР = Math.PI / 2;
   private static final double Ш3 = Math.PI / 2;
   private static final double Шр = Math.PI / 2;
   private static final float Ш6 = 0.22F;
   private static final float Ш_ = 1.08F;
   private static final double ШШ = 0.18;
   private static final double Шы = 0.82;
   private static final double Шй = 0.34;
   private static final double ШЖ = 0.155;
   private static final double Шм = 0.031;
   private static final double ШК = 0.2;
   private static final double ШI = 0.8;
   private static final double ШТ = 0.58;
   private static final double ШО = 0.58;
   private static final int ШФ = -65536;
   private static final float ШЙ = 0.76F;
   private static final float Шк = 0.24F;
   private static final float Шг = 0.35F;
   private static final double Ш0 = 3.0;
   private static final double Шх = 0.19;
   private static final double ШУ = 0.02;
   private static final double ШЗ = 0.014;
   private static final double ШЕ = 0.3;
   private static final double ШП = 0.12;
   private static final double Ш西 = 0.05;
   private static final double Шд = 2.45;
   private static final double ШБ = 0.05;
   private static final double ШС = 0.04;
   private static final double Шз = 0.045;
   private static final double Шц = 0.018;
   private static final double Шя = 0.96;
   private static final double ыс = 0.04;
   private static final double ыВ = 0.065;
   private static final double ыЯ = 0.018;
   private static final double ыХ = Math.PI;
   private static final double ые = 2.15;
   private static final double ыА = 0.9;
   private static final double ы4 = Math.PI * 2;
   private static final double ып = Math.PI * 2;
   private static final double ыД = -Math.PI / 2;
   private static final double ыЪ = Math.PI;
   private static final double ыт = -Math.PI / 2;
   private static final double ыа = Math.PI;
   private static final double ыЮ = Math.PI * 2;
   private static final double ыЧ = Math.PI * 2;
   private static final String CUBE_MASK_DEBUG_LABEL = "target-esp-cube-core";
   private static final float ы诶 = 1.4F;
   private static final long ы1 = 100000L;
   private static final float ыЬ = 1.5F;
   private static final float ы衣 = 360.0F;
   private static final float ыш = 80.0F;
   private static final float ыИ = 0.08F;
   private static final float ыь = 0.002F;
   private static final float ыж = 0.3F;
   private static final float ы弟 = 0.007F;
   private static final float ыб = 0.5F;
   private static final float ыЫ = 1.2F;
   private static final float ыl = 0.5F;
   private static final float ы8 = 0.15F;
   private static final float ы5 = -0.15F;
   private static final float ыв = 5.0F;
   private static final float ыъ = 0.62F;
   private static final float ыМ = 6.0F;
   private static final float ыН = 0.05F;
   private static final double ын = 2.0;
   private static final float ыЦ = 20.0F;
   private static final float ыч = 0.7F;
   private static final double ыЭ = 0.15;
   private static final double ы必 = 10.0;
   private static final double ыщ = 25.0;
   private static final double ыГ = 0.285;
   private static final double ыл = -1.0;
   private static final double ыэ = -1.0;
   private static final double ыю = -1.0;
   private static final double ыЩ = -1.0;
   private static final double ыу = -1.0;
   private static final int ы2 = Integer.MIN_VALUE;
   private static final float ыф = 0.22F;
   private static final float ыи = 0.22F;
   private static final float ы9 = 0.22F;
   private static final float ыо = 0.78F;
   private static final float ыO = 0.01F;
   private static final float ы7 = 0.12F;
   private static final double ыР = 9.0;
   private static final double ы3 = 0.5;
   private static final float ыр = 0.65F;
   private static final float ы6 = 0.45F;
   private static final double ы_ = 0.7;
   private static final double ыШ = 1.15;
   private static final float ыы = 0.18F;
   private static final float ый = 0.001F;
   private static final float ыЖ = 0.8F;
   private static final float ым = 0.5F;
   private static final double ыК = 0.1;
   private static final double ыI = 1.7;
   private static final float ыТ = 0.7F;
   private static final float ыО = 0.25F;
   private static final float ыФ = 20.0F;
   private static final double ыЙ = 0.0035;
   private static final float ык = 4.0F;
   private static final float ыг = 0.12F;
   private static final float ы0 = 0.21F;
   private static final float ых = 0.01F;
   private static final double ыУ = 3.1;
   private static final float ыЗ = 0.4F;
   private static final float ыЕ = 0.12F;
   private static final float ыП = 0.24F;
   private static final double ы西 = 0.55;
   private static final double ыд = Math.PI * 2;
   private static final double ыБ = 26.0;
   private static final double ыС = 0.15;
   private static final double ыз = 3.0;
   private static final float ыц = 6.0F;
   private static final double ыя = 0.35;
   private static final float йс = 1.5F;
   private static final long йВ = 900L;
   private static final float йЯ = 900.0F;
   private static final float йХ = 1.25F;
   private static final float йе = 1.05F;
   private static final float йА = 0.4F;
   private static final long й4 = 25L;
   private static final double йп = 0.15;
   private static final double йД = 25.0;
   private static final double йЪ = -1.0;
   private static final float йт = 0.2F;
   private static final double йа = -1.0;
   private static final double йЮ = -1.0;
   private static final float йЧ = 0.2F;
   private static final double йЛ = -1.0;
   private static final double й诶 = -1.0;
   private static final float й1 = 0.2F;
   private static final double йЬ = 0.14;
   private static final double й衣 = 1.0E-5;
   private static final double йш = 5.0;
   private static final double йИ = 0.5;
   private static final double йь = 1.5;
   private static final double йж = 0.1;
   private static final double й弟 = 0.65;
   private static final double йб = 0.1;
   private static final double йЫ = 0.65;
   private static final double йl = 0.1;
   private static final double й8 = 0.65;
   private static final float й5 = 2.1F;
   private static final float йв = 0.7F;
   private static final float йъ = 0.24F;
   private static final float йМ = 0.14F;
   private static final float йН = 2.1F;
   private static final float йн = 0.48F;
   private static final double йЦ = 0.7;
   private static final double йч = 0.7;
   private static final double йЭ = -1.0;
   private static final double й必 = -1.0;
   private static final double йщ = -1.0;
   private static final float йГ = 2.70158F;
   private static final float йл = 1.70158F;
   private static final long йэ = 200L;
   private static final float йю = 0.01F;
   private static final float йЩ = 0.6F;
   private static final float йу = 0.5F;
   private static final float й2 = 0.2F;
   private static final float йф = 0.38F;
   private static final float йи = 0.45F;
   private static final float й9 = 0.85F;
   private static final float йо = 0.02F;
   private static final long йO = 90L;
   private static final float й7 = 0.02F;
   private static final float йР = 0.38F;
   private static final float й3 = 0.14F;
   private static final float йр = 0.2F;
   private static final float й6 = 0.22F;
   private static final float й_ = 0.02F;
   private static final float йШ = 0.14F;
   private static final float йы = 2.2F;
   private static final float йй = 0.55F;
   private static final float йЖ = 1.35F;
   private static final float йм = 0.75F;
   private static final float йК = 0.02F;
   private static final float йI = 0.14F;
   private static final float йТ = 0.45F;
   private static final float йО = 0.6F;
   private static final float йФ = 0.25F;
   private static final float йЙ = 0.45F;
   private static final float йк = 0.12F;
   private static final float йг = 0.16F;
   private static final float й0 = 0.5F;
   private static final float йх = 0.5F;
   private static final float йУ = 1.05F;
   private static final float йЗ = 0.55F;
   private static final float йЕ = 0.82F;
   private static final float йП = 0.1F;
   private static final float й西 = 0.12F;
   private static final float йд = 0.55F;
   private static final float йБ = 0.25F;
   private static final float йС = 1.25F;
   private static final float йз = 0.55F;
   private static final float йц = 0.9F;
   private static final float йя = 0.1F;
   private static final float Жс = 0.25F;
   private static final float ЖВ = 0.2F;
   private static final float ЖЯ = 1.25F;
   private static final float ЖХ = 0.55F;
   private static final double Же = Math.PI;
   private static final double ЖА = 2.0;
   private static final float Ж4 = 1.2F;
   private static final float Жп = 0.35F;
   private static final float ЖД = 0.03F;
   private static final float ЖЪ = 100.0F;
   private static final float Жт = 100.0F;
   private static final float Жа = 0.12F;
   private static final float ЖЮ = 0.78F;
   private static final float ЖЧ = 0.22F;
   private static final float ЖЛ = 0.92F;
   private static final float Ж诶 = 0.08F;
   private static final long Ж1 = 90L;
   private static final float ЖЬ = 19.37F;
   private static final float Ж衣 = 0.5F;
   private static final float Жш = 0.35F;
   private static final float ЖИ = 0.5F;
   private static final float Жь = 0.1F;
   private static final float Жж = 0.5F;
   private static final float Ж弟 = 0.07F;
   private static final double Жб = Math.PI;
   private static final double ЖЫ = 2.0;
   private static final float Жl = 0.92F;
   private static final float Ж8 = 1.1F;
   private static final double Ж5 = 0.14;
   private static final long Жв = 31L;
   private static final long Жъ = 97L;
   private static final double ЖМ = 1.0E-6;
   private static final double ЖН = 0.55;
   private static final float Жн = 27.0F;
   private static final double ЖЦ = 0.015;
   private static final double Жч = 1.0E-8;
   private static final double ЖЭ = 0.85;
   private static final double Ж必 = 1.0E-8;
   private static final double Жщ = 0.5;
   private static final double ЖГ = 2.0;
   private static final double Жл = 0.5;
   private static final double Жэ = 2.0;
   private static final double Жю = 0.35;
   private static final double ЖЩ = 0.5;
   private static final double Жу = 0.5;
   private static final double Ж2 = 0.5;
   private static final float Жф = 1.0E-4F;
   private static final double Жи = 1.0E-10;
   private static final double Ж9 = 0.5;
   private static final double Жо = 1.0E-8;
   private static final double ЖO = 1.0E-8;
   private static final double Ж7 = 1.0E-8;
   private static final double ЖР = -1.0;
   private static final double Ж3 = 1.0E-8;
   private static final float Жр = 0.5F;
   private static final float Ж6 = 0.5F;
   private static final float Ж_ = 0.5F;
   private static final float ЖШ = 0.5F;
   private static final float Жы = 1.0E-4F;
   private static final float Жй = 0.001F;
   private static final double ЖЖ = 1.0E-9;
   private static final double Жм = 0.9;
   private static final double ЖК = 1.0E-9;
   private static final double ЖI = -1.0;
   private static final double ЖТ = 1.0E-9;
   private static final float ЖО = 4.0F;
   private static final float ЖФ = 0.25F;
   private static final float ЖЙ = 0.001F;
   private static final float Жк = 2.4F;
   private static final float Жг = 2.4F;
   private static final double Ж0 = Math.PI * 2;
   private static final double Жх = 6.0;
   private static final double ЖУ = Math.PI * 2;
   private static final double ЖЗ = 6.0;
   private static final float ЖЕ = 6.0F;
   private static final float ЖП = 6.0F;
   private static final float Ж西 = 0.72F;
   private static final float Жд = 0.28F;
   private static final double ЖБ = Math.PI;
   private static final double ЖС = 0.75;
   private static final float Жз = 6.0F;
   private static final float Жц = 15.0F;
   private static final float Жя = 10.0F;
   private static final float мс = 0.5F;
   private static final float мВ = 0.5F;
   private static final int мЯ = 374761393;
   private static final int мХ = 668265263;
   private static final int ме = 1274126177;
   private static final float мА = 0.5F;
   private static final float м4 = 2.03F;
   private static final float мп = 0.5F;
   private static final double мД = 1.0E-9;
   private static final double мЪ = 0.75;
   private static final long мт = 60L;
   private static final float ма = 0.01F;
   private static final float мЮ = 0.4F;
   private static final float мЧ = 0.9F;
   private static final double мЛ = Math.PI;
   private static final float м诶 = 0.02F;
   private static final double м1 = 1.0E-9;
   private static final float мЬ = 0.3F;
   private static final float м衣 = 0.34F;
   private static final float мш = 0.18F;
   private static final float мИ = 0.18F;
   private static final float мь = 0.3F;
   private static final float мж = 0.09F;
   private static final float м弟 = 0.42F;
   private static final float мб = 0.05F;
   private static final float мЫ = 0.6F;
   private static final float мl = 0.028F;
   private static final float м8 = 0.85F;
   private static final float м5 = 0.014F;
   private static final float мв = 0.006F;
   private static final double мъ = 0.6;
   private static final double мМ = 0.5;
   private static final double мН = 0.2;
   private static final double мн = 0.22;
   private static final double мЦ = 0.18;
   private static final float мч = 1.6F;
   private static final float мЭ = 1.2F;
   private static final float м必 = 0.85F;
   private static final float мщ = 0.9F;
   private static final float мГ = (float) Math.PI;
   private static final float мл = 0.82F;
   private static final float мэ = 0.1F;
   private static final float мю = 0.18F;
   private static final double мЩ = 0.06;
   private static final double му = 0.4;
   private static final float м2 = 0.55F;
   private static final float мф = 0.35F;
   private static final float ми = 100.0F;
   private static final float м9 = 0.85F;
   private static final float мо = 0.55F;
   private static final double мO = Math.PI;
   private static final double м7 = 2.0;
   private static final double мР = 0.5;
   private static final double м3 = 0.35;
   private static final double мр = 0.92;
   private static final double м6 = 0.16;
   private static final double м_ = 0.06;
   private static final double мШ = 0.88;
   private static final double мы = 0.5;
   private static final double мй = 0.05;
   private static final double мЖ = 0.5;
   private static final double мм = 0.5;
   private static final double мК = 2.0;
   private static final double мI = 0.5;
   private static final double мТ = 0.5;
   private static final double мО = 0.9;
   private static final double мФ = Math.PI;
   private static final double мЙ = 2.0;
   private static final long мк = 80L;
   private static final float мг = 0.4F;
   private static final long м0 = 30L;
   private static final float мх = 0.35F;
   private static final float мУ = 0.85F;
   private static final float мЗ = 0.2F;
   private static final float мЕ = 0.4F;
   private static final float мП = 0.6F;
   private static final float м西 = 0.07F;
   private static final float мд = 0.8F;
   private static final float мБ = 0.2F;
   private static final double мС = 0.05;
   private static final float мз = 0.015F;
   private static final float мц = 0.001F;
   private static final float мя = 0.001F;
   private static final float Кс = 0.5F;
   private static final float КВ = 0.5F;
   private static final float КЯ = 0.5F;
   private static final float КХ = 0.5F;
   private static final float Ке = 0.045F;
   private static final float КА = 0.6F;
   private static final float К4 = 0.5F;
   private static final float Кп = 0.024F;
   private static final float КД = 11.0F;
   private static final float КЪ = 0.012F;
   private static final float Кт = 0.55F;
   private static final float Ка = 0.45F;
   private static final float КЮ = 23.0F;
   private static final double КЧ = 0.55;
   private static final double КЛ = 0.5;
   private static final double К诶 = 0.18;
   private static final double К1 = Math.PI;
   private static final double КЬ = 2.0;
   private static final double К衣 = 0.2;
   private static final double Кш = 0.55;
   private static final double КИ = 0.2;
   private static final double Кь = 0.55;
   private static final double Кж = 0.5;
   private static final double К弟 = 1.25;
   private static final double Кб = 0.9;
   private static final double КЫ = 1.6;
   private static final double Кl = 2.4;
   private static final double К8 = 2.6;
   private static final double К5 = 0.55;
   private static final double Кв = 0.35;
   private static final double Къ = 1.0E-6;
   private static final double КМ = -1.0;
   private static final double КН = 1.0E-8;
   private static final double Кн = -1.0;
   private static final double КЦ = 0.5;
   private static final double Кч = 0.35;
   private static final double КЭ = 0.55;
   private static final double К必 = 0.15;
   private static final double Кщ = 0.4;
   private static final float КГ = 0.5F;
   private static final float Кл = 0.05F;
   private static final float Кэ = 0.5F;
   private static final double Кю = 0.22;
   private static final double КЩ = 0.18;
   private static final float Ку = 0.45F;
   private static final float К2 = 0.3F;
   private static final float Кф = 0.5F;
   private static final double Ки = Math.PI;
   private static final double К9 = 0.4;
   private static final double Ко = 0.35;
   private static final double КO = 0.3;
   private static final double К7 = 0.4;
   private static final double КР = 0.16;
   private static final double К3 = 0.12;
   private static final float Кр = 0.35F;
   private static final float К6 = 0.3F;
   private static final float К_ = 100.0F;
   private static final double КШ = -1.0;
   private static final float Кы = 0.4F;
   private static final float Кй = 0.6F;
   private static final double КЖ = 0.5;
   private static final double Км = 1.0E-8;
   private static final double КК = 1.0E-8;
   private static final double КI = 1.0E-8;
   private static final double КТ = 1.0E-8;
   private static final double КО = 0.5;
   private static final double КФ = 2.0;
   private static final double КЙ = 0.5;
   private static final double Кк = 0.55;
   private static final double Кг = 0.52;
   private static final double К0 = 0.52;
   private static final float Кх = 1.0E-4F;
   private static final float КУ = 0.75F;
   private static final float КЗ = 0.28F;
   private static final float КЕ = 0.01F;
   private static final double КП = 1.0E-8;
   private static final double К西 = 0.88;
   private static final double Кд = 1.0E-8;
   private static final double КБ = 0.45;
   private static final double КС = 0.45;
   private static final float Кз = 0.5F;
   private static final float Кц = 1.18F;
   private static final float Кя = 0.52F;
   private static final float Iс = 1.18F;
   private static final float IВ = 0.72F;
   private static final float IЯ = 0.12F;
   private static final float IХ = 0.88F;
   private static final float Iе = 0.12F;
   private static final double IА = Math.PI * 2;
   private static final double I4 = Math.PI * 2;
   private static final double Iп = 0.5;
   private static final double IД = 2.0;
   private static final double IЪ = 2.0;
   private static final double Iт = 5.0;
   private static final double Iа = 4.0;
   private static final double IЮ = 3.0;
   private static final double IЧ = 3.0;
   private static final double IЛ = 0.5;
   private static final double I诶 = 2.0;
   private static final double I1 = 2.0;
   private static final double IЬ = 5.0;
   private static final double I衣 = 4.0;
   private static final double Iш = 3.0;
   private static final double IИ = 3.0;
   private static final double Iь = 0.5;
   private static final double Iж = 2.0;
   private static final double I弟 = 2.0;
   private static final double Iб = 5.0;
   private static final double IЫ = 4.0;
   private static final double Il = 3.0;
   private static final double I8 = 3.0;
   private static final float I5 = 4.0F;
   private static final float Iв = 1.25F;
   private static final String DRAW_LIGHTNING_GEOMETRY_IN_SAMPLER_NAME = "InSampler";
   private static final String LIGHTNING_MASK_DEBUG_LABEL = "target-esp-lightning-glow-mask";
   private static final double IН = 12.9898;
   private static final double Iн = 78.233;
   private static final float IЦ = 43758.547F;
   private static final float Iч = 0.001F;
   private static final double IЭ = 0.58;
   private static final float I必 = 0.95F;
   private static final float Iщ = 0.05F;
   private static final float IГ = 2.6667F;
   private static final float Iл = 58.0F;
   private static final float Iэ = 58.0F;
   private static final float Iю = 16.0F;
   private static final float IЩ = 5.5F;
   private static final float Iу = -4.2F;
   private static final float I2 = 7.4F;
   private static final float Iф = 130.0F;
   private static final float Iи = 250.0F;
   private static final float I9 = 0.9F;
   private static final float Iо = 0.35F;
   private static final float IO = 0.16F;
   private static final float I7 = 0.4F;
   private static final float IР = 0.6F;
   private static final float I3 = 0.34F;
   private static final float Iр = 6.0F;
   private static final float I6 = 0.1F;
   private static final float I_ = 0.08F;
   private static final float IШ = 0.82F;
   private static final float Iы = -7.0F;
   private static final float Iй = 7.0F;
   private static final float IЖ = 0.095F;
   private static final float Iм = 0.88F;
   private static final float IК = 0.2F;
   private static final float II = 0.5F;
   private static final float IТ = 0.32F;
   private static final float IО = 0.0628F;
   private static final double IФ = 0.98;
   private static final String SQUARE_ALTERNATE_TYPE2_LABEL = "Тип 2";
   private static final float Iк = 1.5F;
   private static final float Iг = 1.2F;
   private static final double I0 = 1.667;
   private static final float Iх = 180.0F;
   private static final float IУ = 255.0F;
   private static final double IЗ = 0.5;
   private static final float IЕ = 3.0F;
   private static final float IП = 0.08F;
   private static final float I西 = 0.04F;
   private static final float Iд = 0.42F;
   private static final float IБ = 0.32F;
   private static final float IС = 0.001F;
   private static final double Iз = 0.85F;
   private static final float Iц = 0.3F;
   private static final float Iя = 0.1764706F;
   private static final float Тс = 2.1F;
   private static final float ТВ = -0.95F;
   private static final float ТЯ = 0.6F;
   private static final float ТХ = 0.95F;
   private static final float Те = 0.6F;
   private static final float ТА = -1.05F;
   private static final float Т4 = 5.0F;
   private static final float Тп = 0.85F;
   private static final float ТД = 3.0F;
   private static final float ТЪ = 3.0F;
   private static final float Тт = 0.5F;
   private static final float Та = 0.7F;
   private static final float ТЮ = 3.0F;
   private static final float ТЧ = 0.08F;
   private static final float ТЛ = 3.0F;
   private static final float Т诶 = 3.0F;
   private static final float Т1 = 0.9F;
   private static final float ТЬ = 80.0F;
   private static final float Т衣 = 175.0F;
   private static final float Тш = 255.0F;
   private static final float ТИ = 0.95F;
   private static final float Ть = 1.9F;
   private static final float Тж = 0.002F;
   private static final float Т弟 = 0.35F;
   private static final float Тб = 2.2F;
   private static final float ТЫ = 0.45F;
   private static final float Тl = 1.3F;
   private static final float Т8 = 3.0F;
   private static final double Т5 = Math.PI * 2;
   private static final double Тв = Math.PI;
   private static final float Тъ = 0.15F;
   private static final float ТМ = 0.5F;
   private static final float ТН = 0.5F;
   private static final double Тн = Math.PI;
   private static final double ТЦ = 3.0;
   private static final float Тч = 0.6F;
   private static final float ТЭ = 1.65F;
   private static final float Т必 = 0.55F;
   private static final float Тщ = 0.5F;
   private static final float ТГ = 0.16F;
   private static final float Тл = 12.9898F;
   private static final float Тэ = 43758.547F;
   private static final float Тю = 0.01F;
   private static final float ТЩ = 0.52F;
   private static final float Ту = 0.18F;
   private static final float Т2 = 0.04F;
   private static final double Тф = 3.1;
   private static final float Ти = 0.68F;
   private static final float Т9 = 1.14F;
   private static final float То = 0.15F;
   private static final double ТO = 0.55;
   private static final double Т7 = 0.035;
   private static final float ТР = 1.32F;
   private static final float Т3 = 0.18F;
   private static final float Тр = 0.4F;
   private static final double Т6 = 1.28;
   private static final double Т_ = 0.17;
   private static final double ТШ = 34.0;
   private static final double Ты = 9.0;
   private static final double Тй = 0.7;
   private static final double ТЖ = 0.82;
   private static final double Тм = 0.48;
   private static final float ТК = 0.88F;
   private static final float ТI = 0.12F;
   private static final float ТТ = 0.85365856F;
   private static final float ТО = 0.55F;
   private static final float ТФ = 0.22F;
   private static final float ТЙ = 0.42F;
   private static final float Тк = 0.16F;
   private static final float Тг = 0.14F;
   private static final float Т0 = 0.22F;
   private static final float Тх = 0.78F;
   private static final float ТУ = 0.28F;
   private static final float ТЗ = 0.2F;
   private static final float ТЕ = 0.72F;
   private static final float ТП = 0.28F;
   private static final double Т西 = 4.2;
   private static final double Тд = 1.256637;
   private static final float ТБ = 0.11F;
   private static final float ТС = 0.88F;
   private static final float Тз = 0.9F;
   private static final float Тц = 0.2F;
   private static final float Тя = 0.5F;
   private static final float Ос = 0.32F;
   private static final float ОВ = 0.42F;
   private static final float ОЯ = 3.0F;
   private static final float ОХ = 5.0F;
   private static final float Ое = 0.09F;
   private static final float ОА = 0.015F;
   private static final float О4 = 0.92F;
   private static final float Оп = 0.7F;
   private static final float ОД = 0.16F;
   private static final float ОЪ = 2.1F;
   private static final float От = 0.55F;
   private static final float Оа = 0.28F;
   private static final float ОЮ = 0.06F;
   private static final double ОЧ = 1.55;
   private static final double ОЛ = Math.PI;
   private static final double О诶 = 2.0;
   private static final double О1 = 5.0;
   private static final double ОЬ = 1.04;
   private static final double О衣 = 1.04;
   private static final float Ош = 0.07F;
   private static final float ОИ = 0.7F;
   private static final float Оь = 0.48F;
   private static final float Ож = 0.82F;
   private static final double О弟 = Math.PI / 2;
   private static final double Об = Math.PI;
   private static final double ОЫ = 2.0;
   private static final double Оl = 5.0;
   private static final double О8 = 1.0E-6;
   private static final float О5 = 0.68F;
   private static final float Ов = 0.72F;
   private static final float Оъ = 1.15F;
   private static final float ОМ = 0.16F;
   private static final double ОН = 2.0;
   private static final double Он = 0.17;
   private static final double ОЦ = 22.0;
   private static final float Оч = 0.46F;
   private static final float ОЭ = 2.72F;
   private static final float О必 = 0.22F;
   private static final float Ощ = 2.6667F;
   private static final float ОГ = 0.48F;
   private static final float Ол = 0.94F;
   private static final double Оэ = 5.0;
   private static final float Ою = 13.0F;
   private static final double ОЩ = 0.075;
   private static final float Оу = 0.42F;
   private static final float О2 = 0.07F;
   private static final float Оф = 0.105F;
   private static final float Ои = 0.72F;
   private static final float О9 = 0.9F;
   private static final float Оо = 0.5F;
   private static final float ОO = 0.4F;
   private static final double О7 = Math.PI * 2;
   private static final float ОР = 360.0F;
   private static final float О3 = 0.13F;
   private static final float Ор = 0.52F;
   private static final float О6 = 0.82F;
   private static final float О_ = 0.17F;
   private static final float ОШ = 0.115F;
   private static final float Оы = 0.085F;
   private static final float Ой = 0.65F;
   private static final float ОЖ = 0.01F;
   private static final double Ом = 1550.0;
   private static final double ОК = Math.PI;
   private static final double ОI = 2.0;
   private static final float ОТ = 10.0F;
   private static final float ОО = 0.25F;
   private static final double ОФ = 0.5;
   private static final double ОЙ = 0.5;
   private static final double Ок = 0.2875;
   private static final float Ог = 0.005F;
   private static final float О0 = 0.008F;
   private static final float Ох = 0.3F;
   private static final float ОУ = 0.75F;
   private static final float ОЗ = 0.65F;
   private static final float ОЕ = 0.65F;
   private static final int ОП = -3329486;
   private static final double О西 = 5.0;
   private static final float Од = 5.0F;
   private static final float ОБ = 0.1F;
   private static final float ОС = 0.8F;
   private static final double Оз = 0.5;
   private static final float Оц = 0.3F;
   private static final float Оя = 0.2F;
   private static final float Фс = 0.2F;
   private static final float ФВ = 0.8F;
   private static final float ФЯ = 50.0F;
   private static final float ФХ = 0.005F;
   private static final float Фе = 1800.0F;
   private static final long ФА = 1500L;
   private static final double Ф4 = 2.0;
   private static final double Фп = 2.0;
   private static final float ФД = 0.5F;
   private static final double ФЪ = -2.0;
   private static final double Фт = 2.0;
   private static final double Фа = 2.0;
   private static final double ФЮ = 2.0;
   private static final double ФЧ = 2.0;
   private static final float ФЛ = 0.5F;
   private static final float Ф诶 = 2000000.0F;
   private static final float Ф1 = 10.0F;
   private static final float ФЬ = 2.5F;
   private static final float Ф衣 = 0.7F;
   private static final float Фш = 14.0F;
   private static final float ФИ = 0.5F;
   private static final float Фь = 0.5F;
   private static final float Фж = 0.5F;
   private static final float Ф弟 = 0.2F;
   private static final float Фб = 3.5F;
   private static final float ФЫ = 15.0F;
   private static final float Фl = 0.5F;
   private static final double Ф8 = Math.PI;
   private static final double Ф5 = 2.0;
   private static final long Фв = 12345L;
   private static final double Фъ = 0.5;
   private static final double ФМ = 0.5;
   private static final double ФН = 0.5;
   private static final float Фн = 10.0F;
   private static final float ФЦ = 0.15F;
   private static final float Фч = 0.5882353F;
   private static final double ФЭ = 0.5;
   private static final float Ф必 = 1.5F;
   private static final float Фщ = 1.5F;
   private static final float ФГ = 1.5F;
   private static final double Фл = 4.0;
   private static final float Фэ = 8.0F;
   private static final float Фю = 0.55F;
   private static final float ФЩ = 0.2F;
   private static final float Фу = 0.5F;
   private static final double Ф2 = 125.0;
   private static final double Фф = 0.2;
   private static final double Фи = 10.0;
   private static final double Ф9 = 15.0;
   private static final double Фо = 0.45;
   private static final double ФO = 0.45;
   private static final float Ф7 = 0.045F;
   private static final double ФР = 0.35F;
   private static final float Ф3 = 0.045F;
   private static final double Фр = 0.35F;
   private static final float Ф6 = 0.045F;
   private static final float Ф_ = 0.2F;
   private static final float ФШ = 0.35F;
   private static final float Фы = 1.5F;
   private static final float Фй = 2.25F;
   private static final float ФЖ = 0.2F;
   private static final long Фм = 16L;
   private static final double ФК = 4.0;
   private static final double ФI = 360.0;
   private static final float ФТ = 0.6F;
   private static final float ФО = 0.5F;
   private static final float ФФ = 0.4F;
   private static final double ФЙ = 360.0;
   private static final double Фк = 2.0;
   private static final float Фг = 0.4F;
   private static final float Ф0 = 100.0F;
   private static final float Фх = 0.01F;
   private static final float ФУ = 400.0F;
   private static final float ФЗ = 400.0F;
   private static final float ФЕ = 800.0F;
   private static final float ФП = 400.0F;
   private static final float Ф西 = 0.01F;
   private static final float Фд = 0.01F;
   private static final double ФБ = 0.004;
   private static final double ФС = Math.PI * 2;
   private static final double Фз = 0.15;
   private static final double Фц = -1.0;
   private static final double Фя = 0.8;
   private static final double Йс = 0.55;
   private static final double ЙВ = 0.5;
   private static final double ЙЯ = 0.04;
   private static final float ЙХ = 0.34F;
   private static final float Йе = 0.21F;
   private static final double ЙА = 1.8;
   private static final float Й4 = 0.2F;
   private static final double Йп = Math.PI;
   private static final double ЙД = 10.0;
   private static final float ЙЪ = 0.1F;
   private static final double Йт = Math.PI;
   private static final double Йа = 2.0;
   private static final long ЙЮ = 10L;
   private static final long ЙЧ = 360L;
   private static final float ЙЛ = 0.75F;
   private static final float Й诶 = 0.75F;
   private static final float Й1 = 0.75F;
   private static final double ЙЬ = 255.0;
   private static final float Й衣 = 180.0F;
   private static final int Йш = -39836;
   private static final int ЙИ = 16777215;
   private static final double Йь = 2000.0;
   private static final float Йж = 360.0F;
   private static final float Й弟 = 20.0F;
   private static final float Йб = 0.3F;
   private static final double ЙЫ = 1.5;
   private static final double Йl = 0.7F;
   private static final double Й8 = 1.5;
   private static final double Й5 = 0.7F;
   private static final double Йв = 10430.378350470453;
   private static final int Йъ = 65535;
   private static final double ЙМ = Math.PI;
   private static final double ЙН = 2.0;
   private static final double Йн = 65536.0;
   private static final float ЙЦ = 0.3F;
   private static final float Йч = 0.35F;
   private static final float ЙЭ = 255.0F;
   private static final float Й必 = 0.06F;
   private static final float Йщ = 0.2F;
   private static final float ЙГ = 255.0F;
   private static final float Йл = 0.06F;
   private static final float Йэ = 255.0F;
   private static final long Йю = 360000L;
   private static final float ЙЩ = 7.25F;
   private static final float Йу = 0.001F;
   private static final float Й2 = 1.5F;
   private static final float Йф = 1.25F;
   private static final float Йи = 0.5F;
   private static final double Й9 = 0.05;
   private static final float Йо = 0.15F;
   private static final float ЙO = 19.3F;
   private static final float Й7 = 0.9625F;
   private static final float ЙР = 0.2F;
   private static final float Й3 = 0.4375F;
   private static final float Йр = 0.22F;
   private static final double Й6 = 2.0;
   private static final float Й_ = 1.5F;
   private static final float ЙШ = 1.25F;
   private static final float Йы = 0.5F;
   private static final long Йй = 360000L;
   private static final float ЙЖ = 2.5F;
   private static final float Йм = 0.3F;
   private static final float ЙК = 0.1F;
   private static final float ЙI = 0.1F;
   private static final float ЙТ = 0.3F;
   private static final float ЙО = 0.1F;
   private static final float ЙФ = 1.5F;
   private static final float ЙЙ = 0.2F;
   private static final float Йк = 0.6F;
   private static final float Йг = 0.2F;
   private static final float Й0 = 1.5F;
   private static final long Йх = 133769420L;
   private static final float ЙУ = 12.0F;
   private static final float ЙЗ = -1.0F;
   private static final float ЙЕ = 360.0F;
   private static final float ЙП = Float.MAX_VALUE;
   private static final double Й西 = 240.0;
   private static final float Йд = 260.0F;
   private static final double ЙБ = 0.9;
   private static final float ЙС = 60.0F;
   private static final float Йз = 0.05F;
   private static final int Йц = -52686;
   private static final float Йя = 0.5F;
   private static final float кс = 0.5F;
   private static final float кВ = 0.5F;
   private static final float кЯ = 0.5F;
   private static final float кХ = 1.0E-8F;
   private static final float ке = -1.0F;
   private static final float кА = 1.3F;
   private static final float к4 = 1.3F;
   private static final float кп = 1.3F;
   private static final float кД = 0.92F;
   private static final float кЪ = 1.7F;
   private static final float кт = 0.16F;
   private static final float ка = 0.54F;
   private static final float кЮ = 0.76F;
   private static final float кЧ = 0.92F;
   private static final float кЛ = 4.3F;
   private static final float к诶 = 0.16F;
   private static final float к1 = 1.12F;
   private static final float кЬ = 0.92F;
   private static final float к衣 = 8.1F;
   private static final float кш = 0.12F;
   private static final float кИ = 12.6F;
   private static final float кь = 0.5F;
   private static final float кж = 0.07F;
   private static final float к弟 = 17.2F;
   private static final float кб = 0.5F;
   private static final float кЫ = 0.07F;
   private static final float кl = 0.58F;
   private static final float к8 = 0.42F;
   private static final float к5 = 0.18F;
   private static final float кв = 0.45F;
   private static final float къ = 0.45F;
   private static final float кМ = 0.45F;
   private static final float кН = 0.45F;
   private static final float кн = 0.45F;
   private static final float кЦ = 0.45F;
   private static final float кч = 0.45F;
   private static final float кЭ = 0.45F;
   private static final float к必 = 0.61F;
   private static final float кщ = 23.0F;
   private static final float кГ = 1.27F;
   private static final float кл = 51.0F;
   private static final float кэ = 17.0F;
   private static final float кю = 1.3F;
   private static final float кЩ = 1.3F;
   private static final float ку = 1.3F;
   private static final float к2 = 0.62F;
   private static final float кф = 0.92F;
   private static final float ки = 1.7F;
   private static final float к9 = 0.16F;
   private static final float ко = 0.76F;
   private static final float кO = 0.92F;
   private static final float к7 = 4.3F;
   private static final float кР = 0.16F;
   private static final float к3 = 0.76F;
   private static final float кр = 0.92F;
   private static final float к6 = 8.1F;
   private static final float к_ = 0.16F;
   private static final float кШ = 12.6F;
   private static final float кы = 0.5F;
   private static final float кй = 0.08F;
   private static final float кЖ = 17.2F;
   private static final float км = 0.5F;
   private static final float кК = 0.08F;
   private static final float кI = 0.58F;
   private static final float кТ = 0.6F;
   private static final float кО = 0.6F;
   private static final float кФ = 0.6F;
   private static final float кЙ = 0.6F;
   private static final float кк = 0.6F;
   private static final float кг = 0.6F;
   private static final float к0 = 0.6F;
   private static final float кх = 0.6F;
   private static final float кУ = 78.233F;
   private static final float кЗ = 43758.547F;
   private static final float кЕ = 0.5F;
   private static final float кП = 1.0E-8F;
   private static final float к西 = -1.0F;
   private static final float кд = 1.34F;
   private static final float кБ = 1.34F;
   private static final float кС = 0.84F;
   private static final float кз = 0.36F;
   private static final float кц = 1.12F;
   private static final float кя = 0.5F;
   private static final float гс = 0.5F;
   private static final float гВ = 0.5F;
   private static final float гЯ = 0.5F;
   private static final float гХ = 0.5F;
   private static final float ге = 0.5F;
   private static final float гА = 0.5F;
   private static final float г4 = 0.5F;
   private static final float гп = 0.5F;
   private static final float гД = 0.5F;
   private static final float гЪ = 0.5F;
   private static final float гт = 0.5F;
   private static final float га = 0.5F;
   private static final float гЮ = 0.5F;
   private static final float гЧ = 0.5F;
   private static final float гЛ = 0.5F;
   private static final float г诶 = 0.5F;
   private static final float г1 = 0.5F;
   private static final float гЬ = 0.5F;
   private static final float г衣 = 0.5F;
   private static final float гш = 0.5F;
   private static final float гИ = 0.5F;
   private static final float гь = 255.0F;
   private static final float гж = 0.5F;
   private static final float г弟 = 255.0F;
   private static final float гб = 10.0F;
   private static final float гЫ = 360.0F;
   private static final float гl = 0.5F;
   private static final long г8 = 4L;
   private static final long г5 = 360L;
   private static final float гв = 180.0F;
   private static final float гъ = 0.85F;
   private static final float гМ = 3.0F;
   private static final String DRAW_TEXTURED_GEOMETRY_SAMPLER0_NAME = "Sampler0";
   private static final String TARGET_ESP_MASK_PASS_DEBUG_LABEL = "targetEspMask";
   private static final String MASK_VERTEX_BUFFER_DEBUG_LABEL = "targetEspMaskVertices";
   private static final String TARGET_ESP_COLOR_PASS_DEBUG_LABEL = "targetEspColor";
   private static final String COLOR_VERTEX_BUFFER_DEBUG_LABEL = "targetEspColorVertices";
   private static final String TARGET_ESP_PASS_DEBUG_LABEL = "targetEsp";
   private static final String TEXTURED_VERTEX_BUFFER_DEBUG_LABEL = "targetEspVertices";
   private static final int гГ = 16777215;
   private static final String TARGET_ESP_TRIANGLE_LINES_PASS_DEBUG_LABEL = "targetEspTriangleLines";
   private static final String TRIANGLE_LINES_VERTEX_BUFFER_DEBUG_LABEL = "targetEspTriangleLines";
   private static final String TARGET_ESP_LIGHTNING_CORE_PASS_DEBUG_LABEL = "targetEspLightningCore";
   private static final String TARGET_ESP_CUBE_COMPOSITE_PASS_DEBUG_LABEL = "targetEspCubeComposite";
   private static final String TARGET_TEXTURE_NAMESPACE = "rose";
   private static final String TARGET_TEXTURE_PATH = "icons/world_render/target.png";
   private static final String TARGET_ALTERNATE_TEXTURE_NAMESPACE = "rose";
   private static final String TARGET_ALTERNATE_TEXTURE_PATH = "icons/world_render/target1.png";
   private static final String GHOST_FIRE_TEXTURE_NAMESPACE = "rose";
   private static final String GHOST_FIRE_TEXTURE_PATH = "icons/world_render/firepart2.png";
   private static final String GHOST_TEXTURE_NAMESPACE = "rose";
   private static final String GHOST_TEXTURE_PATH = "textures/targetesp/zenith_ghost.png";
   private static final String WORLD_BLOOM_TEXTURE_NAMESPACE = "rose";
   private static final String WORLD_BLOOM_TEXTURE_PATH = "icons/world_render/bloom.png";
   private static final String GHOST_PARTICLE_BLOOM_TEXTURE_NAMESPACE = "rose";
   private static final String GHOST_PARTICLE_BLOOM_TEXTURE_PATH = "images/particles/bloom.png";
   private static final String CRYSTAL_PARTICLE_BLOOM_TEXTURE_NAMESPACE = "rose";
   private static final String CRYSTAL_PARTICLE_BLOOM_TEXTURE_PATH = "images/particles/bloom.png";
   private static final String TARGET_GLOW_TEXTURE_NAMESPACE = "rose";
   private static final String TARGET_GLOW_TEXTURE_PATH = "images/targetesp/glow.png";
   private static final String LIGHTNING_ARC_TEXTURE_NAMESPACE = "rose";
   private static final String LIGHTNING_ARC_TEXTURE_PATH = "textures/targetesp/lightning_arc.png";
   private static final String TARGET_PIPELINE_LOCATION_ID = "rose/target_esp";
   private static final String TARGET_PIPELINE_VERTEX_SHADER_ID = "dead_chams";
   private static final String TARGET_PIPELINE_FRAGMENT_SHADER_ID = "dead_chams";
   private static final String TARGET_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String GHOST_NO_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_ghost_no_depth";
   private static final String GHOST_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID = "zenith_target_esp";
   private static final String GHOST_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "zenith_target_esp";
   private static final String GHOST_NO_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String GHOST_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_ghost_depth";
   private static final String GHOST_DEPTH_PIPELINE_VERTEX_SHADER_ID = "zenith_target_esp";
   private static final String GHOST_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "zenith_target_esp";
   private static final String GHOST_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String CRYSTAL_COLOR_PIPELINE_LOCATION_ID = "rose/target_esp_crystal_color";
   private static final String CRYSTAL_COLOR_PIPELINE_VERTEX_SHADER_ID = "target_esp_crystal";
   private static final String CRYSTAL_COLOR_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_crystal";
   private static final String CRYSTAL_COLOR_NO_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_crystal_color_no_depth";
   private static final String CRYSTAL_COLOR_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID = "target_esp_crystal";
   private static final String CRYSTAL_COLOR_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_crystal";
   private static final String CRYSTAL_GLOW_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_crystal_glow_depth";
   private static final String CRYSTAL_GLOW_DEPTH_PIPELINE_VERTEX_SHADER_ID = "dead_chams";
   private static final String CRYSTAL_GLOW_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "dead_chams";
   private static final String CRYSTAL_GLOW_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String ORBIT_CRYSTAL_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_orbit_crystal_depth";
   private static final String ORBIT_CRYSTAL_DEPTH_PIPELINE_VERTEX_SHADER_ID = "target_esp_crystal";
   private static final String ORBIT_CRYSTAL_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_crystal";
   private static final String ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_orbit_crystal_no_depth";
   private static final String ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID = "target_esp_crystal";
   private static final String ORBIT_CRYSTAL_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_crystal";
   private static final String ORBIT_GLOW_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_orbit_glow_depth";
   private static final String ORBIT_GLOW_DEPTH_PIPELINE_VERTEX_SHADER_ID = "target_esp_orbit_glow";
   private static final String ORBIT_GLOW_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_orbit_glow";
   private static final String ORBIT_GLOW_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String ORBIT_GLOW_NO_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_orbit_glow_no_depth";
   private static final String ORBIT_GLOW_NO_DEPTH_PIPELINE_VERTEX_SHADER_ID = "target_esp_orbit_glow";
   private static final String ORBIT_GLOW_NO_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_orbit_glow";
   private static final String ORBIT_GLOW_NO_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String RING_PIPELINE_LOCATION_ID = "rose/target_esp_ring";
   private static final String RING_PIPELINE_VERTEX_SHADER_ID = "target_esp_ring";
   private static final String RING_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_ring";
   private static final String HOOP_PIPELINE_LOCATION_ID = "rose/target_esp_hoop";
   private static final String HOOP_PIPELINE_VERTEX_SHADER_ID = "target_esp_ring";
   private static final String HOOP_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_ring";
   private static final String RING_WAVE_PIPELINE_LOCATION_ID = "rose/target_esp_ring_wave";
   private static final String RING_WAVE_PIPELINE_VERTEX_SHADER_ID = "target_esp_cube";
   private static final String RING_WAVE_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_cube";
   private static final String CUBE_PIPELINE_LOCATION_ID = "rose/target_esp_cube";
   private static final String CUBE_PIPELINE_VERTEX_SHADER_ID = "target_esp_cube";
   private static final String CUBE_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_cube";
   private static final String CUBE_COMPOSITE_PIPELINE_LOCATION_ID = "rose/target_esp_cube_composite";
   private static final String CUBE_COMPOSITE_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String CUBE_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_cube_composite";
   private static final String CUBE_COMPOSITE_PIPELINE_IN_SAMPLER_NAME = "InSampler";
   private static final String LIGHTNING_ARC_PIPELINE_LOCATION_ID = "rose/target_esp_lightning_arc";
   private static final String LIGHTNING_ARC_PIPELINE_VERTEX_SHADER_ID = "dead_chams";
   private static final String LIGHTNING_ARC_PIPELINE_FRAGMENT_SHADER_ID = "dead_chams";
   private static final String LIGHTNING_ARC_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String LIGHTNING_ARC_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_lightning_arc_depth";
   private static final String LIGHTNING_ARC_DEPTH_PIPELINE_VERTEX_SHADER_ID = "dead_chams";
   private static final String LIGHTNING_ARC_DEPTH_PIPELINE_FRAGMENT_SHADER_ID = "dead_chams";
   private static final String LIGHTNING_ARC_DEPTH_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String LIGHTNING_PIPELINE_LOCATION_ID = "rose/target_esp_lightning";
   private static final String LIGHTNING_PIPELINE_VERTEX_SHADER_ID = "target_esp_lightning";
   private static final String LIGHTNING_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_lightning";
   private static final String LIGHTNING_CORE_PIPELINE_LOCATION_ID = "rose/target_esp_lightning_core";
   private static final String LIGHTNING_CORE_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String LIGHTNING_CORE_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_lightning_core";
   private static final String LIGHTNING_CORE_PIPELINE_IN_SAMPLER_NAME = "InSampler";
   private static final String TRIANGLE_LINES_PIPELINE_LOCATION_ID = "rose/target_esp_triangle_lines";
   private static final String PENTAGRAM_PIPELINE_LOCATION_ID = "rose/target_esp_pentagram";
   private static final String PENTAGRAM_PIPELINE_GLOBALS_UNIFORM_NAME = "Globals";
   private static final String PENTAGRAM_PIPELINE_VERTEX_SHADER_ID = "target_esp_pentagram";
   private static final String PENTAGRAM_PIPELINE_FRAGMENT_SHADER_ID = "target_esp_pentagram";
   private static final float field_07 = 1.5F;
   private static final float field_0Р = -1.5F;
   private static final float field_03 = -1.0F;
   private static final float field_0р = -1.0F;
   private static final float field_06 = 0.8F;
   private static final float field_0_ = 0.6F;
   private static final float field_0Ш = 0.9F;
   private static final float field_0ы = 0.7F;
   private static final float field_0й = 0.5F;
   private static final float field_0Ж = 0.4F;
   private static final float field_0м = 0.6F;
   private static final int field_0К = Integer.MIN_VALUE;
   private static final int field_0I = Integer.MIN_VALUE;
   private static final int field_0Т = -11486721;
   private static final float field_0О = 1.2F;
   private static final String INITIAL_RING_TYPE_LABEL = "Тип 1";
   private static final String INITIAL_SQUARE_TYPE_LABEL = "Тип 1";
   private static final int field_0к = Integer.MIN_VALUE;
   private static final String INITIAL_GHOST_TYPE_LABEL = "Тип 1";
   private static final String INITIAL_CRYSTAL_TYPE_LABEL = "Тип 1";
   private static final String INITIAL_LIGHTNING_TYPE_LABEL = "Тип 2";
   private static final int field_0У = Integer.MIN_VALUE;
   private static final int field_0З = Integer.MIN_VALUE;
   private static final float field_0Е = 12.0F;
   private static final String RING_PRIVATE_GLOW_TEXTURE_IMAGE_PATH = "private/glow";
   private static final String PRIVATE_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_private_depth";
   private static final String PRIVATE_NO_DEPTH_PIPELINE_LOCATION_ID = "rose/target_esp_private_no_depth";
   private static final float field_0Б = 0.2F;
   private static final int field_0С = Integer.MIN_VALUE;

   public static boolean hasVisibleTarget() {
      return fadingTarget != null && targetVisibility > рЕ;
   }

   private static void renderPentagram(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, float hurtProgress) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      float visibility = easeOutCubic(alpha);
      if (!(visibility <= Тю)) {
         float height = target.getBbHeight();
         float sizeEnvelope = Mth.lerp(visibility, ТЩ, 1.0F) * easeOutBack(targetVisibility);
         sizeEnvelope *= 1.0F + hurtProgress * Ту;
         float sizePulse = 1.0F + Т2 * (float)Math.sin(effectElapsedSeconds * Тф);
         float radius = Math.max(Ти, target.getBbWidth() * Т9 + То) * sizeEnvelope * sizePulse;
         Matrix4f pose = event.getPoses().last().pose();
         BufferBuilder pentagramBuffer = beginQuads();
         Vec3[] groundBasis = rotateBasis(createUprightBasis(new Vec3(0.0, 1.0, 0.0)), -effectElapsedSeconds * ТO);
         appendScaledOrientedBillboard(pentagramBuffer, pose, targetPosition.add(0.0, Т7, 0.0), groundBasis[0], groundBasis[1], radius * ТР, multiplyAlpha(brightenColor(color, Т3), visibility * Тр));
         double var14 = effectElapsedSeconds * Т6 + target.getId() * Т_;
         double var16 = Math.toRadians(ТШ + Ты * Math.sin(effectElapsedSeconds * Тй));
         Vec3 normal = new Vec3(Math.sin(var16) * Math.cos(var14), Math.cos(var16), Math.sin(var16) * Math.sin(var14));
         Vec3[] basis = rotateBasis(createUprightBasis(normal), effectElapsedSeconds * ТЖ);
         Vec3 center = targetPosition.add(0.0, height * Тм, 0.0);
         appendScaledOrientedBillboard(pentagramBuffer, pose, center, basis[0], basis[1], radius, multiplyAlpha(color, visibility * (ТК + hurtProgress * ТI)));
         drawColorGeometry(pentagramBuffer, PENTAGRAM_PIPELINE, true);
         float pentagonRadius = radius * ТТ;
         BufferBuilder glowBuffer = beginQuads();
         appendCameraFacingBillboard(glowBuffer, pose, center.x, center.y, center.z, radius * (ТО + hurtProgress * ТФ), multiplyAlpha(brightenColor(color, ТЙ), visibility * (Тк + hurtProgress * Тг)));
         appendCameraFacingBillboard(glowBuffer, pose, center.x, center.y, center.z, radius * Т0, multiplyAlpha(brightenColor(color, Тх), visibility * (ТУ + hurtProgress * ТЗ)));

         for (int var23 = 0; var23 < 5; var23++) {
            Vec3 var24 = getPentagonPoint(center, basis[0], basis[1], pentagonRadius, var23);
            float var25 = ТЕ + ТП * (float)Math.sin(effectElapsedSeconds * Т西 + var23 * Тд);
            appendCameraFacingBillboard(glowBuffer, pose, var24.x, var24.y, var24.z, ТБ * var25, multiplyAlpha(brightenColor(color, ТС), visibility * Тз));
            appendCameraFacingBillboard(glowBuffer, pose, var24.x, var24.y, var24.z, Тц * var25, multiplyAlpha(brightenColor(color, Тя), visibility * Ос));
         }

         int[] pentagramOrder = new int[]{0, 2, 4, 1, 3};

         for (int var36 = 0; var36 < 3; var36++) {
            float var38 = (effectElapsedSeconds * ОВ + var36 / ОЯ) % 1.0F;
            float var26 = var38 * ОХ;
            int var27 = Math.min(4, (int)var26);
            float var28 = var26 - var27;
            Vec3 var29 = getPentagonPoint(center, basis[0], basis[1], pentagonRadius, pentagramOrder[var27]);
            Vec3 var30 = getPentagonPoint(center, basis[0], basis[1], pentagonRadius, pentagramOrder[(var27 + 1) % 5]);
            Vec3 var31 = var29.lerp(var30, var28);
            float var32 = Ое - var36 * ОА;
            appendCameraFacingBillboard(glowBuffer, pose, var31.x, var31.y, var31.z, var32, multiplyAlpha(brightenColor(color, О4), visibility * (Оп - var36 * ОД)));
            appendCameraFacingBillboard(glowBuffer, pose, var31.x, var31.y, var31.z, var32 * ОЪ, multiplyAlpha(brightenColor(color, От), visibility * (Оа - var36 * ОЮ)));
         }

         for (int var37 = 0; var37 < 5; var37++) {
            double var39 = effectElapsedSeconds * ОЧ + var37 * ОЛ * О诶 / О1;
            Vec3 var40 = center.add(basis[0].scale(Math.cos(var39) * radius * ОЬ)).add(basis[1].scale(Math.sin(var39) * radius * О衣));
            appendCameraFacingBillboard(glowBuffer, pose, var40.x, var40.y, var40.z, Ош, multiplyAlpha(brightenColor(color, ОИ), visibility * Оь));
         }

         drawTexturedGeometry(glowBuffer, WORLD_BLOOM_TEXTURE, true);
      }
   }

   private static void appendFacingCrystal(BufferBuilder buffer, PoseStack poses, float x, float y, float z, float size, int color, float targetHeight, float spinDegrees) {
      poses.pushPose();
      poses.translate(x, y, z);
      float var9 = targetHeight * кЕ;
      float var10 = -x;
      float var11 = var9 - y;
      float var12 = -z;
      float var13 = var10 * var10 + var11 * var11 + var12 * var12;
      if (var13 > кП) {
         float var14 = 1.0F / (float)Math.sqrt(var13);
         float var15 = Mth.clamp(var11 * var14, к西, 1.0F);
         float var16 = (float)Math.toDegrees(Math.atan2(var10, var12));
         float var17 = -((float)Math.toDegrees(Math.asin(var15)));
         poses.mulPose(Axis.YP.rotationDegrees(var16));
         poses.mulPose(Axis.XP.rotationDegrees(var17));
      }

      poses.mulPose(Axis.ZP.rotationDegrees(spinDegrees));
      poses.scale(size * кд, size * кБ, size * кС);
      Matrix4f pose = poses.last().pose();
      float var19 = кз;
      float var20 = кц;
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, var20, -var19, 0.0F, 0.0F, 0.0F, var19, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, var20, 0.0F, var19, 0.0F, var19, 0.0F, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, var20, var19, 0.0F, 0.0F, 0.0F, -var19, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, var20, 0.0F, -var19, 0.0F, -var19, 0.0F, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, -var20, 0.0F, var19, 0.0F, -var19, 0.0F, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, -var20, var19, 0.0F, 0.0F, 0.0F, var19, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, -var20, 0.0F, -var19, 0.0F, var19, 0.0F, 0.0F, color);
      appendTriangleQuad(buffer, pose, 0.0F, 0.0F, -var20, -var19, 0.0F, 0.0F, 0.0F, -var19, 0.0F, color);
      poses.popPose();
   }

   private static float getTrailWidthFactor(int index, int pointCount) {
      float var2 = (float)index / Math.max(1, pointCount - 1);
      return Ж西 + Жд * (float)Math.sin(ЖБ * Math.pow(var2, ЖС));
   }

   private static void renderCrystalType1(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha, long nowMillis) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      initializeCrystalType1Offsets(target);
      float var8 = (float)(nowMillis % field_66) / field_6_;
      float var9 = (float)nowMillis * field_6Ш;
      float var10 = field_6ы - field_6й * alpha;
      float var11 = (float)Math.sin(Math.toRadians(var8));
      float var12 = (float)Math.cos(Math.toRadians(var8));
      BufferBuilder var13 = beginQuads();
      PoseStack poses = event.getPoses();

      for (int var15 = 0; var15 < crystalType1Offsets.size(); var15++) {
         Vector3f var16 = crystalType1Offsets.get(var15);
         float var17 = var16.x * var12 - var16.z * var11;
         float var18 = var16.x * var11 + var16.z * var12;
         float var19 = var17 * var10;
         float var20 = var18 * var10;
         float var21 = var16.y + (float)(field_6Ж * Math.sin(var9 * field_6м + var15 * field_6К));
         appendCameraFacingBillboard(var13, poses, targetPosition.x + var19, targetPosition.y + var21, targetPosition.z + var20, field_6I, multiplyAlpha(primaryColor, alpha * field_6Т));
         appendCameraFacingBillboard(var13, poses, targetPosition.x + var19, targetPosition.y + var21, targetPosition.z + var20, field_6О, multiplyAlpha(brightenColor(primaryColor, field_6Ф), alpha * field_6Й));
      }

      drawTexturedGeometry(var13, WORLD_BLOOM_TEXTURE, true, getCrystalGlowPipeline());
      BufferBuilder var32 = beginQuads();

      for (int var33 = 0; var33 < crystalType1Offsets.size(); var33++) {
         Vector3f var34 = crystalType1Offsets.get(var33);
         float var35 = var34.x * var12 - var34.z * var11;
         float var36 = var34.x * var11 + var34.z * var12;
         float var37 = var35 * var10;
         float var38 = var36 * var10;
         float var22 = var34.y + (float)(field_6к * Math.sin(var9 * field_6г + var33 * field_60));
         float var23 = var8 * (var33 % 2 == 0 ? field_6х : field_6У) + var33 * field_6З;
         float var24 = (float)Math.sin(var33 * field_6Е + field_6П) * field_6西;
         var24 -= (float)Math.floor(var24);
         float var25 = field_6д + var24 * field_6Б;
         float var26 = field_6С + ((float)Math.sin(var9 * field_6з + var33 * field_6ц) + 1.0F) * field_6я;
         int var27 = ColorMath.packArgb(ColorMath.getRed(primaryColor), ColorMath.getGreen(primaryColor), ColorMath.getBlue(primaryColor), Math.round(_с * alpha));
         int var28 = ColorMath.packArgb(ColorMath.getRed(primaryColor), ColorMath.getGreen(primaryColor), ColorMath.getBlue(primaryColor), Math.round(_В * alpha));
         int var29 = brightenColor(var27, var26);
         int var30 = ColorMath.lerpArgb(var29, -1, _Я);
         poses.pushPose();
         poses.translate(targetPosition.x, targetPosition.y, targetPosition.z);
         appendRotatingRhomb(var32, poses, var37, var22, var38, var25 * _Х, var29, var30, var23, true, var33, true);
         appendRotatingRhomb(var32, poses, var37, var22, var38, var25, var29, var30, var23, true, var33, false);
         poses.popPose();
      }

      drawColorGeometry(var32, getCrystalPipeline());
   }

   private static void appendTexturedVertex(BufferBuilder buffer, Matrix4f pose, float x, float y, float z, float u, float v, int color) {
      appendTexturedVertex(buffer, pose, x, y, z, u, v, ColorMath.toRgbaVector(color));
   }

   private static void appendParticleHalo(BufferBuilder buffer, Matrix4f pose, double x, double y, double z, int color, float alpha) {
      if (!(alpha <= ых)) {
         float var10 = smoothstep(ghostHurtCycleProgress);
         double var11 = ыУ * var10;
         int var13 = multiplyAlpha(color, alpha);
         int var14 = multiplyAlpha(color, alpha * ыЗ);
         float var15 = ыЕ + ыП * (1.0F - var10);
         double var16 = ы西 * var10;

         for (int var18 = 0; var18 < 26; var18++) {
            double var19 = ыд * var18 / ыБ + ghostOrbitalPhase * ыС;
            double var21 = x + Math.cos(var19) * var11;
            double var23 = z + Math.sin(var19) * var11;
            double var25 = y + Math.sin(var19 * ыз + ghostHurtCycleProgress * ыц) * var16 * ыя;
            appendCameraFacingBillboard(buffer, pose, var21, var25, var23, var15, var13);
            appendCameraFacingBillboard(buffer, pose, var21, var25, var23, var15 * йс, var14);
         }
      }
   }

   private static void renderRingType3(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      BufferBuilder buffer = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      Vec3 cameraRelativePosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      PoseStack poses = new PoseStack();
      poses.mulPose(event.getPoses().last().pose());
      double phase = effectElapsedSeconds * Й西;
      Quaternionf cameraRotation = MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation();
      TargetRingGeometry.Ж(target.getBbWidth(), target.getBbHeight(), phase, alpha, ringParticleSize, ringLayers, (particleX, particleY, particleZ, particleSize, particleAlpha) -> {
         if (particleAlpha != 0) {
            poses.pushPose();
            poses.translate(cameraRelativePosition.x + particleX, cameraRelativePosition.y + particleY, cameraRelativePosition.z + particleZ);
            poses.mulPose(cameraRotation);
            GlowQuadRendering.Х(buffer, poses.last().pose(), particleSize, color & гГ | particleAlpha << 24);
            poses.popPose();
         }
      });
      drawTexturedGeometry(buffer, RING_PRIVATE_GLOW_TEXTURE, false, ringThroughWalls ? PRIVATE_NO_DEPTH_PIPELINE : PRIVATE_DEPTH_PIPELINE);
   }

   private static float fractalNoise2d(float x, float y) {
      float var2 = 0.0F;
      float var3 = мА;
      float var4 = 1.0F;

      for (int var5 = 0; var5 < 4; var5++) {
         var2 += noise2d(x * var4, y * var4) * var3;
         var4 *= м4;
         var3 *= мп;
      }

      return Mth.clamp(var2, 0.0F, 1.0F);
   }

   private static void renderGhostType7(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      if (orbitTrailEntityId != target.getId()) {
         orbitTrail.clear();
         lastOrbitTrailSampleMillis = 0L;
         orbitTrailEntityId = target.getId();
      }

      Vec3 var5 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      double var6 = targetPosition.x;
      double var8 = targetPosition.y + target.getBbHeight() / ын;
      double var10 = targetPosition.z;
      double var12 = var6 - var5.x;
      double var14 = var8 - var5.y;
      double var16 = var10 - var5.z;
      long var18 = System.currentTimeMillis();
      PoseStack poses = event.getPoses();
      BufferBuilder buffer = beginQuads();
      appendFadingTrail(poses, buffer, var5, alpha, color);

      for (int var22 = 0; var22 < 20; var22++) {
         float var23 = 1.0F - var22 / ыЦ * ыч;
         double var24 = ыЭ * (var18 - var22 * ы必) / ыщ;
         float var26 = (float)(ыГ * var23);
         appendGhostOrbitSprite(poses, buffer, var12, var14, var16, var24, 1.0, 1.0, ыл, var26, alpha, color);
         appendGhostOrbitSprite(poses, buffer, var12, var14, var16, var24, ыэ, 1.0, ыю, var26, alpha, color);
         appendGhostOrbitSprite(poses, buffer, var12, var14, var16, var24, ыЩ, ыу, 1.0, var26, alpha, color);
      }

      emitOrbitTrailSamples(var6, var8, var10, var18, alpha);
      drawTexturedGeometry(buffer, GHOST_FIRE_TEXTURE, true);
   }

   private static int brightenColor(int color, float amount) {
      amount = Mth.clamp(amount, 0.0F, 1.0F);
      return ColorMath.packArgb(
         Math.round(Mth.lerpInt(amount, ColorMath.getRed(color), 255)),
         Math.round(Mth.lerpInt(amount, ColorMath.getGreen(color), 255)),
         Math.round(Mth.lerpInt(amount, ColorMath.getBlue(color), 255)),
         ColorMath.getAlpha(color)
      );
   }

   private static void appendExplodingCubeCorners(
      BufferBuilder buffer,
      Matrix4f pose,
      Vec3 origin,
      Vec3 cameraPosition,
      double xRotationRadians,
      double yRotationRadians,
      double halfSize,
      double edgeLength,
      double nodeRadius,
      double groundY,
      float explosionProgress,
      int color
   ) {
      for (int var18 = -1; var18 <= 1; var18 += 2) {
         for (int var19 = -1; var19 <= 1; var19 += 2) {
            for (int var20 = -1; var20 <= 1; var20 += 2) {
               double var21 = 1.0 - Math.pow(1.0F - explosionProgress, Ш0);
               Vec3 var23 = rotateVector(new Vec3(var18 * halfSize, var19 * halfSize, var20 * halfSize), xRotationRadians, yRotationRadians, 0.0);
               Vec3 var24 = new Vec3(var23.x, 0.0, var23.z).normalize();
               double var25 = var21 * (Шх + (var19 > 0 ? ШУ : 0.0) + (var18 * var20 > 0 ? ШЗ : 0.0));
               double var27 = getCubeCornerHeight(var23.y, groundY, explosionProgress, var18, var19, var20);
               Vec3 var29 = new Vec3(var23.x + var24.x * var25, var27, var23.z + var24.z * var25);
               double var30 = var21 * (ШЕ + (var18 * var19 * var20 > 0 ? ШП : Ш西));
               Vec3 var32 = rotateVector(new Vec3(-var18, 0.0, 0.0), xRotationRadians, yRotationRadians, 0.0);
               Vec3 var33 = rotateVector(new Vec3(0.0, -var19, 0.0), xRotationRadians, yRotationRadians, 0.0);
               Vec3 var34 = rotateVector(new Vec3(0.0, 0.0, -var20), xRotationRadians, yRotationRadians, 0.0);
               var32 = rotateVector(var32, var30 * var20, var30 * var19, var30 * var18);
               var33 = rotateVector(var33, var30 * var20, var30 * var19, var30 * var18);
               var34 = rotateVector(var34, var30 * var20, var30 * var19, var30 * var18);
               appendCornerEdges(buffer, pose, origin, cameraPosition, var29, var32, var33, var34, edgeLength, nodeRadius, color);
            }
         }
      }
   }

   private static int oscillateColors(int primaryColor, int secondaryColor, float phaseOffset) {
      float var3 = ((float)System.currentTimeMillis() / гб + phaseOffset) % гЫ;
      float var4 = ((float)Math.sin(Math.toRadians(var3)) + 1.0F) * гl;
      return ColorMath.lerpArgb(primaryColor, secondaryColor, var4);
   }

   private static Vec3[] rotateBasis(Vec3[] basis, double angleRadians) {
      double var3 = Math.cos(angleRadians);
      double var5 = Math.sin(angleRadians);
      Vec3 var7 = basis[0];
      Vec3 var8 = basis[1];
      return new Vec3[]{var7.scale(var3).add(var8.scale(var5)), var7.scale(-var5).add(var8.scale(var3))};
   }

   private static RenderPipeline getCrystalPipeline() {
      return crystalThroughWalls ? CRYSTAL_COLOR_NO_DEPTH_PIPELINE : CRYSTAL_COLOR_PIPELINE;
   }

   private static void appendRotatedBillboard(BufferBuilder buffer, Matrix4f pose, double x, double y, double z, float size, float yawDegrees, float pitchDegrees, int color) {
      Vector4f var12 = ColorMath.toRgbaVector(color);
      float var13 = size * кВ;
      float var14 = (float)Math.toRadians(yawDegrees);
      float var15 = (float)Math.toRadians(pitchDegrees);
      float var16 = (float)Math.cos(var14) * var13;
      float var17 = (float)Math.sin(var14) * var13;
      float var18 = (float)Math.sin(var14) * (float)Math.sin(var15) * var13;
      float var19 = (float)Math.cos(var15) * var13;
      float var20 = -((float)Math.cos(var14)) * (float)Math.sin(var15) * var13;
      appendTexturedVertex(buffer, pose, (float)x - var16 - var18, (float)y - var19, (float)z - var17 - var20, 0.0F, 1.0F, var12);
      appendTexturedVertex(buffer, pose, (float)x - var16 + var18, (float)y + var19, (float)z - var17 + var20, 0.0F, 0.0F, var12);
      appendTexturedVertex(buffer, pose, (float)x + var16 + var18, (float)y + var19, (float)z + var17 + var20, 1.0F, 0.0F, var12);
      appendTexturedVertex(buffer, pose, (float)x + var16 - var18, (float)y - var19, (float)z + var17 - var20, 1.0F, 1.0F, var12);
   }

   private static Vec3 getPentagonPoint(Vec3 center, Vec3 basisX, Vec3 basisY, float radius, int index) {
      double var5 = О弟 + index * Об * ОЫ / Оl;
      return center.add(basisX.scale(Math.cos(var5) * radius)).add(basisY.scale(Math.sin(var5) * radius));
   }

   private static TextureTarget getLightningMask(int width, int height) {
      if (lightningMask == null) {
         lightningMask = new TextureTarget(LIGHTNING_MASK_DEBUG_LABEL, width, height, true, GpuFormat.RGBA8_UNORM);
      } else if (lightningMask.width != width || lightningMask.height != height) {
         lightningMask.resize(width, height);
      }

      return lightningMask;
   }

   private static void drawColorGeometry(BufferBuilder buffer, RenderPipeline pipeline) {
      drawColorGeometry(buffer, pipeline, false);
   }

   private static int getHurtColor(LivingEntity target, int baseColor, float deltaSeconds, long nowMillis) {
      if (ringHurtEntityId != target.getId()) {
         ringHurtEntityId = target.getId();
         previousRingHurtTime = target.hurtTime;
         ringHurtTriggerMillis = 0L;
         ringHurtBlend = 0.0F;
      }

      if (target.hurtTime > previousRingHurtTime) {
         ringHurtTriggerMillis = nowMillis;
      }

      previousRingHurtTime = target.hurtTime;
      if (!hurtRecolorEnabled) {
         ringHurtBlend = 0.0F;
         return baseColor;
      } else {
         float var5 = Mth.clamp((float)(nowMillis - ringHurtTriggerMillis) / Йд, 0.0F, 1.0F);
         float var6 = 1.0F - easeOutCubic(var5);
         float var7 = 1.0F - (float)Math.pow(ЙБ, deltaSeconds * ЙС);
         ringHurtBlend = Mth.lerp(var7, ringHurtBlend, var6);
         return ringHurtBlend < Йз ? baseColor : ColorMath.lerpArgb(baseColor, Йц, ringHurtBlend);
      }
   }

   private static float quinticSmoothstep(float value) {
      return value * value * value * (value * (value * Жз - Жц) + Жя);
   }

   private static List<Vec3> createJitteredPath(Vec3 start, Vec3 end, int depth, double amplitude) {
      if (depth <= 0) {
         ArrayList var13 = new ArrayList(2);
         var13.add(start);
         var13.add(end);
         return var13;
      } else {
         Vec3 var5 = end.subtract(start);
         Vec3 var6 = start.add(var5.scale(мЖ));
         Vec3 var7 = randomPerpendicular(var5);
         double var8 = (EFFECT_RANDOM.nextDouble() - мм) * мК * amplitude;
         Vec3 var10 = var6.add(var7.scale(var8));
         List var11 = createJitteredPath(start, var10, depth - 1, amplitude * мI);
         List var12 = createJitteredPath(var10, end, depth - 1, amplitude * мТ);
         var11.remove(var11.size() - 1);
         var11.addAll(var12);
         return var11;
      }
   }

   private static RenderPipeline getOrbitCrystalPipeline() {
      return crystalThroughWalls ? ORBIT_CRYSTAL_NO_DEPTH_PIPELINE : ORBIT_CRYSTAL_DEPTH_PIPELINE;
   }

   private static void drawTexturedGeometry(BufferBuilder buffer, Identifier texture, boolean submitDistortion, RenderPipeline pipeline) {
      MeshData mesh = buffer.build();
      if (mesh != null) {
         GpuBuffer vertexBuffer = null;

         try {
            DrawState drawState = mesh.drawState();
            int indexCount = drawState.indexCount();
            AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
            GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
            IndexType indexType = sequentialIndices.type();
            vertexBuffer = RenderSystem.getDevice().createBuffer(() -> TEXTURED_VERTEX_BUFFER_DEBUG_LABEL, 32, mesh.vertexBuffer());
            if (submitDistortion && distortionEnabled) {
               GlowDistortionRenderer.INSTANCE.renderMaskAndDistort(vertexBuffer, indexBuffer, indexType, indexCount, distortionStrength);
            }

            RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            AbstractTexture textureObject = MINECRAFT_CLIENT.getTextureManager().getTexture(texture);

            try (RenderPass pass = encoder.createRenderPass(
                  () -> TARGET_ESP_PASS_DEBUG_LABEL, mainTarget.getColorTextureView(), Optional.empty(), mainTarget.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(pass);
               pass.setPipeline(pipeline);
               pass.bindTexture(DRAW_TEXTURED_GEOMETRY_SAMPLER0_NAME, textureObject.getTextureView(), textureObject.getSampler());
               pass.setVertexBuffer(0, vertexBuffer.slice());
               pass.setIndexBuffer(indexBuffer, indexType);
               pass.drawIndexed(indexCount, 1, 0, 0, 0);
            }
         } finally {
            if (vertexBuffer != null) {
               vertexBuffer.close();
            }

            mesh.close();
         }
      }
   }

   private static RenderPipeline getCrystalGlowPipeline() {
      return crystalThroughWalls ? TARGET_PIPELINE : CRYSTAL_GLOW_DEPTH_PIPELINE;
   }

   private static float smoothstep(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      return value * value * (гМ - 2.0F * value);
   }

   private static void drawTexturedGeometry(BufferBuilder buffer, Identifier texture) {
      drawTexturedGeometry(buffer, texture, false);
   }

   private static void appendGrowingLightningTube(BufferBuilder buffer, Matrix4f pose, List<Vec3> points, float growth, float tubeRadius, int color, float seed, float fadeProgress) {
      int var8 = points.size() - 1;
      if (var8 > 0 && !(growth <= 0.0F) && !(tubeRadius <= Кх)) {
         float var9 = growth * var8;

         for (int var10 = 0; var10 < var8 && !(var9 <= var10); var10++) {
            float var11 = Mth.clamp(var9 - var10, 0.0F, 1.0F);
            float var12 = hashArcUnitFloat(var10 + 1, seed);
            float var13 = 1.0F - smoothstep(Mth.clamp((fadeProgress - var12 * КУ) / КЗ, 0.0F, 1.0F));
            if (!(var13 <= КЕ)) {
               Vec3 var14 = (Vec3)points.get(var10);
               Vec3 var15 = (Vec3)points.get(var10 + 1);
               Vec3 var16 = var11 < 1.0F ? var14.lerp(var15, var11) : var15;
               Vec3 var17 = var16.subtract(var14);
               if (!(var17.lengthSqr() < КП)) {
                  var17 = var17.normalize();
                  Vec3 var18 = Math.abs(var17.y) < К西 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
                  Vec3 var19 = var17.cross(var18);
                  if (var19.lengthSqr() < Кд) {
                     var19 = new Vec3(0.0, 0.0, 1.0);
                  }

                  var19 = var19.normalize();
                  Vec3 var20 = var17.cross(var19).normalize();
                  if (var10 > 0) {
                     var14 = var14.subtract(var17.scale(tubeRadius * КБ));
                  }

                  if (var11 >= 1.0F && var10 < var8 - 1) {
                     var16 = var16.add(var17.scale(tubeRadius * КС));
                  }

                  float var21 = (var10 + var11 * Кз) / var8;
                  float var22 = Mth.lerp(var21, Кц, Кя);
                  float var23 = var11 < 1.0F ? Iс : 1.0F;
                  float var24 = tubeRadius * var22 * var23;
                  float var25 = var24 * Mth.lerp(var11, 1.0F, IВ);
                  float var26 = (1.0F - var21 * IЯ) * (IХ + var12 * Iе) * var13;
                  int var27 = multiplyAlpha(color, var26);
                  byte var28 = 6;

                  for (int var29 = 0; var29 < var28; var29++) {
                     double var30 = IА * var29 / var28;
                     double var32 = I4 * (var29 + 1) / var28;
                     Vec3 var34 = var14.add(var19.scale(Math.cos(var30) * var24)).add(var20.scale(Math.sin(var30) * var24));
                     Vec3 var35 = var14.add(var19.scale(Math.cos(var32) * var24)).add(var20.scale(Math.sin(var32) * var24));
                     Vec3 var36 = var16.add(var19.scale(Math.cos(var30) * var25)).add(var20.scale(Math.sin(var30) * var25));
                     Vec3 var37 = var16.add(var19.scale(Math.cos(var32) * var25)).add(var20.scale(Math.sin(var32) * var25));
                     appendTexturedVertex(buffer, pose, (float)var34.x, (float)var34.y, (float)var34.z, 0.0F, (float)var29 / var28, var27);
                     appendTexturedVertex(buffer, pose, (float)var35.x, (float)var35.y, (float)var35.z, 0.0F, (var29 + 1.0F) / var28, var27);
                     appendTexturedVertex(buffer, pose, (float)var37.x, (float)var37.y, (float)var37.z, var11, (var29 + 1.0F) / var28, var27);
                     appendTexturedVertex(buffer, pose, (float)var36.x, (float)var36.y, (float)var36.z, var11, (float)var29 / var28, var27);
                  }
               }
            }
         }
      }
   }

   private static void appendSphere(BufferBuilder buffer, Matrix4f pose, Vec3 origin, Vec3 cameraPosition, Vec3 center, double radius, int latitudeSegments, int longitudeSegments, int color) {
      for (int var10 = 0; var10 < latitudeSegments; var10++) {
         double var11 = ыД + ыЪ * var10 / latitudeSegments;
         double var13 = ыт + ыа * (var10 + 1) / latitudeSegments;

         for (int var15 = 0; var15 < longitudeSegments; var15++) {
            double var16 = ыЮ * var15 / longitudeSegments;
            double var18 = ыЧ * (var15 + 1) / longitudeSegments;
            Vec3 var20 = getSpherePoint(center, radius, var11, var16);
            Vec3 var21 = getSpherePoint(center, radius, var11, var18);
            Vec3 var22 = getSpherePoint(center, radius, var13, var18);
            Vec3 var23 = getSpherePoint(center, radius, var13, var16);
            appendTexturedQuad(buffer, pose, toCameraRelative(var20, origin, cameraPosition), toCameraRelative(var21, origin, cameraPosition), toCameraRelative(var22, origin, cameraPosition), toCameraRelative(var23, origin, cameraPosition), color);
         }
      }
   }

   private static void renderGhostType2(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha) {
      Vec3 var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      Vec3 var7 = targetPosition.add(0.0, target.getBbHeight() * ФЭ, 0.0).subtract(var6);
      BufferBuilder buffer = beginQuads();
      PoseStack poses = event.getPoses();
      poses.pushPose();
      poses.translate(var7.x, var7.y, var7.z);
      poses.scale(Ф必, Фщ, ФГ);
      double var10 = (System.currentTimeMillis() - GHOST_ANIMATION_START_MILLIS) / Фл;

      for (int var12 = 0; var12 < 4; var12++) {
         for (int var13 = 0; var13 < 9; var13++) {
            poses.pushPose();
            float var14 = var13 / Фэ;
            float var15 = (Фю * (1.0F - var14) + ФЩ * var14) * Фу;
            double var16 = var10 - var12 * Ф2;
            double var18 = Фф * (var16 - var13 * Фи) / Ф9;
            double var20 = 0.0;
            double var22 = -Math.cos(var18) * Фо;
            double var24 = Math.sin(var18) * ФO;
            switch (var12) {
               case 0:
                  var20 += var13 * Ф7;
                  poses.translate(var22, var20 + ФР, var24);
                  break;
               case 1:
               case 3:
                  var20 -= var13 * Ф3;
                  poses.translate(var22, var20 - Фр, var24);
                  break;
               case 2:
               default:
                  var20 += var13 * Ф6;
                  poses.translate(var22, var20, var24);
            }

            int var26 = multiplyAlpha(getOscillatingColor(primaryColor, secondaryColor, var13 * 25, false), alpha);
            poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
            Matrix4f pose = poses.last().pose();
            appendGhostQuad(buffer, pose, var15, normalizeAdditiveColor(var26));
            if (ghostBloomEnabled) {
               int var28 = multiplyAlpha(brightenColor(var26, Ф_), ФШ);
               appendGhostQuad(buffer, pose, var15 * Фы, normalizeAdditiveColor(var28));
               appendGhostQuad(buffer, pose, var15 * Фй, normalizeAdditiveColor(multiplyAlpha(var28, ФЖ)));
            }

            poses.popPose();
         }
      }

      poses.popPose();
      drawGhostGeometry(buffer, GHOST_TEXTURE);
   }

   private static Vec3 getGhostOrbitOffset(double angle, double axisX, double axisY, double axisZ) {
      double var8 = Math.sin(angle) * йЦ;
      double var10 = Math.cos(angle) * йч;
      if (axisX == 1.0 && axisY == 1.0 && axisZ == йЭ) {
         return new Vec3(var8, var10, -var10);
      } else {
         return axisX == й必 && axisY == 1.0 && axisZ == йщ ? new Vec3(-var8, var8, -var10) : new Vec3(-var8, -var8, var10);
      }
   }

   private static void appendGhostOrbitTrail(
      BufferBuilder buffer,
      Matrix4f pose,
      double x,
      double y,
      double z,
      long nowMillis,
      double radius,
      float sizeScale,
      float hurtPulse,
      int coreColor,
      int glowColor,
      int orbitIndex
   ) {
      double var17 = ыК * (1.0 + ыI * hurtPulse);
      float var19 = ыТ + ыО * hurtPulse;

      for (int var20 = 0; var20 < 20; var20++) {
         float var21 = 1.0F - var20 / ыФ * var19;
         double var22 = nowMillis * ыЙ - var20 * var17 + ghostOrbitalPhase;
         double var24 = Math.sin(var22) * radius;
         double var26 = Math.cos(var22) * radius;
         double var28 = x;
         double var30 = y;
         double var32 = z;
         switch (orbitIndex) {
            case 0:
               var28 += var24;
               var30 += var26;
               var32 -= var26;
               break;
            case 1:
               var28 -= var24;
               var30 += var24;
               var32 -= var26;
               break;
            default:
               var28 -= var24;
               var30 -= var24;
               var32 += var26;
         }

         float var34 = ык * var21 * sizeScale;
         appendCameraFacingBillboard(buffer, pose, var28, var30, var32, var34 * ыг, coreColor);
         appendCameraFacingBillboard(buffer, pose, var28, var30, var32, var34 * ы0, glowColor);
      }
   }

   private static void appendFadingTrail(PoseStack poses, BufferBuilder buffer, Vec3 cameraPosition, float alpha, int color) {
      long var5 = System.currentTimeMillis();
      Iterator var7 = orbitTrail.iterator();

      while (var7.hasNext()) {
         TargetEspMask.TrailSample var8 = (TargetEspMask.TrailSample)var7.next();
         long var9 = var5 - var8.createdMillis;
         if (var9 > йВ) {
            var7.remove();
         } else {
            float var11 = 1.0F - (float)var9 / йЯ;
            if (var11 <= 0.0F) {
               var7.remove();
            } else {
               float var12 = Mth.clamp(alpha * var8.alpha * var11 * йХ, 0.0F, 1.0F);
               float var13 = var8.size * (йе + йА * var11);
               appendGlowBillboard(poses, buffer, var8.position.x - cameraPosition.x, var8.position.y - cameraPosition.y, var8.position.z - cameraPosition.z, var13, var12, color);
            }
         }
      }
   }

   private static boolean shouldDeferStyle(String style) {
      if (DEFER_LIGHTNING_STYLE_LABEL.equals(style)) {
         return lightningThroughWalls;
      } else {
         return DEFER_CRYSTAL_STYLE_LABEL.equals(style) ? crystalThroughWalls : true;
      }
   }

   private static BufferBuilder beginTriangles() {
      return SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR);
   }

   private static void renderRingType1(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      float var5 = effectElapsedSeconds * effectSpeed;
      float var6 = target.getBbWidth() * О9;
      float var7 = target.getBbHeight();
      float var8 = easeOutCubic(alpha);
      int var9 = multiplyRgb(color, Оо);
      short var10 = 144;
      byte var11 = 32;
      Matrix4f pose = event.getPoses().last().pose();
      BufferBuilder buffer = beginQuads();
      double var14 = Math.abs(Math.sin(var5)) * var7;
      double var16 = Math.abs(Math.sin(var5 - ОO)) * var7;

      for (int var18 = 0; var18 < var10; var18++) {
         double var19 = О7 * var18 / var10;
         double var21 = targetPosition.x + Math.cos(var19) * var6;
         double var23 = targetPosition.z + Math.sin(var19) * var6;
         int var25 = oscillateColors(color, var9, var18 * (ОР / var10));
         appendCameraFacingBillboard(buffer, pose, var21, targetPosition.y + var14, var23, О3, multiplyAlpha(var25, var8 * Ор));

         for (int var26 = 1; var26 <= var11; var26++) {
            float var27 = (float)var26 / var11;
            double var28 = var14 + (var16 - var14) * var27;
            float var30 = var8 * (1.0F - var27 * О6) * О_;
            float var31 = Mth.lerp(var27, ОШ, Оы);
            appendCameraFacingBillboard(buffer, pose, var21, targetPosition.y + var28, var23, var31, multiplyAlpha(var25, var30));
         }
      }

      drawTexturedGeometry(buffer, TARGET_GLOW_TEXTURE, true);
   }

   private static void appendTexturedLightningTube(BufferBuilder buffer, Matrix4f pose, List<Vec3> points, float growth, float tubeRadius, int color, int textureBand, float texturePhase) {
      int var8 = points.size() - 1;
      if (var8 > 0 && !(tubeRadius <= Жы) && !(growth <= Жй)) {
         float var9 = growth * var8;
         int var10 = Math.min(var8 - 1, (int)var9);
         Vec3[] var11 = new Vec3[var10 + 2];
         Vec3[] var12 = new Vec3[var10 + 2];
         Vec3[] var13 = new Vec3[var10 + 2];
         Vec3 var14 = null;

         for (int var15 = 0; var15 < var11.length; var15++) {
            Vec3 var16 = (Vec3)points.get(Math.max(0, var15 - 1));
            Vec3 var17 = (Vec3)points.get(Math.min(points.size() - 1, var15 + 1));
            Vec3 var18 = var17.subtract(var16);
            if (var18.lengthSqr() < ЖЖ) {
               var18 = new Vec3(0.0, 1.0, 0.0);
            }

            var18 = var18.normalize();
            Vec3 var19 = Math.abs(var18.y) < Жм ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
            Vec3 var20 = var18.cross(var19);
            if (var20.lengthSqr() < ЖК) {
               var20 = new Vec3(1.0, 0.0, 0.0);
            }

            var20 = var20.normalize();
            if (var14 != null && var20.dot(var14) < 0.0) {
               var20 = var20.scale(ЖI);
            }

            if (var14 != null) {
               var20 = var14.add(var20).normalize();
               if (var20.lengthSqr() < ЖТ) {
                  var20 = var14;
               }
            }

            Vec3 var21 = var18.cross(var20).normalize();
            var11[var15] = var18;
            var12[var15] = var20;
            var13[var15] = var21;
            var14 = var20;
         }

         float var45 = textureBand / ЖО;
         float var46 = var45 + ЖФ;
         byte var47 = 6;
         float var49 = tubeRadius;

         for (int var50 = 0; var50 <= var10; var50++) {
            float var52 = Mth.clamp(var9 - var50, 0.0F, 1.0F);
            if (var52 <= ЖЙ) {
               break;
            }

            float var53 = var49 * getTrailWidthFactor(var50, points.size());
            float var22 = var49 * getTrailWidthFactor(var50 + 1, points.size());
            if (var52 < 1.0F) {
               var22 = Mth.lerp(var52, var53, var22);
            }

            Vec3 var23 = (Vec3)points.get(var50);
            Vec3 var24 = (Vec3)points.get(var50 + 1);
            if (var52 < 1.0F) {
               var24 = var23.lerp(var24, var52);
            }

            Vec3 var25 = var12[var50];
            Vec3 var26 = var13[var50];
            Vec3 var27 = var50 + 1 < var12.length ? var12[var50 + 1] : var12[var50];
            Vec3 var28 = var50 + 1 < var13.length ? var13[var50 + 1] : var13[var50];
            if (var52 < 1.0F) {
               var27 = var25.lerp(var27, var52).normalize();
               var28 = var26.lerp(var28, var52).normalize();
            }

            float var29 = (float)var50 / var8 * Жк + texturePhase;
            float var30 = (var50 + var52) / var8 * Жг + texturePhase;
            int var31 = var52 < 1.0F ? multiplyAlpha(color, var52) : color;

            for (int var32 = 0; var32 < 6; var32++) {
               float var33 = (float)(Ж0 * var32 / Жх);
               float var34 = (float)(ЖУ * (var32 + 1) / ЖЗ);
               float var35 = (float)Math.cos(var33);
               float var36 = (float)Math.sin(var33);
               float var37 = (float)Math.cos(var34);
               float var38 = (float)Math.sin(var34);
               Vec3 var39 = var23.add(var25.scale(var35 * var53)).add(var26.scale(var36 * var53));
               Vec3 var40 = var23.add(var25.scale(var37 * var53)).add(var26.scale(var38 * var53));
               Vec3 var41 = var24.add(var27.scale(var37 * var22)).add(var28.scale(var38 * var22));
               Vec3 var42 = var24.add(var27.scale(var35 * var22)).add(var28.scale(var36 * var22));
               float var43 = Mth.lerp(var32 / ЖЕ, var45, var46);
               float var44 = Mth.lerp((var32 + 1) / ЖП, var45, var46);
               appendTexturedVertex(buffer, pose, (float)var39.x, (float)var39.y, (float)var39.z, var29, var43, color);
               appendTexturedVertex(buffer, pose, (float)var40.x, (float)var40.y, (float)var40.z, var29, var44, color);
               appendTexturedVertex(buffer, pose, (float)var41.x, (float)var41.y, (float)var41.z, var30, var44, var31);
               appendTexturedVertex(buffer, pose, (float)var42.x, (float)var42.y, (float)var42.z, var30, var43, var31);
            }
         }
      }
   }

   private static void appendTriangleQuad(
      BufferBuilder buffer, Matrix4f pose, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, int color
   ) {
      Vector4f var12 = ColorMath.toRgbaVector(color);
      appendTexturedVertex(buffer, pose, x1, y1, z1, гА, г4, var12);
      appendTexturedVertex(buffer, pose, x2, y2, z2, гп, гД, var12);
      appendTexturedVertex(buffer, pose, x3, y3, z3, гЪ, гт, var12);
      appendTexturedVertex(buffer, pose, x3, y3, z3, га, гЮ, var12);
   }

   private static int getAnimatedEffectColor(int primaryColor, int secondaryColor, int phase, float hurtProgress, float brightness) {
      int var5 = ThemeEditor.isRainbowEnabled() ? ColorMath.getAnimatedThemeColor(phase) : getOscillatingColor(primaryColor, secondaryColor, phase, false);
      int var6 = brightenColor(var5, 1.0F - brightness);
      if (hurtProgress > 0.0F) {
         int var7 = ColorMath.saturatedPingPongGradient(ОП, -29556, phase, 4);
         var6 = ColorMath.lerpArgb(var6, var7, hurtProgress);
      }

      return var6;
   }

   private static void renderGhostType8(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, float deltaSeconds, boolean active) {
      if (!(alpha <= ыO)) {
         if (ghostPulseEntityId != target.getId()) {
            ghostPulseEntityId = target.getId();
            ghostHurtCycleProgress = 1.0F;
            ghostHurtPulse = 0.0F;
            ghostHurtLatched = false;
         }

         boolean hurtActive = active && target.hurtTime > 7;
         if (hurtActive && !ghostHurtLatched) {
            ghostHurtCycleProgress = 0.0F;
            ghostHurtLatched = true;
         } else if (!hurtActive) {
            ghostHurtLatched = false;
         }

         if (ghostHurtCycleProgress < 1.0F) {
            ghostHurtCycleProgress = Math.min(1.0F, ghostHurtCycleProgress + deltaSeconds / 1.0F);
         }

         ghostHurtPulse = moveToward(ghostHurtPulse, pulseEnvelope(ghostHurtCycleProgress), deltaSeconds / ы7);
         float hurtPulse = ghostHurtPulse;
         ghostOrbitalPhase = ghostOrbitalPhase + deltaSeconds * hurtPulse * ыР;
         Vec3 cameraRelativePosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
         double x = cameraRelativePosition.x;
         double y = cameraRelativePosition.y + target.getBbHeight() * ы3;
         double z = cameraRelativePosition.z;
         long nowMillis = System.currentTimeMillis();
         int effectColor = ColorMath.lerpArgb(color, -1, hurtPulse * ыр);
         int coreColor = multiplyAlpha(effectColor, Math.min(1.0F, alpha * (1.0F + hurtPulse)));
         int glowColor = multiplyAlpha(effectColor, alpha * ы6);
         double radius = ы_ * (1.0 + ыШ * hurtPulse);
         float sizeScale = 1.0F * (1.0F + ыы * hurtPulse);
         Matrix4f pose = event.getPoses().last().pose();
         BufferBuilder buffer = beginQuads();

         for (int var26 = 0; var26 < 3; var26++) {
            appendGhostOrbitTrail(buffer, pose, x, y, z, nowMillis, radius, sizeScale, hurtPulse, coreColor, glowColor, var26);
         }

         if (hurtPulse > ый) {
            appendParticleHalo(buffer, pose, x, y, z, effectColor, alpha * hurtPulse);
            appendCameraFacingBillboard(buffer, pose, x, y, z, ыЖ * hurtPulse, multiplyAlpha(effectColor, alpha * hurtPulse * ым));
         }

         drawTexturedGeometry(buffer, TARGET_GLOW_TEXTURE, true);
      }
   }

   private static void renderAnimatedArcs(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis, boolean active) {
      animatedArcs.removeIf(expiredArc -> nowMillis - expiredArc.createdMillis > expiredArc.durationMillis);
      animatedArcs.removeIf(undersizedArc -> undersizedArc.angularSpan < 1.0F);
      if (active) {
         while (animatedArcs.size() < 2) {
            animatedArcs.add(createAnimatedArc(nowMillis - EFFECT_RANDOM.nextInt(120)));
         }

         if (nowMillis - lastArcSpawnMillis >= йэ && animatedArcs.size() < 3) {
            animatedArcs.add(createAnimatedArc(nowMillis));
            lastArcSpawnMillis = nowMillis;
         }
      }

      if (!animatedArcs.isEmpty() && !(alpha <= йю)) {
         Vec3 center = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
         float height = Math.max(target.getBbHeight(), йЩ);
         float radius = Math.max(target.getBbWidth() * йу, й2);
         float arcRadius = radius + йф;
         Matrix4f pose = event.getPoses().last().pose();
         PoseStack poses = event.getPoses();
         int glowColor = brightenColor(color, йи);
         int coreColor = brightenColor(color, й9);
         ArrayList arcPaths = new ArrayList(animatedArcs.size());
         float[] arcAlphas = new float[animatedArcs.size()];

         for (int var18 = 0; var18 < animatedArcs.size(); var18++) {
            TargetEspMask.AnimatedArc arc = animatedArcs.get(var18);
            float arcAlpha = getAnimatedArcAlpha(arc, nowMillis, alpha);
            arcAlphas[var18] = arcAlpha;
            ArrayList points = new ArrayList(28);
            if (arcAlpha > йо) {
               int sampleIndex = (int)((nowMillis - arc.createdMillis) / йO);
               buildAnimatedArcPath(arc, center, height, arcRadius, sampleIndex, points);
            }

            arcPaths.add(points);
         }

         BufferBuilder haloBuffer = beginQuads();

         for (int var27 = 0; var27 < animatedArcs.size(); var27++) {
            float var29 = arcAlphas[var27];
            List var32 = (List)arcPaths.get(var27);
            if (!(var29 <= й7) && var32.size() >= 2) {
               TargetEspMask.AnimatedArc var35 = animatedArcs.get(var27);

               for (int var23 = 0; var23 < var32.size(); var23++) {
                  Vec3 var24 = (Vec3)var32.get(var23);
                  float var25 = getTrailWidthFactor(var23, var32.size()) * var35.widthScale;
                  appendCameraFacingBillboard(haloBuffer, poses, var24.x, var24.y, var24.z, йР * var25, multiplyAlpha(color, var29 * й3));
                  appendCameraFacingBillboard(haloBuffer, poses, var24.x, var24.y, var24.z, йр * var25, multiplyAlpha(glowColor, var29 * й6));
               }
            }
         }

         drawTexturedGeometry(haloBuffer, WORLD_BLOOM_TEXTURE, false, LIGHTNING_ARC_PIPELINE);
         BufferBuilder glowRibbonBuffer = beginQuads();

         for (int var30 = 0; var30 < animatedArcs.size(); var30++) {
            float var33 = arcAlphas[var30];
            List var36 = (List)arcPaths.get(var30);
            if (!(var33 <= й_) && var36.size() >= 2) {
               float var38 = йШ * animatedArcs.get(var30).widthScale;
               appendCameraFacingRibbon(glowRibbonBuffer, pose, var36, var38 * йы, multiplyAlpha(color, var33 * йй));
               appendCameraFacingRibbon(glowRibbonBuffer, pose, var36, var38 * йЖ, multiplyAlpha(glowColor, var33 * йм));
            }
         }

         drawTexturedGeometry(glowRibbonBuffer, TARGET_GLOW_TEXTURE, false, LIGHTNING_ARC_PIPELINE);
         BufferBuilder coreRibbonBuffer = beginQuads();

         for (int var34 = 0; var34 < animatedArcs.size(); var34++) {
            float var37 = arcAlphas[var34];
            List var39 = (List)arcPaths.get(var34);
            if (!(var37 <= йК) && var39.size() >= 2) {
               float var40 = йI * animatedArcs.get(var34).widthScale;
               appendCameraFacingRibbon(coreRibbonBuffer, pose, var39, var40, multiplyAlpha(coreColor, var37));
               appendCameraFacingRibbon(coreRibbonBuffer, pose, var39, var40 * йТ, multiplyAlpha(-1, var37));
            }
         }

         drawTexturedGeometry(coreRibbonBuffer, TARGET_GLOW_TEXTURE, false, LIGHTNING_ARC_PIPELINE);
      }
   }

   private static void renderRhombs(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha, long nowMillis) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      initializeCrystalType1Offsets(target);
      byte var8 = 12;
      byte var9 = 20;
      int var10 = var8 + var9;
      float var11 = (float)(nowMillis % _е) / _А;
      float var12 = (float)(nowMillis % _4) / _п;
      float var13 = (float)nowMillis * _Д;
      float var14 = target.getBbWidth() * _Ъ;
      float var15 = _т * (_а + _Ю * alpha);
      float var16 = target.getBbHeight();
      float var17 = _Ч - _Л * alpha;
      PoseStack poses = event.getPoses();
      BufferBuilder var19 = beginQuads();

      for (int var20 = 0; var20 < var10; var20++) {
         TargetEspMask.OrbitParticleSample var21 = sampleOrbitParticle(var20, var8, var9, var11, var12, var13, var14, var16, var15, var17);
         float var22 = _诶 + var21.size() * _1;
         float var23 = _Ь + var21.size() * _衣;
         appendCameraFacingBillboard(var19, poses, targetPosition.x + var21.x(), targetPosition.y + var21.y(), targetPosition.z + var21.z(), var22, multiplyAlpha(primaryColor, alpha * _ш));
         appendCameraFacingBillboard(var19, poses, targetPosition.x + var21.x(), targetPosition.y + var21.y(), targetPosition.z + var21.z(), var23, multiplyAlpha(brightenColor(primaryColor, _И), alpha * _ь));
      }

      drawTexturedGeometry(var19, WORLD_BLOOM_TEXTURE, true);
      int var26 = ColorMath.packArgb(ColorMath.getRed(primaryColor), ColorMath.getGreen(primaryColor), ColorMath.getBlue(primaryColor), Math.round(_ж * alpha));
      int var27 = ColorMath.packArgb(ColorMath.getRed(primaryColor), ColorMath.getGreen(primaryColor), ColorMath.getBlue(primaryColor), Math.round(_弟 * alpha));
      BufferBuilder var28 = beginQuads();
      poses.pushPose();
      poses.translate(targetPosition.x, targetPosition.y, targetPosition.z);

      for (int var29 = 0; var29 < var10; var29++) {
         TargetEspMask.OrbitParticleSample var24 = sampleOrbitParticle(var29, var8, var9, var11, var12, var13, var14, var16, var15, var17);
         appendFacingRhomb(var28, poses, var24.x(), var24.y(), var24.z(), var24.size() * _б, var27, secondaryColor, var24.spin(), var29, var16, true);
         appendFacingRhomb(var28, poses, var24.x(), var24.y(), var24.z(), var24.size(), var26, secondaryColor, var24.spin(), var29, var16, false);
      }

      poses.popPose();
      drawColorGeometry(var28, CRYSTAL_COLOR_PIPELINE);
   }

   private static void appendGhostQuad(BufferBuilder buffer, Matrix4f pose, float halfSize, int color) {
      TargetLighting.addLight(pose, 0.0F, 0.0F, 0.0F, halfSize * 2.0F, color);
      Vector4f var4 = ColorMath.toRgbaVector(color);
      appendTexturedVertex(buffer, pose, -halfSize, -halfSize, 0.0F, 1.0F, 1.0F, var4);
      appendTexturedVertex(buffer, pose, halfSize, -halfSize, 0.0F, 0.0F, 1.0F, var4);
      appendTexturedVertex(buffer, pose, halfSize, halfSize, 0.0F, 0.0F, 0.0F, var4);
      appendTexturedVertex(buffer, pose, -halfSize, halfSize, 0.0F, 1.0F, 0.0F, var4);
   }

   private static void appendAnimatedTriangleTiles(PoseStack poses, int color, float alpha, long nowMillis) {
      byte var5 = 5;
      float[] var6 = new float[]{ТВ, ТЯ};
      float[] var7 = new float[]{ТХ, Те};
      float[] var8 = new float[]{0.0F, ТА};
      float[][][] var9 = new float[6][][];

      for (int var10 = 0; var10 <= 5; var10++) {
         float var11 = var10 / Т4;
         float var12 = var6[0] + (var8[0] - var6[0]) * var11;
         float var13 = var6[1] + (var8[1] - var6[1]) * var11;
         float var14 = var7[0] + (var8[0] - var7[0]) * var11;
         float var15 = var7[1] + (var8[1] - var7[1]) * var11;
         int var16 = 5 - var10;
         var9[var10] = new float[var16 + 1][2];

         for (int var17 = 0; var17 <= var16; var17++) {
            float var18 = var16 == 0 ? 0.0F : (float)var17 / var16;
            var9[var10][var17][0] = var12 + (var14 - var12) * var18;
            var9[var10][var17][1] = var13 + (var15 - var13) * var18;
         }
      }

      ArrayList var25 = new ArrayList();

      for (int var26 = 0; var26 < 5; var26++) {
         int var28 = 5 - var26;

         for (int var30 = 0; var30 < var28; var30++) {
            var25.add(new float[][]{var9[var26][var30], var9[var26][var30 + 1], var9[var26 + 1][var30]});
            if (var30 < var28 - 1) {
               var25.add(new float[][]{var9[var26][var30 + 1], var9[var26 + 1][var30 + 1], var9[var26 + 1][var30]});
            }
         }
      }

      Matrix4f var27 = poses.last().pose();
      int var29 = ColorMath.blendArgb(color, -1, Тп);
      BufferBuilder var31 = beginQuads();

      for (int var32 = 0; var32 < var25.size(); var32++) {
         float[][] var34 = (float[][])var25.get(var32);
         float var37 = (var34[0][0] + var34[1][0] + var34[2][0]) / ТД;
         float var40 = (var34[0][1] + var34[1][1] + var34[2][1]) / ТЪ;
         float[] var43 = getTriangleTileAnimation(var37, nowMillis);
         float var19 = var43[0];
         float var20 = var43[1];
         float var21 = var43[2];
         float var22 = var43[3];
         Matrix4f var23 = rotateTriangleTile(var27, var37, var40, var19, var20);
         int var24 = getTriangleTileColor(var40, var32, color);
         if (var21 > 0.0F) {
            var24 = ColorMath.blendArgb(var24, -1, var21 * Тт);
         }

         if (var22 > 0.0F) {
            var24 = ColorMath.blendArgb(var24, var29, Mth.clamp(var22 * Та, 0.0F, 1.0F));
         }

         appendTriangleQuad(var31, var23, var34[0][0], var34[0][1], 0.0F, var34[1][0], var34[1][1], 0.0F, var34[2][0], var34[2][1], 0.0F, multiplyAlpha(var24, alpha));
      }

      drawColorGeometry(var31, CUBE_PIPELINE, true);
      ArrayList<Integer> var33 = new ArrayList<>();

      for (int var35 = 0; var35 < var25.size(); var35++) {
         float[][] var38 = (float[][])var25.get(var35);
         float var41 = (var38[0][0] + var38[1][0] + var38[2][0]) / ТЮ;
         if (getTriangleTileAnimation(var41, nowMillis)[2] > ТЧ) {
            var33.add(var35);
         }
      }

      if (!var33.isEmpty()) {
         BufferBuilder var36 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

         for (int var42 : var33) {
            float[][] var44 = (float[][])var25.get(var42);
            float var45 = (var44[0][0] + var44[1][0] + var44[2][0]) / ТЛ;
            float var46 = (var44[0][1] + var44[1][1] + var44[2][1]) / Т诶;
            float[] var47 = getTriangleTileAnimation(var45, nowMillis);
            Matrix4f var48 = rotateTriangleTile(var27, var45, var46, var47[0], var47[1]);
            int var49 = multiplyAlpha(ColorMath.blendArgb(color, -1, Т1), (ТЬ + Т衣 * var47[2]) / Тш * alpha);
            Vector4f var50 = ColorMath.toRgbaVector(var49);
            appendLine(var36, var48, var44[0][0], var44[0][1], 0.0F, var44[1][0], var44[1][1], 0.0F, var50);
            appendLine(var36, var48, var44[1][0], var44[1][1], 0.0F, var44[2][0], var44[2][1], 0.0F, var50);
            appendLine(var36, var48, var44[2][0], var44[2][1], 0.0F, var44[0][0], var44[0][1], 0.0F, var50);
         }

         drawTriangleLines(var36, TRIANGLE_LINES_PIPELINE);
      }
   }

   private static void renderGhostType5(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha, float tickDelta) {
      Vec3 var7 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      float var8 = target.getBbHeight();
      long var9 = ФА;
      double var11 = System.currentTimeMillis() % var9;
      boolean var13 = var11 > var9 / Ф4;
      float var14 = (float)(var11 / (var9 / Фп));
      if (var13) {
         var14--;
      } else {
         var14 = 1.0F - var14;
      }

      var14 = var14 < ФД ? 2.0F * var14 * var14 : (float)(1.0 - Math.pow(ФЪ * var14 + Фт, Фа) / ФЮ);
      double var15 = var8 / ФЧ * (var14 > ФЛ ? 1.0 - var14 : var14) * (var13 ? -1 : 1);
      long var17 = System.nanoTime();
      float var19 = (float)(var17 - lastGhostType5UpdateNanos) / Ф诶;
      lastGhostType5UpdateNanos = var17;
      float var20 = target.hurtTime <= 0 ? 0.0F : (target.hurtTime - tickDelta) / Ф1;
      ghostType5HurtSpeedBoost = ghostType5HurtSpeedBoost + Math.max(0.0F, var20) * var19;
      float var21 = (float)(System.currentTimeMillis() - GHOST_ANIMATION_START_MILLIS) / ФЬ + ghostType5HurtSpeedBoost;
      BufferBuilder buffer = beginQuads();
      PoseStack poses = event.getPoses();
      poses.pushPose();
      poses.translate(targetPosition.x - var7.x, targetPosition.y - var7.y + var8 * var14 + var15, targetPosition.z - var7.z);
      float var24 = Ф衣;

      for (int var25 = 0; var25 < 4; var25++) {
         for (int var26 = 0; var26 < 15; var26++) {
            poses.pushPose();
            float var27 = var26 / Фш;
            float var28 = (ФИ * (1.0F - var27) + Фь * var27) * alpha * Фж;
            float var29 = Ф弟 * (var21 - var26 * Фб) / ФЫ;
            boolean var30 = var27 < Фl;
            float var31 = var30 ? var27 * 2.0F : (1.0F - var27) * 2.0F;
            double var32 = Math.sin(var31 * Ф8) * Ф5;
            Random var34 = new Random(var26 * Фв);
            double var35 = (var34.nextDouble() - Фъ) * var32;
            double var37 = (var34.nextDouble() - ФМ) * var32;
            double var39 = (var34.nextDouble() - ФН) * var32;
            var35 = var35 * alpha - var35;
            var37 = var37 * alpha - var37;
            var39 = var39 * alpha - var39;
            switch (var25) {
               case 0:
                  poses.translate(Math.cos(var29) * var24 + var35, var37, Math.sin(var29) * var24 + var39);
                  break;
               case 1:
                  poses.translate(-Math.sin(var29) * var24 + var35, var37, Math.cos(var29) * var24 + var39);
                  break;
               case 2:
                  poses.translate(-Math.cos(var29) * var24 + var35, var37, -Math.sin(var29) * var24 + var39);
                  break;
               default:
                  poses.translate(Math.sin(var29) * var24 + var35, var37, -Math.cos(var29) * var24 + var39);
            }

            int var41 = brightenColor(oscillateColors(primaryColor, secondaryColor, var26 * Фн), ФЦ);
            int var42 = multiplyAlpha(var41, alpha * Фч);
            poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
            appendGhostQuad(buffer, poses.last().pose(), var28, normalizeAdditiveColor(var42));
            poses.popPose();
         }
      }

      poses.popPose();
      drawGhostGeometry(buffer, GHOST_TEXTURE);
   }

   private static void drawColorGeometry(BufferBuilder buffer, RenderPipeline pipeline, boolean submitDistortion) {
      MeshData mesh = buffer.build();
      if (mesh != null) {
         GpuBuffer vertexBuffer = null;

         try {
            DrawState drawState = mesh.drawState();
            int indexCount = drawState.indexCount();
            AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
            GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
            IndexType indexType = sequentialIndices.type();
            vertexBuffer = RenderSystem.getDevice().createBuffer(() -> COLOR_VERTEX_BUFFER_DEBUG_LABEL, 32, mesh.vertexBuffer());
            if (submitDistortion && distortionEnabled) {
               GlowDistortionRenderer.INSTANCE.renderMaskAndDistort(vertexBuffer, indexBuffer, indexType, indexCount, distortionStrength);
            }

            RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass pass = encoder.createRenderPass(
                  () -> TARGET_ESP_COLOR_PASS_DEBUG_LABEL, mainTarget.getColorTextureView(), Optional.empty(), mainTarget.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(pass);
               pass.setPipeline(pipeline);
               pass.setVertexBuffer(0, vertexBuffer.slice());
               pass.setIndexBuffer(indexBuffer, indexType);
               pass.drawIndexed(indexCount, 1, 0, 0, 0);
            }
         } finally {
            if (vertexBuffer != null) {
               vertexBuffer.close();
            }

            mesh.close();
         }
      }
   }

   private static void appendCenteredBillboard(BufferBuilder buffer, Matrix4f pose, float size, int color) {
      TargetLighting.addLight(pose, 0.0F, 0.0F, 0.0F, size, color);
      Vector4f var4 = ColorMath.toRgbaVector(color);
      float var5 = size * Йя;
      appendTexturedVertex(buffer, pose, -var5, -var5, 0.0F, 0.0F, 1.0F, var4);
      appendTexturedVertex(buffer, pose, -var5, var5, 0.0F, 0.0F, 0.0F, var4);
      appendTexturedVertex(buffer, pose, var5, var5, 0.0F, 1.0F, 0.0F, var4);
      appendTexturedVertex(buffer, pose, var5, -var5, 0.0F, 1.0F, 1.0F, var4);
   }

   private static TargetEspMask.RotatingPolyline createRotatingPolyline(LivingEntity target, Vec3 origin, long nowMillis, float phase) {
      double height = Math.max(target.getBbHeight(), мъ);
      double radius = Math.max(target.getBbWidth() * мМ, мН) + мн + EFFECT_RANDOM.nextDouble() * мЦ;
      int direction = EFFECT_RANDOM.nextBoolean() ? 1 : -1;
      float turnCount = мч + EFFECT_RANDOM.nextFloat() * мЭ;
      float initialAngleRadians = phase * (м必 + EFFECT_RANDOM.nextFloat() * мщ) + EFFECT_RANDOM.nextFloat() * мГ * 2.0F;
      float startProgress = EFFECT_RANDOM.nextFloat() * мл;
      float endProgress = Math.min(1.0F, startProgress + мэ + EFFECT_RANDOM.nextFloat() * мю);
      Vec3 start = getLightningBranchPoint(origin, height, radius, initialAngleRadians, turnCount, direction, startProgress);
      Vec3 end = getLightningBranchPoint(origin, height, radius, initialAngleRadians, turnCount, direction, endProgress);
      double pathLength = start.distanceTo(end);
      List<Vec3> worldPoints = createJitteredPath(start, end, 3, Math.max(мЩ, pathLength * му));
      ArrayList localPoints = new ArrayList(worldPoints.size());

      for (Vec3 worldPoint : worldPoints) {
         localPoints.add(worldPoint.subtract(origin));
      }

      float angularSpeed = м2 + EFFECT_RANDOM.nextFloat() * мф;
      return new TargetEspMask.RotatingPolyline(
         localPoints, nowMillis, System.nanoTime(), 520 + EFFECT_RANDOM.nextInt(260), EFFECT_RANDOM.nextFloat() * ми, м9 + EFFECT_RANDOM.nextFloat() * мо, direction, angularSpeed
      );
   }

   public static void flushDeferredRender() {
      deferredRenderActive = false;
      if (deferredEvent != null && deferredStyle != null) {
         GpuBufferSlice projectionBuffer = RenderSystem.getProjectionMatrixBuffer();
         ProjectionType projectionType = RenderSystem.getProjectionType();
         WorldEffectsEvent event = deferredEvent;
         LivingEntity target = deferredTarget;
         String style = deferredStyle;
         int primaryColor = deferredPrimaryColor;
         int secondaryColor = deferredSecondaryColor;
         deferredEvent = null;
         deferredTarget = null;
         deferredStyle = null;

         try {
            RenderSystem.setProjectionMatrix(deferredProjectionBuffer, deferredProjectionType);
            render(event, target, style, primaryColor, secondaryColor);
            if (cubeCompositePending) {
               compositeCubeMask();
            }
         } finally {
            RenderSystem.setProjectionMatrix(projectionBuffer, projectionType);
         }
      }
   }

   private static float hashArcUnitFloat(int index, float seed) {
      float var2 = (float)Math.sin(index * IН + seed * Iн);
      var2 *= IЦ;
      return var2 - (float)Math.floor(var2);
   }

   private static TargetEspMask.OrbitParticleSample sampleOrbitParticle(
      int index, int innerCount, int outerCount, float innerAngleDegrees, float outerAngleDegrees, float timePhase, float radius, float height, float sizeScale, float radialScale
   ) {
      if (index >= innerCount) {
         int var25 = index - innerCount;
         int var26 = var25 * crystalType1Offsets.size() / outerCount;
         Vector3f var27 = crystalType1Offsets.get(var26);
         float var28 = (float)Math.toRadians(outerAngleDegrees);
         float var29 = Mth.sin(var28);
         float var30 = Mth.cos(var28);
         float var31 = var27.x * var30 - var27.z * var29;
         float var33 = var27.x * var29 + var27.z * var30;
         float var35 = Mth.sqrt(var31 * var31 + var33 * var33);
         float var36 = radius * _Ы;
         float var20 = Math.max(var35 * _l, var36);
         if (var35 > _8) {
            float var21 = var20 / var35;
            var31 *= var21;
            var33 *= var21;
         }

         var31 *= radialScale;
         var33 *= radialScale;
         float var37 = var27.y + (float)Math.sin(timePhase * _5 + var25 * _в) * _ъ;
         float var22 = hashRhombUnitFloat(var25, _М);
         float var23 = sizeScale * (_Н + var22 * _н);
         float var24 = outerAngleDegrees * (var25 % 2 == 0 ? _Ц : _ч) + var25 * _Э;
         return new TargetEspMask.OrbitParticleSample(var31, var37, var33, var23, var24);
      } else {
         float var10 = _必 * index / innerCount;
         float var11 = (float)Math.toRadians(var10 + innerAngleDegrees);
         float var12 = _щ + Math.abs((float)Math.sin(var11 * 2.0F)) * _Г;
         float var13 = radius * var12 * radialScale;
         float var14 = (float)Math.sin(var11) * var13;
         float var15 = (float)Math.cos(var11) * var13;
         float var16 = _л + Math.abs((float)Math.sin(var11 * _э + innerAngleDegrees * _ю));
         float var17 = height * (_Щ + _у * var16);
         float var18 = sizeScale * (_2 + Math.abs((float)Math.sin(var11 * _ф)) * _и);
         float var19 = innerAngleDegrees * _9 + var10 * _о;
         return new TargetEspMask.OrbitParticleSample(var14, var17, var15, var18, var19);
      }
   }

   private static Vec3[] createPerpendicularBasis(Vec3 direction) {
      Vec3 var1 = Math.abs(direction.y) > IФ ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
      Vec3 var2 = var1.cross(direction).normalize();
      Vec3 var3 = direction.cross(var2).normalize();
      return new Vec3[]{var2, var3};
   }

   private static TargetEspMask.LightningEffect createLightningEffect(LivingEntity target, Vec3 origin, long nowMillis) {
      double height = Math.max(target.getBbHeight(), КЧ);
      double radius = Math.max(target.getBbWidth() * КЛ, К诶);
      double endAngleRadians = EFFECT_RANDOM.nextDouble() * К1 * КЬ;
      double endRadius = radius * (К衣 + EFFECT_RANDOM.nextDouble() * Кш);
      double endHeight = height * (КИ + EFFECT_RANDOM.nextDouble() * Кь);
      Vec3 endPoint = origin.add(Math.cos(endAngleRadians) * endRadius, endHeight, Math.sin(endAngleRadians) * endRadius);
      double startAngleRadians = endAngleRadians + (EFFECT_RANDOM.nextDouble() - Кж) * К弟;
      double startRadius = radius + Кб + EFFECT_RANDOM.nextDouble() * КЫ;
      double startHeight = height + Кl + EFFECT_RANDOM.nextDouble() * К8;
      Vec3 startPoint = origin.add(Math.cos(startAngleRadians) * startRadius, startHeight, Math.sin(startAngleRadians) * startRadius);
      ArrayList branches = new ArrayList();
      List mainPath = createLightningPath(startPoint, endPoint, 4, К5 + EFFECT_RANDOM.nextDouble() * Кв);
      branches.add(new TargetEspMask.LightningBranch(mainPath, 1.0F, 0.0F));
      int sideBranchCount = 1 + EFFECT_RANDOM.nextInt(3);

      for (int var25 = 0; var25 < sideBranchCount; var25++) {
         int attachmentIndex = 3 + EFFECT_RANDOM.nextInt(Math.max(1, mainPath.size() - 6));
         Vec3 attachmentPoint = (Vec3)mainPath.get(attachmentIndex);
         Vec3 mainDirection = endPoint.subtract(startPoint);
         if (mainDirection.lengthSqr() < Къ) {
            mainDirection = new Vec3(0.0, КМ, 0.0);
         }

         mainDirection = mainDirection.normalize();
         Vec3 lateralDirection = mainDirection.cross(new Vec3(0.0, 1.0, 0.0));
         if (lateralDirection.lengthSqr() < КН) {
            lateralDirection = new Vec3(1.0, 0.0, 0.0);
         }

         lateralDirection = lateralDirection.normalize();
         Vec3 branchDirection = lateralDirection.scale(EFFECT_RANDOM.nextBoolean() ? 1.0 : Кн).add(0.0, EFFECT_RANDOM.nextDouble() * КЦ - Кч, 0.0).normalize();
         double branchLength = КЭ + EFFECT_RANDOM.nextDouble() * 1.0;
         Vec3 branchEnd = attachmentPoint.add(branchDirection.scale(branchLength)).add(mainDirection.scale(К必 + EFFECT_RANDOM.nextDouble() * Кщ));
         float growthDelay = Mth.clamp((float)attachmentIndex / (mainPath.size() - 1) * КГ, Кл, Кэ);
         branches.add(new TargetEspMask.LightningBranch(createLightningPath(attachmentPoint, branchEnd, 3, Кю + EFFECT_RANDOM.nextDouble() * КЩ), Ку + EFFECT_RANDOM.nextFloat() * К2, growthDelay));
      }

      if (EFFECT_RANDOM.nextFloat() < Кф) {
         double extraBranchAngleRadians = endAngleRadians + Ки * (К9 + EFFECT_RANDOM.nextDouble() * Ко) * (EFFECT_RANDOM.nextBoolean() ? 1 : -1);
         Vec3 extraBranchEnd = origin.add(Math.cos(extraBranchAngleRadians) * radius, height * (КO + EFFECT_RANDOM.nextDouble() * К7), Math.sin(extraBranchAngleRadians) * radius);
         branches.add(new TargetEspMask.LightningBranch(createLightningPath(endPoint, extraBranchEnd, 3, КР + EFFECT_RANDOM.nextDouble() * К3), Кр, К6));
      }

      return new TargetEspMask.LightningEffect(branches, endPoint, origin, nowMillis, 560 + EFFECT_RANDOM.nextInt(340), EFFECT_RANDOM.nextFloat() * К_, -1, 0.0, КШ, Кы, Кй);
   }

   private static void subdivideLightning(Vec3[] points, int startIndex, int endIndex, double amplitude, Random random) {
      if (endIndex - startIndex > 1) {
         int var6 = startIndex + endIndex >>> 1;
         Vec3 var7 = points[startIndex];
         Vec3 var8 = points[endIndex];
         Vec3 var9 = var7.add(var8).scale(КЖ);
         Vec3 var10 = var8.subtract(var7);
         if (var10.lengthSqr() < Км) {
            points[var6] = var9;
         } else {
            Vec3 var11 = var10.cross(new Vec3(0.0, 1.0, 0.0));
            if (var11.lengthSqr() < КК) {
               var11 = var10.cross(new Vec3(1.0, 0.0, 0.0));
            }

            if (var11.lengthSqr() < КI) {
               var11 = new Vec3(1.0, 0.0, 0.0);
            }

            var11 = var11.normalize();
            Vec3 var12 = var10.cross(var11);
            if (var12.lengthSqr() < КТ) {
               var12 = new Vec3(0.0, 1.0, 0.0);
            } else {
               var12 = var12.normalize();
            }

            double var13 = (random.nextDouble() - КО) * КФ * amplitude;
            double var15 = (random.nextDouble() - КЙ) * amplitude * Кк;
            points[var6] = var9.add(var11.scale(var13)).add(var12.scale(var15));
            subdivideLightning(points, startIndex, var6, amplitude * Кг, random);
            subdivideLightning(points, var6, endIndex, amplitude * К0, random);
         }
      }
   }

   private static void appendCornerEdges(
      BufferBuilder buffer, Matrix4f pose, Vec3 origin, Vec3 cameraPosition, Vec3 corner, Vec3 edgeX, Vec3 edgeY, Vec3 edgeZ, double edgeLength, double nodeRadius, int color
   ) {
      appendSphere(buffer, pose, origin, cameraPosition, corner, nodeRadius, 3, 6, color);
      Vec3[] var13 = new Vec3[]{edgeX, edgeY, edgeZ};

      for (Vec3 var17 : var13) {
         Vec3 var18 = corner.add(var17.scale(edgeLength));
         appendCylinder(buffer, pose, origin, cameraPosition, corner, var18, nodeRadius, 6, color);
         appendSphere(buffer, pose, origin, cameraPosition, var18, nodeRadius, 3, 6, color);
      }
   }

   private static void renderGhostType4(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, boolean active) {
      long var6 = System.currentTimeMillis();
      if (active && var6 - lastOrbitParticleEmissionMillis >= Фм) {
         lastOrbitParticleEmissionMillis = var6;
         orbitEmissionAngle = orbitEmissionAngle + ФК;
         double var8 = orbitEmissionAngle % ФI;
         float var10 = ФТ + target.getBbWidth() * ФО;
         float var11 = target.getBbHeight() / 2.0F;

         for (int var12 = 0; var12 < 4; var12++) {
            float var13 = (var12 - 1) * ФФ;
            double var14 = var8 + var13 * ФЙ;
            double var16 = Math.toRadians(-var14);
            double var18 = Math.sin(var16 + Фк) * var13;
            double var20 = Math.cos(var16) * var10;
            double var22 = Math.sin(var16) * var10;
            int var24 = getOscillatingColor(primaryColor, secondaryColor, (int)var8 + 90 * var12, false);
            fadingOrbitParticles.add(new TargetEspMask.FadingOrbitParticle(targetPosition.x + var20, targetPosition.y + var11 + var18, targetPosition.z + var22, var6, var24));
         }
      }

      Vec3 var25 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      BufferBuilder buffer = beginQuads();
      PoseStack poses = event.getPoses();
      float var27 = Фг;
      Iterator var28 = fadingOrbitParticles.iterator();

      while (var28.hasNext()) {
         TargetEspMask.FadingOrbitParticle var29 = (TargetEspMask.FadingOrbitParticle)var28.next();
         float var30 = (float)(var6 - var29.createdMillis);
         var29.updateAlpha(var6);
         if (var30 > Ф0 && var29.alpha < Фх) {
            var28.remove();
         } else {
            float var15;
            if (var30 < ФУ) {
               var15 = 1.0F - var30 / ФЗ;
            } else if (var30 < ФЕ) {
               var15 = var30 / ФП - 1.0F;
            } else {
               var15 = 1.0F;
            }

            var15 = Mth.clamp(var15, 0.0F, 1.0F);
            if (!(var29.alpha <= Ф西)) {
               poses.pushPose();
               poses.translate(var29.x - var25.x, var29.y - var25.y, var29.z - var25.z);
               poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
               appendGhostQuad(buffer, poses.last().pose(), var27 * var15, normalizeAdditiveColor(multiplyAlpha(var29.color, var29.alpha)));
               poses.popPose();
            }
         }
      }

      drawGhostGeometry(buffer, GHOST_TEXTURE);
   }

   private static void appendLine(BufferBuilder buffer, Matrix4f pose, float startX, float startY, float startZ, float endX, float endY, float endZ, Vector4f color) {
      buffer.addVertex(pose, startX, startY, startZ).setColor(color.x, color.y, color.z, color.w);
      buffer.addVertex(pose, endX, endY, endZ).setColor(color.x, color.y, color.z, color.w);
   }

   private static void appendCameraFacingRibbon(BufferBuilder buffer, Matrix4f pose, List<Vec3> points, float width, int color) {
      if (points.size() >= 2 && !(width <= Жф)) {
         Vec3 var5 = Vec3.ZERO;
         Vec3[] var6 = new Vec3[points.size() - 1];

         for (int var7 = 0; var7 < points.size() - 1; var7++) {
            Vec3 var8 = (Vec3)points.get(var7);
            Vec3 var9 = (Vec3)points.get(var7 + 1);
            Vec3 var10 = var9.subtract(var8);
            if (var10.lengthSqr() < Жи) {
               var6[var7] = var7 > 0 ? var6[var7 - 1] : new Vec3(1.0, 0.0, 0.0);
            } else {
               var10 = var10.normalize();
               Vec3 var11 = var8.add(var9).scale(Ж9);
               Vec3 var12 = var5.subtract(var11);
               if (var12.lengthSqr() < Жо) {
                  var12 = new Vec3(0.0, 0.0, 1.0);
               }

               Vec3 var13 = var10.cross(var12.normalize());
               if (var13.lengthSqr() < ЖO) {
                  var13 = var10.cross(new Vec3(0.0, 1.0, 0.0));
                  if (var13.lengthSqr() < Ж7) {
                     var13 = new Vec3(1.0, 0.0, 0.0);
                  }
               }

               var13 = var13.normalize();
               if (var7 > 0 && var13.dot(var6[var7 - 1]) < 0.0) {
                  var13 = var13.scale(ЖР);
               }

               var6[var7] = var13;
            }
         }

         Vec3[] var15 = new Vec3[points.size()];

         for (int var16 = 0; var16 < points.size(); var16++) {
            Vec3 var18;
            if (var16 == 0) {
               var18 = var6[0];
            } else if (var16 == points.size() - 1) {
               var18 = var6[var6.length - 1];
            } else {
               Vec3 var22 = var6[var16 - 1];
               Vec3 var24 = var6[var16];
               var18 = var22.add(var24);
               if (var18.lengthSqr() < Ж3) {
                  var18 = var22;
               } else {
                  var18 = var18.normalize();
               }
            }

            var15[var16] = var18.scale(width * getTrailWidthFactor(var16, points.size()));
         }

         for (int var17 = 0; var17 < points.size() - 1; var17++) {
            Vec3 var20 = (Vec3)points.get(var17);
            Vec3 var23 = (Vec3)points.get(var17 + 1);
            Vec3 var25 = var20.subtract(var15[var17]);
            Vec3 var26 = var20.add(var15[var17]);
            Vec3 var28 = var23.subtract(var15[var17 + 1]);
            Vec3 var14 = var23.add(var15[var17 + 1]);
            appendTexturedVertex(buffer, pose, (float)var25.x, (float)var25.y, (float)var25.z, 0.0F, Жр, color);
            appendTexturedVertex(buffer, pose, (float)var26.x, (float)var26.y, (float)var26.z, 1.0F, Ж6, color);
            appendTexturedVertex(buffer, pose, (float)var14.x, (float)var14.y, (float)var14.z, 1.0F, Ж_, color);
            appendTexturedVertex(buffer, pose, (float)var28.x, (float)var28.y, (float)var28.z, 0.0F, ЖШ, color);
         }
      }
   }

   public static void reset() {
      lastCrystalType4UpdateMillis = 0L;
      crystalType4Visibility = 0.0F;
      crystalType4HurtBlend = 0.0F;
      fadingTarget = null;
      targetVisibility = 0.0F;
      effectElapsedSeconds = 0.0F;
      lastRenderMillis = 0L;
      previousStyle = "";
      crystalType2Offsets.clear();
      crystalType2EntityId = рг;
      crystalType1Offsets.clear();
      crystalType1EntityId = р0;
      lightningEffects.clear();
      lastLightningSpawnMillis = 0L;
      if (lightningMask != null) {
         lightningMask.destroyBuffers();
         lightningMask = null;
      }

      animatedArcs.clear();
      lastArcSpawnMillis = 0L;
      rotatingPolylines.clear();
      lastPolylineSpawnMillis = 0L;
      previousGhostType = "";
      previousCrystalType = "";
      previousLightningType = "";
      orbitTrail.clear();
      lastOrbitTrailSampleMillis = 0L;
      orbitTrailEntityId = рх;
      resetGhostPulse();
      LightningTargetEspRenderer.н2();
      clearGhostTrails();
      phaseEntityId = рУ;
      cubeCompositePending = false;
      cubeRotationProgress = 0.0F;
      cubeHurtLatched = false;
      cubeRotationActive = false;
      cubeXRotationRadians = 0.0;
      cubeYRotationRadians = 0.0;
      cubeStartXRotationRadians = 0.0;
      cubeStartYRotationRadians = 0.0;
      cubeEndXRotationRadians = 0.0;
      cubeEndYRotationRadians = 0.0;
   }

   private static float[] getTriangleTileAnimation(float horizontalPosition, long nowMillis) {
      float var3 = 1.0F - Mth.clamp((horizontalPosition + ТИ) / Ть, 0.0F, 1.0F);
      float var4 = (float)nowMillis * Тж;
      var4 -= (float)Math.floor(var4);
      float var5 = Т弟;
      float var6 = Тб;
      float var7 = ТЫ;
      float var8 = Тl;
      if (var4 >= var5) {
         return new float[]{0.0F, 0.0F, 0.0F, 0.0F};
      } else {
         float var9 = var4 / var5 * var6;
         float var10 = (var9 - var3) / var7;
         if (var10 > 0.0F && var10 < 1.0F) {
            float var15 = var10 * var10 * (Т8 - 2.0F * var10);
            float var16 = (float)(Т5 * var15);
            float var17 = (float)Math.sin(Тв * var10);
            return new float[]{var16, var17 * Тъ, Math.abs((float)Math.sin(var16)), 0.0F};
         } else if (var10 >= 1.0F && var10 < 1.0F + var8) {
            float var11 = (var10 - 1.0F) / var8;
            float var12 = 1.0F - var11;
            float var13 = ТМ + ТН * (float)Math.sin(var11 * Тн * ТЦ);
            return new float[]{0.0F, 0.0F, 0.0F, var12 * var13};
         } else {
            return new float[]{0.0F, 0.0F, 0.0F, 0.0F};
         }
      }
   }

   private static RenderPipeline getOrbitGlowPipeline() {
      return crystalThroughWalls ? ORBIT_GLOW_NO_DEPTH_PIPELINE : ORBIT_GLOW_DEPTH_PIPELINE;
   }

   private static Vec3 toCameraRelative(Vec3 localPoint, Vec3 origin, Vec3 cameraPosition) {
      return localPoint.add(origin).subtract(cameraPosition);
   }

   private static void initializeCrystalType1Offsets(LivingEntity target) {
      if (crystalType1EntityId != target.getId()) {
         crystalType1EntityId = target.getId();
         crystalType1Offsets.clear();
         float var1 = target.getBbWidth() * _O + _7;
         float var2 = target.getBbHeight();
         float[][] var3 = new float[][]{
            {_Р, _3, _р},
            {_6, __, _Ш},
            {_ы, _й, _Ж},
            {_м, _К, _I},
            {_Т, _О, _Ф},
            {_Й, _к, _г},
            {_0, _х, _У},
            {_З, _Е, _П},
            {_西, _д, _Б},
            {_С, _з, _ц},
            {_я, Шс, ШВ},
            {ШЯ, ШХ, Ше},
            {ША, Ш4, Шп},
            {ШД, ШЪ, Шт},
            {Ша, ШЮ, ШЧ},
            {ШЛ, Ш诶, Ш1},
            {ШЬ, Ш衣, Шш},
            {ШИ, Шь, Шж},
            {Ш弟, Шб, ШЫ},
            {Шl, Ш8, 1.0F},
            {Ш5, Шв, Шъ},
            {ШМ, ШН, Шн},
            {ШЦ, Шч, ШЭ},
            {Ш必, Шщ, ШГ},
            {Шл, Шэ, Шю},
            {ШЩ, Шу, Ш2},
            {Шф, Ши, Ш9}
         };

         for (float[] var7 : var3) {
            float var8 = (float)Math.toRadians(var7[0]);
            float var9 = var1 * var7[2];
            crystalType1Offsets.add(new Vector3f(Mth.cos(var8) * var9, Mth.clamp(var7[1] * var2, Шо, var2 - ШO), Mth.sin(var8) * var9));
         }
      }
   }

   private static void drawGhostGeometry(BufferBuilder buffer, Identifier texture) {
      drawTexturedGeometry(buffer, texture, false, ghostThroughWalls ? GHOST_NO_DEPTH_PIPELINE : GHOST_DEPTH_PIPELINE);
   }

   private static void appendGhostOrbitSprite(
      PoseStack poses,
      BufferBuilder buffer,
      double x,
      double y,
      double z,
      double angle,
      double axisX,
      double axisY,
      double axisZ,
      float size,
      float alpha,
      int color
   ) {
      Vec3 var19 = getGhostOrbitOffset(angle, axisX, axisY, axisZ);
      Vec3 var20 = getGhostOrbitOffset(angle + йЬ, axisX, axisY, axisZ);
      double var21 = var20.x - var19.x;
      double var23 = var20.y - var19.y;
      double var25 = var20.z - var19.z;
      double var27 = Math.sqrt(var21 * var21 + var23 * var23 + var25 * var25);
      double var29 = var27 > й衣 ? 1.0 / var27 : 0.0;
      double var31 = var21 * var29;
      double var33 = var23 * var29;
      double var35 = var25 * var29;
      float var37 = (float)Mth.clamp(var27 * йш, йИ, йь);
      poses.pushPose();
      poses.translate(x + var19.x - var31 * (йж + size * й弟), y + var19.y - var33 * (йб + size * йЫ), z + var19.z - var35 * (йl + size * й8));
      poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
      Matrix4f pose = poses.last().pose();
      float var39 = size * (й5 + йв * var37);
      appendCenteredBillboard(buffer, pose, var39, multiplyAlpha(color, alpha * (йъ + йМ * var37)));
      appendCenteredBillboard(buffer, pose, size, multiplyAlpha(color, alpha));
      poses.popPose();
   }

   private static Matrix4f rotateTriangleTile(Matrix4f pose, float pivotX, float pivotY, float angleRadians, float depthOffset) {
      Matrix4f var5 = new Matrix4f(pose);
      var5.translate(pivotX, pivotY, depthOffset);
      var5.rotate(angleRadians, 0.0F, 1.0F, 0.0F);
      var5.translate(-pivotX, -pivotY, 0.0F);
      return var5;
   }

   private static List<Vec3> createLightningPath(Vec3 start, Vec3 end, int depth, double amplitude) {
      int var5 = (1 << depth) + 1;
      Vec3[] var6 = new Vec3[var5];
      var6[0] = start;
      var6[var5 - 1] = end;
      subdivideLightning(var6, 0, var5 - 1, amplitude, EFFECT_RANDOM);
      ArrayList var7 = new ArrayList(var5);

      for (Vec3 var11 : var6) {
         var7.add(var11);
      }

      return var7;
   }

   public static void compositeCubeMask() {
      if (cubeCompositePending && cubeMask != null) {
         cubeCompositePending = false;
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (mainTarget != null && mainTarget.getColorTextureView() != null) {
            EntityEspOuterGlowRenderer.compositeOuterGlow(cubeMask, mainTarget, cubeGlowColor, рП, cubeGlowStrength, 1.0F);
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass pass = encoder.createRenderPass(() -> TARGET_ESP_CUBE_COMPOSITE_PASS_DEBUG_LABEL, mainTarget.getColorTextureView(), Optional.empty())) {
               pass.setPipeline(CUBE_COMPOSITE_PIPELINE);
               pass.bindTexture(COMPOSITE_CUBE_MASK_IN_SAMPLER_NAME, cubeMask.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               pass.draw(3, 1, 0, 0);
            }
         }
      }
   }

   private static Vec3 getSpherePoint(Vec3 center, double radius, double latitudeRadians, double longitudeRadians) {
      double var7 = Math.cos(latitudeRadians) * radius;
      return center.add(Math.cos(longitudeRadians) * var7, Math.sin(latitudeRadians) * radius, Math.sin(longitudeRadians) * var7);
   }

   private static float pulseEnvelope(float progress) {
      if (progress <= 0.0F || progress >= 1.0F) {
         return 0.0F;
      } else {
         return progress < ыф ? smoothstep(progress / ыи) : smoothstep(1.0F - (progress - ы9) / ыо);
      }
   }

   private static TargetEspMask.AnimatedArc createAnimatedArc(long nowMillis) {
      float var2 = EFFECT_RANDOM.nextFloat();
      float var3;
      float var4;
      float var5;
      if (var2 < йО) {
         float var6 = йФ + EFFECT_RANDOM.nextFloat() * йЙ;
         float var7 = йк + EFFECT_RANDOM.nextFloat() * йг;
         var4 = var6 - var7 * й0;
         var3 = var6 + var7 * йх;
         var5 = йУ + EFFECT_RANDOM.nextFloat() * йЗ;
      } else if (var2 < йЕ) {
         var4 = йП + EFFECT_RANDOM.nextFloat() * й西;
         var3 = йд + EFFECT_RANDOM.nextFloat() * йБ;
         var5 = йС + EFFECT_RANDOM.nextFloat() * йз;
      } else {
         var4 = йц - EFFECT_RANDOM.nextFloat() * йя;
         var3 = Жс + EFFECT_RANDOM.nextFloat() * ЖВ;
         var5 = ЖЯ + EFFECT_RANDOM.nextFloat() * ЖХ;
      }

      return new TargetEspMask.AnimatedArc(
         EFFECT_RANDOM.nextDouble() * Же * ЖА,
         EFFECT_RANDOM.nextBoolean() ? 1 : -1,
         var5,
         var3,
         var4,
         1.0F,
         Ж4 + EFFECT_RANDOM.nextFloat() * Жп,
         ЖД,
         EFFECT_RANDOM.nextInt(4),
         EFFECT_RANDOM.nextFloat() * ЖЪ,
         nowMillis,
         1500 + EFFECT_RANDOM.nextInt(500),
         EFFECT_RANDOM.nextFloat() * Жт
      );
   }

   private TargetEspMask() {
   }

   private static void renderOrbitRings(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha, long nowMillis) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      initializeTargetPhase(target);
      float var8 = (float)nowMillis * Iч + targetPhase;
      float var9 = target.getBbHeight();
      Vec3 var10 = targetPosition.add(0.0, var9 * IЭ, 0.0);
      Matrix4f pose = event.getPoses().last().pose();
      float var12 = target.getBbWidth() * I必 + Iщ;
      float var13 = var12 * IГ;
      float[] var14 = new float[]{Iл, Iэ, Iю};
      float[] var15 = new float[]{IЩ, Iу, I2};
      float[] var16 = new float[]{0.0F, Iф, Iи};
      BufferBuilder var17 = beginQuads();
      BufferBuilder var18 = beginQuads();
      appendCameraFacingBillboard(var18, pose, var10.x, var10.y, var10.z, var12 * I9, multiplyAlpha(brightenColor(primaryColor, Iо), alpha * IO));
      appendCameraFacingBillboard(var18, pose, var10.x, var10.y, var10.z, var12 * I7, multiplyAlpha(brightenColor(primaryColor, IР), alpha * I3));

      for (int var19 = 0; var19 < var14.length; var19++) {
         float var20 = var8 * var15[var19] * Iр + var16[var19];
         double var21 = Math.toRadians(var20);
         double var23 = Math.toRadians(var14[var19]);
         Vec3 var25 = new Vec3(Math.sin(var23) * Math.cos(var21), Math.cos(var23), Math.sin(var23) * Math.sin(var21));
         Vec3[] var26 = createPerpendicularBasis(var25);
         int var27 = var19 == var14.length - 1 ? brightenColor(secondaryColor, I6) : brightenColor(primaryColor, I_);
         appendOrientedBillboard(var17, pose, var10, var26[0], var26[1], var13, multiplyAlpha(var27, alpha * IШ));
         double var28 = Math.toRadians(var20 * (var15[var19] < 0.0F ? Iы : Iй) + var16[var19]);
         Vec3 var30 = var10.add(var26[0].scale(Math.cos(var28) * var12)).add(var26[1].scale(Math.sin(var28) * var12));
         appendCameraFacingBillboard(var18, pose, var30.x, var30.y, var30.z, IЖ, multiplyAlpha(brightenColor(primaryColor, Iм), alpha));
         appendCameraFacingBillboard(var18, pose, var30.x, var30.y, var30.z, IК, multiplyAlpha(brightenColor(primaryColor, II), alpha * IТ));
      }

      drawColorGeometry(var17, RING_PIPELINE);
      drawTexturedGeometry(var18, WORLD_BLOOM_TEXTURE, true);
   }

   private static void renderRingType2(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha, float tickDelta) {
      float var7 = alpha * Ой;
      if (!(var7 <= ОЖ)) {
         Vec3 var8 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         float var9 = target.getBbHeight();
         long var10 = Math.max(1L, Math.round(Ом / effectSpeed));
         double var12 = (double)(System.currentTimeMillis() % var10) / var10 * ОК * ОI;
         float var14 = 0.0F;
         if (hurtRecolorEnabled && target.hurtTime > 0) {
            var14 = Mth.clamp((target.hurtTime - tickDelta) / ОТ, 0.0F, 1.0F);
         }

         float var15 = target.getBbWidth() * ОО;
         float var16 = target.getBbWidth() + var15 - var15 * var7;
         float var17 = (float)(targetPosition.x - var8.x);
         float var18 = (float)(targetPosition.y - var8.y);
         float var19 = (float)(targetPosition.z - var8.z);
         float var20 = var18 + var9 * (float)(ОФ + ОЙ * Math.cos(var12));
         float var21 = (float)(Math.sin(var12) * var9 * Ок);
         Matrix4f pose = event.getPoses().last().pose();
         BufferBuilder buffer = beginQuads();
         byte var24 = 2;
         float var25 = Ог;
         float var26 = О0;

         for (int var27 = 0; var27 < var24; var27++) {
            float var28 = var20 + var25 / var24 * var27;
            float var29 = var7 * (1.0F - (float)var27 / var24 * Ох);
            int var30 = var27 * 10;

            for (int var31 = 0; var31 < 360; var31 += 5) {
               int var32 = var31 / 5;
               int var33 = getAnimatedEffectColor(primaryColor, secondaryColor, var31 * 5 + var30, var14, ОУ);
               Vector4f var34 = ColorMath.toRgbaVector(multiplyAlpha(var33, var29));
               float var35 = var17 + (float)(CIRCLE_COSINES[var32] * var16);
               float var36 = var19 + (float)(CIRCLE_SINES[var32] * var16);
               float var37 = var17 + (float)(CIRCLE_COSINES[var32 + 1] * var16);
               float var38 = var19 + (float)(CIRCLE_SINES[var32 + 1] * var16);
               float var39 = var37 - var35;
               float var40 = var38 - var36;
               float var41 = Mth.sqrt(var39 * var39 + var40 * var40);
               float var42 = var41 == 0.0F ? var26 : -var40 / var41 * var26;
               float var43 = var41 == 0.0F ? 0.0F : var39 / var41 * var26;
               appendTexturedVertex(buffer, pose, var35 + var42, var28, var36 + var43, 0.0F, 0.0F, var34);
               appendTexturedVertex(buffer, pose, var37 + var42, var28, var38 + var43, 0.0F, 1.0F, var34);
               appendTexturedVertex(buffer, pose, var37 - var42, var28, var38 - var43, 1.0F, 1.0F, var34);
               appendTexturedVertex(buffer, pose, var35 - var42, var28, var36 - var43, 1.0F, 0.0F, var34);
            }
         }

         for (int var44 = 0; var44 < 360; var44 += 5) {
            int var45 = var44 / 5;
            int var46 = getAnimatedEffectColor(primaryColor, secondaryColor, var44 * 20, var14, ОЗ);
            int var47 = getAnimatedEffectColor(primaryColor, secondaryColor, (var44 + 5) * 20, var14, ОЕ);
            Vector4f var48 = ColorMath.toRgbaVector(multiplyAlpha(var46, var7));
            Vector4f var49 = ColorMath.toRgbaVector(multiplyAlpha(var46, 0.0F));
            Vector4f var50 = ColorMath.toRgbaVector(multiplyAlpha(var47, var7));
            Vector4f var51 = ColorMath.toRgbaVector(multiplyAlpha(var47, 0.0F));
            float var52 = var17 + (float)(CIRCLE_COSINES[var45] * var16);
            float var53 = var19 + (float)(CIRCLE_SINES[var45] * var16);
            float var54 = var17 + (float)(CIRCLE_COSINES[var45 + 1] * var16);
            float var55 = var19 + (float)(CIRCLE_SINES[var45 + 1] * var16);
            float var56 = var20 + var21;
            appendTexturedVertex(buffer, pose, var52, var20, var53, 0.0F, 0.0F, var48);
            appendTexturedVertex(buffer, pose, var52, var56, var53, 0.0F, 1.0F, var49);
            appendTexturedVertex(buffer, pose, var54, var56, var55, 1.0F, 1.0F, var51);
            appendTexturedVertex(buffer, pose, var54, var20, var55, 1.0F, 0.0F, var50);
         }

         drawColorGeometry(buffer, RING_WAVE_PIPELINE);
      }
   }

   private static void renderGhostType3(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int primaryColor, int secondaryColor, float alpha) {
      if (alpha <= Фд) {
         clearGhostTrails();
      } else {
         targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
         long var6 = System.currentTimeMillis();
         PoseStack poses = event.getPoses();
         BufferBuilder buffer = beginQuads();

         for (int var10 = 0; var10 < ghostTrails.length; var10++) {
            double var11 = var6 * ФБ;
            double var13 = var10 * (ФС / ghostTrails.length);
            double var15 = target.getBbWidth() + Фз;
            double var17 = Math.sin(var11 + var13) * var15;
            double var19 = Math.cos(var11 + var13) * var15;
            double var21 = var10 % 2 == 0 ? 1.0 : Фц;
            double var23 = Math.sin((var11 + var13) * Фя) * var21;
            double var25 = var23 * (target.getBbHeight() * Йс) + target.getBbHeight() * ЙВ;
            LinkedList<Vec3> var27 = ghostTrails[var10];
            Vec3 var28 = new Vec3(var17, var25, var19);
            if (!var27.isEmpty()) {
               Vec3 var29 = (Vec3)var27.getFirst();
               double var30 = var29.distanceTo(var28);
               int var32 = Mth.clamp((int)Math.ceil(var30 / ЙЯ), 1, 6);

               for (int var33 = 1; var33 <= var32; var33++) {
                  double var34 = (double)var33 / var32;
                  var27.addFirst(var29.lerp(var28, var34));
               }
            } else {
               var27.addFirst(var28);
            }

            while (var27.size() > 34) {
               var27.removeLast();
            }

            int var42 = 0;
            int var43 = var27.size();

            for (Vec3 var44 : var27) {
               float var45 = 1.0F - (float)var42 / var43;
               float var46 = (float)(targetPosition.x + var44.x);
               float var35 = (float)(targetPosition.y + var44.y);
               float var36 = (float)(targetPosition.z + var44.z);
               int var37 = ColorMath.lerpArgb(primaryColor, secondaryColor, var45);
               float var38 = var42 == 0 ? ЙХ : Йе;
               float var39 = var38 * var45 * alpha;
               float var40 = (float)(Math.pow(var45, ЙА) * alpha);
               appendCameraFacingBillboard(buffer, poses, var46, var35, var36, var39, multiplyAlpha(var37, var40));
               if (var42 == 0) {
                  appendCameraFacingBillboard(buffer, poses, var46, var35, var36, Й4 * alpha, multiplyAlpha(-1, alpha));
               }

               var42++;
            }
         }

         drawTexturedGeometry(buffer, GHOST_PARTICLE_BLOOM_TEXTURE, false);
      }
   }

   private static float moveToward(float value, float target, float step) {
      if (value < target) {
         return Math.min(value + step, target);
      } else {
         return value > target ? Math.max(value - step, target) : value;
      }
   }

   private static int multiplyAlpha(int color, float alphaMultiplier) {
      return ColorMath.packArgb(
         ColorMath.getRed(color), ColorMath.getGreen(color), ColorMath.getBlue(color), ColorMath.getAlpha(color) * Mth.clamp(alphaMultiplier, 0.0F, 1.0F)
      );
   }

   private static void renderCrystalType3(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis) {
      Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      Vec3 targetCenter = target.position().add(0.0, target.getBbHeight() / Й6, 0.0);
      float radius = target.getBbWidth() * Й_;
      float height = target.getBbHeight();
      float var11 = ЙШ - Йы * alpha;
      float var12 = (float)(nowMillis % Йй) / ЙЖ + alpha;
      PoseStack poses = event.getPoses();
      BufferBuilder crystalBuffer = beginTriangles();

      for (int var15 = 0; var15 < 360; var15 += 20) {
         double var16 = Math.toRadians(var15 + var12 * Йм);
         float var18 = (float)Math.sin(var16) * radius * var11;
         float var19 = (float)Math.cos(var16) * radius * var11;
         float var20 = ЙК + height * Math.abs((float)Math.sin(var15));
         Vec3 var21 = targetPosition.add(var18, var20, var19);
         poses.pushPose();
         poses.translate(var21.x - cameraPosition.x, var21.y - cameraPosition.y, var21.z - cameraPosition.z);
         Vector3f var22 = new Vector3f((float)(targetCenter.x - var21.x), (float)(targetCenter.y - var21.y), (float)(targetCenter.z - var21.z)).normalize();
         poses.mulPose(new Quaternionf().rotationTo(new Vector3f(0.0F, 1.0F, 0.0F), var22));
         appendOctahedron(crystalBuffer, poses, ЙI, multiplyAlpha(color, alpha));
         poses.popPose();
      }

      drawColorGeometry(crystalBuffer, getOrbitCrystalPipeline());
      BufferBuilder glowBuffer = beginQuads();

      for (int var24 = 0; var24 < 360; var24 += 20) {
         double var17 = Math.toRadians(var24 + var12 * ЙТ);
         float var25 = (float)Math.sin(var17) * radius * var11;
         float var26 = (float)Math.cos(var17) * radius * var11;
         float var27 = ЙО + height * Math.abs((float)Math.sin(var24));
         Vec3 var28 = targetPosition.add(var25, var27, var26);
         appendCameraFacingBillboard(glowBuffer, poses, var28.x - cameraPosition.x, var28.y - cameraPosition.y, var28.z - cameraPosition.z, ЙФ * alpha, multiplyAlpha(color, alpha * ЙЙ));
         appendCameraFacingBillboard(glowBuffer, poses, var28.x - cameraPosition.x, var28.y - cameraPosition.y, var28.z - cameraPosition.z, Йк * alpha, multiplyAlpha(color, alpha * Йг));
      }

      drawTexturedGeometry(glowBuffer, CRYSTAL_PARTICLE_BLOOM_TEXTURE, false, getOrbitGlowPipeline());
   }

   public static void setRingParticleOptions(boolean throughWalls, float particleSize, int layers) {
      ringThroughWalls = throughWalls;
      ringParticleSize = particleSize;
      ringLayers = layers;
   }

   private static float hashColorUnitFloat(int index) {
      float var1 = (float)Math.sin(index * Тл) * Тэ;
      return var1 - (float)Math.floor(var1);
   }

   private static void clearGhostTrails() {
      for (LinkedList var3 : ghostTrails) {
         var3.clear();
      }
   }

   private static void appendCameraFacingBillboard(BufferBuilder buffer, Matrix4f pose, double x, double y, double z, float size, int color) {
      PoseStack poses = new PoseStack();
      poses.mulPose(pose);
      poses.translate(x, y, z);
      poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
      appendCenteredBillboard(buffer, poses.last().pose(), size, color);
   }

   private static Vec3 rotateVector(Vec3 vector, double xRadians, double yRadians, double zRadians) {
      double var7 = Math.sin(xRadians);
      double var9 = Math.cos(xRadians);
      double var11 = vector.y * var9 - vector.z * var7;
      double var13 = vector.y * var7 + vector.z * var9;
      vector = new Vec3(vector.x, var11, var13);
      double var15 = Math.sin(yRadians);
      double var17 = Math.cos(yRadians);
      double var19 = vector.x * var17 - vector.z * var15;
      var13 = vector.x * var15 + vector.z * var17;
      vector = new Vec3(var19, vector.y, var13);
      double var21 = Math.sin(zRadians);
      double var23 = Math.cos(zRadians);
      var19 = vector.x * var23 - vector.y * var21;
      var11 = vector.x * var21 + vector.y * var23;
      return new Vec3(var19, var11, vector.z);
   }

   private static void renderCrystalType4(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, float deltaSeconds, long nowMillis) {
      float hurtBlend = (float)Math.sin(target.hurtTime * Йп / ЙД);
      crystalType4HurtBlend = crystalType4HurtBlend + (hurtBlend - crystalType4HurtBlend) * (float)Math.sin(Math.min(deltaSeconds / ЙЪ, 1.0F) * Йт / Йа);
      int colorPhase = (int)((nowMillis / ЙЮ + 1L) % ЙЧ);
      colorPhase = colorPhase >= 180 ? 360 - colorPhase : colorPhase;
      int brightColor = ColorMath.packArgb(
         Math.min(255, Math.round(ColorMath.getRed(color) / ЙЛ)),
         Math.min(255, Math.round(ColorMath.getGreen(color) / Й诶)),
         Math.min(255, Math.round(ColorMath.getBlue(color) / Й1)),
         ЙЬ
      );
      int effectColor = ColorMath.lerpArgb(color, brightColor, colorPhase / Й衣);
      if (hurtRecolorEnabled) {
         effectColor = ColorMath.lerpArgb(effectColor, Йш, crystalType4HurtBlend);
      }

      effectColor &= ЙИ;
      float var12 = (float)Math.cos(nowMillis / Йь) * Йж + crystalType4HurtBlend * Й弟;
      float sizeScale = (1.0F - Йб * crystalType4HurtBlend) * alpha;
      Camera camera = MINECRAFT_CLIENT.gameRenderer.mainCamera();
      Vec3 cameraPosition = camera.position();
      PoseStack poses = event.getPoses();

      for (int var17 = 0; var17 < 3; var17++) {
         BufferBuilder buffer = NightixCubeGlow.method_9(var17 == 2, var17 == 0);

         for (int var19 = 0; var19 < 360; var19 += 12) {
            float var20 = (float)Math.toRadians(var19 + var12);
            float var21 = (float)(Math.sin(var20) * target.getBbWidth() * ЙЫ * Йl);
            float var22 = (float)(Math.cos(var20) * target.getBbWidth() * Й8 * Й5);
            double var23 = target.getBbHeight() * Math.abs(Math.sin(((int)(var19 * Йв) & Йъ) * ЙМ * ЙН / Йн));
            poses.pushPose();
            poses.translate(targetPosition.x + var21 - cameraPosition.x, targetPosition.y + var23 - cameraPosition.y, targetPosition.z + var22 - cameraPosition.z);
            if (var17 == 0) {
               poses.mulPose(camera.rotation());
               NightixCubeGlow.method_9(buffer, poses.last().pose(), ЙЦ * sizeScale, effectColor | (int)(alpha * Йч * ЙЭ) << 24);
            } else {
               poses.mulPose(new Quaternionf().rotationTo(new Vector3f(0.0F, 1.0F, 0.0F), new Vector3f(-var21, (float)(-var23), -var22).normalize()));
               if (var17 == 1) {
                  NightixCubeGlow.Ц(buffer, poses.last().pose(), Й必 * sizeScale, effectColor | (int)(alpha * Йщ * ЙГ) << 24);
               } else {
                  NightixCubeGlow.ц(buffer, poses.last().pose(), Йл * sizeScale, effectColor | (int)(alpha * Йэ) << 24);
               }
            }

            poses.popPose();
         }

         NightixCubeGlow.method_9(buffer, var17 == 2, var17 == 0 ? NightixCubeGlow.field_4 : null, !crystalThroughWalls);
      }
   }

   private static void addOrbitTrailSample(double x, double y, double z, double angle, double axisX, double axisY, double axisZ, float size, float alpha) {
      Vec3 orbitOffset = getGhostOrbitOffset(angle, axisX, axisY, axisZ);
      orbitTrail.addLast(new TargetEspMask.TrailSample(new Vec3(x + orbitOffset.x, y + orbitOffset.y, z + orbitOffset.z), System.currentTimeMillis(), size, alpha));

      while (orbitTrail.size() > 130) {
         orbitTrail.pollFirst();
      }
   }

   private static void renderGhostType1(WorldEffectsEvent event, Vec3 targetPosition, int color, float alpha) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      BufferBuilder buffer = beginQuads();
      PoseStack poses = event.getPoses();
      float orbitPhase = effectElapsedSeconds * Од;
      float visibility = smoothstep(targetVisibility);

      for (int var8 = 0; var8 < 9; var8 += 3) {
         for (int var9 = 0; var9 < 12; var9++) {
            float var10 = orbitPhase + var9 * ОБ;
            int var11 = var8 * var8;
            double var12 = targetPosition.x + ОС * Mth.sin(var10 + var11);
            double var14 = targetPosition.y + Оз + Оц * Mth.sin(orbitPhase + var9 * Оя) + Фс * var8;
            double var16 = targetPosition.z + ФВ * Mth.cos(var10 - var11);
            float var18 = ФЯ * visibility * (ФХ + var9 / Фе);
            poses.pushPose();
            poses.translate(var12, var14, var16);
            poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
            appendCenteredBillboard(buffer, poses.last().pose(), var18, multiplyAlpha(color, alpha));
            poses.popPose();
         }
      }

      drawTexturedGeometry(buffer, GHOST_FIRE_TEXTURE, true);
   }

   private static int multiplyRgb(int color, float brightness) {
      return ColorMath.packArgb(
         Math.min(255, Math.round(ColorMath.getRed(color) * brightness)),
         Math.min(255, Math.round(ColorMath.getGreen(color) * brightness)),
         Math.min(255, Math.round(ColorMath.getBlue(color) * brightness)),
         ColorMath.getAlpha(color)
      );
   }

   private static void appendScaledOrientedBillboard(BufferBuilder buffer, Matrix4f pose, Vec3 center, Vec3 basisX, Vec3 basisY, float sizeScale, int color) {
      appendOrientedBillboard(buffer, pose, center, basisX, basisY, sizeScale * 2.0F / Ож, color);
   }

   private static float gradientNoise2d(int hash, float x, float y) {
      return switch (hash & 3) {
         case 0 -> x + y;
         case 1 -> -x + y;
         case 2 -> x - y;
         default -> -x - y;
      };
   }

   public static void beginDeferredRender() {
      TargetLighting.resetFrame();
      deferredRenderActive = true;
      deferredEvent = null;
      deferredTarget = null;
      deferredStyle = null;
      deferredProjectionBuffer = RenderSystem.getProjectionMatrixBuffer();
      deferredProjectionType = RenderSystem.getProjectionType();
   }

   private static Vec3 getLightningBranchPoint(Vec3 origin, double height, double radius, float initialAngleRadians, float turnCount, int direction, float progress) {
      double angleRadians = initialAngleRadians + direction * turnCount * мO * м7 * progress + (EFFECT_RANDOM.nextDouble() - мР) * м3;
      double radialOffset = radius * (мр + EFFECT_RANDOM.nextDouble() * м6);
      double verticalOffset = height * (м_ + progress * мШ) + (EFFECT_RANDOM.nextDouble() - мы) * height * мй;
      return origin.add(Math.cos(angleRadians) * radialOffset, verticalOffset, Math.sin(angleRadians) * radialOffset);
   }

   private static void appendColoredTriangleQuad(
      BufferBuilder buffer,
      Matrix4f pose,
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float x3,
      float y3,
      float z3,
      int color1,
      int color2,
      int color3
   ) {
      Vector4f firstColor = ColorMath.toRgbaVector(color1);
      Vector4f secondColor = ColorMath.toRgbaVector(color2);
      Vector4f thirdColor = ColorMath.toRgbaVector(color3);
      appendTexturedVertex(buffer, pose, x1, y1, z1, гЧ, гЛ, firstColor);
      appendTexturedVertex(buffer, pose, x3, y3, z3, г诶, г1, thirdColor);
      appendTexturedVertex(buffer, pose, x2, y2, z2, гЬ, г衣, secondColor);
      appendTexturedVertex(buffer, pose, x2, y2, z2, гш, гИ, secondColor);
   }

   public static void setDistortionOptions(boolean enabled, float strength) {
      distortionEnabled = enabled;
      distortionStrength = strength;
   }

   private static float easeOutBack(float value) {
      float var1 = Mth.clamp(value, 0.0F, 1.0F) - 1.0F;
      return 1.0F + йГ * var1 * var1 * var1 + йл * var1 * var1;
   }

   private static void renderLightningType2(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis, boolean active) {
      double timeSeconds = System.nanoTime() * мД;
      float phase = (float)(timeSeconds * мЪ);
      if (active && nowMillis - lastPolylineSpawnMillis >= мт && rotatingPolylines.size() < 18) {
         for (int var11 = 0; var11 < 2 && rotatingPolylines.size() < 18; var11++) {
            rotatingPolylines.add(createRotatingPolyline(target, targetPosition, nowMillis, phase));
         }

         lastPolylineSpawnMillis = nowMillis;
      }

      rotatingPolylines.removeIf(expiredPolyline -> nowMillis - expiredPolyline.createdMillis > expiredPolyline.durationMillis);
      if (!rotatingPolylines.isEmpty() && !(alpha <= ма)) {
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         Matrix4f pose = event.getPoses().last().pose();
         PoseStack poses = event.getPoses();
         int brightColor = brightenColor(color, мЮ);
         int coreColor = brightenColor(color, мЧ);
         BufferBuilder haloBuffer = beginQuads();
         BufferBuilder ribbonBuffer = beginQuads();

         for (TargetEspMask.RotatingPolyline polyline : rotatingPolylines) {
            float progress = (float)(nowMillis - polyline.createdMillis) / (float)polyline.durationMillis;
            if (!(progress < 0.0F) && !(progress > 1.0F)) {
               float envelope = (float)Math.sin(мЛ * Mth.clamp(progress, 0.0F, 1.0F));
               envelope = smoothstep(envelope);
               float effectAlpha = alpha * envelope;
               if (!(effectAlpha <= м诶)) {
                  double elapsedSeconds = (System.nanoTime() - polyline.createdNanos) * м1;
                  float rotationAngle = (float)(elapsedSeconds * polyline.angularSpeed * polyline.rotationDirection);
                  float cosine = (float)Math.cos(rotationAngle);
                  float sine = (float)Math.sin(rotationAngle);
                  ArrayList points = new ArrayList(polyline.localPoints.size());

                  for (Vec3 var30 : polyline.localPoints) {
                     double var31 = var30.x * cosine - var30.z * sine;
                     double var33 = var30.x * sine + var30.z * cosine;
                     points.add(targetPosition.add(var31, var30.y, var33).subtract(cameraPosition));
                  }

                  if (points.size() >= 2) {
                     for (int var37 = 0; var37 < points.size(); var37++) {
                        Vec3 point = (Vec3)points.get(var37);
                        float taper = 1.0F - (float)var37 / Math.max(1, points.size() - 1) * мЬ;
                        appendCameraFacingBillboard(haloBuffer, poses, point.x, point.y, point.z, м衣 * taper * polyline.widthScale, multiplyAlpha(color, effectAlpha * мш));
                        appendCameraFacingBillboard(haloBuffer, poses, point.x, point.y, point.z, мИ * taper * polyline.widthScale, multiplyAlpha(brightColor, effectAlpha * мь));
                        appendCameraFacingBillboard(haloBuffer, poses, point.x, point.y, point.z, мж * taper * polyline.widthScale, multiplyAlpha(coreColor, effectAlpha * м弟));
                     }

                     appendCameraFacingRibbon(ribbonBuffer, pose, points, мб * polyline.widthScale, multiplyAlpha(color, effectAlpha * мЫ));
                     appendCameraFacingRibbon(ribbonBuffer, pose, points, мl * polyline.widthScale, multiplyAlpha(brightColor, effectAlpha * м8));
                     appendCameraFacingRibbon(ribbonBuffer, pose, points, м5 * polyline.widthScale, multiplyAlpha(coreColor, effectAlpha));
                     appendCameraFacingRibbon(ribbonBuffer, pose, points, мв * polyline.widthScale, multiplyAlpha(-1, effectAlpha));
                  }
               }
            }
         }

         drawTexturedGeometry(haloBuffer, WORLD_BLOOM_TEXTURE, false, LIGHTNING_ARC_PIPELINE);
         drawTexturedGeometry(ribbonBuffer, TARGET_GLOW_TEXTURE, false, LIGHTNING_ARC_PIPELINE);
      }
   }

   private static double[] createCircleLookup(boolean cosine) {
      double[] lookup = new double[73];

      for (int var2 = 0; var2 <= 72; var2++) {
         double angle = Math.toRadians(var2 * О西);
         lookup[var2] = cosine ? Math.cos(angle) : Math.sin(angle);
      }

      return lookup;
   }

   private static int getOscillatingColor(int primaryColor, int secondaryColor, int phaseOffset, boolean brighten) {
      int colorPhase = (int)((System.currentTimeMillis() / г8 + phaseOffset) % г5);
      if (colorPhase >= 180) {
         colorPhase = 360 - colorPhase;
      }

      int color = ColorMath.lerpArgb(primaryColor, secondaryColor, colorPhase / гв);
      return brighten ? brightenColor(color, гъ) : color;
   }

   private static void resetGhostPulse() {
      ghostHurtCycleProgress = 1.0F;
      ghostHurtPulse = 0.0F;
      ghostOrbitalPhase = 0.0;
      ghostHurtLatched = false;
      ghostPulseEntityId = ы2;
   }

   private static void drawLightningGeometry(BufferBuilder glowBuffer, BufferBuilder coreBuffer, RenderPipeline pipeline) {
      drawLightningGeometry(glowBuffer, coreBuffer, pipeline, -1);
   }

   private static void renderGhostType6(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      float radius = target.getBbWidth() * ы诶;
      float rotationDegrees = (float)(System.currentTimeMillis() % ы1) / ыЬ;
      int segmentCount = Mth.clamp(ghostSegments, 2, 4);
      float segmentDegrees = ы衣 / segmentCount;
      float arcDegrees = ыш;
      float gapDegrees = segmentDegrees - arcDegrees;
      int var11 = 0;
      int var12 = 0;
      int var13 = 0;
      Matrix4f pose = event.getPoses().last().pose();
      BufferBuilder buffer = beginQuads();

      for (int var16 = 0; var16 < 360; var16 += 2) {
         if (var12 > 0) {
            var12 -= 2;
            if (var12 <= 0) {
               var13++;
            }
         } else {
            float var17 = ыИ + ыь * var11;
            float var18 = ыж + ы弟 * var11;
            var11 += 2;
            if (var11 > arcDegrees) {
               var12 = (int)gapDegrees;
               var11 = 0;
            } else {
               float var19 = Math.max(ыб, ыЫ - ыl * alpha);
               float var20 = (float)Math.toRadians(var16 + rotationDegrees);
               float var21 = (float)(targetPosition.x + Math.sin(var20) * radius * var19);
               float var22 = (float)(targetPosition.z + Math.cos(var20) * radius * var19);
               float var23 = (var13 % 2 == 0 ? ы8 : ы5) * target.getBbHeight();
               float var24 = (float)Math.sin(Math.toRadians(var16 / 2.0F + rotationDegrees / ыв));
               float var25 = (float)(targetPosition.y + target.getBbHeight() * ыъ + target.getBbHeight() / ыМ * var24 + var23);
               appendCameraFacingBillboard(buffer, pose, var21, var25, var22, var18, multiplyAlpha(color, alpha * ыН));
               appendCameraFacingBillboard(buffer, pose, var21, var25, var22, var17, multiplyAlpha(color, alpha));
            }
         }
      }

      drawTexturedGeometry(buffer, TARGET_GLOW_TEXTURE, true);
   }

   private static void drawLightningGeometry(BufferBuilder glowBuffer, BufferBuilder coreBuffer, RenderPipeline pipeline, int glowColor) {
      RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      if (mainTarget != null && mainTarget.getColorTextureView() != null && mainTarget.getDepthTextureView() != null) {
         TextureTarget maskTarget = getLightningMask(mainTarget.width, mainTarget.height);
         maskTarget.copyDepthFrom(mainTarget);
         drawMaskGeometry(coreBuffer, pipeline, maskTarget, true);
         EntityEspOuterGlowRenderer.compositeOuterGlow(maskTarget, mainTarget, glowColor, I5, Iв, 1.0F);
         drawColorGeometry(glowBuffer, pipeline);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

         try (RenderPass pass = encoder.createRenderPass(() -> TARGET_ESP_LIGHTNING_CORE_PASS_DEBUG_LABEL, mainTarget.getColorTextureView(), Optional.empty())) {
            pass.setPipeline(LIGHTNING_CORE_PIPELINE);
            pass.bindTexture(DRAW_LIGHTNING_GEOMETRY_IN_SAMPLER_NAME, maskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            pass.draw(3, 1, 0, 0);
         }
      }
   }

   public static void render(WorldEffectsEvent event, LivingEntity target, String style, int primaryColor, int secondaryColor) {
      if (deferredRenderActive && lightingEnabled && (style.equals(SCENE_CAPTURE_GHOST_STYLE_LABEL) || style.equals(SCENE_CAPTURE_CRYSTAL_STYLE_LABEL) || style.equals(SCENE_CAPTURE_LIGHTNING_STYLE_LABEL)) && (target != null && target.isAlive() || hasVisibleTarget())) {
         TargetLighting.captureSceneTextures();
      }

      if (deferredRenderActive && shouldDeferStyle(style)) {
         deferredEvent = event;
         deferredTarget = target;
         deferredStyle = style;
         deferredPrimaryColor = primaryColor;
         deferredSecondaryColor = secondaryColor;
      } else {
         long var5 = System.currentTimeMillis();
         float var7 = lastRenderMillis == 0L ? 0.0F : Math.min(рз, (float)(var5 - lastRenderMillis) / рц);
         lastRenderMillis = var5;
         boolean var8 = target != null && target.isAlive();
         if (var8) {
            previousPrimaryColor = primaryColor;
            previousSecondaryColor = secondaryColor;
         }

         boolean var9 = style.equals(VISIBILITY_RING_STYLE_LABEL) && ringType.equals(VISIBILITY_RING_TYPE3_LABEL);
         float var10 = var9 ? 1.0F : (!var8 && style.equals(VISIBILITY_CUBE_STYLE_LABEL) ? field_6Я : field_6Х);
         targetVisibility = moveToward(targetVisibility, var8 ? 1.0F : 0.0F, var7 / var10);
         if (var8) {
            fadingTarget = target;
         }

         if (!previousStyle.equals(style)) {
            if (PREVIOUS_LIGHTNING_STYLE_RESET_LABEL.equals(previousStyle) && PREVIOUS_LIGHTNING_TYPE3_STYLE_RESET_LABEL.equals(previousLightningType)) {
               LightningTargetEspRenderer.н2();
            }

            effectElapsedSeconds = 0.0F;
            crystalType1Offsets.clear();
            crystalType1EntityId = field_64;
            crystalType2Offsets.clear();
            crystalType2EntityId = field_6п;
            lightningEffects.clear();
            lastLightningSpawnMillis = 0L;
            animatedArcs.clear();
            lastArcSpawnMillis = 0L;
            rotatingPolylines.clear();
            lastPolylineSpawnMillis = 0L;
            clearGhostTrails();
            fadingOrbitParticles.clear();
            orbitEmissionAngle = 0.0;
            phaseEntityId = field_6Д;
            cubeCompositePending = false;
            cubeRotationProgress = 0.0F;
            cubeHurtLatched = false;
            cubeRotationActive = false;
            cubeXRotationRadians = 0.0;
            cubeYRotationRadians = 0.0;
            cubeStartXRotationRadians = 0.0;
            cubeStartYRotationRadians = 0.0;
            cubeEndXRotationRadians = 0.0;
            cubeEndYRotationRadians = 0.0;
            previousGhostType = "";
            previousCrystalType = "";
            previousLightningType = "";
            orbitTrail.clear();
            lastOrbitTrailSampleMillis = 0L;
            orbitTrailEntityId = field_6Ъ;
            resetGhostPulse();
            previousStyle = style;
         }

         boolean var11 = style.equals(CRYSTAL_PULSE_STYLE_LABEL) && crystalType.equals(CRYSTAL_PULSE_TYPE4_LABEL);
         float var12 = lastCrystalType4UpdateMillis == 0L ? 0.0F : (float)(var5 - lastCrystalType4UpdateMillis) / field_6Ю;
         if (var11) {
            crystalType4Visibility = crystalType4Visibility + ((var8 ? 1.0F : 0.0F) - crystalType4Visibility) * (float)Math.sin(Math.min(var12 / field_6Ч, 1.0F) * field_6Л / field_6诶);
            targetVisibility = crystalType4Visibility;
            lastCrystalType4UpdateMillis = var5;
         } else {
            lastCrystalType4UpdateMillis = 0L;
            crystalType4Visibility = 0.0F;
            crystalType4HurtBlend = 0.0F;
         }

         if (fadingTarget != null && !(targetVisibility <= (var11 ? field_61 : field_6Ь))) {
            effectElapsedSeconds += var7;
            float var13 = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            Vec3 var14 = new Vec3(
               Mth.lerp(var13, fadingTarget.xo, fadingTarget.getX()),
               Mth.lerp(var13, fadingTarget.yo, fadingTarget.getY()),
               Mth.lerp(var13, fadingTarget.zo, fadingTarget.getZ())
            );
            float var15 = fadingTarget.hurtDuration <= 0 ? 0.0F : Mth.clamp((float)fadingTarget.hurtTime / fadingTarget.hurtDuration, 0.0F, 1.0F);
            int var16 = hurtRecolorEnabled ? ColorMath.lerpArgb(primaryColor, field_6衣, var15) : primaryColor;
            int var17 = hurtRecolorEnabled ? ColorMath.lerpArgb(secondaryColor, field_6ш, var15) : secondaryColor;
            float var18 = var11 ? targetVisibility : smoothstep(targetVisibility);
            TargetLighting.beginLightCollection(lightingEnabled && (style.equals(LIGHT_COLLECTION_GHOST_STYLE_LABEL) || style.equals(LIGHT_COLLECTION_CRYSTAL_STYLE_LABEL) || style.equals(LIGHT_COLLECTION_LIGHTNING_STYLE_LABEL)), event.getPoses().last().pose());

            try {
               switch (style) {
                  case RING_RENDER_STYLE_LABEL:
                     if (RING_RENDER_TYPE3_LABEL.equals(ringType)) {
                        float var29 = var8 ? easeOutCubic(targetVisibility) : 1.0F - (1.0F - targetVisibility) * (1.0F - targetVisibility);
                        renderRingType3(event, var14, fadingTarget, getHurtColor(fadingTarget, primaryColor, var7, var5), var29);
                     } else if (RING_RENDER_TYPE2_LABEL.equals(ringType)) {
                        renderRingType2(event, var14, fadingTarget, primaryColor, secondaryColor, var18, var13);
                     } else {
                        renderRingType1(event, var14, fadingTarget, var16, var18);
                     }
                     break;
                  case HOOP_RENDER_STYLE_LABEL:
                     renderHoop(event, var14, fadingTarget, var16, var18);
                     break;
                  case GHOST_RENDER_STYLE_LABEL:
                     if (!previousGhostType.equals(ghostType)) {
                        clearGhostTrails();
                        fadingOrbitParticles.clear();
                        orbitEmissionAngle = 0.0;
                        orbitTrail.clear();
                        lastOrbitTrailSampleMillis = 0L;
                        orbitTrailEntityId = field_6ч;
                        resetGhostPulse();
                        previousGhostType = ghostType;
                     }

                     switch (ghostType) {
                        case GHOST_RENDER_TYPE2_LABEL:
                           renderGhostType2(event, var14, fadingTarget, var16, var17, var18);
                           return;
                        case GHOST_RENDER_TYPE3_LABEL:
                           renderGhostType3(event, var14, fadingTarget, var16, var17, var18);
                           return;
                        case GHOST_RENDER_TYPE4_LABEL:
                           renderGhostType4(event, var14, fadingTarget, var16, var17, var8);
                           return;
                        case GHOST_RENDER_TYPE5_LABEL:
                           renderGhostType5(event, var14, fadingTarget, var16, var17, targetVisibility, var13);
                           return;
                        case GHOST_RENDER_TYPE6_LABEL:
                           renderGhostType6(event, var14, fadingTarget, var16, var18);
                           return;
                        case GHOST_RENDER_TYPE7_LABEL:
                           renderGhostType7(event, var14, fadingTarget, var16, var18);
                           return;
                        case GHOST_RENDER_TYPE8_LABEL:
                           renderGhostType8(event, var14, fadingTarget, var16, var18, var7, var8);
                           return;
                        default:
                           renderGhostType1(event, var14, var16, var18);
                           return;
                     }
                  case CRYSTAL_RENDER_STYLE_LABEL:
                     if (!previousCrystalType.equals(crystalType)) {
                        crystalType1Offsets.clear();
                        crystalType1EntityId = field_6Щ;
                        crystalType2Offsets.clear();
                        crystalType2EntityId = field_6у;
                        previousCrystalType = crystalType;
                     }

                     switch (crystalType) {
                        case CRYSTAL_RENDER_TYPE2_LABEL:
                           renderCrystalType2(event, var14, fadingTarget, var16, var18, var5);
                           return;
                        case CRYSTAL_RENDER_TYPE3_LABEL:
                           renderCrystalType3(event, var14, fadingTarget, var16, var18, var5);
                           return;
                        case CRYSTAL_RENDER_TYPE4_LABEL:
                           renderCrystalType4(event, var14, fadingTarget, primaryColor, var18, var12, var5);
                           return;
                        default:
                           renderCrystalType1(event, var14, fadingTarget, var16, secondaryColor, var18, var5);
                           return;
                     }
                  case RHOMBS_RENDER_STYLE_LABEL:
                     renderRhombs(event, var14, fadingTarget, var16, secondaryColor, var18, var5);
                     break;
                  case LIGHTNING_RENDER_STYLE_LABEL:
                     try {
                        if (!previousLightningType.equals(lightningType)) {
                           if (PREVIOUS_LIGHTNING_TYPE3_VARIANT_RESET_LABEL.equals(previousLightningType)) {
                              LightningTargetEspRenderer.н2();
                           }

                           lightningEffects.clear();
                           animatedArcs.clear();
                           rotatingPolylines.clear();
                           lastLightningSpawnMillis = 0L;
                           lastArcSpawnMillis = 0L;
                           lastPolylineSpawnMillis = 0L;
                           previousLightningType = lightningType;
                        }

                        if (LIGHTNING_RENDER_TYPE1_LABEL.equals(lightningType)) {
                           renderLightningType1(event, var14, fadingTarget, var16, var18, var5, var8, var15);
                        } else if (LIGHTNING_RENDER_TYPE3_LABEL.equals(lightningType)) {
                           LightningTargetEspRenderer.method_1(MINECRAFT_CLIENT.gameRenderer.mainCamera(), var13, fadingTarget, var18, primaryColor, hurtRecolorEnabled, lightningThroughWalls ? LIGHTNING_THROUGH_WALLS_OCCLUSION_NEVER_LABEL : LIGHTNING_DEPTH_OCCLUSION_AUTO_LABEL);
                        } else {
                           renderLightningType2(event, var14, fadingTarget, var16, var18, var5, var8);
                        }
                     } catch (Throwable var26) {
                        var26.printStackTrace();
                        lightningEffects.clear();
                        animatedArcs.clear();
                        rotatingPolylines.clear();
                        lastLightningSpawnMillis = 0L;
                        lastArcSpawnMillis = 0L;
                        lastPolylineSpawnMillis = 0L;
                     }
                     break;
                  case CUBE_RENDER_STYLE_LABEL:
                     renderCube(event, var14, fadingTarget, var16, targetVisibility, var7, var8, var15);
                     break;
                  case SQUARE_RENDER_STYLE_LABEL:
                     renderSquare(event, var14, fadingTarget, var16, var18);
                     break;
                  case TRIANGLE_RENDER_STYLE_LABEL:
                     renderTriangle(event, var14, fadingTarget, var16, var18, var5);
                     break;
                  case PENTAGRAM_RENDER_STYLE_LABEL:
                     renderPentagram(event, var14, fadingTarget, var16, var18, var15);
               }
            } finally {
               TargetLighting.renderSurfaceLighting(event.getProjectionMatrix());
            }
         } else {
            if (!var8) {
               fadingTarget = null;
            }

            clearGhostTrails();
            fadingOrbitParticles.clear();
            orbitTrail.clear();
         }
      }
   }

   private static void renderSquare(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      boolean alternate = SQUARE_ALTERNATE_TYPE2_LABEL.equals(squareType);
      float hurtProgress = target.hurtDuration <= 0 ? 0.0F : Mth.clamp((float)target.hurtTime / target.hurtDuration, 0.0F, 1.0F);
      float size = Mth.lerpInt(targetVisibility, 2, 1) * (alternate ? Iк : Iг);
      float rotationDegrees = (float)Math.sin(effectElapsedSeconds * I0) * Iх;
      int effectColor = color;
      if (alternate) {
         int red = Mth.clamp((int)(ColorMath.getRed(color) * (1.0F - hurtProgress) + IУ * hurtProgress), 0, 255);
         int green = Mth.clamp((int)(ColorMath.getGreen(color) * (1.0F - hurtProgress)), 0, 255);
         int blue = Mth.clamp((int)(ColorMath.getBlue(color) * (1.0F - hurtProgress)), 0, 255);
         effectColor = ColorMath.packArgb(red, green, blue, ColorMath.getAlpha(color));
      }

      BufferBuilder buffer = beginQuads();
      PoseStack poses = event.getPoses();
      poses.pushPose();
      poses.translate(targetPosition.x, targetPosition.y + target.getBbHeight() * IЗ, targetPosition.z);
      poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
      poses.mulPose(Axis.ZP.rotationDegrees(rotationDegrees));
      appendCenteredBillboard(buffer, poses.last().pose(), size, multiplyAlpha(effectColor, alpha));
      poses.popPose();
      drawTexturedGeometry(buffer, alternate ? TARGET_ALTERNATE_TEXTURE : TARGET_TEXTURE, true);
   }

   private static void renderLightningType1(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis, boolean active, float hurtProgress) {
      if (active && nowMillis - lastLightningSpawnMillis >= мк && lightningEffects.size() < 5) {
         lightningEffects.add(createLightningEffect(target, targetPosition, nowMillis));
         if (EFFECT_RANDOM.nextFloat() < мг && lightningEffects.size() < 5) {
            lightningEffects.add(createLightningEffect(target, targetPosition, nowMillis + м0));
         }

         lastLightningSpawnMillis = nowMillis;
      }

      Iterator effectIterator = lightningEffects.iterator();

      while (effectIterator.hasNext()) {
         TargetEspMask.LightningEffect expiredEffect = (TargetEspMask.LightningEffect)effectIterator.next();
         if (nowMillis - expiredEffect.createdMillis > expiredEffect.durationMillis) {
            effectIterator.remove();
         }
      }

      if (!lightningEffects.isEmpty()) {
         int glowColor = brightenColor(color, мх);
         int coreColor = brightenColor(color, мУ);
         BufferBuilder glowBuffer = beginQuads();
         BufferBuilder coreBuffer = beginQuads();
         Matrix4f pose = event.getPoses().last().pose();
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();

         for (TargetEspMask.LightningEffect effect : lightningEffects) {
            float progress = (float)(nowMillis - effect.createdMillis) / (float)effect.durationMillis;
            if (!(progress < 0.0F)) {
               float growth = easeOutCubic(Mth.clamp(progress / мЗ, 0.0F, 1.0F));
               float fade = 1.0F - smoothstep(Mth.clamp((progress - мЕ) / мП, 0.0F, 1.0F));
               float flash = 1.0F - smoothstep(Mth.clamp(progress / м西, 0.0F, 1.0F));
               float flicker = мд + мБ * (float)Math.sin(nowMillis * мС + effect.seed);
               float effectAlpha = alpha * fade * flicker;
               if (!(effectAlpha <= мз)) {
                  Vec3 targetDisplacement = targetPosition.subtract(effect.origin);

                  for (TargetEspMask.LightningBranch branch : effect.branches) {
                     float branchGrowth = Mth.clamp((growth - branch.growthDelay) / Math.max(мц, 1.0F - branch.growthDelay), 0.0F, 1.0F);
                     if (!(branchGrowth <= мя)) {
                        ArrayList points = new ArrayList(branch.points.size());

                        for (Vec3 point : branch.points) {
                           points.add(point.add(targetDisplacement).subtract(cameraPosition));
                        }

                        float branchAlpha = effectAlpha * (Кс + branch.widthScale * КВ);

                        for (int var36 = 1; var36 <= 2; var36++) {
                           float var31 = (points.size() - 1) * branchGrowth * var36 * КЯ;
                           int var32 = Math.min((int)var31, points.size() - 1);
                           Vec3 lightPosition = ((Vec3)points.get(var32)).lerp((Vec3)points.get(Math.min(var32 + 1, points.size() - 1)), var31 - var32);
                           TargetLighting.addLight((float)lightPosition.x, (float)lightPosition.y, (float)lightPosition.z, КХ * branch.widthScale, multiplyAlpha(color, branchAlpha));
                        }

                        appendGrowingLightningTube(glowBuffer, pose, points, branchGrowth, branch.widthScale * Ке, multiplyAlpha(glowColor, branchAlpha * (КА + flash * К4)), effect.seed, 0.0F);
                        appendGrowingLightningTube(coreBuffer, pose, points, branchGrowth, branch.widthScale * Кп, multiplyAlpha(coreColor, Math.min(1.0F, branchAlpha * (1.0F + flash))), effect.seed + КД, 0.0F);
                        appendGrowingLightningTube(glowBuffer, pose, points, branchGrowth, branch.widthScale * КЪ, multiplyAlpha(-1, Math.min(1.0F, branchAlpha * (Кт + flash * Ка))), effect.seed + КЮ, 0.0F);
                     }
                  }
               }
            }
         }

         drawLightningGeometry(glowBuffer, coreBuffer, LIGHTNING_PIPELINE, color);
      }
   }

   private static void renderHoop(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      float visibility = easeOutCubic(alpha);
      float height = target.getBbHeight();
      float sizeScale = Mth.lerp(visibility, О5, 1.0F);
      float radius = Math.max(Ов, target.getBbWidth() * Оъ + ОМ) * sizeScale;
      double orbitPhase = effectElapsedSeconds * ОН + target.getId() * Он;
      double tiltAngle = Math.toRadians(ОЦ);
      Vec3 normal = new Vec3(Math.sin(tiltAngle) * Math.cos(orbitPhase), Math.cos(tiltAngle), Math.sin(tiltAngle) * Math.sin(orbitPhase));
      Vec3[] basis = createPerpendicularBasis(normal);
      Vec3 center = targetPosition.add(0.0, height * Оч, 0.0);
      Matrix4f pose = event.getPoses().last().pose();
      BufferBuilder hoopBuffer = beginQuads();
      appendOrientedBillboard(hoopBuffer, pose, center, basis[0], basis[1], radius * ОЭ, multiplyAlpha(color, visibility * О必));
      appendOrientedBillboard(hoopBuffer, pose, center, basis[0], basis[1], radius * Ощ, multiplyAlpha(brightenColor(color, ОГ), visibility * Ол));
      drawColorGeometry(hoopBuffer, HOOP_PIPELINE);
      BufferBuilder glowBuffer = beginQuads();
      double particlePhase = effectElapsedSeconds * Оэ;

      for (int var21 = 0; var21 < 13; var21++) {
         float var22 = 1.0F - var21 / Ою;
         double var23 = particlePhase - var21 * ОЩ;
         Vec3 particlePosition = center.add(basis[0].scale(Math.cos(var23) * radius)).add(basis[1].scale(Math.sin(var23) * radius));
         float particleAlpha = visibility * var22 * var22 * Оу;
         float particleSize = О2 + var22 * Оф;
         appendCameraFacingBillboard(glowBuffer, pose, particlePosition.x, particlePosition.y, particlePosition.z, particleSize, multiplyAlpha(brightenColor(color, Ои), particleAlpha));
      }

      drawTexturedGeometry(glowBuffer, WORLD_BLOOM_TEXTURE, true);
   }

   private static int hashNoiseByte(int index) {
      int var1 = index * мЯ + мХ;
      var1 = (var1 ^ var1 >> 13) * ме;
      return var1 & 0xFF;
   }

   public static void setGhostOptions(boolean bloomEnabled, boolean throughWalls) {
      ghostBloomEnabled = bloomEnabled;
      ghostThroughWalls = throughWalls;
   }

   private static int normalizeAdditiveColor(int color) {
      float alpha = ColorMath.getAlpha(color) / гь;
      float premultipliedRed = ColorMath.getRed(color) * alpha;
      float premultipliedGreen = ColorMath.getGreen(color) * alpha;
      float premultipliedBlue = ColorMath.getBlue(color) * alpha;
      float halfMaxPremultipliedChannel = Math.max(premultipliedRed, Math.max(premultipliedGreen, premultipliedBlue)) * гж;
      if (halfMaxPremultipliedChannel <= 0.0F) {
         return 0;
      }

      int outputAlpha = Mth.clamp(Math.round(halfMaxPremultipliedChannel), 1, 255);
      float scale = г弟 / (outputAlpha * 2.0F);
      return ColorMath.packArgb(
         Mth.clamp(Math.round(premultipliedRed * scale), 0, 255), Mth.clamp(Math.round(premultipliedGreen * scale), 0, 255), Mth.clamp(Math.round(premultipliedBlue * scale), 0, 255), outputAlpha
      );
   }

   public static void setStyleOptions(
      float requestedSpeed, String requestedRingType, int requestedGhostSegments, boolean requestedHurtRecolor, String requestedGhostType, String requestedCrystalType, boolean requestedCrystalThroughWalls, String requestedLightningType, boolean requestedLightningThroughWalls, boolean requestedLightingEnabled, String requestedSquareType
   ) {
      effectSpeed = Mth.clamp(requestedSpeed, рК, рI);
      ringType = requestedRingType != null && !requestedRingType.isEmpty() ? requestedRingType : REQUESTED_RING_TYPE_FALLBACK_LABEL;
      squareType = requestedSquareType != null && !requestedSquareType.isEmpty() ? requestedSquareType : REQUESTED_SQUARE_TYPE_FALLBACK_LABEL;
      ghostSegments = Mth.clamp(requestedGhostSegments, 2, 4);
      hurtRecolorEnabled = requestedHurtRecolor;
      ghostType = requestedGhostType != null && !requestedGhostType.isEmpty() ? requestedGhostType : REQUESTED_GHOST_TYPE_FALLBACK_LABEL;
      crystalType = requestedCrystalType != null && !requestedCrystalType.isEmpty() ? requestedCrystalType : REQUESTED_CRYSTAL_TYPE_FALLBACK_LABEL;
      crystalThroughWalls = requestedCrystalThroughWalls;
      lightningType = requestedLightningType != null && !requestedLightningType.isEmpty() ? requestedLightningType : REQUESTED_LIGHTNING_TYPE_FALLBACK_LABEL;
      lightningThroughWalls = requestedLightningThroughWalls;
      lightingEnabled = requestedLightingEnabled;
   }

   private static int getTriangleTileColor(float flash, int index, int color) {
      float var3 = Mth.clamp((Тч - flash) / ТЭ, 0.0F, 1.0F);
      int var4 = ColorMath.blendArgb(color, -1, Т必);
      int var5 = ColorMath.blendArgb(var4, color, var3);
      float var6 = (hashColorUnitFloat(index) - Тщ) * ТГ;
      return ColorMath.blendArgb(var5, var6 >= 0.0F ? -1 : color, Math.abs(var6));
   }

   private static void drawTriangleLines(BufferBuilder buffer, RenderPipeline pipeline) {
      MeshData mesh = buffer.build();
      if (mesh != null) {
         GpuBuffer vertexBuffer = null;

         try {
            DrawState drawState = mesh.drawState();
            int indexCount = drawState.indexCount();
            AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
            GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
            IndexType indexType = sequentialIndices.type();
            vertexBuffer = RenderSystem.getDevice().createBuffer(() -> TRIANGLE_LINES_VERTEX_BUFFER_DEBUG_LABEL, 32, mesh.vertexBuffer());
            RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass pass = encoder.createRenderPass(
                  () -> TARGET_ESP_TRIANGLE_LINES_PASS_DEBUG_LABEL, mainTarget.getColorTextureView(), Optional.empty(), mainTarget.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(pass);
               pass.setPipeline(pipeline);
               pass.setVertexBuffer(0, vertexBuffer.slice());
               pass.setIndexBuffer(indexBuffer, indexType);
               pass.drawIndexed(indexCount, 1, 0, 0, 0);
            }
         } finally {
            if (vertexBuffer != null) {
               vertexBuffer.close();
            }

            mesh.close();
         }
      }
   }

   private static float noise2d(float x, float y) {
      int cellX = (int)Math.floor(x) & 0xFF;
      int cellY = (int)Math.floor(y) & 0xFF;
      float fractionX = x - (float)Math.floor(x);
      float fractionY = y - (float)Math.floor(y);
      float smoothX = quinticSmoothstep(fractionX);
      float smoothY = quinticSmoothstep(fractionY);
      int lowerLeftHash = hashNoiseByte(cellX) + cellY;
      int upperLeftHash = hashNoiseByte(cellX) + cellY + 1;
      int lowerRightHash = hashNoiseByte(cellX + 1) + cellY;
      int upperRightHash = hashNoiseByte(cellX + 1) + cellY + 1;
      float lowerNoise = Mth.lerp(smoothX, gradientNoise2d(hashNoiseByte(lowerLeftHash), fractionX, fractionY), gradientNoise2d(hashNoiseByte(lowerRightHash), fractionX - 1.0F, fractionY));
      float upperNoise = Mth.lerp(smoothX, gradientNoise2d(hashNoiseByte(upperLeftHash), fractionX, fractionY - 1.0F), gradientNoise2d(hashNoiseByte(upperRightHash), fractionX - 1.0F, fractionY - 1.0F));
      return Mth.clamp(Mth.lerp(smoothY, lowerNoise, upperNoise) * мс + мВ, 0.0F, 1.0F);
   }

   public static void renderFadingTarget(WorldEffectsEvent event) {
      if (fadingTarget != null && targetVisibility > рЗ) {
         render(event, null, previousStyle, previousPrimaryColor, previousSecondaryColor);
      }
   }

   private static void initializeCrystalType2Offsets(LivingEntity target) {
      float radius = target.getBbWidth() * Й0;
      float height = target.getBbHeight();
      if (crystalType2EntityId != target.getId() || crystalType2Radius != radius || crystalType2Height != height) {
         crystalType2EntityId = target.getId();
         crystalType2Radius = radius;
         crystalType2Height = height;
         crystalType2Offsets.clear();
         Random random = new Random(target.getId() * Йх);
         int sampleCount = (int)(radius + height * ЙУ);

         for (int var5 = 0; var5 < sampleCount; var5++) {
            Vector3f bestSample = null;
            float bestDistanceSquared = ЙЗ;

            for (int var8 = 0; var8 < 15; var8++) {
               float angleDegrees = random.nextFloat() * ЙЕ;
               float sampleHeight = random.nextFloat() * height;
               float angleRadians = (float)Math.toRadians(angleDegrees);
               float sampleX = (float)Math.sin(angleRadians) * radius;
               float sampleZ = (float)Math.cos(angleRadians) * radius;
               if (crystalType2Offsets.isEmpty()) {
                  bestSample = new Vector3f(sampleX, sampleHeight, sampleZ);
                  break;
               }

               float nearestDistanceSquared = ЙП;

               for (Vector3f existingSample : crystalType2Offsets) {
                  float var17 = existingSample.x - sampleX;
                  float var18 = existingSample.y - sampleHeight;
                  float var19 = existingSample.z - sampleZ;
                  nearestDistanceSquared = Math.min(nearestDistanceSquared, var17 * var17 + var18 * var18 + var19 * var19);
               }

               if (nearestDistanceSquared > bestDistanceSquared) {
                  bestDistanceSquared = nearestDistanceSquared;
                  bestSample = new Vector3f(sampleX, sampleHeight, sampleZ);
               }
            }

            if (bestSample != null) {
               crystalType2Offsets.add(bestSample);
            }
         }
      }
   }

   private static Vec3 randomPerpendicular(Vec3 direction) {
      Vec3 normal = direction.normalize();
      Vec3 reference = Math.abs(normal.y) < мО ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
      Vec3 basisX = normal.cross(reference).normalize();
      Vec3 basisY = normal.cross(basisX).normalize();
      double angle = EFFECT_RANDOM.nextDouble() * мФ * мЙ;
      return basisX.scale(Math.cos(angle)).add(basisY.scale(Math.sin(angle)));
   }

   private static void drawMaskGeometry(BufferBuilder buffer, RenderPipeline pipeline, RenderTarget target, boolean clearColor) {
      MeshData mesh = buffer.build();
      if (mesh != null) {
         GpuBuffer vertexBuffer = null;

         try {
            DrawState drawState = mesh.drawState();
            int indexCount = drawState.indexCount();
            AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
            GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
            IndexType indexType = sequentialIndices.type();
            vertexBuffer = RenderSystem.getDevice().createBuffer(() -> MASK_VERTEX_BUFFER_DEBUG_LABEL, 32, mesh.vertexBuffer());
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass pass = encoder.createRenderPass(
                  () -> TARGET_ESP_MASK_PASS_DEBUG_LABEL,
                  target.getColorTextureView(),
                  clearColor ? Optional.of(new Vector4f(0.0F)) : Optional.empty(),
                  target.getDepthTextureView(),
                  OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(pass);
               pass.setPipeline(pipeline);
               pass.setVertexBuffer(0, vertexBuffer.slice());
               pass.setIndexBuffer(indexBuffer, indexType);
               pass.drawIndexed(indexCount, 1, 0, 0, 0);
            }
         } finally {
            if (vertexBuffer != null) {
               vertexBuffer.close();
            }

            mesh.close();
         }
      }
   }

   private static void appendTexturedVertex(BufferBuilder buffer, Matrix4f pose, float x, float y, float z, float u, float v, Vector4f color) {
      buffer.addVertex(pose, x, y, z).setUv(u, v).setColor(color.x, color.y, color.z, color.w);
   }

   private static double getCubeCornerHeight(double startY, double groundY, float progress, int xSign, int ySign, int zSign) {
      double gravity = Шд + (xSign > 0 ? ШБ : 0.0) + (zSign > 0 ? ШС : 0.0);
      double initialVelocity = Шз + (ySign > 0 ? Шц : 0.0);
      double height = startY + initialVelocity * progress - gravity * progress * progress;
      if (height >= groundY) {
         return height;
      }

      double estimatedImpactProgress = Mth.clamp(Math.sqrt(Math.max(0.0, (startY - groundY) / gravity)), 0.0, Шя);
      double bounceProgress = Mth.clamp((progress - estimatedImpactProgress) / Math.max(ыс, 1.0 - estimatedImpactProgress), 0.0, 1.0);
      double bounceAmplitude = ыВ + (xSign * zSign > 0 ? ыЯ : 0.0);
      double bounce = Math.abs(Math.sin(bounceProgress * ыХ * ые)) * bounceAmplitude * (1.0 - bounceProgress) * (1.0 - bounceProgress);
      return groundY + bounce;
   }

   private static List<Vec3> smoothPath(List<Vec3> points, int subdivisions) {
      if (points.size() >= 3 && subdivisions > 1) {
         ArrayList smoothedPoints = new ArrayList((points.size() - 1) * subdivisions + 1);

         for (int var3 = 0; var3 < points.size() - 1; var3++) {
            Vec3 previous = (Vec3)points.get(Math.max(0, var3 - 1));
            Vec3 start = (Vec3)points.get(var3);
            Vec3 end = (Vec3)points.get(var3 + 1);
            Vec3 next = (Vec3)points.get(Math.min(points.size() - 1, var3 + 2));

            for (int var8 = 0; var8 < subdivisions; var8++) {
               double progress = (double)var8 / subdivisions;
               smoothedPoints.add(catmullRom(previous, start, end, next, progress));
            }
         }

         smoothedPoints.add((Vec3)points.get(points.size() - 1));
         return smoothedPoints;
      } else {
         return new ArrayList<>(points);
      }
   }

   private static void renderCrystalType2(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      initializeCrystalType2Offsets(target);
      float rotationDegrees = (float)(nowMillis % Йю) / ЙЩ;
      float timePhase = (float)nowMillis * Йу;
      float radius = target.getBbWidth() * Й2;
      float height = target.getBbHeight();
      float radialScale = Йф - Йи * alpha;
      float sine = (float)Math.sin(Math.toRadians(rotationDegrees));
      float cosine = (float)Math.cos(Math.toRadians(rotationDegrees));
      int effectColor = multiplyAlpha(color, alpha);
      PoseStack poses = event.getPoses();
      BufferBuilder buffer = beginQuads();

      for (int var17 = 0; var17 < crystalType2Offsets.size(); var17++) {
         Vector3f offset = crystalType2Offsets.get(var17);
         float rotatedX = offset.x * cosine - offset.z * sine;
         float rotatedZ = offset.x * sine + offset.z * cosine;
         float offsetX = rotatedX * radialScale;
         float offsetZ = rotatedZ * radialScale;
         float offsetY = offset.y + (float)(Й9 * Math.sin(timePhase * 2.0F + var17 * 1337));
         poses.pushPose();
         poses.translate(targetPosition.x, targetPosition.y, targetPosition.z);
         appendFacingCrystal(buffer, poses, offsetX, offsetY, offsetZ, Йо, effectColor, height, rotationDegrees + var17 * ЙO);
         poses.popPose();
         appendCameraFacingBillboard(buffer, poses, targetPosition.x + offsetX, targetPosition.y + offsetY, targetPosition.z + offsetZ, Й7, multiplyAlpha(color, alpha * ЙР));
         appendCameraFacingBillboard(buffer, poses, targetPosition.x + offsetX, targetPosition.y + offsetY, targetPosition.z + offsetZ, Й3, multiplyAlpha(color, alpha * Йр));
      }

      drawTexturedGeometry(buffer, WORLD_BLOOM_TEXTURE, true, getCrystalGlowPipeline());
   }

   private static float easeOutCubic(float value) {
      float var1 = 1.0F - Mth.clamp(value, 0.0F, 1.0F);
      return 1.0F - var1 * var1 * var1;
   }

   private static void appendFacingRhomb(
      BufferBuilder buffer, PoseStack poses, float x, float y, float z, float size, int primaryColor, int secondaryColor, float spinDegrees, int index, float targetHeight, boolean brighten
   ) {
      poses.pushPose();
      poses.translate(x, y, z);
      float centerHeight = targetHeight * кЯ;
      float directionX = -x;
      float directionY = centerHeight - y;
      float directionZ = -z;
      float directionLengthSquared = directionX * directionX + directionY * directionY + directionZ * directionZ;
      if (directionLengthSquared > кХ) {
         float inverseLength = 1.0F / (float)Math.sqrt(directionLengthSquared);
         float verticalDirection = Mth.clamp(directionY * inverseLength, ке, 1.0F);
         float yawDegrees = (float)Math.toDegrees(Math.atan2(directionX, directionZ));
         float pitchDegrees = -((float)Math.toDegrees(Math.asin(verticalDirection)));
         poses.mulPose(Axis.YP.rotationDegrees(yawDegrees));
         poses.mulPose(Axis.XP.rotationDegrees(pitchDegrees));
      }

      poses.mulPose(Axis.ZP.rotationDegrees(spinDegrees));
      poses.scale(size * кА, size * к4, size * кп);
      Matrix4f pose = poses.last().pose();
      float var30 = кД + hashRhombUnitFloat(index, кЪ) * кт;
      float var31 = ка * var30;
      float var32 = кЮ * (кЧ + hashRhombUnitFloat(index, кЛ) * к诶);
      float var21 = к1 * (кЬ + hashRhombUnitFloat(index, к衣) * кш);
      float var22 = (hashRhombUnitFloat(index, кИ) - кь) * кж;
      float var23 = (hashRhombUnitFloat(index, к弟) - кб) * кЫ;
      int var24 = ColorMath.getAlpha(primaryColor);
      int var25 = ColorMath.packArgb(ColorMath.getRed(secondaryColor), ColorMath.getGreen(secondaryColor), ColorMath.getBlue(secondaryColor), var24);
      int var26 = brighten ? brightenColor(primaryColor, кl) : primaryColor;
      int var27 = brighten ? brightenColor(var25, к8) : var25;
      int var28 = brighten ? brightenColor(primaryColor, к5) : primaryColor;
      appendColoredTriangleQuad(buffer, pose, var22, var23, var32, -var31, 0.0F, 0.0F, 0.0F, var31, 0.0F, var26, var28, var28);
      appendColoredTriangleQuad(buffer, pose, var22, var23, var32, 0.0F, var31, 0.0F, var31, 0.0F, 0.0F, var26, var28, var28);
      appendColoredTriangleQuad(buffer, pose, var22, var23, var32, var31, 0.0F, 0.0F, 0.0F, -var31, 0.0F, var26, var28, var28);
      appendColoredTriangleQuad(buffer, pose, var22, var23, var32, 0.0F, -var31, 0.0F, -var31, 0.0F, 0.0F, var26, var28, var28);
      appendColoredTriangleQuad(buffer, pose, -var22 * кв, -var23 * къ, -var21, 0.0F, var31, 0.0F, -var31, 0.0F, 0.0F, var27, var28, var28);
      appendColoredTriangleQuad(buffer, pose, -var22 * кМ, -var23 * кН, -var21, var31, 0.0F, 0.0F, 0.0F, var31, 0.0F, var27, var28, var28);
      appendColoredTriangleQuad(buffer, pose, -var22 * кн, -var23 * кЦ, -var21, 0.0F, -var31, 0.0F, var31, 0.0F, 0.0F, var27, var28, var28);
      appendColoredTriangleQuad(buffer, pose, -var22 * кч, -var23 * кЭ, -var21, -var31, 0.0F, 0.0F, 0.0F, -var31, 0.0F, var27, var28, var28);
      poses.popPose();
   }

   private static void appendCylinder(BufferBuilder buffer, Matrix4f pose, Vec3 origin, Vec3 cameraPosition, Vec3 start, Vec3 end, double radius, int sides, int color) {
      Vec3 direction = end.subtract(start).normalize();
      Vec3 reference = Math.abs(direction.y) < ыА ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
      Vec3 basisX = direction.cross(reference).normalize().scale(radius);
      Vec3 basisY = direction.cross(basisX).normalize().scale(radius);

      for (int var14 = 0; var14 < sides; var14++) {
         double angleStart = ы4 * var14 / sides;
         double angleEnd = ып * (var14 + 1) / sides;
         Vec3 radialStart = basisX.scale(Math.cos(angleStart)).add(basisY.scale(Math.sin(angleStart)));
         Vec3 radialEnd = basisX.scale(Math.cos(angleEnd)).add(basisY.scale(Math.sin(angleEnd)));
         appendTexturedQuad(buffer, pose, toCameraRelative(start.add(radialStart), origin, cameraPosition), toCameraRelative(start.add(radialEnd), origin, cameraPosition), toCameraRelative(end.add(radialEnd), origin, cameraPosition), toCameraRelative(end.add(radialStart), origin, cameraPosition), color);
      }
   }

   public static boolean isCubeCompositePending() {
      return cubeCompositePending;
   }

   private static void drawLightningGeometry(BufferBuilder glowBuffer, BufferBuilder coreBuffer) {
      drawLightningGeometry(glowBuffer, coreBuffer, LIGHTNING_PIPELINE, -1);
   }

   private static void drawTexturedGeometry(BufferBuilder buffer, Identifier texture, boolean submitDistortion) {
      drawTexturedGeometry(buffer, texture, submitDistortion, TARGET_PIPELINE);
   }

   private static void renderTriangle(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, long nowMillis) {
      targetPosition = targetPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
      float verticalBob = (float)Math.sin(effectElapsedSeconds * IЕ) * IП;
      float sizePulse = 1.0F + I西 * (float)Math.sin(effectElapsedSeconds * 2.0F);
      float size = (Iд + target.getBbWidth() * IБ) * alpha * easeOutBack(targetVisibility) * sizePulse;
      if (!(size <= IС)) {
         double x = targetPosition.x;
         double y = targetPosition.y + target.getBbHeight() + Iз + verticalBob;
         double z = targetPosition.z;
         float yawDegrees = -MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         PoseStack poses = event.getPoses();
         BufferBuilder glowBuffer = beginQuads();
         poses.pushPose();
         poses.translate(x, y, z);
         poses.mulPose(Axis.YP.rotationDegrees(yawDegrees));
         int glowColor = multiplyAlpha(ColorMath.blendArgb(color, -1, Iц), Iя * alpha);
         appendCenteredBillboard(glowBuffer, poses.last().pose(), size * Тс, glowColor);
         poses.popPose();
         drawTexturedGeometry(glowBuffer, TARGET_GLOW_TEXTURE, true);
         poses.pushPose();
         poses.translate(x, y, z);
         poses.mulPose(Axis.YP.rotationDegrees(yawDegrees));
         poses.scale(size, size, size);
         appendAnimatedTriangleTiles(poses, color, alpha, nowMillis);
         poses.popPose();
      }
   }

   private static void appendRotatingRhomb(
      BufferBuilder buffer,
      PoseStack poses,
      float x,
      float y,
      float z,
      float size,
      int primaryColor,
      int secondaryColor,
      float spinDegrees,
      boolean alternateColors,
      int index,
      boolean reverseColors
   ) {
      poses.pushPose();
      poses.translate(x, y, z);
      poses.mulPose(Axis.XP.rotationDegrees(spinDegrees * к必 + кщ));
      poses.mulPose(Axis.YP.rotationDegrees(spinDegrees * кГ + кл));
      poses.mulPose(Axis.ZP.rotationDegrees(spinDegrees + кэ));
      poses.scale(size * кю, size * кЩ, size * ку);
      Matrix4f pose = poses.last().pose();
      float var13 = к2 * (кф + hashRhombUnitFloat(index, ки) * к9);
      float var14 = ко * (кO + hashRhombUnitFloat(index, к7) * кР);
      float var15 = к3 * (кр + hashRhombUnitFloat(index, к6) * к_);
      float var16 = (hashRhombUnitFloat(index, кШ) - кы) * кй;
      float var17 = (hashRhombUnitFloat(index, кЖ) - км) * кК;
      int var18 = alternateColors ? brightenColor(primaryColor, кI) : primaryColor;
      int var19 = primaryColor;
      int var20 = alternateColors
         ? ColorMath.packArgb(ColorMath.getRed(secondaryColor), ColorMath.getGreen(secondaryColor), ColorMath.getBlue(secondaryColor), ColorMath.getAlpha(primaryColor))
         : primaryColor;
      int var21 = reverseColors ? var20 : var18;
      int var22 = reverseColors ? var18 : var20;
      appendColoredTriangleQuad(buffer, pose, var16, var17, var14, -var13, 0.0F, 0.0F, 0.0F, var13, 0.0F, var21, var19, var19);
      appendColoredTriangleQuad(buffer, pose, var16, var17, var14, 0.0F, var13, 0.0F, var13, 0.0F, 0.0F, var21, var19, var19);
      appendColoredTriangleQuad(buffer, pose, var16, var17, var14, var13, 0.0F, 0.0F, 0.0F, -var13, 0.0F, var21, var19, var19);
      appendColoredTriangleQuad(buffer, pose, var16, var17, var14, 0.0F, -var13, 0.0F, -var13, 0.0F, 0.0F, var21, var19, var19);
      appendColoredTriangleQuad(buffer, pose, -var16 * кТ, -var17 * кО, -var15, 0.0F, var13, 0.0F, -var13, 0.0F, 0.0F, var22, var19, var19);
      appendColoredTriangleQuad(buffer, pose, -var16 * кФ, -var17 * кЙ, -var15, var13, 0.0F, 0.0F, 0.0F, var13, 0.0F, var22, var19, var19);
      appendColoredTriangleQuad(buffer, pose, -var16 * кк, -var17 * кг, -var15, 0.0F, -var13, 0.0F, var13, 0.0F, 0.0F, var22, var19, var19);
      appendColoredTriangleQuad(buffer, pose, -var16 * к0, -var17 * кх, -var15, -var13, 0.0F, 0.0F, 0.0F, -var13, 0.0F, var22, var19, var19);
      poses.popPose();
   }

   private static void appendTexturedQuad(BufferBuilder buffer, Matrix4f pose, Vec3 first, Vec3 second, Vec3 third, Vec3 fourth, int color) {
      appendTexturedVertex(buffer, pose, (float)first.x, (float)first.y, (float)first.z, 0.0F, 0.0F, color);
      appendTexturedVertex(buffer, pose, (float)second.x, (float)second.y, (float)second.z, 1.0F, 0.0F, color);
      appendTexturedVertex(buffer, pose, (float)third.x, (float)third.y, (float)third.z, 1.0F, 1.0F, color);
      appendTexturedVertex(buffer, pose, (float)fourth.x, (float)fourth.y, (float)fourth.z, 0.0F, 1.0F, color);
   }

   private static void appendOctahedron(BufferBuilder buffer, PoseStack poses, float size, int color) {
      poses.pushPose();
      poses.scale(size, size, size);
      Matrix4f pose = poses.last().pose();

      for (int var5 = 0; var5 < OCTAHEDRON_FACES.length; var5++) {
         int faceColor = ColorMath.packArgb(
            Math.round(ColorMath.getRed(color) * OCTAHEDRON_FACE_SHADING[var5]),
            Math.round(ColorMath.getGreen(color) * OCTAHEDRON_FACE_SHADING[var5]),
            Math.round(ColorMath.getBlue(color) * OCTAHEDRON_FACE_SHADING[var5]),
            ColorMath.getAlpha(color)
         );
         float[] firstVertex = OCTAHEDRON_VERTICES[OCTAHEDRON_FACES[var5][0]];
         float[] secondVertex = OCTAHEDRON_VERTICES[OCTAHEDRON_FACES[var5][1]];
         float[] thirdVertex = OCTAHEDRON_VERTICES[OCTAHEDRON_FACES[var5][2]];
         Vector4f rgba = ColorMath.toRgbaVector(faceColor);
         appendTexturedVertex(buffer, pose, firstVertex[0], firstVertex[1], firstVertex[2], кя, гс, rgba);
         appendTexturedVertex(buffer, pose, secondVertex[0], secondVertex[1], secondVertex[2], гВ, гЯ, rgba);
         appendTexturedVertex(buffer, pose, thirdVertex[0], thirdVertex[1], thirdVertex[2], гХ, ге, rgba);
      }

      poses.popPose();
   }

   private static float hashRhombUnitFloat(int index, float seed) {
      float var2 = (float)Math.sin(index * кУ + seed) * кЗ;
      return var2 - (float)Math.floor(var2);
   }

   private static void appendGlowBillboard(PoseStack poses, BufferBuilder buffer, double x, double y, double z, float size, float alpha, int color) {
      poses.pushPose();
      poses.translate(x, y, z);
      poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
      Matrix4f pose = poses.last().pose();
      appendCenteredBillboard(buffer, pose, size * йН, multiplyAlpha(color, alpha * йн));
      appendCenteredBillboard(buffer, pose, size, multiplyAlpha(color, alpha));
      poses.popPose();
   }

   private static void subdivideArc(Vec3[] points, int startIndex, int endIndex, double amplitude, int depth, Random random) {
      if (endIndex - startIndex >= 2 && !(amplitude < ЖЦ) && depth <= 3) {
         int midpointIndex = (startIndex + endIndex) / 2;
         Vec3 start = points[startIndex];
         Vec3 end = points[endIndex];
         Vec3 direction = end.subtract(start);
         if (!(direction.lengthSqr() < Жч)) {
            direction = direction.normalize();
            Vec3 basisX = direction.cross(Math.abs(direction.y) < ЖЭ ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0));
            if (basisX.lengthSqr() < Ж必) {
               basisX = new Vec3(0.0, 0.0, 1.0);
            }

            basisX = basisX.normalize();
            Vec3 basisY = direction.cross(basisX).normalize();
            double offsetX = (random.nextDouble() - Жщ) * ЖГ * amplitude;
            double offsetY = (random.nextDouble() - Жл) * Жэ * amplitude * Жю;
            points[midpointIndex] = start.lerp(end, ЖЩ).add(basisX.scale(offsetX)).add(basisY.scale(offsetY));
            subdivideArc(points, startIndex, midpointIndex, amplitude * Жу, depth + 1, random);
            subdivideArc(points, midpointIndex, endIndex, amplitude * Ж2, depth + 1, random);
         }
      }
   }

   private static void emitOrbitTrailSamples(double x, double y, double z, long nowMillis, float alpha) {
      long sampleMillis = System.currentTimeMillis();
      if (sampleMillis - lastOrbitTrailSampleMillis >= й4) {
         lastOrbitTrailSampleMillis = sampleMillis;
         double angle = йп * nowMillis / йД;
         addOrbitTrailSample(x, y, z, angle, 1.0, 1.0, йЪ, йт, alpha);
         addOrbitTrailSample(x, y, z, angle, йа, 1.0, йЮ, йЧ, alpha);
         addOrbitTrailSample(x, y, z, angle, йЛ, й诶, 1.0, й1, alpha);
      }
   }

   private static BufferBuilder beginQuads() {
      return SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
   }

   private static void appendCameraFacingBillboard(BufferBuilder buffer, PoseStack poses, double x, double y, double z, float size, int color) {
      poses.pushPose();
      poses.translate(x, y, z);
      poses.mulPose(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
      appendCenteredBillboard(buffer, poses.last().pose(), size, color);
      poses.popPose();
   }

   private static void appendOrientedBillboard(BufferBuilder buffer, Matrix4f pose, Vec3 center, Vec3 basisX, Vec3 basisY, float size, int color) {
      Vector4f rgba = ColorMath.toRgbaVector(color);
      float halfSize = size * кс;
      Vec3 firstCorner = center.subtract(basisX.scale(halfSize)).subtract(basisY.scale(halfSize));
      Vec3 secondCorner = center.subtract(basisX.scale(halfSize)).add(basisY.scale(halfSize));
      Vec3 thirdCorner = center.add(basisX.scale(halfSize)).add(basisY.scale(halfSize));
      Vec3 fourthCorner = center.add(basisX.scale(halfSize)).subtract(basisY.scale(halfSize));
      appendTexturedVertex(buffer, pose, (float)firstCorner.x, (float)firstCorner.y, (float)firstCorner.z, 0.0F, 0.0F, rgba);
      appendTexturedVertex(buffer, pose, (float)secondCorner.x, (float)secondCorner.y, (float)secondCorner.z, 0.0F, 1.0F, rgba);
      appendTexturedVertex(buffer, pose, (float)thirdCorner.x, (float)thirdCorner.y, (float)thirdCorner.z, 1.0F, 1.0F, rgba);
      appendTexturedVertex(buffer, pose, (float)fourthCorner.x, (float)fourthCorner.y, (float)fourthCorner.z, 1.0F, 0.0F, rgba);
   }

   private static Vec3[] createUprightBasis(Vec3 direction) {
      Vec3 verticalProjection = new Vec3(0.0, 1.0, 0.0).subtract(direction.scale(direction.y));
      if (verticalProjection.lengthSqr() < О8) {
         return createPerpendicularBasis(direction);
      }

      Vec3 basisY = verticalProjection.normalize();
      Vec3 basisX = basisY.cross(direction).normalize();
      return new Vec3[]{basisX, basisY};
   }

   private static void initializeTargetPhase(LivingEntity target) {
      if (phaseEntityId != target.getId()) {
         phaseEntityId = target.getId();
         targetPhase = target.getId() % 1000 * IО;
      }
   }

   private static void renderCube(WorldEffectsEvent event, Vec3 targetPosition, LivingEntity target, int color, float alpha, float deltaSeconds, boolean active, float hurtProgress) {
      Matrix4f pose = event.getPoses().last().pose();
      Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      boolean hurtActive = active && target.hurtTime > 7;
      if (hurtActive && !cubeHurtLatched) {
         cubeStartXRotationRadians = cubeXRotationRadians;
         cubeStartYRotationRadians = cubeYRotationRadians;
         cubeEndXRotationRadians = cubeStartXRotationRadians;
         cubeEndYRotationRadians = cubeStartYRotationRadians;
         switch (EFFECT_RANDOM.nextInt(4)) {
            case 0:
               cubeEndYRotationRadians = cubeEndYRotationRadians + Ш7;
               break;
            case 1:
               cubeEndYRotationRadians = cubeEndYRotationRadians - ШР;
               break;
            case 2:
               cubeEndXRotationRadians = cubeEndXRotationRadians + Ш3;
               break;
            default:
               cubeEndXRotationRadians = cubeEndXRotationRadians - Шр;
         }

         cubeRotationProgress = 0.0F;
         cubeRotationActive = true;
         cubeHurtLatched = true;
      } else if (!hurtActive) {
         cubeHurtLatched = false;
      }

      if (!active && cubeRotationActive) {
         float var11 = smoothstep(cubeRotationProgress);
         cubeXRotationRadians = Mth.lerp(var11, cubeStartXRotationRadians, cubeEndXRotationRadians);
         cubeYRotationRadians = Mth.lerp(var11, cubeStartYRotationRadians, cubeEndYRotationRadians);
         cubeRotationActive = false;
      }

      if (cubeRotationActive) {
         cubeRotationProgress = Math.min(1.0F, cubeRotationProgress + deltaSeconds / Ш6);
         if (cubeRotationProgress >= 1.0F) {
            cubeXRotationRadians = cubeEndXRotationRadians;
            cubeYRotationRadians = cubeEndYRotationRadians;
            cubeRotationActive = false;
         }
      }

      float rotationBlend = smoothstep(cubeRotationProgress);
      float explosionGlow = 0.0F;
      explosionGlow = smoothstep(explosionGlow);
      float visibility = active ? Mth.clamp(alpha, 0.0F, 1.0F) : 1.0F;
      float sizeEnvelope = active ? Math.min(Ш_, easeOutBack(smoothstep(visibility))) : 1.0F;
      float explosionProgress = active ? 0.0F : Mth.clamp(1.0F - alpha, 0.0F, 1.0F);
      double scale = 1.0;
      double sizeScale = ШШ + sizeEnvelope * Шы;
      double halfSize = Шй * scale * sizeScale;
      double edgeLength = ШЖ * scale * sizeScale;
      double nodeRadius = Шм * scale * (ШК + sizeEnvelope * ШI);
      double xRotationRadians = cubeRotationActive ? Mth.lerp(rotationBlend, cubeStartXRotationRadians, cubeEndXRotationRadians) : cubeXRotationRadians;
      double yRotationRadians = cubeRotationActive ? Mth.lerp(rotationBlend, cubeStartYRotationRadians, cubeEndYRotationRadians) : cubeYRotationRadians;
      Vec3 origin = targetPosition.add(0.0, target.getBbHeight() * ШТ, 0.0);
      double groundY = -target.getBbHeight() * ШО + nodeRadius;
      int themeColor = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int effectColor = hurtRecolorEnabled ? ColorMath.lerpArgb(themeColor, ШФ, hurtProgress) : themeColor;
      float effectAlpha = active ? 1.0F : 1.0F - smoothstep(Mth.clamp((explosionProgress - ШЙ) / Шк, 0.0F, 1.0F));
      int maskColor = multiplyAlpha(effectColor, effectAlpha);
      RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      ensureCubeMask(mainTarget.width, mainTarget.height);
      BufferBuilder buffer = beginQuads();
      appendExplodingCubeCorners(buffer, pose, origin, cameraPosition, xRotationRadians, yRotationRadians, halfSize, edgeLength, nodeRadius, groundY, explosionProgress, maskColor);
      drawMaskGeometry(buffer, CUBE_PIPELINE, cubeMask, true);
      if (distortionEnabled) {
         GlowDistortionRenderer.INSTANCE.distortMaskTarget(cubeMask, distortionStrength);
      }

      cubeGlowColor = effectColor;
      cubeGlowStrength = 1.0F + explosionGlow * Шг;
      cubeCompositePending = true;
   }

   private static float getAnimatedArcAlpha(TargetEspMask.AnimatedArc arc, long nowMillis, float alpha) {
      float progress = (float)(nowMillis - arc.createdMillis) / (float)arc.durationMillis;
      if (!(progress < 0.0F) && !(progress > 1.0F)) {
         float attack = easeOutCubic(Mth.clamp(progress / Жа, 0.0F, 1.0F));
         float release = 1.0F - smoothstep(Mth.clamp((progress - ЖЮ) / ЖЧ, 0.0F, 1.0F));
         float flicker = ЖЛ + Ж诶 * hashArcUnitFloat((int)((nowMillis - arc.createdMillis) / Ж1) + 1, arc.flickerSeed);
         return alpha * attack * release * flicker;
      } else {
         return 0.0F;
      }
   }

   private static void buildAnimatedArcPath(TargetEspMask.AnimatedArc arc, Vec3 center, float targetHeight, float radius, int sampleIndex, List<Vec3> points) {
      points.clear();
      byte controlPointCount = 12;
      Vec3[] controlPoints = new Vec3[controlPointCount];
      float seed = arc.geometrySeed + sampleIndex * ЖЬ;
      float angularSpan = Math.max(arc.angularSpan, 1.0F);

      for (int var10 = 0; var10 < controlPointCount; var10++) {
         float progress = (float)var10 / (controlPointCount - 1);
         float angularJitter = (hashArcUnitFloat(var10 + 3 + sampleIndex * 11, seed) - Ж衣) * Жш;
         float radialJitter = (hashArcUnitFloat(var10 + 71 + sampleIndex * 5, seed) - ЖИ) * Жь;
         float heightJitter = (hashArcUnitFloat(var10 + 19 + sampleIndex, seed) - Жж) * Ж弟;
         double angle = arc.initialAngleRadians + arc.direction * angularSpan * Жб * ЖЫ * progress + angularJitter;
         double radialDistance = radius * arc.radiusScale * (1.0F + radialJitter);
         radialDistance = Mth.clamp((float)radialDistance, radius * Жl, radius * Ж8);
         double height = targetHeight * Mth.lerp(progress, arc.startHeightFactor, arc.endHeightFactor) + targetHeight * heightJitter;
         controlPoints[var10] = center.add(Math.cos(angle) * radialDistance, height, Math.sin(angle) * radialDistance);
      }

      subdivideArc(controlPoints, 0, controlPointCount - 1, radius * Ж5, 0, new Random(Float.floatToIntBits(seed) * Жв + sampleIndex * Жъ));

      for (int var21 = 0; var21 < controlPointCount; var21++) {
         Vec3 offset = controlPoints[var21].subtract(center);
         double radiusFromCenter = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
         if (radiusFromCenter > ЖМ) {
            double targetRadius = radius * arc.radiusScale;
            double radialScale = targetRadius / radiusFromCenter;
            double radialCorrection = ЖН;
            controlPoints[var21] = center.add(offset.x * (1.0 - radialCorrection + radialCorrection * radialScale), offset.y, offset.z * (1.0 - radialCorrection + radialCorrection * radialScale));
         }
      }

      for (int var22 = 0; var22 < 28; var22++) {
         float sampleProgress = var22 / Жн;
         float controlPointIndex = sampleProgress * (controlPointCount - 1);
         int segmentIndex = Math.min(controlPointCount - 2, (int)controlPointIndex);
         float segmentProgress = controlPointIndex - segmentIndex;
         points.add(controlPoints[segmentIndex].lerp(controlPoints[segmentIndex + 1], segmentProgress));
      }
   }

   private static Vec3 catmullRom(Vec3 previous, Vec3 start, Vec3 end, Vec3 next, double progress) {
      double progressSquared = progress * progress;
      double progressCubed = progressSquared * progress;
      return new Vec3(
         Iп
            * (
               IД * start.x
                  + (-previous.x + end.x) * progress
                  + (IЪ * previous.x - Iт * start.x + Iа * end.x - next.x) * progressSquared
                  + (-previous.x + IЮ * start.x - IЧ * end.x + next.x) * progressCubed
            ),
         IЛ
            * (
               I诶 * start.y
                  + (-previous.y + end.y) * progress
                  + (I1 * previous.y - IЬ * start.y + I衣 * end.y - next.y) * progressSquared
                  + (-previous.y + Iш * start.y - IИ * end.y + next.y) * progressCubed
            ),
         Iь
            * (
               Iж * start.z
                  + (-previous.z + end.z) * progress
                  + (I弟 * previous.z - Iб * start.z + IЫ * end.z - next.z) * progressSquared
                  + (-previous.z + Il * start.z - I8 * end.z + next.z) * progressCubed
            )
      );
   }

   private static void ensureCubeMask(int width, int height) {
      if (cubeMask == null) {
         cubeMask = new TextureTarget(CUBE_MASK_DEBUG_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (cubeMask.width != width || cubeMask.height != height) {
         cubeMask.resize(width, height);
      }
   }

   private static final class TrailSample {
      private final @com.google.gson.annotations.SerializedName("l")  Vec3 position;
      private final @com.google.gson.annotations.SerializedName("со")  long createdMillis;
      private final @com.google.gson.annotations.SerializedName("Яц")  float size;
      private final @com.google.gson.annotations.SerializedName("Яя")  float alpha;

      private TrailSample(Vec3 position, long createdMillis, float size, float alpha) {
         this.position = position;
         this.createdMillis = createdMillis;
         this.size = size;
         this.alpha = alpha;
      }
   }

   private record OrbitParticleSample(float x, float y, float z, float size, float spin) {
   }

   private static final class FadingOrbitParticle {
      private final @com.google.gson.annotations.SerializedName("ц")  double x;
      private final @com.google.gson.annotations.SerializedName("я")  double y;
      private final @com.google.gson.annotations.SerializedName("сс")  double z;
      private final @com.google.gson.annotations.SerializedName("сЮ")  long createdMillis;
      private final @com.google.gson.annotations.SerializedName("В1")  int color;
      private @com.google.gson.annotations.SerializedName("В3")  float alpha;
      private @com.google.gson.annotations.SerializedName("Вр")  float previousAlpha;
      private @com.google.gson.annotations.SerializedName("В6")  float targetAlpha = 1.0F;
      private @com.google.gson.annotations.SerializedName("сЧ")  long lastUpdateMillis;
      private @com.google.gson.annotations.SerializedName("сЛ")  long transitionDurationMillis;
      private static final long INITIAL_TRANSITION_MILLIS = 100L;
      private static final long FADE_START_MILLIS = 200L;
      private static final long UPDATE_TRANSITION_MILLIS = 50L;

      private void updateAlpha(long nowMillis) {
         float transitionProgress = Mth.clamp((float)(nowMillis - this.lastUpdateMillis) / (float)this.transitionDurationMillis, 0.0F, 1.0F);
         this.alpha = Mth.lerp(transitionProgress, this.previousAlpha, this.targetAlpha);
         this.previousAlpha = this.alpha;
         this.targetAlpha = nowMillis - this.createdMillis < FADE_START_MILLIS ? 1.0F : 0.0F;
         this.lastUpdateMillis = nowMillis;
         this.transitionDurationMillis = UPDATE_TRANSITION_MILLIS;
      }

      private FadingOrbitParticle(double x, double y, double z, long createdMillis, int color) {
         this.transitionDurationMillis = INITIAL_TRANSITION_MILLIS;
         this.x = x;
         this.y = y;
         this.z = z;
         this.createdMillis = createdMillis;
         this.color = color;
         this.lastUpdateMillis = createdMillis;
      }
   }

   private static final class AnimatedArc {
      private final @com.google.gson.annotations.SerializedName("弟")  double initialAngleRadians;
      private final @com.google.gson.annotations.SerializedName("Г")  int direction;
      private final @com.google.gson.annotations.SerializedName("сь")  float angularSpan;
      private final @com.google.gson.annotations.SerializedName("сж")  float endHeightFactor;
      private final @com.google.gson.annotations.SerializedName("с弟")  float startHeightFactor;
      private final @com.google.gson.annotations.SerializedName("сб")  float radiusScale;
      private final @com.google.gson.annotations.SerializedName("сЫ")  float widthScale;
      private final float сl;
      private final @com.google.gson.annotations.SerializedName("л")  int textureBand;
      private final @com.google.gson.annotations.SerializedName("с8")  float geometrySeed;
      private final @com.google.gson.annotations.SerializedName("O")  long createdMillis;
      @SerializedName("7")
      private final long durationMillis;
      private final @com.google.gson.annotations.SerializedName("с5")  float flickerSeed;

      private AnimatedArc(
         double initialAngleRadians,
         int direction,
         float angularSpan,
         float endHeightFactor,
         float startHeightFactor,
         float radiusScale,
         float widthScale,
         float var9,
         int textureBand,
         float geometrySeed,
         long createdMillis,
         long durationMillis,
         float flickerSeed
      ) {
         this.initialAngleRadians = initialAngleRadians;
         this.direction = direction;
         this.angularSpan = angularSpan;
         this.endHeightFactor = endHeightFactor;
         this.startHeightFactor = startHeightFactor;
         this.radiusScale = radiusScale;
         this.widthScale = widthScale;
         this.сl = var9;
         this.textureBand = textureBand;
         this.geometrySeed = geometrySeed;
         this.createdMillis = createdMillis;
         this.durationMillis = durationMillis;
         this.flickerSeed = flickerSeed;
      }
   }

   private static final class LightningBranch {
      private final @com.google.gson.annotations.SerializedName("с诶")  List<Vec3> points;
      private final @com.google.gson.annotations.SerializedName("АЩ")  float widthScale;
      private final @com.google.gson.annotations.SerializedName("Ау")  float growthDelay;

      private LightningBranch(List<Vec3> points, float widthScale, float growthDelay) {
         this.points = points;
         this.widthScale = widthScale;
         this.growthDelay = growthDelay;
      }
   }

   private static final class RotatingPolyline {
      private final @com.google.gson.annotations.SerializedName("ь")  List<Vec3> localPoints;
      private final @com.google.gson.annotations.SerializedName("д")  long createdMillis;
      private final @com.google.gson.annotations.SerializedName("Б")  long createdNanos;
      private final @com.google.gson.annotations.SerializedName("С")  long durationMillis;
      private final float с西;
      private final @com.google.gson.annotations.SerializedName("сд")  float widthScale;
      private final @com.google.gson.annotations.SerializedName("сч")  int rotationDirection;
      private final @com.google.gson.annotations.SerializedName("сБ")  float angularSpeed;

      private RotatingPolyline(List<Vec3> localPoints, long createdMillis, long createdNanos, long durationMillis, float var8, float widthScale, int rotationDirection, float angularSpeed) {
         this.localPoints = localPoints;
         this.createdMillis = createdMillis;
         this.createdNanos = createdNanos;
         this.durationMillis = durationMillis;
         this.с西 = var8;
         this.widthScale = widthScale;
         this.rotationDirection = rotationDirection;
         this.angularSpeed = angularSpeed;
      }
   }

   private static final class LightningEffect {
      private final @com.google.gson.annotations.SerializedName("Ь")  List<TargetEspMask.LightningBranch> branches;
      private final @com.google.gson.annotations.SerializedName("Ю")  Vec3 endPoint;
      private final @com.google.gson.annotations.SerializedName("Ч")  Vec3 origin;
      @SerializedName("0")
      private final long createdMillis;
      private final @com.google.gson.annotations.SerializedName("х")  long durationMillis;
      private final @com.google.gson.annotations.SerializedName("с6")  float seed;
      private final int сЧ;
      private final double и;
      @SerializedName("9")
      private final double field_9;
      private final float с_;
      private final float сШ;

      private LightningEffect(
         List<TargetEspMask.LightningBranch> branches,
         Vec3 endPoint,
         Vec3 origin,
         long createdMillis,
         long durationMillis,
         float seed,
         int var9,
         double var10,
         double var12,
         float var14,
         float var15
      ) {
         this.branches = branches;
         this.endPoint = endPoint;
         this.origin = origin;
         this.createdMillis = createdMillis;
         this.durationMillis = durationMillis;
         this.seed = seed;
         this.сЧ = var9;
         this.и = var10;
         this.field_9 = var12;
         this.с_ = var14;
         this.сШ = var15;
      }
   }
}
