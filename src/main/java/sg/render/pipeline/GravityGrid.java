package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.util.Optional;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class GravityGrid implements MinecraftAccess {
   public static final GravityGrid INSTANCE = new GravityGrid();
   private static final int UNIFORM_DATA_BYTES = 48;
   private final @com.google.gson.annotations.SerializedName("с5")  RenderPipeline pipeline;
   private static final String PIPELINE_LOCATION = "rose/gravity_grid";
   private static final String VERTEX_SHADER = "hands_fullscreen";
   private static final String FRAGMENT_SHADER = "gravity_grid";
   private static final String GRID_UNIFORM_LAYOUT_NAME = "GravityGrid";
   private static final float MIN_REVEAL_PROGRESS = 0.01F;
   private static final String GRID_UNIFORM_BINDING = "GravityGrid";
   private static final String PASS_LABEL = "gravityGrid";
   private static final String UNIFORM_BUFFER_LABEL = "gravityGridUniforms";

   public void drawGrid(float width, float height, float revealProgress) {
      if (!(revealProgress <= MIN_REVEAL_PROGRESS) && !(width <= 0.0F) && !(height <= 0.0F)) {
         RenderTarget outputTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (outputTarget != null && outputTarget.getColorTextureView() != null) {
            Vector4f accentTop = ColorMath.toRgbaVector(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
            Vector4f accentBottom = ColorMath.toRgbaVector(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO));
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

            try (MemoryStack stack = MemoryStack.stackPush()) {
               Std140Builder uniformBuilder = Std140Builder.onStack(stack, 48)
                  .putVec4(width, height, Math.clamp(revealProgress, 0.0F, 1.0F), 0.0F)
                  .putVec4(accentTop.x, accentTop.y, accentTop.z, 1.0F)
                  .putVec4(accentBottom.x, accentBottom.y, accentBottom.z, 1.0F);
               ByteBuffer uniformData = uniformBuilder.get();

               try (
                  GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> UNIFORM_BUFFER_LABEL, 128, uniformData);
                  RenderPass renderPass = encoder.createRenderPass(() -> PASS_LABEL, outputTarget.getColorTextureView(), Optional.empty());
               ) {
                  RenderSystem.bindDefaultUniforms(renderPass);
                  renderPass.setPipeline(this.pipeline);
                  renderPass.setUniform(GRID_UNIFORM_BINDING, uniformBuffer);
                  renderPass.draw(3, 1, 0, 0);
               }
            }
         }
      }
   }

   private GravityGrid() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withColorWrite(true, true)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(GRID_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .build();
   }
}
