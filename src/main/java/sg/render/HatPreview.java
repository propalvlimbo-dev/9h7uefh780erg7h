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
import sg.ec.HatPreviewRenderState;

public final class HatPreview extends PictureInPictureRenderer<HatPreviewRenderState> {
   private final @com.google.gson.annotations.SerializedName("сБ")  Map<CosmeticModel, GeometryBoundsConsumer> modelBounds = new WeakHashMap<>();
   private static final float HORIZONTAL_CENTER_FACTOR = 0.5F;
   private static final float PREVIEW_X_ROTATION_DEGREES = 15.0F;
   private static final float PREVIEW_Y_ROTATION_DEGREES = 205.0F;
   private static final float MEASUREMENT_X_ROTATION_DEGREES = 15.0F;
   private static final float MEASUREMENT_Y_ROTATION_DEGREES = 205.0F;
   private static final int MEASUREMENT_PACKED_LIGHT = 15728880;
   private static final String TEXTURE_LABEL = "hat preview";
   private static final int PREVIEW_PACKED_LIGHT = 15728880;

   protected void renderToTexture(HatPreviewRenderState state, PoseStack matrices, SubmitNodeCollector collector) {
      Minecraft.getInstance().gameRenderer.lighting().setupFor(Entry.ENTITY_IN_UI);
      float halfTargetWidth = (state.x1() - state.x0()) * HORIZONTAL_CENTER_FACTOR;

      for (HatPreviewRenderState.HatDraw draw : state.hats()) {
         HatDefinition hat = draw.hat();
         matrices.pushPose();
         this.modelBounds.computeIfAbsent(hat.model(), this::measure).fit(matrices, draw.x() - halfTargetWidth, draw.y(), draw.width(), draw.height());
         GeometryBoundsConsumer.orient(matrices, PREVIEW_X_ROTATION_DEGREES, PREVIEW_Y_ROTATION_DEGREES, hat.model().absolutePivotTransforms);
         collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(hat.texture()), (geometryPose, geometryVertices) -> {
            PoseStack geometryMatrices = new PoseStack();
            geometryMatrices.last().set(geometryPose);
            CosmeticModelRenderer.render(geometryMatrices, geometryVertices, hat.model(), PREVIEW_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, draw.alpha());
         });
         matrices.popPose();
      }
   }

   @Override
   protected String getTextureLabel() {
      return TEXTURE_LABEL;
   }

   @Override
   public Class<HatPreviewRenderState> getRenderStateClass() {
      return HatPreviewRenderState.class;
   }

   private GeometryBoundsConsumer measure(CosmeticModel model) {
      GeometryBoundsConsumer bounds = new GeometryBoundsConsumer();
      PoseStack matrices = new PoseStack();
      GeometryBoundsConsumer.orient(matrices, MEASUREMENT_X_ROTATION_DEGREES, MEASUREMENT_Y_ROTATION_DEGREES, model.absolutePivotTransforms);
      CosmeticModelRenderer.render(matrices, bounds, model, MEASUREMENT_PACKED_LIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      return bounds;
   }

   @Override
   protected float getTranslateY(int height, int guiScale) {
      return 0.0F;
   }
}
