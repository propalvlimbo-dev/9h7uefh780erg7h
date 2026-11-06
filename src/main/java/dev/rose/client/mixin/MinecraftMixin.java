package dev.rose.client.mixin;

import dev.rose.client.screens.RoseTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces the vanilla title screen with the Rose main menu. */
@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void rose$replaceTitleScreen(Screen screen, CallbackInfo ci) {
		if (screen instanceof TitleScreen) {
			ci.cancel();
			((Minecraft) (Object) this).setScreen(new RoseTitleScreen());
		}
	}
}
