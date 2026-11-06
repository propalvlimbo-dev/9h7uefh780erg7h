package sg.events.impl;

import lombok.Generated;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class HudRenderEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("п弟")  float mouseX;
   private final @com.google.gson.annotations.SerializedName("пб")  float mouseY;
   private final @com.google.gson.annotations.SerializedName("Я")  GuiGraphicsExtractor graphics;

   @Generated
   public float getMouseX() {
      return this.mouseX;
   }

   @Generated
   public GuiGraphicsExtractor getGraphics() {
      return this.graphics;
   }

   @Generated
   public float getMouseY() {
      return this.mouseY;
   }

   @Generated
   public HudRenderEvent(float mouseX, float mouseY, GuiGraphicsExtractor graphics) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.graphics = graphics;
   }

   public static class GuiRender extends HudRenderEvent {
      public GuiRender(float mouseX, float mouseY) {
         super(mouseX, mouseY, null);
      }
   }

   public static class HotbarRender extends HudRenderEvent {
      public HotbarRender(float mouseX, float mouseY, GuiGraphicsExtractor graphics) {
         super(mouseX, mouseY, graphics);
      }
   }

   public static class CrosshairRender extends HudRenderEvent {
      public CrosshairRender(float mouseX, float mouseY, GuiGraphicsExtractor graphics) {
         super(mouseX, mouseY, graphics);
      }
   }
}
