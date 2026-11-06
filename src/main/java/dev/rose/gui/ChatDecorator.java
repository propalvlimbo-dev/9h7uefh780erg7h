package dev.rose.gui;

import dev.rose.module.impl.visual.ChatModule;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;


public final class ChatDecorator implements ChatComponent.ChatGraphicsAccess {
   private static final Map<FormattedCharSequence, Long> SEEN = new WeakHashMap<>();
   private final ChatComponent.ChatGraphicsAccess delegate;
   private final GuiGraphics graphics;
   private final ChatModule chat;
   private final List<GuiMessage.Line> lines;
   private final int tick;
   private final boolean focused;
   private final float opacity;
   private float panelTop = Float.MAX_VALUE;
   private float panelBottom = -Float.MAX_VALUE;
   private float panelRight = -Float.MAX_VALUE;
   private boolean panelDrawn;

   public ChatDecorator(ChatComponent.ChatGraphicsAccess delegate, GuiGraphics graphics, ChatModule chat, List<GuiMessage.Line> lines, int tick, boolean focused, float opacity) {
      this.delegate = delegate;
      this.graphics = graphics;
      this.chat = chat;
      this.lines = lines;
      this.tick = tick;
      this.focused = focused;
      this.opacity = opacity;
   }

   public void updatePose(Consumer<Matrix3x2f> consumer) {
      this.delegate.updatePose(consumer);
   }

   public void fill(int x0, int y0, int x1, int y1, int color) {
      if (this.chat.glass() && x0 == -4) {
         this.panelTop = Math.min(this.panelTop, y0);
         this.panelBottom = Math.max(this.panelBottom, y1);
         this.panelRight = Math.max(this.panelRight, x1 - 4);
      } else {
         this.delegate.fill(x0, y0, x1, y1, color);
      }
   }

   public boolean handleMessage(int y, float alpha, FormattedCharSequence text) {
      this.drawPanel();
      if (!this.chat.animation()) {
         return this.delegate.handleMessage(y, alpha, text);
      }

      GuiMessage.Line line = this.find(text);
      long seen = SEEN.computeIfAbsent(text, key -> line != null && this.tick - line.addedTime() <= 20 ? System.currentTimeMillis() : 0L);
      float slide = seen == 0L ? 0.0F : this.chat.slide(seen);
      if (slide <= 0.001F) {
         return this.delegate.handleMessage(y, alpha, text);
      }

      float shift = -slide * 14.0F;
      this.delegate.updatePose(pose -> pose.translate(shift, 0.0F));
      boolean hovered = this.delegate.handleMessage(y, Math.max(0.02F, alpha * (1.0F - slide)), text);
      this.delegate.updatePose(pose -> pose.translate(-shift, 0.0F));
      return hovered;
   }

   public void handleTag(int x0, int y0, int x1, int y1, float alpha, GuiMessageTag tag) {
      this.delegate.handleTag(x0, y0, x1, y1, alpha, tag);
   }

   public void handleTagIcon(int x, int y, boolean hovered, GuiMessageTag tag, GuiMessageTag.Icon icon) {
      this.delegate.handleTagIcon(x, y, hovered, tag, icon);
   }

   private void drawPanel() {
      if (!this.panelDrawn && this.chat.glass() && !(this.panelBottom < this.panelTop)) {
         this.panelDrawn = true;
         float alpha = 0.0F;

         for (GuiMessage.Line line : this.lines) {
            int age = this.tick - line.addedTime();
            if (this.focused || age < 200) {
               alpha = Math.max(alpha, this.focused ? 1.0F : visibility(age));
            }
         }

         if (!(alpha <= 0.01F)) {
            this.chat
               .drawPanel(this.graphics, -4.0F, this.panelTop - 2.0F, this.panelRight + 4.0F, this.panelBottom - this.panelTop + 3.0F, alpha * this.opacity);
         }
      }
   }

   private static float visibility(int age) {
      double d = Mth.clamp((1.0 - age / 200.0) * 10.0, 0.0, 1.0);
      return (float)(d * d);
   }

   private GuiMessage.Line find(FormattedCharSequence text) {
      for (GuiMessage.Line line : this.lines) {
         if (line.content() == text) {
            return line;
         }
      }

      return null;
   }
}
