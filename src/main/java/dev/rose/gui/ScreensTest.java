package dev.rose.gui;

import com.google.gson.JsonPrimitive;
import dev.rose.RoseClient;
import dev.rose.gui.screen.ServerListDesign;
import dev.rose.mixin.JoinMultiplayerScreenAccessor;
import dev.rose.mixin.MouseHandlerAccessor;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import java.util.List;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ManageServerScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.status.ServerStatus;


public final class ScreensTest {
   private static final String[][] SERVERS = new String[][]{
      {"FunTime", "mc.funtime.su", "§6FunTime §7— §fгриферство и анархия", "18432", "25000", "38"},
      {"Rose Test", "127.0.0.1:25599", "§bЛокальный тестовый сервер", "3", "20", "4"},
      {"HolyWorld", "mc.holyworld.me", "§dHolyWorld §7| §fвыживание", "9120", "15000", "142"},
      {"Friends", "192.168.1.40", "§aСервер с друзьями", "2", "10", "260"}
   };
   private static long stepAt = -1L;
   private static int step;
   private static boolean done;

   private ScreensTest() {
   }

   public static void tick(Minecraft mc) {
      if ("screens".equals(RoseClient.uiTest()) && !done) {
         if (mc.getOverlay() == null || step == 3 || step == 4) {
            long now = System.currentTimeMillis();
            if (stepAt < 0L) {
               if (!(mc.screen instanceof TitleScreen)) {
                  return;
               }

               stepAt = now + 5000L;
            }

            if (now >= stepAt) {
               long next = run(mc, step);
               if (next >= 0L) {
                  RoseClient.LOGGER.info("[Rose][screenstest] step {} done", step);
                  step++;
                  stepAt = now + next;
               }
            }
         }
      }
   }

   private static long run(Minecraft mc, int index) {
      switch (index) {
         case 0:
            shot(mc, "title");
            break;
         case 1:
            if (mc.screen instanceof TitleScreen screen) {
               hover(mc, find(screen, "menu.multiplayer"));
               return 700L;
            }

            return -1L;
         case 2:
            shot(mc, "title_hover");
            mc.reloadResourcePacks();
            return 900L;
         case 3:
            shot(mc, "loading_a");
            return 1300L;
         case 4:
            shot(mc, "loading_b");
            return 1000L;
         case 5:
            if (mc.getOverlay() == null && mc.screen instanceof TitleScreen screen) {
               prepareServers(mc);
               mc.setScreen(new JoinMultiplayerScreen(screen));
               fakeStatus(mc);
               mouse(mc, 20.0, 20.0);
               return 1500L;
            }

            return -1L;
         case 6:
            shot(mc, "servers");
            break;
         case 7:
            mouse(mc, 320.0, 136.0);
            return 800L;
         case 8:
            shot(mc, "servers_hover");
            if (mc.screen instanceof JoinMultiplayerScreen screen) {
               ServerData third = screen.getServers().get(2);
               ServerListDesign.testToggleFavorite(screen, third);
            }

            return 900L;
         case 9:
            shot(mc, "servers_favorite");
            if (mc.screen instanceof JoinMultiplayerScreen screen) {
               mc.setScreen(
                  new ManageServerScreen(
                     screen,
                     Component.translatable("manageServer.add.title"),
                     accepted -> mc.setScreen(screen),
                     new ServerData("Rose", "", ServerData.Type.OTHER)
                  )
               );
            }

            return 1000L;
         case 10:
            shot(mc, "edit_server");
            if (mc.screen instanceof ManageServerScreen edit) {
               mc.setScreen(new SafetyScreen(edit));
            }

            return 1000L;
         case 11:
            shot(mc, "safety");
            if (mc.screen instanceof SafetyScreen) {
               mc.setScreen(new JoinMultiplayerScreen(new TitleScreen()));
               fakeStatus(mc);
            }

            return 1200L;
         case 12:
            if (mc.screen instanceof JoinMultiplayerScreen screen) {
               ConnectScreen.startConnecting(
                  screen,
                  mc,
                  ServerAddress.parseString("10.255.255.1:25565"),
                  new ServerData("Rose Test", "10.255.255.1:25565", ServerData.Type.OTHER),
                  false,
                  null
               );
            }

            mouse(mc, 20.0, 20.0);
            return 1500L;
         case 13:
            shot(mc, "connecting");
            if (mc.screen instanceof ConnectScreen screen) {
               press(screen, "gui.cancel");
            }

            return 1200L;
         case 14:
            if (mc.screen instanceof JoinMultiplayerScreen screen) {
               ConnectScreen.startConnecting(
                  screen, mc, ServerAddress.parseString("127.0.0.1:1"), new ServerData("Rose Test", "127.0.0.1:1", ServerData.Type.OTHER), false, null
               );
               return 2500L;
            }

            return -1L;
         case 15:
            if (!(mc.screen instanceof DisconnectedScreen)) {
               return -1L;
            }

            shot(mc, "disconnected");
            mc.setScreen(new TitleScreen());
            return 1500L;
         case 16:
            WorldTest.enterWorldForTest(mc);
            return 100L;
         case 17:
            if (!(mc.screen instanceof LevelLoadingScreen)) {
               return mc.level != null ? 0L : -1L;
            }

            return 350L;
         case 18:
            if (mc.screen instanceof LevelLoadingScreen) {
               shot(mc, "world_loading");
            }

            return 0L;
         case 19:
            if (mc.level != null && mc.player != null && mc.screen == null) {
               mc.options.hideGui = false;
               return 3500L;
            }

            return -1L;
         case 20:
            mc.pauseGame(false);
            mouse(mc, 20.0, 20.0);
            return 1200L;
         case 21:
            shot(mc, "pause");
            if (mc.screen instanceof PauseScreen screen) {
               hover(mc, find(screen, "menu.returnToGame"));
            }

            return 700L;
         case 22:
            shot(mc, "pause_hover");
            if (mc.screen instanceof PauseScreen screen) {
               mc.setScreen(new OptionsScreen(screen, mc.options));
            }

            mouse(mc, 20.0, 20.0);
            return 1200L;
         case 23:
            shot(mc, "options");
            Module performance = Modules.byId("performance");
            if (performance != null) {
               performance.settings().stream().filter(setting -> setting.id().equals("lite")).forEach(setting -> setting.fromJson(new JsonPrimitive(true)));
            }

            return 1200L;
         case 24:
            shot(mc, "options_lite");
            RoseClient.LOGGER.info("[Rose][screenstest] finished");
            done = true;
      }

      return 900L;
   }

   private static void shot(Minecraft mc, String name) {
      Screenshot.grab(mc.gameDirectory, "rose_" + name + ".png", mc.getMainRenderTarget(), 1, message -> {});
      RoseClient.LOGGER.info("[Rose][screenstest] shot {}", name);
   }

   private static AbstractWidget find(Screen screen, String key) {
      for (AbstractWidget widget : Screens.getButtons(screen)) {
         if (widget.getMessage().getContents() instanceof TranslatableContents contents && contents.getKey().equals(key)) {
            return widget;
         }
      }

      return null;
   }

   private static void press(Screen screen, String key) {
      AbstractWidget widget = find(screen, key);
      if (widget != null) {
         widget.onClick(new MouseButtonEvent(widget.getX() + 1, widget.getY() + 1, new MouseButtonInfo(0, 0)), false);
      }
   }

   private static void hover(Minecraft mc, AbstractWidget widget) {
      if (widget != null) {
         mouse(mc, widget.getX() + widget.getWidth() / 2.0, widget.getY() + widget.getHeight() / 2.0);
      }
   }

   private static void mouse(Minecraft mc, double guiX, double guiY) {
      double scale = mc.getWindow().getGuiScale();
      MouseHandlerAccessor accessor = (MouseHandlerAccessor)mc.mouseHandler;
      accessor.rose$setX(guiX * scale);
      accessor.rose$setY(guiY * scale);
   }

   private static void prepareServers(Minecraft mc) {
      ServerList list = new ServerList(mc);
      list.load();

      while (list.size() > 0) {
         list.remove(list.get(0));
      }

      for (String[] server : SERVERS) {
         list.add(new ServerData(server[0], server[1], ServerData.Type.OTHER), false);
      }

      list.save();
   }

   private static void fakeStatus(Minecraft mc) {
      if (mc.screen instanceof JoinMultiplayerScreen screen) {
         ServerList var11 = screen.getServers();

         for (int i = 0; i < var11.size(); i++) {
            ServerData data = var11.get(i);

            for (String[] server : SERVERS) {
               if (server[1].equals(data.ip)) {
                  data.setState(ServerData.State.SUCCESSFUL);
                  data.motd = Component.literal(server[2]);
                  int online = Integer.parseInt(server[3]);
                  int max = Integer.parseInt(server[4]);
                  data.players = new ServerStatus.Players(max, online, List.of());
                  data.status = Component.literal(online + "")
                     .withStyle(ChatFormatting.GRAY)
                     .append(Component.literal("/").withStyle(ChatFormatting.DARK_GRAY))
                     .append(Component.literal(max + "").withStyle(ChatFormatting.GRAY));
                  data.ping = Long.parseLong(server[5]);
               }
            }
         }

         ((JoinMultiplayerScreenAccessor)screen).rose$serverSelectionList().updateOnlineServers(var11);
      }
   }
}
