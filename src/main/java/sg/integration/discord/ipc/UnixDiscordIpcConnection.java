package sg.integration.discord.ipc;

import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import sg.integration.discord.ipc.DiscordIpcOpcode;
import sg.integration.discord.ipc.DiscordIpcFrame;
import sg.integration.discord.ipc.DiscordIpcConnection;

public class UnixDiscordIpcConnection extends DiscordIpcConnection {
   @SerializedName("В")
   private final Selector selector = Selector.open();
   @SerializedName("В")
   private final SocketChannel channel;
   private final @com.google.gson.annotations.SerializedName("т")  Consumer<DiscordIpcFrame> frameConsumer;

   public UnixDiscordIpcConnection(String socketPath, Consumer<DiscordIpcFrame> frameConsumer) throws IOException {
      this.channel = SocketChannel.open(UnixDomainSocketAddress.of(socketPath));
      this.frameConsumer = frameConsumer;
      this.channel.configureBlocking(false);
      this.channel.register(this.selector, 1);
      Thread var3 = new Thread(this::readLoop);
      var3.setName("Discord IPC - Read thread");
      var3.start();
   }

   private void readLoop() {
      UnixDiscordIpcConnection.ReadState var1 = UnixDiscordIpcConnection.ReadState.Opcode;
      ByteBuffer var2 = ByteBuffer.allocate(4);
      ByteBuffer var3 = null;
      DiscordIpcOpcode var4 = null;

      try {
         while (true) {
            this.selector.select();
            switch (var1) {
               case Opcode:
                  this.channel.read(var2);
                  if (!var2.hasRemaining()) {
                     var4 = DiscordIpcOpcode.fromOrdinal(Integer.reverseBytes(var2.getInt(0)));
                     var1 = UnixDiscordIpcConnection.ReadState.Length;
                     var2.rewind();
                  }
                  break;
               case Length:
                  this.channel.read(var2);
                  if (!var2.hasRemaining()) {
                     var3 = ByteBuffer.allocate(Integer.reverseBytes(var2.getInt(0)));
                     var1 = UnixDiscordIpcConnection.ReadState.Data;
                     var2.rewind();
                  }
                  break;
               case Data:
                  this.channel.read(var3);
                  if (!var3.hasRemaining()) {
                     String var5 = Charset.defaultCharset().decode(var3.rewind()).toString();
                     this.frameConsumer.accept(new DiscordIpcFrame(var4, JsonParser.parseString(var5).getAsJsonObject()));
                     var3 = null;
                     var1 = UnixDiscordIpcConnection.ReadState.Opcode;
                  }
            }
         }
      } catch (Exception var6) {
      }
   }

   @Override
   public void close() {
      try {
         this.selector.close();
         this.channel.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   @Override
   protected void write(ByteBuffer buffer) {
      try {
         this.channel.write(buffer);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   private enum ReadState {
      Opcode,
      Length,
      Data;
   }
}
