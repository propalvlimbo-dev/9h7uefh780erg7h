package sg.mixin;

import java.nio.file.Path;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sg.ec.UnhookState;
import sg.modules.impl.init.UnhookFileManager;

@Mixin(PackSelectionScreen.class)
public abstract class PackSelectionScreenFolderMixin {
   private static final String сюЮ = "resourcepacks";

   @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private static Path rose$redirectResourcePackFolder(Path var0) {
      if (UnhookState.ые() && var0 != null) {
         Path var1 = var0.getFileName();
         if (var1 != null && сюЮ.equalsIgnoreCase(var1.toString())) {
            Path var2 = UnhookFileManager.Д();
            return var2 == null ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }
}
