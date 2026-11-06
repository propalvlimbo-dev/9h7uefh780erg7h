package sg.modules.impl.render;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.impl.EntityRenderStateEvent;
import sg.events.EventListener;
import sg.render.WorldProjection;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public final class ImpactNova extends ClientModule {
   public static final int ДМ = -65282;
   private static final int ДН = 6;
   private static final int Дн = 336;
   private static final float ЪЕ = 0.16F;
   private static final float ЪП = 0.34F;
   private static final float Ъ西 = 0.55F;
   private static final ImpactNova.ImpactUniforms В = new ImpactNova.ImpactUniforms(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   private final SingleChoiceSetting сЧ;
   private final BooleanSetting ВЕ;
   private final ColorSetting Щ;
   private final FloatSetting ХЭ;
   private final FloatSetting Х必;
   private final FloatSetting Хщ;
   private final List<ImpactNova.Impact> сЙ;
   private final Random а;
   private final RenderPipeline с西;
   private final EventListener<AttackEntityEvent> ХЧ;
   private final EventListener<EntityRenderStateEvent> ХЛ;
   private static final String ХЬЛ = "ImpactNova";
   private static final String ХЬ诶 = "Шейдерная накладка на модель после удара";
   private static final String ХЬ1 = "Вид";
   private static final String ХЬЬ = "Импульс";
   private static final String ХЬ衣 = "Сжигание";
   private static final String ХЬш = "Цвет темы";
   private static final String ХЬИ = "Цвет";
   private static final int ХЬь = -7643905;
   private static final String ХЬж = "Масштаб";
   private static final float ХЬ弟 = 0.5F;
   private static final float ХЬб = 0.1F;
   private static final String ХЬЫ = "Сила";
   private static final float ХЬl = 0.3F;
   private static final float ХЬ8 = 0.1F;
   private static final String ХЬ5 = "Длительность";
   private static final float ХЬв = 1.4F;
   private static final float ХЬъ = 0.4F;
   private static final float ХЬМ = 3.0F;
   private static final float ХЬН = 0.1F;
   private static final String ХЬн = "rose/impact_nova_model";
   private static final String ХЬЦ = "hands_fullscreen";
   private static final String ХЬч = "impact_nova_model";
   private static final String ХЬЭ = "ImpactNova";
   private static final String ХЬ必 = "MaskSampler";
   private static final double ХЬщ = 0.001;
   private static final int ХЬГ = -65282;
   private static final double ХЬл = 0.25;
   private static final float ХЬэ = 0.1F;
   private static final float ХЬю = 1000.0F;
   private static final float ХЬЩ = 0.002F;
   private static final float ХЬу = 0.2F;
   private static final float ХЬ2 = 0.2F;
   private static final double ХЬф = 0.5;
   private static final float ХЬи = 1.0E-5F;
   private static final float ХЬ9 = 0.55F;
   private static final float ХЬо = 0.5F;
   private static final float ХЬO = 0.2F;
   private static final float ХЬ7 = 0.2F;
   private static final float ХЬР = 0.16F;
   private static final float ХЬ3 = 0.65999997F;
   private static final float ХЬр = 0.34F;
   private static final float ХЬ6 = 255.0F;
   private static final float ХЬ_ = 255.0F;
   private static final float ХЬШ = 255.0F;
   private static final String ХЬы = "MaskSampler";
   private static final String ХЬй = "ImpactNova";
   private static final String ХЬЖ = "Сжигание";
   private static final float ХЬм = 6.0F;
   private static final float ХЬК = 15.0F;
   private static final float ХЬI = 10.0F;
   private static final float ХЬТ = 0.1F;
   private static final float ХЬО = 1000.0F;
   private static final String ХЬФ = "impactNovaOverlay";
   private static final String ХЬЙ = "impactNovaUniforms";

   private float _I(float var1) {
      return var1 * var1 * var1 * (var1 * (var1 * ХЬм - ХЬК) + ХЬI);
   }

   public ImpactNova() {
      super(ХЬЛ, ХЬ诶, ModuleCategory.RENDER);
      this.сЧ = new SingleChoiceSetting(ХЬ1, new ChoiceOption(ХЬЬ), new ChoiceOption(ХЬ衣));
      this.ВЕ = new BooleanSetting(ХЬш, true);
      this.Щ = new ColorSetting(ХЬИ, false, ХЬь).visibleWhen(() -> !this.ВЕ.getValue());
      this.ХЭ = new FloatSetting(ХЬж, 1.0F, ХЬ弟, 2.0F).withStep(ХЬб);
      this.Х必 = new FloatSetting(ХЬЫ, 1.0F, ХЬl, 2.0F).withStep(ХЬ8);
      this.Хщ = new FloatSetting(ХЬ5, ХЬв, ХЬъ, ХЬМ).withStep(ХЬН);
      this.сЙ = new ArrayList<>();
      this.а = new Random();
      this.с西 = RenderPipelineBuilder.builder()
         .withLocation(ХЬн)
         .withVertexShader(ClientResourceIds.getCoreId(ХЬЦ))
         .withFragmentShader(ClientResourceIds.getCoreId(ХЬч))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(ХЬЭ, UniformType.UNIFORM_BUFFER)
         .withSampler(ХЬ必)
         .build();
      this.ХЧ = this::н;
      this.ХЛ = this::в;
      this.addSettings(this.сЧ, this.ВЕ, this.Щ, this.ХЭ, this.Х必, this.Хщ);
   }

   private void м(RenderTarget var1, RenderTarget var2, List<ImpactNova.ImpactUniforms> var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      GpuBuffer var5 = null;

      try (MemoryStack var6 = MemoryStack.stackPush()) {
         int var7 = this.ВЕ.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.Щ.getValue();
         Std140Builder var8 = Std140Builder.onStack(var6, 336)
            .putVec4(this.зю(), this.Х必.getValue(), var3.size(), this.ХЭ.getValue())
            .putVec4(ColorMath.getRed(var7) / ХЬ6, ColorMath.getGreen(var7) / ХЬ_, ColorMath.getBlue(var7) / ХЬШ, 1.0F)
            .putVec4(var1.width, var1.height, 0.0F, 0.0F);

         for (int var9 = 0; var9 < 6; var9++) {
            ImpactNova.ImpactUniforms var10 = var9 < var3.size() ? (ImpactNova.ImpactUniforms)var3.get(var9) : В;
            var8.putVec4(var10.centerU(), var10.centerV(), var10.blockScale(), var10.seed());
         }

         for (int var24 = 0; var24 < 6; var24++) {
            ImpactNova.ImpactUniforms var27 = var24 < var3.size() ? (ImpactNova.ImpactUniforms)var3.get(var24) : В;
            var8.putVec4(var27.hitLocalX(), var27.hitLocalY(), 0.0F, 0.0F);
         }

         for (int var25 = 0; var25 < 6; var25++) {
            ImpactNova.ImpactUniforms var28 = var25 < var3.size() ? (ImpactNova.ImpactUniforms)var3.get(var25) : В;
            var8.putVec4(var28.progress(), var28.age(), var28.envelope(), var28.growth());
         }

         ByteBuffer var26 = var8.get();
         var5 = RenderSystem.getDevice().createBuffer(() -> ХЬЙ, 128, var26);

         try (RenderPass var29 = var4.createRenderPass(() -> ХЬФ, var2.getColorTextureView(), Optional.empty())) {
            var29.setPipeline(this.с西);
            var29.bindTexture(ХЬы, var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            var29.setUniform(ХЬй, var5);
            var29.draw(3, 1, 0, 0);
         }
      } finally {
         if (var5 != null) {
            var5.close();
         }
      }
   }

   private boolean А(Entity var1) {
      if (var1 != null && MINECRAFT_CLIENT.gameRenderer != null) {
         Vec3 var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         return var1.getBoundingBox().inflate(ХЬл).contains(var2);
      } else {
         return false;
      }
   }

   private Vec3 м(Entity var1) {
      float var2 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      return new Vec3(Mth.lerp(var2, var1.xo, var1.getX()), Mth.lerp(var2, var1.yo, var1.getY()), Mth.lerp(var2, var1.zo, var1.getZ()));
   }

   private void рО() {
      long var1 = System.currentTimeMillis();
      long var3 = (long)(Math.max(this.Хщ.getValue(), ХЬТ) * ХЬО);
      Iterator var5 = this.сЙ.iterator();

      while (var5.hasNext()) {
         ImpactNova.Impact var6 = (ImpactNova.Impact)var5.next();
         if (var6.target == null || var6.target.isRemoved() || var1 - var6.startedAt >= var3) {
            var5.remove();
         }
      }
   }

   public boolean ы(Entity var1) {
      if (this.isEnabled() && var1 != null) {
         if (this.А(var1)) {
            return false;
         }

         this.рО();

         for (ImpactNova.Impact var3 : this.сЙ) {
            if (var3.target == var1) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean фо() {
      this.рО();
      return this.isEnabled() && !this.сЙ.isEmpty();
   }

   private float м(Vec3 var1, float var2, float var3) {
      float var4 = var2 * ХЬо;
      float var5 = Float.POSITIVE_INFINITY;
      float var6 = Float.POSITIVE_INFINITY;
      float var7 = Float.NEGATIVE_INFINITY;
      float var8 = Float.NEGATIVE_INFINITY;
      int var9 = 0;

      for (float var13 : new float[]{0.0F, var3}) {
         for (float var17 : new float[]{-var4, var4}) {
            for (float var21 : new float[]{-var4, var4}) {
               Vector2f var22 = WorldProjection.projectToNormalizedScreen(var1.add(var17, var13, var21), false);
               if (var22 != null) {
                  var5 = Math.min(var5, var22.x);
                  var6 = Math.min(var6, var22.y);
                  var7 = Math.max(var7, var22.x);
                  var8 = Math.max(var8, var22.y);
                  var9++;
               }
            }
         }
      }

      if (var9 < 2) {
         return 0.0F;
      }

      float var23 = (var7 - var5) / Math.max(var2, ХЬO);
      float var24 = (var8 - var6) / Math.max(var3, ХЬ7);
      return Math.max(var23, var24);
   }

   @Override
   public void onDisable() {
      this.сЙ.clear();
   }

   public void Ш(RenderTarget var1, RenderTarget var2) {
      if (this.фо()) {
         long var3 = System.currentTimeMillis();
         float var5 = Math.max(this.Хщ.getValue(), ХЬэ);
         float var6 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         float var7 = (float)var1.width / Math.max(var1.height, 1);
         ArrayList var8 = new ArrayList(6);

         for (ImpactNova.Impact var10 : List.copyOf(this.сЙ)) {
            if (var8.size() >= 6) {
               break;
            }

            if (!this.А(var10.target)) {
               float var11 = (float)(var3 - var10.startedAt) / ХЬю;
               float var12 = Math.clamp(var11 / var5, 0.0F, 1.0F);
               float var13 = this._Ю(var12);
               if (!(var13 <= ХЬЩ)) {
                  ImpactNova.ImpactUniforms var14 = this.м(var10, var6, var7, var12, var11, var13);
                  if (var14 != null) {
                     var8.add(var14);
                  }
               }
            }
         }

         if (!var8.isEmpty()) {
            this.м(var1, var2, var8);
         }
      }
   }

   private float м(Vec3 var1, float var2) {
      float var3 = (float)Math.toRadians(var2);
      Vec3 var4 = new Vec3(Math.cos(var3), 0.0, Math.sin(var3));
      Vec3 var5 = new Vec3(-Math.sin(var3), 0.0, Math.cos(var3));
      Vec3 var6 = new Vec3(0.0, 1.0, 0.0);
      Vector2f var7 = WorldProjection.projectToNormalizedScreen(var1, false);
      if (var7 == null) {
         return 0.0F;
      }

      float var8 = 0.0F;

      for (Vec3 var12 : new Vec3[]{var4, var6, var5}) {
         Vector2f var13 = WorldProjection.projectToNormalizedScreen(var1.add(var12), false);
         if (var13 != null) {
            var8 = Math.max(var8, (float)Math.hypot(var13.x - var7.x, var13.y - var7.y));
         }
      }

      return var8;
   }

   private float _弟(float var1) {
      float var2 = 1.0F - var1;
      return 1.0F - var2 * var2 * var2;
   }

   private float зю() {
      return this.сЧ.isSelected(ХЬЖ) ? 1.0F : 0.0F;
   }

   private ImpactNova.ImpactUniforms м(ImpactNova.Impact var1, float var2, float var3, float var4, float var5, float var6) {
      Entity var7 = var1.target;
      Vec3 var8 = this.м(var7);
      float var9 = Math.max(var7.getBbHeight(), ХЬу);
      float var10 = Math.max(var7.getBbWidth(), ХЬ2);
      Vec3 var11 = var8.add(0.0, var9 * ХЬф, 0.0);
      Vector2f var12 = WorldProjection.projectToNormalizedScreen(var11, true);
      if (var12 == null) {
         return null;
      }

      float var13 = this.м(var11, var7.getYRot(var2));
      var13 = Math.max(var13, this.м(var8, var10, var9));
      if (var13 < ХЬи) {
         return null;
      }

      Vector2f var14 = WorldProjection.projectToNormalizedScreen(var8.add(var1.localHit), false);
      if (var14 == null) {
         return null;
      }

      float var15 = (var14.x - var12.x) * var3 / var13;
      float var16 = (var14.y - var12.y) / var13;
      return new ImpactNova.ImpactUniforms(var12.x, var12.y, var13, var1.seed, var15, var16, var4, var5, var6, this._弟(Math.clamp(var4 / ХЬ9, 0.0F, 1.0F)));
   }

   private void в(EntityRenderStateEvent var1) {
      if (this.isEnabled() && this.ы(var1.getEntity())) {
         var1.getRenderState().outlineColor = ХЬГ;
      }
   }

   private float _Ю(float var1) {
      float var2 = this._I(Math.clamp(var1 / ХЬР, 0.0F, 1.0F));
      float var3 = this._I(Math.clamp((var1 - ХЬ3) / ХЬр, 0.0F, 1.0F));
      return var2 * (1.0F - var3);
   }

   private void н(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getTarget() != null && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         Entity var2 = var1.getTarget();
         Vec3 var3 = this.в(var1).subtract(this.м(var2));
         this.сЙ.removeIf(var1x -> var1x.target == var2);
         this.сЙ.add(new ImpactNova.Impact(var2, var3, System.currentTimeMillis(), this.а.nextFloat()));

         while (this.сЙ.size() > 6) {
            this.сЙ.removeFirst();
         }
      }
   }

   private Vec3 в(AttackEntityEvent var1) {
      if (MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var2 && var2.getEntity() == var1.getTarget()) {
         return var2.getLocation();
      } else {
         AABB var4 = var1.getTarget().getBoundingBox().inflate(ХЬщ);
         Vec3 var5 = var1.getAttacker().getEyePosition();
         return var4.clip(var5, var4.getCenter()).orElse(var4.getCenter());
      }
   }

   private record Impact(Entity target, Vec3 localHit, long startedAt, float seed) {
   }

   private record ImpactUniforms(
      float centerU, float centerV, float blockScale, float seed, float hitLocalX, float hitLocalY, float progress, float age, float envelope, float growth
   ) {
   }
}
