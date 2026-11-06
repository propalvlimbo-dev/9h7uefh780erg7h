package dev.rose.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import org.lwjgl.BufferUtils;


public final class Synth {
   public static final int RATE = 44100;

   private Synth() {
   }

   public static float[] buffer(float seconds) {
      return new float[Math.max(1, Math.round(seconds * 44100.0F))];
   }

   public static void tone(float[] out, float start, float duration, float from, float to, Synth.Wave wave, float amplitude, float attack, float decay) {
      int first = Math.round(start * 44100.0F);
      int count = Math.round(duration * 44100.0F);
      double phase = 0.0;

      for (int i = 0; i < count && first + i < out.length; i++) {
         float t = i / 44100.0F;
         float progress = count <= 1 ? 0.0F : (float)i / (count - 1);
         double frequency = from * Math.pow(to / from, progress);
         phase += frequency / 44100.0;
         double cycle = phase - Math.floor(phase);

         float sample = switch (wave) {
            case SINE -> (float)Math.sin(cycle * Math.PI * 2.0);
            case SQUARE -> cycle < 0.5 ? 1.0F : -1.0F;
            case SAW -> (float)(cycle * 2.0 - 1.0);
            case TRIANGLE -> (float)(cycle < 0.5 ? cycle * 4.0 - 1.0 : 3.0 - cycle * 4.0);
         };
         out[first + i] = out[first + i] + sample * envelope(t, attack, decay) * amplitude;
      }
   }

   public static void noise(float[] out, float start, float duration, float amplitude, float attack, float decay, float lowpass, float highpass, long seed) {
      Random random = new Random(seed);
      int first = Math.round(start * 44100.0F);
      int count = Math.round(duration * 44100.0F);
      float low = 0.0F;
      float lowForHigh = 0.0F;

      for (int i = 0; i < count && first + i < out.length; i++) {
         float t = i / 44100.0F;
         float sample = random.nextFloat() * 2.0F - 1.0F;
         if (lowpass > 0.0F) {
            low += (sample - low) * (1.0F - lowpass);
            sample = low;
         }

         if (highpass > 0.0F) {
            lowForHigh += (sample - lowForHigh) * (1.0F - highpass);
            sample -= lowForHigh;
         }

         out[first + i] = out[first + i] + sample * envelope(t, attack, decay) * amplitude;
      }
   }

   private static float envelope(float t, float attack, float decay) {
      float rise = attack <= 0.0F ? 1.0F : Math.min(1.0F, t / attack);
      return rise * (float)Math.exp(-t / Math.max(5.0E-4F, decay));
   }

   public static void soften(float[] data, float amount) {
      float low = 0.0F;

      for (int i = 0; i < data.length; i++) {
         low += (data[i] - low) * (1.0F - amount);
         data[i] = low;
      }
   }

   public static float[] finish(float[] data, float peak) {
      float max = 0.0F;

      for (float sample : data) {
         max = Math.max(max, Math.abs(sample));
      }

      float gain = max <= 0.0F ? 1.0F : peak / max;
      int fade = Math.min(data.length / 4, 220);

      for (int i = 0; i < data.length; i++) {
         float edge = 1.0F;
         if (i < 40) {
            edge = i / 40.0F;
         }

         if (i > data.length - fade) {
            edge = Math.min(edge, (float)(data.length - i) / fade);
         }

         data[i] *= gain * edge;
      }

      return data;
   }

   public static ByteBuffer toPcm(float[] data) {
      ByteBuffer buffer = BufferUtils.createByteBuffer(data.length * 2).order(ByteOrder.LITTLE_ENDIAN);

      for (float sample : data) {
         buffer.putShort((short)Math.round(Math.clamp(sample, -1.0F, 1.0F) * 32767.0F));
      }

      buffer.flip();
      return buffer;
   }

   public enum Wave {
      SINE,
      SQUARE,
      SAW,
      TRIANGLE;
   }
}
