package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.EntityHealth;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.modules.impl.render.Hud;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.gui.hud.HudSettingsPanel;
import sg.hud.InteractiveHudElement;
import sg.settings.FloatSetting;
import sg.network.InventoryInteractions;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.util.time.Stopwatch;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class PvPAssist extends HudElement implements InteractiveHudElement {
   private static final Identifier Ф = ClientResourceIds.getImageId(PvPAssist.свя);
   private static final Identifier Й = ClientResourceIds.getImageId(PvPAssist.със);
   private static final float пШ = 16.0F;
   private static final float пы = 84.0F;
   private static final float пй = 16.0F;
   private static final float пЖ = 116.0F;
   private static final float пм = 18.0F;
   private static final float пК = 14.0F;
   private static final float пI = 15.0F;
   private static final float пТ = 18.0F;
   private static final float пО = 180.0F;
   private static final float пФ = 260.0F;
   private static final int field_4С = 3;
   private static final int field_4з = 5;
   private static final int field_4ц = ColorMath.packArgb(255, 96, 96, PvPAssist.съВ);
   private static final long ХЮ = 4000L;
   private static final float пЙ = 0.5F;
   private static final EquipmentSlot[] А_at_38334ec8 = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private final FloatSetting Яй;
   private final HudSettingsPanel Х;
   private final FloatAnimation с诶;
   private final FloatAnimation с1;
   private final FloatAnimation сЬ;
   private final Stopwatch в;
   private final Stopwatch ъ;
   private final Stopwatch М;
   private LivingEntity 诶;
   private String Вз;
   private float пк;
   private float пг;
   @SerializedName("4я")
   private int field_4я;
   private float п0;
   private float пх;
   private PvPAssist.Advice В;
   private PvPAssist.Advice Я;
   private final EventListener<HudRenderEvent> Яч;
   private final EventListener<PacketReceiveEvent> ЯЭ;
   private static final String свХ = "PvPAssist";
   private static final float све = 37.0F;
   private static final float свА = 187.0F;
   private static final float св4 = 100.0F;
   private static final float свп = 116.0F;
   private static final float свД = 9.0F;
   private static final float свЪ = 12.0F;
   private static final float свт = 6.0F;
   private static final float сва = -1.0F;
   private static final String свЮ = "PvPAssist";
   private static final String свЧ = "Размер";
   private static final String свЛ = "PvP помощник";
   private static final float св诶 = 0.01F;
   private static final float св1 = 180.0F;
   private static final float свЬ = 260.0F;
   private static final float св衣 = 132.0F;
   private static final float свш = 116.0F;
   private static final float свИ = 116.0F;
   private static final float свь = 0.05F;
   private static final float свж = 255.0F;
   private static final float св弟 = 18.0F;
   private static final float свб = 1000.0F;
   private static final float свЫ = 0.5F;
   private static final float свl = 0.5F;
   private static final String св8 = "Отхилься у тебя мало ХП";
   private static final long св5 = 4000L;
   private static final long свв = 4000L;
   private static final String свъ = "У тебя скоро сломается броня, Починись!";
   private static final String свМ = "Быстрее бей его, у него сейчас хрустнет броня!";
   private static final String свН = "Помогаю тебе доминировать в бою";
   private static final float свн = 58.0F;
   private static final float свЦ = 58.0F;
   private static final float свч = 84.0F;
   private static final float свЭ = 0.999F;
   private static final float св必 = 0.86F;
   private static final float свщ = 0.14F;
   private static final float свГ = 0.86F;
   private static final float свл = 0.14F;
   private static final float свэ = 0.01F;
   private static final float свю = 0.2F;
   private static final float свЩ = 255.0F;
   private static final float сву = 255.0F;
   private static final float св2 = 1.2F;
   private static final float свф = 0.8F;
   private static final float сви = 1.8F;
   private static final float св9 = 6.0F;
   private static final float сво = 0.07F;
   private static final String свO = "N";
   private static final float св7 = 16.0F;
   private static final float свР = 24.0F;
   private static final String св3 = "N";
   private static final float свр = 20.0F;
   private static final String св6 = "PvP помощник";
   private static final float св_ = 7.0F;
   private static final float свШ = 20.0F;
   private static final float свы = 42.0F;
   private static final float свй = 6.0F;
   private static final float свЖ = 58.0F;
   private static final float свм = 0.999F;
   private static final float свК = -16.0F;
   private static final float свI = 16.0F;
   private static final float свТ = 0.01F;
   private static final float свО = 18.0F;
   private static final float свФ = 44.0F;
   private static final float свЙ = 255.0F;
   private static final float свк = 18.0F;
   private static final float свг = 18.0F;
   private static final float св0 = 6.0F;
   private static final float свх = 2.5F;
   private static final float свУ = 0.12F;
   private static final String свЗ = " ";
   private static final float свЕ = 260.0F;
   private static final String свП = "N";
   private static final float св西 = 7.0F;
   private static final float свд = 15.0F;
   private static final float свБ = 14.0F;
   private static final float свС = -1.0F;
   private static final float свз = 100.0F;
   private static final String свц = "%.2fx";
   private static final String свя = "hud/pvpassist/win";
   private static final String със = "hud/pvpassist/lose";
   private static final double съВ = 255.0;

   private float П(List<String> var1) {
      float var2 = 0.0F;

      for (String var4 : var1) {
         var2 = Math.max(var2, this.method_8(var4));
      }

      return var2;
   }

   public void Г(HudRenderEvent var1) {
      if (this.П_de4e60d5().я().isSelected(свЛ) && MINECRAFT_CLIENT.player != null) {
         UiDrawing.setTransform(new Matrix4f());
         this.пх = this.Яй.getValue();
         this.зЗ();
         this.зШ();
         this.сЬ.updateTarget(1.0F);
         float var2 = this.с诶.getValue();
         if (!(var2 < св诶) && this.В != null) {
            List var3 = this.ы(this.В.text());
            float var4 = Math.clamp(Math.max(this.П(var3), this.ыХ()), св1 * this.пх, свЬ * this.пх);
            this.с1.updateTarget(св衣 * this.пх + var4);
            float var5 = this.с1.getValue();
            float var6 = свш * this.пх;
            float var7 = this.getX();
            float var8 = this.getY();
            float var9 = var7 + свИ * this.пх;
            float var10 = this.сЬ.getValue();
            float var11 = 1.0F + свь * (1.0F - var2);
            this.setWidth(var5);
            this.setHeight(var6);
            this.pushGuiScaleTransform();
            UiDrawing.setGlobalAlpha(свж * var2);
            WorldProjection.scaleAroundRect(var7, var8, var5, var6, var11);

            try {
                HudBackground.draw(var7, var8, var5, var6, св弟 * this.пх);
               this.уЛ(var10);
               this.I(var9, var4);
               this.щ(var9, var4, var10);
               this.西(var5, var6, var10);
            } finally {
               WorldProjection.unscaleAroundRect(var7, var8, var5, var6, var11);
               UiDrawing.resetGlobalAlpha();
               this.popGuiScaleTransform();
            }
         } else {
            this.зк();
         }
      } else {
         this.Х.сС();
         this.зк();
      }
   }

   private int ыз() {
      return Math.max(1, Math.round(свБ * this.пх));
   }

   private float ыХ() {
      int var1 = this.ыз();
      return UiDrawing.getTextWidth(ClientFonts.getIconsFont(), свП, var1) + св西 * this.пх + ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Вз, var1, 0.0F);
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (var3 == 1 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
         this.Х.method_2(var1, var2);
         return true;
      } else {
         return this.Х.ю(var1, var2, var3);
      }
   }

   private int ыЪ() {
      int var1 = InventoryInteractions.щ(Items.TOTEM_OF_UNDYING);
      ItemStack var2 = MINECRAFT_CLIENT.player.getOffhandItem();
      return var2.is(Items.TOTEM_OF_UNDYING) ? var1 + var2.getCount() : var1;
   }

   public HudSettingsPanel method_8_10d41d08() {
      return this.Х;
   }

   private int ы6() {
      return Math.max(1, Math.round(свд * this.пх));
   }

   private float method_8(String var1) {
      return ClientFonts.getMediumFont().getFontFace().measureTextWidth(var1, this.ы6(), 0.0F);
   }

   private void ы(PvPAssist.Advice var1, float var2, float var3, float var4) {
      if (!(var4 <= свТ)) {
         List var5 = this.ы(var1.text());
         float var6 = свО * this.пх;
         float var7 = this.getY() + свФ * this.пх + (3 - var5.size()) * var6 / 2.0F + var3;
         int var8 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.WINDOW_TEXT) / свЙ * var4);

         for (int var9 = 0; var9 < var5.size(); var9++) {
            UiDrawing.drawText(ClientFonts.getMediumFont(), (String)var5.get(var9), var2, var7 + var9 * var6, this.ы6(), var8);
         }
      }
   }

   private void ы(PvPAssist.Advice var1, float var2, float var3, float var4, float var5) {
      if (!(var5 <= свэ)) {
         float var6 = var2 - var4 / 2.0F;
         float var7 = var3 - var4 / 2.0F;
         float var8 = var4 * свю;
         int var9 = this.ы_a83dd0cc(var1);
         this.ы(var6, var7, var4, var8, var9, var5);
          HudBackground.draw(var6, var7, var4, var4, var8);
         UiDrawing.drawTexture(var6, var7, var4, var4, var8, new Vector4f(1.0F), ColorMath.packArgb(255, 255, 255, сву * var5), var1.good() ? Ф : Й);
         UiDrawing.drawOutline(var6, var7, var4, var4, св2 * this.пх, var8, ColorMath.withAlpha(var9, свф * var5));
      }
   }

   private void зЗ() {
      LivingEntity var1 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
      if (var1 == null && this.诶 == null && MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen) {
         var1 = MINECRAFT_CLIENT.player;
      }

      if (var1 != null) {
         if (var1 != this.诶) {
            this.诶 = var1;
            this.field_4я = 0;
            this.в.ВД();
            this.М.я(0L);
         }

         this.Вз = var1.getName().getString();
         this.пк = this.П(var1);
         this.пг = this.method_8(var1);
      }
   }

   private void щ(float var1, float var2, float var3) {
      UiDrawing.pushScissor(var1 - 2.0F * this.пх, this.getY() + свы * this.пх, var2 + свй * this.пх, свЖ * this.пх);
      if (this.Я != null && var3 < свм) {
         this.ы(this.Я, var1, свК * this.пх * var3, 1.0F - var3);
      }

      this.ы(this.В, var1, свI * this.пх * (1.0F - var3), var3);
      UiDrawing.popScissor();
   }

   private float П(LivingEntity var1) {
      return RoseClient.getInstance().getServices().getModules().getScoreboardHealth().isEnabled()
         ? Math.max(0.0F, EntityHealth.getHealth(var1))
         : var1.getHealth() + var1.getAbsorptionAmount();
   }

   @Override
   public void иВ() {
      this.Х.сУ();
   }

   public void х(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundEntityEventPacket var2 && MINECRAFT_CLIENT.level != null) {
         if (var2.getEventId() == 35) {
            Entity var4 = var2.getEntity(MINECRAFT_CLIENT.level);
            if (var4 != null) {
               if (var4 == MINECRAFT_CLIENT.player) {
                  this.ъ.ВД();
               } else if (var4 == this.诶) {
                  this.М.ВД();
               }
            }
         }
      }
   }

   private int ы_a83dd0cc(PvPAssist.Advice var1) {
      return var1.good() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : field_4ц;
   }

   private void I(float var1, float var2) {
      int var3 = this.ыз();
      float var4 = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), свO, var3);
      UiDrawing.pushScissor(var1, this.getY() + св7 * this.пх, var2, свР * this.пх);
      UiDrawing.drawText(ClientFonts.getIconsFont(), св3, var1, this.getY() + свр * this.пх, var3, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         this.Вз.isEmpty() ? св6 : this.Вз,
         var1 + var4 + св_ * this.пх,
         this.getY() + свШ * this.пх,
         var3,
         ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
      );
      UiDrawing.popScissor();
   }

   @Override
   public void Б5() {
      this.Х.сС();
   }

   private void ы_de5a6f78(PvPAssist.Advice var1) {
      if (this.В == null) {
         this.В = var1;
         this.сЬ.setImmediately(1.0F);
      } else if (!this.В.text().equals(var1.text())) {
         this.Я = this.В;
         this.В = var1;
         this.сЬ.setImmediately(0.0F);
      }
   }

   private List<String> ы(String var1) {
      ArrayList var2 = new ArrayList(3);
      StringBuilder var3 = new StringBuilder();

      for (String var7 : var1.split(свЗ)) {
         String var8 = var3.isEmpty() ? var7 : var3 + " " + var7;
         if (!var3.isEmpty() && !(this.method_8(var8) <= свЕ * this.пх)) {
            var2.add(var3.toString());
            var3.setLength(0);
            var3.append(var7);
            if (var2.size() == 3) {
               return var2;
            }
         } else {
            var3.setLength(0);
            var3.append(var8);
         }
      }

      if (!var3.isEmpty()) {
         var2.add(var3.toString());
      }

      return var2;
   }

   private void 西(float var1, float var2, float var3) {
      float var4 = this.getX() + свк * this.пх;
      float var5 = var1 - свг * this.пх * 2.0F;
      float var6 = this.getY() + var2 - св0 * this.пх;
      float var7 = свх * this.пх;
      int var8 = this.Я == null ? this.ы_a83dd0cc(this.В) : ColorMath.lerpArgb(this.ы_a83dd0cc(this.Я), this.ы_a83dd0cc(this.В), var3);
      UiDrawing.drawRoundedRect(var4, var6, var5, var7, var7 / 2.0F, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), свУ));
      UiDrawing.drawRoundedRect(var4, var6, Math.max(var7, var5 * this.п0), var7, var7 / 2.0F, var8);
   }

   private void уЛ(float var1) {
      float var2 = this.getX() + свн * this.пх;
      float var3 = this.getY() + свЦ * this.пх;
      float var4 = свч * this.пх;
      if (this.Я != null && var1 < свЭ) {
         this.ы(this.Я, var2, var3, var4 * (св必 + свщ * (1.0F - var1)), 1.0F - var1);
      }

      this.ы(this.В, var2, var3, var4 * (свГ + свл * var1), var1);
   }

   private float method_8(LivingEntity var1) {
      float var2 = свС;

      for (EquipmentSlot var6 : А_at_38334ec8) {
         ItemStack var7 = var1.getItemBySlot(var6);
         if (!var7.isEmpty() && var7.isDamageableItem() && var7.getMaxDamage() > 0) {
            float var8 = (var7.getMaxDamage() - var7.getDamageValue()) * свз / var7.getMaxDamage();
            if (var2 < 0.0F || var8 < var2) {
               var2 = var8;
            }
         }
      }

      return var2;
   }

   private void ы(float var1, float var2, float var3, float var4, int var5, float var6) {
      for (int var7 = 5; var7 > 0; var7--) {
         float var8 = var7 * сви * this.пх;
         float var9 = 1.0F - var7 / св9;
         UiDrawing.drawRoundedRect(var1 - var8, var2 - var8, var3 + var8 * 2.0F, var3 + var8 * 2.0F, var4 + var8, ColorMath.withAlpha(var5, сво * var9 * var6));
      }
   }

   private void зк() {
      this.setWidth(0.0F);
      this.setHeight(0.0F);
   }

   private List<PvPAssist.Advice> Б_2571477d() {
      ArrayList var1 = new ArrayList();
      if (!this.Вз.isEmpty()) {
         float var2 = this.П(MINECRAFT_CLIENT.player);
         if (var2 > this.пк + свЫ) {
            var1.add(new PvPAssist.Advice("Молодец! Ты ебешь " + this.Вз + " Так держать!", true));
         } else if (var2 < this.пк - свl) {
            var1.add(new PvPAssist.Advice(св8, false));
         }

         if (!this.М.method_0(св5)) {
            var1.add(new PvPAssist.Advice("Молодец ты дал " + this.Вз + " Тотем", true));
         }

         if (!this.ъ.method_0(свв)) {
            var1.add(new PvPAssist.Advice("Эй! Откинь перл и отхилься тебе дали тотем! У тебя их осталось (" + this.ыЪ() + ")", false));
         }

         float var3 = this.method_8(MINECRAFT_CLIENT.player);
         float var4 = this.П_de4e60d5().Юг().getValue();
         if (var3 >= 0.0F && this.пг >= 0.0F) {
            if (var3 < this.пг && var3 <= var4) {
               var1.add(new PvPAssist.Advice(свъ, false));
            } else if (this.пг < var3 && this.пг <= var4) {
               var1.add(new PvPAssist.Advice(свМ, true));
            }
         }
      }

      if (var1.isEmpty() && MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen) {
         var1.add(new PvPAssist.Advice(свН, true));
      }

      return var1;
   }

   private void зШ() {
      List var1 = this.Б_2571477d();
      this.с诶.updateTarget(var1.isEmpty() ? 0.0F : 1.0F);
      if (!var1.isEmpty()) {
         long var2 = (long)(this.П_de4e60d5().Юк().getValue() * свб);
         if (var1.size() > 1 && this.в.ф(var2, true)) {
            this.field_4я++;
         }

         this.п0 = var1.size() > 1 ? Math.clamp((float)this.в.method_9() / (float)var2, 0.0F, 1.0F) : 1.0F;
         this.ы_de5a6f78((PvPAssist.Advice)var1.get(Math.floorMod(this.field_4я, var1.size())));
      }
   }

   public PvPAssist(FloatSetting var1) {
      super(свХ, све, свА, св4, свп);
      this.с诶 = new FloatAnimation(0.0F, свД, Easings.CUBIC_OUT);
      this.с1 = new FloatAnimation(0.0F, свЪ, Easings.CUBIC_OUT);
      this.сЬ = new FloatAnimation(1.0F, свт, Easings.CUBIC_OUT);
      this.в = new Stopwatch();
      this.ъ = new Stopwatch();
      this.М = new Stopwatch();
      this.Вз = "";
      this.пг = сва;
      this.п0 = 1.0F;
      this.пх = 1.0F;
      this.Яч = this::Г;
      this.ЯЭ = this::х;
      this.Яй = var1;
      this.Х = new HudSettingsPanel(свЮ, свЧ, var1, var0 -> String.format(Locale.US, свц, var0));
      this.ъ.я(0L);
      this.М.я(0L);
   }

   private Hud П_de4e60d5() {
      return RoseClient.getInstance().getServices().getModules().getHud();
   }

   private record Advice(String text, boolean good) {
   }
}
