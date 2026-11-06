package sg.modules.impl.misc;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public class NoInteract extends ClientModule {
   public static final NoInteract Я_at_1211d0f0 = new NoInteract();
   private static final String ХЫР = "NoInteract";
   private static final String ХЫ3 = "Запрещает взаимодействие с блоками";

   public NoInteract() {
      super(ХЫР, ХЫ3, ModuleCategory.MISC);
   }
}
