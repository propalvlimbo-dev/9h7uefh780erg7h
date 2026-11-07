package sg.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.render.pipeline.UiDrawing;

public final class HatPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("сч")  List<HatPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;
   private static final float SPRITE_FACTOR = 1.85F;

   public void clearPreviews() {
      this.previews.clear();
   }

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      for (HatPreviewQueue.PreviewEntry preview : this.previews) {
         int alpha = Math.clamp(Math.round(preview.alpha * 255.0F), 0, 255);
         if (alpha <= 0) {
            continue;
         }

         float side = Math.max(preview.width, preview.height) * SPRITE_FACTOR;
         float spriteX = clipX + preview.x - side * 0.5F;
         float spriteY = clipY + preview.y - side * 0.5F;
         UiDrawing.drawTexture(spriteX, spriteY, side, side, 0.0F, alpha << 24 | 0xFFFFFF, preview.hat.texture());
      }
   }

   public void queuePreview(HatDefinition hat, float x, float y, float width, float height, float alpha) {
      this.previews.add(new HatPreviewQueue.PreviewEntry(hat, x + width * HORIZONTAL_CENTER_FACTOR, y + height * VERTICAL_CENTER_FACTOR, alpha, width, height));
   }

   private record PreviewEntry(HatDefinition hat, float x, float y, float alpha, float width, float height) {
   }
}
