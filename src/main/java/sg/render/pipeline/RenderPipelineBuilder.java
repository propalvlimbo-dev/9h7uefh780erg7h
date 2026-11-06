package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.resources.Identifier;

public final class RenderPipelineBuilder {
   public static final VertexFormat EMPTY_VERTEX_FORMAT = VertexFormat.builder(0).build();
   public static final Snippet PROJECTION_SNIPPET = RenderPipeline.builder().withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION).buildSnippet();
   @SerializedName("В")
   private final Builder pipelineBuilder;
   @SerializedName("В")
   private final com.mojang.blaze3d.pipeline.BindGroupLayout.Builder bindGroupLayoutBuilder = BindGroupLayout.builder();
   private final @com.google.gson.annotations.SerializedName("б")  Set<String> bindingNames = new HashSet<>();
   private @com.google.gson.annotations.SerializedName("ЯЧ")  boolean bindGroupLayoutDirty;
   @SerializedName("В")
   private CompareOp depthCompare = CompareOp.GREATER_THAN_OR_EQUAL;
   private @com.google.gson.annotations.SerializedName("ЯЛ")  boolean writeDepth = true;
   private @com.google.gson.annotations.SerializedName("Я诶")  boolean depthStateDirty;
   private @com.google.gson.annotations.SerializedName("АМ")  float depthBiasScaleFactor;
   private @com.google.gson.annotations.SerializedName("АН")  float depthBiasConstant;
   @SerializedName("В")
   private Optional<BlendFunction> blendFunction = Optional.empty();
   private @com.google.gson.annotations.SerializedName("ел")  int colorWriteMask = 15;
   private @com.google.gson.annotations.SerializedName("Я1")  boolean colorTargetStateDirty;
   private @com.google.gson.annotations.SerializedName("Я")  GpuFormat colorTargetFormat = GpuFormat.RGBA8_UNORM;

   public RenderPipelineBuilder withColorTargetFormat(GpuFormat format) {
      this.colorTargetFormat = format;
      this.colorTargetStateDirty = true;
      return this;
   }

   public RenderPipelineBuilder withPolygonMode(PolygonMode polygonMode) {
      this.pipelineBuilder.withPolygonMode(polygonMode);
      return this;
   }

   private RenderPipelineBuilder(Snippet... snippets) {
      this.pipelineBuilder = RenderPipeline.builder(snippets);

      for (Snippet snippet : snippets) {
         snippet.bindGroupLayouts().ifPresent(layouts -> layouts.forEach(layout -> {
            this.bindingNames.addAll(layout.getSamplers());
            layout.getUniforms().forEach(uniform -> this.bindingNames.add(uniform.name()));
         }));
         snippet.depthStencilState().ifPresent(depthState -> {
            this.depthCompare = depthState.depthTest();
            this.writeDepth = depthState.writeDepth();
            this.depthBiasScaleFactor = depthState.depthBiasScaleFactor();
            this.depthBiasConstant = depthState.depthBiasConstant();
         });
         if (snippet.colorTargetStates() != null && snippet.activeColorTargetStateCount() > 0 && snippet.colorTargetStates()[0] != null) {
            this.colorTargetFormat = snippet.colorTargetStates()[0].format();
            this.blendFunction = snippet.colorTargetStates()[0].blendFunction();
            this.colorWriteMask = snippet.colorTargetStates()[0].writeMask();
         }
      }
   }

   public static RenderPipelineBuilder builder(Snippet... snippets) {
      return new RenderPipelineBuilder(snippets);
   }

   public RenderPipelineBuilder withDepthTest(CompareOp depthCompare) {
      this.depthCompare = depthCompare;
      this.depthStateDirty = true;
      return this;
   }

   public RenderPipelineBuilder withColorWrite(boolean writeRgb, boolean writeAlpha) {
      this.colorWriteMask = (writeRgb ? 7 : 0) | (writeAlpha ? 8 : 0);
      this.colorTargetStateDirty = true;
      return this;
   }

   public RenderPipelineBuilder withDepthBias(float scaleFactor, float constant) {
      this.depthBiasScaleFactor = scaleFactor;
      this.depthBiasConstant = constant;
      this.depthStateDirty = true;
      return this;
   }

   public RenderPipeline build() {
      this.applyPendingState();
      return this.pipelineBuilder.build();
   }

   public RenderPipelineBuilder withShaderDefine(String name, float value) {
      this.pipelineBuilder.withShaderDefine(name, value);
      return this;
   }

   private void applyPendingState() {
      if (this.bindGroupLayoutDirty) {
         this.pipelineBuilder.withBindGroupLayout(this.bindGroupLayoutBuilder.build());
         this.bindGroupLayoutDirty = false;
      }

      if (this.depthStateDirty) {
         this.pipelineBuilder
            .withDepthStencilState(
               this.depthCompare == CompareOp.ALWAYS_PASS ? Optional.empty() : Optional.of(new DepthStencilState(this.depthCompare, this.writeDepth, this.depthBiasScaleFactor, this.depthBiasConstant))
            );
      }

      if (this.colorTargetStateDirty) {
         this.pipelineBuilder.withColorTargetState(new ColorTargetState(this.blendFunction, this.colorTargetFormat, this.colorWriteMask));
      }
   }

   public RenderPipelineBuilder withCull(boolean cull) {
      this.pipelineBuilder.withCull(cull);
      return this;
   }

   public RenderPipelineBuilder withLocation(String location) {
      this.pipelineBuilder.withLocation(location);
      return this;
   }

   public RenderPipelineBuilder withoutBlend() {
      this.blendFunction = Optional.empty();
      this.colorTargetStateDirty = true;
      return this;
   }

   public Snippet buildSnippet() {
      this.applyPendingState();
      return this.pipelineBuilder.buildSnippet();
   }

   public RenderPipelineBuilder withUniform(String name, UniformType type) {
      if (this.bindingNames.add(name)) {
         this.bindGroupLayoutBuilder.withUniform(name, type);
         this.bindGroupLayoutDirty = true;
      }

      return this;
   }

   public RenderPipelineBuilder withFragmentShader(String shader) {
      this.pipelineBuilder.withFragmentShader(shader);
      return this;
   }

   public RenderPipelineBuilder withBlend(BlendFunction blendFunction) {
      this.blendFunction = Optional.of(blendFunction);
      this.colorTargetStateDirty = true;
      return this;
   }

   public RenderPipelineBuilder withShaderDefine(String name, int value) {
      this.pipelineBuilder.withShaderDefine(name, value);
      return this;
   }

   public RenderPipelineBuilder withVertexShader(Identifier shader) {
      this.pipelineBuilder.withVertexShader(shader);
      return this;
   }

   public RenderPipelineBuilder withShaderDefine(String name) {
      this.pipelineBuilder.withShaderDefine(name);
      return this;
   }

   public RenderPipelineBuilder withDepthWrite(boolean writeDepth) {
      this.writeDepth = writeDepth;
      this.depthStateDirty = true;
      return this;
   }

   public RenderPipelineBuilder withVertexShader(String shader) {
      this.pipelineBuilder.withVertexShader(shader);
      return this;
   }

   public RenderPipelineBuilder withVertexFormat(VertexFormat vertexFormat, PrimitiveTopology topology) {
      if (vertexFormat.getVertexSize() != 0) {
         this.pipelineBuilder.withVertexBinding(0, vertexFormat);
      }

      this.pipelineBuilder.withPrimitiveTopology(topology);
      return this;
   }

   public RenderPipelineBuilder withFragmentShader(Identifier shader) {
      this.pipelineBuilder.withFragmentShader(shader);
      return this;
   }

   public RenderPipelineBuilder withSampler(String name) {
      if (this.bindingNames.add(name)) {
         this.bindGroupLayoutBuilder.withSampler(name);
         this.bindGroupLayoutDirty = true;
      }

      return this;
   }

   public RenderPipelineBuilder withLocation(Identifier location) {
      this.pipelineBuilder.withLocation(location);
      return this;
   }
}
