package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
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
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import settings.render.ClipDepth;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;

public class ShaderMist extends ClientModule {
   private static final int Дя = 80;
   private final @com.google.gson.annotations.SerializedName("Хх")  FloatSetting speedSetting;
   @SerializedName("ХУ")
   private final FloatSetting scaleSetting;
   private final @com.google.gson.annotations.SerializedName("ХЗ")  FloatSetting intensitySetting;
   private final @com.google.gson.annotations.SerializedName("ХЕ")  FloatSetting opacitySetting;
   private final @com.google.gson.annotations.SerializedName("ЯЬ")  BooleanSetting themeColorSetting;
   private final @com.google.gson.annotations.SerializedName("о")  ColorSetting customColorSetting;
   private final @com.google.gson.annotations.SerializedName("В4")  RenderPipeline pulsarSkyPipeline;
   private @com.google.gson.annotations.SerializedName("и")  TextureTarget sceneCopyTarget;
   @SerializedName("ХУ")
   private long effectStartedAtMillis;
   private static final String MODULE_NAME = "ShaderMist";
   private static final String MODULE_DESCRIPTION = "Шейдерное небо Pulsar";
   private static final String SPEED_LABEL = "Скорость";
   private static final float MIN_SPEED = 0.1F;
   private static final float MAX_SPEED = 5.0F;
   private static final float SPEED_STEP = 0.1F;
   private static final String SCALE_LABEL = "Размер";
   private static final float DEFAULT_SCALE = 5.0F;
   private static final float MAX_SCALE = 20.0F;
   private static final float SCALE_STEP = 0.5F;
   private static final String INTENSITY_LABEL = "Интенсивность";
   private static final float DEFAULT_INTENSITY = 0.01F;
   private static final float MIN_INTENSITY = 0.001F;
   private static final float MAX_INTENSITY = 0.05F;
   private static final float INTENSITY_STEP = 0.001F;
   private static final String OPACITY_LABEL = "Прозрачность";
   private static final float MIN_OPACITY = 0.3F;
   private static final float OPACITY_STEP = 0.05F;
   private static final String THEME_COLOR_LABEL = "Авто цвет под тему";
   private static final String CUSTOM_COLOR_LABEL = "Цвет";
   private static final int DEFAULT_CUSTOM_COLOR = -11885569;
   private static final String PULSAR_PIPELINE_LOCATION = "rose/shader_mist_pulsar";
   private static final String FULLSCREEN_VERTEX_SHADER_PATH = "hands_fullscreen";
   private static final String PULSAR_FRAGMENT_SHADER_PATH = "shader_mist_pulsar";
   private static final String PULSAR_UNIFORM_LAYOUT_NAME = "ShaderMist";
   private static final String SCENE_SAMPLER_LAYOUT_NAME = "SceneSampler";
   private static final String DEPTH_SAMPLER_LAYOUT_NAME = "DepthSampler";
   private static final long INITIAL_EFFECT_START_MILLIS = -1L;
   private static final float MILLIS_PER_SECOND = 1000.0F;
   private static final float MIN_SKY_DISTANCE_BLOCKS = 16.0F;
   private static final float BLOCKS_PER_RENDER_DISTANCE_CHUNK = 16.0F;
   private static final float SKY_MASK_NEAR_DISTANCE_SCALE = 0.98F;
   private static final float SKY_MASK_FAR_DISTANCE_SCALE = 1.05F;
   private static final float MIN_INVERTED_DEPTH_THRESHOLD_SPAN = 5.0E-4F;
   private static final String SCENE_SAMPLER_BINDING = "SceneSampler";
   private static final String DEPTH_SAMPLER_BINDING = "DepthSampler";
   private static final String PULSAR_UNIFORM_BINDING = "ShaderMist";
   private static final String SCENE_COPY_TARGET_LABEL = "ShaderMist scene";
   private static final long DISABLED_EFFECT_START_MILLIS = -1L;
   private static final String PULSAR_PASS_LABEL = "ShaderMist";
   private static final String PULSAR_UNIFORM_BUFFER_LABEL = "ShaderMist uniforms";

   public ShaderMist() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.speedSetting = new FloatSetting(SPEED_LABEL, 1.0F, MIN_SPEED, MAX_SPEED).withStep(SPEED_STEP);
      this.scaleSetting = new FloatSetting(SCALE_LABEL, DEFAULT_SCALE, 1.0F, MAX_SCALE).withStep(SCALE_STEP);
      this.intensitySetting = new FloatSetting(INTENSITY_LABEL, DEFAULT_INTENSITY, MIN_INTENSITY, MAX_INTENSITY).withStep(INTENSITY_STEP);
      this.opacitySetting = new FloatSetting(OPACITY_LABEL, 1.0F, MIN_OPACITY, 1.0F).withStep(OPACITY_STEP);
      this.themeColorSetting = new BooleanSetting(THEME_COLOR_LABEL, true);
      this.customColorSetting = new ColorSetting(CUSTOM_COLOR_LABEL, false, DEFAULT_CUSTOM_COLOR).visibleWhen(() -> !this.themeColorSetting.getValue());
      this.pulsarSkyPipeline = RenderPipelineBuilder.builder()
         .withLocation(PULSAR_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FULLSCREEN_VERTEX_SHADER_PATH))
         .withFragmentShader(ClientResourceIds.getCoreId(PULSAR_FRAGMENT_SHADER_PATH))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(PULSAR_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(SCENE_SAMPLER_LAYOUT_NAME)
         .withSampler(DEPTH_SAMPLER_LAYOUT_NAME)
         .build();
      this.effectStartedAtMillis = INITIAL_EFFECT_START_MILLIS;
      this.addSettings(this.speedSetting, this.scaleSetting, this.intensitySetting, this.opacitySetting, this.themeColorSetting, this.customColorSetting);
   }

   public void renderPulsarSky(Matrix4f projectionMatrix, Matrix4f viewRotationMatrix, DeltaTracker deltaTracker) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && projectionMatrix != null && viewRotationMatrix != null) {
         RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getDepthTexture() != null && !renderTarget.getDepthTexture().isClosed()) {
            if (this.effectStartedAtMillis < 0L) {
               this.effectStartedAtMillis = System.currentTimeMillis();
            }

            int width = Math.max(1, renderTarget.width);
            int height = Math.max(1, renderTarget.height);
            this.ensureSceneCopyTarget(width, height);
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            encoder.copyTextureToTexture(renderTarget.getColorTexture(), this.sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, width, height);
            float elapsedSeconds = (float)(System.currentTimeMillis() - this.effectStartedAtMillis) / MILLIS_PER_SECOND;
            Vector4f rgba = ColorMath.toRgbaVector(this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.customColorSetting.getValue());
            float cameraYawRadians = (float)Math.toRadians(-MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot());
            float cameraPitchRadians = (float)Math.toRadians(MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot());
            float fieldOfViewDegrees = MINECRAFT_CLIENT.options.fov().get().intValue();
            float projectionM22 = projectionMatrix.m22();
            float projectionM32 = projectionMatrix.m32();
            float skyDistanceBlocks = Math.max(MIN_SKY_DISTANCE_BLOCKS, MINECRAFT_CLIENT.options.getEffectiveRenderDistance() * BLOCKS_PER_RENDER_DISTANCE_CHUNK);
            float skyMaskDepthStart = computeInvertedDepthThreshold(skyDistanceBlocks * SKY_MASK_NEAR_DISTANCE_SCALE, projectionM22, projectionM32);
            float skyMaskDepthEnd = Math.max(computeInvertedDepthThreshold(skyDistanceBlocks * SKY_MASK_FAR_DISTANCE_SCALE, projectionM22, projectionM32), skyMaskDepthStart + MIN_INVERTED_DEPTH_THRESHOLD_SPAN);

            try (MemoryStack stack = MemoryStack.stackPush()) {
               ByteBuffer uniformData = Std140Builder.onStack(stack, 80)
                  .putVec4(width, height, elapsedSeconds, fieldOfViewDegrees)
                  .putVec4(rgba.x, rgba.y, rgba.z, this.opacitySetting.getValue())
                  .putVec4(this.speedSetting.getValue(), this.scaleSetting.getValue(), this.intensitySetting.getValue(), 0.0F)
                  .putVec4(cameraYawRadians, cameraPitchRadians, 0.0F, 0.0F)
                  .putVec4(0.0F, skyMaskDepthStart, skyMaskDepthEnd, 0.0F)
                  .get();

               try (
                  GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> PULSAR_UNIFORM_BUFFER_LABEL, 128, uniformData);
                  RenderPass renderPass = encoder.createRenderPass(() -> PULSAR_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty());
               ) {
                  renderPass.setPipeline(this.pulsarSkyPipeline);
                  renderPass.bindTexture(SCENE_SAMPLER_BINDING, this.sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                  renderPass.bindTexture(DEPTH_SAMPLER_BINDING, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  renderPass.setUniform(PULSAR_UNIFORM_BINDING, uniformBuffer);
                  renderPass.draw(3, 1, 0, 0);
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.effectStartedAtMillis = DISABLED_EFFECT_START_MILLIS;
      if (this.sceneCopyTarget != null) {
         this.sceneCopyTarget.destroyBuffers();
         this.sceneCopyTarget = null;
      }
   }

   private static float computeInvertedDepthThreshold(float distanceBlocks, float projectionM22, float projectionM32) {
      return 1.0F - ClipDepth.м(distanceBlocks, projectionM22, projectionM32, RenderSystem.getDevice().getDeviceInfo().isZZeroToOne());
   }

   public boolean isSkyEffectEnabled() {
      return this.isEnabled();
   }

   private void ensureSceneCopyTarget(int width, int height) {
      if (this.sceneCopyTarget == null) {
         this.sceneCopyTarget = new TextureTarget(SCENE_COPY_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.sceneCopyTarget.width != width || this.sceneCopyTarget.height != height) {
         this.sceneCopyTarget.resize(width, height);
      }
   }
}
