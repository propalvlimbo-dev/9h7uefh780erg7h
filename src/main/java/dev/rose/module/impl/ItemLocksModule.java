package dev.rose.module.impl;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.notify.Notifications;
import dev.rose.setting.KeySetting;
import dev.rose.setting.TextSetting;
import java.util.BitSet;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;


public final class ItemLocksModule extends Module {
   private static final int SLOTS = 41;
   private final KeySetting lockKey = this.add(new KeySetting("lock_key", 76));
   private final TextSetting stored = this.add(new TextSetting("slots", "", 200)).visibleWhen(() -> false);
   private final BitSet locked = new BitSet(41);
   private long lastNotice;

   public ItemLocksModule() {
      super("item_locks", Category.INTERFACE, true);
   }

   @Override
   public int icon() {
      return 57611;
   }

   @Override
   public boolean hasKeybind() {
      return false;
   }

   @Override
   public void onConfigLoaded() {
      this.locked.clear();

      for (String part : this.stored.get().split(",")) {
         try {
            int slot = Integer.parseInt(part.trim());
            if (slot >= 0 && slot < 41) {
               this.locked.set(slot);
            }
         } catch (NumberFormatException var6) {
         }
      }
   }

   private void store() {
      StringBuilder text = new StringBuilder();

      for (int slot = this.locked.nextSetBit(0); slot >= 0; slot = this.locked.nextSetBit(slot + 1)) {
         if (!text.isEmpty()) {
            text.append(',');
         }

         text.append(slot);
      }

      this.stored.set(text.toString());
   }

   public int lockKey() {
      return this.lockKey.get();
   }

   public boolean isLocked(int inventorySlot) {
      return this.isEnabled() && inventorySlot >= 0 && inventorySlot < 41 && this.locked.get(inventorySlot);
   }

   public boolean isLocked(Slot slot) {
      return slot != null && slot.container instanceof Inventory && this.isLocked(slot.getContainerSlot());
   }

   public boolean toggle(Slot slot) {
      if (this.isEnabled() && slot != null && slot.container instanceof Inventory) {
         int index = slot.getContainerSlot();
         if (index >= 0 && index < 41) {
            this.locked.flip(index);
            this.store();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void notifyBlocked() {
      long now = System.currentTimeMillis();
      if (now - this.lastNotice >= 2500L) {
         this.lastNotice = now;
         Notifications.show(
            Notifications.Kind.NEUTRAL,
            I18n.get("rose.item_locks.blocked", new Object[0]),
            I18n.get("rose.item_locks.blocked.hint", new Object[0])
         );
      }
   }
}
