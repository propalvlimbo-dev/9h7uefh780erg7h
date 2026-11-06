package dev.rose.hud;

import dev.rose.module.Modules;
import dev.rose.module.impl.visual.HitEffectsModule;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;


public final class TargetTracker {
   private static LivingEntity attacked;
   private static long attackedAt;

   private TargetTracker() {
   }

   public static void register() {
      AttackEntityCallback.EVENT.register((AttackEntityCallback)(player, level, hand, entity, hitResult) -> {
         if (level.isClientSide() && entity instanceof LivingEntity living) {
            attacked = living;
            attackedAt = System.currentTimeMillis();
            HitEffectsModule effects = Modules.hitEffects();
            if (effects != null && effects.isEnabled()) {
               effects.onClick(living);
            }
         }

         return InteractionResult.PASS;
      });
   }

   public static long lastAttackMillis() {
      return attackedAt;
   }

   public static LivingEntity target(long keepMillis, boolean crosshair) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) {
         attacked = null;
         return null;
      }

      if (attacked != null && (attacked.isRemoved() || attacked.level() != mc.level || System.currentTimeMillis() - attackedAt > keepMillis)) {
         attacked = null;
      }

      if (attacked != null) {
         return attacked.isInvisibleTo(mc.player) ? null : attacked;
      } else {
         return crosshair && mc.crosshairPickEntity instanceof LivingEntity living && living.isAlive() && !living.isInvisibleTo(mc.player) ? living : null;
      }
   }
}
