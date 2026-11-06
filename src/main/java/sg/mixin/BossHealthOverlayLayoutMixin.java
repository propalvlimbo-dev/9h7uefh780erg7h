package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.hud.HudDisplacementManager;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayLayoutMixin {
   private static final float Х衣А = 9.0F;
   private static final float Х衣4 = 19.0F;

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void render(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.BOSSBAR)) {
         var2.cancel();
      }
   }

   @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 12))
   private int dimas$shiftBossBarY(int var1) {
      HudDisplacementManager.updateDisplacements();
      int var2 = Math.max(1, ((BossHealthOverlayAccessor)this).getBossBars().size());
      return var1 + Math.round(HudDisplacementManager.calculatePlayerListDisplacement(var1 - Х衣А, var2 * Х衣4));
   }

   @ModifyExpressionValue(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiHeight()I"))
   private int dimas$allowBossBarBelowTab(int var1) {
      return HudDisplacementManager.isPlayerListObstructionVisible() ? var1 * 3 : var1;
   }
}
