package sg.gui.manager.now;

import sg.ec.FontRenderer;
import sg.render.pipeline.UiDrawing;

public final class FontText {
    private FontText() { }
    private static final class Fonts {
        static final FontRenderer REGULAR = new FontRenderer("now_menu_semibold");
        static final FontRenderer MEDIUM = new FontRenderer("now_menu_bold");
    }
    private static FontRenderer font(FontFace face) { return face == FontFace.SEMI_BOLD ? Fonts.MEDIUM : Fonts.REGULAR; }
    public static void processData3(FontFace face, String text, float x, float y, float size, int color) {
        if (text == null || text.isEmpty() || size <= 0) return;
        UiDrawing.drawText(font(face), text, Render2D.px(x), Render2D.px(y), Render2D.px(size),
                Render2D.applyAlpha(color), 0.0F, 0.5F, 1.0F);
    }
    public static float computeFloat(FontFace face, String text, float size) {
        return UiDrawing.getTextWidth(font(face), text, size, 1.0F);
    }
}
