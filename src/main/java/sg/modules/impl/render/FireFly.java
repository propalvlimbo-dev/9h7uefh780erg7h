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
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
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

public class FireFly extends ClientModule {
   private static final Identifier И = Identifier.fromNamespaceAndPath(FireFly.ЯрУ, FireFly.ЯрЗ);
   private static final Identifier ь = Identifier.fromNamespaceAndPath(FireFly.ЯрЕ, FireFly.ЯрП);
   private static final long сЦ = 5500L;
   private static final long сч = 400L;
   private static final long сЭ = 450L;
   private static final int Вщ = 40;
   private static final int ВГ = 8;
   private static final int Вл = 6;
   private static final int Вэ = 8;
   private final SingleChoiceSetting ж;
   @SerializedName("1")
   private final ColorSetting field_1;
   private final BooleanSetting ы;
   private final BooleanSetting й;
   private final FloatSetting Й;
   private final List<FireFly.FireflyParticle> э;
   private float ЯЬ;
   private float Я衣;
   private float Яш;
   private float ЯИ;
   private float Яь;
   private float Яж;
   @SerializedName("7")
   private final RenderPipeline field_7;
   private final EventListener<PlayerTickEvent> сД;
   private final EventListener<WorldEffectsEvent> сЪ;
   private static final String Я3Е = "FireFly";
   private static final String Я3П = "Светлячки вокруг игрока";
   private static final String Я3西 = "Режим цвета";
   private static final String Я3д = "Случайный";
   private static final String Я3Б = "Свой";
   private static final String Я3С = "Клиент";
   private static final String Я3з = "Цвет";
   private static final int Я3ц = -47616;
   private static final String Я3я = "Тёмный след";
   private static final String Ярс = "Свечение";
   private static final String ЯрВ = "Задержка спавна";
   private static final float ЯрЯ = 3.0F;
   private static final float ЯрХ = 10.0F;
   private static final String Яре = "rose/fire_fly_add";
   private static final String ЯрА = "dead_chams";
   private static final String Яр4 = "dead_chams";
   private static final String Ярп = "Sampler0";
   private static final double ЯрД = 10.0;
   private static final double ЯрЪ = 4.0;
   private static final double Ярт = 6.0;
   private static final double Яра = 5.0;
   private static final String ЯрЮ = "Случайный";
   private static final int ЯрЧ = -16777216;
   private static final String ЯрЛ = "Клиент";
   private static final double Яр诶 = 3.0;
   private static final double Яр1 = 3.0;
   private static final double ЯрЬ = 3.0;
   private static final float Яр衣 = 0.004F;
   private static final double Ярш = 1.6;
   private static final float ЯрИ = 255.0F;
   private static final float Ярь = 0.3F;
   private static final float Ярж = 1.0E-5F;
   private static final float Яр弟 = 5.5F;
   private static final float Ярб = 3.0F;
   private static final float ЯрЫ = 20.0F;
   private static final float Ярl = 0.012F;
   private static final float Яр8 = 255.0F;
   private static final float Яр5 = 0.25F;
   private static final float Ярв = 255.0F;
   private static final float Яръ = 255.0F;
   private static final float ЯрМ = 0.25F;
   private static final float ЯрН = 5.0F;
   private static final float Ярн = 3.0F;
   private static final float ЯрЦ = 10.0F;
   private static final float Ярч = 0.012F;
   private static final float ЯрЭ = 255.0F;
   private static final float Яр必 = 255.0F;
   private static final float Ярщ = 255.0F;
   private static final float ЯрГ = 0.3F;
   private static final float Ярл = 255.0F;
   private static final float Ярэ = 0.1F;
   private static final float Ярю = 255.0F;
   private static final float ЯрЩ = 0.25F;
   private static final float Яру = 0.5F;
   private static final float Яр2 = 5.0F;
   private static final float Ярф = 0.004F;
   private static final float Яри = 255.0F;
   private static final float Яр9 = 0.5F;
   private static final float Яро = 255.0F;
   private static final float ЯрO = 0.3F;
   private static final float Яр7 = 255.0F;
   private static final double ЯрР = 1.0E-8;
   private static final double Яр3 = 1.0E-8;
   private static final float Ярр = 255.0F;
   private static final float Яр6 = 255.0F;
   private static final float Яр_ = 255.0F;
   private static final float ЯрШ = 255.0F;
   private static final float Яры = 0.5F;
   private static final float Ярй = 0.5F;
   private static final float ЯрЖ = 255.0F;
   private static final float Ярм = 255.0F;
   private static final float ЯрК = 255.0F;
   private static final float ЯрI = 255.0F;
   private static final String ЯрТ = "Sampler0";
   private static final float ЯрО = 765.0F;
   private static final float ЯрФ = 0.5F;
   private static final float ЯрЙ = -2.0F;
   private static final double Ярк = 2.0;
   private static final String Ярг = "fireFly";
   private static final String Яр0 = "fireFly";
   private static final String Ярх = "Свой";
   private static final String ЯрУ = "rose";
   private static final String ЯрЗ = "images/fireflies/firepart.png";
   private static final String ЯрЕ = "rose";
   private static final String ЯрП = "images/fireflies/bloom.png";

   private void 西(BufferBuilder var1, Matrix4f var2, double var3, double var5, double var7, float var9, float var10, int var11) {
      float var12 = var9 * Яры;
      float var13 = var10 * Ярй;
      float var14 = ColorMath.getRed(var11) / ЯрЖ;
      float var15 = ColorMath.getGreen(var11) / Ярм;
      float var16 = ColorMath.getBlue(var11) / ЯрК;
      float var17 = ColorMath.getAlpha(var11) / ЯрI;
      float var18 = this.ЯЬ * var12;
      float var19 = this.Я衣 * var12;
      float var20 = this.Яш * var12;
      float var21 = this.ЯИ * var13;
      float var22 = this.Яь * var13;
      float var23 = this.Яж * var13;
      float var24 = (float)var3;
      float var25 = (float)var5;
      float var26 = (float)var7;
      var1.addVertex(var2, var24 - var18 - var21, var25 - var19 - var22, var26 - var20 - var23).setUv(0.0F, 0.0F).setColor(var14, var15, var16, var17);
      var1.addVertex(var2, var24 - var18 + var21, var25 - var19 + var22, var26 - var20 + var23).setUv(0.0F, 1.0F).setColor(var14, var15, var16, var17);
      var1.addVertex(var2, var24 + var18 + var21, var25 + var19 + var22, var26 + var20 + var23).setUv(1.0F, 1.0F).setColor(var14, var15, var16, var17);
      var1.addVertex(var2, var24 + var18 - var21, var25 + var19 - var22, var26 + var20 - var23).setUv(1.0F, 0.0F).setColor(var14, var15, var16, var17);
   }

   private static float И(double var0, double var2) {
      return 西(ThreadLocalRandom.current(), var0, var2);
   }

   private static float 西(ThreadLocalRandom var0, double var1, double var3) {
      return (float)(var1 + var0.nextDouble() * (var3 - var1));
   }

   @Override
   public void onDisable() {
      this.э.clear();
   }

   private void й(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.player.tickCount == 1) {
            for (FireFly.FireflyParticle var3 : this.э) {
               var3.Ак = true;
            }
         }

         long var7 = System.currentTimeMillis();
         int var4 = MINECRAFT_CLIENT.player.tickCount;
         int var5 = 0;

         for (int var6 = this.э.size(); var5 < var6; var5++) {
            this.э.get(var5).method_1(var7, var4);
         }

         this.э.removeIf(var0 -> var0.Ак);
         var5 = Math.round(this.Й.getValue());
         if (var4 % (var5 + 1) == 0 && this.э.size() < 40) {
            int var9 = this.М();
            this.э.add(new FireFly.FireflyParticle(this.西(ЯрД, ЯрЪ), var9));
            if (this.э.size() < 40) {
               this.э.add(new FireFly.FireflyParticle(this.西(Ярт, Яра), var9));
            }
         }
      } else {
         this.э.clear();
      }
   }

   private int 西(
      BufferBuilder var1, Matrix4f var2, Vec3 var3, double var4, double var6, double var8, double var10, double var12, double var14, float var16, int var17
   ) {
      double var18 = var10 - var4;
      double var20 = var12 - var6;
      double var22 = var14 - var8;
      if (var18 * var18 + var20 * var20 + var22 * var22 < ЯрР) {
         return 0;
      }

      double var24 = -var22;
      double var26 = var18;
      double var28 = Math.sqrt(var24 * var24 + var26 * var26);
      if (var28 < Яр3) {
         var24 = var16;
         var26 = 0.0;
      } else {
         var24 = var24 / var28 * var16;
         var26 = var26 / var28 * var16;
      }

      float var30 = ColorMath.getRed(var17) / Ярр;
      float var31 = ColorMath.getGreen(var17) / Яр6;
      float var32 = ColorMath.getBlue(var17) / Яр_;
      float var33 = ColorMath.getAlpha(var17) / ЯрШ;
      var1.addVertex(var2, (float)(var4 - var3.x + var24), (float)(var6 - var3.y), (float)(var8 - var3.z + var26))
         .setUv(0.0F, 0.0F)
         .setColor(var30, var31, var32, var33);
      var1.addVertex(var2, (float)(var4 - var3.x - var24), (float)(var6 - var3.y), (float)(var8 - var3.z - var26))
         .setUv(0.0F, 1.0F)
         .setColor(var30, var31, var32, var33);
      var1.addVertex(var2, (float)(var10 - var3.x - var24), (float)(var12 - var3.y), (float)(var14 - var3.z - var26))
         .setUv(1.0F, 1.0F)
         .setColor(var30, var31, var32, var33);
      var1.addVertex(var2, (float)(var10 - var3.x + var24), (float)(var12 - var3.y), (float)(var14 - var3.z + var26))
         .setUv(1.0F, 0.0F)
         .setColor(var30, var31, var32, var33);
      return 4;
   }

   private Vec3 西(double var1, double var3) {
      Vec3 var5 = MINECRAFT_CLIENT.player.position();
      ThreadLocalRandom var6 = ThreadLocalRandom.current();
      Vec3 var7 = var5.add(西(var6, -var1, var1), 西(var6, -var3 / Яр诶, var3), 西(var6, -var1, var1));
      double var8 = var1 / Яр1;

      for (int var10 = 0; var10 < 8; var10++) {
         BlockPos var11 = BlockPos.containing(var7);
         boolean var12 = !MINECRAFT_CLIENT.level.getBlockState(var11).getCollisionShape(MINECRAFT_CLIENT.level, var11).isEmpty();
         double var13 = Math.hypot(var7.x - var5.x, var7.z - var5.z);
         if (!var12 && var13 >= var8) {
            break;
         }

         var7 = var5.add(西(var6, -var1, var1), 西(var6, -var3 / ЯрЬ, var3), 西(var6, -var1, var1));
      }

      return var7;
   }

   private static float Ы(float var0) {
      var0 = Mth.clamp(var0, 0.0F, 1.0F);
      return var0 < ЯрФ ? 2.0F * var0 * var0 : 1.0F - (float)Math.pow(ЯрЙ * var0 + 2.0F, Ярк) / 2.0F;
   }

   public FireFly() {
      super(Я3Е, Я3П, ModuleCategory.RENDER);
      this.ж = new SingleChoiceSetting(Я3西, new ChoiceOption(Я3д), new ChoiceOption(Я3Б), new ChoiceOption(Я3С));
      this.field_1 = new ColorSetting(Я3з, false, Я3ц).visibleWhen(() -> this.ж.isSelected(Ярх));
      this.ы = new BooleanSetting(Я3я, false);
      this.й = new BooleanSetting(Ярс, false);
      this.Й = new FloatSetting(ЯрВ, ЯрЯ, 1.0F, ЯрХ).withStep(1.0F);
      this.э = new ArrayList<>(40);
      this.field_7 = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Яре)
         .withVertexShader(ClientResourceIds.getCoreId(ЯрА))
         .withFragmentShader(ClientResourceIds.getCoreId(Яр4))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Ярп)
         .build();
      this.сД = this::й;
      this.сЪ = this::с;
      this.addSettings(this.ж, this.field_1, this.ы, this.й, this.Й);
   }

   private void с(WorldEffectsEvent var1) {
      if (!this.э.isEmpty() && MINECRAFT_CLIENT.player != null) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Camera var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var4 = var3.position();
         this.р(var3.yRot(), var3.xRot());
         long var5 = System.currentTimeMillis();
         Vec3 var7 = MINECRAFT_CLIENT.player.getEyePosition();
         Matrix4f var8 = var1.getPoses().last().pose();
         boolean var9 = this.ы.getValue();
         boolean var10 = this.й.getValue();
         int var11 = this.э.size() > 24 ? 2 : 1;
         BufferBuilder var12 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var13 = 0;

         for (FireFly.FireflyParticle var15 : this.э) {
            float var16 = var15.ЮЩ;
            if (!(var16 < Яр衣)) {
               double var17 = var15.сВ.x - var7.x;
               double var19 = var15.сВ.y + Ярш - var7.y;
               double var21 = var15.сВ.z - var7.z;
               float var23 = (float)(var17 * var17 + var19 * var19 + var21 * var21);
               float var24 = Mth.sqrt(var23);
               int var25 = var15.ЪП;
               if (var25 >= 2) {
                  int var26 = ColorMath.withAlpha(var15.Ъд, ColorMath.getAlpha(var15.Ъд) / ЯрИ * var16);
                  int var27 = var9 ? И(var26, Ярь) : var26;
                  float var28 = Ярж + Яр弟 * Mth.clamp(1.0F - (var24 - Ярб) / ЯрЫ, 0.0F, 1.0F) * Ярl;

                  for (int var29 = var11; var29 < var25; var29 += var11) {
                     int var30 = var29 - var11;
                     float var31 = (float)var29 / var25;
                     float var32 = Ы(var31);
                     int var33 = ColorMath.withAlpha(var27, ColorMath.getAlpha(var27) / Яр8 * var16 * var32);
                     float var34 = method_7(var33) * Яр5 * var31 * (ColorMath.getAlpha(var33) / Ярв);
                     var33 = ColorMath.lerpArgb(var33, ColorMath.withAlpha(-1, ColorMath.getAlpha(var33) / Яръ), var34);
                     var13 += this.西(
                        var12, var8, var4, var15.Ч[var30], var15.Л[var30], var15.诶[var30], var15.Ч[var29], var15.Л[var29], var15.诶[var29], var28, var33
                     );
                  }
               }

               float var44 = (ЯрМ + ЯрН * Mth.clamp(1.0F - (var24 - Ярн) / ЯрЦ, 0.0F, 1.0F)) * Ярч;

               for (int var46 = 0; var46 < var15.Ъ西; var46++) {
                  FireFly.SparkParticle var49 = var15.В[var46];
                  float var52 = (float)var49.ь(var5);
                  if (!(var52 >= 1.0F)) {
                     int var54 = ColorMath.lerpArgb(-1, var15.Ъд, var52);
                     var54 = ColorMath.withAlpha(var54, ColorMath.getAlpha(var15.Ъд) / ЯрЭ * var16 * (1.0F - var52 * var52 * var52));
                     double var58 = Mth.lerp(var2, var49.field_4, var49.Х) - var4.x;
                     double var61 = Mth.lerp(var2, var49.п, var49.е) - var4.y;
                     double var35 = Mth.lerp(var2, var49.Д, var49.А) - var4.z;
                     this.西(var12, var8, var58, var61, var35, var44, var44, var54);
                     var13 += 4;
                  }
               }

               if (var10) {
                  int var47 = ColorMath.withAlpha(var15.Ъд, ColorMath.getAlpha(var15.Ъд) / Яр必 * var16);
                  float var50 = ColorMath.getAlpha(var47) / Ярщ * method_7(var47) * ЯрГ;
                  int var53 = ColorMath.lerpArgb(var47, ColorMath.withAlpha(-1, ColorMath.getAlpha(var47) / Ярл), var50);
                  int var56 = И(var53, Ярэ);
                  var56 = ColorMath.withAlpha(var56, ColorMath.getAlpha(var56) / Ярю * ЯрЩ);
                  double var59 = Mth.lerp(var2, var15.сс.x, var15.сВ.x) - var4.x;
                  double var62 = Mth.lerp(var2, var15.сс.y, var15.сВ.y) - var4.y;
                  double var63 = Mth.lerp(var2, var15.сс.z, var15.сВ.z) - var4.z;
                  float var37 = Яру * var16 * Яр2;
                  this.西(var12, var8, var59, var62, var63, var37, var37, var56);
                  var13 += 4;
               }
            }
         }

         this.西(var12, ь, var13);
         BufferBuilder var38 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var39 = 0;

         for (FireFly.FireflyParticle var41 : this.э) {
            float var18 = var41.ЮЩ;
            if (!(var18 < Ярф)) {
               int var42 = ColorMath.withAlpha(var41.Ъд, ColorMath.getAlpha(var41.Ъд) / Яри * var18);
               double var20 = Mth.lerp(var2, var41.сс.x, var41.сВ.x) - var4.x;
               double var22 = Mth.lerp(var2, var41.сс.y, var41.сВ.y) - var4.y;
               double var43 = Mth.lerp(var2, var41.сс.z, var41.сВ.z) - var4.z;
               float var45 = Яр9 * var18;
               float var48 = ColorMath.getAlpha(var42) / Яро * method_7(var42) * ЯрO;
               int var51 = ColorMath.lerpArgb(var42, ColorMath.withAlpha(-1, ColorMath.getAlpha(var42) / Яр7), var48);
               this.西(var38, var8, var20, var22, var43, var45, var45, var51);
               var39 += 4;
            }
         }

         this.西(var38, И, var39);
      }
   }

   private int М() {
      if (this.ж.isSelected(ЯрЮ)) {
         return Color.getHSBColor(ThreadLocalRandom.current().nextFloat(), 1.0F, 1.0F).getRGB() | ЯрЧ;
      } else {
         return this.ж.isSelected(ЯрЛ) ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.field_1.getValue();
      }
   }

   private void р(float var1, float var2) {
      float var3 = (float)Math.toRadians(-var1);
      float var4 = (float)Math.toRadians(var2);
      float var5 = Mth.cos(var3);
      float var6 = Mth.sin(var3);
      float var7 = Mth.cos(var4);
      float var8 = Mth.sin(var4);
      this.ЯЬ = var5;
      this.Я衣 = 0.0F;
      this.Яш = -var6;
      this.ЯИ = var8 * var6;
      this.Яь = var7;
      this.Яж = var8 * var5;
   }

   private void 西(BufferBuilder var1, Identifier var2, int var3) {
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
               var5 = RenderSystem.getDevice().createBuffer(() -> Яр0, 32, var4.vertexBuffer());
               RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var13 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var14 = var12.createRenderPass(
                     () -> Ярг, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var14);
                  var14.setPipeline(this.field_7);
                  var14.bindTexture(ЯрТ, var13.getTextureView(), var13.getSampler());
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

   private static int И(int var0, float var1) {
      var1 = Mth.clamp(var1, 0.0F, 1.0F);
      return ColorMath.packArgb(
         (int)(ColorMath.getRed(var0) * var1), (int)(ColorMath.getGreen(var0) * var1), (int)(ColorMath.getBlue(var0) * var1), ColorMath.getAlpha(var0)
      );
   }

   private static float method_7(int var0) {
      return (ColorMath.getRed(var0) + ColorMath.getGreen(var0) + ColorMath.getBlue(var0)) / ЯрО;
   }

   private static final class SparkParticle {
      private double Х;
      private double е;
      private double А;
      @SerializedName("4")
      private double field_4;
      private double п;
      private double Д;
      private double Ъ;
      private float с4;
      private float сп;
      private float сД;
      private long ч;
      private int в;
      private static final float шК = (float) (Math.PI * 2);
      private static final float шI = -135.0F;
      private static final float шТ = 90.0F;
      private static final double шО = 50.0;
      private static final double шФ = 1.2;

      private double ь(long var1) {
         return Mth.clamp((float)(var1 - this.ч) / this.в, 0.0F, 1.0F);
      }

      private void ф() {
         this.field_4 = this.Х;
         this.п = this.е;
         this.Д = this.А;
         this.Х = this.Х + this.с4 * this.Ъ;
         this.е = this.е + this.сп * this.Ъ;
         this.А = this.А + this.сД * this.Ъ;
         this.Ъ = this.Ъ / шФ;
      }

      private boolean д(long var1) {
         return var1 - this.ч >= this.в;
      }

      private void ь(Vec3 var1, long var2, int var4) {
         ThreadLocalRandom var5 = ThreadLocalRandom.current();
         float var6 = var5.nextFloat() * шК;
         float var7 = (float)Math.toRadians(шI + var5.nextFloat() * шТ);
         this.с4 = Mth.sin(var6);
         this.сп = Mth.cos(var7);
         this.сД = Mth.cos(var6);
         this.ч = var2;
         this.в = var4;
         this.Ъ = var5.nextDouble() / шО;
         this.Х = this.field_4 = var1.x;
         this.е = this.п = var1.y;
         this.А = this.Д = var1.z;
      }
   }

   private final class FireflyParticle {
      private final double[] Ч = new double[8];
      private final double[] Л = new double[8];
      private final double[] 诶 = new double[8];
      private final long[] Х = new long[8];
      private int ЪП;
      private final FireFly.SparkParticle[] В = new FireFly.SparkParticle[6];
      private int Ъ西;
      private Vec3 сс;
      private Vec3 сВ;
      private final int Ъд;
      private float ЮГ;
      private float Юл;
      private float Юэ;
      private float Юю;
      private final long е1;
      private long еЬ;
      private int ЪБ;
      private float ЮЩ;
      private float Юу;
      private boolean Ак;
      private float Ю2;
      private int ЪС;
      private static final double ВАи = 360.0;
      private static final double ВА9 = 0.08;
      private static final double ВАо = 0.175;
      private static final double ВАO = -0.075;
      private static final double ВА7 = 0.1;
      private static final double ВАР = 300.5;
      private static final double ВА3 = 900.5;
      private static final float ВАр = 5500.0F;
      private static final float ВА6 = 0.02F;
      private static final double ВА_ = 300.5;
      private static final double ВАШ = 900.5;
      private static final double ВАы = 360.0;
      private static final float ВАй = 0.065F;
      private static final float ВАЖ = 1.005F;
      private static final float ВАм = 1.02F;
      private static final float ВАК = 0.1F;
      private static final float ВАI = 0.3F;
      private static final float ВАТ = 0.003921569F;
      private static final long ВАО = 400L;

      private void method_1(double var1, double var3, double var5, long var7) {
         if (this.ЪП == 8) {
            System.arraycopy(this.Ч, 1, this.Ч, 0, 7);
            System.arraycopy(this.Л, 1, this.Л, 0, 7);
            System.arraycopy(this.诶, 1, this.诶, 0, 7);
            System.arraycopy(this.Х, 1, this.Х, 0, 7);
            this.ЪП = 7;
         }

         this.Ч[this.ЪП] = var1;
         this.Л[this.ЪП] = var3;
         this.诶[this.ЪП] = var5;
         this.Х[this.ЪП] = var7;
         this.ЪП++;
      }

      private void method_1(long var1, int var3) {
         this.вщ();
         if (var1 - this.еЬ >= this.ЪБ) {
            this.ЪБ = (int)FireFly.И(ВА_, ВАШ);
            this.еЬ = var1;
            this.ЮГ = FireFly.И(0.0, ВАы);
         }

         this.Юл = Mth.lerp(ВАй, this.Юл, this.ЮГ);
         this.Юэ = this.Юэ / ВАЖ;
         this.Юю = this.Юю / ВАм;
         float var4 = -Mth.sin((float)Math.toRadians(this.Юл)) * this.Юэ;
         float var5 = Mth.cos((float)Math.toRadians(this.Юл)) * this.Юэ;
         this.сс = this.сВ;
         if (--this.ЪС <= 0) {
            this.ЪС = 3;
            if (MinecraftAccess.MINECRAFT_CLIENT.level != null) {
               float var6 = ВАК;
               AABB var7 = new AABB(
                  this.сВ.x - var6 / 2.0F, this.сВ.y, this.сВ.z - var6 / 2.0F, this.сВ.x + var6 / 2.0F, this.сВ.y + var6, this.сВ.z + var6 / 2.0F
               );
               this.Ю2 = MinecraftAccess.MINECRAFT_CLIENT.level.noCollision(var7) ? 1.0F : ВАI;
            } else {
               this.Ю2 = 1.0F;
            }
         }

         this.сВ = this.сВ.add(var4 / this.Ю2, this.Юю / this.Ю2, var5 / this.Ю2);
         if (this.о(var1) >= 1.0F) {
            this.Юу = 0.0F;
            if (this.ЮЩ < ВАТ) {
               this.Ак = true;
            }
         }

         this.method_1(this.сВ.x, this.сВ.y, this.сВ.z, var1 + ВАО);
         this.с(var1);
         if ((var3 & 1) == 0) {
            this.У(this.сВ, var1);
         }

         for (int var8 = 0; var8 < this.Ъ西; var8++) {
            this.В[var8].ф();
         }

         this.ы(var1);
      }

      private FireflyParticle(Vec3 var2, int var3) {
         this.ЮГ = FireFly.И(0.0, ВАи);
         this.Юл = this.ЮГ;
         this.Юэ = FireFly.И(ВА9, ВАо);
         this.Юю = FireFly.И(ВАO, ВА7);
         this.е1 = System.currentTimeMillis();
         this.еЬ = this.е1;
         this.ЪБ = (int)FireFly.И(ВАР, ВА3);
         this.ЮЩ = 0.0F;
         this.Юу = 1.0F;
         this.Ю2 = 1.0F;
         this.сВ = var2;
         this.сс = var2;
         this.Ъд = var3;

         for (int var4 = 0; var4 < 6; var4++) {
            this.В[var4] = new FireFly.SparkParticle();
         }
      }

      private void У(Vec3 var1, long var2) {
         if (this.Ъ西 >= 6) {
            FireFly.SparkParticle var4 = this.В[0];
            System.arraycopy(this.В, 1, this.В, 0, 5);
            this.В[5] = var4;
            this.Ъ西 = 5;
         }

         this.В[this.Ъ西++].ь(var1, var2, 450);
      }

      private void вщ() {
         float var1 = ВА6;
         if (this.ЮЩ < this.Юу) {
            this.ЮЩ = Math.min(this.Юу, this.ЮЩ + var1);
         } else if (this.ЮЩ > this.Юу) {
            this.ЮЩ = Math.max(this.Юу, this.ЮЩ - var1);
         }
      }

      private void с(long var1) {
         int var3 = 0;

         for (int var4 = 0; var4 < this.ЪП; var4++) {
            if (this.Х[var4] > var1) {
               if (var3 != var4) {
                  this.Ч[var3] = this.Ч[var4];
                  this.Л[var3] = this.Л[var4];
                  this.诶[var3] = this.诶[var4];
                  this.Х[var3] = this.Х[var4];
               }

               var3++;
            }
         }

         this.ЪП = var3;
      }

      private float о(long var1) {
         return Mth.clamp((float)(var1 - this.е1) / ВАр, 0.0F, 1.0F);
      }

      private void ы(long var1) {
         int var3 = 0;

         for (int var4 = 0; var4 < this.Ъ西; var4++) {
            FireFly.SparkParticle var5 = this.В[var4];
            if (!var5.д(var1)) {
               if (var3 != var4) {
                  FireFly.SparkParticle var6 = this.В[var3];
                  this.В[var3] = var5;
                  this.В[var4] = var6;
               }

               var3++;
            }
         }

         this.Ъ西 = var3;
      }
   }
}
