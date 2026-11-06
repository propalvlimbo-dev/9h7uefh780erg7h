package sg.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.TitleScreenRouter;
import sg.ec.GuiRenderFlush;
import sg.client.RoseClient;

@Mixin(Gui.class)
public class GuiScreenRoutingMixin {
   @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
   private Screen replaceTitleScreen(Screen var1) {
      Screen var2 = TitleScreenRouter.В(var1, Minecraft.getInstance().level != null);
      return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null
         ? RoseClient.getInstance().getServices().getModules().getEcExploit().й(var2)
         : var2;
   }

   @Inject(
      method = "extractRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"
      )
   )
   private void flushHudBeforeScreen(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      GuiRenderFlush.method_8Х();
   }
}
