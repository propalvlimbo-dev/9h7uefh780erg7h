package dev.rose.client.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import dev.rose.client.render.gfx.RoseGfx;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws everything Rose recorded during the GUI frame.
 * Runs after vanilla has finished its own GUI passes, i.e. on top of the whole interface.
 */
@Mixin(GuiRenderer.class)
public class GuiRendererMixin {

	@Inject(method = "render", at = @At("TAIL"))
	private void rose$flushGui(GpuBufferSlice fog, CallbackInfo ci) {
		RoseGfx.flush();
	}
}
