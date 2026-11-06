package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import sg.ec.MinecraftAccess;
import sg.gui.ServerStatusParser;
import sg.render.pipeline.WardenFarmArea;

public final class TimedChestTracker implements MinecraftAccess {
   private static final Pattern Д = Pattern.compile(TimedChestTracker.ЯМ8);
   @SerializedName("4")
   private final List<BlockPos> field_4 = new ArrayList<>();
   private final List<TimedChestTracker.TimedChest> п = new ArrayList<>();
   private int Ц;
   private static final int ЯМИ = Integer.MIN_VALUE;
   private static final int ЯМь = Integer.MIN_VALUE;
   private static final long ЯМж = -1L;
   private static final long ЯМ弟 = -1L;
   private static final long ЯМб = -1L;
   private static final long ЯМЫ = 60L;
   private static final long ЯМl = 1000L;
   private static final String ЯМ8 = "(\\d{2}):(\\d{2})";

   public List<BlockPos> о_2571477d() {
      return this.field_4;
   }

   private void х_de5a6f78() {
      if (WardenFarmArea.Л(MINECRAFT_CLIENT.level)) {
         for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var2 instanceof ArmorStand var3) {
               long var4 = 诶(var3);
               if (var4 >= 0L) {
                  BlockPos var6 = this.诶_10b53497(var3.blockPosition());
                  if (var6 != null) {
                     TimedChestTracker.TimedChest var7 = this.诶_80db4183(var6);
                     if (var7 != null) {
                        var7.В(var4);
                     } else {
                        this.п.add(new TimedChestTracker.TimedChest(var6, var4));
                     }
                  }
               }
            }
         }

         this.п.removeIf(var0 -> var0.Ф() <= 0L);
      }
   }

   public TimedChestTracker() {
      this.Ц = ЯМИ;
   }

   private static long 诶(ArmorStand var0) {
      String var1 = var0.getCustomName() == null ? "" : var0.getCustomName().getString();
      Matcher var2 = Д.matcher(var1);
      return !var2.find() ? ЯМб : (Integer.parseInt(var2.group(1)) * ЯМЫ + Integer.parseInt(var2.group(2))) * ЯМl;
   }

   private void М() {
      this.field_4.clear();
      if (WardenFarmArea.Л(MINECRAFT_CLIENT.level)) {
         short var1 = -130;
         byte var2 = -121;
         short var3 = -130;
         byte var4 = -121;

         for (int var5 = var1; var5 <= var2; var5++) {
            for (int var6 = var3; var6 <= var4; var6++) {
               if (MINECRAFT_CLIENT.level.hasChunk(var5, var6)) {
                  LevelChunk var7 = (LevelChunk)MINECRAFT_CLIENT.level.getChunk(var5, var6, ChunkStatus.FULL, false);
                  if (var7 != null) {
                     for (BlockEntity var9 : var7.getBlockEntities().values()) {
                        BlockPos var10 = var9.getBlockPos();
                        if (WardenFarmArea.о(var10)) {
                           BlockEntityType var11 = var9.getType();
                           if (var11 == BlockEntityTypes.CHEST || var11 == BlockEntityTypes.TRAPPED_CHEST) {
                              this.field_4.add(var10.immutable());
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void щ() {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.field_4.clear();
         this.п.clear();
         this.Ц = ЯМь;
      } else if (MINECRAFT_CLIENT.player.tickCount != this.Ц) {
         this.Ц = MINECRAFT_CLIENT.player.tickCount;
         this.М();
         this.х_de5a6f78();
      }
   }

   public TimedChestTracker.TimedChest 诶_80db4183(BlockPos var1) {
      int var2 = ServerStatusParser.method_6_a83dd0cc();

      for (TimedChestTracker.TimedChest var4 : this.п) {
         if (var4.field_4.equals(var1) && var4.Х西 == var2) {
            return var4;
         }
      }

      return null;
   }

   private BlockPos 诶_10b53497(BlockPos var1) {
      for (BlockPos var3 : this.field_4) {
         if (var1.getX() == var3.getX() && var1.getZ() == var3.getZ()) {
            return var3;
         }
      }

      return null;
   }

   public long 诶_6da43b94(BlockPos var1) {
      if (var1 != null && MINECRAFT_CLIENT.level != null) {
         for (Entity var3 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var3 instanceof ArmorStand var4 && var4.blockPosition().getX() == var1.getX() && var4.blockPosition().getZ() == var1.getZ()) {
               long var5 = 诶(var4);
               if (var5 >= 0L) {
                  return var5;
               }
            }
         }

         TimedChestTracker.TimedChest var7 = this.诶_80db4183(var1);
         return var7 == null ? ЯМ弟 : var7.Ф();
      } else {
         return ЯМж;
      }
   }

   public static final class TimedChest {
      @SerializedName("4")
      private final BlockPos field_4;
      private long Вб;
      private long ВЫ;
      private int Х西;
      private static final long В7п = 1000L;
      private static final long В7Д = 1000L;
      private static final long В7Ъ = 5L;

      private void В(long var1) {
         if (Math.abs(var1 / В7п - this.Ф() / В7Д) > В7Ъ) {
            this.Вб = var1;
            this.ВЫ = System.currentTimeMillis();
            this.Х西 = ServerStatusParser.method_6_a83dd0cc();
         }
      }

      public long Ф() {
         return Math.max(0L, this.Вб - (System.currentTimeMillis() - this.ВЫ));
      }

      private TimedChest(BlockPos var1, long var2) {
         this.field_4 = var1.immutable();
         this.Вб = var2;
         this.ВЫ = System.currentTimeMillis();
         this.Х西 = ServerStatusParser.method_6_a83dd0cc();
      }
   }
}
