package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public final class BlockDestroyDelayEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("Дщ")  int destroyDelay;
   private @com.google.gson.annotations.SerializedName("ер")  boolean clearDelayRequested;

   @Generated
   public int getDestroyDelay() {
      return this.destroyDelay;
   }

   @Generated
   public void setClearDelayRequested(boolean clearDelayRequested) {
      this.clearDelayRequested = clearDelayRequested;
   }

   @Generated
   public boolean isClearDelayRequested() {
      return this.clearDelayRequested;
   }

   public BlockDestroyDelayEvent(int destroyDelay) {
      this.destroyDelay = destroyDelay;
   }
}
