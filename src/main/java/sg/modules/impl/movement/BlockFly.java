package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import sg.ec.PlayerCameraPositionSmoothing;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.MovementInputEvent;

public class BlockFly extends ClientModule {
   private final FloatSetting ЯХ;
   @SerializedName("1")
   private final Stopwatch field_1;
   private final EventListener<MovementInputEvent> ВЖ;
   private final EventListener<PlayerTickEvent> Вм;
   private static final String сlю = "BlockFly";
   private static final String сlЩ = "Позволяет летать блоками на RW";
   private static final String сlу = "Задержка";
   private static final float сl2 = 500.0F;
   private static final float сlф = 500.0F;
   private static final float сlи = 50.0F;

   public BlockFly() {
      super(сlю, сlЩ, ModuleCategory.MOVEMENT);
      this.ЯХ = new FloatSetting(сlу, сl2, 0.0F, сlф).withStep(сlи);
      this.field_1 = new Stopwatch();
      this.ВЖ = this::ы;
      this.Вм = this::ВЦ;
      this.addSettings(this.ЯХ);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.field_1.ВД();
      PlayerCameraPositionSmoothing.Ъ7();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.noPhysics = false;
      }

      PlayerCameraPositionSmoothing.Ъ7();
   }

   private void ы(MovementInputEvent var1) {
      if (this.isEnabled()) {
         var1.setSneakPressed(false);
      }
   }

   private void ВЦ(PlayerTickEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         boolean var2 = MINECRAFT_CLIENT.options.keyUp.isDown();
         boolean var3 = MINECRAFT_CLIENT.options.keyJump.isDown();
         boolean var4 = MINECRAFT_CLIENT.options.keyShift.isDown();
         if (!MINECRAFT_CLIENT.player.onGround()) {
            double var5 = (var3 ? 1.0 : 0.0) - (var4 ? 1.0 : 0.0);
            if (var2 || var5 != 0.0) {
               if (this.field_1.method_0(this.ЯХ.getValue().longValue())) {
                  double var7 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
                  double var9 = var2 ? -Math.sin(var7) : 0.0;
                  double var11 = var2 ? Math.cos(var7) : 0.0;
                  if (MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
                     PlayerCameraPositionSmoothing.У(MINECRAFT_CLIENT.player);
                  } else {
                     PlayerCameraPositionSmoothing.Ъ7();
                  }

                  MINECRAFT_CLIENT.player.setPos(MINECRAFT_CLIENT.player.getX() + var9, MINECRAFT_CLIENT.player.getY() + var5, MINECRAFT_CLIENT.player.getZ() + var11);
                  this.field_1.ВД();
               }
            }
         }
      }
   }
}
