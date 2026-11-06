package sg.modules.impl.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import settings.render.WardenLootItemClassifier;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.ec.BaritoneIntegration;
import sg.gui.ServerStatusParser;
import sg.network.ClientChat;
import sg.network.InventoryInteractions;
import sg.render.pipeline.WardenFarmArea;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.rotation.FixedAngleRotation;
import sg.events.impl.MovementInputEvent;
import sg.settings.SingleChoiceSetting;
import sg.modules.impl.init.TimedChestTracker;
import sg.modules.impl.render.WardenESP;

public final class AutoWarden extends ClientModule {
   private final BooleanSetting ВК;
   private final BooleanSetting ВI;
   private final SingleChoiceSetting сД;
   private final List<Integer> сТ;
   private final Map<BlockPos, Integer> сО;
   private final Map<BlockPos, Integer> сФ;
   @SerializedName("Я")
   private final TimedChestTracker Я_at_d90e78f5;
   @SerializedName("е")
   private final FixedAngleRotation е_at_d90e78f5;
   @SerializedName("А")
   private final FixedAngleRotation А_at_d90e78f5;
   private final Stopwatch Ц;
   private final EventListener<PlayerTickEvent> Яц;
   private final EventListener<PacketReceiveEvent> Яя;
   private final EventListener<MovementInputEvent> Хс;
   private int ДХ;
   private int Де;
   private boolean еЬ;
   @SerializedName("Х")
   private AABB Х_at_d90e78f5;
   private BlockPos ж;
   private String Яж;
   private AutoWarden.FarmStage В;
   private boolean е衣;
   private static final String Веш = "Auto Warden";
   private static final String ВеИ = "Автоматизирует фарм варденов на анархии";
   private static final String Веь = "Использовать скорость";
   private static final String Веж = "Репортить обидчиков";
   private static final String Ве弟 = "Приоритеты лута";
   private static final String Веб = "Низкий";
   private static final String ВеЫ = "Средний";
   private static final String Веl = "Высокий";
   private static final String Ве8 = "Shift + Пробел — быстрое выключение функции";
   private static final String Ве5 = "avoidance";
   private static final String Вев = "maxFallHeightNoWater";
   private static final String Веъ = "turnSpeed";
   private static final float ВеМ = 75.0F;
   private static final String ВеН = "blockFreeLook";
   private static final String Вен = "randomLooking";
   private static final String ВеЦ = "randomLooking113";
   private static final String Веч = "allowBreak";
   private static final String ВеЭ = "allowPlace";
   private static final String Ве必 = "avoidance";
   private static final String Вещ = "maxFallHeightNoWater";
   private static final double ВеГ = 32.0;
   private static final String Вел = "warden.roar";
   private static final String Веэ = "warden.angry";
   private static final String Вею = "warden.sonic";
   private static final String ВеЩ = "Помянем. Вы погибли";
   private static final String Веу = "Вас убил";
   private static final String Ве2 = "нет";
   private static final double Веф = 2.0;
   private static final String Веи = "Вас убил ";
   private static final String Ве9 = ",";
   private static final double Вео = 3.0;
   private static final double ВеO = 0.05;
   private static final double Ве7 = 0.05;
   private static final String ВеР = "ОШИБКА -> .warden list пустой";
   private static final String Ве3 = "Низкий";
   private static final double Вер = 1.5;
   private static final String Ве6 = "Высокий";
   private static final double Ве_ = 0.8;
   private static final long ВеШ = 3L;
   private static final double Веы = 14.0;
   private static final String Вей = "home";
   private static final double ВеЖ = 900.0;
   private static final double Вем = 16.0;
   private static final double ВеК = 0.01;
   private static final double ВеI = 360.0;
   private static final double ВеТ = 540.0;
   private static final double ВеО = 360.0;
   private static final double ВеФ = 180.0;
   private static final double ВеЙ = 10.0;
   private static final double Век = 16.0;
   private static final double Вег = 2.0;
   private static final double Ве0 = -1.0;
   private static final double Вех = 25.0;
   private static final double ВеУ = 25.0;
   private static final double ВеЗ = Double.MAX_VALUE;
   private static final long ВеЕ = 25000L;
   private static final long ВеП = 1000L;
   private static final long Ве西 = 1000L;
   private static final double Вед = 7.0;
   private static final double ВеБ = 2.0;
   private static final long ВеС = 6000L;
   private static final double Вез = 20.0;
   private static final double Вец = 10.0;
   private static final double Вея = 40.0;
   private static final double ВАс = 2.4;
   private static final double ВАВ = 10.0;
   private static final double ВАЯ = 10.0;
   private static final long ВАХ = 45000L;
   private static final double ВАе = Double.MAX_VALUE;
   private static final double ВАА = 16.0;
   private static final double ВА4 = 25.0;
   private static final long ВАп = 5000L;
   private static final double ВАД = 144.0;
   private static final double ВАЪ = 144.0;
   private static final long ВАт = 15000L;
   private static final double ВАа = 625.0;
   private static final double ВАЮ = 0.5;
   private static final double ВАЧ = 0.5;
   private static final double ВАЛ = 0.5;
   private static final double ВА诶 = Double.MAX_VALUE;
   private static final double ВА1 = -0.4;
   private static final double ВАЬ = 0.41;
   private static final double ВА衣 = -0.4;
   private static final double ВАш = 0.41;
   private static final double ВАИ = -0.4;
   private static final double ВАь = 0.41;
   private static final double ВАж = 0.4;
   private static final double ВА弟 = 0.4;
   private static final double ВАб = 0.4;
   private static final double ВАЫ = 25.0;
   private static final double ВАl = 20.0;
   private static final float ВА8 = 90.0F;
   private static final float ВА5 = 90.0F;
   private static final double ВАв = -1.0;
   private static final float ВАъ = 0.31F;
   private static final double ВАМ = 0.5;
   private static final float ВАН = 0.73F;
   private static final float ВАн = 1.1F;
   private static final double ВАЦ = 0.3;
   private static final float ВАч = 1.7F;
   private static final float ВАЭ = 2.6F;
   private static final double ВА必 = 0.2;
   private static final double ВАщ = 8.0;
   private static final float ВАГ = 4.0F;
   private static final float ВАл = -90.0F;
   private static final float ВАэ = 90.0F;
   private static final double ВАю = 5.0;
   private static final double ВАЩ = 0.0025;
   private static final String ВАу = "Anarchies";
   private static final String ВА2 = "Anarchies";
   private static final String ВАф = "Anarchies";

   private int ю(Predicate<ItemStack> var1) {
      for (int var2 = 0; var2 < 36; var2++) {
         if (var1.test(MINECRAFT_CLIENT.player.getInventory().getItem(var2))) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      this.сТ.clear();
      if (var1 != null && var1.has(ВА2)) {
         for (JsonElement var3 : var1.getAsJsonArray(ВАф)) {
            this.сТ.add(var3.getAsInt());
         }
      }
   }

   private void Д(MovementInputEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (this.Ъю() && MINECRAFT_CLIENT.player.getMainHandItem().isEmpty() && !this.я(Вео)) {
            int var2 = MINECRAFT_CLIENT.player.tickCount % 10 <= ThreadLocalRandom.current().nextInt(3, 9) ? -1 : 1;
            var1.setForwardAmount(var2);
            var1.setLeftAmount(var2);
            var1.setForwardPressed(var2 > 0);
            var1.setBackwardPressed(var2 < 0);
            var1.setLeftPressed(var2 > 0);
            var1.setRightPressed(var2 < 0);
         }
      }
   }

   private boolean ЪИ() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen && MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         MINECRAFT_CLIENT.player.closeContainer();
      }

      return !(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen);
   }

   private double ю(int var1, int var2, boolean var3) {
      double var4 = ВеЗ;

      for (Entity var7 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var7 != MINECRAFT_CLIENT.player && (var7 instanceof Player || var3 && var7 instanceof Warden)) {
            var4 = Math.min(var4, Math.hypot(var7.getX() - var1, var7.getZ() - var2));
         }
      }

      return var4;
   }

   private int юД() {
      return this.сТ.isEmpty() ? -1 : this.сТ.getFirst();
   }

   private boolean Ъл() {
      return MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var1 && !var1.getMenu().getCarried().isEmpty();
   }

   private boolean я(Warden var1) {
      double var2 = Math.toDegrees(Math.atan2(-(MINECRAFT_CLIENT.player.getX() - var1.getX()), MINECRAFT_CLIENT.player.getZ() - var1.getZ()));
      return Math.abs(((var1.getVisualRotationYInDegrees() - var2) % ВеI + ВеТ) % ВеО - ВеФ) < ВеЙ;
   }

   private boolean Ъ9() {
      return this.юД() >= 0 && this.юД() == ServerStatusParser.method_6_a83dd0cc();
   }

   private void ю(boolean var1, boolean var2, AutoWarden.FarmStage var3) {
      if (!var1) {
         if (this.ЪИ()) {
            this.В = var3;
         }
      } else if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var4) {
         if (var2) {
            this.я(var4);
         } else {
            this.method_9(var4);
         }
      } else {
         this.я(this.ю(var2), 2);
      }
   }

   private void ье(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() instanceof DeathScreen && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.tickCount >= 5) {
         MINECRAFT_CLIENT.player.respawn();
      }

      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player.connection != null) {
         this.Я_at_d90e78f5.щ();
         if (MINECRAFT_CLIENT.options.keyShift.isDown() && MINECRAFT_CLIENT.options.keyJump.isDown()) {
            this.disable();
         } else {
            for (Entity var3 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
               if (var3 instanceof Warden var4) {
                  this.сФ.put(var4.blockPosition().immutable(), MINECRAFT_CLIENT.player.tickCount + 100);
               }
            }

            this.сФ.values().removeIf(var0 -> MINECRAFT_CLIENT.player.tickCount > var0);
            if (this.Яж != null) {
               if (MINECRAFT_CLIENT.player.tickCount >= 20 && MINECRAFT_CLIENT.player.tickCount < 30) {
                  MINECRAFT_CLIENT.player.connection.sendCommand("report " + this.Яж + " чит");
                  this.Яж = null;
               }
            } else if (MINECRAFT_CLIENT.player.tickCount < 5) {
               this.Де = 0;
               this.сО.clear();
            } else if (ServerStatusParser.method_6_a83dd0cc() < 0) {
               if (MINECRAFT_CLIENT.player.tickCount % 100 == 0 && this.юД() >= 0 && MINECRAFT_CLIENT.player.tickCount > 300) {
                  MINECRAFT_CLIENT.player.connection.sendCommand("an" + this.юД());
               }

               this.В = AutoWarden.FarmStage.SAVE;
            } else {
               if (MINECRAFT_CLIENT.player.tickCount % 100 == 0 && WardenFarmArea.Ап() && (this.Х_at_d90e78f5 == null || !this.ЪЙ())) {
                  this.Х_at_d90e78f5 = WardenFarmArea.Л(MINECRAFT_CLIENT.player.blockPosition().getY());
               }

               if (MINECRAFT_CLIENT.player.hasEffect(MobEffects.GLOWING) && this.method_9(ВеГ)) {
                  this.ьн(false);
               } else {
                  switch (this.В) {
                     case SAVE:
                        this.Ж_();
                        break;
                     case TAKE:
                        this.ЖI();
                        break;
                     case COLLECTING:
                        this.Ж诶();
                        break;
                     case ESCAPE:
                        this.ЖС();
                  }

                  if (this.Ъю() && MINECRAFT_CLIENT.player.tickCount % 15 == 0) {
                     BaritoneIntegration.г2();
                  }
               }
            }
         }
      }
   }

   private void method_2(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSoundPacket var2 && MINECRAFT_CLIENT.player != null) {
         SoundEvent var10 = var2.getSound().value();
         String var11 = var10.location().getPath();
         if (var11.contains(Вел)
            || var11.contains(Веэ)
            || var11.contains(Вею)
            || var10 == SoundEvents.WARDEN_ROAR
            || var10 == SoundEvents.WARDEN_ANGRY
            || var10 == SoundEvents.WARDEN_SONIC_BOOM
            || var10 == SoundEvents.WARDEN_SONIC_CHARGE) {
            this.Де = MINECRAFT_CLIENT.player.tickCount + 100;
         }
      } else if (var1.getPacket() instanceof ClientboundSystemChatPacket var7) {
         String var9 = var7.content().getString();
         if (var9.contains(ВеЩ)) {
            this.еЬ = true;
            if (this.ВI.getValue() && MINECRAFT_CLIENT.player != null && var9.contains(Веу)) {
               StringBuilder var4 = new StringBuilder();

               for (MobEffectInstance var6 : MINECRAFT_CLIENT.player.getActiveEffects()) {
                  var4.append(var6.getEffect().value().getDisplayName().getString()).append(' ');
               }

               ClientChat.З("Эффекты при смерти: " + (var4.isEmpty() ? Ве2 : var4.toString().trim()));
               if (!MINECRAFT_CLIENT.player.hasEffect(MobEffects.GLOWING) && !this.я(Веф)) {
                  this.Яж = var9.split(Веи)[1].split(Ве9)[0].trim();
               }
            }
         }
      }
   }

   public List<Integer> т() {
      return this.сТ;
   }

   private void я(ContainerScreen var1) {
      if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         boolean var2 = false;
         boolean var3 = false;
         int var4 = 0;

         for (Slot var6 : var1.getMenu().slots) {
            if (var4 >= 4) {
               return;
            }

            ItemStack var7 = var6.getItem();
            if (var6.container == MINECRAFT_CLIENT.player.getInventory() && !var7.isEmpty() && (!this.ВК.getValue() || !WardenLootItemClassifier.isSpeedPotion(var7))) {
               if (!var2 && WardenLootItemClassifier.hasInvisibilityPotionType(var7)) {
                  var2 = true;
               } else if (!var3 && var7.is(Items.GOLDEN_CARROT)) {
                  var3 = true;
               } else {
                  this.ю(var1, var6, 0, ContainerInput.QUICK_MOVE);
                  var4++;
               }
            }
         }
      }
   }

   private BlockPos method_9(BlockPos var1) {
      double var2 = MINECRAFT_CLIENT.player.tickCount / Вея * ВАс;
      return new BlockPos(
         WardenFarmArea.Л(var1.getX() + (int)(Math.cos(var2) * ВАВ), this.Х_at_d90e78f5),
         var1.getY(),
         WardenFarmArea.А(var1.getZ() + (int)(Math.sin(var2) * ВАЯ), this.Х_at_d90e78f5)
      );
   }

   private void ьЕ(boolean var1) {
      for (Block var3 : BuiltInRegistries.BLOCK) {
         if (var3.defaultBlockState().is(BlockTags.CANDLES)) {
            BaritoneIntegration.Ъ(var3, var1);
         }
      }
   }

   private BlockPos Й() {
      BlockPos var1 = null;
      long var2 = ВАХ;

      for (BlockPos var5 : this.Я_at_d90e78f5.о_2571477d()) {
         long var6 = this.Я_at_d90e78f5.诶_6da43b94(var5);
         if (var6 >= 0L && var6 < var2 && this.ъ(var5) && !this.Г(var5) && !this.В(var5)) {
            var2 = var6;
            var1 = var5;
         }
      }

      return var1;
   }

   private boolean я(BlockPos var1, BlockPos var2) {
      return this.ю(Vec3.atCenterOf(var1).add(0.0, MINECRAFT_CLIENT.player.getEyeHeight() - ВАЛ, 0.0), var2) != null;
   }

   private boolean ЪЪ() {
      return MINECRAFT_CLIENT.player.getDeltaMovement().horizontalDistanceSqr() > ВАЩ;
   }

   private boolean ы(BlockPos var1) {
      if (MINECRAFT_CLIENT.level.getBlockState(var1.below()).is(Blocks.HOPPER)) {
         return true;
      }

      BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(var1);
      if (var2.hasProperty(BlockStateProperties.CHEST_TYPE) && var2.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) {
         for (Direction var4 : Plane.HORIZONTAL) {
            BlockPos var5 = var1.relative(var4);
            BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(var5);
            if (var6.is(Blocks.CHEST)
               && var6.hasProperty(BlockStateProperties.CHEST_TYPE)
               && var6.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE
               && var6.getValue(BlockStateProperties.CHEST_TYPE) != var2.getValue(BlockStateProperties.CHEST_TYPE)
               && var6.getValue(BlockStateProperties.HORIZONTAL_FACING) == var2.getValue(BlockStateProperties.HORIZONTAL_FACING)
               && MINECRAFT_CLIENT.level.getBlockState(var5.below()).is(Blocks.HOPPER)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean М(Player var1) {
      for (EquipmentSlot var5 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         ItemStack var6 = var1.getItemBySlot(var5);
         Equippable var7 = var6.get(DataComponents.EQUIPPABLE);
         if (var7 != null && var7.slot().getType() == Type.HUMANOID_ARMOR) {
            return true;
         }
      }

      return false;
   }

   private boolean ЪO() {
      if (MINECRAFT_CLIENT.player.tickCount >= this.Де) {
         return false;
      }

      for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var2 instanceof Warden var3) {
            double var4 = MINECRAFT_CLIENT.player.distanceToSqr(var3);
            if (var4 < ВеЖ && this.я(var3) && (var4 < Вем || this.ю(var3))) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean Г(BlockPos var1) {
      for (BlockPos var3 : this.сФ.keySet()) {
         if (var3.distSqr(var1) < ВАЫ) {
            return true;
         }
      }

      return false;
   }

   private void ьн(boolean var1) {
      this.ЪИ();
      this.Ж4();
      BlockPos var2 = null;
      double var3 = Ве0;
      int var5 = MINECRAFT_CLIENT.player.blockPosition().getY();

      for (int var6 = 0; var6 < 360; var6 += 30) {
         int var7 = WardenFarmArea.Л((int)(MINECRAFT_CLIENT.player.getX() + Math.cos(Math.toRadians(var6)) * Вех), this.Х_at_d90e78f5);
         int var8 = WardenFarmArea.А((int)(MINECRAFT_CLIENT.player.getZ() + Math.sin(Math.toRadians(var6)) * ВеУ), this.Х_at_d90e78f5);
         double var9 = this.ю(var7, var8, var1);
         if (var9 > var3) {
            var3 = var9;
            var2 = new BlockPos(var7, var5, var8);
         }
      }

      this.method_5(var2);
   }

   private boolean method_9(double var1) {
      double var3 = var1 * var1;

      for (Player var6 : MINECRAFT_CLIENT.level.players()) {
         if (var6 != MINECRAFT_CLIENT.player && MINECRAFT_CLIENT.player.distanceToSqr(var6) < var3) {
            return true;
         }
      }

      return false;
   }

   private int юЬ() {
      int var1 = 0;

      for (ItemStack var3 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
         if (WardenLootItemClassifier.hasInvisibilityPotionType(var3)) {
            var1++;
         }
      }

      return var1;
   }

   private void Ж诶() {
      if (!this.Ъф()) {
         if (this.сТ.size() <= 1) {
            if (MINECRAFT_CLIENT.player.tickCount % 20 == 0) {
               ClientChat.З(ВеР);
            }
         } else {
            if (this.ДХ >= this.сТ.size()) {
               this.ДХ = 1;
            }

            int var1 = this.сТ.get(this.ДХ);
            if (ServerStatusParser.method_6_a83dd0cc() != var1) {
               if (MINECRAFT_CLIENT.player.tickCount % 10 == 0 && MINECRAFT_CLIENT.player.tickCount > 10) {
                  MINECRAFT_CLIENT.player.connection.sendCommand("an" + var1);
               }
            } else {
               if (MINECRAFT_CLIENT.player.tickCount > 5) {
                  this.Ж西();
               }
            }
         }
      }
   }

   private int З(int var1) {
      double var2 = this.сД.isSelected(Ве3) ? Вер : (this.сД.isSelected(Ве6) ? Ве_ : 1.0);
      return (int)(var1 * var2);
   }

   private boolean ю(Warden var1) {
      return (MINECRAFT_CLIENT.player.getX() - var1.getX()) * (var1.getX() - var1.xo) + (MINECRAFT_CLIENT.player.getZ() - var1.getZ()) * (var1.getZ() - var1.zo) > ВеК;
   }

   private boolean Б(ItemStack var1) {
      return var1.isEmpty()
         ? false
         : WardenLootItemClassifier.hasInvisibilityPotionType(var1) && this.юЬ() < 1
            || var1.is(Items.GOLDEN_CARROT) && InventoryInteractions.щ(Items.GOLDEN_CARROT) < 3
            || this.ВК.getValue() && WardenLootItemClassifier.isSpeedPotion(var1) && this.ю(WardenLootItemClassifier::isSpeedPotion) < 0;
   }

   private void ю(ContainerScreen var1) {
      if (this.ЪЪ()) {
         BaritoneIntegration.г2();
      } else if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         Slot var2 = this.ю(var1, false, var1x -> !var1x.isEmpty() && !WardenLootItemClassifier.shouldSkipLoot(var1x, this.сД.getSelectedOption().getName()));
         if (var2 == null) {
            this.ЪИ();
         } else {
            this.ю(var1, var2, 0, ContainerInput.QUICK_MOVE);
         }
      }
   }

   private Slot ю(ContainerScreen var1, boolean var2, Predicate<ItemStack> var3) {
      for (Slot var5 : var1.getMenu().slots) {
         if (var5.container == MINECRAFT_CLIENT.player.getInventory() == var2 && var3.test(var5.getItem())) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public void onDisable() {
      BaritoneIntegration.Ъ(Веч, Boolean.valueOf(false));
      BaritoneIntegration.Ъ(ВеЭ, Boolean.valueOf(false));
      BaritoneIntegration.Ъ(Ве必, Boolean.valueOf(false));
      BaritoneIntegration.Ъ(Вещ, Integer.valueOf(3));
      this.ьЕ(false);
      BaritoneIntegration.г2();
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.е_at_d90e78f5);
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.А_at_d90e78f5);
      this.е衣 = false;
      super.onDisable();
   }

   private boolean ЪЙ() {
      return this.Х_at_d90e78f5 != null
         && MINECRAFT_CLIENT.player.getX() >= this.Х_at_d90e78f5.minX
         && MINECRAFT_CLIENT.player.getX() <= this.Х_at_d90e78f5.maxX
         && MINECRAFT_CLIENT.player.getZ() >= this.Х_at_d90e78f5.minZ
         && MINECRAFT_CLIENT.player.getZ() <= this.Х_at_d90e78f5.maxZ;
   }

   @Override
   public void onEnable() {
      int var1 = ServerStatusParser.method_6_a83dd0cc();
      if (var1 >= 0) {
         this.сТ.remove(Integer.valueOf(var1));
         this.сТ.add(0, var1);
      }

      this.ДХ = 1;
      this.В = AutoWarden.FarmStage.COLLECTING;
      this.сФ.clear();
      WardenESP var2 = RoseClient.getInstance().getServices().getModules().getWardenESP();
      if (var2 != null && !var2.isEnabled()) {
         var2.enable();
      }

      ClientChat.З(Ве8);
      BaritoneIntegration.Ъ(Ве5, Boolean.valueOf(true));
      BaritoneIntegration.Ъ(Вев, Integer.valueOf(256));
      BaritoneIntegration.Ъ(Веъ, ВеМ);
      BaritoneIntegration.Ъ(ВеН, Boolean.valueOf(true));
      BaritoneIntegration.Ъ(Вен, 1.0);
      BaritoneIntegration.Ъ(ВеЦ, 1.0);
      this.ьЕ(true);
      super.onEnable();
   }

   private boolean В(BlockPos var1) {
      for (Player var3 : MINECRAFT_CLIENT.level.players()) {
         if (var3 != MINECRAFT_CLIENT.player && !(var3.distanceToSqr(Vec3.atCenterOf(var1)) >= ВАl) && this.М(var3)) {
            return true;
         }
      }

      return false;
   }

   private void ЖI() {
      if (this.еЬ && this.сТ.size() > 1) {
         this.ДХ++;
         if (this.ДХ >= this.сТ.size()) {
            this.ДХ = 1;
         }

         this.еЬ = false;
      }

      this.Де = 0;
      this.сО.clear();
      if (this.Ъ9()) {
         this.ю(this.Ън() || this.Ъл(), false, AutoWarden.FarmStage.COLLECTING);
      }
   }

   private void method_9(ContainerScreen var1) {
      if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
         ItemStack var2 = var1.getMenu().getCarried();
         Predicate<ItemStack> var3 = var1x -> var1x.isEmpty() || ItemStack.isSameItemSameComponents(var1x, var2);
         if (!var2.isEmpty()) {
            if (!this.Б(var2)) {
               this.ю(var1, this.ю(var1, false, var3), 0, ContainerInput.PICKUP);
            } else {
               this.ю(var1, this.ю(var1, true, var3), 1, ContainerInput.PICKUP);
            }
         } else {
            this.ю(var1, this.ю(var1, false, this::Б), 0, ContainerInput.PICKUP);
         }
      }
   }

   private boolean Ъф() {
      boolean var1 = this.ЪO();
      if ((
            var1
               || this.ю7() > this.З(20)
               || MINECRAFT_CLIENT.player.getFoodData().getFoodLevel() < 8
               || this.сО.values().stream().filter(var0 -> var0 >= 2).count() >= ВеШ && MINECRAFT_CLIENT.player.tickCount % 30 == 0
         )
         && MINECRAFT_CLIENT.player.tickCount > 100) {
         if (var1) {
            this.еЬ = true;
         }

         this.В = AutoWarden.FarmStage.ESCAPE;
         return true;
      } else if (!ServerStatusParser.З_bbeebd87() && this.ю7() > this.З(8)) {
         this.В = AutoWarden.FarmStage.ESCAPE;
         return true;
      } else {
         int var2 = ServerStatusParser.ц();
         if (var2 >= 0 && var2 < 7 && !this.method_9(Веы) && this.ю7() > this.З(7)) {
            this.В = AutoWarden.FarmStage.ESCAPE;
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean я(BlockPos var1, double var2) {
      double var4 = var2 * var2;

      for (Player var7 : MINECRAFT_CLIENT.level.players()) {
         if (var7 != MINECRAFT_CLIENT.player && var7.distanceToSqr(Vec3.atCenterOf(var1)) < var4) {
            return true;
         }
      }

      return false;
   }

   public AutoWarden() {
      super(Веш, ВеИ, ModuleCategory.MISC);
      this.ВК = new BooleanSetting(Веь, false);
      this.ВI = new BooleanSetting(Веж, false);
      this.сД = new SingleChoiceSetting(Ве弟, 1, new ChoiceOption(Веб), new ChoiceOption(ВеЫ), new ChoiceOption(Веl));
      this.сТ = new ArrayList<>();
      this.сО = new HashMap<>();
      this.сФ = new HashMap<>();
      this.Я_at_d90e78f5 = new TimedChestTracker();
      this.е_at_d90e78f5 = new FixedAngleRotation();
      this.А_at_d90e78f5 = new FixedAngleRotation();
      this.Ц = new Stopwatch();
      this.Яц = this::ье;
      this.Яя = this::method_2;
      this.Хс = this::Д;
      this.ДХ = 1;
      this.В = AutoWarden.FarmStage.SAVE;
      this.addSettings(this.ВК, this.ВI, this.сД);
   }

   private int ю7() {
      int var1 = 0;

      for (ItemStack var3 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
         if (!var3.isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private void ю(ContainerScreen var1, Slot var2, int var3, ContainerInput var4) {
      if (var2 != null) {
         MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.getMenu().containerId, var2.index, var3, var4, MINECRAFT_CLIENT.player);
      }
   }

   private boolean я(double var1) {
      double var3 = var1 * var1;

      for (BlockPos var6 : this.Я_at_d90e78f5.о_2571477d()) {
         if (MINECRAFT_CLIENT.player.distanceToSqr(Vec3.atCenterOf(var6)) <= var3) {
            return true;
         }
      }

      return false;
   }

   private void method_5(BlockPos var1) {
      if (var1 != null) {
         if (MINECRAFT_CLIENT.player.tickCount % 10 == 0 || !this.ЪЪ() && MINECRAFT_CLIENT.player.tickCount % 5 == 0) {
            BaritoneIntegration.ы(WardenFarmArea.Л(var1.getX(), this.Х_at_d90e78f5), var1.getY(), WardenFarmArea.А(var1.getZ(), this.Х_at_d90e78f5));
         }
      }
   }

   private boolean Ън() {
      return this.юЬ() < 1 || InventoryInteractions.щ(Items.GOLDEN_CARROT) < 3 || this.ВК.getValue() && this.ю(WardenLootItemClassifier::isSpeedPotion) < 0;
   }

   private BlockPos ю(boolean var1) {
      BlockPos var2 = MINECRAFT_CLIENT.player.blockPosition();
      MutableBlockPos var3 = new MutableBlockPos();

      for (int var4 = -4; var4 <= 4; var4++) {
         for (int var5 = -4; var5 <= 4; var5++) {
            for (int var6 = -4; var6 <= 4; var6++) {
               var3.set(var2.getX() + var4, var2.getY() + var5, var2.getZ() + var6);
               if (MINECRAFT_CLIENT.level.getBlockState(var3).is(Blocks.CHEST) && this.ы(var3) == var1 && MINECRAFT_CLIENT.level.getBlockState(var3.above()).isAir()) {
                  BlockHitResult var7 = MINECRAFT_CLIENT.level
                     .clip(
                        new ClipContext(
                           MINECRAFT_CLIENT.player.getEyePosition(), Vec3.atCenterOf(var3), net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player
                        )
                     );
                  if (var7.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK && var7.getBlockPos().equals(var3)) {
                     return var3.immutable();
                  }
               }
            }
         }
      }

      return null;
   }

   private void Ж西() {
      MobEffectInstance var1 = MINECRAFT_CLIENT.player.getEffect(MobEffects.INVISIBILITY);
      boolean var2 = MINECRAFT_CLIENT.player.hasEffect(MobEffects.GLOWING) || var1 != null && var1.getDuration() >= 400;
      if (!var2 && var1 == null && this.юЬ() < 1 && MINECRAFT_CLIENT.player.tickCount % 5 == 0 && !ServerStatusParser.З_bbeebd87()) {
         this.В = AutoWarden.FarmStage.ESCAPE;
      } else {
         if (!var2) {
            this.Ж弟();
         }

         if (!WardenFarmArea.Ап()) {
            if (MINECRAFT_CLIENT.player.tickCount % 50 == 0) {
               MINECRAFT_CLIENT.player.connection.sendCommand(Вей);
            }
         } else if (var2) {
            int var3 = this.ВК.getValue() && MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED) == null ? this.ю(WardenLootItemClassifier::isSpeedPotion) : -1;
            if (var3 < 0) {
               this.Жl();
            } else {
               this.ьO(var3);
            }
         }
      }
   }

   private void Ж4() {
      if (!MINECRAFT_CLIENT.player.getMainHandItem().isEmpty()) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (MINECRAFT_CLIENT.player.getInventory().getItem(var1).isEmpty()) {
               MINECRAFT_CLIENT.player.getInventory().selected = var1;
               return;
            }
         }

         for (int var2 = 9; var2 < 36; var2++) {
            if (MINECRAFT_CLIENT.player.getInventory().getItem(var2).isEmpty()) {
               if (MINECRAFT_CLIENT.player.tickCount % 10 >= 2 && this.ЪЪ()) {
                  BaritoneIntegration.г2();
               }

               if (MINECRAFT_CLIENT.player.tickCount % 10 == 4) {
                  MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var2, MINECRAFT_CLIENT.player.getInventory().selected, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               }

               return;
            }
         }
      }
   }

   private void Ж_() {
      if (this.юД() >= 0 && ServerStatusParser.method_6_a83dd0cc() != this.юД() && !ServerStatusParser.З_bbeebd87() && MINECRAFT_CLIENT.player.tickCount % 5 == 0 && MINECRAFT_CLIENT.player.tickCount > 5) {
         MINECRAFT_CLIENT.player.connection.sendCommand("an" + this.юД());
      }

      if (this.Ъ9()) {
         this.Ж4();
         this.ю(this.ЪЕ(), true, AutoWarden.FarmStage.TAKE);
      }
   }

   @Override
   public JsonObject saveConfig() {
      JsonObject var1 = super.saveConfig();
      JsonArray var2 = new JsonArray();

      for (int var4 : this.сТ) {
         var2.add(var4);
      }

      var1.add(ВАу, var2);
      return var1;
   }

   private boolean ЪЕ() {
      boolean var1 = false;
      boolean var2 = false;

      for (ItemStack var4 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
         if (!var4.isEmpty() && (!this.ВК.getValue() || !WardenLootItemClassifier.isSpeedPotion(var4))) {
            if (var1 || !WardenLootItemClassifier.hasInvisibilityPotionType(var4)) {
               if (var2 || !var4.is(Items.GOLDEN_CARROT)) {
                  return true;
               }

               var2 = true;
            } else {
               var1 = true;
            }
         }
      }

      return false;
   }

   private BlockPos ф(BlockPos var1) {
      if (var1 == null) {
         return null;
      }

      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            if (var2 != 0 || var3 != 0) {
               BlockPos var4 = var1.offset(var2, 0, var3);
               if (MINECRAFT_CLIENT.level.getBlockState(var4).isAir()
                  && MINECRAFT_CLIENT.level.getBlockState(var4.above()).isAir()
                  && !MINECRAFT_CLIENT.level.getBlockState(var4.below()).isAir()
                  && this.я(var4, var1)) {
                  return var4;
               }
            }
         }
      }

      return MINECRAFT_CLIENT.level.getBlockState(var1.above()).isAir() && MINECRAFT_CLIENT.level.getBlockState(var1.above(2)).isAir() && this.я(var1.above(), var1) ? var1.above() : null;
   }

   private boolean я(BlockPos var1, int var2) {
      if (var1 != null && !(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen)) {
         Vec3 var3 = MINECRAFT_CLIENT.player.getEyePosition();
         Vec3 var4 = this.ю(var3, var1);
         if (var4 == null) {
            return false;
         }

         Vec3 var5 = var4.subtract(var3).normalize();
         float var6 = (float)Math.toDegrees(Math.atan2(-var5.x, var5.z));
         float var7 = (float)Math.toDegrees(-Math.asin(Mth.clamp(var5.y, ВАв, 1.0)));
         float var8 = MINECRAFT_CLIENT.player.tickCount + MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var9 = (float)((Math.sin(var8 * ВАъ) * ВАМ + Math.sin(var8 * ВАН + ВАн) * ВАЦ + Math.sin(var8 * ВАч + ВАЭ) * ВА必) * ВАщ);
         this.е_at_d90e78f5.setTargetAngles(var6 + var9, Mth.clamp(var7 + var9 / ВАГ, ВАл, ВАэ));
         RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.е_at_d90e78f5, 3, 1);
         if (MINECRAFT_CLIENT.player.tickCount % var2 == 0 && !(Math.hypot(Mth.wrapDegrees(var6 - MINECRAFT_CLIENT.player.getYRot()), var7 - MINECRAFT_CLIENT.player.getXRot()) > ВАю)) {
            BlockHitResult var10 = MINECRAFT_CLIENT.level.clip(new ClipContext(var3, var4, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
            if (var10.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK && var10.getBlockPos().equals(var1)) {
               MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var10);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean ъ(BlockPos var1) {
      return this.ф(var1) != null;
   }

   private boolean Ъю() {
      BlockState var1 = MINECRAFT_CLIENT.level.getBlockState(MINECRAFT_CLIENT.player.blockPosition());
      BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(MINECRAFT_CLIENT.player.blockPosition().below());
      return !var1.is(BlockTags.CANDLES) && !var2.is(BlockTags.CANDLES)
         ? !this.ЪO() && this.В == AutoWarden.FarmStage.COLLECTING && WardenFarmArea.Ап() && MINECRAFT_CLIENT.gui.screen() == null && this.Ъя()
         : true;
   }

   private void ЖС() {
      if (this.Ъ9()) {
         this.В = AutoWarden.FarmStage.SAVE;
      } else if (this.ЪO() && ServerStatusParser.З_bbeebd87()) {
         this.ьн(true);
      } else {
         BlockPos var1 = this.Ъ_10b53497();
         if (!(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen)
            && (var1 == null || this.Я_at_d90e78f5.诶_6da43b94(var1) >= 0L || !(MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= Век))) {
            if (!ServerStatusParser.З_bbeebd87()) {
               this.В = AutoWarden.FarmStage.SAVE;
            } else {
               if (this.ю7() < 23 && !this.method_9(Вег) && ServerStatusParser.ц() > 16 && var1 != null) {
                  this.Жl();
               } else {
                  this.ьн(true);
               }
            }
         } else {
            this.Жl();
         }
      }
   }

   private void ьO(int var1) {
      this.А_at_d90e78f5.setTargetAngles(MINECRAFT_CLIENT.player.getYRot(), ВА8);
      if (RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.А_at_d90e78f5, 10, 1)) {
         this.е衣 = true;
         if (!(Math.abs(ВА5 - MINECRAFT_CLIENT.player.getXRot()) > 1.0F)) {
            int var2 = MINECRAFT_CLIENT.player.getInventory().selected;
            if (var1 < 9) {
               InventoryInteractions.Ъ_de5a6f78(var1);
               MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
            } else {
               MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               InventoryInteractions.Ъ_de5a6f78(var2);
               MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            }

            InventoryInteractions.Ъ_de5a6f78(var2);
            RoseClient.getInstance().getServices().getRotations().expireScheduledStrategy(this.А_at_d90e78f5);
            this.е衣 = false;
         }
      }
   }

   private void Ж弟() {
      int var1 = this.ю(WardenLootItemClassifier::hasInvisibilityPotionType);
      if (var1 >= 0 && MINECRAFT_CLIENT.player.tickCount > 20) {
         this.ьO(var1);
      }
   }

   private BlockPos Ъ_10b53497() {
      BlockPos var1 = null;
      byte var2 = 99;
      double var3 = ВАе;

      for (BlockPos var6 : this.Я_at_d90e78f5.о_2571477d()) {
         if (this.ъ(var6) && !this.В(var6)) {
            double var7 = MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var6));
            long var9 = this.Я_at_d90e78f5.诶_6da43b94(var6);
            if ((!this.Г(var6) || var9 < 0L && var7 <= ВАА) && (var9 >= 0L || this.сО.getOrDefault(var6, 0) < 3)) {
               byte var11;
               if (var9 < 0L && var7 <= ВА4) {
                  var11 = 0;
               } else if (var9 >= 0L && var9 <= ВАп && var7 <= ВАД) {
                  var11 = 1;
               } else if (var9 < 0L && var7 <= ВАЪ) {
                  var11 = 2;
               } else if (var9 >= 0L && var9 <= ВАт && var7 <= ВАа) {
                  var11 = 3;
               } else {
                  if (var9 >= 0L) {
                     continue;
                  }

                  var11 = 4;
               }

               double var12 = Vec3.atCenterOf(var6).y - MINECRAFT_CLIENT.player.getEyeY();
               double var14 = var6.getX() + ВАЮ - MINECRAFT_CLIENT.player.getX();
               double var16 = var6.getZ() + ВАЧ - MINECRAFT_CLIENT.player.getZ();
               double var18 = var14 * var14 + var16 * var16 + (var12 > 0.0 ? 2 : 1) * var12 * var12;
               if (var11 < var2 || var11 == var2 && var18 < var3) {
                  var2 = var11;
                  var3 = var18;
                  var1 = var6;
               }
            }
         }
      }

      return var1;
   }

   private void Жl() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var9) {
         this.ю(var9);
      } else {
         BlockPos var1 = this.Ъ_10b53497();
         if (var1 == null) {
            var1 = this.Й();
         }

         boolean var10 = var1 != null && this.ж != null && !var1.equals(this.ж) && this.Я_at_d90e78f5.诶_6da43b94(this.ж) > ВеЕ;
         if (!var10) {
            this.Ц.ВД();
         }

         if (!var10 || this.Ц.method_0(ВеП)) {
            this.ж = var1;
         }

         BlockPos var3 = this.ж;
         if (var3 == null && MINECRAFT_CLIENT.player.tickCount % 40 == 0) {
            this.В = AutoWarden.FarmStage.ESCAPE;
            this.еЬ = true;
         } else if (var3 != null) {
            long var4 = this.Я_at_d90e78f5.诶_6da43b94(var3);
            if (var4 > Ве西 && this.я(var3, Вед)) {
               BlockPos var11 = this.ф(var3);
               if (var11 != null) {
                  if (MINECRAFT_CLIENT.player.distanceToSqr(Vec3.atCenterOf(var11)) > ВеБ) {
                     this.method_5(var11);
                  } else {
                     BaritoneIntegration.г2();
                  }
               }
            } else if (var4 > ВеС) {
               this.method_5(this.method_9(var3));
            } else {
               double var6 = MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var3));
               if (var6 <= Вез) {
                  int var8 = var4 >= 0L ? 1 : 3;
                  if (this.сО.getOrDefault(var3, 0) < var8 && this.я(var3, var4 >= 0L ? 6 : 1)) {
                     this.сО.merge(var3, 1, Integer::sum);
                  }
               } else {
                  if (var6 > Вец) {
                     this.Ж4();
                  }

                  this.method_5(this.ф(var3));
               }
            }
         }
      }
   }

   private boolean Ъя() {
      AABB var1 = MINECRAFT_CLIENT.player.getBoundingBox().inflate(ВеO, 0.0, Ве7);

      for (BlockPos var3 : BlockPos.betweenClosed(BlockPos.containing(var1.minX, var1.minY, var1.minZ), BlockPos.containing(var1.maxX, var1.maxY, var1.maxZ))) {
         if (!MINECRAFT_CLIENT.level.getBlockState(var3).isAir()) {
            return true;
         }
      }

      return false;
   }

   private Vec3 ю(Vec3 var1, BlockPos var2) {
      Vec3 var3 = Vec3.atCenterOf(var2);
      Vec3 var4 = null;
      double var5 = ВА诶;

      for (double var7 = ВА1; var7 <= ВАЬ; var7 += ВАб) {
         for (double var9 = ВА衣; var9 <= ВАш; var9 += ВА弟) {
            for (double var11 = ВАИ; var11 <= ВАь; var11 += ВАж) {
               Vec3 var13 = var3.add(var7, var9, var11);
               double var14 = var13.distanceToSqr(var3);
               if (!(var14 >= var5)) {
                  BlockHitResult var16 = MINECRAFT_CLIENT.level.clip(new ClipContext(var1, var13, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
                  if (var16.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK && var16.getBlockPos().equals(var2)) {
                     var5 = var14;
                     var4 = var13;
                  }
               }
            }
         }
      }

      return var4;
   }

   private enum FarmStage {
      SAVE,
      TAKE,
      COLLECTING,
      ESCAPE;
   }
}
