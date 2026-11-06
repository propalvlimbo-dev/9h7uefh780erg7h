package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import sg.render.pose.IndexedPoseTransform;

public class RotateXPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   private float ЮШ;
   private float Юы;
   private float Юй;
   private float ЮЖ;

   public RotateXPoseTransform(float var1, float var2, float var3, float var4) {
      this.ЮЖ = var1;
      this.ЮШ = var2;
      this.Юы = var3;
      this.Юй = var4;
   }

   @Override
   public void К(PoseStack var1) {
      var1.rotateAround(Axis.XP.rotationDegrees(this.ЮЖ), this.ЮШ, this.Юы, this.Юй);
   }
}
