package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.setting.BoolSetting;
import dev.rose.util.ColorUtil;


public abstract class InfoElement extends HudElement {
   private static final float HEIGHT = 16.0F;
   private static final float PAD = 7.0F;
   private static final float VALUE = 7.4F;
   private static final float UNIT = 6.2F;
   private static final float GRAPH_W = 30.0F;
   private static final int SAMPLES = 30;
   private final BoolSetting showIcon = this.add(new BoolSetting("show_icon", true)).translatedAs("rose.hud.info");
   private final BoolSetting showGraph;
   private final float[] samples = new float[30];
   private int sampleHead;
   private int sampleCount;

   protected InfoElement(String id, Anchor anchor, float x, float y, boolean graph) {
      super(id, false, anchor, x, y);
      this.showGraph = graph ? this.add(new BoolSetting("graph", true)).translatedAs("rose.hud.info") : null;
   }

   protected abstract String value(HudContext var1);

   protected String unit(HudContext hud) {
      return "";
   }

   protected abstract int valueIcon();

   protected int tint(HudContext hud) {
      return hud.accent();
   }

   protected final void sample(float value) {
      this.samples[this.sampleHead] = value;
      this.sampleHead = (this.sampleHead + 1) % 30;
      this.sampleCount = Math.min(30, this.sampleCount + 1);
   }

   @Override
   protected float defaultRoundness() {
      return 1.0F;
   }

   private boolean graph() {
      return this.showGraph != null && this.showGraph.isOn();
   }

   @Override
   protected Size measure(HudContext hud) {
      String value = this.value(hud);
      String unit = this.unit(hud);
      float width = 7.0F + (this.showIcon.isOn() ? 12 : 0) + hud.semibold.width(value, 7.4F);
      if (!unit.isEmpty()) {
         width += 3.0F + hud.regular.width(unit, 6.2F);
      }

      if (this.graph()) {
         width += 36.0F;
      }

      return new Size(width + 7.0F, 16.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      int tint = this.tint(hud);
      float cursor = 7.0F;
      if (this.showIcon.isOn()) {
         hud.icon(this.valueIcon(), cursor + 4.5F, h / 2.0F, 8.5F, tint);
         cursor += 12.0F;
      }

      String value = this.value(hud);
      String unit = this.unit(hud);
      cursor += hud.text(hud.semibold, value, cursor, HudContext.centered(0.0F, h, 7.4F), 7.4F, hud.label);
      if (!unit.isEmpty()) {
         cursor += 3.0F;
         cursor += hud.text(hud.regular, unit, cursor, HudContext.centered(0.0F, h, 6.2F) + 0.4F, 6.2F, hud.secondary);
      }

      if (this.graph()) {
         this.drawGraph(hud, cursor + 6.0F, 4.0F, 30.0F, h - 8.0F, tint);
      }
   }

   private void drawGraph(HudContext hud, float x, float y, float w, float h, int color) {
      int count = hud.preview && this.sampleCount == 0 ? 30 : this.sampleCount;
      if (count >= 2) {
         float min = Float.MAX_VALUE;
         float max = -Float.MAX_VALUE;

         for (int i = 0; i < count; i++) {
            float value = this.sampleAt(hud, i, count);
            min = Math.min(min, value);
            max = Math.max(max, value);
         }

         boolean flat = max - min <= Math.max(0.001F, Math.abs(max) * 0.04F);
         float step = w / 29.0F;
         float previous = 0.0F;

         for (int i = 0; i < count; i++) {
            float value = this.sampleAt(hud, i, count);
            float t = flat ? (max > 0.0F ? 0.5F : 0.0F) : (value - min) / (max - min);
            float pointY = y + h - (0.1F + 0.8F * t) * h;
            float pointX = x + w - (count - 1 - i) * step;
            float fresh = 0.3F + 0.7F * (i + 1) / count;
            hud.quad(pointX - step / 2.0F, pointY, pointX + step / 2.0F, y + h, ColorUtil.fade(color, 0.16F * fresh));
            if (i > 0) {
               hud.quad(pointX - step, Math.min(previous, pointY) - 0.45F, pointX, Math.max(previous, pointY) + 0.45F, ColorUtil.fade(color, fresh));
            }

            previous = pointY;
         }
      }
   }

   private float sampleAt(HudContext hud, int i, int count) {
      if (hud.preview && this.sampleCount == 0) {
         return 0.55F + 0.3F * (float)Math.sin(i * 0.5F) + 0.15F * (float)Math.sin(i * 1.7F);
      }

      int index = Math.floorMod(this.sampleHead - count + i, 30);
      return this.samples[index];
   }
}
