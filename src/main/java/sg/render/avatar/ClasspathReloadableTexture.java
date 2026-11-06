package sg.render.avatar;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Optional;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import sg.util.crypto.EncryptedAssetContainer;

public final class ClasspathReloadableTexture extends ReloadableTexture {
   private static final TextureMetadataSection В = new TextureMetadataSection(true, true, MipmapStrategy.AUTO, 0.0F);
   private final boolean АЧ;

   private TextureContents load(InputStream var1, TextureMetadataSection var2) throws IOException {
      try (
         InputStream var3 = var1;
         InputStream var4 = EncryptedAssetContainer.л_5c929562(var3, this.resourceId().toString());
      ) {
         return new TextureContents(NativeImage.read(var4), var2);
      } catch (GeneralSecurityException var11) {
         throw new IOException("Failed to decrypt texture: " + this.resourceId(), var11);
      }
   }

   @Override
   public TextureContents loadContents(ResourceManager var1) throws IOException {
      if (!this.АЧ) {
         Optional var2 = var1.getResource(this.resourceId());
         if (var2.isPresent()) {
            Resource var6 = (Resource)var2.get();
            TextureMetadataSection var4 = var6.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);
            return this.load(var6.open(), var4);
         }
      }

      String var5 = "/assets/" + this.resourceId().getNamespace() + "/" + this.resourceId().getPath();
      InputStream var3 = ClasspathReloadableTexture.class.getResourceAsStream(var5);
      if (var3 == null) {
         throw new FileNotFoundException(var5);
      } else {
         return this.load(var3, В);
      }
   }

   public ClasspathReloadableTexture(Identifier var1, boolean var2) {
      super(var1);
      this.АЧ = var2;
   }

   public ClasspathReloadableTexture(Identifier var1) {
      this(var1, false);
   }
}
