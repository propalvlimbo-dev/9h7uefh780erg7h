package sg.mixinbridge;

import net.minecraft.world.phys.Vec3;

public interface ResolvedEntityPoseAccess {
   void setResolvedPos(Vec3 var1);

   float getResolvedYaw();

   void setResolvedPitch(float var1);

   void setResolvedYaw(float var1);

   float getResolvedPitch();

   Vec3 getResolvedPos();

   Vec3 getResolvedForward();
}
