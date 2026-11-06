package dev.rose.mixin;

import dev.rose.gui.screen.ServerListDesign;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractSelectionList.class)
public abstract class AbstractSelectionListMixin {
   @Inject(method = "renderListBackground", at = @At("HEAD"), cancellable = true)
   private void rose$background(GuiGraphics g, CallbackInfo ci) {
      if (ServerListDesign.styled(this)) {
         ci.cancel();
      }
   }

   @Inject(method = "renderListSeparators", at = @At("HEAD"), cancellable = true)
   private void rose$separators(GuiGraphics g, CallbackInfo ci) {
      if (ServerListDesign.styled(this)) {
         ci.cancel();
      }
   }

   @Inject(method = "renderItem", at = @At("HEAD"))
   private void rose$card(GuiGraphics g, int mouseX, int mouseY, float partialTick, @Coerce LayoutElement entry, CallbackInfo ci) {
      if (ServerListDesign.styled(this)) {
         AbstractSelectionList<?> list = (AbstractSelectionList<?>)(Object)this;
         int index = list.children().indexOf(entry);
         if (index >= 0) {
            ServerListDesign.card(
               list,
               g,
               index,
               entry.getX(),
               entry.getY(),
               entry.getWidth(),
               entry.getHeight(),
               list.getSelected() == entry,
               list.isMouseOver(mouseX, mouseY) && ((GuiEventListener)entry).isMouseOver(mouseX, mouseY)
            );
         }
      }
   }

   @Inject(method = "renderSelection", at = @At("HEAD"), cancellable = true)
   private void rose$selection(GuiGraphics g, @Coerce LayoutElement entry, int color, CallbackInfo ci) {
      if (ServerListDesign.styled(this)) {
         ci.cancel();
      }
   }
}
