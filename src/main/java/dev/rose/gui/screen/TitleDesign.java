package dev.rose.gui.screen;

import dev.rose.RoseClient;
import dev.rose.anim.Spring;
import dev.rose.config.BuildLinks;
import dev.rose.config.UpdateChecker;
import dev.rose.module.impl.ScreensModule;
import dev.rose.render.RoseLogo;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;


public final class TitleDesign {
   private static final RoseLogo MARK = new RoseLogo("title");
   private static final PlayerPreview PLAYER = new PlayerPreview();
   private static final int WHATS_NEW_LINES = 4;
   private static final Spring yaw = new Spring(0.0, 0.9, 0.9);
   private static final Spring pitch = new Spring(0.0, 0.9, 0.9);
   private static final Spring closeHover = new Spring(0.0, 0.25, 0.8);
   private static final Spring cardPresence = new Spring(1.0, 0.45, 0.85);
   private static boolean laidOut;
   private static int columnX;
   private static int columnW;
   private static int headerY;
   private static int realmsDx;
   private static int realmsDy;
   private static float fade = 1.0F;
   private static float closeX;
   private static float closeY;
   private static float closeSize;
   private static boolean cardVisible;

   private TitleDesign() {
   }

   public static boolean active() {
      return ScreensModule.titleScreen();
   }

   public static void layout(TitleScreen screen, int width, int height) {
      laidOut = false;
      if (active()) {
         AbstractWidget single = null;
         AbstractWidget reset = null;
         AbstractWidget multi = null;
         AbstractWidget realms = null;
         AbstractWidget mods = null;
         AbstractWidget options = null;
         AbstractWidget quit = null;
         AbstractWidget language = null;
         AbstractWidget accessibility = null;

         for (AbstractWidget widget : Screens.getButtons(screen)) {
            switch (GlassWidgets.key(widget)) {
               case "menu.singleplayer":
               case "menu.playdemo":
                  single = widget;
                  break;
               case "menu.resetdemo":
                  reset = widget;
                  break;
               case "menu.multiplayer":
                  multi = widget;
                  break;
               case "menu.online":
                  realms = widget;
                  break;
               case "modmenu.title":
                  mods = widget;
                  break;
               case "menu.options":
                  options = widget;
                  break;
               case "menu.quit":
                  quit = widget;
                  break;
               case "options.language":
                  language = widget;
                  break;
               case "options.accessibility":
               case "accessibility.onboarding.accessibility.button":
                  accessibility = widget;
            }
         }

         if (single != null && options != null && quit != null) {
            int margin = margin(width);
            columnW = Math.round(Math.clamp(width * 0.27F, 150.0F, 196.0F));
            int buttonH = height >= 300 ? 22 : 20;
            int gap = height >= 300 ? 5 : 4;
            List<AbstractWidget> primary = new ArrayList<>();
            List<Integer> icons = new ArrayList<>();
            add(primary, icons, single, "menu.playdemo".equals(GlassWidgets.key(single)) ? 57660 : 57759);
            add(primary, icons, reset, 57672);
            add(primary, icons, multi, 57764);
            add(primary, icons, realms, 57480);
            add(primary, icons, mods, 57641);
            int header = 52;
            int headerGap = 14;
            int columnH = header + headerGap + primary.size() * (buttonH + gap) + buttonH;
            int top = Math.max(26, Math.round((height - columnH) * 0.5F));
            columnX = margin;
            headerY = top;
            int y = top + header + headerGap;

            for (int i = 0; i < primary.size(); i++) {
               AbstractWidget widget = primary.get(i);
               widget.setRectangle(columnW, buttonH, columnX, y);
               GlassWidgets.style(widget).icon(icons.get(i)).left();
               y += buttonH + gap;
            }

            int squares = 1 + (language != null ? 1 : 0) + (accessibility != null ? 1 : 0);
            int optionsW = columnW - squares * (buttonH + gap);
            options.setRectangle(optionsW, buttonH, columnX, y);
            GlassWidgets.style(options).icon(57684).left();
            int x = columnX + optionsW + gap;
            if (language != null) {
               language.setRectangle(buttonH, buttonH, x, y);
               x += buttonH + gap;
            }

            if (accessibility != null) {
               accessibility.setRectangle(buttonH, buttonH, x, y);
               x += buttonH + gap;
            }

            quit.setRectangle(buttonH, buttonH, x, y);
            GlassWidgets.style(quit).icon(57664).iconOnly().tint(ColorUtil.withAlpha(Theme.danger, 168), true);
            quit.setTooltip(Tooltip.create(quit.getMessage()));
            if (realms != null) {
               realmsDx = realms.getX() + realms.getWidth() - (width / 2 + 100);
               realmsDy = realms.getY() - (height / 4 + 48 + 48);
            } else {
               realmsDy = 0;
               realmsDx = 0;
            }

            laidOut = true;
         }
      }
   }

   private static void add(List<AbstractWidget> widgets, List<Integer> icons, AbstractWidget widget, int icon) {
      if (widget != null) {
         widgets.add(widget);
         icons.add(icon);
      }
   }

   private static int margin(int width) {
      return Math.round(Math.clamp(width * 0.055F, 14.0F, 44.0F));
   }

   public static boolean laidOut() {
      return laidOut && active();
   }

   public static int realmsDx() {
      return realmsDx;
   }

   public static int realmsDy() {
      return realmsDy;
   }

   public static void setFade(float value) {
      fade = value;
   }

   public static void renderBackdrop(GuiGraphics g, int width, int height) {
      if (laidOut()) {
         try {
            Render2D.gradientH(g, 0.0F, 0.0F, width * 0.6F, height, 0.0F, ColorUtil.fade(1711276032, fade), 0);
            Render2D.gradientV(g, 0.0F, height * 0.72F, width, height * 0.28F, 0.0F, 0, ColorUtil.fade(1241513984, fade));
         } catch (RuntimeException e) {
            ScreensModule.crashed(e);
         }
      }
   }

   public static void renderForeground(GuiGraphics g, int width, int height, int mouseX, int mouseY, String splash) {
      if (laidOut()) {
         try {
            header(g, splash);
            version(g, height);
            float cardBottom = whatsNew(g, width, height, mouseX, mouseY);
            player(g, width, height, mouseX, mouseY, cardBottom);
         } catch (RuntimeException e) {
            ScreensModule.crashed(e);
         }
      }
   }

   private static void header(GuiGraphics g, String splash) {
      SdfFont display = Fonts.get(FontWeight.DISPLAY_BOLD);
      SdfFont medium = Fonts.get(FontWeight.MEDIUM);
      if (display != null && medium != null) {
         float markW = 17.0F;
         float markH = markW * 1.34F;
         MARK.draw(g, columnX, headerY, markW, 0.6F, fade);
         float size = 21.0F;
         float textX = columnX + markW + 8.0F;
         float centerY = headerY + markH * 0.58F;
         display.draw(g, "ROSE", textX, centerY - display.ascent(size) + display.capHeight(size) / 2.0F, size, 1.4F, ColorUtil.fade(-1, fade));
         float subY = headerY + markH + 7.0F;
         medium.draw(g, "Minecraft " + SharedConstants.getCurrentVersion().name(), columnX + 1, subY, 8.0F, ColorUtil.fade(-1191182337, fade));
         if (splash != null && !splash.isEmpty() && ScreensModule.titleSplash()) {
            float splashSize = 7.5F;
            int color = ColorUtil.fade(ColorUtil.lerp(Theme.accent(), -1, 0.35F), fade * (0.85F + 0.15F * (float)Math.sin(Theme.time() * 2.4)));
            if (medium.canDisplay(splash)) {
               float w = medium.width(splash, splashSize);
               if (w > columnW) {
                  splashSize *= columnW / w;
               }

               medium.draw(g, splash, columnX + 1, subY + 11.0F, splashSize, color);
            } else {
               g.drawString(Minecraft.getInstance().font, splash, columnX + 1, Math.round(subY + 11.0F), color, false);
            }
         }
      }
   }

   private static void version(GuiGraphics g, int height) {
      SdfFont font = Fonts.get(FontWeight.MEDIUM);
      if (font != null) {
         Minecraft mc = Minecraft.getInstance();
         String text = "Minecraft "
            + SharedConstants.getCurrentVersion().name()
            + ("release".equalsIgnoreCase(mc.getVersionType()) ? "" : "/" + mc.getVersionType())
            + (Minecraft.checkModStatus().shouldReportAsModified() ? I18n.get("menu.modded", new Object[0]) : "")
            + "  ·  Rose "
            + RoseClient.version()
            + (BuildLinks.shortOfficial().isEmpty() ? "" : "  ·  " + BuildLinks.shortOfficial());
         if (!font.canDisplay(text)) {
            g.drawString(mc.font, text, 4, height - 10, ColorUtil.fade(-1711276033, fade), false);
         } else {
            font.draw(g, text, columnX + 1, height - 13, 7.0F, ColorUtil.fade(-1929379841, fade));
         }
      }
   }

   private static float whatsNew(GuiGraphics g, int width, int height, int mouseX, int mouseY) {
      boolean show = (ScreensModule.titleWhatsNew() || UpdateChecker.latest() != null) && width >= 500 && height >= 250;
      float presence = cardPresence.setTarget(show ? 1.0 : 0.0).getFloat();
      cardVisible = show;
      if (presence <= 0.01F) {
         return 30.0F;
      }

      SdfFont semibold = Fonts.get(FontWeight.SEMIBOLD);
      SdfFont regular = Fonts.get(FontWeight.REGULAR);
      SdfFont medium = Fonts.get(FontWeight.MEDIUM);
      SdfFont icons = Fonts.icons();
      if (semibold != null && regular != null && medium != null && icons != null) {
         int margin = margin(width);
         float cardW = Math.clamp(width * 0.27F, 168.0F, 214.0F);
         float x = width - margin - cardW;
         float y = 36.0F;
         float pad = 11.0F;
         float textSize = 7.6F;
         float lineH = 11.5F;
         List<String> lines = new ArrayList<>();
         List<Boolean> firsts = new ArrayList<>();
         float textW = cardW - pad * 2.0F - 9.0F;

         for (int i = 1; i <= 4; i++) {
            String key = "rose.screens.whats_new." + i;
            if (I18n.exists(key)) {
               boolean first = true;

               for (String line : wrap(regular, I18n.get(key, new Object[0]), textSize, textW)) {
                  lines.add(line);
                  firsts.add(first);
                  first = false;
               }
            }
         }

         float cardH = pad + 14.0F + 7.0F + lines.size() * lineH + pad - 3.0F;
         float alpha = fade * presence;
         float lift = (1.0F - presence) * -8.0F;
         Corners corners = Corners.themed(14.0F);
         Surfaces.shadow(g, x, y + lift + 3.0F, cardW, cardH, corners, new Shadow(1342177280, 14.0F, 0.0F, 0.0F, 4.0F), alpha);
         LiquidGlass.draw(g, x, y + lift, cardW, cardH, corners, Theme.glass(), alpha);
         int label = Theme.glass().label;
         float rowY = y + lift + pad + 7.0F;
         icons.drawIcon(g, 58184, x + pad + 5.0F, rowY, 10.0F, ColorUtil.fade(Theme.accent(), alpha));
         String title = I18n.get("rose.screens.whats_new", new Object[0]);
         semibold.draw(g, title, x + pad + 15.0F, rowY - semibold.ascent(9.0F) + semibold.capHeight(9.0F) / 2.0F, 9.0F, ColorUtil.fade(label, alpha));
         String update = UpdateChecker.latest();
         String version = update != null ? I18n.get("rose.update.badge", new Object[]{update}) : "v" + RoseClient.version();
         float badgeX = x + pad + 15.0F + semibold.width(title, 9.0F) + 6.0F;
         float badgeW = medium.width(version, 6.8F) + 9.0F;
         float badgeH = 11.0F;
         Surfaces.fill(
            g, badgeX, rowY - badgeH / 2.0F, badgeW, badgeH, Corners.round(badgeH / 2.0F), Paint.solid(ColorUtil.withAlpha(Theme.accent(), 64)), alpha
         );
         medium.draw(
            g,
            version,
            badgeX + 4.5F,
            rowY - medium.ascent(6.8F) + medium.capHeight(6.8F) / 2.0F,
            6.8F,
            ColorUtil.fade(ColorUtil.lerp(label, Theme.accent(), 0.4F), alpha)
         );
         closeSize = 14.0F;
         closeX = x + cardW - pad - closeSize + 4.0F;
         closeY = rowY - closeSize / 2.0F;
         boolean hovered = mouseX >= closeX && mouseX <= closeX + closeSize && mouseY >= closeY && mouseY <= closeY + closeSize;
         float h = Math.clamp(closeHover.setTarget(hovered ? 1.0 : 0.0).getFloat(), 0.0F, 1.0F);
         if (h > 0.01F) {
            Surfaces.fill(
               g, closeX, closeY, closeSize, closeSize, Corners.round(closeSize / 2.0F), Paint.solid(ColorUtil.withAlpha(label, Math.round(h * 34.0F))), alpha
            );
         }

         icons.drawIcon(
            g,
            57778,
            closeX + closeSize / 2.0F,
            closeY + closeSize / 2.0F,
            9.0F,
            ColorUtil.fade(ColorUtil.withAlpha(label, 144 + Math.round(111.0F * h)), alpha)
         );
         float lineY = y + lift + pad + 14.0F + 7.0F;

         for (int i = 0; i < lines.size(); i++) {
            if (firsts.get(i)) {
               Render2D.circle(g, x + pad + 2.5F, lineY + lineH * 0.42F, 1.6F, ColorUtil.fade(Theme.accent(), alpha));
            }

            regular.draw(g, lines.get(i), x + pad + 9.0F, lineY, textSize, ColorUtil.fade(ColorUtil.withAlpha(label, 200), alpha));
            lineY += lineH;
         }

         return y + cardH;
      } else {
         return 30.0F;
      }
   }

   private static List<String> wrap(SdfFont font, String text, float size, float maxWidth) {
      List<String> lines = new ArrayList<>();
      StringBuilder line = new StringBuilder();

      for (String word : text.split(" ")) {
         String candidate = line.isEmpty() ? word : line + " " + word;
         if (!(font.width(candidate, size) <= maxWidth) && !line.isEmpty()) {
            lines.add(line.toString());
            line.setLength(0);
            line.append(word);
         } else {
            line.setLength(0);
            line.append(candidate);
         }
      }

      if (!line.isEmpty()) {
         lines.add(line.toString());
      }

      return lines;
   }

   private static void player(GuiGraphics g, int width, int height, int mouseX, int mouseY, float cardBottom) {
      if (ScreensModule.titlePlayer() && width >= 420 && height >= 220) {
         SdfFont semibold = Fonts.get(FontWeight.SEMIBOLD);
         if (semibold != null) {
            Minecraft mc = Minecraft.getInstance();
            int margin = margin(width);
            float cardW = Math.clamp(width * 0.27F, 168.0F, 214.0F);
            float centerX = width - margin - cardW / 2.0F;
            float feetY = height - Math.max(margin, 24) - 20;
            float modelH = Math.clamp(height * 0.4F, 70.0F, 150.0F);
            float headTop = feetY - modelH * 1.06F;
            if (headTop < cardBottom + 14.0F) {
               modelH = (feetY - cardBottom - 14.0F) / 1.06F;
            }

            if (!(modelH < 56.0F)) {
               float time = Theme.time();
               float targetYaw = Math.clamp((mouseX - centerX) / width * 70.0F, -38.0F, 38.0F) + (float)Math.sin(time * 0.5) * 4.0F;
               float headCenterY = feetY - modelH * 0.88F;
               float targetPitch = Math.clamp((mouseY - headCenterY) / height * 50.0F, -24.0F, 24.0F);
               float bodyYaw = yaw.setTarget(targetYaw).getFloat();
               float headPitch = pitch.setTarget(targetPitch).getFloat();
               Surfaces.shadow(
                  g, centerX - modelH * 0.2F, feetY - 2.0F, modelH * 0.4F, 4.0F, Corners.round(2.0F), new Shadow(2013265920, 9.0F, 0.0F, 0.0F, 0.0F), fade
               );
               PLAYER.render(g, centerX, feetY, modelH, -bodyYaw * 0.55F, -bodyYaw * 0.45F, headPitch, time, fade);
               String name = mc.getUser().getName();
               float size = 8.2F;
               boolean sdf = semibold.canDisplay(name);
               float textW = sdf ? semibold.width(name, size) : mc.font.width(name);
               float pillH = 16.0F;
               float pillW = textW + 24.0F;
               float pillX = centerX - pillW / 2.0F;
               float pillY = feetY + 6.0F;
               Glass glass = Theme.glass();
               LiquidGlass.draw(g, pillX, pillY, pillW, pillH, Corners.round(pillH / 2.0F), glass, fade);
               Render2D.circle(g, pillX + 8.0F, pillY + pillH / 2.0F, 2.2F, ColorUtil.fade(Theme.positive, fade));
               if (sdf) {
                  semibold.draw(
                     g,
                     name,
                     pillX + 14.0F,
                     pillY + pillH / 2.0F - semibold.ascent(size) + semibold.capHeight(size) / 2.0F,
                     size,
                     ColorUtil.fade(glass.label, fade)
                  );
               } else {
                  g.drawString(mc.font, name, Math.round(pillX + 14.0F), Math.round(pillY + 4.0F), ColorUtil.fade(glass.label, fade), false);
               }
            }
         }
      }
   }

   public static boolean mouseClicked(double mouseX, double mouseY) {
      if (!laidOut() || !cardVisible) {
         return false;
      } else if (mouseX >= closeX && mouseX <= closeX + closeSize && mouseY >= closeY && mouseY <= closeY + closeSize) {
         ScreensModule.dismissWhatsNew();
         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         return false;
      }
   }
}
