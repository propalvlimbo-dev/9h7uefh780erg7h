package sg.network;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class StyledComponentCache<T> {
   private static volatile int field_4Л;
   @SerializedName("4诶")
   private final int field_4诶;
   private final LinkedHashMap<List<StyledComponentCache.StyledRun>, T> е;
   @SerializedName("41")
   private int field_41;
   private static final float Вгб = 0.75F;

   public void Ъ2() {
      this.е.clear();
   }

   public static void Ъ1() {
      field_4Л++;
   }

   public T I(Component var1, Function<Component, T> var2) {
      if (this.field_41 != field_4Л) {
         this.Ъ2();
         this.field_41 = field_4Л;
      }

      ArrayList var3 = new ArrayList();
      var1.visit((var1x, var2x) -> {
         if (!var2x.isEmpty()) {
            var3.add(new StyledComponentCache.StyledRun(var2x, var1x));
         }

         return Optional.empty();
      }, Style.EMPTY);
      if (var3.size() > 128) {
         return (T)var2.apply(var1);
      }

      List var4 = List.copyOf(var3);
      Object var5 = this.е.get(var4);
      if (var5 != null) {
         return (T)var5;
      }

      Object var6 = var2.apply(var1);
      if (this.е.size() >= this.field_4诶) {
         this.е.remove(this.е.keySet().iterator().next());
      }

      this.е.put(var4, (T)var6);
      return (T)var6;
   }

   public StyledComponentCache(int var1) {
      this.е = new LinkedHashMap<>(64, Вгб, true);
      this.field_41 = field_4Л;
      this.field_4诶 = Math.max(1, var1);
   }

   private record StyledRun(String text, Style style) {
   }
}
