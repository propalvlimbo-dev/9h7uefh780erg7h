package dev.rose.render;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;


public final class Images {
   private static final float[] RADII = new float[4];
   private static final float[] SHAPES = new float[4];

   private Images() {
   }

   public static void draw(
      GuiGraphics g, AbstractTexture texture, float u0, float v0, float u1, float v1, float x, float y, float w, float h, Corners corners, float alpha
   ) {
      draw(g, texture.getTextureView(), texture.getSampler(), u0, v0, u1, v1, x, y, w, h, corners, alpha);
   }

   public static void draw(
      GuiGraphics g,
      GpuTextureView view,
      GpuSampler sampler,
      float u0,
      float v0,
      float u1,
      float v1,
      float x,
      float y,
      float w,
      float h,
      Corners corners,
      float alpha
   ) {
      RoseShader shader = Shaders.roundedImage();
      if (!(w <= 0.0F) && !(h <= 0.0F) && !(alpha <= 0.001F)) {
         corners.writeRadii(RADII, 0);
         corners.writeShapes(SHAPES, 0);
         shader.safeGetUniform("Size").set(w, h);
         shader.safeGetUniform("Radius").set(RADII[0], RADII[1], RADII[2], RADII[3]);
         shader.safeGetUniform("Corner").set(SHAPES[0], SHAPES[1], SHAPES[2], SHAPES[3]);
         shader.safeGetUniform("UvRect").set(u0, v0, u1, v1);
         shader.setSampler("Sampler0", view, sampler);
         int color = ColorUtil.fade(-1, alpha);
         Matrix4f pose = GuiDraw.pose(g);
         BufferBuilder buffer = GuiDraw.begin(shader);
         buffer.addVertex(pose, x, y, 0.0F).setUv(0.0F, 0.0F).setColor(color);
         buffer.addVertex(pose, x, y + h, 0.0F).setUv(0.0F, h).setColor(color);
         buffer.addVertex(pose, x + w, y + h, 0.0F).setUv(w, h).setColor(color);
         buffer.addVertex(pose, x + w, y, 0.0F).setUv(w, 0.0F).setColor(color);
         GuiDraw.submit(g, shader, Blend.NORMAL, buffer);
      }
   }

   public static void playerFace(GuiGraphics g, Identifier skin, float x, float y, float size, Corners corners, float alpha) {
      AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(skin);
      draw(g, texture, 0.125F, 0.125F, 0.25F, 0.25F, x, y, size, size, corners, alpha);
      draw(g, texture, 0.625F, 0.125F, 0.75F, 0.25F, x, y, size, size, corners, alpha);
   }
}
