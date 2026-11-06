package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.DeviceInfo;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.render.ClientResourceIds;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MotionBlurFrameInterpolator;

public class MotionBlur extends ClientModule {
   private static final Logger field_4 = LoggerFactory.getLogger(MotionBlur.class);
   private static final int Д4 = 336;
   private static final float ЪЬ = 0.24F;
   private final FloatSetting Хь;
   private final RenderPipeline сЕ;
   private final Matrix4f Ц;
   private final Matrix4f ч;
   private final Matrix4f Э;
   private final Matrix4f 必;
   private final Matrix4f щ;
   private final Matrix4f Г;
   @SerializedName("В")
   private final MotionBlurFrameInterpolator В_39af36a4;
   @SerializedName("е")
   private final GpuBuffer[] е_at_f51e284a;
   @SerializedName("В")
   private final GpuFence[] В_3ca269a0;
   private TextureTarget э;
   private int Дп;
   private int ДД;
   private int ДЪ;
   private boolean еь;
   private Vec3 З;
   private float Ъ衣;
   private float Ъш;
   @SerializedName("Я")
   private CameraType Я_at_f51e284a;
   private static final String сЫу = "MotionBlur";
   private static final String сЫ2 = "Физически основанный MotionBlur, очень сильно повышает плавность картинки.";
   private static final String сЫф = "Плавность";
   private static final float сЫи = 0.68F;
   private static final float сЫ9 = 0.01F;
   private static final String сЫо = "rose/motion_blur";
   private static final String сЫO = "hands_fullscreen";
   private static final String сЫ7 = "motion_blur";
   private static final String сЫР = "MotionBlur";
   private static final String сЫ3 = "SceneSampler";
   private static final String сЫр = "DepthSampler";
   private static final float сЫ6 = 1.0E-5F;
   private static final float сЫ_ = 1.0E-8F;
   private static final float сЫШ = 1.0E-8F;
   private static final float сЫы = 0.0062F;
   private static final float сЫй = -0.0074F;
   private static final float сЫЖ = 4.0E-5F;
   private static final String сЫм = "MotionBlur shader pipeline is invalid";
   private static final String сЫК = "MotionBlur output is unavailable";
   private static final long сЫI = 336L;
   private static final String сЫТ = "SceneSampler";
   private static final String сЫО = "DepthSampler";
   private static final String сЫФ = "MotionBlur";
   private static final String сЫЙ = "MotionBlur apply failed on {} / {} ({})";
   private static final float сЫк = 3.0F;
   private static final float сЫг = 0.38F;
   private static final float сЫ0 = 0.92F;
   private static final float сЫх = 1.1F;
   private static final float сЫУ = 2.25F;
   private static final float сЫЗ = 7.0F;
   private static final float сЫЕ = 24.0F;
   private static final float сЫП = 82.0F;
   private static final float сЫ西 = 0.3F;
   private static final float сЫд = 0.42F;
   private static final float сЫБ = 0.22F;
   private static final float сЫС = 1.12F;
   private static final float сЫз = 0.68F;
   private static final float сЫц = 2.85F;
   private static final float сЫя = 1.35F;
   private static final float сlс = 0.035F;
   private static final float сlВ = 0.075F;
   private static final float сlЯ = 0.00175F;
   private static final float сlХ = 0.00225F;
   private static final double сlе = 0.12;
   private static final double сlА = 0.045;
   private static final String сl4 = "MotionBlur output";
   private static final float сlп = -0.24F;
   private static final float сlД = 0.24F;
   private static final String сlЪ = "MotionBlur";
   private static final String сlт = "MotionBlur uniforms";

   private float к(Camera var1) {
      float var2 = 衣7(var1.yRot() - this.Ъ衣);
      float var3 = var1.xRot() - this.Ъш;
      double var4 = var1.position().distanceTo(this.З);
      float var6 = Math.abs(var2) * сlЯ + Math.abs(var3) * сlХ;
      float var7 = (float)Math.min(сlе, var4 * сlА);
      return var6 + var7;
   }

   @Override
   public void onEnable() {
      this.еь = false;
      this.О4();
   }

   private void О_() {
      TextureTarget var1 = this.э;
      this.э = null;
      if (var1 != null) {
         RenderSystem.queueFencedTask(var1::destroyBuffers);
      }
   }

   public void к(Matrix4fc var1, Matrix4fc var2) {
      if (!this.еь
         && this.isEnabled()
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.gui.screen() == null
         && var1 != null
         && var2 != null
         && var1.isFinite()
         && var2.isFinite()) {
         this.Ц.set(var1);
         this.ч.set(var2);
         Camera var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         RenderTarget var4 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (var4 != null && var3 != null) {
            GpuTexture var5 = var4.getColorTexture();
            GpuTextureView var6 = var4.getColorTextureView();
            GpuTexture var7 = var4.getDepthTexture();
            GpuTextureView var8 = var4.getDepthTextureView();
            if (var5 != null && !var5.isClosed() && var6 != null && !var6.isClosed() && var7 != null && !var7.isClosed() && var8 != null && !var8.isClosed()) {
               int var9 = var5.getWidth(0);
               int var10 = var5.getHeight(0);
               if (var9 > 1
                  && var10 > 1
                  && var4.width == var9
                  && var4.height == var10
                  && var7.getWidth(0) == var9
                  && var7.getHeight(0) == var10
                  && var5.getFormat() == GpuFormat.RGBA8_UNORM) {
                  MotionBlur.BlurSettings var11 = this.к();
                  if (var11.strength <= сЫ6) {
                     this.О4();
                  } else {
                     if (this.ДД != var9 || this.ДЪ != var10) {
                        this.О4();
                        this.ДД = var9;
                        this.ДЪ = var10;
                     }

                     if (!(Math.abs(this.Ц.determinant()) <= сЫ_) && !(Math.abs(this.ч.determinant()) <= сЫШ)) {
                        if (this.Я_at_f51e284a != MINECRAFT_CLIENT.options.getCameraType()) {
                           this.О4();
                        }

                        this.Я_at_f51e284a = MINECRAFT_CLIENT.options.getCameraType();
                        Vec3 var12 = var3.position();
                        if (this.В_39af36a4.Т(System.nanoTime(), this.Ц, this.ч, var12.x, var12.y, var12.z, var3.yRot(), var3.xRot())) {
                           this.Э.set(this.В_39af36a4.Т_72a92728());
                           this.必.set(this.В_39af36a4.Х());
                           this.З = new Vec3(this.В_39af36a4.Т_afb6a793().x(), this.В_39af36a4.Т_afb6a793().y(), this.В_39af36a4.Т_afb6a793().z());
                           this.Ъ衣 = this.В_39af36a4.ХЕ();
                           this.Ъш = this.В_39af36a4.Хл();
                           float var13 = this.к(var3);
                           float var14 = 衣Х(衣7(var3.yRot() - this.Ъ衣) * сЫы);
                           float var15 = 衣Х((var3.xRot() - this.Ъш) * сЫй);
                           if (!(var13 < var11.activation * сЫЖ)) {
                              this.щ.set(this.Ц).invert();
                              this.Г.set(this.ч).invert();
                              if (this.щ.isFinite() && this.Г.isFinite() && this.Э.isFinite() && this.必.isFinite()) {
                                 Vec3 var16 = var12.subtract(this.З);

                                 try {
                                    try (MemoryStack var17 = MemoryStack.stackPush()) {
                                       int var33 = this.кт();
                                       if (var33 < 0) {
                                          return;
                                       }

                                       if (!RenderSystem.getDevice().precompilePipeline(this.сЕ).isValid()) {
                                          throw new IllegalStateException(сЫм);
                                       }

                                       TextureTarget var19 = this.ф(var9, var10);
                                       GpuTexture var20 = var19.getColorTexture();
                                       GpuTextureView var21 = var19.getColorTextureView();
                                       if (var20 == null || var20.isClosed() || var21 == null || var21.isClosed()) {
                                          throw new IllegalStateException(сЫК);
                                       }

                                       CommandEncoder var22 = RenderSystem.getDevice().createCommandEncoder();
                                       ByteBuffer var23 = Std140Builder.onStack(var17, 336)
                                          .putMat4f(this.щ)
                                          .putMat4f(this.Г)
                                          .putMat4f(this.Э)
                                          .putMat4f(this.必)
                                          .putVec4((float)var16.x, (float)var16.y, (float)var16.z, var11.strength)
                                          .putVec4(var11.temporalScale, 0.0F, 0.0F, 0.0F)
                                          .putVec4(var11.maxRadius, var11.edgeFocus, var11.chromatic, var11.depthGuard)
                                          .putVec4(var11.decay, var11.activation, var13, var11.samples)
                                          .putVec4(var14, var15, var9, var10)
                                          .get();
                                       GpuBuffer var24 = this.е_at_f51e284a[var33];
                                       if (var24 == null || var24.isClosed()) {
                                          var24 = RenderSystem.getDevice().createBuffer(() -> сlт, 136, сЫI);
                                          this.е_at_f51e284a[var33] = var24;
                                       }

                                       var22.writeToBuffer(var24.slice(), var23);

                                       try (RenderPass var25 = var22.createRenderPass(() -> сlЪ, var21, Optional.empty())) {
                                          var25.setPipeline(this.сЕ);
                                          var25.bindTexture(сЫТ, var6, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                                          var25.bindTexture(сЫО, var8, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                                          var25.setUniform(сЫФ, var24);
                                          var25.draw(3, 1, 0, 0);
                                       }

                                       var22.copyTextureToTexture(var20, var5, 0, 0, 0, 0, 0, var9, var10);
                                       this.В_3ca269a0[var33] = var22.createFence();
                                       this.Дп = (var33 + 1) % this.е_at_f51e284a.length;
                                    }
                                 } catch (Throwable var32) {
                                    if (!this.еь) {
                                       DeviceInfo var18 = RenderSystem.getDevice().getDeviceInfo();
                                       field_4.error(сЫЙ, var18.name(), var18.backendName(), var18.driverInfo(), var32);
                                       this.еь = true;
                                    }

                                    this.О4();
                                 }
                              } else {
                                 this.О4();
                              }
                           }
                        }
                     } else {
                        this.О4();
                     }
                  }
               } else {
                  this.О4();
               }
            } else {
               this.О4();
            }
         } else {
            this.О4();
         }
      } else {
         this.О4();
      }
   }

   @Override
   public void onDisable() {
      this.Ои();
   }

   private static float 衣7(float var0) {
      return Mth.wrapDegrees(var0);
   }

   private int кт() {
      int var1 = 0;

      int var2;
      while (true) {
         if (var1 >= this.е_at_f51e284a.length) {
            return -1;
         }

         var2 = (this.Дп + var1) % this.е_at_f51e284a.length;
         GpuFence var3 = this.В_3ca269a0[var2];
         if (var3 == null) {
            break;
         }

         if (var3.awaitCompletion(0L)) {
            var3.close();
            this.В_3ca269a0[var2] = null;
            break;
         }

         var1++;
      }

      return var2;
   }

   public void Ои() {
      if (!RenderSystem.isOnRenderThread()) {
         MINECRAFT_CLIENT.execute(this::Ои);
      } else {
         this.О4();
         this.еь = false;
         this.Дп = 0;

         for (int var1 = 0; var1 < this.е_at_f51e284a.length; var1++) {
            if (this.В_3ca269a0[var1] != null) {
               this.В_3ca269a0[var1].close();
               this.В_3ca269a0[var1] = null;
            }

            GpuBuffer var2 = this.е_at_f51e284a[var1];
            this.е_at_f51e284a[var1] = null;
            if (var2 != null && !var2.isClosed()) {
               RenderSystem.queueFencedTask(var2::close);
            }
         }

         this.О_();
      }
   }

   public MotionBlur() {
      super(сЫу, сЫ2, ModuleCategory.RENDER);
      this.Хь = new FloatSetting(сЫф, сЫи, 0.0F, 1.0F).withStep(сЫ9);
      this.сЕ = RenderPipelineBuilder.builder()
         .withLocation(сЫо)
         .withVertexShader(ClientResourceIds.getCoreId(сЫO))
         .withFragmentShader(ClientResourceIds.getCoreId(сЫ7))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(сЫР, UniformType.UNIFORM_BUFFER)
         .withSampler(сЫ3)
         .withSampler(сЫр)
         .build();
      this.Ц = new Matrix4f();
      this.ч = new Matrix4f();
      this.Э = new Matrix4f();
      this.必 = new Matrix4f();
      this.щ = new Matrix4f();
      this.Г = new Matrix4f();
      this.В_39af36a4 = new MotionBlurFrameInterpolator();
      this.е_at_f51e284a = new GpuBuffer[3];
      this.В_3ca269a0 = new GpuFence[this.е_at_f51e284a.length];
      this.З = Vec3.ZERO;
      this.addSettings(this.Хь);
   }

   private TextureTarget ф(int var1, int var2) {
      if (this.э != null && (this.э.width != var1 || this.э.height != var2)) {
         this.О_();
      }

      if (this.э == null) {
         this.э = new TextureTarget(сl4, var1, var2, false, GpuFormat.RGBA8_UNORM);
      }

      return this.э;
   }

   private MotionBlur.BlurSettings к() {
      float var1 = Math.clamp(this.Хь.getValue(), 0.0F, 1.0F);
      float var2 = var1 * var1 * (сЫк - 2.0F * var1);
      return new MotionBlur.BlurSettings(
         сЫг + var2 * сЫ0,
         сЫх + var2 * сЫУ,
         5 + Math.round(var2 * сЫЗ),
         сЫЕ + var2 * сЫП,
         сЫ西 + var2 * сЫд,
         сЫБ + var2 * сЫС,
         сЫз,
         сЫц - var2 * сЫя,
         сlс + (1.0F - var2) * сlВ
      );
   }

   private static float 衣Х(float var0) {
      return !Float.isFinite(var0) ? 0.0F : Math.clamp(var0, сlп, сlД);
   }

   private void О4() {
      this.В_39af36a4.ВШ();
      this.Я_at_f51e284a = null;
      this.З = Vec3.ZERO;
      this.Ъ衣 = 0.0F;
      this.Ъш = 0.0F;
      this.Э.identity();
      this.必.identity();
   }

   private record BlurSettings(
      float strength, float temporalScale, int samples, float maxRadius, float edgeFocus, float chromatic, float depthGuard, float decay, float activation
   ) {
   }
}
