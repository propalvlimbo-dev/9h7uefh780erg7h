package settings.render;

import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.resources.Identifier;
import sg.ec.ShaderDepthConvention;
import sg.util.crypto.EncryptedAssetContainer;

public final class ClientShaderSource {
   private static final ShaderSource SHADER_SOURCE = ClientShaderSource::loadShaderSource;
   private static final String VERTEX_SHADER_EXTENSION = ".vsh";
   private static final String FRAGMENT_SHADER_EXTENSION = ".fsh";
   private static final String INPUT_PATH_SEPARATOR = "/";
   private static final String CURRENT_DIRECTORY_SEGMENT = ".";
   private static final String PARENT_DIRECTORY_SEGMENT = "..";
   private static final String PATH_ESCAPE_MESSAGE = "Shader path escapes its namespace";
   private static final String NORMALIZED_PATH_SEPARATOR = "/";

   private static String normalizeShaderPath(String path) {
      ArrayDeque pathSegments = new ArrayDeque();

      for (String pathSegment : path.replace('\\', '/').split(INPUT_PATH_SEPARATOR)) {
         if (!pathSegment.isEmpty() && !pathSegment.equals(CURRENT_DIRECTORY_SEGMENT)) {
            if (pathSegment.equals(PARENT_DIRECTORY_SEGMENT)) {
               if (pathSegments.isEmpty()) {
                  throw new IllegalArgumentException(PATH_ESCAPE_MESSAGE);
               }

               pathSegments.removeLast();
            } else {
               pathSegments.addLast(pathSegment);
            }
         }
      }

      return String.join(NORMALIZED_PATH_SEPARATOR, pathSegments);
   }

   public static ShaderSource getShaderSource() {
      return SHADER_SOURCE;
   }

   private ClientShaderSource() {
   }

   private static String loadShaderSource(final Identifier shaderId, ShaderType shaderType) {
      String shaderExtension = shaderType == ShaderType.VERTEX ? VERTEX_SHADER_EXTENSION : FRAGMENT_SHADER_EXTENSION;
      String normalizedShaderPath = normalizeShaderPath(shaderId.getPath() + shaderExtension);
      String shaderText = readDecodedShaderText(shaderId.getNamespace(), normalizedShaderPath);
      if (shaderText == null) {
         return null;
      }

      int lastSlashIndex = normalizedShaderPath.lastIndexOf(47);
      final String includeBasePath = lastSlashIndex < 0 ? "" : normalizedShaderPath.substring(0, lastSlashIndex + 1);
      GlslPreprocessor preprocessor = new GlslPreprocessor() {
         private final Set<String> ю = new HashSet<>();

         @Override
         public String applyImport(boolean var1, String var2x) {
            Identifier var3x;
            String var4x;
            try {
               if (var1) {
                  var3x = shaderId;
                  var4x = ClientShaderSource.normalizeShaderPath(includeBasePath + var2x);
               } else {
                  var3x = Identifier.parse(var2x);
                  var4x = ClientShaderSource.normalizeShaderPath("include/" + var3x.getPath());
               }
            } catch (RuntimeException var7x) {
               return "#error " + var7x.getMessage();
            }

            String var5x = var3x.getNamespace() + ":" + var4x;
            if (!this.ю.add(var5x)) {
               return null;
            }

            String var6x = ClientShaderSource.readDecodedShaderText(var3x.getNamespace(), var4x);
            return var6x == null ? "#error Missing GLSL import " + var5x : var6x;
         }
      };
      return ShaderDepthConvention.injectDepthToClipMacro(shaderId, String.join("", preprocessor.process(shaderText)));
   }

   private static String readDecodedShaderText(String namespace, String shaderPath) {
      String resourcePath = "/assets/" + namespace + "/shaders/" + shaderPath;

      try (InputStream assetStream = ClientShaderSource.class.getResourceAsStream(resourcePath)) {
         if (assetStream == null) {
            return null;
         }

         try (InputStream decodedStream = EncryptedAssetContainer.л_5c929562(assetStream, namespace + ":shaders/" + shaderPath)) {
            return new String(decodedStream.readAllBytes(), StandardCharsets.UTF_8);
         }
      } catch (IOException | GeneralSecurityException var11) {
         return null;
      }
   }
}
