package sg.integration.discord.presence;

import com.google.gson.JsonObject;

public final class DiscordActivityButton {
   private final @com.google.gson.annotations.SerializedName("ш")  String label;
   private final @com.google.gson.annotations.SerializedName("И")  String url;
   private static final String LABEL_KEY = "label";
   private static final String URL_KEY = "url";

   public static boolean isBlank(String value) {
      return value == null || value.isBlank();
   }

   public JsonObject toJson() {
      JsonObject var1 = new JsonObject();
      var1.addProperty(LABEL_KEY, this.label);
      var1.addProperty(URL_KEY, this.url);
      return var1;
   }

   public String getLabel() {
      return this.label;
   }

   public boolean isValid() {
      return !isBlank(this.label) && !isBlank(this.url);
   }

   public String getUrl() {
      return this.url;
   }

   public DiscordActivityButton(String label, String url) {
      this.label = label;
      this.url = url;
   }
}
