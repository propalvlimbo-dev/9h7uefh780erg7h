package sg.integration.discord.ipc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.lang.management.ManagementFactory;
import java.util.function.BiConsumer;
import sg.integration.discord.presence.DiscordActivity;

public class DiscordIpcClient {
   private static final Gson GSON = new Gson();
   private static BiConsumer<Integer, String> errorHandler = DiscordIpcClient::logError;
   private static DiscordIpcConnection connection;
   private static Runnable onReady;
   private static boolean handshakeComplete;
   private static JsonObject pendingActivity;
   private static DiscordUser currentUser;

   public static void setErrorHandler(BiConsumer<Integer, String> handler) {
      errorHandler = handler;
   }

   public static boolean isConnected() {
      return connection != null;
   }

   private static void logError(int code, String message) {
      System.err.println("Discord IPC error " + code + " with message: " + message);
   }

   private static int currentProcessId() {
      String var0 = ManagementFactory.getRuntimeMXBean().getName();
      return Integer.parseInt(var0.substring(0, var0.indexOf(64)));
   }

   private static void sendPendingActivity() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("pid", currentProcessId());
      var0.add("activity", pendingActivity);
      JsonObject var1 = new JsonObject();
      var1.addProperty("cmd", "SET_ACTIVITY");
      var1.add("args", var0);
      connection.send(DiscordIpcOpcode.Frame, var1);
      pendingActivity = null;
   }

   private static void handleFrame(DiscordIpcFrame frame) {
      if (frame.opcode() == DiscordIpcOpcode.Close) {
         if (errorHandler != null) {
            errorHandler.accept(frame.data().get("code").getAsInt(), frame.data().get("message").getAsString());
         }

         disconnect();
      } else if (frame.opcode() == DiscordIpcOpcode.Frame) {
         if (frame.data().has("evt") && frame.data().get("evt").getAsString().equals("ERROR")) {
            JsonObject var1 = frame.data().getAsJsonObject("data");
            if (errorHandler != null) {
               errorHandler.accept(var1.get("code").getAsInt(), var1.get("message").getAsString());
            }
         } else if (frame.data().has("cmd") && frame.data().get("cmd").getAsString().equals("DISPATCH")) {
            handshakeComplete = true;
            currentUser = GSON.fromJson(frame.data().getAsJsonObject("data").getAsJsonObject("user"), DiscordUser.class);
            if (onReady != null) {
               onReady.run();
            }

            if (pendingActivity != null) {
               sendPendingActivity();
            }
         }
      }
   }

   public static DiscordUser getCurrentUser() {
      return currentUser;
   }

   public static void setActivity(DiscordActivity activity) {
      if (connection != null) {
         pendingActivity = activity.toJson();
         if (handshakeComplete) {
            sendPendingActivity();
         }
      }
   }

   public static boolean connect(long applicationId, Runnable readyCallback) {
      connection = DiscordIpcConnection.open(DiscordIpcClient::handleFrame);
      if (connection == null) {
         return false;
      }

      onReady = readyCallback;
      JsonObject var3 = new JsonObject();
      var3.addProperty("v", 1);
      var3.addProperty("client_id", Long.toString(applicationId));
      connection.send(DiscordIpcOpcode.Handshake, var3);
      return true;
   }

   public static void disconnect() {
      if (connection != null) {
         connection.close();
         connection = null;
         onReady = null;
         handshakeComplete = false;
         pendingActivity = null;
         currentUser = null;
      }
   }
}
