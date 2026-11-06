package sg.render;

public enum BackpackColorVariant {
   LEATHER("Кожаный", 10511680),
   WHITE("Белый", 16383998),
   ORANGE("Оранжевый", 16351261),
   MAGENTA("Пурпурный", 13061821),
   LIGHT_BLUE("Голубой", 3847130),
   YELLOW("Жёлтый", 16701501),
   LIME("Лаймовый", 8439583),
   PINK("Розовый", 15961002),
   GRAY("Серый", 4673362),
   LIGHT_GRAY("Светло-серый", 10329495),
   CYAN("Бирюзовый", 1481884),
   PURPLE("Фиолетовый", 8991416),
   BLUE("Синий", 3949738),
   BROWN("Коричневый", 8606770),
   GREEN("Зелёный", 6192150),
   RED("Красный", 11546150),
   BLACK("Чёрный", 1908001);

   private final @com.google.gson.annotations.SerializedName("сб")  String displayName;
   private final @com.google.gson.annotations.SerializedName("В0")  int colorRgb;

   BackpackColorVariant(String displayName, int colorRgb) {
      this.displayName = displayName;
      this.colorRgb = colorRgb;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public float getRedComponent() {
      return (this.colorRgb >> 16 & 0xFF) / 255.0F;
   }

   public float getGreenComponent() {
      return (this.colorRgb >> 8 & 0xFF) / 255.0F;
   }

   public float getBlueComponent() {
      return (this.colorRgb & 0xFF) / 255.0F;
   }
}
