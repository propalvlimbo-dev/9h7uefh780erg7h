package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rose.gui.screen.StatusDesign;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.server.level.progress.ChunkLoadStatusView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
   @WrapWithCondition(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;renderChunks(Lnet/minecraft/client/gui/GuiGraphics;IIIILnet/minecraft/server/level/progress/ChunkLoadStatusView;)V")
   )
   private boolean rose$chunks(GuiGraphics g, int x, int y, int size, int spacing, ChunkLoadStatusView view) {
      return !StatusDesign.styled((LevelLoadingScreen)(Object)this);
   }

   @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;drawProgressBar(Lnet/minecraft/client/gui/GuiGraphics;IIIIF)V"))
   private boolean rose$progressBar(LevelLoadingScreen screen, GuiGraphics g, int x, int y, int width, int height, float progress) {
      return !StatusDesign.styled(screen);
   }
}
