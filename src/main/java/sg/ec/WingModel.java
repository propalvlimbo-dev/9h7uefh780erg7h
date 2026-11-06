package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

public abstract class WingModel {
   public abstract void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

   public static WingModelPart createPart(int textureU, int textureV) {
      return new WingModelPart(64, 64, textureU, textureV);
   }

   public static void applyMirroredRotationDegrees(WingModelPart leftPart, WingModelPart rightPart, Vec3 rotationDegrees) {
      rightPart.xRotationRadians = leftPart.xRotationRadians = WingAnimationMath.toRadians((float)rotationDegrees.x);
      leftPart.yRotationRadians = WingAnimationMath.toRadians((float)rotationDegrees.y);
      rightPart.yRotationRadians = -leftPart.yRotationRadians;
      leftPart.zRotationRadians = WingAnimationMath.toRadians((float)rotationDegrees.z);
      rightPart.zRotationRadians = -leftPart.zRotationRadians;
   }
}
