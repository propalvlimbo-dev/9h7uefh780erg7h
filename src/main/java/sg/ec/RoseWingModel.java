package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import sg.render.AvianWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * A high-resolution rose wing built as a curved textured membrane over four
 * articulated primary bones and four animated secondary feather panels. It uses
 * the project's smooth wing poses while preserving the 512px artwork.
 */
public final class RoseWingModel extends WingModel {
   private static final float SPINE_INSET = 1.0F;
   private static final float WING_TIP = 27.0F;
   private static final float WING_TOP = -18.0F;
   private static final float WING_BOTTOM = 10.0F;
   private static final float WING_SCALE = 0.82F;
   private static final float PIVOT_Y = 4.0F;
   private static final float PIVOT_Z = 2.5F;
   private static final float MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final int TEXTURE_SIZE = 512;
   private static final int SPAN_SEGMENTS = 14;
   private static final int FEATHER_SEGMENTS = 10;
   private static final int[] BONE_BOUNDARIES = {0, 3, 7, 11, SPAN_SEGMENTS};
   private static final float SURFACE_ARCH = 1.35F;
   private static final float SURFACE_DEPTH = 1.85F;
   private static final float SURFACE_BULGE = 0.38F;
   private static final float FEATHER_SCALLOP = 0.22F;
   private static final float SECONDARY_FEATHER_START = 0.62F;
   private static final float SECONDARY_ROTATION_SCALE = 0.12F;
   private static final float PI = (float)Math.PI;

   private final WingModelPart root = new WingModelPart(TEXTURE_SIZE, TEXTURE_SIZE, 0, 0);
   private final WingModelPart[] leftPrimaryParts;
   private final WingModelPart[] rightPrimaryParts;
   private final WingModelPart[] leftSecondaryParts;
   private final WingModelPart[] rightSecondaryParts;

   public RoseWingModel() {
      WingParts left = this.createWing(-1);
      WingParts right = this.createWing(1);
      this.leftPrimaryParts = left.primary();
      this.rightPrimaryParts = right.primary();
      this.leftSecondaryParts = left.secondary();
      this.rightSecondaryParts = right.secondary();
   }

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
      if (!(animator instanceof AvianWingAnimator avianAnimator)) {
         return;
      }

      for (int segmentIndex = 0; segmentIndex < this.leftPrimaryParts.length; segmentIndex++) {
         Vec3 primaryRotation = avianAnimator.getPrimaryRotationDegrees(segmentIndex, partialTick);
         Vec3 secondaryRotation = avianAnimator.getSecondaryRotationDegrees(segmentIndex, partialTick);
         applyMirroredRotationDegrees(this.leftPrimaryParts[segmentIndex], this.rightPrimaryParts[segmentIndex], primaryRotation);
         applyMirroredRotationDegrees(
            this.leftSecondaryParts[segmentIndex],
            this.rightSecondaryParts[segmentIndex],
            scaleRotation(secondaryRotation, SECONDARY_ROTATION_SCALE)
         );
      }

      matrices.pushPose();
      matrices.translate(0.0F, PIVOT_Y / MODEL_UNITS_PER_BLOCK, PIVOT_Z / MODEL_UNITS_PER_BLOCK);
      matrices.scale(WING_SCALE, WING_SCALE, WING_SCALE);
      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
      matrices.popPose();
   }

   private WingParts createWing(int side) {
      WingModelPart[] primaryParts = new WingModelPart[BONE_BOUNDARIES.length - 1];
      WingModelPart[] secondaryParts = new WingModelPart[BONE_BOUNDARIES.length - 1];
      SurfacePoint previousHinge = new SurfacePoint(0.0F, 0.0F, 0.0F);

      for (int boneIndex = 0; boneIndex < primaryParts.length; boneIndex++) {
         int firstSpan = BONE_BOUNDARIES[boneIndex];
         int endSpan = BONE_BOUNDARIES[boneIndex + 1];
         float hingeU = (float)firstSpan / SPAN_SEGMENTS;
         SurfacePoint hinge = boneIndex == 0 ? previousHinge : surfacePoint(side, hingeU, 0.5F);
         RoseSurfacePart primaryPart = new RoseSurfacePart(side, firstSpan, endSpan, hinge, 0.0F, SECONDARY_FEATHER_START);

         if (boneIndex == 0) {
            primaryPart.setPivot(0.0F, 0.0F, 0.0F);
            this.root.addChild(primaryPart);
         } else {
            primaryPart.setPivot(hinge.x() - previousHinge.x(), hinge.y() - previousHinge.y(), hinge.z() - previousHinge.z());
            primaryParts[boneIndex - 1].addChild(primaryPart);
         }

         SurfacePoint secondaryHinge = surfacePoint(side, hingeU, SECONDARY_FEATHER_START);
         RoseSurfacePart secondaryPart = new RoseSurfacePart(side, firstSpan, endSpan, secondaryHinge, SECONDARY_FEATHER_START, 1.0F);
         secondaryPart.setPivot(
            secondaryHinge.x() - hinge.x(),
            secondaryHinge.y() - hinge.y(),
            secondaryHinge.z() - hinge.z()
         );
         primaryPart.addChild(secondaryPart);

         primaryParts[boneIndex] = primaryPart;
         secondaryParts[boneIndex] = secondaryPart;
         previousHinge = hinge;
      }

      return new WingParts(primaryParts, secondaryParts);
   }

   private static SurfacePoint surfacePoint(int side, float u, float v) {
      float span = WING_TIP - SPINE_INSET;
      float arch = (float)Math.sin(PI * u);
      float featherScallop = (float)Math.sin(u * PI * SPAN_SEGMENTS) * FEATHER_SCALLOP * v;
      float x = side * (SPINE_INSET + span * u);
      float y = lerp(WING_TOP, WING_BOTTOM, v) + arch * (v - 0.5F) * SURFACE_ARCH + featherScallop;
      float z = -SURFACE_DEPTH * arch + SURFACE_BULGE * arch * (float)Math.sin(PI * v);
      return new SurfacePoint(x, y, z);
   }

   private static Vector3f surfaceNormal(int side, float u, float v) {
      float epsilon = 1.0E-3F;
      float u0 = Math.max(0.0F, u - epsilon);
      float u1 = Math.min(1.0F, u + epsilon);
      float v0 = Math.max(0.0F, v - epsilon);
      float v1 = Math.min(1.0F, v + epsilon);
      SurfacePoint alongSpanStart = surfacePoint(side, u0, v);
      SurfacePoint alongSpanEnd = surfacePoint(side, u1, v);
      SurfacePoint alongFeathersStart = surfacePoint(side, u, v0);
      SurfacePoint alongFeathersEnd = surfacePoint(side, u, v1);
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
      Vector3f normal = spanTangent.cross(featherTangent).normalize();
      if (normal.z > 0.0F) {
         normal.negate();
      }

      return normal;
   }

   private static Vec3 scaleRotation(Vec3 rotation, float scale) {
      return new Vec3(rotation.x * scale, rotation.y * scale, rotation.z * scale);
   }

   private static float lerp(float start, float end, float delta) {
      return start + (end - start) * delta;
   }

   private record SurfacePoint(float x, float y, float z) {
   }

   private record WingParts(WingModelPart[] primary, WingModelPart[] secondary) {
   }

   private static final class RoseSurfacePart extends WingModelPart {
      private final int side;
      private final SurfacePoint hinge;
      private final float featherStart;
      private final float featherEnd;

      private RoseSurfacePart(int side, int firstSpan, int endSpan, SurfacePoint hinge, float featherStart, float featherEnd) {
         super(TEXTURE_SIZE, TEXTURE_SIZE, 0, 0);
         this.side = side;
         this.hinge = hinge;
         this.featherStart = featherStart;
         this.featherEnd = featherEnd;
         this.addShell(firstSpan, endSpan);
      }

      private void addShell(int firstSpan, int endSpan) {
         for (int spanIndex = firstSpan; spanIndex < endSpan; spanIndex++) {
            float u0 = (float)spanIndex / SPAN_SEGMENTS;
            float u1 = (float)(spanIndex + 1) / SPAN_SEGMENTS;

            for (int featherIndex = 0; featherIndex < FEATHER_SEGMENTS; featherIndex++) {
               float v0 = lerp(this.featherStart, this.featherEnd, (float)featherIndex / FEATHER_SEGMENTS);
               float v1 = lerp(this.featherStart, this.featherEnd, (float)(featherIndex + 1) / FEATHER_SEGMENTS);
               SurfacePoint innerTop = surfacePoint(this.side, u0, v0);
               SurfacePoint outerTop = surfacePoint(this.side, u1, v0);
               SurfacePoint outerBottom = surfacePoint(this.side, u1, v1);
               SurfacePoint innerBottom = surfacePoint(this.side, u0, v1);
               float[][] front = this.side > 0
                  ? this.localPositions(outerTop, innerTop, innerBottom, outerBottom)
                  : this.localPositions(innerTop, outerTop, outerBottom, innerBottom);
               float textureU0 = (this.side > 0 ? u0 : u1) * TEXTURE_SIZE;
               float textureU1 = (this.side > 0 ? u1 : u0) * TEXTURE_SIZE;
               float textureV0 = v0 * TEXTURE_SIZE;
               float textureV1 = v1 * TEXTURE_SIZE;
               Vector3f normal = surfaceNormal(this.side, (u0 + u1) * 0.5F, (v0 + v1) * 0.5F);
               this.addTexturedFace(front, textureU0, textureV0, textureU1, textureV1, normal);

               float[][] back = new float[][]{front[3], front[2], front[1], front[0]};
               this.addTexturedFace(back, textureU0, textureV1, textureU1, textureV0, new Vector3f(normal).negate());
            }
         }
      }

      private float[][] localPositions(SurfacePoint first, SurfacePoint second, SurfacePoint third, SurfacePoint fourth) {
         return new float[][]{
            this.localPosition(first),
            this.localPosition(second),
            this.localPosition(third),
            this.localPosition(fourth)
         };
      }

      private float[] localPosition(SurfacePoint point) {
         return new float[]{point.x() - this.hinge.x(), point.y() - this.hinge.y(), point.z() - this.hinge.z()};
      }
   }
}
