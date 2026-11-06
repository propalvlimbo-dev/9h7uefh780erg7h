package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.impl.render.Ambience;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public final class Rain extends ClientModule {
   private static final int пж = 32;
   private static final int п弟 = 3;
   private static final int пб = 1408;
   private static final int пЫ = 42;
   private static final int пl = 6;
   private static final int п8 = 48;
   private static final double Яб = 48.0;
   private static final float ДИ = 0.55F;
   private static final float Дь = 0.68F;
   private final @com.google.gson.annotations.SerializedName("ХА")  FloatSetting reflectionStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Х4")  FloatSetting distortionMotionSetting;
   private final @com.google.gson.annotations.SerializedName("Хп")  FloatSetting reflectionSoftnessSetting;
   private final @com.google.gson.annotations.SerializedName("ХД")  FloatSetting wetSurfaceDarkeningSetting;
   private final @com.google.gson.annotations.SerializedName("ХЪ")  FloatSetting rainwaterActivitySetting;
   private final @com.google.gson.annotations.SerializedName("Вэ")  BooleanSetting footstepSplashesSetting;
   private final @com.google.gson.annotations.SerializedName("Хт")  FloatSetting splashStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Ха")  FloatSetting waveLifetimeSetting;
   private final @com.google.gson.annotations.SerializedName("Вю")  BooleanSetting onlyDuringRainSetting;
   private final @com.google.gson.annotations.SerializedName("сУ")  RenderPipeline rainEffectPipeline;
   @SerializedName("В")
   private final TextureTarget[] reflectionTargets;
   @SerializedName("В")
   private final ProjectionMatrixBuffer[] reflectionProjectionBuffers;
   @SerializedName("В")
   private final Matrix4f[] reflectionTextureMatrices;
   private final @com.google.gson.annotations.SerializedName("ъ")  Matrix4f inverseProjectionScratch;
   private final @com.google.gson.annotations.SerializedName("М")  Matrix4f inverseViewScratch;
   private final @com.google.gson.annotations.SerializedName("Э")  float[] reflectionPlaneHeights;
   private @com.google.gson.annotations.SerializedName("л")  TextureTarget sceneCopyTarget;
   @SerializedName("2")
   private GpuBuffer rainUniformBuffer;
   private @com.google.gson.annotations.SerializedName("l")  ClientLevel reflectionCacheLevel;
   private @com.google.gson.annotations.SerializedName("п5")  int reflectionCenterBlockX;
   private @com.google.gson.annotations.SerializedName("пв")  int reflectionCenterBlockZ;
   private @com.google.gson.annotations.SerializedName("пъ")  int reflectionPlaneCount;
   private @com.google.gson.annotations.SerializedName("ХШ")  boolean reflectionCacheInitialized;
   private @com.google.gson.annotations.SerializedName("Хы")  boolean hasReflectionPlanes;
   private @com.google.gson.annotations.SerializedName("Хй")  boolean reflectionCaptureReady;
   @SerializedName("г")
   private Vec3 lastReflectionCameraPosition;
   private @com.google.gson.annotations.SerializedName("ХЖ")  boolean cleanupPendingAfterCapture;
   private static boolean capturingReflection;
   private static RenderTarget currentReflectionTarget;
   private static Matrix4f Н;
   private static Matrix4f н;
   private final @com.google.gson.annotations.SerializedName("с9")  List<Rain.FootstepSplash> activeFootstepSplashes;
   private final @com.google.gson.annotations.SerializedName("со")  List<Rain.FootstepSplash> visibleFootstepSplashes;
   private final @com.google.gson.annotations.SerializedName("必")  float[] visibleSplashAlphas;
   private final @com.google.gson.annotations.SerializedName("ЯЖ")  EventListener<PlayerTickEvent> playerTickListener;
   @SerializedName("8")
   private ClientLevel splashLevel;
   @SerializedName("0")
   private Vec3 lastFootstepPosition;
   private @com.google.gson.annotations.SerializedName("ЯЫ")  double footstepDistanceAccumulator;
   private @com.google.gson.annotations.SerializedName("ХК")  boolean alternateFootstepSide;
   private @com.google.gson.annotations.SerializedName("пМ")  int footstepSplashSequence;
   private @com.google.gson.annotations.SerializedName("пН")  int ambientSplashSequence;
   private @com.google.gson.annotations.SerializedName("пн")  int ambientSplashTickCounter;
   private static final String ХДЕ = "Rain";
   private static final String ХДП = "Стабильные влажные отражения на поверхности";
   private static final String ХД西 = "Сила отражений";
   private static final float ХДд = 0.32F;
   private static final float ХДБ = 0.02F;
   private static final float ХДС = 0.01F;
   private static final String ХДз = "Искажение и движение";
   private static final float ХДц = 0.4F;
   private static final float ХДя = 2.5F;
   private static final float ХЪс = 0.05F;
   private static final String ХЪВ = "Мягкость отражений";
   private static final float ХЪЯ = 0.72F;
   private static final float ХЪХ = 0.2F;
   private static final float ХЪе = 0.02F;
   private static final String ХЪА = "Затемнение мокрой поверхности";
   private static final float ХЪ4 = 0.1F;
   private static final float ХЪп = 0.25F;
   private static final float ХЪД = 0.01F;
   private static final String ХЪЪ = "Активность дождевой воды";
   private static final float ХЪт = 1.35F;
   private static final float ХЪа = 3.0F;
   private static final float ХЪЮ = 0.05F;
   private static final String ХЪЧ = "Брызги при ходьбе";
   private static final String ХЪЛ = "Сила брызг";
   private static final float ХЪ诶 = 0.2F;
   private static final float ХЪ1 = 0.05F;
   private static final String ХЪЬ = "Время жизни волн (сек)";
   private static final float ХЪ衣 = 1.8F;
   private static final float ХЪш = 0.6F;
   private static final float ХЪИ = 4.0F;
   private static final float ХЪь = 0.1F;
   private static final String ХЪж = "Только во время дождя";
   private static final String ХЪ弟 = "rose/rain_effect";
   private static final String ХЪб = "rain_effect";
   private static final String ХЪЫ = "rain_effect";
   private static final String ХЪl = "RainEffectData";
   private static final String ХЪ8 = "SceneSampler";
   private static final String ХЪ5 = "MainDepthSampler";
   private static final String ХЪв = "ReflectionSampler0";
   private static final String ХЪъ = "ReflectionSampler1";
   private static final String ХЪМ = "ReflectionSampler2";
   private static final float ХЪН = 0.01F;
   private static final String ХЪн = "capture setup";
   private static final float ХЪЦ = -1.0F;
   private static final float ХЪч = -1.0F;
   private static final float ХЪЭ = 0.002F;
   private static final float ХЪ必 = 0.002F;
   private static final String ХЪщ = "reflection capture";
   private static final long ХЪГ = 240000L;
   private static final float ХЪл = 20.0F;
   private static final String ХЪэ = "SceneSampler";
   private static final String ХЪю = "MainDepthSampler";
   private static final String ХЪЩ = "RainEffectData";
   private static final String ХЪу = "reflection composite";
   private static final long ХЪ2 = 1408L;
   private static final double ХЪф = 16.0;
   private static final float ХЪи = 0.08F;
   private static final double ХЪ9 = 0.008;
   private static final double ХЪо = 0.54;
   private static final double ХЪO = 0.44;
   private static final double ХЪ7 = 0.5;
   private static final double ХЪР = 0.22;
   private static final double ХЪ3 = -1.0;
   private static final double ХЪр = 0.145;
   private static final double ХЪ6 = 0.045;
   private static final double ХЪ_ = 0.025;
   private static final double ХЪШ = 0.045;
   private static final float ХЪы = 1.22F;
   private static final float ХЪй = 0.86F;
   private static final float ХЪЖ = 0.28F;
   private static final float ХЪм = 1000.0F;
   private static final float ХЪК = 0.88F;
   private static final float ХЪI = 0.24F;
   private static final double ХЪТ = 0.003;
   private static final float ХЪО = 4.0F;
   private static final float ХЪФ = 4.0F;
   private static final double ХЪЙ = Math.PI;
   private static final double ХЪк = 2.0;
   private static final double ХЪг = 0.025;
   private static final double ХЪ0 = 0.055;
   private static final float ХЪх = 1.8F;
   private static final double ХЪУ = 0.012;
   private static final double ХЪЗ = 0.018;
   private static final float ХЪЕ = 1.5F;
   private static final double ХЪП = 0.055;
   private static final double ХЪ西 = 0.105;
   private static final float ХЪд = 0.75F;
   private static final float ХЪБ = 0.35F;
   private static final float ХЪС = 1.45F;
   private static final double ХЪз = 0.025;
   private static final double ХЪц = 0.11;
   private static final double ХЪя = 0.025;
   private static final float Хтс = 0.08F;
   private static final float ХтВ = 0.01F;
   private static final float ХтЯ = 1.8F;
   private static final float ХтХ = 1.25F;
   private static final double Хте = Math.PI;
   private static final double ХтА = 2.0;
   private static final double Хт4 = 2.0;
   private static final double Хтп = 11.0;
   private static final double ХтД = 14.0;
   private static final double ХтЪ = 0.025;
   private static final double Хтт = 0.08;
   private static final double Хта = Math.PI;
   private static final double ХтЮ = 2.0;
   private static final double ХтЧ = 0.006;
   private static final double ХтЛ = 0.022;
   private static final float Хт诶 = 2.4F;
   private static final double Хт1 = 0.055;
   private static final double ХтЬ = 0.075;
   private static final double Хт衣 = 0.25;
   private static final double Хтш = 0.015;
   private static final double ХтИ = 0.015;
   private static final double Хть = 0.015;
   private static final double Хтж = 0.015;
   private static final double Хт弟 = 0.28;
   private static final double Хтб = 0.72;
   private static final double ХтЫ = 9216.0;
   private static final int Хтl = 73244475;
   private static final int Хт8 = 73244475;
   private static final int Хт5 = 16777215;
   private static final float Хтв = 1.6777215E7F;
   private static final double Хтъ = 2304.0;
   private static final long ХтМ = 2304L;
   private static final float ХтН = 16.0F;
   private static final int Хтн = Integer.MIN_VALUE;
   private static final int ХтЦ = Integer.MIN_VALUE;
   private static final float Хтч = 16.0F;
   private static final float ХтЭ = 16.0F;
   private static final double Хт必 = 0.999;
   private static final double Хтщ = 0.001;
   private static final double ХтГ = 16.0;
   private static final double Хтл = 16.0;
   private static final double Хтэ = 0.45;
   private static final float Хтю = 0.55F;
   private static final float ХтЩ = 0.55F;
   private static final String Хту = "Rain scene backup";
   private static final float Хт2 = 1.0E-6F;
   private static final float Хтф = -1.0F;
   private static final float Хти = -1.0F;
   private static final float Хт9 = -1.0F;
   private static final float Хто = 1.0E-6F;
   private static final float ХтO = 0.68F;
   private static final float Хт7 = 0.68F;
   private static final String ХтР = "resource release";
   private static final double Хт3 = 16.0;
   private static final String Хтр = "Rain effect uniforms";
   private static final String Хт6 = "Rain effect composite";

   private static double method_8(Entry<Integer, Integer> var0, double var1) {
      double var3 = Math.abs(((Integer)var0.getKey()).intValue() / Хтл - var1);
      return ((Integer)var0.getValue()).intValue() * (1.0 + Хтэ / (1.0 + var3));
   }

   public static Matrix4f П_20497804() {
      return н;
   }

   private static Throwable method_8(Throwable var0, Throwable var1) {
      if (var0 == null) {
         return var1;
      }

      if (method_8(var1) && !method_8(var0)) {
         var1.addSuppressed(var0);
         return var1;
      }

      if (var0 != var1) {
         var0.addSuppressed(var1);
      }

      return var0;
   }

   private static void method_0(RenderTarget var0, RenderTarget var1) {
      if (var0.getDepthTexture() != null && var1.getDepthTexture() != null) {
         var1.copyDepthFrom(var0);
      }
   }

   private static Entry<Integer, Integer> method_8(List<Entry<Integer, Integer>> var0, int var1) {
      for (Entry var3 : var0) {
         if ((Integer)var3.getKey() == var1) {
            return var3;
         }
      }

      return null;
   }

   private static void method_3(TextureTarget var0) {
      RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(var0.getColorTexture(), new Vector4f(0.0F), var0.getDepthTexture(), 0.0);
   }

   public static Matrix4f Р_20497804() {
      return Н;
   }

   private void method_0(Vec3 var1, float var2) {
      float var3 = this.rainwaterActivitySetting.getValue();
      if (!(var2 <= Хтс) && !(var3 <= ХтВ)) {
         this.ambientSplashTickCounter++;
         int var4 = var3 >= ХтЯ ? 1 : 2;
         if (this.ambientSplashTickCounter % var4 == 0) {
            int var5 = Math.clamp(1 + Mth.floor(var3 * var2 * ХтХ), 1, 5);

            for (int var6 = 0; var6 < var5; var6++) {
               int var7 = ++this.ambientSplashSequence;
               float var8 = Х(var7 * 89 + 7);
               float var9 = Х(var7 * 131 + 19);
               double var10 = var8 * Хте * ХтА;
               double var12 = Хт4 + Math.sqrt(var9) * Хтп;
               double var14 = var1.x + Math.cos(var10) * var12;
               double var16 = var1.z + Math.sin(var10) * var12;
               int var18 = MINECRAFT_CLIENT.level.getHeight(Types.WORLD_SURFACE, Mth.floor(var14), Mth.floor(var16));
               Rain.SurfaceHeight var19 = this.method_8(var14, var16, var18);
               if (var19 != null && !(Math.abs(var19.y() - var1.y) > ХтД)) {
                  double var20 = var19.y() + ХтЪ;
                  if (MINECRAFT_CLIENT.level.canSeeSky(BlockPos.containing(var14, var20 + Хтт, var16))) {
                     float var22 = Х(var7 * 173 + 31);
                     float var23 = Х(var7 * 211 + 43);
                     double var24 = var22 * Хта * ХтЮ;
                     double var26 = (ХтЧ + var23 * ХтЛ) * Math.min(var3, Хт诶);
                     double var28 = Math.cos(var24) * var26;
                     double var30 = Math.sin(var24) * var26;
                     double var32 = Хт1 + Х(var7 * 251 + 59) * ХтЬ * Math.min(var3, 2.0F);
                     Particle var34 = MINECRAFT_CLIENT.particleEngine.createParticle(ParticleTypes.SPLASH, var14, var20, var16, var28, 0.0, var30);
                     if (var34 != null) {
                        var34.setParticleSpeed(var28, var32, var30);
                     }
                  }
               }
            }
         }
      } else {
         this.ambientSplashTickCounter = 0;
      }
   }

   public boolean Пу() {
      return this.isEnabled() && !capturingReflection && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameRenderer != null && (!this.onlyDuringRainSetting.getValue() || this.method_3л() > ХЪН || this.П7());
   }

   public void method_8(Camera var1, Matrix4f var2, Matrix4f var3) {
      if (this.Пу() && this.reflectionCaptureReady && var1 != null && var2 != null && var3 != null) {
         RenderTarget var4 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (var4 != null
            && var4.getColorTexture() != null
            && var4.getDepthTexture() != null
            && var4.getColorTextureView() != null
            && var4.getDepthTextureView() != null) {
            Throwable var5 = null;

            try {
               this.弟(var4.width, var4.height);
               if (!this.reflectionCaptureReady) {
                  return;
               }

               method_3(var4, this.sceneCopyTarget);
               Matrix4f var6 = this.inverseProjectionScratch.set(var3).invert();
               Vec3 var7 = var1.position();
               Matrix4f var8 = this.inverseViewScratch.set(var2).translate((float)(-var7.x), (float)(-var7.y), (float)(-var7.z)).invert();

               try (MemoryStack var9 = MemoryStack.stackPush()) {
                  Std140Builder var10 = Std140Builder.onStack(var9, 1408).putMat4f(var6).putMat4f(var8);

                  for (Matrix4f var14 : this.reflectionTextureMatrices) {
                     var10.putMat4f(var14);
                  }

                  float var22 = ((float)(MINECRAFT_CLIENT.level.getGameTime() % ХЪГ) + MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / ХЪл;
                  var10.putVec4(this.reflectionPlaneHeights[0], this.reflectionPlaneHeights[1], this.reflectionPlaneHeights[2], this.reflectionPlaneCount)
                     .putVec4(this.reflectionStrengthSetting.getValue(), this.reflectionSoftnessSetting.getValue(), this.wetSurfaceDarkeningSetting.getValue(), this.method_3л())
                     .putVec4(this.distortionMotionSetting.getValue(), var22, this.rainwaterActivitySetting.getValue(), 0.0F);
                  this.method_8(var10, var7);
                  ByteBuffer var23 = var10.get();
                  CommandEncoder var24 = RenderSystem.getDevice().createCommandEncoder();
                  this.rainUniformBuffer = this.method_0();
                  var24.writeToBuffer(this.rainUniformBuffer.slice(), var23);

                  try (RenderPass var25 = var24.createRenderPass(() -> Хт6, var4.getColorTextureView(), Optional.empty())) {
                     var25.setPipeline(this.rainEffectPipeline);
                     var25.bindTexture(ХЪэ, this.sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                     var25.bindTexture(ХЪю, var4.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));

                     for (int var15 = 0; var15 < 3; var15++) {
                        var25.bindTexture(
                           "ReflectionSampler" + var15,
                           this.reflectionTargets[var15].getColorTextureView(),
                           RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
                        );
                     }

                     var25.setUniform(ХЪЩ, this.rainUniformBuffer);
                     var25.draw(3, 1, 0, 0);
                  }
               }
            } catch (Throwable var21) {
               var5 = method_8(var5, var21);
            }

            method_8(ХЪу, var5);
         }
      }
   }

   public static RenderTarget method_8_68bfbb94() {
      return currentReflectionTarget;
   }

   private static Throwable method_8(Throwable var0, Runnable var1) {
      try {
         var1.run();
         return var0;
      } catch (Throwable var3) {
         return method_8(var0, var3);
      }
   }

   private void Йа() {
      this.reflectionCacheLevel = null;
      this.reflectionCenterBlockX = 0;
      this.reflectionCenterBlockZ = 0;
      this.reflectionPlaneCount = 0;
      this.reflectionCacheInitialized = false;
      this.hasReflectionPlanes = false;
      this.reflectionCaptureReady = false;

      for (int var1 = 0; var1 < 3; var1++) {
         this.reflectionPlaneHeights[var1] = Float.NaN;
         this.reflectionTextureMatrices[var1].identity();
      }
   }

   private void Йк() {
      capturingReflection = false;
      currentReflectionTarget = null;
      Н = null;
      н = null;
      this.cleanupPendingAfterCapture = false;
      TextureTarget var1 = this.sceneCopyTarget;
      this.sceneCopyTarget = null;
      GpuBuffer var2 = this.rainUniformBuffer;
      this.rainUniformBuffer = null;
      TextureTarget[] var3 = (TextureTarget[])this.reflectionTargets.clone();
      ProjectionMatrixBuffer[] var4 = (ProjectionMatrixBuffer[])this.reflectionProjectionBuffers.clone();

      for (int var5 = 0; var5 < 3; var5++) {
         this.reflectionTargets[var5] = null;
      }

      for (int var15 = 0; var15 < 3; var15++) {
         this.reflectionProjectionBuffers[var15] = null;
      }

      this.reflectionCacheLevel = null;
      this.lastReflectionCameraPosition = null;
      this.reflectionPlaneCount = 0;
      this.reflectionCacheInitialized = false;
      this.hasReflectionPlanes = false;
      this.reflectionCaptureReady = false;
      Throwable var16 = null;
      if (var2 != null && !var2.isClosed()) {
         try {
            var2.close();
         } catch (Throwable var14) {
            var16 = method_8(var16, var14);
         }
      }

      if (var1 != null) {
         try {
            var1.destroyBuffers();
         } catch (Throwable var13) {
            var16 = method_8(var16, var13);
         }
      }

      for (TextureTarget var9 : var3) {
         if (var9 != null) {
            try {
               var9.destroyBuffers();
            } catch (Throwable var12) {
               var16 = method_8(var16, var12);
            }
         }
      }

      for (ProjectionMatrixBuffer var20 : var4) {
         if (var20 != null) {
            try {
               var20.close();
            } catch (Throwable var11) {
               var16 = method_8(var16, var11);
            }
         }
      }

      method_8(ХтР, var16);
   }

   private float method_3л() {
      if (!this.onlyDuringRainSetting.getValue()) {
         return 1.0F;
      }

      float var1 = MINECRAFT_CLIENT.level == null ? 0.0F : MINECRAFT_CLIENT.level.getRainLevel(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false));
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Ambience var2 = RoseClient.getInstance().getServices().getModules().getAmbience();
         if (var2 != null) {
            if (var2.isRainOverrideEnabled()) {
               return 1.0F;
            }

            if (var2.isWeatherOverrideEnabled()) {
               return 0.0F;
            }
         }
      }

      return Math.clamp(var1, 0.0F, 1.0F);
   }

   private void method_8(Camera var1) {
      Vec3 var2 = var1.position();
      boolean var3 = this.reflectionCacheLevel != null && this.reflectionCacheLevel != MINECRAFT_CLIENT.level;
      boolean var4 = this.lastReflectionCameraPosition != null && this.lastReflectionCameraPosition.distanceToSqr(var2) > Хтъ;
      this.lastReflectionCameraPosition = var2;
      if (var3 || var4) {
         this.Йа();
      }

      boolean var5 = this.reflectionCacheLevel == MINECRAFT_CLIENT.level;
      long var6 = (long)Math.floor(var2.x) - this.reflectionCenterBlockX;
      long var8 = (long)Math.floor(var2.z) - this.reflectionCenterBlockZ;
      boolean var10 = var6 * var6 + var8 * var8 > ХтМ;
      if (!var5 || !this.reflectionCacheInitialized || var10) {
         int[] var11 = new int[3];

         for (int var12 = 0; var12 < 3; var12++) {
            var11[var12] = var5 && Float.isFinite(this.reflectionPlaneHeights[var12]) ? Math.round(this.reflectionPlaneHeights[var12] * ХтН) : Хтн;
         }

         this.reflectionCacheLevel = MINECRAFT_CLIENT.level;
         this.reflectionCenterBlockX = (int)Math.floor(var2.x);
         this.reflectionCenterBlockZ = (int)Math.floor(var2.z);
         this.hasReflectionPlanes = false;
         this.reflectionCaptureReady = false;
         Map<Integer, Integer> var21 = this.д_ea411899();
         if (var21.isEmpty()) {
            this.reflectionPlaneCount = 0;
            this.reflectionCacheInitialized = false;
         } else {
            this.reflectionCacheInitialized = true;
            ArrayList<Entry<Integer, Integer>> var13 = new ArrayList<>(var21.entrySet());
            ArrayList<Entry<Integer, Integer>> var14 = new ArrayList<>(3);

            for (int var15 = 0; var15 < 3; var15++) {
               var14.add(null);
            }

            HashSet var22 = new HashSet();
            double var16 = MINECRAFT_CLIENT.player == null ? var2.y : MINECRAFT_CLIENT.player.getY();

            for (int var18 = 0; var18 < 3; var18++) {
               int var19 = var11[var18];
               if (var19 != ХтЦ) {
                  Entry var20 = method_8(var13, var19);
                  if (var20 != null && var22.add(var19)) {
                     var14.set(var18, var20);
                  }
               }
            }

            if (var14.get(0) == null) {
               Entry var23 = var13.stream()
                  .filter(var1x -> !var22.contains(var1x.getKey()))
                  .min(Comparator.comparingDouble(var2x -> Math.abs(((Integer)var2x.getKey()).intValue() / Хт3 - var16)))
                  .orElse(null);
               if (var23 != null) {
                  var14.set(0, var23);
                  var22.add((Integer)var23.getKey());
               }
            }

            for (int var24 = 0; var24 < 3; var24++) {
               if (var14.get(var24) == null) {
                  Entry var26 = var13.stream()
                     .filter(var1x -> !var22.contains(var1x.getKey()))
                     .max(Comparator.comparingDouble(var2x -> method_8((Entry<Integer, Integer>)var2x, var16)))
                     .orElse(null);
                  if (var26 == null) {
                     break;
                  }

                  var14.set(var24, var26);
                  var22.add((Integer)var26.getKey());
               }
            }

            List<Entry<Integer, Integer>> var25 = var14.stream().filter(var0 -> var0 != null).toList();
            this.reflectionPlaneCount = Math.min(3, var25.size());
            float var27 = ((Integer)((Entry)var25.getFirst()).getKey()).intValue() / Хтч;

            for (int var28 = 0; var28 < 3; var28++) {
               this.reflectionPlaneHeights[var28] = var28 < this.reflectionPlaneCount ? ((Integer)((Entry)var25.get(var28)).getKey()).intValue() / ХтЭ : var27;
            }

            this.hasReflectionPlanes = this.reflectionPlaneCount > 0;
         }
      }
   }

   private static void method_8(String var0, Throwable var1) {
      if (var1 != null) {
         if (var1 instanceof VirtualMachineError var3) {
            throw var3;
         } else if (var1 instanceof LinkageError var2) {
            throw var2;
         } else {
            System.err.println("[Rain] " + var0 + " failed: " + var1.getMessage());
            var1.printStackTrace();
         }
      }
   }

   private void method_8(double var1, double var3, double var5, double var7, double var9, float var11, float var12, boolean var13) {
      if (MINECRAFT_CLIENT.particleEngine != null) {
         int var14 = Math.clamp(Math.round(ХЪО + var12 * ХЪФ + (var13 ? 2.0F : 0.0F)), 4, 14);

         for (int var15 = 0; var15 < var14; var15++) {
            float var16 = Х(this.footstepSplashSequence * 193 + var15 * 47 + 3);
            float var17 = Х(this.footstepSplashSequence * 157 + var15 * 29 + 11);
            float var18 = Х(this.footstepSplashSequence * 101 + var15 * 61 + 23);
            double var19 = var16 * ХЪЙ * ХЪк;
            double var21 = Math.cos(var19);
            double var23 = Math.sin(var19);
            double var25 = (ХЪг + var17 * ХЪ0) * Math.min(var12, ХЪх);
            double var27 = (ХЪУ + var11 * ХЪЗ) * Math.min(var12, ХЪЕ);
            double var29 = var21 * var25 + var7 * var27;
            double var31 = var23 * var25 + var9 * var27;
            double var33 = (ХЪП + var18 * ХЪ西) * Math.min(ХЪд + var12 * ХЪБ, ХЪС);
            double var35 = ХЪз + Х(this.footstepSplashSequence * 83 + var15 * 37 + 41) * ХЪц;
            Particle var37 = MINECRAFT_CLIENT.particleEngine.createParticle(ParticleTypes.SPLASH, var1 + var21 * var35, var3 + ХЪя, var5 + var23 * var35, var29, 0.0, var31);
            if (var37 != null) {
               var37.setParticleSpeed(var29, var33, var31);
            }
         }
      }
   }

   public Rain() {
      super(ХДЕ, ХДП, ModuleCategory.RENDER);
      this.reflectionStrengthSetting = new FloatSetting(ХД西, ХДд, ХДБ, 1.0F).withStep(ХДС);
      this.distortionMotionSetting = new FloatSetting(ХДз, ХДц, 0.0F, ХДя).withStep(ХЪс);
      this.reflectionSoftnessSetting = new FloatSetting(ХЪВ, ХЪЯ, ХЪХ, 1.0F).withStep(ХЪе);
      this.wetSurfaceDarkeningSetting = new FloatSetting(ХЪА, ХЪ4, 0.0F, ХЪп).withStep(ХЪД);
      this.rainwaterActivitySetting = new FloatSetting(ХЪЪ, ХЪт, 0.0F, ХЪа).withStep(ХЪЮ);
      this.footstepSplashesSetting = new BooleanSetting(ХЪЧ, true);
      this.splashStrengthSetting = new FloatSetting(ХЪЛ, 1.0F, ХЪ诶, 2.0F).withStep(ХЪ1).visibleWhen(this.footstepSplashesSetting::getValue);
      this.waveLifetimeSetting = new FloatSetting(ХЪЬ, ХЪ衣, ХЪш, ХЪИ).withStep(ХЪь).visibleWhen(this.footstepSplashesSetting::getValue);
      this.onlyDuringRainSetting = new BooleanSetting(ХЪж, false);
      this.rainEffectPipeline = RenderPipelineBuilder.builder()
         .withLocation(ХЪ弟)
         .withVertexShader(ClientResourceIds.getCoreId(ХЪб))
         .withFragmentShader(ClientResourceIds.getCoreId(ХЪЫ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(ХЪl, UniformType.UNIFORM_BUFFER)
         .withSampler(ХЪ8)
         .withSampler(ХЪ5)
         .withSampler(ХЪв)
         .withSampler(ХЪъ)
         .withSampler(ХЪМ)
         .build();
      this.reflectionTargets = new TextureTarget[3];
      this.reflectionProjectionBuffers = new ProjectionMatrixBuffer[3];
      this.reflectionTextureMatrices = new Matrix4f[3];
      this.inverseProjectionScratch = new Matrix4f();
      this.inverseViewScratch = new Matrix4f();
      this.reflectionPlaneHeights = new float[3];
      this.activeFootstepSplashes = new ArrayList<>(32);
      this.visibleFootstepSplashes = new ArrayList<>(32);
      this.visibleSplashAlphas = new float[32];
      this.playerTickListener = this::лГ;
      this.alternateFootstepSide = true;
      this.addSettings(this.reflectionStrengthSetting, this.distortionMotionSetting, this.reflectionSoftnessSetting, this.wetSurfaceDarkeningSetting, this.rainwaterActivitySetting, this.footstepSplashesSetting, this.splashStrengthSetting, this.waveLifetimeSetting, this.onlyDuringRainSetting);

      for (int var1 = 0; var1 < 3; var1++) {
         this.reflectionTextureMatrices[var1] = new Matrix4f();
         this.reflectionPlaneHeights[var1] = Float.NaN;
      }
   }

   @Override
   public void onDisable() {
      this.Йи();
      this.ЙЧ();
   }

   private void 弟(int var1, int var2) {
      int var3 = Math.max(1, var1);
      int var4 = Math.max(1, var2);
      int var5 = Math.max(1, Math.round(var3 * Хтю));
      int var6 = Math.max(1, Math.round(var4 * ХтЩ));
      boolean var7 = false;
      if (this.sceneCopyTarget == null) {
         this.sceneCopyTarget = new TextureTarget(Хту, var3, var4, true, GpuFormat.RGBA8_UNORM);
      } else if (this.sceneCopyTarget.width != var3 || this.sceneCopyTarget.height != var4) {
         this.sceneCopyTarget.resize(var3, var4);
      }

      for (int var8 = 0; var8 < 3; var8++) {
         if (this.reflectionProjectionBuffers[var8] == null) {
            this.reflectionProjectionBuffers[var8] = new ProjectionMatrixBuffer("rain reflection " + var8);
         }

         if (this.reflectionTargets[var8] == null) {
            this.reflectionTargets[var8] = new TextureTarget("Rain reflection " + var8, var5, var6, true, GpuFormat.RGBA8_UNORM);
            var7 = true;
         } else if (this.reflectionTargets[var8].width != var5 || this.reflectionTargets[var8].height != var6) {
            this.reflectionTargets[var8].resize(var5, var6);
            var7 = true;
         }
      }

      if (var7) {
         this.reflectionCaptureReady = false;
      }
   }

   private void method_8(Vec3 var1, double var2, double var4, boolean var6) {
      float var7 = Х(++this.footstepSplashSequence);
      float var8 = Х(this.footstepSplashSequence * 31 + 17);
      double var9 = (var7 - ХЪ7) * ХЪР;
      double var11 = Math.cos(var9);
      double var13 = Math.sin(var9);
      double var15 = var2 * var11 - var4 * var13;
      double var17 = var2 * var13 + var4 * var11;
      double var19 = var17;
      double var21 = -var15;
      double var23 = this.alternateFootstepSide ? ХЪ3 : 1.0;
      double var25 = var23 * (ХЪр + var8 * ХЪ6);
      double var27 = ХЪ_ + var7 * ХЪШ;
      double var29 = var1.x + var19 * var25 + var15 * var27;
      double var31 = var1.z + var21 * var25 + var17 * var27;
      Rain.SurfaceHeight var33 = this.method_8(var29, var31, var1.y);
      this.alternateFootstepSide = !this.alternateFootstepSide;
      if (var33 != null) {
         float var34 = this.splashStrengthSetting.getValue() * (var6 ? ХЪы : 1.0F) * (ХЪй + var8 * ХЪЖ);
         long var35 = (long)(this.waveLifetimeSetting.getValue() * ХЪм * (ХЪК + var7 * ХЪI));
         long var37 = System.currentTimeMillis();
         double var39 = var33.y() + ХЪТ;
         if (this.activeFootstepSplashes.size() >= 32) {
            this.activeFootstepSplashes.removeFirst();
         }

         this.activeFootstepSplashes.add(new Rain.FootstepSplash(var29, var39, var31, (float)var15, (float)var17, var7, var34, var37, var37 + var35));
         this.method_8(var29, var39, var31, var15, var17, var7, var34, var6);
      }
   }

   private static float Х(int var0) {
      int var1 = var0 * Хтl;
      var1 = (var1 ^ var1 >>> 16) * Хт8;
      var1 ^= var1 >>> 16;
      return (var1 & Хт5) / Хтв;
   }

   private void method_8(Std140Builder var1, Vec3 var2) {
      long var3 = System.currentTimeMillis();
      this.visibleFootstepSplashes.clear();

      for (Rain.FootstepSplash var6 : this.activeFootstepSplashes) {
         double var7 = var6.x - var2.x;
         double var9 = var6.z - var2.z;
         if (var7 * var7 + var9 * var9 <= ХтЫ && var6.expiresAtMillis > var3) {
            this.visibleFootstepSplashes.add(var6);
         }
      }

      int var11 = Math.min(32, this.visibleFootstepSplashes.size());

      for (int var12 = 0; var12 < var11; var12++) {
         this.visibleSplashAlphas[var12] = this.visibleFootstepSplashes.get(var12).method_0(var3);
      }

      for (int var13 = 0; var13 < 32; var13++) {
         if (var13 < var11) {
            Rain.FootstepSplash var15 = this.visibleFootstepSplashes.get(var13);
            var1.putVec4((float)(var15.x - var2.x), (float)(var15.z - var2.z), (float)(var15.y - var2.y), this.visibleSplashAlphas[var13]);
         } else {
            var1.putVec4(0.0F, 0.0F, 0.0F, 0.0F);
         }
      }

      for (int var14 = 0; var14 < 32; var14++) {
         if (var14 < var11) {
            Rain.FootstepSplash var16 = this.visibleFootstepSplashes.get(var14);
            var1.putVec4(var16.directionX, var16.directionZ, var16.randomPhase, var16.strength);
         } else {
            var1.putVec4(0.0F, 0.0F, 0.0F, 0.0F);
         }
      }

      var1.putVec4(var11, 1.0F, 0.0F, 0.0F);
   }

   private Rain.SurfaceHeight method_8(double var1, double var3, double var5) {
      int var7 = Mth.floor(var1);
      int var8 = Mth.floor(var3);
      int var9 = Mth.floor(var5 + Хт衣);
      double var10 = var1 - var7;
      double var12 = var3 - var8;
      double var14 = Double.NEGATIVE_INFINITY;
      MutableBlockPos var16 = new MutableBlockPos();

      for (int var17 = var9; var17 >= var9 - 3; var17--) {
         var16.set(var7, var17, var8);
         BlockState var18 = MINECRAFT_CLIENT.level.getBlockState(var16);
         if (!var18.isAir()) {
            VoxelShape var19 = var18.getCollisionShape(MINECRAFT_CLIENT.level, var16);
            if (!var19.isEmpty()) {
               for (AABB var21 : var19.toAabbs()) {
                  if (!(var10 < var21.minX - Хтш) && !(var10 > var21.maxX + ХтИ) && !(var12 < var21.minZ - Хть) && !(var12 > var21.maxZ + Хтж)) {
                     double var22 = var17 + var21.maxY;
                     if (var22 <= var5 + Хт弟 && var5 - var22 <= Хтб) {
                        var14 = Math.max(var14, var22);
                     }
                  }
               }
            }
         }
      }

      return Double.isFinite(var14) ? new Rain.SurfaceHeight(var14) : null;
   }

   private static Matrix4f method_8(Matrix4f var0) {
      return new Matrix4f(var0).scale(ХтO, Хт7, 1.0F);
   }

   public void method_8(
      LevelRenderer var1,
      GraphicsResourceAllocator var2,
      DeltaTracker var3,
      Camera var4,
      Matrix4f var5,
      Matrix4f var6,
      Matrix4f var7,
      GpuBufferSlice var8,
      Vector4f var9,
      boolean var10
   ) {
      if (this.Пу() && var1 != null && var2 != null && var3 != null && var4 != null && var5 != null && var6 != null && var7 != null) {
         RenderTarget var11;
         Rain.RenderStateSnapshot var12;
         try {
            this.method_8(var4);
            if (!this.hasReflectionPlanes || this.reflectionPlaneCount == 0) {
               return;
            }

            var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            if (var11 == null || var11.getColorTexture() == null || var11.getDepthTexture() == null) {
               return;
            }

            this.弟(var11.width, var11.height);
            var12 = Rain.RenderStateSnapshot.Д();
         } catch (Throwable var68) {
            method_8(ХЪн, var68);
            return;
         }

         Vec3 var13 = var4.position();
         boolean var14 = false;
         boolean var15 = false;
         boolean var16 = false;
         Throwable var17 = null;
         Н = new Matrix4f(var5);
         н = method_8(var7);
         capturingReflection = true;

         try {
            try {
               method_3(var11, this.sceneCopyTarget);
               var14 = true;
            } catch (Throwable var64) {
               var17 = method_8(var17, var64);
            }

            try {
               method_0(var11, this.sceneCopyTarget);
               var15 = true;
            } catch (Throwable var63) {
               var17 = method_8(var17, var63);
            }

            if (var17 == null) {
               for (int var18 = 0; var18 < this.reflectionPlaneCount; var18++) {
                  float var19 = this.reflectionPlaneHeights[var18];
                  TextureTarget var20 = this.reflectionTargets[var18];
                  Matrix4f var21 = method_8(var6);
                  Matrix4f var22 = method_8(var7);
                  Matrix4f var23 = new Matrix4f(var5).translate((float)(-var13.x), (float)(-var13.y), (float)(-var13.z));
                  Matrix4f var24 = new Matrix4f().translation(0.0F, var19 * 2.0F, 0.0F).scale(1.0F, ХЪЦ, 1.0F);
                  Matrix4f var25 = new Matrix4f().scaling(ХЪч, 1.0F, 1.0F).mul(var23).mul(var24);
                  Matrix4f var26 = new Matrix4f(var25).translate((float)var13.x, (float)var13.y, (float)var13.z);
                  Matrix4f var27 = method_8(var21, var25, var19 + ХЪЭ);
                  Matrix4f var28 = method_8(var22, var25, var19 + ХЪ必);
                  this.reflectionTextureMatrices[var18].set(new Matrix4f(var27).mul(var25));

                  Rain.RenderStateSnapshot var29;
                  try {
                     var29 = Rain.RenderStateSnapshot.Д();
                  } catch (Throwable var65) {
                     var17 = method_8(var17, var65);
                     break;
                  }

                  Throwable var30 = null;

                  try {
                     method_3(var20);
                     currentReflectionTarget = var20;
                     RenderSystem.outputColorTextureOverride = var20.getColorTextureView();
                     RenderSystem.outputDepthTextureOverride = var20.getDepthTextureView();
                     RenderSystem.setProjectionMatrix(this.reflectionProjectionBuffers[var18].getBuffer(var27), RenderSystem.getProjectionType());
                     MINECRAFT_CLIENT.levelExtractor.extract(var3, var4, var3.getGameTimeDeltaPartialTick(false));
                     CameraRenderState var31 = MINECRAFT_CLIENT.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
                     var31.viewRotationMatrix.set(var26);
                     var31.projectionMatrix.set(var27);
                     var1.render(var2, var3, false, var31, var26, var8, var9 == null ? new Vector4f() : new Vector4f(var9), var10);
                  } catch (Throwable var61) {
                     var30 = method_8(var30, var61);
                  } finally {
                     var30 = var29.Д(var30);
                  }

                  if (var30 != null) {
                     var17 = method_8(var17, var30);
                     break;
                  }
               }

               var16 = var17 == null;
            }
         } catch (Throwable var66) {
            var17 = method_8(var17, var66);
         } finally {
            var17 = var12.Д(var17);
            if (var14) {
               try {
                  method_3(this.sceneCopyTarget, var11);
               } catch (Throwable var60) {
                  var17 = method_8(var17, var60);
               }
            }

            if (var15) {
               try {
                  method_0(this.sceneCopyTarget, var11);
               } catch (Throwable var59) {
                  var17 = method_8(var17, var59);
               }
            }

            capturingReflection = false;
            Н = null;
            н = null;
            var17 = method_8(var17, () -> MINECRAFT_CLIENT.levelExtractor.extract(var3, var4, var3.getGameTimeDeltaPartialTick(false)));
         }

         if (this.cleanupPendingAfterCapture) {
            this.Йк();
            var16 = false;
         }

         this.reflectionCaptureReady = this.isEnabled() && var16 && var17 == null;
         method_8(ХЪщ, var17);
      }
   }

   private static boolean method_8(Throwable var0) {
      return var0 instanceof VirtualMachineError || var0 instanceof LinkageError;
   }

   @Override
   public void onEnable() {
      this.Йа();
      this.lastReflectionCameraPosition = null;
      this.cleanupPendingAfterCapture = false;
      this.Йи();
      this.splashLevel = MINECRAFT_CLIENT.level;
   }

   public void ЙЧ() {
      this.reflectionCaptureReady = false;
      if (capturingReflection) {
         this.cleanupPendingAfterCapture = true;
      } else {
         this.Йк();
      }
   }

   private Map<Integer, Integer> д_ea411899() {
      HashMap<Integer, Integer> var1 = new HashMap<>();
      MutableBlockPos var2 = new MutableBlockPos();
      MutableBlockPos var3 = new MutableBlockPos();

      for (int var4 = -42; var4 <= 42; var4 += 6) {
         for (int var5 = -42; var5 <= 42; var5 += 6) {
            int var6 = this.reflectionCenterBlockX + var5;
            int var7 = this.reflectionCenterBlockZ + var4;
            int var8 = MINECRAFT_CLIENT.level.getHeight(Types.WORLD_SURFACE, var6, var7);

            for (int var9 = MINECRAFT_CLIENT.level.getMinY(); var9 < var8; var9++) {
               var2.set(var6, var9, var7);
               BlockState var10 = MINECRAFT_CLIENT.level.getBlockState(var2);
               if (!var10.isAir()) {
                  VoxelShape var11 = var10.getCollisionShape(MINECRAFT_CLIENT.level, var2);
                  if (var11.isEmpty()) {
                     var11 = var10.getShape(MINECRAFT_CLIENT.level, var2);
                  }

                  if (!var11.isEmpty()) {
                     double var12 = var11.max(Axis.Y);
                     var3.set(var6, var9 + 1, var7);
                     BlockState var14 = MINECRAFT_CLIENT.level.getBlockState(var3);
                     VoxelShape var15 = var14.getCollisionShape(MINECRAFT_CLIENT.level, var3);
                     if (var15.isEmpty()) {
                        var15 = var14.getShape(MINECRAFT_CLIENT.level, var3);
                     }

                     boolean var16 = var12 < Хт必 || var15.isEmpty() || var15.min(Axis.Y) > Хтщ;
                     if (var16) {
                        int var17 = (int)Math.round((var9 + var12) * ХтГ);
                        var1.merge(var17, 1, Integer::sum);
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   private static void method_3(RenderTarget var0, RenderTarget var1) {
      int var2 = Math.min(var0.width, var1.width);
      int var3 = Math.min(var0.height, var1.height);
      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0.getColorTexture(), var1.getColorTexture(), 0, 0, 0, 0, 0, var2, var3);
   }

   private static Matrix4f method_8(Matrix4f var0, Matrix4f var1, float var2) {
      Vector4f var3 = new Vector4f(0.0F, 1.0F, 0.0F, -var2);
      new Matrix4f(var1).invert().transpose().transform(var3);
      float var4 = (float)Math.sqrt(var3.x * var3.x + var3.y * var3.y + var3.z * var3.z);
      if (var4 <= Хт2) {
         return new Matrix4f(var0);
      }

      var3.div(var4);
      boolean var5 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
      Vector4f var6 = new Vector4f(var3.x >= 0.0F ? 1.0F : Хтф, var3.y >= 0.0F ? 1.0F : Хти, var5 ? 0.0F : Хт9, 1.0F);
      new Matrix4f(var0).invert().transform(var6);
      float var7 = var3.dot(var6);
      if (Math.abs(var7) <= Хто) {
         return new Matrix4f(var0);
      }

      Vector4f var8 = new Vector4f(var3).mul((var5 ? 1.0F : 2.0F) / var7);
      Matrix4f var9 = new Matrix4f(var0);
      var9.m02(var9.m03() - var8.x);
      var9.m12(var9.m13() - var8.y);
      var9.m22(var9.m23() - var8.z);
      var9.m32(var9.m33() - var8.w);
      return var9;
   }

   public static boolean Пз() {
      return capturingReflection;
   }

   private GpuBuffer method_0() {
      if (this.rainUniformBuffer == null || this.rainUniformBuffer.isClosed()) {
         this.rainUniformBuffer = RenderSystem.getDevice().createBuffer(() -> Хтр, 136, ХЪ2);
      }

      return this.rainUniformBuffer;
   }

   private void Йи() {
      this.activeFootstepSplashes.clear();
      this.visibleFootstepSplashes.clear();
      this.splashLevel = null;
      this.lastFootstepPosition = null;
      this.footstepDistanceAccumulator = 0.0;
      this.alternateFootstepSide = true;
      this.ambientSplashTickCounter = 0;
   }

   private boolean П7() {
      if (!this.footstepSplashesSetting.getValue()) {
         return false;
      }

      long var1 = System.currentTimeMillis();

      for (Rain.FootstepSplash var4 : this.activeFootstepSplashes) {
         if (var4.expiresAtMillis > var1) {
            return true;
         }
      }

      return false;
   }

   private void лГ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.splashLevel != MINECRAFT_CLIENT.level) {
            this.Йи();
            this.splashLevel = MINECRAFT_CLIENT.level;
         }

         long var2 = System.currentTimeMillis();
         this.activeFootstepSplashes.removeIf(var2x -> var2x.expiresAtMillis <= var2);
         Vec3 var4 = MINECRAFT_CLIENT.player.position();
         float var5 = this.method_3л();
         this.method_0(var4, var5);
         if (!this.footstepSplashesSetting.getValue()) {
            this.activeFootstepSplashes.clear();
            this.lastFootstepPosition = var4;
            this.footstepDistanceAccumulator = 0.0;
         } else if (this.lastFootstepPosition == null) {
            this.lastFootstepPosition = var4;
         } else {
            double var6 = var4.x - this.lastFootstepPosition.x;
            double var8 = var4.z - this.lastFootstepPosition.z;
            double var10 = var6 * var6 + var8 * var8;
            this.lastFootstepPosition = var4;
            if (var10 > ХЪф) {
               this.footstepDistanceAccumulator = 0.0;
            } else if (MINECRAFT_CLIENT.player.onGround() && !MINECRAFT_CLIENT.player.isPassenger() && !MINECRAFT_CLIENT.player.isSwimming() && !(var5 <= ХЪи)) {
               double var12 = Math.sqrt(var10);
               if (!(var12 < ХЪ9)) {
                  this.footstepDistanceAccumulator += var12;
                  double var14 = MINECRAFT_CLIENT.player.isSprinting() ? ХЪо : ХЪO;
                  if (!(this.footstepDistanceAccumulator < var14)) {
                     this.footstepDistanceAccumulator %= var14;
                     double var16 = var6 / var12;
                     double var18 = var8 / var12;
                     this.method_8(var4, var16, var18, MINECRAFT_CLIENT.player.isSprinting());
                  }
               }
            } else {
               this.footstepDistanceAccumulator = 0.0;
            }
         }
      } else {
         this.Йи();
      }
   }

   private static final class FootstepSplash {
      private final @com.google.gson.annotations.SerializedName("Ва")  double x;
      private final @com.google.gson.annotations.SerializedName("ВЮ")  double y;
      private final @com.google.gson.annotations.SerializedName("ВЧ")  double z;
      private final @com.google.gson.annotations.SerializedName("А_")  float directionX;
      private final @com.google.gson.annotations.SerializedName("АШ")  float directionZ;
      private final @com.google.gson.annotations.SerializedName("Аы")  float randomPhase;
      private final @com.google.gson.annotations.SerializedName("Ай")  float strength;
      private final @com.google.gson.annotations.SerializedName("В6")  long createdAtMillis;
      private final @com.google.gson.annotations.SerializedName("В_")  long expiresAtMillis;

      private float method_0(long var1) {
         long var3 = Math.max(1L, this.expiresAtMillis - this.createdAtMillis);
         return Math.clamp((float)(var1 - this.createdAtMillis) / (float)var3, 0.0F, 1.0F);
      }

      private FootstepSplash(double var1, double var3, double var5, float var7, float var8, float var9, float var10, long var11, long var13) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.directionX = var7;
         this.directionZ = var8;
         this.randomPhase = var9;
         this.strength = var10;
         this.createdAtMillis = var11;
         this.expiresAtMillis = var13;
      }
   }

   private static final class RenderStateSnapshot {
      @SerializedName("е")
      private final GpuTextureView outputColorTextureOverride = RenderSystem.outputColorTextureOverride;
      private final @com.google.gson.annotations.SerializedName("А")  GpuTextureView outputDepthTextureOverride = RenderSystem.outputDepthTextureOverride;
      @SerializedName("Я")
      private final GpuBufferSlice projectionBuffer = RenderSystem.getProjectionMatrixBuffer();
      @SerializedName("Я")
      private final ProjectionType projectionType = RenderSystem.getProjectionType();
      private final @com.google.gson.annotations.SerializedName("Х")  GpuBufferSlice shaderFog = RenderSystem.getShaderFog();
      @SerializedName("е")
      private final GpuBufferSlice shaderLights = RenderSystem.getShaderLights();
      private final @com.google.gson.annotations.SerializedName("ч")  GpuBuffer globalSettingsUniform = RenderSystem.getGlobalSettingsUniform();
      private final @com.google.gson.annotations.SerializedName("Л")  Matrix4f modelViewMatrix = new Matrix4f(RenderSystem.getModelViewMatrixCopy());
      private final @com.google.gson.annotations.SerializedName("诶")  Matrix4f cameraViewRotationMatrix;
      @SerializedName("1")
      private final Matrix4f cameraProjectionMatrix;
      private final @com.google.gson.annotations.SerializedName("ВO")  boolean scissorEnabled;
      private final @com.google.gson.annotations.SerializedName("Хщ")  int scissorX;
      private final @com.google.gson.annotations.SerializedName("ХГ")  int scissorY;
      private final @com.google.gson.annotations.SerializedName("Хл")  int scissorWidth;
      private final @com.google.gson.annotations.SerializedName("Хэ")  int scissorHeight;
      @SerializedName("е")
      private final RenderTarget reflectionTarget;

      private Throwable Д(Throwable var1) {
         var1 = Rain.method_8(var1, () -> Rain.currentReflectionTarget = this.reflectionTarget);
         var1 = Rain.method_8(var1, () -> RenderSystem.outputColorTextureOverride = this.outputColorTextureOverride);
         var1 = Rain.method_8(var1, () -> RenderSystem.outputDepthTextureOverride = this.outputDepthTextureOverride);
         var1 = Rain.method_8(var1, () -> RenderSystem.setProjectionMatrix(this.projectionBuffer, this.projectionType));
         var1 = Rain.method_8(var1, () -> RenderSystem.setShaderFog(this.shaderFog));
         var1 = Rain.method_8(var1, () -> RenderSystem.setShaderLights(this.shaderLights));
         var1 = Rain.method_8(var1, () -> RenderSystem.setGlobalSettingsUniform(this.globalSettingsUniform));
         var1 = Rain.method_8(var1, () -> RenderSystem.getModelViewStack().set(this.modelViewMatrix));
         CameraRenderState var2 = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         var1 = Rain.method_8(var1, () -> var2.viewRotationMatrix.set(this.cameraViewRotationMatrix));
         var1 = Rain.method_8(var1, () -> var2.projectionMatrix.set(this.cameraProjectionMatrix));
         var1 = Rain.method_8(var1, () -> RenderSystem.enableScissorForRenderTypeDraws(this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight));
         if (!this.scissorEnabled) {
            var1 = Rain.method_8(var1, RenderSystem::disableScissorForRenderTypeDraws);
         }

         return var1;
      }

      private static Rain.RenderStateSnapshot Д() {
         return new Rain.RenderStateSnapshot();
      }

      private RenderStateSnapshot() {
         CameraRenderState var1 = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         this.cameraViewRotationMatrix = new Matrix4f(var1.viewRotationMatrix);
         this.cameraProjectionMatrix = new Matrix4f(var1.projectionMatrix);
         ScissorState var2 = RenderSystem.getScissorStateForRenderTypeDraws();
         this.scissorEnabled = var2.enabled();
         this.scissorX = var2.x();
         this.scissorY = var2.y();
         this.scissorWidth = var2.width();
         this.scissorHeight = var2.height();
         this.reflectionTarget = Rain.currentReflectionTarget;
      }
   }

   private record SurfaceHeight(double y) {
   }
}
