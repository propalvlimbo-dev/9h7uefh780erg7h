package dev.rose.gui.screen;

import dev.rose.anim.Spring;
import dev.rose.mixin.AbstractWidgetAccessor;
import dev.rose.module.impl.ScreensModule;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.util.ColorUtil;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;


public final class GlassWidgets {
   private static final Map<AbstractWidget, GlassWidgets.Style> STYLES = new WeakHashMap<>();
   private static final Map<AbstractWidget, Spring> HOVER = new WeakHashMap<>();
   private static final Map<Identifier, Integer> SPRITE_ICONS = Map.of(
      Identifier.withDefaultNamespace("icon/language"), 57598, Identifier.withDefaultNamespace("icon/accessibility"), 58007
   );
   public static boolean skipBackground;

   private GlassWidgets() {
   }

   public static GlassWidgets.Style style(AbstractWidget widget) {
      return STYLES.computeIfAbsent(widget, w -> new GlassWidgets.Style());
   }

   public static String key(AbstractWidget widget) {
      return widget.getMessage().getContents() instanceof TranslatableContents contents ? contents.getKey() : "";
   }

   public static boolean active() {
      if (!ScreensModule.glassButtons()) {
         return false;
      }

      Screen screen = Minecraft.getInstance().screen;
      return screen != null && !(screen instanceof AbstractContainerScreen);
   }

   public static GlassWidgets.ButtonResult renderButton(AbstractButton button, GuiGraphics g) {
      if (!active()) {
         return GlassWidgets.ButtonResult.VANILLA;
      }

      boolean glass = button.getClass() == Button.Plain.class || button.getClass() == CycleButton.class || button instanceof SpriteIconButton;
      if (!glass) {
         return GlassWidgets.ButtonResult.VANILLA;
      }

      try {
         return drawButton(button, g, ((AbstractWidgetAccessor)button).rose$alpha()) ? GlassWidgets.ButtonResult.CONTENT : GlassWidgets.ButtonResult.DONE;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return GlassWidgets.ButtonResult.VANILLA;
      }
   }

   public static boolean renderSlider(AbstractSliderButton slider, GuiGraphics g, double value, boolean adjusting) {
      if (!active()) {
         return false;
      }

      try {
         drawSlider(slider, g, value, adjusting, ((AbstractWidgetAccessor)slider).rose$alpha());
         return true;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   public static boolean replaceSprite(AbstractButton button, GuiGraphics g, Identifier sprite, int x, int y, int w, int h) {
      if (!active()) {
         return false;
      } else {
         GlassWidgets.Style style = STYLES.get(button);
         Integer icon = style != null && style.icon != 0 ? style.icon : SPRITE_ICONS.get(sprite);
         SdfFont icons = Fonts.icons();
         if (icon != null && icons != null) {
            float alpha = ((AbstractWidgetAccessor)button).rose$alpha() * (button.active ? 1.0F : 0.5F);
            int color = ColorUtil.fade(Theme.glass().label, alpha);
            float size = Math.min(button.getWidth(), button.getHeight()) * 0.52F;
            icons.drawIcon(g, icon, x + w / 2.0F, y + h / 2.0F, size, color);
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean renderEditBox(AbstractWidget box, GuiGraphics g, int x, int y, int w, int h) {
      if (!active()) {
         return false;
      }

      try {
         float hover = hover(box).setTarget(!box.isHovered() && !box.isFocused() ? 0.0 : 1.0).getFloat();
         Corners corners = Corners.themed(Math.min(h / 2.0F, 7.0F));
         Glass glass = Theme.glass().copy();
         glass.bevel = Math.min(glass.bevel, h * 0.3F);
         glass.refraction = Math.min(glass.refraction, h * 0.25F);
         float alpha = box.active ? 1.0F : 0.6F;
         LiquidGlass.draw(g, x, y, w, h, corners, glass, alpha);
         if (hover > 0.01F) {
            Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 12.0F))), alpha);
         }

         if (box.isFocused()) {
            Surfaces.border(g, x, y, w, h, corners, 1.2F, ColorUtil.withAlpha(Theme.accent(), 216), 1.0F);
         }

         return true;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   public static boolean renderCheckbox(AbstractWidget checkbox, GuiGraphics g, boolean selected, int x, int y, int size) {
      if (!active()) {
         return false;
      }

      try {
         float alpha = ((AbstractWidgetAccessor)checkbox).rose$alpha() * (checkbox.active ? 1.0F : 0.6F);
         float hover = hover(checkbox).setTarget(checkbox.isHovered() ? 1.0 : 0.0).getFloat();
         Corners corners = Corners.themed(size * 0.3F);
         Glass glass = Theme.glass().copy();
         glass.bevel = Math.min(glass.bevel, size * 0.3F);
         glass.refraction = Math.min(glass.refraction, size * 0.25F);
         LiquidGlass.draw(g, x, y, size, size, corners, glass, alpha);
         if (selected) {
            Surfaces.fill(
               g, x, y, size, size, corners, Paint.linear(90.0F, ColorUtil.withAlpha(Theme.accent(), 208), ColorUtil.withAlpha(Theme.accent2(), 208)), alpha
            );
         }

         if (hover > 0.01F) {
            Surfaces.fill(g, x, y, size, size, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 22.0F))), alpha);
         }

         if (checkbox.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard()) {
            Surfaces.border(g, x, y, size, size, corners, 1.2F, ColorUtil.withAlpha(Theme.accent(), 224), alpha);
         }

         SdfFont icons = Fonts.icons();
         if (selected && icons != null) {
            icons.drawIcon(g, 57452, x + size / 2.0F, y + size / 2.0F, size * 0.62F, ColorUtil.fade(-1, alpha));
         }

         return true;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   public static boolean renderLock(AbstractButton button, GuiGraphics g, boolean locked) {
      if (!active()) {
         return false;
      }

      try {
         float x = button.getX();
         float y = button.getY();
         float w = button.getWidth();
         float h = button.getHeight();
         float hover = hover(button).setTarget(button.active && button.isHoveredOrFocused() ? 1.0 : 0.0).getFloat();
         float alpha = button.active ? 1.0F : 0.6F;
         Corners corners = Corners.themed(Math.min(w, h) / 2.0F);
         Glass glass = Theme.glass().copy();
         glass.bevel = Math.min(glass.bevel, h * 0.34F);
         glass.refraction = Math.min(glass.refraction, h * 0.3F);
         LiquidGlass.draw(g, x, y, w, h, corners, glass, alpha);
         if (locked) {
            Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(Theme.accent(), 80)), alpha);
         }

         if (hover > 0.01F) {
            Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 24.0F))), alpha);
         }

         SdfFont icons = Fonts.icons();
         if (icons != null) {
            icons.drawIcon(g, locked ? 57611 : 57612, x + w / 2.0F, y + h / 2.0F, Math.min(w, h) * 0.5F, ColorUtil.fade(glass.label, alpha));
         }

         return true;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   public static boolean renderTextPanel(AbstractWidget widget, GuiGraphics g, int x, int y, int w, int h) {
      if (!active()) {
         return false;
      }

      try {
         float pad = 5.0F;
         Corners corners = Corners.themed(11.0F);
         LiquidGlass.draw(g, x - pad, y - pad, w + pad * 2.0F, h + pad * 2.0F, corners, Theme.glass(), 1.0F);
         if (widget.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard()) {
            Surfaces.border(g, x - pad, y - pad, w + pad * 2.0F, h + pad * 2.0F, corners, 1.2F, ColorUtil.withAlpha(Theme.accent(), 208), 1.0F);
         }

         return true;
      } catch (RuntimeException e) {
         ScreensModule.crashed(e);
         return false;
      }
   }

   private static Spring hover(AbstractWidget widget) {
      return HOVER.computeIfAbsent(widget, w -> new Spring(0.0, 0.28, 0.78));
   }

   private static boolean drawButton(AbstractButton button, GuiGraphics g, float widgetAlpha) {
      Minecraft mc = Minecraft.getInstance();
      GlassWidgets.Style style = STYLES.get(button);
      boolean enabled = button.active;
      float hover = hover(button).setTarget(enabled && button.isHovered() ? 1.0 : 0.0).getFloat();
      boolean keyboardFocus = button.isFocused() && mc.getLastInputType().isKeyboard();
      float alpha = widgetAlpha * (enabled ? 1.0F : 0.6F);
      float x = button.getX();
      float y = button.getY();
      float w = button.getWidth();
      float h = button.getHeight();
      float grow = Math.min(1.6F, Math.min(w, h) * 0.07F) * Math.clamp(hover, 0.0F, 1.2F);
      float gx = x - grow;
      float gy = y - grow * 0.7F;
      float gw = w + grow * 2.0F;
      float gh = h + grow * 1.4F;
      Corners corners = Corners.themed(gh / 2.0F);
      Glass glass = Theme.glass().copy();
      glass.bevel = Math.min(glass.bevel, gh * 0.34F);
      glass.refraction = Math.min(glass.refraction, gh * 0.3F);
      LiquidGlass.draw(g, gx, gy, gw, gh, corners, glass, alpha);
      if (style != null && style.tint != 0) {
         float amount = style.tintOnHover ? hover : 0.6F + 0.4F * hover;
         if (amount > 0.01F) {
            Surfaces.fill(g, gx, gy, gw, gh, corners, Paint.solid(ColorUtil.fade(style.tint, Math.min(1.0F, amount))), alpha);
         }
      }

      if (hover > 0.01F) {
         Surfaces.fill(g, gx, gy, gw, gh, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 24.0F))), alpha);
      }

      if (keyboardFocus) {
         Surfaces.border(g, gx, gy, gw, gh, corners, 1.2F, ColorUtil.withAlpha(Theme.accent(), 224), widgetAlpha);
      }

      int label = ColorUtil.withAlpha(glass.label, enabled ? 255 : 153);
      if (button instanceof SpriteIconButton) {
         return true;
      }

      if (style != null && style.iconOnly && style.icon != 0) {
         SdfFont icons = Fonts.icons();
         int color = style.tint != 0 && style.tintOnHover ? ColorUtil.lerp(label, -1, hover) : label;
         if (icons != null) {
            icons.drawIcon(g, style.icon, x + w / 2.0F, y + h / 2.0F, Math.min(w, h) * 0.5F, ColorUtil.fade(color, widgetAlpha));
         }

         return false;
      } else {
         if (drawLabel(g, button.getMessage().getString(), style, x, y, w, h, label, widgetAlpha, hover)) {
            return false;
         }

         int vanillaColor = (enabled ? glass.label : ColorUtil.lerp(glass.label, -8355712, 0.5F)) & 16777215;
         Component message = button.getMessage().copy().withColor(vanillaColor);
         g.textRendererForWidget(button, GuiGraphics.HoveredTextEffects.NONE)
            .acceptScrollingWithDefaultCenter(
               message,
               button.getX() + 2,
               button.getX() + button.getWidth() - 2,
               button.getY(),
               button.getY() + button.getHeight()
            );
         return false;
      }
   }

   private static boolean drawLabel(GuiGraphics g, String text, GlassWidgets.Style style, float x, float y, float w, float h, int color, float alpha, float hover) {
      SdfFont font = Fonts.get(FontWeight.SEMIBOLD);
      if (font != null && !text.isEmpty() && font.canDisplay(text)) {
         int icon = style != null ? style.icon : 0;
         SdfFont icons = icon != 0 ? Fonts.icons() : null;
         float size = Math.clamp(h * 0.42F, 7.0F, 10.0F);
         float iconSize = size * 1.35F;
         float gap = size * 0.6F;
         float padding = Math.max(6.0F, h * 0.45F);
         float iconW = icons != null ? iconSize + gap : 0.0F;
         float textW = font.width(text, size);
         float available = w - padding * 2.0F;
         if (textW + iconW > available) {
            float fit = size * (available - iconW) / textW;
            if (fit < 6.2F) {
               return false;
            }

            size = fit;
            textW = font.width(text, size);
         }

         boolean left = style != null && style.alignLeft;
         float start = left ? x + padding : x + (w - textW - iconW) / 2.0F;
         float cy = y + h / 2.0F;
         if (icons != null) {
            int iconColor = ColorUtil.lerp(color, ColorUtil.withAlpha(Theme.accent(), ColorUtil.alpha(color)), 0.55F + 0.45F * Math.clamp(hover, 0.0F, 1.0F));
            icons.drawIcon(g, icon, start + iconSize / 2.0F, cy, iconSize, ColorUtil.fade(iconColor, alpha));
         }

         font.draw(g, text, start + iconW, cy - font.ascent(size) + font.capHeight(size) / 2.0F, size, ColorUtil.fade(color, alpha));
         return true;
      } else {
         return false;
      }
   }

   private static void drawSlider(AbstractSliderButton slider, GuiGraphics g, double value, boolean adjusting, float widgetAlpha) {
      Minecraft mc = Minecraft.getInstance();
      boolean enabled = slider.active;
      float hover = hover(slider).setTarget(!enabled || !slider.isHovered() && !adjusting ? 0.0 : 1.0).getFloat();
      float alpha = widgetAlpha * (enabled ? 1.0F : 0.6F);
      float x = slider.getX();
      float y = slider.getY();
      float w = slider.getWidth();
      float h = slider.getHeight();
      Corners corners = Corners.themed(h / 2.0F);
      Glass glass = Theme.glass().copy();
      glass.bevel = Math.min(glass.bevel, h * 0.34F);
      glass.refraction = Math.min(glass.refraction, h * 0.3F);
      LiquidGlass.draw(g, x, y, w, h, corners, glass, alpha);
      float inset = 2.0F;
      float knobW = Math.max(6.0F, h * 0.34F);
      float knobX = x + inset + (float)value * (w - inset * 2.0F - knobW);
      float fillW = knobX + knobW / 2.0F - x;
      if (fillW > 1.0F) {
         Surfaces.fill(g, x, y, fillW, h, corners, Paint.linear(0.0F, ColorUtil.withAlpha(Theme.accent(), 48), ColorUtil.withAlpha(Theme.accent2(), 72)), alpha);
      }

      if (hover > 0.01F) {
         Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(glass.label, Math.round(Math.min(1.0F, hover) * 18.0F))), alpha);
      }

      float knobGrow = 1.2F * Math.clamp(hover, 0.0F, 1.2F);
      float ky = y + inset - knobGrow;
      float kh = h - inset * 2.0F + knobGrow * 2.0F;
      Glass lens = Glass.lens();
      LiquidGlass.shapes().add(knobX - knobGrow * 0.5F, ky, knobW + knobGrow, kh, Corners.themed(kh / 2.0F)).refreshBackdrop().draw(g, lens, 0.0F, alpha);
      Surfaces.fill(
         g,
         knobX - knobGrow * 0.5F,
         ky,
         knobW + knobGrow,
         kh,
         Corners.themed(kh / 2.0F),
         Paint.solid(ColorUtil.withAlpha(-1, 112 + Math.round(48.0F * Math.min(1.0F, hover)))),
         alpha
      );
      if (slider.isFocused() && mc.getLastInputType().isKeyboard()) {
         Surfaces.border(g, x, y, w, h, corners, 1.2F, ColorUtil.withAlpha(Theme.accent(), adjusting ? 255 : 176), widgetAlpha);
      }

      int label = ColorUtil.withAlpha(glass.label, enabled ? 255 : 153);
      String text = slider.getMessage().getString();
      SdfFont font = Fonts.get(FontWeight.SEMIBOLD);
      float size = Math.clamp(h * 0.42F, 7.0F, 10.0F);
      if (font != null && font.canDisplay(text) && font.width(text, size) < w - 16.0F) {
         font.draw(
            g,
            text,
            x + (w - font.width(text, size)) / 2.0F,
            y + h / 2.0F - font.ascent(size) + font.capHeight(size) / 2.0F,
            size,
            ColorUtil.fade(label, widgetAlpha)
         );
      } else {
         g.textRendererForWidget(slider, GuiGraphics.HoveredTextEffects.NONE)
            .acceptScrollingWithDefaultCenter(
               slider.getMessage().copy().withColor(label & 16777215),
               slider.getX() + 2,
               slider.getX() + slider.getWidth() - 2,
               slider.getY(),
               slider.getY() + slider.getHeight()
            );
      }
   }

   public enum ButtonResult {
      VANILLA,
      DONE,
      CONTENT;
   }

   public static final class Style {
      public int icon;
      public boolean iconOnly;
      public boolean alignLeft;
      public int tint;
      public boolean tintOnHover;

      public GlassWidgets.Style icon(int value) {
         this.icon = value;
         return this;
      }

      public GlassWidgets.Style iconOnly() {
         this.iconOnly = true;
         return this;
      }

      public GlassWidgets.Style left() {
         this.alignLeft = true;
         return this;
      }

      public GlassWidgets.Style tint(int color, boolean onHover) {
         this.tint = color;
         this.tintOnHover = onHover;
         return this;
      }
   }
}
