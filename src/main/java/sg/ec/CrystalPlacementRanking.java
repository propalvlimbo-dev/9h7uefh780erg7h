package sg.ec;

public final class CrystalPlacementRanking {
   private static final float рл = 30.0F;

   private CrystalPlacementRanking() {
   }

   public static boolean М(boolean var0, boolean var1, boolean var2, float var3) {
      return !var0 || !var1 || !var2 || !(var3 > рл);
   }

   public record PlacementCost(int blocks, float ticks, boolean current, boolean preferred, float score)
      implements Comparable<CrystalPlacementRanking.PlacementCost> {
      public int compareTo(CrystalPlacementRanking.PlacementCost var1) {
         if (this.blocks == 0 != (var1.blocks == 0)) {
            return this.blocks == 0 ? -1 : 1;
         }

         if (this.blocks > 0) {
            if (Float.compare(this.ticks, var1.ticks) != 0) {
               return Float.compare(this.ticks, var1.ticks);
            }

            if (this.blocks != var1.blocks) {
               return Integer.compare(this.blocks, var1.blocks);
            }

            if (this.current != var1.current) {
               return this.current ? -1 : 1;
            }
         }

         if (this.preferred != var1.preferred) {
            return this.preferred ? -1 : 1;
         } else {
            return Float.compare(var1.score, this.score);
         }
      }
   }
}
