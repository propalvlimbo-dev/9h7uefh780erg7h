package sg.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sg.network.ScoreboardValueEditor;
import sg.network.NameProtectionTextRewriter;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorNameProtectionMixin {
   @ModifyVariable(method = "text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private Component protectCompleteText(Component var1) {
      if (!NameProtectionTextRewriter.Д9() && !NameProtectionTextRewriter.Д8()) {
         Component var2;
         if (NameProtectionTextRewriter.ДМ()) {
            var2 = var1;
         } else {
            var2 = NameProtectionTextRewriter.ДЗ() ? NameProtectionTextRewriter.Е(var1) : NameProtectionTextRewriter.Ф(var1);
         }

         return ScoreboardValueEditor.method_9_f022d65c(var2);
      } else {
         return var1;
      }
   }

   @ModifyVariable(method = "text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private String protectCompleteString(String var1) {
      if (!NameProtectionTextRewriter.Д9() && !NameProtectionTextRewriter.Д8()) {
         String var2 = NameProtectionTextRewriter.ДЗ() ? NameProtectionTextRewriter.лШ(var1) : NameProtectionTextRewriter.лO(var1);
         return ScoreboardValueEditor.ш(var2);
      } else {
         return var1;
      }
   }

   @ModifyVariable(
      method = "text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private FormattedCharSequence protectCompleteOrderedText(FormattedCharSequence var1) {
      if (!NameProtectionTextRewriter.Д9() && !NameProtectionTextRewriter.Д8()) {
         return NameProtectionTextRewriter.ДЗ() ? var1 : NameProtectionTextRewriter.Ь_f9278523(var1);
      } else {
         return var1;
      }
   }
}
