package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.ec.MinecraftAccess;
import sg.events.impl.WorldGeometryBuffer;
import sg.render.CosmeticAnimationClip;
import sg.render.CosmeticModelRenderer;
import sg.render.CosmeticDefinition;

public final class PetMotionAnimator implements MinecraftAccess {
   private static final Set<String> field_5 = Set.of(PetMotionAnimator.сбж, PetMotionAnimator.сб弟, PetMotionAnimator.сбб, PetMotionAnimator.сбЫ);
   private static final Set<String> в = Set.of(PetMotionAnimator.сбl, PetMotionAnimator.сб8, PetMotionAnimator.сб5);
   @SerializedName("4")
   private final Random field_4 = new Random();
   private double Вб;
   private double ВЫ;
   private double Вl;
   private double В8;
   @SerializedName("В5")
   private double В5_3f39d5c3;
   private double Вв;
   @SerializedName("4ж")
   private float field_4ж;
   @SerializedName("4弟")
   private float field_4弟;
   @SerializedName("4б")
   private float field_4б;
   private boolean Я9;
   private boolean Яо;
   private boolean ЯO;
   private double Въ = 1.0;
   @SerializedName("4Ы")
   private float field_4Ы;
   @SerializedName("В5")
   private String В5_388008bb;
   private Vec3 р;
   private int Аэ;
   private static final String сжБ = "idle";
   private static final String сжС = "idle";
   private static final double сжз = 20.0;
   private static final float сжц = 0.01F;
   private static final double сжя = 324.0;
   private static final double с弟с = 0.22;
   private static final double с弟В = 0.06;
   private static final double с弟Я = 1.4;
   private static final double с弟Х = 0.02;
   private static final double с弟е = 0.22;
   private static final double с弟А = 0.48;
   private static final double с弟4 = 0.42;
   private static final double с弟п = 0.35;
   private static final double с弟Д = 1.6;
   private static final double с弟Ъ = 0.04;
   private static final double с弟т = 0.42;
   private static final double с弟а = 0.78;
   private static final double с弟Ю = 0.03;
   private static final double с弟Ч = 0.08;
   private static final double с弟Л = 0.42;
   private static final double с弟诶 = 0.35;
   private static final double с弟1 = 0.12;
   private static final double с弟Ь = 0.22;
   private static final double с弟衣 = 2.2;
   private static final double с弟ш = 1.45;
   private static final double с弟И = 0.16;
   private static final double с弟ь = 0.06;
   private static final double с弟ж = 0.16;
   private static final double с弟弟 = 12.0;
   private static final double с弟б = 5.0;
   private static final double с弟Ы = 7.0;
   private static final double с弟l = 3.4;
   private static final double с弟8 = 1.8;
   private static final double с弟5 = 10.0;
   private static final double с弟в = 2.4;
   private static final double с弟ъ = 6.0;
   private static final double с弟М = 1.5;
   private static final double с弟Н = 0.9;
   private static final double с弟н = 1.1;
   private static final double с弟Ц = 1.2;
   private static final double с弟ч = 0.2;
   private static final double с弟Э = 1.2;
   private static final double с弟必 = 2.8;
   private static final double с弟щ = 2.2;
   private static final double с弟Г = 0.25;
   private static final double с弟л = 1.4;
   private static final double с弟э = 0.18;
   private static final double с弟ю = 0.08;
   private static final double с弟Щ = 1.05;
   private static final double с弟у = 1.5;
   private static final double с弟2 = 0.12;
   private static final double с弟ф = 0.03;
   private static final double с弟и = 0.04;
   private static final double с弟9 = 0.06;
   private static final double с弟о = 0.62;
   private static final double с弟O = 1.3;
   private static final double с弟7 = 0.16;
   private static final double с弟Р = 1.05;
   private static final double с弟3 = 0.1;
   private static final double с弟р = 0.08;
   private static final double с弟6 = 0.95;
   private static final double с弟_ = 1.9;
   private static final double с弟Ш = 0.08;
   private static final double с弟ы = 0.02;
   private static final double с弟й = 0.012;
   private static final double с弟Ж = 1.0E-4;
   private static final double с弟м = 0.001;
   private static final double с弟К = 0.04;
   private static final double с弟I = 0.02;
   private static final float с弟Т = 14.0F;
   private static final float с弟О = 6.0F;
   private static final double с弟Ф = 0.045;
   private static final double с弟Й = 0.012;
   private static final double с弟к = 0.045;
   private static final float с弟г = 18.0F;
   private static final float с弟0 = 5.0F;
   private static final String с弟х = "fly";
   private static final String с弟У = "idle";
   private static final double с弟З = 1.4;
   private static final String с弟Е = "run";
   private static final String с弟П = "walk";
   private static final double с弟西 = 1.0E-4;
   private static final float с弟д = (float) (Math.PI / 180.0);
   private static final double с弟Б = 1.55;
   private static final double с弟С = 1.25;
   private static final double с弟з = 1.35;
   private static final double с弟ц = 0.7;
   private static final double с弟я = 0.8;
   private static final double сбс = 0.5;
   private static final double сбВ = 4.2;
   private static final double сбЯ = 0.5;
   private static final double сбХ = 2.4;
   private static final double сбе = 0.35;
   private static final double сбА = 3.4;
   private static final double сб4 = 0.35;
   private static final double сбп = 1.35;
   private static final double сбД = 0.22;
   private static final double сбЪ = 0.32;
   private static final double сбт = 0.4225;
   private static final double сба = 1.0E-4;
   private static final float сбЮ = (float) (Math.PI / 180.0);
   private static final double сбЧ = 0.65;
   private static final double сбЛ = 0.4225;
   private static final double сб诶 = 1.0E-4;
   private static final float сб1 = (float) (Math.PI / 180.0);
   private static final double сбЬ = 0.65;
   private static final double сб衣 = Double.MAX_VALUE;
   private static final double сбш = 3.0;
   private static final double сбИ = 0.4;
   private static final float сбь = 90.0F;
   private static final String сбж = "fly_rope_left";
   private static final String сб弟 = "fly_rope_right";
   private static final String сбб = "fly_rope_left_tip";
   private static final String сбЫ = "fly_rope_right_tip";
   private static final String сбl = "rope_spin";
   private static final String сб8 = "left_handle";
   private static final String сб5 = "right_handle";

   public void Гс() {
      this.Я9 = false;
      this.р = null;
      this.Яо = false;
      this.ЯO = false;
      this.В5_388008bb = сжС;
      this.field_4Ы = 0.0F;
   }

   private Vec3 ж(Vec3 var1, Player var2) {
      double var3 = var1.x - var2.getX();
      double var5 = var1.z - var2.getZ();
      double var7 = var3 * var3 + var5 * var5;
      if (var7 >= сбЛ) {
         return var1;
      }

      double var9 = Math.sqrt(var7);
      if (var9 < сб诶) {
         float var11 = var2.getYRot() * сб1;
         var3 = -Mth.sin(var11);
         var5 = Mth.cos(var11);
         var9 = 1.0;
      }

      double var13 = сбЬ / var9;
      return new Vec3(var2.getX() + var3 * var13, var1.y, var2.getZ() + var5 * var13);
   }

   private void Б(Vec3 var1) {
      this.Вб = this.В8 = var1.x;
      this.ВЫ = this.В5_3f39d5c3 = var1.y;
      this.Вl = this.Вв = var1.z;
      this.р = var1;
   }

   private Vec3 ж(Player var1, boolean var2) {
      Vec3 var3 = new Vec3(var1.getDeltaMovement().x, 0.0, var1.getDeltaMovement().z);
      Vec3 var4;
      if (var3.lengthSqr() < с弟西) {
         float var5 = var1.getYRot() * с弟д;
         var4 = new Vec3(-Mth.sin(var5), 0.0, Mth.cos(var5));
      } else {
         var4 = var3.normalize();
      }

      Vec3 var16 = new Vec3(-var4.z, 0.0, var4.x);
      double var6 = var2 ? (var1.isFallFlying() ? с弟Б : с弟С) : с弟з;
      double var8 = (var2 ? с弟ц : с弟я) * this.Аэ;
      double var10 = var2 ? Math.clamp(сбс + var1.getDeltaMovement().length() * сбВ, сбЯ, сбХ) : Math.clamp(сбе + var3.length() * сбА, сб4, сбп);
      Vec3 var12 = var1.position().add(var4.scale(var10));
      Vec3 var13 = var12.subtract(var4.scale(var6)).add(var16.scale(var8));
      if (var2) {
         double var14 = var1.isFallFlying() ? сбД : сбЪ;
         return new Vec3(var13.x, var1.getY() + var14, var13.z);
      } else {
         return this.ж(new Vec3(var13.x, this.У(var13.x, var13.z, var1.getY()), var13.z), var1);
      }
   }

   private double Б(float var1) {
      return Mth.lerp(var1, this.В5_3f39d5c3, this.ВЫ);
   }

   private void method_0(Player var1) {
      Vec3 var2 = new Vec3(this.Вб, this.ВЫ, this.Вl);
      double var3 = var2.distanceToSqr(var1.position());
      double var5 = Math.sqrt(var3);
      double var7 = var1.getDeltaMovement().horizontalDistance();
      boolean var9 = var1.isFallFlying() || var1.getAbilities().flying;
      Vec3 var10 = this.ж(var1, var9);
      if (var3 > сжя) {
         this.Б(var10);
         this.ж(var10, 0.0, false, var9, 1.0);
      } else {
         double var11 = var9
            ? Math.clamp(с弟с + var1.getDeltaMovement().length() * с弟В + Math.max(0.0, var5 - с弟Я) * с弟Х, с弟е, с弟А)
            : Math.clamp(с弟4 + var7 * с弟п + Math.max(0.0, var5 - с弟Д) * с弟Ъ, с弟т, с弟а);
         this.р = this.р == null ? var10 : this.р.lerp(var10, var11);
         Vec3 var13 = this.р.subtract(var2);
         double var14 = Math.hypot(var13.x, var13.z);
         double var16 = var13.length();
         boolean var18 = var7 > с弟Ю || Math.abs(var1.getDeltaMovement().y) > с弟Ч;
         boolean var19 = var14 < с弟Л && Math.abs(var13.y) < с弟诶;
         if (!var9 && var19 && !var18) {
            this.ж(this.ь(var2, var1.getY()), 0.0, false, false, 1.0);
         } else {
            MobEffectInstance var20 = var1.getEffect(MobEffects.SPEED);
            int var21 = var20 != null ? var20.getAmplifier() + 1 : 0;
            if (var9) {
               boolean var28 = var16 > с弟1 || var18;
               double var23 = Math.clamp(
                  с弟Ь + var1.getDeltaMovement().length() * (var1.isFallFlying() ? с弟衣 : с弟ш) + Math.max(0.0, var5 - 1.0) * с弟И + var21 * с弟ь,
                  с弟ж,
                  var1.isFallFlying() ? (var5 > с弟弟 ? с弟б : (var5 > с弟Ы ? с弟l : с弟8)) : (var5 > с弟5 ? с弟в : (var5 > с弟ъ ? с弟М : с弟Н))
               );
               double var25 = Math.clamp(с弟н + Math.max(0.0, var5 - с弟Ц) * с弟ч + var1.getDeltaMovement().length() * с弟Э, 1.0, var1.isFallFlying() ? с弟必 : с弟щ);
               this.ж(this.р, var23, var28, true, var25);
               this.В(var1);
            } else {
               double var22 = Math.clamp((var14 - с弟Г) / с弟л, с弟э, 1.0);
               double var24 = Math.clamp(с弟ю + var7 * с弟Щ + Math.max(0.0, var5 - с弟у) * с弟2 + var21 * с弟ф + (var1.isSprinting() ? с弟и : 0.0), с弟9, с弟о) * var22;
               double var26 = Math.clamp(1.0 + Math.max(0.0, var5 - с弟O) * с弟7 + var7 * с弟Р + var21 * с弟3 + (var1.isSprinting() ? с弟р : 0.0), с弟6, с弟_);
               this.ж(this.р, var24, var14 > с弟Ш || var18, false, var26);
               this.В(var1);
            }
         }
      }
   }

   private static float ъ(float var0, float var1, float var2) {
      return var0 + Mth.clamp(Mth.wrapDegrees(var1 - var0), -var2, var2);
   }

   public PetMotionAnimator() {
      this.В5_388008bb = сжБ;
      this.Аэ = 1;
   }

   private static float Г(double var0, double var2) {
      return (float)Math.toDegrees(Mth.atan2(var2, var0)) - сбь;
   }

   public void ж(PoseStack var1, WorldGeometryBuffer var2, Vec3 var3, float var4, CosmeticDefinition var5, float var6) {
      if (this.Я9 && var5 != null && !(var6 < сжц)) {
         CosmeticAnimationClip var7 = var5.clip(this.В5_388008bb);
         RenderType var8 = RenderTypes.entityCutout(var5.texture());
         VertexConsumer var9 = var2.getBuffer(var8);
         var1.pushPose();
         var1.translate(this.Ю(var4) - var3.x, this.Б(var4) - var3.y, this.у(var4) - var3.z);
         var1.mulPose(Axis.YP.rotationDegrees(-this.Яр(var4)));
         float var10 = var5.scale();
         var1.scale(var10, var10, var10);
         CosmeticModelRenderer.render(var1, var9, var5.model(), var7, this.field_4Ы, this.ЯЫ(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, var6, this.ЯO ? в : field_5);
         var1.popPose();
         if (var2 instanceof WorldGeometryBuffer.StagedBuffer var11) {
            var11.flush(var8);
         }
      }
   }

   public void Г(Player var1) {
      if (var1 != null && MINECRAFT_CLIENT.level != null) {
         if (!this.Я9) {
            this.в(var1);
         }

         this.В8 = this.Вб;
         this.В5_3f39d5c3 = this.ВЫ;
         this.Вв = this.Вl;
         this.field_4弟 = this.field_4ж;
         this.method_0(var1);
         this.field_4Ы = this.field_4Ы + (float)(this.Въ / сжз);
      } else {
         this.Гс();
      }
   }

   private void в(Player var1) {
      this.Аэ = this.field_4.nextBoolean() ? 1 : -1;
      Vec3 var2 = this.ж(var1, var1.isFallFlying() || var1.getAbilities().flying);
      this.Вб = this.В8 = var2.x;
      this.ВЫ = this.В5_3f39d5c3 = var2.y;
      this.Вl = this.Вв = var2.z;
      this.field_4ж = this.field_4弟 = this.field_4б = var1.getYRot();
      this.р = var2;
      this.Я9 = true;
   }

   private double У(double var1, double var3, double var5) {
      ClientLevel var7 = MINECRAFT_CLIENT.level;
      if (var7 == null) {
         return var5;
      }

      int var8 = Mth.floor(var1);
      int var9 = Mth.floor(var3);
      int var10 = Mth.floor(var5) + 2;
      int var11 = Mth.floor(var5) - 8;
      MutableBlockPos var12 = new MutableBlockPos(var8, var10, var9);
      double var13 = var5;
      double var15 = сб衣;

      for (int var17 = var10; var17 >= var11; var17--) {
         var12.setY(var17);
         BlockState var18 = var7.getBlockState(var12);
         if (!var18.is(BlockTags.LEAVES)) {
            VoxelShape var19 = var18.getCollisionShape(var7, var12);
            if (!var19.isEmpty()) {
               double var20 = var17 + var19.max(net.minecraft.core.Direction.Axis.Y);
               double var22 = Math.abs(var20 - var5);
               if (var22 < var15) {
                  var15 = var22;
                  var13 = var20;
               }
            }
         }
      }

      return var15 <= сбш ? var13 : var5;
   }

   private void ж(Vec3 var1, double var2, boolean var4, boolean var5, double var6) {
      this.ЯO = var5;
      this.Въ = var6;
      Vec3 var8 = new Vec3(this.Вб, this.ВЫ, this.Вl);
      Vec3 var9 = var1.subtract(var8);
      double var10 = Math.hypot(var9.x, var9.z);
      double var12 = var9.length();
      if (var5) {
         if (var4 && var12 > с弟ы) {
            double var14 = Math.min(var12, Math.max(с弟й, var2));
            Vec3 var16 = var9.normalize().scale(var14);
            this.Вб = this.Вб + var16.x;
            this.ВЫ = this.ВЫ + var16.y;
            this.Вl = this.Вl + var16.z;
            if (var10 > с弟Ж) {
               this.field_4б = Г(var9.x, var9.z);
            }
         } else if (Math.abs(var9.y) > с弟м) {
            this.ВЫ = this.ВЫ + Math.copySign(Math.min(Math.abs(var9.y), с弟К), var9.y);
         }

         this.Яо = var4 && var12 > с弟I;
         this.field_4ж = ъ(this.field_4ж, this.field_4б, this.Яо ? с弟Т : с弟О);
      } else {
         if (var4 && var10 > с弟Ф) {
            this.field_4б = Г(var9.x, var9.z);
            double var17 = Math.min(var10, Math.max(с弟Й, var2));
            this.Вб = this.Вб + var9.x / var10 * var17;
            this.Вl = this.Вl + var9.z / var10 * var17;
         }

         this.ВЫ = this.У(this.Вб, this.Вl, var1.y);
         this.Яо = var4 && var10 > с弟к;
         this.field_4ж = ъ(this.field_4ж, this.field_4б, this.Яо ? с弟г : с弟0);
      }

      String var18 = this.ЯO ? с弟х : (!this.Яо ? с弟У : (this.Въ >= с弟З ? с弟Е : с弟П));
      if (!var18.equals(this.В5_388008bb)) {
         this.В5_388008bb = var18;
         this.field_4Ы = 0.0F;
      }
   }

   private float Яр(float var1) {
      return Mth.rotLerp(var1, this.field_4弟, this.field_4ж);
   }

   private double Ю(float var1) {
      return Mth.lerp(var1, this.В8, this.Вб);
   }

   private double у(float var1) {
      return Mth.lerp(var1, this.Вв, this.Вl);
   }

   private int ЯЫ() {
      if (MINECRAFT_CLIENT.level == null) {
         return LightCoordsUtil.pack(15, 15);
      }

      BlockPos var1 = BlockPos.containing(this.Вб, this.ВЫ + сбИ, this.Вl);
      return LightCoordsUtil.pack(MINECRAFT_CLIENT.level.getBrightness(LightLayer.BLOCK, var1), MINECRAFT_CLIENT.level.getBrightness(LightLayer.SKY, var1));
   }

   private void В(Player var1) {
      double var2 = this.Вб - var1.getX();
      double var4 = this.Вl - var1.getZ();
      double var6 = var2 * var2 + var4 * var4;
      if (!(var6 >= сбт)) {
         double var8 = Math.sqrt(var6);
         if (var8 < сба) {
            float var10 = var1.getYRot() * сбЮ;
            var2 = -Mth.sin(var10);
            var4 = Mth.cos(var10);
            var8 = 1.0;
         }

         double var12 = сбЧ / var8;
         this.Вб = var1.getX() + var2 * var12;
         this.Вl = var1.getZ() + var4 * var12;
      }
   }

   private Vec3 ь(Vec3 var1, double var2) {
      return new Vec3(var1.x, this.У(var1.x, var1.z, var2), var1.z);
   }
}
