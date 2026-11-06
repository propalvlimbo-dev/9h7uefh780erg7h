package dev.rose.render.style;

import dev.rose.util.ColorUtil;


public record Shadow(int color, float blur, float spread, float offsetX, float offsetY) {
   public static final Shadow NONE = new Shadow(0, 0.0F, 0.0F, 0.0F, 0.0F);

   public static Shadow soft(int color, float blur, float offsetY) {
      return new Shadow(color, blur, 0.0F, 0.0F, offsetY);
   }

   public static Shadow glow(int color, float blur, float spread) {
      return new Shadow(color, blur, spread, 0.0F, 0.0F);
   }

   public boolean visible() {
      return ColorUtil.alpha(this.color) > 0 && (this.blur > 0.0F || this.spread != 0.0F);
   }
}
