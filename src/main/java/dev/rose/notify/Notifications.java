package dev.rose.notify;

import dev.rose.anim.Spring;
import dev.rose.gui.UiSounds;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.NotificationsModule;
import dev.rose.module.impl.hud.IslandModule;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.style.Glass;
import dev.rose.util.ColorUtil;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;


public final class Notifications {
   private static final int MAX_QUEUE = 6;
   private static final long APPEAR_MS = 150L;
   private static final long CLOSE_MS = 330L;
   private static final long HIDE_MS = 380L;
   private static final Deque<Notifications.Entry> queue = new ArrayDeque<>();
   private static final Spring presence = new Spring(0.0, 0.42, 0.78);
   private static final Spring expansion = new Spring(0.0, 0.5, 0.66);
   private static Notifications.Entry current;
   private static Notifications.Phase phase = Notifications.Phase.IDLE;
   private static long phaseStart;

   private Notifications() {
   }

   public static void show(Notifications.Kind kind, String title, String subtitle) {
      NotificationsModule settings = Modules.notifications();
      if (settings == null || settings.isEnabled() || kind.important()) {
         long duration = settings == null ? 2200L : settings.durationMs();
         if (kind.important()) {
            duration = Math.max(duration, 4500L);
         }

         if (queue.size() >= 6) {
            queue.pollFirst();
         }

         queue.addLast(new Notifications.Entry(kind, title, subtitle == null ? "" : subtitle, duration));
         UiSounds.play(UiSounds.Sound.NOTIFY);
      }
   }

   public static Notifications.Shown shown() {
      update();
      if (current != null && (phase == Notifications.Phase.APPEAR || phase == Notifications.Phase.OPEN)) {
         float left = phase == Notifications.Phase.OPEN
            ? 1.0F - Math.min((float)(System.currentTimeMillis() - phaseStart) / (float)current.durationMs, 1.0F)
            : 1.0F;
         return new Notifications.Shown(current.title, current.subtitle, current.kind.color(), left);
      } else {
         return null;
      }
   }

   private static boolean islandShows() {
      IslandModule island = Modules.island();
      Minecraft mc = Minecraft.getInstance();
      return island != null && island.showsNotices() && mc.screen == null && mc.level != null && !mc.options.hideGui;
   }

   public static void moduleToggled(Module module) {
      NotificationsModule settings = Modules.notifications();
      if (settings != null && settings.isEnabled() && settings.showToggles()) {
         boolean on = module.isEnabled();
         show(
            on ? Notifications.Kind.SUCCESS : Notifications.Kind.NEUTRAL,
            module.name(),
            I18n.get(on ? "rose.notify.enabled" : "rose.notify.disabled", new Object[0])
         );
      }
   }

   public static void render(GuiGraphics g) {
      update();
      if (current != null && !islandShows()) {
         SdfFont bold = Fonts.get(FontWeight.SEMIBOLD);
         SdfFont regular = Fonts.get(FontWeight.REGULAR);
         if (bold != null && regular != null) {
            float appear = presence.getFloat();
            float alpha = Math.clamp(appear, 0.0F, 1.0F);
            if (!(alpha <= 0.01F)) {
               float open = expansion.getFloat();
               float titleSize = 7.5F;
               float subtitleSize = 6.5F;
               float dot = 7.0F;
               boolean hasSubtitle = !current.subtitle.isEmpty();
               float textWidth = Math.max(bold.width(current.title, titleSize), hasSubtitle ? regular.width(current.subtitle, subtitleSize) : 0.0F);
               float openW = 12.0F + dot + 8.0F + textWidth + 14.0F;
               float openH = hasSubtitle ? 30.0F : 20.0F;
               float closedW = 30.0F;
               float closedH = 10.0F;
               float grow = 0.55F + 0.45F * appear;
               float w = Math.max(4.0F, (closedW + (openW - closedW) * open) * grow);
               float h = Math.max(4.0F, (closedH + (openH - closedH) * open) * grow);
               float x = (g.guiWidth() - w) / 2.0F;
               float y = 5.0F;
               Glass glass = Theme.glass();
               Render2D.shadow(g, x, y + 2.0F, w, h, h / 2.0F, 8.0F, ColorUtil.fade(1426063360, alpha));
               LiquidGlass.draw(g, x, y, w, h, h / 2.0F, glass, alpha);
               float contentAlpha = alpha * smooth((Math.clamp(open, 0.0F, 1.0F) - 0.55F) / 0.45F);
               if (contentAlpha > 0.01F) {
                  GuiDraw.scissor(g, (int)Math.floor(x), (int)Math.floor(y), (int)Math.ceil(x + w), (int)Math.ceil(y + h));
                  int color = current.kind.color();
                  float centerY = y + h / 2.0F;
                  float cursor = x + 12.0F;
                  Render2D.circle(g, cursor + dot / 2.0F, centerY, dot / 2.0F, ColorUtil.fade(color, contentAlpha));
                  cursor += dot + 8.0F;
                  int label = glass.label;
                  if (hasSubtitle) {
                     bold.draw(g, current.title, cursor, centerY - titleSize - 0.5F, titleSize, ColorUtil.fade(label, contentAlpha));
                     regular.draw(g, current.subtitle, cursor, centerY + 1.0F, subtitleSize, ColorUtil.fade(label, contentAlpha * 0.65F));
                  } else {
                     bold.draw(g, current.title, cursor, centerY - titleSize / 2.0F, titleSize, ColorUtil.fade(label, contentAlpha));
                  }

                  if (phase == Notifications.Phase.OPEN) {
                     float left = 1.0F - Math.min((float)(System.currentTimeMillis() - phaseStart) / (float)current.durationMs, 1.0F);
                     Render2D.rect(g, x + h / 2.0F, y + h - 2.4F, (w - h) * left, 1.2F, 0.6F, ColorUtil.fade(color, contentAlpha * 0.6F));
                  }

                  g.disableScissor();
               }
            }
         }
      }
   }

   private static void update() {
      long now = System.currentTimeMillis();
      long elapsed = now - phaseStart;
      switch (phase) {
         case IDLE:
            if (!queue.isEmpty()) {
               current = queue.pollFirst();
               enter(Notifications.Phase.APPEAR, now);
               presence.setTarget(1.0);
            }
            break;
         case APPEAR:
            if (elapsed >= 150L) {
               enter(Notifications.Phase.OPEN, now);
               expansion.setTarget(1.0);
            }
            break;
         case OPEN:
            if (elapsed >= current.durationMs) {
               enter(Notifications.Phase.CLOSE, now);
               expansion.setTarget(0.0);
            }
            break;
         case CLOSE:
            if (elapsed >= 330L) {
               if (!queue.isEmpty()) {
                  current = queue.pollFirst();
                  enter(Notifications.Phase.APPEAR, now);
               } else {
                  enter(Notifications.Phase.HIDE, now);
                  presence.setTarget(0.0);
               }
            }
            break;
         case HIDE:
            if (elapsed >= 380L) {
               enter(Notifications.Phase.IDLE, now);
               current = null;
            }
      }
   }

   private static void enter(Notifications.Phase next, long now) {
      phase = next;
      phaseStart = now;
   }

   private static float smooth(float t) {
      float x = Math.clamp(t, 0.0F, 1.0F);
      return x * x * (3.0F - 2.0F * x);
   }

   private record Entry(Notifications.Kind kind, String title, String subtitle, long durationMs) {
   }

   public enum Kind {
      INFO,
      SUCCESS,
      NEUTRAL,
      WARNING,
      ERROR;

      int color() {
         return switch (this) {
            case INFO -> Theme.accent();
            case SUCCESS -> Theme.positive;
            case NEUTRAL -> Theme.neutral;
            case WARNING -> Theme.warning;
            case ERROR -> Theme.danger;
         };
      }

      boolean important() {
         return this == WARNING || this == ERROR;
      }
   }

   private enum Phase {
      IDLE,
      APPEAR,
      OPEN,
      CLOSE,
      HIDE;
   }

   public record Shown(String title, String subtitle, int color, float remaining) {
   }
}
