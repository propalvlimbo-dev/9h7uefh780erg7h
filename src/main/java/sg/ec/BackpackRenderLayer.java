package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import sg.mixinbridge.ModelBending;
import sg.render.BackpackModel;
import sg.render.CosmeticCategory;
import sg.render.PlayerEmoteController;
import sg.render.BackpackColorVariant;
import sg.render.ClientCosmeticSelections;

public final class BackpackRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private static final float MIN_VISIBLE_ALPHA = 0.01F;
   private static final double BACKPACK_Z_OFFSET = 0.18;
   private static final float BACKPACK_Y_ROTATION_DEGREES = 180.0F;
   private static final float BACKPACK_X_SCALE = 0.8F;
   private static final float BACKPACK_Y_SCALE = 0.8F;
   private static final float BACKPACK_Z_SCALE = 0.8F;

   private boolean visibleFor(Player player, Minecraft client) {
      return ClientCosmeticSelections.shouldShowCosmeticsForPlayer(player, client);
   }

   public void submit(PoseStack matrices, SubmitNodeCollector collector, int packedLight, AvatarRenderState state, float yRot, float xRot) {
      Minecraft client = Minecraft.getInstance();
      if (client.level != null && !state.isInvisible && client.level.getEntity(state.id) instanceof Player player && this.visibleFor(player, client)) {
         String remoteSelectionName = ClientCosmeticSelections.getRemoteBuiltinSelection(player, CosmeticCategory.BACKPACKS);
         if ((!ClientCosmeticSelections.hasRemoteCosmeticsPayload(player) || remoteSelectionName != null) && (remoteSelectionName != null || !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.BACKPACKS))) {
            float alpha = remoteSelectionName == null ? ClientCosmeticSelections.getBackpacksVisibility() : 1.0F;
            if (!(alpha < MIN_VISIBLE_ALPHA)) {
               BackpackColorVariant variant;
               try {
                  variant = remoteSelectionName == null
                     ? ClientCosmeticSelections.getSelectedBackpackColor()
                     : BackpackColorVariant.valueOf(remoteSelectionName);
               } catch (IllegalArgumentException invalidSelection) {
                  return;
               }

               Object previousBendingOwner = ModelBending.衣_7fd512f1(this.getParentModel().root());
               matrices.pushPose();

               try {
                  this.getParentModel().body.translateAndRotate(matrices);
                  PlayerEmoteController.э(state, matrices);
                  matrices.translate(0.0, 0.0, BACKPACK_Z_OFFSET);
                  matrices.mulPose(Axis.YP.rotationDegrees(BACKPACK_Y_ROTATION_DEGREES));
                  matrices.scale(BACKPACK_X_SCALE, BACKPACK_Y_SCALE, BACKPACK_Z_SCALE);
                  collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(BackpackModel.BODY_TEXTURE), (bodyPose, bodyVertices) -> {
                     PoseStack bodyMatrices = new PoseStack();
                     bodyMatrices.mulPose(bodyPose.pose());
                     BackpackModel.renderBody(bodyMatrices, bodyVertices, packedLight, OverlayTexture.NO_OVERLAY, variant, alpha);
                  });
                  collector.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(BackpackModel.OVERLAY_TEXTURE), (detailPose, detailVertices) -> {
                     PoseStack detailMatrices = new PoseStack();
                     detailMatrices.mulPose(detailPose.pose());
                     BackpackModel.renderOverlay(detailMatrices, detailVertices, packedLight, OverlayTexture.NO_OVERLAY, alpha);
                  });
               } finally {
                  matrices.popPose();
                  ModelBending.method_2(previousBendingOwner);
               }
            }
         }
      }
   }

   public BackpackRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
      super(parent);
   }
}
