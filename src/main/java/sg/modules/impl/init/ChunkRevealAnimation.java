package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import sg.animation.Easings;
import sg.modules.impl.render.BetterMinecraft;

public final class ChunkRevealAnimation {
   private final Minecraft Х = Minecraft.getInstance();
   private final WeakHashMap<Object, ChunkRevealAnimation.SectionState> В = new WeakHashMap<>();
   @SerializedName("4")
   private ClientLevel field_4;
   private static final String ВЪв = "По направлению";
   private static final double ВЪъ = 1000000.0;
   private static final String ВЪМ = "От горизонта";
   private static final String ВЪН = "Снизу";
   private static final String ВЪн = "Сверху";
   private static final String ВЪЦ = "Сверху";
   private static final String ВЪч = "От игрока";
   private static final String ВЪЭ = "По направлению";
   private static final double ВЪ必 = 8.0;
   private static final double ВЪщ = 8.0;
   private static final String ВЪГ = "Quad";
   private static final String ВЪл = "Cubic";
   private static final String ВЪэ = "Quart";
   private static final String ВЪю = "Quint";
   private static final String ВЪЩ = "Expo";
   private static final String ВЪу = "Sine";
   private static final String ВЪ2 = "Circ";
   private static final String ВЪф = "Back";
   private static final String ВЪи = "Bounce";
   private static final String ВЪ9 = "Elastic";

   public void Б(Object var1, BlockPos var2) {
      this.БЧ();
      if (this.Х.player != null && this.Х.level != null) {
         this.В.put(var1, new ChunkRevealAnimation.SectionState(var2.asLong(), this.Б(var2)));
      }
   }

   private void БЧ() {
      if (this.field_4 != this.Х.level) {
         this.В.clear();
         this.field_4 = this.Х.level;
      }
   }

   public void Б6() {
      this.В.clear();
      this.field_4 = null;
   }

   private Direction Б(BlockPos var1) {
      double var2 = this.Х.player.getX() - var1.getX() - ВЪ必;
      double var4 = this.Х.player.getZ() - var1.getZ() - ВЪщ;
      return Math.abs(var2) > Math.abs(var4) ? (var2 > 0.0 ? Direction.EAST : Direction.WEST) : (var4 > 0.0 ? Direction.SOUTH : Direction.NORTH);
   }

   public void Б(RenderSection var1) {
      this.Б(var1, var1.getRenderOrigin());
   }

   public static double Б(String var0, double var1) {
      double var3 = Math.clamp(var1, 0.0, 1.0);

      return switch (var0) {
         case ВЪГ -> Easings.QUAD_OUT.apply(var3);
         case ВЪл -> Easings.CUBIC_OUT.apply(var3);
         case ВЪэ -> Easings.QUART_OUT.apply(var3);
         case ВЪю -> Easings.QUINT_OUT.apply(var3);
         case ВЪЩ -> Easings.EXPO_OUT.apply(var3);
         case ВЪу -> Easings.SINE_OUT.apply(var3);
         case ВЪ2 -> Math.sqrt(1.0 - (var3 - 1.0) * (var3 - 1.0));
         case ВЪф -> Easings.BACK_OUT.apply(var3);
         case ВЪи -> Easings.BOUNCE_OUT.apply(var3);
         case ВЪ9 -> Easings.ELASTIC_OUT.apply(var3);
         default -> var3;
      };
   }

   public Vec3 Б(RenderSection var1, BetterMinecraft var2) {
      return this.Б(var1, var1.getRenderOrigin(), var2);
   }

   public Vec3 Б(Object var1, BlockPos var2, BetterMinecraft var3) {
      this.БЧ();
      if (var3.isEnabled() && var3.chunkAnimationSetting.getValue() && this.Х.player != null && this.Х.level != null) {
         ChunkRevealAnimation.SectionState var4 = this.В.get(var1);
         if (var4 == null || var4.ВЙ != var2.asLong()) {
            this.Б(var1, var2);
            var4 = this.В.get(var1);
         }

         long var5 = System.nanoTime();
         if (var4.Вк == 0L) {
            var4.Вк = var5;
            if (var3.chunkDirectionSetting.isSelected(ВЪв)) {
               var4.Х = this.Б(var2);
            }
         }

         double var7 = Math.min(1.0, (var5 - var4.Вк) / (var3.chunkAnimationDurationSetting.getValue().floatValue() * ВЪъ));
         if (var7 >= 1.0) {
            return Vec3.ZERO;
         }

         double var9 = 1.0 - Б(var3.chunkEasingSetting.getSelectedOption().getName(), var7);
         String var11 = var3.chunkDirectionSetting.getSelectedOption().getName();
         if (var11.equals(ВЪМ)) {
            var11 = var2.getY() < this.Х.level.getSeaLevel() ? ВЪН : ВЪн;
         }
         return switch (var11) {
            case ВЪЦ -> new Vec3(0.0, (this.Х.level.getMinY() + this.Х.level.getHeight() - var2.getY()) * var9, 0.0);
            case ВЪч, ВЪЭ -> new Vec3(-var4.Х.getStepX() * 200 * var9, 0.0, -var4.Х.getStepZ() * 200 * var9);
            default -> new Vec3(0.0, (this.Х.level.getMinY() - var2.getY()) * var9, 0.0);
         };
      } else {
         this.Б6();
         return Vec3.ZERO;
      }
   }

   private static final class SectionState {
      private final long ВЙ;
      private long Вк;
      private Direction Х;

      private SectionState(long var1, Direction var3) {
         this.ВЙ = var1;
         this.Х = var3;
      }
   }
}
