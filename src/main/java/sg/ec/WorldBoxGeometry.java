package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class WorldBoxGeometry {
   private static final float MIN_LINE_LENGTH_SQUARED = 1.0E-6F;

   public static Matrix4f getPoseMatrix(PoseStack poseStack) {
      return poseStack.last().pose();
   }

   public static void appendLine(Pose pose, VertexConsumer vertices, float startX, float startY, float startZ, float endX, float endY, float endZ, int color) {
      if (Float.isFinite(startX) && Float.isFinite(startY) && Float.isFinite(startZ) && Float.isFinite(endX) && Float.isFinite(endY) && Float.isFinite(endZ)) {
         vertices.addVertex(pose, startX, startY, startZ).setColor(color);
         vertices.addVertex(pose, endX, endY, endZ).setColor(color);
      }
   }

   public static void appendBoxOutline(
      PoseStack poseStack, VertexConsumer vertices, Vec3 cameraPosition, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color
   ) {
      float relativeMinX = (float)(minX - cameraPosition.x);
      float relativeMinY = (float)(minY - cameraPosition.y);
      float relativeMinZ = (float)(minZ - cameraPosition.z);
      float relativeMaxX = (float)(maxX - cameraPosition.x);
      float relativeMaxY = (float)(maxY - cameraPosition.y);
      float relativeMaxZ = (float)(maxZ - cameraPosition.z);
      Pose pose = poseStack.last();
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMaxZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMinY, relativeMaxZ, relativeMinX, relativeMinY, relativeMaxZ, color);
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMinY, relativeMaxZ, relativeMinX, relativeMinY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMaxZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, color);
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMinX, relativeMaxY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, color);
      appendLineWithNormals(pose, vertices, relativeMaxX, relativeMinY, relativeMaxZ, relativeMaxX, relativeMaxY, relativeMaxZ, color);
      appendLineWithNormals(pose, vertices, relativeMinX, relativeMinY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, color);
   }

   private WorldBoxGeometry() {
   }

   public static void appendBox(
      PoseStack poseStack, VertexConsumer vertices, Vec3 cameraPosition, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color, boolean filled
   ) {
      float relativeMinX = (float)(minX - cameraPosition.x);
      float relativeMinY = (float)(minY - cameraPosition.y);
      float relativeMinZ = (float)(minZ - cameraPosition.z);
      float relativeMaxX = (float)(maxX - cameraPosition.x);
      float relativeMaxY = (float)(maxY - cameraPosition.y);
      float relativeMaxZ = (float)(maxZ - cameraPosition.z);
      Pose pose = poseStack.last();
      if (filled) {
         appendQuad(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMaxZ, relativeMinX, relativeMinY, relativeMaxZ, color);
         appendQuad(pose, vertices, relativeMinX, relativeMaxY, relativeMinZ, relativeMinX, relativeMaxY, relativeMaxZ, relativeMaxX, relativeMaxY, relativeMaxZ, relativeMaxX, relativeMaxY, relativeMinZ, color);
         appendQuad(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMinX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMinY, relativeMinZ, color);
         appendQuad(pose, vertices, relativeMinX, relativeMinY, relativeMaxZ, relativeMaxX, relativeMinY, relativeMaxZ, relativeMaxX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, color);
         appendQuad(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMinX, relativeMinY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMinZ, color);
         appendQuad(pose, vertices, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMaxZ, relativeMaxX, relativeMinY, relativeMaxZ, color);
      } else {
         appendLine(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMinY, relativeMaxZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMinY, relativeMaxZ, relativeMinX, relativeMinY, relativeMaxZ, color);
         appendLine(pose, vertices, relativeMinX, relativeMinY, relativeMaxZ, relativeMinX, relativeMinY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMinX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMaxY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMaxZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, color);
         appendLine(pose, vertices, relativeMinX, relativeMaxY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMinX, relativeMinY, relativeMinZ, relativeMinX, relativeMaxY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMinY, relativeMinZ, relativeMaxX, relativeMaxY, relativeMinZ, color);
         appendLine(pose, vertices, relativeMaxX, relativeMinY, relativeMaxZ, relativeMaxX, relativeMaxY, relativeMaxZ, color);
         appendLine(pose, vertices, relativeMinX, relativeMinY, relativeMaxZ, relativeMinX, relativeMaxY, relativeMaxZ, color);
      }
   }

   private static void appendQuad(
      Pose pose,
      VertexConsumer vertices,
      float firstX,
      float firstY,
      float firstZ,
      float secondX,
      float secondY,
      float secondZ,
      float thirdX,
      float thirdY,
      float thirdZ,
      float fourthX,
      float fourthY,
      float fourthZ,
      int color
   ) {
      vertices.addVertex(pose, firstX, firstY, firstZ).setColor(color);
      vertices.addVertex(pose, secondX, secondY, secondZ).setColor(color);
      vertices.addVertex(pose, thirdX, thirdY, thirdZ).setColor(color);
      vertices.addVertex(pose, fourthX, fourthY, fourthZ).setColor(color);
   }

   public static void appendLineWithNormals(Pose pose, VertexConsumer vertices, float startX, float startY, float startZ, float endX, float endY, float endZ, int color) {
      float directionX = endX - startX;
      float directionY = endY - startY;
      float directionZ = endZ - startZ;
      float lengthSquared = directionX * directionX + directionY * directionY + directionZ * directionZ;
      if (!(lengthSquared <= MIN_LINE_LENGTH_SQUARED)) {
         float inverseLength = 1.0F / (float)Math.sqrt(lengthSquared);
         directionX *= inverseLength;
         directionY *= inverseLength;
         directionZ *= inverseLength;
         vertices.addVertex(pose, startX, startY, startZ).setColor(color).setNormal(pose, directionX, directionY, directionZ).setLineWidth(2.0F);
         vertices.addVertex(pose, endX, endY, endZ).setColor(color).setNormal(pose, directionX, directionY, directionZ).setLineWidth(2.0F);
      }
   }
}
