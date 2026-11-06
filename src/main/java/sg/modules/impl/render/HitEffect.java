package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.modules.impl.misc.Optimization;
import sg.settings.SingleChoiceSetting;

public class HitEffect extends ClientModule {
   private static final Identifier С = Identifier.fromNamespaceAndPath(HitEffect.сжш, HitEffect.сжИ);
   private static final Identifier з = Identifier.fromNamespaceAndPath(HitEffect.сжь, HitEffect.сжж);
   private static final Identifier ц = Identifier.fromNamespaceAndPath(HitEffect.сж弟, HitEffect.сжб);
   private static final long ХЕ = 1600L;
   private static final int Ъе = 32;
   private static final int ЪА = 32;
   private static final int Ъ4 = 32;
   private static final int Ъп = 32;
   private static final HitEffect.MarkerUv[] В = 必_eb266f85();
   private static int ЪД;
   private static final float тС = 1.1F;
   private static final double ЯШ = 0.2F;
   private static final double Яы = 0.12F;
   private static boolean АЮ;
   private final BooleanSetting Яш;
   private final SingleChoiceSetting сЫ;
   private final SingleChoiceSetting сl;
   private final FloatSetting Х西;
   private final FloatSetting Хд;
   private final FloatSetting ХБ;
   private final FloatSetting ХС;
   private final FloatSetting Хз;
   private final FloatSetting Хц;
   private final FloatSetting Хя;
   private final BooleanSetting ЯИ;
   private final BooleanSetting Яь;
   private final BooleanSetting Яж;
   private final BooleanSetting Я弟;
   private final ColorSetting O;
   @SerializedName("7")
   private final ColorSetting field_7;
   private final BooleanSetting Яб;
   private final FloatSetting ес;
   private final List<HitEffect.SpinningMarker> Ве;
   private final List<HitEffect.HitPulse> ВА;
   private final List<HitEffect.TimedEffect> В4;
   private final RenderPipeline Вп;
   private final RenderPipeline ВД;
   private final RenderPipeline ВЪ;
   private final EventListener<AttackEntityEvent> ХТ;
   private final EventListener<WorldEffectsEvent> ХО;
   private static final String сИс = "HitEffect";
   private static final String сИВ = "Пузырь / сфера / маркер при ударе";
   private static final String сИЯ = "Визуал";
   private static final String сИХ = "Режим";
   private static final String сИе = "Пузырь";
   private static final String сИА = "Сфера";
   private static final String сИ4 = "Маркер";
   private static final String сИп = "Пропадание";
   private static final String сИД = "Альфа";
   private static final String сИЪ = "Разлом";
   private static final String сИт = "Сжатие";
   private static final String сИа = "Спираль";
   private static final String сИЮ = "Скорость";
   private static final float сИЧ = 0.25F;
   private static final float сИЛ = 3.0F;
   private static final float сИ诶 = 0.05F;
   private static final String сИ1 = "Длительность (мс)";
   private static final float сИЬ = 450.0F;
   private static final float сИ衣 = 100.0F;
   private static final float сИш = 2000.0F;
   private static final float сИИ = 10.0F;
   private static final String сИь = "Размер";
   private static final float сИж = 1.25F;
   private static final float сИ弟 = 0.3F;
   private static final float сИб = 3.0F;
   private static final float сИЫ = 0.05F;
   private static final String сИl = "Размер маркера";
   private static final float сИ8 = 0.5F;
   private static final float сИ5 = 0.1F;
   private static final float сИв = 0.05F;
   private static final String сИъ = "Время появления";
   private static final float сИМ = 100.0F;
   private static final float сИН = 50.0F;
   private static final float сИн = 500.0F;
   private static final float сИЦ = 10.0F;
   private static final String сИч = "Время показа";
   private static final float сИЭ = 300.0F;
   private static final float сИ必 = 100.0F;
   private static final float сИщ = 1000.0F;
   private static final float сИГ = 50.0F;
   private static final String сИл = "Время исчезновения";
   private static final float сИэ = 200.0F;
   private static final float сИю = 50.0F;
   private static final float сИЩ = 500.0F;
   private static final float сИу = 10.0F;
   private static final String сИ2 = "Масштаб-анимация";
   private static final String сИф = "Свечение";
   private static final String сИи = "Больше эффектов";
   private static final String сИ9 = "Авто цвет под тему";
   private static final String сИо = "Цвет";
   private static final int сИO = -10962689;
   private static final String сИ7 = "Цвет 2";
   private static final int сИР = -12829441;
   private static final String сИ3 = "Звук";
   private static final String сИр = "Громкость";
   private static final float сИ6 = 0.4F;
   private static final float сИ_ = 0.1F;
   private static final float сИШ = 0.05F;
   private static final String сИы = "rose/hit_effect";
   private static final String сИй = "dead_chams";
   private static final String сИЖ = "dead_chams";
   private static final String сИм = "Sampler0";
   private static final String сИК = "rose/hit_marker";
   private static final String сИI = "dead_chams";
   private static final String сИТ = "dead_chams";
   private static final String сИО = "Sampler0";
   private static final String сИФ = "rose/hit_sphere";
   private static final String сИЙ = "hit_sphere";
   private static final String сИк = "hit_sphere";
   private static final String сИг = "HitSphere";
   private static final String сИ0 = "Сфера";
   private static final double сИх = 1.55;
   private static final double сИУ = 2.25;
   private static final String сИЗ = "Маркер";
   private static final double сИЕ = 360.0;
   private static final float сИП = 1.1F;
   private static final double сИ西 = 0.2F;
   private static final double сИд = 0.12F;
   private static final String сИБ = "minecraft";
   private static final String сИС = "entity.player.attack";
   private static final float сИз = 0.8F;
   private static final float сИц = 1100.0F;
   private static final float сИя = 750.0F;
   private static final float сьс = 450.0F;
   private static final float сьВ = 0.05F;
   private static final float сьЯ = 80.0F;
   private static final double сьХ = 1.0E-4;
   private static final double сье = 1.0E-4;
   private static final String сьА = "Сфера";
   private static final String сь4 = "Маркер";
   private static final double сьп = 2.0;
   private static final double сьД = 6.0;
   private static final float сьЪ = 0.01F;
   private static final float сьт = 1600.0F;
   private static final float сьа = 0.22F;
   private static final float сьЮ = 0.225F;
   private static final float сьЧ = 0.4F;
   private static final float сьЛ = 3.0F;
   private static final float сь诶 = 0.02F;
   private static final float сь1 = 1.55F;
   private static final float сьЬ = 1.5F;
   private static final float сь衣 = -210.0F;
   private static final float сьш = 0.5F;
   private static final float сьИ = 0.5F;
   private static final String сьь = "Альфа";
   private static final long сьж = 100000L;
   private static final float сь弟 = 1000.0F;
   private static final float сьб = 0.15F;
   private static final float сьЫ = 0.15F;
   private static final float сьl = 0.15F;
   private static final float сь8 = 0.85F;
   private static final float сь5 = 0.01F;
   private static final float сьв = 0.15F;
   private static final float сьъ = 0.15F;
   private static final float сьМ = 0.85F;
   private static final double сьН = 0.4;
   private static final double сьн = 1.2;
   private static final double сьЦ = 0.5;
   private static final String сьч = "Разлом";
   private static final float сьЭ = 0.15F;
   private static final float сь必 = 0.65F;
   private static final float сьщ = 0.65F;
   private static final float сьГ = 0.35F;
   private static final String сьл = "Сжатие";
   private static final String сьэ = "Спираль";
   private static final double сью = 0.85;
   private static final double сьЩ = 720.0;
   private static final double сьу = 2.0;
   private static final double сь2 = 0.32;
   private static final double сьф = 1.8;
   private static final float сьи = 0.5F;
   private static final float сь9 = 0.5F;
   private static final double сьо = 0.18;
   private static final double сьO = 0.6;
   private static final double сь7 = 0.5;
   private static final float сьР = 0.35F;
   private static final float сь3 = 0.8F;
   private static final long сьр = 3600L;
   private static final float сь6 = 3600.0F;
   private static final float сь_ = 360.0F;
   private static final double сьШ = 1.35;
   private static final double сьы = 0.22;
   private static final double сьй = 60.0;
   private static final double сьЖ = 37.0;
   private static final String сьм = "Разлом";
   private static final String сьК = "Сжатие";
   private static final String сьI = "Спираль";
   private static final float сьТ = 3.0F;
   private static final double сьО = 1.15;
   private static final String сьФ = "Пузырь";
   private static final String сьЙ = "Сфера";
   private static final float ськ = 300.0F;
   private static final float сьг = 0.05F;
   private static final float сь0 = 0.5F;
   private static final float сьх = 0.5F;
   private static final float сьУ = 0.5F;
   private static final String сьЗ = "HitSphere";
   private static final String сьЕ = "Sampler0";
   private static final float сьП = 1.70158F;
   private static final double сь西 = 3.0;
   private static final double сьд = 2.0;
   private static final long сьБ = 48879L;
   private static final float сьС = 0.5F;
   private static final float сьз = 0.65F;
   private static final float сьц = 0.5F;
   private static final float сья = 0.65F;
   private static final float сжс = 4.0F;
   private static final float сжВ = 4.0F;
   private static final float сжЯ = 0.5F;
   private static final String сжХ = "hitEffect";
   private static final String сже = "hitSpheres";
   private static final float сжА = 1600.0F;
   private static final String сж4 = "Пузырь";
   private static final String сжп = "Сфера";
   private static final String сжД = "Сфера";
   private static final String сжЪ = "Маркер";
   private static final String сжт = "Маркер";
   private static final String сжа = "Маркер";
   private static final String сжЮ = "Маркер";
   private static final String сжЧ = "Маркер";
   private static final String сжЛ = "Маркер";
   private static final String сж诶 = "Сфера";
   private static final String сж1 = "Сфера";
   private static final String сжЬ = "Сфера";
   private static final String сж衣 = "Сфера";
   private static final String сжш = "rose";
   private static final String сжИ = "images/hiteffect/bubble.png";
   private static final String сжь = "rose";
   private static final String сжж = "images/particles/bloom.png";
   private static final String сж弟 = "rose";
   private static final String сжб = "images/hiteffect/marker.png";

   public HitEffect() {
      super(сИс, сИВ, ModuleCategory.RENDER);
      this.Яш = new BooleanSetting(сИЯ, true);
      this.сЫ = new SingleChoiceSetting(сИХ, new ChoiceOption(сИе), new ChoiceOption(сИА), new ChoiceOption(сИ4)).visibleWhen(this.Яш::getValue);
      this.сl = new SingleChoiceSetting(сИп, new ChoiceOption(сИД), new ChoiceOption(сИЪ), new ChoiceOption(сИт), new ChoiceOption(сИа))
         .visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сж衣));
      this.Х西 = new FloatSetting(сИЮ, 1.0F, сИЧ, сИЛ).withStep(сИ诶).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжЬ));
      this.Хд = new FloatSetting(сИ1, сИЬ, сИ衣, сИш).withStep(сИИ).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сж1));
      this.ХБ = new FloatSetting(сИь, сИж, сИ弟, сИб).withStep(сИЫ).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сж诶));
      this.ХС = new FloatSetting(сИl, сИ8, сИ5, 2.0F).withStep(сИв).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжЛ));
      this.Хз = new FloatSetting(сИъ, сИМ, сИН, сИн).withStep(сИЦ).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжЧ));
      this.Хц = new FloatSetting(сИч, сИЭ, сИ必, сИщ).withStep(сИГ).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжЮ));
      this.Хя = new FloatSetting(сИл, сИэ, сИю, сИЩ).withStep(сИу).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжа));
      this.ЯИ = new BooleanSetting(сИ2, true).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжт));
      this.Яь = new BooleanSetting(сИф, true).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжЪ));
      this.Яж = new BooleanSetting(сИи, true).visibleWhen(() -> this.Яш.getValue() && this.сЫ.isSelected(сжД));
      this.Я弟 = new BooleanSetting(сИ9, true).visibleWhen(this.Яш::getValue);
      this.O = new ColorSetting(сИо, false, сИO).visibleWhen(() -> this.Яш.getValue() && !this.Я弟.getValue());
      this.field_7 = new ColorSetting(сИ7, false, сИР).visibleWhen(() -> this.Яш.getValue() && !this.Я弟.getValue() && (this.сЫ.isSelected(сж4) || this.сЫ.isSelected(сжп)));
      this.Яб = new BooleanSetting(сИ3, true);
      this.ес = new FloatSetting(сИр, сИ6, сИ_, 1.0F).withStep(сИШ).visibleWhen(this.Яб::getValue);
      this.Ве = new ArrayList<>();
      this.ВА = new ArrayList<>();
      this.В4 = new ArrayList<>();
      this.Вп = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(сИы)
         .withVertexShader(ClientResourceIds.getCoreId(сИй))
         .withFragmentShader(ClientResourceIds.getCoreId(сИЖ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(сИм)
         .build();
      this.ВД = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(сИК)
         .withVertexShader(ClientResourceIds.getCoreId(сИI))
         .withFragmentShader(ClientResourceIds.getCoreId(сИТ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(сИО)
         .build();
      this.ВЪ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(сИФ)
         .withVertexShader(ClientResourceIds.getCoreId(сИЙ))
         .withFragmentShader(ClientResourceIds.getCoreId(сИк))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withUniform(сИг, UniformType.UNIFORM_BUFFER)
         .build();
      this.ХТ = this::ъ;
      this.ХО = this::ф;
      this.addSettings(
         this.Яш,
         this.сЫ,
         this.сl,
         this.Х西,
         this.Хд,
         this.ХБ,
         this.ХС,
         this.Хз,
         this.Хц,
         this.Хя,
         this.ЯИ,
         this.Яь,
         this.Яж,
         this.Я弟,
         this.O,
         this.field_7,
         this.Яб,
         this.ес
      );
   }

   private static float Щ衣(float var0) {
      float var1 = Mth.clamp(var0, 0.0F, 1.0F);
      float var2 = сьП;
      float var3 = var2 + 1.0F;
      return 1.0F + var3 * (float)Math.pow(var1 - 1.0F, сь西) + var2 * (float)Math.pow(var1 - 1.0F, сьд);
   }

   private Vec3 Ы(LivingEntity var1, Vec3 var2) {
      Vec3 var3 = var2.subtract(new Vec3(var1.getX(), var1.getY(), var1.getZ()));
      var3 = new Vec3(var3.x, 0.0, var3.z);
      if (var3.lengthSqr() < сьХ) {
         Vec3 var4 = var1.getLookAngle();
         var3 = new Vec3(var4.x, 0.0, var4.z);
      }

      if (var3.lengthSqr() < сье) {
         var3 = new Vec3(0.0, 0.0, 1.0);
      }

      return var3.normalize();
   }

   private static float Щщ(float var0) {
      return var0 * var0 * var0;
   }

   private void э(WorldEffectsEvent var1) {
      if (!this.Ве.isEmpty()) {
         Camera var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var3 = var2.position();
         PoseStack var4 = var1.getPoses();
         long var5 = System.currentTimeMillis();
         BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var8 = 0;

         for (HitEffect.SpinningMarker var10 : this.Ве) {
            float var11 = (float)(var5 - var10.spawnTime) / сьт;
            if (!(var11 >= 1.0F)) {
               float var12 = Math.max(0.0F, Math.min(1.0F, var11 / сьа));
               float var13 = Math.max(0.0F, Math.min(1.0F, (var11 - сьЮ) / сьЧ));
               float var14 = var12 * var12 * (сьЛ - 2.0F * var12);
               float var15 = 1.0F - var13 * var13;
               float var16 = сь诶 + сь1 * var14 * var15;
               float var17 = 1.0F - var13 * var13 * var13;
               float var18 = (float)(var5 - var10.spawnTime) / сьЬ + var10.spinSeed;
               Vec3 var19 = var10.pos.subtract(var3);
               int var20 = ColorMath.withAlpha(this.р(0), var17);
               var4.pushPose();
               var4.translate(var19.x, var19.y, var19.z);
               var4.mulPose(Axis.YP.rotationDegrees(var10.sideYaw));
               var4.mulPose(Axis.XP.rotationDegrees(сь衣));
               var4.mulPose(Axis.ZP.rotationDegrees(var18));
               float var21 = -var16 * сьш;
               float var22 = -var16 * сьИ;
               Matrix4f var23 = var4.last().pose();
               Vector4f var24 = ColorMath.toRgbaVector(var20);
               var7.addVertex(var23, var21, var22, 0.0F).setUv(0.0F, 0.0F).setColor(var24.x, var24.y, var24.z, var24.w);
               var7.addVertex(var23, var21, var22 + var16, 0.0F).setUv(0.0F, 1.0F).setColor(var24.x, var24.y, var24.z, var24.w);
               var7.addVertex(var23, var21 + var16, var22 + var16, 0.0F).setUv(1.0F, 1.0F).setColor(var24.x, var24.y, var24.z, var24.w);
               var7.addVertex(var23, var21 + var16, var22, 0.0F).setUv(1.0F, 0.0F).setColor(var24.x, var24.y, var24.z, var24.w);
               var8 += 4;
               var4.popPose();
            }
         }

         this.Ц(var7, С, var8);
         this.Ве.removeIf(var2x -> (float)(var5 - var2x.spawnTime) / сжА >= 1.0F);
      }
   }

   public static boolean 必(SoundEvent var0) {
      return !АЮ && var0 != null ? я(var0.location()) : false;
   }

   private static Vec3 Ы(Entity var0, float var1) {
      return new Vec3(Mth.lerp(var1, var0.xo, var0.getX()), Mth.lerp(var1, var0.yo, var0.getY()), Mth.lerp(var1, var0.zo, var0.getZ()));
   }

   private int 必(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, double var6, int var8) {
      double var9 = var6 * сьО;
      Vec3 var11 = var4.scale(var9);
      Vec3 var12 = var5.scale(var9);
      Vec3 var13 = var3.subtract(var11).subtract(var12);
      Vec3 var14 = var3.add(var11).subtract(var12);
      Vec3 var15 = var3.add(var11).add(var12);
      Vec3 var16 = var3.subtract(var11).add(var12);
      Vector4f var17 = ColorMath.toRgbaVector(var8);
      this.я(var1, var2, (float)var13.x, (float)var13.y, (float)var13.z, 0.0F, 1.0F, var17);
      this.я(var1, var2, (float)var14.x, (float)var14.y, (float)var14.z, 1.0F, 1.0F, var17);
      this.я(var1, var2, (float)var15.x, (float)var15.y, (float)var15.z, 1.0F, 0.0F, var17);
      this.я(var1, var2, (float)var16.x, (float)var16.y, (float)var16.z, 0.0F, 0.0F, var17);
      return 4;
   }

   @Override
   public void onDisable() {
      this.Ве.clear();
      this.ВА.clear();
      this.В4.clear();
   }

   private int Ы(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, double var6, int var8) {
      Vec3 var9 = var4.scale(var6);
      Vec3 var10 = var5.scale(var6);
      Vec3 var11 = var3.subtract(var9).subtract(var10);
      Vec3 var12 = var3.add(var9).subtract(var10);
      Vec3 var13 = var3.add(var9).add(var10);
      Vec3 var14 = var3.subtract(var9).add(var10);
      Vector4f var15 = ColorMath.toRgbaVector(var8);
      this.я(var1, var2, (float)var11.x, (float)var11.y, (float)var11.z, 0.0F, 1.0F, var15);
      this.я(var1, var2, (float)var12.x, (float)var12.y, (float)var12.z, 1.0F, 1.0F, var15);
      this.я(var1, var2, (float)var13.x, (float)var13.y, (float)var13.z, 1.0F, 0.0F, var15);
      this.я(var1, var2, (float)var14.x, (float)var14.y, (float)var14.z, 0.0F, 0.0F, var15);
      return 4;
   }

   private void п(WorldEffectsEvent var1) {
      if (!this.ВА.isEmpty()) {
         Camera var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var3 = var2.position();
         Quaternionf var4 = var2.rotation();
         Vector3f var5 = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
         Vector3f var6 = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
         Vec3 var7 = new Vec3(var5.x, var5.y, var5.z);
         Vec3 var8 = new Vec3(var6.x, var6.y, var6.z);
         Matrix4f var9 = var1.getPoses().last().pose();
         long var10 = System.currentTimeMillis();
         String var12 = this.сl.getSelectedOption() == null ? сьь : this.сl.getSelectedOption().getName();
         float var13 = в(var12);
         float var14 = (float)(var10 % сьж) / сь弟;
         BufferBuilder var15 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         ArrayList var16 = new ArrayList(this.ВА.size());
         ArrayList<HitEffect.BoxInstance> var17 = new ArrayList<>(this.ВА.size());
         int var18 = 0;
         Iterator var19 = this.ВА.iterator();

         while (var19.hasNext()) {
            HitEffect.HitPulse var20 = (HitEffect.HitPulse)var19.next();
            float var21 = Math.min(1.0F, (float)(var10 - var20.сЗ) / var20.Хэ);
            if (var21 >= 1.0F) {
               var19.remove();
            } else {
               float var22 = var21 < сьб ? var21 / сьЫ : 1.0F - (var21 - сьl) / сь8;
               var22 = Mth.clamp(var22, 0.0F, 1.0F);
               var22 *= var22;
               if (!(var22 <= сь5)) {
                  float var23 = var21 > сьв ? (var21 - сьъ) / сьМ : 0.0F;
                  double var24 = (сьН + var21 * сьн) * this.ХБ.getValue().floatValue();
                  double var26 = var24 * сьЦ;
                  float var28 = var22;
                  double var29 = var26;
                  boolean var31 = false;
                  float var32 = 0.0F;
                  Vec3 var33 = var7;
                  Vec3 var34 = var8;
                  if (var12.equals(сьч)) {
                     float var35 = Math.min(1.0F, var21 / сьЭ);
                     if (var21 < сь必) {
                        var28 = var35;
                     } else {
                        var32 = (var21 - сьщ) / сьГ;
                        var28 = (1.0F - var32) * (1.0F - var32);
                        var31 = true;
                     }
                  } else if (var23 > 0.0F) {
                     if (var12.equals(сьл)) {
                        var29 = var26 * (1.0 - var23);
                     } else if (var12.equals(сьэ)) {
                        var29 = var26 * (1.0 - var23 * сью);
                        double var70 = var23 * var23 * Math.toRadians(сьЩ);
                        double var37 = Math.cos(var70);
                        double var39 = Math.sin(var70);
                        var33 = var7.scale(var37).add(var8.scale(var39));
                        var34 = var7.scale(-var39).add(var8.scale(var37));
                     }
                  }

                  Vec3 var71 = var20.в.subtract(var3);
                  int var36 = this.р(var20.ХВ * 40);
                  int var72 = 0;
                  if (var31) {
                     double var38 = сьу * var29;
                     double var40 = var29 * сь2;
                     double var42 = var32 * var32 * сьф;
                     Vec3 var44 = new Vec3(0.0, -var42, 0.0);

                     for (HitEffect.MarkerUv var48 : В) {
                        double var49 = var48.cv - сьи;
                        double var51 = var48.cu - сь9;
                        double var53 = var48.spin * сьо * var32;
                        Vec3 var55 = var71.add(var7.scale(var49 * var38 + var53)).add(var8.scale(var51 * var38)).add(var44);
                        double var56 = var40 * (сьO + сь7 * Math.abs(var48.spin));
                        float var58 = var28 * (1.0F - var32 * сьР);
                        var72 += this.必(var15, var9, var55, var33, var34, var56, ColorMath.withAlpha(var36, var58));
                     }
                  } else {
                     var72 += this.必(var15, var9, var71, var33, var34, var29, ColorMath.withAlpha(var36, var28));
                  }

                  int var73 = var72 / 4 * 6;
                  Vector4f var74 = ColorMath.toRgbaVector(var36);

                  try (MemoryStack var75 = MemoryStack.stackPush()) {
                     ByteBuffer var41 = Std140Builder.onStack(var75, 32)
                        .putVec4(var21, var28, var13, var14 + var20.ХВ)
                        .putVec4(var74.x, var74.y, var74.z, var32)
                        .get();
                     var16.add(new HitEffect.DrawBatch(var18, var73, TransientGpuUpload.uploadUniform(var41)));
                  }

                  var18 += var73;
                  if (this.Яж.getValue() && !var31) {
                     var17.add(new HitEffect.BoxInstance(var71, var29, ColorMath.withAlpha(var36, var28 * сь3), var20.ХВ));
                  }
               }
            }
         }

         this.必(var15, var16);
         if (!var17.isEmpty()) {
            BufferBuilder var61 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var62 = 0;
            float var65 = (float)(var10 % сьр) / сь6 * сь_;

            for (HitEffect.BoxInstance var67 : var17) {
               double var25 = var67.halfSize * сьШ;
               double var27 = var67.halfSize * сьы;

               for (int var68 = 0; var68 < 6; var68++) {
                  double var30 = Math.toRadians(var65 + var68 * сьй + var67.seed * сьЖ);
                  Vec3 var69 = var67.center.add(var7.scale(Math.cos(var30) * var25)).add(var8.scale(Math.sin(var30) * var25));
                  var62 += this.Ы(var61, var9, var69, var7, var8, var27, var67.color);
               }
            }

            this.Ц(var61, з, var62);
         }
      }
   }

   private void ф(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!this.Яш.getValue()) {
            this.Ве.clear();
            this.ВА.clear();
            this.В4.clear();
         } else if (!this.Ве.isEmpty() || !this.ВА.isEmpty() || !this.В4.isEmpty()) {
            Vec3 var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            double var3 = Optimization.мн(Double.POSITIVE_INFINITY);
            if (Double.isFinite(var3)) {
               this.Ве.removeIf(var3x -> var3x.pos.distanceToSqr(var2) > var3);
               this.ВА.removeIf(var3x -> var3x.в.distanceToSqr(var2) > var3);
               this.В4.removeIf(var3x -> var3x.Й.distanceToSqr(var2) > var3);
            }

            int var5 = Optimization.method_2(32);
            int var6 = Optimization.method_2(32);
            int var7 = Optimization.method_2(32);

            while (this.Ве.size() > var5) {
               this.Ве.removeFirst();
            }

            while (this.ВА.size() > var6) {
               this.ВА.removeFirst();
            }

            while (this.В4.size() > var7) {
               this.В4.removeFirst();
            }

            if (this.сЫ.isSelected(сьА)) {
               this.Ве.clear();
               this.В4.clear();
               this.п(var1);
            } else if (this.сЫ.isSelected(сь4)) {
               this.Ве.clear();
               this.ВА.clear();
               this.ш(var1);
            } else {
               this.ВА.clear();
               this.В4.clear();
               this.э(var1);
            }
         }
      }
   }

   private void Ц(BufferBuilder var1, Identifier var2, int var3) {
      this.必(var1, var2, var3, this.Вп, true);
   }

   private int Ы(
      BufferBuilder var1, PoseStack var2, double var3, double var5, double var7, float var9, float var10, float var11, float var12, float var13, int var14
   ) {
      return this.必(var1, var2, var3, var5, var7, var9, var10, var11, var12, var13, var14, var14, var14, var14);
   }

   private boolean бш() {
      return MINECRAFT_CLIENT.player == null
         ? false
         : MINECRAFT_CLIENT.player.fallDistance > 0.0
            && !MINECRAFT_CLIENT.player.onGround()
            && !MINECRAFT_CLIENT.player.onClimbable()
            && !MINECRAFT_CLIENT.player.isInWater()
            && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.BLINDNESS)
            && !MINECRAFT_CLIENT.player.isPassenger();
   }

   private static float в(String var0) {
      return switch (var0) {
         case сьм -> 1.0F;
         case сьК -> 2.0F;
         case сьI -> сьТ;
         default -> 0.0F;
      };
   }

   private static float Щт(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   private Vec3 必(Entity var1, Entity var2) {
      Vec3 var3 = new Vec3(var2.getX(), var2.getY() + var2.getBbHeight() / сьп, var2.getZ());
      if (var1 == null) {
         return var3;
      }

      Vec3 var4 = var1.getEyePosition(1.0F);
      Vec3 var5 = var1.getViewVector(1.0F);
      Vec3 var6 = var2.getBoundingBox().getCenter();
      double var7 = Math.max(var4.distanceTo(var6) + 1.0, сьД);
      Vec3 var9 = var4.add(var5.scale(var7));
      return var2.getBoundingBox().clip(var4, var9).orElse(var4.add(var5.scale(var4.distanceTo(var6))));
   }

   public static boolean 必(SoundInstance var0) {
      return !АЮ && var0 != null ? я(var0.getIdentifier()) : false;
   }

   private void 必(BufferBuilder var1, List<HitEffect.DrawBatch> var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         try (var3) {
            if (var2.isEmpty()) {
               return;
            }

            DrawState var5 = var3.drawState();
            AutoStorageIndexBuffer var6 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
            GpuBuffer var7 = var6.getBuffer(var5.indexCount());
            GpuBufferSlice var8 = TransientGpuUpload.uploadVertices(var3.vertexBuffer(), var5.format().getVertexSize());
            RenderTarget var9 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            CommandEncoder var10 = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass var11 = var10.createRenderPass(
                  () -> сже, var9.getColorTextureView(), Optional.empty(), var9.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(var11);
               var11.setPipeline(this.ВЪ);
               var11.setVertexBuffer(0, var8);
               var11.setIndexBuffer(var7, var6.type());

               for (HitEffect.DrawBatch var13 : var2) {
                  var11.setUniform(сьЗ, var13.uniforms);
                  var11.drawIndexed(var13.indexCount, 1, var13.firstIndex, 0, 0);
               }
            }
         }
      }
   }

   private void 必(BufferBuilder var1, Identifier var2, int var3, RenderPipeline var4, boolean var5) {
      MeshData var6 = var1.build();
      if (var6 != null) {
         if (var3 == 0) {
            var6.close();
         } else {
            try {
               DrawState var7 = var6.drawState();
               int var8 = var7.indexCount();
               AutoStorageIndexBuffer var9 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
               GpuBuffer var10 = var9.getBuffer(var8);
               IndexType var11 = var9.type();
               GpuBufferSlice var12 = TransientGpuUpload.uploadVertices(var6.vertexBuffer(), var7.format().getVertexSize());
               RenderTarget var13 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var15 = var5 && var2 != null ? MINECRAFT_CLIENT.getTextureManager().getTexture(var2) : null;

               try (RenderPass var16 = var14.createRenderPass(
                     () -> сжХ, var13.getColorTextureView(), Optional.empty(), var13.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var16);
                  var16.setPipeline(var4);
                  if (var15 != null) {
                     var16.bindTexture(сьЕ, var15.getTextureView(), var15.getSampler());
                  }

                  var16.setVertexBuffer(0, var12);
                  var16.setIndexBuffer(var10, var11);
                  var16.drawIndexed(var8, 1, 0, 0, 0);
               }
            } finally {
               var6.close();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.Ве.clear();
      this.ВА.clear();
      this.В4.clear();
   }

   private float Ыэ() {
      float var1 = MINECRAFT_CLIENT.player == null ? 1.0F : MINECRAFT_CLIENT.player.getAttackStrengthScale(0.0F);
      float var2 = var1 > сИз ? сИц : сИя;
      return Math.max(var2 * (this.Хд.getValue() / сьс) / Math.max(this.Х西.getValue(), сьВ), сьЯ);
   }

   private int р(int var1) {
      if (this.Я弟.getValue()) {
         return ColorMath.getAnimatedThemeColor(var1 / 16);
      } else {
         return !this.сЫ.isSelected(сьФ) && !this.сЫ.isSelected(сьЙ)
            ? this.O.getValue()
            : ColorMath.lerpArgb(this.O.getValue(), this.field_7.getValue(), (Mth.sin((float)System.currentTimeMillis() / ськ + var1 * сьг) + 1.0F) * сь0);
      }
   }

   private static boolean я(Identifier var0) {
      if (var0 != null && сИБ.equals(var0.getNamespace()) && var0.getPath().startsWith(сИС)) {
         try {
            HitEffect var1 = RoseClient.getInstance().getServices().getModules().getHitEffect();
            return var1 != null && var1.isEnabled() && var1.Яб.getValue();
         } catch (Throwable var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   private void я(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, Vector4f var8) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(var8.x, var8.y, var8.z, var8.w);
   }

   private void ш(WorldEffectsEvent var1) {
      if (!this.В4.isEmpty()) {
         long var2 = System.currentTimeMillis();
         Camera var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var5 = var4.position();
         PoseStack var6 = var1.getPoses();
         BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var8 = 0;
         Iterator var9 = this.В4.iterator();

         while (var9.hasNext()) {
            HitEffect.TimedEffect var10 = (HitEffect.TimedEffect)var9.next();
            if (var10.б(var2)) {
               var9.remove();
            } else {
               float var11 = var10.с(var2);
               if (!(var11 <= сьЪ)) {
                  float var12 = this.ХС.getValue();
                  if (this.ЯИ.getValue()) {
                     var12 *= var10.ы(var2);
                  }

                  double var13 = var10.Й.x - var5.x;
                  double var15 = var10.Й.y - var5.y;
                  double var17 = var10.Й.z - var5.z;
                  int var19 = ColorMath.withAlpha(this.р(0), var11);
                  var8 += this.Ы(var7, var6, var13, var15, var17, var12, var12, var4.yRot(), var4.xRot(), 0.0F, var19);
               }
            }
         }

         this.必(var7, ц, var8, this.Яь.getValue() ? this.Вп : this.ВД, true);
      }
   }

   private void СЭ() {
      if (this.Яб.getValue() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getSoundManager() != null) {
         boolean var1 = this.бш();
         float var2 = var1 ? сИП + (float)(Math.random() * сИ西) : 1.0F + (float)(Math.random() * сИд);
         SoundEvent var3 = var1 ? SoundEvents.PLAYER_ATTACK_CRIT : SoundEvents.PLAYER_ATTACK_STRONG;
         АЮ = true;

         try {
            MINECRAFT_CLIENT.getSoundManager()
               .play(
                  new SimpleSoundInstance(var3, SoundSource.PLAYERS, this.ес.getValue(), var2, MINECRAFT_CLIENT.player.getRandom(), MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY(), MINECRAFT_CLIENT.player.getZ())
               );
         } finally {
            АЮ = false;
         }
      }
   }

   private void ъ(AttackEntityEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
            if (var1.getTarget() instanceof LivingEntity var3 && var3.isAlive()) {
               this.СЭ();
               if (this.Яш.getValue()) {
                  if (this.сЫ.isSelected(сИ0)) {
                     float var9 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
                     Vec3 var10 = Ы(var3, var9).add(0.0, var3.getBbHeight() / сИх / (var3.isBaby() ? сИУ : 1.0), 0.0);
                     if (Optimization.method_7(var10.x, var10.y, var10.z)) {
                        this.ВА.add(new HitEffect.HitPulse(var10, this.Ыэ()));

                        while (this.ВА.size() > Optimization.method_2(32)) {
                           this.ВА.removeFirst();
                        }
                     }
                  } else if (this.сЫ.isSelected(сИЗ)) {
                     Vec3 var8 = this.必(var1.getAttacker(), var3);
                     if (Optimization.method_7(var8.x, var8.y, var8.z)) {
                        this.В4
                           .add(
                              new HitEffect.TimedEffect(
                                 var8, System.currentTimeMillis(), this.Хз.getValue().longValue(), this.Хц.getValue().longValue(), this.Хя.getValue().longValue()
                              )
                           );

                        while (this.В4.size() > Optimization.method_2(32)) {
                           this.В4.removeFirst();
                        }
                     }
                  } else {
                     Vec3 var4 = new Vec3(var1.getAttacker().getX(), var1.getAttacker().getY(), var1.getAttacker().getZ());
                     Vec3 var5 = this.Ы(var3, var4);
                     Vec3 var6 = this.必(var1.getAttacker(), var3);
                     if (Optimization.method_7(var6.x, var6.y, var6.z)) {
                        float var7 = (float)Math.toDegrees(Math.atan2(var5.x, var5.z));
                        this.Ве.add(new HitEffect.SpinningMarker(var6, System.currentTimeMillis(), (float)(Math.random() * сИЕ), var7));

                        while (this.Ве.size() > Optimization.method_2(32)) {
                           this.Ве.removeFirst();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private int 必(
      BufferBuilder var1,
      PoseStack var2,
      double var3,
      double var5,
      double var7,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      int var14,
      int var15,
      int var16,
      int var17
   ) {
      float var18 = var9 * сьх;
      float var19 = var10 * сьУ;
      var2.pushPose();
      var2.translate(var3, var5, var7);
      var2.mulPose(Axis.YP.rotationDegrees(-var11));
      var2.mulPose(Axis.XP.rotationDegrees(var12));
      var2.mulPose(Axis.ZP.rotationDegrees(var13));
      Matrix4f var20 = var2.last().pose();
      this.я(var1, var20, -var18, -var19, 0.0F, 0.0F, 1.0F, ColorMath.toRgbaVector(var14));
      this.я(var1, var20, -var18, var19, 0.0F, 0.0F, 0.0F, ColorMath.toRgbaVector(var15));
      this.я(var1, var20, var18, var19, 0.0F, 1.0F, 0.0F, ColorMath.toRgbaVector(var16));
      this.я(var1, var20, var18, -var19, 0.0F, 1.0F, 1.0F, ColorMath.toRgbaVector(var17));
      var2.popPose();
      return 4;
   }

   private static HitEffect.MarkerUv[] 必_eb266f85() {
      byte var0 = 4;
      float[][][] var1 = new float[var0 + 1][var0 + 1][2];
      Random var2 = new Random(сьБ);

      for (int var3 = 0; var3 <= var0; var3++) {
         for (int var4 = 0; var4 <= var0; var4++) {
            float var5 = (float)var3 / var0;
            float var6 = (float)var4 / var0;
            if (var3 > 0 && var3 < var0 && var4 > 0 && var4 < var0) {
               var5 += (var2.nextFloat() - сьС) * (сьз / var0);
               var6 += (var2.nextFloat() - сьц) * (сья / var0);
            }

            var1[var3][var4][0] = var6;
            var1[var3][var4][1] = var5;
         }
      }

      HitEffect.MarkerUv[] var18 = new HitEffect.MarkerUv[var0 * var0];
      int var19 = 0;

      for (int var20 = 0; var20 < var0; var20++) {
         for (int var21 = 0; var21 < var0; var21++) {
            float var7 = var1[var20][var21][0];
            float var8 = var1[var20][var21][1];
            float var9 = var1[var20 + 1][var21][0];
            float var10 = var1[var20 + 1][var21][1];
            float var11 = var1[var20 + 1][var21 + 1][0];
            float var12 = var1[var20 + 1][var21 + 1][1];
            float var13 = var1[var20][var21 + 1][0];
            float var14 = var1[var20][var21 + 1][1];
            float var15 = (var7 + var9 + var11 + var13) / сжс;
            float var16 = (var8 + var10 + var12 + var14) / сжВ;
            float var17 = (var2.nextFloat() - сжЯ) * 2.0F;
            var18[var19++] = new HitEffect.MarkerUv(var15, var16, var17);
         }
      }

      return var18;
   }

   private static final class HitPulse {
      private final Vec3 в;
      private final float Хэ;
      private final int ХВ;
      private final long сЗ = System.currentTimeMillis();

      private HitPulse(Vec3 var1, float var2) {
         this.в = var1;
         this.Хэ = var2;
         this.ХВ = HitEffect.ЪД++;
      }
   }

   private record SpinningMarker(Vec3 pos, long spawnTime, float spinSeed, float sideYaw) {
   }

   private record MarkerUv(float cu, float cv, float spin) {
   }

   private record BoxInstance(Vec3 center, double halfSize, int color, int seed) {
   }

   private record DrawBatch(int firstIndex, int indexCount, GpuBufferSlice uniforms) {
   }

   private static final class TimedEffect {
      private final Vec3 Й;
      private final long ХЬ;
      private final long Х衣;
      private final long Хш;
      private final long ХИ;
      private static final float сЙя = 0.5F;
      private static final float скс = 0.5F;
      private static final float скВ = 0.3F;

      private boolean б(long var1) {
         return var1 - this.ХЬ >= this.Х衣 + this.Хш + this.ХИ;
      }

      private float с(long var1) {
         long var3 = var1 - this.ХЬ;
         if (var3 < this.Х衣) {
            return HitEffect.Щт((float)var3 / (float)Math.max(this.Х衣, 1L));
         }

         if (var3 < this.Х衣 + this.Хш) {
            return 1.0F;
         }

         long var5 = var3 - this.Х衣 - this.Хш;
         float var7 = Math.min(1.0F, (float)var5 / (float)Math.max(this.ХИ, 1L));
         return 1.0F - HitEffect.Щщ(var7);
      }

      private float ы(long var1) {
         long var3 = var1 - this.ХЬ;
         if (var3 < this.Х衣) {
            return сЙя + скс * HitEffect.Щ衣((float)var3 / (float)Math.max(this.Х衣, 1L));
         }

         if (var3 < this.Х衣 + this.Хш) {
            return 1.0F;
         }

         long var5 = var3 - this.Х衣 - this.Хш;
         float var7 = Math.min(1.0F, (float)var5 / (float)Math.max(this.ХИ, 1L));
         return 1.0F - скВ * HitEffect.Щщ(var7);
      }

      private TimedEffect(Vec3 var1, long var2, long var4, long var6, long var8) {
         this.Й = var1;
         this.ХЬ = var2;
         this.Х衣 = var4;
         this.Хш = var6;
         this.ХИ = var8;
      }
   }
}
