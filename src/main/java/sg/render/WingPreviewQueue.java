package sg.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.ec.WingPreviewRenderState;

public final class WingPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("衣")  Map<WingSelection, WingPreviewQueue.PreviewAnimator> previewAnimators = new EnumMap<>(WingSelection.class);
   private final @com.google.gson.annotations.SerializedName("И")  List<WingPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;

   public void queuePreview(WingSelection wings, float x, float y, float width, float height, float alpha) {
      WingPreviewQueue.PreviewAnimator previewAnimator = this.previewAnimators.computeIfAbsent(wings, variant -> new WingPreviewQueue.PreviewAnimator(variant.getAnimatedVariant().createAnimator()));
      this.previews.add(new WingPreviewQueue.PreviewEntry(wings, previewAnimator, x + width * HORIZONTAL_CENTER_FACTOR, y + height * VERTICAL_CENTER_FACTOR, width, height, alpha));
   }

   public void clearPreviews() {
      this.previews.clear();
   }

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      if (!this.previews.isEmpty()) {
         GuiCoordinateTransform transform = GuiCoordinateTransform.of(clipX, clipY, clipWidth, clipHeight);
         List draws = this.previews
            .stream()
            .map(
               preview -> new WingPreviewRenderState.WingDraw(
                  preview.wing.getAnimatedVariant(),
                  preview.preview.animator,
                  preview.preview.advanceAndGetPartialTick(),
                  preview.alpha,
                  transform.localX(preview.x),
                  transform.localY(preview.y),
                  preview.width * transform.scaleX(),
                  preview.height * transform.scaleY()
               )
            )
            .toList();
         graphics.guiRenderState.addPicturesInPictureState(new WingPreviewRenderState(draws, transform.x1(), transform.y1(), transform.x2(), transform.y2(), 1.0F, transform.clip()));
      }
   }

   private static final class PreviewAnimator {
      private final @com.google.gson.annotations.SerializedName("Я")  WingAnimator animator;
      private @com.google.gson.annotations.SerializedName("еЛ")  long lastTickNanos = System.nanoTime();
      private static final long TICK_INTERVAL_NANOS = 50000000L;

      private PreviewAnimator(WingAnimator animator) {
         this.animator = animator;
         animator.setFoldedIdle();
      }

      private float advanceAndGetPartialTick() {
         long nowNanos = System.nanoTime();
         long tickIntervalNanos = TICK_INTERVAL_NANOS;

         while (nowNanos - this.lastTickNanos >= tickIntervalNanos) {
            this.animator.tick();
            this.lastTickNanos += tickIntervalNanos;
         }

         return Math.min(1.0F, (float)(nowNanos - this.lastTickNanos) / (float)tickIntervalNanos);
      }
   }

   private record PreviewEntry(WingSelection wing, WingPreviewQueue.PreviewAnimator preview, float x, float y, float width, float height, float alpha) {
   }
}
