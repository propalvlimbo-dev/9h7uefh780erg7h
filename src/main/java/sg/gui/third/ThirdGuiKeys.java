package sg.gui.third;

import org.lwjgl.glfw.GLFW;

/**
 * Key name formatting, ported 1:1 from catlavanskid.
 */
public final class ThirdGuiKeys {
   private static final int MOUSE_BIND_BASE = 1000;
   private static final int MOUSE_BIND_OFFSET = 1;

   private ThirdGuiKeys() {
   }

   public static String name(int bind) {
      if (bind < 0) {
         return "NONE";
      }

      if (bind >= MOUSE_BIND_BASE) {
         return "M" + (bind - MOUSE_BIND_BASE + MOUSE_BIND_OFFSET);
      }

      switch (bind) {
         case GLFW.GLFW_KEY_RIGHT_SHIFT -> {
            return "RSHIFT";
         }
         case GLFW.GLFW_KEY_LEFT_SHIFT -> {
            return "LSHIFT";
         }
         case GLFW.GLFW_KEY_RIGHT_CONTROL -> {
            return "RCTRL";
         }
         case GLFW.GLFW_KEY_LEFT_CONTROL -> {
            return "LCTRL";
         }
         case GLFW.GLFW_KEY_RIGHT_ALT -> {
            return "RALT";
         }
         case GLFW.GLFW_KEY_LEFT_ALT -> {
            return "LALT";
         }
         case GLFW.GLFW_KEY_HOME -> {
            return "HOME";
         }
         case GLFW.GLFW_KEY_END -> {
            return "END";
         }
         case GLFW.GLFW_KEY_INSERT -> {
            return "INS";
         }
         case GLFW.GLFW_KEY_DELETE -> {
            return "DEL";
         }
         case GLFW.GLFW_KEY_PAGE_UP -> {
            return "PGUP";
         }
         case GLFW.GLFW_KEY_PAGE_DOWN -> {
            return "PGDN";
         }
         case GLFW.GLFW_KEY_SPACE -> {
            return "SPACE";
         }
         case GLFW.GLFW_KEY_TAB -> {
            return "TAB";
         }
         case GLFW.GLFW_KEY_CAPS_LOCK -> {
            return "CAPS";
         }
         case GLFW.GLFW_KEY_ENTER -> {
            return "ENTER";
         }
         case GLFW.GLFW_KEY_BACKSPACE -> {
            return "BKSP";
         }
         case GLFW.GLFW_KEY_UP -> {
            return "UP";
         }
         case GLFW.GLFW_KEY_DOWN -> {
            return "DOWN";
         }
         case GLFW.GLFW_KEY_LEFT -> {
            return "LEFT";
         }
         case GLFW.GLFW_KEY_RIGHT -> {
            return "RIGHT";
         }
         case GLFW.GLFW_KEY_SEMICOLON -> {
            return ";";
         }
         default -> {
         }
      }

      if (bind >= GLFW.GLFW_KEY_F1 && bind <= GLFW.GLFW_KEY_F25) {
         return "F" + (bind - GLFW.GLFW_KEY_F1 + MOUSE_BIND_OFFSET);
      }

      String glfwName = GLFW.glfwGetKeyName(bind, 0);
      return glfwName != null ? glfwName.toUpperCase() : "KEY" + bind;
   }
}
