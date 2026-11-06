package dev.rose.gui.screen;

import dev.rose.module.impl.ScreensModule;
import dev.rose.render.RoseLogo;
import dev.rose.render.font.BitmapText;
import net.minecraft.client.gui.GuiGraphics;


public final class RoseSplash {
   public static final int BACKGROUND = -15987696;
   private static final RoseLogo LOGO = new RoseLogo("splash");
   private static final BitmapText WORDMARK = new BitmapText("unbounded-bold", "splash");
   private static boolean failed;

   private RoseSplash() {
   }

   public static boolean active() {
      return !failed && ScreensModule.loadingScreen();
   }

   public static void draw(GuiGraphics g, float alpha, float progress) {
      try {
         int width = g.guiWidth();
         int height = g.guiHeight();
         float logoW = Math.clamp(Math.min(width * 0.1F, height * 0.16F), 22.0F, 84.0F);
         float logoH = logoW * 1.34F;
         float textSize = logoW * 0.3F;
         float blockH = logoH + logoW * 0.34F + textSize;
         float top = height * 0.47F - blockH / 2.0F;
         float level = 0.08F + 0.92F * Math.clamp(progress, 0.0F, 1.0F);
         LOGO.draw(g, width / 2.0F - logoW / 2.0F, top, logoW, level, alpha);
         WORDMARK.drawCentered(g, "ROSE", width / 2.0F, top + logoH + logoW * 0.34F, textSize, 0.3F, -855307, alpha);
      } catch (RuntimeException e) {
         failed = true;
         ScreensModule.crashed(e);
      }
   }
}
