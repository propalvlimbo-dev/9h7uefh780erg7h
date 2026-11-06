package sg.events.impl;

import lombok.Generated;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import sg.events.ClientEvent;

public class ContainerSlotActionEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("Х")  AbstractContainerScreen<?> screen;
   private final @com.google.gson.annotations.SerializedName("В")  Slot slot;
   private final @com.google.gson.annotations.SerializedName("а")  Runnable quickMoveAction;

   @Generated
   public Slot getSlot() {
      return this.slot;
   }

   @Generated
   public AbstractContainerScreen<?> getScreen() {
      return this.screen;
   }

   @Generated
   public ContainerSlotActionEvent(AbstractContainerScreen<?> screen, Slot slot, Runnable quickMoveAction) {
      this.screen = screen;
      this.slot = slot;
      this.quickMoveAction = quickMoveAction;
   }

   public void runQuickMove() {
      this.quickMoveAction.run();
   }
}
