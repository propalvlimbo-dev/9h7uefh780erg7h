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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.events.impl.AttackEntityEvent;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;

public class Cubes extends ClientModule {
   private static final Identifier ю = Identifier.fromNamespaceAndPath(Cubes.В弟г, Cubes.В弟0);
   private static final float ХР = 12.0F;
   private static final float Х3 = 0.18F;
   private static final float Хр = 0.25F;
   private static final float Х6 = 1.7F;
   private static final float Х_ = 900.0F;
   private static final long сд = 65536L;
   private static final byte[][] В = new byte[][]{
      {-1, -1, -1, 1, -1, -1},
      {1, -1, -1, 1, -1, 1},
      {1, -1, 1, -1, -1, 1},
      {-1, -1, 1, -1, -1, -1},
      {-1, 1, -1, 1, 1, -1},
      {1, 1, -1, 1, 1, 1},
      {1, 1, 1, -1, 1, 1},
      {-1, 1, 1, -1, 1, -1},
      {-1, -1, -1, -1, 1, -1},
      {1, -1, -1, 1, 1, -1},
      {1, -1, 1, 1, 1, 1},
      {-1, -1, 1, -1, 1, 1}
   };
   private static final byte[][] Я_ce4c9511_at_206f93c3 = new byte[][]{{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {2, 3}, {3, 4}, {4, 1}};
   private static final float[] А_at_206f93c3 = new float[]{Cubes.В弟х, Cubes.В弟У, Cubes.В弟З};
   private static final float[] field_4 = new float[]{Cubes.В弟Е, Cubes.В弟П, Cubes.В弟西};
   private final SingleChoiceSetting Н;
   private final SingleChoiceSetting н;
   private final FloatSetting ср;
   private final FloatSetting с6;
   private final FloatSetting с_;
   private final List<Cubes.FloatingShape> Ф;
   private final List<Cubes.FloatingShape> Й;
   @SerializedName("Я")
   private final Random Я_5650d2a3_at_206f93c3;
   private boolean Вв;
   private float ХШ;
   private float Хы;
   private float Хй;
   private int Ха;
   private int ХЮ;
   private ClientLevel Ю;
   @SerializedName("Х")
   private final GpuBuffer[] Х_at_206f93c3;
   @SerializedName("Я")
   private final long[] Я_ff204daf_at_206f93c3;
   private float ХЖ;
   @SerializedName("п")
   private float[] п_at_206f93c3;
   private float[] Д;
   private final RenderPipeline са;
   private final RenderPipeline сЮ;
   private final RenderPipeline сЧ;
   private final RenderPipeline сЛ;
   private final EventListener<WorldEffectsEvent> су;
   private final EventListener<AttackEntityEvent> с2;
   private static final String Вж8 = "Cubes";
   private static final String Вж5 = "Парящие кубы и треугольники вокруг игрока";
   private static final String Вжв = "Анимация";
   private static final String Вжъ = "Разлет";
   private static final String ВжМ = "Падение";
   private static final String ВжН = "Форма";
   private static final String Вжн = "Кубы";
   private static final String ВжЦ = "Треугольники";
   private static final String Вжч = "Количество";
   private static final float ВжЭ = 30.0F;
   private static final float Вж必 = 5.0F;
   private static final float Вжщ = 1000.0F;
   private static final String ВжГ = "Размер";
   private static final float Вжл = 0.1F;
   private static final float Вжэ = 3.0F;
   private static final float Вжю = 0.1F;
   private static final String ВжЩ = "Скорость";
   private static final float Вжу = 0.1F;
   private static final float Вж2 = 5.0F;
   private static final float Вжф = 0.1F;
   private static final int Вжи = Integer.MIN_VALUE;
   private static final String Вж9 = "rose/cubes_glow";
   private static final String Вжо = "dead_chams";
   private static final String ВжO = "dead_chams";
   private static final String Вж7 = "Sampler0";
   private static final String ВжР = "rose/cubes_faces";
   private static final String Вж3 = "target_esp_crystal";
   private static final String Вжр = "target_esp_crystal";
   private static final String Вж6 = "rose/cubes_triangle_faces";
   private static final String Вж_ = "target_esp_crystal";
   private static final String ВжШ = "target_esp_crystal";
   private static final String Вжы = "rose/cubes_edges";
   private static final String Вжй = "target_esp_crystal";
   private static final String ВжЖ = "cubes_edges";
   private static final double Вжм = Double.MAX_VALUE;
   private static final double ВжК = 128.0;
   private static final double ВжI = 1.32;
   private static final double ВжТ = 0.08;
   private static final double ВжО = 0.02;
   private static final float ВжФ = 0.25F;
   private static final float ВжЙ = 12.0F;
   private static final String Вжк = "Падение";
   private static final double Вжг = 6.25;
   private static final float Вж0 = 0.06F;
   private static final double Вжх = 0.0024F;
   private static final float ВжУ = 0.8F;
   private static final double ВжЗ = 0.002F;
   private static final float ВжЕ = 8.0E-5F;
   private static final float ВжП = -0.032F;
   private static final float Вж西 = 0.2F;
   private static final float Вжд = 0.2F;
   private static final float ВжБ = 0.2F;
   private static final float ВжС = 0.995F;
   private static final float Вжз = 0.995F;
   private static final float Вжц = 0.995F;
   private static final double Вжя = 2.5;
   private static final float В弟с = 0.18F;
   private static final float В弟В = 1.7F;
   private static final float В弟Я = 255.0F;
   private static final float В弟Х = 255.0F;
   private static final float В弟е = 255.0F;
   private static final double В弟А = 900.0;
   private static final double В弟4 = -1.0;
   private static final float В弟п = 0.001F;
   private static final String В弟Д = "Кубы";
   private static final float В弟Ъ = 0.5F;
   private static final String В弟т = "cubesGlow";
   private static final float В弟а = 20.0F;
   private static final float В弟Ю = 0.4F;
   private static final float В弟Ч = 0.001F;
   private static final String В弟Л = "cubesFaces";
   private static final float В弟诶 = 0.4F;
   private static final float В弟1 = 0.001F;
   private static final String В弟Ь = "cubesTriangleFaces";
   private static final float В弟衣 = 0.0025F;
   private static final float В弟ш = 0.866F;
   private static final float В弟И = 0.5F;
   private static final float В弟ь = 0.5F;
   private static final float В弟ж = 0.5F;
   private static final float В弟弟 = 0.5F;
   private static final String В弟б = "cubesEdges";
   private static final String В弟Ы = "cubesTriangleEdges";
   private static final float В弟l = 1.5F;
   private static final float В弟8 = 1.5F;
   private static final float В弟5 = 1.5F;
   private static final float В弟в = 1.5F;
   private static final float В弟ъ = 1.5F;
   private static final float В弟М = 1.5F;
   private static final float В弟Н = 0.3F;
   private static final float В弟н = 0.25F;
   private static final float В弟Ц = 0.866F;
   private static final float В弟ч = 0.001F;
   private static final float В弟Э = 12.0F;
   private static final String В弟必 = "Падение";
   private static final double В弟щ = 2.0;
   private static final double В弟Г = 4.0;
   private static final double В弟л = 0.55;
   private static final double В弟э = 2.0;
   private static final double В弟ю = 0.8;
   private static final double В弟Щ = 2.0;
   private static final float В弟у = 0.5F;
   private static final float В弟2 = 0.008F;
   private static final float В弟ф = -0.012F;
   private static final float В弟и = 0.012F;
   private static final float В弟9 = 0.5F;
   private static final float В弟о = 0.008F;
   private static final float В弟O = 360.0F;
   private static final float В弟7 = 0.01F;
   private static final float В弟Р = 0.02F;
   private static final float В弟3 = 0.5F;
   private static final float В弟р = 0.01F;
   private static final float В弟6 = 360.0F;
   private static final float В弟_ = 360.0F;
   private static final float В弟Ш = 360.0F;
   private static final float В弟ы = 0.5F;
   private static final float В弟й = 1.5F;
   private static final float В弟Ж = 0.5F;
   private static final float В弟м = 1.5F;
   private static final float В弟К = 0.5F;
   private static final float В弟I = 1.5F;
   private static final double В弟Т = Math.PI;
   private static final double В弟О = 2.0;
   private static final float В弟Ф = 10.0F;
   private static final String В弟Й = "Sampler0";
   private static final long В弟к = 65536L;
   private static final String В弟г = "rose";
   private static final String В弟0 = "images/fireflies/bloom.png";
   private static final float В弟х = 10.0F;
   private static final float В弟У = 6.0F;
   private static final float В弟З = 3.5F;
   private static final float В弟Е = 0.06F;
   private static final float В弟П = 0.14F;
   private static final float В弟西 = 0.25F;

   private int р(BufferBuilder var1, Matrix4f var2, float var3, float var4) {
      this.р(var1, var2, -var3, -var3, var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, var3, -var3, var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, var3, var3, var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var3, var3, var3, 0.0F, 1.0F, var4);
      this.р(var1, var2, var3, -var3, -var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, -var3, -var3, -var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, -var3, var3, -var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, var3, var3, -var3, 0.0F, 1.0F, var4);
      this.р(var1, var2, -var3, var3, var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, var3, var3, var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, var3, var3, -var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var3, var3, -var3, 0.0F, 1.0F, var4);
      this.р(var1, var2, -var3, -var3, -var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, var3, -var3, -var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, var3, -var3, var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var3, -var3, var3, 0.0F, 1.0F, var4);
      this.р(var1, var2, var3, -var3, var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, var3, -var3, -var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, var3, var3, -var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, var3, var3, var3, 0.0F, 1.0F, var4);
      this.р(var1, var2, -var3, -var3, -var3, 0.0F, 0.0F, var4);
      this.р(var1, var2, -var3, -var3, var3, 1.0F, 0.0F, var4);
      this.р(var1, var2, -var3, var3, var3, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var3, var3, -var3, 0.0F, 1.0F, var4);
      return 24;
   }

   private void Ы(float var1, CommandEncoder var2) {
      if (this.Ыо()) {
         BufferBuilder var3 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var4 = 0;
         int var5 = this.Й.size();

         for (int var6 = 0; var6 < var5; var6++) {
            Cubes.FloatingShape var7 = this.Й.get(var6);
            float var8 = var7.е9 * В弟诶;
            if (!(var8 <= В弟1)) {
               var4 += this.Ы(var3, var7.Ь, var1, var8);
            }
         }

         this.р(var3, var4, this.сЧ, В弟Ь, var2);
      }
   }

   private void р(float var1, CommandEncoder var2) {
      if (this.Ыо()) {
         BufferBuilder var3 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var4 = 0;
         int var5 = this.Й.size();

         for (int var6 = 0; var6 < var5; var6++) {
            Cubes.FloatingShape var7 = this.Й.get(var6);
            float var8 = var7.е9 * В弟Ю;
            if (!(var8 <= В弟Ч)) {
               var4 += this.р(var3, var7.Ь, var1, var8);
            }
         }

         this.р(var3, var4, this.сЮ, В弟Л, var2);
      }
   }

   private void р(float var1, float var2, CommandEncoder var3) {
      BufferBuilder var4 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var5 = 0;
      int var6 = this.Й.size();

      for (int var7 = 0; var7 < var6; var7++) {
         Cubes.FloatingShape var8 = this.Й.get(var7);
         float var9 = var8.е9;
         Matrix4f var10 = var8.衣;

         for (int var11 = 0; var11 < 3; var11++) {
            float var12 = var1 * А_at_206f93c3[var11] * var2;
            float var13 = var9 * field_4[var11] * var2;
            float var14 = var12 * В弟Ъ;
            var4.addVertex(var10, -var14, var14, 0.0F).setUv(0.0F, 1.0F).setColor(this.ХШ, this.Хы, this.Хй, var13);
            var4.addVertex(var10, var14, var14, 0.0F).setUv(1.0F, 1.0F).setColor(this.ХШ, this.Хы, this.Хй, var13);
            var4.addVertex(var10, var14, -var14, 0.0F).setUv(1.0F, 0.0F).setColor(this.ХШ, this.Хы, this.Хй, var13);
            var4.addVertex(var10, -var14, -var14, 0.0F).setUv(0.0F, 0.0F).setColor(this.ХШ, this.Хы, this.Хй, var13);
            var5 += 4;
         }
      }

      this.р(var4, ю, var5, this.са, В弟т, var3);
   }

   private float method_2(int var1, float var2) {
      if (var1 == 1 || var1 == 2) {
         return var2;
      } else {
         return var1 != 3 && var1 != 4 ? 0.0F : -var2;
      }
   }

   private int р(BufferBuilder var1, Matrix4f var2, float[] var3, float var4, float var5, float var6, float var7) {
      for (int var8 = 0; var8 < var3.length; var8 += 6) {
         var1.addVertex(var2, var3[var8], var3[var8 + 1], var3[var8 + 2]).setUv(0.0F, 0.0F).setColor(var4, var5, var6, var7);
         var1.addVertex(var2, var3[var8 + 3], var3[var8 + 4], var3[var8 + 5]).setUv(1.0F, 1.0F).setColor(var4, var5, var6, var7);
      }

      return var3.length / 3;
   }

   private int М(BufferBuilder var1, Matrix4f var2, float var3, float var4) {
      float var5 = Math.min(1.0F, this.ХШ * В弟в);
      float var6 = Math.min(1.0F, this.Хы * В弟ъ);
      float var7 = Math.min(1.0F, this.Хй * В弟М);
      this.method_8(var3);
      return this.р(var1, var2, this.Д, var5, var6, var7, var4);
   }

   private void р(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(this.ХШ, this.Хы, this.Хй, var8);
   }

   @Override
   public void onEnable() {
      this.Ф.clear();
      this.Й.clear();
      this.Вв = false;
      this.ХЮ = 0;
   }

   private void р(ArrayList<Float> var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = var5 - var2;
      float var11 = var6 - var3;
      float var12 = var7 - var4;
      float var13 = Mth.sqrt(var10 * var10 + var11 * var11 + var12 * var12);
      if (!(var13 < В弟ч)) {
         float var14 = var10 / var13;
         float var15 = var11 / var13;
         float var16 = var12 / var13;
         float var17 = 0.0F;

         for (boolean var18 = true; var17 < var13; var18 = !var18) {
            float var19 = var18 ? var8 : var9;
            float var20 = Math.min(var17 + var19, var13);
            if (var18) {
               var1.add(var2 + var14 * var17);
               var1.add(var3 + var15 * var17);
               var1.add(var4 + var16 * var17);
               var1.add(var2 + var14 * var20);
               var1.add(var3 + var15 * var20);
               var1.add(var4 + var16 * var20);
            }

            var17 = var20;
         }
      }
   }

   private void Эш() {
      for (int var1 = 0; var1 < this.Х_at_206f93c3.length; var1++) {
         GpuBuffer var2 = this.Х_at_206f93c3[var1];
         if (var2 != null && !var2.isClosed()) {
            var2.close();
         }

         this.Х_at_206f93c3[var1] = null;
         this.Я_ff204daf_at_206f93c3[var1] = 0L;
      }
   }

   private void С(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.Ю != null && this.Ю != MINECRAFT_CLIENT.level) {
            this.Ф.clear();
         }

         this.Ю = MINECRAFT_CLIENT.level;
         if (MINECRAFT_CLIENT.player.tickCount == 1) {
            this.Ф.clear();
         }

         boolean var2 = MINECRAFT_CLIENT.options.keyAttack.isDown();
         if (var2 && !this.Вв) {
            this.Эл();
         }

         this.Вв = var2;
         this.ХЮ++;
         if (this.ХЮ % 2 == 0) {
            this.Э1();
         }

         this.д(var1);
      } else {
         this.Ф.clear();
         this.Ю = null;
      }
   }

   private int Ы(BufferBuilder var1, Matrix4f var2, float var3, float var4) {
      float var5 = var3;
      float var6 = -var3;
      float var7 = var3 * В弟ш;
      this.р(var1, var2, 0.0F, var5, 0.0F, В弟И, 1.0F, var4);
      this.р(var1, var2, -var7, var6, var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, var7, var6, var7, 1.0F, 0.0F, var4);
      this.р(var1, var2, 0.0F, var5, 0.0F, В弟ь, 1.0F, var4);
      this.р(var1, var2, var7, var6, var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, var7, var6, -var7, 1.0F, 0.0F, var4);
      this.р(var1, var2, 0.0F, var5, 0.0F, В弟ж, 1.0F, var4);
      this.р(var1, var2, var7, var6, -var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, -var7, var6, -var7, 1.0F, 0.0F, var4);
      this.р(var1, var2, 0.0F, var5, 0.0F, В弟弟, 1.0F, var4);
      this.р(var1, var2, -var7, var6, -var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, -var7, var6, var7, 1.0F, 0.0F, var4);
      this.р(var1, var2, -var7, var6, var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, var7, var6, var7, 1.0F, 0.0F, var4);
      this.р(var1, var2, var7, var6, -var7, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var7, var6, var7, 0.0F, 0.0F, var4);
      this.р(var1, var2, var7, var6, -var7, 1.0F, 1.0F, var4);
      this.р(var1, var2, -var7, var6, -var7, 0.0F, 1.0F, var4);
      return 18;
   }

   private GpuBuffer р(int var1, int var2, String var3) {
      GpuBuffer var4 = this.Х_at_206f93c3[var1];
      if (var4 != null && !var4.isClosed() && this.Я_ff204daf_at_206f93c3[var1] >= var2) {
         return var4;
      }

      if (var4 != null && !var4.isClosed()) {
         var4.close();
      }

      long var5 = В弟к;

      while (var5 < var2) {
         var5 <<= 1;
      }

      var4 = RenderSystem.getDevice().createBuffer(() -> var3 + "Vertices", 40, var5);
      this.Х_at_206f93c3[var1] = var4;
      this.Я_ff204daf_at_206f93c3[var1] = var5;
      return var4;
   }

   private static float[] р(ArrayList<Float> var0) {
      float[] var1 = new float[var0.size()];

      for (int var2 = 0; var2 < var0.size(); var2++) {
         var1[var2] = (Float)var0.get(var2);
      }

      return var1;
   }

   private int Э(BufferBuilder var1, Matrix4f var2, float var3, float var4) {
      float var5 = Math.min(1.0F, this.ХШ * В弟l);
      float var6 = Math.min(1.0F, this.Хы * В弟8);
      float var7 = Math.min(1.0F, this.Хй * В弟5);
      this.method_8(var3);
      return this.р(var1, var2, this.п_at_206f93c3, var5, var6, var7, var4);
   }

   public Cubes() {
      super(Вж8, Вж5, ModuleCategory.RENDER);
      this.Н = new SingleChoiceSetting(Вжв, new ChoiceOption(Вжъ), new ChoiceOption(ВжМ));
      this.н = new SingleChoiceSetting(ВжН, new ChoiceOption(Вжн), new ChoiceOption(ВжЦ));
      this.ср = new FloatSetting(Вжч, ВжЭ, Вж必, Вжщ).withStep(1.0F);
      this.с6 = new FloatSetting(ВжГ, 1.0F, Вжл, Вжэ).withStep(Вжю);
      this.с_ = new FloatSetting(ВжЩ, 1.0F, Вжу, Вж2).withStep(Вжф);
      this.Ф = new ArrayList<>();
      this.Й = new ArrayList<>();
      this.Я_5650d2a3_at_206f93c3 = new Random();
      this.Ха = Вжи;
      this.Х_at_206f93c3 = new GpuBuffer[3];
      this.Я_ff204daf_at_206f93c3 = new long[3];
      this.ХЖ = Float.NaN;
      this.п_at_206f93c3 = new float[0];
      this.Д = new float[0];
      this.са = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Вж9)
         .withVertexShader(ClientResourceIds.getCoreId(Вжо))
         .withFragmentShader(ClientResourceIds.getCoreId(ВжO))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Вж7)
         .build();
      this.сЮ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВжР)
         .withVertexShader(ClientResourceIds.getCoreId(Вж3))
         .withFragmentShader(ClientResourceIds.getCoreId(Вжр))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.сЧ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Вж6)
         .withVertexShader(ClientResourceIds.getCoreId(Вж_))
         .withFragmentShader(ClientResourceIds.getCoreId(ВжШ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.TRIANGLES)
         .build();
      this.сЛ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Вжы)
         .withVertexShader(ClientResourceIds.getCoreId(Вжй))
         .withFragmentShader(ClientResourceIds.getCoreId(ВжЖ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.DEBUG_LINES)
         .build();
      this.су = this::С;
      this.с2 = this::Т;
      this.addSettings(this.Н, this.н, this.ср, this.с6, this.с_);
   }

   private void М(float var1, CommandEncoder var2) {
      BufferBuilder var3 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.DEBUG_LINES, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var4 = 0;
      int var5 = this.Й.size();

      for (int var6 = 0; var6 < var5; var6++) {
         Cubes.FloatingShape var7 = this.Й.get(var6);
         var4 += this.М(var3, var7.Ь, var1, var7.е9);
      }

      this.р(var3, var4, this.сЛ, В弟Ы, var2);
   }

   private void д(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         PoseStack var2 = var1.getPoses();
         Vec3 var3 = var1.getCameraPosition();
         double var4 = var3.x;
         double var6 = var3.y;
         double var8 = var3.z;
         float var10 = В弟с * this.с6.getValue();
         float var11 = В弟В;
         int var12 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         if (var12 != this.Ха) {
            this.Ха = var12;
            this.ХШ = (var12 >> 16 & 0xFF) / В弟Я;
            this.Хы = (var12 >> 8 & 0xFF) / В弟Х;
            this.Хй = (var12 & 0xFF) / В弟е;
         }

         this.Й.clear();
         Camera var13 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         float var14 = (float)Math.toRadians(var13.yRot());
         float var15 = (float)Math.toRadians(var13.xRot());
         double var16 = -Mth.sin(var14) * Mth.cos(var15);
         double var18 = -Mth.sin(var15);
         double var20 = Mth.cos(var14) * Mth.cos(var15);
         int var22 = this.Ф.size();

         for (int var23 = 0; var23 < var22; var23++) {
            Cubes.FloatingShape var24 = this.Ф.get(var23);
            double var25 = var24.сЗ - var4;
            double var27 = var24.сЕ - var6;
            double var29 = var24.сП - var8;
            double var31 = var25 * var25 + var27 * var27 + var29 * var29;
            if (!(var31 > В弟А) && !(var25 * var16 + var27 * var18 + var29 * var20 < В弟4)) {
               var24.е9 = this.р(var24);
               if (!(var24.е9 <= В弟п)) {
                  var24.Ь
                     .set(var2.last().pose())
                     .translate((float)var25, (float)var27, (float)var29)
                     .rotateX((float)Math.toRadians(var24.ел))
                     .rotateY((float)Math.toRadians(var24.еэ))
                     .rotateZ((float)Math.toRadians(var24.ею));
                  var24.衣
                     .set(var2.last().pose())
                     .translate((float)var25, (float)var27, (float)var29)
                     .rotateY((float)Math.toRadians(-var13.yRot()))
                     .rotateX((float)Math.toRadians(var13.xRot()));
                  this.Й.add(var24);
               }
            }
         }

         if (!this.Й.isEmpty()) {
            CommandEncoder var33 = RenderSystem.getDevice().createCommandEncoder();
            this.р(var10, var11, var33);
            boolean var34 = this.н.isSelected(В弟Д);
            if (var34) {
               this.р(var10, var33);
               this.Э(var10, var33);
            } else {
               this.Ы(var10, var33);
               this.М(var10, var33);
            }
         }
      }
   }

   private void Т(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.PRE) {
         this.Эл();
      }
   }

   private void method_8(float var1) {
      if (Float.compare(this.ХЖ, var1) != 0) {
         this.ХЖ = var1;
         ArrayList var2 = new ArrayList();
         float var3 = var1 * В弟Н;
         float var4 = var1 * В弟н;

         for (byte[] var8 : В) {
            this.р(var2, var8[0] * var1, var8[1] * var1, var8[2] * var1, var8[3] * var1, var8[4] * var1, var8[5] * var1, var3, var4);
         }

         this.п_at_206f93c3 = р(var2);
         ArrayList var15 = new ArrayList();
         float var16 = var1;
         float var17 = -var1;
         float var18 = var1 * В弟Ц;

         for (byte[] var12 : Я_ce4c9511_at_206f93c3) {
            byte var13 = var12[0];
            byte var14 = var12[1];
            this.р(
               var15,
               this.Т(var13, var18),
               var13 == 0 ? var16 : var17,
               this.method_2(var13, var18),
               this.Т(var14, var18),
               var14 == 0 ? var16 : var17,
               this.method_2(var14, var18),
               var3,
               var4
            );
         }

         this.Д = р(var15);
      }
   }

   @Override
   public void onDisable() {
      this.Ф.clear();
      this.Й.clear();
      this.Ю = null;
      this.Эш();
   }

   private void Эл() {
      Camera var1 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
      if (!this.Ф.isEmpty() && var1 != null) {
         Vec3 var2 = var1.position();
         double var3 = var2.x;
         double var5 = var2.y;
         double var7 = var2.z;
         float var9 = (float)Math.toRadians(var1.yRot());
         float var10 = (float)Math.toRadians(var1.xRot());
         double var11 = -Mth.sin(var9) * Mth.cos(var10);
         double var13 = -Mth.sin(var10);
         double var15 = Mth.cos(var9) * Mth.cos(var10);
         Cubes.FloatingShape var17 = null;
         double var18 = Вжм;
         int var20 = this.Ф.size();

         for (int var21 = 0; var21 < var20; var21++) {
            Cubes.FloatingShape var22 = this.Ф.get(var21);
            double var23 = var22.сЗ - var3;
            double var25 = var22.сЕ - var5;
            double var27 = var22.сП - var7;
            double var29 = var23 * var11 + var25 * var13 + var27 * var15;
            if (!(var29 < 0.0) && !(var29 > ВжК)) {
               double var31 = var3 + var11 * var29;
               double var33 = var5 + var13 * var29;
               double var35 = var7 + var15 * var29;
               double var37 = var22.сЗ - var31;
               double var39 = var22.сЕ - var33;
               double var41 = var22.сП - var35;
               double var43 = var37 * var37 + var39 * var39 + var41 * var41;
               if (!(var43 > ВжI) && !(var29 >= var18)) {
                  var18 = var29;
                  var17 = var22;
               }
            }
         }

         if (var17 != null) {
            double var45 = ВжТ * this.с_.getValue().floatValue();
            var17.е必 += (float)(var11 * var45);
            var17.ещ = var17.ещ + (float)(var13 * var45 + ВжО);
            var17.еГ += (float)(var15 * var45);
         }
      }
   }

   private void р(BufferBuilder var1, int var2, RenderPipeline var3, String var4, CommandEncoder var5) {
      MeshData var6 = var1.build();
      if (var6 != null) {
         if (var2 == 0) {
            var6.close();
         } else {
            try {
               DrawState var7 = var6.drawState();
               int var8 = var7.indexCount();
               AutoStorageIndexBuffer var9 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
               GpuBuffer var10 = var9.getBuffer(var8);
               IndexType var11 = var9.type();
               int var12 = var3 == this.сЛ ? 2 : 1;
               int var13 = var6.vertexBuffer().remaining();
               GpuBuffer var14 = this.р(var12, var13, var4);
               var5.writeToBuffer(var14.slice(0L, var13), var6.vertexBuffer());
               RenderTarget var15 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

               try (RenderPass var16 = var5.createRenderPass(
                     () -> var4, var15.getColorTextureView(), Optional.empty(), var15.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var16);
                  var16.setPipeline(var3);
                  var16.setVertexBuffer(0, var14.slice());
                  var16.setIndexBuffer(var10, var11);
                  var16.drawIndexed(var8, 1, 0, 0, 0);
               }
            } finally {
               var6.close();
            }
         }
      }
   }

   private void Э(float var1, CommandEncoder var2) {
      BufferBuilder var3 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.DEBUG_LINES, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var4 = 0;
      int var5 = this.Й.size();

      for (int var6 = 0; var6 < var5; var6++) {
         Cubes.FloatingShape var7 = this.Й.get(var6);
         var4 += this.Э(var3, var7.Ь, var1, var7.е9);
      }

      this.р(var3, var4, this.сЛ, В弟б, var2);
   }

   private Cubes.FloatingShape р() {
      float var1 = В弟Э;
      boolean var2 = this.Н.isSelected(В弟必);
      int var3 = var2 ? 260 + this.Я_5650d2a3_at_206f93c3.nextInt(220) : 420 + this.Я_5650d2a3_at_206f93c3.nextInt(420);
      double var4 = MINECRAFT_CLIENT.player.getX() + (this.Я_5650d2a3_at_206f93c3.nextDouble() * В弟щ - 1.0) * var1;
      double var6 = var2
         ? MINECRAFT_CLIENT.player.getY() + В弟Г + this.Я_5650d2a3_at_206f93c3.nextDouble() * (var1 * В弟л)
         : MINECRAFT_CLIENT.player.getY() + В弟э + this.Я_5650d2a3_at_206f93c3.nextDouble() * (var1 * В弟ю);
      double var8 = MINECRAFT_CLIENT.player.getZ() + (this.Я_5650d2a3_at_206f93c3.nextDouble() * В弟Щ - 1.0) * var1;
      float var10 = this.с_.getValue();
      float var11;
      float var12;
      float var13;
      if (var2) {
         var11 = (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟у) * В弟2 * var10;
         var12 = (В弟ф - this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟и) * var10;
         var13 = (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟9) * В弟о * var10;
      } else {
         float var14 = this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟O;
         float var15 = (В弟7 + this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟Р) * var10;
         var11 = -Mth.sin((float)Math.toRadians(var14)) * var15;
         var13 = Mth.cos((float)Math.toRadians(var14)) * var15;
         var12 = (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟3) * В弟р * var10;
      }

      return new Cubes.FloatingShape(
         var4,
         var6,
         var8,
         var11,
         var12,
         var13,
         this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟6,
         this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟_,
         this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟Ш,
         (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟ы) * В弟й,
         (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟Ж) * В弟м,
         (this.Я_5650d2a3_at_206f93c3.nextFloat() - В弟К) * В弟I,
         var3,
         (float)(this.Я_5650d2a3_at_206f93c3.nextDouble() * В弟Т * В弟О),
         this.Я_5650d2a3_at_206f93c3.nextFloat() * В弟Ф
      );
   }

   private boolean Ыо() {
      int var1 = this.Й.size();

      for (int var2 = 0; var2 < var1; var2++) {
         if (this.Й.get(var2).е9 > В弟衣) {
            return true;
         }
      }

      return false;
   }

   private void Э1() {
      int var1 = this.ср.getValue().intValue();
      int var2 = this.Ф.size();
      if (var2 < var1) {
         int var3 = Math.min(var1 - var2, 5);

         for (int var4 = 0; var4 < var3; var4++) {
            this.Ф.add(this.р());
         }
      } else if (var2 > var1) {
         this.Ф.subList(var1, var2).clear();
      }

      float var24 = ВжФ * this.с_.getValue();
      float var25 = ВжЙ;
      boolean var5 = this.Н.isSelected(Вжк);
      double var6 = MINECRAFT_CLIENT.player.getX();
      double var8 = MINECRAFT_CLIENT.player.getY();
      double var10 = MINECRAFT_CLIENT.player.getZ();
      double var12 = var25 * var25 * Вжг;

      for (int var14 = this.Ф.size() - 1; var14 >= 0; var14--) {
         Cubes.FloatingShape var15 = this.Ф.get(var14);
         if (var5) {
            var15.еф = var15.еф + Вж0 * var24;
            var15.сЗ = var15.сЗ + (var15.е必 * var24 + Math.sin(var15.еф + var15.еи) * Вжх * var24);
            var15.сЕ = var15.сЕ + var15.ещ * var24;
            var15.сП = var15.сП + (var15.еГ * var24 + Math.cos(var15.еф * ВжУ + var15.еи) * ВжЗ * var24);
            var15.ещ = Math.max(var15.ещ - ВжЕ * var24, ВжП);
            var15.ел = var15.ел + var15.еЩ * Вж西 * var24;
            var15.еэ = var15.еэ + var15.еу * Вжд * var24;
            var15.ею = var15.ею + var15.е2 * ВжБ * var24;
         } else {
            var15.сЗ = var15.сЗ + var15.е必 * var24;
            var15.сЕ = var15.сЕ + var15.ещ * var24;
            var15.сП = var15.сП + var15.еГ * var24;
            var15.ел = var15.ел + var15.еЩ * var24;
            var15.еэ = var15.еэ + var15.еу * var24;
            var15.ею = var15.ею + var15.е2 * var24;
            var15.е必 = var15.е必 * ВжС;
            var15.ещ = var15.ещ * Вжз;
            var15.еГ = var15.еГ * Вжц;
         }

         var15.ХЩ--;
         double var16 = var15.сЗ - var6;
         double var18 = var15.сЕ - var8;
         double var20 = var15.сП - var10;
         double var22 = var16 * var16 + var18 * var18 + var20 * var20;
         if (var15.ХЩ <= 0 || var22 > var12 || var5 && var15.сЕ < var8 - Вжя) {
            this.Ф.remove(var14);
            this.Ф.add(this.р());
         }
      }
   }

   private float р(Cubes.FloatingShape var1) {
      float var2 = Mth.clamp((float)var1.ХЩ / var1.Ху, 0.0F, 1.0F);
      float var3 = Math.min(1.0F, (var1.Ху - var1.ХЩ) / В弟а);
      return var2 * var3;
   }

   private void р(BufferBuilder var1, Identifier var2, int var3, RenderPipeline var4, String var5, CommandEncoder var6) {
      MeshData var7 = var1.build();
      if (var7 != null) {
         if (var3 == 0) {
            var7.close();
         } else {
            try {
               DrawState var8 = var7.drawState();
               int var9 = var8.indexCount();
               AutoStorageIndexBuffer var10 = RenderSystem.getSequentialBuffer(var8.primitiveTopology());
               GpuBuffer var11 = var10.getBuffer(var9);
               IndexType var12 = var10.type();
               int var13 = var7.vertexBuffer().remaining();
               GpuBuffer var14 = this.р(0, var13, var5);
               var6.writeToBuffer(var14.slice(0L, var13), var7.vertexBuffer());
               RenderTarget var15 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               AbstractTexture var16 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var17 = var6.createRenderPass(
                     () -> var5, var15.getColorTextureView(), Optional.empty(), var15.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var17);
                  var17.setPipeline(var4);
                  var17.bindTexture(В弟Й, var16.getTextureView(), var16.getSampler());
                  var17.setVertexBuffer(0, var14.slice());
                  var17.setIndexBuffer(var11, var12);
                  var17.drawIndexed(var9, 1, 0, 0, 0);
               }
            } finally {
               var7.close();
            }
         }
      }
   }

   private float Т(int var1, float var2) {
      if (var1 == 1 || var1 == 4) {
         return -var2;
      } else {
         return var1 != 2 && var1 != 3 ? 0.0F : var2;
      }
   }

   private static class FloatingShape {
      double сЗ;
      double сЕ;
      double сП;
      float е必;
      float ещ;
      float еГ;
      float ел;
      float еэ;
      float ею;
      float еЩ;
      float еу;
      float е2;
      float еф;
      float еи;
      float е9;
      final Matrix4f Ь = new Matrix4f();
      final Matrix4f 衣 = new Matrix4f();
      int ХЩ;
      int Ху;

      FloatingShape(
         double var1,
         double var3,
         double var5,
         float var7,
         float var8,
         float var9,
         float var10,
         float var11,
         float var12,
         float var13,
         float var14,
         float var15,
         int var16,
         float var17,
         float var18
      ) {
         this.сЗ = var1;
         this.сЕ = var3;
         this.сП = var5;
         this.е必 = var7;
         this.ещ = var8;
         this.еГ = var9;
         this.ел = var10;
         this.еэ = var11;
         this.ею = var12;
         this.еЩ = var13;
         this.еу = var14;
         this.е2 = var15;
         this.ХЩ = this.Ху = var16;
         this.еф = var17;
         this.еи = var18;
      }
   }
}
