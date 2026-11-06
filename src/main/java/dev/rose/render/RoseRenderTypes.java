package dev.rose.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.RoseShader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;


public final class RoseRenderTypes {
   private static final Map<Identifier, RenderType> GLOW_SPRITES = new HashMap<>();
   private static final Map<Identifier, RenderType> GLOW_SPRITES_THROUGH = new HashMap<>();
   private static final Map<Identifier, RenderType> SOFT_SPRITES = new HashMap<>();
   private static final Map<Identifier, RenderType> LIT = new HashMap<>();
   private static final OutputTarget GLOW_TARGET = new OutputTarget("rose_glow_target", GlowEngine::glowTarget);
   private static final OutputTarget HALO_TARGET = new OutputTarget("rose_halo_target", GlowEngine::haloTarget);
   private static final RenderPipeline GLOW_PIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.MATRICES_FOG_SNIPPET})
      .withLocation(id("pipeline/glow"))
      .withVertexShader("core/rendertype_lightning")
      .withFragmentShader("core/rendertype_lightning")
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final RenderPipeline GLOW_THROUGH_PIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.MATRICES_FOG_SNIPPET})
      .withLocation(id("pipeline/glow_through"))
      .withVertexShader("core/rendertype_lightning")
      .withFragmentShader("core/rendertype_lightning")
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final RenderPipeline FACES_PIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.MATRICES_PROJECTION_SNIPPET})
      .withLocation(id("pipeline/faces"))
      .withVertexShader("core/position_color")
      .withFragmentShader("core/position_color")
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   public static final RenderType GLOW = RenderType.create(
      "rose_glow", RenderSetup.builder(GLOW_PIPELINE).setOutputTarget(GLOW_TARGET).sortOnUpload().bufferSize(8192).createRenderSetup()
   );
   public static final RenderType FACES = RenderType.create(
      "rose_faces", RenderSetup.builder(FACES_PIPELINE).setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).sortOnUpload().createRenderSetup()
   );
   public static final RenderType ITEM_SILHOUETTE = silhouette("rose_item_silhouette", TextureAtlas.LOCATION_BLOCKS);
   public static final RenderType ITEM_SILHOUETTE_ITEMS = silhouette("rose_item_silhouette_items", TextureAtlas.LOCATION_ITEMS);
   public static final RenderType GLOW_THROUGH = RenderType.create(
      "rose_glow_through", RenderSetup.builder(GLOW_THROUGH_PIPELINE).setOutputTarget(GLOW_TARGET).sortOnUpload().bufferSize(2048).createRenderSetup()
   );
   private static final RenderPipeline ADDITIVE_ENTITY_PIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.MATRICES_FOG_SNIPPET})
      .withLocation(id("pipeline/additive_entity"))
      .withVertexShader("core/entity")
      .withFragmentShader("core/entity")
      .withShaderDefine("EMISSIVE")
      .withShaderDefine("NO_OVERLAY")
      .withShaderDefine("NO_CARDINAL_LIGHTING")
      .withSampler("Sampler0")
      .withBlend(BlendFunction.ADDITIVE)
      .withDepthWrite(false)
      .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
      .build();
   private static final Map<Identifier, RenderType> ADDITIVE_ENTITY = new HashMap<>();

   private RoseRenderTypes() {
   }

   private static Identifier id(String path) {
      return Identifier.fromNamespaceAndPath("rose", path);
   }

   private static RenderType silhouette(String name, Identifier atlas) {
      return RenderType.create(
         name,
         RenderSetup.builder(Shaders.silhouette().pipeline(Blend.REPLACE, RoseShader.Depth.TEST))
            .withTexture("Sampler0", atlas)
            .setOutputTarget(HALO_TARGET)
            .bufferSize(4096)
            .createRenderSetup()
      );
   }

   public static RenderType glowSpriteThrough(Identifier texture) {
      return GLOW_SPRITES_THROUGH.computeIfAbsent(
         texture,
         id -> RenderType.create(
            "rose_glow_sprite_through",
            RenderSetup.builder(Shaders.softSprite().pipeline(Blend.LIGHTNING, RoseShader.Depth.NONE))
               .withTexture("Sampler0", id, RoseRenderTypes::smooth)
               .setOutputTarget(GLOW_TARGET)
               .sortOnUpload()
               .bufferSize(1024)
               .createRenderSetup()
         )
      );
   }

   public static RenderType glowSprite(Identifier texture) {
      return GLOW_SPRITES.computeIfAbsent(
         texture,
         id -> RenderType.create(
            "rose_glow_sprite",
            RenderSetup.builder(Shaders.softSprite().pipeline(Blend.LIGHTNING, RoseShader.Depth.TEST))
               .withTexture("Sampler0", id, RoseRenderTypes::smooth)
               .setOutputTarget(GLOW_TARGET)
               .sortOnUpload()
               .bufferSize(4096)
               .createRenderSetup()
         )
      );
   }

   public static RenderType softSprite(Identifier texture) {
      return SOFT_SPRITES.computeIfAbsent(
         texture,
         id -> RenderType.create(
            "rose_soft_sprite",
            RenderSetup.builder(Shaders.softSprite().pipeline(Blend.NORMAL, RoseShader.Depth.TEST))
               .withTexture("Sampler0", id, RoseRenderTypes::smooth)
               .setOutputTarget(OutputTarget.WEATHER_TARGET)
               .sortOnUpload()
               .bufferSize(4096)
               .createRenderSetup()
         )
      );
   }

   public static RenderType lit(Identifier texture) {
      return LIT.computeIfAbsent(
         texture,
         id -> RenderType.create(
            "rose_lit",
            RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT)
               .withTexture("Sampler0", id, RoseRenderTypes::smooth)
               .useLightmap()
               .useOverlay()
               .affectsCrumbling()
               .sortOnUpload()
               .bufferSize(4096)
               .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
               .createRenderSetup()
         )
      );
   }

   public static RenderType lines() {
      return RenderTypes.lines();
   }

   public static RenderType additiveEntity(Identifier texture) {
      return ADDITIVE_ENTITY.computeIfAbsent(
         texture,
         id -> RenderType.create(
            "rose_additive_entity", RenderSetup.builder(ADDITIVE_ENTITY_PIPELINE).withTexture("Sampler0", id).sortOnUpload().createRenderSetup()
         )
      );
   }

   private static GpuSampler smooth() {
      return RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR);
   }
}
