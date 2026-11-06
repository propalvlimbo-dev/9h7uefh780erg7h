package sg.modules.impl.misc;

import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.mixin.BossHealthOverlayAccessor;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public final class PvpSafe extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Ч")  EventListener<PlayerTickEvent> playerTickListener = this::Ь;
   private final @com.google.gson.annotations.SerializedName("Л")  EventListener<PacketSendEvent> packetSendListener = this::И;
   private @com.google.gson.annotations.SerializedName("о")  long lastPvpBossBarMillis;
   private static final String сз西 = "Pvp Safe";
   private static final String сзд = "Во время ПВП блокирует выход с сервера и команды выхода (/hub /limbo /an /anarchy)";
   private static final String сзБ = "/";
   private static final String сзС = "\\s+";
   private static final String сзз = "hub";
   private static final String сзц = "limbo";
   private static final String сзя = "anarchy";
   private static final String сцс = "an";
   private static final String сцВ = ".*\\b(hub|limbo|anarchy)\\b.*";
   private static final long сцЯ = 500L;
   private static final String сцХ = "pvp";
   private static final String сце = "пвп";

   private void Ь(PlayerTickEvent var1) {
      this.ЛЬ();
   }

   private boolean к() {
      this.ЛЬ();
      return System.currentTimeMillis() - this.lastPvpBossBarMillis < сцЯ;
   }

   public PvpSafe() {
      super(сз西, сзд, ModuleCategory.MISC);
   }

   public static boolean Д(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String var1 = var0.toLowerCase().trim();
         if (var1.startsWith(сзБ)) {
            var1 = var1.substring(1).trim();
         }

         String var2 = var1.split(сзС, 2)[0];
         return !var2.equals(сзз) && !var2.equals(сзц) && !var2.equals(сзя) && !var2.startsWith(сцс) ? var1.matches(сцВ) : true;
      } else {
         return false;
      }
   }

   private void И(PacketSendEvent var1) {
      if (this.к()) {
         String var2 = null;
         if (var1.getPacket() instanceof ServerboundChatPacket var3) {
            var2 = var3.message();
         } else if (var1.getPacket() instanceof ServerboundChatCommandSignedPacket var4) {
            var2 = var4.command();
         } else if (var1.getPacket() instanceof ServerboundChatCommandPacket var5) {
            var2 = var5.command();
         }

         if (var2 != null && Д(var2)) {
            var1.setCancelled(true);
         }
      }
   }

   public static boolean method_7() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         PvpSafe var0 = RoseClient.getInstance().getServices().getModules().getPvpSafe();
         return var0 != null && var0.isEnabled() && var0.к();
      } else {
         return false;
      }
   }

   private void ЛЬ() {
      if (MINECRAFT_CLIENT.gui.hud != null) {
         for (LerpingBossEvent var2 : ((BossHealthOverlayAccessor)MINECRAFT_CLIENT.gui.hud.getBossOverlay()).getBossBars().values()) {
            String var3 = var2.getName().getString().toLowerCase();
            if (var3.contains(сцХ) || var3.contains(сце)) {
               this.lastPvpBossBarMillis = System.currentTimeMillis();
               return;
            }
         }
      }
   }
}
