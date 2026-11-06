package dev.rose.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.rose.gui.ChatDecorator;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.ChatModule;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private List<GuiMessage> allMessages;
   @Shadow
   @Final
   private List<GuiMessage.Line> trimmedMessages;
   @Unique
   private String rose$lastText;
   @Unique
   private int rose$repeats = 1;

   @ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), argsOnly = true)
   private Component rose$decorate(Component message) {
      ChatModule chat = Modules.chat();
      if (chat != null && chat.isEnabled()) {
         String text = message.getString();
         Component result = message;
         if (chat.merge() && text.equals(this.rose$lastText) && !this.allMessages.isEmpty()) {
            this.rose$repeats++;
            this.allMessages.remove(0);
            if (!this.trimmedMessages.isEmpty()) {
               this.trimmedMessages.remove(0);

               while (!this.trimmedMessages.isEmpty() && !this.trimmedMessages.get(0).endOfEntry()) {
                  this.trimmedMessages.remove(0);
               }
            }

            result = message.copy().append(Component.literal(" ×" + this.rose$repeats).withStyle(ChatFormatting.GRAY));
         } else {
            this.rose$repeats = 1;
         }

         this.rose$lastText = text;
         return chat.decorate(result);
      } else {
         this.rose$lastText = null;
         return message;
      }
   }

   @ModifyArg(
      method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V"),
      index = 0
   )
   private ChatComponent.ChatGraphicsAccess rose$decorate(
      ChatComponent.ChatGraphicsAccess access,
      @Local(argsOnly = true) GuiGraphics g,
      @Local(argsOnly = true, ordinal = 0) int tick,
      @Local(argsOnly = true, ordinal = 0) boolean focused
   ) {
      ChatModule chat = Modules.chat();
      if (chat != null && (chat.glass() || chat.animation())) {
         float opacity = (float)((Double)this.minecraft.options.chatOpacity().get() * 0.9 + 0.1);
         return new ChatDecorator(access, g, chat, this.trimmedMessages, tick, focused, opacity);
      } else {
         return access;
      }
   }
}
