package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.ec.BaritoneIntegration;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;

public class KeyFinder extends ClientModule {
   private final FloatSetting Я9;
   private final BooleanSetting В衣;
   private final BooleanSetting Вш;
   private final BooleanSetting ВИ;
   @SerializedName("Ы")
   private final MultiChoiceSetting Ы_3bdb855e;
   @SerializedName("l")
   private final MultiChoiceSetting l_3bdb855e;
   private final BooleanSetting Вь;
   private final BooleanSetting Вж;
   private final FloatSetting Яо;
   private final FloatSetting ЯO;
   private final FloatSetting Я7;
   private final BooleanSetting В弟;
   private final BooleanSetting Вб;
   private final BooleanSetting ВЫ;
   private final FloatSetting ЯР;
   @SerializedName("В")
   private KeyFinder.SearchStage В_1538e300;
   @SerializedName("1")
   private BlockPos field_1;
   @SerializedName("В")
   private MinecartChest В_6bde07c5;
   private BlockPos Ь;
   private BlockPos 衣;
   private BlockPos ш;
   @SerializedName("ь")
   private final Stopwatch ь_fed7dec4;
   private final Stopwatch ж;
   private final Stopwatch 弟;
   private final Stopwatch б;
   @SerializedName("Ы")
   private final Stopwatch Ы_fed7dec4;
   @SerializedName("l")
   private final Stopwatch l_fed7dec4;
   @SerializedName("8")
   private final Stopwatch field_8;
   private final Set<Long> н;
   private final Set<BlockPos> Ц;
   private final Set<Long> ч;
   private final Set<Long> Э;
   private final Set<Long> 必;
   private boolean Хь;
   private boolean Хж;
   private boolean Х弟;
   private boolean Хб;
   private boolean ХЫ;
   private boolean Хl;
   private volatile BlockPos И;
   private long ЯЙ;
   @SerializedName("4л")
   private int field_4л;
   private long Як;
   private String ВШ;
   @SerializedName("4э")
   private int field_4э;
   @SerializedName("4ю")
   private int field_4ю;
   @SerializedName("4Щ")
   private int field_4Щ;
   @SerializedName("4у")
   private int field_4у;
   @SerializedName("42")
   private int field_42;
   private boolean Х8;
   private boolean Х5;
   private boolean Хв;
   private long Яг;
   @SerializedName("4ф")
   private int field_4ф;
   private long Я0;
   @SerializedName("ь")
   private BlockPos ь_10b53497;
   private long Ях;
   private long ЯУ;
   @SerializedName("4и")
   private int field_4и;
   private static final double Яс = 65.0;
   private static final double ЯВ = 0.001;
   private static final long ЯЗ = 200000000L;
   private static final long ЯЕ = 150000000L;
   private static final long ЯП = 200000000L;
   private static final int field_49 = 100;
   private static final int field_4о = 3;
   private static final double ЯЯ = 2.5;
   private static final double ЯХ = 0.62;
   private static final int field_4O = 3;
   private static final int field_47 = 1;
   private static final double Яе = 0.29;
   private static final double ЯА = 1.0;
   private static final double Я4 = 1.8;
   private static final double Яп = 0.25;
   private static final int field_4Р = 10;
   private static final long Я西 = 5000L;
   private static final long Яд = 30000L;
   private static final String Вы = "извините, но вы не можете";
   private static final String Вй = "не можете сломать блок";
   private final EventListener<PacketReceiveEvent> Яl;
   private final EventListener<PlayerTickEvent> Я8;
   private static final Set<Item> щ = Set.of(
      Items.COBBLESTONE,
      Items.COBBLED_DEEPSLATE,
      Items.MOSSY_COBBLESTONE,
      Items.STONE,
      Items.GRANITE,
      Items.POLISHED_GRANITE,
      Items.DIORITE,
      Items.POLISHED_DIORITE,
      Items.ANDESITE,
      Items.POLISHED_ANDESITE,
      Items.DEEPSLATE,
      Items.TUFF,
      Items.CALCITE,
      Items.DIRT,
      Items.COARSE_DIRT,
      Items.ROOTED_DIRT,
      Items.GRAVEL,
      Items.SAND,
      Items.RED_SAND,
      Items.NETHERRACK,
      Items.BASALT,
      Items.BLACKSTONE,
      Items.END_STONE
   );
   private static final String ЯщА = "KeyFinder";
   private static final String Ящ4 = "Автоматически ищет ключ-карты на ReallyWorld";
   private static final String Ящп = "Радиус поиска";
   private static final float ЯщД = 64.0F;
   private static final float ЯщЪ = 4.0F;
   private static final float Ящт = 64.0F;
   private static final String Яща = "Поиск сундуков";
   private static final String ЯщЮ = "Поиск вагонеток";
   private static final String ЯщЧ = "Только рядом с спавнером";
   private static final String ЯщЛ = "Что ломать за собой";
   private static final String Ящ诶 = "Сундуки";
   private static final String Ящ1 = "Вагонетки";
   private static final String ЯщЬ = "Что лутать";
   private static final String Ящ衣 = "Зачарованные яблоки";
   private static final String Ящш = "Золотые яблоки";
   private static final String ЯщИ = "Ключ Карты";
   private static final String Ящь = "Зачар книги";
   private static final String Ящж = "/ec";
   private static final String Ящ弟 = "Выкидывать мусор";
   private static final String Ящб = "Задержка взятия";
   private static final float ЯщЫ = 120.0F;
   private static final float Ящl = 1000.0F;
   private static final float Ящ8 = 10.0F;
   private static final String Ящ5 = "Задержка открытия";
   private static final float Ящв = 600.0F;
   private static final float Ящъ = 3000.0F;
   private static final float ЯщМ = 50.0F;
   private static final String ЯщН = "Радиус ходьба";
   private static final float Ящн = 256.0F;
   private static final float ЯщЦ = 16.0F;
   private static final float Ящч = 256.0F;
   private static final String ЯщЭ = "Teleport";
   private static final String Ящ必 = "Статус в чате";
   private static final String Ящщ = "Авто /heal";
   private static final String ЯщГ = "Когда /heal(еда)";
   private static final float Ящл = 7.0F;
   private static final float Ящэ = 20.0F;
   private static final long Ящю = Long.MIN_VALUE;
   private static final long ЯщЩ = Long.MIN_VALUE;
   private static final long Ящу = Long.MIN_VALUE;
   private static final String Ящ2 = "Baritone mod not found";
   private static final String Ящф = "извините, но вы не можете";
   private static final String Ящи = "не можете сломать блок";
   private static final String Ящ9 = "access denied, blacklisted 10 blocks around target";
   private static final String Ящо = "break denied, rerouting";
   private static final long ЯщO = Long.MIN_VALUE;
   private static final String Ящ7 = "#cancel";
   private static final long ЯщР = 400L;
   private static final String Ящ3 = "found chest minecart";
   private static final String Ящр = "found chest";
   private static final String Ящ6 = "target lost";
   private static final String Ящ_ = "opening target";
   private static final long ЯщШ = 3000L;
   private static final long Ящы = Long.MIN_VALUE;
   private static final long Ящй = 500L;
   private static final long ЯщЖ = 1200L;
   private static final long Ящм = Long.MIN_VALUE;
   private static final String ЯщК = "dumping junk for loot";
   private static final String ЯщI = "taking key card";
   private static final String ЯщТ = "taking extra loot";
   private static final String ЯщО = "ec f";
   private static final String ЯщФ = "opening /ec";
   private static final long ЯщЙ = 5000L;
   private static final long Ящк = 5000L;
   private static final long Ящг = 30000L;
   private static final String Ящ0 = "depositing farmed loot to /ec";
   private static final String Ящх = "stored farmed loot in /ec";
   private static final String ЯщУ = "could not store farmed loot in /ec";
   private static final String ЯщЗ = "Сундуки";
   private static final String ЯщЕ = "breaking chest";
   private static final String ЯщП = "Вагонетки";
   private static final long Ящ西 = 6000L;
   private static final String Ящд = "breaking chest minecart";
   private static final String ЯщБ = "resyncing tool slot";
   private static final long ЯщС = 50L;
   private static final long Ящз = 250L;
   private static final String Ящц = "found chest minecart while exploring";
   private static final String Ящя = "found chest while exploring";
   private static final long ЯГс = 10000L;
   private static final double ЯГВ = Double.MAX_VALUE;
   private static final float ЯГЯ = 16.0F;
   private static final double ЯГХ = 81.0;
   private static final double ЯГе = 6.25;
   private static final double ЯГА = 0.1;
   private static final long ЯГ4 = 150000000L;
   private static final String ЯГп = "Ключ-Карта";
   private static final String ЯГД = "Ключ Карты";
   private static final String ЯГЪ = "Зачарованные яблоки";
   private static final String ЯГт = "Золотые яблоки";
   private static final String ЯГа = "Зачар книги";
   private static final double ЯГЮ = Double.MAX_VALUE;
   private static final double ЯГЧ = Double.MAX_VALUE;
   private static final double ЯГЛ = 0.5;
   private static final double ЯГ诶 = 0.5;
   private static final double ЯГ1 = 1000.0;
   private static final double ЯГЬ = 6.25;
   private static final double ЯГ衣 = 0.5;
   private static final double ЯГш = 0.62;
   private static final double ЯГИ = 0.5;
   private static final double ЯГь = 0.5;
   private static final double ЯГж = 0.5;
   private static final double ЯГ弟 = Double.MAX_VALUE;
   private static final double ЯГб = 1000.0;
   private static final double ЯГЫ = 0.5;
   private static final double ЯГl = 0.5;
   private static final double ЯГ8 = 65.0;
   private static final double ЯГ5 = 0.5;
   private static final double ЯГв = 0.5;
   private static final double ЯГъ = 0.001;
   private static final double ЯГМ = 0.001;
   private static final double ЯГН = 65.0;
   private static final double ЯГн = 0.001;
   private static final double ЯГЦ = Double.MAX_VALUE;
   private static final double ЯГч = 0.5;
   private static final double ЯГЭ = 0.5;
   private static final double ЯГ必 = 65.001;
   private static final double ЯГщ = 0.25;
   private static final double ЯГГ = 0.29;
   private static final double ЯГл = 0.29;
   private static final double ЯГэ = 0.29;
   private static final double ЯГю = 1.8;
   private static final double ЯГЩ = 0.29;
   private static final double ЯГу = 0.29;
   private static final double ЯГ2 = 0.29;
   private static final double ЯГф = 0.29;
   private static final double ЯГи = 1.8;
   private static final double ЯГ9 = 0.29;
   private static final long ЯГо = 200000000L;
   private static final double ЯГO = 0.5;
   private static final double ЯГ7 = 0.5;
   private static final long ЯГР = 200000000L;
   private static final long ЯГ3 = Long.MIN_VALUE;
   private static final long ЯГр = Long.MIN_VALUE;
   private static final long ЯГ6 = Long.MIN_VALUE;
   private static final String ЯГ_ = "teleport point failed 3 times, choosing another";
   private static final double ЯГШ = 0.5;
   private static final double ЯГы = 0.5;
   private static final double ЯГй = 0.64;
   private static final double ЯГЖ = 1.25;
   private static final long ЯГм = Long.MIN_VALUE;
   private static final long ЯГК = Long.MIN_VALUE;
   private static final long ЯГI = Long.MIN_VALUE;
   private static final String ЯГТ = "teleport area unavailable, choosing another";
   private static final long ЯГО = Long.MIN_VALUE;
   private static final String ЯГФ = "teleport target unavailable, skipping";
   private static final long ЯГЙ = Long.MIN_VALUE;
   private static final String ЯГк = "#cancel";
   private static final long ЯГг = 500L;
   private static final long ЯГ0 = 5000L;
   private static final String ЯГх = "heal";
   private static final String ЯГУ = "low hunger, sent /heal";
   private static final float ЯГЗ = 90.0F;
   private static final float ЯГЕ = -90.0F;
   private static final float ЯГП = 90.0F;
   private static final long ЯГ西 = 4294967295L;
   private static final long ЯГд = 4294967295L;
   private static final double ЯГБ = 0.5;
   private static final double ЯГС = 0.5;
   private static final long ЯГз = Long.MIN_VALUE;

   private boolean П(BlockPos var1) {
      BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(var1);
      BlockState var3 = MINECRAFT_CLIENT.level.getBlockState(var1.above());
      BlockState var4 = MINECRAFT_CLIENT.level.getBlockState(var1.below());
      return var2.getCollisionShape(MINECRAFT_CLIENT.level, var1).isEmpty()
         && var3.getCollisionShape(MINECRAFT_CLIENT.level, var1.above()).isEmpty()
         && var2.getFluidState().isEmpty()
         && var3.getFluidState().isEmpty()
         && !var4.getCollisionShape(MINECRAFT_CLIENT.level, var1.below()).isEmpty();
   }

   private Integer Ю_fb15200a(int var1, int var2) {
      int var3 = Math.min(MINECRAFT_CLIENT.level.getHeight(Types.WORLD_SURFACE, var1, var2) - 1, MINECRAFT_CLIENT.level.getMaxY());

      for (int var4 = var3; var4 >= MINECRAFT_CLIENT.level.getMinY(); var4--) {
         BlockPos var5 = new BlockPos(var1, var4, var2);
         BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(var5);
         if (!var6.getCollisionShape(MINECRAFT_CLIENT.level, var5).isEmpty()) {
            return var4;
         }
      }

      return null;
   }

   private void эо() {
      this.ч.add(this.Ю_6da43b94(Mth.floor(MINECRAFT_CLIENT.player.getX()), Mth.floor(MINECRAFT_CLIENT.player.getZ())));
   }

   private long ш_6da43b94() {
      return Math.max(0L, this.ЯO.getValue().longValue());
   }

   private boolean у8() {
      return !this.Хв || System.nanoTime() - this.Яг >= ЯГо;
   }

   private BlockPos ы(int var1, int var2, int var3, int var4, int var5) {
      int var6 = Math.max(8, var4);

      for (int var7 = 0; var7 < var5; var7++) {
         int var8 = ThreadLocalRandom.current().nextInt(-var6, var6 + 1);
         int var9 = ThreadLocalRandom.current().nextInt(-var6, var6 + 1);
         if (Math.abs(var8) >= 8 || Math.abs(var9) >= 8) {
            BlockPos var10 = new BlockPos(var1 + var8, var2, var3 + var9);
            if (!this.я_bbeebd87(var10)) {
               return var10;
            }
         }
      }

      return null;
   }

   private BlockHitResult ы(Vec3 var1, Vec3 var2) {
      return MINECRAFT_CLIENT.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
   }

   private boolean 必(BlockPos var1) {
      for (BlockPos var3 : this.Ц) {
         if (Math.abs(var1.getX() - var3.getX()) <= 10 && Math.abs(var1.getZ() - var3.getZ()) <= 10) {
            return true;
         }
      }

      return false;
   }

   private void Лй(boolean var1) {
      this.с(var1 ? Ящх : ЯщУ);
      this.field_8.ВД();
      this.эЪ();
      this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
   }

   private boolean у7() {
      if (this.ВШ == null) {
         return false;
      }

      this.эл();
      MINECRAFT_CLIENT.options.keyLeft.setDown(false);
      MINECRAFT_CLIENT.options.keyRight.setDown(false);
      MINECRAFT_CLIENT.options.keyDown.setDown(false);
      if (MINECRAFT_CLIENT.gameMode != null) {
         MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
      }

      if (this.field_4э > 0) {
         this.field_4э--;
         return true;
      }

      if (MINECRAFT_CLIENT.getConnection() != null) {
         MINECRAFT_CLIENT.getConnection().sendChat(this.ВШ);
      }

      this.ВШ = null;
      this.Ы_fed7dec4.ВД();
      return true;
   }

   public KeyFinder() {
      super(ЯщА, Ящ4, ModuleCategory.MISC);
      this.Я9 = new FloatSetting(Ящп, ЯщД, ЯщЪ, Ящт).withStep(1.0F);
      this.В衣 = new BooleanSetting(Яща, true);
      this.Вш = new BooleanSetting(ЯщЮ, true);
      this.ВИ = new BooleanSetting(ЯщЧ, true);
      this.Ы_3bdb855e = new MultiChoiceSetting(ЯщЛ, new ChoiceOption(Ящ诶, false), new ChoiceOption(Ящ1, false));
      this.l_3bdb855e = new MultiChoiceSetting(
         ЯщЬ, new ChoiceOption(Ящ衣, true), new ChoiceOption(Ящш, true), new ChoiceOption(ЯщИ, true), new ChoiceOption(Ящь, true)
      );
      this.Вь = new BooleanSetting(Ящж, false);
      this.Вж = new BooleanSetting(Ящ弟, true);
      this.Яо = new FloatSetting(Ящб, ЯщЫ, 0.0F, Ящl).withStep(Ящ8);
      this.ЯO = new FloatSetting(Ящ5, Ящв, 0.0F, Ящъ).withStep(ЯщМ);
      this.Я7 = new FloatSetting(ЯщН, Ящн, ЯщЦ, Ящч).withStep(1.0F);
      this.В弟 = new BooleanSetting(ЯщЭ, false);
      this.Вб = new BooleanSetting(Ящ必, false);
      this.ВЫ = new BooleanSetting(Ящщ, false);
      this.ЯР = new FloatSetting(ЯщГ, Ящл, 0.0F, Ящэ).withStep(1.0F).visibleWhen(this.ВЫ::getValue);
      this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      this.ь_fed7dec4 = new Stopwatch();
      this.ж = new Stopwatch();
      this.弟 = new Stopwatch();
      this.б = new Stopwatch();
      this.Ы_fed7dec4 = new Stopwatch();
      this.l_fed7dec4 = new Stopwatch();
      this.field_8 = new Stopwatch();
      this.н = new HashSet<>();
      this.Ц = new HashSet<>();
      this.ч = new HashSet<>();
      this.Э = new HashSet<>();
      this.必 = new HashSet<>();
      this.Як = Ящю;
      this.field_4у = -1;
      this.Я0 = ЯщЩ;
      this.ЯУ = Ящу;
      this.Яl = this::ш;
      this.Я8 = this::Ля;
      this.addSettings(
         this.Я9, this.В衣, this.Вш, this.ВИ, this.Ы_3bdb855e, this.l_3bdb855e, this.Вь, this.Вж, this.Яо, this.ЯO, this.Я7, this.В弟, this.Вб, this.ВЫ, this.ЯР
      );
   }

   private void method_9_de5a6f78(String var1) {
      this.ы(var1, false);
   }

   private boolean уэ() {
      if (!this.Вь.getValue()
         || this.В_1538e300 == KeyFinder.SearchStage.EC_OPEN
         || this.В_1538e300 == KeyFinder.SearchStage.EC_STORE
         || !this.field_8.method_0(Ящг)) {
         return false;
      } else if (this.В_1538e300 == KeyFinder.SearchStage.OPEN
         || this.В_1538e300 == KeyFinder.SearchStage.LOOT
         || this.В_1538e300 == KeyFinder.SearchStage.BREAK
         || this.В_1538e300 == KeyFinder.SearchStage.BREAK_CART) {
         return false;
      } else if (!this.уу()) {
         this.field_8.ВД();
         return false;
      } else {
         this.эт();
         this.эл();
         this.эЪ();
         this.Хб = false;
         this.弟.ВД();
         this.ж.ВД();
         this.с(Ящ0);
         this.В_1538e300 = KeyFinder.SearchStage.EC_OPEN;
         return true;
      }
   }

   private Vec3 ц() {
      if (this.В_6bde07c5 != null) {
         return this.В_6bde07c5.getBoundingBox().getCenter();
      } else {
         return this.field_1 != null ? Vec3.atCenterOf(this.field_1) : null;
      }
   }

   private void э1() {
      if (!this.уВ()) {
         this.с(Ящ6);
         this.эЕ();
         this.эЪ();
         this.эт();
         this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      } else {
         this.эо();
         if (MINECRAFT_CLIENT.player.isInWater()) {
            MINECRAFT_CLIENT.options.keyJump.setDown(true);
         }

         if (this.уч()) {
            this.эт();
            this.эл();
            this.Хь = false;
            this.Хж = false;
            this.Х弟 = false;
            this.field_4л = 0;
            this.弟.ВД();
            this.с(Ящ_);
            this.В_1538e300 = KeyFinder.SearchStage.OPEN;
         } else {
            BlockPos var1 = this.В_6bde07c5 != null ? this.В_6bde07c5.blockPosition() : this.field_1;
            if (var1 != null) {
               if (this.В弟.getValue()) {
                  BlockPos var2 = this.ш;
                  if (!this.l(var2)) {
                     var2 = this.ж();
                  }

                  if (var2 == null) {
                     this.Лж(false);
                     return;
                  }

                  this.Як = var1.asLong();
                  this.ш = var2;
                  this.ц(var2);
               } else if (this.уп() && (this.б.method_0(ЯщШ) || this.Як != var1.asLong() || this.ш == null)) {
                  this.эш();
                  this.б.ВД();
               }
            }
         }
      }
   }

   private boolean ы_bbeebd87(AbstractContainerMenu var1, int var2) {
      for (int var3 = 0; var3 < var2; var3++) {
         if (var1.slots.get(var3).getItem().isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private void method_8(BlockPos var1) {
      if (var1 != null && this.у8()) {
         this.эл();
         double var2 = var1.getX() + ЯГ5;
         double var4 = var1.getZ() + ЯГв;
         double var6 = Math.hypot(var2 - MINECRAFT_CLIENT.player.getX(), var4 - MINECRAFT_CLIENT.player.getZ());
         if (!(var6 <= ЯГъ)) {
            KeyFinder.SearchPosition var8 = this.ы(var2, var4);
            if (var8 == null) {
               this.Лж(true);
            } else {
               this.ы(var8);
            }
         }
      }
   }

   private void эе() {
      this.field_4ю = 0;
      this.field_4Щ = 0;
      this.field_4у = -1;
      this.field_42 = 0;
      this.Х8 = false;
   }

   private KeyFinder.SearchPosition ы(double var1, double var3) {
      double var5 = MINECRAFT_CLIENT.player.getX();
      double var7 = MINECRAFT_CLIENT.player.getZ();
      double var9 = var1 - var5;
      double var11 = var3 - var7;
      double var13 = Math.hypot(var9, var11);
      if (var13 <= ЯГМ) {
         return null;
      }

      for (double var15 = Math.min(var13, ЯГН); var15 > ЯГн; var15 -= Math.min(1.0, var15)) {
         double var17 = var15 / var13;
         double var19 = var5 + var9 * var17;
         double var21 = var7 + var11 * var17;
         KeyFinder.SearchPosition var23 = this.ы(var5, var7, var19, var21, var1, var3);
         if (var23 != null) {
            return var23;
         }
      }

      return null;
   }

   private void Ля(PlayerTickEvent var1) {
      this.эЬ();
      this.э5();
      BlockPos var2 = this.И;
      if (var2 != null) {
         this.И = null;
         this.Ц.add(var2);
         this.с(Ящ9);
         this.эЪ();
         this.эт();
         this.эл();
         this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      } else if (!this.у7()) {
         if (this.Хl) {
            this.Хl = false;
            this.с(Ящо);
            this.эЕ();
            this.эЪ();
            this.эт();
            if (MINECRAFT_CLIENT.gameMode != null) {
               MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
            }

            this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
         } else {
            this.эЗ();
            if (!this.уэ()) {
               if (this.В_1538e300 != KeyFinder.SearchStage.EC_OPEN && this.В_1538e300 != KeyFinder.SearchStage.EC_STORE) {
                  this.эЦ();
               }

               switch (this.В_1538e300) {
                  case SEARCH:
                     this.эШ();
                     break;
                  case GOTO:
                     this.э1();
                     break;
                  case OPEN:
                     this.эl();
                     break;
                  case LOOT:
                     this.эТ();
                     break;
                  case EC_OPEN:
                     this.эП();
                     break;
                  case EC_STORE:
                     this.эO();
                     break;
                  case BREAK:
                     this.э必();
                     break;
                  case BREAK_CART:
                     this.эЧ();
                     break;
                  case EXPLORE:
                     this.э_();
               }
            }
         }
      }
   }

   private void с(String var1) {
      if (this.Вб.getValue()) {
         ClientChat.З("[KeyFinder] " + var1);
      }
   }

   private long А_6da43b94() {
      return Math.max(0L, this.Яо.getValue().longValue());
   }

   private void эЗ() {
      boolean var1 = this.Вь.getValue();
      if (var1 != this.ХЫ) {
         this.ХЫ = var1;
         this.field_8.ВД();
         if (!var1 && (this.В_1538e300 == KeyFinder.SearchStage.EC_OPEN || this.В_1538e300 == KeyFinder.SearchStage.EC_STORE)) {
            AbstractContainerMenu var2 = MINECRAFT_CLIENT.player.containerMenu;
            if (var2 instanceof ChestMenu) {
               this.ы(var2);
            }

            this.Хб = false;
            this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
         }
      }
   }

   private void эА() {
      if (MINECRAFT_CLIENT.gameMode != null) {
         if (this.В_6bde07c5 != null) {
            this.эщ();
            Vec3 var3 = this.В_6bde07c5.getBoundingBox().getCenter();
            EntityHitResult var4 = new EntityHitResult(this.В_6bde07c5, var3);
            MINECRAFT_CLIENT.gameMode.interact(MINECRAFT_CLIENT.player, this.В_6bde07c5, var4, InteractionHand.MAIN_HAND);
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         } else {
            if (this.field_1 != null) {
               Vec3 var1 = this.ц();
               if (var1 == null) {
                  return;
               }

               this.эщ();
               BlockHitResult var2 = this.ы(MINECRAFT_CLIENT.player.getEyePosition(), var1);
               if (var2.getType() != Type.BLOCK || !var2.getBlockPos().equals(this.field_1)) {
                  return;
               }

               MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var2);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            }
         }
      }
   }

   private boolean уп() {
      return !this.В弟.getValue() && this.у3();
   }

   private void эх() {
      int var1 = Math.max(1, this.Я7.getValue().intValue());
      int var2 = Mth.floor(MINECRAFT_CLIENT.player.getX());
      int var3 = Mth.floor(MINECRAFT_CLIENT.player.getY());
      int var4 = Mth.floor(MINECRAFT_CLIENT.player.getZ());
      BlockPos var5;
      if (this.В弟.getValue()) {
         var5 = this.ы(var2, var3, var4, var1, 120);
      } else {
         var5 = this.ы(var2, var3, var4, var1, 80, 8, 6);
         if (var5 == null) {
            var5 = this.ы(var2, var3, var4, var1 * 2, 120, 10, 6);
         }

         if (var5 == null) {
            var5 = this.ы(new BlockPos(var2 + var1, var3, var4), 10, 6, false);
         }
      }

      if (var5 == null) {
         var5 = new BlockPos(var2 + var1, var3, var4);
      }

      this.衣 = var5;
      this.эЩ();
      this.Э.add(this.Ю(this.衣));
   }

   private Slot ы_cfa9b459(AbstractContainerMenu var1, int var2) {
      for (int var3 = var2; var3 < var1.slots.size(); var3++) {
         Slot var4 = var1.slots.get(var3);
         ItemStack var5 = var4.getItem();
         if (!var5.isEmpty() && this.с(var5)) {
            return var4;
         }
      }

      return null;
   }

   private void эю() {
      long var1;
      if (this.field_1 != null) {
         var1 = this.field_1.asLong();
      } else if (this.В_6bde07c5 != null) {
         var1 = this.В_6bde07c5.blockPosition().asLong();
      } else {
         var1 = ЯГм;
      }

      if (var1 != this.Я0) {
         this.эН();
         this.Я0 = var1;
      }
   }

   private boolean уу() {
      for (int var1 = 0; var1 < 36; var1++) {
         ItemStack var2 = MINECRAFT_CLIENT.player.getInventory().getItem(var1);
         if (!var2.isEmpty() && this.с(var2)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onDisable() {
      this.эт();
      this.эл();
      this.эЯ();
      super.onDisable();
   }

   private void эЕ() {
      if (this.В_6bde07c5 != null) {
         this.н.add(this.В_6bde07c5.blockPosition().asLong());
      } else if (this.field_1 != null) {
         this.н.add(this.field_1.asLong());
      }
   }

   private void ш(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSystemChatPacket var2) {
         String var5 = var2.content().getString().toLowerCase(Locale.ROOT);
         if (var5.contains(Ящф) && this.В_1538e300 == KeyFinder.SearchStage.OPEN && this.Хь) {
            BlockPos var4 = this.В_6bde07c5 != null ? this.В_6bde07c5.blockPosition() : this.field_1;
            if (var4 != null) {
               this.И = var4.immutable();
            }

            return;
         }

         if (var5.contains(Ящи)) {
            this.Хl = true;
         }
      }
   }

   private boolean ж(double var1, double var3, double var5) {
      AABB var7 = new AABB(var1 - ЯГГ, var3 + 1.0, var5 - ЯГл, var1 + ЯГэ, var3 + ЯГю, var5 + ЯГЩ);
      return MINECRAFT_CLIENT.level.noCollision(MINECRAFT_CLIENT.player, var7);
   }

   private void э_() {
      this.эо();
      if (this.ь_fed7dec4.ф(Ящз, true) && this.уи()) {
         this.эт();
         this.эл();
         this.эш();
         this.с(this.В_6bde07c5 != null ? Ящц : Ящя);
         this.б.ВД();
         this.В_1538e300 = KeyFinder.SearchStage.GOTO;
      } else if (this.衣 != null && !this.ы(this.衣, 1.0)) {
         if (this.В弟.getValue()) {
            this.method_8(this.衣);
         } else if (this.б.method_0(ЯГс)) {
            this.эЩ();
         }
      } else {
         this.эх();
      }
   }

   private long Ю(BlockPos var1) {
      return this.Ю_6da43b94(var1.getX(), var1.getZ());
   }

   private boolean Х(ItemStack var1) {
      String var2 = ЯГп;
      if (var2 != null && !var2.trim().isEmpty()) {
         String var3 = var1.getHoverName().getString();
         String var4 = StringUtil.stripColor(var3);
         if (var4 == null) {
            var4 = var3;
         }

         return var4.toLowerCase(Locale.ROOT).contains(var2.trim().toLowerCase(Locale.ROOT));
      } else {
         return false;
      }
   }

   private void эк() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         this.field_4у = MINECRAFT_CLIENT.player.getInventory().selected;
         int var1 = this.field_4у == 0 ? 1 : 0;
         MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
         this.Лй(var1);
         this.Х8 = true;
         this.field_42 = 1;
         this.с(ЯщБ);
      }
   }

   private boolean l(BlockPos var1) {
      if (var1 == null || this.必.contains(var1.asLong()) || !this.Т(var1)) {
         return false;
      }

      if (this.field_1 != null && var1.below().equals(this.field_1)) {
         return false;
      }

      Vec3 var2 = this.ц();
      if (var2 == null) {
         return false;
      }

      Vec3 var3 = this.й(var1);
      return var3.distanceToSqr(var2) <= ЯГЬ && this.й(var3, var2);
   }

   private boolean ы(BlockPos var1, List<BlockPos> var2, double var3) {
      for (BlockPos var6 : var2) {
         if (var1.distSqr(var6) <= var3) {
            return true;
         }
      }

      return false;
   }

   private void ы(BlockPos var1, KeyFinder.SearchPosition var2) {
      this.ы(var2);
      this.ь_10b53497 = var1.immutable();
      this.Ях = this.Яг + ЯГР;
   }

   private void э必() {
      if (this.Ь != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player != null) {
         BlockState var1 = MINECRAFT_CLIENT.level.getBlockState(this.Ь);
         net.minecraft.world.level.block.Block var2 = var1.getBlock();
         if (var1.isAir() || !(var2 instanceof ChestBlock)) {
            this.н.add(this.Ь.asLong());
            this.Ь = null;
            this.эЪ();
            MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
            this.эе();
            this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
         } else if (this.Х8) {
            this.эО();
         } else {
            this.п(Vec3.atCenterOf(this.Ь));
            MINECRAFT_CLIENT.gameMode.continueDestroyBlock(this.Ь, Direction.UP);
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            this.field_4ю++;
            if (this.field_4ю >= 20) {
               this.field_4ю = 0;
               this.field_4Щ++;
               MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
               if (this.field_4Щ >= 2) {
                  this.field_4Щ = 0;
                  this.эк();
               }
            }
         }
      } else {
         this.эе();
         this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      }
   }

   private BlockPos ы(BlockPos var1, int var2) {
      if (this.В弟.getValue() && this.method_1(var1)) {
         BlockPos var3 = this.ж();
         if (var3 != null) {
            return var3;
         }
      }

      BlockPos var13 = null;
      double var4 = ЯГЮ;
      int var6 = Math.max(1, var2);

      for (int var7 = -1; var7 <= 1; var7++) {
         for (int var8 = -var6; var8 <= var6; var8++) {
            for (int var9 = -var6; var9 <= var6; var9++) {
               if (var8 != 0 || var7 != 0 || var9 != 0) {
                  BlockPos var10 = var1.offset(var8, var7, var9);
                  if (this.П(var10)) {
                     double var11 = MINECRAFT_CLIENT.player.distanceToSqr(Vec3.atCenterOf(var10));
                     if (var11 < var4) {
                        var4 = var11;
                        var13 = var10;
                     }
                  }
               }
            }
         }
      }

      if (var13 != null) {
         return var13;
      }

      BlockPos var14 = this.ы(var1, Math.max(4, var2 + 3), 4);
      return var14 != null ? var14 : var1;
   }

   private boolean method_1(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.field_1 != null ? this.field_1.equals(var1) : this.В_6bde07c5 != null && this.В_6bde07c5.blockPosition().equals(var1);
      }
   }

   private boolean й(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.ы(var1, var2);
      if (this.field_1 == null) {
         return var3.getType() == Type.MISS ? true : var1.distanceTo(var3.getLocation()) + ЯГА >= var1.distanceTo(var2);
      } else {
         return var3.getType() == Type.BLOCK && var3.getBlockPos().equals(this.field_1);
      }
   }

   private void эт() {
      this.Як = ЯГЙ;
      this.ш = null;
      this.ВШ = null;
      this.field_4э = 0;
      this.эл();
      if (this.уп() && MINECRAFT_CLIENT.getConnection() != null) {
         MINECRAFT_CLIENT.getConnection().sendChat(ЯГк);
         this.Ы_fed7dec4.ВД();
      }
   }

   private Vec3 й(BlockPos var1) {
      return new Vec3(var1.getX() + ЯГ衣, var1.getY() + ЯГш, var1.getZ() + ЯГИ);
   }

   private void й(BlockPos var1, int var2) {
      this.ы(var1, this.ы(var1, var2));
   }

   private void Лз(boolean var1) {
      if (this.衣 != null) {
         if (this.В弟.getValue()) {
            this.эл();
            this.Як = this.衣.asLong();
            this.ш = this.衣;
            this.field_4ф = 0;
            this.б.ВД();
         } else {
            BlockPos var2 = this.ы(this.衣, 8, 6, !var1);
            if (var2 != null) {
               this.衣 = var2;
            }

            if (this.уп() && this.ы("#goto " + this.衣.getX() + " " + this.衣.getZ(), true)) {
               this.Як = this.衣.asLong();
               this.ш = this.衣;
            }

            this.б.ВД();
         }
      }
   }

   private boolean у3() {
      return BaritoneIntegration.ЮЧ();
   }

   private void эл() {
      MINECRAFT_CLIENT.options.keyUp.setDown(false);
      MINECRAFT_CLIENT.options.keyDown.setDown(false);
      MINECRAFT_CLIENT.options.keyLeft.setDown(false);
      MINECRAFT_CLIENT.options.keyRight.setDown(false);
      MINECRAFT_CLIENT.options.keySprint.setDown(false);
      MINECRAFT_CLIENT.options.keyJump.setDown(false);
   }

   private BlockPos ы(BlockPos var1, int var2, int var3, boolean var4) {
      if (var1 == null) {
         return null;
      }

      BlockPos var5 = null;
      double var6 = ЯГ弟;
      int var8 = Math.max(1, var2);
      int var9 = Math.max(1, var3);

      for (int var10 = -var9; var10 <= var9; var10++) {
         for (int var11 = -var8; var11 <= var8; var11++) {
            for (int var12 = -var8; var12 <= var8; var12++) {
               BlockPos var13 = var1.offset(var11, var10, var12);
               if (this.П(var13) && (!var4 || !this.я_bbeebd87(var13))) {
                  double var14 = var11 * var11 + var10 * var10 + var12 * var12;
                  double var16 = MINECRAFT_CLIENT.player.distanceToSqr(Vec3.atCenterOf(var13));
                  double var18 = var14 * ЯГб + var16;
                  if (var18 < var6) {
                     var6 = var18;
                     var5 = var13;
                  }
               }
            }
         }
      }

      return var5;
   }

   private void эЯ() {
      this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      this.эЪ();
      this.н.clear();
      this.Ц.clear();
      this.И = null;
      this.ч.clear();
      this.Э.clear();
      this.衣 = null;
      this.ш = null;
      this.Як = ЯГз;
      this.Хl = false;
      this.ЯЙ = 0L;
      this.field_4л = 0;
      this.ь_fed7dec4.ВД();
      this.ж.ВД();
      this.弟.ВД();
      this.б.ВД();
      this.Ы_fed7dec4.ВД();
      this.l_fed7dec4.ВД();
      this.field_8.ВД();
      this.ХЫ = false;
      this.ВШ = null;
      this.field_4э = 0;
      this.Х5 = false;
      this.Хв = false;
      this.Яг = 0L;
      this.field_4ф = 0;
      this.эе();
   }

   private boolean з(double var1, double var3) {
      int var5 = (int)Math.floor(var1) >> 4;
      int var6 = (int)Math.floor(var3) >> 4;
      return MINECRAFT_CLIENT.level.getChunkSource().getChunk(var5, var6, ChunkStatus.FULL, false) != null;
   }

   private long й(int var1, int var2) {
      return (var1 & ЯГ西) << 32 | var2 & ЯГд;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (!this.В弟.getValue() && !this.у3()) {
         this.с(Ящ2);
      }

      this.эЯ();
   }

   private void эЪ() {
      this.field_1 = null;
      this.В_6bde07c5 = null;
      this.Ь = null;
      this.Хь = false;
      this.Хж = false;
      this.Х弟 = false;
      this.эд();
      this.field_4л = 0;
      this.ш = null;
      this.эН();
   }

   private void эЧ() {
      if (this.В_6bde07c5 != null && this.В_6bde07c5.isAlive()) {
         if (System.currentTimeMillis() >= this.ЯЙ) {
            this.н.add(this.В_6bde07c5.blockPosition().asLong());
            this.эЪ();
            this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
         } else {
            this.п(this.В_6bde07c5.getEyePosition());
            if (this.ж.ф(Math.max(ЯщС, this.А_6da43b94()), true) && MINECRAFT_CLIENT.gameMode != null) {
               MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, this.В_6bde07c5);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            }
         }
      } else {
         if (this.В_6bde07c5 != null) {
            this.н.add(this.В_6bde07c5.blockPosition().asLong());
         }

         this.эЪ();
         this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      }
   }

   private void эщ() {
      Vec3 var1 = this.ц();
      if (var1 != null) {
         this.п(var1);
      }
   }

   private void эO() {
      AbstractContainerMenu var1 = MINECRAFT_CLIENT.player.containerMenu;
      if (!(var1 instanceof ChestMenu)) {
         this.Лй(false);
      } else {
         int var2 = Math.max(0, var1.slots.size() - 36);
         Slot var3 = this.ы_cfa9b459(var1, var2);
         if (var3 == null) {
            this.ы(var1);
            this.Лй(true);
         } else if (this.弟.method_0(Ящк)) {
            this.ы(var1);
            this.Лй(false);
         } else if (this.ж.ф(this.А_6da43b94(), true) && MINECRAFT_CLIENT.gameMode != null) {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.containerId, var3.index, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
         }
      }
   }

   private boolean уи() {
      this.field_1 = null;
      this.В_6bde07c5 = null;
      double var1 = ЯГВ;
      if (this.Вш.getValue()) {
         double var3 = this.Я9.getValue() + ЯГЯ;
         double var5 = var3 * var3;

         for (Entity var8 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var8 instanceof MinecartChest var9) {
               double var10 = MINECRAFT_CLIENT.player.distanceToSqr(var8);
               if (!(var10 > var5)) {
                  BlockPos var12 = var9.blockPosition();
                  if (!this.н.contains(var12.asLong()) && !this.必(var12) && var10 < var1) {
                     var1 = var10;
                     this.В_6bde07c5 = var9;
                     this.field_1 = null;
                  }
               }
            }
         }
      }

      if (this.В衣.getValue()) {
         int var13 = this.Я9.getValue().intValue() + 16;
         BlockPos var4 = MINECRAFT_CLIENT.player.blockPosition();
         ArrayList<BlockPos> var14 = new ArrayList<>();
         ArrayList<BlockPos> var6 = new ArrayList<>();
         this.ы(var4, var13, this.ВИ.getValue(), var14, var6);
         double var15 = ЯГХ;

         for (BlockPos var17 : var14) {
            if (!this.н.contains(var17.asLong()) && !this.必(var17) && (!this.ВИ.getValue() || this.ы(var17, var6, var15))) {
               double var11 = MINECRAFT_CLIENT.player.distanceToSqr(Vec3.atCenterOf(var17));
               if (var11 < var1) {
                  var1 = var11;
                  this.field_1 = var17;
                  this.В_6bde07c5 = null;
               }
            }
         }
      }

      return this.field_1 != null || this.В_6bde07c5 != null;
   }

   private void эП() {
      this.эл();
      if (!this.Хб) {
         if (MINECRAFT_CLIENT.getConnection() == null) {
            this.Лй(false);
         } else {
            MINECRAFT_CLIENT.getConnection().sendCommand(ЯщО);
            this.Хб = true;
            this.弟.ВД();
            this.с(ЯщФ);
         }
      } else if (MINECRAFT_CLIENT.player.containerMenu instanceof ChestMenu) {
         this.ж.ВД();
         this.弟.ВД();
         this.В_1538e300 = KeyFinder.SearchStage.EC_STORE;
      } else {
         if (this.弟.method_0(ЯщЙ)) {
            this.Лй(false);
         }
      }
   }

   private void эШ() {
      this.эл();
      if (this.ь_fed7dec4.ф(ЯщР, true)) {
         if (this.уи()) {
            this.эш();
            this.с(this.В_6bde07c5 != null ? Ящ3 : Ящр);
            this.б.ВД();
            this.В_1538e300 = KeyFinder.SearchStage.GOTO;
         } else {
            this.эх();
            this.В_1538e300 = KeyFinder.SearchStage.EXPLORE;
         }
      }
   }

   private void эl() {
      AbstractContainerMenu var1 = MINECRAFT_CLIENT.player.containerMenu;
      if (var1 instanceof ChestMenu) {
         this.ж.ВД();
         this.В_1538e300 = KeyFinder.SearchStage.LOOT;
      } else if (this.у诶()) {
         if (!this.уц()) {
            this.Хь = false;
            this.Як = Ящы;
            this.ш = null;
            this.эш();
            this.б.ВД();
            this.В_1538e300 = KeyFinder.SearchStage.GOTO;
         } else if (!this.Хь) {
            this.эщ();
            if (this.弟.method_0(this.ш_6da43b94())) {
               this.эА();
               this.Хь = true;
               this.field_4л++;
               this.弟.ВД();
            }
         } else {
            if (this.弟.method_0(this.В_6bde07c5 != null ? Ящй : ЯщЖ)) {
               int var2 = this.В_6bde07c5 != null ? 2 : 3;
               if (this.field_4л < var2 && this.уВ()) {
                  if (this.В_6bde07c5 != null) {
                     this.эл();
                     this.эщ();
                     this.эА();
                     this.field_4л++;
                     this.弟.ВД();
                     return;
                  }

                  this.Хь = false;
                  this.Як = Ящм;
                  this.ш = null;
                  this.эш();
                  this.б.ВД();
                  this.В_1538e300 = KeyFinder.SearchStage.GOTO;
                  return;
               }

               this.эЕ();
               this.эЪ();
               this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
            }
         }
      }
   }

   private boolean ы(String var1, boolean var2) {
      if (MINECRAFT_CLIENT.getConnection() == null) {
         return false;
      }

      if (this.ВШ != null) {
         return false;
      }

      if (!var2 && !this.Ы_fed7dec4.ф(ЯГг, true)) {
         return false;
      }

      this.эл();
      MINECRAFT_CLIENT.options.keyLeft.setDown(false);
      MINECRAFT_CLIENT.options.keyRight.setDown(false);
      MINECRAFT_CLIENT.options.keyDown.setDown(false);
      this.ВШ = var1;
      this.field_4э = 5;
      this.Ы_fed7dec4.ВД();
      return true;
   }

   private void ы(BlockPos var1, int var2, boolean var3, List<BlockPos> var4, List<BlockPos> var5) {
      int var6 = var1.getX() - var2;
      int var7 = var1.getX() + var2;
      int var8 = var1.getZ() - var2;
      int var9 = var1.getZ() + var2;
      int var10 = Math.max(MINECRAFT_CLIENT.level.getMinY(), var1.getY() - var2);
      int var11 = Math.min(MINECRAFT_CLIENT.level.getMaxY(), var1.getY() + var2);
      int var12 = var6 >> 4;
      int var13 = var7 >> 4;
      int var14 = var8 >> 4;
      int var15 = var9 >> 4;

      for (int var16 = var12; var16 <= var13; var16++) {
         for (int var17 = var14; var17 <= var15; var17++) {
            if (MINECRAFT_CLIENT.level.hasChunk(var16, var17)) {
               LevelChunk var18 = (LevelChunk)MINECRAFT_CLIENT.level.getChunk(var16, var17, ChunkStatus.FULL, false);
               if (var18 != null) {
                  int var19 = var16 << 4;
                  int var20 = var17 << 4;
                  LevelChunkSection[] var21 = var18.getSections();

                  for (int var22 = 0; var22 < var21.length; var22++) {
                     LevelChunkSection var23 = var21[var22];
                     if (var23 != null && !var23.hasOnlyAir()) {
                        int var24 = MINECRAFT_CLIENT.level.getSectionYFromSectionIndex(var22) << 4;
                        if (var24 <= var11 && var24 + 15 >= var10) {
                           for (int var25 = 0; var25 < 16; var25++) {
                              int var26 = var24 + var25;
                              if (var26 >= var10 && var26 <= var11) {
                                 for (int var27 = 0; var27 < 16; var27++) {
                                    int var28 = var19 + var27;
                                    if (var28 >= var6 && var28 <= var7) {
                                       for (int var29 = 0; var29 < 16; var29++) {
                                          int var30 = var20 + var29;
                                          if (var30 >= var8 && var30 <= var9) {
                                             BlockState var31 = var23.getBlockState(var27, var25, var29);
                                             net.minecraft.world.level.block.Block var32 = var31.getBlock();
                                             if (var32 instanceof ChestBlock) {
                                                var4.add(new BlockPos(var28, var26, var30));
                                             } else if (var3 && var32 instanceof SpawnerBlock) {
                                                var5.add(new BlockPos(var28, var26, var30));
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void эд() {
      this.Хб = false;
   }

   private void ы(AbstractContainerMenu var1) {
      MINECRAFT_CLIENT.player.closeContainer();
   }

   private boolean Т(BlockPos var1) {
      BlockState var2 = MINECRAFT_CLIENT.level.getBlockState(var1);
      BlockState var3 = MINECRAFT_CLIENT.level.getBlockState(var1.above());
      BlockState var4 = MINECRAFT_CLIENT.level.getBlockState(var1.below());
      double var5 = var1.getX() + ЯГь;
      double var7 = var1.getZ() + ЯГж;
      return var2.getCollisionShape(MINECRAFT_CLIENT.level, var1).isEmpty()
         && var3.getCollisionShape(MINECRAFT_CLIENT.level, var1.above()).isEmpty()
         && !var4.getCollisionShape(MINECRAFT_CLIENT.level, var1.below()).isEmpty()
         && this.ж(var5, var1.getY() - 1.0, var7)
         && this.у(var5, var1.getY(), var7);
   }

   private boolean я_bbeebd87(BlockPos var1) {
      long var2 = this.Ю(var1);
      if (!this.ч.contains(var2) && !this.Э.contains(var2)) {
         int var4 = var1.getX() >> 3;
         int var5 = var1.getZ() >> 3;

         for (int var6 = -1; var6 <= 1; var6++) {
            for (int var7 = -1; var7 <= 1; var7++) {
               long var8 = this.й(var4 + var6, var5 + var7);
               if (this.ч.contains(var8) || this.Э.contains(var8)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private BlockPos ы(BlockPos var1, int var2, int var3) {
      return this.ы(var1, var2, var3, false);
   }

   private void Лй(int var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         if (var1 >= 0 && var1 <= 8) {
            MINECRAFT_CLIENT.player.getInventory().selected = var1;
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var1));
         }
      }
   }

   private void эя() {
      if (this.field_1 != null && this.Ы_3bdb855e.isSelected(ЯщЗ)) {
         this.Ь = this.field_1;
         this.эе();
         this.field_4у = MINECRAFT_CLIENT.player.getInventory().selected;
         this.ж.ВД();
         this.с(ЯщЕ);
         this.В_1538e300 = KeyFinder.SearchStage.BREAK;
      } else if (this.В_6bde07c5 != null && this.Ы_3bdb855e.isSelected(ЯщП)) {
         this.ж.ВД();
         this.ЯЙ = System.currentTimeMillis() + Ящ西;
         this.с(Ящд);
         this.В_1538e300 = KeyFinder.SearchStage.BREAK_CART;
      } else {
         this.эЕ();
         this.эЪ();
         this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
      }
   }

   private Slot Ю(AbstractContainerMenu var1, int var2) {
      for (int var3 = var2; var3 < var1.slots.size(); var3++) {
         Slot var4 = var1.slots.get(var3);
         ItemStack var5 = var4.getItem();
         if (!var5.isEmpty() && this.в(var5)) {
            return var4;
         }
      }

      return null;
   }

   private KeyFinder.SearchPosition ы(double var1, double var3, double var5, double var7, double var9, double var11) {
      int var13 = (int)Math.floor(var5);
      int var14 = (int)Math.floor(var7);
      KeyFinder.SearchPosition var15 = null;
      double var16 = ЯГЦ;
      double var18 = Math.hypot(var9 - var1, var11 - var3);

      for (int var20 = -1; var20 <= 1; var20++) {
         for (int var21 = -1; var21 <= 1; var21++) {
            int var22 = var13 + var20;
            int var23 = var14 + var21;
            double var24 = var22 + ЯГч;
            double var26 = var23 + ЯГЭ;
            if (!(Math.hypot(var24 - var1, var26 - var3) > ЯГ必) && !(Math.hypot(var9 - var24, var11 - var26) > var18 - ЯГщ) && this.з(var24, var26)) {
               Integer var28 = this.Ю_fb15200a(var22, var23);
               if (var28 != null && this.ж(var24, var28.intValue(), var26) && this.у(var24, var28.intValue() + 1.0, var26)) {
                  double var29 = var24 - var5;
                  double var31 = var26 - var7;
                  double var33 = var29 * var29 + var31 * var31;
                  if (var33 < var16) {
                     var16 = var33;
                     var15 = new KeyFinder.SearchPosition(var24, var28.intValue(), var26);
                  }
               }
            }
         }
      }

      return var15;
   }

   private void эЩ() {
      this.Лз(true);
   }

   private void ц(BlockPos var1) {
      this.эю();
      if (var1 != null && this.ь_10b53497 == null && this.у8()) {
         this.эл();
         double var2 = var1.getX() + ЯГЫ;
         double var4 = var1.getZ() + ЯГl;
         double var6 = var2 - MINECRAFT_CLIENT.player.getX();
         double var8 = var4 - MINECRAFT_CLIENT.player.getZ();
         double var10 = Math.hypot(var6, var8);
         if (var10 > ЯГ8) {
            KeyFinder.SearchPosition var12 = this.ы(var2, var4);
            if (var12 == null) {
               this.Лж(false);
            } else {
               this.ы(var12);
            }
         } else if (!this.з(var2, var4)) {
            this.Лж(false);
         } else {
            this.ы(var1, new KeyFinder.SearchPosition(var2, var1.getY() - 1.0, var4));
         }
      }
   }

   private BlockPos ы(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = Math.max(1, var4);

      for (int var9 = 0; var9 < var5; var9++) {
         int var10 = ThreadLocalRandom.current().nextInt(-var8, var8 + 1);
         int var11 = ThreadLocalRandom.current().nextInt(-var8, var8 + 1);
         if (Math.abs(var10) >= 8 || Math.abs(var11) >= 8) {
            BlockPos var12 = new BlockPos(var1 + var10, var2, var3 + var11);
            if (!this.я_bbeebd87(var12)) {
               BlockPos var13 = this.ы(var12, var6, var7, true);
               if (var13 != null && !this.я_bbeebd87(var13)) {
                  return var13;
               }
            }
         }
      }

      return null;
   }

   private boolean уц() {
      Vec3 var1 = this.ц();
      if (var1 == null) {
         return false;
      }

      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      return var2.distanceToSqr(var1) > ЯГе ? false : this.й(var2, var1);
   }

   private void эЬ() {
      boolean var1 = this.В弟.getValue();
      if (var1 != this.Х5) {
         this.Х5 = var1;
         this.ВШ = null;
         this.field_4э = 0;
         this.Як = ЯщO;
         this.ш = null;
         this.Хв = false;
         this.Яг = 0L;
         this.field_4ф = 0;
         this.эН();
         this.эл();
         if (var1 && this.у3() && MINECRAFT_CLIENT.getConnection() != null) {
            MINECRAFT_CLIENT.getConnection().sendChat(Ящ7);
         }

         if (!var1) {
            if (this.В_1538e300 != KeyFinder.SearchStage.GOTO || this.В_6bde07c5 == null && this.field_1 == null) {
               if (this.В_1538e300 == KeyFinder.SearchStage.EXPLORE && this.衣 != null) {
                  this.эЩ();
               }
            } else {
               this.эш();
            }
         }
      }
   }

   private void ы(BlockPos var1, BlockPos var2) {
      if (this.В弟.getValue()) {
         this.эл();
         this.Як = var1.asLong();
         this.ш = var2;
         this.field_4ф = 0;
      } else if (this.уп() && this.ы("#goto " + var2.getX() + " " + var2.getY() + " " + var2.getZ(), true)) {
         this.Як = var1.asLong();
         this.ш = var2;
      }
   }

   private boolean ы(AbstractContainerMenu var1, int var2, ItemStack var3) {
      for (int var4 = var2; var4 < var1.slots.size(); var4++) {
         ItemStack var5 = var1.slots.get(var4).getItem();
         if (var5.isEmpty()) {
            return true;
         }

         if (ItemStack.isSameItemSameComponents(var5, var3) && var5.getCount() < var5.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private boolean у(double var1, double var3, double var5) {
      AABB var7 = new AABB(var1 - ЯГу, var3, var5 - ЯГ2, var1 + ЯГф, var3 + ЯГи, var5 + ЯГ9);
      return MINECRAFT_CLIENT.level.noCollision(MINECRAFT_CLIENT.player, var7);
   }

   private void Лж(boolean var1) {
      this.field_4ф++;
      if (this.field_4ф >= 100) {
         this.field_4ф = 0;
         if (var1) {
            this.с(ЯГТ);
            this.衣 = null;
            this.ш = null;
            this.Як = ЯГО;
         } else {
            this.с(ЯГФ);
            this.эЕ();
            this.эЪ();
            this.В_1538e300 = KeyFinder.SearchStage.SEARCH;
         }
      }
   }

   private long Ю_6da43b94(int var1, int var2) {
      return this.й(var1 >> 3, var2 >> 3);
   }

   private void эО() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         this.эл();
         MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
         if (this.field_42 > 0) {
            this.field_42--;
         } else {
            this.Лй(this.field_4у);
            this.Х8 = false;
            this.field_42 = 0;
            this.field_4ю = 0;
            this.field_4Щ = 0;
         }
      } else {
         this.эе();
      }
   }

   private boolean уч() {
      return this.уц();
   }

   private void эш() {
      if (this.В_6bde07c5 != null) {
         this.й(this.В_6bde07c5.blockPosition(), 1);
      } else if (this.field_1 != null) {
         this.ч(this.field_1);
      }
   }

   private boolean в(ItemStack var1) {
      return щ.contains(var1.getItem());
   }

   private void п(Vec3 var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var3 = var1.subtract(var2);
      double var4 = Math.sqrt(var3.x * var3.x + var3.z * var3.z);
      float var6 = (float)Math.toDegrees(Math.atan2(var3.z, var3.x)) - ЯГЗ;
      float var7 = (float)(-Math.toDegrees(Math.atan2(var3.y, var4)));
      MINECRAFT_CLIENT.player.setYRot(Mth.wrapDegrees(var6));
      MINECRAFT_CLIENT.player.setXRot(Mth.clamp(var7, ЯГЕ, ЯГП));
   }

   private boolean у诶() {
      return !this.В弟.getValue() || !this.Хв || System.nanoTime() - this.Яг >= ЯГ4;
   }

   private void эТ() {
      AbstractContainerMenu var1 = MINECRAFT_CLIENT.player.containerMenu;
      if (!(var1 instanceof ChestMenu)) {
         this.эя();
      } else {
         int var2 = Math.max(0, var1.slots.size() - 36);
         Slot var3 = null;

         for (int var4 = 0; var4 < var2; var4++) {
            Slot var5 = var1.slots.get(var4);
            ItemStack var6 = var5.getItem();
            if (!var6.isEmpty() && this.с(var6)) {
               var3 = var5;
               break;
            }
         }

         if (var3 == null) {
            this.ы(var1);
            this.эя();
         } else {
            if (this.Вж.getValue() && !this.ы(var1, var2, var3.getItem()) && this.ы_bbeebd87(var1, var2)) {
               Slot var7 = this.Ю(var1, var2);
               if (var7 != null) {
                  if (this.ж.ф(this.А_6da43b94(), true)) {
                     if (MINECRAFT_CLIENT.gameMode != null) {
                        MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.containerId, var7.index, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
                     }

                     if (!this.Х弟) {
                        this.с(ЯщК);
                     }

                     this.Х弟 = true;
                  }

                  return;
               }
            }

            if (this.ж.ф(this.А_6da43b94(), true)) {
               ItemStack var8 = var3.getItem();
               boolean var9 = this.Х(var8);
               if (MINECRAFT_CLIENT.gameMode != null) {
                  MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.containerId, var3.index, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
               }

               if (!this.Хж) {
                  this.с(var9 ? ЯщI : ЯщТ);
               }

               this.Хж = true;
            }
         }
      }
   }

   private boolean уВ() {
      return this.В_6bde07c5 != null ? this.В_6bde07c5.isAlive() : this.field_1 != null && MINECRAFT_CLIENT.level.getBlockState(this.field_1).getBlock() instanceof ChestBlock;
   }

   private void эЦ() {
      if (this.ВЫ.getValue()) {
         if (MINECRAFT_CLIENT.player.getFoodData().getFoodLevel() < this.ЯР.getValue().intValue()) {
            if (this.l_fed7dec4.ф(ЯГ0, true)) {
               if (MINECRAFT_CLIENT.getConnection() != null) {
                  MINECRAFT_CLIENT.getConnection().sendCommand(ЯГх);
               }

               this.с(ЯГУ);
            }
         }
      }
   }

   private boolean ы(BlockPos var1, double var2) {
      double var4 = MINECRAFT_CLIENT.player.getX() - (var1.getX() + ЯГБ);
      double var6 = MINECRAFT_CLIENT.player.getZ() - (var1.getZ() + ЯГС);
      return var4 * var4 + var6 * var6 <= var2 * var2;
   }

   private boolean с(ItemStack var1) {
      return this.l_3bdb855e.isSelected(ЯГД) && this.Х(var1)
         || this.l_3bdb855e.isSelected(ЯГЪ) && var1.is(Items.ENCHANTED_GOLDEN_APPLE)
         || this.l_3bdb855e.isSelected(ЯГт) && var1.is(Items.GOLDEN_APPLE)
         || this.l_3bdb855e.isSelected(ЯГа) && var1.is(Items.ENCHANTED_BOOK);
   }

   private void ы(KeyFinder.SearchPosition var1) {
      this.field_4ф = 0;
      double var2 = Math.floor(var1.x()) + ЯГO;
      double var4 = Math.floor(var1.z()) + ЯГ7;
      MINECRAFT_CLIENT.player.setPos(var2, var1.y(), var4);
      this.Хв = true;
      this.Яг = System.nanoTime();
   }

   private void ч(BlockPos var1) {
      this.ы(var1, this.ы(var1, 1));
   }

   private void э5() {
      if (this.ь_10b53497 != null && System.nanoTime() >= this.Ях) {
         BlockPos var1 = this.ь_10b53497;
         this.ь_10b53497 = null;
         this.Ях = 0L;
         if (this.В弟.getValue() && (this.field_1 != null || this.В_6bde07c5 != null)) {
            if (this.З(var1)) {
               this.ЯУ = ЯГ3;
               this.field_4и = 0;
            } else {
               long var2 = var1.asLong();
               if (this.ЯУ == var2) {
                  this.field_4и++;
               } else {
                  this.ЯУ = var2;
                  this.field_4и = 1;
               }

               if (this.field_4и >= 3) {
                  this.必.add(var2);
                  if (var1.equals(this.ш)) {
                     this.ш = null;
                     this.Як = ЯГр;
                  }

                  this.ЯУ = ЯГ6;
                  this.field_4и = 0;
                  this.с(ЯГ_);
               }
            }
         }
      }
   }

   private void эН() {
      this.Я0 = ЯГК;
      this.ь_10b53497 = null;
      this.Ях = 0L;
      this.ЯУ = ЯГI;
      this.field_4и = 0;
      this.必.clear();
   }

   private BlockPos ж() {
      this.эю();
      Vec3 var1 = this.ц();
      BlockPos var2 = this.field_1 != null ? this.field_1 : (this.В_6bde07c5 != null ? this.В_6bde07c5.blockPosition() : null);
      if (var1 != null && var2 != null) {
         BlockPos var3 = null;
         double var4 = ЯГЧ;

         for (int var6 = -2; var6 <= 2; var6++) {
            for (int var7 = -3; var7 <= 3; var7++) {
               for (int var8 = -3; var8 <= 3; var8++) {
                  BlockPos var9 = var2.offset(var7, var6, var8);
                  if (this.l(var9)) {
                     Vec3 var10 = new Vec3(var9.getX() + ЯГЛ, var9.getY() - 1.0, var9.getZ() + ЯГ诶);
                     Vec3 var11 = this.й(var9);
                     double var12 = var11.distanceToSqr(var1) * ЯГ1 + MINECRAFT_CLIENT.player.distanceToSqr(var10);
                     if (var12 < var4) {
                        var4 = var12;
                        var3 = var9;
                     }
                  }
               }
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private boolean З(BlockPos var1) {
      double var2 = MINECRAFT_CLIENT.player.getX() - (var1.getX() + ЯГШ);
      double var4 = MINECRAFT_CLIENT.player.getZ() - (var1.getZ() + ЯГы);
      double var6 = var1.getY() - 1.0;
      double var8 = Math.min(Math.abs(MINECRAFT_CLIENT.player.getY() - var6), Math.abs(MINECRAFT_CLIENT.player.getY() - var1.getY()));
      return var2 * var2 + var4 * var4 <= ЯГй && var8 <= ЯГЖ && this.уц();
   }

   private record SearchPosition(double x, double y, double z) {
   }

   private enum SearchStage {
      SEARCH,
      GOTO,
      OPEN,
      LOOT,
      EC_OPEN,
      EC_STORE,
      BREAK,
      BREAK_CART,
      EXPLORE;
   }
}
