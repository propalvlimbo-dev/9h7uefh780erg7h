package sg.mixin;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.client.Options;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.ViaProtocolVersion;
import sg.client.RoseClient;
import sg.events.impl.MovementInputEvent;
import sg.modules.impl.init.MovementUtils;
import sg.rotation.RotationStrategy;

@Mixin(KeyboardInput.class)
public class KeyboardInputMovementEventMixin {
   @Final
   @Shadow
   private Options options;

   @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
   public void tick(CallbackInfo var1) {
      var1.cancel();
      MovementInputEvent var2 = new MovementInputEvent(
         KeyboardInput.calculateImpulse(this.options.keyUp.isDown(), this.options.keyDown.isDown()),
         KeyboardInput.calculateImpulse(this.options.keyLeft.isDown(), this.options.keyRight.isDown()),
         this.options.keyUp.isDown(),
         this.options.keyDown.isDown(),
         this.options.keyLeft.isDown(),
         this.options.keyRight.isDown(),
         this.options.keyJump.isDown(),
         this.options.keyShift.isDown(),
         this.options.keySprint.isDown()
      );
      RoseClient.getInstance().getEventBus().post(var2);
      if (!var2.isCancelled()) {
         RotationStrategy var3 = RoseClient.getInstance().getServices().getRotations().getActiveStrategy();
         if (var3 != null && var3.shouldCaptureCameraRotationOnActivate()) {
            MovementUtils.correctMovementInput(var2);
         }

         ((KeyboardInput)(Object)this).keyPresses = new Input(var2.isForwardPressed(), var2.isBackwardPressed(), var2.isLeftPressed(), var2.isRightPressed(), var2.isJumpPressed(), var2.isSneakPressed(), var2.isSprintPressed());
         if (ViaProtocolVersion.Г() != null && ViaProtocolVersion.Г().olderThanOrEqualTo(ProtocolVersion.v1_21_4)) {
            ((KeyboardInput)(Object)this).moveVector = new Vec2(var2.getLeftAmount(), var2.getForwardAmount());
         } else {
            ((KeyboardInput)(Object)this).moveVector = new Vec2(var2.getLeftAmount(), var2.getForwardAmount()).normalized();
         }
      }
   }
}
