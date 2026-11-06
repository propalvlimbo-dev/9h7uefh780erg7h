package dev.rose.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import dev.rose.RoseClient;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.ColorTarget;
import dev.rose.render.gpu.GpuDraw;
import dev.rose.render.gpu.GpuFrame;
import dev.rose.render.gpu.RoseShader;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;


public final class Backdrop {
   private static final int MAX_PASSES = 6;
   private static final ColorTarget[] down = new ColorTarget[7];
   private static final ColorTarget[] scratch = new ColorTarget[7];
   private static final ColorTarget[] results = new ColorTarget[Backdrop.Level.values().length];
   private static final long[] resultFrame = new long[]{-1L, -1L, -1L};
   private static ColorTarget sharp;
   private static long sharpFrame = -1L;
   private static long downFrame = -1L;
   private static boolean broken;

   private Backdrop() {
   }

   public static boolean available() {
      return !broken && Shaders.kawaseDown().ready(Blend.REPLACE) && Shaders.kawaseUp().ready(Blend.REPLACE);
   }

   public static boolean capture() {
      return broken ? false : sharpFrame == GpuFrame.frame() || safely(Backdrop::captureSharp);
   }

   public static boolean refreshSharp() {
      if (!broken && safely(Backdrop::captureSharp)) {
         downFrame = -1L;

         for (int i = 0; i < resultFrame.length; i++) {
            resultFrame[i] = -1L;
         }

         return true;
      } else {
         return false;
      }
   }

   public static GpuTextureView sharpTexture() {
      return sharp.view();
   }

   @Nullable
   public static GpuTextureView blurTexture(Backdrop.Level level) {
      if (broken) {
         return null;
      }

      int index = level.ordinal();
      if (resultFrame[index] != GpuFrame.frame()) {
         if (!safely(() -> computeBlur(level))) {
            return null;
         }

         resultFrame[index] = GpuFrame.frame();
      }

      return results[index].view();
   }

   private static boolean safely(Runnable action) {
      if (!available()) {
         return false;
      }

      try {
         action.run();
         return true;
      } catch (RuntimeException e) {
         broken = true;
         RoseClient.LOGGER.error("[Rose] backdrop disabled", e);
         return false;
      }
   }

   private static void captureSharp() {
      RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
      sharp = ColorTarget.ensure(sharp, "backdrop", main.width, main.height, false);
      RenderSystem.getDevice()
         .createCommandEncoder()
         .copyTextureToTexture(main.getColorTexture(), sharp.texture(), 0, 0, 0, 0, 0, main.width, main.height);
      sharpFrame = GpuFrame.frame();
   }

   private static void computeBlur(Backdrop.Level level) {
      if (sharpFrame != GpuFrame.frame()) {
         captureSharp();
      }

      int width = sharp.width();
      int height = sharp.height();
      if (downFrame != GpuFrame.frame()) {
         GpuTextureView source = sharp.view();
         int sourceWidth = width;
         int sourceHeight = height;

         for (int i = 1; i <= 6; i++) {
            down[i] = ColorTarget.ensure(down[i], "backdrop down " + i, width >> i, height >> i, true);
            pass(Shaders.kawaseDown(), source, sourceWidth, sourceHeight, down[i], i == 1);
            source = down[i].view();
            sourceWidth = down[i].width();
            sourceHeight = down[i].height();
         }

         downFrame = GpuFrame.frame();
      }

      int index = level.ordinal();
      results[index] = ColorTarget.ensure(results[index], "backdrop " + level, width >> 1, height >> 1, true);
      ColorTarget from = down[level.passes];
      GpuTextureView source = from.view();
      int sourceWidth = from.width();
      int sourceHeight = from.height();

      for (int j = level.passes - 1; j >= 1; j--) {
         ColorTarget target = j == 1 ? results[index] : (scratch[j] = ColorTarget.ensure(scratch[j], "backdrop up " + j, width >> j, height >> j, true));
         pass(Shaders.kawaseUp(), source, sourceWidth, sourceHeight, target, false);
         source = target.view();
         sourceWidth = target.width();
         sourceHeight = target.height();
      }
   }

   private static void pass(RoseShader shader, GpuTextureView source, int sourceWidth, int sourceHeight, ColorTarget target, boolean linearize) {
      shader.setSampler("DiffuseSampler", source, ColorTarget.linear());
      shader.safeGetUniform("HalfPixel").set(0.5F / sourceWidth, 0.5F / sourceHeight);
      shader.safeGetUniform("Offset").set(Theme.blurSpread);
      shader.safeGetUniform("Linearize").set(linearize ? 1.0F : 0.0F);
      GpuDraw.screen(shader, Blend.REPLACE, target.view(), false);
   }

   public enum Level {
      LIGHT(2),
      MEDIUM(4),
      STRONG(6);

      final int passes;

      Level(int passes) {
         this.passes = passes;
      }
   }
}
