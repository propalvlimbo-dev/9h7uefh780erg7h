package sg.render;

public enum CosmeticAttachmentPoint {
   HEAD,
   BODY,
   ARMS,
   LEGS,
   PAWS,
   STOCKINGS,
   WORLD;

   public static CosmeticAttachmentPoint fromKey(String attachmentKey) {
      return switch (attachmentKey) {
         case "head" -> HEAD;
         case "body" -> BODY;
         case "arms" -> ARMS;
         case "legs" -> LEGS;
         case "paws" -> PAWS;
         case "stockings" -> STOCKINGS;
         case "world" -> WORLD;
         default -> throw new IllegalArgumentException(attachmentKey);
      };
   }

   public static CosmeticAttachmentPoint defaultForCategory(CosmeticCategory category) {
      return switch (category) {
         case HATS, MASKS, EARS, HEADS -> HEAD;
         case PAWS -> PAWS;
         case STOCKINGS -> STOCKINGS;
         case WINGS, BACKPACKS, PETS, TAILS -> BODY;
      };
   }

   public boolean isWorldAttachment() {
      return this == WORLD;
   }
}
