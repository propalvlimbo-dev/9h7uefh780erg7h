package sg.modules.impl.misc;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.rotation.RotationStrategy;

public class FreeLook extends ClientModule {
   private static final String сМЙ = "FreeLook";
   private static final String сМк = "Позволяет осмотреться от третьего лица без отображения другим игрокам";

   @Override
   public void onDisable() {
      RoseClient.getInstance().getServices().getRotations().activateStrategy(null);
   }

   public FreeLook() {
      super(сМЙ, сМк, ModuleCategory.MISC);
   }

   @Override
   public void onEnable() {
      RoseClient.getInstance().getServices().getRotations().activateStrategy(new RotationStrategy(null));
   }
}
