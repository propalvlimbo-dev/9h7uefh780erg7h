package sg.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.events.impl.UiAnimationEvents;
import sg.hud.HudDisplacementManager;
import sg.client.RoseClient;
import sg.network.NameProtectionTextRewriter;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayAnimationMixin {
   @Shadow
   private Component footer;
   @Shadow
   private Component header;
   @Unique
   private Component dimas$originalFooter;
   @Unique
   private Component dimas$originalHeader;
   @Unique
   private Component dimas$sourceFooter;
   @Unique
   private Component dimas$sourceHeader;
   @Unique
   private Component dimas$protectedFooter;
   @Unique
   private Component dimas$protectedHeader;
   @Unique
   private boolean dimas$animationMatrixPushed;
   @Unique
   private final WeakHashMap<PlayerInfo, Component> dimas$sourceNameCache = new WeakHashMap<>();
   @Unique
   private final WeakHashMap<PlayerInfo, Component> dimas$protectedNameCache = new WeakHashMap<>();
   @Unique
   private int dimas$protectionSignature;
   @Unique
   private boolean dimas$nameProtectEnabled;
   @Unique
   private UUID dimas$localPlayerUuid;
   private static final float ЯМп = 0.5F;
   private static final float ЯМД = 10.0F;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void dimas$beginTabCapture(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      this.dimas$originalFooter = this.footer;
      this.dimas$originalHeader = this.header;
      Minecraft var6 = Minecraft.getInstance();
      this.dimas$nameProtectEnabled = NameProtectionTextRewriter.Д2();
      int var7 = NameProtectionTextRewriter.Ьа();
      if (var7 != this.dimas$protectionSignature) {
         this.dimas$protectionSignature = var7;
         this.dimas$sourceNameCache.clear();
         this.dimas$protectedNameCache.clear();
         this.dimas$sourceFooter = null;
         this.dimas$sourceHeader = null;
      }

      this.dimas$localPlayerUuid = var6.player == null ? null : var6.player.getUUID();
      if (this.dimas$sourceFooter != this.footer) {
         this.dimas$sourceFooter = this.footer;
         this.dimas$protectedFooter = NameProtectionTextRewriter.Д(this.footer);
      }

      if (this.dimas$sourceHeader != this.header) {
         this.dimas$sourceHeader = this.header;
         this.dimas$protectedHeader = NameProtectionTextRewriter.Д(this.header);
      }

      this.footer = this.dimas$protectedFooter;
      this.header = this.dimas$protectedHeader;
      boolean var8 = var6.options != null && var6.options.keyPlayerList.isDown();
      UiAnimationEvents.PlayerListAnimationEvent var9 = dimas$playerListAnimation(var8);
      this.dimas$animationMatrixPushed = var9.isAnimationEnabled();
      if (this.dimas$animationMatrixPushed) {
         float var10 = var9.getScale();
         float var11 = var2 * ЯМп;
         float var12 = ЯМД;
         var1.pose().pushMatrix();
         var1.pose().translate(var11, var12);
         var1.pose().scale(var10, var10);
         var1.pose().translate(-var11, -var12);
      }

      HudDisplacementManager.beginPlayerListBoundsCapture();
      NameProtectionTextRewriter.жЛ();
      NameProtectionTextRewriter.ж3();
   }

   @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
   private void dimas$protectTabPlayerName(PlayerInfo var1, CallbackInfoReturnable<Component> var2) {
      Component var3 = (Component)var2.getReturnValue();
      if (var3 != null && this.dimas$nameProtectEnabled) {
         Component var4 = this.dimas$sourceNameCache.get(var1);
         Component var5 = this.dimas$protectedNameCache.get(var1);
         if (var5 == null || var4 != var3 && (var4 == null || !var4.equals(var3))) {
            boolean var6 = this.dimas$localPlayerUuid != null && var1.getProfile().id().equals(this.dimas$localPlayerUuid);
            Component var7 = var6 ? NameProtectionTextRewriter.必(var3) : NameProtectionTextRewriter.Ц(var3);
            this.dimas$sourceNameCache.put(var1, var3);
            this.dimas$protectedNameCache.put(var1, var7);
            var2.setReturnValue(var7);
         } else {
            var2.setReturnValue(var5);
         }
      }
   }

   @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
   private void dimas$captureTabFill(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, Operation<Void> var7) {
      HudDisplacementManager.capturePlayerListRectangle(var2, var3, var4, var5);
      var7.call(var1, var2, var3, var4, var5, var6);
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void dimas$endTabCapture(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      try {
         HudDisplacementManager.finishPlayerListBoundsCapture();
      } finally {
         if (this.dimas$animationMatrixPushed) {
            var1.pose().popMatrix();
            this.dimas$animationMatrixPushed = false;
         }

         this.footer = this.dimas$originalFooter;
         this.header = this.dimas$originalHeader;
         this.dimas$originalFooter = null;
         this.dimas$originalHeader = null;
         this.dimas$nameProtectEnabled = false;
         this.dimas$localPlayerUuid = null;
         NameProtectionTextRewriter.жд();
         NameProtectionTextRewriter.ж_();
      }
   }

   @Unique
   private static UiAnimationEvents.PlayerListAnimationEvent dimas$playerListAnimation(boolean var0) {
      UiAnimationEvents.PlayerListAnimationEvent var1 = new UiAnimationEvents.PlayerListAnimationEvent(var0);
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RoseClient.getInstance().getEventBus().post(var1);
      }

      return var1;
   }
}
