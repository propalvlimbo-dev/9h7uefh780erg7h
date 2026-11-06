package dev.rose.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.rose.RoseClient;
import dev.rose.compat.Compat;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.ColorGradingModule;
import dev.rose.module.impl.visual.MotionBlurModule;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.ColorTarget;
import dev.rose.render.gpu.GpuDraw;
import dev.rose.render.gpu.RoseShader;
import net.minecraft.client.Minecraft;


public final class PostEffects {
   private static ColorTarget scene;
   private static ColorTarget history;
   private static boolean historyValid;
   private static boolean broken;

   private PostEffects() {
   }

   public static void apply() {
      Minecraft mc = Minecraft.getInstance();
      ColorGradingModule grading = Modules.colorGrading();
      MotionBlurModule blur = Modules.motionBlur();
      boolean grade = grading != null && grading.isEnabled();
      float blurStrength = blur != null && blur.isEnabled() ? blur.strength() : 0.0F;
      RoseShader shader = Shaders.postGrade();
      if (!broken && mc.level != null && (grade || !(blurStrength <= 0.0F)) && !Compat.shaderPackActive() && shader.ready(Blend.REPLACE)) {
         try {
            render(mc.getMainRenderTarget(), shader, grading, grade, blurStrength);
         } catch (RuntimeException e) {
            broken = true;
            RoseClient.LOGGER.error("[Rose] post effects disabled", e);
         }
      } else {
         historyValid = false;
      }
   }

   private static void render(RenderTarget main, RoseShader shader, ColorGradingModule grading, boolean grade, float blurStrength) {
      boolean resized = scene == null || scene.width() != main.width || scene.height() != main.height;
      scene = ColorTarget.ensure(scene, "post scene", main.width, main.height, false);
      history = ColorTarget.ensure(history, "post history", main.width, main.height, false);
      if (resized) {
         historyValid = false;
      }

      CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
      encoder.copyTextureToTexture(main.getColorTexture(), scene.texture(), 0, 0, 0, 0, 0, main.width, main.height);
      shader.setSampler("DiffuseSampler", scene.view(), ColorTarget.linear());
      shader.setSampler("HistorySampler", history.view(), ColorTarget.linear());
      shader.safeGetUniform("Grade").set(grade ? 1.0F : 0.0F);
      if (grade) {
         shader.safeGetUniform("Saturation").set(grading.saturation());
         shader.safeGetUniform("Contrast").set(grading.contrast());
         shader.safeGetUniform("Vibrance").set(grading.vibrance());
         shader.safeGetUniform("Exposure").set(grading.exposure());
         shader.safeGetUniform("Warmth").set(grading.warmth());
      }

      shader.safeGetUniform("Blur").set(historyValid ? blurStrength : 0.0F);
      GpuDraw.screen(shader, Blend.REPLACE, main.getColorTextureView(), false);
      if (blurStrength > 0.0F) {
         encoder.copyTextureToTexture(main.getColorTexture(), history.texture(), 0, 0, 0, 0, 0, main.width, main.height);
         historyValid = true;
      } else {
         historyValid = false;
      }
   }
}
