package sg.events;

import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import sg.events.ClientEvent;
import sg.events.EventListener;

public class EventBus {
   @SerializedName("В")
   private final ConcurrentHashMap<Type, CopyOnWriteArrayList<EventListener<?>>> listenersByType = new ConcurrentHashMap<>();
   @SerializedName("В")
   private final Map<Class<?>, Field[]> declaredFieldsByClass = new HashMap<>();
   @SerializedName("В")
   private final Comparator<EventListener<?>> listenerPriorityOrder = Comparator.<EventListener<?>>comparingInt(listener -> listener.getPriority()).reversed();
   @SerializedName("В")
   private final BiConsumer<List<EventListener<?>>, Comparator<EventListener<?>>> listenerSorter = List::sort;
   @SerializedName("В")
   private final Consumer<Throwable> errorHandler = Throwable::printStackTrace;

   public <T extends ClientEvent> void post(T event) {
      Class eventClass = event.getClass();
      CopyOnWriteArrayList<EventListener<?>> listeners = this.listenersByType.get(eventClass);
      if (listeners != null) {
         for (EventListener listener : listeners) {
            try {
               EventListener invokableListener = listener;
               invokableListener.onEvent(event);
            } catch (Throwable dispatchError) {
               this.errorHandler.accept(dispatchError);
            }
         }
      }
   }

   private Field[] getDeclaredFields(Class<?> ownerClass) {
      return this.declaredFieldsByClass.computeIfAbsent(ownerClass, Class::getDeclaredFields);
   }

   private void visitListeners(Object owner, BiConsumer<Type, EventListener<?>> visitor) {
      Class ownerClass = owner.getClass();

      for (Field field : this.getDeclaredFields(ownerClass)) {
         if (field.getType() == EventListener.class) {
            EventListener listener = this.getListener(owner, field);
            Type eventType = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
            visitor.accept(eventType, listener);
         }
      }
   }

   private EventListener<?> getListener(Object owner, Field field) {
      boolean wasAccessible = field.canAccess(owner);
      field.setAccessible(true);
      EventListener listener = null;

      try {
         listener = (EventListener)field.get(owner);
      } catch (IllegalAccessException accessError) {
         accessError.printStackTrace();
      } finally {
         field.setAccessible(wasAccessible);
      }

      return listener;
   }

   public void register(Object owner) {
      this.visitListeners(owner, (eventType, listener) -> {
         this.listenersByType.computeIfAbsent(eventType, ignoredType -> new CopyOnWriteArrayList<>()).add(listener);
         this.listenerSorter.accept(this.listenersByType.get(eventType), this.listenerPriorityOrder);
      });
   }

   public void unregister(Object owner) {
      this.visitListeners(owner, (eventType, listener) -> {
         CopyOnWriteArrayList listeners = this.listenersByType.get(eventType);
         if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
               this.listenersByType.remove(eventType);
            }
         }
      });
   }
}
