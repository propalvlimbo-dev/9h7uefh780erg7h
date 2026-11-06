package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.ec.PlayerCameraPositionSmoothing;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.ec.HiddenBlockPositions;
import sg.mixin.MinecraftRightClickDelayAccessor;
import sg.mixin.MultiPlayerGameModeAccessor;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.events.impl.MovementInputEvent;
import sg.settings.SingleChoiceSetting;

public class NoClip extends ClientModule {
   private static final int Ъб = 1;
   private static final int ЪЫ = 2;
   private static final long Хд_6da43b94 = 8000L;
   private static final Set<BlockPos> ф = ConcurrentHashMap.newKeySet();
   private final SingleChoiceSetting съ;
   private final FloatSetting е4;
   private final FloatSetting еп;
   private final FloatSetting еД;
   private final FloatSetting еЪ;
   private final BooleanSetting ЯМ;
   private final FloatSetting ет;
   private final BooleanSetting ЯН;
   private final BooleanSetting Ян;
   private final BooleanSetting ЯЦ;
   private final BooleanSetting Яч;
   private final BooleanSetting ЯЭ;
   private final BooleanSetting Я必;
   private final BooleanSetting Ящ;
   private final BooleanSetting ЯГ;
   private final BooleanSetting Ял;
   private final BooleanSetting Яэ;
   private final EventListener<PlayerTickEvent.Post> ХУ;
   private final FloatSetting еа;
   private final Stopwatch щ;
   private final Stopwatch Г;
   private final Stopwatch л;
   private final List<BlockPos> ВЪ;
   private final FloatSetting еЮ;
   private final BooleanSetting Яю;
   private final EventListener<PacketReceiveEvent> ХЗ;
   private final List<BlockPos> Вт;
   private final Map<BlockPos, Long> В4;
   private final Stopwatch э;
   private final CopyOnWriteArrayList<Packet<?>> Ъ;
   private final Stopwatch ю;
   private final EventListener<PlayerMovementSyncEvent> ХЕ;
   private BlockPos l;
   private long ХБ;
   private int Ъl;
   private boolean А衣;
   @SerializedName("8")
   private BlockPos field_8;
   private long ХС;
   private long Хз;
   private boolean Аш;
   private boolean АИ;
   private int Ъ8;
   @SerializedName("5")
   private BlockPos field_5;
   private boolean Аь;
   private final EventListener<PacketSendEvent> ХП;
   private boolean Аж;
   private boolean А弟;
   private final EventListener<MovementInputEvent> Х西;
   private long Хц;
   @SerializedName("Хд")
   private final EventListener<PlayerTickEvent> Хд_f8db0d09;
   private static final String с7с = "NoClip";
   private static final String с7В = "Позволяет вам ходить через стены и двери";
   private static final String с7Я = "Режим";
   private static final String с7Х = "Блоки";
   private static final String с7е = "Флай (RW)";
   private static final String с7А = "Двери";
   private static final String с74 = "Задержка";
   private static final float с7п = 500.0F;
   private static final float с7Д = 500.0F;
   private static final float с7Ъ = 50.0F;
   private static final String с7т = "Задержка прыжка";
   private static final float с7а = 10.0F;
   private static final String с7Ю = "Скорость ломания";
   private static final float с7Ч = 0.1F;
   private static final float с7Л = 0.05F;
   private static final String с7诶 = "Задержка ПКМ (мс)";
   private static final float с71 = 400.0F;
   private static final float с7Ь = 2000.0F;
   private static final float с7衣 = 10.0F;
   private static final String с7ш = "Увеличивать пинг";
   private static final String с7И = "Пинг)";
   private static final float с7ь = 200.0F;
   private static final float с7ж = 50.0F;
   private static final float с7弟 = 2000.0F;
   private static final float с7б = 25.0F;
   private static final String с7Ы = "Менять порядок ломания";
   private static final String с7l = "Ломать только один блок перед собой в позе лежа";
   private static final String с78 = "Сквозь люки";
   private static final String с75 = "Бурмалда";
   private static final String с7в = "Анти-лежак";
   private static final String с7ъ = "Байпассик дорогой";
   private static final String с7М = "Бурмалдаяц";
   private static final String с7Н = "Ломать блоки в которых ты находишься";
   private static final String с7н = "Анти-фантом";
   private static final String с7Ц = "Фикс усадки хитбокса";
   private static final String с7ч = "Интервал перелома (мс)";
   private static final float с7Э = 200.0F;
   private static final float с7必 = 2000.0F;
   private static final float с7щ = 10.0F;
   private static final String с7Г = "БПС";
   private static final float с7л = 12.0F;
   private static final float с7э = 20.0F;
   private static final String с7ю = "Скрывать ломание";
   private static final String с7Щ = "Блоки";
   private static final String с7у = "Блоки 2";
   private static final String с72 = "Блоки";
   private static final String с7ф = "Блоки 2";
   private static final String с7и = "Двери";
   private static final String с79 = "Блоки";
   private static final String с7о = "Блоки 2";
   private static final String с7O = "Блоки";
   private static final String с77 = "Блоки 2";
   private static final String с7Р = "Флай (RW)";
   private static final String с73 = "Блоки";
   private static final String с7р = "Блоки 2";
   private static final String с76 = "Двери";
   private static final String с7_ = "Блоки";
   private static final long с7Ш = 50L;
   private static final String с7ы = "Блоки";
   private static final String с7й = "Блоки 2";
   private static final String с7Ж = "Блоки";
   private static final String с7м = "Блоки 2";
   private static final String с7К = "Блоки";
   private static final String с7I = "Блоки";
   private static final String с7Т = "Флай (RW)";
   private static final String с7О = "Блоки";
   private static final String с7Ф = "Блоки 2";
   private static final String с7Й = "Двери";
   private static final String с7к = "Блоки";
   private static final String с7г = "Блоки";
   private static final String с70 = "Блоки 2";
   private static final long с7х = 8000L;
   private static final String с7У = "Блоки";
   private static final String с7З = "Блоки 2";
   private static final String с7Е = "На предмете нет зачарования \"Эффективность 7\", будьте аккуратны тк есть шанс кика.";
   private static final String с7П = "ВНИМАНИЕ !!! ";
   private static final String с7西 = "Для использования модуля нужно зачарование на кирке/топоре/лопате эффективность 7. Иначе модуль будет работать хуже.";
   private static final long с7д = 3000L;
   private static final long с7Б = 8000L;
   private static final long с7С = 200L;
   private static final double с7з = 1000.0;
   private static final long с7ц = 4000L;
   private static final double с7я = Double.MAX_VALUE;
   private static final String сРс = "Блоки";
   private static final String сРВ = "Блоки 2";
   private static final String сРЯ = "Блоки 2";
   private static final String сРХ = "Блоки";
   private static final String сРе = "Блоки 2";
   private static final String сРА = "Блоки";
   private static final String сР4 = "Блоки 2";
   private static final String сРп = "Блоки";
   private static final String сРД = "Блоки 2";
   private static final String сРЪ = "Блоки";
   private static final String сРт = "Блоки";
   private static final String сРа = "Блоки";
   private static final String сРЮ = "Блоки";
   private static final String сРЧ = "Двери";
   private static final String сРЛ = "Двери";
   private static final String сР诶 = "Блоки";
   private static final String сР1 = "Блоки";
   private static final String сРЬ = "Блоки";
   private static final String сР衣 = "Двери";
   private static final String сРш = "Блоки";
   private static final String сРИ = "Флай (RW)";

   private void ыб() {
      if (this.Ъl != -1) {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
            MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(this.Ъl);
            MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
         }

         this.Ъl = -1;
      }
   }

   private boolean Х(BlockState var1) {
      Block var2 = var1.getBlock();
      if (var2 instanceof DoorBlock) {
         return var1.getValue(DoorBlock.OPEN);
      } else if (var2 instanceof FenceGateBlock) {
         return var1.getValue(FenceGateBlock.OPEN);
      } else {
         return var2 instanceof TrapDoorBlock ? var1.getValue(TrapDoorBlock.OPEN) : false;
      }
   }

   private void т(BlockPos var1) {
      ((MinecraftRightClickDelayAccessor)MINECRAFT_CLIENT).setItemUseCooldown(0);
      BlockHitResult var2 = new BlockHitResult(Vec3.atCenterOf(var1), Direction.UP, var1, false);
      MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var2);
      MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
   }

   private boolean method_7(ItemStack var1) {
      return var1.is(ItemTags.PICKAXES) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SHOVELS);
   }

   private Component method_3() {
      return Component.literal(с7П).withStyle(ChatFormatting.RED, ChatFormatting.BOLD).append(Component.literal(с7西).withStyle(ChatFormatting.YELLOW));
   }

   private void ыШ() {
      MINECRAFT_CLIENT.player.setShiftKeyDown(false);
      if (MINECRAFT_CLIENT.player.input != null && MINECRAFT_CLIENT.player.input.keyPresses != null) {
         Input var1 = MINECRAFT_CLIENT.player.input.keyPresses;
         MINECRAFT_CLIENT.player.input.keyPresses = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), false, var1.sprint());
      }
   }

   private void ы6() {
      this.ыб();
      this.field_8 = null;
      this.Хз = System.currentTimeMillis() + (long)(с7з / this.еЮ.getValue().floatValue());
   }

   private void з(BlockPos var1) {
      if (var1 != null && !MINECRAFT_CLIENT.level.isEmptyBlock(var1) && this.п(var1)) {
         this.Ч(var1);
         Direction var2 = this.ь(var1);
         MINECRAFT_CLIENT.gameMode.startDestroyBlock(var1, var2);
         this.ы西();
         MultiPlayerGameModeAccessor var3 = (MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode;
         var3.setBlockBreakingCooldown(0);
         var3.setCurrentBreakingProgress(this.еД.getValue());
         MINECRAFT_CLIENT.gameMode.continueDestroyBlock(var1, var2);
      }
   }

   private void ыя() {
      if (!this.АИ) {
         if (!this.Аш) {
            this.гл(this.Ъ8);
            this.field_5 = this.в_10b53497();
            this.Аш = true;
         }

         this.ыр();
         if (this.Вт.isEmpty() && this.field_8 == null) {
            this.Аш = false;
            this.АИ = true;
            this.э.ВД();
         }
      } else {
         int var1 = this.н(this.в_10b53497());
         if (var1 >= this.Ъ8 || this.э.method_0(с7ц)) {
            this.АИ = false;
            if (this.Ящ.getValue()) {
               this.Ъ8 = this.Ъ8 == 1 ? 2 : 1;
            }
         }
      }
   }

   private void ы西() {
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }
   }

   private void ыд() {
      long var1 = System.currentTimeMillis() - с7х;
      this.В4.forEach((var3, var4) -> {
         if (var4 < var1 && this.В4.remove(var3, var4)) {
            ф.remove(var3);
         }
      });
   }

   private boolean ФП() {
      BlockPos var1 = this.в_10b53497().below();
      return MINECRAFT_CLIENT.level.getBlockState(var1).getBlock() instanceof TrapDoorBlock;
   }

   private void гл(int var1) {
      this.Вт.clear();
      BlockPos var2 = this.в_10b53497();
      Direction var3 = MINECRAFT_CLIENT.player.getDirection();

      for (int var4 = 1; var4 <= var1; var4++) {
         BlockPos var5 = var2.relative(var3, var4);

         for (int var6 = 0; var6 < 2; var6++) {
            BlockPos var7 = var5.above(var6);
            if (!MINECRAFT_CLIENT.level.isEmptyBlock(var7)) {
               this.Вт.add(var7);
            }
         }
      }

      if (this.ЯГ.getValue()) {
         for (int var8 = 0; var8 < 2; var8++) {
            BlockPos var9 = var2.above(var8);
            if (!MINECRAFT_CLIENT.level.isEmptyBlock(var9)) {
               this.Вт.add(var9);
            }
         }
      }
   }

   private void З(MovementInputEvent var1) {
      if (!this.isEnabled()) {
         this.А弟 = false;
      } else if (this.съ.isSelected(с7Т)) {
         this.А弟 = false;
         var1.setSneakPressed(false);
      } else {
         boolean var2 = this.съ.isSelected(с7О) || this.съ.isSelected(с7Ф);
         boolean var3 = this.съ.isSelected(с7Й);
         if (!var2 && !var3) {
            this.А弟 = false;
         } else {
            if (var3 && this.Яч.getValue()) {
               var1.setSneakPressed(true);
            }

            if (var2 && this.Яэ.getValue()) {
               var1.setSneakPressed(false);
            }

            if (this.А弟) {
               var1.setJumpPressed(true);
               this.А弟 = false;
            }
         }
      }
   }

   public NoClip() {
      super(с7с, с7В, ModuleCategory.MOVEMENT);
      this.съ = new SingleChoiceSetting(с7Я, new ChoiceOption(с7Х), new ChoiceOption(с7е), new ChoiceOption(с7А));
      this.е4 = new FloatSetting(с74, с7п, 0.0F, с7Д).withStep(с7Ъ).visibleWhen(() -> this.съ.isSelected(сРИ));
      this.еп = new FloatSetting(с7т, 0.0F, 0.0F, с7а).withStep(1.0F);
      this.еД = new FloatSetting(с7Ю, 1.0F, с7Ч, 1.0F).withStep(с7Л).visibleWhen(() -> this.съ.isSelected(сРш));
      this.еЪ = new FloatSetting(с7诶, с71, 0.0F, с7Ь).withStep(с7衣).visibleWhen(() -> this.съ.isSelected(сР衣));
      this.ЯМ = new BooleanSetting(с7ш, false).visibleWhen(() -> this.съ.isSelected(сРЬ));
      this.ет = new FloatSetting(с7И, с7ь, с7ж, с7弟).withStep(с7б).visibleWhen(this.ЯМ::getValue);
      this.ЯН = new BooleanSetting(с7Ы, false).visibleWhen(() -> this.съ.isSelected(сР1));
      this.Ян = new BooleanSetting(с7l, false).visibleWhen(() -> this.съ.isSelected(сР诶));
      this.ЯЦ = new BooleanSetting(с78, false).visibleWhen(() -> this.съ.isSelected(сРЛ));
      this.Яч = new BooleanSetting(с75, false).visibleWhen(() -> this.съ.isSelected(сРЧ));
      this.ЯЭ = new BooleanSetting(с7в, false).visibleWhen(() -> this.съ.isSelected(сРЮ));
      this.Я必 = new BooleanSetting(с7ъ, false).visibleWhen(() -> this.съ.isSelected(сРа));
      this.Ящ = new BooleanSetting(с7М, false).visibleWhen(() -> this.съ.isSelected(сРт));
      this.ЯГ = new BooleanSetting(с7Н, false).visibleWhen(() -> this.съ.isSelected(сРЪ));
      this.Ял = new BooleanSetting(с7н, false).visibleWhen(() -> this.съ.isSelected(сРп) || this.съ.isSelected(сРД));
      this.Яэ = new BooleanSetting(с7Ц, false).visibleWhen(() -> this.съ.isSelected(сРА) || this.съ.isSelected(сР4));
      this.ХУ = this::М;
      this.еа = new FloatSetting(с7ч, с7Э, 0.0F, с7必).withStep(с7щ).visibleWhen(() -> this.съ.isSelected(сРХ) || this.съ.isSelected(сРе));
      this.щ = new Stopwatch();
      this.Г = new Stopwatch();
      this.л = new Stopwatch();
      this.ВЪ = new ArrayList<>();
      this.еЮ = new FloatSetting(с7Г, с7л, 1.0F, с7э).withStep(1.0F).visibleWhen(() -> this.съ.isSelected(сРЯ));
      this.Яю = new BooleanSetting(с7ю, true).visibleWhen(() -> this.съ.isSelected(сРс) || this.съ.isSelected(сРВ));
      this.ХЗ = this::ж;
      this.Вт = new ArrayList<>();
      this.В4 = new ConcurrentHashMap<>();
      this.э = new Stopwatch();
      this.Ъ = new CopyOnWriteArrayList<>();
      this.ю = new Stopwatch();
      this.ХЕ = new EventListener<PlayerMovementSyncEvent>() {
         private static final String ХЧЩ = "Блоки";
         private static final String ХЧу = "Блоки 2";
         private static final int ХЧ2 = Integer.MIN_VALUE;

         @Override
         public int getPriority() {
            return ХЧ2;
         }

         public void onEvent(PlayerMovementSyncEvent var1) {
            if (NoClip.this.isEnabled() && (NoClip.this.съ.isSelected(ХЧЩ) || NoClip.this.съ.isSelected(ХЧу))) {
               var1.setPitch(0.0F);
            }
         }
      };
      this.Ъl = -1;
      this.Ъ8 = 1;
      this.field_5 = BlockPos.ZERO;
      this.ХП = this::Ф;
      this.Х西 = this::З;
      this.Хд_f8db0d09 = this::г6;
      this.addSettings(this.съ, this.е4, this.еп, this.еД, this.еЪ, this.Яч, this.ЯЦ, this.ЯЭ, this.еЮ, this.Яю, this.Яэ, this.еа);
   }

   private int Д(BlockState var1) {
      int var2 = -1;
      float var3 = 1.0F;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = MINECRAFT_CLIENT.player.getInventory().getItem(var4);
         if (!var5.isEmpty() && this.西(var5)) {
            float var6 = var5.getDestroySpeed(var1);
            int var7 = this.в(var5);
            if (var6 > 1.0F && var7 > 0) {
               var6 += var7 * var7 + 1.0F;
            }

            if (var6 > var3) {
               var3 = var6;
               var2 = var4;
            }
         }
      }

      return var2;
   }

   private void ыЬ() {
      if (this.Г.method_0(this.еЪ.getValue().longValue())) {
         BlockPos var1 = this.ы_10b53497();
         if (var1 != null && !MINECRAFT_CLIENT.level.isEmptyBlock(var1)) {
            BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(var1);
            if (this.в(var2) && !this.Х(var2)) {
               this.т(var1);
            }

            this.Г.ВД();
         }
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.noPhysics = false;
      }

      PlayerCameraPositionSmoothing.Ъ7();
      this.ыЛ();
      this.Вт.clear();
      this.field_8 = null;
      ф.clear();
      this.В4.clear();
      HiddenBlockPositions.弟.clear();
      this.А弟 = false;
      this.Аш = false;
      this.АИ = false;
      this.Ъ8 = 1;
      this.field_5 = BlockPos.ZERO;
      this.ыб();
      this.ы必();
      if (MINECRAFT_CLIENT.player != null && (this.съ.isSelected(с72) || this.съ.isSelected(с7ф) || this.съ.isSelected(с7и))) {
         this.ыШ();
      }
   }

   private boolean 西(ItemStack var1) {
      return var1.is(ItemTags.PICKAXES) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SHOVELS) || var1.is(ItemTags.HOES);
   }

   private Component Ъ_f022d65c() {
      return Component.literal(с7Е).withStyle(ChatFormatting.GOLD);
   }

   private void ыр() {
      long var1 = System.currentTimeMillis();
      if (this.field_8 != null) {
         if (MINECRAFT_CLIENT.level.isEmptyBlock(this.field_8)) {
            this.ы6();
         } else if (var1 - this.ХС > с7С) {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, this.field_8, this.ь(this.field_8)));
            this.ы6();
         }
      } else if (var1 >= this.Хз) {
         while (!this.Вт.isEmpty()) {
            BlockPos var3 = this.Вт.removeFirst();
            if (!MINECRAFT_CLIENT.level.isEmptyBlock(var3)) {
               int var4 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
               int var5 = this.Д(MINECRAFT_CLIENT.level.getBlockState(var3));
               if (var5 != -1 && var5 != var4) {
                  this.Ъl = var4;
                  MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var5);
                  MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
               } else {
                  this.Ъl = -1;
               }

               this.Ч(var3);
               MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var3, this.ь(var3)));
               this.ы西();
               this.field_8 = var3;
               this.ХС = var1;
               return;
            }
         }
      }
   }

   private void Ф(PacketSendEvent var1) {
      if (this.isEnabled() && this.ЯМ.getValue() && this.съ.isSelected(с7к) && !this.Аь) {
         Packet var2 = var1.getPacket();
         if (!(var2 instanceof ServerboundKeepAlivePacket) && !(var2 instanceof ServerboundPongPacket)) {
            var1.setCancelled(true);
            this.Ъ.add(var2);
         }
      }
   }

   private int н(BlockPos var1) {
      int var2 = var1.getX() - this.field_5.getX();
      int var3 = var1.getZ() - this.field_5.getZ();

      return switch (MINECRAFT_CLIENT.player.getDirection()) {
         case EAST -> var2;
         case WEST -> -var2;
         case SOUTH -> var3;
         case NORTH -> -var3;
         default -> 0;
      };
   }

   private void Ч(BlockPos var1) {
      if (var1 != null) {
         BlockPos var2 = var1.immutable();
         this.В4.put(var2, System.currentTimeMillis());
         if (this.Яю.getValue()) {
            ф.add(var2);
         }
      }
   }

   private boolean method_8(BlockPos var1) {
      return this.В4.containsKey(var1) || ф.contains(var1) || HiddenBlockPositions.弟.contains(var1);
   }

   private void ж(PacketReceiveEvent var1) {
      if (this.isEnabled() && (this.съ.isSelected(с7г) || this.съ.isSelected(с70))) {
         Packet var2 = var1.getPacket();
         if (var2 instanceof ClientboundBlockDestructionPacket var3) {
            if (this.Яю.getValue() && ф.contains(var3.getPos())) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundLevelEventPacket var4) {
            if (this.Яю.getValue() && var4.getType() == 2001 && ф.contains(var4.getPos())) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundSoundPacket var5) {
            BlockPos var8 = BlockPos.containing(var5.getX(), var5.getY(), var5.getZ());
            if (this.Яю.getValue() && ф.contains(var8)) {
               var1.setCancelled(true);
            }
         } else if (var2 instanceof ClientboundBlockUpdatePacket var6) {
            BlockPos var9 = var6.getPos().immutable();
            if (this.method_8(var9)) {
               if (var6.getBlockState().isAir()) {
                  this.Т(var9);
                  if (this.Яю.getValue()) {
                     var1.setCancelled(true);
                  }
               } else {
                  this.к(var9);
                  if (this.Ял.getValue()) {
                     var1.setCancelled(true);
                     this.Ц(var9);
                  }
               }
            }
         } else if (var2 instanceof ClientboundSectionBlocksUpdatePacket var7) {
            boolean[] var10 = new boolean[]{false};
            var7.runUpdates((var2x, var3x) -> {
               BlockPos var4x = var2x.immutable();
               if (this.method_8(var4x)) {
                  if (var3x.isAir()) {
                     this.Т(var4x);
                     if (this.Яю.getValue()) {
                        var10[0] = true;
                     }
                  } else {
                     this.к(var4x);
                     if (this.Ял.getValue()) {
                        var10[0] = true;
                        this.Ц(var4x);
                     }
                  }
               }
            });
            if (var10[0]) {
               var1.setCancelled(true);
            }
         }
      }
   }

   private int в(ItemStack var1) {
      return EnchantmentHelper.getItemEnchantmentLevel(
         MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.EFFICIENCY).orElseThrow(), var1
      );
   }

   private void ыж() {
      if (this.ВЪ.isEmpty()) {
         this.ыЛ();
      } else {
         this.l = this.ВЪ.removeFirst();
         if (!this.п(this.l)) {
            this.l = null;
            if (this.ВЪ.isEmpty()) {
               this.ыЛ();
            } else {
               this.ыж();
            }
         } else {
            this.Ч(this.l);
            MINECRAFT_CLIENT.gameMode.startDestroyBlock(this.l, this.ь(this.l));
            this.ы西();
         }
      }
   }

   private void г6(PlayerTickEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         this.А弟 = false;
         if (this.съ.isSelected(с7Р)) {
            boolean var13 = MINECRAFT_CLIENT.options.keyUp.isDown();
            boolean var14 = MINECRAFT_CLIENT.options.keyJump.isDown();
            boolean var15 = MINECRAFT_CLIENT.options.keyShift.isDown();
            if (!MINECRAFT_CLIENT.player.onGround()) {
               double var16 = (var14 ? 1.0 : 0.0) - (var15 ? 1.0 : 0.0);
               if (var13 || var16 != 0.0) {
                  if (this.щ.method_0(this.е4.getValue().longValue())) {
                     double var17 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
                     double var9 = var13 ? -Math.sin(var17) : 0.0;
                     double var11 = var13 ? Math.cos(var17) : 0.0;
                     if (MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
                        PlayerCameraPositionSmoothing.У(MINECRAFT_CLIENT.player);
                     } else {
                        PlayerCameraPositionSmoothing.Ъ7();
                     }

                     MINECRAFT_CLIENT.player.setPos(MINECRAFT_CLIENT.player.getX() + var9, MINECRAFT_CLIENT.player.getY() + var16, MINECRAFT_CLIENT.player.getZ() + var11);
                     this.щ.ВД();
                  }
               }
            }
         } else {
            boolean var2 = this.съ.isSelected(с73) || this.съ.isSelected(с7р);
            boolean var3 = this.съ.isSelected(с76);
            if (var2 || var3) {
               if (var2 && this.Яэ.getValue()) {
                  this.ый();
               }

               boolean var4 = var2 && this.Яю.getValue();
               MINECRAFT_CLIENT.player.noPhysics = false;
               if (!var4) {
                  ф.clear();
                  HiddenBlockPositions.弟.clear();
               }

               this.ыд();
               if (this.съ.isSelected(с7_) && this.ЯЭ.getValue() && this.ФБ()) {
                  this.ыЖ();
               }

               if (var3) {
                  ((MinecraftRightClickDelayAccessor)MINECRAFT_CLIENT).setItemUseCooldown(0);
               }

               if (var2) {
                  ((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).setBlockBreakingCooldown(0);
               }

               if (var3 && this.ЯЦ.getValue() && this.ФП()) {
                  this.ы弟();
               } else {
                  MINECRAFT_CLIENT.player.noJumpDelay = 0;
                  boolean var5 = MINECRAFT_CLIENT.player.horizontalCollision;
                  boolean var6 = false;
                  long var7 = this.еп.getValue().longValue() * с7Ш;
                  if (MINECRAFT_CLIENT.player.onGround() && this.щ.method_0(var7)) {
                     this.А弟 = true;
                     this.щ.ВД();
                     var6 = true;
                  }

                  if (var6) {
                     if (var3 && var5) {
                        this.ыЬ();
                     }

                     if (this.съ.isSelected(с7ы) && !this.Я必.getValue()) {
                        this.ыO();
                     }

                     if (this.съ.isSelected(с7й) && !this.Я必.getValue()) {
                        this.ыа();
                     }
                  }
               }

               if (this.съ.isSelected(с7Ж)) {
                  if (this.Я必.getValue()) {
                     this.ыя();
                  } else if (this.А衣) {
                     this.ы1();
                  }
               }

               if (this.съ.isSelected(с7м)) {
                  if (this.Я必.getValue()) {
                     this.ыя();
                  } else {
                     this.ыа();
                     this.ыр();
                  }
               }

               if (var2 && !this.Я必.getValue() && this.еа.getValue() > 0.0F && !this.А衣 && this.л.method_0(this.еа.getValue().longValue())) {
                  if (this.съ.isSelected(с7К)) {
                     this.ыO();
                  } else {
                     this.ыа();
                  }

                  this.л.ВД();
               }

               if (this.ЯМ.getValue() && this.съ.isSelected(с7I)) {
                  if (!this.Ъ.isEmpty() && this.ю.Д() > this.ет.getValue().longValue()) {
                     this.ы必();
                  }
               } else if (!this.Ъ.isEmpty()) {
                  this.ы必();
               }
            }
         }
      }
   }

   private void ы弟() {
      if (this.Г.method_0(this.еЪ.getValue().longValue())) {
         BlockPos var1 = this.в_10b53497().below();
         if (!MINECRAFT_CLIENT.level.isEmptyBlock(var1) && MINECRAFT_CLIENT.level.getBlockState(var1).getBlock() instanceof TrapDoorBlock) {
            this.т(var1);
            this.Г.ВД();
         }
      }
   }

   private void к(BlockPos var1) {
      this.В4.remove(var1);
      HiddenBlockPositions.弟.remove(var1);
      if (!this.Ял.getValue()) {
         ф.remove(var1);
      }
   }

   private void Т(BlockPos var1) {
      this.В4.remove(var1);
      if (this.Яю.getValue()) {
         ф.add(var1);
         HiddenBlockPositions.弟.add(var1);
      } else {
         ф.remove(var1);
         HiddenBlockPositions.弟.remove(var1);
      }
   }

   private boolean ФБ() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.getPose() == Pose.SWIMMING;
   }

   private Direction ь(BlockPos var1) {
      BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(var1);
      VoxelShape var3 = var2.getCollisionShape(MINECRAFT_CLIENT.level, var1);
      AABB var4 = var3.isEmpty() ? new AABB(var1) : var3.bounds().move(var1);
      Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
      Direction var6 = null;
      double var7 = с7я;
      if (var5.x <= var4.minX) {
         var7 = var4.minX - var5.x;
         var6 = Direction.WEST;
      }

      if (var5.x >= var4.maxX && var5.x - var4.maxX < var7) {
         var7 = var5.x - var4.maxX;
         var6 = Direction.EAST;
      }

      if (var5.z <= var4.minZ && var4.minZ - var5.z < var7) {
         var7 = var4.minZ - var5.z;
         var6 = Direction.NORTH;
      }

      if (var5.z >= var4.maxZ && var5.z - var4.maxZ < var7) {
         var6 = Direction.SOUTH;
      }

      if (var6 != null) {
         return var6;
      } else if (var5.y >= var4.maxY) {
         return Direction.UP;
      } else {
         return var5.y <= var4.minY ? Direction.DOWN : MINECRAFT_CLIENT.player.getDirection().getOpposite();
      }
   }

   private BlockPos ы_10b53497() {
      return MINECRAFT_CLIENT.hitResult instanceof BlockHitResult var2 ? var2.getBlockPos() : null;
   }

   private boolean п(BlockPos var1) {
      if (var1 != null && !MINECRAFT_CLIENT.level.isEmptyBlock(var1)) {
         int var2 = this.Д(MINECRAFT_CLIENT.level.getBlockState(var1));
         if (var2 == -1) {
            return false;
         }

         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getItem(var2);
         if (!this.method_7(var3)) {
            return false;
         }

         MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var2);
         MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
         return true;
      } else {
         return false;
      }
   }

   private void ый() {
      if (this.isEnabled() && (this.съ.isSelected(с7У) || this.съ.isSelected(с7З)) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.ыШ();
         AABB var1 = MINECRAFT_CLIENT.player.getDimensions(Pose.STANDING).makeBoundingBox(MINECRAFT_CLIENT.player.position());
         if (MINECRAFT_CLIENT.player.getPose() != Pose.STANDING && MINECRAFT_CLIENT.level.noCollision(MINECRAFT_CLIENT.player, var1)) {
            MINECRAFT_CLIENT.player.setPose(Pose.STANDING);
         }
      }
   }

   private void ыO() {
      if (!this.А衣) {
         Direction var1 = MINECRAFT_CLIENT.player.getDirection();
         BlockPos var2 = this.в_10b53497();
         this.ВЪ.clear();
         if (this.Ян.getValue() && this.ФБ()) {
            BlockPos var9 = var2.relative(var1);
            if (!MINECRAFT_CLIENT.level.isEmptyBlock(var9)) {
               this.ВЪ.add(var9);
            }
         } else {
            int var3 = this.Ящ.getValue() ? 2 : 1;

            for (int var4 = 1; var4 <= var3; var4++) {
               BlockPos var5 = var2.relative(var1, var4);

               for (int var6 = 0; var6 < 2; var6++) {
                  BlockPos var7 = var5.above(var6);
                  if (!MINECRAFT_CLIENT.level.isEmptyBlock(var7)) {
                     this.ВЪ.add(var7);
                  }
               }
            }
         }

         if (this.ЯГ.getValue()) {
            for (int var10 = 0; var10 < 2; var10++) {
               BlockPos var12 = var2.above(var10);
               if (!MINECRAFT_CLIENT.level.isEmptyBlock(var12)) {
                  this.ВЪ.add(var12);
               }
            }
         }

         if (!this.ВЪ.isEmpty()) {
            boolean var11 = true;

            for (BlockPos var14 : this.ВЪ) {
               BlockState var15 = MINECRAFT_CLIENT.level.getBlockState(var14);
               int var16 = this.Д(var15);
               if (var16 == -1) {
                  var11 = false;
               } else {
                  ItemStack var8 = MINECRAFT_CLIENT.player.getInventory().getItem(var16);
                  if (!this.method_7(var8) || this.в(var8) < 7) {
                     var11 = false;
                  }
               }
            }

            this.Аж = var11;
            if (!this.Аж && System.currentTimeMillis() - this.Хц > с7д) {
               ClientChat.З(this.Ъ_f022d65c());
               this.Хц = System.currentTimeMillis();
            }

            if (this.ЯН.getValue()) {
               this.ВЪ
                  .sort(
                     Comparator.<BlockPos>comparingInt(var1x -> Math.abs(var1x.getX() - var2.getX()) + Math.abs(var1x.getZ() - var2.getZ()))
                        .thenComparingInt(Vec3i::getY)
                  );
            }

            this.Ъl = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
            this.А衣 = true;
            this.ХБ = System.currentTimeMillis();
            MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
            this.ыж();
         }
      }
   }

   private void М(PlayerTickEvent.Post var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if ((this.съ.isSelected(с7O) || this.съ.isSelected(с77)) && this.Яэ.getValue()) {
            this.ый();
         }
      }
   }

   private void ыЖ() {
      int var1 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
      BlockPos var2 = this.в_10b53497();
      this.з(var2);
      this.з(var2.above());
      MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var1);
      MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
   }

   private boolean в(BlockState var1) {
      Block var2 = var1.getBlock();
      return var2 instanceof DoorBlock || var2 instanceof FenceGateBlock || var2 instanceof TrapDoorBlock;
   }

   private void Ц(BlockPos var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.level.isEmptyBlock(var1)) {
         this.Ч(var1);
         Direction var2 = this.ь(var1);
         MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var1, var2));
         MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var1, var2));
         this.ы西();
      }
   }

   public static boolean э(BlockPos var0) {
      return var0 != null && ф.contains(var0);
   }

   private void ы1() {
      if (System.currentTimeMillis() - this.ХБ > с7Б) {
         this.ыЛ();
      } else if (!this.Аж) {
         if (this.l != null && !MINECRAFT_CLIENT.level.isEmptyBlock(this.l)) {
            ((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).setBlockBreakingCooldown(0);
            boolean var3 = MINECRAFT_CLIENT.gameMode.continueDestroyBlock(this.l, this.ь(this.l));
            this.ы西();
            if (var3) {
               MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
               this.l = null;
               if (this.ВЪ.isEmpty()) {
                  this.ыЛ();
               } else {
                  this.ыж();
               }
            }
         } else {
            this.l = null;
            if (this.ВЪ.isEmpty()) {
               this.ыЛ();
            } else {
               this.ыж();
            }
         }
      } else {
         if (this.l != null) {
            this.з(this.l);
         }

         for (BlockPos var2 : this.ВЪ) {
            this.з(var2);
         }

         this.ыЛ();
      }
   }

   public static boolean И(int var0) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         NoClip var1 = RoseClient.getInstance().getServices().getModules().getNoClip();
         return var1 != null
            && var1.isEnabled()
            && (var1.съ.isSelected(с7Щ) || var1.съ.isSelected(с7у))
            && Minecraft.getInstance().player != null
            && Minecraft.getInstance().player.getId() == var0;
      } else {
         return false;
      }
   }

   private void ы必() {
      if (!this.Ъ.isEmpty()) {
         this.Аь = true;

         for (Packet var2 : this.Ъ) {
            PacketEventBypass.method_4(var2);
         }

         this.Аь = false;
         this.Ъ.clear();
         this.ю.ВД();
      }
   }

   private void ыа() {
      if (this.Вт.isEmpty()) {
         BlockPos var1 = this.в_10b53497();
         Direction var2 = MINECRAFT_CLIENT.player.getDirection();

         for (int var3 = 1; var3 <= 2; var3++) {
            BlockPos var4 = var1.relative(var2, var3);

            for (int var5 = 0; var5 < 3; var5++) {
               BlockPos var6 = var4.above(var5);
               if (!MINECRAFT_CLIENT.level.isEmptyBlock(var6)) {
                  this.Вт.add(var6);
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.щ.ВД();
      this.А弟 = false;
      PlayerCameraPositionSmoothing.Ъ7();
      if (this.съ.isSelected(с79) || this.съ.isSelected(с7о)) {
         ClientChat.З(this.method_3());
      }
   }

   private BlockPos в_10b53497() {
      return BlockPos.containing(MINECRAFT_CLIENT.player.position());
   }

   private void ыЛ() {
      this.А衣 = false;
      this.l = null;
      this.ВЪ.clear();
      this.ыб();
   }
}
