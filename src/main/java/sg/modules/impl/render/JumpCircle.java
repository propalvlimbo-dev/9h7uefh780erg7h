package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
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
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;

public class JumpCircle extends ClientModule {
   private static final Identifier RING_TEXTURE = Identifier.fromNamespaceAndPath(JumpCircle.RING_TEXTURE_NAMESPACE, JumpCircle.RING_TEXTURE_PATH);
   private static final Identifier ALTERNATE_RING_TEXTURE = Identifier.fromNamespaceAndPath(JumpCircle.ALTERNATE_RING_TEXTURE_NAMESPACE, JumpCircle.ALTERNATE_RING_TEXTURE_PATH);
   private static final int Дй = 200;
   private static final Identifier[] LEECH_FRAME_TEXTURES = createLeechFrameTextures();
   private static final int ДЖ = 16;
   private static final int Дм = 32;
   private static final int ДК = 64;
   private static final int ДI = 1648;
   private final @com.google.gson.annotations.SerializedName("с衣")  SingleChoiceSetting shapeSetting;
   private final @com.google.gson.annotations.SerializedName("ХO")  FloatSetting radiusSetting;
   private final @com.google.gson.annotations.SerializedName("Яс")  BooleanSetting themeColorSetting;
   @SerializedName("у")
   private final ColorSetting glowColorSetting;
   private final @com.google.gson.annotations.SerializedName("ЯВ")  BooleanSetting distortionSetting;
   private final @com.google.gson.annotations.SerializedName("Х7")  FloatSetting distortionStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("ХР")  FloatSetting alternateRingLifetimeStepsSetting;
   private final @com.google.gson.annotations.SerializedName("Х3")  FloatSetting alternateRingRadiusSetting;
   private final @com.google.gson.annotations.SerializedName("ЯЯ")  BooleanSetting alternateRingGlassSetting;
   private final @com.google.gson.annotations.SerializedName("сш")  SingleChoiceSetting leechAnimationSetting;
   private final @com.google.gson.annotations.SerializedName("Хр")  FloatSetting leechLifetimeMillisSetting;
   private final @com.google.gson.annotations.SerializedName("Х6")  FloatSetting leechSizeSetting;
   private final @com.google.gson.annotations.SerializedName("сИ")  SingleChoiceSetting leechColorModeSetting;
   @SerializedName("2")
   private final ColorSetting leechPrimaryColorSetting;
   @SerializedName("ф")
   private final ColorSetting leechSecondaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("ЯХ")  BooleanSetting leechDeepLightSetting;
   private final @com.google.gson.annotations.SerializedName("Х_")  FloatSetting flexibleLifetimeMillisSetting;
   private final @com.google.gson.annotations.SerializedName("ХШ")  FloatSetting flexibleSizeSetting;
   private final @com.google.gson.annotations.SerializedName("сь")  SingleChoiceSetting flexibleAnimationSetting;
   private final @com.google.gson.annotations.SerializedName("Хы")  FloatSetting flexibleWaveStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Хй")  FloatSetting flexibleWaveThicknessSetting;
   private final @com.google.gson.annotations.SerializedName("ХЖ")  FloatSetting flexibleSaturationSetting;
   private final @com.google.gson.annotations.SerializedName("Яе")  BooleanSetting flexibleTurbulenceSetting;
   private final @com.google.gson.annotations.SerializedName("Хм")  FloatSetting flexibleTurbulenceStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("ЯА")  BooleanSetting flexibleGlowSetting;
   private final @com.google.gson.annotations.SerializedName("ХК")  FloatSetting flexibleGlowHeightSetting;
   @SerializedName("ХI")
   private final FloatSetting flexibleGlowWidthSetting;
   private final @com.google.gson.annotations.SerializedName("ХТ")  FloatSetting flexibleGlowColorStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("ХО")  FloatSetting flexibleGlowOpacitySetting;
   private final @com.google.gson.annotations.SerializedName("ХФ")  FloatSetting flexibleColorStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("сж")  SingleChoiceSetting flexibleWaveColorModeSetting;
   private final @com.google.gson.annotations.SerializedName("Я4")  BooleanSetting flexibleSecondColorEnabledSetting;
   private final @com.google.gson.annotations.SerializedName("и")  ColorSetting flexiblePrimaryColorSetting;
   @SerializedName("9")
   private final ColorSetting flexibleSecondaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("сУ")  List<JumpCircle.RingEffect> ringEffects;
   private final @com.google.gson.annotations.SerializedName("сЗ")  List<JumpCircle.LightningEffect> lightningEffects;
   private final @com.google.gson.annotations.SerializedName("сЕ")  List<JumpCircle.LeechEffect> leechEffects;
   private final @com.google.gson.annotations.SerializedName("сП")  List<JumpCircle.AlternateRingSurfaceEffect> alternateRingSurfaceEffects;
   private final @com.google.gson.annotations.SerializedName("с西")  List<JumpCircle.FlexibleWaveEffect> flexibleWaveEffects;
   private final @com.google.gson.annotations.SerializedName("сС")  RenderPipeline ringPipeline;
   private final @com.google.gson.annotations.SerializedName("сз")  RenderPipeline alternateRingPipeline;
   private final @com.google.gson.annotations.SerializedName("сц")  RenderPipeline alternateRingGlassPipeline;
   private final @com.google.gson.annotations.SerializedName("ся")  RenderPipeline distortionPipeline;
   private final @com.google.gson.annotations.SerializedName("Вс")  RenderPipeline distortionGlowPipeline;
   private final @com.google.gson.annotations.SerializedName("ВВ")  RenderPipeline diamondPipeline;
   private final @com.google.gson.annotations.SerializedName("ВЯ")  RenderPipeline lightningPipeline;
   private final @com.google.gson.annotations.SerializedName("ВХ")  RenderPipeline flexibleWavePipeline;
   private @com.google.gson.annotations.SerializedName("еУ")  boolean wasOnGround;
   private @com.google.gson.annotations.SerializedName("еЗ")  boolean wasJumpKeyDown;
   private @com.google.gson.annotations.SerializedName("еЕ")  boolean jumpEffectTriggered;
   @SerializedName("ХI")
   private long lastEffectSpawnMillis;
   @SerializedName("Я")
   private AbstractTexture ringTexture;
   @SerializedName("Х")
   private AbstractTexture alternateRingTexture;
   @SerializedName("2")
   private TextureTarget sharedSceneCopyTarget;
   @SerializedName("ф")
   private TextureTarget flexibleSceneCopyTarget;
   @SerializedName("у")
   private final Matrix4f inverseViewProjectionMatrix;
   private final @com.google.gson.annotations.SerializedName("Хв")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Хъ")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
   private final @com.google.gson.annotations.SerializedName("М")  int[] angularThemeColors;
   private static final String LEECH_FRAME_TEXTURE_NAMESPACE = "rose";
   private static final String MODULE_NAME = "JumpCircle";
   private static final String MODULE_DESCRIPTION = "Создаёт визуальный эффект при прыжке";
   private static final String SHAPE_LABEL = "Форма";
   private static final String SHAPE_RING_OPTION = "Круг";
   private static final String SHAPE_DIAMOND_OPTION = "Ромб";
   private static final String SHAPE_LIGHTNING_OPTION = "Молния";
   private static final String SHAPE_ALTERNATE_RING_OPTION = "Круг 2";
   private static final String SHAPE_LEECH_OPTION = "Пиявки";
   private static final String SHAPE_FLEXIBLE_OPTION = "Гибкий";
   private static final String RADIUS_LABEL = "Радиус";
   private static final float DEFAULT_RADIUS = 1.5F;
   private static final float RADIUS_STEP = 0.1F;
   private static final String THEME_COLOR_LABEL = "Авто цвет под тему";
   private static final String GLOW_COLOR_LABEL = "Цвет свечения";
   private static final int DEFAULT_GLOW_COLOR = -1661312;
   private static final String DISTORTION_LABEL = "Искажение";
   private static final String DISTORTION_STRENGTH_LABEL = "Сила искажения";
   private static final float DEFAULT_DISTORTION_STRENGTH = 18.0F;
   private static final float MIN_DISTORTION_STRENGTH = 3.0F;
   private static final float MAX_DISTORTION_STRENGTH = 40.0F;
   private static final String ALTERNATE_RING_LIFETIME_STEPS_LABEL = "Время жизни";
   private static final float DEFAULT_ALTERNATE_RING_LIFETIME_STEPS = 15.0F;
   private static final float MIN_ALTERNATE_RING_LIFETIME_STEPS = 15.0F;
   private static final float MAX_ALTERNATE_RING_LIFETIME_STEPS = 50.0F;
   private static final String ALTERNATE_RING_RADIUS_LABEL = "Радиус Круг 2";
   private static final float DEFAULT_ALTERNATE_RING_RADIUS = 1.25F;
   private static final float MIN_ALTERNATE_RING_RADIUS = 0.5F;
   private static final float MAX_ALTERNATE_RING_RADIUS = 2.5F;
   private static final float ALTERNATE_RING_RADIUS_STEP = 0.1F;
   private static final String ALTERNATE_RING_GLASS_LABEL = "Стекло";
   private static final String LEECH_ANIMATION_LABEL = "Анимация";
   private static final String LEECH_ANIMATION_FADE_OUT_ROTATE_OPTION = "FadeOutRotate";
   private static final String LEECH_ANIMATION_FADE_IN_OUT_ROTATE_OPTION = "FadeInOutRotate";
   private static final String LEECH_ANIMATION_FADE_IN_EXPONENT_OPTION = "FadeInExponent";
   private static final String LEECH_LIFETIME_MILLIS_LABEL = "Время жизни";
   private static final float DEFAULT_LEECH_LIFETIME_MILLIS = 3500.0F;
   private static final float MIN_LEECH_LIFETIME_MILLIS = 500.0F;
   private static final float MAX_LEECH_LIFETIME_MILLIS = 5000.0F;
   private static final float LEECH_LIFETIME_MILLIS_STEP = 100.0F;
   private static final String LEECH_SIZE_LABEL = "Дальность";
   private static final float MAX_LEECH_SIZE = 3.0F;
   private static final float LEECH_SIZE_STEP = 0.1F;
   private static final String LEECH_COLOR_MODE_LABEL = "Режим цвета";
   private static final String LEECH_COLOR_MODE_RAINBOW_OPTION = "Радуга";
   private static final String LEECH_COLOR_MODE_THEME_OPTION = "Клиент";
   private static final String LEECH_COLOR_MODE_CUSTOM_OPTION = "Свой";
   private static final String LEECH_COLOR_MODE_CUSTOM_FADE_OPTION = "Свой+Fade";
   private static final String LEECH_PRIMARY_COLOR_LABEL = "Цвет 1";
   private static final int DEFAULT_LEECH_PRIMARY_COLOR = -45056;
   private static final String LEECH_SECONDARY_COLOR_LABEL = "Цвет 2";
   private static final String LEECH_DEEP_LIGHT_LABEL = "Глубокий свет";
   private static final String FLEXIBLE_LIFETIME_MILLIS_LABEL = "Время";
   private static final float DEFAULT_FLEXIBLE_LIFETIME_MILLIS = 1500.0F;
   private static final float MIN_FLEXIBLE_LIFETIME_MILLIS = 500.0F;
   private static final float MAX_FLEXIBLE_LIFETIME_MILLIS = 5000.0F;
   private static final float FLEXIBLE_LIFETIME_MILLIS_STEP = 50.0F;
   private static final String FLEXIBLE_SIZE_LABEL = "Размер";
   private static final float DEFAULT_FLEXIBLE_SIZE = 3.0F;
   private static final float MIN_FLEXIBLE_SIZE = 0.5F;
   private static final float MAX_FLEXIBLE_SIZE = 3.0F;
   private static final float FLEXIBLE_SIZE_STEP = 0.1F;
   private static final String FLEXIBLE_ANIMATION_LABEL = "Тип анимации";
   private static final String FLEXIBLE_ANIMATION_ELASTIC_OPTION = "Эластичная";
   private static final String FLEXIBLE_ANIMATION_NORMAL_OPTION = "Обычная";
   private static final String FLEXIBLE_ANIMATION_BACK_OPTION = "Назад";
   private static final String FLEXIBLE_WAVE_STRENGTH_LABEL = "Сила волны";
   private static final float MIN_FLEXIBLE_WAVE_STRENGTH = 0.2F;
   private static final float MAX_FLEXIBLE_WAVE_STRENGTH = 3.0F;
   private static final float FLEXIBLE_WAVE_STRENGTH_STEP = 0.1F;
   private static final String FLEXIBLE_WAVE_THICKNESS_LABEL = "Толщина волны";
   private static final float DEFAULT_FLEXIBLE_WAVE_THICKNESS = 0.5F;
   private static final float MIN_FLEXIBLE_WAVE_THICKNESS = 0.3F;
   private static final float MAX_FLEXIBLE_WAVE_THICKNESS = 0.8F;
   private static final float FLEXIBLE_WAVE_THICKNESS_STEP = 0.05F;
   private static final String FLEXIBLE_SATURATION_LABEL = "Насыщенность";
   private static final float MIN_FLEXIBLE_SATURATION = -1.0F;
   private static final float FLEXIBLE_SATURATION_STEP = 0.05F;
   private static final String FLEXIBLE_TURBULENCE_LABEL = "Турбулентность";
   private static final String FLEXIBLE_TURBULENCE_STRENGTH_LABEL = "Сила турбулентности";
   private static final float DEFAULT_FLEXIBLE_TURBULENCE_STRENGTH = 0.5F;
   private static final float MIN_FLEXIBLE_TURBULENCE_STRENGTH = 0.1F;
   private static final float FLEXIBLE_TURBULENCE_STRENGTH_STEP = 0.1F;
   private static final String FLEXIBLE_GLOW_LABEL = "Свечение волны";
   private static final String FLEXIBLE_GLOW_HEIGHT_LABEL = "Высота свечения";
   private static final float DEFAULT_FLEXIBLE_GLOW_HEIGHT = 22.0F;
   private static final float MIN_FLEXIBLE_GLOW_HEIGHT = 15.0F;
   private static final float MAX_FLEXIBLE_GLOW_HEIGHT = 45.0F;
   private static final String FLEXIBLE_GLOW_WIDTH_LABEL = "Ширина свечения";
   private static final float DEFAULT_FLEXIBLE_GLOW_WIDTH = 18.0F;
   private static final float MIN_FLEXIBLE_GLOW_WIDTH = 10.0F;
   private static final float MAX_FLEXIBLE_GLOW_WIDTH = 30.0F;
   private static final String FLEXIBLE_GLOW_COLOR_STRENGTH_LABEL = "Сила цвета свечения";
   private static final float DEFAULT_FLEXIBLE_GLOW_COLOR_STRENGTH = 70.0F;
   private static final float MIN_FLEXIBLE_GLOW_COLOR_STRENGTH = 30.0F;
   private static final float MAX_FLEXIBLE_GLOW_COLOR_STRENGTH = 100.0F;
   private static final String FLEXIBLE_GLOW_OPACITY_LABEL = "Прозрачность свечения";
   private static final float DEFAULT_FLEXIBLE_GLOW_OPACITY = 60.0F;
   private static final float MIN_FLEXIBLE_GLOW_OPACITY = 20.0F;
   private static final float MAX_FLEXIBLE_GLOW_OPACITY = 80.0F;
   private static final String FLEXIBLE_COLOR_STRENGTH_LABEL = "Сила цвета";
   private static final float DEFAULT_FLEXIBLE_COLOR_STRENGTH = 55.0F;
   private static final float MAX_FLEXIBLE_COLOR_STRENGTH = 100.0F;
   private static final String FLEXIBLE_WAVE_COLOR_MODE_LABEL = "Режим цвета волны";
   private static final String FLEXIBLE_WAVE_COLOR_MODE_RAINBOW_OPTION = "Радуга";
   private static final String FLEXIBLE_WAVE_COLOR_MODE_THEME_OPTION = "Клиент";
   private static final String FLEXIBLE_WAVE_COLOR_MODE_CUSTOM_OPTION = "Свой";
   private static final String FLEXIBLE_SECOND_COLOR_ENABLED_LABEL = "Второй цвет";
   private static final String FLEXIBLE_PRIMARY_COLOR_LABEL = "Цвет волны";
   private static final String FLEXIBLE_SECONDARY_COLOR_LABEL = "Цвет волны 2";
   private static final int DEFAULT_FLEXIBLE_SECONDARY_COLOR = -1661312;
   private static final String RING_PIPELINE_LOCATION = "rose/jump_circle";
   private static final String RING_PIPELINE_VERTEX_SHADER = "dead_chams";
   private static final String RING_PIPELINE_FRAGMENT_SHADER = "dead_chams";
   private static final String RING_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String ALTERNATE_RING_PIPELINE_LOCATION = "rose/jump_circle_zenith";
   private static final String ALTERNATE_RING_PIPELINE_VERTEX_SHADER = "dead_chams";
   private static final String ALTERNATE_RING_PIPELINE_FRAGMENT_SHADER = "dead_chams";
   private static final String ALTERNATE_RING_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String ALTERNATE_RING_GLASS_PIPELINE_LOCATION = "rose/jump_circle_zenith_glass";
   private static final String ALTERNATE_RING_GLASS_PIPELINE_GLOBALS_UNIFORM_LAYOUT_NAME = "Globals";
   private static final String ALTERNATE_RING_GLASS_PIPELINE_VERTEX_SHADER = "jump_frost";
   private static final String ALTERNATE_RING_GLASS_PIPELINE_FRAGMENT_SHADER = "jump_frost";
   private static final String ALTERNATE_RING_GLASS_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String DISTORTION_PIPELINE_LOCATION = "rose/jump_circle_distortion";
   private static final String DISTORTION_PIPELINE_VERTEX_SHADER = "jump_circle";
   private static final String DISTORTION_PIPELINE_FRAGMENT_SHADER = "jump_circle_distortion";
   private static final String DISTORTION_PIPELINE_DISTORTION_UNIFORM_LAYOUT_NAME = "JumpDistortion";
   private static final String DISTORTION_PIPELINE_SCENE_SAMPLER_LAYOUT_NAME = "SceneSampler";
   private static final String DISTORTION_GLOW_PIPELINE_LOCATION = "rose/jump_circle_distortion_glow";
   private static final String DISTORTION_GLOW_PIPELINE_VERTEX_SHADER = "jump_circle";
   private static final String DISTORTION_GLOW_PIPELINE_FRAGMENT_SHADER = "jump_circle_glow";
   private static final String DIAMOND_PIPELINE_LOCATION = "rose/jump_diamond";
   private static final String DIAMOND_PIPELINE_VERTEX_SHADER = "jump_circle";
   private static final String DIAMOND_PIPELINE_FRAGMENT_SHADER = "jump_diamond";
   private static final String LIGHTNING_PIPELINE_LOCATION = "rose/jump_lightning";
   private static final String LIGHTNING_PIPELINE_VERTEX_SHADER = "jump_circle";
   private static final String LIGHTNING_PIPELINE_FRAGMENT_SHADER = "jump_lightning";
   private static final String FLEXIBLE_WAVE_PIPELINE_LOCATION = "rose/jump_flex";
   private static final String FLEXIBLE_WAVE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String FLEXIBLE_WAVE_PIPELINE_FRAGMENT_SHADER = "jump_flex";
   private static final String FLEXIBLE_WAVE_PIPELINE_FLEXIBLE_UNIFORM_LAYOUT_NAME = "JumpFlex";
   private static final String FLEXIBLE_WAVE_PIPELINE_SCENE_SAMPLER_LAYOUT_NAME = "SceneSampler";
   private static final String FLEXIBLE_WAVE_PIPELINE_DEPTH_SAMPLER_LAYOUT_NAME = "DepthSampler";
   private static final String RENDER_LIGHTNING_SHAPE_KEY = "Молния";
   private static final String RENDER_DIAMOND_SHAPE_KEY = "Ромб";
   private static final String RENDER_ALTERNATE_RING_SHAPE_KEY = "Круг 2";
   private static final String RENDER_LEECH_SHAPE_KEY = "Пиявки";
   private static final String RENDER_FLEXIBLE_SHAPE_KEY = "Гибкий";
   private static final String LEECH_FADE_IN_OUT_ROTATE_ANIMATION_KEY = "FadeInOutRotate";
   private static final String LEECH_FADE_IN_EXPONENT_ANIMATION_KEY = "FadeInExponent";
   private static final float LEECH_FADE_IN_OUT_ALPHA_SPLIT = 0.5F;
   private static final float LEECH_FADE_IN_OUT_SIZE_SPLIT = 0.5F;
   private static final double LEECH_FADE_IN_OUT_ROTATION_DEGREES = 90.0;
   private static final float LEECH_EXPONENT_BACK_OVERSHOOT = 1.70158F;
   private static final double LEECH_EXPONENT_CUBIC_POWER = 3.0;
   private static final double LEECH_EXPONENT_QUADRATIC_POWER = 2.0;
   private static final float LEECH_FADE_OUT_ALPHA_SPLIT_PROGRESS = 0.5F;
   private static final double LEECH_FADE_OUT_ROTATION_DEGREES = -90.0;
   private static final float LEECH_MIN_VISIBLE_ALPHA = 0.003921569F;
   private static final float LEECH_MIN_VISIBLE_SIZE = 0.02F;
   private static final float LEECH_FRAME_PHASE_OFFSET = 0.6F;
   private static final float LEECH_FRAME_PHASE_SCALE = 199.5F;
   private static final float LEECH_LAST_FRAME_FLOAT_INDEX = 199.0F;
   private static final float LEECH_DEEP_LIGHT_RGB_SCALE = 0.15F;
   private static final float LEECH_DEEP_LIGHT_LAYER_SCALE = 40.0F;
   private static final float LEECH_DEEP_LIGHT_HEIGHT_DIVISOR = 4.0F;
   private static final float LEECH_DEEP_LIGHT_SIZE_DIVISOR = 12.0F;
   private static final float LEECH_DEEP_LIGHT_RGB_BYTE_SCALE = 255.0F;
   private static final float LEECH_DEEP_LIGHT_RADIUS_PHASE_OFFSET = 1.5F;
   private static final float LEECH_DEEP_LIGHT_HALF_SIZE_FACTOR = 0.5F;
   private static final float LEECH_RING_HALF_SIZE_FACTOR = 0.5F;
   private static final float LEECH_RING_SURFACE_OFFSET = 0.001F;
   private static final String TEXTURED_MESH_TEXTURE_SAMPLER_BINDING = "Sampler0";
   private static final String LEECH_THEME_COLOR_KEY = "Клиент";
   private static final String LEECH_CUSTOM_COLOR_KEY = "Свой";
   private static final String LEECH_CUSTOM_FADE_COLOR_KEY = "Свой+Fade";
   private static final float LEECH_PRIMARY_ALPHA_BYTE_SCALE = 255.0F;
   private static final float LEECH_FADE_PRIMARY_ALPHA_BYTE_SCALE = 255.0F;
   private static final float LEECH_FADE_SECONDARY_ALPHA_BYTE_SCALE = 255.0F;
   private static final float LEECH_COLOR_PHASE_SCALE = 0.08F;
   private static final float LEECH_COLOR_PHASE_TIME_MILLIS = 300.0F;
   private static final float LEECH_COLOR_MIX_NORMALIZATION = 0.5F;
   private static final float LEECH_RAINBOW_TIME_STEP_MILLIS = 8.0F;
   private static final float LEECH_RAINBOW_PHASE_WRAP = 360.0F;
   private static final float LEECH_RAINBOW_HUE_DIVISOR = 360.0F;
   private static final float LEECH_RAINBOW_SATURATION = 0.65F;
   private static final int LEECH_RAINBOW_OPAQUE_MASK = -16777216;
   private static final float LEECH_HIGHLIGHT_ALPHA_BYTE_SCALE = 255.0F;
   private static final float LEECH_WHITE_HIGHLIGHT_MIX = 0.125F;
   private static final float LEECH_DEEP_LIGHT_SURFACE_OFFSET = 0.001F;
   private static final float TRIANGLE_ENVELOPE_MIDPOINT = 0.5F;
   private static final float QUADRATIC_IN_OUT_MIDPOINT = 0.5F;
   private static final float QUADRATIC_IN_OUT_TAIL_SLOPE = -2.0F;
   private static final double QUADRATIC_IN_OUT_TAIL_POWER = 2.0;
   private static final double CIRCULAR_OUT_POWER = 2.0;
   private static final float ELASTIC_IN_OUT_PERIOD_RADIANS = (float) (Math.PI * 4.0 / 9.0);
   private static final float ELASTIC_IN_OUT_MIDPOINT = 0.5F;
   private static final double ELASTIC_IN_OUT_LEADING_EXPONENT_BASE = 2.0;
   private static final float ELASTIC_IN_OUT_LEADING_EXPONENT_SCALE = 20.0F;
   private static final float ELASTIC_IN_OUT_LEADING_EXPONENT_OFFSET = 10.0F;
   private static final float ELASTIC_IN_OUT_LEADING_PHASE_SCALE = 20.0F;
   private static final double ELASTIC_IN_OUT_LEADING_PHASE_OFFSET = 11.125;
   private static final double ELASTIC_IN_OUT_LEADING_AMPLITUDE_DIVISOR = 2.0;
   private static final double ELASTIC_IN_OUT_TRAILING_EXPONENT_BASE = 2.0;
   private static final float ELASTIC_IN_OUT_TRAILING_EXPONENT_SCALE = -20.0F;
   private static final float ELASTIC_IN_OUT_TRAILING_EXPONENT_OFFSET = 10.0F;
   private static final float ELASTIC_IN_OUT_TRAILING_PHASE_SCALE = 20.0F;
   private static final double ELASTIC_IN_OUT_TRAILING_PHASE_OFFSET = 11.125;
   private static final double ELASTIC_IN_OUT_TRAILING_AMPLITUDE_DIVISOR = 2.0;
   private static final float EXPONENTIAL_IN_OUT_MIDPOINT = 0.5F;
   private static final double EXPONENTIAL_IN_OUT_LEADING_EXPONENT_BASE = 2.0;
   private static final float EXPONENTIAL_IN_OUT_LEADING_EXPONENT_SCALE = 20.0F;
   private static final float EXPONENTIAL_IN_OUT_LEADING_EXPONENT_OFFSET = 10.0F;
   private static final double EXPONENTIAL_IN_OUT_LEADING_AMPLITUDE_DIVISOR = 2.0;
   private static final double EXPONENTIAL_IN_OUT_TRAILING_VALUE_OFFSET = 2.0;
   private static final double EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_BASE = 2.0;
   private static final float EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_SCALE = -20.0F;
   private static final float EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_OFFSET = 10.0F;
   private static final double EXPONENTIAL_IN_OUT_TRAILING_AMPLITUDE_DIVISOR = 2.0;
   private static final float ELASTIC_OUT_PERIOD_RADIANS = (float) (Math.PI * 2.0 / 3.0);
   private static final double ELASTIC_OUT_EXPONENT_BASE = 2.0;
   private static final float ELASTIC_OUT_EXPONENT_SCALE = -10.0F;
   private static final float ELASTIC_OUT_PHASE_SCALE = 10.0F;
   private static final double ELASTIC_OUT_PHASE_OFFSET = 0.75;
   private static final float BACK_OUT_OVERSHOOT = 1.70158F;
   private static final double BACK_OUT_CUBIC_POWER = 3.0;
   private static final double BACK_OUT_QUADRATIC_POWER = 2.0;
   private static final float RING_ROTATION_DEGREES_PER_TICK = 2.5F;
   private static final float RING_LIFETIME_MILLIS = 1250.0F;
   private static final float RING_FADE_IN_PROGRESS_SPAN = 0.34F;
   private static final float RING_FADE_OUT_START_PROGRESS = 0.66F;
   private static final float RING_FADE_OUT_PROGRESS_SPAN = 0.34F;
   private static final float RING_RADIUS_GROWTH_PROGRESS_SPAN = 0.4F;
   private static final float RING_PRIMARY_PULSE_PHASE_RADIANS = (float) Math.PI;
   private static final float RING_PRIMARY_PULSE_RADIUS_SCALE = 0.035F;
   private static final float RING_SECONDARY_PULSE_START_PROGRESS = 0.28F;
   private static final float RING_SECONDARY_PULSE_PROGRESS_SPAN = 0.38F;
   private static final float RING_SECONDARY_PULSE_PHASE_RADIANS = (float) Math.PI;
   private static final float RING_SECONDARY_PULSE_RADIUS_SCALE = 0.012F;
   private static final float RING_INITIAL_RADIUS_SCALE = 0.14F;
   private static final float RING_RADIUS_GROWTH_SCALE = 0.86F;
   private static final float RING_FADE_OUT_RADIUS_SCALE = 0.15F;
   private static final float RING_SETTING_RADIUS_SCALE = 0.55F;
   private static final float DISTORTION_QUAD_RADIUS_SCALE = 1.4F;
   private static final float RING_PROGRESS_ROTATION_DEGREES = 72.0F;
   private static final float RING_DELAY_ROTATION_DEGREES_PER_MILLI = 0.06F;
   private static final long DISTORTION_TIME_WRAP_MILLIS = 1000000L;
   private static final float DISTORTION_MILLIS_PER_SECOND = 1000.0F;
   private static final float DISTORTION_MOUSE_X_SCALE = 1.5F;
   private static final float DISTORTION_MOUSE_Y_SCALE = 1.5F;
   private static final String DISTORTION_SCENE_SAMPLER_BINDING = "SceneSampler";
   private static final String DISTORTION_UNIFORM_BINDING = "JumpDistortion";
   private static final String RING_TEXTURE_SAMPLER_BINDING = "Sampler0";
   private static final String SHARED_SCENE_TARGET_LABEL = "jump-circle-distortion-scene";
   private static final float DIAMOND_ROTATION_DEGREES_PER_TICK = 2.5F;
   private static final float DIAMOND_LIFETIME_MILLIS = 1250.0F;
   private static final float DIAMOND_FADE_IN_PROGRESS_SPAN = 0.34F;
   private static final float DIAMOND_FADE_OUT_START_PROGRESS = 0.66F;
   private static final float DIAMOND_FADE_OUT_PROGRESS_SPAN = 0.34F;
   private static final float DIAMOND_RADIUS_GROWTH_PROGRESS_SPAN = 0.4F;
   private static final float DIAMOND_PRIMARY_PULSE_PHASE_RADIANS = (float) Math.PI;
   private static final float DIAMOND_PRIMARY_PULSE_RADIUS_SCALE = 0.035F;
   private static final float DIAMOND_SECONDARY_PULSE_START_PROGRESS = 0.28F;
   private static final float DIAMOND_SECONDARY_PULSE_PROGRESS_SPAN = 0.38F;
   private static final float DIAMOND_SECONDARY_PULSE_PHASE_RADIANS = (float) Math.PI;
   private static final float DIAMOND_SECONDARY_PULSE_RADIUS_SCALE = 0.012F;
   private static final float DIAMOND_INITIAL_RADIUS_SCALE = 0.14F;
   private static final float DIAMOND_RADIUS_GROWTH_SCALE = 0.86F;
   private static final float DIAMOND_FADE_OUT_RADIUS_SCALE = 0.15F;
   private static final float DIAMOND_SETTING_RADIUS_SCALE = 0.55F;
   private static final float DIAMOND_PROGRESS_ROTATION_DEGREES = 72.0F;
   private static final float DIAMOND_DELAY_ROTATION_DEGREES_PER_MILLI = 0.06F;
   private static final long LIGHTNING_TIME_WRAP_MILLIS = 100000L;
   private static final float LIGHTNING_MILLIS_PER_SECOND = 1000.0F;
   private static final float LIGHTNING_LIFETIME_MILLIS = 1550.0F;
   private static final float LIGHTNING_REVEAL_PROGRESS_SPAN = 0.4F;
   private static final float LIGHTNING_FLASH_PROGRESS_SPAN = 0.05F;
   private static final float LIGHTNING_FADE_OUT_START_PROGRESS = 0.58F;
   private static final float LIGHTNING_FADE_OUT_PROGRESS_SPAN = 0.42000002F;
   private static final float LIGHTNING_FLICKER_BASE = 0.85F;
   private static final float LIGHTNING_FLICKER_AMPLITUDE = 0.15F;
   private static final float LIGHTNING_FLICKER_RADIANS_PER_SECOND = 42.0F;
   private static final float LIGHTNING_MIN_VISIBLE_ALPHA = 0.01F;
   private static final float LIGHTNING_MIN_REVEAL_PROGRESS = 0.001F;
   private static final float LIGHTNING_MIN_BRANCH_REVEAL_PROGRESS = 0.001F;
   private static final float LIGHTNING_SEGMENT_START_WIDTH_SCALE_AT_ORIGIN = 0.26F;
   private static final float LIGHTNING_SEGMENT_START_WIDTH_SCALE_AT_TIP = 0.03F;
   private static final float LIGHTNING_SEGMENT_END_WIDTH_SCALE_AT_ORIGIN = 0.26F;
   private static final float LIGHTNING_SEGMENT_END_WIDTH_SCALE_AT_TIP = 0.03F;
   private static final float LIGHTNING_SEGMENT_MIDPOINT_SCALE = 0.5F;
   private static final float LIGHTNING_BRANCH_TIP_ALPHA_SCALE = 0.45F;
   private static final float LIGHTNING_PARTIAL_SEGMENT_ALPHA_SCALE = 1.9F;
   private static final float LIGHTNING_FLASH_ALPHA_SCALE = 1.6F;
   private static final float LIGHTNING_FULL_SEGMENT_TINT_PROGRESS_SCALE = 1.6F;
   private static final float LIGHTNING_MIN_START_WIDTH = 0.001F;
   private static final float LIGHTNING_MIN_END_WIDTH = 0.001F;
   private static final double LIGHTNING_MIN_SEGMENT_LENGTH = 1.0E-4;
   private static final double LIGHTNING_SURFACE_OFFSET = 0.03;
   private static final double LIGHTNING_START_POSITIVE_X_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_START_POSITIVE_Z_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_START_NEGATIVE_X_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_START_NEGATIVE_Z_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_END_POSITIVE_X_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_END_POSITIVE_Z_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_END_NEGATIVE_X_HALF_WIDTH = 0.5;
   private static final double LIGHTNING_END_NEGATIVE_Z_HALF_WIDTH = 0.5;
   private static final double MIN_JUMP_UPWARD_VELOCITY = 0.01;
   private static final long EFFECT_SPAWN_COOLDOWN_MILLIS = 80L;
   private static final double EFFECT_SPAWN_SURFACE_OFFSET = 0.001;
   private static final String SPAWN_LIGHTNING_SHAPE_KEY = "Молния";
   private static final String SPAWN_LEECH_SHAPE_KEY = "Пиявки";
   private static final String SPAWN_ALTERNATE_RING_SHAPE_KEY = "Круг 2";
   private static final String SPAWN_FLEXIBLE_SHAPE_KEY = "Гибкий";
   private static final float SURFACE_GRID_RADIUS_MARGIN = 0.5F;
   private static final double SURFACE_GRID_DIAMETER_FACTOR = 2.0;
   private static final double SURFACE_GRID_SAMPLES_PER_BLOCK = 8.0;
   private static final long ALTERNATE_RING_LIFETIME_STEP_MILLIS = 100L;
   private static final float SMOOTHSTEP_CUBIC_COEFFICIENT = 3.0F;
   private static final int RGB_MASK = 16777215;
   private static final float ELEVATED_SAMPLE_HEIGHT_THRESHOLD = 0.2F;
   private static final double OUTLINE_LOCAL_X_MAX = 0.999;
   private static final double OUTLINE_LOCAL_Z_MAX = 0.999;
   private static final double OUTLINE_MAX_HEIGHT_ADJUSTMENT = 0.3;
   private static final double COLLISION_UPPER_REFERENCE_OFFSET = 2.8F;
   private static final double COLLISION_UPPER_PROBE_MARGIN = 0.6;
   private static final double COLLISION_PRIMARY_RAY_DEPTH = 4.0;
   private static final double COLLISION_RETRY_RAY_DEPTH = 4.0;
   private static final double COLLISION_INSIDE_BLOCK_HEIGHT_EPSILON = 0.05;
   private static final double COLLISION_INSIDE_BLOCK_POSITION_OFFSET = 0.5;
   private static final double COLLISION_BLOCKED_REFERENCE_OFFSET = 2.8F;
   private static final double COLLISION_BLOCKED_HEIGHT_SENTINEL_OFFSET = 4.0;
   private static final double COLLISION_FLOATING_HIT_HEIGHT_THRESHOLD = 0.6;
   private static final double COLLISION_RETRY_START_MARGIN = 0.05;
   private static final double WALL_SCAN_RADIUS_MARGIN = 0.2;
   private static final double WALL_SURFACE_SAMPLE_X_OFFSET = 0.5;
   private static final double WALL_SURFACE_SAMPLE_Z_OFFSET = 0.5;
   private static final double WALL_MAX_SURFACE_HEIGHT = 5.8F;
   private static final double WALL_FACE_CENTER_X_OFFSET = 0.5;
   private static final double WALL_FACE_CENTER_X_DIRECTION_SCALE = 0.5;
   private static final double WALL_FACE_CENTER_Z_OFFSET = 0.5;
   private static final double WALL_FACE_CENTER_Z_DIRECTION_SCALE = 0.5;
   private static final double WALL_Z_SURFACE_BIAS = 0.02;
   private static final double WALL_X_SURFACE_BIAS = 0.02;
   private static final float ALTERNATE_RING_GRID_RADIUS_MARGIN = 0.5F;
   private static final double ALTERNATE_RING_GRID_DIAMETER_FACTOR = 2.0;
   private static final float ALTERNATE_RING_ELEVATED_SAMPLE_MIN_HEIGHT = -2.55F;
   private static final float ALTERNATE_RING_BLOCKED_SAMPLE_HEIGHT = 5.8F;
   private static final double ALTERNATE_RING_MIN_SAMPLE_DEPTH = 2.55F;
   private static final double ALTERNATE_RING_MAX_SAMPLE_HEIGHT = 2.8F;
   private static final float ALTERNATE_RING_GLOW_SURFACE_OFFSET = 0.04F;
   private static final float ALTERNATE_RING_GLOW_MIN_RADIUS = 1.0E-4F;
   private static final float ALTERNATE_RING_GLOW_RADIAL_X_EPSILON = 1.0E-4F;
   private static final float ALTERNATE_RING_GLOW_RADIAL_Z_EPSILON = 1.0E-4F;
   private static final float ALTERNATE_RING_GLOW_U_CENTER = 0.5F;
   private static final float ALTERNATE_RING_GLOW_U_RADIUS_SCALE = 0.5F;
   private static final float ALTERNATE_RING_GLOW_V_CENTER = 0.5F;
   private static final float ALTERNATE_RING_GLOW_V_RADIUS_SCALE = 0.5F;
   private static final float ALTERNATE_RING_GLOW_HEIGHT_FADE_START = 1.05F;
   private static final float ALTERNATE_RING_GLOW_HEIGHT_FADE_END = 1.9F;
   private static final float ALTERNATE_RING_GLOW_RADIAL_BASE = 0.5F;
   private static final float ALTERNATE_RING_GLOW_RADIAL_SCALE = 0.5F;
   private static final float ALTERNATE_RING_GLOW_RADIUS_FADE_START = 0.72F;
   private static final float ALTERNATE_RING_GLOW_FULL_TURN_RADIANS = (float) (Math.PI * 2);
   private static final float ALTERNATE_RING_GLOW_ANGULAR_COLOR_COUNT = 48.0F;
   private static final float ALTERNATE_RING_GLOW_INITIAL_UNBLOCKED_MAX_HEIGHT = -Float.MAX_VALUE;
   private static final float ALTERNATE_RING_GLOW_SUBDIVISION_HEIGHT_STEP = 0.35F;
   private static final float ALTERNATE_RING_SURFACE_INITIAL_UNBLOCKED_MAX_HEIGHT = -Float.MAX_VALUE;
   private static final float ALTERNATE_RING_SURFACE_SUBDIVISION_HEIGHT_STEP = 0.35F;
   private static final double TURBULENCE_WAVE0_X_FREQUENCY = 2.3;
   private static final double TURBULENCE_WAVE0_Z_FREQUENCY = 1.7;
   private static final double TURBULENCE_WAVE0_Y_FREQUENCY = 1.1;
   private static final float TURBULENCE_WAVE0_TIME_FREQUENCY = 0.9F;
   private static final double TURBULENCE_WAVE1_X_FREQUENCY = 1.3;
   private static final double TURBULENCE_WAVE1_Z_FREQUENCY = 2.9;
   private static final double TURBULENCE_WAVE1_Y_FREQUENCY = 2.6;
   private static final float TURBULENCE_WAVE1_TIME_FREQUENCY = 1.3F;
   private static final double TURBULENCE_WAVE2_X_FREQUENCY = 3.7;
   private static final double TURBULENCE_WAVE2_Z_FREQUENCY = 0.9;
   private static final double TURBULENCE_WAVE2_Y_FREQUENCY = 1.8;
   private static final float TURBULENCE_WAVE2_TIME_FREQUENCY = 0.6F;
   private static final float TURBULENCE_WAVE_COUNT = 3.0F;
   private static final float WALL_GLOW_HEIGHT_TURBULENCE_SCALE = 0.45F;
   private static final float WALL_GLOW_RADIUS_TURBULENCE_SCALE = 0.12F;
   private static final float WALL_GLOW_MAX_HEIGHT = 2.6F;
   private static final float WALL_GLOW_REMAINING_RADIUS_HEIGHT_SCALE = 1.3F;
   private static final float WALL_GLOW_MIN_HEIGHT = 0.05F;
   private static final float WALL_GLOW_HEIGHT_FADE_START_SCALE = 0.55F;
   private static final float WALL_GLOW_INTENSITY = 0.8F;
   private static final float WALL_GLOW_HEIGHT_FADE_IN_END = 0.3F;
   private static final float WALL_GLOW_RADIUS_FADE_OUT_START = 0.85F;
   private static final float WALL_GLOW_RADIUS_FADE_OUT_END = 1.05F;
   private static final float WALL_GLOW_NOISE_BASE = 0.82F;
   private static final float WALL_GLOW_NOISE_AMPLITUDE = 0.18F;
   private static final double WALL_GLOW_NOISE_X_FREQUENCY = 1.9;
   private static final double WALL_GLOW_NOISE_X_PHASE = 7.3;
   private static final double WALL_GLOW_NOISE_Y_FREQUENCY = 1.6;
   private static final double WALL_GLOW_NOISE_Z_FREQUENCY = 2.1;
   private static final double WALL_GLOW_NOISE_Z_PHASE = 3.1;
   private static final float WALL_GLOW_NOISE_TIME_FREQUENCY = 1.4F;
   private static final float WALL_GLOW_RADIAL_X_EPSILON = 1.0E-4F;
   private static final float WALL_GLOW_RADIAL_Z_EPSILON = 1.0E-4F;
   private static final float WALL_GLOW_U_CENTER = 0.5F;
   private static final float WALL_GLOW_U_RADIUS_SCALE = 0.5F;
   private static final float WALL_GLOW_V_CENTER = 0.5F;
   private static final float WALL_GLOW_V_RADIUS_SCALE = 0.5F;
   private static final float WALL_GLOW_FULL_TURN_RADIANS = (float) (Math.PI * 2);
   private static final float WALL_GLOW_ANGULAR_COLOR_COUNT = 48.0F;
   private static final double WALL_GLOW_RADIUS_CULL_SQUARED_SCALE = 1.1;
   private static final float WALL_GLOW_HORIZONTAL_START_SUBDIVISIONS = 4.0F;
   private static final float WALL_GLOW_HORIZONTAL_END_SUBDIVISIONS = 4.0F;
   private static final float WALL_GLOW_VERTICAL_START_SUBDIVISIONS = 6.0F;
   private static final float WALL_GLOW_VERTICAL_END_SUBDIVISIONS = 6.0F;
   private static final float ALTERNATE_RING_THEME_PHASE_DEGREES = 360.0F;
   private static final float ALTERNATE_RING_THEME_COLOR_COUNT = 48.0F;
   private static final double ALTERNATE_RING_ALPHA_ENVELOPE_RADIANS = Math.PI;
   private static final float ALTERNATE_RING_MIN_VISIBLE_RADIUS = 0.001F;
   private static final float ALTERNATE_RING_MIN_VISIBLE_ALPHA = 0.001F;
   private static final float ALTERNATE_RING_MILLIS_PER_SECOND = 1000.0F;
   private static final String ALTERNATE_RING_GLASS_SCENE_SAMPLER_BINDING = "Sampler0";
   private static final String ALTERNATE_RING_TEXTURE_SAMPLER_BINDING = "Sampler0";
   private static final float SMOOTHERSTEP_QUINTIC_COEFFICIENT = 6.0F;
   private static final float SMOOTHERSTEP_QUARTIC_COEFFICIENT = 15.0F;
   private static final float SMOOTHERSTEP_CUBIC_COEFFICIENT = 10.0F;
   private static final float HORIZONTAL_RING_SURFACE_OFFSET = 0.04F;
   private static final float FLEXIBLE_WAVE_BASE_STRENGTH = 0.018F;
   private static final float FLEXIBLE_WAVE_FADE_IN_PROGRESS_SPAN = 0.1F;
   private static final float FLEXIBLE_WAVE_FADE_OUT_PROGRESS_SPAN = 0.3F;
   private static final float FLEXIBLE_WAVE_RADIUS_SCALE = 0.75F;
   private static final float FLEXIBLE_WAVE_MIN_THICKNESS = 0.15F;
   private static final float FLEXIBLE_RING_COLOR_PHASE_STEP = 45.0F;
   private static final long FLEXIBLE_TIME_WRAP_MILLIS = 100000L;
   private static final float FLEXIBLE_MILLIS_PER_SECOND = 1000.0F;
   private static final float FLEXIBLE_TURBULENCE_STRENGTH_SCALE = 0.01F;
   private static final float FLEXIBLE_COLOR_STRENGTH_PERCENT_SCALE = 100.0F;
   private static final float FLEXIBLE_GLOW_INTENSITY = 0.6F;
   private static final float FLEXIBLE_GLOW_HEIGHT_PERCENT_SCALE = 100.0F;
   private static final float FLEXIBLE_MIN_GLOW_HEIGHT_SCALE = 0.05F;
   private static final float FLEXIBLE_MAX_GLOW_HEIGHT_SCALE = 5.0F;
   private static final float FLEXIBLE_GLOW_WIDTH_PERCENT_SCALE = 100.0F;
   private static final float FLEXIBLE_MIN_GLOW_WIDTH_SCALE = 0.05F;
   private static final float FLEXIBLE_MAX_GLOW_WIDTH_SCALE = 5.0F;
   private static final float FLEXIBLE_GLOW_COLOR_STRENGTH_PERCENT_SCALE = 100.0F;
   private static final float FLEXIBLE_GLOW_OPACITY_PERCENT_SCALE = 100.0F;
   private static final String FLEXIBLE_SCENE_SAMPLER_BINDING = "SceneSampler";
   private static final String FLEXIBLE_DEPTH_SAMPLER_BINDING = "DepthSampler";
   private static final String FLEXIBLE_UNIFORM_BINDING = "JumpFlex";
   private static final String FLEXIBLE_BACK_ANIMATION_KEY = "Назад";
   private static final String FLEXIBLE_NORMAL_ANIMATION_KEY = "Обычная";
   private static final String FLEXIBLE_RAINBOW_COLOR_KEY = "Радуга";
   private static final float FLEXIBLE_RAINBOW_TIME_STEP_MILLIS = 8.0F;
   private static final float FLEXIBLE_RAINBOW_PHASE_WRAP = 360.0F;
   private static final float FLEXIBLE_RAINBOW_HUE_DIVISOR = 360.0F;
   private static final int FLEXIBLE_RAINBOW_OPAQUE_MASK = -16777216;
   private static final String FLEXIBLE_THEME_COLOR_KEY = "Клиент";
   private static final String FLEXIBLE_CUSTOM_COLOR_KEY = "Свой";
   private static final int FLEXIBLE_PRIMARY_COLOR_OPAQUE_MASK = -16777216;
   private static final int FLEXIBLE_SECONDARY_COLOR_OPAQUE_MASK = -16777216;
   private static final long FLEXIBLE_CUSTOM_COLOR_TIME_STEP_MILLIS = 8L;
   private static final long FLEXIBLE_CUSTOM_COLOR_PHASE_WRAP = 360L;
   private static final float FLEXIBLE_CUSTOM_COLOR_MIX_DIVISOR = 180.0F;
   private static final String FLEXIBLE_SINGLE_CUSTOM_COLOR_KEY = "Свой";
   private static final String FLEXIBLE_SCENE_TARGET_LABEL = "jump-flex-scene";
   private static final float UNIFORM_RED_BYTE_SCALE = 255.0F;
   private static final float UNIFORM_GREEN_BYTE_SCALE = 255.0F;
   private static final float UNIFORM_BLUE_BYTE_SCALE = 255.0F;
   private static final double MAX_FINITE_WORLD_COORDINATE = 3.0E7;
   private static final String FLEXIBLE_WAVE_PASS_LABEL = "jumpFlexWave";
   private static final String FLEXIBLE_UNIFORM_BUFFER_LABEL = "jumpFlexWaveUniforms";
   private static final String ALTERNATE_RING_PASS_LABEL = "jumpCircle2";
   private static final String ALTERNATE_RING_GLASS_PASS_LABEL = "jumpCircleZenithGlass";
   private static final String ALTERNATE_RING_VERTEX_BUFFER_LABEL = "jumpCircle2";
   private static final String PROCEDURAL_EFFECT_PASS_LABEL = "jumpShape";
   private static final String PROCEDURAL_EFFECT_VERTEX_BUFFER_LABEL = "jumpShapeVertices";
   private static final String RING_PASS_LABEL = "jumpCircle";
   private static final String DISTORTION_GLOW_PASS_LABEL = "jumpCircleDistortionGlow";
   private static final String DISTORTION_PASS_LABEL = "jumpCircleDistortion";
   private static final String DISTORTION_UNIFORM_BUFFER_LABEL = "jumpCircleDistortionUniforms";
   private static final String RING_VERTEX_BUFFER_LABEL = "jumpCircleVertices";
   private static final String TEXTURED_EFFECT_PASS_LABEL = "jumpLeeches";
   private static final String TEXTURED_EFFECT_VERTEX_BUFFER_LABEL = "jumpLeeches";
   private static final String FLEXIBLE_SECONDARY_COLOR_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_SECONDARY_COLOR_CUSTOM_VISIBILITY_KEY = "Свой";
   private static final String FLEXIBLE_PRIMARY_COLOR_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_PRIMARY_COLOR_CUSTOM_VISIBILITY_KEY = "Свой";
   private static final String FLEXIBLE_SECOND_COLOR_ENABLED_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_SECOND_COLOR_ENABLED_CUSTOM_VISIBILITY_KEY = "Свой";
   private static final String FLEXIBLE_WAVE_COLOR_MODE_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_COLOR_STRENGTH_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_GLOW_OPACITY_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_GLOW_COLOR_STRENGTH_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_GLOW_WIDTH_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_GLOW_HEIGHT_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_GLOW_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_TURBULENCE_STRENGTH_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_TURBULENCE_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_SATURATION_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_WAVE_THICKNESS_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_WAVE_STRENGTH_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_ANIMATION_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_SIZE_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String FLEXIBLE_LIFETIME_MILLIS_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String LEECH_DEEP_LIGHT_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_SECONDARY_COLOR_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_SECONDARY_COLOR_CUSTOM_FADE_VISIBILITY_KEY = "Свой+Fade";
   private static final String LEECH_PRIMARY_COLOR_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_PRIMARY_COLOR_CUSTOM_VISIBILITY_KEY = "Свой";
   private static final String LEECH_PRIMARY_COLOR_CUSTOM_FADE_VISIBILITY_KEY = "Свой+Fade";
   private static final String LEECH_COLOR_MODE_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_SIZE_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_LIFETIME_MILLIS_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String LEECH_ANIMATION_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String ALTERNATE_RING_GLASS_ALTERNATE_RING_VISIBILITY_KEY = "Круг 2";
   private static final String ALTERNATE_RING_RADIUS_ALTERNATE_RING_VISIBILITY_KEY = "Круг 2";
   private static final String ALTERNATE_RING_LIFETIME_STEPS_ALTERNATE_RING_VISIBILITY_KEY = "Круг 2";
   private static final String DISTORTION_STRENGTH_RING_VISIBILITY_KEY = "Круг";
   private static final String DISTORTION_RING_VISIBILITY_KEY = "Круг";
   private static final String GLOW_COLOR_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String GLOW_COLOR_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String THEME_COLOR_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String THEME_COLOR_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String RADIUS_LEECH_VISIBILITY_KEY = "Пиявки";
   private static final String RADIUS_ALTERNATE_RING_VISIBILITY_KEY = "Круг 2";
   private static final String RADIUS_FLEXIBLE_VISIBILITY_KEY = "Гибкий";
   private static final String RING_TEXTURE_NAMESPACE = "rose";
   private static final String RING_TEXTURE_PATH = "icons/world_render/circle2.png";
   private static final String ALTERNATE_RING_TEXTURE_NAMESPACE = "rose";
   private static final String ALTERNATE_RING_TEXTURE_PATH = "textures/jumpcircles/zenith_circle.png";

   private void ensureFlexibleSceneCopyTarget(int width, int height) {
      if (this.flexibleSceneCopyTarget == null) {
         this.flexibleSceneCopyTarget = new TextureTarget(FLEXIBLE_SCENE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.flexibleSceneCopyTarget.width != width || this.flexibleSceneCopyTarget.height != height) {
         this.flexibleSceneCopyTarget.resize(width, height);
      }
   }

   public JumpCircle() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.shapeSetting = new SingleChoiceSetting(
         SHAPE_LABEL,
         new ChoiceOption(SHAPE_RING_OPTION),
         new ChoiceOption(SHAPE_DIAMOND_OPTION),
         new ChoiceOption(SHAPE_LIGHTNING_OPTION),
         new ChoiceOption(SHAPE_ALTERNATE_RING_OPTION),
         new ChoiceOption(SHAPE_LEECH_OPTION),
         new ChoiceOption(SHAPE_FLEXIBLE_OPTION)
      );
      this.radiusSetting = new FloatSetting(RADIUS_LABEL, DEFAULT_RADIUS, 1.0F, 2.0F).withStep(RADIUS_STEP).visibleWhen(() -> !this.shapeSetting.isSelected(RADIUS_LEECH_VISIBILITY_KEY) && !this.shapeSetting.isSelected(RADIUS_ALTERNATE_RING_VISIBILITY_KEY) && !this.shapeSetting.isSelected(RADIUS_FLEXIBLE_VISIBILITY_KEY));
      this.themeColorSetting = new BooleanSetting(THEME_COLOR_LABEL, true).visibleWhen(() -> !this.shapeSetting.isSelected(THEME_COLOR_LEECH_VISIBILITY_KEY) && !this.shapeSetting.isSelected(THEME_COLOR_FLEXIBLE_VISIBILITY_KEY));
      this.glowColorSetting = new ColorSetting(GLOW_COLOR_LABEL, false, DEFAULT_GLOW_COLOR).visibleWhen(() -> !this.shapeSetting.isSelected(GLOW_COLOR_LEECH_VISIBILITY_KEY) && !this.shapeSetting.isSelected(GLOW_COLOR_FLEXIBLE_VISIBILITY_KEY) && !this.themeColorSetting.getValue());
      this.distortionSetting = new BooleanSetting(DISTORTION_LABEL, false).visibleWhen(() -> this.shapeSetting.isSelected(DISTORTION_RING_VISIBILITY_KEY));
      this.distortionStrengthSetting = new FloatSetting(DISTORTION_STRENGTH_LABEL, DEFAULT_DISTORTION_STRENGTH, MIN_DISTORTION_STRENGTH, MAX_DISTORTION_STRENGTH).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(DISTORTION_STRENGTH_RING_VISIBILITY_KEY) && this.distortionSetting.getValue());
      this.alternateRingLifetimeStepsSetting = new FloatSetting(ALTERNATE_RING_LIFETIME_STEPS_LABEL, DEFAULT_ALTERNATE_RING_LIFETIME_STEPS, MIN_ALTERNATE_RING_LIFETIME_STEPS, MAX_ALTERNATE_RING_LIFETIME_STEPS).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(ALTERNATE_RING_LIFETIME_STEPS_ALTERNATE_RING_VISIBILITY_KEY));
      this.alternateRingRadiusSetting = new FloatSetting(ALTERNATE_RING_RADIUS_LABEL, DEFAULT_ALTERNATE_RING_RADIUS, MIN_ALTERNATE_RING_RADIUS, MAX_ALTERNATE_RING_RADIUS).withStep(ALTERNATE_RING_RADIUS_STEP).visibleWhen(() -> this.shapeSetting.isSelected(ALTERNATE_RING_RADIUS_ALTERNATE_RING_VISIBILITY_KEY));
      this.alternateRingGlassSetting = new BooleanSetting(ALTERNATE_RING_GLASS_LABEL, false).visibleWhen(() -> this.shapeSetting.isSelected(ALTERNATE_RING_GLASS_ALTERNATE_RING_VISIBILITY_KEY));
      this.leechAnimationSetting = new SingleChoiceSetting(LEECH_ANIMATION_LABEL, new ChoiceOption(LEECH_ANIMATION_FADE_OUT_ROTATE_OPTION), new ChoiceOption(LEECH_ANIMATION_FADE_IN_OUT_ROTATE_OPTION), new ChoiceOption(LEECH_ANIMATION_FADE_IN_EXPONENT_OPTION)).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_ANIMATION_LEECH_VISIBILITY_KEY));
      this.leechLifetimeMillisSetting = new FloatSetting(LEECH_LIFETIME_MILLIS_LABEL, DEFAULT_LEECH_LIFETIME_MILLIS, MIN_LEECH_LIFETIME_MILLIS, MAX_LEECH_LIFETIME_MILLIS).withStep(LEECH_LIFETIME_MILLIS_STEP).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_LIFETIME_MILLIS_LEECH_VISIBILITY_KEY));
      this.leechSizeSetting = new FloatSetting(LEECH_SIZE_LABEL, 2.0F, 1.0F, MAX_LEECH_SIZE).withStep(LEECH_SIZE_STEP).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_SIZE_LEECH_VISIBILITY_KEY));
      this.leechColorModeSetting = new SingleChoiceSetting(LEECH_COLOR_MODE_LABEL, new ChoiceOption(LEECH_COLOR_MODE_RAINBOW_OPTION), new ChoiceOption(LEECH_COLOR_MODE_THEME_OPTION), new ChoiceOption(LEECH_COLOR_MODE_CUSTOM_OPTION), new ChoiceOption(LEECH_COLOR_MODE_CUSTOM_FADE_OPTION)).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_COLOR_MODE_LEECH_VISIBILITY_KEY));
      this.leechPrimaryColorSetting = new ColorSetting(LEECH_PRIMARY_COLOR_LABEL, false, DEFAULT_LEECH_PRIMARY_COLOR).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_PRIMARY_COLOR_LEECH_VISIBILITY_KEY) && (this.leechColorModeSetting.isSelected(LEECH_PRIMARY_COLOR_CUSTOM_VISIBILITY_KEY) || this.leechColorModeSetting.isSelected(LEECH_PRIMARY_COLOR_CUSTOM_FADE_VISIBILITY_KEY)));
      this.leechSecondaryColorSetting = new ColorSetting(LEECH_SECONDARY_COLOR_LABEL, false, -29184).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_SECONDARY_COLOR_LEECH_VISIBILITY_KEY) && this.leechColorModeSetting.isSelected(LEECH_SECONDARY_COLOR_CUSTOM_FADE_VISIBILITY_KEY));
      this.leechDeepLightSetting = new BooleanSetting(LEECH_DEEP_LIGHT_LABEL, true).visibleWhen(() -> this.shapeSetting.isSelected(LEECH_DEEP_LIGHT_LEECH_VISIBILITY_KEY));
      this.flexibleLifetimeMillisSetting = new FloatSetting(FLEXIBLE_LIFETIME_MILLIS_LABEL, DEFAULT_FLEXIBLE_LIFETIME_MILLIS, MIN_FLEXIBLE_LIFETIME_MILLIS, MAX_FLEXIBLE_LIFETIME_MILLIS).withStep(FLEXIBLE_LIFETIME_MILLIS_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_LIFETIME_MILLIS_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleSizeSetting = new FloatSetting(FLEXIBLE_SIZE_LABEL, DEFAULT_FLEXIBLE_SIZE, MIN_FLEXIBLE_SIZE, MAX_FLEXIBLE_SIZE).withStep(FLEXIBLE_SIZE_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_SIZE_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleAnimationSetting = new SingleChoiceSetting(FLEXIBLE_ANIMATION_LABEL, new ChoiceOption(FLEXIBLE_ANIMATION_ELASTIC_OPTION), new ChoiceOption(FLEXIBLE_ANIMATION_NORMAL_OPTION), new ChoiceOption(FLEXIBLE_ANIMATION_BACK_OPTION)).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_ANIMATION_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleWaveStrengthSetting = new FloatSetting(FLEXIBLE_WAVE_STRENGTH_LABEL, 1.0F, MIN_FLEXIBLE_WAVE_STRENGTH, MAX_FLEXIBLE_WAVE_STRENGTH).withStep(FLEXIBLE_WAVE_STRENGTH_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_WAVE_STRENGTH_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleWaveThicknessSetting = new FloatSetting(FLEXIBLE_WAVE_THICKNESS_LABEL, DEFAULT_FLEXIBLE_WAVE_THICKNESS, MIN_FLEXIBLE_WAVE_THICKNESS, MAX_FLEXIBLE_WAVE_THICKNESS).withStep(FLEXIBLE_WAVE_THICKNESS_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_WAVE_THICKNESS_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleSaturationSetting = new FloatSetting(FLEXIBLE_SATURATION_LABEL, 0.0F, MIN_FLEXIBLE_SATURATION, 1.0F).withStep(FLEXIBLE_SATURATION_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_SATURATION_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleTurbulenceSetting = new BooleanSetting(FLEXIBLE_TURBULENCE_LABEL, true).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_TURBULENCE_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleTurbulenceStrengthSetting = new FloatSetting(FLEXIBLE_TURBULENCE_STRENGTH_LABEL, DEFAULT_FLEXIBLE_TURBULENCE_STRENGTH, MIN_FLEXIBLE_TURBULENCE_STRENGTH, 2.0F).withStep(FLEXIBLE_TURBULENCE_STRENGTH_STEP).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_TURBULENCE_STRENGTH_FLEXIBLE_VISIBILITY_KEY) && this.flexibleTurbulenceSetting.getValue());
      this.flexibleGlowSetting = new BooleanSetting(FLEXIBLE_GLOW_LABEL, true).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_GLOW_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleGlowHeightSetting = new FloatSetting(FLEXIBLE_GLOW_HEIGHT_LABEL, DEFAULT_FLEXIBLE_GLOW_HEIGHT, MIN_FLEXIBLE_GLOW_HEIGHT, MAX_FLEXIBLE_GLOW_HEIGHT).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_GLOW_HEIGHT_FLEXIBLE_VISIBILITY_KEY) && this.flexibleGlowSetting.getValue());
      this.flexibleGlowWidthSetting = new FloatSetting(FLEXIBLE_GLOW_WIDTH_LABEL, DEFAULT_FLEXIBLE_GLOW_WIDTH, MIN_FLEXIBLE_GLOW_WIDTH, MAX_FLEXIBLE_GLOW_WIDTH).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_GLOW_WIDTH_FLEXIBLE_VISIBILITY_KEY) && this.flexibleGlowSetting.getValue());
      this.flexibleGlowColorStrengthSetting = new FloatSetting(FLEXIBLE_GLOW_COLOR_STRENGTH_LABEL, DEFAULT_FLEXIBLE_GLOW_COLOR_STRENGTH, MIN_FLEXIBLE_GLOW_COLOR_STRENGTH, MAX_FLEXIBLE_GLOW_COLOR_STRENGTH).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_GLOW_COLOR_STRENGTH_FLEXIBLE_VISIBILITY_KEY) && this.flexibleGlowSetting.getValue());
      this.flexibleGlowOpacitySetting = new FloatSetting(FLEXIBLE_GLOW_OPACITY_LABEL, DEFAULT_FLEXIBLE_GLOW_OPACITY, MIN_FLEXIBLE_GLOW_OPACITY, MAX_FLEXIBLE_GLOW_OPACITY).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_GLOW_OPACITY_FLEXIBLE_VISIBILITY_KEY) && this.flexibleGlowSetting.getValue());
      this.flexibleColorStrengthSetting = new FloatSetting(FLEXIBLE_COLOR_STRENGTH_LABEL, DEFAULT_FLEXIBLE_COLOR_STRENGTH, 0.0F, MAX_FLEXIBLE_COLOR_STRENGTH).withStep(1.0F).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_COLOR_STRENGTH_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleWaveColorModeSetting = new SingleChoiceSetting(FLEXIBLE_WAVE_COLOR_MODE_LABEL, new ChoiceOption(FLEXIBLE_WAVE_COLOR_MODE_RAINBOW_OPTION), new ChoiceOption(FLEXIBLE_WAVE_COLOR_MODE_THEME_OPTION), new ChoiceOption(FLEXIBLE_WAVE_COLOR_MODE_CUSTOM_OPTION)).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_WAVE_COLOR_MODE_FLEXIBLE_VISIBILITY_KEY));
      this.flexibleSecondColorEnabledSetting = new BooleanSetting(FLEXIBLE_SECOND_COLOR_ENABLED_LABEL, false).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_SECOND_COLOR_ENABLED_FLEXIBLE_VISIBILITY_KEY) && this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_SECOND_COLOR_ENABLED_CUSTOM_VISIBILITY_KEY));
      this.flexiblePrimaryColorSetting = new ColorSetting(FLEXIBLE_PRIMARY_COLOR_LABEL, false, -1).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_PRIMARY_COLOR_FLEXIBLE_VISIBILITY_KEY) && this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_PRIMARY_COLOR_CUSTOM_VISIBILITY_KEY));
      this.flexibleSecondaryColorSetting = new ColorSetting(FLEXIBLE_SECONDARY_COLOR_LABEL, false, DEFAULT_FLEXIBLE_SECONDARY_COLOR).visibleWhen(() -> this.shapeSetting.isSelected(FLEXIBLE_SECONDARY_COLOR_FLEXIBLE_VISIBILITY_KEY) && this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_SECONDARY_COLOR_CUSTOM_VISIBILITY_KEY) && this.flexibleSecondColorEnabledSetting.getValue());
      this.ringEffects = new ArrayList<>();
      this.lightningEffects = new ArrayList<>();
      this.leechEffects = new ArrayList<>();
      this.alternateRingSurfaceEffects = new ArrayList<>();
      this.flexibleWaveEffects = new ArrayList<>();
      this.ringPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(RING_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(RING_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(RING_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(RING_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME)
         .withBlend(BlendFunction.ADDITIVE)
         .build();
      this.alternateRingPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ALTERNATE_RING_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(ALTERNATE_RING_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(ALTERNATE_RING_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ALTERNATE_RING_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME)
         .withBlend(BlendFunction.OVERLAY)
         .build();
      this.alternateRingGlassPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ALTERNATE_RING_GLASS_PIPELINE_LOCATION)
         .withUniform(ALTERNATE_RING_GLASS_PIPELINE_GLOBALS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withVertexShader(ClientResourceIds.getCoreId(ALTERNATE_RING_GLASS_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(ALTERNATE_RING_GLASS_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ALTERNATE_RING_GLASS_PIPELINE_TEXTURE_SAMPLER_LAYOUT_NAME)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.distortionPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(DISTORTION_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(DISTORTION_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(DISTORTION_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withUniform(DISTORTION_PIPELINE_DISTORTION_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(DISTORTION_PIPELINE_SCENE_SAMPLER_LAYOUT_NAME)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.distortionGlowPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(DISTORTION_GLOW_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(DISTORTION_GLOW_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(DISTORTION_GLOW_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withBlend(BlendFunction.ADDITIVE)
         .build();
      this.diamondPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(DIAMOND_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(DIAMOND_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(DIAMOND_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withBlend(BlendFunction.ADDITIVE)
         .build();
      this.lightningPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(LIGHTNING_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(LIGHTNING_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(LIGHTNING_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withBlend(BlendFunction.ADDITIVE)
         .build();
      this.flexibleWavePipeline = RenderPipelineBuilder.builder()
         .withLocation(FLEXIBLE_WAVE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FLEXIBLE_WAVE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FLEXIBLE_WAVE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(FLEXIBLE_WAVE_PIPELINE_FLEXIBLE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(FLEXIBLE_WAVE_PIPELINE_SCENE_SAMPLER_LAYOUT_NAME)
         .withSampler(FLEXIBLE_WAVE_PIPELINE_DEPTH_SAMPLER_LAYOUT_NAME)
         .build();
      this.wasOnGround = true;
      this.inverseViewProjectionMatrix = new Matrix4f();
      this.playerTickListener = this::onPlayerTick;
      this.worldEffectsListener = this::onWorldEffects;
      this.angularThemeColors = new int[49];
      this.addSettings(
         this.shapeSetting,
         this.radiusSetting,
         this.themeColorSetting,
         this.glowColorSetting,
         this.distortionSetting,
         this.distortionStrengthSetting,
         this.alternateRingLifetimeStepsSetting,
         this.alternateRingRadiusSetting,
         this.alternateRingGlassSetting,
         this.leechAnimationSetting,
         this.leechLifetimeMillisSetting,
         this.leechSizeSetting,
         this.leechColorModeSetting,
         this.leechPrimaryColorSetting,
         this.leechSecondaryColorSetting,
         this.leechDeepLightSetting,
         this.flexibleLifetimeMillisSetting,
         this.flexibleSizeSetting,
         this.flexibleAnimationSetting,
         this.flexibleWaveStrengthSetting,
         this.flexibleWaveThicknessSetting,
         this.flexibleSaturationSetting,
         this.flexibleTurbulenceSetting,
         this.flexibleTurbulenceStrengthSetting,
         this.flexibleGlowSetting,
         this.flexibleGlowHeightSetting,
         this.flexibleGlowWidthSetting,
         this.flexibleGlowColorStrengthSetting,
         this.flexibleGlowOpacitySetting,
         this.flexibleColorStrengthSetting,
         this.flexibleWaveColorModeSetting,
         this.flexibleSecondColorEnabledSetting,
         this.flexiblePrimaryColorSetting,
         this.flexibleSecondaryColorSetting
      );
   }

   private JumpCircle.AlternateRingSurfaceEffect createAlternateRingSurfaceEffect(Entity entity, double centerX, double centerY, double centerZ, float radius) {
      float sampleRadius = radius + ALTERNATE_RING_GRID_RADIUS_MARGIN;
      int gridSize = getSurfaceGridSize(radius);
      double gridOriginX = centerX - sampleRadius;
      double gridOriginZ = centerZ - sampleRadius;
      double gridStep = ALTERNATE_RING_GRID_DIAMETER_FACTOR * sampleRadius / (gridSize - 1);
      float[] sampleHeights = new float[gridSize * gridSize];
      float[] elevatedWeights = new float[gridSize * gridSize];
      boolean[] blockedSamples = new boolean[gridSize * gridSize];

      for (int gridX = 0; gridX < gridSize; gridX++) {
         for (int gridZ = 0; gridZ < gridSize; gridZ++) {
            double sampleX = gridOriginX + gridX * gridStep;
            double sampleZ = gridOriginZ + gridZ * gridStep;
            float sampleHeight = this.sampleCollisionHeight(entity, sampleX, sampleZ, centerY);
            float heightAboveOrigin = sampleHeight - (float)centerY;
            int sampleIndex = gridX * gridSize + gridZ;
            elevatedWeights[sampleIndex] = heightAboveOrigin >= ALTERNATE_RING_ELEVATED_SAMPLE_MIN_HEIGHT ? 1.0F : 0.0F;
            blockedSamples[sampleIndex] = heightAboveOrigin > ALTERNATE_RING_BLOCKED_SAMPLE_HEIGHT;
            sampleHeights[sampleIndex] = Mth.clamp(sampleHeight, (float)(centerY - ALTERNATE_RING_MIN_SAMPLE_DEPTH), (float)(centerY + ALTERNATE_RING_MAX_SAMPLE_HEIGHT));
         }
      }

      spreadElevatedSurfaceSamples(sampleHeights, elevatedWeights, blockedSamples, gridSize);
      return new JumpCircle.AlternateRingSurfaceEffect(
         new Vec3(centerX, centerY, centerZ), radius, gridOriginX, gridOriginZ, gridStep, gridSize, sampleHeights, elevatedWeights, blockedSamples, this.collectExposedBlockSideSegments(entity, centerX, centerY, centerZ, radius)
      );
   }

   private void drawTexturedEffectMesh(BufferBuilder builder, Identifier textureId, int vertexCount) {
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
               vertexBuffer = RenderSystem.getDevice().createBuffer(() -> TEXTURED_EFFECT_VERTEX_BUFFER_LABEL, 32, mesh.vertexBuffer());
               RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture texture = MINECRAFT_CLIENT.getTextureManager().getTexture(textureId);

               try (RenderPass renderPass = encoder.createRenderPass(
                     () -> TEXTURED_EFFECT_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(renderPass);
                  renderPass.bindTexture(TEXTURED_MESH_TEXTURE_SAMPLER_BINDING, texture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  renderPass.setPipeline(this.ringPipeline);
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

   private static float easeInOutElastic(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      if (value != 0.0F && value != 1.0F) {
         float periodRadians = ELASTIC_IN_OUT_PERIOD_RADIANS;
         return value < ELASTIC_IN_OUT_MIDPOINT
            ? (float)(-(Math.pow(ELASTIC_IN_OUT_LEADING_EXPONENT_BASE, ELASTIC_IN_OUT_LEADING_EXPONENT_SCALE * value - ELASTIC_IN_OUT_LEADING_EXPONENT_OFFSET) * Math.sin((ELASTIC_IN_OUT_LEADING_PHASE_SCALE * value - ELASTIC_IN_OUT_LEADING_PHASE_OFFSET) * periodRadians)) / ELASTIC_IN_OUT_LEADING_AMPLITUDE_DIVISOR)
            : (float)(Math.pow(ELASTIC_IN_OUT_TRAILING_EXPONENT_BASE, ELASTIC_IN_OUT_TRAILING_EXPONENT_SCALE * value + ELASTIC_IN_OUT_TRAILING_EXPONENT_OFFSET) * Math.sin((ELASTIC_IN_OUT_TRAILING_PHASE_SCALE * value - ELASTIC_IN_OUT_TRAILING_PHASE_OFFSET) * periodRadians) / ELASTIC_IN_OUT_TRAILING_AMPLITUDE_DIVISOR + 1.0);
      } else {
         return value;
      }
   }

   private BlockHitResult clipVerticalCollisionRay(Entity entity, double x, double z, double startY, double endY) {
      Vec3 start = new Vec3(x, startY, z);
      Vec3 end = new Vec3(x, endY, z);
      return MINECRAFT_CLIENT.level.clip(new ClipContext(start, end, Block.COLLIDER, Fluid.NONE, entity));
   }

   private void appendAlternateRingWallGlowVertex(
      BufferBuilder builder, Matrix4f matrix, JumpCircle.AlternateRingSurfaceEffect effect, Vec3 cameraPosition, float radius, float opacity, float time, float[] wallSegment, float horizontalProgress, float heightOffset
   ) {
      double x = Mth.lerp(horizontalProgress, wallSegment[0], wallSegment[2]);
      double z = Mth.lerp(horizontalProgress, wallSegment[1], wallSegment[3]);
      double y = wallSegment[4] + heightOffset;
      double relativeX = x - effect.origin.x;
      double relativeZ = z - effect.origin.z;
      float distance = (float)Math.sqrt(relativeX * relativeX + relativeZ * relativeZ);
      float normalizedRadius = distance / radius;
      float relativeY = (float)(y - effect.origin.y);
      float turbulence = sampleTurbulence(x, y, z, time);
      float perturbedHeight = relativeY + WALL_GLOW_HEIGHT_TURBULENCE_SCALE * turbulence;
      float perturbedRadius = normalizedRadius + WALL_GLOW_RADIUS_TURBULENCE_SCALE * turbulence;
      float edgeHeight = Math.min(WALL_GLOW_MAX_HEIGHT, Math.max(0.0F, radius - distance) * WALL_GLOW_REMAINING_RADIUS_HEIGHT_SCALE);
      float heightMask = edgeHeight > WALL_GLOW_MIN_HEIGHT ? 1.0F - smoothstep(edgeHeight * WALL_GLOW_HEIGHT_FADE_START_SCALE, edgeHeight, perturbedHeight) : 0.0F;
      float glowMask = WALL_GLOW_INTENSITY * smoothstep(0.0F, WALL_GLOW_HEIGHT_FADE_IN_END, relativeY) * heightMask * (1.0F - smoothstep(WALL_GLOW_RADIUS_FADE_OUT_START, WALL_GLOW_RADIUS_FADE_OUT_END, perturbedRadius));
      float noiseModulation = WALL_GLOW_NOISE_BASE + WALL_GLOW_NOISE_AMPLITUDE * sampleTurbulence(x * WALL_GLOW_NOISE_X_FREQUENCY + WALL_GLOW_NOISE_X_PHASE, y * WALL_GLOW_NOISE_Y_FREQUENCY, z * WALL_GLOW_NOISE_Z_FREQUENCY - WALL_GLOW_NOISE_Z_PHASE, time * WALL_GLOW_NOISE_TIME_FREQUENCY);
      float vertexAlpha = opacity * glowMask * noiseModulation;
      float clampedRadius = Math.min(normalizedRadius, 1.0F);
      float radialX = distance > WALL_GLOW_RADIAL_X_EPSILON ? (float)(relativeX / distance) : 0.0F;
      float radialZ = distance > WALL_GLOW_RADIAL_Z_EPSILON ? (float)(relativeZ / distance) : 0.0F;
      float u = WALL_GLOW_U_CENTER + radialX * clampedRadius * WALL_GLOW_U_RADIUS_SCALE;
      float v = WALL_GLOW_V_CENTER + radialZ * clampedRadius * WALL_GLOW_V_RADIUS_SCALE;
      float angleRadians = (float)Math.atan2(relativeZ, relativeX);
      int angularColorIndex = Math.floorMod(Math.round(angleRadians / WALL_GLOW_FULL_TURN_RADIANS * WALL_GLOW_ANGULAR_COLOR_COUNT), 48);
      int colorArgb = this.themeColorSetting.getValue() ? this.angularThemeColors[angularColorIndex] : this.getEffectColorArgb();
      this.appendVertex(builder, matrix, (float)(x - cameraPosition.x), (float)(y - cameraPosition.y), (float)(z - cameraPosition.z), u, v, ColorMath.toRgbaVector(scaleAlpha(colorArgb, vertexAlpha)));
   }

   private void renderLightningEffects(WorldEffectsEvent event) {
      if (!this.lightningEffects.isEmpty()) {
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         Matrix4f matrix = event.getPoses().last().pose();
         Vector4f rgba = ColorMath.toRgbaVector(this.getEffectColorArgb());
         BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int vertexCount = 0;
         long nowMillis = System.currentTimeMillis();
         float animationTime = (float)(nowMillis % LIGHTNING_TIME_WRAP_MILLIS) / LIGHTNING_MILLIS_PER_SECOND;
         Iterator effectIterator = this.lightningEffects.iterator();

         while (effectIterator.hasNext()) {
            JumpCircle.LightningEffect effect = (JumpCircle.LightningEffect)effectIterator.next();
            float progress = (float)(nowMillis - effect.createdAtMillis) / LIGHTNING_LIFETIME_MILLIS;
            if (progress >= 1.0F) {
               effectIterator.remove();
            } else {
               float revealProgress = this.smootherstep(Mth.clamp(progress / LIGHTNING_REVEAL_PROGRESS_SPAN, 0.0F, 1.0F));
               float flashProgress = 1.0F - this.smootherstep(Mth.clamp(progress / LIGHTNING_FLASH_PROGRESS_SPAN, 0.0F, 1.0F));
               float fadeProgress = this.smootherstep(Mth.clamp((progress - LIGHTNING_FADE_OUT_START_PROGRESS) / LIGHTNING_FADE_OUT_PROGRESS_SPAN, 0.0F, 1.0F));
               float flickerScale = revealProgress >= 1.0F ? LIGHTNING_FLICKER_BASE + LIGHTNING_FLICKER_AMPLITUDE * Mth.sin(animationTime * LIGHTNING_FLICKER_RADIANS_PER_SECOND + effect.flickerPhase) : 1.0F;
               float effectAlpha = (1.0F - fadeProgress) * flickerScale;
               if (!(effectAlpha <= LIGHTNING_MIN_VISIBLE_ALPHA) && !(revealProgress <= LIGHTNING_MIN_REVEAL_PROGRESS)) {
                  for (JumpCircle.LightningEffect.LightningBranch branch : effect.branches) {
                     Vec3[] points = branch.points;
                     int segmentCount = points.length - 1;
                     float branchRevealProgress = Mth.clamp((revealProgress - branch.revealDelayProgress) / (1.0F - branch.revealDelayProgress), 0.0F, 1.0F);
                     if (!(branchRevealProgress <= LIGHTNING_MIN_BRANCH_REVEAL_PROGRESS)) {
                        float visibleSegmentCount = branchRevealProgress * segmentCount;

                        for (int segment = 0; segment < segmentCount && !(visibleSegmentCount <= segment); segment++) {
                           float segmentRevealProgress = Mth.clamp(visibleSegmentCount - segment, 0.0F, 1.0F);
                           boolean partialSegment = segmentRevealProgress < 1.0F;
                           Vec3 segmentEnd = partialSegment ? points[segment].lerp(points[segment + 1], segmentRevealProgress) : points[segment + 1];
                           float startProgress = (float)segment / segmentCount;
                           float endProgress = startProgress + segmentRevealProgress / segmentCount;
                           float startWidth = Mth.lerp(startProgress, LIGHTNING_SEGMENT_START_WIDTH_SCALE_AT_ORIGIN, LIGHTNING_SEGMENT_START_WIDTH_SCALE_AT_TIP) * this.radiusSetting.getValue();
                           float endWidth = Mth.lerp(endProgress, LIGHTNING_SEGMENT_END_WIDTH_SCALE_AT_ORIGIN, LIGHTNING_SEGMENT_END_WIDTH_SCALE_AT_TIP) * this.radiusSetting.getValue();
                           float branchAlphaScale = Mth.lerp((startProgress + endProgress) * LIGHTNING_SEGMENT_MIDPOINT_SCALE, 1.0F, LIGHTNING_BRANCH_TIP_ALPHA_SCALE);
                           float tipAlphaScale = partialSegment ? LIGHTNING_PARTIAL_SEGMENT_ALPHA_SCALE : 1.0F;
                           float segmentAlpha = effectAlpha * branchAlphaScale * rgba.w * (1.0F + flashProgress * LIGHTNING_FLASH_ALPHA_SCALE) * tipAlphaScale;
                           float tintProgress = partialSegment ? 1.0F : Math.min(startProgress * LIGHTNING_FULL_SEGMENT_TINT_PROGRESS_SCALE, 1.0F);
                           Vector4f segmentRgba = new Vector4f(
                              Mth.lerp(tintProgress, 1.0F, rgba.x), Mth.lerp(tintProgress, 1.0F, rgba.y), Mth.lerp(tintProgress, 1.0F, rgba.z), Mth.clamp(segmentAlpha, 0.0F, 1.0F)
                           );
                           if (startWidth > LIGHTNING_MIN_START_WIDTH && endWidth > LIGHTNING_MIN_END_WIDTH) {
                              vertexCount += this.appendLightningSegment(builder, matrix, effect.origin, cameraPosition, points[segment], segmentEnd, startWidth, endWidth, segmentRgba);
                           }
                        }
                     }
                  }
               }
            }
         }

         this.drawProceduralEffectMesh(builder, vertexCount, this.lightningPipeline);
      }
   }

   private int appendAlternateRingGlowCell(BufferBuilder builder, Matrix4f matrix, JumpCircle.AlternateRingSurfaceEffect effect, Vec3 cameraPosition, float radius, float opacity, int gridX, int gridZ) {
      int gridSize = effect.gridSize;
      int index00 = gridX * gridSize + gridZ;
      int index01 = gridX * gridSize + gridZ + 1;
      int index11 = (gridX + 1) * gridSize + gridZ + 1;
      int index10 = (gridX + 1) * gridSize + gridZ;
      boolean blocked00 = effect.blockedSamples[index00];
      boolean blocked01 = effect.blockedSamples[index01];
      boolean blocked11 = effect.blockedSamples[index11];
      boolean blocked10 = effect.blockedSamples[index10];
      if (blocked00 && blocked01 && blocked11 && blocked10) {
         return 0;
      }

      float height00 = effect.sampleHeights[index00];
      float height01 = effect.sampleHeights[index01];
      float height11 = effect.sampleHeights[index11];
      float height10 = effect.sampleHeights[index10];
      float weight00 = effect.elevatedWeights[index00];
      float weight01 = effect.elevatedWeights[index01];
      float weight11 = effect.elevatedWeights[index11];
      float weight10 = effect.elevatedWeights[index10];
      if (blocked00 || blocked01 || blocked11 || blocked10) {
         float maxUnblockedHeight = ALTERNATE_RING_GLOW_INITIAL_UNBLOCKED_MAX_HEIGHT;
         if (!blocked00) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height00);
         }

         if (!blocked01) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height01);
         }

         if (!blocked11) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height11);
         }

         if (!blocked10) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height10);
         }

         if (blocked00) {
            height00 = maxUnblockedHeight;
         }

         if (blocked01) {
            height01 = maxUnblockedHeight;
         }

         if (blocked11) {
            height11 = maxUnblockedHeight;
         }

         if (blocked10) {
            height10 = maxUnblockedHeight;
         }
      }

      float minHeight = Math.min(Math.min(height00, height01), Math.min(height11, height10));
      float maxHeight = Math.max(Math.max(height00, height01), Math.max(height11, height10));
      int subdivisionCount = Mth.clamp((int)Math.ceil((maxHeight - minHeight) / ALTERNATE_RING_GLOW_SUBDIVISION_HEIGHT_STEP), 1, 8);
      boolean subdivideAlongX = Math.abs(height00 - height10) + Math.abs(height01 - height11) >= Math.abs(height00 - height01) + Math.abs(height10 - height11);
      int vertexCount = 0;

      for (int subdivision = 0; subdivision < subdivisionCount; subdivision++) {
         float subdivisionStart = (float)subdivision / subdivisionCount;
         float subdivisionEnd = (float)(subdivision + 1) / subdivisionCount;
         if (subdivideAlongX) {
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionStart, 0.0F, height00, weight00);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionStart, 1.0F, height10, weight10);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionEnd, 1.0F, height11, weight11);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionEnd, 0.0F, height01, weight01);
         } else {
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 0.0F, subdivisionStart, height00, weight00);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 0.0F, subdivisionEnd, height10, weight10);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 1.0F, subdivisionEnd, height11, weight11);
            this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 1.0F, subdivisionStart, height01, weight01);
         }

         vertexCount += 4;
      }

      return vertexCount;
   }

   private float getFlexibleRadiusScale(float progress) {
      float clampedProgress = Mth.clamp(progress, 0.0F, 1.0F);
      if (this.flexibleAnimationSetting.isSelected(FLEXIBLE_BACK_ANIMATION_KEY)) {
         return easeOutBack(clampedProgress);
      } else {
         return this.flexibleAnimationSetting.isSelected(FLEXIBLE_NORMAL_ANIMATION_KEY) ? clampedProgress : easeOutElastic(clampedProgress);
      }
   }

   private static void writeOpaqueRgb(float[] values, int offset, int colorArgb) {
      values[offset] = (colorArgb >> 16 & 0xFF) / UNIFORM_RED_BYTE_SCALE;
      values[offset + 1] = (colorArgb >> 8 & 0xFF) / UNIFORM_GREEN_BYTE_SCALE;
      values[offset + 2] = (colorArgb & 0xFF) / UNIFORM_BLUE_BYTE_SCALE;
      values[offset + 3] = 1.0F;
   }

   private List<float[]> collectExposedBlockSideSegments(Entity entity, double centerX, double centerY, double centerZ, float radius) {
      ArrayList segments = new ArrayList();
      double scanRadius = radius + WALL_SCAN_RADIUS_MARGIN;
      int minBlockX = Mth.floor(centerX - scanRadius);
      int maxBlockX = Mth.floor(centerX + scanRadius);
      int minBlockZ = Mth.floor(centerZ - scanRadius);
      int maxBlockZ = Mth.floor(centerZ + scanRadius);
      int minBlockY = Mth.floor(centerY) - 1;
      int maxBlockY = Mth.floor(centerY) + 2;
      MutableBlockPos blockPosition = new MutableBlockPos();

      for (int blockX = minBlockX; blockX <= maxBlockX; blockX++) {
         for (int blockZ = minBlockZ; blockZ <= maxBlockZ; blockZ++) {
            float surfaceHeight = this.sampleCollisionHeight(entity, blockX + WALL_SURFACE_SAMPLE_X_OFFSET, blockZ + WALL_SURFACE_SAMPLE_Z_OFFSET, centerY);
            if (surfaceHeight - centerY <= WALL_MAX_SURFACE_HEIGHT) {
               for (int blockY = minBlockY; blockY <= maxBlockY; blockY++) {
                  blockPosition.set(blockX, blockY, blockZ);
                  VoxelShape collisionShape = MINECRAFT_CLIENT.level.getBlockState(blockPosition).getCollisionShape(MINECRAFT_CLIENT.level, blockPosition);
                  if (isFullCollisionBlock(collisionShape)) {
                     for (Direction direction : HORIZONTAL_DIRECTIONS) {
                        blockPosition.set(blockX + direction.getStepX(), blockY, blockZ + direction.getStepZ());
                        VoxelShape neighborShape = MINECRAFT_CLIENT.level.getBlockState(blockPosition).getCollisionShape(MINECRAFT_CLIENT.level, blockPosition);
                        boolean sideExposed = neighborShape.isEmpty() || !isFullCollisionBlock(neighborShape);
                        blockPosition.set(blockX, blockY, blockZ);
                        if (sideExposed) {
                           double faceCenterX = blockX + WALL_FACE_CENTER_X_OFFSET + direction.getStepX() * WALL_FACE_CENTER_X_DIRECTION_SCALE;
                           double faceCenterZ = blockZ + WALL_FACE_CENTER_Z_OFFSET + direction.getStepZ() * WALL_FACE_CENTER_Z_DIRECTION_SCALE;
                           if (!((centerX - faceCenterX) * direction.getStepX() + (centerZ - faceCenterZ) * direction.getStepZ() <= 0.0)) {
                              float startX;
                              float startZ;
                              float endX;
                              float endZ;
                              if (direction.getStepZ() != 0) {
                                 startX = blockX;
                                 endX = blockX + 1;
                                 startZ = endZ = (float)(blockZ + (direction == Direction.SOUTH ? 1.0 : 0.0) + direction.getStepZ() * WALL_Z_SURFACE_BIAS);
                              } else {
                                 startZ = blockZ;
                                 endZ = blockZ + 1;
                                 startX = endX = (float)(blockX + (direction == Direction.EAST ? 1.0 : 0.0) + direction.getStepX() * WALL_X_SURFACE_BIAS);
                              }

                              double nearestRelativeX = Mth.clamp(centerX, Math.min(startX, endX), Math.max(startX, endX)) - centerX;
                              double nearestRelativeZ = Mth.clamp(centerZ, Math.min(startZ, endZ), Math.max(startZ, endZ)) - centerZ;
                              if (!(nearestRelativeX * nearestRelativeX + nearestRelativeZ * nearestRelativeZ > scanRadius * scanRadius)) {
                                 segments.add(new float[]{startX, startZ, endX, endZ, blockY});
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return segments;
   }

   private static float smoothstep(float edge0, float edge1, float value) {
      float progress = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0F, 1.0F);
      return progress * progress * (SMOOTHSTEP_CUBIC_COEFFICIENT - 2.0F * progress);
   }

   private static float easeOutBack(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      float overshoot = BACK_OUT_OVERSHOOT;
      float overshootPlusOne = overshoot + 1.0F;
      return 1.0F + overshootPlusOne * (float)Math.pow(value - 1.0F, BACK_OUT_CUBIC_POWER) + overshoot * (float)Math.pow(value - 1.0F, BACK_OUT_QUADRATIC_POWER);
   }

   private static float easeOutCircular(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      return (float)Math.sqrt(1.0 - Math.pow(value - 1.0F, CIRCULAR_OUT_POWER));
   }

   private int getEffectColorArgb() {
      return this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.glowColorSetting.getValue();
   }

   private void renderRingEffects(WorldEffectsEvent event) {
      if (!this.ringEffects.isEmpty()) {
         if (this.ringTexture == null) {
            this.ringTexture = MINECRAFT_CLIENT.getTextureManager().getTexture(RING_TEXTURE);
         }

         if (this.ringTexture != null) {
            float partialTick = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            float rotationDegrees = ((float)MINECRAFT_CLIENT.level.getGameTime() + partialTick) * RING_ROTATION_DEGREES_PER_TICK;
            Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            Matrix4f matrix = event.getPoses().last().pose();
            boolean distortionEnabled = this.distortionSetting.getValue();
            BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int vertexCount = 0;
            Iterator effectIterator = this.ringEffects.iterator();

            while (effectIterator.hasNext()) {
               JumpCircle.RingEffect effect = (JumpCircle.RingEffect)effectIterator.next();
               long elapsedMillis = System.currentTimeMillis() - effect.createdAtMillis - effect.delayMillis;
               if (elapsedMillis >= 0L) {
                  float progress = (float)elapsedMillis / RING_LIFETIME_MILLIS;
                  if (progress >= 1.0F) {
                     effectIterator.remove();
                  } else {
                     float fadeInProgress = Mth.clamp(progress / RING_FADE_IN_PROGRESS_SPAN, 0.0F, 1.0F);
                     float fadeOutProgress = Mth.clamp((progress - RING_FADE_OUT_START_PROGRESS) / RING_FADE_OUT_PROGRESS_SPAN, 0.0F, 1.0F);
                     float fadeInScale = this.smootherstep(fadeInProgress);
                     float fadeOutScale = this.smootherstep(fadeOutProgress);
                     float radiusGrowthScale = this.easeOutCubicRadius(Mth.clamp(progress / RING_RADIUS_GROWTH_PROGRESS_SPAN, 0.0F, 1.0F));
                     float primaryRadiusPulse = Mth.sin(fadeInProgress * RING_PRIMARY_PULSE_PHASE_RADIANS) * RING_PRIMARY_PULSE_RADIUS_SCALE;
                     float secondaryRadiusPulse = Mth.sin(Mth.clamp((progress - RING_SECONDARY_PULSE_START_PROGRESS) / RING_SECONDARY_PULSE_PROGRESS_SPAN, 0.0F, 1.0F) * RING_SECONDARY_PULSE_PHASE_RADIANS) * RING_SECONDARY_PULSE_RADIUS_SCALE;
                     float effectAlpha = fadeInScale * (1.0F - fadeOutScale) * effect.alphaScale;
                     float radiusScale = RING_INITIAL_RADIUS_SCALE + radiusGrowthScale * RING_RADIUS_GROWTH_SCALE + primaryRadiusPulse + secondaryRadiusPulse + fadeOutScale * RING_FADE_OUT_RADIUS_SCALE + effect.radiusOffset;
                     float settingRadius = this.radiusSetting.getValue() * RING_SETTING_RADIUS_SCALE;
                     float radius = settingRadius * radiusScale;
                     float quadRadius = distortionEnabled ? radius * DISTORTION_QUAD_RADIUS_SCALE : radius;
                     Vector4f baseRgba = ColorMath.toRgbaVector(this.getEffectColorArgb());
                     Vector4f rgba = new Vector4f(baseRgba.x, baseRgba.y, baseRgba.z, baseRgba.w * effectAlpha);
                     float rotationRadians = distortionEnabled ? 0.0F : (float)Math.toRadians(rotationDegrees + progress * RING_PROGRESS_ROTATION_DEGREES + (float)effect.delayMillis * RING_DELAY_ROTATION_DEGREES_PER_MILLI);
                     this.appendHorizontalQuad(builder, matrix, effect.position, cameraPosition, quadRadius, quadRadius, rotationRadians, rgba);
                     vertexCount += 4;
                  }
               }
            }

            MeshData mesh = builder.build();
            if (mesh != null) {
               if (vertexCount == 0) {
                  mesh.close();
               } else {
                  RenderPass ringPass = null;
                  GpuBuffer vertexBuffer = null;
                  GpuBuffer distortionUniformBuffer = null;

                  try {
                     RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
                     CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                     float animationSeconds = (float)(System.currentTimeMillis() % DISTORTION_TIME_WRAP_MILLIS) / DISTORTION_MILLIS_PER_SECOND;
                     float screenHeight = Math.max(1.0F, MINECRAFT_CLIENT.getWindow().getScreenHeight());
                     float mouseX = DISTORTION_MOUSE_X_SCALE * (2.0F * (float)MINECRAFT_CLIENT.mouseHandler.xpos() - MINECRAFT_CLIENT.getWindow().getScreenWidth()) / screenHeight;
                     float mouseY = DISTORTION_MOUSE_Y_SCALE * (2.0F * (MINECRAFT_CLIENT.getWindow().getScreenHeight() - (float)MINECRAFT_CLIENT.mouseHandler.ypos()) - MINECRAFT_CLIENT.getWindow().getScreenHeight()) / screenHeight;
                     DrawState drawState = mesh.drawState();
                     int indexCount = drawState.indexCount();
                     AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
                     GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
                     IndexType indexType = sequentialIndices.type();
                     vertexBuffer = RenderSystem.getDevice().createBuffer(() -> RING_VERTEX_BUFFER_LABEL, 32, mesh.vertexBuffer());
                     if (distortionEnabled) {
                        TextureTarget sceneCopyTarget = this.getSharedSceneCopyTarget(renderTarget);
                        encoder.copyTextureToTexture(renderTarget.getColorTexture(), sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, renderTarget.width, renderTarget.height);

                        try (MemoryStack stack = MemoryStack.stackPush()) {
                           ByteBuffer distortionUniformData = Std140Builder.onStack(stack, 32)
                              .putVec2(renderTarget.width, renderTarget.height)
                              .putFloat(this.distortionStrengthSetting.getValue())
                              .putFloat(animationSeconds)
                              .putVec2(mouseX, mouseY)
                              .align(16)
                              .get();
                           distortionUniformBuffer = RenderSystem.getDevice().createBuffer(() -> DISTORTION_UNIFORM_BUFFER_LABEL, 128, distortionUniformData);
                        }

                        try (RenderPass distortionPass = encoder.createRenderPass(
                              () -> DISTORTION_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                           )) {
                           RenderSystem.bindDefaultUniforms(distortionPass);
                           distortionPass.bindTexture(DISTORTION_SCENE_SAMPLER_BINDING, sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                           distortionPass.setUniform(DISTORTION_UNIFORM_BINDING, distortionUniformBuffer);
                           distortionPass.setPipeline(this.distortionPipeline);
                           distortionPass.setVertexBuffer(0, vertexBuffer.slice());
                           distortionPass.setIndexBuffer(indexBuffer, indexType);
                           distortionPass.drawIndexed(indexCount, 1, 0, 0, 0);
                        }

                        try (RenderPass distortionGlowPass = encoder.createRenderPass(
                              () -> DISTORTION_GLOW_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                           )) {
                           RenderSystem.bindDefaultUniforms(distortionGlowPass);
                           distortionGlowPass.setPipeline(this.distortionGlowPipeline);
                           distortionGlowPass.setVertexBuffer(0, vertexBuffer.slice());
                           distortionGlowPass.setIndexBuffer(indexBuffer, indexType);
                           distortionGlowPass.drawIndexed(indexCount, 1, 0, 0, 0);
                        }
                     }

                     if (!distortionEnabled) {
                        ringPass = encoder.createRenderPass(
                           () -> RING_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                        );
                        RenderSystem.bindDefaultUniforms(ringPass);
                        ringPass.bindTexture(RING_TEXTURE_SAMPLER_BINDING, this.ringTexture.getTextureView(), this.ringTexture.getSampler());
                        ringPass.setPipeline(this.ringPipeline);
                        ringPass.setVertexBuffer(0, vertexBuffer.slice());
                        ringPass.setIndexBuffer(indexBuffer, indexType);
                        ringPass.drawIndexed(indexCount, 1, 0, 0, 0);
                     }
                  } finally {
                     if (ringPass != null) {
                        ringPass.close();
                     }

                     if (vertexBuffer != null) {
                        vertexBuffer.close();
                     }

                     if (distortionUniformBuffer != null) {
                        distortionUniformBuffer.close();
                     }

                     mesh.close();
                  }
               }
            }
         }
      }
   }

   private int appendLeechQuad(BufferBuilder builder, Matrix4f matrix, Vec3 cameraPosition, JumpCircle.LeechRenderSample sample) {
      float halfSize = sample.size * LEECH_RING_HALF_SIZE_FACTOR;
      float rotationRadians = (float)Math.toRadians(sample.rotationDegrees);
      int colorPhase = sample.colorPhaseOffset;
      float alpha = sample.alpha;
      Vector4f corner0Rgba = ColorMath.toRgbaVector(this.getLeechColor(colorPhase, alpha));
      Vector4f corner1Rgba = ColorMath.toRgbaVector(this.getLeechColor(colorPhase + 324, alpha));
      Vector4f corner2Rgba = ColorMath.toRgbaVector(this.getLeechColor(colorPhase + 648, alpha));
      Vector4f corner3Rgba = ColorMath.toRgbaVector(this.getLeechColor(colorPhase + 972, alpha));
      float cosRotation = Mth.cos(rotationRadians);
      float sinRotation = Mth.sin(rotationRadians);
      float relativeY = (float)(sample.position.y - cameraPosition.y) + LEECH_RING_SURFACE_OFFSET;
      this.appendRotatedVertexAtHeight(builder, matrix, sample.position, cameraPosition, -halfSize, -halfSize, relativeY, cosRotation, sinRotation, 0.0F, 0.0F, corner0Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, sample.position, cameraPosition, -halfSize, halfSize, relativeY, cosRotation, sinRotation, 0.0F, 1.0F, corner1Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, sample.position, cameraPosition, halfSize, halfSize, relativeY, cosRotation, sinRotation, 1.0F, 1.0F, corner2Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, sample.position, cameraPosition, halfSize, -halfSize, relativeY, cosRotation, sinRotation, 1.0F, 0.0F, corner3Rgba);
      return 4;
   }

   private int appendAlternateRingSurfaceCell(BufferBuilder builder, Matrix4f matrix, JumpCircle.AlternateRingSurfaceEffect effect, Vec3 cameraPosition, float radius, float opacity, int gridX, int gridZ) {
      int gridSize = effect.gridSize;
      int index00 = gridX * gridSize + gridZ;
      int index01 = gridX * gridSize + gridZ + 1;
      int index11 = (gridX + 1) * gridSize + gridZ + 1;
      int index10 = (gridX + 1) * gridSize + gridZ;
      boolean blocked00 = effect.blockedSamples[index00];
      boolean blocked01 = effect.blockedSamples[index01];
      boolean blocked11 = effect.blockedSamples[index11];
      boolean blocked10 = effect.blockedSamples[index10];
      if (blocked00 && blocked01 && blocked11 && blocked10) {
         return 0;
      }

      float height00 = effect.sampleHeights[index00];
      float height01 = effect.sampleHeights[index01];
      float height11 = effect.sampleHeights[index11];
      float height10 = effect.sampleHeights[index10];
      float weight00 = effect.elevatedWeights[index00];
      float weight01 = effect.elevatedWeights[index01];
      float weight11 = effect.elevatedWeights[index11];
      float weight10 = effect.elevatedWeights[index10];
      if (blocked00 || blocked01 || blocked11 || blocked10) {
         float maxUnblockedHeight = ALTERNATE_RING_SURFACE_INITIAL_UNBLOCKED_MAX_HEIGHT;
         if (!blocked00) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height00);
         }

         if (!blocked01) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height01);
         }

         if (!blocked11) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height11);
         }

         if (!blocked10) {
            maxUnblockedHeight = Math.max(maxUnblockedHeight, height10);
         }

         if (blocked00) {
            height00 = maxUnblockedHeight;
         }

         if (blocked01) {
            height01 = maxUnblockedHeight;
         }

         if (blocked11) {
            height11 = maxUnblockedHeight;
         }

         if (blocked10) {
            height10 = maxUnblockedHeight;
         }
      }

      float minHeight = Math.min(Math.min(height00, height01), Math.min(height11, height10));
      float maxHeight = Math.max(Math.max(height00, height01), Math.max(height11, height10));
      int subdivisionCount = Mth.clamp((int)Math.ceil((maxHeight - minHeight) / ALTERNATE_RING_SURFACE_SUBDIVISION_HEIGHT_STEP), 1, 8);
      boolean subdivideAlongX = Math.abs(height00 - height10) + Math.abs(height01 - height11) >= Math.abs(height00 - height01) + Math.abs(height10 - height11);
      int vertexCount = 0;

      for (int subdivision = 0; subdivision < subdivisionCount; subdivision++) {
         float subdivisionStart = (float)subdivision / subdivisionCount;
         float subdivisionEnd = (float)(subdivision + 1) / subdivisionCount;
         if (subdivideAlongX) {
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionStart, 0.0F, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionStart, 1.0F, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionEnd, 1.0F, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, subdivisionEnd, 0.0F, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
         } else {
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 0.0F, subdivisionStart, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 0.0F, subdivisionEnd, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 1.0F, subdivisionEnd, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
            this.appendInterpolatedAlternateRingVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, 1.0F, subdivisionStart, height00, height01, height11, height10, weight00, weight01, weight11, weight10);
         }

         vertexCount += 4;
      }

      return vertexCount;
   }

   private static boolean isBoundedFiniteCoordinate(double value) {
      return Double.isFinite(value) && Math.abs(value) <= MAX_FINITE_WORLD_COORDINATE;
   }

   private void renderAlternateRingSurfaceEffects(WorldEffectsEvent event) {
      if (!this.alternateRingSurfaceEffects.isEmpty()) {
         long nowMillis = System.currentTimeMillis();
         long lifetimeMillis = this.getAlternateRingLifetimeMillis();
         this.alternateRingSurfaceEffects.removeIf(effectToPrune -> nowMillis - effectToPrune.createdAtMillis >= lifetimeMillis || effectToPrune.isFadeOutComplete(nowMillis));
         if (!this.alternateRingSurfaceEffects.isEmpty()) {
            if (this.alternateRingTexture == null) {
               this.alternateRingTexture = MINECRAFT_CLIENT.getTextureManager().getTexture(ALTERNATE_RING_TEXTURE);
            }

            for (int angularColorIndex = 0; angularColorIndex <= 48; angularColorIndex++) {
               this.angularThemeColors[angularColorIndex] = ColorMath.getAnimatedThemeColor((int)(angularColorIndex * ALTERNATE_RING_THEME_PHASE_DEGREES / ALTERNATE_RING_THEME_COLOR_COUNT));
            }

            Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            Matrix4f matrix = event.getPoses().last().pose();
            BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int vertexCount = 0;

            for (JumpCircle.AlternateRingSurfaceEffect effect : this.alternateRingSurfaceEffects) {
               float progress = Mth.clamp((float)(nowMillis - effect.createdAtMillis) / (float)lifetimeMillis, 0.0F, 1.0F);
               float radius = easeOutCubic(progress) * effect.radius;
               float opacity = (float)Math.sin(progress * ALTERNATE_RING_ALPHA_ENVELOPE_RADIANS) * effect.getFadeOutAlpha(nowMillis);
               if (!(radius <= ALTERNATE_RING_MIN_VISIBLE_RADIUS) && !(opacity <= ALTERNATE_RING_MIN_VISIBLE_ALPHA)) {
                  double centerX = effect.origin.x;
                  double centerZ = effect.origin.z;
                  double radiusSquared = radius * radius;
                  int minGridX = Math.max(0, (int)Math.floor((centerX - radius - effect.gridOriginX) / effect.gridStep));
                  int minGridZ = Math.max(0, (int)Math.floor((centerZ - radius - effect.gridOriginZ) / effect.gridStep));
                  int maxGridX = Math.min(effect.gridSize - 2, (int)Math.ceil((centerX + radius - effect.gridOriginX) / effect.gridStep));
                  int maxGridZ = Math.min(effect.gridSize - 2, (int)Math.ceil((centerZ + radius - effect.gridOriginZ) / effect.gridStep));

                  for (int gridX = minGridX; gridX <= maxGridX; gridX++) {
                     double cellX = effect.gridOriginX + gridX * effect.gridStep;

                     for (int gridZ = minGridZ; gridZ <= maxGridZ; gridZ++) {
                        double cellZ = effect.gridOriginZ + gridZ * effect.gridStep;
                        double nearestRelativeX = Mth.clamp(centerX, cellX, cellX + effect.gridStep) - centerX;
                        double nearestRelativeZ = Mth.clamp(centerZ, cellZ, cellZ + effect.gridStep) - centerZ;
                        if (!(nearestRelativeX * nearestRelativeX + nearestRelativeZ * nearestRelativeZ >= radiusSquared)) {
                           vertexCount += this.appendAlternateRingSurfaceCell(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ);
                        }
                     }
                  }

                  float animationSeconds = (float)(nowMillis - effect.createdAtMillis) / ALTERNATE_RING_MILLIS_PER_SECOND;

                  for (float[] wallSegment : effect.wallSegments) {
                     vertexCount += this.appendAlternateRingWallGlow(builder, matrix, effect, cameraPosition, radius, opacity, animationSeconds, wallSegment);
                  }
               }
            }

            this.drawAlternateRingSurfaceMesh(builder, vertexCount);
         }
      }
   }

   private static int scaleRgb(int colorArgb, float scale) {
      scale = Mth.clamp(scale, 0.0F, 1.0F);
      return ColorMath.packArgb(
         (int)(ColorMath.getRed(colorArgb) * scale), (int)(ColorMath.getGreen(colorArgb) * scale), (int)(ColorMath.getBlue(colorArgb) * scale), ColorMath.getAlpha(colorArgb)
      );
   }

   private float smootherstep(float value) {
      return value * value * value * (value * (value * SMOOTHERSTEP_QUINTIC_COEFFICIENT - SMOOTHERSTEP_QUARTIC_COEFFICIENT) + SMOOTHERSTEP_CUBIC_COEFFICIENT);
   }

   private void renderFlexibleWaves(WorldEffectsEvent event) {
      float lifetimeMillis = Math.max(1.0F, this.flexibleLifetimeMillisSetting.getValue());
      long nowMillis = System.currentTimeMillis();
      this.flexibleWaveEffects.removeIf(effectToPrune -> (float)(nowMillis - effectToPrune.createdAtMillis) / lifetimeMillis > 1.0F || !isBoundedFinitePosition(effectToPrune.position));
      if (!this.flexibleWaveEffects.isEmpty()) {
         this.renderFlexibleWavePass(event, lifetimeMillis);
      }
   }

   private static boolean isFullCollisionBlock(VoxelShape shape) {
      return !shape.isEmpty() && net.minecraft.world.level.block.Block.isShapeFullBlock(shape);
   }

   private void appendColoredHorizontalQuad(
      BufferBuilder builder, Matrix4f matrix, Vec3 center, Vec3 cameraPosition, float halfSize, float heightOffset, float rotationRadians, Vector4f corner0Rgba, Vector4f corner1Rgba, Vector4f corner2Rgba, Vector4f corner3Rgba
   ) {
      float cosRotation = Mth.cos(rotationRadians);
      float sinRotation = Mth.sin(rotationRadians);
      float relativeY = (float)(center.y - cameraPosition.y) + LEECH_DEEP_LIGHT_SURFACE_OFFSET + heightOffset;
      this.appendRotatedVertexAtHeight(builder, matrix, center, cameraPosition, -halfSize, -halfSize, relativeY, cosRotation, sinRotation, 0.0F, 0.0F, corner0Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, center, cameraPosition, -halfSize, halfSize, relativeY, cosRotation, sinRotation, 0.0F, 1.0F, corner1Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, center, cameraPosition, halfSize, halfSize, relativeY, cosRotation, sinRotation, 1.0F, 1.0F, corner2Rgba);
      this.appendRotatedVertexAtHeight(builder, matrix, center, cameraPosition, halfSize, -halfSize, relativeY, cosRotation, sinRotation, 1.0F, 0.0F, corner3Rgba);
   }

   private static float easeOutCubic(float value) {
      float remaining = 1.0F - value;
      return 1.0F - remaining * remaining * remaining;
   }

   private void renderFlexibleWavePass(WorldEffectsEvent event, float lifetimeMillis) {
      RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      if (renderTarget != null
         && renderTarget.getColorTexture() != null
         && renderTarget.getDepthTexture() != null
         && !renderTarget.getDepthTexture().isClosed()
         && renderTarget.width > 0
         && renderTarget.height > 0) {
         Vec3 cameraPosition = event.getCameraPosition();
         this.inverseViewProjectionMatrix.set(event.getProjectionMatrix()).mul(event.getViewMatrix()).invert();
         long nowMillis = System.currentTimeMillis();
         float waveStrength = FLEXIBLE_WAVE_BASE_STRENGTH * this.flexibleWaveStrengthSetting.getValue();
         float aspectRatio = (float)renderTarget.width / Math.max(1, renderTarget.height);
         boolean glowEnabled = this.flexibleGlowSetting.getValue();
         boolean singleColor = this.usesSingleFlexibleWaveColor();
         int singleColorArgb = singleColor ? this.getFlexibleWaveColor(0) : 0;
         this.ensureFlexibleSceneCopyTarget(renderTarget.width, renderTarget.height);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         encoder.copyTextureToTexture(renderTarget.getColorTexture(), this.flexibleSceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, renderTarget.width, renderTarget.height);

         try (MemoryStack stack = MemoryStack.stackPush()) {
            Std140Builder uniformBuilder = Std140Builder.onStack(stack, 1648);
            int waveCount = 0;
            float accumulatedColorPhase = 0.0F;
            float[] geometryValues = new float[128];
            float[] colorValues = new float[256];

            for (JumpCircle.FlexibleWaveEffect effect : this.flexibleWaveEffects) {
               if (waveCount >= 16) {
                  break;
               }

               float progress = (float)(nowMillis - effect.createdAtMillis) / lifetimeMillis;
               if (!(progress < 0.0F) && !(progress >= 1.0F) && isBoundedFinitePosition(effect.position)) {
                  float radiusScale = this.getFlexibleRadiusScale(progress);
                  float fadeInScale = Mth.clamp(progress / FLEXIBLE_WAVE_FADE_IN_PROGRESS_SPAN, 0.0F, 1.0F);
                  float fadeOutScale = Mth.clamp((1.0F - progress) / FLEXIBLE_WAVE_FADE_OUT_PROGRESS_SPAN, 0.0F, 1.0F);
                  float opacity = fadeInScale * fadeOutScale;
                  float baseRadius = effect.size * FLEXIBLE_WAVE_RADIUS_SCALE;
                  float thickness = Math.max(FLEXIBLE_WAVE_MIN_THICKNESS, baseRadius * this.flexibleWaveThicknessSetting.getValue() / 2.0F);
                  int geometryOffset = waveCount * 8;
                  geometryValues[geometryOffset] = (float)(effect.position.x - cameraPosition.x);
                  geometryValues[geometryOffset + 1] = (float)(effect.position.y - cameraPosition.y);
                  geometryValues[geometryOffset + 2] = (float)(effect.position.z - cameraPosition.z);
                  geometryValues[geometryOffset + 3] = radiusScale * baseRadius;
                  geometryValues[geometryOffset + 4] = thickness;
                  geometryValues[geometryOffset + 5] = waveStrength * opacity;
                  geometryValues[geometryOffset + 6] = baseRadius;
                  geometryValues[geometryOffset + 7] = opacity;
                  int colorOffset = waveCount * 16;
                  if (singleColor) {
                     writeOpaqueRgb(colorValues, colorOffset, singleColorArgb);
                     writeOpaqueRgb(colorValues, colorOffset + 4, singleColorArgb);
                     writeOpaqueRgb(colorValues, colorOffset + 8, singleColorArgb);
                     writeOpaqueRgb(colorValues, colorOffset + 12, singleColorArgb);
                  } else {
                     int colorPhase = (int)accumulatedColorPhase;
                     writeOpaqueRgb(colorValues, colorOffset, this.getFlexibleWaveColor(colorPhase));
                     writeOpaqueRgb(colorValues, colorOffset + 4, this.getFlexibleWaveColor(90 + colorPhase));
                     writeOpaqueRgb(colorValues, colorOffset + 8, this.getFlexibleWaveColor(180 + colorPhase));
                     writeOpaqueRgb(colorValues, colorOffset + 12, this.getFlexibleWaveColor(270 + colorPhase));
                  }

                  accumulatedColorPhase += FLEXIBLE_RING_COLOR_PHASE_STEP * (1.0F - progress);
                  waveCount++;
               }
            }

            if (waveCount == 0) {
               return;
            }

            uniformBuilder.putVec4(waveCount, aspectRatio, (float)(nowMillis % FLEXIBLE_TIME_WRAP_MILLIS) / FLEXIBLE_MILLIS_PER_SECOND, this.flexibleTurbulenceSetting.getValue() ? FLEXIBLE_TURBULENCE_STRENGTH_SCALE * this.flexibleTurbulenceStrengthSetting.getValue() : 0.0F);
            uniformBuilder.putVec4(Mth.clamp(this.flexibleColorStrengthSetting.getValue() / FLEXIBLE_COLOR_STRENGTH_PERCENT_SCALE, 0.0F, 1.0F), Mth.clamp(1.0F + this.flexibleSaturationSetting.getValue(), 0.0F, 2.0F), glowEnabled ? 1.0F : 0.0F, FLEXIBLE_GLOW_INTENSITY);
            uniformBuilder.putVec4(
               Mth.clamp(this.flexibleGlowHeightSetting.getValue() / FLEXIBLE_GLOW_HEIGHT_PERCENT_SCALE, FLEXIBLE_MIN_GLOW_HEIGHT_SCALE, FLEXIBLE_MAX_GLOW_HEIGHT_SCALE),
               Mth.clamp(this.flexibleGlowWidthSetting.getValue() / FLEXIBLE_GLOW_WIDTH_PERCENT_SCALE, FLEXIBLE_MIN_GLOW_WIDTH_SCALE, FLEXIBLE_MAX_GLOW_WIDTH_SCALE),
               Mth.clamp(this.flexibleGlowColorStrengthSetting.getValue() / FLEXIBLE_GLOW_COLOR_STRENGTH_PERCENT_SCALE, 0.0F, 1.0F),
               Mth.clamp(this.flexibleGlowOpacitySetting.getValue() / FLEXIBLE_GLOW_OPACITY_PERCENT_SCALE, 0.0F, 1.0F)
            );
            uniformBuilder.putMat4f(this.inverseViewProjectionMatrix);

            for (int geometryVectorIndex = 0; geometryVectorIndex < 32; geometryVectorIndex++) {
               int geometryVectorOffset = geometryVectorIndex * 4;
               uniformBuilder.putVec4(geometryValues[geometryVectorOffset], geometryValues[geometryVectorOffset + 1], geometryValues[geometryVectorOffset + 2], geometryValues[geometryVectorOffset + 3]);
            }

            for (int colorVectorIndex = 0; colorVectorIndex < 64; colorVectorIndex++) {
               int colorVectorOffset = colorVectorIndex * 4;
               uniformBuilder.putVec4(colorValues[colorVectorOffset], colorValues[colorVectorOffset + 1], colorValues[colorVectorOffset + 2], colorValues[colorVectorOffset + 3]);
            }

            ByteBuffer uniformData = uniformBuilder.get();

            try (
               GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> FLEXIBLE_UNIFORM_BUFFER_LABEL, 128, uniformData);
               RenderPass renderPass = encoder.createRenderPass(() -> FLEXIBLE_WAVE_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty());
            ) {
               renderPass.setPipeline(this.flexibleWavePipeline);
               renderPass.bindTexture(FLEXIBLE_SCENE_SAMPLER_BINDING, this.flexibleSceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.bindTexture(FLEXIBLE_DEPTH_SAMPLER_BINDING, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               renderPass.setUniform(FLEXIBLE_UNIFORM_BINDING, uniformBuffer);
               renderPass.draw(3, 1, 0, 0);
            }
         }
      }
   }

   private void renderDiamondEffects(WorldEffectsEvent event) {
      if (!this.ringEffects.isEmpty()) {
         float partialTick = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         float rotationDegrees = ((float)MINECRAFT_CLIENT.level.getGameTime() + partialTick) * DIAMOND_ROTATION_DEGREES_PER_TICK;
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         Matrix4f matrix = event.getPoses().last().pose();
         BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int vertexCount = 0;
         Iterator effectIterator = this.ringEffects.iterator();

         while (effectIterator.hasNext()) {
            JumpCircle.RingEffect effect = (JumpCircle.RingEffect)effectIterator.next();
            long elapsedMillis = System.currentTimeMillis() - effect.createdAtMillis - effect.delayMillis;
            if (elapsedMillis >= 0L) {
               float progress = (float)elapsedMillis / DIAMOND_LIFETIME_MILLIS;
               if (progress >= 1.0F) {
                  effectIterator.remove();
               } else {
                  float fadeInProgress = Mth.clamp(progress / DIAMOND_FADE_IN_PROGRESS_SPAN, 0.0F, 1.0F);
                  float fadeOutProgress = Mth.clamp((progress - DIAMOND_FADE_OUT_START_PROGRESS) / DIAMOND_FADE_OUT_PROGRESS_SPAN, 0.0F, 1.0F);
                  float fadeInScale = this.smootherstep(fadeInProgress);
                  float fadeOutScale = this.smootherstep(fadeOutProgress);
                  float radiusGrowthScale = this.easeOutCubicRadius(Mth.clamp(progress / DIAMOND_RADIUS_GROWTH_PROGRESS_SPAN, 0.0F, 1.0F));
                  float primaryRadiusPulse = Mth.sin(fadeInProgress * DIAMOND_PRIMARY_PULSE_PHASE_RADIANS) * DIAMOND_PRIMARY_PULSE_RADIUS_SCALE;
                  float secondaryRadiusPulse = Mth.sin(Mth.clamp((progress - DIAMOND_SECONDARY_PULSE_START_PROGRESS) / DIAMOND_SECONDARY_PULSE_PROGRESS_SPAN, 0.0F, 1.0F) * DIAMOND_SECONDARY_PULSE_PHASE_RADIANS) * DIAMOND_SECONDARY_PULSE_RADIUS_SCALE;
                  float effectAlpha = fadeInScale * (1.0F - fadeOutScale) * effect.alphaScale;
                  float radiusScale = DIAMOND_INITIAL_RADIUS_SCALE + radiusGrowthScale * DIAMOND_RADIUS_GROWTH_SCALE + primaryRadiusPulse + secondaryRadiusPulse + fadeOutScale * DIAMOND_FADE_OUT_RADIUS_SCALE + effect.radiusOffset;
                  float settingRadius = this.radiusSetting.getValue() * DIAMOND_SETTING_RADIUS_SCALE;
                  float radius = settingRadius * radiusScale;
                  Vector4f baseRgba = ColorMath.toRgbaVector(this.getEffectColorArgb());
                  Vector4f rgba = new Vector4f(baseRgba.x, baseRgba.y, baseRgba.z, baseRgba.w * effectAlpha);
                  float rotationRadians = (float)Math.toRadians(rotationDegrees + progress * DIAMOND_PROGRESS_ROTATION_DEGREES + (float)effect.delayMillis * DIAMOND_DELAY_ROTATION_DEGREES_PER_MILLI);
                  this.appendHorizontalQuad(builder, matrix, effect.position, cameraPosition, radius, radius, rotationRadians, rgba);
                  vertexCount += 4;
               }
            }
         }

         this.drawProceduralEffectMesh(builder, vertexCount, this.diamondPipeline);
      }
   }

   private int appendAlternateRingWallGlow(BufferBuilder builder, Matrix4f matrix, JumpCircle.AlternateRingSurfaceEffect effect, Vec3 cameraPosition, float radius, float opacity, float time, float[] wallSegment) {
      double centerX = effect.origin.x;
      double centerZ = effect.origin.z;
      double nearestRelativeX = Mth.clamp(centerX, Math.min(wallSegment[0], wallSegment[2]), Math.max(wallSegment[0], wallSegment[2])) - centerX;
      double nearestRelativeZ = Mth.clamp(centerZ, Math.min(wallSegment[1], wallSegment[3]), Math.max(wallSegment[1], wallSegment[3])) - centerZ;
      if (nearestRelativeX * nearestRelativeX + nearestRelativeZ * nearestRelativeZ >= radius * radius * WALL_GLOW_RADIUS_CULL_SQUARED_SCALE) {
         return 0;
      }

      int vertexCount = 0;

      for (int horizontalSegment = 0; horizontalSegment < 4; horizontalSegment++) {
         float horizontalStart = horizontalSegment / WALL_GLOW_HORIZONTAL_START_SUBDIVISIONS;
         float horizontalEnd = (horizontalSegment + 1) / WALL_GLOW_HORIZONTAL_END_SUBDIVISIONS;

         for (int verticalSegment = 0; verticalSegment < 6; verticalSegment++) {
            float verticalStart = verticalSegment / WALL_GLOW_VERTICAL_START_SUBDIVISIONS;
            float verticalEnd = (verticalSegment + 1) / WALL_GLOW_VERTICAL_END_SUBDIVISIONS;
            this.appendAlternateRingWallGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, time, wallSegment, horizontalStart, verticalStart);
            this.appendAlternateRingWallGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, time, wallSegment, horizontalEnd, verticalStart);
            this.appendAlternateRingWallGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, time, wallSegment, horizontalEnd, verticalEnd);
            this.appendAlternateRingWallGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, time, wallSegment, horizontalStart, verticalEnd);
            vertexCount += 4;
         }
      }

      return vertexCount;
   }

   private void appendAlternateRingGlowVertex(
      BufferBuilder builder,
      Matrix4f matrix,
      JumpCircle.AlternateRingSurfaceEffect effect,
      Vec3 cameraPosition,
      float radius,
      float opacity,
      int gridX,
      int gridZ,
      float gridU,
      float gridV,
      float height,
      float elevatedWeight
   ) {
      double x = effect.gridOriginX + (gridX + gridU) * effect.gridStep;
      double z = effect.gridOriginZ + (gridZ + gridV) * effect.gridStep;
      double y = height + ALTERNATE_RING_GLOW_SURFACE_OFFSET;
      double relativeX = x - effect.origin.x;
      double relativeZ = z - effect.origin.z;
      float distance = (float)Math.sqrt(relativeX * relativeX + relativeZ * relativeZ);
      float normalizedRadius = distance / Math.max(ALTERNATE_RING_GLOW_MIN_RADIUS, radius);
      float clampedRadius = Math.min(normalizedRadius, 1.0F);
      float radialX = distance > ALTERNATE_RING_GLOW_RADIAL_X_EPSILON ? (float)(relativeX / distance) : 0.0F;
      float radialZ = distance > ALTERNATE_RING_GLOW_RADIAL_Z_EPSILON ? (float)(relativeZ / distance) : 0.0F;
      float u = ALTERNATE_RING_GLOW_U_CENTER + radialX * clampedRadius * ALTERNATE_RING_GLOW_U_RADIUS_SCALE;
      float v = ALTERNATE_RING_GLOW_V_CENTER + radialZ * clampedRadius * ALTERNATE_RING_GLOW_V_RADIUS_SCALE;
      float heightMask = 1.0F - smoothstep(ALTERNATE_RING_GLOW_HEIGHT_FADE_START, ALTERNATE_RING_GLOW_HEIGHT_FADE_END, height - (float)effect.origin.y);
      float radiusMask = (ALTERNATE_RING_GLOW_RADIAL_BASE + ALTERNATE_RING_GLOW_RADIAL_SCALE * clampedRadius) * (1.0F - smoothstep(ALTERNATE_RING_GLOW_RADIUS_FADE_START, 1.0F, normalizedRadius));
      float vertexAlpha = opacity * elevatedWeight * heightMask * radiusMask;
      float angleRadians = (float)Math.atan2(relativeZ, relativeX);
      int angularColorIndex = Math.floorMod(Math.round(angleRadians / ALTERNATE_RING_GLOW_FULL_TURN_RADIANS * ALTERNATE_RING_GLOW_ANGULAR_COLOR_COUNT), 48);
      int colorArgb = this.themeColorSetting.getValue() ? this.angularThemeColors[angularColorIndex] : this.getEffectColorArgb();
      this.appendVertex(builder, matrix, (float)(x - cameraPosition.x), (float)(y - cameraPosition.y), (float)(z - cameraPosition.z), u, v, ColorMath.toRgbaVector(scaleAlpha(colorArgb, vertexAlpha)));
   }

   private void detectJumpTrigger() {
      boolean onGround = MINECRAFT_CLIENT.player.onGround();
      boolean jumpKeyDown = MINECRAFT_CLIENT.options.keyJump.isDown();
      boolean jumpPressedOnGround = jumpKeyDown && !this.wasJumpKeyDown && onGround;
      boolean upwardTakeoff = this.wasOnGround && !onGround && MINECRAFT_CLIENT.player.getDeltaMovement().y > MIN_JUMP_UPWARD_VELOCITY;
      if (onGround && !this.wasOnGround) {
         this.jumpEffectTriggered = false;
      }

      if ((jumpPressedOnGround || upwardTakeoff) && !this.jumpEffectTriggered) {
         this.spawnConfiguredJumpEffect();
         this.jumpEffectTriggered = true;
      }

      this.wasOnGround = onGround;
      this.wasJumpKeyDown = jumpKeyDown;
   }

   private void limitActiveAlternateRingSurfaces() {
      int activeCount = 0;

      for (JumpCircle.AlternateRingSurfaceEffect effect : this.alternateRingSurfaceEffects) {
         if (!effect.fadingOut) {
            activeCount++;
         }
      }

      for (JumpCircle.AlternateRingSurfaceEffect oldestEffect : this.alternateRingSurfaceEffects) {
         if (activeCount < 8) {
            break;
         }

         if (!oldestEffect.fadingOut) {
            oldestEffect.beginFadeOut();
            activeCount--;
         }
      }
   }

   private static int getSurfaceGridSize(float radius) {
      float sampleRadius = radius + SURFACE_GRID_RADIUS_MARGIN;
      return Math.max(24, Math.min(56, (int)Math.ceil(SURFACE_GRID_DIAMETER_FACTOR * sampleRadius * SURFACE_GRID_SAMPLES_PER_BLOCK) + 1));
   }

   private void renderLeechEffects(WorldEffectsEvent event) {
      if (!this.leechEffects.isEmpty()) {
         long nowMillis = System.currentTimeMillis();
         float lifetimeMillis = this.leechLifetimeMillisSetting.getValue();
         this.leechEffects.removeIf(effectToPrune -> (float)(nowMillis - effectToPrune.createdAtMillis) / lifetimeMillis >= 1.0F);
         if (!this.leechEffects.isEmpty()) {
            Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            Matrix4f matrix = event.getPoses().last().pose();
            float configuredSize = this.leechSizeSetting.getValue();
            boolean deepLightEnabled = this.leechDeepLightSetting.getValue();
            String animationName = this.leechAnimationSetting.getSelectedOption().getName();
            HashMap<Integer, List<JumpCircle.LeechRenderSample>> samplesByFrame = new HashMap<>();
            ArrayList<JumpCircle.LeechRenderSample> deepLightSamples = new ArrayList<>();

            for (JumpCircle.LeechEffect effect : this.leechEffects) {
               float progress = (float)(nowMillis - effect.createdAtMillis) / lifetimeMillis;
               float remainingProgress = 1.0F - progress;
               float envelope = triangleEnvelope(1.0F - remainingProgress);
               float alpha;
               float size;
               float rotationDegrees;
               switch (animationName) {
                  case LEECH_FADE_IN_OUT_ROTATE_ANIMATION_KEY:
                     alpha = easeOutCircular(triangleEnvelope(1.0F - remainingProgress));
                     if (remainingProgress < LEECH_FADE_IN_OUT_ALPHA_SPLIT) {
                        alpha *= easeInOutExponential(alpha);
                     }

                     float fadeInOutProgress = envelope;
                     size = (remainingProgress > LEECH_FADE_IN_OUT_SIZE_SPLIT ? easeOutElastic(fadeInOutProgress * fadeInOutProgress) : easeOutBack(fadeInOutProgress)) * configuredSize;
                     rotationDegrees = (float)(easeInOutElastic(fadeInOutProgress) * LEECH_FADE_IN_OUT_ROTATION_DEGREES / (1.0 + fadeInOutProgress));
                     break;
                  case LEECH_FADE_IN_EXPONENT_ANIMATION_KEY:
                     alpha = easeInOutQuadratic(envelope);
                     float overshoot = LEECH_EXPONENT_BACK_OVERSHOOT;
                     float overshootPlusOne = overshoot + 1.0F;
                     size = (float)(1.0 + overshootPlusOne * Math.pow(remainingProgress - 1.0, LEECH_EXPONENT_CUBIC_POWER) + overshoot * Math.pow(remainingProgress - 1.0, LEECH_EXPONENT_QUADRATIC_POWER)) * configuredSize;
                     rotationDegrees = 0.0F;
                     break;
                  default:
                     alpha = progress < LEECH_FADE_OUT_ALPHA_SPLIT_PROGRESS ? easeInOutQuadratic(envelope) : envelope;
                     size = (1.0F - easeInOutQuadratic(progress)) * configuredSize;
                     rotationDegrees = (float)(easeInOutElastic(envelope) * LEECH_FADE_OUT_ROTATION_DEGREES);
               }

               if (!(alpha < LEECH_MIN_VISIBLE_ALPHA) && !(size < LEECH_MIN_VISIBLE_SIZE)) {
                  float framePhase = (remainingProgress + LEECH_FRAME_PHASE_OFFSET) % 1.0F;
                  if (framePhase < 0.0F) {
                     framePhase++;
                  }

                  int frameIndex = Mth.clamp((int)Math.min(framePhase * LEECH_FRAME_PHASE_SCALE, LEECH_LAST_FRAME_FLOAT_INDEX), 0, 199);
                  int colorPhaseOffset = effect.sequenceIndex * 30;
                  JumpCircle.LeechRenderSample sample = new JumpCircle.LeechRenderSample(effect.position, size, rotationDegrees, alpha, colorPhaseOffset, frameIndex);
                  samplesByFrame.computeIfAbsent(frameIndex, unusedFrameKey -> new ArrayList<>()).add(sample);
                  if (deepLightEnabled) {
                     deepLightSamples.add(sample);
                  }
               }
            }

            for (Entry<Integer, List<JumpCircle.LeechRenderSample>> frameEntry : samplesByFrame.entrySet()) {
               BufferBuilder frameBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
               int frameVertexCount = 0;

               for (JumpCircle.LeechRenderSample frameSample : frameEntry.getValue()) {
                  frameVertexCount += this.appendLeechQuad(frameBuilder, matrix, cameraPosition, frameSample);
               }

               this.drawTexturedEffectMesh(frameBuilder, LEECH_FRAME_TEXTURES[frameEntry.getKey()], frameVertexCount);
            }

            if (deepLightEnabled) {
               for (JumpCircle.LeechRenderSample deepLightSample : deepLightSamples) {
                  float baseRgbScale = LEECH_DEEP_LIGHT_RGB_SCALE;
                  float alphaEase = easeInOutQuadratic(deepLightSample.alpha);
                  float layerCount = Math.max(1.0F, LEECH_DEEP_LIGHT_LAYER_SCALE * alphaEase);
                  float maxLayerHeight = deepLightSample.size * alphaEase / LEECH_DEEP_LIGHT_HEIGHT_DIVISOR;
                  float radiusGrowth = deepLightSample.size / LEECH_DEEP_LIGHT_SIZE_DIVISOR;
                  float rotationRadians = (float)Math.toRadians(deepLightSample.rotationDegrees);
                  BufferBuilder deepLightBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                  int deepLightVertexCount = 0;
                  int deepLightColorPhase = deepLightSample.colorPhaseOffset;
                  int[] cornerColors = new int[]{this.getLeechColor(deepLightColorPhase, 1.0F), this.getLeechColor(deepLightColorPhase + 324, 1.0F), this.getLeechColor(deepLightColorPhase + 648, 1.0F), this.getLeechColor(deepLightColorPhase + 972, 1.0F)};

                  for (int layerIndex = 1; layerIndex < (int)layerCount; layerIndex++) {
                     float layerProgress = layerIndex / layerCount;
                     float layerRgbScale = Mth.lerp(layerProgress * ((layerCount - 1.0F) / layerCount), alphaEase * baseRgbScale, 0.0F);
                     if (!(layerRgbScale * LEECH_DEEP_LIGHT_RGB_BYTE_SCALE < 1.0F)) {
                        float layerSize = deepLightSample.size + easeOutCircular(triangleEnvelope(layerProgress - LEECH_DEEP_LIGHT_RADIUS_PHASE_OFFSET / layerCount)) * radiusGrowth;
                        float layerHeight = maxLayerHeight * layerIndex / layerCount - maxLayerHeight / layerCount;
                        this.appendColoredHorizontalQuad(
                           deepLightBuilder,
                           matrix,
                           deepLightSample.position,
                           cameraPosition,
                           layerSize * LEECH_DEEP_LIGHT_HALF_SIZE_FACTOR,
                           layerHeight,
                           rotationRadians,
                           ColorMath.toRgbaVector(scaleRgb(cornerColors[0], layerRgbScale)),
                           ColorMath.toRgbaVector(scaleRgb(cornerColors[1], layerRgbScale)),
                           ColorMath.toRgbaVector(scaleRgb(cornerColors[2], layerRgbScale)),
                           ColorMath.toRgbaVector(scaleRgb(cornerColors[3], layerRgbScale))
                        );
                        deepLightVertexCount += 4;
                     }
                  }

                  this.drawTexturedEffectMesh(deepLightBuilder, LEECH_FRAME_TEXTURES[deepLightSample.frameIndex], deepLightVertexCount);
               }
            }
         }
      }
   }

   private static float easeInOutQuadratic(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      return value < QUADRATIC_IN_OUT_MIDPOINT ? 2.0F * value * value : 1.0F - (float)Math.pow(QUADRATIC_IN_OUT_TAIL_SLOPE * value + 2.0F, QUADRATIC_IN_OUT_TAIL_POWER) / 2.0F;
   }

   private static Identifier[] createLeechFrameTextures() {
      Identifier[] textures = new Identifier[200];

      for (int frameIndex = 0; frameIndex < 200; frameIndex++) {
         textures[frameIndex] = Identifier.fromNamespaceAndPath(LEECH_FRAME_TEXTURE_NAMESPACE, "images/jumpcircles/leeches/frames/circleframe_" + (frameIndex + 1) + ".png");
      }

      return textures;
   }

   private void drawAlternateRingSurfaceMesh(BufferBuilder builder, int vertexCount) {
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
               vertexBuffer = RenderSystem.getDevice().createBuffer(() -> ALTERNATE_RING_VERTEX_BUFFER_LABEL, 32, mesh.vertexBuffer());
               RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture texture = this.alternateRingTexture != null ? this.alternateRingTexture : MINECRAFT_CLIENT.getTextureManager().getTexture(ALTERNATE_RING_TEXTURE);
               if (this.alternateRingGlassSetting.getValue()) {
                  TextureTarget sceneCopyTarget = this.getSharedSceneCopyTarget(renderTarget);
                  encoder.copyTextureToTexture(renderTarget.getColorTexture(), sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, renderTarget.width, renderTarget.height);

                  try (RenderPass glassPass = encoder.createRenderPass(
                        () -> ALTERNATE_RING_GLASS_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                     )) {
                     RenderSystem.bindDefaultUniforms(glassPass);
                     glassPass.bindTexture(ALTERNATE_RING_GLASS_SCENE_SAMPLER_BINDING, sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                     glassPass.setPipeline(this.alternateRingGlassPipeline);
                     glassPass.setVertexBuffer(0, vertexBuffer.slice());
                     glassPass.setIndexBuffer(indexBuffer, indexType);
                     glassPass.drawIndexed(indexCount, 1, 0, 0, 0);
                  }
               }

               try (RenderPass ringPass = encoder.createRenderPass(
                     () -> ALTERNATE_RING_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(ringPass);
                  ringPass.bindTexture(ALTERNATE_RING_TEXTURE_SAMPLER_BINDING, texture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                  ringPass.setPipeline(this.alternateRingPipeline);
                  ringPass.setVertexBuffer(0, vertexBuffer.slice());
                  ringPass.setIndexBuffer(indexBuffer, indexType);
                  ringPass.drawIndexed(indexCount, 1, 0, 0, 0);
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

   private void appendHorizontalQuad(BufferBuilder builder, Matrix4f matrix, Vec3 center, Vec3 cameraPosition, float halfWidth, float halfDepth, float rotationRadians, Vector4f rgba) {
      float cosRotation = Mth.cos(rotationRadians);
      float sinRotation = Mth.sin(rotationRadians);
      this.appendRotatedHorizontalVertex(builder, matrix, center, cameraPosition, -halfWidth, -halfDepth, cosRotation, sinRotation, 0.0F, 0.0F, rgba);
      this.appendRotatedHorizontalVertex(builder, matrix, center, cameraPosition, halfWidth, -halfDepth, cosRotation, sinRotation, 1.0F, 0.0F, rgba);
      this.appendRotatedHorizontalVertex(builder, matrix, center, cameraPosition, halfWidth, halfDepth, cosRotation, sinRotation, 1.0F, 1.0F, rgba);
      this.appendRotatedHorizontalVertex(builder, matrix, center, cameraPosition, -halfWidth, halfDepth, cosRotation, sinRotation, 0.0F, 1.0F, rgba);
   }

   private static int scaleAlpha(int colorArgb, float scale) {
      int alpha = Math.round((colorArgb >>> 24 & 0xFF) * scale);
      alpha = Mth.clamp(alpha, 0, 255);
      return alpha << 24 | colorArgb & RGB_MASK;
   }

   private float sampleCollisionHeight(Entity entity, double x, double z, double referenceY) {
      double rayStartY = referenceY + COLLISION_UPPER_REFERENCE_OFFSET + COLLISION_UPPER_PROBE_MARGIN;

      for (int attempt = 0; attempt < 4; attempt++) {
         BlockHitResult primaryHit = this.clipVerticalCollisionRay(entity, x, z, rayStartY, referenceY - COLLISION_PRIMARY_RAY_DEPTH);
         if (primaryHit.getType() != Type.BLOCK) {
            return (float)referenceY;
         }

         float hitY = (float)primaryHit.getLocation().y;
         if (primaryHit.isInside()) {
            double fallbackStartY = referenceY + 1.0;
            BlockHitResult fallbackHit = this.clipVerticalCollisionRay(entity, x, z, fallbackStartY, referenceY - COLLISION_RETRY_RAY_DEPTH);
            if (fallbackHit.getType() != Type.BLOCK) {
               return (float)referenceY;
            }

            float fallbackHitY = (float)fallbackHit.getLocation().y;
            boolean fallbackInsideBlock = fallbackHit.isInside();
            if (!fallbackInsideBlock && fallbackHitY > fallbackStartY - COLLISION_INSIDE_BLOCK_HEIGHT_EPSILON) {
               BlockPos fallbackBlockPosition = BlockPos.containing(x, fallbackStartY + COLLISION_INSIDE_BLOCK_POSITION_OFFSET, z);
               fallbackInsideBlock = !MINECRAFT_CLIENT.level.getBlockState(fallbackBlockPosition).getCollisionShape(MINECRAFT_CLIENT.level, fallbackBlockPosition).isEmpty();
            }

            if (fallbackInsideBlock) {
               return (float)(referenceY + COLLISION_BLOCKED_REFERENCE_OFFSET + COLLISION_BLOCKED_HEIGHT_SENTINEL_OFFSET);
            }

            primaryHit = fallbackHit;
            hitY = fallbackHitY;
         }

         BlockPos belowHitPosition = primaryHit.getBlockPos().below();
         boolean floatingHit = hitY - referenceY > COLLISION_FLOATING_HIT_HEIGHT_THRESHOLD && MINECRAFT_CLIENT.level.getBlockState(belowHitPosition).getCollisionShape(MINECRAFT_CLIENT.level, belowHitPosition).isEmpty();
         if (!floatingHit) {
            return this.adjustHeightToBlockOutline(primaryHit.getBlockPos(), x, z, hitY);
         }

         rayStartY = hitY - COLLISION_RETRY_START_MARGIN;
      }

      return (float)referenceY;
   }

   public void onPlayerTick(PlayerTickEvent event) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (this.ringTexture == null) {
            this.ringTexture = MINECRAFT_CLIENT.getTextureManager().getTexture(RING_TEXTURE);
         }

         this.detectJumpTrigger();
      } else {
         this.ringEffects.clear();
         this.lightningEffects.clear();
         this.leechEffects.clear();
         this.alternateRingSurfaceEffects.clear();
         this.flexibleWaveEffects.clear();
         this.wasOnGround = true;
         this.wasJumpKeyDown = false;
         this.jumpEffectTriggered = false;
      }
   }

   private int getFlexibleWaveColor(int phaseOffset) {
      if (this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_RAINBOW_COLOR_KEY)) {
         float rainbowHue = ((float)System.currentTimeMillis() / FLEXIBLE_RAINBOW_TIME_STEP_MILLIS + phaseOffset) % FLEXIBLE_RAINBOW_PHASE_WRAP / FLEXIBLE_RAINBOW_HUE_DIVISOR;
         if (rainbowHue < 0.0F) {
            rainbowHue++;
         }

         return Color.HSBtoRGB(rainbowHue, 1.0F, 1.0F) | FLEXIBLE_RAINBOW_OPAQUE_MASK;
      } else if (!this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_THEME_COLOR_KEY) && this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_CUSTOM_COLOR_KEY)) {
         int primaryColor = this.flexiblePrimaryColorSetting.getValue() | FLEXIBLE_PRIMARY_COLOR_OPAQUE_MASK;
         int secondaryColor = this.flexibleSecondColorEnabledSetting.getValue() ? this.flexibleSecondaryColorSetting.getValue() | FLEXIBLE_SECONDARY_COLOR_OPAQUE_MASK : primaryColor;
         if (primaryColor == secondaryColor) {
            return primaryColor;
         }

         int colorMixPhase = (int)((System.currentTimeMillis() / FLEXIBLE_CUSTOM_COLOR_TIME_STEP_MILLIS + phaseOffset) % FLEXIBLE_CUSTOM_COLOR_PHASE_WRAP);
         if (colorMixPhase >= 180) {
            colorMixPhase = 360 - colorMixPhase;
         }

         return ColorMath.blendArgb(primaryColor, secondaryColor, colorMixPhase / FLEXIBLE_CUSTOM_COLOR_MIX_DIVISOR);
      } else {
         return ColorMath.saturatedPingPongGradient(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), phaseOffset, 8);
      }
   }

   private static void spreadElevatedSurfaceSamples(float[] heights, float[] weights, boolean[] blockedSamples, int gridSize) {
      float[] originalHeights = (float[])heights.clone();
      float[] originalWeights = (float[])weights.clone();

      for (int gridX = 0; gridX < gridSize; gridX++) {
         for (int gridZ = 0; gridZ < gridSize; gridZ++) {
            int sampleIndex = gridX * gridSize + gridZ;
            if (!blockedSamples[sampleIndex]) {
               float height = originalHeights[sampleIndex];
               float weight = originalWeights[sampleIndex];

               for (int neighborOffsetX = -1; neighborOffsetX <= 1; neighborOffsetX++) {
                  for (int neighborOffsetZ = -1; neighborOffsetZ <= 1; neighborOffsetZ++) {
                     if (neighborOffsetX != 0 || neighborOffsetZ != 0) {
                        int neighborX = gridX + neighborOffsetX;
                        int neighborZ = gridZ + neighborOffsetZ;
                        if (neighborX >= 0 && neighborZ >= 0 && neighborX < gridSize && neighborZ < gridSize) {
                           int neighborIndex = neighborX * gridSize + neighborZ;
                           if (!blockedSamples[neighborIndex] && originalHeights[neighborIndex] > height + ELEVATED_SAMPLE_HEIGHT_THRESHOLD) {
                              height = originalHeights[neighborIndex];
                              weight = Math.max(weight, originalWeights[neighborIndex]);
                           }
                        }
                     }
                  }
               }

               heights[sampleIndex] = height;
               weights[sampleIndex] = weight;
            }
         }
      }
   }

   private void drawProceduralEffectMesh(BufferBuilder builder, int vertexCount, RenderPipeline pipeline) {
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
               vertexBuffer = RenderSystem.getDevice().createBuffer(() -> PROCEDURAL_EFFECT_VERTEX_BUFFER_LABEL, 32, mesh.vertexBuffer());
               RenderTarget renderTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

               try (RenderPass renderPass = encoder.createRenderPass(
                     () -> PROCEDURAL_EFFECT_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(renderPass);
                  renderPass.setPipeline(pipeline);
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

   private long getAlternateRingLifetimeMillis() {
      return Math.round(this.alternateRingLifetimeStepsSetting.getValue()) * ALTERNATE_RING_LIFETIME_STEP_MILLIS;
   }

   private static float easeOutElastic(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      if (value != 0.0F && value != 1.0F) {
         float periodRadians = ELASTIC_OUT_PERIOD_RADIANS;
         return (float)(Math.pow(ELASTIC_OUT_EXPONENT_BASE, ELASTIC_OUT_EXPONENT_SCALE * value) * Math.sin((value * ELASTIC_OUT_PHASE_SCALE - ELASTIC_OUT_PHASE_OFFSET) * periodRadians) + 1.0);
      } else {
         return value;
      }
   }

   private void appendRotatedVertexAtHeight(
      BufferBuilder builder,
      Matrix4f matrix,
      Vec3 center,
      Vec3 cameraPosition,
      float localX,
      float localZ,
      float relativeY,
      float cosRotation,
      float sinRotation,
      float u,
      float v,
      Vector4f rgba
   ) {
      float rotatedX = localX * cosRotation - localZ * sinRotation;
      float rotatedZ = localX * sinRotation + localZ * cosRotation;
      this.appendVertex(builder, matrix, (float)(center.x - cameraPosition.x) + rotatedX, relativeY, (float)(center.z - cameraPosition.z) + rotatedZ, u, v, rgba);
   }

   private static float sampleTurbulence(double x, double y, double z, float time) {
      float wave0 = Mth.sin((float)(x * TURBULENCE_WAVE0_X_FREQUENCY + z * TURBULENCE_WAVE0_Z_FREQUENCY + y * TURBULENCE_WAVE0_Y_FREQUENCY) + time * TURBULENCE_WAVE0_TIME_FREQUENCY);
      float wave1 = Mth.sin((float)(x * TURBULENCE_WAVE1_X_FREQUENCY - z * TURBULENCE_WAVE1_Z_FREQUENCY + y * TURBULENCE_WAVE1_Y_FREQUENCY) - time * TURBULENCE_WAVE1_TIME_FREQUENCY);
      float wave2 = Mth.sin((float)(x * TURBULENCE_WAVE2_X_FREQUENCY + z * TURBULENCE_WAVE2_Z_FREQUENCY - y * TURBULENCE_WAVE2_Y_FREQUENCY) + time * TURBULENCE_WAVE2_TIME_FREQUENCY);
      return (wave0 + wave1 + wave2) / TURBULENCE_WAVE_COUNT;
   }

   private void appendVertex(BufferBuilder builder, Matrix4f matrix, float x, float y, float z, float u, float v, Vector4f rgba) {
      builder.addVertex(matrix, x, y, z).setUv(u, v).setColor(rgba.x, rgba.y, rgba.z, rgba.w);
   }

   private static float triangleEnvelope(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      return value > TRIANGLE_ENVELOPE_MIDPOINT ? (1.0F - value) * 2.0F : value * 2.0F;
   }

   private void appendInterpolatedAlternateRingVertex(
      BufferBuilder builder,
      Matrix4f matrix,
      JumpCircle.AlternateRingSurfaceEffect effect,
      Vec3 cameraPosition,
      float radius,
      float opacity,
      int gridX,
      int gridZ,
      float gridU,
      float gridV,
      float height00,
      float height01,
      float height11,
      float height10,
      float weight00,
      float weight01,
      float weight11,
      float weight10
   ) {
      float var19 = Mth.lerp(gridU, Mth.lerp(gridV, height00, height01), Mth.lerp(gridV, height10, height11));
      float interpolatedHeight = Mth.lerp(gridU, Mth.lerp(gridV, height00, height01), Mth.lerp(gridV, height10, height11));
      float interpolatedWeight = Mth.lerp(gridU, Mth.lerp(gridV, weight00, weight01), Mth.lerp(gridV, weight10, weight11));
      this.appendAlternateRingGlowVertex(builder, matrix, effect, cameraPosition, radius, opacity, gridX, gridZ, gridU, gridV, interpolatedHeight, interpolatedWeight);
   }

   private void spawnConfiguredJumpEffect() {
      long nowMillis = System.currentTimeMillis();
      if (nowMillis - this.lastEffectSpawnMillis >= EFFECT_SPAWN_COOLDOWN_MILLIS) {
         this.lastEffectSpawnMillis = nowMillis;
         Vec3 position = new Vec3(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY() + EFFECT_SPAWN_SURFACE_OFFSET, MINECRAFT_CLIENT.player.getZ());
         if (this.shapeSetting.isSelected(SPAWN_LIGHTNING_SHAPE_KEY)) {
            this.ringEffects.clear();
            this.leechEffects.clear();
            this.alternateRingSurfaceEffects.clear();
            this.flexibleWaveEffects.clear();
            this.lightningEffects.add(new JumpCircle.LightningEffect(position, this.radiusSetting.getValue()));
         } else if (this.shapeSetting.isSelected(SPAWN_LEECH_SHAPE_KEY)) {
            this.lightningEffects.clear();
            this.ringEffects.clear();
            this.alternateRingSurfaceEffects.clear();
            this.flexibleWaveEffects.clear();
            this.leechEffects.add(new JumpCircle.LeechEffect(position, this.leechEffects.size()));

            while (this.leechEffects.size() > 16) {
               this.leechEffects.removeFirst();
            }
         } else if (this.shapeSetting.isSelected(SPAWN_ALTERNATE_RING_SHAPE_KEY)) {
            this.lightningEffects.clear();
            this.ringEffects.clear();
            this.leechEffects.clear();
            this.flexibleWaveEffects.clear();
            this.alternateRingSurfaceEffects.add(this.createAlternateRingSurfaceEffect(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY(), MINECRAFT_CLIENT.player.getZ(), this.alternateRingRadiusSetting.getValue()));
            this.limitActiveAlternateRingSurfaces();
         } else if (this.shapeSetting.isSelected(SPAWN_FLEXIBLE_SHAPE_KEY)) {
            this.lightningEffects.clear();
            this.ringEffects.clear();
            this.leechEffects.clear();
            this.alternateRingSurfaceEffects.clear();
            this.flexibleWaveEffects.add(new JumpCircle.FlexibleWaveEffect(position, this.flexibleSizeSetting.getValue()));

            while (this.flexibleWaveEffects.size() > 16) {
               this.flexibleWaveEffects.removeFirst();
            }
         } else {
            this.lightningEffects.clear();
            this.leechEffects.clear();
            this.alternateRingSurfaceEffects.clear();
            this.flexibleWaveEffects.clear();
            this.ringEffects.add(new JumpCircle.RingEffect(position, 0L, 1.0F, 0.0F));
         }
      }
   }

   private TextureTarget getSharedSceneCopyTarget(RenderTarget renderTarget) {
      if (this.sharedSceneCopyTarget == null) {
         this.sharedSceneCopyTarget = new TextureTarget(SHARED_SCENE_TARGET_LABEL, renderTarget.width, renderTarget.height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.sharedSceneCopyTarget.width != renderTarget.width || this.sharedSceneCopyTarget.height != renderTarget.height) {
         this.sharedSceneCopyTarget.resize(renderTarget.width, renderTarget.height);
      }

      return this.sharedSceneCopyTarget;
   }

   private void appendRotatedHorizontalVertex(
      BufferBuilder builder, Matrix4f matrix, Vec3 center, Vec3 cameraPosition, float localX, float localZ, float cosRotation, float sinRotation, float u, float v, Vector4f rgba
   ) {
      float rotatedX = localX * cosRotation - localZ * sinRotation;
      float rotatedZ = localX * sinRotation + localZ * cosRotation;
      this.appendVertex(builder, matrix, (float)(center.x - cameraPosition.x) + rotatedX, (float)(center.y - cameraPosition.y) + HORIZONTAL_RING_SURFACE_OFFSET, (float)(center.z - cameraPosition.z) + rotatedZ, u, v, rgba);
   }

   private static float easeInOutExponential(float value) {
      value = Mth.clamp(value, 0.0F, 1.0F);
      if (value != 0.0F && value != 1.0F) {
         return value < EXPONENTIAL_IN_OUT_MIDPOINT ? (float)(Math.pow(EXPONENTIAL_IN_OUT_LEADING_EXPONENT_BASE, EXPONENTIAL_IN_OUT_LEADING_EXPONENT_SCALE * value - EXPONENTIAL_IN_OUT_LEADING_EXPONENT_OFFSET) / EXPONENTIAL_IN_OUT_LEADING_AMPLITUDE_DIVISOR) : (float)((EXPONENTIAL_IN_OUT_TRAILING_VALUE_OFFSET - Math.pow(EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_BASE, EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_SCALE * value + EXPONENTIAL_IN_OUT_TRAILING_EXPONENT_OFFSET)) / EXPONENTIAL_IN_OUT_TRAILING_AMPLITUDE_DIVISOR);
      } else {
         return value;
      }
   }

   private boolean usesSingleFlexibleWaveColor() {
      if (!this.flexibleWaveColorModeSetting.isSelected(FLEXIBLE_SINGLE_CUSTOM_COLOR_KEY)) {
         return false;
      } else {
         return !this.flexibleSecondColorEnabledSetting.getValue() ? true : this.flexiblePrimaryColorSetting.getValue() == this.flexibleSecondaryColorSetting.getValue();
      }
   }

   public void onWorldEffects(WorldEffectsEvent event) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         this.detectJumpTrigger();
         if (this.shapeSetting.isSelected(RENDER_LIGHTNING_SHAPE_KEY)) {
            this.renderLightningEffects(event);
         } else if (this.shapeSetting.isSelected(RENDER_DIAMOND_SHAPE_KEY)) {
            this.renderDiamondEffects(event);
         } else if (this.shapeSetting.isSelected(RENDER_ALTERNATE_RING_SHAPE_KEY)) {
            this.renderAlternateRingSurfaceEffects(event);
         } else if (this.shapeSetting.isSelected(RENDER_LEECH_SHAPE_KEY)) {
            this.renderLeechEffects(event);
         } else if (this.shapeSetting.isSelected(RENDER_FLEXIBLE_SHAPE_KEY)) {
            this.renderFlexibleWaves(event);
         } else {
            this.renderRingEffects(event);
         }
      } else {
         this.ringEffects.clear();
         this.lightningEffects.clear();
         this.leechEffects.clear();
         this.alternateRingSurfaceEffects.clear();
         this.flexibleWaveEffects.clear();
         this.wasOnGround = true;
         this.wasJumpKeyDown = false;
         this.jumpEffectTriggered = false;
      }
   }

   @Override
   public void onDisable() {
      this.ringEffects.clear();
      this.lightningEffects.clear();
      this.leechEffects.clear();
      this.alternateRingSurfaceEffects.clear();
      this.flexibleWaveEffects.clear();
      this.wasOnGround = true;
      this.wasJumpKeyDown = false;
      this.jumpEffectTriggered = false;
      this.lastEffectSpawnMillis = 0L;
      this.ringTexture = null;
      this.alternateRingTexture = null;
      if (this.sharedSceneCopyTarget != null) {
         this.sharedSceneCopyTarget.destroyBuffers();
         this.sharedSceneCopyTarget = null;
      }

      if (this.flexibleSceneCopyTarget != null) {
         this.flexibleSceneCopyTarget.destroyBuffers();
         this.flexibleSceneCopyTarget = null;
      }
   }

   @Override
   public void onEnable() {
      this.ringEffects.clear();
      this.lightningEffects.clear();
      this.leechEffects.clear();
      this.alternateRingSurfaceEffects.clear();
      this.flexibleWaveEffects.clear();
      this.wasOnGround = true;
      this.wasJumpKeyDown = false;
      this.jumpEffectTriggered = false;
      this.lastEffectSpawnMillis = 0L;
      this.ringTexture = null;
      this.alternateRingTexture = null;
   }

   private int appendLightningSegment(BufferBuilder builder, Matrix4f matrix, Vec3 effectOrigin, Vec3 cameraPosition, Vec3 start, Vec3 end, float startWidth, float endWidth, Vector4f rgba) {
      double deltaX = end.x - start.x;
      double deltaZ = end.z - start.z;
      double segmentLength = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
      if (segmentLength < LIGHTNING_MIN_SEGMENT_LENGTH) {
         return 0;
      }

      double perpendicularX = -deltaZ / segmentLength;
      double perpendicularZ = deltaX / segmentLength;
      double relativeOriginX = effectOrigin.x - cameraPosition.x;
      double relativeOriginY = effectOrigin.y - cameraPosition.y + LIGHTNING_SURFACE_OFFSET;
      double relativeOriginZ = effectOrigin.z - cameraPosition.z;
      float startPositiveX = (float)(relativeOriginX + start.x + perpendicularX * startWidth * LIGHTNING_START_POSITIVE_X_HALF_WIDTH);
      float startPositiveZ = (float)(relativeOriginZ + start.z + perpendicularZ * startWidth * LIGHTNING_START_POSITIVE_Z_HALF_WIDTH);
      float startNegativeX = (float)(relativeOriginX + start.x - perpendicularX * startWidth * LIGHTNING_START_NEGATIVE_X_HALF_WIDTH);
      float startNegativeZ = (float)(relativeOriginZ + start.z - perpendicularZ * startWidth * LIGHTNING_START_NEGATIVE_Z_HALF_WIDTH);
      float endPositiveX = (float)(relativeOriginX + end.x + perpendicularX * endWidth * LIGHTNING_END_POSITIVE_X_HALF_WIDTH);
      float endPositiveZ = (float)(relativeOriginZ + end.z + perpendicularZ * endWidth * LIGHTNING_END_POSITIVE_Z_HALF_WIDTH);
      float endNegativeX = (float)(relativeOriginX + end.x - perpendicularX * endWidth * LIGHTNING_END_NEGATIVE_X_HALF_WIDTH);
      float endNegativeZ = (float)(relativeOriginZ + end.z - perpendicularZ * endWidth * LIGHTNING_END_NEGATIVE_Z_HALF_WIDTH);
      float startY = (float)(relativeOriginY + start.y);
      float endY = (float)(relativeOriginY + end.y);
      builder.addVertex(matrix, startPositiveX, startY, startPositiveZ).setUv(0.0F, 0.0F).setColor(rgba.x, rgba.y, rgba.z, rgba.w);
      builder.addVertex(matrix, endPositiveX, endY, endPositiveZ).setUv(1.0F, 0.0F).setColor(rgba.x, rgba.y, rgba.z, rgba.w);
      builder.addVertex(matrix, endNegativeX, endY, endNegativeZ).setUv(1.0F, 1.0F).setColor(rgba.x, rgba.y, rgba.z, rgba.w);
      builder.addVertex(matrix, startNegativeX, startY, startNegativeZ).setUv(0.0F, 1.0F).setColor(rgba.x, rgba.y, rgba.z, rgba.w);
      return 4;
   }

   private float easeOutCubicRadius(float value) {
      float remaining = 1.0F - value;
      return 1.0F - remaining * remaining * remaining;
   }

   private int getLeechColor(int phaseOffset, float alpha) {
      String colorMode = this.leechColorModeSetting.getSelectedOption().getName();

      int colorArgb = switch (colorMode) {
         case LEECH_THEME_COLOR_KEY -> ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), alpha);
         case LEECH_CUSTOM_COLOR_KEY -> ColorMath.withAlpha(this.leechPrimaryColorSetting.getValue(), ColorMath.getAlpha(this.leechPrimaryColorSetting.getValue()) / LEECH_PRIMARY_ALPHA_BYTE_SCALE * alpha);
         case LEECH_CUSTOM_FADE_COLOR_KEY -> {
            int primaryColor = ColorMath.withAlpha(this.leechPrimaryColorSetting.getValue(), ColorMath.getAlpha(this.leechPrimaryColorSetting.getValue()) / LEECH_FADE_PRIMARY_ALPHA_BYTE_SCALE * alpha);
            int secondaryColor = ColorMath.withAlpha(this.leechSecondaryColorSetting.getValue(), ColorMath.getAlpha(this.leechSecondaryColorSetting.getValue()) / LEECH_FADE_SECONDARY_ALPHA_BYTE_SCALE * alpha);
            float mixProgress = (Mth.sin(phaseOffset * LEECH_COLOR_PHASE_SCALE + (float)System.currentTimeMillis() / LEECH_COLOR_PHASE_TIME_MILLIS) + 1.0F) * LEECH_COLOR_MIX_NORMALIZATION;
            yield ColorMath.lerpArgb(primaryColor, secondaryColor, mixProgress);
         }
         default -> {
            float rainbowHue = ((float)System.currentTimeMillis() / LEECH_RAINBOW_TIME_STEP_MILLIS + phaseOffset) % LEECH_RAINBOW_PHASE_WRAP / LEECH_RAINBOW_HUE_DIVISOR;
            yield ColorMath.withAlpha(Color.HSBtoRGB(rainbowHue, LEECH_RAINBOW_SATURATION, 1.0F) | LEECH_RAINBOW_OPAQUE_MASK, alpha);
         }
      };
      return ColorMath.lerpArgb(colorArgb, ColorMath.withAlpha(-1, ColorMath.getAlpha(colorArgb) / LEECH_HIGHLIGHT_ALPHA_BYTE_SCALE), LEECH_WHITE_HIGHLIGHT_MIX);
   }

   private static boolean isBoundedFinitePosition(Vec3 position) {
      return isBoundedFiniteCoordinate(position.x) && isBoundedFiniteCoordinate(position.y) && isBoundedFiniteCoordinate(position.z);
   }

   private float adjustHeightToBlockOutline(BlockPos blockPosition, double x, double z, float height) {
      double var7 = Mth.clamp(x - blockPosition.getX(), 0.0, OUTLINE_LOCAL_X_MAX);
      double var9 = Mth.clamp(z - blockPosition.getZ(), 0.0, OUTLINE_LOCAL_Z_MAX);

      for (BlockPos samplePosition : new BlockPos[]{blockPosition, blockPosition.above()}) {
         VoxelShape outlineShape = MINECRAFT_CLIENT.level.getBlockState(samplePosition).getShape(MINECRAFT_CLIENT.level, samplePosition);
         if (!outlineShape.isEmpty()) {
            double outlineTopY = samplePosition.getY() + outlineShape.bounds().maxY;
            if (outlineTopY > height && outlineTopY - height <= OUTLINE_MAX_HEIGHT_ADJUSTMENT) {
               height = (float)outlineTopY;
            }
         }
      }

      return height;
   }

   private static final class LeechEffect {
      private final @com.google.gson.annotations.SerializedName("я")  Vec3 position;
      private final @com.google.gson.annotations.SerializedName("еЮ")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Ъ0")  int sequenceIndex;

      private LeechEffect(Vec3 position, int sequenceIndex) {
         this.position = position;
         this.sequenceIndex = sequenceIndex;
      }
   }

   private static final class LightningEffect {
      private static final float Ъд = 1550.0F;
      private static final float ЪБ = 0.4F;
      private static final float ЪС = 0.58F;
      private static final float Ъз = 1.4F;
      private final @com.google.gson.annotations.SerializedName("Е")  Vec3 origin;
      private final @com.google.gson.annotations.SerializedName("Х3")  long createdAtMillis;
      private final @com.google.gson.annotations.SerializedName("Ъц")  float flickerPhase;
      private final @com.google.gson.annotations.SerializedName("сг")  List<JumpCircle.LightningEffect.LightningBranch> branches;
      private static final float FLICKER_PHASE_RANGE = 1000.0F;
      private static final double FULL_TURN_RADIANS = Math.PI * 2;
      private static final double MIN_ROOT_ANGLE_JITTER = -0.26;
      private static final double MAX_ROOT_ANGLE_JITTER = 0.26;
      private static final double ROOT_LENGTH_BASE_SCALE = 0.85;
      private static final double ROOT_LENGTH_RANDOM_SCALE = 0.7;
      private static final float FORK_PROBABILITY = 0.55F;
      private static final double FORK_ANGLE_BASE_OFFSET = 0.55;
      private static final double FORK_ANGLE_RANDOM_OFFSET = 0.45;
      private static final double FORK_LENGTH_BASE_SCALE = 0.3;
      private static final double FORK_LENGTH_RANDOM_SCALE = 0.25;
      private static final float MAX_FORK_REVEAL_DELAY_PROGRESS = 0.75F;
      private static final double MIN_SEGMENT_ANGLE_JITTER = -0.16;
      private static final double MAX_SEGMENT_ANGLE_JITTER = 0.16;
      private static final double SEGMENT_LENGTH_BASE_SCALE = 0.85;
      private static final double SEGMENT_LENGTH_RANDOM_SCALE = 0.3;
      private static final double MIN_TERRAIN_HEIGHT_OFFSET = -1.4F;
      private static final double MAX_TERRAIN_HEIGHT_OFFSET = 1.4F;

      private static Vec3 projectOffsetToTerrain(Vec3 origin, Vec3 offset) {
         if (MinecraftAccess.MINECRAFT_CLIENT.level == null) {
            return offset;
         }

         double worldX = origin.x + offset.x;
         double worldZ = origin.z + offset.z;
         int terrainHeight = MinecraftAccess.MINECRAFT_CLIENT.level.getHeight(Types.MOTION_BLOCKING, Mth.floor(worldX), Mth.floor(worldZ));
         double relativeY = Mth.clamp(terrainHeight - origin.y, MIN_TERRAIN_HEIGHT_OFFSET, MAX_TERRAIN_HEIGHT_OFFSET);
         return new Vec3(offset.x, relativeY, offset.z);
      }

      private static List<JumpCircle.LightningEffect.LightningBranch> createBranches(Vec3 origin, float radius) {
         ThreadLocalRandom random = ThreadLocalRandom.current();
         int branchCount = 7 + random.nextInt(3);
         ArrayList branches = new ArrayList(branchCount * 2);

         for (int branchIndex = 0; branchIndex < branchCount; branchIndex++) {
            double angleRadians = FULL_TURN_RADIANS / branchCount * branchIndex + random.nextDouble(MIN_ROOT_ANGLE_JITTER, MAX_ROOT_ANGLE_JITTER);
            double length = (ROOT_LENGTH_BASE_SCALE + random.nextDouble() * ROOT_LENGTH_RANDOM_SCALE) * radius;
            byte segmentCount = 6;
            Vec3[] points = createBranchPoints(origin, Vec3.ZERO, angleRadians, length, segmentCount, random);
            branches.add(new JumpCircle.LightningEffect.LightningBranch(points, 0.0F));
            if (points.length > 3 && random.nextFloat() < FORK_PROBABILITY) {
               int forkIndex = 2 + random.nextInt(points.length - 3);
               double forkAngleRadians = angleRadians + (random.nextBoolean() ? 1 : -1) * (FORK_ANGLE_BASE_OFFSET + random.nextDouble() * FORK_ANGLE_RANDOM_OFFSET);
               double forkLength = length * (FORK_LENGTH_BASE_SCALE + random.nextDouble() * FORK_LENGTH_RANDOM_SCALE);
               Vec3[] forkPoints = createBranchPoints(origin, points[forkIndex], forkAngleRadians, forkLength, 3, random);
               float revealDelayProgress = Mth.clamp((float)forkIndex / segmentCount, 0.0F, MAX_FORK_REVEAL_DELAY_PROGRESS);
               branches.add(new JumpCircle.LightningEffect.LightningBranch(forkPoints, revealDelayProgress));
            }
         }

         return branches;
      }

      private static Vec3[] createBranchPoints(Vec3 origin, Vec3 start, double angleRadians, double length, int segmentCount, ThreadLocalRandom random) {
         Vec3[] points = new Vec3[segmentCount + 1];
         points[0] = projectOffsetToTerrain(origin, start);
         double currentAngleRadians = angleRadians;
         double segmentLength = length / segmentCount;
         double x = start.x;
         double z = start.z;

         for (int segmentIndex = 1; segmentIndex <= segmentCount; segmentIndex++) {
            currentAngleRadians += random.nextDouble(MIN_SEGMENT_ANGLE_JITTER, MAX_SEGMENT_ANGLE_JITTER);
            double stepLength = segmentLength * (SEGMENT_LENGTH_BASE_SCALE + random.nextDouble() * SEGMENT_LENGTH_RANDOM_SCALE);
            x += Math.cos(currentAngleRadians) * stepLength;
            z += Math.sin(currentAngleRadians) * stepLength;
            points[segmentIndex] = projectOffsetToTerrain(origin, new Vec3(x, 0.0, z));
         }

         return points;
      }

      private LightningEffect(Vec3 origin, float radius) {
         this.origin = origin;
         this.createdAtMillis = System.currentTimeMillis();
         this.flickerPhase = ThreadLocalRandom.current().nextFloat() * FLICKER_PHASE_RANGE;
         this.branches = createBranches(origin, radius);
      }

      private static final class LightningBranch {
         private final @com.google.gson.annotations.SerializedName("В")  Vec3[] points;
         @SerializedName("4ъ")
         private final float revealDelayProgress;

         private LightningBranch(Vec3[] points, float revealDelayProgress) {
            this.points = points;
            this.revealDelayProgress = revealDelayProgress;
         }
      }
   }

   private static final class LeechRenderSample {
      private final @com.google.gson.annotations.SerializedName("л")  Vec3 position;
      private final @com.google.gson.annotations.SerializedName("ег")  float size;
      private final @com.google.gson.annotations.SerializedName("е0")  float rotationDegrees;
      private final @com.google.gson.annotations.SerializedName("ех")  float alpha;
      private final @com.google.gson.annotations.SerializedName("ХЖ")  int colorPhaseOffset;
      private final @com.google.gson.annotations.SerializedName("Хм")  int frameIndex;

      private LeechRenderSample(Vec3 position, float size, float rotationDegrees, float alpha, int colorPhaseOffset, int frameIndex) {
         this.position = position;
         this.size = size;
         this.rotationDegrees = rotationDegrees;
         this.alpha = alpha;
         this.colorPhaseOffset = colorPhaseOffset;
         this.frameIndex = frameIndex;
      }
   }

   private static final class RingEffect {
      private final @com.google.gson.annotations.SerializedName("Ц")  Vec3 position;
      private final @com.google.gson.annotations.SerializedName("В1")  long createdAtMillis;
      private final @com.google.gson.annotations.SerializedName("ВЬ")  long delayMillis;
      private final @com.google.gson.annotations.SerializedName("ен")  float alphaScale;
      private final @com.google.gson.annotations.SerializedName("еЦ")  float radiusOffset;

      private RingEffect(Vec3 position, long delayMillis, float alphaScale, float radiusOffset) {
         this.position = position;
         this.createdAtMillis = System.currentTimeMillis();
         this.delayMillis = delayMillis;
         this.alphaScale = alphaScale;
         this.radiusOffset = radiusOffset;
      }
   }

   private static final class FlexibleWaveEffect {
      @SerializedName("8")
      private final Vec3 position;
      private final @com.google.gson.annotations.SerializedName("Хс")  float size;
      private final @com.google.gson.annotations.SerializedName("сO")  long createdAtMillis = System.currentTimeMillis();

      private FlexibleWaveEffect(Vec3 position, float size) {
         this.position = position;
         this.size = size;
      }
   }

   private static final class AlternateRingSurfaceEffect {
      private final @com.google.gson.annotations.SerializedName("О")  Vec3 origin;
      private final @com.google.gson.annotations.SerializedName("п3")  float radius;
      private final @com.google.gson.annotations.SerializedName("ЯЮ")  double gridOriginX;
      private final @com.google.gson.annotations.SerializedName("ЯЧ")  double gridOriginZ;
      private final @com.google.gson.annotations.SerializedName("ЯЛ")  double gridStep;
      @SerializedName("4I")
      private final int gridSize;
      private final @com.google.gson.annotations.SerializedName("l")  float[] sampleHeights;
      @SerializedName("8")
      private final float[] elevatedWeights;
      private final @com.google.gson.annotations.SerializedName("Х")  boolean[] blockedSamples;
      private final @com.google.gson.annotations.SerializedName("сэ")  List<float[]> wallSegments;
      private final @com.google.gson.annotations.SerializedName("Хс")  long createdAtMillis = System.currentTimeMillis();
      private @com.google.gson.annotations.SerializedName("Хщ")  boolean fadingOut;
      private @com.google.gson.annotations.SerializedName("ХВ")  long fadeOutStartedAtMillis;
      private static final float FADE_OUT_DURATION_MILLIS_FLOAT = 350.0F;
      private static final long FADE_OUT_DURATION_MILLIS = 350L;

      private boolean isFadeOutComplete(long nowMillis) {
         return this.fadingOut && nowMillis - this.fadeOutStartedAtMillis >= FADE_OUT_DURATION_MILLIS;
      }

      private void beginFadeOut() {
         if (!this.fadingOut) {
            this.fadingOut = true;
            this.fadeOutStartedAtMillis = System.currentTimeMillis();
         }
      }

      private AlternateRingSurfaceEffect(
         Vec3 origin, float radius, double gridOriginX, double gridOriginZ, double gridStep, int gridSize, float[] sampleHeights, float[] elevatedWeights, boolean[] blockedSamples, List<float[]> wallSegments
      ) {
         this.origin = origin;
         this.radius = radius;
         this.gridOriginX = gridOriginX;
         this.gridOriginZ = gridOriginZ;
         this.gridStep = gridStep;
         this.gridSize = gridSize;
         this.sampleHeights = sampleHeights;
         this.elevatedWeights = elevatedWeights;
         this.blockedSamples = blockedSamples;
         this.wallSegments = wallSegments;
      }

      private float getFadeOutAlpha(long nowMillis) {
         if (!this.fadingOut) {
            return 1.0F;
         }

         float progress = Mth.clamp((float)(nowMillis - this.fadeOutStartedAtMillis) / FADE_OUT_DURATION_MILLIS_FLOAT, 0.0F, 1.0F);
         return 1.0F - progress;
      }
   }
}
