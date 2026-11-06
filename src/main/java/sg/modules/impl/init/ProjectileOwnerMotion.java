package sg.modules.impl.init;

import net.minecraft.world.phys.Vec3;

public final class ProjectileOwnerMotion {
   private Vec3 Ж = Vec3.ZERO;
   private Vec3 м = Vec3.ZERO;
   private boolean Хс;
   private boolean ХВ;

   public void Г(Vec3 var1, boolean var2) {
      this.м = var1.subtract(this.Ж);
      this.Ж = var1;
      this.Хс = var2;
      this.ХВ = true;
   }

   public void П衣() {
      if (!this.ХВ) {
         this.м = Vec3.ZERO;
      }

      this.ХВ = false;
   }

   public ProjectileOwnerMotion.Sample l() {
      return new ProjectileOwnerMotion.Sample(this.Ж, this.м, this.Хс);
   }

   public void l(Vec3 var1, boolean var2) {
      this.Ж = var1;
      this.Хс = var2;
      this.м = Vec3.ZERO;
      this.ХВ = false;
   }

   public record Sample(Vec3 position, Vec3 movement, boolean onGround) {
      public Vec3 inherited() {
         return new Vec3(this.movement.x, this.onGround ? 0.0 : this.movement.y, this.movement.z);
      }
   }
}
