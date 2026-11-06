package dev.rose.config;

import dev.rose.RoseClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public final class BuildLinks {
   public static final String UPDATE_URL;
   public static final String OFFICIAL_PAGE;
   public static final String AUTHOR_PAGE;
   public static final String DISCORD_PAGE;

   private BuildLinks() {
   }

   public static String shortOfficial() {
      return shorten(OFFICIAL_PAGE);
   }

   public static List<String> all() {
      List<String> list = new ArrayList<>(3);

      for (String link : new String[]{OFFICIAL_PAGE, AUTHOR_PAGE, DISCORD_PAGE}) {
         if (!link.isEmpty()) {
            list.add(link);
         }
      }

      return list;
   }

   private static String shorten(String link) {
      return link.isEmpty() ? "" : link.substring("https://".length());
   }

   private static String clean(String value) {
      String trimmed = value == null ? "" : value.trim();
      return trimmed.startsWith("https://") ? trimmed : "";
   }

   static {
      Properties properties = new Properties();

      try (InputStream in = BuildLinks.class.getResourceAsStream("/rose-build.properties")) {
         if (in != null) {
            properties.load(in);
         }
      } catch (IOException e) {
         RoseClient.LOGGER.warn("[Rose] cannot read build links", e);
      }

      UPDATE_URL = clean(properties.getProperty("update_url"));
      OFFICIAL_PAGE = clean(properties.getProperty("official_page"));
      AUTHOR_PAGE = clean(properties.getProperty("author_page"));
      DISCORD_PAGE = clean(properties.getProperty("discord_page"));
   }
}
