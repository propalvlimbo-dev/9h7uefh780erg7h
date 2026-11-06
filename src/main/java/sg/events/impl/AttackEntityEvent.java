package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sg.events.ClientEvent;

public class AttackEntityEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("е")  Player attacker;
   private final @com.google.gson.annotations.SerializedName("Я")  Entity target;
   private final @com.google.gson.annotations.SerializedName("В")  AttackEntityEvent.Phase phase;

   @Generated
   public AttackEntityEvent(Player attacker, Entity target, AttackEntityEvent.Phase phase) {
      this.attacker = attacker;
      this.target = target;
      this.phase = phase;
   }

   @Generated
   public Entity getTarget() {
      return this.target;
   }

   @Generated
   public Player getAttacker() {
      return this.attacker;
   }

   @Generated
   public AttackEntityEvent.Phase getPhase() {
      return this.phase;
   }

   public enum Phase {
      PRE,
      POST;
   }
}
