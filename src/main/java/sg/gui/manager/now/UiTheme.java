package sg.gui.manager.now;

import java.util.ArrayList;
import java.util.List;

public final class UiTheme {
    public static boolean flag = false;

    // Меню в бело-рубиновой палитре: белый текст, рубиновые акценты.
    public static final int intVal = 0xFFFFFFFF;       // Pure white
    public static final int intVal7 = 0xFFE4294B;      // Рубиновый акцент (#E4294B)
    public static final int intVal8 = 0xFFFF6B85;      // Акцент при наведении (#FF6B85)
    public static final int intVal10 = 0xFFFFFFFF;     // High contrast white
    public static final int intVal23 = 0xFFFF5C6E;     // Red / Remove
    public static final int intVal28 = 0xFFC9BFC6;     // Inactive icon color (тёплый серый)
    public static final int intVal29 = 0x99FFFFFF;     // 60% opacity white text
    public static final int intVal30 = 0x55FFFFFF;     // 33% opacity white / placeholder
    public static final int intVal31 = 0x22FFFFFF;     // Divider line
    public static final int intVal33 = 0x33FFFFFF;     // Inactive border
    public static final int intVal34 = 0x88FFFFFF;     // Active border
    public static final int intVal35 = 0xFFFF6B85;     // Ruby alert accent
    public static final int intVal36 = 0xFFC1123A;     // Глубокий рубин (тени акцента)
    public static final int intVal37 = 0x66E4294B;     // Рубиновое свечение 40%

    public static boolean matchesCriteria(float mx, float my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    public static float computeFloat(float val) {
        return val;
    }

    public static float computeFloat2(float t) {
        float clamped = Math.clamp(t, 0.0F, 1.0F);
        return 1.0F - (float) Math.pow(1.0F - clamped, 3.0F);
    }

    public static float computeFloat3(long start, long delay, long duration) {
        long elapsed = System.currentTimeMillis() - start - delay;
        if (elapsed <= 0L) return 0.0F;
        if (elapsed >= duration) return 1.0F;
        return (float) elapsed / (float) duration;
    }

    public static int computeInt(int color, float alpha) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int finalAlpha = Math.clamp(Math.round(a * alpha), 0, 255);
        return (finalAlpha << 24) | (r << 16) | (g << 8) | b;
    }

    public static int computeInt2(int c1, int c2, float t) {
        float f = Math.clamp(t, 0.0F, 1.0F);
        int a1 = (c1 >>> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >>> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = Math.round(a1 + (a2 - a1) * f);
        int r = Math.round(r1 + (r2 - r1) * f);
        int g = Math.round(g1 + (g2 - g1) * f);
        int b = Math.round(b1 + (b2 - b1) * f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void processData6(String path, float width, float height, float alpha) {
        Render2D.drawFullscreenTexture(path, width, height, alpha);
    }

    public static void processData3(String path, float x, float y, float w, float h, int color) {
        Render2D.drawTexture(path, x, y, w, h, color);
    }

    public static void processData4(long timeMillis, float width, float height) {
        Render2D.drawLoadingIndicator(timeMillis, width, height);
    }

    public static void processData7(String icon, float x, float y, float size, int color) {
        FontText.processData3(FontFace.SEMI, "x", x, y - size / 2f, size, color);
    }

    public static List<String> getList(FontFace face, String text, float size, float maxWidth) {
        List<String> list = new ArrayList<>();
        if (text == null || text.isEmpty()) return list;
        String[] words = text.split(" ");
        StringBuilder cur = new StringBuilder();
        for (String w : words) {
            String test = cur.length() == 0 ? w : cur + " " + w;
            if (FontText.computeFloat(face, test, size) <= maxWidth) {
                cur = new StringBuilder(test);
            } else {
                if (cur.length() > 0) list.add(cur.toString());
                cur = new StringBuilder(w);
            }
        }
        if (cur.length() > 0) list.add(cur.toString());
        return list;
    }
}
