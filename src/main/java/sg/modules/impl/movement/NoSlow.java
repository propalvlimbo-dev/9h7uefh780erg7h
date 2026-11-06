package sg.modules.impl.movement;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.ItemUseSlowdownEvent;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class NoSlow extends ClientModule {
   public static final SingleChoiceSetting сХ = new SingleChoiceSetting(
      NoSlow.ВOЙ,
      new ChoiceOption(NoSlow.ВOк),
      new ChoiceOption(NoSlow.ВOг),
      new ChoiceOption(NoSlow.ВO0),
      new ChoiceOption(NoSlow.ВOх),
      new ChoiceOption(NoSlow.ВOУ)
   );
   private final Stopwatch Н = new Stopwatch();
   private final Stopwatch н = new Stopwatch();
   private int пИ = 0;
   private static int пь = 0;
   private boolean Х_ = false;
   private final EventListener<PlayerTickEvent> Яы = this::ЧЭ;
   private final EventListener<ItemUseSlowdownEvent> Яй = this::б;
   private static final String ВO_ = "NoSlow";
   private static final String ВOШ = "Убирает замедление";
   private static final String ВOы = "Grim New";
   private static final String ВOй = "Reallyworld";
   private static final String ВOЖ = "Grim Ticks";
   private static final long ВOм = 125L;
   private static final String ВOК = "Vanilla";
   private static final String ВOI = "Reallyworld";
   private static final String ВOТ = "Grim Ticks";
   private static final String ВOО = "Funtime арбалет";
   private static final String ВOФ = "Grim New";
   private static final String ВOЙ = "Режим";
   private static final String ВOк = "Vanilla";
   private static final String ВOг = "Grim Ticks";
   private static final String ВO0 = "Grim New";
   private static final String ВOх = "Reallyworld";
   private static final String ВOУ = "Funtime арбалет";

   private void нй() {
      if (MINECRAFT_CLIENT.getConnection() != null) {
         if (MINECRAFT_CLIENT.player.getUsedItemHand() == InteractionHand.MAIN_HAND && MINECRAFT_CLIENT.player.getTicksUsingItem() == 1) {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerActionPacket(Action.DROP_ALL_ITEMS, BlockPos.ZERO, MINECRAFT_CLIENT.player.getDirection()));
         }
      }
   }

   public void б(ItemUseSlowdownEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!MINECRAFT_CLIENT.player.isFallFlying()) {
            if (MINECRAFT_CLIENT.player.isUsingItem()) {
               switch (сХ.getSelectedOption().getName()) {
                  case ВOК:
                     var1.setCancelled(true);
                     break;
                  case ВOI:
                  case ВOТ:
                     if (this.Х_) {
                        var1.setCancelled(true);
                     }
                     break;
                  case ВOО:
                     this.method_4(var1);
                     break;
                  case ВOФ:
                     if (MINECRAFT_CLIENT.player.getUsedItemHand() == InteractionHand.MAIN_HAND && MINECRAFT_CLIENT.player.getTicksUsingItem() > 1 && this.пИ % 2 == 0) {
                        var1.setCancelled(true);
                     }
               }
            }
         }
      }
   }

   private void method_4(ItemUseSlowdownEvent var1) {
      if ((MINECRAFT_CLIENT.player.getOffhandItem().getItem() == Items.CROSSBOW || MINECRAFT_CLIENT.player.getMainHandItem().getItem() == Items.CROSSBOW)
         && MINECRAFT_CLIENT.player.getUseItem().getItem() == Items.CROSSBOW) {
         var1.setCancelled(true);
      }
   }

   public void ЧЭ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.player.isFallFlying()) {
         this.Х_ = false;
         if (сХ.isSelected(ВOы)) {
            this.нй();
         }

         if (сХ.isSelected(ВOй)) {
            if (MINECRAFT_CLIENT.player.isUsingItem()) {
               this.пИ++;
               int[] var2 = new int[]{2, 3, 3};
               int var3 = var2[пь % 3];
               if (this.пИ >= var3) {
                  this.Х_ = true;
                  this.пИ = 0;
                  пь++;
               }
            } else {
               this.пИ = 0;
               пь = 0;
            }
         } else {
            this.пИ = 0;
         }

         if (сХ.isSelected(ВOЖ) && MINECRAFT_CLIENT.player.isUsingItem() && this.н.method_0(ВOм)) {
            this.Х_ = true;
            MINECRAFT_CLIENT.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0, MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot()));
            this.н.ВД();
         }

         if (MINECRAFT_CLIENT.player.hurtTime == 10) {
            this.Н.ВД();
         }
      }
   }

   public NoSlow() {
      super(ВO_, ВOШ, ModuleCategory.MOVEMENT);
      this.addSettings(сХ);
   }
}
