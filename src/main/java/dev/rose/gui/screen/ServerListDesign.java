package dev.rose.gui.screen;

import dev.rose.anim.Spring;
import dev.rose.mixin.JoinMultiplayerScreenAccessor;
import dev.rose.module.impl.ScreensModule;
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
import dev.rose.server.FavoriteServers;
import dev.rose.server.KnownServers;
import dev.rose.util.ColorUtil;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;


public final class ServerListDesign {
   public static final int ROW_HEIGHT = 42;
   private static final float STAR = 10.0F;
   private static final float STAR_GAP = 4.0F;
   private static final Map<Object, Spring> HOVER = new WeakHashMap<>();
   private static final Map<Object, float[]> STARS = new WeakHashMap<>();

   private ServerListDesign() {
   }

   public static boolean active() {
      return ScreensModule.serverList();
   }

   public static boolean styled(Object list) {
      return list instanceof ServerSelectionList && active();
   }

   public static void card(AbstractSelectionList<?> list, GuiGraphics g, int index, int left, int top, int rowWidth, int height, boolean selected, boolean hovered) {
      try {
         Object entry = list.children().get(index);
         if (entry instanceof ServerSelectionList.LANHeader) {
            return;
         }

         float hover = HOVER.computeIfAbsent(entry, ex -> new Spring(0.0, 0.3, 0.85)).setTarget(hovered ? 1.0 : 0.0).getFloat();
         float x = left - 5;
         float y = top - 3;
         float w = rowWidth + 6;
         float h = Math.min(height, 32) + 6;
         Corners corners = Corners.themed(12.0F);
         Glass glass = Theme.glass();
         LiquidGlass.draw(g, x, y, w, h, corners, glass, 1.0F);
         if (hover > 0.01F) {
            Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 16.0F))), 1.0F);
         }

         if (selected) {
            Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(Theme.accent(), 34)), 1.0F);
            Surfaces.border(g, x, y, w, h, corners, 1.1F, ColorUtil.withAlpha(Theme.accent(), 208), 1.0F);
         }
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
      }
   }

   public static boolean scrollbar(GuiGraphics g, Identifier sprite, int x, int y, int w, int h) {
      String path = sprite.getPath();
      if (path.equals("widget/scroller_background")) {
         return true;
      }

      if (!path.equals("widget/scroller")) {
         return false;
      }

      Surfaces.fill(g, x + 2, y + 1, 3.0F, h - 2, Corners.round(1.5F), Paint.solid(-2130706433), 1.0F);
      return true;
   }

   public static int extraStatusWidth(ServerData server) {
      return Math.round(pingWidth(server) - 10.0F + 10.0F + 4.0F);
   }

   private static float pingWidth(ServerData server) {
      if (!ScreensModule.pingMs()) {
         return 10.0F;
      }

      return switch (server.state()) {
         case SUCCESSFUL -> {
            SdfFont font = Fonts.get(FontWeight.SEMIBOLD);
            yield font == null ? 10.0F : font.width(I18n.get("rose.screens.ping", new Object[]{server.ping}), 7.0F) + 12.0F;
         }
         case INITIAL, PINGING -> 22.0F;
         default -> 10.0F;
      };
   }

   public static boolean status(
      Object entry, JoinMultiplayerScreen screen, ServerData server, GuiGraphics g, int x, int y, int rowLeft, int rowTop, boolean hovered, int mouseX, int mouseY
   ) {
      try {
         SdfFont font = Fonts.get(FontWeight.SEMIBOLD);
         SdfFont icons = Fonts.icons();
         if (font != null && icons != null) {
            float right = x + 10;
            float pingW = pingWidth(server);
            boolean ownPing = ScreensModule.pingMs()
               && (
                  server.state() == ServerData.State.SUCCESSFUL
                     || server.state() == ServerData.State.PINGING
                     || server.state() == ServerData.State.INITIAL
               );
            float cy = y + 4;
            if (ownPing) {
               float px = right - pingW;
               Surfaces.fill(g, px, cy - 5.5F, pingW, 11.0F, Corners.round(5.5F), Paint.solid(536870911), 1.0F);
               if (server.state() == ServerData.State.SUCCESSFUL) {
                  long ping = server.ping;
                  int color = ping < 100L ? Theme.positive : (ping < 200L ? -1846454 : (ping < 400L ? Theme.warning : Theme.danger));
                  Render2D.circle(g, px + 5.5F, cy, 2.0F, color);
                  String text = I18n.get("rose.screens.ping", new Object[]{ping});
                  font.draw(g, text, px + 10.0F, cy - font.ascent(7.0F) + font.capHeight(7.0F) / 2.0F, 7.0F, -986892);
               } else {
                  float time = Theme.time();

                  for (int i = 0; i < 3; i++) {
                     float a = 0.35F + 0.65F * (float)Math.max(0.0, Math.sin(time * 6.0F - i * 0.9));
                     Render2D.circle(g, px + 6.0F + i * 5, cy, 1.5F, ColorUtil.withAlpha(-1, Math.round(a * 255.0F)));
                  }
               }
            }

            boolean favorite = FavoriteServers.isFavorite(server);
            float starX = right - pingW - 4.0F - 10.0F;
            float starY = cy - 5.0F;
            STARS.put(entry, new float[]{starX - 1.0F, starY - 1.0F, 12.0F, 12.0F});
            boolean overStar = mouseX >= starX - 1.0F && mouseX <= starX + 10.0F + 1.0F && mouseY >= starY - 1.0F && mouseY <= starY + 10.0F + 1.0F;
            if (favorite) {
               icons.drawIcon(g, 57718, starX + 5.0F, cy, 13.0F, ColorUtil.withAlpha(-15043, 80));
               icons.drawIcon(g, 57718, starX + 5.0F, cy, 10.0F, -15043);
            } else if (hovered) {
               icons.drawIcon(g, 57718, starX + 5.0F, cy, 10.0F, overStar ? -520093697 : 1895825407);
            }

            if (overStar && hovered) {
               g.setTooltipForNextFrame(Component.translatable(favorite ? "rose.screens.unfavorite" : "rose.screens.favorite"), mouseX, mouseY);
            }

            KnownServers.whitelisted(KnownServers.normalizeAddress(server.ip)).ifPresent(known -> {
               Minecraft mc = Minecraft.getInstance();
               float nameEnd = rowLeft + 35 + mc.font.width(server.name) + 5;
               float iconY = rowTop + 5;
               icons.drawIcon(g, 57854, nameEnd + 4.0F, iconY, 8.5F, Theme.warning);
               if (hovered && mouseX >= nameEnd - 1.0F && mouseX <= nameEnd + 9.0F && mouseY >= iconY - 5.0F && mouseY <= iconY + 5.0F) {
                  g.setTooltipForNextFrame(Component.translatable("rose.screens.whitelist", new Object[]{known.name()}), mouseX, mouseY);
               }
            });
            return ownPing;
         } else {
            return false;
         }
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   public static boolean click(Object entry, JoinMultiplayerScreen screen, ServerData server, double mouseX, double mouseY) {
      if (!active()) {
         return false;
      }

      float[] star = STARS.get(entry);
      if (star != null && !(mouseX < star[0]) && !(mouseX > star[0] + star[2]) && !(mouseY < star[1]) && !(mouseY > star[1] + star[3])) {
         boolean favorite = FavoriteServers.toggle(server);
         ServerList servers = screen.getServers();
         if (favorite) {
            int index = -1;

            for (int i = 0; i < servers.size(); i++) {
               if (servers.get(i) == server) {
                  index = i;
               }
            }

            for (int i = index; i > 0; i--) {
               servers.swap(i, i - 1);
            }

            servers.save();
         }

         ServerSelectionList list = ((JoinMultiplayerScreenAccessor)screen).rose$serverSelectionList();
         list.updateOnlineServers(servers);

         for (ServerSelectionList.Entry candidate : list.children()) {
            if (candidate instanceof ServerSelectionList.OnlineServerEntry online && online.getServerData() == server) {
               list.setSelected(candidate);
               list.setScrollAmount(0.0);
            }
         }

         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, favorite ? 1.25F : 0.9F));
         return true;
      } else {
         return false;
      }
   }

   public static void testToggleFavorite(JoinMultiplayerScreen screen, ServerData server) {
      for (ServerSelectionList.Entry candidate : ((JoinMultiplayerScreenAccessor)screen).rose$serverSelectionList().children()) {
         if (candidate instanceof ServerSelectionList.OnlineServerEntry online && online.getServerData() == server) {
            float[] star = STARS.get(candidate);
            if (star != null) {
               click(candidate, screen, server, star[0] + 2.0F, star[1] + 2.0F);
            }
         }
      }
   }

   public static boolean title(GuiGraphics g, JoinMultiplayerScreen screen) {
      SdfFont font = Fonts.get(FontWeight.DISPLAY_BOLD);
      SdfFont icons = Fonts.icons();
      String title = screen.getTitle().getString();
      if (font != null && icons != null && font.canDisplay(title)) {
         float size = 11.0F;
         float w = font.width(title, size, 0.3F) + 16.0F;
         float x = screen.width / 2.0F - w / 2.0F;
         icons.drawIcon(g, 57683, x + 5.0F, 16.0F, 10.0F, Theme.accent());
         font.draw(g, title, x + 16.0F, 16.0F - font.ascent(size) + font.capHeight(size) / 2.0F, size, 0.3F, -1);
         return true;
      } else {
         return false;
      }
   }
}
