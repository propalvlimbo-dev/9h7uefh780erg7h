package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.rose.sound.SoundLibrary;
import java.util.List;
import net.minecraft.client.resources.language.I18n;


public class SoundSetting extends Setting<String> {
   public SoundSetting(String id, String defaultSound) {
      super(id, defaultSound);
   }

   public List<String> options() {
      return SoundLibrary.options();
   }

   public static String label(String sound) {
      if (sound.startsWith("file:")) {
         return sound.substring(5);
      }

      String key = "rose.sound." + sound;
      return I18n.exists(key) ? I18n.get(key, new Object[0]) : sound;
   }

   public void preview(float volume) {
      SoundLibrary.play(this.get(), volume, 1.0F);
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json != null && json.isJsonPrimitive()) {
         this.set(json.getAsString());
      }
   }
}
