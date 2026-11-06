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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.LivingEntityDamageEvent;
import sg.settings.SingleChoiceSetting;

public class PlayerAlert extends ClientModule {
   private final FloatSetting Вд;
   private final FloatSetting ВБ;
   private final FloatSetting ВС;
   private final FloatSetting Вз;
   private final FloatSetting Вц;
   private final FloatSetting Вя;
   private final BooleanSetting сй;
   private final BooleanSetting сЖ;
   private final BooleanSetting см;
   private final BooleanSetting сК;
   private final BooleanSetting сI;
   private final BooleanSetting сТ;
   private final BooleanSetting сО;
   private final SingleChoiceSetting Ф;
   private static final long ВЖ = 130L;
   private static final long Вм = 250L;
   private static final int ея = 24;
   private static final int Ас = 0;
   private static final int АВ = 1;
   private final List<PlayerAlert.PlayerState> сш;
   private final Map<UUID, Float> сб;
   private float Ам;
   private float АК;
   private float АI;
   private long ВК;
   private final RenderPipeline сю;
   private final EventListener<LivingEntityDamageEvent> ВO;
   private final EventListener<HudRenderEvent> В7;
   private static final String Зк = "PlayerAlert";
   private static final String Зг = "Дуга-индикатор урона/хила вокруг прицела";
   private static final String З0 = "Дистанция от прицела";
   private static final float Зх = 38.0F;
   private static final float ЗУ = 10.0F;
   private static final float ЗЗ = 150.0F;
   private static final String ЗЕ = "Ширина дуги";
   private static final float ЗП = 60.0F;
   private static final float З西 = 15.0F;
   private static final float Зд = 160.0F;
   private static final String ЗБ = "Толщина";
   private static final float ЗС = 5.0F;
   private static final float Зз = 25.0F;
   private static final float Зц = 0.5F;
   private static final String Зя = "Длительность (мс)";
   private static final float Ес = 1500.0F;
   private static final float ЕВ = 250.0F;
   private static final float ЕЯ = 5000.0F;
   private static final float ЕХ = 50.0F;
   private static final String Ее = "Радиус событий";
   private static final float ЕА = 30.0F;
   private static final float Е4 = 4.0F;
   private static final float Еп = 64.0F;
   private static final String ЕД = "Показывать от дистанции";
   private static final float ЕЪ = 80.0F;
   private static final String Ет = "Когда бьют тебя";
   private static final String Еа = "Когда бьют врага";
   private static final String ЕЮ = "Хил (зелёным)";
   private static final String ЕЧ = "Игнорировать друзей";
   private static final String ЕЛ = "Размер от урона";
   private static final String Е诶 = "Яркость от близости";
   private static final String Е1 = "Свечение";
   private static final String ЕЬ = "Цвет";
   private static final String Е衣 = "Урон/Хил";
   private static final String Еш = "Тема клиента";
   private static final String ЕИ = "rose/sixth_sense";
   private static final String Еь = "sixth_sense";
   private static final String Еж = "sixth_sense";
   private static final float Е弟 = 0.5F;
   private static final float Еб = -0.05F;
   private static final float ЕЫ = 0.05F;
   private static final float Еl = -0.05F;
   private static final float Е8 = 0.05F;
   private static final float Е5 = 0.5F;
   private static final double Ев = -1.0;
   private static final long Еъ = 130L;
   private static final long ЕМ = 250L;
   private static final float ЕН = 180.0F;
   private static final float Ен = 180.0F;
   private static final float ЕЦ = 270.0F;
   private static final float Еч = 60.0F;
   private static final float ЕЭ = 0.004F;
   private static final float Е必 = 0.92F;
   private static final float Ещ = 0.35F;
   private static final float ЕГ = 0.65F;
   private static final float Ел = 0.004F;
   private static final float Еэ = 0.75F;
   private static final float Ею = 0.6F;
   private static final float ЕЩ = 0.85F;
   private static final float Еу = 0.3F;
   private static final float Е2 = 0.9F;
   private static final float Еф = 1.9F;
   private static final float Еи = 1.12F;
   private static final float Е9 = 0.3F;
   private static final float Ео = 0.3F;
   private static final float ЕO = 0.7F;
   private static final float Е7 = 0.7F;
   private static final float ЕР = 0.7F;
   private static final float Е3 = 14.0F;
   private static final float Ер = 0.45F;
   private static final double Е6 = 48.0;
   private static final float Е_ = 0.85F;
   private static final float ЕШ = 0.25F;
   private static final float Еы = 0.75F;
   private static final String Ей = "Тема клиента";
   private static final float ЕЖ = 0.9F;
   private static final float Ем = 255.0F;
   private static final float ЕК = 255.0F;
   private static final float ЕI = 255.0F;
   private static final float ЕТ = 3.0F;
   private static final float ЕО = -1.0F;
   private static final String ЕФ = "sixthSense";
   private static final String ЕЙ = "sixthSenseVertices";

   private static float method_0(float var0, float var1) {
      return ЕО + var0 * 2.0F / var1;
   }

   private int х(
      BufferBuilder var1,
      PlayerAlert.PlayerState var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      Vec3 var8,
      float var9,
      long var10,
      float var12
   ) {
      float var13 = (float)(var10 - var2.field_9);
      float var14 = Mth.clamp(var13 / var12, 0.0F, 1.0F);
      float var15 = Mth.clamp(var13 / Еч, 0.0F, 1.0F);
      float var16 = (1.0F - var14) * (1.0F - var14);
      float var17 = var15 * var16;
      if (var17 <= ЕЭ) {
         return 0;
      }

      double var19 = var2.ж;
      float var18;
      if (var2.Я != null && var2.Я.isAlive() && !var2.Я.isRemoved()) {
         var18 = this.х(var2.Я, var7, var8, var9);
         var19 = MINECRAFT_CLIENT.player.distanceTo(var2.Я);
      } else if (var2.щ) {
         var18 = var2.сш;
      } else {
         var18 = var2.сш + (var2.сИ - var7);
      }

      float var21 = this.х(var2.с衣, var19);
      float var22 = Е必 * var17 * (this.сТ.getValue() ? Ещ + ЕГ * var21 : 1.0F);
      if (var22 <= Ел) {
         return 0;
      }

      float var23 = this.сI.getValue() ? Еэ + Ею * var21 : 1.0F;
      float var24 = this.Вд.getValue();
      float var25 = this.ВС.getValue() * var23;
      float var26 = this.ВБ.getValue() * (ЕЩ + Еу * var23);
      int var27 = this.х(var2.必, var21);
      int var28 = 0;
      if (this.сО.getValue()) {
         var28 += this.х(var1, var3, var4, var5, var6, var24 - var25 * Е2, var24 + var25 * Еф, var18, var26 * Еи, var27, var22 * Е9);
      }

      var28 += this.х(var1, var3, var4, var5, var6, var24, var24 + var25, var18, var26, var27, var22);
      return var28 + this.х(var1, var3, var4, var5, var6, var24 + var25 * Ео, var24 + var25 * ЕO, var18, var26 * Е7, ъ(var27, 60), var22 * ЕР);
   }

   private void Ц(long var1) {
      HashMap var3 = new HashMap();
      float var4 = this.Вц.getValue();

      for (Player var6 : MINECRAFT_CLIENT.level.players()) {
         if (var6 != MINECRAFT_CLIENT.player
            && !(MINECRAFT_CLIENT.player.distanceTo(var6) > var4)
            && (!this.сК.getValue() || !RoseClient.getInstance().getServices().getFriends().О(var6.getName().getString()))) {
            UUID var7 = var6.getUUID();
            float var8 = var6.getHealth() + var6.getAbsorptionAmount();
            var3.put(var7, var8);
            Float var9 = this.сб.get(var7);
            if (var9 != null) {
               float var10 = var8 - var9;
               if (var10 < Еl && this.сЖ.getValue()) {
                  this.х(false, -var10, var6, 1, false);
               } else if (var10 > Е8 && var9 > Е5 && this.см.getValue()) {
                  this.х(true, var10, var6, 1, false);
               }
            }
         }
      }

      this.сб.clear();
      this.сб.putAll(var3);
   }

   @Override
   public void onDisable() {
      this.вГ();
   }

   private void х(LivingEntityDamageEvent var1) {
      if (MINECRAFT_CLIENT.player != null && var1.getEntity() == MINECRAFT_CLIENT.player) {
         this.АК = var1.getHurtDirection();
         this.АI = MINECRAFT_CLIENT.player.getYRot();
         this.ВК = System.currentTimeMillis();
      }
   }

   private int х(boolean var1, float var2) {
      if (this.Ф.isSelected(Ей)) {
         return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      } else {
         return var1
            ? ColorMath.blendArgb(ColorMath.packRgb(18, 120, 40), ColorMath.packRgb(60, 235, 110), var2)
            : ColorMath.blendArgb(ColorMath.packRgb(160, 18, 14), ColorMath.packRgb(255, 60, 48), var2);
      }
   }

   private void вГ() {
      this.сш.clear();
      this.сб.clear();
      this.Ам = Float.NaN;
      this.ВК = 0L;
   }

   @Override
   public void onEnable() {
      this.вГ();
   }

   private static float г(float var0, float var1) {
      return 1.0F - var0 * 2.0F / var1;
   }

   private void method_4(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.gui.hud.isHidden()) {
         long var2 = System.currentTimeMillis();
         this.п(var2);
         this.Ц(var2);
         float var4 = this.Вз.getValue();
         this.сш.removeIf(var3 -> (float)(var2 - var3.field_9) >= var4);
         if (!this.сш.isEmpty()) {
            Camera var5 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
            if (var5 != null) {
               float var6 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
               float var7 = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth();
               float var8 = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
               float var9 = var7 / 2.0F;
               float var10 = var8 / 2.0F;
               float var11 = var5.yRot();
               Vec3 var12 = var5.position();
               BufferBuilder var13 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
               int var14 = 0;

               for (PlayerAlert.PlayerState var16 : this.сш) {
                  var14 += this.х(var13, var16, var9, var10, var7, var8, var11, var12, var6, var2, var4);
               }

               this.ъ(var13, var14);
            }
         }
      }
   }

   public PlayerAlert() {
      super(Зк, Зг, ModuleCategory.RENDER);
      this.Вд = new FloatSetting(З0, Зх, ЗУ, ЗЗ).withStep(1.0F);
      this.ВБ = new FloatSetting(ЗЕ, ЗП, З西, Зд).withStep(1.0F);
      this.ВС = new FloatSetting(ЗБ, ЗС, 1.0F, Зз).withStep(Зц);
      this.Вз = new FloatSetting(Зя, Ес, ЕВ, ЕЯ).withStep(ЕХ);
      this.Вц = new FloatSetting(Ее, ЕА, Е4, Еп).withStep(1.0F);
      this.Вя = new FloatSetting(ЕД, 0.0F, 0.0F, ЕЪ).withStep(1.0F);
      this.сй = new BooleanSetting(Ет, true);
      this.сЖ = new BooleanSetting(Еа, true);
      this.см = new BooleanSetting(ЕЮ, true);
      this.сК = new BooleanSetting(ЕЧ, true);
      this.сI = new BooleanSetting(ЕЛ, true);
      this.сТ = new BooleanSetting(Е诶, true);
      this.сО = new BooleanSetting(Е1, true);
      this.Ф = new SingleChoiceSetting(ЕЬ, new ChoiceOption(Е衣), new ChoiceOption(Еш));
      this.сш = new ArrayList<>();
      this.сб = new HashMap<>();
      this.Ам = Float.NaN;
      this.АК = 0.0F;
      this.АI = 0.0F;
      this.ВК = 0L;
      this.сю = RenderPipelineBuilder.builder()
         .withLocation(ЕИ)
         .withVertexShader(ClientResourceIds.getCoreId(Еь))
         .withFragmentShader(ClientResourceIds.getCoreId(Еж))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.TRIANGLES)
         .build();
      this.ВO = this::х;
      this.В7 = this::method_4;
      this.addSettings(this.Вд, this.ВБ, this.ВС, this.Вз, this.Вц, this.Вя, this.сй, this.сЖ, this.см, this.сК, this.сI, this.сТ, this.сО, this.Ф);
   }

   private void ъ(BufferBuilder var1, int var2) {
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
               var4 = RenderSystem.getDevice().createBuffer(() -> ЕЙ, 32, var3.vertexBuffer());
               RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();

               try (RenderPass var12 = var11.createRenderPass(() -> ЕФ, var10.getColorTextureView(), Optional.empty())) {
                  RenderSystem.bindDefaultUniforms(var12);
                  var12.setPipeline(this.сю);
                  var12.setVertexBuffer(0, var4.slice());
                  var12.setIndexBuffer(var8, var9);
                  var12.drawIndexed(var6, 1, 0, 0, 0);
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

   private static int ъ(int var0, int var1) {
      return ColorMath.packRgb(
         Math.min(255, ColorMath.getRed(var0) + var1), Math.min(255, ColorMath.getGreen(var0) + var1), Math.min(255, ColorMath.getBlue(var0) + var1)
      );
   }

   private void х(boolean var1, float var2, LivingEntity var3, int var4, boolean var5) {
      long var6 = System.currentTimeMillis();
      double var8 = var3 != null ? MINECRAFT_CLIENT.player.distanceTo(var3) : Ев;
      float var10 = this.Вя.getValue();
      if (!var5 || !(var10 > 0.0F) || !(var8 >= 0.0) || !(var8 < var10)) {
         if (!this.сш.isEmpty()) {
            PlayerAlert.PlayerState var11 = this.сш.get(this.сш.size() - 1);
            if (var6 - var11.field_9 <= Еъ && var11.必 == var1 && var11.Я == var3) {
               var11.с衣 += var2;
               if (var3 != null) {
                  var11.ж = var8;
               }

               return;
            }
         }

         PlayerAlert.PlayerState var12 = new PlayerAlert.PlayerState(var6, var1, var2, var8);
         var12.Я = var3;
         if (var3 == null) {
            if (var4 == 0 && var6 - this.ВК <= ЕМ) {
               var12.сш = this.АК + ЕН;
               var12.сИ = this.АI;
            } else if (var4 == 0) {
               var12.сш = MINECRAFT_CLIENT.player.getHurtDir() + Ен;
               var12.сИ = MINECRAFT_CLIENT.player.getYRot();
            } else {
               var12.сш = ЕЦ;
               var12.щ = true;
            }
         }

         this.сш.add(var12);

         while (this.сш.size() > 24) {
            this.сш.remove(0);
         }
      }
   }

   private float х(float var1, double var2) {
      float var4 = Mth.clamp(var1 / Е3, 0.0F, 1.0F);
      float var5 = var2 < 0.0 ? Ер : Mth.clamp(1.0F - (float)(var2 / Е6), 0.0F, 1.0F);
      float var6 = Math.max(var4, var5 * Е_);
      return Mth.clamp(ЕШ + Еы * var6, 0.0F, 1.0F);
   }

   private float х(LivingEntity var1, float var2, Vec3 var3, float var4) {
      double var5 = Mth.lerp(var4, var1.xo, var1.getX()) - var3.x;
      double var7 = Mth.lerp(var4, var1.zo, var1.getZ()) - var3.z;
      double var9 = Math.toRadians(var2);
      double var11 = Math.sin(var9);
      double var13 = Math.cos(var9);
      double var15 = var5 * -var11 + var7 * var13;
      double var17 = var5 * -var13 + var7 * -var11;
      return (float)Math.toDegrees(Math.atan2(-var15, var17));
   }

   private static float Иъ(float var0) {
      float var1 = 1.0F - Math.abs(var0 * 2.0F - 1.0F);
      return var1 * var1 * (ЕТ - 2.0F * var1);
   }

   private int х(BufferBuilder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, float var11) {
      if (var11 <= 0.0F) {
         return 0;
      }

      int var12 = Math.max(20, (int)(var9 * ЕЖ));
      float var13 = ColorMath.getRed(var10) / Ем;
      float var14 = ColorMath.getGreen(var10) / ЕК;
      float var15 = ColorMath.getBlue(var10) / ЕI;
      float var16 = var8 - var9 / 2.0F;
      int var17 = 0;

      for (int var18 = 0; var18 < var12; var18++) {
         float var19 = (float)var18 / var12;
         float var20 = (float)(var18 + 1) / var12;
         float var21 = (float)Math.toRadians(var16 + var19 * var9);
         float var22 = (float)Math.toRadians(var16 + var20 * var9);
         float var23 = Mth.cos(var21);
         float var24 = Mth.sin(var21);
         float var25 = Mth.cos(var22);
         float var26 = Mth.sin(var22);
         float var27 = Иъ(var19);
         float var28 = Иъ(var20);
         float var29 = var11 * var27;
         float var30 = var11 * var28;
         float var31 = method_0(var2 + var23 * var7, var4);
         float var32 = г(var3 + var24 * var7, var5);
         float var33 = method_0(var2 + var23 * var6, var4);
         float var34 = г(var3 + var24 * var6, var5);
         float var35 = method_0(var2 + var25 * var7, var4);
         float var36 = г(var3 + var26 * var7, var5);
         float var37 = method_0(var2 + var25 * var6, var4);
         float var38 = г(var3 + var26 * var6, var5);
         var1.addVertex(var31, var32, 0.0F).setColor(var13, var14, var15, var29);
         var1.addVertex(var33, var34, 0.0F).setColor(var13, var14, var15, var29);
         var1.addVertex(var35, var36, 0.0F).setColor(var13, var14, var15, var30);
         var1.addVertex(var33, var34, 0.0F).setColor(var13, var14, var15, var29);
         var1.addVertex(var37, var38, 0.0F).setColor(var13, var14, var15, var30);
         var1.addVertex(var35, var36, 0.0F).setColor(var13, var14, var15, var30);
         var17 += 6;
      }

      return var17;
   }

   private LivingEntity П_95bedd6e() {
      Player var1 = null;
      double var2 = this.Вц.getValue().floatValue();

      for (Player var5 : MINECRAFT_CLIENT.level.players()) {
         if (var5 != MINECRAFT_CLIENT.player && var5.isAlive() && (!this.сК.getValue() || !RoseClient.getInstance().getServices().getFriends().О(var5.getName().getString()))) {
            double var6 = MINECRAFT_CLIENT.player.distanceTo(var5);
            if (var6 < var2) {
               var2 = var6;
               var1 = var5;
            }
         }
      }

      return var1;
   }

   private void п(long var1) {
      float var3 = MINECRAFT_CLIENT.player.getHealth() + MINECRAFT_CLIENT.player.getAbsorptionAmount();
      if (!Float.isNaN(this.Ам) && MINECRAFT_CLIENT.player.isAlive() && this.Ам > Е弟) {
         float var4 = var3 - this.Ам;
         if (var4 < Еб && this.сй.getValue()) {
            this.х(false, -var4, this.П_95bedd6e(), 0, true);
         } else if (var4 > ЕЫ && this.см.getValue()) {
            this.х(true, var4, this.П_95bedd6e(), 1, false);
         }
      }

      this.Ам = var3;
   }

   private static final class PlayerState {
      @SerializedName("9")
      long field_9;
      boolean 必;
      float с衣;
      double ж;
      LivingEntity Я;
      float сш;
      float сИ;
      boolean щ;

      PlayerState(long var1, boolean var3, float var4, double var5) {
         this.field_9 = var1;
         this.必 = var3;
         this.с衣 = var4;
         this.ж = var5;
      }
   }
}
