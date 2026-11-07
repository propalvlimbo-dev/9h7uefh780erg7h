package sg.render;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.Identifier;
import sg.ec.WingModel;
import sg.ec.AllayWingModel;
import sg.ec.InsectoidWingModel;
import sg.ec.RoseWingModel;

public enum AnimatedWingVariant {
   ANGEL("Ангел", "angel", AnimatedWingVariant.WingModelFamily.AVIAN),
   PARROT("Попугай", "parrot", AnimatedWingVariant.WingModelFamily.AVIAN),
   BAT("Летучая мышь", "bat", AnimatedWingVariant.WingModelFamily.AVIAN),
   DRAGON("Дракон", "dragon", AnimatedWingVariant.WingModelFamily.AVIAN),
   EVIL("Демон", "evil", AnimatedWingVariant.WingModelFamily.AVIAN),
   FIRE("Огонь", "fire", AnimatedWingVariant.WingModelFamily.AVIAN),
   METALLIC("Металл", "metallic", AnimatedWingVariant.WingModelFamily.AVIAN),
   GOD("Бог", "god", AnimatedWingVariant.WingModelFamily.AVIAN),
   BLUE_BUTTERFLY("Синяя бабочка", "blue_butterfly", AnimatedWingVariant.WingModelFamily.INSECTOID),
   MONARCH_BUTTERFLY("Бабочка монарх", "monarch_butterfly", AnimatedWingVariant.WingModelFamily.INSECTOID),
   FAIRY("Фея", "fairy", AnimatedWingVariant.WingModelFamily.INSECTOID),
   SLIME("Слизень", "slime", AnimatedWingVariant.WingModelFamily.INSECTOID),
   KANEKI_KAGUNE("Кагуне Канеки", "kaneki_kagune", AnimatedWingVariant.WingModelFamily.KAGUNE),
   ROSE("Роза", "rose", AnimatedWingVariant.WingModelFamily.ROSE);

   private static WingModel cachedAvianModel;
   private static WingModel cachedInsectoidModel;
   private static WingModel cachedKaguneModel;
   private static WingModel cachedAllayModel;
   private static WingModel cachedRoseModel;
   private final @com.google.gson.annotations.SerializedName("Х弟")  String displayName;
   private final @com.google.gson.annotations.SerializedName("сД")  Identifier textureId;
   @SerializedName("В")
   private final AnimatedWingVariant.WingModelFamily modelFamily;

   AnimatedWingVariant(String displayName, String textureName, AnimatedWingVariant.WingModelFamily modelFamily) {
      this.displayName = displayName;
      this.textureId = Identifier.fromNamespaceAndPath("rose", "textures/wings/" + textureName + "_wings.png");
      this.modelFamily = modelFamily;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public Identifier getTextureId() {
      return this.textureId;
   }

   public WingAnimator createAnimator() {
      return switch (this.modelFamily) {
         case AVIAN, ALLAY, ROSE -> new AvianWingAnimator();
         case INSECTOID, KAGUNE -> new SimpleWingAnimator();
      };
   }

   public WingModel getModel() {
      return switch (this.modelFamily) {
         case AVIAN -> {
            if (cachedAvianModel == null) {
               cachedAvianModel = new AvianWingModel();
            }

            yield cachedAvianModel;
         }
         case INSECTOID -> {
            if (cachedInsectoidModel == null) {
               cachedInsectoidModel = new InsectoidWingModel();
            }

            yield cachedInsectoidModel;
         }
         case KAGUNE -> {
            if (cachedKaguneModel == null) {
               cachedKaguneModel = new KaguneWingModel();
            }

            yield cachedKaguneModel;
         }
         case ALLAY -> {
            if (cachedAllayModel == null) {
               cachedAllayModel = new AllayWingModel();
            }

            yield cachedAllayModel;
         }
         case ROSE -> {
            if (cachedRoseModel == null) {
               cachedRoseModel = new RoseWingModel();
            }

            yield cachedRoseModel;
         }
      };
   }

   public static String[] getDisplayNames() {
      AnimatedWingVariant[] variants = values();
      String[] displayNames = new String[variants.length];

      for (int variantIndex = 0; variantIndex < variants.length; variantIndex++) {
         displayNames[variantIndex] = variants[variantIndex].displayName;
      }

      return displayNames;
   }

   public static AnimatedWingVariant fromDisplayName(String displayName) {
      for (AnimatedWingVariant variant : values()) {
         if (variant.displayName.equals(displayName)) {
            return variant;
         }
      }

      return ANGEL;
   }

   private enum WingModelFamily {
      AVIAN,
      INSECTOID,
      KAGUNE,
      ALLAY,
      ROSE;
   }
}
