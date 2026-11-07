package sg.modules.impl.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;

public class SkeletonESP extends ClientModule {
   private final ColorSetting Ю;
   private final ColorSetting Ч;
   private final FloatSetting р;
   private final EventListener<WorldEffectsEvent> й;
   private static final String сЕЭ = "SkeletonESP";
   private static final String сЕ必 = "Рендерит скелет игроков линиями";
   private static final String сЕщ = "Цвет";
   private static final String сЕГ = "Цвет друзей";
   private static final int сЕл = -16711936;
   private static final String сЕэ = "Толщина";
   private static final float сЕю = 1.5F;
   private static final float сЕЩ = 0.5F;
   private static final float сЕу = 3.0F;
   private static final float сЕ2 = 0.5F;
   private static final float сЕф = 0.5F;
   private static final float сЕи = 1.05F;
   private static final double сЕ9 = 1.6;
   private static final double сЕо = 1.2;
   private static final double сЕO = 0.6;
   private static final double сЕ7 = 0.33F;
   private static final double сЕР = 0.8;
   private static final double сЕ3 = 0.15;
   private static final double сЕр = 1.5;
   private static final double сЕ6 = 1.2;
   private static final double сЕ_ = 0.6;
   private static final double сЕШ = 0.8;
   private static final double сЕы = 0.1;
   private static final double сЕй = 0.33F;
   private static final double сЕЖ = 0.25;
   private static final double сЕм = 1.25;
   private static final double сЕК = 0.95;
   private static final double сЕI = 0.45;
   private static final double сЕТ = 0.55;
   private static final double сЕО = 0.1;
   private static final double сЕФ = 1.5;
   private static final double сЕЙ = 1.2;
   private static final double сЕк = 0.6;
   private static final double сЕг = 0.8;
   private static final double сЕ0 = 0.1;
   private static final double сЕх = 0.33F;
   private static final double сЕУ = 1.5;
   private static final double сЕЗ = 1.2;
   private static final double сЕЕ = 0.6;
   private static final double сЕП = 0.8;
   private static final double сЕ西 = 0.1;
   private static final double сЕд = 0.33F;
   private static final float сЕБ = -50.0F;
   private static final float сЕС = 50.0F;
   private static final float сЕз = 3.0F;
   private static final float сЕц = 0.8F;
   private static final float сЕя = 0.6662F;
   private static final float сПс = (float) Math.PI;
   private static final float сПВ = 0.5F;
   private static final float сПЯ = 0.6662F;
   private static final float сПХ = 0.5F;
   private static final float сПе = 0.6662F;
   private static final float сПА = 1.4F;
   private static final float сП4 = 0.6662F;
   private static final float сПп = (float) Math.PI;
   private static final float сПД = 1.4F;
   private static final float сПЪ = 90.0F;
   private static final float сПт = -0.1F;
   private static final float сПа = 4.0F;
   private static final float сПЮ = 0.5F;
   private static final float сПЧ = 180.0F / (float)Math.PI;
   private static final float сПЛ = 180.0F / (float)Math.PI;
   private static final float сП诶 = 180.0F / (float)Math.PI;
   private static final float сП1 = 180.0F / (float)Math.PI;
   private static final float сПЬ = 0.0025F;

   public SkeletonESP() {
      super(сЕЭ, сЕ必, ModuleCategory.RENDER);
      this.Ю = new ColorSetting(сЕщ, false, -1);
      this.Ч = new ColorSetting(сЕГ, false, сЕл);
      this.р = new FloatSetting(сЕэ, сЕю, сЕЩ, сЕу).withStep(сЕ2);
      this.й = this::method_2;
      this.addSettings(this.Ю, this.Ч, this.р);
   }

   private void э(PoseStack var1, VertexConsumer var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      WorldBoxGeometry.appendLine(var1.last(), var2, var3, var4, var5, var6, var7, var8, var9);
      float var10 = Math.max(0.0F, this.р.getValue() - 1.0F) * сПЬ;
      if (var10 > 0.0F) {
         WorldBoxGeometry.appendLine(var1.last(), var2, var3 + var10, var4, var5, var6 + var10, var7, var8, var9);
         WorldBoxGeometry.appendLine(var1.last(), var2, var3 - var10, var4, var5, var6 - var10, var7, var8, var9);
         WorldBoxGeometry.appendLine(var1.last(), var2, var3, var4, var5 + var10, var6, var7, var8 + var10, var9);
         WorldBoxGeometry.appendLine(var1.last(), var2, var3, var4, var5 - var10, var6, var7, var8 - var10, var9);
      }
   }

   private void э(PoseStack var1, VertexConsumer var2, Player var3, Vec3 var4, float var5) {
      double var6 = Mth.lerp(var5, var3.xo, var3.getX()) - var4.x;
      double var8 = Mth.lerp(var5, var3.yo, var3.getY()) - var4.y;
      double var10 = Mth.lerp(var5, var3.zo, var3.getZ()) - var4.z;
      float var12 = var3.isBaby() ? сЕф : сЕи;
      double var13 = var8 + сЕ9 * var12;
      double var15 = var8 + сЕо * var12;
      double var17 = var8 + сЕO * var12;
      double var19 = сЕ7;
      double var21 = var8 + сЕР * var12;
      double var23 = сЕ3 * var12;
      double var25 = var8;
      boolean var27 = var3.isVisuallySwimming() || var3.isSwimming();
      boolean var28 = var3.isFallFlying();
      boolean var29 = var3.isVisuallyCrawling() && !var3.isCrouching();
      boolean var30 = var3.isCrouching();
      if (var27) {
         var13 = var8 + сЕр * var12;
         var15 = var8 + сЕ6 * var12;
         var17 = var8 + сЕ_ * var12;
         var21 = var8 + сЕШ * var12;
         var25 = var8 + сЕы * var12;
         var19 = сЕй;
         var23 = сЕЖ * var12;
      } else if (var30) {
         var13 = var8 + сЕм * var12;
         var15 = var8 + сЕК * var12;
         var17 = var8 + сЕI * var12;
         var21 = var8 + сЕТ * var12;
         var25 = var8 - сЕО * var12;
      } else if (var28) {
         var13 = var8 + сЕФ * var12;
         var15 = var8 + сЕЙ * var12;
         var17 = var8 + сЕк * var12;
         var21 = var8 + сЕг * var12;
         var25 = var8 + сЕ0 * var12;
         var19 = сЕх;
      } else if (var29) {
         var13 = var8 + сЕУ * var12;
         var15 = var8 + сЕЗ * var12;
         var17 = var8 + сЕЕ * var12;
         var21 = var8 + сЕП * var12;
         var25 = var8 + сЕ西 * var12;
         var19 = сЕд;
      }

      int var31 = RoseClient.getInstance().getServices().getFriends().О(var3.getName().getString()) ? this.Ч.getValue() : this.Ю.getValue();
      float var32 = Mth.rotLerp(var5, var3.yBodyRotO, var3.yBodyRot);
      float var33 = Mth.rotLerp(var5, var3.yHeadRotO, var3.yHeadRot);
      float var34 = Mth.lerp(var5, var3.xRotO, var3.getXRot());
      float var35 = var33 - var32;
      var34 = Mth.clamp(var34, сЕБ, сЕС);
      float var36 = var3.walkAnimation.position(var5);
      float var37 = var3.walkAnimation.speed(var5);
      if (var3.isBaby()) {
         var36 *= сЕз;
         var37 *= сЕц;
      }

      if (var37 > 1.0F) {
         var37 = 1.0F;
      }

      float var38 = 0.0F;
      float var39 = 0.0F;
      float var40 = 0.0F;
      float var41 = 0.0F;
      if (!var27 && !var28 && !var29) {
         var38 = Mth.cos(var36 * сЕя + сПс) * 2.0F * var37 * сПВ;
         var39 = Mth.cos(var36 * сПЯ) * 2.0F * var37 * сПХ;
         var40 = Mth.cos(var36 * сПе) * сПА * var37;
         var41 = Mth.cos(var36 * сП4 + сПп) * сПД * var37;
      }

      var1.pushPose();
      var1.translate(var6, var8, var10);
      var1.mulPose(Axis.YP.rotationDegrees(-var32));
      if (var27 || var28 || var29) {
         var1.mulPose(Axis.XP.rotationDegrees(var34 + сПЪ));
      } else if (var30) {
         var1.translate(0.0F, сПт * var12, 0.0F);
         var1.mulPose(Axis.XP.rotationDegrees(сПа));
      }

      var1.pushPose();
      var1.translate(0.0F, (float)(var15 - var8), 0.0F);
      var1.mulPose(Axis.YP.rotationDegrees(-var35));
      if (!var27 && !var28 && !var29) {
         var1.mulPose(Axis.XP.rotationDegrees(var34 * сПЮ));
      }

      this.э(var1, var2, 0.0F, 0.0F, 0.0F, 0.0F, (float)(var13 - var15), 0.0F, var31);
      var1.popPose();
      this.э(var1, var2, 0.0F, (float)(var15 - var8), 0.0F, 0.0F, (float)(var17 - var8), 0.0F, var31);
      var1.pushPose();
      var1.translate((float)(-var19), (float)(var15 - var8), 0.0F);
      var1.mulPose(Axis.XP.rotationDegrees(-var39 * сПЧ));
      this.э(var1, var2, 0.0F, 0.0F, 0.0F, 0.0F, (float)(var21 - var15), 0.0F, var31);
      var1.popPose();
      this.э(var1, var2, 0.0F, (float)(var15 - var8), 0.0F, (float)(-var19), (float)(var15 - var8), 0.0F, var31);
      var1.pushPose();
      var1.translate((float)var19, (float)(var15 - var8), 0.0F);
      var1.mulPose(Axis.XP.rotationDegrees(-var38 * сПЛ));
      this.э(var1, var2, 0.0F, 0.0F, 0.0F, 0.0F, (float)(var21 - var15), 0.0F, var31);
      var1.popPose();
      this.э(var1, var2, 0.0F, (float)(var15 - var8), 0.0F, (float)var19, (float)(var15 - var8), 0.0F, var31);
      var1.pushPose();
      var1.translate((float)(-var23), (float)(var17 - var8), 0.0F);
      var1.mulPose(Axis.XP.rotationDegrees(-var41 * сП诶));
      this.э(var1, var2, 0.0F, 0.0F, 0.0F, 0.0F, (float)(var25 - var17), 0.0F, var31);
      var1.popPose();
      this.э(var1, var2, 0.0F, (float)(var17 - var8), 0.0F, (float)(-var23), (float)(var17 - var8), 0.0F, var31);
      var1.pushPose();
      var1.translate((float)var23, (float)(var17 - var8), 0.0F);
      var1.mulPose(Axis.XP.rotationDegrees(-var40 * сП1));
      this.э(var1, var2, 0.0F, 0.0F, 0.0F, 0.0F, (float)(var25 - var17), 0.0F, var31);
      var1.popPose();
      this.э(var1, var2, 0.0F, (float)(var17 - var8), 0.0F, (float)var23, (float)(var17 - var8), 0.0F, var31);
      var1.popPose();
   }

   private void method_2(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         PoseStack var2 = var1.getPoses();
         VertexConsumer var3 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         float var4 = var1.getTickDelta();
         Vec3 var5 = var1.getCameraPosition();

         for (AbstractClientPlayer var7 : MINECRAFT_CLIENT.level.players()) {
            if (this.method_2(var7)) {
               this.э(var2, var3, var7, var5, var4);
            }
         }
      }
   }

   private boolean method_2(Player var1) {
      if (!var1.isAlive() || var1.isRemoved()) {
         return false;
      } else {
         return var1 == MINECRAFT_CLIENT.player ? !MINECRAFT_CLIENT.options.getCameraType().isFirstPerson() : true;
      }
   }
}
