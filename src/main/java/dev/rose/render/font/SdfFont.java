package dev.rose.render.font;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.Shaders;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.gpu.RoseTexture;
import dev.rose.util.ColorUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;


public final class SdfFont {
   static final int TEXT_GLYPH_SIZE = 48;
   static final int ICON_GLYPH_SIZE = 64;
   private static final int ATLAS_WIDTH = 1024;
   private static final int GAP = 1;
   private final SdfFont.Prepared font;
   private final Long2FloatOpenHashMap kerning = new Long2FloatOpenHashMap();
   private final RoseTexture texture;
   private final SdfFont.GlyphTransform transform = new SdfFont.GlyphTransform();

   SdfFont(SdfFont.Prepared font) {
      this.font = font;
      this.kerning.defaultReturnValue(Float.NaN);
      NativeImage image = new NativeImage(1024, font.atlasHeight, false);

      for (int y = 0; y < font.atlasHeight; y++) {
         for (int x = 0; x < 1024; x++) {
            int d = font.atlas[y * 1024 + x] & 255;
            image.setPixelABGR(x, y, 0xFF000000 | d << 16 | d << 8 | d);
         }
      }

      this.texture = new RoseTexture("font " + font.name, image).filter(true, false);
      Minecraft.getInstance().getTextureManager().register(Identifier.fromNamespaceAndPath("rose", "font/" + font.name), this.texture);
   }

   static int[] defaultCharset() {
      IntArrayList list = new IntArrayList();

      for (int c = 32; c < 127; c++) {
         list.add(c);
      }

      for (int c = 160; c <= 255; c++) {
         list.add(c);
      }

      for (int c = 1024; c <= 1119; c++) {
         list.add(c);
      }

      list.addElements(
         list.size(), new int[]{8211, 8212, 8216, 8217, 8220, 8221, 8226, 8230, 8249, 8250, 8470, 8592, 8593, 8594, 8595, 8722, 8734, 10003, 10005}
      );
      return list.toIntArray();
   }

   public float draw(GuiGraphics g, String text, float x, float y, float size, int color) {
      return this.emit(g, text, x, y, size, 0.0F, color, color, null);
   }

   public float draw(GuiGraphics g, String text, float x, float y, float size, float tracking, int color) {
      return this.emit(g, text, x, y, size, tracking, color, color, null);
   }

   public float drawGradient(GuiGraphics g, String text, float x, float y, float size, int left, int right) {
      return this.emit(g, text, x, y, size, 0.0F, left, right, null);
   }

   public float drawAnimated(GuiGraphics g, String text, float x, float y, float size, float tracking, int color, SdfFont.GlyphAnimator animator) {
      return this.emit(g, text, x, y, size, tracking, color, color, animator);
   }

   private float emit(GuiGraphics g, String text, float x, float y, float size, float tracking, int left, int right, SdfFont.GlyphAnimator animator) {
      RoseShader shader = Shaders.text();
      if (text.isEmpty()) {
         return this.width(text, size, tracking);
      }

      float k = size / this.font.glyphSize;
      float baseline = animator == null ? snapToPixel(y + this.font.ascent * k) : y + this.font.ascent * k;
      float totalWidth = left == right ? 1.0F : Math.max(1.0F, this.width(text, size, tracking));
      int count = animator == null ? 0 : text.codePointCount(0, text.length());
      float penX = x;
      int previous = -1;
      int index = 0;
      Matrix4f pose = GuiDraw.pose(g);
      BufferBuilder buffer = GuiDraw.begin(shader);

      for (int i = 0; i < text.length(); index++) {
         int cp = text.codePointAt(i);
         i += Character.charCount(cp);
         SdfFont.Glyph glyph = this.glyph(cp);
         if (glyph != null) {
            if (previous != -1) {
               penX += this.kerning(previous, cp) * k + tracking;
            }

            if (glyph.width > 0.0F) {
               float x0 = penX + glyph.offsetX * k;
               float y0 = baseline + glyph.offsetY * k;
               float x1 = x0 + glyph.width * k;
               float y1 = y0 + glyph.height * k;
               if (animator == null) {
                  int c0 = left == right ? left : ColorUtil.lerp(left, right, Math.clamp((x0 - x) / totalWidth, 0.0F, 1.0F));
                  int c1 = left == right ? left : ColorUtil.lerp(left, right, Math.clamp((x1 - x) / totalWidth, 0.0F, 1.0F));
                  buffer.addVertex(pose, x0, y0, 0.0F).setUv(glyph.u0, glyph.v0).setColor(c0);
                  buffer.addVertex(pose, x0, y1, 0.0F).setUv(glyph.u0, glyph.v1).setColor(c0);
                  buffer.addVertex(pose, x1, y1, 0.0F).setUv(glyph.u1, glyph.v1).setColor(c1);
                  buffer.addVertex(pose, x1, y0, 0.0F).setUv(glyph.u1, glyph.v0).setColor(c1);
               } else {
                  this.transform.reset(left);
                  animator.apply(index, count, this.transform);
                  if (ColorUtil.alpha(this.transform.color) > 0 && this.transform.scale > 0.001F) {
                     this.addTransformed(buffer, pose, glyph, x0, y0, x1, y1);
                  }
               }
            }

            penX += glyph.advance * k;
            previous = cp;
         }
      }

      this.submit(g, shader, buffer);
      return penX - x;
   }

   private void addTransformed(BufferBuilder buffer, Matrix4f pose, SdfFont.Glyph glyph, float x0, float y0, float x1, float y1) {
      float cx = (x0 + x1) / 2.0F + this.transform.offsetX;
      float cy = (y0 + y1) / 2.0F + this.transform.offsetY;
      float hw = (x1 - x0) / 2.0F * this.transform.scale;
      float hh = (y1 - y0) / 2.0F * this.transform.scale;
      float cos = (float)Math.cos(this.transform.rotation);
      float sin = (float)Math.sin(this.transform.rotation);
      int color = this.transform.color;
      vertex(buffer, pose, cx, cy, -hw, -hh, cos, sin).setUv(glyph.u0, glyph.v0).setColor(color);
      vertex(buffer, pose, cx, cy, -hw, hh, cos, sin).setUv(glyph.u0, glyph.v1).setColor(color);
      vertex(buffer, pose, cx, cy, hw, hh, cos, sin).setUv(glyph.u1, glyph.v1).setColor(color);
      vertex(buffer, pose, cx, cy, hw, -hh, cos, sin).setUv(glyph.u1, glyph.v0).setColor(color);
   }

   private static VertexConsumer vertex(BufferBuilder buffer, Matrix4f pose, float cx, float cy, float dx, float dy, float cos, float sin) {
      return buffer.addVertex(pose, cx + dx * cos - dy * sin, cy + dx * sin + dy * cos, 0.0F);
   }

   public void drawIcon(GuiGraphics g, int codepoint, float centerX, float centerY, float size, int color) {
      RoseShader shader = Shaders.text();
      SdfFont.Glyph glyph = (SdfFont.Glyph)this.font.glyphs.get(codepoint);
      if (glyph != null && !(glyph.width <= 0.0F)) {
         float k = size / this.font.glyphSize;
         float w = glyph.width * k;
         float h = glyph.height * k;
         float x0 = centerX - w / 2.0F;
         float y0 = centerY - h / 2.0F;
         Matrix4f pose = GuiDraw.pose(g);
         BufferBuilder buffer = GuiDraw.begin(shader);
         buffer.addVertex(pose, x0, y0, 0.0F).setUv(glyph.u0, glyph.v0).setColor(color);
         buffer.addVertex(pose, x0, y0 + h, 0.0F).setUv(glyph.u0, glyph.v1).setColor(color);
         buffer.addVertex(pose, x0 + w, y0 + h, 0.0F).setUv(glyph.u1, glyph.v1).setColor(color);
         buffer.addVertex(pose, x0 + w, y0, 0.0F).setUv(glyph.u1, glyph.v0).setColor(color);
         this.submit(g, shader, buffer);
      }
   }

   private void submit(GuiGraphics g, RoseShader shader, BufferBuilder buffer) {
      shader.safeGetUniform("DistPerTexel").set(128.0F / this.font.padding / 255.0F);
      shader.setSampler("Sampler0", this.texture);
      GuiDraw.submit(g, shader, Blend.NORMAL, buffer);
   }

   public float width(String text, float size) {
      return this.width(text, size, 0.0F);
   }

   public float width(String text, float size, float tracking) {
      float k = size / this.font.glyphSize;
      float width = 0.0F;
      int previous = -1;
      int i = 0;

      while (i < text.length()) {
         int cp = text.codePointAt(i);
         i += Character.charCount(cp);
         SdfFont.Glyph glyph = this.glyph(cp);
         if (glyph != null) {
            if (previous != -1) {
               width += this.kerning(previous, cp) * k + tracking;
            }

            width += glyph.advance * k;
            previous = cp;
         }
      }

      return width;
   }

   public float lineHeight(float size) {
      return (this.font.ascent - this.font.descent + this.font.lineGap) * size / this.font.glyphSize;
   }

   public float capHeight(float size) {
      SdfFont.Glyph glyph = (SdfFont.Glyph)this.font.glyphs.get(72);
      return glyph == null ? this.font.ascent * 0.7F * size / this.font.glyphSize : (-glyph.offsetY - this.font.padding) * size / this.font.glyphSize;
   }

   public float ascent(float size) {
      return this.font.ascent * size / this.font.glyphSize;
   }

   public boolean canDisplay(String text) {
      int i = 0;

      while (i < text.length()) {
         int cp = text.codePointAt(i);
         i += Character.charCount(cp);
         if (!this.font.glyphs.containsKey(cp)) {
            return false;
         }
      }

      return true;
   }

   private SdfFont.Glyph glyph(int codepoint) {
      SdfFont.Glyph glyph = (SdfFont.Glyph)this.font.glyphs.get(codepoint);
      return glyph != null ? glyph : (SdfFont.Glyph)this.font.glyphs.get(63);
   }

   private float kerning(int left, int right) {
      long key = (long)left << 32 | right & 4294967295L;
      float value = this.kerning.get(key);
      if (Float.isNaN(value)) {
         value = STBTruetype.stbtt_GetCodepointKernAdvance(this.font.info, left, right) * this.font.scale;
         this.kerning.put(key, value);
      }

      return value;
   }

   private static float snapToPixel(float value) {
      double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
      return (float)(Math.round(value * guiScale) / guiScale);
   }

   private record Glyph(float u0, float v0, float u1, float v1, float offsetX, float offsetY, float width, float height, float advance) {
   }

   @FunctionalInterface
   public interface GlyphAnimator {
      void apply(int var1, int var2, SdfFont.GlyphTransform var3);
   }

   public static final class GlyphTransform {
      public float offsetX;
      public float offsetY;
      public float scale;
      public float rotation;
      public int color;

      void reset(int baseColor) {
         this.offsetX = this.offsetY = this.rotation = 0.0F;
         this.scale = 1.0F;
         this.color = baseColor;
      }
   }

   static final class Prepared {
      final String name;
      final int glyphSize;
      final int padding;
      final ByteBuffer data;
      final STBTTFontinfo info = STBTTFontinfo.create();
      final float scale;
      final float ascent;
      final float descent;
      final float lineGap;
      final Int2ObjectOpenHashMap<SdfFont.Glyph> glyphs = new Int2ObjectOpenHashMap();
      final byte[] atlas;
      final int atlasHeight;

      Prepared(String name, byte[] ttf, int[] codepoints, int glyphSize) {
         this.name = name;
         this.glyphSize = glyphSize;
         this.padding = glyphSize / 8;
         this.data = BufferUtils.createByteBuffer(ttf.length).put(ttf).flip();
         if (!STBTruetype.stbtt_InitFont(this.info, this.data)) {
            throw new IllegalStateException("Cannot read font " + name);
         }

         this.scale = STBTruetype.stbtt_ScaleForPixelHeight(this.info, glyphSize);
         int[] a = new int[1];
         int[] d = new int[1];
         int[] g = new int[1];
         STBTruetype.stbtt_GetFontVMetrics(this.info, a, d, g);
         this.ascent = a[0] * this.scale;
         this.descent = d[0] * this.scale;
         this.lineGap = g[0] * this.scale;

         record Bitmap(int codepoint, byte[] pixels, int width, int height, int offsetX, int offsetY, float advance) {
         }

         List<Bitmap> bitmaps = new ArrayList<>();
         int[] w = new int[1];
         int[] h = new int[1];
         int[] ox = new int[1];
         int[] oy = new int[1];
         int[] advance = new int[1];
         int[] bearing = new int[1];

         for (int cp : codepoints) {
            if (cp == 32 || STBTruetype.stbtt_FindGlyphIndex(this.info, cp) != 0) {
               STBTruetype.stbtt_GetCodepointHMetrics(this.info, cp, advance, bearing);
               ByteBuffer sdf = STBTruetype.stbtt_GetCodepointSDF(this.info, this.scale, cp, this.padding, (byte)-128, 128.0F / this.padding, w, h, ox, oy);
               if (sdf == null) {
                  bitmaps.add(new Bitmap(cp, new byte[0], 0, 0, 0, 0, advance[0] * this.scale));
               } else {
                  byte[] pixels = new byte[w[0] * h[0]];
                  sdf.get(0, pixels);
                  STBTruetype.stbtt_FreeSDF(sdf);
                  bitmaps.add(new Bitmap(cp, pixels, w[0], h[0], ox[0], oy[0], advance[0] * this.scale));
               }
            }
         }

         int[][] positions = new int[bitmaps.size()][];
         int x = 1;
         int y = 1;
         int rowHeight = 0;

         for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            if (x + bitmap.width + 1 > 1024) {
               x = 1;
               y += rowHeight + 1;
               rowHeight = 0;
            }

            positions[i] = new int[]{x, y};
            x += bitmap.width + 1;
            rowHeight = Math.max(rowHeight, bitmap.height);
         }

         int height = 1;

         while (height < y + rowHeight + 1) {
            height <<= 1;
         }

         this.atlasHeight = height;
         this.atlas = new byte[1024 * this.atlasHeight];

         for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            int px = positions[i][0];
            int py = positions[i][1];

            for (int row = 0; row < bitmap.height; row++) {
               System.arraycopy(bitmap.pixels, row * bitmap.width, this.atlas, (py + row) * 1024 + px, bitmap.width);
            }

            this.glyphs
               .put(
                  bitmap.codepoint,
                  new SdfFont.Glyph(
                     px / 1024.0F,
                     (float)py / this.atlasHeight,
                     (px + bitmap.width) / 1024.0F,
                     (float)(py + bitmap.height) / this.atlasHeight,
                     bitmap.offsetX,
                     bitmap.offsetY,
                     bitmap.width,
                     bitmap.height,
                     bitmap.advance
                  )
               );
         }
      }
   }
}
