package dev.rose.hud;

public enum Anchor {
   TOP_LEFT(0.0F, 0.0F),
   TOP(0.5F, 0.0F),
   TOP_RIGHT(1.0F, 0.0F),
   LEFT(0.0F, 0.5F),
   CENTER(0.5F, 0.5F),
   RIGHT(1.0F, 0.5F),
   BOTTOM_LEFT(0.0F, 1.0F),
   BOTTOM(0.5F, 1.0F),
   BOTTOM_RIGHT(1.0F, 1.0F);

   private final float fx;
   private final float fy;

   Anchor(float fx, float fy) {
      this.fx = fx;
      this.fy = fy;
   }

   public float fx() {
      return this.fx;
   }

   public float fy() {
      return this.fy;
   }

   public static Anchor nearest(float centerX, float centerY, float screenW, float screenH) {
      int column = centerX < screenW / 3.0F ? 0 : (centerX > screenW * 2.0F / 3.0F ? 2 : 1);
      int row = centerY < screenH / 3.0F ? 0 : (centerY > screenH * 2.0F / 3.0F ? 2 : 1);
      return values()[row * 3 + column];
   }
}
