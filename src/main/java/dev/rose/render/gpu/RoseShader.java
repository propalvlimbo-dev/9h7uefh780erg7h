package dev.rose.render.gpu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.RoseClient;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;


public final class RoseShader {
   private static final List<RoseShader> ALL = new ArrayList<>();
   private final String name;
   private final Identifier vertex;
   private final Identifier fragment;
   private final VertexFormat format;
   private final VertexFormat.Mode mode;
   private final List<String> samplers;
   private final boolean screenSpace;
   private final UniformBlock block;
   private final ByteBuffer values;
   private final Map<String, RoseShader.Texture> textures = new LinkedHashMap<>();
   private final RenderPipeline[] pipelines = new RenderPipeline[Blend.values().length * RoseShader.Depth.values().length];
   @Nullable
   private DynamicUniformStorage<RoseShader.Values> storage;

   private RoseShader(String namespace, String name, String vertex, String fragment, VertexFormat format, VertexFormat.Mode mode, List<String> samplers) {
      this.name = name;
      this.vertex = Identifier.fromNamespaceAndPath(namespace, "core/" + vertex);
      this.fragment = Identifier.fromNamespaceAndPath(namespace, "core/" + fragment);
      this.format = format;
      this.mode = mode;
      this.samplers = List.copyOf(samplers);
      this.screenSpace = vertex.equals("blit");
      UniformBlock parsed = UniformBlock.parse(read("core/" + fragment + ".fsh"));
      if (parsed.size() == 0) {
         parsed = UniformBlock.parse(read("core/" + vertex + ".vsh"));
      }

      this.block = parsed;
      this.values = this.block.newValues();
   }

   public static RoseShader create(String name, String vertex, String fragment, VertexFormat format, VertexFormat.Mode mode, String... samplers) {
      return register(new RoseShader("rose", name, vertex, fragment, format, mode, List.of(samplers)));
   }

   public static RoseShader vanilla(String name, String shader, VertexFormat format, VertexFormat.Mode mode, String... samplers) {
      return register(new RoseShader("minecraft", name, shader, shader, format, mode, List.of(samplers)));
   }

   private static RoseShader register(RoseShader shader) {
      synchronized (ALL) {
         ALL.add(shader);
         return shader;
      }
   }

   public static RoseShader create(String name, VertexFormat format, VertexFormat.Mode mode) {
      JsonObject json = JsonParser.parseString(read("core/" + name + ".json")).getAsJsonObject();
      List<String> samplers = new ArrayList<>();
      if (json.has("samplers")) {
         for (JsonElement sampler : json.getAsJsonArray("samplers")) {
            samplers.add(sampler.getAsJsonObject().get("name").getAsString());
         }
      }

      RoseShader shader = create(
         name, path(json.get("vertex").getAsString()), path(json.get("fragment").getAsString()), format, mode, samplers.toArray(String[]::new)
      );
      if (json.has("uniforms")) {
         for (JsonElement element : json.getAsJsonArray("uniforms")) {
            JsonObject uniform = element.getAsJsonObject();
            JsonArray values = uniform.getAsJsonArray("values");
            float[] numbers = new float[values.size()];

            for (int i = 0; i < numbers.length; i++) {
               numbers[i] = values.get(i).getAsFloat();
            }

            shader.safeGetUniform(uniform.get("name").getAsString()).set(numbers);
         }
      }

      return shader;
   }

   private static String path(String location) {
      int colon = location.indexOf(58);
      return colon < 0 ? location : location.substring(colon + 1);
   }

   private static Identifier id(String path) {
      return Identifier.fromNamespaceAndPath("rose", path);
   }

   private static String read(String path) {
      try (InputStream in = RoseShader.class.getResourceAsStream("/assets/rose/shaders/" + path)) {
         return in == null ? "" : new String(in.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
         RoseClient.LOGGER.error("[Rose] cannot read shader {}", path, e);
         return "";
      }
   }

   public String name() {
      return this.name;
   }

   public VertexFormat format() {
      return this.format;
   }

   public VertexFormat.Mode mode() {
      return this.mode;
   }

   public boolean screenSpace() {
      return this.screenSpace;
   }

   public List<String> samplers() {
      return this.samplers;
   }

   public boolean ready(Blend blend) {
      return this.ready(blend, RoseShader.Depth.NONE);
   }

   public boolean ready(Blend blend, RoseShader.Depth depth) {
      return RenderSystem.getDevice().precompilePipeline(this.pipeline(blend, depth)).isValid();
   }

   public RenderPipeline pipeline(Blend blend) {
      return this.pipeline(blend, RoseShader.Depth.NONE);
   }

   public RenderPipeline pipeline(Blend blend, RoseShader.Depth depth) {
      int index = blend.ordinal() * RoseShader.Depth.values().length + depth.ordinal();
      RenderPipeline pipeline = this.pipelines[index];
      if (pipeline == null) {
         this.pipelines[index] = pipeline = this.build(blend, depth);
      }

      return pipeline;
   }

   private RenderPipeline build(Blend blend, RoseShader.Depth depth) {
      Builder builder = this.screenSpace ? RenderPipeline.builder(new Snippet[0]) : RenderPipeline.builder(new Snippet[]{RenderPipelines.MATRICES_PROJECTION_SNIPPET});
      builder.withLocation(
            id(
               "pipeline/"
                  + this.name
                  + "/"
                  + blend.name().toLowerCase(Locale.ROOT)
                  + (depth == RoseShader.Depth.NONE ? "" : "_" + depth.name().toLowerCase(Locale.ROOT))
            )
         )
         .withVertexShader(this.vertex)
         .withFragmentShader(this.fragment)
         .withVertexFormat(this.format, this.mode)
         .withDepthTestFunction(depth == RoseShader.Depth.NONE ? DepthTestFunction.NO_DEPTH_TEST : DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(depth == RoseShader.Depth.TEST_AND_WRITE)
         .withCull(false);
      if (this.block.size() > 0) {
         builder.withUniform("RoseParams", UniformType.UNIFORM_BUFFER);
      }

      for (String sampler : this.samplers) {
         builder.withSampler(sampler);
      }

      if (blend.function() != null) {
         builder.withBlend(blend.function());
      } else {
         builder.withoutBlend();
      }

      return builder.build();
   }

   public RoseShader.Uniform safeGetUniform(String name) {
      UniformBlock.Field field = this.block.field(name);
      return field == null ? RoseShader.Uniform.DUMMY : new RoseShader.Uniform(this.values, field);
   }

   public void setSampler(String name, GpuTextureView view, GpuSampler sampler) {
      this.textures.put(name, new RoseShader.Texture(view, sampler));
   }

   public void setSampler(String name, AbstractTexture texture) {
      this.textures.put(name, new RoseShader.Texture(texture.getTextureView(), texture.getSampler()));
   }

   public void clearSamplers() {
      this.textures.clear();
   }

   public ByteBuffer snapshotValues() {
      ByteBuffer copy = this.block.newValues();
      copy.put(0, this.values, 0, this.values.capacity());
      return copy;
   }

   public Map<String, RoseShader.Texture> snapshotTextures() {
      return Map.copyOf(this.textures);
   }

   Map<String, RoseShader.Texture> currentTextures() {
      return this.textures;
   }

   public int blockSize() {
      return this.block.size();
   }

   @Nullable
   GpuBufferSlice upload(ByteBuffer snapshot) {
      if (this.block.size() == 0) {
         return null;
      }

      if (this.storage == null) {
         this.storage = new DynamicUniformStorage("Rose " + this.name, this.block.size(), 32);
      }

      return this.storage.writeUniform(new RoseShader.Values(snapshot));
   }

   public static void endFrame() {
      synchronized (ALL) {
         for (RoseShader shader : ALL) {
            if (shader.storage != null) {
               shader.storage.endFrame();
            }
         }
      }
   }

   public enum Depth {
      NONE,
      TEST,
      TEST_AND_WRITE;
   }

   public record Texture(GpuTextureView view, GpuSampler sampler) {
   }

   public static class Uniform {
      static final RoseShader.Uniform DUMMY = new RoseShader.Uniform(null, null);
      @Nullable
      private final ByteBuffer values;
      private final @Nullable UniformBlock.Field field;

      Uniform(@Nullable ByteBuffer values, @Nullable UniformBlock.Field field) {
         this.values = values;
         this.field = field;
      }

      public void set(float x) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x);
         }
      }

      public void set(float x, float y) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x, y);
         }
      }

      public void set(float x, float y, float z) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x, y, z);
         }
      }

      public void set(float x, float y, float z, float w) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x, y, z, w);
         }
      }

      public void set(float[] numbers) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, numbers);
         }
      }

      public void set(int x) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x);
         }
      }

      public void set(int x, int y) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x, y);
         }
      }

      public void set(int x, int y, int z, int w) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, x, y, z, w);
         }
      }

      public void set(Matrix4fc matrix) {
         if (this.field != null) {
            UniformBlock.write(this.values, this.field, matrix);
         }
      }
   }

   record Values(ByteBuffer bytes) implements DynamicUniformStorage.DynamicUniform {
      public void write(ByteBuffer target) {
         ByteBuffer source = this.bytes.duplicate();
         source.position(0).limit(Math.min(source.capacity(), target.remaining()));
         target.put(source);
      }
   }
}
