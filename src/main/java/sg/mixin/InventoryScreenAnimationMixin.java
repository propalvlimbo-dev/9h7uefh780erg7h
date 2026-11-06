package sg.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.UiAnimationEvents;
import sg.gui.ArrowSelectorScreen;
import sg.gui.SwapItemSelectorScreen;
import sg.client.RoseClient;
import sg.modules.impl.render.ShulkerView;
import sg.util.time.Stopwatch;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenAnimationMixin extends AbstractContainerScreen<InventoryMenu> {
   @Unique
   private Button dropAllButton;
   @Unique
   private boolean shouldDropItems;
   @Unique
   private final Stopwatch dropTimer = new Stopwatch();
   @Unique
   private boolean animationMatrixPushed;
   @Unique
   private boolean backgroundAnimationMatrixPushed;
   @Unique
   private float inventoryAnimationScale = 1.0F;
   private static final String ВOя = "Drop All";
   private static final long В7с = 50L;
   private static final float В7В = 0.5F;
   private static final float В7Я = 0.5F;
   private static final float В7Х = 0.5F;
   private static final float В7е = 0.5F;
   private static final float В7А = 0.5F;
   private static final float В74 = 0.5F;

   protected InventoryScreenAnimationMixin(InventoryMenu var1, Inventory var2, Component var3) {
      super(var1, var2, var3);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void addDropAllButton(CallbackInfo var1) {
      if (!((Object)this instanceof SwapItemSelectorScreen) && !((Object)this instanceof ArrowSelectorScreen)) {
         this.dropAllButton = Button.builder(Component.literal(ВOя), var1x -> {
            this.shouldDropItems = true;
            this.dropTimer.ВД();
         }).bounds(this.width / 2 - 35, this.height / 2 - 105, 70, 20).build();
         this.addRenderableWidget(this.dropAllButton);
         this.updateButtonState();
         dispatchInventoryAnimation(UiAnimationEvents.InventoryAnimationEvent.Phase.START);
      }
   }

   @Inject(method = "containerTick", at = @At("TAIL"))
   private void tickDrop(CallbackInfo var1) {
      this.updateButtonState();
      if (this.shouldDropItems && this.menu != null && this.dropTimer.method_0(В7с)) {
         boolean var2 = false;

         for (int var3 = 0; var3 < 5; var3++) {
            Slot var4 = null;

            for (Slot var6 : this.menu.slots) {
               if (var6.hasItem()) {
                  var4 = var6;
                  break;
               }
            }

            if (var4 == null) {
               break;
            }

            this.minecraft
               .gameMode
               .handleContainerInput(this.menu.containerId, var4.index, var4.getItem().getCount() > 1 ? 1 : 0, ContainerInput.THROW, this.minecraft.player);
            var2 = true;
         }

         if (!var2) {
            this.shouldDropItems = false;
            this.onClose();
         }

         this.dropTimer.ВД();
      }
   }

   @Unique
   private void updateButtonState() {
      if (this.dropAllButton != null && this.minecraft != null && this.minecraft.player != null) {
         this.dropAllButton.active = !this.minecraft.player.getInventory().isEmpty();
      }
   }

   @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At("HEAD"))
   public void render(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      UiAnimationEvents.InventoryAnimationEvent var6 = dispatchInventoryAnimation(UiAnimationEvents.InventoryAnimationEvent.Phase.UPDATE);
      this.animationMatrixPushed = var6.isAnimationEnabled();
      if (this.animationMatrixPushed) {
         this.pushInventoryScale(var1, var6.getScale());
      }
   }

   @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At("RETURN"))
   private void finishInventoryAnimation(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.animationMatrixPushed) {
         var1.pose().popMatrix();
         this.animationMatrixPushed = false;
      }

      ShulkerView.method_1(var1, this.getHoveredSlot(var2, var3), var2, var3);
   }

   @Inject(method = "extractBackground", at = @At("HEAD"))
   private void animateInventoryBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      UiAnimationEvents.InventoryAnimationEvent var6 = dispatchInventoryAnimation(UiAnimationEvents.InventoryAnimationEvent.Phase.UPDATE);
      this.backgroundAnimationMatrixPushed = var6.isAnimationEnabled();
      if (this.backgroundAnimationMatrixPushed) {
         this.inventoryAnimationScale = var6.getScale();
         this.pushInventoryScale(var1, this.inventoryAnimationScale);
      } else {
         this.inventoryAnimationScale = 1.0F;
      }
   }

   @Inject(method = "extractBackground", at = @At("RETURN"))
   private void finishInventoryBackgroundAnimation(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.backgroundAnimationMatrixPushed) {
         var1.pose().popMatrix();
         this.backgroundAnimationMatrixPushed = false;
      }
   }

   @Override
   public void extractTransparentBackground(GuiGraphicsExtractor var1) {
      if (!this.animationMatrixPushed && !this.backgroundAnimationMatrixPushed) {
         super.extractTransparentBackground(var1);
      } else {
         var1.pose().pushMatrix();

         try {
            var1.pose().identity();
            super.extractTransparentBackground(var1);
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Redirect(
      method = "extractBackground",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"
      )
   )
   private void animateInventoryPlayer(
      GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, LivingEntity var10
   ) {
      float var11 = this.inventoryAnimationScale;
      float var12 = this.width * В7В;
      float var13 = this.height * В7Я;
      int var14 = Math.round(var12 + (var2 - var12) * var11);
      int var15 = Math.round(var13 + (var3 - var13) * var11);
      int var16 = Math.round(var12 + (var4 - var12) * var11);
      int var17 = Math.round(var13 + (var5 - var13) * var11);
      int var18 = Math.max(1, Math.round(var6 * var11));
      InventoryScreen.extractEntityInInventoryFollowsMouse(var1, var14, var15, var16, var17, var18, var7, var8, var9, var10);
   }

   @Unique
   private void pushInventoryScale(GuiGraphicsExtractor var1, float var2) {
      var1.pose().pushMatrix();
      var1.pose().translate(this.width * В7Х, this.height * В7е);
      var1.pose().scale(var2, var2);
      var1.pose().translate(-this.width * В7А, -this.height * В74);
   }

   @Unique
   private static UiAnimationEvents.InventoryAnimationEvent dispatchInventoryAnimation(UiAnimationEvents.InventoryAnimationEvent.Phase var0) {
      UiAnimationEvents.InventoryAnimationEvent var1 = new UiAnimationEvents.InventoryAnimationEvent(var0);
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RoseClient.getInstance().getEventBus().post(var1);
      }

      return var1;
   }
}
