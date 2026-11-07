package sg.mixin;

import sg.mixinbridge.HeldItemRenderOverrideEvent;
import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.UnhookState;
import sg.modules.impl.init.AutoInventoryScreen;
import sg.client.RoseClient;
import sg.modules.impl.init.PotionSwapScreen;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.RenderSuppressionQuery;
import sg.hud.Scoreboard;
import sg.gui.click.ClickGuiScreen;
import sg.modules.impl.render.SwingAnimations;
import sg.network.ScoreboardValueEditor;
import sg.network.NameProtectionTextRewriter;

@Mixin(Hud.class)
public class HudCustomizationMixin {
   @Shadow
   @Final
   private static Identifier POWDER_SNOW_OUTLINE_LOCATION;

   @Inject(method = "extractItemHotbar", at = @At("HEAD"), cancellable = true)
   private void hideVanillaHotbar(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (!UnhookState.ые()) {
         HudRenderEvent.HotbarRender var4 = new HudRenderEvent.HotbarRender(
            (float)Minecraft.getInstance().mouseHandler.xpos(), (float)Minecraft.getInstance().mouseHandler.ypos(), var1
         );
         RoseClient.getInstance().getEventBus().post(var4);
         if (var4.isCancelled()) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void hidePowderSnowOverlay(GuiGraphicsExtractor var1, Identifier var2, float var3, CallbackInfo var4) {
      if (var2 == POWDER_SNOW_OUTLINE_LOCATION && RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.POWDER_SNOW)) {
         var4.cancel();
      }
   }

   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void hideCrosshairInClickGui(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (!UnhookState.ые()) {
         if (SwingAnimations.Ъ(Minecraft.getInstance().gui.screen())) {
            var3.cancel();
         } else {
            HudRenderEvent.CrosshairRender var4 = new HudRenderEvent.CrosshairRender(
               (float)Minecraft.getInstance().mouseHandler.xpos(), (float)Minecraft.getInstance().mouseHandler.ypos(), var1
            );
            RoseClient.getInstance().getEventBus().post(var4);
            if (var4.isCancelled()) {
               var3.cancel();
            }

            if (Minecraft.getInstance().gui.screen() instanceof ClickGuiScreen
               || Minecraft.getInstance().gui.screen() instanceof com.deluxe.gui.DeluxeClickGUI
               || Minecraft.getInstance().gui.screen() instanceof PotionSwapScreen) {
               var3.cancel();
            }

            if (Minecraft.getInstance().gui.screen() instanceof AutoInventoryScreen) {
               var3.cancel();
            }
         }
      }
   }

   @Inject(method = "extractCameraOverlays", at = @At("HEAD"))
   private void render(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (!UnhookState.ые()) {
         HudRenderEvent var4 = new HudRenderEvent(
            (float)Minecraft.getInstance().mouseHandler.xpos(), (float)Minecraft.getInstance().mouseHandler.ypos(), var1
         );
         RoseClient.getInstance().getEventBus().post(var4);
      }
   }

   @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
   private void renderVignetteOverlay(GuiGraphicsExtractor var1, Entity var2, CallbackInfo var3) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.VIGNETTE)) {
         var3.cancel();
      }
   }

   @Inject(method = "extractTitle", at = @At("HEAD"), cancellable = true)
   private void renderTitleAndSubtitle(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.TITLE)) {
         var3.cancel();
      }
   }

   @Inject(
      method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderScoreboardSidebar(GuiGraphicsExtractor var1, Objective var2, CallbackInfo var3) {
      NameProtectionTextRewriter.жЭ();
      ScoreboardValueEditor.зу();
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.SCOREBOARD)) {
         Scoreboard var5 = Scoreboard.必_791f1767();
         if (var5 != null) {
            var5.ШР();
         }

         ScoreboardValueEditor.з1();
         NameProtectionTextRewriter.жО();
         var3.cancel();
      } else {
         Scoreboard var4 = Scoreboard.必_791f1767();
         if (var4 != null) {
            var4.у(var1);
         }
      }
   }

   @Inject(method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V", at = @At("RETURN"))
   private void endScoreboardSidebar(GuiGraphicsExtractor var1, Objective var2, CallbackInfo var3) {
      Scoreboard var4 = Scoreboard.必_791f1767();
      if (var4 != null) {
         var4.必(var1);
      }

      ScoreboardValueEditor.з1();
      NameProtectionTextRewriter.жО();
   }

   @WrapOperation(
      method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
   )
   private void captureScoreboardFill(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, Operation<Void> var7) {
      Scoreboard var8 = Scoreboard.必_791f1767();
      if (var8 != null) {
         var8.у(var2, var3, var4, var5);
      }

      var7.call(var1, var2, var3, var4, var5, sg.hud.HudBackground.COLOR);
   }

   @WrapOperation(
      method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"
      )
   )
   private void decorateScoreboardText(GuiGraphicsExtractor var1, Font var2, Component var3, int var4, int var5, int var6, boolean var7, Operation<Void> var8) {
      Component var9 = NameProtectionTextRewriter.И(var3);
      ScoreboardValueEditor.method_9(var1, var2, var9, var4, var5);
      Component var10 = ScoreboardValueEditor.method_9_f022d65c(var9);
      NameProtectionTextRewriter.жц();

      try {
         var8.call(var1, var2, var10, var4, var5, var6, var7);
      } finally {
         NameProtectionTextRewriter.жР();
      }
   }

   @ModifyExpressionValue(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int replaceRenderedHotbarSlot(int var1) {
      if (UnhookState.ые()) {
         return var1;
      }

      HeldItemRenderOverrideEvent var2 = HeldItemRenderOverrideEvent.INSTANCE.resetForSelectedSlot(var1);
      RoseClient.getInstance().getEventBus().post(var2);
      return var2.getSelectedSlot();
   }
}
