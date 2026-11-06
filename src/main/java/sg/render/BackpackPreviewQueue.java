package sg.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.gui.BackpackPreviewRenderState;

public final class BackpackPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("В")  List<BackpackPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      if (!this.previews.isEmpty()) {
         GuiCoordinateTransform transform = GuiCoordinateTransform.of(clipX, clipY, clipWidth, clipHeight);
         List draws = this.previews
            .stream()
            .map(
               preview -> new BackpackPreviewRenderState.Preview(
                  preview.variant, preview.alpha, transform.localX(preview.x), transform.localY(preview.y), preview.width * transform.scaleX(), preview.height * transform.scaleY()
               )
            )
            .toList();
         graphics.guiRenderState.addPicturesInPictureState(new BackpackPreviewRenderState(draws, transform.x1(), transform.y1(), transform.x2(), transform.y2(), transform.clip()));
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
