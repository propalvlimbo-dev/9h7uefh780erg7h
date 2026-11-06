package dev.rose.gui;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.render.ImageFiles;
import dev.rose.render.gpu.RoseTexture;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public final class Thumbnails {
   private static final Map<Path, Thumbnails.Thumbnail> CACHE = new HashMap<>();
   private static final Thumbnails.Thumbnail BROKEN = new Thumbnails.Thumbnail(null, 1, 1);

   private Thumbnails() {
   }

   public static Thumbnails.Thumbnail get(Path file) {
      Thumbnails.Thumbnail thumbnail = CACHE.computeIfAbsent(file, Thumbnails::load);
      return thumbnail == BROKEN ? null : thumbnail;
   }

   private static Thumbnails.Thumbnail load(Path file) {
      try {
         NativeImage image = ImageFiles.toNative(ImageFiles.read(file, 256));
         RoseTexture texture = new RoseTexture("thumbnail", image).filter(true, false);
         return new Thumbnails.Thumbnail(texture, image.getWidth(), image.getHeight());
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] cannot read picture {}", file, e);
         return BROKEN;
      }
   }

   public record Thumbnail(RoseTexture texture, int width, int height) {
   }
}
