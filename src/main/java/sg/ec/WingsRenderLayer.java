package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import sg.mixinbridge.ModelBending;
import sg.render.WingOutlineQueue;
import sg.render.WingSelection;
import sg.render.AnimatedWingVariant;
import sg.render.CosmeticCategory;
import sg.render.PlayerWingState;
import sg.render.PlayerEmoteController;
import sg.render.ClientCosmeticSelections;

public final class WingsRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private final @com.google.gson.annotations.SerializedName("П")  Map<UUID, PlayerWingState> playerWingStates = new HashMap<>();
   private static final float MIN_VISIBLE_ALPHA = 0.01F;
   private static final double WING_Y_OFFSET = -0.0625;
   private static final double CHEST_EQUIPMENT_Z_OFFSET = 0.0625;

   public void submit(PoseStack matrices, SubmitNodeCollector collector, int packedLight, AvatarRenderState state, float yRot, float xRot) {
      Minecraft client = Minecraft.getInstance();
      if (client.level != null) {
         if (client.level.getEntity(state.id) instanceof Player player) {
            String remoteSelectionName = ClientCosmeticSelections.getRemoteBuiltinSelection(player, CosmeticCategory.WINGS);
            if (!ClientCosmeticSelections.hasRemoteCosmeticsPayload(player) || remoteSelectionName != null) {
               if (remoteSelectionName != null || !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.WINGS)) {
                  float alpha = remoteSelectionName == null ? ClientCosmeticSelections.getWingsVisibility() : 1.0F;
                  if (!(alpha < MIN_VISIBLE_ALPHA) && ClientCosmeticSelections.shouldShowWingsForPlayer(player, client)) {
                     AnimatedWingVariant variant;
                     try {
                        variant = remoteSelectionName == null ? ClientCosmeticSelections.getSelectedWings().getAnimatedVariant() : WingSelection.valueOf(remoteSelectionName).getAnimatedVariant();
                     } catch (IllegalArgumentException invalidSelection) {
                        return;
                     }

                     PlayerWingState wingState = this.playerWingStates.computeIfAbsent(player.getUUID(), playerId -> new PlayerWingState(variant));
                     if (wingState.getVariant() != variant) {
                        wingState = new PlayerWingState(variant);
                        this.playerWingStates.put(player.getUUID(), wingState);
                     }

                     wingState.updateForPlayerTick(player);
                     Object previousBendingOwner = ModelBending.衣_7fd512f1(this.getParentModel().root());
                     matrices.pushPose();

                     try {
                        matrices.translate(0.0, WING_Y_OFFSET, player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.0 : CHEST_EQUIPMENT_Z_OFFSET);
                        this.getParentModel().body.translateAndRotate(matrices);
                        PlayerEmoteController.э(state, matrices);
                        PlayerWingState renderWingState = wingState;
                        float renderAlpha = alpha;
                        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
                        boolean hasOutline = state.outlineColor != 0;
                        boolean invisible = state.isInvisible;
                        if (!invisible) {
                           boolean rose = variant == AnimatedWingVariant.ROSE;
                           RenderType wingRenderType = rose
                              ? RenderTypes.entityTranslucent(variant.getTextureId())
                              : RenderTypes.entityCutout(variant.getTextureId());
                           int wingLight = rose ? LightTexture.FULL_BRIGHT : packedLight;
                           collector.submitCustomGeometry(matrices, wingRenderType, (geometryPose, geometryVertices) -> {
                              PoseStack geometryMatrices = new PoseStack();
                              geometryMatrices.mulPose(geometryPose.pose());
                              variant.getModel().render(renderWingState.getAnimator(), partialTick, geometryMatrices, geometryVertices, wingLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, renderAlpha);
                           });
                        }

                        if (hasOutline) {
                           WingOutlineQueue.queue(matrices.last(), variant, renderWingState.getAnimator(), partialTick, state.outlineColor);
                        }
                     } finally {
                        matrices.popPose();
                        ModelBending.method_2(previousBendingOwner);
                     }
                  }
               }
            }
         }
      }
   }

   public WingsRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
      super(parent);
   }
}
