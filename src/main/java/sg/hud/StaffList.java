package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.util.time.StaffTracker;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class StaffList extends HudElement {
   private final StaffTracker В = new StaffTracker();
   @SerializedName("О")
   private final FloatAnimation О_04d91340;
   private final FloatAnimation Ф;
   private final FloatAnimation Й;
   @SerializedName("О")
   private final Map<StaffTracker.StaffEntry, FloatAnimation[]> О_ea411899;
   private final EventListener<PlayerTickEvent> сл;
   private final EventListener<HudRenderEvent> сэ;
   private static final String сЬЭ = "StaffList";
   private static final float сЬ必 = 1720.0F;
   private static final float сЬщ = 645.0F;
   private static final float сЬГ = 120.0F;
   private static final float сЬл = 35.0F;
   private static final float сЬэ = 15.0F;
   private static final float сЬю = 15.0F;
   private static final float сЬЩ = 10.0F;
   private static final String сЬу = "Персонал";
   private static final float сЬ2 = 0.001F;
   private static final float сЬф = 13.0F;
   private static final float сЬи = 13.0F;
   private static final float сЬ9 = 13.0F;
   private static final float сЬо = 100.0F;
   private static final float сЬO = 50.0F;
   private static final float сЬ7 = 255.0F;
   private static final float сЬР = 12.0F;
   private static final float сЬ3 = 33.0F;
   private static final float сЬр = 10.0F;
   private static final float сЬ6 = 0.01F;
   private static final float сЬ_ = 0.5F;
   private static final float сЬШ = 0.5F;
   private static final double сЬы = 700.0;
   private static final float сЬй = 0.55F;
   private static final float сЬЖ = 10.0F;
   private static final float сЬм = 33.0F;
   private static final float сЬК = 20.0F;
   private static final float сЬI = 1.4F;
   private static final String сЬТ = "O";
   private static final float сЬО = 10.0F;
   private static final float сЬФ = 8.0F;
   private static final String сЬЙ = "StaffList";
   private static final float сЬк = 32.0F;
   private static final float сЬг = 9.0F;
   private static final float сЬ0 = -10.0F;
   private static final float сЬх = 36.0F;
   private static final float сЬУ = 255.0F;
   private static final float сЬЗ = 10.0F;
   private static final float сЬЕ = 2.5F;
   private static final float сЬП = 10.0F;
   private static final float сЬ西 = 13.0F;
   private static final float сЬд = 2.5F;
   private static final float сЬБ = 10.0F;
   private static final float сЬС = 13.0F;
   private static final float сЬз = 2.5F;
   private static final float сЬц = 36.0F;
   private static final float сЬя = 15.0F;
   private static final float с衣с = -10.0F;
   private static final float с衣В = 15.0F;
   private static final float с衣Я = 15.0F;

   public StaffList() {
      super(сЬЭ, сЬ必, сЬщ, сЬГ, сЬл);
      this.О_04d91340 = new FloatAnimation(0.0F, сЬэ);
      this.Ф = new FloatAnimation(0.0F, сЬю);
      this.Й = new FloatAnimation(0.0F, сЬЩ);
      this.О_ea411899 = new HashMap<>();
      this.сл = var1 -> this.В.Вс();
      this.сэ = this::с;
   }

   private List<StaffTracker.StaffEntry> Щ() {
      return this.В.Ф();
   }

   public void с(HudRenderEvent var1) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(сЬу)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         List<StaffTracker.StaffEntry> var2 = this.Щ();
         boolean var3 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen || !var2.isEmpty();
         this.Й.updateTarget(var3 ? 1.0F : 0.0F);
         if (!var3 && this.Й.getValue() <= сЬ2) {
            this.О_ea411899.clear();
            this.setWidth(0.0F);
            this.setHeight(0.0F);
         } else {
            float var4 = this.getX();
            float var5 = this.getY();
            float var6 = 0.0F;
            float var7 = 0.0F;
            float var8 = 0.0F;

            for (StaffTracker.StaffEntry var10 : var2) {
               var6 = Math.max(var6, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.г_(), сЬф, 0.0F));
               var7 = Math.max(var7, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.п().getString(), сЬи, 0.0F));
               var8 = Math.max(var8, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.г().к6(), сЬ9, 0.0F));
            }

            float var23 = Math.max(сЬо, var6 + var7 + var8 + сЬO);
            this.О_04d91340.updateTarget(var23);
            UiDrawing.setGlobalAlpha(сЬ7 * this.Й.getValue());
            this.pushGuiScaleTransform();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               UiBatchDrawing.drawHudWindowBackground(var4, var5, this.О_04d91340.getValue(), this.Ф.getValue(), сЬР);
               float var24 = Math.clamp((this.Ф.getValue() - сЬ3) / сЬр, 0.0F, 1.0F);
               if (var24 > сЬ6) {
                  int var11 = ColorMath.withAlpha(
                     ColorMath.blendArgb(
                        ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), сЬ_ + сЬШ * (float)Math.sin(System.currentTimeMillis() / сЬы)
                     ),
                     сЬй * var24
                  );
                  int var12 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), 0.0F);
                  UiBatchDrawing.drawRoundedRect(
                     var4 + сЬЖ, var5 + сЬм, this.О_04d91340.getValue() - сЬК, сЬI, new Vector4f(0.0F), new Vector4i(var12, var11, var11, var12)
                  );
               }

               UiBatchDrawing.drawText(ClientFonts.getIconsFont(), сЬТ, var4 + сЬО, var5 + сЬФ, 16, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
               UiBatchDrawing.drawText(ClientFonts.getMediumFont(), сЬЙ, var4 + сЬк, var5 + сЬг, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
               ArrayList var25 = new ArrayList();

               for (int var26 = 0; var26 < var2.size(); var26++) {
                  StaffTracker.StaffEntry var13 = (StaffTracker.StaffEntry)var2.get(var26);
                  int var14 = var26;
                  FloatAnimation[] var15 = this.О_ea411899.computeIfAbsent(var13, var1x -> new FloatAnimation[]{new FloatAnimation(0.0F, сЬя), new FloatAnimation(с衣с, с衣В), new FloatAnimation(var14 * 20, с衣Я)});
                  var15[0].updateTarget(1.0F);
                  var15[1].updateTarget(0.0F);
                  float var16 = var26 * 20;
                  var15[2].updateTarget(var16);
               }

               for (Entry var29 : this.О_ea411899.entrySet()) {
                  if (!var2.contains(var29.getKey())) {
                     FloatAnimation[] var31 = (FloatAnimation[])var29.getValue();
                     var31[0].updateTarget(0.0F);
                     var31[1].updateTarget(сЬ0);
                     if (var31[0].isFinished() && var31[1].isFinished()) {
                        var25.add((StaffTracker.StaffEntry)var29.getKey());
                     }
                  }
               }

               var25.forEach(this.О_ea411899::remove);

               for (Entry var30 : this.О_ea411899.entrySet()) {
                  StaffTracker.StaffEntry var32 = (StaffTracker.StaffEntry)var30.getKey();
                  FloatAnimation[] var33 = (FloatAnimation[])var30.getValue();
                  float var34 = var33[0].getValue();
                  if (!(var34 <= 0.0F) || !var33[0].isFinished()) {
                     float var17 = var5 + сЬх + var33[2].getValue();
                     String var18 = var32.г_();
                     float var19 = var33[1].getValue();
                     UiDrawing.setGlobalAlpha(var34 * сЬУ);
                     UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var32.п(), var4 + сЬЗ, var17 + сЬЕ + var19, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(),
                        var18,
                        var4 + сЬП + ClientFonts.getMediumFont().getFontFace().measureTextWidth(var32.п().getString() + " ", сЬ西, 0.0F),
                        var17 + сЬд + var19,
                        13,
                        ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT)
                     );
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(),
                        var32.г().к6(),
                        var4 + this.О_04d91340.getValue() - сЬБ - ClientFonts.getMediumFont().getFontFace().measureTextWidth(var32.г().к6(), сЬС, 0.0F),
                        var17 + сЬз + var19,
                        13,
                        var32.г().Д1
                     );
                     UiDrawing.resetGlobalAlpha();
                  }
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
               UiDrawing.resetGlobalAlpha();
               this.popGuiScaleTransform();
            }

            this.Ф.updateTarget(var2.isEmpty() ? сЬц : 36 + var2.size() * 20 + 7);
            this.setWidth(this.О_04d91340.getValue());
            this.setHeight(this.Ф.getValue());
         }
      }
   }

   public enum StaffStatus {
      NONE("Active", "W", 195, 195, 195),
      VANISH("Spec", "i", 255, 95, 95),
      SPECTATOR("GM3", "d", 255, 190, 80);

      private final String ЯЦ;
      private final String Яч;
      private final int ДЧ;
      private final int ДЛ;
      private final int Д诶;
      private final int Д1;

      StaffStatus(String var3, String var4, int var5, int var6, int var7) {
         this.ЯЦ = var3;
         this.Яч = var4;
         this.ДЧ = var5;
         this.ДЛ = var6;
         this.Д诶 = var7;
         this.Д1 = ColorMath.packRgb(var5, var6, var7);
      }

      public String к6() {
         return this.ЯЦ;
      }

      public String к1() {
         return this.Яч;
      }

      public int ЬТ() {
         return this.Д1;
      }

      public int ЬO() {
         return this.ДЧ;
      }

      public int Ьо() {
         return this.ДЛ;
      }

      public int ЬМ() {
         return this.Д诶;
      }
   }
}
