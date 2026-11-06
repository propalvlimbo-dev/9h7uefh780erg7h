package com.deluxe.internal;

public final class GuiBlurState {
   private static float radius = (24.0F);
   private static float opacity = 1.0F;
   private static boolean enabled;
   private static boolean requested;

   public static void setEnabled(boolean state) {
      enabled = state;
   }

   public static boolean isRequested() {
      return requested;
   }

   public static void setRadius(float value) {
      radius = Math.max(0.0F, value);
   }

   public static void setRequested(boolean state) {
      requested = state;
   }

   public static float getOpacity() {
      return opacity;
   }

   private GuiBlurState() {
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static float getRadius() {
      return radius;
   }

   public static void setOpacity(float value) {
      opacity = Math.max(0.0F, Math.min(1.0F, value));
   }
}
