package dev.rose.gui.menu;

import dev.rose.util.ColorUtil;


public final class Scroll {
   private static final float STEP = 28.0F;
   private float target;
   private float shown;
   private float max;
   private float view;
   private float content;
   private float dragStart;

   public float value() {
      return this.shown;
   }

   public float max() {
      return this.max;
   }

   public void reset() {
      this.target = 0.0F;
      this.shown = 0.0F;
   }

   public void scrollTo(float value) {
      this.target = Math.clamp(value, 0.0F, Math.max(0.0F, this.max));
   }

   public void update(MenuUi ui, float contentHeight, float viewHeight) {
      this.content = contentHeight;
      this.view = viewHeight;
      this.max = Math.max(0.0F, contentHeight - viewHeight);
      this.target = Math.clamp(this.target, 0.0F, this.max);
      float k = 1.0F - (float)Math.exp(-ui.delta() * 20.0F);
      this.shown = this.shown + (this.target - this.shown) * k;
      if (Math.abs(this.target - this.shown) < 0.05F) {
         this.shown = this.target;
      }

      this.shown = Math.clamp(this.shown, 0.0F, this.max);
   }

   public MenuUi.Handler wheel() {
      return new MenuUi.Handler() {
         @Override
         public boolean scroll(double amount) {
            if (amount != 0.0) {
               Scroll.this.target = Math.clamp(Scroll.this.target - (float)amount * 28.0F, 0.0F, Scroll.this.max);
            }

            return true;
         }
      };
   }

   public void drawBar(MenuUi ui, String id, float x, float y, float h) {
      if (!(this.max <= 0.5F) && !(this.content <= 0.0F)) {
         MenuPalette p = ui.palette;
         float thumbH = Math.max(18.0F, h * this.view / this.content);
         float thumbY = y + (h - thumbH) * (this.shown / this.max);
         boolean active = ui.hot(id) || ui.pressed(id);
         float grow = ui.anim(id + ":grow", active ? 1.0F : 0.0F, 18.0F);
         float w = 2.5F + grow * 1.5F;
         MenuDraw.pill(
            ui,
            x - w,
            thumbY,
            w,
            thumbH,
            p.light() ? ColorUtil.withAlpha(-16777216, 48 + Math.round(48.0F * grow)) : ColorUtil.withAlpha(-1, 38 + Math.round(48.0F * grow))
         );
         final float travel = h - thumbH;
         ui.target(id, x - 8.0F, thumbY, 8.0F, thumbH, new MenuUi.Handler() {
            private float pressY;

            @Override
            public void press(int button, float localX, float localY) {
               this.pressY = localY;
               Scroll.this.dragStart = Scroll.this.target;
            }

            @Override
            public void drag(float localX, float localY) {
               if (!(travel <= 0.0F)) {
                  Scroll.this.target = Math.clamp(Scroll.this.dragStart + (localY - this.pressY) * Scroll.this.max / travel, 0.0F, Scroll.this.max);
                  Scroll.this.shown = Scroll.this.target;
               }
            }

            @Override
            public boolean scroll(double amount) {
               if (amount != 0.0) {
                  Scroll.this.target = Math.clamp(Scroll.this.target - (float)amount * 28.0F, 0.0F, Scroll.this.max);
               }

               return true;
            }
         });
      }
   }
}
