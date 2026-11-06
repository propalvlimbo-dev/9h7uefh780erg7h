package sg.render.avatar;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.EntityEspOuterGlowRenderer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class HolyWorldStunRenderer {
   private static final Identifier Р = Identifier.fromNamespaceAndPath(HolyWorldStunRenderer.ВМl, HolyWorldStunRenderer.ВМ8);
   private static final float[][] Я = new float[][]{
      {HolyWorldStunRenderer.ВМ5, HolyWorldStunRenderer.ВМв, HolyWorldStunRenderer.ВМъ, 0.0F, 1.0F, 0.0F},
      {HolyWorldStunRenderer.ВММ, HolyWorldStunRenderer.ВМН, HolyWorldStunRenderer.ВМн, 0.0F, 1.0F, 0.0F},
      {HolyWorldStunRenderer.ВМЦ, HolyWorldStunRenderer.ВМч, HolyWorldStunRenderer.ВМЭ, 0.0F, 1.0F, 0.0F},
      {HolyWorldStunRenderer.ВМ必, HolyWorldStunRenderer.ВМщ, HolyWorldStunRenderer.ВМГ, 0.0F, 1.0F, 0.0F},
      {HolyWorldStunRenderer.ВМл, HolyWorldStunRenderer.ВМэ, HolyWorldStunRenderer.ВМю, 1.0F, 0.0F, 0.0F},
      {HolyWorldStunRenderer.ВМЩ, HolyWorldStunRenderer.ВМу, HolyWorldStunRenderer.ВМ2, 1.0F, 0.0F, 0.0F},
      {HolyWorldStunRenderer.ВМф, HolyWorldStunRenderer.ВМи, HolyWorldStunRenderer.ВМ9, 1.0F, 0.0F, 0.0F},
      {HolyWorldStunRenderer.ВМо, HolyWorldStunRenderer.ВМO, HolyWorldStunRenderer.ВМ7, 1.0F, 0.0F, 0.0F},
      {HolyWorldStunRenderer.ВМР, HolyWorldStunRenderer.ВМ3, HolyWorldStunRenderer.ВМр, 0.0F, 0.0F, 1.0F},
      {HolyWorldStunRenderer.ВМ6, HolyWorldStunRenderer.ВМ_, HolyWorldStunRenderer.ВМШ, 0.0F, 0.0F, 1.0F},
      {HolyWorldStunRenderer.ВМы, HolyWorldStunRenderer.ВМй, HolyWorldStunRenderer.ВМЖ, 0.0F, 0.0F, 1.0F},
      {HolyWorldStunRenderer.ВМм, HolyWorldStunRenderer.ВМК, HolyWorldStunRenderer.ВМI, 0.0F, 0.0F, 1.0F}
   };
   private static final long В2 = 520L;
   private final List<HolyWorldStunRenderer.StunBurst> сЮ = new ArrayList<>();
   private TextureTarget н;
   private final RenderPipeline сч;
   private static final String ВъЗ = "rose/hw_stan_bloom";
   private static final String ВъЕ = "dead_chams";
   private static final String ВъП = "dead_chams";
   private static final String Въ西 = "Sampler0";
   private static final float Въд = 520.0F;
   private static final float ВъБ = 0.025F;
   private static final float ВъС = 0.92F;
   private static final float Въз = 30.0F;
   private static final float Въц = 30.0F;
   private static final float Въя = 30.0F;
   private static final float ВМс = 0.045F;
   private static final float ВМВ = 0.001F;
   private static final float ВМЯ = 0.001F;
   private static final float ВМХ = 255.0F;
   private static final float ВМе = 255.0F;
   private static final float ВМА = 255.0F;
   private static final float ВМ4 = 255.0F;
   private static final float ВМп = 0.5F;
   private static final float ВМД = 0.5F;
   private static final float ВМЪ = 0.5F;
   private static final float ВМт = 0.5F;
   private static final float ВМа = 0.5F;
   private static final float ВМЮ = 0.5F;
   private static final float ВМЧ = 0.5F;
   private static final float ВМЛ = 0.5F;
   private static final float ВМ诶 = 6.0F;
   private static final float ВМ1 = 15.0F;
   private static final float ВМЬ = 10.0F;
   private static final float ВМ衣 = 5.5F;
   private static final float ВМш = 0.72F;
   private static final float ВМИ = 1.05F;
   private static final String ВМь = "Sampler0";
   private static final String ВМж = "holy-world-stun-glow";
   private static final String ВМ弟 = "hwStanFx";
   private static final String ВМб = "hwStanFx";
   private static final double ВМЫ = 0.01;
   private static final String ВМl = "rose";
   private static final String ВМ8 = "images/fireflies/bloom.png";
   private static final float ВМ5 = 15.0F;
   private static final float ВМв = -15.0F;
   private static final float ВМъ = 15.0F;
   private static final float ВММ = -15.0F;
   private static final float ВМН = -15.0F;
   private static final float ВМн = 15.0F;
   private static final float ВМЦ = 15.0F;
   private static final float ВМч = -15.0F;
   private static final float ВМЭ = -15.0F;
   private static final float ВМ必 = -15.0F;
   private static final float ВМщ = -15.0F;
   private static final float ВМГ = -15.0F;
   private static final float ВМл = -15.0F;
   private static final float ВМэ = 15.0F;
   private static final float ВМю = 15.0F;
   private static final float ВМЩ = -15.0F;
   private static final float ВМу = -15.0F;
   private static final float ВМ2 = 15.0F;
   private static final float ВМф = -15.0F;
   private static final float ВМи = 15.0F;
   private static final float ВМ9 = -15.0F;
   private static final float ВМо = -15.0F;
   private static final float ВМO = -15.0F;
   private static final float ВМ7 = -15.0F;
   private static final float ВМР = 15.0F;
   private static final float ВМ3 = 15.0F;
   private static final float ВМр = -15.0F;
   private static final float ВМ6 = -15.0F;
   private static final float ВМ_ = 15.0F;
   private static final float ВМШ = -15.0F;
   private static final float ВМы = 15.0F;
   private static final float ВМй = -15.0F;
   private static final float ВМЖ = -15.0F;
   private static final float ВМм = -15.0F;
   private static final float ВМК = -15.0F;
   private static final float ВМI = -15.0F;

   private void method_7(CommandEncoder var1, RenderTarget var2, GpuBuffer var3, GpuBuffer var4, IndexType var5, int var6, AbstractTexture var7) {
      try (RenderPass var8 = var1.createRenderPass(() -> ВМ弟, var2.getColorTextureView(), Optional.empty(), var2.getDepthTextureView(), OptionalDouble.empty())) {
         RenderSystem.bindDefaultUniforms(var8);
         var8.setPipeline(this.сч);
         var8.bindTexture(ВМь, var7.getTextureView(), var7.getSampler());
         var8.setVertexBuffer(0, var3.slice());
         var8.setIndexBuffer(var4, var5);
         var8.drawIndexed(var6, 1, 0, 0, 0);
      }
   }

   private int method_4(
      BufferBuilder var1, Matrix4f var2, Vec3 var3, double var4, double var6, double var8, double var10, double var12, double var14, float var16, int var17
   ) {
      float var18 = (float)(Math.min(var4, var10) - var3.x);
      float var19 = (float)(Math.min(var6, var12) - var3.y);
      float var20 = (float)(Math.min(var8, var14) - var3.z);
      float var21 = (float)(Math.max(var4, var10) - var3.x);
      float var22 = (float)(Math.max(var6, var12) - var3.y);
      float var23 = (float)(Math.max(var8, var14) - var3.z);
      if (var21 - var18 >= ВМВ) {
         var19 -= var16;
         var22 += var16;
         var20 -= var16;
         var23 += var16;
      } else if (var22 - var19 >= ВМЯ) {
         var18 -= var16;
         var21 += var16;
         var20 -= var16;
         var23 += var16;
      } else {
         var18 -= var16;
         var21 += var16;
         var19 -= var16;
         var22 += var16;
      }

      return this.method_7(var1, var2, var18, var19, var20, var21, var22, var23, var17);
   }

   private void Ч(int var1, int var2) {
      var1 = Math.max(1, var1);
      var2 = Math.max(1, var2);
      if (this.н == null) {
         this.н = new TextureTarget(ВМж, var1, var2, true, GpuFormat.RGBA8_UNORM);
      } else if (this.н.width != var1 || this.н.height != var2) {
         this.н.resize(var1, var2);
      }
   }

   private static float о(float var0) {
      return var0 * var0 * var0 * (var0 * (var0 * ВМ诶 - ВМ1) + ВМЬ);
   }

   private int method_7(
      BufferBuilder var1,
      Matrix4f var2,
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
      float var18
   ) {
      var1.addVertex(var2, var3, var4, var5).setUv(ВМп, ВМД).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var6, var7, var8).setUv(ВМЪ, ВМт).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var9, var10, var11).setUv(ВМа, ВМЮ).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var12, var13, var14).setUv(ВМЧ, ВМЛ).setColor(var15, var16, var17, var18);
      return 4;
   }

   public void method__(Vec3 var1) {
      this.сЮ.removeIf(var1x -> var1x.center.distanceToSqr(var1) < ВМЫ);
      this.сЮ.add(new HolyWorldStunRenderer.StunBurst(var1, System.currentTimeMillis()));
   }

   private void method_4(BufferBuilder var1, int var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         if (var2 == 0) {
            var3.close();
         } else {
            GpuBuffer var4 = null;

            try {
               DrawState var5 = var3.drawState();
               int var6 = var5.indexCount();
               AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
               GpuBuffer var8 = var7.getBuffer(var6);
               IndexType var9 = var7.type();
               var4 = RenderSystem.getDevice().createBuffer(() -> ВМб, 32, var3.vertexBuffer());
               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               this.Ч(var10.width, var10.height);
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(this.н.getColorTexture(), new Vector4f(0.0F));
               this.н.copyDepthFrom(var10);
               AbstractTexture var12 = Minecraft.getInstance().getTextureManager().getTexture(Р);
               this.method_7(var11, this.н, var4, var8, var9, var6, var12);
               EntityEspOuterGlowRenderer.compositeOuterGlow(this.н, var10, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ВМ衣, ВМш, ВМИ, true, 0.0F, 0, 0.0F);
               this.method_7(RenderSystem.getDevice().createCommandEncoder(), var10, var4, var8, var9, var6, var12);
            } finally {
               if (var4 != null) {
                  var4.close();
               }

               var3.close();
            }
         }
      }
   }

   public HolyWorldStunRenderer() {
      this.сч = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВъЗ)
         .withVertexShader(ClientResourceIds.getCoreId(ВъЕ))
         .withFragmentShader(ClientResourceIds.getCoreId(ВъП))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Въ西)
         .build();
   }

   public void Н3() {
      this.сЮ.clear();
   }

   public void method_7(WorldEffectsEvent var1, List<Vec3> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var3.level != null) {
         if (!var2.isEmpty() || !this.сЮ.isEmpty()) {
            Vec3 var4 = var1.getCameraPosition();
            Matrix4f var5 = var1.getPoses().last().pose();
            int var6 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
            BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var8 = 0;

            for (Vec3 var10 : var2) {
               var8 += this.method_7(var7, var5, var4, var10, var6, 1.0F, 1.0F);
            }

            long var15 = System.currentTimeMillis();
            Iterator var11 = this.сЮ.iterator();

            while (var11.hasNext()) {
               HolyWorldStunRenderer.StunBurst var12 = (HolyWorldStunRenderer.StunBurst)var11.next();
               float var13 = Mth.clamp((float)(var15 - var12.startedAt) / Въд, 0.0F, 1.0F);
               if (var13 >= 1.0F) {
                  var11.remove();
               } else {
                  float var14 = о(var13);
                  var8 += this.method_7(var7, var5, var4, var12.center, var6, 1.0F - var14, 1.0F - var14 * ВъБ);
               }
            }

            this.method_4(var7, var8);
         }
      } else {
         this.Н3();
      }
   }

   private int method_7(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, int var5, float var6, float var7) {
      int var8 = 0;
      int var9 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);

      for (int var10 = 0; var10 < Я.length; var10++) {
         float[] var11 = Я[var10];
         int var12 = ColorMath.lerpArgb(var5, var9, (float)var10 / (Я.length - 1));
         int var13 = ColorMath.withAlpha(var12, ВъС * var6);
         double var14 = var4.x + var11[0] * var7;
         double var16 = var4.y + var11[1] * var7;
         double var18 = var4.z + var11[2] * var7;
         double var20 = var14 + Въз * var11[3] * var7;
         double var22 = var16 + Въц * var11[4] * var7;
         double var24 = var18 + Въя * var11[5] * var7;
         var8 += this.method_4(var1, var2, var3, var14, var16, var18, var20, var22, var24, ВМс, var13);
      }

      return var8;
   }

   private int method_7(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      float var10 = ColorMath.getRed(var9) / ВМХ;
      float var11 = ColorMath.getGreen(var9) / ВМе;
      float var12 = ColorMath.getBlue(var9) / ВМА;
      float var13 = ColorMath.getAlpha(var9) / ВМ4;
      int var14 = 0;
      var14 += this.method_7(var1, var2, var3, var4, var5, var3, var7, var5, var3, var7, var8, var3, var4, var8, var10, var11, var12, var13);
      var14 += this.method_7(var1, var2, var6, var4, var8, var6, var7, var8, var6, var7, var5, var6, var4, var5, var10, var11, var12, var13);
      var14 += this.method_7(var1, var2, var3, var4, var8, var6, var4, var8, var6, var4, var5, var3, var4, var5, var10, var11, var12, var13);
      var14 += this.method_7(var1, var2, var3, var7, var5, var6, var7, var5, var6, var7, var8, var3, var7, var8, var10, var11, var12, var13);
      var14 += this.method_7(var1, var2, var6, var4, var5, var6, var7, var5, var3, var7, var5, var3, var4, var5, var10, var11, var12, var13);
      return var14 + this.method_7(var1, var2, var3, var4, var8, var3, var7, var8, var6, var7, var8, var6, var4, var8, var10, var11, var12, var13);
   }

   private record StunBurst(Vec3 center, long startedAt) {
   }
}
