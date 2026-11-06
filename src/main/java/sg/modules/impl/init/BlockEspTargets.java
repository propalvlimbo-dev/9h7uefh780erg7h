package sg.modules.impl.init;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.resources.Identifier;

public final class BlockEspTargets {
   private final CopyOnWriteArrayList<BlockEspTargets.Target> а = new CopyOnWriteArrayList<>();
   private final AtomicInteger п = new AtomicInteger();
   private static final int сOГ = 16777215;
   private static final int сOл = 16777215;
   private static final String сOэ = ":";

   public void с(Collection<BlockEspTargets.Target> var1) {
      this.а.clear();
      if (var1 != null) {
         for (BlockEspTargets.Target var3 : var1) {
            if (var3 != null) {
               String var4 = То_388008bb(var3.target());
               if (var4 != null) {
                  this.а.removeIf(var1x -> var1x.target().equalsIgnoreCase(var4));
                  this.а.add(new BlockEspTargets.Target(var4, var3.color() & сOл));
               }
            }
         }
      }

      this.п.incrementAndGet();
   }

   public boolean O(String var1, int var2) {
      String var3 = То_388008bb(var1);
      if (var3 == null) {
         return false;
      }

      this.а.removeIf(var1x -> var1x.target().equalsIgnoreCase(var3));
      this.а.add(new BlockEspTargets.Target(var3, var2 & сOГ));
      this.п.incrementAndGet();
      return true;
   }

   public int РО() {
      return this.п.get();
   }

   public List<BlockEspTargets.Target> Т2() {
      return List.copyOf(this.а);
   }

   public int РД() {
      int var1 = this.а.size();
      if (var1 == 0) {
         return 0;
      }

      this.а.clear();
      this.п.incrementAndGet();
      return var1;
   }

   public boolean То_bbeebd87(String var1) {
      String var2 = То_388008bb(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = this.а.removeIf(var1x -> var1x.target().equalsIgnoreCase(var2));
      if (var3) {
         this.п.incrementAndGet();
      }

      return var3;
   }

   public static String То_388008bb(String var0) {
      if (var0 != null && !var0.isBlank()) {
         String var1 = var0.trim().toLowerCase(Locale.ROOT);
         if (!var1.contains(сOэ)) {
            var1 = "minecraft:" + var1;
         }

         Identifier var2 = Identifier.tryParse(var1);
         return var2 == null ? null : var2.toString();
      } else {
         return null;
      }
   }

   public record Target(String target, int color) {
   }
}
