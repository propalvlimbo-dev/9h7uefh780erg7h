package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.SingleChoiceSetting;

public class Spider extends ClientModule {
   private final SingleChoiceSetting а;
   @SerializedName("А")
   private final Stopwatch А_at_a2dc3ee7;
   private final EventListener<PlayerMovementSyncEvent> э;
   private int с1;
   private int сЬ;
   private int с衣;
   private boolean р;
   @SerializedName("6")
   private boolean field_6;
   private int сш;
   private static final String ся必 = "Spider";
   private static final String сящ = "Лазит по стенам";
   private static final String сяГ = "Mode";
   private static final String сял = "FunTimeV4";
   private static final String сяэ = "WhiteFlags";
   private static final String сяю = "FunTime Прыжки";
   private static final String сяЩ = "FunTimeV4";
   private static final String сяу = "Нужен сахарный тростник!";
   private static final String ся2 = "WhiteFlags";
   private static final String сяф = "Нужен белый флаг!";
   private static final String сяи = "FunTime Прыжки";
   private static final String ся9 = "FunTimeV4";
   private static final String сяо = "WhiteFlags";
   private static final float сяO = 130.0F;
   private static final String ся7 = "Нужен сахарный тростник!";
   private static final float сяР = 50.0F;
   private static final String ся3 = "Нужен белый флаг!";
   private static final float сяр = 50.0F;
   private static final float ся6 = 90.0F;
   private static final float ся_ = 90.0F;
   private static final double сяШ = 0.03;
   private static final double сяы = 0.01;
   private static final double сяй = 0.01;
   private static final double сяЖ = 0.01;
   private static final double сям = 0.01;
   private static final double сяК = 0.9;
   private static final double сяI = 0.01;
   private static final double сяТ = 0.5;
   private static final double сяО = 0.5;
   private static final float сяФ = 90.0F;

   private void OЗ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         Inventory var1 = MINECRAFT_CLIENT.player.getInventory();
         if (this.р && this.сЬ != -1 && this.с衣 != -1) {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, this.сЬ, this.с衣, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         }

         if (this.с1 >= 0 && this.с1 < 9) {
            var1.setSelectedSlot(this.с1);
         }

         this.Oн();
      } else {
         this.Oн();
      }
   }

   private KeyMapping[] method_9() {
      return new KeyMapping[]{MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint};
   }

   private int Ш_a83dd0cc() {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      Inventory var1 = MINECRAFT_CLIENT.player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty() && var3.is(Items.SUGAR_CANE)) {
            return var2;
         }
      }

      return -1;
   }

   private boolean OЭ() {
      BlockPos var1 = MINECRAFT_CLIENT.player.blockPosition();
      BlockPos var2 = var1.above();
      return this.С(var1) || this.С(var2);
   }

   private boolean С(BlockPos var1) {
      Block var2 = MINECRAFT_CLIENT.level.getBlockState(var1).getBlock();
      return var2 == Blocks.BANNER.white() || var2 == Blocks.WALL_BANNER.white();
   }

   private void Oн() {
      this.р = false;
      this.сЬ = -1;
      this.с衣 = -1;
   }

   private void method_6(PlayerMovementSyncEvent var1) {
      int var2 = this.Ш_a83dd0cc();
      if (var2 == -1) {
         ClientChat.З(ся7);
         this.disable();
      } else {
         if (this.А_at_a2dc3ee7.Т(сяР) && MINECRAFT_CLIENT.player.horizontalCollision) {
            this.method_9(var1, var2, Spider.ClimbItem.REED);
            this.А_at_a2dc3ee7.ВД();
         }
      }
   }

   private boolean OЧ() {
      AABB var1 = MINECRAFT_CLIENT.player.getBoundingBox().inflate(сяШ);
      int var2 = Mth.floor(var1.minX - сяы);
      int var3 = Mth.floor(var1.maxX + сяй);
      int var4 = Mth.floor(var1.minY);
      int var5 = Mth.floor(var1.maxY);
      int var6 = Mth.floor(var1.minZ - сяЖ);
      int var7 = Mth.floor(var1.maxZ + сям);

      for (int var8 = var2; var8 <= var3; var8++) {
         for (int var9 = var4; var9 <= var5; var9++) {
            for (int var10 = var6; var10 <= var7; var10++) {
               BlockPos var11 = new BlockPos(var8, var9, var10);
               BlockState var12 = MINECRAFT_CLIENT.level.getBlockState(var11);
               if (!var12.isAir()) {
                  VoxelShape var13 = var12.getCollisionShape(MINECRAFT_CLIENT.level, var11);
                  if (!var13.isEmpty()) {
                     AABB var14 = var13.bounds().move(var11);
                     double var15 = var14.getXsize();
                     double var17 = var14.getYsize();
                     double var19 = var14.getZsize();
                     boolean var21 = (var15 < 1.0 || var19 < 1.0) && var17 >= сяК;
                     if (var21) {
                        AABB var22 = new AABB(var11.getX(), var11.getY(), var11.getZ(), var11.getX() + 1.0, var11.getY() + 1.0, var11.getZ() + 1.0);
                        if (var1.intersects(var14) || var1.intersects(var22)) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private void Ь(int var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         Inventory var2 = MINECRAFT_CLIENT.player.getInventory();
         int var3 = var2.getSelectedSlot();
         if (var1 != var3) {
            if (this.р) {
               this.OЗ();
               var3 = var2.getSelectedSlot();
            }

            this.с1 = var3;
            if (var1 < 9) {
               var2.setSelectedSlot(var1);
            } else {
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var1, var3, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               this.сЬ = var1;
               this.с衣 = var3;
               this.р = true;
            }
         }
      }
   }

   private void O1() {
      BlockPos var1 = BlockPos.containing(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY() - сяI, MINECRAFT_CLIENT.player.getZ());
      this.Й(var1);
   }

   private void Й(PlayerMovementSyncEvent var1) {
      if (this.field_6) {
         this.西(var1);
      } else {
         int var2 = this.м_a83dd0cc();
         if (var2 == -1) {
            ClientChat.З(ся3);
            this.disable();
         } else {
            boolean var3 = MINECRAFT_CLIENT.player.horizontalCollision || this.OЭ();
            if (this.А_at_a2dc3ee7.Т(сяр) && var3) {
               if (var2 >= 9) {
                  var1.setPitch(ся6);
                  this.Щ(var2);
               } else {
                  this.method_9(var1, var2, Spider.ClimbItem.WHITE_FLAG);
               }

               this.А_at_a2dc3ee7.ВД();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.Oн();
      this.field_6 = false;
      if (MINECRAFT_CLIENT.player != null) {
         this.с1 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
         if (this.а.isSelected(сяЩ) && this.Ш_a83dd0cc() == -1) {
            ClientChat.З(сяу);
            this.toggle();
            return;
         }

         if (this.а.isSelected(ся2) && this.м_a83dd0cc() == -1) {
            ClientChat.З(сяф);
            this.toggle();
            return;
         }
      }

      super.onEnable();
   }

   private void Oе() {
      for (KeyMapping var4 : this.method_9()) {
         var4.setDown(false);
      }

      RoseClient.getInstance().getServices().getModules().getGuiMove().з(true);
   }

   public void ч(PlayerMovementSyncEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         MINECRAFT_CLIENT.options.keyUse.setDown(false);
         if (this.а.isSelected(сяи)) {
            this.С(var1);
         } else if (this.а.isSelected(ся9)) {
            this.method_6(var1);
         } else {
            if (this.а.isSelected(сяо)) {
               this.Й(var1);
            }
         }
      }
   }

   private int м_a83dd0cc() {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      Inventory var1 = MINECRAFT_CLIENT.player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty() && var3.is(Items.BANNER.white())) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public void onDisable() {
      this.OЗ();
      this.OК();
      if (MINECRAFT_CLIENT.options != null) {
         MINECRAFT_CLIENT.options.keyUse.setDown(false);
      }

      super.onDisable();
   }

   private void 西(PlayerMovementSyncEvent var1) {
      this.Oе();
      var1.setPitch(сяФ);
      int var2 = RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue();
      if (MINECRAFT_CLIENT.player.tickCount - this.сш > var2) {
         int var3 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
         this.method_9(var1, var3, Spider.ClimbItem.WHITE_FLAG);
         this.OК();
         this.А_at_a2dc3ee7.ВД();
      }
   }

   public Spider() {
      super(ся必, сящ, ModuleCategory.MOVEMENT);
      this.а = new SingleChoiceSetting(сяГ, new ChoiceOption(сял), new ChoiceOption(сяэ), new ChoiceOption(сяю));
      this.А_at_a2dc3ee7 = new Stopwatch();
      this.э = this::ч;
      this.с1 = -1;
      this.сЬ = -1;
      this.с衣 = -1;
      this.р = false;
      this.addSettings(this.а);
   }

   private void Щ(int var1) {
      this.Oе();
      this.Ь(var1);
      this.сш = MINECRAFT_CLIENT.player.tickCount;
      this.field_6 = true;
   }

   private void OЪ() {
      BlockPos var1 = MINECRAFT_CLIENT.player.blockPosition().below();

      for (int var2 = 0; var2 < 10; var2++) {
         BlockPos var3 = var1.above(var2);
         Block var4 = MINECRAFT_CLIENT.level.getBlockState(var3).getBlock();
         if (var4 != Blocks.SUGAR_CANE) {
            break;
         }

         var1 = var3;
      }

      BlockPos var5 = var1.above();
      if (MINECRAFT_CLIENT.level.getBlockState(var5).isAir()) {
         this.Й(var1);
      }
   }

   private void method_9(PlayerMovementSyncEvent var1, int var2, Spider.ClimbItem var3) {
      var1.setPitch(ся_);
      var1.setOnGround(true);
      MINECRAFT_CLIENT.player.setOnGround(true);
      MINECRAFT_CLIENT.player.verticalCollision = true;
      MINECRAFT_CLIENT.player.needsSync = true;
      MINECRAFT_CLIENT.player.jumpFromGround();
      this.Ь(var2);
      switch (var3) {
         case REED:
            this.OЪ();
            break;
         case WHITE_FLAG:
            this.O1();
      }
   }

   private void Й(BlockPos var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         Vec3 var2 = new Vec3(var1.getX() + сяТ, var1.getY() + 1.0, var1.getZ() + сяО);
         BlockHitResult var3 = new BlockHitResult(var2, Direction.UP, var1, false);
         InteractionResult var4 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var3);
         if (var4.consumesAction()) {
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         }
      }
   }

   private void С(PlayerMovementSyncEvent var1) {
      if (this.А_at_a2dc3ee7.Т(сяO)) {
         if (this.OЧ()) {
            if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
               var1.setOnGround(true);
               MINECRAFT_CLIENT.player.setOnGround(true);
               MINECRAFT_CLIENT.player.jumpFromGround();
            }
         }
      }
   }

   private void OК() {
      this.field_6 = false;
      RoseClient.getInstance().getServices().getModules().getGuiMove().з(false);
      if (MINECRAFT_CLIENT.gui.screen() == null && MINECRAFT_CLIENT.options != null) {
         for (KeyMapping var4 : this.method_9()) {
            var4.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var4.key.getValue()));
         }
      }
   }

   private enum ClimbItem {
      REED,
      WHITE_FLAG;
   }
}
