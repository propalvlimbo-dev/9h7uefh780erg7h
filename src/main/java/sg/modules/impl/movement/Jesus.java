package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public class Jesus extends ClientModule {
   private final SingleChoiceSetting 弟;
   @SerializedName("п")
   private final MultiChoiceSetting п_at_f61e84cb;
   private final FloatSetting х;
   private final FloatSetting У;
   private final FloatSetting З;
   private final FloatSetting Е;
   private long су;
   private long с2;
   private float ЯО;
   private long сф;
   private boolean ср;
   private Boolean В;
   private long си;
   private float ЯФ;
   private float ЯЙ;
   private int В西;
   private final EventListener<PacketReceiveEvent> с诶;
   private final EventListener<PlayerTickEvent> с1;
   private final EventListener<PlayerMovementEvent> сЬ;
   private final EventListener<PlayerMovementSyncEvent> с衣;
   private static final String сч必 = "Jesus";
   private static final String счщ = "Ходьба по воде";
   private static final String счГ = "Mode";
   private static final String счл = "MetaHvH";
   private static final String счэ = "Дополнения";
   private static final String счю = "Авто буст";
   private static final String счЩ = "Интервал бустов";
   private static final float счу = 1200.0F;
   private static final float сч2 = 800.0F;
   private static final float счф = 2000.0F;
   private static final float счи = 50.0F;
   private static final String сч9 = "Сила авто буста";
   private static final float счо = 0.1F;
   private static final float счO = 0.05F;
   private static final float сч7 = 1.5F;
   private static final float счР = 0.01F;
   private static final String сч3 = "Длительность буста";
   private static final float счр = 300.0F;
   private static final float сч6 = 200.0F;
   private static final float сч_ = 1000.0F;
   private static final float счШ = 50.0F;
   private static final String счы = "Антифлаг";
   private static final float счй = 4000.0F;
   private static final float счЖ = 2000.0F;
   private static final float счм = 15000.0F;
   private static final float счК = 500.0F;
   private static final String счI = "minecraft:generic.armor";
   private static final String счТ = "minecraft:armor";
   private static final String счО = "generic.armor";
   private static final double счФ = 3.5;
   private static final String счЙ = "minecraft:generic.armor_toughness";
   private static final String счк = "minecraft:armor_toughness";
   private static final String счг = "generic.armor_toughness";
   private static final double сч0 = 3.0;
   private static final String счх = "minecraft:generic.attack_damage";
   private static final String счУ = "minecraft:attack_damage";
   private static final String счЗ = "generic.attack_damage";
   private static final double счЕ = 3.0;
   private static final String счП = "minecraft:generic.movement_speed";
   private static final String сч西 = "minecraft:movement_speed";
   private static final String счд = "generic.movement_speed";
   private static final double счБ = 0.1;
   private static final String счС = "minecraft:generic.movement_speed";
   private static final String счз = "minecraft:movement_speed";
   private static final String счц = "generic.movement_speed";
   private static final float счя = 2.1F;
   private static final long сЭс = 30L;
   private static final float сЭВ = 0.36F;
   private static final float сЭЯ = 0.2F;
   private static final float сЭХ = 0.15F;
   private static final float сЭе = 1.25F;
   private static final float сЭА = 0.8F;
   private static final String сЭ4 = "Авто буст";
   private static final long сЭп = 1500L;
   private static final double сЭД = 0.25;
   private static final long сЭЪ = 30L;
   private static final float сЭт = 0.25F;
   private static final float сЭа = 0.9445F;
   private static final float сЭЮ = 1.012F;
   private static final float сЭЧ = 0.595F;
   private static final float сЭЛ = 0.2F;
   private static final float сЭ诶 = 0.15F;
   private static final float сЭ1 = 1.25F;
   private static final String сЭЬ = "MetaHvH";
   private static final double сЭ衣 = -0.01;
   private static final String сЭш = "MetaHvH";
   private static final double сЭИ = 0.3F;
   private static final String сЭь = "Smooth";
   private static final String сЭж = "MetaHvH";
   private static final double сЭ弟 = -0.01;
   private static final double сЭб = 0.1;
   private static final double сЭЫ = 1.0E-4;
   private static final String сЭl = "AttributeModifiers";
   private static final String сЭ8 = "AttributeName";
   private static final String сЭ5 = "Amount";
   private static final String сЭв = "Operation";
   private static final String сЭъ = "Авто буст";
   private static final String сЭМ = "Авто буст";
   private static final String сЭН = "Авто буст";
   private static final String сЭн = "Авто буст";

   private boolean method_3ф() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.FEET);
      return var1 != null && !var1.isEmpty();
   }

   private void рА() {
      if (!this.п_at_f61e84cb.isSelected(сЭ4) || this.method_3щ()) {
         this.ЯО = 0.0F;
      } else if (this.В西 <= 0) {
         long var1 = System.currentTimeMillis();
         if (this.Й(var1)) {
            this.ЯО = 0.0F;
         } else {
            long var3 = this.ср ? сЭп : this.х.getValue().longValue();
            if (var1 - this.су >= var3) {
               this.су = var1;
               this.с2 = var1 + this.З.getValue().longValue();
               this.ЯО = this.У.getValue();
               this.ср = false;
            }

            if (this.с2 > 0L && var1 >= this.с2) {
               this.ЯО = 0.0F;
            }
         }
      }
   }

   private boolean method_3У() {
      BlockPos var1 = MINECRAFT_CLIENT.player.blockPosition();
      return MINECRAFT_CLIENT.level.getBlockState(var1).is(Blocks.WATER);
   }

   private void method_3(PlayerMovementEvent var1, double var2) {
      if (!MovementUtils.hasMovementInput()) {
         this.method_3(var1, 0.0, 0.0);
      } else {
         MovementUtils.setHorizontalSpeed(var2);
         Vec3 var4 = MINECRAFT_CLIENT.player.getDeltaMovement();
         this.method_3(var1, var4.x, var4.z);
      }
   }

   private void method_8(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (var1.getPacket() instanceof ClientboundPlayerPositionPacket) {
            this.сф = System.currentTimeMillis();
            this.ЯО = 0.0F;
            this.В西 = 5;
            this.ср = true;
         }
      }
   }

   private void ь(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.method_3ф()) {
            boolean var2 = this.method_3У() && this.弟.isSelected(сЭЬ) && this.method_3у() && !MINECRAFT_CLIENT.player.isSwimming();
            if (var2) {
               this.рА();
               this.Г(сЭ衣);
            }

            this.ф(var2);
         }
      }
   }

   private boolean method_3у() {
      MobEffectInstance var1 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SLOWNESS);
      int var2 = var1 != null ? var1.getAmplifier() + 1 : 0;
      return var2 < 4;
   }

   private boolean Й(long var1) {
      long var3 = this.Е.getValue().longValue();
      return var3 > 0L && var1 - this.сф < var3;
   }

   private List<Jesus.AttributeModifier> method_3(ItemStack var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null && !var1.isEmpty()) {
         ItemAttributeModifiers var3 = var1.get(DataComponents.ATTRIBUTE_MODIFIERS);
         if (var3 != null) {
            for (Entry var5 : var3.modifiers()) {
               String var6 = var5.attribute().unwrapKey().map(var0 -> var0.identifier().toString()).orElse("");
               var2.add(new Jesus.AttributeModifier(var6, var5.modifier().amount(), this.method_3(var5.modifier().operation())));
            }
         }

         CustomData var7 = var1.get(DataComponents.CUSTOM_DATA);
         if (var7 != null) {
            this.method_3(var7.copyTag(), var2);
         }

         return var2;
      } else {
         return var2;
      }
   }

   private float method_3(Holder<MobEffect> var1, float var2) {
      MobEffectInstance var3 = MINECRAFT_CLIENT.player.getEffect(var1);
      return var3 != null ? var2 * (var3.getAmplifier() + 1) : 0.0F;
   }

   private void method_3(CompoundTag var1, List<Jesus.AttributeModifier> var2) {
      Optional var3 = var1.getList(сЭl);
      if (!var3.isEmpty()) {
         for (Tag var5 : (ListTag)var3.get()) {
            if (var5 instanceof CompoundTag var6) {
               String var7 = var6.getString(сЭ8).orElse("");
               double var8 = var6.getDouble(сЭ5).orElse(0.0);
               int var10 = var6.getInt(сЭв).orElse(0);
               var2.add(new Jesus.AttributeModifier(var7, var8, var10));
            }
         }
      }
   }

   private float пм() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getOffhandItem();
      if (var1.isEmpty()) {
         return 0.0F;
      }

      for (Jesus.AttributeModifier var3 : this.method_3(var1)) {
         if (var3.is(счП, сч西, счд)) {
            return switch (var3.operation()) {
               case 0 -> (float)(var3.amount() / счБ);
               case 1, 2 -> (float)var3.amount();
               default -> 0.0F;
            };
         }
      }

      return 0.0F;
   }

   private void method_8(PlayerMovementSyncEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         boolean var2 = this.method_3У() && this.弟.isSelected(сЭж) && this.method_3у() && !MINECRAFT_CLIENT.player.isSwimming();
         if (var2) {
            this.рА();
            this.Г(сЭ弟);
         }

         this.ф(var2);
      }
   }

   private void method_3(PlayerMovementEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.method_3У() && this.弟.isSelected(сЭш) && this.method_3у() && !MINECRAFT_CLIENT.player.isSwimming()) {
            double var2 = this.Й();
            if (var2 == сЭИ && !this.method_3щ()) {
               this.method_3(var1, 0.0, 0.0);
            }

            Aura var4 = RoseClient.getInstance().getServices().getModules().getAura();
            if (MINECRAFT_CLIENT.options.keyUp.isDown() && var4 != null && var4.Ж_95bedd6e() != null && var4.method_8_f1816549().isSelected(сЭь)) {
               double[] var5 = this.method_3(var4.Ж_95bedd6e());
               this.method_3(var1, var5[0] * var2, var5[1] * var2);
            } else {
               this.method_3(var1, var2);
            }
         }
      }
   }

   private float пХ() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
      if (var1 != null && !var1.isEmpty()) {
         for (Jesus.AttributeModifier var3 : this.method_3(var1)) {
            if (var3.is(счС, счз, счц) && var3.amount() < 0.0) {
               return (float)Math.abs(var3.amount());
            }
         }

         return 0.0F;
      } else {
         return 0.0F;
      }
   }

   private int method_3(Operation var1) {
      return switch (var1) {
         case ADD_VALUE -> 0;
         case ADD_MULTIPLIED_BASE -> 1;
         case ADD_MULTIPLIED_TOTAL -> 2;
      };
   }

   private boolean Ж(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.PLAYER_HEAD)) {
         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;

         for (Jesus.AttributeModifier var6 : this.method_3(var1)) {
            if (var6.is(счI, счТ, счО) && var6.amount() == счФ) {
               var2 = true;
            }

            if (var6.is(счЙ, счк, счг) && var6.amount() == сч0) {
               var3 = true;
            }

            if (var6.is(счх, счУ, счЗ) && var6.amount() == счЕ) {
               var4 = true;
            }
         }

         return var2 && var3 && var4;
      } else {
         return false;
      }
   }

   private void ф(boolean var1) {
      if (MINECRAFT_CLIENT.options == null) {
         this.В = null;
      } else {
         if (var1) {
            if (this.В == null) {
               this.В = MINECRAFT_CLIENT.options.keyJump.isDown();
            }

            MINECRAFT_CLIENT.options.keyJump.setDown(true);
         } else if (this.В != null) {
            MINECRAFT_CLIENT.options.keyJump.setDown(this.В);
            this.В = null;
         }
      }
   }

   private double[] method_3(LivingEntity var1) {
      if (var1 == null) {
         return new double[]{0.0, 0.0};
      }

      double var2 = сЭб;
      float var4 = var1.getYRot();
      double var5 = Math.toRadians(var4);
      double var7 = var1.getX() - Math.sin(var5) * var2;
      double var9 = var1.getZ() + Math.cos(var5) * var2;
      double var11 = var7 - MINECRAFT_CLIENT.player.getX();
      double var13 = var9 - MINECRAFT_CLIENT.player.getZ();
      double var15 = Math.hypot(var11, var13);
      return var15 < сЭЫ ? new double[]{0.0, 0.0} : new double[]{var11 / var15, var13 / var15};
   }

   private double Й() {
      long var1 = System.currentTimeMillis();
      if (this.Й(var1)) {
         return this.пА();
      }

      if (this.В西 > 0) {
         this.В西--;
         return сЭД;
      }

      this.рА();
      if (this.method_3щ()) {
         return this.пА();
      }

      if (var1 - this.си >= сЭЪ) {
         this.ЯФ = this.пм();
         this.ЯЙ = this.пХ();
         this.си = var1;
      }

      float var3 = сЭт;
      if (var1 < this.с2) {
         float var4 = this.ЯО;
         MobEffectInstance var5 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED);
         if (var5 != null && var5.getAmplifier() >= 2) {
            var4 *= сЭа;
         }

         var4 *= сЭЮ + this.ЯФ * сЭЧ;
         var4 += var4 * this.method_3(MobEffects.SPEED, сЭЛ);
         var4 -= var4 * this.method_3(MobEffects.SLOWNESS, сЭ诶);
         var4 -= var4 * this.ЯЙ * сЭ1;
         return var3 + var4;
      } else {
         return var3;
      }
   }

   public Jesus() {
      super(сч必, счщ, ModuleCategory.MOVEMENT);
      this.弟 = new SingleChoiceSetting(счГ, new ChoiceOption(счл));
      this.п_at_f61e84cb = new MultiChoiceSetting(счэ, new ChoiceOption(счю, false));
      this.х = new FloatSetting(счЩ, счу, сч2, счф).withStep(счи).visibleWhen(() -> this.п_at_f61e84cb.isSelected(сЭн));
      this.У = new FloatSetting(сч9, счо, счO, сч7).withStep(счР).visibleWhen(() -> this.п_at_f61e84cb.isSelected(сЭН));
      this.З = new FloatSetting(сч3, счр, сч6, сч_).withStep(счШ).visibleWhen(() -> this.п_at_f61e84cb.isSelected(сЭМ));
      this.Е = new FloatSetting(счы, счй, счЖ, счм).withStep(счК).visibleWhen(() -> this.п_at_f61e84cb.isSelected(сЭъ));
      this.с诶 = this::method_8;
      this.с1 = this::ь;
      this.сЬ = this::method_3;
      this.с衣 = this::method_8;
      this.addSettings(this.弟, this.п_at_f61e84cb, this.У, this.З, this.х, this.Е);
   }

   private boolean method_3щ() {
      return MINECRAFT_CLIENT.player.hasEffect(MobEffects.DOLPHINS_GRACE);
   }

   @Override
   public void onDisable() {
      this.ф(false);
      this.ЯО = 0.0F;
      this.си = 0L;
      this.ЯФ = 0.0F;
      this.ЯЙ = 0.0F;
      super.onDisable();
   }

   private void Г(double var1) {
      Vec3 var3 = MINECRAFT_CLIENT.player.getDeltaMovement();
      MINECRAFT_CLIENT.player.setDeltaMovement(var3.x, var1, var3.z);
   }

   private float пА() {
      if (this.method_3щ()) {
         return счя;
      }

      long var1 = System.currentTimeMillis();
      if (var1 - this.си >= сЭс) {
         this.ЯФ = this.пм();
         this.ЯЙ = this.пХ();
         this.си = var1;
      }

      float var3 = сЭВ;
      var3 *= 1.0F + this.ЯФ;
      var3 += var3 * this.method_3(MobEffects.SPEED, сЭЯ);
      var3 -= var3 * this.method_3(MobEffects.SLOWNESS, сЭХ);
      var3 -= var3 * this.ЯЙ * сЭе;
      if (this.ЯО == 0.0F) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
         if (this.Ж(var4)) {
            var3 *= сЭА;
         }
      }

      return var3;
   }

   private void method_3(PlayerMovementEvent var1, double var2, double var4) {
      Vec3 var6 = MINECRAFT_CLIENT.player.getDeltaMovement();
      MINECRAFT_CLIENT.player.setDeltaMovement(var2, var6.y, var4);
      Vec3 var7 = var1.getMovement();
      var1.setMovement(new Vec3(var2, var7.y, var4));
   }

   private record AttributeModifier(String name, double amount, int operation) {
      boolean is(String... var1) {
         String var2 = this.name == null ? "" : this.name.toLowerCase(Locale.ROOT);

         for (String var6 : var1) {
            String var7 = var6.toLowerCase(Locale.ROOT);
            if (var2.equals(var7) || var2.endsWith(":" + var7) || var2.endsWith("." + var7)) {
               return true;
            }
         }

         return false;
      }
   }
}
