package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import sg.render.pose.IndexedPoseTransform;

public class RotateYPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   private float ЮИ;
   private float Юь;
   private float Юж;
   private float Ю弟;

   @Override
   public void К(PoseStack var1) {
      var1.rotateAround(Axis.YP.rotationDegrees(this.Ю弟), this.ЮИ, this.Юь, this.Юж);
   }

   public RotateYPoseTransform(float var1, float var2, float var3, float var4) {
      this.Ю弟 = var1;
      this.ЮИ = var2;
      this.Юь = var3;
      this.Юж = var4;
   }
}
