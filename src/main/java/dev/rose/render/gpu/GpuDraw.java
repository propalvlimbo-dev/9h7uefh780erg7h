package dev.rose.render.gpu;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.Map.Entry;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;


public final class GpuDraw {
   private static final Vector4f WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
   private static final Vector3f ZERO = new Vector3f();
   private static final Matrix4f IDENTITY = new Matrix4f();
   @Nullable
   private static GpuBuffer screenQuad;

   private GpuDraw() {
   }

   public static BufferBuilder begin(RoseShader shader) {
      return Tesselator.getInstance().begin(shader.mode(), shader.format());
   }

   public static boolean draw(
      RoseShader shader, Blend blend, RoseShader.Depth depth, BufferBuilder buffer, GpuTextureView color, @Nullable GpuTextureView depthView, Matrix4fc modelView
   ) {
      MeshData mesh = buffer.build();
      if (mesh == null) {
         return false;
      }

      MeshData var8 = mesh;

      boolean var9;
      try {
         var9 = draw(shader, blend, depth, mesh, color, depthView, modelView);
      } catch (Throwable var12) {
         if (var8 != null) {
            try {
               var8.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }
         }

         throw var12;
      }

      if (var8 != null) {
         var8.close();
      }

      return var9;
   }

   public static boolean draw(
      RoseShader shader, Blend blend, RoseShader.Depth depth, MeshData mesh, GpuTextureView color, @Nullable GpuTextureView depthView, Matrix4fc modelView
   ) {
      if (shader.ready(blend, depth) && hasTextures(shader)) {
         MeshData.DrawState state = mesh.drawState();
         GpuBufferSlice params = shader.upload(shader.snapshotValues());
         GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(modelView, WHITE, ZERO, IDENTITY);
         GpuBuffer vertices = state.format().uploadImmediateVertexBuffer(mesh.vertexBuffer());
         GpuBuffer indices;
         VertexFormat.IndexType indexType;
         if (mesh.indexBuffer() == null) {
            RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(state.mode());
            indices = sequential.getBuffer(state.indexCount());
            indexType = sequential.type();
         } else {
            indices = state.format().uploadImmediateIndexBuffer(mesh.indexBuffer());
            indexType = state.indexType();
         }

         RenderPass pass = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(
               () -> "Rose " + shader.name(), color, OptionalInt.empty(), depth == RoseShader.Depth.NONE ? null : depthView, OptionalDouble.empty()
            );

         try {
            pass.setPipeline(shader.pipeline(blend, depth));
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", transforms);
            if (params != null) {
               pass.setUniform("RoseParams", params);
            }

            bindTextures(pass, shader);
            pass.setVertexBuffer(0, vertices);
            pass.setIndexBuffer(indices, indexType);
            pass.drawIndexed(0, 0, state.indexCount(), 1);
         } catch (Throwable var17) {
            if (pass != null) {
               try {
                  pass.close();
               } catch (Throwable var16) {
                  var17.addSuppressed(var16);
               }
            }

            throw var17;
         }

         if (pass != null) {
            pass.close();
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean screen(RoseShader shader, Blend blend, GpuTextureView target, boolean clear) {
      if (shader.ready(blend) && hasTextures(shader)) {
         GpuBufferSlice params = shader.upload(shader.snapshotValues());
         RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
         GpuBuffer indices = sequential.getBuffer(6);
         RenderPass pass = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Rose " + shader.name(), target, clear ? OptionalInt.of(0) : OptionalInt.empty());

         try {
            pass.setPipeline(shader.pipeline(blend));
            if (params != null) {
               pass.setUniform("RoseParams", params);
            }

            bindTextures(pass, shader);
            pass.setVertexBuffer(0, screenQuad());
            pass.setIndexBuffer(indices, sequential.type());
            pass.drawIndexed(0, 0, 6, 1);
         } catch (Throwable var11) {
            if (pass != null) {
               try {
                  pass.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (pass != null) {
            pass.close();
         }

         return true;
      } else {
         return false;
      }
   }

   private static GpuBuffer screenQuad() {
      if (screenQuad == null || screenQuad.isClosed()) {
         MemoryStack stack = MemoryStack.stackPush();

         try {
            ByteBuffer data = stack.malloc(48);
            float[] corners = new float[]{0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F};

            for (int i = 0; i < 4; i++) {
               data.putFloat(corners[i * 2]).putFloat(corners[i * 2 + 1]).putFloat(0.0F);
            }

            data.flip();
            screenQuad = RenderSystem.getDevice().createBuffer(() -> "Rose screen quad", 32, data);
         } catch (Throwable var5) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (stack != null) {
            stack.close();
         }
      }

      return screenQuad;
   }

   private static boolean hasTextures(RoseShader shader) {
      Map<String, RoseShader.Texture> textures = shader.currentTextures();

      for (String sampler : shader.samplers()) {
         if (!textures.containsKey(sampler)) {
            return false;
         }
      }

      return true;
   }

   private static void bindTextures(RenderPass pass, RoseShader shader) {
      for (Map.Entry<String, RoseShader.Texture> texture : shader.currentTextures().entrySet()) {
         pass.bindTexture(texture.getKey(), texture.getValue().view(), texture.getValue().sampler());
      }
   }

   public static VertexFormat screenFormat() {
      return DefaultVertexFormat.POSITION;
   }
}
