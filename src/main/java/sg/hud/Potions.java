package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.events.impl.HudRenderEvent;
import sg.gui.hud.HudSettingsPanel;
import sg.settings.SingleChoiceSetting;

public class Potions extends HudElement implements InteractiveHudElement {
   private static final int еэ = 72000;
   private final FloatAnimation П;
   private final FloatAnimation 西;
   private final FloatAnimation д;
   private final FloatAnimation Б;
   private final Map<Potions.EffectEntry, FloatAnimation[]> сЮ;
   private final Map<Potions.EffectEntry, FloatAnimation[]> сЧ;
   private final Map<Potions.EffectEntry, Integer> сЛ;
   private final List<Potions.EffectEntry> са;
   @SerializedName("А")
   private final Random А_at_d53d68ac;
   private long Ву;
   private final SingleChoiceSetting й;
   private final HudSettingsPanel Я;
   private final EventListener<HudRenderEvent> ВЦ;
   private static final String с9ю = "Potions";
   private static final float с9Щ = 691.0F;
   private static final float с9у = 801.0F;
   private static final float с92 = 120.0F;
   private static final float с9ф = 35.0F;
   private static final float с9и = 15.0F;
   private static final float с99 = 15.0F;
   private static final float с9о = 10.0F;
   private static final float с9O = 10.0F;
   private static final String с97 = "Potions";
   private static final String с9Р = "Вид";
   private static final String с93 = "Активные зелья";
   private static final String с9р = "2";
   private static final float с96 = 0.001F;
   private static final int с9_ = 72000;
   private static final String с9Ш = "**:**";
   private static final String с9ы = "%d:%02d";
   private static final float с9й = 13.0F;
   private static final float с9Ж = 13.0F;
   private static final float с9м = 13.0F;
   private static final float с9К = 95.0F;
   private static final float с9I = 50.0F;
   private static final float с9Т = 255.0F;
   private static final float с9О = 12.0F;
   private static final float с9Ф = 33.0F;
   private static final float с9Й = 10.0F;
   private static final float с9к = 0.01F;
   private static final float с9г = 0.5F;
   private static final float с90 = 0.5F;
   private static final double с9х = 700.0;
   private static final float с9У = 0.55F;
   private static final float с9З = 10.0F;
   private static final float с9Е = 33.0F;
   private static final float с9П = 20.0F;
   private static final float с9西 = 1.4F;
   private static final String с9д = "E";
   private static final float с9Б = 10.0F;
   private static final float с9С = 8.0F;
   private static final String с9з = "Potions";
   private static final float с9ц = 32.0F;
   private static final float с9я = 9.0F;
   private static final float сос = 15.0F;
   private static final float соВ = -10.0F;
   private static final float соЯ = 15.0F;
   private static final float соХ = 20.0F;
   private static final float сое = 15.0F;
   private static final float соА = 20.0F;
   private static final float со4 = -10.0F;
   private static final float соп = 36.0F;
   private static final int соД = 72000;
   private static final String соЪ = "**:**";
   private static final String сот = "%d:%02d";
   private static final String соа = "effect.minecraft.";
   private static final String соЮ = "effect.";
   private static final String соЧ = "minecraft";
   private static final float соЛ = 255.0F;
   private static final float со诶 = 10.0F;
   private static final float со1 = 2.5F;
   private static final float соЬ = 16.0F;
   private static final float со衣 = 16.0F;
   private static final float сош = 30.0F;
   private static final float соИ = 2.5F;
   private static final float соь = 30.0F;
   private static final float сож = 13.0F;
   private static final float со弟 = 2.5F;
   private static final float соб = 10.0F;
   private static final float соЫ = 13.0F;
   private static final float соl = 2.5F;
   private static final float со8 = 36.0F;
   private static final float со5 = 39.0F;
   private static final float сов = 5.0F;
   private static final float соъ = 15.0F;
   private static final float соМ = -8.0F;
   private static final float соН = 15.0F;
   private static final float сон = 15.0F;
   private static final float соЦ = -8.0F;
   private static final float соч = 0.01F;
   private static final float соЭ = 255.0F;
   private static final float со必 = 0.01F;
   private static final float сощ = 255.0F;
   private static final float соГ = 16.0F;
   private static final float сол = 30.0F;
   private static final float соэ = 14.0F;
   private static final float сою = 14.0F;
   private static final String соЩ = "effect.minecraft.";
   private static final String соу = "effect.";
   private static final String со2 = "minecraft";
   private static final float соф = 37.0F;
   private static final float сои = 12.0F;
   private static final float со9 = 13.0F;
   private static final float соо = 13.0F;
   private static final float соO = 20.0F;
   private static final float со7 = 24.0F;
   private static final float соР = 13.5F;
   private static final int со3 = 72000;
   private static final float сор = 255.0F;
   private static final float со6 = 12.0F;
   private static final float со_ = 11.5F;
   private static final float соШ = 18.0F;
   private static final float соы = 18.0F;
   private static final double сой = 255.0;
   private static final float соЖ = 12.0F;
   private static final float сом = 1.7F;
   private static final float соК = 0.2F;
   private static final float соI = 12.0F;
   private static final float соТ = 1.7F;
   private static final float соО = 13.0F;
   private static final float соФ = 13.0F;
   private static final float соЙ = 13.0F;
   private static final float сок = 37.0F;
   private static final float сог = 20.0F;
   private static final float со0 = 36.0F;
   private static final int сох = 72000;
   private static final String соУ = "**:**";
   private static final String соЗ = "%d:%02d";
   private static final long соЕ = 2000L;

   private float Ч(Potions.EffectEntry var1) {
      String var2 = var1.衣Ы() > 0 ? " " + (var1.衣Ы() + 1) : "";
      float var3 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var1.шз(), соО, 0.0F);
      float var4 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var2, соФ, 0.0F);
      float var5 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Ь(var1.衣().getDuration()), соЙ, 0.0F);
      return сок + var3 + var4 + сог + var5 + со0;
   }

   @Generated
   public HudSettingsPanel ш_10d41d08() {
      return this.Я;
   }

   private List<Potions.EffectEntry> 西_2571477d() {
      ArrayList var1 = new ArrayList();

      for (MobEffectInstance var3 : MINECRAFT_CLIENT.player.getActiveEffects()) {
         var1.add(new Potions.EffectEntry(var3, var3.getAmplifier()));
      }

      return var1;
   }

   public Potions(SingleChoiceSetting var1) {
      super(с9ю, с9Щ, с9у, с92, с9ф);
      this.П = new FloatAnimation(0.0F, с9и);
      this.西 = new FloatAnimation(0.0F, с99);
      this.д = new FloatAnimation(0.0F, с9о);
      this.Б = new FloatAnimation(0.0F, с9O);
      this.сЮ = new HashMap<>();
      this.сЧ = new LinkedHashMap<>();
      this.сЛ = new HashMap<>();
      this.са = new ArrayList<>();
      this.А_at_d53d68ac = new Random();
      this.ВЦ = this::method__;
      this.й = var1;
      this.Я = new HudSettingsPanel(с97, с9Р, () -> var1);
   }

   private void ГС() {
      long var1 = System.currentTimeMillis();
      if (this.са.isEmpty() || var1 - this.Ву >= соЕ) {
         ArrayList var3 = new ArrayList<>(
            List.of(
               MobEffects.SPEED,
               MobEffects.STRENGTH,
               MobEffects.REGENERATION,
               MobEffects.FIRE_RESISTANCE,
               MobEffects.RESISTANCE,
               MobEffects.NIGHT_VISION,
               MobEffects.JUMP_BOOST,
               MobEffects.HASTE
            )
         );
         Collections.shuffle(var3, this.А_at_d53d68ac);
         this.са.clear();
         int var4 = 2 + this.А_at_d53d68ac.nextInt(2);

         for (int var5 = 0; var5 < var4; var5++) {
            int var6 = 1200 + this.А_at_d53d68ac.nextInt(3600);
            int var7 = this.А_at_d53d68ac.nextInt(3);
            MobEffectInstance var8 = new MobEffectInstance((Holder<MobEffect>)var3.get(var5), var6, var7);
            this.са.add(new Potions.EffectEntry(var8, var7));
         }

         this.Ву = var1;
      }
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (var3 == 1 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
         this.Я.method_2(var1, var2);
         return true;
      } else {
         return this.Я.ю(var1, var2, var3);
      }
   }

   private String Ь(int var1) {
      if (var1 > сох) {
         return соУ;
      }

      int var2 = var1 / 20;
      return String.format(соЗ, var2 / 60, var2 % 60);
   }

    private void Г(float var1, float var2, float var3, float var4) {
       HudBackground.drawBatched(var1, var2, var3, var4, сою);
   }

   private void Ч(Potions.EffectEntry var1, float var2, float var3, float var4, float var5, float var6) {
      String var7 = var1.шз();
      String var8 = var1.衣Ы() > 0 ? " " + (var1.衣Ы() + 1) : "";
      String var9 = this.Ь(var1.衣().getDuration());
      String var10 = var1.衣().getDescriptionId().replace(соЩ, "").replace(соу, "");
      Identifier var11 = Identifier.fromNamespaceAndPath(со2, "textures/mob_effect/" + var10 + ".png");
      float var12 = var2 + соф;
      float var13 = var3 + сои;
      float var14 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var7, со9, 0.0F);
      float var15 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var8, соо, 0.0F);
      float var16 = var12 + var14 + var15 + соO;
      float var17 = var2 + var4 - со7;
      float var18 = var3 + соР;
      int var19 = var1.衣().getEffect().value().getColor();
      int var20 = Math.max(1, this.сЛ.getOrDefault(var1, var1.衣().getDuration()));
      float var21 = var1.衣().getDuration() > со3 ? 1.0F : Math.clamp((float)var1.衣().getDuration() / var20, 0.0F, 1.0F);
      UiDrawing.setGlobalAlpha(сор * this.Б.getValue() * var6);
      UiBatchDrawing.drawTexture(var2 + со6, var3 + со_, соШ, соы, new Vector4f(0.0F), new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), new Vector4i(-1), var11);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var7, var12, var13, 13, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), var6));
      if (!var8.isEmpty()) {
         UiBatchDrawing.drawText(
            ClientFonts.getMediumFont(), var8, var12 + var14, var13, 13, ColorMath.withAlpha(ColorMath.packArgb(108, 108, 108, сой), var6)
         );
      }

      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var9, var16, var13, 13, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), var6));
      UiBatchDrawing.drawCircularProgress(var17, var18, соЖ, сом, 1.0F, ColorMath.withAlpha(var19, соК * var6));
      UiBatchDrawing.drawCircularProgress(var17, var18, соI, соТ, var21, ColorMath.withAlpha(var19, var6));
   }

   @Override
   public void иВ() {
      this.Я.сУ();
   }

   public void method__(HudRenderEvent var1) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(с93)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else if (this.й.isSelected(с9р)) {
         this.西(var1);
      } else {
         List<Potions.EffectEntry> var2 = this.西_2571477d();
         boolean var3 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen || !var2.isEmpty();
         this.д.updateTarget(var3 ? 1.0F : 0.0F);
         if (!var3 && this.д.getValue() <= с96) {
            this.сЮ.clear();
            this.setWidth(0.0F);
            this.setHeight(0.0F);
         } else {
            float var4 = this.getX();
            float var5 = this.getY();
            float var6 = 0.0F;
            float var7 = 0.0F;
            float var8 = 0.0F;

            for (Potions.EffectEntry var10 : var2) {
               int var11 = var10.衣().getDuration();
               int var12 = var11 / 20;
               int var13 = var12 / 60;
               int var14 = var12 % 60;
               String var15 = var11 > с9_ ? с9Ш : String.format(с9ы, var13, var14);
               var6 = Math.max(var6, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.шз(), с9й, 0.0F));
               var7 = Math.max(var7, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.衣Ы() > 0 ? " " + (var10.衣Ы() + 1) : "", с9Ж, 0.0F));
               var8 = Math.max(var7, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var15, с9м, 0.0F));
            }

            float var31 = Math.max(с9К, var6 + var7 + var8 + с9I);
            this.П.updateTarget(var31);
            UiDrawing.setGlobalAlpha(с9Т * this.д.getValue());
            this.pushGuiScaleTransform();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               UiBatchDrawing.drawHudWindowBackground(var4, var5, this.П.getValue(), this.西.getValue(), с9О);
               float var32 = Math.clamp((this.西.getValue() - с9Ф) / с9Й, 0.0F, 1.0F);
               if (var32 > с9к) {
                  int var33 = ColorMath.withAlpha(
                     ColorMath.blendArgb(
                        ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), с9г + с90 * (float)Math.sin(System.currentTimeMillis() / с9х)
                     ),
                     с9У * var32
                  );
                  int var35 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), 0.0F);
                  UiBatchDrawing.drawRoundedRect(var4 + с9З, var5 + с9Е, this.П.getValue() - с9П, с9西, new Vector4f(0.0F), new Vector4i(var35, var33, var33, var35));
               }

               UiBatchDrawing.drawText(ClientFonts.getIconsFont(), с9д, var4 + с9Б, var5 + с9С, 16, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
               UiBatchDrawing.drawText(ClientFonts.getMediumFont(), с9з, var4 + с9ц, var5 + с9я, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
               ArrayList var34 = new ArrayList();

               for (int var36 = 0; var36 < var2.size(); var36++) {
                  Potions.EffectEntry var39 = (Potions.EffectEntry)var2.get(var36);
                  FloatAnimation[] var42 = this.сЮ.remove(var39);
                  if (var42 == null) {
                     var42 = new FloatAnimation[]{new FloatAnimation(0.0F, сос), new FloatAnimation(соВ, соЯ), new FloatAnimation(var36 * соХ, сое)};
                  }

                  this.сЮ.put(var39, var42);
                  var42[0].updateTarget(1.0F);
                  var42[1].updateTarget(0.0F);
                  var42[2].updateTarget(var36 * соА);
               }

               for (Entry var40 : this.сЮ.entrySet()) {
                  if (!var2.contains(var40.getKey())) {
                     FloatAnimation[] var43 = (FloatAnimation[])var40.getValue();
                     var43[0].updateTarget(0.0F);
                     var43[1].updateTarget(со4);
                     if (var43[0].isFinished() && var43[1].isFinished()) {
                        var34.add((Potions.EffectEntry)var40.getKey());
                     }
                  }
               }

               var34.forEach(this.сЮ::remove);

               for (Entry var41 : this.сЮ.entrySet()) {
                  Potions.EffectEntry var44 = (Potions.EffectEntry)var41.getKey();
                  FloatAnimation[] var45 = (FloatAnimation[])var41.getValue();
                  float var16 = var45[0].getValue();
                  if (!(var16 <= 0.0F) || !var45[0].isFinished()) {
                     float var17 = var5 + соп + var45[2].getValue();
                     String var18 = var44.шз();
                     String var19 = var44.衣Ы() + "";
                     float var20 = var45[1].getValue();
                     int var21 = var44.衣().getDuration();
                     int var22 = var21 / 20;
                     int var23 = var22 / 60;
                     int var24 = var22 % 60;
                     String var25 = var21 > соД ? соЪ : String.format(сот, var23, var24);
                     String var26 = var44.衣().getDescriptionId().replace(соа, "").replace(соЮ, "");
                     Identifier var27 = Identifier.fromNamespaceAndPath(соЧ, "textures/mob_effect/" + var26 + ".png");
                     UiDrawing.setGlobalAlpha(var16 * соЛ);
                     UiBatchDrawing.drawTexture(
                        var4 + со诶, var17 + со1 + var20, соЬ, со衣, new Vector4f(0.0F), new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), new Vector4i(-1), var27
                     );
                     UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var18, var4 + сош, var17 + соИ + var20, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(),
                        var44.衣Ы() > 0 ? " " + (var44.衣Ы() + 1) : "",
                        var4 + соь + ClientFonts.getMediumFont().getFontFace().measureTextWidth(var18, сож, 0.0F),
                        var17 + со弟 + var20,
                        13,
                        ThemeEditor.getColorArgb(ThemeColorSlot.MAIN)
                     );
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(),
                        var25,
                        var4 + this.П.getValue() - соб - ClientFonts.getMediumFont().getFontFace().measureTextWidth(var25, соЫ, 0.0F),
                        var17 + соl + var20,
                        13,
                        ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
                     );
                     UiDrawing.resetGlobalAlpha();
                  }
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
               UiDrawing.resetGlobalAlpha();
               this.popGuiScaleTransform();
            }

            this.西.updateTarget(var2.isEmpty() ? со8 : 36 + var2.size() * 20 + 7);
            this.setWidth(this.П.getValue());
            this.setHeight(this.西.getValue());
         }
      }
   }

   @Override
   public void Б5() {
      this.Я.сС();
   }

   private void 西(HudRenderEvent var1) {
      List<Potions.EffectEntry> var2 = this.西_2571477d();
      boolean var3 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen && var2.isEmpty();
      if (var3) {
         this.ГС();
         var2 = new ArrayList<>(this.са);
      }

      boolean var4 = var3 || !var2.isEmpty();
      this.Б.updateTarget(var4 ? 1.0F : 0.0F);
      float var5 = this.getX();
      float var6 = this.getY();
      float var7 = со5;
      float var8 = сов;
      float var9 = 0.0F;
      ArrayList<Potions.EffectEntry> var10 = new ArrayList<>();

      for (int var11 = 0; var11 < var2.size(); var11++) {
         Potions.EffectEntry var12 = (Potions.EffectEntry)var2.get(var11);
         FloatAnimation[] var13 = this.сЧ.remove(var12);
         if (var13 == null) {
            var13 = new FloatAnimation[]{new FloatAnimation(0.0F, соъ), new FloatAnimation(соМ, соН), new FloatAnimation(var11 * (var7 + var8), сон)};
         }

         this.сЧ.put(var12, var13);
         var13[0].updateTarget(1.0F);
         var13[1].updateTarget(0.0F);
         var13[2].updateTarget(var11 * (var7 + var8));
         this.сЛ.merge(var12, var12.衣().getDuration(), Math::max);
         var9 = Math.max(var9, this.Ч(var12));
      }

      for (Entry var27 : this.сЧ.entrySet()) {
         if (!var2.contains(var27.getKey())) {
            FloatAnimation[] var30 = (FloatAnimation[])var27.getValue();
            var30[0].updateTarget(0.0F);
            var30[1].updateTarget(соЦ);
            if (var30[0].isFinished() && var30[1].isFinished()) {
               var10.add((Potions.EffectEntry)var27.getKey());
            }
         }

         var9 = Math.max(var9, this.Ч((Potions.EffectEntry)var27.getKey()));
      }

      for (Potions.EffectEntry var28 : var10) {
         this.сЧ.remove(var28);
         this.сЛ.remove(var28);
      }

      float var26 = this.сЧ.isEmpty() ? (var3 ? var7 : 0.0F) : this.сЧ.size() * var7 + Math.max(0, this.сЧ.size() - 1) * var8;
      float var29 = this.Б.getValue();
      if (var29 > соч) {
         UiDrawing.setGlobalAlpha(соЭ * var29);
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            for (Entry var14 : this.сЧ.entrySet()) {
               Potions.EffectEntry var15 = (Potions.EffectEntry)var14.getKey();
               FloatAnimation[] var16 = (FloatAnimation[])var14.getValue();
               float var17 = var16[0].getValue();
               if (!(var17 <= со必) || !var16[0].isFinished()) {
                  float var18 = var5 + var16[1].getValue();
                  float var19 = var6 + var16[2].getValue();
                  float var20 = this.Ч(var15);
                  UiDrawing.setGlobalAlpha(сощ * var29 * var17);
                  this.Г(var18, var19, var20, var7);
                  this.Ч(var15, var18, var19, var20, var7, var17);
               }
            }
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
            UiDrawing.resetGlobalAlpha();
            this.popGuiScaleTransform();
         }
      }

      this.setWidth(var4 ? var9 : 0.0F);
      this.setHeight(var4 ? var26 : 0.0F);
   }

   private static class EffectEntry {
      private final MobEffectInstance В;
      private final int пс;

      @Override
      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return !(var1 instanceof Potions.EffectEntry var2) ? false : this.пс == var2.пс && this.В.getEffect().equals(var2.В.getEffect());
         }
      }

      public EffectEntry(MobEffectInstance var1, int var2) {
         this.В = var1;
         this.пс = var2;
      }

      public String шз() {
         return I18n.get(this.В.getDescriptionId());
      }

      @Generated
      public int 衣Ы() {
         return this.пс;
      }

      @Generated
      public MobEffectInstance 衣() {
         return this.В;
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.В.getEffect(), this.пс);
      }
   }
}
