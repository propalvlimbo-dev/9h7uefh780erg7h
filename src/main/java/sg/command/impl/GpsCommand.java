package sg.command.impl;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import sg.ec.MinecraftAccess;
import sg.command.argument.CoordinateArgumentType;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class GpsCommand extends ClientCommand implements ArgumentType<String>, MinecraftAccess {
   public static boolean gpsEnabled = false;
   public static double targetX = 0.0;
   public static double targetZ = 0.0;
   private static final String COMMAND_NAME = "gps";
   private static final String X_ARGUMENT_NAME = "x";
   private static final String Z_ARGUMENT_NAME = "z";
   private static final String OFF_LITERAL = "off";
   private static final String INFO_LITERAL = "info";
   private static final String GPS_DISABLED_MESSAGE = "Сейчас GPS отключен!";
   private static final String GPS_INFO_MESSAGE_PREFIX = "Информация о текущем GPS: ";
   private static final String X_COORDINATE_LABEL = "x: ";
   private static final String X_COORDINATE_FORMAT = "%.1f";
   private static final String X_DECIMAL_COMMA = ",";
   private static final String X_DECIMAL_POINT = ".";
   private static final String Z_COORDINATE_LABEL = " z: ";
   private static final String Z_COORDINATE_FORMAT = "%.1f";
   private static final String Z_DECIMAL_COMMA = ",";
   private static final String Z_DECIMAL_POINT = ".";
   private static final String X_ARGUMENT_KEY = "x";
   private static final String Z_ARGUMENT_KEY = "z";

   public GpsCommand() {
      super(COMMAND_NAME);
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      return reader.readString();
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(
         this.argument(X_ARGUMENT_NAME, CoordinateArgumentType.coordinate(CoordinateArgumentType.Axis.X))
            .then(this.argument(Z_ARGUMENT_NAME, CoordinateArgumentType.coordinate(CoordinateArgumentType.Axis.Z)).executes(commandContext -> {
               int requestedX = (Integer)commandContext.getArgument(X_ARGUMENT_KEY, Integer.class);
               int requestedZ = (Integer)commandContext.getArgument(Z_ARGUMENT_KEY, Integer.class);
               gpsEnabled = true;
               targetX = requestedX;
               targetZ = requestedZ;
               return 1;
            }))
      );
      commandBuilder.then(this.literal(OFF_LITERAL).executes(commandContext -> {
         gpsEnabled = false;
         targetX = 0.0;
         targetZ = 0.0;
         return 1;
      }));
      commandBuilder.then(this.literal(INFO_LITERAL).executes(commandContext -> {
         if (!gpsEnabled) {
            MutableComponent disabledMessage = Component.literal(GPS_DISABLED_MESSAGE).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY));
            ClientChat.З(disabledMessage);
         } else {
            MutableComponent infoPrefix = Component.literal(GPS_INFO_MESSAGE_PREFIX).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY));
            MutableComponent xLabel = Component.literal(X_COORDINATE_LABEL).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY));
            MutableComponent xValue = Component.literal(String.format(X_COORDINATE_FORMAT, targetX).replace(X_DECIMAL_COMMA, X_DECIMAL_POINT)).setStyle(Style.EMPTY.applyFormat(ChatFormatting.WHITE));
            MutableComponent zLabel = Component.literal(Z_COORDINATE_LABEL).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY));
            MutableComponent zValue = Component.literal(String.format(Z_COORDINATE_FORMAT, targetZ).replace(Z_DECIMAL_COMMA, Z_DECIMAL_POINT)).setStyle(Style.EMPTY.applyFormat(ChatFormatting.WHITE));
            ClientChat.З(Component.literal("").append(infoPrefix).append(xLabel).append(xValue).append(zLabel).append(zValue));
         }

         return 1;
      }));
   }

   private static class DoubleCoordinateArgument implements ArgumentType<Double> {
      public Double parse(StringReader reader) throws CommandSyntaxException {
         try {
            return reader.readDouble();
         } catch (CommandSyntaxException parseError) {
            throw new DynamicCommandExceptionType(invalidCoordinateValue -> Component.literal("Неверный формат координаты: " + invalidCoordinateValue)).create(reader.getString());
         }
      }
   }
}
