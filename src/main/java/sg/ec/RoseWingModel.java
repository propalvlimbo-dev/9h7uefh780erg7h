package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза»: две ветви с шипами, на концах побегов — маленькие розы.
 * Все боксы используют палитровые блоки текстуры {@code rose_wings.png}.
 */
public final class RoseWingModel extends WingModel {
   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart leftWing = createPart(0, 0);
   private final WingModelPart rightWing = createPart(0, 0);
   private final WingModelPart leftWood = createPart(0, 0);
   private final WingModelPart rightWood = createPart(0, 0);
   private final WingModelPart leftThorns = createPart(16, 0);
   private final WingModelPart rightThorns = createPart(16, 0);
   private final WingModelPart leftRoseCore = createPart(32, 0);
   private final WingModelPart rightRoseCore = createPart(32, 0);
   private final WingModelPart leftRosePetals = createPart(48, 0);
   private final WingModelPart rightRosePetals = createPart(48, 0);
   private final WingModelPart leftLeaves = createPart(0, 16);
   private final WingModelPart rightLeaves = createPart(0, 16);
   private static final float LEFT_PIVOT_Y = 2.0F;
   private static final float LEFT_PIVOT_Z = 2.5F;

   public RoseWingModel() {
      this.leftWing.setPivot(0.0F, LEFT_PIVOT_Y, LEFT_PIVOT_Z);
      this.rightWing.setPivot(0.0F, LEFT_PIVOT_Y, LEFT_PIVOT_Z);
      this.buildBranch(this.leftWood, this.leftThorns, this.leftRoseCore, this.leftRosePetals, this.leftLeaves, 1);
      this.buildBranch(this.rightWood, this.rightThorns, this.rightRoseCore, this.rightRosePetals, this.rightLeaves, -1);
      this.leftWing.addChild(this.leftWood);
      this.leftWing.addChild(this.leftThorns);
      this.leftWing.addChild(this.leftRoseCore);
      this.leftWing.addChild(this.leftRosePetals);
      this.leftWing.addChild(this.leftLeaves);
      this.rightWing.addChild(this.rightWood);
      this.rightWing.addChild(this.rightThorns);
      this.rightWing.addChild(this.rightRoseCore);
      this.rightWing.addChild(this.rightRosePetals);
      this.rightWing.addChild(this.rightLeaves);
      this.root.addChild(this.leftWing);
      this.root.addChild(this.rightWing);
   }

   private void buildBranch(WingModelPart wood, WingModelPart thorns, WingModelPart roseCore, WingModelPart rosePetals, WingModelPart leaves, int sign) {
      // основной побег: три сегмента вверх-наружу
      box(wood, 2, 6, 0, 3, 6, 2, sign);
      box(wood, 4, 0, 0, 3, 6, 2, sign);
      box(wood, 6, -6, 0, 3, 6, 2, sign);
      // внешняя ветвь
      box(wood, 8, 4, 0, 2, 8, 2, sign);
      // боковые отростки
      box(wood, 9, 1, 0, 4, 2, 1, sign);
      box(wood, 7, -5, 0, 4, 2, 1, sign);
      box(wood, 3, -3, 0, 4, 2, 1, sign);
      // шипы
      box(thorns, 1, 8, 1, 2, 1, 1, sign);
      box(thorns, 5, 2, 1, 2, 1, 1, sign);
      box(thorns, 3, -4, 1, 2, 1, 1, sign);
      box(thorns, 10, 5, 1, 1, 1, 1, sign);
      // листья вдоль побегов
      box(leaves, 5, 5, -1, 2, 1, 1, sign);
      box(leaves, 7, -2, -1, 2, 1, 1, sign);
      box(leaves, 9, -7, -1, 2, 1, 1, sign);
      box(leaves, 1, 3, -1, 2, 1, 1, sign);
      // маленькие розы на концах побегов
      rose(roseCore, rosePetals, 11, -1, 0, sign, 2, 2, 2);
      rose(roseCore, rosePetals, 7, -9, 0, sign, 2, 2, 1);
      rose(roseCore, rosePetals, 2, -6, 0, sign, 1, 2, 1);
      rose(roseCore, rosePetals, 4, 7, 0, sign, 1, 1, 1);
   }

   private void rose(WingModelPart core, WingModelPart petals, int x, int y, int z, int sign, int width, int height, int depth) {
      box(core, x, y, z, width, height, depth, sign);
      box(petals, x, y + height, z, width, 1, depth, sign);
      box(petals, x - 1, y + 1, z, 1, height, depth, sign);
      box(petals, x + width, y + 1, z, 1, height, depth, sign);
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
