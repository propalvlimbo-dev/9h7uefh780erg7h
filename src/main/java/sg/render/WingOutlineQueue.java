package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;

public final class WingOutlineQueue {
   private static final List<WingOutlineQueue.WingOutlineEntry> entries = new ArrayList<>();
   private static final int OUTLINE_PACKED_LIGHT = 15728880;
   private static final float RED_CHANNEL_DIVISOR = 255.0F;
   private static final float GREEN_CHANNEL_DIVISOR = 255.0F;
   private static final float BLUE_CHANNEL_DIVISOR = 255.0F;
   private static final float ALPHA_CHANNEL_DIVISOR = 255.0F;

   public static void clear() {
      entries.clear();
   }

   public static void flush(SubmitNodeCollector collector) {
      if (!entries.isEmpty() && collector != null) {
         PoseStack matrices = new PoseStack();

         for (WingOutlineQueue.WingOutlineEntry entry : entries) {
            matrices.last().set(entry.matricesEntry);
            collector.submitCustomGeometry(
               matrices,
               RenderTypes.outline(entry.type.getTextureId()),
               (geometryPose, geometryVertices) -> {
                  PoseStack geometryMatrices = new PoseStack();
                  geometryMatrices.last().set(geometryPose);
                  entry.type
                     .getModel()
                     .render(
                        entry.animator,
                        entry.tickDelta,
                        geometryMatrices,
                        geometryVertices,
                        OUTLINE_PACKED_LIGHT,
                        OverlayTexture.NO_OVERLAY,
                        ARGB.red(entry.outlineColor) / RED_CHANNEL_DIVISOR,
                        ARGB.green(entry.outlineColor) / GREEN_CHANNEL_DIVISOR,
                        ARGB.blue(entry.outlineColor) / BLUE_CHANNEL_DIVISOR,
                        ARGB.alpha(entry.outlineColor) / ALPHA_CHANNEL_DIVISOR
                     );
               }
            );
         }

         entries.clear();
      }
   }

   private WingOutlineQueue() {
   }

   public static void queue(Pose pose, AnimatedWingVariant variant, WingAnimator animator, float partialTick, int outlineColor) {
      if (outlineColor != 0) {
         entries.add(new WingOutlineQueue.WingOutlineEntry(pose.copy(), variant, animator, partialTick, outlineColor));
      }
   }

   private record WingOutlineEntry(Pose matricesEntry, AnimatedWingVariant type, WingAnimator animator, float tickDelta, int outlineColor) {
   }
}
