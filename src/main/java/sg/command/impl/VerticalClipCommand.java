package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.math.NumberUtils;
import sg.ec.MinecraftAccess;
import sg.command.ClientCommand;

public class VerticalClipCommand extends ClientCommand {
   private static final String COMMAND_NAME = "vclip";
   private static final String UP_DIRECTION = "up";
   private static final String DOWN_DIRECTION = "down";
   private static final float DISTANCE_COUNT_DIVISOR = 1000.0F;
   private static final String UP_LITERAL = "up";
   private static final String DOWN_LITERAL = "down";
   private static final String DISTANCE_ARGUMENT_NAME = "value";
   private static final String DISTANCE_ARGUMENT_KEY = "value";
   private static final String DOWN_CLIP_MODE = "down";
   private static final String UP_CLIP_MODE = "up";

   private float findVerticalPassageOffset(BlockPos origin, boolean scanUpward) {
      int initialOffset = scanUpward ? 1 : -1;
      int scanOffsetLimit = scanUpward ? MinecraftAccess.MINECRAFT_CLIENT.level.getMaxY() - origin.getY() : MinecraftAccess.MINECRAFT_CLIENT.level.getMinY() - origin.getY();
      int offsetStep = scanUpward ? 1 : -1;
      boolean passedBlockedSpace = false;

      for (int verticalOffset = initialOffset; scanUpward ? verticalOffset < scanOffsetLimit : verticalOffset > scanOffsetLimit; verticalOffset += offsetStep) {
         BlockPos feetPosition = origin.offset(0, verticalOffset, 0);
         BlockPos headPosition = feetPosition.above();
         BlockState feetBlockState = MinecraftAccess.MINECRAFT_CLIENT.level.getBlockState(feetPosition);
         BlockState headBlockState = MinecraftAccess.MINECRAFT_CLIENT.level.getBlockState(headPosition);
         if (!scanUpward && feetBlockState.is(Blocks.BEDROCK)) {
            return 0.0F;
         }

         boolean feetSpaceIsAir = feetBlockState.isAir();
         boolean headSpaceIsAir = headBlockState.isAir();
         if (!feetSpaceIsAir) {
            passedBlockedSpace = true;
         } else if (!headSpaceIsAir) {
            passedBlockedSpace = true;
         } else if (passedBlockedSpace) {
            return verticalOffset;
         }
      }

      return 0.0F;
   }

   private void applyVerticalOffset(float verticalOffset, boolean var2) {
      int unusedDistanceDerivedCount = this.calculateDistanceDerivedCount(verticalOffset);

      for (int currentPositionPacketIndex = 0; currentPositionPacketIndex < 10; currentPositionPacketIndex++) {
         MinecraftAccess.MINECRAFT_CLIENT
            .player
            .connection
            .send(
               new Pos(
                  MinecraftAccess.MINECRAFT_CLIENT.player.getX(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.getY(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.getZ(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.onGround(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.horizontalCollision
               )
            );
      }

      double destinationY = MinecraftAccess.MINECRAFT_CLIENT.player.getY() + verticalOffset;

      for (int destinationPacketIndex = 0; destinationPacketIndex < 10; destinationPacketIndex++) {
         MinecraftAccess.MINECRAFT_CLIENT.player.setDeltaMovement(Vec3.ZERO);
         MinecraftAccess.MINECRAFT_CLIENT
            .player
            .connection
            .send(
               new Pos(
                  MinecraftAccess.MINECRAFT_CLIENT.player.getX(),
                  destinationY,
                  MinecraftAccess.MINECRAFT_CLIENT.player.getZ(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.onGround(),
                  MinecraftAccess.MINECRAFT_CLIENT.player.horizontalCollision
               )
            );
         MinecraftAccess.MINECRAFT_CLIENT.player.setPos(MinecraftAccess.MINECRAFT_CLIENT.player.getX(), destinationY, MinecraftAccess.MINECRAFT_CLIENT.player.getZ());
      }
   }

   public VerticalClipCommand() {
      super(COMMAND_NAME);
   }

   private int calculateDistanceDerivedCount(float verticalOffset) {
      return Math.max((int)(Math.abs(verticalOffset) / DISTANCE_COUNT_DIVISOR), 3);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(UP_LITERAL).executes(upContext -> {
         this.executeVerticalClip(UP_CLIP_MODE, 0.0F);
         return 1;
      })).then(this.literal(DOWN_LITERAL).executes(downContext -> {
         this.executeVerticalClip(DOWN_CLIP_MODE, 0.0F);
         return 1;
      })).then(this.argument(DISTANCE_ARGUMENT_NAME, IntegerArgumentType.integer()).executes(distanceContext -> {
         int requestedOffset = distanceContext.getArgument(DISTANCE_ARGUMENT_KEY, Integer.class);
         this.executeVerticalClip("", requestedOffset);
         return 1;
      }));
   }

   private float parseVerticalOffsetOrZero(String offsetText) {
      return NumberUtils.isParsable(offsetText) ? Float.parseFloat(offsetText) : 0.0F;
   }

   private void executeVerticalClip(String direction, float requestedOffset) {
      if (MinecraftAccess.MINECRAFT_CLIENT.player != null && MinecraftAccess.MINECRAFT_CLIENT.level != null) {
         BlockPos origin = MinecraftAccess.MINECRAFT_CLIENT.player.blockPosition();

         float verticalOffset = switch (direction) {
            case UP_DIRECTION -> this.findVerticalPassageOffset(origin, true);
            case DOWN_DIRECTION -> this.findVerticalPassageOffset(origin, false);
            default -> requestedOffset;
         };
         if (verticalOffset != 0.0F) {
            this.applyVerticalOffset(verticalOffset, false);
         }
      }
   }
}
