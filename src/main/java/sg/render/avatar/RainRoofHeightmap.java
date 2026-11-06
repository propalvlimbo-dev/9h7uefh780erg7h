package sg.render.avatar;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage.Format;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Arrays;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class RainRoofHeightmap implements AutoCloseable {
   private static final int Аы = 128;
   private final int[] 诶 = new int[16384];
   @SerializedName("1")
   private final int[] field_1 = new int[16384];
   @SerializedName("В")
   private final MutableBlockPos В_9ef22e40 = new MutableBlockPos();
   @SerializedName("В")
   private NativeImage В_a0966bf3;
   private GpuTexture Д;
   private GpuTextureView т;
   private GpuBuffer щ;
   private ClientLevel ь;
   private int Ай;
   private int АЖ;
   private int Ам;
   private long ЯЧ;
   private boolean ЯК;
   private static final int с5衣 = 32768;
   private static final int с5ш = 65535;
   private static final int с5И = 65535;
   private static final int с5ь = -16777216;
   private static final long с5ж = 100000000L;
   private static final double с5弟 = 128.0;
   private static final double с5б = 128.0;
   private static final double с5Ы = 128.0;
   private static final double с5l = 128.0;
   private static final float с58 = 0.0078125F;
   private static final String с55 = "RainRoof";
   private static final String с5в = "RoofSampler";
   private static final int с5ъ = Integer.MIN_VALUE;
   private static final int с5М = Integer.MIN_VALUE;
   private static final long с5Н = 16L;
   private static final String с5н = "Ambience rain roof uniforms";
   private static final String с5Ц = "Ambience rain heightmap";

   @Override
   public void close() {
      if (this.т != null) {
         this.т.close();
      }

      if (this.Д != null) {
         this.Д.close();
      }

      if (this.В_a0966bf3 != null) {
         this.В_a0966bf3.close();
      }

      if (this.щ != null && !this.щ.isClosed()) {
         this.щ.close();
      }

      this.т = null;
      this.Д = null;
      this.В_a0966bf3 = null;
      this.щ = null;
      this.ь = null;
      this.ЯК = false;
      this.ЯЧ = 0L;
      this.Ам = 0;
   }

   public void Ы(ClientLevel var1, Vec3 var2, CommandEncoder var3) {
      if (this.ь != var1) {
         this.close();
         this.ь = var1;
      }

      this.В4();
      int var4 = Mth.floor(var2.x) - 64;
      int var5 = Mth.floor(var2.z) - 64;
      long var6 = System.nanoTime();
      boolean var8 = var6 >= this.ЯЧ;
      if (!this.ЯК || var4 != this.Ай || var5 != this.АЖ || var8) {
         this.Ай = var4;
         this.АЖ = var5;

         for (int var9 = this.АЖ >> 4; var9 <= this.АЖ + 128 - 1 >> 4; var9++) {
            int var10 = Math.max(this.АЖ, var9 << 4);
            int var11 = Math.min(this.АЖ + 128, var9 + 1 << 4);

            for (int var12 = this.Ай >> 4; var12 <= this.Ай + 128 - 1 >> 4; var12++) {
               int var13 = Math.max(this.Ай, var12 << 4);
               int var14 = Math.min(this.Ай + 128, var12 + 1 << 4);
               boolean var15 = var1.hasChunkAt(this.В_9ef22e40.set(var12 << 4, 0, var9 << 4));

               for (int var16 = var10; var16 < var11; var16++) {
                  int var17 = var16 & 127;
                  boolean var18 = var8 && var17 / 32 == this.Ам;

                  for (int var19 = var13; var19 < var14; var19++) {
                     int var20 = var19 & 127;
                     int var21 = var17 * 128 + var20;
                     if (!this.ЯК || var18 || this.诶[var21] != var19 || this.field_1[var21] != var16) {
                        int var22 = var15 ? Mth.clamp(var1.getHeight(Types.MOTION_BLOCKING, var19, var16) + с5衣, 0, с5ш) : с5И;
                        this.В_a0966bf3.setPixel(var20, var17, с5ь | (var22 & 0xFF) << 16 | var22 >> 8 << 8);
                        this.诶[var21] = var19;
                        this.field_1[var21] = var16;
                     }
                  }
               }
            }
         }

         var3.writeToTexture(this.Д, this.В_a0966bf3);
         this.ЯК = true;
         if (var8) {
            this.Ам = this.Ам + 1 & 3;
            this.ЯЧ = var6 + с5ж;
         }
      }

      try (MemoryStack var25 = MemoryStack.stackPush()) {
         var3.writeToBuffer(
            this.щ.slice(),
            Std140Builder.onStack(var25, 16)
               .putVec4((float)(var2.x - Math.floor(var2.x / с5弟) * с5б), (float)(var2.z - Math.floor(var2.z / с5Ы) * с5l), (float)var2.y, с58)
               .get()
         );
      }
   }

   private void В4() {
      if (this.В_a0966bf3 == null) {
         Arrays.fill(this.诶, с5ъ);
         Arrays.fill(this.field_1, с5М);
         this.В_a0966bf3 = new NativeImage(Format.RGBA, 128, 128, false);
         this.Д = RenderSystem.getDevice().createTexture(() -> с5Ц, 5, GpuFormat.RGBA8_UNORM, 128, 128, 1, 1);
         this.т = RenderSystem.getDevice().createTextureView(this.Д);
         this.щ = RenderSystem.getDevice().createBuffer(() -> с5н, 136, с5Н);
      }
   }

   public void Ж(RenderPass var1) {
      var1.setUniform(с55, this.щ);
      var1.bindTexture(с5в, this.т, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
   }
}
