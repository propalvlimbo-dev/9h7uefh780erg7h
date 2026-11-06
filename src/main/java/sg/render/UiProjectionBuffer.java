package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;

public final class UiProjectionBuffer implements AutoCloseable {
   @SerializedName("В")
   private final Projection В_c515bcd2 = new Projection();
   @SerializedName("В")
   private final ProjectionMatrixBuffer В_1125ba39;
   private final float ея;
   private final float Ас;
   private final boolean Яс;

   public UiProjectionBuffer(String var1, float var2, float var3, boolean var4) {
      this.В_1125ba39 = new ProjectionMatrixBuffer(var1);
      this.ея = var2;
      this.Ас = var3;
      this.Яс = var4;
   }

   @Override
   public void close() {
      this.В_1125ba39.close();
   }

   public GpuBufferSlice а(float var1, float var2) {
      this.В_c515bcd2.setupOrtho(this.ея, this.Ас, var1, var2, this.Яс);
      return this.В_1125ba39.getBuffer(this.В_c515bcd2);
   }
}
