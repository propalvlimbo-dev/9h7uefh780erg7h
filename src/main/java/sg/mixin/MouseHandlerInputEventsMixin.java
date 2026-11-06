package sg.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.client.RoseClient;
import sg.events.impl.MouseButtonEvent;
import sg.events.impl.PlayerTurnEvent;
import sg.events.impl.InputEvent;
import sg.events.impl.MouseDeltaEvent;
import sg.render.WorldProjection;

@Mixin(MouseHandler.class)
public class MouseHandlerInputEventsMixin {
   @Shadow
   private double accumulatedDX;
   @Shadow
   private double accumulatedDY;

   @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
   private void onMouseButton(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      RoseClient.getInstance().getEventBus().post(new InputEvent(var3.button(), var4));
      Minecraft var6 = Minecraft.getInstance();
      if (var4 == 1 && var6.gui.screen() != null) {
         Vector2f var7 = WorldProjection.guiToScreen(var6.mouseHandler.xpos(), var6.mouseHandler.ypos());
         MouseButtonEvent var8 = new MouseButtonEvent(var7.x, var7.y, var3.button());
         RoseClient.getInstance().getEventBus().post(var8);
         if (var8.isCancelled()) {
            var5.cancel();
         }
      }
   }

   @Inject(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;turnPlayer(D)V", shift = Shift.AFTER))
   private void accumulateMove(CallbackInfo var1) {
      MouseDeltaEvent var2 = new MouseDeltaEvent(this.accumulatedDX, this.accumulatedDY);
      RoseClient.getInstance().getEventBus().post(var2);
   }

   @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
   public void changeLookDirection(LocalPlayer var1, double var2, double var4) {
      PlayerTurnEvent var6 = new PlayerTurnEvent((float)var2, (float)var4);
      RoseClient.getInstance().getEventBus().post(var6);
      if (!var6.isCancelled()) {
         var1.turn(var2, var4);
      }
   }
}
