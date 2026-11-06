package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import sg.modules.impl.misc.AutoJoiner;
import sg.modules.impl.init.SupportedServer;

public final class ServerSelectionConfig extends JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("Ш")  Map<SupportedServer, ServerSelectionConfig.Selection> selectionsByServer = new EnumMap<>(SupportedServer.class);
   private static final String CONFIG_DIRECTORY = "rose/client";
   private static final String CONFIG_NAME = "servers";
   private static final String SAVE_GRIEF_NUMBER_KEY = "grief";
   private static final String SAVE_HOLY_CATEGORY_KEY = "category";
   private static final String SAVE_HOLY_NUMBER_KEY = "number";
   private static final String READ_GRIEF_NUMBER_KEY = "grief";
   private static final String READ_HOLY_CATEGORY_KEY = "category";
   private static final String READ_HOLY_NUMBER_KEY = "number";

   private static String readNonBlankString(JsonObject objectJson, String propertyKey) {
      if (objectJson.has(propertyKey) && !objectJson.get(propertyKey).isJsonNull()) {
         String propertyValue = objectJson.get(propertyKey).getAsString();
         return propertyValue != null && !propertyValue.isBlank() ? propertyValue : null;
      } else {
         return null;
      }
   }

   public void setGriefSelection(SupportedServer server, int griefNumber) {
      if (server != null && griefNumber >= 1) {
         ServerSelectionConfig.Selection previousSelection = this.getSelection(server);
         if (previousSelection.grief() != griefNumber || previousSelection.holyCategory() != null) {
            this.selectionsByServer.put(server, new ServerSelectionConfig.Selection(griefNumber, null, 0));
            this.save();
         }
      }
   }

   public ServerSelectionConfig.Selection getSelection(SupportedServer server) {
      return this.selectionsByServer.getOrDefault(server, ServerSelectionConfig.Selection.EMPTY);
   }

   private static int readIntOrZero(JsonObject objectJson, String propertyKey) {
      if (objectJson.has(propertyKey) && !objectJson.get(propertyKey).isJsonNull()) {
         JsonElement propertyJson = objectJson.get(propertyKey);

         try {
            return propertyJson.getAsInt();
         } catch (Exception integerConversionException) {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public void setHolySelection(SupportedServer server, AutoJoiner.HolyWorldSelection holySelection) {
      if (server != null && holySelection != null && holySelection.number() >= 1) {
         ServerSelectionConfig.Selection previousSelection = this.getSelection(server);
         if (!holySelection.category().name().equals(previousSelection.holyCategory()) || previousSelection.holyNumber() != holySelection.number()) {
            this.selectionsByServer.put(server, new ServerSelectionConfig.Selection(0, holySelection.category().name(), holySelection.number()));
            this.save();
         }
      }
   }

   public ServerSelectionConfig() {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
   }

   @Override
   protected void loadContents() {
      this.selectionsByServer.clear();
      String fileContents = this.readFileContents();
      if (fileContents != null && !fileContents.isBlank()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();

         for (SupportedServer server : SupportedServer.values()) {
            String serverKey = server.name().toLowerCase(Locale.ROOT);
            if (configJson.has(serverKey) && configJson.get(serverKey).isJsonObject()) {
               JsonObject selectionJson = configJson.getAsJsonObject(serverKey);
               int griefNumber = readIntOrZero(selectionJson, READ_GRIEF_NUMBER_KEY);
               String holyCategory = readNonBlankString(selectionJson, READ_HOLY_CATEGORY_KEY);
               int holyNumber = readIntOrZero(selectionJson, READ_HOLY_NUMBER_KEY);
               ServerSelectionConfig.Selection selection = new ServerSelectionConfig.Selection(griefNumber, holyCategory, holyNumber);
               if (selection.hasAny()) {
                  this.selectionsByServer.put(server, selection);
               }
            }
         }
      }
   }

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();

      for (Entry selectionEntry : this.selectionsByServer.entrySet()) {
         ServerSelectionConfig.Selection selection = (ServerSelectionConfig.Selection)selectionEntry.getValue();
         if (selection.hasAny()) {
            JsonObject selectionJson = new JsonObject();
            if (selection.hasGrief()) {
               selectionJson.addProperty(SAVE_GRIEF_NUMBER_KEY, selection.grief());
            }

            if (selection.hasHoly()) {
               selectionJson.addProperty(SAVE_HOLY_CATEGORY_KEY, selection.holyCategory());
               selectionJson.addProperty(SAVE_HOLY_NUMBER_KEY, selection.holyNumber());
            }

            configJson.add(((SupportedServer)selectionEntry.getKey()).name().toLowerCase(Locale.ROOT), selectionJson);
         }
      }

      this.writeJson(configJson);
   }

   public record Selection(int grief, String holyCategory, int holyNumber) {
      public static final ServerSelectionConfig.Selection EMPTY = new ServerSelectionConfig.Selection(0, null, 0);

      public boolean hasHoly() {
         return this.holyCategory != null && !this.holyCategory.isBlank() && this.holyNumber > 0;
      }

      private static String holyLabel(String categoryName) {
         return switch (categoryName.toUpperCase(Locale.ROOT)) {
            case "SOLO" -> "Соло";
            case "DUO" -> "Дуо";
            case "TRIO" -> "Трио";
            case "CLAN" -> "Клан";
            default -> categoryName;
         };
      }

      public boolean hasAny() {
         return this.hasGrief() || this.hasHoly();
      }

      public String label() {
         if (this.hasHoly()) {
            return holyLabel(this.holyCategory) + " #" + this.holyNumber;
         } else {
            return this.hasGrief() ? "Гриф #" + this.grief : "Пока не заходили";
         }
      }

      public boolean hasGrief() {
         return this.grief > 0;
      }
   }
}
