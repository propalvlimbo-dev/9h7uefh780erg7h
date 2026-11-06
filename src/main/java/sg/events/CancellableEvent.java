package sg.events;

import sg.events.ClientEvent;

public abstract class CancellableEvent implements ClientEvent, Cancellable {
   private @com.google.gson.annotations.SerializedName("ХЛ")  boolean cancelled;

   @Override
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Override
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
