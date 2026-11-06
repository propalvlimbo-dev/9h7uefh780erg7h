package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;


public class FileChoiceSetting extends Setting<String> {
   private final Supplier<List<Path>> files;

   public FileChoiceSetting(String id, Supplier<List<Path>> files) {
      super(id, "");
      this.files = files;
   }

   public List<Path> files() {
      return this.files.get();
   }

   public Path selected() {
      List<Path> available = this.files();

      for (Path file : available) {
         if (file.getFileName().toString().equals(this.get())) {
            return file;
         }
      }

      return available.isEmpty() ? null : available.getFirst();
   }

   protected String sanitize(String newValue) {
      return newValue == null ? "" : newValue;
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
