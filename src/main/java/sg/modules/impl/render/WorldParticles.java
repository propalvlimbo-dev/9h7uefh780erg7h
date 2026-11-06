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
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class WorldParticles extends ClientModule {
   private static final float пд = 1.4142135F;
   private static final float[] field_5 = new float[]{WorldParticles.Я_Ф, WorldParticles.Я_Й, WorldParticles.Я_к};
   private static final float[] в_at_b0eff908 = new float[]{WorldParticles.Я_г, WorldParticles.Я_0, WorldParticles.Я_х};
   private static final int[] Ы = new int[]{
      WorldParticles.Я_У,
      -19361,
      -142,
      WorldParticles.Я_З,
      WorldParticles.Я_Е,
      WorldParticles.Я_П,
      WorldParticles.Я_西,
      WorldParticles.Я_д,
      -1,
      WorldParticles.Я_Б
   };
   private final SingleChoiceSetting ц;
   private final SingleChoiceSetting я;
   private final FloatSetting ЯЖ;
   private final FloatSetting Ям;
   private final FloatSetting ЯК;
   private final FloatSetting ЯI;
   private final FloatSetting ЯТ;
   private final SingleChoiceSetting сс;
   private final FloatSetting ЯО;
   private final FloatSetting ЯФ;
   private final FloatSetting ЯЙ;
   private final BooleanSetting Вl;
   private final BooleanSetting В8;
   private final BooleanSetting В5;
   private final BooleanSetting Вв;
   private final ColorSetting ю;
   private final List<WorldParticles.Particle> су;
   private final RenderPipeline с0;
   private final EventListener<PlayerTickEvent> Я必;
   private final EventListener<WorldEffectsEvent> Ящ;
   private Vec3 Ф;
   @SerializedName("Я")
   private WorldParticles.Texture Я_at_b0eff908;
   private final float[] ъ;
   private final float[] М;
   private static final String Ярд = "WorldParticles";
   private static final String ЯрБ = "Частицы вокруг игрока";
   private static final String ЯрС = "Форма";
   private static final String Ярз = "Корона";
   private static final String Ярц = "Взрыв куба";
   private static final String Яря = "Доллар";
   private static final String Я6с = "Сердце";
   private static final String Я6В = "Молния";
   private static final String Я6Я = "Линия";
   private static final String Я6Х = "Ромб";
   private static final String Я6е = "Снежинка";
   private static final String Я6А = "Звезда";
   private static final String Я64 = "Звезда 2";
   private static final String Я6п = "Треугольник";
   private static final String Я6Д = "Свечение";
   private static final String Я6Ъ = "Рандом";
   private static final String Я6т = "Анимация";
   private static final String Я6а = "Обычная";
   private static final String Я6Ю = "Парение";
   private static final String Я6Ч = "Маятник";
   private static final String Я6Л = "Кувырок";
   private static final String Я6诶 = "Пульсация";
   private static final String Я61 = "Скорость анимации";
   private static final float Я6Ь = 0.25F;
   private static final float Я6衣 = 3.0F;
   private static final float Я6ш = 0.05F;
   private static final String Я6И = "Сила анимации";
   private static final float Я6ь = 0.2F;
   private static final float Я6ж = 0.05F;
   private static final String Я6弟 = "Количество";
   private static final float Я6б = 100.0F;
   private static final float Я6Ы = 10.0F;
   private static final float Я6l = 300.0F;
   private static final String Я68 = "Время жизни";
   private static final float Я65 = 10.0F;
   private static final float Я6в = 60.0F;
   private static final String Я6ъ = "Размер";
   private static final float Я6М = 1.5F;
   private static final float Я6Н = 0.1F;
   private static final float Я6н = 1.5F;
   private static final float Я6Ц = 0.05F;
   private static final String Я6ч = "Тип свечения";
   private static final String Я6Э = "Тип 1";
   private static final String Я6必 = "Тип 2";
   private static final String Я6щ = "Свечение";
   private static final float Я6Г = 1.5F;
   private static final float Я6л = 0.1F;
   private static final float Я6э = 5.0F;
   private static final float Я6ю = 0.1F;
   private static final String Я6Щ = "Размер свечения";
   private static final float Я6у = 3.0F;
   private static final String Я62 = "Дальность";
   private static final float Я6ф = 48.0F;
   private static final float Я6и = 16.0F;
   private static final float Я69 = 96.0F;
   private static final String Я6о = "Физика";
   private static final String Я6O = "Анимированная альфа";
   private static final String Я67 = "Случайный цвет";
   private static final String Я6Р = "Авто цвет под тему";
   private static final String Я63 = "Цвет";
   private static final int Я6р = -10962689;
   private static final String Я66 = "rose/world_particles";
   private static final String Я6_ = "dead_chams";
   private static final String Я6Ш = "dead_chams";
   private static final String Я6ы = "Sampler0";
   private static final String Я6й = "Парение";
   private static final String Я6Ж = "Маятник";
   private static final String Я6м = "Кувырок";
   private static final String Я6К = "Пульсация";
   private static final float Я6I = 0.25F;
   private static final float Я6Т = 0.22F;
   private static final float Я6О = 12.0F;
   private static final String Я6Ф = "Тип 2";
   private static final float Я6Й = 0.25F;
   private static final float Я6к = 0.25F;
   private static final float Я6г = 0.36F;
   private static final float Я60 = 1.4142135F;
   private static final double Я6х = 0.08;
   private static final double Я6У = 0.015;
   private static final double Я6З = 3.0;
   private static final double Я6Е = 5.0;
   private static final double Я6П = 0.72;
   private static final double Я6西 = Math.PI * 2;
   private static final double Я6д = 0.015;
   private static final double Я6Б = 1.0E-4;
   private static final double Я6С = 0.45;
   private static final double Я6з = 0.45;
   private static final double Я6ц = -1.25;
   private static final double Я6я = 1.25;
   private static final double Я_с = 4.0;
   private static final double Я_В = 20.0;
   private static final float Я_Я = 0.25F;
   private static final double Я_Х = -0.035;
   private static final double Я_е = 0.035;
   private static final double Я_А = -0.006;
   private static final double Я_4 = 0.018;
   private static final double Я_п = -0.035;
   private static final double Я_Д = 0.035;
   private static final float Я_Ъ = 1000.0F;
   private static final String Я_т = "Корона";
   private static final String Я_а = "Взрыв куба";
   private static final String Я_Ю = "Доллар";
   private static final String Я_Ч = "Сердце";
   private static final String Я_Л = "Молния";
   private static final String Я_诶 = "Линия";
   private static final String Я_1 = "Ромб";
   private static final String Я_Ь = "Снежинка";
   private static final String Я_衣 = "Звезда";
   private static final String Я_ш = "Звезда 2";
   private static final String Я_И = "Треугольник";
   private static final String Я_ь = "Свечение";
   private static final String Я_ж = "Рандом";
   private static final String Я_弟 = "Тип 2";
   private static final String Я_б = "Тип 2";
   private static final double Я_Ы = 0.04;
   private static final double Я_l = 200.0;
   private static final double Я_8 = 2.0;
   private static final float Я_5 = 0.01F;
   private static final float Я_в = (float) (Math.PI / 180.0);
   private static final float Я_ъ = 0.001F;
   private static final float Я_М = 0.22F;
   private static final float Я_Н = 2.4F;
   private static final float Я_н = 0.72F;
   private static final float Я_Ц = 0.5F;
   private static final float Я_ч = 1.4142135F;
   private static final float Я_Э = 0.01F;
   private static final float Я_必 = 0.34F;
   private static final float Я_щ = 255.0F;
   private static final float Я_Г = 0.001F;
   private static final float Я_л = 18.0F;
   private static final float Я_э = 0.8F;
   private static final float Я_ю = 0.001F;
   private static final float Я_Щ = 55.0F;
   private static final float Я_у = 0.001F;
   private static final float Я_2 = 150.0F;
   private static final float Я_ф = 75.0F;
   private static final float Я_и = 1.7F;
   private static final float Я_9 = 0.001F;
   private static final float Я_о = 150.0F;
   private static final float Я_O = 95.0F;
   private static final float Я_7 = 1.1F;
   private static final String Я_Р = "Sampler0";
   private static final float Я_3 = (float) (Math.PI / 180.0);
   private static final float Я_р = (float) (Math.PI / 180.0);
   private static final float Я_6 = 0.5F;
   private static final float Я__ = 255.0F;
   private static final float Я_Ш = 255.0F;
   private static final float Я_ы = 255.0F;
   private static final float Я_й = 255.0F;
   private static final String Я_Ж = "worldParticles";
   private static final String Я_м = "worldParticlesVertices";
   private static final String Я_К = "Тип 2";
   private static final String Я_I = "Тип 2";
   private static final String Я_Т = "Обычная";
   private static final String Я_О = "Обычная";
   private static final float Я_Ф = 1.5F;
   private static final float Я_Й = 2.25F;
   private static final float Я_к = 3.375F;
   private static final float Я_г = 0.7F;
   private static final float Я_0 = 0.45F;
   private static final float Я_х = 0.28F;
   private static final int Я_У = -41121;
   private static final int Я_З = -9830517;
   private static final int Я_Е = -9901825;
   private static final int Я_П = -9202689;
   private static final int Я_西 = -5018625;
   private static final int Я_д = -35610;
   private static final int Я_Б = -10092590;

   private void МУ() {
      this.су.clear();
      this.Ф = null;
      this.Я_at_b0eff908 = null;

      for (int var1 = 0; var1 < this.ъ.length; var1++) {
         this.ъ[var1] = this.М[var1] = var1 == 0 ? 1.0F : 0.0F;
      }
   }

   private void Ф(BufferBuilder var1, Identifier var2, int var3) {
      MeshData var4 = var1.build();
      if (var4 != null) {
         if (var3 == 0) {
            var4.close();
         } else {
            GpuBuffer var5 = null;

            try {
               DrawState var6 = var4.drawState();
               int var7 = var6.indexCount();
               AutoStorageIndexBuffer var8 = RenderSystem.getSequentialBuffer(var6.primitiveTopology());
               GpuBuffer var9 = var8.getBuffer(var7);
               IndexType var10 = var8.type();
               var5 = RenderSystem.getDevice().createBuffer(() -> Я_м, 32, var4.vertexBuffer());
               RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var13 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var14 = var12.createRenderPass(
                     () -> Я_Ж, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var14);
                  var14.setPipeline(this.с0);
                  var14.bindTexture(Я_Р, var13.getTextureView(), var13.getSampler());
                  var14.setVertexBuffer(0, var5.slice());
                  var14.setIndexBuffer(var9, var10);
                  var14.drawIndexed(var7, 1, 0, 0, 0);
               }
            } finally {
               if (var5 != null) {
                  var5.close();
               }

               var4.close();
            }
         }
      }
   }

   private void к(BufferBuilder var1, Matrix4f var2, float var3, int var4) {
      float var5 = var3 * Я_6;
      float var6 = ColorMath.getRed(var4) / Я__;
      float var7 = ColorMath.getGreen(var4) / Я_Ш;
      float var8 = ColorMath.getBlue(var4) / Я_ы;
      float var9 = ColorMath.getAlpha(var4) / Я_й;
      var1.addVertex(var2, -var5, -var5, 0.0F).setUv(0.0F, 0.0F).setColor(var6, var7, var8, var9);
      var1.addVertex(var2, -var5, var5, 0.0F).setUv(0.0F, 1.0F).setColor(var6, var7, var8, var9);
      var1.addVertex(var2, var5, var5, 0.0F).setUv(1.0F, 1.0F).setColor(var6, var7, var8, var9);
      var1.addVertex(var2, var5, -var5, 0.0F).setUv(1.0F, 0.0F).setColor(var6, var7, var8, var9);
   }

   private void method__(
      BufferBuilder var1,
      Matrix4f var2,
      Matrix4f var3,
      Matrix4f var4,
      double var5,
      double var7,
      double var9,
      float var11,
      float var12,
      float var13,
      float var14,
      int var15
   ) {
      var4.set(var2).translate((float)var5, (float)var7, (float)var9).mul(var3).rotateZ((float)Math.toRadians(var12));
      if (var13 != 0.0F) {
         var4.rotateX(var13 * Я_3);
      }

      if (var14 != 0.0F) {
         var4.rotateY(var14 * Я_р);
      }

      this.к(var1, var4, var11, var15);
   }

   private void method__(
      Identifier var1,
      WorldParticles.Texture var2,
      boolean var3,
      float var4,
      Vec3 var5,
      double var6,
      float var8,
      boolean var9,
      boolean var10,
      int var11,
      long var12,
      Matrix4f var14,
      Matrix4f var15,
      Matrix4f var16
   ) {
      BufferBuilder var17 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var18 = 0;
      float var19 = this.Ям.getValue();
      float var20 = Mth.lerp(var4, this.М[1], this.ъ[1]) * var19;
      float var21 = Mth.lerp(var4, this.М[2], this.ъ[2]);
      float var22 = Mth.lerp(var4, this.М[3], this.ъ[3]);
      float var23 = Mth.lerp(var4, this.М[4], this.ъ[4]) * var19;
      int var24 = !var3 && this.сс.isSelected(Я_б) ? this._е() : 0;

      for (WorldParticles.Particle var26 : this.су) {
         if (var2 == null || var26.В == var2) {
            double var27 = Mth.lerp(var4, var26.сШ, var26.ср);
            double var29 = Mth.lerp(var4, var26.сы, var26.с6);
            double var31 = Mth.lerp(var4, var26.сй, var26.с_);
            double var33 = var27 - var5.x;
            double var35 = var29 - var5.y;
            double var37 = var31 - var5.z;
            double var39 = var33 * var33 + var35 * var35 + var37 * var37;
            if (!(var39 > var6) && !(var39 < Я_Ы)) {
               float var41 = var26.а(var12);
               if (var9) {
                  var41 *= (float)((Math.sin((var12 - var26.Ч()) / Я_l) + 1.0) / Я_8);
               }

               if (!(var41 <= Я_5)) {
                  float var42 = Mth.lerp(var4, var26.Х7, var26.ХO);
                  float var43 = var26.Хи * Я_в;
                  float var44 = var23 > Я_ъ ? 1.0F + Я_М * var23 * (float)Math.sin(var42 * Я_Н + var43) : 1.0F;
                  float var45 = var3 ? var26.Хф * var8 * Я_н : var26.Хф * Я_Ц;
                  var45 *= var44;
                  var45 = Math.min(var45, var26.Хо * Я_ч);
                  if (!(var45 <= Я_Э)) {
                     float var46 = var3 ? var41 * Я_必 : var41;
                     int var47 = var26.ф(var11, var10);
                     var47 = ColorMath.packArgb(ColorMath.getRed(var47), ColorMath.getGreen(var47), ColorMath.getBlue(var47), Я_щ * var46);
                     float var48 = var26.я(var12);
                     if (var20 > Я_Г) {
                        var48 += var20 * Я_л * (float)Math.sin(var42 * Я_э + var43);
                     }

                     if (var21 > Я_ю) {
                        float var49 = var26.Хи + Я_Щ * var19 * (float)Math.sin(var42 * 2.0F + var43);
                        var48 += Mth.wrapDegrees(var49 - var48) * var21;
                     }

                     float var57 = !var3 && var22 > Я_у ? var22 * Math.min(Я_2, Я_ф * var19) * (float)Math.sin(var42 * Я_и + var43) : 0.0F;
                     float var50 = !var3 && var22 > Я_9 ? var22 * Math.min(Я_о, Я_O * var19) * (float)Math.cos(var42 * Я_7 + var43) : 0.0F;
                     this.method__(var17, var14, var15, var16, var33, var35, var37, var45, var48, var57, var50, var47);
                     var18 += 4;

                     for (int var51 = 0; var51 < var24; var51++) {
                        float var52 = var45 * field_5[var51];
                        int var53 = ColorMath.withAlpha(var47, var46 * в_at_b0eff908[var51]);
                        if (ColorMath.getAlpha(var53) != 0) {
                           this.к(var17, var16, var52, var53);
                           var18 += 4;
                        }
                     }
                  }
               }
            }
         }
      }

      this.Ф(var17, var1, var18);
   }

   private int _е() {
      return Math.clamp(Math.round(this.ЯФ.getValue()), 1, field_5.length);
   }

   public WorldParticles() {
      super(Ярд, ЯрБ, ModuleCategory.RENDER);
      this.ц = new SingleChoiceSetting(
         ЯрС,
         8,
         new ChoiceOption(Ярз),
         new ChoiceOption(Ярц),
         new ChoiceOption(Яря),
         new ChoiceOption(Я6с),
         new ChoiceOption(Я6В),
         new ChoiceOption(Я6Я),
         new ChoiceOption(Я6Х),
         new ChoiceOption(Я6е),
         new ChoiceOption(Я6А),
         new ChoiceOption(Я64),
         new ChoiceOption(Я6п),
         new ChoiceOption(Я6Д),
         new ChoiceOption(Я6Ъ)
      );
      this.я = new SingleChoiceSetting(
         Я6т, new ChoiceOption(Я6а), new ChoiceOption(Я6Ю), new ChoiceOption(Я6Ч), new ChoiceOption(Я6Л), new ChoiceOption(Я6诶)
      );
      this.ЯЖ = new FloatSetting(Я61, 1.0F, Я6Ь, Я6衣).withStep(Я6ш).visibleWhen(() -> !this.я.isSelected(Я_О));
      this.Ям = new FloatSetting(Я6И, 1.0F, Я6ь, 2.0F).withStep(Я6ж).visibleWhen(() -> !this.я.isSelected(Я_Т));
      this.ЯК = new FloatSetting(Я6弟, Я6б, Я6Ы, Я6l).withStep(1.0F);
      this.ЯI = new FloatSetting(Я68, Я65, 2.0F, Я6в).withStep(1.0F);
      this.ЯТ = new FloatSetting(Я6ъ, Я6М, Я6Н, Я6н).withStep(Я6Ц);
      this.сс = new SingleChoiceSetting(Я6ч, new ChoiceOption(Я6Э), new ChoiceOption(Я6必));
      this.ЯО = new FloatSetting(Я6щ, Я6Г, Я6л, Я6э).withStep(Я6ю).visibleWhen(() -> !this.сс.isSelected(Я_I));
      this.ЯФ = new FloatSetting(Я6Щ, 2.0F, 1.0F, Я6у).withStep(1.0F).visibleWhen(() -> this.сс.isSelected(Я_К));
      this.ЯЙ = new FloatSetting(Я62, Я6ф, Я6и, Я69).withStep(1.0F);
      this.Вl = new BooleanSetting(Я6о, false);
      this.В8 = new BooleanSetting(Я6O, false);
      this.В5 = new BooleanSetting(Я67, false);
      this.Вв = new BooleanSetting(Я6Р, true).visibleWhen(() -> !this.В5.getValue());
      this.ю = new ColorSetting(Я63, false, Я6р).visibleWhen(() -> !this.В5.getValue() && !this.Вв.getValue());
      this.су = new ArrayList<>();
      this.с0 = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Я66)
         .withVertexShader(ClientResourceIds.getCoreId(Я6_))
         .withFragmentShader(ClientResourceIds.getCoreId(Я6Ш))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Я6ы)
         .build();
      this.Я必 = this::зх;
      this.Ящ = this::м;
      this.ъ = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      this.М = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      this.addSettings(this.ц, this.я, this.ЯЖ, this.Ям, this.ЯК, this.ЯI, this.ЯТ, this.сс, this.ЯО, this.ЯФ, this.ЯЙ, this.Вl, this.В8, this.В5, this.Вв, this.ю);
   }

   private WorldParticles.Texture method__() {
      return switch (this.ц.getSelectedOption().getName()) {
         case Я_т -> WorldParticles.Texture.CROWN;
         case Я_а -> WorldParticles.Texture.CUBE_BLAST;
         case Я_Ю -> WorldParticles.Texture.DOLLAR;
         case Я_Ч -> WorldParticles.Texture.HEART;
         case Я_Л -> WorldParticles.Texture.LIGHTNING;
         case Я_诶 -> WorldParticles.Texture.LINE;
         case Я_1 -> WorldParticles.Texture.RHOMBUS;
         case Я_Ь -> WorldParticles.Texture.SNOWFLAKE;
         case Я_衣 -> WorldParticles.Texture.STAR;
         case Я_ш -> WorldParticles.Texture.STAR_ALT;
         case Я_И -> WorldParticles.Texture.TRIANGLE;
         case Я_ь -> WorldParticles.Texture.GLOW;
         case Я_ж -> WorldParticles.Texture.RANDOM;
         default -> WorldParticles.Texture.STAR;
      };
   }

   private void м(WorldEffectsEvent var1) {
      if (!this.су.isEmpty() && MINECRAFT_CLIENT.player != null) {
         WorldParticles.Texture var2 = this.Я_at_b0eff908;
         float var3 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         float var5 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         float var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot();
         double var7 = this.ЯЙ.getValue().floatValue();
         double var9 = var7 * var7;
         float var11 = this.ЯО.getValue();
         boolean var12 = this.В8.getValue();
         boolean var13 = this.В5.getValue();
         int var14 = this.Вв.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.ю.getValue();
         long var15 = System.currentTimeMillis();
         Matrix4f var17 = var1.getPoses().last().pose();
         Matrix4f var18 = new Matrix4f().rotateY((float)Math.toRadians(-var5)).rotateX((float)Math.toRadians(var6));
         Matrix4f var19 = new Matrix4f();
         if (var2 == WorldParticles.Texture.RANDOM) {
            for (WorldParticles.Texture var23 : WorldParticles.Texture.В) {
               this.method__(var23.field_0, var23, false, var3, var4, var9, var11, var12, var13, var14, var15, var17, var18, var19);
            }
         } else if (var2 != null) {
            this.method__(var2.field_0, var2, false, var3, var4, var9, var11, var12, var13, var14, var15, var17, var18, var19);
         }

         if (!this.сс.isSelected(Я_弟)) {
            this.method__(WorldParticles.Texture.г, null, true, var3, var4, var9, var11, var12, var13, var14, var15, var17, var18, var19);
         }
      }
   }

   private void зх(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         long var2 = System.currentTimeMillis();

         byte var4 = switch (this.я.getSelectedOption().getName()) {
            case Я6й -> 1;
            case Я6Ж -> 2;
            case Я6м -> 3;
            case Я6К -> 4;
            default -> 0;
         };

         for (int var21 = 0; var21 < this.ъ.length; var21++) {
            this.М[var21] = this.ъ[var21];
            this.ъ[var21] = this.ъ[var21] + ((var21 == var4 ? 1 : 0) - this.ъ[var21]) * Я6I;
         }

         float var22 = this.Ям.getValue();
         float var23 = 1.0F + Я6Т * var22 * this.ъ[4];
         Vec3 var7 = MINECRAFT_CLIENT.player.position();
         Vec3 var8 = this.Ф == null ? Vec3.ZERO : var7.subtract(this.Ф);
         this.Ф = var7;
         WorldParticles.Texture var9 = this.method__();
         if (this.Я_at_b0eff908 != var9) {
            this.су.clear();
            this.Я_at_b0eff908 = var9;
         }

         double var10 = this.ЯЙ.getValue() + Я6О;
         double var12 = var10 * var10;
         float var14 = this.сс.isSelected(Я6Ф) ? Я6Й * field_5[this._е() - 1] : Math.max(Я6к, this.ЯО.getValue() * Я6г);
         Iterator var15 = this.су.iterator();

         while (var15.hasNext()) {
            WorldParticles.Particle var16 = (WorldParticles.Particle)var15.next();
            var16.ф(this.Вl.getValue(), var16.Хф * var14 * Я60 * var23, this.ЯЖ.getValue(), this.ъ[1] * var22);
            if (var16.Ж(var2) || var16.ф(var7) > var12) {
               var15.remove();
            }
         }

         int var24 = Math.round(this.ЯК.getValue());
         if (this.су.size() < var24) {
            double var17 = var8.horizontalDistance();
            int var19 = var17 > Я6х ? 3 : (var17 > Я6У ? 2 : 1);
            var19 = Math.min(var19, var24 - this.су.size());

            for (int var20 = 0; var20 < var19; var20++) {
               this.method__(var7, var8, var17, var9, var2);
            }
         }
      } else {
         this.МУ();
      }
   }

   private void method__(Vec3 var1, Vec3 var2, double var3, WorldParticles.Texture var5, long var6) {
      ThreadLocalRandom var8 = ThreadLocalRandom.current();
      WorldParticles.Texture var9 = var5 == WorldParticles.Texture.RANDOM ? WorldParticles.Texture.Р() : var5;
      int var10 = Ы[var8.nextInt(Ы.length)];
      float var11 = this.ЯТ.getValue();

      for (int var12 = 0; var12 < 8; var12++) {
         double var13 = Я6З + var8.nextDouble() * Math.max(Я6Е, this.ЯЙ.getValue().floatValue() * Я6П);
         double var15 = var8.nextDouble(Я6西);
         double var17 = var1.x;
         double var19 = var1.z;
         if (var3 > Я6д) {
            double var21 = Math.max(var2.horizontalDistance(), Я6Б);
            var17 += var2.x / var21 * var13 * Я6С;
            var19 += var2.z / var21 * var13 * Я6з;
            double var23 = Math.atan2(var2.z, var2.x);
            var15 = var23 + var8.nextDouble(Я6ц, Я6я);
         }

         double var27 = var17 + Math.cos(var15) * var13;
         double var28 = var1.y - Я_с + var8.nextDouble(Я_В);
         double var25 = var19 + Math.sin(var15) * var13;
         if (WorldParticles.Particle.б(var27, var28, var25, var11 * Я_Я)) {
            this.су
               .add(
                  new WorldParticles.Particle(
                     var27,
                     var28,
                     var25,
                     var8.nextDouble(Я_Х, Я_е),
                     var8.nextDouble(Я_А, Я_4),
                     var8.nextDouble(Я_п, Я_Д),
                     var6,
                     (long)(this.ЯI.getValue() * Я_Ъ),
                     var11,
                     var10,
                     var9
                  )
               );
            return;
         }
      }
   }

   @Override
   public void onDisable() {
      this.МУ();
   }

   private static final class Particle {
      private double ср;
      private double с6;
      private double с_;
      private double сШ;
      private double сы;
      private double сй;
      private double сЖ;
      private double см;
      private double сК;
      private final long сП;
      private final long с西;
      private final float Хф;
      private final int ХЪ;
      private final WorldParticles.Texture В;
      private final float Хи;
      private final float Х9;
      private float Хо;
      private int Хт;
      private float ХO;
      private float Х7;
      private static final float Х5и = 360.0F;
      private static final float Х59 = 0.5F;
      private static final float Х5о = 1.5F;
      private static final float Х5O = 0.25F;
      private static final float Х57 = 1.4142135F;
      private static final float Х5Р = 0.05F;
      private static final double Х53 = 5.94E-4;
      private static final float Х5р = 0.25F;
      private static final double Х56 = 2.9701;
      private static final double Х5_ = 2.9701;
      private static final double Х5Ш = 2.9701;
      private static final float Х5ы = 0.001F;
      private static final double Х5й = Math.PI;
      private static final double Х5Ж = 180.0;
      private static final double Х5м = 0.9;
      private static final double Х5К = 0.9;
      private static final double Х5I = 0.8;
      private static final double Х5Т = 1.3;
      private static final double Х5О = 1.3;
      private static final double Х5Ф = 0.55;
      private static final double Х5Й = 0.7;
      private static final double Х5к = 0.7;
      private static final double Х5г = 0.65;
      private static final double Х50 = -0.32;
      private static final double Х5х = 0.003;
      private static final double Х5У = 0.28;
      private static final double Х5З = 0.012;
      private static final double Х5Е = 0.28;
      private static final double Х5П = 0.88;
      private static final double Х5西 = 0.88;
      private static final double Х5д = -0.32;
      private static final double Х5Б = 0.970299;
      private static final double Х5С = 0.970299;
      private static final double Х5з = 0.970299;
      private static final float Х5ц = 0.5F;
      private static final float Х5я = 0.92F;
      private static final float Хвс = 600.0F;
      private static final float ХвВ = 400.0F;
      private static final long ХвЯ = 400L;
      private static final float ХвХ = 0.06F;
      private static final float Хве = 0.5F;
      private static final float ХвА = -2.0F;
      private static final double Хв4 = 2.0;
      private static final float Хвп = 0.5F;

      private float я(long var1) {
         return this.Хи + this.Х9 * (float)(var1 - this.сП) * ХвХ;
      }

      private static float ф(double var0, double var2, double var4, float var6) {
         if (б(var0, var2, var4, var6)) {
            return var6;
         }

         float var7 = 0.0F;
         float var8 = var6;

         for (int var9 = 0; var9 < 5; var9++) {
            float var10 = (var7 + var8) * Х5ц;
            if (б(var0, var2, var4, var10)) {
               var7 = var10;
            } else {
               var8 = var10;
            }
         }

         return var7 * Х5я;
      }

      private static boolean б(double var0, double var2, double var4, double var6) {
         return MinecraftAccess.MINECRAFT_CLIENT.level == null
            ? false
            : MinecraftAccess.MINECRAFT_CLIENT.level.noCollision(new AABB(var0 - var6, var2 - var6, var4 - var6, var0 + var6, var2 + var6, var4 + var6));
      }

      private float а(long var1) {
         long var3 = var1 - this.сП;
         float var5 = this.у(Mth.clamp((float)var3 / Хвс, 0.0F, 1.0F));
         float var6 = 1.0F - this.у(Mth.clamp((float)(var3 - this.с西) / ХвВ, 0.0F, 1.0F));
         return var5 * var6;
      }

      private int ф(int var1, boolean var2) {
         return var2 ? this.ХЪ : var1;
      }

      private Particle(
         double var1,
         double var3,
         double var5,
         double var7,
         double var9,
         double var11,
         long var13,
         long var15,
         float var17,
         int var18,
         WorldParticles.Texture var19
      ) {
         this.Хи = ThreadLocalRandom.current().nextFloat() * Х5и;
         this.Х9 = Х59 + ThreadLocalRandom.current().nextFloat() * Х5о;
         this.ср = this.сШ = var1;
         this.с6 = this.сы = var3;
         this.с_ = this.сй = var5;
         this.сЖ = var7;
         this.см = var9;
         this.сК = var11;
         this.сП = var13;
         this.с西 = var15;
         this.Хф = var17;
         this.ХЪ = var18;
         this.В = var19;
         this.Хо = var17 * Х5O * Х57;
      }

      private void ф(boolean var1, float var2, float var3, float var4) {
         this.сШ = this.ср;
         this.сы = this.с6;
         this.сй = this.с_;
         this.Х7 = this.ХO;
         this.ХO = this.ХO + Х5Р * var3;
         if (var1) {
            this.см = this.см - Х53;
         }

         double var5 = this.Хф * Х5р;
         double var7 = this.сЖ * Х56;
         double var9 = this.см * Х5_;
         double var11 = this.сК * Х5Ш;
         if (var4 > Х5ы) {
            double var13 = this.Хи * Х5й / Х5Ж;
            var7 += (Math.sin(this.ХO * Х5м + var13) - Math.sin(this.Х7 * Х5К + var13)) * Х5I * var4;
            var9 += (Math.sin(this.ХO * Х5Т + var13) - Math.sin(this.Х7 * Х5О + var13)) * Х5Ф * var4;
            var11 += (Math.cos(this.ХO * Х5Й + var13) - Math.cos(this.Х7 * Х5к + var13)) * Х5г * var4;
         }

         if (б(this.ср + var7, this.с6 + var9, this.с_ + var11, var5)) {
            this.ср += var7;
            this.с6 += var9;
            this.с_ += var11;
         } else {
            if (б(this.ср + var7, this.с6, this.с_, var5)) {
               this.ср += var7;
            } else {
               this.сЖ = this.сЖ * Х50;
            }

            if (б(this.ср, this.с6 + var9, this.с_, var5)) {
               this.с6 += var9;
            } else {
               double var15 = Math.abs(this.см);
               this.см = !(var9 < 0.0) || !var1 ? -this.см * Х5Е : (var15 > Х5х ? Math.min(var15 * Х5У, Х5З) : 0.0);
               this.сЖ = this.сЖ * Х5П;
               this.сК = this.сК * Х5西;
            }

            if (б(this.ср, this.с6, this.с_ + var11, var5)) {
               this.с_ += var11;
            } else {
               this.сК = this.сК * Х5д;
            }
         }

         this.сЖ = this.сЖ * Х5Б;
         this.см = this.см * Х5С;
         this.сК = this.сК * Х5з;
         if (this.Хт-- <= 0) {
            this.Хо = ф(this.ср, this.с6, this.с_, Math.max((float)var5, var2));
            this.Хт = 4;
         }
      }

      private boolean Ж(long var1) {
         return var1 - this.сП > this.с西 + ХвЯ;
      }

      private long Ч() {
         return this.сП;
      }

      private float у(float var1) {
         return var1 < Хве ? 2.0F * var1 * var1 : 1.0F - (float)Math.pow(ХвА * var1 + 2.0F, Хв4) * Хвп;
      }

      private double ф(Vec3 var1) {
         double var2 = this.ср - var1.x;
         double var4 = this.с6 - var1.y;
         double var6 = this.с_ - var1.z;
         return var2 * var2 + var4 * var4 + var6 * var6;
      }
   }

   private enum Texture {
      CROWN("crown.png"),
      CUBE_BLAST("cubeblast1.png"),
      DOLLAR("dollar.png"),
      HEART("heart.png"),
      LIGHTNING("lightning.png"),
      LINE("line.png"),
      RHOMBUS("rhombus.png"),
      SNOWFLAKE("snowflake.png"),
      STAR("star.png"),
      STAR_ALT("star1.png"),
      TRIANGLE("triangle.png"),
      GLOW("glow.png"),
      RANDOM(null);

      private static final WorldParticles.Texture[] В = new WorldParticles.Texture[]{
         CROWN, CUBE_BLAST, DOLLAR, HEART, LIGHTNING, LINE, RHOMBUS, SNOWFLAKE, STAR, STAR_ALT, TRIANGLE, GLOW
      };
      private static final Identifier г = Identifier.fromNamespaceAndPath("rose", "textures/world/dashbloomsample.png");
      @SerializedName("0")
      private final Identifier field_0;

      Texture(String var3) {
         this.field_0 = var3 == null ? null : Identifier.fromNamespaceAndPath("rose", "textures/world/" + var3);
      }

      private static WorldParticles.Texture Р() {
         return В[ThreadLocalRandom.current().nextInt(В.length)];
      }
   }
}
