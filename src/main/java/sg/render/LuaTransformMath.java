package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import sg.annotation.LuaExport;

public class LuaTransformMath {
   @LuaExport("PI")
   public double ЯИ;
   private static final double ВШО = (float) Math.PI;

   @LuaExport("rotateY")
   public void rotateY(PoseStack matrices, double angleDegrees) {
      matrices.mulPose(Axis.YP.rotationDegrees((float)angleDegrees));
   }

   @LuaExport("clamp")
   public double clamp(double value, double minimum, double maximum) {
      return Math.clamp(value, minimum, maximum);
   }

   @LuaExport("pow")
   public double pow(double base, double exponent) {
      return Math.pow(base, exponent);
   }

   @LuaExport("cos")
   public double cos(double angleRadians) {
      return Math.cos(angleRadians);
   }

   @LuaExport("abs")
   public double abs(double value) {
      return Math.abs(value);
   }

   @LuaExport("moveX")
   public void moveX(PoseStack matrices, double offset) {
      matrices.translate(offset, 0.0, 0.0);
   }

   @LuaExport("push")
   public void push(PoseStack matrices) {
      matrices.pushPose();
   }

   @LuaExport("moveY")
   public void moveY(PoseStack matrices, double offset) {
      matrices.translate(0.0, offset, 0.0);
   }

   @LuaExport
   public double дФ(double var1) {
      return Math.round(var1);
   }

   @LuaExport("moveZ")
   public void moveZ(PoseStack matrices, double offset) {
      matrices.translate(0.0, 0.0, offset);
   }

   @LuaExport
   public double дl(double var1) {
      return Math.ceil(var1);
   }

   @LuaExport("rotateZ")
   public void rotateZ(PoseStack matrices, double angleDegrees, double pivotX, double pivotY, double pivotZ) {
      matrices.rotateAround(Axis.ZP.rotationDegrees((float)angleDegrees), (float)pivotX, (float)pivotY, (float)pivotZ);
   }

   @LuaExport("translate")
   public void translate(PoseStack matrices, double x, double y, double z) {
      matrices.translate(x, y, z);
   }

   @LuaExport("shear")
   public void shear(PoseStack matrices, double var2, double var4, double var6) {
      Matrix4f var8 = new Matrix4f(
         1.0F, (float)var2, (float)var2, 0.0F, (float)var4, 1.0F, (float)var4, 0.0F, (float)var6, (float)var6, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F
      );
      matrices.last().pose().mul(var8);
   }

   @LuaExport("pop")
   public void pop(PoseStack matrices) {
      matrices.popPose();
   }

   @LuaExport
   public double дн(double var1) {
      return Math.floor(var1);
   }

   @LuaExport("rotateX")
   public void rotateX(PoseStack matrices, double angleDegrees) {
      matrices.mulPose(Axis.XP.rotationDegrees((float)angleDegrees));
   }

   @LuaExport("scale")
   public void scale(PoseStack matrices, double xScale, double yScale, double zScale) {
      matrices.scale((float)xScale, (float)yScale, (float)zScale);
   }

   public LuaTransformMath() {
      this.ЯИ = ВШО;
   }

   @LuaExport("sin")
   public double sin(double angleRadians) {
      return Math.sin(angleRadians);
   }

   @LuaExport("rotateY")
   public void rotateY(PoseStack matrices, double angleDegrees, double pivotX, double pivotY, double pivotZ) {
      matrices.rotateAround(Axis.YP.rotationDegrees((float)angleDegrees), (float)pivotX, (float)pivotY, (float)pivotZ);
   }

   @LuaExport("rotateX")
   public void rotateX(PoseStack matrices, double angleDegrees, double pivotX, double pivotY, double pivotZ) {
      matrices.rotateAround(Axis.XP.rotationDegrees((float)angleDegrees), (float)pivotX, (float)pivotY, (float)pivotZ);
   }

   @LuaExport("lerp")
   public double lerp(double progress, double start, double end) {
      return Mth.lerp(progress, start, end);
   }

   @LuaExport("rotateZ")
   public void rotateZ(PoseStack matrices, double angleDegrees) {
      matrices.mulPose(Axis.ZP.rotationDegrees((float)angleDegrees));
   }
}
