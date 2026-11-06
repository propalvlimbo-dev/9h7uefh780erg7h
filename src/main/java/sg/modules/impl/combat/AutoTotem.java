package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.modules.impl.init.InventoryActionQueue;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.init.TickTimer;
import sg.modules.impl.init.TotemConditionRegistry;

public class AutoTotem extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Д")  MultiChoiceSetting checksSetting;
   private final @com.google.gson.annotations.SerializedName("П")  FloatSetting healthSetting;
   private final @com.google.gson.annotations.SerializedName("西")  FloatSetting reverseSwapHealthSetting;
   private final @com.google.gson.annotations.SerializedName("д")  FloatSetting elytraHealthSetting;
   private final @com.google.gson.annotations.SerializedName("Б")  FloatSetting crystalDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("С")  FloatSetting anchorDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("з")  FloatSetting fallDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("ц")  FloatSetting tntDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("я")  FloatSetting swapDelayTicksSetting;
   private final @com.google.gson.annotations.SerializedName("м")  BooleanSetting avoidSwappingOrbSetting;
   private final @com.google.gson.annotations.SerializedName("К")  BooleanSetting preserveEnchantedTotemSetting;
   private final @com.google.gson.annotations.SerializedName("I")  BooleanSetting avoidDragoniteSetting;
   private final @com.google.gson.annotations.SerializedName("Т")  BooleanSetting customOrbHealthSetting;
   private final @com.google.gson.annotations.SerializedName("сс")  FloatSetting orbHealthSetting;
   private final @com.google.gson.annotations.SerializedName("сВ")  FloatSetting crystalSwapDelaySetting;
   private final @com.google.gson.annotations.SerializedName("сЯ")  FloatSetting anchorSwapDelaySetting;
   @SerializedName("Х")
   private final InventoryActionQueue actionQueue;
   private final @com.google.gson.annotations.SerializedName("В")  TotemConditionRegistry conditionRegistry;
   @SerializedName("Х")
   private final TickTimer swapDelayTimer;
   private @com.google.gson.annotations.SerializedName("с8")  String pendingActionKey;
   private @com.google.gson.annotations.SerializedName("Я弟")  int pendingSwapSlot;
   private @com.google.gson.annotations.SerializedName("Яб")  int returnItemSlot;
   private @com.google.gson.annotations.SerializedName("сЙ")  boolean totemPopped;
   private @com.google.gson.annotations.SerializedName("ск")  boolean totemSwapQueued;
   private @com.google.gson.annotations.SerializedName("сг")  boolean returnSwapQueued;
   private final @com.google.gson.annotations.SerializedName("сш")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("сИ")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private static final String сВц = "AutoTotem";
   private static final String сВя = "Автоматически берет тотем при различных факторах";
   private static final String сЯс = "Проверки";
   private static final String сЯВ = "Золотые сердца";
   private static final String сЯЯ = "Кристаллы";
   private static final String сЯХ = "Якорь";
   private static final String сЯе = "Падение";
   private static final String сЯА = "Динамит";
   private static final String сЯ4 = "Броня";
   private static final String сЯп = "Здоровье";
   private static final float сЯД = 5.0F;
   private static final float сЯЪ = 20.0F;
   private static final String сЯт = "Здоровье обратного свапа";
   private static final float сЯа = 20.0F;
   private static final String сЯЮ = "Здоровье на элитре";
   private static final float сЯЧ = 5.0F;
   private static final float сЯЛ = 20.0F;
   private static final String сЯ诶 = "Дистанция до кристалла";
   private static final float сЯ1 = 5.0F;
   private static final float сЯЬ = 10.0F;
   private static final float сЯ衣 = 0.1F;
   private static final String сЯш = "Дистанция до якоря";
   private static final float сЯИ = 5.0F;
   private static final float сЯь = 10.0F;
   private static final float сЯж = 0.1F;
   private static final String сЯ弟 = "Дистанция падения";
   private static final float сЯб = 20.0F;
   private static final float сЯЫ = 50.0F;
   private static final String сЯl = "Дистанция до динамита";
   private static final float сЯ8 = 5.0F;
   private static final float сЯ5 = 10.0F;
   private static final float сЯв = 0.1F;
   private static final String сЯъ = "Тики задержки свапа";
   private static final float сЯМ = 4.0F;
   private static final float сЯН = 20.0F;
   private static final String сЯн = "Не свапать шар";
   private static final String сЯЦ = "Сохранять зачарованный тотем";
   private static final String сЯч = "Не свапать драконитки";
   private static final String сЯЭ = "Кастомное здоровье с шаром";
   private static final String сЯ必 = "Здоровье с шаром";
   private static final float сЯщ = 5.0F;
   private static final float сЯГ = 20.0F;
   private static final String сЯл = "Задержка свапа на кристал";
   private static final float сЯэ = 4.0F;
   private static final float сЯю = 60.0F;
   private static final String сЯЩ = "Задержка свапа на якорь";
   private static final float сЯу = 4.0F;
   private static final float сЯ2 = 60.0F;
   private static final String сЯф = "Легитный";
   private static final String сЯи = "totem";
   private static final String сЯ9 = "back";
   private static final String сЯо = "Золотые сердца";
   private static final String сЯO = "Якорь";
   private static final String сЯ7 = "Кристаллы";
   private static final String сЯР = "Динамит";
   private static final String сЯ3 = "Падение";
   private static final String сЯр = "Якорь";
   private static final String сЯ6 = "Кристаллы";

   @Generated
   public FloatSetting getCrystalDistanceSetting() {
      return this.crystalDistanceSetting;
   }

   private void ЫЧ() {
      int var1 = this.pendingSwapSlot;
      Runnable var2 = () -> {
         this.actionQueue.Э_de5a6f78(this.actionQueue.ф(), var1);
         this.actionQueue.зl();
         InventoryActions.衣(this.actionQueue.ф());
         this.actionQueue.зИ();
         InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
         this.actionQueue.Э_de5a6f78(this.actionQueue.ф(), var1);
         this.actionQueue.зl();
      };
      this.actionQueue.з(this.pendingActionKey, var2);
      this.pendingSwapSlot = -1;
   }

   @Generated
   public boolean Ъу() {
      return this.totemPopped;
   }

   private void Ы衣() {
      this.actionQueue.Э_de5a6f78(40, this.pendingSwapSlot);
      this.actionQueue.зl();
      this.totemPopped = false;
      this.pendingSwapSlot = -1;
   }

   @Generated
   public FloatSetting м_989fed9a() {
      return this.healthSetting;
   }

   @Generated
   public EventListener<PacketReceiveEvent> Е() {
      return this.packetReceiveListener;
   }

   @Generated
   public BooleanSetting я() {
      return this.preserveEnchantedTotemSetting;
   }

   private boolean ЪЮ() {
      return this.conditionRegistry.ЬК();
   }

   @Generated
   public InventoryActionQueue Ъ_a818e6b1() {
      return this.actionQueue;
   }

   @Generated
   public boolean Ъд() {
      return this.totemSwapQueued;
   }

   @Generated
   public FloatSetting У_989fed9a() {
      return this.crystalSwapDelaySetting;
   }

   @Generated
   public int йэ() {
      return this.pendingSwapSlot;
   }

   @Generated
   public FloatSetting х_989fed9a() {
      return this.elytraHealthSetting;
   }

   @Generated
   public MultiChoiceSetting Ы() {
      return this.checksSetting;
   }

   @Generated
   public String йб() {
      return this.pendingActionKey;
   }

   private int йl() {
      for (int var1 = 0; var1 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var1++) {
         ItemStack var2 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var1);
         if (var2.getItem().equals(Items.TOTEM_OF_UNDYING)) {
            return var1 < 9 ? var1 + 36 : var1;
         }
      }

      return -1;
   }

   @Generated
   public boolean ЪА() {
      return this.returnSwapQueued;
   }

   @Generated
   public EventListener<PlayerTickEvent> к() {
      return this.playerTickListener;
   }

   @Generated
   public FloatSetting ч_989fed9a() {
      return this.tntDistanceSetting;
   }

   @Generated
   public BooleanSetting ф() {
      return this.avoidDragoniteSetting;
   }

   private boolean ЪЛ() {
      if (this.ЪЮ()) {
         if (MINECRAFT_CLIENT.player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING) || this.totemSwapQueued) {
            return false;
         }

         this.pendingSwapSlot = this.йl();
         if (this.pendingSwapSlot == -1) {
            return false;
         }

         if (this.returnItemSlot == -1) {
            this.returnItemSlot = this.pendingSwapSlot;
         }

         this.pendingActionKey = сЯи;
         this.totemSwapQueued = true;
         this.returnSwapQueued = false;
         this.swapDelayTimer.reset();
      } else if (this.Ър()) {
         if (this.returnSwapQueued || this.returnItemSlot == -1) {
            return false;
         }

         this.pendingSwapSlot = this.returnItemSlot;
         this.returnItemSlot = -1;
         this.pendingActionKey = сЯ9;
         this.totemSwapQueued = false;
         this.returnSwapQueued = true;
         this.swapDelayTimer.reset();
      }

      return this.pendingSwapSlot >= 0 && this.pendingActionKey != null;
   }

   @Generated
   public FloatSetting е_989fed9a() {
      return this.fallDistanceSetting;
   }

   @Generated
   public FloatSetting 弟() {
      return this.anchorSwapDelaySetting;
   }

   @Generated
   public BooleanSetting ш_3276742a() {
      return this.avoidSwappingOrbSetting;
   }

   public void Й(PlayerTickEvent var1) {
      this.ЫЗ();
      if (this.ЪЛ()) {
         if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(сЯф)) {
            this.Ыз();
         } else {
            this.Ы衣();
         }
      }
   }

   private void Ыз() {
      int var1 = this.pendingSwapSlot;
      this.actionQueue.з(this.pendingActionKey, () -> this.actionQueue.Э_de5a6f78(40, var1), this.actionQueue::зl);
      this.totemPopped = false;
      this.pendingSwapSlot = -1;
   }

   @Generated
   public BooleanSetting м_3276742a() {
      return this.customOrbHealthSetting;
   }

   @Generated
   public FloatSetting getAnchorDistanceSetting() {
      return this.anchorDistanceSetting;
   }

   private boolean Ър() {
      float var1 = MINECRAFT_CLIENT.player.getHealth();
      if (this.checksSetting.isSelected(сЯо) && MINECRAFT_CLIENT.player.hasEffect(MobEffects.ABSORPTION)) {
         var1 += MINECRAFT_CLIENT.player.getAbsorptionAmount();
      }

      return this.totemPopped ? MINECRAFT_CLIENT.player.getOffhandItem().isEmpty() : var1 > this.healthSetting.getValue() + this.reverseSwapHealthSetting.getValue();
   }

   @Generated
   public FloatSetting getReverseSwapHealthSetting() {
      return this.reverseSwapHealthSetting;
   }

   @Override
   public void onDisable() {
      this.actionQueue.з_b1d77d5c().clear();
   }

   @Generated
   public int йЦ() {
      return this.returnItemSlot;
   }

   public void Н(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundEntityEventPacket var2 && var2.getEventId() == 35 && var2.getEntity(MINECRAFT_CLIENT.level) instanceof LocalPlayer) {
         this.totemPopped = true;
         this.totemSwapQueued = false;
         this.returnSwapQueued = false;
      }
   }

   private void ЫЗ() {
      if (this.swapDelayTimer.hasElapsed(this.swapDelayTicksSetting.getValue().intValue())) {
         if (MINECRAFT_CLIENT.player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.totemSwapQueued = false;
         }

         if (!MINECRAFT_CLIENT.player.getOffhandItem().getItem().equals(Items.AIR) && !MINECRAFT_CLIENT.player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.returnSwapQueued = false;
         }
      }
   }

   @Generated
   public FloatSetting 西_989fed9a() {
      return this.orbHealthSetting;
   }

   @Generated
   public FloatSetting К() {
      return this.swapDelayTicksSetting;
   }

   @Generated
   public TotemConditionRegistry Ъ_b4b2969e() {
      return this.conditionRegistry;
   }

   public AutoTotem() {
      super(сВц, сВя, ModuleCategory.COMBAT);
      this.checksSetting = new MultiChoiceSetting(
         сЯс,
         new ChoiceOption(сЯВ),
         new ChoiceOption(сЯЯ),
         new ChoiceOption(сЯХ),
         new ChoiceOption(сЯе),
         new ChoiceOption(сЯА),
         new ChoiceOption(сЯ4)
      );
      this.healthSetting = new FloatSetting(сЯп, сЯД, 1.0F, сЯЪ).withStep(1.0F);
      this.reverseSwapHealthSetting = new FloatSetting(сЯт, 2.0F, 1.0F, сЯа).withStep(1.0F);
      this.elytraHealthSetting = new FloatSetting(сЯЮ, сЯЧ, 1.0F, сЯЛ).withStep(1.0F);
      this.crystalDistanceSetting = new FloatSetting(сЯ诶, сЯ1, 1.0F, сЯЬ).withStep(сЯ衣).visibleWhen(() -> this.checksSetting.isSelected(сЯ6));
      this.anchorDistanceSetting = new FloatSetting(сЯш, сЯИ, 1.0F, сЯь).withStep(сЯж).visibleWhen(() -> this.checksSetting.isSelected(сЯр));
      this.fallDistanceSetting = new FloatSetting(сЯ弟, сЯб, 1.0F, сЯЫ).withStep(1.0F).visibleWhen(() -> this.checksSetting.isSelected(сЯ3));
      this.tntDistanceSetting = new FloatSetting(сЯl, сЯ8, 1.0F, сЯ5).withStep(сЯв).visibleWhen(() -> this.checksSetting.isSelected(сЯР));
      this.swapDelayTicksSetting = new FloatSetting(сЯъ, сЯМ, 1.0F, сЯН).withStep(1.0F);
      this.avoidSwappingOrbSetting = new BooleanSetting(сЯн, true);
      this.preserveEnchantedTotemSetting = new BooleanSetting(сЯЦ, false);
      this.avoidDragoniteSetting = new BooleanSetting(сЯч, true);
      this.customOrbHealthSetting = new BooleanSetting(сЯЭ, true).visibleWhen(this.avoidSwappingOrbSetting::getValue);
      this.orbHealthSetting = new FloatSetting(сЯ必, сЯщ, 1.0F, сЯГ).withStep(1.0F).visibleWhen(() -> this.avoidSwappingOrbSetting.getValue() && this.customOrbHealthSetting.getValue());
      this.crystalSwapDelaySetting = new FloatSetting(сЯл, сЯэ, 0.0F, сЯю).withStep(1.0F).visibleWhen(() -> this.checksSetting.isSelected(сЯ7));
      this.anchorSwapDelaySetting = new FloatSetting(сЯЩ, сЯу, 0.0F, сЯ2).withStep(1.0F).visibleWhen(() -> this.checksSetting.isSelected(сЯO));
      this.actionQueue = new InventoryActionQueue();
      this.conditionRegistry = new TotemConditionRegistry(this);
      this.swapDelayTimer = new TickTimer();
      this.pendingActionKey = null;
      this.pendingSwapSlot = -1;
      this.returnItemSlot = -1;
      this.playerTickListener = this::Й;
      this.packetReceiveListener = this::Н;
      this.addSettings(this.checksSetting, this.healthSetting, this.reverseSwapHealthSetting, this.elytraHealthSetting, this.crystalDistanceSetting, this.anchorDistanceSetting, this.fallDistanceSetting, this.swapDelayTicksSetting, this.preserveEnchantedTotemSetting, this.avoidDragoniteSetting, this.avoidSwappingOrbSetting, this.customOrbHealthSetting, this.orbHealthSetting, this.crystalSwapDelaySetting, this.anchorSwapDelaySetting);
   }

   @Generated
   public TickTimer method_6_1004dbd2() {
      return this.swapDelayTimer;
   }
}
