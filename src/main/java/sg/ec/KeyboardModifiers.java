package sg.ec;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class KeyboardModifiers {
   private static boolean Ф(long var0, int var2, int var3) {
      return GLFW.glfwGetKey(var0, var2) == 1 || GLFW.glfwGetKey(var0, var3) == 1;
   }

   private KeyboardModifiers() {
   }

   public static int Ф5() {
      long var0 = Minecraft.getInstance().getWindow().handle();
      int var2 = 0;
      if (Ф(var0, 340, 344)) {
         var2 |= 1;
      }

      if (Ф(var0, 341, 345)) {
         var2 |= 2;
      }

      if (Ф(var0, 342, 346)) {
         var2 |= 4;
      }

      if (Ф(var0, 343, 347)) {
         var2 |= 8;
      }

      return var2;
   }
}
