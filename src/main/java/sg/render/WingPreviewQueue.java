package sg.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import sg.render.pipeline.UiDrawing;

public final class WingPreviewQueue {
   private final @com.google.gson.annotations.SerializedName("衣")  Map<WingSelection, WingPreviewQueue.PreviewAnimator> previewAnimators = new EnumMap<>(WingSelection.class);
   private final @com.google.gson.annotations.SerializedName("И")  List<WingPreviewQueue.PreviewEntry> previews = new ArrayList<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float VERTICAL_CENTER_FACTOR = 0.5F;
   private static final float SPRITE_FACTOR = 0.86F;
   /** Готовые 3D-рендеры моделей: показываются вместо UV-атласа, чтобы в ячейке читался силуэт. */
   private static final Map<WingSelection, Identifier> PREVIEW_SPRITES = Map.of(
      WingSelection.ROSE, Identifier.fromNamespaceAndPath("rose", "textures/wings/rose_wings_preview.png")
   );

   public void queuePreview(WingSelection wings, float x, float y, float width, float height, float alpha) {
      WingPreviewQueue.PreviewAnimator previewAnimator = this.previewAnimators.computeIfAbsent(wings, variant -> new WingPreviewQueue.PreviewAnimator(variant.getAnimatedVariant().createAnimator()));
      this.previews.add(new WingPreviewQueue.PreviewEntry(wings, previewAnimator, x + width * HORIZONTAL_CENTER_FACTOR, y + height * VERTICAL_CENTER_FACTOR, width, height, alpha));
   }

   public void clearPreviews() {
      this.previews.clear();
   }

   public void submitPreviews(GuiGraphicsExtractor graphics, float clipX, float clipY, float clipWidth, float clipHeight) {
      for (WingPreviewQueue.PreviewEntry preview : this.previews) {
         int alpha = Math.clamp(Math.round(preview.alpha * 255.0F), 0, 255);
         if (alpha <= 0) {
            continue;
         }

         float side = Math.min(preview.width, preview.height) * SPRITE_FACTOR;
         float spriteX = clipX + preview.x - side * 0.5F;
         float spriteY = clipY + preview.y - side * 0.5F;
         Identifier sprite = PREVIEW_SPRITES.getOrDefault(preview.wing, preview.wing.getAnimatedVariant().getTextureId());
         UiDrawing.drawTexture(spriteX, spriteY, side, side, 0.0F, alpha << 24 | 0xFFFFFF, sprite);
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
