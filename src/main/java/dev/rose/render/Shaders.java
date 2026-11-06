package dev.rose.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.render.gpu.RoseShader;


public final class Shaders {
   private static final VertexFormat.Mode QUADS = VertexFormat.Mode.QUADS;
   private static final RoseShader SURFACE = RoseShader.create("surface", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader LIQUID_GLASS = RoseShader.create("liquid_glass", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader TEXT = RoseShader.create("sdf_text", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader KAWASE_DOWN = RoseShader.create("kawase_down", DefaultVertexFormat.POSITION, QUADS);
   private static final RoseShader KAWASE_UP = RoseShader.create("kawase_up", DefaultVertexFormat.POSITION, QUADS);
   private static final RoseShader ROUNDED_IMAGE = RoseShader.create("rounded_image", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader CHARACTER = RoseShader.create("character", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader POST_GRADE = RoseShader.create("post_grade", DefaultVertexFormat.POSITION, QUADS);
   private static final RoseShader SOFT_SPRITE = RoseShader.create("soft_sprite", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader GLOW_COMPOSITE = RoseShader.create("glow_composite", DefaultVertexFormat.POSITION, QUADS);
   private static final RoseShader SILHOUETTE = RoseShader.create("silhouette", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader BLACK_HOLE = RoseShader.create("black_hole", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);
   private static final RoseShader NEBULA = RoseShader.create("nebula", DefaultVertexFormat.POSITION_TEX_COLOR, QUADS);

   private Shaders() {
   }

   public static void register() {
   }

   public static RoseShader softSprite() {
      return SOFT_SPRITE;
   }

   public static RoseShader silhouette() {
      return SILHOUETTE;
   }

   public static RoseShader glowComposite() {
      return GLOW_COMPOSITE;
   }

   public static RoseShader surface() {
      return SURFACE;
   }

   public static RoseShader liquidGlass() {
      return LIQUID_GLASS;
   }

   public static RoseShader text() {
      return TEXT;
   }

   public static RoseShader kawaseDown() {
      return KAWASE_DOWN;
   }

   public static RoseShader character() {
      return CHARACTER;
   }

   public static RoseShader roundedImage() {
      return ROUNDED_IMAGE;
   }

   public static RoseShader postGrade() {
      return POST_GRADE;
   }

   public static RoseShader kawaseUp() {
      return KAWASE_UP;
   }

   public static RoseShader blackHole() {
      return BLACK_HOLE;
   }

   public static RoseShader nebula() {
      return NEBULA;
   }
}
