package sg.hud;

import sg.render.pipeline.UiBatchDrawing;
import sg.render.pipeline.UiDrawing;

/** HUD panels share the Third GUI's base panel color, independently of theme/glass settings. */
public final class HudBackground {
   public static final int COLOR = sg.gui.third.ThirdGuiColors.PANEL;

   private HudBackground() { }

   public static void draw(float x, float y, float width, float height, float radius) {
      float alpha = UiDrawing.getGlobalAlpha();
      if (width <= 0.0F || height <= 0.0F || alpha <= 0.0F) return;
      UiDrawing.setGlobalAlpha(255.0F);
      try {
         UiDrawing.drawRoundedRect(x, y, width, height, radius, COLOR);
      } finally {
         UiDrawing.setGlobalAlpha(alpha);
      }
   }

   public static void drawBatched(float x, float y, float width, float height, float radius) {
      float alpha = UiDrawing.getGlobalAlpha();
      if (width <= 0.0F || height <= 0.0F || alpha <= 0.0F) return;
      UiDrawing.setGlobalAlpha(255.0F);
      try {
         UiBatchDrawing.drawRoundedRect(x, y, width, height, radius, COLOR);
      } finally {
         UiDrawing.setGlobalAlpha(alpha);
      }
   }
}
