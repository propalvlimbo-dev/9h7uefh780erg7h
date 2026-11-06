package sg.ec;

import net.minecraft.resources.Identifier;
import sg.annotation.LuaExport;

public class LuaIdentifiers {
   @LuaExport("of")
   public Identifier Ш(String var1, String var2) {
      return Identifier.fromNamespaceAndPath(var1, var2);
   }
}
