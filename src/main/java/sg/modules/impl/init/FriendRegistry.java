package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class FriendRegistry extends CopyOnWriteArrayList<FriendEntry> {
   private final AtomicInteger Х = new AtomicInteger();

   @Override
   public boolean removeIf(Predicate<? super FriendEntry> var1) {
      boolean var2 = super.removeIf(var1);
      if (var2) {
         this.Х.incrementAndGet();
      }

      return var2;
   }

   public Optional<FriendEntry> С(String var1) {
      return this.stream().filter(var1x -> var1x.lз().equalsIgnoreCase(var1)).findFirst();
   }

   public boolean add(FriendEntry var1) {
      boolean var2 = super.add(var1);
      if (var2) {
         this.Х.incrementAndGet();
      }

      return var2;
   }

   public int яб() {
      return this.Х.get();
   }

   public void К(String var1, String var2) {
      this.removeIf(var1x -> var1x.lз().equalsIgnoreCase(var1));
      this.add(new FriendEntry(var1, var2));
   }

   @Override
   public boolean remove(Object var1) {
      boolean var2 = super.remove(var1);
      if (var2) {
         this.Х.incrementAndGet();
      }

      return var2;
   }

   @Override
   public boolean addAll(Collection<? extends FriendEntry> var1) {
      boolean var2 = super.addAll(var1);
      if (var2) {
         this.Х.incrementAndGet();
      }

      return var2;
   }

   public List<String> а() {
      ArrayList var1 = new ArrayList();

      for (FriendEntry var3 : this) {
         var1.add(var3.lз());
      }

      return var1;
   }

   @Override
   public void clear() {
      if (!this.isEmpty()) {
         super.clear();
         this.Х.incrementAndGet();
      }
   }

   public boolean О(String var1) {
      return this.С(var1).isPresent();
   }
}
