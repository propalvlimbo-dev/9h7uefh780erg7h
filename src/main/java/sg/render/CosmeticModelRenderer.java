package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import java.util.Collection;
import org.joml.Vector3f;

public final class CosmeticModelRenderer {
   private static final float RELATIVE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ANIMATION_POSITION_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ANIMATION_POSITION_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ANIMATION_POSITION_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float TINT_RED_CHANNEL_DIVISOR = 255.0F;
   private static final float TINT_GREEN_CHANNEL_DIVISOR = 255.0F;
   private static final float TINT_BLUE_CHANNEL_DIVISOR = 255.0F;
   private static final float CUBE_ORIGIN_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float INVERTED_CUBE_ORIGIN_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float INVERTED_CUBE_HEIGHT_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_ORIGIN_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_ORIGIN_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_WIDTH_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_HEIGHT_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_DEPTH_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_INFLATE_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_INFLATE_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBE_INFLATE_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ZERO_WIDTH_HALF_EXPANSION = 5.0E-5F;
   private static final float ZERO_WIDTH_EXPANSION = 1.0E-4F;
   private static final float ZERO_HEIGHT_HALF_EXPANSION = 5.0E-5F;
   private static final float ZERO_HEIGHT_EXPANSION = 1.0E-4F;
   private static final float ZERO_DEPTH_HALF_EXPANSION = 5.0E-5F;
   private static final float ZERO_DEPTH_EXPANSION = 1.0E-4F;
   private static final String NORTH_FACE_KEY = "north";
   private static final float NORTH_NORMAL_Z = -1.0F;
   private static final String SOUTH_FACE_KEY = "south";
   private static final String WEST_FACE_KEY = "west";
   private static final float WEST_NORMAL_X = -1.0F;
   private static final String EAST_FACE_KEY = "east";
   private static final String UP_FACE_KEY = "up";
   private static final String DOWN_FACE_KEY = "down";
   private static final float DOWN_NORMAL_Y = -1.0F;
   private static final float FLAT_HEIGHT_DOWN_NORMAL_Y = -1.0F;
   private static final float BOX_DOWN_NORMAL_Y = -1.0F;
   private static final float BOX_NORTH_NORMAL_Z = -1.0F;
   private static final float BOX_WEST_NORMAL_X = -1.0F;

   private static void renderCube(
      CosmeticModel.ModelCube cube,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      boolean mirrorUv,
      int textureWidth,
      int textureHeight,
      float red,
      float green,
      float blue,
      float alpha,
      boolean invertY
   ) {
      float x = cube.originX / CUBE_ORIGIN_X_MODEL_UNITS_PER_BLOCK;
      float y = invertY ? -cube.originY / INVERTED_CUBE_ORIGIN_Y_MODEL_UNITS_PER_BLOCK - cube.height / INVERTED_CUBE_HEIGHT_MODEL_UNITS_PER_BLOCK : cube.originY / CUBE_ORIGIN_Y_MODEL_UNITS_PER_BLOCK;
      float z = cube.originZ / CUBE_ORIGIN_Z_MODEL_UNITS_PER_BLOCK;
      float width = cube.width / CUBE_WIDTH_MODEL_UNITS_PER_BLOCK;
      float height = cube.height / CUBE_HEIGHT_MODEL_UNITS_PER_BLOCK;
      float depth = cube.depth / CUBE_DEPTH_MODEL_UNITS_PER_BLOCK;
      float inflateX = cube.inflateX / CUBE_INFLATE_X_MODEL_UNITS_PER_BLOCK;
      float inflateY = cube.inflateY / CUBE_INFLATE_Y_MODEL_UNITS_PER_BLOCK;
      float inflateZ = cube.inflateZ / CUBE_INFLATE_Z_MODEL_UNITS_PER_BLOCK;
      boolean zeroWidth = width == 0.0F;
      boolean zeroHeight = height == 0.0F;
      boolean zeroDepth = depth == 0.0F;
      if (zeroWidth) {
         x -= ZERO_WIDTH_HALF_EXPANSION;
         width = ZERO_WIDTH_EXPANSION;
      }

      if (zeroHeight) {
         y -= ZERO_HEIGHT_HALF_EXPANSION;
         height = ZERO_HEIGHT_EXPANSION;
      }

      if (zeroDepth) {
         z -= ZERO_DEPTH_HALF_EXPANSION;
         depth = ZERO_DEPTH_EXPANSION;
      }

      if (!cube.faceUvs.isEmpty()) {
         renderPerFaceUvCuboid(matrices, vertices, cube, x, y, z, width, height, depth, inflateX, inflateY, inflateZ, textureWidth, textureHeight, packedLight, packedOverlay, red, green, blue, alpha, mirrorUv);
      } else {
         renderBoxUvCuboid(
            matrices,
            vertices,
            x,
            y,
            z,
            width,
            height,
            depth,
            inflateX,
            inflateY,
            inflateZ,
            cube.textureU,
            cube.textureV,
            cube.width,
            cube.height,
            cube.depth,
            textureWidth,
            textureHeight,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha,
            mirrorUv,
            zeroWidth,
            zeroHeight,
            zeroDepth
         );
      }
   }

   private static void emitFace(
      PoseStack matrices,
      VertexConsumer vertices,
      float x0,
      float y0,
      float z0,
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float x3,
      float y3,
      float z3,
      float u0,
      float v0,
      float u1,
      float v1,
      float normalX,
      float normalY,
      float normalZ,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      Pose pose = matrices.last();
      Vector3f transformedNormal = pose.normal().transform(new Vector3f(normalX, normalY, normalZ)).normalize();
      emitVertex(pose, vertices, x0, y0, z0, u0, v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
      emitVertex(pose, vertices, x1, y1, z1, u0, v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
      emitVertex(pose, vertices, x2, y2, z2, u1, v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
      emitVertex(pose, vertices, x3, y3, z3, u1, v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private CosmeticModelRenderer() {
   }

   private static void renderAbsolutePivotBone(
      CosmeticModel.ModelBone bone,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      int textureWidth,
      int textureHeight,
      float red,
      float green,
      float blue,
      float alpha,
      CosmeticAnimationClip clip,
      float time,
      Collection<String> excludedBones
   ) {
      if (excludedBones == null || !excludedBones.contains(bone.name)) {
         matrices.pushPose();
         CosmeticAnimationClip.TransformSample animationSample = clip == null ? null : clip.э(bone.name, time);
         float[] animationPosition = animationSample == null ? null : animationSample.position();
         float[] animationRotation = animationSample == null ? null : animationSample.rotation();
         float[] animationScale = animationSample == null ? null : animationSample.scale();
         float positionX = animationPosition == null ? 0.0F : -animationPosition[0];
         float positionY = animationPosition == null ? 0.0F : animationPosition[1];
         float positionZ = animationPosition == null ? 0.0F : animationPosition[2];
         float xRotationDegrees = bone.xRotationDegrees + (animationRotation == null ? 0.0F : -animationRotation[0]);
         float yRotationDegrees = bone.yRotationDegrees + (animationRotation == null ? 0.0F : -animationRotation[1]);
         float zRotationDegrees = bone.zRotationDegrees + (animationRotation == null ? 0.0F : animationRotation[2]);
         matrices.translate(positionX / ANIMATION_POSITION_X_MODEL_UNITS_PER_BLOCK, positionY / ANIMATION_POSITION_Y_MODEL_UNITS_PER_BLOCK, positionZ / ANIMATION_POSITION_Z_MODEL_UNITS_PER_BLOCK);
         matrices.translate(bone.pivotX / ABSOLUTE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK, bone.pivotY / ABSOLUTE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, bone.pivotZ / ABSOLUTE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK);
         if (zRotationDegrees != 0.0F) {
            matrices.mulPose(Axis.ZP.rotationDegrees(zRotationDegrees));
         }

         if (yRotationDegrees != 0.0F) {
            matrices.mulPose(Axis.YP.rotationDegrees(yRotationDegrees));
         }

         if (xRotationDegrees != 0.0F) {
            matrices.mulPose(Axis.XP.rotationDegrees(xRotationDegrees));
         }

         if (animationScale != null) {
            matrices.scale(animationScale[0], animationScale[1], animationScale[2]);
         }

         matrices.translate(-bone.pivotX / ABSOLUTE_BONE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK, -bone.pivotY / ABSOLUTE_BONE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK, -bone.pivotZ / ABSOLUTE_BONE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK);

         for (CosmeticModel.ModelCube cube : bone.cubes) {
            matrices.pushPose();
            matrices.translate(cube.pivotX / ABSOLUTE_CUBE_PIVOT_X_MODEL_UNITS_PER_BLOCK, cube.pivotY / ABSOLUTE_CUBE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, cube.pivotZ / ABSOLUTE_CUBE_PIVOT_Z_MODEL_UNITS_PER_BLOCK);
            if (cube.zRotationDegrees != 0.0F) {
               matrices.mulPose(Axis.ZP.rotationDegrees(cube.zRotationDegrees));
            }

            if (cube.yRotationDegrees != 0.0F) {
               matrices.mulPose(Axis.YP.rotationDegrees(cube.yRotationDegrees));
            }

            if (cube.xRotationDegrees != 0.0F) {
               matrices.mulPose(Axis.XP.rotationDegrees(cube.xRotationDegrees));
            }

            matrices.translate(-cube.pivotX / ABSOLUTE_CUBE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK, -cube.pivotY / ABSOLUTE_CUBE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK, -cube.pivotZ / ABSOLUTE_CUBE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK);
            renderCube(
               cube,
               matrices,
               vertices,
               packedLight,
               packedOverlay,
               bone.mirrorUv || cube.mirrorUv,
               textureWidth,
               textureHeight,
               red * ((bone.tintColorRgb >> 16 & 0xFF) / TINT_RED_CHANNEL_DIVISOR),
               green * ((bone.tintColorRgb >> 8 & 0xFF) / TINT_GREEN_CHANNEL_DIVISOR),
               blue * ((bone.tintColorRgb & 0xFF) / TINT_BLUE_CHANNEL_DIVISOR),
               alpha,
               false
            );
            matrices.popPose();
         }

         for (CosmeticModel.ModelBone child : bone.children) {
            renderAbsolutePivotBone(child, matrices, vertices, packedLight, packedOverlay, textureWidth, textureHeight, red, green, blue, alpha, clip, time, excludedBones);
         }

         matrices.popPose();
      }
   }

   private static void renderRelativePivotBone(
      CosmeticModel.ModelBone bone,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      int textureWidth,
      int textureHeight,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      matrices.pushPose();
      matrices.translate(bone.pivotX / RELATIVE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK, -bone.pivotY / RELATIVE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, bone.pivotZ / RELATIVE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK);
      if (bone.zRotationDegrees != 0.0F) {
         matrices.mulPose(Axis.ZP.rotationDegrees(bone.zRotationDegrees));
      }

      if (bone.yRotationDegrees != 0.0F) {
         matrices.mulPose(Axis.YP.rotationDegrees(bone.yRotationDegrees));
      }

      if (bone.xRotationDegrees != 0.0F) {
         matrices.mulPose(Axis.XP.rotationDegrees(bone.xRotationDegrees));
      }

      for (CosmeticModel.ModelCube cube : bone.cubes) {
         renderCube(cube, matrices, vertices, packedLight, packedOverlay, bone.mirrorUv || cube.mirrorUv, textureWidth, textureHeight, red, green, blue, alpha, true);
      }

      for (CosmeticModel.ModelBone child : bone.children) {
         renderRelativePivotBone(child, matrices, vertices, packedLight, packedOverlay, textureWidth, textureHeight, red, green, blue, alpha);
      }

      matrices.popPose();
   }

   private static void emitUvRectFace(
      CosmeticModel.UvRect uv,
      PoseStack matrices,
      VertexConsumer vertices,
      float x0,
      float y0,
      float z0,
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float x3,
      float y3,
      float z3,
      float normalX,
      float normalY,
      float normalZ,
      int textureWidth,
      int textureHeight,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha,
      boolean mirrorUv
   ) {
      if (uv != null) {
         float u0 = uv.u() / textureWidth;
         float v0 = uv.v() / textureHeight;
         float u1 = (uv.u() + uv.width()) / textureWidth;
         float v1 = (uv.v() + uv.height()) / textureHeight;
         if (mirrorUv) {
            float previousU0 = u0;
            u0 = u1;
            u1 = previousU0;
         }

         emitFace(
            matrices,
            vertices,
            x0,
            y0,
            z0,
            x1,
            y1,
            z1,
            x2,
            y2,
            z2,
            x3,
            y3,
            z3,
            u0,
            v0,
            u1,
            v1,
            normalX,
            normalY,
            normalZ,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      }
   }

   private static void renderPerFaceUvCuboid(
      PoseStack matrices,
      VertexConsumer vertices,
      CosmeticModel.ModelCube cube,
      float x,
      float y,
      float z,
      float width,
      float height,
      float depth,
      float inflateX,
      float inflateY,
      float inflateZ,
      int textureWidth,
      int textureHeight,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha,
      boolean mirrorUv
   ) {
      float minX = x - inflateX;
      float minY = y - inflateY;
      float minZ = z - inflateZ;
      float maxX = x + width + inflateX;
      float maxY = y + height + inflateY;
      float maxZ = z + depth + inflateZ;
      emitUvRectFace(
         cube.faceUvs.get(NORTH_FACE_KEY),
         matrices,
         vertices,
         maxX,
         maxY,
         minZ,
         maxX,
         minY,
         minZ,
         minX,
         minY,
         minZ,
         minX,
         maxY,
         minZ,
         0.0F,
         0.0F,
         NORTH_NORMAL_Z,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
      emitUvRectFace(
         cube.faceUvs.get(SOUTH_FACE_KEY),
         matrices,
         vertices,
         minX,
         maxY,
         maxZ,
         minX,
         minY,
         maxZ,
         maxX,
         minY,
         maxZ,
         maxX,
         maxY,
         maxZ,
         0.0F,
         0.0F,
         1.0F,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
      emitUvRectFace(
         cube.faceUvs.get(WEST_FACE_KEY),
         matrices,
         vertices,
         minX,
         maxY,
         minZ,
         minX,
         minY,
         minZ,
         minX,
         minY,
         maxZ,
         minX,
         maxY,
         maxZ,
         WEST_NORMAL_X,
         0.0F,
         0.0F,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
      emitUvRectFace(
         cube.faceUvs.get(EAST_FACE_KEY),
         matrices,
         vertices,
         maxX,
         maxY,
         maxZ,
         maxX,
         minY,
         maxZ,
         maxX,
         minY,
         minZ,
         maxX,
         maxY,
         minZ,
         1.0F,
         0.0F,
         0.0F,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
      emitUvRectFace(
         cube.faceUvs.get(UP_FACE_KEY),
         matrices,
         vertices,
         minX,
         maxY,
         minZ,
         minX,
         maxY,
         maxZ,
         maxX,
         maxY,
         maxZ,
         maxX,
         maxY,
         minZ,
         0.0F,
         1.0F,
         0.0F,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
      emitUvRectFace(
         cube.faceUvs.get(DOWN_FACE_KEY),
         matrices,
         vertices,
         maxX,
         minY,
         minZ,
         maxX,
         minY,
         maxZ,
         minX,
         minY,
         maxZ,
         minX,
         minY,
         minZ,
         0.0F,
         DOWN_NORMAL_Y,
         0.0F,
         textureWidth,
         textureHeight,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha,
         mirrorUv
      );
   }

   public static void render(
      PoseStack matrices, VertexConsumer vertices, CosmeticModel model, CosmeticAnimationClip clip, float time, int packedLight, int packedOverlay, float red, float green, float blue, float alpha
   ) {
      render(matrices, vertices, model, clip, time, packedLight, packedOverlay, red, green, blue, alpha, null);
   }

   public static void render(
      PoseStack matrices,
      VertexConsumer vertices,
      CosmeticModel model,
      CosmeticAnimationClip clip,
      float time,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha,
      Collection<String> excludedBones
   ) {
      if (!model.absolutePivotTransforms) {
         for (CosmeticModel.ModelBone rootBone : model.rootBones) {
            renderRelativePivotBone(rootBone, matrices, vertices, packedLight, packedOverlay, model.textureWidth, model.textureHeight, red, green, blue, alpha);
         }
      } else {
         for (CosmeticModel.ModelBone rootBone : model.rootBones) {
            renderAbsolutePivotBone(rootBone, matrices, vertices, packedLight, packedOverlay, model.textureWidth, model.textureHeight, red, green, blue, alpha, clip, time, excludedBones);
         }
      }
   }

   private static void emitVertex(
      Pose pose,
      VertexConsumer vertices,
      float x,
      float y,
      float z,
      float u,
      float v,
      Vector3f normal,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      Vector3f transformedPosition = pose.pose().transformPosition(new Vector3f(x, y, z));
      vertices.addVertex(transformedPosition.x, transformedPosition.y, transformedPosition.z)
         .setColor(red, green, blue, alpha)
         .setUv(u, v)
         .setOverlay(packedOverlay)
         .setLight(packedLight)
         .setNormal(normal.x, normal.y, normal.z);
   }

   public static void render(PoseStack matrices, VertexConsumer vertices, CosmeticModel model, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      render(matrices, vertices, model, null, 0.0F, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private static void renderBoxUvCuboid(
      PoseStack matrices,
      VertexConsumer vertices,
      float x,
      float y,
      float z,
      float width,
      float height,
      float depth,
      float inflateX,
      float inflateY,
      float inflateZ,
      float textureU,
      float textureV,
      float boxUvWidth,
      float boxUvHeight,
      float boxUvDepth,
      int textureWidth,
      int textureHeight,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha,
      boolean mirrorUv,
      boolean zeroWidth,
      boolean zeroHeight,
      boolean zeroDepth
   ) {
      float inverseTextureWidth = 1.0F / textureWidth;
      float inverseTextureHeight = 1.0F / textureHeight;
      float minX = x - inflateX;
      float minY = y - inflateY;
      float minZ = z - inflateZ;
      float maxX = x + width + inflateX;
      float maxY = y + height + inflateY;
      float maxZ = z + depth + inflateZ;
      float downU0 = (textureU + boxUvDepth) * inverseTextureWidth;
      float downU1 = (textureU + boxUvDepth + boxUvWidth) * inverseTextureWidth;
      float downV0 = textureV * inverseTextureHeight;
      float downV1 = (textureV + boxUvDepth) * inverseTextureHeight;
      float upU0 = (textureU + boxUvDepth + boxUvWidth) * inverseTextureWidth;
      float upU1 = (textureU + boxUvDepth + boxUvWidth * 2.0F) * inverseTextureWidth;
      float upV0 = textureV * inverseTextureHeight;
      float upV1 = (textureV + boxUvDepth) * inverseTextureHeight;
      float northU0 = (textureU + boxUvDepth) * inverseTextureWidth;
      float northU1 = (textureU + boxUvDepth + boxUvWidth) * inverseTextureWidth;
      float northV0 = (textureV + boxUvDepth) * inverseTextureHeight;
      float northV1 = (textureV + boxUvDepth + boxUvHeight) * inverseTextureHeight;
      float southU0 = (textureU + boxUvDepth + boxUvWidth + boxUvDepth) * inverseTextureWidth;
      float southU1 = (textureU + boxUvDepth + boxUvWidth + boxUvDepth + boxUvWidth) * inverseTextureWidth;
      float southV0 = (textureV + boxUvDepth) * inverseTextureHeight;
      float southV1 = (textureV + boxUvDepth + boxUvHeight) * inverseTextureHeight;
      float eastU0 = (textureU + boxUvDepth + boxUvWidth) * inverseTextureWidth;
      float eastU1 = (textureU + boxUvDepth + boxUvWidth + boxUvDepth) * inverseTextureWidth;
      float eastV0 = (textureV + boxUvDepth) * inverseTextureHeight;
      float eastV1 = (textureV + boxUvDepth + boxUvHeight) * inverseTextureHeight;
      float westU0 = textureU * inverseTextureWidth;
      float westU1 = (textureU + boxUvDepth) * inverseTextureWidth;
      float westV0 = (textureV + boxUvDepth) * inverseTextureHeight;
      float westV1 = (textureV + boxUvDepth + boxUvHeight) * inverseTextureHeight;
      if (mirrorUv) {
         float previousU0 = westU0;
         westU0 = westU1;
         westU1 = previousU0;
         previousU0 = eastU0;
         eastU0 = eastU1;
         eastU1 = previousU0;
         previousU0 = northU0;
         northU0 = northU1;
         northU1 = previousU0;
         previousU0 = southU0;
         southU0 = southU1;
         southU1 = previousU0;
         previousU0 = downU0;
         downU0 = downU1;
         downU1 = previousU0;
         previousU0 = upU0;
         upU0 = upU1;
         upU1 = previousU0;
      }

      if (zeroHeight) {
         emitFace(
            matrices,
            vertices,
            minX,
            maxY,
            maxZ,
            minX,
            maxY,
            minZ,
            maxX,
            maxY,
            minZ,
            maxX,
            maxY,
            maxZ,
            downU0,
            downV0,
            downU1,
            downV1,
            0.0F,
            1.0F,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
         emitFace(
            matrices,
            vertices,
            minX,
            minY,
            maxZ,
            minX,
            minY,
            minZ,
            maxX,
            minY,
            minZ,
            maxX,
            minY,
            maxZ,
            downU0,
            downV0,
            downU1,
            downV1,
            0.0F,
            FLAT_HEIGHT_DOWN_NORMAL_Y,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      } else {
         emitFace(
            matrices,
            vertices,
            minX,
            maxY,
            minZ,
            minX,
            maxY,
            maxZ,
            maxX,
            maxY,
            maxZ,
            maxX,
            maxY,
            minZ,
            upU0,
            upV0,
            upU1,
            upV1,
            0.0F,
            1.0F,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
         emitFace(
            matrices,
            vertices,
            minX,
            minY,
            maxZ,
            minX,
            minY,
            minZ,
            maxX,
            minY,
            minZ,
            maxX,
            minY,
            maxZ,
            downU0,
            downV0,
            downU1,
            downV1,
            0.0F,
            BOX_DOWN_NORMAL_Y,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      }

      emitFace(
         matrices,
         vertices,
         minX,
         minY,
         minZ,
         minX,
         maxY,
         minZ,
         maxX,
         maxY,
         minZ,
         maxX,
         minY,
         minZ,
         northU0,
         northV0,
         northU1,
         northV1,
         0.0F,
         0.0F,
         BOX_NORTH_NORMAL_Z,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
      if (zeroDepth) {
         emitFace(
            matrices,
            vertices,
            minX,
            minY,
            maxZ,
            minX,
            maxY,
            maxZ,
            maxX,
            maxY,
            maxZ,
            maxX,
            minY,
            maxZ,
            northU0,
            northV0,
            northU1,
            northV1,
            0.0F,
            0.0F,
            1.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      } else {
         emitFace(
            matrices,
            vertices,
            maxX,
            minY,
            maxZ,
            maxX,
            maxY,
            maxZ,
            minX,
            maxY,
            maxZ,
            minX,
            minY,
            maxZ,
            southU0,
            southV0,
            southU1,
            southV1,
            0.0F,
            0.0F,
            1.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      }

      emitFace(
         matrices,
         vertices,
         minX,
         minY,
         maxZ,
         minX,
         maxY,
         maxZ,
         minX,
         maxY,
         minZ,
         minX,
         minY,
         minZ,
         westU0,
         westV0,
         westU1,
         westV1,
         BOX_WEST_NORMAL_X,
         0.0F,
         0.0F,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
      if (zeroWidth) {
         emitFace(
            matrices,
            vertices,
            maxX,
            minY,
            maxZ,
            maxX,
            maxY,
            maxZ,
            maxX,
            maxY,
            minZ,
            maxX,
            minY,
            minZ,
            westU0,
            westV0,
            westU1,
            westV1,
            1.0F,
            0.0F,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      } else {
         emitFace(
            matrices,
            vertices,
            maxX,
            minY,
            minZ,
            maxX,
            maxY,
            minZ,
            maxX,
            maxY,
            maxZ,
            maxX,
            minY,
            maxZ,
            eastU0,
            eastV0,
            eastU1,
            eastV1,
            1.0F,
            0.0F,
            0.0F,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
         );
      }
   }
}
