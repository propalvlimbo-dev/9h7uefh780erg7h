package dev.rose.hud;

import dev.rose.anim.Spring;
import dev.rose.render.Images;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.ItemAlpha;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;


public final class HudContext {
   private static final float SHADOW_OFFSET = 0.6F;
   public final Minecraft mc = Minecraft.getInstance();
   public GuiGraphics g;
   public boolean preview;
   public float partialTick;
   public float alpha = 1.0F;
   public SdfFont regular;
   public SdfFont medium;
   public SdfFont semibold;
   public SdfFont bold;
   public SdfFont display;
   public SdfFont displayBold;
   public SdfFont icons;
   public Glass glass;
   public int label;
   public int secondary;
   private HudElement element;
   private final Deque<Boolean> clips = new ArrayDeque<>();
   private final Map<String, Spring> springs = new HashMap<>();

   boolean begin(GuiGraphics graphics, float partialTick, boolean preview) {
      this.g = graphics;
      this.partialTick = partialTick;
      this.preview = preview;
      this.regular = Fonts.get(FontWeight.REGULAR);
      this.medium = Fonts.get(FontWeight.MEDIUM);
      this.semibold = Fonts.get(FontWeight.SEMIBOLD);
      this.bold = Fonts.get(FontWeight.BOLD);
      this.display = Fonts.get(FontWeight.DISPLAY);
      this.displayBold = Fonts.get(FontWeight.DISPLAY_BOLD);
      this.icons = Fonts.icons();
      this.glass = Theme.glass();
      return this.regular != null && this.medium != null && this.semibold != null && this.bold != null && this.display != null && this.displayBold != null;
   }

   void element(HudElement current, float appear) {
      this.element = current;
      this.alpha = appear;

      this.label = switch (current.background()) {
         case GLASS -> this.glass.label;
         case SOLID -> luminance(current.backgroundColor()) > 0.55F ? -14935010 : -1;
         case NONE -> -1;
      };
      this.secondary = ColorUtil.fade(this.label, 0.62F);
   }

   public HudElement element() {
      return this.element;
   }

   public boolean alignRight() {
      return this.element.anchor().fx() == 1.0F;
   }

   public boolean alignCenter() {
      return this.element.anchor().fx() == 0.5F;
   }

   public boolean alignBottom() {
      return this.element.anchor().fy() == 1.0F;
   }

   public int accent() {
      return Theme.accent();
   }

   public Corners corners(float w, float h) {
      float half = Math.min(w, h) / 2.0F;
      float roundness = this.element.roundness();
      return roundness >= 0.999F ? Corners.round(half) : Corners.themed(half * roundness);
   }

   public void background(float x, float y, float w, float h) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && !(this.alpha <= 0.004F)) {
         HudElement.Background type = this.element.background();
         if (type != HudElement.Background.NONE) {
            Corners corners = this.corners(w, h);
            if (this.element.shadow()) {
               Surfaces.shadow(this.g, x, y + 1.5F, w, h, corners, new Shadow(1157627904, 5.0F, 0.0F, 0.0F, 1.5F), this.alpha);
            }

            if (type == HudElement.Background.GLASS) {
               LiquidGlass.draw(this.g, x, y, w, h, corners, this.glass, this.alpha);
            } else {
               int color = this.element.backgroundColor();
               Surfaces.fill(this.g, x, y, w, h, corners, color, color, color, color, this.alpha);
            }
         }
      }
   }

   public void fill(float x, float y, float w, float h, float radius, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F)) {
         Surfaces.fill(this.g, x, y, w, h, Corners.round(Math.min(radius, Math.min(w, h) / 2.0F)), color, color, color, color, this.alpha);
      }
   }

   public float text(SdfFont font, String text, float x, float y, float size, int color) {
      if (this.element.background() == HudElement.Background.NONE) {
         int shade = ColorUtil.withAlpha(0, Math.round(ColorUtil.alpha(color) * 0.5F * this.alpha));
         font.draw(this.g, text, x + 0.6F, y + 0.6F, size, shade);
      }

      return font.draw(this.g, text, x, y, size, ColorUtil.fade(color, this.alpha));
   }

   public float textRight(SdfFont font, String text, float right, float y, float size, int color) {
      float width = font.width(text, size);
      this.text(font, text, right - width, y, size, color);
      return width;
   }

   public static float centered(float y, float h, float size) {
      return y + h / 2.0F - size * 0.62F;
   }

   public void icon(int codepoint, float centerX, float centerY, float size, int color) {
      if (this.icons != null && codepoint != 0) {
         if (this.element.background() == HudElement.Background.NONE) {
            int shade = ColorUtil.withAlpha(0, Math.round(ColorUtil.alpha(color) * 0.5F * this.alpha));
            this.icons.drawIcon(this.g, codepoint, centerX + 0.6F, centerY + 0.6F, size, shade);
         }

         this.icons.drawIcon(this.g, codepoint, centerX, centerY, size, ColorUtil.fade(color, this.alpha));
      }
   }

   public void bar(float x, float y, float w, float h, float progress, int color, int track) {
      this.fill(x, y, w, h, h / 2.0F, track);
      float filled = w * Math.clamp(progress, 0.0F, 1.0F);
      if (filled > 0.2F) {
         this.fill(x, y, Math.max(filled, Math.min(h, w)), h, h / 2.0F, color);
      }
   }

   public void ring(float centerX, float centerY, float radius, float thickness, float progress, int color, int track) {
      float size = radius * 2.0F;
      Corners circle = Corners.round(radius);
      if (ColorUtil.alpha(track) > 0) {
         Surfaces.border(this.g, centerX - radius, centerY - radius, size, size, circle, thickness, track, this.alpha);
      }

      float p = Math.clamp(progress, 0.0F, 1.0F);
      if (!(p <= 0.001F)) {
         int clear = ColorUtil.withAlpha(color, 0);
         Paint paint = p >= 0.999F ? Paint.solid(color) : Paint.conic(-90.0F, color, color, clear, clear).withStops(0.0F, p, Math.min(1.0F, p + 0.002F), 1.0F);
         Surfaces.border(this.g, centerX - radius, centerY - radius, size, size, circle, thickness, paint, this.alpha);
      }
   }

   public void quad(float x0, float y0, float x1, float y1, int color) {
      Render2D.quad(this.g, x0, y0, x1, y1, ColorUtil.fade(color, this.alpha));
   }

   public void item(ItemStack stack, float x, float y, float size) {
      if (stack != null && !stack.isEmpty()) {
         this.g.pose().pushMatrix();
         this.g.pose().translate(x, y);
         this.g.pose().scale(size / 16.0F, size / 16.0F);
         ItemAlpha.render(this.g, stack, 0, 0, this.alpha);
         this.g.pose().popMatrix();
      }
   }

   public void effect(Holder<MobEffect> effect, float x, float y, float size) {
      this.g.pose().pushMatrix();
      this.g.pose().translate(x, y);
      this.g.pose().scale(size / 18.0F, size / 18.0F);
      this.g.blitSprite(RenderPipelines.GUI_TEXTURED, Gui.getMobEffectSprite(effect), 0, 0, 18, 18, ARGB.white(this.alpha));
      this.g.pose().popMatrix();
   }

   public void face(Identifier skin, float x, float y, float size, float radius) {
      Images.playerFace(this.g, skin, x, y, size, Corners.themed(radius), this.alpha);
   }

   public void clip(float x, float y, float w, float h) {
      Vector3f min = GuiDraw.pose(this.g).transformPosition(new Vector3f(x, y, 0.0F));
      Vector3f max = GuiDraw.pose(this.g).transformPosition(new Vector3f(x + w, y + h, 0.0F));
      GuiDraw.scissor(
         this.g,
         (int)Math.floor(Math.min(min.x, max.x)),
         (int)Math.floor(Math.min(min.y, max.y)),
         (int)Math.ceil(Math.max(min.x, max.x)),
         (int)Math.ceil(Math.max(min.y, max.y))
      );
      this.clips.push(Boolean.TRUE);
   }

   public void unclip() {
      if (this.clips.poll() != null) {
         this.g.disableScissor();
      }
   }

   void resetClips() {
      while (this.clips.poll() != null) {
         this.g.disableScissor();
      }
   }

   public Spring spring(String key, double initial, double response, double damping) {
      return this.springs.computeIfAbsent(this.element.id() + ":" + key, k -> new Spring(initial, response, damping));
   }

   public float animate(String key, float target, double response, double damping) {
      return this.spring(key, target, response, damping).setTarget(target).getFloat();
   }

   private static float luminance(int color) {
      return (ColorUtil.red(color) * 0.2126F + ColorUtil.green(color) * 0.7152F + ColorUtil.blue(color) * 0.0722F) / 255.0F;
   }
}
