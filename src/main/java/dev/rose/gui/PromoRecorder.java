package dev.rose.gui;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.looks.Look;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.render.Theme;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.Setting;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public final class PromoRecorder {
   private static final Path FOLDER = Path.of(
      System.getProperty("java.io.tmpdir"), "rose-promo", new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date())
   );
   private static final double FPS = 24.0;
   private static final int QUEUE_LIMIT = 24;
   private static final float WARMUP = 2.5F;
   private static PromoRecorder instance;
   private static boolean entering;
   private final Minecraft mc = Minecraft.getInstance();
   private final ExecutorService writers = Executors.newFixedThreadPool(3, run -> {
      Thread thread = new Thread(run, "Rose Promo Writer");
      thread.setDaemon(true);
      return thread;
   });
   private final AtomicInteger pending = new AtomicInteger();
   private final Deque<String> log = new ArrayDeque<>();
   private long startedAt;
   private long lastFrameAt;
   private int frames;
   private int scene = -1;
   private boolean done;
   private static final float[] SCENE_LENGTH = new float[]{3.5F, 4.0F, 4.0F, 4.0F, 5.0F, 4.5F, 4.0F, 4.0F, 3.0F};
   private static final float TOTAL = total();
   private float lastPulse;

   private PromoRecorder() {
   }

   public static boolean handles(String mode) {
      return "promo".equals(mode);
   }

   public static void tick(Minecraft mc) {
      if (handles(RoseClient.uiTest())) {
         if (mc.level == null) {
            if (!entering && mc.screen instanceof TitleScreen) {
               entering = true;
               WorldTest.enterWorldForTest(mc);
            }
         } else {
            if (instance == null) {
               instance = new PromoRecorder();
            }

            instance.run();
         }
      }
   }

   public static void frame() {
      if (instance != null) {
         instance.capture();
      }
   }

   private void run() {
      LocalPlayer player = this.mc.player;
      if (player != null) {
         if (this.startedAt == 0L) {
            this.startedAt = System.nanoTime();
            this.mc.options.pauseOnLostFocus = false;
            this.mc.options.hideGui = false;
            if (!this.mc.getWindow().isFullscreen()) {
               this.mc.getWindow().toggleFullScreen();
            }

            this.prepare();
            RoseClient.LOGGER.info("[Rose][promo] recording to {}", FOLDER);
         }

         if (this.mc.screen instanceof PauseScreen) {
            this.mc.setScreen(null);
         }

         if (this.mc.screen == null && this.mc.mouseHandler.isMouseGrabbed()) {
            this.mc.mouseHandler.releaseMouse();
         }

         float time = (float)(System.nanoTime() - this.startedAt) / 1.0E9F - 2.5F;
         if (time < 0.0F) {
            this.mc.gui.getChat().clearMessages(true);
            if (this.scene < 0) {
               this.scene = 0;
               this.enterScene(0, player);
            }
         } else {
            int wanted = sceneAt(time);
            if (wanted != this.scene) {
               this.scene = wanted;

               try {
                  this.enterScene(this.scene, player);
               } catch (RuntimeException e) {
                  RoseClient.LOGGER.error("[Rose][promo] scene {} failed", this.scene, e);
               }
            }

            try {
               this.live(this.scene, time, player);
            } catch (RuntimeException e) {
               RoseClient.LOGGER.error("[Rose][promo] scene {} tick failed", this.scene, e);
            }

            if (time > TOTAL && !this.done) {
               this.finish();
            }
         }
      }
   }

   private static float total() {
      float sum = 0.0F;

      for (float length : SCENE_LENGTH) {
         sum += length;
      }

      return sum;
   }

   private static int sceneAt(float time) {
      float sum = 0.0F;

      for (int i = 0; i < SCENE_LENGTH.length; i++) {
         sum += SCENE_LENGTH[i];
         if (time < sum) {
            return i;
         }
      }

      return SCENE_LENGTH.length - 1;
   }

   private void enterScene(int scene, LocalPlayer player) {
      this.mc.gui.getChat().clearMessages(true);
      switch (scene) {
         case 0:
            this.command("gamerule sendCommandFeedback false");
            this.command("time set 1000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -20 -61 -20 20 -50 20 minecraft:air");
            this.command("fill -20 -61 -20 20 -61 20 minecraft:grass_block");
            this.command("fill -3 -60 8 3 -57 8 minecraft:deepslate_bricks");
            this.command("setblock 0 -60 6 minecraft:chest");
            this.command("gamemode creative @p");
            this.command("tp @p 0 -60 -4 0 2");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.command("item replace entity @p hotbar.1 with minecraft:totem_of_undying");
            this.command("item replace entity @p armor.chest with minecraft:netherite_chestplate");
            player.getInventory().setSelectedSlot(0);
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.look(player, 0.0F, 2.0F);
            this.apply(Look.SIGNATURE);
            break;
         case 1:
            MenuScreen.remember(Modules.byId("hit_effects"));
            this.mc.setScreen(new MenuScreen(null));
            break;
         case 2:
            this.mc.setScreen(null);
            this.apply(Look.BLOOD);
            this.command("time set 15000");
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            this.look(player, 0.0F, -8.0F);
            break;
         case 3:
            this.apply(Look.POLAR);
            break;
         case 4:
            this.apply(Look.SIGNATURE);
            this.command("time set 13000");
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.command("summon minecraft:villager 0 -60 1.6 {NoAI:1b,Silent:1b}");
            this.command("summon minecraft:villager -1.6 -60 2.4 {NoAI:1b,Silent:1b}");
            this.command("summon minecraft:villager 1.6 -60 2.4 {NoAI:1b,Silent:1b}");
            this.look(player, 0.0F, 4.0F);
            break;
         case 5:
            this.command("kill @e[type=!minecraft:player]");
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            this.enable("aura", "wings", "pet", "trail", "jump_circles", "afterimage");
            this.setChoice("aura", "style", "SURGE");
            this.setChoice("wings", "style", "ANGEL");
            this.setChoice("pet", "pet", "DRAGON");
            break;
         case 6:
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.enable("swing_trail", "item_glow");
            this.look(player, 0.0F, 6.0F);
            break;
         case 7:
            this.command("time set 18000");
            this.apply(Look.POLAR);
            this.mc.options.hideGui = true;
            this.look(player, 0.0F, -22.0F);
            break;
         case 8:
            this.mc.options.hideGui = false;
            this.command("time set 1000");
            this.apply(Look.SIGNATURE);
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            if (Modules.island() != null) {
               Modules.island().moment(58494, Theme.accent(), "Rose", "визуал для FunTime", 4.0F);
            }
      }
   }

   private void live(int scene, float time, LocalPlayer player) {
      float local = time - before(scene);
      switch (scene) {
         case 0:
            player.setYRot(Mth.rotLerp(0.02F, player.getYRot(), 18.0F));
            break;
         case 1:
            if (this.mc.screen instanceof MenuScreen menu) {
               if (local > 1.2F && local < 1.35F) {
                  menu.testClick("nav:cosmetics", 0);
               } else if (local > 2.2F && local < 2.35F) {
                  menu.testClick("module:aura", 1);
               } else if (local > 3.2F && local < 3.35F) {
                  menu.testClick("nav:visuals", 0);
               }
            }
            break;
         case 2:
         case 3:
            player.setYRot(player.getYRot() + 0.35F);
            break;
         case 4:
            Entity target = this.nearest();
            if (target != null && this.pulse(local, 0.55F)) {
               this.mc.gameMode.attack(player, target);
               player.swing(InteractionHand.MAIN_HAND);
            }
            break;
         case 5:
            player.setYRot(player.getYRot() + 0.5F);
            if (this.pulse(local, 1.5F) && player.onGround()) {
               player.jumpFromGround();
            }
            break;
         case 6:
            if (this.pulse(local, 0.45F)) {
               player.swing(InteractionHand.MAIN_HAND);
            }
            break;
         case 7:
            player.setYRot(player.getYRot() + 0.25F);
            break;
         case 8:
            player.setYRot(player.getYRot() + 0.4F);
      }
   }

   private static float before(int scene) {
      float sum = 0.0F;

      for (int i = 0; i < scene; i++) {
         sum += SCENE_LENGTH[i];
      }

      return sum;
   }

   private boolean pulse(float local, float period) {
      if (local - this.lastPulse < period) {
         return false;
      }

      this.lastPulse = local;
      return true;
   }

   private Entity nearest() {
      Entity best = null;
      double bestDistance = Double.MAX_VALUE;

      for (Entity entity : this.mc.level.entitiesForRendering()) {
         if (entity != this.mc.player && entity instanceof LivingEntity) {
            double distance = entity.distanceToSqr(this.mc.player);
            if (distance < bestDistance) {
               bestDistance = distance;
               best = entity;
            }
         }
      }

      return best;
   }

   private void capture() {
      if (!this.done && this.startedAt != 0L) {
         if (!((float)(System.nanoTime() - this.startedAt) / 1.0E9F < 2.5F)) {
            long now = System.nanoTime();
            if (this.lastFrameAt == 0L || !(now - this.lastFrameAt < 4.1666666666666664E7)) {
               if (this.pending.get() < 24) {
                  this.lastFrameAt = now;
                  String name = String.format(Locale.ROOT, "frame_%05d.jpg", ++this.frames);
                  this.pending.incrementAndGet();

                  try {
                     Screenshot.takeScreenshot(this.mc.getMainRenderTarget(), image -> this.writers.execute(() -> {
                        try {
                           NativeImage owned = image;

                           try {
                              Files.createDirectories(FOLDER);
                              write(owned, FOLDER.resolve(name));
                           } catch (Throwable var12) {
                              if (owned != null) {
                                 try {
                                    owned.close();
                                 } catch (Throwable var11) {
                                    var12.addSuppressed(var11);
                                 }
                              }

                              throw var12;
                           }

                           if (owned != null) {
                              owned.close();
                           }
                        } catch (Exception e) {
                           RoseClient.LOGGER.error("[Rose][promo] cannot write {}", name, e);
                        } finally {
                           this.pending.decrementAndGet();
                        }
                     }));
                  } catch (RuntimeException e) {
                     this.pending.decrementAndGet();
                     RoseClient.LOGGER.error("[Rose][promo] cannot grab frame", e);
                  }
               }
            }
         }
      }
   }

   private static void write(NativeImage image, Path file) throws IOException {
      int width = image.getWidth();
      int height = image.getHeight();
      BufferedImage out = new BufferedImage(width, height, 1);
      int[] row = new int[width];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            row[x] = image.getPixel(x, y) & 16777215;
         }

         out.setRGB(0, y, width, 1, row, 0, width);
      }

      ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
      ImageWriteParam params = writer.getDefaultWriteParam();
      params.setCompressionMode(2);
      params.setCompressionQuality(0.93F);

      try (ImageOutputStream stream = ImageIO.createImageOutputStream(file.toFile())) {
         writer.setOutput(stream);
         writer.write(null, new IIOImage(out, null, null), params);
      } finally {
         writer.dispose();
      }
   }

   private void finish() {
      this.done = true;
      this.writers.shutdown();
      float seconds = (float)(System.nanoTime() - this.startedAt) / 1.0E9F;
      RoseClient.LOGGER
         .info(
            "[Rose][promo] finished: {} frames in {} s, {} fps, folder {}",
            new Object[]{this.frames, String.format(Locale.ROOT, "%.1f", seconds), String.format(Locale.ROOT, "%.1f", this.frames / seconds), FOLDER}
         );

      for (String line : this.log) {
         RoseClient.LOGGER.info("[Rose][promo] {}", line);
      }
   }

   private void prepare() {
      this.disable("aspect_ratio", "motion_blur", "film_effects");
      this.enable("bloom", "sky", "hit_effects", "block_outline", "kill_effects", "island", "watermark", "chunk_fade");
   }

   private void apply(Look look) {
      if (Modules.looks() != null) {
         Modules.looks().apply(look, false);
      }
   }

   private void look(LocalPlayer player, float yaw, float pitch) {
      player.setYRot(yaw);
      player.setXRot(pitch);
      player.yBodyRot = yaw;
      player.yHeadRot = yaw;
   }

   private void command(String command) {
      if (this.mc.player != null && this.mc.player.connection != null) {
         this.mc.player.connection.sendCommand(command);
      }
   }

   private void enable(String... ids) {
      for (String id : ids) {
         Module module = Modules.byId(id);
         if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
         }
      }
   }

   private void disable(String... ids) {
      for (String id : ids) {
         Module module = Modules.byId(id);
         if (module != null && module.isEnabled()) {
            module.setEnabled(false);
         }
      }
   }

   private void setChoice(String moduleId, String settingId, String value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         for (Setting<?> setting : module.settings()) {
            if (setting.id().equals(settingId) && setting instanceof ChoiceSetting<?> choice) {
               for (Object option : choice.options()) {
                  if (((Enum)option).name().equals(value)) {
                     ((ChoiceSetting)choice).set((Enum)option);
                     return;
                  }
               }
            }
         }
      }
   }
}
