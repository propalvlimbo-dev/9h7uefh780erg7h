package settings.render;

import java.util.regex.Pattern;
import lombok.Generated;

public final class PlayerNamePatterns {
   private static final Pattern В = Pattern.compile(PlayerNamePatterns.сбъ);
   private static final Pattern Я = Pattern.compile(PlayerNamePatterns.сбМ, 66);
   private static final String сбв = "This is a utility class and cannot be instantiated";
   private static final String сбъ = "^\\w{3,16}$";
   private static final String сбМ = ".*(owner|admin|moder|helper|curator|support|staff|куратор|модер|админ|хелпер|поддержка|сотрудник|стаж[её]р).*";

   @Generated
   public static Pattern ж() {
      return Я;
   }

   @Generated
   public static Pattern 必() {
      return В;
   }

   @Generated
   private PlayerNamePatterns() {
      throw new UnsupportedOperationException(сбв);
   }
}
