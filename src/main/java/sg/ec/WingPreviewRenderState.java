package sg.ec;

import java.util.List;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;
import sg.render.WingAnimator;
import sg.render.AnimatedWingVariant;

public record WingPreviewRenderState(
   List<WingPreviewRenderState.WingDraw> wings,
   int x0,
   int y0,
   int x1,
   int y1,
   float scale,
   @Nullable ScreenRectangle scissorArea,
   @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {
   public WingPreviewRenderState(List<WingPreviewRenderState.WingDraw> var1, int var2, int var3, int var4, int var5, float var6, @Nullable ScreenRectangle var7) {
      this(var1, var2, var3, var4, var5, var6, var7, PictureInPictureRenderState.getBounds(var2, var3, var4, var5, var7));
   }

   public record WingDraw(AnimatedWingVariant type, WingAnimator animator, float delta, float alpha, float x, float y, float width, float height) {
   }
}
