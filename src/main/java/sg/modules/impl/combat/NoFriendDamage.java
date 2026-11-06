package sg.modules.impl.combat;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public class NoFriendDamage extends ClientModule {
   private static final String Вlу = "NoFriendDamage";
   private static final String Вl2 = "Отменяет урон по друзьям";

   public NoFriendDamage() {
      super(Вlу, Вl2, ModuleCategory.COMBAT);
   }
}
