package sg.ec;

import lombok.Generated;
import sg.render.FontFace;
import sg.render.pipeline.UiRenderPassSupport;

public class FontRenderer implements MinecraftAccess, UiRenderPassSupport {
   private final @com.google.gson.annotations.SerializedName("В")  FontFace fontFace;

   public FontRenderer(String textureName, String fontName) {
      this.fontFace = FontFace.builder().fontName(fontName).textureResource(textureName).metadataResource(fontName).build();
   }

   public FontRenderer(String fontName) {
      this(fontName, fontName);
   }

   /** Opt-in atlas metrics for imported fonts; existing fonts keep their legacy placement. */
   public FontRenderer(String fontName, boolean useAtlasMetrics) {
      this.fontFace = FontFace.builder().fontName(fontName).textureResource(fontName).metadataResource(fontName)
         .useAtlasMetrics(useAtlasMetrics).build();
   }

   @Generated
   public FontFace getFontFace() {
      return this.fontFace;
   }
}
