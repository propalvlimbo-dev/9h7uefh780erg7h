package sg.modules.impl.init;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public enum NotificationCategory {
   ARMOR("Броня"),
   EFFECTS("Эффекты зелий"),
   SELF_USE("Мои предметы"),
   PLAYER_USE("Предметы игроков"),
   TNT("TNT рядом"),
   MODULES("Модули"),
   OTHER("Прочее");

   private final String сф;

   NotificationCategory(String var3) {
      this.сф = var3;
   }

   public boolean ъ(MultiChoiceSetting var1) {
      return var1.isSelected(this.сф);
   }

   public static MultiChoiceSetting р() {
      NotificationCategory[] var0 = values();
      ChoiceOption[] var1 = new ChoiceOption[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = new ChoiceOption(var0[var2].сф, true);
      }

      return new MultiChoiceSetting("Типы уведомлений", var1);
   }
}
