package sg.hud;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.rotation.AimGeometryMath;
import sg.ec.ClientTextureLookup;
import sg.ec.EntityHealth;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.gui.hud.HudSettingsPanel;
import sg.hud.InteractiveHudElement;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.network.NameProtectionTextRewriter;
import sg.render.RoseBatchedItemAtlas;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;

public class TargetHud extends HudElement implements InteractiveHudElement {
   private final CatlavanTargetHudRenderer catlavan = new CatlavanTargetHudRenderer();
   private static final float Ъв = 200.0F;
   private static final float Ъъ = 76.0F;
   private static final float ЪМ = 220.0F;
   private static final float ЪН = 63.0F;
   private static final float Ън = 173.0F;
   private static final float ЪЦ = 13.0F;
   private static final float Ъч = 38.0F;
   private static final float ЪЭ = 2.8F;
   private static final float Ъ必 = 74.0F;
   private static final float Ъщ = 10.0F;
   private static final int ДЬ = 16;
   private static final int Д衣 = ColorMath.packRgb(255, 243, 0);
   private static final Identifier х = ClientResourceIds.getImageId(TargetHud.Х弟Л);
   private static final Identifier У = ClientResourceIds.getImageId(TargetHud.Х弟诶);
   private final FloatAnimation сщ;
   private final FloatAnimation сГ;
   private final FloatAnimation сл;
   private boolean еъ;
   private final SingleChoiceSetting ст;
   private final BooleanSetting Вк;
   private final BooleanSetting Вг;
   private final FloatSetting Х8;
   private final SingleChoiceSetting са;
   private final BooleanSetting В0;
   private final HudSettingsPanel е;
   private final FloatAnimation сэ;
   private final FloatAnimation сю;
   private final FloatAnimation сЩ;
   private final FloatAnimation су;
   @SerializedName("В")
   private final AnimatedIntegerText В_587528a4;
   private final FloatAnimation с2;
   private final FloatAnimation сф;
   @SerializedName("В")
   private final DecimalFormat В_f15b0243;
   private final CopyOnWriteArrayList<TargetHud.EffectParticle> п;
   private Entity Ъ;
   private final RoseBatchedItemAtlas.ItemHandle[] Я;
   private ItemStack ь;
   private float ЪГ;
   private float Ъл;
   private float Ъэ;
   private float Ъю;
   private float ЪЩ;
   private float Ъу;
   private float Ъ2;
   private float Ъф;
   private float Ъи;
   private float Ъ9;
   private long Хэ;
   private final EventListener<HudRenderEvent> ХХ;
   private static final String Хш7 = "TargetHud";
   private static final float ХшР = 10.0F;
   private static final float Хш3 = 10.0F;
   private static final float Хшр = 100.0F;
   private static final float Хш6 = 100.0F;
   private static final float Хш_ = 10.0F;
   private static final float ХшШ = 5.5F;
   private static final float Хшы = 5.0F;
   private static final float Хшй = 3.0F;
   private static final float ХшЖ = 6.0F;
   private static final float Хшм = 2.6F;
   private static final float ХшК = 4.5F;
   private static final float ХшI = 8.0F;
   private static final float ХшТ = 3.0F;
   private static final String ХшО = "0.0";
   private static final float ХшФ = -1.0F;
   private static final float ХшЙ = -1.0F;
   private static final float Хшк = -1.0F;
   private static final float Хшг = -1.0F;
   private static final float Хш0 = -1.0F;
   private static final float Хшх = -1.0F;
   private static final float ХшУ = -1.0F;
   private static final String ХшЗ = "TargetHud";
   private static final String ХшЕ = "Заменить Скин";
   private static final String ХшП = "От полоски";
   private static final String Хш西 = "Информация о цели";
   private static final float Хшд = 0.05F;
   private static final float ХшБ = 1.0E9F;
   private static final float ХшС = -1.0F;
   private static final float Хшз = -1.0F;
   private static final float Хшц = -1.0F;
   private static final float Хшя = -1.0F;
   private static final float ХИс = -1.0F;
   private static final float ХИВ = -1.0F;
   private static final String ХИЯ = "2";
   private static final float ХИХ = 0.001F;
   private static final float ХИе = 0.01F;
   private static final String ХИА = "2";
   private static final float ХИ4 = 220.0F;
   private static final float ХИп = 200.0F;
   private static final float ХИД = 16.0F;
   private static final float ХИЪ = 200.0F;
   private static final float ХИт = 74.0F;
   private static final float ХИа = 10.0F;
   private static final String ХИЮ = "2";
   private static final float ХИЧ = 16.0F;
   private static final float ХИЛ = 74.0F;
   private static final float ХИ诶 = 10.0F;
   private static final float ХИ1 = 255.0F;
   private static final float ХИЬ = 76.0F;
   private static final float ХИ衣 = 76.0F;
   private static final float ХИш = 10.0F;
   private static final float ХИИ = 255.0F;
   private static final float ХИь = 0.3F;
   private static final float ХИж = 74.0F;
   private static final float ХИ弟 = 54.0F;
   private static final float ХИб = 8.0F;
   private static final float ХИЫ = 3.0F;
   private static final float ХИl = 74.0F;
   private static final float ХИ8 = 54.0F;
   private static final float ХИ5 = 8.0F;
   private static final float ХИв = 3.0F;
   private static final float ХИъ = 74.0F;
   private static final float ХИМ = 54.0F;
   private static final float ХИН = 8.0F;
   private static final float ХИн = 3.0F;
   private static final float ХИЦ = 76.0F;
   private static final String ХИч = "2";
   private static final float ХИЭ = 63.0F;
   private static final float ХИ必 = 76.0F;
   private static final float ХИщ = 0.96F;
   private static final float ХИГ = 0.04F;
   private static final float ХИл = 192.0F;
   private static final float ХИэ = 32.0F;
   private static final float ХИю = 17.25F;
   private static final float ХИЩ = 220.0F;
   private static final float ХИу = 63.0F;
   private static final float ХИ2 = 31.5F;
   private static final float ХИф = 31.5F;
   private static final float ХИи = 255.0F;
   private static final float ХИ9 = 220.0F;
   private static final float ХИо = 63.0F;
   private static final float ХИO = 220.0F;
   private static final float ХИ7 = 63.0F;
   private static final float ХИР = 14.0F;
   private static final float ХИ3 = 60.0F;
   private static final float ХИр = 14.0F;
   private static final float ХИ6 = 105.0F;
   private static final float ХИ_ = 14.0F;
   private static final float ХИШ = 33.0F;
   private static final float ХИы = 18.0F;
   private static final float ХИй = 18.0F;
   private static final float ХИЖ = 255.0F;
   private static final float ХИм = 0.25F;
   private static final float ХИК = 2.8F;
   private static final float ХИI = 0.55F;
   private static final float ХИТ = 173.0F;
   private static final float ХИО = 13.0F;
   private static final float ХИФ = 38.0F;
   private static final float ХИЙ = 2.8F;
   private static final float ХИк = 0.001F;
   private static final float ХИг = 173.0F;
   private static final float ХИ0 = 13.0F;
   private static final float ХИх = 38.0F;
   private static final float ХИУ = 2.8F;
   private static final float ХИЗ = 0.38F;
   private static final float ХИЕ = 0.001F;
   private static final float ХИП = 0.01F;
   private static final float ХИ西 = 171.5F;
   private static final float ХИд = 11.5F;
   private static final float ХИБ = 41.0F;
   private static final float ХИС = 1.8F;
   private static final float ХИз = 0.22F;
   private static final float ХИц = 173.0F;
   private static final float ХИя = 13.0F;
   private static final float Хьс = 38.0F;
   private static final float ХьВ = 0.001F;
   private static final float ХьЯ = 173.0F;
   private static final float ХьХ = 13.0F;
   private static final float Хье = 38.0F;
   private static final float ХьА = 0.32F;
   private static final float Хь4 = 191.5F;
   private static final float Хьп = 24.5F;
   private static final float ХьД = 220.0F;
   private static final float ХьЪ = 63.0F;
   private static final float Хьт = 13.0F;
   private static final float Хьа = 13.0F;
   private static final float ХьЮ = 4.0F;
   private static final float ХьЧ = 15.0F;
   private static final float ХьЛ = 10.0F;
   private static final float Хь诶 = 10.0F;
   private static final float Хь1 = 43.0F;
   private static final float ХьЬ = 43.0F;
   private static final float Хь衣 = 11.0F;
   private static final float Хьш = 10.0F;
   private static final float ХьИ = 10.0F;
   private static final float Хьь = 43.0F;
   private static final float Хьж = 43.0F;
   private static final float Хь弟 = 11.0F;
   private static final float Хьб = 0.125F;
   private static final float ХьЫ = 0.01F;
   private static final float Хьl = 5.5F;
   private static final float Хь8 = 10.0F;
   private static final float Хь5 = 0.14F;
   private static final float Хьв = 0.12F;
   private static final float Хьъ = 0.88F;
   private static final float ХьМ = 8.0F;
   private static final float ХьН = 8.0F;
   private static final float Хьн = 60.0F;
   private static final float ХьЦ = 60.0F;
   private static final float Хьч = 8.0F;
   private static final float ХьЭ = 8.0F;
   private static final float Хь必 = 8.0F;
   private static final float Хьщ = 8.0F;
   private static final float ХьГ = 8.0F;
   private static final float Хьл = 8.0F;
   private static final float Хьэ = 60.0F;
   private static final float Хью = 60.0F;
   private static final float ХьЩ = 8.0F;
   private static final float Хьу = 0.125F;
   private static final String Хь2 = "N";
   private static final float Хьф = 14.0F;
   private static final float Хьи = 20.0F;
   private static final float Хь9 = 8.0F;
   private static final float Хьо = 8.0F;
   private static final float ХьO = 60.0F;
   private static final float Хь7 = 0.01F;
   private static final float ХьР = 22.0F;
   private static final float Хь3 = 8.0F;
   private static final float Хьр = 255.0F;
   private static final float Хь6 = 255.0F;
   private static final float Хь_ = 14.0F;
   private static final float ХьШ = 8.0F;
   private static final float Хьы = 1.9F;
   private static final float Хьй = 255.0F;
   private static final float ХьЖ = 12.0F;
   private static final float Хьм = 43.0F;
   private static final float ХьК = 20.0F;
   private static final String ХьI = "Классика";
   private static final float ХьТ = 76.0F;
   private static final float ХьО = 38.0F;
   private static final float ХьФ = 38.0F;
   private static final float ХьЙ = 0.5F;
   private static final float Хьк = 10.0F;
   private static final float Хьг = 17.0F;
   private static final double Хь0 = Math.PI * 2;
   private static final double Хьх = -0.3;
   private static final double ХьУ = 0.3;
   private static final double ХьЗ = 4.0;
   private static final double ХьЕ = 20.0;
   private static final double ХьП = 16.0;
   private static final double Хь西 = 34.0;
   private static final double Хьд = 6.0;
   private static final double ХьБ = Math.PI * 2;
   private static final double ХьС = 8.0;
   private static final double Хьз = 22.0;
   private static final double Хьц = -6.0;
   private static final float Хья = 6.0F;
   private static final double Хжс = -6.0;
   private static final float ХжВ = 6.0F;
   private static final double ХжЯ = 5.0;
   private static final float ХжХ = 2.5F;
   private static final double Хже = Math.PI * 2;
   private static final double ХжА = 6.0;
   private static final double Хж4 = 16.0;
   private static final double Хжп = -6.0;
   private static final float ХжД = 6.0F;
   private static final double ХжЪ = -6.0;
   private static final float Хжт = 6.0F;
   private static final double Хжа = 4.0;
   private static final float ХжЮ = 0.05F;
   private static final float ХжЧ = 0.35F;
   private static final float ХжЛ = 1.5F;
   private static final float Хж诶 = 58.0F;
   private static final float Хж1 = 0.06F;
   private static final float ХжЬ = 74.0F;
   private static final double Хж衣 = -3.5;
   private static final double Хжш = 3.5;
   private static final double ХжИ = -Math.PI * 9.0 / 10.0;
   private static final double Хжь = Math.PI * 9.0 / 10.0;
   private static final double Хжж = 20.0;
   private static final double Хж弟 = 52.0;
   private static final double Хжб = 6.0;
   private static final double ХжЫ = 16.0;
   private static final float Хжl = 0.05F;
   private static final float Хж8 = 0.002F;
   private static final float Хж5 = 0.02F;
   private static final float Хжв = 0.18F;
   private static final double Хжъ = 1.0E-4;
   private static final double ХжМ = Math.PI;
   private static final double ХжН = 2.0;
   private static final double Хжн = 1.4;
   private static final double ХжЦ = 1.05;
   private static final double Хжч = 2.4;
   private static final double ХжЭ = Math.PI;
   private static final double Хж必 = 2.0;
   private static final double Хжщ = -0.55;
   private static final double ХжГ = 0.55;
   private static final double Хжл = 0.35;
   private static final double Хжэ = 1.05;
   private static final double Хжю = -0.2;
   private static final double ХжЩ = 0.35;
   private static final double Хжу = -3.5;
   private static final double Хж2 = 3.5;
   private static final float Хжф = 255.0F;
   private static final float Хжи = 10.0F;
   private static final float Хж9 = 0.75F;
   private static final float Хжо = 6.0F;
   private static final float ХжO = 6.0F;
   private static final float Хж7 = 8.0F;
   private static final float ХжР = 8.0F;
   private static final float Хж3 = 3.0F;
   private static final float Хжр = 6.0F;
   private static final float Хж6 = 6.0F;
   private static final float Хж_ = 8.0F;
   private static final float ХжШ = 8.0F;
   private static final float Хжы = 3.0F;
   private static final float Хжй = 0.15F;
   private static final float ХжЖ = 0.6F;
   private static final float Хжм = 0.4F;
   private static final float ХжК = 3.0F;
   private static final float ХжI = 0.8F;
   private static final float ХжТ = 0.2F;
   private static final float ХжО = 0.01F;
   private static final float ХжФ = 0.75F;
   private static final float ХжЙ = 0.25F;
   private static final double Хжк = 255.0;
   private static final float Хжг = 0.5F;
   private static final float Хж0 = 2.2F;
   private static final float Хжх = 0.11F;
   private static final float ХжУ = 0.004F;
   private static final float ХжЗ = 0.7F;
   private static final float ХжЕ = -22.0F;
   private static final float ХжП = 20.0F;
   private static final float Хж西 = 20.0F;
   private static final float Хжд = 20.0F;
   private static final float ХжБ = 20.0F;
   private static final float ХжС = 20.0F;
   private static final float Хжз = 20.0F;
   private static final float Хжц = 20.0F;
   private static final float Хжя = 20.0F;
   private static final float Х弟с = 74.0F;
   private static final float Х弟В = 12.0F;
   private static final String Х弟Я = "h";
   private static final float Х弟Х = 74.0F;
   private static final float Х弟е = 74.0F;
   private static final String Х弟А = "h";
   private static final float Х弟4 = 15.0F;
   private static final float Х弟п = 4.0F;
   private static final float Х弟Д = 36.0F;
   private static final float Х弟Ъ = 78.0F;
   private static final float Х弟т = 74.0F;
   private static final float Х弟а = 13.0F;
   private static final float Х弟Ю = 4.0F;
   private static final float Х弟Ч = 29.0F;
   private static final String Х弟Л = "hud/target_skin";
   private static final String Х弟诶 = "hud/target_skin_hit";

   private void Я(float var1, float var2, boolean var3, float var4, float var5, float var6) {
      if (!this.Вг.getValue()) {
         this.method_6(var1, var2, var3);
      } else {
         float var7 = var3 ? this.Ъу : this.ЪЩ;
         float var8 = var3 ? this.Ъю : this.Ъл;
         boolean var9 = var8 >= 0.0F && var2 < var8 - Хжl;
         boolean var10 = var7 >= 0.0F && var1 < var7 - Хж8;
         if (var9 || var10) {
            float var11 = var7 < 0.0F ? var1 : var7;
            float var12 = Math.min(var1, var11);
            float var13 = Math.max(var1, var11);
            float var14 = Math.max(Хж5, var13 - var12);
            int var15 = var9 ? Math.round(this.Х8.getValue()) : Math.max(2, Math.round(this.Х8.getValue() * Хжв));
            ThreadLocalRandom var16 = ThreadLocalRandom.current();
            boolean var17 = this.ЗЪ();
            int var18 = var3 ? Д衣 : 0;
            double var19 = this.Ъ9 > Хжъ ? var14 / this.Ъ9 * ХжМ * ХжН : Хжн;

            for (int var21 = 0; var21 < var15; var21++) {
               double var22 = var12 + var14 * Math.pow(var16.nextDouble(), var9 ? ХжЦ : Хжч);
               double var24 = var22 * ХжЭ * Хж必;
               double var26 = var6 + var16.nextDouble(Хжщ, ХжГ);
               double var28 = -var19 * var16.nextDouble(Хжл, Хжэ) + var16.nextDouble(Хжю, ХжЩ);
               double var30 = var16.nextDouble(Хжу, Хж2);
               this.п.add(new TargetHud.EffectParticle(var4, var5, var26, var24, var28, var30, var18, var17));
            }
         }

         this.method_6(var1, var2, var3);
      }
   }

   public TargetHud(SingleChoiceSetting var1, BooleanSetting var2, BooleanSetting var3, FloatSetting var4, SingleChoiceSetting var5, BooleanSetting var6) {
      super(Хш7, ХшР, Хш3, Хшр, Хш6);
      this.сщ = new FloatAnimation(0.0F, Хш_, Easings.CUBIC_OUT);
      this.сГ = new FloatAnimation(0.0F, ХшШ, Easings.BACK_OUT);
      this.сл = new FloatAnimation(0.0F, Хшы, Easings.CUBIC_OUT);
      this.сэ = new FloatAnimation(0.0F, Хшй, Easings.CUBIC_OUT);
      this.сю = new FloatAnimation(0.0F, ХшЖ, Easings.CUBIC_OUT);
      this.сЩ = new FloatAnimation(0.0F, Хшм, Easings.CUBIC_OUT);
      this.су = new FloatAnimation(0.0F, ХшК, Easings.CUBIC_OUT);
      this.В_587528a4 = new AnimatedIntegerText();
      this.с2 = new FloatAnimation(0.0F, ХшI, Easings.CUBIC_OUT);
      this.сф = new FloatAnimation(0.0F, ХшТ, Easings.CUBIC_OUT);
      this.В_f15b0243 = new DecimalFormat(ХшО, new DecimalFormatSymbols(Locale.US));
      this.п = new CopyOnWriteArrayList<>();
      this.Ъ = null;
      this.Я = new RoseBatchedItemAtlas.ItemHandle[6];
      this.ь = ItemStack.EMPTY;
      this.ЪГ = ХшФ;
      this.Ъл = ХшЙ;
      this.Ъэ = Хшк;
      this.Ъю = Хшг;
      this.ЪЩ = Хш0;
      this.Ъу = Хшх;
      this.Ъ2 = ХшУ;
      this.Ъф = 0.0F;
      this.ХХ = this::弟;

      for (int var7 = 0; var7 < this.Я.length; var7++) {
         this.Я[var7] = new RoseBatchedItemAtlas.ItemHandle(true);
      }

      this.ст = var1;
      this.Вк = var2;
      this.Вг = var3;
      this.Х8 = var4;
      this.са = var5;
      this.В0 = var6;
      this.е = new HudSettingsPanel(ХшЗ, var2, var4, var5, var6, ХшЕ, var1, var3, ХшП);
   }

   private void method_6(float var1, float var2, boolean var3) {
      if (var3) {
         this.Ъу = var1;
         this.Ъю = var2;
      } else {
         this.ЪЩ = var1;
         this.Ъл = var2;
      }
   }

   private void method_1(float var1, float var2, boolean var3) {
      if (var3) {
         this.Ъэ = var1;
         this.Ъю = var2;
      } else {
         this.ЪГ = var1;
         this.Ъл = var2;
      }
   }

   private float method_4衣() {
      return this.ст.isSelected(ХИч) ? ХИЭ : ХИ必;
   }

   private void Я(LivingEntity var1, float var2) {
      this.Я(var1, var2, ХьТ, ХьО, ХьФ);
   }

   private float Я_f67ab10a(Entity var1) {
      if (this.ст.isSelected(ХИА)) {
         return ХИ4;
      }

      if (var1 == null) {
         return ХИп;
      }

      float var2 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Я_388008bb(var1), ХИД, 0.0F);
      return Math.max(ХИЪ, ХИт + var2 + ХИа);
   }

   private float method_4К() {
      return 1.0F - Хь5 * (1.0F - this.сГ.getValue());
   }

   private void Я(LivingEntity var1, float var2, float var3, GuiGraphicsExtractor var4, float var5) {
      ArrayList var6 = new ArrayList();

      for (EquipmentSlot var8 : EquipmentSlotGroup.ARMOR) {
         if (var8.getType() == Type.HUMANOID_ARMOR) {
            ItemStack var9 = var1.getItemBySlot(var8);
            if (!var9.isEmpty()) {
               var6.add(var9);
            }
         }
      }

      int var16 = var6.size();
      ItemStack var17 = var1.getOffhandItem();
      ItemStack var18 = var1.getMainHandItem();
      int var10 = var16 + (var17.isEmpty() ? 0 : 1) + (var18.isEmpty() ? 0 : 1);
      if (var10 > 0) {
         int var11 = 0;
         float var12 = 0.0F;
         float var13 = this.Л(var3, ХжЕ);
         if (!var17.isEmpty()) {
            UiBatchDrawing.drawItemWithOverlay(this.Л(var2, var12), var13, ХжП, Хж西, var17, true, this.Я[var11++]);
            var12 += Хжд;
         }

         if (!var18.isEmpty()) {
            UiBatchDrawing.drawItemWithOverlay(this.Л(var2, var12), var13, ХжБ, ХжС, var18, true, this.Я[var11++]);
            var12 += Хжз;
         }

         for (int var14 = 0; var14 < var16; var14++) {
            float var15 = this.Л(var2, var12 + var14 * 20);
            UiBatchDrawing.drawItemWithOverlay(var15, var13, Хжц, Хжя, (ItemStack)var6.get(var14), true, this.Я[var11++]);
         }
      }
   }

   private void Я(Entity var1, float var2, float var3, float var4, HudRenderEvent var5) {
      if (var1 instanceof LivingEntity var6) {
         if (this.ст.isSelected(ХИЮ)) {
            this.Я(var1, var6, var2, var3);
         } else {
            var3 += ХИЧ * (1.0F - this.сГ.getValue());
            float var7 = this.сщ.getValue();
            float var8 = var4 - ХИЛ - ХИ诶;
            float var9 = this.method_6(var6);
            float var10 = var6.getAbsorptionAmount();
            float var11 = var6.getMaxHealth();
            ItemStack var12 = this.Я_a4db5d8e(var6);
            if (!var12.isEmpty()) {
               this.ь = var12.copy();
            }

            this.с2.updateTarget(var12.isEmpty() ? 0.0F : 1.0F);
            float var13 = Math.min(this.сэ.getValue() / var11, 1.0F);
            float var14 = var8 * var13;
            float var15 = Math.min(this.сф.getValue() / var11, 1.0F);
            float var16 = var8 * var15;
            this.Я(var6, var4);
            this.е(var14, var9);
            this.method_4(var16, var10, true);
            float var17 = this.method_4К();
            float var18 = this.method_4_();
            UiDrawing.setGlobalAlpha(ХИ1 * this.сщ.getValue());
            WorldProjection.scaleAroundRect(var2, var3, var4, ХИЬ, var17);
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            UiBatchDrawing.drawHudWindowBackground(var2, var3, var4, ХИ衣, ХИш);
            this.Я(var1, var2, var3, -1, var7);
            this.Я(var6, var12, var2, var3, var5.getGraphics(), var7);
            int var19 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
            int var20 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorAlpha(ThemeColorSlot.MAIN) / ХИИ * ХИь);
            UiBatchDrawing.drawRoundedRect(var2 + ХИж, var3 + ХИ弟, var8, ХИб, ХИЫ, var20);
            UiBatchDrawing.drawRoundedRect(var2 + ХИl, var3 + ХИ8, var14 * var18, ХИ5, ХИв, var19);
            if (var16 > 0.0F) {
               UiBatchDrawing.drawRoundedRect(var2 + ХИъ, var3 + ХИМ, var16 * var18, ХИН, ХИн, Д衣);
            }

            this.Я(var1, var2, var3, var9, var14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), var10);
            UiDrawing.disableScissor();
            this.Я(var6, var2, var3, var5.getGraphics(), var7);
            this.ш(var2, var3, var7);
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            WorldProjection.unscaleAroundRect(var2, var3, var4, ХИЦ, var17);
            UiDrawing.resetGlobalAlpha();
         }
      }
   }

   private void Я(Entity var1, float var2, float var3) {
      if (!this.В0.getValue()) {
         if (var1 instanceof LivingEntity var6) {
            GpuTextureView var7 = ClientTextureLookup.Н_dd184401(var6);
            if (var7 != null) {
               UiBatchDrawing.drawTextureNearestFlipped(var2 + Хьш, var3 + ХьИ, Хьь, Хьж, new Vector4f(Хь弟), new Vector4f(Хьб), new Vector4i(-1), var7);
            }
         }
      } else {
         Identifier var4 = var1 instanceof LivingEntity var5 && var5.hurtTime > 0 ? У : х;
         UiBatchDrawing.flushBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         UiBatchDrawing.drawTexture(var2 + ХьЛ, var3 + Хь诶, Хь1, ХьЬ, new Vector4f(Хь衣), new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), new Vector4i(-1), var4);
      }
   }

   private void З(float var1, float var2, int var3) {
      String var4 = this.В_587528a4.цС();
      float var5 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var4, Хьт, 0.0F);
      if (!this.В_587528a4.Тв()) {
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var4, var1 - var5 / 2.0F, var2, 13, var3);
      } else {
         String var6 = this.В_587528a4.цг();
         float var7 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var6, Хьа, 0.0F);
         float var8 = this.В_587528a4.РВ();
         float var9 = this.В_587528a4.method_2в() * 17;
         float var10 = Math.max(var5, var7) + ХьЮ;
         UiBatchDrawing.flushBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         UiDrawing.pushScissor(var1 - var10 / 2.0F, var2 - 1.0F, var10, ХьЧ);

         try {
            UiDrawing.drawText(ClientFonts.getMediumFont(), var6, var1 - var7 / 2.0F, var2 + var9 * var8, 13, var3);
            UiDrawing.drawText(ClientFonts.getMediumFont(), var4, var1 - var5 / 2.0F, var2 - var9 * (1.0F - var8), 13, var3);
         } finally {
            UiDrawing.popScissor();
         }
      }
   }

   private boolean ЗЪ() {
      return this.са.isSelected(ХьI);
   }

   private void ш(float var1, float var2, float var3) {
      if (this.Вк.getValue() || this.Вг.getValue() || !this.п.isEmpty()) {
         long var4 = System.currentTimeMillis();
         this.п.removeIf(var2x -> var4 - var2x.ЯЭ > var2x.Я必);
         int var6 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);

         for (TargetHud.EffectParticle var8 : this.п) {
            int var9 = var8.field_4Ю != 0 ? var8.field_4Ю : var6;
            float var10 = var8.field_4Ю != 0 ? 1.0F : ThemeEditor.getColorAlpha(ThemeColorSlot.MAIN) / Хжф;
            if (var8.Х4 && !var8.Хп) {
               var8.пь = AimGeometryMath.lerpByFrameTime(var8.пь, 1.0F, Хжи);
               var8.I = AimGeometryMath.lerpVectorComponents(var8.I, var8.Т, Хж9);
               float var27 = 1.0F - (float)(var4 - var8.ЯЭ) / (float)var8.Я必;
               float var29 = var1 + (float)var8.I.x;
               float var31 = var2 + (float)var8.I.y;
               UiBatchDrawing.drawRoundedRect(var29 - Хжо, var31 - ХжO, Хж7, ХжР, Хж3, ColorMath.withAlpha(var9, var10 * var8.пь * var27 * var3));
            } else {
               var8.л(this.Ъ9, var4);
               if (var8.Х4) {
                  float var26 = 1.0F - (float)(var4 - var8.ЯЭ) / (float)var8.Я必;
                  float var28 = var1 + (float)var8.Вг;
                  float var30 = var2 + (float)var8.В0;
                  UiBatchDrawing.drawRoundedRect(var28 - Хжр, var30 - Хж6, Хж_, ХжШ, Хжы, ColorMath.withAlpha(var9, var10 * var26 * var3));
               } else {
                  float var11 = (float)(var4 - var8.ЯЭ) / (float)var8.Я必;
                  float var12 = Math.min(var11 / Хжй, 1.0F);
                  float var13 = 1.0F - Math.max(0.0F, (var11 - ХжЖ) / Хжм);
                  float var14 = var12 * var12 * (ХжК - 2.0F * var12) * var13;
                  float var15 = ХжI + ХжТ * (float)Math.sin((float)(var4 - var8.ЯЭ) * ХжО + var8.пш);
                  float var16 = var8.п衣 * (ХжФ + ХжЙ * var13);
                  int var17 = ColorMath.lerpArgb(var9, ColorMath.packArgb(255, 255, 255, Хжк), var8.пИ * Хжг);
                  float var18 = var1 + (float)var8.Вг;
                  float var19 = var2 + (float)var8.В0;
                  float var20 = var3 * var14 * var15;
                  float var21 = var16 * Хж0;

                  for (int var22 = (int)var21; var22 > 0; var22--) {
                     float var23 = 1.0F - var22 / var21;
                     float var24 = var20 * Хжх * var23 * var23;
                     if (!(var24 <= ХжУ)) {
                        float var25 = var16 + var22;
                        UiBatchDrawing.drawRoundedRect(var18 - var25, var19 - var25, var25 * 2.0F, var25 * 2.0F, var25 - 1.0F, ColorMath.withAlpha(var9, var24));
                     }
                  }

                  UiBatchDrawing.drawRoundedRect(var18 - var16, var19 - var16, var16 * 2.0F, var16 * 2.0F, var16 - 1.0F, ColorMath.withAlpha(var17, var20 * ХжЗ));
               }
            }
         }
      }
   }

   private void е(float var1, float var2) {
      this.method_4(var1, var2, false);
   }

   private Entity Я_f553a72a(LivingEntity var1) {
      return var1 == null && MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen ? MINECRAFT_CLIENT.player : var1;
   }

   @Override
   public void иВ() {
      this.е.сУ();
   }

   @Override
   public void Б5() {
      this.е.сС();
   }

   private void Я(Entity var1, LivingEntity var2, float var3, float var4) {
      float var5 = this.сщ.getValue();
      float var6 = ХИщ + ХИГ * var5;
      float var7 = this.сю.getValue();
      float var8 = this.сЩ.getValue();
      float var9 = this.су.getValue();
      float var10 = Math.max(1.0F, var2.getMaxHealth());
      float var11 = Math.clamp(var7 / var10, 0.0F, 1.0F);
      float var12 = Math.clamp(var8 / var10, 0.0F, 1.0F);
      float var13 = var2.getAbsorptionAmount();
      float var14 = Math.clamp(this.сф.getValue() / var10, 0.0F, 1.0F);
      float var15 = ХИл;
      float var16 = ХИэ;
      float var17 = ХИю;
      this.Я(var2, ХИЩ, ХИу, ХИ2, ХИф);
      this.Я(var11, this.method_6(var2), false, var15, var16, var17);
      this.Я(var14, var13, true, var15, var16, var17);
      UiDrawing.setGlobalAlpha(ХИи * var5);
      WorldProjection.scaleAroundRect(var3, var4, ХИ9, ХИо, var6);
      UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
      UiBatchDrawing.drawHudWindowBackground(var3, var4, ХИO, ХИ7, ХИР);
      this.Я(var1, var3, var4);
      String var18 = this.Я_388008bb(var1);
      UiBatchDrawing.drawFadedText(ClientFonts.getMediumFont(), Component.literal(var18), var3 + ХИ3, var4 + ХИр, 14, ColorMath.packRgb(255, 255, 255), ХИ6, ХИ_);
      ItemStack[] var19 = new ItemStack[]{
         var2.getItemBySlot(EquipmentSlot.FEET),
         var2.getItemBySlot(EquipmentSlot.LEGS),
         var2.getItemBySlot(EquipmentSlot.CHEST),
         var2.getItemBySlot(EquipmentSlot.HEAD),
         var2.getMainHandItem(),
         var2.getOffhandItem()
      };
      ArrayList var20 = new ArrayList();

      for (ItemStack var24 : var19) {
         if (!var24.isEmpty()) {
            var20.add(var24);
         }
      }

      for (int var25 = 0; var25 < var20.size(); var25++) {
         float var27 = this.Л(var3, 60 + var25 * 18);
         float var29 = this.Л(var4, ХИШ);
         UiBatchDrawing.drawItemWithOverlay(var27, var29, ХИы, ХИй, (ItemStack)var20.get(var25), false, this.Я[var25]);
      }

      int var26 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var28 = ColorMath.withAlpha(var26, ThemeEditor.getColorAlpha(ThemeColorSlot.MAIN) / ХИЖ * ХИм);
      float var30 = ХИК + ХИI * var9;
      UiBatchDrawing.drawCircularProgress(var3 + ХИТ, var4 + ХИО, ХИФ, ХИЙ, 1.0F, var28);
      if (var12 > var11 + ХИк) {
         UiBatchDrawing.drawCircularProgress(var3 + ХИг, var4 + ХИ0, ХИх, ХИУ, var12, ColorMath.withAlpha(var26, ХИЗ));
      }

      if (var11 > ХИЕ) {
         if (var9 > ХИП) {
            UiBatchDrawing.drawCircularProgress(var3 + ХИ西, var4 + ХИд, ХИБ, ХИС, var11, ColorMath.withAlpha(var26, ХИз * var9));
         }

         UiBatchDrawing.drawCircularProgress(var3 + ХИц, var4 + ХИя, Хьс, var30, var11, var26);
      }

      if (var14 > ХьВ) {
         UiBatchDrawing.drawCircularProgress(var3 + ХьЯ, var4 + ХьХ, Хье, var30, var14, Д衣);
      }

      int var31 = ColorMath.lerpArgb(ColorMath.packRgb(255, 255, 255), var26, ХьА * var9);
      this.З(var3 + Хь4, var4 + Хьп, var31);
      UiDrawing.disableScissor();
      this.ш(var3, var4, var5);
      UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
      WorldProjection.unscaleAroundRect(var3, var4, ХьД, ХьЪ, var6);
      UiDrawing.resetGlobalAlpha();
   }

   private void Я(LivingEntity var1, ItemStack var2, float var3, float var4, GuiGraphicsExtractor var5, float var6) {
      float var7 = var3 + Хь9;
      float var8 = var4 + Хьо;
      float var9 = ХьO;
      float var10 = this.с2.getValue() * var6;
      if (!(var10 <= Хь7)) {
         UiBatchDrawing.flushBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         UiBatchDrawing.drawBlur(var7, var8, var9, var9, ХьР, Хь3, ColorMath.packArgb(255, 255, 255, Хьр * var10));
         ItemStack var11 = var2.isEmpty() ? this.ь : var2;
         if (!var11.isEmpty()) {
            UiBatchDrawing.flushBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            UiDrawing.setGlobalAlpha(Хь6 * var10);
            UiDrawing.drawItem(var11, var7 + Хь_, var8 + ХьШ, Хьы, false);
            UiDrawing.setGlobalAlpha(Хьй * this.сщ.getValue());
         }

         if (!var2.isEmpty()) {
            String var12 = this.Я_388008bb(var1);
            float var13 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var12, ХьЖ, 0.0F);
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var12, var7 + var9 / 2.0F - var13 / 2.0F, var8 + Хьм, 12, ColorMath.withAlpha(-1, var10));
         }
      }
   }

    public void 弟(HudRenderEvent var1) {
       if (this.ст.isSelected("Catlavan")) {
          this.renderCatlavan();
          return;
       }
       this.catlavan.reset();
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(Хш西)) {
         this.сщ.setImmediately(0.0F);
         this.сГ.setImmediately(0.0F);
         this.сл.setImmediately(0.0F);
         this.еъ = false;
         this.п.clear();
         this.Хэ = 0L;
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         UiDrawing.setTransform(new Matrix4f());
         long var2 = System.nanoTime();
         this.Ъ9 = this.Хэ == 0L ? 0.0F : Math.min(Хшд, (float)(var2 - this.Хэ) / ХшБ);
         this.Хэ = var2;
         float var4 = this.getX();
         float var5 = this.getY();
         LivingEntity var6 = null;
         Entity var7 = this.Я_f553a72a(var6);
         float var8 = var7 != null ? 1.0F : 0.0F;
         if ((var7 != null ? var7 : this.Ъ) instanceof LivingEntity var10) {
            float var11 = this.method_6(var10);
            float var12 = var10.getAbsorptionAmount();
            if (var7 != this.Ъ && var7 != null) {
               this.сэ.setImmediately(var11);
               this.сю.setImmediately(var11);
               this.сЩ.setImmediately(var11);
               this.су.setImmediately(0.0F);
               this.Ъ2 = var11;
               this.В_587528a4.method_2Р(Math.round(var11));
               this.сф.setImmediately(var12);
               this.с2.setImmediately(0.0F);
               this.ь = ItemStack.EMPTY;
               this.ЪГ = ХшС;
               this.Ъл = Хшз;
               this.Ъэ = Хшц;
               this.Ъю = Хшя;
               this.ЪЩ = ХИс;
               this.Ъу = ХИВ;
               this.Ъф = 0.0F;
               this.Ъи = 0.0F;
               this.п.clear();
            }

            this.сф.updateTarget(var12);
            this.сэ.updateTarget(var11);
            if (this.ст.isSelected(ХИЯ)) {
               if (this.Ъ2 >= 0.0F && Math.abs(var11 - this.Ъ2) > ХИХ) {
                  this.су.setImmediately(1.0F);
               }

               this.сю.updateTarget(var11);
               this.сЩ.updateTarget(var11);
               this.су.updateTarget(0.0F);
               this.В_587528a4.Р(Math.round(var11), this.Ъ9);
               this.Ъ2 = var11;
            }
         }

         this.Хб(var8);
         if (this.сщ.getValue() < ХИе) {
            this.п.clear();
            this.setWidth(0.0F);
            this.setHeight(0.0F);
         } else {
            Entity var16 = var7 != null ? var7 : this.Ъ;
            float var17 = this.Я_f67ab10a(var16);
            if (this.сщ.getValue() > 0.0F) {
               this.pushGuiScaleTransform();

               try {
                  this.Я(var16, var4, var5, var17, var1);
               } finally {
                  this.popGuiScaleTransform();
               }
            }

            this.Ъ = var7 != null ? var7 : this.Ъ;
            this.setHeight(this.method_4衣());
            this.setWidth(var17);
         }
      }
   }

    private String Я_388008bb(LivingEntity var1) {
      float var2 = Math.max(0, var1.getUseItemRemainingTicks()) / ХьК;
      return this.В_f15b0243.format(var2) + "s";
    }

    private void renderCatlavan() {
       boolean enabled = RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(Хш西);
       if (!enabled || MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
          this.catlavan.reset();
          this.Хэ = 0L;
          this.setWidth(0.0F);
          this.setHeight(0.0F);
          return;
       }
       long now = System.nanoTime();
       float dt = this.Хэ == 0L ? 0.016F : Math.clamp((now - this.Хэ) / 1.0E9F, 0.0F, 0.1F);
       this.Хэ = now;
       LivingEntity target = this.catlavan.update(((net.minecraft.world.entity.LivingEntity) null), dt);
       if (target == null) {
          this.setWidth(0.0F);
          this.setHeight(0.0F);
          return;
       }
       this.setWidth(CatlavanTargetHudRenderer.WIDTH);
       this.setHeight(CatlavanTargetHudRenderer.HEIGHT);
        UiBatchDrawing.flushPendingGeometry();
       UiDrawing.setTransform(new Matrix4f());
       UiDrawing.resetGlobalAlpha();
       this.pushGuiScaleTransform();
       try {
          this.catlavan.draw(this.getX(), this.getY());
       } finally {
          this.popGuiScaleTransform();
          UiDrawing.resetGlobalAlpha();
       }
    }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (var3 == 1 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
         this.е.method_2(var1, var2);
         return true;
      } else {
         return this.е.ю(var1, var2, var3);
      }
   }

   private float method_4_() {
      return Math.clamp((this.сл.getValue() - Хьв) / Хьъ, 0.0F, 1.0F);
   }

   @Generated
   public CopyOnWriteArrayList<TargetHud.EffectParticle> method_1() {
      return this.п;
   }

   private void Я(Entity var1, float var2, float var3, int var4, float var5) {
      if (this.В0.getValue()) {
         Identifier var7 = х;
         if (var1 instanceof LivingEntity var8 && var8.hurtTime > 0) {
            var7 = У;
         }

         UiBatchDrawing.flushBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         UiBatchDrawing.drawTexture(
            var2 + ХьМ,
            var3 + ХьН,
            Хьн,
            ХьЦ,
            new Vector4f(Хьч, ХьЭ, Хь必, Хьщ),
            new Vector4f(0.0F, 0.0F, 1.0F, 1.0F),
            new Vector4i(ColorMath.withAlpha(-1, var5)),
            var7
         );
      } else if (var1 instanceof LivingEntity var6) {
         GpuTextureView var9 = ClientTextureLookup.Н_dd184401(var6);
         if (var9 == null) {
            return;
         }

         UiBatchDrawing.drawTextureNearestFlipped(var2 + ХьГ, var3 + Хьл, Хьэ, Хью, new Vector4f(ХьЩ), new Vector4f(Хьу), new Vector4i(-1), var9);
      } else {
         UiBatchDrawing.drawText(ClientFonts.getIconsFont(), Хь2, var2 + Хьф, var3 + Хьи, 38, var4);
      }
   }

   private void Я(LivingEntity var1, float var2, float var3, float var4, float var5) {
      float var6 = var1.hurtTime > 0 ? Math.min(ХьЙ, (float)var1.hurtTime / Math.max(1, var1.hurtDuration)) : 0.0F;
      if (!this.Вк.getValue()) {
         this.Ъф = var6;
      } else {
         boolean var7 = this.ЗЪ();
         int var8 = Math.max(1, Math.round(this.Х8.getValue()));
         ThreadLocalRandom var9 = ThreadLocalRandom.current();
         if (var6 > this.Ъф) {
            if (var7) {
               for (int var10 = 0; var10 < var8; var10++) {
                  this.п.add(new TargetHud.EffectParticle(new Vec3(var4 + 2.0F, var5 + 2.0F, 0.0)));
               }
            } else {
               int var19 = Math.max(1, Math.round(var8 * Хьк / Хьг));
               int var11 = Math.max(0, var8 - var19);

               for (int var12 = 0; var12 < var19; var12++) {
                  double var13 = Хь0 * var12 / var19 + var9.nextDouble(Хьх, ХьУ);
                  double var15 = var9.nextDouble(ХьЗ, ХьЕ);
                  double var17 = var9.nextDouble(ХьП, Хь西);
                  this.п
                     .add(
                        new TargetHud.EffectParticle(
                           var4 + Math.cos(var13) * var15, var5 + Math.sin(var13) * var15, Math.cos(var13) * var17, Math.sin(var13) * var17 - Хьд
                        )
                     );
               }

               for (int var21 = 0; var21 < var11; var21++) {
                  double var23 = var9.nextDouble(ХьБ);
                  double var24 = var9.nextDouble(ХьС, Хьз);
                  this.п
                     .add(
                        new TargetHud.EffectParticle(
                           var9.nextDouble(Хьц, var2 + Хья), var9.nextDouble(Хжс, var3 + ХжВ), Math.cos(var23) * var24, Math.sin(var23) * var24 - ХжЯ
                        )
                     );
               }
            }
         }

         this.Ъф = var6;
         if (!var7) {
            this.Ъи = this.Ъи + this.Ъ9 * ХжХ;

            while (this.Ъи >= 1.0F) {
               this.Ъи--;
               if (this.п.size() >= Math.max(40, var8 * 2)) {
                  break;
               }

               double var20 = var9.nextDouble(Хже);
               double var22 = var9.nextDouble(ХжА, Хж4);
               this.п
                  .add(
                     new TargetHud.EffectParticle(
                        var9.nextDouble(Хжп, var2 + ХжД), var9.nextDouble(ХжЪ, var3 + Хжт), Math.cos(var20) * var22, Math.sin(var20) * var22 - Хжа
                     )
                  );
            }
         }
      }
   }

   private void Хб(float var1) {
      boolean var2 = var1 > 0.0F;
      if (var2 != this.еъ) {
         if (var2 && this.сщ.getValue() < ХьЫ) {
            this.сГ.setImmediately(0.0F);
            this.сл.setImmediately(0.0F);
         }

         this.сГ.setEasing(var2 ? Easings.BACK_OUT : Easings.CUBIC_OUT);
         this.сГ.setSpeed(var2 ? Хьl : Хь8);
         this.еъ = var2;
      }

      this.сщ.updateTarget(var1);
      this.сГ.updateTarget(var1);
      if (var2) {
         this.сл.updateTarget(1.0F);
      }
   }

   private void Я(Entity var1, float var2, float var3, float var4, float var5, int var6, float var7) {
      String var8 = this.Я_388008bb(var1);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var8, var2 + Х弟с, var3 + Х弟В, 16, var6);
      UiBatchDrawing.drawText(
         ClientFonts.getIconsFont(), Х弟Я, var2 + Math.max(Х弟Х, Х弟е + var5 - ClientFonts.getIconsFont().getFontFace().measureTextWidth(Х弟А, Х弟4, 0.0F) / 2.0F - Х弟п), var3 + Х弟Д, 15, var6
      );
      UiBatchDrawing.drawText(
         ClientFonts.getMediumFont(),
         (int)var4 + "",
         var2 + Math.max(Х弟Ъ, Х弟т + var5 - ClientFonts.getMediumFont().getFontFace().measureTextWidth((int)var4 + "", Х弟а, 0.0F) / 2.0F - Х弟Ю),
         var3 + Х弟Ч,
         13,
         var6
      );
   }

   private String Я_388008bb(Entity var1) {
      if (var1 == null) {
         return "";
      } else {
         return var1 instanceof Player var2 ? NameProtectionTextRewriter.С(var2) : NameProtectionTextRewriter.лШ(var1.getName().getString());
      }
   }

   private void method_4(float var1, float var2, boolean var3) {
      if (!this.Вг.getValue()) {
         this.method_1(var1, var2, var3);
      } else {
         float var4 = var3 ? this.Ъэ : this.ЪГ;
         float var5 = var3 ? this.Ъю : this.Ъл;
         boolean var6 = var5 >= 0.0F && var2 < var5 - ХжЮ;
         boolean var7 = var4 >= 0.0F && var1 < var4 - ХжЧ;
         if (var6 || var7) {
            float var8 = Math.max(var1, var4);
            float var9 = Math.max(ХжЛ, var8 - var1);
            float var10 = Хж诶;
            int var11 = var6 ? Math.round(this.Х8.getValue()) : Math.max(1, Math.round(this.Х8.getValue() * Хж1));
            ThreadLocalRandom var12 = ThreadLocalRandom.current();
            boolean var13 = this.ЗЪ();
            int var14 = var3 ? Д衣 : 0;

            for (int var15 = 0; var15 < var11; var15++) {
               double var16 = ХжЬ + var1 + var12.nextDouble(0.0, var9);
               double var18 = var10 + var12.nextDouble(Хж衣, Хжш);
               if (var13) {
                  this.п.add(new TargetHud.EffectParticle(new Vec3(var16, var18, 0.0), var14));
               } else {
                  double var20 = var12.nextDouble(ХжИ, Хжь);
                  double var22 = var12.nextDouble(Хжж, Хж弟);
                  this.п
                     .add(new TargetHud.EffectParticle(var16, var18, Math.cos(var20) * var22, Math.sin(var20) * var22 - var12.nextDouble(Хжб, ХжЫ), var14));
               }
            }
         }

         this.method_1(var1, var2, var3);
      }
   }

   private float Л(float var1, float var2) {
      float var3 = HudScaleCoordinates.getHudScale();
      return var1 + (Math.round(var1 + var2 * var3) - var1) / var3;
   }

   private float method_6(LivingEntity var1) {
      boolean var2 = RoseClient.getInstance().getServices().getModules().getScoreboardHealth().isEnabled();
      if (var2) {
         float var3 = EntityHealth.getHealth(var1);
         float var4 = var1.getHealth() + var1.getAbsorptionAmount();
         return var3 == Math.floor(var4) ? var1.getHealth() : Math.max(0.0F, var3);
      } else {
         return var1.getHealth();
      }
   }

   @Generated
   public HudSettingsPanel method_6_10d41d08() {
      return this.е;
   }

   private ItemStack Я_a4db5d8e(LivingEntity var1) {
      if (!var1.isUsingItem()) {
         return ItemStack.EMPTY;
      }

      ItemStack var2 = var1.getUseItem();
      if (var2.isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemUseAnimation var3 = var2.getUseAnimation();
      return var3 != ItemUseAnimation.EAT && var3 != ItemUseAnimation.DRINK ? ItemStack.EMPTY : var2;
   }

   public static class EffectParticle {
      private final boolean Х4;
      private final boolean Хп;
      @SerializedName("4Ю")
      private final int field_4Ю;
      private final long ЯЭ = System.currentTimeMillis();
      private final long Я必;
      private final float п衣;
      private final float пш;
      private final float пИ;
      private double Вг;
      private double В0;
      private double Вх;
      private double ВУ;
      private double ВЗ;
      private double ВЕ;
      private double ВП;
      private double В西;
      private double Вд;
      private Vec3 I;
      private Vec3 Т;
      private float пь;
      private static final float ЯжК = 10.0F;
      private static final long ЯжI = 900L;
      private static final long ЯжТ = 700L;
      private static final float ЯжО = 1.8F;
      private static final float ЯжФ = 1.8F;
      private static final float ЯжЙ = 10.0F;
      private static final long Яжк = 1250L;
      private static final long Яжг = 750L;
      private static final float Яж0 = 4.0F;
      private static final long Яжх = 900L;
      private static final long ЯжУ = 700L;
      private static final float ЯжЗ = 1.8F;
      private static final float ЯжЕ = 1.8F;
      private static final float ЯжП = 10.0F;
      private static final float Яж西 = -75.0F;
      private static final float Яжд = 75.0F;
      private static final float ЯжБ = -75.0F;
      private static final float ЯжС = 75.0F;
      private static final float Яжз = -75.0F;
      private static final float Яжц = 75.0F;
      private static final long Яжя = 1250L;
      private static final long Я弟с = 750L;
      private static final float Я弟В = 4.0F;
      private static final float Я弟Я = 10.0F;
      private static final double Я弟Х = 0.7;
      private static final double Я弟е = 3.2;
      private static final double Я弟А = 0.004;
      private static final float Я弟4 = 4.0F;
      private static final double Я弟п = 0.4;
      private static final double Я弟Д = 0.55;
      private static final double Я弟Ъ = 0.55;
      private static final float Я弟т = 18.0F;
      private static final double Я弟а = 0.002;
      private static final float Я弟Ю = 4.0F;
      private static final double Я弟Ч = 6.0;
      private static final double Я弟Л = 0.0017;
      private static final float Я弟诶 = 3.0F;
      private static final double Я弟1 = 4.0;

      public EffectParticle(double var1, double var3, double var5, double var7) {
         this(var1, var3, var5, var7, 0);
      }

      public EffectParticle(Vec3 var1) {
         this(var1, 0);
      }

      private void л(float var1, long var2) {
         if (this.Хп) {
            this.Вд = this.Вд + this.Вх * var1;
            this.ВП = this.ВП + this.ВУ * var1;
            this.ВП = this.ВП + (this.В西 - this.ВП) * Math.min(1.0F, var1 * Я弟Я);
            this.Вх = this.Вх * Math.exp(-var1 * Я弟Х);
            this.ВУ = this.ВУ * Math.exp(-var1 * Я弟е);
            double var4 = Math.sin(var2 * Я弟А + this.пш * Я弟4) * Я弟п;
            this.Вг = this.ВЗ + Math.sin(this.Вд) * (this.ВП + var4);
            this.В0 = this.ВЕ - Math.cos(this.Вд) * (this.ВП + var4);
         } else {
            this.Вх = this.Вх * Math.exp(-var1 * Я弟Д);
            this.ВУ = this.ВУ * Math.exp(-var1 * Я弟Ъ);
            this.ВУ = this.ВУ + Я弟т * var1;
            this.Вг = this.Вг + (this.Вх * var1 + Math.sin(var2 * Я弟а + this.пш * Я弟Ю) * Я弟Ч * var1);
            this.В0 = this.В0 + (this.ВУ * var1 + Math.cos(var2 * Я弟Л + this.пш * Я弟诶) * Я弟1 * var1);
         }
      }

      public EffectParticle(Vec3 var1, int var2) {
         this.пш = ThreadLocalRandom.current().nextFloat() * ЯжП;
         this.пИ = ThreadLocalRandom.current().nextFloat();
         this.Х4 = true;
         this.Хп = false;
         this.field_4Ю = var2;
         this.I = var1;
         this.Т = var1.add(
            -ThreadLocalRandom.current().nextFloat(Яж西, Яжд),
            -ThreadLocalRandom.current().nextFloat(ЯжБ, ЯжС),
            -ThreadLocalRandom.current().nextFloat(Яжз, Яжц)
         );
         this.Я必 = Яжя + ThreadLocalRandom.current().nextLong(Я弟с);
         this.п衣 = Я弟В;
      }

      public EffectParticle(double var1, double var3, double var5, double var7, double var9, double var11, int var13, boolean var14) {
         this.пш = ThreadLocalRandom.current().nextFloat() * ЯжЙ;
         this.пИ = ThreadLocalRandom.current().nextFloat();
         this.Х4 = var14;
         this.Хп = true;
         this.field_4Ю = var13;
         this.ВЗ = var1;
         this.ВЕ = var3;
         this.ВП = var5;
         this.В西 = var5;
         this.Вд = var7;
         this.Вх = var9;
         this.ВУ = var11;
         this.Вг = var1 + Math.sin(var7) * var5;
         this.В0 = var3 - Math.cos(var7) * var5;
         if (var14) {
            this.Я必 = Яжк + ThreadLocalRandom.current().nextLong(Яжг);
            this.п衣 = Яж0;
         } else {
            this.Я必 = Яжх + ThreadLocalRandom.current().nextLong(ЯжУ);
            this.п衣 = ЯжЗ + ThreadLocalRandom.current().nextFloat() * ЯжЕ;
         }
      }

      public EffectParticle(double var1, double var3, double var5, double var7, int var9) {
         this.пш = ThreadLocalRandom.current().nextFloat() * ЯжК;
         this.пИ = ThreadLocalRandom.current().nextFloat();
         this.Х4 = false;
         this.Хп = false;
         this.field_4Ю = var9;
         this.Вг = var1;
         this.В0 = var3;
         this.Вх = var5;
         this.ВУ = var7;
         this.Я必 = ЯжI + ThreadLocalRandom.current().nextLong(ЯжТ);
         this.п衣 = ЯжО + ThreadLocalRandom.current().nextFloat() * ЯжФ;
      }
   }
}
