package dev.rose;

import dev.rose.config.BuildLinks;
import dev.rose.config.ConfigManager;
import dev.rose.config.CrashGuard;
import dev.rose.config.UpdateChecker;
import dev.rose.gui.HudEditorScreen;
import dev.rose.gui.RoseMenuButton;
import dev.rose.gui.PersonalTest;
import dev.rose.gui.PromoRecorder;
import dev.rose.gui.ScreensTest;
import dev.rose.gui.WorldTest;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.gui.screen.GlassSearchField;
import dev.rose.gui.screen.PauseDesign;
import dev.rose.gui.screen.PlayerPreview;
import dev.rose.gui.screen.TitleDesign;
import dev.rose.hud.GameStats;
import dev.rose.hud.TargetTracker;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.BlockOutlineModule;
import dev.rose.notify.Notifications;
import dev.rose.render.GlowEngine;
import dev.rose.render.Shaders;
import dev.rose.render.font.Fonts;
import dev.rose.render.world.CosmeticsLayer;
import dev.rose.render.world.WorldCosmetics;
import dev.rose.server.ServerProfiles;
import java.util.Locale;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterEntities;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeBlockOutline;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.EndMain;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.StartMain;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterRender;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RoseClient implements ClientModInitializer {
   public static final String MOD_ID = "rose";
   public static final Logger LOGGER = LoggerFactory.getLogger("Rose");
   private static final boolean DEMO = Boolean.getBoolean("rose.demo");
   private static final String UI_TEST = readUiTest();
   private static boolean demoOpened;
   private static boolean welcomed;

   public static String uiTest() {
      return UI_TEST;
   }

   private static String readUiTest() {
      String value = System.getProperty("rose.uitest", "false").trim();
      return !value.isEmpty() && !value.equalsIgnoreCase("false") ? value.toLowerCase(Locale.ROOT) : null;
   }

   public static String version() {
      String full = fullVersion();
      int plus = full.indexOf(43);
      return plus > 0 ? full.substring(0, plus) : full;
   }

   public static String fullVersion() {
      return FabricLoader.getInstance().getModContainer("rose").map(mod -> mod.getMetadata().getVersion().getFriendlyString()).orElse("dev");
   }

   public void onInitializeClient() {
      Shaders.register();
      PlayerPreview.register();
      Fonts.preload();
      Modules.init();
      ClientLifecycleEvents.CLIENT_STARTED.register((ClientStarted)client -> {
         ConfigManager.load();
         Modules.onConfigLoaded();
         ServerProfiles.restoreAfterCrash();
         CrashGuard.onStart();
         UpdateChecker.start(Modules.notifications() == null || Modules.notifications().checkUpdates());
      });
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> {
         ConfigManager.save();
         CrashGuard.onStop();
      });
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         Modules.onTick();
         ConfigManager.tick();
         if (UI_TEST != null) {
            WorldTest.tick(client);
            PromoRecorder.tick(client);
            ScreensTest.tick(client);
            PersonalTest.tick(client);
         }
      });
      HudRenderCallback.EVENT.register((HudRenderCallback)(graphics, deltaTracker) -> {
         Modules.onHud(graphics, deltaTracker);
         if (Minecraft.getInstance().screen == null) {
            Notifications.render(graphics);
         }
      });
      ScreenEvents.AFTER_INIT.register((AfterInit)(client, screen, width, height) -> {
         if (UI_TEST != null) {
            ScreenEvents.afterRender(screen).register((AfterRender)(current, graphics, mouseX, mouseY, delta) -> {
               if (WorldTest.tooltipDemo) {
                  graphics.setTooltipForNextFrame(client.font, new ItemStack(Items.DIAMOND_SWORD), width / 2 + 40, height / 2 - 30);
                  graphics.renderDeferredElements();
               }
            });
         }
      });
      ScreenEvents.AFTER_INIT
         .register(
            (AfterInit)(client, screen, width, height) -> {
               ScreenEvents.afterRender(screen).register((AfterRender)(current, graphics, mouseX, mouseY, delta) -> Notifications.render(graphics));
               if (Modules.containerSearch() != null) {
                  GlassSearchField search = Modules.containerSearch().attach(screen);
                  if (search != null) {
                     Screens.getButtons(screen).add(search);
                  }
               }

               if (screen instanceof TitleScreen title) {
                  Screens.getButtons(screen).add(new RoseMenuButton(screen, width - 74, 8, 66, 20));
                  TitleDesign.layout(title, width, height);
               } else if (screen instanceof PauseScreen pause && pause.showsPauseMenu()) {
                  if (PauseDesign.active()) {
                     PauseDesign.layout(pause, width, height);
                  } else {
                     Screens.getButtons(screen).add(new RoseMenuButton(screen, width - 74, 8, 66, 20));
                  }
               }

               if (!welcomed && screen instanceof TitleScreen) {
                  welcomed = true;
                  Notifications.show(
                     Notifications.Kind.INFO,
                     I18n.get("rose.notify.welcome.title", new Object[]{version()}),
                     I18n.get("rose.notify.welcome.subtitle", new Object[0])
                  );
               }

               if (DEMO
                  && !demoOpened
                  && screen instanceof TitleScreen
                  && !WorldTest.handles(UI_TEST)
                  && !PromoRecorder.handles(UI_TEST)
                  && !"screens".equals(UI_TEST)
                  && !"personal".equals(UI_TEST)) {
                  demoOpened = true;
                  if ("hud".equals(UI_TEST)) {
                     client.schedule(() -> client.setScreen(new HudEditorScreen(() -> screen)));
                  } else {
                     client.schedule(() -> client.setScreen(new MenuScreen(screen)));
                  }
               }
            }
         );
      ClientPlayConnectionEvents.JOIN.register((Join)(handler, sender, client) -> client.execute(() -> {
         Modules.serverWarning().onJoin();
         if (Modules.eventSounds() != null) {
            Modules.eventSounds().onJoin();
         }

         ServerProfiles.onJoin();
         GameStats.sessionStarted();
      }));
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> {
         GameStats.sessionEnded();
         client.execute(ServerProfiles::onLeave);
      });
      TargetTracker.register();
      WorldRenderEvents.START_MAIN.register((StartMain)context -> GlowEngine.beginWorld());
      WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)context -> GlowEngine.refreshDepth());
      WorldRenderEvents.AFTER_ENTITIES.register(WorldCosmetics::render);
      LivingEntityFeatureRendererRegistrationCallback.EVENT
         .register((LivingEntityFeatureRendererRegistrationCallback)(entityType, renderer, helper, context) -> {
            if (renderer instanceof AvatarRenderer<?> player) {
               helper.register(new CosmeticsLayer(player));
            }
         });
      WorldRenderEvents.END_MAIN.register((EndMain)context -> {
         if (Modules.trajectories() != null) {
            Modules.guard(Modules.trajectories(), () -> Modules.trajectories().render(context));
         }
      });
      WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(context, outline) -> {
         BlockOutlineModule module = Modules.blockOutline();
         if (module != null && module.isEnabled() && outline != null) {
            BlockPos pos = outline.pos();
            Vec3 camera = context.worldState().cameraRenderState.pos;

            try {
               module.render(context.matrices(), outline.shape(), pos, camera.x, camera.y, camera.z);
               return false;
            } catch (RuntimeException e) {
               Modules.fail(module, e);
               return true;
            }
         } else {
            return true;
         }
      });
      WorldRenderEvents.END_MAIN.register((EndMain)context -> GlowEngine.endWorld());
      LOGGER.info("[Rose] initialized, version {}, {} modules", version(), Modules.all().size());

      for (String link : BuildLinks.all()) {
         LOGGER.info("[Rose] official: {}", link);
      }
   }
}
