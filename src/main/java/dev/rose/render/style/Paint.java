package dev.rose.render.style;

import dev.rose.util.ColorUtil;


public final class Paint {
   public static final int MAX_STOPS = 4;
   private final Paint.Type type;
   private final int[] colors;
   private final float[] stops;
   private final float angle;
   private final float spin;
   private final float centerX;
   private final float centerY;
   private final float radius;
   private final float[] lab = new float[16];

   private Paint(Paint.Type type, int[] colors, float[] stops, float angle, float spin, float centerX, float centerY, float radius) {
      if (colors.length != 0 && colors.length <= 4) {
         this.type = type;
         this.colors = (int[])colors.clone();
         this.stops = (float[])stops.clone();
         this.angle = angle;
         this.spin = spin;
         this.centerX = centerX;
         this.centerY = centerY;
         this.radius = radius;

         for (int i = 0; i < colors.length; i++) {
            float[] ok = ColorUtil.toOklab(colors[i]);
            this.lab[i * 4] = ok[0];
            this.lab[i * 4 + 1] = ok[1];
            this.lab[i * 4 + 2] = ok[2];
            this.lab[i * 4 + 3] = ColorUtil.alpha(colors[i]) / 255.0F;
         }
      } else {
         throw new IllegalArgumentException("Paint needs 1..4 colors");
      }
   }

   public static Paint solid(int color) {
      return new Paint(Paint.Type.SOLID, new int[]{color}, new float[]{0.0F}, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
   }

   public static Paint linear(float angle, int... colors) {
      return new Paint(Paint.Type.LINEAR, colors, evenStops(colors.length), angle, 0.0F, 0.5F, 0.5F, 1.0F);
   }

   public static Paint radial(float centerX, float centerY, float radius, int... colors) {
      return new Paint(Paint.Type.RADIAL, colors, evenStops(colors.length), 0.0F, 0.0F, centerX, centerY, radius);
   }

   public static Paint conic(float angle, int... colors) {
      return new Paint(Paint.Type.CONIC, colors, evenStops(colors.length), angle, 0.0F, 0.5F, 0.5F, 1.0F);
   }

   public Paint spinning(float degreesPerSecond) {
      return new Paint(this.type, this.colors, this.stops, this.angle, degreesPerSecond, this.centerX, this.centerY, this.radius);
   }

   public Paint withStops(float... positions) {
      if (positions.length != this.colors.length) {
         throw new IllegalArgumentException("stops count must match colors");
      } else {
         return new Paint(this.type, this.colors, positions, this.angle, this.spin, this.centerX, this.centerY, this.radius);
      }
   }

   public Paint faded(float factor) {
      if (factor >= 1.0F) {
         return this;
      }

      int[] faded = new int[this.colors.length];

      for (int i = 0; i < this.colors.length; i++) {
         faded[i] = ColorUtil.fade(this.colors[i], factor);
      }

      return new Paint(this.type, faded, this.stops, this.angle, this.spin, this.centerX, this.centerY, this.radius);
   }

   public Paint.Type type() {
      return this.type;
   }

   public int stopCount() {
      return this.colors.length;
   }

   public int color(int index) {
      return this.colors[index];
   }

   public float stop(int index) {
      return this.stops[index];
   }

   public float angle() {
      return this.angle;
   }

   public float spin() {
      return this.spin;
   }

   public float centerX() {
      return this.centerX;
   }

   public float centerY() {
      return this.centerY;
   }

   public float radius() {
      return this.radius;
   }

   public float[] lab() {
      return this.lab;
   }

   private static float[] evenStops(int count) {
      float[] result = new float[count];

      for (int i = 0; i < count; i++) {
         result[i] = count == 1 ? 0.0F : (float)i / (count - 1);
      }

      return result;
   }

   public enum Type {
      SOLID,
      LINEAR,
      RADIAL,
      CONIC;
   }
}
