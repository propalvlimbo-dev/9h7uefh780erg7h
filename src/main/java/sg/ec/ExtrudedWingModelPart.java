package sg.ec;

import org.joml.Vector3f;
import sg.render.WingModelPart;

public final class ExtrudedWingModelPart extends WingModelPart {
   private static final float NEGATIVE_Z_NORMAL_COMPONENT = -1.0F;
   private static final float U_HALF_TEXEL_FACTOR = 0.5F;
   private static final float V_HALF_TEXEL_FACTOR = 0.5F;
   private static final float NEGATIVE_X_NORMAL_COMPONENT = -1.0F;
   private static final float NEGATIVE_Y_NORMAL_COMPONENT = -1.0F;

   public static ExtrudedWingModelPart create(float x, float y, float z, int width, int height, int textureU, int textureV, int textureWidth, int textureHeight) {
      return new ExtrudedWingModelPart(x, y, z, width, height, textureU, textureV, textureU + width, textureV + height, textureWidth, textureHeight);
   }

   private ExtrudedWingModelPart(float x, float y, float z, int width, int height, float u0, float v0, float u1, float v1, int textureWidth, int textureHeight) {
      super(textureWidth, textureHeight, 0, 0);
      float minX = x;
      float maxX = x + width;
      float minY = y;
      float maxY = y + height;
      float minZ = z;
      float maxZ = z + 1.0F;
      this.addExtrudedFace(minX, minY, minZ, maxX, maxY, minZ, u0, v0, u1, v1, new Vector3f(0.0F, 0.0F, NEGATIVE_Z_NORMAL_COMPONENT), false);
      this.addExtrudedFace(minX, maxY, maxZ, maxX, minY, maxZ, u0, v1, u1, v0, new Vector3f(0.0F, 0.0F, 1.0F), false);
      float halfTexelU = U_HALF_TEXEL_FACTOR * (u0 - u1) / width;
      float halfTexelV = V_HALF_TEXEL_FACTOR * (v0 - v1) / height;

      for (int column = 0; column < width; column++) {
         float xEdge = minX + column;
         float uCenter = u0 + (u1 - u0) * ((float)column / width) - halfTexelU;
         this.addExtrudedFace(xEdge, minY, minZ, xEdge, maxY, maxZ, uCenter, v0, uCenter, v1, new Vector3f(NEGATIVE_X_NORMAL_COMPONENT, 0.0F, 0.0F), false);
      }

      for (int column = 0; column < width; column++) {
         float uCenter = u0 + (u1 - u0) * ((float)column / width) - halfTexelU;
         float xEdge = minX + (column + 1);
         this.addExtrudedFace(xEdge, maxY, minZ, xEdge, minY, maxZ, uCenter, v1, uCenter, v0, new Vector3f(1.0F, 0.0F, 0.0F), false);
      }

      for (int row = 0; row < height; row++) {
         float vCenter = v0 + (v1 - v0) * ((float)row / height) - halfTexelV;
         float yEdge = minY + (row + 1);
         this.addExtrudedFace(minX, yEdge, minZ, maxX, yEdge, maxZ, u0, vCenter, u1, vCenter, new Vector3f(0.0F, 1.0F, 0.0F), true);
      }

      for (int row = 0; row < height; row++) {
         float yEdge = minY + row;
         float vCenter = v0 + (v1 - v0) * ((float)row / height) - halfTexelV;
         this.addExtrudedFace(maxX, yEdge, minZ, minX, yEdge, maxZ, u1, vCenter, u0, vCenter, new Vector3f(0.0F, NEGATIVE_Y_NORMAL_COMPONENT, 0.0F), true);
      }
   }

   private void addExtrudedFace(
      float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, Vector3f normal, boolean depthAlongV
   ) {
      float[][] positions = new float[][]{{x1, y0, z0}, {x0, y0, depthAlongV ? z0 : z1}, {x0, y1, z1}, {x1, y1, depthAlongV ? z1 : z0}};
      this.addTexturedFace(positions, u0, v0, u1, v1, normal);
   }
}
