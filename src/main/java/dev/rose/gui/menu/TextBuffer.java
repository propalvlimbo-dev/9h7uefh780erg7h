package dev.rose.gui.menu;

import net.minecraft.client.Minecraft;


public final class TextBuffer {
   private final int maxLength;
   private String text = "";
   private int cursor;

   public TextBuffer(int maxLength) {
      this.maxLength = maxLength;
   }

   public String text() {
      return this.text;
   }

   public int cursor() {
      return this.cursor;
   }

   public boolean isEmpty() {
      return this.text.isEmpty();
   }

   public void set(String value) {
      this.text = value.length() > this.maxLength ? value.substring(0, this.maxLength) : value;
      this.cursor = this.text.length();
   }

   public void clear() {
      this.set("");
   }

   public boolean insert(String value) {
      StringBuilder clean = new StringBuilder();

      for (int i = 0; i < value.length(); i++) {
         char c = value.charAt(i);
         if (c >= ' ' && c != 127 && c != 167) {
            clean.append(c);
         }
      }

      int room = this.maxLength - this.text.length();
      if (!clean.isEmpty() && room > 0) {
         if (clean.length() > room) {
            clean.setLength(room);
         }

         this.text = this.text.substring(0, this.cursor) + clean + this.text.substring(this.cursor);
         this.cursor = this.cursor + clean.length();
         return true;
      } else {
         return false;
      }
   }

   public boolean key(int key, int modifiers) {
      boolean control = (modifiers & 2) != 0;
      switch (key) {
         case 65:
            if (control) {
               this.cursor = this.text.length();
            }
            break;
         case 86:
            return control && this.insert(Minecraft.getInstance().keyboardHandler.getClipboard());
         case 259:
            if (this.cursor == 0) {
               return false;
            }

            int from = control ? this.wordStart(this.cursor) : this.cursor - 1;
            this.text = this.text.substring(0, from) + this.text.substring(this.cursor);
            this.cursor = from;
            return true;
         case 261:
            if (this.cursor >= this.text.length()) {
               return false;
            }

            int to = control ? this.wordEnd(this.cursor) : this.cursor + 1;
            this.text = this.text.substring(0, this.cursor) + this.text.substring(to);
            return true;
         case 262:
            this.cursor = control ? this.wordEnd(this.cursor) : Math.min(this.text.length(), this.cursor + 1);
            break;
         case 263:
            this.cursor = control ? this.wordStart(this.cursor) : Math.max(0, this.cursor - 1);
            break;
         case 268:
            this.cursor = 0;
            break;
         case 269:
            this.cursor = this.text.length();
      }

      return false;
   }

   private int wordStart(int from) {
      int i = from;

      while (i > 0 && this.text.charAt(i - 1) == ' ') {
         i--;
      }

      while (i > 0 && this.text.charAt(i - 1) != ' ') {
         i--;
      }

      return i;
   }

   private int wordEnd(int from) {
      int i = from;

      while (i < this.text.length() && this.text.charAt(i) == ' ') {
         i++;
      }

      while (i < this.text.length() && this.text.charAt(i) != ' ') {
         i++;
      }

      return i;
   }
}
