package sg.modules.impl.combat;

import sg.events.EventListener;
import sg.events.impl.Frame60Event;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.StandaloneOnnxAim60hz;

public class AiAimbot extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("В")  StandaloneOnnxAim60hz onnxAimController;
   private final @com.google.gson.annotations.SerializedName("л")  EventListener<Frame60Event> frame60Listener = this::ш;
   private static final String MODULE_NAME = "AiAimbot";
   private static final String MODULE_DESCRIPTION = "Функция для разработчиков";

   public void ш(Frame60Event var1) {
      this.onnxAimController.ЙС();
      StandaloneOnnxAim60hz.ж(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
   }

   public AiAimbot() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.onnxAimController = new StandaloneOnnxAim60hz();
      this.onnxAimController.Йд();
   }
}
