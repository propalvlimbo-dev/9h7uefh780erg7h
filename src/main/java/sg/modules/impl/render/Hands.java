package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector4f;
import sg.render.pipeline.HandsItemBloom;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class Hands extends ClientModule {
   private static final String FILL_MODE_LABEL = "Заливка";
   private static final String SHADER_MODE_LABEL = "Шейдер";
   private static final String MIRROR_MODE_LABEL = "Зеркало";
   private static final String SHADER_MIRROR_MODE_LABEL = "Шейдер + Зеркало";
   private static final String NONE_MODE_LABEL = "Nothing";
   private static final String FLAME_TRAIL_MODE_LABEL = "Обычный";
   private static final String ENERGY_TRAIL_MODE_LABEL = "Энергия";
   private static final String RIBBON_TRAIL_MODE_LABEL = "Ленты";
   public static volatile boolean captureActive;
   public static RenderTarget activeCaptureTarget;
   @SerializedName("5")
   private final SingleChoiceSetting effectModeSetting;
   @SerializedName("0")
   private final BooleanSetting useThemeColorsSetting;
   private final @com.google.gson.annotations.SerializedName("х")  BooleanSetting useItemColorsSetting;
   private final @com.google.gson.annotations.SerializedName("са")  FloatSetting mirrorBlendSetting;
   private final @com.google.gson.annotations.SerializedName("сЮ")  FloatSetting mirrorDistortionSetting;
   private final @com.google.gson.annotations.SerializedName("сЧ")  FloatSetting mirrorBlurStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("У")  BooleanSetting rainbowFillSetting;
   private final @com.google.gson.annotations.SerializedName("сЛ")  FloatSetting rainbowSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("с诶")  FloatSetting rainbowScaleSetting;
   private final @com.google.gson.annotations.SerializedName("衣")  ColorSetting fillColorSetting;
   private final @com.google.gson.annotations.SerializedName("с1")  FloatSetting fillOpacitySetting;
   private final @com.google.gson.annotations.SerializedName("З")  BooleanSetting preserveFillShadingSetting;
   private final @com.google.gson.annotations.SerializedName("сЬ")  FloatSetting fillShadingStrengthSetting;
   @SerializedName("в")
   private final SingleChoiceSetting shaderStyleSetting;
   private final @com.google.gson.annotations.SerializedName("ш")  ColorSetting shaderPrimaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("Е")  BooleanSetting secondShaderColorSetting;
   private final @com.google.gson.annotations.SerializedName("И")  ColorSetting shaderSecondaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("с衣")  FloatSetting shaderSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("сш")  FloatSetting shaderScaleSetting;
   private final @com.google.gson.annotations.SerializedName("сИ")  FloatSetting shaderGlowSetting;
   private final @com.google.gson.annotations.SerializedName("сь")  FloatSetting shaderQualitySetting;
   private final @com.google.gson.annotations.SerializedName("П")  BooleanSetting shaderStarsSetting;
   private final @com.google.gson.annotations.SerializedName("сж")  FloatSetting shaderOpacitySetting;
   private final @com.google.gson.annotations.SerializedName("西")  BooleanSetting preserveShaderShadingSetting;
   private final @com.google.gson.annotations.SerializedName("с弟")  FloatSetting shaderShadingStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("д")  BooleanSetting glowEnabledSetting;
   private final @com.google.gson.annotations.SerializedName("сб")  FloatSetting glowBlurStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("сЫ")  FloatSetting glowBrightnessSetting;
   private final @com.google.gson.annotations.SerializedName("ь")  ColorSetting glowPrimaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("ж")  ColorSetting glowSecondaryColorSetting;
   private final @com.google.gson.annotations.SerializedName("сl")  FloatSetting innerGlowStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("Б")  BooleanSetting outlineEnabledSetting;
   @SerializedName("弟")
   private final ColorSetting outlineColorSetting;
   private final @com.google.gson.annotations.SerializedName("с8")  FloatSetting outlineWidthSetting;
   private final @com.google.gson.annotations.SerializedName("С")  BooleanSetting trailEnabledSetting;
   private final @com.google.gson.annotations.SerializedName("ъ")  SingleChoiceSetting trailStyleSetting;
   private final @com.google.gson.annotations.SerializedName("с5")  FloatSetting energyTrailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("св")  FloatSetting energyTrailBrightnessSetting;
   private final @com.google.gson.annotations.SerializedName("съ")  FloatSetting energyTrailMotionSetting;
   private final @com.google.gson.annotations.SerializedName("сМ")  FloatSetting energyTrailPulseSetting;
   private final @com.google.gson.annotations.SerializedName("сН")  FloatSetting energyCameraTrailStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("сн")  FloatSetting ribbonTrailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("сЦ")  FloatSetting ribbonTrailWidthSetting;
   private final @com.google.gson.annotations.SerializedName("сч")  FloatSetting ribbonTrailBrightnessSetting;
   private final @com.google.gson.annotations.SerializedName("сЭ")  FloatSetting ribbonCameraTrailStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("с必")  FloatSetting ribbonRiseSetting;
   @SerializedName("сщ")
   private final FloatSetting ribbonBendSetting;
   @SerializedName("сГ")
   private final FloatSetting ribbonWaveSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("з")  BooleanSetting swingRibbonsSetting;
   @SerializedName("сл")
   private final FloatSetting swingRibbonLengthSetting;
   @SerializedName("сэ")
   private final FloatSetting swingRibbonStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("ц")  BooleanSetting eatingRibbonsSetting;
   @SerializedName("сю")
   private final FloatSetting eatingRibbonLengthSetting;
   @SerializedName("сЩ")
   private final FloatSetting eatingRibbonStrengthSetting;
   @SerializedName("су")
   private final FloatSetting flameTrailDecaySetting;
   @SerializedName("с2")
   private final FloatSetting flameTrailRiseSetting;
   private final @com.google.gson.annotations.SerializedName("сф")  FloatSetting flameTrailSwaySetting;
   private final @com.google.gson.annotations.SerializedName("си")  FloatSetting flameCameraTrailStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("с9")  FloatSetting flameTrailTurbulenceSetting;
   private final @com.google.gson.annotations.SerializedName("со")  FloatSetting flameTrailFlickerSetting;
   private final @com.google.gson.annotations.SerializedName("я")  BooleanSetting swingDecayBoostSetting;
   private final @com.google.gson.annotations.SerializedName("сO")  FloatSetting swingDecayBoostStrengthSetting;
   @SerializedName("弟")
   private TextureTarget captureTarget;
   private @com.google.gson.annotations.SerializedName("Вт")  boolean captureInProgress;
   private @com.google.gson.annotations.SerializedName("ск")  long activationTimeMillis;
   private static final String MODULE_NAME = "Hands";
   private static final String MODULE_DESCRIPTION = "Визуальные эффекты рук от первого лица";
   private static final String EFFECT_MODE_LABEL = "Режим";
   private static final String EFFECT_MODE_INITIAL_NONE_OPTION_LABEL = "Nothing";
   private static final String EFFECT_MODE_FILL_OPTION_LABEL = "Заливка";
   private static final String EFFECT_MODE_SHADER_OPTION_LABEL = "Шейдер";
   private static final String EFFECT_MODE_MIRROR_OPTION_LABEL = "Зеркало";
   private static final String EFFECT_MODE_SHADER_MIRROR_OPTION_LABEL = "Шейдер + Зеркало";
   private static final String EFFECT_MODE_FINAL_NONE_OPTION_LABEL = "Nothing";
   private static final String USE_THEME_COLORS_LABEL = "Авто цвет под тему";
   private static final String USE_ITEM_COLORS_LABEL = "Авто цвет под предмет";
   private static final String MIRROR_BLEND_LABEL = "Смешивание";
   private static final float MIRROR_BLEND_DEFAULT = 0.5F;
   private static final float MIRROR_BLEND_STEP = 0.01F;
   private static final String MIRROR_DISTORTION_LABEL = "Искажение зеркала";
   private static final float MIRROR_DISTORTION_STEP = 0.05F;
   private static final String MIRROR_BLUR_STRENGTH_LABEL = "Сила размытия";
   private static final float MIRROR_BLUR_STRENGTH_MIN = 0.1F;
   private static final float MIRROR_BLUR_STRENGTH_MAX = 3.0F;
   private static final float MIRROR_BLUR_STRENGTH_STEP = 0.05F;
   private static final String RAINBOW_FILL_LABEL = "Радужная";
   private static final String RAINBOW_SPEED_LABEL = "Скорость радуги";
   private static final float RAINBOW_SPEED_DEFAULT = 0.4F;
   private static final float RAINBOW_SPEED_STEP = 0.05F;
   private static final String RAINBOW_SCALE_LABEL = "Масштаб радуги";
   private static final float RAINBOW_SCALE_MIN = 0.2F;
   private static final float RAINBOW_SCALE_MAX = 3.0F;
   private static final float RAINBOW_SCALE_STEP = 0.1F;
   private static final String FILL_COLOR_LABEL = "Цвет заливки";
   private static final int FILL_COLOR_DEFAULT_ARGB = -48060;
   private static final String FILL_OPACITY_LABEL = "Прозрачность заливки";
   private static final float FILL_OPACITY_DEFAULT = 0.8F;
   private static final float FILL_OPACITY_STEP = 0.05F;
   private static final String PRESERVE_FILL_SHADING_LABEL = "Сохранить тени";
   private static final String FILL_SHADING_STRENGTH_LABEL = "Сила теней";
   private static final float FILL_SHADING_STRENGTH_DEFAULT = 0.3F;
   private static final float FILL_SHADING_STRENGTH_STEP = 0.05F;
   private static final String SHADER_STYLE_LABEL = "Стиль шейдера";
   private static final String SHADER_STYLE_SHADER_OPTION_LABEL = "Шейдер";
   private static final String SHADER_STYLE_HAND_SHADER_OPTION_LABEL = "HandShader";
   private static final String SHADER_STYLE_NEBULA_OPTION_LABEL = "Туманность";
   private static final String SHADER_STYLE_COSMOS_OPTION_LABEL = "Космос";
   private static final String SHADER_STYLE_GRADIENT_OPTION_LABEL = "Градиент";
   private static final String SHADER_STYLE_HOMIE_OPTION_LABEL = "Homie";
   private static final String SHADER_PRIMARY_COLOR_LABEL = "Цвет шейдера 1";
   private static final int SHADER_PRIMARY_COLOR_DEFAULT_ARGB = -3632385;
   private static final String SECOND_SHADER_COLOR_LABEL = "Второй цвет";
   private static final String SHADER_SECONDARY_COLOR_LABEL = "Цвет шейдера 2";
   private static final int SHADER_SECONDARY_COLOR_DEFAULT_ARGB = -11534136;
   private static final String SHADER_SPEED_LABEL = "Скорость шейдера";
   private static final float SHADER_SPEED_DEFAULT = 1.5F;
   private static final float SHADER_SPEED_MIN = 0.05F;
   private static final float SHADER_SPEED_MAX = 2.5F;
   private static final float SHADER_SPEED_STEP = 0.05F;
   private static final String SHADER_SCALE_LABEL = "Масштаб шейдера";
   private static final float SHADER_SCALE_DEFAULT = 1.5F;
   private static final float SHADER_SCALE_MIN = 0.2F;
   private static final float SHADER_SCALE_MAX = 5.0F;
   private static final float SHADER_SCALE_STEP = 0.1F;
   private static final String SHADER_GLOW_LABEL = "Свечение шейдера";
   private static final float SHADER_GLOW_DEFAULT = 1.2F;
   private static final float SHADER_GLOW_MAX = 3.0F;
   private static final float SHADER_GLOW_STEP = 0.05F;
   private static final String SHADER_QUALITY_LABEL = "Качество шейдера";
   private static final float SHADER_QUALITY_MAX = 3.0F;
   private static final String SHADER_STARS_LABEL = "Звёзды";
   private static final String SHADER_OPACITY_LABEL = "Прозрачность шейдера";
   private static final float SHADER_OPACITY_STEP = 0.05F;
   private static final String PRESERVE_SHADER_SHADING_LABEL = "Сохранить тени шейдера";
   private static final String SHADER_SHADING_STRENGTH_LABEL = "Сила теней шейдера";
   private static final float SHADER_SHADING_STRENGTH_DEFAULT = 0.5F;
   private static final float SHADER_SHADING_STRENGTH_STEP = 0.05F;
   private static final String GLOW_ENABLED_LABEL = "Глов";
   private static final String GLOW_BLUR_STRENGTH_LABEL = "Размытие";
   private static final float GLOW_BLUR_STRENGTH_DEFAULT = 3.0F;
   private static final float GLOW_BLUR_STRENGTH_MAX = 5.0F;
   private static final String GLOW_BRIGHTNESS_LABEL = "Яркость";
   private static final float GLOW_BRIGHTNESS_MIN = 0.5F;
   private static final float GLOW_BRIGHTNESS_MAX = 5.0F;
   private static final float GLOW_BRIGHTNESS_STEP = 0.1F;
   private static final String GLOW_PRIMARY_COLOR_LABEL = "Цвет глова 1";
   private static final String GLOW_SECONDARY_COLOR_LABEL = "Цвет глова 2";
   private static final int GLOW_SECONDARY_COLOR_DEFAULT_ARGB = -54784;
   private static final String INNER_GLOW_STRENGTH_LABEL = "Внутреннее свечение";
   private static final float INNER_GLOW_STRENGTH_DEFAULT = 0.65F;
   private static final float INNER_GLOW_STRENGTH_MAX = 1.5F;
   private static final float INNER_GLOW_STRENGTH_STEP = 0.05F;
   private static final String OUTLINE_ENABLED_LABEL = "Обводка";
   private static final String OUTLINE_COLOR_LABEL = "Цвет обводки";
   private static final String OUTLINE_WIDTH_LABEL = "Толщина обводки";
   private static final float OUTLINE_WIDTH_MIN = 0.5F;
   private static final float OUTLINE_WIDTH_STEP = 0.5F;
   private static final String TRAIL_ENABLED_LABEL = "Шлейф";
   private static final String TRAIL_STYLE_LABEL = "Режим шлейфа";
   private static final String TRAIL_STYLE_FLAME_OPTION_LABEL = "Обычный";
   private static final String TRAIL_STYLE_ENERGY_OPTION_LABEL = "Энергия";
   private static final String TRAIL_STYLE_RIBBON_OPTION_LABEL = "Ленты";
   private static final String ENERGY_TRAIL_LENGTH_LABEL = "Длина энергии";
   private static final float ENERGY_TRAIL_LENGTH_MIN = 0.4F;
   private static final float ENERGY_TRAIL_LENGTH_STEP = 0.05F;
   private static final String ENERGY_TRAIL_BRIGHTNESS_LABEL = "Яркость энергии";
   private static final float ENERGY_TRAIL_BRIGHTNESS_DEFAULT = 1.35F;
   private static final float ENERGY_TRAIL_BRIGHTNESS_MIN = 0.5F;
   private static final float ENERGY_TRAIL_BRIGHTNESS_MAX = 2.5F;
   private static final float ENERGY_TRAIL_BRIGHTNESS_STEP = 0.05F;
   private static final String ENERGY_TRAIL_MOTION_LABEL = "Движение энергии";
   private static final float ENERGY_TRAIL_MOTION_DEFAULT = 1.3F;
   private static final float ENERGY_TRAIL_MOTION_STEP = 0.05F;
   private static final String ENERGY_TRAIL_PULSE_LABEL = "Пульсация энергии";
   private static final float ENERGY_TRAIL_PULSE_STEP = 0.05F;
   private static final String ENERGY_CAMERA_TRAIL_STRENGTH_LABEL = "Шлейф за камерой";
   private static final float ENERGY_CAMERA_TRAIL_STRENGTH_DEFAULT = 0.05F;
   private static final float ENERGY_CAMERA_TRAIL_STRENGTH_STEP = 0.05F;
   private static final String RIBBON_TRAIL_LENGTH_LABEL = "Длина лент";
   private static final float RIBBON_TRAIL_LENGTH_DEFAULT = 0.3F;
   private static final float RIBBON_TRAIL_LENGTH_MIN = 0.15F;
   private static final float RIBBON_TRAIL_LENGTH_MAX = 3.0F;
   private static final float RIBBON_TRAIL_LENGTH_STEP = 0.05F;
   private static final String RIBBON_TRAIL_WIDTH_LABEL = "Ширина лент";
   private static final float RIBBON_TRAIL_WIDTH_DEFAULT = 0.55F;
   private static final float RIBBON_TRAIL_WIDTH_MIN = 0.25F;
   private static final float RIBBON_TRAIL_WIDTH_MAX = 1.5F;
   private static final float RIBBON_TRAIL_WIDTH_STEP = 0.05F;
   private static final String RIBBON_TRAIL_BRIGHTNESS_LABEL = "Яркость лент";
   private static final float RIBBON_TRAIL_BRIGHTNESS_MAX = 2.5F;
   private static final float RIBBON_TRAIL_BRIGHTNESS_STEP = 0.05F;
   private static final String RIBBON_CAMERA_TRAIL_STRENGTH_LABEL = "Сила лент за камерой";
   private static final float RIBBON_CAMERA_TRAIL_STRENGTH_DEFAULT = 0.1F;
   private static final float RIBBON_CAMERA_TRAIL_STRENGTH_STEP = 0.05F;
   private static final String RIBBON_RISE_LABEL = "Подъём лент";
   private static final float RIBBON_RISE_DEFAULT = 0.32F;
   private static final float RIBBON_RISE_STEP = 0.01F;
   private static final String RIBBON_BEND_LABEL = "Изгиб лент";
   private static final float RIBBON_BEND_STEP = 0.05F;
   private static final String RIBBON_WAVE_SPEED_LABEL = "Скорость волн";
   private static final float RIBBON_WAVE_SPEED_DEFAULT = 1.5F;
   private static final float RIBBON_WAVE_SPEED_MAX = 3.0F;
   private static final float RIBBON_WAVE_SPEED_STEP = 0.05F;
   private static final String SWING_RIBBONS_LABEL = "Ленты при ударе";
   private static final String SWING_RIBBON_LENGTH_LABEL = "Длина при ударе";
   private static final float SWING_RIBBON_LENGTH_MIN = 0.15F;
   private static final float SWING_RIBBON_LENGTH_MAX = 3.0F;
   private static final float SWING_RIBBON_LENGTH_STEP = 0.05F;
   private static final String SWING_RIBBON_STRENGTH_LABEL = "Сила при ударе";
   private static final float SWING_RIBBON_STRENGTH_DEFAULT = 1.15F;
   private static final float SWING_RIBBON_STRENGTH_MAX = 2.5F;
   private static final float SWING_RIBBON_STRENGTH_STEP = 0.05F;
   private static final String EATING_RIBBONS_LABEL = "Ленты при еде";
   private static final String EATING_RIBBON_LENGTH_LABEL = "Длина при еде";
   private static final float EATING_RIBBON_LENGTH_DEFAULT = 0.65F;
   private static final float EATING_RIBBON_LENGTH_MIN = 0.15F;
   private static final float EATING_RIBBON_LENGTH_MAX = 3.0F;
   private static final float EATING_RIBBON_LENGTH_STEP = 0.05F;
   private static final String EATING_RIBBON_STRENGTH_LABEL = "Сила при еде";
   private static final float EATING_RIBBON_STRENGTH_DEFAULT = 0.7F;
   private static final float EATING_RIBBON_STRENGTH_MAX = 2.5F;
   private static final float EATING_RIBBON_STRENGTH_STEP = 0.05F;
   private static final String FLAME_TRAIL_DECAY_LABEL = "Скорость затухания";
   private static final float FLAME_TRAIL_DECAY_DEFAULT = 0.04F;
   private static final float FLAME_TRAIL_DECAY_MIN = 0.002F;
   private static final float FLAME_TRAIL_DECAY_MAX = 0.2F;
   private static final float FLAME_TRAIL_DECAY_STEP = 0.002F;
   private static final String FLAME_TRAIL_RISE_LABEL = "Подъём";
   private static final float FLAME_TRAIL_RISE_DEFAULT = 0.2F;
   private static final float FLAME_TRAIL_RISE_MAX = 1.5F;
   private static final float FLAME_TRAIL_RISE_STEP = 0.05F;
   private static final String FLAME_TRAIL_SWAY_LABEL = "Качание";
   private static final float FLAME_TRAIL_SWAY_MAX = 0.2F;
   private static final float FLAME_TRAIL_SWAY_STEP = 0.005F;
   private static final String FLAME_CAMERA_TRAIL_STRENGTH_LABEL = "Шлейф от камеры";
   private static final float FLAME_CAMERA_TRAIL_STRENGTH_STEP = 0.05F;
   private static final String FLAME_TRAIL_TURBULENCE_LABEL = "Турбулентность";
   private static final float FLAME_TRAIL_TURBULENCE_DEFAULT = 0.02F;
   private static final float FLAME_TRAIL_TURBULENCE_MAX = 0.6F;
   private static final float FLAME_TRAIL_TURBULENCE_STEP = 0.01F;
   private static final String FLAME_TRAIL_FLICKER_LABEL = "Мерцание";
   private static final float FLAME_TRAIL_FLICKER_DEFAULT = 0.2F;
   private static final float FLAME_TRAIL_FLICKER_MAX = 0.2F;
   private static final float FLAME_TRAIL_FLICKER_STEP = 0.01F;
   private static final String SWING_DECAY_BOOST_LABEL = "Сдув при ударе";
   private static final String SWING_DECAY_BOOST_STRENGTH_LABEL = "Сила сдува";
   private static final float SWING_DECAY_BOOST_STRENGTH_DEFAULT = 4.0F;
   private static final float SWING_DECAY_BOOST_STRENGTH_MAX = 10.0F;
   private static final float SWING_DECAY_BOOST_STRENGTH_STEP = 0.5F;
   private static final String CAPTURE_TARGET_LABEL = "hands-capture";
   private static final String FILL_MODE_MATCH_LABEL = "Заливка";
   private static final String SHADER_MODE_MATCH_LABEL = "Шейдер";
   private static final String MIRROR_MODE_MATCH_LABEL = "Зеркало";
   private static final String SHADER_MIRROR_MODE_MATCH_LABEL = "Шейдер + Зеркало";
   private static final String NONE_MODE_MATCH_LABEL = "Nothing";
   private static final String SHADER_MODE_ID_LABEL = "Шейдер";
   private static final String MIRROR_MODE_ID_LABEL = "Зеркало";
   private static final String HAND_SHADER_STYLE_MATCH_LABEL = "HandShader";
   private static final String NEBULA_SHADER_STYLE_MATCH_LABEL = "Туманность";
   private static final String COSMOS_SHADER_STYLE_MATCH_LABEL = "Космос";
   private static final String GRADIENT_SHADER_STYLE_MATCH_LABEL = "Градиент";
   private static final String HOMIE_SHADER_STYLE_MATCH_LABEL = "Homie";
   private static final float MILLISECONDS_PER_SECOND = 1000.0F;
   private static final String FLAME_TRAIL_MODE_MATCH_LABEL = "Обычный";
   private static final String ENERGY_TRAIL_MODE_MATCH_LABEL = "Энергия";
   private static final String RIBBON_TRAIL_MODE_MATCH_LABEL = "Ленты";
   private static final String SHADER_QUALITY_HIDDEN_STYLE_LABEL = "Градиент";

   public float getRainbowSpeed() {
      return this.rainbowSpeedSetting.getValue();
   }

   public int getShaderIterationBudget() {
      int qualityLevel = Math.round(this.shaderQualitySetting.getValue());
      return qualityLevel == 1 ? 3 : (qualityLevel == 3 ? 8 : 5);
   }

   public int getPrimaryShaderColorArgb() {
      return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.shaderPrimaryColorSetting.getValue();
   }

   public float getShaderGlow() {
      return this.shaderGlowSetting.getValue();
   }

   public float getEnergyTrailPulse() {
      return this.energyTrailPulseSetting.getValue();
   }

   public float getRibbonTrailLength() {
      return this.ribbonTrailLengthSetting.getValue();
   }

   public float getMirrorBlend() {
      return this.mirrorBlendSetting.getValue();
   }

   public void beginCapture() {
      if (this.shouldCapture() && !this.captureInProgress && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         int captureWidth = Math.max(1, mainTarget.width);
         int captureHeight = Math.max(1, mainTarget.height);
         if (this.captureTarget == null) {
            this.captureTarget = new TextureTarget(CAPTURE_TARGET_LABEL, captureWidth, captureHeight, true, GpuFormat.RGBA8_UNORM);
         } else if (this.captureTarget.width != captureWidth || this.captureTarget.height != captureHeight) {
            this.captureTarget.resize(captureWidth, captureHeight);
         }

         RenderSystem.getDevice()
            .createCommandEncoder()
            .clearColorAndDepthTextures(this.captureTarget.getColorTexture(), new Vector4f(0.0F), this.captureTarget.getDepthTexture(), 0.0);
         activeCaptureTarget = this.captureTarget;
         captureActive = true;
         this.captureInProgress = true;
      }
   }

   public float getGlowBrightness() {
      return this.glowBrightnessSetting.getValue();
   }

   public float getSwingRibbonLength() {
      return this.swingRibbonLengthSetting.getValue();
   }

   public int getEffectModeId() {
      if (this.isNoneMode()) {
         return 4;
      } else {
         return this.isFillMode() ? 0 : (this.effectModeSetting.isSelected(SHADER_MODE_ID_LABEL) ? 1 : (this.effectModeSetting.isSelected(MIRROR_MODE_ID_LABEL) ? 2 : 3));
      }
   }

   public int getSecondaryShaderColorArgb() {
      if (this.useThemeColorsSetting.getValue()) {
         return this.secondShaderColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      } else {
         return this.secondShaderColorSetting.getValue() ? this.shaderSecondaryColorSetting.getValue() : this.shaderPrimaryColorSetting.getValue();
      }
   }

   public float getEatingRibbonLength() {
      return this.eatingRibbonLengthSetting.getValue();
   }

   public int getFillColorArgb() {
      return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.fillColorSetting.getValue();
   }

   public boolean isFillShadingPreserved() {
      return this.preserveFillShadingSetting.getValue();
   }

   public boolean isSwingRibbonsEnabled() {
      return this.swingRibbonsSetting.getValue();
   }

   public boolean isFillMode() {
      return this.effectModeSetting.isSelected(FILL_MODE_MATCH_LABEL);
   }

   public boolean isNoneMode() {
      return this.effectModeSetting.isSelected(NONE_MODE_MATCH_LABEL);
   }

   public boolean isOutlineEnabled() {
      return this.outlineEnabledSetting.getValue();
   }

   public void endCaptureAndComposite() {
      if (this.captureInProgress) {
         captureActive = false;
         activeCaptureTarget = null;
         this.captureInProgress = false;
         if (this.isEnabled() && this.captureTarget != null && MINECRAFT_CLIENT.gameRenderer.mainRenderTarget() != null) {
            HandsItemBloom.INSTANCE.compositeHands(this.captureTarget, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget(), this);
         }
      }
   }

   public boolean isShaderMode() {
      return this.effectModeSetting.isSelected(SHADER_MODE_MATCH_LABEL) || this.isShaderMirrorMode();
   }

   public boolean isShaderShadingPreserved() {
      return this.preserveShaderShadingSetting.getValue();
   }

   public float getGlowBlurStrength() {
      return this.glowBlurStrengthSetting.getValue();
   }

   public boolean isEnergyTrailEnabled() {
      return this.isTrailEnabled() && this.trailStyleSetting.isSelected(ENERGY_TRAIL_MODE_MATCH_LABEL);
   }

   public float getEnergyTrailBrightness() {
      return this.energyTrailBrightnessSetting.getValue();
   }

   public Hands() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.effectModeSetting = new SingleChoiceSetting(
         EFFECT_MODE_LABEL,
         new ChoiceOption(EFFECT_MODE_INITIAL_NONE_OPTION_LABEL),
         new ChoiceOption(EFFECT_MODE_FILL_OPTION_LABEL),
         new ChoiceOption(EFFECT_MODE_SHADER_OPTION_LABEL),
         new ChoiceOption(EFFECT_MODE_MIRROR_OPTION_LABEL),
         new ChoiceOption(EFFECT_MODE_SHADER_MIRROR_OPTION_LABEL),
         new ChoiceOption(EFFECT_MODE_FINAL_NONE_OPTION_LABEL)
      );
      this.useThemeColorsSetting = new BooleanSetting(USE_THEME_COLORS_LABEL, true);
      this.useItemColorsSetting = new BooleanSetting(USE_ITEM_COLORS_LABEL, false);
      this.mirrorBlendSetting = new FloatSetting(MIRROR_BLEND_LABEL, MIRROR_BLEND_DEFAULT, 0.0F, 1.0F).withStep(MIRROR_BLEND_STEP).visibleWhen(this::isMirrorMode);
      this.mirrorDistortionSetting = new FloatSetting(MIRROR_DISTORTION_LABEL, 0.0F, 0.0F, 1.0F).withStep(MIRROR_DISTORTION_STEP).visibleWhen(this::isMirrorMode);
      this.mirrorBlurStrengthSetting = new FloatSetting(MIRROR_BLUR_STRENGTH_LABEL, 1.0F, MIRROR_BLUR_STRENGTH_MIN, MIRROR_BLUR_STRENGTH_MAX).withStep(MIRROR_BLUR_STRENGTH_STEP).visibleWhen(this::isMirrorMode);
      this.rainbowFillSetting = new BooleanSetting(RAINBOW_FILL_LABEL, false).visibleWhen(this::isFillMode);
      this.rainbowSpeedSetting = new FloatSetting(RAINBOW_SPEED_LABEL, RAINBOW_SPEED_DEFAULT, 0.0F, 2.0F).withStep(RAINBOW_SPEED_STEP).visibleWhen(() -> this.isFillMode() && this.rainbowFillSetting.getValue());
      this.rainbowScaleSetting = new FloatSetting(RAINBOW_SCALE_LABEL, 1.0F, RAINBOW_SCALE_MIN, RAINBOW_SCALE_MAX).withStep(RAINBOW_SCALE_STEP).visibleWhen(() -> this.isFillMode() && this.rainbowFillSetting.getValue());
      this.fillColorSetting = new ColorSetting(FILL_COLOR_LABEL, false, FILL_COLOR_DEFAULT_ARGB).visibleWhen(() -> this.isFillMode() && !this.rainbowFillSetting.getValue() && !this.useThemeColorsSetting.getValue());
      this.fillOpacitySetting = new FloatSetting(FILL_OPACITY_LABEL, FILL_OPACITY_DEFAULT, 0.0F, 1.0F).withStep(FILL_OPACITY_STEP).visibleWhen(this::isFillMode);
      this.preserveFillShadingSetting = new BooleanSetting(PRESERVE_FILL_SHADING_LABEL, true).visibleWhen(this::isFillMode);
      this.fillShadingStrengthSetting = new FloatSetting(FILL_SHADING_STRENGTH_LABEL, FILL_SHADING_STRENGTH_DEFAULT, 0.0F, 1.0F).withStep(FILL_SHADING_STRENGTH_STEP).visibleWhen(() -> this.isFillMode() && this.preserveFillShadingSetting.getValue());
      this.shaderStyleSetting = new SingleChoiceSetting(
            SHADER_STYLE_LABEL,
            new ChoiceOption(SHADER_STYLE_SHADER_OPTION_LABEL),
            new ChoiceOption(SHADER_STYLE_HAND_SHADER_OPTION_LABEL),
            new ChoiceOption(SHADER_STYLE_NEBULA_OPTION_LABEL),
            new ChoiceOption(SHADER_STYLE_COSMOS_OPTION_LABEL),
            new ChoiceOption(SHADER_STYLE_GRADIENT_OPTION_LABEL),
            new ChoiceOption(SHADER_STYLE_HOMIE_OPTION_LABEL)
         )
         .visibleWhen(this::isShaderMode);
      this.shaderPrimaryColorSetting = new ColorSetting(SHADER_PRIMARY_COLOR_LABEL, false, SHADER_PRIMARY_COLOR_DEFAULT_ARGB).visibleWhen(() -> this.isShaderMode() && !this.useThemeColorsSetting.getValue());
      this.secondShaderColorSetting = new BooleanSetting(SECOND_SHADER_COLOR_LABEL, false).visibleWhen(this::isShaderMode);
      this.shaderSecondaryColorSetting = new ColorSetting(SHADER_SECONDARY_COLOR_LABEL, false, SHADER_SECONDARY_COLOR_DEFAULT_ARGB).visibleWhen(() -> this.isShaderMode() && this.secondShaderColorSetting.getValue() && !this.useThemeColorsSetting.getValue());
      this.shaderSpeedSetting = new FloatSetting(SHADER_SPEED_LABEL, SHADER_SPEED_DEFAULT, SHADER_SPEED_MIN, SHADER_SPEED_MAX).withStep(SHADER_SPEED_STEP).visibleWhen(this::isShaderMode);
      this.shaderScaleSetting = new FloatSetting(SHADER_SCALE_LABEL, SHADER_SCALE_DEFAULT, SHADER_SCALE_MIN, SHADER_SCALE_MAX).withStep(SHADER_SCALE_STEP).visibleWhen(this::isShaderMode);
      this.shaderGlowSetting = new FloatSetting(SHADER_GLOW_LABEL, SHADER_GLOW_DEFAULT, 0.0F, SHADER_GLOW_MAX).withStep(SHADER_GLOW_STEP).visibleWhen(this::isShaderMode);
      this.shaderQualitySetting = new FloatSetting(SHADER_QUALITY_LABEL, 2.0F, 1.0F, SHADER_QUALITY_MAX).withStep(1.0F).visibleWhen(() -> this.isShaderMode() && !this.shaderStyleSetting.isSelected(SHADER_QUALITY_HIDDEN_STYLE_LABEL));
      this.shaderStarsSetting = new BooleanSetting(SHADER_STARS_LABEL, true).visibleWhen(this::isShaderMode);
      this.shaderOpacitySetting = new FloatSetting(SHADER_OPACITY_LABEL, 1.0F, 0.0F, 1.0F).withStep(SHADER_OPACITY_STEP).visibleWhen(this::isShaderMode);
      this.preserveShaderShadingSetting = new BooleanSetting(PRESERVE_SHADER_SHADING_LABEL, false).visibleWhen(this::isShaderMode);
      this.shaderShadingStrengthSetting = new FloatSetting(SHADER_SHADING_STRENGTH_LABEL, SHADER_SHADING_STRENGTH_DEFAULT, 0.0F, 1.0F).withStep(SHADER_SHADING_STRENGTH_STEP).visibleWhen(() -> this.isShaderMode() && this.preserveShaderShadingSetting.getValue());
      this.glowEnabledSetting = new BooleanSetting(GLOW_ENABLED_LABEL, true);
      this.glowBlurStrengthSetting = new FloatSetting(GLOW_BLUR_STRENGTH_LABEL, GLOW_BLUR_STRENGTH_DEFAULT, 1.0F, GLOW_BLUR_STRENGTH_MAX).withStep(1.0F).visibleWhen(this.glowEnabledSetting::getValue);
      this.glowBrightnessSetting = new FloatSetting(GLOW_BRIGHTNESS_LABEL, 1.0F, GLOW_BRIGHTNESS_MIN, GLOW_BRIGHTNESS_MAX).withStep(GLOW_BRIGHTNESS_STEP).visibleWhen(this.glowEnabledSetting::getValue);
      this.glowPrimaryColorSetting = new ColorSetting(GLOW_PRIMARY_COLOR_LABEL, false, -11713).visibleWhen(() -> this.glowEnabledSetting.getValue() && !this.useThemeColorsSetting.getValue() && !this.useItemColorsSetting.getValue());
      this.glowSecondaryColorSetting = new ColorSetting(GLOW_SECONDARY_COLOR_LABEL, false, GLOW_SECONDARY_COLOR_DEFAULT_ARGB).visibleWhen(() -> this.glowEnabledSetting.getValue() && !this.useThemeColorsSetting.getValue() && !this.useItemColorsSetting.getValue());
      this.innerGlowStrengthSetting = new FloatSetting(INNER_GLOW_STRENGTH_LABEL, INNER_GLOW_STRENGTH_DEFAULT, 0.0F, INNER_GLOW_STRENGTH_MAX).withStep(INNER_GLOW_STRENGTH_STEP).visibleWhen(this.glowEnabledSetting::getValue);
      this.outlineEnabledSetting = new BooleanSetting(OUTLINE_ENABLED_LABEL, true);
      this.outlineColorSetting = new ColorSetting(OUTLINE_COLOR_LABEL, true, -1).visibleWhen(() -> this.outlineEnabledSetting.getValue() && !this.useThemeColorsSetting.getValue() && !this.useItemColorsSetting.getValue());
      this.outlineWidthSetting = new FloatSetting(OUTLINE_WIDTH_LABEL, 1.0F, OUTLINE_WIDTH_MIN, 2.0F).withStep(OUTLINE_WIDTH_STEP).visibleWhen(this.outlineEnabledSetting::getValue);
      this.trailEnabledSetting = new BooleanSetting(TRAIL_ENABLED_LABEL, true).visibleWhen(this.glowEnabledSetting::getValue);
      this.trailStyleSetting = new SingleChoiceSetting(TRAIL_STYLE_LABEL, new ChoiceOption(TRAIL_STYLE_FLAME_OPTION_LABEL), new ChoiceOption(TRAIL_STYLE_ENERGY_OPTION_LABEL), new ChoiceOption(TRAIL_STYLE_RIBBON_OPTION_LABEL)).visibleWhen(this::isTrailEnabled);
      this.energyTrailLengthSetting = new FloatSetting(ENERGY_TRAIL_LENGTH_LABEL, 1.0F, ENERGY_TRAIL_LENGTH_MIN, 2.0F).withStep(ENERGY_TRAIL_LENGTH_STEP).visibleWhen(this::isEnergyTrailEnabled);
      this.energyTrailBrightnessSetting = new FloatSetting(ENERGY_TRAIL_BRIGHTNESS_LABEL, ENERGY_TRAIL_BRIGHTNESS_DEFAULT, ENERGY_TRAIL_BRIGHTNESS_MIN, ENERGY_TRAIL_BRIGHTNESS_MAX).withStep(ENERGY_TRAIL_BRIGHTNESS_STEP).visibleWhen(this::isEnergyTrailEnabled);
      this.energyTrailMotionSetting = new FloatSetting(ENERGY_TRAIL_MOTION_LABEL, ENERGY_TRAIL_MOTION_DEFAULT, 0.0F, 2.0F).withStep(ENERGY_TRAIL_MOTION_STEP).visibleWhen(this::isEnergyTrailEnabled);
      this.energyTrailPulseSetting = new FloatSetting(ENERGY_TRAIL_PULSE_LABEL, 1.0F, 0.0F, 2.0F).withStep(ENERGY_TRAIL_PULSE_STEP).visibleWhen(this::isEnergyTrailEnabled);
      this.energyCameraTrailStrengthSetting = new FloatSetting(ENERGY_CAMERA_TRAIL_STRENGTH_LABEL, ENERGY_CAMERA_TRAIL_STRENGTH_DEFAULT, 0.0F, 2.0F).withStep(ENERGY_CAMERA_TRAIL_STRENGTH_STEP).visibleWhen(this::isEnergyTrailEnabled);
      this.ribbonTrailLengthSetting = new FloatSetting(RIBBON_TRAIL_LENGTH_LABEL, RIBBON_TRAIL_LENGTH_DEFAULT, RIBBON_TRAIL_LENGTH_MIN, RIBBON_TRAIL_LENGTH_MAX).withStep(RIBBON_TRAIL_LENGTH_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonTrailWidthSetting = new FloatSetting(RIBBON_TRAIL_WIDTH_LABEL, RIBBON_TRAIL_WIDTH_DEFAULT, RIBBON_TRAIL_WIDTH_MIN, RIBBON_TRAIL_WIDTH_MAX).withStep(RIBBON_TRAIL_WIDTH_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonTrailBrightnessSetting = new FloatSetting(RIBBON_TRAIL_BRIGHTNESS_LABEL, 1.0F, 0.0F, RIBBON_TRAIL_BRIGHTNESS_MAX).withStep(RIBBON_TRAIL_BRIGHTNESS_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonCameraTrailStrengthSetting = new FloatSetting(RIBBON_CAMERA_TRAIL_STRENGTH_LABEL, RIBBON_CAMERA_TRAIL_STRENGTH_DEFAULT, 0.0F, 2.0F).withStep(RIBBON_CAMERA_TRAIL_STRENGTH_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonRiseSetting = new FloatSetting(RIBBON_RISE_LABEL, RIBBON_RISE_DEFAULT, 0.0F, 1.0F).withStep(RIBBON_RISE_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonBendSetting = new FloatSetting(RIBBON_BEND_LABEL, 2.0F, 0.0F, 2.0F).withStep(RIBBON_BEND_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.ribbonWaveSpeedSetting = new FloatSetting(RIBBON_WAVE_SPEED_LABEL, RIBBON_WAVE_SPEED_DEFAULT, 0.0F, RIBBON_WAVE_SPEED_MAX).withStep(RIBBON_WAVE_SPEED_STEP).visibleWhen(this::isRibbonTrailEnabled);
      this.swingRibbonsSetting = new BooleanSetting(SWING_RIBBONS_LABEL, true).visibleWhen(this::isRibbonTrailEnabled);
      this.swingRibbonLengthSetting = new FloatSetting(SWING_RIBBON_LENGTH_LABEL, 0.0F, SWING_RIBBON_LENGTH_MIN, SWING_RIBBON_LENGTH_MAX).withStep(SWING_RIBBON_LENGTH_STEP).visibleWhen(() -> this.isRibbonTrailEnabled() && this.swingRibbonsSetting.getValue());
      this.swingRibbonStrengthSetting = new FloatSetting(SWING_RIBBON_STRENGTH_LABEL, SWING_RIBBON_STRENGTH_DEFAULT, 0.0F, SWING_RIBBON_STRENGTH_MAX).withStep(SWING_RIBBON_STRENGTH_STEP).visibleWhen(() -> this.isRibbonTrailEnabled() && this.swingRibbonsSetting.getValue());
      this.eatingRibbonsSetting = new BooleanSetting(EATING_RIBBONS_LABEL, true).visibleWhen(this::isRibbonTrailEnabled);
      this.eatingRibbonLengthSetting = new FloatSetting(EATING_RIBBON_LENGTH_LABEL, EATING_RIBBON_LENGTH_DEFAULT, EATING_RIBBON_LENGTH_MIN, EATING_RIBBON_LENGTH_MAX).withStep(EATING_RIBBON_LENGTH_STEP).visibleWhen(() -> this.isRibbonTrailEnabled() && this.eatingRibbonsSetting.getValue());
      this.eatingRibbonStrengthSetting = new FloatSetting(EATING_RIBBON_STRENGTH_LABEL, EATING_RIBBON_STRENGTH_DEFAULT, 0.0F, EATING_RIBBON_STRENGTH_MAX).withStep(EATING_RIBBON_STRENGTH_STEP).visibleWhen(() -> this.isRibbonTrailEnabled() && this.eatingRibbonsSetting.getValue());
      this.flameTrailDecaySetting = new FloatSetting(FLAME_TRAIL_DECAY_LABEL, FLAME_TRAIL_DECAY_DEFAULT, FLAME_TRAIL_DECAY_MIN, FLAME_TRAIL_DECAY_MAX).withStep(FLAME_TRAIL_DECAY_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.flameTrailRiseSetting = new FloatSetting(FLAME_TRAIL_RISE_LABEL, FLAME_TRAIL_RISE_DEFAULT, 0.0F, FLAME_TRAIL_RISE_MAX).withStep(FLAME_TRAIL_RISE_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.flameTrailSwaySetting = new FloatSetting(FLAME_TRAIL_SWAY_LABEL, 0.0F, 0.0F, FLAME_TRAIL_SWAY_MAX).withStep(FLAME_TRAIL_SWAY_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.flameCameraTrailStrengthSetting = new FloatSetting(FLAME_CAMERA_TRAIL_STRENGTH_LABEL, 0.0F, 0.0F, 2.0F).withStep(FLAME_CAMERA_TRAIL_STRENGTH_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.flameTrailTurbulenceSetting = new FloatSetting(FLAME_TRAIL_TURBULENCE_LABEL, FLAME_TRAIL_TURBULENCE_DEFAULT, 0.0F, FLAME_TRAIL_TURBULENCE_MAX).withStep(FLAME_TRAIL_TURBULENCE_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.flameTrailFlickerSetting = new FloatSetting(FLAME_TRAIL_FLICKER_LABEL, FLAME_TRAIL_FLICKER_DEFAULT, 0.0F, FLAME_TRAIL_FLICKER_MAX).withStep(FLAME_TRAIL_FLICKER_STEP).visibleWhen(this::isFlameTrailEnabled);
      this.swingDecayBoostSetting = new BooleanSetting(SWING_DECAY_BOOST_LABEL, false).visibleWhen(this::isFlameTrailEnabled);
      this.swingDecayBoostStrengthSetting = new FloatSetting(SWING_DECAY_BOOST_STRENGTH_LABEL, SWING_DECAY_BOOST_STRENGTH_DEFAULT, 1.0F, SWING_DECAY_BOOST_STRENGTH_MAX).withStep(SWING_DECAY_BOOST_STRENGTH_STEP).visibleWhen(() -> this.isFlameTrailEnabled() && this.swingDecayBoostSetting.getValue());
      this.addSettings(
         this.effectModeSetting,
         this.useThemeColorsSetting,
         this.useItemColorsSetting,
         this.mirrorBlendSetting,
         this.mirrorDistortionSetting,
         this.mirrorBlurStrengthSetting,
         this.rainbowFillSetting,
         this.rainbowSpeedSetting,
         this.rainbowScaleSetting,
         this.fillColorSetting,
         this.fillOpacitySetting,
         this.preserveFillShadingSetting,
         this.fillShadingStrengthSetting,
         this.shaderStyleSetting,
         this.shaderPrimaryColorSetting,
         this.secondShaderColorSetting,
         this.shaderSecondaryColorSetting,
         this.shaderSpeedSetting,
         this.shaderScaleSetting,
         this.shaderGlowSetting,
         this.shaderQualitySetting,
         this.shaderStarsSetting,
         this.shaderOpacitySetting,
         this.preserveShaderShadingSetting,
         this.shaderShadingStrengthSetting,
         this.glowEnabledSetting,
         this.glowBlurStrengthSetting,
         this.glowBrightnessSetting,
         this.glowPrimaryColorSetting,
         this.glowSecondaryColorSetting,
         this.innerGlowStrengthSetting,
         this.outlineEnabledSetting,
         this.outlineColorSetting,
         this.outlineWidthSetting,
         this.trailEnabledSetting,
         this.trailStyleSetting,
         this.energyTrailLengthSetting,
         this.energyTrailBrightnessSetting,
         this.energyTrailMotionSetting,
         this.energyTrailPulseSetting,
         this.energyCameraTrailStrengthSetting,
         this.ribbonTrailLengthSetting,
         this.ribbonTrailWidthSetting,
         this.ribbonTrailBrightnessSetting,
         this.ribbonCameraTrailStrengthSetting,
         this.ribbonRiseSetting,
         this.ribbonBendSetting,
         this.ribbonWaveSpeedSetting,
         this.swingRibbonsSetting,
         this.swingRibbonLengthSetting,
         this.swingRibbonStrengthSetting,
         this.eatingRibbonsSetting,
         this.eatingRibbonLengthSetting,
         this.eatingRibbonStrengthSetting,
         this.flameTrailDecaySetting,
         this.flameTrailRiseSetting,
         this.flameTrailSwaySetting,
         this.flameCameraTrailStrengthSetting,
         this.flameTrailTurbulenceSetting,
         this.flameTrailFlickerSetting,
         this.swingDecayBoostSetting,
         this.swingDecayBoostStrengthSetting
      );
   }

   public float getShaderScale() {
      return this.shaderScaleSetting.getValue();
   }

   public float getEatingRibbonStrength() {
      return this.eatingRibbonStrengthSetting.getValue();
   }

   @Override
   public void onEnable() {
      this.activationTimeMillis = System.currentTimeMillis();
   }

   public float getFlameTrailSway() {
      return this.flameTrailSwaySetting.getValue();
   }

   public float getFillOpacity() {
      return this.fillOpacitySetting.getValue();
   }

   public int getMirrorColorArgb() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
   }

   public float getRibbonBend() {
      return this.ribbonBendSetting.getValue();
   }

   public boolean isSwingDecayBoostEnabled() {
      return this.swingDecayBoostSetting.getValue();
   }

   public boolean isEatingRibbonsEnabled() {
      return this.eatingRibbonsSetting.getValue();
   }

   public float getEnergyCameraTrailStrength() {
      return this.energyCameraTrailStrengthSetting.getValue();
   }

   public float getFlameCameraTrailStrength() {
      return this.flameCameraTrailStrengthSetting.getValue();
   }

   public float getFlameTrailRise() {
      return this.flameTrailRiseSetting.getValue();
   }

   public float getSwingRibbonStrength() {
      return this.swingRibbonStrengthSetting.getValue();
   }

   public boolean isMirrorMode() {
      return this.effectModeSetting.isSelected(MIRROR_MODE_MATCH_LABEL) || this.isShaderMirrorMode();
   }

   public float getShaderSpeed() {
      return this.shaderSpeedSetting.getValue();
   }

   public boolean isRibbonTrailEnabled() {
      return this.isTrailEnabled() && this.trailStyleSetting.isSelected(RIBBON_TRAIL_MODE_MATCH_LABEL);
   }

   public int getOutlineColorArgb() {
      if (this.useItemColorsSetting.getValue()) {
         return -1;
      } else {
         return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.outlineColorSetting.getValue();
      }
   }

   public float getRainbowScale() {
      return this.rainbowScaleSetting.getValue();
   }

   public float getMirrorDistortion() {
      return this.mirrorDistortionSetting.getValue();
   }

   public int getShaderStyleId() {
      if (this.shaderStyleSetting.isSelected(HAND_SHADER_STYLE_MATCH_LABEL)) {
         return 4;
      } else if (this.shaderStyleSetting.isSelected(NEBULA_SHADER_STYLE_MATCH_LABEL)) {
         return 1;
      } else if (this.shaderStyleSetting.isSelected(COSMOS_SHADER_STYLE_MATCH_LABEL)) {
         return 2;
      } else if (this.shaderStyleSetting.isSelected(GRADIENT_SHADER_STYLE_MATCH_LABEL)) {
         return 3;
      } else {
         return this.shaderStyleSetting.isSelected(HOMIE_SHADER_STYLE_MATCH_LABEL) ? 5 : 0;
      }
   }

   public float getOutlineWidth() {
      return this.outlineWidthSetting.getValue();
   }

   public float getFlameTrailDecay() {
      return this.flameTrailDecaySetting.getValue();
   }

   public float getRibbonRise() {
      return this.ribbonRiseSetting.getValue();
   }

   public float getRibbonTrailBrightness() {
      return this.ribbonTrailBrightnessSetting.getValue();
   }

   public float getRibbonWaveSpeed() {
      return this.ribbonWaveSpeedSetting.getValue();
   }

   public boolean isFlameTrailEnabled() {
      return this.isTrailEnabled() && this.trailStyleSetting.isSelected(FLAME_TRAIL_MODE_MATCH_LABEL);
   }

   public int getPrimaryGlowColorArgb() {
      return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.glowPrimaryColorSetting.getValue();
   }

   public float getEnergyTrailLength() {
      return this.energyTrailLengthSetting.getValue();
   }

   public float getMirrorBlurStrength() {
      return this.mirrorBlurStrengthSetting.getValue();
   }

   public float getFlameTrailFlicker() {
      return this.flameTrailFlickerSetting.getValue();
   }

   public float getRibbonTrailWidth() {
      return this.ribbonTrailWidthSetting.getValue();
   }

   public int getSecondaryGlowColorArgb() {
      return this.useThemeColorsSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.glowSecondaryColorSetting.getValue();
   }

   public float getFillShadingStrength() {
      return this.fillShadingStrengthSetting.getValue();
   }

   public boolean isShaderMirrorMode() {
      return this.effectModeSetting.isSelected(SHADER_MIRROR_MODE_MATCH_LABEL);
   }

   public float getRibbonCameraTrailStrength() {
      return this.ribbonCameraTrailStrengthSetting.getValue();
   }

   public float getSwingDecayBoostStrength() {
      return this.swingDecayBoostStrengthSetting.getValue();
   }

   @Override
   public void onDisable() {
      captureActive = false;
      activeCaptureTarget = null;
      this.captureInProgress = false;
      if (this.captureTarget != null) {
         this.captureTarget.destroyBuffers();
         this.captureTarget = null;
      }

      HandsItemBloom.INSTANCE.dispose();
   }

   public float getFlameTrailTurbulence() {
      return this.flameTrailTurbulenceSetting.getValue();
   }

   public boolean shouldCapture() {
      return this.isEnabled() && (!this.isNoneMode() || this.glowEnabledSetting.getValue() || this.outlineEnabledSetting.getValue());
   }

   public float getInnerGlowStrength() {
      return this.innerGlowStrengthSetting.getValue();
   }

   public float getElapsedSeconds() {
      return (float)(System.currentTimeMillis() - this.activationTimeMillis) / MILLISECONDS_PER_SECOND;
   }

   public boolean isTrailEnabled() {
      return this.glowEnabledSetting.getValue() && this.trailEnabledSetting.getValue();
   }

   public float getShaderOpacity() {
      return this.shaderOpacitySetting.getValue();
   }

   public boolean isItemColorsEnabled() {
      return this.useItemColorsSetting.getValue();
   }

   public boolean isShaderStarsEnabled() {
      return this.shaderStarsSetting.getValue();
   }

   public boolean isRainbowFillEnabled() {
      return this.rainbowFillSetting.getValue();
   }

   public int getTrailStyleId() {
      return this.isRibbonTrailEnabled() ? 2 : (this.isEnergyTrailEnabled() ? 1 : 0);
   }

   public float getShaderShadingStrength() {
      return this.shaderShadingStrengthSetting.getValue();
   }

   public boolean isGlowEnabled() {
      return this.glowEnabledSetting.getValue();
   }

   public float getEnergyTrailMotion() {
      return this.energyTrailMotionSetting.getValue();
   }
}
