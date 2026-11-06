package sg.modules.impl.movement;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.SingleChoiceSetting;

public class AirJump extends ClientModule {
   private final SingleChoiceSetting O;
   private final EventListener<PlayerTickEvent> В4;
   private final EventListener<PlayerMovementSyncEvent> Вп;
   private static final String ЯуЖ = "AirJump";
   private static final String Яум = "Прыжок в воздухе";
   private static final String ЯуК = "Обход";
   private static final String ЯуI = "Matrix";
   private static final String ЯуТ = "Default";
   private static final String ЯуО = "Default";
   private static final String ЯуФ = "Matrix";
   private static final String ЯуЙ = "Matrix";
   private static final double Яук = 0.5;
   private static final double Яуг = 0.5;
   private static final double Яу0 = -1.0;

   private void Ч(PlayerMovementSyncEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.O.isSelected(ЯуЙ) && this.method_4б()) {
            var1.setOnGround(true);
         }
      }
   }

   private void Щ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.O.isSelected(ЯуО)) {
            MINECRAFT_CLIENT.player.setOnGround(true);
         }

         if (this.O.isSelected(ЯуФ) && this.method_4б()) {
            MINECRAFT_CLIENT.player.fallDistance = 0.0;
            MINECRAFT_CLIENT.player.noJumpDelay = 0;
            MINECRAFT_CLIENT.player.setOnGround(true);
         }
      }
   }

   private boolean method_4б() {
      return MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getBoundingBox().inflate(Яук, 0.0, Яуг).move(0.0, Яу0, 0.0)).iterator().hasNext()
         && MINECRAFT_CLIENT.player.tickCount % 2 == 0;
   }

   public AirJump() {
      super(ЯуЖ, Яум, ModuleCategory.MOVEMENT);
      this.O = new SingleChoiceSetting(ЯуК, new ChoiceOption(ЯуI), new ChoiceOption(ЯуТ));
      this.В4 = this::Щ;
      this.Вп = this::Ч;
      this.addSettings(this.O);
   }
}
