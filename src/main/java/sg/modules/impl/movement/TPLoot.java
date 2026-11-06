package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class TPLoot extends ClientModule {
   private static final Set<Item> е_2f23a69a_at_847356b8 = Set.of(
      Items.TOTEM_OF_UNDYING,
      Items.CRYING_OBSIDIAN,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.GOLDEN_APPLE,
      Items.ELYTRA,
      Items.NAME_TAG,
      Items.NETHERITE_INGOT,
      Items.NETHERITE_CHESTPLATE,
      Items.NETHERITE_HELMET,
      Items.NETHERITE_BOOTS,
      Items.NETHERITE_LEGGINGS,
      Items.NETHERITE_SWORD,
      Items.PLAYER_HEAD
   );
   private static final double н_3f39d5c3 = 65.0;
   private static final double Ц_3f39d5c3 = 0.001;
   private static final double ч = 0.25;
   private static final double Э = 0.29;
   private static final double 必 = 1.8;
   private static final long м = 250000000L;
   private static final long К = 450000000L;
   private static final long I = 900000000L;
   private static final int П = 10;
   private static final int 西 = 10;
   private static final int д = 1;
   private static final int Б = 1;
   private static final int С = 2;
   private static final int з = 8;
   private static final int ц = 40;
   private static final int я = 100;
   @SerializedName("п")
   private final SingleChoiceSetting п_at_847356b8;
   @SerializedName("н")
   private final FloatSetting н_989fed9a;
   @SerializedName("Ц")
   private final FloatSetting Ц_989fed9a;
   private final EventListener<PlayerTickEvent> Н;
   @SerializedName("е")
   private final Stopwatch е_fed7dec4_at_847356b8;
   @SerializedName("А")
   private final Set<Integer> А_at_847356b8;
   private TPLoot.RouteStage В;
   private Vec3 Д;
   private Vec3 Ъ;
   private boolean и;
   @SerializedName("9")
   private boolean field_9;
   private int сс;
   private int сВ;
   private int сЯ;
   private long Т;
   private long О;
   private static final String с2Ч = "TPLoot";
   private static final String с2Л = "Телепортируется к ценным предметам";
   private static final String с2诶 = "Возврат";
   private static final String с21 = "Назад";
   private static final String с2Ь = "/home";
   private static final String с2衣 = "Никуда";
   private static final String с2ш = "Задержка /home";
   private static final float с2И = 100.0F;
   private static final float с2ь = 50.0F;
   private static final float с2ж = 200.0F;
   private static final float с2弟 = 10.0F;
   private static final String с2б = "Радиус";
   private static final float с2Ы = 100.0F;
   private static final float с2l = 10.0F;
   private static final float с28 = 150.0F;
   private static final float с25 = 5.0F;
   private static final String с2в = "Назад";
   private static final long с2ъ = 900000000L;
   private static final String с2М = "/home";
   private static final long с2Н = 450000000L;
   private static final String с2н = "/home";
   private static final String с2Ц = "home home";
   private static final long с2ч = 450000000L;
   private static final double с2Э = -Double.MAX_VALUE;
   private static final double с2必 = 10000.0;
   private static final double с2щ = 0.5;
   private static final double с2Г = 0.5;
   private static final double с2л = 65.0;
   private static final long с2э = 250000000L;
   private static final long с2ю = 250000000L;
   private static final double с2Щ = 65.0;
   private static final double с2у = Double.MAX_VALUE;
   private static final double с22 = 0.5;
   private static final double с2ф = 0.5;
   private static final double с2и = 0.5;
   private static final double с29 = 0.5;
   private static final double с2о = 0.001;
   private static final double с2O = 65.0;
   private static final double с27 = 0.001;
   private static final double с2Р = Double.MAX_VALUE;
   private static final double с23 = 0.5;
   private static final double с2р = 0.5;
   private static final double с26 = 65.001;
   private static final double с2_ = 0.25;
   private static final double с2Ш = 0.29;
   private static final double с2ы = 0.29;
   private static final double с2й = 0.29;
   private static final double с2Ж = 1.8;
   private static final double с2м = 0.29;
   private static final double с2К = 0.16;
   private static final String с2I = "/home";

   @Override
   public void onEnable() {
      this.西0();
   }

   private void 西г() {
      if (this.Д != null) {
         TPLoot.StepResult var1 = this.р(this.Д, с2ч);
         if (var1 == TPLoot.StepResult.REACHED) {
            this.Д = null;
            this.西к();
         } else if (var1 == TPLoot.StepResult.MOVED) {
            this.сЯ = 0;
         } else {
            if (var1 == TPLoot.StepResult.BLOCKED && ++this.сЯ >= 100) {
               this.Д = null;
               this.西к();
            }
         }
      }
   }

   private boolean р_bbeebd87(TPLoot.Destination var1) {
      double var2 = MINECRAFT_CLIENT.player.getX() - var1.x();
      double var4 = MINECRAFT_CLIENT.player.getY() - var1.feetY();
      double var6 = MINECRAFT_CLIENT.player.getZ() - var1.z();
      return var2 * var2 + var4 * var4 + var6 * var6 <= с2К;
   }

   private void и(ItemEntity var1) {
      this.А_at_847356b8.add(var1.getId());
      this.西д();
   }

   private TPLoot.Destination р_31266291(double var1, double var3) {
      double var5 = MINECRAFT_CLIENT.player.getX();
      double var7 = MINECRAFT_CLIENT.player.getZ();
      double var9 = var1 - var5;
      double var11 = var3 - var7;
      double var13 = Math.hypot(var9, var11);
      if (var13 <= с2о) {
         return null;
      }

      for (double var15 = Math.min(var13, с2O); var15 > с27; var15 -= Math.min(1.0, var15)) {
         double var17 = var15 / var13;
         double var19 = var5 + var9 * var17;
         double var21 = var7 + var11 * var17;
         TPLoot.Destination var23 = this.р(var5, var7, var19, var21, var1, var3);
         if (var23 != null) {
            return var23;
         }
      }

      return null;
   }

   private TPLoot.StepResult р_bb996d52(ItemEntity var1) {
      double var2 = Math.floor(var1.getX()) + с2щ;
      double var4 = Math.floor(var1.getZ()) + с2Г;
      double var6 = this.и(var2, var4);
      if (var6 > с2л) {
         if (this.в(с2э)) {
            return TPLoot.StepResult.WAITING;
         }

         TPLoot.Destination var9 = this.р_31266291(var2, var4);
         if (var9 == null) {
            return TPLoot.StepResult.BLOCKED;
         }

         this.р_de5a6f78(var9);
         return TPLoot.StepResult.MOVED;
      } else {
         TPLoot.Destination var8 = this.р_31266291(var1.getX(), var1.getY(), var1.getZ());
         if (var8 == null) {
            return TPLoot.StepResult.BLOCKED;
         }

         if (this.р_bbeebd87(var8)) {
            return TPLoot.StepResult.REACHED;
         }

         if (this.в(с2ю)) {
            return TPLoot.StepResult.WAITING;
         }

         this.р_de5a6f78(var8);
         return TPLoot.StepResult.MOVED;
      }
   }

   private void 西9() {
      if (this.Ъ == null) {
         this.西к();
      } else if (System.nanoTime() >= this.О) {
         TPLoot.StepResult var1 = this.р(this.Ъ, с2Н);
         if (var1 == TPLoot.StepResult.REACHED) {
            this.西к();
         } else if (var1 == TPLoot.StepResult.MOVED) {
            this.сЯ = 0;
         } else {
            if (var1 == TPLoot.StepResult.BLOCKED && ++this.сЯ >= 100) {
               this.西к();
            }
         }
      }
   }

   private void 西1() {
      ItemEntity var1 = this.р();
      if (var1 == null) {
         this.西д();
         if (this.и) {
            this.西ч();
         } else {
            this.Ъ = null;
         }
      } else {
         if (this.Ъ == null) {
            this.Ъ = MINECRAFT_CLIENT.player.position();
         }

         this.и = true;
         this.р_de5a6f78(var1);
         switch (this.р_bb996d52(var1)) {
            case WAITING:
            default:
               break;
            case MOVED:
               this.сЯ = 0;
               this.сВ = 0;
               break;
            case REACHED:
               this.сЯ = 0;
               this.сВ++;
               if (this.сВ >= 40) {
                  this.и(var1);
               }
               break;
            case BLOCKED:
               this.сЯ++;
               this.сВ = 0;
               if (this.сЯ >= 100) {
                  this.и(var1);
               }
         }
      }
   }

   private void 西Ч() {
      if (!this.п_at_847356b8.isSelected(с2н)) {
         this.西ч();
      } else if (this.е_fed7dec4_at_847356b8.method_0(this.н_989fed9a.getValue().longValue())) {
         if (MINECRAFT_CLIENT.getConnection() != null) {
            MINECRAFT_CLIENT.getConnection().sendCommand(с2Ц);
         }

         this.西к();
      }
   }

   private boolean р_bbeebd87(ItemEntity var1) {
      return var1.isAlive()
         && !var1.getItem().isEmpty()
         && !this.А_at_847356b8.contains(var1.getId())
         && е_2f23a69a_at_847356b8.contains(var1.getItem().getItem());
   }

   private boolean р_bbeebd87(double var1, double var3, double var5) {
      AABB var7 = new AABB(var1 - с2Ш, var3, var5 - с2ы, var1 + с2й, var3 + с2Ж, var5 + с2м);
      return MINECRAFT_CLIENT.level.noCollision(MINECRAFT_CLIENT.player, var7);
   }

   private void А(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.player.getHealth() <= 0.0F) {
            if (this.Д == null) {
               this.Д = MINECRAFT_CLIENT.player.position();
            }

            this.西к();
         } else if (this.Д != null) {
            this.西г();
         } else {
            switch (this.В) {
               case SEARCH:
                  this.西1();
                  break;
               case RETURN:
                  this.西9();
                  break;
               case HOME:
                  this.西Ч();
            }
         }
      }
   }

   private void р(double var1, double var3, double var5, int var7) {
      boolean var8 = MINECRAFT_CLIENT.player.onGround();
      boolean var9 = MINECRAFT_CLIENT.player.horizontalCollision;

      for (int var10 = 0; var10 < var7; var10++) {
         MINECRAFT_CLIENT.player.connection.send(new Pos(var1, var3, var5, var8, var9));
      }
   }

   private ItemEntity р() {
      double var1 = this.Ц_989fed9a.getValue().floatValue();
      double var3 = var1 * var1;
      ItemEntity var5 = null;
      double var6 = с2Э;

      for (Entity var9 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var9 instanceof ItemEntity var10 && this.р_bbeebd87(var10)) {
            double var11 = MINECRAFT_CLIENT.player.distanceToSqr(var10);
            if (!(var11 > var3)) {
               double var13 = this.Ю(var10.getItem().getItem()) * с2必 - var11;
               if (!(var13 <= var6)) {
                  var6 = var13;
                  var5 = var10;
               }
            }
         }
      }

      return var5;
   }

   private boolean р(int var1, int var2, int var3) {
      BlockPos var4 = new BlockPos(var1, var2, var3);
      BlockState var5 = MINECRAFT_CLIENT.level.getBlockState(var4);
      return var5.getCollisionShape(MINECRAFT_CLIENT.level, var4).isEmpty() ? false : this.р_bbeebd87(var1 + с2и, var2 + 1.0, var3 + с29);
   }

   private TPLoot.Destination р_31266291(double var1, double var3, double var5) {
      int var7 = Mth.floor(var1);
      int var8 = Mth.floor(var3) - 1;
      int var9 = Mth.floor(var5);
      TPLoot.Destination var10 = null;
      double var11 = с2у;

      for (int var13 = -1; var13 <= 1; var13++) {
         for (int var14 = -1; var14 <= 1; var14++) {
            int var15 = var7 + var13;
            int var16 = var9 + var14;
            double var17 = var15 + с22;
            double var19 = var16 + с2ф;
            if (this.р_bbeebd87(var17, var19)) {
               for (int var21 = 2; var21 >= -8; var21--) {
                  int var22 = var8 + var21;
                  double var23 = var22 + 1.0;
                  if (this.р(var15, var22, var16)) {
                     double var25 = var17 - var1;
                     double var27 = var23 - var3;
                     double var29 = var19 - var5;
                     double var31 = var25 * var25 + var27 * var27 + var29 * var29;
                     if (!(var31 >= var11)) {
                        var11 = var31;
                        var10 = new TPLoot.Destination(var17, var23, var19);
                     }
                  }
               }
            }
         }
      }

      return var10;
   }

   private TPLoot.StepResult р(Vec3 var1, long var2) {
      double var4 = var1.x;
      double var6 = var1.z;
      double var8 = this.и(var4, var6);
      if (var8 > с2Щ) {
         if (this.в(var2)) {
            return TPLoot.StepResult.WAITING;
         }

         TPLoot.Destination var11 = this.р_31266291(var4, var6);
         if (var11 == null) {
            return TPLoot.StepResult.BLOCKED;
         }

         this.р_de5a6f78(var11);
         return TPLoot.StepResult.MOVED;
      } else {
         if (!this.р_bbeebd87(var4, var6)) {
            return TPLoot.StepResult.BLOCKED;
         }

         TPLoot.Destination var10 = this.р(var1);
         if (var10 == null) {
            return TPLoot.StepResult.BLOCKED;
         }

         if (this.р_bbeebd87(var10)) {
            return TPLoot.StepResult.REACHED;
         }

         if (this.в(var2)) {
            return TPLoot.StepResult.WAITING;
         }

         this.р_de5a6f78(var10);
         return TPLoot.StepResult.MOVED;
      }
   }

   private void 西ч() {
      this.西д();
      if (this.п_at_847356b8.isSelected(с2в) && this.Ъ != null) {
         this.В = TPLoot.RouteStage.RETURN;
         this.сЯ = 0;
         this.О = System.nanoTime() + с2ъ;
      } else if (this.п_at_847356b8.isSelected(с2М)) {
         this.В = TPLoot.RouteStage.HOME;
         this.е_fed7dec4_at_847356b8.ВД();
      } else {
         this.西к();
      }
   }

   private void 西0() {
      this.Д = null;
      this.西к();
   }

   private double и(double var1, double var3) {
      return Math.hypot(var1 - MINECRAFT_CLIENT.player.getX(), var3 - MINECRAFT_CLIENT.player.getZ());
   }

   @Override
   public void onDisable() {
      this.西0();
      super.onDisable();
   }

   private void р_de5a6f78(TPLoot.Destination var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.connection != null) {
         Vec3 var2 = MINECRAFT_CLIENT.player.position();
         this.р(var2.x, var2.y, var2.z, 10);
         MINECRAFT_CLIENT.player.setDeltaMovement(Vec3.ZERO);
         this.р(var1.x(), var1.feetY(), var1.z(), 10);
         MINECRAFT_CLIENT.player.setPos(var1.x(), var1.feetY(), var1.z());
         this.field_9 = true;
         this.Т = System.nanoTime();
      }
   }

   private Integer р(int var1, int var2) {
      int var3 = Math.min(MINECRAFT_CLIENT.level.getHeight(Types.WORLD_SURFACE, var1, var2) - 1, MINECRAFT_CLIENT.level.getMaxY());

      for (int var4 = var3; var4 >= MINECRAFT_CLIENT.level.getMinY(); var4--) {
         BlockPos var5 = new BlockPos(var1, var4, var2);
         if (!MINECRAFT_CLIENT.level.getBlockState(var5).getCollisionShape(MINECRAFT_CLIENT.level, var5).isEmpty()) {
            return var4;
         }
      }

      return null;
   }

   public TPLoot() {
      super(с2Ч, с2Л, ModuleCategory.MOVEMENT);
      this.п_at_847356b8 = new SingleChoiceSetting(с2诶, new ChoiceOption(с21), new ChoiceOption(с2Ь), new ChoiceOption(с2衣));
      this.н_989fed9a = new FloatSetting(с2ш, с2И, с2ь, с2ж).withStep(с2弟).visibleWhen(() -> this.п_at_847356b8.isSelected(с2I));
      this.Ц_989fed9a = new FloatSetting(с2б, с2Ы, с2l, с28).withStep(с25);
      this.Н = this::А;
      this.е_fed7dec4_at_847356b8 = new Stopwatch();
      this.А_at_847356b8 = new HashSet<>();
      this.В = TPLoot.RouteStage.SEARCH;
      this.сс = -1;
      this.addSettings(this.п_at_847356b8, this.н_989fed9a, this.Ц_989fed9a);
   }

   private void 西к() {
      this.В = TPLoot.RouteStage.SEARCH;
      this.Ъ = null;
      this.и = false;
      this.А_at_847356b8.clear();
      this.西д();
      this.field_9 = false;
      this.Т = 0L;
      this.О = 0L;
      this.е_fed7dec4_at_847356b8.ВД();
   }

   private TPLoot.Destination р(double var1, double var3, double var5, double var7, double var9, double var11) {
      int var13 = Mth.floor(var5);
      int var14 = Mth.floor(var7);
      TPLoot.Destination var15 = null;
      double var16 = с2Р;
      double var18 = Math.hypot(var9 - var1, var11 - var3);

      for (int var20 = -1; var20 <= 1; var20++) {
         for (int var21 = -1; var21 <= 1; var21++) {
            int var22 = var13 + var20;
            int var23 = var14 + var21;
            double var24 = var22 + с23;
            double var26 = var23 + с2р;
            if (!(Math.hypot(var24 - var1, var26 - var3) > с26) && !(Math.hypot(var9 - var24, var11 - var26) > var18 - с2_) && this.р_bbeebd87(var24, var26)) {
               Integer var28 = this.р(var22, var23);
               if (var28 != null) {
                  double var29 = var28.intValue() + 1.0;
                  if (this.р_bbeebd87(var24, var29, var26)) {
                     double var31 = var24 - var5;
                     double var33 = var26 - var7;
                     double var35 = var31 * var31 + var33 * var33;
                     if (!(var35 >= var16)) {
                        var16 = var35;
                        var15 = new TPLoot.Destination(var24, var29, var26);
                     }
                  }
               }
            }
         }
      }

      return var15;
   }

   private TPLoot.Destination р(Vec3 var1) {
      return this.р_bbeebd87(var1.x, var1.z) && this.р_bbeebd87(var1.x, var1.y, var1.z)
         ? new TPLoot.Destination(var1.x, var1.y, var1.z)
         : this.р_31266291(var1.x, var1.y, var1.z);
   }

   private boolean р_bbeebd87(double var1, double var3) {
      int var5 = Mth.floor(var1) >> 4;
      int var6 = Mth.floor(var3) >> 4;
      return MINECRAFT_CLIENT.level.getChunkSource().getChunk(var5, var6, ChunkStatus.FULL, false) != null;
   }

   private boolean в(long var1) {
      return this.field_9 && System.nanoTime() - this.Т < var1;
   }

   private int Ю(Item var1) {
      if (var1 == Items.PLAYER_HEAD) {
         return 10;
      } else if (var1 == Items.ENCHANTED_GOLDEN_APPLE) {
         return 8;
      } else if (var1 == Items.TOTEM_OF_UNDYING || var1 == Items.ELYTRA) {
         return 5;
      } else {
         return var1 == Items.NETHERITE_INGOT ? 4 : 1;
      }
   }

   private void 西д() {
      this.сс = -1;
      this.сВ = 0;
      this.сЯ = 0;
   }

   private void р_de5a6f78(ItemEntity var1) {
      if (this.сс != var1.getId()) {
         this.сс = var1.getId();
         this.сВ = 0;
         this.сЯ = 0;
      }
   }

   private enum StepResult {
      WAITING,
      MOVED,
      REACHED,
      BLOCKED;
   }

   private record Destination(double x, double feetY, double z) {
   }

   private enum RouteStage {
      SEARCH,
      RETURN,
      HOME;
   }
}
