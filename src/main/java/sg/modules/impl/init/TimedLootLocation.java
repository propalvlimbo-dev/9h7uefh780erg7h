package sg.modules.impl.init;

import java.util.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

public final class TimedLootLocation {
   private final Item Х;
   private final Vec3 O;
   private final String Вь;
   private final int АМ;
   private final double В衣;

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof TimedLootLocation var2)
            ? false
            : this.АМ == var2.АМ
               && Double.compare(this.В衣, var2.В衣) == 0
               && Objects.equals(this.Х, var2.Х)
               && Objects.equals(this.O, var2.O)
               && Objects.equals(this.Вь, var2.Вь);
      }
   }

   public String ю必() {
      return this.Вь;
   }

   public double К() {
      return this.В衣;
   }

   public int Ц5() {
      return this.АМ;
   }

   public Item ы() {
      return this.Х;
   }

   public Vec3 т() {
      return this.O;
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.Х, this.O, this.Вь, this.АМ, this.В衣);
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName()
         + "[item="
         + this.Х
         + ", vec="
         + this.O
         + ", world="
         + this.Вь
         + ", anarchy="
         + this.АМ
         + ", time="
         + this.В衣
         + "]";
   }

   public TimedLootLocation(Item var1, Vec3 var2, String var3, int var4, double var5) {
      this.Х = var1;
      this.O = var2;
      this.Вь = var3;
      this.АМ = var4;
      this.В衣 = var5;
   }
}
