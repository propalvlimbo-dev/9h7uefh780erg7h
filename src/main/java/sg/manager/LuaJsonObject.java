package sg.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import sg.annotation.LuaExport;

public class LuaJsonObject {
   private final JsonObject Ю;

   public LuaJsonObject(JsonObject var1) {
      this.Ю = var1;
   }

   @LuaExport
   public Object а(String var1) {
      JsonElement var2 = this.Ю.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         if (var2.isJsonPrimitive()) {
            JsonPrimitive var3 = var2.getAsJsonPrimitive();
            if (var3.isBoolean()) {
               return var3.getAsBoolean();
            }

            if (var3.isNumber()) {
               Number var4 = var3.getAsNumber();
               double var5 = var4.doubleValue();
               if (var5 == (int)var5) {
                  return (int)var5;
               }

               if (var5 == (long)var5) {
                  return (long)var5;
               }

               return var5;
            }

            if (var3.isString()) {
               return var3.getAsString();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @LuaExport
   public LuaJsonObject м(String var1) {
      JsonElement var2 = this.Ю.get(var1);
      return var2 != null && var2.isJsonObject() ? new LuaJsonObject(var2.getAsJsonObject()) : null;
   }
}
