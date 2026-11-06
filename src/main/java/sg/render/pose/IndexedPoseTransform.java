package sg.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import sg.render.pose.PoseTransformMarker;

public class IndexedPoseTransform implements PoseTransformMarker {
   public static int Хц = 0;
   public int Хя = 0;
   public int ес = Хц;

   public void К(PoseStack var1) {
   }

   public int Я() {
      return this.ес;
   }

   public void Я(int var1) {
      this.ес = var1;
   }

   protected IndexedPoseTransform() {
   }
}
