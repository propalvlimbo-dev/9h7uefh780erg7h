package sg.integration.discord.presence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import sg.integration.discord.presence.DiscordActivityButton;

public final class ConfiguredDiscordActivity extends DiscordActivity {
   private final @com.google.gson.annotations.SerializedName("Я")  DiscordPresenceConfig config;
   private static final String BUTTONS_KEY = "buttons";

   private String nullIfBlank(String text) {
      return DiscordActivityButton.isBlank(text) ? null : text;
   }

   @Override
   public JsonObject toJson() {
      JsonObject var1 = super.toJson();
      JsonArray var2 = new JsonArray();
      int var3 = 0;

      for (DiscordActivityButton var5 : this.config.getButtons()) {
         if (var5.isValid()) {
            var2.add(var5.toJson());
            if (++var3 == 2) {
               break;
            }
         }
      }

      if (var2.size() > 0) {
         var1.add(BUTTONS_KEY, var2);
      }

      return var1;
   }

   public ConfiguredDiscordActivity(DiscordPresenceConfig config, Minecraft minecraft, long sessionStartSeconds) {
      this.config = config;
      this.setIfPresent(config.getDetailsText(), this::setDetails);
      this.setIfPresent(config.resolveState(minecraft), this::setState);
      if (!DiscordActivityButton.isBlank(config.getLargeImageKey())) {
         this.setLargeAsset(config.getLargeImageKey(), this.nullIfBlank(config.getLargeImageText()));
      }

      if (!DiscordActivityButton.isBlank(config.getSmallImageKey())) {
         this.setSmallAsset(config.getSmallImageKey(), this.nullIfBlank(config.getSmallImageText()));
      }

      if (config.showElapsedTime()) {
         this.setStartTimestamp(sessionStartSeconds);
      }
   }

   private void setIfPresent(String value, ConfiguredDiscordActivity.TextSetter setter) {
      if (!DiscordActivityButton.isBlank(value)) {
         setter.set(value);
      }
   }

   private interface TextSetter {
      void set(String value);
   }
}
