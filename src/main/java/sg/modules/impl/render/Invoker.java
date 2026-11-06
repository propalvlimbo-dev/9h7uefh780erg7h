package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.mixin.RenderTypeAccessor;
import sg.mixin.RenderPipelinesAccessor;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.settings.SingleChoiceSetting;

public class Invoker extends ClientModule {
   private static final Identifier field_3 = Identifier.fromNamespaceAndPath(Invoker.сЙЭ, Invoker.сЙ必);
   private static final BlendFunction е_at_5266f6df = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE);
   private static final int ег = 3;
   private static final int е0 = 10;
   private static final int ех = 16;
   private static final int еУ = 640;
   private static final float АO = 0.1F;
   private static final float А7 = 1.0F;
   private static final float АР = 380.0F;
   private static final float[] а = new float[]{0.0F, Invoker.сЙщ, Invoker.сЙГ};
   private static final float[] Ю = new float[]{1.0F, Invoker.сЙл, Invoker.сЙэ};
   private static final float[] Ч_06b4556e = new float[]{Invoker.сЙю, Invoker.сЙЩ, Invoker.сЙу};
   private static final float[] Л = new float[]{Invoker.сЙ2, 1.0F, 1.0F};
   private static final float А3 = 0.8627451F;
   private static final float[] 诶 = new float[640];
   private static final float[] field_1 = new float[640];
   private static final float[] Ь = new float[640];
   private static final float[] 衣 = new float[640];
   private static final RenderPipeline с必;
   public static final RenderType Я_at_5266f6df;
   private static final RenderPipeline сщ;
   public static final RenderType Х_at_5266f6df;
   private final BooleanSetting сф;
   private final BooleanSetting си;
   private final BooleanSetting с9;
   private final BooleanSetting со;
   private final SingleChoiceSetting м;
   @SerializedName("Вю")
   private final FloatSetting Вю_989fed9a;
   private final FloatSetting ВЩ;
   private final Map<UUID, ArrayDeque<Invoker.OrbTrailPoint>[]> сш;
   private final Set<UUID> Ы;
   private final Invoker.OrbRenderState[] В;
   private int еЗ;
   @SerializedName("Ч")
   private final int[] Ч_28f5b604;
   private final float[] ш;
   private final float[] И;
   private long В3;
   private boolean ЯИ;
   private int еЕ;
   private float Ар;
   private float А6;
   private final EventListener<WorldEffectsEvent> Вэ;
   @SerializedName("Вю")
   private final EventListener<InputEvent> Вю_f8db0d09;
   private static final float сФи = 0.1F;
   private static final float сФ9 = 0.1F;
   private static final float сФо = 0.1F;
   private static final double сФO = 0.56;
   private static final double сФ7 = 0.44;
   private static final double сФР = 0.65;
   private static final double сФ3 = 0.35;
   private static final String сФр = "Invoker";
   private static final String сФ6 = "Три сферы вокруг игрока";
   private static final String сФ_ = "Себя";
   private static final String сФШ = "Других";
   private static final String сФы = "От первого лица";
   private static final String сФй = "Каст";
   private static final String сФЖ = "След";
   private static final String сФм = "None";
   private static final String сФК = "Trail";
   private static final String сФI = "Fire";
   private static final String сФТ = "Длина следа";
   private static final float сФО = 40.0F;
   private static final float сФФ = 6.0F;
   private static final float сФЙ = 48.0F;
   private static final String сФк = "Сила следа";
   private static final float сФг = 0.7F;
   private static final float сФ0 = 0.3F;
   private static final float сФх = 0.1F;
   private static final long сФУ = -1L;
   private static final long сФЗ = -1L;
   private static final float сФЕ = 380.0F;
   private static final float сФП = 1.70158F;
   private static final String сФ西 = "None";
   private static final String сФд = "Fire";
   private static final long сФБ = 1000000L;
   private static final float сФС = 1000.0F;
   private static final float сФз = 0.12F;
   private static final float сФц = 0.88F;
   private static final float сФя = 2.1F;
   private static final double сЙс = 0.08F;
   private static final float сЙВ = 3.0F;
   private static final float сЙЯ = 1.2F;
   private static final float сЙХ = 0.1F;
   private static final float сЙе = 2.8F;
   private static final double сЙА = 0.085;
   private static final float сЙ4 = 0.45F;
   private static final float сЙп = 2.1F;
   private static final double сЙД = 0.08F;
   private static final float сЙЪ = 3.0F;
   private static final float сЙт = 1.2F;
   private static final float сЙа = 0.1F;
   private static final float сЙЮ = 2.8F;
   private static final double сЙЧ = 0.085;
   private static final float сЙЛ = 0.01F;
   private static final float сЙ诶 = 0.02F;
   private static final float сЙ1 = 0.8627451F;
   private static final float сЙЬ = 0.01F;
   private static final float сЙ衣 = 0.02F;
   private static final float сЙш = 0.6F;
   private static final float сЙИ = 0.534902F;
   private static final float сЙь = 0.09F;
   private static final float сЙж = 0.64F;
   private static final float сЙ弟 = 0.09F;
   private static final float сЙб = 0.55F;
   private static final float сЙЫ = 0.02F;
   private static final float сЙl = 0.5F;
   private static final float сЙ8 = 0.01F;
   private static final float сЙ5 = 0.5176471F;
   private static final float сЙв = 0.74509805F;
   private static final float сЙъ = 0.25490198F;
   private static final float сЙМ = 0.48000002F;
   private static final float сЙН = 0.55F;
   private static final float сЙн = 0.7F;
   private static final String сЙЦ = "None";
   private static final String сЙч = "None";
   private static final String сЙЭ = "rose";
   private static final String сЙ必 = "images/particles/bloom.png";
   private static final float сЙщ = (float) (Math.PI * 2.0 / 3.0);
   private static final float сЙГ = (float) (Math.PI * 4.0 / 3.0);
   private static final float сЙл = 0.2509804F;
   private static final float сЙэ = 0.54901963F;
   private static final float сЙю = 0.32156864F;
   private static final float сЙЩ = 0.8235294F;
   private static final float сЙу = 0.3529412F;
   private static final float сЙ2 = 0.14117648F;
   private static final double сЙф = 10.0;
   private static final double сЙи = Math.PI;
   private static final double сЙ9 = Math.PI / 2;
   private static final double сЙо = 10.0;
   private static final double сЙO = Math.PI;
   private static final double сЙ7 = Math.PI / 2;
   private static final double сЙР = 16.0;
   private static final double сЙ3 = Math.PI;
   private static final double сЙр = 2.0;
   private static final double сЙ6 = 16.0;
   private static final double сЙ_ = Math.PI;
   private static final double сЙШ = 2.0;
   private static final String сЙы = "rose/pipeline/invoker_sphere";
   private static final String сЙй = "target_esp_crystal";
   private static final String сЙЖ = "target_esp_crystal";
   private static final String сЙм = "rose_invoker_sphere";
   private static final String сЙК = "rose/pipeline/invoker_bloom";
   private static final String сЙI = "dead_chams";
   private static final String сЙТ = "dead_chams";
   private static final String сЙО = "Sampler0";
   private static final String сЙФ = "rose_invoker_bloom";
   private static final String сЙЙ = "Sampler0";

   private float УI() {
      return this.В3 < 0L ? 1.0F : Mth.clamp((float)(System.currentTimeMillis() - this.В3) / сФЕ, 0.0F, 1.0F);
   }

   private void У(VertexConsumer var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      if (!(var6 < сЙЫ)) {
         for (int var11 = 0; var11 < 640; var11++) {
            float var12 = 衣[var11];
            var1.addVertex(var2, var3 + 诶[var11] * var6, var4 + field_1[var11] * var6, var5 + Ь[var11] * var6)
               .setUv(0.0F, 0.0F)
               .setColor(var7 * var12, var8 * var12, var9 * var12, var10);
         }
      }
   }

   private void У(VertexConsumer var1, PoseStack var2, double var3, double var5, double var7, float var9, float var10, float var11, boolean var12) {
      for (int var13 = 0; var13 < this.еЗ; var13++) {
         Invoker.OrbRenderState var14 = this.В[var13];
         ArrayDeque[] var15 = this.сш.get(var14.Л);
         if (var15 != null && var14.пЧ >= 0) {
            ArrayDeque<Invoker.OrbTrailPoint> var16 = var15[var14.пЧ];
            if (!var16.isEmpty()) {
               int var17 = var16.size();
               int var18 = 0;
               float var19 = Ю[var14.пЛ];
               float var20 = Ч_06b4556e[var14.пЛ];
               float var21 = Л[var14.пЛ];

               for (Invoker.OrbTrailPoint var23 : var16) {
                  float var24 = (float)(var23.Яэ - var3);
                  float var25 = (float)(var23.Яю - var5);
                  float var26 = (float)(var23.ЯЩ - var7);
                  float var27 = method_8(var24, var25, var26);
                  if (var27 <= сЙ8) {
                     var18++;
                  } else {
                     float var28 = var17 <= 1 ? 0.0F : (float)var18 / (var17 - 1);
                     float var29 = (1.0F - var28) * (1.0F - var28);
                     float var30 = Mth.clamp(сЙ5 * var29 * var11 * var27, 0.0F, 1.0F);
                     float var31 = var19;
                     float var32 = var20;
                     float var33 = var21;
                     if (var12) {
                        var31 = Math.min(1.0F, Mth.lerp(var29, var31, 1.0F));
                        var32 = Math.min(1.0F, Mth.lerp(var29, var32, сЙв));
                        var33 = Math.min(1.0F, Mth.lerp(var29, var33, сЙъ));
                     }

                     this.У(var1, var2, var24, var25, var26, var9, var10, сЙМ * (сЙН + сЙн * var29), var31, var32, var33, var30);
                     var18++;
                  }
               }
            }
         }
      }
   }

   private void в(InputEvent var1) {
      if (this.со.getValue() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.screen() == null) {
         if (var1.getAction() == 1) {
            byte var2 = switch (var1.getInputCode()) {
               case 65 -> 1;
               case 68 -> 2;
               case 87 -> 0;
               default -> -1;
            };
            if (var2 >= 0) {
               this.诶(var2);
            }
         }
      }
   }

   private void У(UUID var1, int var2, double var3, double var5, double var7, int var9) {
      ArrayDeque[] var10 = this.сш.computeIfAbsent(var1, var0 -> У_698df620());
      ArrayDeque var11 = var10[var2];
      Invoker.OrbTrailPoint var12 = var11.size() >= var9 ? (Invoker.OrbTrailPoint)var11.removeLast() : null;
      if (var12 == null) {
         var12 = new Invoker.OrbTrailPoint();
      }

      var12.Яэ = var3;
      var12.Яю = var5;
      var12.ЯЩ = var7;
      var11.addFirst(var12);

      while (var11.size() > var9) {
         var11.removeLast();
      }
   }

   private static ArrayDeque<Invoker.OrbTrailPoint>[] У_698df620() {
      ArrayDeque[] var0 = new ArrayDeque[3];

      for (int var1 = 0; var1 < 3; var1++) {
         var0[var1] = new ArrayDeque();
      }

      return var0;
   }

   private void 诶(int var1) {
      float var2 = Н(this.УI());
      float[] var3 = new float[3];
      float[] var4 = new float[3];

      for (int var5 = 0; var5 < 3; var5++) {
         var3[var5] = Mth.lerp(var2, this.ш[var5], а[var5]);
         var4[var5] = Mth.lerp(var2, this.И[var5], 1.0F);
      }

      this.еЕ = this.Ч_28f5b604[0];
      this.Ар = var3[0];
      this.А6 = var4[0];
      this.ш[0] = var3[1];
      this.ш[1] = var3[2];
      this.ш[2] = а[2];
      this.И[0] = var4[1];
      this.И[1] = var4[2];
      this.И[2] = 0.0F;
      this.Ч_28f5b604[0] = this.Ч_28f5b604[1];
      this.Ч_28f5b604[1] = this.Ч_28f5b604[2];
      this.Ч_28f5b604[2] = var1;
      this.ЯИ = true;
      this.В3 = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      this.сш.clear();
      this.Ч_28f5b604[0] = 0;
      this.Ч_28f5b604[1] = 1;
      this.Ч_28f5b604[2] = 2;
      this.Лм();
      super.onDisable();
   }

   private void Я(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = var1.getCameraPosition();
         Camera var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         PoseStack var5 = var1.getPoses();
         Matrix4f var6 = var5.last().pose();
         boolean var7 = MINECRAFT_CLIENT.options.getCameraType().isFirstPerson();
         boolean var8 = !this.м.isSelected(сФ西);
         boolean var9 = this.м.isSelected(сФд);
         int var10 = this.Вю_989fed9a.getValue().intValue();
         float var11 = this.ВЩ.getValue();
         float var12 = (float)(System.currentTimeMillis() % сФБ) / сФС;
         double var13 = var3.x;
         double var15 = var3.y;
         double var17 = var3.z;
         float var19 = var4.yRot();
         float var20 = var4.xRot();
         this.Ы.clear();
         this.еЗ = 0;

         for (Player var22 : MINECRAFT_CLIENT.level.players()) {
            if (var22 != null && !var22.isSpectator()) {
               boolean var23 = var22 == MINECRAFT_CLIENT.player;
               if (var23 ? this.сф.getValue() && (!var7 || this.с9.getValue()) : this.си.getValue()) {
                  UUID var24 = var22.getUUID();
                  this.Ы.add(var24);
                  double var25 = Mth.lerp(var2, var22.xo, var22.getX());
                  double var27 = Mth.lerp(var2, var22.yo, var22.getY());
                  double var29 = Mth.lerp(var2, var22.zo, var22.getZ());
                  float var31 = Mth.lerp(var2, var22.yBodyRotO, var22.yBodyRot);
                  double var32 = Math.toRadians(var31);
                  double var34 = var27 + var22.getBbHeight();
                  float var36 = this.со.getValue() && var23 ? Н(this.УI()) : 1.0F;
                  float var37 = this.со.getValue() && var23 ? ХХ(this.УI()) : 1.0F;

                  for (int var38 = 0; var38 < 3 && this.еЗ < this.В.length; var38++) {
                     float var39 = а[var38];
                     float var40 = 1.0F;
                     float var41 = 1.0F;
                     if (this.со.getValue() && var23) {
                        var39 = Mth.lerp(var36, this.ш[var38], а[var38]);
                        var40 = Mth.lerp(var36, this.И[var38], 1.0F);
                        if (this.ЯИ && var38 == 2) {
                           var41 = сФз + сФц * var37;
                        }
                     }

                     float var42 = 1.0F * var40 * (1.0F + (float)(Math.sin(var12 * сФя + var39) * сЙс));
                     float var43 = var12 * 2.0F + var39 + (float)var32;
                     float var44 = (float)Math.sin(var43 * сЙВ + var12 * сЙЯ);
                     double var45 = var25 + Math.cos(var43) * var42;
                     double var47 = var34 + var44 * сЙХ * var40 + Math.sin(var12 * сЙе + var39) * сЙА * var40;
                     double var49 = var29 + Math.sin(var43) * var42;
                     if (var8 && var40 > сЙ4) {
                        this.У(var24, var38, var45, var47, var49, var10);
                     }

                     Invoker.OrbRenderState var51 = this.В[this.еЗ++];
                     var51.Л = var24;
                     var51.пЧ = var38;
                     var51.пЛ = this.со.getValue() && var23 ? this.Ч_28f5b604[var38] : var38;
                     var51.пБ = var41;
                     var51.Яь = var45;
                     var51.Яж = var47;
                     var51.Я弟 = var49;
                  }

                  if (this.со.getValue() && var23 && this.еЕ >= 0 && var36 < 1.0F && this.еЗ < this.В.length) {
                     float var64 = Mth.lerp(var36, this.А6, 0.0F);
                     float var65 = this.Ар;
                     float var66 = 1.0F * var64 * (1.0F + (float)(Math.sin(var12 * сЙп + var65) * сЙД));
                     float var67 = var12 * 2.0F + var65 + (float)var32;
                     float var68 = (float)Math.sin(var67 * сЙЪ + var12 * сЙт);
                     Invoker.OrbRenderState var69 = this.В[this.еЗ++];
                     var69.Л = var24;
                     var69.пЧ = -1;
                     var69.пЛ = this.еЕ;
                     var69.пБ = 1.0F - var36;
                     var69.Яь = var25 + Math.cos(var67) * var66;
                     var69.Яж = var34 + var68 * сЙа * var64 + Math.sin(var12 * сЙЮ + var65) * сЙЧ * var64;
                     var69.Я弟 = var29 + Math.sin(var67) * var66;
                  }
               }
            }
         }

         this.сш.keySet().removeIf(var1x -> !this.Ы.contains(var1x));
         if (this.УI() >= 1.0F) {
            this.ЯИ = false;
            this.еЕ = -1;
         }

         if (this.еЗ != 0) {
            VertexConsumer var52 = var1.getGeometryBuffer().getBuffer(Я_at_5266f6df);

            for (int var53 = 0; var53 < this.еЗ; var53++) {
               Invoker.OrbRenderState var55 = this.В[var53];
               float var57 = (float)(var55.Яь - var13);
               float var59 = (float)(var55.Яж - var15);
               float var26 = (float)(var55.Я弟 - var17);
               float var62 = method_8(var57, var59, var26);
               if (!(var62 <= сЙЛ) && !(var55.пБ < сЙ诶)) {
                  this.У(var52, var6, var57, var59, var26, var55.пБ, Ю[var55.пЛ], Ч_06b4556e[var55.пЛ], Л[var55.пЛ], сЙ1 * var62);
               }
            }

            VertexConsumer var54 = var1.getGeometryBuffer().getBuffer(Х_at_5266f6df);

            for (int var56 = 0; var56 < this.еЗ; var56++) {
               Invoker.OrbRenderState var58 = this.В[var56];
               float var60 = (float)(var58.Яь - var13);
               float var61 = (float)(var58.Яж - var15);
               float var63 = (float)(var58.Я弟 - var17);
               float var28 = method_8(var60, var61, var63);
               if (!(var28 <= сЙЬ) && !(var58.пБ < сЙ衣)) {
                  this.У(var54, var5, var60, var61, var63, var19, var20, сЙш * var58.пБ, Ю[var58.пЛ], Ч_06b4556e[var58.пЛ], Л[var58.пЛ], сЙИ * var28);
               }
            }

            if (var8) {
               this.У(var54, var5, var13, var15, var17, var19, var20, var11, var9);
            }
         }
      }
   }

   public Invoker() {
      super(сФр, сФ6, ModuleCategory.RENDER);
      this.сф = new BooleanSetting(сФ_, true);
      this.си = new BooleanSetting(сФШ, false);
      this.с9 = new BooleanSetting(сФы, false);
      this.со = new BooleanSetting(сФй, false);
      this.м = new SingleChoiceSetting(сФЖ, new ChoiceOption(сФм), new ChoiceOption(сФК), new ChoiceOption(сФI));
      this.Вю_989fed9a = new FloatSetting(сФТ, сФО, сФФ, сФЙ).withStep(1.0F).visibleWhen(() -> !this.м.isSelected(сЙч));
      this.ВЩ = new FloatSetting(сФк, сФг, сФ0, 2.0F).withStep(сФх).visibleWhen(() -> !this.м.isSelected(сЙЦ));
      this.сш = new HashMap<>();
      this.Ы = new HashSet<>();
      this.В = new Invoker.OrbRenderState[96];
      this.Ч_28f5b604 = new int[]{0, 1, 2};
      this.ш = new float[]{а[0], а[1], а[2]};
      this.И = new float[]{1.0F, 1.0F, 1.0F};
      this.В3 = сФУ;
      this.еЕ = -1;
      this.Вэ = this::Я;
      this.Вю_f8db0d09 = this::в;
      this.addSettings(this.сф, this.си, this.с9, this.со, this.м, this.Вю_989fed9a, this.ВЩ);

      for (int var1 = 0; var1 < this.В.length; var1++) {
         this.В[var1] = new Invoker.OrbRenderState();
      }
   }

   private void У(
      VertexConsumer var1,
      PoseStack var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12
   ) {
      float var13 = var8 * сЙl;
      var2.pushPose();
      var2.translate(var3, var4, var5);
      var2.mulPose(Axis.YP.rotationDegrees(-var6));
      var2.mulPose(Axis.XP.rotationDegrees(var7));
      Matrix4f var14 = var2.last().pose();
      var1.addVertex(var14, -var13, var13, 0.0F).setUv(0.0F, 1.0F).setColor(var9, var10, var11, var12);
      var1.addVertex(var14, var13, var13, 0.0F).setUv(1.0F, 1.0F).setColor(var9, var10, var11, var12);
      var1.addVertex(var14, var13, -var13, 0.0F).setUv(1.0F, 0.0F).setColor(var9, var10, var11, var12);
      var1.addVertex(var14, -var13, -var13, 0.0F).setUv(0.0F, 0.0F).setColor(var9, var10, var11, var12);
      var2.popPose();
   }

   private void Лм() {
      for (int var1 = 0; var1 < 3; var1++) {
         this.ш[var1] = а[var1];
         this.И[var1] = 1.0F;
      }

      this.В3 = сФЗ;
      this.ЯИ = false;
      this.еЕ = -1;
   }

   static {
      int var0 = 0;

      for (int var1 = 0; var1 < 10; var1++) {
         double var2 = var1 / сЙф * сЙи - сЙ9;
         double var4 = (var1 + 1) / сЙо * сЙO - сЙ7;

         for (int var6 = 0; var6 < 16; var6++) {
            double var7 = var6 / сЙР * сЙ3 * сЙр;
            double var9 = (var6 + 1) / сЙ6 * сЙ_ * сЙШ;
            var0 = У(var0, var2, var7);
            var0 = У(var0, var4, var7);
            var0 = У(var0, var4, var9);
            var0 = У(var0, var2, var9);
         }
      }

      с必 = RenderPipelinesAccessor.register(
         RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
            .withLocation(сЙы)
            .withVertexShader(ClientResourceIds.getCoreId(сЙй))
            .withFragmentShader(ClientResourceIds.getCoreId(сЙЖ))
            .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
            .withDepthWrite(true)
            .withCull(true)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
            .build()
      );
      Я_at_5266f6df = RenderTypeAccessor.create(сЙм, RenderSetup.builder(с必).createRenderSetup());
      сщ = RenderPipelinesAccessor.register(
         RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
            .withLocation(сЙК)
            .withVertexShader(ClientResourceIds.getCoreId(сЙI))
            .withFragmentShader(ClientResourceIds.getCoreId(сЙТ))
            .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
            .withDepthWrite(false)
            .withCull(false)
            .withBlend(е_at_5266f6df)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
            .withSampler(сЙО)
            .build()
      );
      Х_at_5266f6df = RenderTypeAccessor.create(сЙФ, RenderSetup.builder(сщ).withTexture(сЙЙ, field_3).createRenderSetup());
   }

   private static float method_8(float var0, float var1, float var2) {
      float var3 = var0 * var0 + var1 * var1 + var2 * var2;
      if (var3 < сЙь) {
         return 0.0F;
      } else {
         return var3 >= сЙж ? 1.0F : (var3 - сЙ弟) / сЙб;
      }
   }

   private static int У(int var0, double var1, double var3) {
      float var5 = (float)(Math.cos(var3) * Math.cos(var1));
      float var6 = (float)Math.sin(var1);
      float var7 = (float)(Math.sin(var3) * Math.cos(var1));
      诶[var0] = var5 * сФи;
      field_1[var0] = var6 * сФ9;
      Ь[var0] = var7 * сФо;
      衣[var0] = (float)Mth.clamp(сФO + сФ7 * Math.max(0.0, var6 * сФР + var7 * сФ3), 0.0, 1.0);
      return var0 + 1;
   }

   private static float Н(float var0) {
      var0 = Mth.clamp(var0, 0.0F, 1.0F);
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   private static float ХХ(float var0) {
      var0 = Mth.clamp(var0, 0.0F, 1.0F);
      float var1 = сФП;
      float var2 = var1 + 1.0F;
      float var3 = var0 - 1.0F;
      return 1.0F + var2 * var3 * var3 * var3 + var1 * var3 * var3;
   }

   private static final class OrbTrailPoint {
      double Яэ;
      double Яю;
      double ЯЩ;
   }

   private static final class OrbRenderState {
      UUID Л;
      int пЧ;
      int пЛ;
      float пБ;
      double Яь;
      double Яж;
      double Я弟;
   }
}
