package sg.ec;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import sg.util.crypto.EncryptedAssetContainer;

public final class EncryptedTextureRegistry {
   private static final Set<Identifier> Г = ConcurrentHashMap.newKeySet();
   private static final String ВвЫ = "rose";
   private static final String Ввl = ".png";

   private static boolean Э(Identifier var0) {
      return ВвЫ.equals(var0.getNamespace()) && var0.getPath().endsWith(Ввl);
   }

   private EncryptedTextureRegistry() {
   }

   public static AbstractTexture Х(Identifier var0) {
      TextureManager var1 = Minecraft.getInstance().getTextureManager();
      if (!Э(var0)) {
         return var1.getTexture(var0);
      }

      if (Г.add(var0)) {
         try {
            var1.registerAndLoad(var0, new EncryptedTextureRegistry.EncryptedReloadableTexture(var0));
         } catch (RuntimeException var3) {
            Г.remove(var0);
            throw var3;
         }
      }

      return var1.getTexture(var0);
   }

   private static final class EncryptedReloadableTexture extends ReloadableTexture {
      @Override
      public TextureContents loadContents(ResourceManager var1) throws IOException {
         try (
            InputStream var2 = var1.open(this.resourceId());
            InputStream var3 = EncryptedAssetContainer.л_5c929562(var2, this.resourceId().toString());
         ) {
            return new TextureContents(NativeImage.read(var3), null);
         } catch (GeneralSecurityException var10) {
            throw new IOException("Unable to decrypt texture: " + this.resourceId(), var10);
         }
      }

      private EncryptedReloadableTexture(Identifier var1) {
         super(var1);
      }
   }
}
