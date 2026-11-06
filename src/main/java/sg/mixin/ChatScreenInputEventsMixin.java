package sg.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.InputQuirks;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.UnhookState;
import sg.events.impl.ChatClosedEvent;
import sg.events.impl.ChatOpenedEvent;
import sg.ec.ChatMessageCopy;
import sg.client.RoseClient;
import sg.events.impl.ChatMouseButtonEvent;
import sg.modules.impl.init.FriendAliasCommands;
import sg.network.ScoreboardValueEditor;
import sg.render.WorldProjection;

@Mixin(ChatScreen.class)
public class ChatScreenInputEventsMixin {
   @Shadow
   protected EditBox input;

   @ModifyVariable(method = "handleChatInput", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private String replaceFriendAliasInServerTeleport(String var1) {
      return UnhookState.ые() ? var1 : FriendAliasCommands.х(var1);
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void onInit(CallbackInfo var1) {
      if (!UnhookState.ые()) {
         ChatOpenedEvent var2 = new ChatOpenedEvent();
         RoseClient.getInstance().getEventBus().post(var2);
      }
   }

   @Inject(
      method = "handleChatInput",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/ChatScreen;normalizeChatMessage(Ljava/lang/String;)Ljava/lang/String;",
         shift = Shift.AFTER
      ),
      cancellable = true
   )
   private void sendMessage(String var1, boolean var2, CallbackInfo var3) {
      var1 = ((ChatScreen)(Object)this).normalizeChatMessage(var1);
      if (!var1.isEmpty()) {
         if (RoseClient.getInstance().getServices().getUnhookFiles().ь(var1)) {
            var3.cancel();
         } else if (!RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
            if (var1.startsWith(RoseClient.getInstance().getServices().getCommands().getCommandPrefix().toString())) {
               if (var2) {
                  Minecraft.getInstance().gui.hud.getChat().addRecentChat(var1);
               }

               RoseClient.getInstance().getServices().getCommands().executeChatCommand(var1);
               var3.cancel();
            }
         }
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void mouseClicked(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!UnhookState.ые()) {
         Vector2f var4 = WorldProjection.guiToScreen(var1.x(), var1.y());
         ChatMouseButtonEvent var5 = new ChatMouseButtonEvent(var4.x, var4.y, var1.button(), var2);
         RoseClient.getInstance().getEventBus().post(var5);
         if (var5.isCancelled()) {
            var3.setReturnValue(true);
         } else {
            boolean var6 = var1.button() == 2;
            boolean var7 = var1.button() == 0 && (var1.buttonInfo().modifiers() & InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER) != 0;
            if ((var6 || var7) && ChatMessageCopy.дк()) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void keyPressed(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (!UnhookState.ые()) {
         if (ScoreboardValueEditor.п(var1.key())) {
            var2.setReturnValue(true);
         } else {
            if (var1.isCopy() && !this.hasChatFieldSelection() && ChatMessageCopy.дк()) {
               var2.setReturnValue(true);
            }
         }
      }
   }

   private boolean hasChatFieldSelection() {
      EditBox var1 = this.input;
      if (var1 == null) {
         return false;
      }

      String var2 = var1.getHighlighted();
      return var2 != null && !var2.isEmpty();
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void removed(CallbackInfo var1) {
      if (!UnhookState.ые()) {
         ChatClosedEvent var2 = new ChatClosedEvent();
         RoseClient.getInstance().getEventBus().post(var2);
      }
   }
}
