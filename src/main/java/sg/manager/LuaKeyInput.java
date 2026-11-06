package sg.manager;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import sg.annotation.LuaExport;

public class LuaKeyInput {
   KeyMapping В;

   @LuaExport("isKeyPressed")
   public boolean isKeyPressed(int keyCode) {
      if (keyCode == 0) {
         return false;
      }

      long var2 = Minecraft.getInstance().getWindow().handle();
      return GLFW.glfwGetKey(var2, keyCode) == 1;
   }
}
