package sg.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.util.crypto.EncryptedAssetContainer;

@Mixin(FallbackResourceManager.class)
public abstract class FallbackResourceManagerDecryptionMixin {
   private static final String с1Я = "rose";
   private static final String с1Х = ".png";
   private static final String с1е = ".vsh";
   private static final String с1А = ".fsh";
   private static final String с14 = ".glsl";

   @Inject(method = "createResource", at = @At("RETURN"), cancellable = true)
   private static void decryptRoseResource(
      PackResources var0, Identifier var1, IoSupplier<InputStream> var2, IoSupplier<ResourceMetadata> var3, CallbackInfoReturnable<Resource> var4
   ) {
      if (shouldDecrypt(var1)) {
         Resource var5 = (Resource)var4.getReturnValue();
         Resource var6 = new Resource(var5.source(), () -> decrypt(var5, var1), var5::metadata);
         var4.setReturnValue(var6);
      }
   }

   private static InputStream decrypt(Resource var0, Identifier var1) throws IOException {
      try (InputStream var2 = var0.open()) {
         try {
            return EncryptedAssetContainer.л_5c929562(var2, var1.toString());
         } catch (GeneralSecurityException var6) {
            throw new IOException("Failed to decrypt resource: " + var1, var6);
         }
      }
   }

   private static boolean shouldDecrypt(Identifier var0) {
      if (!с1Я.equals(var0.getNamespace())) {
         return false;
      }

      String var1 = var0.getPath();
      return var1.endsWith(с1Х) || var1.endsWith(с1е) || var1.endsWith(с1А) || var1.endsWith(с14);
   }
}
