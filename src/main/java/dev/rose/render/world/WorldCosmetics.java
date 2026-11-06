package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.AfterimageModule;
import dev.rose.module.impl.cosmetic.AuraModule;
import dev.rose.module.impl.cosmetic.JumpCirclesModule;
import dev.rose.module.impl.cosmetic.KillEffectsModule;
import dev.rose.module.impl.cosmetic.PetModule;
import dev.rose.module.impl.cosmetic.TrailModule;
import dev.rose.module.impl.visual.HitEffectsModule;
import dev.rose.module.impl.world.AmbientParticlesModule;
import dev.rose.module.impl.world.WaypointsModule;
import dev.rose.render.Theme;
import dev.rose.render.world.life.Motion;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector3f;


public final class WorldCosmetics {
   private static long lastFrame;

   private WorldCosmetics() {
   }

   public static void render(WorldRenderContext context) {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      PoseStack pose = context.matrices();
      if (player != null && mc.level != null && pose != null) {
         AuraModule aura = Modules.aura();
         TrailModule trail = Modules.trail();
         JumpCirclesModule circles = Modules.jumpCircles();
         PetModule pet = Modules.pet();
         KillEffectsModule kills = Modules.killEffects();
         AmbientParticlesModule particles = Modules.ambientParticles();
         HitEffectsModule hits = Modules.hitEffects();
         AfterimageModule echoes = Modules.afterimage();
         WaypointsModule waypoints = Modules.waypoints();
         boolean any = aura != null && aura.isEnabled()
            || trail != null && trail.isEnabled()
            || circles != null && circles.isEnabled()
            || pet != null && pet.isEnabled()
            || kills != null && kills.isEnabled()
            || particles != null && particles.isEnabled()
            || hits != null && hits.isEnabled() && hits.active()
            || echoes != null && echoes.isEnabled() && echoes.active()
            || waypoints != null && waypoints.isEnabled() && waypoints.active();
         long now = System.nanoTime();
         float delta = lastFrame == 0L ? 0.0F : Math.min(0.1F, (float)(now - lastFrame) / 1.0E9F);
         lastFrame = now;
         Motion.update(player, delta);
         if (any) {
            Camera camera = context.gameRenderer().getMainCamera();
            Vector3f right = new Vector3f(camera.leftVector()).negate();
            Vector3f up = new Vector3f(camera.upVector());
            CosmeticContext ctx = new CosmeticContext(
               pose,
               mc.renderBuffers().bufferSource(),
               camera,
               camera.position(),
               player,
               mc.getDeltaTracker().getGameTimeDeltaPartialTick(false),
               Theme.time(),
               delta,
               mc.options.getCameraType().isFirstPerson(),
               right,
               up
            );
            if (particles != null && particles.isEnabled()) {
               Modules.guard(particles, () -> particles.render(ctx));
            }

            if (kills != null && kills.isEnabled()) {
               Modules.guard(kills, () -> kills.render(ctx));
            }

            if (hits != null && hits.isEnabled() && hits.active()) {
               Modules.guard(hits, () -> hits.render(ctx));
            }

            if (!player.isInvisible()) {
               if (echoes != null && echoes.isEnabled() && echoes.active()) {
                  Modules.guard(echoes, () -> echoes.render(ctx));
               }

               if (circles != null && circles.isEnabled()) {
                  Modules.guard(circles, () -> circles.render(ctx));
               }

               if (aura != null && aura.isEnabled()) {
                  Modules.guard(aura, () -> aura.render(ctx));
               }

               if (trail != null && trail.isEnabled()) {
                  Modules.guard(trail, () -> trail.render(ctx));
               }

               if (pet != null && pet.isEnabled()) {
                  Modules.guard(pet, () -> pet.render(ctx));
               }
            }

            if (waypoints != null && waypoints.isEnabled() && waypoints.active()) {
               Modules.guard(waypoints, () -> waypoints.render(ctx));
            }

            ctx.buffers().endBatch();
         }
      }
   }
}
