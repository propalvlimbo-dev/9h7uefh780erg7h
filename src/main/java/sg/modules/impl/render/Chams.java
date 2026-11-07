package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.player.Player;
import sg.mixinbridge.EntityRenderStateIdAccess;
import sg.render.pipeline.PrismaticChams;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public class Chams extends ClientModule {
   public static final String MODE_CRYSTAL = "Кристалл";
   public static final String MODE_VOID = "Войд";
   public static final String MODE_OUTLINE = "Обводка";
   public static final String DEPTH_HYBRID = "Гибрид";
   public static final String DEPTH_SCENE = "По сцене";
   public static final String DEPTH_THROUGH_WALLS = "Сквозь стены";
   private final @com.google.gson.annotations.SerializedName("Ь")  MultiChoiceSetting targets;
   @SerializedName("7")
   private final SingleChoiceSetting mode;
   private final @com.google.gson.annotations.SerializedName("Р")  SingleChoiceSetting depthMode;
   private final @com.google.gson.annotations.SerializedName("сЫ")  BooleanSetting hideEquipment;
   private final @com.google.gson.annotations.SerializedName("сl")  BooleanSetting hideShadow;
   private final @com.google.gson.annotations.SerializedName("Вт")  FloatSetting maxDistance;
   private final @com.google.gson.annotations.SerializedName("с8")  BooleanSetting useThemeColors;
   private final @com.google.gson.annotations.SerializedName("l")  ColorSetting upperAccent;
   @SerializedName("8")
   private final ColorSetting lowerAccent;
   private final @com.google.gson.annotations.SerializedName("Ва")  FloatSetting intensity;
   private final @com.google.gson.annotations.SerializedName("ВЮ")  FloatSetting opacity;
   private final @com.google.gson.annotations.SerializedName("ВЧ")  FloatSetting refraction;
   private final @com.google.gson.annotations.SerializedName("сВ")  Map<Integer, Chams.AnimationState> animations;
   private static final String MODULE_NAME = "Chams";
   private static final String MODULE_DESCRIPTION = "Шейдерная заливка и обводка моделей";
   private static final String TARGETS_LABEL = "Цели";
   private static final String PLAYERS_OPTION = "Игроков";
   private static final String FRIENDS_OPTION = "Друзей";
   private static final String MOBS_OPTION = "Мобов";
   private static final String SELF_OPTION = "Себя";
   private static final String INVISIBLE_OPTION = "Невидимых";
   private static final String MODE_LABEL = "Режим";
   private static final String CRYSTAL_OPTION = "Кристалл";
   private static final String VOID_OPTION = "Войд";
   private static final String OUTLINE_OPTION = "Обводка";
   private static final String DEPTH_MODE_LABEL = "Глубина";
   private static final String HYBRID_DEPTH_OPTION = "Гибрид";
   private static final String SCENE_DEPTH_OPTION = "По сцене";
   private static final String THROUGH_WALLS_DEPTH_OPTION = "Сквозь стены";
   private static final String HIDE_EQUIPMENT_LABEL = "Скрывать броню и предметы";
   private static final String HIDE_SHADOW_LABEL = "Скрывать ванильную тень";
   private static final String MAX_DISTANCE_LABEL = "Дистанция";
   private static final float DEFAULT_MAX_DISTANCE = 96.0F;
   private static final float MIN_DISTANCE = 8.0F;
   private static final float MAX_DISTANCE = 256.0F;
   private static final String THEME_COLORS_LABEL = "Авто цвет под тему";
   private static final String UPPER_ACCENT_LABEL = "Верхний акцент";
   private static final int DEFAULT_UPPER_ACCENT_COLOR = -14757377;
   private static final String LOWER_ACCENT_LABEL = "Нижний акцент";
   private static final int DEFAULT_LOWER_ACCENT_COLOR = -3068161;
   private static final String INTENSITY_LABEL = "Интенсивность";
   private static final float DEFAULT_INTENSITY = 1.35F;
   private static final float MIN_INTENSITY = 0.35F;
   private static final float MAX_INTENSITY = 3.0F;
   private static final float INTENSITY_STEP = 0.05F;
   private static final String OPACITY_LABEL = "Прозрачность";
   private static final float MIN_OPACITY = 0.25F;
   private static final float OPACITY_STEP = 0.01F;
   private static final String REFRACTION_LABEL = "Преломление";
   private static final float DEFAULT_REFRACTION = 0.72F;
   private static final float MIN_REFRACTION = 0.35F;
   private static final float MAX_REFRACTION = 1.15F;
   private static final float REFRACTION_STEP = 0.01F;
   private static final float CHAMS_VISIBILITY_EPSILON = 0.001F;
   private static final float LAYER_HIDE_EPSILON = 0.001F;
   private static final float SHADOW_HIDE_EPSILON = 0.001F;
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MAX_ANIMATION_DELTA_SECONDS = 0.05F;
   private static final float ANIMATION_SPRING_STIFFNESS = 42.0F;
   private static final float ANIMATION_DAMPING_EXPONENT_RATE = -13.0F;
   private static final float ANIMATION_OVERSHOOT_LIMIT = 1.12F;
   private static final float ANIMATION_OVERSHOOT_CLAMP_AMOUNT = 1.12F;
   private static final float ANIMATION_BOUNCE_VELOCITY_SCALE = -0.22F;
   private static final float ANIMATION_REMOVAL_EPSILON = 0.001F;
   private static final float ACTIVE_ANIMATION_AMOUNT_EPSILON = 0.001F;
   private static final float ACTIVE_ANIMATION_VELOCITY_EPSILON = 0.001F;
   private static final String VOID_MODE_KEY = "Войд";
   private static final String OUTLINE_MODE_KEY = "Обводка";
   private static final String LEGACY_VOID_MODE_KEY = "Void";
   private static final String CURRENT_VOID_MODE_KEY = "Войд";
   private static final String LEGACY_CRYSTAL_MODE_KEY = "Crystal";
   private static final String CURRENT_CRYSTAL_MODE_KEY = "Кристалл";
   private static final String HYBRID_DEPTH_MODE_KEY = "Гибрид";
   private static final String SCENE_DEPTH_MODE_KEY = "По сцене";
   private static final String INVISIBLE_TARGET_KEY = "Невидимых";
   private static final String MOB_TARGET_KEY = "Мобов";
   private static final String SELF_TARGET_KEY = "Себя";
   private static final String FRIEND_TARGET_KEY = "Друзей";
   private static final String PLAYER_TARGET_KEY = "Игроков";
   private static final int MISSING_ENTITY_ID = Integer.MIN_VALUE;
   private static final float X_POSITION_QUANTIZATION_SCALE = 8.0F;
   private static final float Y_POSITION_QUANTIZATION_SCALE = 8.0F;
   private static final float Z_POSITION_QUANTIZATION_SCALE = 8.0F;
   private static final String OUTLINE_REFRACTION_VISIBILITY_KEY = "Обводка";

   public boolean hasActiveAnimations() {
      Iterator animationIterator = this.animations.entrySet().iterator();

      while (animationIterator.hasNext()) {
         Chams.AnimationState animation = (Chams.AnimationState)((Entry)animationIterator.next()).getValue();
         if (animation.amount > ACTIVE_ANIMATION_AMOUNT_EPSILON || Math.abs(animation.velocity) > ACTIVE_ANIMATION_VELOCITY_EPSILON) {
            return true;
         }

         animationIterator.remove();
      }

      return false;
   }

   public float getOpacity() {
      return this.opacity.getValue();
   }

   private boolean isTargetEligible(LivingEntityRenderState state) {
      if (!this.isEnabled() || state == null || MINECRAFT_CLIENT.level == null) {
         return false;
      } else if (state.isInvisible && !this.targets.isSelected(INVISIBLE_TARGET_KEY)) {
         return false;
      } else {
         float maxDistance = Math.max(1.0F, this.maxDistance.getValue());
         if (state.distanceToCameraSq > maxDistance * maxDistance) {
            return false;
         } else {
            return state instanceof AvatarRenderState playerState ? this.isPlayerTargetEligible(playerState) : this.targets.isSelected(MOB_TARGET_KEY);
         }
      }
   }

   public Chams() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.targets = new MultiChoiceSetting(
         TARGETS_LABEL,
         new ChoiceOption(PLAYERS_OPTION, true),
         new ChoiceOption(FRIENDS_OPTION, true),
         new ChoiceOption(MOBS_OPTION, true),
         new ChoiceOption(SELF_OPTION, false),
         new ChoiceOption(INVISIBLE_OPTION, true)
      );
      this.mode = new SingleChoiceSetting(MODE_LABEL, new ChoiceOption(CRYSTAL_OPTION), new ChoiceOption(VOID_OPTION), new ChoiceOption(OUTLINE_OPTION));
      this.depthMode = new SingleChoiceSetting(DEPTH_MODE_LABEL, new ChoiceOption(HYBRID_DEPTH_OPTION), new ChoiceOption(SCENE_DEPTH_OPTION), new ChoiceOption(THROUGH_WALLS_DEPTH_OPTION));
      this.hideEquipment = new BooleanSetting(HIDE_EQUIPMENT_LABEL, true);
      this.hideShadow = new BooleanSetting(HIDE_SHADOW_LABEL, true);
      this.maxDistance = new FloatSetting(MAX_DISTANCE_LABEL, DEFAULT_MAX_DISTANCE, MIN_DISTANCE, MAX_DISTANCE).withStep(1.0F);
      this.useThemeColors = new BooleanSetting(THEME_COLORS_LABEL, true);
      this.upperAccent = new ColorSetting(UPPER_ACCENT_LABEL, false, DEFAULT_UPPER_ACCENT_COLOR).visibleWhen(() -> !this.useThemeColors.getValue());
      this.lowerAccent = new ColorSetting(LOWER_ACCENT_LABEL, false, DEFAULT_LOWER_ACCENT_COLOR).visibleWhen(() -> !this.useThemeColors.getValue());
      this.intensity = new FloatSetting(INTENSITY_LABEL, DEFAULT_INTENSITY, MIN_INTENSITY, MAX_INTENSITY).withStep(INTENSITY_STEP);
      this.opacity = new FloatSetting(OPACITY_LABEL, 1.0F, MIN_OPACITY, 1.0F).withStep(OPACITY_STEP);
      this.refraction = new FloatSetting(REFRACTION_LABEL, DEFAULT_REFRACTION, MIN_REFRACTION, MAX_REFRACTION).withStep(REFRACTION_STEP).visibleWhen(() -> !this.mode.isSelected(OUTLINE_REFRACTION_VISIBILITY_KEY));
      this.animations = new ConcurrentHashMap<>();
      this.addSettings(this.targets, this.mode, this.depthMode, this.hideEquipment, this.hideShadow, this.maxDistance, this.useThemeColors, this.upperAccent, this.lowerAccent, this.intensity, this.opacity, this.refraction);
      PrismaticChams.initializeRenderType();
   }

   public boolean isHybridDepthMode() {
      return this.depthMode.isSelected(HYBRID_DEPTH_MODE_KEY);
   }

   public float getIntensity() {
      return this.intensity.getValue();
   }

   public static Chams getActiveInstance() {
      Chams chams = getInstance();
      return chams != null && (chams.isEnabled() || chams.hasActiveAnimations()) ? chams : null;
   }

   public boolean isSceneDepthMode() {
      return this.depthMode.isSelected(SCENE_DEPTH_MODE_KEY);
   }

   public static Chams getInstance() {
      return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null
         ? RoseClient.getInstance().getServices().getModules().getChams()
         : null;
   }

   public float[] getUpperAccentColor() {
      return toShaderColor(this.useThemeColors.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.upperAccent.getValue());
   }

   public boolean shouldHideShadow(LivingEntityRenderState state) {
      return this.hideShadow.getValue() && this.getAnimationAmount(state) > SHADOW_HIDE_EPSILON;
   }

   public static int getEntityId(LivingEntityRenderState state) {
      return state instanceof AvatarRenderState playerState ? playerState.id : ((EntityRenderStateIdAccess)state).rose$getEntityId();
   }

   public int getShaderMode() {
      this.normalizeLegacyModeName();
      if (this.mode.isSelected(VOID_MODE_KEY)) {
         return 1;
      } else {
         return this.mode.isSelected(OUTLINE_MODE_KEY) ? 2 : 0;
      }
   }

   private boolean isPlayerTargetEligible(AvatarRenderState playerState) {
      int entityId = getEntityId(playerState);
      LocalPlayer localPlayer = MINECRAFT_CLIENT.player;
      if (localPlayer != null && entityId == localPlayer.getId()) {
         return MINECRAFT_CLIENT.options.getCameraType().isFirstPerson() && MINECRAFT_CLIENT.getCameraEntity() == localPlayer ? false : this.targets.isSelected(SELF_TARGET_KEY);
      }

      if ((MINECRAFT_CLIENT.level == null ? null : MINECRAFT_CLIENT.level.getEntity(entityId)) instanceof Player player) {
         if (this.isFriend(player)) {
            return this.targets.isSelected(FRIEND_TARGET_KEY);
         }
      }

      return this.targets.isSelected(PLAYER_TARGET_KEY);
   }

   public float getRefraction() {
      return this.refraction.getValue();
   }

   private void normalizeLegacyModeName() {
      String modeName = this.mode.getPendingSelectionName();
      if (modeName != null) {
         if (LEGACY_VOID_MODE_KEY.equals(modeName)) {
            this.mode.selectByName(CURRENT_VOID_MODE_KEY);
         } else if (LEGACY_CRYSTAL_MODE_KEY.equals(modeName)) {
            this.mode.selectByName(CURRENT_CRYSTAL_MODE_KEY);
         }
      }
   }

   public float getAnimationAmount(LivingEntityRenderState state) {
      if (state == null) {
         return 0.0F;
      }

      this.normalizeLegacyModeName();
      int animationKey = getAnimationKey(state);
      boolean eligible = this.isTargetEligible(state);
      Chams.AnimationState animation = this.animations.get(animationKey);
      if (animation == null && !eligible) {
         return 0.0F;
      }

      if (animation == null) {
         animation = new Chams.AnimationState();
         animation.amount = eligible ? 1.0F : 0.0F;
         this.animations.put(animationKey, animation);
      }

      long nowNanos = System.nanoTime();
      float deltaSeconds = animation.lastUpdateNanos == 0L ? 0.0F : Math.min((float)(nowNanos - animation.lastUpdateNanos) / NANOS_PER_SECOND, MAX_ANIMATION_DELTA_SECONDS);
      animation.lastUpdateNanos = nowNanos;
      float targetAmount = eligible ? 1.0F : 0.0F;
      float amountError = targetAmount - animation.amount;
      animation.velocity = animation.velocity + amountError * ANIMATION_SPRING_STIFFNESS * deltaSeconds;
      animation.velocity = animation.velocity * (float)Math.exp(ANIMATION_DAMPING_EXPONENT_RATE * deltaSeconds);
      animation.amount = animation.amount + animation.velocity * deltaSeconds;
      if (animation.amount < 0.0F) {
         animation.amount = 0.0F;
         animation.velocity = 0.0F;
      } else if (animation.amount > ANIMATION_OVERSHOOT_LIMIT) {
         animation.amount = ANIMATION_OVERSHOOT_CLAMP_AMOUNT;
         animation.velocity = animation.velocity * ANIMATION_BOUNCE_VELOCITY_SCALE;
      }

      if (!eligible && animation.amount <= ANIMATION_REMOVAL_EPSILON) {
         this.animations.remove(animationKey);
         return 0.0F;
      } else {
         return animation.amount;
      }
   }

   public boolean shouldHideLayers(LivingEntityRenderState state) {
      return this.hideEquipment.getValue() && this.getAnimationAmount(state) > LAYER_HIDE_EPSILON;
   }

   public boolean shouldRenderChams(LivingEntityRenderState state) {
      return this.getAnimationAmount(state) > CHAMS_VISIBILITY_EPSILON;
   }

   public float[] getLowerAccentColor() {
      return toShaderColor(this.useThemeColors.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.lowerAccent.getValue());
   }

   @Override
   public void onDisable() {
      PrismaticChams.beginFrame();
   }

   private static float[] toShaderColor(int argb) {
      float[] rgba = ColorMath.toRgbaArray(argb);
      return new float[]{rgba[0], rgba[1], rgba[2], rgba[3] <= 0.0F ? 1.0F : rgba[3]};
   }

   private boolean isFriend(Player player) {
      return player.getGameProfile() != null && player.getGameProfile().name() != null
         ? RoseClient.getInstance().getServices().getFriends().О(player.getGameProfile().name())
         : false;
   }

   private static int getAnimationKey(LivingEntityRenderState state) {
      int entityId = getEntityId(state);
      if (entityId != MISSING_ENTITY_ID) {
         return entityId;
      }

      int entityTypeHash = state.entityType == null ? 0 : state.entityType.hashCode();
      int quantizedX = Math.round((float)state.x * X_POSITION_QUANTIZATION_SCALE);
      int quantizedY = Math.round((float)state.y * Y_POSITION_QUANTIZATION_SCALE);
      int quantizedZ = Math.round((float)state.z * Z_POSITION_QUANTIZATION_SCALE);
      int key = entityTypeHash * 31 + quantizedX;
      key = key * 31 + quantizedY;
      return key * 31 + quantizedZ;
   }

   private static final class AnimationState {
       @com.google.gson.annotations.SerializedName("еМ") float amount;
       @com.google.gson.annotations.SerializedName("еН") float velocity;
       @com.google.gson.annotations.SerializedName("В诶") long lastUpdateNanos;
   }
}
