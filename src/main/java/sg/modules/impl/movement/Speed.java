package sg.modules.impl.movement;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.events.impl.RelativeMovementEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.events.impl.MovementInputEvent;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class Speed extends ClientModule {
   final Map<Entity, Vec3> сН = new HashMap<>();
   private final SingleChoiceSetting х;
   private final FloatSetting ЯИ;
   private final FloatSetting Яь;
   private final FloatSetting Яж;
   private final BooleanSetting ся;
   private final SingleChoiceSetting У;
   private final FloatSetting Я弟;
   private final BooleanSetting Вс;
   private final FloatSetting Яб;
   private final FloatSetting ЯЫ;
   private final FloatSetting Яl;
   private int А2;
   private int Аф;
   private long Яп;
   private double Вщ;
   private double ВГ;
   private boolean ЯШ;
   private int Аи;
   private final EventListener<MovementInputEvent> ВУ;
   private final EventListener<PlayerTickEvent> ВЗ;
   private final EventListener<PacketReceiveEvent> ВЕ;
   private final EventListener<PlayerMovementSyncEvent.Post> ВП;
   private final EventListener<RelativeMovementEvent> В西;
   private final EventListener<PlayerMovementEvent> Вд;
   private static final String Яlи = "Speed";
   private static final String Яl9 = "Ускоряет скорость передвижения";
   private static final String Яlо = "Мод";
   private static final String ЯlO = "Grim 72";
   private static final String Яl7 = "Grim колиззия";
   private static final String ЯlР = "MetaHvH";
   private static final String Яl3 = "HVH";
   private static final String Яlр = "Vanila";
   private static final String Яl6 = "Скорость";
   private static final float Яl_ = 0.4F;
   private static final float ЯlШ = 0.1F;
   private static final String Яlы = "Скорость 2";
   private static final float Яlй = 3.4F;
   private static final float ЯlЖ = 5.0F;
   private static final float Яlм = 0.1F;
   private static final String ЯlК = "HolyWorld скорость";
   private static final float ЯlI = 1.6F;
   private static final float ЯlТ = 0.8F;
   private static final float ЯlО = 3.0F;
   private static final float ЯlФ = 0.05F;
   private static final String ЯlЙ = "Авто прыжок";
   private static final String Яlк = "HVH режим";
   private static final String Яlг = "Default";
   private static final String Яl0 = "Custom";
   private static final String Яlх = "HVH скорость";
   private static final float ЯlУ = 0.2F;
   private static final float ЯlЗ = 0.2F;
   private static final float ЯlЕ = 1.05F;
   private static final float ЯlП = 0.01F;
   private static final String Яl西 = "HVH буст с дамагом";
   private static final String Яlд = "HVH значение буста";
   private static final float ЯlБ = 0.7F;
   private static final float ЯlС = 0.1F;
   private static final float Яlз = 5.0F;
   private static final float Яlц = 0.1F;
   private static final String Яlя = "HVH длительность буста";
   private static final float Я8с = 700.0F;
   private static final float Я8В = 100.0F;
   private static final float Я8Я = 2000.0F;
   private static final float Я8Х = 100.0F;
   private static final String Я8е = "Vanila скорость";
   private static final float Я8А = 0.5F;
   private static final float Я84 = 0.3F;
   private static final float Я8п = 0.1F;
   private static final String Я8Д = "HVH";
   private static final String Я8Ъ = "MetaHvH";
   private static final String Я8т = "Grim колиззия";
   private static final double Я8а = 0.35;
   private static final double Я8Ю = 0.067;
   private static final double Я8Ч = 5.0;
   private static final double Я8Л = 0.067;
   private static final String Я8诶 = "Custom";
   private static final float Я81 = 0.49665F;
   private static final float Я8Ь = 0.41598004F;
   private static final float Я8衣 = 0.43F;
   private static final float Я8ш = 0.36F;
   private static final float Я8И = 0.2924F;
   private static final float Я8ь = 0.24480002F;
   private static final float Я8ж = 0.835F;
   private static final float Я8弟 = 1.435F;
   private static final float Я8б = 10.0F;
   private static final float Я8Ы = 1.3F;
   private static final String Я8l = "Шар Геракла 2";
   private static final String Я88 = "Шар CHAMPION";
   private static final String Я85 = "Шар GOD";
   private static final String Я8в = "Талисман Венома";
   private static final String Я8ъ = "КУБИК-РУБИК";
   private static final double Я8М = 3.0;
   private static final double Я8Н = 3.5;
   private static final String Я8н = "AttributeModifiers";
   private static final String Я8Ц = "AttributeName";
   private static final String Я8ч = "Amount";
   private static final double Я8Э = 3.0;
   private static final double Я8必 = 3.5;
   private static final String Я8щ = "minecraft:generic.armor";
   private static final String Я8Г = "minecraft:armor";
   private static final String Я8л = "generic.armor";
   private static final String Я8э = ":armor";
   private static final String Я8ю = ".armor";
   private static final float Я8Щ = 0.452154F;
   private static final float Я8у = 0.520174F;
   private static final float Я82 = 0.591416F;
   private static final float Я8ф = 0.358F;
   private static final float Я8и = 0.358F;
   private static final double Я89 = 20.0;
   private static final float Я8о = -45.0F;
   private static final float Я8O = 45.0F;
   private static final float Я87 = 45.0F;
   private static final float Я8Р = -45.0F;
   private static final float Я83 = -1.0F;
   private static final float Я8р = 90.0F;
   private static final float Я86 = 90.0F;
   private static final String Я8_ = "Vanila";
   private static final double Я8Ш = 1.0E-4;
   private static final double Я8ы = 0.97;
   private static final double Я8й = 0.7;
   private static final double Я8Ж = 0.02;
   private static final double Я8м = 0.08;
   private static final String Я8К = "Grim 72";
   private static final String Я8I = "HolyWorld";
   private static final String Я8Т = "Grim 72";
   private static final double Я8О = 0.03;
   private static final double Я8Ф = 0.03;
   private static final double Я8Й = 0.085;
   private static final double Я8к = 0.03;
   private static final double Я8г = -0.08;
   private static final String Я80 = "Grim 72";
   private static final String Я8х = "HolyWorld";
   private static final String Я8У = "Grim 72";
   private static final String Я8З = "HVH";
   private static final String Я8Е = "Grim 72";
   private static final String Я8П = "HolyWorld";
   private static final String Я8西 = "Grim 72";
   private static final String Я8д = "HolyWorld";
   private static final String Я8Б = "Vanila";
   private static final String Я8С = "HVH";
   private static final String Я8з = "HVH";
   private static final String Я8ц = "HVH";
   private static final String Я8я = "HVH";
   private static final String Я5с = "Custom";
   private static final String Я5В = "HVH";
   private static final String Я5Я = "MetaHvH";
   private static final String Я5Х = "HolyWorld";
   private static final String Я5е = "Grim 72";
   private static final String Я5А = "Grim 72";

   @Override
   public void onDisable() {
      this.Цк();
   }

   private boolean о诶() {
      return MINECRAFT_CLIENT.player == null
         || !this.х.isSelected(Я8西) && !this.х.isSelected(Я8д)
         || MINECRAFT_CLIENT.level == null
         || MINECRAFT_CLIENT.getConnection() == null
         || !MovementUtils.hasMovementInput()
         || MINECRAFT_CLIENT.player.isPassenger()
         || MINECRAFT_CLIENT.player.getAbilities().flying;
   }

   private boolean оО() {
      return MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.getConnection() != null
         && MovementUtils.hasMovementInput()
         && !MINECRAFT_CLIENT.player.isPassenger()
         && !MINECRAFT_CLIENT.player.getAbilities().flying
         && this.оФ();
   }

   private void Цо() {
      this.Аи = 0;
      this.ЦХ();
   }

   public Speed() {
      super(Яlи, Яl9, ModuleCategory.MOVEMENT);
      this.х = new SingleChoiceSetting(
         Яlо, new ChoiceOption(ЯlO), new ChoiceOption(Яl7), new ChoiceOption(ЯlР), new ChoiceOption(Яl3), new ChoiceOption(Яlр)
      );
      this.ЯИ = new FloatSetting(Яl6, Яl_, 0.0F, 2.0F).withStep(ЯlШ).visibleWhen(() -> this.х.isSelected(Я5А));
      this.Яь = new FloatSetting(Яlы, Яlй, 1.0F, ЯlЖ).withStep(Яlм).visibleWhen(() -> this.х.isSelected(Я5е));
      this.Яж = new FloatSetting(ЯlК, ЯlI, ЯlТ, ЯlО).withStep(ЯlФ).visibleWhen(() -> this.х.isSelected(Я5Х));
      this.ся = new BooleanSetting(ЯlЙ, true).visibleWhen(() -> this.х.isSelected(Я5Я));
      this.У = new SingleChoiceSetting(Яlк, new ChoiceOption(Яlг), new ChoiceOption(Яl0)).visibleWhen(() -> this.х.isSelected(Я5В));
      this.Я弟 = new FloatSetting(Яlх, ЯlУ, ЯlЗ, ЯlЕ).withStep(ЯlП).visibleWhen(() -> this.х.isSelected(Я8я) && this.У.isSelected(Я5с));
      this.Вс = new BooleanSetting(Яl西, false).visibleWhen(() -> this.х.isSelected(Я8ц));
      this.Яб = new FloatSetting(Яlд, ЯlБ, ЯlС, Яlз).withStep(Яlц).visibleWhen(() -> this.х.isSelected(Я8з) && this.Вс.getValue());
      this.ЯЫ = new FloatSetting(Яlя, Я8с, Я8В, Я8Я).withStep(Я8Х).visibleWhen(() -> this.х.isSelected(Я8С) && this.Вс.getValue());
      this.Яl = new FloatSetting(Я8е, Я8А, Я84, 2.0F).withStep(Я8п).visibleWhen(() -> this.х.isSelected(Я8Б));
      this.Аф = -1;
      this.ВУ = this::х;
      this.ВЗ = this::Чх;
      this.ВЕ = this::method_3;
      this.ВП = this::о;
      this.В西 = this::I;
      this.Вд = this::ъ;
      this.addSettings(this.х, this.ЯИ, this.Яь, this.Яж, this.ся, this.У, this.Я弟, this.Вс, this.Яб, this.ЯЫ, this.Яl);
   }

   private double method__(Entity var1) {
      Vec3 var2 = var1.position();
      Vec3 var3 = this.сН.put(var1, var2);
      if (var3 == null) {
         return 0.0;
      }

      double var4 = var2.x - var3.x;
      double var6 = var2.z - var3.z;
      return Math.sqrt(var4 * var4 + var6 * var6) * Я89;
   }

   private boolean оФ() {
      return MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) || this.Чэ() != -1;
   }

   public static void Чк(float var0) {
      ClientTimerSpeed.Оэ(var0);
   }

   private void ЦЦ() {
      if (this.оО() && !MINECRAFT_CLIENT.player.isFallFlying() && !MINECRAFT_CLIENT.player.onGround() && !(MINECRAFT_CLIENT.player.getDeltaMovement().y >= 0.0) && this.Аи <= 0) {
         int var1 = this.Чэ();
         if (MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
            this.Аи = 4;
         } else if (var1 != -1) {
            if (this.Аф == -1) {
               this.Аф = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
            }

            if (MINECRAFT_CLIENT.player.getInventory().getSelectedSlot() != var1) {
               MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var1);
            } else {
               MINECRAFT_CLIENT.getConnection()
                  .send(
                     new ServerboundPlayerActionPacket(
                        net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN
                     )
                  );
               MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
               MINECRAFT_CLIENT.getConnection()
                  .send(
                     new ServerboundPlayerActionPacket(
                        net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN
                     )
                  );
               this.ЦХ();
               this.Аи = 4;
            }
         }
      }
   }

   private int Чэ() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var1).is(Items.ELYTRA)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean л(String var1) {
      String var2 = var1 == null ? "" : var1.toLowerCase(Locale.ROOT);
      return var2.equals(Я8щ) || var2.equals(Я8Г) || var2.equals(Я8л) || var2.endsWith(Я8э) || var2.endsWith(Я8ю);
   }

   private void Ц西() {
      if (!this.оО()) {
         this.Цо();
      } else {
         if (this.Аи > 0) {
            this.Аи--;
         }

         if (MINECRAFT_CLIENT.player.isFallFlying()) {
            Vec3 var1 = MINECRAFT_CLIENT.player.getViewVector(1.0F);
            double var2 = this.Яж.getValue().floatValue();
            MINECRAFT_CLIENT.player.setDeltaMovement(var1.x * var2, Math.min(var1.y * var2, Я8г), var1.z * var2);
         }
      }
   }

   public void method_3(PacketReceiveEvent var1) {
      if (this.х.isSelected(Я8З) && this.Вс.getValue() && var1.getPacket() instanceof ClientboundSetEntityMotionPacket var2 && MINECRAFT_CLIENT.player != null && var2.id() == MINECRAFT_CLIENT.player.getId()) {
         this.Яп = System.currentTimeMillis();
      }

      if (!this.о诶()) {
         if (var1.getPacket() instanceof ClientboundPlayerPositionPacket) {
            if (this.х.isSelected(Я8Е)) {
               if (this.А2 % 2 == 1) {
                  this.А2++;
               }

               Чк(1.0F);
            } else if (this.х.isSelected(Я8П)) {
               this.Аи = 5;
               this.ЦХ();
            }
         }
      }
   }

   public void Чх(PlayerTickEvent var1) {
      if (this.х.isSelected(Я8Д)) {
         this.Ц_();
      } else if (this.х.isSelected(Я8Ъ)) {
         this.Цъ();
      } else if (this.х.isSelected(Я8т)) {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MovementUtils.hasMovementInput()) {
            double var2 = Я8а;
            List<Entity> var4 = MINECRAFT_CLIENT.level
               .getEntities(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getBoundingBox().inflate(var2), var0 -> var0.isAlive() && (var0 instanceof Player || var0 instanceof Boat));
            if (!var4.isEmpty()) {
               double var5 = Я8Ю;

               for (Entity var8 : var4) {
                  if (var8 instanceof Player) {
                     double var9 = this.method__(var8);
                     if (var9 >= Я8Ч) {
                        var5 = Я8Л;
                        break;
                     }
                  }
               }

               double[] var11 = this.I(var5);
               MINECRAFT_CLIENT.player.push(var11[0], 0.0, var11[1]);
            }
         }
      }
   }

   private void ЦХ() {
      if (this.Аф != -1 && MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(this.Аф);
      }

      this.Аф = -1;
   }

   private double[] method__(double var1, double var3) {
      return new double[]{var1, var3};
   }

   @Override
   public void onEnable() {
      this.Цк();
   }

   private void ъ(PlayerMovementEvent var1) {
      if (this.х.isSelected(Я8_) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!MINECRAFT_CLIENT.player.getAbilities().flying && !MINECRAFT_CLIENT.player.isFallFlying() && !MINECRAFT_CLIENT.player.isPassenger()) {
            Vec3 var2 = var1.getMovement();
            if (!MovementUtils.hasMovementInput()) {
               this.ЯШ = false;
               if (MINECRAFT_CLIENT.player.onGround()) {
                  var1.setMovement(new Vec3(0.0, var2.y, 0.0));
               }
            } else {
               double var3 = MovementUtils.getMovementDirection(true);
               double var5 = this.Яl.getValue().floatValue();
               double var7 = -Math.sin(var3);
               double var9 = Math.cos(var3);
               double var11 = var7 * var5;
               double var13 = var9 * var5;
               boolean var15 = MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isInLava();
               if (!MINECRAFT_CLIENT.player.onGround() || var15) {
                  double var16 = this.ЯШ ? this.Вщ : var2.x;
                  double var18 = this.ЯШ ? this.ВГ : var2.z;
                  double var20 = Math.hypot(var16, var18);
                  double var22 = var20 < Я8Ш ? 1.0 : (var16 * var7 + var18 * var9) / var20;
                  double var24 = var15 ? Я8ы : Я8й;
                  double var26 = var15 ? Я8Ж : Я8м;
                  if (var22 < var24) {
                     double var28 = var11 - var16;
                     double var30 = var13 - var18;
                     double var32 = Math.hypot(var28, var30);
                     if (var32 > var26) {
                        var11 = var16 + var28 / var32 * var26;
                        var13 = var18 + var30 / var32 * var26;
                     }
                  }
               }

               this.ЯШ = true;
               double[] var36 = this.method__(var11, var13);
               var11 = var36[0];
               var13 = var36[1];
               this.Вщ = var11;
               this.ВГ = var13;
               MINECRAFT_CLIENT.player.setDeltaMovement(var11, MINECRAFT_CLIENT.player.getDeltaMovement().y, var13);
               var1.setMovement(new Vec3(var11, var2.y, var13));
            }
         } else {
            this.ЯШ = false;
         }
      }
   }

   private void Цк() {
      this.ЦХ();
      this.А2 = 0;
      this.Аи = 0;
      this.ЯШ = false;
      Чк(1.0F);
   }

   private void Цъ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.player.getAbilities().flying) {
         MobEffectInstance var2 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED);
         float var1;
         if (var2 != null) {
            if (var2.getAmplifier() == 0) {
               var1 = Я8Щ;
            } else if (var2.getAmplifier() == 1) {
               var1 = Я8у;
            } else if (var2.getAmplifier() >= 2) {
               var1 = Я82;
            } else {
               var1 = Я8ф;
            }
         } else {
            var1 = Я8и;
         }

         if (!MINECRAFT_CLIENT.player.isFallFlying() && !MINECRAFT_CLIENT.player.isInWater() && !MINECRAFT_CLIENT.player.isEyeInFluid(FluidTags.WATER) && !MINECRAFT_CLIENT.player.onGround()) {
            MovementUtils.setHorizontalSpeedOrStop(var1);
         }

         if (this.ся.getValue() && MovementUtils.hasMovementInput() && MINECRAFT_CLIENT.player.onGround() && !MINECRAFT_CLIENT.options.keyJump.isDown()) {
            MINECRAFT_CLIENT.player.jumpFromGround();
         }
      }
   }

   public void х(MovementInputEvent var1) {
   }

   private boolean о5() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
      if (!var1.isEmpty() && var1.is(Items.PLAYER_HEAD)) {
         ItemAttributeModifiers var2 = var1.get(DataComponents.ATTRIBUTE_MODIFIERS);
         if (var2 != null) {
            for (Entry var4 : var2.modifiers()) {
               if (this.л(var4.attribute().unwrapKey().map(var0 -> var0.identifier().toString()).orElse(""))) {
                  double var5 = var4.modifier().amount();
                  if (var5 == Я8М || var5 == Я8Н) {
                     return true;
                  }
               }
            }
         }

         CustomData var11 = var1.get(DataComponents.CUSTOM_DATA);
         if (var11 == null) {
            return false;
         }

         Optional var12 = var11.copyTag().getList(Я8н);
         if (var12.isEmpty()) {
            return false;
         }

         for (Tag var6 : (ListTag)var12.get()) {
            if (var6 instanceof CompoundTag var7) {
               String var8 = var7.getString(Я8Ц).orElse("");
               double var9 = var7.getDouble(Я8ч).orElse(0.0);
               if (this.л(var8) && (var9 == Я8Э || var9 == Я8必)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void о(PlayerMovementSyncEvent.Post var1) {
      if (this.о诶() && this.х.isSelected(Я80)) {
         this.Цк();
      } else if (this.х.isSelected(Я8х)) {
         this.ЦЦ();
      } else {
         if (this.х.isSelected(Я8У) && this.А2 % 2 == 0) {
            Чк(this.ЯИ.getValue());
            MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
            MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
         }
      }
   }

   private void Ц_() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.player.getAbilities().flying && !MINECRAFT_CLIENT.player.isFallFlying()) {
         MobEffectInstance var2 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED);
         MobEffectInstance var3 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SLOWNESS);
         String var4 = MINECRAFT_CLIENT.player.getOffhandItem().getHoverName().getString();
         boolean var5 = this.А(var4);
         float var1;
         if (this.У.isSelected(Я8诶)) {
            var1 = this.Я弟.getValue();
         } else if (var2 != null && var2.getAmplifier() == 2) {
            var1 = var5 ? Я81 : Я8Ь;
         } else if (var2 != null && var2.getAmplifier() == 1) {
            var1 = var5 ? Я8衣 : Я8ш;
         } else {
            var1 = var5 ? Я8И : Я8ь;
         }

         if (var3 != null) {
            var1 *= Я8ж;
         }

         if (!MINECRAFT_CLIENT.player.onGround()) {
            var1 *= Я8弟;
         }

         if (this.Вс.getValue() && System.currentTimeMillis() - this.Яп <= this.ЯЫ.getValue().longValue()) {
            var1 += this.Яб.getValue() / Я8б;
         }

         if (this.о5()) {
            var1 /= Я8Ы;
         }

         MovementUtils.setHorizontalSpeed(var1);
      }
   }

   private double[] I(double var1) {
      float var3 = MINECRAFT_CLIENT.player.zza;
      float var4 = MINECRAFT_CLIENT.player.xxa;
      float var5 = MINECRAFT_CLIENT.player.getYRot();
      if (var3 != 0.0F) {
         if (var4 > 0.0F) {
            var5 += var3 > 0.0F ? Я8о : Я8O;
         } else if (var4 < 0.0F) {
            var5 += var3 > 0.0F ? Я87 : Я8Р;
         }

         var4 = 0.0F;
         if (var3 > 0.0F) {
            var3 = 1.0F;
         } else if (var3 < 0.0F) {
            var3 = Я83;
         }
      }

      double var6 = Math.sin(Math.toRadians(var5 + Я8р));
      double var8 = Math.cos(Math.toRadians(var5 + Я86));
      double var10 = var3 * var1 * var8 + var4 * var1 * var6;
      double var12 = var3 * var1 * var6 - var4 * var1 * var8;
      return new double[]{var10, var12};
   }

   public void I(RelativeMovementEvent var1) {
      if (this.о诶() && this.х.isSelected(Я8К)) {
         this.Цк();
      } else if (this.х.isSelected(Я8I)) {
         this.Ц西();
      } else {
         if (this.х.isSelected(Я8Т)) {
            Чк(this.Яь.getValue());
            if (this.А2 > 3) {
               double var2 = Я8О;
               if (this.А2 % 2 == 0) {
                  MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().add(0.0, Я8Ф, 0.0));
                  var2 = MINECRAFT_CLIENT.player.onGround() ? Я8Й : Я8к;
               }

               double var4 = Math.toRadians(MovementUtils.getMovementDirection(false));
               double var6 = MovementUtils.getKeyMovementDirectionVector()[0];
               double var8 = MovementUtils.getKeyMovementDirectionVector()[1];
               MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().add(var6 * var2, 0.0, var8 * var2));
            }

            this.А2++;
         }
      }
   }

   private boolean А(String var1) {
      return var1.contains(Я8l) || var1.contains(Я88) || var1.contains(Я85) || var1.contains(Я8в) || var1.contains(Я8ъ);
   }
}
