package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import sg.events.EventListener;
import sg.render.avatar.WindowsMediaSession;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;
import sg.modules.impl.init.NotificationManager;

public class MusicPlayer extends ClientModule {
   @SerializedName("Х")
   private final KeybindSetting playPauseKeybindSetting;
   @SerializedName("е")
   private final KeybindSetting nextTrackKeybindSetting;
   @SerializedName("А")
   private final KeybindSetting previousTrackKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("а")  BooleanSetting notificationsSetting;
   private final @com.google.gson.annotations.SerializedName("Ю")  EventListener<InputEvent> inputListener;
   private static final String MODULE_NAME = "MusicPlayer";
   private static final String MODULE_DESCRIPTION = "Плеер того, что играет в браузере";
   private static final String PLAY_PAUSE_KEY_LABEL = "Кнопка плей/пауза";
   private static final String NEXT_TRACK_KEY_LABEL = "Кнопка следующего трека";
   private static final String PREVIOUS_TRACK_KEY_LABEL = "Кнопка предыдущего трека";
   private static final String NOTIFICATIONS_LABEL = "Уведомления";
   private static final String PAUSED_MESSAGE = "Пауза";
   private static final String NEXT_TRACK_MESSAGE = "Следующий трек";
   private static final String PREVIOUS_TRACK_MESSAGE = "Предыдущий трек";
   private static final String PLAYING_MESSAGE = "Воспроизведение";
   private static final String NOTIFICATION_ICON_LABEL = "M";
   private static final String NOTIFICATION_TITLE = "Музыка";

   private void я(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1) {
         int var2 = var1.getInputCode();
         if (var2 != -1) {
            if (var2 == this.playPauseKeybindSetting.getValue()) {
               this.必_de5a6f78();
            } else if (var2 == this.nextTrackKeybindSetting.getValue()) {
               this.ВВ();
            } else if (var2 == this.previousTrackKeybindSetting.getValue()) {
               this.Вм();
            }
         }
      }
   }

   public MusicPlayer() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.playPauseKeybindSetting = new KeybindSetting(PLAY_PAUSE_KEY_LABEL, -1);
      this.nextTrackKeybindSetting = new KeybindSetting(NEXT_TRACK_KEY_LABEL, -1);
      this.previousTrackKeybindSetting = new KeybindSetting(PREVIOUS_TRACK_KEY_LABEL, -1);
      this.notificationsSetting = new BooleanSetting(NOTIFICATIONS_LABEL, true);
      this.inputListener = this::я;
      this.addSettings(this.playPauseKeybindSetting, this.nextTrackKeybindSetting, this.previousTrackKeybindSetting, this.notificationsSetting);
   }

   private String м(WindowsMediaSession var1) {
      return var1.Цl() ? var1.Сй() : PLAYING_MESSAGE;
   }

   public void ВВ() {
      WindowsMediaSession.П_1751811a().УЮ();
      this.щ(NEXT_TRACK_MESSAGE);
   }

   private void щ(String var1) {
      if (this.notificationsSetting.getValue()) {
         NotificationManager.р(NOTIFICATION_ICON_LABEL, var1, NOTIFICATION_TITLE, -1);
      }
   }

   public void 必_de5a6f78() {
      WindowsMediaSession var1 = WindowsMediaSession.П_1751811a();
      boolean var2 = var1.Ц7();
      var1.УН();
      this.щ(var2 ? PAUSED_MESSAGE : this.м(var1));
   }

   @Override
   public void onEnable() {
      WindowsMediaSession.П_1751811a().Ул();
   }

   public void method__(int var1) {
      WindowsMediaSession.П_1751811a().Фи(var1);
   }

   @Override
   public void onDisable() {
      WindowsMediaSession.П_1751811a().Уц();
   }

   public void Вм() {
      WindowsMediaSession.П_1751811a().Уг();
      this.щ(PREVIOUS_TRACK_MESSAGE);
   }
}
