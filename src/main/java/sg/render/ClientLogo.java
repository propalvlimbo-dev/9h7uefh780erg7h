package sg.render;

import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.FontRenderer;
import sg.render.pipeline.UiBatchDrawing;
import sg.render.pipeline.UiDrawing;

/** One vector-derived SDF glyph shared by every client logo renderer. Sizes refer to visible ink. */
public final class ClientLogo {
   public static final int CODEPOINT = 0xE000;
   public static final String GLYPH = "\uE000";
   public static final float ASPECT = 273.0F / 295.0F;
   public static final float DISTANCE_RANGE = 64.0F;
   public static final float DISPLAY_SCALE = 0.86F;
   public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("rose", "fonts/client_logo.png");
   /** Обычная (не SDF) текстура знака — нужна для вращения, меняющего ширину. */
   public static final Identifier LOGO_TEXTURE = Identifier.fromNamespaceAndPath("rose", "textures/gui/logo_rose.png");

   private ClientLogo() { }

   public static FontRenderer font() { return Holder.FONT; }
   public static float width(float height) { return ASPECT * height; }

   public static void drawCentered(float centerX, float centerY, float height, int color) {
      height *= DISPLAY_SCALE;
      UiDrawing.drawText(font(), GLYPH, centerX - width(height) * 0.5F, centerY - height * 0.5F,
         height, color, 0.0F, 0.5F, 1.0F);
   }

   public static void drawInBox(float x, float y, float width, float height, int color) {
      if (width <= 0.0F || height <= 0.0F) return;
      drawCentered(x + width * 0.5F, y + height * 0.5F, Math.min(height, width / ASPECT), color);
   }

   /** The caller must include font() in its existing batch's font list. */
   public static void drawCenteredBatched(float centerX, float centerY, int height, int color) {
      height = Math.max(1, Math.round(height * DISPLAY_SCALE));
      UiBatchDrawing.drawText(font(), GLYPH, centerX - width(height) * 0.5F,
         centerY - height * 0.5F, height, color);
   }

   /**
    * Вращение знака вокруг вертикальной оси: ширина сжимается по косинусу угла,
    * высота слегка уменьшается на «ребре» — читается как объёмное вращение.
    */
   public static void drawSpin3D(float centerX, float centerY, float size, int color, float angle) {
      if (size <= 0.0F) {
         return;
      }
      float c = Math.abs((float) Math.cos(angle));
      float shownWidth = size * ASPECT * (0.14F + 0.86F * c);
      float shownHeight = size * (1.0F - 0.05F * (1.0F - c));
      UiDrawing.drawTexture(centerX - shownWidth * 0.5F, centerY - shownHeight * 0.5F,
         shownWidth, shownHeight, 0.0F, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F),
         new Vector4i(color), LOGO_TEXTURE);
   }

   /** То же вращение, но угол берётся из текущего времени — крутится само, вечно. */
   public static void drawSpin3DByTime(float centerX, float centerY, float size, int color, long periodMs) {
      long period = periodMs <= 0L ? 4000L : periodMs;
      float progress = (System.currentTimeMillis() % period) / (float) period;
      drawSpin3D(centerX, centerY, size, color, progress * 6.2831855F);
   }

   private static final class Holder {
      private static final FontRenderer FONT = new FontRenderer("client_logo", true);
   }
}
