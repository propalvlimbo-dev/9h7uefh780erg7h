package sg.manager;

import net.minecraft.client.Minecraft;
import sg.annotation.LuaExport;
import sg.mixinbridge.HmiCameraAccess;

public class LuaCameraAccess {
   @LuaExport
   public void п(double var1, double var3, double var5) {
      if (Minecraft.getInstance().gameRenderer.mainCamera() instanceof HmiCameraAccess var8) {
         var8.hMI5_0$setRotationValues((float)var1, (float)var3, (float)var5);
      }
   }

   @LuaExport
   public void ъ(double var1, double var3, double var5) {
      if (Minecraft.getInstance().gameRenderer.mainCamera() instanceof HmiCameraAccess var8) {
         var8.hMI5_0$setPosValues((float)var1, (float)var3, (float)var5);
      }
   }
}
