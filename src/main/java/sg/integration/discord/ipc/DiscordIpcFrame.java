package sg.integration.discord.ipc;

import com.google.gson.JsonObject;

public record DiscordIpcFrame(DiscordIpcOpcode opcode, JsonObject data) {
}
