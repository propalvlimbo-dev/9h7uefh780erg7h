package sg.util.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.jetbrains.annotations.Nullable;
import sg.ec.MinecraftAccess;

public final class CombatCalculations implements MinecraftAccess {
   private static final double ВЪ = 12.0;
   private static final EquipmentSlot[] armorSlots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private static final Map<CombatCalculations.ExposureCacheKey, Float> explosionExposureCache = new HashMap<>();
   private static final Map<BlockState, Float> blockBreakTicksCache = new HashMap<>();
   private static boolean cachingEnabled;
   private static final double DEFAULT_EXPLOSION_RANGE = 12.0;
   private static final double ENTITY_CENTER_X_FACTOR = 0.5;
   private static final double ENTITY_CENTER_Z_FACTOR = 0.5;
   private static final double DAMAGE_QUADRATIC_SCALE = 0.5;
   private static final double DAMAGE_STRENGTH_SCALE = 7.0;
   private static final double EXPOSURE_GRID_X_SCALE = 2.0;
   private static final double EXPOSURE_GRID_Y_SCALE = 2.0;
   private static final double EXPOSURE_GRID_Z_SCALE = 2.0;
   private static final double EXPOSURE_X_CENTERING_FACTOR = 0.5;
   private static final double EXPOSURE_Z_CENTERING_FACTOR = 0.5;
   private static final double RAY_DIRECTION_EPSILON = 1.0E-8;
   private static final double RAY_FALLBACK_ADVANCE = 1.0E-4;
   private static final double RAY_BLOCK_EXIT_ADVANCE = 1.0E-4;
   private static final float TOOL_SPEED_COMPARISON_EPSILON = 1.0E-4F;
   private static final float TOUGHNESS_DIVISOR = 4.0F;
   private static final float MIN_ARMOR_FRACTION = 0.2F;
   private static final float MAX_EFFECTIVE_ARMOR = 20.0F;
   private static final float ARMOR_REDUCTION_DIVISOR = 25.0F;
   private static final float ENCHANTMENT_REDUCTION_DIVISOR = 25.0F;
   private static final float RESISTANCE_REDUCTION_PER_LEVEL = 0.2F;
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   private static float applyArmorReduction(LivingEntity entity, float damage) {
      float armor = entity.getArmorValue();
      if (armor <= 0.0F) {
         return damage;
      }

      float toughness = (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
      float toughnessFactor = 2.0F + toughness / TOUGHNESS_DIVISOR;
      float effectiveArmor = Mth.clamp(armor - damage / toughnessFactor, armor * MIN_ARMOR_FRACTION, MAX_EFFECTIVE_ARMOR);
      return damage * (1.0F - effectiveArmor / ARMOR_REDUCTION_DIVISOR);
   }

   public static int findBestToolSlot(BlockState blockState) {
      if (blockState != null && MINECRAFT_CLIENT.player != null) {
         int bestSlot = -1;
         ItemStack bestTool = ItemStack.EMPTY;
         float bestSpeed = getEffectiveToolSpeed(bestTool, blockState);

         for (int var4 = 0; var4 < 9; var4++) {
            ItemStack candidateTool = MINECRAFT_CLIENT.player.getInventory().getItem(var4);
            float candidateSpeed = getEffectiveToolSpeed(candidateTool, blockState);
            if (isBetterTool(candidateTool, bestTool, candidateSpeed, bestSpeed, blockState)) {
               bestSlot = var4;
               bestTool = candidateTool;
               bestSpeed = candidateSpeed;
            }
         }

         return bestSlot;
      } else {
         return -1;
      }
   }

   public static float calculateExplosionDamage(Vec3 explosionPosition, Entity entity) {
      return entity == null ? 0.0F : calculateExplosionDamage(explosionPosition, entity, entity.getBoundingBox());
   }

   private static float applyResistanceReduction(LivingEntity entity, float damage) {
      MobEffectInstance resistance = entity.getEffect(MobEffects.RESISTANCE);
      if (resistance == null) {
         return damage;
      }

      float reduction = Mth.clamp((resistance.getAmplifier() + 1) * RESISTANCE_REDUCTION_PER_LEVEL, 0.0F, 1.0F);
      return damage * (1.0F - reduction);
   }

   private static int getEfficiencyLevel(ItemStack stack) {
      if (stack != null && !stack.isEmpty() && MINECRAFT_CLIENT.level != null) {
         Registry enchantmentRegistry = MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
         return EnchantmentHelper.getItemEnchantmentLevel(enchantmentRegistry.getOrThrow(Enchantments.EFFICIENCY), stack);
      } else {
         return 0;
      }
   }

   public static float estimateBlockBreakTicks(BlockState blockState, BlockPos blockPosition) {
      if (blockState != null && blockPosition != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Float cachedTicks = cachingEnabled ? blockBreakTicksCache.get(blockState) : null;
         if (cachedTicks != null) {
            return cachedTicks;
         }

         int bestToolSlot = findBestToolSlot(blockState);
         int previousSelectedSlot = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();

         float destroyProgress;
         try {
            if (bestToolSlot >= 0) {
               MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(bestToolSlot);
            }

            destroyProgress = blockState.getDestroyProgress(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.level, blockPosition);
         } finally {
            MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(previousSelectedSlot);
         }

         float breakTicks = destroyProgress > 0.0F ? (float)Math.ceil(1.0F / destroyProgress) : Float.POSITIVE_INFINITY;
         if (cachingEnabled) {
            blockBreakTicksCache.put(blockState, breakTicks);
         }

         return breakTicks;
      } else {
         return Float.POSITIVE_INFINITY;
      }
   }

   public static float calculateExplosionDamage(Vec3 explosionPosition, LivingEntity entity, AABB entityBox, @Nullable AABB obstructionBox, List<BlockPos> ignoredBlocks) {
      return calculateExplosionDamage(explosionPosition, entity, entityBox, obstructionBox, ignoredBlocks, DEFAULT_EXPLOSION_RANGE);
   }

   private static float sampleExplosionExposure(Vec3 explosionPosition, AABB entityBox, Entity entity, @Nullable AABB obstructionBox, List<BlockPos> ignoredBlocks) {
      double stepX = 1.0 / ((entityBox.maxX - entityBox.minX) * EXPOSURE_GRID_X_SCALE + 1.0);
      double stepY = 1.0 / ((entityBox.maxY - entityBox.minY) * EXPOSURE_GRID_Y_SCALE + 1.0);
      double stepZ = 1.0 / ((entityBox.maxZ - entityBox.minZ) * EXPOSURE_GRID_Z_SCALE + 1.0);
      if (!(stepX < 0.0) && !(stepY < 0.0) && !(stepZ < 0.0)) {
         double offsetX = (1.0 - Math.floor(1.0 / stepX) * stepX) * EXPOSURE_X_CENTERING_FACTOR;
         double offsetZ = (1.0 - Math.floor(1.0 / stepZ) * stepZ) * EXPOSURE_Z_CENTERING_FACTOR;
         int visibleSamples = 0;
         int totalSamples = 0;

         for (double var17 = 0.0; var17 <= 1.0; var17 += stepX) {
            for (double var19 = 0.0; var19 <= 1.0; var19 += stepY) {
               for (double var21 = 0.0; var21 <= 1.0; var21 += stepZ) {
                  Vec3 samplePoint = new Vec3(
                     Mth.lerp(var17, entityBox.minX, entityBox.maxX) + offsetX, Mth.lerp(var19, entityBox.minY, entityBox.maxY), Mth.lerp(var21, entityBox.minZ, entityBox.maxZ) + offsetZ
                  );
                  if (isExplosionRayClear(samplePoint, explosionPosition, entity, obstructionBox, ignoredBlocks)) {
                     visibleSamples++;
                  }

                  totalSamples++;
               }
            }
         }

         return totalSamples == 0 ? 0.0F : (float)visibleSamples / totalSamples;
      } else {
         return 0.0F;
      }
   }

   public static float calculateExplosionDamage(Vec3 explosionPosition, LivingEntity entity, AABB entityBox, @Nullable AABB obstructionBox, List<BlockPos> ignoredBlocks, double explosionRange) {
      if (explosionPosition == null || entity == null || entityBox == null || MINECRAFT_CLIENT.level == null) {
         return 0.0F;
      }

      if (!entity.isAlive()) {
         return 0.0F;
      }

      Vec3 entityBaseCenter = new Vec3((entityBox.minX + entityBox.maxX) * ENTITY_CENTER_X_FACTOR, entityBox.minY, (entityBox.minZ + entityBox.maxZ) * ENTITY_CENTER_Z_FACTOR);
      double distance = entityBaseCenter.distanceTo(explosionPosition);
      if (distance > explosionRange) {
         return 0.0F;
      }

      float exposure = getExplosionExposure(explosionPosition, entityBox, entity, obstructionBox, ignoredBlocks == null ? List.of() : ignoredBlocks);
      double impact = (1.0 - distance / explosionRange) * exposure;
      float damage = (float)((impact * impact + impact) * DAMAGE_QUADRATIC_SCALE * DAMAGE_STRENGTH_SCALE * explosionRange + 1.0);
      damage = applyArmorReduction(entity, damage);
      damage = applyProtectionReduction(entity, damage);
      damage = applyResistanceReduction(entity, damage);
      return Math.max(0.0F, damage);
   }

   public static void beginCachedCalculation() {
      explosionExposureCache.clear();
      blockBreakTicksCache.clear();
      cachingEnabled = true;
   }

   private static boolean isExplosionRayClear(Vec3 rayStart, Vec3 rayEnd, Entity entity, @Nullable AABB obstructionBox, List<BlockPos> ignoredBlocks) {
      if (obstructionBox != null && obstructionBox.clip(rayStart, rayEnd).isPresent()) {
         return false;
      }

      Vec3 currentRayStart = rayStart;
      int maxIgnoredHits = ignoredBlocks == null ? 0 : ignoredBlocks.size();
      int ignoredHits = 0;

      while (ignoredHits <= maxIgnoredHits) {
         BlockHitResult hit = MINECRAFT_CLIENT.level.clip(new ClipContext(currentRayStart, rayEnd, Block.COLLIDER, Fluid.NONE, entity));
         if (hit.getType() == Type.MISS) {
            return true;
         }

         if (ignoredBlocks != null && ignoredBlocks.contains(hit.getBlockPos())) {
            Vec3 rayDirection = rayEnd.subtract(rayStart).normalize();
            Vec3 nextRayStart = advanceRayPastBlock(hit.getLocation(), rayDirection, hit.getBlockPos());
            if (nextRayStart != null && !(rayEnd.subtract(nextRayStart).dot(rayDirection) <= 0.0)) {
               currentRayStart = nextRayStart;
               ignoredHits++;
               continue;
            }

            return true;
         }

         return false;
      }

      return false;
   }

   public static float calculateExplosionDamage(Vec3 explosionPosition, Entity entity, AABB entityBox) {
      return entity instanceof LivingEntity livingEntity ? calculateExplosionDamage(explosionPosition, livingEntity, entityBox, null, List.of()) : 0.0F;
   }

   private static float getEffectiveToolSpeed(ItemStack stack, BlockState blockState) {
      if (stack != null && !stack.isEmpty()) {
         float destroySpeed = stack.getDestroySpeed(blockState);
         int efficiencyLevel = getEfficiencyLevel(stack);
         if (destroySpeed > 1.0F && efficiencyLevel > 0) {
            destroySpeed += efficiencyLevel * efficiencyLevel + 1.0F;
         }

         return destroySpeed;
      } else {
         return 1.0F;
      }
   }

   private static boolean isShearsWithoutEfficiency(ItemStack stack) {
      return stack != null && !stack.isEmpty() && stack.is(Items.SHEARS) && getEfficiencyLevel(stack) == 0;
   }

   @Generated
   private CombatCalculations() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   private static boolean isBetterTool(ItemStack candidate, ItemStack currentTool, float candidateSpeed, float currentSpeed, BlockState blockState) {
      float speedEpsilon = TOOL_SPEED_COMPARISON_EPSILON;
      if (candidateSpeed > currentSpeed + speedEpsilon) {
         return true;
      }

      if (candidateSpeed + speedEpsilon < currentSpeed) {
         return false;
      }

      boolean candidateHarvests = canHarvestBlock(candidate, blockState);
      boolean currentHarvests = canHarvestBlock(currentTool, blockState);
      if (candidateHarvests != currentHarvests) {
         return candidateHarvests;
      }

      int candidateEfficiency = getEfficiencyLevel(candidate);
      int currentEfficiency = getEfficiencyLevel(currentTool);
      if (candidateEfficiency != currentEfficiency) {
         return candidateEfficiency > currentEfficiency;
      }

      boolean candidateShears = isShearsWithoutEfficiency(candidate);
      boolean currentShears = isShearsWithoutEfficiency(currentTool);
      return candidateShears != currentShears && !candidateShears;
   }

   public static float getExplosionExposure(Vec3 explosionPosition, AABB entityBox, Entity entity, @Nullable AABB obstructionBox, List<BlockPos> ignoredBlocks) {
      boolean hasCustomObstructions = obstructionBox != null || ignoredBlocks != null && !ignoredBlocks.isEmpty();
      if (!hasCustomObstructions && cachingEnabled) {
         CombatCalculations.ExposureCacheKey cacheKey = new CombatCalculations.ExposureCacheKey(entity.getId(), explosionPosition, entityBox);
         Float cachedExposure = explosionExposureCache.get(cacheKey);
         if (cachedExposure != null) {
            return cachedExposure;
         }

         float exposure = sampleExplosionExposure(explosionPosition, entityBox, entity, null, List.of());
         explosionExposureCache.put(cacheKey, exposure);
         return exposure;
      } else {
         return sampleExplosionExposure(explosionPosition, entityBox, entity, obstructionBox, ignoredBlocks == null ? List.of() : ignoredBlocks);
      }
   }

   private static float applyProtectionReduction(LivingEntity entity, float damage) {
      if (MINECRAFT_CLIENT.level == null) {
         return damage;
      }

      Registry enchantmentRegistry = MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      Reference protection = enchantmentRegistry.getOrThrow(Enchantments.PROTECTION);
      Reference blastProtection = enchantmentRegistry.getOrThrow(Enchantments.BLAST_PROTECTION);
      int protectionPoints = 0;

      for (EquipmentSlot var9 : armorSlots) {
         ItemStack armorStack = entity.getItemBySlot(var9);
         if (!armorStack.isEmpty()) {
            protectionPoints += EnchantmentHelper.getItemEnchantmentLevel(protection, armorStack);
            protectionPoints += EnchantmentHelper.getItemEnchantmentLevel(blastProtection, armorStack) * 2;
         }
      }

      protectionPoints = Mth.clamp(protectionPoints, 0, 20);
      return damage * (1.0F - protectionPoints / ENCHANTMENT_REDUCTION_DIVISOR);
   }

   private static boolean canHarvestBlock(ItemStack stack, BlockState blockState) {
      return !blockState.requiresCorrectToolForDrops() || stack != null && !stack.isEmpty() && stack.isCorrectToolForDrops(blockState);
   }

   public static void endCachedCalculation() {
      cachingEnabled = false;
   }

   private static Vec3 advanceRayPastBlock(Vec3 rayPosition, Vec3 rayDirection, BlockPos blockPosition) {
      AABB blockBox = new AABB(blockPosition);
      double exitDistance = Double.POSITIVE_INFINITY;
      double directionEpsilon = RAY_DIRECTION_EPSILON;
      if (rayDirection.x > directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.maxX - rayPosition.x) / rayDirection.x);
      } else if (rayDirection.x < -directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.minX - rayPosition.x) / rayDirection.x);
      }

      if (rayDirection.y > directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.maxY - rayPosition.y) / rayDirection.y);
      } else if (rayDirection.y < -directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.minY - rayPosition.y) / rayDirection.y);
      }

      if (rayDirection.z > directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.maxZ - rayPosition.z) / rayDirection.z);
      } else if (rayDirection.z < -directionEpsilon) {
         exitDistance = Math.min(exitDistance, (blockBox.minZ - rayPosition.z) / rayDirection.z);
      }

      return Double.isFinite(exitDistance) && !(exitDistance < 0.0) ? rayPosition.add(rayDirection.scale(exitDistance + RAY_BLOCK_EXIT_ADVANCE)) : rayPosition.add(rayDirection.scale(RAY_FALLBACK_ADVANCE));
   }

   private record ExposureCacheKey(int entityId, Vec3 explosion, AABB box) {
   }
}
