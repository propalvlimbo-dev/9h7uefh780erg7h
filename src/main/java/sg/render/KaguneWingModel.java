package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sg.ec.WingModel;

public final class KaguneWingModel extends WingModel {
   private static final int CROSS_SECTION_SEGMENTS = 18;
   private static final float[][] PRIMARY_CENTERLINE = new float[][]{
      {KaguneWingModel.PRIMARY_POINT_0_X, KaguneWingModel.PRIMARY_POINT_0_Y, KaguneWingModel.PRIMARY_POINT_0_Z},
      {KaguneWingModel.PRIMARY_POINT_1_X, KaguneWingModel.PRIMARY_POINT_1_Y, KaguneWingModel.PRIMARY_POINT_1_Z},
      {KaguneWingModel.PRIMARY_POINT_2_X, KaguneWingModel.PRIMARY_POINT_2_Y, KaguneWingModel.PRIMARY_POINT_2_Z},
      {KaguneWingModel.PRIMARY_POINT_3_X, KaguneWingModel.PRIMARY_POINT_3_Y, KaguneWingModel.PRIMARY_POINT_3_Z},
      {KaguneWingModel.PRIMARY_POINT_4_X, KaguneWingModel.PRIMARY_POINT_4_Y, KaguneWingModel.PRIMARY_POINT_4_Z},
      {KaguneWingModel.PRIMARY_POINT_5_X, KaguneWingModel.PRIMARY_POINT_5_Y, KaguneWingModel.PRIMARY_POINT_5_Z},
      {KaguneWingModel.PRIMARY_POINT_6_X, KaguneWingModel.PRIMARY_POINT_6_Y, KaguneWingModel.PRIMARY_POINT_6_Z},
      {KaguneWingModel.PRIMARY_POINT_7_X, KaguneWingModel.PRIMARY_POINT_7_Y, KaguneWingModel.PRIMARY_POINT_7_Z},
      {KaguneWingModel.PRIMARY_POINT_8_X, KaguneWingModel.PRIMARY_POINT_8_Y, KaguneWingModel.PRIMARY_POINT_8_Z},
      {KaguneWingModel.PRIMARY_POINT_9_X, KaguneWingModel.PRIMARY_POINT_9_Y, KaguneWingModel.PRIMARY_POINT_9_Z},
      {KaguneWingModel.PRIMARY_POINT_10_X, KaguneWingModel.PRIMARY_POINT_10_Y, KaguneWingModel.PRIMARY_POINT_10_Z},
      {KaguneWingModel.PRIMARY_POINT_11_X, KaguneWingModel.PRIMARY_POINT_11_Y, KaguneWingModel.PRIMARY_POINT_11_Z}
   };
   private static final float[][] MIRRORED_PRIMARY_CENTERLINE = mirrorX(PRIMARY_CENTERLINE);
   private static final float[][] SECONDARY_CENTERLINE = new float[][]{
      {KaguneWingModel.SECONDARY_POINT_0_X, KaguneWingModel.SECONDARY_POINT_0_Y, KaguneWingModel.SECONDARY_POINT_0_Z},
      {KaguneWingModel.SECONDARY_POINT_1_X, KaguneWingModel.SECONDARY_POINT_1_Y, KaguneWingModel.SECONDARY_POINT_1_Z},
      {KaguneWingModel.SECONDARY_POINT_2_X, KaguneWingModel.SECONDARY_POINT_2_Y, KaguneWingModel.SECONDARY_POINT_2_Z},
      {KaguneWingModel.SECONDARY_POINT_3_X, KaguneWingModel.SECONDARY_POINT_3_Y, KaguneWingModel.SECONDARY_POINT_3_Z},
      {KaguneWingModel.SECONDARY_POINT_4_X, KaguneWingModel.SECONDARY_POINT_4_Y, KaguneWingModel.SECONDARY_POINT_4_Z},
      {KaguneWingModel.SECONDARY_POINT_5_X, KaguneWingModel.SECONDARY_POINT_5_Y, KaguneWingModel.SECONDARY_POINT_5_Z},
      {KaguneWingModel.SECONDARY_POINT_6_X, KaguneWingModel.SECONDARY_POINT_6_Y, KaguneWingModel.SECONDARY_POINT_6_Z},
      {KaguneWingModel.SECONDARY_POINT_7_X, KaguneWingModel.SECONDARY_POINT_7_Y, KaguneWingModel.SECONDARY_POINT_7_Z},
      {KaguneWingModel.SECONDARY_POINT_8_X, KaguneWingModel.SECONDARY_POINT_8_Y, KaguneWingModel.SECONDARY_POINT_8_Z},
      {KaguneWingModel.SECONDARY_POINT_9_X, KaguneWingModel.SECONDARY_POINT_9_Y, KaguneWingModel.SECONDARY_POINT_9_Z},
      {KaguneWingModel.SECONDARY_POINT_10_X, KaguneWingModel.SECONDARY_POINT_10_Y, KaguneWingModel.SECONDARY_POINT_10_Z},
      {KaguneWingModel.SECONDARY_POINT_11_X, KaguneWingModel.SECONDARY_POINT_11_Y, KaguneWingModel.SECONDARY_POINT_11_Z}
   };
   private static final float[][] MIRRORED_SECONDARY_CENTERLINE = mirrorX(SECONDARY_CENTERLINE);
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MIRRORED_PRIMARY_PHASE_OFFSET = 1.6F;
   private static final float SECONDARY_PHASE_OFFSET = 3.2F;
   private static final float MIRRORED_SECONDARY_PHASE_OFFSET = 4.8F;
   private static final float ROOT_Y = 0.215F;
   private static final float ROOT_Z = -0.025F;
   private static final float POINT_PHASE_OFFSET = 0.38F;
   private static final float Y_WAVE_AMPLITUDE = 0.018F;
   private static final float Z_WAVE_AMPLITUDE = 0.035F;
   private static final float ANGLE_STEP_RADIANS = 0.34906587F;
   private static final float NEXT_ANGLE_STEP_RADIANS = 0.34906587F;
   private static final float START_RING_CURRENT_V_DIVISOR = 18.0F;
   private static final float START_RING_NEXT_V_DIVISOR = 18.0F;
   private static final float END_RING_NEXT_V_DIVISOR = 18.0F;
   private static final float END_RING_CURRENT_V_DIVISOR = 18.0F;
   private static final float REFERENCE_AXIS_Y_THRESHOLD = 0.92F;
   private static final float TIP_RADIUS = 0.018F;
   private static final float RADIUS_TAPER_AMPLITUDE = 0.092F;
   private static final float PRIMARY_POINT_0_X = 0.08438F;
   private static final float PRIMARY_POINT_0_Y = 0.1F;
   private static final float PRIMARY_POINT_0_Z = 0.0125F;
   private static final float PRIMARY_POINT_1_X = 0.14911F;
   private static final float PRIMARY_POINT_1_Y = 0.07842F;
   private static final float PRIMARY_POINT_1_Z = 0.06928F;
   private static final float PRIMARY_POINT_2_X = 0.22179F;
   private static final float PRIMARY_POINT_2_Y = 0.04038F;
   private static final float PRIMARY_POINT_2_Z = 0.11723F;
   private static final float PRIMARY_POINT_3_X = 0.30059F;
   private static final float PRIMARY_POINT_3_Y = -0.01158F;
   private static final float PRIMARY_POINT_3_Z = 0.15561F;
   private static final float PRIMARY_POINT_4_X = 0.38368F;
   private static final float PRIMARY_POINT_4_Y = -0.07488F;
   private static final float PRIMARY_POINT_4_Z = 0.18368F;
   private static final float PRIMARY_POINT_5_X = 0.46925F;
   private static final float PRIMARY_POINT_5_Y = -0.14701F;
   private static final float PRIMARY_POINT_5_Z = 0.20068F;
   private static final float PRIMARY_POINT_6_X = 0.55547F;
   private static final float PRIMARY_POINT_6_Y = -0.22539F;
   private static final float PRIMARY_POINT_6_Z = 0.20586F;
   private static final float PRIMARY_POINT_7_X = 0.64052F;
   private static final float PRIMARY_POINT_7_Y = -0.30749F;
   private static final float PRIMARY_POINT_7_Z = 0.19848F;
   private static final float PRIMARY_POINT_8_X = 0.72257F;
   private static final float PRIMARY_POINT_8_Y = -0.39074F;
   private static final float PRIMARY_POINT_8_Z = 0.17778F;
   private static final float PRIMARY_POINT_9_X = 0.79981F;
   private static final float PRIMARY_POINT_9_Y = -0.47261F;
   private static final float PRIMARY_POINT_9_Z = 0.14302F;
   private static final float PRIMARY_POINT_10_X = 0.8704F;
   private static final float PRIMARY_POINT_10_Y = -0.55054F;
   private static final float PRIMARY_POINT_10_Z = 0.09344F;
   private static final float PRIMARY_POINT_11_X = 0.93253F;
   private static final float PRIMARY_POINT_11_Y = -0.62198F;
   private static final float PRIMARY_POINT_11_Z = 0.02831F;
   private static final float SECONDARY_POINT_0_X = 0.07813F;
   private static final float SECONDARY_POINT_0_Y = 0.31563F;
   private static final float SECONDARY_POINT_0_Z = 0.0125F;
   private static final float SECONDARY_POINT_1_X = 0.14968F;
   private static final float SECONDARY_POINT_1_Y = 0.32694F;
   private static final float SECONDARY_POINT_1_Z = 0.07296F;
   private static final float SECONDARY_POINT_2_X = 0.22736F;
   private static final float SECONDARY_POINT_2_Y = 0.35176F;
   private static final float SECONDARY_POINT_2_Z = 0.12276F;
   private static final float SECONDARY_POINT_3_X = 0.30962F;
   private static final float SECONDARY_POINT_3_Y = 0.38809F;
   private static final float SECONDARY_POINT_3_Z = 0.16138F;
   private static final float SECONDARY_POINT_4_X = 0.39491F;
   private static final float SECONDARY_POINT_4_Y = 0.43391F;
   private static final float SECONDARY_POINT_4_Z = 0.18831F;
   private static final float SECONDARY_POINT_5_X = 0.48167F;
   private static final float SECONDARY_POINT_5_Y = 0.48724F;
   private static final float SECONDARY_POINT_5_Z = 0.20305F;
   private static final float SECONDARY_POINT_6_X = 0.56836F;
   private static final float SECONDARY_POINT_6_Y = 0.54609F;
   private static final float SECONDARY_POINT_6_Z = 0.20508F;
   private static final float SECONDARY_POINT_7_X = 0.65342F;
   private static final float SECONDARY_POINT_7_Y = 0.60846F;
   private static final float SECONDARY_POINT_7_Z = 0.19389F;
   private static final float SECONDARY_POINT_8_X = 0.7353F;
   private static final float SECONDARY_POINT_8_Y = 0.67234F;
   private static final float SECONDARY_POINT_8_Z = 0.16898F;
   private static final float SECONDARY_POINT_9_X = 0.81245F;
   private static final float SECONDARY_POINT_9_Y = 0.73574F;
   private static final float SECONDARY_POINT_9_Z = 0.12983F;
   private static final float SECONDARY_POINT_10_X = 0.88332F;
   private static final float SECONDARY_POINT_10_Y = 0.79668F;
   private static final float SECONDARY_POINT_10_Z = 0.07594F;
   private static final float SECONDARY_POINT_11_X = 0.94635F;
   private static final float SECONDARY_POINT_11_Y = 0.85313F;
   private static final float SECONDARY_POINT_11_Z = 0.00679F;

   private static float[][] mirrorX(float[][] points) {
      float[][] mirroredPoints = new float[points.length][3];

      for (int pointIndex = 0; pointIndex < points.length; pointIndex++) {
         mirroredPoints[pointIndex][0] = -points[pointIndex][0];
         mirroredPoints[pointIndex][1] = points[pointIndex][1];
         mirroredPoints[pointIndex][2] = points[pointIndex][2];
      }

      return mirroredPoints;
   }

   private KaguneWingModel.SectionBasis computeSectionBasis(KaguneWingModel.RibbonPoint[] points, int pointIndex) {
      KaguneWingModel.RibbonPoint previousPoint = points[Math.max(0, pointIndex - 1)];
      KaguneWingModel.RibbonPoint nextPoint = points[Math.min(points.length - 1, pointIndex + 1)];
      Vector3f tangent = new Vector3f(nextPoint.x - previousPoint.x, nextPoint.y - previousPoint.y, nextPoint.z - previousPoint.z).normalize();
      Vector3f referenceAxis = Math.abs(tangent.y) > REFERENCE_AXIS_Y_THRESHOLD ? new Vector3f(1.0F, 0.0F, 0.0F) : new Vector3f(0.0F, 1.0F, 0.0F);
      Vector3f side = new Vector3f(tangent).cross(referenceAxis).normalize();
      Vector3f up = new Vector3f(side).cross(tangent).normalize();
      return new KaguneWingModel.SectionBasis(side, up);
   }

   private void emitRibbonVertex(
      Pose pose,
      VertexConsumer vertices,
      KaguneWingModel.RibbonPoint point,
      KaguneWingModel.SectionBasis basis,
      float radius,
      float angleRadians,
      float u,
      float v,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      float cosine = (float)Math.cos(angleRadians);
      float sine = (float)Math.sin(angleRadians);
      Vector3f radialNormal = new Vector3f(basis.side).mul(cosine).add(new Vector3f(basis.up).mul(sine)).normalize();
      Vector3f position = new Vector3f(radialNormal).mul(radius).add(point.x, point.y, point.z);
      Matrix4f positionMatrix = pose.pose();
      Matrix3f normalMatrix = pose.normal();
      Vector3f transformedPosition = positionMatrix.transformPosition(position);
      Vector3f transformedNormal = normalMatrix.transform(radialNormal).normalize();
      vertices.addVertex(transformedPosition.x, transformedPosition.y, transformedPosition.z)
         .setColor(red, green, blue, alpha)
         .setUv(u * 2.0F, v)
         .setOverlay(packedOverlay)
         .setLight(packedLight)
         .setNormal(transformedNormal.x, transformedNormal.y, transformedNormal.z);
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      float timeSeconds = (float)System.nanoTime() / NANOS_PER_SECOND;
      this.renderRibbon(matrices.last(), vertices, PRIMARY_CENTERLINE, timeSeconds, 0.0F, packedLight, packedOverlay, red, green, blue, alpha);
      this.renderRibbon(matrices.last(), vertices, MIRRORED_PRIMARY_CENTERLINE, timeSeconds, MIRRORED_PRIMARY_PHASE_OFFSET, packedLight, packedOverlay, red, green, blue, alpha);
      this.renderRibbon(matrices.last(), vertices, SECONDARY_CENTERLINE, timeSeconds, SECONDARY_PHASE_OFFSET, packedLight, packedOverlay, red, green, blue, alpha);
      this.renderRibbon(matrices.last(), vertices, MIRRORED_SECONDARY_CENTERLINE, timeSeconds, MIRRORED_SECONDARY_PHASE_OFFSET, packedLight, packedOverlay, red, green, blue, alpha);
   }

   private void renderRibbon(Pose pose, VertexConsumer vertices, float[][] centerline, float timeSeconds, float phaseOffset, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      KaguneWingModel.RibbonPoint[] animatedPoints = new KaguneWingModel.RibbonPoint[centerline.length + 1];
      animatedPoints[0] = new KaguneWingModel.RibbonPoint(0.0F, ROOT_Y, ROOT_Z);

      for (int pointIndex = 0; pointIndex < centerline.length; pointIndex++) {
         float pointFraction = (pointIndex + 1.0F) / centerline.length;
         float wave = (float)Math.sin(timeSeconds * 2.0F + phaseOffset + pointIndex * POINT_PHASE_OFFSET) * pointFraction;
         animatedPoints[pointIndex + 1] = new KaguneWingModel.RibbonPoint(centerline[pointIndex][0], centerline[pointIndex][1] + wave * Y_WAVE_AMPLITUDE, centerline[pointIndex][2] + wave * Z_WAVE_AMPLITUDE);
      }

      for (int segmentIndex = 0; segmentIndex < animatedPoints.length - 1; segmentIndex++) {
         KaguneWingModel.RibbonPoint startPoint = animatedPoints[segmentIndex];
         KaguneWingModel.RibbonPoint endPoint = animatedPoints[segmentIndex + 1];
         KaguneWingModel.SectionBasis startBasis = this.computeSectionBasis(animatedPoints, segmentIndex);
         KaguneWingModel.SectionBasis endBasis = this.computeSectionBasis(animatedPoints, segmentIndex + 1);
         float startFraction = (float)segmentIndex / (animatedPoints.length - 1);
         float endFraction = (segmentIndex + 1.0F) / (animatedPoints.length - 1);
         float startRadius = this.getRadius(startFraction);
         float endRadius = this.getRadius(endFraction);

         for (int sideIndex = 0; sideIndex < 18; sideIndex++) {
            float angleRadians = sideIndex * ANGLE_STEP_RADIANS;
            float nextAngleRadians = (sideIndex + 1) * NEXT_ANGLE_STEP_RADIANS;
            this.emitRibbonVertex(pose, vertices, startPoint, startBasis, startRadius, angleRadians, startFraction, sideIndex / START_RING_CURRENT_V_DIVISOR, packedLight, packedOverlay, red, green, blue, alpha);
            this.emitRibbonVertex(pose, vertices, startPoint, startBasis, startRadius, nextAngleRadians, startFraction, (sideIndex + 1.0F) / START_RING_NEXT_V_DIVISOR, packedLight, packedOverlay, red, green, blue, alpha);
            this.emitRibbonVertex(pose, vertices, endPoint, endBasis, endRadius, nextAngleRadians, endFraction, (sideIndex + 1.0F) / END_RING_NEXT_V_DIVISOR, packedLight, packedOverlay, red, green, blue, alpha);
            this.emitRibbonVertex(pose, vertices, endPoint, endBasis, endRadius, angleRadians, endFraction, sideIndex / END_RING_CURRENT_V_DIVISOR, packedLight, packedOverlay, red, green, blue, alpha);
         }
      }
   }

   private float getRadius(float fraction) {
      float taper = 1.0F - fraction * fraction;
      return TIP_RADIUS + RADIUS_TAPER_AMPLITUDE * taper;
   }

   private record SectionBasis(Vector3f side, Vector3f up) {
   }

   private record RibbonPoint(float x, float y, float z) {
   }
}
