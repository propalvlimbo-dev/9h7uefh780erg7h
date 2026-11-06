package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import org.joml.Vector2f;
import settings.render.VoicechatAudioPlayback;
import sg.ec.ClientService;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.render.pipeline.InventoryActions;

public class ClientTickState extends ClientService {
   @SerializedName("4")
   private final Queue<String> pendingChatMessages = new ConcurrentLinkedQueue<>();
   private @com.google.gson.annotations.SerializedName("Д0")  int tickCount = 0;
   private final @com.google.gson.annotations.SerializedName("ш")  Vector2f pitchHistory = new Vector2f();
   private final @com.google.gson.annotations.SerializedName("ХГ")  EventListener<PlayerTickEvent> playerTickListener = this::onPlayerTick;
   private final @com.google.gson.annotations.SerializedName("Хл")  EventListener<PacketReceiveEvent> packetReceiveListener = this::onPacketReceive;

   public void onPlayerTick(PlayerTickEvent event) {
      VoicechatAudioPlayback.жь();
      this.pitchHistory.y = this.pitchHistory.x;
      this.pitchHistory.x = MINECRAFT_CLIENT.player.getXRot();
      this.tickCount++;

      String message;
      while ((message = this.pendingChatMessages.poll()) != null) {
         ClientChat.З(message);
      }
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacket() instanceof ServerboundSetCarriedItemPacket carriedItemPacket) {
         InventoryActions.Б(carriedItemPacket.getSlot());
      }
   }

   @Generated
   public int getTickCount() {
      return this.tickCount;
   }

   @Generated
   public Vector2f getPitchHistory() {
      return this.pitchHistory;
   }

   @Generated
   public Queue<String> getPendingChatMessages() {
      return this.pendingChatMessages;
   }
}
