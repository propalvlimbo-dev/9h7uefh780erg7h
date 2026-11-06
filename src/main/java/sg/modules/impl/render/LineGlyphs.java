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
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public class LineGlyphs extends ClientModule {
   private static final Vec3i[] В_a1351bad = new Vec3i[]{
      new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(0, -1, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1)
   };
   private static final float тЗ = 0.02F;
   private final FloatSetting Хк;
   private final BooleanSetting ЯЧ;
   private final BooleanSetting ЯЛ;
   private final FloatSetting Хг;
   private final Random Ч;
   private final List<LineGlyphs.GlyphPath> сц;
   private final List<LineGlyphs.GlyphPath> ся;
   private final List<LineGlyphs.ColoredPoint> Вс;
   private int Дд;
   private final RenderPipeline Ве;
   private final RenderPipeline ВА;
   @SerializedName("В")
   private final LineGlyphs.LineStyle[] В_6034b685;
   private final EventListener<PlayerTickEvent> Хи;
   private final EventListener<WorldEffectsEvent> Х9;
   private static final String ВЙб = "LineGlyphs";
   private static final String ВЙЫ = "Неоновые ломаные линии вокруг игрока";
   private static final String ВЙl = "Количество Линий";
   private static final float ВЙ8 = 70.0F;
   private static final float ВЙ5 = 5.0F;
   private static final float ВЙв = 200.0F;
   private static final float ВЙъ = 5.0F;
   private static final String ВЙМ = "Медленная Скорость";
   private static final String ВЙН = "Пунктирные Линии";
   private static final String ВЙн = "Шаг Пунктира";
   private static final float ВЙЦ = 3.0F;
   private static final float ВЙч = 0.5F;
   private static final float ВЙЭ = 20.0F;
   private static final float ВЙ必 = 0.5F;
   private static final long ВЙщ = 93882L;
   private static final String ВЙГ = "rose/line_glyphs_glow";
   private static final String ВЙл = "target_esp_crystal";
   private static final String ВЙэ = "target_esp_crystal";
   private static final String ВЙю = "rose/line_glyphs_core";
   private static final String ВЙЩ = "target_esp_crystal";
   private static final String ВЙу = "target_esp_crystal";
   private static final float ВЙ2 = 4.0F;
   private static final float ВЙф = 0.08F;
   private static final float ВЙи = 2.5F;
   private static final float ВЙ9 = 0.15F;
   private static final float ВЙо = 1.5F;
   private static final float ВЙO = 0.25F;
   private static final float ВЙ7 = 0.01F;
   private static final float ВЙР = 0.04F;
   private static final float ВЙ3 = 0.08F;
   private static final float ВЙр = 0.85F;
   private static final float ВЙ6 = 0.01F;
   private static final float ВЙ_ = 0.25F;
   private static final float ВЙШ = 1.75F;
   private static final float ВЙы = 0.018F;
   private static final float ВЙй = 0.01F;
   private static final float ВЙЖ = 0.25F;
   private static final float ВЙм = 1.75F;
   private static final float ВЙК = 255.0F;
   private static final double ВЙI = 1.0E-4;
   private static final float ВЙТ = -135.0F;
   private static final float ВЙО = 135.0F;
   private static final float ВЙФ = 0.02F;
   private static final float ВЙЙ = 0.5F;
   private static final double ВЙк = 1.0E-10;
   private static final double ВЙг = 0.5;
   private static final double ВЙ0 = 0.5;
   private static final double ВЙх = 0.5;
   private static final double ВЙУ = 1.0E-8;
   private static final double ВЙЗ = 1.0E-8;
   private static final float ВЙЕ = 255.0F;
   private static final float ВЙП = 255.0F;
   private static final float ВЙ西 = 255.0F;
   private static final float ВЙд = 255.0F;
   private static final String ВЙБ = "lineGlyphs";
   private static final String ВЙС = "lineGlyphsVertices";

   private void д(List<LineGlyphs.ColoredPoint> var1, Vec3 var2, int var3, float var4) {
      var1.add(new LineGlyphs.ColoredPoint(var2.x - var4, var2.y, var2.z, var3));
      var1.add(new LineGlyphs.ColoredPoint(var2.x + var4, var2.y, var2.z, var3));
      var1.add(new LineGlyphs.ColoredPoint(var2.x, var2.y - var4, var2.z, var3));
      var1.add(new LineGlyphs.ColoredPoint(var2.x, var2.y + var4, var2.z, var3));
      var1.add(new LineGlyphs.ColoredPoint(var2.x, var2.y, var2.z - var4, var3));
      var1.add(new LineGlyphs.ColoredPoint(var2.x, var2.y, var2.z + var4, var3));
   }

   private void method_5ш() {
      Iterator var1 = this.сц.iterator();

      while (var1.hasNext()) {
         LineGlyphs.GlyphPath var2 = (LineGlyphs.GlyphPath)var1.next();
         var2.method_9о();
         if (var2.method_6弟()) {
            var1.remove();
         }
      }
   }

   private List<LineGlyphs.ColoredPoint> д(List<LineGlyphs.GlyphPath> var1, float var2, float var3) {
      boolean var4 = this.ЯЛ.getValue();
      float var5 = Math.max(ВЙР, this.Хг.getValue() * ВЙ3);
      float var6 = var5 * ВЙр;
      List var7 = this.Вс;
      var7.clear();

      for (LineGlyphs.GlyphPath var9 : var1) {
         float var10 = var9.method_41() * var3;
         if (!(var10 <= ВЙ6)) {
            List var11 = var9.сф;
            int var12 = var11.size();
            if (var12 >= 2) {
               for (int var13 = 0; var13 < var12 - 1; var13++) {
                  Vec3 var14 = (Vec3)var11.get(var13);
                  Vec3 var15 = (Vec3)var11.get(var13 + 1);
                  float var16 = var10 * (ВЙ_ + (float)var13 / var12 / ВЙШ);
                  int var17 = this.Д(var16);
                  if (var4) {
                     this.д(var7, var14, var15, var17, var5, var6);
                  } else {
                     this.д(var7, var14, var15, var17);
                  }
               }
            }
         }
      }

      float var18 = ВЙы * var2;

      for (LineGlyphs.GlyphPath var20 : var1) {
         float var21 = var20.method_41() * var3;
         if (!(var21 <= ВЙй)) {
            List var22 = var20.сф;
            int var23 = var22.size();
            if (var23 >= 2) {
               for (int var24 = 0; var24 < var23; var24++) {
                  Vec3 var25 = (Vec3)var22.get(var24);
                  float var26 = var21 * (ВЙЖ + (float)var24 / var23 / ВЙм);
                  int var27 = this.Д(var26);
                  this.д(var7, var25, var27, var18);
               }
            }
         }
      }

      return var7;
   }

   private void д(List<LineGlyphs.ColoredPoint> var1, Vec3 var2, Vec3 var3, int var4, float var5, float var6) {
      double var7 = var3.x - var2.x;
      double var9 = var3.y - var2.y;
      double var11 = var3.z - var2.z;
      double var13 = Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
      if (!(var13 < ВЙI)) {
         double var15 = var7 / var13;
         double var17 = var9 / var13;
         double var19 = var11 / var13;
         double var21 = 0.0;

         for (boolean var23 = true; var21 < var13; var23 = !var23) {
            double var24 = var23 ? var5 : var6;
            double var26 = Math.min(var13, var21 + var24);
            if (var23) {
               var1.add(new LineGlyphs.ColoredPoint(var2.x + var15 * var21, var2.y + var17 * var21, var2.z + var19 * var21, var4));
               var1.add(new LineGlyphs.ColoredPoint(var2.x + var15 * var26, var2.y + var17 * var26, var2.z + var19 * var26, var4));
            }

            var21 = var26;
         }
      }
   }

   @Override
   public void onDisable() {
      this.сц.clear();
      this.ся.clear();
      this.Вс.clear();
   }

   private boolean д_bbeebd87(Vec3i var1) {
      if (MINECRAFT_CLIENT.level == null) {
         return true;
      } else {
         BlockPos var2 = new BlockPos(var1.getX(), var1.getY(), var1.getZ());
         BlockState var3 = MINECRAFT_CLIENT.level.getBlockState(var2);
         if (var3.isAir()) {
            return true;
         } else {
            return !var3.getFluidState().isEmpty() ? false : var3.getCollisionShape(MINECRAFT_CLIENT.level, var2).isEmpty();
         }
      }
   }

   private Vec3 в_2f96ac28() {
      Camera var1 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
      return var1 != null ? var1.position() : MINECRAFT_CLIENT.player.position();
   }

   private void method_5衣() {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.сц.clear();
         this.Дд = -1;
      } else if (MINECRAFT_CLIENT.player.tickCount != this.Дд) {
         this.Дд = MINECRAFT_CLIENT.player.tickCount;
         this.method_5ш();
         this.method_5й();
      }
   }

   @Override
   public void onEnable() {
      this.сц.clear();
      this.ся.clear();
      this.Вс.clear();
      this.Дд = -1;
   }

   private void д(List<LineGlyphs.ColoredPoint> var1, Matrix4f var2, Vec3 var3, float var4, RenderPipeline var5) {
      if (var1.size() >= 2) {
         float var6 = ВЙФ * var4 * ВЙЙ;
         BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var8 = 0;

         for (int var9 = 0; var9 + 1 < var1.size(); var9 += 2) {
            var8 += this.д(var7, var2, var3, (LineGlyphs.ColoredPoint)var1.get(var9), (LineGlyphs.ColoredPoint)var1.get(var9 + 1), var6);
         }

         this.Ш(var7, var8, var5);
      }
   }

   private float method_1(float var1, float var2) {
      return var2 <= var1 ? var1 : var1 + this.Ч.nextFloat() * (var2 - var1);
   }

   private static double method_5(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   private void к(WorldEffectsEvent var1) {
      this.ся.clear();
      this.Вс.clear();
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.method_5衣();
         float var2 = var1.getTickDelta();

         for (LineGlyphs.GlyphPath var4 : this.сц) {
            if (var4.method_41() > ВЙ7 && var4.method_4Й() >= 2) {
               var4.必И(var2);
               this.ся.add(var4);
            }
         }

         if (!this.ся.isEmpty()) {
            Matrix4f var13 = var1.getPoses().last().pose();
            Vec3 var14 = var1.getCameraPosition();

            try {
               for (LineGlyphs.LineStyle var8 : this.В_6034b685) {
                  List var9 = this.д(this.ся, var8.widthMul, var8.alphaMul);
                  this.д(var9, var13, var14, var8.widthMul, var8.pipeline);
               }
            } finally {
               this.ся.clear();
               this.Вс.clear();
            }
         }
      }
   }

   private void method_8l(PlayerTickEvent var1) {
      this.method_5衣();
   }

   private int д(BufferBuilder var1, Matrix4f var2, Vec3 var3, LineGlyphs.ColoredPoint var4, LineGlyphs.ColoredPoint var5, float var6) {
      double var7 = var4.сЮ - var3.x;
      double var9 = var4.сЧ - var3.y;
      double var11 = var4.сЛ - var3.z;
      double var13 = var5.сЮ - var3.x;
      double var15 = var5.сЧ - var3.y;
      double var17 = var5.сЛ - var3.z;
      double var19 = var13 - var7;
      double var21 = var15 - var9;
      double var23 = var17 - var11;
      double var25 = var19 * var19 + var21 * var21 + var23 * var23;
      if (var25 < ВЙк) {
         return 0;
      }

      double var27 = (var7 + var13) * ВЙг;
      double var29 = (var9 + var15) * ВЙ0;
      double var31 = (var11 + var17) * ВЙх;
      double var33 = var21 * var31 - var23 * var29;
      double var35 = var23 * var27 - var19 * var31;
      double var37 = var19 * var29 - var21 * var27;
      double var39 = Math.sqrt(var33 * var33 + var35 * var35 + var37 * var37);
      if (var39 < ВЙУ) {
         var33 = -var23;
         var35 = 0.0;
         var37 = var19;
         var39 = Math.sqrt(var33 * var33 + var37 * var37);
         if (var39 < ВЙЗ) {
            var33 = var6;
            var35 = 0.0;
            var37 = 0.0;
            var39 = var6;
         }
      }

      float var41 = (float)(var6 / var39);
      float var42 = (float)(var33 * var41);
      float var43 = (float)(var35 * var41);
      float var44 = (float)(var37 * var41);
      float var45 = var4.Вй / ВЙЕ;
      float var46 = var4.ВЖ / ВЙП;
      float var47 = var4.Вм / ВЙ西;
      float var48 = var4.ВК / ВЙд;
      var1.addVertex(var2, (float)var7 - var42, (float)var9 - var43, (float)var11 - var44).setUv(0.0F, 0.0F).setColor(var45, var46, var47, var48);
      var1.addVertex(var2, (float)var7 + var42, (float)var9 + var43, (float)var11 + var44).setUv(1.0F, 0.0F).setColor(var45, var46, var47, var48);
      var1.addVertex(var2, (float)var13 + var42, (float)var15 + var43, (float)var17 + var44).setUv(1.0F, 1.0F).setColor(var45, var46, var47, var48);
      var1.addVertex(var2, (float)var13 - var42, (float)var15 - var43, (float)var17 - var44).setUv(0.0F, 1.0F).setColor(var45, var46, var47, var48);
      return 4;
   }

   private Vec3i д_83aea87e(Vec3i var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         Vec3i var3 = this.Ы();
         int var4 = var3.getX() * var1.getX() + var3.getY() * var1.getY() + var3.getZ() * var1.getZ();
         if (var4 == 0) {
            return var3;
         }
      }

      return this.Ы();
   }

   private int Д(float var1) {
      int var2 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      float var3 = Mth.clamp(var1, 0.0F, 1.0F);
      int var4 = var2 >> 16 & 0xFF;
      int var5 = var2 >> 8 & 0xFF;
      int var6 = var2 & 0xFF;
      int var7 = (int)(var3 * ВЙК);
      return var7 << 24 | var4 << 16 | var5 << 8 | var6;
   }

   private float д(int var1, int var2, float var3) {
      if (var1 <= 0) {
         return 1.0F;
      }

      float var4 = 1.0F - (var2 - var3) / var1;
      return Mth.clamp(var4, 0.0F, 1.0F);
   }

   private void д(List<LineGlyphs.ColoredPoint> var1, Vec3 var2, Vec3 var3, int var4) {
      var1.add(new LineGlyphs.ColoredPoint(var2.x, var2.y, var2.z, var4));
      var1.add(new LineGlyphs.ColoredPoint(var3.x, var3.y, var3.z, var4));
   }

   private int д西() {
      return Math.max(1, Math.round(this.Хк.getValue()));
   }

   private int Е(int var1, int var2) {
      return var2 <= var1 ? var1 : this.Ч.nextInt(var2 - var1) + var1;
   }

   public LineGlyphs() {
      super(ВЙб, ВЙЫ, ModuleCategory.RENDER);
      this.Хк = new FloatSetting(ВЙl, ВЙ8, ВЙ5, ВЙв).withStep(ВЙъ);
      this.ЯЧ = new BooleanSetting(ВЙМ, false);
      this.ЯЛ = new BooleanSetting(ВЙН, false);
      this.Хг = new FloatSetting(ВЙн, ВЙЦ, ВЙч, ВЙЭ).withStep(ВЙ必).visibleWhen(() -> this.ЯЛ.getValue());
      this.Ч = new Random(ВЙщ);
      this.сц = new ArrayList<>();
      this.ся = new ArrayList<>();
      this.Вс = new ArrayList<>();
      this.Дд = -1;
      this.Ве = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВЙГ)
         .withVertexShader(ClientResourceIds.getCoreId(ВЙл))
         .withFragmentShader(ClientResourceIds.getCoreId(ВЙэ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.ВА = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВЙю)
         .withVertexShader(ClientResourceIds.getCoreId(ВЙЩ))
         .withFragmentShader(ClientResourceIds.getCoreId(ВЙу))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.В_6034b685 = new LineGlyphs.LineStyle[]{
         new LineGlyphs.LineStyle(ВЙ2, ВЙф, this.Ве),
         new LineGlyphs.LineStyle(ВЙи, ВЙ9, this.Ве),
         new LineGlyphs.LineStyle(ВЙо, ВЙO, this.Ве),
         new LineGlyphs.LineStyle(1.0F, 1.0F, this.ВА)
      };
      this.Хи = this::method_8l;
      this.Х9 = this::к;
      this.addSettings(this.Хк, this.ЯЧ, this.ЯЛ, this.Хг);
   }

   private Vec3i Ы() {
      return В_a1351bad[this.Ч.nextInt(В_a1351bad.length)];
   }

   private Vec3i д_83aea87e() {
      Vec3 var1 = this.в_2f96ac28();

      for (int var2 = 0; var2 < 16; var2++) {
         int var3 = this.Е(6, 25);
         float var4 = MINECRAFT_CLIENT.player != null ? MINECRAFT_CLIENT.player.getYRot() : 0.0F;
         float var5 = var4 + this.method_1(ВЙТ, ВЙО);
         float var6 = (float)Math.toRadians(var5);
         int var7 = (int)(-(Mth.sin(var6) * var3));
         int var8 = this.Е(0, 13);
         int var9 = (int)(Mth.cos(var6) * var3);
         Vec3i var10 = new Vec3i((int)Math.floor(var1.x) + var7, (int)Math.floor(var1.y) + var8, (int)Math.floor(var1.z) + var9);
         if (this.д_bbeebd87(var10)) {
            return var10;
         }
      }

      return new Vec3i((int)Math.floor(var1.x), (int)Math.floor(var1.y), (int)Math.floor(var1.z));
   }

   private int дЬ() {
      int var1 = 0;

      for (LineGlyphs.GlyphPath var3 : this.сц) {
         if (!var3.method_6Р()) {
            var1++;
         }
      }

      return var1;
   }

   private void method_5й() {
      int var1 = this.д西();

      int var2;
      for (var2 = this.дЬ(); var2 < var1; var2++) {
         this.сц.add(new LineGlyphs.GlyphPath(this.д_83aea87e(), this.Е(7, 13)));
      }

      if (var2 > var1) {
         for (LineGlyphs.GlyphPath var4 : this.сц) {
            if (var2 <= var1) {
               break;
            }

            if (!var4.method_6Р()) {
               var4.method_9С();
               var2--;
            }
         }
      }
   }

   private void Ш(BufferBuilder var1, int var2, RenderPipeline var3) {
      MeshData var4 = var1.build();
      if (var4 != null) {
         if (var2 == 0) {
            var4.close();
         } else {
            GpuBuffer var5 = null;

            try {
               DrawState var6 = var4.drawState();
               int var7 = var6.indexCount();
               AutoStorageIndexBuffer var8 = RenderSystem.getSequentialBuffer(var6.primitiveTopology());
               GpuBuffer var9 = var8.getBuffer(var7);
               IndexType var10 = var8.type();
               var5 = RenderSystem.getDevice().createBuffer(() -> ВЙС, 32, var4.vertexBuffer());
               RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();

               try (RenderPass var13 = var12.createRenderPass(
                     () -> ВЙБ, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var13);
                  var13.setPipeline(var3);
                  var13.setVertexBuffer(0, var5.slice());
                  var13.setIndexBuffer(var9, var10);
                  var13.drawIndexed(var7, 1, 0, 0, 0);
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

   private record LineStyle(float widthMul, float alphaMul, RenderPipeline pipeline) {
   }

   private static class ColoredPoint {
      final double сЮ;
      final double сЧ;
      final double сЛ;
      final int Вй;
      final int ВЖ;
      final int Вм;
      final int ВК;

      ColoredPoint(double var1, double var3, double var5, int var7) {
         this.сЮ = var1;
         this.сЧ = var3;
         this.сЛ = var5;
         this.ВК = var7 >> 24 & 0xFF;
         this.Вй = var7 >> 16 & 0xFF;
         this.ВЖ = var7 >> 8 & 0xFF;
         this.Вм = var7 & 0xFF;
      }
   }

   private class GlyphPath {
      private final List<Vec3i> с2 = new ArrayList<>();
      private final List<Vec3> сф = new ArrayList<>();
      private Vec3i В;
      private int пт;
      private int па;
      private int пЮ;
      private boolean Х7;
      private final long Х诶 = System.currentTimeMillis();
      private long Х1;
      private static final double Я0щ = 0.5;
      private static final double Я0Г = 0.5;
      private static final double Я0л = 0.5;
      private static final double Я0э = 0.5;
      private static final double Я0ю = 0.5;
      private static final double Я0Щ = 0.5;
      private static final float Я0у = 0.33F;
      private static final float Я02 = 400.0F;
      private static final float Я0ф = 600.0F;
      private static final float Я0и = 0.03F;

      void method_9С() {
         if (!this.Х7) {
            this.Х7 = true;
            this.Х1 = System.currentTimeMillis();
         }
      }

      void method_9о() {
         if (!this.Х7) {
            if (this.пЮ <= 0) {
               this.method_9С();
            } else if (this.пт > 0) {
               this.пт = this.пт - (LineGlyphs.this.ЯЧ.getValue() ? 1 : 2);
               if (this.пт < 0) {
                  this.пт = 0;
               }
            } else {
               Vec3i var1 = this.с2.get(this.с2.size() - 1);
               boolean var2 = false;

               for (int var3 = 0; var3 < 8; var3++) {
                  Vec3i var4 = LineGlyphs.this.д_83aea87e(this.В);
                  int var5 = LineGlyphs.this.Е(1, 4);
                  Vec3i var6 = new Vec3i(var1.getX() + var4.getX() * var5, var1.getY() + var4.getY() * var5, var1.getZ() + var4.getZ() * var5);
                  if (LineGlyphs.this.д_bbeebd87(var6)) {
                     this.В = var4;
                     this.па = var5;
                     this.пт = var5;
                     this.с2.add(var6);
                     this.пЮ--;
                     var2 = true;
                     break;
                  }
               }

               if (!var2) {
                  this.method_9С();
               }
            }
         }
      }

      void 必И(float var1) {
         this.сф.clear();
         float var2 = LineGlyphs.this.д(this.па, this.пт, var1);

         for (int var3 = 0; var3 < this.с2.size(); var3++) {
            Vec3i var4 = this.с2.get(var3);
            double var5 = var4.getX() + Я0щ;
            double var7 = var4.getY() + Я0Г;
            double var9 = var4.getZ() + Я0л;
            if (this.с2.size() >= 2 && var3 == this.с2.size() - 1) {
               Vec3i var11 = this.с2.get(this.с2.size() - 2);
               var5 = LineGlyphs.method_5(var11.getX() + Я0э, var5, var2);
               var7 = LineGlyphs.method_5(var11.getY() + Я0ю, var7, var2);
               var9 = LineGlyphs.method_5(var11.getZ() + Я0Щ, var9, var2);
            }

            this.сф.add(new Vec3(var5, var7, var9));
         }
      }

      int method_4Й() {
         return this.с2.size();
      }

      boolean method_6Р() {
         return this.Х7;
      }

      float method_41() {
         float var1 = LineGlyphs.this.ЯЧ.getValue() ? Я0у : 1.0F;
         if (this.Х7) {
            long var4 = System.currentTimeMillis() - this.Х1;
            return Math.max(0.0F, 1.0F - (float)var4 * var1 / Я02);
         } else {
            long var2 = System.currentTimeMillis() - this.Х诶;
            return Math.min(1.0F, (float)var2 * var1 / Я0ф);
         }
      }

      GlyphPath(Vec3i var2, int var3) {
         this.с2.add(var2);
         this.В = LineGlyphs.this.Ы();
         this.пЮ = var3;
         this.method_9о();
      }

      boolean method_6弟() {
         return this.Х7 && this.method_41() <= Я0и;
      }
   }
}
