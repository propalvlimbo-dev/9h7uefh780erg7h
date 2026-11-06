package sg.render.avatar;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import sg.util.crypto.EncryptedAssetContainer;

public final class AsyncTextureAtlas {
   private static final int Вl = 1;
   private final Identifier 衣;
   private final int В8;
   @SerializedName("В")
   private final DynamicTexture В_be9bcd64;
   private final Map<Identifier, AsyncTextureAtlas.AtlasRegion> о = new HashMap<>();
   @SerializedName("1")
   private final Set<Identifier> field_1 = ConcurrentHashMap.newKeySet();
   private final Set<Identifier> Ь = ConcurrentHashMap.newKeySet();
   private final Queue<AsyncTextureAtlas.PendingImage> А = new ConcurrentLinkedQueue<>();
   @SerializedName("В")
   private final ExecutorService В_11dd5828 = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1x = new Thread(var0, ВкШ);
      var1x.setDaemon(true);
      return var1x;
   });
   private int В5 = 1;
   private int Вв = 1;
   private int Въ;
   private static final String Вк_ = "Atlas full";
   private static final String ВкШ = "Atlas-Loader";

   private AsyncTextureAtlas.AtlasPosition Ы(int var1, int var2) {
      if (this.В5 + var1 + 1 > this.В8) {
         this.В5 = 1;
         this.Вв = this.Вв + this.Въ + 1;
         this.Въ = 0;
      }

      if (this.Вв + var2 + 1 > this.В8) {
         throw new IllegalStateException(Вк_);
      }

      AsyncTextureAtlas.AtlasPosition var3 = new AsyncTextureAtlas.AtlasPosition(this.В5, this.Вв);
      this.В5 += var1 + 1;
      this.Въ = Math.max(this.Въ, var2);
      return var3;
   }

   public Identifier Ы_c635a640() {
      return this.衣;
   }

   public AsyncTextureAtlas.AtlasRegion Ы(Identifier var1) {
      AsyncTextureAtlas.AtlasRegion var2 = this.о.get(var1);
      if (var2 != null || this.Ь.contains(var1)) {
         return var2;
      }

      if (!this.field_1.add(var1)) {
         return null;
      }

      ResourceManager var3 = Minecraft.getInstance().getResourceManager();
      this.В_11dd5828.execute(() -> {
         try (
            InputStream var3x = var3.open(var1);
            InputStream var4 = EncryptedAssetContainer.л_5c929562(var3x, var1.toString());
         ) {
            this.А.add(new AsyncTextureAtlas.PendingImage(var1, NativeImage.read(var4)));
         } catch (Exception var11) {
            this.Ь.add(var1);
            this.field_1.remove(var1);
         }
      });
      return null;
   }

   public AsyncTextureAtlas(Identifier var1, int var2) {
      this.衣 = var1;
      this.В8 = var2;
      this.В_be9bcd64 = new DynamicTexture(var1.toString(), var2, var2, false);
      NativeImage var3 = this.В_be9bcd64.getPixels();
      if (var3 != null) {
         var3.fillRect(0, 0, var2, var2, 0);
      }

      this.В_be9bcd64.upload();
      Minecraft.getInstance().getTextureManager().register(var1, this.В_be9bcd64);
   }

   public DynamicTexture Ы_be9bcd64() {
      return this.В_be9bcd64;
   }

   public void ЫН() {
      this.В_11dd5828.shutdownNow();

      AsyncTextureAtlas.PendingImage var1;
      while ((var1 = this.А.poll()) != null) {
         var1.image().close();
      }

      Minecraft.getInstance().getTextureManager().release(this.衣);
   }

   public void О(int var1) {
      if (!this.А.isEmpty()) {
         for (int var2 = 0; var2 < var1; var2++) {
            AsyncTextureAtlas.PendingImage var3 = this.А.poll();
            if (var3 == null) {
               return;
            }

            try (NativeImage var4 = var3.image()) {
               int var5 = var4.getWidth();
               int var6 = var4.getHeight();
               AsyncTextureAtlas.AtlasPosition var7 = this.Ы(var5, var6);
               RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.В_be9bcd64.getTexture(), var4, 0, 0, var7.x(), var7.y());
               this.о
                  .put(
                     var3.id(),
                     new AsyncTextureAtlas.AtlasRegion(
                        (float)var7.x() / this.В8,
                        (float)var7.y() / this.В8,
                        (float)(var7.x() + var5) / this.В8,
                        (float)(var7.y() + var6) / this.В8,
                        var5,
                        var6
                     )
                  );
            } catch (Exception var15) {
               this.Ь.add(var3.id());
            } finally {
               this.field_1.remove(var3.id());
            }
         }
      }
   }

   public record AtlasRegion(float u0, float v0, float u1, float v1, int width, int height) {
   }

   private record AtlasPosition(int x, int y) {
   }

   private record PendingImage(Identifier id, NativeImage image) {
   }
}
