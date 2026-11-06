package sg.gui;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.UiRenderPassSupport;
import sg.util.time.ColorMath;

public final class ClickGuiBackground implements MinecraftAccess, UiRenderPassSupport {
   private static final Identifier К = ClientResourceIds.getImageId(ClickGuiBackground.ВсЯ);
   private static final Identifier I = ClientResourceIds.getImageId(ClickGuiBackground.ВсХ);
   private static final Identifier Т = ClientResourceIds.getImageId(ClickGuiBackground.Все);
   private final long Вд = System.nanoTime();
   private final RenderPipeline с3;
   private static final String сяЙ = "rose/click_gui_background";
   private static final String сяк = "click_gui_background";
   private static final String сяг = "click_gui_background";
   private static final String ся0 = "ClickGuiBackground";
   private static final float сях = 1.0E9F;
   private static final float сяУ = 255.0F;
   private static final float сяЗ = 0.82F;
   private static final float сяЕ = 0.82F;
   private static final float сяП = 0.48F;
   private static final float ся西 = 0.48F;
   private static final float сяд = 20.0F;
   private static final float сяБ = 30.0F;
   private static final float сяС = 30.0F;
   private static final float сяз = -30.0F;
   private static final float сяц = 30.0F;
   private static final String сяя = "ClickGuiBackground";
   private static final String Всс = "clickGuiBackgroundUniforms";
   private static final String ВсВ = "clickGuiBackgroundPass";
   private static final String ВсЯ = "nightix/background";
   private static final String ВсХ = "nightix/circles_effect";
   private static final String Все = "nightix/top_glow";

   public ClickGuiBackground() {
      this.с3 = RenderPipelineBuilder.builder()
         .withLocation(сяЙ)
         .withVertexShader(ClientResourceIds.getCoreId(сяк))
         .withFragmentShader(ClientResourceIds.getCoreId(сяг))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(ся0, UniformType.UNIFORM_BUFFER)
         .build();
   }

   public void з(float var1, float var2, float var3, int var4, int var5) {
      float var6 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
      float var7 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
      float var8 = (float)(System.nanoTime() - this.Вд) / сях;
      int var9 = ColorMath.packArgb(255, 255, 255, сяУ * var3);
      int var10 = ColorMath.withAlpha(var4, var3 * сяЗ);
      int var11 = ColorMath.withAlpha(var5, var3 * сяЕ);
      int var12 = ColorMath.withAlpha(var4, var3 * сяП);
      int var13 = ColorMath.withAlpha(var5, var3 * ся西);
      UiDrawing.drawBlur(0.0F, 0.0F, var6, var7, сяд, 0.0F, var9);
      UiDrawing.drawTexture(0.0F, 0.0F, var6, var7, 0.0F, new Vector4i(var10, var11, var11, var10), К);
      Vector4i var14 = new Vector4i(var12, var13, var13, var12);
      UiDrawing.drawTexture(0.0F, сяБ - сяС * var3, var6, var7, 0.0F, var14, I);
      UiDrawing.drawTexture(0.0F, сяз + сяц * var3, var6, var7, 0.0F, var14, Т);
      BufferBuilder var15 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
      this.appendQuad(var15, 0.0F, 0.0F, var6, var7);
      CommandEncoder var16 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var17 = var16.createRenderPass(() -> ВсВ, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
      RenderSystem.bindDefaultUniforms(var17);

      GpuBuffer var18;
      try (MemoryStack var19 = MemoryStack.stackPush()) {
         Vector4f var20 = ColorMath.toRgbaVector(var4);
         Vector4f var21 = ColorMath.toRgbaVector(var5);
         ByteBuffer var22 = Std140Builder.onStack(var19, 64)
            .putVec4(var6, var7, var8, var3)
            .putVec4(var1, var2, 0.0F, 0.0F)
            .putVec4(var20)
            .putVec4(var21)
            .get();
         var18 = RenderSystem.getDevice().createBuffer(() -> Всс, 128, var22);
         var17.setUniform(сяя, var18);
      }

      this.drawAndClose(var15, var17, this.с3);
      var18.close();
   }
}
