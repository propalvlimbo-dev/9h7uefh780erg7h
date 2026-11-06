package com.deluxe.internal;

import net.minecraft.client.Minecraft;

public final class GuiScale {
   public static float getBaseScale() {
      return 2.0F;
   }

   public static float getFramebufferHeight() {
      Minecraft client = Minecraft.getInstance();
      if (client == null) {
         return 0.0F;
      }

      return client.getWindow() == null ? 0.0F : client.getWindow().getScreenHeight();
   }

   public static float getWindowScale() {
      Minecraft client = Minecraft.getInstance();
      if (client == null) {
         return 1.0F;
      }

      return client.getWindow() == null ? 1.0F : Math.max(1.0F, client.getWindow().getGuiScale());
   }

   public static int toGuiPixels(float coordinate) {
      return Math.round(toGuiUnits(coordinate));
   }

   private GuiScale() {
   }

   public static float getMatrixScale() {
      return 2.0F / getWindowScale();
   }

   public static float toGuiUnits(float coordinate) {
      return coordinate * getMatrixScale();
   }
}
