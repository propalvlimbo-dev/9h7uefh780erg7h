package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sg.render.SimpleWingAnimator;
import sg.render.WingAnimator;

/**
 * Роза — не плоский билборд: текстура лежит на сегментированной изогнутой
 * оболочке, а поверх неё проходят тонкие объёмные жилки-лепестки. Геометрия
 * гнётся вместе с крылом и сохраняет исходную HD-UV-развёртку.
 */
public final class RoseWingModel extends WingModel {
   private static final float SPINE_INSET = 1.0F;
   private static final float WING_TIP = 27.0F;
   private static final float WING_TOP = -18.0F;
   private static final float WING_BOTTOM = 10.0F;
   private static final float WING_SCALE = 0.82F;
   private static final float WING_Z = 0.0F;
   private static final float PIVOT_Y = 4.0F;
   private static final float PIVOT_Z = 2.5F;
   private static final float MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final int SPAN_SEGMENTS = 14;
   private static final int FEATHER_SEGMENTS = 10;
   private static final int RIB_SEGMENTS = 8;
   private static final float RIB_TEXTURE_U = 0.75F;
   private static final float RIB_TEXTURE_V = 0.5F;
   private static final float RIB_DEPTH = 0.16F;
   private static final float RIB_HALF_WIDTH = 0.075F;
   private static final float VEIN_HALF_WIDTH = 0.045F;
   private static final float PI = (float)Math.PI;

   @Override
   public void render(
      WingAnimator animator,
      float partialTick,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      if (!(animator instanceof SimpleWingAnimator simpleAnimator)) {
         return;
      }

      Vec3 rotationDegrees = simpleAnimator.getRotationDegrees(partialTick);
      float timeSeconds = (float)(System.nanoTime() * 1.0E-9);
      this.renderWing(-1, rotationDegrees, timeSeconds, matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
      this.renderWing(1, rotationDegrees, timeSeconds, matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private void renderWing(
      int side,
      Vec3 rotationDegrees,
      float timeSeconds,
      PoseStack matrices,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      matrices.pushPose();
      matrices.translate(0.0F, PIVOT_Y / MODEL_UNITS_PER_BLOCK, PIVOT_Z / MODEL_UNITS_PER_BLOCK);
      matrices.scale(WING_SCALE, WING_SCALE, WING_SCALE);
      float mirror = side > 0 ? -1.0F : 1.0F;
      matrices.mulPose(Axis.ZP.rotation(WingAnimationMath.toRadians((float)rotationDegrees.z * mirror)));
      matrices.mulPose(Axis.YP.rotation(WingAnimationMath.toRadians((float)rotationDegrees.y * mirror)));
      matrices.mulPose(Axis.XP.rotation(WingAnimationMath.toRadians((float)rotationDegrees.x)));

      Pose pose = matrices.last();
      this.renderCurvedShell(side, timeSeconds, pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);
      this.renderPetalVeins(side, timeSeconds, pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);
      matrices.popPose();
   }

   private void renderCurvedShell(
      int side,
      float timeSeconds,
      Pose pose,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      for (int spanIndex = 0; spanIndex < SPAN_SEGMENTS; spanIndex++) {
         float u0 = (float)spanIndex / SPAN_SEGMENTS;
         float u1 = (float)(spanIndex + 1) / SPAN_SEGMENTS;

         for (int featherIndex = 0; featherIndex < FEATHER_SEGMENTS; featherIndex++) {
            float v0 = (float)featherIndex / FEATHER_SEGMENTS;
            float v1 = (float)(featherIndex + 1) / FEATHER_SEGMENTS;
            SurfacePoint innerTop = this.surfacePoint(side, u0, v0, timeSeconds, 0.0F);
            SurfacePoint outerTop = this.surfacePoint(side, u1, v0, timeSeconds, 0.0F);
            SurfacePoint outerBottom = this.surfacePoint(side, u1, v1, timeSeconds, 0.0F);
            SurfacePoint innerBottom = this.surfacePoint(side, u0, v1, timeSeconds, 0.0F);
            Vector3f normal = this.surfaceNormal(side, (u0 + u1) * 0.5F, (v0 + v1) * 0.5F, timeSeconds);
            // Keep the artwork's front face consistently aimed away from the player's back.
            if (normal.z > 0.0F) {
               normal.negate();
            }

            // Keep the source texture's spine at u=0 on both sides; signed X mirrors the wing.
            SurfacePoint frontFirst = side > 0 ? outerTop : innerTop;
            float frontFirstU = side > 0 ? u1 : u0;
            SurfacePoint frontSecond = side > 0 ? innerTop : outerTop;
            float frontSecondU = side > 0 ? u0 : u1;
            SurfacePoint frontThird = side > 0 ? innerBottom : outerBottom;
            float frontThirdU = side > 0 ? u0 : u1;
            SurfacePoint frontFourth = side > 0 ? outerBottom : innerBottom;
            float frontFourthU = side > 0 ? u1 : u0;
            this.emitSurfaceVertex(pose, vertices, frontFirst, frontFirstU, v0, normal, packedLight, packedOverlay, red, green, blue, alpha, 1.0F);
            this.emitSurfaceVertex(pose, vertices, frontSecond, frontSecondU, v0, normal, packedLight, packedOverlay, red, green, blue, alpha, 1.0F);
            this.emitSurfaceVertex(pose, vertices, frontThird, frontThirdU, v1, normal, packedLight, packedOverlay, red, green, blue, alpha, 1.0F);
            this.emitSurfaceVertex(pose, vertices, frontFourth, frontFourthU, v1, normal, packedLight, packedOverlay, red, green, blue, alpha, 1.0F);

            Vector3f backNormal = new Vector3f(normal).negate();
            this.emitSurfaceVertex(pose, vertices, frontFourth, frontFourthU, v1, backNormal, packedLight, packedOverlay, red, green, blue, alpha, 0.84F);
            this.emitSurfaceVertex(pose, vertices, frontThird, frontThirdU, v1, backNormal, packedLight, packedOverlay, red, green, blue, alpha, 0.84F);
            this.emitSurfaceVertex(pose, vertices, frontSecond, frontSecondU, v0, backNormal, packedLight, packedOverlay, red, green, blue, alpha, 0.84F);
            this.emitSurfaceVertex(pose, vertices, frontFirst, frontFirstU, v0, backNormal, packedLight, packedOverlay, red, green, blue, alpha, 0.84F);
         }
      }
   }

   /** Draw a luminous rachis and short feather veins into the curved surface. */
   private void renderPetalVeins(
      int side,
      float timeSeconds,
      Pose pose,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      this.renderRibbon(side, 0.055F, 0.49F, 0.77F, 0.49F, RIB_HALF_WIDTH, RIB_DEPTH, timeSeconds, pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);

      for (int index = 0; index < 5; index++) {
         float startU = 0.15F + index * 0.12F;
         float endU = startU + 0.10F;
         this.renderRibbon(side, startU, 0.49F, endU, 0.34F, VEIN_HALF_WIDTH, RIB_DEPTH, timeSeconds, pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         this.renderRibbon(side, startU, 0.49F, endU, 0.64F, VEIN_HALF_WIDTH, RIB_DEPTH, timeSeconds, pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);
      }
   }

   private void renderRibbon(
      int side,
      float startU,
      float startV,
      float endU,
      float endV,
      float halfWidth,
      float depthOffset,
      float timeSeconds,
      Pose pose,
      VertexConsumer vertices,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      float spanWidth = WING_TIP - SPINE_INSET;
      float tangentX = side * spanWidth * (endU - startU);
      float tangentY = (WING_BOTTOM - WING_TOP) * (endV - startV);
      float tangentLength = Math.max(1.0E-4F, (float)Math.sqrt(tangentX * tangentX + tangentY * tangentY));
      float offsetU = -tangentY / tangentLength * halfWidth / (side * spanWidth);
      float offsetV = tangentX / tangentLength * halfWidth / (WING_BOTTOM - WING_TOP);
      Vector3f normal = this.surfaceNormal(side, (startU + endU) * 0.5F, (startV + endV) * 0.5F, timeSeconds);
      if (normal.z > 0.0F) {
         normal.negate();
      }
      float pulse = 0.78F + 0.22F * (float)Math.sin(timeSeconds * 1.7F + startU * 7.0F + side * 0.4F);
      float detailRed = red;
      float detailGreen = green * 0.68F;
      float detailBlue = blue * 0.86F;
      float detailAlpha = alpha * pulse * 0.68F;

      for (int segment = 0; segment < RIB_SEGMENTS; segment++) {
         float t0 = (float)segment / RIB_SEGMENTS;
         float t1 = (float)(segment + 1) / RIB_SEGMENTS;
         float u0 = lerp(startU, endU, t0);
         float v0 = lerp(startV, endV, t0);
         float u1 = lerp(startU, endU, t1);
         float v1 = lerp(startV, endV, t1);
         SurfacePoint a = this.surfacePoint(side, clamp01(u0 - offsetU), clamp01(v0 - offsetV), timeSeconds, -depthOffset);
         SurfacePoint b = this.surfacePoint(side, clamp01(u1 - offsetU), clamp01(v1 - offsetV), timeSeconds, -depthOffset);
         SurfacePoint c = this.surfacePoint(side, clamp01(u1 + offsetU), clamp01(v1 + offsetV), timeSeconds, -depthOffset);
         SurfacePoint d = this.surfacePoint(side, clamp01(u0 + offsetU), clamp01(v0 + offsetV), timeSeconds, -depthOffset);

         this.emitSurfaceVertex(pose, vertices, d, RIB_TEXTURE_U, RIB_TEXTURE_V, normal, packedLight, packedOverlay, detailRed, detailGreen, detailBlue, detailAlpha, 1.0F);
         this.emitSurfaceVertex(pose, vertices, c, RIB_TEXTURE_U, RIB_TEXTURE_V, normal, packedLight, packedOverlay, detailRed, detailGreen, detailBlue, detailAlpha, 1.0F);
         this.emitSurfaceVertex(pose, vertices, b, RIB_TEXTURE_U, RIB_TEXTURE_V, normal, packedLight, packedOverlay, detailRed, detailGreen, detailBlue, detailAlpha, 1.0F);
         this.emitSurfaceVertex(pose, vertices, a, RIB_TEXTURE_U, RIB_TEXTURE_V, normal, packedLight, packedOverlay, detailRed, detailGreen, detailBlue, detailAlpha, 1.0F);
      }
   }

   private SurfacePoint surfacePoint(int side, float u, float v, float timeSeconds, float depthOffset) {
      float span = WING_TIP - SPINE_INSET;
      float arch = (float)Math.sin(PI * u);
      float tipFraction = u * u;
      float featherScallop = (float)Math.sin(u * PI * 14.0F) * 0.22F * v;
      float breathing = (float)Math.sin(timeSeconds * 1.8F + u * 4.0F + (side > 0 ? 0.45F : 0.0F)) * tipFraction * 0.16F;
      float x = side * (SPINE_INSET + span * u);
      float y = lerp(WING_TOP, WING_BOTTOM, v) + arch * (v - 0.5F) * 1.35F + featherScallop + breathing * (0.4F + v);
      float z = WING_Z - 1.85F * arch + 0.38F * arch * (float)Math.sin(PI * v) + breathing * 1.6F + depthOffset;
      return new SurfacePoint(x, y, z);
   }

   private Vector3f surfaceNormal(int side, float u, float v, float timeSeconds) {
      float epsilon = 1.0E-3F;
      float u0 = Math.max(0.0F, u - epsilon);
      float u1 = Math.min(1.0F, u + epsilon);
      float v0 = Math.max(0.0F, v - epsilon);
      float v1 = Math.min(1.0F, v + epsilon);
      SurfacePoint alongSpanStart = this.surfacePoint(side, u0, v, timeSeconds, 0.0F);
      SurfacePoint alongSpanEnd = this.surfacePoint(side, u1, v, timeSeconds, 0.0F);
      SurfacePoint alongFeathersStart = this.surfacePoint(side, u, v0, timeSeconds, 0.0F);
      SurfacePoint alongFeathersEnd = this.surfacePoint(side, u, v1, timeSeconds, 0.0F);
      Vector3f spanTangent = new Vector3f(
         alongSpanEnd.x() - alongSpanStart.x(),
         alongSpanEnd.y() - alongSpanStart.y(),
         alongSpanEnd.z() - alongSpanStart.z()
      );
      Vector3f featherTangent = new Vector3f(
         alongFeathersEnd.x() - alongFeathersStart.x(),
         alongFeathersEnd.y() - alongFeathersStart.y(),
         alongFeathersEnd.z() - alongFeathersStart.z()
      );
      return spanTangent.cross(featherTangent).normalize();
   }

   private void emitSurfaceVertex(
      Pose pose,
      VertexConsumer vertices,
      SurfacePoint point,
      float u,
      float v,
      Vector3f normal,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha,
      float shade
   ) {
      Matrix4f positionMatrix = pose.pose();
      Matrix3f normalMatrix = pose.normal();
      Vector3f transformedPosition = positionMatrix.transformPosition(
         new Vector3f(point.x() / MODEL_UNITS_PER_BLOCK, point.y() / MODEL_UNITS_PER_BLOCK, point.z() / MODEL_UNITS_PER_BLOCK)
      );
      Vector3f transformedNormal = normalMatrix.transform(new Vector3f(normal)).normalize();
      vertices.addVertex(transformedPosition.x, transformedPosition.y, transformedPosition.z)
         .setColor(red * shade, green * shade, blue * shade, alpha)
         .setUv(u, v)
         .setOverlay(packedOverlay)
         .setLight(packedLight)
         .setNormal(transformedNormal.x, transformedNormal.y, transformedNormal.z);
   }

   private static float lerp(float start, float end, float delta) {
      return start + (end - start) * delta;
   }

   private static float clamp01(float value) {
      return Math.clamp(value, 0.0F, 1.0F);
   }

   private record SurfacePoint(float x, float y, float z) {
   }
}
