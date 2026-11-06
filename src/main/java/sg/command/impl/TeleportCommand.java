package sg.command.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerPositionCorrectionEvent;
import sg.events.EventListener;
import sg.events.impl.ClientTickEvent;
import sg.command.ClientCommand;
import sg.modules.impl.init.TeleportRouteController;
import sg.modules.impl.init.TeleportTargetResolver;

public final class TeleportCommand extends ClientCommand {
   private static final int Дъ = 29999984;
   @SerializedName("В")
   private final TeleportRouteController routeController;
   @SerializedName("В")
   private final TeleportTargetResolver targetResolver;
   private final @com.google.gson.annotations.SerializedName("Ха")  EventListener<ClientTickEvent> clientTickListener;
   private final @com.google.gson.annotations.SerializedName("ХЮ")  EventListener<PlayerPositionCorrectionEvent> positionCorrectionListener;
   private static final String COMMAND_NAME = "tp";
   private static final String STOP_LITERAL = "stop";
   private static final String RW_LITERAL = "rw";
   private static final String RW_PLAYER_NAME_ARGUMENT_NAME = "name";
   private static final String RW_X_ARGUMENT_NAME = "x";
   private static final String RW_Z_ARGUMENT_NAME = "z";
   private static final String PLAYER_NAME_ARGUMENT_NAME = "name";
   private static final String X_ARGUMENT_NAME = "x";
   private static final String Y_ARGUMENT_NAME = "y";
   private static final String Z_ARGUMENT_NAME = "z";
   private static final int MIN_INTEGER_HORIZONTAL_COORDINATE = -29999984;
   private static final int MAX_INTEGER_HORIZONTAL_COORDINATE = 29999984;
   private static final double MIN_DOUBLE_HORIZONTAL_COORDINATE = -2.9999984E7;
   private static final double MAX_DOUBLE_HORIZONTAL_COORDINATE = 2.9999984E7;
   private static final String X_ARGUMENT_KEY = "x";
   private static final String Y_ARGUMENT_KEY = "y";
   private static final String Z_ARGUMENT_KEY = "z";
   private static final String PLAYER_NAME_ARGUMENT_KEY = "name";
   private static final String RW_X_ARGUMENT_KEY = "x";
   private static final String RW_Z_ARGUMENT_KEY = "z";
   private static final String RW_PLAYER_NAME_ARGUMENT_KEY = "name";

   private int stopRoute() {
      this.routeController.ФТ();
      return 1;
   }

   private int moveToLoadedPlayer(String playerName) {
      Player targetPlayer = null;

      for (Player nearbyPlayer : Minecraft.getInstance().level.players()) {
         if (nearbyPlayer.getGameProfile().name().equalsIgnoreCase(playerName)) {
            targetPlayer = nearbyPlayer;
         }
      }

      if (targetPlayer != null) {
         for (int currentPositionPacketIndex = 0; currentPositionPacketIndex < 10; currentPositionPacketIndex++) {
            PacketEventBypass.А(
               new Pos(
                  Minecraft.getInstance().player.getX(),
                  Minecraft.getInstance().player.getY(),
                  Minecraft.getInstance().player.getZ(),
                  Minecraft.getInstance().player.onGround(),
                  Minecraft.getInstance().player.horizontalCollision
               )
            );
         }

         Vec3 targetPosition = targetPlayer.position();

         for (int destinationPacketIndex = 0; destinationPacketIndex < 10; destinationPacketIndex++) {
            PacketEventBypass.А(
               new Pos(targetPosition.x, targetPosition.y, targetPosition.z, Minecraft.getInstance().player.onGround(), Minecraft.getInstance().player.horizontalCollision)
            );
            Minecraft.getInstance().player.setPos(targetPosition.x, targetPosition.y, targetPosition.z);
         }
      }

      return 1;
   }

   private IntegerArgumentType integerHorizontalCoordinateArgument() {
      return IntegerArgumentType.integer(MIN_INTEGER_HORIZONTAL_COORDINATE, MAX_INTEGER_HORIZONTAL_COORDINATE);
   }

   private int moveToResolvedPlayerBlock(String playerName) {
      return this.moveToResolvedPosition(this.targetResolver.щ(playerName));
   }

   private int moveToCoordinates(double targetX, double targetY, double targetZ) {
      return this.moveToResolvedPosition(Optional.of(new Vec3(targetX, targetY, targetZ)));
   }

   private int moveToResolvedPosition(Optional<Vec3> resolvedPosition) {
      if (resolvedPosition.isEmpty()) {
         return 0;
      }

      Vec3 targetPosition = (Vec3)resolvedPosition.get();
      if (!this.targetResolver.Ф(targetPosition)) {
         return 0;
      }

      this.routeController.ФР();
      this.targetResolver.Ъ(targetPosition);
      return 1;
   }

   public TeleportCommand() {
      super(COMMAND_NAME);
      Minecraft minecraft = Minecraft.getInstance();
      this.routeController = new TeleportRouteController(minecraft);
      this.targetResolver = new TeleportTargetResolver(minecraft);
      this.clientTickListener = tickEvent -> this.routeController.Ф9();
      this.positionCorrectionListener = correctionEvent -> this.routeController.Фо();
   }

   private int startRoute(int targetX, int targetZ) {
      this.routeController.т(targetX, targetZ);
      return 1;
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(STOP_LITERAL).executes(commandContext -> this.stopRoute()))
         .then(
            this.literal(RW_LITERAL)
               .then(
                  this.argument(RW_PLAYER_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.targetResolver.У(suggestionsBuilder))
                     .executes(commandContext -> this.moveToResolvedPlayerBlock(StringArgumentType.getString(commandContext, RW_PLAYER_NAME_ARGUMENT_KEY)))
               )
               .then(
                  this.argument(RW_X_ARGUMENT_NAME, this.integerHorizontalCoordinateArgument())
                     .then(
                        this.argument(RW_Z_ARGUMENT_NAME, this.integerHorizontalCoordinateArgument())
                           .executes(commandContext -> this.startRoute(IntegerArgumentType.getInteger(commandContext, RW_X_ARGUMENT_KEY), IntegerArgumentType.getInteger(commandContext, RW_Z_ARGUMENT_KEY)))
                     )
               )
         )
         .then(
            this.argument(PLAYER_NAME_ARGUMENT_NAME, StringArgumentType.word())
               .suggests((commandContext, suggestionsBuilder) -> this.targetResolver.У(suggestionsBuilder))
               .executes(commandContext -> this.moveToLoadedPlayer(StringArgumentType.getString(commandContext, PLAYER_NAME_ARGUMENT_KEY)))
         )
         .then(
            this.argument(X_ARGUMENT_NAME, this.doubleHorizontalCoordinateArgument())
               .then(
                  this.argument(Y_ARGUMENT_NAME, DoubleArgumentType.doubleArg())
                     .then(
                        this.argument(Z_ARGUMENT_NAME, this.doubleHorizontalCoordinateArgument())
                           .executes(
                              commandContext -> this.moveToCoordinates(
                                 DoubleArgumentType.getDouble(commandContext, X_ARGUMENT_KEY), DoubleArgumentType.getDouble(commandContext, Y_ARGUMENT_KEY), DoubleArgumentType.getDouble(commandContext, Z_ARGUMENT_KEY)
                              )
                           )
                     )
               )
         );
   }

   private DoubleArgumentType doubleHorizontalCoordinateArgument() {
      return DoubleArgumentType.doubleArg(MIN_DOUBLE_HORIZONTAL_COORDINATE, MAX_DOUBLE_HORIZONTAL_COORDINATE);
   }
}
