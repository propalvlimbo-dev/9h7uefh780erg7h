package sg.modules.impl.render;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public final class ArmorDurability extends ClientModule {
   private static ArmorDurability В;
   private final BooleanSetting Ъ;
   private final BooleanSetting т;
   private static final String Я3Т = "ArmorDurability";
   private static final String Я3О = "Показывает прочность брони";
   private static final String Я3Ф = "На себе";
   private static final String Я3Й = "На врагах";
   private static final double Я3к = 255.0;
   private static final double Я3г = 100.0;
   private static final float Я30 = 9.0F;
   private static final float Я3х = 8.0F;
   private static final float Я3У = 0.5F;
   private static final float Я3З = 16.0F;

   public int Й(ItemStack var1) {
      int var2 = var1.getMaxDamage();
      return var2 <= 0 ? 100 : (int)Math.round((var2 - var1.getDamageValue()) * Я3г / var2);
   }

   public void о(ItemStack var1, float var2, float var3, float var4, boolean var5) {
      String var6 = this.Й(var1) + "%";
      int var7 = Math.max(7, Math.round(Я30 * var4));
      float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var6, var7);
      float var9 = var2 + Я3х * var4 - var8 * Я3У;
      float var10 = var5 ? var3 - var7 - 1.0F * var4 : var3 + Я3З * var4 + 1.0F * var4;
      UiDrawing.drawText(ClientFonts.getMediumFont(), var6, var9, var10, var7, this.к(var1));
   }

   public int к(ItemStack var1) {
      int var2 = var1.getBarColor();
      return ColorMath.packArgb(var2 >> 16 & 0xFF, var2 >> 8 & 0xFF, var2 & 0xFF, Я3к);
   }

   public boolean о(ItemStack var1, boolean var2) {
      if (!this.isEnabled() || var1 == null || var1.isEmpty()) {
         return false;
      } else {
         return !var1.is(ItemTags.DURABILITY_ENCHANTABLE) ? false : var2 ? this.Ъ.getValue() : this.т.getValue();
      }
   }

   public ArmorDurability() {
      super(Я3Т, Я3О, ModuleCategory.RENDER);
      this.Ъ = new BooleanSetting(Я3Ф, true);
      this.т = new BooleanSetting(Я3Й, true);
      В = this;
      this.addSettings(this.Ъ, this.т);
   }

   public static ArmorDurability о_911da74a() {
      return В;
   }
}
