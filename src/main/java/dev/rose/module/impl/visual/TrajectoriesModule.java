package dev.rose.module.impl.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.Geometry;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public final class TrajectoriesModule extends Module {
   private static final int MAX_STEPS = 320;
   private final NumberSetting width = this.add(new NumberSetting("width", 2.0, 1.0, 5.0, 0.5, "rose.unit.pixels"));
   private final BoolSetting landing = this.add(new BoolSetting("landing", true));

   public TrajectoriesModule() {
      super("trajectories", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 58251;
   }

   private static TrajectoriesModule.Throw throwFor(LocalPlayer player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      boolean using = player.isUsingItem() && player.getUsedItemHand() == hand;
      if (stack.is(Items.BOW)) {
         if (!using) {
            return null;
         }

         float power = BowItem.getPowerForTime(stack.getUseDuration(player) - player.getUseItemRemainingTicks());
         return power < 0.1F ? null : new TrajectoriesModule.Throw(power * 3.0F, 0.05, 0.99, 0.0F);
      } else if (stack.is(Items.CROSSBOW)) {
         if (!CrossbowItem.isCharged(stack)) {
            return null;
         }

         ChargedProjectiles charged = (ChargedProjectiles)stack.get(DataComponents.CHARGED_PROJECTILES);
         boolean firework = charged != null && charged.contains(Items.FIREWORK_ROCKET);
         return firework ? null : new TrajectoriesModule.Throw(3.15F, 0.05, 0.99, 0.0F);
      } else if (stack.is(Items.TRIDENT)) {
         return using ? new TrajectoriesModule.Throw(2.5F, 0.05, 0.99, 0.0F) : null;
      } else if (stack.is(Items.ENDER_PEARL) || stack.is(Items.SNOWBALL) || stack.is(Items.EGG)) {
         return new TrajectoriesModule.Throw(1.5F, 0.03, 0.99, 0.0F);
      } else if (stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
         return new TrajectoriesModule.Throw(0.5F, 0.05, 0.99, -20.0F);
      } else {
         return stack.is(Items.EXPERIENCE_BOTTLE) ? new TrajectoriesModule.Throw(0.7F, 0.07, 0.99, -20.0F) : null;
      }
   }

   public void render(WorldRenderContext context) {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      PoseStack pose = context.matrices();
      if (this.isEnabled() && player != null && mc.level != null && pose != null && mc.options.getCameraType().isFirstPerson()) {
         InteractionHand hand = InteractionHand.MAIN_HAND;
         TrajectoriesModule.Throw shot = throwFor(player, hand);
         if (shot == null) {
            hand = InteractionHand.OFF_HAND;
            shot = throwFor(player, hand);
         }

         if (shot != null) {
            float partial = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            float yaw = player.getViewYRot(partial);
            float pitch = player.getViewXRot(partial);
            double x = Mth.lerp(partial, player.xo, player.getX());
            double y = Mth.lerp(partial, player.yo, player.getY()) + player.getEyeHeight() - 0.1;
            double z = Mth.lerp(partial, player.zo, player.getZ());
            float rad = (float) (Math.PI / 180.0);
            Vec3 direction = new Vec3(
                  -Mth.sin(yaw * rad) * Mth.cos(pitch * rad),
                  -Mth.sin((pitch + shot.pitchOffset()) * rad),
                  Mth.cos(yaw * rad) * Mth.cos(pitch * rad)
               )
               .normalize()
               .scale(shot.speed());
            Vec3 motion = player.getDeltaMovement();
            Vec3 velocity = direction.add(motion.x, player.onGround() ? 0.0 : motion.y, motion.z);
            List<Vec3> points = new ArrayList<>();
            Vec3 position = new Vec3(x, y, z);
            points.add(position);
            BlockHitResult hit = null;

            for (int step = 0; step < 320; step++) {
               Vec3 next = position.add(velocity);
               BlockHitResult clip = mc.level.clip(new ClipContext(position, next, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
               if (clip.getType() != HitResult.Type.MISS) {
                  points.add(clip.getLocation());
                  hit = clip;
                  break;
               }

               position = next;
               points.add(position);
               boolean water = mc.level.getFluidState(BlockPos.containing(position)).isSource();
               velocity = velocity.scale(water ? 0.6 : shot.drag()).subtract(0.0, shot.gravity(), 0.0);
               if (position.y < mc.level.getMinY() - 32) {
                  break;
               }
            }

            Camera camera = context.gameRenderer().getMainCamera();
            Vec3 cam = camera.position();
            HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean rightHand = arm == HumanoidArm.RIGHT;
            Vec3 side = new Vec3(camera.leftVector())
               .scale(rightHand ? -0.28 : 0.28)
               .add(new Vec3(camera.upVector()).scale(-0.16));
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            int from = Theme.accent();
            int to = Theme.accent2();
            Vec3 start = points.get(0);
            int count = points.size();

            for (int pass = 0; pass < 2; pass++) {
               RenderType type = RoseRenderTypes.lines();
               VertexConsumer lines = Geometry.lines(buffers.getBuffer(type), pass == 0 ? this.width.floatValue() * 2.6F : this.width.floatValue());
               PoseStack.Pose last = pose.last();
               float alpha = pass == 0 ? 0.22F : 0.95F;

               for (int i = 1; i < count; i++) {
                  Vec3 a = points.get(i - 1);
                  Vec3 b = points.get(i);
                  double distance = b.distanceTo(start);
                  if (!(distance < 0.8)) {
                     float t = (float)i / Math.max(1, count - 1);
                     float fade = (float)Math.min(1.0, (distance - 0.8) / 1.6);
                     Vec3 offsetA = side.scale(Math.max(0.0, 1.0 - a.distanceTo(start) / 7.0));
                     Vec3 offsetB = side.scale(Math.max(0.0, 1.0 - distance / 7.0));
                     line(
                        lines,
                        last,
                        a.add(offsetA).subtract(cam),
                        b.add(offsetB).subtract(cam),
                        ColorUtil.lerp(from, to, t),
                        alpha * fade
                     );
                  }
               }

               if (this.landing.isOn() && hit != null) {
                  double distance = hit.getLocation().distanceTo(start);
                  ring(lines, last, hit.getLocation().subtract(cam), hit.getDirection(), to, (float)(0.28 + distance * 0.012), alpha);
               }

               buffers.endBatch(type);
            }
         }
      }
   }

   private static void line(VertexConsumer lines, PoseStack.Pose pose, Vec3 a, Vec3 b, int color, float alpha) {
      float dx = (float)(b.x - a.x);
      float dy = (float)(b.y - a.y);
      float dz = (float)(b.z - a.z);
      float length = Mth.sqrt(dx * dx + dy * dy + dz * dz);
      if (!(length < 1.0E-4F)) {
         dx /= length;
         dy /= length;
         dz /= length;
         float r = ColorUtil.red(color) / 255.0F;
         float g = ColorUtil.green(color) / 255.0F;
         float bl = ColorUtil.blue(color) / 255.0F;
         lines.addVertex(pose, (float)a.x, (float)a.y, (float)a.z).setColor(r, g, bl, alpha).setNormal(pose, dx, dy, dz);
         lines.addVertex(pose, (float)b.x, (float)b.y, (float)b.z).setColor(r, g, bl, alpha).setNormal(pose, dx, dy, dz);
      }
   }

   private static void ring(VertexConsumer lines, PoseStack.Pose pose, Vec3 center, Direction face, int color, float radius, float alpha) {
      Vec3 normal = Vec3.atLowerCornerOf(face.getUnitVec3i());
      Vec3 axisA = Math.abs(normal.y) > 0.5 ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
      Vec3 axisB = normal.cross(axisA).normalize();
      axisA = axisB.cross(normal).normalize();
      Vec3 base = center.add(normal.scale(0.02));
      float pulse = radius * (1.0F + 0.12F * Mth.sin((float)System.currentTimeMillis() / 180.0F));
      int segments = 28;

      for (int i = 0; i < segments; i++) {
         double a0 = (Math.PI * 2) * i / segments;
         double a1 = (Math.PI * 2) * (i + 1) / segments;
         Vec3 p0 = base.add(axisA.scale(Math.cos(a0) * pulse)).add(axisB.scale(Math.sin(a0) * pulse));
         Vec3 p1 = base.add(axisA.scale(Math.cos(a1) * pulse)).add(axisB.scale(Math.sin(a1) * pulse));
         line(lines, pose, p0, p1, color, alpha);
      }
   }

   private record Throw(float speed, double gravity, double drag, float pitchOffset) {
   }
}
