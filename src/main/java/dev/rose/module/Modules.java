package dev.rose.module;

import dev.rose.RoseClient;
import dev.rose.hud.HudElement;
import dev.rose.hud.HudManager;
import dev.rose.module.impl.ClickGuiModule;
import dev.rose.module.impl.ContainerSearchModule;
import dev.rose.module.impl.HudSettingsModule;
import dev.rose.module.impl.ItemLocksModule;
import dev.rose.module.impl.KeysModule;
import dev.rose.module.impl.NotificationsModule;
import dev.rose.module.impl.PerformanceModule;
import dev.rose.module.impl.ProfilesModule;
import dev.rose.module.impl.ScreensModule;
import dev.rose.module.impl.ServerWarningModule;
import dev.rose.module.impl.SoundsModule;
import dev.rose.module.impl.ThemeModule;
import dev.rose.module.impl.cosmetic.AfterimageModule;
import dev.rose.module.impl.cosmetic.AuraModule;
import dev.rose.module.impl.cosmetic.CapeModule;
import dev.rose.module.impl.cosmetic.HatsModule;
import dev.rose.module.impl.cosmetic.ItemGlowModule;
import dev.rose.module.impl.cosmetic.JumpCirclesModule;
import dev.rose.module.impl.cosmetic.KillEffectsModule;
import dev.rose.module.impl.cosmetic.PetModule;
import dev.rose.module.impl.cosmetic.SwingTrailModule;
import dev.rose.module.impl.cosmetic.TrailModule;
import dev.rose.module.impl.cosmetic.WingsModule;
import dev.rose.module.impl.hud.ArmorModule;
import dev.rose.module.impl.hud.ArrayListModule;
import dev.rose.module.impl.hud.BlockInfoModule;
import dev.rose.module.impl.hud.ClockModule;
import dev.rose.module.impl.hud.CompassModule;
import dev.rose.module.impl.hud.CoordinatesModule;
import dev.rose.module.impl.hud.CpsModule;
import dev.rose.module.impl.hud.FpsModule;
import dev.rose.module.impl.hud.GlassBossbarModule;
import dev.rose.module.impl.hud.GlassHotbarModule;
import dev.rose.module.impl.hud.GlassScoreboardModule;
import dev.rose.module.impl.hud.GlassTabModule;
import dev.rose.module.impl.hud.InventoryModule;
import dev.rose.module.impl.hud.IslandModule;
import dev.rose.module.impl.hud.ItemCounterModule;
import dev.rose.module.impl.hud.KeyBindsModule;
import dev.rose.module.impl.hud.KeystrokesModule;
import dev.rose.module.impl.hud.MusicModule;
import dev.rose.module.impl.hud.PingModule;
import dev.rose.module.impl.hud.PotionsModule;
import dev.rose.module.impl.hud.SessionModule;
import dev.rose.module.impl.hud.SpeedModule;
import dev.rose.module.impl.hud.StopwatchModule;
import dev.rose.module.impl.hud.TargetHudModule;
import dev.rose.module.impl.hud.TitlesModule;
import dev.rose.module.impl.hud.WarningsModule;
import dev.rose.module.impl.hud.WatermarkModule;
import dev.rose.module.impl.sound.EventSoundsModule;
import dev.rose.module.impl.sound.HitSoundsModule;
import dev.rose.module.impl.visual.AspectRatioModule;
import dev.rose.module.impl.visual.BlockOutlineModule;
import dev.rose.module.impl.visual.BloomModule;
import dev.rose.module.impl.visual.CameraModule;
import dev.rose.module.impl.visual.ChatModule;
import dev.rose.module.impl.visual.ColorGradingModule;
import dev.rose.module.impl.visual.ContainerAnimationModule;
import dev.rose.module.impl.visual.CrosshairModule;
import dev.rose.module.impl.visual.EntityColorsModule;
import dev.rose.module.impl.visual.FilmEffectsModule;
import dev.rose.module.impl.visual.GlassTooltipsModule;
import dev.rose.module.impl.visual.HandModule;
import dev.rose.module.impl.visual.HitEffectsModule;
import dev.rose.module.impl.visual.LooksModule;
import dev.rose.module.impl.visual.MotionBlurModule;
import dev.rose.module.impl.visual.NametagsModule;
import dev.rose.module.impl.visual.SelfViewModule;
import dev.rose.module.impl.visual.TrajectoriesModule;
import dev.rose.module.impl.visual.ZoomModule;
import dev.rose.module.impl.world.AmbientParticlesModule;
import dev.rose.module.impl.world.ChunkFadeModule;
import dev.rose.module.impl.world.FullbrightModule;
import dev.rose.module.impl.world.ItemPhysicsModule;
import dev.rose.module.impl.world.ParticlesModule;
import dev.rose.module.impl.world.SkyModule;
import dev.rose.module.impl.world.TimeWeatherModule;
import dev.rose.module.impl.world.WaypointsModule;
import dev.rose.notify.Notifications;
import dev.rose.render.Backdrop;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;


public final class Modules {
   private static final List<Module> modules = new ArrayList<>();
   private static final Map<String, Module> byId = new HashMap<>();
   private static ThemeModule theme;
   private static ClickGuiModule clickGui;
   private static NotificationsModule notifications;
   private static ServerWarningModule serverWarning;
   private static ScreensModule screens;
   private static SoundsModule sounds;
   private static HitSoundsModule hitSounds;
   private static EventSoundsModule eventSounds;
   private static PerformanceModule performance;
   private static HudSettingsModule hud;
   private static PotionsModule potions;
   private static GlassHotbarModule glassHotbar;
   private static GlassTabModule glassTab;
   private static GlassScoreboardModule glassScoreboard;
   private static GlassBossbarModule glassBossbar;
   private static TitlesModule titles;
   private static FullbrightModule fullbright;
   private static CameraModule camera;
   private static TimeWeatherModule timeWeather;
   private static SkyModule sky;
   private static ParticlesModule particles;
   private static EntityColorsModule entityColors;
   private static HatsModule hats;
   private static WingsModule wings;
   private static AuraModule aura;
   private static CapeModule cape;
   private static TrailModule trail;
   private static JumpCirclesModule jumpCircles;
   private static PetModule pet;
   private static KillEffectsModule killEffects;
   private static AfterimageModule afterimage;
   private static AmbientParticlesModule ambientParticles;
   private static CrosshairModule crosshair;
   private static HandModule hand;
   private static BlockOutlineModule blockOutline;
   private static HitEffectsModule hitEffects;
   private static ChunkFadeModule chunkFade;
   private static SelfViewModule selfView;
   private static IslandModule island;
   private static StopwatchModule stopwatch;
   private static MusicModule music;
   private static ItemGlowModule itemGlow;
   private static WaypointsModule waypoints;
   private static ItemLocksModule itemLocks;
   private static ContainerSearchModule containerSearch;
   private static ZoomModule zoom;
   private static AspectRatioModule aspectRatio;
   private static SwingTrailModule swingTrail;
   private static LooksModule looks;
   private static BloomModule bloom;
   private static TrajectoriesModule trajectories;
   private static NametagsModule nametags;
   private static ContainerAnimationModule containerAnimation;
   private static GlassTooltipsModule glassTooltips;
   private static ChatModule chat;
   private static ColorGradingModule colorGrading;
   private static MotionBlurModule motionBlur;
   private static ItemPhysicsModule itemPhysics;

   private Modules() {
   }

   public static void init() {
      theme = register(new ThemeModule());
      clickGui = register(new ClickGuiModule());
      notifications = register(new NotificationsModule());
      serverWarning = register(new ServerWarningModule());
      register(new ProfilesModule());
      screens = register(new ScreensModule());
      sounds = register(new SoundsModule());
      performance = register(new PerformanceModule());
      register(new KeysModule());
      itemLocks = register(new ItemLocksModule());
      containerSearch = register(new ContainerSearchModule());
      hud = register(new HudSettingsModule());
      register(new WatermarkModule());
      island = register(new IslandModule());
      register(new ArrayListModule());
      register(new KeyBindsModule());
      register(new TargetHudModule());
      potions = register(new PotionsModule());
      register(new ArmorModule());
      register(new InventoryModule());
      register(new CoordinatesModule());
      register(new CompassModule());
      register(new WarningsModule());
      register(new FpsModule());
      register(new PingModule());
      register(new SpeedModule());
      register(new CpsModule());
      register(new ClockModule());
      stopwatch = register(new StopwatchModule());
      register(new SessionModule());
      music = register(new MusicModule());
      register(new KeystrokesModule());
      register(new ItemCounterModule());
      register(new BlockInfoModule());
      glassHotbar = register(new GlassHotbarModule());
      glassTab = register(new GlassTabModule());
      glassScoreboard = register(new GlassScoreboardModule());
      glassBossbar = register(new GlassBossbarModule());
      titles = register(new TitlesModule());
      looks = register(new LooksModule());
      crosshair = register(new CrosshairModule());
      camera = register(new CameraModule());
      hand = register(new HandModule());
      selfView = register(new SelfViewModule());
      bloom = register(new BloomModule());
      zoom = register(new ZoomModule());
      aspectRatio = register(new AspectRatioModule());
      hitEffects = register(new HitEffectsModule());
      blockOutline = register(new BlockOutlineModule());
      trajectories = register(new TrajectoriesModule());
      nametags = register(new NametagsModule());
      containerAnimation = register(new ContainerAnimationModule());
      glassTooltips = register(new GlassTooltipsModule());
      chat = register(new ChatModule());
      colorGrading = register(new ColorGradingModule());
      motionBlur = register(new MotionBlurModule());
      entityColors = register(new EntityColorsModule());
      register(new FilmEffectsModule());
      fullbright = register(new FullbrightModule());
      timeWeather = register(new TimeWeatherModule());
      sky = register(new SkyModule());
      particles = register(new ParticlesModule());
      itemPhysics = register(new ItemPhysicsModule());
      ambientParticles = register(new AmbientParticlesModule());
      waypoints = register(new WaypointsModule());
      chunkFade = register(new ChunkFadeModule());
      hats = register(new HatsModule());
      wings = register(new WingsModule());
      aura = register(new AuraModule());
      cape = register(new CapeModule());
      trail = register(new TrailModule());
      jumpCircles = register(new JumpCirclesModule());
      pet = register(new PetModule());
      killEffects = register(new KillEffectsModule());
      afterimage = register(new AfterimageModule());
      swingTrail = register(new SwingTrailModule());
      itemGlow = register(new ItemGlowModule());
      hitSounds = register(new HitSoundsModule());
      eventSounds = register(new EventSoundsModule());
   }

   private static <T extends Module> T register(T module) {
      if (byId.putIfAbsent(module.id(), module) != null) {
         throw new IllegalStateException("Duplicate module id: " + module.id());
      }

      modules.add(module);
      module.registered();
      return module;
   }

   public static List<Module> all() {
      return Collections.unmodifiableList(modules);
   }

   public static List<Module> in(Category category) {
      return modules.stream().filter(module -> module.category() == category).toList();
   }

   public static Module byId(String id) {
      return byId.get(id);
   }

   public static ThemeModule theme() {
      return theme;
   }

   public static CrosshairModule crosshair() {
      return crosshair;
   }

   public static HandModule hand() {
      return hand;
   }

   public static BlockOutlineModule blockOutline() {
      return blockOutline;
   }

   public static BloomModule bloom() {
      return bloom;
   }

   public static LooksModule looks() {
      return looks;
   }

   public static ZoomModule zoom() {
      return zoom;
   }

   public static AspectRatioModule aspectRatio() {
      return aspectRatio;
   }

   public static SwingTrailModule swingTrail() {
      return swingTrail;
   }

   public static ItemLocksModule itemLocks() {
      return itemLocks;
   }

   public static ContainerSearchModule containerSearch() {
      return containerSearch;
   }

   public static WaypointsModule waypoints() {
      return waypoints;
   }

   public static ItemGlowModule itemGlow() {
      return itemGlow;
   }

   public static IslandModule island() {
      return island;
   }

   public static StopwatchModule stopwatch() {
      return stopwatch;
   }

   public static MusicModule music() {
      return music;
   }

   public static SelfViewModule selfView() {
      return selfView;
   }

   public static ChunkFadeModule chunkFade() {
      return chunkFade;
   }

   public static HitEffectsModule hitEffects() {
      return hitEffects;
   }

   public static TrajectoriesModule trajectories() {
      return trajectories;
   }

   public static NametagsModule nametags() {
      return nametags;
   }

   public static ContainerAnimationModule containerAnimation() {
      return containerAnimation;
   }

   public static GlassTooltipsModule glassTooltips() {
      return glassTooltips;
   }

   public static ChatModule chat() {
      return chat;
   }

   public static ColorGradingModule colorGrading() {
      return colorGrading;
   }

   public static MotionBlurModule motionBlur() {
      return motionBlur;
   }

   public static ItemPhysicsModule itemPhysics() {
      return itemPhysics;
   }

   public static HatsModule hats() {
      return hats;
   }

   public static WingsModule wings() {
      return wings;
   }

   public static AuraModule aura() {
      return aura;
   }

   public static CapeModule cape() {
      return cape;
   }

   public static TrailModule trail() {
      return trail;
   }

   public static JumpCirclesModule jumpCircles() {
      return jumpCircles;
   }

   public static AfterimageModule afterimage() {
      return afterimage;
   }

   public static KillEffectsModule killEffects() {
      return killEffects;
   }

   public static AmbientParticlesModule ambientParticles() {
      return ambientParticles;
   }

   public static PetModule pet() {
      return pet;
   }

   public static FullbrightModule fullbright() {
      return fullbright;
   }

   public static CameraModule camera() {
      return camera;
   }

   public static TimeWeatherModule timeWeather() {
      return timeWeather;
   }

   public static SkyModule sky() {
      return sky;
   }

   public static ParticlesModule particles() {
      return particles;
   }

   public static EntityColorsModule entityColors() {
      return entityColors;
   }

   public static GlassHotbarModule glassHotbar() {
      return glassHotbar;
   }

   public static GlassTabModule glassTab() {
      return glassTab;
   }

   public static GlassScoreboardModule glassScoreboard() {
      return glassScoreboard;
   }

   public static GlassBossbarModule glassBossbar() {
      return glassBossbar;
   }

   public static TitlesModule titles() {
      return titles;
   }

   public static PotionsModule potions() {
      return potions;
   }

   public static HudSettingsModule hud() {
      return hud;
   }

   public static ClickGuiModule clickGui() {
      return clickGui;
   }

   public static List<Module> search(String query) {
      String needle = query.trim().toLowerCase(Locale.ROOT);
      return needle.isEmpty()
         ? List.of()
         : modules.stream()
            .filter(module -> module.name().toLowerCase(Locale.ROOT).contains(needle) || module.description().toLowerCase(Locale.ROOT).contains(needle))
            .toList();
   }

   public static NotificationsModule notifications() {
      return notifications;
   }

   public static ServerWarningModule serverWarning() {
      return serverWarning;
   }

   public static ScreensModule screens() {
      return screens;
   }

   public static HitSoundsModule hitSounds() {
      return hitSounds;
   }

   public static EventSoundsModule eventSounds() {
      return eventSounds;
   }

   public static SoundsModule sounds() {
      return sounds;
   }

   public static PerformanceModule performance() {
      return performance;
   }

   public static void onConfigLoaded() {
      for (Module module : modules) {
         module.onConfigLoaded();
      }
   }

   public static void onTick() {
      for (Module module : modules) {
         if (module.isEnabled()) {
            try {
               module.onTick();
            } catch (RuntimeException e) {
               fail(module, e);
            }
         }
      }
   }

   public static void onHud(GuiGraphics g, DeltaTracker deltaTracker) {
      Backdrop.capture();

      for (Module module : modules) {
         if (module.isEnabled() && !(module instanceof HudElement)) {
            try {
               module.onHud(g, deltaTracker);
            } catch (RuntimeException e) {
               fail(module, e);
            }
         }
      }

      HudManager.renderInGame(g, deltaTracker);
   }

   public static void onKeyPressed(int key) {
      for (Module module : modules) {
         if (module.hasKeybind() && module.key().get() == key) {
            module.onKeyBind();
         }

         if (module.isEnabled()) {
            module.onRawKey(key);
         }
      }
   }

   public static void guard(Module module, Runnable action) {
      try {
         action.run();
      } catch (RuntimeException e) {
         fail(module, e);
      }
   }

   public static void fail(Module module, RuntimeException e) {
      RoseClient.LOGGER.error("[Rose] module {} crashed and was disabled", module.id(), e);
      module.setEnabled(false);
      Notifications.show(Notifications.Kind.ERROR, module.name(), I18n.get("rose.notify.error", new Object[0]));
   }
}
