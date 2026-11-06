package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import sg.ec.MaskCubeModel;
import sg.mixinbridge.ModelBending;

public final class HeadCosmeticsRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private static final float с1И = 0.01F;
   private static final float с1ь = 0.01F;
   private static final float с1ж = 0.01F;
   private static final float с1弟 = 0.01F;
   private static final double с1б = -0.5;
   private static final double с1Ы = 0.25;
   private static final float с1l = 1.005F;
   private static final float с18 = 1.005F;
   private static final float с15 = 1.005F;
   private static final double с1в = -0.25;

   public HeadCosmeticsRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> var1) {
      super(var1);
   }

   public void submit(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6) {
      Minecraft var7 = Minecraft.getInstance();
      if (var7.level != null && !var4.isInvisible) {
         if (var7.level.getEntity(var4.id) instanceof Player var8) {
            if (this.shouldRenderFor(var8, var7)) {
               boolean var22 = ClientCosmeticSelections.hasRemoteCosmeticsPayload(var8);
               String var10 = ClientCosmeticSelections.getRemoteBuiltinSelection(var8, CosmeticCategory.HATS);
               String var11 = ClientCosmeticSelections.getRemoteBuiltinSelection(var8, CosmeticCategory.MASKS);
               float var12 = var22 ? (var10 == null ? 0.0F : 1.0F) : ClientCosmeticSelections.getHatsVisibility();
               float var13 = var22 ? (var11 == null ? 0.0F : 1.0F) : ClientCosmeticSelections.getMasksVisibility();
               if (!(var12 < с1И) || !(var13 < с1ь)) {
                  Object var14 = ModelBending.衣_7fd512f1(this.getParentModel().root());
                  var1.pushPose();

                  try {
                     this.getParentModel().head.translateAndRotate(var1);
                     if (var13 >= с1ж && (var22 || !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.MASKS))) {
                        EmojiMaskVariant var15;
                        try {
                           var15 = var11 == null ? ClientCosmeticSelections.getSelectedMask() : EmojiMaskVariant.valueOf(var11);
                        } catch (IllegalArgumentException var20) {
                           var15 = null;
                        }

                        if (var15 != null) {
                           var2.submitCustomGeometry(var1, RenderTypes.entityCutout(var15.getTextureId()), (var2x, var3x) -> {
                              PoseStack var4x = new PoseStack();
                              var4x.mulPose(var2x.pose());
                              MaskCubeModel.К(var4x, var3x, var3, OverlayTexture.NO_OVERLAY, var13);
                           });
                        }
                     }

                     if (var12 >= с1弟 && (var22 || !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.HATS))) {
                        HatDefinition var23 = HatCatalog.getHat(var10 == null ? ClientCosmeticSelections.getSelectedHatKey() : var10);
                        if (var23 != null) {
                           var1.pushPose();
                           var1.translate(0.0, с1б, 0.0);
                           var1.translate(0.0, с1Ы, 0.0);
                           var1.scale(с1l, с18, с15);
                           var1.translate(0.0, с1в, 0.0);
                           var2.submitCustomGeometry(var1, RenderTypes.entityTranslucent(var23.texture()), (var3x, var4x) -> {
                              PoseStack var5x = new PoseStack();
                              var5x.mulPose(var3x.pose());
                              CosmeticModelRenderer.render(var5x, var4x, var23.model(), var3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, var12);
                           });
                           var1.popPose();
                        }
                     }
                  } finally {
                     var1.popPose();
                     ModelBending.method_2(var14);
                  }
               }
            }
         }
      }
   }

   private boolean shouldRenderFor(Player var1, Minecraft var2) {
      return ClientCosmeticSelections.shouldShowCosmeticsForPlayer(var1, var2);
   }
}
