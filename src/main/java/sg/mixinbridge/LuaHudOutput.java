package sg.mixinbridge;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import sg.annotation.LuaExport;

public class LuaHudOutput {
   private ArrayList<String> Л = new ArrayList<>();

   public static <T> ArrayList<T> ъ(ArrayList<T> var0) {
      LinkedHashSet var1 = new LinkedHashSet(var0);
      var0.clear();
      var0.addAll(var1);
      return var0;
   }

   public void 必弟() {
      this.Л.clear();
   }

   @LuaExport("out")
   public void out(String message) {
      this.Л.add(message);
   }

   public ArrayList<String> с() {
      return ъ(this.Л);
   }
}
