package dev.rose.module.impl;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.rose.RoseClient;
import dev.rose.compat.Compat;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.notify.Notifications;
import dev.rose.render.font.TextBatch;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import java.util.Locale;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class PerformanceModule extends Module {
   private static final int WINDOW_SECONDS = 30;
   private final BoolSetting lite = this.add(new BoolSetting("lite", false));
   private final BoolSetting suggest = this.add(new BoolSetting("suggest", true)).visibleWhen(() -> !this.lite.isOn());
   private final NumberSetting threshold = this.add(new NumberSetting("threshold", 40.0, 20.0, 90.0, 5.0, "rose.unit.fps"))
      .visibleWhen(() -> !this.lite.isOn() && this.suggest.isOn());
   private final BoolSetting textBatching = this.add(new BoolSetting("text_batching", true)).onChange(this::apply);
   private final ActionSetting diagnostics = this.add(new ActionSetting("diagnostics", 57513, PerformanceModule::copyDiagnostics));
   private final int[] fps = new int[30];
   private int samples;
   private int ticks;
   private boolean suggested;

   public PerformanceModule() {
      super("performance", Category.INTERFACE, true);
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }

   @Override
   public int icon() {
      return 57791;
   }

   @Override
   public void onConfigLoaded() {
      this.apply();
   }

   private void apply() {
      TextBatch.enabled = this.textBatching.isOn() && !Compat.immediatelyFast();
   }

   public static boolean lite() {
      PerformanceModule module = Modules.performance();
      return module != null && module.lite.isOn();
   }

   @Override
   public void onTick() {
      Minecraft mc = Minecraft.getInstance();
      if (!this.lite.isOn() && this.suggest.isOn() && !this.suggested && mc.level != null && mc.screen == null && RoseClient.uiTest() == null) {
         if (++this.ticks % 20 == 0) {
            this.fps[this.samples % 30] = mc.getFps();
            this.samples++;
            if (this.samples >= 30) {
               int sum = 0;

               for (int value : this.fps) {
                  sum += value;
               }

               int average = sum / 30;
               if (average < this.threshold.intValue()) {
                  this.suggested = true;
                  Notifications.show(
                     Notifications.Kind.WARNING,
                     I18n.get("rose.performance.suggest.title", new Object[]{average}),
                     I18n.get("rose.performance.suggest.subtitle", new Object[0])
                  );
               }
            }
         }
      } else {
         this.samples = 0;
      }
   }

   private static void copyDiagnostics() {
      Minecraft mc = Minecraft.getInstance();
      String loader = FabricLoader.getInstance().getModContainer("fabricloader").map(mod -> mod.getMetadata().getVersion().getFriendlyString()).orElse("?");
      String text = String.join(
         "\n",
         "Rose " + RoseClient.fullVersion() + " · Minecraft " + SharedConstants.getCurrentVersion().name() + " · Fabric Loader " + loader,
         "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " · Java " + System.getProperty("java.version"),
         "GPU: " + RenderSystem.getDevice().getRenderer() + " · " + RenderSystem.getDevice().getVendor(),
         "OpenGL: " + RenderSystem.getDevice().getVersion(),
         "CPU: " + GLX._getCpuInfo(),
         String.format(
            Locale.ROOT,
            "Window: %dx%d · GUI scale %.0f · %d FPS",
            mc.getWindow().getWidth(),
            mc.getWindow().getHeight(),
            mc.getWindow().getGuiScale(),
            mc.getFps()
         ),
         "Mods: sodium="
            + Compat.sodium()
            + " iris="
            + Compat.iris()
            + (Compat.iris() ? " (shaderpack=" + Compat.shaderPackActive() + ")" : "")
            + " immediatelyfast="
            + Compat.immediatelyFast()
            + " modmenu="
            + Compat.modMenu(),
         "Rose: lite=" + lite() + " textBatching=" + TextBatch.enabled
      );
      mc.keyboardHandler.setClipboard(text);
      Notifications.show(
         Notifications.Kind.SUCCESS, I18n.get("rose.performance.diagnostics.copied", new Object[0]), RenderSystem.getDevice().getRenderer()
      );
   }
}
