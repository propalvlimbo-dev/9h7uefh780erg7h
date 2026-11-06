package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class HitMarker {
   @SerializedName("6")
   public final Vec3 field_6;
   public final long Вц;
   public final long Вя;
   public final boolean Я3;
   @SerializedName("4l")
   public final float field_4l;
   public final int АЩ;
   @SerializedName("48")
   public float field_48;
   public boolean Яр;

   public HitMarker(Vec3 var1, long var2, long var4, boolean var6, float var7, int var8) {
      this.field_6 = var1;
      this.Вц = var2;
      this.Вя = var4;
      this.Я3 = var6;
      this.field_4l = var7;
      this.АЩ = var8;
   }

   public float ж(long var1) {
      return Mth.clamp((float)(var1 - this.Вц) / (float)this.Вя, 0.0F, 1.0F);
   }

   public boolean ы(long var1) {
      return var1 - this.Вц > this.Вя;
   }
}
