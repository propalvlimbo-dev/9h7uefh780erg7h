package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import sg.render.pose.IndexedPoseTransform;

public class TranslateZPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   float ВЭ;

   public TranslateZPoseTransform(float var1) {
      this.ВЭ = var1;
   }

   @Override
   public void К(PoseStack var1) {
      var1.translate(0.0F, 0.0F, this.ВЭ);
   }
}
