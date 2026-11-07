package sg.integration.discord.ipc;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Consumer;
import sg.integration.discord.ipc.UnixDiscordIpcConnection;
import sg.integration.discord.ipc.WindowsDiscordIpcConnection;

public abstract class DiscordIpcConnection {
   private static final String[] RUNTIME_DIRECTORY_ENV_KEYS = new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};

   public static DiscordIpcConnection open(Consumer<DiscordIpcFrame> frameConsumer) {
      String var1 = System.getProperty("os.name").toLowerCase();
      if (var1.contains("win")) {
         for (int var10 = 0; var10 < 10; var10++) {
            try {
               return new WindowsDiscordIpcConnection("\\\\.\\pipe\\discord-ipc-" + var10, frameConsumer);
            } catch (IOException var7) {
            }
         }
      } else {
         String var2 = null;

         for (String var6 : RUNTIME_DIRECTORY_ENV_KEYS) {
            var2 = System.getenv(var6);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            var2 = "/tmp";
         }

         var2 = var2 + "/discord-ipc-";

         for (int var11 = 0; var11 < 10; var11++) {
            try {
               return new UnixDiscordIpcConnection(var2 + var11, frameConsumer);
            } catch (IOException var8) {
            }
         }
      }

      return null;
   }

   public void send(DiscordIpcOpcode opcode, JsonObject payload) {
      payload.addProperty("nonce", UUID.randomUUID().toString());
      byte[] var3 = payload.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
      ByteBuffer var4 = ByteBuffer.allocate(var3.length + 8);
      var4.putInt(Integer.reverseBytes(opcode.ordinal()));
      var4.putInt(Integer.reverseBytes(var3.length));
      var4.put(var3);
      var4.rewind();
      this.write(var4);
   }

   public abstract void close();

   protected abstract void write(ByteBuffer buffer);
}
