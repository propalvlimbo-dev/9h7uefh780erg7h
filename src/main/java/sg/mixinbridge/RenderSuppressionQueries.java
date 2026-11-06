package sg.mixinbridge;

import lombok.Generated;
import sg.client.RoseClient;
import sg.modules.impl.render.Rain;
import sg.events.impl.RenderSuppressionQuery;
import sg.modules.impl.render.Ambience;

public final class RenderSuppressionQueries {
   private static final String Х弟М = "This is a utility class and cannot be instantiated";

   public static boolean дЕ() {
      if (RoseClient.getInstance() == null) {
         return false;
      }

      Ambience var0 = RoseClient.getInstance().getServices().getModules().getAmbience();
      return Rain.Пз() || С(RenderSuppressionQuery.Effect.RAIN) || var0 != null && var0.isWeatherOverrideEnabled();
   }

   @Generated
   private RenderSuppressionQueries() {
      throw new UnsupportedOperationException(Х弟М);
   }

   public static boolean С(RenderSuppressionQuery.Effect var0) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RenderSuppressionQuery var1 = new RenderSuppressionQuery(var0);
         RoseClient.getInstance().getEventBus().post(var1);
         return var1.isCancelled();
      } else {
         return false;
      }
   }
}
