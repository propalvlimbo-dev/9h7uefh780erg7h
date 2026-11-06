package sg.modules.impl.combat;

import sg.events.EventListener;
import sg.events.impl.Frame60Event;
import sg.network.ClientChat;
import sg.util.time.AimDatasetRecorder;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;

public class AiTester extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("н")  KeybindSetting recordKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("В")  AimDatasetRecorder datasetRecorder;
   private @com.google.gson.annotations.SerializedName("ВИ")  boolean recording;
   private final @com.google.gson.annotations.SerializedName("сн")  EventListener<InputEvent> inputListener;
   private final @com.google.gson.annotations.SerializedName("сЦ")  EventListener<Frame60Event> frame60Listener;
   private static final String MODULE_NAME = "AiTester";
   private static final String MODULE_DESCRIPTION = "Функция для разработчиков";
   private static final String RECORD_BIND_LABEL = "Record Bind";
   private static final String RECORDING_STOPPED_MESSAGE = "Stopped recording";
   private static final String RECORDING_STARTED_MESSAGE = "Started recording";

   public void Щ(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1 && var1.getInputCode() == this.recordKeybindSetting.getValue()) {
         if (this.recording) {
            ClientChat.З(RECORDING_STOPPED_MESSAGE);
            this.datasetRecorder.Кз();
            this.recording = false;
         } else {
            ClientChat.З(RECORDING_STARTED_MESSAGE);
            this.datasetRecorder.К4();
            this.recording = true;
         }
      }
   }

   public void method__(Frame60Event var1) {
      this.datasetRecorder.Пв(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
   }

   public AiTester() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.recordKeybindSetting = new KeybindSetting(RECORD_BIND_LABEL, -1);
      this.datasetRecorder = new AimDatasetRecorder();
      this.recording = false;
      this.inputListener = this::Щ;
      this.frame60Listener = this::method__;
      this.addSettings(this.recordKeybindSetting);
   }
}
