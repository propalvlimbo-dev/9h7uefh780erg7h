package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;

public class FriendEntry {
   @SerializedName("0")
   private final String field_0;
   private final String х;

   @Generated
   public String lз() {
      return this.field_0;
   }

   public FriendEntry(String var1, String var2) {
      this.field_0 = var1;
      this.х = var2 != null && !var2.isBlank() ? var2 : null;
   }

   public boolean lк() {
      return this.х != null;
   }

   public FriendEntry(String var1) {
      this(var1, null);
   }

   @Generated
   public String l3() {
      return this.х;
   }
}
