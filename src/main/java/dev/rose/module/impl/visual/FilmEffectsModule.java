package dev.rose.module.impl.visual;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Images;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Paint;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.Random;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;


public final class FilmEffectsModule extends Module {
   private final NumberSetting vignette;
   private final NumberSetting vignetteSize;
   private final ColorSetting vignetteColor;
   private final NumberSetting grain;
   private final NumberSetting grainSize;
   private final NumberSetting letterbox;
   private final BoolSetting hideInMenus;
   private final NumberSetting tint;
   private final ColorSetting tintColor;
   private final Random random;

   public FilmEffectsModule() {
      super("film_effects", Category.VISUALS, false);
      this.section("vignette", 57408);
      this.vignette = this.add(new NumberSetting("vignette", 45.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.vignetteSize = this.add(new NumberSetting("vignette_size", 60.0, 20.0, 100.0, 5.0, "rose.unit.percent"));
      this.vignetteColor = this.add(new ColorSetting("vignette_color", -16777216, false));
      this.section("grain", 57552);
      this.grain = this.add(new NumberSetting("grain", 20.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.grainSize = this.add(new NumberSetting("grain_size", 100.0, 50.0, 300.0, 10.0, "rose.unit.percent"));
      this.section("frame", 57619);
      this.letterbox = this.add(new NumberSetting("letterbox", 0.0, 0.0, 20.0, 1.0, "rose.unit.percent"));
      this.hideInMenus = this.add(new BoolSetting("hide_in_menus", true));
      this.tint = this.add(new NumberSetting("tint", 0.0, 0.0, 60.0, 2.0, "rose.unit.percent"));
      this.tintColor = this.add(new ColorSetting("tint_color", -24822, false));
      this.random = new Random();
   }

   @Override
   public int icon() {
      return 57552;
   }

   @Override
   public void onHud(GuiGraphics g, DeltaTracker deltaTracker) {
      Minecraft mc = Minecraft.getInstance();
      if (!this.hideInMenus.isOn() || mc.screen == null) {
         int w = g.guiWidth();
         int h = g.guiHeight();
         if (this.tint.floatValue() > 0.0F) {
            int color = ColorUtil.withAlpha(this.tintColor.color(), Math.round(255.0F * this.tint.floatValue() / 100.0F));
            g.fill(0, 0, w, h, color);
         }

         if (this.vignette.floatValue() > 0.0F) {
            int dark = ColorUtil.withAlpha(this.vignetteColor.color(), Math.round(255.0F * this.vignette.floatValue() / 100.0F));
            int clear = ColorUtil.withAlpha(this.vignetteColor.color(), 0);
            float inner = 1.0F - this.vignetteSize.floatValue() / 100.0F * 0.8F;
            Surfaces.fill(g, 0.0F, 0.0F, w, h, Corners.NONE, Paint.radial(0.5F, 0.5F, 0.72F, clear, clear, dark).withStops(0.0F, inner, 1.0F), 1.0F);
         }

         if (this.grain.floatValue() > 0.0F) {
            GpuTextureView texture = Minecraft.getInstance().getTextureManager().getTexture(CosmeticTextures.noise()).getTextureView();
            float cell = 256.0F * this.grainSize.floatValue() / 100.0F / Minecraft.getInstance().getWindow().getGuiScale();
            float u = this.random.nextFloat();
            float v = this.random.nextFloat();
            Images.draw(
               g,
               texture,
               RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST),
               u,
               v,
               u + w / cell,
               v + h / cell,
               0.0F,
               0.0F,
               w,
               h,
               Corners.NONE,
               this.grain.floatValue() / 100.0F * 0.55F
            );
         }

         float bar = h * this.letterbox.floatValue() / 100.0F;
         if (bar > 0.5F) {
            g.fill(0, 0, w, Math.round(bar), -16777216);
            g.fill(0, h - Math.round(bar), w, h, -16777216);
         }
      }
   }
}
