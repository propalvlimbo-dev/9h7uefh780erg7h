package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sg.mixinbridge.ModelBending;
import sg.util.time.EmoteSyncController;
import sg.util.time.EmoteSyncMessage;

public final class PlayerEmoteController {
   private static final Map<AvatarRenderState, UUID> п = new WeakHashMap<>();
   private static final Map<String, Integer> Д = new HashMap<>();
   private static final Map<UUID, PlayerEmoteController.ActiveEmote> Ъ = new HashMap<>();
   private static PlayerEmoteController.ActiveEmote В;
   private static final float сЧ7 = 1.0E-4F;
   private static final float сЧР = 1.0E9F;
   private static final long сЧ3 = 180L;
   private static final String сЧр = "head";
   private static final String сЧ6 = "torso";
   private static final String сЧ_ = "right_arm";
   private static final float сЧШ = -5.0F;
   private static final String сЧы = "left_arm";
   private static final float сЧй = 5.0F;
   private static final String сЧЖ = "right_leg";
   private static final float сЧм = -1.9F;
   private static final float сЧК = 12.0F;
   private static final float сЧI = 0.1F;
   private static final String сЧТ = "left_leg";
   private static final float сЧО = 1.9F;
   private static final float сЧФ = 12.0F;
   private static final float сЧЙ = 0.1F;
   private static final String сЧк = "body";
   private static final float сЧг = 16.0F;
   private static final float сЧ0 = 16.0F;
   private static final float сЧх = 16.0F;
   private static final float сЧУ = 0.7F;
   private static final float сЧЗ = -0.7F;
   private static final String сЧЕ = "left_arm";
   private static final String сЧП = "right_arm";
   private static final float сЧ西 = 1.0E-4F;
   private static final float сЧд = 0.25F;
   private static final float сЧБ = -0.25F;
   private static final String сЧС = "left_leg";
   private static final String сЧз = "right_leg";
   private static final float сЧц = 0.375F;
   private static final String сЧя = "body";
   private static final String сЛс = "torso";
   private static final float сЛВ = 0.375F;
   private static final float сЛЯ = 1.0E-4F;
   private static final float сЛХ = (float) (Math.PI / 180.0);
   private static final String сЛе = "body";
   private static final String сЛА = "torso";
   private static final String сЛ4 = "right_arm";
   private static final String сЛп = "left_arm";
   private static final String сЛД = "right_leg";
   private static final String сЛЪ = "left_leg";

   private static void 衣(AvatarRenderState var0, String var1, float var2, PoseStack var3) {
      long var4 = System.nanoTime();
      PlayerEmoteController.ActiveEmote var6 = 衣(var0, var4);
      if (var6 != null) {
         EmoteAnimationClip.BonePoseSample var7 = var6.В_36700806.З(var1, var6.Т(var4));
         衣(var7.bendDirection(), var7.bend() * var6.Ы(var4), var2, var3);
      }
   }

   public static void 衣(AvatarRenderState var0, boolean var1, PoseStack var2) {
      衣(var0, var1 ? сЧС : сЧз, сЧц, var2);
   }

   private static void 衣(HumanoidModel<?> var0) {
      ModelBending.в_de5a6f78(var0.body);
      ModelBending.в_de5a6f78(var0.rightArm);
      ModelBending.в_de5a6f78(var0.leftArm);
      ModelBending.в_de5a6f78(var0.rightLeg);
      ModelBending.в_de5a6f78(var0.leftLeg);
      ModelBending.衣_de5a6f78(var0.head);
      ModelBending.衣_de5a6f78(var0.rightArm);
      ModelBending.衣_de5a6f78(var0.leftArm);
      if (var0 instanceof PlayerModel var1) {
         ModelBending.в_de5a6f78(var1.jacket);
         ModelBending.в_de5a6f78(var1.rightSleeve);
         ModelBending.в_de5a6f78(var1.leftSleeve);
         ModelBending.в_de5a6f78(var1.rightPants);
         ModelBending.в_de5a6f78(var1.leftPants);
      }
   }

   public static void э(AvatarRenderState var0, PoseStack var1) {
      long var2 = System.nanoTime();
      PlayerEmoteController.ActiveEmote var4 = 衣(var0, var2);
      if (var4 != null) {
         EmoteAnimationClip var5 = var4.В_36700806;
         float var6 = var4.Т(var2);
         float var7 = var4.Ы(var2);
         EmoteAnimationClip.BonePoseSample var8 = var5.З(сЧя, var6);
         EmoteAnimationClip.BonePoseSample var9 = var5.З(сЛс, var6);
         衣(var8.bendDirection() + var9.bendDirection(), (var8.bend() + var9.bend()) * var7, сЛВ, var1);
      }
   }

   private static void 衣(ModelPart var0, EmoteAnimationClip.BonePoseSample var1, float var2, float var3, float var4, float var5) {
      Vector3f var6 = var1.rotation();
      Vector3f var7 = var1.position();
      float var8 = сЛХ;
      if (var1.hasRotation()) {
         var0.xRot = Mth.lerp(var2, var0.xRot, var6.x * var8);
         var0.yRot = Mth.lerp(var2, var0.yRot, var6.y * var8);
         var0.zRot = Mth.lerp(var2, var0.zRot, var6.z * var8);
      }

      if (var1.hasPosition()) {
         var0.x = Mth.lerp(var2, var0.x, var3 + var7.x);
         var0.y = Mth.lerp(var2, var0.y, var4 - var7.y);
         var0.z = Mth.lerp(var2, var0.z, var5 + var7.z);
      }
   }

   public static void 必ш() {
      if (В != null && В.В_1c6e94e4.й == 0L) {
         В.В_1c6e94e4.з(System.nanoTime());
         EmoteSyncController.Ы().lв();
      }
   }

   public static void ш(boolean var0) {
      long var1 = System.nanoTime();
      Ъ.entrySet().removeIf(var2 -> !EmoteSyncController.Ы().Ы(var2.getKey(), var1) || var2.getValue().В_1c6e94e4.I(var1));
      if (В != null) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3.player != null && var3.level != null) {
            if (!var0
               && (
                  var3.player.input.getMoveVector().lengthSquared() > сЧ7
                     || var3.player.input.keyPresses.jump()
                     || var3.player.input.keyPresses.shift()
                     || var3.player.isFallFlying()
                     || var3.player.isSwimming()
               )) {
               必ш();
            }

            if (В != null) {
               float var4 = (float)(var1 - В.В_1c6e94e4.ы) / сЧР;
               if (!В.В_df4768fc.loop() && var4 >= В.В_36700806.ЗO()) {
                  必ш();
               }

               if (В != null && В.В_1c6e94e4.I(var1)) {
                  必т();
               }
            }
         } else {
            必т();
         }
      }
   }

   public static PlayerEmoteController.ActiveEmote 衣() {
      return В;
   }

   private PlayerEmoteController() {
   }

   private static void 衣(float var0, float var1, float var2, PoseStack var3) {
      if (!(Math.abs(var1) < сЛЯ)) {
         float var4 = -var0;
         var3.translate(0.0F, var2, 0.0F);
         var3.mulPose(new Quaternionf().rotateAxis(var1, (float)Math.cos(var4), 0.0F, (float)Math.sin(var4)));
         var3.translate(0.0F, -var2, 0.0F);
      }
   }

   private static PlayerEmoteController.ActiveEmote 衣(AvatarRenderState var0, long var1) {
      UUID var3 = п.get(var0);
      Minecraft var4 = Minecraft.getInstance();
      if (var3 != null && var4.player != null && EmoteSyncController.Ы().жэ()) {
         PlayerEmoteController.ActiveEmote var5 = var3.equals(var4.player.getUUID()) ? В : (EmoteSyncController.Ы().Ы(var3, var1) ? Ъ.get(var3) : null);
         if (var5 == null) {
            return null;
         }

         var5.Р(var1);
         return var5.В_1c6e94e4.I(var1) ? null : var5;
      } else {
         return null;
      }
   }

   public static void 衣(AvatarRenderState var0, PoseStack var1) {
      long var2 = System.nanoTime();
      PlayerEmoteController.ActiveEmote var4 = 衣(var0, var2);
      if (var4 != null) {
         EmoteAnimationClip var5 = var4.В_36700806;
         float var6 = var4.Т(var2);
         float var7 = var4.Ы(var2);
         EmoteAnimationClip.BonePoseSample var8 = var5.З(сЧк, var6);
         Vector3f var9 = var8.position();
         Vector3f var10 = var8.rotation();
         float var11 = var8.hasPosition() ? -var9.x * var7 / сЧг : 0.0F;
         float var12 = var8.hasPosition() ? var9.y * var7 / сЧ0 : 0.0F;
         float var13 = var8.hasPosition() ? var9.z * var7 / сЧх : 0.0F;
         float var14 = var8.hasRotation() ? -var10.x * var7 : 0.0F;
         float var15 = var8.hasRotation() ? -var10.y * var7 : 0.0F;
         float var16 = var8.hasRotation() ? var10.z * var7 : 0.0F;
         var1.translate(var11, var12 + сЧУ, var13);
         var1.mulPose(Axis.ZP.rotationDegrees(var16));
         var1.mulPose(Axis.YP.rotationDegrees(var15));
         var1.mulPose(Axis.XP.rotationDegrees(var14));
         var1.translate(0.0F, сЧЗ, 0.0F);
      }
   }

   public static void 必т() {
      В = null;
      EmoteSyncController.Ы().lы();
   }

   public static void 衣(UUID var0, EmoteSyncMessage var1, EmoteDefinition var2, long var3, long var5) {
      if (var1.state() != EmoteSyncMessage.State.CLEAR && (var1.state() != EmoteSyncMessage.State.STOP || var1.stopAgeMillis() + var5 < сЧ3)) {
         long var7 = var1.elapsedMillis() + var5;
         PlayerEmoteController.ActiveEmote var9 = Ъ.get(var0);
         if (var9 == null || !var9.А.equals(var1.id()) || var9.В_df4768fc != var2 || !var9.ск.equals(var1.variant())) {
            var9 = new PlayerEmoteController.ActiveEmote(var1.id(), var2, var1.variant(), var3, var7);
            Ъ.put(var0, var9);
         }

         var9.В_1c6e94e4.д(var3, var7, var1.state() == EmoteSyncMessage.State.STOP, var1.stopAgeMillis() + var5);
         var9.Р(var3);
      } else {
         Ъ.remove(var0);
      }
   }

   public static void 衣(EmoteDefinition var0) {
      EmoteSyncController.Ы().lТ();
      int var1 = Д.getOrDefault(var0.id(), 0);
      String var2 = var0.animations().get(var1 % var0.animations().size());
      Д.put(var0.id(), (var1 + 1) % var0.animations().size());
      В = new PlayerEmoteController.ActiveEmote(UUID.randomUUID(), var0, var2, System.nanoTime(), 0L);
      В.В_1c6e94e4.ы = System.nanoTime();
      EmoteSyncController.Ы().lв();
   }

   public static void 衣(AvatarRenderState var0, UUID var1) {
      п.put(var0, var1);
   }

   private static void 衣(HumanoidModel<?> var0, EmoteAnimationClip var1, float var2, float var3) {
      Object var4 = ModelBending.в_7fd512f1(var0.root());
      EmoteAnimationClip.BonePoseSample var5 = var1.З(сЛе, var2);
      EmoteAnimationClip.BonePoseSample var6 = var1.З(сЛА, var2);
      EmoteAnimationClip.BonePoseSample var7 = var1.З(сЛ4, var2);
      EmoteAnimationClip.BonePoseSample var8 = var1.З(сЛп, var2);
      EmoteAnimationClip.BonePoseSample var9 = var1.З(сЛД, var2);
      EmoteAnimationClip.BonePoseSample var10 = var1.З(сЛЪ, var2);
      float var11 = var6.bendDirection() + var5.bendDirection();
      float var12 = (var6.bend() + var5.bend()) * var3;
      ModelBending.в(var0.body, var4, true, var11, var12);
      ModelBending.в(var0.rightArm, var4, false, var7.bendDirection(), var7.bend() * var3);
      ModelBending.в(var0.leftArm, var4, false, var8.bendDirection(), var8.bend() * var3);
      ModelBending.в(var0.rightLeg, var4, false, var9.bendDirection(), var9.bend() * var3);
      ModelBending.в(var0.leftLeg, var4, false, var10.bendDirection(), var10.bend() * var3);
      ModelBending.в(var0.head, var4, var11, var12);
      ModelBending.в(var0.rightArm, var4, var11, var12);
      ModelBending.в(var0.leftArm, var4, var11, var12);
      if (var0 instanceof PlayerModel var13) {
         ModelBending.в(var13.jacket, var4, true, var11, var12);
         ModelBending.в(var13.rightSleeve, var4, false, var7.bendDirection(), var7.bend() * var3);
         ModelBending.в(var13.leftSleeve, var4, false, var8.bendDirection(), var8.bend() * var3);
         ModelBending.в(var13.rightPants, var4, false, var9.bendDirection(), var9.bend() * var3);
         ModelBending.в(var13.leftPants, var4, false, var10.bendDirection(), var10.bend() * var3);
      }
   }

   public static void 必и() {
      В = null;
      Ъ.clear();
      п.clear();
   }

   public static void 衣(AvatarRenderState var0, HumanoidArm var1, PoseStack var2) {
      long var3 = System.nanoTime();
      PlayerEmoteController.ActiveEmote var5 = 衣(var0, var3);
      if (var5 != null) {
         EmoteAnimationClip var6 = var5.В_36700806;
         float var7 = var5.Т(var3);
         float var8 = var5.Ы(var3);
         EmoteAnimationClip.BonePoseSample var9 = var6.З(var1 == HumanoidArm.LEFT ? сЧЕ : сЧП, var7);
         float var10 = var9.bend() * var8;
         if (!(Math.abs(var10) < сЧ西)) {
            float var11 = -var9.bendDirection();
            var2.translate(0.0F, сЧд, 0.0F);
            var2.mulPose(new Quaternionf().rotateAxis(var10, (float)Math.cos(var11), 0.0F, (float)Math.sin(var11)));
            var2.translate(0.0F, сЧБ, 0.0F);
         }
      }
   }

   public static boolean l() {
      return В != null;
   }

   public static void 衣(HumanoidModel<?> var0, AvatarRenderState var1) {
      衣(var0);
      long var2 = System.nanoTime();
      PlayerEmoteController.ActiveEmote var4 = 衣(var1, var2);
      if (var4 != null) {
         EmoteAnimationClip var5 = var4.В_36700806;
         float var6 = var4.Т(var2);
         float var7 = var4.Ы(var2);
         衣(var0.head, var5.З(сЧр, var6), var7, 0.0F, 0.0F, 0.0F);
         衣(var0.body, var5.З(сЧ6, var6), var7, 0.0F, 0.0F, 0.0F);
         衣(var0.rightArm, var5.З(сЧ_, var6), var7, сЧШ, 2.0F, 0.0F);
         衣(var0.leftArm, var5.З(сЧы, var6), var7, сЧй, 2.0F, 0.0F);
         衣(var0.rightLeg, var5.З(сЧЖ, var6), var7, сЧм, сЧК, сЧI);
         衣(var0.leftLeg, var5.З(сЧТ, var6), var7, сЧО, сЧФ, сЧЙ);
         衣(var0, var5, var6, var7);
      }
   }

   public static final class ActiveEmote {
      public final UUID А;
      @SerializedName("В")
      public final EmoteDefinition В_df4768fc;
      public final String ск;
      @SerializedName("В")
      final EmoteAnimationClip В_36700806;
      @SerializedName("В")
      public final EmoteFadeTimeline В_1c6e94e4;
      private static final float суд = 1.0E9F;
      private static final float суБ = 1.0E9F;

      void Р(long var1) {
         if (!this.В_df4768fc.loop() && this != PlayerEmoteController.В) {
            long var3 = this.В_1c6e94e4.ы + (long)(this.В_36700806.ЗO() * суд);
            if (var1 >= var3 && (this.В_1c6e94e4.й == 0L || this.В_1c6e94e4.й > var3)) {
               this.В_1c6e94e4.й = var3;
            }
         }
      }

      float Ы(long var1) {
         return this.В_1c6e94e4.必(var1);
      }

      float Т(long var1) {
         return this.В_36700806.З((float)(var1 - this.В_1c6e94e4.ы) / суБ, this.В_df4768fc.loop());
      }

      ActiveEmote(UUID var1, EmoteDefinition var2, String var3, long var4, long var6) {
         this.А = var1;
         this.В_df4768fc = var2;
         this.ск = var3;
         this.В_36700806 = EmoteAnimationClip.З(var2, var3);
         this.В_1c6e94e4 = new EmoteFadeTimeline(var4, var6);
      }
   }
}
