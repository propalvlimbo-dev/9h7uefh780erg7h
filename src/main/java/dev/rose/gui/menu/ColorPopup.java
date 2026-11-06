package dev.rose.gui.menu;

import dev.rose.gui.UiSounds;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.minecraft.client.resources.language.I18n;


public final class ColorPopup {
   private static final int[] PRESETS = new int[]{-1, -47814, -24822, -10742, -13577896, -10169601, -16087809, -4236558, -51361, -14935010};
   private static final float W = 176.0F;
   final String id;
   private final float anchorX;
   private final float anchorY;
   private final boolean hasAlpha;
   private final IntConsumer apply;
   private final TextBuffer hex = new TextBuffer(9);
   private BooleanSupplier rainbowGet;
   private Consumer<Boolean> rainbowSet;
   private float hue;
   private float saturation;
   private float value;
   private float alpha;

   public ColorPopup(String id, float anchorX, float anchorY, int color, boolean hasAlpha, IntConsumer apply) {
      this.id = id;
      this.anchorX = anchorX;
      this.anchorY = anchorY;
      this.hasAlpha = hasAlpha;
      this.apply = apply;
      this.load(color);
   }

   public ColorPopup rainbow(BooleanSupplier get, Consumer<Boolean> set) {
      this.rainbowGet = get;
      this.rainbowSet = set;
      return this;
   }

   private void load(int color) {
      float[] hsb = ColorUtil.toHsb(color);
      this.hue = hsb[0];
      this.saturation = hsb[1];
      this.value = hsb[2];
      this.alpha = ColorUtil.alpha(color) / 255.0F;
      this.hex.set(String.format(Locale.ROOT, this.hasAlpha ? "#%08X" : "#%06X", this.hasAlpha ? color : color & 16777215));
   }

   private int color() {
      return ColorUtil.hsb(this.hue, this.saturation, this.value, Math.round((this.hasAlpha ? this.alpha : 1.0F) * 255.0F));
   }

   private void changed() {
      int color = this.color();
      this.apply.accept(color);
      this.hex.set(String.format(Locale.ROOT, this.hasAlpha ? "#%08X" : "#%06X", this.hasAlpha ? color : color & 16777215));
   }

   public void draw(MenuUi ui) {
      MenuPalette p = ui.palette;
      float pad = 9.0F;
      float squareH = 88.0F;
      float h = pad + squareH + 6.0F + 8.0F + (this.hasAlpha ? 14 : 0) + 8.0F + 18.0F + 8.0F + 12.0F + pad;
      float x = Math.clamp(this.anchorX - 176.0F + 20.0F, 4.0F, ui.screenWidth - 176.0F - 4.0F);
      float y = this.anchorY + 20.0F + h > ui.screenHeight - 4.0F ? this.anchorY - h - 4.0F : this.anchorY + 20.0F;
      float appear = ui.anim(this.id + ":appear", 1.0F, 26.0F);
      float previous = ui.alpha;
      ui.alpha = previous * appear;
      ui.setLayer(9);
      ui.target("color_catcher", -100000.0F, -100000.0F, 200000.0F, 200000.0F, MenuUi.click(ui::closePopups, ui::closePopups));
      ui.setLayer(10);
      ui.target(this.id + ":body", x, y, 176.0F, h, new MenuUi.Handler() {});
      MenuDraw.popup(ui, x, y, 176.0F, h, 12.0F);
      float cx = x + pad;
      float cy = y + pad;
      float cw = 176.0F - pad * 2.0F;
      int pure = ColorUtil.hsb(this.hue, 1.0F, 1.0F, 255);
      Surfaces.fill(ui.g, cx, cy, cw, squareH, Corners.round(4.0F), -1, pure, pure, -1, ui.alpha);
      Surfaces.fill(ui.g, cx, cy, cw, squareH, Corners.round(4.0F), 0, 0, -16777216, -16777216, ui.alpha);
      float px = cx + this.saturation * cw;
      float py = cy + (1.0F - this.value) * squareH;
      MenuDraw.circle(ui, px, py, 4.2F, -1);
      MenuDraw.circle(ui, px, py, 3.0F, ColorUtil.withAlpha(this.color(), 255));
      ui.target(this.id + ":square", cx, cy, cw, squareH, drag((lx, ly) -> {
         this.saturation = Math.clamp(lx / cw, 0.0F, 1.0F);
         this.value = 1.0F - Math.clamp(ly / squareH, 0.0F, 1.0F);
         this.changed();
      }));
      cy += squareH + 6.0F;
      float segment = cw / 6.0F;

      for (int i = 0; i < 6; i++) {
         int a = ColorUtil.hsb(i / 6.0F, 1.0F, 1.0F, 255);
         int b = ColorUtil.hsb((i + 1) / 6.0F, 1.0F, 1.0F, 255);
         Surfaces.fill(ui.g, cx + i * segment - (i > 0 ? 0.5F : 0.0F), cy, segment + (i > 0 ? 0.5F : 0.0F), 8.0F, Corners.NONE, a, b, b, a, ui.alpha);
      }

      float hueX = cx + this.hue * cw;
      MenuDraw.rect(ui, hueX - 1.5F, cy - 1.5F, 3.0F, 11.0F, 1.5F, -1);
      ui.target(this.id + ":hue", cx, cy - 2.0F, cw, 12.0F, drag((lx, ly) -> {
         this.hue = Math.clamp(lx / cw, 0.0F, 0.999F);
         this.changed();
      }));
      cy += 8.0F;
      if (this.hasAlpha) {
         cy += 6.0F;

         for (int i = 0; i < (int)(cw / 4.0F); i++) {
            for (int j = 0; j < 2; j++) {
               if ((i + j) % 2 == 0) {
                  MenuDraw.rect(ui, cx + i * 4, cy + j * 4, 4.0F, 4.0F, 0.0F, -6645094);
               }
            }
         }

         int opaque = ColorUtil.withAlpha(this.color(), 255);
         int clear = ColorUtil.withAlpha(this.color(), 0);
         Surfaces.fill(ui.g, cx, cy, cw, 8.0F, Corners.NONE, clear, opaque, opaque, clear, ui.alpha);
         float alphaX = cx + this.alpha * cw;
         MenuDraw.rect(ui, alphaX - 1.5F, cy - 1.5F, 3.0F, 11.0F, 1.5F, -1);
         ui.target(this.id + ":alpha", cx, cy - 2.0F, cw, 12.0F, drag((lx, ly) -> {
            this.alpha = Math.clamp(lx / cw, 0.0F, 1.0F);
            this.changed();
         }));
         cy += 8.0F;
      }

      cy += 8.0F;
      float previewSize = 18.0F;
      MenuDraw.rect(ui, cx, cy, previewSize, previewSize, 4.0F, ColorUtil.withAlpha(this.color(), 255));
      MenuDraw.border(ui, cx, cy, previewSize, previewSize, 4.0F, 1.0F, p.border());
      float fieldX = cx + previewSize + 6.0F;
      float fieldW = this.rainbowGet != null ? 62.0F : cw - previewSize - 6.0F;
      MenuWidgets.textField(ui, this.id + ":hex", fieldX, cy, fieldW, this.hex, "#FFFFFF", null, this::applyHex);
      if (this.rainbowGet != null) {
         boolean on = this.rainbowGet.getAsBoolean();
         float tx = x + 176.0F - pad - 30.0F;
         ui.textCentered(
            ui.medium, I18n.get("rose.menu.rainbow", new Object[0]), fieldX + fieldW + 6.0F, cy + previewSize / 2.0F, 7.0F, p.textDim()
         );
         MenuWidgets.toggle(ui, this.id + ":rainbow", tx, cy + (previewSize - 17.0F) / 2.0F, on, () -> this.rainbowSet.accept(!on));
      }

      cy += previewSize + 8.0F;
      int[] swatches = new int[PRESETS.length + 2];
      swatches[0] = Theme.accent();
      swatches[1] = Theme.accent2();
      System.arraycopy(PRESETS, 0, swatches, 2, PRESETS.length);
      float swatch = Math.min(12.0F, (cw - (swatches.length - 1) * 2) / swatches.length);

      for (int i = 0; i < swatches.length; i++) {
         float sx = cx + i * (swatch + 2.0F);
         int color = swatches[i];
         String swatchId = this.id + ":preset:" + i;
         MenuDraw.rect(ui, sx, cy, swatch, swatch, 3.0F, color);
         if (ui.hot(swatchId)) {
            MenuDraw.border(ui, sx - 1.0F, cy - 1.0F, swatch + 2.0F, swatch + 2.0F, 4.0F, 1.0F, p.text());
         }

         ui.target(swatchId, sx, cy, swatch, swatch, MenuUi.click(() -> {
            float keepAlpha = this.alpha;
            this.load(color);
            if (this.hasAlpha) {
               this.alpha = keepAlpha;
            }

            this.changed();
            UiSounds.play(UiSounds.Sound.CLICK);
         }));
      }

      ui.setLayer(0);
      ui.alpha = previous;
   }

   private void applyHex() {
      String text = this.hex.text().trim().replace("#", "");

      try {
         long parsed = Long.parseLong(text, 16);
         int color = text.length() <= 6 ? (int)parsed | 0xFF000000 : (int)parsed;
         this.load(color);
         this.changed();
      } catch (NumberFormatException ignored) {
         this.changed();
      }
   }

   private static MenuUi.Handler drag(ColorPopup.Move move) {
      return new MenuUi.Handler() {
         @Override
         public void press(int button, float localX, float localY) {
            move.move(localX, localY);
         }

         @Override
         public void drag(float localX, float localY) {
            move.move(localX, localY);
         }
      };
   }

   private interface Move {
      void move(float var1, float var2);
   }
}
