package dev.rose.gui;

import dev.rose.RoseClient;
import dev.rose.anim.Spring;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.gui.menu.MenuUi;
import dev.rose.gui.menu.SettingsPanel;
import dev.rose.hud.Anchor;
import dev.rose.hud.HudElement;
import dev.rose.hud.HudLayouts;
import dev.rose.hud.HudManager;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.HudSettingsModule;
import dev.rose.notify.Notifications;
import dev.rose.render.Backdrop;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.ui.LiquidToggle;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;


public final class HudEditorScreen extends Screen {
   private static final float TOOLBAR_H = 34.0F;
   private static final float TOOL = 26.0F;
   private static final float TOOL_GAP = 2.0F;
   private static final float TOOL_SEPARATOR = 9.0F;
   private static final float TOOLBAR_PAD = 5.0F;
   private static final float INSPECTOR_W = 214.0F;
   private static final float POPOVER_W = 184.0F;
   private static final float POPOVER_ROW = 26.0F;
   private static final float LAYOUT_ROW = 32.0F;
   private static final float SNAP_DISTANCE = 5.0F;
   private static final float HANDLE = 4.5F;
   private static final long HINT_MS = 9000L;
   private static final long DOUBLE_CLICK_MS = 320L;
   private static final long RESET_CONFIRM_MS = 3000L;
   private final Supplier<Screen> back;
   private final GuiContext ctx = new GuiContext();
   private final MenuUi panelUi = new MenuUi();
   private final SettingsPanel panel = new SettingsPanel();
   private HudElement panelElement;
   private final GuiInput input = new GuiInput(this.ctx);
   private final HudHistory history = new HudHistory();
   private final ScrollArea inspectorScroll = new ScrollArea();
   private final ScrollArea elementsScroll = new ScrollArea();
   private final Spring appear = new Spring(0.0, 0.45, 0.86);
   private final Spring toolbarIn = new Spring(0.0, 0.46, 0.74);
   private final Spring inspectorOpen = new Spring(0.0, 0.42, 0.86);
   private final Spring inspectorSide = new Spring(1.0, 0.42, 0.9);
   private final Spring popoverOpen = new Spring(0.0, 0.3, 0.78);
   private final Spring chipVisible = new Spring(0.0, 0.26, 0.86);
   private final Spring guideXAlpha = new Spring(0.0, 0.16, 1.0);
   private final Spring guideYAlpha = new Spring(0.0, 0.16, 1.0);
   private final Spring anchorAlpha = new Spring(0.0, 0.2, 1.0);
   private final Spring hintAlpha = new Spring(0.0, 0.4, 1.0);
   private final Map<String, float[]> toolRects = new HashMap<>();
   private final long openedAt = System.currentTimeMillis();
   private final HudEditorTest test;
   private static final float CANVAS_W = 640.0F;
   private static final float CANVAS_H = 360.0F;
   private float uiScale = 1.0F;
   private float canvasW = 640.0F;
   private float canvasH = 360.0F;
   private float mouseGuiX = -1.0F;
   private float mouseGuiY = -1.0F;
   private HudElement hovered;
   private HudElement selected;
   private HudElement chipElement;
   private HudElement inspected;
   private HudElement shownInspected;
   private HudEditorScreen.Popover popover = HudEditorScreen.Popover.NONE;
   private HudEditorScreen.Popover shownPopover = HudEditorScreen.Popover.NONE;
   private float[] popoverRect;
   private float[] inspectorRect;
   private float[] toolbarRect = new float[4];
   private HudEditorScreen.Drag drag;
   private HudEditorScreen.Scaling scaling;
   private float guideX = Float.NaN;
   private float guideY = Float.NaN;
   private float shownGuideX;
   private float shownGuideY;
   private boolean hintDismissed;
   private boolean closing;
   private long resetAllSince;
   private long lastScaleChange;
   private HudElement lastClicked;
   private long lastClickTime;
   private double[] testMouse;
   private static HudElement pendingFocus;

   public HudEditorScreen(Supplier<Screen> back) {
      super(Component.translatable("rose.hud.editor.title"));
      this.back = back;
      HudManager.setEditing(true);
      this.appear.setTarget(1.0);
      UiSounds.play(UiSounds.Sound.OPEN);
      this.test = "hud".equals(RoseClient.uiTest()) ? new HudEditorTest(this) : null;
      if (pendingFocus != null) {
         this.selected = pendingFocus;
         pendingFocus = null;
      }
   }

   public static void open(HudElement focus) {
      if (focus != null) {
         focus.setEnabled(true);
         MenuScreen.remember(focus);
         pendingFocus = focus;
      }

      open();
   }

   public static void open() {
      Minecraft mc = Minecraft.getInstance();
      Screen current = mc.screen;
      if (!(current instanceof HudEditorScreen)) {
         if (current instanceof MenuScreen menu) {
            menu.openHudEditor();
         } else {
            mc.setScreen(new HudEditorScreen(() -> current));
         }
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void removed() {
      HudManager.setEditing(false);

      for (HudElement element : HudManager.elements()) {
         HudManager.setFollowTight(element, false);
      }
   }

   public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
   }

   public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      if (this.test != null) {
         this.test.tick();
         if (this.testMouse != null) {
            mouseX = (int)Math.round(this.testMouse[0]);
            mouseY = (int)Math.round(this.testMouse[1]);
         }
      }

      if (this.closing && this.appear.getFloat() < 0.02F) {
         this.minecraft.setScreen(this.back.get());
      } else {
         float shown = Math.clamp(this.appear.getFloat(), 0.0F, 1.0F);
         this.uiScale = Math.min(this.width / 640.0F, this.height / 360.0F);
         this.canvasW = this.width / this.uiScale;
         this.canvasH = this.height / this.uiScale;
         this.mouseGuiX = mouseX;
         this.mouseGuiY = mouseY;
         if (this.minecraft.level == null) {
            this.renderPanorama(g, partialTick);
         }

         g.fill(0, 0, this.width, this.height, ColorUtil.withAlpha(0, Math.round((this.minecraft.level == null ? 80 : 46) * shown)));
         this.drawGrid(g, shown);
         Backdrop.capture();
         HudManager.render(g, partialTick, true, false);
         boolean overUi = this.pointerOverUi(mouseX / this.uiScale, mouseY / this.uiScale);
         this.hovered = this.drag != null
            ? this.drag.element
            : (this.scaling != null ? this.scaling.element() : (overUi ? null : this.elementAt(mouseX, mouseY)));
         if (this.ctx.begin(g, shown)) {
            this.ctx.canvasW = this.canvasW;
            this.ctx.canvasH = this.canvasH;
            this.ctx.originX = 0.0F;
            this.ctx.originY = 0.0F;
            this.ctx.scale = this.uiScale;
            this.ctx.mouseX = mouseX / this.uiScale;
            this.ctx.mouseY = mouseY / this.uiScale;
            this.ctx.mouseDown = this.input.dragging() || this.drag != null || this.scaling != null;
            this.drawGuides(g, shown);
            this.drawAnchorLink(g, shown);
            this.drawElementFrames(g, shown);
            g.pose().pushMatrix();
            g.pose().scale(this.uiScale, this.uiScale);
            this.drawEmptyState();
            this.drawHint();
            this.drawScaleBadge();
            this.drawChip();
            this.drawToolbar();
            this.drawPopover();
            this.drawInspector();
            this.ctx.drawTooltip(!this.closing && this.drag == null && this.scaling == null);
            g.pose().popMatrix();
         }
      }
   }

   private void drawGrid(GuiGraphics g, float shown) {
      HudSettingsModule settings = Modules.hud();
      float visible = shown * this.ctx.animate("grid_visible", settings.grid() ? 1.0F : 0.0F, 0.3, 1.0);
      if (!(visible <= 0.01F)) {
         double guiScale = this.minecraft.getWindow().getGuiScale();
         int step = settings.gridSize();
         g.pose().pushMatrix();
         g.pose().scale((float)(1.0 / guiScale), (float)(1.0 / guiScale));
         int physicalW = (int)Math.ceil(this.width * guiScale);
         int physicalH = (int)Math.ceil(this.height * guiScale);
         int faint = ColorUtil.withAlpha(16777215, Math.round(18.0F * visible));
         int strong = ColorUtil.withAlpha(16777215, Math.round(34.0F * visible));

         for (int i = 0; i * step <= this.width; i++) {
            int x = (int)Math.round(i * step * guiScale);
            g.fill(x, 0, x + 1, physicalH, i % 4 == 0 ? strong : faint);
         }

         for (int i = 0; i * step <= this.height; i++) {
            int y = (int)Math.round(i * step * guiScale);
            g.fill(0, y, physicalW, y + 1, i % 4 == 0 ? strong : faint);
         }

         g.pose().popMatrix();
      }
   }

   private void drawElementFrames(GuiGraphics g, float shown) {
      int accent = Theme.accent();

      for (HudElement element : HudManager.elements()) {
         float[] b = element.bounds();
         if (element.isEnabled() && !(b[2] <= 0.0F)) {
            String id = element.id();
            float hover = this.ctx.animate("frame_hover:" + id, element == this.hovered ? 1.0F : 0.0F, 0.18, 1.0);
            float pick = this.ctx.animate("frame_pick:" + id, element == this.selected ? 1.0F : 0.0F, 0.24, 0.8);
            float lift = this.ctx.animate("frame_lift:" + id, this.drag != null && this.drag.element == element && this.drag.moved ? 1.0F : 0.0F, 0.22, 0.75);
            float pad = 2.0F + lift;
            float x = b[0] - pad;
            float y = b[1] - pad;
            float w = b[2] + pad * 2.0F;
            float h = b[3] + pad * 2.0F;
            Corners corners = Corners.themed(Math.min(7.0F, Math.min(w, h) / 2.0F));
            if (lift > 0.01F) {
               Surfaces.shadow(g, x, y + 2.0F, w, h, corners, new Shadow(1342177280, 8.0F, 0.0F, 0.0F, 3.0F), shown * lift);
            }

            if (hover > 0.01F) {
               int fill = ColorUtil.withAlpha(16777215, Math.round(16.0F * hover));
               Surfaces.fill(g, x, y, w, h, corners, fill, fill, fill, fill, shown);
            }

            int idle = ColorUtil.withAlpha(16777215, Math.round(46.0F + 80.0F * hover));
            int color = ColorUtil.lerp(idle, accent, Math.clamp(pick, 0.0F, 1.0F));
            Surfaces.border(g, x, y, w, h, corners, 0.6F + 0.5F * Math.clamp(pick, 0.0F, 1.0F), color, shown);
            if (pick > 0.01F) {
               float size = 4.5F * Math.max(0.0F, pick);
               float[][] points = new float[][]{{x, y}, {x + w, y}, {x + w, y + h}, {x, y + h}};

               for (int i = 0; i < 4; i++) {
                  float handleHover = this.ctx
                     .animate("handle_hover:" + id + i, this.handleAt(this.mouseGuiX, this.mouseGuiY) == i && element == this.selected ? 1.0F : 0.0F, 0.16, 0.9);
                  float s = size * (1.0F + 0.35F * handleHover);
                  float hx = points[i][0] - s / 2.0F;
                  float hy = points[i][1] - s / 2.0F;
                  Surfaces.fill(g, hx, hy, s, s, Corners.round(s / 2.0F), -1, -1, -1, -1, shown);
                  Surfaces.border(g, hx, hy, s, s, Corners.round(s / 2.0F), 0.7F, accent, shown);
               }
            }

            float label = hover * (1.0F - Math.clamp(pick, 0.0F, 1.0F));
            if (label > 0.01F && this.drag == null) {
               this.drawNameTag(g, element, x, y, w, h, label * shown);
            }
         }
      }
   }

   private void drawNameTag(GuiGraphics g, HudElement element, float x, float y, float w, float h, float alpha) {
      String name = element.name();
      float size = 5.8F;
      float tagH = 11.0F;
      float tagW = this.ctx.medium.width(name, size) + 10.0F;
      float tagX = Math.clamp(x + w / 2.0F - tagW / 2.0F, 2.0F, this.width - tagW - 2.0F);
      float tagY = y - tagH - 3.0F;
      if (tagY < 2.0F) {
         tagY = y + h + 3.0F;
      }

      Glass glass = this.ctx.glass.copy();
      glass.tint = Paint.solid(this.ctx.darkGlass() ? -938471404 : -638060550);
      glass.bevel = 4.0F;
      glass.refraction = 3.0F;
      LiquidGlass.draw(g, tagX, tagY, tagW, tagH, Corners.round(tagH / 2.0F), glass, alpha);
      this.ctx.medium.draw(g, name, tagX + 5.0F, centered(tagY, tagH, size), size, ColorUtil.fade(this.ctx.label, alpha));
   }

   private void drawGuides(GuiGraphics g, float shown) {
      float px = 1 / this.minecraft.getWindow().getGuiScale();
      int color = ColorUtil.lerp(Theme.accent2(), -1, 0.15F);
      if (!Float.isNaN(this.guideX)) {
         this.shownGuideX = this.guideX;
      }

      if (!Float.isNaN(this.guideY)) {
         this.shownGuideY = this.guideY;
      }

      float ax = shown * this.guideXAlpha.setTarget(Float.isNaN(this.guideX) ? 0.0 : 1.0).getFloat();
      float ay = shown * this.guideYAlpha.setTarget(Float.isNaN(this.guideY) ? 0.0 : 1.0).getFloat();
      float line = Math.max(px, 0.5F);
      if (ax > 0.01F) {
         Surfaces.fill(g, this.shownGuideX - line / 2.0F, 0.0F, line, this.height, Corners.NONE, color, color, color, color, Math.clamp(ax, 0.0F, 1.0F));
         if (this.drag != null) {
            guideDots(
               g, this.shownGuideX, this.drag.element.bounds()[1], this.shownGuideX, this.drag.element.bounds()[1] + this.drag.element.bounds()[3], color, ax
            );
         }
      }

      if (ay > 0.01F) {
         Surfaces.fill(g, 0.0F, this.shownGuideY - line / 2.0F, this.width, line, Corners.NONE, color, color, color, color, Math.clamp(ay, 0.0F, 1.0F));
         if (this.drag != null) {
            guideDots(
               g, this.drag.element.bounds()[0], this.shownGuideY, this.drag.element.bounds()[0] + this.drag.element.bounds()[2], this.shownGuideY, color, ay
            );
         }
      }
   }

   private static void guideDots(GuiGraphics g, float x0, float y0, float x1, float y1, int color, float alpha) {
      float r = 1.6F;
      Surfaces.fill(g, x0 - r, y0 - r, r * 2.0F, r * 2.0F, Corners.round(r), color, color, color, color, Math.clamp(alpha, 0.0F, 1.0F));
      Surfaces.fill(g, x1 - r, y1 - r, r * 2.0F, r * 2.0F, Corners.round(r), color, color, color, color, Math.clamp(alpha, 0.0F, 1.0F));
   }

   private void drawAnchorLink(GuiGraphics g, float shown) {
      HudElement element = this.drag != null && this.drag.moved ? this.drag.element : null;
      float visible = shown * Math.clamp(this.anchorAlpha.setTarget(element != null ? 1.0 : 0.0).getFloat(), 0.0F, 1.0F);
      if (!(visible <= 0.01F) && element != null) {
         float[] b = element.bounds();
         Anchor anchor = element.anchor();
         float inset = 5.0F;
         float ax = Math.clamp(anchor.fx() * this.width, inset, this.width - inset);
         float ay = Math.clamp(anchor.fy() * this.height, inset, this.height - inset);
         float px = b[0] + anchor.fx() * b[2];
         float py = b[1] + anchor.fy() * b[3];
         int accent = Theme.accent();
         dashedLine(g, ax, ay, px, py, 0.7F, ColorUtil.withAlpha(accent, 176), visible);
         float r = 3.2F;
         Surfaces.fill(g, ax - r, ay - r, r * 2.0F, r * 2.0F, Corners.round(r), accent, accent, accent, accent, visible);
         Surfaces.border(
            g, ax - r - 1.5F, ay - r - 1.5F, r * 2.0F + 3.0F, r * 2.0F + 3.0F, Corners.round(r + 1.5F), 0.7F, ColorUtil.withAlpha(accent, 128), visible
         );
      }
   }

   private static void dashedLine(GuiGraphics g, float x0, float y0, float x1, float y1, float thickness, int color, float alpha) {
      float dx = x1 - x0;
      float dy = y1 - y0;
      float length = (float)Math.hypot(dx, dy);
      if (!(length < 2.0F)) {
         g.pose().pushMatrix();
         g.pose().translate(x0, y0);
         g.pose().rotate((float)Math.atan2(dy, dx));
         float dash = 3.0F;
         float gap = 2.5F;

         for (float t = 0.0F; t < length; t += dash + gap) {
            float segment = Math.min(dash, length - t);
            Surfaces.fill(g, t, -thickness / 2.0F, segment, thickness, Corners.NONE, color, color, color, color, alpha);
         }

         g.pose().popMatrix();
      }
   }

   private void drawEmptyState() {
      boolean empty = HudManager.elements().stream().noneMatch(Module::isEnabled);
      float visible = this.ctx.animate("empty", empty ? 1.0F : 0.0F, 0.3, 1.0);
      if (!(visible <= 0.01F)) {
         String title = I18n.get("rose.hud.editor.empty", new Object[0]);
         String text = I18n.get("rose.hud.editor.empty_hint", new Object[0]);
         float w = Math.max(this.ctx.displayBold.width(title, 11.0F), this.ctx.regular.width(text, 7.0F)) + 40.0F;
         float h = 52.0F;
         float x = (this.canvasW - w) / 2.0F;
         float y = this.canvasH / 2.0F - h / 2.0F - 20.0F;
         float fullAlpha = this.ctx.alpha;
         this.ctx.alpha = fullAlpha * visible;
         LiquidGlass.draw(this.ctx.g, x, y, w, h, Corners.themed(18.0F), this.ctx.glass, this.ctx.alpha);
         this.ctx.icon(58665, x + w / 2.0F, y + 12.0F, 11.0F, this.ctx.accentText());
         this.ctx.text(this.ctx.displayBold, title, x + (w - this.ctx.displayBold.width(title, 11.0F)) / 2.0F, y + 21.0F, 11.0F, this.ctx.label);
         this.ctx.text(this.ctx.regular, text, x + (w - this.ctx.regular.width(text, 7.0F)) / 2.0F, y + 36.0F, 7.0F, this.ctx.secondary());
         this.ctx.alpha = fullAlpha;
      }
   }

   private void drawHint() {
      boolean show = !this.hintDismissed && !this.closing && System.currentTimeMillis() - this.openedAt < 9000L && this.popover == HudEditorScreen.Popover.NONE;
      float visible = Math.clamp(this.hintAlpha.setTarget(show ? 1.0 : 0.0).getFloat(), 0.0F, 1.0F);
      if (!(visible <= 0.01F)) {
         String text = I18n.get("rose.hud.editor.hint", new Object[0]);
         float size = 6.8F;
         float w = this.ctx.regular.width(text, size) + 20.0F;
         float h = 17.0F;
         float x = (this.canvasW - w) / 2.0F;
         float y = this.toolbarRect[1] - h - 8.0F + (1.0F - visible) * 6.0F;
         Glass glass = this.ctx.glass.copy();
         glass.tint = Paint.solid(this.ctx.darkGlass() ? -1508896748 : -940050438);
         LiquidGlass.shapes().add(x, y, w, h, Corners.round(h / 2.0F)).refreshBackdrop().draw(this.ctx.g, glass, 0.0F, this.ctx.alpha * visible);
         float fullAlpha = this.ctx.alpha;
         this.ctx.alpha = fullAlpha * visible;
         this.ctx.text(this.ctx.regular, text, x + 10.0F, centered(y, h, size), size, ColorUtil.fade(this.ctx.label, 0.85F));
         this.ctx.alpha = fullAlpha;
      }
   }

   private void drawScaleBadge() {
      HudElement element = this.scaling != null ? this.scaling.element() : this.selected;
      boolean show = element != null && (this.scaling != null || System.currentTimeMillis() - this.lastScaleChange < 900L);
      float visible = this.ctx.animate("scale_badge", show ? 1.0F : 0.0F, 0.2, 0.9);
      if (!(visible <= 0.01F) && element != null) {
         float[] b = element.bounds();
         String text = Math.round(element.scale() * 100.0F) + "%";
         float size = 7.0F;
         float h = 15.0F;
         float w = this.ctx.semibold.width(text, size) + 14.0F;
         float x = Math.clamp((b[0] + b[2]) / this.uiScale - w / 2.0F, 4.0F, this.canvasW - w - 4.0F);
         float y = Math.clamp((b[1] + b[3]) / this.uiScale + 6.0F, 4.0F, this.canvasH - h - 4.0F);
         Surfaces.fill(
            this.ctx.g, x, y, w, h, Corners.round(h / 2.0F), Theme.accent(), Theme.accent(), Theme.accent(), Theme.accent(), this.ctx.alpha * visible
         );
         float fullAlpha = this.ctx.alpha;
         this.ctx.alpha = fullAlpha * visible;
         this.ctx.text(this.ctx.semibold, text, x + 7.0F, centered(y, h, size), size, -1);
         this.ctx.alpha = fullAlpha;
      }
   }

   private void drawChip() {
      boolean badge = System.currentTimeMillis() - this.lastScaleChange < 900L;
      boolean show = this.selected != null && this.drag == null && this.scaling == null && !badge && !this.closing;
      if (this.selected != null) {
         this.chipElement = this.selected;
      }

      float t = this.chipVisible.setTarget(show ? 1.0 : 0.0).getFloat();
      float visible = Math.clamp(t, 0.0F, 1.0F);
      if (!(visible <= 0.01F) && this.chipElement != null) {
         float[] b = this.chipElement.bounds();
         if (!(b[2] <= 0.0F)) {
            String name = GuiContext.ellipsize(this.ctx.semibold, this.chipElement.name(), 7.0F, 110.0F);
            float size = 7.0F;
            float button = 20.0F;
            float gap = 1.0F;
            float h = 26.0F;
            float nameW = this.ctx.semibold.width(name, size);
            float w = 10.0F + nameW + 8.0F + button * 5.0F + gap * 4.0F + 3.0F;
            float bx = b[0] / this.uiScale;
            float by = b[1] / this.uiScale;
            float bw = b[2] / this.uiScale;
            float bh = b[3] / this.uiScale;
            float x = Math.clamp(bx + bw / 2.0F - w / 2.0F, 6.0F, this.canvasW - w - 6.0F);
            float y = by - h - 9.0F;
            if (y < 6.0F) {
               y = by + bh + 9.0F;
            }

            y += (1.0F - t) * 5.0F * this.ctx.motion;
            Glass glass = this.ctx.glass.copy();
            glass.bevel = 6.0F;
            LiquidGlass.shapes().add(x, y, w, h, Corners.round(h / 2.0F)).refreshBackdrop().draw(this.ctx.g, glass, 0.0F, this.ctx.alpha * visible);
            float fullAlpha = this.ctx.alpha;
            this.ctx.alpha = fullAlpha * visible;
            boolean interactive = this.ctx.interactive;
            this.ctx.interactive = interactive && show;
            this.ctx.text(this.ctx.semibold, name, x + 10.0F, centered(y, h, size), size, this.ctx.label);
            float cursor = x + 10.0F + nameW + 8.0F;
            HudElement element = this.chipElement;
            this.chipButton("chip:smaller", 57628, "rose.hud.editor.size_down", cursor, y + (h - button) / 2.0F, button, () -> this.resize(element, -0.1F));
            cursor += button + gap;
            this.chipButton("chip:bigger", 57661, "rose.hud.editor.size_up", cursor, y + (h - button) / 2.0F, button, () -> this.resize(element, 0.1F));
            cursor += button + gap;
            this.chipButton("chip:settings", 58010, "rose.hud.editor.settings", cursor, y + (h - button) / 2.0F, button, () -> this.openInspector(element));
            cursor += button + gap;
            this.chipButton("chip:reset", 57672, "rose.hud.editor.reset_element", cursor, y + (h - button) / 2.0F, button, () -> {
               element.resetPlacement();
               element.setScale(1.0F);
               UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
            });
            cursor += button + gap;
            this.chipButton("chip:hide", 57531, "rose.hud.editor.hide", cursor, y + (h - button) / 2.0F, button, () -> this.hide(element));
            this.ctx.interactive = interactive;
            this.ctx.alpha = fullAlpha;
         }
      }
   }

   private void resize(HudElement element, float delta) {
      this.history.begin();
      this.scaleBy(element, delta);
      this.history.end("scale:" + element.id());
      UiSounds.play(UiSounds.Sound.SLIDER);
   }

   private void chipButton(String id, int icon, String tooltip, float x, float y, float size, Runnable action) {
      float hover = this.ctx.animate("chip_hover:" + id, this.ctx.hovered(x, y, size, size) ? 1.0F : 0.0F, 0.18, 1.0);
      if (hover > 0.01F) {
         int fill = ColorUtil.withAlpha(16777215, Math.round((this.ctx.darkGlass() ? 30 : 102) * hover));
         Surfaces.fill(this.ctx.g, x, y, size, size, Corners.round(size / 2.0F), fill, fill, fill, fill, this.ctx.alpha);
      }

      this.ctx.icon(icon, x + size / 2.0F, y + size / 2.0F, 10.0F, ColorUtil.lerp(ColorUtil.fade(this.ctx.label, 0.8F), this.ctx.accentText(), hover));
      this.ctx.target(id, x, y, size, size, this.pressable(id, action));
      this.ctx.tooltip(id, x, y, size, size, I18n.get(tooltip, new Object[0]));
   }

   private List<HudEditorScreen.Tool> tools() {
      HudSettingsModule settings = Modules.hud();
      List<HudEditorScreen.Tool> tools = new ArrayList<>();
      tools.add(new HudEditorScreen.Tool("tool:undo", 58017, "rose.hud.editor.undo", () -> false, this.history::canUndo, this::undo));
      tools.add(new HudEditorScreen.Tool("tool:redo", 58016, "rose.hud.editor.redo", () -> false, this.history::canRedo, this::redo));
      tools.add(null);
      tools.add(new HudEditorScreen.Tool("tool:grid", 57577, "rose.hud.editor.grid", settings::grid, () -> true, () -> settings.setGrid(!settings.grid())));
      tools.add(new HudEditorScreen.Tool("tool:snap", 58037, "rose.hud.editor.snap", settings::snap, () -> true, () -> settings.setSnap(!settings.snap())));
      tools.add(null);
      tools.add(
         new HudEditorScreen.Tool(
            "tool:elements",
            58665,
            "rose.hud.editor.elements",
            () -> this.popover == HudEditorScreen.Popover.ELEMENTS,
            () -> true,
            () -> this.togglePopover(HudEditorScreen.Popover.ELEMENTS)
         )
      );
      tools.add(
         new HudEditorScreen.Tool(
            "tool:layouts",
            57793,
            "rose.hud.editor.layouts",
            () -> this.popover == HudEditorScreen.Popover.LAYOUTS,
            () -> true,
            () -> this.togglePopover(HudEditorScreen.Popover.LAYOUTS)
         )
      );
      tools.add(new HudEditorScreen.Tool("tool:reset", 57672, "rose.hud.editor.reset_all", this::confirmingReset, () -> true, this::resetAll));
      return tools;
   }

   private void drawToolbar() {
      boolean show = !this.closing && this.drag == null && this.scaling == null && System.currentTimeMillis() - this.openedAt > 60L;
      float t = this.toolbarIn.setTarget(show ? 1.0 : 0.0).getFloat();
      float visible = Math.clamp(t, 0.0F, 1.0F);
      List<HudEditorScreen.Tool> tools = this.tools();
      String doneLabel = I18n.get("rose.hud.editor.done", new Object[0]);
      float doneH = 22.0F;
      float doneW = Widgets.pillWidth(this.ctx, 57452, doneLabel, doneH);
      float w = 10.0F + doneW + 4.0F;

      for (HudEditorScreen.Tool tool : tools) {
         w += tool == null ? 9.0F : 28.0F;
      }

      float x = (this.canvasW - w) / 2.0F;
      float restY = this.canvasH - 12.0F - 34.0F;
      float y = restY + (1.0F - t) * 52.0F * this.ctx.motion;
      this.toolbarRect = new float[]{x, restY, w, 34.0F};
      if (!(visible <= 0.01F)) {
         Glass glass = this.ctx.glass.copy();
         glass.bevel = 8.0F;
         Surfaces.shadow(this.ctx.g, x, y + 3.0F, w, 34.0F, Corners.round(17.0F), new Shadow(1342177280, 12.0F, 0.0F, 0.0F, 4.0F), this.ctx.alpha * visible);
         LiquidGlass.shapes().add(x, y, w, 34.0F, Corners.round(17.0F)).refreshBackdrop().draw(this.ctx.g, glass, 0.0F, this.ctx.alpha * visible);
         float fullAlpha = this.ctx.alpha;
         boolean interactive = this.ctx.interactive;
         this.ctx.alpha = fullAlpha * visible;
         this.ctx.interactive = interactive && show;
         float cursor = x + 5.0F;
         int separator = ColorUtil.fade(this.ctx.label, 0.14F);

         for (HudEditorScreen.Tool tool : tools) {
            if (tool == null) {
               Surfaces.fill(
                  this.ctx.g, cursor + 4.5F - 0.4F - 1.0F, y + 9.0F, 0.8F, 16.0F, Corners.NONE, separator, separator, separator, separator, this.ctx.alpha
               );
               cursor += 9.0F;
            } else {
               this.drawTool(tool, cursor, y + 4.0F);
               cursor += 28.0F;
            }
         }

         boolean confirming = this.confirmingReset();
         Widgets.pill(this.ctx, "tool:done", cursor + 2.0F, y + (34.0F - doneH) / 2.0F, doneH, 57452, doneLabel, Widgets.Style.ACCENT, this::onClose);
         if (confirming) {
            float[] rect = this.toolRects.get("tool:reset");
            if (rect != null) {
               String text = I18n.get("rose.hud.editor.reset_confirm", new Object[0]);
               float size = 6.8F;
               float tw = this.ctx.medium.width(text, size) + 16.0F;
               float th = 16.0F;
               float tx = Math.clamp(rect[0] + rect[2] / 2.0F - tw / 2.0F, 4.0F, this.canvasW - tw - 4.0F);
               float ty = y - th - 6.0F;
               Surfaces.fill(this.ctx.g, tx, ty, tw, th, Corners.round(th / 2.0F), Theme.danger, Theme.danger, Theme.danger, Theme.danger, this.ctx.alpha);
               this.ctx.text(this.ctx.medium, text, tx + 8.0F, centered(ty, th, size), size, -1);
            }
         }

         this.ctx.interactive = interactive;
         this.ctx.alpha = fullAlpha;
      }
   }

   private void drawTool(HudEditorScreen.Tool tool, float x, float y) {
      this.toolRects.put(tool.id(), new float[]{x, y, 26.0F, 26.0F});
      boolean enabled = tool.enabled().getAsBoolean();
      boolean active = tool.active().getAsBoolean();
      boolean danger = tool.id().equals("tool:reset") && active;
      float hover = this.ctx.animate("tool_hover:" + tool.id(), enabled && this.ctx.hovered(x, y, 26.0F, 26.0F) ? 1.0F : 0.0F, 0.18, 1.0);
      float on = this.ctx.animate("tool_on:" + tool.id(), active ? 1.0F : 0.0F, 0.24, 0.85);
      float press = this.ctx.animate("tool_press:" + tool.id(), tool.id().equals(this.ctx.pressed) ? 1.0F : 0.0F, 0.12, 0.9);
      float usable = this.ctx.animate("tool_enabled:" + tool.id(), enabled ? 1.0F : 0.0F, 0.2, 1.0);
      float size = 26.0F * (1.0F - 0.08F * press) * (1.0F + 0.04F * Math.max(0.0F, on - 1.0F));
      float bx = x + (26.0F - size) / 2.0F;
      float by = y + (26.0F - size) / 2.0F;
      int idle = ColorUtil.withAlpha(16777215, Math.round((this.ctx.darkGlass() ? 28 : 96) * hover));
      int onColor = ColorUtil.withAlpha(danger ? Theme.danger : Theme.accent(), this.ctx.darkGlass() ? 112 : 144);
      int fill = ColorUtil.lerp(idle, onColor, Math.clamp(on, 0.0F, 1.0F));
      if (ColorUtil.alpha(fill) > 0) {
         Surfaces.fill(this.ctx.g, bx, by, size, size, Corners.round(size / 2.0F), fill, fill, fill, fill, this.ctx.alpha);
      }

      int iconColor = ColorUtil.lerp(this.ctx.label, this.ctx.darkGlass() ? -1 : this.ctx.label, Math.clamp(on, 0.0F, 1.0F));
      this.ctx.icon(tool.icon(), x + 13.0F, y + 13.0F, 11.5F, ColorUtil.fade(iconColor, 0.3F + 0.7F * usable));
      this.ctx.target(tool.id(), x, y, 26.0F, 26.0F, new GuiContext.Interaction() {
         @Override
         public void press(float localX, float localY) {
            HudEditorScreen.this.ctx.pressed = tool.id();
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            HudEditorScreen.this.ctx.pressed = null;
            if (inside && tool.enabled().getAsBoolean()) {
               UiSounds.play(UiSounds.Sound.CLICK);
               tool.action().run();
            }
         }
      });
      if (this.popover == HudEditorScreen.Popover.NONE) {
         this.ctx.tooltip(tool.id(), x, y, 26.0F, 26.0F, I18n.get(tool.tooltip(), new Object[0]));
      }
   }

   private void drawPopover() {
      if (this.popover != HudEditorScreen.Popover.NONE) {
         this.shownPopover = this.popover;
      }

      float t = this.popoverOpen.setTarget(this.popover != HudEditorScreen.Popover.NONE ? 1.0 : 0.0).getFloat();
      float visible = Math.clamp(t, 0.0F, 1.0F);
      if (!(visible <= 0.01F) && this.shownPopover != HudEditorScreen.Popover.NONE) {
         float[] button = this.toolRects.get(this.shownPopover == HudEditorScreen.Popover.ELEMENTS ? "tool:elements" : "tool:layouts");
         if (button != null) {
            List<HudElement> elements = HudManager.elements();
            float contentH = this.shownPopover == HudEditorScreen.Popover.ELEMENTS ? elements.size() * 26.0F : 130.0F;
            float bottom = this.toolbarRect[1] - 10.0F;
            float maxContent = Math.max(52.0F, bottom - 40.0F - 32.0F);
            float viewH = Math.min(contentH, maxContent);
            float w = 184.0F;
            float h = 30.0F + viewH + 8.0F;
            float anchorX = button[0] + button[2] / 2.0F;
            float x = Math.clamp(anchorX - w / 2.0F, 8.0F, this.canvasW - w - 8.0F);
            float y = bottom - h;
            this.popoverRect = new float[]{x, y, w, h};
            float grow = 0.86F + 0.14F * Math.min(t, 1.05F);
            this.ctx.g.pose().pushMatrix();
            this.ctx.g.pose().translate(anchorX, bottom);
            this.ctx.g.pose().scale(grow, grow);
            this.ctx.g.pose().translate(-anchorX, -bottom);
            Glass glass = this.ctx.glass.copy();
            glass.tint = Paint.solid(this.ctx.darkGlass() ? -1274015724 : -856164358);
            Surfaces.shadow(this.ctx.g, x, y + 3.0F, w, h, Corners.themed(18.0F), new Shadow(1426063360, 14.0F, 0.0F, 0.0F, 4.0F), this.ctx.alpha * visible);
            LiquidGlass.shapes().add(x, y, w, h, Corners.themed(18.0F)).refreshBackdrop().draw(this.ctx.g, glass, 0.0F, this.ctx.alpha * visible);
            float fullAlpha = this.ctx.alpha;
            boolean interactive = this.ctx.interactive;
            this.ctx.alpha = fullAlpha * visible;
            this.ctx.interactive = interactive && this.popover != HudEditorScreen.Popover.NONE;
            String title = I18n.get(
                  this.shownPopover == HudEditorScreen.Popover.ELEMENTS ? "rose.hud.editor.elements" : "rose.hud.editor.layouts", new Object[0]
               )
               .toUpperCase(Locale.ROOT);
            this.ctx.text(this.ctx.semibold, title, x + 14.0F, y + 11.0F, 6.0F, 0.8F, ColorUtil.fade(this.ctx.label, 0.5F));
            float viewY = y + 26.0F;
            this.ctx.clip(x, viewY, w, viewH + 4.0F);
            if (this.shownPopover == HudEditorScreen.Popover.ELEMENTS) {
               this.elementsScroll.setBounds(contentH, viewH);
               this.ctx.viewport(viewY, viewY + viewH + 4.0F);
               float rowY = viewY - this.elementsScroll.get();

               for (HudElement element : elements) {
                  this.elementRow(element, x + 6.0F, rowY, w - 12.0F);
                  rowY += 26.0F;
               }

               this.ctx.clearViewport();
            } else {
               float rowY = viewY;

               for (HudLayouts.Layout layout : HudLayouts.Layout.values()) {
                  this.layoutRow(layout, x + 6.0F, rowY, w - 12.0F);
                  rowY += 32.0F;
               }

               int line = ColorUtil.fade(this.ctx.label, 0.1F);
               Surfaces.fill(this.ctx.g, x + 16.0F, rowY + 3.5F, w - 32.0F, 0.6F, Corners.NONE, line, line, line, line, this.ctx.alpha);
               rowY += 8.0F;
               this.resetLayoutRow(x + 6.0F, rowY, w - 12.0F);
            }

            this.ctx.unclip();
            this.ctx.interactive = interactive;
            this.ctx.alpha = fullAlpha;
            this.ctx.g.pose().popMatrix();
         }
      } else {
         this.popoverRect = null;
         if (this.popover == HudEditorScreen.Popover.NONE) {
            this.shownPopover = HudEditorScreen.Popover.NONE;
         }
      }
   }

   private void elementRow(HudElement element, float x, float y, float w) {
      String id = "pop:element:" + element.id();
      float fade = this.ctx.edgeFade(y, 26.0F);
      float fullAlpha = this.ctx.alpha;
      this.ctx.alpha = fullAlpha * fade;
      float hover = this.ctx.animate("pop_hover:" + id, this.ctx.hovered(x, y, w, 26.0F) ? 1.0F : 0.0F, 0.18, 1.0);
      if (hover > 0.01F) {
         int fill = ColorUtil.withAlpha(16777215, Math.round((this.ctx.darkGlass() ? 20 : 85) * hover));
         Surfaces.fill(this.ctx.g, x, y + 1.0F, w, 24.0F, Corners.themed(10.0F), fill, fill, fill, fill, this.ctx.alpha);
      }

      boolean on = element.isEnabled();
      float active = this.ctx.animate("pop_on:" + id, on ? 1.0F : 0.0F, 0.25, 0.9);
      this.ctx.icon(element.icon(), x + 13.0F, y + 13.0F, 10.0F, ColorUtil.lerp(ColorUtil.fade(this.ctx.label, 0.5F), this.ctx.accentText(), active));
      float toggleW = 26.0F;
      float toggleH = 15.0F;
      String name = GuiContext.ellipsize(this.ctx.medium, element.name(), 7.4F, w - 30.0F - toggleW - 14.0F);
      this.ctx.text(this.ctx.medium, name, x + 26.0F, centered(y, 26.0F, 7.4F), 7.4F, ColorUtil.fade(this.ctx.label, 0.65F + 0.35F * active));
      LiquidToggle toggle = this.ctx.toggle("pop_toggle:" + element.id(), on);
      toggle.render(this.ctx.g, x + w - 8.0F - toggleW, y + (26.0F - toggleH) / 2.0F, toggleW, toggleH, this.ctx.alpha, Theme.accent(), this.ctx.track());
      this.ctx.target(id, x, y, w, 26.0F, GuiContext.click(() -> {
         boolean enable = !element.isEnabled();
         element.setEnabled(enable);
         UiSounds.play(enable ? UiSounds.Sound.TOGGLE_ON : UiSounds.Sound.TOGGLE_OFF);
         if (enable) {
            this.select(element);
         } else if (this.selected == element) {
            this.select(null);
         }
      }));
      this.ctx.alpha = fullAlpha;
   }

   private void layoutRow(HudLayouts.Layout layout, float x, float y, float w) {
      String key = layout.name().toLowerCase(Locale.ROOT);
      String id = "pop:layout:" + key;
      float hover = this.ctx.animate("pop_hover:" + id, this.ctx.hovered(x, y, w, 32.0F) ? 1.0F : 0.0F, 0.18, 1.0);
      if (hover > 0.01F) {
         int fill = ColorUtil.withAlpha(16777215, Math.round((this.ctx.darkGlass() ? 20 : 85) * hover));
         Surfaces.fill(this.ctx.g, x, y + 1.0F, w, 30.0F, Corners.themed(10.0F), fill, fill, fill, fill, this.ctx.alpha);
      }

      float iconBox = 20.0F;
      float iconX = x + 5.0F;
      float iconY = y + (32.0F - iconBox) / 2.0F;
      int boxFill = ColorUtil.withAlpha(Theme.accent(), this.ctx.darkGlass() ? 58 : 85);
      Surfaces.fill(this.ctx.g, iconX, iconY, iconBox, iconBox, Corners.themed(7.0F), boxFill, boxFill, boxFill, boxFill, this.ctx.alpha);
      this.ctx.icon(57793, iconX + iconBox / 2.0F, iconY + iconBox / 2.0F, 10.0F, this.ctx.accentText());
      float textX = iconX + iconBox + 8.0F;
      this.ctx.text(this.ctx.semibold, I18n.get("rose.hud.layout." + key, new Object[0]), textX, y + 6.5F, 7.4F, this.ctx.label);
      String description = GuiContext.ellipsize(
         this.ctx.regular, I18n.get("rose.hud.layout." + key + ".description", new Object[0]), 6.0F, w - (textX - x) - 8.0F
      );
      this.ctx.text(this.ctx.regular, description, textX, y + 17.5F, 6.0F, this.ctx.secondary());
      this.ctx.target(id, x, y, w, 32.0F, GuiContext.click(() -> this.applyLayout(layout)));
   }

   private void resetLayoutRow(float x, float y, float w) {
      String id = "pop:layout:reset";
      float hover = this.ctx.animate("pop_hover:" + id, this.ctx.hovered(x, y, w, 26.0F) ? 1.0F : 0.0F, 0.18, 1.0);
      if (hover > 0.01F) {
         int fill = ColorUtil.withAlpha(Theme.danger, Math.round(48.0F * hover));
         Surfaces.fill(this.ctx.g, x, y + 1.0F, w, 24.0F, Corners.themed(10.0F), fill, fill, fill, fill, this.ctx.alpha);
      }

      int danger = ColorUtil.lerp(Theme.danger, -1, this.ctx.darkGlass() ? 0.2F : 0.0F);
      this.ctx.icon(57672, x + 15.0F, y + 13.0F, 10.0F, danger);
      this.ctx
         .text(this.ctx.medium, I18n.get("rose.hud.editor.reset_positions", new Object[0]), x + 33.0F, centered(y, 26.0F, 7.4F), 7.4F, danger);
      this.ctx
         .target(
            id,
            x,
            y,
            w,
            26.0F,
            GuiContext.click(
               () -> {
                  HudLayouts.resetPlacements();
                  this.popover = HudEditorScreen.Popover.NONE;
                  Notifications.show(
                     Notifications.Kind.NEUTRAL,
                     I18n.get("rose.hud.editor.title", new Object[0]),
                     I18n.get("rose.hud.editor.reset_done", new Object[0])
                  );
               }
            )
         );
   }

   private void drawInspector() {
      if (this.inspected != null) {
         this.shownInspected = this.inspected;
      }

      float t = this.inspectorOpen.setTarget(this.inspected != null && !this.closing ? 1.0 : 0.0).getFloat();
      float visible = Math.clamp(t, 0.0F, 1.0F);
      if (!(visible <= 0.01F) && this.shownInspected != null) {
         HudElement element = this.shownInspected;
         float[] b = element.bounds();
         if (this.inspected != null && b[2] > 0.0F) {
            float center = (b[0] + b[2] / 2.0F) / this.width;
            if (center > 0.62F) {
               this.inspectorSide.setTarget(0.0);
            } else if (center < 0.38F) {
               this.inspectorSide.setTarget(1.0);
            }
         }

         float side = this.inspectorSide.getFloat();
         float w = 214.0F;
         float top = 10.0F;
         float bottom = this.canvasH - 12.0F - 34.0F - 10.0F;
         float h = bottom - top;
         float restX = 10.0F + (this.canvasW - 20.0F - w) * side;
         float x = restX + (1.0F - t) * (side > 0.5F ? 36 : -36) * this.ctx.motion;
         this.inspectorRect = new float[]{x, top, w, h};
         Glass glass = this.ctx.glass.copy();
         glass.tint = Paint.solid(this.ctx.darkGlass() ? -1475342316 : -990382086);
         glass.refraction = 6.0F + Math.min(18.0F, (float)Math.abs(this.inspectorSide.velocity()) * 12.0F) * this.ctx.motion;
         Surfaces.shadow(this.ctx.g, x, top + 4.0F, w, h, Corners.themed(22.0F), new Shadow(1509949440, 18.0F, 0.0F, 0.0F, 6.0F), this.ctx.alpha * visible);
         LiquidGlass.shapes().add(x, top, w, h, Corners.themed(22.0F)).refreshBackdrop().draw(this.ctx.g, glass, 0.0F, this.ctx.alpha * visible);
         float fullAlpha = this.ctx.alpha;
         boolean interactive = this.ctx.interactive;
         this.ctx.alpha = fullAlpha * visible;
         this.ctx.interactive = interactive && this.inspected != null;
         float iconBox = 26.0F;
         float iconX = x + 12.0F;
         float iconY = top + 12.0F;
         int boxFill = ColorUtil.withAlpha(Theme.accent(), this.ctx.darkGlass() ? 68 : 92);
         Surfaces.fill(this.ctx.g, iconX, iconY, iconBox, iconBox, Corners.themed(9.0F), boxFill, boxFill, boxFill, boxFill, this.ctx.alpha);
         this.ctx.icon(element.icon(), iconX + iconBox / 2.0F, iconY + iconBox / 2.0F, 13.0F, this.ctx.accentText());
         float closeSize = 20.0F;
         float closeX = x + w - 12.0F - closeSize;
         float closeY = top + 15.0F;
         float closeHover = this.ctx.animate("inspector_close_hover", this.ctx.hovered(closeX, closeY, closeSize, closeSize) ? 1.0F : 0.0F, 0.18, 1.0);
         int closeFill = ColorUtil.lerp(this.ctx.track(), ColorUtil.lerp(this.ctx.track(), -1, 0.3F), closeHover);
         Surfaces.fill(
            this.ctx.g, closeX, closeY, closeSize, closeSize, Corners.round(closeSize / 2.0F), closeFill, closeFill, closeFill, closeFill, this.ctx.alpha
         );
         this.ctx.icon(57778, closeX + closeSize / 2.0F, closeY + closeSize / 2.0F, 9.0F, this.ctx.label);
         this.ctx
            .target(
               "inspector:close", closeX - 3.0F, closeY - 3.0F, closeSize + 6.0F, closeSize + 6.0F, this.pressable("inspector:close", this::closeInspector)
            );
         float textX = iconX + iconBox + 8.0F;
         float textW = closeX - 6.0F - textX;
         this.ctx.text(this.ctx.displayBold, GuiContext.ellipsize(this.ctx.displayBold, element.name(), 9.5F, textW), textX, top + 13.0F, 9.5F, this.ctx.label);
         this.ctx
            .text(this.ctx.regular, GuiContext.ellipsize(this.ctx.regular, element.description(), 6.2F, textW), textX, top + 27.0F, 6.2F, this.ctx.secondary());
         float viewX = x + 4.0F;
         float viewY = top + 46.0F;
         float viewW = w - 8.0F;
         float viewH = h - 50.0F;
         float contentH = 0.0F;
         if (this.panelUi.begin(this.ctx.g, this.uiScale, this.ctx.mouseX, this.ctx.mouseY)) {
            this.panelUi.screenWidth = this.canvasW;
            this.panelUi.screenHeight = this.canvasH;
            this.panelUi.alpha = this.ctx.alpha;
            this.panelUi.motion = this.ctx.motion;
            this.panelUi.interactive = this.ctx.interactive && this.drag == null && this.scaling == null;
            if (this.panelElement != element) {
               this.panelElement = element;
               this.panelUi.closePopups();
               this.panelUi.unfocus();
               this.panel.restartAppear(this.panelUi, element);
            }

            this.panelUi.clip(viewX, viewY, viewW, viewH);
            this.panelUi.viewport(viewY, viewY + viewH);
            contentH = this.panel.draw(this.panelUi, element, viewX + 4.0F, viewY + 2.0F - this.inspectorScroll.get(), viewW - 8.0F);
            this.panelUi.clearViewport();
            this.panelUi.unclip();
            this.panelUi.drawOverlays();
            this.panelUi.drawTooltip();
            this.panelUi.end();
         }

         this.inspectorScroll.setBounds(contentH + 8.0F, viewH);
         this.ctx.interactive = interactive;
         this.ctx.alpha = fullAlpha;
      } else {
         this.inspectorRect = null;
         if (this.inspected == null) {
            this.shownInspected = null;
         }
      }
   }

   private void select(HudElement element) {
      if (this.selected != element) {
         this.selected = element;
         if (element != null && this.inspected != null && this.inspected != element) {
            this.openInspector(element);
         }
      }
   }

   private void openInspector(HudElement element) {
      if (element != null) {
         this.selected = element;
         if (this.inspected != element) {
            if (this.inspectorOpen.getFloat() < 0.05F) {
               float[] b = element.bounds();
               this.inspectorSide.snap(b[2] > 0.0F && (b[0] + b[2] / 2.0F) / this.width > 0.5F ? 0.0 : 1.0);
            }

            this.inspected = element;
            this.inspectorScroll.reset();
            this.ctx.restartStagger("page");
            this.ctx.expandedColor = null;
            this.ctx.listeningKey = null;
            if (this.ctx.focusedOwner != null) {
               this.ctx.unfocus();
            }
         }

         this.popover = HudEditorScreen.Popover.NONE;
         UiSounds.play(UiSounds.Sound.TAB);
      }
   }

   private void closeInspector() {
      this.inspected = null;
      this.ctx.expandedColor = null;
      this.ctx.listeningKey = null;
      if (this.ctx.focusedOwner != null) {
         this.ctx.unfocus();
      }
   }

   private void togglePopover(HudEditorScreen.Popover which) {
      this.popover = this.popover == which ? HudEditorScreen.Popover.NONE : which;
      if (this.popover == HudEditorScreen.Popover.ELEMENTS) {
         this.elementsScroll.reset();
      }
   }

   private void hide(HudElement element) {
      element.setEnabled(false);
      if (this.selected == element) {
         this.selected = null;
      }

      if (this.inspected == element) {
         this.closeInspector();
      }

      UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
      Notifications.show(Notifications.Kind.NEUTRAL, element.name(), I18n.get("rose.hud.editor.hidden", new Object[0]));
   }

   private void applyLayout(HudLayouts.Layout layout) {
      HudLayouts.apply(layout);
      this.popover = HudEditorScreen.Popover.NONE;
      UiSounds.play(UiSounds.Sound.TAB);
      Notifications.show(
         Notifications.Kind.SUCCESS,
         I18n.get("rose.hud.layout." + layout.name().toLowerCase(Locale.ROOT), new Object[0]),
         I18n.get("rose.hud.editor.layout_applied", new Object[0])
      );
   }

   private boolean confirmingReset() {
      return System.currentTimeMillis() - this.resetAllSince < 3000L;
   }

   private void resetAll() {
      if (!this.confirmingReset()) {
         this.resetAllSince = System.currentTimeMillis();
      } else {
         this.resetAllSince = 0L;
         HudLayouts.resetPlacements();
         Notifications.show(
            Notifications.Kind.NEUTRAL,
            I18n.get("rose.hud.editor.title", new Object[0]),
            I18n.get("rose.hud.editor.reset_done", new Object[0])
         );
      }
   }

   private void undo() {
      if (this.history.undo()) {
         UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
      }
   }

   private void redo() {
      if (this.history.redo()) {
         UiSounds.play(UiSounds.Sound.TOGGLE_ON);
      }
   }

   private void nudge(float dx, float dy) {
      if (this.selected != null) {
         float[] b = this.selected.bounds();
         HudManager.placeAt(
            this.selected,
            Math.clamp(b[0] + dx, 0.0F, this.width - b[2]),
            Math.clamp(b[1] + dy, 0.0F, this.height - b[3]),
            b[2],
            b[3],
            this.width,
            this.height
         );
         float[] target = HudManager.position(this.selected, b[2], b[3], this.width, this.height);
         this.selected.bounds()[0] = target[0];
         this.selected.bounds()[1] = target[1];
      }
   }

   private void scaleBy(HudElement element, float delta) {
      float[] b = element.bounds();
      float centerX = b[0] + b[2] / 2.0F;
      float centerY = b[1] + b[3] / 2.0F;
      float scale = Math.clamp(Math.round((element.scale() + delta) * 20.0F) / 20.0F, 0.5F, 3.0F);
      element.setScale(scale);
      float[] content = HudManager.contentSize(element);
      float w = content[0] * scale;
      float h = content[1] * scale;
      HudManager.placeAt(element, centerX - w / 2.0F, centerY - h / 2.0F, w, h, this.width, this.height);
      this.lastScaleChange = System.currentTimeMillis();
   }

   private void startDrag(HudElement element, float mouseX, float mouseY) {
      float[] b = element.bounds();
      List<float[]> others = new ArrayList<>();

      for (HudElement other : HudManager.elements()) {
         float[] ob = other.bounds();
         if (other != element && other.isEnabled() && ob[2] > 0.0F) {
            others.add((float[])ob.clone());
         }
      }

      this.drag = new HudEditorScreen.Drag(element, mouseX - b[0], mouseY - b[1], (float[])b.clone(), others);
      HudManager.setFollowTight(element, true);
   }

   private void updateDrag(float mouseX, float mouseY) {
      float x = mouseX - this.drag.grabX;
      float y = mouseY - this.drag.grabY;
      if (!this.drag.moved) {
         if (Math.hypot(x - this.drag.startX, y - this.drag.startY) < 2.0) {
            return;
         }

         this.drag.moved = true;
         this.hintDismissed = true;
         UiSounds.play(UiSounds.Sound.HOVER);
      }

      HudSettingsModule settings = Modules.hud();
      boolean free = Minecraft.getInstance().hasAltDown();
      float previousX = this.guideX;
      float previousY = this.guideY;
      this.guideX = Float.NaN;
      this.guideY = Float.NaN;
      if (settings.snap() && !free) {
         List<Float> edgesX = new ArrayList<>(List.of(0.0F, (float)this.width));
         List<Float> centersX = new ArrayList<>(List.of(this.width / 2.0F));
         List<Float> edgesY = new ArrayList<>(List.of(0.0F, (float)this.height));
         List<Float> centersY = new ArrayList<>(List.of(this.height / 2.0F));

         for (float[] other : this.drag.others) {
            edgesX.add(other[0]);
            edgesX.add(other[0] + other[2]);
            centersX.add(other[0] + other[2] / 2.0F);
            edgesY.add(other[1]);
            edgesY.add(other[1] + other[3]);
            centersY.add(other[1] + other[3] / 2.0F);
         }

         float[] snappedX = snapAxis(x, this.drag.width, edgesX, centersX);
         if (snappedX != null) {
            x = snappedX[0];
            this.guideX = snappedX[1];
         }

         float[] snappedY = snapAxis(y, this.drag.height, edgesY, centersY);
         if (snappedY != null) {
            y = snappedY[0];
            this.guideY = snappedY[1];
         }
      }

      if (settings.grid() && !free) {
         int step = settings.gridSize();
         if (Float.isNaN(this.guideX)) {
            x = Math.round(x / step) * step;
         }

         if (Float.isNaN(this.guideY)) {
            y = Math.round(y / step) * step;
         }
      }

      if (!Float.isNaN(this.guideX) && this.guideX != previousX || !Float.isNaN(this.guideY) && this.guideY != previousY) {
         UiSounds.play(UiSounds.Sound.HOVER);
      }

      x = Math.clamp(x, 0.0F, Math.max(0.0F, this.width - this.drag.width));
      y = Math.clamp(y, 0.0F, Math.max(0.0F, this.height - this.drag.height));
      HudManager.placeAt(this.drag.element, x, y, this.drag.width, this.drag.height, this.width, this.height);
   }

   private static float[] snapAxis(float start, float size, List<Float> edges, List<Float> centers) {
      float best = 5.001F;
      float line = Float.NaN;

      for (float edge : edges) {
         float toStart = edge - start;
         float toEnd = edge - (start + size);
         if (Math.abs(toStart) < Math.abs(best)) {
            best = toStart;
            line = edge;
         }

         if (Math.abs(toEnd) < Math.abs(best)) {
            best = toEnd;
            line = edge;
         }
      }

      for (float center : centers) {
         float toCenter = center - (start + size / 2.0F);
         if (Math.abs(toCenter) < Math.abs(best)) {
            best = toCenter;
            line = center;
         }
      }

      return Float.isNaN(line) ? null : new float[]{start + best, line};
   }

   private void finishDrag() {
      HudManager.setFollowTight(this.drag.element, false);
      if (this.drag.moved) {
         UiSounds.play(UiSounds.Sound.TAB);
      }

      this.drag = null;
      this.guideX = Float.NaN;
      this.guideY = Float.NaN;
   }

   private void startScaling(int corner) {
      float[] b = this.selected.bounds();
      boolean left = corner == 0 || corner == 3;
      boolean topSide = corner == 0 || corner == 1;
      float fixedX = left ? b[0] + b[2] : b[0];
      float fixedY = topSide ? b[1] + b[3] : b[1];
      float handleX = left ? b[0] : b[0] + b[2];
      float handleY = topSide ? b[1] : b[1] + b[3];
      float[] content = HudManager.contentSize(this.selected);
      float distance = (float)Math.max(4.0, Math.hypot(handleX - fixedX, handleY - fixedY));
      this.scaling = new HudEditorScreen.Scaling(this.selected, left, topSide, fixedX, fixedY, content[0], content[1], this.selected.scale(), distance);
      HudManager.setFollowTight(this.selected, true);
      this.hintDismissed = true;
   }

   private void updateScaling(float mouseX, float mouseY) {
      float distance = (float)Math.hypot(mouseX - this.scaling.fixedX(), mouseY - this.scaling.fixedY());
      float scale = Math.clamp(Math.round(this.scaling.startScale() * distance / this.scaling.startDistance() * 20.0F) / 20.0F, 0.5F, 3.0F);
      HudElement element = this.scaling.element();
      if (scale != element.scale()) {
         element.setScale(scale);
         UiSounds.play(UiSounds.Sound.SLIDER);
      }

      float w = this.scaling.contentWidth() * scale;
      float h = this.scaling.contentHeight() * scale;
      float x = this.scaling.leftHandle() ? this.scaling.fixedX() - w : this.scaling.fixedX();
      float y = this.scaling.topHandle() ? this.scaling.fixedY() - h : this.scaling.fixedY();
      HudManager.placeAt(element, x, y, w, h, this.width, this.height);
      this.lastScaleChange = System.currentTimeMillis();
   }

   private void finishScaling() {
      HudManager.setFollowTight(this.scaling.element(), false);
      this.scaling = null;
   }

   private HudElement elementAt(double x, double y) {
      List<HudElement> elements = HudManager.elements();

      for (int i = elements.size() - 1; i >= 0; i--) {
         HudElement element = elements.get(i);
         float[] b = element.bounds();
         if (element.isEnabled() && !(b[2] <= 0.0F) && x >= b[0] - 2.0F && y >= b[1] - 2.0F && x < b[0] + b[2] + 2.0F && y < b[1] + b[3] + 2.0F) {
            return element;
         }
      }

      return null;
   }

   private int handleAt(double x, double y) {
      if (this.selected == null) {
         return -1;
      }

      float[] b = this.selected.bounds();
      if (b[2] <= 0.0F) {
         return -1;
      }

      float pad = 2.0F;
      float reach = 7.0F;
      float[][] points = new float[][]{
         {b[0] - pad, b[1] - pad}, {b[0] + b[2] + pad, b[1] - pad}, {b[0] + b[2] + pad, b[1] + b[3] + pad}, {b[0] - pad, b[1] + b[3] + pad}
      };

      for (int i = 0; i < 4; i++) {
         if (Math.abs(x - points[i][0]) <= reach && Math.abs(y - points[i][1]) <= reach) {
            return i;
         }
      }

      return -1;
   }

   private boolean overSettings(float cx, float cy) {
      return this.inspected != null && (this.panelUi.popupOpen() || this.panelUi.dragging() || inside(this.inspectorRect, cx, cy));
   }

   private boolean pointerOverUi(float x, float y) {
      return this.ctx.targetAt(x, y) != null || this.overSettings(x, y) || inside(this.inspectorRect, x, y) || inside(this.popoverRect, x, y);
   }

   private static boolean inside(float[] rect, float x, float y) {
      return rect != null && x >= rect[0] && y >= rect[1] && x < rect[0] + rect[2] && y < rect[1] + rect[3];
   }

   private GuiContext.Interaction pressable(String id, Runnable action) {
      return new GuiContext.Interaction() {
         @Override
         public void press(float localX, float localY) {
            HudEditorScreen.this.ctx.pressed = id;
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            HudEditorScreen.this.ctx.pressed = null;
            if (inside) {
               UiSounds.play(UiSounds.Sound.CLICK);
               action.run();
            }
         }
      };
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (this.closing) {
         return true;
      }

      float x = (float)mouseX;
      float y = (float)mouseY;
      float cx = x / this.uiScale;
      float cy = y / this.uiScale;
      this.history.begin();
      GuiContext.Target target = this.ctx.targetAt(cx, cy);
      if (target == null && this.overSettings(cx, cy)) {
         this.panelUi.blurOutside(this.panelUi.targetAt(cx, cy));
         if (!this.panelUi.press(cx, cy, button)) {
            this.panelUi.closePopups();
         }

         return true;
      } else {
         this.panelUi.blurOutside(null);
         this.panelUi.closePopups();
         boolean overPanel = target != null || inside(this.inspectorRect, cx, cy) || inside(this.popoverRect, cx, cy);
         if (button == 1) {
            if (!overPanel) {
               HudElement element = this.elementAt(x, y);
               if (element != null) {
                  this.openInspector(element);
               }
            } else {
               this.ctx.secondaryAt(cx, cy, button);
            }

            return true;
         } else {
            if (button != 0) {
               return true;
            }

            if (this.popover != HudEditorScreen.Popover.NONE && !inside(this.popoverRect, cx, cy)) {
               boolean ownButton = target != null && (target.id().equals("tool:elements") || target.id().equals("tool:layouts"));
               if (!ownButton) {
                  this.popover = HudEditorScreen.Popover.NONE;
                  if (target == null || !target.id().startsWith("tool:")) {
                     return true;
                  }
               }
            }

            if (overPanel) {
               this.input.press(cx, cy, false);
               return true;
            }

            this.input.press(cx, cy, false);
            int corner = this.handleAt(x, y);
            if (corner >= 0) {
               this.startScaling(corner);
               return true;
            }

            HudElement element = this.elementAt(x, y);
            if (element != null) {
               long now = System.currentTimeMillis();
               if (element == this.lastClicked && now - this.lastClickTime < 320L) {
                  this.openInspector(element);
               }

               this.lastClicked = element;
               this.lastClickTime = now;
               if (this.selected != element) {
                  UiSounds.play(UiSounds.Sound.CLICK);
               }

               this.select(element);
               this.startDrag(element, x, y);
               return true;
            } else {
               this.select(null);
               return true;
            }
         }
      }
   }

   public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (this.drag != null) {
         this.updateDrag((float)mouseX, (float)mouseY);
      } else if (this.scaling != null) {
         this.updateScaling((float)mouseX, (float)mouseY);
      } else if (this.panelUi.dragging()) {
         this.panelUi.drag((float)mouseX / this.uiScale, (float)mouseY / this.uiScale);
      } else {
         this.input.drag((float)mouseX / this.uiScale, (float)mouseY / this.uiScale);
      }

      return true;
   }

   public boolean mouseReleased(MouseButtonEvent event) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      String kind = null;
      if (this.drag != null) {
         this.finishDrag();
      } else if (this.scaling != null) {
         this.finishScaling();
      } else if (this.panelUi.dragging()) {
         kind = this.panelUi.activeId();
         this.panelUi.release((float)mouseX / this.uiScale, (float)mouseY / this.uiScale);
      } else {
         kind = this.ctx.activeId;
         this.input.release((float)mouseX / this.uiScale, (float)mouseY / this.uiScale);
      }

      this.history.end(kind != null && kind.startsWith("setting:") ? kind : null);
      return true;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      if (this.closing) {
         return true;
      }

      float cx = (float)mouseX / this.uiScale;
      float cy = (float)mouseY / this.uiScale;
      if (this.overSettings(cx, cy)) {
         if (!this.panelUi.scroll(cx, cy, scrollY) && inside(this.inspectorRect, cx, cy)) {
            this.inspectorScroll.scroll(scrollY);
         }

         return true;
      } else if (inside(this.popoverRect, cx, cy)) {
         if (this.shownPopover == HudEditorScreen.Popover.ELEMENTS) {
            this.elementsScroll.scroll(scrollY);
         }

         return true;
      } else {
         HudElement element = this.elementAt(mouseX, mouseY);
         if (element == null) {
            element = this.selected;
         }

         if (element != null && scrollY != 0.0) {
            this.history.begin();
            this.scaleBy(element, scrollY > 0.0 ? 0.05F : -0.05F);
            this.history.end("scale:" + element.id());
            this.select(element);
            UiSounds.play(UiSounds.Sound.SLIDER);
            return true;
         } else {
            return true;
         }
      }
   }

   public boolean keyPressed(KeyEvent event) {
      int key = event.key();
      int scancode = event.scancode();
      int modifiers = event.modifiers();
      if (this.closing) {
         return true;
      }

      boolean control = (modifiers & 2) != 0;
      boolean shift = (modifiers & 1) != 0;
      if (this.panelUi.focused != null || this.panelUi.listening != null) {
         this.history.begin();
         this.panelUi.keyPressed(key, modifiers);
         this.history.end("text");
         return true;
      }

      if (key == 256 && this.panelUi.popupOpen()) {
         this.panelUi.closePopups();
         return true;
      }

      if (this.ctx.focusedInput == null && this.ctx.listeningKey == null && control && (key == 90 || key == 89)) {
         if (key != 89 && !shift) {
            this.undo();
         } else {
            this.redo();
         }

         return true;
      } else {
         this.history.begin();
         String kind = null;

         try {
            if (this.input.assignKey(key)) {
               return true;
            }

            if (this.ctx.focusedInput != null) {
               kind = "text";
               return this.input.editText(key, modifiers);
            }

            switch (key) {
               case 71:
                  HudSettingsModule settings = Modules.hud();
                  settings.setGrid(!settings.grid());
                  UiSounds.play(UiSounds.Sound.CLICK);
                  break;
               case 256:
                  if (this.popover != HudEditorScreen.Popover.NONE) {
                     this.popover = HudEditorScreen.Popover.NONE;
                  } else if (this.inspected != null) {
                     this.closeInspector();
                  } else if (this.selected != null) {
                     this.select(null);
                  } else {
                     this.onClose();
                  }
                  break;
               case 257:
               case 335:
                  if (this.selected != null) {
                     this.openInspector(this.selected);
                  }
                  break;
               case 258:
                  this.selectNext(shift ? -1 : 1);
                  break;
               case 259:
               case 261:
                  if (this.selected != null) {
                     this.hide(this.selected);
                  }
                  break;
               case 262:
               case 263:
               case 264:
               case 265:
                  float step = shift ? 10.0F : 1.0F;
                  float dx = key == 263 ? -step : (key == 262 ? step : 0.0F);
                  float dy = key == 265 ? -step : (key == 264 ? step : 0.0F);
                  this.nudge(dx, dy);
                  kind = this.selected == null ? null : "nudge:" + this.selected.id();
                  break;
               default:
                  if (key == Modules.hud().key().get() && System.currentTimeMillis() - this.openedAt > 250L) {
                     this.onClose();
                  }
            }

            return true;
         } finally {
            this.history.end(kind);
         }
      }
   }

   public boolean charTyped(CharacterEvent event) {
      char character = (char)event.codepoint();
      int modifiers = event.modifiers();
      this.history.begin();
      if (!this.panelUi.charTyped(character)) {
         this.input.type(character);
      }

      this.history.end("text");
      return true;
   }

   private void selectNext(int direction) {
      List<HudElement> visible = HudManager.elements().stream().filter(element -> element.isEnabled() && element.bounds()[2] > 0.0F).toList();
      if (!visible.isEmpty()) {
         int index = this.selected == null ? (direction > 0 ? -1 : 0) : visible.indexOf(this.selected);
         this.select(visible.get(Math.floorMod(index + direction, visible.size())));
         UiSounds.play(UiSounds.Sound.HOVER);
      }
   }

   public void onClose() {
      if (!this.closing) {
         this.closing = true;
         this.popover = HudEditorScreen.Popover.NONE;
         this.ctx.unfocus();
         this.ctx.listeningKey = null;
         this.appear.configure(0.3, 1.0);
         this.appear.setTarget(0.0);
         UiSounds.play(UiSounds.Sound.CLOSE);
      }
   }

   void testClick(String id) {
      GuiContext.Target target = this.ctx.targetById(id);
      MenuUi.Target setting = target == null ? this.panelUi.find(id) : null;
      if (target == null && setting == null) {
         RoseClient.LOGGER.warn("[Rose][hudtest] target not found: {}", id);
      } else {
         double x = (target != null ? target.x() + target.w() / 2.0F : setting.x() + setting.w() / 2.0F) * this.uiScale;
         double y = (target != null ? target.y() + target.h() / 2.0F : setting.y() + setting.h() / 2.0F) * this.uiScale;
         this.testMouse = new double[]{x, y};
         this.mouseClicked(new MouseButtonEvent(x, y, new MouseButtonInfo(0, 0)), false);
         this.mouseReleased(new MouseButtonEvent(x, y, new MouseButtonInfo(0, 0)));
      }
   }

   void testPressElement(String id, float fx, float fy) {
      HudElement element = element(id);
      if (element != null) {
         float[] b = element.bounds();
         this.testMouse = new double[]{b[0] + b[2] * fx, b[1] + b[3] * fy};
         this.mouseClicked(new MouseButtonEvent(this.testMouse[0], this.testMouse[1], new MouseButtonInfo(0, 0)), false);
      }
   }

   void testDragTo(float fx, float fy) {
      if (this.testMouse != null) {
         double x0 = this.testMouse[0];
         double y0 = this.testMouse[1];
         double x1 = this.width * fx;
         double y1 = this.height * fy;

         for (int i = 1; i <= 10; i++) {
            this.mouseDragged(new MouseButtonEvent(x0 + (x1 - x0) * i / 10.0, y0 + (y1 - y0) * i / 10.0, new MouseButtonInfo(0, 0)), 0.0, 0.0);
         }

         this.testMouse = new double[]{x1, y1};
      }
   }

   void testDragBy(float dx, float dy) {
      if (this.testMouse != null) {
         double x0 = this.testMouse[0];
         double y0 = this.testMouse[1];

         for (int i = 1; i <= 8; i++) {
            this.mouseDragged(new MouseButtonEvent(x0 + dx * i / 8.0F, y0 + dy * i / 8.0F, new MouseButtonInfo(0, 0)), 0.0, 0.0);
         }

         this.testMouse = new double[]{x0 + dx, y0 + dy};
      }
   }

   void testPressHandle(int corner) {
      if (this.selected != null) {
         float[] b = this.selected.bounds();
         float pad = 2.0F;
         float[][] points = new float[][]{
            {b[0] - pad, b[1] - pad}, {b[0] + b[2] + pad, b[1] - pad}, {b[0] + b[2] + pad, b[1] + b[3] + pad}, {b[0] - pad, b[1] + b[3] + pad}
         };
         this.testMouse = new double[]{points[corner][0], points[corner][1]};
         this.mouseClicked(new MouseButtonEvent(this.testMouse[0], this.testMouse[1], new MouseButtonInfo(0, 0)), false);
      }
   }

   void testRelease() {
      if (this.testMouse != null) {
         this.mouseReleased(new MouseButtonEvent(this.testMouse[0], this.testMouse[1], new MouseButtonInfo(0, 0)));
      }
   }

   void testScrollElement(String id, double notches) {
      HudElement element = element(id);
      if (element != null) {
         float[] b = element.bounds();
         this.testMouse = new double[]{b[0] + b[2] / 2.0F, b[1] + b[3] / 2.0F};
         this.mouseScrolled(this.testMouse[0], this.testMouse[1], 0.0, notches);
      }
   }

   void testShot(String name) {
      this.minecraft
         .schedule(() -> Screenshot.grab(this.minecraft.gameDirectory, "rose_" + name + ".png", this.minecraft.getMainRenderTarget(), 1, text -> {}));
   }

   void testScrollInspector(double notches) {
      if (this.inspectorRect != null) {
         this.mouseScrolled(
            (this.inspectorRect[0] + this.inspectorRect[2] / 2.0F) * this.uiScale,
            (this.inspectorRect[1] + this.inspectorRect[3] / 2.0F) * this.uiScale,
            0.0,
            notches
         );
      }
   }

   void testRightClickElement(String id) {
      HudElement element = element(id);
      if (element != null) {
         float[] b = element.bounds();
         this.mouseClicked(new MouseButtonEvent(b[0] + b[2] / 2.0F, b[1] + b[3] / 2.0F, new MouseButtonInfo(1, 0)), false);
      }
   }

   void testHoverElement(String id) {
      HudElement element = element(id);
      if (element != null) {
         float[] b = element.bounds();
         this.testMouse = new double[]{b[0] + b[2] / 2.0F, b[1] + b[3] / 2.0F};
      }
   }

   void testMoveMouse(float fx, float fy) {
      this.testMouse = new double[]{this.width * fx, this.height * fy};
   }

   void testKey(int key, int modifiers) {
      this.keyPressed(new KeyEvent(key, 0, modifiers));
   }

   private static HudElement element(String id) {
      for (HudElement element : HudManager.elements()) {
         if (element.id().equals(id)) {
            return element;
         }
      }

      RoseClient.LOGGER.warn("[Rose][hudtest] element not found: {}", id);
      return null;
   }

   private static float centered(float y, float h, float size) {
      return y + h / 2.0F - size * 0.62F;
   }

   private static final class Drag {
      final HudElement element;
      final float grabX;
      final float grabY;
      final float width;
      final float height;
      final float startX;
      final float startY;
      final List<float[]> others;
      boolean moved;

      Drag(HudElement element, float grabX, float grabY, float[] bounds, List<float[]> others) {
         this.element = element;
         this.grabX = grabX;
         this.grabY = grabY;
         this.width = bounds[2];
         this.height = bounds[3];
         this.startX = bounds[0];
         this.startY = bounds[1];
         this.others = others;
      }
   }

   private enum Popover {
      NONE,
      ELEMENTS,
      LAYOUTS;
   }

   private record Scaling(
      HudElement element,
      boolean leftHandle,
      boolean topHandle,
      float fixedX,
      float fixedY,
      float contentWidth,
      float contentHeight,
      float startScale,
      float startDistance
   ) {
   }

   private record Tool(String id, int icon, String tooltip, BooleanSupplier active, BooleanSupplier enabled, Runnable action) {
   }
}
