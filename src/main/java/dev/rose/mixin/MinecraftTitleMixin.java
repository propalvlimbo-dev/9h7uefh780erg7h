package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.RoseClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Minecraft.class)
public abstract class MinecraftTitleMixin {
   @ModifyReturnValue(method = "createTitle", at = @At("RETURN"))
   private String rose$testTitle(String title) {
      return RoseClient.uiTest() == null ? title : title + " — АВТОТЕСТ Rose, не трогать";
   }
}
