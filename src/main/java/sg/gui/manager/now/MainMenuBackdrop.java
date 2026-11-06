package sg.gui.manager.now;

import sg.render.pipeline.UiDrawing;

/**
 * Процедурный анимированный фон главного меню в стиле Minecraft.
 * <p>
 * Слои (от дальнего к ближнему): небо с градиентом и звёздами, солнце с гало у горизонта,
 * дымка над горизонтом, плывущие блочные облака, три плана блочного рельефа с деревьями,
 * блочная земля, частицы-лепестки и виньетка.
 * <p>
 * Управление: ЛКМ (по пустому месту) + перетаскивание — панорама с инерцией,
 * колесо мыши — зум, ПКМ — сброс вида, движение мыши — параллакс.
 */
public final class MainMenuBackdrop {
    private static final float ZOOM_MIN = 0.86F;
    private static final float ZOOM_MAX = 1.30F;
    private static final float HORIZON = 0.64F;

    private static float panX;
    private static float panVelocity;
    private static float panY;
    private static float autoScroll;
    private static float zoom = 1.0F;
    private static float parallaxX;
    private static float parallaxY;
    private static float time;
    private static long lastFrameNs;
    private static boolean dragging;

    private MainMenuBackdrop() {
    }

    // ------------------------------------------------------------------ управление

    public static void beginDrag() {
        dragging = true;
        panVelocity = 0.0F;
    }

    public static boolean isDragging() {
        return dragging;
    }

    public static void endDrag() {
        dragging = false;
    }

    public static void drag(double deltaX, double deltaY) {
        dragging = true;
        panX += (float) deltaX * 1.15F;
        panY = clamp(panY + (float) deltaY * 0.30F, -60.0F, 60.0F);
        panVelocity = (float) deltaX * 20.0F;
    }

    public static void scroll(double amount) {
        zoom = clamp(zoom + (float) amount * 0.06F, ZOOM_MIN, ZOOM_MAX);
    }

    public static void reset() {
        panX = 0.0F;
        panY = 0.0F;
        panVelocity = 0.0F;
        zoom = 1.0F;
    }

    // ------------------------------------------------------------------ кадр

    public static void render(float width, float height, float scale, float mouseX, float mouseY) {
        float w = Math.max(1.0F, width);
        float h = Math.max(1.0F, height);

        long now = System.nanoTime();
        float dt = lastFrameNs == 0L ? 0.016F : Math.min(0.05F, (now - lastFrameNs) / 1.0E9F);
        lastFrameNs = now;
        time += dt;

        if (!dragging) {
            panX += panVelocity * dt;
            panVelocity *= (float) Math.pow(0.0025F, dt);
            if (Math.abs(panVelocity) < 1.5F) {
                panVelocity = 0.0F;
            }
            autoScroll -= 6.0F * dt;
        }

        float targetX = (mouseX / w - 0.5F) * 2.0F;
        float targetY = (mouseY / h - 0.5F) * 2.0F;
        float ease = Math.min(1.0F, dt * 5.0F);
        parallaxX += (targetX - parallaxX) * ease;
        parallaxY += (targetY - parallaxY) * ease;

        int accent = UiTheme.intVal7;
        float horizon = h * HORIZON + panY * 0.05F;

        sky(w, h, horizon, accent);
        stars(w, horizon, scale, accent);
        sun(w, h, horizon, scale, accent);
        haze(w, horizon, accent);
        clouds(w, h, scale, accent);

        ridge(w, h, horizon, scale, 0.10F, 0.155F, 0.55F, 0.9F, mix(0xFF18112A, accent, 0.26F), false);
        ridge(w, h, horizon, scale, 0.26F, 0.095F, 0.60F, 3.7F, mix(0xFF0D0915, accent, 0.28F), true);
        ridge(w, h, horizon, scale, 0.55F, 0.050F, 0.65F, 7.3F, mix(0xFF050308, accent, 0.30F), true);

        ground(w, h, scale);
        particles(w, h, scale, accent);
        vignette(w, h);
    }

    // ------------------------------------------------------------------ небо и свет

    private static void sky(float w, float h, float horizon, int accent) {
        int top = mix(0xFF040309, accent, 0.08F);
        int mid = mix(0xFF0D0611, accent, 0.18F);
        int low = mix(0xFF602030, accent, 0.38F);
        int bands = 64;
        float bandH = horizon / bands;
        for (int i = 0; i < bands; i++) {
            float t = (float) Math.pow(i / (float) bands, 1.5F);
            int color = t < 0.60F ? mix(top, mid, t / 0.60F) : mix(mid, low, (t - 0.60F) / 0.40F);
            band(0.0F, i * bandH, w, bandH + 1.0F, color);
        }
    }

    private static void stars(float w, float horizon, float scale, int accent) {
        int color = mix(0xFFFFF0F8, accent, 0.15F);
        for (int i = 0; i < 90; i++) {
            float seed = i * 37.13F;
            float sy = hash01(seed * 1.7F + 4.0F) * horizon * 0.80F;
            float twinkle = 0.55F + 0.45F * (float) Math.sin(time * (0.7F + hash01(seed * 2.3F)) + seed);
            float size = (0.8F + hash01(seed * 3.1F) * 1.3F) * Math.max(0.8F, scale);
            float drawX = mod(hash01(seed) * w + parallaxX * 10.0F * scale + panX * 0.05F + autoScroll * 0.05F, w);
            float alpha = (0.15F + 0.50F * twinkle) * (1.0F - sy / horizon);
            band(drawX, sy + parallaxY * 4.0F * scale, size, size, withAlpha(color, clamp(alpha, 0.0F, 1.0F)));
        }
    }

    private static void sun(float w, float h, float horizon, float scale, int accent) {
        float cx = w * 0.71F - parallaxX * 26.0F * scale;
        float cy = horizon - h * 0.20F - parallaxY * 10.0F * scale;
        float radius = Math.max(14.0F, 22.0F * Math.max(0.7F, scale)) * zoom;
        int glow = mix(0xFFFFCE9E, accent, 0.30F);

        for (int i = 11; i >= 1; i--) {
            float rr = radius * (1.0F + i * 0.75F);
            disc(cx, cy, rr, withAlpha(glow, 0.052F * (12 - i) / 11.0F));
        }
        disc(cx, cy, radius * 1.5F, withAlpha(glow, 0.25F));
        disc(cx, cy, radius, withAlpha(mix(0xFFFFE8C4, accent, 0.18F), 0.95F));
    }

    private static void haze(float w, float horizon, int accent) {
        int color = mix(0xFFFFB4A0, accent, 0.30F);
        for (int i = 0; i < 9; i++) {
            float bandH = Math.max(1.0F, horizon * 0.010F);
            band(0.0F, horizon - (i + 1) * bandH, w, bandH + 0.6F, withAlpha(color, 0.18F * (1.0F - i / 9.0F)));
        }
    }

    private static void clouds(float w, float h, float scale, int accent) {
        int color = mix(0xFFFFE2EE, accent, 0.20F);
        for (int i = 0; i < 11; i++) {
            float seed = i * 91.7F;
            float cw = (100.0F + hash01(seed) * 160.0F) * Math.max(0.7F, scale) * zoom;
            float ch = cw * 0.15F;
            float span = w + cw * 3.0F;
            float drift = time * (3.0F + i * 0.40F) + i * 211.0F + autoScroll * 0.50F + panX * 0.5F;
            float cx = w + cw - mod(drift, span);
            float cy = h * (0.07F + hash01(seed * 1.7F) * 0.34F) - parallaxY * 6.0F * scale;
            int cc = withAlpha(color, 0.09F + hash01(seed * 2.3F) * 0.12F);
            pill(cx, cy, cw, ch, cc);
            pill(cx + cw * 0.15F, cy - ch * 0.60F, cw * 0.57F, ch * 1.05F, cc);
            pill(cx + cw * 0.34F, cy + ch * 0.22F, cw * 0.46F, ch * 0.78F, cc);
        }
    }

    // ------------------------------------------------------------------ рельеф

    /**
     * План блочного рельефа. Высота гребня задаётся суммой синусов с периодом ширины экрана,
     * поэтому слой бесшовно повторяется при любом горизонтальном сдвиге.
     */
    private static void ridge(float w, float h, float horizon, float scale, float factor, float heightFactor,
                              float amp, float seed, int color, boolean trees) {
        float blockW = Math.max(6.0F, 16.0F * Math.max(0.7F, scale) / zoom);
        float offset = mod(panX * factor + parallaxX * 26.0F * scale * factor + autoScroll * factor, w);
        float shiftY = parallaxY * 4.0F * scale * factor;
        int treeColor = darker(color, 0.35F);

        for (int pass = 0; pass < 2; pass++) {
            float shift = offset + pass * w;
            int columns = (int) Math.ceil(w / blockW) + 2;
            for (int i = -1; i < columns; i++) {
                float x = i * blockW + shift;
                if (x > w || x + blockW < 0.0F) {
                    continue;
                }
                float heightPx = crestHeight(x - shift, w, h, heightFactor, amp, seed, blockW);
                band(x, horizon - heightPx + shiftY, blockW + 0.7F, heightPx + h * 0.30F, color);
            }
            if (trees) {
                trees(w, h, horizon, shift, blockW, heightFactor, amp, seed, treeColor, shiftY);
            }
        }
    }

    private static float crestHeight(float worldX, float w, float h, float heightFactor, float amp, float seed,
                                     float blockW) {
        float t = worldX / w;
        float a = (float) Math.sin(t * Math.PI * 2.0 + seed);
        float b = (float) Math.sin(t * Math.PI * 6.0 + seed * 1.7F + 1.1F) * 0.50F;
        float c = (float) Math.sin(t * Math.PI * 11.0 + seed * 2.3F + 2.4F) * 0.25F;
        float shape = (a + b + c) / 1.75F;
        float heightPx = h * heightFactor * (1.0F + amp * shape) * zoom;
        if (heightPx < blockW) {
            heightPx = blockW;
        }
        return (float) Math.round(heightPx / blockW) * blockW;
    }

    private static void trees(float w, float h, float horizon, float shift, float blockW, float heightFactor,
                              float amp, float seed, int color, float shiftY) {
        for (int i = 0; i < 18; i++) {
            float treeSeed = i * 17.3F + seed * 11.0F;
            if (hash01(treeSeed) > 0.42F) {
                continue;
            }
            float worldX = hash01(treeSeed * 2.7F) * w;
            float x = worldX + shift;
            if (x < -blockW * 4.0F || x > w + blockW * 4.0F) {
                continue;
            }
            float surface = crestHeight(worldX, w, h, heightFactor, amp, seed, blockW);
            float baseY = horizon - surface * 0.75F + shiftY;
            int trunkBlocks = 2 + (int) (hash01(treeSeed * 4.3F) * 3.0F);
            float trunkTop = baseY - trunkBlocks * blockW;
            band(x, trunkTop, blockW + 0.5F, baseY - trunkTop + blockW, color);
            for (int gy = 0; gy < 2; gy++) {
                for (int gx = -1; gx <= 1; gx++) {
                    band(x + gx * blockW, trunkTop - gy * blockW - blockW, blockW + 0.5F, blockW + 0.5F, color);
                }
            }
            band(x, trunkTop - blockW * 3.0F, blockW + 0.5F, blockW + 0.5F, color);
        }
    }

    private static void ground(float w, float h, float scale) {
        for (int i = 0; i < 4; i++) {
            float bandH = h * 0.035F;
            band(0.0F, h * 0.86F + i * bandH, w, bandH + 1.0F, argb(0, 0, 0, (int) (30.0F * (1.0F - i / 4.0F))));
        }
        float blockW = 16.0F * Math.max(0.7F, scale) / zoom;
        float offset = mod(panX * 0.55F + autoScroll * 0.55F, blockW * 2.0F);
        int stripes = (int) Math.ceil(w / (blockW * 2.0F)) + 2;
        for (int i = -1; i < stripes; i++) {
            band(i * blockW * 2.0F + offset, h * 0.80F, blockW, h * 0.20F, argb(255, 255, 255, 4));
        }
    }

    private static void particles(float w, float h, float scale, int accent) {
        int color = mix(0xFFFF7896, accent, 0.35F);
        for (int i = 0; i < 46; i++) {
            float seed = i * 53.9F;
            float speed = 8.0F + hash01(seed) * 16.0F;
            float size = (2.0F + hash01(seed * 2.1F) * 3.0F) * Math.max(0.8F, scale);
            float travel = time * speed + hash01(seed * 4.7F) * h;
            float py = h * 0.94F - mod(travel, h * 0.94F);
            float sway = (float) Math.sin(time * 0.9F + seed) * 14.0F * scale;
            float px = mod(hash01(seed * 3.3F) * w + sway + parallaxX * 18.0F * scale + panX * 1.15F, w);
            float alpha = 0.14F + 0.36F * hash01(seed * 5.1F);
            band(px, py, size, size, withAlpha(color, alpha));
        }
    }

    private static void vignette(float w, float h) {
        int steps = 14;
        float height = h * 0.18F;
        float stepH = height / steps;
        for (int i = 0; i < steps; i++) {
            float fade = 1.0F - i / (float) steps;
            band(0.0F, i * stepH, w, stepH + 1.0F, argb(0, 0, 0, (int) (13.0F * fade)));
            band(0.0F, h - (i + 1) * stepH, w, stepH + 1.0F, argb(0, 0, 0, (int) (15.0F * fade)));
        }
    }

    // ------------------------------------------------------------------ примитивы

    private static void band(float x, float y, float w, float h, int argb) {
        if (w <= 0.0F || h <= 0.0F) {
            return;
        }
        UiDrawing.drawRoundedRect(Render2D.px(x), Render2D.px(y), Render2D.px(w), Render2D.px(h), 0.0F, argb);
    }

    private static void disc(float cx, float cy, float radius, int argb) {
        if (radius <= 0.0F) {
            return;
        }
        UiDrawing.drawRoundedRect(Render2D.px(cx - radius), Render2D.px(cy - radius),
                Render2D.px(radius * 2.0F), Render2D.px(radius * 2.0F), Render2D.px(radius), argb);
    }

    private static void pill(float x, float y, float w, float h, int argb) {
        if (w <= 0.0F || h <= 0.0F) {
            return;
        }
        UiDrawing.drawRoundedRect(Render2D.px(x), Render2D.px(y), Render2D.px(w), Render2D.px(h),
                Render2D.px(Math.min(w, h) * 0.5F), argb);
    }

    // ------------------------------------------------------------------ утилиты

    private static float mod(float value, float span) {
        if (span <= 0.0F) {
            return 0.0F;
        }
        float result = value % span;
        return result < 0.0F ? result + span : result;
    }

    private static float clamp(float value, float min, float max) {
        return value < min ? min : (value > max ? max : value);
    }

    private static float hash01(float seed) {
        float value = (float) Math.sin(seed * 12.9898F) * 43758.547F;
        return value - (float) Math.floor(value);
    }

    private static int channel(int value) {
        return value < 0 ? 0 : (value > 255 ? 255 : value);
    }

    private static int argb(int r, int g, int b, int a) {
        return (channel(a) << 24) | (channel(r) << 16) | (channel(g) << 8) | channel(b);
    }

    private static int withAlpha(int color, float multiplier) {
        int alpha = (int) (((color >>> 24) & 0xFF) * clamp(multiplier, 0.0F, 1.0F));
        return (channel(alpha) << 24) | (color & 0x00FFFFFF);
    }

    private static int darker(int color, float amount) {
        return mix(color, 0xFF000000, amount);
    }

    private static int mix(int from, int to, float t) {
        float k = clamp(t, 0.0F, 1.0F);
        int a = (int) (((from >>> 24) & 0xFF) + (((to >>> 24) & 0xFF) - ((from >>> 24) & 0xFF)) * k);
        int r = (int) (((from >>> 16) & 0xFF) + (((to >>> 16) & 0xFF) - ((from >>> 16) & 0xFF)) * k);
        int g = (int) (((from >>> 8) & 0xFF) + (((to >>> 8) & 0xFF) - ((from >>> 8) & 0xFF)) * k);
        int b = (int) ((from & 0xFF) + ((to & 0xFF) - (from & 0xFF)) * k);
        return argb(r, g, b, a);
    }
}
