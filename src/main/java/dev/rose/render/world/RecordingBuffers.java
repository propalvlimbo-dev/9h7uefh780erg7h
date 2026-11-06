package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;


public final class RecordingBuffers implements MultiBufferSource {
   private static final PoseStack IDENTITY = new PoseStack();
   private final Map<RenderType, RecordingBuffers.Recorder> recorders = new LinkedHashMap<>();

   public VertexConsumer getBuffer(RenderType type) {
      return this.recorders.computeIfAbsent(type, t -> new RecordingBuffers.Recorder());
   }

   public void submit(SubmitNodeCollector collector) {
      for (Map.Entry<RenderType, RecordingBuffers.Recorder> entry : this.recorders.entrySet()) {
         RecordingBuffers.Recorder recorder = entry.getValue();
         if (recorder.count != 0) {
            RecordingBuffers.Recorder snapshot = recorder.copy();
            collector.submitCustomGeometry(IDENTITY, entry.getKey(), (pose, consumer) -> snapshot.replay(consumer));
         }
      }

      this.recorders.clear();
   }

   private static final class Recorder implements VertexConsumer {
      private static final int COLOR = 1;
      private static final int UV = 2;
      private static final int UV1 = 4;
      private static final int UV2 = 8;
      private static final int NORMAL = 16;
      private static final int LINE = 32;
      private final FloatArrayList floats;
      private final IntArrayList ints;
      private int count;
      private static final int FLOATS = 9;
      private static final int INTS = 4;

      Recorder() {
         this(new FloatArrayList(), new IntArrayList(), 0);
      }

      private Recorder(FloatArrayList floats, IntArrayList ints, int count) {
         this.floats = floats;
         this.ints = ints;
         this.count = count;
      }

      RecordingBuffers.Recorder copy() {
         return new RecordingBuffers.Recorder(new FloatArrayList(this.floats), new IntArrayList(this.ints), this.count);
      }

      private int f(int field) {
         return (this.count - 1) * 9 + field;
      }

      private int i(int field) {
         return (this.count - 1) * 4 + field;
      }

      private void mark(int flag) {
         int at = this.i(3);
         this.ints.set(at, this.ints.getInt(at) | flag);
      }

      public VertexConsumer addVertex(float x, float y, float z) {
         this.count++;
         this.floats.add(x);
         this.floats.add(y);
         this.floats.add(z);

         for (int k = 3; k < 9; k++) {
            this.floats.add(0.0F);
         }

         for (int k = 0; k < 4; k++) {
            this.ints.add(0);
         }

         return this;
      }

      public VertexConsumer setColor(int r, int g, int b, int a) {
         return this.setColor(a << 24 | r << 16 | g << 8 | b);
      }

      public VertexConsumer setColor(int argb) {
         if (this.count == 0) {
            return this;
         }

         this.ints.set(this.i(0), argb);
         this.mark(1);
         return this;
      }

      public VertexConsumer setUv(float u, float v) {
         if (this.count == 0) {
            return this;
         }

         this.floats.set(this.f(3), u);
         this.floats.set(this.f(4), v);
         this.mark(2);
         return this;
      }

      public VertexConsumer setUv1(int u, int v) {
         if (this.count == 0) {
            return this;
         }

         this.ints.set(this.i(1), v << 16 | u & 65535);
         this.mark(4);
         return this;
      }

      public VertexConsumer setUv2(int u, int v) {
         if (this.count == 0) {
            return this;
         }

         this.ints.set(this.i(2), v << 16 | u & 65535);
         this.mark(8);
         return this;
      }

      public VertexConsumer setNormal(float x, float y, float z) {
         if (this.count == 0) {
            return this;
         }

         this.floats.set(this.f(5), x);
         this.floats.set(this.f(6), y);
         this.floats.set(this.f(7), z);
         this.mark(16);
         return this;
      }

      public VertexConsumer setLineWidth(float width) {
         if (this.count == 0) {
            return this;
         }

         this.floats.set(this.f(8), width);
         this.mark(32);
         return this;
      }

      void replay(VertexConsumer out) {
         for (int n = 0; n < this.count; n++) {
            int fo = n * 9;
            int io = n * 4;
            int flags = this.ints.getInt(io + 3);
            out.addVertex(this.floats.getFloat(fo), this.floats.getFloat(fo + 1), this.floats.getFloat(fo + 2));
            if ((flags & 1) != 0) {
               out.setColor(this.ints.getInt(io));
            }

            if ((flags & 2) != 0) {
               out.setUv(this.floats.getFloat(fo + 3), this.floats.getFloat(fo + 4));
            }

            if ((flags & 4) != 0) {
               int packed = this.ints.getInt(io + 1);
               out.setUv1(packed & 65535, packed >>> 16);
            }

            if ((flags & 8) != 0) {
               int packed = this.ints.getInt(io + 2);
               out.setUv2(packed & 65535, packed >>> 16);
            }

            if ((flags & 16) != 0) {
               out.setNormal(this.floats.getFloat(fo + 5), this.floats.getFloat(fo + 6), this.floats.getFloat(fo + 7));
            }

            if ((flags & 32) != 0) {
               out.setLineWidth(this.floats.getFloat(fo + 8));
            }
         }
      }
   }
}
