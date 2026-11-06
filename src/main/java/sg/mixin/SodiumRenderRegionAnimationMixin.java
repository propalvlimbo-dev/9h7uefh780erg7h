package sg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import sg.annotation.RequiresMods;
import sg.render.pipeline.RoseChunkOffsets;

@RequiresMods("sodium")
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion", remap = false)
public class SodiumRenderRegionAnimationMixin implements RoseChunkOffsets.RegionAccess {
   @Unique
   private final RoseChunkOffsets.SectionAccess[] rose$animatedSections = new RoseChunkOffsets.SectionAccess[256];

   @Override
   public RoseChunkOffsets.SectionAccess[] rose$sections() {
      return this.rose$animatedSections;
   }
}
