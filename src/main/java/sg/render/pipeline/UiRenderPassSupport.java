package sg.render.pipeline;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import sg.ec.TransientGpuUpload;
import sg.util.time.ColorMath;

public interface UiRenderPassSupport {
   default GpuBufferSlice uploadCornerColors(MemoryStack stack, Vector4i colors) {
      Vector4f firstColor = ColorMath.toRgbaVector(colors.x);
      Vector4f secondColor = ColorMath.toRgbaVector(colors.y);
      Vector4f thirdColor = ColorMath.toRgbaVector(colors.z);
      Vector4f fourthColor = ColorMath.toRgbaVector(colors.w);
      firstColor.w = firstColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      secondColor.w = secondColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      thirdColor.w = thirdColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      fourthColor.w = fourthColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      ByteBuffer colorData = Std140Builder.onStack(stack, 64).putVec4(firstColor).putVec4(secondColor).putVec4(thirdColor).putVec4(fourthColor).get();
      return TransientGpuUpload.uploadUniform(colorData);
   }

   default boolean isOutsideClip(float x, float y, float width, float height) {
      if (!UiDrawing.isScissorEnabled()) {
         return false;
      }

      float clipX = UiDrawing.getScissor().x;
      float clipY = UiDrawing.getScissor().y;
      float clipHeight = UiDrawing.getScissor().w;
      float clipWidth = UiDrawing.getScissor().z;
      boolean overlapsVertically = y + height > clipY && y < clipY + clipHeight;
      boolean overlapsHorizontally = x + width > clipX && x < clipX + clipWidth;
      return !overlapsVertically || !overlapsHorizontally;
   }

   default GpuBufferSlice uploadClipUniform(Vector4f bounds, MemoryStack stack) {
      float boundsLeft = bounds.x;
      float boundsTop = bounds.y;
      float boundsRight = bounds.x + bounds.z;
      float boundsBottom = bounds.y + bounds.w;
      Vector4f clipRect = UiDrawing.getScissor();
      float clipLeft = clipRect.x;
      float clipTop = clipRect.y;
      float clipRight = clipRect.x + clipRect.z;
      float clipBottom = clipRect.y + clipRect.w;
      float clippedLeft = Math.max(boundsLeft, clipLeft);
      float clippedTop = Math.max(boundsTop, clipTop);
      float clippedRight = Math.min(boundsRight, clipRight);
      float clippedBottom = Math.min(boundsBottom, clipBottom);
      float normalizedX = bounds.z != 0.0F ? (clippedLeft - boundsLeft) / bounds.z : 0.0F;
      float normalizedY = bounds.w != 0.0F ? (clippedTop - boundsTop) / bounds.w : 0.0F;
      float normalizedWidth = bounds.z != 0.0F ? (clippedRight - clippedLeft) / bounds.z : 0.0F;
      float normalizedHeight = bounds.w != 0.0F ? (clippedBottom - clippedTop) / bounds.w : 0.0F;
      normalizedY = 1.0F - (normalizedY + normalizedHeight);
      ByteBuffer clipData = Std140Builder.onStack(stack, 32).putVec4(new Vector4f(normalizedY, normalizedX, normalizedHeight, normalizedWidth)).putFloat(UiDrawing.isScissorEnabled() ? 1.0F : 0.0F).get();
      return TransientGpuUpload.uploadUniform(clipData);
   }

   default Vector4f getNormalizedClipRect(Vector4f bounds) {
      float boundsLeft = bounds.x;
      float boundsTop = bounds.y;
      float boundsRight = bounds.x + bounds.z;
      float boundsBottom = bounds.y + bounds.w;
      Vector4f clipRect = UiDrawing.getScissor();
      float clipLeft = clipRect.x;
      float clipTop = clipRect.y;
      float clipRight = clipRect.x + clipRect.z;
      float clipBottom = clipRect.y + clipRect.w;
      float clippedLeft = Math.max(boundsLeft, clipLeft);
      float clippedTop = Math.max(boundsTop, clipTop);
      float clippedRight = Math.min(boundsRight, clipRight);
      float clippedBottom = Math.min(boundsBottom, clipBottom);
      float normalizedX = bounds.z != 0.0F ? (clippedLeft - boundsLeft) / bounds.z : 0.0F;
      float normalizedY = bounds.w != 0.0F ? (clippedTop - boundsTop) / bounds.w : 0.0F;
      float normalizedWidth = bounds.z != 0.0F ? (clippedRight - clippedLeft) / bounds.z : 0.0F;
      float normalizedHeight = bounds.w != 0.0F ? (clippedBottom - clippedTop) / bounds.w : 0.0F;
      normalizedY = 1.0F - (normalizedY + normalizedHeight);
      return new Vector4f(normalizedY, normalizedX, normalizedHeight, normalizedWidth);
   }

   default boolean isFullyTransparent(Vector4i colors) {
      Vector4f firstColor = ColorMath.toRgbaVector(colors.x);
      Vector4f secondColor = ColorMath.toRgbaVector(colors.y);
      Vector4f thirdColor = ColorMath.toRgbaVector(colors.z);
      Vector4f fourthColor = ColorMath.toRgbaVector(colors.w);
      firstColor.w = firstColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      secondColor.w = secondColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      thirdColor.w = thirdColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      fourthColor.w = fourthColor.w * (UiDrawing.getGlobalAlpha() / 255.0F);
      return firstColor.w == 0.0F && secondColor.w == 0.0F && thirdColor.w == 0.0F && fourthColor.w == 0.0F;
   }

   default void drawAndClose(BufferBuilder buffer, RenderPass renderPass, RenderPipeline pipeline) {
      try (
         renderPass;
         MeshData mesh = buffer.build();
      ) {
         if (mesh != null) {
            DrawState drawState = mesh.drawState();
            int indexCount = drawState.indexCount();
            AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
            GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
            IndexType indexType = sequentialIndices.type();
            GpuBufferSlice vertexBuffer = TransientGpuUpload.uploadVertices(mesh.vertexBuffer(), drawState.format().getVertexSize());
            renderPass.setPipeline(pipeline);
            renderPass.setVertexBuffer(0, vertexBuffer);
            renderPass.setIndexBuffer(indexBuffer, indexType);
            renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
         }
      }
   }

   default void appendQuad(BufferBuilder buffer, float x, float y, float width, float height) {
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
      float ndcLeft = -1.0F + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      buffer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F);
      buffer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F);
      buffer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F);
      buffer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F);
   }
}
