package sg.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sg.network.ScoreboardValueEditor;
import sg.network.StyledComponentCache;
import sg.network.NameProtectionTextRewriter;

@Mixin(Font.class)
public class FontNameProtectionCacheMixin {
   @Unique
   private final StyledComponentCache<Integer> dimas$scoreboardWidths = new StyledComponentCache<>(128);

   @WrapMethod(method = "width(Lnet/minecraft/network/chat/FormattedText;)I")
   private int dimas$cacheScoreboardWidth(FormattedText var1, Operation<Integer> var2) {
      if (NameProtectionTextRewriter.ДМ() && !NameProtectionTextRewriter.Д8() && var1 instanceof Component var3) {
         Component var4 = ScoreboardValueEditor.method_9_f022d65c(NameProtectionTextRewriter.И(var3));
         return this.dimas$scoreboardWidths.I(var4, var1x -> {
            NameProtectionTextRewriter.жц();

            try {
               return (Integer)var2.call(var1x);
            } finally {
               NameProtectionTextRewriter.жР();
            }
         });
      } else {
         return (Integer)var2.call(var1);
      }
   }

   @ModifyVariable(
      method = {
            "width(Lnet/minecraft/network/chat/FormattedText;)I",
            "wordWrapHeight(Lnet/minecraft/network/chat/FormattedText;I)I",
            "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;",
            "splitIgnoringLanguage(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"
      },
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private FormattedText protectCompleteText(FormattedText var1) {
      if (NameProtectionTextRewriter.Д9() || NameProtectionTextRewriter.Д8()) {
         return var1;
      }

      if (var1 instanceof Component var2) {
         if (NameProtectionTextRewriter.ДМ()) {
            return ScoreboardValueEditor.method_9_f022d65c(NameProtectionTextRewriter.И(var2));
         } else {
            return ScoreboardValueEditor.цн() ? ScoreboardValueEditor.method_9_f022d65c(NameProtectionTextRewriter.Ф(var2)) : NameProtectionTextRewriter.Е(var2);
         }
      } else {
         return var1;
      }
   }
}
