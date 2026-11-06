package sg.ec;

import com.google.gson.annotations.SerializedName;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

public class LuaRuntime {
   private static final LuaRuntime В_166b3054 = new LuaRuntime();
   @SerializedName("В")
   public final Globals В_b22603a6 = Д_b22603a6();

   private static Globals Д_b22603a6() {
      Globals var0 = new Globals();
      var0.load(new JseBaseLib());
      var0.load(new PackageLib());
      var0.load(new Bit32Lib());
      var0.load(new TableLib());
      var0.load(new StringLib());
      var0.load(new CoroutineLib());
      var0.load(new JseMathLib());
      LoadState.install(var0);
      LuaC.install(var0);
      return var0;
   }

   public static LuaRuntime Д_166b3054() {
      return В_166b3054;
   }

   private LuaRuntime() {
   }
}
