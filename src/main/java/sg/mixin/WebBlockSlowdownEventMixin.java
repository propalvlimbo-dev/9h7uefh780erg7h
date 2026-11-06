package sg.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.WebSlowdownEvent;
import sg.client.RoseClient;

@Mixin(WebBlock.class)
public class WebBlockSlowdownEventMixin {
   private static final double ВмХ = 0.5;
   private static final double Вме = 0.25;
   private static final double ВмА = 0.5;

   @Inject(
      method = "entityInside",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V",
         shift = Shift.BEFORE
      ),
      cancellable = true
   )
   private void inside(BlockState var1, Level var2, BlockPos var3, Entity var4, InsideBlockEffectApplier var5, boolean var6, CallbackInfo var7) {
      if (var4 instanceof LocalPlayer) {
         WebSlowdownEvent var8 = new WebSlowdownEvent(new Vec3(ВмХ, Вме, ВмА), var3);
         RoseClient.getInstance().getEventBus().post(var8);
         if (var8.isCancelled()) {
            var7.cancel();
         }
      }
   }
}
