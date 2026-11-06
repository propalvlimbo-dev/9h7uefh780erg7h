package sg.events;

public interface EventListener<T extends ClientEvent> {
   void onEvent(T event);

   default int getPriority() {
      return 0;
   }
}
