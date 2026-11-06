package dev.rose.module.impl;

import dev.rose.RoseClient;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.TextSetting;


public final class ScreensModule extends Module {
   private final BoolSetting glassButtons;
   private final BoolSetting title;
   private final BoolSetting titleSplash;
   private final BoolSetting titlePlayer;
   private final BoolSetting titleWhatsNew;
   private final BoolSetting pause;
   private final BoolSetting pauseInfo;
   private final BoolSetting loading;
   private final BoolSetting status;
   private final BoolSetting serverList;
   private final BoolSetting pingMs;
   private final BoolSetting inventory;
   private final TextSetting seenVersion;

   public ScreensModule() {
      super("screens", Category.INTERFACE, true);
      this.section("everywhere", 58406);
      this.glassButtons = this.add(new BoolSetting("glass_buttons", true));
      this.section("title", 57589);
      this.title = this.add(new BoolSetting("title", true));
      this.titlePlayer = this.add(new BoolSetting("title_player", true)).visibleWhen(this::titleOn);
      this.titleWhatsNew = this.add(new BoolSetting("title_whats_new", true)).visibleWhen(this::titleOn);
      this.titleSplash = this.add(new BoolSetting("title_splash", true)).visibleWhen(this::titleOn);
      this.section("pause", 57646);
      this.pause = this.add(new BoolSetting("pause", true));
      this.pauseInfo = this.add(new BoolSetting("pause_info", true)).visibleWhen(() -> this.pause.isOn());
      this.section("loading", 58006);
      this.loading = this.add(new BoolSetting("loading", true));
      this.status = this.add(new BoolSetting("status", true));
      this.section("servers", 57683);
      this.serverList = this.add(new BoolSetting("server_list", true));
      this.pingMs = this.add(new BoolSetting("ping_ms", true)).visibleWhen(() -> this.serverList.isOn());
      this.section("inventory", 58056);
      this.inventory = this.add(new BoolSetting("inventory", true));
      this.seenVersion = this.add(new TextSetting("seen_version", "", 32)).visibleWhen(() -> false);
   }

   private boolean titleOn() {
      return this.title.isOn();
   }

   @Override
   public int icon() {
      return 57629;
   }

   private static ScreensModule module() {
      ScreensModule module = Modules.screens();
      return module != null && module.isEnabled() ? module : null;
   }

   public static boolean glassButtons() {
      ScreensModule m = module();
      return m != null && m.glassButtons.isOn();
   }

   public static boolean titleScreen() {
      ScreensModule m = module();
      return m != null && m.title.isOn();
   }

   public static boolean titlePlayer() {
      ScreensModule m = module();
      return m != null && m.titlePlayer.isOn();
   }

   public static boolean titleSplash() {
      ScreensModule m = module();
      return m != null && m.titleSplash.isOn();
   }

   public static boolean titleWhatsNew() {
      ScreensModule m = module();
      return m != null && m.titleWhatsNew.isOn() && !RoseClient.version().equals(m.seenVersion.get());
   }

   public static void dismissWhatsNew() {
      ScreensModule m = Modules.screens();
      if (m != null) {
         m.seenVersion.set(RoseClient.version());
      }
   }

   public static boolean pauseScreen() {
      ScreensModule m = module();
      return m != null && m.pause.isOn();
   }

   public static boolean pauseInfo() {
      ScreensModule m = module();
      return m != null && m.pauseInfo.isOn();
   }

   public static boolean loadingScreen() {
      ScreensModule m = module();
      return m != null && m.loading.isOn();
   }

   public static boolean statusScreens() {
      ScreensModule m = module();
      return m != null && m.status.isOn();
   }

   public static boolean serverList() {
      ScreensModule m = module();
      return m != null && m.serverList.isOn();
   }

   public static boolean inventory() {
      ScreensModule m = module();
      return m != null && m.inventory.isOn();
   }

   public static boolean pingMs() {
      ScreensModule m = module();
      return m != null && m.pingMs.isOn();
   }

   public static void crashed(RuntimeException e) {
      ScreensModule m = Modules.screens();
      if (m != null && m.isEnabled()) {
         Modules.fail(m, e);
      } else {
         RoseClient.LOGGER.error("[Rose] screens", e);
      }
   }
}
