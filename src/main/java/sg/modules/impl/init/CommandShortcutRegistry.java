package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import sg.command.KeyMacro;
import sg.ec.KeyNameRegistry;

public class CommandShortcutRegistry extends CopyOnWriteArrayList<KeyMacro> {
   public List<String> А() {
      ArrayList var1 = new ArrayList();

      for (KeyMacro var3 : this) {
         var1.add(KeyNameRegistry.сч.get(var3.О()));
      }

      return var1;
   }
}
