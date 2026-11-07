package sg.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.render.pipeline.UiDrawing;

public final class BackpackPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("В")  List<BackpackPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;
   private static final float SPRITE_FACTOR = 0.92F;

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      for (BackpackPreviewQueue.PreviewEntry preview : this.previews) {
         int alpha = Math.clamp(Math.round(preview.alpha * 255.0F), 0, 255);
         if (alpha <= 0) {
            continue;
         }

         float side = Math.min(preview.width, preview.height) * SPRITE_FACTOR;
         float spriteX = clipX + preview.x - side * 0.5F;
         float spriteY = clipY + preview.y - side * 0.5F;
         int red = Math.clamp(Math.round(preview.variant.getRedComponent() * 255.0F), 0, 255);
         int green = Math.clamp(Math.round(preview.variant.getGreenComponent() * 255.0F), 0, 255);
         int blue = Math.clamp(Math.round(preview.variant.getBlueComponent() * 255.0F), 0, 255);
         UiDrawing.drawRoundedRect(spriteX, spriteY, side, side, side * 0.22F, alpha << 24 | red << 16 | green << 8 | blue);
      }
   }

   public void clearPreviews() {
      this.previews.clear();
   }

   public void queuePreview(BackpackColorVariant variant, float x, float y, float width, float height, float alpha) {
      this.previews.add(new BackpackPreviewQueue.PreviewEntry(variant, x + width * HORIZONTAL_CENTER_FACTOR, y + height * VERTICAL_CENTER_FACTOR, alpha, width, height));
   }

   private record PreviewEntry(
      BackpackColorVariant variant, float x, float y, float alpha, float width, float height
   ) {
   }
}
