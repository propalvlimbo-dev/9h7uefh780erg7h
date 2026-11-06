package sg.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.ec.HatPreviewRenderState;

public final class HatPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("сч")  List<HatPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;

   public void clearPreviews() {
      this.previews.clear();
   }

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      if (!this.previews.isEmpty()) {
         GuiCoordinateTransform transform = GuiCoordinateTransform.of(clipX, clipY, clipWidth, clipHeight);
         List draws = this.previews
            .stream()
            .map(
               preview -> new HatPreviewRenderState.HatDraw(
                  preview.hat, preview.alpha, transform.localX(preview.x), transform.localY(preview.y), preview.width * transform.scaleX(), preview.height * transform.scaleY()
               )
            )
            .toList();
         graphics.guiRenderState.addPicturesInPictureState(new HatPreviewRenderState(draws, transform.x1(), transform.y1(), transform.x2(), transform.y2(), 1.0F, transform.clip()));
      }
   }

   public void queuePreview(HatDefinition hat, float x, float y, float width, float height, float alpha) {
      this.previews.add(new HatPreviewQueue.PreviewEntry(hat, x + width * HORIZONTAL_CENTER_FACTOR, y + height * VERTICAL_CENTER_FACTOR, alpha, width, height));
   }

   private record PreviewEntry(HatDefinition hat, float x, float y, float alpha, float width, float height) {
   }
}
