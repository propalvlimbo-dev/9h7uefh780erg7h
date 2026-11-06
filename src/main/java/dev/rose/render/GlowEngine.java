package dev.rose.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import dev.rose.RoseClient;
import dev.rose.compat.Compat;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.BloomModule;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.ColorTarget;
import dev.rose.render.gpu.GpuDraw;
import dev.rose.render.gpu.RoseShader;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;


public final class GlowEngine {
   private static final int LEVELS = 5;
   private static TextureTarget glow;
   private static TextureTarget halo;
   private static final ColorTarget[] down = new ColorTarget[6];
   private static final ColorTarget[] scratch = new ColorTarget[12];
   private static boolean worldPhase;
   private static boolean used;
   private static boolean haloUsed;
   private static boolean depthFresh;
   private static boolean haloDepthFresh;
   private static boolean broken;

   private GlowEngine() {
   }

   private static BloomModule module() {
      BloomModule module = Modules.bloom();
      return module != null && module.isEnabled() ? module : null;
   }

   public static boolean active() {
      return worldPhase
         && !broken
         && module() != null
         && !Compat.shaderPackActive()
         && Shaders.glowComposite().ready(Blend.ADDITIVE_PREMULTIPLIED)
         && Shaders.kawaseDown().ready(Blend.REPLACE)
         && Shaders.kawaseUp().ready(Blend.REPLACE);
   }

   public static void beginWorld() {
      worldPhase = true;
      haloUsed = false;
      used = false;
      haloDepthFresh = false;
      depthFresh = false;
   }

   public static void refreshDepth() {
      haloDepthFresh = false;
      depthFresh = false;
   }

   @Nullable
   public static RenderTarget glowTarget() {
      Minecraft mc = Minecraft.getInstance();
      if (!active()) {
         return mc.levelRenderer.getWeatherTarget();
      }

      try {
         glow = prepare(glow, "glow", !used, !depthFresh);
         depthFresh = true;
         used = true;
         return glow;
      } catch (RuntimeException e) {
         fail(e);
         return mc.levelRenderer.getWeatherTarget();
      }
   }

   @Nullable
   public static RenderTarget haloTarget() {
      if (!active()) {
         return null;
      }

      try {
         halo = prepare(halo, "halo", !haloUsed, !haloDepthFresh);
         haloDepthFresh = true;
         haloUsed = true;
         return halo;
      } catch (RuntimeException e) {
         fail(e);
         return null;
      }
   }

   private static TextureTarget prepare(TextureTarget target, String label, boolean clear, boolean copyDepth) {
      RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
      TextureTarget result = target;
      if (result == null) {
         result = new TextureTarget("Rose " + label, main.width, main.height, true);
      } else if (result.width != main.width || result.height != main.height) {
         result.resize(main.width, main.height);
      }

      if (clear) {
         RenderSystem.getDevice().createCommandEncoder().clearColorTexture(result.getColorTexture(), 0);
      }

      if (copyDepth) {
         result.copyDepthFrom(main);
      }

      return result;
   }

   public static void endWorld() {
      boolean sharp = used;
      boolean soft = haloUsed;
      boolean draw = (sharp || soft) && active();
      worldPhase = false;
      haloUsed = false;
      used = false;
      if (draw) {
         BloomModule settings = module();
         RenderTarget main = Minecraft.getInstance().getMainRenderTarget();

         try {
            TextureTarget first = sharp ? glow : halo;
            int width = first.width;
            int height = first.height;
            GpuTextureView source = first.getColorTextureView();
            int sourceWidth = width;
            int sourceHeight = height;
            float spread = settings.spread();

            for (int i = 1; i <= 5; i++) {
               down[i] = ColorTarget.ensure(down[i], "glow down " + i, width >> i, height >> i, true);
               blur(Shaders.kawaseDown(), source, sourceWidth, sourceHeight, down[i], spread, Blend.REPLACE);
               if (i == 1 && sharp && soft) {
                  blur(Shaders.kawaseDown(), halo.getColorTextureView(), width, height, down[1], spread, Blend.ADDITIVE_PREMULTIPLIED);
               }

               source = down[i].view();
               sourceWidth = down[i].width();
               sourceHeight = down[i].height();
            }

            ColorTarget tight = rise(3, 0, spread, width, height);
            ColorTarget wide = rise(5, 1, spread, width, height);
            float strength = settings.strength();
            GpuTextureView mask = soft ? halo.getColorTextureView() : first.getColorTextureView();
            float cut = soft ? 0.9F : 0.0F;
            GpuTextureView out = main.getColorTextureView();
            if (sharp) {
               composite(out, glow.getColorTextureView(), 1.0F, 1.0F, mask, cut);
            }

            composite(out, tight.view(), 0.85F * strength, settings.saturation(), mask, cut);
            composite(out, wide.view(), 0.7F * strength * settings.halo(), settings.saturation(), mask, cut);
         } catch (RuntimeException e) {
            fail(e);
         }
      }
   }

   private static ColorTarget rise(int from, int set, float spread, int width, int height) {
      ColorTarget source = down[from];

      for (int level = from - 1; level >= 1; level--) {
         int slot = set * 5 + level;
         ColorTarget target = scratch[slot] = ColorTarget.ensure(scratch[slot], "glow up " + slot, width >> level, height >> level, true);
         blur(Shaders.kawaseUp(), source.view(), source.width(), source.height(), target, spread, Blend.REPLACE);
         source = target;
      }

      return source;
   }

   private static void blur(RoseShader shader, GpuTextureView texture, int sourceWidth, int sourceHeight, ColorTarget target, float spread, Blend blend) {
      shader.setSampler("DiffuseSampler", texture, ColorTarget.linear());
      shader.safeGetUniform("HalfPixel").set(0.5F / sourceWidth, 0.5F / sourceHeight);
      shader.safeGetUniform("Offset").set(spread);
      shader.safeGetUniform("Linearize").set(0.0F);
      GpuDraw.screen(shader, blend, target.view(), false);
   }

   private static void composite(GpuTextureView out, GpuTextureView texture, float strength, float saturation, GpuTextureView mask, float cut) {
      if (!(strength <= 0.001F)) {
         RoseShader shader = Shaders.glowComposite();
         shader.setSampler("DiffuseSampler", texture, ColorTarget.linear());
         shader.setSampler("MaskSampler", mask, ColorTarget.linear());
         shader.safeGetUniform("Cut").set(cut);
         shader.safeGetUniform("Strength").set(strength);
         shader.safeGetUniform("Saturation").set(saturation);
         GpuDraw.screen(shader, Blend.ADDITIVE_PREMULTIPLIED, out, false);
      }
   }

   private static void fail(RuntimeException e) {
      broken = true;
      RoseClient.LOGGER.error("[Rose] glow engine disabled", e);
   }
}
