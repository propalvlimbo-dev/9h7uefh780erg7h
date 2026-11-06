package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;

public class ActionSetting extends ValueSetting<Boolean> {
   @SerializedName("4")
   private Runnable action;
   @SerializedName("3")
   private boolean hideOtherSettings = true;

   public Runnable getAction() {
      return this.action;
   }

   public ActionSetting showOtherSettings() {
      this.hideOtherSettings = false;
      return this;
   }

   public boolean hidesOtherSettings() {
      return this.hideOtherSettings;
   }

   public ActionSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public ActionSetting onClick(Runnable action) {
      this.action = action;
      return this;
   }

   public ActionSetting(String name) {
      super(name, false);
   }
}
