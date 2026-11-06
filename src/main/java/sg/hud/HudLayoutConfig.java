package sg.hud;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sg.client.RoseClient;
import sg.hud.Notifications;
import sg.hud.HudElement;
import sg.hud.Scoreboard;
import sg.config.JsonConfigFile;

public class HudLayoutConfig extends JsonConfigFile {
   private static final String сТМ = "rose/client";
   private static final String сТН = "draggable";
   private static final String сТн = "hud_without_config";
   private static final String сТЦ = "x";
   private static final String сТч = "y";
   private static final String сТЭ = "locked";
   private static final String сТ必 = "x";
   private static final String сТщ = "x";
   private static final String сТГ = "y";
   private static final String сТл = "y";
   private static final String сТэ = "locked";
   private static final String сТю = "locked";
   private static final String сТЩ = ":";

   @Override
   protected void loadContents() {
      if (!this.readFileContents().isEmpty()) {
         JsonParser var1 = new JsonParser();
         JsonObject var2 = var1.parse(this.readFileContents()).getAsJsonObject();
         Ы(var2);
      }
   }

   public HudLayoutConfig() {
      super(сТМ, сТН);
   }

   public static void Ы(JsonObject var0) {
      for (HudElement var2 : RoseClient.getInstance().getServices().getHudElements()) {
         if (var0.has(var2.getName())) {
            if (var0.get(var2.getName()).isJsonObject()) {
               JsonObject var3 = var0.getAsJsonObject(var2.getName());
               if (var3.has(сТ必)) {
                  var2.setX(var3.get(сТщ).getAsFloat());
               }

               if (var3.has(сТГ)) {
                  var2.setBaseY(var3.get(сТл).getAsFloat());
               }

               if (var2 instanceof Scoreboard var4 && var3.has(сТэ)) {
                  var4._д(var3.get(сТю).getAsBoolean());
               }
            } else {
               String[] var5 = var0.get(var2.getName()).getAsString().split(сТЩ);
               if (var5.length == 2) {
                  var2.setX(Float.parseFloat(var5[0]));
                  var2.setBaseY(Float.parseFloat(var5[1]));
               }
            }
         }
      }
   }

   public static JsonObject Ы() {
      JsonObject var0 = new JsonObject();

      for (HudElement var2 : RoseClient.getInstance().getServices().getHudElements()) {
         if (!var2.getName().equals(сТн) && !(var2 instanceof Scoreboard var3 && !var3.П弟())) {
            JsonObject var5 = new JsonObject();
            var5.addProperty(сТЦ, var2.getX());
            var5.addProperty(сТч, var2 instanceof Notifications var4 ? var4.method_6К() : var2.getY());
            if (var2 instanceof Scoreboard var6) {
               var5.addProperty(сТЭ, var6.ПХ());
            }

            var0.add(var2.getName(), var5);
         }
      }

      return var0;
   }

   @Override
   protected void saveContents() {
      this.writeJson(Ы());
   }
}
