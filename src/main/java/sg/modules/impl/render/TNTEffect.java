package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Map.Entry;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public final class TNTEffect extends ClientModule {
   private final sg.settings.SingleChoiceSetting effectStyle = new sg.settings.SingleChoiceSetting("Эффект",
      new sg.settings.ChoiceOption("Energy"), new sg.settings.ChoiceOption("Original"));
   private final sg.render.effects.TntBlastEffect importedEffect;
   private static TNTEffect instance;

   public boolean usesImportedEffect() { return this.effectStyle.isSelected("Energy"); }
   public static Vec3 cameraShake() {
      return instance == null ? Vec3.ZERO : instance.importedEffect.cameraOffset();
   }
   public static void onClientExplosion(Vec3 position, float power, boolean tnt) {
      if (instance != null && instance.isEnabled()) instance.importedEffect.spawn(position, power, tnt);
   }
   private static final int еВ = 8;
   private static final int еЯ = 24;
   private static final int еХ = 36;
   private static final int ее = 144;
   private final FloatSetting Вп;
   private final FloatSetting ВД;
   private final FloatSetting ВЪ;
   private final List<TNTEffect.Explosion> сА;
   private final Map<Integer, TNTEffect.TrackedTnt> сс;
   @SerializedName("8")
   private TextureTarget field_8;
   private final RenderPipeline с弟;
   private final EventListener<PlayerTickEvent> ВЮ;
   private final EventListener<PacketReceiveEvent> ВЧ;
   private final EventListener<WorldEffectsEvent> ВЛ;
   private static final String ЮФ = "TNTEffect";
   private static final String ЮЙ = "Ударная волна при взрыве TNT";
   private static final String Юк = "Сила";
   private static final float Юг = 1.35F;
   private static final float Ю0 = 0.5F;
   private static final float Юх = 2.5F;
   private static final float ЮУ = 0.05F;
   private static final String ЮЗ = "Радиус";
   private static final float ЮЕ = 16.0F;
   private static final float ЮП = 8.0F;
   private static final float Ю西 = 28.0F;
   private static final float Юд = 0.5F;
   private static final String ЮБ = "Длительность";
   private static final float ЮС = 0.9F;
   private static final float Юз = 0.5F;
   private static final float Юц = 1.6F;
   private static final float Юя = 0.05F;
   private static final String Чс = "rose/tnt_effect";
   private static final String ЧВ = "tnt_effect";
   private static final String ЧЯ = "tnt_effect";
   private static final String ЧХ = "TNTSphere";
   private static final String Че = "SceneSampler";
   private static final double ЧА = 128.0;
   private static final double Ч4 = 16.0;
   private static final long Чп = 1000L;
   private static final double ЧД = 0.25;
   private static final long ЧЪ = 150000000L;
   private static final float Чт = 4.0F;
   private static final float Ча = 0.75F;
   private static final float ЧЮ = 0.1F;
   private static final float ЧЧ = 1.0E9F;
   private static final double ЧЛ = 3.4;
   private static final float Ч诶 = 0.35F;
   private static final float Ч1 = 0.045F;
   private static final float ЧЬ = 0.52F;
   private static final float Ч衣 = 0.18F;
   private static final float Чш = 0.022F;
   private static final float ЧИ = 0.038F;
   private static final int Чь = -16777216;
   private static final float Чж = 1.15F;
   private static final float Ч弟 = 0.58F;
   private static final float Чб = 0.05F;
   private static final float ЧЫ = 1.05F;
   private static final float Чl = 0.52F;
   private static final String Ч8 = "SceneSampler";
   private static final String Ч5 = "TNTSphere";
   private static final float Чв = 24.0F;
   private static final float Чъ = 24.0F;
   private static final float ЧМ = 0.5F;
   private static final float ЧН = (float) Math.PI;
   private static final float Чн = 0.5F;
   private static final float ЧЦ = (float) Math.PI;
   private static final float Чч = 36.0F;
   private static final float ЧЭ = 36.0F;
   private static final float Ч必 = (float) (Math.PI * 2);
   private static final float Чщ = (float) (Math.PI * 2);
   private static final String ЧГ = "tnt-effect-scene";
   private static final float Чл = 3.0F;
   private static final String Чэ = "TNT shockwave spheres";
   private static final String Чю = "TNT shockwave sphere uniforms";
   private static final String ЧЩ = "TNT shockwave sphere vertices";
   private static final float Чу = 1.0E9F;
   private static final long Ч2 = 1000L;

   private void ДЪ(PlayerTickEvent var1) {
      this.importedEffect.tick();
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         long var2 = System.currentTimeMillis();

         for (PrimedTnt var5 : MINECRAFT_CLIENT.level.getEntitiesOfClass(PrimedTnt.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(ЧА), var0 -> true)) {
            this.сс.put(var5.getId(), new TNTEffect.TrackedTnt(var5.position(), var2));
         }

         this.сс.values().removeIf(var2x -> var2 - var2x.lastSeen > Ч2);
      } else {
         this.сА.clear();
         this.сс.clear();
      }
   }

   private void И(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundExplodePacket var2) {
         Vec3 var5 = var2.center();
         float var4 = var2.radius();
         MINECRAFT_CLIENT.execute(() -> this.Ъ(var5, var4));
      }
   }

   private void Ъ(Vec3 var1, float var2) {
      if (this.isEnabled() && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         long var3 = System.currentTimeMillis();
         Integer var5 = null;
         double var6 = Ч4;

         for (Entry var9 : this.сс.entrySet()) {
            TNTEffect.TrackedTnt var10 = (TNTEffect.TrackedTnt)var9.getValue();
            if (var3 - var10.lastSeen <= Чп) {
               double var11 = var10.position.distanceToSqr(var1);
               if (var11 < var6) {
                  var6 = var11;
                  var5 = (Integer)var9.getKey();
               }
            }
         }

         if (this.usesImportedEffect()) {
            this.importedEffect.spawn(var1, var2, var5 != null);
            if (var5 != null) this.сс.remove(var5);
            return;
         }
         if (var5 != null) {
            this.сс.remove(var5);
            long var13 = System.nanoTime();

            for (TNTEffect.Explosion var16 : this.сА) {
               if (var16.position.distanceToSqr(var1) < ЧД && var13 - var16.startedAt < ЧЪ) {
                  return;
               }
            }

            float var15 = Mth.clamp(var2 / Чт, Ча, 2.0F);
            this.сА.add(new TNTEffect.Explosion(var1, var13, var15));

            while (this.сА.size() > 8) {
               this.сА.removeFirst();
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.importedEffect.clear();
      this.сА.clear();
      this.сс.clear();
      if (this.field_8 != null) {
         this.field_8.destroyBuffers();
         this.field_8 = null;
      }
   }

   private void Ъ(RenderTarget var1, List<TNTEffect.ShockwaveUniforms> var2, Matrix4f var3, Matrix4f var4) {
      TextureTarget var5 = this.Ъ(var1);
      CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
      var6.copyTextureToTexture(var1.getColorTexture(), var5.getColorTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
      Vector4f var7 = ColorMath.toRgbaVector(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | Чь);
      BufferBuilder var8 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
      Matrix4f var9 = new Matrix4f();
      int var10 = 0;

      for (TNTEffect.ShockwaveUniforms var12 : var2) {
         float var13 = var12.centerX * var12.centerX + var12.centerY * var12.centerY + var12.centerZ * var12.centerZ;
         boolean var14 = var13 < (var12.radius + var12.thickness) * (var12.radius + var12.thickness);
         var10 += this.Ъ(var8, var9, var12, var12.radius + var12.thickness * Чж, var12.strength * Ч弟, var14, var7);
         var10 += this.Ъ(var8, var9, var12, Math.max(Чб, var12.radius - var12.thickness * ЧЫ), -var12.strength * Чl, var14, var7);
         var10 += this.Ъ(var8, var9, var12, var12.radius, var12.strength, var14, var7);
      }

      MeshData var35 = var8.build();
      if (var35 != null && var10 != 0) {
         try (
            var35;
            MemoryStack var37 = MemoryStack.stackPush();
         ) {
            DrawState var38 = var35.drawState();
            AutoStorageIndexBuffer var15 = RenderSystem.getSequentialBuffer(var38.primitiveTopology());
            GpuBuffer var16 = var15.getBuffer(var38.indexCount());
            ByteBuffer var17 = Std140Builder.onStack(var37, 144).putMat4f(var3).putMat4f(var4).putVec4(var1.width, var1.height, var3.m00(), var3.m11()).get();

            try (
               GpuBuffer var18 = RenderSystem.getDevice().createBuffer(() -> ЧЩ, 32, var35.vertexBuffer());
               GpuBuffer var19 = RenderSystem.getDevice().createBuffer(() -> Чю, 128, var17);
               RenderPass var20 = var6.createRenderPass(
                  () -> Чэ, var1.getColorTextureView(), Optional.empty(), var1.getDepthTextureView(), OptionalDouble.empty()
               );
            ) {
               var20.setPipeline(this.с弟);
               var20.bindTexture(Ч8, var5.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var20.setUniform(Ч5, var19);
               var20.setVertexBuffer(0, var18.slice());
               var20.setIndexBuffer(var16, var15.type());
               var20.drawIndexed(var38.indexCount(), 1, 0, 0, 0);
            }
         }
      } else {
         if (var35 != null) {
            var35.close();
         }
      }
   }

   @Override
   public void onEnable() {
      this.importedEffect.clear();
      this.сА.clear();
      this.сс.clear();
   }

   private int Ъ(
      BufferBuilder var1,
      Matrix4f var2,
      TNTEffect.ShockwaveUniforms var3,
      float var4,
      float var5,
      float var6,
      float var7,
      boolean var8,
      Vector4f var9,
      float var10,
      float var11
   ) {
      float var12 = Mth.cos(var6);
      float var13 = var12 * Mth.cos(var5);
      float var14 = Mth.sin(var6);
      float var15 = var12 * Mth.sin(var5);
      var1.addVertex(var2, var3.centerX + var13 * var4, var3.centerY + var14 * var4, var3.centerZ + var15 * var4)
         .setUv(var7, var8 ? -var4 : var4)
         .setColor(var9.x, var9.y, var9.z, var3.alpha)
         .setNormal(var13, var14, var15);
      return 1;
   }

   private TextureTarget Ъ(RenderTarget var1) {
      if (this.field_8 == null) {
         this.field_8 = new TextureTarget(ЧГ, var1.width, var1.height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.field_8.width != var1.width || this.field_8.height != var1.height) {
         this.field_8.resize(var1.width, var1.height);
      }

      return this.field_8;
   }

   private void Б(WorldEffectsEvent var1) {
      if (this.usesImportedEffect()) {
         this.importedEffect.render(var1);
         return;
      }
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !this.сА.isEmpty()) {
         long var2 = System.nanoTime();
         float var4 = Math.max(ЧЮ, this.ВЪ.getValue());
         this.сА.removeIf(var3 -> (float)(var2 - var3.startedAt) / Чу >= var4);
         if (!this.сА.isEmpty()) {
            RenderTarget var5 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            if (var5 != null && var5.getColorTexture() != null && var5.getDepthTexture() != null && !var5.getDepthTexture().isClosed()) {
               ArrayList var6 = new ArrayList(8);

               for (TNTEffect.Explosion var8 : this.сА) {
                  TNTEffect.ShockwaveUniforms var9 = this.Ъ(var8, var1.getCameraPosition(), var2, var4);
                  if (var9 != null) {
                     var6.add(var9);
                  }
               }

               if (!var6.isEmpty()) {
                  this.Ъ(var5, var6, var1.getProjectionMatrix(), var1.getViewMatrix());
               }
            }
         }
      }
   }

   private int Ъ(BufferBuilder var1, Matrix4f var2, TNTEffect.ShockwaveUniforms var3, float var4, float var5, boolean var6, Vector4f var7) {
      int var8 = 0;

      for (int var9 = 0; var9 < 24; var9++) {
         float var10 = var9 / Чв;
         float var11 = (var9 + 1.0F) / Чъ;
         float var12 = (var10 - ЧМ) * ЧН;
         float var13 = (var11 - Чн) * ЧЦ;

         for (int var14 = 0; var14 < 36; var14++) {
            float var15 = var14 / Чч;
            float var16 = (var14 + 1.0F) / ЧЭ;
            float var17 = var15 * Ч必;
            float var18 = var16 * Чщ;
            var8 += this.Ъ(var1, var2, var3, var4, var17, var12, var5, var6, var7, var15, var10);
            var8 += this.Ъ(var1, var2, var3, var4, var18, var13, var5, var6, var7, var16, var11);
            var8 += this.Ъ(var1, var2, var3, var4, var18, var12, var5, var6, var7, var16, var10);
            var8 += this.Ъ(var1, var2, var3, var4, var17, var12, var5, var6, var7, var15, var10);
            var8 += this.Ъ(var1, var2, var3, var4, var17, var13, var5, var6, var7, var15, var11);
            var8 += this.Ъ(var1, var2, var3, var4, var18, var13, var5, var6, var7, var16, var11);
         }
      }

      return var8;
   }

   public TNTEffect() {
      super(ЮФ, ЮЙ, ModuleCategory.RENDER);
      this.Вп = new FloatSetting(Юк, Юг, Ю0, Юх).withStep(ЮУ);
      this.ВД = new FloatSetting(ЮЗ, ЮЕ, ЮП, Ю西).withStep(Юд);
      this.ВЪ = new FloatSetting(ЮБ, ЮС, Юз, Юц).withStep(Юя);
      this.сА = new ArrayList<>();
      this.сс = new HashMap<>();
      this.с弟 = RenderPipelineBuilder.builder()
         .withLocation(Чс)
         .withVertexShader(ClientResourceIds.getCoreId(ЧВ))
         .withFragmentShader(ClientResourceIds.getCoreId(ЧЯ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, PrimitiveTopology.TRIANGLES)
         .withUniform(ЧХ, UniformType.UNIFORM_BUFFER)
         .withSampler(Че)
         .build();
      this.ВЮ = this::ДЪ;
      this.ВЧ = this::И;
      this.ВЛ = this::Б;
      this.addSettings(this.effectStyle, this.Вп.visibleWhen(() -> !this.usesImportedEffect()),
         this.ВД.visibleWhen(() -> !this.usesImportedEffect()), this.ВЪ.visibleWhen(() -> !this.usesImportedEffect()));
      this.importedEffect = new sg.render.effects.TntBlastEffect(this);
      instance = this;
   }

   private TNTEffect.ShockwaveUniforms Ъ(TNTEffect.Explosion var1, Vec3 var2, long var3, float var5) {
      float var6 = Mth.clamp((float)(var3 - var1.startedAt) / ЧЧ / var5, 0.0F, 1.0F);
      float var7 = 1.0F - (float)Math.pow(1.0F - var6, ЧЛ);
      float var8 = (Ч诶 + var7 * this.ВД.getValue()) * var1.power;
      Vec3 var9 = var1.position.subtract(var2);
      float var10 = Н(0.0F, Ч1, var6);
      float var11 = 1.0F - Н(ЧЬ, 1.0F, var6);
      float var12 = var10 * var11;
      float var13 = Math.max(Ч衣, var8 * (Чш + ЧИ * (1.0F - var6)));
      float var14 = this.Вп.getValue() * var1.power * var12;
      return new TNTEffect.ShockwaveUniforms((float)var9.x, (float)var9.y, (float)var9.z, var8, var13, var14, var12, var6);
   }

   private static float Н(float var0, float var1, float var2) {
      float var3 = Mth.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (Чл - 2.0F * var3);
   }

   private record ShockwaveUniforms(
      float centerX, float centerY, float centerZ, float radius, float thickness, float strength, float alpha, float progress
   ) {
   }

   private record Explosion(Vec3 position, long startedAt, float power) {
   }

   private record TrackedTnt(Vec3 position, long lastSeen) {
   }
}
