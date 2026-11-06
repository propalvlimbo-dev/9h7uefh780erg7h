package sg.render;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.player.Player;

public final class PlayerWingState {
   @SerializedName("В")
   private final AnimatedWingVariant variant;
   @SerializedName("В")
   private final WingAnimator animator;
   @SerializedName("В")
   private WingMotionState motionState;
   private @com.google.gson.annotations.SerializedName("е衣")  int lastPlayerTick;
   private static final int UNINITIALIZED_PLAYER_TICK = Integer.MIN_VALUE;

   public WingAnimator getAnimator() {
      return this.animator;
   }

   public AnimatedWingVariant getVariant() {
      return this.variant;
   }

   public PlayerWingState(AnimatedWingVariant variant) {
      this.lastPlayerTick = UNINITIALIZED_PLAYER_TICK;
      this.variant = variant;
      this.animator = variant.createAnimator();
      this.motionState = new WingMotionState.GroundedState();
   }

   public void updateForPlayerTick(Player player) {
      if (player.tickCount != this.lastPlayerTick) {
         this.lastPlayerTick = player.tickCount;
         this.animator.tick();
         double deltaX = player.getX() - player.xo;
         double deltaY = player.getY() - player.yo;
         double deltaZ = player.getZ() - player.zo;
         WingMotionState nextState = this.motionState.update(!player.onGround(), deltaX, deltaY, deltaZ, player);
         if (nextState != this.motionState) {
            nextState.activateAnimation(this.animator);
            this.motionState = nextState;
         }
      }
   }
}
