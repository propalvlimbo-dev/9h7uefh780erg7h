package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import sg.modules.impl.init.SoundPadManager;

public final class SoundPadConfig extends JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("В")  SoundPadManager soundPadManager;
   private static final String CONFIG_DIRECTORY_PATH = "rose/configs";
   private static final String CONFIG_NAME = "soundpad";
   private static final String WRITE_HEAR_LOCAL_KEY = "HearLocal";
   private static final String WRITE_LOCAL_VOLUME_KEY = "LocalVolume";
   private static final String WRITE_BINDINGS_KEY = "Bindings";
   private static final String READ_HEAR_LOCAL_PRESENT_KEY = "HearLocal";
   private static final String READ_HEAR_LOCAL_VALUE_KEY = "HearLocal";
   private static final String READ_LOCAL_VOLUME_PRESENT_KEY = "LocalVolume";
   private static final String READ_LOCAL_VOLUME_VALUE_KEY = "LocalVolume";
   private static final float DEFAULT_LOCAL_VOLUME = 0.8F;
   private static final String READ_BINDINGS_PRESENT_KEY = "Bindings";
   private static final String READ_BINDINGS_OBJECT_CHECK_KEY = "Bindings";
   private static final String READ_BINDINGS_OBJECT_VALUE_KEY = "Bindings";

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();
      configJson.addProperty(WRITE_HEAR_LOCAL_KEY, this.soundPadManager.ЙЫ());
      configJson.addProperty(WRITE_LOCAL_VOLUME_KEY, this.soundPadManager.б4());
      JsonObject bindingsJson = new JsonObject();

      for (Entry bindingEntry : this.soundPadManager.Х().entrySet()) {
         bindingsJson.addProperty((String)bindingEntry.getKey(), (Number)bindingEntry.getValue());
      }

      configJson.add(WRITE_BINDINGS_KEY, bindingsJson);
      this.writeJson(configJson);
   }

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (!fileContents.isBlank()) {
         JsonObject configJson = new JsonParser().parse(fileContents).getAsJsonObject();
         boolean hearLocal = !configJson.has(READ_HEAR_LOCAL_PRESENT_KEY) || configJson.get(READ_HEAR_LOCAL_VALUE_KEY).getAsBoolean();
         float localVolume = configJson.has(READ_LOCAL_VOLUME_PRESENT_KEY) ? configJson.get(READ_LOCAL_VOLUME_VALUE_KEY).getAsFloat() : DEFAULT_LOCAL_VOLUME;
         LinkedHashMap bindings = new LinkedHashMap();
         if (configJson.has(READ_BINDINGS_PRESENT_KEY) && configJson.get(READ_BINDINGS_OBJECT_CHECK_KEY).isJsonObject()) {
            for (Entry bindingEntry : configJson.getAsJsonObject(READ_BINDINGS_OBJECT_VALUE_KEY).entrySet()) {
               bindings.put((String)bindingEntry.getKey(), ((JsonElement)bindingEntry.getValue()).getAsInt());
            }
         }

         this.soundPadManager.н(bindings, hearLocal, localVolume);
         this.soundPadManager.method_12(false);
      }
   }

   public SoundPadConfig(SoundPadManager soundPadManager) {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
      this.soundPadManager = soundPadManager;
   }
}
