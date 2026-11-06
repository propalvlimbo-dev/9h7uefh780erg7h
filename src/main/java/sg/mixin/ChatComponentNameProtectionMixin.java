package sg.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ChatComponent.DisplayMode;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.ChatMessageCopy;
import sg.network.NameProtectionTextRewriter;

@Mixin(ChatComponent.class)
public class ChatComponentNameProtectionMixin {
   @ModifyVariable(
      method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private Component rose$protectWholeChatMessage(Component var1) {
      return NameProtectionTextRewriter.Е(var1);
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V",
      at = @At("HEAD")
   )
   private void rose$clearChatHover(GuiGraphicsExtractor var1, Font var2, int var3, int var4, int var5, DisplayMode var6, boolean var7, CallbackInfo var8) {
      ChatMessageCopy.Чэ();
   }
}
