package sg.network;

import java.awt.Color;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import sg.ec.UnhookState;
import sg.ec.MinecraftAccess;
import sg.mixin.ChatComponentAccessor;
import sg.client.RoseClient;
import sg.modules.impl.init.FriendAliasCommands;
import sg.util.time.ColorMath;

public final class ClientChat implements MinecraftAccess {
   private static final Set<Component> э = Collections.newSetFromMap(new IdentityHashMap<>());
   private static final String вг = "Rose";
   private static final String в0 = " > ";
   private static final String вх = "/";
   private static final String вУ = "This is a utility class and cannot be instantiated";

   public static void К(String var0) {
      if (!MINECRAFT_CLIENT.isSameThread()) {
         MINECRAFT_CLIENT.execute(() -> К(var0));
      } else if (MINECRAFT_CLIENT.player != null && !UnhookState.ые()) {
         Х_f022d65c(Component.nullToEmpty(var0));
      }
   }

   public static void т(String var0) {
      if (MINECRAFT_CLIENT.player != null) {
         if (!RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
            var0 = FriendAliasCommands.х(var0);
            if (var0.startsWith(RoseClient.getInstance().getServices().getCommands().getCommandPrefix().toString())) {
               RoseClient.getInstance().getServices().getCommands().executeChatCommand(var0);
            } else if (var0.startsWith(вх)) {
               MINECRAFT_CLIENT.player.connection.sendCommand(var0.substring(1));
            } else {
               MINECRAFT_CLIENT.player.connection.sendChat(var0);
            }
         }
      }
   }

   public static void method_2(Object var0, long var1) {
      if (!MINECRAFT_CLIENT.isSameThread()) {
         MINECRAFT_CLIENT.execute(() -> method_2(var0, var1));
      } else if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.hud != null) {
         Component var3 = Х_f022d65c(method_2(var0));
         if (var3 != null) {
            CompletableFuture.delayedExecutor(Math.max(0L, var1), TimeUnit.MILLISECONDS).execute(() -> MINECRAFT_CLIENT.execute(() -> {
               if (MINECRAFT_CLIENT.gui.hud != null && MINECRAFT_CLIENT.gui.hud.getChat() instanceof ChatComponentAccessor var1x) {
                  if (var1x.getMessages().removeIf(var1xx -> var1xx.content() == var3)) {
                     var1x.invokeRefreshTrimmedMessages();
                  }

                  э.remove(var3);
               }
            }));
         }
      }
   }

   public static void вТ() {
      if (!MINECRAFT_CLIENT.isSameThread()) {
         MINECRAFT_CLIENT.execute(ClientChat::вТ);
      } else {
         if (MINECRAFT_CLIENT.gui.hud != null && MINECRAFT_CLIENT.gui.hud.getChat() instanceof ChatComponentAccessor var0 && var0.getMessages().removeIf(var0x -> э.contains(var0x.content()))) {
            var0.invokeRefreshTrimmedMessages();
         }

         э.clear();
      }
   }

   public static void З(Object var0) {
      if (!MINECRAFT_CLIENT.isSameThread()) {
         MINECRAFT_CLIENT.execute(() -> З(var0));
      } else if (MINECRAFT_CLIENT.player != null && !UnhookState.ые()) {
         Х_f022d65c(method_2(var0));
      }
   }

   @Generated
   private ClientChat() {
      throw new UnsupportedOperationException(вУ);
   }

   private static Component Х_f022d65c(Component var0) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.hud != null) {
         ChatComponentAccessor var1 = (ChatComponentAccessor)MINECRAFT_CLIENT.gui.hud.getChat();
         GuiMessage var2 = var1.getMessages().isEmpty() ? null : var1.getMessages().getFirst();
         MINECRAFT_CLIENT.gui.hud.getChat().addClientSystemMessage(var0);
         э.removeIf(var1x -> var1.getMessages().stream().noneMatch(var1xx -> var1xx.content() == var1x));
         if (!var1.getMessages().isEmpty() && var1.getMessages().getFirst() != var2) {
            Component var3 = var1.getMessages().getFirst().content();
            э.add(var3);
            return var3;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static MutableComponent method_2(Object var0) {
      MutableComponent var1 = Component.literal("");
      var1.append(ColorMath.colorizeWithTheme(вг)).setStyle(Style.EMPTY.withBold(true));
      var1.append(Component.literal(в0).setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(new Color(200, 200, 200).getRGB()))));
      if (var0 instanceof Component var2) {
         var1.append(var2).setStyle(Style.EMPTY.withBold(false));
      } else {
         var1.append(
            Component.literal(String.valueOf(var0)).setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(new Color(200, 200, 200).getRGB())))
         );
      }

      return var1;
   }
}
