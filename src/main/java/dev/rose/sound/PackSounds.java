package dev.rose.sound;

import java.util.List;
import java.util.Locale;
import net.minecraft.resources.Identifier;


public enum PackSounds {
   HIT_SOFT,
   HIT_PUNCH,
   HIT_METAL,
   HIT_WOOD,
   HIT_GLASS,
   HIT_BELL,
   HIT_PLATE,
   HIT_PLUCK,
   HIT_TICK,
   HIT_DROP,
   SLICE,
   CRIT_ZAP,
   CRIT_PHASER,
   CRIT_GLASS,
   CRIT_HIGH,
   WIN_STEEL,
   WIN_PIZZI,
   WIN_NES,
   WIN_POWER,
   COINS,
   JOIN_CONFIRM,
   DEATH_DOWN,
   ALARM,
   TOTEM_CHIME,
   LEVEL_UP,
   PAGE,
   UI_CLICK,
   UI_HOVER,
   UI_TOGGLE_ON,
   UI_TOGGLE_OFF,
   UI_OPEN,
   UI_CLOSE,
   UI_SELECT,
   UI_SCROLL,
   UI_TAB,
   UI_NOTIFY;

   private static final List<PackSounds> CHOOSABLE = List.of(values()).stream().filter(sound -> !sound.name().startsWith("UI_")).toList();

   public String id() {
      return this.name().toLowerCase(Locale.ROOT);
   }

   public Identifier file() {
      return Identifier.fromNamespaceAndPath("rose", "sounds/" + this.id() + ".ogg");
   }

   public static PackSounds byId(String id) {
      for (PackSounds sound : values()) {
         if (sound.id().equals(id)) {
            return sound;
         }
      }

      return null;
   }

   public static List<PackSounds> choosable() {
      return CHOOSABLE;
   }
}
