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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.render.WorldProjection;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.HitMarker;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.UiDrawing;

public class HitMarkers extends ClientModule {
   private static final Identifier я = Identifier.fromNamespaceAndPath(HitMarkers.ВмВ, HitMarkers.ВмЯ);
   private static final double Яй = 0.3;
   private static final double ЯЖ = 0.15;
   private static final double Ям = 0.55;
   private static final double ЯК = 0.5;
   private static final float тц = 0.01F;
   private static final float тя = 0.01F;
   private static final float ас = 0.01F;
   private static final float аВ = 1.70158F;
   private static final double ЯI = 3.0;
   private static final double ЯТ = 2.0;
   private static final float аЯ = 4.0F;
   private static final float аХ = 0.6F;
   private static final float ае = 0.4F;
   private static final float аА = 0.7F;
   private static final float а4 = 3.33F;
   private static final float ап = 0.3F;
   private static final float аД = 0.4F;
   private static final float аЪ = 0.001F;
   private static final float ат = 0.5F;
   private static final float аа = 0.5F;
   private static final float аЮ = 0.65F;
   private static final float аЧ = 4.0F;
   private static final float аЛ = 6.0F;
   private static final float а诶 = 0.8F;
   private static final float а1 = 0.2F;
   private static final float аЬ = 0.3F;
   private static final float а衣 = 4.5F;
   private static final float аш = 0.9F;
   private static final float аИ = 0.1F;
   private static final float аь = 1.2F;
   private static final float аж = 0.8F;
   private static final float а弟 = 2.5F;
   private static final int Ъ诶 = -16777216;
   private static final float аб = 0.5F;
   private static final float аЫ = 1.85F;
   private static final float аl = 0.25F;
   private static final float а8 = 1.25F;
   private static final float а5 = 1.15F;
   private static final float ав = 2.2F;
   private static final int Ъ1 = -12779716;
   private static final int ЪЬ = -50116;
   private static final int Ъ衣 = -3618616;
   private static final String Як = "MISS";
   private static final float аъ = 0.001F;
   private static final String Яг = "-%.1f";
   private static final String Я0 = "+%.1f";
   private static final float аМ = 0.01F;
   private static final float аН = 0.001F;
   private static final float ан = 8.0F;
   private static final float аЦ = 0.6F;
   private static final float ач = 2.5F;
   private static final double ЯО = 0.3;
   private static final double ЯФ = 0.7;
   private static final float аЭ = 0.88F;
   private static final float а必 = 0.14F;
   private static final float ащ = 5.0F;
   private static final float аГ = 0.18F;
   private static final float ал = 7.0F;
   private static final float аэ = 0.65F;
   private static final float аю = 0.7F;
   private static final float аЩ = 7.0F;
   private static final float ау = 0.65F;
   private static final float а2 = 0.7F;
   private static final float аф = 0.6F;
   private static final float аи = 0.7F;
   private static final float а9 = 0.01F;
   private static final int Ъш = -10128;
   private static final float ао = 0.45F;
   private static final int ЪИ = -16777216;
   private static final float аO = 0.92F;
   private static final float а7 = 0.16F;
   private static final float аР = 5.0F;
   private static final float а3 = 0.18F;
   private static final int Ъь = -46518;
   private static final int Ъж = -10944645;
   private static final float ар = 0.35F;
   private static final int Ъ弟 = -7286;
   private static final float а6 = 0.45F;
   private final SingleChoiceSetting с5;
   private final SingleChoiceSetting св;
   @SerializedName("3")
   private final ColorSetting field_3;
   private final ColorSetting р;
   private final FloatSetting ее;
   private final FloatSetting еА;
   private final BooleanSetting Яв;
   private final BooleanSetting Яъ;
   private final List<HitMarker> ВД;
   private final RenderPipeline Вт;
   private final RenderPipeline Ва;
   private final EventListener<AttackEntityEvent> Хг;
   private final EventListener<WorldEffectsEvent> Х0;
   private final EventListener<HudRenderEvent> Хх;
   private static final String ВйЬ = "HitMarkers";
   private static final String Вй衣 = "Маркеры попадания и плавающий урон";
   private static final String Вйш = "Режим";
   private static final String ВйИ = "3D";
   private static final String Вйь = "2D";
   private static final String Вйж = "Текст";
   private static final String Вй弟 = "Цвет";
   private static final String Вйб = "Тема";
   private static final String ВйЫ = "Статичный";
   private static final String Вйl = "По ХП";
   private static final String Вй8 = "Статичный цвет";
   private static final String Вй5 = "Цвет крита";
   private static final String Вйв = "Размер";
   private static final float Вйъ = 0.45F;
   private static final float ВйМ = 0.15F;
   private static final float ВйН = 1.2F;
   private static final float Вйн = 0.05F;
   private static final String ВйЦ = "Длительность (мс)";
   private static final float Вйч = 550.0F;
   private static final float ВйЭ = 200.0F;
   private static final float Вй必 = 1500.0F;
   private static final float Вйщ = 50.0F;
   private static final String ВйГ = "Эффект крита";
   private static final String Вйл = "Плавающий урон";
   private static final String Вйэ = "rose/hit_markers_bloom";
   private static final String Вйю = "dead_chams";
   private static final String ВйЩ = "dead_chams";
   private static final String Вйу = "Sampler0";
   private static final String Вй2 = "rose/hit_markers_cross";
   private static final String Вйф = "sixth_sense";
   private static final String Вйи = "sixth_sense";
   private static final double Вй9 = 0.3;
   private static final double Вйо = 0.15;
   private static final double ВйO = 0.5;
   private static final double Вй7 = 0.55;
   private static final double ВйР = 0.5;
   private static final double Вй3 = 0.5;
   private static final String Вйр = "3D";
   private static final float Вй6 = 0.01F;
   private static final float Вй_ = 0.6F;
   private static final float ВйШ = 0.4F;
   private static final float Вйы = 4.0F;
   private static final float Вйй = 0.7F;
   private static final float ВйЖ = 3.33F;
   private static final float Вйм = 0.3F;
   private static final float ВйК = 0.4F;
   private static final String ВйI = "hitMarkersBloom";
   private static final String ВйТ = "2D";
   private static final String ВйО = "Текст";
   private static final String ВйФ = "Текст";
   private static final float ВйЙ = 0.01F;
   private static final float Вйк = 4.0F;
   private static final float Вйг = 6.0F;
   private static final float Вй0 = 0.8F;
   private static final float Вйх = 0.2F;
   private static final float ВйУ = 0.3F;
   private static final float ВйЗ = 4.5F;
   private static final float ВйЕ = 0.9F;
   private static final float ВйП = 0.1F;
   private static final float Вй西 = 1.2F;
   private static final float Вйд = 0.8F;
   private static final float ВйБ = 0.8F;
   private static final float ВйС = 2.5F;
   private static final int Вйз = -16777216;
   private static final float Вйц = 0.5F;
   private static final float Вйя = 1.85F;
   private static final float ВЖс = 1.85F;
   private static final float ВЖВ = 0.25F;
   private static final float ВЖЯ = 1.25F;
   private static final float ВЖХ = 1.15F;
   private static final float ВЖе = 2.2F;
   private static final String ВЖА = "hitMarkersCross";
   private static final float ВЖ4 = 8.0F;
   private static final float ВЖп = 0.6F;
   private static final float ВЖД = 2.5F;
   private static final float ВЖЪ = 0.01F;
   private static final double ВЖт = 0.3;
   private static final double ВЖа = 0.7;
   private static final int ВЖЮ = -16777216;
   private static final float ВЖЧ = 0.7F;
   private static final float ВЖЛ = 0.88F;
   private static final float ВЖ诶 = 0.14F;
   private static final float ВЖ1 = 5.0F;
   private static final float ВЖЬ = 0.18F;
   private static final float ВЖ衣 = 7.0F;
   private static final float ВЖш = 0.65F;
   private static final float ВЖИ = 0.5F;
   private static final float ВЖь = 0.5F;
   private static final float ВЖж = 0.01F;
   private static final float ВЖ弟 = 0.6F;
   private static final float ВЖб = 0.7F;
   private static final float ВЖЫ = 0.01F;
   private static final double ВЖl = 0.3;
   private static final double ВЖ8 = 0.7;
   private static final String ВЖ5 = "%.1f";
   private static final int ВЖв = -16777216;
   private static final float ВЖъ = 0.7F;
   private static final float ВЖМ = 0.92F;
   private static final float ВЖН = 0.16F;
   private static final float ВЖн = 5.0F;
   private static final float ВЖЦ = 0.18F;
   private static final float ВЖч = 7.0F;
   private static final float ВЖЭ = 0.65F;
   private static final float ВЖ必 = 0.5F;
   private static final float ВЖщ = 0.5F;
   private static final float ВЖГ = -0.65F;
   private static final float ВЖл = -0.65F;
   private static final float ВЖэ = 0.65F;
   private static final float ВЖю = 0.65F;
   private static final float ВЖЩ = 0.001F;
   private static final float ВЖу = 0.5F;
   private static final float ВЖ2 = 0.5F;
   private static final float ВЖф = -1.0F;
   private static final String ВЖи = "Статичный";
   private static final String ВЖ9 = "По ХП";
   private static final int ВЖо = -46518;
   private static final int ВЖO = -10944645;
   private static final float ВЖ7 = 0.35F;
   private static final float ВЖР = 0.35F;
   private static final String ВЖ3 = "Статичный";
   private static final float ВЖр = 0.45F;
   private static final float ВЖ6 = 0.45F;
   private static final String ВЖ_ = "MISS";
   private static final int ВЖШ = -3618616;
   private static final String ВЖы = "+";
   private static final int ВЖй = -50116;
   private static final String ВЖЖ = "-";
   private static final int ВЖм = -12779716;
   private static final String ВЖК = "MISS";
   private static final float ВЖI = 0.001F;
   private static final String ВЖТ = "-%.1f";
   private static final float ВЖО = 0.001F;
   private static final String ВЖФ = "+%.1f";
   private static final float ВЖЙ = 0.001F;
   private static final String ВЖк = "-%.1f";
   private static final float ВЖг = 0.01F;
   private static final String ВЖ0 = "-%.1f";
   private static final float ВЖх = 6.0F;
   private static final float ВЖУ = 0.5F;
   private static final float ВЖЗ = 1.70158F;
   private static final double ВЖЕ = 3.0;
   private static final double ВЖП = 2.0;
   private static final float ВЖ西 = 3.0F;
   private static final String ВЖд = "Sampler0";
   private static final String ВЖБ = "Текст";
   private static final String ВЖС = "Текст";
   private static final String ВЖз = "Статичный";
   private static final String ВЖц = "Текст";
   private static final String ВЖя = "Статичный";
   private static final String Вмс = "Текст";
   private static final String ВмВ = "rose";
   private static final String ВмЯ = "images/hitmarkers/bloom.png";

   private void р(long var1) {
      float var3 = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth();
      float var4 = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
      float var5 = var3 / 2.0F;
      float var6 = var4 / 2.0F;
      BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);
      int var8 = 0;

      for (HitMarker var10 : this.ВД) {
         float var11 = var10.ж(var1);
         float var12 = Ущ(var11);
         if (!(var12 <= ВйЙ)) {
            float var13 = УЪ(Math.min(1.0F, var11 * Вйк));
            float var14 = this.ее.getValue() * Вйг * (Вй0 + Вйх * var13) * (1.0F + var11 * ВйУ);
            float var15 = this.ее.getValue() * ВйЗ * (ВйЕ + ВйП * var13);
            float var16 = this.ее.getValue() * Вй西 * Mth.clamp(Вйд, ВйБ, ВйС);
            int var17 = ColorMath.withAlpha(Вйз, var12 * Вйц);
            int var18 = ColorMath.withAlpha(this.Р_a83dd0cc(var10), var12);
            var8 += this.Р(var7, var5, var6, var3, var4, var14 * Вйя, var15, var16 * ВЖс, var17);
            var8 += this.Р(var7, var5, var6, var3, var4, var14, var15, var16, var18);
            if (var10.Я3) {
               float var19 = var12 * ВЖВ * (1.0F - var11);
               int var20 = ColorMath.withAlpha(this.я(var10), var19);
               var8 += this.Р(var7, var5, var6, var3, var4, var14 * ВЖЯ, var15 * ВЖХ, var16 * ВЖе, var20);
            }
         }
      }

      this.я(var7, var8, this.Ва, ВЖА);
   }

   private void а(long var1) {
      float var3 = Math.max(1.0F, MINECRAFT_CLIENT.getWindow().getGuiScale());

      for (HitMarker var5 : this.ВД) {
         float var6 = var5.ж(var1);
         float var7 = Уь(Math.min(1.0F, var6 * ВЖ4)) * (1.0F - УЭ(Math.max(0.0F, var6 - ВЖп) * ВЖД));
         var7 = УГ(var7);
         if (!(var7 <= ВЖЪ)) {
            Vec3 var8 = var5.field_6.add(0.0, ВЖт + УЭ(var6) * ВЖа, 0.0);
            Vector2f var9 = WorldProjection.projectToScreen(var8);
            if (var9 != null) {
               String var10 = this.Р_388008bb(var5);
               int var11 = ColorMath.withAlpha(this.Р(var5, var10), var7);
               int var12 = ColorMath.withAlpha(ВЖЮ, var7 * ВЖЧ);
               float var13 = ВЖЛ + (var5.Я3 ? ВЖ诶 : 0.0F) + (1.0F - УЭ(Math.min(1.0F, var6 * ВЖ1))) * ВЖЬ;
               float var14 = ВЖ衣 * var3 * var13;
               float var15 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var10, var14, 1.0F);
               float var16 = ВЖш * var3;
               UiDrawing.drawText(ClientFonts.getRegularFont(), var10, var9.x - var15 / 2.0F + var16, var9.y + var16, var14, var12, 0.0F, ВЖИ, 1.0F);
               UiDrawing.drawText(ClientFonts.getRegularFont(), var10, var9.x - var15 / 2.0F, var9.y, var14, var11, 0.0F, ВЖь, 1.0F);
            }
         }
      }
   }

   private int в(HitMarker var1) {
      return var1.Я3 ? ColorMath.blendArgb(this.Р_a83dd0cc(var1), -10128, ВЖ6) : this.Р_a83dd0cc(var1);
   }

   private int я(HitMarker var1) {
      return this.св.isSelected(ВЖ3) ? this.р.getValue() : ColorMath.blendArgb(this.Р_a83dd0cc(var1), -1, ВЖр);
   }

   private static float УЪ(float var0) {
      float var1 = УГ(var0);
      float var2 = ВЖЗ;
      float var3 = var2 + 1.0F;
      return 1.0F + var3 * (float)Math.pow(var1 - 1.0F, ВЖЕ) + var2 * (float)Math.pow(var1 - 1.0F, ВЖП);
   }

   private static void Р(
      BufferBuilder var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, int var11
   ) {
      Vector4f var12 = ColorMath.toRgbaVector(var11);
      var0.addVertex(Н(var1, var9), Я(var2, var10), 0.0F).setColor(var12.x, var12.y, var12.z, var12.w);
      var0.addVertex(Н(var3, var9), Я(var4, var10), 0.0F).setColor(var12.x, var12.y, var12.z, var12.w);
      var0.addVertex(Н(var5, var9), Я(var6, var10), 0.0F).setColor(var12.x, var12.y, var12.z, var12.w);
      var0.addVertex(Н(var7, var9), Я(var8, var10), 0.0F).setColor(var12.x, var12.y, var12.z, var12.w);
   }

   private static float Уь(float var0) {
      float var1 = УГ(var0);
      return var1 * var1 * (ВЖ西 - 2.0F * var1);
   }

   private float Р_f67ab10a(HitMarker var1) {
      float var2 = 1.0F;
      if (MINECRAFT_CLIENT.level == null) {
         return var2;
      }

      if (MINECRAFT_CLIENT.level.getEntity(var1.АЩ) instanceof LivingEntity var4) {
         float var5 = Math.max(1.0F, var4.getMaxHealth() + Math.max(0.0F, var4.getAbsorptionAmount()));
         var2 = Mth.clamp(Ы(var4) / var5, 0.0F, 1.0F);
      }

      return var2;
   }

   private void ч(long var1) {
      float var3 = Math.max(1.0F, MINECRAFT_CLIENT.getWindow().getGuiScale());

      for (HitMarker var5 : this.ВД) {
         if (var5.Яр && !(var5.field_48 <= ВЖж)) {
            float var6 = var5.ж(var1);
            float var7 = 1.0F - УЭ(var6 * ВЖ弟);
            float var8 = 1.0F - Ун(Math.max(0.0F, var6 - ВЖб) * 2.0F);
            float var9 = УГ(var7 * var8);
            if (!(var9 <= ВЖЫ)) {
               Vec3 var10 = var5.field_6.add(0.0, ВЖl + УЭ(var6) * ВЖ8, 0.0);
               Vector2f var11 = WorldProjection.projectToScreen(var10);
               if (var11 != null) {
                  String var12 = String.format(Locale.US, ВЖ5, var5.field_48);
                  int var13 = ColorMath.withAlpha(this.в(var5), var9);
                  int var14 = ColorMath.withAlpha(ВЖв, var9 * ВЖъ);
                  float var15 = ВЖМ + (var5.Я3 ? ВЖН : 0.0F) + (1.0F - УЭ(Math.min(1.0F, var6 * ВЖн))) * ВЖЦ;
                  float var16 = ВЖч * var3 * var15;
                  float var17 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var12, var16, 1.0F);
                  float var18 = ВЖЭ * var3;
                  UiDrawing.drawText(ClientFonts.getRegularFont(), var12, var11.x - var17 / 2.0F + var18, var11.y + var18, var16, var14, 0.0F, ВЖ必, 1.0F);
                  UiDrawing.drawText(ClientFonts.getRegularFont(), var12, var11.x - var17 / 2.0F, var11.y, var16, var13, 0.0F, ВЖщ, 1.0F);
               }
            }
         }
      }
   }

   private int Р(HitMarker var1, String var2) {
      if (ВЖ_.equals(var2)) {
         return ВЖШ;
      } else if (var2.startsWith(ВЖы)) {
         return ВЖй;
      } else {
         return var2.startsWith(ВЖЖ) ? ВЖм : this.Р_a83dd0cc(var1);
      }
   }

   private static float Ущ(float var0) {
      float var1 = Уь(var0 * ВЖх);
      float var2 = 1.0F - Ун(Math.max(0.0F, var0 - ВЖУ) * 2.0F);
      return УГ(var1 * var2);
   }

   private void method_9(long var1) {
      Iterator var3 = this.ВД.iterator();

      while (var3.hasNext()) {
         HitMarker var4 = (HitMarker)var3.next();
         if (var4.ы(var1)) {
            var3.remove();
         } else if (!var4.Яр && MINECRAFT_CLIENT.level != null) {
            Entity var5 = MINECRAFT_CLIENT.level.getEntity(var4.АЩ);
            if (var5 instanceof LivingEntity var6) {
               float var7 = Ы(var6);
               if (var7 < var4.field_4l) {
                  var4.field_48 = var4.field_4l - var7;
                  var4.Яр = true;
               }
            } else if (var5 == null) {
               var4.field_48 = var4.field_4l;
               var4.Яр = true;
            }
         }
      }
   }

   private int Р_a83dd0cc(HitMarker var1) {
      if (this.св.isSelected(ВЖи)) {
         return var1.Я3 ? this.р.getValue() : this.field_3.getValue();
      } else if (this.св.isSelected(ВЖ9)) {
         float var4 = this.Р_f67ab10a(var1);
         int var3 = ColorMath.blendArgb(ВЖо, ВЖO, var4);
         return var1.Я3 ? ColorMath.blendArgb(var3, -7286, ВЖ7) : var3;
      } else {
         int var2 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         return var1.Я3 ? ColorMath.blendArgb(var2, -7286, ВЖР) : var2;
      }
   }

   private static float Ун(float var0) {
      float var1 = УГ(var0);
      return var1 * var1;
   }

   private int Р(BufferBuilder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      return this.я(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private void УУ(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !this.ВД.isEmpty()) {
         long var2 = System.currentTimeMillis();
         this.method_9(var2);
         if (!this.ВД.isEmpty() && this.с5.isSelected(Вйр)) {
            Camera var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
            Vec3 var5 = var4.position();
            PoseStack var6 = var1.getPoses();
            BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var8 = 0;

            for (HitMarker var10 : this.ВД) {
               float var11 = var10.ж(var2);
               float var12 = Ущ(var11);
               if (!(var12 <= Вй6)) {
                  float var13 = Вй_ + ВйШ * Math.min(1.0F, var11 * Вйы) * УЪ(var11);
                  float var14 = 1.0F - Ун(Math.max(0.0F, var11 - Вйй) * ВйЖ) * Вйм;
                  float var15 = this.ее.getValue() * var13 * var14;
                  float var16 = УЭ(var11) * ВйК;
                  int var17 = ColorMath.withAlpha(this.Р_a83dd0cc(var10), var12);
                  var6.pushPose();
                  var6.translate(var10.field_6.x - var5.x, var10.field_6.y - var5.y + var16, var10.field_6.z - var5.z);
                  var6.mulPose(Axis.YP.rotationDegrees(-var4.yRot()));
                  var6.mulPose(Axis.XP.rotationDegrees(var4.xRot()));
                  var6.scale(var15, var15, var15);
                  var8 += Р(var7, var6.last().pose(), var17);
                  var6.popPose();
               }
            }

            this.Р(var7, я, var8, this.Вт, ВйI);
         }
      }
   }

   private void УЩ(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !this.ВД.isEmpty()) {
         long var2 = System.currentTimeMillis();
         this.method_9(var2);
         if (!this.ВД.isEmpty()) {
            if (this.с5.isSelected(ВйТ)) {
               this.р(var2);
            }

            UiDrawing.setTransform(new Matrix4f());
            if (this.с5.isSelected(ВйО)) {
               this.а(var2);
            }

            if (this.Яъ.getValue() && !this.с5.isSelected(ВйФ)) {
               this.ч(var2);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.ВД.clear();
   }

   public HitMarkers() {
      super(ВйЬ, Вй衣, ModuleCategory.RENDER);
      this.с5 = new SingleChoiceSetting(Вйш, new ChoiceOption(ВйИ), new ChoiceOption(Вйь), new ChoiceOption(Вйж));
      this.св = new SingleChoiceSetting(Вй弟, new ChoiceOption(Вйб), new ChoiceOption(ВйЫ), new ChoiceOption(Вйl)).visibleWhen(() -> !this.с5.isSelected(Вмс));
      this.field_3 = new ColorSetting(Вй8, false, -1).visibleWhen(() -> !this.с5.isSelected(ВЖц) && this.св.isSelected(ВЖя));
      this.р = new ColorSetting(Вй5, false, -10496).visibleWhen(() -> !this.с5.isSelected(ВЖС) && this.св.isSelected(ВЖз));
      this.ее = new FloatSetting(Вйв, Вйъ, ВйМ, ВйН).withStep(Вйн);
      this.еА = new FloatSetting(ВйЦ, Вйч, ВйЭ, Вй必).withStep(Вйщ);
      this.Яв = new BooleanSetting(ВйГ, true);
      this.Яъ = new BooleanSetting(Вйл, true).visibleWhen(() -> !this.с5.isSelected(ВЖБ));
      this.ВД = new ArrayList<>();
      this.Вт = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Вйэ)
         .withVertexShader(ClientResourceIds.getCoreId(Вйю))
         .withFragmentShader(ClientResourceIds.getCoreId(ВйЩ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Вйу)
         .build();
      this.Ва = RenderPipelineBuilder.builder()
         .withLocation(Вй2)
         .withVertexShader(ClientResourceIds.getCoreId(Вйф))
         .withFragmentShader(ClientResourceIds.getCoreId(Вйи))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.Хг = this::ы;
      this.Х0 = this::УУ;
      this.Хх = this::УЩ;
      this.addSettings(this.с5, this.св, this.field_3, this.р, this.ее, this.еА, this.Яв, this.Яъ);
   }

   private void я(BufferBuilder var1, int var2, RenderPipeline var3, String var4) {
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

               try (RenderPass var14 = var13.createRenderPass(() -> var4, var12.getColorTextureView(), Optional.empty())) {
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

   private static int Р(BufferBuilder var0, Matrix4f var1, int var2) {
      Vector4f var3 = ColorMath.toRgbaVector(var2);
      float var4 = ВЖГ;
      float var5 = ВЖл;
      float var6 = ВЖэ;
      float var7 = ВЖю;
      var0.addVertex(var1, var4, var5, 0.0F).setUv(0.0F, 0.0F).setColor(var3.x, var3.y, var3.z, var3.w);
      var0.addVertex(var1, var4, var7, 0.0F).setUv(0.0F, 1.0F).setColor(var3.x, var3.y, var3.z, var3.w);
      var0.addVertex(var1, var6, var7, 0.0F).setUv(1.0F, 1.0F).setColor(var3.x, var3.y, var3.z, var3.w);
      var0.addVertex(var1, var6, var5, 0.0F).setUv(1.0F, 0.0F).setColor(var3.x, var3.y, var3.z, var3.w);
      return 4;
   }

   private static float УЭ(float var0) {
      float var1 = УГ(var0);
      return 1.0F - (1.0F - var1) * (1.0F - var1);
   }

   private String Р_388008bb(HitMarker var1) {
      String var2 = ВЖК;
      if (MINECRAFT_CLIENT.level == null) {
         return var2;
      }

      if (MINECRAFT_CLIENT.level.getEntity(var1.АЩ) instanceof LivingEntity var4) {
         float var5 = Ы(var4);
         if (var5 < var1.field_4l - ВЖI) {
            return String.format(Locale.US, ВЖТ, var1.field_4l - var5);
         }

         if (var5 > var1.field_4l + ВЖО) {
            return String.format(Locale.US, ВЖФ, var5 - var1.field_4l);
         }

         if (var1.Яр && var1.field_48 > ВЖЙ) {
            return String.format(Locale.US, ВЖк, var1.field_48);
         }
      } else if (var1.Яр && var1.field_48 > ВЖг) {
         return String.format(Locale.US, ВЖ0, var1.field_48);
      }

      return var2;
   }

   private int я(BufferBuilder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      float var10 = var6 - var7;
      float var11 = var6 - var8;
      int var12 = 0;
      var12 += this.Р(var1, var2 - var10, var3 - var11, var2 + var10, var3 + var11, var7, var8, var4, var5, var9);
      var12 += this.Р(var1, var2 - var10, var3 + var11, var2 + var10, var3 - var11, var7, var8, var4, var5, var9);
      var12 += this.Р(var1, var2 - var11, var3 - var10, var2 + var11, var3 + var10, var8, var7, var4, var5, var9);
      return var12 + this.Р(var1, var2 - var11, var3 + var10, var2 + var11, var3 - var10, var8, var7, var4, var5, var9);
   }

   private static float УГ(float var0) {
      return Mth.clamp(var0, 0.0F, 1.0F);
   }

   private static float Я(float var0, float var1) {
      return 1.0F - var0 * 2.0F / var1;
   }

   private int Р(BufferBuilder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      float var11 = var4 - var2;
      float var12 = var5 - var3;
      float var13 = Mth.sqrt(var11 * var11 + var12 * var12);
      if (var13 <= ВЖЩ) {
         return 0;
      }

      float var14 = -var12 / var13 * var6 * ВЖу;
      float var15 = var11 / var13 * var7 * ВЖ2;
      Р(var1, var2 + var14, var3 + var15, var2 - var14, var3 - var15, var4 - var14, var5 - var15, var4 + var14, var5 + var15, var8, var9, var10);
      return 4;
   }

   private void Р(BufferBuilder var1, Identifier var2, int var3, RenderPipeline var4, String var5) {
      MeshData var6 = var1.build();
      if (var6 != null) {
         if (var3 == 0) {
            var6.close();
         } else {
            GpuBuffer var7 = null;

            try {
               DrawState var8 = var6.drawState();
               int var9 = var8.indexCount();
               AutoStorageIndexBuffer var10 = RenderSystem.getSequentialBuffer(var8.primitiveTopology());
               GpuBuffer var11 = var10.getBuffer(var9);
               IndexType var12 = var10.type();
               var7 = RenderSystem.getDevice().createBuffer(() -> var5 + "Vertices", 32, var6.vertexBuffer());
               RenderTarget var13 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var15 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var16 = var14.createRenderPass(
                     () -> var5, var13.getColorTextureView(), Optional.empty(), var13.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var16);
                  var16.setPipeline(var4);
                  var16.bindTexture(ВЖд, var15.getTextureView(), var15.getSampler());
                  var16.setVertexBuffer(0, var7.slice());
                  var16.setIndexBuffer(var11, var12);
                  var16.drawIndexed(var9, 1, 0, 0, 0);
               }
            } finally {
               if (var7 != null) {
                  var7.close();
               }

               var6.close();
            }
         }
      }
   }

   private static float Н(float var0, float var1) {
      return ВЖф + var0 * 2.0F / var1;
   }

   private boolean хА() {
      return MINECRAFT_CLIENT.player == null
         ? false
         : MINECRAFT_CLIENT.player.fallDistance > 0.0
            && !MINECRAFT_CLIENT.player.onGround()
            && !MINECRAFT_CLIENT.player.onClimbable()
            && !MINECRAFT_CLIENT.player.isInWater()
            && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.BLINDNESS)
            && !MINECRAFT_CLIENT.player.isPassenger();
   }

   private static float Ы(LivingEntity var0) {
      return var0.getHealth() + Math.max(0.0F, var0.getAbsorptionAmount());
   }

   private void ы(AttackEntityEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPhase() == AttackEntityEvent.Phase.PRE && var1.getAttacker() == MINECRAFT_CLIENT.player) {
            if (var1.getTarget() instanceof LivingEntity var2 && var1.getTarget() != MINECRAFT_CLIENT.player) {
               double var13 = var2.getBbWidth() * Вй9;
               double var5 = var2.getBbHeight() * Вйо;
               Vec3 var7 = new Vec3(
                  var2.getX() + (Math.random() - ВйO) * var13,
                  var2.getY() + var2.getBbHeight() * Вй7 + (Math.random() - ВйР) * var5,
                  var2.getZ() + (Math.random() - Вй3) * var13
               );
               boolean var8 = this.хА() && this.Яв.getValue();
               long var9 = this.еА.getValue().longValue();
               long var11 = System.currentTimeMillis();
               this.ВД.add(new HitMarker(var7, var11, var9, var8, Ы(var2), var2.getId()));
            }
         }
      }
   }
}
