package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;


public class ChoiceSetting<E extends Enum<E>> extends Setting<E> {
   private final E[] options;

   public ChoiceSetting(String id, E defaultValue) {
      super(id, defaultValue);
      this.options = defaultValue.getDeclaringClass().getEnumConstants();
   }

   public E[] options() {
      return (E[])((Enum[])this.options.clone());
   }

   public String label(E option) {
      return I18n.get(this.translationKey() + "." + option.name().toLowerCase(Locale.ROOT), new Object[0]);
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get().name());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
         for (E option : this.options) {
            if (option.name().equalsIgnoreCase(json.getAsString())) {
               this.set(option);
               return;
            }
         }
      }
   }
}
