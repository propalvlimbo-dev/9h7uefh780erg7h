package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.GpuBufferSlice.MappedView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Consumer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.util.time.ColorMath;

public final class ViewModelEditorCompositor {
   public static final ViewModelEditorCompositor В = new ViewModelEditorCompositor();
   private static final RenderPipeline сП = RenderPipelineBuilder.builder()
      .withLocation(ViewModelEditorCompositor.ВЪЕ)
      .withVertexShader(ClientResourceIds.getCoreId(ViewModelEditorCompositor.ВЪП))
      .withFragmentShader(ClientResourceIds.getCoreId(ViewModelEditorCompositor.ВЪ西))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(ViewModelEditorCompositor.ВЪд, UniformType.UNIFORM_BUFFER)
      .withSampler(ViewModelEditorCompositor.ВЪБ)
      .withSampler(ViewModelEditorCompositor.ВЪС)
      .withSampler(ViewModelEditorCompositor.ВЪз)
      .withSampler(ViewModelEditorCompositor.ВЪц)
      .build();
   private TextureTarget ю;
   private TextureTarget Щ;
   private GpuBuffer ф;
   private GpuBuffer и;
   private boolean еу;
   private boolean е2;
   private static final String ВЪЖ = "view-model-editor-hands";
   private static final String ВЪм = "view-model-editor-items";
   private static final float ВЪК = 255.0F;
   private static final float ВЪI = 255.0F;
   private static final float ВЪТ = 255.0F;
   private static final float ВЪО = 255.0F;
   private static final String ВЪФ = "HandSampler";
   private static final String ВЪЙ = "ItemSampler";
   private static final String ВЪк = "HandDepthSampler";
   private static final String ВЪг = "ItemDepthSampler";
   private static final String ВЪ0 = "ViewModelEditor";
   private static final String ВЪх = "viewModelEditorHitReadback";
   private static final String ВЪУ = "viewModelEditorComposite";
   private static final String ВЪЗ = "viewModelEditorUniforms";
   private static final String ВЪЕ = "rose/view_model_editor";
   private static final String ВЪП = "hands_fullscreen";
   private static final String ВЪ西 = "view_model_editor";
   private static final String ВЪд = "ViewModelEditor";
   private static final String ВЪБ = "HandSampler";
   private static final String ВЪС = "ItemSampler";
   private static final String ВЪз = "HandDepthSampler";
   private static final String ВЪц = "ItemDepthSampler";

   public RenderTarget method_4() {
      RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.Щ.getColorTexture(), new Vector4f(0.0F), this.Щ.getDepthTexture(), 0.0);
      return this.Щ;
   }

   private ViewModelEditorCompositor() {
   }

   public RenderTarget Я() {
      RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.ю.getColorTexture(), new Vector4f(0.0F), this.ю.getDepthTexture(), 0.0);
      return this.ю;
   }

   public void о弟() {
      if (this.ю != null) {
         this.ю.destroyBuffers();
         this.ю = null;
      }

      if (this.Щ != null) {
         this.Щ.destroyBuffers();
         this.Щ = null;
      }

      if (this.ф != null) {
         this.ф.close();
      }

      if (this.и != null) {
         this.и.close();
      }

      this.ф = null;
      this.и = null;
      this.еу = false;
      this.е2 = false;
   }

   private GpuBuffer Н(TextureTarget var1, GpuBuffer var2, int var3, int var4, Consumer<Boolean> var5, Runnable var6) {
      if (var1 != null && var1.getColorTexture() != null) {
         int var7 = var1.width;
         int var8 = var1.height;
         int var9 = Math.clamp(var3 - 2, 0, Math.max(0, var7 - 1));
         int var10 = Math.clamp(var4 - 2, 0, Math.max(0, var8 - 1));
         int var11 = Math.min(5, var7 - var9);
         int var12 = Math.min(5, var8 - var10);
         int var13 = var1.getColorTexture().getFormat().blockSize();
         long var14 = (long)var7 * var8 * var13;
         if (var2 == null || var2.size() < var14) {
            if (var2 != null) {
               var2.close();
            }

            var2 = RenderSystem.getDevice().createBuffer(() -> ВЪх, 9, var14);
         }

         GpuBuffer var16 = var2;
         CommandEncoder var17 = RenderSystem.getDevice().createCommandEncoder();
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(var1.getColorTexture(), var16, 0L, () -> {
            boolean var6x = false;

            try (MappedView var7x = var16.slice().map(true, false)) {
               ByteBuffer var8x = var7x.data();
               int var9x = var11 * var12;

               for (int var10x = 0; var10x < var9x; var10x++) {
                  int var11x = var10x * var13;

                  for (int var12x = 0; var12x < var13; var12x++) {
                     if (var8x.get(var11x + var12x) != 0) {
                        var6x = true;
                        break;
                     }
                  }

                  if (var6x) {
                     break;
                  }
               }
            } finally {
               var6.run();
            }

            var5.accept(var6x);
         }, 0, var9, var10, var11, var12);
         return var2;
      } else {
         var6.run();
         return var2;
      }
   }

   public void Н(int var1, int var2, Consumer<Boolean> var3) {
      if (!this.еу) {
         this.еу = true;
         this.ф = this.Н(this.ю, this.ф, var1, var2, var3, () -> this.еу = false);
      }
   }

   public void ы(int var1, int var2) {
      if (this.ю == null) {
         this.ю = new TextureTarget(ВЪЖ, var1, var2, true, GpuFormat.RGBA8_UNORM);
         this.Щ = new TextureTarget(ВЪм, var1, var2, true, GpuFormat.RGBA8_UNORM);
      } else if (this.ю.width != var1 || this.ю.height != var2) {
         this.ю.resize(var1, var2);
         this.Щ.resize(var1, var2);
      }
   }

   public void Н(RenderTarget var1, float var2, float var3, float var4, float var5, Vector2f var6, Vector2f var7, int var8) {
      if (this.ю != null
         && this.Щ != null
         && var1 != null
         && this.ю.getColorTextureView() != null
         && this.Щ.getColorTextureView() != null
         && this.ю.getDepthTextureView() != null
         && this.Щ.getDepthTextureView() != null
         && var1.getColorTextureView() != null) {
         CommandEncoder var9 = RenderSystem.getDevice().createCommandEncoder();

         try (MemoryStack var10 = MemoryStack.stackPush()) {
            ByteBuffer var11 = Std140Builder.onStack(var10, 48)
               .putVec4(var2, var3, var4, var5)
               .putVec4(var6.x, var6.y, var7.x, var7.y)
               .putVec4(ColorMath.getRed(var8) / ВЪК, ColorMath.getGreen(var8) / ВЪI, ColorMath.getBlue(var8) / ВЪТ, ColorMath.getAlpha(var8) / ВЪО)
               .get();

            try (
               GpuBuffer var12 = RenderSystem.getDevice().createBuffer(() -> ВЪЗ, 128, var11);
               RenderPass var13 = var9.createRenderPass(() -> ВЪУ, var1.getColorTextureView(), Optional.empty());
            ) {
               var13.setPipeline(сП);
               var13.bindTexture(ВЪФ, this.ю.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var13.bindTexture(ВЪЙ, this.Щ.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var13.bindTexture(ВЪк, this.ю.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               var13.bindTexture(ВЪг, this.Щ.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               var13.setUniform(ВЪ0, var12);
               var13.draw(3, 1, 0, 0);
            }
         }
      }
   }

   public void Я(int var1, int var2, Consumer<Boolean> var3) {
      if (!this.е2) {
         this.е2 = true;
         this.и = this.Н(this.Щ, this.и, var1, var2, var3, () -> this.е2 = false);
      }
   }
}
