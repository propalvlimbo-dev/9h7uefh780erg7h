package sg.mixin;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.server.DownloadedPackSource;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.RWFixer;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerReallyWorldMixin {
   @Shadow
   @Final
   protected Minecraft minecraft;
   @Shadow
   @Final
   protected Connection connection;
   @Shadow
   @Final
   protected ServerData serverData;
   private static final String сНъ = "https";
   private static final String сНМ = "http";

   @Inject(
      method = "handleResourcePackPush",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      ),
      cancellable = true
   )
   private void rose$loadReallyWorldPack(ClientboundResourcePackPushPacket var1, CallbackInfo var2) {
      RWFixer var3 = RWFixer.З_d084f225();
      if (var3 != null && this.serverData != null && RWFixer.Ю(this.serverData.ip)) {
         try {
            URI var4 = URI.create(var1.url());
            if (!сНъ.equalsIgnoreCase(var4.getScheme()) && !сНМ.equalsIgnoreCase(var4.getScheme()) || var4.getHost() == null) {
               return;
            }

            URL var5 = var4.toURL();
            DownloadedPackSource var6 = this.minecraft.getDownloadedPackSource();
            var3.б(this.connection, this.serverData.ip, var1.id(), var5.toExternalForm());
            var6.allowServerPacks();
            var6.pushPack(var1.id(), var5, var1.hash());
            var2.cancel();
         } catch (MalformedURLException | IllegalArgumentException var7) {
         }
      }
   }
}
