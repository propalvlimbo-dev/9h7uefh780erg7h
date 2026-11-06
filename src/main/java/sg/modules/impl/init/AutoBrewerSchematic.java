package sg.modules.impl.init;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import sg.ec.BaritoneIntegration;

public final class AutoBrewerSchematic implements BaritoneIntegration.BuildSchematic {
   private static final Map<String, AutoBrewerSchematic.SchematicData> ч_ea411899 = new HashMap<>();
   @SerializedName("В")
   private final BlockState[][][] В_d7b04778;
   private final int ВЮ;
   private final int ВЧ;
   private final int ВЛ;
   private final BlockPos Я;
   @SerializedName("В")
   private final Direction В_10be190f;
   private final Map<Item, Integer> Э;
   private final List<BlockPos> М;
   private final List<BlockPos> Н;
   private final List<BlockPos> н;
   private final List<BlockPos> Ц;
   @SerializedName("ч")
   private final List<BlockPos> ч_2571477d;
   @SerializedName("В")
   private final AutoBrewerSchematic.SchematicKind В_7381ae69;
   private static final int ХЬУ = Integer.MAX_VALUE;
   private static final int ХЬЗ = Integer.MAX_VALUE;
   private static final int ХЬЕ = Integer.MAX_VALUE;
   private static final int ХЬП = Integer.MIN_VALUE;
   private static final int ХЬ西 = Integer.MIN_VALUE;
   private static final int ХЬд = Integer.MIN_VALUE;
   private static final String ХЬБ = ", ";
   private static final String ХЬС = "none";
   private static final String ХЬз = "blocks";
   private static final String ХЬц = "props";
   private static final String ХЬя = "props";
   private static final String Х衣с = "props";
   private static final String Х衣В = "x";
   private static final String Х衣Я = "y";
   private static final String Х衣Х = "z";
   private static final String Х衣е = "block";

   public List<BlockPos> и() {
      return this.Ц;
   }

   private static int з(Direction var0) {
      return Math.floorMod(var0.get2DDataValue() - Direction.SOUTH.get2DDataValue(), 4);
   }

   private static Item з_50cb85c5(BlockState var0) {
      Block var1 = var0.getBlock();
      if (var1 == Blocks.REDSTONE_WALL_TORCH || var1 == Blocks.REDSTONE_TORCH) {
         return Items.REDSTONE_TORCH;
      }

      if (var1 == Blocks.REDSTONE_WIRE) {
         return Items.REDSTONE;
      }

      Item var2 = var1.asItem();
      return var2 == null ? Items.AIR : var2;
   }

   @Override
   public BlockState з(int var1, int var2, int var3, BlockState var4, List<BlockState> var5) {
      if (var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.ВЮ && var2 < this.ВЧ && var3 < this.ВЛ) {
         BlockState var6 = this.В_d7b04778[var1][var2][var3];
         if (var6 == null || Б(var6)) {
            return var4 != null ? var4 : Blocks.AIR.defaultBlockState();
         }

         if (ы(var6)) {
            if (var4 != null && ы(var4)) {
               return var4;
            }

            if (var5 != null) {
               for (BlockState var13 : var5) {
                  if (var13 != null && ы(var13)) {
                     return var13;
                  }
               }
            }

            return var6;
         } else {
            if (var4 != null && var4.getBlock() == var6.getBlock()) {
               Block var7 = var6.getBlock();
               if (var7 == Blocks.REDSTONE_WIRE || var7 == Blocks.REPEATER || var7 == Blocks.LEVER) {
                  return var4;
               }

               if (var4.equals(var6)) {
                  return var4;
               }
            }

            if (var5 != null) {
               for (BlockState var8 : var5) {
                  if (var8 != null && var8.getBlock() == var6.getBlock() && р(var6, var8)) {
                     return var8;
                  }
               }

               for (BlockState var12 : var5) {
                  if (var12 != null && var12.getBlock() == var6.getBlock()) {
                     return var12;
                  }
               }
            }

            return var6;
         }
      } else {
         return var4 != null ? var4 : Blocks.AIR.defaultBlockState();
      }
   }

   public String Ю2() {
      ArrayList var1 = new ArrayList();
      this.Э
         .entrySet()
         .stream()
         .sorted(
            (var0, var1x) -> var0.getKey()
               .getDefaultInstance()
               .getItemName()
               .getString()
               .compareToIgnoreCase(var1x.getKey().getDefaultInstance().getItemName().getString())
         )
         .forEach(var1x -> var1.add(var1x.getKey().getDefaultInstance().getItemName().getString() + " x" + var1x.getValue()));
      return String.join(ХЬБ, var1);
   }

   private static Direction з(Direction var0, int var1) {
      if (var0.getAxis() == Axis.Y) {
         return var0;
      }

      Direction var2 = var0;

      for (int var3 = 0; var3 < Math.floorMod(var1, 4); var3++) {
         var2 = var2.getClockWise();
      }

      return var2;
   }

   private AutoBrewerSchematic(
      AutoBrewerSchematic.SchematicKind var1,
      BlockState[][][] var2,
      int var3,
      int var4,
      int var5,
      BlockPos var6,
      Direction var7,
      Map<Item, Integer> var8,
      List<BlockPos> var9,
      List<BlockPos> var10,
      List<BlockPos> var11,
      List<BlockPos> var12,
      List<BlockPos> var13
   ) {
      this.В_7381ae69 = var1;
      this.В_d7b04778 = var2;
      this.ВЮ = var3;
      this.ВЧ = var4;
      this.ВЛ = var5;
      this.Я = var6;
      this.В_10be190f = var7;
      this.Э = var8;
      this.М = var9;
      this.Н = var10;
      this.н = var11;
      this.Ц = var12;
      this.ч_2571477d = var13;
   }

   @Override
   public int 必() {
      return this.ВЛ;
   }

   public static boolean ы(BlockState var0) {
      return var0.is(Blocks.REDSTONE_TORCH) || var0.is(Blocks.REDSTONE_WALL_TORCH);
   }

   private static BlockState з_13788996(BlockState var0) {
      BlockState var1 = var0;
      if (var1.hasProperty(RedStoneWireBlock.POWER)) {
         var1 = var1.setValue(RedStoneWireBlock.POWER, 0);
      }

      if (var1.hasProperty(RepeaterBlock.POWERED)) {
         var1 = var1.setValue(RepeaterBlock.POWERED, false);
      }

      if (var1.hasProperty(RepeaterBlock.LOCKED)) {
         var1 = var1.setValue(RepeaterBlock.LOCKED, false);
      }

      if (var1.hasProperty(LeverBlock.POWERED)) {
         var1 = var1.setValue(LeverBlock.POWERED, false);
      }

      if (var1.hasProperty(HopperBlock.ENABLED)) {
         var1 = var1.setValue(HopperBlock.ENABLED, true);
      }

      if (var1.hasProperty(RedstoneTorchBlock.LIT)) {
         var1 = var1.setValue(RedstoneTorchBlock.LIT, true);
      }

      if (var1.hasProperty(BrewingStandBlock.HAS_BOTTLE[0])) {
         var1 = var1.setValue(BrewingStandBlock.HAS_BOTTLE[0], false)
            .setValue(BrewingStandBlock.HAS_BOTTLE[1], false)
            .setValue(BrewingStandBlock.HAS_BOTTLE[2], false);
      }

      return var1;
   }

   public BlockPos з_10b53497() {
      return this.Я;
   }

   private static BlockState з(String var0, Map<String, String> var1) {
      Identifier var2 = Identifier.tryParse(var0);
      if (var2 == null) {
         return Blocks.AIR.defaultBlockState();
      }

      Block var3 = BuiltInRegistries.BLOCK.getValue(var2);
      BlockState var4 = var3.defaultBlockState();
      if (var1 != null && !var1.isEmpty()) {
         for (Entry var6 : var1.entrySet()) {
            Property var7 = var3.getStateDefinition().getProperty((String)var6.getKey());
            if (var7 != null) {
               Optional var8 = var7.getValue((String)var6.getValue());
               if (var8.isPresent()) {
                  var4 = var4.setValue(var7, (Comparable)var8.get());
               }
            }
         }

         return з_13788996(var4);
      } else {
         return з_13788996(var4);
      }
   }

   @Override
   public boolean 诶(int var1, int var2, int var3) {
      if (var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.ВЮ && var2 < this.ВЧ && var3 < this.ВЛ) {
         BlockState var4 = this.В_d7b04778[var1][var2][var3];
         return var4 == null ? false : !Б(var4);
      } else {
         return false;
      }
   }

   private static String з(BlockState var0, Direction var1) {
      return switch (var1) {
         case NORTH -> var0.getValue(RedStoneWireBlock.NORTH).getSerializedName();
         case EAST -> var0.getValue(RedStoneWireBlock.EAST).getSerializedName();
         case SOUTH -> var0.getValue(RedStoneWireBlock.SOUTH).getSerializedName();
         case WEST -> var0.getValue(RedStoneWireBlock.WEST).getSerializedName();
         default -> ХЬС;
      };
   }

   public List<BlockPos> с() {
      return this.ч_2571477d;
   }

   public Map<Item, Integer> Б() {
      return this.Э;
   }

   public List<BlockPos> Я() {
      return this.М;
   }

   private static AutoBrewerSchematic.SchematicData з(String var0) {
      AutoBrewerSchematic.SchematicData var1 = ч_ea411899.get(var0);
      if (var1 != null) {
         return var1;
      }

      String var2 = "assets/rose/schematics/autobrewers/" + var0 + ".json";

      try (InputStream var3 = AutoBrewerSchematic.class.getClassLoader().getResourceAsStream(var2)) {
         if (var3 == null) {
            throw new IllegalStateException("Схема не найдена: " + var2);
         }

         JsonObject var4 = JsonParser.parseReader(new InputStreamReader(var3, StandardCharsets.UTF_8)).getAsJsonObject();
         JsonArray var5 = var4.getAsJsonArray(ХЬз);
         ArrayList var6 = new ArrayList(var5.size());

         for (JsonElement var8 : var5) {
            JsonObject var9 = var8.getAsJsonObject();
            HashMap var10 = new HashMap();
            if (var9.has(ХЬц) && var9.get(ХЬя).isJsonObject()) {
               for (Entry var12 : var9.getAsJsonObject(Х衣с).entrySet()) {
                  var10.put((String)var12.getKey(), ((JsonElement)var12.getValue()).getAsString());
               }
            }

            var6.add(
               new AutoBrewerSchematic.BlockEntry(var9.get(Х衣В).getAsInt(), var9.get(Х衣Я).getAsInt(), var9.get(Х衣Х).getAsInt(), var9.get(Х衣е).getAsString(), var10)
            );
         }

         AutoBrewerSchematic.SchematicData var17 = new AutoBrewerSchematic.SchematicData(var6);
         ч_ea411899.put(var0, var17);
         return var17;
      } catch (IllegalStateException var15) {
         throw var15;
      } catch (Exception var16) {
         throw new IllegalStateException("Не удалось загрузить схему " + var0 + ": " + var16.getMessage(), var16);
      }
   }

   private static BlockState з(BlockState var0, Direction var1, String var2) {
      EnumProperty var3 = switch (var1) {
         case NORTH -> RedStoneWireBlock.NORTH;
         case EAST -> RedStoneWireBlock.EAST;
         case SOUTH -> RedStoneWireBlock.SOUTH;
         case WEST -> RedStoneWireBlock.WEST;
         default -> null;
      };
      if (var3 == null) {
         return var0;
      }

      Optional var4 = var3.getValue(var2);
      return var4.isPresent() ? var0.setValue(var3, (Comparable)var4.get()) : var0;
   }

   public List<BlockPos> У() {
      return this.Н;
   }

   public static boolean Б(BlockState var0) {
      return var0.is(Blocks.CHEST) || var0.is(Blocks.HOPPER) || var0.is(Blocks.BREWING_STAND);
   }

   @Override
   public int 西() {
      return this.ВЮ;
   }

   public BlockState з(BlockPos var1) {
      int var2 = var1.getX() - this.Я.getX();
      int var3 = var1.getY() - this.Я.getY();
      int var4 = var1.getZ() - this.Я.getZ();
      return var2 >= 0 && var3 >= 0 && var4 >= 0 && var2 < this.ВЮ && var3 < this.ВЧ && var4 < this.ВЛ ? this.В_d7b04778[var2][var3][var4] : null;
   }

   private static int[] з(int var0, int var1, int var2) {
      return switch (Math.floorMod(var2, 4)) {
         case 1 -> new int[]{var1, -var0};
         case 2 -> new int[]{-var0, -var1};
         case 3 -> new int[]{-var1, var0};
         default -> new int[]{var0, var1};
      };
   }

   @Override
   public int l() {
      return this.ВЧ;
   }

   public AutoBrewerSchematic.SchematicKind з_7381ae69() {
      return this.В_7381ae69;
   }

   public static AutoBrewerSchematic з(AutoBrewerSchematic.SchematicKind var0, BlockPos var1, Direction var2) {
      AutoBrewerSchematic.SchematicData var3 = з(var0.Д);
      int var4 = з(var2);
      LinkedHashMap<BlockPos, BlockState> var5 = new LinkedHashMap<>();
      int var6 = ХЬУ;
      int var7 = ХЬЗ;
      int var8 = ХЬЕ;
      int var9 = ХЬП;
      int var10 = ХЬ西;
      int var11 = ХЬд;

      for (AutoBrewerSchematic.BlockEntry var13 : var3.blocks) {
         int[] var14 = з(var13.x, var13.z, var4);
         BlockPos var15 = new BlockPos(var14[0], var13.y, var14[1]);
         BlockState var16 = з(з(var13.block, var13.props), var4);
         var5.put(var15, var16);
         var6 = Math.min(var6, var15.getX());
         var7 = Math.min(var7, var15.getY());
         var8 = Math.min(var8, var15.getZ());
         var9 = Math.max(var9, var15.getX());
         var10 = Math.max(var10, var15.getY());
         var11 = Math.max(var11, var15.getZ());
      }

      int var29 = var9 - var6 + 1;
      int var30 = var10 - var7 + 1;
      int var31 = var11 - var8 + 1;
      BlockState[][][] var32 = new BlockState[var29][var30][var31];
      ArrayList var33 = new ArrayList(var5.size());
      ArrayList var17 = new ArrayList();
      ArrayList var18 = new ArrayList();
      ArrayList var19 = new ArrayList();
      ArrayList var20 = new ArrayList();
      HashMap<Item, Integer> var21 = new HashMap<>();
      BlockPos var22 = var1;

      for (Entry var24 : var5.entrySet()) {
         BlockPos var25 = ((BlockPos)var24.getKey()).offset(-var6, -var7, -var8);
         BlockState var26 = (BlockState)var24.getValue();
         var32[var25.getX()][var25.getY()][var25.getZ()] = var26;
         BlockPos var27 = var22.offset(var25.getX(), var25.getY(), var25.getZ());
         var33.add(var27);
         if (var26.is(Blocks.CHEST)) {
            var17.add(var27);
         }

         if (ы(var26)) {
            var18.add(var27);
         }

         if (var26.is(Blocks.HOPPER)) {
            var19.add(var27);
         }

         if (var26.is(Blocks.BREWING_STAND)) {
            var20.add(var27);
         }

         Item var28 = з_50cb85c5(var26);
         if (var28 != Items.AIR) {
            var21.merge(var28, 1, Integer::sum);
         }
      }

      return new AutoBrewerSchematic(var0, var32, var29, var30, var31, var22, var2, var21, var33, var17, var18, var19, var20);
   }

   public Direction з_10be190f() {
      return this.В_10be190f;
   }

   private static BlockState з(BlockState var0, int var1) {
      if (var1 != 0 && var0 != null) {
         BlockState var2 = var0;
         if (var2.hasProperty(HopperBlock.FACING)) {
            var2 = var2.setValue(HopperBlock.FACING, з(var2.getValue(HopperBlock.FACING), var1));
         }

         if (var2.hasProperty(HorizontalDirectionalBlock.FACING)) {
            var2 = var2.setValue(HorizontalDirectionalBlock.FACING, з(var2.getValue(HorizontalDirectionalBlock.FACING), var1));
         }

         if (var2.hasProperty(RedstoneWallTorchBlock.FACING)) {
            var2 = var2.setValue(RedstoneWallTorchBlock.FACING, з(var2.getValue(RedstoneWallTorchBlock.FACING), var1));
         }

         if (var2.hasProperty(LeverBlock.FACING)) {
            var2 = var2.setValue(LeverBlock.FACING, з(var2.getValue(LeverBlock.FACING), var1));
         }

         if (var2.is(Blocks.REDSTONE_WIRE)) {
            String var3 = з(var2, Direction.NORTH);
            String var4 = з(var2, Direction.EAST);
            String var5 = з(var2, Direction.SOUTH);
            String var6 = з(var2, Direction.WEST);
            String[] var7 = new String[4];
            String[] var8 = new String[]{var3, var4, var5, var6};

            for (int var9 = 0; var9 < 4; var9++) {
               var7[(var9 + var1) % 4] = var8[var9];
            }

            var2 = з(var2, Direction.NORTH, var7[0]);
            var2 = з(var2, Direction.EAST, var7[1]);
            var2 = з(var2, Direction.SOUTH, var7[2]);
            var2 = з(var2, Direction.WEST, var7[3]);
         }

         return var2;
      } else {
         return var0;
      }
   }

   private static boolean р(BlockState var0, BlockState var1) {
      if (var0.hasProperty(HopperBlock.FACING) && var1.hasProperty(HopperBlock.FACING)) {
         return var0.getValue(HopperBlock.FACING) == var1.getValue(HopperBlock.FACING);
      } else if (var0.hasProperty(HorizontalDirectionalBlock.FACING) && var1.hasProperty(HorizontalDirectionalBlock.FACING)) {
         return var0.getValue(HorizontalDirectionalBlock.FACING) == var1.getValue(HorizontalDirectionalBlock.FACING);
      } else {
         return var0.hasProperty(RedstoneWallTorchBlock.FACING) && var1.hasProperty(RedstoneWallTorchBlock.FACING)
            ? var0.getValue(RedstoneWallTorchBlock.FACING) == var1.getValue(RedstoneWallTorchBlock.FACING)
            : true;
      }
   }

   public List<BlockPos> method_6() {
      return this.н;
   }

   private record SchematicData(List<AutoBrewerSchematic.BlockEntry> blocks) {
   }

   public enum SchematicKind {
      OBYCHNAYA_PITEVYE("obychnaya_pitevye", "Обычная питьевые");

      public final String Д;
      public final String Ъ;

      SchematicKind(String var3, String var4) {
         this.Д = var3;
         this.Ъ = var4;
      }

      public static AutoBrewerSchematic.SchematicKind Л(String var0) {
         return OBYCHNAYA_PITEVYE;
      }
   }

   private record BlockEntry(int x, int y, int z, String block, Map<String, String> props) {
   }
}
