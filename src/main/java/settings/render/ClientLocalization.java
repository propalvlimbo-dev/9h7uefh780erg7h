package settings.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sg.events.impl.TextSearchMatcher;
import sg.modules.impl.render.ClickGui;
import sg.client.RoseClient;

public final class ClientLocalization {
   public static final String RUSSIAN_LANGUAGE_LABEL = "Русский";
   public static final String ENGLISH_LANGUAGE_LABEL = "English";
   private static final Map<String, String> englishTranslations = new ConcurrentHashMap<>();
   private static volatile boolean englishEnabled;

   public static void replaceEnglishTranslations(Map<String, String> translations) {
      englishTranslations.clear();
      if (translations != null) {
         englishTranslations.putAll(translations);
      }
   }

   public static boolean isEnglishEnabled() {
      return englishEnabled;
   }

   public static boolean matchesSearch(String query, String candidateText) {
      return TextSearchMatcher.matches(query, candidateText);
   }

   public static boolean isRussianEnabled() {
      return !englishEnabled;
   }

   public static void refreshLanguageFromClickGui() {
      try {
         ClickGui clickGui = RoseClient.getInstance().getServices().getModules().getClickGui();
         englishEnabled = clickGui != null && clickGui.и诶();
      } catch (Throwable var1) {
         englishEnabled = false;
      }
   }

   public static String translate(String text) {
      if (text != null && !text.isEmpty() && englishEnabled) {
         String translatedText = englishTranslations.get(text);
         return translatedText != null ? translatedText : text;
      } else {
         return text;
      }
   }

   public static int getSearchRelevancePercent(String query, String... candidateTexts) {
      int searchScore = TextSearchMatcher.score(query, candidateTexts);
      if (searchScore < 0) {
         return 0;
      } else if (searchScore >= 1000) {
         return 100;
      } else if (searchScore >= 800) {
         return 80;
      } else {
         return searchScore >= 600 ? 60 : 40;
      }
   }

   public static void setEnglishEnabled(boolean enabled) {
      englishEnabled = enabled;
   }

   private ClientLocalization() {
   }

   public static String selectLocalizedText(String russianText, String englishText) {
      return englishEnabled ? englishText : russianText;
   }
}
