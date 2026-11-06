package sg.render;

import net.minecraft.resources.Identifier;
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

   private static final class Holder {
      private static final FontRenderer FONT = new FontRenderer("client_logo", true);
   }
}
