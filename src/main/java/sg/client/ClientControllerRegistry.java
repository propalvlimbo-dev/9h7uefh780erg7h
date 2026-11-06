package sg.client;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import lombok.Generated;
import sg.ec.DeathCoordinatesNotifier;
import sg.ec.ServerTickRateEstimator;
import sg.ec.ServerSelectionTracker;
import sg.ec.ClientService;
import sg.ec.WorldPetCosmetics;
import sg.ec.CommandPingProbe;
import sg.ec.InvalidPacketGuard;
import sg.modules.impl.init.ClientTelemetry;
import sg.modules.impl.init.ClientTickState;
import sg.modules.impl.init.KeybindDispatcher;
import sg.modules.impl.init.NotificationManager;
import sg.rotation.RotationController;
import sg.modules.impl.init.ServerPlayerState;

public class ClientControllerRegistry extends ArrayList<ClientService> {
   @SerializedName("В")
   private final KeybindDispatcher keybinds = new KeybindDispatcher();
   @SerializedName("В")
   private final InvalidPacketGuard invalidPacketGuard = new InvalidPacketGuard();
   private final @com.google.gson.annotations.SerializedName("Я")  RotationController rotations = new RotationController();
   @SerializedName("В")
   private final ClientTickState clientTicks = new ClientTickState();
   @SerializedName("В")
   private final CommandPingProbe commandPing = new CommandPingProbe();
   @SerializedName("В")
   private final DeathCoordinatesNotifier deathCoordinates = new DeathCoordinatesNotifier();
   @SerializedName("В")
   private final ServerPlayerState serverPlayers = new ServerPlayerState();
   @SerializedName("В")
   private final NotificationManager notifications = new NotificationManager();
   @SerializedName("В")
   private final ServerTickRateEstimator serverTickRate = new ServerTickRateEstimator();

   @Generated
   public ServerPlayerState getServerPlayers() {
      return this.serverPlayers;
   }

   @Generated
   public KeybindDispatcher getKeybinds() {
      return this.keybinds;
   }

   @Generated
   public ClientTickState getClientTicks() {
      return this.clientTicks;
   }

   @Generated
   public NotificationManager getNotifications() {
      return this.notifications;
   }

   @Generated
   public RotationController getRotations() {
      return this.rotations;
   }

   @Generated
   public DeathCoordinatesNotifier getDeathCoordinates() {
      return this.deathCoordinates;
   }

   public ClientControllerRegistry() {
      this.add(this.keybinds);
      this.add(this.invalidPacketGuard);
      this.add(this.rotations);
      this.add(this.clientTicks);
      this.add(this.deathCoordinates);
      this.add(this.serverPlayers);
      this.add(this.notifications);
      this.add(this.commandPing);
      this.add(this.serverTickRate);
      this.add(new WorldPetCosmetics());
      this.add(ClientTelemetry.и_4109462f());
      this.add(new ServerSelectionTracker());
      this.forEach(service -> RoseClient.getInstance().getEventBus().register(service));
   }

   @Generated
   public CommandPingProbe getCommandPing() {
      return this.commandPing;
   }

   @Generated
   public InvalidPacketGuard getInvalidPacketGuard() {
      return this.invalidPacketGuard;
   }

   @Generated
   public ServerTickRateEstimator getServerTickRate() {
      return this.serverTickRate;
   }
}
