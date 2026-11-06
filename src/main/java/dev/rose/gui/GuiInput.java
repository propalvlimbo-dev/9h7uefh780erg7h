package dev.rose.gui;

import java.util.Objects;
import net.minecraft.client.Minecraft;


final class GuiInput {
   private final GuiContext ctx;
   private GuiContext.Target active;

   GuiInput(GuiContext ctx) {
      this.ctx = ctx;
   }

   boolean dragging() {
      return this.active != null;
   }

   GuiContext.Target press(float x, float y, boolean keepFocus) {
      GuiContext.Target target = this.ctx.targetAt(x, y);
      String id = target == null ? null : target.id();
      if (this.ctx.focusedOwner != null && !keepFocus && !Objects.equals(id, this.ctx.focusedTargetId)) {
         this.ctx.unfocus();
      }

      if (this.ctx.listeningKey != null && (id == null || !id.startsWith("key:"))) {
         this.ctx.listeningKey = null;
      }

      if (target != null) {
         this.active = target;
         this.ctx.activeId = id;
         target.interaction().press(x - target.originX(), y - target.originY());
      }

      return target;
   }

   void drag(float x, float y) {
      if (this.active != null) {
         this.active.interaction().drag(x - this.active.originX(), y - this.active.originY());
      }
   }

   void release(float x, float y) {
      if (this.active != null) {
         GuiContext.Target target = this.active;
         this.active = null;
         target.interaction().release(x - target.originX(), y - target.originY(), target.contains(x, y));
      }

      this.ctx.pressed = null;
      this.ctx.activeId = null;
   }

   boolean assignKey(int key) {
      if (this.ctx.listeningKey == null) {
         return false;
      }

      boolean clear = key == 256 || key == 259 || key == 261;
      this.ctx.listeningKey.set(clear ? -1 : key);
      this.ctx.listeningKey = null;
      return true;
   }

   boolean editText(int key, int modifiers) {
      TextInput input = this.ctx.focusedInput;
      if (input == null) {
         return false;
      }

      boolean control = (modifiers & 2) != 0;
      boolean changed = false;
      switch (key) {
         case 86:
            changed = control && input.insert(Minecraft.getInstance().keyboardHandler.getClipboard());
            break;
         case 256:
            this.ctx.unfocus();
            break;
         case 257:
         case 335:
            this.ctx.inputSubmitted();
            break;
         case 259:
            changed = input.backspace(control);
            break;
         case 261:
            changed = input.delete(control);
            break;
         case 262:
            input.right(control);
            break;
         case 263:
            input.left(control);
            break;
         case 268:
            input.home();
            break;
         case 269:
            input.end();
      }

      if (changed) {
         this.ctx.inputChanged();
      }

      return true;
   }

   boolean type(char character) {
      if (this.ctx.listeningKey != null) {
         return true;
      }

      if (this.ctx.focusedInput == null) {
         return false;
      }

      if (this.ctx.focusedInput.insert(String.valueOf(character))) {
         this.ctx.inputChanged();
      }

      return true;
   }
}
