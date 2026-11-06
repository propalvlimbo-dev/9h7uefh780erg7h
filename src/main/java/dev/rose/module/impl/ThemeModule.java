package dev.rose.module.impl;

import dev.rose.config.ThemeLibrary;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Theme;
import dev.rose.render.font.Fonts;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.FileChoiceSetting;
import dev.rose.setting.GallerySetting.Preview;
import dev.rose.setting.GallerySetting;
import dev.rose.setting.NumberSetting;
import dev.rose.setting.Setting;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;


public final class ThemeModule extends Module {
   private boolean loadingPreset;
   private final GallerySetting<ThemeModule.Palette> palette;
   private final ColorSetting accent;
   private final ColorSetting accent2;
   private final ColorSetting accent3;
   private final ChoiceSetting<ThemeModule.Preset> preset;
   private final NumberSetting blur;
   private final NumberSetting blurSpread;
   private final ColorSetting tint;
   private final ChoiceSetting<ThemeModule.LabelColor> label;
   private final NumberSetting refraction;
   private final NumberSetting bevel;
   private final NumberSetting curve;
   private final NumberSetting magnify;
   private final NumberSetting dispersion;
   private final NumberSetting depth;
   private final NumberSetting wobble;
   private final NumberSetting wobbleScale;
   private final NumberSetting wobbleSpeed;
   private final NumberSetting specular;
   private final NumberSetting sharpness;
   private final ColorSetting specularColor;
   private final NumberSetting rim;
   private final NumberSetting rimWidth;
   private final NumberSetting fresnel;
   private final NumberSetting lightAngle;
   private final BoolSetting lightFollowsMouse;
   private final NumberSetting saturation;
   private final NumberSetting brightness;
   private final NumberSetting contrast;
   private final NumberSetting grain;
   private final ChoiceSetting<ThemeModule.CornerStyle> cornerStyle;
   private final NumberSetting cornerSmoothness;
   private final NumberSetting cornerScale;
   public static final Path FONTS_DIR = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("fonts");
   private static List<Path> fonts = List.of();
   private static long fontsScannedAt;
   private final ChoiceSetting<Fonts.Family> textFont;
   private final ChoiceSetting<Fonts.Family> titleFont;
   private final FileChoiceSetting fontFile;
   private final ActionSetting openFonts;

   public ThemeModule() {
      super("theme", Category.INTERFACE, true);
      this.section("palette", 58783);
      this.palette = this.add(new GallerySetting<>("palette", ThemeModule.Palette.ROSE)).onChange(this::loadPalette);
      this.accent = this.add(new ColorSetting("accent", ThemeModule.Palette.ROSE.primary(), false)).onChange(this::changed);
      this.accent2 = this.add(new ColorSetting("accent2", ThemeModule.Palette.ROSE.secondary(), false)).onChange(this::changed);
      this.accent3 = this.add(new ColorSetting("accent3", ThemeModule.Palette.ROSE.tertiary(), false)).onChange(this::changed);
      this.section("glass", 57524);
      this.preset = this.add(new ChoiceSetting<>("preset", ThemeModule.Preset.DARK)).onChange(this::loadPreset);
      this.blur = this.number("blur", 80.0, 0.0, 100.0, 1.0, "rose.unit.percent");
      this.blurSpread = this.number("blur_spread", 1.6, 0.5, 4.0, 0.1, "");
      this.tint = this.add(new ColorSetting("tint", -2146562542, true)).onChange(this::changed);
      this.label = this.add(new ChoiceSetting<>("label", ThemeModule.LabelColor.AUTO)).onChange(this::changed);
      this.refraction = this.number("refraction", 6.0, -30.0, 30.0, 0.5, "rose.unit.pixels");
      this.bevel = this.number("bevel", 10.0, 1.0, 40.0, 0.5, "rose.unit.pixels");
      this.curve = this.number("curve", 2.0, 0.5, 6.0, 0.1, "");
      this.magnify = this.number("magnify", 100.0, 50.0, 200.0, 1.0, "rose.unit.percent");
      this.dispersion = this.number("dispersion", 20.0, 0.0, 100.0, 1.0, "rose.unit.percent");
      this.depth = this.number("depth", 8.0, 0.0, 40.0, 1.0, "rose.unit.percent");
      this.wobble = this.number("wobble", 0.0, 0.0, 12.0, 0.1, "rose.unit.pixels");
      this.wobbleScale = this.number("wobble_scale", 1.0, 0.2, 5.0, 0.1, "");
      this.wobbleSpeed = this.number("wobble_speed", 1.0, 0.0, 5.0, 0.1, "");
      this.section("light", 57720);
      this.specular = this.number("specular", 35.0, 0.0, 200.0, 1.0, "rose.unit.percent");
      this.sharpness = this.number("sharpness", 5.0, 1.0, 20.0, 0.5, "");
      this.specularColor = this.add(new ColorSetting("specular_color", -1, false)).onChange(this::changed);
      this.rim = this.number("rim", 22.0, 0.0, 100.0, 1.0, "rose.unit.percent");
      this.rimWidth = this.number("rim_width", 1.5, 0.0, 6.0, 0.1, "rose.unit.pixels");
      this.fresnel = this.number("fresnel", 0.0, 0.0, 100.0, 1.0, "rose.unit.percent");
      this.lightAngle = this.number("light_angle", 238.0, 0.0, 360.0, 1.0, "rose.unit.degrees");
      this.lightFollowsMouse = this.add(new BoolSetting("light_mouse", false)).onChange(this::changed);
      this.section("correction", 58780);
      this.saturation = this.number("saturation", 125.0, 0.0, 250.0, 1.0, "rose.unit.percent");
      this.brightness = this.number("brightness", 0.0, -50.0, 50.0, 1.0, "rose.unit.percent");
      this.contrast = this.number("contrast", 100.0, 50.0, 200.0, 1.0, "rose.unit.percent");
      this.grain = this.number("grain", 0.0, 0.0, 20.0, 0.5, "rose.unit.percent");
      this.section("shape", 58633);
      this.cornerStyle = this.add(new ChoiceSetting<>("corner_style", ThemeModule.CornerStyle.SQUIRCLE)).onChange(this::changed);
      this.cornerSmoothness = this.add(new NumberSetting("corner_smoothness", 2.6, 2.0, 10.0, 0.1, ""))
         .visibleWhen(() -> this.cornerStyle.get() == ThemeModule.CornerStyle.SQUIRCLE)
         .onChange(this::changed);
      this.cornerScale = this.number("corner_scale", 100.0, 0.0, 250.0, 1.0, "rose.unit.percent");
      this.section("font", 57752);
      this.textFont = this.add(new ChoiceSetting<>("text_font", Fonts.Family.ONEST)).onChange(this::changed);
      this.titleFont = this.add(new ChoiceSetting<>("title_font", Fonts.Family.UNBOUNDED)).onChange(this::changed);
      this.fontFile = this.add(new FileChoiceSetting("font_file", ThemeModule::fontFiles)).visibleWhen(this::customFont).onChange(this::changed);
      this.openFonts = this.add(new ActionSetting("open_fonts", 57927, ThemeModule::openFontsFolder)).visibleWhen(this::customFont);
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }

   @Override
   public int icon() {
      return 57821;
   }

   @Override
   public void onConfigLoaded() {
      this.apply(false);
   }

   private NumberSetting number(String id, double defaultValue, double min, double max, double step, String unit) {
      return this.add(new NumberSetting(id, defaultValue, min, max, step, unit)).onChange(this::changed);
   }

   private void changed() {
      if (!this.loadingPreset) {
         this.apply(false);
      }
   }

   public void applyAnimated() {
      this.apply(true);
   }

   public void applyBuiltin(ThemeLibrary.Builtin theme) {
      this.loadingPreset = true;

      try {
         for (Setting<?> setting : this.settings()) {
            if (!setting.section().equals("font")) {
               setting.reset();
            }
         }

         this.palette.set(theme.palette());
         this.accent.set(theme.palette().primary());
         this.accent2.set(theme.palette().secondary());
         this.accent3.set(theme.palette().tertiary());
      } finally {
         this.loadingPreset = false;
      }

      this.preset.set(theme.glass());
      this.loadPreset();
      this.loadingPreset = true;

      try {
         if (theme.tint() != 0) {
            this.tint.set(theme.tint());
         }

         this.label.set(theme.label());
         this.cornerStyle.set(theme.corners());
         this.cornerScale.set((double)theme.cornerScale());
         theme.numbers().forEach((id, value) -> {
            for (Setting<?> settingx : this.settings()) {
               if (settingx.id().equals(id) && settingx instanceof NumberSetting number) {
                  number.set(value);
               }
            }
         });
      } finally {
         this.loadingPreset = false;
      }

      this.apply(true);
   }

   private boolean customFont() {
      return this.textFont.get() == Fonts.Family.CUSTOM || this.titleFont.get() == Fonts.Family.CUSTOM;
   }

   private static List<Path> fontFiles() {
      long now = System.currentTimeMillis();
      if (now - fontsScannedAt < 2000L) {
         return fonts;
      }

      fontsScannedAt = now;

      try {
         Files.createDirectories(FONTS_DIR);

         try (Stream<Path> files = Files.list(FONTS_DIR)) {
            fonts = files.filter(path -> {
               String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
               return name.endsWith(".ttf") || name.endsWith(".otf");
            }).sorted().toList();
         }
      } catch (IOException e) {
         fonts = List.of();
      }

      return fonts;
   }

   private static void openFontsFolder() {
      try {
         Files.createDirectories(FONTS_DIR);
      } catch (IOException var1) {
      }

      Util.getPlatform().openPath(FONTS_DIR);
   }

   private void loadPalette() {
      if (!this.loadingPreset) {
         ThemeModule.Palette chosen = this.palette.get();
         this.loadingPreset = true;

         try {
            this.accent.set(chosen.primary());
            this.accent2.set(chosen.secondary());
            this.accent3.set(chosen.tertiary());
         } finally {
            this.loadingPreset = false;
         }

         this.apply(false);
      }
   }

   private void loadPreset() {
      if (!this.loadingPreset) {
         Glass glass = switch ((ThemeModule.Preset)this.preset.get()) {
            case CLEAR -> Glass.clear();
            case LIGHT -> Glass.light();
            case DARK -> Glass.dark();
         };
         this.loadingPreset = true;

         try {
            this.blur.set(glass.blur * 100.0);
            this.refraction.set((double)glass.refraction);
            this.bevel.set((double)glass.bevel);
            this.curve.set((double)glass.curve);
            this.magnify.set(glass.magnify * 100.0);
            this.dispersion.set(glass.dispersion * 100.0);
            this.specular.set(glass.specular * 100.0);
            this.sharpness.set((double)glass.sharpness);
            this.specularColor.set(glass.specularColor);
            this.rim.set(glass.rim * 100.0);
            this.rimWidth.set((double)glass.rimWidth);
            this.fresnel.set(glass.fresnel * 100.0);
            this.saturation.set(glass.saturation * 100.0);
            this.brightness.set(glass.brightness * 100.0);
            this.contrast.set(glass.contrast * 100.0);
            this.grain.set(glass.grain * 100.0);
            this.wobble.set((double)glass.wobble);
            this.depth.set(glass.depth * 100.0);
            this.tint.set(glass.tint.color(0));
            this.label.set(ThemeModule.LabelColor.AUTO);
         } finally {
            this.loadingPreset = false;
         }

         this.apply(true);
      }
   }

   private void apply(boolean animate) {
      Glass glass = new Glass();
      glass.blur = this.blur.floatValue() / 100.0F;
      glass.refraction = this.refraction.floatValue();
      glass.bevel = this.bevel.floatValue();
      glass.curve = this.curve.floatValue();
      glass.magnify = this.magnify.floatValue() / 100.0F;
      glass.dispersion = this.dispersion.floatValue() / 100.0F;
      glass.specular = this.specular.floatValue() / 100.0F;
      glass.sharpness = this.sharpness.floatValue();
      glass.specularColor = this.specularColor.get();
      glass.rim = this.rim.floatValue() / 100.0F;
      glass.rimWidth = this.rimWidth.floatValue();
      glass.fresnel = this.fresnel.floatValue() / 100.0F;
      glass.saturation = this.saturation.floatValue() / 100.0F;
      glass.brightness = this.brightness.floatValue() / 100.0F;
      glass.contrast = this.contrast.floatValue() / 100.0F;
      glass.grain = this.grain.floatValue() / 100.0F;
      glass.wobble = this.wobble.floatValue();
      glass.wobbleScale = this.wobbleScale.floatValue();
      glass.wobbleSpeed = this.wobbleSpeed.floatValue();
      glass.depth = this.depth.floatValue() / 100.0F;
      glass.tint = Paint.solid(this.tint.get());
      glass.label = this.labelColor();
      glass.lightFollowsMouse = this.lightFollowsMouse.isOn();
      Theme.setGlass(glass, animate);
      Theme.lightAngle = this.lightAngle.floatValue();
      Theme.blurSpread = this.blurSpread.floatValue();

      Theme.cornerShape = switch ((ThemeModule.CornerStyle)this.cornerStyle.get()) {
         case ROUND, SHARP -> 2.0F;
         case SQUIRCLE -> this.cornerSmoothness.floatValue();
         case BEVEL -> 1.0F;
         case SCOOP -> -1.0F;
      };
      Theme.cornerScale = this.cornerStyle.get() == ThemeModule.CornerStyle.SHARP ? 0.0F : this.cornerScale.floatValue() / 100.0F;
      Theme.setAccent(this.accent::color);
      Theme.setAccent2(this.accent2::color);
      Theme.setAccent3(this.accent3::color);
      Fonts.configure(this.textFont.get(), this.titleFont.get(), this.customFont() ? this.fontFile.selected() : null);
   }

   private int labelColor() {
      return switch ((ThemeModule.LabelColor)this.label.get()) {
         case AUTO -> {
            int color = this.tint.get();
            float alpha = ColorUtil.alpha(color) / 255.0F;
            float luminance = (ColorUtil.red(color) * 0.2126F + ColorUtil.green(color) * 0.7152F + ColorUtil.blue(color) * 0.0722F) / 255.0F;
            yield luminance * alpha + 0.22F * (1.0F - alpha) > 0.5F ? -14935010 : -1;
         }
         case LIGHT -> -1;
         case DARK -> -14935010;
      };
   }

   public enum CornerStyle {
      ROUND,
      SQUIRCLE,
      BEVEL,
      SCOOP,
      SHARP;
   }

   public enum LabelColor {
      AUTO,
      LIGHT,
      DARK;
   }

   public enum Palette implements Preview {
      ROSE(-1824437, -38011, -4124102),
      AURORA(-10593050, -13580801, -53867),
      MIDNIGHT(-16087809, -10593050, -10169601),
      SAKURA(-36939, -18325, -4236558),
      OCEAN(-16726082, -16087809, -10169601),
      SUNSET(-24822, -51361, -10742),
      NEON(-13577896, -2818304, -16718337),
      MONO(-1710614, -7434605, -10263706);

      private final int primary;
      private final int secondary;
      private final int tertiary;

      Palette(int primary, int secondary, int tertiary) {
         this.primary = primary;
         this.secondary = secondary;
         this.tertiary = tertiary;
      }

      @Override
      public int primary() {
         return this.primary;
      }

      @Override
      public int secondary() {
         return this.secondary;
      }

      @Override
      public int tertiary() {
         return this.tertiary;
      }
   }

   public enum Preset {
      CLEAR,
      LIGHT,
      DARK;
   }
}
