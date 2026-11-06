package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import net.minecraft.client.resources.server.PackLoadFeedback.FinalResult;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket.Action;
import net.minecraft.server.packs.DownloadQueue.BatchResult;
import net.minecraft.server.packs.DownloadQueue.DownloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.render.ReallyWorldResourcePackPatcher;
import settings.render.ReallyWorldRankTextures;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ActionSetting;

public class RWFixer extends ClientModule {
   private static final File Я_f0e5dd92_at_ea4024bc = new File(sg.config.ClientDirectories.forDevelopment(System.getenv(RWFixer.ХНф)) + "\\Dimas\\", RWFixer.ХНи);
   private static final Logger А_at_ea4024bc = LoggerFactory.getLogger(RWFixer.ХН9);
   private static volatile RWFixer instance;
   private static final byte[] Я_1be31765_at_ea4024bc = new byte[]{80, 75, 3, 4};
   private static final Pattern 诶 = Pattern.compile(RWFixer.ХНо, 32);
   private static final Pattern field_1 = Pattern.compile(RWFixer.ХНO);
   private static final String Въ = "{\n  \"sources\": [\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"item/\",\n      \"source\": \"item\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"archangel_set_texture/\",\n      \"source\": \"archangel_set_texture\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"gui/\",\n      \"source\": \"gui\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"custom/\",\n      \"source\": \"custom\"\n    },\n    {\n      \"type\": \"minecraft:paletted_permutations\",\n      \"palette_key\": \"minecraft:trims/color_palettes/trim_palette\",\n      \"permutations\": {\n        \"amethyst\": \"minecraft:trims/color_palettes/amethyst\",\n        \"copper\": \"minecraft:trims/color_palettes/copper\",\n        \"copper_darker\": \"minecraft:trims/color_palettes/copper_darker\",\n        \"diamond\": \"minecraft:trims/color_palettes/diamond\",\n        \"diamond_darker\": \"minecraft:trims/color_palettes/diamond_darker\",\n        \"emerald\": \"minecraft:trims/color_palettes/emerald\",\n        \"gold\": \"minecraft:trims/color_palettes/gold\",\n        \"gold_darker\": \"minecraft:trims/color_palettes/gold_darker\",\n        \"iron\": \"minecraft:trims/color_palettes/iron\",\n        \"iron_darker\": \"minecraft:trims/color_palettes/iron_darker\",\n        \"lapis\": \"minecraft:trims/color_palettes/lapis\",\n        \"netherite\": \"minecraft:trims/color_palettes/netherite\",\n        \"netherite_darker\": \"minecraft:trims/color_palettes/netherite_darker\",\n        \"quartz\": \"minecraft:trims/color_palettes/quartz\",\n        \"redstone\": \"minecraft:trims/color_palettes/redstone\",\n        \"resin\": \"minecraft:trims/color_palettes/resin\"\n      },\n      \"textures\": [\n        \"minecraft:trims/items/helmet_trim\",\n        \"minecraft:trims/items/chestplate_trim\",\n        \"minecraft:trims/items/leggings_trim\",\n        \"minecraft:trims/items/boots_trim\"\n      ]\n    }\n  ]\n}\n";
   private final @com.google.gson.annotations.SerializedName("ВД")  BooleanSetting statusSpoofingSetting;
   private final @com.google.gson.annotations.SerializedName("ВЪ")  BooleanSetting notificationsSetting;
   @SerializedName("е")
   private final ActionSetting openFolderActionSetting;
   @SerializedName("Х")
   private final ExecutorService packPatchExecutor;
   private final @com.google.gson.annotations.SerializedName("сЭ")  Map<UUID, RWFixer.PackRequest> packRequests;
   private volatile @com.google.gson.annotations.SerializedName("В")  Connection activeConnection;
   private static final String ХМм = "RWFixer";
   private static final String ХМК = "Автоматически исправляет ресурспак на ReallyWorld и PlayRW";
   private static final String ХМI = "Спуф статуса";
   private static final String ХМТ = "Уведомления";
   private static final String ХМО = "Открыть папку";
   private static final int ХМФ = 65535;
   private static final String ХМЙ = ".";
   private static final String ХМк = "reallyworld.me";
   private static final String ХМг = ".reallyworld.me";
   private static final String ХМ0 = "reallyworld.ru";
   private static final String ХМх = ".reallyworld.ru";
   private static final String ХМУ = "playrw.ru";
   private static final String ХМЗ = ".playrw.ru";
   private static final String ХМЕ = "playrw.me";
   private static final String ХМП = ".playrw.me";
   private static final String ХМ西 = "RwFixed applied for resource pack {}";
   private static final String ХМд = "RwFixed загружен и применён.";
   private static final String ХМБ = "RwFixed could not be applied for resource pack {}: {}";
   private static final String ХМС = "Не удалось загрузить РП. Причина записана в latest.log.";
   private static final String ХМз = "Не удалось применить RwFixed. Причина записана в latest.log.";
   private static final String ХМц = "RwFixed";
   private static final String ХМя = "Подготавливаю RwFixed для ресурспака ReallyWorld...";
   private static final String ХНс = "Could not update ReallyWorld rank icons";
   private static final String ХНВ = "pack.mcmeta";
   private static final String ХНЯ = "rwfixed-";
   private static final String ХНХ = ".writing";
   private static final String ХНе = "rp-raw-";
   private static final String ХНА = ".zip";
   private static final String ХН4 = "rp-out-";
   private static final String ХНп = ".zip";
   private static final String ХНД = "assets/";
   private static final String ХНЪ = "/models/item/";
   private static final String ХНт = ".json";
   private static final String ХНа = "/models/";
   private static final String ХНЮ = "/items/";
   private static final String ХНЧ = "/";
   private static final String ХНЛ = "pack.mcmeta";
   private static final String ХН诶 = "assets/";
   private static final String ХН1 = "/models/";
   private static final String ХНЬ = ".json";
   private static final String ХН衣 = "pack.mcmeta";
   private static final String ХНш = "pack.mcmeta";
   private static final String ХНИ = "/models/";
   private static final String ХНь = "/items/";
   private static final String ХНж = "assets/minecraft/atlases/items.json";
   private static final String ХН弟 = "assets/minecraft/atlases/items.json";
   private static final String ХНб = "{\n  \"sources\": [\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"item/\",\n      \"source\": \"item\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"archangel_set_texture/\",\n      \"source\": \"archangel_set_texture\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"gui/\",\n      \"source\": \"gui\"\n    },\n    {\n      \"type\": \"minecraft:directory\",\n      \"prefix\": \"custom/\",\n      \"source\": \"custom\"\n    },\n    {\n      \"type\": \"minecraft:paletted_permutations\",\n      \"palette_key\": \"minecraft:trims/color_palettes/trim_palette\",\n      \"permutations\": {\n        \"amethyst\": \"minecraft:trims/color_palettes/amethyst\",\n        \"copper\": \"minecraft:trims/color_palettes/copper\",\n        \"copper_darker\": \"minecraft:trims/color_palettes/copper_darker\",\n        \"diamond\": \"minecraft:trims/color_palettes/diamond\",\n        \"diamond_darker\": \"minecraft:trims/color_palettes/diamond_darker\",\n        \"emerald\": \"minecraft:trims/color_palettes/emerald\",\n        \"gold\": \"minecraft:trims/color_palettes/gold\",\n        \"gold_darker\": \"minecraft:trims/color_palettes/gold_darker\",\n        \"iron\": \"minecraft:trims/color_palettes/iron\",\n        \"iron_darker\": \"minecraft:trims/color_palettes/iron_darker\",\n        \"lapis\": \"minecraft:trims/color_palettes/lapis\",\n        \"netherite\": \"minecraft:trims/color_palettes/netherite\",\n        \"netherite_darker\": \"minecraft:trims/color_palettes/netherite_darker\",\n        \"quartz\": \"minecraft:trims/color_palettes/quartz\",\n        \"redstone\": \"minecraft:trims/color_palettes/redstone\",\n        \"resin\": \"minecraft:trims/color_palettes/resin\"\n      },\n      \"textures\": [\n        \"minecraft:trims/items/helmet_trim\",\n        \"minecraft:trims/items/chestplate_trim\",\n        \"minecraft:trims/items/leggings_trim\",\n        \"minecraft:trims/items/boots_trim\"\n      ]\n    }\n  ]\n}\n";
   private static final String ХНЫ = "assets/";
   private static final String ХНl = ".json";
   private static final String ХН8 = "assets/";
   private static final String ХН5 = "models/";
   private static final String ХНв = "models/";
   private static final String ХНъ = "assets/";
   private static final String ХНМ = "/textures/";
   private static final String ХНН = ".png";
   private static final String ХНн = "assets/";
   private static final String ХНЦ = "textures/";
   private static final String ХНч = "textures/";
   private static final String ХНЭ = "/";
   private static final String ХН必 = "minecraft";
   private static final String ХНщ = "\"textures\":{";
   private static final String ХНГ = "\":\"";
   private static final String ХНл = "/";
   private static final String ХНэ = "assets/";
   private static final String ХНю = "SHA-1";
   private static final String ХНЩ = "explorer.exe";
   private static final String ХНу = "Failed to prepare ReallyWorld resource pack {}";
   private static final String ХН2 = "RWFixer";
   private static final String ХНф = "SystemDrive";
   private static final String ХНи = "rose/resourcepacks";
   private static final String ХН9 = "Rose/RWFixer";
   private static final String ХНо = "\"textures\"\\s*:\\s*\\{([^}]*)}";
   private static final String ХНO = "\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"";

   private byte[] М(byte[] var1) {
      int var2 = this.б(var1, Я_1be31765_at_ea4024bc);
      if (var2 <= 0) {
         return var1;
      }

      byte[] var3 = new byte[var1.length - var2];
      System.arraycopy(var1, var2, var3, 0, var3.length);
      return var3;
   }

   private Set<String> б(File var1) throws Exception {
      HashSet var2 = new HashSet();

      try (ZipFile var3 = new ZipFile(var1)) {
         Enumeration var4 = var3.entries();

         while (var4.hasMoreElements()) {
            String var5 = this.ф(this.б((ZipEntry)var4.nextElement()));
            if (var5.startsWith(ХНъ) && var5.contains(ХНМ) && var5.endsWith(ХНН)) {
               String var6 = var5.substring(ХНн.length());
               int var7 = var6.indexOf(47);
               if (var7 >= 0) {
                  String var8 = var6.substring(0, var7);
                  String var9 = var6.substring(var7 + 1);
                  if (var9.startsWith(ХНЦ)) {
                     String var10 = var9.substring(ХНч.length(), var9.length() - 4);
                     var2.add(var8 + ":" + var10);
                  }
               }
            }
         }
      }

      return var2;
   }

   @Override
   public void disable() {
   }

   private String ф(String var1) {
      int var2 = var1.indexOf(ХНэ);
      return var2 > 0 && var1.charAt(var2 - 1) == '/' ? var1.substring(var2) : var1;
   }

   @Override
   public void enable() {
      if (!this.isEnabled()) {
         super.enable();
      }
   }

   private int б(byte[] var1, byte[] var2) {
      label24:
      for (int var3 = 0; var3 <= var1.length - var2.length; var3++) {
         for (int var4 = 0; var4 < var2.length; var4++) {
            if (var1[var3 + var4] != var2[var4]) {
               continue label24;
            }
         }

         return var3;
      }

      return -1;
   }

   public void б(Connection var1, UUID var2, FinalResult var3) {
      if (this.activeConnection == var1 && this.packRequests.containsKey(var2)) {
         if (var3 == FinalResult.APPLIED) {
            А_at_ea4024bc.info(ХМ西, var2);
            this.Я(ХМд);
         } else if (var3 == FinalResult.DOWNLOAD_FAILED || var3 == FinalResult.ACTIVATION_FAILED) {
            А_at_ea4024bc.warn(ХМБ, var2, var3);
            this.Я(var3 == FinalResult.DOWNLOAD_FAILED ? ХМС : ХМз);
         }
      }
   }

   private String З(byte[] var1) throws Exception {
      byte[] var2 = MessageDigest.getInstance(ХНю).digest(var1);
      return HexFormat.of().formatHex(var2);
   }

   public void б(Map<UUID, DownloadRequest> var1, Connection var2, BatchResult var3, Consumer<BatchResult> var4, Executor var5) {
      if (var2 != null && this.activeConnection == var2) {
         HashMap<UUID, Path> var6 = new HashMap<>();

         for (Entry var8 : var3.downloaded().entrySet()) {
            DownloadRequest var9 = (DownloadRequest)var1.get(var8.getKey());
            RWFixer.PackRequest var10 = this.packRequests.get(var8.getKey());
            if (var9 != null && var10 != null && var10.url().equals(var9.url().toExternalForm())) {
               var6.put((UUID)var8.getKey(), (Path)var8.getValue());
            }
         }

         if (var6.isEmpty()) {
            var4.accept(var3);
         } else {
            this.packPatchExecutor.execute(() -> {
               HashMap var6x = new HashMap<>(var3.downloaded());
               HashSet var7 = new HashSet<>(var3.failed());

               for (Entry var9x : var6.entrySet()) {
                  if (this.activeConnection != var2) {
                     break;
                  }

                  try {
                     var6x.put((UUID)var9x.getKey(), this.弟((Path)var9x.getValue()));
                  } catch (Exception var12) {
                     var6x.remove(var9x.getKey());
                     var7.add((UUID)var9x.getKey());
                     А_at_ea4024bc.error(ХНу, var9x.getKey(), var12);
                     String var11 = var12.getMessage() != null ? var12.getMessage() : var12.getClass().getSimpleName();
                     this.Я("Не удалось подготовить RwFixed: " + var11);
                  }
               }

               BatchResult var13 = new BatchResult(var6x, var7);
               var5.execute(() -> var4.accept(this.activeConnection == var2 ? var13 : var3));
            });
         }
      } else {
         var4.accept(var3);
      }
   }

   public static RWFixer З_d084f225() {
      return instance;
   }

   public RWFixer() {
      super(ХМм, ХМК, ModuleCategory.MISC);
      this.statusSpoofingSetting = new BooleanSetting(ХМI, true);
      this.notificationsSetting = new BooleanSetting(ХМТ, true);
      this.openFolderActionSetting = new ActionSetting(ХМО).onClick(this::Аг).showOtherSettings();
      this.packPatchExecutor = Executors.newSingleThreadExecutor(var0 -> {
         Thread var1 = new Thread(var0, ХН2);
         var1.setDaemon(true);
         return var1;
      });
      this.packRequests = new HashMap<>();
      this.addSettings(this.statusSpoofingSetting, this.notificationsSetting, this.openFolderActionSetting);
      instance = this;
   }

   private byte[] method_5(byte[] var1) throws Exception {
      Path var2 = Files.createTempFile(ХНе, ХНА);
      Path var3 = Files.createTempFile(ХН4, ХНп);

      try {
         Files.write(var2, var1);
         Set var4 = this.б(var2.toFile());
         HashSet<String> var5 = new HashSet<>();
         ArrayList<String> var6 = new ArrayList<>();

         try (ZipFile var7 = new ZipFile(var2.toFile())) {
            Enumeration var8 = var7.entries();

            while (var8.hasMoreElements()) {
               String var9 = this.б((ZipEntry)var8.nextElement());
               var5.add(var9);
            }

            for (String var10 : var5) {
               if (this.ф(var10).startsWith(ХНД) && var10.contains(ХНЪ) && var10.endsWith(ХНт)) {
                  String var11 = var10.replace(ХНа, ХНЮ);
                  if (!var5.contains(var11)) {
                     var6.add(var10);
                  }
               }
            }
         }

         try (
            ZipFile var42 = new ZipFile(var2.toFile());
            ZipOutputStream var43 = new ZipOutputStream(Files.newOutputStream(var3));
         ) {
            var43.setLevel(-1);
            byte[] var45 = new byte[8192];

            for (Enumeration var46 = var42.entries(); var46.hasMoreElements(); var43.closeEntry()) {
               ZipEntry var47 = (ZipEntry)var46.nextElement();
               String var12 = this.б(var47);
               ZipEntry var13 = new ZipEntry(var12);
               if (var47.getTime() >= 0L) {
                  var13.setTime(var47.getTime());
               }

               var43.putNextEntry(var13);
               if (!var12.endsWith(ХНЧ)) {
                  byte[] var14;
                  try (
                     InputStream var15 = var42.getInputStream(var47);
                     ByteArrayOutputStream var16 = new ByteArrayOutputStream();
                  ) {
                     int var17;
                     while ((var17 = var15.read(var45)) >= 0) {
                        var16.write(var45, 0, var17);
                     }

                     var14 = var16.toByteArray();
                  }

                  if (var12.equals(ХНЛ)) {
                     var14 = ReallyWorldResourcePackPatcher.Щ(var14);
                  } else if (ReallyWorldResourcePackPatcher.西(var12)) {
                     var14 = ReallyWorldResourcePackPatcher.А_1be31765(var14);
                  } else if (this.ф(var12).startsWith(ХН诶) && var12.contains(ХН1) && var12.endsWith(ХНЬ)) {
                     var14 = this.б(var12, var14, var4);
                  }

                  var43.write(var14);
               }
            }

            if (!var5.contains(ХН衣)) {
               var43.putNextEntry(new ZipEntry(ХНш));
               var43.write(ReallyWorldResourcePackPatcher.Ъ());
               var43.closeEntry();
            }

            for (String var49 : var6) {
               String var50 = var49.replace(ХНИ, ХНь);
               String var51 = this.西(var49);
               if (var51 != null) {
                  byte[] var52 = ("{\"model\":{\"type\":\"minecraft:model\",\"model\":\"" + var51 + "\"}}").getBytes(StandardCharsets.UTF_8);
                  var43.putNextEntry(new ZipEntry(var50));
                  var43.write(var52);
                  var43.closeEntry();
               }
            }

            if (!var5.contains(ХНж)) {
               var43.putNextEntry(new ZipEntry(ХН弟));
               var43.write(ХНб.getBytes(StandardCharsets.UTF_8));
               var43.closeEntry();
            }
         }

         return Files.readAllBytes(var3);
      } finally {
         Files.deleteIfExists(var2);
         Files.deleteIfExists(var3);
      }
   }

   public void АП() {
      this.activeConnection = null;
      this.packRequests.clear();
   }

   private Path 弟(Path var1) throws Exception {
      byte[] var2 = Files.readAllBytes(var1);
      Path var3 = Я_f0e5dd92_at_ea4024bc.toPath().resolve(ХМц);
      Files.createDirectories(var3);
      Path var4 = var3.resolve(ReallyWorldResourcePackPatcher.О(this.З(var2)));
      if (Files.isRegularFile(var4) && this.Л(var4)) {
         return var4;
      }

      this.Я(ХМя);
      this.б(var4, this.method_5(this.М(var2)));

      try {
         ReallyWorldRankTextures.Я(var4);
      } catch (Exception var6) {
         А_at_ea4024bc.warn(ХНс, var6);
      }

      return var4;
   }

   public static boolean Ю(String var0) {
      if (var0 != null && !var0.isBlank()) {
         try {
            URI var1 = URI.create("minecraft://" + var0.strip());
            if (var1.getUserInfo() == null && var1.getQuery() == null && var1.getFragment() == null && var1.getRawPath().isEmpty() && var1.getPort() <= ХМФ) {
               String var2 = var1.getHost();
               if (var2 == null) {
                  return false;
               }

               var2 = var2.toLowerCase(Locale.ROOT);
               if (var2.endsWith(ХМЙ)) {
                  var2 = var2.substring(0, var2.length() - 1);
               }

               return var2.equals(ХМк)
                  || var2.endsWith(ХМг)
                  || var2.equals(ХМ0)
                  || var2.endsWith(ХМх)
                  || var2.equals(ХМУ)
                  || var2.endsWith(ХМЗ)
                  || var2.equals(ХМЕ)
                  || var2.endsWith(ХМП);
            } else {
               return false;
            }
         } catch (IllegalArgumentException var3) {
            return false;
         }
      } else {
         return false;
      }
   }

   private byte[] б(String var1, byte[] var2, Set<String> var3) {
      String[] var4 = this.ф(var1).split(ХНЭ);
      if (var4.length < 3) {
         return var2;
      }

      String var5 = var4[1];
      if (ХН必.equals(var5)) {
         return var2;
      }

      String var6 = new String(var2, StandardCharsets.UTF_8);
      Matcher var7 = 诶.matcher(var6);
      StringBuilder var8 = new StringBuilder();
      int var9 = 0;
      boolean var10 = false;

      while (var7.find()) {
         var8.append(var6, var9, var7.start());
         String var11 = var7.group(1);
         String var12 = this.б(var11, var5, var3);
         if (!var12.equals(var11)) {
            var10 = true;
         }

         var8.append(ХНщ).append(var12).append('}');
         var9 = var7.end();
      }

      if (!var10) {
         return var2;
      }

      var8.append(var6.substring(var9));
      return var8.toString().getBytes(StandardCharsets.UTF_8);
   }

   public Connection б() {
      return this.activeConnection;
   }

   private void Я(String var1) {
      if (this.notificationsSetting.getValue()) {
         MINECRAFT_CLIENT.execute(() -> {
            if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getCurrentServer() != null && Ю(MINECRAFT_CLIENT.getCurrentServer().ip)) {
               ClientChat.З("[RWFixer] " + var1);
            }
         });
      }
   }

   private String З(String var1, String var2, Set<String> var3) {
      if (var1.isEmpty() || var1.charAt(0) == '#' || var1.indexOf(58) >= 0) {
         return var1;
      } else if (var3.contains(var2 + ":" + var1)) {
         return var1;
      } else {
         return var3.contains("minecraft:" + var1) ? "minecraft:" + var1 : var1;
      }
   }

   private String 西(String var1) {
      var1 = this.ф(var1);
      if (var1.startsWith(ХНЫ) && var1.endsWith(ХНl)) {
         String var2 = var1.substring(ХН8.length(), var1.length() - 5);
         int var3 = var2.indexOf(47);
         if (var3 < 0) {
            return null;
         }

         String var4 = var2.substring(0, var3);
         String var5 = var2.substring(var3 + 1);
         return !var5.startsWith(ХН5) ? null : var4 + ":" + var5.substring(ХНв.length());
      } else {
         return null;
      }
   }

   private void б(Path var1, byte[] var2) throws Exception {
      Path var3 = Files.createTempFile(var1.getParent(), ХНЯ, ХНХ);

      try {
         Files.write(var3, var2);

         try {
            Files.move(var3, var1, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException var8) {
            Files.move(var3, var1, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   private void Аг() {
      try {
         if (!Я_f0e5dd92_at_ea4024bc.exists()) {
            Я_f0e5dd92_at_ea4024bc.mkdirs();
         }

         new ProcessBuilder(ХНЩ, Я_f0e5dd92_at_ea4024bc.getAbsolutePath()).start();
      } catch (Exception var3) {
         String var2 = var3.getMessage() != null ? var3.getMessage() : var3.getClass().getSimpleName();
         ClientChat.З("[RWFixer] Не удалось открыть папку: " + var2);
         ClientChat.З("[RWFixer] Путь: " + Я_f0e5dd92_at_ea4024bc.getAbsolutePath());
      }
   }

   private String б(ZipEntry var1) {
      String var2 = var1.getName().replace('\\', '/');
      if (var1.getSize() > 0L) {
         while (var2.endsWith(ХНл)) {
            var2 = var2.substring(0, var2.length() - 1);
         }
      }

      return var2;
   }

   public boolean б(Connection var1, UUID var2) {
      if (this.activeConnection != var1) {
         return false;
      }

      RWFixer.PackRequest var3 = this.packRequests.get(var2);
      return var3 != null && var3.spoofed();
   }

   private String б(String var1, String var2, Set<String> var3) {
      Matcher var4 = field_1.matcher(var1);
      StringBuilder var5 = new StringBuilder();

      int var6;
      for (var6 = 0; var4.find(); var6 = var4.end()) {
         var5.append(var1, var6, var4.start());
         String var7 = var4.group(1);
         String var8 = var4.group(2);
         String var9 = this.З(var8, var2, var3);
         var5.append('"').append(var7).append(ХНГ).append(var9).append('"');
      }

      var5.append(var1.substring(var6));
      return var5.toString();
   }

   private boolean Л(Path var1) {
      try (ZipFile var2 = new ZipFile(var1.toFile())) {
         ZipEntry var3 = var2.getEntry(ХНВ);
         if (var3 == null) {
            return false;
         }

         try (InputStream var4 = var2.getInputStream(var3)) {
            return ReallyWorldResourcePackPatcher.А_bbeebd87(var4.readAllBytes());
         }
      } catch (Exception var11) {
         return false;
      }
   }

   public void б(Connection var1, String var2, UUID var3, String var4) {
      if (Ю(var2)) {
         if (this.activeConnection != var1) {
            this.АП();
            this.activeConnection = var1;
         }

         boolean var5 = this.statusSpoofingSetting.getValue();
         this.packRequests.put(var3, new RWFixer.PackRequest(var4, var5));
         if (var5) {
            var1.send(new ServerboundResourcePackPacket(var3, Action.ACCEPTED));
            var1.send(new ServerboundResourcePackPacket(var3, Action.SUCCESSFULLY_LOADED));
         }
      }
   }

   private record PackRequest(String url, boolean spoofed) {
   }
}
