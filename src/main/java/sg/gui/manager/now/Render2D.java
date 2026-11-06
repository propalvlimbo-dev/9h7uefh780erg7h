package sg.gui.manager.now;

import java.util.ArrayDeque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import sg.render.ClientLogo;
import sg.render.pipeline.UiDrawing;

/** Source menu coordinates are GUI units; the base renderer consumes framebuffer pixels. */
public final class Render2D {
    private static GuiGraphicsExtractor context;
    private static final ArrayDeque<Float> ALPHAS = new ArrayDeque<>();
    private static float alpha = 1.0F;
    private Render2D() { }

    static float px(float value) { return value * (float) Minecraft.getInstance().getWindow().getGuiScale(); }
    public static GuiGraphicsExtractor getCurrentDrawContext() { return context; }
    public static int applyAlpha(int color) { return UiTheme.computeInt(color, alpha); }
    public static void processData46(float value) {
        ALPHAS.push(alpha);
        alpha *= Math.clamp(value, 0.0F, 1.0F);
    }
    public static void updateState() { alpha = ALPHAS.isEmpty() ? 1.0F : ALPHAS.pop(); }
    public static void renderElement5(GuiGraphicsExtractor value) {
        context = value;
        alpha = 1.0F;
        ALPHAS.clear();
        UiDrawing.resetTransforms();
        UiDrawing.resetGlobalAlpha();
        UiDrawing.disableScissor();
    }
    public static void updateState6() {
        context = null;
        ALPHAS.clear();
        alpha = 1.0F;
        UiDrawing.resetTransforms();
        UiDrawing.resetGlobalAlpha();
        UiDrawing.disableScissor();
    }
    public static void processData51(float x, float y, float w, float h, float radius, int color) {
        UiDrawing.drawRoundedRect(px(x), px(y), px(w), px(h), px(radius), applyAlpha(color));
    }
    public static void processData86(float x, float y, float w, float h, float radius, float stroke, int color) {
        UiDrawing.drawOutline(px(x), px(y), px(w), px(h), px(stroke), new Vector4f(px(radius)), applyAlpha(color));
    }
    public static void processData89(String path, float x, float y, float w, float h, float radius, int color) {
        drawTexture(path, x, y, w, h, color);
    }
    public static void drawTexture(String path, float x, float y, float w, float h, int color) {
        if (path.endsWith("/logo.png")) {
            ClientLogo.drawInBox(px(x), px(y), px(w), px(h), applyAlpha(color));
        } else {
            UiDrawing.drawTexture(px(x), px(y), px(w), px(h), 0.0F, applyAlpha(color), toIdentifier(path));
        }
    }
    public static void processData26(String path, float x, float y, float w, float h, float radius,
                                     float opacity, float u0, float v0, float u1, float v1, int color) {
        UiDrawing.drawTexture(px(x), px(y), px(w), px(h), px(radius),
                new Vector4f(u0, v0, u1 - u0, v1 - v0), UiTheme.computeInt(applyAlpha(color), opacity), toIdentifier(path));
    }
    public static void drawFullscreenTexture(String path, float w, float h, float opacity) {
        drawTexture(path, 0, 0, w, h, UiTheme.computeInt(-1, opacity));
    }
    /** Полноэкранная картинка с масштабом и сдвигом — для «живого» зума фона. */
    public static void drawFullscreenTextureZoom(String path, float w, float h, float opacity,
                                                 float zoomFactor, float offsetX, float offsetY) {
        float drawnW = w * zoomFactor;
        float drawnH = h * zoomFactor;
        float x = (w - drawnW) * 0.5F + offsetX;
        float y = (h - drawnH) * 0.5F + offsetY;
        UiDrawing.drawTexture(px(x), px(y), px(drawnW), px(drawnH), 0.0F,
                applyAlpha(UiTheme.computeInt(-1, opacity)), toIdentifier(path));
    }
    public static void drawLoadingIndicator(long start, float w, float h) {
        ClientLogo.drawSpin3DByTime(px(w / 2.0F), px(h / 2.0F), px(42.0F), applyAlpha(UiTheme.intVal7), 2600L);
        for (int i = 0; i < 3; i++) {
            float phase = (System.currentTimeMillis() - start + i * 180L) % 1000L / 1000.0F;
            processData51(w / 2.0F - 18 + i * 18, h / 2.0F + 40, 6, 6, 3,
                    UiTheme.computeInt(-1, (float) Math.sin(phase * Math.PI) * .78F));
        }
    }
    public static void renderElement2(GuiGraphicsExtractor ignored, float x, float y, float w, float h) {
        UiDrawing.pushScissor(px(x), px(y), px(w), px(h));
    }
    public static void renderElement(GuiGraphicsExtractor ignored) { popScissor(); }
    public static void pushScissorBounds(int left, int top, int right, int bottom) {
        UiDrawing.pushScissor(px(left), px(top), px(right - left), px(bottom - top));
    }
    public static void popScissor() { UiDrawing.popScissor(); }
    public static Identifier toIdentifier(String sourcePath) {
        String path = sourcePath.replace('\\', '/');
        String prefix = "assets/deluxe/textures/gui/mainmenu/";
        if (path.startsWith(prefix)) path = path.substring(prefix.length());
        return Identifier.fromNamespaceAndPath("rose", "textures/gui/now_mainmenu/" + path);
    }
}
