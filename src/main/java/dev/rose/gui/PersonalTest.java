package dev.rose.gui;

import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.config.ConfigManager;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.ClickGuiModule;
import dev.rose.server.ServerProfiles;
import dev.rose.setting.Setting;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;


public final class PersonalTest {
   private static long stepAt = -1L;
   private static int step;
   private static boolean done;
   private static MenuScreen menu;
   private static CharacterRigEditor editor;
   private static Path character;

   private PersonalTest() {
   }

   public static void tick(Minecraft mc) {
      if ("personal".equals(RoseClient.uiTest()) && !done && mc.getOverlay() == null) {
         long now = System.currentTimeMillis();
         if (stepAt < 0L) {
            if (!(mc.screen instanceof TitleScreen)) {
               return;
            }

            stepAt = now + 4000L;
         }

         if (now >= stepAt) {
            long next = run(mc, step);
            if (next >= 0L) {
               RoseClient.LOGGER.info("[Rose][personaltest] step {} done", step);
               step++;
               stepAt = now + next;
            }
         }
      }
   }

   private static long run(Minecraft mc, int index) {
      switch (index) {
         case 0:
            try {
               Path background = ClickGuiModule.BACKGROUNDS_DIR.resolve("rose_test_sunset.png");
               character = ClickGuiModule.CHARACTERS_DIR.resolve("rose_test_character.png");
               Files.createDirectories(background.getParent());
               Files.createDirectories(character.getParent());
               paintBackground(background);
               paintCharacter(character);
            } catch (IOException e) {
               RoseClient.LOGGER.error("[Rose][personaltest] cannot write test pictures", e);
            }

            set("clickgui", "background", "IMAGE");
            set("clickgui", "background_file", "rose_test_sunset.png");
            set("clickgui", "character", "NONE");
            return 2500L;
         case 1:
            shot(mc, "bg_title");
            menu = new MenuScreen(mc.screen);
            mc.setScreen(menu);
            return 2200L;
         case 2:
            menu.testClick("nav:interface", 0);
            break;
         case 3:
            menu.testClick("module:theme", 1);
            break;
         case 4:
            shot(mc, "themes");
            menu.testClick("themes:builtin:SAKURA", 0);
            return 1400L;
         case 5:
            shot(mc, "theme_sakura");
            menu.testClick("themes:builtin:MIDNIGHT", 0);
            return 1400L;
         case 6:
            shot(mc, "theme_midnight");
            menu.testClick("themes:builtin:ROSE", 0);
            menu.testScroll("page:view", -200.0);
            return 1500L;
         case 7:
            shot(mc, "theme_fonts");
            return 600L;
         case 8:
            menu.testClick("module:sounds", 1);
            break;
         case 9:
            shot(mc, "sounds");
            ConfigManager.saveProfile("FunTime PvP");
            ServerProfiles.bind("funtime.su", "FunTime PvP");
            return 1000L;
         case 10:
            menu.testClick("module:profiles", 1);
            break;
         case 11:
            menu.testScroll("page:view", -6.0);
            return 1100L;
         case 12:
            shot(mc, "profiles_servers");
            set("clickgui", "character", "CUSTOM");
            set("clickgui", "character_file", "rose_test_character.png");
            editor = new CharacterRigEditor(menu, character);
            mc.setScreen(editor);
            return 1200L;
         case 13:
            editor.testMark(CharacterRigEditor.Tool.HEAD, 200.0F, 150.0F, 200.0F, 238.0F);
            editor.testMark(CharacterRigEditor.Tool.NECK, 200.0F, 252.0F, 200.0F, 252.0F);
            editor.testMark(CharacterRigEditor.Tool.BREATH, 0.0F, 280.0F, 0.0F, 470.0F);
            editor.testMark(CharacterRigEditor.Tool.TAIL, 118.0F, 150.0F, 70.0F, 600.0F);
            editor.testMark(CharacterRigEditor.Tool.TAIL, 282.0F, 150.0F, 330.0F, 600.0F);
            editor.testMark(CharacterRigEditor.Tool.EYE, 158.0F, 150.0F, 182.0F, 176.0F);
            editor.testMark(CharacterRigEditor.Tool.EYE, 218.0F, 150.0F, 242.0F, 176.0F);
            editor.testMark(CharacterRigEditor.Tool.HAIR, 90.0F, 400.0F, 90.0F, 400.0F);
            editor.testMark(CharacterRigEditor.Tool.TAIL, 118.0F, 150.0F, 118.0F, 150.0F);
            return 1600L;
         case 14:
            shot(mc, "rig_editor");
            editor.testSave();
            return 800L;
         case 15:
            mc.setScreen(new MenuScreen(new TitleScreen()));
            return 2600L;
         case 16:
            shot(mc, "character_menu");
            RoseClient.LOGGER.info("[Rose][personaltest] finished");
            done = true;
      }

      return 1100L;
   }

   private static void set(String moduleId, String settingId, String value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         for (Setting<?> setting : module.settings()) {
            if (setting.id().equals(settingId)) {
               setting.fromJson(new JsonPrimitive(value));
            }
         }
      }
   }

   private static void shot(Minecraft mc, String name) {
      Screenshot.grab(mc.gameDirectory, "rose_" + name + ".png", mc.getMainRenderTarget(), 1, message -> {});
      RoseClient.LOGGER.info("[Rose][personaltest] shot {}", name);
   }

   private static void paintBackground(Path file) throws IOException {
      int w = 1280;
      int h = 720;
      NativeImage image = new NativeImage(w, h, false);

      try {
         for (int y = 0; y < h; y++) {
            float t = (float)y / h;
            int sky = lerp(-14733483, -810901, (float)Math.pow(t, 1.4));

            for (int x = 0; x < w; x++) {
               int color = sky;
               float sun = (float)Math.hypot(x - w * 0.68F, y - h * 0.58F);
               if (sun < 70.0F) {
                  color = lerp(color, -7248, Math.clamp((70.0F - sun) / 4.0F, 0.0F, 1.0F));
               } else if (sun < 190.0F) {
                  color = lerp(color, -15750, 0.25F * (1.0F - (sun - 70.0F) / 120.0F));
               }

               for (int layer = 0; layer < 3; layer++) {
                  double ridge = h * (0.62 + layer * 0.12)
                     + Math.sin(x * (0.004 + layer * 0.003) + layer * 1.7) * (40 - layer * 8)
                     + Math.sin(x * (0.011 + layer * 0.004) + layer) * (18 - layer * 4);
                  if (y > ridge) {
                     color = new int[]{-9745810, -12636593, -14542288}[layer];
                  }
               }

               image.setPixelABGR(x, y, abgr(color));
            }
         }

         image.writeToFile(file);
      } catch (Throwable var14) {
         try {
            image.close();
         } catch (Throwable var13) {
            var14.addSuppressed(var13);
         }

         throw var14;
      }

      image.close();
   }

   private static void paintCharacter(Path file) throws IOException {
      int w = 400;
      int h = 800;
      int hair = -33098;
      int hairDark = -2072422;
      int skin = -664376;
      int dress = -10794102;
      int dressLight = -9083992;
      int eye = -13948096;
      NativeImage image = new NativeImage(w, h, true);

      try {
         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               int color = 0;

               for (int side : new int[]{-1, 1}) {
                  float cx = 200.0F + side * (95.0F + (y - 150) * 0.12F);
                  float halfWidth = y < 150 ? 0.0F : 34.0F * (float)Math.sin(Math.min(1.0F, (y - 150) / 470.0F) * Math.PI);
                  if (y >= 150 && y <= 620 && Math.abs(x - cx) < halfWidth) {
                     color = (x + y) % 23 < 3 ? hairDark : hair;
                  }
               }

               if (y > 640 && y < 790 && (Math.abs(x - 170) < 16 || Math.abs(x - 230) < 16)) {
                  color = skin;
               }

               if (y > 280 && y < 520 && (Math.abs(x - (118.0F - (y - 280) * 0.08F)) < 15.0F || Math.abs(x - (282.0F + (y - 280) * 0.08F)) < 15.0F)) {
                  color = skin;
               }

               float dressHalf = 58.0F + (y - 250) * 0.3F;
               if (y > 250 && y < 660 && Math.abs(x - 200) < dressHalf) {
                  color = Math.abs(x - 200) < 12 ? dressLight : dress;
               }

               if (y > 215 && y < 262 && Math.abs(x - 200) < 18) {
                  color = skin;
               }

               float head = (float)Math.hypot(x - 200, y - 150);
               if (head < 82.0F) {
                  color = skin;
               }

               if (head < 88.0F && y < 140.0F + (x - 200) * (x - 200) / 400.0F) {
                  color = hair;
               }

               if (Math.hypot((x - 170) / 11.0, (y - 163) / 14.0) < 1.0 || Math.hypot((x - 230) / 11.0, (y - 163) / 14.0) < 1.0) {
                  color = eye;
               }

               if (Math.hypot(x - 200, (y - 196) * 2.2) < 12.0 && y > 196) {
                  color = -2527110;
               }

               if (color != 0) {
                  image.setPixelABGR(x, y, abgr(color));
               }
            }
         }

         image.writeToFile(file);
      } catch (Throwable var20) {
         try {
            image.close();
         } catch (Throwable var19) {
            var20.addSuppressed(var19);
         }

         throw var20;
      }

      image.close();
   }

   private static int lerp(int a, int b, float t) {
      return ColorUtil.lerp(a, b, t);
   }

   private static int abgr(int argb) {
      return argb & -16711936 | argb >> 16 & 0xFF | (argb & 0xFF) << 16;
   }
}
