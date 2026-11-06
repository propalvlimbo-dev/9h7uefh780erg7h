package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.InventoryInteractions;
import sg.render.pipeline.InventoryActions;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionQueue;
import sg.settings.KeybindSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public class ElytraSwap extends ClientModule {
   @SerializedName("В")
   private final SingleChoiceSetting В_f1816549;
   @SerializedName("В")
   private final MultiChoiceSetting В_3bdb855e;
   @SerializedName("В")
   private final KeybindSetting В_bf56bd03;
   @SerializedName("Я")
   private final KeybindSetting Я_at_41acf27a;
   @SerializedName("В")
   private final InventoryActionQueue В_a818e6b1;
   private boolean ж;
   private boolean 弟;
   private boolean б;
   private int 诶;
   @SerializedName("1")
   private int field_1;
   private String ь;
   @SerializedName("е")
   private final EventListener<PlayerTickEvent> е_at_41acf27a;
   @SerializedName("А")
   private final EventListener<InputEvent> А_at_41acf27a;
   private static final String Вч衣 = "ElytraSwap";
   private static final String Вчш = "Помощник элитр";
   private static final String ВчИ = "Мод запуска фейверка";
   private static final String Вчь = "Рейджовый";
   private static final String Вчж = "Легитный";
   private static final String Вч弟 = "Прочие настройки";
   private static final String Вчб = "Авто взлет";
   private static final String ВчЫ = "Клавиша элитр";
   private static final String Вчl = "Клавиша фейерверка";
   private static final String Вч8 = "Легитный";
   private static final String Вч5 = "Легитный";
   private static final String Вчв = "chestplate";
   private static final String Вчъ = "elytra";
   private static final String ВчМ = "firework";
   private static final String ВчН = "Авто взлет";

   private boolean б() {
      return this.В_3bdb855e.isSelected(ВчН) && this.б && MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA) && !MINECRAFT_CLIENT.player.isFallFlying();
   }

   @Override
   public void onDisable() {
      this.В_a818e6b1.з_b1d77d5c().clear();
   }

   private boolean method_4() {
      if (!this.弟) {
         return false;
      }

      this.field_1 = InventoryActions.ш(Items.FIREWORK_ROCKET);
      this.ь = ВчМ;
      this.弟 = false;
      return this.field_1 >= 0;
   }

   private void method_1() {
      if (InventoryActions.б(this.field_1)) {
         InventoryActions.衣(InventoryActions.ч(this.field_1));
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
      } else {
         this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), this.field_1);
         InventoryActions.衣(this.В_a818e6b1.ф());
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
         this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), this.field_1);
      }
   }

   private void Т_de5a6f78() {
      if (InventoryActions.б(this.诶)) {
         this.В_a818e6b1.Э_de5a6f78(InventoryActions.ч(this.诶), 6);
      } else {
         this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), this.诶);
         this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), 6);
         this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), this.诶);
      }

      this.б = true;
      this.诶 = -1;
   }

   private void method_3() {
      if (MINECRAFT_CLIENT.player.onGround()) {
         MINECRAFT_CLIENT.player.jumpFromGround();
      } else {
         MINECRAFT_CLIENT.player.startFallFlying();
         PacketEventBypass.А(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
      }
   }

   private void Я() {
      int var1 = this.诶;
      if (InventoryActions.б(var1)) {
         this.В_a818e6b1.з(this.ь, () -> this.В_a818e6b1.Э_de5a6f78(InventoryActions.ч(var1), 6), () -> {
            this.В_a818e6b1.зl();
            this.б = true;
         });
      } else {
         this.В_a818e6b1.з(this.ь, () -> {
            this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), var1);
            this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), 6);
            this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), var1);
         }, () -> {
            this.В_a818e6b1.зl();
            this.б = true;
         });
      }

      this.诶 = -1;
   }

   public void Г(PlayerTickEvent var1) {
      if (this.Ш_bbeebd87()) {
         if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(Вч8)) {
            this.Я();
         } else {
            this.Т_de5a6f78();
         }
      } else if (this.method_4()) {
         if (this.В_f1816549.isSelected(Вч5)) {
            this.К();
         } else {
            this.method_1();
         }
      } else if (this.б()) {
         this.method_3();
      }
   }

   private void К() {
      int var1 = this.field_1;
      if (InventoryActions.б(var1)) {
         Runnable var2 = () -> {
            InventoryActions.衣(InventoryActions.ч(var1));
            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         };
         Runnable var3 = () -> InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
         this.В_a818e6b1.з(this.ь, 1, new Runnable[]{var2, var3});
      } else {
         Runnable var4 = () -> {
            this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), var1);
            this.В_a818e6b1.зl();
            InventoryActions.衣(this.В_a818e6b1.ф());
            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         };
         Runnable var5 = () -> {
            InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
            this.В_a818e6b1.Э_de5a6f78(this.В_a818e6b1.ф(), var1);
            this.В_a818e6b1.зl();
         };
         this.В_a818e6b1.Б(this.ь, 1, var4, var5);
      }
   }

   private boolean Ш_bbeebd87() {
      if (!this.ж) {
         return false;
      }

      if (MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)) {
         this.诶 = InventoryInteractions.Ц_a83dd0cc();
         this.ь = Вчв;
      } else {
         this.诶 = InventoryActions.ш(Items.ELYTRA);
         this.ь = Вчъ;
      }

      this.ж = false;
      return this.诶 >= 0;
   }

   public ElytraSwap() {
      super(Вч衣, Вчш, ModuleCategory.MISC);
      this.В_f1816549 = new SingleChoiceSetting(ВчИ, new ChoiceOption(Вчь), new ChoiceOption(Вчж));
      this.В_3bdb855e = new MultiChoiceSetting(Вч弟, new ChoiceOption(Вчб));
      this.В_bf56bd03 = new KeybindSetting(ВчЫ, -1);
      this.Я_at_41acf27a = new KeybindSetting(Вчl, -1);
      this.В_a818e6b1 = new InventoryActionQueue();
      this.诶 = -1;
      this.field_1 = -1;
      this.ь = null;
      this.е_at_41acf27a = this::Г;
      this.А_at_41acf27a = this::Л;
      this.addSettings(this.В_f1816549, this.В_3bdb855e, this.В_bf56bd03, this.Я_at_41acf27a);
   }

   public void Л(InputEvent var1) {
      int var2 = InventoryActions.ш(Items.FIREWORK_ROCKET);
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1) {
         if (var1.getInputCode() == this.В_bf56bd03.getValue()) {
            this.б = false;
            this.ж = true;
         } else if (var1.getInputCode() == this.Я_at_41acf27a.getValue()
            && MINECRAFT_CLIENT.player.isFallFlying()
            && MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)
            && var2 >= 0) {
            this.弟 = true;
         }
      }
   }
}
