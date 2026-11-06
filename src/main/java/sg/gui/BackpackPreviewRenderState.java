package sg.gui;

import java.util.List;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;
import sg.render.BackpackColorVariant;

public record BackpackPreviewRenderState(
   List<BackpackPreviewRenderState.Preview> previews, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {
   public BackpackPreviewRenderState(List<BackpackPreviewRenderState.Preview> var1, int var2, int var3, int var4, int var5, @Nullable ScreenRectangle var6) {
      this(var1, var2, var3, var4, var5, var6, PictureInPictureRenderState.getBounds(var2, var3, var4, var5, var6));
   }

   @Override
   public float scale() {
      return 1.0F;
   }

   public record Preview(
      BackpackColorVariant variant, float alpha, float x, float y, float width, float height
   ) {
   }
}
