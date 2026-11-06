package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;

public final class SharedGeometryBuffer implements AutoCloseable {
   private final @com.google.gson.annotations.SerializedName("В")  ByteBufferBuilder storage;

   public void clear() {
      this.storage.clear();
   }

   @Override
   public void close() {
      this.storage.close();
   }

   public static SharedGeometryBuffer getInstance() {
      return SharedGeometryBuffer.Holder.INSTANCE;
   }

   public SharedGeometryBuffer(int initialCapacity) {
      this.storage = new ByteBufferBuilder(initialCapacity);
   }

   public BufferBuilder begin(PrimitiveTopology topology, VertexFormat vertexFormat) {
      return new BufferBuilder(this.storage, topology, vertexFormat);
   }

   private static final class Holder {
      private static final SharedGeometryBuffer INSTANCE = new SharedGeometryBuffer(SharedGeometryBuffer.Holder.INITIAL_CAPACITY);
      private static final int INITIAL_CAPACITY = 786432;
   }
}
