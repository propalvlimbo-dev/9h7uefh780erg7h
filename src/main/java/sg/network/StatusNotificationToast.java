package sg.network;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.ClientFonts;
import sg.modules.impl.init.NotificationToast;
import sg.render.RoseBatchedItemAtlas;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public class StatusNotificationToast extends NotificationToast {
   private static final float Ян = 32.0F;
   private static final float ЯЦ = 10.0F;
   private static final float Яч = 20.0F;
   private static final float ЯЭ = 25.0F;
   private static final Vector4f е_at_7d34f10c = new Vector4f(0.0F);
   private static final Vector4i Я_at_7d34f10c = new Vector4i(-1);
   private static final float Я必 = 20.0F;
   private static final float Ящ = 30.0F;
   private final String сж;
   @SerializedName("В")
   private final Integer В_fb15200a;
   private final ItemStack п;
   @SerializedName("В")
   private final RoseBatchedItemAtlas.ItemHandle В_070b834d = new RoseBatchedItemAtlas.ItemHandle();
   private static final long ВрI = 1500L;
   private static final float ВрТ = 10.0F;
   private static final float ВрО = 25.0F;
   private static final float ВрФ = 20.0F;
   private static final float ВрЙ = 30.0F;
   private static final float Врк = 32.0F;
   private static final String Врг = "H";
   private static final String Вр0 = "I";
   private static final String Врх = "M";
   private static final float ВрУ = 255.0F;
   private static final float ВрЗ = 9.0F;
   private static final float ВрЕ = 10.0F;
   private static final float ВрП = 6.0F;
   private static final float Вр西 = 20.0F;
   private static final float Врд = 20.0F;
   private static final float ВрБ = 25.0F;
   private static final float ВрС = 15.5F;
   private static final float Врз = 15.0F;
   private static final float Врц = 20.0F;
   private static final float Вря = 15.5F;
   private static final float В6с = 16.0F;
   private static final float В6В = 17.0F;
   private static final float В6Я = 14.5F;
   private static final float В6Х = 0.28F;
   private static final float В6е = 0.72F;
   private static final float В6А = 5.75F;
   private static final float В64 = 5.75F;
   private static final float В6п = 13.5F;
   private static final float В6Д = 1.65F;
   private static final float В6Ъ = 0.001F;
   private static final float В6т = 5.75F;
   private static final float В6а = 5.75F;
   private static final float В6Ю = 13.5F;
   private static final float В6Ч = 1.65F;
   private static final String В6Л = "H";
   private static final String В6诶 = "I";
   private static final String В61 = "r";
   private static final String В6Ь = "s";

   public StatusNotificationToast(String var1, Component var2, int var3, ItemStack var4) {
      this(var1, var2, var3, var4, ВрI);
   }

   @Generated
   public RoseBatchedItemAtlas.ItemHandle Ф_070b834d() {
      return this.В_070b834d;
   }

   @Generated
   public Integer Ф_fb15200a() {
      return this.В_fb15200a;
   }

   @Override
   public float Щъ() {
      return UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.е.getString(), 14) + ВрТ + (this.ФГ() ? ВрО : 0.0F) + (this.ФЯ() ? ВрФ : 0.0F) + ВрЙ;
   }

   @Override
   public void с(float var1, float var2, float var3) {
      this.衣(var3);
      boolean var4 = this.ЖН();
      this.сЖ.updateTarget(var4 ? 0.0F : 1.0F);
      this.сй.updateTarget(this.Аб && var4 ? var3 - 2.0F : var3);
      float var5 = var2 + this.сй.getValue();
      float var6 = this.сЖ.getValue();
      float var7 = this.Щъ();

      int var8 = switch (this.сж) {
         case Врг -> ColorMath.packRgb(22, 251, 27);
         case Вр0 -> ColorMath.packRgb(251, 22, 22);
         case Врх -> ColorMath.packRgb(255, 165, 0);
         default -> this.В_fb15200a;
      };
      float var17 = Mth.clamp(1.0F - (float)this.у.Д() / (float)this.Хя, 0.0F, 1.0F);
      UiDrawing.setGlobalAlpha(ВрУ * var6);
      UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
      UiBatchDrawing.drawHudWindowBackground(var1, var5 - 1.0F, var7, this.Щр(), ВрЗ);
      float var18 = var1 + ВрЕ;
      if (this.ФГ()) {
         UiBatchDrawing.drawItem(var18, var5 - 1.0F + ВрП, Вр西, Врд, е_at_7d34f10c, Я_at_7d34f10c, this.п, this.В_070b834d);
         var18 += ВрБ;
      }

      String var11 = this.Щл();
      if (!var11.isEmpty()) {
         float var12 = UiDrawing.alignTextCenterY(ClientFonts.getIconsFont(), var11, var5 + ВрС, Врз);
         UiBatchDrawing.drawText(ClientFonts.getIconsFont(), var11, var18 + 1.0F, var12 + 1.0F, 12, var8);
         var18 += Врц;
      }

      float var19 = UiDrawing.alignCapHeightCenterY(ClientFonts.getMediumFont(), var5 + Вря, В6с);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.е, var18, var19, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
      float var13 = var1 + var7 - В6В;
      float var14 = var5 + В6Я;
      int var15 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT), В6Х);
      int var16 = ColorMath.blendArgb(var8, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), В6е);
      UiBatchDrawing.drawCircularProgress(var13 - В6А, var14 - В64, В6п, В6Д, 1.0F, var15);
      if (var17 > В6Ъ) {
         UiBatchDrawing.drawCircularProgress(var13 - В6т, var14 - В6а, В6Ю, В6Ч, var17, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      }

      UiDrawing.resetGlobalAlpha();
      UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
   }

   public StatusNotificationToast(String var1, Component var2, int var3) {
      this(var1, var2, var3, ItemStack.EMPTY);
   }

   private boolean ФЯ() {
      return !this.Щл().isEmpty();
   }

   public StatusNotificationToast(String var1, Component var2, int var3, ItemStack var4, long var5) {
      super(var2, var5);
      this.сж = var1;
      this.В_fb15200a = var3;
      this.п = var4 != null && !var4.isEmpty() ? var4.copy() : ItemStack.EMPTY;
   }

   private boolean ФГ() {
      return !this.п.isEmpty();
   }

   @Generated
   public ItemStack 诶() {
      return this.п;
   }

   public StatusNotificationToast(String var1, String var2, int var3) {
      this(var1, Component.literal(var2), var3, ItemStack.EMPTY);
   }

   @Override
   public float Щр() {
      return Врк;
   }

   @Generated
   public String ЩО() {
      return this.сж;
   }

   private String Щл() {
      return switch (this.сж) {
         case В6Л -> В61;
         case В6诶 -> В6Ь;
         default -> "";
      };
   }
}
