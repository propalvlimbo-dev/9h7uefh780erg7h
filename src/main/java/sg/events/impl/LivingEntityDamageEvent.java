package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import sg.events.ClientEvent;

public class LivingEntityDamageEvent implements ClientEvent {
   @SerializedName("1")
   private final LivingEntity entity;
   private final @com.google.gson.annotations.SerializedName("пУ")  float hurtDirection;

   @Generated
   public LivingEntityDamageEvent(LivingEntity entity, float hurtDirection) {
      this.entity = entity;
      this.hurtDirection = hurtDirection;
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }

   @Generated
   public float getHurtDirection() {
      return this.hurtDirection;
   }
}
