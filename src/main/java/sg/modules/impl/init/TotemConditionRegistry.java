package sg.modules.impl.init;

import java.util.concurrent.CopyOnWriteArrayList;
import sg.ec.MissingArmorTotemTrigger;
import sg.ec.HealthTotemTrigger;
import sg.ec.NearbyAnchorTotemTrigger;
import sg.ec.FallingTotemTrigger;
import sg.ec.NearbyTntTotemTrigger;
import sg.ec.TotemTrigger;
import sg.modules.impl.combat.AutoTotem;

public class TotemConditionRegistry extends CopyOnWriteArrayList<TotemTrigger> {
   public TotemConditionRegistry(AutoTotem var1) {
      this.add(new HealthTotemTrigger());
      this.add(new FallingTotemTrigger());
      this.add(new CrystalTotemCondition());
      this.add(new NearbyTntTotemTrigger());
      this.add(new MissingArmorTotemTrigger());
      this.add(new NearbyAnchorTotemTrigger());

      for (TotemTrigger var3 : this) {
         var3.а(var1);
      }
   }

   public boolean ЬК() {
      for (TotemTrigger var2 : this) {
         if (var2.ац()) {
            return true;
         }
      }

      return false;
   }
}
