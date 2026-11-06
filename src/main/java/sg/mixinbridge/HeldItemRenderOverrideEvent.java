package sg.mixinbridge;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sg.events.ClientEvent;

public final class HeldItemRenderOverrideEvent implements ClientEvent {
   public static final HeldItemRenderOverrideEvent INSTANCE = new HeldItemRenderOverrideEvent();
   @SerializedName("В")
   private HeldItemRenderOverrideEvent.QueryType queryType;
   private @com.google.gson.annotations.SerializedName("ХС")  int selectedSlot;
   private @com.google.gson.annotations.SerializedName("Ъ")  ItemStack stack = ItemStack.EMPTY;
   private @com.google.gson.annotations.SerializedName("а")  LivingEntity entity;
   @SerializedName("В")
   private HumanoidArm arm;

   private HeldItemRenderOverrideEvent() {
   }

   public HeldItemRenderOverrideEvent resetForSelectedSlot(int selectedSlot) {
      this.queryType = HeldItemRenderOverrideEvent.QueryType.SELECTED_SLOT;
      this.selectedSlot = selectedSlot;
      this.stack = ItemStack.EMPTY;
      this.entity = null;
      this.arm = null;
      return this;
   }

   public HeldItemRenderOverrideEvent resetForStackInArm(LivingEntity entity, HumanoidArm arm, ItemStack stack) {
      this.queryType = HeldItemRenderOverrideEvent.QueryType.STACK_IN_ARM;
      this.selectedSlot = -1;
      this.stack = stack;
      this.entity = entity;
      this.arm = arm;
      return this;
   }

   @Generated
   public ItemStack getStack() {
      return this.stack;
   }

   @Generated
   public void setArm(HumanoidArm arm) {
      this.arm = arm;
   }

   @Generated
   public void setEntity(LivingEntity entity) {
      this.entity = entity;
   }

   @Generated
   public void setQueryType(HeldItemRenderOverrideEvent.QueryType queryType) {
      this.queryType = queryType;
   }

   @Generated
   public void setSelectedSlot(int selectedSlot) {
      this.selectedSlot = selectedSlot;
   }

   @Generated
   public void setStack(ItemStack stack) {
      this.stack = stack;
   }

   @Generated
   public HeldItemRenderOverrideEvent.QueryType getQueryType() {
      return this.queryType;
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }

   public HeldItemRenderOverrideEvent resetForMainHandStack(LivingEntity entity, ItemStack stack) {
      this.queryType = HeldItemRenderOverrideEvent.QueryType.MAIN_HAND_STACK;
      this.selectedSlot = -1;
      this.stack = stack;
      this.entity = entity;
      this.arm = null;
      return this;
   }

   @Generated
   public HumanoidArm getArm() {
      return this.arm;
   }

   @Generated
   public int getSelectedSlot() {
      return this.selectedSlot;
   }

   public enum QueryType {
      SELECTED_SLOT,
      MAIN_HAND_STACK,
      STACK_IN_ARM;
   }
}
