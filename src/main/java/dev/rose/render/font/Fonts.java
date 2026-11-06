package dev.rose.render.font;

import dev.rose.RoseClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public final class Fonts {
   private static final Map<FontWeight, CompletableFuture<SdfFont.Prepared>> preparing = new EnumMap<>(FontWeight.class);
   private static final Map<FontWeight, SdfFont> ready = new EnumMap<>(FontWeight.class);
   private static CompletableFuture<SdfFont.Prepared> preparingIcons;
   private static SdfFont icons;
   private static Fonts.Family textFamily = Fonts.Family.ONEST;
   private static Fonts.Family titleFamily = Fonts.Family.UNBOUNDED;
   private static Path customFile;
   private static long customStamp;
   private static CompletableFuture<SdfFont.Prepared> preparingCustom;
   private static SdfFont custom;
   private static boolean customFailed;

   private Fonts() {
   }

   public static void preload() {
      for (FontWeight weight : FontWeight.values()) {
         preparing.computeIfAbsent(weight, w -> CompletableFuture.supplyAsync(() -> prepare(w.file, SdfFont.defaultCharset(), 48), Fonts::startThread));
      }

      if (preparingIcons == null) {
         preparingIcons = CompletableFuture.supplyAsync(() -> prepare("lucide", Icons.all(), 64), Fonts::startThread);
      }
   }

   public static void configure(Fonts.Family text, Fonts.Family title, Path file) {
      textFamily = text;
      titleFamily = title;
      boolean wantsCustom = text == Fonts.Family.CUSTOM || title == Fonts.Family.CUSTOM;
      if (wantsCustom && file != null) {
         long stamp;
         try {
            stamp = Files.getLastModifiedTime(file).toMillis();
         } catch (IOException e) {
            stamp = 0L;
         }

         if (!file.equals(customFile) || stamp != customStamp) {
            customFile = file;
            customStamp = stamp;
            customFailed = false;
            String name = "custom_" + Integer.toHexString(file.toAbsolutePath().toString().hashCode());
            preparingCustom = CompletableFuture.supplyAsync(() -> prepareFile(file, name), Fonts::startThread);
         }
      }
   }

   public static SdfFont get(FontWeight weight) {
      boolean title = weight == FontWeight.DISPLAY || weight == FontWeight.DISPLAY_BOLD || weight == FontWeight.DISPLAY_BLACK;
      Fonts.Family family = title ? titleFamily : textFamily;
      if (family == Fonts.Family.CUSTOM) {
         SdfFont font = custom();
         if (font != null) {
            return font;
         }

         family = title ? Fonts.Family.UNBOUNDED : Fonts.Family.ONEST;
      }

      FontWeight mapped = weight;
      if (title && family == Fonts.Family.ONEST) {
         mapped = weight == FontWeight.DISPLAY ? FontWeight.SEMIBOLD : FontWeight.BOLD;
      } else if (!title && family == Fonts.Family.UNBOUNDED) {
         mapped = weight != FontWeight.REGULAR && weight != FontWeight.MEDIUM ? FontWeight.DISPLAY_BOLD : FontWeight.DISPLAY;
      }

      return builtIn(mapped);
   }

   public static SdfFont builtIn(FontWeight weight) {
      SdfFont font = ready.get(weight);
      if (font != null) {
         return font;
      }

      CompletableFuture<SdfFont.Prepared> future = preparing.get(weight);
      if (future != null && future.isDone()) {
         preparing.remove(weight);
         font = upload(future, weight.file);
         if (font != null) {
            ready.put(weight, font);
         }

         return font;
      } else {
         return null;
      }
   }

   private static SdfFont custom() {
      if (customFailed) {
         return null;
      }

      if (preparingCustom != null && preparingCustom.isDone()) {
         CompletableFuture<SdfFont.Prepared> future = preparingCustom;
         preparingCustom = null;
         SdfFont font = upload(future, String.valueOf(customFile));
         if (font == null) {
            customFailed = true;
         } else {
            custom = font;
         }
      }

      return custom;
   }

   public static SdfFont icons() {
      if (icons == null && preparingIcons != null && preparingIcons.isDone()) {
         icons = upload(preparingIcons, "lucide");
         preparingIcons = null;
         return icons;
      } else {
         return icons;
      }
   }

   private static SdfFont upload(CompletableFuture<SdfFont.Prepared> future, String name) {
      try {
         return new SdfFont(future.join());
      } catch (RuntimeException e) {
         RoseClient.LOGGER.error("[Rose] font {} failed", name, e);
         return null;
      }
   }

   private static SdfFont.Prepared prepare(String file, int[] codepoints, int glyphSize) {
      long start = System.nanoTime();
      String path = "/assets/rose/fonts/" + file + ".ttf";

      try (InputStream in = Fonts.class.getResourceAsStream(path)) {
         if (in == null) {
            throw new FileNotFoundException(path);
         }

         SdfFont.Prepared prepared = new SdfFont.Prepared(file, in.readAllBytes(), codepoints, glyphSize);
         RoseClient.LOGGER.info("[Rose] font '{}' prepared in {} ms", file, (System.nanoTime() - start) / 1000000L);
         return prepared;
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   private static SdfFont.Prepared prepareFile(Path file, String name) {
      long start = System.nanoTime();

      try {
         SdfFont.Prepared prepared = new SdfFont.Prepared(name, Files.readAllBytes(file), SdfFont.defaultCharset(), 48);
         RoseClient.LOGGER.info("[Rose] custom font '{}' prepared in {} ms", file.getFileName(), (System.nanoTime() - start) / 1000000L);
         return prepared;
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   private static void startThread(Runnable task) {
      Thread thread = new Thread(task, "Rose Font Loader");
      thread.setDaemon(true);
      thread.start();
   }

   public enum Family {
      ONEST,
      UNBOUNDED,
      CUSTOM;
   }
}
