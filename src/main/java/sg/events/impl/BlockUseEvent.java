package sg.events.impl;

import lombok.Generated;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class BlockUseEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("е")  LocalPlayer player;
   private final @com.google.gson.annotations.SerializedName("п")  InteractionHand hand;
   private final @com.google.gson.annotations.SerializedName("В")  BlockHitResult hitResult;

   @Generated
   @Override
   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (!(other instanceof BlockUseEvent otherEvent)) {
         return false;
      } else {
         if (!otherEvent.canEqual(this)) {
            return false;
         }

         LocalPlayer thisPlayer = this.getPlayer();
         LocalPlayer otherPlayer = otherEvent.getPlayer();
         if (thisPlayer == null ? otherPlayer == null : thisPlayer.equals(otherPlayer)) {
            InteractionHand thisHand = this.getHand();
            InteractionHand otherHand = otherEvent.getHand();
            if (thisHand == null ? otherHand == null : thisHand.equals(otherHand)) {
               BlockHitResult thisHitResult = this.getHitResult();
               BlockHitResult otherHitResult = otherEvent.getHitResult();
               return thisHitResult == null ? otherHitResult == null : thisHitResult.equals(otherHitResult);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof BlockUseEvent;
   }

   @Generated
   public InteractionHand getHand() {
      return this.hand;
   }

   @Generated
   public LocalPlayer getPlayer() {
      return this.player;
   }

   @Generated
   public BlockHitResult getHitResult() {
      return this.hitResult;
   }

   @Generated
   @Override
   public int hashCode() {
      byte hashMultiplier = 59;
      int result = 1;
      LocalPlayer player = this.getPlayer();
      result = result * 59 + (player == null ? 43 : player.hashCode());
      InteractionHand hand = this.getHand();
      result = result * 59 + (hand == null ? 43 : hand.hashCode());
      BlockHitResult hitResult = this.getHitResult();
      return result * 59 + (hitResult == null ? 43 : hitResult.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "EventRightClickBlock(player=" + this.getPlayer() + ", hand=" + this.getHand() + ", result=" + this.getHitResult() + ")";
   }

   @Generated
   public BlockUseEvent(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult) {
      this.player = player;
      this.hand = hand;
      this.hitResult = hitResult;
   }
}
