package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;

public final class BackpackModel {
   public static final Identifier BODY_TEXTURE = Identifier.fromNamespaceAndPath(BackpackModel.BODY_TEXTURE_NAMESPACE, BackpackModel.BODY_TEXTURE_PATH);
   public static final Identifier OVERLAY_TEXTURE = Identifier.fromNamespaceAndPath(BackpackModel.OVERLAY_TEXTURE_NAMESPACE, BackpackModel.OVERLAY_TEXTURE_PATH);
   private static final float BODY_CUBOID_0_X0 = 3.0F;
   private static final float BODY_CUBOID_0_Z0 = 6.0F;
   private static final float BODY_CUBOID_0_X1 = 13.0F;
   private static final float BODY_CUBOID_0_Y1 = 9.0F;
   private static final float BODY_CUBOID_0_Z1 = 11.0F;
   private static final float BODY_CUBOID_0_NEGATIVE_Y_U0 = 2.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Y_V0 = 4.0F;
   private static final float BODY_CUBOID_0_NEGATIVE_Y_U1 = 7.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Y_V1 = 6.5F;
   private static final float BODY_CUBOID_0_POSITIVE_Y_U0 = 7.5F;
   private static final float BODY_CUBOID_0_POSITIVE_Y_V0 = 4.0F;
   private static final float BODY_CUBOID_0_POSITIVE_Y_U1 = 12.5F;
   private static final float BODY_CUBOID_0_POSITIVE_Y_V1 = 6.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Z_U0 = 2.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Z_V0 = 6.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Z_U1 = 7.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_Z_V1 = 11.0F;
   private static final float BODY_CUBOID_0_POSITIVE_Z_U0 = 10.0F;
   private static final float BODY_CUBOID_0_POSITIVE_Z_V0 = 6.5F;
   private static final float BODY_CUBOID_0_POSITIVE_Z_U1 = 15.0F;
   private static final float BODY_CUBOID_0_POSITIVE_Z_V1 = 11.0F;
   private static final float BODY_CUBOID_0_NEGATIVE_X_U0 = 7.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_X_V0 = 6.5F;
   private static final float BODY_CUBOID_0_NEGATIVE_X_U1 = 10.0F;
   private static final float BODY_CUBOID_0_NEGATIVE_X_V1 = 11.0F;
   private static final float BODY_CUBOID_0_POSITIVE_X_V0 = 6.5F;
   private static final float BODY_CUBOID_0_POSITIVE_X_U1 = 2.5F;
   private static final float BODY_CUBOID_0_POSITIVE_X_V1 = 11.0F;
   private static final float BODY_CUBOID_1_X0 = 4.0F;
   private static final float BODY_CUBOID_1_Z0 = 4.0F;
   private static final float BODY_CUBOID_1_X1 = 12.0F;
   private static final float BODY_CUBOID_1_Y1 = 7.0F;
   private static final float BODY_CUBOID_1_Z1 = 6.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Y_U0 = 5.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Y_V0 = 11.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Y_U1 = 9.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Y_V1 = 12.0F;
   private static final float BODY_CUBOID_1_POSITIVE_Y_V0 = 11.0F;
   private static final float BODY_CUBOID_1_POSITIVE_Y_U1 = 5.0F;
   private static final float BODY_CUBOID_1_POSITIVE_Y_V1 = 12.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Z_V0 = 12.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Z_U1 = 5.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_Z_V1 = 15.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_X_U0 = 5.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_X_V0 = 12.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_X_U1 = 6.0F;
   private static final float BODY_CUBOID_1_NEGATIVE_X_V1 = 15.0F;
   private static final float BODY_CUBOID_1_POSITIVE_X_V0 = 12.0F;
   private static final float BODY_CUBOID_1_POSITIVE_X_V1 = 15.0F;
   private static final float BODY_SHARED_NEGATIVE_Y_U0 = 11.0F;
   private static final float BODY_SHARED_NEGATIVE_Y_V0 = 11.0F;
   private static final float BODY_SHARED_NEGATIVE_Y_U1 = 11.5F;
   private static final float BODY_SHARED_NEGATIVE_Y_V1 = 11.5F;
   private static final float BODY_SHARED_POSITIVE_Y_U0 = 10.5F;
   private static final float BODY_SHARED_POSITIVE_Y_V0 = 11.0F;
   private static final float BODY_SHARED_POSITIVE_Y_U1 = 11.0F;
   private static final float BODY_SHARED_POSITIVE_Y_V1 = 11.5F;
   private static final float BODY_SHARED_POSITIVE_Z_U0 = 10.5F;
   private static final float BODY_SHARED_POSITIVE_Z_V0 = 11.5F;
   private static final float BODY_SHARED_POSITIVE_Z_U1 = 11.0F;
   private static final float BODY_SHARED_POSITIVE_Z_V1 = 15.5F;
   private static final float BODY_SHARED_NEGATIVE_X_U0 = 10.0F;
   private static final float BODY_SHARED_NEGATIVE_X_V0 = 11.5F;
   private static final float BODY_SHARED_NEGATIVE_X_U1 = 10.5F;
   private static final float BODY_SHARED_NEGATIVE_X_V1 = 15.5F;
   private static final float BODY_SHARED_POSITIVE_X_U0 = 11.0F;
   private static final float BODY_SHARED_POSITIVE_X_V0 = 11.5F;
   private static final float BODY_SHARED_POSITIVE_X_U1 = 11.5F;
   private static final float BODY_SHARED_POSITIVE_X_V1 = 15.5F;
   private static final float BODY_CUBOID_2_X0 = 4.0F;
   private static final float BODY_CUBOID_2_Y0 = 3.0F;
   private static final float BODY_CUBOID_2_Z0 = 11.0F;
   private static final float BODY_CUBOID_2_X1 = 5.0F;
   private static final float BODY_CUBOID_2_Y1 = 11.0F;
   private static final float BODY_CUBOID_2_Z1 = 12.0F;
   private static final float BODY_CUBOID_3_X0 = 11.0F;
   private static final float BODY_CUBOID_3_Y0 = 3.0F;
   private static final float BODY_CUBOID_3_Z0 = 11.0F;
   private static final float BODY_CUBOID_3_X1 = 12.0F;
   private static final float BODY_CUBOID_3_Y1 = 11.0F;
   private static final float BODY_CUBOID_3_Z1 = 12.0F;
   private static final float BODY_CUBOID_4_X0 = 3.0F;
   private static final float BODY_CUBOID_4_Y0 = 9.0F;
   private static final float BODY_CUBOID_4_Z0 = 6.0F;
   private static final float BODY_CUBOID_4_X1 = 13.0F;
   private static final float BODY_CUBOID_4_Y1 = 12.0F;
   private static final float BODY_CUBOID_4_Z1 = 11.0F;
   private static final float BODY_CUBOID_4_NEGATIVE_Y_U0 = 7.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Y_U1 = 12.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Y_V1 = 2.5F;
   private static final float BODY_CUBOID_4_POSITIVE_Y_U0 = 2.5F;
   private static final float BODY_CUBOID_4_POSITIVE_Y_U1 = 7.5F;
   private static final float BODY_CUBOID_4_POSITIVE_Y_V1 = 2.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Z_U0 = 2.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Z_V0 = 2.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Z_U1 = 7.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_Z_V1 = 4.0F;
   private static final float BODY_CUBOID_4_POSITIVE_Z_U0 = 10.0F;
   private static final float BODY_CUBOID_4_POSITIVE_Z_V0 = 2.5F;
   private static final float BODY_CUBOID_4_POSITIVE_Z_U1 = 15.0F;
   private static final float BODY_CUBOID_4_POSITIVE_Z_V1 = 4.0F;
   private static final float BODY_CUBOID_4_NEGATIVE_X_U0 = 7.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_X_V0 = 2.5F;
   private static final float BODY_CUBOID_4_NEGATIVE_X_U1 = 10.0F;
   private static final float BODY_CUBOID_4_NEGATIVE_X_V1 = 4.0F;
   private static final float BODY_CUBOID_4_POSITIVE_X_V0 = 2.5F;
   private static final float BODY_CUBOID_4_POSITIVE_X_U1 = 2.5F;
   private static final float BODY_CUBOID_4_POSITIVE_X_V1 = 4.0F;
   private static final float OVERLAY_QUAD_0_X0 = 4.0F;
   private static final float OVERLAY_QUAD_0_Z = 3.995F;
   private static final float OVERLAY_QUAD_0_X1 = 12.0F;
   private static final float OVERLAY_QUAD_0_Y1 = 7.0F;
   private static final float OVERLAY_QUAD_0_V0 = 12.0F;
   private static final float OVERLAY_QUAD_0_U1 = 5.0F;
   private static final float OVERLAY_QUAD_0_V1 = 15.0F;
   private static final float OVERLAY_QUAD_1_X0 = 3.0F;
   private static final float OVERLAY_QUAD_1_Y0 = 9.0F;
   private static final float OVERLAY_QUAD_1_Z = 5.995F;
   private static final float OVERLAY_QUAD_1_X1 = 13.0F;
   private static final float OVERLAY_QUAD_1_Y1 = 12.0F;
   private static final float OVERLAY_QUAD_1_U0 = 2.5F;
   private static final float OVERLAY_QUAD_1_V0 = 2.5F;
   private static final float OVERLAY_QUAD_1_U1 = 7.5F;
   private static final float OVERLAY_QUAD_1_V1 = 4.0F;
   private static final float STANDARD_U0_TEXEL_DIVISOR = 16.0F;
   private static final float STANDARD_V0_TEXEL_DIVISOR = 16.0F;
   private static final float STANDARD_U1_TEXEL_DIVISOR = 16.0F;
   private static final float STANDARD_V1_TEXEL_DIVISOR = 16.0F;
   private static final float ROTATABLE_U0_TEXEL_DIVISOR = 16.0F;
   private static final float ROTATABLE_V0_TEXEL_DIVISOR = 16.0F;
   private static final float ROTATABLE_U1_TEXEL_DIVISOR = 16.0F;
   private static final float ROTATABLE_V1_TEXEL_DIVISOR = 16.0F;
   private static final float CUBOID_X0_ORIGIN = 8.0F;
   private static final float CUBOID_X0_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBOID_X1_ORIGIN = 8.0F;
   private static final float CUBOID_X1_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBOID_Y1_ORIGIN = 12.0F;
   private static final float CUBOID_Y1_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBOID_Y0_ORIGIN = 12.0F;
   private static final float CUBOID_Y0_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBOID_Z0_ORIGIN = 8.0F;
   private static final float CUBOID_Z0_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float CUBOID_Z1_ORIGIN = 8.0F;
   private static final float CUBOID_Z1_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float NEGATIVE_Y_NORMAL_COMPONENT = -1.0F;
   private static final float NEGATIVE_Z_NORMAL_COMPONENT = -1.0F;
   private static final float NEGATIVE_X_NORMAL_COMPONENT = -1.0F;
   private static final float OVERLAY_X0_ORIGIN = 8.0F;
   private static final float OVERLAY_X0_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float OVERLAY_X1_ORIGIN = 8.0F;
   private static final float OVERLAY_X1_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float OVERLAY_Y1_ORIGIN = 12.0F;
   private static final float OVERLAY_Y1_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float OVERLAY_Y0_ORIGIN = 12.0F;
   private static final float OVERLAY_Y0_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float OVERLAY_Z_ORIGIN = 8.0F;
   private static final float OVERLAY_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float OVERLAY_NEGATIVE_Z_NORMAL_COMPONENT = -1.0F;
   private static final String BODY_TEXTURE_NAMESPACE = "rose";
   private static final String BODY_TEXTURE_PATH = "textures/cosmetics/backpacks/backpack.png";
   private static final String OVERLAY_TEXTURE_NAMESPACE = "rose";
   private static final String OVERLAY_TEXTURE_PATH = "textures/cosmetics/backpacks/backpack_overlay.png";

   public static void renderBody(
      PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, BackpackColorVariant variant, float alpha
   ) {
      float red = variant.getRedComponent();
      float green = variant.getGreenComponent();
      float blue = variant.getBlueComponent();
      renderCuboid(
         matrices,
         vertices,
         BODY_CUBOID_0_X0,
         0.0F,
         BODY_CUBOID_0_Z0,
         BODY_CUBOID_0_X1,
         BODY_CUBOID_0_Y1,
         BODY_CUBOID_0_Z1,
         createFaceUv(BODY_CUBOID_0_NEGATIVE_Y_U0, BODY_CUBOID_0_NEGATIVE_Y_V0, BODY_CUBOID_0_NEGATIVE_Y_U1, BODY_CUBOID_0_NEGATIVE_Y_V1, true),
         createFaceUv(BODY_CUBOID_0_POSITIVE_Y_U0, BODY_CUBOID_0_POSITIVE_Y_V0, BODY_CUBOID_0_POSITIVE_Y_U1, BODY_CUBOID_0_POSITIVE_Y_V1, true),
         createFaceUv(BODY_CUBOID_0_NEGATIVE_Z_U0, BODY_CUBOID_0_NEGATIVE_Z_V0, BODY_CUBOID_0_NEGATIVE_Z_U1, BODY_CUBOID_0_NEGATIVE_Z_V1),
         createFaceUv(BODY_CUBOID_0_POSITIVE_Z_U0, BODY_CUBOID_0_POSITIVE_Z_V0, BODY_CUBOID_0_POSITIVE_Z_U1, BODY_CUBOID_0_POSITIVE_Z_V1),
         createFaceUv(BODY_CUBOID_0_NEGATIVE_X_U0, BODY_CUBOID_0_NEGATIVE_X_V0, BODY_CUBOID_0_NEGATIVE_X_U1, BODY_CUBOID_0_NEGATIVE_X_V1),
         createFaceUv(0.0F, BODY_CUBOID_0_POSITIVE_X_V0, BODY_CUBOID_0_POSITIVE_X_U1, BODY_CUBOID_0_POSITIVE_X_V1),
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
      renderCuboid(
         matrices,
         vertices,
         BODY_CUBOID_1_X0,
         1.0F,
         BODY_CUBOID_1_Z0,
         BODY_CUBOID_1_X1,
         BODY_CUBOID_1_Y1,
         BODY_CUBOID_1_Z1,
         createFaceUv(BODY_CUBOID_1_NEGATIVE_Y_U0, BODY_CUBOID_1_NEGATIVE_Y_V0, BODY_CUBOID_1_NEGATIVE_Y_U1, BODY_CUBOID_1_NEGATIVE_Y_V1, true),
         createFaceUv(1.0F, BODY_CUBOID_1_POSITIVE_Y_V0, BODY_CUBOID_1_POSITIVE_Y_U1, BODY_CUBOID_1_POSITIVE_Y_V1, true),
         createFaceUv(1.0F, BODY_CUBOID_1_NEGATIVE_Z_V0, BODY_CUBOID_1_NEGATIVE_Z_U1, BODY_CUBOID_1_NEGATIVE_Z_V1),
         null,
         createFaceUv(BODY_CUBOID_1_NEGATIVE_X_U0, BODY_CUBOID_1_NEGATIVE_X_V0, BODY_CUBOID_1_NEGATIVE_X_U1, BODY_CUBOID_1_NEGATIVE_X_V1),
         createFaceUv(0.0F, BODY_CUBOID_1_POSITIVE_X_V0, 1.0F, BODY_CUBOID_1_POSITIVE_X_V1),
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
      BackpackModel.FaceUv sharedNegativeYUv = createFaceUv(BODY_SHARED_NEGATIVE_Y_U0, BODY_SHARED_NEGATIVE_Y_V0, BODY_SHARED_NEGATIVE_Y_U1, BODY_SHARED_NEGATIVE_Y_V1, true);
      BackpackModel.FaceUv sharedPositiveYUv = createFaceUv(BODY_SHARED_POSITIVE_Y_U0, BODY_SHARED_POSITIVE_Y_V0, BODY_SHARED_POSITIVE_Y_U1, BODY_SHARED_POSITIVE_Y_V1, true);
      BackpackModel.FaceUv sharedPositiveZUv = createFaceUv(BODY_SHARED_POSITIVE_Z_U0, BODY_SHARED_POSITIVE_Z_V0, BODY_SHARED_POSITIVE_Z_U1, BODY_SHARED_POSITIVE_Z_V1);
      BackpackModel.FaceUv sharedNegativeXUv = createFaceUv(BODY_SHARED_NEGATIVE_X_U0, BODY_SHARED_NEGATIVE_X_V0, BODY_SHARED_NEGATIVE_X_U1, BODY_SHARED_NEGATIVE_X_V1);
      BackpackModel.FaceUv sharedPositiveXUv = createFaceUv(BODY_SHARED_POSITIVE_X_U0, BODY_SHARED_POSITIVE_X_V0, BODY_SHARED_POSITIVE_X_U1, BODY_SHARED_POSITIVE_X_V1);
      renderCuboid(matrices, vertices, BODY_CUBOID_2_X0, BODY_CUBOID_2_Y0, BODY_CUBOID_2_Z0, BODY_CUBOID_2_X1, BODY_CUBOID_2_Y1, BODY_CUBOID_2_Z1, sharedNegativeYUv, sharedPositiveYUv, null, sharedPositiveZUv, sharedNegativeXUv, sharedPositiveXUv, packedLight, packedOverlay, red, green, blue, alpha);
      renderCuboid(matrices, vertices, BODY_CUBOID_3_X0, BODY_CUBOID_3_Y0, BODY_CUBOID_3_Z0, BODY_CUBOID_3_X1, BODY_CUBOID_3_Y1, BODY_CUBOID_3_Z1, sharedNegativeYUv, sharedPositiveYUv, null, sharedPositiveZUv, sharedNegativeXUv, sharedPositiveXUv, packedLight, packedOverlay, red, green, blue, alpha);
      renderCuboid(
         matrices,
         vertices,
         BODY_CUBOID_4_X0,
         BODY_CUBOID_4_Y0,
         BODY_CUBOID_4_Z0,
         BODY_CUBOID_4_X1,
         BODY_CUBOID_4_Y1,
         BODY_CUBOID_4_Z1,
         createFaceUv(BODY_CUBOID_4_NEGATIVE_Y_U0, 0.0F, BODY_CUBOID_4_NEGATIVE_Y_U1, BODY_CUBOID_4_NEGATIVE_Y_V1, true),
         createFaceUv(BODY_CUBOID_4_POSITIVE_Y_U0, 0.0F, BODY_CUBOID_4_POSITIVE_Y_U1, BODY_CUBOID_4_POSITIVE_Y_V1, true),
         createFaceUv(BODY_CUBOID_4_NEGATIVE_Z_U0, BODY_CUBOID_4_NEGATIVE_Z_V0, BODY_CUBOID_4_NEGATIVE_Z_U1, BODY_CUBOID_4_NEGATIVE_Z_V1),
         createFaceUv(BODY_CUBOID_4_POSITIVE_Z_U0, BODY_CUBOID_4_POSITIVE_Z_V0, BODY_CUBOID_4_POSITIVE_Z_U1, BODY_CUBOID_4_POSITIVE_Z_V1),
         createFaceUv(BODY_CUBOID_4_NEGATIVE_X_U0, BODY_CUBOID_4_NEGATIVE_X_V0, BODY_CUBOID_4_NEGATIVE_X_U1, BODY_CUBOID_4_NEGATIVE_X_V1),
         createFaceUv(0.0F, BODY_CUBOID_4_POSITIVE_X_V0, BODY_CUBOID_4_POSITIVE_X_U1, BODY_CUBOID_4_POSITIVE_X_V1),
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
   }

   private static BackpackModel.FaceUv createFaceUv(float u0, float v0, float u1, float v1, boolean rotate) {
      return new BackpackModel.FaceUv(u0 / ROTATABLE_U0_TEXEL_DIVISOR, v0 / ROTATABLE_V0_TEXEL_DIVISOR, u1 / ROTATABLE_U1_TEXEL_DIVISOR, v1 / ROTATABLE_V1_TEXEL_DIVISOR, rotate);
   }

   private static void emitFace(
      PoseStack matrices,
      VertexConsumer vertices,
      BackpackModel.FaceUv uv,
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
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      if (uv != null) {
         Pose pose = matrices.last();
         Vector3f transformedNormal = pose.normal().transform(new Vector3f(normalX, normalY, normalZ)).normalize();
         if (uv.rotate) {
            emitVertex(pose, vertices, x0, y0, z0, uv.u1, uv.v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x1, y1, z1, uv.u1, uv.v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x2, y2, z2, uv.u0, uv.v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x3, y3, z3, uv.u0, uv.v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
         } else {
            emitVertex(pose, vertices, x0, y0, z0, uv.u0, uv.v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x1, y1, z1, uv.u0, uv.v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x2, y2, z2, uv.u1, uv.v1, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
            emitVertex(pose, vertices, x3, y3, z3, uv.u1, uv.v0, transformedNormal, packedLight, packedOverlay, red, green, blue, alpha);
         }
      }
   }

   private BackpackModel() {
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

   public static void renderOverlay(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float alpha) {
      renderOverlayQuad(matrices, vertices, OVERLAY_QUAD_0_X0, 1.0F, OVERLAY_QUAD_0_Z, OVERLAY_QUAD_0_X1, OVERLAY_QUAD_0_Y1, createFaceUv(1.0F, OVERLAY_QUAD_0_V0, OVERLAY_QUAD_0_U1, OVERLAY_QUAD_0_V1), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
      renderOverlayQuad(matrices, vertices, OVERLAY_QUAD_1_X0, OVERLAY_QUAD_1_Y0, OVERLAY_QUAD_1_Z, OVERLAY_QUAD_1_X1, OVERLAY_QUAD_1_Y1, createFaceUv(OVERLAY_QUAD_1_U0, OVERLAY_QUAD_1_V0, OVERLAY_QUAD_1_U1, OVERLAY_QUAD_1_V1), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
   }

   private static BackpackModel.FaceUv createFaceUv(float u0, float v0, float u1, float v1) {
      return new BackpackModel.FaceUv(u0 / STANDARD_U0_TEXEL_DIVISOR, v0 / STANDARD_V0_TEXEL_DIVISOR, u1 / STANDARD_U1_TEXEL_DIVISOR, v1 / STANDARD_V1_TEXEL_DIVISOR, false);
   }

   private static void renderCuboid(
      PoseStack matrices,
      VertexConsumer vertices,
      float x0,
      float y0,
      float z0,
      float x1,
      float y1,
      float z1,
      BackpackModel.FaceUv negativeYUv,
      BackpackModel.FaceUv positiveYUv,
      BackpackModel.FaceUv negativeZUv,
      BackpackModel.FaceUv positiveZUv,
      BackpackModel.FaceUv negativeXUv,
      BackpackModel.FaceUv positiveXUv,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      float minX = (x0 - CUBOID_X0_ORIGIN) / CUBOID_X0_MODEL_UNITS_PER_BLOCK;
      float maxX = (x1 - CUBOID_X1_ORIGIN) / CUBOID_X1_MODEL_UNITS_PER_BLOCK;
      float minY = (CUBOID_Y1_ORIGIN - y1) / CUBOID_Y1_MODEL_UNITS_PER_BLOCK;
      float maxY = (CUBOID_Y0_ORIGIN - y0) / CUBOID_Y0_MODEL_UNITS_PER_BLOCK;
      float minZ = (z0 - CUBOID_Z0_ORIGIN) / CUBOID_Z0_MODEL_UNITS_PER_BLOCK;
      float maxZ = (z1 - CUBOID_Z1_ORIGIN) / CUBOID_Z1_MODEL_UNITS_PER_BLOCK;
      emitFace(
         matrices,
         vertices,
         negativeYUv,
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
         0.0F,
         NEGATIVE_Y_NORMAL_COMPONENT,
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
         positiveYUv,
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
         negativeZUv,
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
         0.0F,
         0.0F,
         NEGATIVE_Z_NORMAL_COMPONENT,
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
         positiveZUv,
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
      emitFace(
         matrices,
         vertices,
         negativeXUv,
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
         NEGATIVE_X_NORMAL_COMPONENT,
         0.0F,
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
         positiveXUv,
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

   private static void renderOverlayQuad(
      PoseStack matrices,
      VertexConsumer vertices,
      float x0,
      float y0,
      float z,
      float x1,
      float y1,
      BackpackModel.FaceUv uv,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      float minX = (x0 - OVERLAY_X0_ORIGIN) / OVERLAY_X0_MODEL_UNITS_PER_BLOCK;
      float maxX = (x1 - OVERLAY_X1_ORIGIN) / OVERLAY_X1_MODEL_UNITS_PER_BLOCK;
      float minY = (OVERLAY_Y1_ORIGIN - y1) / OVERLAY_Y1_MODEL_UNITS_PER_BLOCK;
      float maxY = (OVERLAY_Y0_ORIGIN - y0) / OVERLAY_Y0_MODEL_UNITS_PER_BLOCK;
      float modelZ = (z - OVERLAY_Z_ORIGIN) / OVERLAY_Z_MODEL_UNITS_PER_BLOCK;
      emitFace(
         matrices,
         vertices,
         uv,
         minX,
         minY,
         modelZ,
         minX,
         maxY,
         modelZ,
         maxX,
         maxY,
         modelZ,
         maxX,
         minY,
         modelZ,
         0.0F,
         0.0F,
         OVERLAY_NEGATIVE_Z_NORMAL_COMPONENT,
         packedLight,
         packedOverlay,
         red,
         green,
         blue,
         alpha
      );
   }

   private record FaceUv(float u0, float v0, float u1, float v1, boolean rotate) {
   }
}
