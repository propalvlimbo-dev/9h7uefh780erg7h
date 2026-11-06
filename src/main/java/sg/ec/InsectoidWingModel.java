package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.WingAnimator;
import sg.render.SimpleWingAnimator;
import sg.render.WingModelPart;

public final class InsectoidWingModel extends WingModel {
   private final @com.google.gson.annotations.SerializedName("Я")  WingModelPart root = createPart(0, 0);
   private final @com.google.gson.annotations.SerializedName("Х")  WingModelPart leftWingPart = createPart(0, 0);
   private final @com.google.gson.annotations.SerializedName("е")  WingModelPart rightWingPart;
   private static final float LEFT_MIN_Y = -8.0F;
   private static final float LEFT_PIVOT_Z = 2.5F;
   private static final float RIGHT_MIN_X = -19.0F;
   private static final float RIGHT_MIN_Y = -8.0F;
   private static final float RIGHT_PIVOT_Z = 2.5F;

   public InsectoidWingModel() {
      this.leftWingPart.addCuboid(0.0F, LEFT_MIN_Y, 0.0F, 19, 24, 0);
      this.leftWingPart.setPivot(0.0F, 2.0F, LEFT_PIVOT_Z);
      this.rightWingPart = createPart(0, 24);
      this.rightWingPart.addCuboid(RIGHT_MIN_X, RIGHT_MIN_Y, 0.0F, 19, 24, 0);
      this.rightWingPart.setPivot(0.0F, 2.0F, RIGHT_PIVOT_Z);
      this.root.addChild(this.leftWingPart);
      this.root.addChild(this.rightWingPart);
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      SimpleWingAnimator simpleAnimator = (SimpleWingAnimator)animator;
      applyMirroredRotationDegrees(this.leftWingPart, this.rightWingPart, simpleAnimator.getRotationDegrees(partialTick));
      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
