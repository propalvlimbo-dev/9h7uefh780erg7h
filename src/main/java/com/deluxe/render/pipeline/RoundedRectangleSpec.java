package com.deluxe.render.pipeline;

public record RoundedRectangleSpec(
   float x,
   float y,
   float width,
   float height,
   float radiusTopLeft,
   float radiusTopRight,
   float radiusBottomRight,
   float radiusBottomLeft,
   int colorTopLeft,
   int colorTopRight,
   int colorBottomRight,
   int colorBottomLeft,
   float smoothness
) {
   public static final float DEFAULT_SMOOTHNESS = 0.5F;
   public static final int DEFAULT_COLOR = -1;

   public RoundedRectangleSpec withColor(int color) {
      return new RoundedRectangleSpec(
         this.x,
         this.y,
         this.width,
         this.height,
         this.radiusTopLeft,
         this.radiusTopRight,
         this.radiusBottomRight,
         this.radiusBottomLeft,
         color,
         color,
         color,
         color,
         this.smoothness
      );
   }

   public RoundedRectangleSpec withSmoothness(float smoothness) {
      return new RoundedRectangleSpec(
         this.x, this.y, this.width, this.height, this.radiusTopLeft, this.radiusTopRight, this.radiusBottomRight, this.radiusBottomLeft, this.colorTopLeft, this.colorTopRight, this.colorBottomRight, this.colorBottomLeft, smoothness
      );
   }

   public boolean visible() {
      if (this.width <= 0.0F || this.height <= 0.0F) {
         return false;
      } else {
         return (this.colorTopLeft | this.colorTopRight | this.colorBottomRight | this.colorBottomLeft) >>> 24 != 0;
      }
   }

   public RoundedRectangleSpec(float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int color) {
      this(x, y, width, height, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, color, color, color, color, 0.5F);
   }

   public RoundedRectangleSpec(float x, float y, float width, float height, float radius, int color) {
      this(x, y, width, height, radius, radius, radius, radius, color, color, color, color, 0.5F);
   }

   public RoundedRectangleSpec(
      float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft
   ) {
      this(x, y, width, height, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft, colorTopLeft, colorTopRight, colorBottomRight, colorBottomLeft, 0.5F);
   }

   public void render() {
      com.deluxe.gui.Render2D.fillRoundedRectangle(this);
   }
}
