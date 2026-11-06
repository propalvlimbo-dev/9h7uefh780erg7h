package settings.render;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class ClientSoundManager {
   private static final ClientSoundManager В = new ClientSoundManager();
   private static final int Ъю = 4;
   private final Map<String, WavSoundAsset> Ва = new ConcurrentHashMap<>();
   @SerializedName("4")
   private final AtomicInteger field_4 = new AtomicInteger();
   private final ExecutorService А = Executors.newCachedThreadPool(new ClientSoundManager.SoundThreadFactory());

   private ClientSoundManager() {
   }

   public void method_4(String var1, float var2) {
      if (!(var2 <= 0.0F)) {
         WavSoundAsset var3 = this.Ва.computeIfAbsent(var1, WavSoundAsset::new);
         if (!var3.О1()) {
            if (this.field_4.get() < 4) {
               this.field_4.incrementAndGet();
               this.А.execute(() -> {
                  try {
                     var3.诶в(var2);
                  } finally {
                     this.field_4.decrementAndGet();
                  }
               });
            }
         }
      }
   }

   public static ClientSoundManager method_7() {
      return В;
   }

   private static final class SoundThreadFactory implements ThreadFactory {
      private final AtomicInteger Д = new AtomicInteger();

      @Override
      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(var1, "rose-voice-" + this.Д.incrementAndGet());
         var2.setDaemon(true);
         var2.setPriority(1);
         return var2;
      }
   }
}
