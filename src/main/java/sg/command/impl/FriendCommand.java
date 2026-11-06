package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.command.argument.ListedStringArgumentType;
import sg.events.EventListener;
import sg.events.impl.ChatOpenedEvent;
import sg.network.ClientChat;
import sg.command.ClientCommand;
import sg.command.argument.PlayerNameArgumentType;
import sg.client.RoseClient;
import sg.modules.impl.init.FriendEntry;

public class FriendCommand extends ClientCommand {
   private final @com.google.gson.annotations.SerializedName("е")  ListedStringArgumentType friendNamesArgument = new ListedStringArgumentType();
   private final @com.google.gson.annotations.SerializedName("Х诶")  EventListener<ChatOpenedEvent> chatOpenedListener = this::refreshFriendSuggestions;
   private static final String COMMAND_NAME = "friend";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_NAME_ARGUMENT_NAME = "name";
   private static final String ADD_FAKE_NAME_ARGUMENT_NAME = "fakeName";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_NAME_ARGUMENT_NAME = "name";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String NAME_ARGUMENT_KEY = "name";
   private static final String FRIENDS_CLEARED_MESSAGE = "Очистил список друзей.";
   private static final String FRIEND_NAME_SEPARATOR = " -> ";
   private static final String DEFAULT_PROTECTED_NAME = "Protected";
   private static final String FRIEND_ENTRY_SEPARATOR = ", ";
   private static final String EMPTY_FRIEND_LIST_MESSAGE = "Список друзей пуст.";
   private static final String REMOVE_NAME_ARGUMENT_KEY = "name";
   private static final String ADD_FAKE_NAME_ARGUMENT_KEY = "fakeName";

   private void saveFriendsConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getFriendsConfig().save();
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(
            this.literal(ADD_LITERAL)
               .then(
                  this.argument(ADD_NAME_ARGUMENT_NAME, PlayerNameArgumentType.players(PlayerNameArgumentType.PlayerScope.ALL))
                     .executes(commandContext -> this.addFriend(commandContext, null))
                     .then(this.argument(ADD_FAKE_NAME_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> this.addFriend(commandContext, StringArgumentType.getString(commandContext, ADD_FAKE_NAME_ARGUMENT_KEY))))
               )
         )
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_NAME_ARGUMENT_NAME, this.friendNamesArgument).executes(commandContext -> {
            String playerName = commandContext.getArgument(REMOVE_NAME_ARGUMENT_KEY, String.class);
            RoseClient.getInstance().getServices().getFriends().removeIf(friendEntry -> friendEntry.lз().equalsIgnoreCase(playerName));
            this.saveFriendsConfig();
            ClientChat.З("Удалил из списка друзей игрока с ником: " + playerName);
            return 1;
         })))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> {
            StringBuilder friendList = new StringBuilder();

            for (FriendEntry friend : RoseClient.getInstance().getServices().getFriends()) {
               friendList.append(friend.lз()).append(FRIEND_NAME_SEPARATOR).append(friend.lк() ? friend.l3() : DEFAULT_PROTECTED_NAME);
               if (friend != RoseClient.getInstance().getServices().getFriends().getLast()) {
                  friendList.append(FRIEND_ENTRY_SEPARATOR);
               }
            }

            if (friendList.isEmpty()) {
               ClientChat.З(EMPTY_FRIEND_LIST_MESSAGE);
            } else {
               ClientChat.З(friendList.toString());
            }

            return 1;
         }))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> {
            RoseClient.getInstance().getServices().getFriends().clear();
            this.saveFriendsConfig();
            ClientChat.З(FRIENDS_CLEARED_MESSAGE);
            return 1;
         }));
   }

   public FriendCommand() {
      super(COMMAND_NAME);
   }

   public void refreshFriendSuggestions(ChatOpenedEvent event) {
      this.friendNamesArgument.setList(RoseClient.getInstance().getServices().getFriends().а());
   }

   private int addFriend(CommandContext<ClientSuggestionProvider> context, String displayName) {
      String playerName = context.getArgument(NAME_ARGUMENT_KEY, String.class);
      RoseClient.getInstance().getServices().getFriends().К(playerName, displayName);
      this.saveFriendsConfig();
      if (displayName != null && !displayName.isBlank()) {
         ClientChat.З("Добавил друга: " + playerName + " -> " + displayName);
      } else {
         ClientChat.З("Добавил друга: " + playerName + " -> Protected");
      }

      return 1;
   }
}
