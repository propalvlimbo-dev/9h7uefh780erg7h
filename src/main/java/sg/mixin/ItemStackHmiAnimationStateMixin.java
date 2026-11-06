package sg.mixin;

import sg.mixinbridge.HmiItemStackAccess;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackHmiAnimationStateMixin implements HmiItemStackAccess {
   @Unique
   private int transform = -1;
   @Unique
   private int swingSpeed = 10;
   @Unique
   private int shouldRenderAsBlock = -1;

   @Override
   public void hMI5_0$setTransform(boolean var1) {
      this.transform = var1 ? 1 : 0;
   }

   @Override
   public void hMI5_0$setTransform(int var1) {
      this.transform = var1;
   }

   @Override
   public int hMI5_0$getTransform() {
      return this.transform;
   }

   @Override
   public void hMI5_0$setSwingSpeed(int var1) {
      this.swingSpeed = var1;
   }

   @Override
   public int hMI5_0$getSwingSpeed() {
      return this.swingSpeed;
   }

   @Override
   public void hMI5_0$setRenderAsBlock(boolean var1) {
      this.shouldRenderAsBlock = var1 ? 1 : 0;
   }

   @Override
   public int hMI5_0$getRenderAsBlock() {
      return 0;
   }
}
