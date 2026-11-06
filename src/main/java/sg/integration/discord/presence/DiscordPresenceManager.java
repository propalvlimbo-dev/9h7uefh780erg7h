package sg.integration.discord.presence;

import com.google.gson.annotations.SerializedName;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import sg.integration.discord.ipc.DiscordIpcClient;
import sg.util.time.Stopwatch;

public final class DiscordPresenceManager {
   @SerializedName("В")
   private final DiscordPresenceConfig config;
   @SerializedName("В")
   private final Minecraft minecraft;
   private final @com.google.gson.annotations.SerializedName("Я")  Stopwatch updateTimer = new Stopwatch();
   private final @com.google.gson.annotations.SerializedName("Х")  Stopwatch reconnectTimer = new Stopwatch();
   private @com.google.gson.annotations.SerializedName("М")  boolean running;
   private @com.google.gson.annotations.SerializedName("Н")  boolean activityPublished;
   private @com.google.gson.annotations.SerializedName("Ц")  long sessionStartSeconds;
   private static final long MILLIS_PER_SECOND = 1000L;

   private void publishActivity() {
      if (this.running && this.config.isEnabled() && DiscordIpcClient.isConnected()) {
         DiscordIpcClient.setActivity(new ConfiguredDiscordActivity(this.config, this.minecraft, this.sessionStartSeconds));
         this.activityPublished = true;
         this.updateTimer.ВД();
      }
   }

   public DiscordPresenceManager(DiscordPresenceConfig config, Minecraft minecraft) {
      this.config = config;
      this.minecraft = minecraft;
   }

   public void tick() {
      if (this.running && this.config.isEnabled()) {
         if (!DiscordIpcClient.isConnected()) {
            this.activityPublished = false;
            if (this.reconnectTimer.ф(this.config.getReconnectIntervalMillis(), true)) {
               this.connectIfNeeded();
            }
         } else {
            if (!this.activityPublished || this.updateTimer.ф(this.config.getUpdateIntervalMillis(), true)) {
               this.publishActivity();
            }
         }
      }
   }

   private void connectIfNeeded() {
      if (this.running && this.config.isEnabled() && !DiscordIpcClient.isConnected()) {
         DiscordIpcClient.setErrorHandler((var1, var2) -> this.activityPublished = false);
         this.activityPublished = DiscordIpcClient.connect(this.config.getClientId(), this::publishActivity);
         if (this.activityPublished) {
            this.publishActivity();
         }
      }
   }

   public void start() {
      this.running = true;
      this.activityPublished = false;
      this.sessionStartSeconds = System.currentTimeMillis() / MILLIS_PER_SECOND;
      this.updateTimer.ВД();
      this.reconnectTimer.ВД();
      this.connectIfNeeded();
   }

   public void stop() {
      this.running = false;
      this.activityPublished = false;
      DiscordIpcClient.setErrorHandler((BiConsumer<Integer, String>)null);
      if (DiscordIpcClient.isConnected()) {
         DiscordIpcClient.disconnect();
      }
   }
}
