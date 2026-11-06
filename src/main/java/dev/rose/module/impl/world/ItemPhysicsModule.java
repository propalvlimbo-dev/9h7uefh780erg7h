package dev.rose.module.impl.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.NumberSetting;


public final class ItemPhysicsModule extends Module {
   private final NumberSetting spin = this.add(new NumberSetting("spin", 100.0, 0.0, 200.0, 10.0, "rose.unit.percent"));

   public ItemPhysicsModule() {
      super("item_physics", Category.WORLD, false);
   }

   @Override
   public int icon() {
      return 58090;
   }

   public float lift(ItemPhysicsModule.State item, float rest) {
      return item.rose$onGround() && !item.rose$block() ? 0.02F : rest;
   }

   public void rotate(PoseStack pose, ItemPhysicsModule.State item, float ageInTicks) {
      boolean block = item.rose$block();
      float yaw = item.rose$id() * 37 % 360;
      if (item.rose$onGround()) {
         pose.mulPose(Axis.YP.rotationDegrees(yaw));
         if (!block) {
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
         }
      } else {
         float age = ageInTicks * this.spin.floatValue() / 100.0F;
         pose.mulPose(Axis.YP.rotationDegrees(yaw + age * 9.0F));
         pose.mulPose(Axis.XP.rotationDegrees(age * 14.0F));
      }
   }

   public interface State {
      void rose$physics(boolean var1, int var2, boolean var3);

      boolean rose$onGround();

      int rose$id();

      boolean rose$block();
   }
}
