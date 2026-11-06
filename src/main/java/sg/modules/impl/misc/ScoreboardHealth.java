package sg.modules.impl.misc;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public class ScoreboardHealth extends ClientModule {
   private static final String MODULE_NAME = "Scoreboard HP";
   private static final String MODULE_DESCRIPTION = "Получает здоровье из скорборда";

   public ScoreboardHealth() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }
}
