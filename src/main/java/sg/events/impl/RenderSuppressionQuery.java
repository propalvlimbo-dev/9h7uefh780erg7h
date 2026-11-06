package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class RenderSuppressionQuery extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("В")  RenderSuppressionQuery.Effect effect;

   @Generated
   public RenderSuppressionQuery.Effect getEffect() {
      return this.effect;
   }

   @Generated
   public RenderSuppressionQuery(RenderSuppressionQuery.Effect effect) {
      this.effect = effect;
   }

   public enum Effect {
      FIRE,
      BOSSBAR,
      SCOREBOARD,
      TITLE,
      TOTEM,
      HURT_TIME,
      DESTROY_PARTICLES,
      RAIN,
      CAMERA_CLIP,
      SHADOWS,
      SMOKE,
      VIGNETTE,
      HOLOGRAMS,
      GRASS,
      HEALTH,
      BAD_EFFECTS,
      GLOWING,
      UNDERWATER_BLUR,
      DYNAMIC_FOV,
      POWDER_SNOW;
   }
}
