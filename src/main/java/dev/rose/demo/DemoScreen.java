package dev.rose.demo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public final class DemoScreen extends Screen {
   private final Screen parent;

   public DemoScreen(Screen parent) {
      super(Component.literal("Rose"));
      this.parent = parent;
   }

   public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      this.renderPanorama(g, partialTick);
      Showcase.draw(g, this.width, this.height);
   }

   public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
   }

   public void onClose() {
      this.minecraft.setScreen(this.parent);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
