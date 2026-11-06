package dev.rose.gui;

final class TextInput {
   private final int maxLength;
   private String text = "";
   private int cursor;

   TextInput(int maxLength) {
      this.maxLength = maxLength;
   }

   String text() {
      return this.text;
   }

   int cursor() {
      return this.cursor;
   }

   boolean isEmpty() {
      return this.text.isEmpty();
   }

   void set(String value) {
      this.text = value.length() > this.maxLength ? value.substring(0, this.maxLength) : value;
      this.cursor = this.text.length();
   }

   void clear() {
      this.set("");
   }

   boolean insert(String value) {
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

   boolean backspace(boolean word) {
      if (this.cursor == 0) {
         return false;
      }

      int from = word ? this.wordStart(this.cursor) : this.cursor - 1;
      this.text = this.text.substring(0, from) + this.text.substring(this.cursor);
      this.cursor = from;
      return true;
   }

   boolean delete(boolean word) {
      if (this.cursor >= this.text.length()) {
         return false;
      }

      int to = word ? this.wordEnd(this.cursor) : this.cursor + 1;
      this.text = this.text.substring(0, this.cursor) + this.text.substring(to);
      return true;
   }

   void left(boolean word) {
      this.cursor = word ? this.wordStart(this.cursor) : Math.max(0, this.cursor - 1);
   }

   void right(boolean word) {
      this.cursor = word ? this.wordEnd(this.cursor) : Math.min(this.text.length(), this.cursor + 1);
   }

   void home() {
      this.cursor = 0;
   }

   void end() {
      this.cursor = this.text.length();
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
