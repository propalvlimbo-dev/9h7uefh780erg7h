package sg.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.joml.Vector2f;
import sg.mixin.GuiGraphicsExtractorAccessor;

/** Helpers for placing model PiP states in the same transformed viewport as the cosmetic cards. */
public final class GuiPreviewUtil {
   private GuiPreviewUtil() {
   }

   public static Rect transformRect(float x, float y, float width, float height) {
      Vector2f first = WorldProjection.transformScreenPoint(x, y);
      Vector2f second = WorldProjection.transformScreenPoint(x + width, y + height);
      float left = Math.min(first.x, second.x);
      float top = Math.min(first.y, second.y);
      return new Rect(left, top, Math.abs(second.x - first.x), Math.abs(second.y - first.y));
   }

   public static void submit(GuiGraphicsExtractor graphics, PictureInPictureRenderState renderState) {
      GuiRenderState guiRenderState = ((GuiGraphicsExtractorAccessor)graphics).rose$getGuiRenderState();
      guiRenderState.addPicturesInPictureState(renderState);
   }

   public static int floor(float value) {
      return (int)Math.floor(value);
   }

   public static int ceil(float value) {
      return (int)Math.ceil(value);
   }

   public record Rect(float x, float y, float width, float height) {
      public float centerX() {
         return this.x + this.width * 0.5F;
      }

      public float centerY() {
         return this.y + this.height * 0.5F;
      }
   }
}
