package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.resources.language.I18n;


public class KeySetting extends Setting<Integer> {
   public static final int NONE = -1;

   public KeySetting(String id, int defaultKey) {
      super(id, defaultKey);
   }

   public boolean isBound() {
      return this.get() != -1;
   }

   public String keyName() {
      return !this.isBound()
         ? I18n.get("rose.key.none", new Object[0])
         : InputConstants.Type.KEYSYM.getOrCreate(this.get()).getDisplayName().getString();
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
         this.set(json.getAsInt());
      }
   }
}
