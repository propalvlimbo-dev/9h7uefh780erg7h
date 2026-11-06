package sg.mixin;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.network.NameProtectionTextRewriter;

@Mixin(StringSplitter.class)
public abstract class StringSplitterNameProtectionMixin {
   @Inject(
      method = {
            "plainIndexAtWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)I",
            "findLineBreak(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)I"
      },
      at = @At("HEAD")
   )
   private void dimas$beginIndexOperation(String var1, int var2, Style var3, CallbackInfoReturnable<Integer> var4) {
      NameProtectionTextRewriter.жн();
   }

   @Inject(
      method = {
            "plainIndexAtWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)I",
            "findLineBreak(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)I"
      },
      at = @At("RETURN")
   )
   private void dimas$endIndexOperation(String var1, int var2, Style var3, CallbackInfoReturnable<Integer> var4) {
      NameProtectionTextRewriter.жЫ();
   }

   @Inject(
      method = {
            "plainHeadByWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)Ljava/lang/String;",
            "plainTailByWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)Ljava/lang/String;"
      },
      at = @At("HEAD")
   )
   private void dimas$beginSubstringOperation(String var1, int var2, Style var3, CallbackInfoReturnable<String> var4) {
      NameProtectionTextRewriter.жн();
   }

   @Inject(
      method = {
            "plainHeadByWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)Ljava/lang/String;",
            "plainTailByWidth(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)Ljava/lang/String;"
      },
      at = @At("RETURN")
   )
   private void dimas$endSubstringOperation(String var1, int var2, Style var3, CallbackInfoReturnable<String> var4) {
      NameProtectionTextRewriter.жЫ();
   }
}
