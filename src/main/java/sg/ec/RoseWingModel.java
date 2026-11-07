package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза» — HD-арт на двух плоскостях, как у Lunar Client:
 * левое и правое крыло используют одну и ту же текстуру, второе зеркалится
 * геометрией. Плоскости посажены на «позвоночник» игрока и разворачиваются
 * назад аниматором, поэтому силуэт читается и сбоку.
 */
public final class RoseWingModel extends WingModel {
   private static final float SPINE_INSET = 1.0F;
   private static final float WING_TIP = 27.0F;
   private static final float WING_TOP = 6.0F;
   private static final float WING_BOTTOM = -22.0F;
   private static final float WING_Z = 0.0F;

   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart leftWing = createPart(0, 0);
   private final WingModelPart rightWing = createPart(0, 0);

   public RoseWingModel() {
      this.leftWing.setPivot(0.0F, 4.0F, 2.5F);
      this.rightWing.setPivot(0.0F, 4.0F, 2.5F);
      this.buildSide(1);
      this.buildSide(-1);
      this.root.addChild(this.leftWing);
      this.root.addChild(this.rightWing);
   }

   private void buildSide(int sign) {
      WingModelPart wing = sign > 0 ? this.rightWing : this.leftWing;
      if (sign > 0) {
         // корень у позвоночника (u = 0), кончик наружу (u = 1)
         wing.addPlane(SPINE_INSET, WING_TOP, WING_TIP, WING_BOTTOM, WING_Z, 0.0F, 0.0F, 1.0F, 1.0F);
      } else {
         wing.addPlane(-WING_TIP, WING_TOP, -SPINE_INSET, WING_BOTTOM, WING_Z, 1.0F, 0.0F, 0.0F, 1.0F);
      }
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
      SimpleWingAnimator simpleAnimator = (SimpleWingAnimator)animator;
      applyMirroredRotationDegrees(this.leftWing, this.rightWing, simpleAnimator.getRotationDegrees(partialTick));
      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
