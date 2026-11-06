package sg.modules.impl.render;

import com.mojang.blaze3d.textures.FilterMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.AntiBot;
import sg.modules.impl.combat.Aura;
import sg.events.impl.AttackEntityEvent;
import sg.events.impl.HudRenderEvent;
import sg.hud.KillCardsHudElement;

public class KillCards extends ClientModule {
   private static final Identifier field_9 = ClientResourceIds.getImageId(KillCards.ЯМе);
   private static final Identifier о = ClientResourceIds.getImageId(KillCards.ЯМА);
   private static final Identifier O = ClientResourceIds.getImageId(KillCards.ЯМ4);
   private static final Vector4f field_4 = new Vector4f(0.0F, 0.0F, 1.0F, 1.0F);
   private static final int Хз = 5;
   private static final long Въ = 300000L;
   private static final long ВМ = 3500L;
   private static final float АВ = 176.0F;
   private static final float АЯ = 48.0F;
   private static final float АХ = 32.0F;
   private static final float Ае = 50.0F;
   private final BooleanSetting с弟;
   private final BooleanSetting сб;
   private final List<KillCards.TrackedVictim> сХ;
   private final List<KillCards.CardAnimation> се;
   private final FloatAnimation У;
   private final EventListener<AttackEntityEvent> ВД;
   private final EventListener<PacketReceiveEvent> ВЪ;
   private final EventListener<PlayerTickEvent> Вт;
   private final EventListener<HudRenderEvent> Ва;
   private static final String Явл = "KillCards";
   private static final String Явэ = "Карточки последних убийств в стиле CS2";
   private static final String Явю = "Работать на мобов";
   private static final String ЯвЩ = "Свечение";
   private static final float Яву = 12.0F;
   private static final long Яв2 = 300L;
   private static final float Явф = 0.26F;
   private static final float Яви = 640.0F;
   private static final float Яв9 = 420.0F;
   private static final float Яво = 18.0F;
   private static final float ЯвO = 176.0F;
   private static final float Яв7 = 0.5F;
   private static final float ЯвР = 52.0F;
   private static final float Яв3 = 0.01F;
   private static final float Явр = 0.01F;
   private static final float Яв6 = 255.0F;
   private static final float Яв_ = 48.0F;
   private static final float ЯвШ = 0.5F;
   private static final float Явы = 176.0F;
   private static final float Явй = 8.0F;
   private static final float ЯвЖ = 0.5F;
   private static final float Явм = 0.5F;
   private static final float ЯвК = 50.0F;
   private static final float ЯвI = 10.0F;
   private static final float ЯвТ = 0.02F;
   private static final float ЯвО = 0.55F;
   private static final float ЯвФ = 0.5F;
   private static final float ЯвЙ = 48.0F;
   private static final float Явк = 14.0F;
   private static final float Явг = 0.5F;
   private static final float Яв0 = 0.52F;
   private static final float Явх = 0.5F;
   private static final float ЯвУ = 14.0F;
   private static final float ЯвЗ = 5.0F;
   private static final float ЯвЕ = 64.0F;
   private static final float ЯвП = 18.0F;
   private static final float Яв西 = 0.5F;
   private static final float Явд = 0.72F;
   private static final float ЯвБ = 0.45F;
   private static final float ЯвС = 0.58F;
   private static final float Явз = 1.12F;
   private static final float Явц = 0.22F;
   private static final float Явя = 0.5F;
   private static final float Яъс = 0.5F;
   private static final float ЯъВ = 0.38F;
   private static final float ЯъЯ = 0.5F;
   private static final float ЯъХ = 0.55F;
   private static final float Яъе = 1.15F;
   private static final float ЯъА = 1.45F;
   private static final float Яъ4 = 0.5F;
   private static final float Яъп = 0.82F;
   private static final float ЯъД = 0.02F;
   private static final float ЯъЪ = 86.0F;
   private static final float Яът = 28.0F;
   private static final float Яъа = 0.5F;
   private static final float ЯъЮ = 0.5F;
   private static final float ЯъЧ = 1.2F;
   private static final float ЯъЛ = 3.8F;
   private static final float Яъ诶 = 1.6F;
   private static final float Яъ1 = 0.42F;
   private static final float ЯъЬ = 32.0F;
   private static final float Яъ衣 = 50.0F;
   private static final float Яъш = 10.0F;
   private static final long ЯъИ = 300000L;
   private static final float Яъь = 0.86F;
   private static final float Яъж = 0.14F;
   private static final float Яъ弟 = 1.06F;
   private static final float Яъб = 0.5F;
   private static final float ЯъЫ = 2.2F;
   private static final float Яъl = 2.6F;
   private static final float Яъ8 = 16.0F;
   private static final float Яъ5 = 0.5F;
   private static final float Яъв = 0.4F;
   private static final float Яъъ = 0.01F;
   private static final float ЯъМ = 3.4F;
   private static final float ЯъН = 205.0F;
   private static final float Яън = 1.15F;
   private static final float ЯъЦ = 0.7F;
   private static final float Яъч = 0.4F;
   private static final float ЯъЭ = 3.4F;
   private static final float Яъ必 = 0.84F;
   private static final float Яъщ = 0.16F;
   private static final float ЯъГ = 0.05F;
   private static final float Яъл = 1.4F;
   private static final float Яъэ = 1.4F;
   private static final float Яъю = 2.8F;
   private static final float ЯъЩ = 2.8F;
   private static final float Яъу = 2.4F;
   private static final float Яъ2 = 0.12F;
   private static final float Яъф = 7.2F;
   private static final float Яъи = 0.92F;
   private static final float Яъ9 = 2.6F;
   private static final float Яъо = 2.8F;
   private static final float ЯъO = 2.6F;
   private static final float Яъ7 = 2.8F;
   private static final float ЯъР = 18.5F;
   private static final float Яъ3 = 0.5F;
   private static final float Яър = 0.5F;
   private static final float Яъ6 = 0.4F;
   private static final float Яъ_ = 0.7F;
   private static final float ЯъШ = 0.7F;
   private static final float Яъы = 140.0F;
   private static final float Яъй = 0.5F;
   private static final float ЯъЖ = 236.0F;
   private static final float Яъм = 0.58F;
   private static final float ЯъК = 0.5F;
   private static final float ЯъI = 0.02F;
   private static final float ЯъТ = 0.5F;
   private static final float ЯъО = 0.03F;
   private static final float ЯъФ = 0.18F;
   private static final float ЯъЙ = 1.15F;
   private static final float Яък = 0.04F;
   private static final float Яъг = 0.5F;
   private static final float Яъ0 = 1.5F;
   private static final float Яъх = 1.5F;
   private static final float ЯъУ = 3.0F;
   private static final float ЯъЗ = 3.0F;
   private static final float ЯъЕ = 0.95F;
   private static final float ЯъП = 3.0F;
   private static final float Яъ西 = 0.5F;
   private static final float Яъд = 0.16F;
   private static final long ЯъБ = 480L;
   private static final long ЯъС = 50L;
   private static final float Яъз = 50.0F;
   private static final long Яъц = 50L;
   private static final float Яъя = 430.0F;
   private static final int ЯМс = -16777216;
   private static final long ЯМВ = 300000L;
   private static final float ЯМЯ = 0.02F;
   private static final String ЯМе = "hud/killcards/skull";
   private static final String ЯМА = "hud/killcards/glow";
   private static final String ЯМ4 = "hud/killcards/beam";

   private float method_9Й() {
      if (this.се.isEmpty()) {
         return 0.0F;
      }

      long var1 = System.currentTimeMillis() - this.се.get(this.се.size() - 1).ш;
      if (var1 < 0L || var1 > ЯъБ) {
         return 0.0F;
      }

      if (var1 < ЯъС) {
         return (float)var1 / Яъз;
      }

      float var3 = 1.0F - (float)(var1 - Яъц) / Яъя;
      return var3 * var3;
   }

   private void method_9(float var1, float var2, float var3, float var4, float var5, int var6, boolean var7, float var8) {
      int var9 = this.Х衣();
      boolean var10 = this.сб.getValue();
      float var11 = var7 ? 0.0F : this.method_9Й();
      float var12 = Яв_ * var4;
      float var13 = var1 + (var3 - var12) * ЯвШ;
      float var14 = var2 + Явы * var4 - var12 - Явй * var4;
      float var15 = var13 + var12 * ЯвЖ;
      float var16 = var14 + var12 * Явм;
      float var17 = ЯвК * var4;
      float var18 = var14 - var17 + ЯвI * var4;
      if (var10 && var11 > ЯвТ) {
         this.method_9(var15, var18, var17, var16, var12, var4, var9, var8, var11);
      }

      this.method_9(var1, var3, var5, var16, var4, var9, var8, var10, var11);
      this.method_9(var15, var14, var4, var6, var7, var9, var8, var10, var11);
      this.method_9(var13, var14, var12, var9, var8);
   }

   private void ос() {
      long var1 = System.currentTimeMillis();
      this.се.removeIf(var2 -> {
         if (var1 - var2.ш < ЯМВ) {
            return false;
         }

         var2.Я.updateTarget(0.0F);
         return var2.Я.getValue() < ЯМЯ;
      });

      while (this.се.size() > 5) {
         this.се.remove(0);
      }
   }

   private boolean 必(LivingEntity var1) {
      KillCards.TrackedVictim var2 = this.method_9(var1);
      return var2 != null && var2.method_7й() && var2.се;
   }

   private void я(PacketReceiveEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundEntityEventPacket var2) {
            if (var2.getEntity(MINECRAFT_CLIENT.level) instanceof LivingEntity var4 && this.method_3(var4)) {
               if (this.必(var4)) {
                  if (var2.getEventId() == 3) {
                     MINECRAFT_CLIENT.execute(() -> this.я(var4));
                  }
               }
            }
         }
      }
   }

   private void ХХ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         try {
            Aura var2 = RoseClient.getInstance().getServices().getModules().getAura();
            if (var2 != null && var2.isEnabled() && var2.Ж_95bedd6e() instanceof LivingEntity var3 && var3.isAlive()) {
               this.о(var3, true);
            }
         } catch (Throwable var6) {
         }

         Iterator var7 = this.сХ.iterator();

         while (var7.hasNext()) {
            KillCards.TrackedVictim var8 = (KillCards.TrackedVictim)var7.next();
            if (!var8.method_7й()) {
               var7.remove();
            } else {
               LivingEntity var9 = var8.Х;
               boolean var5 = MINECRAFT_CLIENT.level.getEntity(var9.getId()) == null;
               if ((var9.getHealth() <= 0.0F || !var9.isAlive() || var5) && var8.се && !var8.сА) {
                  this.я(var9);
                  var8.к(Яв2);
               }
            }
         }

         this.сХ.removeIf(var0 -> !var0.method_7й());
         this.ос();
      } else {
         this.сХ.clear();
         this.се.clear();
      }
   }

   private KillCards.TrackedVictim method_9(LivingEntity var1) {
      if (var1 == null) {
         return null;
      }

      for (KillCards.TrackedVictim var3 : this.сХ) {
         if (var3.Х.getId() == var1.getId()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void onDisable() {
      this.сХ.clear();
      this.о6();
   }

   private void method_9(float var1, float var2, float var3, float var4, float var5, int var6, float var7, boolean var8, float var9) {
      float var10 = var1 + (var2 - var3) * ЯъЯ;
      float var11 = var4 - ЯъХ * var5;
      float var12 = Math.max(Яъе, ЯъА * var5);
      float var13 = var3 * Яъ4;
      int var14 = ColorMath.withAlpha(var6, 0.0F);
      int var15 = ColorMath.withAlpha(var6, (var8 ? 1.0F : Яъп) * var7);
      UiDrawing.drawRoundedRect(var10, var11, var13, var12, 0.0F, new Vector4i(var14, var14, var15, var15));
      UiDrawing.drawRoundedRect(var10 + var13, var11, var13, var12, 0.0F, new Vector4i(var15, var15, var14, var14));
      if (var8 && var9 > ЯъД) {
         float var16 = (ЯъЪ + Яът * var9) * var5;
         float var17 = var1 + var2 * Яъа;
         UiDrawing.drawRoundedRect(var17 - var16 * ЯъЮ, var11 - ЯъЧ * var5, var16, ЯъЛ * var5, Яъ诶 * var5, ColorMath.withAlpha(var6, Яъ1 * var9 * var7));
      }
   }

   private void о(LivingEntity var1, boolean var2) {
      if (this.method_3(var1)) {
         for (KillCards.TrackedVictim var4 : this.сХ) {
            if (var4.Х.getId() == var1.getId()) {
               var4.ш(var1);
               if (var2) {
                  var4.шЫ();
               }

               return;
            }
         }

         if (var2) {
            KillCards.TrackedVictim var5 = new KillCards.TrackedVictim(var1);
            var5.шЫ();
            this.сХ.add(var5);
         }
      }
   }

   private boolean В(LivingEntity var1) {
      if (var1 instanceof Player var2) {
         try {
            AntiBot var3 = RoseClient.getInstance().getServices().getModules().getAntiBot();
            return var3 != null && var3.isEnabled() && var3.detectedBots.contains(var2);
         } catch (Throwable var4) {
            return false;
         }
      } else {
         return false;
      }
   }

   private void method_9(float var1, float var2, float var3, int var4, boolean var5, int var6, float var7, boolean var8, float var9) {
      if (var4 > 0) {
         float var10 = ЯъЬ * var3;
         float var11 = Яъ衣 * var3;
         float var12 = var2 - var11 + Яъш * var3;

         for (int var13 = 0; var13 < var4; var13++) {
            float var14 = 1.0F;
            float var15 = var7;
            if (!var5 && var13 < this.се.size()) {
               KillCards.CardAnimation var16 = this.се.get(var13);
               var16.В.updateTarget(1.0F);
               boolean var17 = System.currentTimeMillis() - var16.ш >= ЯъИ;
               var16.Я.updateTarget(var17 ? 0.0F : 1.0F);
               var14 = var16.В.getValue();
               var15 *= var16.Я.getValue();
            }

            boolean var27 = var13 == var4 - 1;
            float var28 = Math.max(0.0F, var14);
            float var18 = Яъь + Яъж * Math.min(var28, Яъ弟);
            int var19 = var4 - 1 - var13;
            float var20 = var4 == 1 ? 0.0F : ((float)var13 / (var4 - 1) - Яъб) * 2.0F;
            float var21 = var10 * var18;
            float var22 = var11 * var18;
            float var23 = var20 * ЯъЫ * var3;
            float var24 = -var19 * Яъl * var3 + (1.0F - Math.min(var28, 1.0F)) * Яъ8 * var3;
            float var25 = var1 - var21 * Яъ5 + var23;
            float var26 = var12 + (var11 - var22) + var24;
            this.method_9(var25, var26, var21, var22, var13 + 1, var27, var6, var15 * Math.min(1.0F, Яъв + var28), var8, var3, var27 ? var9 : 0.0F);
         }
      }
   }

   public KillCards() {
      super(Явл, Явэ, ModuleCategory.RENDER);
      this.с弟 = new BooleanSetting(Явю, false);
      this.сб = new BooleanSetting(ЯвЩ, true);
      this.сХ = new ArrayList<>();
      this.се = new ArrayList<>();
      this.У = new FloatAnimation(0.0F, Яву, Easings.CUBIC_OUT);
      this.ВД = this::Б;
      this.ВЪ = this::я;
      this.Вт = this::ХХ;
      this.Ва = this::Д;
      this.addSettings(this.с弟, this.сб);
   }

   private void Д(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getWindow() != null) {
         this.ос();
         float var2 = HudScaleCoordinates.getHudScale();
         float var3 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float var4 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         float var5 = Math.min(var3 * Явф, Яви * var2);
         var5 = Math.max(var5, Яв9 * var2);
         float var6 = var5 + Яво * var2;
         float var7 = ЯвO * var2;
         KillCardsHudElement var8 = this.method_9();
         boolean var9 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen;
         boolean var10 = var9 && this.се.isEmpty();
         boolean var11 = !this.се.isEmpty() || var10;
         this.У.updateTarget(var11 ? 1.0F : 0.0F);
         float var12 = this.У.getValue();
         float var13 = (var3 - var6) * Яв7;
         float var14 = var4 - var7 - ЯвР * var2;
         float var15 = var8 != null && var8.чБ() ? var8.getX() : var13;
         float var16 = var8 != null && var8.чБ() ? var8.getY() : var14;
         if (var8 != null) {
            if (var12 > Яв3) {
               var8.А(var13, var14, var6, var7);
               var15 = var8.getX();
               var16 = var8.getY();
            } else {
               var8.setWidth(0.0F);
               var8.setHeight(0.0F);
            }
         }

         if (!(var12 < Явр)) {
            UiDrawing.setTransform(new Matrix4f());
            UiDrawing.setGlobalAlpha(Яв6 * var12);

            try {
               int var17 = var10 ? 2 : this.се.size();
               this.method_9(var15, var16, var6, var2, var5, var17, var10, var12);
            } finally {
               UiDrawing.resetGlobalAlpha();
               UiDrawing.setTransform(new Matrix4f());
            }
         }
      } else {
         this.о6();
      }
   }

   private boolean method_3(LivingEntity var1) {
      if (var1 == null || var1 == MINECRAFT_CLIENT.player || var1 instanceof ArmorStand) {
         return false;
      } else if (this.В(var1)) {
         return false;
      } else {
         return var1 instanceof Player ? true : this.с弟.getValue();
      }
   }

   private void Б(AttackEntityEvent var1) {
      if (this.isEnabled() && var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         if (var1.getTarget() instanceof LivingEntity var2 && var2.isAlive()) {
            this.о(var2, true);
         }
      }
   }

   private void method_9(float var1, float var2, float var3, int var4, float var5) {
      UiDrawing.drawRoundedRect(var1, var2, var3, var3, var3 * Яъй, ColorMath.packArgb(10, 12, 16, (int)(ЯъЖ * var5)));
      float var6 = var3 * Яъм;
      float var7 = var1 + (var3 - var6) * ЯъК - var3 * ЯъI;
      float var8 = var2 + (var3 - var6) * ЯъТ - var3 * ЯъО;
       sg.render.ClientLogo.drawInBox(var7, var8, var6, var6, ColorMath.withAlpha(var4, var5));
      int var9 = ColorMath.lerpArgb(var4, ColorMath.packRgb(255, 255, 255), ЯъФ);
      UiDrawing.drawOutline(var1, var2, var3, var3, Math.max(ЯъЙ, var3 * Яък), var3 * Яъг, ColorMath.withAlpha(var9, var5));
      UiDrawing.drawOutline(
         var1 + Яъ0, var2 + Яъх, var3 - ЯъУ, var3 - ЯъЗ, ЯъЕ, (var3 - ЯъП) * Яъ西, ColorMath.withAlpha(ColorMath.packRgb(255, 255, 255), Яъд * var5)
      );
   }

   private void method_9(float var1, float var2, float var3, float var4, float var5, float var6, int var7, float var8, float var9) {
      float var10 = var8 * var9;
      int var11 = ColorMath.lerpArgb(var7, ColorMath.packRgb(255, 255, 255), ЯвО);
      float var12 = var2 + var3 * ЯвФ;
      float var13 = (ЯвЙ + Явк * var9) * var6;
      UiDrawing.drawTexture(var1 - var13 * Явг, var12 - var13 * Яв0, var13, var13, 0.0F, field_4, ColorMath.withAlpha(var7, Явх * var10), о, FilterMode.LINEAR);
      float var14 = (ЯвУ + ЯвЗ * var9) * var6;
      float var15 = (ЯвЕ + ЯвП * var9) * var6;
      UiDrawing.drawTexture(
         var1 - var14 * Яв西, var12 - var15 * Явд, var14, var15, var14 * ЯвБ, field_4, ColorMath.withAlpha(var11, ЯвС * var10), O, FilterMode.LINEAR
      );
      float var16 = var5 * (Явз + Явц * var9);
      UiDrawing.drawTexture(var1 - var16 * Явя, var4 - var16 * Яъс, var16, var16, 0.0F, field_4, ColorMath.withAlpha(var7, ЯъВ * var10), о, FilterMode.LINEAR);
   }

   private KillCardsHudElement method_9() {
      try {
         return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null
            ? RoseClient.getInstance().getServices().getHudElements().getKillCards()
            : null;
      } catch (Throwable var2) {
         return null;
      }
   }

   private void о6() {
      KillCardsHudElement var1 = this.method_9();
      if (var1 != null) {
         var1.setWidth(0.0F);
         var1.setHeight(0.0F);
      }
   }

   private void я(LivingEntity var1) {
      if (this.method_3(var1)) {
         KillCards.TrackedVictim var2 = this.method_9(var1);
         if (var2 != null && var2.method_7й() && var2.се && !var2.сА) {
            var2.сА = true;
            KillCards.CardAnimation var3 = new KillCards.CardAnimation();
            var3.В.setImmediately(0.0F);
            this.се.add(var3);

            while (this.се.size() > 5) {
               this.се.remove(0);
            }
         }
      }
   }

   private void method_9(float var1, float var2, float var3, float var4, int var5, boolean var6, int var7, float var8, boolean var9, float var10, float var11) {
      if (!(var8 < Яъъ)) {
          sg.hud.HudBackground.draw(var1, var2, var3, var4, ЯъМ * var10);
         float var12 = Math.max(1.0F, (Яън + ЯъЦ * var11) * var10);
         int var13 = ColorMath.lerpArgb(var7, ColorMath.packRgb(255, 255, 255), Яъч * var11);
         UiDrawing.drawOutline(var1, var2, var3, var4, var12, ЯъЭ * var10, ColorMath.withAlpha(var13, (Яъ必 + Яъщ * var11) * var8));
         if (var9 && var11 > ЯъГ) {
            UiDrawing.drawRoundedRect(
               var1 + Яъл * var10, var2 + Яъэ * var10, var3 - Яъю * var10, var4 - ЯъЩ * var10, Яъу * var10, ColorMath.withAlpha(var7, Яъ2 * var11 * var8)
            );
         }

         float var14 = Яъф * var10;
         int var15 = ColorMath.withAlpha(var7, Яъи * var8);
         UiDrawing.drawTexture(var1 + Яъ9 * var10, var2 + Яъо * var10, var14, var14, 0.0F, field_4, var15, field_9, FilterMode.LINEAR);
         UiDrawing.drawTexture(
            var1 + var3 - var14 - ЯъO * var10, var2 + var4 - var14 - Яъ7 * var10, var14, var14, 0.0F, field_4, var15, field_9, FilterMode.LINEAR
         );
         if (var6) {
            String var16 = String.valueOf(var5);
            int var17 = Math.max(13, Math.round(ЯъР * var10));
            float var18 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var16, var17);
            float var19 = var1 + (var3 - var18) * Яъ3;
            float var20 = var2 + (var4 - var17) * Яър - Яъ6 * var10;
            UiDrawing.drawText(
               ClientFonts.getMediumFont(), var16, var19 + Яъ_ * var10, var20 + ЯъШ * var10, var17, ColorMath.packArgb(0, 0, 0, (int)(Яъы * var8))
            );
            UiDrawing.drawText(ClientFonts.getMediumFont(), var16, var19, var20, var17, ColorMath.withAlpha(var7, var8));
         }
      }
   }

   private int Х衣() {
      return ColorMath.getAnimatedThemeColor(0) | ЯМс;
   }

   private static final class CardAnimation {
      private final long ш = System.currentTimeMillis();
      private final FloatAnimation В;
      private final FloatAnimation Я;
      private static final float Я弟l = 6.0F;
      private static final float Я弟8 = 10.0F;

      private CardAnimation() {
         this.В = new FloatAnimation(0.0F, Я弟l, Easings.BACK_OUT);
         this.Я = new FloatAnimation(1.0F, Я弟8, Easings.CUBIC_IN);
      }
   }

   private static final class TrackedVictim {
      private LivingEntity Х;
      private long с4;
      private long сп;
      private boolean се;
      private boolean сА;
      private static final long Вдз = 3500L;
      private static final long Вдц = 3500L;

      private void к(long var1) {
         this.сп = var1;
         this.с4 = System.currentTimeMillis();
      }

      private void шЫ() {
         this.се = true;
         this.с4 = System.currentTimeMillis();
      }

      private TrackedVictim(LivingEntity var1) {
         this.сп = Вдз;
         this.ш(var1);
      }

      private boolean method_7й() {
         return this.Х != null && System.currentTimeMillis() - this.с4 < this.сп;
      }

      private void ш(LivingEntity var1) {
         this.Х = var1;
         this.с4 = System.currentTimeMillis();
         this.сп = Вдц;
         if (var1.getHealth() > 0.0F) {
            this.сА = false;
         }
      }
   }
}
