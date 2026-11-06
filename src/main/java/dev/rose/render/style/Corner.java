package dev.rose.render.style;

public record Corner(float radius, float shape) {
   public static final float BEVEL = 1.0F;
   public static final float ROUND = 2.0F;
   public static final float SQUIRCLE = 4.0F;
   public static final float SHARP = 64.0F;
   public static final float SCOOP = -1.0F;
   public static final Corner NONE = new Corner(0.0F, 2.0F);

   public static Corner round(float radius) {
      return new Corner(radius, 2.0F);
   }

   public static Corner squircle(float radius) {
      return new Corner(radius, 4.0F);
   }

   public static Corner bevel(float radius) {
      return new Corner(radius, 1.0F);
   }

   public static Corner scoop(float radius) {
      return new Corner(radius, -1.0F);
   }

   public Corner scaled(float factor) {
      return new Corner(this.radius * factor, this.shape);
   }
}
