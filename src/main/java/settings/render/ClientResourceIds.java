package settings.render;

import lombok.Generated;
import net.minecraft.resources.Identifier;

public final class ClientResourceIds {
   private static final String CORE_NAMESPACE = "rose";
   private static final String FONT_NAMESPACE = "rose";
   private static final String IMAGE_NAMESPACE = "rose";
   private static final String UTILITY_INSTANTIATION_MESSAGE = "This is a utility class and cannot be instantiated";

   public static Identifier getFontId(String fontPath) {
      return Identifier.fromNamespaceAndPath(FONT_NAMESPACE, "fonts/" + fontPath);
   }

   @Generated
   private ClientResourceIds() {
      throw new UnsupportedOperationException(UTILITY_INSTANTIATION_MESSAGE);
   }

   public static Identifier getImageId(String imageName) {
      return Identifier.fromNamespaceAndPath(IMAGE_NAMESPACE, "images/" + imageName + ".png");
   }

   public static Identifier getCoreId(String corePath) {
      return Identifier.fromNamespaceAndPath(CORE_NAMESPACE, "core/" + corePath);
   }
}
