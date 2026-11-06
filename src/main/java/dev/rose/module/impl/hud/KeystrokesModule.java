package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.GameStats;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;


public final class KeystrokesModule extends HudElement {
   private final NumberSetting keySize = this.add(new NumberSetting("key_size", 20.0, 14.0, 32.0, 1.0, "rose.unit.pixels"));
   private final NumberSetting gap = this.add(new NumberSetting("gap", 2.0, 0.0, 6.0, 0.5, "rose.unit.pixels"));
   private final BoolSetting mouse = this.add(new BoolSetting("mouse", true));
   private final BoolSetting space = this.add(new BoolSetting("space", true));
   private final BoolSetting cps = this.add(new BoolSetting("cps", true)).visibleWhen(this.mouse::isOn);
   private final BoolSetting arrows = this.add(new BoolSetting("arrows", false));

   public KeystrokesModule() {
      super("keystrokes", false, Anchor.BOTTOM_LEFT, 6.0F, -40.0F);
   }

   @Override
   public int icon() {
      return 57988;
   }

   @Override
   protected boolean drawsBackground() {
      return false;
   }

   @Override
   protected Size measure(HudContext hud) {
      float k = this.keySize.floatValue();
      float g = this.gap.floatValue();
      float width = k * 3.0F + g * 2.0F;
      float height = k * 2.0F + g;
      if (this.mouse.isOn()) {
         height += g + k;
      }

      if (this.space.isOn()) {
         height += g + k * 0.55F;
      }

      return new Size(width, height);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      Options options = hud.mc.options;
      float k = this.keySize.floatValue();
      float g = this.gap.floatValue();
      boolean useArrows = this.arrows.isOn();
      this.key(hud, "W", options.keyUp, useArrows ? "↑" : null, k + g, 0.0F, k, k);
      this.key(hud, "A", options.keyLeft, useArrows ? "←" : null, 0.0F, k + g, k, k);
      this.key(hud, "S", options.keyDown, useArrows ? "↓" : null, k + g, k + g, k, k);
      this.key(hud, "D", options.keyRight, useArrows ? "→" : null, (k + g) * 2.0F, k + g, k, k);
      float y = (k + g) * 2.0F;
      if (this.mouse.isOn()) {
         float half = (w - g) / 2.0F;
         String left = this.cps.isOn() ? GameStats.cps(true) + " CPS" : "LMB";
         String right = this.cps.isOn() ? GameStats.cps(false) + " CPS" : "RMB";
         this.key(hud, left, options.keyAttack, null, 0.0F, y, half, k);
         this.key(hud, right, options.keyUse, null, half + g, y, half, k);
         y += k + g;
      }

      if (this.space.isOn()) {
         this.key(hud, "", options.keyJump, null, 0.0F, y, w, k * 0.55F);
      }
   }

   private void key(HudContext hud, String label, KeyMapping mapping, String arrow, float x, float y, float w, float h) {
      boolean down = mapping.isDown();
      float press = hud.animate("keystrokes:" + mapping.getName(), down ? 1.0F : 0.0F, 0.18, 0.9);
      hud.background(x, y, w, h);
      if (press > 0.01F) {
         hud.fill(x, y, w, h, Math.min(w, h) * 0.3F, ColorUtil.withAlpha(hud.accent(), Math.round(216.0F * press)));
      }

      String text = arrow != null ? arrow : label;
      if (text.isEmpty()) {
         hud.fill(x + w * 0.3F, y + h / 2.0F - 0.75F, w * 0.4F, 1.5F, 0.75F, ColorUtil.lerp(hud.secondary, -1, press));
      } else {
         float size = Math.min(h * 0.38F, text.length() > 2 ? h * 0.3F : h * 0.4F);
         float textW = hud.semibold.width(text, size);
         hud.text(hud.semibold, text, x + (w - textW) / 2.0F, HudContext.centered(y, h, size), size, ColorUtil.lerp(hud.label, -1, press));
      }
   }
}
