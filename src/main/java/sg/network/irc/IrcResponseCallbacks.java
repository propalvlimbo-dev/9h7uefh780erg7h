package sg.network.irc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sg.modules.impl.init.CallbackIrcPacket;
import sg.modules.impl.init.IrcResponseCallback;

public interface IrcResponseCallbacks {
   Map<Class<? extends CallbackIrcPacket>, IrcResponseCallback> сЯ = new ConcurrentHashMap<>();

   default void н(String... var1) {
      IrcResponseCallback var2 = сЯ.get(this.getClass());
      if (var2 != null) {
         var2.Б(var1);
      }
   }

   static <T extends CallbackIrcPacket> void н(Class<T> var0, IrcResponseCallback var1) {
      сЯ.put(var0, var1);
   }
}
