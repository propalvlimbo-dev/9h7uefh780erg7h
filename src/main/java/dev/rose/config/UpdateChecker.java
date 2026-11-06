package dev.rose.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rose.RoseClient;
import dev.rose.notify.Notifications;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.time.Duration;
import java.util.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class UpdateChecker {
   private static volatile String latest;
   private static volatile String downloadUrl;
   private static boolean started;

   private UpdateChecker() {
   }

   public static void start(boolean allowed) {
      String address = BuildLinks.UPDATE_URL;
      if (!started && allowed && !address.isEmpty() && RoseClient.uiTest() == null) {
         started = true;
         HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();
         client.sendAsync(request(address), HttpResponse.BodyHandlers.ofByteArray())
            .thenCombine(client.sendAsync(request(address + ".sig"), HttpResponse.BodyHandlers.ofString()), (list, signature) -> {
               if (list.statusCode() != 200 || signature.statusCode() != 200) {
                  return null;
               } else if (!signedByAuthor(list.body(), signature.body())) {
                  RoseClient.LOGGER.warn("[Rose] update list signature is invalid: ignored");
                  return null;
               } else {
                  return new String(list.body(), StandardCharsets.UTF_8);
               }
            })
            .thenAccept(UpdateChecker::accept)
            .exceptionally(error -> {
               RoseClient.LOGGER.info("[Rose] update check skipped: {}", error.getMessage());
               return null;
            });
      }
   }

   private static HttpRequest request(String address) {
      return HttpRequest.newBuilder(URI.create(address)).timeout(Duration.ofSeconds(8L)).header("User-Agent", "Rose/" + RoseClient.version()).GET().build();
   }

   private static void accept(String body) {
      if (body != null) {
         JsonElement root = JsonParser.parseString(body);
         if (root.isJsonObject()) {
            JsonObject json = root.getAsJsonObject();
            String version = json.has("version") ? json.get("version").getAsString() : null;
            if (version != null && compare(version, RoseClient.version()) > 0) {
               String url = json.has("url") ? json.get("url").getAsString() : "";
               latest = version;
               downloadUrl = url.startsWith("https://") ? url : null;
               Minecraft.getInstance()
                  .execute(
                     () -> Notifications.show(
                        Notifications.Kind.INFO,
                        I18n.get("rose.update.title", new Object[]{version}),
                        I18n.get("rose.update.subtitle", new Object[0])
                     )
                  );
            }
         }
      }
   }

   private static boolean signedByAuthor(byte[] list, String signatureBase64) {
      try (InputStream in = UpdateChecker.class.getResourceAsStream("/assets/rose/official.cer")) {
         if (in == null) {
            return false;
         }

         Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(in);
         Signature verifier = Signature.getInstance("SHA256withRSA");
         verifier.initVerify(certificate.getPublicKey());
         verifier.update(list);
         return verifier.verify(Base64.getDecoder().decode(signatureBase64.trim()));
      } catch (GeneralSecurityException | IllegalArgumentException | IOException e) {
         return false;
      }
   }

   public static String latest() {
      return latest;
   }

   public static String downloadUrl() {
      return downloadUrl;
   }

   static int compare(String a, String b) {
      String[] left = a.split("[.+-]");
      String[] right = b.split("[.+-]");

      for (int i = 0; i < Math.max(left.length, right.length); i++) {
         int x = i < left.length ? number(left[i]) : 0;
         int y = i < right.length ? number(right[i]) : 0;
         if (x != y) {
            return Integer.compare(x, y);
         }
      }

      return 0;
   }

   private static int number(String part) {
      try {
         return Integer.parseInt(part.replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
         return 0;
      }
   }
}
