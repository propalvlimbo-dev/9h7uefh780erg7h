package dev.rose.combat;

import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.KillEffectsModule;
import dev.rose.module.impl.sound.EventSoundsModule;
import dev.rose.module.impl.sound.HitSoundsModule;
import dev.rose.module.impl.visual.HitEffectsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;


public final class CombatEvents {
   private static final long KILL_WINDOW_MS = 2500L;
   private static int lastTarget = -1;
   private static long lastHitAt;

   private CombatEvents() {
   }

   public static void onDamage(Entity target, int sourceId) {
      Minecraft mc = Minecraft.getInstance();
      if (target != null && mc.player != null && sourceId == mc.player.getId() && target != mc.player) {
         lastTarget = target.getId();
         lastHitAt = System.currentTimeMillis();
         HitSoundsModule hits = Modules.hitSounds();
         if (hits != null) {
            hits.onDamage(target);
         }

         HitEffectsModule effects = Modules.hitEffects();
         if (effects != null) {
            effects.onConfirmedHit(target);
         }

         if (Modules.afterimage() != null) {
            Modules.afterimage().onConfirmedHit();
         }
      }
   }

   public static void onCrit(Entity target) {
      HitSoundsModule hits = Modules.hitSounds();
      if (hits != null && target != null) {
         hits.onCrit(target);
      }

      HitEffectsModule effects = Modules.hitEffects();
      if (effects != null && target != null) {
         effects.onCrit(target);
      }
   }

   public static void onDeath(Entity entity) {
      if (entity != null && entity.getId() == lastTarget && System.currentTimeMillis() - lastHitAt <= 2500L) {
         lastTarget = -1;
         Vec3 position = entity.position();
         HitSoundsModule hits = Modules.hitSounds();
         if (hits != null) {
            hits.onKill(entity);
         }

         KillEffectsModule effects = Modules.killEffects();
         if (effects != null) {
            effects.onKill(entity, position);
         }

         if (Modules.pet() != null && Modules.pet().isEnabled()) {
            Modules.pet().cheer();
         }

         if (Modules.island() != null) {
            Modules.island().victory(entity);
         }
      }
   }

   public static void onTotem(Entity entity) {
      EventSoundsModule events = Modules.eventSounds();
      if (events != null) {
         events.onTotem(entity);
      }
   }
}
