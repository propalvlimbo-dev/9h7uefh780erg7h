package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.InventoryInteractions;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.rotation.FixedAngleRotation;

public final class AppleFarmer extends ClientModule {
   @SerializedName("Я")
   private final FixedAngleRotation aimRotation = new FixedAngleRotation();
   private final @com.google.gson.annotations.SerializedName("Г")  EventListener<PlayerTickEvent> playerTickListener = this::щ;
   private static final String ЯЮЪ = "Apple Farmer";
   private static final String ЯЮт = "Автоматически фармит яблоки";
   private static final double ЯЮа = 5.0;
   private static final double ЯЮЮ = 5.0;
   private static final double ЯЮЧ = 16.0;
   private static final float ЯЮЛ = 0.15F;
   private static final double ЯЮ诶 = 0.5;
   private static final double ЯЮ1 = 0.5;
   private static final double ЯЮЬ = 5.0;
   private static final String ЯЮ衣 = "chest";
   private static final float ЯЮш = 10.0F;
   private static final float ЯЮИ = 4.0F;
   private static final float ЯЮь = -30.0F;
   private static final float ЯЮж = 30.0F;
   private static final double ЯЮ弟 = -1.0;
   private static final float ЯЮб = 4.0F;
   private static final float ЯЮЫ = -90.0F;
   private static final float ЯЮl = 90.0F;
   private static final double ЯЮ8 = 20.0;
   private static final float ЯЮ5 = 0.31F;
   private static final double ЯЮв = 0.5;
   private static final float ЯЮъ = 0.73F;
   private static final float ЯЮМ = 1.1F;
   private static final double ЯЮН = 0.3;
   private static final float ЯЮн = 1.7F;
   private static final float ЯЮЦ = 2.6F;
   private static final double ЯЮч = 0.2;
   private static final double ЯЮЭ = 8.0;
   private static final double ЯЮ必 = Double.MAX_VALUE;
   private static final double ЯЮщ = Double.MAX_VALUE;

   private void Щ(BlockPos var1) {
      if (var1 != null && this.Щ(Vec3.atCenterOf(var1)) && MINECRAFT_CLIENT.player.tickCount % 4 == 0) {
         Vec3 var2 = Vec3.atCenterOf(var1);
         Vec3 var3 = new AABB(var1).clip(MINECRAFT_CLIENT.player.getEyePosition(), var2).orElse(var2);
         MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, new BlockHitResult(var3, Direction.getApproximateNearest(var3.subtract(var2)), var1, false));
      }
   }

   private void ф(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.containerId, var2, var3, var4, MINECRAFT_CLIENT.player);
   }

   private boolean method_9Д() {
      return InventoryInteractions.щ(Items.OAK_SAPLING) == 0 || InventoryInteractions.щ(Items.BONE_MEAL) == 0;
   }

   private void method_9Л() {
      float var1 = this.method_1();
      this.ф(var1 * ЯЮш, Mth.clamp(var1 / ЯЮИ, ЯЮь, ЯЮж));
      if (MINECRAFT_CLIENT.player.tickCount % 5 == 0) {
         InventoryMenu var2 = MINECRAFT_CLIENT.player.inventoryMenu;
         int var3 = -1;
         int var4 = -1;

         for (Slot var6 : var2.slots) {
            if (this.ф(var6) && var6.getItem().is(Items.OAK_SAPLING) && var6.getItem().getCount() > var4) {
               var4 = var6.getItem().getCount();
               var3 = var6.index;
            }
         }

         for (Slot var9 : var2.slots) {
            if (this.ф(var9)) {
               ItemStack var7 = var9.getItem();
               if (var7.is(Items.STICK) || var7.is(Items.OAK_SAPLING) && var9.index != var3) {
                  this.ф(var2, var9.index, 1, ContainerInput.THROW);
               }
            }
         }
      }
   }

   private boolean ф(Predicate<ItemStack> var1) {
      int var2 = -1;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.test(MINECRAFT_CLIENT.player.getInventory().getItem(var3))) {
            var2 = var3;
            break;
         }
      }

      if (var2 < 0) {
         return false;
      }

      if (var2 < 9) {
         MINECRAFT_CLIENT.player.getInventory().selected = var2;
         return true;
      }

      if (MINECRAFT_CLIENT.player.tickCount % 4 != 0) {
         return true;
      }

      int var5 = MINECRAFT_CLIENT.player.getInventory().selected;

      for (int var4 = 0; var4 < 9; var4++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var4).isEmpty()) {
            var5 = var4;
            break;
         }
      }

      int var6 = var2 < 9 ? var2 + 36 : var2;
      MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var6, var5, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
      MINECRAFT_CLIENT.player.getInventory().selected = var5;
      return true;
   }

   private int ф(AbstractContainerMenu var1, boolean var2, int var3, Predicate<ItemStack> var4) {
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (var5 >= var3) {
            break;
         }

         if (this.ф(var7) != var2 && var4.test(var7.getItem())) {
            this.ф(var1, var7.index, 0, ContainerInput.QUICK_MOVE);
            var5++;
         }
      }

      return var5;
   }

   private boolean ф(Slot var1) {
      return var1.container == MINECRAFT_CLIENT.player.getInventory();
   }

   private double г(BlockPos var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var3 = MINECRAFT_CLIENT.player.getViewVector(1.0F);
      Vec3 var4 = Vec3.atCenterOf(var1).subtract(var2);
      double var5 = var4.dot(var3);
      return var5 <= 0.0 ? ЯЮ必 : var4.subtract(var3.scale(var5)).lengthSqr();
   }

   private int в(Item var1) {
      for (Slot var3 : MINECRAFT_CLIENT.player.inventoryMenu.slots) {
         if (this.ф(var3) && var3.getItem().is(var1)) {
            return var3.index;
         }
      }

      return -1;
   }

   private void я_de5a6f78(BlockPos var1) {
      boolean var2 = MINECRAFT_CLIENT.level.getBlockState(var1.above()).getBlock() instanceof SaplingBlock;
      Predicate<ItemStack> var3 = var2
         ? var0 -> var0.is(Items.BONE_MEAL)
         : var0 -> var0.getItem() instanceof BlockItem var1x ? var1x.getBlock() instanceof SaplingBlock : false;
      if (!var3.test(MINECRAFT_CLIENT.player.getMainHandItem())) {
         this.ф(var3);
      } else {
         Vec3 var4 = new Vec3(var1.getX() + ЯЮ诶, var1.getY() + 1, var1.getZ() + ЯЮ1);
         if (this.Щ(var4) && MINECRAFT_CLIENT.player.tickCount % 4 == 0) {
            BlockPos var5 = var2 ? var1.above() : var1;
            MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, new BlockHitResult(var4, Direction.UP, var5, false));
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         }
      }
   }

   private boolean Щ(Vec3 var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var3 = var1.subtract(var2).normalize();
      float var4 = (float)Math.toDegrees(Math.atan2(-var3.x, var3.z));
      float var5 = (float)Math.toDegrees(-Math.asin(Mth.clamp(var3.y, ЯЮ弟, 1.0)));
      float var6 = this.method_1();
      this.ф(var4 + var6 / 2.0F, Mth.clamp(var5 + var6 / ЯЮб, ЯЮЫ, ЯЮl));
      float var7 = Mth.wrapDegrees(var4 - MINECRAFT_CLIENT.player.getYRot());
      float var8 = var5 - MINECRAFT_CLIENT.player.getXRot();
      return Math.hypot(var7, var8) < ЯЮ8;
   }

   private void щ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         if (MINECRAFT_CLIENT.player.tickCount % 600 < 10) {
            if (MINECRAFT_CLIENT.player.getMainHandItem().getItem() instanceof AxeItem) {
               MINECRAFT_CLIENT.player.getInventory().selected = ThreadLocalRandom.current().nextInt(9);
            }
         } else {
            BlockPos var2 = this.ф(ЯЮа, var0 -> var0.is(BlockTags.LEAVES), this::г);
            if (var2 != null) {
               this.ф(var2, var0 -> var0.getItem() instanceof ShearsItem);
            } else {
               BlockPos var3 = this.ф(ЯЮЮ, var0 -> var0.is(BlockTags.LOGS), var0 -> var0.getY());
               if (var3 != null) {
                  this.ф(var3, var0 -> var0.getItem() instanceof AxeItem);
               } else if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen) {
                  this.method_9К();
               } else if (InventoryInteractions.щ(Items.BONE) > 0 && InventoryInteractions.щ(Items.BONE_MEAL) == 0) {
                  this.method_9б();
               } else if (!this.method_9З() && !this.method_9Д()) {
                  BlockPos var4 = this.ф(ЯЮЧ, var0 -> var0.is(BlockTags.DIRT), this::ф);
                  if (var4 != null) {
                     if (InventoryInteractions.щ(Items.STICK) <= 128 && InventoryInteractions.щ(Items.OAK_SAPLING) <= 128) {
                        this.я_de5a6f78(var4);
                     } else {
                        this.method_9Л();
                     }
                  }
               } else {
                  this.method_9К();
               }
            }
         }
      }
   }

   private boolean method_9З() {
      return InventoryInteractions.щ(Items.APPLE) > 128 || InventoryInteractions.щ(Items.OAK_LOG) > 192;
   }

   private void ф(BlockPos var1, Predicate<ItemStack> var2) {
      if (!var2.test(MINECRAFT_CLIENT.player.getMainHandItem())) {
         this.ф(var2);
      } else if (!(MINECRAFT_CLIENT.player.getMainHandItem().getItem() instanceof AxeItem) || !(MINECRAFT_CLIENT.player.getAttackStrengthScale(0.0F) <= ЯЮЛ)) {
         Vec3 var3 = Vec3.atCenterOf(var1);
         if (this.Щ(var3)) {
            Vec3 var4 = new AABB(var1).clip(MINECRAFT_CLIENT.player.getEyePosition(), var3).orElse(var3);
            MINECRAFT_CLIENT.gameMode.continueDestroyBlock(var1, Direction.getApproximateNearest(var4.subtract(var3)));
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         }
      }
   }

   private void method_9б() {
      if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         InventoryMenu var1 = MINECRAFT_CLIENT.player.inventoryMenu;
         if (var1.getSlot(0).getItem().is(Items.BONE_MEAL)) {
            this.ф(var1, 0, 0, ContainerInput.QUICK_MOVE);
            MINECRAFT_CLIENT.player.closeContainer();
         } else {
            int var2 = this.в(Items.BONE);
            if (var2 >= 0) {
               this.ф(var1, var2, 0, ContainerInput.PICKUP);
               this.ф(var1, 1, 0, ContainerInput.PICKUP);
            }
         }
      }
   }

   public AppleFarmer() {
      super(ЯЮЪ, ЯЮт, ModuleCategory.MISC);
   }

   private void method_9К() {
      if (!(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var1)) {
         this.Щ(this.ф(ЯЮЬ, var1x -> var1x.is(this.method_9З() ? Blocks.CHEST : Blocks.BARREL), this::ф));
      } else if (MINECRAFT_CLIENT.player.tickCount % 50 == 0) {
         MINECRAFT_CLIENT.player.closeContainer();
      } else if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         AbstractContainerMenu var6 = var1.getMenu();
         boolean var3 = var1.getTitle().getContents() instanceof TranslatableContents var4 && var4.getKey().contains(ЯЮ衣);
         int var7 = var3
            ? this.ф(var6, false, 5, var0 -> var0.is(Items.APPLE) || var0.is(Items.OAK_LOG))
            : (this.method_9Д() ? this.ф(var6, true, 3, this::ф) : 0);
         if (var7 == 0) {
            MINECRAFT_CLIENT.player.closeContainer();
         }
      }
   }

   private double ф(BlockPos var1) {
      return MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1));
   }

   private boolean ф(ItemStack var1) {
      return var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof SaplingBlock ? true : var1.is(Items.BONE_MEAL);
   }

   private void ф(float var1, float var2) {
      this.aimRotation.setTargetAngles(var1, var2);
      RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.aimRotation, 3, 1);
   }

   private float method_1() {
      float var1 = MINECRAFT_CLIENT.player.tickCount + MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false);
      return (float)((Math.sin(var1 * ЯЮ5) * ЯЮв + Math.sin(var1 * ЯЮъ + ЯЮМ) * ЯЮН + Math.sin(var1 * ЯЮн + ЯЮЦ) * ЯЮч) * ЯЮЭ);
   }

   private BlockPos ф(double var1, Predicate<BlockState> var3, ToDoubleFunction<BlockPos> var4) {
      BlockPos var5 = MINECRAFT_CLIENT.player.blockPosition();
      BlockPos var6 = null;
      double var7 = ЯЮщ;
      double var9 = var1 * var1;
      int var11 = (int)Math.ceil(var1);
      MutableBlockPos var12 = new MutableBlockPos();

      for (int var13 = -var11; var13 <= var11; var13++) {
         for (int var14 = -var11; var14 <= var11; var14++) {
            for (int var15 = -var11; var15 <= var11; var15++) {
               var12.set(var5.getX() + var13, var5.getY() + var14, var5.getZ() + var15);
               if (var3.test(MINECRAFT_CLIENT.level.getBlockState(var12)) && !(this.ф(var12) > var9)) {
                  double var16 = var4.applyAsDouble(var12);
                  if (var16 < var7) {
                     var7 = var16;
                     var6 = var12.immutable();
                  }
               }
            }
         }
      }

      return var6;
   }

   @Override
   public void onDisable() {
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.aimRotation);
      super.onDisable();
   }
}
