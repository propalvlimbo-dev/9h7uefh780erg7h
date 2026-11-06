package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.module.Modules;
import dev.rose.module.impl.hud.GlassHotbarModule;
import dev.rose.module.impl.hud.GlassScoreboardModule;
import dev.rose.module.impl.hud.TitlesModule;
import dev.rose.module.impl.visual.CrosshairModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public abstract class GuiMixin {
   @Shadow
   private Component title;
   @Shadow
   private int titleTime;
   @Shadow
   private int titleFadeInTime;
   @Shadow
   private int titleStayTime;
   @Shadow
   private int titleFadeOutTime;
   @Unique
   private boolean rose$titleScaled;

   @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
   private void rose$hideVanillaEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      if (Modules.potions() != null && Modules.potions().hidesVanilla()) {
         ci.cancel();
      }
   }

   @WrapWithCondition(
      method = "renderCrosshair",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
   )
   private boolean rose$customCrosshair(GuiGraphics guiGraphics, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
      CrosshairModule crosshair = Modules.crosshair();
      if (crosshair != null && crosshair.isEnabled() && sprite.getPath().equals("hud/crosshair")) {
         crosshair.render(guiGraphics);
         return false;
      } else {
         return true;
      }
   }

   @Inject(method = "renderItemHotbar", at = @At("HEAD"))
   private void rose$glassHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      GlassHotbarModule hotbar = Modules.glassHotbar();
      if (hotbar != null && hotbar.isEnabled() && Minecraft.getInstance().getCameraEntity() instanceof Player player) {
         hotbar.renderBackground(guiGraphics, player);
      }
   }

   @WrapWithCondition(
      method = "renderItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
   )
   private boolean rose$skipVanillaHotbar(GuiGraphics guiGraphics, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
      GlassHotbarModule hotbar = Modules.glassHotbar();
      if (hotbar != null && hotbar.isEnabled()) {
         String path = sprite.getPath();
         return !path.startsWith("hud/hotbar") || path.startsWith("hud/hotbar_attack");
      } else {
         return true;
      }
   }

   @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void rose$glassScoreboard(GuiGraphics guiGraphics, Objective objective, CallbackInfo ci) {
      GlassScoreboardModule scoreboard = Modules.glassScoreboard();
      if (scoreboard != null && scoreboard.isEnabled()) {
         scoreboard.render(guiGraphics, objective);
         ci.cancel();
      }
   }

   @Inject(method = "renderTitle", at = @At("HEAD"))
   private void rose$titleStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      TitlesModule titles = Modules.titles();
      this.rose$titleScaled = titles != null && titles.isEnabled() && this.title != null && this.titleTime > 0;
      if (this.rose$titleScaled) {
         float time = this.titleTime - deltaTracker.getGameTimeDeltaPartialTick(false);
         float scale = titles.scale(time, this.titleFadeInTime, this.titleStayTime, this.titleFadeOutTime);
         float centerX = guiGraphics.guiWidth() / 2.0F;
         float centerY = guiGraphics.guiHeight() / 2.0F;
         guiGraphics.pose().pushMatrix();
         guiGraphics.pose().translate(centerX, centerY);
         guiGraphics.pose().scale(scale, scale);
         guiGraphics.pose().translate(-centerX, -centerY);
      }
   }

   @Inject(method = "renderTitle", at = @At("TAIL"))
   private void rose$titleEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      if (this.rose$titleScaled) {
         guiGraphics.pose().popMatrix();
      }

      this.rose$titleScaled = false;
   }
}
