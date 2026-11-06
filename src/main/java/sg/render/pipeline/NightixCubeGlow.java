package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;

public final class NightixCubeGlow implements MinecraftAccess {
   public static final Identifier field_4 = Identifier.fromNamespaceAndPath(NightixCubeGlow.нВ, NightixCubeGlow.нЯ);
   public static final Identifier п = Identifier.fromNamespaceAndPath(NightixCubeGlow.нХ, NightixCubeGlow.не);
   private static final RenderPipeline[] В = new RenderPipeline[]{method_9(false, false, false), method_9(false, false, true)};
   private static final RenderPipeline[] Я = new RenderPipeline[]{method_9(true, false, false), method_9(true, false, true)};
   private static final RenderPipeline[] Х = new RenderPipeline[]{method_9(false, true, false), method_9(false, true, true)};
   private static final String Н西 = "nightix_cube_glow";
   private static final String Нд = "nightix_cube";
   private static final String НБ = "nightix_cube_glow";
   private static final String НС = "nightix_cube";
   private static final String Нз = "Sampler0";
   private static final String Нц = "Sampler0";
   private static final String Ня = "Nightix cubes";
   private static final String нс = "Nightix cube vertices";
   private static final String нВ = "rose";
   private static final String нЯ = "textures/nightix/particles_1.png";
   private static final String нХ = "rose";
   private static final String не = "textures/nightix/particles_2.png";

   private NightixCubeGlow() {
   }

   public static void ц(BufferBuilder var0, Matrix4f var1, float var2, int var3) {
      for (int var4 = -1; var4 <= 1; var4 += 2) {
         method_9(var0, var1, -var2, var4 * var2, -var2, var2, var4 * var2, -var2, var3);
         method_9(var0, var1, var2, var4 * var2, -var2, var2, var4 * var2, var2, var3);
         method_9(var0, var1, var2, var4 * var2, var2, -var2, var4 * var2, var2, var3);
         method_9(var0, var1, -var2, var4 * var2, var2, -var2, var4 * var2, -var2, var3);
      }

      for (int var6 = -1; var6 <= 1; var6 += 2) {
         for (int var5 = -1; var5 <= 1; var5 += 2) {
            method_9(var0, var1, var6 * var2, -var2, var5 * var2, var6 * var2, var2, var5 * var2, var3);
         }
      }
   }

   private static void method_9(BufferBuilder var0, Matrix4f var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
      var0.addVertex(var1, var2, var3, var4).setColor(var8);
      var0.addVertex(var1, var5, var6, var7).setColor(var8);
   }

   public static BufferBuilder method_9(boolean var0, boolean var1) {
      return SharedGeometryBuffer.getInstance()
         .begin(var0 ? PrimitiveTopology.DEBUG_LINES : PrimitiveTopology.QUADS, var1 ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR);
   }

   private static void method_9(
      BufferBuilder var0,
      Matrix4f var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      int var14
   ) {
      var0.addVertex(var1, var2, var3, var4).setColor(var14);
      var0.addVertex(var1, var5, var6, var7).setColor(var14);
      var0.addVertex(var1, var8, var9, var10).setColor(var14);
      var0.addVertex(var1, var11, var12, var13).setColor(var14);
   }

   public static void method_9(BufferBuilder var0, Matrix4f var1, float var2, int var3) {
      var0.addVertex(var1, -var2, -var2, 0.0F).setColor(var3).setUv(0.0F, 1.0F);
      var0.addVertex(var1, var2, -var2, 0.0F).setColor(var3).setUv(1.0F, 1.0F);
      var0.addVertex(var1, var2, var2, 0.0F).setColor(var3).setUv(1.0F, 0.0F);
      var0.addVertex(var1, -var2, var2, 0.0F).setColor(var3).setUv(0.0F, 0.0F);
   }

   private static RenderPipeline method_9(boolean var0, boolean var1, boolean var2) {
      RenderPipelineBuilder var3 = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation("rose/nightix_cube_" + var0 + "_" + var1 + "_" + var2)
         .withVertexShader(ClientResourceIds.getCoreId(var1 ? Н西 : Нд))
         .withFragmentShader(ClientResourceIds.getCoreId(var1 ? НБ : НС))
         .withVertexFormat(var1 ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR, var0 ? PrimitiveTopology.DEBUG_LINES : PrimitiveTopology.QUADS)
         .withDepthTest(var2 ? CompareOp.GREATER_THAN_OR_EQUAL : CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING);
      if (var1) {
         var3.withSampler(Нз);
      }

      return var3.build();
   }

   public static void Ц(BufferBuilder var0, Matrix4f var1, float var2, int var3) {
      method_9(var0, var1, -var2, var2, -var2, var2, var2, -var2, var2, var2, var2, -var2, var2, var2, var3);
      method_9(var0, var1, -var2, -var2, var2, var2, -var2, var2, var2, -var2, -var2, -var2, -var2, -var2, var3);
      method_9(var0, var1, var2, -var2, -var2, var2, -var2, var2, var2, var2, var2, var2, var2, -var2, var3);
      method_9(var0, var1, -var2, -var2, var2, -var2, -var2, -var2, -var2, var2, -var2, -var2, var2, var2, var3);
      method_9(var0, var1, -var2, -var2, var2, var2, -var2, var2, var2, var2, var2, -var2, var2, var2, var3);
      method_9(var0, var1, var2, -var2, -var2, -var2, -var2, -var2, -var2, var2, -var2, var2, var2, -var2, var3);
   }

   public static void method_9(BufferBuilder var0, boolean var1, Identifier var2, boolean var3) {
      MeshData var4 = var0.build();
      if (var4 != null) {
         try (
            var4;
            GpuBuffer var6 = RenderSystem.getDevice().createBuffer(() -> нс, 32, var4.vertexBuffer());
         ) {
            DrawState var7 = var4.drawState();
            AutoStorageIndexBuffer var8 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
            GpuBuffer var9 = var8.getBuffer(var7.indexCount());
            RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

            try (RenderPass var11 = RenderSystem.getDevice()
                  .createCommandEncoder()
                  .createRenderPass(() -> Ня, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty())) {
               RenderSystem.bindDefaultUniforms(var11);
               var11.setPipeline((var2 != null ? Х : (var1 ? Я : В))[var3 ? 1 : 0]);
               if (var2 != null) {
                  AbstractTexture var12 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);
                  var11.bindTexture(Нц, var12.getTextureView(), var12.getSampler());
               }

               var11.setVertexBuffer(0, var6.slice());
               var11.setIndexBuffer(var9, var8.type());
               var11.drawIndexed(var7.indexCount(), 1, 0, 0, 0);
            }
         }
      }
   }
}
