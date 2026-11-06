package sg.integration.discord.ipc;

public enum DiscordIpcOpcode {
   Handshake,
   Frame,
   Close,
   Ping,
   Pong;

   private static final DiscordIpcOpcode[] VALUES_BY_ORDINAL = values();

   public static DiscordIpcOpcode fromOrdinal(int ordinal) {
      return VALUES_BY_ORDINAL[ordinal];
   }
}
