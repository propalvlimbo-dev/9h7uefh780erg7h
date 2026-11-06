package dev.rose.looks;

import dev.rose.RoseClient;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.ColorsSetting.Value;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.setting.Setting;
import java.util.function.Consumer;


public enum Look {
   SIGNATURE(
      -12991045,
      -41826,
      -7343133,
      look -> {
         look.cosmetics("hit_effects", "kill_effects", "jump_circles", "block_outline", "pet", "swing_trail", "item_glow");
         look.module("hit_effects").choice("style", "STARS").choice("flash", "GLOW");
         look.module("kill_effects").choice("style", "SOULS");
         look.module("jump_circles").choice("style", "RING");
         look.module("block_outline").choice("style", "GLASS");
         look.module("pet").choice("pet", "KING");
         look.module("sky")
            .on()
            .choice("preset", "CUSTOM")
            .bool("sky_color", false)
            .bool("fog_color", false)
            .number("darkness", 0.0)
            .bool("aurora", true)
            .choice("aurora_when", "NIGHT")
            .bool("rays", true)
            .bool("soft_stars", true)
            .bool("meteors", true)
            .bool("cubes", false)
            .bool("hide_clouds", false);
      }
   ),
   BLOOD(
      -53942,
      -30147,
      -15704,
      look -> {
         look.cosmetics(
            "hit_effects",
            "kill_effects",
            "jump_circles",
            "block_outline",
            "aura",
            "trail",
            "afterimage",
            "wings",
            "pet",
            "ambient_particles",
            "swing_trail",
            "item_glow"
         );
         look.module("sky").on().choice("preset", "BLOOD");
         look.module("hit_effects").choice("style", "SLASHES").choice("flash", "GLOW");
         look.module("kill_effects").choice("style", "LIGHTNING");
         look.module("jump_circles").choice("style", "RIPPLES");
         look.module("block_outline").choice("style", "AURA");
         look.module("aura").choice("style", "SURGE");
         look.module("trail").choice("style", "RIBBON");
         look.module("afterimage").choice("style", "HOLOGRAM");
         look.module("wings").choice("style", "DEMON");
         look.module("pet").choice("pet", "DRAGON");
         look.module("ambient_particles").choice("style", "EMBERS");
         look.module("film_effects").on().number("vignette", 55.0);
         look.module("entity_colors").on().bool("hurt", true).color("hurt_color", -1325454006);
      }
   ),
   POLAR(
      -7709441,
      -12785976,
      -4199937,
      look -> {
         look.cosmetics(
            "hit_effects",
            "kill_effects",
            "jump_circles",
            "block_outline",
            "aura",
            "trail",
            "afterimage",
            "wings",
            "pet",
            "ambient_particles",
            "swing_trail",
            "item_glow"
         );
         look.module("sky").on().choice("preset", "POLAR");
         look.module("hit_effects").choice("style", "STARS").choice("flash", "GLOW");
         look.module("kill_effects").choice("style", "SOULS");
         look.module("jump_circles").choice("style", "DOUBLE");
         look.module("block_outline").choice("style", "GLASS");
         look.module("aura").choice("style", "HELIX");
         look.module("trail").choice("style", "DOUBLE");
         look.module("afterimage").choice("style", "GHOST");
         look.module("wings").choice("style", "ENERGY");
         look.module("pet").choice("pet", "GHOST");
         look.module("ambient_particles").choice("style", "SNOW");
         look.module("film_effects").on().number("vignette", 40.0);
      }
   ),
   SAKURA(
      -36939,
      -18325,
      -4236558,
      look -> {
         look.cosmetics(
            "hit_effects", "kill_effects", "jump_circles", "block_outline", "trail", "wings", "pet", "ambient_particles", "swing_trail", "item_glow"
         );
         look.off("aura", "afterimage", "film_effects");
         look.module("sky")
            .on()
            .choice("preset", "CUSTOM")
            .bool("sky_color", true)
            .color("sky", -24888)
            .bool("fog_color", true)
            .color("fog", -11805)
            .number("strength", 55.0)
            .number("darkness", 0.0)
            .number("night_glow", 45.0)
            .bool("aurora", true)
            .choice("aurora_when", "NIGHT")
            .bool("rays", false)
            .bool("soft_stars", true)
            .bool("meteors", true)
            .bool("cubes", false)
            .bool("hide_clouds", false);
         look.module("sky").colors("aurora_colors", ColorsSetting.Mode.GRADIENT, -36939, -4236558);
         look.module("hit_effects").choice("style", "HEARTS").choice("flash", "GLOW");
         look.module("kill_effects").choice("style", "CONFETTI");
         look.module("jump_circles").choice("style", "STAR");
         look.module("block_outline").choice("style", "FILL");
         look.module("trail").choice("style", "HELIX");
         look.module("wings").choice("style", "BUTTERFLY");
         look.module("pet").choice("pet", "CAT");
         look.module("ambient_particles").choice("style", "SAKURA");
      }
   ),
   SPACE(-16087809, -10593050, -10169601, look -> {
      look.cosmetics("hit_effects", "kill_effects", "jump_circles", "block_outline", "aura", "trail", "afterimage", "pet", "swing_trail", "item_glow");
      look.off("wings", "ambient_particles");
      look.module("sky").on().choice("preset", "SPACE");
      look.module("hit_effects").choice("style", "CRYSTALS").choice("flash", "CORNERS");
      look.module("kill_effects").choice("style", "SHOCKWAVE");
      look.module("jump_circles").choice("style", "HEXAGON");
      look.module("block_outline").choice("style", "CORNERS");
      look.module("aura").choice("style", "ORBIT");
      look.module("trail").choice("style", "DOTS");
      look.module("afterimage").choice("style", "HOLOGRAM");
      look.module("pet").choice("pet", "SLIME");
      look.module("film_effects").on().number("vignette", 45.0);
   }),
   CLEAN(-1710614, -7434605, -12991045, look -> {
      look.off("aura", "trail", "afterimage", "wings", "pet", "hats", "ambient_particles", "sky", "film_effects", "kill_effects", "swing_trail", "item_glow");
      look.cosmetics("hit_effects", "jump_circles", "block_outline");
      look.module("hit_effects").choice("style", "SPARKS").choice("flash", "NONE").number("amount", 8.0);
      look.module("jump_circles").choice("style", "RING");
      look.module("block_outline").choice("style", "OUTLINE");
   });

   private final int primary;
   private final int secondary;
   private final int tertiary;
   private final Consumer<Look.Builder> recipe;

   Look(int primary, int secondary, int tertiary, Consumer<Look.Builder> recipe) {
      this.primary = primary;
      this.secondary = secondary;
      this.tertiary = tertiary;
      this.recipe = recipe;
   }

   public int primary() {
      return this.primary;
   }

   public int secondary() {
      return this.secondary;
   }

   public int tertiary() {
      return this.tertiary;
   }

   public void apply() {
      Look.Builder builder = new Look.Builder();
      builder.module("theme").color("accent", this.primary).color("accent2", this.secondary).color("accent3", this.tertiary);
      builder.module("bloom").on();
      this.recipe.accept(builder);
      RoseClient.LOGGER.info("[Rose] look {} applied", this.name());
   }

   public static final class Builder {
      private Module current;

      public Look.Builder module(String id) {
         this.current = Modules.byId(id);
         if (this.current == null) {
            RoseClient.LOGGER.warn("[Rose] look: no module {}", id);
         }

         return this;
      }

      public Look.Builder on() {
         if (this.current != null) {
            this.current.setEnabled(true);
         }

         return this;
      }

      public void cosmetics(String... ids) {
         for (String id : ids) {
            this.module(id).on().colors("colors", ColorsSetting.Mode.THEME, 0, 0);
         }
      }

      public void off(String... ids) {
         for (String id : ids) {
            Module module = Modules.byId(id);
            if (module != null) {
               module.setEnabled(false);
            }
         }
      }

      private Setting<?> find(String id) {
         if (this.current == null) {
            return null;
         }

         for (Setting<?> setting : this.current.settings()) {
            if (setting.id().equals(id)) {
               return setting;
            }
         }

         RoseClient.LOGGER.warn("[Rose] look: no setting {}.{}", this.current.id(), id);
         return null;
      }

      public Look.Builder choice(String id, String value) {
         if (this.find(id) instanceof ChoiceSetting choice) {
            for (Enum<?> option : choice.options()) {
               if (option.name().equals(value)) {
                  choice.set(option);
                  return this;
               }
            }

            RoseClient.LOGGER.warn("[Rose] look: no option {} in {}.{}", new Object[]{value, this.current.id(), id});
         }

         return this;
      }

      public Look.Builder number(String id, double value) {
         if (this.find(id) instanceof NumberSetting number) {
            number.set(value);
         }

         return this;
      }

      public Look.Builder bool(String id, boolean value) {
         if (this.find(id) instanceof BoolSetting bool) {
            bool.set(value);
         }

         return this;
      }

      public Look.Builder color(String id, int argb) {
         if (this.find(id) instanceof ColorSetting color) {
            color.set(argb);
         }

         return this;
      }

      public Look.Builder colors(String id, ColorsSetting.Mode mode, int first, int second) {
         if (this.find(id) instanceof ColorsSetting colors) {
            Value value = colors.get().withMode(mode);
            if (mode != ColorsSetting.Mode.THEME && mode != ColorsSetting.Mode.RAINBOW) {
               value = value.withColor(0, first).withColor(1, second);
            }

            colors.set(value);
         }

         return this;
      }
   }
}
