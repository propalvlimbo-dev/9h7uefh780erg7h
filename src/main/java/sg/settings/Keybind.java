package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Generated;

public class Keybind {
   private static final AtomicLong changeRevision = new AtomicLong();
   @SerializedName("В")
   private KeybindMode mode;
   private @com.google.gson.annotations.SerializedName("еА")  int keyCode;

   public void setKeyCode(int keyCode) {
      if (this.keyCode != keyCode) {
         this.keyCode = keyCode;
         changeRevision.incrementAndGet();
      }
   }

   public static long getChangeRevision() {
      return changeRevision.get();
   }

   @Generated
   public int getKeyCode() {
      return this.keyCode;
   }

   @Generated
   public Keybind(KeybindMode mode, int keyCode) {
      this.mode = mode;
      this.keyCode = keyCode;
   }

   @Generated
   public void setMode(KeybindMode mode) {
      this.mode = mode;
   }

   @Generated
   public KeybindMode getMode() {
      return this.mode;
   }
}
