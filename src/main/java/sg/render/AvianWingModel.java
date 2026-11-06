package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.ec.WingModel;
import sg.ec.ExtrudedWingModelPart;

public final class AvianWingModel extends WingModel {
   @SerializedName("В")
   private final WingModelPart root = createPart(0, 0);
   @SerializedName("В")
   private final WingModelPart[] leftPrimaryParts;
   private final @com.google.gson.annotations.SerializedName("Я")  WingModelPart[] rightPrimaryParts;
   private final @com.google.gson.annotations.SerializedName("Х")  WingModelPart[] leftSecondaryParts;
   private final @com.google.gson.annotations.SerializedName("е")  WingModelPart[] rightSecondaryParts;
   private static final float LEFT_PRIMARY_0_MIN_Y = -1.5F;
   private static final float LEFT_PRIMARY_0_MIN_Z = -1.5F;
   private static final float LEFT_PRIMARY_0_PIVOT_X = 1.5F;
   private static final float LEFT_PRIMARY_0_PIVOT_Y = 5.5F;
   private static final float LEFT_PRIMARY_0_PIVOT_Z = 2.5F;
   private static final float RIGHT_PRIMARY_0_MIN_X = -5.0F;
   private static final float RIGHT_PRIMARY_0_MIN_Y = -1.5F;
   private static final float RIGHT_PRIMARY_0_MIN_Z = -1.5F;
   private static final float RIGHT_PRIMARY_0_PIVOT_X = -1.5F;
   private static final float RIGHT_PRIMARY_0_PIVOT_Y = 5.5F;
   private static final float RIGHT_PRIMARY_0_PIVOT_Z = 2.5F;
   private static final float LEFT_PRIMARY_1_MIN_X = -0.1F;
   private static final float LEFT_PRIMARY_1_MIN_Y = -1.1F;
   private static final float LEFT_PRIMARY_1_MIN_Z = -2.0F;
   private static final float LEFT_PRIMARY_1_PIVOT_X = 4.7F;
   private static final float LEFT_PRIMARY_1_PIVOT_Y = -0.6F;
   private static final float LEFT_PRIMARY_1_PIVOT_Z = 0.1F;
   private static final float RIGHT_PRIMARY_1_MIN_X = -6.9F;
   private static final float RIGHT_PRIMARY_1_MIN_Y = -1.1F;
   private static final float RIGHT_PRIMARY_1_MIN_Z = -2.0F;
   private static final float RIGHT_PRIMARY_1_PIVOT_X = -4.7F;
   private static final float RIGHT_PRIMARY_1_PIVOT_Y = -0.6F;
   private static final float RIGHT_PRIMARY_1_PIVOT_Z = 0.1F;
   private static final float LEFT_PRIMARY_2_MIN_Y = -1.5F;
   private static final float LEFT_PRIMARY_2_MIN_Z = -1.5F;
   private static final float LEFT_PRIMARY_2_PIVOT_X = 6.5F;
   private static final float LEFT_PRIMARY_2_PIVOT_Y = 0.2F;
   private static final float LEFT_PRIMARY_2_PIVOT_Z = 0.1F;
   private static final float RIGHT_PRIMARY_2_MIN_X = -9.0F;
   private static final float RIGHT_PRIMARY_2_MIN_Y = -1.5F;
   private static final float RIGHT_PRIMARY_2_MIN_Z = -1.5F;
   private static final float RIGHT_PRIMARY_2_PIVOT_X = -6.5F;
   private static final float RIGHT_PRIMARY_2_PIVOT_Y = 0.2F;
   private static final float RIGHT_PRIMARY_2_PIVOT_Z = 0.1F;
   private static final float LEFT_PRIMARY_3_MIN_Y = -1.0F;
   private static final float LEFT_PRIMARY_3_MIN_Z = -1.0F;
   private static final float LEFT_PRIMARY_3_PIVOT_X = 8.5F;
   private static final float RIGHT_PRIMARY_3_MIN_X = -5.0F;
   private static final float RIGHT_PRIMARY_3_MIN_Y = -1.0F;
   private static final float RIGHT_PRIMARY_3_MIN_Z = -1.0F;
   private static final float RIGHT_PRIMARY_3_PIVOT_X = -8.5F;
   private static final float LEFT_SECONDARY_0_MIN_Z = -1.0F;
   private static final float LEFT_SECONDARY_0_PIVOT_X = 0.4F;
   private static final float RIGHT_SECONDARY_0_MIN_X = -6.0F;
   private static final float RIGHT_SECONDARY_0_MIN_Z = -1.0F;
   private static final float RIGHT_SECONDARY_0_PIVOT_X = -0.4F;
   private static final float LEFT_SECONDARY_1_MIN_Z = -0.5F;
   private static final float LEFT_SECONDARY_1_PIVOT_Y = 1.5F;
   private static final float RIGHT_SECONDARY_1_MIN_X = -10.0F;
   private static final float RIGHT_SECONDARY_1_MIN_Z = -0.5F;
   private static final float RIGHT_SECONDARY_1_PIVOT_Y = 1.5F;
   private static final float LEFT_SECONDARY_2_MIN_X = -2.0F;
   private static final float LEFT_SECONDARY_2_MIN_Z = -0.5F;
   private static final float RIGHT_SECONDARY_2_MIN_X = -9.0F;
   private static final float RIGHT_SECONDARY_2_MIN_Z = -0.5F;
   private static final float LEFT_SECONDARY_3_MIN_Y = -2.1F;
   private static final float LEFT_SECONDARY_3_MIN_Z = -0.5F;
   private static final float RIGHT_SECONDARY_3_MIN_X = -11.0F;
   private static final float RIGHT_SECONDARY_3_MIN_Y = -2.1F;
   private static final float RIGHT_SECONDARY_3_MIN_Z = -0.5F;

   private static WingModelPart createExtrudedPart(float x, float y, float z, int width, int height, int textureU, int textureV, float pivotX, float pivotY, float pivotZ) {
      ExtrudedWingModelPart part = ExtrudedWingModelPart.create(x, y, z, width, height, textureU, textureV, 64, 64);
      part.setPivot(pivotX, pivotY, pivotZ);
      return part;
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      AvianWingAnimator avianAnimator = (AvianWingAnimator)animator;

      for (int primaryIndex = 0; primaryIndex < this.leftPrimaryParts.length; primaryIndex++) {
         applyMirroredRotationDegrees(this.leftPrimaryParts[primaryIndex], this.rightPrimaryParts[primaryIndex], avianAnimator.getPrimaryRotationDegrees(primaryIndex, partialTick));
      }

      for (int secondaryIndex = 0; secondaryIndex < this.leftSecondaryParts.length; secondaryIndex++) {
         applyMirroredRotationDegrees(this.leftSecondaryParts[secondaryIndex], this.rightSecondaryParts[secondaryIndex], avianAnimator.getSecondaryRotationDegrees(secondaryIndex, partialTick));
      }

      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private static WingModelPart createCuboidPart(int textureU, int textureV, float x, float y, float z, int width, int height, int depth, float pivotX, float pivotY, float pivotZ) {
      WingModelPart part = createPart(textureU, textureV);
      part.addCuboid(x, y, z, width, height, depth);
      part.setPivot(pivotX, pivotY, pivotZ);
      return part;
   }

   public AvianWingModel() {
      WingModelPart leftPrimary0 = createCuboidPart(0, 28, 0.0F, LEFT_PRIMARY_0_MIN_Y, LEFT_PRIMARY_0_MIN_Z, 5, 3, 3, LEFT_PRIMARY_0_PIVOT_X, LEFT_PRIMARY_0_PIVOT_Y, LEFT_PRIMARY_0_PIVOT_Z);
      WingModelPart rightPrimary0 = createCuboidPart(0, 34, RIGHT_PRIMARY_0_MIN_X, RIGHT_PRIMARY_0_MIN_Y, RIGHT_PRIMARY_0_MIN_Z, 5, 3, 3, RIGHT_PRIMARY_0_PIVOT_X, RIGHT_PRIMARY_0_PIVOT_Y, RIGHT_PRIMARY_0_PIVOT_Z);
      WingModelPart leftPrimary1 = createCuboidPart(0, 0, LEFT_PRIMARY_1_MIN_X, LEFT_PRIMARY_1_MIN_Y, LEFT_PRIMARY_1_MIN_Z, 7, 3, 4, LEFT_PRIMARY_1_PIVOT_X, LEFT_PRIMARY_1_PIVOT_Y, LEFT_PRIMARY_1_PIVOT_Z);
      WingModelPart rightPrimary1 = createCuboidPart(0, 7, RIGHT_PRIMARY_1_MIN_X, RIGHT_PRIMARY_1_MIN_Y, RIGHT_PRIMARY_1_MIN_Z, 7, 3, 4, RIGHT_PRIMARY_1_PIVOT_X, RIGHT_PRIMARY_1_PIVOT_Y, RIGHT_PRIMARY_1_PIVOT_Z);
      WingModelPart leftPrimary2 = createCuboidPart(22, 0, 0.0F, LEFT_PRIMARY_2_MIN_Y, LEFT_PRIMARY_2_MIN_Z, 9, 3, 3, LEFT_PRIMARY_2_PIVOT_X, LEFT_PRIMARY_2_PIVOT_Y, LEFT_PRIMARY_2_PIVOT_Z);
      WingModelPart rightPrimary2 = createCuboidPart(22, 6, RIGHT_PRIMARY_2_MIN_X, RIGHT_PRIMARY_2_MIN_Y, RIGHT_PRIMARY_2_MIN_Z, 9, 3, 3, RIGHT_PRIMARY_2_PIVOT_X, RIGHT_PRIMARY_2_PIVOT_Y, RIGHT_PRIMARY_2_PIVOT_Z);
      WingModelPart leftPrimary3 = createCuboidPart(46, 0, 0.0F, LEFT_PRIMARY_3_MIN_Y, LEFT_PRIMARY_3_MIN_Z, 5, 2, 2, LEFT_PRIMARY_3_PIVOT_X, 0.0F, 0.0F);
      WingModelPart rightPrimary3 = createCuboidPart(46, 4, RIGHT_PRIMARY_3_MIN_X, RIGHT_PRIMARY_3_MIN_Y, RIGHT_PRIMARY_3_MIN_Z, 5, 2, 2, RIGHT_PRIMARY_3_PIVOT_X, 0.0F, 0.0F);
      leftPrimary0.addChild(leftPrimary1);
      rightPrimary0.addChild(rightPrimary1);
      leftPrimary1.addChild(leftPrimary2);
      rightPrimary1.addChild(rightPrimary2);
      leftPrimary2.addChild(leftPrimary3);
      rightPrimary2.addChild(rightPrimary3);
      WingModelPart leftSecondary0 = createExtrudedPart(0.0F, 0.0F, LEFT_SECONDARY_0_MIN_Z, 6, 8, 6, 40, LEFT_SECONDARY_0_PIVOT_X, 0.0F, 1.0F);
      WingModelPart rightSecondary0 = createExtrudedPart(RIGHT_SECONDARY_0_MIN_X, 0.0F, RIGHT_SECONDARY_0_MIN_Z, 6, 8, 0, 40, RIGHT_SECONDARY_0_PIVOT_X, 0.0F, 1.0F);
      WingModelPart leftSecondary1 = createExtrudedPart(0.0F, 0.0F, LEFT_SECONDARY_1_MIN_Z, 10, 14, 10, 14, 0.0F, LEFT_SECONDARY_1_PIVOT_Y, 1.0F);
      WingModelPart rightSecondary1 = createExtrudedPart(RIGHT_SECONDARY_1_MIN_X, 0.0F, RIGHT_SECONDARY_1_MIN_Z, 10, 14, 0, 14, 0.0F, RIGHT_SECONDARY_1_PIVOT_Y, 1.0F);
      WingModelPart leftSecondary2 = createExtrudedPart(LEFT_SECONDARY_2_MIN_X, 0.0F, LEFT_SECONDARY_2_MIN_Z, 11, 12, 31, 14, 0.0F, 1.0F, 0.0F);
      WingModelPart rightSecondary2 = createExtrudedPart(RIGHT_SECONDARY_2_MIN_X, 0.0F, RIGHT_SECONDARY_2_MIN_Z, 11, 12, 20, 14, 0.0F, 1.0F, 0.0F);
      WingModelPart leftSecondary3 = createExtrudedPart(0.0F, LEFT_SECONDARY_3_MIN_Y, LEFT_SECONDARY_3_MIN_Z, 11, 11, 53, 14, 0.0F, 0.0F, 0.0F);
      WingModelPart rightSecondary3 = createExtrudedPart(RIGHT_SECONDARY_3_MIN_X, RIGHT_SECONDARY_3_MIN_Y, RIGHT_SECONDARY_3_MIN_Z, 11, 11, 42, 14, 0.0F, 0.0F, 0.0F);
      leftPrimary0.addChild(leftSecondary0);
      rightPrimary0.addChild(rightSecondary0);
      leftPrimary1.addChild(leftSecondary1);
      rightPrimary1.addChild(rightSecondary1);
      leftPrimary2.addChild(leftSecondary2);
      rightPrimary2.addChild(rightSecondary2);
      leftPrimary3.addChild(leftSecondary3);
      rightPrimary3.addChild(rightSecondary3);
      this.root.addChild(leftPrimary0);
      this.root.addChild(rightPrimary0);
      this.leftPrimaryParts = new WingModelPart[]{leftPrimary0, leftPrimary1, leftPrimary2, leftPrimary3};
      this.rightPrimaryParts = new WingModelPart[]{rightPrimary0, rightPrimary1, rightPrimary2, rightPrimary3};
      this.leftSecondaryParts = new WingModelPart[]{leftSecondary0, leftSecondary1, leftSecondary2, leftSecondary3};
      this.rightSecondaryParts = new WingModelPart[]{rightSecondary0, rightSecondary1, rightSecondary2, rightSecondary3};
   }
}
