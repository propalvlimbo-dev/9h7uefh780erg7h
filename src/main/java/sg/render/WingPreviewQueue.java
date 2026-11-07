package sg.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.ec.WingPreviewRenderState;

/** Collects animated 3D wing models and submits them through Minecraft's GUI PiP renderer. */
public final class WingPreviewQueue {
   private static final long TICK_INTERVAL_NANOS = 50_000_000L;
   private final Map<WingSelection, PreviewAnimator> previewAnimators = new EnumMap<>(WingSelection.class);
   private final List<PreviewEntry> previews = new ArrayList<>();

   public void queuePreview(WingSelection wings, float x, float y, float width, float height, float alpha) {
      PreviewAnimator animator = this.previewAnimators.computeIfAbsent(wings, variant -> new PreviewAnimator(variant.getAnimatedVariant().createAnimator()));
      GuiPreviewUtil.Rect screenRect = GuiPreviewUtil.transformRect(x, y, width, height);
      this.previews.add(
         new PreviewEntry(wings, animator, screenRect.centerX(), screenRect.centerY(), screenRect.width(), screenRect.height(), alpha)
      );
   }

   public void clearPreviews() {
      this.previews.clear();
   }

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      if (this.previews.isEmpty() || clipWidth <= 0.0F || clipHeight <= 0.0F) {
         return;
      }

      GuiPreviewUtil.Rect viewport = GuiPreviewUtil.transformRect(clipX, clipY, clipWidth, clipHeight);
      int x0 = GuiPreviewUtil.floor(viewport.x());
      int y0 = GuiPreviewUtil.floor(viewport.y());
      int x1 = GuiPreviewUtil.ceil(viewport.x() + viewport.width());
      int y1 = GuiPreviewUtil.ceil(viewport.y() + viewport.height());
      List<WingPreviewRenderState.WingDraw> draws = new ArrayList<>(this.previews.size());
      for (PreviewEntry preview : this.previews) {
         float alpha = Math.clamp(preview.alpha, 0.0F, 1.0F);
         if (alpha <= 0.0F) {
            continue;
         }

         draws.add(
            new WingPreviewRenderState.WingDraw(
               preview.wing.getAnimatedVariant(),
               preview.animator.animator,
               preview.animator.advanceAndGetPartialTick(),
               alpha,
               preview.x - x0,
               preview.y - y0,
               preview.width,
               preview.height
            )
         );
      }

      if (draws.isEmpty()) {
         return;
      }

      GuiPreviewUtil.submit(
         graphics,
         new WingPreviewRenderState(List.copyOf(draws), x0, y0, x1, y1, 1.0F, null)
      );
   }

   private static final class PreviewAnimator {
      private final WingAnimator animator;
      private long lastTickNanos = System.nanoTime();

      private PreviewAnimator(WingAnimator animator) {
         this.animator = animator;
         animator.setFoldedIdle();
      }

      private float advanceAndGetPartialTick() {
         long nowNanos = System.nanoTime();
         while (nowNanos - this.lastTickNanos >= TICK_INTERVAL_NANOS) {
            this.animator.tick();
            this.lastTickNanos += TICK_INTERVAL_NANOS;
         }

         return Math.min(1.0F, (float)(nowNanos - this.lastTickNanos) / (float)TICK_INTERVAL_NANOS);
      }
   }

   private record PreviewEntry(WingSelection wing, PreviewAnimator animator, float x, float y, float width, float height, float alpha) {
   }
}
