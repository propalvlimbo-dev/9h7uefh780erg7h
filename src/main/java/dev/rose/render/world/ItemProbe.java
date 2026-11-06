package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;


public final class ItemProbe implements SubmitNodeCollector {
   @Nullable
   private SubmitNodeCollector target;
   @Nullable
   private RenderType silhouetteType;
   private int color;
   private final Matrix4f pose = new Matrix4f();
   private boolean found;

   public ItemProbe capture() {
      this.target = null;
      this.silhouetteType = null;
      this.found = false;
      return this;
   }

   public ItemProbe silhouette(SubmitNodeCollector target, RenderType type, int color) {
      this.target = target;
      this.silhouetteType = type;
      this.color = color;
      this.found = false;
      return this;
   }

   public boolean found() {
      return this.found;
   }

   public Matrix4f pose() {
      return this.pose;
   }

   public void submitItem(
      PoseStack poseStack,
      ItemDisplayContext context,
      int light,
      int overlay,
      int outlineColor,
      int[] tints,
      List<BakedQuad> quads,
      RenderType renderType,
      ItemStackRenderState.FoilType foil
   ) {
      if (!this.found) {
         this.pose.set(poseStack.last().pose());
         this.found = true;
      }

      if (this.target != null && this.silhouetteType != null && atlas(renderType)) {
         int argb = this.color;
         List<BakedQuad> copy = List.copyOf(quads);
         RenderType type = renderType == Sheets.translucentItemSheet() ? RoseRenderTypes.ITEM_SILHOUETTE_ITEMS : this.silhouetteType;
         this.target.submitCustomGeometry(poseStack, type, (last, consumer) -> {
            ItemProbe.Flat flat = new ItemProbe.Flat(consumer, argb);

            for (BakedQuad quad : copy) {
               flat.putBulkData(last, quad, 1.0F, 1.0F, 1.0F, 1.0F, light, overlay);
            }
         });
      }
   }

   private static boolean atlas(RenderType type) {
      return type == Sheets.translucentItemSheet() || type == Sheets.translucentBlockItemSheet() || type == Sheets.cutoutBlockSheet() || type == Sheets.solidBlockSheet();
   }

   public OrderedSubmitNodeCollector order(int order) {
      return this;
   }

   public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
   }

   public void submitNameTag(
      PoseStack poseStack, @Nullable Vec3 position, int offset, Component text, boolean discrete, int light, double distance, CameraRenderState camera
   ) {
   }

   public void submitText(
      PoseStack poseStack, float x, float y, FormattedCharSequence text, boolean shadow, Font.DisplayMode mode, int light, int color, int background, int outline
   ) {
   }

   public void submitFlame(PoseStack poseStack, EntityRenderState state, Quaternionf rotation) {
   }

   public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leash) {
   }

   public <S> void submitModel(
      Model<? super S> model,
      S state,
      PoseStack poseStack,
      RenderType renderType,
      int light,
      int overlay,
      int color,
      @Nullable TextureAtlasSprite sprite,
      int outline,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumbling
   ) {
   }

   public void submitModelPart(
      ModelPart part,
      PoseStack poseStack,
      RenderType renderType,
      int light,
      int overlay,
      @Nullable TextureAtlasSprite sprite,
      boolean sheeted,
      boolean glint,
      int color,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumbling,
      int outline
   ) {
   }

   public void submitBlock(PoseStack poseStack, BlockState state, int light, int overlay, int outline) {
   }

   public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState state) {
   }

   public void submitBlockModel(PoseStack poseStack, RenderType renderType, BlockStateModel model, float r, float g, float b, int light, int overlay, int outline) {
   }

   public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer) {
   }

   public void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer renderer) {
   }

   private static final class Flat implements VertexConsumer {
      private final VertexConsumer target;
      private final int color;

      Flat(VertexConsumer target, int color) {
         this.target = target;
         this.color = color;
      }

      public VertexConsumer addVertex(float x, float y, float z) {
         this.target.addVertex(x, y, z);
         return this;
      }

      public VertexConsumer setColor(int red, int green, int blue, int alpha) {
         this.target.setColor(this.color);
         return this;
      }

      public VertexConsumer setColor(int argb) {
         this.target.setColor(this.color);
         return this;
      }

      public VertexConsumer setUv(float u, float v) {
         this.target.setUv(u, v);
         return this;
      }

      public VertexConsumer setUv1(int u, int v) {
         return this;
      }

      public VertexConsumer setUv2(int u, int v) {
         return this;
      }

      public VertexConsumer setNormal(float x, float y, float z) {
         return this;
      }

      public VertexConsumer setLineWidth(float width) {
         return this;
      }
   }
}
