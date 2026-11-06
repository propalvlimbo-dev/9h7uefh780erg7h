package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sg.gui.FireScreenEffect;
import sg.mixinbridge.HmiClientInitializer;

public class ScriptedParticle {
   private int сЭ = 0;
   @SerializedName("4")
   private Consumer<ScriptedParticle> field_4 = null;
   @SerializedName("ы")
   public String ы_388008bb;
   @SerializedName("й")
   public String й_388008bb;
   @SerializedName("К")
   public boolean К_bbeebd87 = false;
   @SerializedName("I")
   private boolean I_bbeebd87 = true;
   @SerializedName("Ж")
   private final String Ж_388008bb;
   private double Ш = 0.0;
   @SerializedName("ы")
   private double ы_3f39d5c3 = 0.0;
   @SerializedName("й")
   private double й_3f39d5c3 = 0.0;
   @SerializedName("Ж")
   public double Ж_3f39d5c3;
   public double м;
   @SerializedName("К")
   public double К_3f39d5c3;
   @SerializedName("I")
   public double I_3f39d5c3;
   @SerializedName("Т")
   public double Т_3f39d5c3;
   public double О;
   private double Ф;
   private double Й;
   public double к;
   public double г;
   @SerializedName("0")
   public double field_0;
   public double х;
   public double У;
   public double З;
   public double Е;
   @SerializedName("Т")
   private boolean Т_bbeebd87;
   @SerializedName("В")
   public InteractionHand В_8332a9ec;
   private double П;
   private Identifier Ч;
   private List<Identifier> 弟;
   @SerializedName("В")
   public PoseStack В_e9a76781;
   @SerializedName("В")
   Iterator<Identifier> В_4a4ebe7a;
   private static final double ВдК = 255.0;
   private static final float ВдI = 30.0F;
   private static final float ВдТ = 0.1F;
   private static final double ВдО = 9999.0;
   private static final float ВдФ = 0.1F;
   private static final double ВдЙ = 0.9;
   private static final double Вдк = 0.01;
   private static final String Вдг = "SCALE";
   private static final double Вд0 = 0.03;
   private static final double Вдх = 0.07;
   private static final String ВдУ = "OPACITY";
   private static final float ВдЗ = 10.0F;
   private static final double ВдЕ = 255.0;
   private static final String ВдП = "SPAWN";
   private static final String Вд西 = "KEYFRAME";
   private static final String Вдд = "ADDITIVE";
   private static final String ВдБ = "CUTOUT_L";
   private static final double ВдС = 0.25;

   public ScriptedParticle(
      boolean var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24,
      double var26,
      Identifier var28,
      String var29,
      InteractionHand var30,
      String var31,
      String var32,
      double var33,
      double var35,
      Consumer<ScriptedParticle> var37,
      PoseStack var38
   ) {
      this(
         var1,
         var2,
         var4,
         var6,
         var8,
         var10,
         var12,
         var14,
         var16,
         var18,
         var20,
         var22,
         var24,
         var26,
         var28,
         new ArrayList<>(),
         var29,
         var30,
         var31,
         var32,
         var33,
         var35,
         var37,
         var38
      );
   }

   private ScriptedParticle(
      boolean var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24,
      double var26,
      Identifier var28,
      List<Identifier> var29,
      String var30,
      InteractionHand var31,
      String var32,
      String var33,
      double var34,
      double var36,
      Consumer<ScriptedParticle> var38,
      PoseStack var39
   ) {
      this.Ф = ВдК;
      this.Й = 0.0;
      this.к = 1.0;
      this.П = 0.0;
      this.弟 = new ArrayList<>();
      this.В_e9a76781 = null;
      this.й_3f39d5c3 = var34;
      this.Т_bbeebd87 = var1;
      this.ы_388008bb = var32;
      this.Ж_388008bb = var33;
      this.й_388008bb = var30;
      this.В_8332a9ec = var31;
      this.field_4 = var38;
      this.Ж_3f39d5c3 = var2;
      this.м = var4;
      this.К_3f39d5c3 = var6;
      this.Ф = var36;
      this.I_3f39d5c3 = var8;
      this.Т_3f39d5c3 = var10;
      this.О = var12;
      this.г = var14;
      this.field_0 = var16;
      this.х = var18;
      this.У = var20;
      this.З = var22;
      this.Е = var24;
      this.к = var26;
      this.Ч = var28;
      this.弟 = var29;
      this.В_4a4ebe7a = var29.iterator();
      this.В_e9a76781 = var39;
   }

   public ScriptedParticle(
      boolean var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24,
      double var26,
      Identifier var28,
      String var29,
      InteractionHand var30,
      String var31,
      String var32,
      double var33,
      double var35
   ) {
      this(
         var1,
         var2,
         var4,
         var6,
         var8,
         var10,
         var12,
         var14,
         var16,
         var18,
         var20,
         var22,
         var24,
         var26,
         var28,
         new ArrayList<>(),
         var29,
         var30,
         var31,
         var32,
         var33,
         var35,
         null,
         null
      );
   }

   public ScriptedParticle(
      boolean var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24,
      double var26,
      Identifier var28,
      String var29,
      InteractionHand var30,
      String var31,
      String var32,
      double var33,
      double var35,
      Consumer<ScriptedParticle> var37
   ) {
      this(
         var1,
         var2,
         var4,
         var6,
         var8,
         var10,
         var12,
         var14,
         var16,
         var18,
         var20,
         var22,
         var24,
         var26,
         var28,
         new ArrayList<>(),
         var29,
         var30,
         var31,
         var32,
         var33,
         var35,
         var37,
         null
      );
   }

   public ScriptedParticle(
      boolean var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24,
      double var26,
      Identifier var28,
      List<Identifier> var29,
      String var30,
      InteractionHand var31,
      String var32,
      String var33,
      double var34,
      double var36,
      Consumer<ScriptedParticle> var38
   ) {
      this(
         var1,
         var2,
         var4,
         var6,
         var8,
         var10,
         var12,
         var14,
         var16,
         var18,
         var20,
         var22,
         var24,
         var26,
         var28,
         var29,
         var30,
         var31,
         var32,
         var33,
         var34,
         var36,
         var38,
         null
      );
   }

   public void method_8(PoseStack var1, SubmitNodeCollector var2, int var3, AbstractClientPlayer var4, float var5) {
      if (!this.К_bbeebd87) {
         var1.pushPose();
         var1.translate(this.Ж_3f39d5c3, this.м, this.К_3f39d5c3);
         var1.scale((float)this.Й, (float)this.Й, (float)this.Й);
         RenderType var6 = Вдд.equals(this.Ж_388008bb) ? FireScreenEffect.method_5(this.Ч) : RenderTypes.breezeWind(this.Ч, 0.0F, 0.0F);
         Matrix4f var7 = var1.last().pose();
         Vector3f var8 = new Vector3f();
         Vector3f var9 = new Vector3f();
         Quaternionf var10 = new Quaternionf();
         var7.getTranslation(var8);
         var7.getUnnormalizedRotation(var10);
         var7.getScale(var9);
         var7.identity().translate(var8).scale(var9).rotate(Minecraft.getInstance().gameRenderer.mainCamera().rotation());
         var7.rotate(new Quaternionf().rotateX((float)Math.toRadians(this.г)));
         var7.rotate(new Quaternionf().rotateY((float)Math.toRadians(this.field_0)));
         var7.rotate(new Quaternionf().rotateZ((float)Math.toRadians(this.х)));
         Matrix3f var11 = var1.last().normal();
         if (Objects.equals(this.Ж_388008bb, ВдБ)) {
            var3 = LightCoordsUtil.pack(15, 15);
         }

         double var12 = ВдС;
         Vector3f var14 = new Vector3f(0.0F, 0.0F, 1.0F);
         var14.mul(var11);
         int var15 = var3;
         var2.submitCustomGeometry(
            var1,
            var6,
            (var5x, var6x) -> {
               int var7x = Math.clamp((int)this.Ф, 0, 255);
               var6x.addVertex(var5x, (float)var12, (float)var12, 0.0F)
                  .setColor(255, 255, 255, var7x)
                  .setUv(1.0F, 0.0F)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(var15)
                  .setNormal(var14.x, var14.y, var14.z);
               var6x.addVertex(var5x, (float)(-var12), (float)var12, 0.0F)
                  .setColor(255, 255, 255, var7x)
                  .setUv(0.0F, 0.0F)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(var15)
                  .setNormal(var14.x, var14.y, var14.z);
               var6x.addVertex(var5x, (float)(-var12), (float)(-var12), 0.0F)
                  .setColor(255, 255, 255, var7x)
                  .setUv(0.0F, 1.0F)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(var15)
                  .setNormal(var14.x, var14.y, var14.z);
               var6x.addVertex(var5x, (float)var12, (float)(-var12), 0.0F)
                  .setColor(255, 255, 255, var7x)
                  .setUv(1.0F, 1.0F)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(var15)
                  .setNormal(var14.x, var14.y, var14.z);
            }
         );
         var1.popPose();
      }
   }

   public void method_8А() {
      if (!this.К_bbeebd87) {
         float var1 = HmiClientInitializer.Юр * ВдI;
         this.й_3f39d5c3 = this.й_3f39d5c3 - ВдТ * var1;
         this.й_3f39d5c3 = Math.clamp(this.й_3f39d5c3, 0.0, ВдО);
         this.П = this.П + ВдФ * var1;
         this.Ж_3f39d5c3 = this.Ж_3f39d5c3 + this.I_3f39d5c3 * var1;
         this.м = this.м + this.Т_3f39d5c3 * var1;
         this.К_3f39d5c3 = this.К_3f39d5c3 + this.О * var1;
         double var2 = Math.pow(ВдЙ, var1);
         this.I_3f39d5c3 *= var2;
         this.Т_3f39d5c3 *= var2;
         this.О *= var2;
         LocalPlayer var4 = Minecraft.getInstance().player;
         double var5 = var4.getYRot();
         double var7 = Math.toRadians(var5);
         double var9 = -Math.sin(var7);
         double var11 = Math.cos(var7);
         Vec3 var13 = var4.getDeltaMovement();
         double var14 = var13.x * var9 + var13.z * var11;
         double var16 = var4.getDeltaMovement().normalize().x * var11 - var13.z * var9;
         if (this.Т_bbeebd87) {
            this.Т_3f39d5c3 = this.Т_3f39d5c3 - Вдк * var1;
         }

         this.г = this.г + this.У * var1;
         this.field_0 = this.field_0 + this.З * var1;
         this.х = this.х + this.Е * var1;
         if (!this.弟.isEmpty() && this.В_4a4ebe7a.hasNext() && Minecraft.getInstance().player.tickCount % 4 == 0 && var4.tickCount != this.сЭ) {
            this.Ч = this.В_4a4ebe7a.next();
         }

         if (this.ы_388008bb == Вдг) {
            if (!this.I_bbeebd87 && this.й_3f39d5c3 == 0.0) {
               this.Й = this.Й - Вд0 * var1;
               if (this.Й <= 0.0) {
                  this.К_bbeebd87 = true;
               }
            } else {
               this.Й = this.Й + Вдх * var1;
               if (this.Й >= this.к) {
                  this.Й = this.к;
                  this.I_bbeebd87 = false;
               }
            }
         } else if (this.ы_388008bb == ВдУ) {
            if (!this.I_bbeebd87 && this.й_3f39d5c3 == 0.0) {
               this.Ф = this.Ф - ВдЗ * var1;
               if (this.Ф <= 0.0) {
                  this.К_bbeebd87 = true;
                  this.Ф = 0.0;
               }
            } else {
               this.Й = this.к;
               this.I_bbeebd87 = false;
               this.Ф = ВдЕ;
            }
         } else if (this.ы_388008bb == ВдП) {
            if (!this.I_bbeebd87 && this.й_3f39d5c3 == 0.0) {
               this.К_bbeebd87 = true;
            } else {
               this.Й = this.к;
               this.I_bbeebd87 = false;
            }
         } else if (this.ы_388008bb == Вд西) {
            if (!this.I_bbeebd87 && !this.В_4a4ebe7a.hasNext()) {
               this.К_bbeebd87 = true;
            } else {
               this.Й = this.к;
               this.I_bbeebd87 = false;
            }
         }

         if (this.field_4 != null) {
            this.field_4.accept(this);
         }

         this.сЭ = var4.tickCount;
      }
   }
}
