package sg.mixin;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.network.NameProtectionTextRewriter;

@Mixin(StringDecomposer.class)
public class StringDecomposerNameProtectionMixin {
   private static final ThreadLocal<Boolean> REPLACING = ThreadLocal.withInitial(() -> false);

   @Inject(
      method = "iterate(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void protectForwards(String var0, Style var1, FormattedCharSink var2, CallbackInfoReturnable<Boolean> var3) {
      if (canReplace()) {
         String var4 = NameProtectionTextRewriter.лШ(var0);
         if (!var4.equals(var0)) {
            REPLACING.set(true);

            try {
               var3.setReturnValue(StringDecomposer.iterate(var4, var1, var2));
            } finally {
               REPLACING.remove();
            }
         }
      }
   }

   @Inject(
      method = "iterateBackwards(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void protectBackwards(String var0, Style var1, FormattedCharSink var2, CallbackInfoReturnable<Boolean> var3) {
      if (canReplace()) {
         String var4 = NameProtectionTextRewriter.лШ(var0);
         if (!var4.equals(var0)) {
            REPLACING.set(true);

            try {
               var3.setReturnValue(StringDecomposer.iterateBackwards(var4, var1, var2));
            } finally {
               REPLACING.remove();
            }
         }
      }
   }

   @Inject(
      method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void protectFormatted(String var0, int var1, Style var2, FormattedCharSink var3, CallbackInfoReturnable<Boolean> var4) {
      if (canReplace() && var1 == 0) {
         String var5 = NameProtectionTextRewriter.лШ(var0);
         if (!var5.equals(var0)) {
            REPLACING.set(true);

            try {
               var4.setReturnValue(StringDecomposer.iterateFormatted(var5, var1, var2, var3));
            } finally {
               REPLACING.remove();
            }
         }
      }
   }

   @Inject(
      method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void protectName(String var0, int var1, Style var2, Style var3, FormattedCharSink var4, CallbackInfoReturnable<Boolean> var5) {
      if (canReplace() && var1 == 0) {
         String var6 = NameProtectionTextRewriter.лШ(var0);
         if (!var6.equals(var0)) {
            REPLACING.set(true);

            try {
               var5.setReturnValue(StringDecomposer.iterateFormatted(var6, var1, var2, var3, var4));
            } finally {
               REPLACING.remove();
            }
         }
      }
   }

   private static boolean canReplace() {
      return !NameProtectionTextRewriter.Д8() && !REPLACING.get() && !NameProtectionTextRewriter.Д7() && NameProtectionTextRewriter.Д2();
   }
}
