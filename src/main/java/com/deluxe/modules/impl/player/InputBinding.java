package com.deluxe.modules.impl.player;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class InputBinding {
   public static final InputBinding INSTANCE = new InputBinding(InputBinding.BindingType.NONE, -1);
   private final InputBinding.BindingType type;
   private final int code;

   public boolean isBound() {
      return this.type != InputBinding.BindingType.NONE && this.code != -1;
   }

   public static InputBinding mouse(int code) {
      return new InputBinding(InputBinding.BindingType.MOUSE, code);
   }

   private InputBinding(InputBinding.BindingType type, int code) {
      this.type = type;
      this.code = code;
   }

   public InputBinding.BindingType getType() {
      return this.type;
   }

   public boolean isPressed(long windowHandle, boolean allowInScreen) {
      if (!this.isBound()) {
         return false;
      } else if (!allowInScreen && Minecraft.getInstance().gui.screen() != null) {
         return false;
      } else if (GLFW.glfwGetWindowAttrib(windowHandle, (131073)) == 0) {
         return false;
      } else if (this.type == InputBinding.BindingType.MOUSE) {
         return GLFW.glfwGetMouseButton(windowHandle, this.code) == 1;
      } else {
         return GLFW.glfwGetKey(windowHandle, this.code) == 1;
      }
   }

   public String getDisplayName() {
      if (!this.isBound()) {
         return ("NONE");
      }

      if (this.type == InputBinding.BindingType.MOUSE) {
         return "M" + (this.code + 1);
      }

      if (this.code > 32 && this.code <= 96) {
         return String.valueOf((char)this.code);
      }

      if (this.code >= 290 && this.code <= 314) {
         return "F" + (this.code - 290 + 1);
      }

      if (this.code >= 320 && this.code <= 329) {
         return "NUM" + (this.code - 320);
      }

      return switch (this.code) {
         case 32 -> ("SPACE");
         case 256 -> ("ESC");
         case 257 -> ("ENTER");
         case 258 -> ("TAB");
         case 259 -> ("BACKSPACE");
         case 260 -> ("INSERT");
         case 261 -> ("DELETE");
         case 262 -> ("RIGHT");
         case 263 -> ("LEFT");
         case 264 -> ("DOWN");
         case 265 -> ("UP");
         case 266 -> ("PGUP");
         case 267 -> ("PGDN");
         case 268 -> ("HOME");
         case 269 -> ("END");
         case 280 -> ("CAPSLOCK");
         case 281 -> ("SCROLLLOCK");
         case 282 -> ("NUMLOCK");
         case 283 -> ("PRTSC");
         case 284 -> ("PAUSE");
         case 330 -> ("NUM.");
         case 331 -> ("NUM/");
         case 332 -> ("NUM*");
         case 333 -> ("NUM-");
         case 334 -> ("NUM+");
         case 335 -> ("NUMENTER");
         case 336 -> ("NUM=");
         case 340, 344 -> ("SHIFT");
         case 341, 345 -> ("CTRL");
         case 342, 346 -> ("ALT");
         case 343, 347 -> ("WIN");
         case 348 -> ("MENU");
         default -> "KEY" + this.code;
      };
   }

   public int getCode() {
      return this.code;
   }

   public static InputBinding keyboard(int code) {
      return code == -1 ? INSTANCE : new InputBinding(InputBinding.BindingType.KEYBOARD, code);
   }

   public boolean isPressed(long windowHandle) {
      return this.isPressed(windowHandle, false);
   }

   public enum BindingType {
      NONE,
      KEYBOARD,
      MOUSE;
   }
}
