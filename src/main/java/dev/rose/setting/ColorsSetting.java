package dev.rose.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.rose.render.Theme;
import dev.rose.util.ColorUtil;
import java.util.Arrays;
import java.util.Locale;


public class ColorsSetting extends Setting<ColorsSetting.Value> {
   public ColorsSetting(String id, ColorsSetting.Mode mode, int first, int second) {
      super(id, new ColorsSetting.Value(mode, new int[]{first | 0xFF000000, second | 0xFF000000, ColorUtil.lerp(first, second, 0.5F) | 0xFF000000}, 2, 1.0F));
   }

   public int color(float position, float time) {
      ColorsSetting.Value value = this.get();

      return switch (value.mode) {
         case THEME -> cycle(new int[]{Theme.accent(), Theme.accent2(), Theme.accent()}, 3, position + time * 0.1F * value.speed);
         case SINGLE -> value.colors[0];
         case GRADIENT -> {
            float shift = position + time * 0.25F * value.speed;
            int[] stops = value.count == 3
               ? new int[]{value.colors[0], value.colors[1], value.colors[2], value.colors[0]}
               : new int[]{value.colors[0], value.colors[1], value.colors[0]};
            yield cycle(stops, stops.length, shift);
         }
         case RAINBOW -> {
            float[] hsb = ColorUtil.toHsb(value.colors[0]);
            float hue = position + time * 0.2F * value.speed;
            yield ColorUtil.hsb(hue - (float)Math.floor(hue), Math.max(0.55F, hsb[1]), Math.max(0.75F, hsb[2]), 255);
         }
      };
   }

   public int color(float time) {
      return this.color(0.0F, time);
   }

   private static int cycle(int[] stops, int length, float position) {
      float t = position - (float)Math.floor(position);
      float scaled = t * (length - 1);
      int index = Math.min(length - 2, (int)scaled);
      return ColorUtil.lerp(stops[index], stops[index + 1], scaled - index) | 0xFF000000;
   }

   public void setMode(ColorsSetting.Mode mode) {
      this.set(this.get().withMode(mode));
   }

   public void setColor(int index, int color) {
      this.set(this.get().withColor(index, color));
   }

   public void setCount(int count) {
      this.set(this.get().withCount(count));
   }

   public void setSpeed(float speed) {
      this.set(this.get().withSpeed(speed));
   }

   @Override
   public JsonElement toJson() {
      ColorsSetting.Value value = this.get();
      JsonObject json = new JsonObject();
      json.addProperty("mode", value.mode.name());
      JsonArray colors = new JsonArray();

      for (int color : value.colors) {
         colors.add(new JsonPrimitive(String.format(Locale.ROOT, "#%08X", color)));
      }

      json.add("colors", colors);
      json.addProperty("count", value.count);
      json.addProperty("speed", value.speed);
      return json;
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonObject()) {
         JsonObject object = json.getAsJsonObject();
         ColorsSetting.Value current = this.get();
         ColorsSetting.Mode mode = current.mode;
         if (object.has("mode")) {
            for (ColorsSetting.Mode candidate : ColorsSetting.Mode.values()) {
               if (candidate.name().equalsIgnoreCase(object.get("mode").getAsString())) {
                  mode = candidate;
               }
            }
         }

         int[] colors = (int[])current.colors.clone();
         if (object.has("colors") && object.get("colors").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("colors");

            for (int i = 0; i < Math.min(3, array.size()); i++) {
               try {
                  colors[i] = Integer.parseUnsignedInt(array.get(i).getAsString().replace("#", ""), 16) | 0xFF000000;
               } catch (RuntimeException var9) {
               }
            }
         }

         int count = object.has("count") ? object.get("count").getAsInt() : current.count;
         float speed = object.has("speed") ? object.get("speed").getAsFloat() : current.speed;
         this.set(new ColorsSetting.Value(mode, colors, count, speed));
      }
   }

   public enum Mode {
      THEME,
      SINGLE,
      GRADIENT,
      RAINBOW;
   }

   public static final class Value {
      private final ColorsSetting.Mode mode;
      private final int[] colors;
      private final int count;
      private final float speed;

      public Value(ColorsSetting.Mode mode, int[] colors, int count, float speed) {
         this.mode = mode;
         this.colors = Arrays.copyOf(colors, 3);
         this.count = Math.clamp(count, 2, 3);
         this.speed = Math.clamp(speed, 0.0F, 5.0F);
      }

      public ColorsSetting.Mode mode() {
         return this.mode;
      }

      public int color(int index) {
         return this.colors[Math.clamp(index, 0, 2)];
      }

      public int count() {
         return this.count;
      }

      public float speed() {
         return this.speed;
      }

      public ColorsSetting.Value withMode(ColorsSetting.Mode value) {
         return new ColorsSetting.Value(value, this.colors, this.count, this.speed);
      }

      public ColorsSetting.Value withColor(int index, int color) {
         int[] copy = (int[])this.colors.clone();
         copy[Math.clamp(index, 0, 2)] = color | 0xFF000000;
         return new ColorsSetting.Value(this.mode, copy, this.count, this.speed);
      }

      public ColorsSetting.Value withCount(int value) {
         return new ColorsSetting.Value(this.mode, this.colors, value, this.speed);
      }

      public ColorsSetting.Value withSpeed(float value) {
         return new ColorsSetting.Value(this.mode, this.colors, this.count, value);
      }

      @Override
      public boolean equals(Object other) {
         return other instanceof ColorsSetting.Value value
            && value.mode == this.mode
            && Arrays.equals(value.colors, this.colors)
            && value.count == this.count
            && Float.compare(value.speed, this.speed) == 0;
      }

      @Override
      public int hashCode() {
         return (this.mode.hashCode() * 31 + Arrays.hashCode(this.colors)) * 31 + this.count * 7 + Float.hashCode(this.speed);
      }
   }
}
