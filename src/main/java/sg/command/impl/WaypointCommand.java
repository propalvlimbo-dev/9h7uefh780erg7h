package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import sg.ec.MinecraftAccess;
import sg.ec.Waypoint;
import sg.modules.impl.init.WaypointManager;
import sg.network.ClientChat;
import sg.command.ClientCommand;

public final class WaypointCommand extends ClientCommand implements MinecraftAccess {
   private static final String COMMAND_NAME = "way";
   private static final String COMMAND_ALIAS_WAYPOINT = "waypoint";
   private static final String COMMAND_ALIAS_WP = "wp";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_NAME_ARGUMENT_NAME = "name";
   private static final String ADD_X_ARGUMENT_NAME = "x";
   private static final String ADD_Y_ARGUMENT_NAME = "y";
   private static final String ADD_Z_ARGUMENT_NAME = "z";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_NAME_ARGUMENT_NAME = "name";
   private static final String DELETE_LITERAL = "delete";
   private static final String DELETE_NAME_ARGUMENT_NAME = "name";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String CLEARALL_LITERAL = "clearall";
   private static final String EMPTY_WAYPOINT_LIST_MESSAGE = "Список точек пуст";
   private static final String WAYPOINT_LIST_HEADER = "Точки:";
   private static final String DELETE_NAME_KEY = "name";
   private static final String REMOVE_NAME_KEY = "name";
   private static final String ADD_COORDINATES_NAME_KEY = "name";
   private static final String ADD_X_KEY = "x";
   private static final String ADD_Y_KEY = "y";
   private static final String ADD_Z_KEY = "z";
   private static final String ADD_CURRENT_POSITION_NAME_KEY = "name";

   private int clearWaypoints() {
      ClientChat.З("Удалено точек: " + WaypointManager.method__()._Г());
      return 1;
   }

   private int removeWaypoint(String name) {
      ClientChat.З(WaypointManager.method__().З_bbeebd87(name) ? "Точка " + name + " удалена" : "Точка " + name + " не найдена");
      return 1;
   }

   private int listWaypoints() {
      if (MINECRAFT_CLIENT.player == null) {
         return 0;
      }

      if (WaypointManager.method__().О().isEmpty()) {
         ClientChat.З(EMPTY_WAYPOINT_LIST_MESSAGE);
         return 1;
      }

      ClientChat.З(WAYPOINT_LIST_HEADER);

      for (Waypoint waypoint : WaypointManager.method__().О()) {
         BlockPos position = waypoint.pos();
         long distanceMeters = Math.round(MINECRAFT_CLIENT.player.position().distanceTo(Vec3.atCenterOf(position)));
         ClientChat.З(waypoint.name() + " — " + position.getX() + " " + position.getY() + " " + position.getZ() + " — " + distanceMeters + "м");
      }

      return 1;
   }

   public WaypointCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_WAYPOINT, COMMAND_ALIAS_WP);
   }

   private int addWaypoint(String name, BlockPos position) {
      if (MINECRAFT_CLIENT.player == null) {
         return 0;
      } else if (!WaypointManager.method__().method__(name, position)) {
         ClientChat.З("Точка " + name + " уже существует");
         return 1;
      } else {
         ClientChat.З("Точка " + name + " добавлена: " + position.getX() + " " + position.getY() + " " + position.getZ());
         return 1;
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.listWaypoints()))
         .then(
            this.literal(ADD_LITERAL)
               .then(
                  this.argument(ADD_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .executes(commandContext -> this.addWaypoint(StringArgumentType.getString(commandContext, ADD_CURRENT_POSITION_NAME_KEY), MINECRAFT_CLIENT.player.blockPosition()))
                     .then(
                        this.argument(ADD_X_ARGUMENT_NAME, IntegerArgumentType.integer())
                           .then(
                              this.argument(ADD_Y_ARGUMENT_NAME, IntegerArgumentType.integer())
                                 .then(
                                    this.argument(ADD_Z_ARGUMENT_NAME, IntegerArgumentType.integer())
                                       .executes(
                                          commandContext -> this.addWaypoint(
                                             StringArgumentType.getString(commandContext, ADD_COORDINATES_NAME_KEY),
                                             new BlockPos(
                                                IntegerArgumentType.getInteger(commandContext, ADD_X_KEY),
                                                IntegerArgumentType.getInteger(commandContext, ADD_Y_KEY),
                                                IntegerArgumentType.getInteger(commandContext, ADD_Z_KEY)
                                             )
                                          )
                                       )
                                 )
                           )
                     )
               )
         )
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_NAME_ARGUMENT_NAME, StringArgumentType.word()).executes(commandContext -> this.removeWaypoint(StringArgumentType.getString(commandContext, REMOVE_NAME_KEY)))))
         .then(this.literal(DELETE_LITERAL).then(this.argument(DELETE_NAME_ARGUMENT_NAME, StringArgumentType.word()).executes(commandContext -> this.removeWaypoint(StringArgumentType.getString(commandContext, DELETE_NAME_KEY)))))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listWaypoints()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearWaypoints()))
         .then(this.literal(CLEARALL_LITERAL).executes(commandContext -> this.clearWaypoints()));
   }
}
