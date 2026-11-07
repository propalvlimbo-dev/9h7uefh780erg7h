package sg.render;

import net.minecraft.resources.Identifier;

public enum WingSelection {
   ANGEL("Ангел", "angel"),
   PARROT("Попугай", "parrot"),
   BAT("Летучая мышь", "bat"),
   DRAGON("Дракон", "dragon"),
   EVIL("Демон", "evil"),
   FIRE("Огонь", "fire"),
   METALLIC("Металл", "metallic"),
   BLUE_BUTTERFLY("Синяя бабочка", "blue_butterfly"),
   MONARCH("Бабочка монарх", "monarch_butterfly"),
   FAIRY("Фея", "fairy"),
   SLIME("Слизень", "slime"),
   KANEKI_KAGUNE("Кагуне Канеки", "kaneki_kagune"),
   ROSE("Роза", "rose");

   private final @com.google.gson.annotations.SerializedName("Я2")  String displayName;
   private final @com.google.gson.annotations.SerializedName("П")  Identifier textureId;

   WingSelection(String displayName, String textureName) {
      this.displayName = displayName;
      this.textureId = Identifier.fromNamespaceAndPath("rose", "textures/wings/" + textureName + "_wings.png");
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public Identifier getTextureId() {
      return this.textureId;
   }

   public AnimatedWingVariant getAnimatedVariant() {
      String variantName = this.name();
      if (this == MONARCH) {
         variantName = "MONARCH_BUTTERFLY";
      }

      return AnimatedWingVariant.valueOf(variantName);
   }
}
