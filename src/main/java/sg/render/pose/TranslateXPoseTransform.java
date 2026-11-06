package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import sg.render.pose.IndexedPoseTransform;

public class TranslateXPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   float Вц;

   public TranslateXPoseTransform(float var1) {
      this.Вц = var1;
   }

   @Override
   public void К(PoseStack var1) {
      var1.translate(this.Вц, 0.0F, 0.0F);
   }
}
