package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.impl.init.RoseXrayLines;
import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;

public final class BlockOverlay extends ClientModule {
   private static final double Вл = 0.01;
   private static final double Вэ = 0.002;
   private static final double Вю = 36.0;
   private final SingleChoiceSetting П;
   private final BooleanSetting В4;
   private final FloatSetting ЯН;
   private final FloatSetting Ян;
   private final FloatSetting ЯЦ;
   private final FloatSetting Яч;
   private final FloatSetting ЯЭ;
   private final FloatSetting Я必;
   private final BooleanSetting Вп;
   private final FloatSetting Ящ;
   private final RenderPipeline с6;
   private final RenderPipeline с_;
   private final RenderPipeline сШ;
   private final RenderPipeline сы;
   @SerializedName("Я")
   private AABB Я_at_dbaeda60;
   private long ЯД;
   private TextureTarget Э;
   private final EventListener<WorldEffectsEvent> ЯВ;
   private static final String ВАФ = "BlockOverlay";
   private static final String ВАЙ = "Подсвечивает блок под прицелом";
   private static final String ВАк = "Режим";
   private static final String ВАг = "Плазма";
   private static final String ВА0 = "Шейдер";
   private static final String ВАх = "Заливка";
   private static final String ВАУ = "Обводка темы";
   private static final String ВАЗ = "Прозрачность";
   private static final float ВАЕ = 0.72F;
   private static final float ВАП = 0.1F;
   private static final float ВА西 = 0.05F;
   private static final String ВАд = "Скорость плазмы";
   private static final float ВАБ = 1.15F;
   private static final float ВАС = 0.1F;
   private static final float ВАз = 5.0F;
   private static final float ВАц = 0.1F;
   private static final String ВАя = "Масштаб плазмы";
   private static final float В4с = 2.4F;
   private static final float В4В = 0.5F;
   private static final float В4Я = 5.0F;
   private static final float В4Х = 0.1F;
   private static final String В4е = "Ширина оутлайна";
   private static final float В4А = 2.75F;
   private static final float В44 = 0.5F;
   private static final float В4п = 8.0F;
   private static final float В4Д = 0.25F;
   private static final String В4Ъ = "Прозрачность оутлайна";
   private static final float В4т = 70.0F;
   private static final float В4а = 5.0F;
   private static final float В4Ю = 100.0F;
   private static final String В4Ч = "Сила свечения";
   private static final float В4Л = 1.05F;
   private static final float В4诶 = 0.2F;
   private static final float В41 = 2.5F;
   private static final float В4Ь = 0.05F;
   private static final String В4衣 = "Плавность";
   private static final String В4ш = "Скорость плавности";
   private static final float В4И = 10.0F;
   private static final float В4ь = 25.0F;
   private static final float В4ж = 0.5F;
   private static final String В4弟 = "rose/block_overlay_fill";
   private static final String В4б = "block_overlay_mask";
   private static final String В4Ы = "block_overlay_mask";
   private static final String В4l = "rose/block_overlay_mask";
   private static final String В48 = "block_overlay_mask";
   private static final String В45 = "block_overlay_mask";
   private static final String В4в = "rose/block_overlay_plasma";
   private static final String В4ъ = "hands_fullscreen";
   private static final String В4М = "block_overlay_plasma";
   private static final String В4Н = "BlockOverlayPlasma";
   private static final String В4н = "MaskSampler";
   private static final String В4Ц = "rose/block_overlay_crystal";
   private static final String В4ч = "hands_fullscreen";
   private static final String В4Э = "block_overlay_crystal";
   private static final String В4必 = "BlockOverlayCrystal";
   private static final String В4щ = "MaskSampler";
   private static final long В4Г = -1L;
   private static final double В4л = 0.5;
   private static final double В4э = 0.5;
   private static final double В4ю = 0.5;
   private static final double В4Щ = 36.0;
   private static final String В4у = "Плазма";
   private static final String В42 = "Шейдер";
   private static final float В4ф = 255.0F;
   private static final float В4и = 255.0F;
   private static final float В49 = 255.0F;
   private static final float В4о = 255.0F;
   private static final float В4O = 255.0F;
   private static final float В47 = 255.0F;
   private static final long В4Р = 100000L;
   private static final float В43 = 1000.0F;
   private static final float В4р = 100.0F;
   private static final String В46 = "MaskSampler";
   private static final String В4_ = "BlockOverlayPlasma";
   private static final float В4Ш = 255.0F;
   private static final float В4ы = 255.0F;
   private static final float В4й = 255.0F;
   private static final long В4Ж = 100000L;
   private static final float В4м = 1000.0F;
   private static final float В4К = 100.0F;
   private static final String В4I = "MaskSampler";
   private static final String В4Т = "BlockOverlayCrystal";
   private static final double В4О = 0.01;
   private static final float В4Ф = 255.0F;
   private static final float В4Й = 255.0F;
   private static final double В4к = 0.01;
   private static final float В4г = 100.0F;
   private static final double В40 = 0.002;
   private static final String В4х = "block-overlay-mask";
   private static final double В4У = 0.001;
   private static final double В4З = 0.001;
   private static final double В4Е = 0.001;
   private static final double В4П = 0.001;
   private static final double В4西 = 0.001;
   private static final double В4д = 0.001;
   private static final double В4Б = 1.0E-7;
   private static final double В4С = 1.0E-7;
   private static final double В4з = 1.0E-7;
   private static final double В4ц = 1.0E9;
   private static final long В4я = -1L;
   private static final String Впс = "blockOverlayMask";
   private static final String ВпВ = "blockOverlay";
   private static final String ВпЯ = "blockOverlayVertices";
   private static final String ВпХ = "blockOverlayCrystal";
   private static final String Впе = "blockOverlayCrystal";
   private static final String ВпА = "blockOverlayPlasma";
   private static final String Вп4 = "blockOverlayPlasma";
   private static final String Впп = "Плазма";
   private static final String ВпД = "Шейдер";
   private static final String ВпЪ = "Плазма";
   private static final String Впт = "Плазма";

   private void Х(PoseStack var1, AABB var2, Vec3 var3, int var4) {
      if (this.д(var1, var2, var3)) {
         Color var5 = new Color(var4, true);
         CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
         GpuBuffer var7 = null;

         try (MemoryStack var8 = MemoryStack.stackPush()) {
            ByteBuffer var9 = Std140Builder.onStack(var8, 64)
               .putVec4(var5.getRed() / В4Ш, var5.getGreen() / В4ы, var5.getBlue() / В4й, this.ЯН.getValue())
               .putVec4((float)(System.currentTimeMillis() % В4Ж) / В4м, this.В4.getValue() ? 1.0F : 0.0F, 0.0F, 0.0F)
               .putVec4(this.Яч.getValue(), this.ЯЭ.getValue() / В4К, this.Я必.getValue(), 0.0F)
               .putVec2(this.Э.width, this.Э.height)
               .get();
            var7 = RenderSystem.getDevice().createBuffer(() -> Впе, 128, var9);
            RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

            try (RenderPass var11 = var6.createRenderPass(() -> ВпХ, var10.getColorTextureView(), Optional.empty())) {
               RenderSystem.bindDefaultUniforms(var11);
               var11.bindTexture(В4I, this.Э.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var11.setUniform(В4Т, var7);
               var11.setPipeline(this.сы);
               var11.draw(3, 1, 0, 0);
            }
         } finally {
            if (var7 != null) {
               var7.close();
            }
         }
      }
   }

   private void д(PoseStack var1, AABB var2, Vec3 var3, int var4) {
      if (this.д(var1, var2, var3)) {
         Color var5 = new Color(var4, true);
         Color var6 = new Color(Math.min(255, var5.getRed() + 70), Math.min(255, var5.getGreen() + 55), Math.min(255, var5.getBlue() + 40));
         CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
         GpuBuffer var8 = null;

         try (MemoryStack var9 = MemoryStack.stackPush()) {
            ByteBuffer var10 = Std140Builder.onStack(var9, 80)
               .putVec4(var5.getRed() / В4ф, var5.getGreen() / В4и, var5.getBlue() / В49, this.ЯН.getValue())
               .putVec4(var6.getRed() / В4о, var6.getGreen() / В4O, var6.getBlue() / В47, 1.0F)
               .putVec4((float)(System.currentTimeMillis() % В4Р) / В43, this.Ян.getValue(), this.ЯЦ.getValue(), this.В4.getValue() ? 1.0F : 0.0F)
               .putVec4(this.Яч.getValue(), this.ЯЭ.getValue() / В4р, this.Я必.getValue(), 0.0F)
               .putVec2(this.Э.width, this.Э.height)
               .get();
            var8 = RenderSystem.getDevice().createBuffer(() -> Вп4, 128, var10);
            RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

            try (RenderPass var12 = var7.createRenderPass(() -> ВпА, var11.getColorTextureView(), Optional.empty())) {
               RenderSystem.bindDefaultUniforms(var12);
               var12.bindTexture(В46, this.Э.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var12.setUniform(В4_, var8);
               var12.setPipeline(this.сШ);
               var12.draw(3, 1, 0, 0);
            }
         } finally {
            if (var8 != null) {
               var8.close();
            }
         }
      }
   }

   private AABB д(Level var1, BlockPos var2) {
      BlockState var3 = var1.getBlockState(var2);
      AABB var4 = var3.getShape(var1, var2).bounds();
      return !var4.hasNaN() && (!(var4.minX >= var4.maxX - В4Б) || !(var4.minY >= var4.maxY - В4С) || !(var4.minZ >= var4.maxZ - В4з))
         ? var4.move(var2.getX(), var2.getY(), var2.getZ())
         : new AABB(var2);
   }

   private void д(BufferBuilder var1, Matrix4f var2, AABB var3, float var4, float var5, float var6, float var7) {
      float var8 = (float)var3.minX;
      float var9 = (float)var3.minY;
      float var10 = (float)var3.minZ;
      float var11 = (float)var3.maxX;
      float var12 = (float)var3.maxY;
      float var13 = (float)var3.maxZ;
      this.д(var1, var2, var8, var9, var10, var11, var9, var10, var11, var9, var13, var8, var9, var13, var4, var5, var6, var7);
      this.д(var1, var2, var8, var12, var10, var8, var12, var13, var11, var12, var13, var11, var12, var10, var4, var5, var6, var7);
      this.д(var1, var2, var8, var9, var10, var8, var12, var10, var11, var12, var10, var11, var9, var10, var4, var5, var6, var7);
      this.д(var1, var2, var8, var9, var13, var11, var9, var13, var11, var12, var13, var8, var12, var13, var4, var5, var6, var7);
      this.д(var1, var2, var8, var9, var10, var8, var9, var13, var8, var12, var13, var8, var12, var10, var4, var5, var6, var7);
      this.д(var1, var2, var11, var9, var10, var11, var12, var10, var11, var12, var13, var11, var9, var13, var4, var5, var6, var7);
   }

   public static boolean УО() {
      try {
         BlockOverlay var0 = RoseClient.getInstance().getServices().getModules().getBlockOverlay();
         if (var0 == null || !var0.isEnabled()) {
            return false;
         } else if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameRenderer != null) {
            Vec3 var1 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            return !var0.И(var1);
         } else {
            return true;
         }
      } catch (Throwable var2) {
         return false;
      }
   }

   private void д(WorldEffectsEvent var1, AABB var2, Vec3 var3, int var4) {
      float var5 = this.ЯЭ.getValue() / В4г;
      int var6 = ColorMath.withAlpha(var4, var5);
      AABB var7 = var2.inflate(В40);
      VertexConsumer var8 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
      WorldBoxGeometry.appendBoxOutline(var1.getPoses(), var8, var3, var7.minX, var7.minY, var7.minZ, var7.maxX, var7.maxY, var7.maxZ, var6);
   }

   private static double ь(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   private boolean д(BufferBuilder var1, RenderPipeline var2, RenderTarget var3, boolean var4) {
      MeshData var5 = var1.build();
      if (var5 == null) {
         return false;
      }

      GpuBuffer var6 = null;

      try {
         DrawState var7 = var5.drawState();
         int var8 = var7.indexCount();
         if (var8 <= 0) {
            return false;
         }

         AutoStorageIndexBuffer var9 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
         GpuBuffer var10 = var9.getBuffer(var8);
         IndexType var11 = var9.type();
         var6 = RenderSystem.getDevice().createBuffer(() -> ВпЯ, 32, var5.vertexBuffer());
         CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();

         try (RenderPass var13 = var4
               ? var12.createRenderPass(() -> ВпВ, var3.getColorTextureView(), Optional.empty(), var3.getDepthTextureView(), OptionalDouble.empty())
               : var12.createRenderPass(() -> Впс, var3.getColorTextureView(), Optional.empty())) {
            RenderSystem.bindDefaultUniforms(var13);
            var13.setPipeline(var2);
            var13.setVertexBuffer(0, var6.slice());
            var13.setIndexBuffer(var10, var11);
            var13.drawIndexed(var8, 1, 0, 0, 0);
         }

         return true;
      } finally {
         if (var6 != null) {
            var6.close();
         }

         var5.close();
      }
   }

   private boolean И(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1.x - В4У, var1.y - В4З, var1.z - В4Е);
      BlockPos var3 = BlockPos.containing(var1.x + В4П, var1.y + В4西, var1.z + В4д);

      for (BlockPos var5 : BlockPos.betweenClosed(var2, var3)) {
         BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(var5);
         if (!var6.isAir()) {
            VoxelShape var7 = var6.getCollisionShape(MINECRAFT_CLIENT.level, var5);
            if (!var7.isEmpty()) {
               AABB var8 = var7.bounds();
               double var9 = var1.x - var5.getX();
               double var11 = var1.y - var5.getY();
               double var13 = var1.z - var5.getZ();
               if (var8.contains(var9, var11, var13)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void Э弟() {
      int var1 = MINECRAFT_CLIENT.getWindow().getWidth();
      int var2 = MINECRAFT_CLIENT.getWindow().getHeight();
      if (var1 > 0 && var2 > 0) {
         if (this.Э == null) {
            this.Э = new TextureTarget(В4х, var1, var2, false, GpuFormat.RGBA8_UNORM);
         } else {
            if (this.Э.width != var1 || this.Э.height != var2) {
               this.Э.resize(var1, var2);
            }
         }
      }
   }

   private void д(
      BufferBuilder var1,
      Matrix4f var2,
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
      float var14,
      float var15,
      float var16,
      float var17,
      float var18
   ) {
      var1.addVertex(var2, var3, var4, var5).setUv(0.0F, 0.0F).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var6, var7, var8).setUv(1.0F, 0.0F).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var9, var10, var11).setUv(1.0F, 1.0F).setColor(var15, var16, var17, var18);
      var1.addVertex(var2, var12, var13, var14).setUv(0.0F, 1.0F).setColor(var15, var16, var17, var18);
   }

   private boolean д(PoseStack var1, AABB var2, Vec3 var3) {
      this.Э弟();
      if (this.Э == null) {
         return false;
      }

      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      var4.clearColorTexture(this.Э.getColorTexture(), new Vector4f(0.0F));
      AABB var5 = var2.inflate(В4О).move(-var3.x, -var3.y, -var3.z);
      BufferBuilder var6 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      this.д(var6, var1.last().pose(), var5, 1.0F, 1.0F, 1.0F, 1.0F);
      return this.д(var6, this.с_, this.Э, false);
   }

   @Override
   public void onDisable() {
      this.ЭЗ();
      if (this.Э != null) {
         this.Э.destroyBuffers();
         this.Э = null;
      }
   }

   private void у(PoseStack var1, AABB var2, Vec3 var3, int var4) {
      int var5 = Math.max(0, Math.min(255, Math.round(this.ЯН.getValue() * В4Ф)));
      int var6 = ColorMath.withAlpha(var4, var5 / В4Й);
      Vector4f var7 = ColorMath.toRgbaVector(var6);
      AABB var8 = var2.inflate(В4к).move(-var3.x, -var3.y, -var3.z);
      BufferBuilder var9 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      this.д(var9, var1.last().pose(), var8, var7.x, var7.y, var7.z, var7.w);
      this.д(var9, this.с6, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget(), true);
   }

   private void О(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         HitResult var2 = MINECRAFT_CLIENT.hitResult;
         if (var2 != null && var2.getType() == Type.BLOCK) {
            BlockHitResult var3 = (BlockHitResult)var2;
            Vec3 var4 = var1.getCameraPosition();
            if (!var3.isInside() && !this.И(var4)) {
               BlockPos var5 = var3.getBlockPos();
               double var6 = MINECRAFT_CLIENT.player.distanceToSqr(var5.getX() + В4л, var5.getY() + В4э, var5.getZ() + В4ю);
               if (var6 > В4Щ) {
                  this.ЭЗ();
               } else {
                  AABB var8 = this.д(MINECRAFT_CLIENT.level, var5);
                  if (var8.contains(var4)) {
                     this.ЭЗ();
                  } else {
                     this.д(var8);
                     if (this.Я_at_dbaeda60 != null) {
                        this.д(var1, this.Я_at_dbaeda60);
                     }
                  }
               }
            } else {
               this.ЭЗ();
            }
         } else {
            this.ЭЗ();
         }
      } else {
         this.ЭЗ();
      }
   }

   private void д(WorldEffectsEvent var1, AABB var2) {
      Vec3 var3 = var1.getCameraPosition();
      PoseStack var4 = var1.getPoses();
      int var5 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      if (this.П.isSelected(В4у)) {
         this.д(var4, var2, var3, var5);
      } else if (this.П.isSelected(В42)) {
         this.Х(var4, var2, var3, var5);
      } else {
         this.у(var4, var2, var3, var5);
         if (this.В4.getValue()) {
            this.д(var1, var2, var3, var5);
         }
      }
   }

   public BlockOverlay() {
      super(ВАФ, ВАЙ, ModuleCategory.RENDER);
      this.П = new SingleChoiceSetting(ВАк, new ChoiceOption(ВАг), new ChoiceOption(ВА0), new ChoiceOption(ВАх));
      this.В4 = new BooleanSetting(ВАУ, true);
      this.ЯН = new FloatSetting(ВАЗ, ВАЕ, ВАП, 1.0F).withStep(ВА西);
      this.Ян = new FloatSetting(ВАд, ВАБ, ВАС, ВАз).withStep(ВАц).visibleWhen(() -> this.П.isSelected(Впт));
      this.ЯЦ = new FloatSetting(ВАя, В4с, В4В, В4Я).withStep(В4Х).visibleWhen(() -> this.П.isSelected(ВпЪ));
      this.Яч = new FloatSetting(В4е, В4А, В44, В4п).withStep(В4Д).visibleWhen(this.В4::getValue);
      this.ЯЭ = new FloatSetting(В4Ъ, В4т, В4а, В4Ю).withStep(1.0F).visibleWhen(this.В4::getValue);
      this.Я必 = new FloatSetting(В4Ч, В4Л, В4诶, В41).withStep(В4Ь).visibleWhen(() -> (this.П.isSelected(Впп) || this.П.isSelected(ВпД)) && this.В4.getValue());
      this.Вп = new BooleanSetting(В4衣, true);
      this.Ящ = new FloatSetting(В4ш, В4И, 2.0F, В4ь).withStep(В4ж).visibleWhen(this.Вп::getValue);
      this.с6 = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(В4弟)
         .withVertexShader(ClientResourceIds.getCoreId(В4б))
         .withFragmentShader(ClientResourceIds.getCoreId(В4Ы))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.с_ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(В4l)
         .withVertexShader(ClientResourceIds.getCoreId(В48))
         .withFragmentShader(ClientResourceIds.getCoreId(В45))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.сШ = RenderPipelineBuilder.builder()
         .withLocation(В4в)
         .withVertexShader(ClientResourceIds.getCoreId(В4ъ))
         .withFragmentShader(ClientResourceIds.getCoreId(В4М))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(В4Н, UniformType.UNIFORM_BUFFER)
         .withSampler(В4н)
         .build();
      this.сы = RenderPipelineBuilder.builder()
         .withLocation(В4Ц)
         .withVertexShader(ClientResourceIds.getCoreId(В4ч))
         .withFragmentShader(ClientResourceIds.getCoreId(В4Э))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(В4必, UniformType.UNIFORM_BUFFER)
         .withSampler(В4щ)
         .build();
      this.ЯД = В4Г;
      this.ЯВ = this::О;
      this.addSettings(this.П, this.В4, this.ЯН, this.Ян, this.ЯЦ, this.Яч, this.ЯЭ, this.Я必, this.Вп, this.Ящ);
   }

   private void ЭЗ() {
      this.Я_at_dbaeda60 = null;
      this.ЯД = В4я;
   }

   private void д(AABB var1) {
      long var2 = System.nanoTime();
      if (!this.Вп.getValue()) {
         this.Я_at_dbaeda60 = var1;
         this.ЯД = var2;
      } else if (this.Я_at_dbaeda60 != null && this.ЯД >= 0L) {
         double var4 = (var2 - this.ЯД) / В4ц;
         this.ЯД = var2;
         double var6 = this.Ящ.getValue().floatValue();
         double var8 = 1.0 - Math.exp(-var6 * Math.max(0.0, var4));
         this.Я_at_dbaeda60 = new AABB(
            ь(this.Я_at_dbaeda60.minX, var1.minX, var8),
            ь(this.Я_at_dbaeda60.minY, var1.minY, var8),
            ь(this.Я_at_dbaeda60.minZ, var1.minZ, var8),
            ь(this.Я_at_dbaeda60.maxX, var1.maxX, var8),
            ь(this.Я_at_dbaeda60.maxY, var1.maxY, var8),
            ь(this.Я_at_dbaeda60.maxZ, var1.maxZ, var8)
         );
      } else {
         this.Я_at_dbaeda60 = var1;
         this.ЯД = var2;
      }
   }
}
