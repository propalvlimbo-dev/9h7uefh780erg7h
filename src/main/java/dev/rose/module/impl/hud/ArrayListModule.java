package dev.rose.module.impl.hud;

import dev.rose.anim.Spring;
import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;


public final class ArrayListModule extends HudElement {
   private static final float LINE_H = 13.0F;
   private static final float TEXT = 7.0F;
   private static final float PAD_X = 6.0F;
   private static final float GAP = 1.5F;
   private static final float BAR_W = 2.0F;
   private final ChoiceSetting<ArrayListModule.Sort> sort = this.add(new ChoiceSetting<>("sort", ArrayListModule.Sort.WIDTH));
   private final ChoiceSetting<ArrayListModule.Coloring> coloring = this.add(new ChoiceSetting<>("coloring", ArrayListModule.Coloring.GRADIENT));
   private final BoolSetting separate = this.add(new BoolSetting("separate", true));
   private final BoolSetting bar = this.add(new BoolSetting("bar", true));
   private final BoolSetting hudModules = this.add(new BoolSetting("hud_modules", false));
   private final Map<Object, ArrayListModule.Line> lines = new HashMap<>();
   private List<ArrayListModule.Line> shown = List.of();

   public ArrayListModule() {
      super("array_list", false, Anchor.TOP_RIGHT, -6.0F, 6.0F);
   }

   @Override
   public int icon() {
      return 57809;
   }

   @Override
   protected boolean drawsBackground() {
      return !this.separate.isOn();
   }

   @Override
   protected float defaultRoundness() {
      return 0.35F;
   }

   private boolean listed(Module module) {
      return module != this && !module.isAlwaysOn() && module.isEnabled() ? this.hudModules.isOn() || !(module instanceof HudElement) : false;
   }

   private void update(HudContext hud) {
      List<Object> active = new ArrayList<>();

      for (Module module : Modules.all()) {
         if (this.listed(module)) {
            active.add(module);
         }
      }

      if (hud.preview && active.size() < 3) {
         for (String sample : I18n.get("rose.hud.array_list.samples", new Object[0]).split("\\|")) {
            active.add(sample);
         }
      }

      for (Object key : active) {
         this.lines.computeIfAbsent(key, k -> new ArrayListModule.Line(k, k instanceof Module module ? module.name() : (String)k, new Spring(0.0, 0.34, 0.8)));
      }

      List<ArrayListModule.Line> all = new ArrayList<>();

      for (ArrayListModule.Line line : this.lines.values()) {
         boolean on = active.contains(line.key());
         line.presence().setTarget(on ? 1.0 : 0.0);
         if (on || !(line.presence().getFloat() < 0.01F)) {
            all.add(line);
         }
      }

      this.lines.values().removeIf(line -> !active.contains(line.key()) && line.presence().getFloat() < 0.01F);
      Comparator<ArrayListModule.Line> order = this.sort.get() == ArrayListModule.Sort.WIDTH
         ? Comparator.<ArrayListModule.Line>comparingDouble(line -> -hud.medium.width(line.name(), 7.0F)).thenComparing(ArrayListModule.Line::name)
         : Comparator.comparing(ArrayListModule.Line::name, String.CASE_INSENSITIVE_ORDER);
      all.sort(order);
      this.shown = all;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      for (Module module : Modules.all()) {
         if (this.listed(module)) {
            return true;
         }
      }

      return !this.shown.isEmpty();
   }

   @Override
   protected Size measure(HudContext hud) {
      this.update(hud);
      float width = 0.0F;
      float height = 0.0F;

      for (ArrayListModule.Line line : this.shown) {
         float p = Math.clamp(line.presence().getFloat(), 0.0F, 1.0F);
         width = Math.max(width, this.lineWidth(hud, line));
         height += 14.5F * p;
      }

      return new Size(Math.max(width, 20.0F), Math.max(height - 1.5F, 13.0F));
   }

   private float lineWidth(HudContext hud, ArrayListModule.Line line) {
      return 12.0F + hud.medium.width(line.name(), 7.0F) + (this.bar.isOn() ? 4.0F : 0.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      boolean right = hud.alignRight();
      boolean bottom = hud.alignBottom();
      float base = hud.alpha;
      int count = this.shown.size();
      float cursor = bottom ? h : 0.0F;

      for (int i = 0; i < count; i++) {
         ArrayListModule.Line line = this.shown.get(i);
         float appear = line.presence().getFloat();
         float p = Math.clamp(appear, 0.0F, 1.0F);
         if (!(p <= 0.005F)) {
            float lineW = this.lineWidth(hud, line);
            float slide = (1.0F - Math.min(appear, 1.0F)) * (lineW + 10.0F) * (right ? 1 : -1);
            float x = (right ? w - lineW : 0.0F) + slide;
            float step = 14.5F * p;
            float y = bottom ? cursor - 13.0F : cursor;
            cursor += bottom ? -step : step;
            int color = this.color(i, count);
            hud.alpha = base * p;
            if (this.separate.isOn()) {
               hud.background(x, y, lineW, 13.0F);
            }

            float textX = x + 6.0F + (this.bar.isOn() && !right ? 4.0F : 0.0F);
            hud.text(
               hud.medium,
               line.name(),
               textX,
               HudContext.centered(y, 13.0F, 7.0F),
               7.0F,
               this.coloring.get() == ArrayListModule.Coloring.WHITE ? hud.label : color
            );
            if (this.bar.isOn()) {
               float barX = right ? x + lineW - 2.0F - 2.0F : x + 2.0F;
               hud.fill(barX, y + 3.0F, 2.0F, 7.0F, 1.0F, color);
            }
         }
      }

      hud.alpha = base;
   }

   private int color(int index, int count) {
      int accent = Theme.accent();

      return switch ((ArrayListModule.Coloring)this.coloring.get()) {
         case ACCENT, WHITE -> accent;
         case GRADIENT -> {
            float t = count <= 1 ? 0.0F : (float)index / (count - 1);
            float wave = 0.5F + 0.5F * (float)Math.sin(Theme.time() * 1.6F - index * 0.45F);
            yield ColorUtil.lerp(accent, Theme.accent2(), Math.clamp(t * 0.7F + wave * 0.3F, 0.0F, 1.0F));
         }
      };
   }

   public enum Coloring {
      ACCENT,
      GRADIENT,
      WHITE;
   }

   private record Line(Object key, String name, Spring presence) {
   }

   public enum Sort {
      WIDTH,
      ALPHABET;
   }
}
