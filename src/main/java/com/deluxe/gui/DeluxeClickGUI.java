package com.deluxe.gui;

import com.deluxe.internal.ArgbColor;
import com.deluxe.internal.GuiBlurState;
import com.deluxe.internal.GuiScale;
import com.deluxe.modules.impl.misc.ModuleCategory;
import com.deluxe.modules.impl.misc.ModuleCategoryRenderer;
import com.deluxe.modules.impl.player.InputBinding;
import com.deluxe.util.time.ClickGuiContext;
import com.deluxe.util.time.ClickGuiTheme;
import com.deluxe.util.time.GuiFont;
import com.deluxe.util.time.GuiTextRenderer;
import com.deluxe.util.time.GuiVisuals;
import com.deluxe.util.time.TextSetting;
import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import sg.client.RoseClient;
import sg.events.EventListener;
import sg.events.impl.HudRenderEvent;

public class DeluxeClickGUI extends Screen implements ClosingOverlayRegistry.ClosingOverlay {

   private final ClickGuiContext ctx = new ClickGuiContext();
   private final ModuleCategory[] columnCategories;
   private final ModuleCategory[] panelCategories;
   private final SmoothScrollState[] categoryScrolls;
   private final float[][] categoryBounds;
   private float visibilityProgress;
   private float transitionStartProgress;
   private long transitionStartTime;
   private boolean previousVisible;
   private boolean closing;
   private boolean screenRemovalScheduled;
   private boolean closingOverlayActive;
   private long lastFrameTime;
   private long openedAtMillis;
   private boolean searchFocused;
   private final TextSetting search;
   private long lastSearchEditTime;
   private final EventListener<HudRenderEvent> hudRenderListener = new EventListener<HudRenderEvent>() {
      @Override
      public void onEvent(HudRenderEvent event) {
         ClosingOverlayRegistry.render();
      }
   };
   private static DeluxeClickGUI INSTANCE;

   @Override
   public void onClose() {
      this.close();
   }

   public void close() {
      if (!this.closing) {
         this.closing = true;
         this.ctx.focusedText = null;
         this.ctx.inputHandler = null;
         this.ctx.bindingSetting = null;
         this.ctx.popupOwner = null;
         this.ctx.bindingModule = null;
         this.closingOverlayActive = true;
         ClosingOverlayRegistry.add(this);
         if (Minecraft.getInstance().gui.screen() == this) {
             Minecraft.getInstance().gui.setScreen(RoseClient.getInstance().getSelfcodeService().returnScreen());
         }
      }
   }

   private void drawSearchBar(float screenWidth, float screenHeight, float columnBottom) {
      float scale = this.ctx.scale;
      float barWidth = (74.8F) * scale;
      float barHeight = (19.31F) * scale;
      float barX = (screenWidth - barWidth) / 2.0F;
      float barY = Math.min(columnBottom + (30.8F) * scale, screenHeight - barHeight - (5.0F) * scale);
      Render2D.drawSharedBlur(
         barX + (0.5F) * scale,
         barY + (0.5F) * scale,
         barWidth - 1.0F * scale,
         barHeight - 1.0F * scale,
         barHeight / (2.26F) - (0.5F) * scale,
         (30.0F),
         1.0F,
         -1
      );
      Render2D.fillRoundedRectangle(barX, barY, barWidth, barHeight, barHeight / (2.26F), ClickGuiTheme.panelBackground);
      Render2D.drawOutline(barX, barY, barWidth, barHeight, barHeight / (2.26F), (0.5F), ClickGuiTheme.panelOutline);
      Render2D.fillRoundedRectangle(barX, barY, barHeight, barHeight, barHeight / (2.26F), 0.0F, 0.0F, barHeight / (2.26F), ClickGuiTheme.searchIconBackground);
       Render2D.drawClientLogo(
         barX + barHeight / 2.0F - (3.2F) * scale,
         barY + barHeight / 2.0F - (3.2F) * scale,
         (6.4F) * scale,
         (6.4F) * scale,
          ClickGuiTheme.accent
      );
      Render2D.drawRoundedImage(
         ("clickgui/search"),
         barX + barWidth - (10.4F) * scale,
         barY + barHeight / 2.0F - (2.65F) * scale,
         (5.3F) * scale,
         (5.3F) * scale,
         0.0F,
         ClickGuiTheme.headerText
      );
      String searchText = this.search.getText();
      String displayText = searchText.isEmpty() ? (this.searchFocused ? "" : ("Search...")) : searchText;
      float textX = barX + (25.4F) * scale;
      float textY = barY + (barHeight - (6.0F) * scale) / 2.0F;
      float textWidth = barWidth - (25.4F) * scale - (12.0F) * scale;
      Render2D.pushScissor(textX - 1.0F * scale, barY, textWidth + 2.0F * scale, barHeight);
      float displayWidth = GuiTextRenderer.getWidth(GuiFont.INSTANCE, displayText, (6.0F) * scale);
      float scrollOffset = Math.max(0.0F, displayWidth - textWidth);
      if (this.search.hasSelection() && !searchText.isEmpty()) {
         Render2D.fillRoundedRectangle(
            textX - scrollOffset + GuiTextRenderer.getWidth(GuiFont.INSTANCE, searchText.substring(0, this.search.getSelectionStart()), (6.0F) * scale),
            textY - 1.0F * scale,
            GuiTextRenderer.getWidth(GuiFont.INSTANCE, searchText.substring(this.search.getSelectionStart(), this.search.getSelectionEnd()), (6.0F) * scale),
            (8.0F) * scale,
            1.0F * scale,
            ArgbColor.withAlpha(-1, 56)
         );
      }

      if (!displayText.isEmpty()) {
         GuiTextRenderer.draw(
            GuiFont.INSTANCE,
            displayText,
            textX - scrollOffset,
            textY,
            (6.0F) * scale,
            searchText.isEmpty() ? ClickGuiTheme.mutedText : ArgbColor.withAlpha(-1, 210)
         );
      }

      if (this.searchFocused && System.nanoTime() / (1000000L) / (530L) % (2L) == 0L) {
         float cursorX = textX - scrollOffset + GuiTextRenderer.getWidth(GuiFont.INSTANCE, searchText.substring(0, this.search.getCursor()), (6.0F) * scale);
         Render2D.fillRoundedRectangle(
            cursorX + (0.4F) * scale,
            textY - (0.5F) * scale,
            (0.6F) * scale,
            (7.0F) * scale,
            (0.3F) * scale,
            ArgbColor.withAlpha(-1, 200)
         );
      }

      Render2D.popScissor();
      this.ctx.registerClick(barX, barY, barWidth, barHeight, null, () -> {
         this.searchFocused = true;
         this.search.moveToEnd(false);
         this.lastSearchEditTime = System.nanoTime() / (1000000L);
      });
   }

   public static DeluxeClickGUI instance() {
      if (INSTANCE == null) {
         INSTANCE = new DeluxeClickGUI();
      }

      return INSTANCE;
   }

   @Override
   protected void extractBlurredBackground(GuiGraphicsExtractor context) {
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor context, float tickDelta) {
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, int width, int height) {
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickDelta) {
      this.renderGui(mouseX, mouseY);
   }

   private void renderGui(int mouseX, int mouseY) {
      float mouseScale = coordinateScale();
      float scaledMouseX = mouseX * mouseScale;
      float scaledMouseY = mouseY * mouseScale;
      this.ctx.beginFrame(
         (1.13F) * Math.min(1.0F, Math.min(this.width * mouseScale / (640.0F), this.height * mouseScale / (460.0F))),
         scaledMouseX,
         scaledMouseY
      );
      float deltaSeconds = Math.min((0.05F), (float)(System.nanoTime() / (1000000L) - this.lastFrameTime) / (1000.0F));
      this.lastFrameTime = System.nanoTime() / (1000000L);
      Render2D.begin();
      boolean previousVisible = !this.closing;
      if (previousVisible != this.previousVisible) {
         this.transitionStartProgress = this.visibilityProgress;
         this.transitionStartTime = System.nanoTime() / (1000000L);
         this.previousVisible = previousVisible;
      }

      float transitionProgress = Math.min(1.0F, (float)(System.nanoTime() / (1000000L) - this.transitionStartTime) / (300.0F));
      float easedTransition = transitionProgress * transitionProgress * transitionProgress * (transitionProgress * (transitionProgress * (6.0F) - (15.0F)) + (10.0F));
      this.visibilityProgress = this.transitionStartProgress + ((previousVisible ? 1.0F : 0.0F) - this.transitionStartProgress) * easedTransition;
      if (this.closing && this.visibilityProgress <= (0.003F)) {
         GuiBlurState.setEnabled(false);
         Render2D.end();
         if (!this.screenRemovalScheduled) {
            this.screenRemovalScheduled = true;
            Minecraft.getInstance()
               .execute(
                  () -> {
                     if (Minecraft.getInstance().gui.screen() == this) {
                         Minecraft.getInstance().gui.setScreen(RoseClient.getInstance().getSelfcodeService().returnScreen());
                     }
                  }
               );
         }
      } else {
         float screenWidth = this.width * coordinateScale();
         float screenHeight = this.height * coordinateScale();
         GuiBlurState.setEnabled(true);
         GuiBlurState.setRadius((8.0F));
         GuiBlurState.setOpacity(0.0F);
         Render2D.drawSharedBlur(0.0F, 0.0F, screenWidth, screenHeight, 0.0F, (16.0F), 1.0F, -1);
         Render2D.pushAlpha(this.visibilityProgress);
         float columnHeight = Math.min((266.5F) * this.ctx.scale, screenHeight - (60.0F) * this.ctx.scale);
         float columnY = (screenHeight - columnHeight) / 2.0F;

         float columnWidth = (112.5F) * this.ctx.scale;
         float columnGap = (9.0F) * this.ctx.scale;
         float totalColumnsWidth = this.columnCategories.length * columnWidth + (this.columnCategories.length - 1) * columnGap;
         float columnsLeft = (screenWidth - (totalColumnsWidth + columnGap + columnWidth)) / 2.0F;

         for (int categoryIndex = 0; categoryIndex < this.columnCategories.length; categoryIndex++) {
            this.categoryBounds[categoryIndex][0] = columnsLeft + categoryIndex * (columnWidth + columnGap);
            this.categoryBounds[categoryIndex][1] = columnY;
            this.categoryBounds[categoryIndex][2] = columnWidth;
            this.categoryBounds[categoryIndex][3] = columnHeight;
            ModuleCategoryRenderer.render(
               this.ctx,
               this.columnCategories[categoryIndex],
               this.categoryScrolls[categoryIndex],
               this.categoryBounds[categoryIndex][0],
               this.categoryBounds[categoryIndex][1],
               this.categoryBounds[categoryIndex][2],
               this.categoryBounds[categoryIndex][3],
               deltaSeconds
            );
         }

         float cardHeight = (26.0F) * this.ctx.scale;
         float cardGap = (6.0F) * this.ctx.scale;
         float stackHeight = this.panelCategories.length * cardHeight + (this.panelCategories.length - 1) * cardGap;
         float stackX = columnsLeft + totalColumnsWidth + columnGap;
         float stackY = columnY + (columnHeight - stackHeight) / 2.0F;

         for (int panelIndex = 0; panelIndex < this.panelCategories.length; panelIndex++) {
            ModuleCategoryRenderer.renderPanelCard(
               this.ctx,
               this.panelCategories[panelIndex],
               stackX,
               stackY + panelIndex * (cardHeight + cardGap),
               columnWidth,
               cardHeight
            );
         }

         this.drawTip(screenWidth, columnY);
         this.drawSearchBar(screenWidth, screenHeight, columnY + columnHeight);
         this.ctx.renderOverlays();
         Render2D.popAlpha();
         this.updateDrag(mouseX * coordinateScale(), mouseY * coordinateScale());
         Render2D.end();
      }
   }

   private void finishOverlay() {
      GuiBlurState.setEnabled(false);
      this.closingOverlayActive = false;
      this.closing = false;
      this.screenRemovalScheduled = false;
      this.visibilityProgress = 0.0F;
      this.transitionStartProgress = 0.0F;
      this.previousVisible = false;
      this.transitionStartTime = 0L;
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private int searchWordHop(int direction) {
      String text = this.search.getText();
      int cursor = this.search.getCursor();
      if (direction < 0) {
         while (cursor > 0 && text.charAt(cursor - 1) == ' ') {
            cursor--;
         }

         while (cursor > 0 && text.charAt(cursor - 1) != ' ') {
            cursor--;
         }

         return cursor - this.search.getCursor();
      } else {
         while (cursor < text.length() && text.charAt(cursor) != ' ') {
            cursor++;
         }

         while (cursor < text.length() && text.charAt(cursor) == ' ') {
            cursor++;
         }

         return cursor - this.search.getCursor();
      }
   }

   @Override
   public void removed() {
      GuiBlurState.setEnabled(false);
      if (!this.closingOverlayActive) {
         this.closing = false;
         this.screenRemovalScheduled = false;
         this.visibilityProgress = 0.0F;
         this.transitionStartProgress = 0.0F;
         this.previousVisible = false;
         this.transitionStartTime = 0L;
      }

      this.ctx.focusedText = null;
      this.ctx.inputHandler = null;
      this.ctx.bindingSetting = null;
      this.ctx.popupOwner = null;
      this.ctx.bindingModule = null;
      this.searchFocused = false;
      this.search.setValue("");
      this.ctx.searchText = "";
      super.removed();
   }

   @Override
   public boolean charTyped(CharacterEvent charInput) {
      if (this.ctx.inputHandler != null) {
         if (charInput.codepoint() >= 32) {
            this.ctx.inputHandler.charTyped((char)charInput.codepoint());
         }

         return true;
      } else if (this.ctx.focusedText != null) {
         if (charInput.codepoint() >= 32) {
            this.ctx.focusedText.insertText(String.valueOf((char)charInput.codepoint()));
         }

         return true;
      } else if (this.searchFocused) {
         if (charInput.codepoint() >= 32) {
            this.search.insertText(String.valueOf((char)charInput.codepoint()));
            this.ctx.searchText = this.search.getText().toLowerCase();
            this.lastSearchEditTime = System.nanoTime() / (1000000L);
         }

         return true;
      } else {
         return super.charTyped(charInput);
      }
   }

   private static float coordinateScale() {
      return GuiScale.getWindowScale() / GuiScale.getBaseScale();
   }

   private void updateDrag(float mouseX, float mouseY) {
      if (this.ctx.draggedSlider != null || this.ctx.draggedColor != null) {
         float pressedProgress = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().handle(), 0) == 1 ? 1.0F : 0.0F;
         if (this.ctx.draggedSlider != null) {
            if (pressedProgress < (0.5F)) {
               this.ctx.draggedSlider = null;
            } else if (this.ctx.sliderBounds.get(this.ctx.draggedSlider) != null) {
               this.ctx.draggedSlider
                  .setNormalizedValue(
                     ClickGuiContext.clamp((mouseX - this.ctx.sliderBounds.get(this.ctx.draggedSlider)[0]) / this.ctx.sliderBounds.get(this.ctx.draggedSlider)[1], 0.0F, 1.0F)
                  );
            }
         }

         if (this.ctx.draggedColor != null) {
            if (pressedProgress < (0.5F)) {
               this.ctx.draggedColor = null;
            } else if (this.ctx.colorPickerBounds.get(this.ctx.draggedColor) != null) {
               float[] hsba = this.ctx.hsbaState(this.ctx.draggedColor);
               float[] pickerBounds = this.ctx.colorPickerBounds.get(this.ctx.draggedColor);
               if (this.ctx.colorDragMode == 0) {
                  hsba[1] = ClickGuiContext.clamp((mouseX - pickerBounds[0]) / pickerBounds[2], 0.0F, 1.0F);
                  hsba[2] = ClickGuiContext.clamp(1.0F - (mouseY - pickerBounds[1]) / pickerBounds[3], 0.0F, 1.0F);
               } else {
                  hsba[0] = ClickGuiContext.clamp((mouseX - pickerBounds[4]) / pickerBounds[6], 0.0F, 1.0F);
               }

               this.ctx.applyHsba(this.ctx.draggedColor, hsba);
            }
         }
      }
   }

   @Override
   public boolean keyPressed(KeyEvent keyInput) {
      if (this.closing) {
         return true;
      }

      int keyCode = keyInput.key();
      if ((keyCode == 344 || keyCode == 256) && System.currentTimeMillis() - this.openedAtMillis < 150L) {
         return true;
      }

      if (this.ctx.bindingModule != null) {
         this.ctx.bindingModule.withKeyCode(keyCode != 256 && keyCode != 261 && keyCode != 344 ? keyCode : -1);
         this.ctx.bindingModule = null;
         return true;
      }

      if (this.ctx.bindingSetting != null) {
         this.ctx.bindingSetting.withKeyCode(keyCode != 256 && keyCode != 261 ? keyCode : -1);
         this.ctx.bindingSetting = null;
         return true;
      }

      if (this.ctx.inputHandler != null) {
         if (keyCode == 256) {
            this.ctx.inputHandler = null;
         } else {
            this.ctx.inputHandler.keyPressed(keyCode);
         }

         return true;
      } else if (this.ctx.focusedText != null) {
         if ((keyInput.modifiers() & 2) != 0 && keyCode == 65) {
            this.ctx.focusedText.selectAll();
         } else if ((keyInput.modifiers() & 2) != 0 && keyCode == 67 && this.ctx.focusedText.hasSelection()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.ctx.focusedText.getSelectedText());
         } else if ((keyInput.modifiers() & 2) != 0 && keyCode == 88 && this.ctx.focusedText.hasSelection()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.ctx.focusedText.getSelectedText());
            this.ctx.focusedText.backspace();
         } else if ((keyInput.modifiers() & 2) != 0 && keyCode == 86) {
            this.ctx.focusedText.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
         } else if (keyCode == 259) {
            this.ctx.focusedText.backspace();
         } else if (keyCode == 261) {
            this.ctx.focusedText.deleteForward();
         } else if (keyCode == 263) {
            this.ctx.focusedText.moveCursor(-1, (keyInput.modifiers() & 1) != 0);
         } else if (keyCode == 262) {
            this.ctx.focusedText.moveCursor(1, (keyInput.modifiers() & 1) != 0);
         } else if (keyCode == 268) {
            this.ctx.focusedText.moveToStart((keyInput.modifiers() & 1) != 0);
         } else if (keyCode == 269) {
            this.ctx.focusedText.moveToEnd((keyInput.modifiers() & 1) != 0);
         } else if (keyCode == 257 || keyCode == 256) {
            this.ctx.focusedText = null;
         }

         return true;
      } else {
         if (this.ctx.popupOwner != null && keyCode == 256) {
            this.ctx.popupOwner = null;
            return true;
         }

         if ((keyInput.modifiers() & 2) != 0 && keyCode == 70) {
            this.searchFocused = !this.searchFocused;
            if (!this.searchFocused) {
               this.search.setValue("");
               this.ctx.searchText = "";
            }

            this.lastSearchEditTime = System.nanoTime() / (1000000L);
            return true;
         } else if (!this.searchFocused) {
            if (keyCode != 344 && keyCode != 256) {
               return super.keyPressed(keyInput);
            }

            this.close();
            return true;
         } else if (keyCode != 256 && keyCode != 257) {
            boolean controlPressed = (keyInput.modifiers() & 2) != 0;
            boolean shiftPressed = (keyInput.modifiers() & 1) != 0;
            String previousSearchText = this.search.getText();
            if (controlPressed && keyCode == 65) {
               this.search.selectAll();
            } else if (controlPressed && keyCode == 67 && this.search.hasSelection()) {
               Minecraft.getInstance().keyboardHandler.setClipboard(this.search.getSelectedText());
            } else if (controlPressed && keyCode == 88 && this.search.hasSelection()) {
               Minecraft.getInstance().keyboardHandler.setClipboard(this.search.getSelectedText());
               this.search.backspace();
            } else if (controlPressed && keyCode == 86) {
               this.search.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            } else if (keyCode == 259) {
               if (controlPressed && !this.search.hasSelection()) {
                  this.search.moveCursor(this.searchWordHop(-1), true);
               }

               this.search.backspace();
            } else if (keyCode == 261) {
               if (controlPressed && !this.search.hasSelection()) {
                  this.search.moveCursor(this.searchWordHop(1), true);
               }

               this.search.deleteForward();
            } else if (keyCode == 263) {
               this.search.moveCursor(controlPressed ? this.searchWordHop(-1) : -1, shiftPressed);
            } else if (keyCode == 262) {
               this.search.moveCursor(controlPressed ? this.searchWordHop(1) : 1, shiftPressed);
            } else if (keyCode == 268) {
               this.search.moveToStart(shiftPressed);
            } else if (keyCode == 269) {
               this.search.moveToEnd(shiftPressed);
            }

            if (!previousSearchText.equals(this.search.getText())) {
               this.ctx.searchText = this.search.getText().toLowerCase();
               this.lastSearchEditTime = System.nanoTime() / (1000000L);
            }

            return true;
         } else {
            this.searchFocused = false;
            this.search.setValue("");
            this.ctx.searchText = "";
            return true;
         }
      }
   }

   private void drawTip(float screenWidth, float columnY) {
      if (this.ctx.hoveredTooltip != null) {
         this.ctx.lastTooltip = this.ctx.hoveredTooltip;
      }

      float visibleProgress = this.ctx.animateValue(("tip.show"), this.ctx.hoveredTooltip != null ? 1.0F : 0.0F);
      if (this.ctx.hoveredTooltip != null) {
         visibleProgress = 1.0F;
      }

      if (!(visibleProgress < (0.01F)) && !this.ctx.lastTooltip.isEmpty()) {
         float scale = this.ctx.scale;
         List<String> lines = GuiVisuals.wrapWords(GuiFont.INSTANCE, this.ctx.lastTooltip, (6.0F) * scale, (240.0F) * scale);
         float textHeight = lines.size() * (6.95F) * scale;
         float lineY = Math.max((5.0F) * scale, columnY - (29.6F) * scale + ((13.9F) * scale - textHeight) / 2.0F);
         Render2D.pushAlpha(visibleProgress);

         for (String line : lines) {
            GuiTextRenderer.draw(
               GuiFont.INSTANCE,
               line,
               (screenWidth - GuiTextRenderer.getWidth(GuiFont.INSTANCE, line, (6.0F) * scale)) / 2.0F,
               lineY,
               (6.0F) * scale,
               ClickGuiTheme.tooltipText
            );
            lineY += (6.95F) * scale;
         }

         Render2D.popAlpha();
      }
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
      if (this.closing) {
         return true;
      }

      float mouseScale = coordinateScale();
      float scaledMouseX = (float)mouseX * mouseScale;
      float scaledMouseY = (float)mouseY * mouseScale;
      if (this.ctx.scrollBounds != null
         && GuiVisuals.containsPoint(scaledMouseX, scaledMouseY, this.ctx.scrollBounds[0], this.ctx.scrollBounds[1], this.ctx.scrollBounds[2], this.ctx.scrollBounds[3])) {
         this.ctx
            .popupScrollOffsets
            .put(
               this.ctx.scrollOwner,
               ClickGuiContext.clamp(
                  this.ctx.popupScrollOffsets.getOrDefault(this.ctx.scrollOwner, 0.0F) - (float)verticalScroll * (17.86F) * this.ctx.scale, 0.0F, this.ctx.maxPopupScroll
               )
            );
         return true;
      }

      if (this.ctx.modalClickStart > 0) {
         return true;
      }

      for (int categoryIndex = 0; categoryIndex < this.columnCategories.length; categoryIndex++) {
         if (this.categoryBounds[categoryIndex] != null
            && GuiVisuals.containsPoint(
               scaledMouseX, scaledMouseY, this.categoryBounds[categoryIndex][0], this.categoryBounds[categoryIndex][1], this.categoryBounds[categoryIndex][2], this.categoryBounds[categoryIndex][3]
            )) {
            this.categoryScrolls[categoryIndex].target = ClickGuiContext.clamp(
               this.categoryScrolls[categoryIndex].target - (float)verticalScroll * (24.0F) * this.ctx.scale,
               0.0F,
               this.categoryScrolls[categoryIndex].maxScroll
            );
            return true;
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalScroll, verticalScroll);
   }

   private DeluxeClickGUI() {
       super(Component.nullToEmpty("Rose ClickGUI"));
      this.lastFrameTime = System.nanoTime() / (1000000L);
      this.search = new TextSetting(("Поиск"), "").withLengthLimits(0, 28);
      List<ModuleCategory> columnList = new ArrayList<>();
      List<ModuleCategory> panelList = new ArrayList<>();

      for (ModuleCategory category : ModuleCategory.values()) {
         if (category.isPanelCategory()) {
            panelList.add(category);
         } else {
            columnList.add(category);
         }
      }

      this.columnCategories = columnList.toArray(new ModuleCategory[0]);
      this.panelCategories = panelList.toArray(new ModuleCategory[0]);
      this.categoryScrolls = new SmoothScrollState[this.columnCategories.length];
      this.categoryBounds = new float[this.columnCategories.length][4];

      for (int categoryIndex = 0; categoryIndex < this.columnCategories.length; categoryIndex++) {
         this.categoryScrolls[categoryIndex] = new SmoothScrollState();
      }

      RoseClient.getInstance().getEventBus().register(this);
   }

   public ClickGuiContext getCtx() {
      return this.ctx;
   }

   @Override
   protected void init() {
      super.init();
      this.openedAtMillis = System.currentTimeMillis();
      if (this.closingOverlayActive) {
         ClosingOverlayRegistry.remove(this);
         this.closingOverlayActive = false;
         this.closing = false;
         this.screenRemovalScheduled = false;
      }
   }

   @Override
   public boolean renderClosing() {
      Minecraft client = Minecraft.getInstance();
      if (this.closingOverlayActive && client.player != null) {
         Window window = client.getWindow();
         float guiMouseX = (float)client.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
         float guiMouseY = (float)client.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
         this.renderGui((int)guiMouseX, (int)guiMouseY);
         if (this.visibilityProgress <= (0.003F)) {
            this.finishOverlay();
            return false;
         } else {
            return true;
         }
      } else {
         this.finishOverlay();
         return false;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
      if (this.closing) {
         return true;
      }

      if (this.ctx.bindingSetting != null) {
         if (click.button() != 0) {
            this.ctx.bindingSetting.setBinding(InputBinding.mouse(click.button()));
         }

         this.ctx.bindingSetting = null;
         return true;
      } else {
         if (click.button() == 0 || click.button() == 1 || click.button() == 2) {
            float mouseScale = coordinateScale();
            float scaledMouseX = (float)click.x() * mouseScale;
            float scaledMouseY = (float)click.y() * mouseScale;
            if (click.button() == 0) {
               this.ctx.focusedText = null;
               this.ctx.inputHandler = null;
               this.ctx.bindingModule = null;
            }

            for (int regionIndex = this.ctx.clickRegions.size() - 1; regionIndex >= this.ctx.modalClickStart; regionIndex--) {
               ClickGuiContext.ClickRegion region = this.ctx.clickRegions.get(regionIndex);
               if (GuiVisuals.containsPoint(scaledMouseX, scaledMouseY, region.x(), region.y(), region.getWidth(), region.getHeight())) {
                  if (click.button() == 0) {
                     region.action().run();
                     return true;
                  }

                  if (click.button() == 1 && region.rightAction() != null) {
                     region.rightAction().run();
                     return true;
                  }

                  if (click.button() == 2 && region.middleAction() != null) {
                     region.middleAction().run();
                     return true;
                  }
               }
            }

            if (this.ctx.modalClickStart > 0) {
               return true;
            }
         }

         return super.mouseClicked(click, doubled);
      }
   }
}
