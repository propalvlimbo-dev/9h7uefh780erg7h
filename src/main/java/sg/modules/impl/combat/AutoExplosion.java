package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.ItemUseStartEvent;
import sg.rotation.AimGeometryMath;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.BlockPlacementEvent;
import sg.network.InventoryInteractions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.BlockUseEvent;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;
import sg.rotation.RotationRegistry;
import sg.rotation.RotationStrategy;

public class AutoExplosion extends ClientModule {
   public final @com.google.gson.annotations.SerializedName("Я诶")  BooleanSetting placeOnRightClickSetting;
   public final @com.google.gson.annotations.SerializedName("Ц")  MultiChoiceSetting safetyOptionsSetting;
   private @com.google.gson.annotations.SerializedName("б")  BlockPos pendingCrystalPlacementPosition;
   private @com.google.gson.annotations.SerializedName("ДБ")  int crystalHotbarSlot;
   private @com.google.gson.annotations.SerializedName("Ае")  boolean unusedLegacyRepeatPlacementFlag;
   private @com.google.gson.annotations.SerializedName("ДС")  int previousHotbarSlot;
   private @com.google.gson.annotations.SerializedName("АА")  boolean restoreHotbarNextTick;
   @SerializedName("е")
   private AABB crystalDetectionBox;
   private final @com.google.gson.annotations.SerializedName("Хо")  EventListener<BlockUseEvent> blockUseListener;
   private @com.google.gson.annotations.SerializedName("А4")  boolean waitOneTickBeforePlacement;
   private @com.google.gson.annotations.SerializedName("Ап")  boolean obsidianPlacementPending;
   private @com.google.gson.annotations.SerializedName("Дз")  int obsidianPreviousHotbarSlot;
   private @com.google.gson.annotations.SerializedName("Ы")  BlockPos obsidianPlacementPosition;
   @SerializedName("Я")
   private BlockHitResult obsidianPlacementHit;
   @SerializedName("А")
   private RotationStrategy activeAimStrategy;
   private final @com.google.gson.annotations.SerializedName("ХO")  EventListener<BlockPlacementEvent> blockPlacementListener;
   private final @com.google.gson.annotations.SerializedName("Х7")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("ХР")  EventListener<ItemUseStartEvent> itemUseStartListener;
   private static final String field_0з = "Auto Explosion";
   private static final String field_0ц = "Автоматически устанавливает и взрывает кристаллы";
   private static final String field_0я = "Устанавливать по ПКМ";
   private static final String хс = "Настройки";
   private static final String хВ = "Не взрывать себя";
   private static final String хЯ = "Не взрывать друзей";
   private static final String хХ = "Не взрывать предметы";
   private static final String хе = "Не взрывать себя";
   private static final String хА = "Не взрывать друзей";
   private static final String х4 = "Не взрывать предметы";
   private static final double хп = 3.0;
   private static final String хД = "Не взрывать друзей";
   private static final String хЪ = "Не взрывать предметы";
   private static final double хт = 0.1;

   private void н(ItemUseStartEvent var1) {
      if (this.placeOnRightClickSetting.getValue()) {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
            ItemStack var2 = MINECRAFT_CLIENT.player.getMainHandItem();
            ItemStack var3 = MINECRAFT_CLIENT.player.getOffhandItem();
            if (!var2.has(DataComponents.FOOD) && !var3.has(DataComponents.FOOD)) {
               if (MINECRAFT_CLIENT.hitResult instanceof BlockHitResult var4) {
                  BlockPos var9 = var4.getBlockPos();
                  BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(var9);
                  if (!var6.is(Blocks.OBSIDIAN) && !var6.is(Blocks.BEDROCK) && !var6.isAir()) {
                     BlockPos var7 = var9.relative(var4.getDirection());
                     if (MINECRAFT_CLIENT.level.getBlockState(var7).canBeReplaced()) {
                        int var8 = InventoryInteractions.Ъ_a83dd0cc(Items.OBSIDIAN);
                        if (var8 >= 0 && var8 <= 8) {
                           var1.setCancelled(true);
                           this.obsidianPreviousHotbarSlot = MINECRAFT_CLIENT.player.getInventory().selected;
                           this.obsidianPlacementPosition = var7;
                           this.obsidianPlacementHit = new BlockHitResult(var4.getLocation(), var4.getDirection(), var9, false);
                           this.obsidianPlacementPending = true;
                           this.西(var4.getLocation());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void б(BlockPlacementEvent var1) {
      if (!MINECRAFT_CLIENT.player.getCooldowns().isOnCooldown(Items.END_CRYSTAL.getDefaultInstance())) {
         int var2 = this.н(Items.END_CRYSTAL, true);
         if (var2 != -1) {
            this.pendingCrystalPlacementPosition = var1.getPosition();
            this.crystalHotbarSlot = var2;
            this.waitOneTickBeforePlacement = true;
            this.西(this.ч(this.pendingCrystalPlacementPosition));
         }
      }
   }

   private void б(BlockUseEvent var1) {
      if (var1.getHand() == InteractionHand.OFF_HAND && MINECRAFT_CLIENT.player.getOffhandItem().getItem() == Items.PLAYER_HEAD) {
         var1.setCancelled(true);
      }

      if (!MINECRAFT_CLIENT.player.getCooldowns().isOnCooldown(Items.END_CRYSTAL.getDefaultInstance())) {
         BlockPos var2 = var1.getHitResult().getBlockPos();
         Block var3 = MINECRAFT_CLIENT.level.getBlockState(var2).getBlock();
         if (var3 == Blocks.OBSIDIAN || var3 == Blocks.BEDROCK) {
            this.crystalDetectionBox = new AABB(var2.above()).inflate(хт);
         }
      }
   }

   private void 衣诶() {
      if (this.obsidianPlacementPending && this.obsidianPlacementPosition != null && this.obsidianPlacementHit != null) {
         int var1 = InventoryInteractions.Ъ_a83dd0cc(Items.OBSIDIAN);
         if (var1 >= 0 && var1 <= 8 && MINECRAFT_CLIENT.level.getBlockState(this.obsidianPlacementPosition).canBeReplaced()) {
            this.西(this.obsidianPlacementHit.getLocation());
            MINECRAFT_CLIENT.player.getInventory().selected = var1;
            InteractionResult var2 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, this.obsidianPlacementHit);
            if (var2.consumesAction()) {
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            }

            this.衣ы();
         } else {
            this.衣ы();
         }
      }
   }

   private void 衣Ч() {
      if (this.crystalDetectionBox != null) {
         for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var2 instanceof EndCrystal && this.crystalDetectionBox.contains(var2.position())) {
               if (this.safetyOptionsSetting.isSelected(хе)) {
                  double var3 = MINECRAFT_CLIENT.player.getY();
                  double var5 = var2.getY();
                  if (Math.abs(var3 - var5) < 1.0) {
                     this.crystalDetectionBox = null;
                     return;
                  }
               }

               if (this.safetyOptionsSetting.isSelected(хА) || this.safetyOptionsSetting.isSelected(х4)) {
                  for (Entity var4 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
                     if (!(var2.distanceTo(var4) > хп)) {
                        if (this.safetyOptionsSetting.isSelected(хД)
                           && var4 instanceof Player var8
                           && var8 != MINECRAFT_CLIENT.player
                           && RoseClient.getInstance().getServices().getFriends().О(var8.getName().getString())) {
                           this.crystalDetectionBox = null;
                           return;
                        }

                        if (this.safetyOptionsSetting.isSelected(хЪ) && var4 instanceof ItemEntity) {
                           this.crystalDetectionBox = null;
                           return;
                        }
                     }
                  }
               }

               this.西(var2.getBoundingBox().getCenter());
               MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, var2);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
               this.crystalDetectionBox = null;
               this.衣О();
               return;
            }
         }
      }
   }

   private void 衣О() {
      if (this.activeAimStrategy != null) {
         RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
         if (var1.getActiveStrategy() == this.activeAimStrategy) {
            var1.activateStrategy(null);
         }

         this.activeAimStrategy = null;
      }
   }

   private void ФТ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         this.衣诶();
         this.衣Ч();
         if (this.restoreHotbarNextTick) {
            this.restoreHotbarNextTick = false;
            MINECRAFT_CLIENT.player.getInventory().selected = this.previousHotbarSlot;
            MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
            this.衣О();
         } else if (this.pendingCrystalPlacementPosition != null) {
            if (MINECRAFT_CLIENT.level.getBlockState(this.pendingCrystalPlacementPosition).isAir()) {
               this.pendingCrystalPlacementPosition = null;
               this.waitOneTickBeforePlacement = false;
               this.衣О();
            } else {
               Vec3 var2 = this.ч(this.pendingCrystalPlacementPosition);
               this.西(var2);
               if (this.waitOneTickBeforePlacement) {
                  this.waitOneTickBeforePlacement = false;
               } else {
                  this.previousHotbarSlot = MINECRAFT_CLIENT.player.getInventory().selected;
                  MINECRAFT_CLIENT.player.getInventory().selected = this.crystalHotbarSlot;
                  MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
                  Vec3 var3 = MINECRAFT_CLIENT.player.getEyePosition().subtract(var2);
                  InteractionResult var4 = MINECRAFT_CLIENT.gameMode
                     .useItemOn(
                        MINECRAFT_CLIENT.player,
                        InteractionHand.MAIN_HAND,
                        new BlockHitResult(var2, Direction.getApproximateNearest((float)var3.x, (float)var3.y, (float)var3.z), this.pendingCrystalPlacementPosition, false)
                     );
                  if (var4.consumesAction()) {
                     MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
                  }

                  this.restoreHotbarNextTick = true;
                  if (this.unusedLegacyRepeatPlacementFlag) {
                     this.crystalHotbarSlot = this.previousHotbarSlot;
                     this.unusedLegacyRepeatPlacementFlag = false;
                  } else {
                     this.pendingCrystalPlacementPosition = null;
                  }
               }
            }
         }
      }
   }

   public AutoExplosion() {
      super(field_0з, field_0ц, ModuleCategory.COMBAT);
      this.placeOnRightClickSetting = new BooleanSetting(field_0я, true);
      this.safetyOptionsSetting = new MultiChoiceSetting(хс, new ChoiceOption(хВ, true), new ChoiceOption(хЯ, true), new ChoiceOption(хХ, true));
      this.blockUseListener = this::б;
      this.obsidianPreviousHotbarSlot = -1;
      this.blockPlacementListener = this::б;
      this.playerTickListener = this::ФТ;
      this.itemUseStartListener = this::н;
      this.addSettings(this.safetyOptionsSetting, this.placeOnRightClickSetting);
   }

   private int н(Item var1, boolean var2) {
      int var3 = var2 ? 9 : 36;

      for (int var4 = 0; var4 < var3; var4++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var4).is(var1)) {
            return var4;
         }
      }

      return -1;
   }

   private Vec3 ч(BlockPos var1) {
      return AimGeometryMath.clampPointToBox(MINECRAFT_CLIENT.player.getEyePosition(), new AABB(var1));
   }

   private void 西(Vec3 var1) {
      this.activeAimStrategy = RoseClient.getInstance().getServices().getRotations().getSlowAimRotation();
      this.activeAimStrategy.setTargetPosition(var1);
      RoseClient.getInstance().getServices().getRotations().activateStrategy(this.activeAimStrategy);
   }

   private void 衣ы() {
      if (this.obsidianPreviousHotbarSlot >= 0 && this.obsidianPreviousHotbarSlot <= 8) {
         MINECRAFT_CLIENT.player.getInventory().selected = this.obsidianPreviousHotbarSlot;
      }

      this.obsidianPreviousHotbarSlot = -1;
      this.obsidianPlacementPosition = null;
      this.obsidianPlacementHit = null;
      this.obsidianPlacementPending = false;
      if (this.pendingCrystalPlacementPosition == null) {
         this.衣О();
      }
   }

   @Override
   public void onDisable() {
      this.衣О();
      this.pendingCrystalPlacementPosition = null;
      this.crystalHotbarSlot = -1;
      this.unusedLegacyRepeatPlacementFlag = false;
      this.restoreHotbarNextTick = false;
      this.waitOneTickBeforePlacement = false;
      this.crystalDetectionBox = null;
      this.obsidianPlacementPending = false;
      this.obsidianPreviousHotbarSlot = -1;
      this.obsidianPlacementPosition = null;
      this.obsidianPlacementHit = null;
   }
}
