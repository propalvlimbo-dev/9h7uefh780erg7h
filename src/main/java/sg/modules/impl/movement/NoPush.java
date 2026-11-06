package sg.modules.impl.movement;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public class NoPush extends ClientModule {
   public MultiChoiceSetting Ю;
   private static final String Втъ = "NoPush";
   private static final String ВтМ = "Убирает отталкивание от блоков и игроков";
   private static final String ВтН = "Отталкивания";
   private static final String Втн = "Игроки";
   private static final String ВтЦ = "Блоки";
   private static final String Втч = "Вода";
   private static final String ВтЭ = "Удочка";

   public NoPush() {
      super(Втъ, ВтМ, ModuleCategory.MOVEMENT);
      this.Ю = new MultiChoiceSetting(
         ВтН, new ChoiceOption(Втн, true), new ChoiceOption(ВтЦ, false), new ChoiceOption(Втч, false), new ChoiceOption(ВтЭ, false)
      );
      this.addSettings(this.Ю);
   }
}
