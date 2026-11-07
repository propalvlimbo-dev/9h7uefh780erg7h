package sg.render;

/**
 * Временная диагностика пути Picture-in-Picture: помогает понять, где именно гаснут
 * превью косметики — состояние не доходит до GuiRenderer, рендерер не вызывается
 * или его отменяет ESP-миксин. Пишет не больше {@link #LIMIT} строк за запуск.
 */
public final class PreviewDiag {
   private static final int LIMIT = 24;
   private static int lines;
   private static long lastSubmit;
   private static long submitCount;
   private static long renderCount;

   private PreviewDiag() {
   }

   public static void submitted(int x0, int y0, int x1, int y1, int draws) {
      submitCount++;
      long now = System.nanoTime();
      if (lines >= LIMIT || now - lastSubmit < 1_000_000_000L) {
         return;
      }

      lastSubmit = now;
      log("submit #" + submitCount + " draws=" + draws + " rect=" + x0 + "," + y0 + "-" + x1 + "," + y1);
   }

   public static void rendered(int hats, float x, float y, float width, float height) {
      renderCount++;
      if (lines >= LIMIT || renderCount > 3 && renderCount % 20 != 0) {
         return;
      }

      log("renderToTexture #" + renderCount + " draws=" + hats + " first=" + (int)x + "," + (int)y + " size=" + (int)width + "x" + (int)height);
   }

   public static void prepared(String detail) {
      if (lines >= LIMIT) {
         return;
      }

      log("prepare ok: " + detail);
   }

   public static void cancelled(String reason) {
      if (lines >= LIMIT) {
         return;
      }

      log("prepare CANCELLED: " + reason);
   }

   private static void log(String message) {
      lines++;
      System.out.println("[rose-pip] " + message);
   }
}
