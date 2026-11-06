package sg.ec;

import lombok.Generated;
import sg.modules.impl.combat.AutoTotem;

public abstract class TotemTrigger implements MinecraftAccess {
   protected AutoTotem В;

   public abstract boolean ац();

   @Generated
   public void а(AutoTotem var1) {
      this.В = var1;
   }
}
