package sg.ec;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sg.render.pipeline.UiDrawing;

public final class DeferredUiTextureQueue {
   private static final List<DeferredUiTextureQueue.TextureDraw> ВЯ = new ArrayList<>();
   private static final String Хl2 = "This is a utility class and cannot be instantiated";

   public static void жф() {
      if (!ВЯ.isEmpty()) {
         Matrix4f var0 = new Matrix4f(UiDrawing.getTransform());

         try {
            for (DeferredUiTextureQueue.TextureDraw var2 : ВЯ) {
               UiDrawing.setTransform(var2.matrix);
               UiDrawing.drawTexture(var2.x, var2.y, var2.width, var2.height, var2.round, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), var2.color, var2.texture);
            }
         } finally {
            UiDrawing.setTransform(var0);
            ВЯ.clear();
         }
      }
   }

   @Generated
   private DeferredUiTextureQueue() {
      throw new UnsupportedOperationException(Хl2);
   }

   public static void с(Identifier var0, float var1, float var2, float var3, float var4, int var5) {
      с(var0, var1, var2, var3, var4, 0.0F, var5);
   }

   public static void жI() {
      ВЯ.clear();
   }

   public static void с(Identifier var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (var0 != null && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 >>> 24 > 0) {
            ВЯ.add(new DeferredUiTextureQueue.TextureDraw(var0, var1, var2, var3, var4, var5, var6, new Matrix4f(UiDrawing.getTransform())));
         }
      }
   }

   private record TextureDraw(Identifier texture, float x, float y, float width, float height, float round, int color, Matrix4f matrix) {
   }
}
