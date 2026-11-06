package dev.rose.render.gpu;

import com.mojang.blaze3d.buffers.GpuBuffer.MappedView;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.RoseClient;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;


public final class GuiDraw {
   private static final Matrix4f GUI_VIEW = new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F);
   private static final Vector4f WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
   private static final Vector3f ZERO = new Vector3f();
   private static final Matrix4f IDENTITY = new Matrix4f();
   public static final RenderPipeline MARKER = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.fromNamespaceAndPath("rose", "pipeline/gui_marker"))
      .withVertexShader("core/position")
      .withFragmentShader("core/position")
      .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
      .build();
   private static final Int2ObjectSortedMap<List<GuiDraw.Element>> runs = new Int2ObjectAVLTreeMap();
   private static List<GuiDraw.Element> pending = new ArrayList<>();
   private static final Map<VertexFormat, MappableRingBuffer> vertexBuffers = new HashMap<>();
   private static final Map<VertexFormat, MappableRingBuffer> usedBuffers = new IdentityHashMap<>();
   private static boolean uploaded;
   private static boolean failed;

   private GuiDraw() {
   }

   public static Matrix4f pose(GuiGraphics g) {
      return pose(g.pose());
   }

   public static Matrix4f pose(Matrix3x2fc m) {
      return new Matrix4f(m.m00(), m.m01(), 0.0F, 0.0F, m.m10(), m.m11(), 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, m.m20(), m.m21(), 0.0F, 1.0F);
   }

   public static void scissor(GuiGraphics g, int x0, int y0, int x1, int y1) {
      g.pose().pushMatrix();
      g.pose().identity();
      g.enableScissor(x0, y0, x1, y1);
      g.pose().popMatrix();
   }

   public static BufferBuilder begin(RoseShader shader) {
      return Tesselator.getInstance().begin(shader.mode(), shader.format());
   }

   public static void submit(GuiGraphics g, RoseShader shader, Blend blend, BufferBuilder buffer) {
      submit(g, shader, blend, buffer, null);
   }

   public static void submit(GuiGraphics g, RoseShader shader, Blend blend, BufferBuilder buffer, @Nullable GuiDraw.Prepare prepare) {
      MeshData mesh = buffer.build();
      if (mesh != null) {
         MeshData var6 = mesh;

         try {
            submit(g, shader, blend, mesh, prepare);
         } catch (Throwable var10) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var6 != null) {
            var6.close();
         }
      }
   }

   public static void submit(GuiGraphics g, RoseShader shader, Blend blend, MeshData mesh, @Nullable GuiDraw.Prepare prepare) {
      MeshData.DrawState state = mesh.drawState();
      if (state.format() == shader.format() && state.mode() == shader.mode()) {
         ByteBuffer source = mesh.vertexBuffer();
         int vertexSize = state.format().getVertexSize();
         byte[] vertices = new byte[source.remaining()];
         source.get(source.position(), vertices);
         ByteBuffer read = ByteBuffer.wrap(vertices).order(ByteOrder.nativeOrder());
         float minX = Float.MAX_VALUE;
         float minY = Float.MAX_VALUE;
         float maxX = -Float.MAX_VALUE;
         float maxY = -Float.MAX_VALUE;

         for (int offset = 0; offset < vertices.length; offset += vertexSize) {
            float x = read.getFloat(offset);
            float y = read.getFloat(offset + 4);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
         }

         if (maxX > minX && maxY > minY) {
            int left = (int)Math.floor(minX);
            int top = (int)Math.floor(minY);
            ScreenRectangle bounds = new ScreenRectangle(left, top, Math.max(1, (int)Math.ceil(maxX) - left), Math.max(1, (int)Math.ceil(maxY) - top));
            ScreenRectangle scissor = g.scissorStack.peek();
            if (scissor != null) {
               bounds = scissor.intersection(bounds);
               if (bounds == null) {
                  return;
               }
            }

            g.guiRenderState
               .submitGuiElement(
                  new GuiDraw.Element(
                     shader, blend, vertices, state.vertexCount(), state.indexCount(), shader.snapshotValues(), shader.snapshotTextures(), scissor, bounds, prepare
                  )
               );
         }
      } else {
         throw new IllegalArgumentException("Mesh " + state.format() + "/" + state.mode() + " does not match shader " + shader.name());
      }
   }

   public static void onRecordMesh(RenderPipeline pipeline, int drawIndex) {
      if (pipeline == MARKER && !pending.isEmpty()) {
         runs.put(drawIndex, pending);
         pending = new ArrayList<>();
      }
   }

   @Nullable
   public static List<GuiDraw.Element> runAt(int drawIndex) {
      return runs.isEmpty() ? null : (List)runs.get(drawIndex);
   }

   public static boolean hasRuns() {
      return !runs.isEmpty();
   }

   public static void onGuiRendered() {
      runs.clear();
      pending.clear();
      uploaded = false;
   }

   public static void endFrame() {
      for (MappableRingBuffer buffer : usedBuffers.values()) {
         buffer.rotate();
      }

      usedBuffers.clear();
   }

   public static void execute(List<GuiDraw.Element> run, RenderTarget target) {
      if (!failed) {
         try {
            uploadFrame();
            draw(run, target);
         } catch (RuntimeException e) {
            failed = true;
            RoseClient.LOGGER.error("[Rose] GUI drawing disabled", e);
         }
      }
   }

   private static void uploadFrame() {
      if (!uploaded) {
         uploaded = true;
         Object2IntOpenHashMap<VertexFormat> sizes = new Object2IntOpenHashMap();
         Object2IntOpenHashMap<VertexFormat.Mode> indices = new Object2IntOpenHashMap();
         Iterator encoder = runs.values().iterator();

         while (encoder.hasNext()) {
            List<GuiDraw.Element> run = (List<GuiDraw.Element>)encoder.next();

            for (GuiDraw.Element e : run) {
               sizes.addTo(e.shader.format(), e.vertices.length);
               indices.addTo(e.shader.mode(), e.indexCount);
            }
         }

         CommandEncoder encoderx = RenderSystem.getDevice().createCommandEncoder();
         Iterator var18 = sizes.object2IntEntrySet().iterator();

         while (var18.hasNext()) {
            Entry<VertexFormat> entry = (Entry<VertexFormat>)var18.next();
            VertexFormat format = (VertexFormat)entry.getKey();
            int size = entry.getIntValue();
            MappableRingBuffer buffer = vertexBuffers.get(format);
            if (buffer == null || buffer.size() < size) {
               if (buffer != null) {
                  buffer.close();
               }

               int capacity = Math.max(65536, Integer.highestOneBit(size - 1) << 1);
               buffer = new MappableRingBuffer(() -> "Rose GUI vertices " + format, 34, capacity);
               vertexBuffers.put(format, buffer);
            }

            usedBuffers.put(format, buffer);
            MappedView view = encoderx.mapBuffer(buffer.currentBuffer().slice(0L, size), false, true);

            try {
               ByteBuffer data = view.data();
               int offset = 0;
               Iterator var11 = runs.values().iterator();

               while (var11.hasNext()) {
                  List<GuiDraw.Element> run = (List<GuiDraw.Element>)var11.next();

                  for (GuiDraw.Element e : run) {
                     if (e.shader.format() == format) {
                        data.put(offset, e.vertices);
                        e.baseVertex = offset / format.getVertexSize();
                        offset += e.vertices.length;
                     }
                  }
               }
            } catch (Throwable var16) {
               if (view != null) {
                  try {
                     view.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }
               }

               throw var16;
            }

            if (view != null) {
               view.close();
            }
         }

         var18 = indices.object2IntEntrySet().iterator();

         while (var18.hasNext()) {
            Entry<VertexFormat.Mode> entry = (Entry<VertexFormat.Mode>)var18.next();
            if (entry.getIntValue() > 0) {
               RenderSystem.getSequentialBuffer((VertexFormat.Mode)entry.getKey()).getBuffer(entry.getIntValue());
            }
         }
      }
   }

   private static void draw(List<GuiDraw.Element> run, RenderTarget target) {
      CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
      GpuTextureView color = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : target.getColorTextureView();
      int i = 0;
      int n = run.size();

      while (i < n) {
         GuiDraw.Element head = run.get(i);
         if (head.prepare != null) {
            head.prepare.run(head);
         }

         int end = i + 1;

         while (end < n && run.get(end).prepare == null) {
            end++;
         }

         boolean any = false;

         for (int k = i; k < end; k++) {
            GuiDraw.Element e = run.get(k);
            if (!e.skipped && valid(e)) {
               e.params = e.shader.upload(e.values);
               any = true;
            } else {
               e.skipped = true;
            }
         }

         if (any) {
            GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(GUI_VIEW, WHITE, ZERO, IDENTITY);
            RenderPass pass = encoder.createRenderPass(() -> "Rose GUI", color, OptionalInt.empty());

            try {
               RenderSystem.bindDefaultUniforms(pass);
               pass.setUniform("DynamicTransforms", transforms);
               int k = i;

               while (k < end) {
                  GuiDraw.Element first = run.get(k);
                  if (first.skipped) {
                     k++;
                  } else {
                     int indexCount = first.indexCount;

                     int m;
                     for (m = k + 1; m < end && mergeable(run.get(m - 1), run.get(m)); m++) {
                        indexCount += run.get(m).indexCount;
                     }

                     drawGroup(pass, first, indexCount);
                     k = m;
                  }
               }
            } catch (Throwable var16) {
               if (pass != null) {
                  try {
                     pass.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }
               }

               throw var16;
            }

            if (pass != null) {
               pass.close();
            }
         }

         i = end;
      }
   }

   private static boolean mergeable(GuiDraw.Element a, GuiDraw.Element b) {
      VertexFormat.Mode mode = a.shader.mode();
      return !b.skipped
         && !a.skipped
         && a.shader == b.shader
         && a.blend == b.blend
         && (mode == VertexFormat.Mode.QUADS || mode == VertexFormat.Mode.TRIANGLES)
         && b.baseVertex == a.baseVertex + a.vertexCount
         && Objects.equals(a.scissor, b.scissor)
         && Objects.equals(a.params, b.params)
         && a.textures.equals(b.textures);
   }

   private static void drawGroup(RenderPass pass, GuiDraw.Element e, int indexCount) {
      pass.setPipeline(e.shader.pipeline(e.blend));
      if (e.params != null) {
         pass.setUniform("RoseParams", e.params);
      }

      for (Map.Entry<String, RoseShader.Texture> texture : e.textures.entrySet()) {
         pass.bindTexture(texture.getKey(), texture.getValue().view(), texture.getValue().sampler());
      }

      if (e.scissor != null) {
         enableScissor(e.scissor, pass);
      } else {
         pass.disableScissor();
      }

      pass.setVertexBuffer(0, vertexBuffers.get(e.shader.format()).currentBuffer());
      RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(e.shader.mode());
      pass.setIndexBuffer(indices.getBuffer(indexCount), indices.type());
      pass.drawIndexed(e.baseVertex, 0, indexCount, 1);
   }

   private static boolean valid(GuiDraw.Element e) {
      return e.shader.ready(e.blend) && e.textures.keySet().containsAll(e.shader.samplers());
   }

   static void enableScissor(ScreenRectangle rectangle, RenderPass pass) {
      Window window = Minecraft.getInstance().getWindow();
      int scale = window.getGuiScale();
      int x = rectangle.left() * scale;
      int y = window.getHeight() - rectangle.bottom() * scale;
      pass.enableScissor(x, y, Math.max(0, rectangle.width() * scale), Math.max(0, rectangle.height() * scale));
   }

   public static final class Element implements GuiElementRenderState {
      final RoseShader shader;
      final Blend blend;
      final byte[] vertices;
      final int vertexCount;
      final int indexCount;
      final ByteBuffer values;
      final Map<String, RoseShader.Texture> textures;
      @Nullable
      final ScreenRectangle scissor;
      final ScreenRectangle bounds;
      @Nullable
      final GuiDraw.Prepare prepare;
      int baseVertex;
      boolean skipped;
      @Nullable
      GpuBufferSlice params;

      Element(
         RoseShader shader,
         Blend blend,
         byte[] vertices,
         int vertexCount,
         int indexCount,
         ByteBuffer values,
         Map<String, RoseShader.Texture> textures,
         @Nullable ScreenRectangle scissor,
         ScreenRectangle bounds,
         @Nullable GuiDraw.Prepare prepare
      ) {
         this.shader = shader;
         this.blend = blend;
         this.vertices = vertices;
         this.vertexCount = vertexCount;
         this.indexCount = indexCount;
         this.values = values;
         this.textures = prepare == null ? textures : new LinkedHashMap<>(textures);
         this.scissor = scissor;
         this.bounds = bounds;
         this.prepare = prepare;
      }

      public void texture(String name, GpuTextureView view, GpuSampler sampler) {
         this.textures.put(name, new RoseShader.Texture(view, sampler));
      }

      public void skip() {
         this.skipped = true;
      }

      public ScreenRectangle area() {
         return this.bounds;
      }

      public void buildVertices(VertexConsumer consumer) {
         GuiDraw.pending.add(this);

         for (int i = 0; i < 4; i++) {
            consumer.addVertex(0.0F, 0.0F, 0.0F);
         }
      }

      public RenderPipeline pipeline() {
         return GuiDraw.MARKER;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.noTexture();
      }

      @Nullable
      public ScreenRectangle scissorArea() {
         return this.scissor;
      }

      public ScreenRectangle bounds() {
         return this.bounds;
      }
   }

   @FunctionalInterface
   public interface Prepare {
      void run(GuiDraw.Element var1);
   }
}
