package sg.util.time;

import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import sg.render.WorldProjection;

public class TextInputState {
   private final TextInputState.InputType В;
   private Runnable Ю = null;
   private Runnable Ч = null;
   private boolean Аи = false;
   private boolean А9 = false;
   private String Хе = "";
   private static final String ХЫр = ".";
   private static final String ХЫ6 = ",";

   @Generated
   public void йи(boolean var1) {
      this.Аи = var1;
   }

   @Generated
   public TextInputState.InputType method_8() {
      return this.В;
   }

   @Generated
   public Runnable ь() {
      return this.Ч;
   }

   public TextInputState(TextInputState.InputType var1) {
      this.В = var1;
   }

   @Generated
   public String ш西() {
      return this.Хе;
   }

   public float йО(float var1) {
      try {
         return Float.parseFloat(this.Хе);
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   public void йУ(int var1) {
      if (this.Аи) {
         if (var1 == 341) {
            this.А9 = true;
         } else if (!this.Хе.isEmpty() && var1 == 259) {
            this.Хе = this.Хе.substring(0, this.Хе.length() - 1);
         } else if (var1 == 257 || var1 == 256) {
            this.Аи = false;
            this.method_3_();
         }
      }
   }

   public void ь(Runnable var1) {
      this.Ю = var1;
   }

   @Generated
   public Runnable method_5() {
      return this.Ю;
   }

   @Generated
   public void йя(String var1) {
      this.Хе = var1;
   }

   public void method_8(float var1, float var2, float var3, float var4, float var5, float var6, String var7, int var8) {
      if (WorldProjection.containsPoint(var1, var2, var3, var4, var5, var6) && var8 == 0) {
         this.Хе = var7;
         this.Аи = true;
         this.Ч = this.Ю;
      } else {
         this.method_3_();
         this.Аи = false;
      }
   }

   @Generated
   public boolean БЦ() {
      return this.Аи;
   }

   public void й衣(String var1) {
      if (this.Аи) {
         if (this.В == TextInputState.InputType.NUMBER) {
            if (StringUtils.isNumeric(var1)) {
               this.Хе = this.Хе + var1;
            } else if (var1.equals(ХЫр) || var1.equals(ХЫ6)) {
               this.Хе = this.Хе + ".";
            }
         } else if (this.В == TextInputState.InputType.STRING) {
            this.Хе = this.Хе + var1;
         }
      }
   }

   @Generated
   public boolean Бу() {
      return this.А9;
   }

   public void йЯ(int var1) {
      if (this.Аи) {
         if (var1 == 341) {
            this.А9 = false;
         }
      }
   }

   private void method_3_() {
      if (this.Ч != null) {
         this.Ч.run();
         this.Ч = null;
      }
   }

   public enum InputType {
      NUMBER,
      STRING;
   }
}
