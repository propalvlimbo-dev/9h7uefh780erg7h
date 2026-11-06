package dev.rose.setting;

public class GallerySetting<E extends Enum<E> & GallerySetting.Preview> extends ChoiceSetting<E> {
   public GallerySetting(String id, E defaultValue) {
      super(id, defaultValue);
   }

   public interface Preview {
      int primary();

      int secondary();

      int tertiary();
   }
}
