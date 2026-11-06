package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;
import sg.events.impl.WorldGeometryBuffer;
import sg.render.UiProjectionBuffer;

public class UiItemStackPass implements MinecraftAccess, UiRenderPassSupport {
   private final SubmitNodeStorage Х = new SubmitNodeStorage();
   private static final int АА = 256;
   private static final int А4 = 600;
   private final RenderPipeline сЩ;
   @SerializedName("А")
   private final CommandEncoder А_at_1cb4b8e0;
   private final UiProjectionBuffer Я;
   private final Map<UiItemStackPass.ItemTextureKey, UiItemStackPass.CachedItemTexture> сl;
   private int Ап;
   private static final String Вб衣 = "rose/item_stack";
   private static final String Вбш = "texture";
   private static final String ВбИ = "texture";
   private static final String Вбь = "Forms";
   private static final String Вбж = "Colors";
   private static final String Вб弟 = "Scissors";
   private static final String Вбб = "Sampler0";
   private static final String ВбЫ = "rose item projection";
   private static final float Вбl = -1000.0F;
   private static final float Вб8 = 1000.0F;
   private static final float Вб5 = 0.004F;
   private static final float Вбв = 0.004F;
   private static final float Вбъ = 10.0F;
   private static final float ВбМ = 10.0F;
   private static final float ВбН = 10.0F;
   private static final float Вбн = 0.004F;
   private static final float ВбЦ = 1.0E-4F;
   private static final float Вбч = 16.0F;
   private static final float ВбЭ = 1.0E-4F;
   private static final float Вб必 = 16.0F;
   private static final float Вбщ = 8.0F;
   private static final float ВбГ = 8.0F;
   private static final float Вбл = 8.0F;
   private static final float Вбэ = 8.0F;
   private static final float Вбю = 0.5F;
   private static final float ВбЩ = 0.5F;
   private static final float Вбу = -8.0F;
   private static final float Вб2 = -8.0F;
   private static final double Вбф = 16.0;
   private static final double Вби = 16.0;
   private static final double Вб9 = 16.0;
   private static final double Вбо = 16.0;
   private static final double ВбO = 16.0;
   private static final double Вб7 = 16.0;
   private static final double ВбР = 16.0;
   private static final double Вб3 = 16.0;
   private static final float Вбр = 8.0F;
   private static final float Вб6 = 8.0F;
   private static final float Вб_ = 16.0F;
   private static final float ВбШ = 10.0F;
   private static final float Вбы = 10.0F;
   private static final float Вбй = 10.0F;
   private static final int ВбЖ = 15728880;
   private static final float Вбм = -1.0F;
   private static final float ВбК = 0.001F;
   private static final String ВбI = "Sampler0";
   private static final float ВбТ = 255.0F;
   private static final String ВбО = "Forms";
   private static final String ВбФ = "Colors";
   private static final String ВбЙ = "Scissors";
   private static final int Вбк = Integer.MAX_VALUE;
   private static final String Вбг = "itemStackPass";
   private static final String Вб0 = "rose item depth";
   private static final String Вбх = "rose item";

   public void й诶() {
      for (UiItemStackPass.CachedItemTexture var2 : this.сl.values()) {
         this.I(var2);
      }

      this.сl.clear();
   }

   private UiItemStackPass.CachedItemTexture I(ItemStack var1, int var2) {
      return this.I(var1, var2, ItemDisplayContext.GUI, 0, 0, 0);
   }

   private void йЙ() {
      UiItemStackPass.ItemTextureKey var1 = null;
      int var2 = Вбк;

      for (Entry var4 : this.сl.entrySet()) {
         if (((UiItemStackPass.CachedItemTexture)var4.getValue()).ек < var2) {
            var2 = ((UiItemStackPass.CachedItemTexture)var4.getValue()).ек;
            var1 = (UiItemStackPass.ItemTextureKey)var4.getKey();
         }
      }

      if (var1 != null) {
         this.I(this.сl.remove(var1));
      }
   }

   public void I(ItemStack var1, float var2, float var3, float var4, float var5) {
      if (!var1.isEmpty() && !(var4 <= 0.0F) && !(var5 <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= Вбн) && !this.isOutsideClip(var2, var3, var4, var5)) {
         TrackingItemStackRenderState var6 = new TrackingItemStackRenderState();
         MINECRAFT_CLIENT.getItemModelResolver().updateForTopItem(var6, var1, ItemDisplayContext.GUI, MINECRAFT_CLIENT.level, MINECRAFT_CLIENT.player, 0);
         if (!var6.isEmpty()) {
            AABB var7 = var6.getModelBoundingBox();
            float var8 = Math.max(ВбЦ, (float)var7.getXsize() * Вбч);
            float var9 = Math.max(ВбЭ, (float)var7.getYsize() * Вб必);
            float var10 = Math.min(var4 / var8, var5 / var9);
            if (Float.isFinite(var10)) {
               int var11 = Math.clamp(Mth.ceil(var10), 1, 8);
               UiItemStackPass.CachedItemTexture var12 = this.I(var6, var11, ItemDisplayContext.GUI, 0, 0, 0, true);
               if (var12 != null) {
                  float var13 = Вбщ + (float)(var7.minX + var7.maxX) * ВбГ;
                  float var14 = Вбл - (float)(var7.minY + var7.maxY) * Вбэ;
                  this.I(
                     var12,
                     var2 + var4 * Вбю + (var12.А9 - var13) * var10,
                     var3 + var5 * ВбЩ + (var12.Ао - var14) * var10,
                     var12.еО * var10,
                     var12.еФ * var10,
                     var10,
                     var11
                  );
               }
            }
         }
      }
   }

   private UiItemStackPass.CachedItemTexture I(ItemStack var1, int var2, ItemDisplayContext var3, int var4, int var5, int var6) {
      TrackingItemStackRenderState var7 = new TrackingItemStackRenderState();
      MINECRAFT_CLIENT.getItemModelResolver().updateForTopItem(var7, var1, var3, MINECRAFT_CLIENT.level, MINECRAFT_CLIENT.player, 0);
      return var7.isEmpty() ? null : this.I(var7, var2, var3, var4, var5, var6, false);
   }

   public GpuTextureView I(ItemStack var1, float var2) {
      if (!var1.isEmpty() && !(var2 <= 0.0F)) {
         UiItemStackPass.CachedItemTexture var3 = this.I(var1, Math.clamp(Mth.ceil(var2), 1, 8));
         return var3 == null ? null : var3.field_4;
      } else {
         return null;
      }
   }

   public void I(ItemStack var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      if (!var1.isEmpty() && !(var4 <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= Вбв)) {
         int var8 = Math.clamp(Mth.ceil(var4), 1, 8);
         UiItemStackPass.CachedItemTexture var9 = this.I(
            var1, var8, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, Math.round(var5 * Вбъ), Math.round(var6 * ВбМ), Math.round(var7 * ВбН)
         );
         if (var9 != null) {
            this.I(var9, var2 + var9.А9 * var4, var3 + var9.Ао * var4, var9.еО * var4, var9.еФ * var4, var4, var8);
         }
      }
   }

   private void I(UiItemStackPass.CachedItemTexture var1) {
      if (var1 != null) {
         if (var1.field_4 != null) {
            var1.field_4.close();
         }

         if (var1.е != null) {
            var1.е.close();
         }

         if (var1.п != null) {
            var1.п.close();
         }

         if (var1.А != null) {
            var1.А.close();
         }

         var1.е = null;
         var1.field_4 = null;
         var1.А = null;
         var1.п = null;
      }
   }

   public UiItemStackPass() {
      this.сЩ = RenderPipelineBuilder.builder()
         .withLocation(Вб衣)
         .withVertexShader(ClientResourceIds.getCoreId(Вбш))
         .withFragmentShader(ClientResourceIds.getCoreId(ВбИ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX, PrimitiveTopology.QUADS)
         .withUniform(Вбь, UniformType.UNIFORM_BUFFER)
         .withUniform(Вбж, UniformType.UNIFORM_BUFFER)
         .withUniform(Вб弟, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
         .withSampler(Вбб)
         .build();
      this.А_at_1cb4b8e0 = RenderSystem.getDevice().createCommandEncoder();
      this.Я = new UiProjectionBuffer(ВбЫ, Вбl, Вб8, true);
      this.сl = new HashMap<>();
   }

   private UiItemStackPass.CachedItemTexture I(TrackingItemStackRenderState var1, int var2, ItemDisplayContext var3, int var4, int var5, int var6, boolean var7) {
      UiItemStackPass.ItemTextureKey var8 = new UiItemStackPass.ItemTextureKey(var1.getModelIdentity(), var2, var3, var4, var5, var6, var7);
      UiItemStackPass.CachedItemTexture var9 = this.сl.get(var8);
      if (var9 == null || var9.Яш && var9.еЙ != this.Ап) {
         var9 = this.I(var1, var9, var8, var2);
      }

      if (var9 == null) {
         return null;
      }

      var9.ек = this.Ап;
      return var9;
   }

   private void I(UiItemStackPass.CachedItemTexture var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (!this.isOutsideClip(var2, var3, var4, var5)) {
         SharedGeometryBuffer var8 = SharedGeometryBuffer.getInstance();
         BufferBuilder var9 = var8.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
         float var10 = 2.0F / MINECRAFT_CLIENT.getWindow().getScreenWidth() * var4;
         float var11 = 2.0F / MINECRAFT_CLIENT.getWindow().getScreenHeight() * var5;
         float var12 = Вбм + var2 * 2.0F / MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float var13 = 1.0F - var3 * 2.0F / MINECRAFT_CLIENT.getWindow().getScreenHeight() - var11;
         var9.addVertex(UiDrawing.getTransform(), var12, var13, 0.0F).setUv(0.0F, 0.0F);
         var9.addVertex(UiDrawing.getTransform(), var12 + var10, var13, 0.0F).setUv(1.0F, 0.0F);
         var9.addVertex(UiDrawing.getTransform(), var12 + var10, var13 + var11, 0.0F).setUv(1.0F, 1.0F);
         var9.addVertex(UiDrawing.getTransform(), var12, var13 + var11, 0.0F).setUv(0.0F, 1.0F);
         RenderPass var14 = this.А_at_1cb4b8e0.createRenderPass(() -> Вбг, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(var14);
         boolean var15 = Math.abs(var6 - var7) < ВбК;
         var14.bindTexture(ВбI, var1.field_4, RenderSystem.getSamplerCache().getClampToEdge(var15 ? FilterMode.NEAREST : FilterMode.LINEAR));

         try (MemoryStack var19 = MemoryStack.stackPush()) {
            ByteBuffer var20 = Std140Builder.onStack(var19, 32).putVec4(new Vector4f(0.0F)).putVec2(var5, var4).putFloat(0.0F).get();
            float var21 = UiDrawing.getGlobalAlpha() / ВбТ;
            Vector4f var22 = new Vector4f(var21, var21, var21, var21);
            ByteBuffer var23 = Std140Builder.onStack(var19, 64).putVec4(var22).putVec4(var22).putVec4(var22).putVec4(var22).get();
            GpuBufferSlice var16 = this.uploadClipUniform(new Vector4f(var2, var3, var4, var5), var19);
            GpuBufferSlice var17 = TransientGpuUpload.uploadUniform(var20);
            GpuBufferSlice var18 = TransientGpuUpload.uploadUniform(var23);
            var14.setUniform(ВбО, var17);
            var14.setUniform(ВбФ, var18);
            var14.setUniform(ВбЙ, var16);
         }

         this.drawAndClose(var9, var14, this.сЩ);
      }
   }

   private UiItemStackPass.CachedItemTexture I(
      TrackingItemStackRenderState var1, UiItemStackPass.CachedItemTexture var2, UiItemStackPass.ItemTextureKey var3, int var4
   ) {
      float var5 = 0.0F;
      float var6 = 0.0F;
      int var7 = 16;
      int var8 = 16;
      if (var3.context() == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
         var5 = Вбу;
         var6 = Вб2;
         var7 = 32;
         var8 = 32;
      }

      AABB var9 = var1.getModelBoundingBox();
      int var10 = Mth.ceil(var9.getXsize() * Вбф);
      int var11 = Mth.ceil(var9.getYsize() * Вби);
      if (var3.fitted()) {
         int var12 = Mth.floor((float)(var9.minX * Вб9));
         int var13 = Mth.ceil((float)(var9.maxY * Вбо));
         var5 = var12 + 8;
         var6 = 8 - var13;
         var7 = Math.max(1, Mth.ceil((float)(var9.maxX * ВбO)) - var12);
         var8 = Math.max(1, var13 - Mth.floor((float)(var9.minY * Вб7)));
      } else if (var3.context() == ItemDisplayContext.GUI && (var10 > 16 || var11 > 16)) {
         int var23 = Mth.floor((float)(var9.minX * ВбР));
         int var25 = Mth.floor((float)(var9.maxY * Вб3));
         var5 = var23 + 8;
         var6 = 8 - var25;
         var7 = var10;
         var8 = var11;
      }

      int var24 = var7 * var4;
      int var26 = var8 * var4;
      if (var2 != null && (var2.еI != var24 || var2.еТ != var26)) {
         this.сl.remove(var3);
         this.I(var2);
         var2 = null;
      }

      UiItemStackPass.CachedItemTexture var14 = var2 != null ? var2 : new UiItemStackPass.CachedItemTexture();
      if (var14.е == null) {
         GpuDevice var15 = RenderSystem.getDevice();
         var14.е = var15.createTexture(() -> Вбх, 13, GpuFormat.RGBA8_UNORM, var24, var26, 1, 1);
         var14.field_4 = var15.createTextureView(var14.е);
         var14.А = var15.createTexture(() -> Вб0, 9, GpuFormat.D32_FLOAT, var24, var26, 1, 1);
         var14.п = var15.createTextureView(var14.А);
      }

      var14.еI = var24;
      var14.еТ = var26;
      var14.А9 = var5;
      var14.Ао = var6;
      var14.еО = var7;
      var14.еФ = var8;
      var14.Яш = var1.isAnimated();
      var14.еЙ = this.Ап;
      this.А_at_1cb4b8e0.clearColorAndDepthTextures(var14.е, new Vector4f(0.0F), var14.А, 0.0);
      WorldGeometryBuffer.StagedBuffer var27 = WorldGeometryBuffer.getShared();
      var27.flush();
      GpuTextureView var16 = RenderSystem.outputColorTextureOverride;
      GpuTextureView var17 = RenderSystem.outputDepthTextureOverride;
      RenderSystem.outputColorTextureOverride = var14.field_4;
      RenderSystem.outputDepthTextureOverride = var14.п;
      RenderSystem.backupProjectionMatrix();

      try {
         RenderSystem.setProjectionMatrix(this.Я.а(var24, var26), ProjectionType.ORTHOGRAPHIC);
         MINECRAFT_CLIENT.gameRenderer
            .lighting()
            .setupFor(var1.usesBlockLight() ? com.mojang.blaze3d.platform.Lighting.Entry.ITEMS_3D : com.mojang.blaze3d.platform.Lighting.Entry.ITEMS_FLAT);
         PoseStack var18 = new PoseStack();
         var18.translate((Вбр - var5) * var4, (Вб6 - var6) * var4, 0.0F);
         float var19 = Вб_ * var4;
         var18.scale(var19, -var19, var19);
         var18.mulPose(Axis.YP.rotationDegrees(var3.yaw() / ВбШ));
         var18.mulPose(Axis.ZP.rotationDegrees(var3.roll() / Вбы));
         var18.mulPose(Axis.XP.rotationDegrees(var3.pitch() / Вбй));
         var1.submit(var18, this.Х, ВбЖ, OverlayTexture.NO_OVERLAY, 0);
         MINECRAFT_CLIENT.gameRenderer.featureRenderDispatcher().renderAllFeatures(this.Х);
         var27.flush();
      } finally {
         RenderSystem.outputColorTextureOverride = var16;
         RenderSystem.outputDepthTextureOverride = var17;
         RenderSystem.restoreProjectionMatrix();
      }

      if (var2 == null) {
         if (this.сl.size() >= 256) {
            this.йЙ();
         }

         this.сl.put(var3, var14);
      }

      return var14;
   }

   public void йЫ() {
      this.Ап++;
      if (this.Ап % 120 == 0) {
         Iterator var1 = this.сl.entrySet().iterator();

         while (var1.hasNext()) {
            UiItemStackPass.CachedItemTexture var2 = (UiItemStackPass.CachedItemTexture)((Entry)var1.next()).getValue();
            if (this.Ап - var2.ек > 600) {
               this.I(var2);
               var1.remove();
            }
         }
      }
   }

   public void л(ItemStack var1, float var2, float var3, float var4) {
      if (!var1.isEmpty() && !(var4 <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= Вб5)) {
         int var5 = Math.clamp(Mth.ceil(var4), 1, 8);
         UiItemStackPass.CachedItemTexture var6 = this.I(var1, var5);
         if (var6 != null) {
            if (!this.isOutsideClip(var2 + var6.А9 * var4, var3 + var6.Ао * var4, var6.еО * var4, var6.еФ * var4)) {
               this.I(var6, var2 + var6.А9 * var4, var3 + var6.Ао * var4, var6.еО * var4, var6.еФ * var4, var4, var5);
            }
         }
      }
   }

   private static final class CachedItemTexture {
      GpuTexture е;
      @SerializedName("4")
      GpuTextureView field_4;
      GpuTexture А;
      GpuTextureView п;
      int еI;
      int еТ;
      float А9;
      float Ао;
      int еО;
      int еФ;
      boolean Яш;
      int еЙ;
      int ек;
   }

   private record ItemTextureKey(Object modelKey, int texScale, ItemDisplayContext context, int pitch, int yaw, int roll, boolean fitted) {
   }
}
