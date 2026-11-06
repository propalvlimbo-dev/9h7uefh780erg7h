package dev.rose.gui.screen;

import dev.rose.module.impl.ScreensModule;
import dev.rose.render.Backdrop;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.style.Corner;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.Slot;


public final class InventoryDesign {
   public static final int LABEL = -1644564;
   private static final int DIM = 1191182335;
   private static final int FIRE = -24771;

   private InventoryDesign() {
   }

   public static boolean handles(AbstractContainerScreen<?> screen) {
      return !ScreensModule.inventory()
         ? false
         : screen instanceof InventoryScreen
            || screen instanceof ContainerScreen
            || screen instanceof ShulkerBoxScreen
            || screen instanceof DispenserScreen
            || screen instanceof HopperScreen
            || screen instanceof CraftingScreen
            || screen instanceof AbstractFurnaceScreen
            || screen instanceof BrewingStandScreen
            || screen instanceof AnvilScreen;
   }

   public static void renderBackground(AbstractContainerScreen<?> screen, GuiGraphics g, int left, int top, int width, int height, int mouseX, int mouseY) {
      Backdrop.capture();
      int accent = Theme.accent();
      Corners corners = Corners.themed(16.0F);
      float pad = 4.0F;
      float x = left - pad;
      float y = top - pad;
      float w = width + pad * 2.0F;
      float h = height + pad * 2.0F;
      Surfaces.shadow(g, x, y + 5.0F, w, h, corners, new Shadow(2013265920, 24.0F, 0.0F, 0.0F, 10.0F), 1.0F);
      Glass glass = Theme.glass().copy();
      glass.tint = Paint.solid(-1005843182);
      glass.blur = Math.max(glass.blur, 0.85F);
      LiquidGlass.draw(g, x, y, w, h, corners, glass, 1.0F);
      Surfaces.fill(
         g,
         x + 1.0F,
         y + 1.0F,
         w - 2.0F,
         46.0F,
         new Corners(corners.topLeft(), corners.topRight(), Corner.NONE, Corner.NONE),
         ColorUtil.withAlpha(accent, 28),
         ColorUtil.withAlpha(accent, 28),
         ColorUtil.withAlpha(accent, 0),
         ColorUtil.withAlpha(accent, 0),
         1.0F
      );
      if (screen instanceof InventoryScreen) {
         playerCard(g, left, top, accent, mouseX, mouseY);
      }

      Corners cell = Corners.themed(5.0F);

      for (Slot slot : screen.getMenu().slots) {
         if (slot.isActive()) {
            float sx = left + slot.x - 1;
            float sy = top + slot.y - 1;
            Surfaces.fill(g, sx, sy, 18.0F, 18.0F, cell, Paint.solid(1711276032), 1.0F);
            Surfaces.border(g, sx, sy, 18.0F, 18.0F, cell, 0.6F, 520093695, 1.0F);
         }
      }

      arrows(screen, g, left, top);
      if (screen instanceof AbstractFurnaceScreen<?> furnace) {
         furnace(g, (AbstractFurnaceMenu)furnace.getMenu(), left, top, accent);
      } else if (screen instanceof BrewingStandScreen brewing) {
         brewing(g, (BrewingStandMenu)brewing.getMenu(), left, top, accent);
      } else if (screen instanceof AnvilScreen anvil) {
         anvil(g, (AnvilMenu)anvil.getMenu(), left, top, accent);
      }
   }

   private static void playerCard(GuiGraphics g, int left, int top, int accent, int mouseX, int mouseY) {
      float x = left + 25.5F;
      float y = top + 7.5F;
      float w = 50.0F;
      float h = 71.0F;
      Corners corners = Corners.themed(9.0F);
      Surfaces.fill(g, x, y, w, h, corners, Paint.solid(1879048192), 1.0F);
      Surfaces.fill(
         g,
         x,
         y + h * 0.45F,
         w,
         h * 0.55F,
         new Corners(Corner.NONE, Corner.NONE, corners.bottomRight(), corners.bottomLeft()),
         ColorUtil.withAlpha(accent, 0),
         ColorUtil.withAlpha(accent, 0),
         ColorUtil.withAlpha(accent, 70),
         ColorUtil.withAlpha(accent, 70),
         1.0F
      );
      Surfaces.border(g, x, y, w, h, corners, 0.6F, 620756991, 1.0F);
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
         InventoryScreen.renderEntityInInventoryFollowsMouse(g, left + 26, top + 8, left + 75, top + 78, 30, 0.0625F, mouseX, mouseY, mc.player);
      }
   }

   private static void gauge(GuiGraphics g, SdfFont icons, int icon, float centerX, float centerY, float size, int color, int x0, int y0, int x1, int y1) {
      icons.drawIcon(g, icon, centerX, centerY, size, 1191182335);
      if (x1 > x0 && y1 > y0) {
         GuiDraw.scissor(g, x0, y0, x1, y1);
         icons.drawIcon(g, icon, centerX, centerY, size, color);
         g.disableScissor();
      }
   }

   private static void furnace(GuiGraphics g, AbstractFurnaceMenu menu, int left, int top, int accent) {
      SdfFont icons = Fonts.icons();
      if (icons != null) {
         int lit = menu.isLit() ? Mth.ceil(menu.getLitProgress() * 13.0F) + 1 : 0;
         gauge(g, icons, 57554, left + 63, top + 43, 13.0F, -24771, left + 54, top + 36 + 14 - lit, left + 72, top + 51);
         int burn = Mth.ceil(menu.getBurnProgress() * 24.0F);
         gauge(g, icons, 57417, left + 91, top + 42, 16.0F, accent | 0xFF000000, left + 79, top + 32, left + 79 + burn, top + 52);
      }
   }

   private static void brewing(GuiGraphics g, BrewingStandMenu menu, int left, int top, int accent) {
      SdfFont icons = Fonts.icons();
      int fuel = Mth.clamp((18 * menu.getFuel() + 19) / 20, 0, 18);
      int ticks = menu.getBrewingTicks();
      Surfaces.fill(g, left + 60, top + 44, 18.0F, 4.0F, Corners.round(2.0F), Paint.solid(1711276032), 1.0F);
      if (fuel > 0) {
         Surfaces.fill(g, left + 60, top + 44, Math.max(4, fuel), 4.0F, Corners.round(2.0F), Paint.solid(-24771), 1.0F);
      }

      Surfaces.fill(g, left + 99.5F, top + 16, 4.0F, 28.0F, Corners.round(2.0F), Paint.solid(1711276032), 1.0F);
      if (ticks > 0) {
         float done = 28.0F * (1.0F - ticks / 400.0F);
         if (done > 0.5F) {
            Surfaces.fill(g, left + 99.5F, top + 16, 4.0F, Math.max(4.0F, done), Corners.round(2.0F), Paint.solid(accent | 0xFF000000), 1.0F);
         }

         float time = Theme.time();

         for (int i = 0; i < 4; i++) {
            float rise = (time * 0.9F + i * 0.25F) % 1.0F;
            float size = 2.2F + 1.6F * ((i * 7 + 3) % 4) / 3.0F;
            float x = left + 69 + Mth.sin(time * 3.0F + i * 1.9F) * 2.2F - size / 2.0F;
            float y = top + 42 - rise * 27.0F;
            Surfaces.border(
               g,
               x,
               y,
               size,
               size,
               Corners.round(size / 2.0F),
               0.6F,
               ColorUtil.withAlpha(-1, Math.round(200.0F * (1.0F - rise) * Math.min(1.0F, rise * 6.0F))),
               1.0F
            );
         }
      }

      if (icons != null) {
         if (!menu.getSlot(4).hasItem()) {
            icons.drawIcon(g, 57554, left + 25, top + 25, 10.0F, 1191182335);
         }

         for (int i = 0; i < 3; i++) {
            Slot bottle = menu.getSlot(i);
            if (!bottle.hasItem()) {
               icons.drawIcon(g, 57557, left + bottle.x + 8, top + bottle.y + 8, 10.0F, 1191182335);
            }
         }
      }
   }

   private static void anvil(GuiGraphics g, AnvilMenu menu, int left, int top, int accent) {
      boolean named = menu.getSlot(0).hasItem();
      Corners field = Corners.themed(5.0F);
      Surfaces.fill(g, left + 59, top + 20, 110.0F, 16.0F, field, Paint.solid(1879048192), 1.0F);
      Surfaces.border(g, left + 59, top + 20, 110.0F, 16.0F, field, 0.7F, named ? ColorUtil.withAlpha(accent, 176) : 520093695, 1.0F);
      SdfFont icons = Fonts.icons();
      if (icons != null) {
         icons.drawIcon(g, 57661, left + 59.5F, top + 55, 11.0F, -1593835521);
         boolean error = (named || menu.getSlot(1).hasItem()) && !menu.getSlot(menu.getResultSlot()).hasItem();
         icons.drawIcon(g, 57417, left + 113, top + 55, 14.0F, error ? 1191182335 : -1325400065);
         if (error) {
            icons.drawIcon(g, 57778, left + 113, top + 55, 13.0F, -43703);
         }
      }
   }

   private static void arrows(AbstractContainerScreen<?> screen, GuiGraphics g, int left, int top) {
      SdfFont icons = Fonts.icons();
      if (icons != null) {
         if (screen instanceof InventoryScreen) {
            icons.drawIcon(g, 57417, left + 143, top + 36, 10.0F, -1325400065);
         } else if (screen instanceof CraftingScreen) {
            icons.drawIcon(g, 57417, left + 101, top + 43, 13.0F, -1325400065);
         }
      }
   }

   public static void renderSlotMarks(GuiGraphics g, int x, int y, int search, boolean locked) {
      int accent = Theme.accent() | 0xFF000000;
      Corners cell = Corners.themed(5.0F);
      if (search > 0) {
         Surfaces.fill(g, x - 1, y - 1, 18.0F, 18.0F, cell, Paint.solid(ColorUtil.withAlpha(accent, 48)), 1.0F);
         Surfaces.border(g, x - 1, y - 1, 18.0F, 18.0F, cell, 1.0F, ColorUtil.withAlpha(accent, 240), 1.0F);
      } else if (search < 0) {
         Surfaces.fill(g, x - 1, y - 1, 18.0F, 18.0F, cell, Paint.solid(-938865905), 1.0F);
      }

      if (locked) {
         float cx = x + 13.5F;
         float cy = y + 2.5F;
         Surfaces.fill(g, cx - 4.5F, cy - 4.5F, 9.0F, 9.0F, Corners.round(4.5F), Paint.solid(-267119333), 1.0F);
         Surfaces.border(g, cx - 4.5F, cy - 4.5F, 9.0F, 9.0F, Corners.round(4.5F), 0.7F, ColorUtil.withAlpha(accent, 216), 1.0F);
         SdfFont icons = Fonts.icons();
         if (icons != null) {
            icons.drawIcon(g, 57611, cx, cy, 5.6F, -1);
         }
      }
   }

   public static void renderSlotHighlight(GuiGraphics g, int x, int y) {
      int accent = Theme.accent();
      Corners cell = Corners.themed(5.0F);
      Surfaces.fill(g, x - 1, y - 1, 18.0F, 18.0F, cell, Paint.solid(ColorUtil.withAlpha(accent, 74)), 1.0F);
      Surfaces.border(g, x - 1, y - 1, 18.0F, 18.0F, cell, 0.8F, ColorUtil.withAlpha(ColorUtil.lerp(accent, -1, 0.35F), 208), 1.0F);
   }
}
