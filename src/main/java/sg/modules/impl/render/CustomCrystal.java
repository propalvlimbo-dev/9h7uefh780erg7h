package sg.modules.impl.render;

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
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.impl.EntityRenderStateEvent;
import sg.events.EventListener;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public final class CustomCrystal extends ClientModule {
   private static final int ед = -1;
   private final SingleChoiceSetting Т;
   private final FloatSetting Вф;
   private final BooleanSetting сР;
   private final FloatSetting Ви;
   private final FloatSetting В9;
   private final FloatSetting Во;
   private final FloatSetting ВO;
   private final RenderPipeline сГ;
   private boolean Яж;
   private final EventListener<EntityRenderStateEvent> Ву;
   private static final String Я5Б = "CustomCrystal";
   private static final String Я5С = "Шейдер и анимация кристалла";
   private static final String Я5з = "Режим";
   private static final String Я5ц = "Шейдер";
   private static final String Я5я = "Заливка";
   private static final String Явс = "Размер";
   private static final float ЯвВ = 0.2F;
   private static final float ЯвЯ = 3.0F;
   private static final float ЯвХ = 0.05F;
   private static final String Яве = "Обводка темы";
   private static final String ЯвА = "Прозрачность";
   private static final float Яв4 = 85.0F;
   private static final float Явп = 10.0F;
   private static final float ЯвД = 100.0F;
   private static final String ЯвЪ = "Ширина оутлайна";
   private static final float Явт = 2.5F;
   private static final float Ява = 0.5F;
   private static final float ЯвЮ = 8.0F;
   private static final float ЯвЧ = 0.25F;
   private static final String ЯвЛ = "Прозрачность оутлайна";
   private static final float Яв诶 = 65.0F;
   private static final float Яв1 = 5.0F;
   private static final float ЯвЬ = 100.0F;
   private static final String Яв衣 = "Сила свечения";
   private static final float Явш = 0.7F;
   private static final float ЯвИ = 0.2F;
   private static final float Явь = 0.05F;
   private static final String Явж = "rose/custom_crystal_composite";
   private static final String Яв弟 = "hands_fullscreen";
   private static final String Явб = "custom_crystal_composite";
   private static final String ЯвЫ = "CustomCrystalComposite";
   private static final String Явl = "MaskSampler";
   private static final float Яв8 = 24.0F;
   private static final float Яв5 = 255.0F;
   private static final float Явв = 255.0F;
   private static final float Явъ = 255.0F;
   private static final float ЯвМ = 100.0F;
   private static final long ЯвН = 100000L;
   private static final float Явн = 1000.0F;
   private static final String ЯвЦ = "Шейдер";
   private static final float Явч = 100.0F;
   private static final String ЯвЭ = "MaskSampler";
   private static final String Яв必 = "CustomCrystalComposite";
   private static final String Явщ = "customCrystalComposite";
   private static final String ЯвГ = "customCrystalComposite";

   public void л(RenderTarget var1, RenderTarget var2) {
      if (this.isEnabled() && var1 != null && var2 != null && var1.getColorTextureView() != null) {
         Color var3 = new Color(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         GpuBuffer var5 = null;

         try (MemoryStack var6 = MemoryStack.stackPush()) {
            ByteBuffer var7 = Std140Builder.onStack(var6, 64)
               .putVec4(var3.getRed() / Яв5, var3.getGreen() / Явв, var3.getBlue() / Явъ, this.Ви.getValue() / ЯвМ)
               .putVec4((float)(System.currentTimeMillis() % ЯвН) / Явн, this.сР.getValue() ? 1.0F : 0.0F, this.Т.isSelected(ЯвЦ) ? 1.0F : 0.0F, 0.0F)
               .putVec4(this.В9.getValue(), this.Во.getValue() / Явч, this.ВO.getValue(), 0.0F)
               .putVec2(var1.width, var1.height)
               .get();
            var5 = RenderSystem.getDevice().createBuffer(() -> ЯвГ, 128, var7);

            try (RenderPass var8 = var4.createRenderPass(() -> Явщ, var2.getColorTextureView(), Optional.empty())) {
               RenderSystem.bindDefaultUniforms(var8);
               var8.bindTexture(ЯвЭ, var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var8.setUniform(Яв必, var5);
               var8.setPipeline(this.сГ);
               var8.draw(3, 1, 0, 0);
            }
         } finally {
            if (var5 != null) {
               var5.close();
            }
         }
      }
   }

   public void к(PoseStack var1) {
      float var2 = this.Вф.getValue();
      var1.scale(var2, var2, var2);
   }

   @Override
   public void onDisable() {
      this.Яж = false;
   }

   public boolean З2() {
      boolean var1 = this.Яж;
      this.Яж = false;
      return var1;
   }

   public void л(EndCrystalModel var1) {
      var1.cube.visible = false;
      var1.base.visible = false;
      var1.outerGlass.y = Яв8;
   }

   public void л(EndCrystalModel var1, boolean var2, boolean var3, float var4) {
      var1.cube.visible = var2;
      var1.base.visible = var3;
      var1.outerGlass.y = var4;
   }

   private void А(EntityRenderStateEvent var1) {
      if (this.isEnabled() && var1.getEntity() instanceof EndCrystal) {
         var1.getRenderState().outlineColor = -1;
         var1.getRenderState().isInvisible = true;
         this.Яж = true;
      }
   }

   public CustomCrystal() {
      super(Я5Б, Я5С, ModuleCategory.RENDER);
      this.Т = new SingleChoiceSetting(Я5з, new ChoiceOption(Я5ц), new ChoiceOption(Я5я));
      this.Вф = new FloatSetting(Явс, 1.0F, ЯвВ, ЯвЯ).withStep(ЯвХ);
      this.сР = new BooleanSetting(Яве, true);
      this.Ви = new FloatSetting(ЯвА, Яв4, Явп, ЯвД).withStep(1.0F);
      this.В9 = new FloatSetting(ЯвЪ, Явт, Ява, ЯвЮ).withStep(ЯвЧ).visibleWhen(this.сР::getValue);
      this.Во = new FloatSetting(ЯвЛ, Яв诶, Яв1, ЯвЬ).withStep(1.0F).visibleWhen(this.сР::getValue);
      this.ВO = new FloatSetting(Яв衣, Явш, ЯвИ, 2.0F).withStep(Явь).visibleWhen(this.сР::getValue);
      this.сГ = RenderPipelineBuilder.builder()
         .withLocation(Явж)
         .withVertexShader(ClientResourceIds.getCoreId(Яв弟))
         .withFragmentShader(ClientResourceIds.getCoreId(Явб))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(ЯвЫ, UniformType.UNIFORM_BUFFER)
         .withSampler(Явl)
         .build();
      this.Ву = this::А;
      this.addSettings(this.Т, this.Вф, this.сР, this.Ви, this.В9, this.Во, this.ВO);
   }
}
