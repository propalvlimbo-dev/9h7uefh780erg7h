package sg.render;

import java.util.Map;
import net.minecraft.resources.Identifier;

public record CosmeticDefinition(
   String name,
   String displayName,
   CosmeticCategory category,
   float scale,
   float x,
   float y,
   float z,
   Identifier texture,
   CosmeticModel model,
   CosmeticAnimationClip animation,
   CosmeticAttachmentPoint attach,
   Map<String, CosmeticAnimationClip> clips
) {
   public CosmeticAnimationClip clip(String clipName) {
      if (this.clips != null && !this.clips.isEmpty()) {
         CosmeticAnimationClip clip = this.clips.get(clipName);
         return clip != null ? clip : this.animation;
      } else {
         return this.animation;
      }
   }

   public HatDefinition preview() {
      return new HatDefinition(this.name, this.displayName, this.texture, this.model);
   }
}
