package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;
import sg.ec.TransientGpuUpload;
import sg.modules.impl.render.BetterMinecraft;
import sg.client.RoseClient;

public final class RoseChunkOffsets implements AutoCloseable {
   private static final String сЫ = "NowDlcChunkOffsets";
   public static final int Вд = 256;
   private static final int ВБ = 4096;
   @SerializedName("6")
   private final Map<RoseChunkOffsets.RegionAccess, GpuBufferSlice> field_6 = new IdentityHashMap<>();
   private final List<GpuBuffer> и = new ArrayList<>();
   private GpuBuffer ь;
   private int ВС;
   private BetterMinecraft В;
   private static final String Б9 = "NowDlcChunkOffsets";
   private static final String Бо = "OpenGL";
   private static final long БO = 4096L;
   private static final String Б7 = "sodium";
   private static final String БР = "blocks/block_layer_opaque";
   private static final String Б3 = "vec3 position = _vert_position + translation;";
   private static final String Бр = "void main() {";
   private static final String Б6 = "Unrecognized Sodium terrain shader for chunk animation";
   private static final String Б_ = "vec3 position = _vert_position + translation + rose_Offsets[_draw_id].xyz;";
   private static final String БШ = "NowDlc animated chunk offsets";
   private static final String Бы = "NowDlc chunk zero offsets";

   public void цз() {
      this.field_6.clear();
      this.ВС = 0;
      this.В = null;
   }

   public void ц衣() {
      if (this.ь == null || this.ь.isClosed()) {
         try (MemoryStack var1 = MemoryStack.stackPush()) {
            this.ь = RenderSystem.getDevice().createBuffer(() -> Бы, 128, var1.calloc(4096));
         }
      }

      BetterMinecraft var6 = Б();
      BetterMinecraft var2 = var6 != null && var6.isEnabled() && var6.chunkAnimationSetting.getValue() ? var6 : null;
      if (this.В != var2) {
         this.field_6.clear();
      }

      this.В = var2;
      if (var2 == null && var6 != null) {
         var6.chunkRevealAnimation.Б6();
      }
   }

   public static String Б(Identifier var0, ShaderType var1, String var2) {
      if (var2 != null && var1 == ShaderType.VERTEX && var0.getNamespace().equals(Б7) && var0.getPath().equals(БР)) {
         String var3 = Б3;
         String var4 = Бр;
         if (var2.contains(var3) && var2.contains(var4)) {
            return var2.replace(var4, "layout(std140) uniform NowDlcChunkOffsets { vec4 rose_Offsets[256]; };\n" + var4).replace(var3, Б_);
         } else {
            throw new IllegalStateException(Б6);
         }
      } else {
         return var2;
      }
   }

   public GpuBufferSlice Б(RoseChunkOffsets.RegionAccess var1) {
      return this.В == null ? this.ь.slice() : this.field_6.computeIfAbsent(var1, this::ц);
   }

   private GpuBufferSlice ц(RoseChunkOffsets.RegionAccess var1) {
      try (MemoryStack var2 = MemoryStack.stackPush()) {
         ByteBuffer var3 = var2.calloc(4096);
         boolean var4 = false;

         for (RoseChunkOffsets.SectionAccess var8 : var1.rose$sections()) {
            if (var8 != null && var8.rose$hasMesh()) {
               Vec3 var9 = this.В.chunkRevealAnimation.Б(var8, var8.rose$origin(), this.В);
               if (var9.x != 0.0 || var9.y != 0.0 || var9.z != 0.0) {
                  int var10 = var8.rose$index() * 16;
                  var3.putFloat(var10, (float)var9.x);
                  var3.putFloat(var10 + 4, (float)var9.y);
                  var3.putFloat(var10 + 8, (float)var9.z);
                  var4 = true;
               }
            }
         }

         if (!var4) {
            return this.ь.slice();
         }

         if (!Бо.equals(RenderSystem.getDevice().getDeviceInfo().backendName())) {
            return TransientGpuUpload.uploadUniform(var3);
         }

         if (this.ВС == this.и.size()) {
            this.и.add(RenderSystem.getDevice().createBuffer(() -> БШ, 136, БO));
         }

         GpuBuffer var13 = this.и.get(this.ВС++);
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(var13.slice(), var3);
         return var13.slice();
      }
   }

   public static BetterMinecraft Б() {
      RoseClient var0 = RoseClient.getInstance();
      return var0 != null && var0.getServices() != null && var0.getServices().getModules() != null ? var0.getServices().getModules().getBetterMinecraft() : null;
   }

   public static String Л诶() {
      return Б9;
   }

   @Override
   public void close() {
      this.цз();
      if (this.ь != null && !this.ь.isClosed()) {
         this.ь.close();
      }

      this.ь = null;

      for (GpuBuffer var2 : this.и) {
         if (!var2.isClosed()) {
            var2.close();
         }
      }

      this.и.clear();
   }

   public interface ChunkAnimationLifecycle {
      void rose$closeChunkAnimation();
   }

   public interface SectionAccess {
      int rose$index();

      boolean rose$hasMesh();

      BlockPos rose$origin();
   }

   public interface RegionAccess {
      RoseChunkOffsets.SectionAccess[] rose$sections();
   }
}
