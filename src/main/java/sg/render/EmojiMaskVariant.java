package sg.render;

import net.minecraft.resources.Identifier;

public enum EmojiMaskVariant {
   SMILING_SUNGLASSES("В очках", "smiling_face_with_sunglasses"),
   HEART_EYES("Влюблённый", "smiling_face_with_heart_eyes"),
   SMILING_EYES("Улыбка", "smiling_face_with_smiling_eyes"),
   TEARS_OF_JOY("Смех до слёз", "face_with_tears_of_joy"),
   STAR_STRUCK("Звёздные глаза", "star_struck"),
   NERD("Ботаник", "nerd_face"),
   BLOWING_KISS("Поцелуй", "face_blowing_a_kiss"),
   SAVORING_FOOD("Вкусняшка", "face_savoring_food"),
   FLUSHED("Смущение", "flushed_face"),
   PLEADING("Умоляющий", "pleading_face"),
   PENSIVE("Грусть", "pensive_face"),
   CRYING("Слеза", "crying_face"),
   LOUDLY_CRYING("Рёв", "loudly_crying_face"),
   ENRAGED("Ярость", "enraged_face"),
   SCREAMING("Страх", "face_screaming_in_fear"),
   VOMITING("Тошнота", "face_vomiting"),
   SHELF("Shelf", "shelf"),
   MELLSTROY("MellStroy", "mellstroy");

   private final @com.google.gson.annotations.SerializedName("сl")  String displayName;
   private final @com.google.gson.annotations.SerializedName("в")  Identifier textureId;

   EmojiMaskVariant(String displayName, String textureName) {
      this.displayName = displayName;
      this.textureId = Identifier.fromNamespaceAndPath("rose", "textures/masks/" + textureName + "_mask.png");
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public Identifier getTextureId() {
      return this.textureId;
   }
}
