package dev.rose.hud;

import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.HudSettingsModule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;


public final class HudManager {
   private static final HudContext context = new HudContext();
   private static List<HudElement> elements;
   private static boolean editing;

   private HudManager() {
   }

   public static List<HudElement> elements() {
      if (elements == null) {
         List<HudElement> found = new ArrayList<>();

         for (Module module : Modules.all()) {
            if (module instanceof HudElement element) {
               found.add(element);
            }
         }

         elements = List.copyOf(found);
      }

      return elements;
   }

   public static boolean editing() {
      return editing;
   }

   public static void setEditing(boolean value) {
      editing = value;
   }

   public static void renderInGame(GuiGraphics g, DeltaTracker deltaTracker) {
      if (!editing) {
         Minecraft mc = Minecraft.getInstance();
         HudSettingsModule settings = Modules.hud();
         boolean hidden = mc.options.hideGui || settings.hideWithDebug() && mc.getDebugOverlay().showDebugScreen();
         render(g, deltaTracker.getGameTimeDeltaPartialTick(false), false, hidden);
      }
   }

   public static void render(GuiGraphics g, float partialTick, boolean preview, boolean hideAll) {
      HudContext hud = context;
      if (hud.begin(g, partialTick, preview)) {
         boolean animate = Modules.hud().animations();
         float screenW = g.guiWidth();
         float screenH = g.guiHeight();

         for (HudElement element : elements()) {
            hud.element(element, 1.0F);

            try {
               boolean show = element.isEnabled() && !hideAll && (preview || element.hasContent(hud));
               element.presence.setTarget(show ? 1.0 : 0.0);
               if (!animate) {
                  element.presence.snap(show ? 1.0 : 0.0);
               }

               float appear = element.presence.getFloat();
               if (appear <= 0.004F && !show) {
                  element.bounds[2] = 0.0F;
                  element.bounds[3] = 0.0F;
               } else {
                  drawElement(hud, element, appear, screenW, screenH, animate);
               }
            } catch (RuntimeException e) {
               hud.resetClips();
               Modules.fail(element, e);
            }
         }
      }
   }

   private static void drawElement(HudContext hud, HudElement element, float appear, float screenW, float screenH, boolean animate) {
      Size size = element.measure(hud);
      if (!element.measured || !animate) {
         element.animatedWidth.snap(size.width());
         element.animatedHeight.snap(size.height());
         element.measured = true;
      }

      float w = Math.max(1.0F, element.animatedWidth.setTarget(size.width()).getFloat());
      float h = Math.max(1.0F, element.animatedHeight.setTarget(size.height()).getFloat());
      element.contentWidth = w;
      element.contentHeight = h;
      float scale = element.scale();
      float[] target = position(element, w * scale, h * scale, screenW, screenH);
      if (!element.placed || !animate) {
         element.animatedX.snap(target[0]);
         element.animatedY.snap(target[1]);
         element.placed = true;
      }

      float x = element.animatedX.setTarget(target[0]).getFloat();
      float y = element.animatedY.setTarget(target[1]).getFloat();
      element.bounds[0] = x;
      element.bounds[1] = y;
      element.bounds[2] = w * scale;
      element.bounds[3] = h * scale;
      float visible = Math.clamp(appear, 0.0F, 1.0F);
      float pop = 0.9F + 0.1F * Math.min(appear, 1.04F);
      hud.element(element, visible);
      hud.g.pose().pushMatrix();

      try {
         hud.g.pose().translate(x + w * scale / 2.0F, y + h * scale / 2.0F);
         hud.g.pose().scale(scale * pop, scale * pop);
         hud.g.pose().translate(-w / 2.0F, -h / 2.0F);
         if (element.drawsBackground()) {
            hud.background(0.0F, 0.0F, w, h);
         }

         element.render(hud, w, h);
      } finally {
         hud.resetClips();
         hud.g.pose().popMatrix();
      }
   }

   public static float[] position(HudElement element, float w, float h, float screenW, float screenH) {
      Anchor anchor = element.anchor();
      float x = anchor.fx() * screenW + element.offsetX() - anchor.fx() * w;
      float y = anchor.fy() * screenH + element.offsetY() - anchor.fy() * h;
      x = Math.clamp(x, 0.0F, Math.max(0.0F, screenW - w));
      y = Math.clamp(y, 0.0F, Math.max(0.0F, screenH - h));
      return new float[]{x, y};
   }

   public static void placeAt(HudElement element, float x, float y, float w, float h, float screenW, float screenH) {
      Anchor anchor = Anchor.nearest(x + w / 2.0F, y + h / 2.0F, screenW, screenH);
      float offsetX = x + anchor.fx() * w - anchor.fx() * screenW;
      float offsetY = y + anchor.fy() * h - anchor.fy() * screenH;
      element.place(anchor, Math.round(offsetX * 2.0F) / 2.0F, Math.round(offsetY * 2.0F) / 2.0F);
   }

   public static float[] contentSize(HudElement element) {
      return new float[]{element.contentWidth, element.contentHeight};
   }

   public static void setFollowTight(HudElement element, boolean tight) {
      double response = tight ? 0.06 : 0.3;
      double damping = tight ? 1.0 : 0.88;
      element.animatedX.configure(response, damping);
      element.animatedY.configure(response, damping);
   }
}
