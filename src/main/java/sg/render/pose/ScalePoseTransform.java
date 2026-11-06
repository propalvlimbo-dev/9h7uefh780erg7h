package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import sg.render.pose.IndexedPoseTransform;

public class ScalePoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   float сВ;
   float сЯ;
   float сХ;

   @Override
   public void К(PoseStack var1) {
      var1.scale(this.сВ, this.сЯ, this.сХ);
   }

   public ScalePoseTransform(float var1, float var2, float var3) {
      this.сВ = var1;
      this.сЯ = var2;
      this.сХ = var3;
   }
}
