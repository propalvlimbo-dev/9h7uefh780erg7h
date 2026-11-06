package sg.ec;

import net.minecraft.client.Minecraft;

public final class GuiRenderFlush {
   private static boolean сд;

   public static void method_8Х() {
      сд = true;

      try {
         Minecraft.getInstance().gameRenderer.guiRenderer.render();
      } finally {
         сд = false;
      }
   }

   public static boolean ьд() {
      return сд;
   }

   private GuiRenderFlush() {
   }
}
