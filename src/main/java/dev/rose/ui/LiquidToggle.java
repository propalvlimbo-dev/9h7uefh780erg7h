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


public final class LiquidToggle {
   private static final int DROPS = 5;
   private static final Glass DROP = dropGlass();
   private final Spring head;
   private final Spring tail;
   private final Spring flood;
   private final Spring squeeze = new Spring(0.0, 0.2, 0.85);
   private final float[] dropX = new float[5];
   private final float[] dropY = new float[5];
   private final float[] dropVelocityX = new float[5];
   private final float[] dropVelocityY = new float[5];
   private final float[] dropLife = new float[5];
   private long lastNanos = System.nanoTime();
   private boolean on;
   private boolean splashPending;

   public LiquidToggle(boolean on) {
      this.on = on;
      this.head = new Spring(on ? 1.0 : 0.0, 0.3, 0.62);
      this.tail = new Spring(on ? 1.0 : 0.0, 0.46, 0.9);
      this.flood = new Spring(on ? 1.0 : 0.0, 0.42, 0.95);
   }

   public void set(boolean value) {
      if (value != this.on) {
         this.on = value;
         this.head.setTarget(this.on ? 1.0 : 0.0);
         this.tail.setTarget(this.on ? 1.0 : 0.0);
         this.flood.setTarget(this.on ? 1.0 : 0.0);
         this.splashPending = true;
      }
   }

   public void squeeze(boolean pressed) {
      this.squeeze.setTarget(pressed ? 1.0 : 0.0);
   }

   public void render(GuiGraphics g, float x, float y, float w, float h, float alpha, int accent, int track) {
      long now = System.nanoTime();
      float dt = Math.min((float)(now - this.lastNanos) / 1.0E9F, 0.1F);
      this.lastNanos = now;
      float headValue = this.head.getFloat();
      float tailValue = this.tail.getFloat();
      float floodValue = Math.clamp(this.flood.getFloat(), 0.0F, 1.0F);
      float press = Math.clamp(this.squeeze.getFloat(), 0.0F, 1.0F);
      float inset = 2.0F;
      float knob = h - inset * 2.0F;
      float left = x + inset + knob / 2.0F;
      float right = x + w - inset - knob / 2.0F;
      float headX = left + (right - left) * headValue;
      float tailX = left + (right - left) * tailValue;
      float centerY = y + h / 2.0F;
      Corners capsule = Corners.round(h / 2.0F);
      Surfaces.fill(g, x, y, w, h, capsule, track, track, track, track, alpha);
      if (floodValue > 0.002F) {
         float halfDiagonal = (float)Math.hypot(w, h) / 2.0F;
         float reach = (Math.max(headX - x, x + w - headX) + h) / halfDiagonal;
         int clear = ColorUtil.withAlpha(accent, 0);
         Paint paint = Paint.radial((headX - x) / w, 0.5F, Math.max(0.01F, reach * floodValue), accent, accent, clear).withStops(0.0F, 0.9F, 1.0F);
         Surfaces.fill(g, x, y, w, h, capsule, paint, alpha);
      }

      float motion = Math.min(1.0F, Math.abs(headValue - tailValue) * 3.5F);
      float stretch = knob * (0.18F * press + 0.25F * motion);
      float dropW = knob + stretch;
      float dropH = knob * (1.0F - 0.08F * motion);
      Surfaces.shadow(
         g,
         headX - dropW / 2.0F,
         centerY - dropH / 2.0F + 1.0F,
         dropW,
         dropH,
         Corners.round(dropH / 2.0F),
         new Shadow(1107296256, 1.6F, 0.0F, 0.0F, 0.6F),
         alpha
      );
      if (motion > 0.03F) {
         float tailSize = knob * (0.72F - 0.2F * motion);
         LiquidGlass.shapes()
            .add(headX - dropW / 2.0F, centerY - dropH / 2.0F, dropW, dropH, Corners.round(dropH / 2.0F))
            .add(tailX - tailSize / 2.0F, centerY - tailSize / 2.0F, tailSize, tailSize, Corners.round(tailSize / 2.0F))
            .draw(g, DROP, knob * 0.55F, alpha);
      } else {
         int white = -1;
         Surfaces.fill(g, headX - dropW / 2.0F, centerY - dropH / 2.0F, dropW, dropH, Corners.round(dropH / 2.0F), white, white, white, white, alpha);
      }

      if (this.splashPending && Math.abs(headValue - (this.on ? 1 : 0)) < 0.06F) {
         this.splashPending = false;
         float direction = this.on ? 1.0F : -1.0F;

         for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians((this.on ? 0 : 180) + (i - 2.0) * 38.0);
            float speed = 34 + i % 2 * 16;
            this.dropX[i] = headX + direction * dropW * 0.45F;
            this.dropY[i] = centerY;
            this.dropVelocityX[i] = (float)Math.cos(angle) * speed;
            this.dropVelocityY[i] = (float)Math.sin(angle) * speed - 18.0F;
            this.dropLife[i] = 1.0F;
         }
      }

      for (int i = 0; i < 5; i++) {
         if (!(this.dropLife[i] <= 0.0F)) {
            this.dropLife[i] = this.dropLife[i] - dt / 0.32F;
            this.dropVelocityY[i] = this.dropVelocityY[i] + 260.0F * dt;
            this.dropX[i] = this.dropX[i] + this.dropVelocityX[i] * dt;
            this.dropY[i] = this.dropY[i] + this.dropVelocityY[i] * dt;
            float size = 1.8F * Math.max(0.0F, this.dropLife[i]) + 0.4F;
            int color = ColorUtil.lerp(-1, accent, 0.35F);
            Surfaces.fill(
               g,
               this.dropX[i] - size / 2.0F,
               this.dropY[i] - size / 2.0F,
               size,
               size,
               Corners.round(size / 2.0F),
               color,
               color,
               color,
               color,
               alpha * Math.clamp(this.dropLife[i], 0.0F, 1.0F)
            );
         }
      }
   }

   private static Glass dropGlass() {
      Glass glass = Glass.lens();
      glass.tint = Paint.solid(-335544321);
      glass.refraction = 3.0F;
      glass.bevel = 4.0F;
      glass.specular = 1.1F;
      glass.rim = 0.6F;
      return glass;
   }
}
