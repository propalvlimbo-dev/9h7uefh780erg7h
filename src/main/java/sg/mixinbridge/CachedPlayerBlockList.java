package sg.mixinbridge;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.Util;

public final class CachedPlayerBlockList {
   private static final long сЪ = TimeUnit.MINUTES.toNanos(CachedPlayerBlockList.ЯЦД);
   private static final Field Х = method_1();
   private final UserApiService В;
   private volatile Set<?> Ю = Set.of();
   private long ст;
   private static final String ЯЦ4 = "Cannot read the cached player block list";
   private static final String ЯЦп = "blockList";
   private static final long ЯЦД = 2L;

   public static boolean method_1(UserApiService var0) {
      return Х != null && var0 instanceof YggdrasilUserApiService;
   }

   private static Field method_1() {
      try {
         Field var0 = YggdrasilUserApiService.class.getDeclaredField(ЯЦп);
         return var0.trySetAccessible() ? var0 : null;
      } catch (NoSuchFieldException | SecurityException var1) {
         return null;
      }
   }

   public boolean method_1(UUID var1) {
      return var1 != null && (var1.getMostSignificantBits() != 0L || var1.getLeastSignificantBits() != 0L) && this.Ю.contains(var1);
   }

   public CompletableFuture<?> method_1(CompletableFuture<?> var1) {
      this.ст = System.nanoTime() + сЪ;
      return var1.handleAsync((var1x, var2) -> {
         this.method_1щ();
         return null;
      }, Util.nonCriticalIoPool());
   }

   private void method_1щ() {
      try {
         if (Х.get(this.В) instanceof Set var2) {
            this.Ю = Set.copyOf(var2);
         }
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException(ЯЦ4, var3);
      }
   }

   public CompletableFuture<?> ц(CompletableFuture<?> var1) {
      if (var1.isDone() && System.nanoTime() - this.ст >= 0L) {
         this.ст = System.nanoTime() + сЪ;
         return var1.handleAsync((var1x, var2) -> {
            this.В.refreshBlockList();
            this.method_1щ();
            return null;
         }, Util.nonCriticalIoPool());
      } else {
         return var1;
      }
   }

   public CachedPlayerBlockList(UserApiService var1) {
      this.В = var1;
   }
}
