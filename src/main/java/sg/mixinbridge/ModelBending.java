package sg.mixinbridge;

import sg.mixin.ModelPartAccessor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.ModelPart.Polygon;
import net.minecraft.client.model.geom.ModelPart.Vertex;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ModelBending {
   private static final Map<Cube, ModelBending.BendState> сС = Collections.synchronizedMap(new WeakHashMap<>());
   private static final Map<ModelPart, ModelBending.BendState> сз = Collections.synchronizedMap(new WeakHashMap<>());
   private static final Map<ModelPart, Object> сц = Collections.synchronizedMap(new WeakHashMap<>());
   private static final ThreadLocal<Object> Я = new ThreadLocal<>();
   private static final float ш西 = 1.0E-4F;
   private static final float шд = 1.0E-4F;
   private static final float шБ = 0.375F;
   private static final float шС = -0.375F;
   private static final float шз = 16.0F;
   private static final float шц = 16.0F;
   private static final float шя = 16.0F;
   private static final double Ис = 0.001F;
   private static final float ИВ = -1.0F;
   private static final float ИЯ = 0.5F;
   private static final float ИХ = 1.0E-4F;
   private static final float Ие = 0.5F;
   private static final float ИА = -1.0F;
   private static final float И4 = 1.0E-6F;
   private static final float Ип = 0.5F;
   private static final float ИД = 0.5F;
   private static final float ИЪ = 0.5F;

   private static Vector3f в(Vector3f var0, ModelBending.CubeBounds var1, ModelBending.BendState var2) {
      float var3 = var2.down ? ИВ : 1.0F;
      float var4 = (float)Math.cos(var2.direction);
      float var5 = (float)Math.sin(var2.direction) * var3;
      float var6 = var3 * var5;
      float var7 = -var3 * var4;
      float var8 = var6 * (var0.x - var1.centerX) + var7 * (var0.z - var1.centerZ);
      if (!var2.down) {
         var8 = -var8;
      }

      float var9 = var2.down ? var1.minY - var0.y : var0.y - var1.maxY;
      float var10 = var2.down ? var1.maxY - var0.y : var0.y - var1.minY;
      float var11 = (float)Math.tan(var2.angle * ИЯ) * var8;
      Vector3f var12 = new Vector3f(var0);
      float var13 = Math.max(ИХ, var1.height * Ие) * (var2.down ? ИА : 1.0F);
      if (Math.abs(var9) < Math.abs(var10)) {
         var12.y += var3 * (-var9 / var13 * var11);
         var12.sub(var1.centerX, var1.centerY, var1.centerZ).rotateAxis(var2.angle, var4, 0.0F, var5).add(var1.centerX, var1.centerY, var1.centerZ);
      } else {
         var12.y += var3 * (-var10 / var13 * var11);
      }

      return var12;
   }

   private static ModelBending.CubeBounds в(Cube var0) {
      float var1 = Float.POSITIVE_INFINITY;
      float var2 = Float.NEGATIVE_INFINITY;

      for (Polygon var6 : var0.polygons) {
         for (Vertex var10 : var6.vertices()) {
            var1 = Math.min(var1, var10.y());
            var2 = Math.max(var2, var10.y());
         }
      }

      return new ModelBending.CubeBounds(
         (var0.minX + var0.maxX) * Ип, (var0.minY + var0.maxY) * ИД, (var0.minZ + var0.maxZ) * ИЪ, var1, var2, var2 - var1
      );
   }

   public static void в(ModelPart var0, Object var1, boolean var2, float var3, float var4) {
      for (Cube var6 : ((ModelPartAccessor)(Object)var0).rose$getCuboids()) {
         if (Math.abs(var4) < ш西) {
            сС.remove(var6);
         } else {
            сС.put(var6, new ModelBending.BendState(var1, var2, var3, var4));
         }
      }
   }

   public static void 衣_de5a6f78(ModelPart var0) {
      сз.remove(var0);
   }

   private static Vector3f в(ModelBending.TexturedVertex[] var0, Polygon var1) {
      Vector3f var2 = new Vector3f(var0[0].position).sub(var0[2].position);
      Vector3f var3 = new Vector3f(var0[1].position).sub(var0[3].position);
      Vector3f var4 = var2.cross(var3);
      if (var4.lengthSquared() < И4) {
         var4.set(var1.normal());
      } else {
         var4.normalize();
      }

      if (var4.dot(var1.normal()) < 0.0F) {
         var4.negate();
      }

      return var4;
   }

   private static int в(Vertex var0, Vertex var1) {
      float var2 = var1.x() - var0.x();
      float var3 = var1.y() - var0.y();
      float var4 = var1.z() - var0.z();
      return Math.max(1, (int)Math.floor(Math.sqrt(var2 * var2 + var3 * var3 + var4 * var4) + Ис));
   }

   public static void в(ModelPart var0, Object var1, float var2, float var3) {
      if (Math.abs(var3) < шд) {
         сз.remove(var0);
      } else {
         сз.put(var0, new ModelBending.BendState(var1, false, var2, var3));
      }
   }

   public static Object в_7fd512f1(ModelPart var0) {
      return сц.computeIfAbsent(var0, var0x -> new Object());
   }

   private ModelBending() {
   }

   private static void в(
      Polygon var0,
      ModelBending.CubeBounds var1,
      ModelBending.BendState var2,
      Pose var3,
      VertexConsumer var4,
      int var5,
      int var6,
      int var7
   ) {
      Vertex[] var8 = var0.vertices();
      int var9 = в(var8[0], var8[1]);
      int var10 = в(var8[1], var8[2]);

      for (int var11 = 0; var11 < var10; var11++) {
         float var12 = (float)var11 / var10;
         float var13 = (float)(var11 + 1) / var10;

         for (int var14 = 0; var14 < var9; var14++) {
            float var15 = (float)var14 / var9;
            float var16 = (float)(var14 + 1) / var9;
            ModelBending.TexturedVertex[] var17 = new ModelBending.TexturedVertex[]{
               в(var8, var15, var12, var1, var2), в(var8, var16, var12, var1, var2), в(var8, var16, var13, var1, var2), в(var8, var15, var13, var1, var2)
            };
            Vector3f var18 = в(var17, var0);
            var3.transformNormal(var18, var18).normalize();

            for (ModelBending.TexturedVertex var22 : var17) {
               Vector3f var23 = var3.pose().transformPosition(var22.position.x / шз, var22.position.y / шц, var22.position.z / шя, new Vector3f());
               var4.addVertex(var23.x, var23.y, var23.z, var7, var22.u, var22.v, var6, var5, var18.x, var18.y, var18.z);
            }
         }
      }
   }

   public static void method_2(Object var0) {
      if (var0 == null) {
         Я.remove();
      } else {
         Я.set(var0);
      }
   }

   public static void в_de5a6f78(ModelPart var0) {
      for (Cube var2 : ((ModelPartAccessor)(Object)var0).rose$getCuboids()) {
         сС.remove(var2);
      }
   }

   public static boolean в(Cube var0, Pose var1, VertexConsumer var2, int var3, int var4, int var5) {
      Object var6 = Я.get();
      if (var6 == null) {
         return false;
      }

      ModelBending.BendState var7 = сС.get(var0);
      if (var7 != null && var7.owner == var6) {
         ModelBending.CubeBounds var8 = в(var0);

         for (Polygon var12 : var0.polygons) {
            в(var12, var8, var7, var1, var2, var3, var4, var5);
         }

         return true;
      } else {
         return false;
      }
   }

   public static Object 衣_7fd512f1(ModelPart var0) {
      Object var1 = Я.get();
      method_2(сц.get(var0));
      return var1;
   }

   private static ModelBending.TexturedVertex в(
      Vertex[] var0, float var1, float var2, ModelBending.CubeBounds var3, ModelBending.BendState var4
   ) {
      float var5 = (1.0F - var1) * (1.0F - var2);
      float var6 = var1 * (1.0F - var2);
      float var7 = var1 * var2;
      float var8 = (1.0F - var1) * var2;
      Vector3f var9 = new Vector3f(
         var0[0].x() * var5 + var0[1].x() * var6 + var0[2].x() * var7 + var0[3].x() * var8,
         var0[0].y() * var5 + var0[1].y() * var6 + var0[2].y() * var7 + var0[3].y() * var8,
         var0[0].z() * var5 + var0[1].z() * var6 + var0[2].z() * var7 + var0[3].z() * var8
      );
      float var10 = var0[0].u() * var5 + var0[1].u() * var6 + var0[2].u() * var7 + var0[3].u() * var8;
      float var11 = var0[0].v() * var5 + var0[1].v() * var6 + var0[2].v() * var7 + var0[3].v() * var8;
      return new ModelBending.TexturedVertex(в(var9, var3, var4), var10, var11);
   }

   public static void в(ModelPart var0, PoseStack var1) {
      Object var2 = Я.get();
      if (var2 != null) {
         ModelBending.BendState var3 = сз.get(var0);
         if (var3 != null && var3.owner == var2) {
            float var4 = -var3.direction;
            var1.translate(0.0F, шБ, 0.0F);
            var1.mulPose(new Quaternionf().rotateAxis(var3.angle, (float)Math.cos(var4), 0.0F, (float)Math.sin(var4)));
            var1.translate(0.0F, шС, 0.0F);
         }
      }
   }

   private record BendState(Object owner, boolean down, float direction, float angle) {
   }

   private record CubeBounds(float centerX, float centerY, float centerZ, float minY, float maxY, float height) {
   }

   private record TexturedVertex(Vector3f position, float u, float v) {
   }
}
