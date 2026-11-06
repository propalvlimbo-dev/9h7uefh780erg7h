package sg.network.irc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.CorruptedFrameException;
import java.nio.charset.StandardCharsets;
import sg.network.irc.custom.IrcAudience;
import sg.network.irc.custom.IrcCustomPacket;
import sg.network.irc.packet.IrcPacket;
import sg.network.irc.RoseIrc;

public final class IrcPacketCodec {
   public static ByteBuf Ж(Object var0, ByteBufAllocator var1) {
      ByteBuf var2 = var1.buffer();

      try {
         if (var0 instanceof IrcPacket var4) {
            Ж(var2, var4.ъ_91e8a6f5().ъЩ());
            Ж(var2, var4.ъ_91e8a6f5().Э().МН());
            Ж(var2, var4.ъ_91e8a6f5().ъ2());
            ByteBuf var5 = var4.ъ_3279c64b();
            var2.writeInt(var5.readableBytes());
            var2.writeBytes(var5);
            var5.clear();
         } else {
            if (!(var0 instanceof IrcCustomPacket var3)) {
               throw new IllegalArgumentException("Unsupported IRC packet");
            }

            Ж(var2, var3.method_0().method_1В());
            Ж(var2, var3.method_0().э().М6());
            Ж(var2, var3.method_0().method_1п());
         }

         return var2;
      } catch (Throwable var6) {
         var2.release();
         throw var6;
      }
   }

   public static Object Ж_7fd512f1(ByteBuf var0) {
      String var1 = Ж_388008bb(var0);
      String var2 = Ж_388008bb(var0);
      String var3 = Ж_388008bb(var0);
      RoseIrc var4 = RoseIrc.З_3e6e5516();
      if (var4 == null) {
         return null;
      }

      if (var1.startsWith("custom_")) {
         IrcAudience var5 = IrcAudience.М(var2);
         if (var5 == null) {
            return null;
         }

         for (IrcCustomPacket var7 : var4.З_677a0540().М_4c863fd0()) {
            if (var7.method_0().method_1В().equals(var1) && var7.method_0().э() == var5) {
               var7.method_0().О(var3);
               return var7;
            }
         }
      } else {
         int var8 = Ж_a83dd0cc(var0);

         for (IrcPacket var10 : var4.З_677a0540().М_62946491()) {
            if (var10.ъ_91e8a6f5().ъЩ().equals(var1) && var10.ъ_91e8a6f5().Э().МН().equals(var2)) {
               var10.ъ_91e8a6f5().и(var3);
               var10.ъ_3279c64b().clear().writeBytes(var0, var8);
               return var10;
            }
         }
      }

      return null;
   }

   private static int Ж_a83dd0cc(ByteBuf var0) {
      if (var0.readableBytes() < 4) {
         throw new CorruptedFrameException("Missing IRC field length");
      } else {
         int var1 = var0.readInt();
         if (var1 >= 0 && var1 <= var0.readableBytes() && var1 <= 1048576) {
            return var1;
         } else {
            throw new CorruptedFrameException("Invalid IRC field length");
         }
      }
   }

   private static String Ж_388008bb(ByteBuf var0) {
      return var0.readCharSequence(Ж_a83dd0cc(var0), StandardCharsets.UTF_8).toString();
   }

   private static void Ж(ByteBuf var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
      var0.writeInt(var2.length);
      var0.writeBytes(var2);
   }

   private IrcPacketCodec() {
   }
}
