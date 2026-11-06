package dev.rose.gui.menu;

import dev.rose.anim.Spring;
import dev.rose.gui.UiSounds;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.ui.LiquidSlider;
import dev.rose.ui.LiquidToggle;
import dev.rose.ui.SegmentedControl;
import dev.rose.util.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;


public final class MenuUi {
   private static final float EDGE_FADE = 14.0F;
   public GuiGraphics g;
   public float screenWidth = 640.0F;
   public float screenHeight = 360.0F;
   public TextBuffer focused;
   public String focusedId;
   public Runnable focusedChange;
   public Runnable focusedSubmit;
   public String listeningId;
   public IntConsumer listening;
   public String dropdownId;
   public float[] dropdownRect;
   public String[] dropdownLabels;
   public int dropdownSelected;
   public IntConsumer dropdownPick;
   public ColorPopup colorPopup;
   public float scale = 1.0F;
   public float mouseX = -1.0F;
   public float mouseY = -1.0F;
   public float alpha = 1.0F;
   public float motion = 1.0F;
   public boolean interactive = true;
   public MenuPalette palette;
   public SdfFont regular;
   public SdfFont medium;
   public SdfFont semibold;
   public SdfFont bold;
   public SdfFont display;
   public SdfFont icons;
   private List<MenuUi.Target> targets = new ArrayList<>();
   private List<MenuUi.Target> previous = new ArrayList<>();
   private final Deque<float[]> clips = new ArrayDeque<>();
   private final Map<String, MenuUi.Smooth> smooth = new HashMap<>();
   private final Map<String, MenuUi.Kept> kept = new HashMap<>();
   private final List<Runnable> overlays = new ArrayList<>();
   private int layer;
   private long frame;
   private boolean inFrame;
   private long lastNanos = System.nanoTime();
   private float delta;
   private String hotId;
   private String pressedId;
   private MenuUi.Target pressed;
   private String tooltipId;
   private String tooltipText;
   private long tooltipSince;
   private String tooltipCandidate;
   private String tooltipCandidateText;
   private float tooltipX;
   private float tooltipY;
   private float viewTop = Float.NEGATIVE_INFINITY;
   private float viewBottom = Float.POSITIVE_INFINITY;

   public static MenuUi.Handler click(Runnable left) {
      return click(left, null);
   }

   public static MenuUi.Handler click(Runnable left, Runnable right) {
      return new MenuUi.Handler() {
         private int button = -1;

         @Override
         public void press(int pressed, float localX, float localY) {
            this.button = pressed;
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            if (inside) {
               if (this.button == 0 && left != null) {
                  left.run();
               } else if (this.button == 1 && right != null) {
                  right.run();
               }
            }
         }
      };
   }

   public boolean begin(GuiGraphics graphics, float menuScale, float pointerX, float pointerY) {
      this.g = graphics;
      this.scale = menuScale;
      this.mouseX = pointerX;
      this.mouseY = pointerY;
      this.regular = Fonts.get(FontWeight.REGULAR);
      this.medium = Fonts.get(FontWeight.MEDIUM);
      this.semibold = Fonts.get(FontWeight.SEMIBOLD);
      this.bold = Fonts.get(FontWeight.BOLD);
      this.display = Fonts.get(FontWeight.DISPLAY_BOLD);
      this.icons = Fonts.icons();
      if (this.regular != null && this.medium != null && this.semibold != null && this.bold != null && this.display != null && this.icons != null) {
         this.palette = MenuPalette.current();
         long now = System.nanoTime();
         this.delta = Math.min(0.1F, (float)(now - this.lastNanos) / 1.0E9F);
         this.lastNanos = now;
         this.frame++;
         this.inFrame = true;
         this.previous = this.targets;
         this.targets = new ArrayList<>();
         this.clips.clear();
         this.overlays.clear();
         this.layer = 0;
         this.interactive = true;
         this.clearViewport();
         MenuUi.Target hot = this.topmost(pointerX, pointerY);
         this.hotId = this.pressed != null ? this.pressedId : (hot == null ? null : hot.id);
         this.tooltipCandidate = null;
         if (this.frame % 120L == 0L) {
            Iterator<MenuUi.Smooth> smoothIterator = this.smooth.values().iterator();

            while (smoothIterator.hasNext()) {
               if (this.frame - smoothIterator.next().frame > 600L) {
                  smoothIterator.remove();
               }
            }

            Iterator<MenuUi.Kept> keptIterator = this.kept.values().iterator();

            while (keptIterator.hasNext()) {
               if (this.frame - keptIterator.next().frame > 600L) {
                  keptIterator.remove();
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void end() {
      this.inFrame = false;
   }

   private List<MenuUi.Target> settled() {
      return this.inFrame ? this.previous : this.targets;
   }

   public MenuUi.Target find(String id) {
      for (MenuUi.Target target : this.settled()) {
         if (target.id.equals(id)) {
            return target;
         }
      }

      return null;
   }

   public String targetAt(float px, float py) {
      MenuUi.Target target = this.topmost(px, py);
      return target == null ? null : target.id;
   }

   public float delta() {
      return this.delta;
   }

   public void target(String id, float x, float y, float w, float h, MenuUi.Handler handler) {
      if (this.interactive) {
         float[] clip = this.clips.peek();
         if (clip == null) {
            clip = new float[]{-1000000.0F, -1000000.0F, 1000000.0F, 1000000.0F};
         }

         this.targets.add(new MenuUi.Target(id, x, y, w, h, clip[0], clip[1], clip[2], clip[3], this.layer, handler));
      }
   }

   public boolean hot(String id) {
      return id.equals(this.hotId);
   }

   public boolean pressed(String id) {
      return this.pressed != null && id.equals(this.pressedId);
   }

   public boolean inside(float x, float y, float w, float h) {
      float[] clip = this.clips.peek();
      return clip == null || !(this.mouseX < clip[0]) && !(this.mouseY < clip[1]) && !(this.mouseX >= clip[2]) && !(this.mouseY >= clip[3])
         ? this.mouseX >= x && this.mouseY >= y && this.mouseX < x + w && this.mouseY < y + h
         : false;
   }

   private MenuUi.Target topmost(float px, float py) {
      MenuUi.Target best = null;

      for (MenuUi.Target target : this.settled()) {
         if (target.contains(px, py) && (best == null || target.layer >= best.layer)) {
            best = target;
         }
      }

      return best;
   }

   public boolean press(float px, float py, int button) {
      MenuUi.Target target = this.topmost(px, py);
      if (target == null) {
         return false;
      }

      this.pressed = target;
      this.pressedId = target.id;
      target.handler.press(button, px - target.x, py - target.y);
      return true;
   }

   public void drag(float px, float py) {
      if (this.pressed != null) {
         this.pressed.handler.drag(px - this.pressed.x, py - this.pressed.y);
      }
   }

   public boolean release(float px, float py) {
      if (this.pressed == null) {
         return false;
      }

      MenuUi.Target target = this.pressed;
      this.pressed = null;
      this.pressedId = null;
      target.handler.release(px - target.x, py - target.y, target.contains(px, py));
      return true;
   }

   public boolean scroll(float px, float py, double amount) {
      MenuUi.Target best = null;

      for (MenuUi.Target target : this.settled()) {
         if (target.contains(px, py) && (best == null || target.layer >= best.layer) && target.handler.scroll(0.0)) {
            best = target;
         }
      }

      return best != null && best.handler.scroll(amount);
   }

   public boolean dragging() {
      return this.pressed != null;
   }

   public String activeId() {
      return this.pressed != null ? this.pressedId : null;
   }

   public void blurOutside(String under) {
      if (this.listening != null && (under == null || !under.equals(this.listeningId))) {
         this.listening = null;
         this.listeningId = null;
      }

      if (this.focused != null && (under == null || !under.equals(this.focusedId))) {
         if (this.focusedId != null && this.focusedId.endsWith(":edit") && this.focusedSubmit != null) {
            this.focusedSubmit.run();
         } else {
            this.unfocus();
         }
      }
   }

   public boolean keyPressed(int key, int modifiers) {
      if (this.listening == null) {
         if (this.focused == null) {
            return false;
         }

         switch (key) {
            case 256:
               this.unfocus();
               break;
            case 257:
            case 335:
               if (this.focusedSubmit != null) {
                  this.focusedSubmit.run();
               } else {
                  this.unfocus();
               }
               break;
            default:
               Runnable change = this.focusedChange;
               if (this.focused.key(key, modifiers) && change != null) {
                  change.run();
               }
         }

         return true;
      } else {
         IntConsumer consumer = this.listening;
         this.listening = null;
         this.listeningId = null;
         boolean clear = key == 256 || key == 259 || key == 261;
         consumer.accept(clear ? -1 : key);
         UiSounds.play(UiSounds.Sound.CLICK);
         return true;
      }
   }

   public boolean charTyped(char character) {
      if (this.listening != null) {
         return true;
      }

      if (this.focused == null) {
         return false;
      }

      Runnable change = this.focusedChange;
      if (this.focused.insert(String.valueOf(character)) && change != null) {
         change.run();
      }

      return true;
   }

   public void focus(String id, TextBuffer buffer, Runnable onChange, Runnable onSubmit) {
      this.focused = buffer;
      this.focusedId = id;
      this.focusedChange = onChange;
      this.focusedSubmit = onSubmit;
      this.listening = null;
      this.listeningId = null;
   }

   public void unfocus() {
      this.focused = null;
      this.focusedId = null;
      this.focusedChange = null;
      this.focusedSubmit = null;
   }

   public void listen(String id, IntConsumer consumer) {
      this.unfocus();
      this.listeningId = id;
      this.listening = consumer;
   }

   public void closePopups() {
      this.dropdownId = null;
      this.dropdownPick = null;
      this.colorPopup = null;
   }

   public boolean popupOpen() {
      return this.dropdownId != null || this.colorPopup != null;
   }

   public void clip(float x, float y, float w, float h) {
      float[] rect = new float[]{x, y, x + w, y + h};
      float[] top = this.clips.peek();
      if (top != null) {
         rect = new float[]{Math.max(rect[0], top[0]), Math.max(rect[1], top[1]), Math.min(rect[2], top[2]), Math.min(rect[3], top[3])};
      }

      this.clips.push(rect);
      GuiDraw.scissor(
         this.g,
         (int)Math.floor(rect[0] * this.scale),
         (int)Math.floor(rect[1] * this.scale),
         (int)Math.ceil(Math.max(rect[0], rect[2]) * this.scale),
         (int)Math.ceil(Math.max(rect[1], rect[3]) * this.scale)
      );
   }

   public void unclip() {
      this.clips.pop();
      this.g.disableScissor();
   }

   public void viewport(float top, float bottom) {
      this.viewTop = top;
      this.viewBottom = bottom;
   }

   public void clearViewport() {
      this.viewTop = Float.NEGATIVE_INFINITY;
      this.viewBottom = Float.POSITIVE_INFINITY;
   }

   public float edgeFade(float y, float h) {
      float center = y + h * 0.5F;
      float top = Math.clamp((center - this.viewTop) / 14.0F, 0.0F, 1.0F);
      float bottom = Math.clamp((this.viewBottom - center) / 14.0F, 0.0F, 1.0F);
      return Math.min(top, bottom);
   }

   public void overlay(Runnable drawer) {
      this.overlays.add(drawer);
   }

   public void drawOverlays() {
      List<Runnable> queued = new ArrayList<>(this.overlays);
      this.overlays.clear();
      this.layer = 10;

      for (Runnable drawer : queued) {
         drawer.run();
      }

      this.layer = 0;
      MenuWidgets.drawDropdownList(this);
      if (this.colorPopup != null) {
         this.colorPopup.draw(this);
      }
   }

   public void setLayer(int value) {
      this.layer = value;
   }

   public float anim(String key, float target, float speed) {
      MenuUi.Smooth entry = this.smooth.get(key);
      if (entry == null) {
         entry = new MenuUi.Smooth();
         entry.value = target;
         this.smooth.put(key, entry);
      }

      entry.frame = this.frame;
      float k = 1.0F - (float)Math.exp(-this.delta * speed);
      entry.value = entry.value + (target - entry.value) * k;
      if (Math.abs(entry.value - target) < 0.001F) {
         entry.value = target;
      }

      return entry.value;
   }

   public void snap(String key, float value) {
      MenuUi.Smooth entry = this.smooth.computeIfAbsent(key, k -> new MenuUi.Smooth());
      entry.value = value;
      entry.frame = this.frame;
   }

   private <T> T keep(String key, Supplier<T> create) {
      MenuUi.Kept entry = this.kept.get(key);
      if (entry == null) {
         entry = new MenuUi.Kept();
         entry.value = create.get();
         this.kept.put(key, entry);
      }

      entry.frame = this.frame;
      return (T)entry.value;
   }

   public Spring spring(String key, double initial, double response, double damping) {
      return this.keep("spring:" + key, () -> new Spring(initial, response, damping));
   }

   public float bounce(String key, float target, double response, double damping) {
      return this.spring(key, target, response, damping).setTarget(target).getFloat();
   }

   public LiquidToggle toggle(String key, boolean on) {
      LiquidToggle toggle = this.keep("toggle:" + key, () -> new LiquidToggle(on));
      toggle.set(on);
      return toggle;
   }

   public LiquidSlider slider(String key, float value) {
      LiquidSlider slider = this.keep("slider:" + key, () -> new LiquidSlider(value));
      slider.set(value);
      return slider;
   }

   public SegmentedControl segmented(String key, String[] labels, int selected) {
      MenuUi.Kept entry = this.kept.get("segmented:" + key);
      if (entry == null || !Arrays.equals(((MenuUi.SegmentedHolder)entry.value).labels(), labels)) {
         SegmentedControl control = new SegmentedControl(labels);
         control.select(selected);
         entry = new MenuUi.Kept();
         entry.value = new MenuUi.SegmentedHolder(labels, control);
         this.kept.put("segmented:" + key, entry);
      }

      entry.frame = this.frame;
      SegmentedControl control = ((MenuUi.SegmentedHolder)entry.value).control();
      control.select(selected);
      return control;
   }

   public float text(SdfFont font, String text, float x, float y, float size, int color) {
      return font.draw(this.g, text, x, y, size, ColorUtil.fade(color, this.alpha));
   }

   public float text(SdfFont font, String text, float x, float y, float size, float tracking, int color) {
      return font.draw(this.g, text, x, y, size, tracking, ColorUtil.fade(color, this.alpha));
   }

   public float textCentered(SdfFont font, String text, float x, float centerY, float size, int color) {
      return font.draw(this.g, text, x, centerY - font.ascent(size) + font.capHeight(size) / 2.0F, size, ColorUtil.fade(color, this.alpha));
   }

   public float textCentered(SdfFont font, String text, float x, float centerY, float size, float tracking, int color) {
      return font.draw(this.g, text, x, centerY - font.ascent(size) + font.capHeight(size) / 2.0F, size, tracking, ColorUtil.fade(color, this.alpha));
   }

   public void icon(int codepoint, float centerX, float centerY, float size, int color) {
      this.icons.drawIcon(this.g, codepoint, centerX, centerY, size, ColorUtil.fade(color, this.alpha));
   }

   public static String plural(String key, int count) {
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

   public static String ellipsize(SdfFont font, String text, float size, float maxWidth) {
      if (font.width(text, size) <= maxWidth) {
         return text;
      }

      String dots = "…";
      int end = text.length();

      while (end > 0 && font.width(text.substring(0, end) + dots, size) > maxWidth) {
         end--;
      }

      return end <= 0 ? dots : text.substring(0, end).stripTrailing() + dots;
   }

   public static List<String> wrap(SdfFont font, String text, float size, float maxWidth) {
      List<String> lines = new ArrayList<>();

      for (String paragraph : text.split("\n")) {
         StringBuilder line = new StringBuilder();

         for (String word : paragraph.split(" ")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (!line.isEmpty() && font.width(candidate, size) > maxWidth) {
               lines.add(line.toString());
               line.setLength(0);
               line.append(word);
            } else {
               line.setLength(0);
               line.append(candidate);
            }
         }

         lines.add(line.toString());
      }

      return lines;
   }

   public void tooltip(String id, float x, float y, float w, float h, String text) {
      if (text != null && !text.isEmpty() && this.pressed == null && this.inside(x, y, w, h)) {
         this.tooltipCandidate = id;
         this.tooltipCandidateText = text;
      }
   }

   public void drawTooltip() {
      if (this.tooltipCandidate == null) {
         this.tooltipId = null;
      } else {
         if (!this.tooltipCandidate.equals(this.tooltipId)) {
            this.tooltipId = this.tooltipCandidate;
            this.tooltipText = this.tooltipCandidateText;
            this.tooltipSince = System.currentTimeMillis();
            this.tooltipX = this.mouseX;
            this.tooltipY = this.mouseY;
         }

         float appear = Math.clamp((float)(System.currentTimeMillis() - this.tooltipSince - 500L) / 160.0F, 0.0F, 1.0F);
         if (!(appear <= 0.0F)) {
            float size = 7.0F;
            float pad = 7.0F;
            List<String> lines = wrap(this.regular, this.tooltipText, size, 200.0F);
            float w = 0.0F;

            for (String line : lines) {
               w = Math.max(w, this.regular.width(line, size));
            }

            float h = lines.size() * 10 + pad * 2.0F - 2.0F;
            float x = Math.min(this.tooltipX + 10.0F, this.screenWidth - w - pad * 2.0F - 4.0F);
            float y = this.tooltipY + 14.0F;
            if (y + h > this.screenHeight - 4.0F) {
               y = this.tooltipY - h - 8.0F;
            }

            float lift = (1.0F - appear) * 3.0F;
            float previousAlpha = this.alpha;
            this.alpha = previousAlpha * appear;
            MenuDraw.popup(this, x, y + lift, w + pad * 2.0F, h, 9.0F);

            for (int i = 0; i < lines.size(); i++) {
               this.text(this.regular, lines.get(i), x + pad, y + lift + pad + i * 10, size, this.palette.text());
            }

            this.alpha = previousAlpha;
         }
      }
   }

   public interface Handler {
      default void press(int button, float localX, float localY) {
      }

      default void drag(float localX, float localY) {
      }

      default void release(float localX, float localY, boolean inside) {
      }

      default boolean scroll(double amount) {
         return false;
      }
   }

   private static final class Kept {
      Object value;
      long frame;
   }

   private record SegmentedHolder(String[] labels, SegmentedControl control) {
   }

   private static final class Smooth {
      float value;
      long frame;
   }

   public record Target(
      String id, float x, float y, float w, float h, float clipX0, float clipY0, float clipX1, float clipY1, int layer, MenuUi.Handler handler
   ) {
      boolean contains(float px, float py) {
         return px >= Math.max(this.x, this.clipX0)
            && py >= Math.max(this.y, this.clipY0)
            && px < Math.min(this.x + this.w, this.clipX1)
            && py < Math.min(this.y + this.h, this.clipY1);
      }
   }
}
