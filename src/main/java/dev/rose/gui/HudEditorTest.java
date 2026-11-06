package dev.rose.gui;

import dev.rose.RoseClient;
import java.util.Locale;


final class HudEditorTest {
   private static final long FIRST_STEP_MS = 4000L;
   private static final long STEP_MS = 1300L;
   private final HudEditorScreen screen;
   private final long start = System.currentTimeMillis();
   private int step;
   private int frames;
   private long measureStart = System.nanoTime();

   HudEditorTest(HudEditorScreen screen) {
      this.screen = screen;
      RoseClient.LOGGER.info("[Rose][hudtest] started");
   }

   void tick() {
      this.frames++;
      if (System.currentTimeMillis() - this.start >= 4000L + this.step * 1300L) {
         switch (this.step) {
            case 0:
               this.screen.testHoverElement("watermark");
               break;
            case 1:
               this.screen.testPressElement("watermark", 0.5F, 0.5F);
               break;
            case 2:
               this.screen.testDragTo(0.5F, 0.22F);
               break;
            case 3:
               this.screen.testDragBy(40.0F, 0.0F);
               break;
            case 4:
               this.screen.testDragTo(0.99F, 0.01F);
               break;
            case 5:
               this.screen.testRelease();
               break;
            case 6:
               this.screen.testScrollElement("watermark", 1.0);
               break;
            case 7:
               this.screen.testScrollElement("watermark", 1.0);
               break;
            case 8:
               this.screen.testKey(90, 2);
               break;
            case 9:
               this.screen.testKey(90, 3);
               break;
            case 10:
               this.screen.testClick("tool:grid");
               break;
            case 11:
               this.screen.testClick("tool:layouts");
               break;
            case 12:
               this.screen.testClick("pop:layout:focus");
               break;
            case 13:
               this.screen.testClick("tool:elements");
               break;
            case 14:
               this.screen.testClick("pop:element:watermark");
               break;
            case 15:
               this.screen.testClick("pop:element:watermark");
               break;
            case 16:
               this.screen.testKey(256, 0);
               break;
            case 17:
               this.screen.testRightClickElement("watermark");
               break;
            case 18:
               this.screen.testShot("hud_inspector_top");
               break;
            case 19:
               this.screen.testClick("setting:watermark.scale");
               break;
            case 20:
               this.screen.testScrollInspector(-3.0);
               break;
            case 21:
               this.screen.testShot("hud_inspector_scrolled");
               break;
            case 22:
               this.screen.testPressHandle(2);
               break;
            case 23:
               this.screen.testDragBy(34.0F, 10.0F);
               break;
            case 24:
               this.screen.testRelease();
               break;
            case 25:
               this.screen.testClick("tool:grid");
               break;
            case 26:
               this.screen.testKey(256, 0);
               break;
            case 27:
               this.screen.testKey(256, 0);
               break;
            case 28:
               this.screen.testClick("tool:done");
               RoseClient.LOGGER.info("[Rose][hudtest] finished");
               break;
            default:
               return;
         }

         double seconds = (System.nanoTime() - this.measureStart) / 1.0E9;
         RoseClient.LOGGER.info("[Rose][hudtest] step {} done, {} fps", this.step, String.format(Locale.ROOT, "%.0f", this.frames / seconds));
         this.frames = 0;
         this.measureStart = System.nanoTime();
         this.step++;
      }
   }
}
