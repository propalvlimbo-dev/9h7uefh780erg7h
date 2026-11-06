package sg.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.BlockPlacementEvent;
import sg.client.RoseClient;

@Mixin(Block.class)
public class BlockPlacementEventMixin {
   @Inject(method = "setPlacedBy", at = @At("HEAD"))
   public void onPlaced(Level var1, BlockPos var2, BlockState var3, @Nullable LivingEntity var4, ItemStack var5, CallbackInfo var6) {
      if (var1.isClientSide() && var4 == Minecraft.getInstance().player && var5.is(Items.OBSIDIAN)) {
         RoseClient.getInstance().getEventBus().post(new BlockPlacementEvent(var2));
      }
   }
}
