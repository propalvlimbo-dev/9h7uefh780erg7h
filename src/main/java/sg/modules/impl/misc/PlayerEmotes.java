package sg.modules.impl.misc;

import sg.events.EventListener;
import sg.gui.EmoteWheelScreen;
import sg.settings.BooleanSetting;
import sg.util.time.EmoteSyncController;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;
import sg.settings.SingleChoiceSetting;

public final class PlayerEmotes extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Р")  KeybindSetting emoteMenuKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("Я1")  BooleanSetting keepWhileMovingSetting;
   private final @com.google.gson.annotations.SerializedName("сб")  SingleChoiceSetting visibilitySetting;
   private final @com.google.gson.annotations.SerializedName("Х3")  EventListener<InputEvent> inputListener;
   private final @com.google.gson.annotations.SerializedName("Хр")  EventListener<HudRenderEvent> hudRenderListener;
   private @com.google.gson.annotations.SerializedName("В")  EmoteWheelScreen wheelScreen;
   private static final String MODULE_NAME = "PlayerEmotes";
   private static final String MODULE_DESCRIPTION = "Колесо эмоций";
   private static final String EMOTE_MENU_LABEL = "Emote menu";
   private static final String KEEP_WHILE_MOVING_LABEL = "Keep While Moving";
   private static final String VISIBILITY_LABEL = "Видимость";
   private static final String ALL_OPTION_LABEL = "Все";
   private static final String PARTY_ONLY_OPTION_LABEL = "Только пати";
   private static final String PARTY_ONLY_MODE_NAME = "Только пати";

   private void у(InputEvent var1) {
      if (var1.getInputCode() == this.emoteMenuKeybindSetting.getValue()) {
         if (var1.getAction() == 1 && MINECRAFT_CLIENT.gui.screen() == null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player == MINECRAFT_CLIENT.getCameraEntity()) {
            MINECRAFT_CLIENT.gui.setScreen(new EmoteWheelScreen(this, this.emoteMenuKeybindSetting.getValue()));
         } else if (var1.getAction() == 0 && MINECRAFT_CLIENT.gui.screen() instanceof EmoteWheelScreen var2) {
            var2.finish(true);
         }
      }
   }

   public void р(EmoteWheelScreen var1) {
      this.wheelScreen = var1;
   }

   public PlayerEmotes() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.emoteMenuKeybindSetting = new KeybindSetting(EMOTE_MENU_LABEL, 66);
      this.keepWhileMovingSetting = new BooleanSetting(KEEP_WHILE_MOVING_LABEL, false);
      this.visibilitySetting = new SingleChoiceSetting(VISIBILITY_LABEL, new ChoiceOption(ALL_OPTION_LABEL), new ChoiceOption(PARTY_ONLY_OPTION_LABEL));
      this.inputListener = this::у;
      this.hudRenderListener = this::西е;
      this.addSettings(this.emoteMenuKeybindSetting, this.keepWhileMovingSetting, this.visibilitySetting);
      EmoteSyncController.Ы().Ы(this::isEnabled, () -> this.visibilitySetting.isSelected(PARTY_ONLY_MODE_NAME), this.keepWhileMovingSetting::getValue);
   }

   @Override
   public void onDisable() {
      EmoteSyncController.Ы().l6();
      if (MINECRAFT_CLIENT.gui.screen() instanceof EmoteWheelScreen var1) {
         var1.finish(false);
      }

      this.wheelScreen = null;
   }

   private void 西е(HudRenderEvent var1) {
      if (this.wheelScreen != null && var1.getGraphics() != null) {
         if (this.wheelScreen.renderClosing(var1.getGraphics())) {
            this.wheelScreen = null;
         }
      }
   }
}
