package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import sg.ec.MinecraftAccess;
import sg.events.impl.WorldGeometryBuffer;

public final class RoseBatchedItemAtlas implements AutoCloseable, MinecraftAccess {
   @SerializedName("В")
   private final SubmitNodeStorage В_a2b87273 = new SubmitNodeStorage();
   private static final int Ф = 1024;
   private static final int Й = 32;
   private static final int к = 32;
   private static final int г = 1024;
   private static final int field_0 = 512;
   private static final long Ж = 50000000L;
   private static int х;
   private final Map<Object, RoseBatchedItemAtlas.AtlasSlot> Ю = new HashMap<>();
   private final Map<Object, RoseBatchedItemAtlas.PendingRender> Ч = new LinkedHashMap<>();
   private final Map<RoseBatchedItemAtlas.ItemStackKey, RoseBatchedItemAtlas.ResolvedItemEntry> Л;
   @SerializedName("В")
   private final UiProjectionBuffer В_5d4d56f6;
   @SerializedName("Я")
   private GpuTexture Я_98cc79b7;
   @SerializedName("Я")
   private GpuTextureView Я_dd184401;
   @SerializedName("Х")
   private GpuTexture Х_98cc79b7;
   @SerializedName("Х")
   private GpuTextureView Х_dd184401;
   private int У;
   private int З;
   private int Е;
   private Object е;
   private boolean Щ;
   private static final float В_Ц = 0.75F;
   private static final String В_ч = "rose batched item atlas";
   private static final float В_Э = -1000.0F;
   private static final float В_必 = 1000.0F;
   private static final long В_щ = 50000000L;
   private static final long В_Г = 50000000L;
   private static final float В_л = 1024.0F;
   private static final float В_э = 1024.0F;
   private static final float В_ю = 1024.0F;
   private static final float В_Щ = 1024.0F;
   private static final float В_у = 1024.0F;
   private static final float В_2 = 1024.0F;
   private static final float В_ф = 16.0F;
   private static final float В_и = 16.0F;
   private static final float В_9 = 32.0F;
   private static final float В_о = -32.0F;
   private static final float В_O = 32.0F;
   private static final int В_7 = 15728880;
   private static final String В_Р = "Rose batched item atlas";
   private static final String В_3 = "Rose batched item atlas depth";

   public int ъ_a83dd0cc() {
      return this.Ю.size();
   }

   public void _ш() {
      if (!this.Щ) {
         if (this.Е != х || this.е != MINECRAFT_CLIENT.level) {
            this.Ю.values().forEach(var0 -> var0.еС = false);
            this.Л.values().forEach(var0 -> var0.ХЦ = false);
            this.Ю.clear();
            this.Л.clear();
            this.Ч.clear();
            this.У = 0;
            this.Е = х;
            this.е = MINECRAFT_CLIENT.level;
            RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.Я_98cc79b7, new Vector4f(0.0F), this.Х_98cc79b7, 0.0);
         }

         this.З++;
      }
   }

   public boolean _Ш() {
      return !this.Ч.isEmpty();
   }

   public void _ю() {
      if (!this.Щ && !this.Ч.isEmpty()) {
         WorldGeometryBuffer.StagedBuffer var1 = WorldGeometryBuffer.getShared();
         var1.flush();
         GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
         RenderSystem.backupProjectionMatrix();

         try {
            RenderSystem.outputColorTextureOverride = this.Я_dd184401;
            RenderSystem.outputDepthTextureOverride = this.Х_dd184401;
            RenderSystem.setProjectionMatrix(this.В_5d4d56f6.а(В_у, В_2), ProjectionType.ORTHOGRAPHIC);
            PoseStack var4 = new PoseStack();

            for (RoseBatchedItemAtlas.PendingRender var6 : this.Ч.values()) {
               this.Ъ(var6.state(), var6.entry(), var4, var1);
               var6.entry().Дк = this.З;
            }
         } finally {
            try {
               var1.flush();
            } finally {
               RenderSystem.outputColorTextureOverride = var2;
               RenderSystem.outputDepthTextureOverride = var3;
               RenderSystem.restoreProjectionMatrix();
               RenderSystem.disableScissorForRenderTypeDraws();
               this.Ч.clear();
            }
         }
      }
   }

   public static void _г() {
      х++;
   }

   public GpuTextureView Ъ_dd184401() {
      return this.Я_dd184401;
   }

   public RoseBatchedItemAtlas.UvRegion Ъ(ItemStack var1, RoseBatchedItemAtlas.ItemHandle var2) {
      if (!this.Щ && var1 != null && !var1.isEmpty()) {
         long var3 = System.nanoTime();
         if (var2.В_5487a647 != this
            || var2.В_9d93d7f4 == null
            || !var2.В_9d93d7f4.ХЦ
            || (var2.сф || var2.field_4 != var1 || var3 >= var2.В_9d93d7f4.ЯС) && !ItemStack.matches(var1, var2.В_9d93d7f4.В_449c3792.а)) {
            RoseBatchedItemAtlas.ItemStackKey var5 = new RoseBatchedItemAtlas.ItemStackKey(var1);
            RoseBatchedItemAtlas.ResolvedItemEntry var6 = this.Л.get(var5);
            if (var6 == null) {
               if (this.Л.size() >= 512) {
                  Iterator var7 = this.Л.values().iterator();
                  ((RoseBatchedItemAtlas.ResolvedItemEntry)var7.next()).ХЦ = false;
                  var7.remove();
               }

               var6 = new RoseBatchedItemAtlas.ResolvedItemEntry(var5);
               this.Л.put(var5, var6);
            }

            var2.В_5487a647 = this;
            var2.В_9d93d7f4 = var6;
         }

         var2.field_4 = var1;
         RoseBatchedItemAtlas.ResolvedItemEntry var10 = var2.В_9d93d7f4;
         boolean var11 = var2.сф && var10.В_4249d664 != null && var10.В_4249d664.еБ && var10.В_4249d664.Дк != this.З && var10.В_4249d664.Дг != this.З;
         if (var3 < var10.ЯС && (var10.В_4249d664 == null || var10.В_4249d664.еС) && !var11) {
            if (var10.В_4249d664 != null) {
               var10.В_4249d664.ДЙ = this.З;
            }

            return var10.В_4249d664 == null ? null : var10.В_4249d664.У();
         } else {
            var10.ЯС = var3 + В_щ;
            this.Л.get(var10.В_449c3792);
            TrackingItemStackRenderState var12 = new TrackingItemStackRenderState();
            MINECRAFT_CLIENT.getItemModelResolver().updateForTopItem(var12, var10.В_449c3792.а, ItemDisplayContext.GUI, MINECRAFT_CLIENT.level, MINECRAFT_CLIENT.player, 0);
            if (var12.isEmpty()) {
               var10.В_4249d664 = null;
               return null;
            }

            Object var8 = var12.getModelIdentity();
            RoseBatchedItemAtlas.AtlasSlot var9 = this.Ю.get(var8);
            if (var9 == null) {
               var9 = this.Ъ_4249d664();
               if (var9 != null) {
                  var9.еБ = var12.isAnimated();
                  this.Ю.put(var8, var9);
                  this.Ъ(var8, var12, var9);
               }
            } else if (var9.еБ && (var2.сф || var3 - var9.Хг >= В_Г) && var9.Дк != this.З && var9.Дг != this.З) {
               this.Ъ(var8, var12, var9);
            }

            var10.В_4249d664 = var9;
            if (var9 != null) {
               var9.ДЙ = this.З;
            }

            return var9 == null ? null : var9.У();
         }
      } else {
         return null;
      }
   }

   private void Ъ(Object var1, TrackingItemStackRenderState var2, RoseBatchedItemAtlas.AtlasSlot var3) {
      var3.Дг = this.З;
      var3.Хг = System.nanoTime();
      this.Ч.put(var1, new RoseBatchedItemAtlas.PendingRender(var2, var3));
   }

   private RoseBatchedItemAtlas.AtlasSlot Ъ_4249d664() {
      if (this.У < 1024) {
         int var10 = this.У++;
         int var11 = var10 % 32;
         int var13 = var10 / 32;
         int var14 = var11 * 32;
         int var15 = var13 * 32;
         float var6 = var14 / В_л;
         float var7 = (var14 + 32) / В_э;
         float var8 = (1024 - var15) / В_ю;
         float var9 = (1024 - var15 - 32) / В_Щ;
         return new RoseBatchedItemAtlas.AtlasSlot(var14, var15, var6, var8, var7, var9);
      }

      Object var1 = null;
      RoseBatchedItemAtlas.AtlasSlot var2 = null;

      for (Entry var4 : this.Ю.entrySet()) {
         RoseBatchedItemAtlas.AtlasSlot var5 = (RoseBatchedItemAtlas.AtlasSlot)var4.getValue();
         if (var5.ДЙ != this.З && !this.Ч.containsKey(var4.getKey()) && (var2 == null || var5.ДЙ < var2.ДЙ)) {
            var1 = var4.getKey();
            var2 = var5;
         }
      }

      if (var2 == null) {
         return null;
      }

      this.Ю.remove(var1);
      var2.еС = false;
      RoseBatchedItemAtlas.AtlasSlot var12 = new RoseBatchedItemAtlas.AtlasSlot(
         var2.ДО, var2.ДФ, var2.В.u0(), var2.В.vTop(), var2.В.u1(), var2.В.vBottom()
      );
      var12.Дк = 0;
      return var12;
   }

   private void Ъ(TrackingItemStackRenderState var1, RoseBatchedItemAtlas.AtlasSlot var2, PoseStack var3, WorldGeometryBuffer.StagedBuffer var4) {
      if (var2.Дк >= 0) {
         RenderSystem.getDevice()
            .createCommandEncoder()
            .clearColorAndDepthTextures(this.Я_98cc79b7, new Vector4f(0.0F), this.Х_98cc79b7, 0.0, var2.ДО, 1024 - var2.ДФ - 32, 32, 32);
      }

      var3.pushPose();

      try {
         var3.translate(var2.ДО + В_ф, var2.ДФ + В_и, 0.0F);
         var3.scale(В_9, В_о, В_O);
         MINECRAFT_CLIENT.gameRenderer
            .lighting()
            .setupFor(var1.usesBlockLight() ? com.mojang.blaze3d.platform.Lighting.Entry.ITEMS_3D : com.mojang.blaze3d.platform.Lighting.Entry.ITEMS_FLAT);
         RenderSystem.enableScissorForRenderTypeDraws(var2.ДО, 1024 - var2.ДФ - 32, 32, 32);
         var1.submit(var3, this.В_a2b87273, В_7, OverlayTexture.NO_OVERLAY, 0);
         MINECRAFT_CLIENT.gameRenderer.featureRenderDispatcher().renderAllFeatures(this.В_a2b87273);
         var4.flush();
      } finally {
         RenderSystem.disableScissorForRenderTypeDraws();
         var3.popPose();
      }
   }

   public RoseBatchedItemAtlas.UvRegion Ъ(ItemStack var1) {
      if (!this.Щ && var1 != null && !var1.isEmpty()) {
         ItemStack var2 = var1;
         TrackingItemStackRenderState var3 = new TrackingItemStackRenderState();
         MINECRAFT_CLIENT.getItemModelResolver().updateForTopItem(var3, var2, ItemDisplayContext.GUI, MINECRAFT_CLIENT.level, MINECRAFT_CLIENT.player, 0);
         if (var3.isEmpty()) {
            return null;
         }

         Object var4 = var3.getModelIdentity();
         RoseBatchedItemAtlas.AtlasSlot var5 = this.Ю.get(var4);
         if (var5 == null) {
            var5 = this.Ъ_4249d664();
            if (var5 == null) {
               return null;
            }

            var5.еБ = var3.isAnimated();
            this.Ю.put(var4, var5);
            this.Ъ(var4, var3, var5);
         } else if (var5.еБ && var5.Дк != this.З && var5.Дг != this.З) {
            this.Ъ(var4, var3, var5);
         }

         var5.ДЙ = this.З;
         return var5.У();
      } else {
         return null;
      }
   }

   public RoseBatchedItemAtlas() {
      this.Л = new LinkedHashMap<>(64, В_Ц, true);
      this.В_5d4d56f6 = new UiProjectionBuffer(В_ч, В_Э, В_必, true);
      this.Е = х;
      this._Ж();
   }

   @Override
   public void close() {
      if (!this.Щ) {
         this.Щ = true;
         this.Ч.clear();
         this.Ю.clear();
         this.Л.values().forEach(var0 -> var0.ХЦ = false);
         this.Л.clear();
         if (this.Я_dd184401 != null) {
            this.Я_dd184401.close();
            this.Я_dd184401 = null;
         }

         if (this.Я_98cc79b7 != null) {
            this.Я_98cc79b7.close();
            this.Я_98cc79b7 = null;
         }

         if (this.Х_dd184401 != null) {
            this.Х_dd184401.close();
            this.Х_dd184401 = null;
         }

         if (this.Х_98cc79b7 != null) {
            this.Х_98cc79b7.close();
            this.Х_98cc79b7 = null;
         }
      }
   }

   private void _Ж() {
      GpuDevice var1 = RenderSystem.getDevice();
      this.Я_98cc79b7 = var1.createTexture(В_Р, 13, GpuFormat.RGBA8_UNORM, 1024, 1024, 1, 1);
      this.Я_dd184401 = var1.createTextureView(this.Я_98cc79b7);
      this.Х_98cc79b7 = var1.createTexture(В_3, 9, GpuFormat.D32_FLOAT, 1024, 1024, 1, 1);
      this.Х_dd184401 = var1.createTextureView(this.Х_98cc79b7);
      var1.createCommandEncoder().clearColorAndDepthTextures(this.Я_98cc79b7, new Vector4f(0.0F), this.Х_98cc79b7, 0.0);
   }

   private static final class ItemStackKey {
      private final ItemStack а;
      private final int АЯ;

      @Override
      public int hashCode() {
         return this.АЯ;
      }

      private ItemStackKey(ItemStack var1) {
         this.а = var1.copy();
         this.АЯ = 31 * ItemStack.hashItemAndComponents(this.а) + this.а.getCount();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof RoseBatchedItemAtlas.ItemStackKey var2 && ItemStack.matches(this.а, var2.а);
      }
   }

   private static final class AtlasSlot {
      private final int ДО;
      private final int ДФ;
      private final float тO;
      private final float т7;
      private final float тР;
      private final float т3;
      private final RoseBatchedItemAtlas.UvRegion В;
      private boolean еБ;
      private boolean еС = true;
      private int ДЙ;
      private long Хг;
      private int Дк = -1;
      private int Дг = -1;

      private AtlasSlot(int var1, int var2, float var3, float var4, float var5, float var6) {
         this.ДО = var1;
         this.ДФ = var2;
         this.тO = var3;
         this.т7 = var4;
         this.тР = var5;
         this.т3 = var6;
         this.В = new RoseBatchedItemAtlas.UvRegion(var3, var4, var5, var6);
      }

      private RoseBatchedItemAtlas.UvRegion У() {
         return this.В;
      }
   }

   private static final class ResolvedItemEntry {
      @SerializedName("В")
      private final RoseBatchedItemAtlas.ItemStackKey В_449c3792;
      @SerializedName("В")
      private RoseBatchedItemAtlas.AtlasSlot В_4249d664;
      private long ЯС;
      private boolean ХЦ = true;

      private ResolvedItemEntry(RoseBatchedItemAtlas.ItemStackKey var1) {
         this.В_449c3792 = var1;
      }
   }

   public static final class ItemHandle {
      private final boolean сф;
      @SerializedName("В")
      private RoseBatchedItemAtlas В_5487a647;
      @SerializedName("4")
      private ItemStack field_4;
      @SerializedName("В")
      private RoseBatchedItemAtlas.ResolvedItemEntry В_9d93d7f4;

      public ItemHandle(boolean var1) {
         this.сф = var1;
      }

      public ItemHandle() {
         this(false);
      }
   }

   public record UvRegion(float u0, float vTop, float u1, float vBottom) {
   }

   private record PendingRender(TrackingItemStackRenderState state, RoseBatchedItemAtlas.AtlasSlot entry) {
   }
}
