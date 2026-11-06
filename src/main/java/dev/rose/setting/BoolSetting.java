package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;


public class BoolSetting extends Setting<Boolean> {
   public BoolSetting(String id, boolean defaultValue) {
      super(id, defaultValue);
   }

   public boolean isOn() {
      return this.get();
   }

   public void toggle() {
      this.set(!this.get());
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isBoolean()) {
         this.set(json.getAsBoolean());
      }
   }
}
