package dev.rose.sound;

import java.util.Locale;
import java.util.Random;


public enum BuiltinSounds {
   TICK,
   CLICK,
   SNAP,
   WOOD,
   BLIP,
   POP,
   BUBBLE,
   WATER,
   BELL,
   CHIME,
   CRYSTAL,
   GLASS,
   METAL,
   COIN,
   PUNCH,
   THUD,
   DRUM,
   LASER,
   ZAP,
   BOING,
   WHOOSH,
   HEARTBEAT,
   FANFARE,
   POWER_UP,
   POWER_DOWN,
   ORB;

   public String id() {
      return this.name().toLowerCase(Locale.ROOT);
   }

   public static BuiltinSounds byId(String id) {
      for (BuiltinSounds sound : values()) {
         if (sound.id().equals(id)) {
            return sound;
         }
      }

      return null;
   }

   public float[] render() {
      float[] out;
      switch (this) {
         case TICK: {
            out = Synth.buffer(0.06F);
            Synth.tone(out, 0.0F, 0.05F, 3600.0F, 3300.0F, Synth.Wave.SINE, 1.0F, 6.0E-4F, 0.007F);
            Synth.tone(out, 0.0F, 0.05F, 7100.0F, 6800.0F, Synth.Wave.SINE, 0.25F, 4.0E-4F, 0.004F);
            Synth.noise(out, 0.0F, 0.006F, 0.35F, 0.0F, 0.0015F, 0.0F, 0.6F, 11L);
            break;
         }
         case CLICK: {
            out = Synth.buffer(0.05F);
            Synth.tone(out, 0.0F, 0.03F, 1900.0F, 1200.0F, Synth.Wave.SQUARE, 0.35F, 4.0E-4F, 0.004F);
            Synth.tone(out, 0.0F, 0.04F, 620.0F, 520.0F, Synth.Wave.SINE, 0.8F, 6.0E-4F, 0.01F);
            break;
         }
         case SNAP: {
            out = Synth.buffer(0.04F);
            Synth.noise(out, 0.0F, 0.03F, 1.0F, 0.0F, 0.004F, 0.0F, 0.82F, 21L);
            Synth.tone(out, 0.0F, 0.02F, 2400.0F, 1800.0F, Synth.Wave.SINE, 0.3F, 3.0E-4F, 0.004F);
            break;
         }
         case WOOD: {
            out = Synth.buffer(0.1F);
            Synth.tone(out, 0.0F, 0.09F, 880.0F, 840.0F, Synth.Wave.SINE, 1.0F, 8.0E-4F, 0.025F);
            Synth.tone(out, 0.0F, 0.05F, 1760.0F, 1700.0F, Synth.Wave.SINE, 0.4F, 5.0E-4F, 0.012F);
            Synth.noise(out, 0.0F, 0.005F, 0.3F, 0.0F, 0.002F, 0.3F, 0.0F, 31L);
            break;
         }
         case BLIP: {
            out = Synth.buffer(0.08F);
            Synth.tone(out, 0.0F, 0.07F, 1320.0F, 1320.0F, Synth.Wave.SINE, 1.0F, 0.001F, 0.022F);
            Synth.tone(out, 0.0F, 0.07F, 2640.0F, 2640.0F, Synth.Wave.TRIANGLE, 0.15F, 0.001F, 0.015F);
            break;
         }
         case POP: {
            out = Synth.buffer(0.12F);
            Synth.tone(out, 0.0F, 0.1F, 950.0F, 170.0F, Synth.Wave.SINE, 1.0F, 8.0E-4F, 0.035F);
            Synth.noise(out, 0.0F, 0.012F, 0.25F, 0.0F, 0.004F, 0.2F, 0.0F, 41L);
            break;
         }
         case BUBBLE: {
            out = Synth.buffer(0.2F);
            Synth.tone(out, 0.0F, 0.1F, 320.0F, 1500.0F, Synth.Wave.SINE, 1.0F, 0.004F, 0.06F);
            Synth.tone(out, 0.07F, 0.1F, 500.0F, 1900.0F, Synth.Wave.SINE, 0.5F, 0.004F, 0.045F);
            break;
         }
         case WATER: {
            out = Synth.buffer(0.35F);
            Random random = new Random(51L);

            for (int i = 0; i < 5; i++) {
               float start = random.nextFloat() * 0.24F;
               float base = 500.0F + random.nextFloat() * 500.0F;
               Synth.tone(out, start, 0.08F, base, base * 2.6F, Synth.Wave.SINE, 0.5F + random.nextFloat() * 0.5F, 0.003F, 0.03F);
            }
            break;
         }
         case BELL: {
            out = Synth.buffer(1.2F);
            float[] partials = new float[]{1046.5F, 2093.0F, 3139.5F, 4186.0F, 5232.0F};
            float[] amps = new float[]{1.0F, 0.5F, 0.28F, 0.14F, 0.07F};

            for (int i = 0; i < partials.length; i++) {
               Synth.tone(out, 0.0F, 1.2F, partials[i], partials[i] * 0.998F, Synth.Wave.SINE, amps[i], 0.002F, 0.9F / (1.0F + i * 0.6F));
            }
            break;
         }
         case CHIME: {
            out = Synth.buffer(0.7F);
            float[] notes = new float[]{1568.0F, 1975.5F, 2349.3F};

            for (int i = 0; i < notes.length; i++) {
               Synth.tone(out, i * 0.075F, 0.5F, notes[i], notes[i], Synth.Wave.SINE, 1.0F, 0.002F, 0.28F);
               Synth.tone(out, i * 0.075F, 0.3F, notes[i] * 2.0F, notes[i] * 2.0F, Synth.Wave.SINE, 0.25F, 0.002F, 0.12F);
            }
            break;
         }
         case CRYSTAL: {
            out = Synth.buffer(0.8F);
            Synth.tone(out, 0.0F, 0.8F, 4186.0F, 4180.0F, Synth.Wave.SINE, 1.0F, 0.001F, 0.3F);
            Synth.tone(out, 0.01F, 0.6F, 6272.0F, 6260.0F, Synth.Wave.SINE, 0.55F, 0.001F, 0.2F);
            Synth.tone(out, 0.03F, 0.5F, 8372.0F, 8360.0F, Synth.Wave.SINE, 0.25F, 0.001F, 0.12F);
            Synth.tone(out, 0.06F, 0.6F, 5274.0F, 5270.0F, Synth.Wave.SINE, 0.35F, 0.002F, 0.22F);
            break;
         }
         case GLASS: {
            out = Synth.buffer(0.45F);
            float[] partials = new float[]{2637.0F, 3951.0F, 5274.0F, 6592.0F};

            for (int i = 0; i < partials.length; i++) {
               Synth.tone(out, 0.0F, 0.45F, partials[i] * 1.004F, partials[i], Synth.Wave.SINE, 1.0F / (1 + i), 8.0E-4F, 0.16F / (1.0F + i * 0.4F));
            }

            Synth.noise(out, 0.0F, 0.01F, 0.3F, 0.0F, 0.003F, 0.0F, 0.7F, 61L);
            break;
         }
         case METAL: {
            out = Synth.buffer(0.9F);
            float[] partials = new float[]{523.0F, 1307.0F, 1989.0F, 2903.0F, 3721.0F};

            for (int i = 0; i < partials.length; i++) {
               Synth.tone(out, 0.0F, 0.9F, partials[i], partials[i], Synth.Wave.SINE, 1.0F / (1.0F + i * 0.7F), 0.001F, 0.5F / (1.0F + i * 0.5F));
            }

            Synth.noise(out, 0.0F, 0.015F, 0.4F, 0.0F, 0.005F, 0.0F, 0.5F, 71L);
            break;
         }
         case COIN: {
            out = Synth.buffer(0.42F);
            Synth.tone(out, 0.0F, 0.07F, 988.0F, 988.0F, Synth.Wave.SQUARE, 0.5F, 0.001F, 0.2F);
            Synth.tone(out, 0.07F, 0.35F, 1319.0F, 1319.0F, Synth.Wave.SQUARE, 0.5F, 0.001F, 0.12F);
            Synth.soften(out, 0.55F);
            break;
         }
         case PUNCH: {
            out = Synth.buffer(0.3F);
            Synth.tone(out, 0.0F, 0.26F, 130.0F, 44.0F, Synth.Wave.SINE, 1.0F, 0.001F, 0.09F);
            Synth.noise(out, 0.0F, 0.06F, 0.55F, 0.0F, 0.018F, 0.75F, 0.0F, 81L);
            break;
         }
         case THUD: {
            out = Synth.buffer(0.3F);
            Synth.tone(out, 0.0F, 0.3F, 90.0F, 60.0F, Synth.Wave.SINE, 1.0F, 0.002F, 0.12F);
            Synth.noise(out, 0.0F, 0.05F, 0.3F, 0.0F, 0.02F, 0.9F, 0.0F, 91L);
            break;
         }
         case DRUM: {
            out = Synth.buffer(0.25F);
            Synth.noise(out, 0.0F, 0.2F, 0.7F, 0.0F, 0.05F, 0.35F, 0.25F, 101L);
            Synth.tone(out, 0.0F, 0.2F, 200.0F, 140.0F, Synth.Wave.SINE, 0.8F, 0.001F, 0.08F);
            break;
         }
         case LASER: {
            out = Synth.buffer(0.2F);
            Synth.tone(out, 0.0F, 0.18F, 1900.0F, 240.0F, Synth.Wave.SAW, 0.8F, 0.001F, 0.07F);
            Synth.soften(out, 0.4F);
            break;
         }
         case ZAP: {
            out = Synth.buffer(0.18F);
            Synth.tone(out, 0.0F, 0.16F, 2400.0F, 520.0F, Synth.Wave.SAW, 0.6F, 5.0E-4F, 0.05F);
            Synth.noise(out, 0.0F, 0.14F, 0.45F, 0.0F, 0.05F, 0.2F, 0.3F, 111L);
            Synth.soften(out, 0.3F);
            break;
         }
         case BOING: {
            out = Synth.buffer(0.45F);
            int count = out.length;
            double phase = 0.0;

            for (int i = 0; i < count; i++) {
               float t = i / 44100.0F;
               double frequency = 380.0 * Math.pow(0.45, t / 0.45) * (1.0 + 0.08 * Math.sin(t * 2.0F * Math.PI * 17.0) * Math.exp(-t / 0.2));
               phase += frequency / 44100.0;
               out[i] = (float)(Math.sin(phase * Math.PI * 2.0) * Math.exp(-t / 0.22));
            }
            break;
         }
         case WHOOSH: {
            out = Synth.buffer(0.4F);
            Random random = new Random(121L);
            float low = 0.0F;
            float band = 0.0F;

            for (int i = 0; i < out.length; i++) {
               float t = (float)i / out.length;
               float cutoff = 0.93F - 0.35F * t;
               low += (random.nextFloat() * 2.0F - 1.0F - low) * (1.0F - cutoff);
               band += (low - band) * 0.08F;
               float envelope = (float)Math.sin(Math.PI * t);
               out[i] = (low - band) * envelope * envelope;
            }
            break;
         }
         case HEARTBEAT: {
            out = Synth.buffer(0.5F);
            Synth.tone(out, 0.0F, 0.18F, 70.0F, 50.0F, Synth.Wave.SINE, 1.0F, 0.004F, 0.06F);
            Synth.tone(out, 0.19F, 0.18F, 64.0F, 46.0F, Synth.Wave.SINE, 0.75F, 0.004F, 0.05F);
            break;
         }
         case FANFARE: {
            out = Synth.buffer(0.7F);
            float[] notes = new float[]{523.25F, 659.25F, 783.99F, 1046.5F};

            for (int i = 0; i < notes.length; i++) {
               boolean last = i == notes.length - 1;
               Synth.tone(out, i * 0.08F, last ? 0.45F : 0.1F, notes[i], notes[i], Synth.Wave.SQUARE, 0.4F, 0.002F, last ? 0.22F : 0.05F);
               Synth.tone(out, i * 0.08F, last ? 0.45F : 0.1F, notes[i] * 2.0F, notes[i] * 2.0F, Synth.Wave.SINE, 0.3F, 0.002F, last ? 0.18F : 0.04F);
            }

            Synth.soften(out, 0.5F);
            break;
         }
         case POWER_UP: {
            out = Synth.buffer(0.35F);
            Synth.tone(out, 0.0F, 0.32F, 300.0F, 1500.0F, Synth.Wave.TRIANGLE, 0.8F, 0.01F, 0.2F);
            Synth.tone(out, 0.05F, 0.28F, 600.0F, 3000.0F, Synth.Wave.SINE, 0.3F, 0.01F, 0.12F);
            break;
         }
         case POWER_DOWN: {
            out = Synth.buffer(0.35F);
            Synth.tone(out, 0.0F, 0.32F, 1500.0F, 260.0F, Synth.Wave.TRIANGLE, 0.8F, 0.004F, 0.18F);
            break;
         }
         case ORB: {
            out = Synth.buffer(0.35F);
            Synth.tone(out, 0.0F, 0.3F, 1760.0F, 1760.0F, Synth.Wave.SINE, 1.0F, 0.002F, 0.12F);
            Synth.tone(out, 0.04F, 0.3F, 2217.5F, 2217.5F, Synth.Wave.SINE, 0.7F, 0.002F, 0.1F);
            break;
         }
         default: {
            out = Synth.buffer(0.05F);
         }
      }

      return Synth.finish(out, 0.9F);
   }
}
