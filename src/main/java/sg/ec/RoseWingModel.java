package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза»: колючая ветвь вдоль лопатки, от неё веером расходятся семь побегов
 * с шипами, листьями и маленькими розами на концах. Восемь палитровых блоков
 * текстуры задают материалы (дерево, шипы, листва, лепестки).
 */
public final class RoseWingModel extends WingModel {
   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart leftWing = createPart(0, 0);
   private final WingModelPart rightWing = createPart(0, 0);
   private final WingModelPart leftWoodPart = createPart(0, 0);
   private final WingModelPart rightWoodPart = createPart(0, 0);
   private final WingModelPart leftWoodLightPart = createPart(48, 16);
   private final WingModelPart rightWoodLightPart = createPart(48, 16);
   private final WingModelPart leftThornPart = createPart(16, 0);
   private final WingModelPart rightThornPart = createPart(16, 0);
   private final WingModelPart leftLeafPart = createPart(32, 0);
   private final WingModelPart rightLeafPart = createPart(32, 0);
   private final WingModelPart leftLeafDarkPart = createPart(48, 0);
   private final WingModelPart rightLeafDarkPart = createPart(48, 0);
   private final WingModelPart leftRoseCorePart = createPart(0, 16);
   private final WingModelPart rightRoseCorePart = createPart(0, 16);
   private final WingModelPart leftRosePetalPart = createPart(16, 16);
   private final WingModelPart rightRosePetalPart = createPart(16, 16);
   private final WingModelPart leftRoseHiPart = createPart(32, 16);
   private final WingModelPart rightRoseHiPart = createPart(32, 16);

   public RoseWingModel() {
      this.leftWing.setPivot(0.0F, 2.0F, 2.5F);
      this.rightWing.setPivot(0.0F, 2.0F, 2.5F);
      this.buildSide(1);
      this.buildSide(-1);
      this.leftWing.addChild(this.leftWoodPart);
      this.leftWing.addChild(this.leftWoodLightPart);
      this.leftWing.addChild(this.leftThornPart);
      this.leftWing.addChild(this.leftLeafPart);
      this.leftWing.addChild(this.leftLeafDarkPart);
      this.leftWing.addChild(this.leftRoseCorePart);
      this.leftWing.addChild(this.leftRosePetalPart);
      this.leftWing.addChild(this.leftRoseHiPart);
      this.rightWing.addChild(this.rightWoodPart);
      this.rightWing.addChild(this.rightWoodLightPart);
      this.rightWing.addChild(this.rightThornPart);
      this.rightWing.addChild(this.rightLeafPart);
      this.rightWing.addChild(this.rightLeafDarkPart);
      this.rightWing.addChild(this.rightRoseCorePart);
      this.rightWing.addChild(this.rightRosePetalPart);
      this.rightWing.addChild(this.rightRoseHiPart);
      this.root.addChild(this.leftWing);
      this.root.addChild(this.rightWing);
   }

   private void buildSide(int sign) {
      WingModelPart woodPart = sign > 0 ? this.leftWoodPart : this.rightWoodPart;
      WingModelPart woodLightPart = sign > 0 ? this.leftWoodLightPart : this.rightWoodLightPart;
      WingModelPart thornPart = sign > 0 ? this.leftThornPart : this.rightThornPart;
      WingModelPart leafPart = sign > 0 ? this.leftLeafPart : this.rightLeafPart;
      WingModelPart leafDarkPart = sign > 0 ? this.leftLeafDarkPart : this.rightLeafDarkPart;
      WingModelPart roseCorePart = sign > 0 ? this.leftRoseCorePart : this.rightRoseCorePart;
      WingModelPart rosePetalPart = sign > 0 ? this.leftRosePetalPart : this.rightRosePetalPart;
      WingModelPart roseHiPart = sign > 0 ? this.leftRoseHiPart : this.rightRoseHiPart;

      // wood
      box(woodPart, 1, -14, 0, 2, 1, 2, sign);
      box(woodPart, 1, -13, 0, 1, 1, 1, sign);
      box(woodPart, 1, -12, 0, 1, 1, 1, sign);
      box(woodPart, 1, -10, 0, 1, 1, 1, sign);
      box(woodPart, 1, -9, 0, 1, 1, 1, sign);
      box(woodPart, 1, -8, 0, 1, 1, 1, sign);
      box(woodPart, 1, -7, 0, 1, 1, 1, sign);
      box(woodPart, 1, -6, 0, 1, 1, 1, sign);
      box(woodPart, 1, -4, 0, 1, 1, 1, sign);
      box(woodPart, 1, -3, 0, 1, 1, 1, sign);
      box(woodPart, 1, -2, 0, 1, 1, 1, sign);
      box(woodPart, 1, -1, 0, 1, 1, 1, sign);
      box(woodPart, 1, 0, 0, 1, 1, 1, sign);
      box(woodPart, 1, 2, 0, 1, 1, 1, sign);
      box(woodPart, 1, 3, 0, 1, 1, 1, sign);
      box(woodPart, 1, 4, 0, 1, 1, 1, sign);
      box(woodPart, 4, -13, 0, 1, 1, 1, sign);
      box(woodPart, 5, -14, 0, 1, 1, 1, sign);
      box(woodPart, 7, -14, 0, 1, 1, 1, sign);
      box(woodPart, 8, -14, 0, 1, 1, 1, sign);
      box(woodPart, 10, -14, 0, 1, 1, 1, sign);
      box(woodPart, 3, -11, 0, 1, 1, 1, sign);
      box(woodPart, 5, -11, 0, 1, 1, 1, sign);
      box(woodPart, 6, -12, 0, 1, 1, 1, sign);
      box(woodPart, 8, -13, 0, 1, 1, 1, sign);
      box(woodPart, 9, -14, 0, 1, 1, 1, sign);
      box(woodPart, 11, -14, 0, 1, 1, 1, sign);
      box(woodPart, 12, -15, 0, 1, 1, 1, sign);
      box(woodPart, 3, -8, 0, 1, 1, 1, sign);
      box(woodPart, 4, -9, 0, 1, 1, 1, sign);
      box(woodPart, 6, -10, 0, 1, 1, 1, sign);
      box(woodPart, 7, -11, 0, 1, 1, 1, sign);
      box(woodPart, 8, -12, 0, 1, 1, 1, sign);
      box(woodPart, 9, -13, 0, 1, 1, 1, sign);
      box(woodPart, 10, -14, 0, 1, 1, 1, sign);
      box(woodPart, 12, -16, 0, 1, 1, 1, sign);
      box(woodPart, 3, -5, 0, 1, 1, 1, sign);
      box(woodPart, 4, -7, 0, 1, 1, 1, sign);
      box(woodPart, 5, -8, 0, 1, 1, 1, sign);
      box(woodPart, 5, -9, 0, 1, 1, 1, sign);
      box(woodPart, 6, -11, 0, 1, 1, 1, sign);
      box(woodPart, 7, -12, 0, 1, 1, 1, sign);
      box(woodPart, 8, -13, 0, 1, 1, 1, sign);
      box(woodPart, 9, -15, 0, 1, 1, 1, sign);
      box(woodPart, 10, -16, 0, 1, 1, 1, sign);
      box(woodPart, 2, -3, 0, 1, 1, 1, sign);
      box(woodPart, 3, -4, 0, 1, 1, 1, sign);
      box(woodPart, 3, -6, 0, 1, 1, 1, sign);
      box(woodPart, 4, -7, 0, 1, 1, 1, sign);
      box(woodPart, 4, -9, 0, 1, 1, 1, sign);
      box(woodPart, 5, -10, 0, 1, 1, 1, sign);
      box(woodPart, 5, -12, 0, 1, 1, 1, sign);
      box(woodPart, 6, -13, 0, 1, 1, 1, sign);
      box(woodPart, 6, -15, 0, 1, 1, 1, sign);
      box(woodPart, 2, 0, 0, 1, 1, 1, sign);
      box(woodPart, 2, -1, 0, 1, 1, 1, sign);
      box(woodPart, 2, -3, 0, 1, 1, 1, sign);
      box(woodPart, 2, -4, 0, 1, 1, 1, sign);
      box(woodPart, 2, -6, 0, 1, 1, 1, sign);
      box(woodPart, 2, -8, 0, 1, 1, 1, sign);
      box(woodPart, 2, -9, 0, 1, 1, 1, sign);
      box(woodPart, 2, 3, 0, 1, 1, 1, sign);
      box(woodPart, 1, 1, 0, 1, 1, 1, sign);
      box(woodPart, 1, 0, 0, 1, 1, 1, sign);
      box(woodPart, 0, -2, 0, 1, 1, 1, sign);
      // wood_light
      box(woodLightPart, 1, -13, 1, 1, 1, 1, sign);
      box(woodLightPart, 1, -8, 1, 1, 1, 1, sign);
      box(woodLightPart, 1, -4, 1, 1, 1, 1, sign);
      box(woodLightPart, 1, -1, 1, 1, 1, 1, sign);
      box(woodLightPart, 1, 0, 1, 1, 1, 1, sign);
      // thorn
      box(thornPart, 4, -14, 0, 1, 1, 1, sign);
      box(thornPart, 8, -15, 0, 1, 1, 1, sign);
      box(thornPart, 3, -12, 0, 1, 1, 1, sign);
      box(thornPart, 8, -14, 0, 1, 1, 1, sign);
      box(thornPart, 3, -9, 0, 1, 1, 1, sign);
      box(thornPart, 7, -12, 0, 1, 1, 1, sign);
      box(thornPart, 10, -15, 0, 1, 1, 1, sign);
      box(thornPart, 3, -6, 0, 1, 1, 1, sign);
      box(thornPart, 5, -10, 0, 1, 1, 1, sign);
      box(thornPart, 8, -14, 0, 1, 1, 1, sign);
      box(thornPart, 2, -4, 0, 1, 1, 1, sign);
      box(thornPart, 4, -8, 0, 1, 1, 1, sign);
      box(thornPart, 5, -13, 0, 1, 1, 1, sign);
      box(thornPart, 2, -1, 0, 1, 1, 1, sign);
      box(thornPart, 2, -5, 0, 1, 1, 1, sign);
      box(thornPart, 2, 2, 0, 1, 1, 1, sign);
      // leaf
      box(leafPart, 9, -14, 1, 2, 1, 1, sign);
      box(leafPart, 12, -14, 1, 2, 1, 1, sign);
      box(leafPart, 11, -14, 1, 2, 1, 1, sign);
      box(leafPart, 10, -15, 1, 2, 1, 1, sign);
      box(leafPart, 7, -13, 1, 2, 1, 1, sign);
      box(leafPart, 3, -8, 1, 2, 1, 1, sign);
      box(leafPart, 2, 0, 1, 2, 1, 1, sign);
      // rose_core
      box(roseCorePart, 12, -15, 1, 1, 1, 1, sign);
      box(roseCorePart, 10, -16, 1, 1, 1, 1, sign);
      box(roseCorePart, 2, -9, 1, 1, 1, 1, sign);
      // rose_petal
      box(rosePetalPart, 11, -15, 1, 1, 1, 1, sign);
      box(rosePetalPart, 13, -15, 1, 1, 1, 1, sign);
      box(rosePetalPart, 12, -16, 1, 1, 1, 1, sign);
      box(rosePetalPart, 12, -14, 1, 1, 1, 1, sign);
      box(rosePetalPart, 9, -16, 1, 1, 1, 1, sign);
      box(rosePetalPart, 11, -16, 1, 1, 1, 1, sign);
      box(rosePetalPart, 10, -17, 1, 1, 1, 1, sign);
      box(rosePetalPart, 10, -15, 1, 1, 1, 1, sign);
      box(rosePetalPart, 1, -9, 1, 1, 1, 1, sign);
      box(rosePetalPart, 3, -9, 1, 1, 1, 1, sign);
      box(rosePetalPart, 2, -10, 1, 1, 1, 1, sign);
      box(rosePetalPart, 2, -8, 1, 1, 1, 1, sign);
      // rose_hi
      box(roseHiPart, 12, -15, 2, 1, 1, 1, sign);
      box(roseHiPart, 10, -16, 2, 1, 1, 1, sign);
      box(roseHiPart, 2, -9, 2, 1, 1, 1, sign);
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
