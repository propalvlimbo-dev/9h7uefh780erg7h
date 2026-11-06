package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import sg.mixinbridge.ModelBending;

public final class AttachedCosmeticsRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private static final float field_4В = 0.625F;
   private static final float field_4Я = 0.75F;
   private static final float ВыТ = 0.01F;
   private static final float ВыО = 0.625F;
   private static final float ВыФ = 0.625F;
   private static final float ВыЙ = 0.75F;
   private static final float Вык = 0.75F;
   private static final float Выг = 0.625F;
   private static final float Вы0 = 0.625F;
   private static final float Вых = 0.75F;
   private static final float ВыУ = 0.75F;
   private static final float ВыЗ = -90.0F;
   private static final float ВыЕ = 180.0F;
   private static final float ВыП = 0.5F;
   private static final float Вы西 = 0.51F;
   private static final float Выд = 0.5F;
   private static final long ВыБ = 60000000000L;
   private static final float ВыС = 1.0E9F;

   public AttachedCosmeticsRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> var1) {
      super(var1);
   }

   private boolean visibleFor(Player var1, Minecraft var2) {
      return ClientCosmeticSelections.shouldShowCosmeticsForPlayer(var1, var2);
   }

   private void render(CosmeticDefinition var1, AvatarRenderState var2, PoseStack var3, SubmitNodeCollector var4, int var5, float var6) {
      PlayerModel var7 = this.getParentModel();
      switch (var1.attach()) {
         case HEAD:
            this.renderOn(var7.head, var1, var2, var3, var4, var5, var6, false, 0.0F, false);
            break;
         case BODY:
            this.renderOn(var7.body, var1, var2, var3, var4, var5, var6, false, 0.0F, false);
            break;
         case ARMS:
            this.renderOn(var7.rightArm, var1, var2, var3, var4, var5, var6, true, ВыО, false);
            this.renderOn(var7.leftArm, var1, var2, var3, var4, var5, var6, true, ВыФ, false);
            break;
         case LEGS:
            this.renderOn(var7.rightLeg, var1, var2, var3, var4, var5, var6, true, ВыЙ, true);
            this.renderOn(var7.leftLeg, var1, var2, var3, var4, var5, var6, true, Вык, true);
            break;
         case PAWS:
            this.renderOn(var7.rightArm, var1, var2, var3, var4, var5, var6, true, Выг, false);
            this.renderOn(var7.leftArm, var1, var2, var3, var4, var5, var6, true, Вы0, false);
            this.renderOn(var7.rightLeg, var1, var2, var3, var4, var5, var6, true, Вых, true);
            this.renderOn(var7.leftLeg, var1, var2, var3, var4, var5, var6, true, ВыУ, true);
            break;
         case STOCKINGS:
            this.renderOn(var7.rightLeg, var1, var2, var3, var4, var5, var6, true, 0.0F, false);
            this.renderOn(var7.leftLeg, var1, var2, var3, var4, var5, var6, true, 0.0F, false);
         case WORLD:
      }
   }

   private void renderOn(
      ModelPart var1,
      CosmeticDefinition var2,
      AvatarRenderState var3,
      PoseStack var4,
      SubmitNodeCollector var5,
      int var6,
      float var7,
      boolean var8,
      float var9,
      boolean var10
   ) {
      var4.pushPose();
      var1.translateAndRotate(var4);
      PlayerModel var11 = this.getParentModel();
      if (var1 == var11.body) {
         PlayerEmoteController.э(var3, var4);
      } else if (var1 == var11.rightArm) {
         PlayerEmoteController.衣(var3, HumanoidArm.RIGHT, var4);
      } else if (var1 == var11.leftArm) {
         PlayerEmoteController.衣(var3, HumanoidArm.LEFT, var4);
      } else if (var10 && var1 == var11.rightLeg) {
         PlayerEmoteController.衣(var3, false, var4);
      } else if (var10 && var1 == var11.leftLeg) {
         PlayerEmoteController.衣(var3, true, var4);
      }

      if (var8) {
         var4.translate(0.0F, var9, 0.0F);
         if (var10) {
            var4.mulPose(Axis.XP.rotationDegrees(ВыЗ));
         }

         var4.scale(var2.scale(), var2.scale(), var2.scale());
         var4.translate(var2.x(), var2.y(), var2.z());
      } else {
         var4.mulPose(Axis.ZP.rotationDegrees(ВыЕ));
         var4.scale(var2.scale(), var2.scale(), var2.scale());
         var4.translate(var2.x(), var2.y(), var2.z());
         var4.translate(ВыП, Вы西, Выд);
      }

      float var12 = (float)(System.nanoTime() % ВыБ) / ВыС;
      var5.submitCustomGeometry(var4, RenderTypes.entityTranslucent(var2.texture()), (var4x, var5x) -> {
         PoseStack var6x = new PoseStack();
         var6x.mulPose(var4x.pose());
         CosmeticModelRenderer.render(var6x, var5x, var2.model(), var2.animation(), var12, var6, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, var7);
      });
      var4.popPose();
   }

   public void submit(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6) {
      Minecraft var7 = Minecraft.getInstance();
      if (var7.level != null && !var4.isInvisible) {
         if (var7.level.getEntity(var4.id) instanceof Player var8) {
            if (this.visibleFor(var8, var7)) {
               Object var19 = ModelBending.衣_7fd512f1(this.getParentModel().root());

               try {
                  for (CosmeticCategory var13 : CosmeticCategory.values()) {
                     CosmeticDefinition var14 = ClientCosmeticSelections.getCatalogModelForPlayer(var8, var13);
                     float var15 = var7.player != null && var8.getUUID().equals(var7.player.getUUID()) ? ClientCosmeticSelections.getCategoryVisibility(var13) : 1.0F;
                     if (var14 != null && !(var15 < ВыТ) && !var14.attach().isWorldAttachment()) {
                        this.render(var14, var4, var1, var2, var3, var15);
                     }
                  }
               } finally {
                  ModelBending.method_2(var19);
               }
            }
         }
      }
   }
}
