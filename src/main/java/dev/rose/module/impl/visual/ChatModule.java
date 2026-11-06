package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Backdrop;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.setting.BoolSetting;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;


public final class ChatModule extends Module {
   private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
   private static final long SLIDE_MS = 260L;
   private final BoolSetting animation = this.add(new BoolSetting("animation", true));
   private final BoolSetting timestamps = this.add(new BoolSetting("timestamps", true));
   private final BoolSetting merge = this.add(new BoolSetting("merge", true));
   private final BoolSetting glass = this.add(new BoolSetting("glass", true));

   public ChatModule() {
      super("chat", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 57623;
   }

   public boolean merge() {
      return this.isEnabled() && this.merge.isOn();
   }

   public boolean glass() {
      return this.isEnabled() && this.glass.isOn();
   }

   public boolean animation() {
      return this.isEnabled() && this.animation.isOn();
   }

   public Component decorate(Component message) {
      return (Component)(this.isEnabled() && this.timestamps.isOn()
         ? Component.literal(LocalTime.now().format(TIME) + " ").withStyle(ChatFormatting.DARK_GRAY).append(message)
         : message);
   }

   public float slide(long firstSeen) {
      float t = Math.clamp((float)(System.currentTimeMillis() - firstSeen) / 260.0F, 0.0F, 1.0F);
      float eased = 1.0F - (float)Math.pow(1.0F - t, 3.0);
      return 1.0F - eased;
   }

   public void drawPanel(GuiGraphics g, float x, float y, float w, float h, float alpha) {
      if (!(alpha <= 0.01F) && !(h <= 0.0F)) {
         Backdrop.capture();
         Corners corners = Corners.themed(9.0F);
         Glass panel = Theme.glass().copy();
         panel.tint = Paint.solid(-1945235950);
         panel.bevel = 6.0F;
         Surfaces.shadow(g, x, y + 1.5F, w, h, corners, new Shadow(1073741824, 6.0F, 0.0F, 0.0F, 2.0F), alpha);
         LiquidGlass.draw(g, x, y, w, h, corners, panel, alpha);
      }
   }
}
