package sg.gui.third;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import sg.gui.ClickGuiPanelHost;
import sg.gui.autobuy.AutoBuyPanel;
import sg.gui.cosmetics.CosmeticsPanel;
import sg.render.pipeline.ThemeWave;
import sg.util.time.ThemeEditor;

/** Reuses the client editors; Third-specific host bounds remove the unused embedded header. */
final class ThirdGuiEditors implements ClickGuiPanelHost {
   private AutoBuyPanel autoBuy;
   private CosmeticsPanel cosmetics;
   private ThemeEditor themes;
   private ThirdGuiCategory category;
   private float x;
   private float y;
   private float scale = 1.0F;
    private float alpha = 1.0F;
    private float contentWidth = 800.0F;
    private float contentHeight = 504.0F;
   private float scroll;
   private float scrollTarget;
   private float framebufferScale = 1.0F;
   private float framebufferX;
   private float framebufferY;

   void select(ThirdGuiCategory category) {
      this.clearFocus();
      this.category = category;
      this.resetGuiScroll();
      if (category == ThirdGuiCategory.THEMES) {
         if (this.themes == null) this.themes = new ThemeEditor(this);
         this.themes.restartEntranceTransitions();
      } else if (category == ThirdGuiCategory.COSMETICS && this.cosmetics == null) {
         this.cosmetics = new CosmeticsPanel(this);
      } else if (category == ThirdGuiCategory.AUTOBUY && this.autoBuy == null) {
         this.autoBuy = new AutoBuyPanel(this);
      }
   }

   void layout(float panelX, float panelY, float width, float height, float alpha) {
      // Cosmetics has a native 230px preview panel 20px to the right of its main pane.
      float nativeWidth = this.category == ThirdGuiCategory.COSMETICS ? 1050.0F : 800.0F;
      this.scale = Math.min(width / nativeWidth, height / 504.0F);
      this.x = panelX + (width - nativeWidth * this.scale) * 0.5F;
       this.y = panelY;
       this.alpha = alpha;
       this.contentWidth = width / this.scale;
       this.contentHeight = height / this.scale;
   }

   float x() { return this.x; }
   float y() { return this.y; }
   float scale() { return this.scale; }
   void setFramebufferTransform(float scale, float x, float y) {
      this.framebufferScale = scale;
      this.framebufferX = x;
      this.framebufferY = y;
   }
   private float mouseX(float x) { return (x - this.x) / this.scale; }
   private float mouseY(float y) { return (y - this.y) / this.scale; }

   void render(GuiGraphicsExtractor graphics, float x, float y, float dt) {
      float mouseX = this.mouseX(x);
      float mouseY = this.mouseY(y);
      this.scroll += (this.scrollTarget - this.scroll) * (1.0F - (float)Math.exp(-14.0F * dt));
      switch (this.category) {
         case THEMES -> this.themes.render(graphics, mouseX, mouseY, this.scroll);
         case COSMETICS -> {
            this.cosmetics.衣(graphics, mouseX, mouseY, this.scroll);
            float minScroll = -Math.max(0.0F, this.cosmetics.Ь2() - this.getModuleViewportHeight());
            this.scrollTarget = Math.clamp(this.scrollTarget, minScroll, 0.0F);
            this.scroll = Math.clamp(this.scroll, minScroll, 0.0F);
         }
         case AUTOBUY -> this.autoBuy.诶(graphics, mouseX, mouseY, this.scroll);
         default -> { }
      }
   }

   void renderOverlays(GuiGraphicsExtractor graphics, float x, float y) {
      if (this.category == ThirdGuiCategory.THEMES) {
         this.themes.renderOverlays(graphics, this.mouseX(x), this.mouseY(y), this.scroll);
      } else if (this.category == ThirdGuiCategory.AUTOBUY) {
         // Drain the deferred tooltip while the editor's local transform is still active.
         AutoBuyPanel.Вй(this.getPanelBackgroundColor());
      }
   }

   boolean hasDialog() {
      return this.category == ThirdGuiCategory.THEMES && this.themes != null && this.themes.isAnyDialogOpen();
   }

   void mouseClicked(float x, float y, int button) {
      switch (this.category) {
         case THEMES -> this.themes.mouseClicked(this.mouseX(x), this.mouseY(y), button);
         case COSMETICS -> this.cosmetics.щ(this.mouseX(x), this.mouseY(y), button);
         case AUTOBUY -> this.autoBuy.х(this.mouseX(x), this.mouseY(y), button);
         default -> { }
      }
   }

   void mouseReleased(float x, float y, int button) {
      if (this.category == ThirdGuiCategory.THEMES) {
         this.themes.mouseReleased(this.mouseX(x), this.mouseY(y), button);
      }
   }

   void mouseScrolled(float x, float y, double amount) {
      switch (this.category) {
         case THEMES -> this.themes.mouseScrolled(this.mouseX(x), this.mouseY(y), amount);
         case COSMETICS -> {
            if (this.cosmetics.И(this.mouseX(x), this.mouseY(y))) {
               float minScroll = -Math.max(0.0F, this.cosmetics.Ь2() - this.getModuleViewportHeight());
               this.scrollTarget = Math.clamp(this.scrollTarget + (float)amount * 40.0F, minScroll, 0.0F);
            }
         }
         case AUTOBUY -> this.autoBuy.Л(this.mouseX(x), this.mouseY(y), (float)amount);
         default -> { }
      }
   }

   boolean keyPressed(KeyEvent event) {
      if (this.category == ThirdGuiCategory.COSMETICS) return this.cosmetics.Л(event.key(), event.modifiers());
      if (this.category == ThirdGuiCategory.AUTOBUY) return this.autoBuy.к_bbeebd87(event.key());
      if (this.category == ThirdGuiCategory.THEMES) {
         if (this.themes.isPresetPickerOpen()) {
            if (event.key() == GLFW.GLFW_KEY_ESCAPE) this.themes.setPresetPickerOpen(false);
            return true;
         }
         if (this.themes.isNewPresetDialogOpen()) return this.themes.getNewPresetDialog().Ь(event);
      }
      return false;
   }

   boolean charTyped(String text) {
      if (this.category == ThirdGuiCategory.COSMETICS) return this.cosmetics.O(text);
      if (this.category == ThirdGuiCategory.AUTOBUY) return this.autoBuy.Н(text);
      if (this.category == ThirdGuiCategory.THEMES && this.themes.isNewPresetDialogOpen()) {
         return this.themes.getNewPresetDialog().т(text);
      }
      return false;
   }

   void tick() {
      if (this.category == ThirdGuiCategory.THEMES && this.themes != null) this.themes.tick();
   }

   void clearFocus() {
      if (this.themes != null) {
         this.themes.mouseReleased(0.0F, 0.0F, 0);
         this.themes.setNewPresetDialogOpen(false);
         this.themes.setPresetPickerOpen(false);
      }
      if (this.cosmetics != null) this.cosmetics.method_36();
      if (this.autoBuy != null) this.autoBuy.к_bbeebd87(GLFW.GLFW_KEY_ESCAPE);
   }

   // Cancel each embedded editor's built-in insets, so its visible content starts at (0, 0).
   // Cosmetics retains its native bounds, sidebar, preview and scrolling unchanged.
   public float getGuiX() {
      return this.category == ThirdGuiCategory.THEMES ? -18.0F : this.category == ThirdGuiCategory.AUTOBUY ? -10.0F : 0.0F;
   }
   public float getGuiY() {
      return this.category == ThirdGuiCategory.THEMES ? -78.0F : this.category == ThirdGuiCategory.AUTOBUY ? -74.0F : 0.0F;
   }
   public float getGuiWidth() {
      return this.category == ThirdGuiCategory.THEMES ? this.contentWidth + 44.0F
         : this.category == ThirdGuiCategory.AUTOBUY ? this.contentWidth + 20.0F : 800.0F;
   }
   public float getGuiHeight() {
      // Fill the whole content area so Cosmetics matches the height of the other panels.
      return this.category == ThirdGuiCategory.COSMETICS ? this.contentHeight : this.contentHeight - this.getGuiY() + 12.0F;
   }
   public float getSidebarWidth() { return this.category == ThirdGuiCategory.COSMETICS ? 160.0F : 0.0F; }
   public float getModuleViewportHeight() { return this.category == ThirdGuiCategory.COSMETICS ? 426.0F : this.contentHeight; }
   public float getThemeColorPanelBottomInset() { return 12.0F; }
   public float getGuiAlpha() { return this.alpha; }
   public boolean usesCustomBackground() { return true; }
   public int getPanelBackgroundColor() { return ThirdGuiColors.PANEL; }
   public int getPanelCardColor() { return ThirdGuiColors.CARD; }
   public float getGuiScroll() { return this.scroll; }
   public void resetGuiScroll() { this.scroll = 0.0F; this.scrollTarget = 0.0F; }
   public void startThemeTransition(float x, float y, int[] previousColors, int[] nextColors) {
      ThemeWave.INSTANCE.startTransition(x * this.framebufferScale + this.framebufferX,
         y * this.framebufferScale + this.framebufferY, previousColors, nextColors);
   }
}
