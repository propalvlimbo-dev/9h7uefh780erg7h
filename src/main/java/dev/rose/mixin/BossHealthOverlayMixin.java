package dev.rose.mixin;

import dev.rose.module.Modules;
import dev.rose.module.impl.hud.GlassBossbarModule;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
   @Shadow
   @Final
   Map<UUID, LerpingBossEvent> events;
   @Unique
   private float rose$shift;

   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void rose$glassBossbar(GuiGraphics guiGraphics, CallbackInfo ci) {
      GlassBossbarModule bossbar = Modules.glassBossbar();
      if (bossbar != null && bossbar.isEnabled()) {
         bossbar.render(guiGraphics, this.events.values());
         ci.cancel();
      } else {
         this.rose$shift = Modules.island() != null && !this.events.isEmpty()
            ? Math.max(0.0F, Modules.island().reservedTop(guiGraphics.guiWidth()) - 7.0F)
            : 0.0F;
         if (this.rose$shift > 0.0F) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0.0F, this.rose$shift);
         }
      }
   }

   @Inject(method = "render", at = @At("RETURN"))
   private void rose$restoreBossbar(GuiGraphics guiGraphics, CallbackInfo ci) {
      if (this.rose$shift > 0.0F) {
         guiGraphics.pose().popMatrix();
         this.rose$shift = 0.0F;
      }
   }
}
