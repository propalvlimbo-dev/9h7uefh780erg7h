package sg.render.pipeline;

import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.ec.MinecraftAccess;
import sg.modules.impl.combat.HitBox;
import sg.client.RoseClient;

public final class AimRaycast implements MinecraftAccess {
   private static final float Эр = (float) (Math.PI / 180.0);
   private static final float Э6 = (float) Math.PI;
   private static final float Э_ = (float) (Math.PI / 180.0);
   private static final String ЭШ = "This is a utility class and cannot be instantiated";

   public static boolean method_2(float var0, float var1, double var2, Entity var4, boolean var5) {
      Vec3 var6 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var7 = method_2(var1, var0);
      Vec3 var8 = var6.add(var7.scale(var2));
      AABB var9 = var4.getBoundingBox();
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         HitBox var10 = RoseClient.getInstance().getServices().getModules().getHitBox();
         if (var10 != null) {
            var9 = var10.й(var4, var9);
         }
      }

      boolean var13 = var9.contains(var6) || var9.clip(var6, var8).isPresent();
      if (!var13) {
         return false;
      }

      if (var5) {
         Vec3 var11;
         if (var9.contains(var6)) {
            var11 = var6;
         } else {
            var11 = var9.clip(var6, var8).get();
         }

         ClipContext var12 = new ClipContext(var6, var11, Block.OUTLINE, Fluid.NONE, MINECRAFT_CLIENT.player);
         return MINECRAFT_CLIENT.level.clip(var12).getType() == Type.MISS;
      } else {
         return true;
      }
   }

   @Generated
   private AimRaycast() {
      throw new UnsupportedOperationException(ЭШ);
   }

   public static Vec3 method_2(float var0, float var1) {
      float var2 = -var1 * Эр - Э6;
      float var3 = -var0 * Э_;
      float var4 = Mth.cos(var2);
      float var5 = Mth.sin(var2);
      float var6 = -Mth.cos(var3);
      float var7 = Mth.sin(var3);
      return new Vec3(var5 * var6, var7, var4 * var6);
   }

   public static HitResult method_2(double var0, float var2, float var3, Entity var4) {
      Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var6 = method_2(var3, var2);
      Vec3 var7 = var5.add(var6.x * var0, var6.y * var0, var6.z * var0);
      return MINECRAFT_CLIENT.level.clip(new ClipContext(var5, var7, Block.OUTLINE, Fluid.NONE, var4));
   }
}
