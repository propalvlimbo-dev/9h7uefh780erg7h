package sg.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import sg.config.JsonConfigFile;

public final class AccountStore extends JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("сl")  List<AccountStore.AccountEntry> accounts = new ArrayList<>();
   private static final String CONFIG_DIRECTORY_PATH = "rose/client";
   private static final String CONFIG_NAME = "accounts";
   private static final String WRITE_ACCOUNT_NAME_KEY = "name";
   private static final String WRITE_ACCOUNT_DATE_KEY = "date";
   private static final String WRITE_ACCOUNT_FAVORITE_KEY = "favorite";
   private static final String WRITE_ACCOUNTS_ARRAY_KEY = "accounts";
   private static final String READ_ACCOUNTS_PRESENT_KEY = "accounts";
   private static final String READ_ACCOUNTS_ARRAY_CHECK_KEY = "accounts";
   private static final String READ_ACCOUNTS_ARRAY_VALUE_KEY = "accounts";
   private static final String READ_ACCOUNT_NAME_PRESENT_KEY = "name";
   private static final String READ_ACCOUNT_DATE_PRESENT_KEY = "date";
   private static final String READ_ACCOUNT_NAME_VALUE_KEY = "name";
   private static final String READ_ACCOUNT_DATE_VALUE_KEY = "date";
   private static final String READ_ACCOUNT_FAVORITE_PRESENT_KEY = "favorite";
   private static final String READ_ACCOUNT_FAVORITE_VALUE_KEY = "favorite";

   public List<AccountStore.AccountEntry> getAccountsSnapshot() {
      return List.copyOf(this.accounts);
   }

   public void replaceAccounts(List<AccountStore.AccountEntry> accountEntries) {
      this.accounts.clear();
      this.accounts.addAll(accountEntries);
      this.save();
   }

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();
      JsonArray accountsJson = new JsonArray();

      for (AccountStore.AccountEntry accountEntry : this.accounts) {
         JsonObject accountJson = new JsonObject();
         accountJson.addProperty(WRITE_ACCOUNT_NAME_KEY, accountEntry.name());
         accountJson.addProperty(WRITE_ACCOUNT_DATE_KEY, accountEntry.date());
         accountJson.addProperty(WRITE_ACCOUNT_FAVORITE_KEY, accountEntry.favorite());
         accountsJson.add(accountJson);
      }

      configJson.add(WRITE_ACCOUNTS_ARRAY_KEY, accountsJson);
      this.writeJson(configJson);
   }

   public AccountStore() {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
   }

   @Override
   protected void loadContents() {
      this.accounts.clear();
      String fileContents = this.readFileContents();
      if (fileContents != null && !fileContents.isBlank()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();
         if (configJson.has(READ_ACCOUNTS_PRESENT_KEY) && configJson.get(READ_ACCOUNTS_ARRAY_CHECK_KEY).isJsonArray()) {
            for (JsonElement accountElement : configJson.getAsJsonArray(READ_ACCOUNTS_ARRAY_VALUE_KEY)) {
               if (accountElement.isJsonObject()) {
                  JsonObject accountJson = accountElement.getAsJsonObject();
                  if (accountJson.has(READ_ACCOUNT_NAME_PRESENT_KEY) && accountJson.has(READ_ACCOUNT_DATE_PRESENT_KEY)) {
                     String accountName = accountJson.get(READ_ACCOUNT_NAME_VALUE_KEY).getAsString();
                     String accountDate = accountJson.get(READ_ACCOUNT_DATE_VALUE_KEY).getAsString();
                     boolean favorite = accountJson.has(READ_ACCOUNT_FAVORITE_PRESENT_KEY) && accountJson.get(READ_ACCOUNT_FAVORITE_VALUE_KEY).getAsBoolean();
                     if (!accountName.isBlank()) {
                        this.accounts.add(new AccountStore.AccountEntry(accountName, accountDate, favorite));
                     }
                  }
               }
            }
         }
      }
   }

   public record AccountEntry(String name, String date, boolean favorite) {
   }
}
