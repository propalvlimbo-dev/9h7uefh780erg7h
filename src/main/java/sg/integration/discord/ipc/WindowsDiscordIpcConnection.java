package sg.integration.discord.ipc;

import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import sg.integration.discord.ipc.DiscordIpcOpcode;
import sg.integration.discord.ipc.DiscordIpcFrame;
import sg.integration.discord.ipc.DiscordIpcConnection;

public class WindowsDiscordIpcConnection extends DiscordIpcConnection {
   @SerializedName("В")
   private final RandomAccessFile pipe;
   private final @com.google.gson.annotations.SerializedName("Ч")  Consumer<DiscordIpcFrame> frameConsumer;

   private void readFully(ByteBuffer buffer) throws IOException {
      buffer.rewind();
      byte[] chunk = new byte[buffer.remaining()];
      int offset = 0;

      // Named pipe: RandomAccessFile.length() всегда 0, поэтому ждать «длину» нельзя —
      // читаем блокирующим потоком, пока не наберём кадр целиком.
      while (offset < chunk.length) {
         int read = this.pipe.read(chunk, offset, chunk.length - offset);
         if (read < 0) {
            throw new IOException("Discord IPC pipe closed");
         }

         offset += read;
      }

      buffer.put(chunk);
      buffer.rewind();
   }

   public WindowsDiscordIpcConnection(String pipePath, Consumer<DiscordIpcFrame> frameConsumer) throws IOException {
      this.pipe = new RandomAccessFile(pipePath, "rw");
      this.frameConsumer = frameConsumer;
      Thread var3 = new Thread(this::readLoop);
      var3.setName("Discord IPC - Read thread");
      var3.start();
   }

   @Override
   public void close() {
      try {
         this.pipe.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   private void readLoop() {
      ByteBuffer var1 = ByteBuffer.allocate(4);

      try {
         while (true) {
            this.readFully(var1);
            DiscordIpcOpcode var2 = DiscordIpcOpcode.fromOrdinal(Integer.reverseBytes(var1.getInt(0)));
            this.readFully(var1);
            int var3 = Integer.reverseBytes(var1.getInt(0));
            ByteBuffer var4 = ByteBuffer.allocate(var3);
            this.readFully(var4);
            String var5 = Charset.forName("UTF-8").decode(var4.rewind()).toString();
            this.frameConsumer.accept(new DiscordIpcFrame(var2, JsonParser.parseString(var5).getAsJsonObject()));
         }
      } catch (Exception var6) {
         // Пайп закрылся или Discord перезапустился: сообщаем клиенту, чтобы он переподключился.
         DiscordIpcClient.handleConnectionFailure();
      }
   }

   @Override
   protected void write(ByteBuffer buffer) {
      try {
         this.pipe.write(buffer.array());
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }
}
