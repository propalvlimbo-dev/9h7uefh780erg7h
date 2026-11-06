package com.deluxe.gui;

import com.deluxe.internal.ArgbColor;
import com.deluxe.internal.GuiBlurState;
import com.deluxe.internal.GuiScale;
import com.deluxe.modules.impl.misc.ModuleCategory;
import com.deluxe.util.time.ClickGuiContext;
import com.deluxe.util.time.ClickGuiTheme;
import com.deluxe.util.time.GuiFont;
import com.deluxe.util.time.GuiTextRenderer;
import com.deluxe.util.time.GuiVisuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix4f;
import sg.gui.click.ClickGuiScreen;
import sg.modules.impl.misc.ClientSounds;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ThemeEditor;

/**
 * Отдельное GUI для разделов AutoBuy / Cosmetic / Themes.
 * Не открывает первое GUI: панели рендерятся внутри собственного окна в стиле DELUXE.
 * Все размеры окна задаются в пикселях фреймбуфера, хром рисуется в gui-юнитах (1 юнит = 2 px).
 */
public class DeluxePanelGui extends ClickGuiScreen {
   public enum Panel {
      AUTOBUY("AutoBuy", ModuleCategory.AUTOBUY, 74.0F),
      COSMETICS("Cosmetic", ModuleCategory.COSMETICS, 78.0F),
      THEMES("Themes", ModuleCategory.THEMES, 78.0F);

      private final String label;
      private final ModuleCategory category;
      private final float topOffset;

      Panel(String label, ModuleCategory category, float topOffset) {
         this.label = label;
         this.category = category;
         this.topOffset = topOffset;
      }

      public String label() {
         return this.label;
      }

      public ModuleCategory category() {
         return this.category;
      }

      public float topOffset() {
         return this.topOffset;
      }
   }

   private static final float HEADER_HEIGHT = 78.0F;
   private static final float COSMETICS_SIDEBAR_WIDTH = 204.0F;
   private static final float COSMETICS_PREVIEW_BLOCK = 250.0F;
   private static final float COSMETICS_TOTAL_WIDTH = 1050.0F;
   private static final float VIEWPORT_INSET = 108.0F;
   private static final float TAB_HEIGHT = 30.0F;
   private static final float TAB_GAP = 5.0F;
   private static final float TAB_PADDING = 9.0F;
   private static final float TAB_ICON = 12.8F;
   private static final float TAB_ICON_GAP = 8.0F;
   private static final float TAB_TEXT_SIZE = 6.3F;
   private static final float CLOSE_SIZE = 30.0F;
   private static final float CLOSE_Y_OFFSET = -10.0F;
   private static final float CLOSE_BAR_LENGTH = 8.0F;
   private static final float CLOSE_BAR_THICKNESS = 1.15F;
   private static final int CLOSE_HOVER_COLOR = 0xFFFF4D4D;
   private static final int CLOSE_IDLE_GLYPH = 0xFFB9BEC9;
   private static final float LOGO_SIZE = 18.0F;
   private static final float WINDOW_RADIUS = 8.5F;
   private static final float OPEN_MILLIS = 420.0F;
   private static final float CLOSE_MILLIS = 200.0F;
   private static final float TAB_MILLIS = 280.0F;
   private static DeluxePanelGui INSTANCE;

   private final ClickGuiContext ctx = new ClickGuiContext();
   private final float[] tabLefts = new float[Panel.values().length];
   private final float[] tabWidths = new float[Panel.values().length];
   private Panel active = Panel.AUTOBUY;
   private long openNanos;
   private long closeNanos;
   private long tabNanos;
   private long lastFrameNanos;
   private boolean closing;
   private boolean removalScheduled;
   private boolean indicatorReady;
   private float indicatorX;
   private float indicatorWidth;
   private float visibility;
   private float slideOffset;
   private float tabFade;
   private float tabSlide;
   private float mouseX;
   private float mouseY;
   private float screenW;
   private float screenH;
   private float targetWinW;
   private boolean widthReady;
   private float winX;
   private float winY;
   private float winW;
   private float winH;
   private float tabY;
   private float closeX;
   private float closeY;
   private float scrollTarget;
   private float scrollCurrent;
   private float contentHeight;

   public static DeluxePanelGui instance(Panel panel) {
      if (INSTANCE == null) {
         INSTANCE = new DeluxePanelGui();
      }

      INSTANCE.active = panel;
      INSTANCE.contentHeight = 0.0F;
      INSTANCE.resetGuiScroll();
      INSTANCE.closing = false;
      INSTANCE.removalScheduled = false;
      INSTANCE.indicatorReady = false;
      INSTANCE.visibility = 0.0F;
      INSTANCE.slideOffset = 26.0F;
      INSTANCE.tabFade = 0.0F;
      INSTANCE.tabSlide = 12.0F;
      INSTANCE.lastFrameNanos = 0L;
      INSTANCE.openNanos = System.nanoTime();
      INSTANCE.tabNanos = INSTANCE.openNanos;
      if (panel == Panel.THEMES) {
         INSTANCE.getThemeEditor().restartEntranceTransitions();
      }

      ClientSounds.к(ClientSounds.GuiSound.OPEN);
      return INSTANCE;
   }

   public void selectPanel(Panel panel) {
      if (this.active != panel) {
         this.active = panel;
         this.tabNanos = System.nanoTime();
         this.contentHeight = 0.0F;
         this.resetGuiScroll();
         if (panel == Panel.THEMES) {
            this.getThemeEditor().restartEntranceTransitions();
         }

         ClientSounds.к(ClientSounds.GuiSound.TAB, (float)panel.ordinal() / (float)Math.max(1, Panel.values().length - 1));
      }
   }

   private static float pixelScale() {
      return GuiScale.getWindowScale() / GuiScale.getBaseScale() * 2.0F;
   }

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }

   private static float smoothstep(float value) {
      return value * value * (3.0F - 2.0F * value);
   }

   private void updateLayout(float deltaSeconds) {
      Minecraft client = Minecraft.getInstance();
      this.screenW = client.getWindow().getScreenWidth();
      this.screenH = client.getWindow().getScreenHeight();
      this.targetWinW = this.active == Panel.COSMETICS
         ? Math.min(this.screenW - 60.0F, COSMETICS_TOTAL_WIDTH)
         : Math.min(this.screenW - 220.0F, 980.0F);
      if (!this.widthReady) {
         this.winW = this.targetWinW;
         this.widthReady = true;
      } else if (deltaSeconds > 0.0F) {
         this.winW += (this.targetWinW - this.winW) * Math.min(1.0F, deltaSeconds * 14.0F);
         if (Math.abs(this.targetWinW - this.winW) < 0.5F) {
            this.winW = this.targetWinW;
         }
      }

      this.winH = Math.min(this.screenH - 130.0F, 680.0F);
      this.centerWindow();

      long now = System.nanoTime();
      float openProgress = clamp01((float)((double)(now - this.openNanos) / 1000000.0) / OPEN_MILLIS);
      float closeProgress = this.closing ? clamp01((float)((double)(now - this.closeNanos) / 1000000.0) / CLOSE_MILLIS) : 0.0F;
      float closeEased = smoothstep(closeProgress);
      this.visibility = clamp01(smoothstep(openProgress) * (1.0F - closeEased));
      this.slideOffset = (1.0F - GuiVisuals.easeOutBack(openProgress)) * 26.0F + closeEased * 14.0F;
      float tabProgress = clamp01((float)((double)(now - this.tabNanos) / 1000000.0) / TAB_MILLIS);
      float tabEased = smoothstep(tabProgress);
      this.tabFade = tabEased;
      this.tabSlide = (1.0F - tabEased) * 12.0F;

      Panel[] panels = Panel.values();
      float cursor = this.winX + 20.0F + LOGO_SIZE + 16.0F;

      for (int index = 0; index < panels.length; index++) {
         float labelWidth = GuiTextRenderer.getWidth(GuiFont.INSTANCE, panels[index].label(), TAB_TEXT_SIZE) * 2.0F;
         float width = TAB_PADDING * 2.0F + TAB_ICON + TAB_ICON_GAP + labelWidth;
         this.tabLefts[index] = cursor;
         this.tabWidths[index] = width;
         cursor += width + TAB_GAP;
      }

      this.tabY = this.winY + this.slideOffset + (HEADER_HEIGHT - TAB_HEIGHT) / 2.0F;
      this.closeX = this.winX + this.winW - 16.0F - CLOSE_SIZE;
      this.closeY = this.winY + this.slideOffset + (HEADER_HEIGHT - CLOSE_SIZE) / 2.0F + CLOSE_Y_OFFSET;
   }

   private float panelShift() {
      return HEADER_HEIGHT - this.active.topOffset();
   }

   private void centerWindow() {
      this.winX = (this.screenW - this.winW) / 2.0F;
      this.winY = (this.screenH - this.winH) / 2.0F;
   }

   private float sidebarWidth() {
      return this.active == Panel.COSMETICS ? COSMETICS_SIDEBAR_WIDTH : 0.0F;
   }

   private float mainWidth() {
      return this.active == Panel.COSMETICS ? Math.max(420.0F, this.winW - COSMETICS_PREVIEW_BLOCK) : this.winW;
   }

   @Override
   public float getInterfaceScale() {
      return 1.0F;
   }

   @Override
   public float getGuiX() {
      return this.winX;
   }

   @Override
   public float getGuiY() {
      return this.winY + this.panelShift() + this.slideOffset + this.tabSlide;
   }

   @Override
   public float getGuiWidth() {
      return this.mainWidth();
   }

   @Override
   public float getGuiHeight() {
      return this.winH - this.panelShift();
   }

   @Override
   public float getSidebarWidth() {
      return this.sidebarWidth();
   }

   @Override
   public float getModuleViewportHeight() {
      return this.getGuiHeight() - VIEWPORT_INSET;
   }

   @Override
   public float getLayoutContentAlpha(int layer) {
      return this.visibility * this.tabFade;
   }

   @Override
   public float getLayoutOffset(int layer) {
      return 0.0F;
   }

   @Override
   public float getSearchOverlayAlpha() {
      return 0.0F;
   }

   @Override
   public float getGuiAlpha() {
      return this.visibility;
   }

   @Override
   public float getSidebarLabelAlpha() {
      return this.visibility * this.tabFade;
   }

   @Override
   public float getGuiScroll() {
      return this.scrollCurrent;
   }

   @Override
   public void resetGuiScroll() {
      this.scrollTarget = 0.0F;
      this.scrollCurrent = 0.0F;
   }

   private float viewportHeight() {
      return this.active == Panel.COSMETICS ? this.getCosmeticsPanel().ЬЮ() : this.getModuleViewportHeight();
   }

   private float minScroll() {
      return Math.min(0.0F, this.viewportHeight() - this.contentHeight);
   }

   private void updateScroll(float deltaSeconds) {
      float minimum = this.minScroll();
      this.scrollTarget = Math.max(minimum, Math.min(0.0F, this.scrollTarget));
      float factor = 1.0F - (float)Math.exp(-deltaSeconds * 22.0F);
      this.scrollCurrent += (this.scrollTarget - this.scrollCurrent) * factor;
      this.scrollCurrent = Math.max(minimum, Math.min(0.0F, this.scrollCurrent));
      if (Math.abs(this.scrollTarget - this.scrollCurrent) < 0.05F) {
         this.scrollCurrent = this.scrollTarget;
      }
   }

   private void scrollBy(float amount) {
      this.scrollTarget = Math.max(this.minScroll(), Math.min(0.0F, this.scrollTarget + amount * 42.0F));
   }

   @Override
   public boolean isUnderSearch(float x, float y, float width, float height) {
      return false;
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickDelta) {
      long now = System.nanoTime();
      float deltaSeconds = this.lastFrameNanos == 0L ? 0.016F : Math.min(0.05F, (float)(now - this.lastFrameNanos) / 1.0E9F);
      this.lastFrameNanos = now;
      this.updateLayout(deltaSeconds);
      float scale = pixelScale();
      this.mouseX = (float)mouseX * scale;
      this.mouseY = (float)mouseY * scale;
      this.ctx.beginFrame(1.0F, this.mouseX / 2.0F, this.mouseY / 2.0F);
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      GuiBlurState.setEnabled(true);
      GuiBlurState.setRadius(8.0F);
      GuiBlurState.setOpacity(0.0F);
      Render2D.begin();
      Render2D.drawSharedBlur(0.0F, 0.0F, this.screenW / 2.0F, this.screenH / 2.0F, 0.0F, 18.0F, 1.0F, -1);
      Render2D.pushAlpha(this.visibility);
      Render2D.fillRectangle(0.0F, 0.0F, this.screenW / 2.0F, this.screenH / 2.0F, 90 << 24);
      this.updateScroll(deltaSeconds);
      this.renderWindow();
      this.renderContent(context);
      this.renderHeader(deltaSeconds);
      Render2D.popAlpha();
      Render2D.end();
   }

   private void renderWindow() {
      float x = this.winX / 2.0F;
      float y = (this.winY + this.slideOffset) / 2.0F;
      float width = this.winW / 2.0F;
      float height = this.winH / 2.0F;
      Render2D.drawSharedBlur(x + 0.5F, y + 0.5F, width - 1.0F, height - 1.0F, 8.0F, 30.0F, 1.0F, -1);
      Render2D.fillRoundedRectangle(x, y, width, height, WINDOW_RADIUS, ClickGuiTheme.panelBackground);
      Render2D.fillRoundedGradient(
         x,
         y,
         width,
         28.0F,
         WINDOW_RADIUS,
         WINDOW_RADIUS,
         0.0F,
         0.0F,
         ArgbColor.withAlpha(ClickGuiTheme.accent, 26),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 26),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 0),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 0)
      );
      Render2D.drawOutline(x, y, width, height, WINDOW_RADIUS, 0.5F, ClickGuiTheme.panelOutline);
   }

   private void renderContent(GuiGraphicsExtractor context) {
      float x = this.winX / 2.0F;
      float width = this.winW / 2.0F;
      float contentY = (this.winY + this.slideOffset + HEADER_HEIGHT) / 2.0F;
      float contentHeight = (this.winH - HEADER_HEIGHT) / 2.0F;
      Render2D.pushScissor(x, contentY, width, contentHeight);

      switch (this.active) {
         case AUTOBUY -> {
            this.contentHeight = 0.0F;
            this.getAutoBuyPanel().诶(context, this.mouseX, this.mouseY, this.scrollCurrent);
         }
         case COSMETICS -> {
            sg.gui.cosmetics.CosmeticsPanel panel = this.getCosmeticsPanel();
            panel.衣(context, this.mouseX, this.mouseY, this.scrollCurrent);
            this.contentHeight = panel.Ь2();
         }
         case THEMES -> {
            ThemeEditor editor = this.getThemeEditor();
            float previousAlpha = UiDrawing.getGlobalAlpha();
            UiDrawing.setGlobalAlpha(previousAlpha * Math.max(0.02F, this.visibility * this.tabFade));
            editor.render(context, this.mouseX, this.mouseY, this.scrollCurrent);
            UiDrawing.setGlobalAlpha(previousAlpha);
            this.contentHeight = editor.getHeight();
         }
      }

      Render2D.popScissor();
      if (this.active == Panel.THEMES) {
         Render2D.pushScissor(x, (this.winY + this.slideOffset) / 2.0F, width, this.winH / 2.0F);
         this.getThemeEditor().renderOverlays(context, this.mouseX, this.mouseY, this.scrollCurrent);
         Render2D.popScissor();
      }
   }

   private void renderHeader(float deltaSeconds) {
      float mouseUnitX = this.mouseX / 2.0F;
      float mouseUnitY = this.mouseY / 2.0F;
      float centerY = (this.winY + this.slideOffset + HEADER_HEIGHT / 2.0F) / 2.0F;
      float left = this.winX / 2.0F;
      float right = (this.winX + this.winW) / 2.0F;
       Render2D.drawClientLogo(left + 10.0F, centerY - LOGO_SIZE / 4.0F, LOGO_SIZE / 2.0F, LOGO_SIZE / 2.0F, ClickGuiTheme.accent);
      float tabHeight = TAB_HEIGHT / 2.0F;
      float tabTop = this.tabY / 2.0F;
      int activeIndex = this.active.ordinal();
      float targetX = this.tabLefts[activeIndex] / 2.0F;
      float targetWidth = this.tabWidths[activeIndex] / 2.0F;
      if (!this.indicatorReady) {
         this.indicatorX = targetX;
         this.indicatorWidth = targetWidth;
         this.indicatorReady = true;
      }

      float smoothing = Math.min(1.0F, deltaSeconds * 16.0F);
      this.indicatorX += (targetX - this.indicatorX) * smoothing;
      this.indicatorWidth += (targetWidth - this.indicatorWidth) * smoothing;
      Render2D.fillRoundedRectangle(this.indicatorX, tabTop, this.indicatorWidth, tabHeight, tabHeight / 2.0F, ArgbColor.withAlpha(ClickGuiTheme.accent, 36));
      Render2D.drawOutline(this.indicatorX, tabTop, this.indicatorWidth, tabHeight, tabHeight / 2.0F, 0.5F, ArgbColor.withAlpha(ClickGuiTheme.accent, 110));
      Panel[] panels = Panel.values();

      for (int index = 0; index < panels.length; index++) {
         float x = this.tabLefts[index] / 2.0F;
         float width = this.tabWidths[index] / 2.0F;
         boolean selected = index == activeIndex;
         boolean hovered = GuiVisuals.containsPoint(mouseUnitX, mouseUnitY, x, tabTop, width, tabHeight);
         float hover = this.ctx.animateValue("panelgui.tab." + index, hovered && !selected ? 1.0F : 0.0F);
         if (hover > 0.01F) {
            Render2D.fillRoundedRectangle(x, tabTop, width, tabHeight, tabHeight / 2.0F, ArgbColor.withAlpha(ClickGuiTheme.accent, Math.round(16.0F * hover)));
         }

         float emphasis = Math.max(selected ? 1.0F : 0.0F, hover);
         float iconSize = TAB_ICON / 2.0F;
         Render2D.drawRoundedImage(
            ClickGuiTheme.categoryIcon(panels[index].category()),
            x + TAB_PADDING / 2.0F,
            centerY - iconSize / 2.0F,
            iconSize,
            iconSize,
            2.0F,
            GuiVisuals.lerpColor(ClickGuiTheme.disabledSettingIcon, ClickGuiTheme.accent, emphasis)
         );
         GuiTextRenderer.draw(
            GuiFont.INSTANCE,
            panels[index].label(),
            x + (TAB_PADDING + TAB_ICON + TAB_ICON_GAP) / 2.0F,
            centerY - TAB_TEXT_SIZE / 2.0F,
            TAB_TEXT_SIZE,
            GuiVisuals.lerpColor(ClickGuiTheme.disabledValueText, ClickGuiTheme.tooltipText, emphasis)
         );
      }

      float closeSize = CLOSE_SIZE / 2.0F;
      float closeLeft = this.closeX / 2.0F;
      float closeTop = this.closeY / 2.0F;
      float closeCenterX = closeLeft + closeSize / 2.0F;
      float closeCenterY = closeTop + closeSize / 2.0F;
      boolean closeHovered = GuiVisuals.containsPoint(mouseUnitX, mouseUnitY, closeLeft, closeTop, closeSize, closeSize);
      float closeProgress = this.ctx.animateValue("panelgui.close", closeHovered ? 1.0F : 0.0F);
      if (closeProgress > 0.01F) {
         Render2D.fillRoundedRectangle(closeLeft, closeTop, closeSize, closeSize, closeSize / 2.0F, ArgbColor.withAlpha(CLOSE_HOVER_COLOR, Math.round(34.0F * closeProgress)));
         Render2D.strokeCircle(closeCenterX, closeCenterY, closeSize / 2.0F + 1.7F, 0.45F, ArgbColor.withAlpha(CLOSE_HOVER_COLOR, Math.round(48.0F * closeProgress)));
      }

      Render2D.drawOutline(
         closeLeft,
         closeTop,
         closeSize,
         closeSize,
         closeSize / 2.0F,
         0.5F + 0.25F * closeProgress,
         GuiVisuals.lerpColor(ClickGuiTheme.panelOutline, ArgbColor.withAlpha(CLOSE_HOVER_COLOR, 215), closeProgress)
      );
      float barLength = CLOSE_BAR_LENGTH + 0.7F * closeProgress;
      float barThickness = CLOSE_BAR_THICKNESS + 0.1F * closeProgress;
      int glyphColor = GuiVisuals.lerpColor(CLOSE_IDLE_GLYPH, CLOSE_HOVER_COLOR, closeProgress);
      Render2D.drawRotatedRectangle(
         closeCenterX - barLength / 2.0F, closeCenterY - barThickness / 2.0F, barLength, barThickness, barThickness / 2.0F, glyphColor, 45.0F, closeCenterX, closeCenterY
      );
      Render2D.drawRotatedRectangle(
         closeCenterX - barLength / 2.0F, closeCenterY - barThickness / 2.0F, barLength, barThickness, barThickness / 2.0F, glyphColor, -45.0F, closeCenterX, closeCenterY
      );
      float lineY = (this.winY + this.slideOffset + HEADER_HEIGHT) / 2.0F - 0.25F;
      Render2D.fillRoundedGradient(
         left + 10.0F,
         lineY,
         right - left - 20.0F,
         0.5F,
         0.25F,
         ArgbColor.withAlpha(ClickGuiTheme.accent, 0),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 70),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 70),
         ArgbColor.withAlpha(ClickGuiTheme.accent, 0)
      );
   }

   @Override
   public void tick() {
      if (this.closing && this.visibility <= 0.004F && !this.removalScheduled) {
         this.removalScheduled = true;
         Minecraft.getInstance()
            .execute(
               () -> {
                  if (Minecraft.getInstance().gui.screen() == this) {
                      Minecraft.getInstance().gui.setScreen(sg.client.RoseClient.getInstance().getSelfcodeService().returnScreen());
                  }
               }
            );
      } else if (this.active == Panel.THEMES) {
         this.getThemeEditor().tick();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
      if (this.closing) {
         return true;
      }

      this.updateLayout(0.0F);
      float scale = pixelScale();
      float x = (float)click.x() * scale;
      float y = (float)click.y() * scale;
      Panel[] panels = Panel.values();

      for (int index = 0; index < panels.length; index++) {
         if (GuiVisuals.containsPoint(x, y, this.tabLefts[index], this.tabY, this.tabWidths[index], TAB_HEIGHT)) {
            this.selectPanel(panels[index]);
            return true;
         }
      }

      if (GuiVisuals.containsPoint(x, y, this.closeX, this.closeY, CLOSE_SIZE, CLOSE_SIZE)) {
         this.startClose();
         return true;
      }

      switch (this.active) {
         case AUTOBUY -> this.getAutoBuyPanel().х(x, y, click.button());
         case COSMETICS -> this.getCosmeticsPanel().щ(x, y, click.button());
         case THEMES -> this.getThemeEditor().mouseClicked(x, y, click.button());
      }

      return true;
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
      if (this.closing) {
         return true;
      }

      this.updateLayout(0.0F);
      float scale = pixelScale();
      float x = (float)mouseX * scale;
      float y = (float)mouseY * scale;
      boolean handled = switch (this.active) {
         case AUTOBUY -> this.getAutoBuyPanel().Л(x, y, (float)verticalScroll);
         case THEMES -> this.getThemeEditor().mouseScrolled(x, y, verticalScroll);
         case COSMETICS -> !this.getCosmeticsPanel().И(x, y);
      };
      if (!handled) {
         this.scrollBy((float)verticalScroll);
      }

      return true;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent click, double dragX, double dragY) {
      return true;
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent click) {
      this.updateLayout(0.0F);
      float scale = pixelScale();
      if (this.active == Panel.THEMES) {
         this.getThemeEditor().mouseReleased((float)click.x() * scale, (float)click.y() * scale, click.button());
      }

      return true;
   }

   @Override
   public boolean keyPressed(KeyEvent key) {
      int keyCode = key.key();
      if (keyCode == 256 || keyCode == 344) {
         this.startClose();
         return true;
      }

      if (this.closing) {
         return true;
      }

      if (this.active == Panel.THEMES) {
         ThemeEditor themes = this.getThemeEditor();
         if (themes.isNewPresetDialogOpen()) {
            return themes.getNewPresetDialog().Ь(key);
         }

         if (themes.isPresetPickerOpen()) {
            themes.setPresetPickerOpen(false);
            return true;
         }
      }

      switch (this.active) {
         case AUTOBUY -> this.getAutoBuyPanel().к_bbeebd87(keyCode);
         case COSMETICS -> this.getCosmeticsPanel().Л(keyCode, key.modifiers());
         case THEMES -> {
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent charInput) {
      if (this.closing) {
         return true;
      }

      String typed = charInput.codepointAsString();
      if (this.active == Panel.THEMES && this.getThemeEditor().isNewPresetDialogOpen()) {
         return this.getThemeEditor().getNewPresetDialog().т(typed);
      }

      switch (this.active) {
         case AUTOBUY -> this.getAutoBuyPanel().Н(typed);
         case COSMETICS -> this.getCosmeticsPanel().O(typed);
         case THEMES -> {
         }
      }

      return true;
   }

   private void startClose() {
      if (!this.closing) {
         this.closing = true;
         this.closeNanos = System.nanoTime();
         this.getCosmeticsPanel().method_36();
         ClientSounds.к(ClientSounds.GuiSound.CLOSE);
      }
   }

   @Override
   public void onClose() {
      this.startClose();
   }

   @Override
   public void removed() {
      GuiBlurState.setEnabled(false);
      this.closing = false;
      this.removalScheduled = false;
      this.indicatorReady = false;
      this.widthReady = false;
      this.contentHeight = 0.0F;
      this.resetGuiScroll();
      this.visibility = 0.0F;
      this.tabFade = 0.0F;
      this.openNanos = 0L;
      this.tabNanos = 0L;
      this.lastFrameNanos = 0L;
      this.ctx.focusedText = null;
      this.ctx.inputHandler = null;
      this.ctx.popupOwner = null;
      super.removed();
   }
}
