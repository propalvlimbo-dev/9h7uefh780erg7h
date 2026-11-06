package sg.network;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import sg.client.RoseClient;
import sg.modules.impl.misc.NameProtect;
import sg.hud.Scoreboard;

public final class ScoreboardValueEditor {
   private static final Pattern т = Pattern.compile(ScoreboardValueEditor.ВвO);
   private static final int Х2 = 32;
   private static final int Хф = 9;
   private static final Map<String, String> д_ea411899 = new ConcurrentHashMap<>();
   private static final ThreadLocal<Boolean> В_289b0448 = ThreadLocal.withInitial(() -> false);
   private static final Map<String, Optional<ScoreboardValueEditor.RewriteResult>> Б = new LinkedHashMap<>(64, ScoreboardValueEditor.Вв7, true);
   private static final StyledComponentCache<ScoreboardValueEditor.ComponentOverride> В_f85d83ce = new StyledComponentCache<>(128);
   private static boolean В3;
   private static final List<ScoreboardValueEditor.EditHitRegion> 西 = new ArrayList<>();
   private static volatile List<ScoreboardValueEditor.EditHitRegion> д_2571477d = List.of();
   private static String с0;
   private static String сх = "";
   private static String сУ = "";
   private static boolean Вр;
   private static final long Вву = 400L;
   private static final String Вв2 = "0";
   private static final double Ввф = 11.0;
   private static final int Вви = -670398;
   private static final int Вв9 = 1728053247;
   private static final long Вво = 2L;
   private static final String ВвO = "\\d+(?:[.,]\\d+)*";
   private static final float Вв7 = 0.75F;

   private static void зБ() {
      Б.clear();
      В_f85d83ce.Ъ2();
   }

   private static void з5() {
      с0 = null;
      сУ = "";
      Вр = false;
      зБ();
   }

   private static ScoreboardValueEditor.RewriteResult method_9(String var0, ScoreboardValueEditor.ScoreField var1) {
      Matcher var2 = т.matcher(var0);
      if (!var2.find()) {
         return null;
      }

      String var3 = var2.group();
      String var4 = ц_388008bb(var1.name(), var3);
      String var5 = var0.substring(0, var2.start()) + var4 + var0.substring(var2.end());
      return new ScoreboardValueEditor.RewriteResult(
         var5, List.of(new ScoreboardValueEditor.ReplacementSpan(var1.name(), var3, var2.start(), var2.end(), var2.start(), var2.start() + var4.length()))
      );
   }

   public static boolean с(String var0) {
      if (!ц衣() || с0 == null || var0 == null || var0.isEmpty()) {
         return false;
      }

      if (Character.isISOControl(var0.charAt(0))) {
         return false;
      }

      if (Вр) {
         сУ = "";
         Вр = false;
      }

      if (сУ.codePointCount(0, сУ.length()) >= 32) {
         return true;
      }

      сУ = сУ + var0;
      зБ();
      return true;
   }

   public static boolean ф(float var0, float var1, int var2) {
      if (ц衣() && var2 == 0) {
         for (int var3 = д_2571477d.size() - 1; var3 >= 0; var3--) {
            ScoreboardValueEditor.EditHitRegion var4 = д_2571477d.get(var3);
            if (!(var0 < var4.x) && !(var1 < var4.y) && !(var0 > var4.x + var4.width) && !(var1 > var4.y + var4.height)) {
               зЮ();
               ц_de5a6f78(var4.key(), ц_388008bb(var4.key(), var4.original()));
               return true;
            }
         }

         if (с0 != null) {
            зЮ();
         }

         return false;
      } else {
         return false;
      }
   }

   private static void зЮ() {
      if (с0 != null) {
         String var0 = сУ == null ? "" : сУ;
         if (!var0.isEmpty() && !var0.equals(сх)) {
            д_ea411899.put(с0, var0);
         } else {
            д_ea411899.remove(с0);
         }

         с0 = null;
         сУ = "";
         Вр = false;
         зБ();
      }
   }

   public static void method_9(GuiGraphicsExtractor var0, Font var1, Component var2, int var3, int var4) {
      if (цн() && var2 != null && var1 != null) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.gui.screen() instanceof ChatScreen) {
            ScoreboardValueEditor.RewriteResult var6 = method_9_7a9bb51c(var2.getString());
            if (var6 != null) {
               double var7 = var5.getWindow().getGuiScale();
               long var9 = System.currentTimeMillis() / Вву;

               for (ScoreboardValueEditor.ReplacementSpan var12 : var6.spans) {
                  String var13 = var6.output.substring(0, var12.start);
                  String var14 = var6.output.substring(var12.start, var12.end);
                  int var15 = var1.width(var13);
                  int var16 = Math.max(4, var1.width(var14.isEmpty() ? Вв2 : var14));
                  int var17 = var3 + var15;
                  float var18 = Scoreboard.уЪ();
                  float var19 = Scoreboard.уР();
                  西.add(
                     new ScoreboardValueEditor.EditHitRegion(
                        var12.key, var12.original, (var17 - 1 + var18) * var7, (var4 - 1 + var19) * var7, (var16 + 2) * var7, Ввф * var7
                     )
                  );
                  boolean var20 = var12.key.equals(с0);
                  int var21 = var20 ? Вви : Вв9;
                  var0.fill(var17, var4 + 9 - 1, var17 + var16, var4 + 9, var21);
                  if (var20 && var9 % Вво == 0L) {
                     int var22 = var17 + var1.width(сУ);
                     var0.fill(var22, var4, var22 + 1, var4 + 9 - 1, -1);
                  }
               }
            }
         }
      }
   }

   public static boolean п(int var0) {
      if (ц衣() && с0 != null) {
         switch (var0) {
            case 256:
               з5();
               return true;
            case 257:
            case 335:
               зЮ();
               return true;
            case 259:
            case 261:
               if (Вр) {
                  сУ = "";
                  Вр = false;
               } else if (!сУ.isEmpty()) {
                  сУ = сУ.substring(0, сУ.offsetByCodePoints(сУ.length(), -1));
               }

               зБ();
               return true;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   private static Component method_9(Component var0, ScoreboardValueEditor.RewriteResult var1) {
      String var2 = var0.getString();
      ArrayList var3 = new ArrayList(var2.length());
      var0.visit((var1x, var2x) -> {
         for (int var3x = 0; var3x < var2x.length(); var3x++) {
            var3.add(var1x);
         }

         return Optional.empty();
      }, Style.EMPTY);
      if (var3.size() != var2.length()) {
         return Component.literal(var1.output).setStyle(var0.getStyle());
      }

      MutableComponent var4 = Component.empty();
      int var5 = 0;
      int var6 = 0;

      while (var5 < var2.length()) {
         if (var6 < var1.spans.size() && var5 == var1.spans.get(var6).origStart) {
            ScoreboardValueEditor.ReplacementSpan var10 = var1.spans.get(var6++);
            Style var11 = (Style)var3.get(Math.min(var10.origStart, var3.size() - 1));
            var4.append(Component.literal(var1.output.substring(var10.start, var10.end)).setStyle(var11));
            var5 = var10.origEnd;
         } else {
            int var7 = var6 < var1.spans.size() ? var1.spans.get(var6).origStart : var2.length();
            int var8 = var5;
            Style var9 = (Style)var3.get(var8);
            var5++;

            while (var5 < var7 && Objects.equals(var3.get(var5), var9)) {
               var5++;
            }

            var4.append(Component.literal(var2.substring(var8, var5)).setStyle(var9));
         }
      }

      return var4;
   }

   public static void зу() {
      西.clear();
      boolean var0 = ц衣();
      В_289b0448.set(var0);
      if (В3 != var0) {
         зБ();
         В3 = var0;
      }

      if (!var0) {
         с0 = null;
         Вр = false;
      }
   }

   public static Component method_9_f022d65c(Component var0) {
      if (цн() && var0 != null) {
         ScoreboardValueEditor.ComponentOverride var1 = В_f85d83ce.I(var0, var0x -> {
            String var1x = var0x.getString();
            ScoreboardValueEditor.RewriteResult var2 = method_9_7a9bb51c(var1x);
            return new ScoreboardValueEditor.ComponentOverride(var2 != null && !var2.output.equals(var1x) ? method_9(var0x, var2) : null);
         });
         return var1.value == null ? var0 : var1.value;
      } else {
         return var0;
      }
   }

   private static void ц_de5a6f78(String var0, String var1) {
      с0 = var0;
      сх = з(var0, var1);
      сУ = var1 == null ? "" : var1;
      Вр = true;
      зБ();
   }

   private ScoreboardValueEditor() {
   }

   private static String з(String var0, String var1) {
      for (ScoreboardValueEditor.EditHitRegion var3 : д_2571477d) {
         if (var3.key().equals(var0)) {
            return var3.original();
         }
      }

      return var1;
   }

   private static ScoreboardValueEditor.ScoreField method_9_8e61646e(String var0) {
      String var1 = var0.toLowerCase(Locale.ROOT);

      for (ScoreboardValueEditor.ScoreField var5 : ScoreboardValueEditor.ScoreField.values()) {
         if (var1.contains(var5.у)) {
            return var5;
         }
      }

      return null;
   }

   public static void з1() {
      д_2571477d = List.copyOf(西);
      西.clear();
      В_289b0448.set(false);
   }

   private static ScoreboardValueEditor.RewriteResult method_9_7a9bb51c(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         Optional var1 = Б.get(var0);
         if (var1 != null) {
            return (ScoreboardValueEditor.RewriteResult)var1.orElse(null);
         }

         ScoreboardValueEditor.ScoreField var2 = method_9_8e61646e(var0);
         ScoreboardValueEditor.RewriteResult var3 = var2 == null ? null : method_9(var0, var2);
         if (Б.size() >= 128) {
            Б.remove(Б.keySet().iterator().next());
         }

         Б.put(var0, Optional.ofNullable(var3));
         return var3;
      } else {
         return null;
      }
   }

   public static boolean цн() {
      return Boolean.TRUE.equals(В_289b0448.get());
   }

   private static String ц_388008bb(String var0, String var1) {
      return var0.equals(с0) ? сУ : д_ea411899.getOrDefault(var0, var1);
   }

   public static String ш(String var0) {
      if (цн() && var0 != null && !var0.isEmpty()) {
         ScoreboardValueEditor.RewriteResult var1 = method_9_7a9bb51c(var0);
         return var1 == null ? var0 : var1.output;
      } else {
         return var0;
      }
   }

   public static void зХ() {
      зЮ();
   }

   public static boolean ц衣() {
      if (!NameProtectionTextRewriter.Д2()) {
         return false;
      }

      NameProtect var0 = RoseClient.getInstance().getServices().getModules().getNameProtect();
      return var0 != null && var0.бЬ();
   }

   private record ComponentOverride(Component value) {
   }

   private record RewriteResult(String output, List<ScoreboardValueEditor.ReplacementSpan> spans) {
   }

   private enum ScoreField {
      RILLIKI("риллик"),
      ROMASHKI("ромашк"),
      BALANCE("баланс"),
      ELO("elo");

      private final String у;

      ScoreField(String var3) {
         this.у = var3;
      }
   }

   private record EditHitRegion(String key, String original, double x, double y, double width, double height) {
   }

   private record ReplacementSpan(String key, String original, int origStart, int origEnd, int start, int end) {
   }
}
