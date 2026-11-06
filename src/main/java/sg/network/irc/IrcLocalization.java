package sg.network.irc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map.Entry;
import lombok.Generated;
import settings.render.EnglishLanguageResource;
import sg.network.irc.IrcLanguageResource;

public class IrcLocalization {
   private final HashMap<String, String> Ъ = new HashMap<>();
   private IrcLanguageResource В = new EnglishLanguageResource();

   @Generated
   public HashMap<String, String> д() {
      return this.Ъ;
   }

   public IrcLocalization() {
      this.Бн();
   }

   public void д(IrcLanguageResource var1) {
      this.В = var1;
      this.Бн();
   }

   private void Бн() {
      String var1 = this.В.ар();
      if (var1 == null) {
         System.out.println("Error with language!");
      } else {
         JsonObject var2 = JsonParser.parseString(var1).getAsJsonObject();
         this.Ъ.clear();

         for (Entry var4 : var2.entrySet()) {
            this.Ъ.put((String)var4.getKey(), ((JsonElement)var4.getValue()).getAsString());
         }
      }
   }
}
