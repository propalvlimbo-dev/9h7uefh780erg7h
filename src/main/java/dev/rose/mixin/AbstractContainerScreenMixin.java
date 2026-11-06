package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rose.gui.UiSounds;
import dev.rose.gui.screen.GlassSearchField;
import dev.rose.gui.screen.InventoryDesign;
import dev.rose.module.Modules;
import dev.rose.module.impl.ContainerSearchModule;
import dev.rose.module.impl.ItemLocksModule;
import dev.rose.module.impl.ScreensModule;
import dev.rose.module.impl.visual.ContainerAnimationModule;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
   @Shadow
   protected int imageWidth;
   @Shadow
   protected int imageHeight;
   @Shadow
   protected int leftPos;
   @Shadow
   protected int topPos;
   @Shadow
   protected Slot hoveredSlot;
   @Unique
   private long rose$openedAt;

   @Inject(method = "init", at = @At("TAIL"))
   private void rose$opened(CallbackInfo ci) {
      if (this.rose$openedAt == 0L) {
         this.rose$openedAt = System.currentTimeMillis();
      }
   }

   @WrapOperation(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"))
   private void rose$animateBackground(AbstractContainerScreen<?> screen, GuiGraphics g, float partialTick, int mouseX, int mouseY, Operation<Void> original) {
      ContainerAnimationModule module = Modules.containerAnimation();
      float progress = module == null ? 1.0F : module.progress(this.rose$openedAt);
      if (module != null && !(progress >= 1.0F)) {
         g.pose().pushMatrix();
         g.pose().translate(this.leftPos, this.topPos);
         module.apply(g.pose(), this.imageWidth, this.imageHeight, progress);
         g.pose().translate(-this.leftPos, -this.topPos);
         this.rose$background(screen, g, partialTick, mouseX, mouseY, original);
         g.pose().popMatrix();
      } else {
         this.rose$background(screen, g, partialTick, mouseX, mouseY, original);
      }
   }

   @Unique
   private void rose$background(AbstractContainerScreen<?> screen, GuiGraphics g, float partialTick, int mouseX, int mouseY, Operation<Void> original) {
      if (InventoryDesign.handles(screen)) {
         try {
            InventoryDesign.renderBackground(screen, g, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseX, mouseY);
            return;
         } catch (RuntimeException e) {
            ScreensModule.crashed(e);
         }
      }

      original.call(new Object[]{screen, g, partialTick, mouseX, mouseY});
   }

   @ModifyArg(
      method = "renderLabels",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"),
      index = 4
   )
   private int rose$labelColor(int color) {
      return InventoryDesign.handles((AbstractContainerScreen<?>)(Object)this) ? -1644564 : color;
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void rose$keys(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
      int key = event.key();
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)(Object)this;
      ContainerSearchModule search = Modules.containerSearch();
      if (search != null && search.typing(screen)) {
         GlassSearchField box = search.box(screen);
         if (key != 256 && key != 257 && key != 335) {
            box.keyPressed(event);
         } else {
            if (key == 256) {
               box.setValue("");
            }

            box.setFocused(false);
            screen.setFocused(null);
         }

         cir.setReturnValue(true);
      } else {
         ItemLocksModule locks = Modules.itemLocks();
         if (locks != null && key == locks.lockKey() && locks.toggle(this.hoveredSlot)) {
            UiSounds.play(UiSounds.Sound.CLICK);
            cir.setReturnValue(true);
         }
      }
   }

   @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
   private void rose$lockedThrow(Slot slot, int slotId, int button, ClickType type, CallbackInfo ci) {
      ItemLocksModule locks = Modules.itemLocks();
      if (type == ClickType.THROW && locks != null && locks.isLocked(slot)) {
         locks.notifyBlocked();
         ci.cancel();
      }
   }

   @Inject(method = "renderSlot", at = @At("TAIL"))
   private void rose$slotOverlay(GuiGraphics g, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)(Object)this;
      ContainerSearchModule search = Modules.containerSearch();
      String query = search == null ? "" : search.query(screen);
      ItemLocksModule locks = Modules.itemLocks();
      boolean locked = locks != null && locks.isLocked(slot);
      if (!query.isEmpty() || locked) {
         InventoryDesign.renderSlotMarks(
            g, slot.x, slot.y, query.isEmpty() ? 0 : (ContainerSearchModule.matches(slot.getItem(), query) ? 1 : -1), locked
         );
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"))
   private void rose$searchBlur(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
      double mouseX = event.x();
      double mouseY = event.y();
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)(Object)this;
      ContainerSearchModule search = Modules.containerSearch();
      if (search != null && search.typing(screen)) {
         GlassSearchField box = search.box(screen);
         if (!box.isMouseOver(mouseX, mouseY)) {
            box.setFocused(false);
            screen.setFocused(null);
         }
      }
   }

   @Inject(method = "render", at = @At("HEAD"))
   private void rose$placeSearch(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      ContainerSearchModule search = Modules.containerSearch();
      if (search != null) {
         search.place((AbstractContainerScreen)(Object)this, this.leftPos, this.topPos, this.imageWidth);
      }
   }

   @Inject(method = "renderSlotHighlightBack", at = @At("HEAD"), cancellable = true)
   private void rose$slotHighlight(GuiGraphics g, CallbackInfo ci) {
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)(Object)this;
      if (InventoryDesign.handles(screen)) {
         if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
            InventoryDesign.renderSlotHighlight(g, this.hoveredSlot.x, this.hoveredSlot.y);
         }

         ci.cancel();
      }
   }

   @Inject(method = "renderSlotHighlightFront", at = @At("HEAD"), cancellable = true)
   private void rose$slotHighlightFront(GuiGraphics g, CallbackInfo ci) {
      if (InventoryDesign.handles((AbstractContainerScreen<?>)(Object)this)) {
         ci.cancel();
      }
   }

   @Inject(
      method = "renderContents",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;translate(FF)Lorg/joml/Matrix3x2f;", ordinal = 0, shift = Shift.AFTER)
   )
   private void rose$animateSlots(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      ContainerAnimationModule module = Modules.containerAnimation();
      if (module != null) {
         module.apply(g.pose(), this.imageWidth, this.imageHeight, module.progress(this.rose$openedAt));
      }
   }
}
