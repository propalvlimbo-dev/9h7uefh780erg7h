package dev.rose.module.impl;

import dev.rose.gui.CharacterRigEditor;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.notify.Notifications;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ImageChoiceSetting;
import dev.rose.setting.NumberSetting;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Util;


public final class ClickGuiModule extends Module {
   public static final Path CHARACTERS_DIR = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("characters");
   private static List<Path> images = List.of();
   private static long imagesScannedAt;
   public static final Path BACKGROUNDS_DIR = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("backgrounds");
   private static List<Path> backgrounds = List.of();
   private static long backgroundsScannedAt;
   private final ChoiceSetting<ClickGuiModule.WindowStyle> style;
   private final NumberSetting density;
   private final NumberSetting opacity;
   private final NumberSetting scale;
   private final ChoiceSetting<ClickGuiModule.OpenAnimation> animation;
   private final ActionSetting resetWindow;
   private final NumberSetting dim;
   private final BoolSetting blurWorld;
   private final BoolSetting pause;
   private final BoolSetting reduceMotion;
   private final NumberSetting windowX;
   private final NumberSetting windowY;
   private final NumberSetting windowW;
   private final NumberSetting windowH;
   private final ChoiceSetting<ClickGuiModule.Background> background;
   private final ImageChoiceSetting backgroundFile;
   private final ActionSetting openBackgrounds;
   private final NumberSetting backgroundBlur;
   private final BoolSetting backgroundParallax;
   private final BoolSetting backgroundMenus;
   private final ChoiceSetting<ClickGuiModule.Companion> character;
   private final ImageChoiceSetting characterFile;
   private final ActionSetting openFolder;
   private final ActionSetting editRig;
   private final NumberSetting characterScale;
   private final NumberSetting hairPhysics;
   private final BoolSetting reactions;
   private final BoolSetting onboarded;

   public ClickGuiModule() {
      super("clickgui", Category.INTERFACE, true, 344);
      this.section("window", 58406);
      this.style = this.add(new ChoiceSetting<>("window_style", ClickGuiModule.WindowStyle.GLASS));
      this.density = this.add(new NumberSetting("glass_density", 100.0, 20.0, 200.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.style.get() == ClickGuiModule.WindowStyle.GLASS);
      this.opacity = this.add(new NumberSetting("window_opacity", 96.0, 60.0, 100.0, 1.0, "rose.unit.percent"))
         .visibleWhen(() -> this.style.get() == ClickGuiModule.WindowStyle.SOLID);
      this.scale = this.add(new NumberSetting("menu_scale", 100.0, 60.0, 150.0, 5.0, "rose.unit.percent"));
      this.animation = this.add(new ChoiceSetting<>("menu_animation", ClickGuiModule.OpenAnimation.LIQUID));
      this.resetWindow = this.add(new ActionSetting("reset_window", 57619, this::resetWindow));
      this.section("behind", 57530);
      this.dim = this.add(new NumberSetting("menu_dim", 0.0, 0.0, 80.0, 1.0, "rose.unit.percent"));
      this.blurWorld = this.add(new BoolSetting("menu_blur", false));
      this.pause = this.add(new BoolSetting("pause", false));
      this.reduceMotion = this.add(new BoolSetting("reduce_motion", false));
      this.windowX = this.add(new NumberSetting("window_x", 500.0, 0.0, 1000.0, 1.0, "")).visibleWhen(() -> false);
      this.windowY = this.add(new NumberSetting("window_y", 500.0, 0.0, 1000.0, 1.0, "")).visibleWhen(() -> false);
      this.windowW = this.add(new NumberSetting("window_width", 620.0, 420.0, 1400.0, 1.0, "")).visibleWhen(() -> false);
      this.windowH = this.add(new NumberSetting("window_height", 380.0, 260.0, 900.0, 1.0, "")).visibleWhen(() -> false);
      this.section("background", 57590);
      this.background = this.add(new ChoiceSetting<>("background", ClickGuiModule.Background.GAME));
      this.backgroundFile = this.add(new ImageChoiceSetting("background_file", ClickGuiModule::backgroundImages)).visibleWhen(this::imageBackground);
      this.openBackgrounds = this.add(new ActionSetting("open_backgrounds", 57927, () -> openFolder(BACKGROUNDS_DIR))).visibleWhen(this::imageBackground);
      this.backgroundBlur = this.add(new NumberSetting("background_blur", 0.0, 0.0, 100.0, 5.0, "rose.unit.percent")).visibleWhen(this::imageBackground);
      this.backgroundParallax = this.add(new BoolSetting("background_parallax", true)).visibleWhen(this::imageBackground);
      this.backgroundMenus = this.add(new BoolSetting("background_menus", true)).visibleWhen(this::imageBackground);
      this.section("character", 58185);
      this.character = this.add(new ChoiceSetting<>("character", ClickGuiModule.Companion.NONE));
      this.characterFile = this.add(new ImageChoiceSetting("character_file", ClickGuiModule::characterImages))
         .visibleWhen(() -> this.character.get() == ClickGuiModule.Companion.CUSTOM);
      this.openFolder = this.add(new ActionSetting("open_folder", 57927, () -> openFolder(CHARACTERS_DIR)))
         .visibleWhen(() -> this.character.get() == ClickGuiModule.Companion.CUSTOM);
      this.editRig = this.add(new ActionSetting("edit_rig", 57849, ClickGuiModule::openRigEditor))
         .visibleWhen(() -> this.character.get() == ClickGuiModule.Companion.CUSTOM);
      this.characterScale = this.add(new NumberSetting("character_scale", 100.0, 60.0, 140.0, 5.0, "rose.unit.percent")).visibleWhen(this::showsCharacter);
      this.hairPhysics = this.add(new NumberSetting("hair_physics", 100.0, 0.0, 200.0, 5.0, "rose.unit.percent")).visibleWhen(this::showsCharacter);
      this.reactions = this.add(new BoolSetting("reactions", true)).visibleWhen(this::showsCharacter);
      this.onboarded = this.add(new BoolSetting("onboarded", false)).visibleWhen(() -> false);
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }

   @Override
   public boolean hasKeybind() {
      return true;
   }

   @Override
   public int icon() {
      return 58010;
   }

   @Override
   public void onKeyBind() {
      Minecraft mc = Minecraft.getInstance();
      if (!(mc.screen instanceof MenuScreen)) {
         mc.setScreen(new MenuScreen(mc.screen));
      }
   }

   public ClickGuiModule.WindowStyle windowStyle() {
      return this.style.get();
   }

   public float glassDensity() {
      return this.density.floatValue() / 100.0F;
   }

   public float windowOpacity() {
      return this.opacity.floatValue() / 100.0F;
   }

   public float scale() {
      return this.scale.floatValue() / 100.0F;
   }

   public ClickGuiModule.OpenAnimation openAnimation() {
      return this.reduceMotion.isOn() ? ClickGuiModule.OpenAnimation.NONE : this.animation.get();
   }

   public float dim() {
      return this.dim.floatValue() / 100.0F;
   }

   public boolean blurWorld() {
      return this.blurWorld.isOn();
   }

   public boolean pausesGame() {
      return this.pause.isOn();
   }

   public boolean reduceMotion() {
      return this.reduceMotion.isOn();
   }

   public float windowX() {
      return this.windowX.floatValue() / 1000.0F;
   }

   public float windowY() {
      return this.windowY.floatValue() / 1000.0F;
   }

   public float windowWidth() {
      return this.windowW.floatValue();
   }

   public float windowHeight() {
      return this.windowH.floatValue();
   }

   public void saveWindow(float fractionX, float fractionY, float width, float height) {
      this.windowX.set((double)Math.round(Math.clamp(fractionX, 0.0F, 1.0F) * 1000.0F));
      this.windowY.set((double)Math.round(Math.clamp(fractionY, 0.0F, 1.0F) * 1000.0F));
      this.windowW.set((double)Math.round(width));
      this.windowH.set((double)Math.round(height));
   }

   private void resetWindow() {
      this.windowX.reset();
      this.windowY.reset();
      this.windowW.reset();
      this.windowH.reset();
   }

   public ClickGuiModule.Companion character() {
      return this.character.get();
   }

   private boolean showsCharacter() {
      return this.character.get() != ClickGuiModule.Companion.NONE;
   }

   public float characterScale() {
      return this.characterScale.floatValue() / 100.0F;
   }

   public float hairPhysics() {
      return this.hairPhysics.floatValue() / 100.0F;
   }

   public boolean reactions() {
      return this.reactions.isOn();
   }

   public Path characterImage() {
      return this.characterFile.selected();
   }

   public boolean onboarded() {
      return this.onboarded.isOn();
   }

   public void finishOnboarding() {
      this.onboarded.set(true);
   }

   private static List<Path> characterImages() {
      long now = System.currentTimeMillis();
      if (now - imagesScannedAt < 2000L) {
         return images;
      }

      imagesScannedAt = now;

      try {
         Files.createDirectories(CHARACTERS_DIR);

         try (Stream<Path> files = Files.list(CHARACTERS_DIR)) {
            images = files.filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png")).sorted().toList();
         }
      } catch (IOException e) {
         images = List.of();
      }

      return images;
   }

   private static void openRigEditor() {
      Minecraft mc = Minecraft.getInstance();
      Path file = characterImages().isEmpty() ? null : Modules.clickGui().characterImage();
      if (file == null) {
         Notifications.show(
            Notifications.Kind.INFO,
            I18n.get("rose.character.custom_missing.title", new Object[0]),
            I18n.get("rose.character.custom_missing.subtitle", new Object[0])
         );
      } else {
         mc.setScreen(new CharacterRigEditor(mc.screen, file));
      }
   }

   private static void openFolder(Path folder) {
      try {
         Files.createDirectories(folder);
      } catch (IOException var2) {
      }

      Util.getPlatform().openPath(folder);
   }

   private boolean imageBackground() {
      return this.background.get() == ClickGuiModule.Background.IMAGE;
   }

   public boolean customBackground() {
      return this.imageBackground();
   }

   public Path backgroundImage() {
      return this.imageBackground() ? this.backgroundFile.selected() : null;
   }

   public float backgroundBlur() {
      return this.backgroundBlur.floatValue();
   }

   public boolean backgroundParallax() {
      return this.backgroundParallax.isOn();
   }

   public boolean backgroundInMenus() {
      return this.backgroundMenus.isOn();
   }

   private static List<Path> backgroundImages() {
      long now = System.currentTimeMillis();
      if (now - backgroundsScannedAt < 2000L) {
         return backgrounds;
      }

      backgroundsScannedAt = now;

      try {
         Files.createDirectories(BACKGROUNDS_DIR);

         try (Stream<Path> files = Files.list(BACKGROUNDS_DIR)) {
            backgrounds = files.filter(path -> {
               String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
               return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
            }).sorted().toList();
         }
      } catch (IOException e) {
         backgrounds = List.of();
      }

      return backgrounds;
   }

   public enum Background {
      GAME,
      IMAGE;
   }

   public enum Companion {
      NONE,
      CUSTOM;
   }

   public enum OpenAnimation {
      NONE,
      FADE,
      LIQUID;
   }

   public enum WindowStyle {
      SOLID,
      GLASS;
   }
}
