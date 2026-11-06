package dev.rose.gui;

import dev.rose.anim.Spring;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;


public final class RoseMenuButton extends AbstractWidget {
   private final Screen owner;
   private final Spring hover = new Spring(0.0, 0.2, 0.8);

   public RoseMenuButton(Screen owner, int x, int y, int width, int height) {
      super(x, y, width, height, Component.literal("Rose"));
      this.owner = owner;
   }

   protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      SdfFont bold = Fonts.get(FontWeight.DISPLAY_BOLD);
      SdfFont icons = Fonts.icons();
      float h = Math.clamp(this.hover.setTarget(this.isHoveredOrFocused() ? 1.0 : 0.0).getFloat(), 0.0F, 1.2F);
      float grow = 1.0F + 0.06F * h;
      float w = this.getWidth() * grow;
      float height = this.getHeight() * grow;
      float x = this.getX() + (this.getWidth() - w) / 2.0F;
      float y = this.getY() + (this.getHeight() - height) / 2.0F;
      Glass glass = Theme.glass().copy();
      glass.bevel = 6.0F;
      LiquidGlass.draw(g, x, y, w, height, Corners.round(height / 2.0F), glass, 1.0F);
      if (bold != null && icons != null) {
         int label = glass.label;
         float size = height * 0.36F;
         float textW = bold.width("ROSE", size, 0.8F);
         float iconSize = height * 0.5F;
         float contentW = iconSize + 4.0F + textW;
         float left = this.getX() + (this.getWidth() - contentW) / 2.0F;
         icons.drawIcon(
            g,
            57525,
            left + iconSize / 2.0F,
            this.getY() + this.getHeight() / 2.0F,
            iconSize,
            ColorUtil.lerp(label, Theme.accent(), 0.4F + 0.6F * Math.min(1.0F, h))
         );
         bold.draw(g, "ROSE", left + iconSize + 4.0F, this.getY() + this.getHeight() / 2.0F - size * 0.62F, size, 0.8F, label);
      }
   }

   public void onClick(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      Minecraft.getInstance().setScreen(new MenuScreen(this.owner));
   }

   protected void updateWidgetNarration(NarrationElementOutput output) {
      this.defaultButtonNarrationText(output);
   }
}
