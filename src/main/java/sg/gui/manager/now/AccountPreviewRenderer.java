package sg.gui.manager.now;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import org.joml.Vector4f;
import sg.render.pipeline.UiDrawing;

public final class AccountPreviewRenderer {
    private AccountPreviewRenderer() { }
    public static void renderElement(GuiGraphicsExtractor ignored, AccountEntry entry, float scale, float x, float y, float size) {
        if (entry == null || size <= 0) return;
        var skin = DefaultPlayerSkin.get(entry.getUuid()).body().texturePath();
        UiDrawing.drawTexture(Render2D.px(x), Render2D.px(y), Render2D.px(size), Render2D.px(size),
                Render2D.px(2), new Vector4f(.125F, .125F, .125F, .125F), -1, skin);
        UiDrawing.drawTexture(Render2D.px(x), Render2D.px(y), Render2D.px(size), Render2D.px(size),
                Render2D.px(2), new Vector4f(.625F, .125F, .125F, .125F), -1, skin);
    }
}
