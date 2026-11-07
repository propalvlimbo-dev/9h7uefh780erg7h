package sg.render;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Vector2f;

public record GuiCoordinateTransform(float clipX, float clipY, float offsetX, float offsetY, float scaleX, float scaleY, int x1, int y1, int x2, int y2) {
   public float screenX(float var1) {
      return this.x1 + this.localX(var1);
   }

   public float localX(float var1) {
      return this.offsetX + (var1 - this.clipX) * this.scaleX;
   }

   public float screenY(float var1) {
      return this.y1 + this.localY(var1);
   }

   public static GuiCoordinateTransform of(float var0, float var1, float var2, float var3) {
      Vector2f var4 = WorldProjection.transformScreenPoint(var0, var1);
      Vector2f var5 = WorldProjection.transformScreenPoint(var0 + var2, var1 + var3);
      int var7 = (int)Math.floor(var4.x);
      int var8 = (int)Math.floor(var4.y);
      int var9 = (int)Math.ceil(var5.x);
      int var10 = (int)Math.ceil(var5.y);
      float var11 = var2 <= 0.0F ? 1.0F : (var5.x - var4.x) / var2;
      float var12 = var3 <= 0.0F ? 1.0F : (var5.y - var4.y) / var3;
      return new GuiCoordinateTransform(var0, var1, var4.x - var7, var4.y - var8, var11, var12, var7, var8, var9, var10);
   }

   public ScreenRectangle clip() {
      return new ScreenRectangle(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
   }

   public float localY(float var1) {
      return this.offsetY + (var1 - this.clipY) * this.scaleY;
   }
}
