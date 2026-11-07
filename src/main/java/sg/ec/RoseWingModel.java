package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import sg.render.AvianWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/** A rose-themed articulated wing rig based on the segmented avian wing skeleton. */
public final class RoseWingModel extends WingModel {
   private static final int TEXTURE_SIZE = 64;
   private static final int PRIMARY_SEGMENT_COUNT = 4;
   private static final float SECONDARY_ROTATION_SCALE = 0.16F;
   private static final float BLOOM_PIVOT_Y = 5.0F;
   private static final float BLOOM_PIVOT_Z = 2.1F;
   private static final float BLOOM_HALF_SIZE = 1.8F;
   private static final float BLOOM_U0 = 56.0F / TEXTURE_SIZE;
   private static final float BLOOM_V0 = 56.0F / TEXTURE_SIZE;
   private static final float BLOOM_U1 = 1.0F;
   private static final float BLOOM_V1 = 1.0F;

   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart[] leftPrimaryParts;
   private final WingModelPart[] rightPrimaryParts;
   private final WingModelPart[] leftSecondaryParts;
   private final WingModelPart[] rightSecondaryParts;

   public RoseWingModel() {
      WingModelPart leftPrimary0 = createCuboidPart(0, 28, 0.0F, -1.5F, -1.5F, 5, 3, 3, 1.35F, 5.5F, 2.5F);
      WingModelPart rightPrimary0 = createCuboidPart(0, 34, -5.0F, -1.5F, -1.5F, 5, 3, 3, -1.35F, 5.5F, 2.5F);
      WingModelPart leftPrimary1 = createCuboidPart(0, 0, -0.2F, -1.1F, -1.8F, 7, 3, 4, 4.3F, -0.4F, 0.2F);
      WingModelPart rightPrimary1 = createCuboidPart(0, 7, -6.8F, -1.1F, -1.8F, 7, 3, 4, -4.3F, -0.4F, 0.2F);
      WingModelPart leftPrimary2 = createCuboidPart(22, 0, 0.0F, -1.5F, -1.4F, 9, 3, 3, 6.2F, 0.4F, 0.1F);
      WingModelPart rightPrimary2 = createCuboidPart(22, 6, -9.0F, -1.5F, -1.4F, 9, 3, 3, -6.2F, 0.4F, 0.1F);
      WingModelPart leftPrimary3 = createCuboidPart(46, 0, 0.0F, -1.0F, -1.0F, 5, 2, 2, 8.2F, -0.2F, 0.0F);
      WingModelPart rightPrimary3 = createCuboidPart(46, 4, -5.0F, -1.0F, -1.0F, 5, 2, 2, -8.2F, -0.2F, 0.0F);

      leftPrimary0.addChild(leftPrimary1);
      rightPrimary0.addChild(rightPrimary1);
      leftPrimary1.addChild(leftPrimary2);
      rightPrimary1.addChild(rightPrimary2);
      leftPrimary2.addChild(leftPrimary3);
      rightPrimary2.addChild(rightPrimary3);

      WingModelPart leftSecondary0 = createExtrudedPart(0.0F, 0.0F, -1.0F, 6, 8, 6, 40, 0.4F, -0.2F, 1.0F);
      WingModelPart rightSecondary0 = createExtrudedPart(-6.0F, 0.0F, -1.0F, 6, 8, 0, 40, -0.4F, -0.2F, 1.0F);
      WingModelPart leftSecondary1 = createExtrudedPart(0.0F, 0.0F, -0.8F, 10, 14, 10, 14, 0.0F, 1.0F, 0.6F);
      WingModelPart rightSecondary1 = createExtrudedPart(-10.0F, 0.0F, -0.8F, 10, 14, 0, 14, 0.0F, 1.0F, 0.6F);
      WingModelPart leftSecondary2 = createExtrudedPart(-2.0F, 0.0F, -0.6F, 11, 12, 31, 14, 0.0F, 0.8F, 0.2F);
      WingModelPart rightSecondary2 = createExtrudedPart(-9.0F, 0.0F, -0.6F, 11, 12, 20, 14, 0.0F, 0.8F, 0.2F);
      WingModelPart leftSecondary3 = createExtrudedPart(0.0F, -2.0F, -0.5F, 11, 11, 53, 14, 0.0F, 0.0F, 0.0F);
      WingModelPart rightSecondary3 = createExtrudedPart(-11.0F, -2.0F, -0.5F, 11, 11, 42, 14, 0.0F, 0.0F, 0.0F);

      leftPrimary0.addChild(leftSecondary0);
      rightPrimary0.addChild(rightSecondary0);
      leftPrimary1.addChild(leftSecondary1);
      rightPrimary1.addChild(rightSecondary1);
      leftPrimary2.addChild(leftSecondary2);
      rightPrimary2.addChild(rightSecondary2);
      leftPrimary3.addChild(leftSecondary3);
      rightPrimary3.addChild(rightSecondary3);

      WingModelPart bloom = createPart(0, 0);
      bloom.addPlane(
         -BLOOM_HALF_SIZE,
         -BLOOM_HALF_SIZE,
         BLOOM_HALF_SIZE,
         BLOOM_HALF_SIZE,
         -0.7F,
         BLOOM_U0,
         BLOOM_V0,
         BLOOM_U1,
         BLOOM_V1
      );
      bloom.setPivot(0.0F, BLOOM_PIVOT_Y, BLOOM_PIVOT_Z);
      this.root.addChild(leftPrimary0);
      this.root.addChild(rightPrimary0);
      this.root.addChild(bloom);

      this.leftPrimaryParts = new WingModelPart[]{leftPrimary0, leftPrimary1, leftPrimary2, leftPrimary3};
      this.rightPrimaryParts = new WingModelPart[]{rightPrimary0, rightPrimary1, rightPrimary2, rightPrimary3};
      this.leftSecondaryParts = new WingModelPart[]{leftSecondary0, leftSecondary1, leftSecondary2, leftSecondary3};
      this.rightSecondaryParts = new WingModelPart[]{rightSecondary0, rightSecondary1, rightSecondary2, rightSecondary3};
   }

   @Override
   public void render(
      WingAnimator animator,
      float partialTick,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      if (!(animator instanceof AvianWingAnimator avianAnimator)) {
         return;
      }

      for (int index = 0; index < PRIMARY_SEGMENT_COUNT; index++) {
         Vec3 primaryRotation = avianAnimator.getPrimaryRotationDegrees(index, partialTick);
         Vec3 secondaryRotation = scaled(avianAnimator.getSecondaryRotationDegrees(index, partialTick), SECONDARY_ROTATION_SCALE);
         applyMirroredRotationDegrees(this.leftPrimaryParts[index], this.rightPrimaryParts[index], primaryRotation);
         applyMirroredRotationDegrees(this.leftSecondaryParts[index], this.rightSecondaryParts[index], secondaryRotation);
      }

      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private static WingModelPart createCuboidPart(
      int textureU,
      int textureV,
      float x,
      float y,
      float z,
      int width,
      int height,
      int depth,
      float pivotX,
      float pivotY,
      float pivotZ
   ) {
      WingModelPart part = createPart(textureU, textureV);
      part.addCuboid(x, y, z, width, height, depth);
      part.setPivot(pivotX, pivotY, pivotZ);
      return part;
   }

   private static WingModelPart createExtrudedPart(
      float x,
      float y,
      float z,
      int width,
      int height,
      int textureU,
      int textureV,
      float pivotX,
      float pivotY,
      float pivotZ
   ) {
      WingModelPart part = ExtrudedWingModelPart.create(x, y, z, width, height, textureU, textureV, TEXTURE_SIZE, TEXTURE_SIZE);
      part.setPivot(pivotX, pivotY, pivotZ);
      return part;
   }

   private static Vec3 scaled(Vec3 rotation, float scale) {
      return new Vec3(rotation.x * scale, rotation.y * scale, rotation.z * scale);
   }
}
