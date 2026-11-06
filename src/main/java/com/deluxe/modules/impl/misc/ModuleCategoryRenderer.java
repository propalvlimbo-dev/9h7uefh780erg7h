package com.deluxe.modules.impl.misc;

import com.deluxe.gui.DeluxePanelGui;
import com.deluxe.gui.ModuleRowRenderer;
import com.deluxe.gui.Render2D;
import com.deluxe.gui.SmoothScrollState;
import com.deluxe.internal.ArgbColor;
import com.deluxe.internal.RoundedClipStack;
import com.deluxe.util.time.ClickGuiContext;
import com.deluxe.util.time.ClickGuiTheme;
import com.deluxe.util.time.GuiFont;
import com.deluxe.util.time.GuiTextRenderer;
import com.deluxe.util.time.GuiVisuals;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import sg.client.RoseClient;
import sg.gui.click.ClickGuiScreen;

public final class ModuleCategoryRenderer {
   private static final Map<Module, ModuleCategoryRenderer.SearchEntry> searchEntries = new IdentityHashMap<>();

   public static void render(ClickGuiContext context, ModuleCategory category, SmoothScrollState scroll, float x, float y, float width, float height, float deltaSeconds) {
      float scale = context.scale;
      float viewportY = y + (21.0F) * scale;
      Render2D.drawSharedBlur(x + (0.5F) * scale, y + (0.5F) * scale, width - 1.0F * scale, height - 1.0F * scale, (8.0F) * scale, (30.0F), 1.0F, -1);
      Render2D.fillRoundedRectangle(x, y, width, height, (8.5F) * scale, ClickGuiTheme.panelBackground);
      Render2D.drawOutline(x, y, width, height, (8.5F) * scale, (0.5F), ClickGuiTheme.panelOutline);
      GuiTextRenderer.draw(
         GuiFont.INSTANCE,
         category.displayName().toUpperCase(Locale.ROOT),
         x + (11.0F) * scale,
         y + (9.0F) * scale,
         (6.3F) * scale,
         ClickGuiTheme.headerText
      );
      Render2D.drawRoundedImage(
         ClickGuiTheme.categoryIcon(category),
         x + width - (17.3F) * scale,
         y + (9.4F) * scale,
         (6.4F) * scale,
         (6.4F) * scale,
         0.0F,
         ClickGuiTheme.headerText
      );
      scroll.update(deltaSeconds);
      float rowY = y + (26.5F) * scale - scroll.getScroll();
      Render2D.pushScissor(x, viewportY, width, height - (21.0F) * scale);
      RoundedClipStack.push(x, viewportY, width, height - (21.0F) * scale, 0.0F, 0.0F, (8.5F) * scale, (8.5F) * scale);
      float[] viewportClip = null;

      for (Module module : ModuleRegistry.byCategory(category)) {
         ModuleCategoryRenderer.SearchEntry searchMetadata = searchEntry(module);
         float searchProgress = context.animateValue(
            searchMetadata.searchKey, !context.searchText.isEmpty() && !searchMetadata.lowerName.contains(context.searchText) && !searchMetadata.lowerDescription.contains(context.searchText) ? 0.0F : 1.0F
         );
         if (!(searchProgress < (0.01F))) {
            if (searchProgress < (0.999F)) {
               Render2D.pushAlpha(searchProgress * searchProgress);
            }

            float rowHeight = ModuleRowRenderer.getHeight(context, module, width - (9.2F) * scale);
            if (rowY + rowHeight >= viewportY - 2.0F && rowY <= viewportY + (height - (21.0F) * scale) + 2.0F) {
               if (viewportClip == null) {
                  viewportClip = new float[]{x, viewportY, width, height - (21.0F) * scale};
               }

               ModuleRowRenderer.render(context, module, x + (4.6F) * scale, rowY, width - (9.2F) * scale, viewportClip);
            }

            if (searchProgress < (0.999F)) {
               Render2D.popAlpha();
            }

            rowY += (rowHeight + (1.5F) * scale) * searchProgress;
         }
      }

      RoundedClipStack.pop();
      Render2D.popScissor();
      scroll.maxScroll = Math.max(0.0F, rowY - y + scroll.getScroll() - height + (4.5F) * scale);
   }

   private ModuleCategoryRenderer() {
   }

   public static void renderPanelCard(ClickGuiContext context, ModuleCategory category, float x, float y, float width, float height) {
      float scale = context.scale;
      boolean hovered = GuiVisuals.containsPoint(context.mouseX, context.mouseY, x, y, width, height);
      float hoverProgress = context.animateValue("panel.open." + category.name(), hovered ? 1.0F : 0.0F);
      float radius = (8.5F) * scale;
      Render2D.drawSharedBlur(x + (0.5F) * scale, y + (0.5F) * scale, width - 1.0F * scale, height - 1.0F * scale, (8.0F) * scale, (30.0F), 1.0F, -1);
      Render2D.fillRoundedRectangle(x, y, width, height, radius, ClickGuiTheme.panelBackground);
      if (hoverProgress > 0.01F) {
         Render2D.fillRoundedRectangle(x, y, width, height, radius, ArgbColor.withAlpha(ClickGuiTheme.accent, Math.round((18.0F) * hoverProgress)));
      }

      Render2D.drawOutline(
         x,
         y,
         width,
         height,
         radius,
         (0.5F),
         GuiVisuals.lerpColor(ArgbColor.withAlpha(ClickGuiTheme.accent, 60), ArgbColor.withAlpha(ClickGuiTheme.accent, 170), hoverProgress)
      );
      Render2D.drawRoundedImage(
         ClickGuiTheme.categoryIcon(category),
         x + (8.0F) * scale,
         y + height / 2.0F - (3.2F) * scale,
         (6.4F) * scale,
         (6.4F) * scale,
         0.0F,
         GuiVisuals.lerpColor(ClickGuiTheme.headerText, ClickGuiTheme.accent, hoverProgress)
      );
      GuiTextRenderer.draw(
         GuiFont.INSTANCE,
         category.displayName(),
         x + (17.5F) * scale,
         y + (height - (6.3F) * scale) / 2.0F,
         (6.3F) * scale,
         GuiVisuals.lerpColor(ClickGuiTheme.disabledValueText, ClickGuiTheme.tooltipText, hoverProgress)
      );
      Render2D.drawRoundedImage(
         ("clickgui/chevright"),
         x + width - (9.5F) * scale,
         y + height / 2.0F - (2.65F) * scale,
         (5.3F) * scale,
         (5.3F) * scale,
         0.0F,
         GuiVisuals.lerpColor(ClickGuiTheme.disabledSettingIcon, ClickGuiTheme.accent, hoverProgress)
      );
      context.registerClick(x, y, width, height, null, () -> openPanelMenu(category));
   }

   private static void openPanelMenu(ModuleCategory category) {
      Minecraft.getInstance().gui.setScreen(DeluxePanelGui.instance(panelOf(category)));
   }

   private static DeluxePanelGui.Panel panelOf(ModuleCategory category) {
      return switch (category) {
         case COSMETICS -> DeluxePanelGui.Panel.COSMETICS;
         case THEMES -> DeluxePanelGui.Panel.THEMES;
         default -> DeluxePanelGui.Panel.AUTOBUY;
      };
   }

   private static ModuleCategoryRenderer.SearchEntry searchEntry(Module module) {
      ModuleCategoryRenderer.SearchEntry cachedEntry = searchEntries.get(module);
      if (cachedEntry == null || cachedEntry.name != module.name || cachedEntry.description != module.description) {
         String searchDescription = module.description == null ? "" : module.description;
         cachedEntry = new ModuleCategoryRenderer.SearchEntry(module.name, module.description, module.name.toLowerCase(), searchDescription.toLowerCase(), "search." + module.name);
         searchEntries.put(module, cachedEntry);
      }

      return cachedEntry;
   }

   private record SearchEntry(String name, String description, String lowerName, String lowerDescription, String searchKey) {
   }
}
