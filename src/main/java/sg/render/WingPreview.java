package sg.render;

import com.mojang.blaze3d.platform.Lighting.Entry;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import sg.ec.WingModel;
import sg.ec.WingPreviewRenderState;

public final class WingPreview extends PictureInPictureRenderer<WingPreviewRenderState> {
   private final @com.google.gson.annotations.SerializedName("сЙ")  Map<WingModel, GeometryBoundsConsumer> modelBounds = new WeakHashMap<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float PREVIEW_WIDTH_FACTOR = 0.96F;
   private static final float PREVIEW_HEIGHT_FACTOR = 0.96F;
   private static final float PREVIEW_X_ROTATION_DEGREES = 24.0F;
   private static final float PREVIEW_Y_ROTATION_DEGREES = 200.0F;
   private static final float MEASUREMENT_X_ROTATION_DEGREES = 24.0F;
   private static final float MEASUREMENT_Y_ROTATION_DEGREES = 200.0F;
   private static final int MEASUREMENT_PACKED_LIGHT = 15728880;
   private static final String TEXTURE_LABEL = "wing preview";
   private static final int PREVIEW_PACKED_LIGHT = 15728880;

   @Override
   protected float getTranslateY(int height, int guiScale) {
      return 0.0F;
   }

   @Override
   protected String getTextureLabel() {
      return TEXTURE_LABEL;
   }

   private GeometryBoundsConsumer measure(AnimatedWingVariant variant) {
      GeometryBoundsConsumer bounds = new GeometryBoundsConsumer();
      PoseStack matrices = new PoseStack();
      GeometryBoundsConsumer.orient(matrices, MEASUREMENT_X_ROTATION_DEGREES, MEASUREMENT_Y_ROTATION_DEGREES, false);
      WingAnimator animator = variant.createAnimator();
      animator.setFoldedIdle();

      for (int sampleIndex = 0; sampleIndex < 17; sampleIndex++) {
         variant.getModel().render(animator, 1.0F, matrices, bounds, MEASUREMENT_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

         for (int tickIndex = 0; tickIndex < 8; tickIndex++) {
            animator.tick();
         }
      }

      return bounds;
   }

   protected void renderToTexture(WingPreviewRenderState state, PoseStack matrices, SubmitNodeCollector collector) {
      Minecraft.getInstance().gameRenderer.lighting().setupFor(Entry.ENTITY_IN_UI);

      for (WingPreviewRenderState.WingDraw draw : state.wings()) {
         AnimatedWingVariant variant = draw.type();
         matrices.pushPose();
         this.modelBounds.computeIfAbsent(variant.getModel(), model -> this.measure(variant)).fit(matrices, draw.x(), draw.y(), draw.width() * PREVIEW_WIDTH_FACTOR, draw.height() * PREVIEW_HEIGHT_FACTOR);
         GeometryBoundsConsumer.orient(matrices, PREVIEW_X_ROTATION_DEGREES, PREVIEW_Y_ROTATION_DEGREES, false);
         collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(variant.getTextureId()), (geometryPose, geometryVertices) -> {
            PoseStack geometryMatrices = new PoseStack();
            geometryMatrices.last().set(geometryPose);
            variant.getModel().render(draw.animator(), draw.delta(), geometryMatrices, geometryVertices, PREVIEW_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, draw.alpha());
         });
         matrices.popPose();
      }
   }

   @Override
   public Class<WingPreviewRenderState> getRenderStateClass() {
      return WingPreviewRenderState.class;
   }
}
