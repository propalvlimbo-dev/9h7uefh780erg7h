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

      while (this.pipe.length() < buffer.remaining()) {
         Thread.onSpinWait();

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

      while (buffer.hasRemaining()) {
         this.pipe.getChannel().read(buffer);
      }
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
            String var5 = Charset.defaultCharset().decode(var4.rewind()).toString();
            this.frameConsumer.accept(new DiscordIpcFrame(var2, JsonParser.parseString(var5).getAsJsonObject()));
         }
      } catch (Exception var6) {
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
