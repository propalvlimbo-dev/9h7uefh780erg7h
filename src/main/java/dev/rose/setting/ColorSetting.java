package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.rose.config.ConfigManager;
import dev.rose.util.ColorUtil;
import java.util.Locale;


public class ColorSetting extends Setting<Integer> {
   private static final long RAINBOW_PERIOD_MS = 6000L;
   private final boolean alpha;
   private boolean rainbow;

   public ColorSetting(String id, int defaultColor, boolean alpha) {
      super(id, defaultColor);
      this.alpha = alpha;
   }

   public boolean hasAlpha() {
      return this.alpha;
   }

   public boolean isRainbow() {
      return this.rainbow;
   }

   public void setRainbow(boolean rainbow) {
      if (this.rainbow != rainbow) {
         this.rainbow = rainbow;
         ConfigManager.markDirty();
      }
   }

   @Override
   public void reset() {
      this.setRainbow(false);
      super.reset();
   }

   @Override
   public boolean isDefault() {
      return super.isDefault() && !this.rainbow;
   }

   public int color() {
      int base = this.get();
      if (!this.rainbow) {
         return base;
      }

      float[] hsb = ColorUtil.toHsb(base);
      float shift = (float)(System.currentTimeMillis() % 6000L) / 6000.0F;
      return ColorUtil.hsb(hsb[0] + shift, Math.max(hsb[1], 0.5F), Math.max(hsb[2], 0.6F), ColorUtil.alpha(base));
   }

   protected Integer sanitize(Integer newValue) {
      return newValue == null ? this.defaultValue() : this.alpha ? newValue : newValue | 0xFF000000;
   }

   @Override
   public JsonElement toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("color", String.format(Locale.ROOT, "#%08X", this.get()));
      json.addProperty("rainbow", this.rainbow);
      return json;
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonObject()) {
         JsonObject object = json.getAsJsonObject();
         if (object.has("color") && object.get("color").isJsonPrimitive()) {
            String hex = object.get("color").getAsString().replace("#", "");

            try {
               this.set(Integer.parseUnsignedInt(hex, 16));
            } catch (NumberFormatException var5) {
            }
         }

         if (object.has("rainbow") && object.get("rainbow").isJsonPrimitive()) {
            this.rainbow = object.get("rainbow").getAsBoolean();
         }
      }
   }
}
