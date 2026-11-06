package sg.ec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

public final class ViaFabricPlusDefaults {
   private static final String сшК = "viafabricplus";
   private static final String сшI = "rose-via";
   private static final String сшТ = "Could not apply default ViaFabricPlus settings";
   private static final String сшО = "viafabricplus/settings.json";
   private static final String сшФ = ".rose-defaults-26.2-v1.applied";
   private static final String сшЙ = "/defaults/viafabricplus/settings.json";
   private static final String сшк = "Missing bundled ViaFabricPlus settings";
   private static final String сшг = ".rose-settings-";
   private static final String сш0 = ".tmp";
   private static final String сшх = "Rose ViaFabricPlus preset 26.2 v1\n";

   public static void ЖП() {
      FabricLoader var0 = FabricLoader.getInstance();
      if (var0.isModLoaded(сшК)) {
         try {
            Ж(var0.getConfigDir());
         } catch (IOException var2) {
            LoggerFactory.getLogger(сшI).warn(сшТ, var2);
         }
      }
   }

   private ViaFabricPlusDefaults() {
   }

   static synchronized void Ж(Path var0) throws IOException {
      Path var1 = var0.resolve(сшО);
      Path var2 = var1.resolveSibling(сшФ);
      if (!Files.exists(var2, LinkOption.NOFOLLOW_LINKS) || !Files.exists(var1, LinkOption.NOFOLLOW_LINKS)) {
         try (InputStream var3 = ViaFabricPlusDefaults.class.getResourceAsStream(сшЙ)) {
            if (var3 == null) {
               throw new IOException(сшк);
            }

            Files.createDirectories(var1.getParent());
            Path var4 = Files.createTempFile(var1.getParent(), сшг, сш0);

            try {
               Files.copy(var3, var4, StandardCopyOption.REPLACE_EXISTING);

               try {
                  Files.move(var4, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
               } catch (AtomicMoveNotSupportedException var14) {
                  Files.move(var4, var1, StandardCopyOption.REPLACE_EXISTING);
               }
            } finally {
               Files.deleteIfExists(var4);
            }

            try {
               Files.writeString(var2, сшх, StandardOpenOption.CREATE_NEW);
            } catch (FileAlreadyExistsException var13) {
            }
         }
      }
   }
}
