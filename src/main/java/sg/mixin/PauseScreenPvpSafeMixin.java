package sg.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.PvpSafe;

@Mixin(PauseScreen.class)
public class PauseScreenPvpSafeMixin {
   @Shadow
   private Button disconnectButton;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void disableExit(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.disconnectButton != null) {
         this.disconnectButton.active = !PvpSafe.method_7();
      }
   }
}
