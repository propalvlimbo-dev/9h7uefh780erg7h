package sg.ec;

import java.util.function.Consumer;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import sg.render.ScriptedParticle;

public class LuaParticleCallback implements Consumer<ScriptedParticle> {
   private final LuaFunction В;

   public LuaParticleCallback(LuaFunction var1) {
      this.В = var1;
   }

   public void accept(ScriptedParticle var1) {
      try {
         this.В.call(CoerceJavaToLua.coerce(var1));
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
