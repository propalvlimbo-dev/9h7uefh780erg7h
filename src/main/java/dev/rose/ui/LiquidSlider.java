package dev.rose.ui;

import dev.rose.anim.Spring;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;


public final class LiquidSlider {
   private static final Glass KNOB = knobGlass();
   private final Spring value;
   private final Spring bubble = new Spring(0.0, 0.38, 0.62);
   private final Spring grab = new Spring(0.0, 0.2, 0.8);
   private float target;

   public LiquidSlider(float initial) {
      this.target = initial;
      this.value = new Spring(initial, 0.24, 0.86);
   }

   public void set(float newValue) {
      this.target = Math.clamp(newValue, 0.0F, 1.0F);
      this.value.setTarget(this.target);
   }

   public static float valueAt(float localX, float w) {
      return Math.clamp(localX / Math.max(1.0F, w), 0.0F, 1.0F);
   }

   public float[] render(
      GuiGraphics g,
      float x,
      float y,
      float w,
      float h,
      float alpha,
      int accent,
      int track,
      boolean dragging,
      boolean hovered,
      float bubbleWidth,
      Glass bubbleGlass
   ) {
      this.bubble.setTarget(dragging ? 1.0 : 0.0);
      this.grab.setTarget(dragging ? 1.0 : (hovered ? 0.4 : 0.0));
      float v = Math.clamp(this.value.getFloat(), 0.0F, 1.0F);
      float rise = this.bubble.getFloat();
      float hold = Math.clamp(this.grab.getFloat(), 0.0F, 1.0F);
      float centerY = y + h / 2.0F;
      float trackH = 4.0F;
      float knob = 11.0F + 4.0F * hold;
      float knobX = x + v * w;
      Corners trackCorners = Corners.round(trackH / 2.0F);
      Surfaces.fill(g, x, centerY - trackH / 2.0F, w, trackH, trackCorners, track, track, track, track, alpha);
      int end = ColorUtil.lerp(accent, -1, 0.18F);
      Surfaces.fill(g, x, centerY - trackH / 2.0F, Math.max(trackH, knobX - x), trackH, trackCorners, accent, end, end, accent, alpha);
      float bubbleH = 15.0F;
      float bubbleW = Math.max(bubbleWidth, 22.0F);
      float bubbleY = centerY - (knob / 2.0F + 12.0F + bubbleH / 2.0F) * Math.max(0.0F, rise);
      Surfaces.shadow(
         g, knobX - knob / 2.0F, centerY - knob / 2.0F + 1.0F, knob, knob, Corners.round(knob / 2.0F), new Shadow(1207959552, 1.8F, 0.0F, 0.0F, 0.8F), alpha
      );
      if (rise > 0.02F) {
         float grow = Math.clamp(rise, 0.0F, 1.2F);
         float bw = knob + (bubbleW - knob) * Math.min(1.0F, grow);
         float bh = knob + (bubbleH - knob) * Math.min(1.0F, grow);
         Glass glass = bubbleGlass.copy();
         glass.tint = Paint.solid(ColorUtil.withAlpha(accent, 230));
         LiquidGlass.shapes()
            .add(knobX - bw / 2.0F, bubbleY - bh / 2.0F, bw, bh, Corners.round(bh / 2.0F))
            .add(knobX - knob / 2.0F, centerY - knob / 2.0F, knob, knob, Corners.round(knob / 2.0F))
            .draw(g, glass, 7.0F, alpha);
      }

      LiquidGlass.shapes().add(knobX - knob / 2.0F, centerY - knob / 2.0F, knob, knob, Corners.round(knob / 2.0F)).draw(g, KNOB, 0.0F, alpha);
      return new float[]{knobX, bubbleY};
   }

   public float bubbleVisibility() {
      return Math.clamp((this.bubble.getFloat() - 0.55F) / 0.35F, 0.0F, 1.0F);
   }

   private static Glass knobGlass() {
      Glass glass = Glass.lens();
      glass.tint = Paint.solid(-218103809);
      glass.refraction = 3.0F;
      glass.bevel = 4.0F;
      glass.specular = 1.1F;
      glass.rim = 0.6F;
      return glass;
   }
}
