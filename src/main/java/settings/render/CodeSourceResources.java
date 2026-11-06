package settings.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;

public final class CodeSourceResources {
   private static final ClassValue<URL> CODE_SOURCE_ROOTS = new ClassValue<URL>() {
      private static final String В诶й = "/";

      protected URL computeValue(Class<?> var1) {
         CodeSource var2 = var1.getProtectionDomain().getCodeSource();
         if (var2 != null && var2.getLocation() != null) {
            URL var3 = var2.getLocation();

            try {
               return var3.toExternalForm().endsWith(В诶й) ? var3 : URI.create("jar:" + var3.toExternalForm() + "!/").toURL();
            } catch (MalformedURLException var5) {
               throw new IllegalStateException("Invalid code source for " + var1.getName(), var5);
            }
         } else {
            throw new IllegalStateException("No code source for " + var1.getName());
         }
      }
   };

   public static InputStream openResource(Class<?> ownerClass, String resourcePath) {
      try {
         URL resourceUrl = new URL(CODE_SOURCE_ROOTS.get(ownerClass), resourcePath);
         URLConnection connection = resourceUrl.openConnection();
         connection.setUseCaches(false);
         return connection.getInputStream();
      } catch (FileNotFoundException var4) {
         return null;
      } catch (IOException var5) {
         throw new UncheckedIOException("Cannot read " + resourcePath + " from " + ownerClass.getName(), var5);
      }
   }

   private CodeSourceResources() {
   }
}
