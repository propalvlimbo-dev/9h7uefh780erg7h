package sg.modules.impl.render;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.server.packs.resources.Resource;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.events.impl.HudRenderEvent;

public class Cursor extends ClientModule {
   private static final int ВЯ = 18;
   private static final int ВХ = 1;
   private long сД = 0L;
   private boolean са;
   private final EventListener<PlayerTickEvent> х = this::п;
   private final EventListener<HudRenderEvent.GuiRender> У = this::method_1;
   private static final String ЛЛ = "Cursor";
   private static final String Л诶 = "Курсор красной морды";
   private static final String Л1 = "gui/cursor";
   private static final int ЛЬ = 208897;
   private static final int Л衣 = 212993;

   private void method_1Е() {
      if (MINECRAFT_CLIENT.getWindow() != null) {
         GLFW.glfwSetCursor(MINECRAFT_CLIENT.getWindow().handle(), 0L);
      }
   }

   @Override
   public void onDisable() {
      this.method_1Е();
   }

   private void п(PlayerTickEvent var1) {
      this.method_1Т();
      this.method_10();
   }

   public Cursor() {
      super(ЛЛ, Л诶, ModuleCategory.RENDER);
   }

   private void method_10() {
      if (this.сД != 0L && MINECRAFT_CLIENT.getWindow() != null) {
         long var1 = MINECRAFT_CLIENT.getWindow().handle();
         if (MINECRAFT_CLIENT.gui.screen() != null && GLFW.glfwGetInputMode(var1, ЛЬ) == Л衣) {
            GLFW.glfwSetCursor(var1, this.сД);
         }
      }
   }

   @Override
   public void onEnable() {
      this.method_1Т();
      this.method_10();
   }

   private void method_1(HudRenderEvent.GuiRender var1) {
      this.method_1Т();
      this.method_10();
   }

   private void method_1Т() {
      if (!this.са && this.сД == 0L) {
         if (MINECRAFT_CLIENT.getWindow() != null && MINECRAFT_CLIENT.getResourceManager() != null) {
            this.са = true;

            try {
               Optional var1 = MINECRAFT_CLIENT.getResourceManager().getResource(ClientResourceIds.getImageId(Л1));
               if (var1.isEmpty()) {
                  return;
               }

               try (
                  InputStream var2 = ((Resource)var1.get()).open();
                  NativeImage var3 = NativeImage.read(var2);
               ) {
                  int var4 = var3.getWidth();
                  int var5 = var3.getHeight();
                  ByteBuffer var6 = MemoryUtil.memAlloc(var4 * var5 * 4);

                  try {
                     for (int var7 = 0; var7 < var5; var7++) {
                        for (int var8 = 0; var8 < var4; var8++) {
                           int var9 = var3.getPixel(var8, var7);
                           var6.put((byte)(var9 >> 16 & 0xFF));
                           var6.put((byte)(var9 >> 8 & 0xFF));
                           var6.put((byte)(var9 & 0xFF));
                           var6.put((byte)(var9 >> 24 & 0xFF));
                        }
                     }

                     var6.flip();

                     try (MemoryStack var27 = MemoryStack.stackPush()) {
                        GLFWImage var28 = GLFWImage.malloc(var27);
                        var28.set(var4, var5, var6);
                        this.сД = GLFW.glfwCreateCursor(var28, 18, 1);
                     }
                  } finally {
                     MemoryUtil.memFree(var6);
                  }
               }
            } catch (Throwable var26) {
               var26.printStackTrace();
               this.сД = 0L;
            }
         }
      }
   }
}
