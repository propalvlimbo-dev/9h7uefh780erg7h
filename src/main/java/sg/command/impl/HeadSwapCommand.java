package sg.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.item.ItemStack;
import sg.modules.impl.misc.HeadSwap;
import sg.client.RoseClient;
import sg.network.ClientChat;
import sg.command.ClientCommand;
import sg.ec.MinecraftAccess;

public final class HeadSwapCommand extends ClientCommand implements MinecraftAccess {
   private static final String COMMAND_NAME = "hs";
   private static final String COMMAND_ALIAS_HEADSWAP = "headswap";
   private static final String ADD_LITERAL = "add";
   private static final String REMOVE_LITERAL = "remove";
   private static final String CLEAR_LITERAL = "clear";
   private static final String LIST_LITERAL = "list";
   private static final String EMPTY_HAND_MESSAGE = "Возьми предмет в руку";
   private static final String EMPTY_HEAD_SWAP_MESSAGE = "HeadSwap: список пуст";
   private static final String HEAD_SWAP_NOT_SET_MESSAGE = "HeadSwap: предмет не добавлен. .hs add";

   private int showSelectedItem() {
      HeadSwap headSwap = this.getHeadSwap();
      if (!headSwap.ЧЙ()) {
         ClientChat.З(HEAD_SWAP_NOT_SET_MESSAGE);
         return 1;
      } else {
         ClientChat.З("HeadSwap: " + headSwap.Лш());
         return 1;
      }
   }

   private HeadSwap getHeadSwap() {
      return RoseClient.getInstance().getServices().getModules().getHeadSwap();
   }

   private int setItemFromMainHand() {
      if (MINECRAFT_CLIENT.player == null) {
         return 1;
      }

      ItemStack heldStack = MINECRAFT_CLIENT.player.getMainHandItem();
      if (heldStack.isEmpty()) {
         ClientChat.З(EMPTY_HAND_MESSAGE);
         return 1;
      }

      HeadSwap headSwap = this.getHeadSwap();
      String previousItemName = headSwap.ЧЙ() ? headSwap.Лш() : null;
      headSwap.method_9(heldStack);
      this.saveHeadSwapConfig();
      if (previousItemName != null && !previousItemName.equalsIgnoreCase(heldStack.getHoverName().getString())) {
         ClientChat.З("HeadSwap: заменён " + previousItemName + " → " + heldStack.getHoverName().getString());
      } else {
         ClientChat.З("HeadSwap: записан " + heldStack.getHoverName().getString());
      }

      return 1;
   }

   private void saveHeadSwapConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getHeadSwapConfig().save();
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.showSelectedItem()))
         .then(this.literal(ADD_LITERAL).executes(commandContext -> this.setItemFromMainHand()))
         .then(this.literal(REMOVE_LITERAL).executes(commandContext -> this.clearSelectedItem()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearSelectedItem()))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.showSelectedItem()));
   }

   public HeadSwapCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_HEADSWAP);
   }

   private int clearSelectedItem() {
      HeadSwap headSwap = this.getHeadSwap();
      if (!headSwap.ЧЙ()) {
         ClientChat.З(EMPTY_HEAD_SWAP_MESSAGE);
         return 1;
      } else {
         String previousItemName = headSwap.Лш();
         headSwap.лк();
         this.saveHeadSwapConfig();
         ClientChat.З("HeadSwap: удалён " + previousItemName);
         return 1;
      }
   }
}
