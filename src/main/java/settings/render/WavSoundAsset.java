package settings.render;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;

public final class WavSoundAsset {
   private static final String ХЦ = "/assets/rose/sounds/";
   private static final String Хч = ".wav";
   private static final int т4 = 4096;
   private static final int тп = 16;
   private final String ХЭ;
   private byte[] А;
   private boolean А西;

   private AudioInputStream Р(AudioInputStream var1) {
      AudioFormat var2 = var1.getFormat();
      if (var2.getEncoding() == Encoding.PCM_SIGNED && var2.getSampleSizeInBits() == 16 && !var2.isBigEndian()) {
         return var1;
      }

      int var3 = var2.getChannels();
      AudioFormat var4 = new AudioFormat(Encoding.PCM_SIGNED, var2.getSampleRate(), 16, var3, var3 * 16 / 8, var2.getSampleRate(), false);
      return AudioSystem.getAudioInputStream(var4, var1);
   }

   private void Р(AudioInputStream var1, float var2) throws LineUnavailableException, IOException {
      AudioFormat var3 = var1.getFormat();

      try (SourceDataLine var4 = AudioSystem.getSourceDataLine(var3)) {
         var4.open(var3);
         var4.start();
         byte[] var5 = new byte[4096];

         int var6;
         while ((var6 = var1.read(var5)) > 0) {
            this.Р(var5, var6, var3, var2);
            var4.write(var5, 0, var6);
         }

         var4.drain();
         var4.stop();
      }
   }

   public boolean О1() {
      return this.А西;
   }

   public WavSoundAsset(String var1) {
      this.ХЭ = var1;
   }

   private void Р(byte[] var1, int var2, AudioFormat var3, float var4) {
      if (var4 != 1.0F) {
         if (var3.getSampleSizeInBits() == 16 && !var3.isBigEndian()) {
            for (int var5 = 0; var5 + 1 < var2; var5 += 2) {
               short var6 = (short)(var1[var5] & 0xFF | var1[var5 + 1] << 8);
               int var7 = Math.clamp(Math.round(var6 * var4), -32768, 32767);
               var1[var5] = (byte)var7;
               var1[var5 + 1] = (byte)(var7 >> 8);
            }
         }
      }
   }

   private synchronized byte[] э() {
      if (this.А == null && !this.А西) {
         try (InputStream var1 = WavSoundAsset.class.getResourceAsStream("/assets/rose/sounds/" + this.ХЭ + ".wav")) {
            if (var1 == null) {
               this.А西 = true;
               return null;
            }

            this.А = var1.readAllBytes();
         } catch (IOException var6) {
            this.А西 = true;
         }

         return this.А;
      } else {
         return this.А;
      }
   }

   public void 诶в(float var1) {
      byte[] var2 = this.э();
      if (var2 != null) {
         try (
            AudioInputStream var3 = AudioSystem.getAudioInputStream(new ByteArrayInputStream(var2));
            AudioInputStream var4 = this.Р(var3);
         ) {
            this.Р(var4, var1);
         } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | IllegalArgumentException var11) {
            this.А西 = true;
         }
      }
   }

   public String До() {
      return this.ХЭ;
   }
}
