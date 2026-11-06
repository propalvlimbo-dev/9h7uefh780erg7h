package sg.util.time;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sg.modules.impl.render.GhostHit;
import sg.client.RoseClient;
import sg.modules.impl.render.Particles;

public final class EntityModelPointSampler {
   private static final long Х西 = 1500L;
   private static final double ЯЙ = 0.2;
   private static final double Як = 0.32;
   private static final double Яг = 0.12;
   private static final double Я0 = 0.12;
   private static final Map<Integer, EntityModelPointSampler.ModelSample> playerSamplesById = new HashMap<>();
   private static final Map<UUID, EntityModelPointSampler.ModelSample> entitySamplesByUuid = new HashMap<>();
   private static final Map<Integer, Long> playerCaptureTicksById = new HashMap<>();
   private static final Map<UUID, Long> entityCaptureTicksByUuid = new HashMap<>();
   private static final Map<LivingEntityRenderState, EntityModelPointSampler.EntityIdentity> pendingEntityIdentitiesByState = new WeakHashMap<>();
   private static final Map<LivingEntityRenderState, Object> pendingModelsByState = new WeakHashMap<>();
   private static final double PLAYER_MODEL_SCALE = 0.9375;
   private static final long RANDOM_SEED_ID_MULTIPLIER = 31L;
   private static final long PLAYER_SAMPLE_MAX_AGE_MILLIS = 1500L;
   private static final long ENTITY_SAMPLE_MAX_AGE_MILLIS = 1500L;
   private static final double HUMANOID_HEAD_WEIGHT = 0.2;
   private static final double HUMANOID_BODY_WEIGHT = 0.32;
   private static final double HUMANOID_RIGHT_ARM_WEIGHT = 0.12;
   private static final double HUMANOID_LEFT_ARM_WEIGHT = 0.12;
   private static final double HUMANOID_RIGHT_LEG_WEIGHT = 0.12;
   private static final double HUMANOID_LEFT_LEG_WEIGHT = 0.12;
   private static final double HUMANOID_UPPER_REGION_THRESHOLD = 0.16;
   private static final double HUMANOID_UPPER_MIN_Y_FACTOR = 0.73;
   private static final double HUMANOID_UPPER_Y_RANGE_FACTOR = 0.27;
   private static final double HUMANOID_UPPER_X_CENTER = 0.5;
   private static final double HUMANOID_UPPER_X_WIDTH_FACTOR = 0.8;
   private static final double HUMANOID_MIDDLE_REGION_THRESHOLD = 0.5;
   private static final double HUMANOID_MIDDLE_MIN_Y_FACTOR = 0.36;
   private static final double HUMANOID_MIDDLE_Y_RANGE_FACTOR = 0.39;
   private static final double HUMANOID_MIDDLE_X_CENTER = 0.5;
   private static final double HUMANOID_LATERAL_REGION_THRESHOLD = 0.7;
   private static final double HUMANOID_LATERAL_MIN_Y_FACTOR = 0.35;
   private static final double HUMANOID_LATERAL_Y_RANGE_FACTOR = 0.4;
   private static final double HUMANOID_LATERAL_NEGATIVE_SIGN = -1.0;
   private static final double HUMANOID_LATERAL_MIN_X_FACTOR = 0.48;
   private static final double HUMANOID_LATERAL_X_RANGE_FACTOR = 0.42;
   private static final double HUMANOID_LOWER_MIN_Y_FACTOR = 0.02;
   private static final double HUMANOID_LOWER_Y_RANGE_FACTOR = 0.36;
   private static final double HUMANOID_LOWER_NEGATIVE_SIGN = -1.0;
   private static final double HUMANOID_LOWER_MIN_X_FACTOR = 0.12;
   private static final double HUMANOID_LOWER_X_RANGE_FACTOR = 0.2;
   private static final double HUMANOID_Y_CENTER = 0.5;
   private static final double HUMANOID_Z_CENTER = 0.5;
   private static final double HUMANOID_Z_WIDTH_FACTOR = 0.72;
   private static final double CREEPER_UPPER_REGION_THRESHOLD = 0.28;
   private static final double CREEPER_UPPER_MIN_Y_FACTOR = 0.68;
   private static final double CREEPER_UPPER_Y_RANGE_FACTOR = 0.32;
   private static final double CREEPER_UPPER_X_CENTER = 0.5;
   private static final double CREEPER_UPPER_Z_CENTER = 0.5;
   private static final double CREEPER_MIDDLE_REGION_THRESHOLD = 0.62;
   private static final double CREEPER_MIDDLE_MIN_Y_FACTOR = 0.34;
   private static final double CREEPER_MIDDLE_Y_RANGE_FACTOR = 0.34;
   private static final double CREEPER_MIDDLE_X_CENTER = 0.5;
   private static final double CREEPER_MIDDLE_X_WIDTH_FACTOR = 0.78;
   private static final double CREEPER_MIDDLE_Z_CENTER = 0.5;
   private static final double CREEPER_MIDDLE_Z_WIDTH_FACTOR = 0.78;
   private static final double CREEPER_LOWER_MIN_Y_FACTOR = 0.02;
   private static final double CREEPER_LOWER_Y_RANGE_FACTOR = 0.32;
   private static final double CREEPER_LOWER_X_NEGATIVE_SIGN = -1.0;
   private static final double CREEPER_LOWER_MIN_X_FACTOR = 0.18;
   private static final double CREEPER_LOWER_X_RANGE_FACTOR = 0.18;
   private static final double CREEPER_LOWER_Z_NEGATIVE_SIGN = -1.0;
   private static final double CREEPER_LOWER_MIN_Z_FACTOR = 0.18;
   private static final double CREEPER_LOWER_Z_RANGE_FACTOR = 0.18;
   private static final double CREEPER_Y_CENTER = 0.5;
   private static final double SPIDER_NEGATIVE_Z_REGION_THRESHOLD = 0.24;
   private static final double SPIDER_NEGATIVE_Z_X_CENTER = 0.5;
   private static final double SPIDER_NEGATIVE_Z_X_WIDTH_FACTOR = 0.58;
   private static final double SPIDER_NEGATIVE_Z_MIN_Y_FACTOR = 0.24;
   private static final double SPIDER_NEGATIVE_Z_Y_RANGE_FACTOR = 0.3;
   private static final double SPIDER_NEGATIVE_Z_MIN_Z_FACTOR = 0.22;
   private static final double SPIDER_NEGATIVE_Z_Z_RANGE_FACTOR = 0.3;
   private static final double SPIDER_POSITIVE_Z_REGION_THRESHOLD = 0.53;
   private static final double SPIDER_POSITIVE_Z_X_CENTER = 0.5;
   private static final double SPIDER_POSITIVE_Z_X_WIDTH_FACTOR = 0.88;
   private static final double SPIDER_POSITIVE_Z_MIN_Y_FACTOR = 0.25;
   private static final double SPIDER_POSITIVE_Z_Y_RANGE_FACTOR = 0.35;
   private static final double SPIDER_POSITIVE_Z_MIN_Z_FACTOR = 0.03;
   private static final double SPIDER_POSITIVE_Z_Z_RANGE_FACTOR = 0.38;
   private static final double SPIDER_LATERAL_NEGATIVE_SIGN = -1.0;
   private static final double SPIDER_LATERAL_MIN_X_FACTOR = 0.42;
   private static final double SPIDER_LATERAL_PAIR_X_INCREMENT = 0.16;
   private static final double SPIDER_LATERAL_X_RANGE_FACTOR = 0.22;
   private static final double SPIDER_LATERAL_MIN_Y_FACTOR = 0.08;
   private static final double SPIDER_LATERAL_Y_RANGE_FACTOR = 0.22;
   private static final double SPIDER_LATERAL_PAIR_Z_CENTER = 1.5;
   private static final double SPIDER_LATERAL_PAIR_Z_FACTOR = 0.3;
   private static final double SPIDER_LATERAL_Z_JITTER_CENTER = 0.5;
   private static final double SPIDER_LATERAL_Z_JITTER_FACTOR = 0.18;
   private static final double SPIDER_Y_CENTER = 0.5;
   private static final double QUADRUPED_CENTRAL_REGION_THRESHOLD = 0.5;
   private static final double QUADRUPED_CENTRAL_X_CENTER = 0.5;
   private static final double QUADRUPED_CENTRAL_X_WIDTH_FACTOR = 1.45;
   private static final double QUADRUPED_CENTRAL_MIN_Y_FACTOR = 0.4;
   private static final double QUADRUPED_CENTRAL_Y_RANGE_FACTOR = 0.3;
   private static final double QUADRUPED_CENTRAL_Z_CENTER = 0.5;
   private static final double QUADRUPED_CENTRAL_Z_WIDTH_FACTOR = 0.72;
   private static final double QUADRUPED_POSITIVE_X_REGION_THRESHOLD = 0.68;
   private static final double QUADRUPED_POSITIVE_X_MIN_X_FACTOR = 0.55;
   private static final double QUADRUPED_POSITIVE_X_X_RANGE_FACTOR = 0.3;
   private static final double QUADRUPED_POSITIVE_X_MIN_Y_FACTOR = 0.48;
   private static final double QUADRUPED_POSITIVE_X_Y_RANGE_FACTOR = 0.3;
   private static final double QUADRUPED_POSITIVE_X_Z_CENTER = 0.5;
   private static final double QUADRUPED_POSITIVE_X_Z_WIDTH_FACTOR = 0.66;
   private static final double QUADRUPED_LATERAL_X_NEGATIVE_SIGN = -1.0;
   private static final double QUADRUPED_LATERAL_MIN_X_FACTOR = 0.38;
   private static final double QUADRUPED_LATERAL_X_RANGE_FACTOR = 0.33;
   private static final double QUADRUPED_LATERAL_MIN_Y_FACTOR = 0.02;
   private static final double QUADRUPED_LATERAL_Y_RANGE_FACTOR = 0.4;
   private static final double QUADRUPED_LATERAL_Z_NEGATIVE_SIGN = -1.0;
   private static final double QUADRUPED_LATERAL_MIN_Z_FACTOR = 0.2;
   private static final double QUADRUPED_LATERAL_Z_RANGE_FACTOR = 0.18;
   private static final double QUADRUPED_Y_CENTER = 0.5;
   private static final long ENTITY_SAMPLE_PURGE_AGE_MILLIS = 1500L;
   private static final long PLAYER_SAMPLE_PURGE_AGE_MILLIS = 1500L;

   private static EntityModelPointSampler.ModelSample captureRootModelSample(LivingEntityRenderState renderState, Model model, double modelScale) {
      ArrayList cubeVolumes = new ArrayList();
      collectPartVolumes(model.root(), new PoseStack(), cubeVolumes, 1.0);
      return createModelSample(cubeVolumes, renderState, modelScale);
   }

   private static Vec3 sampleCreeperOffset(Random random, double width, double height) {
      double regionChoice = random.nextDouble();
      double offsetX;
      double heightFromBase;
      double offsetZ;
      if (regionChoice < CREEPER_UPPER_REGION_THRESHOLD) {
         heightFromBase = height * (CREEPER_UPPER_MIN_Y_FACTOR + random.nextDouble() * CREEPER_UPPER_Y_RANGE_FACTOR);
         offsetX = (random.nextDouble() - CREEPER_UPPER_X_CENTER) * width;
         offsetZ = (random.nextDouble() - CREEPER_UPPER_Z_CENTER) * width;
      } else if (regionChoice < CREEPER_MIDDLE_REGION_THRESHOLD) {
         heightFromBase = height * (CREEPER_MIDDLE_MIN_Y_FACTOR + random.nextDouble() * CREEPER_MIDDLE_Y_RANGE_FACTOR);
         offsetX = (random.nextDouble() - CREEPER_MIDDLE_X_CENTER) * width * CREEPER_MIDDLE_X_WIDTH_FACTOR;
         offsetZ = (random.nextDouble() - CREEPER_MIDDLE_Z_CENTER) * width * CREEPER_MIDDLE_Z_WIDTH_FACTOR;
      } else {
         heightFromBase = height * (CREEPER_LOWER_MIN_Y_FACTOR + random.nextDouble() * CREEPER_LOWER_Y_RANGE_FACTOR);
         offsetX = (random.nextBoolean() ? 1.0 : CREEPER_LOWER_X_NEGATIVE_SIGN) * width * (CREEPER_LOWER_MIN_X_FACTOR + random.nextDouble() * CREEPER_LOWER_X_RANGE_FACTOR);
         offsetZ = (random.nextBoolean() ? 1.0 : CREEPER_LOWER_Z_NEGATIVE_SIGN) * width * (CREEPER_LOWER_MIN_Z_FACTOR + random.nextDouble() * CREEPER_LOWER_Z_RANGE_FACTOR);
      }

      return new Vec3(offsetX, heightFromBase - height * CREEPER_Y_CENTER, offsetZ);
   }

   private static Vec3 sampleHumanoidOffset(Random random, double width, double height) {
      double regionChoice = random.nextDouble();
      double heightFromBase;
      double offsetX;
      if (regionChoice < HUMANOID_UPPER_REGION_THRESHOLD) {
         heightFromBase = height * (HUMANOID_UPPER_MIN_Y_FACTOR + random.nextDouble() * HUMANOID_UPPER_Y_RANGE_FACTOR);
         offsetX = (random.nextDouble() - HUMANOID_UPPER_X_CENTER) * width * HUMANOID_UPPER_X_WIDTH_FACTOR;
      } else if (regionChoice < HUMANOID_MIDDLE_REGION_THRESHOLD) {
         heightFromBase = height * (HUMANOID_MIDDLE_MIN_Y_FACTOR + random.nextDouble() * HUMANOID_MIDDLE_Y_RANGE_FACTOR);
         offsetX = (random.nextDouble() - HUMANOID_MIDDLE_X_CENTER) * width;
      } else if (regionChoice < HUMANOID_LATERAL_REGION_THRESHOLD) {
         heightFromBase = height * (HUMANOID_LATERAL_MIN_Y_FACTOR + random.nextDouble() * HUMANOID_LATERAL_Y_RANGE_FACTOR);
         offsetX = (random.nextBoolean() ? 1.0 : HUMANOID_LATERAL_NEGATIVE_SIGN) * width * (HUMANOID_LATERAL_MIN_X_FACTOR + random.nextDouble() * HUMANOID_LATERAL_X_RANGE_FACTOR);
      } else {
         heightFromBase = height * (HUMANOID_LOWER_MIN_Y_FACTOR + random.nextDouble() * HUMANOID_LOWER_Y_RANGE_FACTOR);
         offsetX = (random.nextBoolean() ? 1.0 : HUMANOID_LOWER_NEGATIVE_SIGN) * width * (HUMANOID_LOWER_MIN_X_FACTOR + random.nextDouble() * HUMANOID_LOWER_X_RANGE_FACTOR);
      }

      return new Vec3(offsetX, heightFromBase - height * HUMANOID_Y_CENTER, (random.nextDouble() - HUMANOID_Z_CENTER) * width * HUMANOID_Z_WIDTH_FACTOR);
   }

   public static void beginModelCapture(LivingEntityRenderState renderState, Object modelObject) {
      if (isModelSamplingEnabled() && renderState != null && modelObject instanceof Model && pendingEntityIdentitiesByState.containsKey(renderState)) {
         pendingModelsByState.putIfAbsent(renderState, modelObject);
      }
   }

   public static void purgeExpiredSamples() {
      long nowMillis = System.currentTimeMillis();
      playerSamplesById.entrySet().removeIf(playerSampleEntry -> nowMillis - playerSampleEntry.getValue().capturedAt > PLAYER_SAMPLE_PURGE_AGE_MILLIS);
      entitySamplesByUuid.entrySet().removeIf(entitySampleEntry -> nowMillis - entitySampleEntry.getValue().capturedAt > ENTITY_SAMPLE_PURGE_AGE_MILLIS);
      playerCaptureTicksById.keySet().retainAll(playerSamplesById.keySet());
      entityCaptureTicksByUuid.keySet().retainAll(entitySamplesByUuid.keySet());
   }

   private static EntityModelPointSampler.ModelSample captureHumanoidModelSample(HumanoidRenderState renderState, HumanoidModel<?> model, double modelScale) {
      ArrayList cubeVolumes = new ArrayList();
      collectPartVolumes(model.head, cubeVolumes, HUMANOID_HEAD_WEIGHT);
      collectPartVolumes(model.body, cubeVolumes, HUMANOID_BODY_WEIGHT);
      collectPartVolumes(model.rightArm, cubeVolumes, HUMANOID_RIGHT_ARM_WEIGHT);
      collectPartVolumes(model.leftArm, cubeVolumes, HUMANOID_LEFT_ARM_WEIGHT);
      collectPartVolumes(model.rightLeg, cubeVolumes, HUMANOID_RIGHT_LEG_WEIGHT);
      collectPartVolumes(model.leftLeg, cubeVolumes, HUMANOID_LEFT_LEG_WEIGHT);
      return createModelSample(cubeVolumes, renderState, modelScale);
   }

   private static Vec3 sampleFallbackOffset(Random random, double width, double height, EntityModelPointSampler.FallbackShape shape) {
      return switch (shape) {
         case HUMANOID -> sampleHumanoidOffset(random, width, height);
         case CREEPER -> sampleCreeperOffset(random, width, height);
         case SPIDER -> sampleSpiderOffset(random, width, height);
         case QUADRUPED -> sampleQuadrupedOffset(random, width, height);
      };
   }

   public static void capturePlayerModel(AvatarRenderState renderState, HumanoidModel<?> model) {
      if (isModelSamplingEnabled() && renderState != null && model != null && !GhostHit.Щ(renderState)) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player == null || renderState.id != minecraft.player.getId()) {
            if (shouldCapturePlayerThisTick(renderState.id)) {
               EntityModelPointSampler.ModelSample modelSample = captureHumanoidModelSample(renderState, model, PLAYER_MODEL_SCALE);
               if (modelSample != null) {
                  playerSamplesById.put(renderState.id, modelSample);
               }
            }
         }
      }
   }

   private EntityModelPointSampler() {
   }

   private static AABB getInterpolatedBounds(LivingEntity entity, float partialTick) {
      double interpolatedX = Mth.lerp(partialTick, entity.xOld, entity.getX());
      double interpolatedY = Mth.lerp(partialTick, entity.yOld, entity.getY());
      double interpolatedZ = Mth.lerp(partialTick, entity.zOld, entity.getZ());
      return entity.getBoundingBox().move(interpolatedX - entity.getX(), interpolatedY - entity.getY(), interpolatedZ - entity.getZ());
   }

   private static EntityModelPointSampler.ModelSample createModelSample(List<EntityModelPointSampler.ModelCubeVolume> cubeVolumes, LivingEntityRenderState renderState, double modelScale) {
      if (cubeVolumes.isEmpty()) {
         return null;
      }

      double totalWeight = 0.0;

      for (EntityModelPointSampler.ModelCubeVolume cubeVolume : cubeVolumes) {
         totalWeight += cubeVolume.weight;
      }

      return totalWeight <= 0.0
         ? null
         : new EntityModelPointSampler.ModelSample(List.copyOf(cubeVolumes), totalWeight, new Vec3(renderState.x, renderState.y, renderState.z), renderState.bodyRot, modelScale, System.currentTimeMillis());
   }

   private static boolean shouldCapturePlayerThisTick(int entityId) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         return false;
      }

      long gameTick = minecraft.level.getGameTime();
      Long previousCaptureTick = playerCaptureTicksById.put(entityId, gameTick);
      return previousCaptureTick == null || previousCaptureTick != gameTick;
   }

   public static void finishModelCapture(LivingEntityRenderState renderState, Object modelObject) {
      if (isModelSamplingEnabled() && renderState != null && modelObject instanceof Model model) {
         if (pendingModelsByState.get(renderState) == modelObject) {
            pendingModelsByState.remove(renderState);
            EntityModelPointSampler.EntityIdentity entityIdentity = pendingEntityIdentitiesByState.remove(renderState);
            if (entityIdentity != null) {
               EntityModelPointSampler.ModelSample modelSample = captureModelSample(renderState, model, entityIdentity.scale);
               if (modelSample != null) {
                  if (entityIdentity.player) {
                     playerSamplesById.put(entityIdentity.id, modelSample);
                  } else {
                     entitySamplesByUuid.put(entityIdentity.uuid, modelSample);
                  }
               }
            }
         }
      }
   }

   public static void clearCaches() {
      playerSamplesById.clear();
      entitySamplesByUuid.clear();
      playerCaptureTicksById.clear();
      entityCaptureTicksByUuid.clear();
      pendingEntityIdentitiesByState.clear();
      pendingModelsByState.clear();
   }

   private static Vec3 sampleSpiderOffset(Random random, double width, double height) {
      double regionChoice = random.nextDouble();
      double offsetX;
      double heightFromBase;
      double offsetZ;
      if (regionChoice < SPIDER_NEGATIVE_Z_REGION_THRESHOLD) {
         offsetX = (random.nextDouble() - SPIDER_NEGATIVE_Z_X_CENTER) * width * SPIDER_NEGATIVE_Z_X_WIDTH_FACTOR;
         heightFromBase = height * (SPIDER_NEGATIVE_Z_MIN_Y_FACTOR + random.nextDouble() * SPIDER_NEGATIVE_Z_Y_RANGE_FACTOR);
         offsetZ = -width * (SPIDER_NEGATIVE_Z_MIN_Z_FACTOR + random.nextDouble() * SPIDER_NEGATIVE_Z_Z_RANGE_FACTOR);
      } else if (regionChoice < SPIDER_POSITIVE_Z_REGION_THRESHOLD) {
         offsetX = (random.nextDouble() - SPIDER_POSITIVE_Z_X_CENTER) * width * SPIDER_POSITIVE_Z_X_WIDTH_FACTOR;
         heightFromBase = height * (SPIDER_POSITIVE_Z_MIN_Y_FACTOR + random.nextDouble() * SPIDER_POSITIVE_Z_Y_RANGE_FACTOR);
         offsetZ = width * (SPIDER_POSITIVE_Z_MIN_Z_FACTOR + random.nextDouble() * SPIDER_POSITIVE_Z_Z_RANGE_FACTOR);
      } else {
         int legIndex = random.nextInt(8);
         int legPairIndex = legIndex / 2;
         double sideSign = (legIndex & 1) == 0 ? SPIDER_LATERAL_NEGATIVE_SIGN : 1.0;
         offsetX = sideSign * width * (SPIDER_LATERAL_MIN_X_FACTOR + legPairIndex * SPIDER_LATERAL_PAIR_X_INCREMENT + random.nextDouble() * SPIDER_LATERAL_X_RANGE_FACTOR);
         heightFromBase = height * (SPIDER_LATERAL_MIN_Y_FACTOR + random.nextDouble() * SPIDER_LATERAL_Y_RANGE_FACTOR);
         offsetZ = (legPairIndex - SPIDER_LATERAL_PAIR_Z_CENTER) * width * SPIDER_LATERAL_PAIR_Z_FACTOR + (random.nextDouble() - SPIDER_LATERAL_Z_JITTER_CENTER) * width * SPIDER_LATERAL_Z_JITTER_FACTOR;
      }

      return new Vec3(offsetX, heightFromBase - height * SPIDER_Y_CENTER, offsetZ);
   }

   private static void collectPartVolumes(ModelPart modelPart, List<EntityModelPointSampler.ModelCubeVolume> cubeVolumes, double partWeight) {
      collectPartVolumes(modelPart, new PoseStack(), cubeVolumes, partWeight);
   }

   static boolean isModelSamplingEnabled() {
      try {
         if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
            Particles particles = RoseClient.getInstance().getServices().getModules().getParticles();
            return particles != null && particles.isEnabled() && particles.фь();
         } else {
            return false;
         }
      } catch (Throwable ignoredError) {
         return false;
      }
   }

   private static void collectPartVolumes(ModelPart modelPart, PoseStack poseStack, List<EntityModelPointSampler.ModelCubeVolume> cubeVolumes, double partWeight) {
      if (modelPart != null && modelPart.visible && !modelPart.skipDraw) {
         ArrayList<EntityModelPointSampler.ModelCubeVolume> partVolumes = new ArrayList<>();
         modelPart.visit(poseStack, (pose, partPath, cubeIndex, cube) -> partVolumes.add(EntityModelPointSampler.ModelCubeVolume.copyFromCube(pose.pose(), cube)));
         double totalVolumeWeight = 0.0;

         for (EntityModelPointSampler.ModelCubeVolume cubeVolume : partVolumes) {
            totalVolumeWeight += cubeVolume.weight;
         }

         if (!(totalVolumeWeight <= 0.0)) {
            double weightScale = partWeight / totalVolumeWeight;

            for (EntityModelPointSampler.ModelCubeVolume cubeVolume : partVolumes) {
               cubeVolumes.add(cubeVolume.withWeight(cubeVolume.weight * weightScale));
            }
         }
      }
   }

   private static EntityModelPointSampler.FallbackShape getFallbackShape(LivingEntity entity) {
      if (entity instanceof Creeper) {
         return EntityModelPointSampler.FallbackShape.CREEPER;
      } else if (entity instanceof Spider) {
         return EntityModelPointSampler.FallbackShape.SPIDER;
      } else {
         return entity instanceof Animal ? EntityModelPointSampler.FallbackShape.QUADRUPED : EntityModelPointSampler.FallbackShape.HUMANOID;
      }
   }

   private static EntityModelPointSampler.ModelSample captureModelSample(LivingEntityRenderState renderState, Model model, double modelScale) {
      return model instanceof HumanoidModel humanoidModel && renderState instanceof HumanoidRenderState humanoidState ? captureHumanoidModelSample(humanoidState, humanoidModel, modelScale) : captureRootModelSample(renderState, model, modelScale);
   }

   private static EntityModelPointSampler.ModelSample getRecentModelSample(LivingEntity entity) {
      long nowMillis = System.currentTimeMillis();
      if (entity instanceof Player) {
         EntityModelPointSampler.ModelSample playerSample = playerSamplesById.get(entity.getId());
         if (playerSample != null && nowMillis - playerSample.capturedAt <= PLAYER_SAMPLE_MAX_AGE_MILLIS) {
            return playerSample;
         }
      }

      EntityModelPointSampler.ModelSample entitySample = entitySamplesByUuid.get(entity.getUUID());
      return entitySample != null && nowMillis - entitySample.capturedAt <= ENTITY_SAMPLE_MAX_AGE_MILLIS ? entitySample : null;
   }

   private static Vec3 sampleQuadrupedOffset(Random random, double width, double height) {
      double regionChoice = random.nextDouble();
      double offsetX;
      double heightFromBase;
      double offsetZ;
      if (regionChoice < QUADRUPED_CENTRAL_REGION_THRESHOLD) {
         offsetX = (random.nextDouble() - QUADRUPED_CENTRAL_X_CENTER) * width * QUADRUPED_CENTRAL_X_WIDTH_FACTOR;
         heightFromBase = height * (QUADRUPED_CENTRAL_MIN_Y_FACTOR + random.nextDouble() * QUADRUPED_CENTRAL_Y_RANGE_FACTOR);
         offsetZ = (random.nextDouble() - QUADRUPED_CENTRAL_Z_CENTER) * width * QUADRUPED_CENTRAL_Z_WIDTH_FACTOR;
      } else if (regionChoice < QUADRUPED_POSITIVE_X_REGION_THRESHOLD) {
         offsetX = width * (QUADRUPED_POSITIVE_X_MIN_X_FACTOR + random.nextDouble() * QUADRUPED_POSITIVE_X_X_RANGE_FACTOR);
         heightFromBase = height * (QUADRUPED_POSITIVE_X_MIN_Y_FACTOR + random.nextDouble() * QUADRUPED_POSITIVE_X_Y_RANGE_FACTOR);
         offsetZ = (random.nextDouble() - QUADRUPED_POSITIVE_X_Z_CENTER) * width * QUADRUPED_POSITIVE_X_Z_WIDTH_FACTOR;
      } else {
         offsetX = (random.nextBoolean() ? 1.0 : QUADRUPED_LATERAL_X_NEGATIVE_SIGN) * width * (QUADRUPED_LATERAL_MIN_X_FACTOR + random.nextDouble() * QUADRUPED_LATERAL_X_RANGE_FACTOR);
         heightFromBase = height * (QUADRUPED_LATERAL_MIN_Y_FACTOR + random.nextDouble() * QUADRUPED_LATERAL_Y_RANGE_FACTOR);
         offsetZ = (random.nextBoolean() ? 1.0 : QUADRUPED_LATERAL_Z_NEGATIVE_SIGN) * width * (QUADRUPED_LATERAL_MIN_Z_FACTOR + random.nextDouble() * QUADRUPED_LATERAL_Z_RANGE_FACTOR);
      }

      return new Vec3(offsetX, heightFromBase - height * QUADRUPED_Y_CENTER, offsetZ);
   }

   private static boolean shouldCaptureEntityThisTick(UUID entityUuid) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         return false;
      }

      long gameTick = minecraft.level.getGameTime();
      Long previousCaptureTick = entityCaptureTicksByUuid.put(entityUuid, gameTick);
      return previousCaptureTick == null || previousCaptureTick != gameTick;
   }

   public static List<Vec3> sampleWorldPoints(LivingEntity entity, int pointCount) {
      ArrayList worldPoints = new ArrayList(pointCount);
      if (entity != null && pointCount > 0) {
         EntityModelPointSampler.ModelSample modelSample = getRecentModelSample(entity);
         Random random = new Random(entity.getId() * RANDOM_SEED_ID_MULTIPLIER ^ System.nanoTime());
         if (modelSample != null) {
            for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
               worldPoints.add(modelSample.sampleWorldPoint(random));
            }

            return worldPoints;
         } else {
            float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            AABB bounds = getInterpolatedBounds(entity, partialTick);
            Vec3 boundsCenter = bounds.getCenter();
            double width = bounds.getXsize();
            double height = bounds.getYsize();
            EntityModelPointSampler.FallbackShape shape = getFallbackShape(entity);
            float viewYaw = entity.getViewYRot(partialTick);

            for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
               Vec3 rotatedOffset = sampleFallbackOffset(random, width, height, shape).yRot((float)Math.toRadians(-viewYaw));
               worldPoints.add(boundsCenter.add(rotatedOffset));
            }

            return worldPoints;
         }
      } else {
         return worldPoints;
      }
   }

   public static void trackEntityRenderState(LivingEntity entity, LivingEntityRenderState renderState) {
      if (isModelSamplingEnabled() && entity != null && renderState != null && !(entity instanceof ArmorStand)) {
         Minecraft minecraft = Minecraft.getInstance();
         if (entity != minecraft.player && !(entity instanceof Player)) {
            if (shouldCaptureEntityThisTick(entity.getUUID())) {
               pendingEntityIdentitiesByState.put(renderState, new EntityModelPointSampler.EntityIdentity(entity.getUUID(), entity.getId(), false, renderState.scale));
            }
         }
      }
   }

   private record ModelCubeVolume(Matrix4f transform, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, double weight) {
      private Vec3 sampleModelPoint(Random random) {
         Vector3f modelPoint = new Vector3f(
            Mth.lerp(random.nextFloat(), this.minX, this.maxX) / 16.0F,
            Mth.lerp(random.nextFloat(), this.minY, this.maxY) / 16.0F,
            Mth.lerp(random.nextFloat(), this.minZ, this.maxZ) / 16.0F
         );
         this.transform.transformPosition(modelPoint);
         return new Vec3(modelPoint.x, modelPoint.y, modelPoint.z);
      }

      private static EntityModelPointSampler.ModelCubeVolume copyFromCube(Matrix4f partPose, Cube cube) {
         double volumeWeight = Math.max(1.0, (cube.maxX - cube.minX) * (cube.maxY - cube.minY) * (cube.maxZ - cube.minZ));
         return new EntityModelPointSampler.ModelCubeVolume(new Matrix4f(partPose), cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.maxZ, volumeWeight);
      }

      private EntityModelPointSampler.ModelCubeVolume withWeight(double newWeight) {
         return new EntityModelPointSampler.ModelCubeVolume(this.transform, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, newWeight);
      }
   }

   private enum FallbackShape {
      HUMANOID,
      CREEPER,
      SPIDER,
      QUADRUPED;
   }

   private record EntityIdentity(UUID uuid, int id, boolean player, double scale) {
   }

   private record ModelSample(
      List<EntityModelPointSampler.ModelCubeVolume> cuboids, double totalWeight, Vec3 entityPosition, float bodyYaw, double modelScale, long capturedAt
   ) {
      private Vec3 toWorldPoint(Vec3 modelPoint) {
         Vec3 scaledOffset = new Vec3(-modelPoint.x * this.modelScale, (1.501 - modelPoint.y) * this.modelScale, modelPoint.z * this.modelScale);
         return this.entityPosition.add(scaledOffset.yRot((float)Math.toRadians(180.0F - this.bodyYaw)));
      }

      private Vec3 sampleWorldPoint(Random random) {
         double remainingWeight = random.nextDouble() * this.totalWeight;

         for (EntityModelPointSampler.ModelCubeVolume cubeVolume : this.cuboids) {
            remainingWeight -= cubeVolume.weight;
            if (remainingWeight <= 0.0) {
               return this.toWorldPoint(cubeVolume.sampleModelPoint(random));
            }
         }

         return this.toWorldPoint(this.cuboids.getLast().sampleModelPoint(random));
      }
   }
}
