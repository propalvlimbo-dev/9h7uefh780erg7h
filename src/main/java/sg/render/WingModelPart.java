package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WingModelPart {
   public @com.google.gson.annotations.SerializedName("А必")  float pivotX;
   public @com.google.gson.annotations.SerializedName("Ащ")  float pivotY;
   public @com.google.gson.annotations.SerializedName("АГ")  float pivotZ;
   public @com.google.gson.annotations.SerializedName("Ал")  float xRotationRadians;
   public @com.google.gson.annotations.SerializedName("Аэ")  float yRotationRadians;
   public @com.google.gson.annotations.SerializedName("Аю")  float zRotationRadians;
   public @com.google.gson.annotations.SerializedName("ЯЬ")  boolean visible = true;
   protected final @com.google.gson.annotations.SerializedName("сЧ")  List<WingModelPart> children = new ArrayList<>();
   protected final @com.google.gson.annotations.SerializedName("сЛ")  List<WingModelPart.TexturedFace> faces = new ArrayList<>();
   protected final @com.google.gson.annotations.SerializedName("ею")  int textureWidth;
   protected final @com.google.gson.annotations.SerializedName("еЩ")  int textureHeight;
   protected final @com.google.gson.annotations.SerializedName("еу")  int textureU;
   protected final @com.google.gson.annotations.SerializedName("е2")  int textureV;
   private static final float NEGATIVE_Y_NORMAL_COMPONENT = -1.0F;
   private static final float NEGATIVE_X_NORMAL_COMPONENT = -1.0F;
   private static final float NEGATIVE_Z_NORMAL_COMPONENT = -1.0F;
   private static final float X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float Z_MODEL_UNITS_PER_BLOCK = 16.0F;

   public void applyTransform(PoseStack matrices) {
      matrices.translate(this.pivotX / X_MODEL_UNITS_PER_BLOCK, this.pivotY / Y_MODEL_UNITS_PER_BLOCK, this.pivotZ / Z_MODEL_UNITS_PER_BLOCK);
      if (this.zRotationRadians != 0.0F) {
         matrices.mulPose(Axis.ZP.rotation(this.zRotationRadians));
      }

      if (this.yRotationRadians != 0.0F) {
         matrices.mulPose(Axis.YP.rotation(this.yRotationRadians));
      }

      if (this.xRotationRadians != 0.0F) {
         matrices.mulPose(Axis.XP.rotation(this.xRotationRadians));
      }
   }

   public void render(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      if (this.visible) {
         matrices.pushPose();
         this.applyTransform(matrices);
         Pose pose = matrices.last();

         for (WingModelPart.TexturedFace face : this.faces) {
            face.emitVertices(pose, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         for (WingModelPart child : this.children) {
            child.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
         }

         matrices.popPose();
      }
   }

   public void addCuboid(float x, float y, float z, int width, int height, int depth) {
      float maxX = x + width;
      float maxY = y + height;
      float maxZ = z + depth;
      float[] corner000 = new float[]{x, y, z};
      float[] corner100 = new float[]{maxX, y, z};
      float[] corner110 = new float[]{maxX, maxY, z};
      float[] corner010 = new float[]{x, maxY, z};
      float[] corner001 = new float[]{x, y, maxZ};
      float[] corner101 = new float[]{maxX, y, maxZ};
      float[] corner111 = new float[]{maxX, maxY, maxZ};
      float[] corner011 = new float[]{x, maxY, maxZ};
      float textureU = this.textureU;
      float textureV = this.textureV;
      float uWestStart = textureU;
      float uFrontStart = textureU + depth;
      float uEastStart = textureU + depth + width;
      float uTopEnd = textureU + depth + width + width;
      float uBackStart = textureU + depth + width + depth;
      float uBackEnd = textureU + depth + width + depth + width;
      float vTopStart = textureV;
      float vSideStart = textureV + depth;
      float vSideEnd = textureV + depth + height;
      this.addTexturedFace(new float[][]{corner101, corner001, corner000, corner100}, uFrontStart, vTopStart, uEastStart, vSideStart, new Vector3f(0.0F, NEGATIVE_Y_NORMAL_COMPONENT, 0.0F));
      this.addTexturedFace(new float[][]{corner110, corner010, corner011, corner111}, uEastStart, vSideStart, uTopEnd, vTopStart, new Vector3f(0.0F, 1.0F, 0.0F));
      this.addTexturedFace(new float[][]{corner000, corner001, corner011, corner010}, uWestStart, vSideStart, uFrontStart, vSideEnd, new Vector3f(NEGATIVE_X_NORMAL_COMPONENT, 0.0F, 0.0F));
      this.addTexturedFace(new float[][]{corner100, corner000, corner010, corner110}, uFrontStart, vSideStart, uEastStart, vSideEnd, new Vector3f(0.0F, 0.0F, NEGATIVE_Z_NORMAL_COMPONENT));
      this.addTexturedFace(new float[][]{corner101, corner100, corner110, corner111}, uEastStart, vSideStart, uBackStart, vSideEnd, new Vector3f(1.0F, 0.0F, 0.0F));
      this.addTexturedFace(new float[][]{corner001, corner101, corner111, corner011}, uBackStart, vSideStart, uBackEnd, vSideEnd, new Vector3f(0.0F, 0.0F, 1.0F));
   }

   /**
    * Плоскость (двусторонняя) с произвольной UV-развёрткой — для HD-арта крыльев,
    * который кладётся на один плоский прямоугольник, как в Lunar Client.
    */
   public void addPlane(float x0, float y0, float x1, float y1, float z, float u0, float v0, float u1, float v1) {
      float textureUStart = u0 * this.textureWidth;
      float textureUEnd = u1 * this.textureWidth;
      float textureVStart = v0 * this.textureHeight;
      float textureVEnd = v1 * this.textureHeight;
      this.addTexturedFace(
         new float[][]{{x1, y0, z}, {x0, y0, z}, {x0, y1, z}, {x1, y1, z}},
         textureUStart,
         textureVStart,
         textureUEnd,
         textureVEnd,
         new Vector3f(0.0F, 0.0F, NEGATIVE_Z_NORMAL_COMPONENT)
      );
      this.addTexturedFace(
         new float[][]{{x0, y0, z}, {x1, y0, z}, {x1, y1, z}, {x0, y1, z}},
         textureUStart,
         textureVStart,
         textureUEnd,
         textureVEnd,
         new Vector3f(0.0F, 0.0F, 1.0F)
      );
   }

   public void addChild(WingModelPart child) {
      this.children.add(child);
   }

   public WingModelPart(int textureWidth, int textureHeight, int textureU, int textureV) {
      this.textureWidth = textureWidth;
      this.textureHeight = textureHeight;
      this.textureU = textureU;
      this.textureV = textureV;
   }

   public void setPivot(float x, float y, float z) {
      this.pivotX = x;
      this.pivotY = y;
      this.pivotZ = z;
   }

   protected void addTexturedFace(float[][] positions, float u0, float v0, float u1, float v1, Vector3f normal) {
      WingModelPart.TexturedVertex[] vertices = new WingModelPart.TexturedVertex[]{
         new WingModelPart.TexturedVertex(positions[0][0], positions[0][1], positions[0][2], u1 / this.textureWidth, v0 / this.textureHeight),
         new WingModelPart.TexturedVertex(positions[1][0], positions[1][1], positions[1][2], u0 / this.textureWidth, v0 / this.textureHeight),
         new WingModelPart.TexturedVertex(positions[2][0], positions[2][1], positions[2][2], u0 / this.textureWidth, v1 / this.textureHeight),
         new WingModelPart.TexturedVertex(positions[3][0], positions[3][1], positions[3][2], u1 / this.textureWidth, v1 / this.textureHeight)
      };
      this.faces.add(new WingModelPart.TexturedFace(vertices, normal));
   }

   protected static final class TexturedFace {
      @SerializedName("В")
      final WingModelPart.TexturedVertex[] vertices;
      @SerializedName("В")
      final Vector3f normal;
      private static final float X_MODEL_UNITS_PER_BLOCK = 16.0F;
      private static final float Y_MODEL_UNITS_PER_BLOCK = 16.0F;
      private static final float Z_MODEL_UNITS_PER_BLOCK = 16.0F;

      TexturedFace(WingModelPart.TexturedVertex[] vertices, Vector3f normal) {
         this.vertices = vertices;
         this.normal = normal;
      }

      void emitVertices(Pose pose, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
         Matrix4f positionMatrix = pose.pose();
         Matrix3f normalMatrix = pose.normal();
         Vector3f transformedNormal = normalMatrix.transform(new Vector3f(this.normal.x, this.normal.y, this.normal.z));

         for (WingModelPart.TexturedVertex vertex : this.vertices) {
            Vector3f transformedPosition = positionMatrix.transformPosition(new Vector3f(vertex.x() / X_MODEL_UNITS_PER_BLOCK, vertex.y() / Y_MODEL_UNITS_PER_BLOCK, vertex.z() / Z_MODEL_UNITS_PER_BLOCK));
            vertices.addVertex(transformedPosition.x, transformedPosition.y, transformedPosition.z)
               .setColor(red, green, blue, alpha)
               .setUv(vertex.u(), vertex.v())
               .setOverlay(packedOverlay)
               .setLight(packedLight)
               .setNormal(transformedNormal.x, transformedNormal.y, transformedNormal.z);
         }
      }
   }

   protected record TexturedVertex(float x, float y, float z, float u, float v) {
   }
}
