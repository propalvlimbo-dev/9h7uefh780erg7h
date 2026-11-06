package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.KeyNameRegistry;
import sg.events.EventListener;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.render.RoseBatchedItemAtlas;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.client.RoseClient;
import sg.modules.impl.misc.FuntimeHelper;
import sg.modules.impl.misc.HolyWorldHelper;
import sg.modules.impl.misc.RwHelper;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.init.BoundItemIndicator;
import sg.gui.hud.HudSettingsPanel;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.settings.FloatSetting;

public class ItemIndicator extends HudElement implements InteractiveHudElement {
   private static final float тЕ = 28.0F;
   private static final float тП = 4.0F;
   private static final float т西 = 8.0F;
   private static final int Дц = 7;
   @SerializedName("7")
   private final Matrix4f field_7 = new Matrix4f();
   @SerializedName("1")
   private final Vector4f field_1 = new Vector4f();
   private final Vector4i е = new Vector4i(-1);
   private final List<RoseBatchedItemAtlas.ItemHandle> ВВ = new ArrayList<>();
   private final FloatSetting Х0;
   private final HudSettingsPanel п;
   private final EventListener<HudRenderEvent> ХШ = this::РЯ;
   private static final String В_р = "ItemIndicator";
   private static final float В_6 = 1226.0F;
   private static final float В__ = 1029.0F;
   private static final String В_Ш = "ItemIndicator";
   private static final String В_ы = "Размер";
   private static final String В_й = "Индикатор предметов";
   private static final float В_Ж = 0.5F;
   private static final float В_м = 28.0F;
   private static final float В_К = 4.0F;
   private static final float В_I = 8.0F;
   private static final float В_Т = 7.0F;
   private static final float В_О = 16.0F;
   private static final float В_Ф = 0.5F;
   private static final float В_Й = 0.5F;
   private static final float В_к = 0.5F;
   private static final String В_г = "SHIFT";
   private static final String В_0 = "CTRL";
   private static final String В_х = "ALT";
   private static final String В_У = "SPACE";
   private static final String В_З = "TAB";
   private static final String В_Е = "CAPS";
   private static final String В_П = "KEY";
   private static final String В_西 = "%.2fx";

   private void РЯ(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && var1.getGraphics() != null && RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(В_й)) {
         List var2 = this.Р诶();
         if (var2.isEmpty()) {
            this.setWidth(0.0F);
            this.setHeight(0.0F);
            this.п.сС();
         } else {
            float var3 = Math.max(В_Ж, this.Х0.getValue());
            float var4 = В_м * var3;
            float var5 = В_К * var3;
            float var6 = В_I * var3;
            int var7 = Math.max(6, Math.round(В_Т * var3));
            float var8 = В_О * var3;
            UiDrawing.setTransform(this.field_7.identity());

            while (this.ВВ.size() < var2.size()) {
               this.ВВ.add(new RoseBatchedItemAtlas.ItemHandle());
            }

            float var9 = var2.size() * var4 + Math.max(0, var2.size() - 1) * var5;
            float var10 = var4;
            float var11 = Math.round(this.getX());
            float var12 = Math.round(this.getY());
            this.pushGuiScaleTransform();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               for (int var13 = 0; var13 < var2.size(); var13++) {
                  float var14 = var11 + var13 * (var4 + var5);
                  UiBatchDrawing.drawHudWindowBackground(var14, var12, var4, var4, var6);
               }

               for (int var25 = 0; var25 < var2.size(); var25++) {
                  BoundItemIndicator var27 = (BoundItemIndicator)var2.get(var25);
                  float var15 = var11 + var25 * (var4 + var5);
                  float var16 = (var4 - var8) * В_Ф;
                  float var17 = Math.round(var15 + var16);
                  float var18 = Math.round(var12 + var16);
                  if (var27.hasCustomIcon()) {
                     UiBatchDrawing.drawTexture(var17, var18, var8, var8, this.field_1, this.е, var27.icon());
                  } else {
                     UiBatchDrawing.drawItemWithOverlay(var17, var18, var8, var8, var27.stack(), false, this.ВВ.get(var25));
                  }
               }

               for (int var26 = 0; var26 < var2.size(); var26++) {
                  BoundItemIndicator var28 = (BoundItemIndicator)var2.get(var26);
                  float var29 = var11 + var26 * (var4 + var5);
                  String var30 = this.Ц(var28.key());
                  float var31 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var30, var7, 0.0F);
                  int var32 = PanelBackgroundRenderer.method_5(var26 * 5 + 2);
                  float var19 = var29 + (var4 - var31) * В_Й;
                  float var20 = var12 + var4 - var7 * В_к + 2.0F * var3;
                  float var21 = UiDrawing.alignTextCenterY(ClientFonts.getMediumFont(), var30, var20, var7);
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var30, var19, var21, var7, var32);
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
               this.popGuiScaleTransform();
            }

            this.setWidth(var9);
            this.setHeight(var10);
         }
      } else {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
         this.п.сС();
      }
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (!(this.getScaledWidth() <= 0.0F) && !(this.getScaledHeight() <= 0.0F)) {
         if (var3 == 1 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
            this.п.method_2(var1, var2);
            return true;
         } else {
            return this.п.ю(var1, var2, var3);
         }
      } else {
         return false;
      }
   }

   @Override
   public void Б5() {
      this.п.сС();
   }

   @Generated
   public HudSettingsPanel method_1() {
      return this.п;
   }

   private List<BoundItemIndicator> Р诶() {
      FuntimeHelper var1 = RoseClient.getInstance().getServices().getModules().getFuntimeHelper();
      HolyWorldHelper var2 = RoseClient.getInstance().getServices().getModules().getHolyWorldHelper();
      RwHelper var3 = RoseClient.getInstance().getServices().getModules().getRwHelper();
      ArrayList var4 = new ArrayList();
      this.П(var4, var1 != null && var1.isEnabled() ? var1.method_5() : null);
      this.П(var4, var2 != null && var2.isEnabled() ? var2.getBoundItemIndicators() : null);
      this.П(var4, var3 != null && var3.isEnabled() ? var3.М() : null);
      return var4;
   }

   private void П(List<BoundItemIndicator> var1, List<BoundItemIndicator> var2) {
      if (var2 != null) {
         for (BoundItemIndicator var4 : var2) {
            if (var4.key() >= 0) {
               var1.add(var4);
            }
         }
      }
   }

   public ItemIndicator(FloatSetting var1) {
      super(В_р, В_6, В__, 0.0F, 0.0F);
      this.Х0 = var1;
      this.п = new HudSettingsPanel(В_Ш, В_ы, var1, var0 -> String.format(Locale.US, В_西, var0));
   }

   private String Ц(int var1) {
      String var2 = KeyNameRegistry.сч.get(var1);
      if (var2 != null) {
         return var2.toUpperCase(Locale.ROOT);
      }

      String var3 = KeyDisplayNames.О(var1);
      if (!var3.isBlank()) {
         return var3.toUpperCase(Locale.ROOT);
      }

      return switch (var1) {
         case 32 -> В_У;
         case 258 -> В_З;
         case 280 -> В_Е;
         case 340, 344 -> В_г;
         case 341, 345 -> В_0;
         case 342, 346 -> В_х;
         default -> В_П;
      };
   }

   @Generated
   public FloatSetting РЫ() {
      return this.Х0;
   }

   @Override
   public void иВ() {
      this.п.сУ();
   }
}
