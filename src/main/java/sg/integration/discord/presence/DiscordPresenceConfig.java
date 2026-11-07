package sg.integration.discord.presence;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import sg.integration.discord.presence.DiscordActivityButton;

public final class DiscordPresenceConfig {
   private final @com.google.gson.annotations.SerializedName("Э")  long clientId;
   @SerializedName("5")
   private final String detailsText;
   private final @com.google.gson.annotations.SerializedName("в")  String stateText;
   private final @com.google.gson.annotations.SerializedName("ъ")  String largeImageKey;
   private final @com.google.gson.annotations.SerializedName("М")  String largeImageText;
   private final @com.google.gson.annotations.SerializedName("Н")  String smallImageKey;
   @SerializedName("н")
   private final String smallImageText;
   @SerializedName("н")
   private final boolean showElapsedTime;
   private final @com.google.gson.annotations.SerializedName("Ц")  boolean showServerAddress;
   private final @com.google.gson.annotations.SerializedName("必")  long updateIntervalMillis;
   private final @com.google.gson.annotations.SerializedName("щ")  long reconnectIntervalMillis;
   private final @com.google.gson.annotations.SerializedName("А")  List<DiscordActivityButton> buttons;
   private static final String SINGLEPLAYER_LABEL = "Одиночная игра";

   public String getStateText() {
      return this.stateText;
   }

   public String getLargeImageText() {
      return this.largeImageText;
   }

   public DiscordPresenceConfig(
      long clientId,
      String detailsText,
      String stateText,
      String largeImageKey,
      String largeImageText,
      String smallImageKey,
      String smallImageText,
      boolean showElapsedTime,
      boolean showServerAddress,
      long updateIntervalMillis,
      long reconnectIntervalMillis,
      List<DiscordActivityButton> buttons
   ) {
      this.clientId = clientId;
      this.detailsText = detailsText;
      this.stateText = stateText;
      this.largeImageKey = largeImageKey;
      this.largeImageText = largeImageText;
      this.smallImageKey = smallImageKey;
      this.smallImageText = smallImageText;
      this.showElapsedTime = showElapsedTime;
      this.showServerAddress = showServerAddress;
      this.updateIntervalMillis = updateIntervalMillis;
      this.reconnectIntervalMillis = reconnectIntervalMillis;
      this.buttons = List.copyOf(buttons);
   }

   public String getDetailsText() {
      return this.detailsText;
   }

   public String resolveState(Minecraft minecraft) {
      if (!this.showServerAddress) {
         return this.stateText;
      }

      // В игре показываем адрес сервера, в меню — запасной текст.
      String address = this.resolveServerAddress(minecraft);
      return DiscordActivityButton.isBlank(address) ? this.stateText : address;
   }

   public String getSmallImageKey() {
      return this.smallImageKey;
   }

   public boolean isEnabled() {
      return this.clientId > 0L;
   }

   public long getUpdateIntervalMillis() {
      return this.updateIntervalMillis;
   }

   public long getReconnectIntervalMillis() {
      return this.reconnectIntervalMillis;
   }

   public String getLargeImageKey() {
      return this.largeImageKey;
   }

   public String getSmallImageText() {
      return this.smallImageText;
   }

   public List<DiscordActivityButton> getButtons() {
      return this.buttons;
   }

   private String resolveServerAddress(Minecraft minecraft) {
      if (minecraft == null || minecraft.level == null) {
         return "";
      }

      if (minecraft.isLocalServer()) {
         return SINGLEPLAYER_LABEL;
      }

      if (minecraft.getConnection() == null) {
         return "";
      }

      ServerData var2 = minecraft.getConnection().getServerData();
      return var2 == null ? "" : var2.ip;
   }

   public long getClientId() {
      return this.clientId;
   }

   public boolean showElapsedTime() {
      return this.showElapsedTime;
   }
}
