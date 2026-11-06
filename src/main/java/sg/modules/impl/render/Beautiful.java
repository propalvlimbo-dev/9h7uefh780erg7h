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
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.SceneEffectsEvent;
import sg.events.EventListener;
import sg.events.impl.ClientTickEvent;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SectionHeaderSetting;
import sg.settings.SingleChoiceSetting;

public final class Beautiful extends ClientModule {
   private static final int Дю = 208;
   private static final Vec3[] RAIN_SOUND_OFFSETS = new Vec3[]{
      new Vec3(Beautiful.NEGATIVE_X_SOUND_OFFSET_X, Beautiful.NEGATIVE_X_SOUND_OFFSET_Y, 0.0),
      new Vec3(Beautiful.POSITIVE_X_SOUND_OFFSET_X, Beautiful.POSITIVE_X_SOUND_OFFSET_Y, 0.0),
      new Vec3(0.0, Beautiful.NEGATIVE_Z_SOUND_OFFSET_Y, Beautiful.NEGATIVE_Z_SOUND_OFFSET_Z),
      new Vec3(0.0, Beautiful.POSITIVE_Z_SOUND_OFFSET_Y, Beautiful.POSITIVE_Z_SOUND_OFFSET_Z)
   };
   private final @com.google.gson.annotations.SerializedName("Ъ")  SectionHeaderSetting wetWorldSection;
   private final @com.google.gson.annotations.SerializedName("ВП")  BooleanSetting wetSurfacesSetting;
   private final @com.google.gson.annotations.SerializedName("ХГ")  FloatSetting reflectionStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Хл")  FloatSetting wetnessSetting;
   private final @com.google.gson.annotations.SerializedName("сЛ")  SingleChoiceSetting wetQualitySetting;
   private final @com.google.gson.annotations.SerializedName("т")  SectionHeaderSetting atmosphereSection;
   private final @com.google.gson.annotations.SerializedName("В西")  BooleanSetting rainSoundsSetting;
   private final @com.google.gson.annotations.SerializedName("Хэ")  FloatSetting rainVolumeSetting;
   private final @com.google.gson.annotations.SerializedName("сБ")  RenderPipeline wetWorldPipeline;
   private final @com.google.gson.annotations.SerializedName("ю")  Matrix4f viewProjectionMatrix;
   private final @com.google.gson.annotations.SerializedName("Щ")  Matrix4f inverseViewProjectionMatrix;
   private final @com.google.gson.annotations.SerializedName("Г")  float[] skyTintComponents;
   private final @com.google.gson.annotations.SerializedName("л")  float[] sunDirectionComponents;
   private final @com.google.gson.annotations.SerializedName("ХИ")  EventListener<SceneEffectsEvent> sceneEffectsListener;
   private final @com.google.gson.annotations.SerializedName("Хь")  EventListener<ClientTickEvent> clientTickListener;
   private @com.google.gson.annotations.SerializedName("у")  TextureTarget sceneBackupTarget;
   @SerializedName("9")
   private GpuBuffer wetWorldUniformBuffer;
   private final @com.google.gson.annotations.SerializedName("В")  Beautiful.RainLoopSound[] rainLoopSounds;
   private @com.google.gson.annotations.SerializedName("ДЩ")  int rainSoundRestartCooldown;
   private static final String MODULE_NAME = "Beautiful";
   private static final String MODULE_DESCRIPTION = "Мокрые поверхности, отражения и звуки дождя";
   private static final String WET_WORLD_SECTION_LABEL = "Мокрый мир";
   private static final String WET_SURFACES_LABEL = "Мокрые поверхности";
   private static final String REFLECTION_STRENGTH_LABEL = "Отражение";
   private static final float DEFAULT_REFLECTION_STRENGTH = 70.0F;
   private static final float MAX_REFLECTION_STRENGTH = 100.0F;
   private static final float REFLECTION_STRENGTH_STEP = 5.0F;
   private static final String WETNESS_LABEL = "Количество влаги";
   private static final float DEFAULT_WETNESS = 60.0F;
   private static final float MAX_WETNESS = 100.0F;
   private static final float WETNESS_STEP = 5.0F;
   private static final String WET_QUALITY_LABEL = "Качество влаги";
   private static final String LOW_QUALITY_OPTION = "Низкое";
   private static final String MEDIUM_QUALITY_OPTION = "Среднее";
   private static final String HIGH_QUALITY_OPTION = "Высокое";
   private static final String ATMOSPHERE_SECTION_LABEL = "Атмосфера";
   private static final String RAIN_SOUNDS_LABEL = "Звуки дождя";
   private static final String RAIN_VOLUME_LABEL = "Громкость дождя";
   private static final float DEFAULT_RAIN_VOLUME = 0.65F;
   private static final float RAIN_VOLUME_STEP = 0.05F;
   private static final String WET_WORLD_PIPELINE_PATH = "rose/beautiful_wet_world";
   private static final String FULLSCREEN_VERTEX_SHADER_PATH = "hands_fullscreen";
   private static final String WET_WORLD_FRAGMENT_SHADER_PATH = "beautiful_wet_world";
   private static final String PIPELINE_UNIFORM_NAME = "WetWorld";
   private static final String PIPELINE_SCENE_SAMPLER_NAME = "SceneSampler";
   private static final String PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final long ANIMATION_TIME_WRAP_MILLIS = 3600000L;
   private static final float MILLIS_PER_SECOND = 1000.0F;
   private static final String LOW_QUALITY_SELECTOR = "Низкое";
   private static final float LOW_QUALITY_RAY_STEPS = 14.0F;
   private static final String HIGH_QUALITY_SELECTOR = "Высокое";
   private static final float HIGH_QUALITY_RAY_STEPS = 40.0F;
   private static final float MEDIUM_QUALITY_RAY_STEPS = 24.0F;
   private static final float REFLECTION_PERCENT_DIVISOR = 100.0F;
   private static final float WETNESS_PERCENT_DIVISOR = 100.0F;
   private static final float RIPPLE_STRENGTH = 0.35F;
   private static final float SKYLIGHT_GLOSS = 0.45F;
   private static final float REFLECTION_HIT_THICKNESS = 0.98F;
   private static final float MAX_REFLECTION_DISTANCE = 24.0F;
   private static final String UNIFORM_BUFFER_LABEL = "Beautiful wet world";
   private static final String DRAW_UNIFORM_NAME = "WetWorld";
   private static final String DRAW_SCENE_SAMPLER_NAME = "SceneSampler";
   private static final String DRAW_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String SCENE_BACKUP_LABEL = "Beautiful scene backup";
   private static final float FALLBACK_SKY_RED = 0.45F;
   private static final float FALLBACK_SKY_GREEN = 0.6F;
   private static final float FALLBACK_SKY_BLUE = 0.9F;
   private static final float SKY_RED_BYTE_DIVISOR = 255.0F;
   private static final float SKY_GREEN_BYTE_DIVISOR = 255.0F;
   private static final float SKY_BLUE_BYTE_DIVISOR = 255.0F;
   private static final float DEGREES_TO_RADIANS = (float) (Math.PI / 180.0);
   private static final String RENDER_PASS_LABEL = "Beautiful wet world";
   private static final double NEGATIVE_X_SOUND_OFFSET_X = -4.0;
   private static final double NEGATIVE_X_SOUND_OFFSET_Y = 1.5;
   private static final double POSITIVE_X_SOUND_OFFSET_X = 4.0;
   private static final double POSITIVE_X_SOUND_OFFSET_Y = 1.5;
   private static final double NEGATIVE_Z_SOUND_OFFSET_Y = 1.5;
   private static final double NEGATIVE_Z_SOUND_OFFSET_Z = -4.0;
   private static final double POSITIVE_Z_SOUND_OFFSET_Y = 1.5;
   private static final double POSITIVE_Z_SOUND_OFFSET_Z = 4.0;

   private float[] sampleSunDirection(Camera camera, float partialTick) {
      float sunAngleRadians = 0.0F;

      try {
         sunAngleRadians = camera.attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, partialTick) * DEGREES_TO_RADIANS;
      } catch (Throwable ignoredAttributeFailure) {
      }

      this.sunDirectionComponents[0] = -Mth.sin(sunAngleRadians);
      this.sunDirectionComponents[1] = Mth.cos(sunAngleRadians);
      this.sunDirectionComponents[2] = 0.0F;
      return this.sunDirectionComponents;
   }

   private void onSceneEffects(WorldEffectsEvent worldEvent) {
      if (this.isEnabled()
         && this.wetSurfacesSetting.getValue()
         && !Rain.Пз()
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.gameRenderer != null
         && worldEvent.getViewMatrix() != null
         && worldEvent.getProjectionMatrix() != null) {
         if (!(this.reflectionStrengthSetting.getValue() <= 0.0F) || !(this.wetnessSetting.getValue() <= 0.0F)) {
            Camera camera = MINECRAFT_CLIENT.gameRenderer.mainCamera();
            if (camera != null && camera.getFluidInCamera() == FogType.NONE) {
               RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               if (renderTarget != null
                  && renderTarget.getColorTexture() != null
                  && renderTarget.getDepthTexture() != null
                  && renderTarget.getColorTextureView() != null
                  && renderTarget.getDepthTextureView() != null
                  && !renderTarget.getDepthTexture().isClosed()) {
                  this.viewProjectionMatrix.set(worldEvent.getProjectionMatrix()).mul(worldEvent.getViewMatrix());
                  this.inverseViewProjectionMatrix.set(this.viewProjectionMatrix).invert();
                  Vec3 cameraPosition = camera.position();
                  float partialTick = worldEvent.getTickDelta();
                  CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                  this.renderWetWorld(encoder, renderTarget, camera, cameraPosition, partialTick);
               }
            }
         }
      }
   }

   private static void copySceneColor(CommandEncoder encoder, RenderTarget sourceTarget, RenderTarget destinationTarget) {
      int copyWidth = Math.min(sourceTarget.width, destinationTarget.width);
      int copyHeight = Math.min(sourceTarget.height, destinationTarget.height);
      encoder.copyTextureToTexture(sourceTarget.getColorTexture(), destinationTarget.getColorTexture(), 0, 0, 0, 0, 0, copyWidth, copyHeight);
   }

   private static GpuBuffer ensureUniformBuffer(GpuBuffer existingBuffer, String label, int byteSize) {
      return existingBuffer != null && !existingBuffer.isClosed() ? existingBuffer : RenderSystem.getDevice().createBuffer(() -> label, 136, byteSize);
   }

   private static void closeBufferIfOpen(GpuBuffer buffer) {
      if (buffer != null && !buffer.isClosed()) {
         buffer.close();
      }
   }

   private void renderWetWorld(CommandEncoder encoder, RenderTarget renderTarget, Camera camera, Vec3 cameraPosition, float partialTick) {
      this.ensureSceneBackupTarget(renderTarget.width, renderTarget.height);
      copySceneColor(encoder, renderTarget, this.sceneBackupTarget);
      float animationSeconds = (float)(System.currentTimeMillis() % ANIMATION_TIME_WRAP_MILLIS) / MILLIS_PER_SECOND;
      float rayMarchSteps = this.wetQualitySetting.isSelected(LOW_QUALITY_SELECTOR) ? LOW_QUALITY_RAY_STEPS : (this.wetQualitySetting.isSelected(HIGH_QUALITY_SELECTOR) ? HIGH_QUALITY_RAY_STEPS : MEDIUM_QUALITY_RAY_STEPS);
      float[] skyTint = this.sampleSkyTint(camera, partialTick);
      float[] sunDirection = this.sampleSunDirection(camera, partialTick);
      boolean hasSkyLight = MINECRAFT_CLIENT.level.dimensionType().hasSkyLight();

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 208)
            .putMat4f(this.viewProjectionMatrix)
            .putMat4f(this.inverseViewProjectionMatrix)
            .putVec4((float)cameraPosition.x, (float)cameraPosition.y, (float)cameraPosition.z, animationSeconds)
            .putVec4(skyTint[0], skyTint[1], skyTint[2], this.reflectionStrengthSetting.getValue() / REFLECTION_PERCENT_DIVISOR)
            .putVec4(sunDirection[0], sunDirection[1], sunDirection[2], this.wetnessSetting.getValue() / WETNESS_PERCENT_DIVISOR)
            .putVec4(RIPPLE_STRENGTH, hasSkyLight ? SKYLIGHT_GLOSS : 0.0F, rayMarchSteps, REFLECTION_HIT_THICKNESS)
            .putVec4(MAX_REFLECTION_DISTANCE, 1.0F, 0.0F, 0.0F)
            .get();
         this.wetWorldUniformBuffer = ensureUniformBuffer(this.wetWorldUniformBuffer, UNIFORM_BUFFER_LABEL, 208);
         encoder.writeToBuffer(this.wetWorldUniformBuffer.slice(), uniformData);
      }

      try (RenderPass renderPass = encoder.createRenderPass(() -> RENDER_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(this.wetWorldPipeline);
         renderPass.setUniform(DRAW_UNIFORM_NAME, this.wetWorldUniformBuffer);
         renderPass.bindTexture(DRAW_SCENE_SAMPLER_NAME, this.sceneBackupTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.bindTexture(DRAW_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private void ensureSceneBackupTarget(int requestedWidth, int requestedHeight) {
      int width = Math.max(1, requestedWidth);
      int height = Math.max(1, requestedHeight);
      if (this.sceneBackupTarget == null) {
         this.sceneBackupTarget = new TextureTarget(SCENE_BACKUP_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else {
         if (this.sceneBackupTarget.width != width || this.sceneBackupTarget.height != height) {
            this.sceneBackupTarget.resize(width, height);
         }
      }
   }

   private float[] sampleSkyTint(Camera camera, float partialTick) {
      float[] skyTint = this.skyTintComponents;
      skyTint[0] = FALLBACK_SKY_RED;
      skyTint[1] = FALLBACK_SKY_GREEN;
      skyTint[2] = FALLBACK_SKY_BLUE;

      try {
         Object skyColorAttribute = camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTick);
         if (skyColorAttribute instanceof Integer packedSkyColor) {
            skyTint[0] = (packedSkyColor >> 16 & 0xFF) / SKY_RED_BYTE_DIVISOR;
            skyTint[1] = (packedSkyColor >> 8 & 0xFF) / SKY_GREEN_BYTE_DIVISOR;
            skyTint[2] = (packedSkyColor & 0xFF) / SKY_BLUE_BYTE_DIVISOR;
         } else if (skyColorAttribute instanceof Vector3f skyRgb) {
            skyTint[0] = skyRgb.x;
            skyTint[1] = skyRgb.y;
            skyTint[2] = skyRgb.z;
         } else if (skyColorAttribute instanceof Vector4f skyRgba) {
            skyTint[0] = skyRgba.x;
            skyTint[1] = skyRgba.y;
            skyTint[2] = skyRgba.z;
         }
      } catch (Throwable ignoredAttributeFailure) {
      }

      return skyTint;
   }

   public void releaseResources() {
      this.stopRainSounds();
      this.destroySceneBackupTarget();
      closeBufferIfOpen(this.wetWorldUniformBuffer);
      this.wetWorldUniformBuffer = null;
   }

   public Beautiful() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.wetWorldSection = new SectionHeaderSetting(WET_WORLD_SECTION_LABEL);
      this.wetSurfacesSetting = new BooleanSetting(WET_SURFACES_LABEL, true);
      this.reflectionStrengthSetting = new FloatSetting(REFLECTION_STRENGTH_LABEL, DEFAULT_REFLECTION_STRENGTH, 0.0F, MAX_REFLECTION_STRENGTH).withStep(REFLECTION_STRENGTH_STEP).visibleWhen(this.wetSurfacesSetting::getValue);
      this.wetnessSetting = new FloatSetting(WETNESS_LABEL, DEFAULT_WETNESS, 0.0F, MAX_WETNESS).withStep(WETNESS_STEP).visibleWhen(this.wetSurfacesSetting::getValue);
      this.wetQualitySetting = new SingleChoiceSetting(WET_QUALITY_LABEL, 1, new ChoiceOption(LOW_QUALITY_OPTION), new ChoiceOption(MEDIUM_QUALITY_OPTION), new ChoiceOption(HIGH_QUALITY_OPTION)).visibleWhen(this.wetSurfacesSetting::getValue);
      this.atmosphereSection = new SectionHeaderSetting(ATMOSPHERE_SECTION_LABEL);
      this.rainSoundsSetting = new BooleanSetting(RAIN_SOUNDS_LABEL, false);
      this.rainVolumeSetting = new FloatSetting(RAIN_VOLUME_LABEL, DEFAULT_RAIN_VOLUME, 0.0F, 1.0F).withStep(RAIN_VOLUME_STEP).visibleWhen(this.rainSoundsSetting::getValue);
      this.wetWorldPipeline = RenderPipelineBuilder.builder()
         .withLocation(WET_WORLD_PIPELINE_PATH)
         .withVertexShader(ClientResourceIds.getCoreId(FULLSCREEN_VERTEX_SHADER_PATH))
         .withFragmentShader(ClientResourceIds.getCoreId(WET_WORLD_FRAGMENT_SHADER_PATH))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(PIPELINE_SCENE_SAMPLER_NAME)
         .withSampler(PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.viewProjectionMatrix = new Matrix4f();
      this.inverseViewProjectionMatrix = new Matrix4f();
      this.skyTintComponents = new float[3];
      this.sunDirectionComponents = new float[3];
      this.sceneEffectsListener = this::onSceneEffects;
      this.clientTickListener = ignoredTickEvent -> this.updateRainSounds();
      this.rainLoopSounds = new Beautiful.RainLoopSound[RAIN_SOUND_OFFSETS.length];
      this.addSettings(this.wetWorldSection, this.wetSurfacesSetting, this.reflectionStrengthSetting, this.wetnessSetting, this.wetQualitySetting, this.atmosphereSection, this.rainSoundsSetting, this.rainVolumeSetting);
   }

   @Override
   public void onDisable() {
      this.releaseResources();
   }

   private void stopRainSounds() {
      for (int soundIndex = 0; soundIndex < this.rainLoopSounds.length; soundIndex++) {
         if (this.rainLoopSounds[soundIndex] != null) {
            this.rainLoopSounds[soundIndex].finish();
            MINECRAFT_CLIENT.getSoundManager().stop(this.rainLoopSounds[soundIndex]);
            this.rainLoopSounds[soundIndex] = null;
         }
      }

      this.rainSoundRestartCooldown = 0;
   }

   private void destroySceneBackupTarget() {
      if (this.sceneBackupTarget != null) {
         this.sceneBackupTarget.destroyBuffers();
         this.sceneBackupTarget = null;
      }
   }

   private void updateRainSounds() {
      if (this.isEnabled() && this.rainSoundsSetting.getValue() && !(this.rainVolumeSetting.getValue() <= 0.0F) && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (!MINECRAFT_CLIENT.isPaused()) {
            SoundManager soundManager = MINECRAFT_CLIENT.getSoundManager();
            if (this.rainSoundRestartCooldown > 0) {
               this.rainSoundRestartCooldown--;
            } else {
               for (int soundIndex = 0; soundIndex < this.rainLoopSounds.length; soundIndex++) {
                  if (this.rainLoopSounds[soundIndex] == null || !soundManager.isActive(this.rainLoopSounds[soundIndex])) {
                     this.rainLoopSounds[soundIndex] = new Beautiful.RainLoopSound(soundIndex);
                     soundManager.play(this.rainLoopSounds[soundIndex]);
                     this.rainSoundRestartCooldown = 20;
                  }
               }
            }
         }
      } else {
         this.stopRainSounds();
      }
   }

   private final class RainLoopSound extends AbstractTickableSoundInstance {
      private final @com.google.gson.annotations.SerializedName("Х")  ClientLevel initialLevel = MinecraftAccess.MINECRAFT_CLIENT.level;
      private final @com.google.gson.annotations.SerializedName("п")  Vec3 cameraWorldOffset;
      private static final float BASE_RAIN_PITCH = 0.88F;
      private static final float RAIN_PITCH_STEP = 0.07F;
      private static final float SUBMERGED_VOLUME_FACTOR = 0.2F;
      private static final float VOLUME_INTERPOLATION_FACTOR = 0.15F;
      private static final float RAIN_VOLUME_FACTOR = 0.38F;

      private void finish() {
         this.stop();
      }

      private RainLoopSound(int soundIndex) {
         super(SoundEvents.WEATHER_RAIN, SoundSource.MASTER, RandomSource.create());
         this.cameraWorldOffset = Beautiful.RAIN_SOUND_OFFSETS[soundIndex];
         this.looping = true;
         this.delay = 0;
         this.relative = false;
         this.attenuation = Attenuation.LINEAR;
         this.pitch = BASE_RAIN_PITCH + soundIndex * RAIN_PITCH_STEP;
         this.volume = 0.0F;
         this.updatePosition();
      }

      @Override
      public void tick() {
         if (MinecraftAccess.MINECRAFT_CLIENT.level == this.initialLevel && MinecraftAccess.MINECRAFT_CLIENT.player != null && Beautiful.this.isEnabled() && Beautiful.this.rainSoundsSetting.getValue()) {
            this.updatePosition();
            float fluidVolumeFactor = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE ? 1.0F : SUBMERGED_VOLUME_FACTOR;
            this.volume = Mth.lerp(VOLUME_INTERPOLATION_FACTOR, this.volume, Beautiful.this.rainVolumeSetting.getValue() * RAIN_VOLUME_FACTOR * fluidVolumeFactor);
         } else {
            this.stop();
         }
      }

      private void updatePosition() {
         Vec3 soundPosition = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.mainCamera().position().add(this.cameraWorldOffset);
         this.x = soundPosition.x;
         this.y = soundPosition.y;
         this.z = soundPosition.z;
      }

      @Override
      public boolean canStartSilent() {
         return true;
      }
   }
}
