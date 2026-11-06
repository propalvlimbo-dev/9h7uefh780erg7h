package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.HatsModule;
import dev.rose.module.impl.cosmetic.WingsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;


public final class CosmeticsLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private final RecordingBuffers buffers = new RecordingBuffers();

   public CosmeticsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
      super(parent);
   }

   public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null && state.id == player.getId() && !state.isInvisible) {
         float age = state.ageInTicks;
         HatsModule hats = Modules.hats();
         if (hats != null && hats.isEnabled()) {
            pose.pushPose();

            try {
               ((PlayerModel)this.getParentModel()).head.translateAndRotate(pose);
               hats.render(pose, this.buffers, light, age);
            } catch (RuntimeException e) {
               Modules.fail(hats, e);
            } finally {
               pose.popPose();
            }
         }

         WingsModule wings = Modules.wings();
         if (wings != null && wings.isEnabled() && !state.isFallFlying) {
            pose.pushPose();

            try {
               ((PlayerModel)this.getParentModel()).body.translateAndRotate(pose);
               wings.render(pose, this.buffers, light, player, age);
            } catch (RuntimeException e) {
               Modules.fail(wings, e);
            } finally {
               pose.popPose();
            }
         }

         this.buffers.submit(collector);
      }
   }
}
