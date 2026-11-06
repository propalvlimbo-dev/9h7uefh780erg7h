package dev.rose.ui;

import dev.rose.anim.Spring;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;


public final class GlassSlider {
   private static final Glass LENS = Glass.lens();
   private final Spring value;
   private final Spring glow = new Spring(0.0, 0.25, 1.0);
   private float target;

   public GlassSlider(float initial) {
      this.target = initial;
      this.value = new Spring(initial, 0.3, 0.8);
   }

   public void set(float newValue) {
      this.target = Math.clamp(newValue, 0.0F, 1.0F);
      this.value.setTarget(this.target);
   }

   public float get() {
      return this.target;
   }

   public static float valueAt(float localX, float w, float h) {
      float knobW = h * 0.72F * 1.6F;
      return Math.clamp((localX - knobW / 2.0F) / Math.max(1.0F, w - knobW), 0.0F, 1.0F);
   }

   public float animated() {
      return this.value.getFloat();
   }

   public float knobCenter(float x, float w, float h) {
      float baseW = h * 0.72F * 1.6F;
      return x + baseW / 2.0F + Math.clamp(this.value.getFloat(), 0.0F, 1.0F) * (w - baseW);
   }

   public void render(GuiGraphics g, float x, float y, float w, float h, Glass glass, float alpha) {
      this.render(g, x, y, w, h, glass, alpha, false);
   }

   public void render(GuiGraphics g, float x, float y, float w, float h, Glass glass, float alpha, boolean active) {
      float v = Math.clamp(this.value.getFloat(), 0.0F, 1.0F);
      float motion = Math.min((float)Math.abs(this.value.velocity()) / 2.5F, 1.0F);
      float lit = Math.clamp(this.glow.setTarget(active ? 1.0 : 0.0).getFloat(), 0.0F, 1.0F);
      float cy = y + h / 2.0F;
      float baseH = h * 0.72F;
      float baseW = baseH * 1.6F;
      float center = x + baseW / 2.0F + v * (w - baseW);
      float trackH = Math.max(3.0F, h * 0.28F);
      int accent = Theme.accent();
      int accent2 = Theme.accent2();
      Render2D.rect(g, x, cy - trackH / 2.0F, w, trackH, trackH / 2.0F, ColorUtil.fade(Controls.track(glass), alpha));
      float fillW = Math.max(trackH, center - x);
      Render2D.gradientH(
         g, x, cy - trackH / 2.0F, fillW, trackH, trackH / 2.0F, ColorUtil.fade(accent, alpha), ColorUtil.fade(ColorUtil.lerp(accent, accent2, v), alpha)
      );
      float knobH = baseH * (1.0F + 0.3F * motion + 0.08F * lit);
      float knobW = baseW * (1.0F + 0.25F * motion + 0.08F * lit);
      float kx = center - knobW / 2.0F;
      float ky = cy - knobH / 2.0F;
      if (lit > 0.01F) {
         Surfaces.glow(
            g,
            kx,
            ky,
            knobW,
            knobH,
            Corners.themed(knobH / 2.0F),
            Shadow.glow(ColorUtil.withAlpha(ColorUtil.lerp(accent, accent2, v), 153), 6.0F, 1.0F),
            alpha * lit
         );
      }

      Render2D.shadow(g, kx, ky + 1.0F, knobW, knobH, knobH / 2.0F, 3.0F, ColorUtil.fade(939524096, alpha));
      if (motion > 0.02F) {
         LiquidGlass.shapes().add(kx, ky, knobW, knobH, knobH / 2.0F).refreshBackdrop().draw(g, LENS, 0.0F, alpha * motion);
      }

      Render2D.rect(g, kx, ky, knobW, knobH, knobH / 2.0F, ColorUtil.fade(-1, alpha * (1.0F - motion)));
   }
}
