package sg.modules.impl.init;

import settings.render.ClientSoundManager;
import sg.settings.ChoiceOption;

public enum SoundEffectPreset {
   SHTREBUX("Штребух", "shtrebux"),
   HONEY("МЁД ПО ТЕЛУ", "med"),
   OAR("Весло", "weslo");

   private final String сЪ;
   private final String ст;

   SoundEffectPreset(String var3, String var4) {
      this.сЪ = var3;
      this.ст = var4;
   }

   public static ChoiceOption[] ъ() {
      SoundEffectPreset[] var0 = values();
      ChoiceOption[] var1 = new ChoiceOption[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = new ChoiceOption(var0[var2].сЪ);
      }

      return var1;
   }

   public static SoundEffectPreset ъ(String var0) {
      for (SoundEffectPreset var4 : values()) {
         if (var4.сЪ.equals(var0)) {
            return var4;
         }
      }

      return SHTREBUX;
   }

   public String Ьз() {
      return this.сЪ;
   }

   public void К(float var1) {
      ClientSoundManager.method_7().method_4(this.ст, var1);
   }
}
