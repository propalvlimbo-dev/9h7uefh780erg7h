package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза»: каждое крыло — раскрывшийся бутон. Три кольца лепестков
 * (внешние светлые, внутренние тёмные), в центре сердцевина, сзади колючий
 * стебель с шипами и листьями. Материалы задаются восемью блоками текстуры.
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
      box(woodPart, 0, -12, -1, 2, 1, 2, sign);
      box(woodPart, 0, -10, -1, 2, 1, 2, sign);
      box(woodPart, 0, -9, -1, 1, 1, 2, sign);
      box(woodPart, 0, -7, -1, 1, 1, 2, sign);
      box(woodPart, 0, -6, -1, 1, 1, 2, sign);
      box(woodPart, 0, -4, -1, 1, 1, 2, sign);
      box(woodPart, 0, -2, -1, 1, 1, 2, sign);
      box(woodPart, 0, -1, -1, 1, 1, 2, sign);
      box(woodPart, 0, 1, -1, 1, 1, 2, sign);
      box(woodPart, 0, 2, -1, 1, 1, 2, sign);
      box(woodPart, 0, 4, -1, 1, 1, 2, sign);
      box(woodPart, 0, 6, -1, 1, 1, 2, sign);
      // thorn
      box(thornPart, -14, -8, 1, 1, 1, 1, sign);
      box(thornPart, 6, -20, 1, 1, 1, 1, sign);
      box(thornPart, 20, -2, 1, 1, 1, 1, sign);
      box(thornPart, -1, -10, 0, 1, 1, 1, sign);
      box(thornPart, 2, -10, 0, 1, 1, 1, sign);
      box(thornPart, -1, -6, 0, 1, 1, 1, sign);
      box(thornPart, 2, -6, 0, 1, 1, 1, sign);
      box(thornPart, -1, -2, 0, 1, 1, 1, sign);
      box(thornPart, 2, -2, 0, 1, 1, 1, sign);
      box(thornPart, -1, 2, 0, 1, 1, 1, sign);
      box(thornPart, 2, 2, 0, 1, 1, 1, sign);
      // leaf
      box(leafPart, -2, 4, 0, 2, 1, 1, sign);
      box(leafPart, 1, 6, 0, 2, 1, 1, sign);
      // leaf_dark
      box(leafDarkPart, -3, 2, 0, 2, 1, 1, sign);
      // rose_core
      box(roseCorePart, -6, -3, 1, 3, 2, 1, sign);
      box(roseCorePart, -8, -3, 1, 2, 1, 1, sign);
      box(roseCorePart, -5, -7, 1, 3, 2, 1, sign);
      box(roseCorePart, -6, -8, 1, 2, 1, 1, sign);
      box(roseCorePart, -2, -10, 1, 3, 2, 1, sign);
      box(roseCorePart, -2, -12, 1, 2, 1, 1, sign);
      box(roseCorePart, 3, -11, 1, 3, 2, 1, sign);
      box(roseCorePart, 4, -13, 1, 2, 1, 1, sign);
      box(roseCorePart, 7, -9, 1, 3, 2, 1, sign);
      box(roseCorePart, 9, -10, 1, 2, 1, 1, sign);
      box(roseCorePart, 9, -5, 1, 3, 2, 1, sign);
      box(roseCorePart, 12, -5, 1, 2, 1, 1, sign);
      box(roseCorePart, 9, 0, 1, 3, 2, 1, sign);
      box(roseCorePart, 11, 0, 1, 2, 1, 1, sign);
      box(roseCorePart, -2, -4, 2, 2, 2, 1, sign);
      box(roseCorePart, -4, -4, 2, 1, 1, 1, sign);
      box(roseCorePart, -1, -7, 2, 2, 2, 1, sign);
      box(roseCorePart, -1, -8, 2, 1, 1, 1, sign);
      box(roseCorePart, 3, -7, 2, 2, 2, 1, sign);
      box(roseCorePart, 3, -9, 2, 1, 1, 1, sign);
      box(roseCorePart, 5, -6, 2, 2, 2, 1, sign);
      box(roseCorePart, 8, -7, 2, 1, 1, 1, sign);
      box(roseCorePart, 6, -3, 2, 2, 2, 1, sign);
      box(roseCorePart, 9, -2, 2, 1, 1, 1, sign);
      // rose_petal
      box(rosePetalPart, -10, -1, 0, 3, 2, 1, sign);
      box(rosePetalPart, -12, -1, 0, 3, 1, 1, sign);
      box(rosePetalPart, -13, 0, 0, 2, 1, 1, sign);
      box(rosePetalPart, -10, -6, 0, 3, 2, 1, sign);
      box(rosePetalPart, -11, -7, 0, 3, 1, 1, sign);
      box(rosePetalPart, -13, -8, 0, 2, 1, 1, sign);
      box(rosePetalPart, -7, -11, 0, 3, 2, 1, sign);
      box(rosePetalPart, -8, -13, 0, 3, 1, 1, sign);
      box(rosePetalPart, -9, -14, 0, 2, 1, 1, sign);
      box(rosePetalPart, -2, -14, 0, 3, 2, 1, sign);
      box(rosePetalPart, -3, -16, 0, 3, 1, 1, sign);
      box(rosePetalPart, -3, -18, 0, 2, 1, 1, sign);
      box(rosePetalPart, 4, -14, 0, 3, 2, 1, sign);
      box(rosePetalPart, 4, -16, 0, 3, 1, 1, sign);
      box(rosePetalPart, 5, -18, 0, 2, 1, 1, sign);
      box(rosePetalPart, 9, -12, 0, 3, 2, 1, sign);
      box(rosePetalPart, 10, -14, 0, 3, 1, 1, sign);
      box(rosePetalPart, 11, -15, 0, 2, 1, 1, sign);
      box(rosePetalPart, 12, -8, 0, 3, 2, 1, sign);
      box(rosePetalPart, 14, -9, 0, 3, 1, 1, sign);
      box(rosePetalPart, 16, -10, 0, 2, 1, 1, sign);
      box(rosePetalPart, 13, -2, 0, 3, 2, 1, sign);
      box(rosePetalPart, 15, -2, 0, 3, 1, 1, sign);
      box(rosePetalPart, 18, -2, 0, 2, 1, 1, sign);
      box(rosePetalPart, 12, 3, 0, 3, 2, 1, sign);
      box(rosePetalPart, 13, 4, 0, 3, 1, 1, sign);
      box(rosePetalPart, 16, 5, 0, 2, 1, 1, sign);
      box(rosePetalPart, -3, -3, 1, 4, 2, 1, sign);
      box(rosePetalPart, -4, -3, 1, 3, 2, 1, sign);
      box(rosePetalPart, -2, -5, 1, 4, 2, 1, sign);
      box(rosePetalPart, -3, -6, 1, 3, 2, 1, sign);
      box(rosePetalPart, -1, -6, 1, 4, 2, 1, sign);
      box(rosePetalPart, -1, -8, 1, 3, 2, 1, sign);
      box(rosePetalPart, 2, -7, 1, 4, 2, 1, sign);
      box(rosePetalPart, 2, -9, 1, 3, 2, 1, sign);
      box(rosePetalPart, 4, -6, 1, 4, 2, 1, sign);
      box(rosePetalPart, 5, -7, 1, 3, 2, 1, sign);
      box(rosePetalPart, 5, -4, 1, 4, 2, 1, sign);
      box(rosePetalPart, 7, -4, 1, 3, 2, 1, sign);
      box(rosePetalPart, 5, -2, 1, 4, 2, 1, sign);
      box(rosePetalPart, 7, -1, 1, 3, 2, 1, sign);
      box(rosePetalPart, -1, -3, 2, 3, 2, 1, sign);
      box(rosePetalPart, 0, -5, 2, 3, 2, 1, sign);
      box(rosePetalPart, 2, -5, 2, 3, 2, 1, sign);
      box(rosePetalPart, 3, -5, 2, 3, 2, 1, sign);
      box(rosePetalPart, 4, -3, 2, 3, 2, 1, sign);
      box(rosePetalPart, 2, -4, 3, 2, 2, 1, sign);
      // rose_hi
      box(roseHiPart, -5, -2, 0, 5, 2, 1, sign);
      box(roseHiPart, -6, -2, 0, 4, 2, 1, sign);
      box(roseHiPart, -8, -1, 0, 4, 2, 1, sign);
      box(roseHiPart, -5, -5, 0, 5, 2, 1, sign);
      box(roseHiPart, -6, -5, 0, 4, 2, 1, sign);
      box(roseHiPart, -8, -6, 0, 4, 2, 1, sign);
      box(roseHiPart, -3, -7, 0, 5, 2, 1, sign);
      box(roseHiPart, -4, -8, 0, 4, 2, 1, sign);
      box(roseHiPart, -6, -10, 0, 4, 2, 1, sign);
      box(roseHiPart, -1, -8, 0, 5, 2, 1, sign);
      box(roseHiPart, -1, -10, 0, 4, 2, 1, sign);
      box(roseHiPart, -2, -12, 0, 4, 2, 1, sign);
      box(roseHiPart, 1, -9, 0, 5, 2, 1, sign);
      box(roseHiPart, 2, -10, 0, 4, 2, 1, sign);
      box(roseHiPart, 3, -12, 0, 4, 2, 1, sign);
      box(roseHiPart, 4, -7, 0, 5, 2, 1, sign);
      box(roseHiPart, 6, -9, 0, 4, 2, 1, sign);
      box(roseHiPart, 7, -11, 0, 4, 2, 1, sign);
      box(roseHiPart, 6, -5, 0, 5, 2, 1, sign);
      box(roseHiPart, 8, -6, 0, 4, 2, 1, sign);
      box(roseHiPart, 10, -7, 0, 4, 2, 1, sign);
      box(roseHiPart, 6, -3, 0, 5, 2, 1, sign);
      box(roseHiPart, 9, -3, 0, 4, 2, 1, sign);
      box(roseHiPart, 11, -3, 0, 4, 2, 1, sign);
      box(roseHiPart, 5, 0, 0, 5, 2, 1, sign);
      box(roseHiPart, 8, 1, 0, 4, 2, 1, sign);
      box(roseHiPart, 9, 2, 0, 4, 2, 1, sign);
      box(roseHiPart, 3, -3, 4, 1, 1, 1, sign);
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
