package sg.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.ec.HatPreviewRenderState;

/** Collects cosmetic hat models into one clipped 3D GUI preview pass. */
public final class HatPreviewQueue {
   private final List<PreviewEntry> previews = new ArrayList<>();

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
      List<HatPreviewRenderState.HatDraw> draws = new ArrayList<>(this.previews.size());
      for (PreviewEntry preview : this.previews) {
         float alpha = Math.clamp(preview.alpha, 0.0F, 1.0F);
         if (alpha > 0.0F) {
            draws.add(
               new HatPreviewRenderState.HatDraw(
                  preview.hat,
                  alpha,
                  GuiPreviewUtil.centeredLocalX(preview.x, x0, x1),
                  preview.y - y0,
                  preview.width,
                  preview.height
               )
            );
         }
      }

      if (draws.isEmpty()) {
         return;
      }

      GuiPreviewUtil.submit(
         graphics,
         new HatPreviewRenderState(List.copyOf(draws), x0, y0, x1, y1, 1.0F, null)
      );
   }

   public void queuePreview(HatDefinition hat, float x, float y, float width, float height, float alpha) {
      GuiPreviewUtil.Rect screenRect = GuiPreviewUtil.transformRect(x, y, width, height);
      this.previews.add(
         new PreviewEntry(hat, screenRect.centerX(), screenRect.centerY(), alpha, screenRect.width(), screenRect.height())
      );
   }

   private record PreviewEntry(HatDefinition hat, float x, float y, float alpha, float width, float height) {
   }
}
