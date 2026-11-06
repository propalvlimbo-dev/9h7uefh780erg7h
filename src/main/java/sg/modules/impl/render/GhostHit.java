package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
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
import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.WorldGeometryBuffer;
import sg.events.impl.WorldEffectsEvent;
import sg.mixin.RenderTypeAccessor;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.modules.impl.render.Rain;

public class GhostHit extends ClientModule {
   @SerializedName("Я")
   private final SubmitNodeStorage Я_at_5b89af69 = new SubmitNodeStorage();
   private static final Identifier р = Identifier.fromNamespaceAndPath(GhostHit.Яэм, GhostHit.ЯэК);
   private static final int еБ = 65280;
   private static final long Вы_6da43b94 = 2000000000L;
   private static final RenderPipeline сл = RenderPipelineBuilder.builder(RenderPipelines.ENTITY_SNIPPET)
      .withLocation(GhostHit.ЯэI)
      .withShaderDefine(GhostHit.ЯэТ, GhostHit.ЯэО)
      .withShaderDefine(GhostHit.ЯэФ)
      .withSampler(GhostHit.ЯэЙ)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final Map<Identifier, RenderType> сж = new HashMap<>();
   private static boolean Я弟;
   private static int еС = GhostHit.Яэк;
   private static final Map<AvatarRenderState, Float> с弟 = Collections.synchronizedMap(new IdentityHashMap<>());
   private final FloatSetting В7;
   private final FloatSetting ВР;
   private final FloatSetting В3;
   private final FloatSetting Вр;
   private final FloatSetting В6;
   private final BooleanSetting с3;
   private final FloatSetting В_;
   private final BooleanSetting ср;
   private final FloatSetting ВШ;
   @SerializedName("Вы")
   private final FloatSetting Вы_989fed9a;
   private final BooleanSetting с6;
   private final BooleanSetting с_;
   private final BooleanSetting сШ;
   private final ColorSetting ч;
   @SerializedName("4")
   private final CopyOnWriteArrayList<GhostHit.GhostSnapshot> field_4;
   private int ез;
   private final RenderPipeline сэ;
   private final EventListener<AttackEntityEvent> В2;
   private final EventListener<PacketReceiveEvent> Вф;
   private final EventListener<WorldEffectsEvent> Ви;
   private static final String Ял弟 = "GhostHit";
   private static final String Ялб = "Призрак цели при ударе и тотеме";
   private static final String ЯлЫ = "Время жизни";
   private static final float Ялl = 1450.0F;
   private static final float Ял8 = 300.0F;
   private static final float Ял5 = 4000.0F;
   private static final float Ялв = 10.0F;
   private static final String Ялъ = "Высота подъёма";
   private static final float ЯлМ = 1.85F;
   private static final float ЯлН = 5.0F;
   private static final float Ялн = 0.05F;
   private static final String ЯлЦ = "Прозрачность";
   private static final float Ялч = 0.55F;
   private static final float ЯлЭ = 0.05F;
   private static final float Ял必 = 0.01F;
   private static final String Ялщ = "Свечение";
   private static final float ЯлГ = 0.05F;
   private static final String Ялл = "Размер свечения";
   private static final float Ялэ = 1.15F;
   private static final float Ялю = 0.4F;
   private static final float ЯлЩ = 2.5F;
   private static final float Ялу = 0.05F;
   private static final String Ял2 = "Свечение по краям";
   private static final String Ялф = "Резкость краёв";
   private static final float Яли = 2.5F;
   private static final float Ял9 = 0.5F;
   private static final float Яло = 6.0F;
   private static final float ЯлO = 0.1F;
   private static final String Ял7 = "Искры";
   private static final String ЯлР = "Кол-во искр";
   private static final float Ял3 = 14.0F;
   private static final float Ялр = 40.0F;
   private static final String Ял6 = "Макс. призраков";
   private static final float Ял_ = 7.0F;
   private static final float ЯлШ = 20.0F;
   private static final String Ялы = "Тотем";
   private static final String Ялй = "Радужное свечение";
   private static final String ЯлЖ = "Цвет темы";
   private static final String Ялм = "Цвет";
   private static final int ЯлК = -10962689;
   private static final String ЯлI = "rose/ghost_hit";
   private static final String ЯлТ = "dead_chams";
   private static final String ЯлО = "dead_chams";
   private static final String ЯлФ = "Sampler0";
   private static final long ЯлЙ = 2000000000L;
   private static final float Ялк = 1000000.0F;
   private static final int Ялг = 15728880;
   private static final float Ял0 = 1.35F;
   private static final float Ялх = 0.01F;
   private static final float ЯлУ = 1.0E9F;
   private static final int ЯлЗ = 16777215;
   private static final float ЯлЕ = 0.14F;
   private static final float ЯлП = 0.82F;
   private static final float Ял西 = 0.18F;
   private static final float Ялд = 3.2F;
   private static final double ЯлБ = 0.9;
   private static final float ЯлС = 0.5F;
   private static final float Ялз = 0.01F;
   private static final float Ялц = 0.85F;
   private static final float Яля = 0.15F;
   private static final float Яэс = 3.4F;
   private static final float ЯэВ = 3.1F;
   private static final float ЯэЯ = 5.2F;
   private static final float ЯэХ = 0.18F;
   private static final float Яэе = 1.7F;
   private static final float ЯэА = 2.9F;
   private static final float Яэ4 = 0.35F;
   private static final double Яэп = 0.95;
   private static final double ЯэД = 0.18;
   private static final float ЯэЪ = 0.94F;
   private static final float Яэт = 0.06F;
   private static final float Яэа = 5.2F;
   private static final float ЯэЮ = 1.15F;
   private static final float ЯэЧ = 0.55F;
   private static final float ЯэЛ = 0.18F;
   private static final float Яэ诶 = 0.55F;
   private static final float Яэ1 = 0.28F;
   private static final float ЯэЬ = 0.09F;
   private static final int Яэ衣 = 65382;
   private static final float Яэш = 0.9F;
   private static final float ЯэИ = 1.15F;
   private static final float Яэь = 0.34F;
   private static final float Яэж = 0.11F;
   private static final float Яэ弟 = 0.28F;
   private static final float Яэб = 0.65F;
   private static final float ЯэЫ = 0.075F;
   private static final int Яэl = 65348;
   private static final double Яэ8 = Math.PI * 2;
   private static final double Яэ5 = 0.025;
   private static final long Яэв = 9176L;
   private static final float Яэъ = 0.4F;
   private static final float ЯэМ = 0.5F;
   private static final double ЯэН = Math.PI;
   private static final double Яэн = 2.0;
   private static final double ЯэЦ = 0.25;
   private static final double Яэч = 0.45;
   private static final double ЯэЭ = 2.5;
   private static final double Яэ必 = 0.12;
   private static final float Яэщ = 0.02F;
   private static final double ЯэГ = 0.1;
   private static final double Яэл = 2.1;
   private static final double Яээ = 0.1;
   private static final double Яэю = 0.1;
   private static final float ЯэЩ = 1.1F;
   private static final float Яэу = 0.5F;
   private static final float Яэ2 = 0.5F;
   private static final String Яэф = "Sampler0";
   private static final float Яэи = 1.0E9F;
   private static final float Яэ9 = 0.08F;
   private static final int Яэо = 65280;
   private static final long ЯэO = 8000000L;
   private static final long Яэ7 = 40L;
   private static final long ЯэР = 360L;
   private static final float Яэ3 = 360.0F;
   private static final int Яэр = 16777215;
   private static final int Яэ6 = 16777215;
   private static final float Яэ_ = -1.0F;
   private static final String ЯэШ = "ghostHit";
   private static final String Яэы = "ghostHitVertices";
   private static final String Яэй = "ghost_hit_model";
   private static final String ЯэЖ = "Sampler0";
   private static final String Яэм = "rose";
   private static final String ЯэК = "images/targetesp/glow.png";
   private static final String ЯэI = "rose/ghost_hit_model";
   private static final String ЯэТ = "ALPHA_CUTOUT";
   private static final float ЯэО = 0.1F;
   private static final String ЯэФ = "PER_FACE_LIGHTING";
   private static final String ЯэЙ = "Sampler1";
   private static final int Яэк = 654311423;

   public static float Й_f67ab10a(AvatarRenderState var0) {
      Float var1 = с弟.get(var0);
      return var1 == null ? Яэ_ : var1;
   }

   private void е(int var1) {
      for (GhostHit.GhostSnapshot var3 : this.field_4) {
         if (var3.ВУ == var1) {
            this.Й(var3);
         }
      }
   }

   private void 衣(PacketReceiveEvent var1) {
      if (this.isEnabled() && this.с6.getValue() && MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundEntityEventPacket var2) {
            if (var2.getEventId() == 35) {
               if (var2.getEntity(MINECRAFT_CLIENT.level) instanceof AbstractClientPlayer var4 && var4 != MINECRAFT_CLIENT.player) {
                  MINECRAFT_CLIENT.execute(() -> {
                     if (this.isEnabled() && this.с6.getValue() && MINECRAFT_CLIENT.level != null) {
                        if (var4.level() == MINECRAFT_CLIENT.level && var4.isAlive()) {
                           this.е(var4.getId());
                           this.Й(var4, true);
                        }
                     }
                  });
               }
            }
         }
      }
   }

   private float Й_f67ab10a(GhostHit.GhostSnapshot var1, long var2) {
      return (float)(var2 - var1.сю) / Яэи;
   }

   private int Й(
      BufferBuilder var1, PoseStack var2, GhostHit.GhostSnapshot var3, Vec3 var4, float var5, float var6, float var7, float var8, float var9, int var10
   ) {
      float var11 = this.Вр.getValue();
      float var12 = this.В6.getValue();
      float var13 = Ялц + Яля * Mth.sin(var9 * Яэс + var3.Вх);
      double var14 = var3.Ы.x - var4.x;
      double var16 = var3.Ы.y + var7 + 1.0 - var4.y;
      double var18 = var3.Ы.z - var4.z;
      int var20 = 0;
      var20 += this.Й(
         var1,
         var2,
         var14,
         var16,
         var18,
         ЯэВ * var12 * var13,
         ЯэЯ * var12 * var13,
         0.0F,
         var5,
         var6,
         ColorMath.withAlpha(var10, Mth.clamp(var11 * var8 * ЯэХ, 0.0F, 1.0F))
      );
      return var20
         + this.Й(
            var1,
            var2,
            var14,
            var16,
            var18,
            Яэе * var12 * var13,
            ЯэА * var12 * var13,
            0.0F,
            var5,
            var6,
            ColorMath.withAlpha(var10, Mth.clamp(var11 * var8 * Яэ4, 0.0F, 1.0F))
         );
   }

   private void Й(
      GhostHit.GhostSnapshot var1,
      EntityRenderDispatcher var2,
      LevelRenderState var3,
      SubmitNodeCollector var4,
      PoseStack var5,
      Vec3 var6,
      Vec3 var7,
      float var8,
      int var9
   ) {
      if (var9 >>> 24 != 0) {
         var1.В.scale = var8;
         еС = var9;
         Я弟 = true;

         try {
            var2.submit(var1.В, var3.cameraRenderState, var6.x - var7.x, var6.y - var7.y, var6.z - var7.z, var5, var4);
         } finally {
            Я弟 = false;
         }
      }
   }

   private void Й(AbstractClientPlayer var1, boolean var2) {
      float var3 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      if (MINECRAFT_CLIENT.getEntityRenderDispatcher().extractEntity(var1, var3) instanceof AvatarRenderState var5) {
         this.Й_de5a6f78(var5);
         GhostHit.GhostSnapshot var6 = new GhostHit.GhostSnapshot(
            var5,
            new Vec3(var5.x, var5.y, var5.z),
            System.nanoTime(),
            var5.scale,
            MINECRAFT_CLIENT.level,
            this.ез++,
            var1.getId(),
            var2,
            var2 ? ЯлЙ : (long)(this.В7.getValue() * Ялк)
         );
         this.field_4.add(var6);
         с弟.put(var5, 0.0F);
         this.Ю8();
      }
   }

   private void Ю8() {
      int var1 = Math.round(this.Вы_989fed9a.getValue());

      while (this.field_4.size() > var1) {
         GhostHit.GhostSnapshot var2 = this.field_4.get(0);
         this.field_4.remove(var2);
         с弟.remove(var2.В);
      }
   }

   private void л(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && !this.field_4.isEmpty() && !Rain.Пз()) {
         this.Й(var1.getViewMatrix());
         boolean var2 = this.Вр.getValue() > 0.0F;
         boolean var3 = this.ср.getValue();
         boolean var4 = false;

         for (GhostHit.GhostSnapshot var6 : this.field_4) {
            if (var6.с7) {
               var4 = true;
               break;
            }
         }

         if (var2 || var3 || var4) {
            long var19 = System.nanoTime();
            Vec3 var7 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            float var8 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
            float var9 = MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot();
            BufferBuilder var10 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var11 = 0;

            for (GhostHit.GhostSnapshot var13 : this.field_4) {
               if (var13.Д == MINECRAFT_CLIENT.level) {
                  float var14 = (float)(var19 - var13.сю) / (float)var13.сЩ;
                  if (!(var14 >= 1.0F)) {
                     float var15 = this.ЕЙ(var14);
                     if (!(var15 <= Ялз)) {
                        float var16 = this.ЕЩ(var14);
                        float var17 = this.Й_f67ab10a(var13, var19);
                        int var18 = this.Й_a83dd0cc(var13, var19);
                        if (var2) {
                           var11 += this.Й(var10, var1.getPoses(), var13, var7, var8, var9, var16, var15, var17, var18);
                        }

                        if (var3) {
                           var11 += this.ч(var10, var1.getPoses(), var13, var7, var8, var9, var16, var15, var17, var18);
                        }

                        if (var13.с7) {
                           var11 += this.Щ(var10, var1.getPoses(), var13, var7, var8, var9, var16, var15, var17, var18);
                        }
                     }
                  }
               }
            }

            this.ч(var10, var11);
         }
      }
   }

   private void Й(Matrix4f var1) {
      WorldGeometryBuffer.StagedBuffer var2 = WorldGeometryBuffer.getShared();
      var2.flush();
      Matrix4fStack var3 = RenderSystem.getModelViewStack();
      var3.pushMatrix();

      try {
         var3.mul(var1);
         this.Й(new PoseStack(), MINECRAFT_CLIENT.gameRenderer.gameRenderState().levelRenderState, this.Я_at_5b89af69, MINECRAFT_CLIENT.getEntityRenderDispatcher());
         MINECRAFT_CLIENT.gameRenderer.featureRenderDispatcher().renderAllFeatures(this.Я_at_5b89af69);
         var2.flush();
      } finally {
         var3.popMatrix();
      }
   }

   private int Й(
      BufferBuilder var1, PoseStack var2, double var3, double var5, double var7, float var9, float var10, float var11, float var12, float var13, int var14
   ) {
      if (var14 >>> 24 <= 1) {
         return 0;
      }

      Vector4f var15 = ColorMath.toRgbaVector(var14);
      float var16 = var9 * Яэу;
      float var17 = var10 * Яэ2;
      var2.pushPose();
      var2.translate(var3, var5, var7);
      var2.mulPose(Axis.YP.rotationDegrees(-var12));
      var2.mulPose(Axis.XP.rotationDegrees(var13));
      var2.mulPose(Axis.ZP.rotationDegrees(var11));
      Matrix4f var18 = var2.last().pose();
      this.ч(var1, var18, -var16, -var17, 0.0F, 0.0F, 0.0F, var15);
      this.ч(var1, var18, -var16, var17, 0.0F, 0.0F, 1.0F, var15);
      this.ч(var1, var18, var16, var17, 0.0F, 1.0F, 1.0F, var15);
      this.ч(var1, var18, var16, -var17, 0.0F, 1.0F, 0.0F, var15);
      var2.popPose();
      return 4;
   }

   public static RenderType Й(Identifier var0) {
      return сж.computeIfAbsent(
         var0, var0x -> RenderTypeAccessor.create(Яэй, RenderSetup.builder(сл).withTexture(ЯэЖ, var0x).useLightmap().useOverlay().sortOnUpload().createRenderSetup())
      );
   }

   private void ч(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, Vector4f var8) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(var8.x, var8.y, var8.z, var8.w);
   }

   @Override
   public void onEnable() {
      this.Ю西();
   }

   public static boolean Щ(AvatarRenderState var0) {
      return с弟.containsKey(var0);
   }

   private void ч(BufferBuilder var1, int var2) {
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
               var4 = RenderSystem.getDevice().createBuffer(() -> Яэы, 32, var3.vertexBuffer());
               RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var12 = MINECRAFT_CLIENT.getTextureManager().getTexture(р);

               try (RenderPass var13 = var11.createRenderPass(
                     () -> ЯэШ, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var13);
                  var13.setPipeline(this.сэ);
                  var13.bindTexture(Яэф, var12.getTextureView(), var12.getSampler());
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

   private int Й(
      BufferBuilder var1,
      PoseStack var2,
      GhostHit.GhostSnapshot var3,
      Vec3 var4,
      float var5,
      float var6,
      double var7,
      float var9,
      float var10,
      float var11,
      int var12,
      int var13,
      float var14
   ) {
      int var15 = 0;

      for (int var16 = 0; var16 < var12; var16++) {
         double var17 = var10 + var16 * (Яэ8 / var12);
         double var19 = var3.Ы.x + Math.cos(var17) * var9 - var4.x;
         double var21 = var7 + Math.sin(var17) * Яэ5 - var4.y;
         double var23 = var3.Ы.z + Math.sin(var17) * var9 - var4.z;
         var15 += this.Й(var1, var2, var19, var21, var23, var11, var11, 0.0F, var5, var6, ColorMath.withAlpha(var13, var14));
      }

      return var15;
   }

   private float ЕЩ(float var1) {
      float var2 = 1.0F - var1;
      return this.ВР.getValue() * (1.0F - var2 * var2 * var2);
   }

   public GhostHit() {
      super(Ял弟, Ялб, ModuleCategory.RENDER);
      this.В7 = new FloatSetting(ЯлЫ, Ялl, Ял8, Ял5).withStep(Ялв);
      this.ВР = new FloatSetting(Ялъ, ЯлМ, 0.0F, ЯлН).withStep(Ялн);
      this.В3 = new FloatSetting(ЯлЦ, Ялч, ЯлЭ, 1.0F).withStep(Ял必);
      this.Вр = new FloatSetting(Ялщ, 1.0F, 0.0F, 2.0F).withStep(ЯлГ);
      this.В6 = new FloatSetting(Ялл, Ялэ, Ялю, ЯлЩ).withStep(Ялу).visibleWhen(() -> this.Вр.getValue() > 0.0F);
      this.с3 = new BooleanSetting(Ял2, true);
      this.В_ = new FloatSetting(Ялф, Яли, Ял9, Яло).withStep(ЯлO).visibleWhen(() -> this.с3.getValue());
      this.ср = new BooleanSetting(Ял7, true);
      this.ВШ = new FloatSetting(ЯлР, Ял3, 2.0F, Ялр).withStep(1.0F).visibleWhen(() -> this.ср.getValue());
      this.Вы_989fed9a = new FloatSetting(Ял6, Ял_, 1.0F, ЯлШ).withStep(1.0F);
      this.с6 = new BooleanSetting(Ялы, true);
      this.с_ = new BooleanSetting(Ялй, false);
      this.сШ = new BooleanSetting(ЯлЖ, true).visibleWhen(() -> !this.с_.getValue());
      this.ч = new ColorSetting(Ялм, false, ЯлК).visibleWhen(() -> !this.с_.getValue() && !this.сШ.getValue());
      this.field_4 = new CopyOnWriteArrayList<>();
      this.сэ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ЯлI)
         .withVertexShader(ClientResourceIds.getCoreId(ЯлТ))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯлО))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ЯлФ)
         .build();
      this.В2 = this::Х;
      this.Вф = this::衣;
      this.Ви = this::л;
      this.addSettings(this.В7, this.ВР, this.В3, this.Вр, this.В6, this.с3, this.В_, this.ср, this.ВШ, this.Вы_989fed9a, this.с6, this.с_, this.сШ, this.ч);
   }

   private void Х(AttackEntityEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.level != null && var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         if (var1.getTarget() instanceof AbstractClientPlayer var2 && var2.isAlive()) {
            this.Й(var2, false);
         }
      }
   }

   private void Й_de5a6f78(AvatarRenderState var1) {
      var1.isInvisible = true;
      var1.isInvisibleToPlayer = false;
      var1.displayFireAnimation = false;
      var1.outlineColor = 0;
      var1.nameTag = null;
      var1.nameTagAttachment = null;
      var1.scoreText = null;
      var1.leashStates = null;
      var1.shadowPieces.clear();
      var1.arrowCount = 0;
      var1.stingerCount = 0;
      var1.showHat = false;
      var1.showJacket = false;
      var1.showLeftPants = false;
      var1.showRightPants = false;
      var1.showLeftSleeve = false;
      var1.showRightSleeve = false;
      var1.showCape = false;
      var1.showExtraEars = false;
      var1.parrotOnLeftShoulder = null;
      var1.parrotOnRightShoulder = null;
      var1.headEquipment = ItemStack.EMPTY;
      var1.chestEquipment = ItemStack.EMPTY;
      var1.legsEquipment = ItemStack.EMPTY;
      var1.feetEquipment = ItemStack.EMPTY;
      var1.leftHandItemStack = ItemStack.EMPTY;
      var1.rightHandItemStack = ItemStack.EMPTY;
      var1.headItem.clear();
      var1.leftHandItemState.clear();
      var1.rightHandItemState.clear();
      var1.heldOnHead.clear();
      var1.lightCoords = Ялг;
      var1.isPassenger = false;
      var1.isAutoSpinAttack = false;
      var1.attackTime = 0.0F;
   }

   private float ЕЙ(float var1) {
      float var2 = Mth.clamp(var1 / Яэ9, 0.0F, 1.0F);
      float var3 = 1.0F - var1;
      return var2 * var3 * var3;
   }

   private int Щ(
      BufferBuilder var1, PoseStack var2, GhostHit.GhostSnapshot var3, Vec3 var4, float var5, float var6, float var7, float var8, float var9, int var10
   ) {
      double var11 = var3.Ы.y + var7 + var3.В.boundingBoxHeight * Яэп + ЯэД;
      double var13 = var3.Ы.x - var4.x;
      double var15 = var11 - var4.y;
      double var17 = var3.Ы.z - var4.z;
      float var19 = ЯэЪ + Яэт * Mth.sin(var9 * Яэа + var3.Вх);
      float var20 = Mth.clamp(var8 * ЯэЮ, 0.0F, 1.0F);
      int var21 = 0;
      var21 += this.Й(var1, var2, var13, var15, var17, ЯэЧ * var19, ЯэЛ * var19, 0.0F, var5, var6, ColorMath.withAlpha(var10, Mth.clamp(var8 * Яэ诶, 0.0F, 1.0F)));
      var21 += this.Й(var1, var2, var13, var15, var17, Яэ1 * var19, ЯэЬ * var19, 0.0F, var5, var6, ColorMath.withAlpha(Яэ衣, Mth.clamp(var8 * Яэш, 0.0F, 1.0F)));
      byte var22 = 32;
      float var23 = var9 * ЯэИ;
      var21 += this.Й(var1, var2, var3, var4, var5, var6, var11, Яэь, var23, Яэж, var22, var10, var20);
      return var21 + this.Й(var1, var2, var3, var4, var5, var6, var11, Яэ弟, -var23 * Яэб, ЯэЫ, var22, Яэl, var20);
   }

   private void Й(PoseStack var1, LevelRenderState var2, SubmitNodeCollector var3, EntityRenderDispatcher var4) {
      if (MINECRAFT_CLIENT.level != null && !this.field_4.isEmpty()) {
         long var5 = System.nanoTime();
         Vec3 var7 = var2.cameraRenderState.pos;

         for (GhostHit.GhostSnapshot var9 : this.field_4) {
            if (var9.Д != MINECRAFT_CLIENT.level) {
               this.Й(var9);
            } else {
               float var10 = (float)(var5 - var9.сю) / (float)var9.сЩ;
               if (var10 >= 1.0F) {
                  this.Й(var9);
               } else {
                  float var11 = (var9.с7 ? Math.min(1.0F, this.В3.getValue() * Ял0) : this.В3.getValue()) * this.ЕЙ(var10);
                  if (!(var11 <= Ялх)) {
                     Vec3 var12 = var9.Ы.add(0.0, this.ЕЩ(var10), 0.0);
                     с弟.put(var9.В, (float)(var5 - var9.сю) / ЯлУ);
                     int var13 = var9.с7 ? this.Й_a83dd0cc(var9, var5) : ЯлЗ;
                     if (this.с3.getValue()) {
                        float var14 = ЯлЕ / this.В_.getValue();
                        float var15 = ЯлП + Ял西 * Mth.sin(this.Й_f67ab10a(var9, var5) * Ялд + var9.Вх);
                        this.Й(
                           var9,
                           var4,
                           var2,
                           var3,
                           var1,
                           var12.subtract(0.0, var14 * ЯлБ, 0.0),
                           var7,
                           var9.ЯI * (1.0F + var14),
                           ColorMath.withAlpha(this.Й_a83dd0cc(var9, var5), var11 * ЯлС * var15)
                        );
                     }

                     this.Й(var9, var4, var2, var3, var1, var12, var7, var9.ЯI, ColorMath.withAlpha(var13, var11));
                  }
               }
            }
         }
      }
   }

   private void Ю西() {
      this.field_4.clear();
      с弟.clear();
      сж.clear();
   }

   public static int М(int var0) {
      return Я弟 ? еС : var0;
   }

   private int ч(
      BufferBuilder var1, PoseStack var2, GhostHit.GhostSnapshot var3, Vec3 var4, float var5, float var6, float var7, float var8, float var9, int var10
   ) {
      int var11 = Math.round(this.ВШ.getValue());
      Random var12 = new Random(var3.Вх * Яэв + 1L);
      int var13 = 0;

      for (int var14 = 0; var14 < var11; var14++) {
         float var15 = var12.nextFloat();
         float var16 = (var9 * (Яэъ + var12.nextFloat() * ЯэМ) + var15) % 1.0F;
         double var17 = var12.nextFloat() * ЯэН * Яэн;
         double var19 = ЯэЦ + var12.nextFloat() * Яэч;
         double var21 = Math.sin(var9 * ЯэЭ + var14) * Яэ必;
         float var23 = (1.0F - var16) * var8;
         if (!(var23 <= Яэщ)) {
            double var24 = var3.Ы.x + Math.cos(var17) * var19 + var21 - var4.x;
            double var26 = var3.Ы.y + ЯэГ + var16 * (var7 + Яэл) - var4.y;
            double var28 = var3.Ы.z + Math.sin(var17) * var19 - var4.z;
            float var30 = (float)(Яээ + Яэю * (1.0F - var16));
            var13 += this.Й(var1, var2, var24, var26, var28, var30, var30, 0.0F, var5, var6, ColorMath.withAlpha(var10, Mth.clamp(var23 * ЯэЩ, 0.0F, 1.0F)));
         }
      }

      return var13;
   }

   private int Й_a83dd0cc(GhostHit.GhostSnapshot var1, long var2) {
      if (var1.с7) {
         return Яэо;
      } else if (this.с_.getValue()) {
         float var4 = (float)((var2 / ЯэO + var1.Вх * Яэ7) % ЯэР) / Яэ3;
         return Color.HSBtoRGB(var4, 1.0F, 1.0F) & Яэр;
      } else {
         return (this.сШ.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.ч.getValue()) & Яэ6;
      }
   }

   @Override
   public void onDisable() {
      this.Ю西();
      Я弟 = false;
   }

   private void Й(GhostHit.GhostSnapshot var1) {
      this.field_4.remove(var1);
      с弟.remove(var1.В);
   }

   private static final class GhostSnapshot {
      private final AvatarRenderState В;
      private final Vec3 Ы;
      private final long сю;
      private final float ЯI;
      private final ClientLevel Д;
      private final int Вх;
      private final int ВУ;
      private final boolean с7;
      private final long сЩ;

      private GhostSnapshot(AvatarRenderState var1, Vec3 var2, long var3, float var5, ClientLevel var6, int var7, int var8, boolean var9, long var10) {
         this.В = var1;
         this.Ы = var2;
         this.сю = var3;
         this.ЯI = var5;
         this.Д = var6;
         this.Вх = var7;
         this.ВУ = var8;
         this.с7 = var9;
         this.сЩ = var10;
      }
   }
}
