package sg.ec;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;

public final class TransientGpuUpload {
   private static final long VERTEX_UPLOAD_ALIGNMENT = 4L;

   private TransientGpuUpload() {
   }

   public static GpuBufferSlice uploadVertices(ByteBuffer data, int vertexStride) {
      GpuDevice device = RenderSystem.getDevice();
      return device.createCommandEncoder().transientMemory().uploadStaging(data, VERTEX_UPLOAD_ALIGNMENT, 32, data.remaining(), vertexStride);
   }

   public static GpuBufferSlice uploadUniform(ByteBuffer data) {
      GpuDevice device = RenderSystem.getDevice();
      long uniformAlignment = device.getDeviceInfo().limits().minUniformOffsetAlignment();
      return device.createCommandEncoder().transientMemory().uploadStaging(data, uniformAlignment, 128);
   }
}
