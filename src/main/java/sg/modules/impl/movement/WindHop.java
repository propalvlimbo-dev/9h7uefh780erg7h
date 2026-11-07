package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.InventoryInteractions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.rotation.FixedAngleRotation;
import sg.rotation.RotationRegistry;

public class WindHop extends ClientModule {
   private static final int пХ = 5;
   private static final int пе = 4;
   private final BooleanSetting Въ;
   private final BooleanSetting ВМ;
   private final BooleanSetting ВН;
   @SerializedName("Х")
   private final FixedAngleRotation Х_at_8e839d0c;
   private WindHop.HopStage В;
   private int пА;
   private int п4;
   private int пп;
   private int пД;
   private int пЪ;
   private boolean Хи;
   private boolean Х9;
   private final EventListener<PlayerTickEvent> Ял;
   private static final String Я弟衣 = "WindHop";
   private static final String Я弟ш = "Прыгает и использует заряд ветра под игрока";
   private static final String Я弟И = "Авто Свап";
   private static final String Я弟ь = "Взмах рукой";
   private static final String Я弟ж = "Авто прыжок";
   private static final float Я弟弟 = 90.0F;
   private static final float Я弟б = 90.0F;
   private static final float Я弟Ы = 4.0F;

   private void аO() {
      if (this.Хи) {
         this.пД++;
         if (MINECRAFT_CLIENT.player.onGround() && this.пД <= 4) {
            return;
         }

         this.ая();
      }

      if (this.Въ.getValue() && this.пА >= 0 && this.пА <= 8 && this.пА != this.п4) {
         InventoryInteractions.Ъ_de5a6f78(this.пА);
      }

      this.disable();
   }

   private boolean Ыж() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.getConnection() != null;
   }

   private void аЖ() {
      if (!MINECRAFT_CLIENT.player.onGround()) {
         this.disable();
      } else {
         MINECRAFT_CLIENT.options.keyJump.setDown(true);
         this.Хи = true;
         this.пД = 0;
         this.В = WindHop.HopStage.SWITCH_BACK;
      }
   }

   private void ач() {
      this.пп++;
      int var1 = RoseClient.getInstance().getControllers().getServerPlayers().ПO();
      if (var1 != this.п4) {
         if (this.пп > 5) {
            this.disable();
         }
      } else {
         this.а衣();
      }
   }

   @Override
   public void onDisable() {
      this.ая();
      this.method_3Ь();
      this.method_3т();
      super.onDisable();
   }

   private void аЯ() {
      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      if (var1.getActiveStrategy() != this.Х_at_8e839d0c) {
         this.disable();
      } else if (!(Math.abs(Я弟б - MINECRAFT_CLIENT.player.getXRot()) > Я弟Ы)) {
         if (this.Въ.getValue() && this.пА != this.п4) {
            InventoryInteractions.Ъ_de5a6f78(this.п4);
            this.пп = 0;
            this.В = WindHop.HopStage.WAIT_SWITCH;
         } else {
            this.а衣();
         }
      }
   }

   private void Ьл(PlayerTickEvent var1) {
      if (this.Ыж()) {
         switch (this.В) {
            case IDLE:
               this.а_();
               break;
            case ROTATING:
               this.аЯ();
               break;
            case WAIT_SWITCH:
               this.ач();
               break;
            case JUMP:
               this.аЖ();
               break;
            case USE:
               this.а8();
               break;
            case SWITCH_BACK:
               this.аO();
         }
      }
   }

   private void а8() {
      if (this.п4 >= 0 && this.п4 <= 8 && MINECRAFT_CLIENT.player.getInventory().getItem(this.п4).is(Items.WIND_CHARGE)) {
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         this.пЪ = RoseClient.getInstance().getControllers().getClientTicks().getTickCount();
         if (this.ВМ.getValue()) {
            InventoryInteractions.Ъ(InventoryInteractions.SwingMode.MainHand);
         }

         this.method_3Ь();
         this.В = this.ВН.getValue() ? WindHop.HopStage.JUMP : WindHop.HopStage.SWITCH_BACK;
      } else {
         this.disable();
      }
   }

   private void а衣() {
      this.В = WindHop.HopStage.USE;
   }

   public WindHop() {
      super(Я弟衣, Я弟ш, ModuleCategory.MOVEMENT);
      this.Въ = new BooleanSetting(Я弟И, true);
      this.ВМ = new BooleanSetting(Я弟ь, true);
      this.ВН = new BooleanSetting(Я弟ж, true);
      this.Х_at_8e839d0c = new FixedAngleRotation();
      this.В = WindHop.HopStage.IDLE;
      this.пА = -1;
      this.п4 = -1;
      this.пЪ = -1000;
      this.Ял = this::Ьл;
      this.addSettings(this.Въ, this.ВМ, this.ВН);
   }

   public boolean ЫЙ() {
      return this.В != WindHop.HopStage.IDLE
         || MINECRAFT_CLIENT.player != null && RoseClient.getInstance().getControllers().getClientTicks().getTickCount() - this.пЪ <= 2 && this.isEnabled();
   }

   private void а_() {
      if (MINECRAFT_CLIENT.player.onGround()) {
         this.пА = MINECRAFT_CLIENT.player.getInventory().selected;
         this.п4 = InventoryInteractions.Ъ(Items.WIND_CHARGE, 0, 8);
         if (this.п4 != -1) {
            if (!this.Въ.getValue()) {
               if (!MINECRAFT_CLIENT.player.getMainHandItem().is(Items.WIND_CHARGE)) {
                  return;
               }

               this.п4 = this.пА;
            }

            this.Х_at_8e839d0c.setTargetAngles(MINECRAFT_CLIENT.player.getYRot(), Я弟弟);
            if (RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.Х_at_8e839d0c, 20, 10)) {
               this.Х9 = true;
               this.пп = 0;
               this.В = WindHop.HopStage.ROTATING;
            }
         }
      }
   }

   private void method_3т() {
      this.В = WindHop.HopStage.IDLE;
      this.пА = -1;
      this.п4 = -1;
      this.пп = 0;
      this.пД = 0;
      this.Хи = false;
      this.Х9 = false;
   }

   private void method_3Ь() {
      if (this.Х9) {
         RoseClient.getInstance().getServices().getRotations().expireScheduledStrategy(this.Х_at_8e839d0c);
         this.Х9 = false;
      }
   }

   private boolean Ы7() {
      return false;
   }

   private void ая() {
      if (this.Хи) {
         MINECRAFT_CLIENT.options.keyJump.setDown(false);
         this.Хи = false;
      }
   }

   private enum HopStage {
      IDLE,
      ROTATING,
      WAIT_SWITCH,
      JUMP,
      USE,
      SWITCH_BACK;
   }
}
