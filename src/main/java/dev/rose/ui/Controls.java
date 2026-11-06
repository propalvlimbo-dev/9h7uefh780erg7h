package dev.rose.ui;

import dev.rose.render.style.Glass;
import dev.rose.util.ColorUtil;


final class Controls {
   private Controls() {
   }

   static int track(Glass glass) {
      return ColorUtil.withAlpha(7895167, Math.round(51.0F + 77.0F * glass.labelBrightness()));
   }

   static int selection(Glass glass) {
      return ColorUtil.withAlpha(16777215, Math.round(230.0F + -169.0F * glass.labelBrightness()));
   }
}
