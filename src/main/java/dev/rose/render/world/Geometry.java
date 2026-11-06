package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Vector3f;
import org.joml.Vector3fc;


public final class Geometry {
   private Geometry() {
   }

   public static void quad(
      VertexConsumer consumer, PoseStack.Pose pose, Vector3f a, Vector3f b, Vector3f c, Vector3f d, int color, int light, float u0, float v0, float u1, float v1
   ) {
      Vector3f normal = new Vector3f(b).sub(a).cross(new Vector3f(d).sub(a));
      if (normal.lengthSquared() < 1.0E-12F) {
         normal.set(new Vector3f(c).sub(a).cross(new Vector3f(d).sub(b)));
      }

      if (normal.lengthSquared() < 1.0E-12F) {
         normal.set(0.0F, 1.0F, 0.0F);
      }

      normal.normalize();
      vertex(consumer, pose, a, color, u0, v0, light, normal);
      vertex(consumer, pose, b, color, u1, v0, light, normal);
      vertex(consumer, pose, c, color, u1, v1, light, normal);
      vertex(consumer, pose, d, color, u0, v1, light, normal);
   }

   public static void quad(VertexConsumer consumer, PoseStack.Pose pose, Vector3f a, Vector3f b, Vector3f c, Vector3f d, int color, int light) {
      quad(consumer, pose, a, b, c, d, color, light, 0.0F, 0.0F, 1.0F, 1.0F);
   }

   public static void quadUv(
      VertexConsumer consumer,
      PoseStack.Pose pose,
      Vector3f a,
      Vector3f b,
      Vector3f c,
      Vector3f d,
      float ua,
      float va,
      float ub,
      float vb,
      float uc,
      float vc,
      float ud,
      float vd,
      int colorA,
      int colorB,
      int colorC,
      int colorD,
      int light
   ) {
      Vector3f normal = new Vector3f(b).sub(a).cross(new Vector3f(d).sub(a));
      if (normal.lengthSquared() < 1.0E-12F) {
         normal.set(new Vector3f(c).sub(a).cross(new Vector3f(d).sub(b)));
      }

      if (normal.lengthSquared() < 1.0E-12F) {
         normal.set(0.0F, 1.0F, 0.0F);
      }

      normal.normalize();
      vertex(consumer, pose, a, colorA, ua, va, light, normal);
      vertex(consumer, pose, b, colorB, ub, vb, light, normal);
      vertex(consumer, pose, c, colorC, uc, vc, light, normal);
      vertex(consumer, pose, d, colorD, ud, vd, light, normal);
   }

   public static void triangle(VertexConsumer consumer, PoseStack.Pose pose, Vector3f a, Vector3f b, Vector3f c, int color, int light) {
      quad(consumer, pose, a, b, c, c, color, light);
   }

   private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Vector3f p, int color, float u, float v, int light, Vector3f normal) {
      consumer.addVertex(pose, p.x, p.y, p.z)
         .setColor(color)
         .setUv(u, v)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(light)
         .setNormal(pose, normal.x, normal.y, normal.z);
   }

   public static void glowQuad(
      VertexConsumer consumer, PoseStack.Pose pose, Vector3f a, Vector3f b, Vector3f c, Vector3f d, int colorA, int colorB, int colorC, int colorD
   ) {
      consumer.addVertex(pose, a.x, a.y, a.z).setColor(colorA);
      consumer.addVertex(pose, b.x, b.y, b.z).setColor(colorB);
      consumer.addVertex(pose, c.x, c.y, c.z).setColor(colorC);
      consumer.addVertex(pose, d.x, d.y, d.z).setColor(colorD);
   }

   public static void glowRing(VertexConsumer consumer, PoseStack.Pose pose, float y, float inner, float outer, int segments, int innerColor, int outerColor) {
      for (int i = 0; i < segments; i++) {
         double a0 = (Math.PI * 2) * i / segments;
         double a1 = (Math.PI * 2) * (i + 1) / segments;
         float c0 = (float)Math.cos(a0);
         float s0 = (float)Math.sin(a0);
         float c1 = (float)Math.cos(a1);
         float s1 = (float)Math.sin(a1);
         glowQuad(
            consumer,
            pose,
            new Vector3f(c0 * inner, y, s0 * inner),
            new Vector3f(c0 * outer, y, s0 * outer),
            new Vector3f(c1 * outer, y, s1 * outer),
            new Vector3f(c1 * inner, y, s1 * inner),
            innerColor,
            outerColor,
            outerColor,
            innerColor
         );
      }
   }

   public static void beam(VertexConsumer consumer, PoseStack.Pose pose, Vector3f view, Vector3f a, Vector3f b, float widthA, float widthB, int colorA, int colorB) {
      Vector3f direction = new Vector3f(b).sub(a);
      Vector3f toCamera = new Vector3f(a).add(view);
      Vector3f side = direction.cross(toCamera, new Vector3f());
      if (!(side.lengthSquared() < 1.0E-10F)) {
         side.normalize();
         Vector3f sideA = new Vector3f(side).mul(widthA);
         Vector3f sideB = new Vector3f(side).mul(widthB);
         int edgeA = colorA & 16777215;
         int edgeB = colorB & 16777215;
         glowQuad(consumer, pose, new Vector3f(a).sub(sideA), new Vector3f(a), new Vector3f(b), new Vector3f(b).sub(sideB), edgeA, colorA, colorB, edgeB);
         glowQuad(consumer, pose, new Vector3f(a), new Vector3f(a).add(sideA), new Vector3f(b).add(sideB), new Vector3f(b), colorA, edgeA, edgeB, colorB);
      }
   }

   public static void ribbon(VertexConsumer consumer, PoseStack.Pose pose, Vector3f view, List<Vector3f> points, float[] widths, int[] colors) {
      int count = points.size();
      if (count >= 2) {
         Vector3f[] sides = new Vector3f[count];
         Vector3f previous = null;

         for (int i = 0; i < count; i++) {
            Vector3f before = points.get(Math.max(0, i - 1));
            Vector3f after = points.get(Math.min(count - 1, i + 1));
            Vector3f direction = new Vector3f(after).sub(before);
            Vector3f toCamera = new Vector3f((Vector3fc)points.get(i)).add(view);
            Vector3f side = direction.cross(toCamera, new Vector3f());
            if (side.lengthSquared() < 1.0E-10F) {
               side = previous == null ? new Vector3f(1.0F, 0.0F, 0.0F) : new Vector3f(previous);
            } else {
               side.normalize();
            }

            if (previous != null && side.dot(previous) < 0.0F) {
               side.negate();
            }

            sides[i] = side;
            previous = side;
         }

         for (int i = 0; i < count - 1; i++) {
            Vector3f a = points.get(i);
            Vector3f b = points.get(i + 1);
            Vector3f sideA = new Vector3f(sides[i]).mul(widths[i]);
            Vector3f sideB = new Vector3f(sides[i + 1]).mul(widths[i + 1]);
            int colorA = colors[i];
            int colorB = colors[i + 1];
            int edgeA = colorA & 16777215;
            int edgeB = colorB & 16777215;
            glowQuad(consumer, pose, new Vector3f(a).sub(sideA), new Vector3f(a), new Vector3f(b), new Vector3f(b).sub(sideB), edgeA, colorA, colorB, edgeB);
            glowQuad(consumer, pose, new Vector3f(a), new Vector3f(a).add(sideA), new Vector3f(b).add(sideB), new Vector3f(b), colorA, edgeA, edgeB, colorB);
         }
      }
   }

   public static void dotSprite(VertexConsumer consumer, PoseStack.Pose pose, Vector3f center, Vector3f right, Vector3f up, float size, int color) {
      sprite(consumer, pose, center, right, up, size, size, 0.0F, color);
   }

   public static void sprite(
      VertexConsumer consumer, PoseStack.Pose pose, Vector3f center, Vector3f right, Vector3f up, float halfW, float halfH, float angle, int color
   ) {
      float cos = (float)Math.cos(angle);
      float sin = (float)Math.sin(angle);
      Vector3f r = new Vector3f(right).mul(cos * halfW).add(new Vector3f(up).mul(sin * halfW));
      Vector3f u = new Vector3f(up).mul(cos * halfH).sub(new Vector3f(right).mul(sin * halfH));
      Vector3f a = new Vector3f(center).sub(r).sub(u);
      Vector3f b = new Vector3f(center).add(r).sub(u);
      Vector3f c = new Vector3f(center).add(r).add(u);
      Vector3f d = new Vector3f(center).sub(r).add(u);
      consumer.addVertex(pose, a.x, a.y, a.z).setUv(0.0F, 1.0F).setColor(color);
      consumer.addVertex(pose, b.x, b.y, b.z).setUv(1.0F, 1.0F).setColor(color);
      consumer.addVertex(pose, c.x, c.y, c.z).setUv(1.0F, 0.0F).setColor(color);
      consumer.addVertex(pose, d.x, d.y, d.z).setUv(0.0F, 0.0F).setColor(color);
   }

   public static void flatSprite(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float half, float angle, int color) {
      float cos = (float)Math.cos(angle) * half;
      float sin = (float)Math.sin(angle) * half;
      consumer.addVertex(pose, x - cos + sin, y, z - sin - cos).setUv(0.0F, 0.0F).setColor(color);
      consumer.addVertex(pose, x + cos + sin, y, z + sin - cos).setUv(1.0F, 0.0F).setColor(color);
      consumer.addVertex(pose, x + cos - sin, y, z + sin + cos).setUv(1.0F, 1.0F).setColor(color);
      consumer.addVertex(pose, x - cos - sin, y, z - sin + cos).setUv(0.0F, 1.0F).setColor(color);
   }

   public static void texQuad(
      VertexConsumer consumer,
      PoseStack.Pose pose,
      Vector3f a,
      Vector3f b,
      Vector3f c,
      Vector3f d,
      float u0,
      float v0,
      float u1,
      float v1,
      int colorA,
      int colorB,
      int colorC,
      int colorD
   ) {
      consumer.addVertex(pose, a.x, a.y, a.z).setUv(u0, v0).setColor(colorA);
      consumer.addVertex(pose, b.x, b.y, b.z).setUv(u1, v0).setColor(colorB);
      consumer.addVertex(pose, c.x, c.y, c.z).setUv(u1, v1).setColor(colorC);
      consumer.addVertex(pose, d.x, d.y, d.z).setUv(u0, v1).setColor(colorD);
   }

   public static void glowSprite(VertexConsumer consumer, PoseStack.Pose pose, Vector3f center, Vector3f right, Vector3f up, float size, int color) {
      int edge = color & 16777215;
      Vector3f r = new Vector3f(right).mul(size);
      Vector3f u = new Vector3f(up).mul(size);
      Vector3f left = new Vector3f(center).sub(r);
      Vector3f rightPoint = new Vector3f(center).add(r);
      Vector3f top = new Vector3f(center).add(u);
      Vector3f bottom = new Vector3f(center).sub(u);
      Vector3f halfUp = new Vector3f(up).mul(size * 0.35F);
      Vector3f halfRight = new Vector3f(right).mul(size * 0.35F);
      glowQuad(
         consumer,
         pose,
         new Vector3f(left).sub(halfUp),
         new Vector3f(center).sub(halfUp),
         new Vector3f(center).add(halfUp),
         new Vector3f(left).add(halfUp),
         edge,
         color,
         color,
         edge
      );
      glowQuad(
         consumer,
         pose,
         new Vector3f(center).sub(halfUp),
         new Vector3f(rightPoint).sub(halfUp),
         new Vector3f(rightPoint).add(halfUp),
         new Vector3f(center).add(halfUp),
         color,
         edge,
         edge,
         color
      );
      glowQuad(
         consumer,
         pose,
         new Vector3f(bottom).sub(halfRight),
         new Vector3f(bottom).add(halfRight),
         new Vector3f(center).add(halfRight),
         new Vector3f(center).sub(halfRight),
         edge,
         edge,
         color,
         color
      );
      glowQuad(
         consumer,
         pose,
         new Vector3f(center).sub(halfRight),
         new Vector3f(center).add(halfRight),
         new Vector3f(top).add(halfRight),
         new Vector3f(top).sub(halfRight),
         color,
         color,
         edge,
         edge
      );
   }

   public static VertexConsumer lines(VertexConsumer target, float width) {
      return new VertexConsumer() {
         public VertexConsumer addVertex(float x, float y, float z) {
            target.addVertex(x, y, z).setLineWidth(width);
            return this;
         }

         public VertexConsumer setColor(int r, int g, int b, int a) {
            target.setColor(r, g, b, a);
            return this;
         }

         public VertexConsumer setColor(int argb) {
            target.setColor(argb);
            return this;
         }

         public VertexConsumer setUv(float u, float v) {
            target.setUv(u, v);
            return this;
         }

         public VertexConsumer setUv1(int u, int v) {
            target.setUv1(u, v);
            return this;
         }

         public VertexConsumer setUv2(int u, int v) {
            target.setUv2(u, v);
            return this;
         }

         public VertexConsumer setNormal(float x, float y, float z) {
            target.setNormal(x, y, z);
            return this;
         }

         public VertexConsumer setLineWidth(float lineWidth) {
            target.setLineWidth(lineWidth);
            return this;
         }
      };
   }
}
