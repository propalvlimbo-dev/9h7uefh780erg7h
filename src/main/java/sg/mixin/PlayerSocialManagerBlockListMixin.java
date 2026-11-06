package sg.mixin;

import sg.mixinbridge.CachedPlayerBlockList;

import com.mojang.authlib.minecraft.UserApiService;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSocialManager.class)
public abstract class PlayerSocialManagerBlockListMixin {
   @Shadow
   @Final
   private UserApiService service;
   @Shadow
   private boolean onlineMode;
   @Shadow
   private CompletableFuture<?> pendingBlockListRefresh;
   @Unique
   private CachedPlayerBlockList rose$blockList;

   @Inject(method = "startOnlineMode", at = @At("RETURN"))
   private void rose$captureBlockList(CallbackInfo var1) {
      if (CachedPlayerBlockList.method_1(this.service)) {
         if (this.rose$blockList == null) {
            this.rose$blockList = new CachedPlayerBlockList(this.service);
         }

         this.pendingBlockListRefresh = this.rose$blockList.method_1(this.pendingBlockListRefresh);
      }
   }

   @Inject(method = "isBlocked", at = @At("HEAD"), cancellable = true)
   private void rose$checkCachedBlockList(UUID var1, CallbackInfoReturnable<Boolean> var2) {
      if (!this.onlineMode) {
         var2.setReturnValue(false);
      } else if (CachedPlayerBlockList.method_1(this.service)) {
         if (this.rose$blockList == null) {
            this.rose$blockList = new CachedPlayerBlockList(this.service);
            this.pendingBlockListRefresh = this.rose$blockList.method_1(this.pendingBlockListRefresh);
         }

         this.pendingBlockListRefresh = this.rose$blockList.ц(this.pendingBlockListRefresh);
         var2.setReturnValue(this.rose$blockList.method_1(var1));
      }
   }
}
