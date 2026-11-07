package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза»: веер из колючих побегов с листьями и маленькими розами на концах.
 * Восемь палитровых блоков текстуры кодируют материал, шейдер rose_cosmetic
 * дорисовывает свечение, блики и искры поверх базовой геометрии.
 */
public final class RoseWingModel extends WingModel {
   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart leftWing = createPart(0, 0);
   private final WingModelPart rightWing = createPart(0, 0);
   private final WingModelPart leftWoodDarkPart = createPart(0, 0);
   private final WingModelPart leftWoodMidPart = createPart(16, 0);
   private final WingModelPart leftCorePart = createPart(32, 0);
   private final WingModelPart leftPetalPart = createPart(48, 0);
   private final WingModelPart leftLeafDarkPart = createPart(0, 16);
   private final WingModelPart leftLeafLightPart = createPart(16, 16);
   private final WingModelPart leftPetalLightPart = createPart(32, 16);
   private final WingModelPart leftThornPart = createPart(48, 16);
   private final WingModelPart rightWoodDarkPart = createPart(0, 0);
   private final WingModelPart rightWoodMidPart = createPart(16, 0);
   private final WingModelPart rightCorePart = createPart(32, 0);
   private final WingModelPart rightPetalPart = createPart(48, 0);
   private final WingModelPart rightLeafDarkPart = createPart(0, 16);
   private final WingModelPart rightLeafLightPart = createPart(16, 16);
   private final WingModelPart rightPetalLightPart = createPart(32, 16);
   private final WingModelPart rightThornPart = createPart(48, 16);

   public RoseWingModel() {
      this.leftWing.setPivot(0.0F, 2.0F, 2.5F);
      this.rightWing.setPivot(0.0F, 2.0F, 2.5F);
      this.buildSide(
         this.leftWing, this.leftWoodDarkPart, this.leftWoodMidPart, this.leftCorePart, this.leftPetalPart,
         this.leftLeafDarkPart, this.leftLeafLightPart, this.leftPetalLightPart, this.leftThornPart, 1
      );
      this.buildSide(
         this.rightWing, this.rightWoodDarkPart, this.rightWoodMidPart, this.rightCorePart, this.rightPetalPart,
         this.rightLeafDarkPart, this.rightLeafLightPart, this.rightPetalLightPart, this.rightThornPart, -1
      );
      this.leftWing.addChild(this.leftWoodDarkPart);
      this.leftWing.addChild(this.leftWoodMidPart);
      this.leftWing.addChild(this.leftCorePart);
      this.leftWing.addChild(this.leftPetalPart);
      this.leftWing.addChild(this.leftLeafDarkPart);
      this.leftWing.addChild(this.leftLeafLightPart);
      this.leftWing.addChild(this.leftPetalLightPart);
      this.leftWing.addChild(this.leftThornPart);
      this.rightWing.addChild(this.rightWoodDarkPart);
      this.rightWing.addChild(this.rightWoodMidPart);
      this.rightWing.addChild(this.rightCorePart);
      this.rightWing.addChild(this.rightPetalPart);
      this.rightWing.addChild(this.rightLeafDarkPart);
      this.rightWing.addChild(this.rightLeafLightPart);
      this.rightWing.addChild(this.rightPetalLightPart);
      this.rightWing.addChild(this.rightThornPart);
      this.root.addChild(this.leftWing);
      this.root.addChild(this.rightWing);
   }

   private void buildSide(
      WingModelPart wing,
      WingModelPart woodDarkPart,
      WingModelPart woodMidPart,
      WingModelPart corePart,
      WingModelPart petalPart,
      WingModelPart leafDarkPart,
      WingModelPart leafLightPart,
      WingModelPart petalLightPart,
      WingModelPart thornPart,
      int sign
   ) {
      // wood_dark
      box(woodDarkPart, 1, 3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, -1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, -3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, -5, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, -7, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, -9, 0, 2, 3, 1, sign);
      box(woodDarkPart, 1, 3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 2, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 3, -1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 3, -3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 4, -5, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, -7, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, -9, 0, 2, 3, 1, sign);
      box(woodDarkPart, 6, -11, 0, 2, 3, 1, sign);
      box(woodDarkPart, 6, -12, 0, 2, 3, 1, sign);
      box(woodDarkPart, 2, 3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 3, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 4, -1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, -2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 6, -4, 0, 2, 3, 1, sign);
      box(woodDarkPart, 8, -6, 0, 2, 3, 1, sign);
      box(woodDarkPart, 9, -7, 0, 2, 3, 1, sign);
      box(woodDarkPart, 10, -9, 0, 2, 3, 1, sign);
      box(woodDarkPart, 11, -11, 0, 2, 3, 1, sign);
      box(woodDarkPart, 2, 2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 4, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, 0, 0, 2, 3, 1, sign);
      box(woodDarkPart, 7, -1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 9, -3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 10, -4, 0, 2, 3, 1, sign);
      box(woodDarkPart, 12, -5, 0, 2, 3, 1, sign);
      box(woodDarkPart, 13, -6, 0, 2, 3, 1, sign);
      box(woodDarkPart, 3, 2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 6, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 8, 0, 0, 2, 3, 1, sign);
      box(woodDarkPart, 10, -1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 12, -2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 14, -3, 0, 2, 3, 1, sign);
      box(woodDarkPart, 3, 2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 5, 2, 0, 2, 3, 1, sign);
      box(woodDarkPart, 7, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 9, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 11, 1, 0, 2, 3, 1, sign);
      box(woodDarkPart, 13, 1, 0, 1, 2, 1, sign);
      box(woodDarkPart, 4, -11, 2, 2, 3, 1, sign);
      box(woodDarkPart, 4, -13, 2, 2, 3, 1, sign);
      box(woodDarkPart, 4, -15, 2, 2, 3, 1, sign);
      box(woodDarkPart, 8, -10, 2, 2, 3, 1, sign);
      box(woodDarkPart, 9, -12, 2, 2, 3, 1, sign);
      box(woodDarkPart, 10, -13, 2, 2, 3, 1, sign);
      box(woodDarkPart, 12, -8, 2, 2, 3, 1, sign);
      box(woodDarkPart, 13, -9, 2, 2, 3, 1, sign);
      box(woodDarkPart, 14, -11, 2, 2, 3, 1, sign);
      box(woodDarkPart, 13, -4, 2, 2, 3, 1, sign);
      box(woodDarkPart, 15, -5, 2, 2, 3, 1, sign);
      box(woodDarkPart, 16, -6, 2, 2, 3, 1, sign);
      box(woodDarkPart, 13, -1, 2, 2, 3, 1, sign);
      box(woodDarkPart, 15, -1, 2, 2, 3, 1, sign);
      box(woodDarkPart, 11, 2, 2, 2, 3, 1, sign);
      box(woodDarkPart, 13, 2, 2, 1, 2, 1, sign);
      // wood_mid
      box(woodMidPart, 0, 0, 1, 1, 1, 1, sign);
      box(woodMidPart, 2, -2, 1, 1, 1, 1, sign);
      box(woodMidPart, 4, -1, 1, 1, 1, 1, sign);
      box(woodMidPart, 6, 0, 1, 1, 1, 1, sign);
      box(woodMidPart, 5, 2, 1, 1, 1, 1, sign);
      box(woodMidPart, 6, 2, 1, 1, 1, 1, sign);
      box(woodMidPart, 1, 3, 2, 2, 3, 1, sign);
      box(woodMidPart, 2, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 2, -1, 2, 2, 3, 1, sign);
      box(woodMidPart, 2, -3, 2, 2, 3, 1, sign);
      box(woodMidPart, 1, -2, 3, 1, 1, 1, sign);
      box(woodMidPart, 3, -5, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, -7, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, -9, 2, 2, 3, 1, sign);
      box(woodMidPart, 2, 3, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, -1, 2, 2, 3, 1, sign);
      box(woodMidPart, 4, -3, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, -2, 3, 1, 1, 1, sign);
      box(woodMidPart, 5, -5, 2, 2, 3, 1, sign);
      box(woodMidPart, 6, -6, 2, 2, 3, 1, sign);
      box(woodMidPart, 7, -8, 2, 2, 3, 1, sign);
      box(woodMidPart, 2, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 5, -1, 2, 2, 3, 1, sign);
      box(woodMidPart, 6, -2, 2, 2, 3, 1, sign);
      box(woodMidPart, 5, -1, 3, 1, 1, 1, sign);
      box(woodMidPart, 8, -3, 2, 2, 3, 1, sign);
      box(woodMidPart, 9, -5, 2, 2, 3, 1, sign);
      box(woodMidPart, 10, -6, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 4, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 6, 0, 2, 2, 3, 1, sign);
      box(woodMidPart, 8, -1, 2, 2, 3, 1, sign);
      box(woodMidPart, 7, 0, 3, 1, 1, 1, sign);
      box(woodMidPart, 9, -2, 2, 2, 3, 1, sign);
      box(woodMidPart, 11, -3, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 5, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 7, 1, 2, 2, 3, 1, sign);
      box(woodMidPart, 6, 2, 3, 1, 1, 1, sign);
      box(woodMidPart, 9, 0, 2, 2, 3, 1, sign);
      box(woodMidPart, 11, 0, 2, 2, 3, 1, sign);
      box(woodMidPart, 3, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 5, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 7, 2, 2, 2, 3, 1, sign);
      box(woodMidPart, 6, 3, 3, 1, 1, 1, sign);
      box(woodMidPart, 9, 2, 2, 2, 3, 1, sign);
      // core
      box(corePart, 1, -12, 1, 2, 2, 2, sign);
      box(corePart, 7, -15, 1, 2, 2, 2, sign);
      box(corePart, 12, -13, 1, 2, 2, 2, sign);
      box(corePart, 15, -9, 1, 2, 2, 2, sign);
      box(corePart, 16, -4, 1, 2, 2, 2, sign);
      box(corePart, 13, 0, 1, 2, 2, 2, sign);
      box(corePart, 5, -18, 3, 2, 2, 2, sign);
      box(corePart, 11, -16, 3, 2, 2, 2, sign);
      box(corePart, 16, -13, 3, 2, 2, 2, sign);
      box(corePart, 18, -8, 3, 2, 2, 2, sign);
      box(corePart, 17, -3, 3, 2, 2, 2, sign);
      box(corePart, 13, 1, 3, 2, 2, 2, sign);
      // petal
      box(petalPart, 1, -13, 1, 2, 1, 2, sign);
      box(petalPart, 7, -16, 1, 2, 1, 2, sign);
      box(petalPart, 12, -14, 1, 2, 1, 2, sign);
      box(petalPart, 15, -10, 1, 2, 1, 2, sign);
      box(petalPart, 16, -5, 1, 2, 1, 2, sign);
      box(petalPart, 13, -1, 1, 2, 1, 2, sign);
      box(petalPart, 5, -19, 3, 2, 1, 2, sign);
      box(petalPart, 11, -17, 3, 2, 1, 2, sign);
      box(petalPart, 16, -14, 3, 2, 1, 2, sign);
      box(petalPart, 18, -9, 3, 2, 1, 2, sign);
      box(petalPart, 17, -4, 3, 2, 1, 2, sign);
      box(petalPart, 13, 0, 3, 2, 1, 2, sign);
      // leaf_dark
      box(leafDarkPart, 2, -10, 1, 2, 1, 1, sign);
      box(leafDarkPart, 7, -13, 1, 2, 1, 1, sign);
      box(leafDarkPart, 12, -12, 1, 2, 1, 1, sign);
      box(leafDarkPart, 14, -7, 1, 2, 1, 1, sign);
      box(leafDarkPart, 15, -4, 1, 2, 1, 1, sign);
      box(leafDarkPart, 12, 0, 1, 2, 1, 1, sign);
      box(leafDarkPart, 5, -17, 2, 1, 2, 1, sign);
      box(leafDarkPart, 11, -15, 2, 1, 2, 1, sign);
      box(leafDarkPart, 16, -12, 2, 1, 2, 1, sign);
      box(leafDarkPart, 18, -7, 2, 1, 2, 1, sign);
      box(leafDarkPart, 17, -2, 2, 1, 2, 1, sign);
      // leaf_light
      box(leafLightPart, 1, -11, 0, 1, 2, 1, sign);
      box(leafLightPart, 7, -14, 0, 1, 2, 1, sign);
      box(leafLightPart, 12, -12, 0, 1, 2, 1, sign);
      box(leafLightPart, 15, -8, 0, 1, 2, 1, sign);
      box(leafLightPart, 16, -3, 0, 1, 2, 1, sign);
      box(leafLightPart, 5, -16, 3, 2, 1, 1, sign);
      box(leafLightPart, 11, -14, 3, 2, 1, 1, sign);
      box(leafLightPart, 15, -12, 3, 2, 1, 1, sign);
      box(leafLightPart, 17, -7, 3, 2, 1, 1, sign);
      box(leafLightPart, 16, -2, 3, 2, 1, 1, sign);
      box(leafLightPart, 12, 1, 3, 2, 1, 1, sign);
      // petal_light
      box(petalLightPart, 1, -12, 0, 2, 2, 1, sign);
      box(petalLightPart, 7, -15, 0, 2, 2, 1, sign);
      box(petalLightPart, 12, -13, 0, 2, 2, 1, sign);
      box(petalLightPart, 15, -9, 0, 2, 2, 1, sign);
      box(petalLightPart, 16, -4, 0, 2, 2, 1, sign);
      box(petalLightPart, 13, 0, 0, 2, 2, 1, sign);
      box(petalLightPart, 5, -18, 2, 2, 2, 1, sign);
      box(petalLightPart, 11, -16, 2, 2, 2, 1, sign);
      box(petalLightPart, 16, -13, 2, 2, 2, 1, sign);
      box(petalLightPart, 18, -8, 2, 2, 2, 1, sign);
      box(petalLightPart, 17, -3, 2, 2, 2, 1, sign);
      box(petalLightPart, 13, 1, 2, 2, 2, 1, sign);
   }

   private static void box(WingModelPart part, int x, int y, int z, int width, int height, int depth, int sign) {
      part.addCuboid(sign > 0 ? x : -(x + width), y, z, width, height, depth);
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      SimpleWingAnimator simpleAnimator = (SimpleWingAnimator)animator;
      applyMirroredRotationDegrees(this.leftWing, this.rightWing, simpleAnimator.getRotationDegrees(partialTick));
      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
