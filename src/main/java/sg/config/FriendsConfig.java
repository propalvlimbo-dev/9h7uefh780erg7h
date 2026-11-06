package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map.Entry;
import sg.modules.impl.init.FriendEntry;
import sg.client.RoseClient;
import sg.config.JsonConfigFile;

public class FriendsConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY = "rose/client";
   private static final String CONFIG_NAME = "friend";
   private static final String SAVE_FRIEND_NAME_KEY = "name";
   private static final String SAVE_FAKE_NAME_KEY = "fakeName";
   private static final String CHECK_FRIEND_NAME_KEY = "name";
   private static final String CHECK_FRIEND_NAME_NOT_NULL_KEY = "name";
   private static final String READ_FRIEND_NAME_KEY = "name";
   private static final String CHECK_FAKE_NAME_KEY = "fakeName";
   private static final String CHECK_FAKE_NAME_NOT_NULL_KEY = "fakeName";
   private static final String READ_FAKE_NAME_KEY = "fakeName";

   @Override
   protected void loadContents() {
      if (!this.readFileContents().isEmpty()) {
         JsonParser jsonParser = new JsonParser();
         JsonObject friendsJson = jsonParser.parse(this.readFileContents()).getAsJsonObject();
         RoseClient.getInstance().getServices().getFriends().clear();

         for (Entry friendEntry : friendsJson.entrySet()) {
            String friendName = (String)friendEntry.getKey();
            String fakeName = null;
            JsonElement friendElement = (JsonElement)friendEntry.getValue();
            if (friendElement.isJsonObject()) {
               JsonObject friendJson = friendElement.getAsJsonObject();
               if (friendJson.has(CHECK_FRIEND_NAME_KEY) && !friendJson.get(CHECK_FRIEND_NAME_NOT_NULL_KEY).isJsonNull()) {
                  friendName = friendJson.get(READ_FRIEND_NAME_KEY).getAsString();
               }

               if (friendJson.has(CHECK_FAKE_NAME_KEY) && !friendJson.get(CHECK_FAKE_NAME_NOT_NULL_KEY).isJsonNull()) {
                  fakeName = friendJson.get(READ_FAKE_NAME_KEY).getAsString();
               }
            } else if (friendElement.isJsonPrimitive()) {
               String legacyFakeName = friendElement.getAsString();
               if (!legacyFakeName.equalsIgnoreCase(friendName)) {
                  fakeName = legacyFakeName;
               }
            }

            RoseClient.getInstance().getServices().getFriends().К(friendName, fakeName);
         }
      }
   }

   public FriendsConfig() {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
   }

   @Override
   protected void saveContents() {
      JsonObject friendsJson = new JsonObject();

      for (FriendEntry friend : RoseClient.getInstance().getServices().getFriends()) {
         JsonObject friendJson = new JsonObject();
         friendJson.addProperty(SAVE_FRIEND_NAME_KEY, friend.lз());
         if (friend.lк()) {
            friendJson.addProperty(SAVE_FAKE_NAME_KEY, friend.l3());
         }

         friendsJson.add(friend.lз(), friendJson);
      }

      this.writeJson(friendsJson);
   }
}
