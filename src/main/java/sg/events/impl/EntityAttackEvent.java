package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class EntityAttackEvent extends CancellableEvent implements ClientEvent {
   @SerializedName("4")
   private final Player attacker;
   private final @com.google.gson.annotations.SerializedName("Х")  Entity target;

   @Generated
   public Entity getTarget() {
      return this.target;
   }

   @Generated
   public EntityAttackEvent(Player attacker, Entity target) {
      this.attacker = attacker;
      this.target = target;
   }

   @Generated
   public Player getAttacker() {
      return this.attacker;
   }
}
