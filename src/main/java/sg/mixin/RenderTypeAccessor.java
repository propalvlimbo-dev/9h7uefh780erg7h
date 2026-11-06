package sg.mixin;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
   @Invoker("create")
   static RenderType create(String var0, RenderSetup var1) {
      throw new AssertionError();
   }

   @Accessor("state")
   RenderSetup getRenderSetup();
}
