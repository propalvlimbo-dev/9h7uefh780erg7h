package sg.mixinbridge;

import sg.mixin.RenderTypeAccessor;
import sg.mixin.RenderSetupAccessor;
import sg.mixin.RenderPipelinesAccessor;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup.OutlineProperty;
import net.minecraft.client.renderer.rendertype.RenderSetup.TextureBinding;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import sg.modules.impl.render.EntityESP;
import sg.client.RoseClient;
import sg.render.pipeline.RenderPipelineBuilder;

public final class EntityEspOutlineRenderTypes {
   private static final RenderPipeline ВЬ = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelinesAccessor.getOutlineSnippet())
         .withLocation(Identifier.fromNamespaceAndPath(EntityEspOutlineRenderTypes.сЧо, EntityEspOutlineRenderTypes.сЧO))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .build()
   );
   private static final Function<Identifier, RenderType> field_4 = Util.memoize(EntityEspOutlineRenderTypes::method_6);
   private static final Map<RenderType, RenderType> В1 = new ConcurrentHashMap<>();
   private static final String сЧф = "rose_entity_esp1_outline_complete";
   private static final String сЧи = "Sampler0";
   private static final String сЧ9 = "Sampler0";
   private static final String сЧо = "rose";
   private static final String сЧO = "pipeline/entity_esp1_outline_complete";

   public static void 必З() {
      В1.clear();
   }

   public static RenderType б(Identifier var0) {
      return field_4.apply(var0);
   }

   public static void Ю(PreparedRenderType var0, GpuBuffer var1, GpuBuffer var2, IndexType var3, int var4, int var5, int var6) {
      if (var0.pipeline() == ВЬ && method_9Ц()) {
         RoseClient.getInstance().getServices().getModules().getEntityESP().getSurfaceRenderer().captureEntitySurfaces(var0, var1, var2, var3, var4, var5, var6);
      }
   }

   private EntityEspOutlineRenderTypes() {
   }

   public static boolean method_9Ц() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         EntityESP var0 = RoseClient.getInstance().getServices().getModules().getEntityESP();
         return var0 != null && var0.isEnabled() && var0.getSurfaceRenderer() != null && var0.getSurfaceRenderer().shouldUseCustomOutline();
      } else {
         return false;
      }
   }

   public static RenderType Ь_15b47058(RenderType var0) {
      if (var0 != null && var0.isOutline() && method_9Ц()) {
         RenderType var1 = В1.get(var0);
         if (var1 != null) {
            return var1;
         }

         Identifier var2 = Ь_c635a640(var0);
         RenderType var3 = var2 == null ? var0 : б(var2);
         if (В1.size() >= 1024) {
            В1.clear();
         }

         В1.put(var0, var3);
         В1.put(var3, var3);
         return var3;
      } else {
         return var0;
      }
   }

   private static Identifier Ь_c635a640(RenderType var0) {
      RenderSetup var1 = ((RenderTypeAccessor)var0).getRenderSetup();
      if (var1 == null) {
         return null;
      }

      Map var2 = ((RenderSetupAccessor)(Object)var1).getTextures();
      if (var2 == null) {
         return null;
      }

      TextureBinding var3 = (TextureBinding)var2.get(сЧ9);
      return var3 == null ? null : var3.location();
   }

   private static RenderType method_6(Identifier var0) {
      return RenderTypeAccessor.create(
         сЧф,
         RenderSetup.builder(ВЬ).withTexture(сЧи, var0).setOutputTarget(OutputTarget.OUTLINE_TARGET).setOutline(OutlineProperty.IS_OUTLINE).createRenderSetup()
      );
   }
}
