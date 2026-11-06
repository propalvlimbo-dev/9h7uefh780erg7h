package sg.render.pipeline;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import sg.ec.MinecraftAccess;

public final class WardenFarmArea implements MinecraftAccess {
   public static final int Ях = -2070;
   public static final int ЯУ = -1921;
   public static final int ЯЗ = -2076;
   public static final int ЯЕ = -1929;
   public static final int ЯП = -60;
   public static final int Я西 = -35;
   private static final String иЕ = "minecraft:overworld";
   private static final double иП = -2070.0;
   private static final double и西 = -1921.0;
   private static final double ид = -2076.0;
   private static final double иБ = -1929.0;
   private static final double иС = -2070.0;
   private static final double из = -2076.0;
   private static final double иц = -1921.0;
   private static final double ия = -1929.0;
   private static final double field_9с = 10.0;
   private static final double field_9В = 10.0;
   private static final double field_9Я = 10.0;
   private static final double field_9Х = 10.0;

   public static boolean о(BlockPos var0) {
      return var0.getY() >= -60 && var0.getY() <= -35 && var0.getX() >= -2070 && var0.getX() <= -1921 && var0.getZ() >= -2076 && var0.getZ() <= -1929;
   }

   public static boolean Ап() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && Л(MINECRAFT_CLIENT.level) && А(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getZ());
   }

   public static boolean А(double var0, double var2) {
      return var0 >= иП && var0 <= и西 && var2 >= ид && var2 <= иБ;
   }

   public static int Л(int var0, AABB var1) {
      return var1 == null ? var0 : (int)Math.max(var1.minX + field_9с, Math.min(var1.maxX - field_9В, var0));
   }

   public static int А(int var0, AABB var1) {
      return var1 == null ? var0 : (int)Math.max(var1.minZ + field_9Я, Math.min(var1.maxZ - field_9Х, var0));
   }

   private WardenFarmArea() {
   }

   public static AABB Л(int var0) {
      return new AABB(иС, var0, из, иц, var0, ия);
   }

   public static boolean Л(Level var0) {
      return var0 != null && var0.dimension().identifier().toString().equals(иЕ);
   }
}
