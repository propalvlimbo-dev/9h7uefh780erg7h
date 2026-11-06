package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.events.impl.FogRenderEvent;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.RenderPipelineBuilder;

public class FogAtmosphere extends ClientModule {
   private static final int еl = 256;
   private static final int е8 = 320;
   @SerializedName("_")
   private final SingleChoiceSetting renderModeSetting;
   @SerializedName("сМ")
   private final BooleanSetting fogOnlySetting;
   private final @com.google.gson.annotations.SerializedName("Ш")  SingleChoiceSetting lightAtmosphereStyleSetting;
   private final @com.google.gson.annotations.SerializedName("Вб")  FloatSetting lightFogDensitySetting;
   private final @com.google.gson.annotations.SerializedName("ВЫ")  FloatSetting lightFogHeightSetting;
   @SerializedName("сН")
   private final BooleanSetting fastRaysSetting;
   private final @com.google.gson.annotations.SerializedName("Вl")  FloatSetting fastRaysBrightnessSetting;
   @SerializedName("сн")
   private final BooleanSetting fastRaysShadingSetting;
   private final @com.google.gson.annotations.SerializedName("сЦ")  BooleanSetting moonHazeSetting;
   private final @com.google.gson.annotations.SerializedName("В8")  FloatSetting lightFogSoftnessSetting;
   private final @com.google.gson.annotations.SerializedName("сч")  BooleanSetting useClientLightFogColorsSetting;
   private final @com.google.gson.annotations.SerializedName("Н")  ColorSetting coolLightFogColorSetting;
   private final @com.google.gson.annotations.SerializedName("н")  ColorSetting warmLightFogColorSetting;
   private final @com.google.gson.annotations.SerializedName("ы")  SingleChoiceSetting paletteSetting;
   private final @com.google.gson.annotations.SerializedName("В5")  FloatSetting atmosphereSaturationSetting;
   private final @com.google.gson.annotations.SerializedName("Вв")  FloatSetting hazeUpperBoundSetting;
   private final @com.google.gson.annotations.SerializedName("Въ")  FloatSetting sunRayGlowSetting;
   @SerializedName("ВМ")
   private final FloatSetting powerfulFogSoftnessSetting;
   private final @com.google.gson.annotations.SerializedName("Ц")  ColorSetting dawnLineColorSetting;
   private final @com.google.gson.annotations.SerializedName("св")  RenderPipeline optimizedFogPipeline;
   private final @com.google.gson.annotations.SerializedName("съ")  RenderPipeline optimizedFogCompositePipeline;
   @SerializedName("сМ")
   private final RenderPipeline optimizedLightPipeline;
   @SerializedName("сН")
   private final RenderPipeline powerfulAtmospherePipeline;
   @SerializedName("сн")
   private final RenderPipeline powerfulFogOnlyPipeline;
   private @com.google.gson.annotations.SerializedName("ъ")  TextureTarget fogTarget;
   private @com.google.gson.annotations.SerializedName("М")  TextureTarget sceneCopyTarget;
   private @com.google.gson.annotations.SerializedName("Э")  GpuBuffer optimizedUniformBuffer;
   private @com.google.gson.annotations.SerializedName("必")  GpuBuffer powerfulUniformBuffer;
   private @com.google.gson.annotations.SerializedName("诶")  ClientLevel smoothedGroundLevel;
   @SerializedName("1")
   private ClientLevel groundHeightCacheLevel;
   private @com.google.gson.annotations.SerializedName("АЫ")  float smoothedGroundHeight;
   private @com.google.gson.annotations.SerializedName("Аl")  float smoothedCameraY;
   private @com.google.gson.annotations.SerializedName("А8")  float cachedGroundHeight;
   private @com.google.gson.annotations.SerializedName("е5")  int cachedGroundBlockX;
   private @com.google.gson.annotations.SerializedName("ев")  int cachedGroundBlockZ;
   @SerializedName("ВМ")
   private final EventListener<FogRenderEvent> fogRenderListener;
   private static final String спж = "FogAtmosphere";
   private static final String сп弟 = "Низкий объёмный туман и солнечные лучи";
   private static final String спб = "Режим рендера";
   private static final String спЫ = "Optimized";
   private static final String спl = "Powerful";
   private static final String сп8 = "Только туман";
   private static final String сп5 = "Стиль лёгкой атмосферы";
   private static final String спв = "Объёмный туман";
   private static final String спъ = "Ночная дымка";
   private static final String спМ = "Густота лёгкого тумана";
   private static final float спН = 0.05F;
   private static final float спн = 1.5F;
   private static final float спЦ = 0.05F;
   private static final String спч = "Высота лёгкого тумана";
   private static final float спЭ = 14.0F;
   private static final float сп必 = 64.0F;
   private static final String спщ = "Быстрые лучи";
   private static final String спГ = "Яркость быстрых лучей";
   private static final float спл = 0.05F;
   private static final float спэ = 0.05F;
   private static final String спю = "Затенение быстрых лучей";
   private static final String спЩ = "Лунная дымка";
   private static final String спу = "Мягкость лёгкого тумана";
   private static final float сп2 = 0.7F;
   private static final float спф = 0.1F;
   private static final float спи = 1.5F;
   private static final float сп9 = 0.05F;
   private static final String спо = "Цвета клиента для лёгкого тумана";
   private static final String спO = "Холодный оттенок лёгкого тумана";
   private static final int сп7 = -4275002;
   private static final String спР = "Тёплый оттенок лёгкого тумана";
   private static final String сп3 = "Палитра";
   private static final String спр = "Рассвет";
   private static final String сп6 = "Сумерки";
   private static final String сп_ = "Цвет клиента";
   private static final String спШ = "Насыщенность атмосферы";
   private static final float спы = 0.35F;
   private static final float спй = 0.05F;
   private static final float спЖ = 0.8F;
   private static final float спм = 0.01F;
   private static final String спК = "Верхняя граница дымки";
   private static final float спI = 76.0F;
   private static final float спТ = 60.0F;
   private static final float спО = 120.0F;
   private static final String спФ = "Сияние солнечных лучей";
   private static final float спЙ = 0.75F;
   private static final float спк = 0.01F;
   private static final String спг = "Мягкость";
   private static final float сп0 = 0.6F;
   private static final float спх = 0.01F;
   private static final String спУ = "Оттенок линии рассвета";
   private static final String спЗ = "rose/fog_atmosphere";
   private static final String спЕ = "fog_atmosphere";
   private static final String спП = "fog_atmosphere";
   private static final String сп西 = "FogAtmosphere";
   private static final String спд = "DepthSampler";
   private static final String спБ = "rose/fog_atmosphere_composite";
   private static final String спС = "fog_atmosphere";
   private static final String спз = "fog_atmosphere_composite";
   private static final String спц = "FogAtmosphere";
   private static final String спя = "FogSampler";
   private static final String сДс = "rose/fog_atmosphere_light";
   private static final String сДВ = "fog_atmosphere";
   private static final String сДЯ = "fog_atmosphere_light";
   private static final String сДХ = "FogAtmosphere";
   private static final String сДе = "FogSampler";
   private static final String сДА = "rose/fog_atmosphere_powerful";
   private static final String сД4 = "fog_atmosphere";
   private static final String сДп = "fog_atmosphere_powerful";
   private static final String сДД = "PowerfulAtmosphere";
   private static final String сДЪ = "SceneSampler";
   private static final String сДт = "DepthSampler";
   private static final String сДа = "rose/fog_atmosphere_powerful_fog_only";
   private static final String сДЮ = "fog_atmosphere";
   private static final String сДЧ = "fog_atmosphere_powerful_fog_only";
   private static final String сДЛ = "PowerfulAtmosphere";
   private static final String сД诶 = "SceneSampler";
   private static final String сД1 = "DepthSampler";
   private static final int сДЬ = Integer.MIN_VALUE;
   private static final int сД衣 = Integer.MIN_VALUE;
   private static final String сДш = "Powerful";
   private static final float сДИ = 96.0F;
   private static final float сДь = 0.075F;
   private static final double сДж = 96.0;
   private static final float сД弟 = 0.12F;
   private static final float сДб = (float) (Math.PI / 180.0);
   private static final float сДЫ = -0.08F;
   private static final float сДl = 0.2F;
   private static final float сД8 = -0.08F;
   private static final float сД5 = 0.2F;
   private static final float сДв = 0.75F;
   private static final float сДъ = 0.5F;
   private static final float сДМ = 0.5F;
   private static final float сДН = 1.0E-4F;
   private static final float сДн = 0.5F;
   private static final float сДЦ = 0.5F;
   private static final float сДч = 0.5F;
   private static final float сДЭ = 0.5F;
   private static final float сД必 = 0.5F;
   private static final double сДщ = 2.0;
   private static final float сДГ = 0.5F;
   private static final double сДл = 2.0;
   private static final float сДэ = 0.015F;
   private static final float сДю = 0.12F;
   private static final float сДЩ = 0.68F;
   private static final float сДу = 1.55F;
   private static final long сД2 = 240000L;
   private static final float сДф = 20.0F;
   private static final float сДи = -1.0F;
   private static final float сД9 = 16.0F;
   private static final String сДо = "DepthSampler";
   private static final String сДO = "FogAtmosphere";
   private static final String сД7 = "FogAtmosphere";
   private static final String сДР = "FogSampler";
   private static final String сД3 = "FogAtmosphere";
   private static final String сДр = "FogSampler";
   private static final float сД6 = (float) (Math.PI / 180.0);
   private static final String сД_ = "Сумерки";
   private static final float сДШ = -0.045F;
   private static final String сДы = "Цвет клиента";
   private static final float сДй = 0.13F;
   private static final float сДЖ = 0.11F;
   private static final float сДм = -1.0F;
   private static final float сДК = 0.5F;
   private static final float сДI = 0.5F;
   private static final float сДТ = 4.0F;
   private static final float сДО = 1.0E-4F;
   private static final float сДФ = 1000.0F;
   private static final float сДЙ = 1000.0F;
   private static final float сДк = 1000.0F;
   private static final float сДг = 1.0E-4F;
   private static final float сД0 = 0.5F;
   private static final float сДх = 0.5F;
   private static final float сДУ = 0.5F;
   private static final float сДЗ = 0.5F;
   private static final float сДЕ = 18.0F;
   private static final long сДП = 100000L;
   private static final float сД西 = 0.05F;
   private static final float сДд = 16.0F;
   private static final String сДБ = "PowerfulAtmosphere";
   private static final String сДС = "SceneSampler";
   private static final String сДз = "DepthSampler";
   private static final String сДц = "Сумерки";
   private static final float сДя = 0.135F;
   private static final float сЪс = 0.125F;
   private static final float сЪВ = 0.3F;
   private static final float сЪЯ = 0.89F;
   private static final float сЪХ = 0.46F;
   private static final float сЪе = 0.55F;
   private static final float сЪА = 0.38F;
   private static final float сЪ4 = 0.35F;
   private static final float сЪп = 0.56F;
   private static final float сЪД = 0.8F;
   private static final float сЪЪ = 0.52F;
   private static final float сЪт = 0.62F;
   private static final float сЪа = 0.47F;
   private static final float сЪЮ = 0.44F;
   private static final float сЪЧ = 0.64F;
   private static final float сЪЛ = 0.92F;
   private static final float сЪ诶 = 0.56F;
   private static final float сЪ1 = 0.72F;
   private static final String сЪЬ = "Цвет клиента";
   private static final float сЪ衣 = 255.0F;
   private static final float сЪш = 255.0F;
   private static final float сЪИ = 255.0F;
   private static final String сЪь = "Цвет клиента";
   private static final float сЪж = 0.085F;
   private static final float сЪ弟 = 0.1F;
   private static final float сЪб = 0.2F;
   private static final float сЪЫ = 0.3F;
   private static final float сЪl = 0.93F;
   private static final float сЪ8 = 0.82F;
   private static final float сЪ5 = 0.35F;
   private static final float сЪв = 0.52F;
   private static final float сЪъ = 0.58F;
   private static final float сЪМ = 0.74F;
   private static final float сЪН = 0.28F;
   private static final float сЪн = 0.97F;
   private static final float сЪЦ = 0.93F;
   private static final float сЪч = 0.88F;
   private static final float сЪЭ = 0.45F;
   private static final float сЪ必 = 0.58F;
   private static final float сЪщ = 0.63F;
   private static final float сЪГ = 0.76F;
   private static final float сЪл = 0.35F;
   private static final float сЪэ = 0.96F;
   private static final float сЪю = 0.88F;
   private static final float сЪЩ = 0.3F;
   private static final float сЪу = 0.16F;
   private static final float сЪ2 = 0.19F;
   private static final float сЪф = 0.38F;
   private static final float сЪи = 0.14F;
   private static final float сЪ9 = 1.12F;
   private static final float сЪо = 0.88F;
   private static final float сЪO = 0.62F;
   private static final float сЪ7 = 0.56F;
   private static final float сЪР = 0.62F;
   private static final float сЪ3 = 0.8F;
   private static final float сЪр = 0.95F;
   private static final float сЪ6 = 0.55F;
   private static final float сЪ_ = 0.63F;
   private static final float сЪШ = 0.42F;
   private static final float сЪы = 0.6F;
   private static final float сЪй = 0.67F;
   private static final float сЪЖ = 0.82F;
   private static final float сЪм = 1.08F;
   private static final float сЪК = 0.94F;
   private static final float сЪI = 0.72F;
   private static final float сЪТ = 0.41222146F;
   private static final float сЪО = 0.53633255F;
   private static final float сЪФ = 0.051445995F;
   private static final float сЪЙ = 0.2119035F;
   private static final float сЪк = 0.6806995F;
   private static final float сЪг = 0.10739696F;
   private static final float сЪ0 = 0.08830246F;
   private static final float сЪх = 0.28171885F;
   private static final float сЪУ = 0.6299787F;
   private static final float сЪЗ = 0.41222146F;
   private static final float сЪЕ = 0.53633255F;
   private static final float сЪП = 0.051445995F;
   private static final float сЪ西 = 0.2119035F;
   private static final float сЪд = 0.6806995F;
   private static final float сЪБ = 0.10739696F;
   private static final float сЪС = 0.08830246F;
   private static final float сЪз = 0.28171885F;
   private static final float сЪц = 0.6299787F;
   private static final float сЪя = 4.0767417F;
   private static final float стс = 3.3077116F;
   private static final float стВ = 0.23096994F;
   private static final float стЯ = -1.268438F;
   private static final float стХ = 2.6097574F;
   private static final float сте = 0.34131938F;
   private static final float стА = -0.0041960864F;
   private static final float ст4 = 0.7034186F;
   private static final float стп = 1.7076147F;
   private static final float стД = 0.04045F;
   private static final float стЪ = 12.92F;
   private static final float стт = 0.055F;
   private static final float ста = 1.055F;
   private static final double стЮ = 2.4;
   private static final float стЧ = 0.0031308F;
   private static final float стЛ = 12.92F;
   private static final double ст诶 = 1.055;
   private static final double ст1 = 0.4166666666666667;
   private static final double стЬ = 0.055;
   private static final float ст衣 = 8.0F;
   private static final float стш = 0.85F;
   private static final float стИ = 4.0F;
   private static final float сть = 1.0E7F;
   private static final float стж = 1.0E7F;
   private static final String ст弟 = "FogAtmosphere layer";
   private static final String стб = "FogAtmosphere powerful scene";
   private static final long стЫ = 256L;
   private static final long стl = 320L;
   private static final double ст8 = 8192.0;
   private static final double ст5 = 8192.0;
   private static final float ств = 3.0F;
   private static final String стъ = "Ночная дымка";
   private static final int стМ = Integer.MIN_VALUE;
   private static final int стН = Integer.MIN_VALUE;
   private static final String стн = "FogAtmosphere powerful uniforms";
   private static final String стЦ = "FogAtmosphere uniforms";
   private static final String стч = "FogAtmosphere powerful";
   private static final String стЭ = "FogAtmosphere light";
   private static final String ст必 = "FogAtmosphere composite";
   private static final String стщ = "FogAtmosphere layer";
   private static final String стГ = "Powerful";
   private static final String стл = "Рассвет";
   private static final String стэ = "Powerful";
   private static final String стю = "Powerful";
   private static final String стЩ = "Powerful";
   private static final String сту = "Powerful";
   private static final String ст2 = "Powerful";
   private static final String стф = "Optimized";
   private static final String сти = "Optimized";
   private static final String ст9 = "Optimized";
   private static final String сто = "Optimized";
   private static final String стO = "Optimized";
   private static final String ст7 = "Optimized";
   private static final String стР = "Optimized";
   private static final String ст3 = "Optimized";
   private static final String стр = "Optimized";
   private static final String ст6 = "Optimized";
   private static final String ст_ = "Optimized";

   private void У(int var1, int var2) {
      if (this.fogTarget == null) {
         this.fogTarget = new TextureTarget(ст弟, var1, var2, false, GpuFormat.RGBA8_UNORM);
      } else if (this.fogTarget.width != var1 || this.fogTarget.height != var2) {
         this.fogTarget.resize(var1, var2);
      }
   }

   private float Я(Vec3 var1) {
      int var2 = Mth.floor(MINECRAFT_CLIENT.player.getX());
      int var3 = Mth.floor(MINECRAFT_CLIENT.player.getZ());
      if (MINECRAFT_CLIENT.player.onGround()) {
         this.groundHeightCacheLevel = MINECRAFT_CLIENT.level;
         this.cachedGroundBlockX = var2;
         this.cachedGroundBlockZ = var3;
         this.cachedGroundHeight = (float)MINECRAFT_CLIENT.player.getY();
         return this.cachedGroundHeight;
      }

      float var4 = (float)MINECRAFT_CLIENT.player.getY() - Math.max(ст衣, this.lightFogHeightSetting.getValue() * стш);
      if (this.groundHeightCacheLevel == MINECRAFT_CLIENT.level && Math.abs(this.cachedGroundBlockX - var2) <= 8 && Math.abs(this.cachedGroundBlockZ - var3) <= 8 && Float.isFinite(this.cachedGroundHeight)) {
         return Math.max(this.cachedGroundHeight, var4);
      }

      this.groundHeightCacheLevel = MINECRAFT_CLIENT.level;
      this.cachedGroundBlockX = var2;
      this.cachedGroundBlockZ = var3;
      float var5 = Float.NEGATIVE_INFINITY;
      float var6 = (float)var1.y + стИ;

      for (int var7 = -4; var7 <= 4; var7 += 4) {
         for (int var8 = -4; var8 <= 4; var8 += 4) {
            float var9 = MINECRAFT_CLIENT.level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var2 + var7, var3 + var8);
            if (var9 <= var6) {
               var5 = Math.max(var5, var9);
            }
         }
      }

      this.cachedGroundHeight = Float.isFinite(var5) ? var5 : var4;
      return Math.max(this.cachedGroundHeight, var4);
   }

   @Override
   public void onDisable() {
      this.smoothedGroundLevel = null;
      this.groundHeightCacheLevel = null;
      this.smoothedGroundHeight = Float.NaN;
      this.smoothedCameraY = Float.NaN;
      this.cachedGroundHeight = Float.NaN;
      this.cachedGroundBlockX = стМ;
      this.cachedGroundBlockZ = стН;
      if (this.fogTarget != null) {
         this.fogTarget.destroyBuffers();
         this.fogTarget = null;
      }

      if (this.sceneCopyTarget != null) {
         this.sceneCopyTarget.destroyBuffers();
         this.sceneCopyTarget = null;
      }

      if (this.optimizedUniformBuffer != null) {
         this.optimizedUniformBuffer.close();
         this.optimizedUniformBuffer = null;
      }

      if (this.powerfulUniformBuffer != null) {
         this.powerfulUniformBuffer.close();
         this.powerfulUniformBuffer = null;
      }
   }

   private static void Я(float[] var0, int var1, float var2, float var3, float var4) {
      var0[var1] = var2;
      var0[var1 + 1] = var3;
      var0[var1 + 2] = var4;
   }

   private float Я3_f67ab10a() {
      return this.lightAtmosphereStyleSetting.isSelected(стъ) ? 1.0F : 0.0F;
   }

   private boolean Йе() {
      return this.isEnabled()
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.gameRenderer != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera() != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
   }

   private void чх() {
      if (this.powerfulUniformBuffer == null || this.powerfulUniformBuffer.isClosed()) {
         this.powerfulUniformBuffer = RenderSystem.getDevice().createBuffer(() -> стн, 136, стl);
      }
   }

   private void ч(Matrix4f var1, Matrix4f var2, DeltaTracker var3) {
      RenderTarget var4 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      if (var4 != null && var4.getColorTexture() != null && var4.getDepthTexture() != null && !var4.getDepthTexture().isClosed()) {
         int var5 = Math.max(1, var4.width);
         int var6 = Math.max(1, var4.height);
         this.Ю_de5a6f78(var5, var6);
         this.чх();
         float var7 = var3.getGameTimeDeltaPartialTick(false);
         Vec3 var8 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         float var9 = MINECRAFT_CLIENT.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, var7) * сД6;
         float var10 = -Mth.sin(var9);
         float var11 = this.paletteSetting.isSelected(сД_) ? сДШ : (this.paletteSetting.isSelected(сДы) ? сДй : сДЖ);
         float var12 = var10 >= 0.0F ? 1.0F : сДм;
         float var13 = var12 * Mth.cos(var11);
         float var14 = Mth.sin(var11);
         Vector4f var15 = new Vector4f(var13, var14, 0.0F, 0.0F);
         new Matrix4f(var2).transform(var15);
         float var16 = сДК;
         float var17 = сДI;
         float var18 = Mth.clamp(-var15.z * сДТ, 0.0F, 1.0F);
         if (-var15.z > сДО) {
            Vector4f var19 = new Vector4f(var15.x * сДФ, var15.y * сДЙ, var15.z * сДк, 1.0F);
            new Matrix4f(var1).transform(var19);
            if (var19.w > сДг) {
               var16 = var19.x / var19.w * сД0 + сДх;
               var17 = var19.y / var19.w * сДУ + сДЗ;
            }
         }

         float[] var34 = this.Й();
         float var20 = this.hazeUpperBoundSetting.getValue();
         float var21 = var20 - сДЕ;
         float var22 = ((float)(MINECRAFT_CLIENT.level.getGameTime() % сДП) + var7) * сД西;
         float var23 = MINECRAFT_CLIENT.options.getEffectiveRenderDistance() * сДд;
         Matrix4f var24 = new Matrix4f(var1).invert();
         Matrix4f var25 = new Matrix4f(var2).invert();
         var25.m30((float)var8.x);
         var25.m31((float)var8.y);
         var25.m32((float)var8.z);
         CommandEncoder var26 = RenderSystem.getDevice().createCommandEncoder();
         var26.copyTextureToTexture(var4.getColorTexture(), this.sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, var5, var6);

         try (MemoryStack var27 = MemoryStack.stackPush()) {
            ByteBuffer var28 = Std140Builder.onStack(var27, 320)
               .putMat4f(var24)
               .putMat4f(var25)
               .putVec4((float)var8.x, (float)var8.y, (float)var8.z, 0.0F)
               .putVec4(var13, var14, 0.0F, 0.0F)
               .putVec4(var16, var17, var18, 0.0F)
               .putVec4(this.atmosphereSaturationSetting.getValue(), var21, var20, var23)
               .putVec4(var5, var6, var22, this.powerfulFogSoftnessSetting.getValue())
               .putVec4(this.sunRayGlowSetting.getValue(), 0.0F, 0.0F, 0.0F)
               .putVec4(var34[0], var34[1], var34[2], 1.0F)
               .putVec4(var34[3], var34[4], var34[5], 1.0F)
               .putVec4(var34[6], var34[7], var34[8], 1.0F)
               .putVec4(var34[9], var34[10], var34[11], 1.0F)
               .putVec4(var34[12], var34[13], var34[14], 1.0F)
               .putVec4(var34[15], var34[16], var34[17], 1.0F)
               .get();
            var26.writeToBuffer(this.powerfulUniformBuffer.slice(), var28);
         }

         try (RenderPass var35 = var26.createRenderPass(() -> стч, var4.getColorTextureView(), Optional.empty())) {
            var35.setPipeline(this.fogOnlySetting.getValue() ? this.powerfulFogOnlyPipeline : this.powerfulAtmospherePipeline);
            var35.setUniform(сДБ, this.powerfulUniformBuffer);
            var35.bindTexture(сДС, this.sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            var35.bindTexture(сДз, var4.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            var35.draw(3, 1, 0, 0);
         }
      }
   }

   private void чм() {
      if (this.optimizedUniformBuffer == null || this.optimizedUniformBuffer.isClosed()) {
         this.optimizedUniformBuffer = RenderSystem.getDevice().createBuffer(() -> стЦ, 136, стЫ);
      }
   }

   private void Ю_de5a6f78(int var1, int var2) {
      if (this.sceneCopyTarget == null) {
         this.sceneCopyTarget = new TextureTarget(стб, var1, var2, false, GpuFormat.RGBA8_UNORM);
      } else if (this.sceneCopyTarget.width != var1 || this.sceneCopyTarget.height != var2) {
         this.sceneCopyTarget.resize(var1, var2);
      }
   }

   private void Т(FogRenderEvent var1) {
      if (var1.getCamera() != null && var1.getCamera().getFluidInCamera() == FogType.NONE) {
         var1.setDistanceOverride(сть, стж);
      }
   }

   private static float Д(float var0) {
      return Mth.clamp(var0, 0.0F, 1.0F);
   }

   public void Й(Matrix4f var1, Matrix4f var2, DeltaTracker var3) {
      if (this.Йе() && var1 != null && var2 != null && var3 != null) {
         if (this.renderModeSetting.isSelected(сДш)) {
            this.ч(var1, var2, var3);
         } else {
            RenderTarget var4 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            if (var4 != null && var4.getColorTexture() != null && var4.getDepthTexture() != null && !var4.getDepthTexture().isClosed()) {
               int var5 = Math.max(1, var4.width);
               int var6 = Math.max(1, var4.height);
               this.У(Math.max(1, (var5 + 1) / 2), Math.max(1, (var6 + 1) / 2));
               this.чм();
               float var7 = var3.getGameTimeDeltaPartialTick(false);
               Vec3 var8 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
               float var9 = this.Я(var8);
               if (MINECRAFT_CLIENT.player.onGround()) {
                  this.smoothedGroundLevel = MINECRAFT_CLIENT.level;
                  this.smoothedGroundHeight = var9;
               } else if (this.smoothedGroundLevel == MINECRAFT_CLIENT.level && Float.isFinite(this.smoothedGroundHeight) && !(Math.abs(var9 - this.smoothedGroundHeight) > сДИ)) {
                  this.smoothedGroundHeight = Mth.lerp(сДь, this.smoothedGroundHeight, var9);
               } else {
                  this.smoothedGroundLevel = MINECRAFT_CLIENT.level;
                  this.smoothedGroundHeight = var9;
               }

               if (Float.isFinite(this.smoothedCameraY) && !(Math.abs(var8.y - this.smoothedCameraY) > сДж)) {
                  this.smoothedCameraY = Mth.lerp(сД弟, this.smoothedCameraY, (float)var8.y);
               } else {
                  this.smoothedCameraY = (float)var8.y;
               }

               float var10 = MINECRAFT_CLIENT.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, var7) * сДб;
               float var11 = -Mth.sin(var10);
               float var12 = Mth.cos(var10);
               float var13 = и(сДЫ, сДl, var12);
               float var14 = this.moonHazeSetting.getValue() ? и(сД8, сД5, -var12) * сДв : 0.0F;
               boolean var15 = var13 >= var14;
               float var16 = var15 ? var11 : -var11;
               float var17 = var15 ? var12 : -var12;
               float var18 = var15 ? var13 : var14;
               boolean var19 = !this.fogOnlySetting.getValue();
               Vector4f var20 = new Vector4f(var16, var17, 0.0F, 0.0F);
               new Matrix4f(var2).transform(var20);
               new Matrix4f(var1).transform(var20);
               float var21 = сДъ;
               float var22 = сДМ;
               float var23 = 0.0F;
               if (var20.w > сДН) {
                  var21 = var20.x / var20.w * сДн + сДЦ;
                  var22 = var20.y / var20.w * сДч + сДЭ;
                  float var24 = (float)var5 / var6;
                  float var25 = (float)Math.sqrt(Math.pow((var21 - сД必) * var24, сДщ) + Math.pow(var22 - сДГ, сДл));
                  float var26 = и(сДэ, сДю, var20.w);
                  var23 = var18 * var26 * (1.0F - и(сДЩ, сДу, var25));
               }

               float var43 = ((float)(MINECRAFT_CLIENT.level.getGameTime() % сД2) + var7) / сДф;
               Vector4f var44 = ColorMath.toRgbaVector(this.useClientLightFogColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.coolLightFogColorSetting.getValue());
               Vector4f var45 = ColorMath.toRgbaVector(this.useClientLightFogColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.warmLightFogColorSetting.getValue());
               Matrix4f var27 = new Matrix4f(var1).invert();
               Matrix4f var28 = new Matrix4f(var2).invert();
               CommandEncoder var29 = RenderSystem.getDevice().createCommandEncoder();

               try (MemoryStack var30 = MemoryStack.stackPush()) {
                  ByteBuffer var31 = Std140Builder.onStack(var30, 256)
                     .putMat4f(var27)
                     .putMat4f(var28)
                     .putVec4(Я(var8.x), this.smoothedCameraY, Я(var8.z), this.smoothedGroundHeight)
                     .putVec4(var16, var17, 0.0F, var18)
                     .putVec4(var21, var22, var23, !var19 ? сДи : (this.fastRaysSetting.getValue() && this.fastRaysShadingSetting.getValue() ? 1.0F : 0.0F))
                     .putVec4(var13, var14, this.Я3_f67ab10a(), var15 ? 1.0F : 0.0F)
                     .putVec4(this.lightFogDensitySetting.getValue(), this.lightFogHeightSetting.getValue(), this.lightFogSoftnessSetting.getValue(), var19 && this.fastRaysSetting.getValue() ? this.fastRaysBrightnessSetting.getValue() : 0.0F)
                     .putVec4(var5, var6, var43, MINECRAFT_CLIENT.options.getEffectiveRenderDistance() * сД9)
                     .putVec4(var44.x, var44.y, var44.z, 1.0F)
                     .putVec4(var45.x, var45.y, var45.z, 1.0F)
                     .get();
                  var29.writeToBuffer(this.optimizedUniformBuffer.slice(), var31);

                  try (RenderPass var32 = var29.createRenderPass(() -> стщ, this.fogTarget.getColorTextureView(), Optional.empty())) {
                     var32.setPipeline(this.optimizedFogPipeline);
                     var32.bindTexture(сДо, var4.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                     var32.setUniform(сДO, this.optimizedUniformBuffer);
                     var32.draw(3, 1, 0, 0);
                  }
               }

               try (RenderPass var46 = var29.createRenderPass(() -> ст必, var4.getColorTextureView(), Optional.empty())) {
                  var46.setPipeline(this.optimizedFogCompositePipeline);
                  var46.setUniform(сД7, this.optimizedUniformBuffer);
                  var46.bindTexture(сДР, this.fogTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                  var46.draw(3, 1, 0, 0);
               }

               if (var19 && this.fastRaysSetting.getValue()) {
                  try (RenderPass var47 = var29.createRenderPass(() -> стЭ, var4.getColorTextureView(), Optional.empty())) {
                     var47.setPipeline(this.optimizedLightPipeline);
                     var47.setUniform(сД3, this.optimizedUniformBuffer);
                     var47.bindTexture(сДр, this.fogTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                     var47.draw(3, 1, 0, 0);
                  }
               }
            }
         }
      }
   }

   private static float Я(double var0) {
      double var2 = var0 % ст8;
      return (float)(var2 < 0.0 ? var2 + ст5 : var2);
   }

   private static void Я(float[] var0, int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = (float)Math.cbrt(сЪТ * method_4(var2) + сЪО * method_4(var3) + сЪФ * method_4(var4));
      float var10 = (float)Math.cbrt(сЪЙ * method_4(var2) + сЪк * method_4(var3) + сЪг * method_4(var4));
      float var11 = (float)Math.cbrt(сЪ0 * method_4(var2) + сЪх * method_4(var3) + сЪУ * method_4(var4));
      float var12 = (float)Math.cbrt(сЪЗ * method_4(var5) + сЪЕ * method_4(var6) + сЪП * method_4(var7));
      float var13 = (float)Math.cbrt(сЪ西 * method_4(var5) + сЪд * method_4(var6) + сЪБ * method_4(var7));
      float var14 = (float)Math.cbrt(сЪС * method_4(var5) + сЪз * method_4(var6) + сЪц * method_4(var7));
      float var15 = Mth.lerp(Д(var8), var9, var12);
      float var16 = Mth.lerp(Д(var8), var10, var13);
      float var17 = Mth.lerp(Д(var8), var11, var14);
      float var18 = var15 * var15 * var15;
      float var19 = var16 * var16 * var16;
      float var20 = var17 * var17 * var17;
      var0[var1] = в(сЪя * var18 - стс * var19 + стВ * var20);
      var0[var1 + 1] = в(стЯ * var18 + стХ * var19 - сте * var20);
      var0[var1 + 2] = в(стА * var18 - ст4 * var19 + стп * var20);
   }

   public FogAtmosphere() {
      super(спж, сп弟, ModuleCategory.RENDER);
      this.renderModeSetting = new SingleChoiceSetting(спб, new ChoiceOption(спЫ), new ChoiceOption(спl));
      this.fogOnlySetting = new BooleanSetting(сп8, false);
      this.lightAtmosphereStyleSetting = new SingleChoiceSetting(сп5, new ChoiceOption(спв), new ChoiceOption(спъ)).visibleWhen(() -> this.renderModeSetting.isSelected(ст_));
      this.lightFogDensitySetting = new FloatSetting(спМ, 1.0F, спН, спн).withStep(спЦ).visibleWhen(() -> this.renderModeSetting.isSelected(ст6));
      this.lightFogHeightSetting = new FloatSetting(спч, спЭ, 2.0F, сп必).withStep(1.0F).visibleWhen(() -> this.renderModeSetting.isSelected(стр));
      this.fastRaysSetting = new BooleanSetting(спщ, true).visibleWhen(() -> this.renderModeSetting.isSelected(ст3) && !this.fogOnlySetting.getValue());
      this.fastRaysBrightnessSetting = new FloatSetting(спГ, 1.0F, спл, 2.0F).withStep(спэ).visibleWhen(() -> this.renderModeSetting.isSelected(стР) && !this.fogOnlySetting.getValue() && this.fastRaysSetting.getValue());
      this.fastRaysShadingSetting = new BooleanSetting(спю, true).visibleWhen(() -> this.renderModeSetting.isSelected(ст7) && !this.fogOnlySetting.getValue() && this.fastRaysSetting.getValue());
      this.moonHazeSetting = new BooleanSetting(спЩ, true).visibleWhen(() -> this.renderModeSetting.isSelected(стO));
      this.lightFogSoftnessSetting = new FloatSetting(спу, сп2, спф, спи).withStep(сп9).visibleWhen(() -> this.renderModeSetting.isSelected(сто));
      this.useClientLightFogColorsSetting = new BooleanSetting(спо, true).visibleWhen(() -> this.renderModeSetting.isSelected(ст9));
      this.coolLightFogColorSetting = new ColorSetting(спO, false, сп7).visibleWhen(() -> this.renderModeSetting.isSelected(сти) && !this.useClientLightFogColorsSetting.getValue());
      this.warmLightFogColorSetting = new ColorSetting(спР, false, -21126).visibleWhen(() -> this.renderModeSetting.isSelected(стф) && !this.useClientLightFogColorsSetting.getValue());
      this.paletteSetting = new SingleChoiceSetting(сп3, new ChoiceOption(спр), new ChoiceOption(сп6), new ChoiceOption(сп_)).visibleWhen(() -> this.renderModeSetting.isSelected(ст2));
      this.atmosphereSaturationSetting = new FloatSetting(спШ, спы, спй, спЖ).withStep(спм).visibleWhen(() -> this.renderModeSetting.isSelected(сту));
      this.hazeUpperBoundSetting = new FloatSetting(спК, спI, спТ, спО).withStep(1.0F).visibleWhen(() -> this.renderModeSetting.isSelected(стЩ));
      this.sunRayGlowSetting = new FloatSetting(спФ, спЙ, 0.0F, 1.0F).withStep(спк).visibleWhen(() -> this.renderModeSetting.isSelected(стю) && !this.fogOnlySetting.getValue());
      this.powerfulFogSoftnessSetting = new FloatSetting(спг, сп0, 0.0F, 1.0F).withStep(спх).visibleWhen(() -> this.renderModeSetting.isSelected(стэ) && !this.fogOnlySetting.getValue());
      this.dawnLineColorSetting = new ColorSetting(спУ, false, -21126).visibleWhen(() -> this.renderModeSetting.isSelected(стГ) && this.paletteSetting.isSelected(стл));
      this.optimizedFogPipeline = RenderPipelineBuilder.builder()
         .withLocation(спЗ)
         .withVertexShader(ClientResourceIds.getCoreId(спЕ))
         .withFragmentShader(ClientResourceIds.getCoreId(спП))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(сп西, UniformType.UNIFORM_BUFFER)
         .withSampler(спд)
         .build();
      this.optimizedFogCompositePipeline = RenderPipelineBuilder.builder()
         .withLocation(спБ)
         .withVertexShader(ClientResourceIds.getCoreId(спС))
         .withFragmentShader(ClientResourceIds.getCoreId(спз))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(спц, UniformType.UNIFORM_BUFFER)
         .withSampler(спя)
         .build();
      this.optimizedLightPipeline = RenderPipelineBuilder.builder()
         .withLocation(сДс)
         .withVertexShader(ClientResourceIds.getCoreId(сДВ))
         .withFragmentShader(ClientResourceIds.getCoreId(сДЯ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(сДХ, UniformType.UNIFORM_BUFFER)
         .withSampler(сДе)
         .build();
      this.powerfulAtmospherePipeline = RenderPipelineBuilder.builder()
         .withLocation(сДА)
         .withVertexShader(ClientResourceIds.getCoreId(сД4))
         .withFragmentShader(ClientResourceIds.getCoreId(сДп))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(сДД, UniformType.UNIFORM_BUFFER)
         .withSampler(сДЪ)
         .withSampler(сДт)
         .build();
      this.powerfulFogOnlyPipeline = RenderPipelineBuilder.builder()
         .withLocation(сДа)
         .withVertexShader(ClientResourceIds.getCoreId(сДЮ))
         .withFragmentShader(ClientResourceIds.getCoreId(сДЧ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(сДЛ, UniformType.UNIFORM_BUFFER)
         .withSampler(сД诶)
         .withSampler(сД1)
         .build();
      this.smoothedGroundHeight = Float.NaN;
      this.smoothedCameraY = Float.NaN;
      this.cachedGroundHeight = Float.NaN;
      this.cachedGroundBlockX = сДЬ;
      this.cachedGroundBlockZ = сД衣;
      this.fogRenderListener = this::Т;
      this.addSettings(
         this.renderModeSetting,
         this.fogOnlySetting,
         this.lightAtmosphereStyleSetting,
         this.lightFogDensitySetting,
         this.lightFogHeightSetting,
         this.fastRaysSetting,
         this.fastRaysBrightnessSetting,
         this.fastRaysShadingSetting,
         this.moonHazeSetting,
         this.lightFogSoftnessSetting,
         this.useClientLightFogColorsSetting,
         this.coolLightFogColorSetting,
         this.warmLightFogColorSetting,
         this.paletteSetting,
         this.atmosphereSaturationSetting,
         this.hazeUpperBoundSetting,
         this.sunRayGlowSetting,
         this.powerfulFogSoftnessSetting,
         this.dawnLineColorSetting
      );
   }

   private static float method_4(float var0) {
      return var0 <= стД ? var0 / стЪ : (float)Math.pow((var0 + стт) / ста, стЮ);
   }

   private static float в(float var0) {
      var0 = Д(var0);
      return var0 <= стЧ ? var0 * стЛ : (float)(ст诶 * Math.pow(var0, ст1) - стЬ);
   }

   private static float и(float var0, float var1, float var2) {
      float var3 = Mth.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (ств - 2.0F * var3);
   }

   private float[] Й() {
      float[] var1 = new float[18];
      if (this.paletteSetting.isSelected(сДц)) {
         Я(var1, 0, сДя, сЪс, сЪВ);
         Я(var1, 3, сЪЯ, сЪХ, сЪе);
         Я(var1, 6, сЪА, сЪ4, сЪп);
         Я(var1, 9, сЪД, сЪЪ, сЪт);
         Я(var1, 12, сЪа, сЪЮ, сЪЧ);
         Я(var1, 15, сЪЛ, сЪ诶, сЪ1);
         return var1;
      }

      int var2 = this.paletteSetting.isSelected(сЪЬ) ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.dawnLineColorSetting.getValue();
      float var3 = (var2 >> 16 & 0xFF) / сЪ衣;
      float var4 = (var2 >> 8 & 0xFF) / сЪш;
      float var5 = (var2 & 0xFF) / сЪИ;
      if (this.paletteSetting.isSelected(сЪь)) {
         Я(var1, 0, сЪж, сЪ弟, сЪб, var3, var4, var5, сЪЫ);
         Я(var1, 3, var3, var4, var5, 1.0F, сЪl, сЪ8, сЪ5);
         Я(var1, 6, сЪв, сЪъ, сЪМ, var3, var4, var5, сЪН);
         Я(var1, 9, var3, var4, var5, сЪн, сЪЦ, сЪч, сЪЭ);
         Я(var1, 12, сЪ必, сЪщ, сЪГ, var3, var4, var5, сЪл);
         Я(var1, 15, var3, var4, var5, 1.0F, сЪэ, сЪю, сЪЩ);
      } else {
         Я(var1, 0, сЪу, сЪ2, сЪф, var3, var4, var5, сЪи);
         Я(var1, 3, Д(var3 * сЪ9), Д(var4 * сЪо), Д(var5 * сЪO));
         Я(var1, 6, сЪ7, сЪР, сЪ3);
         Я(var1, 9, var3, var4, var5, сЪр, сЪ6, сЪ_, сЪШ);
         Я(var1, 12, сЪы, сЪй, сЪЖ);
         Я(var1, 15, Д(var3 * сЪм), Д(var4 * сЪК), Д(var5 * сЪI));
      }

      return var1;
   }
}
