package sg.modules.impl.init;

import java.util.function.Consumer;

public record WorldEditSelection(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
   public WorldEditSelection(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      if (coordinate(minX)
         && coordinate(minY)
         && coordinate(minZ)
         && coordinate(maxX)
         && coordinate(maxY)
         && coordinate(maxZ)
         && !(minX > maxX)
         && !(minY > maxY)
         && !(minZ > maxZ)) {
         this.minX = minX;
         this.minY = minY;
         this.minZ = minZ;
         this.maxX = maxX;
         this.maxY = maxY;
         this.maxZ = maxZ;
      } else {
         throw new IllegalArgumentException("Координаты выделения выходят за допустимый диапазон.");
      }
   }

   public String positionCommand(int var1, boolean var2) {
      if (var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Неверный угол выделения.");
      } else {
         return (var2 ? "//" : "//pos") + var1 + " " + this.coordinates(var1 == 2, var2 ? "," : " ");
      }
   }

   private String coordinates(boolean var1, String var2) {
      return (int)(var1 ? this.maxX : this.minX) + var2 + (int)(var1 ? this.maxY : this.minY) + var2 + (int)(var1 ? this.maxZ : this.minZ);
   }

   public void send(Consumer<String> var1) {
      var1.accept(this.positionCommand(1, false));
      var1.accept(this.positionCommand(2, false));
   }

   private static boolean coordinate(double var0) {
      return Double.isFinite(var0) && var0 >= -2.1474836E9F && var0 <= 2.147483647E9;
   }

   public static WorldEditSelection read(String[] var0) {
      if (var0 != null && var0.length >= 6) {
         return new WorldEditSelection(
            Double.parseDouble(var0[0]),
            Double.parseDouble(var0[1]),
            Double.parseDouble(var0[2]),
            Double.parseDouble(var0[3]),
            Double.parseDouble(var0[4]),
            Double.parseDouble(var0[5])
         );
      } else {
         throw new IllegalArgumentException("Нет координат выделения.");
      }
   }

   public String xPositionCommand(int var1) {
      if (var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Неверный угол выделения.");
      } else {
         return "//pos" + var1 + " " + (int)(var1 == 1 ? this.maxX : this.minX);
      }
   }

   public static WorldEditSelection centered(double var0, double var2, double var4, double var6, double var8) {
      if (Double.isFinite(var6) && Double.isFinite(var8) && !(var6 < 0.0) && !(var8 < 0.0)) {
         return new WorldEditSelection(var0 - var6, var2 - var8, var4 - var6, var0 + var6, var2 + var8, var4 + var6);
      } else {
         throw new IllegalArgumentException("Радиус должен быть положительным числом.");
      }
   }

   public String serialize() {
      return this.minX + ":" + this.minY + ":" + this.minZ + ":" + this.maxX + ":" + this.maxY + ":" + this.maxZ;
   }

   public void sendX(Consumer<String> var1) {
      var1.accept(this.xPositionCommand(1));
      var1.accept(this.xPositionCommand(2));
   }

   public String partyPositionCommand(int var1) {
      if (var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Неверный угол выделения.");
      } else {
         return "//pos" + var1 + " " + this.coordinates(var1 == 1, ",");
      }
   }
}
