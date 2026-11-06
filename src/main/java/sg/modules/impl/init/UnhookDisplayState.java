package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import sg.network.ClientChat;
import sg.network.irc.RoseIrc;

final class UnhookDisplayState {
   private RoseIrc В;
   private boolean ъ;
   @SerializedName("8")
   private String field_8;

   static String Д() {
      return "Rose";
   }

   void method_2() {
      Window var1 = Minecraft.getInstance().getWindow();
      this.field_8 = GLFW.glfwGetWindowTitle(var1.handle());
      var1.setTitle(Д());
      this.В = RoseIrc.З_3e6e5516();
      this.ъ = this.В != null && !this.В.Щl();
      if (this.В != null) {
         this.В.ьЮ(true);
      }

      ClientChat.вТ();
   }

   void method_4() {
      ClientChat.вТ();
      if (this.field_8 != null) {
         Minecraft.getInstance().getWindow().setTitle(this.field_8);
      }

      this.field_8 = null;
      if (this.ъ && this.В == RoseIrc.З_3e6e5516()) {
         this.В.ьЮ(false);
      }

      this.В = null;
      this.ъ = false;
   }
}
