package sg.render;

public enum CosmeticCategory {
   HATS,
   MASKS,
   WINGS,
   BACKPACKS,
   PETS,
   EARS,
   TAILS,
   PAWS,
   STOCKINGS,
   HEADS;

   public static CosmeticCategory fromConfigKey(String configKey) {
      return switch (configKey) {
         case "hats" -> HATS;
         case "masks" -> MASKS;
         case "wings" -> WINGS;
         case "backpacks" -> BACKPACKS;
         case "pets" -> PETS;
         case "ears" -> EARS;
         case "tails" -> TAILS;
         case "paws" -> PAWS;
         case "dresses", "stockings" -> STOCKINGS;
         case "heads" -> HEADS;
         default -> throw new IllegalArgumentException(configKey);
      };
   }

   public String getConfigKey() {
      return this.name().toLowerCase();
   }

   public boolean isFurryCategory() {
      return this == EARS || this == TAILS || this == PAWS || this == STOCKINGS || this == HEADS;
   }
}
