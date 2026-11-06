package sg.events;

public interface Cancellable {
   void setCancelled(boolean cancelled);

   boolean isCancelled();
}
