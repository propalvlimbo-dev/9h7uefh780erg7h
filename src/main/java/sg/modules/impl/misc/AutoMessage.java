package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.settings.TextSetting;

public class AutoMessage extends ClientModule {
   @SerializedName("4")
   private final TextSetting messageSetting;
   private final @com.google.gson.annotations.SerializedName("Хч")  FloatSetting delaySecondsSetting;
   private final @com.google.gson.annotations.SerializedName("ч")  Stopwatch sendTimer;
   private final @com.google.gson.annotations.SerializedName("Хт")  EventListener<PlayerTickEvent> playerTickListener;
   private static final String MODULE_NAME = "AutoMessage";
   private static final String MODULE_DESCRIPTION = "Отправляет сообщение";
   private static final String MESSAGE_LABEL = "Message";
   private static final String DEFAULT_MESSAGE = "Hello!";
   private static final String DELAY_LABEL = "Delay";
   private static final float DEFAULT_DELAY_SECONDS = 30.0F;
   private static final float MAX_DELAY_SECONDS = 120.0F;
   private static final long MILLIS_PER_SECOND = 1000L;

   public void ух(PlayerTickEvent var1) {
      if (this.sendTimer.Т(this.delaySecondsSetting.getValue().longValue() * MILLIS_PER_SECOND)) {
         ClientChat.т(this.messageSetting.getValue());
         this.sendTimer.ВД();
      }
   }

   public AutoMessage() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.messageSetting = new TextSetting(MESSAGE_LABEL, DEFAULT_MESSAGE);
      this.delaySecondsSetting = new FloatSetting(DELAY_LABEL, DEFAULT_DELAY_SECONDS, 1.0F, MAX_DELAY_SECONDS).withStep(1.0F);
      this.sendTimer = new Stopwatch();
      this.playerTickListener = this::ух;
      this.addSettings(this.messageSetting, this.delaySecondsSetting);
   }
}
