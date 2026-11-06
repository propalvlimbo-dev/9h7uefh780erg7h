package sg.gui.manager.now;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AccountEntry {
    private String name;
    private final long timestamp;
    private UUID uuid;

    public AccountEntry(String name) {
        this(name, getUUID(name));
    }

    public AccountEntry(String name, UUID uuid) {
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.uuid = uuid != null ? uuid : getUUID(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.uuid = getUUID(name);
    }

    public String formatText4() {
        return name;
    }

    public long computeLong() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static UUID getUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }
}
