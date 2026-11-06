package dev.rose.module.impl.cosmetic;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ImageChoiceSetting;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;


public final class CapeModule extends Module {
   private static final Path CAPES_DIR = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("capes");
   private static List<Path> cached = List.of();
   private static long cachedAt;
   private final ChoiceSetting<CapeModule.Source> source = this.add(new ChoiceSetting<>("source", CapeModule.Source.GRADIENT));
   private final ImageChoiceSetting image = this.add(new ImageChoiceSetting("image", CapeModule::capeImages));

   public CapeModule() {
      super("cape", Category.COSMETICS, false);
      this.image.visibleWhen(() -> this.source.get() == CapeModule.Source.IMAGE);
      this.add(new ActionSetting("open_folder", 57927, () -> {
         try {
            Files.createDirectories(CAPES_DIR);
         } catch (IOException var1) {
         }

         Util.getPlatform().openPath(CAPES_DIR);
      })).visibleWhen(() -> this.source.get() == CapeModule.Source.IMAGE);
   }

   @Override
   public int icon() {
      return 57802;
   }

   private static List<Path> capeImages() {
      long now = System.currentTimeMillis();
      if (now - cachedAt < 2000L) {
         return cached;
      }

      cachedAt = now;

      try (Stream<Path> files = Files.list(CAPES_DIR)) {
         cached = files.filter(file -> file.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png")).sorted().toList();
      } catch (IOException e) {
         cached = List.of();
      }

      return cached;
   }

   public Identifier texture() {
      if (!this.isEnabled()) {
         return null;
      }

      if (this.source.get() == CapeModule.Source.GRADIENT) {
         return CosmeticTextures.gradientCape();
      }

      Path file = this.image.selected();
      return file == null ? null : CosmeticTextures.cape(file);
   }

   public enum Source {
      GRADIENT,
      IMAGE;
   }
}
