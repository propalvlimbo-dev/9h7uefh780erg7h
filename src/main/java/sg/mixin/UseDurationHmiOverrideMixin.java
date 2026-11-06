package sg.mixin;

import sg.mixinbridge.HmiScriptState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.HmiIntegration;

@Mixin(UseDuration.class)
public class UseDurationHmiOverrideMixin {
   @Inject(method = "get", at = @At("RETURN"), cancellable = true)
   private void hmiSwapProperty(ItemStack var1, ClientLevel var2, ItemOwner var3, int var4, CallbackInfoReturnable<Float> var5) {
      if (HmiIntegration.п弟()) {
         Minecraft var6 = Minecraft.getInstance();
         if (var6.player != null && var6.player.getUseItem() == var1) {
            String var7 = var1.getItem().toString();
            if (HmiScriptState.field_4.containsKey(var7)) {
               var5.setReturnValue(Float.parseFloat(HmiScriptState.field_4.get(var7).toString()));
            }
         }
      }
   }
}
