package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import settings.render.ChinaHatGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.mixin.RenderPipelinesAccessor;
import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;

public class ChinaHat extends ClientModule {
   private static final int АЫ = 64;
   private static final float Ад = 0.012F;
   private static final int Аl = 6;
   private static final int А8 = 5;
   private static final int А5 = 24;
   private static final int Ав = 8;
   private static final Identifier м = Identifier.fromNamespaceAndPath(ChinaHat.ВЛ1, ChinaHat.ВЛЬ);
   private static final double ВЬ = 20.0;
   @SerializedName("г")
   private final SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("ш")  MultiChoiceSetting displayTargetsSetting;
   private final @com.google.gson.annotations.SerializedName("Яе")  FloatSetting radiusSetting;
   private final @com.google.gson.annotations.SerializedName("ЯА")  FloatSetting heightSetting;
   private final @com.google.gson.annotations.SerializedName("Я4")  FloatSetting offsetSetting;
   private final @com.google.gson.annotations.SerializedName("Яп")  FloatSetting speedSetting;
   private final @com.google.gson.annotations.SerializedName("ЯД")  FloatSetting opacitySetting;
   private final @com.google.gson.annotations.SerializedName("ЯЪ")  FloatSetting sizeSetting;
   private final @com.google.gson.annotations.SerializedName("Ят")  FloatSetting trailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("Яа")  FloatSetting colorOffsetSetting;
   private final @com.google.gson.annotations.SerializedName("с0")  BooleanSetting alwaysShowSetting;
   private final @com.google.gson.annotations.SerializedName("сх")  BooleanSetting useThemeColorSetting;
   private final @com.google.gson.annotations.SerializedName("必")  ColorSetting customColorSetting;
   private final @com.google.gson.annotations.SerializedName("сф")  RenderPipeline hatBillboardPipeline;
   private final @com.google.gson.annotations.SerializedName("си")  RenderPipeline ghostHatPipeline;
   private static final BlendFunction А_at_d5ba4d34 = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE);
   private final @com.google.gson.annotations.SerializedName("с9")  RenderPipeline ghostTrailBasePipeline;
   private final @com.google.gson.annotations.SerializedName("со")  RenderPipeline ghostTrailGlowPipeline;
   private final @com.google.gson.annotations.SerializedName("с5")  Map<Integer, LinkedList<Vec3>[]> ghostTrailHistoryByEntityId;
   @SerializedName("п")
   private final Vector4f transformedPositionScratch;
   private final @com.google.gson.annotations.SerializedName("И")  Matrix4f transformScratch;
   private final @com.google.gson.annotations.SerializedName("ь")  Matrix4f inverseViewScratch;
   private final @com.google.gson.annotations.SerializedName("В")  Matrix3f normalMatrixScratch;
   private final @com.google.gson.annotations.SerializedName("Д")  Vector4f cameraRightVector;
   private final @com.google.gson.annotations.SerializedName("Ъ")  Vector4f cameraUpVector;
   private @com.google.gson.annotations.SerializedName("ВЗ")  long animationStartMillis;
   private final @com.google.gson.annotations.SerializedName("ВК")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private @com.google.gson.annotations.SerializedName("АБ")  float basisRightX;
   private @com.google.gson.annotations.SerializedName("АС")  float basisRightY;
   private @com.google.gson.annotations.SerializedName("Аз")  float basisRightZ;
   private @com.google.gson.annotations.SerializedName("Ац")  float basisUpX;
   private @com.google.gson.annotations.SerializedName("Ая")  float basisUpY;
   @SerializedName("4с")
   private float basisUpZ;
   private static final String Ва6 = "ChinaHat";
   private static final String Ва_ = "Конус над головой игрока";
   private static final String ВаШ = "Режим";
   private static final String Ваы = "Обычный";
   private static final String Вай = "Градиент";
   private static final String ВаЖ = "Контур";
   private static final String Вам = "Cyber";
   private static final String ВаК = "Призраки 1";
   private static final String ВаI = "Призраки 2";
   private static final String ВаТ = "Призраки 3";
   private static final String ВаО = "Отображать";
   private static final String ВаФ = "Себя";
   private static final String ВаЙ = "Друзей";
   private static final String Вак = "Игроков";
   private static final String Ваг = "Радиус";
   private static final float Ва0 = 0.65F;
   private static final float Вах = 0.3F;
   private static final float ВаУ = 1.4F;
   private static final float ВаЗ = 0.05F;
   private static final String ВаЕ = "Высота";
   private static final float ВаП = 0.35F;
   private static final float Ва西 = 0.1F;
   private static final float Вад = 0.9F;
   private static final float ВаБ = 0.05F;
   private static final String ВаС = "Отступ";
   private static final float Ваз = 0.08F;
   private static final float Вац = -0.2F;
   private static final float Вая = 0.4F;
   private static final float ВЮс = 0.01F;
   private static final String ВЮВ = "Скорость";
   private static final float ВЮЯ = 45.0F;
   private static final float ВЮХ = 180.0F;
   private static final String ВЮе = "Прозрачность";
   private static final float ВЮА = 0.75F;
   private static final float ВЮ4 = 0.15F;
   private static final float ВЮп = 0.05F;
   private static final String ВЮД = "Размер";
   private static final float ВЮЪ = 0.3F;
   private static final float ВЮт = 0.05F;
   private static final float ВЮа = 0.01F;
   private static final String ВЮЮ = "Длина трейла";
   private static final float ВЮЧ = 25.0F;
   private static final float ВЮЛ = 5.0F;
   private static final float ВЮ诶 = 100.0F;
   private static final String ВЮ1 = "Смещение цвета";
   private static final float ВЮЬ = 360.0F;
   private static final String ВЮ衣 = "Всегда показывать";
   private static final String ВЮш = "Авто цвет под тему";
   private static final String ВЮИ = "Цвет";
   private static final int ВЮь = -38006;
   private static final String ВЮж = "rose/china_hat";
   private static final String ВЮ弟 = "target_esp_crystal";
   private static final String ВЮб = "target_esp_crystal";
   private static final String ВЮЫ = "rose/china_hat_ghost3";
   private static final String ВЮl = "dead_chams";
   private static final String ВЮ8 = "dead_chams";
   private static final String ВЮ5 = "Sampler0";
   private static final String ВЮв = "rose/pipeline/chinahat_cyber_glass";
   private static final String ВЮъ = "chinahat";
   private static final String ВЮМ = "chinahat";
   private static final String ВЮН = "rose/pipeline/chinahat_cyber_bloom";
   private static final String ВЮн = "chinahat";
   private static final String ВЮЦ = "chinahat";
   private static final long ВЮч = -1L;
   private static final String ВЮЭ = "Призраки";
   private static final String ВЮ必 = "Призраки 1";
   private static final String ВЮщ = "Cyber";
   private static final long ВЮГ = -1L;
   private static final String ВЮл = "Призраки 3";
   private static final long ВЮэ = -1L;
   private static final float ВЮю = 0.05F;
   private static final String ВЮЩ = "Призраки 2";
   private static final String ВЮу = "Призраки 1";
   private static final String ВЮ2 = "Контур";
   private static final int ВЮф = 9347071;
   private static final int ВЮи = 6061823;
   private static final float ВЮ9 = 0.9375F;
   private static final String ВЮо = "chinaHatCyberGlass";
   private static final String ВЮO = "chinaHatCyberBloom";
   private static final double ВЮ7 = Math.PI;
   private static final double ВЮР = 20.0;
   private static final double ВЮ3 = 1000.0;
   private static final float ВЮр = 10.0F;
   private static final float ВЮ6 = 0.3F;
   private static final float ВЮ_ = 1.6F;
   private static final float ВЮШ = 0.5F;
   private static final float ВЮы = 0.26F;
   private static final float ВЮй = 0.42F;
   private static final float ВЮЖ = (float) (Math.PI * 2);
   private static final float ВЮм = 6.0F;
   private static final float ВЮК = 4.0F;
   private static final float ВЮI = 1.9F;
   private static final float ВЮТ = 0.35F;
   private static final int ВЮО = -15394268;
   private static final float ВЮФ = 0.45F;
   private static final float ВЮЙ = 1.25F;
   private static final float ВЮк = 0.5F;
   private static final float ВЮг = 0.22F;
   private static final float ВЮ0 = 0.42F;
   private static final float ВЮх = 0.38F;
   private static final float ВЮУ = 0.22F;
   private static final float ВЮЗ = 0.44F;
   private static final float ВЮЕ = 0.02F;
   private static final float ВЮП = 0.42F;
   private static final float ВЮ西 = 0.18F;
   private static final float ВЮд = 0.36F;
   private static final float ВЮБ = 0.4F;
   private static final float ВЮС = 0.2F;
   private static final float ВЮз = 0.28F;
   private static final float ВЮц = 0.06F;
   private static final float ВЮя = 0.48F;
   private static final float ВЧс = 0.34F;
   private static final float ВЧВ = 0.06F;
   private static final float ВЧЯ = 0.48F;
   private static final float ВЧХ = 0.2F;
   private static final float ВЧе = 0.28F;
   private static final float ВЧА = 0.36F;
   private static final float ВЧ4 = 0.4F;
   private static final float ВЧп = 0.42F;
   private static final float ВЧД = 0.18F;
   private static final float ВЧЪ = 0.44F;
   private static final float ВЧт = 0.02F;
   private static final float ВЧа = 0.38F;
   private static final float ВЧЮ = 0.22F;
   private static final float ВЧЧ = 0.22F;
   private static final float ВЧЛ = 0.42F;
   private static final float ВЧ诶 = 0.05F;
   private static final float ВЧ1 = 0.13F;
   private static final float ВЧЬ = 0.14F;
   private static final float ВЧ衣 = 0.07F;
   private static final float ВЧш = 0.11F;
   private static final float ВЧИ = 0.13F;
   private static final float ВЧь = 0.14F;
   private static final float ВЧж = 0.07F;
   private static final float ВЧ弟 = 0.11F;
   private static final float ВЧб = 1.0E-5F;
   private static final float ВЧЫ = 1.0E-5F;
   private static final float ВЧl = 1.0E-5F;
   private static final double ВЧ8 = 0.004;
   private static final float ВЧ5 = 45.0F;
   private static final double ВЧв = Math.PI * 2;
   private static final double ВЧъ = -1.0;
   private static final double ВЧМ = 0.8;
   private static final double ВЧН = 0.04;
   private static final float ВЧн = 0.26F;
   private static final float ВЧЦ = 0.16F;
   private static final float ВЧч = 0.35F;
   private static final double ВЧЭ = 1.8;
   private static final float ВЧ必 = 0.12F;
   private static final float ВЧщ = 1.0E-5F;
   private static final float ВЧГ = (float) (Math.PI * 2);
   private static final float ВЧл = 8.0F;
   private static final float ВЧэ = (float) (Math.PI * 2);
   private static final float ВЧю = 8.0F;
   private static final float ВЧЩ = 0.5F;
   private static final float ВЧу = 100.0F;
   private static final float ВЧ2 = 90.0F;
   private static final float ВЧф = 90.0F;
   private static final float ВЧи = 90.0F;
   private static final float ВЧ9 = 0.8333334F;
   private static final float ВЧо = 0.12F;
   private static final float ВЧO = -1.0F;
   private static final float ВЧ7 = 0.3F;
   private static final float ВЧР = -1.0F;
   private static final float ВЧ3 = 0.3F;
   private static final String ВЧр = "Градиент";
   private static final float ВЧ6 = 0.95F;
   private static final float ВЧ_ = 0.9F;
   private static final float ВЧШ = (float) (Math.PI * 2);
   private static final float ВЧы = 64.0F;
   private static final float ВЧй = (float) (Math.PI * 2);
   private static final float ВЧЖ = 64.0F;
   private static final float ВЧм = 0.01F;
   private static final float ВЧК = 0.030000001F;
   private static final float ВЧI = 0.012F;
   private static final float ВЧТ = (float) (Math.PI * 2);
   private static final float ВЧО = 64.0F;
   private static final float ВЧФ = (float) (Math.PI * 2);
   private static final float ВЧЙ = 64.0F;
   private static final float ВЧк = 0.012F;
   private static final float ВЧг = 0.012F;
   private static final float ВЧ0 = 0.012F;
   private static final float ВЧх = 0.012F;
   private static final String ВЧУ = "Градиент";
   private static final String ВЧЗ = "Призраки 1";
   private static final String ВЧЕ = "Призраки 2";
   private static final String ВЧП = "Призраки 3";
   private static final String ВЧ西 = "Себя";
   private static final String ВЧд = "Призраки 3";
   private static final String ВЧБ = "Друзей";
   private static final String ВЧС = "Игроков";
   private static final String ВЧз = "chinaHat";
   private static final String ВЧц = "Sampler0";
   private static final long ВЧя = -1L;
   private static final String ВЛс = "chinaHatGhost3";
   private static final String ВЛВ = "chinaHatGhost3Vertices";
   private static final String ВЛЯ = "Градиент";
   private static final String ВЛХ = "Cyber";
   private static final String ВЛе = "Градиент";
   private static final String ВЛА = "Cyber";
   private static final String ВЛ4 = "Призраки 3";
   private static final String ВЛп = "Призраки 3";
   private static final String ВЛД = "Призраки 3";
   private static final String ВЛЪ = "Призраки 3";
   private static final String ВЛт = "Cyber";
   private static final String ВЛа = "Cyber";
   private static final String ВЛЮ = "Cyber";
   private static final String ВЛЧ = "Призраки 3";
   private static final String ВЛЛ = "Cyber";
   private static final String ВЛ诶 = "Cyber";
   private static final String ВЛ1 = "rose";
   private static final String ВЛЬ = "images/fireflies/bloom.png";

   private void и(BufferBuilder var1, int var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         if (var2 == 0) {
            var3.close();
         } else {
            GpuBuffer var4 = null;

            try {
               DrawState var5 = var3.drawState();
               int var6 = var5.indexCount();
               AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
               GpuBuffer var8 = var7.getBuffer(var6);
               IndexType var9 = var7.type();
               var4 = RenderSystem.getDevice().createBuffer(() -> ВЛВ, 32, var3.vertexBuffer());
               RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var12 = MINECRAFT_CLIENT.getTextureManager().getTexture(м);

               try (RenderPass var13 = var11.createRenderPass(
                     () -> ВЛс, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var13);
                  var13.setPipeline(this.ghostHatPipeline);
                  var13.bindTexture(ВЧц, var12.getTextureView(), var12.getSampler());
                  var13.setVertexBuffer(0, var4.slice());
                  var13.setIndexBuffer(var8, var9);
                  var13.drawIndexed(var6, 1, 0, 0, 0);
               }
            } finally {
               if (var4 != null) {
                  var4.close();
               }

               var3.close();
            }
         }
      }
   }

   private void Э(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, Vector4f var6) {
      var1.addVertex(var2, var3, var4, var5).setUv(0.0F, 0.0F).setColor(var6.x, var6.y, var6.z, var6.w);
   }

   private float З(Player var1, ChinaHat.PlayerHatPose var2, boolean var3) {
      boolean var4 = var2.xRot != 0.0F || var2.swimShift > 0.0F || var1.isFallFlying() || var1.isVisuallySwimming() || var1.isVisuallyCrawling();
      float var5 = (var4 ? var1.getDimensions(Pose.STANDING).height() : var1.getBbHeight()) * var2.scale;
      return var3 ? var5 * ВЧ9 - ВЧо : var5 + this.offsetSetting.getValue();
   }

   private int З(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, int var7, float var8) {
      Vector4f var9 = ColorMath.toRgbaVector(ColorMath.withAlpha(var7, var8));
      Vector4f var10 = ColorMath.toRgbaVector(ColorMath.withAlpha(var7, 0.0F));
      int var11 = 0;

      for (int var12 = 0; var12 < 8; var12++) {
         float var13 = ВЧГ * var12 / ВЧл;
         float var14 = ВЧэ * (var12 + 1) / ВЧю;
         float var15 = Mth.cos(var13);
         float var16 = Mth.sin(var13);
         float var17 = Mth.cos(var14);
         float var18 = Mth.sin(var14);
         this.Э(var1, var2, var3, var4, var5, var9);
         this.Э(
            var1,
            var2,
            var3 + this.cameraRightVector.x * var15 * var6 + this.cameraUpVector.x * var16 * var6,
            var4 + this.cameraRightVector.y * var15 * var6 + this.cameraUpVector.y * var16 * var6,
            var5 + this.cameraRightVector.z * var15 * var6 + this.cameraUpVector.z * var16 * var6,
            var10
         );
         this.Э(
            var1,
            var2,
            var3 + this.cameraRightVector.x * var17 * var6 + this.cameraUpVector.x * var18 * var6,
            var4 + this.cameraRightVector.y * var17 * var6 + this.cameraUpVector.y * var18 * var6,
            var5 + this.cameraRightVector.z * var17 * var6 + this.cameraUpVector.z * var18 * var6,
            var10
         );
         this.Э(var1, var2, var3, var4, var5, var9);
         var11 += 4;
      }

      return var11;
   }

   private float Э(PoseStack var1, Vec3 var2, Player var3, float var4, float var5) {
      ChinaHat.PlayerHatPose var6 = this.З_0a94a7f2(var3, var4);
      var1.pushPose();
      this.З(var1, var2, var3, var4, var6, var5, this.З(var3, var6, true));
      return var6.scale;
   }

   private static boolean З(Vector4f var0) {
      float var1 = Mth.sqrt(var0.x * var0.x + var0.y * var0.y + var0.z * var0.z);
      if (!(var1 < ВЧщ) && Float.isFinite(var1)) {
         var0.x /= var1;
         var0.y /= var1;
         var0.z /= var1;
         return true;
      } else {
         return false;
      }
   }

   private float З(PoseStack var1, Vec3 var2, Player var3, float var4, float var5) {
      ChinaHat.PlayerHatPose var6 = this.З_0a94a7f2(var3, var4);
      var1.pushPose();
      this.З(var1, var2, var3, var4, var6, var5, this.З(var3, var6, false));
      return var6.scale;
   }

   private int З(
      BufferBuilder var1,
      Matrix4f var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      float var15,
      Vector4f var16
   ) {
      this.З(var1, var2, var3, var4, var5, var6 - var8, var7 - var9, var10, var11, var12, var13, var14, var15, var16);
      this.З(var1, var2, var3, var4, var5, var6 - var8, var7 + var9, var10, var11, var12, var13, var14, var15, var16);
      this.З(var1, var2, var3, var4, var5, var6 + var8, var7 + var9, var10, var11, var12, var13, var14, var15, var16);
      this.З(var1, var2, var3, var4, var5, var6 + var8, var7 - var9, var10, var11, var12, var13, var14, var15, var16);
      return 4;
   }

   private ChinaHat.PlayerHatPose З_0a94a7f2(Player var1, float var2) {
      float var3 = Mth.rotLerp(var2, var1.yBodyRotO, var1.yBodyRot);
      float var4 = Mth.lerp(var2, var1.xRotO, var1.getXRot());
      float var5 = var1.getSwimAmount(var2);
      float var6 = var1.isBaby() ? ВЧЩ : 1.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      if (var1.isFallFlying()) {
         float var9 = var1.getFallFlyingTicks() + var2;
         float var10 = Mth.clamp(var9 * var9 / ВЧу, 0.0F, 1.0F);
         if (!var1.isAutoSpinAttack()) {
            var7 = var10 * (var4 + ВЧ2);
         }
      } else if (var5 > 0.0F) {
         float var11 = var1.isInWater() ? var4 + ВЧф : ВЧи;
         var7 = Mth.lerp(var5, 0.0F, var11);
         if (var1.isVisuallySwimming()) {
            var8 = var5;
         }
      }

      return new ChinaHat.PlayerHatPose(var3, var7, var8, var6);
   }

   private int З(BufferBuilder var1, PoseStack var2, Vec3 var3, Player var4, float var5) {
      float var6 = this.З(var2, var3, var4, var5, 0.0F);
      Matrix4f var7 = var2.last().pose();
      this.inverseViewScratch.set(var7).invert();
      this.cameraRightVector.set(1.0F, 0.0F, 0.0F, 0.0F);
      this.inverseViewScratch.transform(this.cameraRightVector);
      this.cameraUpVector.set(0.0F, 1.0F, 0.0F, 0.0F);
      this.inverseViewScratch.transform(this.cameraUpVector);
      if (З(this.cameraRightVector) && З(this.cameraUpVector)) {
         float var8 = this.radiusSetting.getValue() * var6;
         float var9 = this.heightSetting.getValue() * var6;
         float var10 = this.opacitySetting.getValue();
         int var11 = this.Л(0);
         int var12 = this.useThemeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.Л(48);
         double var13 = System.currentTimeMillis() * ВЧ8 * (this.speedSetting.getValue() / ВЧ5);
         LinkedList<Vec3>[] var15 = this.З(var4.getId());
         int var16 = 0;

         for (int var17 = 0; var17 < var15.length; var17++) {
            double var18 = var17 * (ВЧв / var15.length);
            double var20 = Math.sin(var13 + var18) * var8;
            double var22 = Math.cos(var13 + var18) * var8;
            double var24 = var17 % 2 == 0 ? 1.0 : ВЧъ;
            double var26 = Math.sin((var13 + var18) * ВЧМ) * var24 * var9;
            LinkedList<Vec3> var28 = var15[var17];
            Vec3 var29 = new Vec3(var20, var26, var22);
            if (!var28.isEmpty()) {
               Vec3 var30 = (Vec3)var28.getFirst();
               double var31 = var30.distanceTo(var29);
               int var33 = Mth.clamp((int)Math.ceil(var31 / ВЧН), 1, 6);

               for (int var34 = 1; var34 <= var33; var34++) {
                  var28.addFirst(var30.lerp(var29, (double)var34 / var33));
               }
            } else {
               var28.addFirst(var29);
            }

            while (var28.size() > 24) {
               var28.removeLast();
            }

            int var42 = 0;
            int var43 = var28.size();

            for (Vec3 var44 : var28) {
               float var45 = 1.0F - (float)var42 / var43;
               float var35 = (float)var44.x;
               float var36 = (float)var44.y;
               float var37 = (float)var44.z;
               int var38 = ColorMath.lerpArgb(var11, var12, var45);
               float var39 = var42 == 0 ? ВЧн : ВЧЦ;
               float var40 = var39 * var45 * Math.max(var10, ВЧч);
               float var41 = (float)(Math.pow(var45, ВЧЭ) * var10);
               var16 += this.З(var1, var7, var35, var36, var37, var40, var38, var41);
               if (var42 == 0) {
                  var16 += this.З(var1, var7, var35, var36, var37, ВЧ必, -1, var10);
               }

               var42++;
            }
         }

         var2.popPose();
         return var16;
      } else {
         var2.popPose();
         return 0;
      }
   }

   private int З(
      BufferBuilder var1,
      Matrix4f var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      Vector4f var14
   ) {
      this.Э(var1, var2, var3 + var8 * var6 + var11 * var7, var4 + var9 * var6 + var12 * var7, var5 + var10 * var6 + var13 * var7, var14);
      return 1;
   }

   private boolean З(float var1, float var2, float var3, float var4, float var5) {
      float var6 = this.transformedPositionScratch.x - var1;
      float var7 = this.transformedPositionScratch.y - var2;
      float var8 = this.transformedPositionScratch.z - var3;
      float var9 = Mth.sqrt(var6 * var6 + var7 * var7 + var8 * var8);
      if (var9 < ВЧб) {
         return false;
      }

      var6 /= var9;
      var7 /= var9;
      var8 /= var9;
      float var10 = var8;
      float var11 = 0.0F;
      float var12 = -var6;
      float var13 = Mth.sqrt(var10 * var10 + var12 * var12);
      if (var13 < ВЧЫ) {
         var10 = var4;
         var11 = 0.0F;
         var12 = 0.0F;
      } else {
         float var14 = var4 / var13;
         var10 *= var14;
         var12 *= var14;
      }

      float var24 = var7 * var12 - var8 * var11;
      float var15 = var8 * var10 - var6 * var12;
      float var16 = var6 * var11 - var7 * var10;
      float var17 = Mth.sqrt(var24 * var24 + var15 * var15 + var16 * var16);
      if (var17 < ВЧl) {
         return false;
      }

      float var18 = var5 / var17;
      this.basisRightX = var10;
      this.basisRightY = var11;
      this.basisRightZ = var12;
      this.basisUpX = var24 * var18;
      this.basisUpY = var15 * var18;
      this.basisUpZ = var16 * var18;
      return true;
   }

   public ChinaHat() {
      super(Ва6, Ва_, ModuleCategory.RENDER);
      this.modeSetting = new SingleChoiceSetting(
         ВаШ,
         new ChoiceOption(Ваы),
         new ChoiceOption(Вай),
         new ChoiceOption(ВаЖ),
         new ChoiceOption(Вам),
         new ChoiceOption(ВаК),
         new ChoiceOption(ВаI),
         new ChoiceOption(ВаТ)
      );
      this.displayTargetsSetting = new MultiChoiceSetting(ВаО, new ChoiceOption(ВаФ, true), new ChoiceOption(ВаЙ, false), new ChoiceOption(Вак, false));
      this.radiusSetting = new FloatSetting(Ваг, Ва0, Вах, ВаУ).withStep(ВаЗ).visibleWhen(() -> !this.modeSetting.isSelected(ВЛ诶));
      this.heightSetting = new FloatSetting(ВаЕ, ВаП, Ва西, Вад).withStep(ВаБ).visibleWhen(() -> !this.modeSetting.isSelected(ВЛЧ) && !this.modeSetting.isSelected(ВЛЛ));
      this.offsetSetting = new FloatSetting(ВаС, Ваз, Вац, Вая).withStep(ВЮс).visibleWhen(() -> !this.modeSetting.isSelected(ВЛЮ));
      this.speedSetting = new FloatSetting(ВЮВ, ВЮЯ, 0.0F, ВЮХ).withStep(1.0F).visibleWhen(() -> !this.modeSetting.isSelected(ВЛа));
      this.opacitySetting = new FloatSetting(ВЮе, ВЮА, ВЮ4, 1.0F).withStep(ВЮп).visibleWhen(() -> !this.modeSetting.isSelected(ВЛт));
      this.sizeSetting = new FloatSetting(ВЮД, ВЮЪ, ВЮт, 1.0F).withStep(ВЮа).visibleWhen(() -> this.modeSetting.isSelected(ВЛЪ));
      this.trailLengthSetting = new FloatSetting(ВЮЮ, ВЮЧ, ВЮЛ, ВЮ诶).withStep(1.0F).visibleWhen(() -> this.modeSetting.isSelected(ВЛД));
      this.colorOffsetSetting = new FloatSetting(ВЮ1, 0.0F, 0.0F, ВЮЬ).withStep(1.0F).visibleWhen(() -> this.modeSetting.isSelected(ВЛп));
      this.alwaysShowSetting = new BooleanSetting(ВЮ衣, false).visibleWhen(() -> this.modeSetting.isSelected(ВЛ4));
      this.useThemeColorSetting = new BooleanSetting(ВЮш, true).visibleWhen(() -> !this.modeSetting.isSelected(ВЛе) && !this.modeSetting.isSelected(ВЛА));
      this.customColorSetting = new ColorSetting(ВЮИ, false, ВЮь).visibleWhen(() -> !this.useThemeColorSetting.getValue() && !this.modeSetting.isSelected(ВЛЯ) && !this.modeSetting.isSelected(ВЛХ));
      this.hatBillboardPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВЮж)
         .withVertexShader(ClientResourceIds.getCoreId(ВЮ弟))
         .withFragmentShader(ClientResourceIds.getCoreId(ВЮб))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.ghostHatPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВЮЫ)
         .withVertexShader(ClientResourceIds.getCoreId(ВЮl))
         .withFragmentShader(ClientResourceIds.getCoreId(ВЮ8))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ВЮ5)
         .build();
      this.ghostTrailBasePipeline = RenderPipelinesAccessor.register(
         RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(ВЮв)
            .withVertexShader(ClientResourceIds.getCoreId(ВЮъ))
            .withFragmentShader(ClientResourceIds.getCoreId(ВЮМ))
            .withDepthTest(CompareOp.ALWAYS_PASS)
            .withDepthWrite(false)
            .withCull(false)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, PrimitiveTopology.TRIANGLES)
            .build()
      );
      this.ghostTrailGlowPipeline = RenderPipelinesAccessor.register(
         RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(ВЮН)
            .withVertexShader(ClientResourceIds.getCoreId(ВЮн))
            .withFragmentShader(ClientResourceIds.getCoreId(ВЮЦ))
            .withDepthTest(CompareOp.ALWAYS_PASS)
            .withDepthWrite(false)
            .withCull(false)
            .withBlend(А_at_d5ba4d34)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, PrimitiveTopology.TRIANGLES)
            .build()
      );
      this.ghostTrailHistoryByEntityId = new HashMap<>();
      this.transformedPositionScratch = new Vector4f();
      this.transformScratch = new Matrix4f();
      this.inverseViewScratch = new Matrix4f();
      this.normalMatrixScratch = new Matrix3f();
      this.cameraRightVector = new Vector4f();
      this.cameraUpVector = new Vector4f();
      this.animationStartMillis = ВЮч;
      this.worldEffectsListener = this::method_3;
      this.addSettings(this.modeSetting, this.displayTargetsSetting, this.radiusSetting, this.heightSetting, this.offsetSetting, this.speedSetting, this.opacitySetting, this.sizeSetting, this.trailLengthSetting, this.colorOffsetSetting, this.alwaysShowSetting, this.useThemeColorSetting, this.customColorSetting);
   }

   private int З(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, int var7) {
      Vector4f var8 = ColorMath.toRgbaVector(var7);
      Vector4f var9 = ColorMath.toRgbaVector(ColorMath.withAlpha(ВЮО, Math.max(ВЮФ, var8.w)));
      float var10 = var6;
      float var11 = var6 * ВЮЙ;
      float[] var12 = new float[]{
         0.0F,
         var11 * ВЮк,
         var10 * ВЮг,
         var11 * ВЮ0,
         var10 * ВЮх,
         var11 * ВЮУ,
         var10 * ВЮЗ,
         var11 * ВЮЕ,
         var10 * ВЮП,
         -var11 * ВЮ西,
         var10 * ВЮд,
         -var11 * ВЮБ,
         var10 * ВЮС,
         -var11 * ВЮз,
         var10 * ВЮц,
         -var11 * ВЮя,
         0.0F,
         -var11 * ВЧс,
         -var10 * ВЧВ,
         -var11 * ВЧЯ,
         -var10 * ВЧХ,
         -var11 * ВЧе,
         -var10 * ВЧА,
         -var11 * ВЧ4,
         -var10 * ВЧп,
         -var11 * ВЧД,
         -var10 * ВЧЪ,
         var11 * ВЧт,
         -var10 * ВЧа,
         var11 * ВЧЮ,
         -var10 * ВЧЧ,
         var11 * ВЧЛ
      };
      float var13 = 0.0F;
      float var14 = 0.0F;
      float var15 = 0.0F;
      float var16 = 0.0F;
      float var17 = 0.0F;
      float var18 = 0.0F;
      if (!this.З(var3, var4, var5, 1.0F, 1.0F)) {
         var13 = 1.0F;
         var17 = 1.0F;
      } else {
         var13 = this.basisRightX;
         var14 = this.basisRightY;
         var15 = this.basisRightZ;
         var16 = this.basisUpX;
         var17 = this.basisUpY;
         var18 = this.basisUpZ;
      }

      int var19 = var12.length / 2;
      int var20 = 0;
      float var21 = 0.0F;
      float var22 = var11 * ВЧ诶;

      for (int var23 = 0; var23 < var19; var23++) {
         int var24 = (var23 + 1) % var19;
         var20 += this.З(var1, var2, var3, var4, var5, var21, var22, var13, var14, var15, var16, var17, var18, var8);
         var20 += this.З(var1, var2, var3, var4, var5, var12[var23 * 2], var12[var23 * 2 + 1], var13, var14, var15, var16, var17, var18, var8);
         var20 += this.З(var1, var2, var3, var4, var5, var12[var24 * 2], var12[var24 * 2 + 1], var13, var14, var15, var16, var17, var18, var8);
         var20 += this.З(var1, var2, var3, var4, var5, var21, var22, var13, var14, var15, var16, var17, var18, var8);
      }

      var20 += this.З(var1, var2, var3, var4, var5, -var10 * ВЧ1, var11 * ВЧЬ, var10 * ВЧ衣, var11 * ВЧш, var13, var14, var15, var16, var17, var18, var9);
      return var20 + this.З(var1, var2, var3, var4, var5, var10 * ВЧИ, var11 * ВЧь, var10 * ВЧж, var11 * ВЧ弟, var13, var14, var15, var16, var17, var18, var9);
   }

   private static Vec3 З_2f96ac28(Player var0, float var1) {
      return new Vec3(Mth.lerp(var1, var0.xo, var0.getX()), Mth.lerp(var1, var0.yo, var0.getY()), Mth.lerp(var1, var0.zo, var0.getZ()));
   }

   @Override
   public void onDisable() {
      this.animationStartMillis = ВЧя;
      this.ghostTrailHistoryByEntityId.clear();
      super.onDisable();
   }

   private int З(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5) {
      int var6 = 0;
      boolean var7 = this.modeSetting.isSelected(ВЧр);
      Vector4f var8 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(0), var5 * (var7 ? ВЧ6 : ВЧ_)));

      for (int var9 = 0; var9 < 64; var9++) {
         float var10 = ВЧШ * var9 / ВЧы;
         float var11 = ВЧй * (var9 + 1) / ВЧЖ;
         float var12 = Mth.cos(var10) * var3;
         float var13 = Mth.sin(var10) * var3;
         float var14 = Mth.cos(var11) * var3;
         float var15 = Mth.sin(var11) * var3;
         Vector4f var16 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(var9), var5));
         Vector4f var17 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(var9 + 1), var5));
         this.Э(var1, var2, 0.0F, var4, 0.0F, var8);
         this.Э(var1, var2, var12, 0.0F, var13, var16);
         this.Э(var1, var2, var14, 0.0F, var15, var17);
         this.Э(var1, var2, 0.0F, var4, 0.0F, var8);
         var6 += 4;
      }

      return var6;
   }

   private void Э(WorldEffectsEvent var1, float var2, Vec3 var3) {
      if (this.animationStartMillis < 0L) {
         this.animationStartMillis = System.currentTimeMillis();
      }

      Camera var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
      float var5 = this.radiusSetting.getValue();
      float var6 = this.sizeSetting.getValue();
      float var7 = this.speedSetting.getValue();
      float var8 = this.opacitySetting.getValue();
      int var9 = this.trailLengthSetting.getValue().intValue();
      float var10 = this.colorOffsetSetting.getValue();
      long var11 = System.currentTimeMillis();
      PoseStack var13 = var1.getPoses();
      BufferBuilder var14 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var15 = 0;

      for (Player var17 : MINECRAFT_CLIENT.level.players()) {
         if (this.Ъ(var17)) {
            ChinaHat.PlayerHatPose var18 = this.З_0a94a7f2(var17, var2);
            this.З(this.transformScratch, var3, var17, var2, var18, 0.0F, this.З(var17, var18, false));
            float var19 = var5 * var18.scale;
            float var20 = var6 * var18.scale;

            for (int var21 = 0; var21 < 2; var21++) {
               double var22 = var21 * ВЮ7;

               for (int var24 = 0; var24 < var9; var24++) {
                  float var25 = (1.0F - (float)var24 / var9) * var8;
                  float var26 = var20 * (1.0F - (float)var24 / var9);
                  double var27 = (var11 - this.animationStartMillis - var24 * ВЮР) / ВЮ3;
                  double var29 = var27 * (var7 / ВЮр) + var22;
                  this.transformedPositionScratch.set((float)(Math.sin(var29) * var19), 0.0F, (float)(Math.cos(var29) * var19), 1.0F);
                  this.transformScratch.transform(this.transformedPositionScratch);
                  float var31 = this.transformedPositionScratch.x;
                  float var32 = this.transformedPositionScratch.y;
                  float var33 = this.transformedPositionScratch.z;
                  int var34 = (int)(var10 + var24 * 30);
                  int var35 = this.useThemeColorSetting.getValue() ? ColorMath.getAnimatedThemeColor(var34) : this.customColorSetting.getValue();
                  int var36 = ColorMath.withAlpha(var35, var25);
                  int var37 = ColorMath.withAlpha(ColorMath.lerpArgb(var35, -1, ВЮ6), var25);
                  var13.pushPose();
                  var13.translate(var31, var32, var33);
                  var13.mulPose(Axis.YP.rotationDegrees(-var4.yRot()));
                  var13.mulPose(Axis.XP.rotationDegrees(var4.xRot()));
                  Matrix4f var38 = var13.last().pose();
                  var15 += З(var14, var38, var26, var36);
                  var15 += З(var14, var38, var26 * ВЮ_, var37);
                  var13.popPose();
               }
            }
         }
      }

      this.и(var14, var15);
   }

   private int Э(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5) {
      int var6 = 0;
      float var7 = Math.max(ВЧм, var3 - ВЧК);
      float var8 = ВЧI;

      for (int var9 = 0; var9 < 64; var9++) {
         float var10 = ВЧТ * var9 / ВЧО;
         float var11 = ВЧФ * (var9 + 1) / ВЧЙ;
         Vector4f var12 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(var9), var5));
         Vector4f var13 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(var9 + 1), var5));
         float var14 = Mth.cos(var10) * var3;
         float var15 = Mth.sin(var10) * var3;
         float var16 = Mth.cos(var11) * var3;
         float var17 = Mth.sin(var11) * var3;
         float var18 = Mth.cos(var10) * var7;
         float var19 = Mth.sin(var10) * var7;
         float var20 = Mth.cos(var11) * var7;
         float var21 = Mth.sin(var11) * var7;
         this.Э(var1, var2, var14, 0.0F, var15, var12);
         this.Э(var1, var2, var16, 0.0F, var17, var13);
         this.Э(var1, var2, var20, 0.0F, var21, var13);
         this.Э(var1, var2, var18, 0.0F, var19, var12);
         var6 += 4;
         if (var9 % 4 == 0) {
            float var22 = -Mth.sin(var10);
            float var23 = Mth.cos(var10);
            Vector4f var24 = ColorMath.toRgbaVector(ColorMath.withAlpha(this.Л(var9), var5));
            this.Э(var1, var2, var22 * var8, var4, var23 * var8, var24);
            this.Э(var1, var2, var14 + var22 * ВЧк, 0.0F, var15 + var23 * ВЧг, var12);
            this.Э(var1, var2, var14 - var22 * ВЧ0, 0.0F, var15 - var23 * ВЧх, var12);
            this.Э(var1, var2, -var22 * var8, var4, -var23 * var8, var24);
            var6 += 4;
         }
      }

      return var6;
   }

   private void method_3(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (ВЮЭ.equals(this.modeSetting.getPendingSelectionName())) {
            this.modeSetting.selectByName(ВЮ必);
         }

         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = var1.getCameraPosition();
         if (this.modeSetting.isSelected(ВЮщ)) {
            this.ghostTrailHistoryByEntityId.clear();
            this.animationStartMillis = ВЮГ;
            this.З(var1, var2, var3);
         } else if (this.modeSetting.isSelected(ВЮл)) {
            this.ghostTrailHistoryByEntityId.clear();
            this.Э(var1, var2, var3);
         } else {
            this.animationStartMillis = ВЮэ;
            float var4 = (float)Math.toRadians(((float)MINECRAFT_CLIENT.level.getGameTime() + var2) * this.speedSetting.getValue() * ВЮю);
            boolean var5 = this.modeSetting.isSelected(ВЮЩ);
            if (!var5) {
               this.ghostTrailHistoryByEntityId.clear();
            }

            BufferBuilder var6 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var7 = 0;
            HashSet var8 = var5 ? new HashSet() : null;
            PoseStack var9 = var1.getPoses();

            for (Player var11 : MINECRAFT_CLIENT.level.players()) {
               if (this.Ъ(var11)) {
                  if (var5) {
                     var8.add(var11.getId());
                     var7 += this.З(var6, var9, var3, var11, var2);
                  } else if (this.modeSetting.isSelected(ВЮу)) {
                     var7 += this.Э(var6, var9, var3, var11, var2, var4);
                  } else {
                     var7 += this.З(var6, var9, var3, var11, var2, var4);
                  }
               }
            }

            if (var8 != null) {
               this.ghostTrailHistoryByEntityId.keySet().removeIf(var1x -> !var8.contains(var1x));
            }

            this.р(var6, var7);
         }
      }
   }

   private int Э(BufferBuilder var1, PoseStack var2, Vec3 var3, Player var4, float var5, float var6) {
      float var7 = this.З(var2, var3, var4, var5, var6);
      this.З(var3, var4, var5, var6);
      Matrix4f var8 = var2.last().pose();
      float var9 = this.radiusSetting.getValue() * var7;
      float var10 = this.heightSetting.getValue() * var7;
      float var11 = this.opacitySetting.getValue();
      float var12 = Mth.clamp(var9 * ВЮШ, ВЮы, ВЮй);
      int var13 = 0;

      for (int var14 = 0; var14 < 6; var14++) {
         float var15 = ВЮЖ * var14 / ВЮм;
         float var16 = Mth.sin(var6 * ВЮК + var14 * ВЮI) * var10 * ВЮТ;
         var13 += this.З(var1, var8, Mth.cos(var15) * var9, var16, Mth.sin(var15) * var9, var12, ColorMath.withAlpha(this.Л(var14 * 8), var11));
      }

      var2.popPose();
      return var13;
   }

   private void З(Vec3 var1, Player var2, float var3, float var4) {
      ChinaHat.PlayerHatPose var5 = this.З_0a94a7f2(var2, var3);
      this.З(this.transformScratch, var1, var2, var3, var5, var4, this.З(var2, var5, false));
      this.transformScratch.invert();
      this.transformedPositionScratch.set(0.0F, 0.0F, 0.0F, 1.0F);
      this.transformScratch.transform(this.transformedPositionScratch);
   }

   private boolean Ъ(Player var1) {
      if (var1 != MINECRAFT_CLIENT.player) {
         return RoseClient.getInstance().getServices().getFriends().О(var1.getName().getString()) ? this.displayTargetsSetting.isSelected(ВЧБ) : this.displayTargetsSetting.isSelected(ВЧС);
      } else if (!this.displayTargetsSetting.isSelected(ВЧ西)) {
         return false;
      } else {
         return !MINECRAFT_CLIENT.options.getCameraType().isFirstPerson() ? true : this.modeSetting.isSelected(ВЧд) && this.alwaysShowSetting.getValue();
      }
   }

   private static int З(BufferBuilder var0, Matrix4f var1, float var2, int var3) {
      Vector4f var4 = ColorMath.toRgbaVector(var3);
      var0.addVertex(var1, -var2, -var2, 0.0F).setUv(0.0F, 0.0F).setColor(var4.x, var4.y, var4.z, var4.w);
      var0.addVertex(var1, -var2, var2, 0.0F).setUv(0.0F, 1.0F).setColor(var4.x, var4.y, var4.z, var4.w);
      var0.addVertex(var1, var2, var2, 0.0F).setUv(1.0F, 1.0F).setColor(var4.x, var4.y, var4.z, var4.w);
      var0.addVertex(var1, var2, -var2, 0.0F).setUv(1.0F, 0.0F).setColor(var4.x, var4.y, var4.z, var4.w);
      return 4;
   }

   private void З(BufferBuilder var1, int var2, RenderPipeline var3, String var4) {
      MeshData var5 = var1.build();
      if (var5 != null) {
         if (var2 == 0) {
            var5.close();
         } else {
            GpuBuffer var6 = null;

            try {
               DrawState var7 = var5.drawState();
               int var8 = var7.indexCount();
               AutoStorageIndexBuffer var9 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
               GpuBuffer var10 = var9.getBuffer(var8);
               IndexType var11 = var9.type();
               var6 = RenderSystem.getDevice().createBuffer(() -> var4 + "Vertices", 32, var5.vertexBuffer());
               RenderTarget var12 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var13 = RenderSystem.getDevice().createCommandEncoder();

               try (RenderPass var14 = var13.createRenderPass(
                     () -> var4, var12.getColorTextureView(), Optional.empty(), var12.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var14);
                  var14.setPipeline(var3);
                  var14.setVertexBuffer(0, var6.slice());
                  var14.setIndexBuffer(var10, var11);
                  var14.drawIndexed(var8, 1, 0, 0, 0);
               }
            } finally {
               if (var6 != null) {
                  var6.close();
               }

               var5.close();
            }
         }
      }
   }

   private LinkedList<Vec3>[] З(int var1) {
      return this.ghostTrailHistoryByEntityId.computeIfAbsent(var1, var0 -> {
         LinkedList[] var1x = new LinkedList[5];

         for (int var2 = 0; var2 < var1x.length; var2++) {
            var1x[var2] = new LinkedList();
         }

         return var1x;
      });
   }

   private void р(BufferBuilder var1, int var2) {
      this.З(var1, var2, this.hatBillboardPipeline, ВЧз);
   }

   private void З(Matrix4f var1, Vec3 var2, Player var3, float var4, ChinaHat.PlayerHatPose var5, float var6, float var7) {
      Vec3 var8 = З_2f96ac28(var3, var4);
      var1.translation((float)(var8.x - var2.x), (float)(var8.y - var2.y), (float)(var8.z - var2.z));
      var1.rotate(Axis.YP.rotationDegrees(-var5.bodyYaw));
      if (var5.xRot != 0.0F) {
         var1.rotate(Axis.XP.rotationDegrees(var5.xRot));
      }

      if (var5.swimShift > 0.0F) {
         var1.translate(0.0F, ВЧР * var5.swimShift * var5.scale, ВЧ3 * var5.swimShift * var5.scale);
      }

      var1.translate(0.0F, var7, 0.0F);
      if (var6 != 0.0F) {
         var1.rotate(Axis.YP.rotation(var6));
      }
   }

   private void З(WorldEffectsEvent var1, float var2, Vec3 var3) {
      try (SharedGeometryBuffer isolatedMeshStorage = new SharedGeometryBuffer(786432)) {
         int var4 = ВЮф;
         int var5 = ВЮи;
         PoseStack var6 = var1.getPoses();
         BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
         BufferBuilder var8 = isolatedMeshStorage.begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
         int var9 = 0;
         int var10 = 0;

         for (Player var12 : MINECRAFT_CLIENT.level.players()) {
            if (this.Ъ(var12)) {
               float var13 = this.Э(var6, var3, var12, var2, 0.0F) * ВЮ9;
               Matrix4f var14 = var6.last().pose();
               this.normalMatrixScratch.set(var6.last().normal());
               var9 += ChinaHatGeometry.method_5(var7, var14, this.normalMatrixScratch, var4, var5, 1.0F, var13, var13);
               var10 += ChinaHatGeometry.т(var8, var14, this.normalMatrixScratch, var4, var5, 1.0F, var13, var13);
               var6.popPose();
            }
         }

         this.З(var7, var9, this.ghostTrailBasePipeline, ВЮо);
         this.З(var8, var10, this.ghostTrailGlowPipeline, ВЮO);
      }
   }

   private int Л(int var1) {
      if (!this.modeSetting.isSelected(ВЧУ) && (!this.method_1В() || !this.useThemeColorSetting.getValue())) {
         return this.useThemeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.customColorSetting.getValue();
      } else {
         return ColorMath.getAnimatedThemeColor(var1 * 3);
      }
   }

   private void З(PoseStack var1, Vec3 var2, Player var3, float var4, ChinaHat.PlayerHatPose var5, float var6, float var7) {
      Vec3 var8 = З_2f96ac28(var3, var4);
      var1.translate(var8.x - var2.x, var8.y - var2.y, var8.z - var2.z);
      var1.mulPose(Axis.YP.rotationDegrees(-var5.bodyYaw));
      if (var5.xRot != 0.0F) {
         var1.mulPose(Axis.XP.rotationDegrees(var5.xRot));
      }

      if (var5.swimShift > 0.0F) {
         var1.translate(0.0F, ВЧO * var5.swimShift * var5.scale, ВЧ7 * var5.swimShift * var5.scale);
      }

      var1.translate(0.0F, var7, 0.0F);
      if (var6 != 0.0F) {
         var1.mulPose(Axis.YP.rotation(var6));
      }
   }

   private int З(BufferBuilder var1, PoseStack var2, Vec3 var3, Player var4, float var5, float var6) {
      float var7 = this.З(var2, var3, var4, var5, var6);
      Matrix4f var8 = var2.last().pose();
      float var9 = this.radiusSetting.getValue() * var7;
      float var10 = this.heightSetting.getValue() * var7;
      float var11 = this.opacitySetting.getValue();
      int var12 = this.modeSetting.isSelected(ВЮ2) ? this.Э(var1, var8, var9, var10, var11) : this.З(var1, var8, var9, var10, var11);
      var2.popPose();
      return var12;
   }

   private boolean method_1В() {
      return this.modeSetting.isSelected(ВЧЗ) || this.modeSetting.isSelected(ВЧЕ) || this.modeSetting.isSelected(ВЧП);
   }

   private record PlayerHatPose(float bodyYaw, float xRot, float swimShift, float scale) {
   }
}
