package sg.modules.impl.init;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;

public final class UnhookFileManager {
   private static final String сЬ = "Sk3dGuardNew";
   private static final String с衣 = "Dimas";
   private static final String сш = "resourcepacks";
   private static final String сИ = "mods";
   private final List<UnhookFileManager.HiddenFile> Г = new ArrayList<>();
   private final List<UnhookFileManager.FileAttributes> л = new ArrayList<>();
   private Path Я;
   private Path Х;
   private boolean сЩ;
   private static final String чЙ = "Dimas";
   private static final String чк = "resourcepacks";
   private static final String чг = "APPDATA";
   private static final String ч0 = ".minecraft";
   private static final String чх = "Minecraft";
   private static final String чУ = "WINEPREFIX";
   private static final String чЗ = "STEAM_COMPAT_DATA_PATH";
   private static final String чЕ = "pfx";
   private static final String чП = "user.home";
   private static final String ч西 = ".minecraft";
   private static final String чд = ".wine";
   private static final String чБ = ".minecraft";
   private static final String чС = ".minecraft";
   private static final String чз = "drive_c";
   private static final String чц = "users";
   private static final String чя = "steamuser";
   private static final String Эс = "AppData";
   private static final String ЭВ = "Roaming";
   private static final String ЭЯ = ".minecraft";
   private static final String ЭХ = "user.name";
   private static final String Эе = "AppData";
   private static final String ЭА = "Roaming";
   private static final String Э4 = ".minecraft";
   private static final String Эп = "AppData";
   private static final String ЭД = "Roaming";
   private static final String ЭЪ = ".minecraft";
   private static final String Эт = "Sk3dGuardNew";
   private static final String Эа = "SystemDrive";
   private static final String ЭЮ = "WINEPREFIX";
   private static final String ЭЧ = "drive_c";
   private static final String ЭЛ = "STEAM_COMPAT_DATA_PATH";
   private static final String Э诶 = "pfx";
   private static final String Э1 = "drive_c";
   private static final String ЭЬ = "user.home";
   private static final String Э衣 = ".wine";
   private static final String Эш = "drive_c";
   private static final String ЭИ = "/";
   private static final String Эь = ".steam/steam/steamapps/compatdata";
   private static final String Эж = ".local/share/Steam/steamapps/compatdata";
   private static final String Э弟 = "pfx";
   private static final String Эб = "drive_c";
   private static final String ЭЫ = "resourcepacks";
   private static final String Эl = "mods";
   private static final String Э8 = ".jar";
   private static final String Э5 = ".zip";
   private static final String Эв = "pack.mcmeta";
   private static final String Эъ = "fabric.mod.json";
   private static final String ЭМ = "quilt.mod.json";
   private static final String ЭН = "META-INF";
   private static final String Эн = "mods.toml";
   private static final String ЭЦ = "META-INF";
   private static final String Эч = "neoforge.mods.toml";
   private static final String ЭЭ = "fabric.mod.json";
   private static final String Э必 = "quilt.mod.json";
   private static final String Эщ = "META-INF/mods.toml";
   private static final String ЭГ = "META-INF/neoforge.mods.toml";
   private static final String Эл = "pack.mcmeta";
   private static final String Ээ = ".";
   private static final String Эю = "WINEPREFIX";
   private static final String ЭЩ = "STEAM_COMPAT_DATA_PATH";
   private static final String Эу = "os.name";
   private static final String Э2 = "win";
   private static final String Эф = "run";
   private static final String Эи = "/ideaprojects/";
   private static final String Э9 = "/projects/";
   private static final String Эо = "/src/";
   private static final String ЭO = "build.gradle";
   private static final String Э7 = "settings.gradle";
   private static final String ЭР = "resourcepacks";
   private static final String Э3 = "mods";

   private static boolean method_9_bbeebd87(Path var0, Path var1) {
      Path var2 = Д_2b4785d0(var0);
      Path var3 = Д_2b4785d0(var1);
      return var2 != null && var3 != null && var2.startsWith(var3);
   }

   private static boolean method_9Б() {
      return !method_9м() ? true : б(Эю) || б(ЭЩ);
   }

   private static boolean Д_bbeebd87(Path var0) {
      return method_9(var0, ЭЭ, Э必, Эщ, ЭГ);
   }

   private static boolean method_9(Path var0, String... var1) {
      try (ZipFile var2 = new ZipFile(var0.toFile())) {
         for (String var6 : var1) {
            ZipEntry var7 = var2.getEntry(var6);
            if (var7 != null) {
               return true;
            }
         }
      } catch (IOException var11) {
      }

      return false;
   }

   public synchronized void method_9Щ() {
      this.method_6(() -> false);
   }

   private static Path ь_2b4785d0(Path var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.gameDirectory != null) {
         Path var2 = Д_2b4785d0(var1.gameDirectory.toPath());
         if (!method_9_bbeebd87(var2, var0) && !method_6(var2)) {
            return var2;
         }
      }

      LinkedHashSet<Path> var9 = new LinkedHashSet<>();
      String var3 = System.getenv(чг);
      if (var3 != null && !var3.isBlank()) {
         var9.add(Path.of(var3).resolve(ч0));
         var9.add(Path.of(var3).resolve(чх));
      }

      method_9(var9, System.getenv(чУ));
      String var4 = System.getenv(чЗ);
      if (var4 != null && !var4.isBlank()) {
         method_9(var9, Path.of(var4).resolve(чЕ).toString());
      }

      String var5 = System.getProperty(чП);
      if (var5 != null && !var5.isBlank()) {
         Path var6 = Path.of(var5);
         var9.add(var6.resolve(ч西));
         method_9(var9, var6.resolve(чд).toString());
      }

      for (Path var7 : var9) {
         Path var8 = Д_2b4785d0(var7);
         if (var8 != null && !method_9_bbeebd87(var8, var0) && Files.isDirectory(var8)) {
            return var8;
         }
      }

      if (var3 != null && !var3.isBlank()) {
         Path var11 = Д_2b4785d0(Path.of(var3).resolve(чБ));
         if (!method_9_bbeebd87(var11, var0)) {
            return var11;
         }
      }

      if (var5 != null && !var5.isBlank()) {
         Path var12 = Д_2b4785d0(Path.of(var5).resolve(чС));
         if (!method_9_bbeebd87(var12, var0)) {
            return var12;
         }
      }

      return null;
   }

   public static Path Д() {
      Path var0 = и();
      Path var1 = ь_2b4785d0(var0);
      if (var1 == null) {
         return null;
      }

      Path var2 = var1.resolve(чк);

      try {
         Files.createDirectories(var2);
      } catch (IOException var4) {
      }

      return var2;
   }

   private void method_9(Path var1, Path var2, BooleanSupplier var3) {
      if (!var3.getAsBoolean()) {
         Path var4 = var2.resolve(ЭЫ);
         Path var5 = var2.resolve(Эl);

         try {
            Files.createDirectories(var4);
            Files.createDirectories(var5);
         } catch (IOException var15) {
            return;
         }

         HashSet var6 = new HashSet();

         try (Stream<Path> var7 = Files.walk(var1)) {
            var7.takeWhile(var1x -> !var3.getAsBoolean()).filter(var0 -> Files.isDirectory(var0)).forEach(var5x -> {
               Path var6x = var5x.getFileName();
               if (var6x != null && !var5x.equals(var1)) {
                  String var7x = var6x.toString().toLowerCase(Locale.ROOT);
                  Path var8x;
                  if (ЭР.equals(var7x)) {
                     var8x = var4;
                  } else {
                     if (!Э3.equals(var7x)) {
                        return;
                     }

                     var8x = var5;
                  }

                  var6.add(Д_2b4785d0(var5x));
                  ь(var5x, var8x, var3);
               }
            });
         } catch (IOException var19) {
         }

         List<Path> var20;
         try (Stream<Path> var8 = Files.walk(var1)) {
            var20 = var8.takeWhile(var1x -> !var3.getAsBoolean())
               .filter(var1x -> !var1x.equals(var1))
               .sorted(Comparator.comparingInt(Path::getNameCount))
               .toList();
         } catch (IOException var17) {
            return;
         }

         HashSet var21 = new HashSet();

         for (Path var10 : var20) {
            if (var3.getAsBoolean()) {
               return;
            }

            Path var11 = Д_2b4785d0(var10);
            if (var11 != null && !method_9(var11, var6) && !method_9(var11, var21)) {
               if (Files.isDirectory(var10)) {
                  if (method_9(var10)) {
                     method_9_de5a6f78(var10, var4.resolve(var10.getFileName()));
                     var21.add(var11);
                  } else if (ь_bbeebd87(var10)) {
                     method_9_de5a6f78(var10, var5.resolve(var10.getFileName()));
                     var21.add(var11);
                  }
               } else if (Files.isRegularFile(var10)) {
                  String var12 = var10.getFileName().toString().toLowerCase(Locale.ROOT);
                  if (var12.endsWith(Э8) && Д_bbeebd87(var10)) {
                     method_9_de5a6f78(var10, var5.resolve(var10.getFileName()));
                  } else if (var12.endsWith(Э5) && и(var10)) {
                     method_9_de5a6f78(var10, var4.resolve(var10.getFileName()));
                  }
               }
            }
         }
      }
   }

   private static void ь(Path var0, Path var1, BooleanSupplier var2) {
      try (DirectoryStream<Path> var3 = Files.newDirectoryStream(var0)) {
         for (Path var5 : var3) {
            if (var2.getAsBoolean()) {
               return;
            }

            method_9_de5a6f78(var5, var1.resolve(var5.getFileName()));
         }
      } catch (IOException var8) {
      }
   }

   private static boolean и(Path var0) {
      return method_9(var0, new String[]{Эл});
   }

   private static boolean б(String var0) {
      String var1 = System.getenv(var0);
      return var1 != null && !var1.isBlank();
   }

   private static boolean method_6(Path var0) {
      Path var1 = Д_2b4785d0(var0);
      if (var1 == null) {
         return false;
      } else {
         Path var2 = var1.getFileName();
         if (var2 != null && Эф.equalsIgnoreCase(var2.toString())) {
            String var3 = var1.toString().replace('\\', '/').toLowerCase(Locale.ROOT);
            return var3.contains(Эи)
               || var3.contains(Э9)
               || var3.contains(Эо)
               || Files.exists(var1.getParent() == null ? var1 : var1.getParent().resolve(ЭO))
               || Files.exists(var1.getParent() == null ? var1 : var1.getParent().resolve(Э7));
         } else {
            return false;
         }
      }
   }

   private static boolean method_9(Path var0) {
      return Files.isRegularFile(var0.resolve(Эв));
   }

   public synchronized void method_9Ф() {
      if (!this.сЩ && this.Г.isEmpty() && this.л.isEmpty()) {
         this.Я = null;
         this.Х = null;
      } else {
         this.method_9О();
         this.method_9Ж();
         this.сЩ = false;
         this.Я = null;
         this.Х = null;
      }
   }

   private static boolean ь_bbeebd87(Path var0) {
      return Files.isRegularFile(var0.resolve(Эъ))
         || Files.isRegularFile(var0.resolve(ЭМ))
         || Files.isRegularFile(var0.resolve(ЭН).resolve(Эн))
         || Files.isRegularFile(var0.resolve(ЭЦ).resolve(Эч));
   }

   private void method_9Ж() {
      Iterator var1 = this.л.iterator();

      while (var1.hasNext()) {
         UnhookFileManager.FileAttributes var2 = (UnhookFileManager.FileAttributes)var1.next();
         if (!Files.exists(var2.path(), LinkOption.NOFOLLOW_LINKS)) {
            var1.remove();
         } else {
            DosFileAttributeView var3 = Files.getFileAttributeView(var2.path(), DosFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            if (var3 == null) {
               throw new IllegalStateException("DOS attributes unavailable for " + var2.path());
            }

            try {
               var3.setHidden(var2.hidden());
               var3.setSystem(var2.system());
               var1.remove();
            } catch (IOException var5) {
               throw new UncheckedIOException("Failed to restore attributes of " + var2.path(), var5);
            }
         }
      }
   }

   private void method_9О() {
      for (int var1 = this.Г.size() - 1; var1 >= 0; var1--) {
         UnhookFileManager.HiddenFile var2 = this.Г.get(var1);
         if (!Files.exists(var2.hidden(), LinkOption.NOFOLLOW_LINKS)) {
            this.Г.remove(var1);
         } else {
            try {
               if (Files.exists(var2.original(), LinkOption.NOFOLLOW_LINKS)) {
                  throw new IOException("Original path is occupied: " + var2.original());
               }

               Files.move(var2.hidden(), var2.original());
               this.Г.remove(var1);
            } catch (IOException var4) {
               throw new UncheckedIOException("Failed to restore " + var2.original(), var4);
            }
         }
      }
   }

   private static void method_9(Set<Path> var0, Path var1, String var2) {
      for (Path var5 : List.of(var1.resolve(Эь), var1.resolve(Эж))) {
         if (Files.isDirectory(var5)) {
            try (DirectoryStream<Path> var6 = Files.newDirectoryStream(var5)) {
               for (Path var8 : var6) {
                  Path var9 = var8.resolve(Э弟).resolve(Эб).resolve(var2);
                  if (Files.isDirectory(var9)) {
                     var0.add(var9);
                  }
               }
            } catch (IOException var12) {
            }
         }
      }
   }

   private static Path Д_2b4785d0(Path var0) {
      return var0 == null ? null : var0.toAbsolutePath().normalize();
   }

   private static void method_9(Set<Path> var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         Path var2 = Path.of(var1).resolve(чз).resolve(чц);
         var0.add(var2.resolve(чя).resolve(Эс).resolve(ЭВ).resolve(ЭЯ));
         String var3 = System.getProperty(ЭХ);
         if (var3 != null && !var3.isBlank()) {
            var0.add(var2.resolve(var3).resolve(Эе).resolve(ЭА).resolve(Э4));
         }

         if (Files.isDirectory(var2)) {
            try (DirectoryStream<Path> var4 = Files.newDirectoryStream(var2)) {
               for (Path var6 : var4) {
                  if (Files.isDirectory(var6)) {
                     var0.add(var6.resolve(Эп).resolve(ЭД).resolve(ЭЪ));
                  }
               }
            } catch (IOException var9) {
            }
         }
      }
   }

   private static Path и() {
      return method_9(Эт);
   }

   private static Path method_9(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.gameDirectory != null) {
         for (Path var2 = Д_2b4785d0(var1.gameDirectory.toPath()); var2 != null; var2 = var2.getParent()) {
            Path var3 = var2.getFileName();
            if (var3 != null && var0.equalsIgnoreCase(var3.toString())) {
               return var2;
            }
         }
      }

      LinkedHashSet<Path> var10 = new LinkedHashSet<>();
      String var11 = System.getenv(Эа);
      if (var11 != null && !var11.isBlank()) {
         var10.add(Path.of(var11 + "\\" + var0));
      }

      if (method_9м()) {
         var10.add(Path.of("C:\\" + var0));
      }

      String var4 = System.getenv(ЭЮ);
      if (var4 != null && !var4.isBlank()) {
         var10.add(Path.of(var4).resolve(ЭЧ).resolve(var0));
      }

      String var5 = System.getenv(ЭЛ);
      if (var5 != null && !var5.isBlank()) {
         var10.add(Path.of(var5).resolve(Э诶).resolve(Э1).resolve(var0));
      }

      String var6 = System.getProperty(ЭЬ);
      if (var6 != null && !var6.isBlank()) {
         Path var7 = Path.of(var6);
         var10.add(var7.resolve(var0));
         var10.add(var7.resolve(Э衣).resolve(Эш).resolve(var0));
         method_9(var10, var7, var0);
      }

      var10.add(Path.of(ЭИ).resolve(var0));

      for (Path var8 : var10) {
         Path var9 = Д_2b4785d0(var8);
         if (var9 != null && Files.isDirectory(var9)) {
            return var9;
         }
      }

      return null;
   }

   private void ь(Path var1, BooleanSupplier var2) {
      if (!var2.getAsBoolean()) {
         try (Stream<Path> var3 = Files.walk(var1)) {
            Iterator var4 = var3.iterator();

            while (!var2.getAsBoolean() && var4.hasNext()) {
               Path var5 = (Path)var4.next();
               DosFileAttributeView var6 = Files.getFileAttributeView(var5, DosFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
               if (var6 != null) {
                  try {
                     DosFileAttributes var7 = var6.readAttributes();
                     this.л.add(new UnhookFileManager.FileAttributes(var5, var7.isHidden(), var7.isSystem()));
                     var6.setHidden(true);
                     var6.setSystem(true);
                  } catch (IOException var9) {
                  }
               }
            }
         } catch (IOException var11) {
         }
      }
   }

   public synchronized void method_6(BooleanSupplier var1) {
      if (!this.сЩ && !var1.getAsBoolean()) {
         this.Я = и();
         this.Х = method_9(чЙ);
         this.Г.clear();
         this.л.clear();
         if (this.Я != null && Files.isDirectory(this.Я)) {
            Path var2 = ь_2b4785d0(this.Я);
            if (var2 != null) {
               this.method_9(this.Я, var2, var1);
            }
         }

         if (method_9Б()) {
            if (this.Я != null && Files.isDirectory(this.Я)) {
               this.method_9(this.Я, var1);
            }

            if (this.Х != null && Files.isDirectory(this.Х)) {
               this.method_9(this.Х, var1);
            }
         } else {
            if (this.Я != null && Files.isDirectory(this.Я)) {
               this.ь(this.Я, var1);
            }

            if (this.Х != null && Files.isDirectory(this.Х)) {
               this.ь(this.Х, var1);
            }
         }

         this.сЩ = !this.Г.isEmpty() || !this.л.isEmpty();
      }
   }

   private static void method_9_de5a6f78(Path var0, Path var1) {
      if (!Files.exists(var1, LinkOption.NOFOLLOW_LINKS)) {
         try {
            if (Files.isDirectory(var0, LinkOption.NOFOLLOW_LINKS)) {
               Files.createDirectory(var1);

               try (DirectoryStream<Path> var2 = Files.newDirectoryStream(var0)) {
                  for (Path var4 : var2) {
                     method_9_de5a6f78(var4, var1.resolve(var4.getFileName()));
                  }
               }
            } else if (Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS)) {
               Files.copy(var0, var1);
            }
         } catch (IOException var7) {
         }
      }
   }

   private void method_9(Path var1, BooleanSupplier var2) {
      if (!var2.getAsBoolean()) {
         List<Path> var3;
         try (Stream<Path> var4 = Files.walk(var1)) {
            var3 = var4.takeWhile(var1x -> !var2.getAsBoolean()).sorted(Comparator.comparingInt(Path::getNameCount).reversed()).toList();
         } catch (IOException var12) {
            return;
         }

         for (Path var5 : var3) {
            if (var2.getAsBoolean()) {
               return;
            }

            Path var6 = var5.getFileName();
            if (var6 != null && !var6.toString().startsWith(Ээ)) {
               Path var7 = var5.resolveSibling("." + var6);
               if (Files.exists(var7, LinkOption.NOFOLLOW_LINKS)) {
                  var7 = var5.resolveSibling(".unhook-" + UUID.randomUUID().toString().substring(0, 8) + "-" + var6);
               }

               try {
                  Files.move(var5, var7);
                  this.Г.add(new UnhookFileManager.HiddenFile(var5, var7));
               } catch (IOException var10) {
               }
            }
         }
      }
   }

   private static boolean method_9(Path var0, Set<Path> var1) {
      for (Path var3 : var1) {
         if (method_9_bbeebd87(var0, var3)) {
            return true;
         }
      }

      return false;
   }

   private static boolean method_9м() {
      return System.getProperty(Эу, "").toLowerCase(Locale.ROOT).contains(Э2);
   }

   private record FileAttributes(Path path, boolean hidden, boolean system) {
   }

   private record HiddenFile(Path original, Path hidden) {
   }
}
