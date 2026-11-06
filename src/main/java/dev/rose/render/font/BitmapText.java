package dev.rose.render.font;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.render.gpu.RoseTexture;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;


public final class BitmapText {
   private final String fontFile;
   private final String name;
   private ByteBuffer data;
   private STBTTFontinfo info;
   private boolean broken;
   private RoseTexture texture;
   private Identifier id;
   private String cachedText;
   private int cachedPixels;
   private float cachedTracking;
   private int width;
   private int height;
   private float ascentPixels;

   public BitmapText(String fontFile, String name) {
      this.fontFile = fontFile;
      this.name = name;
   }

   public void drawCentered(GuiGraphics g, String text, float centerX, float y, float size, float tracking, int color, float alpha) {
      if (!this.broken && !text.isEmpty() && !(alpha <= 0.003F)) {
         double scale = Minecraft.getInstance().getWindow().getGuiScale();
         int pixels = Math.max(4, Math.round((float)(size * scale)));
         if (this.ensure(text, pixels, tracking)) {
            float left = (float)(Math.round((centerX - this.width / scale / 2.0) * scale) / scale);
            float top = (float)(Math.round(y * scale) / scale);
            g.pose().pushMatrix();
            g.pose().translate(left, top);
            g.pose().scale((float)(1.0 / scale), (float)(1.0 / scale));
            int tint = ARGB.color(Math.round((color >>> 24) * Math.clamp(alpha, 0.0F, 1.0F)), color & 16777215);
            g.blit(RenderPipelines.GUI_TEXTURED, this.id, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height, this.width, this.height, tint);
            g.pose().popMatrix();
         }
      }
   }

   private boolean ensure(String text, int pixels, float tracking) {
      if (this.texture != null && text.equals(this.cachedText) && pixels == this.cachedPixels && tracking == this.cachedTracking) {
         return true;
      }

      try {
         if (this.info == null) {
            this.load();
         }

         this.rasterize(text, pixels, tracking);
         this.cachedText = text;
         this.cachedPixels = pixels;
         this.cachedTracking = tracking;
         return true;
      } catch (Exception e) {
         this.broken = true;
         RoseClient.LOGGER.error("[Rose] bitmap text '{}' failed", this.fontFile, e);
         return false;
      }
   }

   private void load() throws Exception {
      String path = "/assets/rose/fonts/" + this.fontFile + ".ttf";

      try (InputStream in = BitmapText.class.getResourceAsStream(path)) {
         if (in == null) {
            throw new IllegalStateException("missing " + path);
         }

         byte[] bytes = in.readAllBytes();
         this.data = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
      }

      this.info = STBTTFontinfo.create();
      if (!STBTruetype.stbtt_InitFont(this.info, this.data)) {
         throw new IllegalStateException("cannot read " + this.fontFile);
      }
   }

   private void rasterize(String text, int pixels, float tracking) {
      float scale = STBTruetype.stbtt_ScaleForPixelHeight(this.info, pixels);
      int[] ascent = new int[1];
      int[] descent = new int[1];
      int[] gap = new int[1];
      STBTruetype.stbtt_GetFontVMetrics(this.info, ascent, descent, gap);
      this.ascentPixels = ascent[0] * scale;
      int[] codepoints = text.codePoints().toArray();
      int[] advance = new int[1];
      int[] bearing = new int[1];
      float pen = 1.0F;

      for (int i = 0; i < codepoints.length; i++) {
         STBTruetype.stbtt_GetCodepointHMetrics(this.info, codepoints[i], advance, bearing);
         pen += advance[0] * scale;
         if (i + 1 < codepoints.length) {
            pen += STBTruetype.stbtt_GetCodepointKernAdvance(this.info, codepoints[i], codepoints[i + 1]) * scale + tracking * pixels;
         }
      }

      int w = (int)Math.ceil(pen) + 2;
      int h = (int)Math.ceil((ascent[0] - descent[0]) * scale) + 2;
      byte[] alpha = new byte[w * h];
      int[] x0 = new int[1];
      int[] y0 = new int[1];
      int[] x1 = new int[1];
      int[] y1 = new int[1];
      pen = 1.0F;
      int baseline = Math.round(this.ascentPixels) + 1;

      for (int i = 0; i < codepoints.length; i++) {
         int cp = codepoints[i];
         int whole = (int)Math.floor(pen);
         float shift = pen - whole;
         STBTruetype.stbtt_GetCodepointBitmapBoxSubpixel(this.info, cp, scale, scale, shift, 0.0F, x0, y0, x1, y1);
         int gw = x1[0] - x0[0];
         int gh = y1[0] - y0[0];
         if (gw > 0 && gh > 0) {
            ByteBuffer glyph = BufferUtils.createByteBuffer(gw * gh);
            STBTruetype.stbtt_MakeCodepointBitmapSubpixel(this.info, glyph, gw, gh, gw, scale, scale, shift, 0.0F, cp);

            for (int gy = 0; gy < gh; gy++) {
               int ty = baseline + y0[0] + gy;
               if (ty >= 0 && ty < h) {
                  for (int gx = 0; gx < gw; gx++) {
                     int tx = whole + x0[0] + gx;
                     if (tx >= 0 && tx < w) {
                        int value = glyph.get(gy * gw + gx) & 255;
                        int index = ty * w + tx;
                        if (value > (alpha[index] & 255)) {
                           alpha[index] = (byte)value;
                        }
                     }
                  }
               }
            }
         }

         STBTruetype.stbtt_GetCodepointHMetrics(this.info, cp, advance, bearing);
         pen += advance[0] * scale;
         if (i + 1 < codepoints.length) {
            pen += STBTruetype.stbtt_GetCodepointKernAdvance(this.info, cp, codepoints[i + 1]) * scale + tracking * pixels;
         }
      }

      NativeImage image = new NativeImage(w, h, true);

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int a = alpha[y * w + x] & 255;
            if (a > 0) {
               image.setPixelABGR(x, y, a << 24 | 16777215);
            }
         }
      }

      if (this.id != null) {
         Minecraft.getInstance().getTextureManager().release(this.id);
      }

      this.texture = new RoseTexture("text " + this.name, image);
      this.id = Identifier.fromNamespaceAndPath("rose", "dynamic/text_" + this.name.toLowerCase(Locale.ROOT));
      Minecraft.getInstance().getTextureManager().register(this.id, this.texture);
      this.width = w;
      this.height = h;
   }
}
