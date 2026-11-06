package dev.rose.render.gpu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;


public final class ItemAlpha {
   public static float current = 1.0F;

   private ItemAlpha() {
   }

   public static void render(GuiGraphics g, ItemStack stack, int x, int y, float alpha) {
      float previous = current;
      current = Math.clamp(alpha, 0.0F, 1.0F);

      try {
         g.renderItem(stack, x, y);
      } finally {
         current = previous;
      }
   }

   public static int blitColor(float alpha) {
      int a = Math.round(Math.clamp(alpha, 0.0F, 1.0F) * 255.0F);
      return a << 24 | a << 16 | a << 8 | a;
   }

   public interface Holder {
      float rose$alpha();
   }
}
