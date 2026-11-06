package sg.modules.impl.init;

import lombok.Generated;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public enum WatermarkField {
   CLIENT("Клиент", true),
   USERNAME("Ник", true),
   FPS("FPS", true),
   PING("Пинг", true),
   CFG("Конфиг", false),
   IP("Айпи сервера", false),
   LOSS("MS Loss", false),
   USEMEMORY("Используемая память", false),
   TPS("ТПС", false),
   GPU("GPU", false),
   TIME("Время", false),
   BRAND("Версия клиента", false);

   private final String В;
   private final boolean е;

   public boolean Э(MultiChoiceSetting var1) {
      return var1.isSelected(this.В);
   }

   public static MultiChoiceSetting Э() {
      ChoiceOption[] var0 = new ChoiceOption[values().length];

      for (int var1 = 0; var1 < var0.length; var1++) {
         var0[var1] = new ChoiceOption(values()[var1].В, values()[var1].е);
      }

      return new MultiChoiceSetting("Элементы WaterMark", var0);
   }

   @Generated
   public String щ() {
      return this.В;
   }

   @Generated
   public boolean method_5() {
      return this.е;
   }

   @Generated
   WatermarkField(String var3, boolean var4) {
      this.В = var3;
      this.е = var4;
   }
}
