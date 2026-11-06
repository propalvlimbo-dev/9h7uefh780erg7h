package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

public final class GeometryBoundsConsumer implements VertexConsumer {
   @SerializedName("4Х")
   private float minX = Float.POSITIVE_INFINITY;
   @SerializedName("4е")
   private float minY = Float.POSITIVE_INFINITY;
   @SerializedName("4А")
   private float minZ = Float.POSITIVE_INFINITY;
   @SerializedName("44")
   private float maxX = Float.NEGATIVE_INFINITY;
   @SerializedName("4п")
   private float maxY = Float.NEGATIVE_INFINITY;
   @SerializedName("4Д")
   private float maxZ = Float.NEGATIVE_INFINITY;
   private static final float Z_AXIS_SCALE = -1.0F;
   private static final float FLIPPED_Z_ROTATION_DEGREES = 180.0F;
   private static final float MIN_FIT_WIDTH = 1.0E-4F;
   private static final float MIN_FIT_HEIGHT = 1.0E-4F;
   private static final float X_CENTER_FACTOR = 0.5F;
   private static final float Y_CENTER_FACTOR = 0.5F;
   private static final float Z_CENTER_FACTOR = 0.5F;

   @Override
   public VertexConsumer setColor(int red, int green, int blue, int alpha) {
      return this;
   }

   public static void orient(PoseStack matrices, float xRotationDegrees, float yRotationDegrees, boolean flipped) {
      matrices.scale(1.0F, 1.0F, Z_AXIS_SCALE);
      matrices.mulPose(Axis.XP.rotationDegrees(xRotationDegrees));
      matrices.mulPose(Axis.YP.rotationDegrees(yRotationDegrees));
      if (flipped) {
         matrices.mulPose(Axis.ZP.rotationDegrees(FLIPPED_Z_ROTATION_DEGREES));
      }
   }

   @Override
   public VertexConsumer setLineWidth(float width) {
      return this;
   }

   @Override
   public VertexConsumer setUv2(int lightU, int lightV) {
      return this;
   }

   public void fit(PoseStack matrices, float x, float y, float width, float height) {
      if (!Float.isFinite(this.minX)) {
         matrices.translate(x, y, 0.0F);
      } else {
         float scale = Math.min(width / Math.max(MIN_FIT_WIDTH, this.maxX - this.minX), height / Math.max(MIN_FIT_HEIGHT, this.maxY - this.minY));
         matrices.translate(
            x - (this.minX + this.maxX) * X_CENTER_FACTOR * scale,
            y - (this.minY + this.maxY) * Y_CENTER_FACTOR * scale,
            -(this.minZ + this.maxZ) * Z_CENTER_FACTOR * scale
         );
         matrices.scale(scale, scale, scale);
      }
   }

   @Override
   public VertexConsumer setUv1(int overlayU, int overlayV) {
      return this;
   }

   @Override
   public VertexConsumer setNormal(float x, float y, float z) {
      return this;
   }

   @Override
   public VertexConsumer setUv(float u, float v) {
      return this;
   }

   @Override
   public VertexConsumer addVertex(float x, float y, float z) {
      if (Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z)) {
         this.minX = Math.min(this.minX, x);
         this.minY = Math.min(this.minY, y);
         this.minZ = Math.min(this.minZ, z);
         this.maxX = Math.max(this.maxX, x);
         this.maxY = Math.max(this.maxY, y);
         this.maxZ = Math.max(this.maxZ, z);
      }

      return this;
   }

   @Override
   public VertexConsumer setColor(int packedColor) {
      return this;
   }
}
