package dev.rose.render.gpu;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;


public record QuadState(Matrix3x2f pose, float x0, float y0, float x1, float y1, int color, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds)
   implements GuiElementRenderState {
   public static void submit(GuiGraphics g, float x0, float y0, float x1, float y1, int color) {
      Matrix3x2f pose = new Matrix3x2f(g.pose());
      ScreenRectangle scissor = g.scissorStack.peek();
      int left = (int)Math.floor(x0);
      int top = (int)Math.floor(y0);
      ScreenRectangle rect = new ScreenRectangle(left, top, Math.max(1, (int)Math.ceil(x1) - left), Math.max(1, (int)Math.ceil(y1) - top)).transformMaxBounds(pose);
      ScreenRectangle bounds = scissor != null ? scissor.intersection(rect) : rect;
      if (bounds != null) {
         g.guiRenderState.submitGuiElement(new QuadState(pose, x0, y0, x1, y1, color, scissor, bounds));
      }
   }

   public void buildVertices(VertexConsumer consumer) {
      consumer.addVertexWith2DPose(this.pose, this.x0, this.y0).setColor(this.color);
      consumer.addVertexWith2DPose(this.pose, this.x0, this.y1).setColor(this.color);
      consumer.addVertexWith2DPose(this.pose, this.x1, this.y1).setColor(this.color);
      consumer.addVertexWith2DPose(this.pose, this.x1, this.y0).setColor(this.color);
   }

   public RenderPipeline pipeline() {
      return RenderPipelines.GUI;
   }

   public TextureSetup textureSetup() {
      return TextureSetup.noTexture();
   }

   @Nullable
   public ScreenRectangle scissorArea() {
      return this.scissorArea;
   }

   @Nullable
   public ScreenRectangle bounds() {
      return this.bounds;
   }
}
