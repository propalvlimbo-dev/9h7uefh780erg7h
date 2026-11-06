package dev.rose.sound;

import dev.rose.RoseClient;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.sound.sampled.AudioFormat;
import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.valueproviders.ConstantFloat;
import org.lwjgl.BufferUtils;


public final class SoundLibrary {
   public static final String NONE = "none";
   public static final Path FOLDER = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("sounds");
   private static final Identifier LOCATION = Identifier.fromNamespaceAndPath("rose", "synth");
   private static final AudioFormat MONO_16 = new AudioFormat(44100.0F, 16, 1, true, false);
   private static final Map<BuiltinSounds, ByteBuffer> RENDERED = new HashMap<>();
   private static List<String> files = List.of();
   private static long scannedAt;

   private SoundLibrary() {
   }

   public static List<String> options() {
      List<String> list = new ArrayList<>();
      list.add("none");

      for (PackSounds sound : PackSounds.choosable()) {
         list.add(sound.id());
      }

      for (BuiltinSounds sound : BuiltinSounds.values()) {
         list.add(sound.id());
      }

      for (String file : files()) {
         list.add("file:" + file);
      }

      return list;
   }

   public static List<String> files() {
      long now = System.currentTimeMillis();
      if (now - scannedAt < 2000L) {
         return files;
      }

      scannedAt = now;

      try {
         Files.createDirectories(FOLDER);

         try (Stream<Path> stream = Files.list(FOLDER)) {
            files = stream.<String>map(path -> path.getFileName().toString())
               .filter(name -> name.toLowerCase(Locale.ROOT).endsWith(".ogg") || name.toLowerCase(Locale.ROOT).endsWith(".wav"))
               .sorted()
               .toList();
         }
      } catch (IOException e) {
         files = List.of();
      }

      return files;
   }

   public static void play(String id, float volume, float pitch) {
      if (id != null && !id.equals("none") && !(volume <= 0.001F)) {
         Minecraft mc = Minecraft.getInstance();
         SoundLibrary.Source source;
         if (id.startsWith("file:")) {
            Path file = FOLDER.resolve(id.substring(5)).normalize();
            if (!file.startsWith(FOLDER) || !Files.isRegularFile(file)) {
               return;
            }

            source = () -> open(file);
         } else {
            PackSounds packed = PackSounds.byId(id);
            if (packed != null) {
               source = () -> new JOrbisAudioStream(new BufferedInputStream(mc.getResourceManager().open(packed.file())));
            } else {
               BuiltinSounds sound = BuiltinSounds.byId(id);
               if (sound == null) {
                  return;
               }

               ByteBuffer pcm;
               synchronized (RENDERED) {
                  pcm = RENDERED.computeIfAbsent(sound, s -> Synth.toPcm(s.render()));
               }

               source = () -> new SoundLibrary.PcmStream(pcm.duplicate(), MONO_16);
            }
         }

         mc.getSoundManager().play(new SoundLibrary.Instance(source, Math.min(volume, 2.0F), Math.clamp(pitch, 0.5F, 2.0F)));
      }
   }

   private static AudioStream open(Path file) throws IOException {
      String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
      return (AudioStream)(name.endsWith(".ogg") ? new JOrbisAudioStream(new BufferedInputStream(Files.newInputStream(file))) : readWav(file));
   }

   private static AudioStream readWav(Path file) throws IOException {
      ByteBuffer data = ByteBuffer.wrap(Files.readAllBytes(file)).order(ByteOrder.LITTLE_ENDIAN);
      if (data.remaining() >= 12 && data.getInt(0) == 1179011410 && data.getInt(8) == 1163280727) {
         int position = 12;
         int channels = 1;
         int rate = 44100;
         int bits = 16;
         ByteBuffer pcm = null;

         while (position + 8 <= data.limit()) {
            int chunk = data.getInt(position);
            int size = data.getInt(position + 4);
            int body = position + 8;
            if (chunk == 544501094) {
               if (data.getShort(body) != 1) {
                  throw new IOException("compressed WAV is not supported");
               }

               channels = data.getShort(body + 2);
               rate = data.getInt(body + 4);
               bits = data.getShort(body + 14);
            } else if (chunk == 1635017060) {
               int length = Math.min(size, data.limit() - body);
               pcm = BufferUtils.createByteBuffer(length);
               pcm.put(data.array(), body, length).flip();
            }

            position = body + size + (size & 1);
         }

         if (pcm != null && (bits == 8 || bits == 16) && channels >= 1 && channels <= 2) {
            return new SoundLibrary.PcmStream(pcm, new AudioFormat(rate, bits, channels, bits == 16, false));
         } else {
            throw new IOException("unsupported WAV format");
         }
      } else {
         throw new IOException("not a WAV file");
      }
   }

   private static final class Instance extends AbstractSoundInstance implements FabricSoundInstance {
      private static final WeighedSoundEvents EVENTS = new WeighedSoundEvents(SoundLibrary.LOCATION, null);
      private final SoundLibrary.Source source;

      Instance(SoundLibrary.Source source, float volume, float pitch) {
         super(SoundLibrary.LOCATION, SoundSource.MASTER, RandomSource.create());
         this.source = source;
         this.volume = volume;
         this.pitch = pitch;
         this.relative = true;
         this.attenuation = SoundInstance.Attenuation.NONE;
      }

      public WeighedSoundEvents resolve(SoundManager manager) {
         this.sound = new Sound(
            SoundLibrary.LOCATION, ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), 1, Sound.Type.FILE, true, false, 16
         );
         return EVENTS;
      }

      public CompletableFuture<AudioStream> getAudioStream(SoundBufferLibrary library, Identifier id, boolean repeatInstantly) {
         return CompletableFuture.supplyAsync(() -> {
            try {
               return this.source.open();
            } catch (IOException e) {
               RoseClient.LOGGER.warn("[Rose] cannot play sound: {}", e.getMessage());
               return new SoundLibrary.PcmStream(BufferUtils.createByteBuffer(0), SoundLibrary.MONO_16);
            }
         }, Util.backgroundExecutor());
      }
   }

   private static final class PcmStream implements AudioStream {
      private final ByteBuffer data;
      private final AudioFormat format;

      PcmStream(ByteBuffer data, AudioFormat format) {
         this.data = data;
         this.format = format;
      }

      public AudioFormat getFormat() {
         return this.format;
      }

      public ByteBuffer read(int size) {
         if (!this.data.hasRemaining()) {
            return null;
         }

         int frame = this.format.getFrameSize();
         int length = Math.min(this.data.remaining(), Math.max(frame, size - size % frame));
         ByteBuffer chunk = BufferUtils.createByteBuffer(length);
         ByteBuffer slice = this.data.slice();
         slice.limit(length);
         chunk.put(slice).flip();
         this.data.position(this.data.position() + length);
         return chunk;
      }

      public void close() {
      }
   }

   @FunctionalInterface
   private interface Source {
      AudioStream open() throws IOException;
   }
}
