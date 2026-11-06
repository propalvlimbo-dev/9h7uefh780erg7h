package sg.modules.impl.misc;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public final class IrcModule extends ClientModule {
   private static final String зи = "IRC";
   private static final String з9 = "Глобальный чат пользователей клиента";

   public IrcModule() {
      super(зи, з9, ModuleCategory.MISC);
      this.setEnabledFlag(true);
   }
}
