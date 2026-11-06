package sg.gui.manager.now;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import sg.mixin.MinecraftUserAccessor;

/** Uses the base's existing account file and accessor instead of reflection or a separate store. */
public final class AccountStore {
    private static AccountStore instance;
    private final sg.account.AccountStore storage = new sg.account.AccountStore();
    private final ArrayList<AccountEntry> accounts = new ArrayList<>();
    private final HashMap<String, sg.account.AccountStore.AccountEntry> metadata = new HashMap<>();
    private AccountEntry active;
    private AccountStore() {
        storage.load();
        for (var entry : storage.getAccountsSnapshot()) {
            accounts.add(new AccountEntry(entry.name()));
            metadata.put(entry.name(), entry);
        }
        syncSession();
    }
    public static AccountStore getAccountStore() {
        if (instance == null) instance = new AccountStore();
        return instance;
    }
    private void syncSession() {
        String name = Minecraft.getInstance().getUser().getName();
        active = accounts.stream().filter(a -> a.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (active == null) active = new AccountEntry(name);
    }
    public AccountEntry getAccountEntry() { syncSession(); return active; }
    public void refresh() {
        storage.load();
        var saved = storage.getAccountsSnapshot();
        accounts.removeIf(a -> saved.stream().noneMatch(s -> s.name().equals(a.getName())));
        for (var entry : saved) {
            if (accounts.stream().noneMatch(a -> a.getName().equals(entry.name()))) accounts.add(new AccountEntry(entry.name()));
            metadata.put(entry.name(), entry);
        }
        syncSession();
    }
    public ArrayList<AccountEntry> getArrayList2(String search) {
        String query = search == null ? "" : search.toLowerCase(Locale.ROOT);
        return new ArrayList<>(accounts.stream().filter(a -> a.getName().toLowerCase(Locale.ROOT).contains(query)).toList());
    }
    private static String validName(String name) {
        String value = name == null ? "" : name.strip();
        return value.matches("[A-Za-z0-9_]{1,16}") ? value : null;
    }
    public AccountEntry getAccountEntry3(String name) {
        name = validName(name);
        if (name == null) return null;
        for (var entry : accounts) if (entry.getName().equalsIgnoreCase(name)) return entry;
        var entry = new AccountEntry(name);
        accounts.add(entry);
        save();
        return entry;
    }
    public void processData5(AccountEntry entry) {
        if (entry == null || validName(entry.getName()) == null || Minecraft.getInstance().level != null) return;
        active = entry;
        ((MinecraftUserAccessor) Minecraft.getInstance()).rose$setSession(
                new User(entry.getName(), entry.getUuid(), "", Optional.empty(), Optional.empty()));
    }
    public void processData(AccountEntry entry, String name) {
        name = validName(name);
        if (entry == null || name == null) return;
        for (var other : accounts) if (other != entry && other.getName().equalsIgnoreCase(name)) return;
        boolean selected = matchesCriteria(entry);
        var old = metadata.remove(entry.getName());
        entry.setName(name);
        if (old != null) metadata.put(name, new sg.account.AccountStore.AccountEntry(name, old.date(), old.favorite()));
        if (!accounts.contains(entry)) accounts.add(entry);
        if (selected) processData5(entry);
        save();
    }
    public void processData3(AccountEntry entry) {
        if (entry == null) return;
        accounts.remove(entry);
        metadata.remove(entry.getName());
        if (matchesCriteria(entry) && !accounts.isEmpty()) processData5(accounts.getFirst());
        save();
    }
    public boolean matchesCriteria(AccountEntry entry) {
        return entry != null && Minecraft.getInstance().getUser().getName().equalsIgnoreCase(entry.getName());
    }
    private void save() {
        storage.replaceAccounts(accounts.stream().map(entry -> metadata.getOrDefault(entry.getName(),
                new sg.account.AccountStore.AccountEntry(entry.getName(), LocalDate.now().toString(), false))).toList());
    }
}
