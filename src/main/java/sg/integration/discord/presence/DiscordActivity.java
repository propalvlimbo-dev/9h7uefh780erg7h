package sg.integration.discord.presence;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DiscordActivity {
   private @com.google.gson.annotations.SerializedName("Яф")  String details;
   private @com.google.gson.annotations.SerializedName("Яи")  String state;
   @SerializedName("В")
   private DiscordActivity.Assets assets;
   @SerializedName("В")
   private DiscordActivity.Timestamps timestamps;

   public void setSmallAsset(String imageKey, String hoverText) {
      if (this.assets == null) {
         this.assets = new DiscordActivity.Assets();
      }

      this.assets.smallImage = imageKey;
      this.assets.smallText = hoverText;
   }

   public void setState(String state) {
      this.state = state;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   public void setEndTimestamp(long epochSeconds) {
      if (this.timestamps == null) {
         this.timestamps = new DiscordActivity.Timestamps();
      }

      this.timestamps.end = epochSeconds;
   }

   public void setLargeAsset(String imageKey, String hoverText) {
      if (this.assets == null) {
         this.assets = new DiscordActivity.Assets();
      }

      this.assets.largeImage = imageKey;
      this.assets.largeText = hoverText;
   }

   public void setStartTimestamp(long epochSeconds) {
      if (this.timestamps == null) {
         this.timestamps = new DiscordActivity.Timestamps();
      }

      this.timestamps.start = epochSeconds;
   }

   public JsonObject toJson() {
      JsonObject var1 = new JsonObject();
      if (this.details != null) {
         var1.addProperty("details", this.details);
      }

      if (this.state != null) {
         var1.addProperty("state", this.state);
      }

      if (this.assets != null) {
         JsonObject var2 = new JsonObject();
         if (this.assets.largeImage != null) {
            var2.addProperty("large_image", this.assets.largeImage);
         }

         if (this.assets.largeText != null) {
            var2.addProperty("large_text", this.assets.largeText);
         }

         if (this.assets.smallImage != null) {
            var2.addProperty("small_image", this.assets.smallImage);
         }

         if (this.assets.smallText != null) {
            var2.addProperty("small_text", this.assets.smallText);
         }

         var1.add("assets", var2);
      }

      if (this.timestamps != null) {
         JsonObject var3 = new JsonObject();
         if (this.timestamps.start != null) {
            var3.addProperty("start", this.timestamps.start);
         }

         if (this.timestamps.end != null) {
            var3.addProperty("end", this.timestamps.end);
         }

         var1.add("timestamps", var3);
      }

      return var1;
   }

   public static class Timestamps {
      public @com.google.gson.annotations.SerializedName("В")  Long start;
      public @com.google.gson.annotations.SerializedName("Я")  Long end;
   }

   public static class Assets {
      public @com.google.gson.annotations.SerializedName("ВК")  String largeImage;
      public @com.google.gson.annotations.SerializedName("ВI")  String largeText;
      public @com.google.gson.annotations.SerializedName("ВТ")  String smallImage;
      public @com.google.gson.annotations.SerializedName("ВО")  String smallText;
   }
}
