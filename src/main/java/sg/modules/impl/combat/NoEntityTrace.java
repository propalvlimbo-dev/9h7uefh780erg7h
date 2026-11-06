package sg.modules.impl.combat;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public class NoEntityTrace extends ClientModule {
   private static final String сЮЦ = "NoEntityTrace";
   private static final String сЮч = "Отменяет урон по сущностям";

   public NoEntityTrace() {
      super(сЮЦ, сЮч, ModuleCategory.COMBAT);
   }
}
