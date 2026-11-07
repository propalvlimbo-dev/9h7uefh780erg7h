package sg.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ColorMath;
import sg.util.time.ThemeEditor;

/**
 * Всплывающая подсказка при наведении: заголовок и текст в плашке у курсора.
 * Появляется с небольшой задержкой, чтобы не мигала при движении мыши.
 */
public final class HoverHint {
   private static final float PADDING = 8.0F;
   private static final float LINE_HEIGHT = 12.0F;
   private static final float TITLE_LINE = 13.0F;
   private static final int TITLE_SIZE = 13;
   private static final int BODY_SIZE = 12;
   private static final float MAX_WIDTH = 205.0F;
   private static final long DELAY_MS = 140L;
   private static final float FADE_SPEED = 130.0F;
   private static final float RADIUS = 6.0F;

   private static String shownKey;
   private static long shownAt;

   private HoverHint() {
   }

   public static void reset() {
      shownKey = null;
   }

   public record Content(String key, String title, String body) {
   }

   public static void render(float mouseX, float mouseY, float left, float top, float right, float bottom, Content content) {
      if (content == null || content.title() == null || content.title().isBlank()) {
         reset();
         return;
      }

      long now = System.currentTimeMillis();
      if (!content.key().equals(shownKey)) {
         shownKey = content.key();
         shownAt = now;
      }

      float appear = Math.clamp((float)(now - shownAt - DELAY_MS) / FADE_SPEED, 0.0F, 1.0F);
      if (appear <= 0.01F) {
         return;
      }

      List<String> lines = wrap(content.body() == null ? "" : content.body(), MAX_WIDTH);
      float titleWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), content.title(), TITLE_SIZE);
      float textWidth = 0.0F;

      for (String line : lines) {
         textWidth = Math.max(textWidth, UiDrawing.getTextWidth(ClientFonts.getMediumFont(), line, BODY_SIZE));
      }

      float width = Math.max(titleWidth, textWidth) + PADDING * 2.0F;
      float height = TITLE_LINE + lines.size() * LINE_HEIGHT + PADDING * 2.0F - 3.0F;
      float x = mouseX + 14.0F;
      float y = mouseY + 16.0F;
      if (x + width > right - 4.0F) {
         x = mouseX - 14.0F - width;
      }

      if (y + height > bottom - 4.0F) {
         y = mouseY - 16.0F - height;
      }

      x = Math.clamp(x, left + 4.0F, Math.max(left + 4.0F, right - width - 4.0F));
      y = Math.clamp(y, top + 4.0F, Math.max(top + 4.0F, bottom - height - 4.0F));

      // Цвета заданы жёстко, чтобы подсказка не зависела от темы и её альфы.
      int glow = (Math.clamp(Math.round(96.0F * appear), 0, 255) << 24);
      int background = (Math.clamp(Math.round(244.0F * appear), 0, 255) << 24) | 0x120F14;
      int border = (Math.clamp(Math.round(30.0F * appear), 0, 255) << 24) | 0xFFFFFF;
      int titleColor = (Math.clamp(Math.round(255.0F * appear), 0, 255) << 24) | 0xFFFFFF;
      int bodyColor = (Math.clamp(Math.round(216.0F * appear), 0, 255) << 24) | 0xC9C1C7;
      UiDrawing.drawRoundedRect(x, y + 2.0F, width, height, RADIUS, glow);
      UiDrawing.drawRoundedRect(x, y, width, height, RADIUS, background);
      UiDrawing.drawOutline(x, y, width, height, 1.0F, RADIUS, border);
      // рубиновая полоска слева — акцент подсказки
      int accent = (Math.clamp(Math.round(235.0F * appear), 0, 255) << 24) | 0xE4294B;
      UiDrawing.drawRoundedRect(x + 1.0F, y + 6.0F, 1.8F, height - 12.0F, 0.9F, accent);
      UiDrawing.drawText(ClientFonts.getMediumFont(), content.title(), x + PADDING, y + PADDING - 1.0F, TITLE_SIZE, titleColor);

      for (int i = 0; i < lines.size(); i++) {
         UiDrawing.drawText(
            ClientFonts.getMediumFont(), lines.get(i), x + PADDING, y + PADDING + TITLE_LINE + i * LINE_HEIGHT, BODY_SIZE, bodyColor
         );
      }
   }

   private static List<String> wrap(String text, float maxWidth) {
      List<String> lines = new ArrayList<>();

      for (String paragraph : text.split("\n")) {
         if (paragraph.isBlank()) {
            continue;
         }

         StringBuilder line = new StringBuilder();

         for (String word : paragraph.trim().split(" ")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (!line.isEmpty() && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), candidate, BODY_SIZE) > maxWidth) {
               lines.add(line.toString());
               line.setLength(0);
               line.append(word);
            } else {
               line.setLength(0);
               line.append(candidate);
            }
         }

         if (!line.isEmpty()) {
            lines.add(line.toString());
         }
      }

      return lines;
   }
}
