package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sg.ec.MinecraftAccess;
import sg.util.time.ColorMath;

public final class CatPetRenderer implements MinecraftAccess {
   private static final float тй = 64.0F;
   private static final float тЖ = 32.0F;
   private static final float тм = (float) (Math.PI / 180.0);
   private static final Vector3f field_4 = new Vector3f(CatPetRenderer.ЯыO, CatPetRenderer.Яы7, CatPetRenderer.ЯыР).normalize();
   private static final Vector3f п = new Vector3f(CatPetRenderer.Яы3, CatPetRenderer.Яыр, CatPetRenderer.Яы6).normalize();
   private final Deque<Matrix4f> Я = new ArrayDeque<>();
   @SerializedName("2")
   private final Matrix4f field_2 = new Matrix4f();
   private final Matrix4f ф = new Matrix4f();
   private final Matrix4f и = new Matrix4f();
   private final Vector3f Д = new Vector3f();
   @SerializedName("В")
   private BufferBuilder В_78c86a6d;
   @SerializedName("В")
   private CatPetRenderer.CoatVariant В_bcd16cbc = CatPetRenderer.CoatVariant.SKIN;
   private float тК = 1.0F;
   private float тI = 1.0F;
   private int ДЗ;
   private static final String ЯыХ = "Sampler0";
   private static final float Яые = (float) (Math.PI / 180.0);
   private static final float ЯыА = (float) (Math.PI / 180.0);
   private static final float Яы4 = (float) (Math.PI / 180.0);
   private static final float Яып = -1.0F;
   private static final float ЯыД = -1.0F;
   private static final float ЯыЪ = -1.0F;
   private static final float Яыт = 64.0F;
   private static final float Яыа = 64.0F;
   private static final float ЯыЮ = 32.0F;
   private static final float ЯыЧ = 32.0F;
   private static final float ЯыЛ = 64.0F;
   private static final float Яы诶 = 64.0F;
   private static final float Яы1 = 32.0F;
   private static final float ЯыЬ = 32.0F;
   private static final float Яы衣 = 64.0F;
   private static final float Яыш = 64.0F;
   private static final float ЯыИ = 32.0F;
   private static final float Яыь = 32.0F;
   private static final float Яыж = 64.0F;
   private static final float Яы弟 = 64.0F;
   private static final float Яыб = 32.0F;
   private static final float ЯыЫ = 32.0F;
   private static final float Яыl = 64.0F;
   private static final float Яы8 = 64.0F;
   private static final float Яы5 = 32.0F;
   private static final float Яыв = 32.0F;
   private static final float Яыъ = 64.0F;
   private static final float ЯыМ = 64.0F;
   private static final float ЯыН = 32.0F;
   private static final float Яын = 32.0F;
   private static final float ЯыЦ = 255.0F;
   private static final float Яыч = 255.0F;
   private static final float ЯыЭ = 255.0F;
   private static final float Яы必 = 255.0F;
   private static final double Яыщ = Math.PI;
   private static final double ЯыГ = 2.0;
   private static final double Яыл = Math.PI;
   private static final double Яыэ = 2.0;
   private static final float Яыю = 0.5F;
   private static final float ЯыЩ = 0.5F;
   private static final float Яыу = 0.55F;
   private static final float Яы2 = 0.35F;
   private static final float Яыф = 0.1F;
   private static final float Яыи = 0.1F;
   private static final String Яы9 = "catPet";
   private static final String Яыо = "catPetVertices";
   private static final float ЯыO = -0.42F;
   private static final float Яы7 = 0.8F;
   private static final float ЯыР = -0.43F;
   private static final float Яы3 = 0.68F;
   private static final float Яыр = 0.2F;
   private static final float Яы6 = 0.7F;

   public void method_7(float var1, float var2, float var3) {
      this.ф.scale(var1, var2, var3);
   }

   public void ы6(float var1) {
      this.ф.scale(var1);
   }

   private void д(
      float var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      float var15,
      float var16,
      float var17,
      float var18,
      float var19,
      float var20,
      float var21
   ) {
      float var22 = Math.min(1.0F, var21);
      float var23 = Math.min(1.0F, var21);
      float var24 = Math.min(1.0F, var21);
      float var25 = this.тК;
      this.д(var1, var2, var3, var13, var14, var22, var23, var24, var25);
      this.д(var4, var5, var6, var15, var16, var22, var23, var24, var25);
      this.д(var7, var8, var9, var17, var18, var22, var23, var24, var25);
      this.д(var10, var11, var12, var19, var20, var22, var23, var24, var25);
      this.ДЗ += 4;
   }

   public void ыъ(float var1) {
      this.ф.rotateZ(var1 * Яы4);
   }

   public void д(RenderPipeline var1, AbstractTexture var2) {
      if (this.В_78c86a6d != null) {
         MeshData var3 = this.В_78c86a6d.build();
         this.В_78c86a6d = null;
         if (var3 != null) {
            if (this.ДЗ == 0) {
               var3.close();
            } else {
               GpuBuffer var4 = null;

               try {
                  DrawState var5 = var3.drawState();
                  int var6 = var5.indexCount();
                  AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
                  GpuBuffer var8 = var7.getBuffer(var6);
                  IndexType var9 = var7.type();
                  var4 = RenderSystem.getDevice().createBuffer(() -> Яыо, 32, var3.vertexBuffer());
                  RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
                  CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();

                  try (RenderPass var12 = var11.createRenderPass(
                        () -> Яы9, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty()
                     )) {
                     RenderSystem.bindDefaultUniforms(var12);
                     var12.setPipeline(var1);
                     if (var2 != null) {
                        var12.bindTexture(ЯыХ, var2.getTextureView(), var2.getSampler());
                     }

                     var12.setVertexBuffer(0, var4.slice());
                     var12.setIndexBuffer(var8, var9);
                     var12.drawIndexed(var6, 1, 0, 0, 0);
                  }
               } finally {
                  if (var4 != null) {
                     var4.close();
                  }

                  var3.close();
               }
            }
         }
      }
   }

   public void ы9(float var1) {
      this.ф.rotateX(var1 * Яые);
   }

   public void йК() {
      this.ф.set(this.Я.pop());
   }

   public void Х_de5a6f78(float var1, float var2, float var3) {
      this.ф.translate(var1, var2, var3);
   }

   private void д(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.В_78c86a6d.addVertex(this.и, var1, var2, var3).setUv(var4, var5).setColor(var6, var7, var8, var9);
   }

   public void д(RenderPipeline var1) {
      this.д(var1, null);
   }

   public void д(CatPetRenderer.CoatVariant var1, Matrix4f var2, float var3, float var4) {
      this.В_bcd16cbc = var1;
      this.тК = var3;
      this.тI = var4;
      this.ДЗ = 0;
      this.Я.clear();
      this.field_2.set(var2);
      this.ф.identity();
      this.В_78c86a6d = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
   }

   public void ыь(float var1) {
      this.ф.rotateY(var1 * ЯыА);
   }

   private float Х_f67ab10a(float var1, float var2, float var3) {
      this.Д.set(var1, var2, var3);
      this.ф.transformDirection(this.Д).normalize();
      float var4 = Math.max(0.0F, this.Д.dot(field_4));
      float var5 = Math.max(0.0F, this.Д.dot(п));
      float var6 = Яыю + ЯыЩ * this.Д.y;
      return (Яыу + var4 * Яы2 + var5 * Яыф + var6 * Яыи) * this.тI;
   }

   public void йщ() {
      this.Я.push(new Matrix4f(this.ф));
   }

   public void В(float var1, float var2, float var3, int var4, int var5) {
      if (this.В_bcd16cbc == CatPetRenderer.CoatVariant.SHADOW) {
         this.и.set(this.field_2).mul(this.ф);
         float var6 = ColorMath.getRed(var4) / ЯыЦ;
         float var7 = ColorMath.getGreen(var4) / Яыч;
         float var8 = ColorMath.getBlue(var4) / ЯыЭ;
         float var9 = ColorMath.getAlpha(var4) / Яы必 * this.тК;

         for (int var10 = 0; var10 < var5; var10++) {
            double var11 = (double)var10 / var5 * Яыщ * ЯыГ;
            double var13 = (double)(var10 + 1) / var5 * Яыл * Яыэ;
            float var15 = (float)Math.cos(var11) * var1;
            float var16 = (float)Math.sin(var11) * var2;
            float var17 = (float)Math.cos(var13) * var1;
            float var18 = (float)Math.sin(var13) * var2;
            this.д(0.0F, var3, 0.0F, 0.0F, 0.0F, var6, var7, var8, var9);
            this.д(var15, var3, var16, 1.0F, 0.0F, var6, var7, var8, var9);
            this.д(var17, var3, var18, 1.0F, 0.0F, var6, var7, var8, var9);
            this.д(0.0F, var3, 0.0F, 0.0F, 0.0F, var6, var7, var8, var9);
            this.ДЗ += 4;
         }
      }
   }

   public void д(float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (this.В_bcd16cbc == CatPetRenderer.CoatVariant.SKIN) {
         this.и.set(this.field_2).mul(this.ф);
         float var9 = var1 + var4;
         float var10 = var2 + var5;
         float var11 = var3 + var6;
         float var12 = this.Х_f67ab10a(0.0F, 0.0F, Яып);
         float var13 = this.Х_f67ab10a(0.0F, 0.0F, 1.0F);
         float var14 = this.Х_f67ab10a(ЯыД, 0.0F, 0.0F);
         float var15 = this.Х_f67ab10a(1.0F, 0.0F, 0.0F);
         float var16 = this.Х_f67ab10a(0.0F, 1.0F, 0.0F);
         float var17 = this.Х_f67ab10a(0.0F, ЯыЪ, 0.0F);
         float var18 = (var7 + var6) / Яыт;
         float var19 = (var7 + var6 + var4) / Яыа;
         float var20 = (var8 + var6) / ЯыЮ;
         float var21 = (var8 + var6 + var5) / ЯыЧ;
         float var22 = (var7 + var6 + var4 + var6) / ЯыЛ;
         float var23 = (var7 + var6 + var4 + var6 + var4) / Яы诶;
         float var24 = (var8 + var6) / Яы1;
         float var25 = (var8 + var6 + var5) / ЯыЬ;
         float var26 = var7 / Яы衣;
         float var27 = (var7 + var6) / Яыш;
         float var28 = (var8 + var6) / ЯыИ;
         float var29 = (var8 + var6 + var5) / Яыь;
         float var30 = (var7 + var6 + var4) / Яыж;
         float var31 = (var7 + var6 + var4 + var6) / Яы弟;
         float var32 = (var8 + var6) / Яыб;
         float var33 = (var8 + var6 + var5) / ЯыЫ;
         float var34 = (var7 + var6) / Яыl;
         float var35 = (var7 + var6 + var4) / Яы8;
         float var36 = var8 / Яы5;
         float var37 = (var8 + var6) / Яыв;
         float var38 = (var7 + var6 + var4) / Яыъ;
         float var39 = (var7 + var6 + var4 + var4) / ЯыМ;
         float var40 = var8 / ЯыН;
         float var41 = (var8 + var6) / Яын;
         this.д(var1, var2, var3, var9, var2, var3, var9, var10, var3, var1, var10, var3, var19, var21, var18, var21, var18, var20, var19, var20, var12);
         this.д(var9, var2, var11, var1, var2, var11, var1, var10, var11, var9, var10, var11, var22, var25, var23, var25, var23, var24, var22, var24, var13);
         this.д(var1, var2, var11, var1, var2, var3, var1, var10, var3, var1, var10, var11, var26, var29, var27, var29, var27, var28, var26, var28, var14);
         this.д(var9, var2, var3, var9, var2, var11, var9, var10, var11, var9, var10, var3, var30, var33, var31, var33, var31, var32, var30, var32, var15);
         this.д(var1, var10, var11, var9, var10, var11, var9, var10, var3, var1, var10, var3, var34, var37, var35, var37, var35, var36, var34, var36, var16);
         this.д(var1, var2, var3, var9, var2, var3, var9, var2, var11, var1, var2, var11, var38, var40, var39, var40, var39, var41, var38, var41, var17);
      }
   }

   public enum CoatVariant {
      SKIN,
      SHADOW;
   }
}
