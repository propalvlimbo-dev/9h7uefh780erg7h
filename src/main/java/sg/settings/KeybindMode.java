package sg.settings;

import lombok.Generated;

public enum KeybindMode {
   TOGGLE("Toggle"),
   HOLD("Hold");

   private final @com.google.gson.annotations.SerializedName("сП")  String displayName;

   public static KeybindMode fromDisplayName(String displayName) {
      return displayName.equals("Hold") ? HOLD : TOGGLE;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   @Generated
   KeybindMode(String displayName) {
      this.displayName = displayName;
   }
}
