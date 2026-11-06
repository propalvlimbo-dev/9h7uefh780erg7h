package sg.modules.impl.misc;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.ContainerSlotActionEvent;
import sg.settings.FloatSetting;

public final class ItemScroller extends ClientModule {
   public final @com.google.gson.annotations.SerializedName("Вн")  FloatSetting delaySetting;
   private @com.google.gson.annotations.SerializedName("В")  AbstractContainerScreen<?> activeContainerScreen;
   private @com.google.gson.annotations.SerializedName("Вф")  long lastActionMillis;
   private final @com.google.gson.annotations.SerializedName("Вч")  EventListener<ContainerSlotActionEvent> slotActionListener;
   private static final String Вф8 = "ItemScroller";
   private static final String Вф5 = "Упрощает перемещение предметов";
   private static final String Вфв = "Задержка";
   private static final float Вфъ = 10.0F;
   private static final long ВфМ = 100L;
   private static final float ВфН = 10.0F;
   private static final float Вфн = 8.0F;

   @Override
   public void onEnable() {
      this.activeContainerScreen = null;
      this.lastActionMillis = 0L;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.activeContainerScreen = null;
      this.lastActionMillis = 0L;
      super.onDisable();
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      float var2 = this.delaySetting.getValue();
      this.delaySetting.setValue(Float.isFinite(var2) && var2 >= 0.0F && var2 <= ВфН ? var2 : Вфн);
   }

   private void и(ContainerSlotActionEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.gui.screen() == var1.getScreen()) {
         long var2 = System.currentTimeMillis();
         if (this.activeContainerScreen != var1.getScreen()) {
            this.activeContainerScreen = var1.getScreen();
            this.lastActionMillis = var2;
         }

         Slot var4 = var1.getSlot();
         if (MINECRAFT_CLIENT.isWindowActive() && var4 != null && var4.isActive() && var4.hasItem()) {
            long var5 = MINECRAFT_CLIENT.getWindow().handle();
            if (GLFW.glfwGetMouseButton(var5, 0) == 1 && GLFW.glfwGetKey(var5, 340) == 1) {
               long var7 = this.delaySetting.getValue().longValue() * ВфМ;
               if (var2 - this.lastActionMillis >= var7) {
                  var1.runQuickMove();
                  this.lastActionMillis = var2;
               }
            }
         }
      }
   }

   public ItemScroller() {
      super(Вф8, Вф5, ModuleCategory.MISC);
      this.delaySetting = new FloatSetting(Вфв, 1.0F, 0.0F, Вфъ).withStep(1.0F);
      this.slotActionListener = this::и;
      this.addSettings(this.delaySetting);
   }
}
