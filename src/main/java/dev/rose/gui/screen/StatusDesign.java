package dev.rose.gui.screen;

import dev.rose.anim.Spring;
import dev.rose.mixin.ConnectScreenAccessor;
import dev.rose.mixin.DisconnectedScreenAccessor;
import dev.rose.mixin.LevelLoadingScreenAccessor;
import dev.rose.mixin.ProgressScreenAccessor;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;


public final class StatusDesign {
   private static final RoseLogo DROP = new RoseLogo("status");
   private static final Spring progress = new Spring(0.0, 0.5, 1.0);
   private static final int MAX_REASON_LINES = 14;
   private static String connectingTo = "";

   private StatusDesign() {
   }

   public static void connecting(ServerData server) {
      connectingTo = server != null && server.name != null ? server.name : "";
   }

   public static boolean styled(Screen screen) {
      return ScreensModule.statusScreens()
         && (
            screen instanceof ConnectScreen
               || screen instanceof LevelLoadingScreen
               || screen instanceof GenericMessageScreen
               || screen instanceof ProgressScreen
               || screen instanceof DisconnectedScreen
         );
   }

   public static void render(GuiGraphics g, Screen screen) {
      if (styled(screen)) {
         try {
            StatusDesign.Content content = content(screen);
            if (content != null) {
               draw(g, screen, content);
            }
         } catch (RuntimeException e) {
            ScreensModule.crashed(e);
         }
      }
   }

   private static StatusDesign.Content content(Screen screen) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      float cardInner = cardWidth(screen) - 32.0F;
      if (screen instanceof ConnectScreen connect) {
         return new StatusDesign.Content(((ConnectScreenAccessor)connect).rose$status().getString(), connectingTo, List.of(), -1.0F, false);
      } else if (screen instanceof LevelLoadingScreen loading) {
         LevelLoadTracker tracker = ((LevelLoadingScreenAccessor)loading).rose$loadTracker();
         if (tracker != null && tracker.hasProgress()) {
            int percent = Math.clamp(Math.round(tracker.serverProgress() * 100.0F), 0, 100);
            return new StatusDesign.Content(
               I18n.get("rose.screens.loading_world", new Object[0]), percent + "%", List.of(), percent / 100.0F, false
            );
         } else {
            return new StatusDesign.Content(I18n.get("multiplayer.downloadingTerrain", new Object[0]), "", List.of(), -1.0F, false);
         }
      } else if (screen instanceof ProgressScreen progressScreen) {
         ProgressScreenAccessor accessor = (ProgressScreenAccessor)progressScreen;
         Component header = accessor.rose$header();
         Component stage = accessor.rose$stage();
         int percent = accessor.rose$progress();
         String body = stage == null ? "" : stage.getString() + (percent > 0 ? "  " + percent + "%" : "");
         return new StatusDesign.Content(
            header == null ? I18n.get("menu.working", new Object[0]) : header.getString(),
            body,
            List.of(),
            percent > 0 ? percent / 100.0F : -1.0F,
            false
         );
      } else {
         if (screen instanceof GenericMessageScreen) {
            hideText(screen);
            return new StatusDesign.Content(screen.getTitle().getString(), "", List.of(), -1.0F, false);
         }

         if (screen instanceof DisconnectedScreen disconnected) {
            hideText(screen);
            Component reason = ((DisconnectedScreenAccessor)disconnected).rose$details().reason();
            List<FormattedCharSequence> lines = new ArrayList<>(font.split(reason, Math.round(cardInner)));
            if (lines.size() > 14) {
               lines = new ArrayList<>(lines.subList(0, 14));
            }

            return new StatusDesign.Content(screen.getTitle().getString(), "", lines, -1.0F, true);
         } else {
            return null;
         }
      }
   }

   private static void hideText(Screen screen) {
      for (AbstractWidget widget : Screens.getButtons(screen)) {
         if (widget instanceof StringWidget || widget instanceof MultiLineTextWidget) {
            widget.visible = false;
         }
      }
   }

   private static float cardWidth(Screen screen) {
      return Math.min(screen.width - 32, 300);
   }

   private static void draw(GuiGraphics g, Screen screen, StatusDesign.Content content) {
      SdfFont semibold = Fonts.get(FontWeight.SEMIBOLD);
      SdfFont regular = Fonts.get(FontWeight.REGULAR);
      SdfFont medium = Fonts.get(FontWeight.MEDIUM);
      SdfFont icons = Fonts.icons();
      if (semibold != null && regular != null && medium != null && icons != null) {
         Minecraft mc = Minecraft.getInstance();
         int width = screen.width;
         int height = screen.height;
         float cw = cardWidth(screen);
         float pad = 16.0F;
         float inner = cw - pad * 2.0F;
         float titleSize = 11.0F;
         float bodySize = 8.4F;
         List<String> titleLines = content.title.isEmpty() ? List.of() : wrap(semibold, content.title, titleSize, inner, 3);
         boolean plainSdf = !content.plain.isEmpty() && regular.canDisplay(content.plain);
         List<String> plainLines = plainSdf ? wrap(regular, content.plain, bodySize, inner, 2) : List.of();
         float iconH = content.error ? 30.0F : 32.16F;
         float bodyH = plainLines.size() * 11.0F + (!plainSdf && !content.plain.isEmpty() ? 10 : 0) + content.lines.size() * 10.0F;
         float cardH = pad
            + iconH
            + 10.0F
            + titleLines.size() * 14.0F
            + (bodyH > 0.0F ? 5.0F + bodyH : 0.0F)
            + (content.progress >= 0.0F ? 12 : 0)
            + pad
            - 2.0F;
         List<AbstractButton> buttons = new ArrayList<>();

         for (AbstractWidget widget : Screens.getButtons(screen)) {
            if (widget instanceof AbstractButton button && widget.visible) {
               buttons.add(button);
            }
         }

         int buttonH = 20;
         int buttonGap = 5;
         float buttonsH = buttons.isEmpty() ? 0.0F : 12 + buttons.size() * (buttonH + buttonGap) - buttonGap;
         float x = (width - cw) / 2.0F;
         float y = Math.max(8.0F, (height - cardH - buttonsH) / 2.0F);
         int buttonW = Math.round(Math.min(cw, 220.0F));
         int by = Math.round(y + cardH + 12.0F);

         for (AbstractButton button : buttons) {
            button.setRectangle(buttonW, buttonH, (width - buttonW) / 2, by);
            by += buttonH + buttonGap;
         }

         Corners corners = Corners.themed(18.0F);
         Glass glass = Theme.glass();
         Surfaces.shadow(g, x, y + 4.0F, cw, cardH, corners, new Shadow(1426063360, 18.0F, 0.0F, 0.0F, 5.0F), 1.0F);
         LiquidGlass.draw(g, x, y, cw, cardH, corners, glass, 1.0F);
         int label = glass.label;
         float cy = y + pad;
         if (content.error) {
            float r = 15.0F;
            Surfaces.fill(g, width / 2.0F - r, cy, r * 2.0F, r * 2.0F, Corners.round(r), Paint.solid(ColorUtil.withAlpha(Theme.danger, 56)), 1.0F);
            icons.drawIcon(g, 58461, width / 2.0F, cy + r, 15.0F, ColorUtil.lerp(Theme.danger, -1, 0.25F));
         } else {
            float level = content.progress >= 0.0F ? progress.setTarget(content.progress).getFloat() : 0.42F + 0.12F * (float)Math.sin(Theme.time() * 1.6);
            DROP.draw(g, width / 2.0F - 12.0F, cy, 24.0F, level, 1.0F);
         }

         cy += iconH + 10.0F;

         for (String line : titleLines) {
            semibold.draw(g, line, width / 2.0F - semibold.width(line, titleSize) / 2.0F, cy, titleSize, label);
            cy += 14.0F;
         }

         if (bodyH > 0.0F) {
            cy += 5.0F;
         }

         for (String line : plainLines) {
            regular.draw(g, line, width / 2.0F - regular.width(line, bodySize) / 2.0F, cy, bodySize, ColorUtil.withAlpha(label, 176));
            cy += 11.0F;
         }

         if (!plainSdf && !content.plain.isEmpty()) {
            g.drawCenteredString(mc.font, content.plain, width / 2, Math.round(cy), ColorUtil.withAlpha(label, 192));
            cy += 10.0F;
         }

         for (FormattedCharSequence line : content.lines) {
            g.drawCenteredString(mc.font, line, width / 2, Math.round(cy), ColorUtil.withAlpha(label, 216));
            cy += 10.0F;
         }

         if (content.progress >= 0.0F) {
            float barW = Math.min(inner, 180.0F);
            float barX = width / 2.0F - barW / 2.0F;
            float barY = cy + 6.0F;
            Surfaces.fill(g, barX, barY, barW, 4.0F, Corners.round(2.0F), Paint.solid(ColorUtil.withAlpha(label, 38)), 1.0F);
            float filled = barW * Math.clamp(progress.getFloat(), 0.0F, 1.0F);
            if (filled > 1.0F) {
               Render2D.gradientH(g, barX, barY, filled, 4.0F, 2.0F, Theme.accent(), Theme.accent2());
            }
         }
      }
   }

   private static List<String> wrap(SdfFont font, String text, float size, float maxWidth, int maxLines) {
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

      return lines.size() > maxLines ? lines.subList(0, maxLines) : lines;
   }

   private record Content(String title, String plain, List<FormattedCharSequence> lines, float progress, boolean error) {
   }
}
