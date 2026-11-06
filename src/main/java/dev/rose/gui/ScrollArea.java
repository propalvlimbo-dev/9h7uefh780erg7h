package dev.rose.gui;

import dev.rose.anim.Spring;


final class ScrollArea {
   private static final float STEP = 34.0F;
   private static final float OVERSCROLL = 40.0F;
   private static final long SETTLE_MS = 140L;
   private final Spring position = new Spring(0.0, 0.3, 1.0);
   private double target;
   private float max;
   private long lastInput;

   void setBounds(float contentHeight, float viewHeight) {
      this.max = Math.max(0.0F, contentHeight - viewHeight);
      if (System.currentTimeMillis() - this.lastInput > 140L) {
         double clamped = Math.clamp(this.target, 0.0, this.max);
         if (clamped != this.target) {
            this.target = clamped;
            this.position.setTarget(this.target);
         }
      }
   }

   void scroll(double notches) {
      this.lastInput = System.currentTimeMillis();
      double delta = -notches * 34.0;
      boolean outside = this.target <= 0.0 && delta < 0.0 || this.target >= this.max && delta > 0.0;
      this.target = Math.clamp(this.target + (outside ? delta * 0.35 : delta), -40.0, this.max + 40.0F);
      this.position.setTarget(this.target);
   }

   void scrollTo(float offset) {
      this.target = Math.clamp(offset, 0.0F, this.max);
      this.position.setTarget(this.target);
   }

   float get() {
      return this.position.getFloat();
   }

   void reset() {
      this.target = 0.0;
      this.position.snap(0.0);
   }
}
