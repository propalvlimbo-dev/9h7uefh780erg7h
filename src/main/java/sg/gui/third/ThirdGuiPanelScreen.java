package sg.gui.third;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import sg.client.RoseClient;
import sg.render.WorldProjection;
import sg.render.pipeline.ThemeWave;
import sg.render.pipeline.UiDrawing;

/** Separate editor window; closing it returns to the Third GUI's module category. */
public final class ThirdGuiPanelScreen extends Screen {
   private static final ThirdGuiCategory[] CATEGORIES = {
      ThirdGuiCategory.THEMES, ThirdGuiCategory.AUTOBUY, ThirdGuiCategory.COSMETICS
   };
   // Icon-only switcher that lives outside the window: a vertical rail on the left edge,
   // centred against the window height. Everything here is 20% smaller than the reference layout.
   private static final float RAIL_BUTTON = 37.0F;
   private static final float RAIL_SPACING = 8.0F;
   private static final float RAIL_GAP = 13.0F;
   private static final float RAIL_ICON_SIZE = 17.0F;
   private static final float RAIL_TOTAL = RAIL_BUTTON + RAIL_GAP;
   private static final float RAIL_LEFT = -RAIL_TOTAL;
   private static final float RAIL_RADIUS = 12.0F;
   private static final int RAIL_ACTIVE_FILL = 0xFF23252D;
   private static final int RAIL_HOVER_FILL = 0xFF1E1F24;
   private static final int RAIL_ACTIVE_BORDER = 0x3D5B7BF5;
   private static final int RAIL_IDLE_BORDER = 0x0FFFFFFF;
   private static final float HEADER_HEIGHT = 42.0F;
   private static final float HEADER_CENTER_Y = HEADER_HEIGHT * 0.5F;
   private static final float TITLE_TEXT_SIZE = 14.0F;
   private static final float CONTENT_INSET = 16.0F;
   private static final float CONTENT_TOP = 50.0F;
   private final Screen parent;
   private final ThirdGuiEditors editors = new ThirdGuiEditors();
   private final ThirdGuiAnim openness = new ThirdGuiAnim(0.0F, 12.0F);
   private ThirdGuiCategory category;
   private boolean closing;
   private long lastFrame;
   private float scale = 1.0F;
   private float x;
   private float y;
   private float windowWidth;
   private static final float HEIGHT = 475.0F;
   private static final float COSMETICS_WIDTH = 872.0F;
   private static final float DEFAULT_WIDTH = 672.0F;
   /** Reveal progress of the editor content; drives the cross-category fade and slide. */
   private final ThirdGuiAnim reveal = new ThirdGuiAnim(0.0F, 9.0F);
   /** Cosmetics is wider than the other panels, so the window itself morphs between widths. */
   private final ThirdGuiAnim widthAnim = new ThirdGuiAnim(0.0F, 10.0F);
   private float slideDirection = 1.0F;

   ThirdGuiPanelScreen(Screen parent, ThirdGuiCategory category) {
      super(Component.literal("Rose — " + category.title()));
      this.parent = parent;
      this.category = category;
      this.editors.select(category);
      this.openness.set(1.0F);
      this.reveal.set(1.0F);
      this.widthAnim.snap(this.targetWidth());
   }

   @Override public boolean isPauseScreen() { return false; }
   @Override protected void extractBlurredBackground(GuiGraphicsExtractor graphics) { }
   @Override protected void extractPanorama(GuiGraphicsExtractor graphics, float tickDelta) { }
   @Override protected void extractMenuBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height) { }

   private float targetWidth() {
      return this.category == ThirdGuiCategory.COSMETICS ? COSMETICS_WIDTH : DEFAULT_WIDTH;
   }

   /** Vertical offset of the editor content while the category reveal is playing. */
   private float contentOffset() {
      return (1.0F - ThirdGuiAnim.outCubic(this.reveal.value)) * 16.0F * this.slideDirection;
   }

   private void layout() {
      this.layout(0.0F);
   }

   private void layout(float dt) {
      var window = Minecraft.getInstance().getWindow();
      if (this.widthAnim.value <= 0.0F) this.widthAnim.snap(this.targetWidth());
      this.widthAnim.set(this.targetWidth());
      this.windowWidth = this.widthAnim.update(dt);
      this.scale = Math.max(0.1F, Math.min(1.35F,
         Math.min((window.getScreenWidth() - 60.0F - RAIL_TOTAL) / this.windowWidth, (window.getScreenHeight() - 80.0F) / HEIGHT)));
      float groupWidth = this.windowWidth + RAIL_TOTAL;
      float groupX = (window.getScreenWidth() - groupWidth * this.scale) / 2.0F;
      this.x = groupX + RAIL_TOTAL * this.scale;
      this.y = (window.getScreenHeight() - HEIGHT * this.scale) / 2.0F + (1.0F - this.openness.value) * 18.0F;
      this.editors.layout(CONTENT_INSET, CONTENT_TOP, this.windowWidth - CONTENT_INSET * 2.0F, HEIGHT - CONTENT_TOP - CONTENT_INSET, this.openness.value);
      this.editors.setFramebufferTransform(this.scale * this.editors.scale(),
         this.x + this.editors.x() * this.scale, this.y + (this.editors.y() + this.contentOffset()) * this.scale);
   }

   private static int indexOf(ThirdGuiCategory category) {
      for (int i = 0; i < CATEGORIES.length; i++) {
         if (CATEGORIES[i] == category) return i;
      }
      return 0;
   }

   private Matrix4f transform(float scale, float x, float y) {
      var window = Minecraft.getInstance().getWindow();
      float width = window.getScreenWidth();
      float height = window.getScreenHeight();
      return new Matrix4f().translate(-1.0F, 1.0F, 0.0F).scale(2.0F / width, -2.0F / height, 1.0F)
         .translate(x, y, 0.0F).scale(scale, scale, 1.0F)
         .scale(width * 0.5F, -height * 0.5F, 1.0F).translate(1.0F, -1.0F, 0.0F);
   }

   /** Top of the vertically centred rail group. */
   private static float railTop() {
      float groupHeight = CATEGORIES.length * RAIL_BUTTON + (CATEGORIES.length - 1) * RAIL_SPACING;
      return (HEIGHT - groupHeight) * 0.5F;
   }

   private static float railButtonY(int index) {
      return railTop() + index * (RAIL_BUTTON + RAIL_SPACING);
   }

   private void drawRail(Vector2f mouse) {
      for (int i = 0; i < CATEGORIES.length; i++) {
         float buttonY = railButtonY(i);
         boolean active = CATEGORIES[i] == this.category;
         boolean hovered = mouse.x >= RAIL_LEFT && mouse.x <= RAIL_LEFT + RAIL_BUTTON
            && mouse.y >= buttonY && mouse.y <= buttonY + RAIL_BUTTON;
         int fill = active ? RAIL_ACTIVE_FILL : hovered ? RAIL_HOVER_FILL : ThirdGuiColors.DOCK;
         UiDrawing.drawRoundedRect(RAIL_LEFT, buttonY, RAIL_BUTTON, RAIL_BUTTON, RAIL_RADIUS, fill);
         UiDrawing.drawOutline(RAIL_LEFT, buttonY, RAIL_BUTTON, RAIL_BUTTON, 1.0F, RAIL_RADIUS,
            active ? RAIL_ACTIVE_BORDER : RAIL_IDLE_BORDER);
         ThirdGuiIcons.drawCentered(CATEGORIES[i].icon(), RAIL_LEFT + RAIL_BUTTON * 0.5F, buttonY + RAIL_BUTTON * 0.5F,
            RAIL_ICON_SIZE, active ? ThirdGuiColors.ACCENT : ThirdGuiColors.ICON_IDLE);
      }
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta) {
      var window = Minecraft.getInstance().getWindow();
      if (window.isIconified() || window.getScreenWidth() <= 0 || window.getScreenHeight() <= 0) return;
      long now = System.nanoTime();
      float dt = this.lastFrame == 0L ? 0.016F : Math.clamp((now - this.lastFrame) / 1.0E9F, 0.0F, 0.1F);
      this.lastFrame = now;
      this.openness.update(dt);
      this.reveal.update(dt);
      if (this.closing && this.openness.value <= 0.004F) {
         Minecraft.getInstance().gui.setScreen(this.parent);
         return;
      }
      this.layout(dt);
      Vector2f mouse = this.mouse(mouseX, mouseY);
      Vector2f contentMouse = new Vector2f(mouse.x, mouse.y - this.contentOffset());
      ThemeWave.INSTANCE.beginFrame();
      UiDrawing.resetTransforms();
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      UiDrawing.drawRoundedRect(0.0F, 0.0F, window.getScreenWidth(), window.getScreenHeight(), 0.0F,
         ThirdGuiColors.alpha(0xFF000000, 0.38F * this.openness.value));
      UiDrawing.setGlobalAlpha(255.0F * this.openness.value);
      UiDrawing.setTransform(this.transform(this.scale, this.x, this.y));
      UiDrawing.drawShadow(0.0F, 6.5F, this.windowWidth, HEIGHT, 20.0F, 0x99000000);
      UiDrawing.drawRoundedRect(0.0F, 0.0F, this.windowWidth, HEIGHT, 15.0F, ThirdGuiColors.PANEL);
      UiDrawing.drawOutline(0.0F, 0.0F, this.windowWidth, HEIGHT, 1.0F, 15.0F, 0x16FFFFFF);
      this.drawRail(mouse);
      String title = this.category.title();
      UiDrawing.drawText(ThirdGuiFonts.semibold(), title, CONTENT_INSET,
         UiDrawing.alignTextCenterY(ThirdGuiFonts.semibold(), title, HEADER_CENTER_Y, TITLE_TEXT_SIZE),
         TITLE_TEXT_SIZE, ThirdGuiColors.TEXT, 0.0F, 0.5F, 1.0F);
      // The multiplication glyph's visual centre is lower than its cap-height centre.
      UiDrawing.drawText(ThirdGuiFonts.semibold(), "×",
         this.windowWidth - 26.0F - ThirdGuiScreen.width(ThirdGuiFonts.semibold(), "×", 21.0F) / 2.0F,
         UiDrawing.alignTextCenterY(ThirdGuiFonts.semibold(), "×", HEADER_CENTER_Y - 2.0F, 21.0F),
         21.0F, ThirdGuiColors.TEXT, 0.0F, 0.5F, 1.0F);
      UiDrawing.drawRoundedRect(CONTENT_INSET, HEADER_HEIGHT, this.windowWidth - CONTENT_INSET * 2.0F, 1.0F, 0.0F, ThirdGuiColors.BORDER);
      float revealAlpha = ThirdGuiAnim.outCubic(this.reveal.value);
      UiDrawing.setGlobalAlpha(255.0F * this.openness.value * revealAlpha);
      UiDrawing.setTransform(this.transform(this.scale * this.editors.scale(),
         this.x + this.editors.x() * this.scale, this.y + (this.editors.y() + this.contentOffset()) * this.scale));
      try {
         this.editors.render(graphics, this.closing ? -10000.0F : contentMouse.x, this.closing ? -10000.0F : contentMouse.y, dt);
         this.editors.renderOverlays(graphics, contentMouse.x, contentMouse.y);
      } finally {
         UiDrawing.disableScissor();
         UiDrawing.resetGlobalAlpha();
         UiDrawing.resetTransforms();
      }
      ThemeWave.INSTANCE.renderTransition();
   }

   private Vector2f mouse(double guiX, double guiY) {
      Vector2f pixel = WorldProjection.guiToScreen(guiX, guiY);
      return new Vector2f((pixel.x - this.x) / this.scale, (pixel.y - this.y) / this.scale);
   }

   @Override public void tick() { if (!this.closing) this.editors.tick(); }

   @Override public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
      if (this.closing) return true;
      this.layout();
      Vector2f mouse = this.mouse(event.x(), event.y());
      float contentY = mouse.y - this.contentOffset();
      if (this.editors.hasDialog()) {
         this.editors.mouseClicked(mouse.x, contentY, event.button());
         return true;
      }
      if (event.button() == 0) {
         for (int i = 0; i < CATEGORIES.length; i++) {
            float buttonY = railButtonY(i);
            if (mouse.x >= RAIL_LEFT && mouse.x <= RAIL_LEFT + RAIL_BUTTON
               && mouse.y >= buttonY && mouse.y <= buttonY + RAIL_BUTTON) {
               if (CATEGORIES[i] != this.category) {
                  this.slideDirection = i > indexOf(this.category) ? 1.0F : -1.0F;
                  this.category = CATEGORIES[i];
                  this.editors.select(this.category);
                  this.reveal.snap(0.0F);
                  this.reveal.set(1.0F);
                  this.layout();
               }
               return true;
            }
         }
         if (mouse.y >= 0.0F && mouse.y <= HEADER_HEIGHT
            && mouse.x >= this.windowWidth - 42.0F && mouse.x <= this.windowWidth - 10.0F) {
            this.onClose();
            return true;
         }
      }
      this.editors.mouseClicked(mouse.x, contentY, event.button());
      return true;
   }

   @Override public boolean mouseReleased(MouseButtonEvent event) {
      Vector2f mouse = this.mouse(event.x(), event.y());
      this.editors.mouseReleased(mouse.x, mouse.y - this.contentOffset(), event.button());
      return true;
   }
   @Override public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) { return true; }
   @Override public boolean mouseScrolled(double x, double y, double horizontal, double vertical) {
      if (!this.closing) {
         Vector2f mouse = this.mouse(x, y);
         this.editors.mouseScrolled(mouse.x, mouse.y - this.contentOffset(), vertical);
      }
      return true;
   }
   @Override public boolean keyPressed(KeyEvent event) {
      if (this.closing) return true;
      if (event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) this.onClose();
      else if (this.editors.keyPressed(event)) return true;
      else if (event.key() == GLFW.GLFW_KEY_ESCAPE) this.onClose();
      return true;
   }
   @Override public boolean charTyped(CharacterEvent event) {
      if (!this.closing) this.editors.charTyped(event.codepointAsString());
      return true;
   }
   @Override public void onClose() {
      if (!this.closing) {
         this.editors.clearFocus();
         this.closing = true;
         this.openness.set(0.0F);
      }
   }
   @Override public void removed() {
      this.editors.clearFocus();
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
      super.removed();
   }
}
