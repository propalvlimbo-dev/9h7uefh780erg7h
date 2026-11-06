package sg.hud;

import lombok.Generated;
import sg.client.RoseClient;

public final class HudScaleCoordinates {
   private static final String UTILITY_CLASS_MESSAGE = "This is a utility class and cannot be instantiated";

   public static float unscaleY(float y, float originY) {
      return originY + (y - originY) / getHudScale();
   }

   public static float unscaleX(float x, float originX) {
      return originX + (x - originX) / getHudScale();
   }

   public static float getHudScale() {
      try {
         return RoseClient.getInstance() != null
               && RoseClient.getInstance().getServices() != null
               && RoseClient.getInstance().getServices().getModules() != null
               && RoseClient.getInstance().getServices().getModules().getHud() != null
            ? RoseClient.getInstance().getServices().getModules().getHud().зй()
            : 1.0F;
      } catch (Throwable ignored) {
         return 1.0F;
      }
   }

   @Generated
   private HudScaleCoordinates() {
      throw new UnsupportedOperationException(UTILITY_CLASS_MESSAGE);
   }
}
