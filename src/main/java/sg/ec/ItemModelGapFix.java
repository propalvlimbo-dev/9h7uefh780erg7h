package sg.ec;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class ItemModelGapFix {
   private static final float еЙ = 0.001F;
   private static final float ек = 1.0E-4F;
   private static final float Хвы = 0.001F;
   private static final float Хвй = 1.0E-4F;
   private static final float ХвЖ = 0.001F;
   private static final float Хвм = 0.001F;
   private static final float ХвК = 1.0E-4F;
   private static final float ХвI = 0.001F;
   private static final float ХвТ = 0.001F;
   private static final float ХвО = 1.0E-4F;
   private static final float ХвФ = 0.001F;
   private static final float ХвЙ = 0.001F;
   private static final float Хвк = 1.0E-4F;
   private static final float Хвг = 0.001F;
   private static final float Хв0 = 1.0E-4F;
   private static final float Хвх = 0.001F;
   private static final float ХвУ = 0.001F;
   private static final float ХвЗ = 1.0E-4F;
   private static final float ХвЕ = 0.001F;
   private static final float ХвП = 0.001F;
   private static final float Хв西 = 1.0E-4F;
   private static final float Хвд = 0.001F;
   private static final float ХвБ = 0.001F;
   private static final float ХвС = 1.0E-4F;
   private static final float Хвз = 0.001F;
   private static final float Хвц = 0.001F;

   private ItemModelGapFix() {
   }

   public static void й(List<CuboidModelElement> var0) {
      ListIterator var1 = var0.listIterator();

      while (var1.hasNext()) {
         CuboidModelElement var2 = (CuboidModelElement)var1.next();
         Set var3 = var2.faces().keySet();
         if (var3.size() == 1) {
            Direction var4 = (Direction)var3.iterator().next();
            if (var4 == Direction.DOWN || var4 == Direction.UP || var4 == Direction.NORTH || var4 == Direction.SOUTH) {
               Vector3fc var5 = var2.from();
               Vector3fc var6 = var2.to();
               Vector3f var7 = new Vector3f(var5);
               Vector3f var8 = new Vector3f(var6);
               К(var7, var8, var4);
               var1.set(new CuboidModelElement(var7, var8, var2.faces(), var2.rotation(), var2.shade(), var2.lightEmission()));
            }
         }
      }
   }

   public static void К(Vector3f var0, Vector3f var1, Direction var2) {
      switch (var2) {
         case DOWN:
            var0.set(var0.x() - Хвы, var0.y() - Хвй, var0.z() - ХвЖ);
            var1.set(var1.x() + Хвм, var1.y() - ХвК, var1.z() + ХвI);
            break;
         case UP:
            var0.set(var0.x() - ХвТ, var0.y() + ХвО, var0.z() - ХвФ);
            var1.set(var1.x() + ХвЙ, var1.y() + Хвк, var1.z() + Хвг);
            break;
         case NORTH:
            var0.set(var0.x() - Хв0, var0.y() + Хвх, var0.z() - ХвУ);
            var1.set(var1.x() - ХвЗ, var1.y() - ХвЕ, var1.z() + ХвП);
            break;
         case SOUTH:
            var0.set(var0.x() + Хв西, var0.y() + Хвд, var0.z() - ХвБ);
            var1.set(var1.x() + ХвС, var1.y() - Хвз, var1.z() + Хвц);
      }
   }
}
