package sg.modules.impl.init;

import java.util.concurrent.CopyOnWriteArrayList;

public class StaffNameRegistry extends CopyOnWriteArrayList<String> {
   public boolean щ(String var1) {
      return var1 != null && this.stream().anyMatch(var1::equalsIgnoreCase);
   }
}
