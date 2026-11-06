package sg.mixin;

import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.ChatMessageCopy;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$DrawingFocusedGraphicsAccess")
public abstract class ChatDrawingFocusedGraphicsHoverMixin {
   @Inject(method = "handleMessage", at = @At("RETURN"))
   private void dimas$captureHoveredLine(int var1, float var2, FormattedCharSequence var3, CallbackInfoReturnable<Boolean> var4) {
      if (Boolean.TRUE.equals(var4.getReturnValue()) && var3 != null) {
         ChatMessageCopy.п_de5a6f78(var3);
      }
   }
}
