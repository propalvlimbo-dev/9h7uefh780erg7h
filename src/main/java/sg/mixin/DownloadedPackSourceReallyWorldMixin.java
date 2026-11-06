package sg.mixin;

import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import net.minecraft.client.User;
import net.minecraft.client.resources.server.DownloadedPackSource;
import net.minecraft.client.resources.server.PackDownloader;
import net.minecraft.client.resources.server.PackLoadFeedback;
import net.minecraft.client.resources.server.PackLoadFeedback.FinalResult;
import net.minecraft.client.resources.server.PackLoadFeedback.Update;
import net.minecraft.client.resources.server.ServerPackManager.PackPromptStatus;
import net.minecraft.network.Connection;
import net.minecraft.server.packs.DownloadQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.misc.RWFixer;

@Mixin(DownloadedPackSource.class)
public abstract class DownloadedPackSourceReallyWorldMixin {
   @Inject(method = "createDownloader", at = @At("RETURN"), cancellable = true)
   private void rose$prepareReallyWorldPacks(DownloadQueue var1, Executor var2, User var3, Proxy var4, CallbackInfoReturnable<PackDownloader> var5) {
      PackDownloader var6 = (PackDownloader)var5.getReturnValue();
      var5.setReturnValue((var2x, var3x) -> {
         Map var4x = Map.copyOf(var2x);
         RWFixer var5x = RWFixer.З_d084f225();
         Connection var6x = var5x != null ? var5x.б() : null;
         var6.download(var4x, var5xx -> {
            if (var5x == null) {
               var3x.accept(var5xx);
            } else {
               var5x.б(var4x, var6x, var5xx, var3x, var2);
            }
         });
      });
   }

   @Inject(method = "createPackResponseSender", at = @At("RETURN"), cancellable = true)
   private static void rose$spoofReallyWorldStatus(final Connection var0, CallbackInfoReturnable<PackLoadFeedback> var1) {
      final PackLoadFeedback var2 = (PackLoadFeedback)var1.getReturnValue();
      var1.setReturnValue(new PackLoadFeedback() {
         @Override
         public void reportUpdate(UUID var1, Update var2x) {
            RWFixer var3 = RWFixer.З_d084f225();
            if (var3 == null || !var3.б(var0, var1)) {
               var2.reportUpdate(var1, var2x);
            }
         }

         @Override
         public void reportFinalResult(UUID var1, FinalResult var2x) {
            RWFixer var3 = RWFixer.З_d084f225();
            if (var3 != null) {
               var3.б(var0, var1, var2x);
            }

            if (var3 == null || !var3.б(var0, var1)) {
               var2.reportFinalResult(var1, var2x);
            }
         }
      });
   }

   @Inject(method = "cleanupAfterDisconnect", at = @At("RETURN"))
   private void rose$clearReallyWorldPacks(CallbackInfo var1) {
      RWFixer var2 = RWFixer.З_d084f225();
      if (var2 != null) {
         var2.АП();
      }
   }

   @Inject(method = "configureForServerControl", at = @At("HEAD"))
   private void rose$resetReallyWorldConnection(Connection var1, PackPromptStatus var2, CallbackInfo var3) {
      RWFixer var4 = RWFixer.З_d084f225();
      if (var4 != null && var4.б() != var1) {
         var4.АП();
      }
   }
}
