package sg.network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import settings.render.ReallyWorldRankGlyphs;
import sg.modules.impl.init.FriendEntry;
import sg.client.RoseClient;
import sg.modules.impl.misc.NameProtect;

public final class NameProtectionTextRewriter {
   private static final String ХЫ = "Protected";
   private static final Pattern Ы = Pattern.compile(NameProtectionTextRewriter.сМТ);
   private static final Pattern l = Pattern.compile(NameProtectionTextRewriter.сМО);
   private static final String Хl = "mc.ReallyWorld.me";
   private static final String Х8 = "t.me/pasterplace";
   private static final String Х5 = "ReallyWorld.ru";
   private static final String Хв = "t.me/pasterplace";
   private static final String Хъ = "vk.com/reallyworlds";
   private static final String ХМ = "t.me/pasterplace";
   private static final Object Ь = new Object();
   private static final ThreadLocal<Integer> Х = ThreadLocal.withInitial(() -> 0);
   private static final ThreadLocal<Integer> е = ThreadLocal.withInitial(() -> 0);
   private static final ThreadLocal<Integer> А = ThreadLocal.withInitial(() -> 0);
   private static final ThreadLocal<Integer> field_4 = ThreadLocal.withInitial(() -> 0);
   private static int тс;
   private static volatile String ХН = "";
   private static volatile String Хн = "";
   private static volatile int тВ = NameProtectionTextRewriter.сМФ;
   private static volatile NameProtectionTextRewriter.ReplacementRules Я = NameProtectionTextRewriter.ReplacementRules.empty();
   private static final ThreadLocal<NameProtectionTextRewriter.RewriteCache> п = ThreadLocal.withInitial(NameProtectionTextRewriter.RewriteCache::new);
   private static final String сМР = "???";
   private static final String сМ3 = "mc.ReallyWorld.me";
   private static final String сМр = "t.me/pasterplace";
   private static final String сМ6 = "ReallyWorld.ru";
   private static final String сМ_ = "t.me/pasterplace";
   private static final String сМШ = "vk.com/reallyworlds";
   private static final String сМы = "t.me/pasterplace";
   private static final int сМй = Integer.MIN_VALUE;
   private static final String сМЖ = "Protected";
   private static final String сМм = "Protected";
   private static final long сМК = Long.MIN_VALUE;
   private static final String сМI = "Protected";
   private static final String сМТ = "(?iu)ГРИФ\\s*#\\s*(\\d+)";
   private static final String сМО = "(?iu)Клан\\s*:\\s*(.+)";
   private static final int сМФ = Integer.MIN_VALUE;

   private static Component Ь(Component var0, String var1, List<NameProtectionTextRewriter.TextReplacement> var2) {
      if (var2.isEmpty()) {
         return var0;
      }

      ArrayList var3 = new ArrayList(var1.length());
      var0.visit((var1x, var2x) -> {
         for (int var3x = 0; var3x < var2x.length(); var3x++) {
            var3.add(var1x);
         }

         return Optional.empty();
      }, Style.EMPTY);
      if (var3.size() != var1.length()) {
         return var0;
      }

      MutableComponent var4 = Component.empty();
      int var5 = 0;

      for (NameProtectionTextRewriter.TextReplacement var7 : var2) {
         if (var7.start() >= var5 && var7.end() <= var1.length()) {
            Ь(var4, var1, var3, var5, var7.start());
            С(var4, var7.value(), var3, var7.start(), var7.end());
            var5 = var7.end();
         }
      }

      Ь(var4, var1, var3, var5, var1.length());
      return var4;
   }

   public static Component 必(Component var0) {
      if (var0 != null && Д2()) {
         Component var1 = ж(var0);
         return Ь(var1, false);
      } else {
         return var0;
      }
   }

   public static boolean Д9() {
      return тс > 0;
   }

   public static Component Е(Component var0) {
      return Ь(var0, true);
   }

   public static void жР() {
      int var0 = field_4.get() - 1;
      if (var0 <= 0) {
         field_4.remove();
      } else {
         field_4.set(var0);
      }
   }

   private static NameProtectionTextRewriter.RewriteCache Ь(NameProtectionTextRewriter.ReplacementRules var0) {
      NameProtectionTextRewriter.RewriteCache var1 = п.get();
      Minecraft var2 = Minecraft.getInstance();
      NameProtect var3 = RoseClient.getInstance().getServices().getModules().getNameProtect();
      char var4 = var3.бO() ? var3.Ю() : '\u0000';
      String var5 = var3.бЧ() ? var3.фР() : null;
      boolean var6 = var3.бИ();
      boolean var7 = var3.бж();
      Language var8 = Language.getInstance();
      String var9 = var1.ЯК;
      long var10 = var2.level == null ? сМК : var2.level.getGameTime();
      if (var4 != 0 && (var10 != var1.ХЗ || var1.Л != var2.level || var1.В != var0 || var1.Ъс != var4)) {
         var9 = ьы();
         var1.ХЗ = var10;
      }

      if (var1.В != var0
         || var1.Л != var2.level
         || var1.诶 != var8
         || var1.Ъс != var4
         || !Objects.equals(var1.ЯК, var9)
         || !Objects.equals(var1.ЯI, var5)
         || var1.АД != var6
         || var1.АЪ != var7) {
         var1.ся.clear();
         var1.Я.Ъ2();
         var1.Х.Ъ2();
         var1.е.Ъ2();
         var1.В = var0;
         var1.Л = var2.level;
         var1.诶 = var8;
         var1.Ъс = var4;
         var1.ЯК = var9;
         var1.ЯI = var5;
         var1.АД = var6;
         var1.АЪ = var7;
      }

      return var1;
   }

   private static Component Ь(Component var0, Pattern var1, int var2, String var3) {
      if (var0 != null && var1 != null) {
         String var4 = С(var0);
         Matcher var5 = var1.matcher(var4);
         ArrayList var6 = new ArrayList();

         while (var5.find()) {
            var6.add(new NameProtectionTextRewriter.TextReplacement(var5.start(var2), var5.end(var2), var3));
         }

         return Ь(var0, var4, var6);
      } else {
         return var0;
      }
   }

   private static String лЮ(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         boolean var1 = false;

         for (int var2 = 0; var2 < var0.length(); var2++) {
            if (ReallyWorldRankGlyphs.Ь(var0.charAt(var2))) {
               var1 = true;
               break;
            }
         }

         if (!var1) {
            return var0;
         }

         if (!RoseClient.getInstance().getServices().getModules().getNameProtect().бO()) {
            return var0;
         }

         boolean[] var7 = Ь(var0);
         if (var7 == null) {
            return var0;
         }

         char var3 = RoseClient.getInstance().getServices().getModules().getNameProtect().Ю();
         char[] var4 = var0.toCharArray();
         boolean var5 = false;

         for (int var6 = 0; var6 < var4.length; var6++) {
            if (var7[var6] && ReallyWorldRankGlyphs.Ь(var4[var6])) {
               var4[var6] = var3;
               var5 = true;
            }
         }

         return var5 ? new String(var4) : var0;
      } else {
         return var0;
      }
   }

   private static boolean 诶(String var0, String var1) {
      String var2 = var0.toLowerCase(Locale.ROOT);
      if (var2.equals(var1)) {
         return true;
      }

      if (!var2.startsWith(var1 + "_")) {
         return false;
      }

      for (int var3 = var1.length() + 1; var3 < var2.length(); var3++) {
         if (!Character.isDigit(var2.charAt(var3))) {
            return false;
         }
      }

      return true;
   }

   public static Component Ц(Component var0) {
      return Ь(var0, false);
   }

   public static void ж0() {
      ХН = "";
      Хн = "";
      тВ = сМй;
      Я = NameProtectionTextRewriter.ReplacementRules.empty();
   }

   private static void Ь(MutableComponent var0, String var1, List<Style> var2, int var3, int var4, Style var5) {
      if (var1 != null && !var1.isEmpty()) {
         int var6 = Math.max(1, var4 - var3);
         int var7 = var1.length();
         int var8 = 0;
         Style var9 = null;

         for (int var10 = 0; var10 < var7; var10++) {
            Style var11 = var5;
            if (var11 == null) {
               int var12 = Math.min(var6 - 1, (int)((long)var10 * var6 / var7));
               int var13 = Math.min(var2.size() - 1, var3 + var12);
               var11 = var13 >= 0 ? (Style)var2.get(var13) : Style.EMPTY;
            }

            if (var10 > 0 && !Objects.equals(var9, var11)) {
               var0.append(Component.literal(var1.substring(var8, var10)).setStyle(var9));
               var8 = var10;
            }

            var9 = var11;
         }

         var0.append(Component.literal(var1.substring(var8)).setStyle(var9));
      }
   }

   public static void жО() {
      int var0 = А.get() - 1;
      if (var0 <= 0) {
         А.remove();
      } else {
         А.set(var0);
      }
   }

   private static String ьв() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null ? var0.player.getGameProfile().name() : null;
   }

   public static String лO(String var0) {
      return лШ(var0);
   }

   public static FormattedCharSequence Ь_f9278523(FormattedCharSequence var0) {
      if (var0 == null || !Д2()) {
         return var0;
      }

      if (!RoseClient.getInstance().getServices().getModules().getNameProtect().бO()) {
         return var0;
      }

      boolean[] var1 = Ь_e1480976(var0);
      if (var1 == null) {
         return var0;
      }

      char var2 = RoseClient.getInstance().getServices().getModules().getNameProtect().Ю();
      return var3 -> var0.accept((var3x, var4, var5) -> {
         if (var3x >= 0 && var3x < var1.length && var1[var3x] && ReallyWorldRankGlyphs.Ь(var5)) {
            var5 = var2;
         }

         return var3.accept(var3x, var4, var5);
      });
   }

   private static NameProtectionTextRewriter.ReplacementRules С_49ed4904() {
      LinkedHashMap<String, String> var0 = new LinkedHashMap<>();
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         Ь(var0, var1.player.getGameProfile().name(), RoseClient.getInstance().getServices().getModules().getNameProtect().фи());

         for (FriendEntry var3 : RoseClient.getInstance().getServices().getFriends()) {
            String var4 = var3.lк() ? var3.l3() : сМЖ;
            Ь(var0, var3.lз(), var4);
         }

         if (var0.isEmpty()) {
            return NameProtectionTextRewriter.ReplacementRules.empty();
         }

         ArrayList<String> var5 = new ArrayList<>(var0.keySet());
         var5.sort(Comparator.comparingInt(String::length).reversed());
         String var6 = var5.stream().map(var0x -> Pattern.quote(var0x) + "(?:_\\d+)?").reduce((var0x, var1x) -> var0x + "|" + var1x).orElse("");
         Pattern var7 = Pattern.compile("(?iu)(?<![\\p{L}\\p{N}_])(?:" + var6 + ")(?![\\p{L}\\p{N}_])");
         return new NameProtectionTextRewriter.ReplacementRules(var7, Map.copyOf(var0));
      } else {
         return NameProtectionTextRewriter.ReplacementRules.empty();
      }
   }

   private static Component С(Component var0, String var1) {
      String var2 = С(var0);
      Matcher var3 = l.matcher(var2);
      return !var3.find() ? var0 : Ь(var0, var3.start(1), var3.end(1), var1, Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
   }

   private static void Ь(MutableComponent var0, String var1, List<Style> var2, int var3, int var4) {
      int var5 = var3;

      while (var5 < var4) {
         Style var6 = (Style)var2.get(var5);
         int var7 = var5 + 1;

         while (var7 < var4 && Objects.equals(var2.get(var7), var6)) {
            var7++;
         }

         var0.append(Component.literal(var1.substring(var5, var7)).setStyle(var6));
         var5 = var7;
      }
   }

   public static boolean ДЗ() {
      return е.get() > 0;
   }

   public static void жц() {
      field_4.set(field_4.get() + 1);
   }

   private static Style Ь(List<NameProtectionTextRewriter.StyledTextSpan> var0, int var1) {
      for (NameProtectionTextRewriter.StyledTextSpan var3 : var0) {
         if (var1 >= var3.start() && var1 < var3.end()) {
            return var3.style();
         }
      }

      return Style.EMPTY;
   }

   public static Component Д(Component var0) {
      if (var0 != null && Д2()) {
         NameProtect var1 = RoseClient.getInstance().getServices().getModules().getNameProtect();
         if (!var1.бж()) {
            return var0;
         }

         Component var2 = Ь(var0, сМ6, сМ_);
         return Ь(var2, сМШ, сМы);
      } else {
         return var0;
      }
   }

   public static void ж_() {
      if (тс > 0) {
         тс--;
      }
   }

   public static void ж3() {
      е.set(е.get() + 1);
   }

   private static boolean[] Ь(String var0) {
      String var1 = ьв();
      if (var1 != null && !var1.isBlank()) {
         NameProtectionTextRewriter.ReplacementRules var2 = Ь();
         if (var2.isEmpty()) {
            return null;
         }

         boolean[] var3 = null;
         String var4 = ьы();
         if (var4 != null && !var4.isBlank()) {
            int var5 = 0;

            while (var5 <= var0.length() - var4.length()) {
               int var6 = С(var0, var4, var5);
               if (var6 < 0) {
                  break;
               }

               int var7 = var6 + var4.length();

               for (int var8 = var6; var8 < var7; var8++) {
                  if (ReallyWorldRankGlyphs.Ь(var0.charAt(var8))) {
                     if (var3 == null) {
                        var3 = new boolean[var0.length()];
                     }

                     var3[var8] = true;
                  }
               }

               var5 = var7;
            }
         }

         String var10 = var1.toLowerCase(Locale.ROOT);
         Matcher var11 = var2.pattern().matcher(var0);

         while (var11.find()) {
            if (诶(var11.group(), var10)) {
               int var12 = Math.max(0, var11.start() - 48);

               for (int var13 = var11.start() - 1; var13 >= var12; var13--) {
                  char var9 = var0.charAt(var13);
                  if (var9 == 187 || var9 == '\n' || var9 == '\r') {
                     break;
                  }

                  if (ReallyWorldRankGlyphs.Ь(var9)) {
                     if (var3 == null) {
                        var3 = new boolean[var0.length()];
                     }

                     var3[var13] = true;
                     break;
                  }
               }
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private static Component ж(Component var0) {
      if (!RoseClient.getInstance().getServices().getModules().getNameProtect().бO()) {
         return var0;
      }

      MutableComponent var1 = Component.empty();
      boolean[] var2 = new boolean[]{false};
      var0.visit((var2x, var3) -> {
         if (!var3.isEmpty()) {
            String var4 = RoseClient.getInstance().getServices().getModules().getNameProtect().о(var3);
            if (!var4.equals(var3)) {
               var2[0] = true;
            }

            var1.append(Component.literal(var4).setStyle(var2x));
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var2[0] ? var1 : var0;
   }

   private static Component method_4(Component var0) {
      Component var1 = Е(var0);
      NameProtect var2 = RoseClient.getInstance().getServices().getModules().getNameProtect();
      if (var2.бЧ()) {
         var1 = С(var1, var2.фР());
      }

      if (var2.бИ()) {
         var1 = Ь(var1, Ы, 1, сМР);
      }

      if (var2.бж()) {
         var1 = Ь(var1, сМ3, сМр);
      }

      return var1;
   }

   public static void жЭ() {
      А.set(А.get() + 1);
   }

   private static Component Ь(Component var0, String var1, String var2) {
      if (var0 != null && var1 != null && !var1.isEmpty()) {
         String var3 = С(var0);
         ArrayList var4 = new ArrayList();
         int var5 = 0;

         while (var5 <= var3.length() - var1.length()) {
            int var6 = С(var3, var1, var5);
            if (var6 < 0) {
               break;
            }

            var4.add(new NameProtectionTextRewriter.TextReplacement(var6, var6 + var1.length(), var2));
            var5 = var6 + var1.length();
         }

         return Ь(var0, var3, var4);
      } else {
         return var0;
      }
   }

   private static String Ь(String var0, NameProtectionTextRewriter.ReplacementRules var1) {
      if (var1.isEmpty()) {
         return var0;
      }

      NameProtectionTextRewriter.RewriteCache var2 = Ь(var1);
      String var3 = var2.ся.get(var0);
      if (var3 != null) {
         return var3;
      }

      String var4 = С(лЮ(var0), var1);
      if (var0.length() <= 4096) {
         if (var2.ся.size() >= 512) {
            var2.ся.clear();
         }

         var2.ся.put(var0, var4);
      }

      return var4;
   }

   private static String С(String var0, NameProtectionTextRewriter.ReplacementRules var1) {
      Matcher var2 = var1.pattern().matcher(var0);
      if (!var2.find()) {
         return var0;
      }

      StringBuffer var3 = new StringBuffer(var0.length());

      do {
         String var4 = ь(var2.group(), var1);
         var2.appendReplacement(var3, Matcher.quoteReplacement(var4));
      } while (var2.find());

      var2.appendTail(var3);
      return var3.toString();
   }

   private static void С(MutableComponent var0, String var1, List<Style> var2, int var3, int var4) {
      Ь(var0, var1, var2, var3, var4, null);
   }

   public static void жд() {
      int var0 = е.get() - 1;
      if (var0 <= 0) {
         е.remove();
      } else {
         е.set(var0);
      }
   }

   private static void Ь(Map<String, String> var0, String var1, String var2) {
      if (var1 != null && !var1.isBlank()) {
         var0.putIfAbsent(var1.toLowerCase(Locale.ROOT), var2 != null && !var2.isBlank() ? var2 : сМм);
      }
   }

   private static void Ь(MutableComponent var0, List<NameProtectionTextRewriter.StyledTextSpan> var1, int var2, int var3, String var4) {
      if (var2 < var3) {
         for (NameProtectionTextRewriter.StyledTextSpan var6 : var1) {
            int var7 = Math.max(var2, var6.start());
            int var8 = Math.min(var3, var6.end());
            if (var7 < var8) {
               var0.append(Component.literal(var4.substring(var7, var8)).setStyle(var6.style()));
            }
         }
      }
   }

   public static Component И(Component var0) {
      if (var0 != null && Д2()) {
         NameProtectionTextRewriter.RewriteCache var1 = Ь(Ь());
         NameProtectionTextRewriter.ComponentOverride var2 = var1.е.I(var0, var0x -> {
            Component var1x = method_4(var0x);
            return new NameProtectionTextRewriter.ComponentOverride(var1x == var0x ? null : var1x);
         });
         return var2.value == null ? var0 : var2.value;
      } else {
         return var0;
      }
   }

   public static int Ьа() {
      if (!Д2()) {
         return 0;
      }

      Minecraft var0 = Minecraft.getInstance();
      NameProtect var1 = RoseClient.getInstance().getServices().getModules().getNameProtect();
      int var2 = 1;
      var2 = 31 * var2 + var0.player.getGameProfile().name().hashCode();
      var2 = 31 * var2 + var1.фи().hashCode();
      var2 = 31 * var2 + RoseClient.getInstance().getServices().getFriends().яб();
      var2 = 31 * var2 + (var1.бO() ? var1.Ю() : '\u0000');
      return 31 * var2 + (var1.бж() ? 1 : 0);
   }

   public static boolean ДМ() {
      return А.get() > 0;
   }

   public static boolean Д8() {
      return field_4.get() > 0;
   }

   public static boolean Д7() {
      return Х.get() > 0;
   }

   public static void жЫ() {
      int var0 = Х.get() - 1;
      if (var0 <= 0) {
         Х.remove();
      } else {
         Х.set(var0);
      }
   }

   private static NameProtectionTextRewriter.ReplacementRules Ь() {
      if (!Д2()) {
         return NameProtectionTextRewriter.ReplacementRules.empty();
      }

      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null && var0.player != null) {
         String var1 = var0.player.getGameProfile().name();
         String var2 = RoseClient.getInstance().getServices().getModules().getNameProtect().фи();
         int var3 = RoseClient.getInstance().getServices().getFriends().яб();
         if (var1.equalsIgnoreCase(ХН) && var2.equals(Хн) && var3 == тВ) {
            return Я;
         }

         synchronized (Ь) {
            if (var1.equalsIgnoreCase(ХН) && var2.equals(Хн) && var3 == тВ) {
               return Я;
            }

            Я = С_49ed4904();
            ХН = var1;
            Хн = var2;
            тВ = var3;
            return Я;
         }
      } else {
         return NameProtectionTextRewriter.ReplacementRules.empty();
      }
   }

   private static int С(String var0, String var1, int var2) {
      int var3 = var0.length() - var1.length();

      for (int var4 = Math.max(0, var2); var4 <= var3; var4++) {
         if (var0.regionMatches(true, var4, var1, 0, var1.length())) {
            return var4;
         }
      }

      return -1;
   }

   private static String С(Component var0) {
      жн();

      try {
         return var0.getString();
      } finally {
         жЫ();
      }
   }

   public static Component Ь(Component var0, String var1) {
      if (var0 == null) {
         return Component.literal(лб(var1));
      }

      if (!Д2()) {
         return var0;
      }

      String var2 = лб(var1);
      return var2 != null && !var2.equals(var1) ? Component.literal(var2).setStyle(var0.getStyle()) : var0;
   }

   public static void жн() {
      Х.set(Х.get() + 1);
   }

   public static String лЧ(String var0) {
      return лШ(var0);
   }

   private static boolean[] Ь_e1480976(FormattedCharSequence var0) {
      StringBuilder var1 = new StringBuilder();
      var0.accept((var1x, var2, var3) -> {
         var1.appendCodePoint(var3);
         return true;
      });
      return Ь(var1.toString());
   }

   private static String ьы() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null ? С(var0.player.getDisplayName()) : null;
   }

   private NameProtectionTextRewriter() {
   }

   public static String лШ(String var0) {
      return var0 != null && !var0.isEmpty() && Д2() ? Ь(var0, Ь()) : var0;
   }

   public static Component Ф(Component var0) {
      return Ь(var0, true);
   }

   private static String ь(String var0, NameProtectionTextRewriter.ReplacementRules var1) {
      String var2 = var0.toLowerCase(Locale.ROOT);
      String var3 = var1.replacements().get(var2);
      if (var3 != null) {
         return var3;
      }

      int var4 = var2.lastIndexOf(95);
      if (var4 > 0 && var4 + 1 < var2.length()) {
         boolean var5 = true;

         for (int var6 = var4 + 1; var6 < var2.length(); var6++) {
            if (!Character.isDigit(var2.charAt(var6))) {
               var5 = false;
               break;
            }
         }

         if (var5) {
            var3 = var1.replacements().get(var2.substring(0, var4));
            if (var3 != null) {
               return var3;
            }
         }
      }

      return сМI;
   }

   public static boolean Д2() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null
         && var0.player != null
         && RoseClient.getInstance() != null
         && RoseClient.getInstance().getServices() != null
         && RoseClient.getInstance().getServices().getModules() != null
         && RoseClient.getInstance().getServices().getModules().getNameProtect() != null
         && RoseClient.getInstance().getServices().getModules().getNameProtect().isEnabled();
   }

   private static Component С(Component var0, boolean var1) {
      NameProtectionTextRewriter.ReplacementRules var2 = Ь();
      if (var2.isEmpty()) {
         return var0;
      }

      String var3 = var0.getString();
      String var4 = var1 ? лЮ(var3) : var3;
      Matcher var5 = var2.pattern().matcher(var4);
      if (!var5.find()) {
         return var0;
      }

      ArrayList var6 = new ArrayList();
      int[] var7 = new int[]{0};
      var0.visit((var2x, var3x) -> {
         if (!var3x.isEmpty()) {
            int var4x = var7[0];
            var7[0] += var3x.length();
            var6.add(new NameProtectionTextRewriter.StyledTextSpan(var4x, var7[0], var3x, var2x));
         }

         return Optional.empty();
      }, Style.EMPTY);
      MutableComponent var8 = Component.empty();
      int var9 = 0;

      do {
         Ь(var8, var6, var9, var5.start(), var4);
         String var10 = ь(var5.group(), var2);
         var8.append(Component.literal(var10).setStyle(Ь(var6, var5.start())));
         var9 = var5.end();
      } while (var5.find());

      Ь(var8, var6, var9, var4.length(), var4);
      return var8;
   }

   public static String С(Player var0) {
      return var0 == null ? "" : лб(var0.getGameProfile().name());
   }

   public static void жЛ() {
      тс++;
   }

   private static Component Ь(Component var0, boolean var1) {
      if (var0 != null && Д2()) {
         NameProtectionTextRewriter.RewriteCache var2 = Ь(Ь());
         StyledComponentCache<NameProtectionTextRewriter.ComponentOverride> var3 = var1 ? var2.Я : var2.Х;
         NameProtectionTextRewriter.ComponentOverride var4 = var3.I(var0, var1x -> {
            Component var2x = С(var1x, var1);
            return new NameProtectionTextRewriter.ComponentOverride(var2x == var1x ? null : var2x);
         });
         return var4.value == null ? var0 : var4.value;
      } else {
         return var0;
      }
   }

   private static Component Ь(Component var0, int var1, int var2, String var3, Style var4) {
      String var5 = С(var0);
      if (var1 >= 0 && var2 >= var1 && var2 <= var5.length()) {
         ArrayList var6 = new ArrayList(var5.length());
         var0.visit((var1x, var2x) -> {
            for (int var3x = 0; var3x < var2x.length(); var3x++) {
               var6.add(var1x);
            }

            return Optional.empty();
         }, Style.EMPTY);
         if (var6.size() != var5.length()) {
            return var0;
         }

         MutableComponent var7 = Component.empty();
         Ь(var7, var5, var6, 0, var1);
         Ь(var7, var3, var6, var1, var2, var4);
         Ь(var7, var5, var6, var2, var5.length());
         return var7;
      } else {
         return var0;
      }
   }

   public static String лб(String var0) {
      if (var0 != null && !var0.isBlank() && Д2()) {
         String var1 = Ь().replacements().get(var0.toLowerCase(Locale.ROOT));
         return var1 == null ? var0 : var1;
      } else {
         return var0;
      }
   }

   private static final class RewriteCache {
      private NameProtectionTextRewriter.ReplacementRules В;
      private Object Л;
      private Object 诶;
      private int Ъс;
      private String ЯК = "";
      private long ХЗ;
      private String ЯI;
      private boolean АД;
      private boolean АЪ;
      private final Map<String, String> ся;
      private final StyledComponentCache<NameProtectionTextRewriter.ComponentOverride> Я;
      private final StyledComponentCache<NameProtectionTextRewriter.ComponentOverride> Х;
      private final StyledComponentCache<NameProtectionTextRewriter.ComponentOverride> е;
      private static final long цК = Long.MIN_VALUE;

      private RewriteCache() {
         this.ХЗ = цК;
         this.ся = new HashMap<>();
         this.Я = new StyledComponentCache<>(256);
         this.Х = new StyledComponentCache<>(256);
         this.е = new StyledComponentCache<>(128);
      }
   }

   private record ReplacementRules(Pattern pattern, Map<String, String> replacements) {
      private boolean isEmpty() {
         return this.pattern == null || this.replacements.isEmpty();
      }

      private static NameProtectionTextRewriter.ReplacementRules empty() {
         return new NameProtectionTextRewriter.ReplacementRules(null, Map.of());
      }
   }

   private record StyledTextSpan(int start, int end, String value, Style style) {
   }

   private record TextReplacement(int start, int end, String value) {
   }

   private record ComponentOverride(Component value) {
   }
}
