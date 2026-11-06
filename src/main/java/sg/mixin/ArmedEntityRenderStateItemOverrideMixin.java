package sg.mixin;

import sg.mixinbridge.HeldItemRenderOverrideEvent;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.client.RoseClient;

@Mixin(ArmedEntityRenderState.class)
public abstract class ArmedEntityRenderStateItemOverrideMixin {
   @ModifyExpressionValue(
      method = "extractArmedEntityRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getItemHeldByArm(Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/world/item/ItemStack;",
         ordinal = 0
      )
   )
   private static ItemStack replaceRightArmStack(ItemStack var0, LivingEntity var1) {
      HeldItemRenderOverrideEvent var2 = HeldItemRenderOverrideEvent.INSTANCE.resetForStackInArm(var1, HumanoidArm.RIGHT, var0);
      RoseClient.getInstance().getEventBus().post(var2);
      return var2.getStack();
   }

   @ModifyExpressionValue(
      method = "extractArmedEntityRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getItemHeldByArm(Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/world/item/ItemStack;",
         ordinal = 1
      )
   )
   private static ItemStack replaceLeftArmStack(ItemStack var0, LivingEntity var1) {
      HeldItemRenderOverrideEvent var2 = HeldItemRenderOverrideEvent.INSTANCE.resetForStackInArm(var1, HumanoidArm.LEFT, var0);
      RoseClient.getInstance().getEventBus().post(var2);
      return var2.getStack();
   }
}
