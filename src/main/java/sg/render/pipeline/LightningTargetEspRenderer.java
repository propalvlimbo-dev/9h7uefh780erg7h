package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.DynamicUniformStorage.DynamicUniform;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.render.ClientResourceIds;
import sg.mixin.RenderPipelinesAccessor;
import sg.render.WorldProjection;

public final class LightningTargetEspRenderer {
   public static final String сО = "Auto";
   public static final String сФ = "Always";
   public static final String сЙ = "Never";
   private static final Minecraft field_4 = Minecraft.getInstance();
   private static final Logger Я_9c0cd60f = LoggerFactory.getLogger(LightningTargetEspRenderer.В衣к);
   private static final List<Runnable> Е = new ArrayList<>();
   private static final BlendFunction Я_515b3179 = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ZERO);
   private static final BlendFunction Х_515b3179 = new BlendFunction(
      BlendFactor.ONE, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_SRC_ALPHA
   );
   private static final RenderPipeline сш = н(LightningTargetEspRenderer.В衣г, CompareOp.ALWAYS_PASS);
   private static final RenderPipeline сИ = н(LightningTargetEspRenderer.В衣0, CompareOp.GREATER_THAN_OR_EQUAL);
   private static final RenderPipeline сь = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder()
         .withVertexShader(ClientResourceIds.getCoreId(LightningTargetEspRenderer.В衣х))
         .withFragmentShader(ClientResourceIds.getCoreId(LightningTargetEspRenderer.В衣У))
         .withSampler(LightningTargetEspRenderer.В衣З)
         .withUniform(LightningTargetEspRenderer.В衣Е, UniformType.UNIFORM_BUFFER)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.TRIANGLE_STRIP)
         .withLocation(Identifier.fromNamespaceAndPath(LightningTargetEspRenderer.В衣П, LightningTargetEspRenderer.В衣西))
         .build()
   );
   private static final RenderPipeline сж = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder()
         .withVertexShader(ClientResourceIds.getCoreId(LightningTargetEspRenderer.В衣д))
         .withFragmentShader(ClientResourceIds.getCoreId(LightningTargetEspRenderer.В衣Б))
         .withSampler(LightningTargetEspRenderer.В衣С)
         .withUniform(LightningTargetEspRenderer.В衣з, UniformType.UNIFORM_BUFFER)
         .withBlend(Х_515b3179)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.TRIANGLE_STRIP)
         .withLocation(Identifier.fromNamespaceAndPath(LightningTargetEspRenderer.В衣ц, LightningTargetEspRenderer.В衣я))
         .build()
   );
   private static final DynamicUniformStorage<LightningTargetEspRenderer.LightningUniform> В_071c1034 = new DynamicUniformStorage<>(LightningTargetEspRenderer.Вшс, 96, 256);
   private static final DynamicUniformStorage<LightningTargetEspRenderer.ScalarUniform> Я_071c1034 = new DynamicUniformStorage<>(LightningTargetEspRenderer.ВшВ, 16, 256);
   private static final DynamicUniformStorage<LightningTargetEspRenderer.ScalarUniform> Х_071c1034 = new DynamicUniformStorage<>(LightningTargetEspRenderer.ВшЯ, 16, 256);
   private static GpuBuffer н;
   private static long ВВ;
   private static GpuBuffer Ц;
   private static GpuSampler В_fe230870;
   private static TextureTarget б;
   private static TextureTarget Ы;
   private static TextureTarget l;
   private static long ВЯ;
   private static float еш;
   private static boolean Вл;
   private static final String ВЬр = "target/lightning_line";
   private static final String ВЬ6 = "target/lightning";
   private static final String ВЬ_ = "LightningLine";
   private static final String ВЬШ = "rose";
   private static final float ВЬы = 12.0F;
   private static final float ВЬй = 0.007843138F;
   private static final String ВЬЖ = "Lightning TargetESP disabled after a render failure";
   private static final float ВЬм = 0.016666668F;
   private static final float ВЬК = 1.0E9F;
   private static final float ВЬI = 0.05F;
   private static final float ВЬТ = 0.75F;
   private static final float ВЬО = 0.16F;
   private static final double ВЬФ = 0.35;
   private static final int ВЬЙ = -16777216;
   private static final int ВЬк = 16718349;
   private static final float ВЬг = 0.5F;
   private static final float ВЬ0 = 0.085F;
   private static final float ВЬх = 10.0F;
   private static final float ВЬУ = 22.0F;
   private static final float ВЬЗ = 0.4F;
   private static final float ВЬЕ = 9.0F;
   private static final float ВЬП = 6.0F;
   private static final float ВЬ西 = 1.3F;
   private static final float ВЬд = (float) (Math.PI * 2);
   private static final float ВЬБ = 0.08F;
   private static final float ВЬС = 0.42F;
   private static final float ВЬз = 0.3F;
   private static final float ВЬц = 0.55F;
   private static final float ВЬя = 0.45F;
   private static final float В衣с = 0.01F;
   private static final float В衣В = 0.16F;
   private static final float В衣Я = 1.7F;
   private static final float В衣Х = 2.1F;
   private static final float В衣е = (float) Math.PI;
   private static final float В衣А = 0.6F;
   private static final float В衣4 = 0.5F;
   private static final float В衣п = -1.0F;
   private static final float В衣Д = 0.85F;
   private static final float В衣Ъ = 0.05F;
   private static final float В衣т = 0.67F;
   private static final float В衣а = 0.12F;
   private static final float В衣Ю = 0.9F;
   private static final float В衣Ч = 10.0F;
   private static final float В衣Л = 0.5F;
   private static final float В衣诶 = 1.0E-4F;
   private static final float В衣1 = 0.9F;
   private static final float В衣Ь = 0.9F;
   private static final float В衣衣 = 1.0E-5F;
   private static final float В衣ш = 0.3F;
   private static final float В衣И = 0.5F;
   private static final float В衣ь = 0.5F;
   private static final float В衣ж = 0.5F;
   private static final float В衣弟 = 0.5F;
   private static final float В衣б = 0.45F;
   private static final float В衣Ы = 0.4F;
   private static final float В衣l = 0.4F;
   private static final float В衣8 = 0.4F;
   private static final float В衣5 = 1.0E-5F;
   private static final float В衣в = 0.75F;
   private static final float В衣ъ = 0.55F;
   private static final float В衣М = 0.7F;
   private static final float В衣Н = 1.0E-5F;
   private static final float В衣н = 3.0F;
   private static final int В衣Ц = 374761393;
   private static final int В衣ч = 668265263;
   private static final int В衣Э = 1274126177;
   private static final int В衣必 = 1274126177;
   private static final int В衣щ = 65535;
   private static final float В衣Г = 32768.0F;
   private static final String В衣л = "LightningLine";
   private static final String В衣э = "Rose TargetESP lightning blur horizontal";
   private static final float В衣ю = 0.5F;
   private static final String В衣Щ = "Rose TargetESP lightning blur vertical";
   private static final String В衣у = "LightningComposite";
   private static final String В衣2 = "Sampler0";
   private static final String В衣ф = "LightningBlur";
   private static final String В衣и = "Sampler0";
   private static final String В衣9 = "Always";
   private static final String В衣о = "Never";
   private static final double В衣O = 1.0E-8;
   private static final double В衣7 = 0.5;
   private static final float В衣Р = 36.0F;
   private static final int В衣3 = 16777215;
   private static final String В衣р = "Rose TargetESP lightning capture";
   private static final String В衣6 = "Rose TargetESP lightning blur A";
   private static final String В衣_ = "Rose TargetESP lightning blur B";
   private static final long В衣Ш = 8192L;
   private static final float В衣ы = -1.0F;
   private static final float В衣й = -1.0F;
   private static final float В衣Ж = -1.0F;
   private static final float В衣м = -1.0F;
   private static final long В衣К = 4L;
   private static final long В衣I = 4L;
   private static final String В衣Т = "Rose TargetESP lightning fullscreen quad";
   private static final String В衣О = "Rose TargetESP lightning vertices";
   private static final String В衣Ф = "Rose TargetESP lightning bloom composite";
   private static final String В衣Й = "Rose TargetESP lightning";
   private static final String В衣к = "rose-target-lightning";
   private static final String В衣г = "target_lightning";
   private static final String В衣0 = "target_lightning_depth";
   private static final String В衣х = "esp/esp_fullscreen";
   private static final String В衣У = "target/lightning_blur";
   private static final String В衣З = "Sampler0";
   private static final String В衣Е = "LightningBlur";
   private static final String В衣П = "rose";
   private static final String В衣西 = "pipeline/target_lightning_blur";
   private static final String В衣д = "esp/esp_fullscreen";
   private static final String В衣Б = "target/lightning_composite";
   private static final String В衣С = "Sampler0";
   private static final String В衣з = "LightningComposite";
   private static final String В衣ц = "rose";
   private static final String В衣я = "pipeline/target_lightning_composite";
   private static final String Вшс = "Target lightning line UBO";
   private static final String ВшВ = "Target lightning blur UBO";
   private static final String ВшЯ = "Target lightning composite UBO";

   private static void method_1(RenderTarget var0, float var1, float var2, boolean var3, float var4) {
      if (б != null && Ы != null && l != null && Ц != null) {
         CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
         method_1(var5, б.getColorTextureView(), l, var1, В衣э);
         method_1(var5, l.getColorTextureView(), Ы, -var1 * В衣ю, В衣Щ);
         if (var3) {
            GlowDistortionRenderer.INSTANCE.distortMaskTarget(Ы, var4);
         }

         GpuBufferSlice var6 = Х_071c1034.writeUniform(new LightningTargetEspRenderer.ScalarUniform(var2));

         try (RenderPass var7 = var5.createRenderPass(() -> В衣Ф, var0.getColorTextureView(), Optional.empty(), null, OptionalDouble.empty())) {
            var7.setPipeline(сж);
            var7.setUniform(В衣у, var6);
            var7.bindTexture(В衣2, Ы.getColorTextureView(), method_1());
            var7.setVertexBuffer(0, Ц.slice());
            var7.draw(4, 1, 0, 0);
         }
      }
   }

   private static void ны() {
      method_1(б);
      method_1(Ы);
      method_1(l);
      б = null;
      Ы = null;
      l = null;
      г(н);
      н = null;
      ВВ = 0L;
      г(Ц);
      Ц = null;
      В_fe230870 = null;
   }

   private static void г(Camera var0, float var1, LivingEntity var2, float var3, int var4, boolean var5, String var6, boolean var7, float var8) {
      RenderTarget var9 = field_4.gameRenderer.mainRenderTarget();
      if (var9 != null && var9.width > 0 && var9.height > 0) {
         Ш(var9.width, var9.height);
         method_1(RenderSystem.getDevice());
         long var10 = System.nanoTime();
         float var12 = ВЯ == 0L ? ВЬм : Math.min((float)(var10 - ВЯ) / ВЬК, ВЬI);
         ВЯ = var10;
         еш += var12;
         Vec3 var13 = var0.position();
         float var14 = (float)(Mth.lerp(var1, var2.xOld, var2.getX()) - var13.x);
         float var15 = (float)(Mth.lerp(var1, var2.yOld, var2.getY()) - var13.y);
         float var16 = (float)(Mth.lerp(var1, var2.zOld, var2.getZ()) - var13.z);
         float var17 = var2.getBbHeight();
         float var18 = var2.getBbWidth() * ВЬТ + ВЬО;
         int var19 = var4;
         if (var5 && var2.hurtTime > 0) {
            float var20 = Math.max(0.0F, var2.hurtTime - var1);
            float var21 = Math.max(1.0F, var2.hurtDuration);
            float var22 = Mth.clamp(var20 / var21, 0.0F, 1.0F);
            float var23 = (float)Math.pow(var22, ВЬФ);
            var19 = г(var4, var4 & ВЬЙ | ВЬк, var23);
         }

         boolean var30 = method_1(var6, var13, var2);
         RenderPipeline var31 = var30 ? сИ : сш;
         Matrix4f var32 = н();
         if (var32 != null) {
            float var33 = var15 + var17 * ВЬг;
            double var24 = Math.sqrt(var14 * var14 + var33 * var33 + var16 * var16);
            float var26 = method_1(ВЬ0, var24, var32, var9.height);
            MeshData var27 = method_1(var14, var15, var16, var17, var18, var19, var3, ВЬх, 1.0F);
            if (var27 != null) {
               method_1(var27, var31, var32, var9.width, var9.height, б.getColorTextureView(), var9.useDepth ? var9.getDepthTextureView() : null, true);
               method_1(var9, var26, var3, var7, var8);
            }

            MeshData var28 = method_1(var14, var15, var16, var17, var18, var19, var3, ВЬУ, ВЬЗ);
            method_1(var28, var31, var32, var9.width, var9.height, var9.getColorTextureView(), var9.useDepth ? var9.getDepthTextureView() : null, false);
            MeshData var29 = method_1(var14, var15, var16, var17, var18, var19, var3, ВЬЕ, 1.0F);
            method_1(var29, var31, var32, var9.width, var9.height, var9.getColorTextureView(), var9.useDepth ? var9.getDepthTextureView() : null, false);
         }
      }
   }

   private static int д(int var0, float var1) {
      int var2 = Math.round((var0 >>> 24 & 0xFF) * Mth.clamp(var1, 0.0F, 1.0F));
      return var2 << 24 | var0 & В衣3;
   }

   private static void method_1(BufferBuilder var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, float var9) {
      float var10 = var4 - var1;
      float var11 = var5 - var2;
      float var12 = var6 - var3;
      float var13 = (float)Math.sqrt(var10 * var10 + var11 * var11 + var12 * var12);
      if (!(var13 < В衣Н)) {
         float var14 = 1.0F / var13;
         float var15 = var10 * var14;
         float var16 = var11 * var14;
         float var17 = var12 * var14;
         var0.addVertex(var1, var2, var3).setColor(var7).setNormal(var15, var16, var17).setLineWidth(var9);
         var0.addVertex(var4, var5, var6).setColor(var8).setNormal(var15, var16, var17).setLineWidth(var9);
      }
   }

   private static void у(int var0) {
      if (н == null || н.isClosed() || ВВ < var0) {
         г(н);
         long var1 = В衣Ш;

         while (var1 < var0) {
            var1 <<= 1;
         }

         н = RenderSystem.getDevice().createBuffer(() -> В衣О, 40, var1);
         ВВ = var1;
      }
   }

   private static void method_1(TextureTarget var0) {
      if (var0 != null) {
         Е.add(var0::destroyBuffers);
      }
   }

   private LightningTargetEspRenderer() {
   }

   public static void н2() {
      ны();
      ни();
      ВЯ = 0L;
      еш = 0.0F;
      Вл = false;
   }

   private static Matrix4f н() {
      Matrix4f var0 = WorldProjection.getProjectionMatrix();
      Matrix4f var1 = WorldProjection.getViewMatrix();
      return var0 != null && var1 != null ? var0.mul(var1) : null;
   }

   private static void method_1(GpuDevice var0) {
      if (Ц == null || Ц.isClosed()) {
         Ц = null;
         float[] var1 = new float[]{В衣ы, В衣й, 0.0F, 1.0F, В衣Ж, 0.0F, В衣м, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F};
         ByteBuffer var2 = ByteBuffer.allocateDirect(var1.length * 4).order(ByteOrder.nativeOrder());

         for (float var6 : var1) {
            var2.putFloat(var6);
         }

         var2.flip();
         Ц = var0.createBuffer(() -> В衣Т, 40, var1.length * В衣К);
         var0.createCommandEncoder().writeToBuffer(Ц.slice(0L, var1.length * В衣I), var2);
      }
   }

   public static void method_1(Camera var0, float var1, LivingEntity var2, float var3, int var4, boolean var5, String var6, boolean var7, float var8) {
      if (!Вл && var0 != null && var2 != null && !(var3 <= ВЬй) && field_4.level != null && field_4.player != null) {
         try {
            г(var0, var1, var2, var3, var4, var5, var6, var7, var8);
         } catch (Throwable var10) {
            Вл = true;
            Я_9c0cd60f.error(ВЬЖ, var10);
            ны();
         }
      }
   }

   private static int г(int var0, int var1, float var2) {
      float var3 = Mth.clamp(var2, 0.0F, 1.0F);
      int var4 = Mth.lerpInt(var3, var0 >>> 24 & 0xFF, var1 >>> 24 & 0xFF);
      int var5 = Mth.lerpInt(var3, var0 >>> 16 & 0xFF, var1 >>> 16 & 0xFF);
      int var6 = Mth.lerpInt(var3, var0 >>> 8 & 0xFF, var1 >>> 8 & 0xFF);
      int var7 = Mth.lerpInt(var3, var0 & 0xFF, var1 & 0xFF);
      return var4 << 24 | var5 << 16 | var6 << 8 | var7;
   }

   private static void method_1(
      BufferBuilder var0,
      float var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      int var7,
      float var8,
      float var9,
      float var10,
      int var11,
      int var12,
      int var13,
      int var14,
      float var15
   ) {
      float var16 = var4 - var1;
      float var17 = var5 - var2;
      float var18 = var6 - var3;
      float var19 = (float)Math.sqrt(var16 * var16 + var17 * var17 + var18 * var18);
      if (var7 > 0 && !(var19 < В衣诶)) {
         float var20 = var16 / var19;
         float var21 = var17 / var19;
         float var22 = var18 / var19;
         float var23 = Math.abs(var21) < В衣1 ? 0.0F : 1.0F;
         float var24 = Math.abs(var21) < В衣Ь ? 1.0F : 0.0F;
         float var25 = -var22 * var24;
         float var26 = var22 * var23;
         float var27 = var20 * var24 - var21 * var23;
         float var28 = (float)Math.sqrt(var25 * var25 + var26 * var26 + var27 * var27);
         if (var28 < В衣衣) {
            var28 = 1.0F;
         }

         var25 /= var28;
         var26 /= var28;
         var27 /= var28;
         float var29 = var21 * var27 - var22 * var26;
         float var30 = var22 * var25 - var20 * var27;
         float var31 = var20 * var26 - var21 * var25;
         float var32 = method_1(var12, 0, 0, var14, var15);
         float var33 = method_1(var12, 1, 0, var14, var15);
         float var34 = var19 * В衣ш;
         float var35 = (var1 + var4) * В衣И + (var25 * var32 + var29 * var33) * var34;
         float var36 = (var2 + var5) * В衣ь + (var26 * var32 + var30 * var33) * var34;
         float var37 = (var3 + var6) * В衣ж + (var27 * var32 + var31 * var33) * var34;
         float var38 = (var9 + var10) * В衣弟;
         method_1(var0, var1, var2, var3, var35, var36, var37, var7 - 1, var8, var9, var38, var11, var12 * 2 + 7, var13, var14, var15);
         method_1(var0, var35, var36, var37, var4, var5, var6, var7 - 1, var8, var38, var10, var11, var12 * 2 + 9, var13, var14, var15);
         if (var13 < 2 && var7 >= 2 && method_1(var12, 2, 0, var14, var15) > В衣б) {
            float var39 = method_1(var12, 3, 0, var14, var15);
            float var40 = method_1(var12, 4, 0, var14, var15);
            float var41 = var20 * В衣Ы + var25 * var39 + var29 * var40;
            float var42 = var21 * В衣l + var26 * var39 + var30 * var40;
            float var43 = var22 * В衣8 + var27 * var39 + var31 * var40;
            float var44 = (float)Math.sqrt(var41 * var41 + var42 * var42 + var43 * var43);
            if (var44 < В衣5) {
               var44 = 1.0F;
            }

            float var45 = var19 * В衣в / var44;
            method_1(
               var0,
               var35,
               var36,
               var37,
               var35 + var41 * var45,
               var36 + var42 * var45,
               var37 + var43 * var45,
               Math.min(var7 - 1, 3),
               var8 * В衣ъ,
               var38 * В衣М,
               0.0F,
               var11,
               var12 * 3 + 13,
               var13 + 1,
               var14,
               var15
            );
         }
      } else {
         method_1(var0, var1, var2, var3, var4, var5, var6, д(var11, var9), д(var11, var10), var8);
      }
   }

   private static MeshData method_1(float var0, float var1, float var2, float var3, float var4, int var5, float var6, float var7, float var8) {
      BufferBuilder var9 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
      byte var10 = 3;
      byte var11 = 3;
      float var12 = еш * ВЬП;
      int var13 = (int)var12;
      float var14 = var12 - var13;
      float var15 = ВЬ西;
      float var16 = ВЬд;

      for (int var17 = 0; var17 < var10; var17++) {
         float var18 = еш * var15 + (float)var17 / var10;
         int var19 = (int)Math.floor(var18);
         float var20 = var18 - var19;
         float var21 = Mth.clamp(var20 / ВЬБ, 0.0F, 1.0F);
         float var22 = 1.0F - Mth.clamp((var20 - ВЬС) / ВЬз, 0.0F, 1.0F);
         float var23 = Mth.clamp(ВЬц + ВЬя * method_1(var17, 900, 0, var13, var14), 0.0F, 1.0F);
         float var24 = var21 * var22 * var23;
         if (!(var24 <= В衣с)) {
            float var25 = var6 * var24 * var8;
            float var26 = var3 * В衣В * (float)Math.sin(еш * В衣Я + var17 * В衣Х);
            float var27 = method_1(var17, 11, var19) * В衣е;
            float var28 = В衣А + В衣4 * Math.abs(method_1(var17, 12, var19));
            float var29 = method_1(var17, 13, var19) >= 0.0F ? 1.0F : В衣п;
            float var30 = var1 + var3 * (В衣Д + В衣Ъ * method_1(var17, 14, var19));
            float var31 = var3 * В衣т;
            float var32 = var4 * (1.0F + В衣а * method_1(var17, 15, var19));
            float var33 = 0.0F;
            float var34 = 0.0F;
            float var35 = 0.0F;

            for (int var36 = 0; var36 <= var11; var36++) {
               float var37 = (float)var36 / var11;
               float var38 = var27 + еш * В衣Ю + var29 * var28 * var16 * var37;
               float var39 = var0 + (float)Math.cos(var38) * var32;
               float var40 = var2 + (float)Math.sin(var38) * var32;
               float var41 = var30 - var31 * var37 + var26;
               if (var7 == В衣Ч && var36 > 0 && var36 < var11) {
                  TargetLighting.addLight(var39, var41, var40, В衣Л, д(var5, var25));
               }

               if (var36 > 0) {
                  int var42 = (var17 * 131 + var36) * 17 + var19 * 101 | 1;
                  method_1(var9, var33, var34, var35, var39, var41, var40, 4, var7, var25, var25, var5, var42, 0, var13, var14);
               }

               var33 = var39;
               var34 = var41;
               var35 = var40;
            }
         }
      }

      return var9.build();
   }

   private static GpuSampler method_1() {
      if (В_fe230870 == null) {
         В_fe230870 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
      }

      return В_fe230870;
   }

   public static void method_1(Camera var0, float var1, LivingEntity var2, float var3, int var4, boolean var5, String var6) {
      method_1(var0, var1, var2, var3, var4, var5, var6, false, ВЬы);
   }

   private static float method_1(int var0, int var1, int var2) {
      int var3 = var0 * В衣Ц + var1 * В衣ч + var2 * В衣Э;
      var3 = (var3 ^ var3 >>> 13) * В衣必;
      var3 ^= var3 >>> 16;
      return (var3 & В衣щ) / В衣Г - 1.0F;
   }

   public static void нш() {
      В_071c1034.endFrame();
      Я_071c1034.endFrame();
      Х_071c1034.endFrame();
      ни();
   }

   private static void method_1(CommandEncoder var0, GpuTextureView var1, TextureTarget var2, float var3, String var4) {
      GpuBufferSlice var5 = Я_071c1034.writeUniform(new LightningTargetEspRenderer.ScalarUniform(var3));

      try (RenderPass var6 = var0.createRenderPass(() -> var4, var2.getColorTextureView(), Optional.of(new Vector4f(0.0F)), null, OptionalDouble.empty())) {
         var6.setPipeline(сь);
         var6.setUniform(В衣ф, var5);
         var6.bindTexture(В衣и, var1, method_1());
         var6.setVertexBuffer(0, Ц.slice());
         var6.draw(4, 1, 0, 0);
      }
   }

   private static void ни() {
      for (int var0 = 0; var0 < Е.size(); var0++) {
         try {
            Е.get(var0).run();
         } catch (Throwable var2) {
         }
      }

      Е.clear();
   }

   private static void method_1(MeshData var0, RenderPipeline var1, Matrix4f var2, int var3, int var4, GpuTextureView var5, GpuTextureView var6, boolean var7) {
      if (var0 != null && var5 != null) {
         try (var0) {
            ByteBuffer var9 = var0.vertexBuffer();
            int var10 = var9.remaining();
            if (var10 <= 0) {
               return;
            }

            у(var10);
            CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
            var11.writeToBuffer(н.slice(0L, var10), var9);
            GpuBufferSlice var12 = В_071c1034.writeUniform(new LightningTargetEspRenderer.LightningUniform(var2, var3, var4));

            try (RenderPass var13 = var11.createRenderPass(
                  () -> В衣Й, var5, var7 ? Optional.of(new Vector4f(0.0F)) : Optional.empty(), var6, OptionalDouble.empty()
               )) {
               var13.setPipeline(var1);
               var13.setUniform(В衣л, var12);
               var13.setVertexBuffer(0, н.slice());
               DrawState var14 = var0.drawState();
               AutoStorageIndexBuffer var15 = RenderSystem.getSequentialBuffer(var14.primitiveTopology());
               var13.setIndexBuffer(var15.getBuffer(var14.indexCount()), var15.type());
               var13.drawIndexed(var14.indexCount(), 1, 0, 0, 0);
            }
         }
      }
   }

   private static void Ш(int var0, int var1) {
      int var2 = Math.max(1, var0 / 2);
      int var3 = Math.max(1, var1 / 2);
      if (б == null || б.width != var0 || б.height != var1) {
         method_1(б);
         б = new TextureTarget(В衣р, var0, var1, false, GpuFormat.RGBA8_UNORM);
      }

      if (Ы == null || Ы.width != var2 || Ы.height != var3) {
         method_1(Ы);
         Ы = new TextureTarget(В衣6, var2, var3, false, GpuFormat.RGBA8_UNORM);
      }

      if (l == null || l.width != var2 || l.height != var3) {
         method_1(l);
         l = new TextureTarget(В衣_, var2, var3, false, GpuFormat.RGBA8_UNORM);
      }
   }

   private static float method_1(float var0, double var1, Matrix4f var3, int var4) {
      float var5 = (float)Math.sqrt(var3.m01() * var3.m01() + var3.m11() * var3.m11() + var3.m21() * var3.m21());
      float var6 = var5 * var4 / (2.0F * (float)Math.max(var1, В衣7));
      return Mth.clamp(var0 * var6, 1.0F, В衣Р);
   }

   private static float method_1(int var0, int var1, int var2, int var3, float var4) {
      float var5 = method_1(var0, var1, var3 * 4 + var2);
      float var6 = method_1(var0, var1, (var3 + 1) * 4 + var2);
      float var7 = var4 * var4 * (В衣н - 2.0F * var4);
      return var5 + (var6 - var5) * var7;
   }

   private static void г(GpuBuffer var0) {
      if (var0 != null) {
         Е.add(() -> {
            if (!var0.isClosed()) {
               var0.close();
            }
         });
      }
   }

   private static boolean method_1(String var0, Vec3 var1, LivingEntity var2) {
      if (В衣9.equalsIgnoreCase(var0)) {
         return true;
      }

      if (!В衣о.equalsIgnoreCase(var0) && field_4.level != null) {
         AABB var3 = var2.getBoundingBox();
         Vec3 var4 = new Vec3(Mth.clamp(var1.x, var3.minX, var3.maxX), Mth.clamp(var1.y, var3.minY, var3.maxY), Mth.clamp(var1.z, var3.minZ, var3.maxZ));
         if (var4.distanceToSqr(var1) < В衣O) {
            return false;
         }

         BlockHitResult var5 = field_4.level.clip(new ClipContext(var1, var4, Block.OUTLINE, Fluid.NONE, field_4.player));
         return var5.getType() != Type.MISS;
      } else {
         return false;
      }
   }

   private static RenderPipeline н(String var0, CompareOp var1) {
      return RenderPipelinesAccessor.register(
         RenderPipelineBuilder.builder()
            .withVertexShader(ClientResourceIds.getCoreId(ВЬр))
            .withFragmentShader(ClientResourceIds.getCoreId(ВЬ6))
            .withUniform(ВЬ_, UniformType.UNIFORM_BUFFER)
            .withBlend(Я_515b3179)
            .withDepthTest(var1)
            .withDepthWrite(false)
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH, PrimitiveTopology.LINES)
            .withLocation(Identifier.fromNamespaceAndPath(ВЬШ, "pipeline/" + var0))
            .build()
      );
   }

   private record ScalarUniform(float value) implements DynamicUniform {
      @Override
      public void write(ByteBuffer var1) {
         var1.putFloat(this.value).putFloat(0.0F).putFloat(0.0F).putFloat(0.0F);
      }
   }

   private record LightningUniform(Matrix4f mvp, float screenWidth, float screenHeight) implements DynamicUniform {
      @Override
      public void write(ByteBuffer var1) {
         this.mvp.get(var1);
         var1.position(var1.position() + 64);
         var1.putFloat(1.0F).putFloat(1.0F).putFloat(1.0F).putFloat(1.0F);
         var1.putFloat(this.screenWidth).putFloat(this.screenHeight).putFloat(0.0F).putFloat(0.0F);
      }
   }
}
