package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;


public class TextSetting extends Setting<String> {
   private final int maxLength;

   public TextSetting(String id, String defaultValue, int maxLength) {
      super(id, defaultValue);
      this.maxLength = maxLength;
   }

   public int maxLength() {
      return this.maxLength;
   }

   protected String sanitize(String newValue) {
      if (newValue == null) {
         return "";
      }

      String line = newValue.replace('\n', ' ').replace('\r', ' ');
      return line.length() > this.maxLength ? line.substring(0, this.maxLength) : line;
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
         this.set(json.getAsString());
      }
   }
}
