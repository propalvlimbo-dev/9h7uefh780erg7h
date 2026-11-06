package dev.rose.gui;

import dev.rose.module.Modules;
import dev.rose.module.impl.SoundsModule;
import dev.rose.sound.PackSounds;
import dev.rose.sound.SoundLibrary;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;


public final class UiSounds {
   private static final float[] NOTES = new float[]{0.707F, 0.794F, 0.891F, 1.059F, 1.189F, 1.414F, 1.587F, 1.782F};
   private static final Random RANDOM = new Random();
   private static long lastHover;
   private static long lastSlider;

   private UiSounds() {
   }

   public static void play(UiSounds.Sound sound) {
      SoundsModule settings = Modules.sounds();
      float volume = settings == null ? 0.6F : settings.volume(sound.category);
      if (!(volume <= 0.001F)) {
         long now = System.currentTimeMillis();
         if (sound == UiSounds.Sound.HOVER) {
            if (now - lastHover < 70L) {
               return;
            }

            lastHover = now;
         } else if (sound == UiSounds.Sound.SLIDER) {
            if (now - lastSlider < 45L) {
               return;
            }

            lastSlider = now;
         }

         SoundsModule.Pack pack = settings == null ? SoundsModule.Pack.LIQUID : settings.pack();
         switch (pack) {
            case STUDIO:
               studio(sound, volume);
               break;
            case LIQUID:
               liquid(sound, volume);
               break;
            case GLASS:
               glass(sound, volume);
               break;
            case SOFT:
               soft(sound, volume);
               break;
            case CLASSIC:
               classic(sound, volume);
         }
      }
   }

   private static void studio(UiSounds.Sound sound, float volume) {
      switch (sound) {
         case OPEN:
            pack(PackSounds.UI_OPEN, 1.0F, 0.75F * volume);
            break;
         case CLOSE:
            pack(PackSounds.UI_CLOSE, 1.0F, 0.7F * volume);
            break;
         case TAB:
            pack(PackSounds.UI_TAB, 1.0F, 0.6F * volume);
            break;
         case HOVER:
            pack(PackSounds.UI_HOVER, 1.05F, 0.22F * volume);
            break;
         case CLICK:
            pack(PackSounds.UI_CLICK, 1.0F, 0.5F * volume);
            break;
         case TOGGLE_ON:
            pack(PackSounds.UI_TOGGLE_ON, 1.0F, 0.7F * volume);
            break;
         case TOGGLE_OFF:
            pack(PackSounds.UI_TOGGLE_OFF, 1.0F, 0.6F * volume);
            break;
         case SLIDER:
            pack(PackSounds.UI_SCROLL, 1.0F + RANDOM.nextFloat() * 0.15F, 0.2F * volume);
            break;
         case SEARCH:
            pack(PackSounds.UI_SELECT, 1.0F, 0.55F * volume);
            break;
         case CHARACTER:
            pack(PackSounds.UI_CLICK, note(), 0.3F * volume);
            break;
         case NOTIFY:
            pack(PackSounds.UI_NOTIFY, 1.0F, 0.7F * volume);
      }
   }

   private static void pack(PackSounds sound, float pitch, float volume) {
      SoundLibrary.play(sound.id(), volume, pitch);
   }

   private static void liquid(UiSounds.Sound sound, float volume) {
      switch (sound) {
         case OPEN:
            emit(SoundEvents.UI_TOAST_IN, 1.35F, 0.55F * volume);
            break;
         case CLOSE:
            emit(SoundEvents.UI_TOAST_OUT, 1.4F, 0.45F * volume);
            break;
         case TAB:
            emit(SoundEvents.AMETHYST_BLOCK_CHIME, 1.5F + RANDOM.nextFloat() * 0.2F, 0.5F * volume);
            break;
         case HOVER:
            emit(SoundEvents.AMETHYST_CLUSTER_STEP, 1.9F, 0.12F * volume);
            break;
         case CLICK:
            emit((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.6F, 0.18F * volume);
            break;
         case TOGGLE_ON:
            emit(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1.5F, 0.9F * volume);
            break;
         case TOGGLE_OFF:
            emit(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 0.95F, 0.8F * volume);
            break;
         case SLIDER:
            emit(SoundEvents.AMETHYST_CLUSTER_STEP, 1.5F + RANDOM.nextFloat() * 0.3F, 0.1F * volume);
            break;
         case SEARCH:
            emit(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.6F, 0.5F * volume);
            break;
         case CHARACTER:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), note(), 0.45F * volume);
            break;
         case NOTIFY:
            emit(SoundEvents.AMETHYST_BLOCK_CHIME, 1.2F, 0.55F * volume);
      }
   }

   private static void glass(UiSounds.Sound sound, float volume) {
      switch (sound) {
         case OPEN:
            emit(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.8F, 0.45F * volume);
            break;
         case CLOSE:
            emit(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.3F, 0.35F * volume);
            break;
         case TAB:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_BELL.value(), 1.7F, 0.3F * volume);
            break;
         case HOVER:
            emit(SoundEvents.AMETHYST_CLUSTER_STEP, 2.0F, 0.1F * volume);
            break;
         case CLICK:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_HAT.value(), 1.8F, 0.22F * volume);
            break;
         case TOGGLE_ON:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.6F, 0.38F * volume);
            break;
         case TOGGLE_OFF:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.19F, 0.32F * volume);
            break;
         case SLIDER:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_HAT.value(), 2.0F, 0.07F * volume);
            break;
         case SEARCH:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_BELL.value(), 1.4F, 0.35F * volume);
            break;
         case CHARACTER:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_BELL.value(), note(), 0.35F * volume);
            break;
         case NOTIFY:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_BELL.value(), 1.26F, 0.45F * volume);
      }
   }

   private static void soft(UiSounds.Sound sound, float volume) {
      switch (sound) {
         case OPEN:
            emit(SoundEvents.WOOL_PLACE, 1.2F, 0.6F * volume);
            break;
         case CLOSE:
            emit(SoundEvents.WOOL_BREAK, 1.0F, 0.5F * volume);
            break;
         case TAB:
            emit(SoundEvents.MOSS_CARPET_PLACE, 1.3F, 0.55F * volume);
            break;
         case HOVER:
            emit(SoundEvents.WOOL_STEP, 1.5F, 0.12F * volume);
            break;
         case CLICK:
            emit(SoundEvents.WOODEN_BUTTON_CLICK_ON, 1.4F, 0.25F * volume);
            break;
         case TOGGLE_ON:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_XYLOPHONE.value(), 1.4F, 0.3F * volume);
            break;
         case TOGGLE_OFF:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_XYLOPHONE.value(), 1.0F, 0.26F * volume);
            break;
         case SLIDER:
            emit(SoundEvents.WOOL_STEP, 1.8F, 0.06F * volume);
            break;
         case SEARCH:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_FLUTE.value(), 1.3F, 0.3F * volume);
            break;
         case CHARACTER:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_FLUTE.value(), note(), 0.35F * volume);
            break;
         case NOTIFY:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_XYLOPHONE.value(), 1.19F, 0.4F * volume);
      }
   }

   private static void classic(UiSounds.Sound sound, float volume) {
      switch (sound) {
         case OPEN:
         case TAB:
         case CLICK:
         case TOGGLE_ON:
         case SEARCH:
            emit((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.25F * volume);
            break;
         case CLOSE:
         case TOGGLE_OFF:
            emit((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.85F, 0.22F * volume);
         case HOVER:
         case SLIDER:
         default:
            break;
         case CHARACTER:
            emit((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), note(), 0.3F * volume);
            break;
         case NOTIFY:
            emit(SoundEvents.UI_TOAST_IN, 1.0F, 0.5F * volume);
      }
   }

   private static float note() {
      return NOTES[RANDOM.nextInt(NOTES.length)];
   }

   private static void emit(SoundEvent event, float pitch, float volume) {
      Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event, pitch, volume));
   }

   public enum Category {
      MENU,
      HOVER,
      TOGGLES,
      NOTIFICATIONS,
      CHARACTER;
   }

   public enum Sound {
      OPEN(UiSounds.Category.MENU),
      CLOSE(UiSounds.Category.MENU),
      TAB(UiSounds.Category.MENU),
      HOVER(UiSounds.Category.HOVER),
      CLICK(UiSounds.Category.MENU),
      TOGGLE_ON(UiSounds.Category.TOGGLES),
      TOGGLE_OFF(UiSounds.Category.TOGGLES),
      SLIDER(UiSounds.Category.TOGGLES),
      SEARCH(UiSounds.Category.MENU),
      CHARACTER(UiSounds.Category.CHARACTER),
      NOTIFY(UiSounds.Category.NOTIFICATIONS);

      final UiSounds.Category category;

      Sound(UiSounds.Category category) {
         this.category = category;
      }
   }
}
