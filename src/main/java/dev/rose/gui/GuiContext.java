package dev.rose.gui;

import dev.rose.anim.Easing;
import dev.rose.anim.Spring;
import dev.rose.module.Modules;
import dev.rose.module.impl.ClickGuiModule;
import dev.rose.render.LiquidGlass;
import dev.rose.render.RippleField;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.KeySetting;
import dev.rose.setting.Setting;
import dev.rose.ui.LiquidSlider;
import dev.rose.ui.LiquidToggle;
import dev.rose.ui.SegmentedControl;
import dev.rose.util.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;


final class GuiContext {
   private static final long STAGGER_MS = 30L;
   private static final long TOOLTIP_DELAY_MS = 480L;
   private static final float EDGE_FADE = 16.0F;
   final List<GuiContext.Target> targets = new ArrayList<>();
   final RippleField ripples = new RippleField();
   GuiGraphics g;
   SdfFont regular;
   SdfFont medium;
   SdfFont semibold;
   SdfFont bold;
   SdfFont display;
   SdfFont displayBold;
   SdfFont displayBlack;
   SdfFont icons;
   Glass glass;
   int label;
   float mouseX;
   float mouseY;
   boolean mouseDown;
   float alpha;
   float originX;
   float originY;
   float scale;
   float motion = 1.0F;
   float canvasW = 640.0F;
   float canvasH = 360.0F;
   boolean interactive = true;
   Object pressed;
   String activeId;
   TextInput focusedInput;
   Object focusedOwner;
   String focusedTargetId;
   private Runnable onInputChanged;
   KeySetting listeningKey;
   ColorSetting expandedColor;
   Setting<?> highlightedSetting;
   long highlightStart;
   float highlightOffset = Float.NaN;
   private final Deque<float[]> clips = new ArrayDeque<>();
   private final Map<Object, LiquidToggle> toggles = new HashMap<>();
   private final Map<Object, LiquidSlider> sliders = new HashMap<>();
   private final Map<Object, SegmentedControl> segments = new HashMap<>();
   private final Map<Object, String[]> segmentLabels = new HashMap<>();
   private final Map<Object, Spring> springs = new HashMap<>();
   private final Map<ColorSetting, float[]> hsb = new HashMap<>();
   private final Map<ColorSetting, Integer> knownColors = new HashMap<>();
   private final Map<String, Long> staggerStarts = new HashMap<>();
   private final Map<String, Spring> appearing = new HashMap<>();
   private Runnable onInputSubmit;
   private float viewTop = Float.NEGATIVE_INFINITY;
   private float viewBottom = Float.POSITIVE_INFINITY;
   private final Spring tooltipSpring = new Spring(0.0, 0.22, 0.78);
   private String tooltipCandidate;
   private String tooltipCandidateText;
   private float[] tooltipCandidateBox;
   private String tooltipShown;
   private String tooltipText = "";
   private float[] tooltipBox;
   private long tooltipSince;

   static GuiContext.Interaction click(Runnable action) {
      return new GuiContext.Interaction() {
         @Override
         public void release(float localX, float localY, boolean inside) {
            if (inside) {
               action.run();
            }
         }
      };
   }

   static GuiContext.Interaction drag(GuiContext.PointerMove move) {
      return new GuiContext.Interaction() {
         @Override
         public void press(float localX, float localY) {
            move.move(localX, localY);
         }

         @Override
         public void drag(float localX, float localY) {
            move.move(localX, localY);
         }
      };
   }

   boolean begin(GuiGraphics graphics, float windowAlpha) {
      this.g = graphics;
      this.alpha = windowAlpha;
      this.interactive = true;
      this.targets.clear();
      this.clips.clear();
      this.tooltipCandidate = null;
      this.clearViewport();
      this.regular = Fonts.get(FontWeight.REGULAR);
      this.medium = Fonts.get(FontWeight.MEDIUM);
      this.semibold = Fonts.get(FontWeight.SEMIBOLD);
      this.bold = Fonts.get(FontWeight.BOLD);
      this.display = Fonts.get(FontWeight.DISPLAY);
      this.displayBold = Fonts.get(FontWeight.DISPLAY_BOLD);
      this.displayBlack = Fonts.get(FontWeight.DISPLAY_BLACK);
      this.icons = Fonts.icons();
      this.glass = Theme.glass();
      this.label = this.glass.label;
      ClickGuiModule settings = Modules.clickGui();
      this.motion = settings.reduceMotion() ? 0.15F : 1.0F;
      return this.regular != null
         && this.medium != null
         && this.semibold != null
         && this.bold != null
         && this.display != null
         && this.displayBold != null
         && this.displayBlack != null;
   }

   boolean darkGlass() {
      return this.glass.labelBrightness() > 0.5F;
   }

   void target(String id, float x, float y, float w, float h, GuiContext.Interaction interaction) {
      if (this.interactive) {
         float x0 = x;
         float y0 = y;
         float x1 = x + w;
         float y1 = y + h;
         float[] clip = this.clips.peek();
         if (clip != null) {
            x0 = Math.max(x0, clip[0]);
            y0 = Math.max(y0, clip[1]);
            x1 = Math.min(x1, clip[2]);
            y1 = Math.min(y1, clip[3]);
            if (x1 <= x0 || y1 <= y0) {
               return;
            }
         }

         this.targets.add(new GuiContext.Target(id, x0, y0, x1 - x0, y1 - y0, x, y, interaction));
      }
   }

   GuiContext.Target targetAt(float x, float y) {
      for (int i = this.targets.size() - 1; i >= 0; i--) {
         if (this.targets.get(i).contains(x, y)) {
            return this.targets.get(i);
         }
      }

      return null;
   }

   boolean secondaryAt(float x, float y, int button) {
      for (int i = this.targets.size() - 1; i >= 0; i--) {
         GuiContext.Target target = this.targets.get(i);
         if (target.contains(x, y) && target.interaction().secondary(x - target.originX(), y - target.originY(), button)) {
            return true;
         }
      }

      return false;
   }

   GuiContext.Target targetById(String id) {
      for (int i = this.targets.size() - 1; i >= 0; i--) {
         if (this.targets.get(i).id().equals(id)) {
            return this.targets.get(i);
         }
      }

      return null;
   }

   boolean hovered(float x, float y, float w, float h) {
      if (this.interactive && !(this.mouseX < x) && !(this.mouseY < y) && !(this.mouseX >= x + w) && !(this.mouseY >= y + h)) {
         float[] clip = this.clips.peek();
         return clip == null || this.mouseX >= clip[0] && this.mouseY >= clip[1] && this.mouseX < clip[2] && this.mouseY < clip[3];
      } else {
         return false;
      }
   }

   void clip(float x, float y, float w, float h) {
      float[] rect = new float[]{x, y, x + w, y + h};
      float[] top = this.clips.peek();
      if (top != null) {
         rect = new float[]{Math.max(rect[0], top[0]), Math.max(rect[1], top[1]), Math.min(rect[2], top[2]), Math.min(rect[3], top[3])};
      }

      this.clips.push(rect);
      GuiDraw.scissor(
         this.g,
         (int)Math.floor(this.originX + rect[0] * this.scale),
         (int)Math.floor(this.originY + rect[1] * this.scale),
         (int)Math.ceil(this.originX + rect[2] * this.scale),
         (int)Math.ceil(this.originY + rect[3] * this.scale)
      );
   }

   void unclip() {
      this.clips.pop();
      this.g.disableScissor();
   }

   void focus(Object owner, String targetId, TextInput input, Runnable onChange) {
      this.focus(owner, targetId, input, onChange, null);
   }

   void focus(Object owner, String targetId, TextInput input, Runnable onChange, Runnable onSubmit) {
      this.focusedOwner = owner;
      this.focusedTargetId = targetId;
      this.focusedInput = input;
      this.onInputChanged = onChange;
      this.onInputSubmit = onSubmit;
      this.listeningKey = null;
   }

   void unfocus() {
      this.focusedOwner = null;
      this.focusedTargetId = null;
      this.focusedInput = null;
      this.onInputChanged = null;
      this.onInputSubmit = null;
   }

   void inputChanged() {
      if (this.onInputChanged != null) {
         this.onInputChanged.run();
      }
   }

   void inputSubmitted() {
      Runnable submit = this.onInputSubmit;
      this.unfocus();
      if (submit != null) {
         submit.run();
      }
   }

   void viewport(float top, float bottom) {
      this.viewTop = top;
      this.viewBottom = bottom;
   }

   void clearViewport() {
      this.viewTop = Float.NEGATIVE_INFINITY;
      this.viewBottom = Float.POSITIVE_INFINITY;
   }

   float edgeFade(float y, float h) {
      float center = y + h * 0.5F;
      float top = Math.clamp((center - this.viewTop) / 16.0F, 0.0F, 1.0F);
      float bottom = Math.clamp((this.viewBottom - center) / 16.0F, 0.0F, 1.0F);
      return Math.min(top, bottom);
   }

   void tooltip(String id, float x, float y, float w, float h, String text) {
      if (text != null && !text.isEmpty() && this.hovered(x, y, w, h)) {
         this.tooltipCandidate = id;
         this.tooltipCandidateText = text;
         this.tooltipCandidateBox = new float[]{x, y, w, h};
      }
   }

   void drawTooltip(boolean allowed) {
      long now = System.currentTimeMillis();
      if (!Objects.equals(this.tooltipCandidate, this.tooltipShown)) {
         this.tooltipShown = this.tooltipCandidate;
         this.tooltipSince = now;
         if (this.tooltipCandidate != null) {
            this.tooltipText = this.tooltipCandidateText;
            this.tooltipBox = this.tooltipCandidateBox;
         }
      }

      boolean show = allowed && this.tooltipShown != null && !this.mouseDown && now - this.tooltipSince > 480L;
      float t = this.tooltipSpring.setTarget(show ? 1.0 : 0.0).getFloat();
      if (!(t <= 0.01F) && !this.tooltipText.isEmpty() && this.tooltipBox != null) {
         float size = 6.8F;
         float lineH = size * 1.45F;
         List<String> lines = wrap(this.regular, this.tooltipText, size, 176.0F);
         float textW = 0.0F;

         for (String line : lines) {
            textW = Math.max(textW, this.regular.width(line, size));
         }

         float w = textW + 18.0F;
         float h = lines.size() * lineH + 11.0F;
         float anchorX = this.tooltipBox[0] + this.tooltipBox[2] / 2.0F;
         boolean below = this.tooltipBox[1] + this.tooltipBox[3] + 8.0F + h < this.canvasH - 4.0F;
         float x = Math.clamp(anchorX - w / 2.0F, 4.0F, this.canvasW - w - 4.0F);
         float y = below ? this.tooltipBox[1] + this.tooltipBox[3] + 7.0F : this.tooltipBox[1] - 7.0F - h;
         float grow = Math.max(0.0F, t);
         float pivotY = below ? y : y + h;
         this.g.pose().pushMatrix();
         this.g.pose().translate(anchorX, pivotY);
         this.g.pose().scale(0.85F + 0.15F * Math.min(1.1F, grow), 0.85F + 0.15F * Math.min(1.1F, grow));
         this.g.pose().translate(-anchorX, -pivotY);
         Glass bubble = this.glass.copy();
         bubble.tint = Paint.solid(this.darkGlass() ? -703590380 : -419956742);
         bubble.refraction = 5.0F;
         float visible = Math.clamp(t, 0.0F, 1.0F);
         LiquidGlass.shapes().add(x, y, w, h, Corners.themed(9.0F)).refreshBackdrop().draw(this.g, bubble, 0.0F, visible);

         for (int i = 0; i < lines.size(); i++) {
            this.regular.draw(this.g, lines.get(i), x + 9.0F, y + 6.0F + i * lineH, size, ColorUtil.fade(this.label, 0.92F * visible));
         }

         this.g.pose().popMatrix();
      }
   }

   static List<String> wrap(SdfFont font, String text, float size, float maxWidth) {
      List<String> lines = new ArrayList<>();

      for (String paragraph : text.split("\n")) {
         StringBuilder line = new StringBuilder();

         for (String word : paragraph.split(" ")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (!line.isEmpty() && font.width(candidate, size) > maxWidth) {
               lines.add(line.toString());
               line = new StringBuilder(word);
            } else {
               line = new StringBuilder(candidate);
            }
         }

         lines.add(line.toString());
      }

      return lines;
   }

   LiquidToggle toggle(Object key, boolean on) {
      LiquidToggle toggle = this.toggles.computeIfAbsent(key, k -> new LiquidToggle(on));
      toggle.set(on);
      return toggle;
   }

   LiquidSlider slider(Object key, float value) {
      LiquidSlider slider = this.sliders.computeIfAbsent(key, k -> new LiquidSlider(value));
      slider.set(value);
      return slider;
   }

   SegmentedControl segmented(Object key, String[] labels, int selected) {
      if (!Arrays.equals(this.segmentLabels.get(key), labels)) {
         SegmentedControl control = new SegmentedControl(labels);
         control.select(selected);
         this.segments.put(key, control);
         this.segmentLabels.put(key, labels);
      }

      SegmentedControl control = this.segments.get(key);
      control.select(selected);
      return control;
   }

   Spring spring(Object key, double initial, double response, double damping) {
      return this.springs.computeIfAbsent(key, k -> new Spring(initial, response, damping));
   }

   float animate(Object key, float target, double response, double damping) {
      return Math.clamp(this.spring(key, target, response, damping).setTarget(target).getFloat(), 0.0F, 1.0F);
   }

   float bounce(Object key, float target, double response, double damping) {
      return this.spring(key, target, response, damping).setTarget(target).getFloat();
   }

   float[] hsb(ColorSetting setting) {
      int color = setting.get();
      float[] state = this.hsb.get(setting);
      Integer known = this.knownColors.get(setting);
      if (state == null || known == null || known != color) {
         float[] parts = ColorUtil.toHsb(color);
         state = new float[]{parts[0], parts[1], parts[2], ColorUtil.alpha(color) / 255.0F};
         this.hsb.put(setting, state);
         this.knownColors.put(setting, color);
      }

      return state;
   }

   void rememberColor(ColorSetting setting, int color) {
      this.knownColors.put(setting, color);
   }

   void restartStagger(String group) {
      this.staggerStarts.put(group, System.currentTimeMillis());
      String prefix = group + "|";
      this.appearing.keySet().removeIf(key -> key.startsWith(prefix));
   }

   float appear(String group, String key, int index) {
      Spring spring = this.appearing.computeIfAbsent(group + "|" + key, k -> new Spring(0.0, 0.44, 0.74));
      long start = this.staggerStarts.getOrDefault(group, 0L);
      if (System.currentTimeMillis() - start >= index * 30L) {
         spring.setTarget(1.0);
      }

      return spring.getFloat();
   }

   float text(SdfFont font, String text, float x, float y, float size, int color) {
      return font.draw(this.g, text, x, y, size, ColorUtil.fade(color, this.alpha));
   }

   float text(SdfFont font, String text, float x, float y, float size, float tracking, int color) {
      return font.draw(this.g, text, x, y, size, tracking, ColorUtil.fade(color, this.alpha));
   }

   void icon(int codepoint, float centerX, float centerY, float size, int color) {
      if (this.icons != null && codepoint != 0) {
         this.icons.drawIcon(this.g, codepoint, centerX, centerY, size, ColorUtil.fade(color, this.alpha));
      }
   }

   void waveText(SdfFont font, String from, String to, long changedAt, float x, float y, float size, float tracking, int color) {
      float t = (float)(System.currentTimeMillis() - changedAt) / 1000.0F;
      float travel = size * 0.7F * this.motion;
      if (!from.isEmpty() && t < 0.5F) {
         font.drawAnimated(this.g, from, x, y, size, tracking, ColorUtil.fade(color, this.alpha), (index, count, glyph) -> {
            float p = (float)Easing.CUBIC_OUT.apply((t - index * 0.016F) / 0.22F);
            glyph.offsetY = -travel * p;
            glyph.color = ColorUtil.fade(glyph.color, 1.0F - p);
         });
      }

      font.drawAnimated(this.g, to, x, y, size, tracking, ColorUtil.fade(color, this.alpha), (index, count, glyph) -> {
         float local = (t - 0.07F - index * 0.024F) / 0.46F;
         float p = (float)Easing.BACK_OUT.apply(local);
         float fade = Math.clamp(local * 2.2F, 0.0F, 1.0F);
         glyph.offsetY = travel * 1.3F * (1.0F - p);
         glyph.rotation = (1.0F - Math.min(1.0F, p)) * 0.28F * this.motion;
         glyph.color = ColorUtil.fade(glyph.color, fade);
      });
   }

   float odometer(Object key, SdfFont font, String text, float x, float y, float size, int color, boolean alignRight) {
      float digitWidth = font.width("0", size);
      float width = 0.0F;

      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         width += Character.isDigit(c) ? digitWidth : font.width(String.valueOf(c), size);
      }

      float cursor = alignRight ? x - width : x;
      float line = size * 1.05F;
      int slot = 0;

      for (int i = text.length() - 1; i >= 0; i--) {
         char c = text.charAt(i);
         if (Character.isDigit(c)) {
            slot++;
         }
      }

      int digitIndex = 0;

      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         if (!Character.isDigit(c)) {
            cursor += this.text(font, String.valueOf(c), cursor, y, size, color);
         } else {
            int fromRight = slot - digitIndex++;
            Spring roll = this.spring(key + "#" + fromRight, c - '0', 0.32, 0.78);
            float current = roll.getFloat();
            double target = c - '0';
            double wrapped = target + Math.round((current - target) / 10.0) * 10.0;
            roll.setTarget(wrapped);
            double base = Math.floor(current);
            float frac = (float)(current - base);
            int d0 = Math.floorMod((long)base, 10);
            int d1 = Math.floorMod((long)base + 1L, 10);
            float offset = frac * line * this.motion;
            String first = String.valueOf(d0);
            String second = String.valueOf(d1);
            float centerX = cursor + (digitWidth - font.width(first, size)) / 2.0F;
            this.text(font, first, centerX, y - offset, size, ColorUtil.fade(color, 1.0F - frac));
            if (frac > 0.01F) {
               float secondX = cursor + (digitWidth - font.width(second, size)) / 2.0F;
               this.text(font, second, secondX, y + line - offset, size, ColorUtil.fade(color, frac));
            }

            cursor += digitWidth;
         }
      }

      return width;
   }

   static String ellipsize(SdfFont font, String text, float size, float maxWidth) {
      if (font.width(text, size) <= maxWidth) {
         return text;
      }

      String dots = "…";
      int end = text.length();

      while (end > 0 && font.width(text.substring(0, end) + dots, size) > maxWidth) {
         end--;
      }

      return text.substring(0, end).stripTrailing() + dots;
   }

   static String plural(String key, int count) {
      String language = Minecraft.getInstance().getLanguageManager().getSelected().toLowerCase(Locale.ROOT);
      boolean slavic = language.startsWith("ru") || language.startsWith("uk") || language.startsWith("be");
      String form;
      if (slavic) {
         int mod10 = count % 10;
         int mod100 = count % 100;
         form = mod10 == 1 && mod100 != 11 ? "one" : (mod10 < 2 || mod10 > 4 || mod100 >= 12 && mod100 <= 14 ? "many" : "few");
      } else {
         form = count == 1 ? "one" : "many";
      }

      return I18n.get(key + "." + form, new Object[]{count});
   }

   int groupColor() {
      return this.darkGlass() ? 318767103 : 1308622847;
   }

   int track() {
      return ColorUtil.withAlpha(7895167, Math.round(56.0F + 56.0F * this.glass.labelBrightness()));
   }

   int accentText() {
      int accent = Theme.accent();
      if (this.darkGlass()) {
         return ColorUtil.lerp(accent, -1, 0.3F);
      }

      float luminance = (ColorUtil.red(accent) * 0.2126F + ColorUtil.green(accent) * 0.7152F + ColorUtil.blue(accent) * 0.0722F) / 255.0F;
      float darken = luminance > 0.3F ? Math.min((luminance - 0.3F) / luminance, 0.7F) : 0.0F;
      return ColorUtil.lerp(accent, -16777216, darken);
   }

   int secondary() {
      return ColorUtil.fade(this.label, 0.55F);
   }

   interface Interaction {
      default void press(float localX, float localY) {
      }

      default void drag(float localX, float localY) {
      }

      default void release(float localX, float localY, boolean inside) {
      }

      default boolean secondary(float localX, float localY, int button) {
         return false;
      }
   }

   interface PointerMove {
      void move(float var1, float var2);
   }

   record Target(String id, float x, float y, float w, float h, float originX, float originY, GuiContext.Interaction interaction) {
      boolean contains(float px, float py) {
         return px >= this.x && py >= this.y && px < this.x + this.w && py < this.y + this.h;
      }
   }
}
