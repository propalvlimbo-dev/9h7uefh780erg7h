package dev.rose.gui.menu;

import com.google.gson.JsonPrimitive;
import dev.rose.RoseClient;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.setting.Setting;
import dev.rose.sound.BuiltinSounds;
import dev.rose.sound.PackSounds;
import dev.rose.sound.SoundLibrary;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.sounds.JOrbisAudioStream;


final class MenuTest {
   private final MenuScreen screen;
   private long nextAt = System.currentTimeMillis() + 4500L;
   private int step;
   private int frames;
   private long renderNanos;
   private long measureStart = System.nanoTime();

   MenuTest(MenuScreen screen) {
      this.screen = screen;
      RoseClient.LOGGER.info("[Rose][uitest] started");
   }

   void tick() {
      this.frames++;
   }

   void frameTime(long nanos) {
      this.renderNanos += nanos;
   }

   void clientTick() {
      long now = System.currentTimeMillis();
      if (now >= this.nextAt) {
         long wait = this.run(this.step);
         if (wait >= 0L) {
            double seconds = (System.nanoTime() - this.measureStart) / 1.0E9;
            RoseClient.LOGGER
               .info(
                  "[Rose][uitest] step {} done, {} fps, menu {} ms/frame",
                  new Object[]{
                     this.step, Math.round(this.frames / seconds), String.format(Locale.ROOT, "%.2f", this.renderNanos / 1000000.0 / Math.max(1, this.frames))
                  }
               );
            this.frames = 0;
            this.renderNanos = 0L;
            this.measureStart = System.nanoTime();
            this.step++;
            this.nextAt = now + wait;
         }
      }
   }

   private long run(int index) {
      switch (index) {
         case 0:
            shot("open");
            break;
         case 1:
            this.screen.testHover("module:crosshair");
            break;
         case 2:
            shot("hover");
            this.screen.testClick("nav:cosmetics", 0);
            break;
         case 3:
            this.screen.testClick("module:wings", 1);
            break;
         case 4:
            shot("cosmetics");
            break;
         case 5:
            this.screen.testType("цвет");
            break;
         case 6:
            shot("search");
            break;
         case 7:
            this.screen.testKey(257, 0);
            break;
         case 8:
            shot("search_pick");
            break;
         case 9:
            this.screen.testKey(256, 0);
            break;
         case 10:
            this.screen.testHover("nav:visuals");
            break;
         case 11:
            this.screen.testClick("nav:visuals", 0);
            break;
         case 12:
            this.screen.testClick("module:crosshair", 1);
            break;
         case 13:
            this.screen.testClick("setting:crosshair.color", 0);
            break;
         case 14:
            shot("color_popup");
            break;
         case 15:
            this.screen.testKey(256, 0);
            break;
         case 16:
            this.screen.testClick("setting:crosshair.style", 0);
            break;
         case 17:
            shot("dropdown");
            break;
         case 18:
            if (this.screen.testPopupOpen()) {
               this.screen.testKey(256, 0);
            }
            break;
         case 19:
            this.screen.testClick("module:crosshair", 0);
            break;
         case 20:
            this.screen.testClick("module:hand", 1);
            break;
         case 21:
            shot("hand");
            break;
         case 22:
            this.screen.testDrag("window:move", 0.5F, 0.5F, 90.0F, 30.0F);
            break;
         case 23:
            shot("dragged");
            break;
         case 24:
            this.screen.testClick("nav:interface", 0);
            set("clickgui", "window_style", "SOLID");
            break;
         case 25:
            this.screen.testClick("module:theme", 1);
            break;
         case 26:
            shot("theme_solid");
            break;
         case 27:
            set("clickgui", "window_style", "GLASS");
            this.screen.testClick("module:profiles", 1);
            break;
         case 28:
            shot("profiles");
            break;
         case 29:
            this.screen.testClick("module:clickgui", 1);
            Modules.clickGui().saveWindow(0.5F, 0.5F, 620.0F, 380.0F);
            break;
         case 30:
            this.screen.testClick("nav:interface", 0);
            this.screen.testClick("module:keys", 1);
            break;
         case 31:
            shot("keys_page");
            break;
         case 32:
            shot("clickgui");
            break;
         case 33:
            this.screen.testClick("nav:sounds", 0);
            int broken = 0;

            for (PackSounds sound : PackSounds.values()) {
               try (InputStream in = Minecraft.getInstance().getResourceManager().open(sound.file())) {
                  JOrbisAudioStream stream = new JOrbisAudioStream(new BufferedInputStream(in));
                  int bytes = 0;

                  for (int chunk = 0; chunk < 8; chunk++) {
                     ByteBuffer data = stream.read(65536);
                     if (data == null) {
                        break;
                     }

                     bytes += data.remaining();
                  }

                  if (bytes <= 0) {
                     broken++;
                     RoseClient.LOGGER.error("[Rose][uitest] pack sound {} is silent", sound.id());
                  } else {
                     RoseClient.LOGGER
                        .info(
                           "[Rose][uitest] pack sound {} ok: {} Hz, {} ch, {} bytes",
                           new Object[]{sound.id(), (int)stream.getFormat().getSampleRate(), stream.getFormat().getChannels(), bytes}
                        );
                  }
               } catch (Exception e) {
                  broken++;
                  RoseClient.LOGGER.error("[Rose][uitest] pack sound {} FAILED: {}", sound.id(), e.toString());
               }
            }

            RoseClient.LOGGER.info("[Rose][uitest] pack sounds checked, broken: {}", broken);

            for (BuiltinSounds sound : BuiltinSounds.values()) {
               float[] data = sound.render();
               float peak = 0.0F;
               boolean bad = false;

               for (float sample : data) {
                  if (Float.isNaN(sample)) {
                     bad = true;
                  }

                  peak = Math.max(peak, Math.abs(sample));
               }

               RoseClient.LOGGER
                  .info("[Rose][uitest] sound {} {} ms peak {} {}", new Object[]{sound.id(), data.length * 1000 / 44100, peak, bad ? "NaN!" : "ok"});
            }
            break;
         case 34:
            this.screen.testClick("module:hit_sounds", 1);
            break;
         case 35:
            shot("sounds");
            SoundLibrary.play("tick", 0.6F, 1.0F);
            break;
         case 36:
            SoundLibrary.play("fanfare", 0.6F, 1.0F);
            break;
         case 37:
            this.screen.testMouseAway();
            this.screen.testKey(256, 0);
            RoseClient.LOGGER.info("[Rose][uitest] finished");
            break;
         default:
            return -1L;
      }

      return 900L;
   }

   private static void shot(String name) {
      Minecraft mc = Minecraft.getInstance();
      Screenshot.grab(mc.gameDirectory, "rose_menu_" + name + ".png", mc.getMainRenderTarget(), 1, message -> {});
      RoseClient.LOGGER.info("[Rose][uitest] shot {}", name);
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
}
