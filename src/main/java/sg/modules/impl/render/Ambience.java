package sg.modules.impl.render;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.EventListener;
import sg.events.impl.ClientTickEvent;
import sg.modules.impl.misc.HpHelper;
import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.modules.impl.render.Rain;
import sg.settings.ChoiceOption;
import sg.events.impl.FogRenderEvent;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.TimeOfDaySetting;
import sg.settings.FloatSetting;
import sg.render.avatar.RainRoofHeightmap;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.AmbienceVolumeFog;
import sg.render.pipeline.NightixAurora;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;

public class Ambience extends ClientModule {
   @SerializedName("В")
   private final NightixAurora auroraRenderer = new NightixAurora();
   @SerializedName("В")
   private final AmbienceVolumeFog volumeFogRenderer = new AmbienceVolumeFog();
   @SerializedName("В")
   private final RainRoofHeightmap rainRoofHeightmap = new RainRoofHeightmap();
   private static final BlendFunction WORLD_COLOR_BLEND = new BlendFunction(BlendFactor.DST_COLOR, BlendFactor.SRC_COLOR, BlendFactor.ZERO, BlendFactor.ONE);
   private static final int ю = 240;
   private static final int Щ = 80;
   private static final int у = 160;
   private static final int field_2 = 32;
   private static final int ф = 48;
   private static final int и = 144;
   private static final long Р_6da43b94 = 131072L;
   private static final int field_9 = 1;
   private static final int о = 64;
   private static final int O = 8192;
   private static final int field_7 = 400;
   private static final int Р_a83dd0cc = 2;
   @SerializedName("е")
   private final RenderPipeline snowParticlePipeline;
   @SerializedName("А")
   private final RenderPipeline rainParticlePipeline;
   @SerializedName("4")
   private final RenderPipeline rainGroundPipeline;
   @SerializedName("п")
   private final RenderPipeline snowGroundPipeline;
   @SerializedName("Д")
   private final RenderPipeline blizzardPipeline;
   @SerializedName("Ъ")
   private final RenderPipeline shaderFogPipeline;
   @SerializedName("т")
   private final RenderPipeline pulsarSkyPipeline;
   @SerializedName("а")
   private final RenderPipeline galaxySkyPipeline;
   @SerializedName("Ю")
   private final RenderPipeline juiceSkyPipeline;
   @SerializedName("Ч")
   private final RenderPipeline skyCompositePipeline;
   @SerializedName("Л")
   private final RenderPipeline worldColorPipeline;
   @SerializedName("诶")
   private final RenderPipeline lightningPipeline;
   @SerializedName("1")
   private final RenderPipeline lightningCorePipeline;
   @SerializedName("Ь")
   private final RenderPipeline lightningFlashPipeline;
   @SerializedName("衣")
   private final RenderPipeline weatherGroundCompositePipeline;
   @SerializedName("ш")
   private final RenderPipeline lightningGlowMaskPipeline;
   @SerializedName("И")
   private final RenderPipeline lightningGlowBlurPipeline;
   @SerializedName("ь")
   private final RenderPipeline lightningGlowCompositePipeline;
   @SerializedName("В")
   private GpuBuffer worldColorUniformBuffer;
   @SerializedName("3")
   private int cachedWorldTintArgb;
   private @com.google.gson.annotations.SerializedName("св")  float cachedWorldTintStrength;
   @SerializedName("Х")
   private TextureTarget shaderSkyTarget;
   @SerializedName("Я")
   private GpuBuffer shaderFogUniformBuffer;
   @SerializedName("Х")
   private GpuBuffer shaderMistUniformBuffer;
   @SerializedName("е")
   private GpuBuffer juiceSkyUniformBuffer;
   @SerializedName("А")
   private GpuBuffer weatherUniformBuffer;
   @SerializedName("е")
   private TextureTarget weatherGroundTarget;
   @SerializedName("4")
   private GpuBuffer unitQuadVertexBuffer;
   @SerializedName("р")
   private int unitQuadIndexCount;
   @SerializedName("3")
   private long shaderSkyStartMillis;
   @SerializedName("А")
   private TextureTarget lightningMaskTarget;
   @SerializedName("4")
   private TextureTarget lightningGlowPingTarget;
   @SerializedName("п")
   private TextureTarget lightningGlowPongTarget;
   @SerializedName("п")
   private GpuBuffer lightningGlowHorizontalUniformBuffer;
   @SerializedName("Д")
   private GpuBuffer lightningGlowVerticalUniformBuffer;
   @SerializedName("Ъ")
   private GpuBuffer lightningGlowCompositeUniformBuffer;
   @SerializedName("т")
   private GpuBuffer lightningFlashUniformBuffer;
   @SerializedName("В")
   private final GpuBuffer[] lightningLayerVertexBuffers;
   @SerializedName("В")
   private final long[] lightningLayerVertexCapacities;
   @SerializedName("Я")
   private final GpuBuffer[] lightningLayerUniformBuffers;
   @SerializedName("Ъ")
   private final List<Ambience.LightningSegment> lightningSegments;
   @SerializedName("Я")
   private final Matrix4f skyInverseProjectionMatrix;
   @SerializedName("Х")
   private final Matrix4f skyInverseViewMatrix;
   @SerializedName("е")
   private final Matrix4f weatherInverseProjectionMatrix;
   @SerializedName("А")
   private final Matrix4f weatherInverseViewMatrix;
   @SerializedName("б")
   private double weatherElapsedSeconds;
   @SerializedName("р")
   private long previousWeatherFrameNanos;
   @SerializedName("Ы")
   private double weatherVerticalOffset;
   @SerializedName("l")
   private double weatherWindOffsetX;
   @SerializedName("8")
   private double weatherWindOffsetZ;
   @SerializedName("5")
   private double weatherFallPhase;
   @SerializedName("6")
   private long lightningStartNanos;
   @SerializedName("_")
   private long nextLightningNanos;
   @SerializedName("е")
   private Vec3 lightningTopWorldPosition;
   @SerializedName("А")
   private Vec3 lightningGroundWorldPosition;
   @SerializedName("6")
   private int thunderDelayTicks;
   @SerializedName("4")
   private Vec3 pendingThunderWorldPosition;
   @SerializedName("В")
   private Ambience.ThunderSound thunderSound;
   @SerializedName("Х")
   private final SingleChoiceSetting fogModeSetting;
   @SerializedName("4")
   private final FloatSetting fogEndSetting;
   @SerializedName("п")
   private final FloatSetting fogStartSetting;
   @SerializedName("Ю")
   private final BooleanSetting useThemeColorsSetting;
   @SerializedName("Я")
   private final ColorSetting fogColorSetting;
   @SerializedName("е")
   private final SingleChoiceSetting skyShaderSetting;
   @SerializedName("Д")
   private final FloatSetting shaderIntensitySetting;
   @SerializedName("Ъ")
   private final FloatSetting shaderSpeedSetting;
   @SerializedName("Ч")
   private final BooleanSetting starsSetting;
   @SerializedName("т")
   private final FloatSetting skySpeedSetting;
   @SerializedName("а")
   private final FloatSetting skySizeSetting;
   @SerializedName("Ю")
   private final FloatSetting skyIntensitySetting;
   @SerializedName("Ч")
   private final FloatSetting skyOpacitySetting;
   @SerializedName("Л")
   private final FloatSetting auroraStrengthSetting;
   @SerializedName("诶")
   private final FloatSetting auroraSpeedSetting;
   @SerializedName("1")
   private final FloatSetting auroraScaleSetting;
   @SerializedName("Ь")
   private final FloatSetting auroraStarsSetting;
   @SerializedName("Х")
   private final ColorSetting auroraPrimaryColorSetting;
   @SerializedName("е")
   private final ColorSetting auroraSecondaryColorSetting;
   @SerializedName("Л")
   private final BooleanSetting vanillaSkySetting;
   @SerializedName("А")
   private final SingleChoiceSetting weatherModeSetting;
   @SerializedName("В")
   private final TimeOfDaySetting timeOfDaySetting;
   @SerializedName("4")
   private final SingleChoiceSetting worldColorModeSetting;
   @SerializedName("А")
   private final ColorSetting worldTintSetting;
   @SerializedName("衣")
   private final FloatSetting worldTintStrengthSetting;
   @SerializedName("ш")
   private final FloatSetting weatherIntensitySetting;
   @SerializedName("И")
   private final FloatSetting weatherDistanceSetting;
   @SerializedName("ь")
   private final FloatSetting weatherFallSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("ж")  FloatSetting weatherParticleSizeSetting;
   private final @com.google.gson.annotations.SerializedName("弟")  FloatSetting windSpeedSetting;
   @SerializedName("б")
   private final FloatSetting windDirectionSetting;
   @SerializedName("诶")
   private final BooleanSetting weatherGroundEffectsSetting;
   @SerializedName("Ы")
   private final FloatSetting groundEffectDurationSetting;
   @SerializedName("l")
   private final FloatSetting groundEffectSizeSetting;
   @SerializedName("4")
   private final ColorSetting rainColorSetting;
   @SerializedName("п")
   private final ColorSetting snowColorSetting;
   @SerializedName("1")
   private final BooleanSetting lightningSetting;
   @SerializedName("8")
   private final FloatSetting lightningFrequencySetting;
   @SerializedName("5")
   private final FloatSetting lightningCountSetting;
   @SerializedName("Ь")
   private final BooleanSetting thunderSoundsSetting;
   @SerializedName("в")
   private final FloatSetting thunderVolumeSetting;
   @SerializedName("衣")
   private final BooleanSetting removeCloudsSetting;
   @SerializedName("1")
   private final EventListener<FogRenderEvent> fogRenderListener;
   @SerializedName("Ь")
   private final EventListener<ClientTickEvent> clientTickListener;
   private static final String MODULE_NAME = "Ambience";
   private static final String MODULE_DESCRIPTION = "Изменение погоды мира";
   private static final String SNOW_PARTICLE_PIPELINE_LOCATION_ID = "rose/weather_particles";
   private static final String SNOW_PARTICLE_PIPELINE_VERTEX_SHADER_ID = "weather_particles";
   private static final String SNOW_PARTICLE_PIPELINE_FRAGMENT_SHADER_ID = "weather_particles";
   private static final String SNOW_PARTICLE_PIPELINE_WEATHER_UNIFORM_NAME = "Weather";
   private static final String SNOW_PARTICLE_PIPELINE_FOG_UNIFORM_NAME = "Fog";
   private static final String RAIN_PARTICLE_PIPELINE_LOCATION_ID = "rose/weather_rain";
   private static final String RAIN_PARTICLE_PIPELINE_VERTEX_SHADER_ID = "weather_particles";
   private static final String RAIN_PARTICLE_PIPELINE_FRAGMENT_SHADER_ID = "weather_particles";
   private static final String RAIN_PARTICLE_PIPELINE_ROSE_RAIN_SHADER_DEFINE = "ROSE_RAIN";
   private static final String RAIN_PARTICLE_PIPELINE_WEATHER_UNIFORM_NAME = "Weather";
   private static final String RAIN_PARTICLE_PIPELINE_FOG_UNIFORM_NAME = "Fog";
   private static final String RAIN_PARTICLE_PIPELINE_RAIN_ROOF_UNIFORM_NAME = "RainRoof";
   private static final String RAIN_PARTICLE_PIPELINE_ROOF_SAMPLER_NAME = "RoofSampler";
   private static final String RAIN_GROUND_PIPELINE_LOCATION_ID = "rose/weather_rain_ground";
   private static final String RAIN_GROUND_PIPELINE_VERTEX_SHADER_ID = "weather_ground";
   private static final String RAIN_GROUND_PIPELINE_FRAGMENT_SHADER_ID = "weather_ground";
   private static final String RAIN_GROUND_PIPELINE_ROSE_RAIN_SHADER_DEFINE = "ROSE_RAIN";
   private static final String RAIN_GROUND_PIPELINE_WEATHER_UNIFORM_NAME = "Weather";
   private static final String RAIN_GROUND_PIPELINE_FOG_UNIFORM_NAME = "Fog";
   private static final String RAIN_GROUND_PIPELINE_RAIN_ROOF_UNIFORM_NAME = "RainRoof";
   private static final String RAIN_GROUND_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RAIN_GROUND_PIPELINE_ROOF_SAMPLER_NAME = "RoofSampler";
   private static final String SNOW_GROUND_PIPELINE_LOCATION_ID = "rose/weather_ground";
   private static final String SNOW_GROUND_PIPELINE_VERTEX_SHADER_ID = "weather_ground";
   private static final String SNOW_GROUND_PIPELINE_FRAGMENT_SHADER_ID = "weather_ground";
   private static final String SNOW_GROUND_PIPELINE_WEATHER_UNIFORM_NAME = "Weather";
   private static final String SNOW_GROUND_PIPELINE_FOG_UNIFORM_NAME = "Fog";
   private static final String SNOW_GROUND_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String BLIZZARD_PIPELINE_LOCATION_ID = "rose/ambience_blizzard";
   private static final String BLIZZARD_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String BLIZZARD_PIPELINE_FRAGMENT_SHADER_ID = "ambience_blizzard";
   private static final String BLIZZARD_PIPELINE_BLIZZARD_UNIFORM_NAME = "Blizzard";
   private static final String BLIZZARD_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String SHADER_FOG_PIPELINE_LOCATION_ID = "rose/ambience_shader_fog";
   private static final String SHADER_FOG_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String SHADER_FOG_PIPELINE_FRAGMENT_SHADER_ID = "ambience_shader_fog";
   private static final String SHADER_FOG_PIPELINE_SHADER_FOG_UNIFORM_NAME = "ShaderFog";
   private static final String SHADER_FOG_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String PULSAR_SKY_PIPELINE_LOCATION_ID = "rose/shader_mist_pulsar";
   private static final String PULSAR_SKY_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String PULSAR_SKY_PIPELINE_FRAGMENT_SHADER_ID = "shader_mist_pulsar";
   private static final String PULSAR_SKY_PIPELINE_SHADER_MIST_UNIFORM_NAME = "ShaderMist";
   private static final String PULSAR_SKY_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String GALAXY_SKY_PIPELINE_LOCATION_ID = "rose/shader_mist_galaxy";
   private static final String GALAXY_SKY_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String GALAXY_SKY_PIPELINE_FRAGMENT_SHADER_ID = "shader_mist_galaxy";
   private static final String GALAXY_SKY_PIPELINE_SHADER_MIST_UNIFORM_NAME = "ShaderMist";
   private static final String GALAXY_SKY_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String JUICE_SKY_PIPELINE_LOCATION_ID = "rose/shader_mist_juice";
   private static final String JUICE_SKY_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String JUICE_SKY_PIPELINE_FRAGMENT_SHADER_ID = "shader_mist_juice";
   private static final String JUICE_SKY_PIPELINE_SKY_SHADER_DATA_UNIFORM_NAME = "SkyShaderData";
   private static final String JUICE_SKY_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String SKY_COMPOSITE_PIPELINE_LOCATION_ID = "rose/ambience_shader_sky_composite";
   private static final String SKY_COMPOSITE_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String SKY_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID = "ambience_shader_sky_composite";
   private static final String SKY_COMPOSITE_PIPELINE_SKY_SAMPLER_NAME = "SkySampler";
   private static final String SKY_COMPOSITE_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String WORLD_COLOR_PIPELINE_LOCATION_ID = "rose/ambience_world_color";
   private static final String WORLD_COLOR_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String WORLD_COLOR_PIPELINE_FRAGMENT_SHADER_ID = "ambience_world_color";
   private static final String WORLD_COLOR_PIPELINE_WORLD_COLOR_UNIFORM_NAME = "WorldColor";
   private static final String LIGHTNING_PIPELINE_LOCATION_ID = "rose/ambience_lightning";
   private static final String LIGHTNING_PIPELINE_VERTEX_SHADER_ID = "ambience_lightning";
   private static final String LIGHTNING_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning";
   private static final String LIGHTNING_PIPELINE_LIGHTNING_UNIFORM_NAME = "Lightning";
   private static final String LIGHTNING_CORE_PIPELINE_LOCATION_ID = "rose/ambience_lightning_core";
   private static final String LIGHTNING_CORE_PIPELINE_VERTEX_SHADER_ID = "ambience_lightning";
   private static final String LIGHTNING_CORE_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning_core";
   private static final String LIGHTNING_CORE_PIPELINE_LIGHTNING_UNIFORM_NAME = "Lightning";
   private static final String LIGHTNING_FLASH_PIPELINE_LOCATION_ID = "rose/ambience_lightning_flash";
   private static final String LIGHTNING_FLASH_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String LIGHTNING_FLASH_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning_flash";
   private static final String LIGHTNING_FLASH_PIPELINE_LIGHTNING_FLASH_UNIFORM_NAME = "LightningFlash";
   private static final String LIGHTNING_FLASH_PIPELINE_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String WEATHER_GROUND_COMPOSITE_PIPELINE_LOCATION_ID = "rose/weather_ground_composite";
   private static final String WEATHER_GROUND_COMPOSITE_PIPELINE_VERTEX_SHADER_ID = "hands_pass";
   private static final String WEATHER_GROUND_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID = "weather_ground_composite";
   private static final String WEATHER_GROUND_COMPOSITE_PIPELINE_SAMPLER0_NAME = "Sampler0";
   private static final String LIGHTNING_GLOW_MASK_PIPELINE_LOCATION_ID = "rose/ambience_lightning_glow_mask";
   private static final String LIGHTNING_GLOW_MASK_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String LIGHTNING_GLOW_MASK_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning_glow_mask";
   private static final String LIGHTNING_GLOW_MASK_PIPELINE_IN_SAMPLER_NAME = "InSampler";
   private static final String LIGHTNING_GLOW_BLUR_PIPELINE_LOCATION_ID = "rose/ambience_lightning_glow_blur";
   private static final String LIGHTNING_GLOW_BLUR_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String LIGHTNING_GLOW_BLUR_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning_glow_blur";
   private static final String LIGHTNING_GLOW_BLUR_PIPELINE_LIGHTNING_GLOW_BLUR_UNIFORM_NAME = "LightningGlowBlur";
   private static final String LIGHTNING_GLOW_BLUR_PIPELINE_IN_SAMPLER_NAME = "InSampler";
   private static final String LIGHTNING_GLOW_COMPOSITE_PIPELINE_LOCATION_ID = "rose/ambience_lightning_glow_composite";
   private static final String LIGHTNING_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER_ID = "hands_fullscreen";
   private static final String LIGHTNING_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID = "ambience_lightning_glow_composite";
   private static final String LIGHTNING_GLOW_COMPOSITE_PIPELINE_LIGHTNING_GLOW_COMPOSITE_UNIFORM_NAME = "LightningGlowComposite";
   private static final String LIGHTNING_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final int Вхз = Integer.MIN_VALUE;
   private static final long Вхц = -1L;
   private static final String FOG_MODE_SETTING_LABEL = "Туман";
   private static final String FOG_MODE_SETTING_OPTION_NO_CHANGE_LABEL = "Ничего не делать";
   private static final String FOG_MODE_SETTING_OPTION_CLEAR_LABEL = "Очистить";
   private static final String FOG_MODE_SETTING_OPTION_OVERRIDE_LABEL = "Переопределить";
   private static final String FOG_END_SETTING_LABEL = "Конец тумана";
   private static final float ВУе = 0.1F;
   private static final float ВУА = 1.5F;
   private static final float ВУ4 = 0.1F;
   private static final String FOG_START_SETTING_LABEL = "Начало тумана";
   private static final float ВУД = 0.5F;
   private static final float ВУЪ = 0.1F;
   private static final float ВУт = 1.4F;
   private static final float ВУа = 0.1F;
   private static final String USE_THEME_COLORS_SETTING_LABEL = "Авто цвет под тему";
   private static final String FOG_COLOR_SETTING_LABEL = "Цвет тумана";
   private static final String SKY_SHADER_SETTING_LABEL = "Шейдер неба";
   private static final String SKY_SHADER_SETTING_OPTION_NONE_LABEL = "Ничего";
   private static final String SKY_SHADER_SETTING_OPTION_PULSAR_LABEL = "Пульсар";
   private static final String SKY_SHADER_SETTING_OPTION_GALAXY_LABEL = "Галактика";
   private static final String SKY_SHADER_SETTING_OPTION_JUICE_LABEL = "Сок";
   private static final String SKY_SHADER_SETTING_OPTION_SPACE_LABEL = "Космос";
   private static final String SKY_SHADER_SETTING_OPTION_MILKY_WAY_LABEL = "Млечный Путь";
   private static final String SKY_SHADER_SETTING_OPTION_PURPLE_LABEL = "Purple";
   private static final String SKY_SHADER_SETTING_OPTION_AURORA_LABEL = "Аврора";
   private static final String SKY_SHADER_SETTING_OPTION_AURORA2_LABEL = "Аврора 2";
   private static final String SKY_SHADER_SETTING_OPTION_NEBULOSITY_LABEL = "Туманность";
   private static final String SKY_SHADER_SETTING_OPTION_PLASMA_LABEL = "Плазма";
   private static final String SKY_SHADER_SETTING_OPTION_SOURCE_LABEL = "Источник";
   private static final String SKY_SHADER_SETTING_OPTION_NEBULA_LABEL = "Небула";
   private static final String SHADER_INTENSITY_SETTING_LABEL = "Интенсивность шейдера";
   private static final float ВУв = 1.5F;
   private static final float ВУъ = 0.05F;
   private static final String SHADER_SPEED_SETTING_LABEL = "Скорость шейдера";
   private static final float ВУН = 0.1F;
   private static final float ВУн = 5.0F;
   private static final float ВУЦ = 0.05F;
   private static final String STARS_SETTING_LABEL = "Звёзды";
   private static final String SKY_SPEED_SETTING_LABEL = "Скорость неба";
   private static final float ВУ必 = 0.1F;
   private static final float ВУщ = 5.0F;
   private static final float ВУГ = 0.1F;
   private static final String SKY_SIZE_SETTING_LABEL = "Размер неба";
   private static final float ВУэ = 5.0F;
   private static final float ВУю = 20.0F;
   private static final float ВУЩ = 0.5F;
   private static final String SKY_INTENSITY_SETTING_LABEL = "Интенсивность неба";
   private static final float ВУ2 = 0.01F;
   private static final float ВУф = 0.001F;
   private static final float ВУи = 0.05F;
   private static final float ВУ9 = 0.001F;
   private static final String SKY_OPACITY_SETTING_LABEL = "Прозрачность неба";
   private static final float ВУO = 0.3F;
   private static final float ВУ7 = 0.05F;
   private static final String AURORA_STRENGTH_SETTING_LABEL = "Сила Авроры";
   private static final float ВУ3 = 0.1F;
   private static final float ВУр = 0.05F;
   private static final String AURORA_SPEED_SETTING_LABEL = "Скорость Авроры";
   private static final float ВУ_ = 0.1F;
   private static final float ВУШ = 3.0F;
   private static final float ВУы = 0.1F;
   private static final String AURORA_SCALE_SETTING_LABEL = "Масштаб Авроры";
   private static final float ВУЖ = 1.1F;
   private static final float ВУм = 0.45F;
   private static final float ВУК = 3.0F;
   private static final float ВУI = 0.05F;
   private static final String AURORA_STARS_SETTING_LABEL = "Звёзды Авроры";
   private static final float ВУО = 0.75F;
   private static final float ВУФ = 0.05F;
   private static final String AURORA_PRIMARY_COLOR_SETTING_LABEL = "Цвет Авроры 1";
   private static final int ВУк = -8788225;
   private static final String AURORA_SECONDARY_COLOR_SETTING_LABEL = "Цвет Авроры 2";
   private static final int ВУ0 = -34074;
   private static final String VANILLA_SKY_SETTING_LABEL = "Ванильное небо";
   private static final String WEATHER_MODE_SETTING_LABEL = "Погода";
   private static final String WEATHER_MODE_SETTING_OPTION_NO_CHANGE_LABEL = "Ничего не делать";
   private static final String WEATHER_MODE_SETTING_OPTION_CLEAR_LABEL = "Очистить";
   private static final String WEATHER_MODE_SETTING_OPTION_RAIN_LABEL = "Дождь";
   private static final String WEATHER_MODE_SETTING_OPTION_SNOW_LABEL = "Снег";
   private static final String WEATHER_MODE_SETTING_OPTION_BLIZZARD_LABEL = "Пурга";
   private static final String TIME_OF_DAY_SETTING_LABEL = "Время";
   private static final String TIME_OF_DAY_SETTING_OPTION_NORMAL_LABEL = "Обычное";
   private static final String TIME_OF_DAY_SETTING_OPTION_MORNING_LABEL = "Утро";
   private static final String TIME_OF_DAY_SETTING_OPTION_DAY_LABEL = "День";
   private static final String TIME_OF_DAY_SETTING_OPTION_EVENING_LABEL = "Вечер";
   private static final String TIME_OF_DAY_SETTING_OPTION_SUNSET_LABEL = "Закат";
   private static final String TIME_OF_DAY_SETTING_OPTION_NIGHT_LABEL = "Ночь";
   private static final String WORLD_COLOR_MODE_SETTING_LABEL = "Цвет мира";
   private static final String WORLD_COLOR_MODE_SETTING_OPTION_NONE_LABEL = "Ничего";
   private static final String WORLD_COLOR_MODE_SETTING_OPTION_THEME_LABEL = "Тема";
   private static final String WORLD_COLOR_MODE_SETTING_OPTION_CUSTOM_LABEL = "Свой";
   private static final String WORLD_TINT_SETTING_LABEL = "Оттенок мира";
   private static final String WORLD_TINT_STRENGTH_SETTING_LABEL = "Сила цвета мира";
   private static final float ВЗД = 0.08F;
   private static final float ВЗЪ = 0.25F;
   private static final float ВЗт = 0.01F;
   private static final String WEATHER_INTENSITY_SETTING_LABEL = "Интенсивность погоды";
   private static final float ВЗЮ = 0.65F;
   private static final float ВЗЧ = 0.1F;
   private static final float ВЗЛ = 0.05F;
   private static final String WEATHER_DISTANCE_SETTING_LABEL = "Дальность погоды";
   private static final float ВЗ1 = 32.0F;
   private static final float ВЗЬ = 12.0F;
   private static final float ВЗ衣 = 64.0F;
   private static final String WEATHER_FALL_SPEED_SETTING_LABEL = "Скорость падения";
   private static final float ВЗИ = 0.25F;
   private static final float ВЗь = 0.05F;
   private static final String WEATHER_PARTICLE_SIZE_SETTING_LABEL = "Размер частиц погоды";
   private static final float ВЗ弟 = 0.5F;
   private static final float ВЗб = 0.05F;
   private static final String WIND_SPEED_SETTING_LABEL = "Скорость ветра";
   private static final float ВЗl = 1.5F;
   private static final float ВЗ8 = 12.0F;
   private static final float ВЗ5 = 0.1F;
   private static final String WIND_DIRECTION_SETTING_LABEL = "Направление ветра";
   private static final float ВЗъ = -180.0F;
   private static final float ВЗМ = 180.0F;
   private static final float ВЗН = 5.0F;
   private static final String WEATHER_GROUND_EFFECTS_SETTING_LABEL = "Эффекты погоды на земле";
   private static final String GROUND_EFFECT_DURATION_SETTING_LABEL = "Время эффекта на земле";
   private static final float ВЗч = 0.6F;
   private static final float ВЗЭ = 0.15F;
   private static final float ВЗ必 = 1.5F;
   private static final float ВЗщ = 0.05F;
   private static final String GROUND_EFFECT_SIZE_SETTING_LABEL = "Размер эффекта на земле";
   private static final float ВЗл = 0.45F;
   private static final float ВЗэ = 0.15F;
   private static final float ВЗю = 1.2F;
   private static final float ВЗЩ = 0.05F;
   private static final String RAIN_COLOR_SETTING_LABEL = "Цвет дождя";
   private static final int ВЗ2 = -4799020;
   private static final String SNOW_COLOR_SETTING_LABEL = "Цвет снега";
   private static final String LIGHTNING_SETTING_LABEL = "Молнии";
   private static final String LIGHTNING_FREQUENCY_SETTING_LABEL = "Скорость появления";
   private static final float ВЗо = 0.25F;
   private static final float ВЗO = 4.0F;
   private static final float ВЗ7 = 0.05F;
   private static final String LIGHTNING_COUNT_SETTING_LABEL = "Количество молний";
   private static final float ВЗ3 = 6.0F;
   private static final String THUNDER_SOUNDS_SETTING_LABEL = "Звуки грома";
   private static final String THUNDER_VOLUME_SETTING_LABEL = "Громкость грома";
   private static final float ВЗ_ = 0.5F;
   private static final float ВЗШ = 0.05F;
   private static final String REMOVE_CLOUDS_SETTING_LABEL = "Удалять Облака";
   private static final String SETTINGS_OBJECT_PROBE_KEY = "Settings";
   private static final String SETTINGS_OBJECT_READ_KEY = "Settings";
   private static final String LEGACY_SHADER_SKY_PROBE_KEY = "Шейдерное небо";
   private static final String LEGACY_SHADER_SKY_READ_KEY = "Шейдерное небо";
   private static final String LEGACY_SHADER_SKY_ENABLED_PULSAR_LABEL = "Пульсар";
   private static final String LEGACY_SHADER_FOG_PROBE_KEY = "Шейдер тумана";
   private static final String LEGACY_SHADER_FOG_READ_KEY = "Шейдер тумана";
   private static final String LEGACY_SHADER_FOG_NONE_LABEL = "Ничего";
   private static final String IS_FOG_CLEAR_ENABLED_CLEAR_OPTION_LABEL = "Очистить";
   private static final String IS_FOG_OVERRIDE_ENABLED_OVERRIDE_OPTION_LABEL = "Переопределить";
   private static final String IS_WEATHER_OVERRIDE_ENABLED_NO_CHANGE_OPTION_LABEL = "Ничего не делать";
   private static final String IS_RAIN_OVERRIDE_ENABLED_RAIN_OPTION_LABEL = "Дождь";
   private static final float ВЗх = 0.4F;
   private static final String HAS_PRECIPITATION_SELECTION_RAIN_OPTION_LABEL = "Дождь";
   private static final String HAS_PRECIPITATION_SELECTION_SNOW_OPTION_LABEL = "Снег";
   private static final String HAS_PRECIPITATION_SELECTION_BLIZZARD_OPTION_LABEL = "Пурга";
   private static final String IS_BLIZZARD_FOG_SELECTED_BLIZZARD_OPTION_LABEL = "Пурга";
   private static final String IS_BLIZZARD_FOG_SELECTED_NO_CHANGE_OPTION_LABEL = "Ничего не делать";
   private static final float ВЗд = 1.0E7F;
   private static final float ВЗБ = 1.0E7F;
   private static final float ВЗС = 16.0F;
   private static final float ВЗз = 1.0E7F;
   private static final float ВЗц = 1.0000001E7F;
   private static final int ВЗя = -16777216;
   private static final float ВЕс = 0.1F;
   private static final float ВЕВ = 0.85F;
   private static final float ВЕЯ = 0.38F;
   private static final float ВЕХ = 0.12F;
   private static final int ВЕе = -3418914;
   private static final float ВЕА = 96.0F;
   private static final float ВЕ4 = 16.0F;
   private static final float ВЕп = 16.0F;
   private static final float ВЕД = 4.0F;
   private static final float ВЕЪ = 8.0F;
   private static final long ВЕт = 240000L;
   private static final float ВЕа = 20.0F;
   private static final long ВЕЮ = -1L;
   private static final String RENDER_WORLD_COLOR_THEME_OPTION_LABEL = "Тема";
   private static final int ВЕЛ = -16777216;
   private static final float ВЕ诶 = 0.2126F;
   private static final float ВЕ1 = 0.7152F;
   private static final float ВЕЬ = 0.0722F;
   private static final float ВЕ衣 = 0.08F;
   private static final float ВЕш = 0.32F;
   private static final float ВЕИ = 0.32F;
   private static final float ВЕь = 0.32F;
   private static final String RENDER_WORLD_COLOR_WORLD_COLOR_UNIFORM_NAME = "WorldColor";
   private static final String CAN_RENDER_WORLD_COLOR_NONE_OPTION_LABEL = "Ничего";
   private static final long ВЕб = 16L;
   private static final int ВЕЫ = Integer.MIN_VALUE;
   private static final int ВЕl = Integer.MIN_VALUE;
   private static final float ВЕ8 = 0.25F;
   private static final float ВЕ5 = 0.25F;
   private static final float ВЕв = 0.25F;
   private static final double ВЕъ = 255.0;
   private static final String RENDER_SHADER_SKY_SKY_SAMPLER_NAME = "SkySampler";
   private static final String RENDER_SHADER_SKY_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final float ВЕн = 1000.0F;
   private static final float ВЕЦ = 0.999999F;
   private static final String RENDER_SHADER_MIST_SKY_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_SHADER_MIST_SKY_SHADER_MIST_UNIFORM_NAME = "ShaderMist";
   private static final float ВЕ必 = 1000.0F;
   private static final int ВЕщ = -16777216;
   private static final int ВЕГ = -16777216;
   private static final float ВЕл = 0.5F;
   private static final int ВЕэ = -16777216;
   private static final String RENDER_JUICE_SKY_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_JUICE_SKY_SKY_SHADER_DATA_UNIFORM_NAME = "SkyShaderData";
   private static final String SHADER_SKY_TARGET_DEBUG_LABEL = "Ambience shader sky layer";
   private static final long ВЕ2 = 80L;
   private static final long ВЕф = 240L;
   private static final long ВЕи = 160L;
   private static final long ВЕ9 = -1L;
   private static final long ВЕо = 240000L;
   private static final float ВЕO = 20.0F;
   private static final String RENDER_SHADER_FOG_SKY_SOURCE_OPTION_LABEL = "Источник";
   private static final String RENDER_SHADER_FOG_SKY_NEBULA_OPTION_LABEL = "Небула";
   private static final String RENDER_SHADER_FOG_SKY_SPACE_OPTION_LABEL = "Космос";
   private static final String RENDER_SHADER_FOG_SKY_PURPLE_OPTION_LABEL = "Purple";
   private static final String RENDER_SHADER_FOG_SKY_AURORA_OPTION_LABEL = "Аврора";
   private static final String RENDER_SHADER_FOG_SKY_PLASMA_OPTION_LABEL = "Плазма";
   private static final String RENDER_SHADER_FOG_SKY_MILKY_WAY_OPTION_LABEL = "Млечный Путь";
   private static final int ВЕы = -16777216;
   private static final int ВЕй = -16777216;
   private static final float ВЕЖ = 0.5F;
   private static final int ВЕм = -16777216;
   private static final int ВЕК = -16777216;
   private static final long ВЕI = 1000000L;
   private static final float ВЕТ = 1000.0F;
   private static final float ВЕО = 0.999999F;
   private static final float ВЕФ = 0.5222222F;
   private static final String RENDER_SHADER_FOG_SKY_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_SHADER_FOG_SKY_SHADER_FOG_UNIFORM_NAME = "ShaderFog";
   private static final String RENDER_BLIZZARD_OVERLAY_BLIZZARD_OPTION_LABEL = "Пурга";
   private static final float ВЕ0 = 4.0F;
   private static final float ВЕх = 3.5F;
   private static final float ВЕУ = 0.7F;
   private static final float ВЕЗ = 2.2F;
   private static final float ВЕЕ = 0.23F;
   private static final float ВЕП = 1.7F;
   private static final float ВЕ西 = 3.0F;
   private static final float ВЕд = 0.11F;
   private static final float ВЕБ = 14.0F;
   private static final float ВЕС = 12.0F;
   private static final float ВЕз = 0.55F;
   private static final float ВЕц = 1.25F;
   private static final double ВЕя = 4096.0;
   private static final double ВПс = 4096.0;
   private static final double ВПВ = 4096.0;
   private static final double ВПЯ = 4096.0;
   private static final String RENDER_BLIZZARD_OVERLAY_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_BLIZZARD_OVERLAY_BLIZZARD_UNIFORM_NAME = "Blizzard";
   private static final float ВПА = 2.0E9F;
   private static final float ВП4 = 0.52F;
   private static final float ВПп = 0.001F;
   private static final int ВПД = -16777216;
   private static final float ВПЪ = 0.28F;
   private static final float ВПт = 0.72F;
   private static final float ВПа = 0.34F;
   private static final float ВПЮ = 3.8F;
   private static final float ВПЧ = 4.6F;
   private static final float ВПЛ = 0.105F;
   private static final float ВП诶 = 0.034F;
   private static final float ВП1 = 0.27F;
   private static final long ВПЬ = 48L;
   private static final String RENDER_LIGHTNING_FLASH_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_LIGHTNING_FLASH_LIGHTNING_FLASH_UNIFORM_NAME = "LightningFlash";
   private static final float ВПИ = 1.0E-4F;
   private static final float ВПь = 0.5F;
   private static final float ВПж = 0.5F;
   private static final float ВП弟 = 0.5F;
   private static final float ВПб = 0.5F;
   private static final String LIGHTNING_MASK_TARGET_DEBUG_LABEL = "Ambience lightning glow";
   private static final float ВПl = 0.001F;
   private static final String RENDER_LIGHTNING_GLOW_IN_SAMPLER_NAME = "InSampler";
   private static final String RENDER_LIGHTNING_GLOW_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RENDER_LIGHTNING_GLOW_LIGHTNING_GLOW_COMPOSITE_UNIFORM_NAME = "LightningGlowComposite";
   private static final String RENDER_LIGHTNING_GLOW_BLUR_IN_SAMPLER_NAME = "InSampler";
   private static final String RENDER_LIGHTNING_GLOW_BLUR_LIGHTNING_GLOW_BLUR_UNIFORM_NAME = "LightningGlowBlur";
   private static final float ВПН = 12.0F;
   private static final float ВПн = 10.0F;
   private static final float ВПЦ = 12.0F;
   private static final float ВПч = 10.0F;
   private static final int ВПЭ = -16777216;
   private static final int ВП必 = -16777216;
   private static final String LIGHTNING_GLOW_PING_TARGET_DEBUG_LABEL = "Ambience lightning glow horizontal";
   private static final String LIGHTNING_GLOW_PONG_TARGET_DEBUG_LABEL = "Ambience lightning glow vertical";
   private static final long ВПл = 32L;
   private static final long ВПэ = 32L;
   private static final long ВПю = 48L;
   private static final double ВПЩ = 1.0E-8;
   private static final double ВПу = 0.5;
   private static final double ВП2 = 1.0E-8;
   private static final double ВПф = 1.0E-8;
   private static final double ВПи = 2.0;
   private static final float ВП9 = 0.1F;
   private static final float ВПо = 1.35F;
   private static final float ВПO = 1.5F;
   private static final float ВП7 = -1.0F;
   private static final float ВПР = -1.0F;
   private static final String RENDER_LIGHTNING_LAYER_LIGHTNING_UNIFORM_NAME = "Lightning";
   private static final long ВПр = 131072L;
   private static final long ВП6 = 144L;
   private static final double ВП_ = 343.0;
   private static final double ВПШ = 20.0;
   private static final double ВПы = -1.7278759594743864;
   private static final double ВПй = 1.7278759594743864;
   private static final double ВПЖ = 52.0;
   private static final double ВПм = 96.0;
   private static final double ВПК = 6.0;
   private static final double ВПI = 58.0;
   private static final double ВПТ = 92.0;
   private static final double ВПО = 36.0;
   private static final double ВПФ = -8.0;
   private static final double ВПЙ = 8.0;
   private static final double ВПк = -8.0;
   private static final double ВПг = 8.0;
   private static final double ВП0 = 0.5;
   private static final double ВПх = 8.5;
   private static final double ВПУ = 8.5;
   private static final double ВПЗ = 0.5;
   private static final float ВПЕ = 0.68F;
   private static final double ВПП = Math.PI * 2;
   private static final float ВП西 = 0.7F;
   private static final double ВПд = 1.35;
   private static final double ВПБ = 0.34;
   private static final double ВПС = -0.65;
   private static final double ВПз = 0.65;
   private static final double ВПц = 1.7;
   private static final double ВПя = 3.5;
   private static final double В西с = -0.65;
   private static final double В西В = 0.65;
   private static final float В西Я = 0.18F;
   private static final double В西Х = 0.5;
   private static final double В西е = 2.0;
   private static final double В西А = 0.57;
   private static final double В西4 = 0.57;
   private static final double В西п = 0.82;
   private static final double В西Д = 1.18;
   private static final float В西Ъ = 0.25F;
   private static final float В西т = 0.25F;
   private static final double В西а = 1.0E9;
   private static final float В西Ю = 0.045F;
   private static final float В西Ч = 0.045F;
   private static final float В西Л = 0.095F;
   private static final float В西诶 = 0.165F;
   private static final float В西1 = 0.095F;
   private static final float В西Ь = 0.07F;
   private static final float В西衣 = 0.58F;
   private static final float В西ш = 0.52F;
   private static final float В西И = 0.165F;
   private static final float В西ь = 0.355F;
   private static final float В西ж = 0.58F;
   private static final float В西弟 = 3.0F;
   private static final String RENDER_WEATHER_RAIN_OPTION_LABEL = "Дождь";
   private static final String RENDER_WEATHER_SNOW_OPTION_LABEL = "Снег";
   private static final float В西l = 1.0E9F;
   private static final float В西8 = 0.05F;
   private static final float В西5 = 4.0F;
   private static final float В西в = 3.5F;
   private static final float В西ъ = 0.7F;
   private static final float В西М = 2.2F;
   private static final float В西Н = 0.23F;
   private static final float В西н = 1.7F;
   private static final float В西Ц = 3.0F;
   private static final float В西ч = 0.11F;
   private static final float В西Э = 14.0F;
   private static final float В西必 = -18.0F;
   private static final float В西щ = -4.5F;
   private static final float В西Г = 0.01F;
   private static final float В西л = 0.35F;
   private static final float В西э = 1.0E-4F;
   private static final float В西ю = 18.0F;
   private static final float В西Щ = 4.5F;
   private static final float В西у = 1.8F;
   private static final float В西2 = 0.001F;
   private static final float В西ф = 1.1F;
   private static final float В西и = 20.0F;
   private static final float В西9 = 48.0F;
   private static final float В西о = 64.0F;
   private static final float В西O = 0.5F;
   private static final double В西7 = 32.0;
   private static final double В西Р = 32.0;
   private static final double В西3 = 4096.0;
   private static final double В西р = 4096.0;
   private static final float В西6 = 0.85F;
   private static final float В西_ = 2.6F;
   private static final float В西Ш = 1.25F;
   private static final double В西ы = 4096.0;
   private static final float В西й = 0.12F;
   private static final float В西Ж = 0.88F;
   private static final float В西м = 8192.0F;
   private static final double В西К = 4096.0;
   private static final double В西I = 4096.0;
   private static final double В西Т = 4096.0;
   private static final float В西О = 0.38F;
   private static final float В西Ф = 0.39999998F;
   private static final String RENDER_WEATHER_PARTICLES_RAIN_OPTION_LABEL = "Дождь";
   private static final String RENDER_WEATHER_PARTICLES_WEATHER_UNIFORM_NAME = "Weather";
   private static final String RENDER_WEATHER_PARTICLES_FOG_UNIFORM_NAME = "Fog";
   private static final String RENDER_WEATHER_GROUND_EFFECTS_RAIN_OPTION_LABEL = "Дождь";
   private static final String RENDER_WEATHER_GROUND_EFFECTS_DEPTH_SAMPLER_NAME = "DepthSampler";
   private static final String RENDER_WEATHER_GROUND_EFFECTS_WEATHER_UNIFORM_NAME = "Weather";
   private static final String RENDER_WEATHER_GROUND_EFFECTS_FOG_UNIFORM_NAME = "Fog";
   private static final String RENDER_WEATHER_GROUND_EFFECTS_SAMPLER0_NAME = "Sampler0";
   private static final long В西П = 400L;
   private static final String WEATHER_GROUND_TARGET_DEBUG_LABEL = "Ambience weather ground layer";
   private static final String HAS_SKY_SHADER_SELECTION_NONE_OPTION_LABEL = "Ничего";
   private static final String LEGACY_SOURCE_SKY_SELECTION_LABEL = "Origin";
   private static final String MIGRATED_SOURCE_SKY_SELECTION_LABEL = "Источник";
   private static final String LEGACY_NEBULA_SKY_SELECTION_LABEL = "Nebula";
   private static final String MIGRATED_NEBULA_SKY_SELECTION_LABEL = "Небула";
   private static final String LEGACY_SOURCE_SKY_OPTION_BIND_KEY = "Origin";
   private static final String MIGRATED_SOURCE_SKY_OPTION_BIND_KEY = "Источник";
   private static final String LEGACY_NEBULA_SKY_OPTION_BIND_KEY = "Nebula";
   private static final String MIGRATED_NEBULA_SKY_OPTION_BIND_KEY = "Небула";
   private static final String IS_SOURCE_OR_NEBULA_SKY_SELECTED_SOURCE_OPTION_LABEL = "Источник";
   private static final String IS_SOURCE_OR_NEBULA_SKY_SELECTED_NEBULA_OPTION_LABEL = "Небула";
   private static final String IS_PULSAR_SKY_SELECTED_PULSAR_OPTION_LABEL = "Пульсар";
   private static final String IS_GALAXY_SKY_SELECTED_GALAXY_OPTION_LABEL = "Галактика";
   private static final String IS_NIGHTIX_AURORA_SELECTED_AURORA2_OPTION_LABEL = "Аврора 2";
   private static final String IS_JUICE_SKY_SELECTED_JUICE_OPTION_LABEL = "Сок";
   private static final float ВдЪ = 1.0E-4F;
   private static final float Вдт = 70.0F;
   private static final double Вда = 2.0;
   private static final float ВдЮ = -1.0F;
   private static final float ВдЧ = -1.0F;
   private static final float ВдЛ = -1.0F;
   private static final float Вд诶 = -1.0F;
   private static final String UNIT_QUAD_VERTEX_BUFFER_DEBUG_LABEL = "Ambience shared fullscreen quad";
   private static final String WEATHER_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience custom weather uniforms";
   private static final String AMBIENCE_CUSTOM_WEATHER_GROUND_COMPOSITE_PASS_DEBUG_LABEL = "Ambience custom weather ground composite";
   private static final String AMBIENCE_CUSTOM_WEATHER_GROUND_EFFECTS_PASS_DEBUG_LABEL = "Ambience custom weather ground effects";
   private static final String AMBIENCE_CUSTOM_WEATHER_PARTICLES_PASS_DEBUG_LABEL = "Ambience custom weather particles";
   private static final String AMBIENCE_LIGHTNING_PASS_DEBUG_LABEL = "Ambience lightning";
   private static final String LIGHTNING_GLOW_COMPOSITE_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience lightning glow composite uniforms";
   private static final String LIGHTNING_GLOW_VERTICAL_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience lightning vertical glow uniforms";
   private static final String LIGHTNING_GLOW_HORIZONTAL_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience lightning horizontal glow uniforms";
   private static final String AMBIENCE_LIGHTNING_DUAL_GLOW_BLUR_PASS_DEBUG_LABEL = "Ambience lightning dual glow blur";
   private static final String AMBIENCE_LIGHTNING_GLOW_COMPOSITE_PASS_DEBUG_LABEL = "Ambience lightning glow composite";
   private static final String AMBIENCE_LIGHTNING_GLOW_MASK_PASS_DEBUG_LABEL = "Ambience lightning glow mask";
   private static final String AMBIENCE_LIGHTNING_FLASH_PASS_DEBUG_LABEL = "Ambience lightning flash";
   private static final String LIGHTNING_FLASH_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience lightning flash uniforms";
   private static final String AMBIENCE_BLIZZARD_OVERLAY_PASS_DEBUG_LABEL = "Ambience blizzard overlay";
   private static final String BLIZZARD_OVERLAY_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience blizzard overlay uniforms";
   private static final String AMBIENCE_PROCEDURAL_SKY_PASS_DEBUG_LABEL = "Ambience procedural sky";
   private static final String JUICE_SKY_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience juice sky uniforms";
   private static final String SHADER_FOG_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience procedural sky uniforms";
   private static final String SHADER_MIST_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience shader sky uniforms";
   private static final String AMBIENCE_JUICE_SKY_PASS_DEBUG_LABEL = "Ambience juice sky";
   private static final String AMBIENCE_SHADER_SKY_PASS_DEBUG_LABEL = "Ambience shader sky";
   private static final String AMBIENCE_SHADER_SKY_COMPOSITE_PASS_DEBUG_LABEL = "Ambience shader sky composite";
   private static final String WORLD_COLOR_UNIFORM_BUFFER_DEBUG_LABEL = "Ambience world color uniforms";
   private static final String AMBIENCE_WORLD_COLOR_PASS_DEBUG_LABEL = "Ambience world color";
   private static final String SNOW_COLOR_SNOW_VISIBILITY_OPTION_LABEL = "Снег";
   private static final String SNOW_COLOR_BLIZZARD_VISIBILITY_OPTION_LABEL = "Пурга";
   private static final String RAIN_COLOR_RAIN_VISIBILITY_OPTION_LABEL = "Дождь";
   private static final String WORLD_TINT_STRENGTH_NONE_VISIBILITY_OPTION_LABEL = "Ничего";
   private static final String WORLD_TINT_CUSTOM_COLOR_VISIBILITY_OPTION_LABEL = "Свой";
   private static final String FOG_COLOR_OVERRIDE_VISIBILITY_OPTION_LABEL = "Переопределить";
   private static final String FOG_START_OVERRIDE_VISIBILITY_OPTION_LABEL = "Переопределить";
   private static final String FOG_END_OVERRIDE_VISIBILITY_OPTION_LABEL = "Переопределить";

   public boolean isFogOverrideEnabled() {
      return this.isEnabled() && this.fogModeSetting.isSelected(IS_FOG_OVERRIDE_ENABLED_OVERRIDE_OPTION_LABEL);
   }

   private void migrateLegacySkyOptions(JsonObject settingsJson) {
      if (settingsJson.has(this.skyShaderSetting.getName())) {
         String legacySkyOption = settingsJson.get(this.skyShaderSetting.getName()).getAsString();
         if (LEGACY_SOURCE_SKY_SELECTION_LABEL.equals(legacySkyOption)) {
            settingsJson.addProperty(this.skyShaderSetting.getName(), MIGRATED_SOURCE_SKY_SELECTION_LABEL);
         } else if (LEGACY_NEBULA_SKY_SELECTION_LABEL.equals(legacySkyOption)) {
            settingsJson.addProperty(this.skyShaderSetting.getName(), MIGRATED_NEBULA_SKY_SELECTION_LABEL);
         }
      }

      String bindsPropertyName = this.skyShaderSetting.getName() + " Binds";
      if (settingsJson.has(bindsPropertyName) && settingsJson.get(bindsPropertyName).isJsonObject()) {
         JsonObject optionBinds = settingsJson.getAsJsonObject(bindsPropertyName);
         renameJsonPropertyIfAbsent(optionBinds, LEGACY_SOURCE_SKY_OPTION_BIND_KEY, MIGRATED_SOURCE_SKY_OPTION_BIND_KEY);
         renameJsonPropertyIfAbsent(optionBinds, LEGACY_NEBULA_SKY_OPTION_BIND_KEY, MIGRATED_NEBULA_SKY_OPTION_BIND_KEY);
      }
   }

   private void destroyWeatherGroundTarget() {
      if (this.weatherGroundTarget != null) {
         this.weatherGroundTarget.destroyBuffers();
         this.weatherGroundTarget = null;
      }
   }

   private void ensureShaderSkyUniformBuffers() {
      if (this.shaderMistUniformBuffer == null || this.shaderMistUniformBuffer.isClosed()) {
         this.shaderMistUniformBuffer = RenderSystem.getDevice().createBuffer(() -> SHADER_MIST_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВЕ2);
      }

      if (this.shaderFogUniformBuffer == null || this.shaderFogUniformBuffer.isClosed()) {
         this.shaderFogUniformBuffer = RenderSystem.getDevice().createBuffer(() -> SHADER_FOG_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВЕф);
      }

      if (this.juiceSkyUniformBuffer == null || this.juiceSkyUniformBuffer.isClosed()) {
         this.juiceSkyUniformBuffer = RenderSystem.getDevice().createBuffer(() -> JUICE_SKY_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВЕи);
      }
   }

   private void renderWeatherParticles(RenderTarget renderTarget, GpuBuffer weatherUniforms, GpuBufferSlice fogUniforms, CommandEncoder encoder, int instanceCount) {
      try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_CUSTOM_WEATHER_PARTICLES_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty())) {
         boolean isRain = this.weatherModeSetting.isSelected(RENDER_WEATHER_PARTICLES_RAIN_OPTION_LABEL);
         renderPass.setPipeline(isRain ? this.rainParticlePipeline : this.snowParticlePipeline);
         if (isRain) {
            this.rainRoofHeightmap.Ж(renderPass);
         }

         renderPass.setUniform(RENDER_WEATHER_PARTICLES_WEATHER_UNIFORM_NAME, weatherUniforms);
         renderPass.setUniform(RENDER_WEATHER_PARTICLES_FOG_UNIFORM_NAME, fogUniforms);
         this.drawUnitQuad(renderPass, instanceCount);
      }
   }

   private boolean isJuiceSkySelected() {
      return this.skyShaderSetting.isSelected(IS_JUICE_SKY_SELECTED_JUICE_OPTION_LABEL);
   }

   private void renderShaderFogSky(RenderTarget renderTarget, CommandEncoder encoder, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, DeltaTracker deltaTracker, int width, int height, float verticalFovDegrees) {
      this.skyInverseProjectionMatrix.set(sourceProjectionMatrix).invert();
      this.skyInverseViewMatrix.set(sourceViewMatrix).invert();
      float worldTimeSeconds = ((float)(MINECRAFT_CLIENT.level.getGameTime() % ВЕо) + deltaTracker.getGameTimeDeltaPartialTick(false)) / ВЕO;
      float negativeYawRadians = (float)Math.toRadians(-MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot());
      float pitchRadians = (float)Math.toRadians(MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot());
      float fovDegrees = verticalFovDegrees;
      boolean isSourceSky = this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_SOURCE_OPTION_LABEL);
      boolean isNebulaSky = this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_NEBULA_OPTION_LABEL);
      int shaderVariant = this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_SPACE_OPTION_LABEL)
         ? 1
         : (
            this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_PURPLE_OPTION_LABEL)
               ? 2
               : (
                  this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_AURORA_OPTION_LABEL)
                     ? 3
                     : (this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_PLASMA_OPTION_LABEL) ? 5 : (this.skyShaderSetting.isSelected(RENDER_SHADER_FOG_SKY_MILKY_WAY_OPTION_LABEL) ? 6 : (isSourceSky ? 7 : (isNebulaSky ? 8 : 4))))
               )
         );
      int primaryArgb = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | ВЕы;
      int secondaryArgb = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) | ВЕй;
      Vector4f primaryColor = ColorMath.toRgbaVector(primaryArgb);
      Vector4f secondaryColor = ColorMath.toRgbaVector(secondaryArgb);
      Vector4f blendedColor = ColorMath.toRgbaVector(ColorMath.blendArgb(primaryArgb, secondaryArgb, ВЕЖ));
      float starsFlag = this.isSourceOrNebulaSkySelected() && this.starsSetting.getValue() ? 1.0F : 0.0F;
      if (isSourceSky || isNebulaSky) {
         Vector4f sourcePrimaryColor = ColorMath.toRgbaVector(primaryArgb | ВЕм);
         Vector4f sourceSecondaryColor = ColorMath.toRgbaVector(secondaryArgb | ВЕК);
         secondaryColor = sourcePrimaryColor;
         blendedColor = sourcePrimaryColor;
         primaryColor = sourceSecondaryColor;
      }

      float timeOfDayFraction = this.timeOfDaySetting.getTimeOfDayFraction(MINECRAFT_CLIENT.level.getOverworldClockTime());
      float rainLevel = MINECRAFT_CLIENT.level.getRainLevel(deltaTracker.getGameTimeDeltaPartialTick(false));
      float timeOrStars = !isSourceSky && !isNebulaSky ? (float)(System.currentTimeMillis() % ВЕI) / ВЕТ : starsFlag;

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 240)
            .putMat4f(this.skyInverseProjectionMatrix)
            .putMat4f(this.skyInverseViewMatrix)
            .putVec4(negativeYawRadians, pitchRadians, fovDegrees, worldTimeSeconds)
            .putVec4(width, height, shaderVariant, this.shaderIntensitySetting.getValue())
            .putVec4(primaryColor.x, primaryColor.y, primaryColor.z, primaryColor.w)
            .putVec4(this.shaderSpeedSetting.getValue(), ВЕО, 1.0F, timeOrStars)
            .putVec4(secondaryColor.x, secondaryColor.y, secondaryColor.z, secondaryColor.w)
            .putVec4(blendedColor.x, blendedColor.y, blendedColor.z, blendedColor.w)
            .putVec4(starsFlag, timeOfDayFraction, rainLevel, ВЕФ)
            .get();
         encoder.writeToBuffer(this.shaderFogUniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_PROCEDURAL_SKY_PASS_DEBUG_LABEL, this.shaderSkyTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(this.shaderFogPipeline);
            renderPass.bindTexture(RENDER_SHADER_FOG_SKY_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.setUniform(RENDER_SHADER_FOG_SKY_SHADER_FOG_UNIFORM_NAME, this.shaderFogUniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private boolean hasSkyShaderSelection() {
      return !this.skyShaderSetting.isSelected(HAS_SKY_SHADER_SELECTION_NONE_OPTION_LABEL);
   }

   private Vector4f projectWorldPositionToUv(Vec3 worldPosition, Vec3 cameraWorldPosition, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix) {
      Vec3 cameraRelativePosition = worldPosition.subtract(cameraWorldPosition);
      Vector4f projectedPosition = new Vector4f((float)cameraRelativePosition.x, (float)cameraRelativePosition.y, (float)cameraRelativePosition.z, 1.0F).mul(sourceViewMatrix).mul(sourceProjectionMatrix);
      if (projectedPosition.w <= ВПИ) {
         return null;
      }

      projectedPosition.div(projectedPosition.w);
      return new Vector4f(projectedPosition.x * ВПь + ВПж, projectedPosition.y * ВП弟 + ВПб, 0.0F, 0.0F);
   }

   private int resolvePrimaryThemeColor(int fallbackArgb) {
      return this.resolveThemeColor(fallbackArgb, ThemeColorSlot.MAIN);
   }

   private boolean isBlizzardFogSelected() {
      return this.weatherModeSetting.isSelected(IS_BLIZZARD_FOG_SELECTED_BLIZZARD_OPTION_LABEL) && this.fogModeSetting.isSelected(IS_BLIZZARD_FOG_SELECTED_NO_CHANGE_OPTION_LABEL);
   }

   private void renderLightningFlash(RenderTarget renderTarget, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, Vec3 cameraWorldPosition, float flashStrength, Vector4f color) {
      if (this.lightningTopWorldPosition != null && this.lightningGroundWorldPosition != null) {
         Vector4f topUv = this.projectWorldPositionToUv(this.lightningTopWorldPosition, cameraWorldPosition, sourceProjectionMatrix, sourceViewMatrix);
         Vector4f groundUv = this.projectWorldPositionToUv(this.lightningGroundWorldPosition, cameraWorldPosition, sourceProjectionMatrix, sourceViewMatrix);
         if (topUv != null && groundUv != null) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
               ByteBuffer uniformData = Std140Builder.onStack(stack, 48)
                  .putVec4(topUv.x, topUv.y, groundUv.x, groundUv.y)
                  .putVec4(flashStrength, (float)renderTarget.width / Math.max(1, renderTarget.height), ВП1, 0.0F)
                  .putVec4(color.x, color.y, color.z, 1.0F)
                  .get();
               if (this.lightningFlashUniformBuffer == null || this.lightningFlashUniformBuffer.isClosed()) {
                  this.lightningFlashUniformBuffer = RenderSystem.getDevice().createBuffer(() -> LIGHTNING_FLASH_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВПЬ);
               }

               CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
               encoder.writeToBuffer(this.lightningFlashUniformBuffer.slice(), uniformData);

               try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_LIGHTNING_FLASH_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
                  renderPass.setPipeline(this.lightningFlashPipeline);
                  renderPass.bindTexture(RENDER_LIGHTNING_FLASH_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  renderPass.setUniform(RENDER_LIGHTNING_FLASH_LIGHTNING_FLASH_UNIFORM_NAME, this.lightningFlashUniformBuffer);
                  renderPass.draw(3, 1, 0, 0);
               }
            }
         }
      }
   }

   private boolean isGenericSkyShaderSelected() {
      return this.hasSkyShaderSelection() && !this.isPulsarOrGalaxySkySelected() && !this.isJuiceSkySelected() && !this.isNightixAuroraSelected();
   }

   private void stopThunderSound() {
      this.thunderDelayTicks = -1;
      this.pendingThunderWorldPosition = null;
      if (this.thunderSound != null) {
         this.thunderSound.finish();
         MINECRAFT_CLIENT.getSoundManager().stop(this.thunderSound);
         this.thunderSound = null;
      }
   }

   private GpuBuffer ensureLightningLayerUniformBuffer(int layerIndex) {
      GpuBuffer uniformBuffer = this.lightningLayerUniformBuffers[layerIndex];
      if (uniformBuffer != null && !uniformBuffer.isClosed()) {
         return uniformBuffer;
      }

      uniformBuffer = RenderSystem.getDevice().createBuffer(() -> "Ambience lightning layer uniforms " + layerIndex, 136, ВП6);
      this.lightningLayerUniformBuffers[layerIndex] = uniformBuffer;
      return uniformBuffer;
   }

   private void drawUnitQuad(RenderPass renderPass) {
      this.drawUnitQuad(renderPass, 1);
   }

   private GpuBuffer ensureWeatherUniformBuffer() {
      if (this.weatherUniformBuffer == null || this.weatherUniformBuffer.isClosed()) {
         this.weatherUniformBuffer = RenderSystem.getDevice().createBuffer(() -> WEATHER_UNIFORM_BUFFER_DEBUG_LABEL, 136, В西П);
      }

      return this.weatherUniformBuffer;
   }

   private static void renameJsonPropertyIfAbsent(JsonObject json, String oldProperty, String newProperty) {
      if (json.has(oldProperty) && !json.has(newProperty)) {
         json.add(newProperty, json.get(oldProperty));
         json.remove(oldProperty);
      }
   }

   public long resolveWorldTime(long worldTime) {
      return this.isEnabled() && !this.timeOfDaySetting.isWorldTimeSelected() ? this.timeOfDaySetting.resolveTimeOfDayTicks(worldTime) : worldTime;
   }

   private void drawUnitQuad(RenderPass renderPass, int instanceCount) {
      GpuBuffer vertexBuffer = this.ensureUnitQuadVertexBuffer();
      AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
      GpuBuffer indexBuffer = sequentialIndices.getBuffer(this.unitQuadIndexCount);
      renderPass.setVertexBuffer(0, vertexBuffer.slice());
      renderPass.setIndexBuffer(indexBuffer, sequentialIndices.type());
      renderPass.drawIndexed(this.unitQuadIndexCount, instanceCount, 0, 0, 0);
   }

   private GpuBuffer ensureLightningLayerVertexBuffer(int layerIndex, int requiredBytes) {
      GpuBuffer vertexBuffer = this.lightningLayerVertexBuffers[layerIndex];
      if (vertexBuffer != null && !vertexBuffer.isClosed() && this.lightningLayerVertexCapacities[layerIndex] >= requiredBytes) {
         return vertexBuffer;
      }

      if (vertexBuffer != null && !vertexBuffer.isClosed()) {
         vertexBuffer.close();
      }

      long capacityBytes = ВПр;

      while (capacityBytes < requiredBytes) {
         capacityBytes <<= 1;
      }

      vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Ambience lightning layer vertices " + layerIndex, 40, capacityBytes);
      this.lightningLayerVertexBuffers[layerIndex] = vertexBuffer;
      this.lightningLayerVertexCapacities[layerIndex] = capacityBytes;
      return vertexBuffer;
   }

   public boolean isWeatherOverrideEnabled() {
      return this.isEnabled() && !this.weatherModeSetting.isSelected(IS_WEATHER_OVERRIDE_ENABLED_NO_CHANGE_OPTION_LABEL);
   }

   private void releaseWorldColorBuffer() {
      if (this.worldColorUniformBuffer != null) {
         this.worldColorUniformBuffer.close();
         this.worldColorUniformBuffer = null;
      }

      this.cachedWorldTintArgb = ВЕl;
      this.cachedWorldTintStrength = Float.NaN;
   }

   private int resolveWeatherColor(int fallbackArgb) {
      int themeArgb = this.resolvePrimaryThemeColor(fallbackArgb);
      return this.canRenderShaderSky() && this.useThemeColorsSetting.getValue() ? ColorMath.blendArgb(fallbackArgb, themeArgb, ВЗх) : themeArgb;
   }

   private boolean isSourceOrNebulaSkySelected() {
      return this.skyShaderSetting.isSelected(IS_SOURCE_OR_NEBULA_SKY_SELECTED_SOURCE_OPTION_LABEL) || this.skyShaderSetting.isSelected(IS_SOURCE_OR_NEBULA_SKY_SELECTED_NEBULA_OPTION_LABEL);
   }

   public boolean isFogClearEnabled() {
      return this.isEnabled() && this.fogModeSetting.isSelected(IS_FOG_CLEAR_ENABLED_CLEAR_OPTION_LABEL);
   }

   private static float computeLightningEnvelope(float normalizedAge) {
      if (normalizedAge < В西Ю) {
         return smoothstep01(normalizedAge / В西Ч);
      } else if (normalizedAge < В西Л) {
         return 1.0F;
      } else if (normalizedAge < В西诶) {
         float decayProgress = smoothstep01((normalizedAge - В西1) / В西Ь);
         return Mth.lerp(decayProgress, 1.0F, В西衣);
      } else if (normalizedAge < В西ш) {
         float fadeOutProgress = smoothstep01((normalizedAge - В西И) / В西ь);
         return В西ж * (1.0F - fadeOutProgress);
      } else {
         return 0.0F;
      }
   }

   public boolean isRainOverrideEnabled() {
      return this.isEnabled() && this.weatherModeSetting.isSelected(IS_RAIN_OVERRIDE_ENABLED_RAIN_OPTION_LABEL);
   }

   public boolean shouldRemoveClouds() {
      return this.isEnabled() && (this.removeCloudsSetting.getValue() || this.hasSkyShaderSelection());
   }

   private void releaseWeatherUniformBuffer() {
      if (this.weatherUniformBuffer != null && !this.weatherUniformBuffer.isClosed()) {
         this.weatherUniformBuffer.close();
      }

      this.weatherUniformBuffer = null;
   }

   private void renderLightningLayer(RenderTarget renderTarget, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, Vec3 cameraWorldPosition, float layerHalfWidth, Vector4f color, int layerIndex) {
      BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
      Matrix4f identityMatrix = new Matrix4f();

      for (Ambience.LightningSegment segment : this.lightningSegments) {
         Vec3 relativeStart = segment.start.subtract(cameraWorldPosition);
         Vec3 relativeEnd = segment.end.subtract(cameraWorldPosition);
         Vec3 segmentDirection = relativeEnd.subtract(relativeStart);
         if (!(segmentDirection.lengthSqr() < ВПЩ)) {
            Vec3 relativeMidpoint = relativeStart.add(relativeEnd).scale(ВПу);
            Vec3 viewDirection = relativeMidpoint.lengthSqr() > ВП2 ? relativeMidpoint.normalize() : new Vec3(0.0, 0.0, 1.0);
            Vec3 ribbonSideDirection = segmentDirection.normalize().cross(viewDirection);
            ribbonSideDirection = ribbonSideDirection.lengthSqr() < ВПф ? new Vec3(1.0, 0.0, 0.0) : ribbonSideDirection.normalize();
            float worldUnitsPerPixel = (float)(ВПи * Math.max(1.0, relativeMidpoint.length()) / Math.max(1.0, Math.abs(sourceProjectionMatrix.m11()) * renderTarget.height));
            float minimumPixelHalfWidth = layerHalfWidth <= ВП9 ? ВПо : ВПO;
            float halfWidth = Math.max(layerHalfWidth * segment.strength, worldUnitsPerPixel * minimumPixelHalfWidth);
            Vec3 sideOffset = ribbonSideDirection.scale(halfWidth);
            Vec3 startMinusSide = relativeStart.subtract(sideOffset);
            Vec3 startPlusSide = relativeStart.add(sideOffset);
            Vec3 endPlusSide = relativeEnd.add(sideOffset);
            Vec3 endMinusSide = relativeEnd.subtract(sideOffset);
            vertexBuilder.addVertex(identityMatrix, (float)startMinusSide.x, (float)startMinusSide.y, (float)startMinusSide.z).setUv(ВП7, 0.0F);
            vertexBuilder.addVertex(identityMatrix, (float)startPlusSide.x, (float)startPlusSide.y, (float)startPlusSide.z).setUv(1.0F, 0.0F);
            vertexBuilder.addVertex(identityMatrix, (float)endPlusSide.x, (float)endPlusSide.y, (float)endPlusSide.z).setUv(1.0F, 1.0F);
            vertexBuilder.addVertex(identityMatrix, (float)endMinusSide.x, (float)endMinusSide.y, (float)endMinusSide.z).setUv(ВПР, 1.0F);
         }
      }

      try (
         MeshData mesh = vertexBuilder.buildOrThrow();
         MemoryStack stack = MemoryStack.stackPush();
      ) {
         DrawState drawState = mesh.drawState();
         AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
         GpuBuffer indexBuffer = sequentialIndices.getBuffer(drawState.indexCount());
         ByteBuffer uniformData = Std140Builder.onStack(stack, 144).putMat4f(sourceProjectionMatrix).putMat4f(sourceViewMatrix).putVec4(color).get();
         ByteBuffer vertexData = mesh.vertexBuffer();
         int vertexByteCount = vertexData.remaining();
         GpuBuffer vertexBuffer = this.ensureLightningLayerVertexBuffer(layerIndex, vertexByteCount);
         GpuBuffer uniformBuffer = this.ensureLightningLayerUniformBuffer(layerIndex);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         encoder.writeToBuffer(vertexBuffer.slice(0L, vertexByteCount), vertexData);
         encoder.writeToBuffer(uniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = encoder.createRenderPass(
               () -> AMBIENCE_LIGHTNING_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
            )) {
            renderPass.setPipeline(layerIndex == 2 ? this.lightningCorePipeline : this.lightningPipeline);
            renderPass.setUniform(RENDER_LIGHTNING_LAYER_LIGHTNING_UNIFORM_NAME, uniformBuffer);
            renderPass.setVertexBuffer(0, vertexBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, sequentialIndices.type());
            renderPass.drawIndexed(drawState.indexCount(), 1, 0, 0, 0);
         }
      }
   }

   private void releaseUnitQuadBuffer() {
      if (this.unitQuadVertexBuffer != null && !this.unitQuadVertexBuffer.isClosed()) {
         this.unitQuadVertexBuffer.close();
      }

      this.unitQuadVertexBuffer = null;
      this.unitQuadIndexCount = 0;
   }

   private void renderWeather(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, GpuBufferSlice fogUniforms) {
      if (this.canRenderWeather() && sourceProjectionMatrix != null && sourceViewMatrix != null && fogUniforms != null) {
         RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getDepthTexture() != null && !renderTarget.getDepthTexture().isClosed()) {
            Camera camera = MINECRAFT_CLIENT.gameRenderer.mainCamera();
            Vec3 cameraWorldPosition = camera.position();
            Matrix4f inverseProjection = this.weatherInverseProjectionMatrix.set(sourceProjectionMatrix).invert();
            Matrix4f inverseView = this.weatherInverseViewMatrix.set(sourceViewMatrix).invert();
            int weatherKind = this.weatherModeSetting.isSelected(RENDER_WEATHER_RAIN_OPTION_LABEL) ? 1 : (this.weatherModeSetting.isSelected(RENDER_WEATHER_SNOW_OPTION_LABEL) ? 2 : 3);
            long nowNanos = System.nanoTime();
            float frameSeconds = this.previousWeatherFrameNanos == 0L ? 0.0F : Math.clamp((float)(nowNanos - this.previousWeatherFrameNanos) / В西l, 0.0F, В西8);
            this.previousWeatherFrameNanos = nowNanos;
            float simulationSeconds = !MINECRAFT_CLIENT.isPaused() && !MINECRAFT_CLIENT.level.tickRateManager().isFrozen() ? frameSeconds : 0.0F;
            this.weatherElapsedSeconds += simulationSeconds;
            float weatherTime = (float)this.weatherElapsedSeconds;
            float windSpeed = this.windSpeedSetting.getValue();
            float windDegrees = this.windDirectionSetting.getValue();
            if (weatherKind == 3) {
               windSpeed = Math.max(windSpeed, В西5) + В西в + (float)Math.sin(weatherTime * В西ъ) * В西М + (float)Math.sin(weatherTime * В西Н + В西н) * В西Ц;
               windDegrees += (float)Math.sin(weatherTime * В西ч) * В西Э;
            }

            float windRadians = (float)Math.toRadians(windDegrees);
            float windX = (float)Math.sin(windRadians) * windSpeed;
            float windZ = (float)Math.cos(windRadians) * windSpeed;
            float fallVelocity = (weatherKind == 1 ? В西必 : В西щ) * Math.max(this.weatherFallSpeedSetting.getValue(), В西Г);
            float horizontalWindFactor = weatherKind == 1 ? В西л : 1.0F;
            float particleWindX = windX * horizontalWindFactor;
            float particleWindZ = windZ * horizontalWindFactor;
            float particleSpeed = (float)Math.sqrt(particleWindX * particleWindX + fallVelocity * fallVelocity + particleWindZ * particleWindZ);
            float inverseParticleSpeed = 1.0F / Math.max(particleSpeed, В西э);
            float particleDirectionX = particleWindX * inverseParticleSpeed;
            float particleDirectionY = fallVelocity * inverseParticleSpeed;
            float particleDirectionZ = particleWindZ * inverseParticleSpeed;
            float horizontalWindSpeed = (float)Math.sqrt(windX * windX + windZ * windZ);
            Vector4f weatherColor = ColorMath.toRgbaVector(this.resolveWeatherColor(weatherKind == 1 ? this.rainColorSetting.getValue() : this.snowColorSetting.getValue()));
            float verticalOffsetSpeed = weatherKind == 1 ? В西ю : (weatherKind == 3 ? В西Щ : В西у);
            this.weatherVerticalOffset = this.weatherVerticalOffset + verticalOffsetSpeed * this.weatherFallSpeedSetting.getValue() * simulationSeconds;
            this.weatherWindOffsetX += particleWindX * simulationSeconds;
            this.weatherWindOffsetZ += particleWindZ * simulationSeconds;
            this.weatherFallPhase = this.weatherFallPhase + this.weatherFallSpeedSetting.getValue() * simulationSeconds;
            if (!(weatherColor.w <= В西2)) {
               float weatherDistance = this.weatherDistanceSetting.getValue();
               float verticalPeriod = Math.clamp(weatherDistance * В西ф, В西и, В西9);
               float cellSize = weatherDistance * 2.0F / В西о;
               float wrappedCameraY = positiveModulo(cameraWorldPosition.y + verticalPeriod * В西O, verticalPeriod);
               double cellX = Math.floor((cameraWorldPosition.x - this.weatherWindOffsetX) / cellSize) - В西7;
               double cellZ = Math.floor((cameraWorldPosition.z - this.weatherWindOffsetZ) / cellSize) - В西Р;
               float relativeCellX = (float)(cellX * cellSize + this.weatherWindOffsetX - cameraWorldPosition.x);
               float relativeCellZ = (float)(cellZ * cellSize + this.weatherWindOffsetZ - cameraWorldPosition.z);
               float wrappedCellX = positiveModulo(cellX, В西3);
               float wrappedCellZ = positiveModulo(cellZ, В西р);
               float groundWrapBase = weatherKind == 1 ? Math.max(В西6, this.groundEffectSizeSetting.getValue() * В西_) : В西Ш;
               double groundWrapPeriod = groundWrapBase * В西ы;
               float wrappedCameraX = positiveModulo(cameraWorldPosition.x, groundWrapPeriod);
               float wrappedCameraZ = positiveModulo(cameraWorldPosition.z, groundWrapPeriod);
               int width = Math.max(1, renderTarget.width);
               int height = Math.max(1, renderTarget.height);
               float densityFactor = В西й + В西Ж * Math.clamp(this.weatherIntensitySetting.getValue(), 0.0F, 1.0F);
               int instanceCount = Math.clamp(Math.round(В西м * densityFactor), 1, 8192);

               try (MemoryStack stack = MemoryStack.stackPush()) {
                  ByteBuffer uniformData = Std140Builder.onStack(stack, 400)
                     .putMat4f(sourceProjectionMatrix)
                     .putMat4f(sourceViewMatrix)
                     .putMat4f(inverseProjection)
                     .putMat4f(inverseView)
                     .putVec4(wrappedCameraX, (float)cameraWorldPosition.y, wrappedCameraZ, weatherTime)
                     .putVec4(width, height, weatherKind, this.weatherIntensitySetting.getValue())
                     .putVec4(this.weatherFallSpeedSetting.getValue(), this.weatherParticleSizeSetting.getValue(), this.weatherDistanceSetting.getValue(), this.canRenderShaderSky() ? 1.0F : 0.0F)
                     .putVec4(windX, windZ, this.groundEffectDurationSetting.getValue(), this.groundEffectSizeSetting.getValue())
                     .putVec4(weatherColor)
                     .putVec4(positiveModulo(this.weatherVerticalOffset, verticalPeriod), (float)(this.weatherWindOffsetX % В西К), (float)(this.weatherWindOffsetZ % В西I), positiveModulo(this.weatherFallPhase, В西Т))
                     .putVec4(relativeCellX, relativeCellZ, wrappedCellX, wrappedCellZ)
                     .putVec4(particleDirectionX, particleDirectionY, particleDirectionZ, horizontalWindSpeed)
                     .putVec4(verticalPeriod, cellSize, wrappedCameraY, (В西О + В西Ф * this.weatherIntensitySetting.getValue()) * weatherColor.w)
                     .get();
                  CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                  if (weatherKind == 1) {
                     this.rainRoofHeightmap.Ы(MINECRAFT_CLIENT.level, cameraWorldPosition, encoder);
                  } else {
                     this.rainRoofHeightmap.close();
                  }

                  this.weatherUniformBuffer = this.ensureWeatherUniformBuffer();
                  encoder.writeToBuffer(this.weatherUniformBuffer.slice(), uniformData);
                  if (this.weatherGroundEffectsSetting.getValue()) {
                     this.renderWeatherGroundEffects(renderTarget, this.weatherUniformBuffer, fogUniforms, encoder);
                  }

                  this.renderWeatherParticles(renderTarget, this.weatherUniformBuffer, fogUniforms, encoder, instanceCount);
               }
            }
         } else {
            this.previousWeatherFrameNanos = 0L;
         }
      } else {
         this.previousWeatherFrameNanos = 0L;
         this.rainRoofHeightmap.close();
      }
   }

   private void onFogRender(FogRenderEvent fogEvent) {
      if (this.shouldOverrideFog(fogEvent.getCamera())) {
         if (this.isFogClearEnabled()) {
            fogEvent.setDistanceOverride(ВЗд, ВЗБ);
         } else {
            float baseEndDistance = Math.max(ВЗС, fogEvent.getBaseEndDistance());
            if (this.isFogOverrideEnabled()) {
               fogEvent.setDistanceOverride(ВЗз, ВЗц);
               fogEvent.setColorOverride(this.fogColorSetting.getValue() | ВЗя);
            } else {
               float weatherIntensity = Math.clamp(this.weatherIntensitySetting.getValue(), ВЕс, 1.0F);
               float blizzardFogEnd = baseEndDistance * Mth.lerp(weatherIntensity, ВЕВ, ВЕЯ);
               fogEvent.setDistanceOverride(blizzardFogEnd * ВЕХ, blizzardFogEnd);
               fogEvent.setColorOverride(ВЕе);
            }
         }
      }
   }

   private void renderBlizzardOverlay(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix) {
      if (this.canRenderWeather() && this.weatherModeSetting.isSelected(RENDER_BLIZZARD_OVERLAY_BLIZZARD_OPTION_LABEL) && sourceProjectionMatrix != null && sourceViewMatrix != null) {
         RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getDepthTexture() != null && !renderTarget.getDepthTexture().isClosed()) {
            Vec3 cameraWorldPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            Matrix4f inverseProjection = this.weatherInverseProjectionMatrix.set(sourceProjectionMatrix).invert();
            Matrix4f inverseView = this.weatherInverseViewMatrix.set(sourceViewMatrix).invert();
            float weatherTime = (float)this.weatherElapsedSeconds;
            float windSpeed = Math.max(this.windSpeedSetting.getValue(), ВЕ0) + ВЕх + (float)Math.sin(weatherTime * ВЕУ) * ВЕЗ + (float)Math.sin(weatherTime * ВЕЕ + ВЕП) * ВЕ西;
            float windDegrees = this.windDirectionSetting.getValue() + (float)Math.sin(weatherTime * ВЕд) * ВЕБ;
            float windRadians = (float)Math.toRadians(windDegrees);
            float windDirectionX = (float)Math.sin(windRadians);
            float windDirectionZ = (float)Math.cos(windRadians);
            float normalizedWindSpeed = Math.clamp(windSpeed / ВЕС, ВЕз, ВЕц);
            Vector4f weatherColor = ColorMath.toRgbaVector(this.resolveWeatherColor(this.snowColorSetting.getValue()));

            try (MemoryStack stack = MemoryStack.stackPush()) {
               ByteBuffer uniformData = Std140Builder.onStack(stack, 208)
                  .putMat4f(inverseProjection)
                  .putMat4f(inverseView)
                  .putVec4(positiveModulo(cameraWorldPosition.x, ВЕя), (float)cameraWorldPosition.y, positiveModulo(cameraWorldPosition.z, ВПс), weatherTime)
                  .putVec4(this.weatherIntensitySetting.getValue(), normalizedWindSpeed, this.weatherDistanceSetting.getValue(), 0.0F)
                  .putVec4((float)(this.weatherWindOffsetX % ВПВ), (float)(this.weatherWindOffsetZ % ВПЯ), windDirectionX, windDirectionZ)
                  .putVec4(weatherColor.x, weatherColor.y, weatherColor.z, 1.0F)
                  .get();

               try (
                  GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> BLIZZARD_OVERLAY_UNIFORM_BUFFER_DEBUG_LABEL, 128, uniformData);
                  RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> AMBIENCE_BLIZZARD_OVERLAY_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty());
               ) {
                  renderPass.setPipeline(this.blizzardPipeline);
                  renderPass.bindTexture(RENDER_BLIZZARD_OVERLAY_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  renderPass.setUniform(RENDER_BLIZZARD_OVERLAY_BLIZZARD_UNIFORM_NAME, uniformBuffer);
                  renderPass.draw(3, 1, 0, 0);
               }
            }
         }
      }
   }

   public void renderShaderSky(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, DeltaTracker deltaTracker) {
      if (this.canRenderShaderSky() && sourceProjectionMatrix != null && sourceViewMatrix != null && deltaTracker != null) {
         RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getDepthTexture() != null && !renderTarget.getDepthTexture().isClosed()) {
            if (this.shaderSkyStartMillis < 0L) {
               this.shaderSkyStartMillis = System.currentTimeMillis();
            }

            int width = Math.max(1, (renderTarget.width + 1 - 1) / 1);
            int height = Math.max(1, (renderTarget.height + 1 - 1) / 1);
            this.ensureShaderSkyTarget(width, height);
            this.ensureShaderSkyUniformBuffers();
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            encoder.clearColorTexture(this.shaderSkyTarget.getColorTexture(), new Vector4f(0.0F));
            float verticalFovDegrees = getVerticalFovDegrees(sourceProjectionMatrix);
            if (this.isPulsarSkySelected()) {
               this.renderShaderMistSky(renderTarget, encoder, width, height, verticalFovDegrees, this.pulsarSkyPipeline);
            } else if (this.isGalaxySkySelected()) {
               this.renderShaderMistSky(renderTarget, encoder, width, height, verticalFovDegrees, this.galaxySkyPipeline);
            } else if (this.isNightixAuroraSelected()) {
               int primaryArgb = this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.auroraPrimaryColorSetting.getValue();
               int secondaryArgb = this.useThemeColorsSetting.getValue()
                  ? ColorMath.packArgb(
                     Math.round(ColorMath.getRed(primaryArgb) * ВЕ8), Math.round(ColorMath.getGreen(primaryArgb) * ВЕ5), Math.round(ColorMath.getBlue(primaryArgb) * ВЕв), ВЕъ
                  )
                  : this.auroraSecondaryColorSetting.getValue();
               this.auroraRenderer
                  .renderAurora(
                     this.shaderSkyTarget,
                     sourceProjectionMatrix,
                     sourceViewMatrix,
                     this.shaderSkyStartMillis,
                     this.auroraStrengthSetting.getValue(),
                     this.auroraSpeedSetting.getValue(),
                     this.auroraScaleSetting.getValue(),
                     this.auroraStarsSetting.getValue(),
                     primaryArgb,
                     secondaryArgb,
                     this.vanillaSkySetting.getValue()
                  );
            } else if (this.isJuiceSkySelected()) {
               this.renderJuiceSky(renderTarget, encoder, width, height, verticalFovDegrees);
            } else {
               this.renderShaderFogSky(renderTarget, encoder, sourceProjectionMatrix, sourceViewMatrix, deltaTracker, width, height, verticalFovDegrees);
            }

            try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_SHADER_SKY_COMPOSITE_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
               renderPass.setPipeline(this.skyCompositePipeline);
               renderPass.bindTexture(RENDER_SHADER_SKY_SKY_SAMPLER_NAME, this.shaderSkyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.bindTexture(RENDER_SHADER_SKY_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               renderPass.draw(3, 1, 0, 0);
            }
         }
      } else {
         this.releaseShaderSkyResources();
      }
   }

   private boolean isPulsarOrGalaxySkySelected() {
      return this.isPulsarSkySelected() || this.isGalaxySkySelected();
   }

   private boolean isPulsarSkySelected() {
      return this.skyShaderSetting.isSelected(IS_PULSAR_SKY_SELECTED_PULSAR_OPTION_LABEL);
   }

   private TextureTarget ensureWeatherGroundTarget(int width, int height) {
      if (this.weatherGroundTarget == null || this.weatherGroundTarget.width != width || this.weatherGroundTarget.height != height) {
         this.destroyWeatherGroundTarget();
         this.weatherGroundTarget = new TextureTarget(WEATHER_GROUND_TARGET_DEBUG_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      }

      return this.weatherGroundTarget;
   }

   private void ensureWorldColorBuffer() {
      if (this.worldColorUniformBuffer == null || this.worldColorUniformBuffer.isClosed()) {
         this.worldColorUniformBuffer = RenderSystem.getDevice().createBuffer(() -> WORLD_COLOR_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВЕб);
         this.cachedWorldTintArgb = ВЕЫ;
         this.cachedWorldTintStrength = Float.NaN;
      }
   }

   private static float smoothstep01(float progress) {
      progress = Math.clamp(progress, 0.0F, 1.0F);
      return progress * progress * (В西弟 - 2.0F * progress);
   }

   private void scheduleThunderSound(Vec3 cameraWorldPosition) {
      if (this.thunderSoundsSetting.getValue() && !(this.thunderVolumeSetting.getValue() <= 0.0F) && !MINECRAFT_CLIENT.isPaused() && !Rain.Пз() && this.lightningGroundWorldPosition != null) {
         if (this.thunderDelayTicks < 0 && (this.thunderSound == null || !MINECRAFT_CLIENT.getSoundManager().isActive(this.thunderSound))) {
            this.pendingThunderWorldPosition = this.lightningGroundWorldPosition;
            this.thunderDelayTicks = Math.clamp((int)Math.ceil(cameraWorldPosition.distanceTo(this.lightningGroundWorldPosition) / ВП_ * ВПШ), 2, 12);
         }
      }
   }

   public float getFogEnd() {
      return this.fogEndSetting.getValue();
   }

   private boolean canRenderWeather() {
      return this.isEnabled()
         && this.hasPrecipitationSelection()
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.gameRenderer != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera() != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
   }

   private boolean hasPrecipitationSelection() {
      return this.weatherModeSetting.isSelected(HAS_PRECIPITATION_SELECTION_RAIN_OPTION_LABEL) || this.weatherModeSetting.isSelected(HAS_PRECIPITATION_SELECTION_SNOW_OPTION_LABEL) || this.weatherModeSetting.isSelected(HAS_PRECIPITATION_SELECTION_BLIZZARD_OPTION_LABEL);
   }

   public boolean shouldHideVanillaSky() {
      return this.isEnabled() && this.hasSkyShaderSelection() && (!this.isNightixAuroraSelected() || !this.vanillaSkySetting.getValue());
   }

   private boolean isGalaxySkySelected() {
      return this.skyShaderSetting.isSelected(IS_GALAXY_SKY_SELECTED_GALAXY_OPTION_LABEL);
   }

   private boolean canRenderShaderSky() {
      return HpHelper.method_2西()
         ? false
         : this.isEnabled()
            && this.hasSkyShaderSelection()
            && (!this.isGenericSkyShaderSelected() && !this.isJuiceSkySelected() || this.shaderIntensitySetting.getValue() > 0.0F)
            && MINECRAFT_CLIENT.player != null
            && MINECRAFT_CLIENT.level != null
            && MINECRAFT_CLIENT.gameRenderer != null
            && isCameraInAirOrWater(MINECRAFT_CLIENT.gameRenderer.mainCamera());
   }

   private void renderVolumeFog(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, DeltaTracker deltaTracker) {
      if (this.isFogOverrideEnabled() && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && this.shouldOverrideFog(MINECRAFT_CLIENT.gameRenderer.mainCamera()) && !Rain.Пз()) {
         float renderDistance = Math.min(ВЕА, Math.max(ВЕ4, MINECRAFT_CLIENT.options.getEffectiveRenderDistance() * ВЕп));
         float fogStartDistance = this.getFogStart() * ВЕД;
         float fogEndDistance = Math.max(fogStartDistance + ВЕЪ, this.getFogEnd() * renderDistance);
         float worldTimeSeconds = ((float)(MINECRAFT_CLIENT.level.getGameTime() % ВЕт) + deltaTracker.getGameTimeDeltaPartialTick(false)) / ВЕа;
         this.volumeFogRenderer
            .renderVolumeFog(MINECRAFT_CLIENT.gameRenderer.mainRenderTarget(), sourceProjectionMatrix, sourceViewMatrix, MINECRAFT_CLIENT.gameRenderer.mainCamera().position(), worldTimeSeconds, this.fogColorSetting.getValue(), fogStartDistance, fogEndDistance);
      } else {
         this.volumeFogRenderer.close();
      }
   }

   private void ensureLightningGlowUniformBuffers() {
      if (this.lightningGlowHorizontalUniformBuffer == null || this.lightningGlowHorizontalUniformBuffer.isClosed()) {
         this.lightningGlowHorizontalUniformBuffer = RenderSystem.getDevice().createBuffer(() -> LIGHTNING_GLOW_HORIZONTAL_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВПл);
      }

      if (this.lightningGlowVerticalUniformBuffer == null || this.lightningGlowVerticalUniformBuffer.isClosed()) {
         this.lightningGlowVerticalUniformBuffer = RenderSystem.getDevice().createBuffer(() -> LIGHTNING_GLOW_VERTICAL_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВПэ);
      }

      if (this.lightningGlowCompositeUniformBuffer == null || this.lightningGlowCompositeUniformBuffer.isClosed()) {
         this.lightningGlowCompositeUniformBuffer = RenderSystem.getDevice().createBuffer(() -> LIGHTNING_GLOW_COMPOSITE_UNIFORM_BUFFER_DEBUG_LABEL, 136, ВПю);
      }
   }

   private void renderWeatherGroundEffects(RenderTarget renderTarget, GpuBuffer weatherUniforms, GpuBufferSlice fogUniforms, CommandEncoder encoder) {
      TextureTarget groundTarget = this.ensureWeatherGroundTarget(Math.max(1, (renderTarget.width + 2 - 1) / 2), Math.max(1, (renderTarget.height + 2 - 1) / 2));

      try (RenderPass groundPass = encoder.createRenderPass(() -> AMBIENCE_CUSTOM_WEATHER_GROUND_EFFECTS_PASS_DEBUG_LABEL, groundTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
         boolean isRain = this.weatherModeSetting.isSelected(RENDER_WEATHER_GROUND_EFFECTS_RAIN_OPTION_LABEL);
         groundPass.setPipeline(isRain ? this.rainGroundPipeline : this.snowGroundPipeline);
         if (isRain) {
            this.rainRoofHeightmap.Ж(groundPass);
         }

         groundPass.bindTexture(RENDER_WEATHER_GROUND_EFFECTS_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         groundPass.setUniform(RENDER_WEATHER_GROUND_EFFECTS_WEATHER_UNIFORM_NAME, weatherUniforms);
         groundPass.setUniform(RENDER_WEATHER_GROUND_EFFECTS_FOG_UNIFORM_NAME, fogUniforms);
         this.drawUnitQuad(groundPass, 1);
      }

      try (RenderPass compositePass = encoder.createRenderPass(() -> AMBIENCE_CUSTOM_WEATHER_GROUND_COMPOSITE_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
         compositePass.setPipeline(this.weatherGroundCompositePipeline);
         compositePass.bindTexture(RENDER_WEATHER_GROUND_EFFECTS_SAMPLER0_NAME, groundTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         this.drawUnitQuad(compositePass);
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.stopThunderSound();
      this.weatherElapsedSeconds = 0.0;
      this.previousWeatherFrameNanos = 0L;
      this.weatherVerticalOffset = 0.0;
      this.weatherWindOffsetX = 0.0;
      this.weatherWindOffsetZ = 0.0;
      this.weatherFallPhase = 0.0;
      this.lightningSegments.clear();
      this.lightningStartNanos = 0L;
      this.nextLightningNanos = 0L;
      this.lightningTopWorldPosition = null;
      this.lightningGroundWorldPosition = null;
      if (this.lightningMaskTarget != null) {
         this.lightningMaskTarget.destroyBuffers();
         this.lightningMaskTarget = null;
      }

      this.releaseLightningGlowResources();
      this.releaseLightningLayerBuffers();
      this.releaseShaderSkyResources();
      this.releaseWorldColorBuffer();
      this.volumeFogRenderer.close();
      this.rainRoofHeightmap.close();
      this.releaseWeatherUniformBuffer();
      this.destroyWeatherGroundTarget();
      this.releaseUnitQuadBuffer();
   }

   @Override
   public void loadConfig(JsonObject configJson) {
      String legacySkySelection = null;
      if (configJson != null && configJson.has(SETTINGS_OBJECT_PROBE_KEY)) {
         JsonObject settingsJson = configJson.getAsJsonObject(SETTINGS_OBJECT_READ_KEY);
         this.migrateLegacySkyOptions(settingsJson);
         if (!settingsJson.has(this.skyShaderSetting.getName())) {
            if (settingsJson.has(LEGACY_SHADER_SKY_PROBE_KEY) && settingsJson.get(LEGACY_SHADER_SKY_READ_KEY).getAsBoolean()) {
               legacySkySelection = LEGACY_SHADER_SKY_ENABLED_PULSAR_LABEL;
            } else if (settingsJson.has(LEGACY_SHADER_FOG_PROBE_KEY)) {
               String legacyFogShader = settingsJson.get(LEGACY_SHADER_FOG_READ_KEY).getAsString();
               if (!legacyFogShader.equals(LEGACY_SHADER_FOG_NONE_LABEL)) {
                  legacySkySelection = legacyFogShader;
               }
            }
         }
      }

      super.loadConfig(configJson);
      if (legacySkySelection != null) {
         this.selectSkyShaderByName(legacySkySelection);
      }
   }

   private static boolean isCameraInAirOrWater(Camera camera) {
      if (camera == null) {
         return false;
      }

      FogType cameraFluid = camera.getFluidInCamera();
      return cameraFluid == FogType.NONE || cameraFluid == FogType.WATER;
   }

   private void selectSkyShaderByName(String optionName) {
      for (ChoiceOption option : this.skyShaderSetting.getValue()) {
         if (option.getName().equals(optionName)) {
            this.skyShaderSetting.setSelectedOption(option);
            return;
         }
      }
   }

   private void renderShaderMistSky(RenderTarget renderTarget, CommandEncoder encoder, int width, int height, float verticalFovDegrees, RenderPipeline pipeline) {
      float elapsedSeconds = (float)(System.currentTimeMillis() - this.shaderSkyStartMillis) / ВЕн;
      Vector4f themeColor = ColorMath.toRgbaVector(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      float negativeYawRadians = (float)Math.toRadians(-MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot());
      float pitchRadians = (float)Math.toRadians(MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot());
      float fovDegrees = verticalFovDegrees;

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 80)
            .putVec4(width, height, elapsedSeconds, fovDegrees)
            .putVec4(themeColor.x, themeColor.y, themeColor.z, this.skyOpacitySetting.getValue())
            .putVec4(this.skySpeedSetting.getValue(), this.skySizeSetting.getValue(), this.skyIntensitySetting.getValue(), 0.0F)
            .putVec4(negativeYawRadians, pitchRadians, 0.0F, 0.0F)
            .putVec4(0.0F, ВЕЦ, 1.0F, 0.0F)
            .get();
         encoder.writeToBuffer(this.shaderMistUniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_SHADER_SKY_PASS_DEBUG_LABEL, this.shaderSkyTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(pipeline);
            renderPass.bindTexture(RENDER_SHADER_MIST_SKY_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.setUniform(RENDER_SHADER_MIST_SKY_SHADER_MIST_UNIFORM_NAME, this.shaderMistUniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private static float positiveModulo(double value, double period) {
      return (float)(value - Math.floor(value / period) * period);
   }

   private static void subdivideLightningOffsets(double[] offsets, int startIndex, int endIndex, double displacement, Random random) {
      if (endIndex - startIndex > 1) {
         int midpointIndex = startIndex + endIndex >>> 1;
         offsets[midpointIndex] = (offsets[startIndex] + offsets[endIndex]) * В西Х + (random.nextDouble() * В西е - 1.0) * displacement;
         subdivideLightningOffsets(offsets, startIndex, midpointIndex, displacement * В西А, random);
         subdivideLightningOffsets(offsets, midpointIndex, endIndex, displacement * В西4, random);
      }
   }

   private TextureTarget ensureLightningMaskTarget(int width, int height) {
      width = Math.max(1, width);
      height = Math.max(1, height);
      if (this.lightningMaskTarget == null) {
         this.lightningMaskTarget = new TextureTarget(LIGHTNING_MASK_TARGET_DEBUG_LABEL, width, height, true, GpuFormat.RGBA8_UNORM);
      } else if (this.lightningMaskTarget.width != width || this.lightningMaskTarget.height != height) {
         this.lightningMaskTarget.resize(width, height);
      }

      return this.lightningMaskTarget;
   }

   private static float getVerticalFovDegrees(Matrix4f sourceProjectionMatrix) {
      float absoluteVerticalScale = Math.abs(sourceProjectionMatrix.m11());
      return absoluteVerticalScale < ВдЪ ? Вдт : (float)Math.toDegrees(Вда * Math.atan(1.0 / absoluteVerticalScale));
   }

   public boolean shouldOverrideFog(Camera camera) {
      return this.isEnabled()
         && !this.isFogAtmosphereEnabled()
         && isCameraInAirOrWater(camera)
         && (this.isFogClearEnabled() || this.isFogOverrideEnabled() || camera.getFluidInCamera() == FogType.NONE && this.isBlizzardFogSelected());
   }

   public void renderLightning(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix) {
      this.renderLightningEffects(sourceProjectionMatrix, sourceViewMatrix);
   }

   private void scheduleNextLightning(long nowNanos) {
      double randomDelayFactor = ThreadLocalRandom.current().nextDouble(В西п, В西Д);
      float delaySeconds = Math.max(В西Ъ, 2.0F / Math.max(В西т, this.lightningFrequencySetting.getValue()));
      this.nextLightningNanos = nowNanos + (long)(delaySeconds * randomDelayFactor * В西а);
   }

   private void renderLightningGlow(RenderTarget sourceTarget, RenderTarget destinationTarget, int primaryArgb, int secondaryArgb, float primaryStrength, float secondaryStrength) {
      if (sourceTarget != null && destinationTarget != null && sourceTarget.getColorTextureView() != null && destinationTarget.getColorTextureView() != null && !(Math.max(primaryStrength, secondaryStrength) <= ВПl)) {
         int width = Math.max(1, sourceTarget.width / 2);
         int height = Math.max(1, sourceTarget.height / 2);
         this.ensureLightningGlowTargets(width, height);
         this.ensureLightningGlowUniformBuffers();
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         this.writeLightningGlowUniforms(encoder, width, height, primaryArgb, secondaryArgb, primaryStrength, secondaryStrength);

         try (RenderPass maskPass = encoder.createRenderPass(() -> AMBIENCE_LIGHTNING_GLOW_MASK_PASS_DEBUG_LABEL, this.lightningGlowPingTarget.getColorTextureView(), Optional.empty())) {
            maskPass.setPipeline(this.lightningGlowMaskPipeline);
            maskPass.bindTexture(RENDER_LIGHTNING_GLOW_IN_SAMPLER_NAME, sourceTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            maskPass.draw(3, 1, 0, 0);
         }

         this.renderLightningGlowBlur(encoder, this.lightningGlowPingTarget, this.lightningGlowPongTarget, this.lightningGlowHorizontalUniformBuffer);
         this.renderLightningGlowBlur(encoder, this.lightningGlowPongTarget, this.lightningGlowPingTarget, this.lightningGlowVerticalUniformBuffer);

         try (RenderPass compositePass = encoder.createRenderPass(() -> AMBIENCE_LIGHTNING_GLOW_COMPOSITE_PASS_DEBUG_LABEL, destinationTarget.getColorTextureView(), Optional.empty())) {
            compositePass.setPipeline(this.lightningGlowCompositePipeline);
            compositePass.bindTexture(RENDER_LIGHTNING_GLOW_BLOOM_SAMPLER_NAME, this.lightningGlowPingTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            compositePass.setUniform(RENDER_LIGHTNING_GLOW_LIGHTNING_GLOW_COMPOSITE_UNIFORM_NAME, this.lightningGlowCompositeUniformBuffer);
            compositePass.draw(3, 1, 0, 0);
         }
      }
   }

   private void renderWorldColor() {
      if (!this.canRenderWorldColor()) {
         this.releaseWorldColorBuffer();
      } else {
         RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getColorTextureView() != null) {
            this.ensureWorldColorBuffer();
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            int tintArgb = this.worldColorModeSetting.isSelected(RENDER_WORLD_COLOR_THEME_OPTION_LABEL) ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.worldTintSetting.getValue();
            float tintStrength = this.worldTintStrengthSetting.getValue();
            if (tintArgb != this.cachedWorldTintArgb || Float.compare(tintStrength, this.cachedWorldTintStrength) != 0) {
               Vector4f tintColor = ColorMath.toRgbaVector(tintArgb | ВЕЛ);
               float luminance = Math.max(tintColor.x * ВЕ诶 + tintColor.y * ВЕ1 + tintColor.z * ВЕЬ, ВЕ衣);
               float normalizedRed = Mth.lerp(ВЕш, 1.0F, tintColor.x / luminance);
               float normalizedGreen = Mth.lerp(ВЕИ, 1.0F, tintColor.y / luminance);
               float normalizedBlue = Mth.lerp(ВЕь, 1.0F, tintColor.z / luminance);

               try (MemoryStack stack = MemoryStack.stackPush()) {
                  ByteBuffer uniformData = Std140Builder.onStack(stack, 16)
                     .putVec4(Mth.lerp(tintStrength, 1.0F, normalizedRed), Mth.lerp(tintStrength, 1.0F, normalizedGreen), Mth.lerp(tintStrength, 1.0F, normalizedBlue), 1.0F)
                     .get();
                  encoder.writeToBuffer(this.worldColorUniformBuffer.slice(), uniformData);
               }

               this.cachedWorldTintArgb = tintArgb;
               this.cachedWorldTintStrength = tintStrength;
            }

            try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_WORLD_COLOR_PASS_DEBUG_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
               renderPass.setPipeline(this.worldColorPipeline);
               renderPass.setUniform(RENDER_WORLD_COLOR_WORLD_COLOR_UNIFORM_NAME, this.worldColorUniformBuffer);
               renderPass.draw(3, 1, 0, 0);
            }
         }
      }
   }

   public float getFogStart() {
      return this.fogStartSetting.getValue();
   }

   private void ensureLightningGlowTargets(int width, int height) {
      if (this.lightningGlowPingTarget == null || this.lightningGlowPongTarget == null) {
         this.lightningGlowPingTarget = new TextureTarget(LIGHTNING_GLOW_PING_TARGET_DEBUG_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         this.lightningGlowPongTarget = new TextureTarget(LIGHTNING_GLOW_PONG_TARGET_DEBUG_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.lightningGlowPingTarget.width != width || this.lightningGlowPingTarget.height != height) {
         this.lightningGlowPingTarget.resize(width, height);
         this.lightningGlowPongTarget.resize(width, height);
      }
   }

   public Ambience() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.snowParticlePipeline = RenderPipelineBuilder.builder()
         .withLocation(SNOW_PARTICLE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(SNOW_PARTICLE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(SNOW_PARTICLE_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(SNOW_PARTICLE_PIPELINE_WEATHER_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SNOW_PARTICLE_PIPELINE_FOG_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .build();
      this.rainParticlePipeline = RenderPipelineBuilder.builder()
         .withLocation(RAIN_PARTICLE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(RAIN_PARTICLE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(RAIN_PARTICLE_PIPELINE_FRAGMENT_SHADER_ID))
         .withShaderDefine(RAIN_PARTICLE_PIPELINE_ROSE_RAIN_SHADER_DEFINE)
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(RAIN_PARTICLE_PIPELINE_WEATHER_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(RAIN_PARTICLE_PIPELINE_FOG_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(RAIN_PARTICLE_PIPELINE_RAIN_ROOF_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(RAIN_PARTICLE_PIPELINE_ROOF_SAMPLER_NAME)
         .build();
      this.rainGroundPipeline = RenderPipelineBuilder.builder()
         .withLocation(RAIN_GROUND_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(RAIN_GROUND_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(RAIN_GROUND_PIPELINE_FRAGMENT_SHADER_ID))
         .withShaderDefine(RAIN_GROUND_PIPELINE_ROSE_RAIN_SHADER_DEFINE)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(RAIN_GROUND_PIPELINE_WEATHER_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(RAIN_GROUND_PIPELINE_FOG_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(RAIN_GROUND_PIPELINE_RAIN_ROOF_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(RAIN_GROUND_PIPELINE_DEPTH_SAMPLER_NAME)
         .withSampler(RAIN_GROUND_PIPELINE_ROOF_SAMPLER_NAME)
         .build();
      this.snowGroundPipeline = RenderPipelineBuilder.builder()
         .withLocation(SNOW_GROUND_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(SNOW_GROUND_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(SNOW_GROUND_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(SNOW_GROUND_PIPELINE_WEATHER_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SNOW_GROUND_PIPELINE_FOG_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(SNOW_GROUND_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.blizzardPipeline = RenderPipelineBuilder.builder()
         .withLocation(BLIZZARD_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(BLIZZARD_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(BLIZZARD_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(BLIZZARD_PIPELINE_BLIZZARD_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(BLIZZARD_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.shaderFogPipeline = RenderPipelineBuilder.builder()
         .withLocation(SHADER_FOG_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(SHADER_FOG_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(SHADER_FOG_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(SHADER_FOG_PIPELINE_SHADER_FOG_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(SHADER_FOG_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.pulsarSkyPipeline = RenderPipelineBuilder.builder()
         .withLocation(PULSAR_SKY_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(PULSAR_SKY_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(PULSAR_SKY_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(PULSAR_SKY_PIPELINE_SHADER_MIST_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(PULSAR_SKY_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.galaxySkyPipeline = RenderPipelineBuilder.builder()
         .withLocation(GALAXY_SKY_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(GALAXY_SKY_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(GALAXY_SKY_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(GALAXY_SKY_PIPELINE_SHADER_MIST_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(GALAXY_SKY_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.juiceSkyPipeline = RenderPipelineBuilder.builder()
         .withLocation(JUICE_SKY_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(JUICE_SKY_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(JUICE_SKY_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(JUICE_SKY_PIPELINE_SKY_SHADER_DATA_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(JUICE_SKY_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.skyCompositePipeline = RenderPipelineBuilder.builder()
         .withLocation(SKY_COMPOSITE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(SKY_COMPOSITE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(SKY_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(SKY_COMPOSITE_PIPELINE_SKY_SAMPLER_NAME)
         .withSampler(SKY_COMPOSITE_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.worldColorPipeline = RenderPipelineBuilder.builder()
         .withLocation(WORLD_COLOR_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(WORLD_COLOR_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(WORLD_COLOR_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(WORLD_COLOR_BLEND)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(WORLD_COLOR_PIPELINE_WORLD_COLOR_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .build();
      this.lightningPipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX, PrimitiveTopology.QUADS)
         .withUniform(LIGHTNING_PIPELINE_LIGHTNING_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .build();
      this.lightningCorePipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_CORE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_CORE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_CORE_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX, PrimitiveTopology.QUADS)
         .withUniform(LIGHTNING_CORE_PIPELINE_LIGHTNING_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .build();
      this.lightningFlashPipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_FLASH_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_FLASH_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_FLASH_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(LIGHTNING_FLASH_PIPELINE_LIGHTNING_FLASH_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(LIGHTNING_FLASH_PIPELINE_DEPTH_SAMPLER_NAME)
         .build();
      this.weatherGroundCompositePipeline = RenderPipelineBuilder.builder()
         .withLocation(WEATHER_GROUND_COMPOSITE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(WEATHER_GROUND_COMPOSITE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(WEATHER_GROUND_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withSampler(WEATHER_GROUND_COMPOSITE_PIPELINE_SAMPLER0_NAME)
         .build();
      this.lightningGlowMaskPipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_GLOW_MASK_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_MASK_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_MASK_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(LIGHTNING_GLOW_MASK_PIPELINE_IN_SAMPLER_NAME)
         .build();
      this.lightningGlowBlurPipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_GLOW_BLUR_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_BLUR_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_BLUR_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(LIGHTNING_GLOW_BLUR_PIPELINE_LIGHTNING_GLOW_BLUR_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(LIGHTNING_GLOW_BLUR_PIPELINE_IN_SAMPLER_NAME)
         .build();
      this.lightningGlowCompositePipeline = RenderPipelineBuilder.builder()
         .withLocation(LIGHTNING_GLOW_COMPOSITE_PIPELINE_LOCATION_ID)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_COMPOSITE_PIPELINE_VERTEX_SHADER_ID))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_GLOW_COMPOSITE_PIPELINE_FRAGMENT_SHADER_ID))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(LIGHTNING_GLOW_COMPOSITE_PIPELINE_LIGHTNING_GLOW_COMPOSITE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(LIGHTNING_GLOW_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME)
         .build();
      this.cachedWorldTintArgb = Вхз;
      this.cachedWorldTintStrength = Float.NaN;
      this.shaderSkyStartMillis = Вхц;
      this.lightningLayerVertexBuffers = new GpuBuffer[3];
      this.lightningLayerVertexCapacities = new long[3];
      this.lightningLayerUniformBuffers = new GpuBuffer[3];
      this.lightningSegments = new ArrayList<>();
      this.skyInverseProjectionMatrix = new Matrix4f();
      this.skyInverseViewMatrix = new Matrix4f();
      this.weatherInverseProjectionMatrix = new Matrix4f();
      this.weatherInverseViewMatrix = new Matrix4f();
      this.thunderDelayTicks = -1;
      this.fogModeSetting = new SingleChoiceSetting(FOG_MODE_SETTING_LABEL, new ChoiceOption(FOG_MODE_SETTING_OPTION_NO_CHANGE_LABEL), new ChoiceOption(FOG_MODE_SETTING_OPTION_CLEAR_LABEL), new ChoiceOption(FOG_MODE_SETTING_OPTION_OVERRIDE_LABEL));
      this.fogEndSetting = new FloatSetting(FOG_END_SETTING_LABEL, 1.0F, ВУе, ВУА).withStep(ВУ4).visibleWhen(() -> this.fogModeSetting.isSelected(FOG_END_OVERRIDE_VISIBILITY_OPTION_LABEL));
      this.fogStartSetting = new FloatSetting(FOG_START_SETTING_LABEL, ВУД, ВУЪ, ВУт).withStep(ВУа).visibleWhen(() -> this.fogModeSetting.isSelected(FOG_START_OVERRIDE_VISIBILITY_OPTION_LABEL)).withMaximum(this.fogEndSetting);
      this.useThemeColorsSetting = new BooleanSetting(USE_THEME_COLORS_SETTING_LABEL, true);
      this.fogColorSetting = new ColorSetting(FOG_COLOR_SETTING_LABEL, false, -1).visibleWhen(() -> this.fogModeSetting.isSelected(FOG_COLOR_OVERRIDE_VISIBILITY_OPTION_LABEL));
      this.skyShaderSetting = new SingleChoiceSetting(
         SKY_SHADER_SETTING_LABEL,
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_NONE_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_PULSAR_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_GALAXY_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_JUICE_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_SPACE_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_MILKY_WAY_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_PURPLE_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_AURORA_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_AURORA2_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_NEBULOSITY_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_PLASMA_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_SOURCE_LABEL),
         new ChoiceOption(SKY_SHADER_SETTING_OPTION_NEBULA_LABEL)
      );
      this.shaderIntensitySetting = new FloatSetting(SHADER_INTENSITY_SETTING_LABEL, 1.0F, 0.0F, ВУв).withStep(ВУъ).visibleWhen(() -> this.isGenericSkyShaderSelected() || this.isJuiceSkySelected());
      this.shaderSpeedSetting = new FloatSetting(SHADER_SPEED_SETTING_LABEL, ВУН, 0.0F, ВУн).withStep(ВУЦ).visibleWhen(this::isGenericSkyShaderSelected);
      this.starsSetting = new BooleanSetting(STARS_SETTING_LABEL, false).visibleWhen(this::isSourceOrNebulaSkySelected);
      this.skySpeedSetting = new FloatSetting(SKY_SPEED_SETTING_LABEL, 1.0F, ВУ必, ВУщ).withStep(ВУГ).visibleWhen(() -> this.isPulsarOrGalaxySkySelected() || this.isJuiceSkySelected());
      this.skySizeSetting = new FloatSetting(SKY_SIZE_SETTING_LABEL, ВУэ, 1.0F, ВУю).withStep(ВУЩ).visibleWhen(this::isPulsarSkySelected);
      this.skyIntensitySetting = new FloatSetting(SKY_INTENSITY_SETTING_LABEL, ВУ2, ВУф, ВУи).withStep(ВУ9).visibleWhen(this::isPulsarSkySelected);
      this.skyOpacitySetting = new FloatSetting(SKY_OPACITY_SETTING_LABEL, 1.0F, ВУO, 1.0F).withStep(ВУ7).visibleWhen(() -> this.isPulsarOrGalaxySkySelected() || this.isJuiceSkySelected());
      this.auroraStrengthSetting = new FloatSetting(AURORA_STRENGTH_SETTING_LABEL, 1.0F, ВУ3, 1.0F).withStep(ВУр).visibleWhen(this::isNightixAuroraSelected);
      this.auroraSpeedSetting = new FloatSetting(AURORA_SPEED_SETTING_LABEL, 1.0F, ВУ_, ВУШ).withStep(ВУы).visibleWhen(this::isNightixAuroraSelected);
      this.auroraScaleSetting = new FloatSetting(AURORA_SCALE_SETTING_LABEL, ВУЖ, ВУм, ВУК).withStep(ВУI).visibleWhen(this::isNightixAuroraSelected);
      this.auroraStarsSetting = new FloatSetting(AURORA_STARS_SETTING_LABEL, ВУО, 0.0F, 1.0F).withStep(ВУФ).visibleWhen(this::isNightixAuroraSelected);
      this.auroraPrimaryColorSetting = new ColorSetting(AURORA_PRIMARY_COLOR_SETTING_LABEL, false, ВУк).visibleWhen(() -> this.isNightixAuroraSelected() && !this.useThemeColorsSetting.getValue());
      this.auroraSecondaryColorSetting = new ColorSetting(AURORA_SECONDARY_COLOR_SETTING_LABEL, false, ВУ0).visibleWhen(() -> this.isNightixAuroraSelected() && !this.useThemeColorsSetting.getValue());
      this.vanillaSkySetting = new BooleanSetting(VANILLA_SKY_SETTING_LABEL, false).visibleWhen(this::isNightixAuroraSelected);
      this.weatherModeSetting = new SingleChoiceSetting(
         WEATHER_MODE_SETTING_LABEL, new ChoiceOption(WEATHER_MODE_SETTING_OPTION_NO_CHANGE_LABEL), new ChoiceOption(WEATHER_MODE_SETTING_OPTION_CLEAR_LABEL), new ChoiceOption(WEATHER_MODE_SETTING_OPTION_RAIN_LABEL), new ChoiceOption(WEATHER_MODE_SETTING_OPTION_SNOW_LABEL), new ChoiceOption(WEATHER_MODE_SETTING_OPTION_BLIZZARD_LABEL)
      );
      this.timeOfDaySetting = new TimeOfDaySetting(
         TIME_OF_DAY_SETTING_LABEL,
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_NORMAL_LABEL),
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_MORNING_LABEL),
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_DAY_LABEL),
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_EVENING_LABEL),
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_SUNSET_LABEL),
         new ChoiceOption(TIME_OF_DAY_SETTING_OPTION_NIGHT_LABEL)
      );
      this.worldColorModeSetting = new SingleChoiceSetting(WORLD_COLOR_MODE_SETTING_LABEL, new ChoiceOption(WORLD_COLOR_MODE_SETTING_OPTION_NONE_LABEL), new ChoiceOption(WORLD_COLOR_MODE_SETTING_OPTION_THEME_LABEL), new ChoiceOption(WORLD_COLOR_MODE_SETTING_OPTION_CUSTOM_LABEL));
      this.worldTintSetting = new ColorSetting(WORLD_TINT_SETTING_LABEL, false, -1).visibleWhen(() -> this.worldColorModeSetting.isSelected(WORLD_TINT_CUSTOM_COLOR_VISIBILITY_OPTION_LABEL));
      this.worldTintStrengthSetting = new FloatSetting(WORLD_TINT_STRENGTH_SETTING_LABEL, ВЗД, 0.0F, ВЗЪ).withStep(ВЗт).visibleWhen(() -> !this.worldColorModeSetting.isSelected(WORLD_TINT_STRENGTH_NONE_VISIBILITY_OPTION_LABEL));
      this.weatherIntensitySetting = new FloatSetting(WEATHER_INTENSITY_SETTING_LABEL, ВЗЮ, ВЗЧ, 1.0F).withStep(ВЗЛ).visibleWhen(this::hasPrecipitationSelection);
      this.weatherDistanceSetting = new FloatSetting(WEATHER_DISTANCE_SETTING_LABEL, ВЗ1, ВЗЬ, ВЗ衣).withStep(1.0F).visibleWhen(this::hasPrecipitationSelection);
      this.weatherFallSpeedSetting = new FloatSetting(WEATHER_FALL_SPEED_SETTING_LABEL, 1.0F, ВЗИ, 2.0F).withStep(ВЗь).visibleWhen(this::hasPrecipitationSelection);
      this.weatherParticleSizeSetting = new FloatSetting(WEATHER_PARTICLE_SIZE_SETTING_LABEL, 1.0F, ВЗ弟, 2.0F).withStep(ВЗб).visibleWhen(this::hasPrecipitationSelection);
      this.windSpeedSetting = new FloatSetting(WIND_SPEED_SETTING_LABEL, ВЗl, 0.0F, ВЗ8).withStep(ВЗ5).visibleWhen(this::hasPrecipitationSelection);
      this.windDirectionSetting = new FloatSetting(WIND_DIRECTION_SETTING_LABEL, 0.0F, ВЗъ, ВЗМ).withStep(ВЗН).visibleWhen(this::hasPrecipitationSelection);
      this.weatherGroundEffectsSetting = new BooleanSetting(WEATHER_GROUND_EFFECTS_SETTING_LABEL, true).visibleWhen(this::hasPrecipitationSelection);
      this.groundEffectDurationSetting = new FloatSetting(GROUND_EFFECT_DURATION_SETTING_LABEL, ВЗч, ВЗЭ, ВЗ必).withStep(ВЗщ).visibleWhen(() -> this.hasPrecipitationSelection() && this.weatherGroundEffectsSetting.getValue());
      this.groundEffectSizeSetting = new FloatSetting(GROUND_EFFECT_SIZE_SETTING_LABEL, ВЗл, ВЗэ, ВЗю).withStep(ВЗЩ).visibleWhen(() -> this.hasPrecipitationSelection() && this.weatherGroundEffectsSetting.getValue());
      this.rainColorSetting = new ColorSetting(RAIN_COLOR_SETTING_LABEL, false, ВЗ2).visibleWhen(() -> this.weatherModeSetting.isSelected(RAIN_COLOR_RAIN_VISIBILITY_OPTION_LABEL) && !this.useThemeColorsSetting.getValue());
      this.snowColorSetting = new ColorSetting(SNOW_COLOR_SETTING_LABEL, false, -1)
         .visibleWhen(() -> (this.weatherModeSetting.isSelected(SNOW_COLOR_SNOW_VISIBILITY_OPTION_LABEL) || this.weatherModeSetting.isSelected(SNOW_COLOR_BLIZZARD_VISIBILITY_OPTION_LABEL)) && !this.useThemeColorsSetting.getValue());
      this.lightningSetting = new BooleanSetting(LIGHTNING_SETTING_LABEL, true);
      this.lightningFrequencySetting = new FloatSetting(LIGHTNING_FREQUENCY_SETTING_LABEL, 1.0F, ВЗо, ВЗO).withStep(ВЗ7).visibleWhen(this.lightningSetting::getValue);
      this.lightningCountSetting = new FloatSetting(LIGHTNING_COUNT_SETTING_LABEL, 1.0F, 1.0F, ВЗ3).withStep(1.0F).visibleWhen(this.lightningSetting::getValue);
      this.thunderSoundsSetting = new BooleanSetting(THUNDER_SOUNDS_SETTING_LABEL, false).visibleWhen(this.lightningSetting::getValue);
      this.thunderVolumeSetting = new FloatSetting(THUNDER_VOLUME_SETTING_LABEL, ВЗ_, 0.0F, 1.0F).withStep(ВЗШ).visibleWhen(() -> this.lightningSetting.getValue() && this.thunderSoundsSetting.getValue());
      this.removeCloudsSetting = new BooleanSetting(REMOVE_CLOUDS_SETTING_LABEL, false);
      this.fogRenderListener = this::onFogRender;
      this.clientTickListener = ignoredTickEvent -> this.updateThunderSound();
      this.addSettings(
         this.fogModeSetting,
         this.fogStartSetting,
         this.fogEndSetting,
         this.useThemeColorsSetting,
         this.fogColorSetting,
         this.skyShaderSetting,
         this.shaderIntensitySetting,
         this.shaderSpeedSetting,
         this.starsSetting,
         this.skySpeedSetting,
         this.skySizeSetting,
         this.skyIntensitySetting,
         this.skyOpacitySetting,
         this.auroraStrengthSetting,
         this.auroraSpeedSetting,
         this.auroraScaleSetting,
         this.auroraStarsSetting,
         this.auroraPrimaryColorSetting,
         this.auroraSecondaryColorSetting,
         this.vanillaSkySetting,
         this.timeOfDaySetting,
         this.worldColorModeSetting,
         this.worldTintSetting,
         this.worldTintStrengthSetting,
         this.weatherModeSetting,
         this.weatherIntensitySetting,
         this.weatherDistanceSetting,
         this.weatherFallSpeedSetting,
         this.weatherParticleSizeSetting,
         this.windSpeedSetting,
         this.windDirectionSetting,
         this.weatherGroundEffectsSetting,
         this.groundEffectDurationSetting,
         this.groundEffectSizeSetting,
         this.rainColorSetting,
         this.snowColorSetting,
         this.lightningSetting,
         this.lightningFrequencySetting,
         this.lightningCountSetting,
         this.thunderSoundsSetting,
         this.thunderVolumeSetting,
         this.removeCloudsSetting
      );
   }

   public void resetWorldEffects() {
      this.auroraRenderer.close();
      if (this.isNightixAuroraSelected()) {
         this.shaderSkyStartMillis = ВЕЮ;
      }

      this.stopThunderSound();
      this.volumeFogRenderer.close();
      this.rainRoofHeightmap.close();
      this.previousWeatherFrameNanos = 0L;
   }

   private boolean canRenderWorldColor() {
      return this.isEnabled() && !this.worldColorModeSetting.isSelected(CAN_RENDER_WORLD_COLOR_NONE_OPTION_LABEL) && this.worldTintStrengthSetting.getValue() > 0.0F && MINECRAFT_CLIENT.level != null;
   }

   private void generateLightningBolt(Vec3 cameraWorldPosition) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      double negativeYawRadians = Math.toRadians(-MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot());
      double boltYawRadians = negativeYawRadians + random.nextDouble(ВПы, ВПй);
      double boltDistance = random.nextDouble(ВПЖ, ВПм);
      double groundX = cameraWorldPosition.x + Math.sin(boltYawRadians) * boltDistance;
      double groundZ = cameraWorldPosition.z + Math.cos(boltYawRadians) * boltDistance;
      int groundY = MINECRAFT_CLIENT.level.getHeight(Types.WORLD_SURFACE, Mth.floor(groundX), Mth.floor(groundZ));
      if (groundY <= MINECRAFT_CLIENT.level.getMinY() + 2) {
         groundY = Mth.floor(cameraWorldPosition.y - ВПК);
      }

      double topY = Math.max(groundY + random.nextDouble(ВПI, ВПТ), cameraWorldPosition.y + ВПО);
      double topX = groundX + random.nextDouble(ВПФ, ВПЙ);
      double topZ = groundZ + random.nextDouble(ВПк, ВПг);
      this.lightningTopWorldPosition = new Vec3(topX, topY, topZ);
      this.lightningGroundWorldPosition = new Vec3(groundX, groundY + ВП0, groundZ);
      byte pointCount = 25;
      double[] offsetsX = new double[pointCount];
      double[] offsetsZ = new double[pointCount];
      Random offsetRandom = new Random(random.nextLong());
      subdivideLightningOffsets(offsetsX, 0, pointCount - 1, ВПх, offsetRandom);
      subdivideLightningOffsets(offsetsZ, 0, pointCount - 1, ВПУ, offsetRandom);
      ArrayList boltPoints = new ArrayList(pointCount);

      for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
         double progress = (double)pointIndex / (pointCount - 1);
         double pointX = Mth.lerp(progress, topX, groundX) + offsetsX[pointIndex];
         double pointY = Mth.lerp(progress, topY, groundY + ВПЗ);
         double pointZ = Mth.lerp(progress, topZ, groundZ) + offsetsZ[pointIndex];
         boltPoints.add(new Vec3(pointX, pointY, pointZ));
         if (pointIndex > 0) {
            this.lightningSegments.add(new Ambience.LightningSegment((Vec3)boltPoints.get(pointIndex - 1), (Vec3)boltPoints.get(pointIndex), 1.0F));
         }
      }

      for (int branchPointIndex = 3; branchPointIndex < pointCount - 3; branchPointIndex += random.nextInt(3, 6)) {
         if (!(random.nextFloat() > ВПЕ)) {
            Vec3 branchStart = (Vec3)boltPoints.get(branchPointIndex);
            double branchYawRadians = random.nextDouble(0.0, ВПП);
            int branchSegmentCount = random.nextInt(3, 7);

            for (int branchSegmentIndex = 0; branchSegmentIndex < branchSegmentCount; branchSegmentIndex++) {
               float branchStrength = ВП西 * (1.0F - (float)branchSegmentIndex / branchSegmentCount);
               double branchStepDistance = ВПд + branchSegmentIndex * ВПБ;
               Vec3 branchEnd = branchStart.add(
                  Math.sin(branchYawRadians) * branchStepDistance + random.nextDouble(ВПС, ВПз), -random.nextDouble(ВПц, ВПя), Math.cos(branchYawRadians) * branchStepDistance + random.nextDouble(В西с, В西В)
               );
               this.lightningSegments.add(new Ambience.LightningSegment(branchStart, branchEnd, Math.max(В西Я, branchStrength)));
               branchStart = branchEnd;
            }
         }
      }
   }

   private void renderLightningEffects(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix) {
      if (this.canRenderLightning() && sourceProjectionMatrix != null && sourceViewMatrix != null) {
         long nowNanos = System.nanoTime();
         if (this.nextLightningNanos == 0L) {
            this.scheduleNextLightning(nowNanos);
         }

         if (this.lightningStartNanos == 0L && nowNanos >= this.nextLightningNanos) {
            this.generateLightningBatch(MINECRAFT_CLIENT.gameRenderer.mainCamera().position(), nowNanos);
         }

         if (this.lightningStartNanos != 0L && !this.lightningSegments.isEmpty()) {
            float normalizedAge = (float)(nowNanos - this.lightningStartNanos) / ВПА;
            if (normalizedAge >= ВП4) {
               this.lightningSegments.clear();
               this.lightningStartNanos = 0L;
               this.lightningTopWorldPosition = null;
               this.lightningGroundWorldPosition = null;
               this.scheduleNextLightning(nowNanos);
            } else {
               float flashStrength = computeLightningEnvelope(normalizedAge);
               if (!(flashStrength <= ВПп)) {
                  RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
                  if (renderTarget != null && renderTarget.getColorTexture() != null && renderTarget.getDepthTexture() != null && !renderTarget.getDepthTexture().isClosed()) {
                     Vec3 cameraWorldPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
                     int primaryArgb = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | ВПД;
                     int glowArgb = ColorMath.blendArgb(primaryArgb, ColorMath.packRgb(255, 255, 255), ВПЪ);
                     int coreArgb = ColorMath.blendArgb(primaryArgb, ColorMath.packRgb(255, 255, 255), ВПт);
                     Vector4f primaryColor = ColorMath.toRgbaVector(primaryArgb);
                     Vector4f glowColor = ColorMath.toRgbaVector(glowArgb);
                     Vector4f coreColor = ColorMath.toRgbaVector(coreArgb);
                     this.renderLightningFlash(renderTarget, sourceProjectionMatrix, sourceViewMatrix, cameraWorldPosition, flashStrength, primaryColor);
                     TextureTarget maskTarget = this.ensureLightningMaskTarget(renderTarget.width, renderTarget.height);
                     CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                     float glowEnvelope = smoothstep01(flashStrength);
                     encoder.clearColorTexture(maskTarget.getColorTexture(), new Vector4f(0.0F));
                     maskTarget.copyDepthFrom(renderTarget);
                     this.renderLightningLayer(maskTarget, sourceProjectionMatrix, sourceViewMatrix, cameraWorldPosition, ВПа, new Vector4f(primaryColor.x, primaryColor.y, primaryColor.z, 1.0F), 0);
                     this.renderLightningGlow(maskTarget, renderTarget, primaryArgb, glowArgb, ВПЮ * glowEnvelope, ВПЧ * glowEnvelope);
                     this.renderLightningLayer(renderTarget, sourceProjectionMatrix, sourceViewMatrix, cameraWorldPosition, ВПЛ, new Vector4f(glowColor.x, glowColor.y, glowColor.z, flashStrength), 1);
                     this.renderLightningLayer(renderTarget, sourceProjectionMatrix, sourceViewMatrix, cameraWorldPosition, ВП诶, new Vector4f(coreColor.x, coreColor.y, coreColor.z, flashStrength), 2);
                  }
               }
            }
         }
      } else {
         this.lightningSegments.clear();
         this.lightningStartNanos = 0L;
         this.nextLightningNanos = 0L;
         this.lightningTopWorldPosition = null;
         this.lightningGroundWorldPosition = null;
      }
   }

   private void releaseLightningGlowResources() {
      if (this.lightningGlowPingTarget != null) {
         this.lightningGlowPingTarget.destroyBuffers();
      }

      if (this.lightningGlowPongTarget != null) {
         this.lightningGlowPongTarget.destroyBuffers();
      }

      this.lightningGlowPingTarget = null;
      this.lightningGlowPongTarget = null;
      if (this.lightningGlowHorizontalUniformBuffer != null && !this.lightningGlowHorizontalUniformBuffer.isClosed()) {
         this.lightningGlowHorizontalUniformBuffer.close();
      }

      if (this.lightningGlowVerticalUniformBuffer != null && !this.lightningGlowVerticalUniformBuffer.isClosed()) {
         this.lightningGlowVerticalUniformBuffer.close();
      }

      if (this.lightningGlowCompositeUniformBuffer != null && !this.lightningGlowCompositeUniformBuffer.isClosed()) {
         this.lightningGlowCompositeUniformBuffer.close();
      }

      this.lightningGlowHorizontalUniformBuffer = null;
      this.lightningGlowVerticalUniformBuffer = null;
      this.lightningGlowCompositeUniformBuffer = null;
   }

   private void renderJuiceSky(RenderTarget renderTarget, CommandEncoder encoder, int width, int height, float verticalFovDegrees) {
      float elapsedSeconds = (float)(System.currentTimeMillis() - this.shaderSkyStartMillis) / ВЕ必;
      Quaternionf cameraRotation = MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation();
      int primaryArgb = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | ВЕщ;
      int secondaryArgb = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) | ВЕГ;
      Vector4f primaryColor = ColorMath.toRgbaVector(primaryArgb);
      Vector4f secondaryColor = ColorMath.toRgbaVector(secondaryArgb);
      Vector4f accentColor = ColorMath.toRgbaVector(ColorMath.blendArgb(primaryArgb, -8054, ВЕл));
      Vector4f fogColor = ColorMath.toRgbaVector(this.resolvePrimaryThemeColor(this.fogColorSetting.getValue()) | ВЕэ);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 160)
            .putVec4(width, height, elapsedSeconds, this.skySpeedSetting.getValue())
            .putVec4(this.shaderIntensitySetting.getValue(), this.skyOpacitySetting.getValue(), verticalFovDegrees, 0.0F)
            .putVec4(cameraRotation.x, cameraRotation.y, cameraRotation.z, cameraRotation.w)
            .putVec4(primaryColor.x, primaryColor.y, primaryColor.z, primaryColor.w)
            .putVec4(secondaryColor.x, secondaryColor.y, secondaryColor.z, secondaryColor.w)
            .putVec4(primaryColor.x, primaryColor.y, primaryColor.z, primaryColor.w)
            .putVec4(secondaryColor.x, secondaryColor.y, secondaryColor.z, secondaryColor.w)
            .putVec4(accentColor.x, accentColor.y, accentColor.z, accentColor.w)
            .putVec4(fogColor.x, fogColor.y, fogColor.z, fogColor.w)
            .putVec4(this.fogStartSetting.getValue(), this.fogEndSetting.getValue(), 0.0F, 0.0F)
            .get();
         encoder.writeToBuffer(this.juiceSkyUniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_JUICE_SKY_PASS_DEBUG_LABEL, this.shaderSkyTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(this.juiceSkyPipeline);
            renderPass.bindTexture(RENDER_JUICE_SKY_DEPTH_SAMPLER_NAME, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.setUniform(RENDER_JUICE_SKY_SKY_SHADER_DATA_UNIFORM_NAME, this.juiceSkyUniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private boolean canRenderLightning() {
      return this.isEnabled()
         && this.lightningSetting.getValue()
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.gameRenderer != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera() != null
         && MINECRAFT_CLIENT.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
   }

   private void writeLightningGlowUniforms(CommandEncoder encoder, int width, int height, int primaryArgb, int secondaryArgb, float primaryStrength, float secondaryStrength) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer horizontalBlurData = Std140Builder.onStack(stack, 32).putVec2(1.0F / width, 1.0F / height).putVec2(1.0F, 0.0F).putVec2(ВПН, ВПн).align(16).get();
         encoder.writeToBuffer(this.lightningGlowHorizontalUniformBuffer.slice(), horizontalBlurData);
         ByteBuffer verticalBlurData = Std140Builder.onStack(stack, 32).putVec2(1.0F / width, 1.0F / height).putVec2(0.0F, 1.0F).putVec2(ВПЦ, ВПч).align(16).get();
         encoder.writeToBuffer(this.lightningGlowVerticalUniformBuffer.slice(), verticalBlurData);
         Vector4f primaryColor = ColorMath.toRgbaVector(primaryArgb | ВПЭ);
         Vector4f secondaryColor = ColorMath.toRgbaVector(secondaryArgb | ВП必);
         ByteBuffer compositeData = Std140Builder.onStack(stack, 48)
            .putVec4(primaryStrength, secondaryStrength, 0.0F, 0.0F)
            .putVec4(primaryColor.x, primaryColor.y, primaryColor.z, 1.0F)
            .putVec4(secondaryColor.x, secondaryColor.y, secondaryColor.z, 1.0F)
            .get();
         encoder.writeToBuffer(this.lightningGlowCompositeUniformBuffer.slice(), compositeData);
      }
   }

   private void ensureShaderSkyTarget(int width, int height) {
      if (this.shaderSkyTarget == null) {
         this.shaderSkyTarget = new TextureTarget(SHADER_SKY_TARGET_DEBUG_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.shaderSkyTarget.width != width || this.shaderSkyTarget.height != height) {
         this.shaderSkyTarget.resize(width, height);
      }
   }

   private void renderLightningGlowBlur(CommandEncoder encoder, RenderTarget sourceTarget, RenderTarget destinationTarget, GpuBuffer uniformBuffer) {
      try (RenderPass renderPass = encoder.createRenderPass(() -> AMBIENCE_LIGHTNING_DUAL_GLOW_BLUR_PASS_DEBUG_LABEL, destinationTarget.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(this.lightningGlowBlurPipeline);
         renderPass.bindTexture(RENDER_LIGHTNING_GLOW_BLUR_IN_SAMPLER_NAME, sourceTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.setUniform(RENDER_LIGHTNING_GLOW_BLUR_LIGHTNING_GLOW_BLUR_UNIFORM_NAME, uniformBuffer);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private boolean isNightixAuroraSelected() {
      return this.skyShaderSetting.isSelected(IS_NIGHTIX_AURORA_SELECTED_AURORA2_OPTION_LABEL);
   }

   public void renderWorldPostEffects(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, DeltaTracker deltaTracker) {
      this.renderBlizzardOverlay(sourceProjectionMatrix, sourceViewMatrix);
      this.renderWorldColor();
      this.renderVolumeFog(sourceProjectionMatrix, sourceViewMatrix, deltaTracker);
   }

   private boolean isFogAtmosphereEnabled() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         FogAtmosphere fogAtmosphere = RoseClient.getInstance().getServices().getModules().getFogAtmosphere();
         return fogAtmosphere != null && fogAtmosphere.isEnabled();
      } else {
         return false;
      }
   }

   private int resolveThemeColor(int fallbackArgb, ThemeColorSlot themeSlot) {
      return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(themeSlot) : fallbackArgb;
   }

   private GpuBuffer ensureUnitQuadVertexBuffer() {
      if (this.unitQuadVertexBuffer == null || this.unitQuadVertexBuffer.isClosed()) {
         Matrix4f identityMatrix = new Matrix4f();
         BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         vertexBuilder.addVertex(identityMatrix, ВдЮ, ВдЧ, 0.0F);
         vertexBuilder.addVertex(identityMatrix, 1.0F, ВдЛ, 0.0F);
         vertexBuilder.addVertex(identityMatrix, 1.0F, 1.0F, 0.0F);
         vertexBuilder.addVertex(identityMatrix, Вд诶, 1.0F, 0.0F);

         try (MeshData mesh = vertexBuilder.buildOrThrow()) {
            this.unitQuadIndexCount = mesh.drawState().indexCount();
            this.unitQuadVertexBuffer = RenderSystem.getDevice().createBuffer(() -> UNIT_QUAD_VERTEX_BUFFER_DEBUG_LABEL, 32, mesh.vertexBuffer());
         }
      }

      return this.unitQuadVertexBuffer;
   }

   private void updateThunderSound() {
      if (!this.canRenderLightning() || !this.thunderSoundsSetting.getValue() || this.thunderVolumeSetting.getValue() <= 0.0F) {
         this.stopThunderSound();
      } else if (!MINECRAFT_CLIENT.isPaused() && this.thunderDelayTicks >= 0) {
         if (this.thunderDelayTicks-- <= 0) {
            this.thunderSound = new Ambience.ThunderSound(this.pendingThunderWorldPosition);
            this.pendingThunderWorldPosition = null;
            MINECRAFT_CLIENT.getSoundManager().play(this.thunderSound);
         }
      }
   }

   private void generateLightningBatch(Vec3 cameraWorldPosition, long nowNanos) {
      this.lightningSegments.clear();
      int boltCount = Math.max(1, Math.round(this.lightningCountSetting.getValue()));

      for (int boltIndex = 0; boltIndex < boltCount; boltIndex++) {
         this.generateLightningBolt(cameraWorldPosition);
      }

      this.lightningStartNanos = nowNanos;
      this.scheduleThunderSound(cameraWorldPosition);
   }

   private void releaseLightningLayerBuffers() {
      for (int layerIndex = 0; layerIndex < this.lightningLayerVertexBuffers.length; layerIndex++) {
         GpuBuffer vertexBuffer = this.lightningLayerVertexBuffers[layerIndex];
         if (vertexBuffer != null && !vertexBuffer.isClosed()) {
            vertexBuffer.close();
         }

         this.lightningLayerVertexBuffers[layerIndex] = null;
         this.lightningLayerVertexCapacities[layerIndex] = 0L;
         GpuBuffer uniformBuffer = this.lightningLayerUniformBuffers[layerIndex];
         if (uniformBuffer != null && !uniformBuffer.isClosed()) {
            uniformBuffer.close();
         }

         this.lightningLayerUniformBuffers[layerIndex] = null;
      }

      if (this.lightningFlashUniformBuffer != null && !this.lightningFlashUniformBuffer.isClosed()) {
         this.lightningFlashUniformBuffer.close();
      }

      this.lightningFlashUniformBuffer = null;
   }

   public void renderWeatherEffects(Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, GpuBufferSlice fogUniforms) {
      this.renderWeather(sourceProjectionMatrix, sourceViewMatrix, fogUniforms);
   }

   private void releaseShaderSkyResources() {
      this.auroraRenderer.close();
      this.shaderSkyStartMillis = ВЕ9;
      if (this.shaderSkyTarget != null) {
         this.shaderSkyTarget.destroyBuffers();
         this.shaderSkyTarget = null;
      }

      if (this.shaderMistUniformBuffer != null) {
         this.shaderMistUniformBuffer.close();
         this.shaderMistUniformBuffer = null;
      }

      if (this.shaderFogUniformBuffer != null) {
         this.shaderFogUniformBuffer.close();
         this.shaderFogUniformBuffer = null;
      }

      if (this.juiceSkyUniformBuffer != null) {
         this.juiceSkyUniformBuffer.close();
         this.juiceSkyUniformBuffer = null;
      }
   }

   private record LightningSegment(Vec3 start, Vec3 end, float strength) {
   }

   private final class ThunderSound extends AbstractTickableSoundInstance {
      private final @com.google.gson.annotations.SerializedName("Ъ")  ClientLevel initialLevel = MinecraftAccess.MINECRAFT_CLIENT.level;
      private static final float BASE_THUNDER_PITCH = 0.85F;
      private static final float THUNDER_PITCH_VARIATION = 0.2F;

      private ThunderSound(Vec3 worldPosition) {
         super(SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.MASTER, RandomSource.create());
         this.x = worldPosition.x;
         this.y = worldPosition.y;
         this.z = worldPosition.z;
         this.volume = Ambience.this.thunderVolumeSetting.getValue();
         this.pitch = BASE_THUNDER_PITCH + this.random.nextFloat() * THUNDER_PITCH_VARIATION;
         this.attenuation = Attenuation.NONE;
      }

      @Override
      public void tick() {
         if (MinecraftAccess.MINECRAFT_CLIENT.level == this.initialLevel && Ambience.this.canRenderLightning() && Ambience.this.thunderSoundsSetting.getValue()) {
            this.volume = Ambience.this.thunderVolumeSetting.getValue();
         } else {
            this.stop();
         }
      }

      private void finish() {
         this.stop();
      }
   }
}
