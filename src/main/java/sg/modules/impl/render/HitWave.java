package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.EntityEspOuterGlowRenderer;
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

public final class HitWave extends ClientModule {
   private static final int е1 = 6;
   private static final int еЬ = 368;
   private static final HitWave.WaveUniforms В = new HitWave.WaveUniforms(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   private final FloatSetting ВЬ;
   private final FloatSetting В衣;
   private final FloatSetting Вш;
   private final FloatSetting ВИ;
   private final FloatSetting Вь;
   private final BooleanSetting св;
   private final ColorSetting ъ;
   private final List<HitWave.Wave> ст;
   private final RenderPipeline сЫ;
   private final RenderPipeline сl;
   @SerializedName("5")
   private TextureTarget field_5;
   private final EventListener<AttackEntityEvent> Вж;
   private final EventListener<WorldEffectsEvent> В弟;
   private static final String Ви6 = "HitWave";
   private static final String Ви_ = "Запускает световую волну по всем прогруженным блокам";
   private static final String ВиШ = "Скорость волны";
   private static final float Виы = 1.25F;
   private static final float Вий = 0.35F;
   private static final float ВиЖ = 3.5F;
   private static final float Вим = 0.05F;
   private static final String ВиК = "Радиус сонара";
   private static final float ВиI = 48.0F;
   private static final float ВиТ = 8.0F;
   private static final float ВиО = 192.0F;
   private static final float ВиФ = 4.0F;
   private static final String ВиЙ = "Импульс блока";
   private static final float Вик = 0.22F;
   private static final float Виг = 0.08F;
   private static final float Ви0 = 0.7F;
   private static final float Вих = 0.02F;
   private static final String ВиУ = "Яркость";
   private static final float ВиЗ = 1.55F;
   private static final float ВиЕ = 0.4F;
   private static final float ВиП = 2.8F;
   private static final float Ви西 = 0.05F;
   private static final String Вид = "Свечение";
   private static final float ВиБ = 8.5F;
   private static final float ВиС = 14.0F;
   private static final float Виз = 0.5F;
   private static final String Виц = "Цвет темы";
   private static final String Вия = "Цвет";
   private static final int В9с = -8950529;
   private static final String В9В = "rose/hit_sonnar_mask";
   private static final String В9Я = "hands_fullscreen";
   private static final String В9Х = "hit_sonnar_mask";
   private static final String В9е = "HitSonnar";
   private static final String В9А = "DepthSampler";
   private static final String В94 = "rose/hit_sonnar";
   private static final String В9п = "hands_fullscreen";
   private static final String В9Д = "hit_sonnar";
   private static final String В9Ъ = "HitSonnar";
   private static final String В9т = "DepthSampler";
   private static final double В9а = 0.001;
   private static final double В9Ю = 0.45;
   private static final double В9Ч = -8.0;
   private static final double В9Л = 0.025;
   private static final double В9诶 = 4096.0;
   private static final double В91 = 4096.0;
   private static final long В9Ь = 1000000L;
   private static final float В9衣 = 1000.0F;
   private static final float В9ш = 0.1F;
   private static final float В9И = 255.0F;
   private static final float В9ь = 255.0F;
   private static final float В9ж = 255.0F;
   private static final float В9弟 = 1.2F;
   private static final float В9б = 0.58F;
   private static final float В9Ы = 1000.0F;
   private static final float В9l = 0.01F;
   private static final float В98 = 0.002F;
   private static final String В95 = "DepthSampler";
   private static final String В9в = "HitSonnar";
   private static final String В9ъ = "DepthSampler";
   private static final String В9М = "HitSonnar";
   private static final String В9Н = "hit-sonnar-surface-mask";
   private static final float В9н = -10000.0F;
   private static final float В9Ц = -10000.0F;
   private static final float В9ч = 1000.0F;
   private static final float В9Э = 6.0F;
   private static final float В9必 = 15.0F;
   private static final float В9щ = 10.0F;
   private static final String В9Г = "hitSonnarSurfaceCore";
   private static final String В9л = "hitSonnarSurfaceMask";
   private static final String В9э = "hitSonnarSurfaceUniforms";

   private float Ц(float var1) {
      return var1 * var1 * var1 * (var1 * (var1 * В9Э - В9必) + В9щ);
   }

   public HitWave() {
      super(Ви6, Ви_, ModuleCategory.RENDER);
      this.ВЬ = new FloatSetting(ВиШ, Виы, Вий, ВиЖ).withStep(Вим);
      this.В衣 = new FloatSetting(ВиК, ВиI, ВиТ, ВиО).withStep(ВиФ);
      this.Вш = new FloatSetting(ВиЙ, Вик, Виг, Ви0).withStep(Вих);
      this.ВИ = new FloatSetting(ВиУ, ВиЗ, ВиЕ, ВиП).withStep(Ви西);
      this.Вь = new FloatSetting(Вид, ВиБ, 2.0F, ВиС).withStep(Виз);
      this.св = new BooleanSetting(Виц, true);
      this.ъ = new ColorSetting(Вия, false, В9с).visibleWhen(() -> !this.св.getValue());
      this.ст = new ArrayList<>();
      this.сЫ = RenderPipelineBuilder.builder()
         .withLocation(В9В)
         .withVertexShader(ClientResourceIds.getCoreId(В9Я))
         .withFragmentShader(ClientResourceIds.getCoreId(В9Х))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(В9е, UniformType.UNIFORM_BUFFER)
         .withSampler(В9А)
         .build();
      this.сl = RenderPipelineBuilder.builder()
         .withLocation(В94)
         .withVertexShader(ClientResourceIds.getCoreId(В9п))
         .withFragmentShader(ClientResourceIds.getCoreId(В9Д))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(В9Ъ, UniformType.UNIFORM_BUFFER)
         .withSampler(В9т)
         .build();
      this.Вж = this::ь;
      this.В弟 = this::е;
      this.addSettings(this.ВЬ, this.В衣, this.Вш, this.ВИ, this.Вь, this.св, this.ъ);
   }

   private void е(WorldEffectsEvent var1) {
      this.I0();
      if (!this.ст.isEmpty() && MINECRAFT_CLIENT.level != null && var1.getProjectionMatrix() != null && var1.getViewMatrix() != null) {
         RenderTarget var2 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (var2 != null && var2.getColorTexture() != null && var2.getDepthTexture() != null && !var2.getDepthTexture().isClosed()) {
            GpuTextureView var3 = var2.getDepthTextureView();
            long var4 = System.currentTimeMillis();
            Vec3 var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            List var7 = this.И(var4, var6);
            if (!var7.isEmpty()) {
               this.з(var2.width, var2.height);
               int var8 = this.тЩ();
               Matrix4f var9 = new Matrix4f(var1.getProjectionMatrix()).invert();
               Matrix4f var10 = new Matrix4f(var1.getViewMatrix()).invert();

               try (MemoryStack var11 = MemoryStack.stackPush()) {
                  Std140Builder var12 = Std140Builder.onStack(var11, 368)
                     .putMat4f(var9)
                     .putMat4f(var10)
                     .putVec4(this.method_0(var6.x, В9诶), (float)var6.y, this.method_0(var6.z, В91), (float)(var4 % В9Ь) / В9衣)
                     .putVec4(var7.size(), this.Вш.getValue() / Math.max(this.ВЬ.getValue(), В9ш), this.ВИ.getValue(), this.В衣.getValue())
                     .putVec4(ColorMath.getRed(var8) / В9И, ColorMath.getGreen(var8) / В9ь, ColorMath.getBlue(var8) / В9ж, this.И(var1));

                  for (int var13 = 0; var13 < 6; var13++) {
                     HitWave.WaveUniforms var14 = var13 < var7.size() ? (HitWave.WaveUniforms)var7.get(var13) : В;
                     var12.putVec4(var14.x, var14.y, var14.z, var14.progress);
                  }

                  for (int var21 = 0; var21 < 6; var21++) {
                     HitWave.WaveUniforms var23 = var21 < var7.size() ? (HitWave.WaveUniforms)var7.get(var21) : В;
                     var12.putVec4(var23.envelope, var23.seed, 0.0F, 0.0F);
                  }

                  ByteBuffer var22 = var12.get();

                  try (GpuBuffer var24 = RenderSystem.getDevice().createBuffer(() -> В9э, 128, var22)) {
                     this.И(var3, var24);
                     EntityEspOuterGlowRenderer.compositeOuterGlow(this.field_5, var2, var8, this.Вь.getValue(), В9弟 + this.ВИ.getValue() * В9б, 1.0F);
                     this.И(var2, var3, var24);
                  }
               }
            }
         }
      }
   }

   private Vec3 н(AttackEntityEvent var1) {
      Entity var2 = var1.getTarget();
      Vec3 var3 = new Vec3(var2.getX(), var2.getBoundingBox().minY + В9Ю, var2.getZ());
      BlockHitResult var4 = MINECRAFT_CLIENT.level.clip(new ClipContext(var3, var3.add(0.0, В9Ч, 0.0), Block.COLLIDER, Fluid.NONE, var2));
      return var4.getType() == Type.BLOCK ? var4.getLocation().add(0.0, В9Л, 0.0) : this.И(var1);
   }

   @Override
   public void onDisable() {
      this.ст.clear();
      if (this.field_5 != null) {
         this.field_5.destroyBuffers();
         this.field_5 = null;
      }
   }

   private Vec3 И(AttackEntityEvent var1) {
      if (MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var2 && var2.getEntity() == var1.getTarget()) {
         return var2.getLocation();
      } else {
         Entity var5 = var1.getTarget();
         AABB var6 = var5.getBoundingBox().inflate(В9а);
         Vec3 var4 = var1.getAttacker().getEyePosition();
         return var6.clip(var4, var6.getCenter()).orElse(var6.getCenter());
      }
   }

   @Override
   public void onEnable() {
      this.ст.clear();
   }

   private void I0() {
      long var1 = System.currentTimeMillis();
      Iterator var3 = this.ст.iterator();

      while (var3.hasNext()) {
         HitWave.Wave var4 = (HitWave.Wave)var3.next();
         if (var4.world != MINECRAFT_CLIENT.level || (float)(var1 - var4.startedAt) >= (var4.duration + var4.flashDuration) * В9ч) {
            var3.remove();
         }
      }
   }

   private void з(int var1, int var2) {
      if (this.field_5 == null) {
         this.field_5 = new TextureTarget(В9Н, var1, var2, false, GpuFormat.RGBA8_UNORM);
      } else if (this.field_5.width != var1 || this.field_5.height != var2) {
         this.field_5.resize(var1, var2);
      }
   }

   private float И(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.options.cloudStatus().get() == CloudStatus.OFF) {
         return В9н;
      }

      float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(false);
      int var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.CLOUD_COLOR, var2);
      return var3 >>> 24 == 0 ? В9Ц : MINECRAFT_CLIENT.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.CLOUD_HEIGHT, var2);
   }

   private float method_0(double var1, double var3) {
      double var5 = var1 % var3;
      return (float)(var5 < 0.0 ? var5 + var3 : var5);
   }

   private void И(RenderTarget var1, GpuTextureView var2, GpuBuffer var3) {
      try (RenderPass var4 = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> В9Г, var1.getColorTextureView(), Optional.empty())) {
         var4.setPipeline(this.сl);
         var4.bindTexture(В9ъ, var2, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         var4.setUniform(В9М, var3);
         var4.draw(3, 1, 0, 0);
      }
   }

   private void ь(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST
         && var1.getAttacker() == MINECRAFT_CLIENT.player
         && var1.getTarget() != null
         && MINECRAFT_CLIENT.level != null) {
         this.ст
            .add(
               new HitWave.Wave(
                  this.н(var1), System.currentTimeMillis(), this.ВЬ.getValue(), this.Вш.getValue(), ThreadLocalRandom.current().nextFloat(), MINECRAFT_CLIENT.level
               )
            );

         while (this.ст.size() > 6) {
            this.ст.removeFirst();
         }
      }
   }

   private void И(GpuTextureView var1, GpuBuffer var2) {
      try (RenderPass var3 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> В9л, this.field_5.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
         var3.setPipeline(this.сЫ);
         var3.bindTexture(В95, var1, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         var3.setUniform(В9в, var2);
         var3.draw(3, 1, 0, 0);
      }
   }

   private List<HitWave.WaveUniforms> И(long var1, Vec3 var3) {
      ArrayList var4 = new ArrayList(6);

      for (HitWave.Wave var6 : this.ст) {
         if (var6.world == MINECRAFT_CLIENT.level && var4.size() < 6) {
            float var7 = (float)(var1 - var6.startedAt) / В9Ы;
            float var8 = Mth.clamp(var7 / var6.duration, 0.0F, 1.0F);
            float var9 = 1.0F - this.Ц(Mth.clamp((var7 - var6.duration) / Math.max(var6.flashDuration, В9l), 0.0F, 1.0F));
            if (!(var9 <= В98)) {
               Vec3 var10 = var6.position.subtract(var3);
               var4.add(new HitWave.WaveUniforms((float)var10.x, (float)var10.y, (float)var10.z, var8, var9, var6.seed, 0.0F, 0.0F));
            }
         }
      }

      return var4;
   }

   private int тЩ() {
      return this.св.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.ъ.getValue();
   }

   private record WaveUniforms(float x, float y, float z, float progress, float envelope, float seed, float unusedX, float unusedY) {
   }

   private record Wave(Vec3 position, long startedAt, float duration, float flashDuration, float seed, ClientLevel world) {
   }
}
