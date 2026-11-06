package sg.network.irc;

import com.google.gson.annotations.SerializedName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import sg.network.irc.custom.IrcAudience;
import sg.handler.PartyChatMessage;
import sg.network.irc.codec.IrcFrameDecoder;
import sg.handler.PartyPresenceMessage;
import sg.handler.PartyTrace;
import sg.network.irc.custom.IrcPacket;
import sg.network.irc.custom.IrcCustomPacket;

public final class IrcPartyInboundHandler extends ChannelInboundHandlerAdapter {
   @SerializedName("Я")
   private final Consumer<String> Я_c8e1e768;
   private final Consumer<String> Х;
   @SerializedName("Я")
   private final Runnable Я_3382daff;
   private static final String Яйз = ":";
   private static final String Яйц = "party.";
   private static final String Яйя = "NOTICE_RX";
   private static final String ЯЖс = "PARTY_UPDATE_RX";
   private static final String ЯЖВ = "IRC_CHANNEL_INACTIVE";

   @Override
   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      PartyTrace.Ы(ЯЖВ, "channel=" + var1.channel().id() + " serverState=unknown");
      this.Я_3382daff.run();
      super.channelInactive(var1);
   }

   public IrcPartyInboundHandler(Consumer<String> var1, Consumer<String> var2, Runnable var3) {
      this.Я_c8e1e768 = var1;
      this.Х = var2;
      this.Я_3382daff = var3;
   }

   @Override
   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      if (var2 instanceof IrcDebugMessagePacket var3) {
         String var5 = var3.ъ_91e8a6f5().ъ2();
         if (var5 != null) {
            String[] var6 = var5.split(Яйз, 3);
            if (var6.length >= 2) {
               String var7 = var6[1].trim();
               if (var7.startsWith(Яйц)) {
                  PartyTrace.Ы(Яйя, "raw=" + var5);
               }

               this.Я_c8e1e768.accept(var7);
            }
         }
      } else if (var2 instanceof IrcPartyUpdatePacket var4) {
         PartyTrace.Ы(ЯЖс, "names=" + var4.ъ_91e8a6f5().ъ2());
         this.Х.accept(var4.ъ_91e8a6f5().ъ2());
      }

      super.channelRead(var1, var2);
   }

   public static final class PresencePacket extends IrcCustomPacket {
      private final Consumer<PartyPresenceMessage> Л;
      private static final String сЙ西 = "rose_party_presence_v2";
      private static final String сЙд = "presence-dispatch";
      private static final long сЙБ = 2000L;
      private static final String сЙС = "PRESENCE_DISPATCH";
      private static final String сЙз = "decoded";
      private static final String сЙц = "PRESENCE_DECODED";

      @Override
      public void Жу() {
         if (PartyTrace.Р(сЙд, сЙБ)) {
            PartyTrace.Ы(сЙС, "packet=" + this.method_0().method_1В() + " route=" + this.method_0().э().М6() + " receiverInstalled=" + (this.Л != null));
         }

         PartyPresenceMessage var1 = PartyPresenceMessage.decode(this.method_0().method_1п());
         if (var1 != null && this.Л != null) {
            if (PartyTrace.Р(сЙз, var1)) {
               PartyTrace.Ы(сЙц, PartyTrace.Р(var1));
            }

            this.Л.accept(var1);
         }
      }

      public PresencePacket(String var1, Consumer<PartyPresenceMessage> var2) {
         super(new IrcPacket(сЙ西, IrcAudience.PARTY_ONLY, var1));
         this.Л = var2;
      }
   }

   public static final class TracingPacketDecoder extends IrcFrameDecoder {
      private static final String сЖХ = "custom_";
      private static final String сЖе = "party_update";
      private static final String сЖА = "debug_message";
      private static final long сЖ4 = 2000L;
      private static final String сЖп = "WIRE_RX";
      private static final String сЖД = "custom_rose_party_presence_v2";
      private static final String сЖЪ = "party_only";
      private static final String сЖт = "wire-error";
      private static final long сЖа = 5000L;
      private static final String сЖЮ = "WIRE_PROBE_ERROR";

      private static String field(ByteBuf var0, int var1, int var2) {
         if (var1 + 4 > var0.writerIndex()) {
            return null;
         }

         int var3 = var0.getInt(var1);
         return var3 >= 0 && var3 <= var2 && var1 + 4 + var3 <= var0.writerIndex() ? var0.toString(var1 + 4, var3, StandardCharsets.UTF_8) : null;
      }

      private static void traceFrame(ByteBuf var0) {
         try {
            int var1 = var0.readerIndex();
            String var2 = field(var0, var1, 160);
            if (var2 == null || !var2.startsWith(сЖХ) && !var2.equals(сЖе) && !var2.equals(сЖА)) {
               return;
            }

            var1 += 4 + var0.getInt(var1);
            String var3 = field(var0, var1, 80);
            if (var3 == null) {
               return;
            }

            var1 += 4 + var0.getInt(var1);
            if (var1 + 4 > var0.writerIndex()) {
               return;
            }

            if (PartyTrace.Р("wire:" + var2 + ":" + var3, сЖ4)) {
               PartyTrace.Ы(
                  сЖп,
                  "packet=" + var2 + " route=" + var3 + " payloadBytes=" + var0.getInt(var1) + " expectedPresence=" + (var2.equals(сЖД) && var3.equals(сЖЪ))
               );
            }
         } catch (RuntimeException var4) {
            if (PartyTrace.Р(сЖт, сЖа)) {
               PartyTrace.Ы(сЖЮ, var4.getClass().getSimpleName());
            }
         }
      }

      @Override
      public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
         if (PartyTrace.IИ() && var2 instanceof ByteBuf var3) {
            traceFrame(var3);
         }

         super.channelRead(var1, var2);
      }
   }

   public static final class PartyChatPacket extends IrcCustomPacket {
      private final Consumer<PartyChatMessage> а;
      private static final String рм = "rose_party_chat_v1";

      @Override
      public void Жу() {
         PartyChatMessage var1 = PartyChatMessage.decode(this.method_0().method_1п());
         if (var1 != null && this.а != null) {
            this.а.accept(var1);
         }
      }

      public PartyChatPacket(String var1, Consumer<PartyChatMessage> var2) {
         super(new IrcPacket(рм, IrcAudience.PARTY_ONLY, var1));
         this.а = var2;
      }
   }
}
