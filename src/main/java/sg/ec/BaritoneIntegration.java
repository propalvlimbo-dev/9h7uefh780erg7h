package sg.ec;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BaritoneIntegration {
   private static final String сз = "baritone";
   private static final String ВуЙ = "baritone";
   private static final String Вук = "baritone-api";
   private static final String Вуг = "baritone-meteor";
   private static final String Ву0 = "baritone";
   private static final String Вух = "getPathingBehavior";
   private static final String ВуУ = "cancelEverything";
   private static final String ВуЗ = "isActive";
   private static final String ВуЕ = "isPaused";
   private static final String ВуП = "pause";
   private static final String Ву西 = "resume";
   private static final String Вуд = "getInputOverrideHandler";
   private static final String ВуБ = "clearAllKeys";
   private static final String ВуС = "getInputOverrideHandler";
   private static final String Вуз = "baritone.api.utils.input.Input";
   private static final String Вуц = "SNEAK";
   private static final String Вуя = "setInputForceState";
   private static final String В2с = "Baritone недоступен";
   private static final String В2В = "baritone.api.pathing.goals.GoalNear";
   private static final String В2Я = "getCustomGoalProcess";
   private static final String В2Х = "setGoalAndPath";
   private static final String В2е = "baritone.api.pathing.goals.Goal";
   private static final String В2А = "baritone.api.pathing.goals.GoalBlock";
   private static final String В24 = "getCustomGoalProcess";
   private static final String В2п = "setGoalAndPath";
   private static final String В2Д = "baritone.api.pathing.goals.Goal";
   private static final String В2Ъ = "getPathingBehavior";
   private static final String В2т = "isPathing";
   private static final String В2а = "BuilderProcess недоступен (Baritone не инициализирован?)";
   private static final String В2Ю = "baritone.api.schematic.ISchematic";
   private static final String В2Ч = "Не найден метод build(String, ISchematic, Vec3i)";
   private static final String В2Л = "widthX";
   private static final String В2诶 = "heightY";
   private static final String В21 = "lengthZ";
   private static final String В2Ь = "reset";
   private static final String В2衣 = "inSchematic";
   private static final String В2ш = "desiredState";
   private static final String В2И = "size";
   private static final String В2ь = "toString";
   private static final String В2ж = "hashCode";
   private static final String В2弟 = "equals";
   private static final String В2б = "X";
   private static final String В2Ы = "Y";
   private static final String В2l = "Z";
   private static final String В28 = "AutoBrewerSchematic";
   private static final String В25 = "allowPlace";
   private static final String В2в = "allowBreak";
   private static final String В2ъ = "allowInventory";
   private static final String В2М = "buildInLayers";
   private static final String В2Н = "layerOrder";
   private static final String В2н = "mapArtMode";
   private static final String В2Ц = "okIfWater";
   private static final String В2ч = "distanceTrim";
   private static final String В2Э = "rightClickContainerOnArrival";
   private static final String В2必 = "Baritone не установлен";
   private static final String В2щ = "Нечего добывать";
   private static final String В2Г = "Baritone недоступен";
   private static final String В2л = "getMineProcess";
   private static final String В2э = "MineProcess недоступен";
   private static final String В2ю = "mineByName";
   private static final String В2Щ = "net.minecraft.world.level.block.Block";
   private static final String В2у = "mine";
   private static final String В22 = "mineByName";
   private static final String В2ф = "getMineProcess";
   private static final String В2и = "isActive";
   private static final String В29 = "getMineProcess";
   private static final String В2о = "displayName";
   private static final String В2O = "filter";
   private static final String В27 = "BlockOptionalMetaLookup";
   private static final String В2Р = "getBlock";
   private static final String В23 = "blocks";
   private static final String В2р = "Block\\{(?:minecraft:)?([a-z0-9_/]+)";
   private static final String В26 = "air";
   private static final String В2_ = "air";
   private static final String В2Ш = "allowBreak";
   private static final String В2ы = "allowPlace";
   private static final String В2й = "allowInventory";
   private static final String В2Ж = "exploreForBlocks";
   private static final String В2м = "net.minecraft.core.registries.BuiltInRegistries";
   private static final String В2К = "BLOCK";
   private static final String В2I = "net.minecraft.resources.Identifier";
   private static final String В2Т = "parse";
   private static final String В2О = "getValue";
   private static final String В2Ф = ":";
   private static final String В2Й = "baritone.api.pathing.goals.GoalBlock";
   private static final String В2к = "getCustomGoalProcess";
   private static final String В2г = "setGoalAndPath";
   private static final String В20 = "baritone.api.pathing.goals.Goal";
   private static final String В2х = "value";
   private static final String В2У = "blocksToAvoid";
   private static final String В2З = "value";
   private static final String В2Е = "baritone.api.BaritoneAPI";
   private static final String В2П = "getSettings";
   private static final String В2西 = "build";
   private static final String В2д = "baritone.api.BaritoneAPI";
   private static final String В2Б = "getProvider";
   private static final String В2С = "getPrimaryBaritone";
   private static final String В2з = "getBuilderProcess";

   private static Object Ъ(String var0) throws Exception {
      Class var1 = Class.forName(В2Е);
      Object var2 = var1.getMethod(В2П).invoke(null);
      return var2.getClass().getField(var0).get(var2);
   }

   public static void г1() {
      Ъ(В25, true);
      Ъ(В2в, true);
      Ъ(В2ъ, true);
      Ъ(В2М, true);
      Ъ(В2Н, false);
      Ъ(В2н, false);
      Ъ(В2Ц, true);
      Ъ(В2ч, false);
      Ъ(В2Э, false);
   }

   public static boolean ЮН() {
      try {
         Object var0 = г();
         if (var0 == null) {
            return false;
         }

         Object var1 = var0.getClass().getMethod(В2Ъ).invoke(var0);
         Object var2 = var1.getClass().getMethod(В2т).invoke(var1);
         return Boolean.TRUE.equals(var2);
      } catch (Throwable var3) {
         return false;
      }
   }

   private static void Ъ(Identifier var0, Set<String> var1) {
      if (var0 != null) {
         String var2 = var0.getPath();
         if (var2 != null && !var2.isEmpty() && !В2_.equals(var2)) {
            var1.add(var2);
         }
      }
   }

   public static boolean ЮИ() {
      try {
         Object var0 = О();
         if (var0 == null) {
            return false;
         }

         Object var1 = var0.getClass().getMethod(ВуЕ).invoke(var0);
         return Boolean.TRUE.equals(var1);
      } catch (Throwable var2) {
         return false;
      }
   }

   public static void Ъ(String var0, Object var1) {
      try {
         Object var2 = Ъ(var0);
         if (var2 == null) {
            return;
         }

         Field var3 = var2.getClass().getField(В2х);
         Object var4 = var3.get(var2);
         if (var4 instanceof Boolean && var1 instanceof Boolean) {
            var3.set(var2, var1);
         } else if (var4 instanceof Integer && var1 instanceof Number var5) {
            var3.set(var2, var5.intValue());
         } else if (var4 instanceof Float && var1 instanceof Number var6) {
            var3.set(var2, var6.floatValue());
         } else if (var4 instanceof Double && var1 instanceof Number var7) {
            var3.set(var2, var7.doubleValue());
         } else if (var4 != null && var1 != null && var4.getClass().isInstance(var1)) {
            var3.set(var2, var1);
         }
      } catch (Throwable var8) {
      }
   }

   private static void ы(String var0, boolean var1) {
      Ъ(var0, Boolean.valueOf(var1));
   }

   private static Object Ъ(String[] var0, Class<?> var1) {
      ArrayList var2 = new ArrayList();

      try {
         Class var3 = Class.forName(В2м);
         Object var4 = var3.getField(В2К).get(null);
         Class var5 = Class.forName(В2I);
         Method var6 = null;

         for (Method var10 : var5.getMethods()) {
            if (var10.getName().equals(В2Т) && var10.getParameterCount() == 1 && var10.getParameterTypes()[0] == String.class) {
               var6 = var10;
               break;
            }
         }

         Method var18 = null;

         for (Method var11 : var4.getClass().getMethods()) {
            if (var11.getName().equals(В2О) && var11.getParameterCount() == 1 && var11.getParameterTypes()[0] == var5) {
               var18 = var11;
               break;
            }
         }

         if (var6 == null || var18 == null) {
            return Array.newInstance(var1, 0);
         }

         for (String var25 : var0) {
            String var12 = var25.contains(В2Ф) ? var25 : "minecraft:" + var25;
            Object var13 = var6.invoke(null, var12);
            Object var14 = var18.invoke(var4, var13);
            if (var14 != null && var1.isInstance(var14)) {
               var2.add(var14);
            }
         }
      } catch (Throwable var15) {
      }

      Object var16 = Array.newInstance(var1, var2.size());

      for (int var17 = 0; var17 < var2.size(); var17++) {
         Array.set(var16, var17, var2.get(var17));
      }

      return var16;
   }

   public static void ы(int var0, int var1, int var2) {
      try {
         Object var3 = г();
         if (var3 == null) {
            return;
         }

         ClassLoader var4 = var3.getClass().getClassLoader();
         Class var5 = Class.forName(В2Й, true, var4);
         Object var6 = var5.getConstructor(int.class, int.class, int.class).newInstance(var0, var1, var2);
         Object var7 = var3.getClass().getMethod(В2к).invoke(var3);
         var7.getClass().getMethod(В2г, Class.forName(В20, true, var4)).invoke(var7, var6);
      } catch (Throwable var8) {
      }
   }

   private static Object Ъ(BaritoneIntegration.BuildSchematic var0, Method var1, Object[] var2) {
      String var3 = var1.getName();
      Class var4 = var1.getReturnType();

      try {
         Object var5 = switch (var3) {
            case В2Л -> var0.西();
            case В2诶 -> var0.l();
            case В21 -> var0.必();
            case В2Ь -> {
               var0.ы衣();
               yield null;
            }
            case В2衣 -> {
               int var16 = ((Number)var2[0]).intValue();
               int var18 = ((Number)var2[1]).intValue();
               int var21 = ((Number)var2[2]).intValue();
               yield var0.诶(var16, var18, var21);
            }
            case В2ш -> {
               int var15 = ((Number)var2[0]).intValue();
               int var17 = ((Number)var2[1]).intValue();
               int var20 = ((Number)var2[2]).intValue();
               BlockState var22 = var2.length > 3 && var2[3] instanceof BlockState var12 ? var12 : null;
               List var23 = var2.length > 4 ? Ъ_2571477d(var2[4]) : List.of();
               BlockState var24 = var0.з(var15, var17, var20, var22, var23);
               yield var24 != null ? var24 : Blocks.AIR.defaultBlockState();
            }
            case В2И -> {
               Object var8 = var2[0];
               String var9 = var8 instanceof Enum var10 ? var10.name() : String.valueOf(var8);
               switch (var9) {
                  case В2б:
                     yield var0.西();
                  case В2Ы:
                     yield var0.l();
                  case В2l:
                     yield var0.必();
                  default:
                     yield 0;
               }
            }
            case В2ь -> В28;
            case В2ж -> System.identityHashCode(var0);
            case В2弟 -> var2.length > 0 && var2[0] == var0;
            default -> Ъ(var4);
         };
         return var5 == null ? Ъ(var4) : var5;
      } catch (Throwable var14) {
         var14.printStackTrace();
         return Ъ(var4);
      }
   }

   public static void Ъ(String var0, boolean var1) {
      Ъ(var0, Boolean.valueOf(var1));
   }

   private static Object Ъ(Class<?> var0) {
      if (var0 == boolean.class) {
         return false;
      } else if (var0 == int.class) {
         return 0;
      } else if (var0 == long.class) {
         return 0L;
      } else if (var0 == float.class) {
         return 0.0F;
      } else if (var0 == double.class) {
         return 0.0;
      } else if (var0 == void.class || var0 == Void.class) {
         return null;
      } else {
         return BlockState.class.isAssignableFrom(var0) ? Blocks.AIR.defaultBlockState() : null;
      }
   }

   public static boolean ЮЧ() {
      return FabricLoader.getInstance().isModLoaded(ВуЙ)
         || FabricLoader.getInstance().isModLoaded(Вук)
         || FabricLoader.getInstance().isModLoaded(Вуг)
         || FabricLoader.getInstance().getModContainer(Ву0).isPresent();
   }

   private static List<BlockState> Ъ_2571477d(Object var0) {
      return var0 instanceof List var1 ? var1 : List.of();
   }

   public static void г2() {
      try {
         Object var0 = г();
         if (var0 == null) {
            return;
         }

         Object var1 = var0.getClass().getMethod(Вух).invoke(var0);
         var1.getClass().getMethod(ВуУ).invoke(var1);
      } catch (Throwable var2) {
      }
   }

   private static Object О() throws Exception {
      Object var0 = г();
      return var0 == null ? null : var0.getClass().getMethod(В2з).invoke(var0);
   }

   private static Object г() throws Exception {
      Class var0 = Class.forName(В2д);
      Object var1 = var0.getMethod(В2Б).invoke(null);
      return var1.getClass().getMethod(В2С).invoke(var1);
   }

   public static String Ъ(BlockPos var0, int var1) {
      try {
         Object var2 = г();
         if (var2 == null) {
            return В2с;
         }

         ClassLoader var10 = var2.getClass().getClassLoader();
         Class var11 = Class.forName(В2В, true, var10);
         Object var12 = var11.getConstructor(BlockPos.class, int.class).newInstance(var0, var1);
         Object var13 = var2.getClass().getMethod(В2Я).invoke(var2);
         var13.getClass().getMethod(В2Х, Class.forName(В2е, true, var10)).invoke(var13, var12);
         return null;
      } catch (Throwable var9) {
         try {
            Object var3 = г();
            ClassLoader var4 = var3.getClass().getClassLoader();
            Class var5 = Class.forName(В2А, true, var4);
            Object var6 = var5.getConstructor(int.class, int.class, int.class).newInstance(var0.getX(), var0.getY(), var0.getZ());
            Object var7 = var3.getClass().getMethod(В24).invoke(var3);
            var7.getClass().getMethod(В2п, Class.forName(В2Д, true, var4)).invoke(var7, var6);
            return null;
         } catch (Throwable var8) {
            return "Не удалось запустить path: " + var8.getMessage();
         }
      }
   }

   public static void гЧ() {
      Ъ(В2Ш, true);
      Ъ(В2ы, true);
      Ъ(В2й, true);
      Ъ(В2Ж, true);
   }

   private static Object Ъ(BlockPos var0, Class<?> var1) {
      if (var1.isInstance(var0)) {
         return var0;
      }

      if (Vec3i.class.isAssignableFrom(var1)) {
         return new Vec3i(var0.getX(), var0.getY(), var0.getZ());
      }

      try {
         return var1.getConstructor(int.class, int.class, int.class).newInstance(var0.getX(), var0.getY(), var0.getZ());
      } catch (Throwable var3) {
         return new Vec3i(var0.getX(), var0.getY(), var0.getZ());
      }
   }

   private static void Ъ(Object var0, Set<String> var1) {
      if (var0 != null) {
         if (var0 instanceof Iterable) {
            for (Object var14 : (Iterable)var0) {
               Ъ(var14, var1);
            }
         } else if (!var0.getClass().isArray()) {
            if (var0 instanceof Block var10) {
               Ъ(BuiltInRegistries.BLOCK.getKey(var10), var1);
            } else {
               try {
                  Object var7 = var0.getClass().getMethod(В2Р).invoke(var0);
                  if (var7 != null && var7 != var0) {
                     Ъ(var7, var1);
                     if (!var1.isEmpty()) {
                        return;
                     }
                  }
               } catch (Throwable var6) {
               }

               try {
                  Object var8 = var0.getClass().getMethod(В23).invoke(var0);
                  if (var8 != null && var8 != var0) {
                     Ъ(var8, var1);
                     if (!var1.isEmpty()) {
                        return;
                     }
                  }
               } catch (Throwable var5) {
               }

               String var9 = String.valueOf(var0);
               Matcher var12 = Pattern.compile(В2р).matcher(var9);

               while (var12.find()) {
                  String var4 = var12.group(1);
                  if (!var4.isEmpty() && !В26.equals(var4)) {
                     var1.add(var4);
                  }
               }
            }
         } else {
            int var2 = Array.getLength(var0);

            for (int var3 = 0; var3 < var2; var3++) {
               Ъ(Array.get(var0, var3), var1);
            }
         }
      }
   }

   public static void гШ() {
      try {
         Object var0 = г();
         if (var0 == null) {
            return;
         }

         Object var1 = var0.getClass().getMethod(Вуд).invoke(var0);
         var1.getClass().getMethod(ВуБ).invoke(var1);
      } catch (Throwable var2) {
      }
   }

   public static String Ъ(String var0, BaritoneIntegration.BuildSchematic var1, BlockPos var2) {
      try {
         Object var3 = О();
         if (var3 == null) {
            return В2а;
         }

         ClassLoader var4 = var3.getClass().getClassLoader();
         Class var5 = Class.forName(В2Ю, true, var4);
         Object var6 = Proxy.newProxyInstance(var4, new Class[]{var5}, (var1x, var2x, var3x) -> Ъ(var1, var2x, var3x == null ? new Object[0] : var3x));
         Method var7 = Ъ(var3.getClass(), var5);
         if (var7 == null) {
            return В2Ч;
         }

         Class var8 = var7.getParameterTypes()[2];
         Object var9 = Ъ(var2, var8);
         var7.invoke(var3, var0, var6, var9);
         г0();
         return null;
      } catch (Throwable var10) {
         var10.printStackTrace();
         return "Ошибка Baritone API: " + var10.getClass().getSimpleName() + ": " + var10.getMessage();
      }
   }

   public static List<String> л() {
      if (!ЮФ()) {
         return List.of();
      }

      try {
         Object var0 = г();
         if (var0 == null) {
            return List.of();
         }

         Object var1 = var0.getClass().getMethod(В29).invoke(var0);
         if (var1 == null) {
            return List.of();
         }

         LinkedHashSet var2 = new LinkedHashSet();
         Object var3 = Ъ_7fd512f1(var1);
         if (var3 != null) {
            Ъ(var3, var2);
         }

         if (var2.isEmpty()) {
            try {
               Object var4 = var1.getClass().getMethod(В2о).invoke(var1);
               Ъ(var4, var2);
            } catch (Throwable var5) {
            }
         }

         return List.copyOf(var2);
      } catch (Throwable var6) {
         return List.of();
      }
   }

   private static Method Ъ(Class<?> var0, Class<?> var1) {
      Method var2 = null;

      for (Method var6 : var0.getMethods()) {
         if (В2西.equals(var6.getName())) {
            Class[] var7 = var6.getParameterTypes();
            if (var7.length == 3 && var7[0] == String.class && (var7[1].isAssignableFrom(var1) || var7[1] == var1)) {
               var2 = var6;
               if (Vec3i.class.isAssignableFrom(var7[2]) || BlockPos.class.isAssignableFrom(var7[2])) {
                  return var6;
               }
            }
         }
      }

      return var2;
   }

   public static void гю() {
      try {
         Object var0 = О();
         if (var0 == null) {
            return;
         }

         var0.getClass().getMethod(ВуП).invoke(var0);
      } catch (Throwable var1) {
      }
   }

   public static boolean Ю8() {
      try {
         Object var0 = О();
         if (var0 == null) {
            return false;
         }

         Object var1 = var0.getClass().getMethod(ВуЗ).invoke(var0);
         return Boolean.TRUE.equals(var1);
      } catch (Throwable var2) {
         return false;
      }
   }

   public static boolean ЮФ() {
      try {
         Object var0 = г();
         if (var0 == null) {
            return false;
         }

         Object var1 = var0.getClass().getMethod(В2ф).invoke(var0);
         if (var1 == null) {
            return false;
         }

         Object var2 = var1.getClass().getMethod(В2и).invoke(var1);
         return Boolean.TRUE.equals(var2);
      } catch (Throwable var3) {
         return false;
      }
   }

   public static String Ъ(int var0, String... var1) {
      if (!ЮЧ()) {
         return В2必;
      }

      if (var1 != null && var1.length != 0) {
         try {
            Object var2 = г();
            if (var2 == null) {
               return В2Г;
            }

            Object var3 = var2.getClass().getMethod(В2л).invoke(var2);
            if (var3 == null) {
               return В2э;
            }

            гЧ();

            try {
               var3.getClass().getMethod(В2ю, int.class, String[].class).invoke(var3, var0, var1);
               return null;
            } catch (NoSuchMethodException var8) {
               Class var4 = Class.forName(В2Щ);
               Object var5 = Array.newInstance(var4, 0);

               try {
                  var3.getClass().getMethod(В2у, int.class, var5.getClass()).invoke(var3, var0, Ъ(var1, var4));
                  return null;
               } catch (Throwable var7) {
                  var3.getClass().getMethod(В22, String[].class).invoke(var3, var1);
                  return null;
               }
            }
         } catch (Throwable var9) {
            return "Не удалось запустить добычу: " + var9.getMessage();
         }
      } else {
         return В2щ;
      }
   }

   public static void Ч(boolean var0) {
      try {
         Object var1 = г();
         if (var1 == null) {
            return;
         }

         Object var2 = var1.getClass().getMethod(ВуС).invoke(var1);
         Class var3 = Class.forName(Вуз, true, var2.getClass().getClassLoader());
         Enum var4 = Enum.valueOf(var3.asSubclass(Enum.class), Вуц);
         var2.getClass().getMethod(Вуя, var3, boolean.class).invoke(var2, var4, var0);
      } catch (Throwable var5) {
      }
   }

   public static void г0() {
      try {
         Object var0 = О();
         if (var0 == null) {
            return;
         }

         var0.getClass().getMethod(Ву西).invoke(var0);
      } catch (Throwable var1) {
      }
   }

   private BaritoneIntegration() {
   }

   public static void Ъ(Block var0, boolean var1) {
      try {
         Object var2 = Ъ(В2У);
         if (var2 == null) {
            return;
         }

         if (!(var2.getClass().getField(В2З).get(var2) instanceof List var4)) {
            return;
         }

         List var5 = var4;
         if (var1) {
            if (!var5.contains(var0)) {
               var5.add(var0);
            }
         } else {
            var5.remove(var0);
         }
      } catch (Throwable var6) {
      }
   }

   private static Object Ъ_7fd512f1(Object var0) {
      try {
         Field var8 = var0.getClass().getDeclaredField(В2O);
         var8.setAccessible(true);
         return var8.get(var0);
      } catch (Throwable var7) {
         for (Field var4 : var0.getClass().getDeclaredFields()) {
            try {
               var4.setAccessible(true);
               Object var5 = var4.get(var0);
               if (var5 != null && var5.getClass().getName().contains(В27)) {
                  return var5;
               }
            } catch (Throwable var6) {
            }
         }

         return null;
      }
   }

   public interface BuildSchematic {
      int 必();

      BlockState з(int var1, int var2, int var3, BlockState var4, List<BlockState> var5);

      int l();

      int 西();

      boolean 诶(int var1, int var2, int var3);

      default void ы衣() {
      }
   }
}
