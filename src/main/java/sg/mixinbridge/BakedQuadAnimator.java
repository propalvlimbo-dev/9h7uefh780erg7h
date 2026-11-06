package sg.mixinbridge;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import sg.annotation.LuaExport;
import sg.render.pose.TranslateXPoseTransform;
import sg.render.pose.RotateYPoseTransform;
import sg.render.pose.RotateXPoseTransform;
import sg.render.pose.ScalePoseTransform;
import sg.render.pose.RotateZPoseTransform;
import sg.render.pose.TranslateZPoseTransform;
import sg.render.pose.TranslateYPoseTransform;
import sg.render.pose.IndexedPoseTransform;

public class BakedQuadAnimator {
   private final ArrayList<IndexedPoseTransform> п = new ArrayList<>();

   @LuaExport("rotateZ")
   public void rotateZ(int firstQuad, int lastQuad, float angleDegrees) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new RotateZPoseTransform(angleDegrees, 0.0F, 0.0F, 0.0F));
      }
   }

   @LuaExport("moveY")
   public void moveY(int firstQuad, int lastQuad, float offset) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new TranslateYPoseTransform(offset));
      }
   }

   @LuaExport("rotateX")
   public void rotateX(int firstQuad, int lastQuad, float angleDegrees, float pivotX, float pivotY, float pivotZ) {
      for (int var7 = firstQuad; var7 <= lastQuad; var7++) {
         this.method_8(var7, new RotateXPoseTransform(angleDegrees, pivotX, pivotY, pivotZ));
      }
   }

   public void Ну() {
      this.п.clear();
   }

   @LuaExport("moveZ")
   public void moveZ(int firstQuad, int lastQuad, float offset) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new TranslateZPoseTransform(offset));
      }
   }

   @LuaExport("rotateY")
   public void rotateY(int firstQuad, int lastQuad, float angleDegrees, float pivotX, float pivotY, float pivotZ) {
      for (int var7 = firstQuad; var7 <= lastQuad; var7++) {
         this.method_8(var7, new RotateYPoseTransform(angleDegrees, pivotX, pivotY, pivotZ));
      }
   }

   @LuaExport("rotateX")
   public void rotateX(int firstQuad, int lastQuad, float angleDegrees) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new RotateXPoseTransform(angleDegrees, 0.0F, 0.0F, 0.0F));
      }
   }

   @LuaExport("scale")
   public void scale(int firstQuad, int lastQuad, float xScale, float yScale, float zScale) {
      for (int var6 = firstQuad; var6 <= lastQuad; var6++) {
         this.method_8(var6, new ScalePoseTransform(xScale, yScale, zScale));
      }
   }

   public void method_8(int var1, PoseStack var2) {
      this.п.reversed().forEach(var2x -> {
         if (var2x.Хя == var1) {
            var2x.К(var2);
         }
      });
   }

   private void method_8(int var1, IndexedPoseTransform var2) {
      var2.Хя = var1;
      this.п.add(var2);
   }

   @LuaExport("rotateZ")
   public void rotateZ(int firstQuad, int lastQuad, float angleDegrees, float pivotX, float pivotY, float pivotZ) {
      for (int var7 = firstQuad; var7 <= lastQuad; var7++) {
         this.method_8(var7, new RotateZPoseTransform(angleDegrees, pivotX, pivotY, pivotZ));
      }
   }

   @LuaExport("rotateY")
   public void rotateY(int firstQuad, int lastQuad, float angleDegrees) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new RotateYPoseTransform(angleDegrees, 0.0F, 0.0F, 0.0F));
      }
   }

   @LuaExport("moveX")
   public void moveX(int firstQuad, int lastQuad, float offset) {
      for (int var4 = firstQuad; var4 <= lastQuad; var4++) {
         this.method_8(var4, new TranslateXPoseTransform(offset));
      }
   }
}
