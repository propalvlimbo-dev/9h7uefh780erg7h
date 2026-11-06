package sg.mixin;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.annotation.RequiresMods;
import sg.modules.impl.render.BetterMinecraft;
import sg.render.pipeline.RoseChunkOffsets;

@RequiresMods("sodium")
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.RenderSection", remap = false)
public abstract class SodiumRenderSectionAnimationMixin implements RoseChunkOffsets.SectionAccess {
   @Unique
   private BlockPos rose$blockOrigin;
   @Unique
   private RoseChunkOffsets.RegionAccess rose$animationRegion;

   @Shadow
   public abstract int getSectionIndex();

   @Shadow
   public abstract boolean isBuilt();

   @Shadow
   public abstract boolean isDisposed();

   @Inject(method = "<init>", at = @At("TAIL"))
   private void rose$trackSection(@Coerce Object var1, int var2, int var3, int var4, CallbackInfo var5) {
      this.rose$blockOrigin = new BlockPos(var2 << 4, var3 << 4, var4 << 4);
      this.rose$animationRegion = (RoseChunkOffsets.RegionAccess)var1;
      this.rose$animationRegion.rose$sections()[this.getSectionIndex()] = this;
      BetterMinecraft var6 = RoseChunkOffsets.Б();
      if (var6 != null && var6.isEnabled() && var6.chunkAnimationSetting.getValue()) {
         var6.chunkRevealAnimation.Б(this, this.rose$blockOrigin);
      }
   }

   @Inject(method = "delete", at = @At("TAIL"))
   private void rose$forgetSection(CallbackInfo var1) {
      if (this.rose$animationRegion != null) {
         RoseChunkOffsets.SectionAccess[] var2 = this.rose$animationRegion.rose$sections();
         if (var2[this.getSectionIndex()] == this) {
            var2[this.getSectionIndex()] = null;
         }
      }
   }

   @Override
   public BlockPos rose$origin() {
      return this.rose$blockOrigin;
   }

   @Override
   public int rose$index() {
      return this.getSectionIndex();
   }

   @Override
   public boolean rose$hasMesh() {
      return this.isBuilt() && !this.isDisposed();
   }
}
