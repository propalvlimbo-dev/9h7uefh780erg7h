package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;

public class ESP extends ClientModule {
   private static final int Ве = 96;
   private static final int ВА = -13303968;
   private static final int В4 = -57312;
   private static final double С = 0.18;
   private static final double з = 0.06;
   @SerializedName("е")
   private final MultiChoiceSetting targetTypesSetting;
   private final @com.google.gson.annotations.SerializedName("ы")  FloatSetting maxDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("Р")  BooleanSetting outlineEnabledSetting;
   private final @com.google.gson.annotations.SerializedName("Л")  ColorSetting friendColorSetting;
   @SerializedName("е")
   private final int[] cornerColors;
   private final @com.google.gson.annotations.SerializedName("З")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String MODULE_NAME = "ESP";
   private static final String MODULE_DESCRIPTION = "Подцветка игроков";
   private static final String TARGET_TYPES_LABEL = "Отображать";
   private static final String PLAYER_OPTION_LABEL = "Игроков";
   private static final String FRIEND_OPTION_LABEL = "Друзей";
   private static final String SELF_OPTION_LABEL = "Себя";
   private static final String MOB_OPTION_LABEL = "Мобов";
   private static final String MAX_DISTANCE_LABEL = "Дистанция";
   private static final float DEFAULT_MAX_DISTANCE = 72.0F;
   private static final float MIN_MAX_DISTANCE = 8.0F;
   private static final float MAX_MAX_DISTANCE = 200.0F;
   private static final String OUTLINE_ENABLED_LABEL = "Обводка";
   private static final String FRIEND_COLOR_LABEL = "Цвет друзей";
   private static final int DEFAULT_FRIEND_COLOR = -13303968;
   private static final String SELF_SELECTION_KEY = "Себя";
   private static final String FRIEND_SELECTION_KEY = "Друзей";
   private static final String PLAYER_SELECTION_KEY = "Игроков";
   private static final String MOB_SELECTION_KEY = "Мобов";
   private static final float PLAYER_BOX_PADDING = 0.09F;
   private static final float MOB_BOX_PADDING = 0.06F;
   private static final float BRIGHT_RGB_SCALE = 1.0869565F;
   private static final float DARK_RGB_SCALE = 0.62F;
   private static final float MID_RGB_SCALE = 0.8F;
   private static final float MIN_DISTANCE_ALPHA_FACTOR = 0.2F;
   private static final float ALPHA_BYTE_MAX = 255.0F;
   private static final float HURT_FADE_TICKS = 10.0F;
   private static final int HURT_COLOR = -57312;
   private static final double MIN_DASHED_LINE_LENGTH = 0.001;
   private static final double DASH_STRIDE = 0.24;
   private static final double DASH_LENGTH = 0.18;
   private static final float DASH_MIDPOINT_SCALE = 0.5F;
   private static final String FRIEND_COLOR_VISIBILITY_KEY = "Друзей";

   private int getEntityColor(Entity entity, float tickDelta) {
      int color;
      if (entity instanceof Player player && player != MINECRAFT_CLIENT.player && this.isFriendPlayer(player)) {
         color = this.friendColorSetting.getValue();
      } else if (entity instanceof Player) {
         color = ColorMath.getAnimatedThemeColor(entity.getId() * 17);
      } else {
         color = ColorMath.getAnimatedThemeColor(entity.getId() * 11);
      }

      if (entity instanceof LivingEntity livingEntity && livingEntity.hurtTime > 0) {
         float hurtFactor = Mth.clamp((livingEntity.hurtTime - tickDelta) / HURT_FADE_TICKS, 0.0F, 1.0F);
         if (hurtFactor > 0.0F) {
            color = ColorMath.lerpArgb(color, HURT_COLOR, hurtFactor);
         }
      }

      return color;
   }

   private boolean isFriendPlayer(Player player) {
      return player.getGameProfile() != null && player.getGameProfile().name() != null
         ? RoseClient.getInstance().getServices().getFriends().О(player.getGameProfile().name())
         : false;
   }

   private int withAlpha(int color, int alpha) {
      return ColorMath.packArgb(ColorMath.getRed(color), ColorMath.getGreen(color), ColorMath.getBlue(color), alpha);
   }

   private int scaleRgb(int color, float rgbScale) {
      return ColorMath.packArgb(
         Mth.clamp(Math.round(ColorMath.getRed(color) * rgbScale), 0, 255),
         Mth.clamp(Math.round(ColorMath.getGreen(color) * rgbScale), 0, 255),
         Mth.clamp(Math.round(ColorMath.getBlue(color) * rgbScale), 0, 255),
         ColorMath.getAlpha(color)
      );
   }

   private void appendEntityOutline(Pose pose, VertexConsumer vertices, Entity entity, Vec3 cameraPosition, float tickDelta, float maxDistance) {
      Vec3 interpolatedPosition = this.getInterpolatedEntityPosition(entity, tickDelta);
      AABB interpolatedBounds = entity.getBoundingBox().move(interpolatedPosition.x - entity.getX(), interpolatedPosition.y - entity.getY(), interpolatedPosition.z - entity.getZ());
      float padding = entity instanceof Player ? PLAYER_BOX_PADDING : MOB_BOX_PADDING;
      AABB cameraRelativeBounds = interpolatedBounds.inflate(padding).move(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
      int baseColor = this.getEntityColor(entity, tickDelta);
      float distanceFade = 1.0F - Mth.clamp(MINECRAFT_CLIENT.player.distanceTo(entity) / maxDistance, 0.0F, 1.0F);
      int brightColor = this.scaleRgb(baseColor, BRIGHT_RGB_SCALE);
      int darkColor = this.scaleRgb(baseColor, DARK_RGB_SCALE);
      int midColor = this.scaleRgb(baseColor, MID_RGB_SCALE);
      this.cornerColors[0] = ColorMath.saturatedPingPongGradient(brightColor, midColor, 0, 10);
      this.cornerColors[1] = ColorMath.saturatedPingPongGradient(midColor, darkColor, 90, 10);
      this.cornerColors[2] = ColorMath.saturatedPingPongGradient(darkColor, midColor, 180, 10);
      this.cornerColors[3] = ColorMath.saturatedPingPongGradient(midColor, brightColor, 270, 10);
      int alpha = Math.round(Mth.clamp(distanceFade, MIN_DISTANCE_ALPHA_FACTOR, 1.0F) * ALPHA_BYTE_MAX);
      this.appendDashedBoxOutline(pose, vertices, cameraRelativeBounds, this.cornerColors, alpha);
   }

   public ESP() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.targetTypesSetting = new MultiChoiceSetting(
         TARGET_TYPES_LABEL, new ChoiceOption(PLAYER_OPTION_LABEL, true), new ChoiceOption(FRIEND_OPTION_LABEL, true), new ChoiceOption(SELF_OPTION_LABEL, false), new ChoiceOption(MOB_OPTION_LABEL, true)
      );
      this.maxDistanceSetting = new FloatSetting(MAX_DISTANCE_LABEL, DEFAULT_MAX_DISTANCE, MIN_MAX_DISTANCE, MAX_MAX_DISTANCE).withStep(1.0F);
      this.outlineEnabledSetting = new BooleanSetting(OUTLINE_ENABLED_LABEL, true);
      this.friendColorSetting = new ColorSetting(FRIEND_COLOR_LABEL, false, DEFAULT_FRIEND_COLOR).visibleWhen(() -> this.targetTypesSetting.isSelected(FRIEND_COLOR_VISIBILITY_KEY));
      this.cornerColors = new int[4];
      this.worldEffectsListener = this::onWorldEffects;
      this.addSettings(this.targetTypesSetting, this.maxDistanceSetting, this.outlineEnabledSetting, this.friendColorSetting);
   }

   private boolean shouldRenderEntity(Entity entity) {
      if (entity == null || !entity.isAlive() || entity.isRemoved()) {
         return false;
      }

      if (!(entity instanceof LivingEntity)) {
         return false;
      }

      if (entity != MINECRAFT_CLIENT.player && entity != MINECRAFT_CLIENT.getCameraEntity()) {
         if (entity instanceof Player player) {
            if (this.isMarkedBot(player)) {
               return false;
            } else {
               return this.isFriendPlayer(player) ? this.targetTypesSetting.isSelected(FRIEND_SELECTION_KEY) : this.targetTypesSetting.isSelected(PLAYER_SELECTION_KEY);
            }
         } else {
            return this.targetTypesSetting.isSelected(MOB_SELECTION_KEY);
         }
      } else {
         return MINECRAFT_CLIENT.options.getCameraType().isFirstPerson() ? false : this.targetTypesSetting.isSelected(SELF_SELECTION_KEY);
      }
   }

   private void onWorldEffects(WorldEffectsEvent event) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (this.outlineEnabledSetting.getValue()) {
            Pose pose = event.getPoses().last();
            VertexConsumer vertices = event.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
            float tickDelta = event.getTickDelta();
            Vec3 cameraPosition = event.getCameraPosition();
            int renderedEntityCount = 0;
            float maxDistance = Math.max(this.maxDistanceSetting.getValue(), 1.0F);
            float maxDistanceSquared = maxDistance * maxDistance;

            for (Entity entity : MINECRAFT_CLIENT.level.entitiesForRendering()) {
               if (renderedEntityCount >= 96) {
                  break;
               }

               if (this.shouldRenderEntity(entity) && !(MINECRAFT_CLIENT.player.distanceToSqr(entity) > maxDistanceSquared)) {
                  this.appendEntityOutline(pose, vertices, entity, cameraPosition, tickDelta, maxDistance);
                  renderedEntityCount++;
               }
            }
         }
      }
   }

   private void appendDashedGradientLine(Pose pose, VertexConsumer vertices, double startX, double startY, double startZ, double endX, double endY, double endZ, int startColor, int endColor) {
      double deltaX = endX - startX;
      double deltaY = endY - startY;
      double deltaZ = endZ - startZ;
      double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
      if (!(length < MIN_DASHED_LINE_LENGTH)) {
         double directionX = deltaX / length;
         double directionY = deltaY / length;
         double directionZ = deltaZ / length;
         double dashStride = DASH_STRIDE;

         for (double dashStartDistance = 0.0; dashStartDistance < length; dashStartDistance += dashStride) {
            double dashEndDistance = Math.min(dashStartDistance + DASH_LENGTH, length);
            if (!(dashEndDistance <= dashStartDistance)) {
               float dashStartProgress = (float)(dashStartDistance / length);
               float dashEndProgress = (float)(dashEndDistance / length);
               WorldBoxGeometry.appendLine(
                  pose,
                  vertices,
                  (float)(startX + directionX * dashStartDistance),
                  (float)(startY + directionY * dashStartDistance),
                  (float)(startZ + directionZ * dashStartDistance),
                  (float)(startX + directionX * dashEndDistance),
                  (float)(startY + directionY * dashEndDistance),
                  (float)(startZ + directionZ * dashEndDistance),
                  ColorMath.blendArgb(startColor, endColor, (dashStartProgress + dashEndProgress) * DASH_MIDPOINT_SCALE)
               );
            }
         }
      }
   }

   private void appendDashedBoxOutline(Pose pose, VertexConsumer vertices, AABB bounds, int[] cornerColors, int alpha) {
      int minXMinZColor = this.withAlpha(cornerColors[0], alpha);
      int maxXMinZColor = this.withAlpha(cornerColors[1], alpha);
      int maxXMaxZColor = this.withAlpha(cornerColors[2], alpha);
      int minXMaxZColor = this.withAlpha(cornerColors[3], alpha);
      double minX = bounds.minX;
      double minY = bounds.minY;
      double minZ = bounds.minZ;
      double maxX = bounds.maxX;
      double maxY = bounds.maxY;
      double maxZ = bounds.maxZ;
      this.appendDashedGradientLine(pose, vertices, minX, minY, minZ, maxX, minY, minZ, minXMinZColor, maxXMinZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, minY, minZ, maxX, minY, maxZ, maxXMinZColor, maxXMaxZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, minY, maxZ, minX, minY, maxZ, maxXMaxZColor, minXMaxZColor);
      this.appendDashedGradientLine(pose, vertices, minX, minY, maxZ, minX, minY, minZ, minXMaxZColor, minXMinZColor);
      this.appendDashedGradientLine(pose, vertices, minX, maxY, minZ, maxX, maxY, minZ, minXMinZColor, maxXMinZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, maxY, minZ, maxX, maxY, maxZ, maxXMinZColor, maxXMaxZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, maxY, maxZ, minX, maxY, maxZ, maxXMaxZColor, minXMaxZColor);
      this.appendDashedGradientLine(pose, vertices, minX, maxY, maxZ, minX, maxY, minZ, minXMaxZColor, minXMinZColor);
      this.appendDashedGradientLine(pose, vertices, minX, minY, minZ, minX, maxY, minZ, minXMinZColor, minXMinZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, minY, minZ, maxX, maxY, minZ, maxXMinZColor, maxXMinZColor);
      this.appendDashedGradientLine(pose, vertices, maxX, minY, maxZ, maxX, maxY, maxZ, maxXMaxZColor, maxXMaxZColor);
      this.appendDashedGradientLine(pose, vertices, minX, minY, maxZ, minX, maxY, maxZ, minXMaxZColor, minXMaxZColor);
   }

   private Vec3 getInterpolatedEntityPosition(Entity entity, float tickDelta) {
      return new Vec3(Mth.lerp(tickDelta, entity.xOld, entity.getX()), Mth.lerp(tickDelta, entity.yOld, entity.getY()), Mth.lerp(tickDelta, entity.zOld, entity.getZ()));
   }

   private boolean isMarkedBot(Player player) {
      return RoseClient.getInstance().getServices().getModules().getAntiBot().detectedBots.contains(player);
   }
}
