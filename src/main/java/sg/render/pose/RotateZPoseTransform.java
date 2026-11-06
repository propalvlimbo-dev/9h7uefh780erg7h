package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import sg.render.pose.IndexedPoseTransform;

public class RotateZPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   private float Юф;
   private float Юи;
   private float Ю9;
   private float Юо;

   public RotateZPoseTransform(float var1, float var2, float var3, float var4) {
      this.Юо = var1;
      this.Юф = var2;
      this.Юи = var3;
      this.Ю9 = var4;
   }

   @Override
   public void К(PoseStack var1) {
      var1.rotateAround(Axis.ZP.rotationDegrees(this.Юо), this.Юф, this.Юи, this.Ю9);
   }
}
