package dev.rose.module.impl.visual;

import dev.rose.anim.Spring;
import dev.rose.hud.TargetTracker;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;


public final class CrosshairModule extends Module {
   private static final long HIT_MS = 320L;
   private final ChoiceSetting<CrosshairModule.Style> style = this.add(new ChoiceSetting<>("style", CrosshairModule.Style.CROSS_DOT));
   private final ColorSetting color = this.add(new ColorSetting("color", -1, true));
   private final BoolSetting outline = this.add(new BoolSetting("outline", true));
   private final NumberSetting length = this.add(new NumberSetting("length", 4.0, 1.0, 12.0, 0.5, "rose.unit.pixels"));
   private final NumberSetting gap = this.add(new NumberSetting("gap", 2.5, 0.0, 10.0, 0.5, "rose.unit.pixels"));
   private final NumberSetting thickness = this.add(new NumberSetting("thickness", 1.0, 0.5, 3.0, 0.25, "rose.unit.pixels"));
   private final BoolSetting dynamic = this.add(new BoolSetting("dynamic", true));
   private final BoolSetting hitMarker = this.add(new BoolSetting("hit_marker", true));
   private final ColorSetting hitColor = this.add(new ColorSetting("hit_color", -47814, false));
   private final Spring spread = new Spring(0.0, 0.18, 0.8);

   public CrosshairModule() {
      super("crosshair", Category.VISUALS, false);
      this.hitColor.visibleWhen(this.hitMarker::isOn);
   }

   @Override
   public int icon() {
      return 57516;
   }

   public void render(GuiGraphics g) {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      float cx = g.guiWidth() / 2.0F;
      float cy = g.guiHeight() / 2.0F;
      float extra = 0.0F;
      if (this.dynamic.isOn() && player != null) {
         double speed = player.getDeltaMovement().horizontalDistance();
         extra = (float)Math.min(4.0, speed * 12.0) + (player.onGround() ? 0.0F : 1.5F) + (1.0F - player.getAttackStrengthScale(0.0F)) * 2.5F;
      }

      float open = this.gap.floatValue() + Math.max(0.0F, this.spread.setTarget(extra).getFloat());
      int main = this.color.color();
      float t = this.thickness.floatValue();
      float len = this.length.floatValue();
      switch ((CrosshairModule.Style)this.style.get()) {
         case CROSS:
         case CROSS_DOT:
         case T_SHAPE:
            if (this.style.get() != CrosshairModule.Style.T_SHAPE) {
               this.bar(g, cx - t / 2.0F, cy - open - len, t, len, main);
            }

            this.bar(g, cx - t / 2.0F, cy + open, t, len, main);
            this.bar(g, cx - open - len, cy - t / 2.0F, len, t, main);
            this.bar(g, cx + open, cy - t / 2.0F, len, t, main);
            if (this.style.get() == CrosshairModule.Style.CROSS_DOT) {
               this.dot(g, cx, cy, t * 0.9F + 0.4F, main);
            }
            break;
         case DOT:
            this.dot(g, cx, cy, Math.max(1.2F, t * 1.4F), main);
            break;
         case CIRCLE:
            float radius = open + len * 0.6F;
            if (this.outline.isOn()) {
               Surfaces.border(
                  g,
                  cx - radius - 0.5F,
                  cy - radius - 0.5F,
                  radius * 2.0F + 1.0F,
                  radius * 2.0F + 1.0F,
                  Corners.round(radius + 0.5F),
                  t + 1.0F,
                  -1879048192,
                  1.0F
               );
            }

            Surfaces.border(g, cx - radius, cy - radius, radius * 2.0F, radius * 2.0F, Corners.round(radius), t, main, 1.0F);
            this.dot(g, cx, cy, t * 0.8F, main);
      }

      long since = System.currentTimeMillis() - TargetTracker.lastAttackMillis();
      if (this.hitMarker.isOn() && since < 320L) {
         float p = (float)since / 320.0F;
         float alpha = 1.0F - p * p;
         float inner = open + 2.0F + p * 3.0F;
         float size = 3.2F;
         int hit = ColorUtil.fade(this.hitColor.color(), alpha);
         g.pose().pushMatrix();
         g.pose().translate(cx, cy);

         for (int i = 0; i < 4; i++) {
            g.pose().pushMatrix();
            g.pose().rotate((float)Math.toRadians(45 + 90 * i));
            this.bar(g, inner, -0.6F, size, 1.2F, hit);
            g.pose().popMatrix();
         }

         g.pose().popMatrix();
      }
   }

   private void bar(GuiGraphics g, float x, float y, float w, float h, int color) {
      if (this.outline.isOn()) {
         Surfaces.fill(
            g, x - 0.5F, y - 0.5F, w + 1.0F, h + 1.0F, Corners.round(0.5F), -1879048192, -1879048192, -1879048192, -1879048192, ColorUtil.alpha(color) / 255.0F
         );
      }

      Surfaces.fill(g, x, y, w, h, Corners.NONE, color, color, color, color, 1.0F);
   }

   private void dot(GuiGraphics g, float cx, float cy, float size, int color) {
      float r = size / 2.0F;
      if (this.outline.isOn()) {
         Surfaces.fill(
            g,
            cx - r - 0.5F,
            cy - r - 0.5F,
            size + 1.0F,
            size + 1.0F,
            Corners.round(r + 0.5F),
            -1879048192,
            -1879048192,
            -1879048192,
            -1879048192,
            ColorUtil.alpha(color) / 255.0F
         );
      }

      Surfaces.fill(g, cx - r, cy - r, size, size, Corners.round(r), color, color, color, color, 1.0F);
   }

   public enum Style {
      CROSS,
      CROSS_DOT,
      DOT,
      CIRCLE,
      T_SHAPE;
   }
}
