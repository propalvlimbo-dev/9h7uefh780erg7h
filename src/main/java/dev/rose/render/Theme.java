package dev.rose.render;

import dev.rose.anim.Spring;
import dev.rose.render.style.Glass;
import java.util.function.IntSupplier;


public final class Theme {
   private static final long START = System.nanoTime();
   public static float cornerShape = 2.6F;
   public static float cornerScale = 1.0F;
   public static float blurSpread = 1.6F;
   public static float lightAngle = 238.0F;
   public static int positive = -13577896;
   public static int warning = -24822;
   public static int danger = -47814;
   public static int neutral = -7434605;
   private static IntSupplier accent = () -> -1824437;
   private static IntSupplier accent2 = () -> -38011;
   private static IntSupplier accent3 = () -> -4124102;
   private static Glass glassFrom = Glass.dark();
   private static Glass glassTo = Glass.dark();
   private static final Spring glassTransition = new Spring(1.0, 0.5, 1.0);

   private Theme() {
   }

   public static float time() {
      return (float)(System.nanoTime() - START) / 1.0E9F;
   }

   public static float lightX() {
      return (float)Math.cos(Math.toRadians(lightAngle));
   }

   public static float lightY() {
      return (float)Math.sin(Math.toRadians(lightAngle));
   }

   public static int accent() {
      return accent.getAsInt();
   }

   public static void setAccent(IntSupplier source) {
      accent = source;
   }

   public static int accent2() {
      return accent2.getAsInt();
   }

   public static void setAccent2(IntSupplier source) {
      accent2 = source;
   }

   public static int accent3() {
      return accent3.getAsInt();
   }

   public static void setAccent3(IntSupplier source) {
      accent3 = source;
   }

   public static Glass glass() {
      float t = Math.clamp(glassTransition.getFloat(), 0.0F, 1.0F);
      return t >= 1.0F ? glassTo : Glass.lerp(glassFrom, glassTo, t);
   }

   public static void setGlass(Glass target, boolean animate) {
      glassFrom = animate ? glass() : target;
      glassTo = target;
      glassTransition.snap(animate ? 0.0 : 1.0);
      glassTransition.setTarget(1.0);
   }
}
