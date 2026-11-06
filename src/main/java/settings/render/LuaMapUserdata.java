package settings.render;

import java.util.HashMap;
import java.util.Map;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

public final class LuaMapUserdata extends LuaUserdata {
   private final LuaValue Д;
   private final LuaTable Я = new LuaTable();
   private final Map<LuaValue, String> ВЬ = new HashMap<>();
   private static final String Р9 = "get";
   private static final String Ро = "getOrDefault";
   private static final String РO = "put";

   @Override
   public LuaValue get(LuaValue var1) {
      LuaValue var2 = this.Я.rawget(var1);
      return var2.isnil() ? this.Д.get(var1) : var2;
   }

   public static LuaValue of(Map<?, ?> var0) {
      return new LuaMapUserdata(var0);
   }

   @Override
   public void set(LuaValue var1, LuaValue var2) {
      this.Д.set(var1, var2);
   }

   private String key(LuaValue var1) {
      String var2 = this.ВЬ.get(var1);
      if (var2 == null) {
         var2 = var1.tojstring();
         if (this.ВЬ.size() >= 2048) {
            this.ВЬ.clear();
         }

         this.ВЬ.put(var1, var2);
      }

      return var2;
   }

   private LuaMapUserdata(final Map<?, ?> var1) {
      super(var1);
      this.Д = CoerceJavaToLua.coerce(var1);
      final Map var2 = var1;
      this.Я
         .set(
            Р9,
            new TwoArgFunction() {
               private static final String сюс = "get";

               @Override
               public LuaValue call(LuaValue var1x, LuaValue var2x) {
                  return var1x.touserdata() == var1 && var2x.type() == 4
                     ? CoerceJavaToLua.coerce(var2.get(LuaMapUserdata.this.key(var2x)))
                     : LuaMapUserdata.this.Д.get(сюс).call(var1x, var2x);
               }
            }
         );
      this.Я.set(Ро, new ThreeArgFunction() {
         private static final String Вв5 = "getOrDefault";

         @Override
         public LuaValue call(LuaValue var1x, LuaValue var2x, LuaValue var3) {
            if (var1x.touserdata() == var1 && var2x.type() == 4) {
               String var4 = LuaMapUserdata.this.key(var2x);
               Object var5 = var2.get(var4);
               if (var5 == null && !var2.containsKey(var4)) {
                  var5 = CoerceLuaToJava.coerce(var3, Object.class);
               }

               return CoerceJavaToLua.coerce(var5);
            } else {
               return LuaMapUserdata.this.Д.get(Вв5).call(var1x, var2x, var3);
            }
         }
      });
      this.Я
         .set(
            РO,
            new ThreeArgFunction() {
               private static final String field_3Ю = "put";

               @Override
               public LuaValue call(LuaValue var1x, LuaValue var2x, LuaValue var3) {
                  return var1x.touserdata() == var1 && var2x.type() == 4
                     ? CoerceJavaToLua.coerce(var2.put(LuaMapUserdata.this.key(var2x), CoerceLuaToJava.coerce(var3, Object.class)))
                     : LuaMapUserdata.this.Д.get(field_3Ю).call(var1x, var2x, var3);
               }
            }
         );
   }
}
