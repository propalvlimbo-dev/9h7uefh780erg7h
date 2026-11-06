package sg.ec;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.Identifier;

public final class ShaderDepthConvention {
   private static final String CLIENT_NAMESPACE = "rose";
   private static final String VERSION_DIRECTIVE = "#version";
   private static final String ZERO_TO_ONE_DEPTH_EXPRESSION = "(value)";
   private static final String NEGATIVE_ONE_TO_ONE_DEPTH_EXPRESSION = "((value) * 2.0 - 1.0)";

   private ShaderDepthConvention() {
   }

   public static String injectDepthToClipMacro(Identifier shaderId, String source) {
      if (source != null && shaderId.getNamespace().equals(CLIENT_NAMESPACE)) {
         int versionOffset = source.indexOf(VERSION_DIRECTIVE);
         int versionLineEnd = versionOffset < 0 ? -1 : source.indexOf(10, versionOffset);
         if (versionLineEnd < 0) {
            return source;
         }

         String depthExpression = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? ZERO_TO_ONE_DEPTH_EXPRESSION : NEGATIVE_ONE_TO_ONE_DEPTH_EXPRESSION;
         return source.substring(0, versionLineEnd + 1) + "#define ROSE_DEPTH_TO_CLIP(value) " + depthExpression + "\n" + source.substring(versionLineEnd + 1);
      } else {
         return source;
      }
   }
}
