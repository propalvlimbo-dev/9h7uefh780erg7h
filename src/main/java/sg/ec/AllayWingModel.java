package sg.ec;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.WingAnimator;
import sg.render.AvianWingAnimator;
import sg.render.WingModelPart;

public final class AllayWingModel extends WingModel {
   private final @com.google.gson.annotations.SerializedName("А")  WingModelPart root = createPart(0, 0);
   @SerializedName("4")
   private final WingModelPart leftWingPart;
   private final @com.google.gson.annotations.SerializedName("п")  WingModelPart rightWingPart;
   private static final float LEFT_MIN_Y = -8.0F;
   private static final float LEFT_PIVOT_X = 0.5F;
   private static final float RIGHT_MIN_X = -16.0F;
   private static final float RIGHT_MIN_Y = -8.0F;
   private static final float RIGHT_PIVOT_X = -0.5F;

   public AllayWingModel() {
      this.leftWingPart = ExtrudedWingModelPart.create(0.0F, LEFT_MIN_Y, 0.0F, 16, 16, 0, 0, 64, 64);
      this.leftWingPart.setPivot(LEFT_PIVOT_X, 1.0F, 2.0F);
      this.rightWingPart = ExtrudedWingModelPart.create(RIGHT_MIN_X, RIGHT_MIN_Y, 0.0F, 16, 16, 16, 0, 64, 64);
      this.rightWingPart.setPivot(RIGHT_PIVOT_X, 1.0F, 2.0F);
      this.root.addChild(this.leftWingPart);
      this.root.addChild(this.rightWingPart);
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      AvianWingAnimator avianAnimator = (AvianWingAnimator)animator;
      applyMirroredRotationDegrees(this.leftWingPart, this.rightWingPart, avianAnimator.getPrimaryRotationDegrees(0, partialTick));
      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
