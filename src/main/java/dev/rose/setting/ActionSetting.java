package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.client.resources.language.I18n;


public class ActionSetting extends Setting<Boolean> {
   private final int icon;
   private final Runnable action;

   public ActionSetting(String id, int icon, Runnable action) {
      super(id, false);
      this.icon = icon;
      this.action = action;
   }

   public int icon() {
      return this.icon;
   }

   public void run() {
      this.action.run();
   }

   public String buttonLabel() {
      return I18n.get(this.translationKey() + ".button", new Object[0]);
   }

   @Override
   public boolean isDefault() {
      return true;
   }

   @Override
   public JsonElement toJson() {
      return JsonNull.INSTANCE;
   }

   @Override
   public void fromJson(JsonElement json) {
   }
}
