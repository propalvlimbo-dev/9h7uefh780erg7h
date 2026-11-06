package sg.modules.impl.movement;

import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import sg.events.impl.WebSlowdownEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.mixinbridge.EntityWebStateAccess;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class NoWeb extends ClientModule {
   private final SingleChoiceSetting Ц;
   private final FloatSetting сй;
   private final FloatSetting сЖ;
   private final EventListener<PlayerTickEvent> сР;
   private final EventListener<WebSlowdownEvent> с3;
   private static final String Я必5 = "NoWeb";
   private static final String Я必в = "Свободное перемещение в паутине";
   private static final String Я必ъ = "Мод";
   private static final String Я必М = "ReallyWorld";
   private static final String Я必Н = "Break";
   private static final String Я必н = "Custom";
   private static final String Я必Ц = "Скорость";
   private static final float Я必ч = 0.21F;
   private static final float Я必Э = 0.1F;
   private static final float Я必必 = 0.01F;
   private static final String Я必щ = "Скорсть по Y";
   private static final float Я必Г = 0.9F;
   private static final float Я必л = 0.1F;
   private static final float Я必э = 0.01F;
   private static final String Я必ю = "ReallyWorld";
   private static final double Я必Щ = 0.9F;
   private static final double Я必у = -0.9F;
   private static final double Я必2 = 0.21F;
   private static final String Я必ф = "Custom";
   private static final String Я必и = "Break";
   private static final String Я必9 = "Custom";
   private static final String Я必о = "Custom";

   public void Щ(WebSlowdownEvent var1) {
      if (this.Ц.isSelected(Я必и)) {
         MINECRAFT_CLIENT.gameMode.startPrediction(MINECRAFT_CLIENT.level, var1x -> new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var1.getPosition(), Direction.UP, var1x));
         var1.setCancelled(true);
      }
   }

   public NoWeb() {
      super(Я必5, Я必в, ModuleCategory.MOVEMENT);
      this.Ц = new SingleChoiceSetting(Я必ъ, new ChoiceOption(Я必М), new ChoiceOption(Я必Н), new ChoiceOption(Я必н));
      this.сй = new FloatSetting(Я必Ц, Я必ч, Я必Э, 1.0F).withStep(Я必必).visibleWhen(() -> this.Ц.isSelected(Я必о));
      this.сЖ = new FloatSetting(Я必щ, Я必Г, Я必л, 2.0F).withStep(Я必э).visibleWhen(() -> this.Ц.isSelected(Я必9));
      this.сР = this::с;
      this.с3 = this::Щ;
      this.addSettings(this.Ц, this.сй, this.сЖ);
   }

   public void с(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.Ц.isSelected(Я必ю) && ((EntityWebStateAccess)MINECRAFT_CLIENT.player).dimas$isInWeb()) {
            MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, 0.0, MINECRAFT_CLIENT.player.getDeltaMovement().z);
            if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
               MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, Я必Щ, MINECRAFT_CLIENT.player.getDeltaMovement().z);
            }

            if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
               MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, Я必у, MINECRAFT_CLIENT.player.getDeltaMovement().z);
            }

            MovementUtils.setHorizontalSpeed(Я必2);
         } else if (this.Ц.isSelected(Я必ф) && ((EntityWebStateAccess)MINECRAFT_CLIENT.player).dimas$isInWeb()) {
            MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, 0.0, MINECRAFT_CLIENT.player.getDeltaMovement().z);
            if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
               MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, this.сЖ.getValue().floatValue(), MINECRAFT_CLIENT.player.getDeltaMovement().z);
            }

            if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
               MINECRAFT_CLIENT.player.setDeltaMovement(MINECRAFT_CLIENT.player.getDeltaMovement().x, -this.сЖ.getValue(), MINECRAFT_CLIENT.player.getDeltaMovement().z);
            }

            MovementUtils.setHorizontalSpeed(this.сй.getValue().floatValue());
         }
      }
   }
}
