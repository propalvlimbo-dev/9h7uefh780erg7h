package dev.rose.render;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;


public final class ImageFiles {
   private ImageFiles() {
   }

   public static ImageFiles.Pixels read(Path file, int maxSize) throws IOException {
      byte[] bytes = Files.readAllBytes(file);
      ByteBuffer data = MemoryUtil.memAlloc(bytes.length);

      ImageFiles.Pixels var34;
      try {
         MemoryStack stack = MemoryStack.stackPush();

         try {
            data.put(bytes).flip();
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            ByteBuffer decoded = STBImage.stbi_load_from_memory(data, w, h, channels, 4);
            if (decoded == null) {
               throw new IOException("Cannot decode " + file.getFileName() + ": " + STBImage.stbi_failure_reason());
            }

            int width = w.get(0);
            int height = h.get(0);
            int[] argb = new int[width * height];

            try {
               for (int i = 0; i < argb.length; i++) {
                  int o = i * 4;
                  int r = decoded.get(o) & 255;
                  int g = decoded.get(o + 1) & 255;
                  int b = decoded.get(o + 2) & 255;
                  int a = decoded.get(o + 3) & 255;
                  argb[i] = a << 24 | r << 16 | g << 8 | b;
               }
            } finally {
               STBImage.stbi_image_free(decoded);
            }

            ImageFiles.Pixels var32 = new ImageFiles.Pixels(width, height, argb);
            int var33 = 1;

            while (Math.max(width, height) / var33 > maxSize) {
               var33++;
            }

            var34 = var33 > 1 ? downscale(var32, var33) : var32;
         } catch (Throwable var30) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var28) {
                  var30.addSuppressed(var28);
               }
            }

            throw var30;
         }

         if (stack != null) {
            stack.close();
         }
      } finally {
         MemoryUtil.memFree(data);
      }

      return var34;
   }

   public static ImageFiles.Pixels downscale(ImageFiles.Pixels source, int factor) {
      int width = Math.max(1, source.width / factor);
      int height = Math.max(1, source.height / factor);
      int[] out = new int[width * height];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            long a = 0L;
            long r = 0L;
            long g = 0L;
            long b = 0L;
            int n = 0;

            for (int dy = 0; dy < factor; dy++) {
               int sy = y * factor + dy;
               if (sy >= source.height) {
                  break;
               }

               for (int dx = 0; dx < factor; dx++) {
                  int sx = x * factor + dx;
                  if (sx >= source.width) {
                     break;
                  }

                  int c = source.argb[sy * source.width + sx];
                  a += c >>> 24;
                  r += c >> 16 & 0xFF;
                  g += c >> 8 & 0xFF;
                  b += c & 0xFF;
                  n++;
               }
            }

            out[y * width + x] = (int)(a / n) << 24 | (int)(r / n) << 16 | (int)(g / n) << 8 | (int)(b / n);
         }
      }

      return new ImageFiles.Pixels(width, height, out);
   }

   public static ImageFiles.Pixels blur(ImageFiles.Pixels source, int radius) {
      if (radius <= 0) {
         return source;
      }

      int[] a = (int[])source.argb.clone();
      int[] b = new int[a.length];

      for (int pass = 0; pass < 3; pass++) {
         box(a, b, source.width, source.height, radius, true);
         box(b, a, source.width, source.height, radius, false);
      }

      return new ImageFiles.Pixels(source.width, source.height, a);
   }

   private static void box(int[] in, int[] out, int width, int height, int radius, boolean horizontal) {
      int length = horizontal ? width : height;
      int lines = horizontal ? height : width;

      for (int line = 0; line < lines; line++) {
         long sa = 0L;
         long sr = 0L;
         long sg = 0L;
         long sb = 0L;
         int window = radius * 2 + 1;

         for (int k = -radius; k <= radius; k++) {
            int c = in[index(line, Math.clamp(k, 0, length - 1), width, horizontal)];
            sa += c >>> 24;
            sr += c >> 16 & 0xFF;
            sg += c >> 8 & 0xFF;
            sb += c & 0xFF;
         }

         for (int i = 0; i < length; i++) {
            out[index(line, i, width, horizontal)] = (int)(sa / window) << 24 | (int)(sr / window) << 16 | (int)(sg / window) << 8 | (int)(sb / window);
            int add = in[index(line, Math.min(i + radius + 1, length - 1), width, horizontal)];
            int remove = in[index(line, Math.max(i - radius, 0), width, horizontal)];
            sa += (add >>> 24) - (remove >>> 24);
            sr += (add >> 16 & 0xFF) - (remove >> 16 & 0xFF);
            sg += (add >> 8 & 0xFF) - (remove >> 8 & 0xFF);
            sb += (add & 0xFF) - (remove & 0xFF);
         }
      }
   }

   private static int index(int line, int i, int width, boolean horizontal) {
      return horizontal ? line * width + i : i * width + line;
   }

   public static NativeImage toNative(ImageFiles.Pixels pixels) {
      NativeImage image = new NativeImage(pixels.width, pixels.height, false);

      for (int y = 0; y < pixels.height; y++) {
         for (int x = 0; x < pixels.width; x++) {
            int c = pixels.argb[y * pixels.width + x];
            image.setPixelABGR(x, y, c & -16711936 | c >> 16 & 0xFF | (c & 0xFF) << 16);
         }
      }

      return image;
   }

   public record Pixels(int width, int height, int[] argb) {
   }
}
