package sg.ec;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Vector4f;
import sg.render.EmojiMaskVariant;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;

public final class EmojiMaskPreview {
   private static final Map<Identifier, EmojiMaskPreview.VisibleTextureRegion> ф = new HashMap<>();
   private static final EmojiMaskPreview.VisibleTextureRegion В = new EmojiMaskPreview.VisibleTextureRegion(
      EmojiMaskPreview.ВO西, EmojiMaskPreview.ВOд, EmojiMaskPreview.ВOБ, EmojiMaskPreview.ВOС, 1.0F
   );
   private static final float ВOЗ = 0.5F;
   private static final float ВOЕ = 0.5F;
   private static final float ВOП = 255.0F;
   private static final float ВO西 = 0.15625F;
   private static final float ВOд = 0.15625F;
   private static final float ВOБ = 0.15625F;
   private static final float ВOС = 0.15625F;

   private static EmojiMaskPreview.VisibleTextureRegion ъ(Identifier var0) {
      Optional var1 = Minecraft.getInstance().getResourceManager().getResource(var0);
      if (var1.isEmpty()) {
         return В;
      }

      try (
         InputStream var2 = ((Resource)var1.get()).open();
         NativeImage var3 = NativeImage.read(var2);
      ) {
         int var4 = var3.getWidth();
         int var5 = var3.getHeight();
         int var6 = (int)(var4 * В.u());
         int var7 = (int)(var5 * В.v());
         int var8 = Math.min(var4, (int)Math.ceil(var4 * (В.u() + В.width())));
         int var9 = Math.min(var5, (int)Math.ceil(var5 * (В.v() + В.height())));
         int var10 = var8;
         int var11 = var9;
         int var12 = var6 - 1;
         int var13 = var7 - 1;

         for (int var14 = var7; var14 < var9; var14++) {
            for (int var15 = var6; var15 < var8; var15++) {
               if (var3.getPixel(var15, var14) >>> 24 > 8) {
                  var10 = Math.min(var10, var15);
                  var11 = Math.min(var11, var14);
                  var12 = Math.max(var12, var15);
                  var13 = Math.max(var13, var14);
               }
            }
         }

         return var12 < var10 || var13 < var11
            ? В
            : new EmojiMaskPreview.VisibleTextureRegion(
               (float)var10 / var4,
               (float)var11 / var5,
               (float)(var12 - var10 + 1) / var4,
               (float)(var13 - var11 + 1) / var5,
               (float)(var12 - var10 + 1) / (var13 - var11 + 1)
            );
      } catch (IOException var20) {
         return В;
      }
   }

   public static void ъ(EmojiMaskVariant var0, float var1, float var2, float var3, float var4, float var5) {
      EmojiMaskPreview.VisibleTextureRegion var6 = ф.computeIfAbsent(var0.getTextureId(), EmojiMaskPreview::ъ);
      float var7 = Math.min(var3, var4 * var6.aspect());
      float var8 = var7 / var6.aspect();
      UiDrawing.drawTexture(
         var1 + (var3 - var7) * ВOЗ,
         var2 + (var4 - var8) * ВOЕ,
         var7,
         var8,
         0.0F,
         new Vector4f(var6.u(), var6.v(), var6.width(), var6.height()),
         ColorMath.packArgb(255, 255, 255, ВOП * var5),
         var0.getTextureId()
      );
   }

   private EmojiMaskPreview() {
   }

   public static void ъ西() {
      ф.clear();
   }

   private record VisibleTextureRegion(float u, float v, float width, float height, float aspect) {
   }
}
