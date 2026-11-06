package sg.mixin;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.events.impl.ChatMouseReleaseEvent;
import sg.client.RoseClient;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerMouseEventsMixin {
   @Inject(method = "mouseReleased", at = @At("TAIL"))
   private void mouseReleased(MouseButtonEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (this instanceof ChatScreen && var1.button() == 0) {
         ChatMouseReleaseEvent var3 = new ChatMouseReleaseEvent();
         RoseClient.getInstance().getEventBus().post(var3);
      }
   }
}
