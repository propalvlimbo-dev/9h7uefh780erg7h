package sg.render.pose;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import sg.render.pose.IndexedPoseTransform;

public class TranslateYPoseTransform extends IndexedPoseTransform implements PoseTransformMarker {
   @SerializedName("4Ю")
   float field_4Ю;

   public TranslateYPoseTransform(float var1) {
      this.field_4Ю = var1;
   }

   @Override
   public void К(PoseStack var1) {
      var1.translate(0.0F, this.field_4Ю, 0.0F);
   }
}
