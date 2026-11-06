package dev.rose.gui.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.rose.util.ColorUtil;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;


public final class PlayerPreview {
   private static final float FOOT_PAD = 2.0F;
   private PlayerModel wide;
   private PlayerModel slim;

   public static void register() {
      SpecialGuiElementRegistry.register(context -> new PlayerPreview.Renderer(context.vertexConsumers()));
   }

   public void render(GuiGraphics g, float centerX, float feetY, float height, float yaw, float headYaw, float headPitch, float time, float alpha) {
      if (!(alpha <= 0.01F) && !(height <= 1.0F)) {
         Minecraft mc = Minecraft.getInstance();
         PlayerSkin skin = (PlayerSkin)mc.getSkinManager().createLookup(mc.getGameProfile(), false).get();
         PlayerModel model = skin.model() == PlayerModelType.SLIM ? this.slim() : this.wide();
         pose(model, headYaw, headPitch, time * 20.0F);
         float halfWidth = height * 0.5F;
         int x0 = Mth.floor(centerX - halfWidth);
         int x1 = Mth.ceil(centerX + halfWidth);
         int y0 = Mth.floor(feetY - height * 1.1F);
         int y1 = Mth.ceil(feetY + 2.0F);
         float scale = height / 2.0F;
         ScreenRectangle scissor = g.scissorStack.peek();
         ScreenRectangle rect = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0);
         ScreenRectangle bounds = scissor != null ? scissor.intersection(rect) : rect;
         if (bounds != null) {
            float footLift = (y1 - feetY) / scale;
            float centerShift = (centerX - (x0 + x1) / 2.0F) / scale;
            g.guiRenderState
               .submitPicturesInPictureState(
                  new PlayerPreview.State(model, skin.body().texturePath(), yaw, alpha, footLift, centerShift, x0, y0, x1, y1, scale, scissor, bounds)
               );
         }
      }
   }

   private static void pose(PlayerModel model, float headYaw, float headPitch, float ticks) {
      Minecraft mc = Minecraft.getInstance();
      model.root().getAllParts().forEach(part -> part.visible = true);
      model.hat.visible = mc.options.isModelPartEnabled(PlayerModelPart.HAT);
      model.jacket.visible = mc.options.isModelPartEnabled(PlayerModelPart.JACKET);
      model.leftSleeve.visible = mc.options.isModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);
      model.rightSleeve.visible = mc.options.isModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
      model.leftPants.visible = mc.options.isModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG);
      model.rightPants.visible = mc.options.isModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG);
      model.head.xRot = headPitch * (float) (Math.PI / 180.0);
      model.head.yRot = headYaw * (float) (Math.PI / 180.0);
      model.head.zRot = 0.0F;
      model.body.xRot = model.body.yRot = model.body.zRot = 0.0F;
      model.rightLeg.xRot = model.rightLeg.yRot = model.rightLeg.zRot = 0.0F;
      model.leftLeg.xRot = model.leftLeg.yRot = model.leftLeg.zRot = 0.0F;
      float swing = Mth.cos(ticks * 0.09F) * 0.05F + 0.05F;
      float lift = Mth.sin(ticks * 0.067F) * 0.05F;
      model.rightArm.xRot = lift;
      model.rightArm.yRot = 0.0F;
      model.rightArm.zRot = swing;
      model.leftArm.xRot = -lift;
      model.leftArm.yRot = 0.0F;
      model.leftArm.zRot = -swing;
   }

   private PlayerModel wide() {
      if (this.wide == null) {
         this.wide = new PlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
      }

      return this.wide;
   }

   private PlayerModel slim() {
      if (this.slim == null) {
         this.slim = new PlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
      }

      return this.slim;
   }

   static final class Renderer extends PictureInPictureRenderer<PlayerPreview.State> {
      Renderer(MultiBufferSource.BufferSource buffers) {
         super(buffers);
      }

      public Class<PlayerPreview.State> getRenderStateClass() {
         return PlayerPreview.State.class;
      }

      protected void renderToTexture(PlayerPreview.State state, PoseStack pose) {
         Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
         pose.translate(state.centerShift(), -state.footLift(), 0.0F);
         pose.mulPose(Axis.YP.rotationDegrees(state.yaw()));
         pose.translate(0.0F, -1.5F, 0.0F);
         state.model()
            .renderToBuffer(
               pose,
               this.bufferSource.getBuffer(RenderTypes.entityTranslucent(state.texture())),
               15728880,
               OverlayTexture.NO_OVERLAY,
               ColorUtil.withAlpha(-1, Math.round(state.alpha() * 255.0F))
            );
      }

      protected String getTextureLabel() {
         return "Rose player preview";
      }
   }

   record State(
      PlayerModel model,
      Identifier texture,
      float yaw,
      float alpha,
      float footLift,
      float centerShift,
      int x0,
      int y0,
      int x1,
      int y1,
      float scale,
      @Nullable ScreenRectangle scissorArea,
      @Nullable ScreenRectangle bounds
   ) implements PictureInPictureRenderState {
      public int x0() {
         return this.x0;
      }

      public int y0() {
         return this.y0;
      }

      public int x1() {
         return this.x1;
      }

      public int y1() {
         return this.y1;
      }

      public float scale() {
         return this.scale;
      }

      @Nullable
      public ScreenRectangle scissorArea() {
         return this.scissorArea;
      }

      @Nullable
      public ScreenRectangle bounds() {
         return this.bounds;
      }
   }
}
