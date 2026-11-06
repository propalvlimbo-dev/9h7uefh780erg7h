package sg.mixin;

import sg.mixinbridge.HmiScriptState;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.HmiIntegration;

@Mixin(IsUsingItem.class)
public class IsUsingItemHmiOverrideMixin {
   @Inject(method = "get", at = @At("RETURN"), cancellable = true)
   private void hmiSwapProperty(ItemStack var1, ClientLevel var2, LivingEntity var3, int var4, ItemDisplayContext var5, CallbackInfoReturnable<Boolean> var6) {
      if (HmiIntegration.п弟()) {
         if (var3 != null && var3.getUseItem() == var1) {
            String var7 = var1.getItem().toString();
            if (HmiScriptState.п.containsKey(var7)) {
               var6.setReturnValue(HmiScriptState.п.get(var7));
            }
         }
      }
   }
}
