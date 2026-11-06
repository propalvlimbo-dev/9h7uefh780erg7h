package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.settings.SingleChoiceSetting;

public class KillEffects extends ClientModule {
   private static KillEffects instance;
   private static final Identifier GLOW_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.GLOW_TEXTURE_NAMESPACE, KillEffects.GLOW_TEXTURE_PATH);
   private static final Identifier SOUL_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.SOUL_TEXTURE_NAMESPACE, KillEffects.SOUL_TEXTURE_PATH);
   private static final Identifier STAR_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.STAR_TEXTURE_NAMESPACE, KillEffects.STAR_TEXTURE_PATH);
   private static final Identifier LIGHTNING_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.LIGHTNING_TEXTURE_NAMESPACE, KillEffects.LIGHTNING_TEXTURE_PATH);
   private static final Identifier WAVE_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.WAVE_TEXTURE_NAMESPACE, KillEffects.WAVE_TEXTURE_PATH);
   private static final Identifier VIGNETTE_TEXTURE = Identifier.fromNamespaceAndPath(KillEffects.VIGNETTE_TEXTURE_NAMESPACE, KillEffects.VIGNETTE_TEXTURE_PATH);
   private static final SoundEvent KILL_SOUND = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(KillEffects.KILL_SOUND_NAMESPACE, KillEffects.KILL_SOUND_PATH));
   private static final SoundEvent TOTEM_SOUND = SoundEvent.createVariableRangeEvent(
      Identifier.fromNamespaceAndPath(KillEffects.TOTEM_SOUND_NAMESPACE, KillEffects.TOTEM_SOUND_PATH)
   );
   private static final long ВI = 2500L;
   private final @com.google.gson.annotations.SerializedName("Й")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("сФ")  BooleanSetting playersOnlySetting;
   private final @com.google.gson.annotations.SerializedName("сЙ")  BooleanSetting totemEffectSetting;
   private final @com.google.gson.annotations.SerializedName("ск")  BooleanSetting themeColorSetting;
   private final @com.google.gson.annotations.SerializedName("Э")  ColorSetting customColorSetting;
   private final @com.google.gson.annotations.SerializedName("Яс")  FloatSetting sizeSetting;
   private final @com.google.gson.annotations.SerializedName("ЯВ")  FloatSetting durationSetting;
   private final @com.google.gson.annotations.SerializedName("ЯЯ")  FloatSetting densitySetting;
   private final @com.google.gson.annotations.SerializedName("сИ")  List<KillEffects.TrackedVictim> trackedVictims;
   private final @com.google.gson.annotations.SerializedName("сь")  List<KillEffects.OrbitParticle> orbitParticles;
   private final @com.google.gson.annotations.SerializedName("сж")  List<KillEffects.EffectParticle> effectParticles;
   private final @com.google.gson.annotations.SerializedName("с弟")  List<KillEffects.TimedGlow> waveEffects;
   private final @com.google.gson.annotations.SerializedName("сб")  List<KillEffects.TimedLightningColumn> lightningEffects;
   private final @com.google.gson.annotations.SerializedName("l")  Set<Integer> processedEffectEntityIds;
   @SerializedName("8")
   private final Set<Integer> processedFragKillEntityIds;
   private @com.google.gson.annotations.SerializedName("Аг")  float killFlashProgress;
   private @com.google.gson.annotations.SerializedName("А0")  float killFlashRiseState;
   private @com.google.gson.annotations.SerializedName("Ах")  float totemFlashProgress;
   private @com.google.gson.annotations.SerializedName("АУ")  float totemFlashRiseState;
   private @com.google.gson.annotations.SerializedName("Яl")  boolean totemParticleTintActive;
   private @com.google.gson.annotations.SerializedName("Я8")  boolean killPeakParticlesSpawned;
   private @com.google.gson.annotations.SerializedName("Ч")  LivingEntity lastEffectVictim;
   private @com.google.gson.annotations.SerializedName("А诶")  int timerRecoveryStage;
   private @com.google.gson.annotations.SerializedName("ВТ")  long timerRecoveryDeadlineMillis;
   private final @com.google.gson.annotations.SerializedName("су")  RenderPipeline worldPipeline;
   private final @com.google.gson.annotations.SerializedName("с2")  RenderPipeline overlayPipeline;
   private final @com.google.gson.annotations.SerializedName("ВР")  EventListener<AttackEntityEvent> attackListener;
   private final @com.google.gson.annotations.SerializedName("В3")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private final @com.google.gson.annotations.SerializedName("Вр")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("В6")  EventListener<HudRenderEvent> hudRenderListener;
   private final @com.google.gson.annotations.SerializedName("В_")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String MODULE_NAME = "KillEffects";
   private static final String MODULE_DESCRIPTION = "Эффекты при убийстве и тотеме";
   private static final String MODE_LABEL = "Режим";
   private static final String FRAG_OPTION = "Фраг";
   private static final String SOUL_OPTION = "Души";
   private static final String WAVE_OPTION = "Волна";
   private static final String LIGHTNING_OPTION = "Молния";
   private static final String PLAYERS_ONLY_LABEL = "Без мобов";
   private static final String TOTEM_EFFECT_LABEL = "Эффект тотема";
   private static final String THEME_COLOR_LABEL = "Авто цвет под тему";
   private static final String CUSTOM_COLOR_LABEL = "Цвет";
   private static final int DEFAULT_CUSTOM_COLOR = -8585238;
   private static final String SIZE_LABEL = "Размер";
   private static final float MIN_SIZE = 0.4F;
   private static final float SIZE_STEP = 0.05F;
   private static final String DURATION_LABEL = "Длительность";
   private static final float DEFAULT_DURATION = 1.8F;
   private static final float MIN_DURATION = 0.6F;
   private static final float MAX_DURATION = 4.0F;
   private static final float DURATION_STEP = 0.1F;
   private static final String DENSITY_LABEL = "Плотность";
   private static final float MIN_DENSITY = 0.4F;
   private static final float DENSITY_STEP = 0.1F;
   private static final String WORLD_PIPELINE_LOCATION = "rose/kill_effects";
   private static final String WORLD_VERTEX_SHADER = "dead_chams";
   private static final String WORLD_FRAGMENT_SHADER = "dead_chams";
   private static final String WORLD_SAMPLER_NAME = "Sampler0";
   private static final String OVERLAY_PIPELINE_LOCATION = "rose/kill_effects_overlay";
   private static final String OVERLAY_VERTEX_SHADER = "kill_effects_overlay_ndc";
   private static final String OVERLAY_FRAGMENT_SHADER = "dead_chams";
   private static final String OVERLAY_SAMPLER_NAME = "Sampler0";
   private static final String FRAG_FOV_MODE_KEY = "Фраг";
   private static final float TOTEM_FOV_PROGRESS_DIVISOR = 6.0F;
   private static final float FLASH_FOV_SCALE = 0.45F;
   private static final double MISSING_VICTIM_DEATH_DISTANCE_SQUARED = 100.0;
   private static final long DEAD_VICTIM_VALIDITY_MILLIS = 300L;
   private static final float TOTEM_AIM_PROGRESS_THRESHOLD = 0.8F;
   private static final String FRAG_PROGRESS_MODE_KEY = "Фраг";
   private static final float INACTIVE_KILL_FLASH_DECAY_LERP = 0.2F;
   private static final float INACTIVE_TOTEM_FLASH_DECAY_LERP = 0.2F;
   private static final float KILL_FLASH_RISE_STEP = 0.06F;
   private static final float KILL_FLASH_PEAK_THRESHOLD = 0.998F;
   private static final float KILL_PEAK_LONG_ORBIT_LIFETIME_MILLIS = 2400.0F;
   private static final float KILL_PEAK_LONG_ORBIT_SPREAD_SCALE = 2.4F;
   private static final float KILL_PEAK_SHORT_ORBIT_LIFETIME_MILLIS = 320.0F;
   private static final float KILL_PEAK_SHORT_ORBIT_SPREAD_SCALE = 3.2F;
   private static final float KILL_FLASH_FALL_STEP = 0.035F;
   private static final float KILL_FLASH_RESET_THRESHOLD = 0.01F;
   private static final float TOTEM_FLASH_RISE_STEP = 0.06F;
   private static final float TOTEM_FLASH_PEAK_THRESHOLD = 0.97F;
   private static final float TOTEM_FLASH_FALL_STEP = 0.04F;
   private static final float TOTEM_FLASH_RESET_THRESHOLD = 0.01F;
   private static final float KILL_RECOVERY_TIMER_SPEED = 0.35F;
   private static final long KILL_RECOVERY_TIMER_DELAY_MILLIS = 180L;
   private static final String FRAG_KILL_MODE_KEY = "Фраг";
   private static final float INITIAL_KILL_FLASH_PROGRESS = 0.5F;
   private static final float KILL_SOUND_VOLUME = 0.55F;
   private static final float KILL_AIM_LERP_PROGRESS = 0.99F;
   private static final float KILL_INITIAL_TIMER_SPEED = 0.12F;
   private static final long KILL_INITIAL_TIMER_DELAY_MILLIS = 80L;
   private static final float KILL_INITIAL_SHORT_ORBIT_LIFETIME_MILLIS = 180.0F;
   private static final float KILL_INITIAL_SHORT_ORBIT_SPREAD_SCALE = 1.6F;
   private static final float KILL_INITIAL_LONG_ORBIT_LIFETIME_MILLIS = 520.0F;
   private static final float KILL_INITIAL_LONG_ORBIT_SPREAD_SCALE = 2.8F;
   private static final String FRAG_TOTEM_MODE_KEY = "Фраг";
   private static final float INITIAL_TOTEM_FLASH_PROGRESS = 0.1F;
   private static final float TOTEM_SOUND_VOLUME = 0.35F;
   private static final float TOTEM_TIMER_SPEED = 0.75F;
   private static final long TOTEM_TIMER_DELAY_MILLIS = 220L;
   private static final float TOTEM_SHORT_ORBIT_LIFETIME_MILLIS = 900.0F;
   private static final float TOTEM_LONG_ORBIT_LIFETIME_MILLIS = 2800.0F;
   private static final float TOTEM_LONG_ORBIT_SPREAD_SCALE = 3.5F;
   private static final float SOUND_BASE_PITCH = 0.95F;
   private static final float SOUND_RANDOM_PITCH_RANGE = 0.1F;
   private static final double AIM_TARGET_HEIGHT_SCALE = 0.5;
   private static final double AIM_YAW_RADIANS_TO_DEGREES = 180.0 / Math.PI;
   private static final float AIM_YAW_OFFSET_DEGREES = 90.0F;
   private static final double AIM_PITCH_RADIANS_TO_DEGREES = 180.0 / Math.PI;
   private static final float AIM_YAW_THRESHOLD_DEGREES = 7.0F;
   private static final float AIM_PITCH_THRESHOLD_DEGREES = 20.0F;
   private static final float DEFAULT_ORBIT_SPREAD_SCALE = 1.5F;
   private static final float ORBIT_BASE_RADIUS = 0.35F;
   private static final float ORBIT_TOTEM_RADIUS_SCALE = 0.5F;
   private static final float ORBIT_VERTICAL_RANGE_DIVISOR = 4.0F;
   private static final double ORBIT_ENTITY_EYE_HEIGHT_SCALE = 0.45;
   private static final float ORBIT_RADIUS_GROWTH_BASE_SCALE = 0.4F;
   private static final float ORBIT_RADIUS_GROWTH_RANDOM_SCALE = 1.4F;
   private static final float ORBIT_START_ANGLE_RANGE_DEGREES = 360.0F;
   private static final float ORBIT_START_RADIUS_SCALE = 0.3F;
   private static final float ORBIT_ANGLE_TRAVEL_RANGE_DEGREES = 40.0F;
   private static final float MIN_EFFECT_ENTITY_HEIGHT = 0.8F;
   private static final float MIN_EFFECT_ENTITY_WIDTH = 0.5F;
   private static final String SOUL_TRIGGER_MODE_KEY = "Души";
   private static final String WAVE_TRIGGER_MODE_KEY = "Волна";
   private static final String LIGHTNING_TRIGGER_MODE_KEY = "Молния";
   private static final float SOUL_PARTICLE_COUNT_SCALE = 28.0F;
   private static final double SOUL_POSITION_X_CENTER = 0.5;
   private static final double SOUL_POSITION_X_WIDTH_SCALE = 1.4;
   private static final double SOUL_POSITION_Z_CENTER = 0.5;
   private static final double SOUL_POSITION_Z_WIDTH_SCALE = 1.4;
   private static final double SOUL_VELOCITY_X_CENTER = 0.5;
   private static final double SOUL_VELOCITY_X_SCALE = 0.02;
   private static final double SOUL_BASE_VELOCITY_Y = 0.04;
   private static final double SOUL_RANDOM_VELOCITY_Y_RANGE = 0.06;
   private static final double SOUL_VELOCITY_Z_CENTER = 0.5;
   private static final double SOUL_VELOCITY_Z_SCALE = 0.02;
   private static final float SOUL_SIZE_BASE_SCALE = 0.35F;
   private static final float SOUL_SIZE_RANDOM_SCALE = 0.45F;
   private static final float SOUL_DURATION_BASE_SCALE = 0.85F;
   private static final float SOUL_DURATION_RANDOM_SCALE = 0.4F;
   private static final float SOUL_INITIAL_ROTATION_RANGE = (float) (Math.PI * 2);
   private static final double WAVE_PRIMARY_SURFACE_OFFSET = 0.05;
   private static final float WAVE_PRIMARY_RADIUS_SCALE = 2.4F;
   private static final float WAVE_PRIMARY_DURATION_MILLIS_SCALE = 700.0F;
   private static final double WAVE_SECONDARY_SURFACE_OFFSET = 0.05;
   private static final float WAVE_SECONDARY_RADIUS_SCALE = 1.6F;
   private static final float WAVE_SECONDARY_DURATION_MILLIS_SCALE = 550.0F;
   private static final long WAVE_SECONDARY_DELAY_MILLIS = 80L;
   private static final float WAVE_BURST_PARTICLE_COUNT_SCALE = 40.0F;
   private static final double WAVE_BURST_FULL_TURN_RADIANS = (float) (Math.PI * 2);
   private static final double WAVE_BURST_BASE_HORIZONTAL_SPEED = 0.08;
   private static final double WAVE_BURST_RANDOM_HORIZONTAL_SPEED_SCALE = 0.14;
   private static final double WAVE_BURST_POSITION_X_WIDTH_SCALE = 0.2;
   private static final double WAVE_BURST_POSITION_Y_BASE_HEIGHT_SCALE = 0.2;
   private static final double WAVE_BURST_POSITION_Y_RANDOM_HEIGHT_SCALE = 0.5;
   private static final double WAVE_BURST_POSITION_Z_WIDTH_SCALE = 0.2;
   private static final double WAVE_BURST_BASE_VELOCITY_Y = 0.02;
   private static final double WAVE_BURST_RANDOM_VELOCITY_Y_RANGE = 0.05;
   private static final float WAVE_BURST_SIZE_BASE_SCALE = 0.28F;
   private static final float WAVE_BURST_SIZE_RANDOM_SCALE = 0.35F;
   private static final float WAVE_BURST_DURATION_SCALE = 0.75F;
   private static final float LIGHTNING_COLUMN_COUNT_SCALE = 3.0F;
   private static final double LIGHTNING_COLUMN_POSITION_X_CENTER = 0.5;
   private static final double LIGHTNING_COLUMN_POSITION_Z_CENTER = 0.5;
   private static final double LIGHTNING_COLUMN_BASE_Y_HEIGHT_SCALE = 0.4;
   private static final double LIGHTNING_COLUMN_RANDOM_Y_HEIGHT_SCALE = 0.4;
   private static final double LIGHTNING_COLUMN_BASE_HEIGHT = 4.5;
   private static final double LIGHTNING_COLUMN_RANDOM_HEIGHT_SCALE = 2.5;
   private static final float LIGHTNING_COLUMN_DURATION_MILLIS_SCALE = 450.0F;
   private static final float LIGHTNING_SPARK_PARTICLE_COUNT_SCALE = 36.0F;
   private static final double LIGHTNING_SPARK_FULL_TURN_RADIANS = (float) (Math.PI * 2);
   private static final double LIGHTNING_SPARK_BASE_HORIZONTAL_SPEED = 0.05;
   private static final double LIGHTNING_SPARK_RANDOM_HORIZONTAL_SPEED_RANGE = 0.12;
   private static final double LIGHTNING_SPARK_POSITION_X_CENTER = 0.5;
   private static final double LIGHTNING_SPARK_POSITION_Y_HEIGHT_SCALE = 0.5;
   private static final double LIGHTNING_SPARK_POSITION_Y_CENTER = 0.5;
   private static final double LIGHTNING_SPARK_POSITION_Y_RANDOM_RANGE = 0.4;
   private static final double LIGHTNING_SPARK_POSITION_Z_CENTER = 0.5;
   private static final double LIGHTNING_SPARK_BASE_VELOCITY_Y = 0.03;
   private static final double LIGHTNING_SPARK_RANDOM_VELOCITY_Y_RANGE = 0.08;
   private static final float LIGHTNING_SPARK_SIZE_BASE_SCALE = 0.22F;
   private static final float LIGHTNING_SPARK_SIZE_RANDOM_SCALE = 0.3F;
   private static final float LIGHTNING_SPARK_DURATION_SCALE = 0.65F;
   private static final float LIGHTNING_SPARK_INITIAL_ROTATION_RANGE_DEGREES = 360.0F;
   private static final String FRAG_HUD_MODE_KEY = "Фраг";
   private static final float HUD_KILL_FLASH_GATE_THRESHOLD = 0.005F;
   private static final float HUD_TOTEM_FLASH_GATE_THRESHOLD = 0.005F;
   private static final float HUD_KILL_VIGNETTE_DRAW_THRESHOLD = 0.005F;
   private static final float HUD_TOTEM_VIGNETTE_DRAW_THRESHOLD = 0.005F;
   private static final float TOTEM_VIGNETTE_PROGRESS_SCALE = 0.75F;
   private static final float VIGNETTE_VISIBILITY_BYTE_SCALE = 255.0F;
   private static final float VIGNETTE_GREEN_BYTE_SCALE = 255.0F;
   private static final float VIGNETTE_GREEN_PROGRESS_DIVISOR = 1.25F;
   private static final float VIGNETTE_BLUE_BYTE_SCALE = 255.0F;
   private static final float VIGNETTE_BLUE_PROGRESS_DIVISOR = 1.25F;
   private static final float VIGNETTE_MAX_ALPHA = 190.0F;
   private static final String OVERLAY_DRAW_LABEL = "killEffectsOverlay";
   private static final float ORBIT_ENVELOPE_ALPHA_SCALE = 3.0F;
   private static final float ORBIT_MIN_VISIBLE_ALPHA = 0.02F;
   private static final int ORBIT_COLOR_RGB_OR_MASK = 16777215;
   private static final int ORBIT_THEME_RGB_COMPONENT_MASK = 16777215;
   private static final float ORBIT_THEME_COLOR_MIX_PROGRESS = 0.35F;
   private static final float ORBIT_TOTEM_TINT_PROGRESS_SCALE = 18.0F;
   private static final float ORBIT_BASE_QUAD_SIZE = 0.03F;
   private static final float ORBIT_ENVELOPE_QUAD_SIZE_SCALE = 0.05F;
   private static final float ORBIT_QUAD_SIZE_RANDOM_BASE = 0.5F;
   private static final float ORBIT_QUAD_SIZE_RANDOM_SCALE = 0.5F;
   private static final float ORBIT_STAR_VARIANT_THRESHOLD = 0.62F;
   private static final float ORBIT_STAR_WIDTH_SCALE = 1.2F;
   private static final float ORBIT_STAR_HEIGHT_SCALE = 1.2F;
   private static final float ORBIT_STAR_ROTATION_RANGE_DEGREES = 360.0F;
   private static final float ORBIT_GLOW_ROTATION_RANGE_DEGREES = 360.0F;
   private static final String ORBIT_GLOW_DRAW_LABEL = "killEffectsFragGlow";
   private static final String ORBIT_STAR_DRAW_LABEL = "killEffectsFragStar";
   private static final float SOUL_RENDER_SIZE_SCALE = 1.4F;
   private static final float SPARK_RENDER_SIZE_SCALE = 0.85F;
   private static final float PARTICLE_MIN_VISIBLE_ALPHA = 0.001F;
   private static final String EFFECT_PARTICLES_DRAW_LABEL = "killEffectsParticles";
   private static final float WAVE_FADE_IN_PROGRESS_END = 0.15F;
   private static final float WAVE_FADE_IN_PROGRESS_DIVISOR = 0.15F;
   private static final float WAVE_ALPHA_SCALE = 0.85F;
   private static final String WAVES_DRAW_LABEL = "killEffectsRings";
   private static final float LIGHTNING_FADE_IN_PROGRESS_END = 0.2F;
   private static final float LIGHTNING_FADE_IN_PROGRESS_DIVISOR = 0.2F;
   private static final float LIGHTNING_FADE_OUT_PROGRESS_START = 0.2F;
   private static final float LIGHTNING_FADE_OUT_PROGRESS_DIVISOR = 0.8F;
   private static final float LIGHTNING_STRIP_HALF_WIDTH_SCALE = 0.12F;
   private static final float LIGHTNING_BASE_GLOW_WIDTH_SCALE = 0.45F;
   private static final float LIGHTNING_BASE_GLOW_HEIGHT_SCALE = 0.45F;
   private static final String LIGHTNING_DRAW_LABEL = "killEffectsBolts";
   private static final double EFFECT_RAY_MIN_DISTANCE_SQUARED = 1.0E-6;
   private static final long LIGHTNING_PATH_NOISE_STEP_MILLIS = 40L;
   private static final double LIGHTNING_PATH_JITTER_SCALE = 0.55;
   private static final double LIGHTNING_PATH_X_RANDOM_CENTER = 0.5;
   private static final double LIGHTNING_PATH_X_RANDOM_RANGE = 2.0;
   private static final double LIGHTNING_PATH_Z_RANDOM_CENTER = 0.5;
   private static final double LIGHTNING_PATH_Z_RANDOM_RANGE = 2.0;
   private static final double STRIP_MIN_LENGTH_SQUARED = 1.0E-8;
   private static final float STRIP_CAMERA_YAW_OFFSET_DEGREES = 90.0F;
   private static final float BILLBOARD_HALF_WIDTH_FACTOR = 0.5F;
   private static final float BILLBOARD_HALF_HEIGHT_FACTOR = 0.5F;
   private static final String TEXTURE_SAMPLER_NAME = "Sampler0";
   private static final float QUADRATIC_IN_OUT_MIDPOINT = 0.5F;
   private static final float QUADRATIC_IN_OUT_TAIL_SLOPE = -2.0F;
   private static final double QUADRATIC_IN_OUT_TAIL_POWER = 2.0;
   private static final String FRAG_DENSITY_VISIBILITY_KEY = "Фраг";
   private static final String FRAG_DURATION_VISIBILITY_KEY = "Фраг";
   private static final String FRAG_SIZE_VISIBILITY_KEY = "Фраг";
   private static final String FRAG_CUSTOM_COLOR_VISIBILITY_KEY = "Фраг";
   private static final String FRAG_THEME_COLOR_VISIBILITY_KEY = "Фраг";
   private static final String GLOW_TEXTURE_NAMESPACE = "rose";
   private static final String GLOW_TEXTURE_PATH = "textures/world/glow.png";
   private static final String SOUL_TEXTURE_NAMESPACE = "rose";
   private static final String SOUL_TEXTURE_PATH = "textures/world/dashbloom.png";
   private static final String STAR_TEXTURE_NAMESPACE = "rose";
   private static final String STAR_TEXTURE_PATH = "textures/world/star.png";
   private static final String LIGHTNING_TEXTURE_NAMESPACE = "rose";
   private static final String LIGHTNING_TEXTURE_PATH = "textures/world/lightning.png";
   private static final String WAVE_TEXTURE_NAMESPACE = "rose";
   private static final String WAVE_TEXTURE_PATH = "icons/world_render/circle2.png";
   private static final String VIGNETTE_TEXTURE_NAMESPACE = "rose";
   private static final String VIGNETTE_TEXTURE_PATH = "images/killeffects/vignette.png";
   private static final String KILL_SOUND_NAMESPACE = "rose";
   private static final String KILL_SOUND_PATH = "strikesf_2";
   private static final String TOTEM_SOUND_NAMESPACE = "rose";
   private static final String TOTEM_SOUND_PATH = "strikesf_5";

   private int appendBillboardQuad(BufferBuilder builder, PoseStack poses, Vec3 position, float width, float height, float rotation, float cameraYaw, float cameraPitch, int color) {
      Vector4f rgba = ColorMath.toRgbaVector(color);
      float halfWidth = width * BILLBOARD_HALF_WIDTH_FACTOR;
      float halfHeight = height * BILLBOARD_HALF_HEIGHT_FACTOR;
      poses.pushPose();
      poses.translate(position.x, position.y, position.z);
      poses.mulPose(Axis.YP.rotationDegrees(-cameraYaw));
      poses.mulPose(Axis.XP.rotationDegrees(cameraPitch));
      if (rotation != 0.0F) {
         poses.mulPose(Axis.ZP.rotationDegrees(rotation));
      }

      Matrix4f poseMatrix = poses.last().pose();
      this.appendVertex(builder, poseMatrix, -halfWidth, -halfHeight, 0.0F, 0.0F, 0.0F, rgba);
      this.appendVertex(builder, poseMatrix, -halfWidth, halfHeight, 0.0F, 0.0F, 1.0F, rgba);
      this.appendVertex(builder, poseMatrix, halfWidth, halfHeight, 0.0F, 1.0F, 1.0F, rgba);
      this.appendVertex(builder, poseMatrix, halfWidth, -halfHeight, 0.0F, 1.0F, 0.0F, rgba);
      poses.popPose();
      return 4;
   }

   private void updateTimerRecovery() {
      long nowMillis = System.currentTimeMillis();
      if (this.timerRecoveryStage == 0) {
         if (ClientTimerSpeed.дю() != 1.0F && this.killFlashProgress <= 0.0F && this.totemFlashProgress <= 0.0F) {
            ClientTimerSpeed.Оэ(1.0F);
         }
      } else {
         if (nowMillis >= this.timerRecoveryDeadlineMillis) {
            if (this.timerRecoveryStage == 1) {
               ClientTimerSpeed.Оэ(KILL_RECOVERY_TIMER_SPEED);
               this.timerRecoveryStage = 2;
               this.timerRecoveryDeadlineMillis = nowMillis + KILL_RECOVERY_TIMER_DELAY_MILLIS;
            } else {
               ClientTimerSpeed.Оэ(1.0F);
               this.timerRecoveryStage = 0;
            }
         }
      }
   }

   public static float getFovScale() {
      if (instance != null && instance.isEnabled() && instance.modeSetting.isSelected(FRAG_FOV_MODE_KEY)) {
         float effectProgress = Math.min(instance.getKillFlashProgress() + easeInOutQuadratic(instance.totemFlashProgress) / TOTEM_FOV_PROGRESS_DIVISOR, 1.0F);
         return 1.0F + effectProgress * FLASH_FOV_SCALE;
      } else {
         return 1.0F;
      }
   }

   private static float easeInOutQuadratic(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      return value < QUADRATIC_IN_OUT_MIDPOINT ? 2.0F * value * value : 1.0F - (float)Math.pow(QUADRATIC_IN_OUT_TAIL_SLOPE * value + 2.0F, QUADRATIC_IN_OUT_TAIL_POWER) / 2.0F;
   }

   private void playEffectSound(SoundEvent sound, float volume) {
      if (MINECRAFT_CLIENT.getSoundManager() != null) {
         MINECRAFT_CLIENT.getSoundManager()
            .play(
               new SimpleSoundInstance(
                  sound.location(),
                  SoundSource.MASTER,
                  volume,
                  SOUND_BASE_PITCH + ThreadLocalRandom.current().nextFloat() * SOUND_RANDOM_PITCH_RANGE,
                  RandomSource.create(),
                  false,
                  0,
                  Attenuation.NONE,
                  0.0,
                  0.0,
                  0.0,
                  true
               )
            );
      }
   }

   private void onPlayerTick(PlayerTickEvent event) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         try {
            Aura aura = RoseClient.getInstance().getServices().getModules().getAura();
            if (aura != null && aura.isEnabled() && aura.Ж_95bedd6e() != null && aura.Ж_95bedd6e().isAlive()) {
               this.trackVictim(aura.Ж_95bedd6e(), false);
            }
         } catch (Throwable ignoredAuraLookupFailure) {
         }

         Iterator victimIterator = this.trackedVictims.iterator();

         while (victimIterator.hasNext()) {
            KillEffects.TrackedVictim trackedVictim = (KillEffects.TrackedVictim)victimIterator.next();
            if (!trackedVictim.isValid()) {
               victimIterator.remove();
            } else {
               LivingEntity victim = trackedVictim.entity;
               boolean entityMissing = MINECRAFT_CLIENT.level.getEntity(victim.getId()) == null;
               if ((victim.getHealth() <= 0.0F || !victim.isAlive() || entityMissing && MINECRAFT_CLIENT.player.distanceToSqr(victim) > MISSING_VICTIM_DEATH_DISTANCE_SQUARED) && !trackedVictim.deathEffectTriggered) {
                  this.handleTrackedVictimDeath(victim);
                  trackedVictim.deathEffectTriggered = true;
                  trackedVictim.setValidityMillis(DEAD_VICTIM_VALIDITY_MILLIS);
               }
            }
         }

         this.trackedVictims.removeIf(victimToPrune -> !victimToPrune.isValid());
         this.updateFlashProgress();
         this.updateTimerRecovery();
         this.orbitParticles.removeIf(KillEffects.OrbitParticle::isExpired);
         this.processedFragKillEntityIds.removeIf(entityIdToPrune -> MINECRAFT_CLIENT.level.getEntity(entityIdToPrune) == null);
         Iterator particleIterator = this.effectParticles.iterator();

         while (particleIterator.hasNext()) {
            KillEffects.EffectParticle particle = (KillEffects.EffectParticle)particleIterator.next();
            particle.tick();
            if (particle.isExpired()) {
               particleIterator.remove();
            }
         }

         long nowMillis = System.currentTimeMillis();
         this.waveEffects.removeIf(waveToPrune -> nowMillis - waveToPrune.createdAtMillis > waveToPrune.durationMillis + waveToPrune.delayMillis);
         this.lightningEffects.removeIf(lightningToPrune -> nowMillis - lightningToPrune.createdAtMillis > lightningToPrune.durationMillis);
         if (this.lastEffectVictim != null && this.totemFlashRiseState == 1.0F && this.totemFlashProgress < TOTEM_AIM_PROGRESS_THRESHOLD) {
            this.lerpAimTowardEntity(this.lastEffectVictim, (1.0F - this.totemFlashProgress * this.totemFlashProgress) / 2.0F);
         }
      } else {
         this.clearEffects();
         ClientTimerSpeed.Оэ(1.0F);
      }
   }

   private void drawTexturedMesh(BufferBuilder builder, Identifier textureId, int vertexCount, String label, RenderPipeline pipeline, boolean overlay) {
      MeshData mesh = builder.build();
      if (mesh != null) {
         if (vertexCount == 0) {
            mesh.close();
         } else {
            GpuBuffer vertexBuffer = null;

            try {
               DrawState drawState = mesh.drawState();
               int indexCount = drawState.indexCount();
               AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
               GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
               IndexType indexType = sequentialIndices.type();
               vertexBuffer = RenderSystem.getDevice().createBuffer(() -> label, 32, mesh.vertexBuffer());
               RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture texture = MINECRAFT_CLIENT.getTextureManager().getTexture(textureId);

               try (RenderPass renderPass = encoder.createRenderPass(
                     () -> label, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(renderPass);
                  renderPass.setPipeline(pipeline);
                  renderPass.bindTexture(TEXTURE_SAMPLER_NAME, texture.getTextureView(), texture.getSampler());
                  renderPass.setVertexBuffer(0, vertexBuffer.slice());
                  renderPass.setIndexBuffer(indexBuffer, indexType);
                  renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
               }
            } finally {
               if (vertexBuffer != null) {
                  vertexBuffer.close();
               }

               mesh.close();
            }
         }
      }
   }

   private void onHudRender(HudRenderEvent event) {
      if (this.modeSetting.isSelected(FRAG_HUD_MODE_KEY) && event.getGraphics() != null) {
         float killFlash = this.getKillFlashProgress();
         float totemFlash = Mth.clamp(this.totemFlashProgress, 0.0F, 1.0F);
         if (!(killFlash < HUD_KILL_FLASH_GATE_THRESHOLD) || !(totemFlash < HUD_TOTEM_FLASH_GATE_THRESHOLD)) {
            int guiWidth = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth();
            int guiHeight = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
            if (killFlash > HUD_KILL_VIGNETTE_DRAW_THRESHOLD) {
               this.drawVignette(killFlash, guiWidth, guiHeight);
            } else if (totemFlash > HUD_TOTEM_VIGNETTE_DRAW_THRESHOLD) {
               this.drawVignette(totemFlash * TOTEM_VIGNETTE_PROGRESS_SCALE, guiWidth, guiHeight);
            }
         }
      }
   }

   private void onWorldEffects(WorldEffectsEvent event) {
      if (!this.orbitParticles.isEmpty()) {
         this.renderOrbitParticles(event);
      }

      if (!this.effectParticles.isEmpty() || !this.waveEffects.isEmpty() || !this.lightningEffects.isEmpty()) {
         this.renderWorldEffectGroups(event);
      }
   }

   private void drawNdcTexturedRectangle(float x, float y, float width, float height, Vector4f color, Identifier textureId) {
      int windowWidth = MINECRAFT_CLIENT.getWindow().getWidth();
      int windowHeight = MINECRAFT_CLIENT.getWindow().getHeight();
      float guiWidth = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth();
      float guiHeight = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
      float scaleX = windowWidth / guiWidth;
      float scaleY = windowHeight / guiHeight;
      float leftPixels = x * scaleX;
      float topPixels = y * scaleY;
      float rightPixels = (x + width) * scaleX;
      float bottomPixels = (y + height) * scaleY;
      float leftNdc = leftPixels / windowWidth * 2.0F - 1.0F;
      float rightNdc = rightPixels / windowWidth * 2.0F - 1.0F;
      float topNdc = 1.0F - topPixels / windowHeight * 2.0F;
      float bottomNdc = 1.0F - bottomPixels / windowHeight * 2.0F;
      BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      Matrix4f identityMatrix = new Matrix4f();
      builder.addVertex(identityMatrix, leftNdc, topNdc, 0.0F).setUv(0.0F, 0.0F).setColor(color.x, color.y, color.z, color.w);
      builder.addVertex(identityMatrix, leftNdc, bottomNdc, 0.0F).setUv(0.0F, 1.0F).setColor(color.x, color.y, color.z, color.w);
      builder.addVertex(identityMatrix, rightNdc, bottomNdc, 0.0F).setUv(1.0F, 1.0F).setColor(color.x, color.y, color.z, color.w);
      builder.addVertex(identityMatrix, rightNdc, topNdc, 0.0F).setUv(1.0F, 0.0F).setColor(color.x, color.y, color.z, color.w);
      this.drawTexturedMesh(builder, textureId, 4, OVERLAY_DRAW_LABEL, this.overlayPipeline, true);
   }

   private int appendCameraFacingStripQuad(BufferBuilder builder, Matrix4f matrix, Vec3 start, Vec3 end, float halfWidth, int color) {
      Vector4f rgba = ColorMath.toRgbaVector(color);
      if (end.subtract(start).lengthSqr() < STRIP_MIN_LENGTH_SQUARED) {
         return 0;
      }

      double cameraYawRadians = Math.toRadians(MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot() + STRIP_CAMERA_YAW_OFFSET_DEGREES);
      Vec3 sideOffset = new Vec3(Math.cos(cameraYawRadians), 0.0, Math.sin(cameraYawRadians)).normalize().scale(halfWidth);
      Vec3 startRight = start.add(sideOffset);
      Vec3 startLeft = start.subtract(sideOffset);
      Vec3 endRight = end.add(sideOffset);
      Vec3 endLeft = end.subtract(sideOffset);
      this.appendVertex(builder, matrix, (float)startLeft.x, (float)startLeft.y, (float)startLeft.z, 0.0F, 0.0F, rgba);
      this.appendVertex(builder, matrix, (float)startRight.x, (float)startRight.y, (float)startRight.z, 0.0F, 1.0F, rgba);
      this.appendVertex(builder, matrix, (float)endRight.x, (float)endRight.y, (float)endRight.z, 1.0F, 1.0F, rgba);
      this.appendVertex(builder, matrix, (float)endLeft.x, (float)endLeft.y, (float)endLeft.z, 1.0F, 0.0F, rgba);
      return 4;
   }

   private void renderOrbitParticles(WorldEffectsEvent event) {
      try (SharedGeometryBuffer isolatedMeshStorage = new SharedGeometryBuffer(786432)) {
         Camera camera = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 cameraPosition = camera.position();
         PoseStack poses = event.getPoses();
         int themeColor = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         short totemTintColor = -25786;
         float alphaScale = 1.0F;
         BufferBuilder glowBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         BufferBuilder starBuilder = isolatedMeshStorage.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int glowVertexCount = 0;
         int starVertexCount = 0;

         for (KillEffects.OrbitParticle particle : this.orbitParticles) {
            float alpha = Mth.clamp(Math.min(particle.getEnvelope() * ORBIT_ENVELOPE_ALPHA_SCALE, 1.0F) * particle.getRemainingAlpha(), 0.0F, 1.0F) * alphaScale;
            if (!(alpha < ORBIT_MIN_VISIBLE_ALPHA)) {
               int whiteColor = ColorMath.withAlpha(-1, alpha);
               int maskedThemeColor = ColorMath.withAlpha(ORBIT_COLOR_RGB_OR_MASK | themeColor & ORBIT_THEME_RGB_COMPONENT_MASK, alpha);
               int color = ColorMath.lerpArgb(ColorMath.withAlpha(0, alpha), whiteColor, particle.variantRandom);
               color = ColorMath.lerpArgb(color, maskedThemeColor, ORBIT_THEME_COLOR_MIX_PROGRESS);
               if (this.totemParticleTintActive) {
                  color = ColorMath.lerpArgb(color, ColorMath.withAlpha(totemTintColor, alpha), Math.min(this.totemFlashProgress * ORBIT_TOTEM_TINT_PROGRESS_SCALE, 1.0F));
               }

               Vec3 position = particle.getPosition();
               if (this.hasClearEffectRay(cameraPosition, position)) {
                  Vec3 cameraRelativePosition = position.subtract(cameraPosition);
                  float quadSize = ORBIT_BASE_QUAD_SIZE + ORBIT_ENVELOPE_QUAD_SIZE_SCALE * particle.getEnvelope() * (ORBIT_QUAD_SIZE_RANDOM_BASE + particle.variantRandom * ORBIT_QUAD_SIZE_RANDOM_SCALE);
                  if (particle.variantRandom > ORBIT_STAR_VARIANT_THRESHOLD) {
                     starVertexCount += this.appendBillboardQuad(starBuilder, poses, cameraRelativePosition, quadSize * ORBIT_STAR_WIDTH_SCALE, quadSize * ORBIT_STAR_HEIGHT_SCALE, particle.variantRandom * ORBIT_STAR_ROTATION_RANGE_DEGREES, camera.yRot(), camera.xRot(), color);
                  } else {
                     glowVertexCount += this.appendBillboardQuad(glowBuilder, poses, cameraRelativePosition, quadSize, quadSize, particle.variantRandom * ORBIT_GLOW_ROTATION_RANGE_DEGREES, camera.yRot(), camera.xRot(), color);
                  }
               }
            }
         }

         this.drawTexturedMesh(glowBuilder, GLOW_TEXTURE, glowVertexCount, ORBIT_GLOW_DRAW_LABEL, this.worldPipeline, false);
         this.drawTexturedMesh(starBuilder, STAR_TEXTURE, starVertexCount, ORBIT_STAR_DRAW_LABEL, this.worldPipeline, false);
      }
   }

   private List<Vec3> buildLightningPath(KillEffects.TimedLightningColumn effect, long nowMillis) {
      ArrayList points = new ArrayList();
      Random random = new Random(effect.noiseSeed ^ nowMillis / LIGHTNING_PATH_NOISE_STEP_MILLIS);
      byte segmentCount = 8;

      for (int segment = 0; segment <= segmentCount; segment++) {
         float progress = (float)segment / segmentCount;
         double y = Mth.lerp(progress, effect.tipY, effect.baseY);
         double jitterScale = (1.0 - progress) * LIGHTNING_PATH_JITTER_SCALE;
         double x = segment == segmentCount ? effect.x : effect.x + (random.nextDouble() - LIGHTNING_PATH_X_RANDOM_CENTER) * jitterScale * LIGHTNING_PATH_X_RANDOM_RANGE;
         double z = segment == segmentCount ? effect.z : effect.z + (random.nextDouble() - LIGHTNING_PATH_Z_RANDOM_CENTER) * jitterScale * LIGHTNING_PATH_Z_RANDOM_RANGE;
         points.add(new Vec3(x, y, z));
      }

      return points;
   }

   private void renderWorldEffectGroups(WorldEffectsEvent event) {
      float tickDelta = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      float cameraYaw = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
      float cameraPitch = MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot();
      PoseStack poses = event.getPoses();
      if (!this.waveEffects.isEmpty()) {
         this.renderWaveEffects(poses, cameraPosition);
      }

      if (!this.lightningEffects.isEmpty()) {
         this.renderLightningEffects(poses, cameraPosition, cameraYaw, cameraPitch);
      }

      if (!this.effectParticles.isEmpty()) {
         this.renderParticlesOfKind(poses, cameraPosition, cameraYaw, cameraPitch, tickDelta, KillEffects.ParticleKind.SOUL, SOUL_TEXTURE, SOUL_RENDER_SIZE_SCALE);
         this.renderParticlesOfKind(poses, cameraPosition, cameraYaw, cameraPitch, tickDelta, KillEffects.ParticleKind.BURST, STAR_TEXTURE, 1.0F);
         this.renderParticlesOfKind(poses, cameraPosition, cameraYaw, cameraPitch, tickDelta, KillEffects.ParticleKind.SPARK, GLOW_TEXTURE, SPARK_RENDER_SIZE_SCALE);
      }
   }

   private boolean isTrackedVictim(LivingEntity entity) {
      if (entity == null) {
         return false;
      }

      for (KillEffects.TrackedVictim trackedVictim : this.trackedVictims) {
         if (trackedVictim.isValid() && trackedVictim.entity.getId() == entity.getId()) {
            return true;
         }
      }

      return false;
   }

   private boolean hasClearEffectRay(Vec3 cameraPosition, Vec3 effectPosition) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !(cameraPosition.distanceToSqr(effectPosition) < EFFECT_RAY_MIN_DISTANCE_SQUARED)) {
         BlockHitResult hit = MINECRAFT_CLIENT.level.clip(new ClipContext(cameraPosition, effectPosition, Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
         return hit.getType() == Type.MISS;
      } else {
         return true;
      }
   }

   private void drawVignette(float progress, int guiWidth, int guiHeight) {
      if (!(progress * VIGNETTE_VISIBILITY_BYTE_SCALE < 1.0F)) {
         float paddingX = guiWidth / 2.0F * (1.0F - progress) * (1.0F - progress);
         float paddingY = guiHeight / 2.0F * (1.0F - progress) * (1.0F - progress);
         float left = -paddingX;
         float top = -paddingY;
         float width = guiWidth + paddingX * 2.0F;
         float height = guiHeight + paddingY * 2.0F;
         short red = 255;
         int green = (int)(VIGNETTE_GREEN_BYTE_SCALE * (1.0F - progress / VIGNETTE_GREEN_PROGRESS_DIVISOR));
         int blue = (int)(VIGNETTE_BLUE_BYTE_SCALE * (1.0F - progress / VIGNETTE_BLUE_PROGRESS_DIVISOR));
         int alpha = (int)(VIGNETTE_MAX_ALPHA * progress);
         this.drawNdcTexturedRectangle(left, top, width, height, ColorMath.toRgbaVector(alpha << 24 | red << 16 | green << 8 | blue), VIGNETTE_TEXTURE);
      }
   }

   private void renderLightningEffects(PoseStack poses, Vec3 cameraPosition, float cameraYaw, float cameraPitch) {
      BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int vertexCount = 0;
      long nowMillis = System.currentTimeMillis();
      Matrix4f matrix = poses.last().pose();

      for (KillEffects.TimedLightningColumn effect : this.lightningEffects) {
         if (this.hasClearEffectRay(cameraPosition, new Vec3(effect.x, effect.baseY, effect.z))) {
            float progress = (float)(nowMillis - effect.createdAtMillis) / (float)effect.durationMillis;
            if (!(progress > 1.0F)) {
               float alpha = Mth.clamp(progress < LIGHTNING_FADE_IN_PROGRESS_END ? progress / LIGHTNING_FADE_IN_PROGRESS_DIVISOR : 1.0F - (progress - LIGHTNING_FADE_OUT_PROGRESS_START) / LIGHTNING_FADE_OUT_PROGRESS_DIVISOR, 0.0F, 1.0F);
               int color = ColorMath.withAlpha(effect.colorArgb, alpha);
               List points = this.buildLightningPath(effect, nowMillis);
               float halfWidth = LIGHTNING_STRIP_HALF_WIDTH_SCALE * this.sizeSetting.getValue();

               for (int segment = 0; segment < points.size() - 1; segment++) {
                  vertexCount += this.appendCameraFacingStripQuad(builder, matrix, ((Vec3)points.get(segment)).subtract(cameraPosition), ((Vec3)points.get(segment + 1)).subtract(cameraPosition), halfWidth, color);
               }

               vertexCount += this.appendBillboardQuad(builder, poses, new Vec3(effect.x, effect.baseY, effect.z).subtract(cameraPosition), LIGHTNING_BASE_GLOW_WIDTH_SCALE * this.sizeSetting.getValue(), LIGHTNING_BASE_GLOW_HEIGHT_SCALE * this.sizeSetting.getValue(), 0.0F, cameraYaw, cameraPitch, color);
            }
         }
      }

      this.drawTexturedMesh(builder, LIGHTNING_TEXTURE, vertexCount, LIGHTNING_DRAW_LABEL, this.worldPipeline, false);
   }

   public KillEffects() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.modeSetting = new SingleChoiceSetting(MODE_LABEL, new ChoiceOption(FRAG_OPTION), new ChoiceOption(SOUL_OPTION), new ChoiceOption(WAVE_OPTION), new ChoiceOption(LIGHTNING_OPTION));
      this.playersOnlySetting = new BooleanSetting(PLAYERS_ONLY_LABEL, true);
      this.totemEffectSetting = new BooleanSetting(TOTEM_EFFECT_LABEL, true);
      this.themeColorSetting = new BooleanSetting(THEME_COLOR_LABEL, true).visibleWhen(() -> !this.modeSetting.isSelected(FRAG_THEME_COLOR_VISIBILITY_KEY));
      this.customColorSetting = new ColorSetting(CUSTOM_COLOR_LABEL, false, DEFAULT_CUSTOM_COLOR).visibleWhen(() -> !this.modeSetting.isSelected(FRAG_CUSTOM_COLOR_VISIBILITY_KEY) && !this.themeColorSetting.getValue());
      this.sizeSetting = new FloatSetting(SIZE_LABEL, 1.0F, MIN_SIZE, 2.0F).withStep(SIZE_STEP).visibleWhen(() -> !this.modeSetting.isSelected(FRAG_SIZE_VISIBILITY_KEY));
      this.durationSetting = new FloatSetting(DURATION_LABEL, DEFAULT_DURATION, MIN_DURATION, MAX_DURATION).withStep(DURATION_STEP).visibleWhen(() -> !this.modeSetting.isSelected(FRAG_DURATION_VISIBILITY_KEY));
      this.densitySetting = new FloatSetting(DENSITY_LABEL, 1.0F, MIN_DENSITY, 2.0F).withStep(DENSITY_STEP).visibleWhen(() -> !this.modeSetting.isSelected(FRAG_DENSITY_VISIBILITY_KEY));
      this.trackedVictims = new ArrayList<>();
      this.orbitParticles = new ArrayList<>();
      this.effectParticles = new ArrayList<>();
      this.waveEffects = new ArrayList<>();
      this.lightningEffects = new ArrayList<>();
      this.processedEffectEntityIds = new HashSet<>();
      this.processedFragKillEntityIds = new HashSet<>();
      this.worldPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(WORLD_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(WORLD_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(WORLD_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(WORLD_SAMPLER_NAME)
         .build();
      this.overlayPipeline = RenderPipelineBuilder.builder()
         .withLocation(OVERLAY_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(OVERLAY_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(OVERLAY_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(OVERLAY_SAMPLER_NAME)
         .build();
      this.attackListener = this::onAttackEntity;
      this.packetReceiveListener = this::onPacketReceive;
      this.playerTickListener = this::onPlayerTick;
      this.hudRenderListener = this::onHudRender;
      this.worldEffectsListener = this::onWorldEffects;
      instance = this;
      this.addSettings(this.modeSetting, this.playersOnlySetting, this.totemEffectSetting, this.themeColorSetting, this.customColorSetting, this.sizeSetting, this.durationSetting, this.densitySetting);
   }

   private void clearEffects() {
      this.trackedVictims.clear();
      this.orbitParticles.clear();
      this.effectParticles.clear();
      this.waveEffects.clear();
      this.lightningEffects.clear();
      this.processedEffectEntityIds.clear();
      this.processedFragKillEntityIds.clear();
      this.killFlashProgress = this.killFlashRiseState = 0.0F;
      this.totemFlashProgress = this.totemFlashRiseState = 0.0F;
      this.totemParticleTintActive = false;
      this.killPeakParticlesSpawned = false;
      this.lastEffectVictim = null;
      this.timerRecoveryStage = 0;
   }

   private void spawnLightningEffects(Vec3 position, float height, float width, int color, float size, float duration, float density) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int columnCount = Math.max(2, Math.round(LIGHTNING_COLUMN_COUNT_SCALE * density));

      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
         double columnX = position.x + (random.nextDouble() - LIGHTNING_COLUMN_POSITION_X_CENTER) * width;
         double columnZ = position.z + (random.nextDouble() - LIGHTNING_COLUMN_POSITION_Z_CENTER) * width;
         double columnBaseY = position.y + height * (LIGHTNING_COLUMN_BASE_Y_HEIGHT_SCALE + random.nextDouble() * LIGHTNING_COLUMN_RANDOM_Y_HEIGHT_SCALE);
         this.lightningEffects
            .add(new KillEffects.TimedLightningColumn(columnX, columnBaseY, columnZ, columnBaseY + LIGHTNING_COLUMN_BASE_HEIGHT + random.nextDouble() * LIGHTNING_COLUMN_RANDOM_HEIGHT_SCALE * size, color, (long)(duration * LIGHTNING_COLUMN_DURATION_MILLIS_SCALE), random.nextLong()));
      }

      int sparkCount = Math.round(LIGHTNING_SPARK_PARTICLE_COUNT_SCALE * density);

      for (int sparkIndex = 0; sparkIndex < sparkCount; sparkIndex++) {
         double angleRadians = random.nextDouble() * LIGHTNING_SPARK_FULL_TURN_RADIANS;
         double horizontalSpeed = LIGHTNING_SPARK_BASE_HORIZONTAL_SPEED + random.nextDouble() * LIGHTNING_SPARK_RANDOM_HORIZONTAL_SPEED_RANGE;
         Vec3 sparkPosition = position.add((random.nextDouble() - LIGHTNING_SPARK_POSITION_X_CENTER) * width, height * LIGHTNING_SPARK_POSITION_Y_HEIGHT_SCALE + (random.nextDouble() - LIGHTNING_SPARK_POSITION_Y_CENTER) * LIGHTNING_SPARK_POSITION_Y_RANDOM_RANGE, (random.nextDouble() - LIGHTNING_SPARK_POSITION_Z_CENTER) * width);
         Vec3 sparkVelocity = new Vec3(Math.cos(angleRadians) * horizontalSpeed, LIGHTNING_SPARK_BASE_VELOCITY_Y + random.nextDouble() * LIGHTNING_SPARK_RANDOM_VELOCITY_Y_RANGE, Math.sin(angleRadians) * horizontalSpeed);
         this.effectParticles
            .add(
               new KillEffects.EffectParticle(
                  sparkPosition, sparkVelocity, color, size * (LIGHTNING_SPARK_SIZE_BASE_SCALE + random.nextFloat() * LIGHTNING_SPARK_SIZE_RANDOM_SCALE), duration * LIGHTNING_SPARK_DURATION_SCALE, KillEffects.ParticleKind.SPARK, random.nextFloat() * LIGHTNING_SPARK_INITIAL_ROTATION_RANGE_DEGREES
               )
            );
      }
   }

   private void spawnConfiguredEffect(LivingEntity entity) {
      if (this.isEnabled() && this.isEligibleVictim(entity)) {
         Vec3 position = entity.position();
         float height = Math.max(entity.getBbHeight(), MIN_EFFECT_ENTITY_HEIGHT);
         float width = Math.max(entity.getBbWidth(), MIN_EFFECT_ENTITY_WIDTH);
         int color = this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.customColorSetting.getValue();
         float size = this.sizeSetting.getValue();
         float duration = this.durationSetting.getValue();
         float density = this.densitySetting.getValue();
         switch (this.modeSetting.getSelectedOption().getName()) {
            case SOUL_TRIGGER_MODE_KEY:
               this.spawnSoulParticles(position, height, width, color, size, duration, density);
               break;
            case WAVE_TRIGGER_MODE_KEY:
               this.spawnWaveEffects(position, height, width, color, size, duration, density);
               break;
            case LIGHTNING_TRIGGER_MODE_KEY:
               this.spawnLightningEffects(position, height, width, color, size, duration, density);
         }
      }
   }

   private void updateFlashProgress() {
      if (!this.modeSetting.isSelected(FRAG_PROGRESS_MODE_KEY)) {
         this.killFlashProgress = Mth.lerp(INACTIVE_KILL_FLASH_DECAY_LERP, this.killFlashProgress, 0.0F);
         this.totemFlashProgress = Mth.lerp(INACTIVE_TOTEM_FLASH_DECAY_LERP, this.totemFlashProgress, 0.0F);
      } else {
         if (this.killFlashRiseState == 1.0F) {
            this.killFlashProgress = Math.min(1.0F, this.killFlashProgress + KILL_FLASH_RISE_STEP);
            if (this.killFlashProgress > KILL_FLASH_PEAK_THRESHOLD) {
               this.killFlashProgress = 1.0F;
               this.killFlashRiseState = 0.0F;
               if (!this.killPeakParticlesSpawned && this.lastEffectVictim != null) {
                  this.spawnOrbitParticles(this.lastEffectVictim, 280, KILL_PEAK_LONG_ORBIT_LIFETIME_MILLIS, KILL_PEAK_LONG_ORBIT_SPREAD_SCALE);
                  this.spawnOrbitParticles(this.lastEffectVictim, 520, KILL_PEAK_SHORT_ORBIT_LIFETIME_MILLIS, KILL_PEAK_SHORT_ORBIT_SPREAD_SCALE);
                  this.killPeakParticlesSpawned = true;
               }
            }
         } else {
            this.killFlashProgress = Math.max(0.0F, this.killFlashProgress - KILL_FLASH_FALL_STEP);
            if (this.killFlashProgress < KILL_FLASH_RESET_THRESHOLD) {
               this.killFlashProgress = 0.0F;
            }
         }

         if (this.totemFlashRiseState == 1.0F) {
            this.totemFlashProgress = Math.min(1.0F, this.totemFlashProgress + TOTEM_FLASH_RISE_STEP);
            if (this.totemFlashProgress > TOTEM_FLASH_PEAK_THRESHOLD) {
               this.totemFlashProgress = 1.0F;
               this.totemFlashRiseState = 0.0F;
            }
         } else {
            this.totemFlashProgress = Math.max(0.0F, this.totemFlashProgress - TOTEM_FLASH_FALL_STEP);
            if (this.totemFlashProgress < TOTEM_FLASH_RESET_THRESHOLD) {
               this.totemFlashProgress = 0.0F;
            }
         }
      }
   }

   private void appendVertex(BufferBuilder builder, Matrix4f matrix, float x, float y, float z, float u, float v, Vector4f color) {
      builder.addVertex(matrix, x, y, z).setUv(u, v).setColor(color.x, color.y, color.z, color.w);
   }

   private float getKillFlashProgress() {
      return Mth.clamp(this.killFlashProgress, 0.0F, 1.0F);
   }

   private void handleTrackedVictimDeath(LivingEntity entity) {
      KillEffects.TrackedVictim trackedVictim = null;

      for (KillEffects.TrackedVictim candidate : this.trackedVictims) {
         if (candidate.entity.getId() == entity.getId()) {
            trackedVictim = candidate;
            break;
         }
      }

      if (trackedVictim != null && trackedVictim.isValid() && trackedVictim.attackedByLocalPlayer) {
         this.triggerKillEffect(entity);
      }
   }

   private void handleTotemUse(LivingEntity entity) {
      if (this.isEligibleVictim(entity) && this.totemEffectSetting.getValue()) {
         if (this.isTrackedVictim(entity)) {
            this.lastEffectVictim = entity;
            if (this.modeSetting.isSelected(FRAG_TOTEM_MODE_KEY)) {
               this.totemParticleTintActive = true;
               this.totemFlashRiseState = 1.0F;
               this.totemFlashProgress = INITIAL_TOTEM_FLASH_PROGRESS;
               this.playEffectSound(TOTEM_SOUND, TOTEM_SOUND_VOLUME);
               ClientTimerSpeed.Оэ(TOTEM_TIMER_SPEED);
               this.timerRecoveryStage = 2;
               this.timerRecoveryDeadlineMillis = System.currentTimeMillis() + TOTEM_TIMER_DELAY_MILLIS;
               this.spawnOrbitParticles(entity, 320, TOTEM_SHORT_ORBIT_LIFETIME_MILLIS, 2.0F);
               this.spawnOrbitParticles(entity, 180, TOTEM_LONG_ORBIT_LIFETIME_MILLIS, TOTEM_LONG_ORBIT_SPREAD_SCALE);
            } else {
               this.spawnConfiguredEffect(entity);
            }
         }
      }
   }

   private void trackVictim(LivingEntity entity, boolean attackedLocally) {
      if (this.isEligibleVictim(entity)) {
         for (KillEffects.TrackedVictim trackedVictim : this.trackedVictims) {
            if (trackedVictim.entity.getId() == entity.getId()) {
               trackedVictim.refresh(entity);
               if (attackedLocally) {
                  trackedVictim.markAttacked();
               }

               return;
            }
         }

         if (attackedLocally) {
            this.trackedVictims.clear();
            KillEffects.TrackedVictim newVictim = new KillEffects.TrackedVictim(entity);
            newVictim.markAttacked();
            this.trackedVictims.add(newVictim);
         }
      }
   }

   private void onAttackEntity(AttackEntityEvent event) {
      if (this.isEnabled() && event.getPhase() == AttackEntityEvent.Phase.POST && event.getAttacker() == MINECRAFT_CLIENT.player) {
         if (event.getTarget() instanceof LivingEntity entity && entity.isAlive()) {
            this.trackVictim(entity, true);
         }
      }
   }

   private void spawnWaveEffects(Vec3 position, float height, float width, int color, float size, float duration, float density) {
      this.waveEffects.add(new KillEffects.TimedGlow(position.x, position.y + WAVE_PRIMARY_SURFACE_OFFSET, position.z, color, size * WAVE_PRIMARY_RADIUS_SCALE, (long)(duration * WAVE_PRIMARY_DURATION_MILLIS_SCALE)));
      this.waveEffects.add(new KillEffects.TimedGlow(position.x, position.y + WAVE_SECONDARY_SURFACE_OFFSET, position.z, color, size * WAVE_SECONDARY_RADIUS_SCALE, (long)(duration * WAVE_SECONDARY_DURATION_MILLIS_SCALE), WAVE_SECONDARY_DELAY_MILLIS));
      int burstCount = Math.round(WAVE_BURST_PARTICLE_COUNT_SCALE * density);
      ThreadLocalRandom random = ThreadLocalRandom.current();

      for (int burstIndex = 0; burstIndex < burstCount; burstIndex++) {
         double angleRadians = random.nextDouble() * WAVE_BURST_FULL_TURN_RADIANS;
         double horizontalSpeed = WAVE_BURST_BASE_HORIZONTAL_SPEED + random.nextDouble() * WAVE_BURST_RANDOM_HORIZONTAL_SPEED_SCALE * size;
         Vec3 burstPosition = position.add(Math.cos(angleRadians) * width * WAVE_BURST_POSITION_X_WIDTH_SCALE, height * (WAVE_BURST_POSITION_Y_BASE_HEIGHT_SCALE + random.nextDouble() * WAVE_BURST_POSITION_Y_RANDOM_HEIGHT_SCALE), Math.sin(angleRadians) * width * WAVE_BURST_POSITION_Z_WIDTH_SCALE);
         Vec3 burstVelocity = new Vec3(Math.cos(angleRadians) * horizontalSpeed, WAVE_BURST_BASE_VELOCITY_Y + random.nextDouble() * WAVE_BURST_RANDOM_VELOCITY_Y_RANGE, Math.sin(angleRadians) * horizontalSpeed);
         this.effectParticles
            .add(
               new KillEffects.EffectParticle(
                  burstPosition, burstVelocity, color, size * (WAVE_BURST_SIZE_BASE_SCALE + random.nextFloat() * WAVE_BURST_SIZE_RANDOM_SCALE), duration * WAVE_BURST_DURATION_SCALE, KillEffects.ParticleKind.BURST, (float)Math.toDegrees(angleRadians)
               )
            );
      }
   }

   private void spawnSoulParticles(Vec3 position, float height, float width, int color, float size, float duration, float density) {
      int particleCount = Math.round(SOUL_PARTICLE_COUNT_SCALE * density);
      ThreadLocalRandom random = ThreadLocalRandom.current();

      for (int particleIndex = 0; particleIndex < particleCount; particleIndex++) {
         Vec3 particlePosition = position.add((random.nextDouble() - SOUL_POSITION_X_CENTER) * width * SOUL_POSITION_X_WIDTH_SCALE, random.nextDouble() * height, (random.nextDouble() - SOUL_POSITION_Z_CENTER) * width * SOUL_POSITION_Z_WIDTH_SCALE);
         Vec3 particleVelocity = new Vec3((random.nextDouble() - SOUL_VELOCITY_X_CENTER) * SOUL_VELOCITY_X_SCALE, SOUL_BASE_VELOCITY_Y + random.nextDouble() * SOUL_RANDOM_VELOCITY_Y_RANGE, (random.nextDouble() - SOUL_VELOCITY_Z_CENTER) * SOUL_VELOCITY_Z_SCALE);
         this.effectParticles
            .add(
               new KillEffects.EffectParticle(
                  particlePosition,
                  particleVelocity,
                  color,
                  size * (SOUL_SIZE_BASE_SCALE + random.nextFloat() * SOUL_SIZE_RANDOM_SCALE),
                  duration * (SOUL_DURATION_BASE_SCALE + random.nextFloat() * SOUL_DURATION_RANDOM_SCALE),
                  KillEffects.ParticleKind.SOUL,
                  random.nextFloat() * SOUL_INITIAL_ROTATION_RANGE
               )
            );
      }
   }

   private static float easeOutCubic(float value) {
      float remaining = 1.0F - value;
      return 1.0F - remaining * remaining * remaining;
   }

   private boolean isEligibleVictim(LivingEntity entity) {
      return entity != null && entity != MINECRAFT_CLIENT.player ? !this.playersOnlySetting.getValue() || entity instanceof Player : false;
   }

   private void triggerKillEffect(LivingEntity entity) {
      if (this.isEligibleVictim(entity)) {
         this.lastEffectVictim = entity;
         if (this.modeSetting.isSelected(FRAG_KILL_MODE_KEY)) {
            if (!this.processedFragKillEntityIds.add(entity.getId())) {
               return;
            }

            this.totemParticleTintActive = false;
            this.killPeakParticlesSpawned = false;
            this.killFlashRiseState = 1.0F;
            this.killFlashProgress = INITIAL_KILL_FLASH_PROGRESS;
            this.playEffectSound(KILL_SOUND, KILL_SOUND_VOLUME);
            this.lerpAimTowardEntity(entity, KILL_AIM_LERP_PROGRESS);
            ClientTimerSpeed.Оэ(KILL_INITIAL_TIMER_SPEED);
            this.timerRecoveryStage = 1;
            this.timerRecoveryDeadlineMillis = System.currentTimeMillis() + KILL_INITIAL_TIMER_DELAY_MILLIS;
            this.spawnOrbitParticles(entity, 700, KILL_INITIAL_SHORT_ORBIT_LIFETIME_MILLIS, KILL_INITIAL_SHORT_ORBIT_SPREAD_SCALE);
            this.spawnOrbitParticles(entity, 900, KILL_INITIAL_LONG_ORBIT_LIFETIME_MILLIS, KILL_INITIAL_LONG_ORBIT_SPREAD_SCALE);
         } else if (this.processedEffectEntityIds.add(entity.getId())) {
            this.spawnConfiguredEffect(entity);
         }
      }
   }

   private void spawnOrbitParticles(LivingEntity entity, int count, float lifetimeMillis) {
      this.spawnOrbitParticles(entity, count, lifetimeMillis, DEFAULT_ORBIT_SPREAD_SCALE);
   }

   private void renderWaveEffects(PoseStack poses, Vec3 cameraPosition) {
      BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int vertexCount = 0;
      long nowMillis = System.currentTimeMillis();
      Matrix4f matrix = poses.last().pose();

      for (KillEffects.TimedGlow effect : this.waveEffects) {
         Vec3 position = new Vec3(effect.x, effect.y, effect.z);
         if (this.hasClearEffectRay(cameraPosition, position)) {
            float progress = (float)(nowMillis - effect.createdAtMillis - effect.delayMillis) / (float)effect.durationMillis;
            if (!(progress < 0.0F) && !(progress > 1.0F)) {
               float radiusScale = easeOutCubic(progress);
               float alpha = (1.0F - progress) * (progress < WAVE_FADE_IN_PROGRESS_END ? progress / WAVE_FADE_IN_PROGRESS_DIVISOR : 1.0F);
               float radius = effect.radius * radiusScale;
               int color = ColorMath.withAlpha(effect.colorArgb, alpha * WAVE_ALPHA_SCALE);
               double relativeX = effect.x - cameraPosition.x;
               double relativeY = effect.y - cameraPosition.y;
               double relativeZ = effect.z - cameraPosition.z;
               Vector4f rgba = ColorMath.toRgbaVector(color);
               this.appendVertex(builder, matrix, (float)(relativeX - radius), (float)relativeY, (float)(relativeZ - radius), 0.0F, 0.0F, rgba);
               this.appendVertex(builder, matrix, (float)(relativeX - radius), (float)relativeY, (float)(relativeZ + radius), 0.0F, 1.0F, rgba);
               this.appendVertex(builder, matrix, (float)(relativeX + radius), (float)relativeY, (float)(relativeZ + radius), 1.0F, 1.0F, rgba);
               this.appendVertex(builder, matrix, (float)(relativeX + radius), (float)relativeY, (float)(relativeZ - radius), 1.0F, 0.0F, rgba);
               vertexCount += 4;
            }
         }
      }

      this.drawTexturedMesh(builder, WAVE_TEXTURE, vertexCount, WAVES_DRAW_LABEL, this.worldPipeline, false);
   }

   private void spawnOrbitParticles(LivingEntity entity, int count, float lifetimeMillis, float spreadScale) {
      if (entity != null) {
         float baseRadius = ORBIT_BASE_RADIUS + this.totemFlashProgress * ORBIT_TOTEM_RADIUS_SCALE;
         float riseHeightRange = baseRadius / ORBIT_VERTICAL_RANGE_DIVISOR;
         float verticalSpreadRange = riseHeightRange;
         Vec3 basePosition = entity.position().add(0.0, entity.getEyeHeight() * ORBIT_ENTITY_EYE_HEIGHT_SCALE, 0.0);

         for (int particleIndex = 0; particleIndex < count; particleIndex++) {
            float angleProgress = (float)particleIndex / count;
            float verticalOffset = (float)(-verticalSpreadRange / 2.0F + Math.random() * verticalSpreadRange);
            float radiusGrowth = baseRadius * spreadScale * (ORBIT_RADIUS_GROWTH_BASE_SCALE + (float)Math.random() * ORBIT_RADIUS_GROWTH_RANDOM_SCALE);
            float riseHeight = (float)(riseHeightRange * Math.random() * spreadScale);
            this.orbitParticles
               .add(new KillEffects.OrbitParticle(basePosition.add(0.0, verticalOffset, 0.0), angleProgress * ORBIT_START_ANGLE_RANGE_DEGREES, baseRadius * ORBIT_START_RADIUS_SCALE, baseRadius + radiusGrowth, ORBIT_ANGLE_TRAVEL_RANGE_DEGREES * (float)Math.random(), riseHeight, lifetimeMillis));
         }
      }
   }

   @Override
   public void onDisable() {
      this.clearEffects();
      ClientTimerSpeed.Оэ(1.0F);
   }

   private void renderParticlesOfKind(PoseStack poses, Vec3 cameraPosition, float cameraYaw, float cameraPitch, float tickDelta, KillEffects.ParticleKind kind, Identifier textureId, float sizeScale) {
      BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int vertexCount = 0;

      for (KillEffects.EffectParticle particle : this.effectParticles) {
         if (particle.kind == kind) {
            float alpha = particle.getAlpha();
            if (!(alpha <= PARTICLE_MIN_VISIBLE_ALPHA)) {
               Vec3 position = particle.getInterpolatedPosition(tickDelta);
               if (this.hasClearEffectRay(cameraPosition, position)) {
                  Vec3 cameraRelativePosition = position.subtract(cameraPosition);
                  vertexCount += this.appendBillboardQuad(builder, poses, cameraRelativePosition, particle.size * sizeScale, particle.size * sizeScale, particle.rotation, cameraYaw, cameraPitch, ColorMath.withAlpha(particle.colorArgb, alpha));
               }
            }
         }
      }

      this.drawTexturedMesh(builder, textureId, vertexCount, EFFECT_PARTICLES_DRAW_LABEL, this.worldPipeline, false);
   }

   private void lerpAimTowardEntity(Entity entity, float progress) {
      if (MINECRAFT_CLIENT.player != null && entity != null) {
         Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
         Vec3 targetCenter = entity.position().add(0.0, entity.getBbHeight() * AIM_TARGET_HEIGHT_SCALE, 0.0);
         Vec3 difference = targetCenter.subtract(eyePosition);
         double horizontalDistance = Math.sqrt(difference.x * difference.x + difference.z * difference.z);
         float targetYaw = (float)(Mth.atan2(difference.z, difference.x) * AIM_YAW_RADIANS_TO_DEGREES) - AIM_YAW_OFFSET_DEGREES;
         float targetPitch = (float)(-(Mth.atan2(difference.y, horizontalDistance) * AIM_PITCH_RADIANS_TO_DEGREES));
         float yawDifference = Mth.wrapDegrees(targetYaw - MINECRAFT_CLIENT.player.getYRot());
         float pitchDifference = targetPitch - MINECRAFT_CLIENT.player.getXRot();
         if (Math.abs(yawDifference) > AIM_YAW_THRESHOLD_DEGREES || Math.abs(pitchDifference) > AIM_PITCH_THRESHOLD_DEGREES) {
            MINECRAFT_CLIENT.player.setYRot(Mth.lerp(progress, MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getYRot() + yawDifference));
            MINECRAFT_CLIENT.player.setXRot(Mth.lerp(progress, MINECRAFT_CLIENT.player.getXRot(), targetPitch));
         }
      }
   }

   private void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacket() instanceof ClientboundEntityEventPacket entityEventPacket && MINECRAFT_CLIENT.level != null) {
         if (entityEventPacket.getEntity(MINECRAFT_CLIENT.level) instanceof LivingEntity entity) {
            if (this.isEligibleVictim(entity)) {
               if (this.isTrackedVictim(entity)) {
                  if (entityEventPacket.getEventId() == 35) {
                     if (!this.totemEffectSetting.getValue()) {
                        return;
                     }

                     MINECRAFT_CLIENT.execute(() -> this.handleTotemUse(entity));
                  } else if (entityEventPacket.getEventId() == 3) {
                     MINECRAFT_CLIENT.execute(() -> this.handleTrackedVictimDeath(entity));
                  }
               }
            }
         }
      }
   }

   private enum ParticleKind {
      SOUL,
      BURST,
      SPARK;
   }

   private static final class TrackedVictim {
      private @com.google.gson.annotations.SerializedName("Ь")  LivingEntity entity;
      private @com.google.gson.annotations.SerializedName("ХЧ")  long lastTrackedMillis;
      private @com.google.gson.annotations.SerializedName("ХЛ")  long validityMillis;
      private @com.google.gson.annotations.SerializedName("Хо")  boolean deathEffectTriggered;
      private @com.google.gson.annotations.SerializedName("ХO")  boolean attackedByLocalPlayer;
      private static final long INITIAL_VALIDITY_MILLIS = 2500L;
      private static final long REFRESH_VALIDITY_MILLIS = 2500L;

      private void markAttacked() {
         this.attackedByLocalPlayer = true;
         this.lastTrackedMillis = System.currentTimeMillis();
      }

      private TrackedVictim(LivingEntity entity) {
         this.validityMillis = INITIAL_VALIDITY_MILLIS;
         this.refresh(entity);
      }

      private void refresh(LivingEntity entity) {
         this.entity = entity;
         this.lastTrackedMillis = System.currentTimeMillis();
         this.validityMillis = REFRESH_VALIDITY_MILLIS;
         if (entity.getHealth() > 0.0F) {
            this.deathEffectTriggered = false;
         }
      }

      private void setValidityMillis(long validityMillis) {
         this.validityMillis = validityMillis;
         this.lastTrackedMillis = System.currentTimeMillis();
      }

      private boolean isValid() {
         return this.entity != null && System.currentTimeMillis() - this.lastTrackedMillis < this.validityMillis;
      }
   }

   private static final class EffectParticle {
      private @com.google.gson.annotations.SerializedName("Яи")  double x;
      private @com.google.gson.annotations.SerializedName("Я9")  double y;
      private @com.google.gson.annotations.SerializedName("Яо")  double z;
      private @com.google.gson.annotations.SerializedName("ЯO")  double previousX;
      private @com.google.gson.annotations.SerializedName("Я7")  double previousY;
      private @com.google.gson.annotations.SerializedName("ЯР")  double previousZ;
      private @com.google.gson.annotations.SerializedName("Я3")  double velocityX;
      private @com.google.gson.annotations.SerializedName("Яр")  double velocityY;
      private @com.google.gson.annotations.SerializedName("Я6")  double velocityZ;
      private final @com.google.gson.annotations.SerializedName("Ды")  int colorArgb;
      private final @com.google.gson.annotations.SerializedName("тМ")  float size;
      private final @com.google.gson.annotations.SerializedName("ХЖ")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Хм")  long durationMillis;
      private final @com.google.gson.annotations.SerializedName("В")  KillEffects.ParticleKind kind;
      private @com.google.gson.annotations.SerializedName("тН")  float rotation;
      private static final float MILLIS_PER_DURATION_SECOND = 1000.0F;
      private static final float MILLIS_PER_OSCILLATION_SECOND = 1000.0F;
      private static final double SOUL_X_OSCILLATION_FREQUENCY = 3.1;
      private static final double SOUL_X_OSCILLATION_AMPLITUDE = 0.0015;
      private static final double SOUL_Z_OSCILLATION_FREQUENCY = 2.7;
      private static final double SOUL_Z_OSCILLATION_AMPLITUDE = 0.0015;
      private static final double SOUL_VERTICAL_DRAG = 0.995;
      private static final double SOUL_VERTICAL_LIFT = 0.0012;
      private static final double PARTICLE_GRAVITY = 0.0045;
      private static final double VELOCITY_X_DRAG = 0.96;
      private static final double VELOCITY_Y_DRAG = 0.96;
      private static final double VELOCITY_Z_DRAG = 0.96;
      private static final float SOUL_ROTATION_STEP = 1.2F;
      private static final float OTHER_PARTICLE_ROTATION_STEP = 4.5F;
      private static final long FADE_IN_MILLIS = 120L;
      private static final float FADE_IN_FLOAT_MILLIS = 120.0F;
      private static final float FADE_OUT_FLOAT_MILLIS = 280.0F;
      private static final long FADE_OUT_MILLIS = 280L;

      private Vec3 getInterpolatedPosition(float tickDelta) {
         return new Vec3(Mth.lerp(tickDelta, this.previousX, this.x), Mth.lerp(tickDelta, this.previousY, this.y), Mth.lerp(tickDelta, this.previousZ, this.z));
      }

      private float getAlpha() {
         long elapsedMillis = System.currentTimeMillis() - this.createdAtMillis;
         if (elapsedMillis < FADE_IN_MILLIS) {
            return Mth.clamp((float)elapsedMillis / FADE_IN_FLOAT_MILLIS, 0.0F, 1.0F);
         } else {
            return elapsedMillis <= this.durationMillis ? 1.0F : 1.0F - Mth.clamp((float)(elapsedMillis - this.durationMillis) / FADE_OUT_FLOAT_MILLIS, 0.0F, 1.0F);
         }
      }

      private boolean isExpired() {
         return System.currentTimeMillis() - this.createdAtMillis > this.durationMillis + FADE_OUT_MILLIS;
      }

      private void tick() {
         this.previousX = this.x;
         this.previousY = this.y;
         this.previousZ = this.z;
         if (this.kind == KillEffects.ParticleKind.SOUL) {
            float elapsedSeconds = (float)(System.currentTimeMillis() - this.createdAtMillis) / MILLIS_PER_OSCILLATION_SECOND;
            this.velocityX = this.velocityX + Math.sin(elapsedSeconds * SOUL_X_OSCILLATION_FREQUENCY + this.rotation) * SOUL_X_OSCILLATION_AMPLITUDE;
            this.velocityZ = this.velocityZ + Math.cos(elapsedSeconds * SOUL_Z_OSCILLATION_FREQUENCY + this.rotation) * SOUL_Z_OSCILLATION_AMPLITUDE;
            this.velocityY = this.velocityY * SOUL_VERTICAL_DRAG;
            this.velocityY = this.velocityY + SOUL_VERTICAL_LIFT;
         } else {
            this.velocityY = this.velocityY - PARTICLE_GRAVITY;
            this.velocityX = this.velocityX * VELOCITY_X_DRAG;
            this.velocityY = this.velocityY * VELOCITY_Y_DRAG;
            this.velocityZ = this.velocityZ * VELOCITY_Z_DRAG;
         }

         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         this.rotation = this.rotation + (this.kind == KillEffects.ParticleKind.SOUL ? SOUL_ROTATION_STEP : OTHER_PARTICLE_ROTATION_STEP);
      }

      private EffectParticle(Vec3 position, Vec3 velocity, int color, float size, float durationSeconds, KillEffects.ParticleKind kind, float rotation) {
         this.x = this.previousX = position.x;
         this.y = this.previousY = position.y;
         this.z = this.previousZ = position.z;
         this.velocityX = velocity.x;
         this.velocityY = velocity.y;
         this.velocityZ = velocity.z;
         this.colorArgb = color;
         this.size = size;
         this.durationMillis = (long)(durationSeconds * MILLIS_PER_DURATION_SECOND);
         this.kind = kind;
         this.rotation = rotation;
      }
   }

   private static final class TimedGlow {
      private final @com.google.gson.annotations.SerializedName("В4")  double x;
      private final @com.google.gson.annotations.SerializedName("Вп")  double y;
      private final @com.google.gson.annotations.SerializedName("ВД")  double z;
      private final @com.google.gson.annotations.SerializedName("еД")  int colorArgb;
      private final @com.google.gson.annotations.SerializedName("АА")  float radius;
      private final @com.google.gson.annotations.SerializedName("ВН")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Вн")  long durationMillis;
      private final @com.google.gson.annotations.SerializedName("ВЦ")  long delayMillis;

      private TimedGlow(double x, double y, double z, int color, float radius, long durationMillis, long delayMillis) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.colorArgb = color;
         this.radius = radius;
         this.durationMillis = durationMillis;
         this.delayMillis = delayMillis;
      }

      private TimedGlow(double x, double y, double z, int color, float radius, long durationMillis) {
         this(x, y, z, color, radius, durationMillis, 0L);
      }
   }

   private static final class TimedLightningColumn {
      private final @com.google.gson.annotations.SerializedName("ЯД")  double x;
      private final @com.google.gson.annotations.SerializedName("ЯЪ")  double baseY;
      private final @com.google.gson.annotations.SerializedName("Ят")  double z;
      private final @com.google.gson.annotations.SerializedName("Яа")  double tipY;
      @SerializedName("4м")
      private final int colorArgb;
      private final @com.google.gson.annotations.SerializedName("Яз")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Яц")  long durationMillis;
      private final @com.google.gson.annotations.SerializedName("Яя")  long noiseSeed;

      private TimedLightningColumn(double x, double baseY, double z, double tipY, int color, long durationMillis, long noiseSeed) {
         this.x = x;
         this.baseY = baseY;
         this.z = z;
         this.tipY = tipY;
         this.colorArgb = color;
         this.durationMillis = durationMillis;
         this.noiseSeed = noiseSeed;
      }
   }

   private static final class OrbitParticle {
      private final @com.google.gson.annotations.SerializedName("У")  Vec3 basePosition;
      private final @com.google.gson.annotations.SerializedName("Дю")  float startAngleDegrees;
      private final @com.google.gson.annotations.SerializedName("ДЩ")  float endAngleDegrees;
      private final @com.google.gson.annotations.SerializedName("Ду")  float startRadius;
      private final @com.google.gson.annotations.SerializedName("Д2")  float endRadius;
      private final @com.google.gson.annotations.SerializedName("Дф")  float riseHeight;
      private final @com.google.gson.annotations.SerializedName("Ди")  float lifetimeMillis;
      private final @com.google.gson.annotations.SerializedName("Д9")  float variantRandom = (float)Math.random();
      private final @com.google.gson.annotations.SerializedName("ХМ")  long createdAtMillis = System.currentTimeMillis();
      private static final float ENVELOPE_MIDPOINT = 0.5F;

      private float getProgress() {
         return Mth.clamp((float)(System.currentTimeMillis() - this.createdAtMillis) / this.lifetimeMillis, 0.0F, 1.0F);
      }

      private Vec3 getPosition() {
         float progress = this.getProgress();
         float angleDegrees = Mth.lerp(progress, this.startAngleDegrees, this.endAngleDegrees);
         float radius = Mth.lerp(progress, this.startRadius, this.endRadius);
         float angleRadians = (float)Math.toRadians(angleDegrees);
         return this.basePosition.add(Mth.sin(angleRadians) * radius, this.riseHeight * progress, Mth.cos(angleRadians) * radius);
      }

      private float getRemainingAlpha() {
         return 1.0F - this.getProgress();
      }

      private OrbitParticle(Vec3 basePosition, float startAngleDegrees, float startRadius, float endRadius, float angleTravelDegrees, float riseHeight, float lifetimeMillis) {
         this.basePosition = basePosition;
         this.startAngleDegrees = startAngleDegrees;
         this.endAngleDegrees = startAngleDegrees + angleTravelDegrees;
         this.startRadius = startRadius;
         this.endRadius = endRadius;
         this.riseHeight = riseHeight;
         this.lifetimeMillis = lifetimeMillis;
      }

      private boolean isExpired() {
         return this.getProgress() >= 1.0F;
      }

      private float getEnvelope() {
         float progress = this.getProgress();
         return (progress > ENVELOPE_MIDPOINT ? 1.0F - progress : progress) * 2.0F;
      }
   }
}
