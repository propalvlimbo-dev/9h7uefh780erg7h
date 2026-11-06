package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class ArmRenderEvent extends CancellableEvent implements ClientEvent {
   @SerializedName("Я")
   public final PoseStack poses;
   @SerializedName("Я")
   public final HumanoidArm arm;
   public final @com.google.gson.annotations.SerializedName("АЗ")  float swingProgress;

   public ArmRenderEvent(PoseStack poses, HumanoidArm arm, float swingProgress) {
      this.poses = poses;
      this.arm = arm;
      this.swingProgress = swingProgress;
   }
}
