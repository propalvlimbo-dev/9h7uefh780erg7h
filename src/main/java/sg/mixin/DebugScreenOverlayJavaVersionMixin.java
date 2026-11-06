package sg.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sg.ec.UnhookState;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayJavaVersionMixin {
   private static final String Ит = "Java:";
   private static final String Иа = "Java: 21";

   @ModifyVariable(method = "extractLines", at = @At("HEAD"), argsOnly = true)
   private List<String> rose$maskJavaVersion(List<String> var1) {
      if (UnhookState.ые() && var1 != null && !var1.isEmpty()) {
         ArrayList var2 = null;

         for (int var3 = 0; var3 < var1.size(); var3++) {
            String var4 = (String)var1.get(var3);
            if (var4 != null && var4.trim().startsWith(Ит)) {
               if (var2 == null) {
                  var2 = new ArrayList(var1);
               }

               var2.set(var3, Иа);
            }
         }

         return var2 == null ? var1 : var2;
      } else {
         return var1;
      }
   }
}
