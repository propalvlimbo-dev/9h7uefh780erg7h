package sg.render;

import com.mojang.blaze3d.platform.Lighting.Entry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import sg.gui.BackpackPreviewRenderState;

public final class BackpackPreview extends PictureInPictureRenderer<BackpackPreviewRenderState> {
   private @com.google.gson.annotations.SerializedName("В")  GeometryBoundsConsumer bounds;
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float PREVIEW_X_ROTATION_DEGREES = 15.0F;
   private static final float PREVIEW_Y_ROTATION_DEGREES = 205.0F;
   private static final float MEASUREMENT_X_ROTATION_DEGREES = 15.0F;
   private static final float MEASUREMENT_Y_ROTATION_DEGREES = 205.0F;
   private static final int MEASUREMENT_BODY_PACKED_LIGHT = 15728880;
   private static final int MEASUREMENT_DETAIL_PACKED_LIGHT = 15728880;
   private static final String TEXTURE_LABEL = "backpack preview";
   private static final int PREVIEW_DETAIL_PACKED_LIGHT = 15728880;
   private static final int PREVIEW_BODY_PACKED_LIGHT = 15728880;

   protected void renderToTexture(BackpackPreviewRenderState state, PoseStack matrices, SubmitNodeCollector collector) {
      Minecraft.getInstance().gameRenderer.lighting().setupFor(Entry.ENTITY_IN_UI);
      float halfTargetWidth = (state.x1() - state.x0()) * HORIZONTAL_CENTER_FACTOR;

      for (BackpackPreviewRenderState.Preview draw : state.previews()) {
         matrices.pushPose();
         if (this.bounds == null) {
            this.bounds = this.measure(draw.variant());
         }

         this.bounds.fit(matrices, draw.x() - halfTargetWidth, draw.y(), draw.width(), draw.height());
         GeometryBoundsConsumer.orient(matrices, PREVIEW_X_ROTATION_DEGREES, PREVIEW_Y_ROTATION_DEGREES, false);
         collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(BackpackModel.BODY_TEXTURE), (bodyPose, bodyVertices) -> {
            PoseStack bodyMatrices = new PoseStack();
            bodyMatrices.last().set(bodyPose);
            BackpackModel.renderBody(bodyMatrices, bodyVertices, PREVIEW_BODY_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, draw.variant(), draw.alpha());
         });
         collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(BackpackModel.OVERLAY_TEXTURE), (detailPose, detailVertices) -> {
            PoseStack detailMatrices = new PoseStack();
            detailMatrices.last().set(detailPose);
            BackpackModel.renderOverlay(detailMatrices, detailVertices, PREVIEW_DETAIL_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, draw.alpha());
         });
         matrices.popPose();
      }
   }

   @Override
   protected float getTranslateY(int height, int guiScale) {
      return 0.0F;
   }

   @Override
   public Class<BackpackPreviewRenderState> getRenderStateClass() {
      return BackpackPreviewRenderState.class;
   }

   private GeometryBoundsConsumer measure(BackpackColorVariant variant) {
      GeometryBoundsConsumer bounds = new GeometryBoundsConsumer();
      PoseStack matrices = new PoseStack();
      GeometryBoundsConsumer.orient(matrices, MEASUREMENT_X_ROTATION_DEGREES, MEASUREMENT_Y_ROTATION_DEGREES, false);
      BackpackModel.renderBody(matrices, bounds, MEASUREMENT_BODY_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, variant, 1.0F);
      BackpackModel.renderOverlay(matrices, bounds, MEASUREMENT_DETAIL_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, 1.0F);
      return bounds;
   }

   @Override
   protected String getTextureLabel() {
      return TEXTURE_LABEL;
   }
}
