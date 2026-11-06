package sg.command.impl;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.command.ClientCommand;
import sg.ec.MinecraftAccess;

public class HorizontalClipCommand extends ClientCommand {
   private static final double Я5 = 100.0;
   private static final double Яв = 0.01;
   private static final String COMMAND_NAME = "hclip";
   private static final String DISTANCE_ARGUMENT_NAME = "distance";
   private static final double MAX_DISTANCE = 10000.0;
   private static final String DISTANCE_ARGUMENT_KEY = "distance";

   public HorizontalClipCommand() {
      super(COMMAND_NAME);
   }

   private void setHorizontalPosition(double targetX, double targetZ) {
      if (MinecraftAccess.MINECRAFT_CLIENT.player != null) {
         if (Double.isFinite(targetX) && Double.isFinite(targetZ)) {
            MinecraftAccess.MINECRAFT_CLIENT.player.setPos(targetX, MinecraftAccess.MINECRAFT_CLIENT.player.getY(), targetZ);
         }
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.argument(DISTANCE_ARGUMENT_NAME, DoubleArgumentType.doubleArg(1.0, MAX_DISTANCE)).executes(commandContext -> {
         if (MinecraftAccess.MINECRAFT_CLIENT.player == null) {
            return 0;
         }

         double distance = DoubleArgumentType.getDouble(commandContext, DISTANCE_ARGUMENT_KEY);
         if (!Double.isFinite(distance)) {
            return 0;
         }

         double yawRadians = Math.toRadians(MinecraftAccess.MINECRAFT_CLIENT.player.getYRot());
         double targetX = MinecraftAccess.MINECRAFT_CLIENT.player.getX() - Math.sin(yawRadians) * distance;
         double targetZ = MinecraftAccess.MINECRAFT_CLIENT.player.getZ() + Math.cos(yawRadians) * distance;
         this.setHorizontalPosition(targetX, targetZ);
         return 1;
      }));
   }
}
