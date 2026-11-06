package dev.rose.module.impl.visual;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.mixin.OverlayTextureAccessor;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;


public final class EntityColorsModule extends Module {
   private static final int VANILLA_HURT = -1291911168;
   private final BoolSetting hurt = this.add(new BoolSetting("hurt", true));
   private final ColorSetting hurtColor = this.add(new ColorSetting("hurt_color", -1470445318, true));
   private final BoolSetting glint = this.add(new BoolSetting("glint", true));
   private final ColorSetting glintColor = this.add(new ColorSetting("glint_color", -12991045, false));
   private int appliedHurt = -1291911168;

   public EntityColorsModule() {
      super("entity_colors", Category.VISUALS, false);
      this.hurtColor.visibleWhen(this.hurt::isOn);
      this.glintColor.visibleWhen(this.glint::isOn);
   }

   @Override
   public int icon() {
      return 58087;
   }

   @Override
   public void onTick() {
      this.applyHurtColor();
   }

   @Override
   protected void onDisable() {
      this.applyHurtColor();
   }

   @Override
   public void onConfigLoaded() {
      this.applyHurtColor();
   }

   private void applyHurtColor() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.gameRenderer != null) {
         int color = this.isEnabled() && this.hurt.isOn() ? this.hurtColor.color() : -1291911168;
         if (color != this.appliedHurt) {
            this.appliedHurt = color;
            DynamicTexture texture = ((OverlayTextureAccessor)mc.gameRenderer.overlayTexture()).rose$texture();
            NativeImage pixels = texture.getPixels();
            if (pixels != null) {
               int abgr = ColorUtil.alpha(color) << 24 | ColorUtil.blue(color) << 16 | ColorUtil.green(color) << 8 | ColorUtil.red(color);

               for (int y = 0; y < 8; y++) {
                  for (int x = 0; x < 16; x++) {
                     pixels.setPixelABGR(x, y, abgr);
                  }
               }

               texture.upload();
            }
         }
      }
   }

   public int glintColor() {
      return this.isEnabled() && this.glint.isOn() ? this.glintColor.color() : 0;
   }
}
