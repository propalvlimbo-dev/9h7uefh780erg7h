package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.PlayerMoveCancelEvent;
import sg.events.impl.ClientTickEvent;
import sg.network.InventoryInteractions;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.modules.impl.init.InventoryActionCoordinator;

public class AirStuck extends ClientModule {
   private BooleanSetting Яа;
   private final BooleanSetting ЯЮ;
   private Vec3 西;
   private final InventoryActionCoordinator Д;
   @SerializedName("А")
   private LocalPlayer А_at_3222a528;
   private int ДЕ;
   private int ДП;
   private boolean Ас;
   private boolean АВ;
   private final AirStuck.TickListener В;
   private final EventListener<PlayerMovementSyncEvent> Ху;
   private final EventListener<PlayerMoveCancelEvent> Х2;
   private final EventListener<PlayerTickEvent> Хф;
   private static final String ХтЗ = "AirStuck";
   private static final String ХтЕ = "Замораживает модель игрока в воздухе";
   private static final String ХтП = "Reallyworld 360 (1.17.1)";
   private static final String Хт西 = "Свап нагрудника";
   private static final String Хтд = "Легитный";

   @Override
   public void onEnable() {
      this.method_9э();
      this.method_9Ы();
      if (MINECRAFT_CLIENT.player != this.А_at_3222a528 || MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
         this.ДП = -1;
      }

      this.А_at_3222a528 = MINECRAFT_CLIENT.player;
      this.西 = MINECRAFT_CLIENT.player == null ? Vec3.ZERO : MINECRAFT_CLIENT.player.getDeltaMovement();
      if (this.ЯЮ.getValue() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
         this.ДЕ = InventoryInteractions.Ц_a83dd0cc();
         this.method_9I();
      }
   }

   @Override
   public void onDisable() {
      this.method_9э();
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player == this.А_at_3222a528) {
         MINECRAFT_CLIENT.player.setDeltaMovement(this.西);
      }

      this.西 = Vec3.ZERO;
      if (this.ДП >= 0 && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player == this.А_at_3222a528) {
         if (!this.АВ) {
            this.АВ = true;
            RoseClient.getInstance().getEventBus().register(this.В);
         }

         this.method_9I();
      } else {
         this.method_9й();
      }
   }

   public void 必(PlayerMovementSyncEvent var1) {
      var1.setCancelled(true);
   }

   public void п(PlayerMoveCancelEvent var1) {
      var1.setCancelled(true);
      if (MINECRAFT_CLIENT.player != null && this.Яа.getValue()) {
         PacketEventBypass.А(
            new PosRot(MINECRAFT_CLIENT.player.position().x(), MINECRAFT_CLIENT.player.position().y(), MINECRAFT_CLIENT.player.position().z(), MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot(), false, false)
         );
      }
   }

   private void method_9I() {
      if (this.ДЕ >= 0 || this.Ас || this.АВ) {
         if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.player != this.А_at_3222a528 || MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.gameMode == null || !MINECRAFT_CLIENT.player.isAlive()) {
            this.method_9й();
         } else if (!this.АВ && !this.ЯЮ.getValue()) {
            this.method_9э();
         } else if (MINECRAFT_CLIENT.gui.screen() == null && MINECRAFT_CLIENT.player.containerMenu == MINECRAFT_CLIENT.player.inventoryMenu) {
            GuiMove var1 = RoseClient.getInstance().getServices().getModules().getGuiMove();
            if (!this.Ас) {
               if (var1.method_3ю()) {
                  return;
               }

               if (this.АВ) {
                  if (MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) || this.п_() < 0) {
                     this.method_9й();
                     return;
                  }
               } else {
                  if (!MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                     this.method_9э();
                     return;
                  }

                  this.ДЕ = InventoryInteractions.Ц_a83dd0cc();
                  if (this.ДЕ < 0) {
                     return;
                  }
               }
            }

            if (!this.Ас && !var1.Щ().isSelected(Хтд)) {
               this.method_9х();
               if (this.АВ) {
                  this.method_9й();
               }
            } else {
               boolean var2 = this.Д.л(this::method_9х, true) == InventoryActionCoordinator.Result.SUCCESS;
               this.Ас = !var2 && var1.method_3ю();
               if (var2 && this.АВ) {
                  this.method_9й();
               }
            }
         } else {
            if (this.Ас) {
               this.method_9э();
            }
         }
      }
   }

   private void method_9э() {
      if (this.Ас) {
         GuiMove var1 = RoseClient.getInstance().getServices().getModules().getGuiMove();
         var1.з(false);
         if (MINECRAFT_CLIENT.gui.screen() == null && !var1.method_3ю()) {
            KeyMapping[] var2 = new KeyMapping[]{
               MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint
            };

            for (KeyMapping var6 : var2) {
               var6.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var6.key.getValue()));
            }
         }
      }

      this.Д.яш();
      this.Ас = false;
      this.ДЕ = -1;
   }

   private int п_() {
      return this.ДП >= 0 && MINECRAFT_CLIENT.player.inventoryMenu.getSlot(this.ДП).getItem().is(Items.ELYTRA) ? this.ДП : InventoryActions.ш(Items.ELYTRA);
   }

   public AirStuck() {
      super(ХтЗ, ХтЕ, ModuleCategory.MOVEMENT);
      this.Яа = new BooleanSetting(ХтП, true);
      this.ЯЮ = new BooleanSetting(Хт西, false);
      this.西 = Vec3.ZERO;
      this.Д = new InventoryActionCoordinator();
      this.ДЕ = -1;
      this.ДП = -1;
      this.В = new AirStuck.TickListener();
      this.Ху = this::必;
      this.Х2 = this::п;
      this.Хф = var1 -> this.method_9I();
      this.addSettings(this.Яа, this.ЯЮ);
   }

   private void method_9х() {
      this.ДЕ = -1;
      boolean var1 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
      if (this.АВ != var1) {
         int var2 = this.АВ ? this.п_() : InventoryInteractions.Ц_a83dd0cc();
         if (var2 >= 0) {
            if (InventoryActions.б(var2)) {
               InventoryActions.method_7(InventoryActions.ч(var2), 6);
            } else {
               InventoryActions.method_7(8, var2);
               InventoryActions.method_7(8, 6);
               InventoryActions.method_7(8, var2);
            }

            if (!this.АВ) {
               this.ДП = var2;
            }
         }
      }
   }

   private void method_9й() {
      this.method_9э();
      this.method_9Ы();
      this.ДП = -1;
      if (!this.isEnabled()) {
         this.А_at_3222a528 = null;
      }
   }

   private void method_9Ы() {
      if (this.АВ) {
         RoseClient.getInstance().getEventBus().unregister(this.В);
         this.АВ = false;
      }
   }

   private final class TickListener {
      private final EventListener<ClientTickEvent> с5 = var1x -> {
         if (AirStuck.this.АВ) {
            AirStuck.this.method_9I();
         }
      };
   }
}
