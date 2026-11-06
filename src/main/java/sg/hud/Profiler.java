package sg.hud;

import java.util.Locale;
import net.minecraft.client.gui.screens.ChatScreen;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.impl.misc.Optimization;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.init.ClientTelemetry;
import sg.modules.impl.init.PanelBackgroundRenderer;

public class Profiler extends HudElement {
   private final @com.google.gson.annotations.SerializedName("西")  EventListener<HudRenderEvent> renderListener = this::renderProfiler;
   private static final String ELEMENT_NAME = "Profiler";
   private static final float INITIAL_X = 40.0F;
   private static final float INITIAL_Y = 190.0F;
   private static final float INITIAL_WIDTH = 210.0F;
   private static final float INITIAL_HEIGHT = 78.0F;
   private static final String HUD_OPTION_LABEL = "Профайлер";
   private static final String HUD_TIME_FORMAT = "HUD %.1f ms";
   private static final float ELEMENT_WIDTH = 218.0F;
   private static final float CHAT_OPEN_HEIGHT = 96.0F;
   private static final float NORMAL_HEIGHT = 78.0F;
   private static final float WINDOW_CORNER_RADIUS = 12.0F;
   private static final String ICON_GLYPH = "U";
   private static final float ICON_OFFSET_X = 10.0F;
   private static final float ICON_OFFSET_Y = 8.0F;
   private static final float FPS_TEXT_OFFSET_X = 32.0F;
   private static final float FPS_TEXT_OFFSET_Y = 7.0F;
   private static final float LOW_FPS_TEXT_OFFSET_X = 118.0F;
   private static final float LOW_FPS_TEXT_OFFSET_Y = 8.0F;
   private static final float LATENCY_TEXT_RIGHT_MARGIN = 14.0F;
   private static final float LATENCY_MEASURE_TEXT_SIZE = 12.0F;
   private static final float LATENCY_TEXT_OFFSET_Y = 8.0F;
   private static final float GRAPH_OFFSET_X = 12.0F;
   private static final float GRAPH_OFFSET_Y = 30.0F;
   private static final float GRAPH_HORIZONTAL_PADDING = 24.0F;
   private static final float GRAPH_HEIGHT = 22.0F;
   private static final float HUD_TIME_TEXT_OFFSET_X = 12.0F;
   private static final float HUD_TIME_TEXT_OFFSET_Y = 56.0F;
   private static final float ENTITY_TEXT_OFFSET_X = 108.0F;
   private static final float ENTITY_TEXT_OFFSET_Y = 56.0F;
   private static final float MEMORY_TEXT_RIGHT_MARGIN = 14.0F;
   private static final float MEMORY_MEASURE_TEXT_SIZE = 11.0F;
   private static final float MEMORY_TEXT_OFFSET_Y = 56.0F;
   private static final float PACKET_TEXT_OFFSET_X = 12.0F;
   private static final float PACKET_TEXT_OFFSET_Y = 74.0F;
   private static final float OPTIMIZATION_TEXT_RIGHT_MARGIN = 14.0F;
   private static final float OPTIMIZATION_MEASURE_TEXT_SIZE = 11.0F;
   private static final float OPTIMIZATION_TEXT_OFFSET_Y = 74.0F;
   private static final float GRAPH_CORNER_RADIUS = 6.0F;
   private static final float GRAPH_BACKGROUND_ALPHA = 0.55F;
   private static final float GRAPH_MIN_SCALE_MILLIS = 8.0F;
   private static final float GRAPH_PREVIOUS_SAMPLE_BOTTOM_MARGIN = 3.0F;
   private static final float GRAPH_PREVIOUS_SAMPLE_VERTICAL_PADDING = 6.0F;
   private static final float GRAPH_CURRENT_SAMPLE_BOTTOM_MARGIN = 3.0F;
   private static final float GRAPH_CURRENT_SAMPLE_VERTICAL_PADDING = 6.0F;
   private static final float GRAPH_MIN_SEGMENT_WIDTH = 1.4F;
   private static final float GRAPH_MIN_SEGMENT_HEIGHT = 1.6F;
   private static final float GRAPH_SEGMENT_HEIGHT_PADDING = 1.2F;
   private static final float GRAPH_SEGMENT_CORNER_RADIUS = 0.6F;
   private static final String OPTIMIZATION_OFF_TEXT = "OPT off";
   private static final String OPTIMIZATION_ON_TEXT = "OPT on";

   private String getOptimizationStatusText() {
      try {
         Optimization optimization = RoseClient.getInstance().getServices().getModules().getOptimization();
         return optimization != null && optimization.isEnabled() ? OPTIMIZATION_ON_TEXT : OPTIMIZATION_OFF_TEXT;
      } catch (Throwable ignored) {
         return "";
      }
   }

   public Profiler() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
   }

   public void renderProfiler(HudRenderEvent event) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(HUD_OPTION_LABEL)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         ClientTelemetry telemetry = ClientTelemetry.и_4109462f();
         boolean chatOpen = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen;
         float[] frameTimesMillis = telemetry.и_06b4556e();
         String fpsText = telemetry.Ж_a83dd0cc() + " FPS";
         String lowFpsText = "1% " + Math.round(telemetry.З_f67ab10a());
         String latencyText = telemetry.З_a83dd0cc() + " ms";
         String hudTimeText = String.format(Locale.ROOT, HUD_TIME_FORMAT, telemetry.э_f67ab10a());
         String entityText = "ENT " + telemetry.з_a83dd0cc();
         String memoryText = telemetry.method_6_6da43b94() + "/" + telemetry.щ() + "M";
         String packetText = chatOpen ? "IN " + telemetry.э_a83dd0cc() + "  OUT " + telemetry.р() : "";
         float width = ELEMENT_WIDTH;
         float height = chatOpen ? CHAT_OPEN_HEIGHT : NORMAL_HEIGHT;
         float x = this.getX();
         float y = this.getY();
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

         try {
            UiBatchDrawing.drawHudWindowBackground(x, y - 1.0F, width, height, WINDOW_CORNER_RADIUS);
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ICON_GLYPH, x + ICON_OFFSET_X, y + ICON_OFFSET_Y, 16, PanelBackgroundRenderer.method_5(11));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), fpsText, x + FPS_TEXT_OFFSET_X, y + FPS_TEXT_OFFSET_Y, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), lowFpsText, x + LOW_FPS_TEXT_OFFSET_X, y + LOW_FPS_TEXT_OFFSET_Y, 12, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
            UiBatchDrawing.drawText(
               ClientFonts.getMediumFont(),
               latencyText,
               x + width - LATENCY_TEXT_RIGHT_MARGIN - ClientFonts.getMediumFont().getFontFace().measureTextWidth(latencyText, LATENCY_MEASURE_TEXT_SIZE, 0.0F),
               y + LATENCY_TEXT_OFFSET_Y,
               12,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
            );
            this.drawFrameTimeGraph(x + GRAPH_OFFSET_X, y + GRAPH_OFFSET_Y, width - GRAPH_HORIZONTAL_PADDING, GRAPH_HEIGHT, frameTimesMillis);
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), hudTimeText, x + HUD_TIME_TEXT_OFFSET_X, y + HUD_TIME_TEXT_OFFSET_Y, 11, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), entityText, x + ENTITY_TEXT_OFFSET_X, y + ENTITY_TEXT_OFFSET_Y, 11, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
            UiBatchDrawing.drawText(
               ClientFonts.getMediumFont(),
               memoryText,
               x + width - MEMORY_TEXT_RIGHT_MARGIN - ClientFonts.getMediumFont().getFontFace().measureTextWidth(memoryText, MEMORY_MEASURE_TEXT_SIZE, 0.0F),
               y + MEMORY_TEXT_OFFSET_Y,
               11,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
            );
            if (chatOpen) {
               String optimizationText = this.getOptimizationStatusText();
               UiBatchDrawing.drawText(ClientFonts.getMediumFont(), packetText, x + PACKET_TEXT_OFFSET_X, y + PACKET_TEXT_OFFSET_Y, 11, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
               UiBatchDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  optimizationText,
                  x + width - OPTIMIZATION_TEXT_RIGHT_MARGIN - ClientFonts.getMediumFont().getFontFace().measureTextWidth(optimizationText, OPTIMIZATION_MEASURE_TEXT_SIZE, 0.0F),
                  y + OPTIMIZATION_TEXT_OFFSET_Y,
                  11,
                  ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
               );
            }
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            this.popGuiScaleTransform();
         }

         this.setWidth(width);
         this.setHeight(height);
      }
   }

   private void drawFrameTimeGraph(float x, float y, float width, float height, float[] frameTimesMillis) {
       HudBackground.drawBatched(x, y, width, height, GRAPH_CORNER_RADIUS);
      if (frameTimesMillis.length >= 2) {
         float maxFrameTimeMillis = GRAPH_MIN_SCALE_MILLIS;

         for (float frameTimeMillis : frameTimesMillis) {
            maxFrameTimeMillis = Math.max(maxFrameTimeMillis, frameTimeMillis);
         }

         float sampleSpacing = width / Math.max(1, frameTimesMillis.length - 1);
         int graphColor = PanelBackgroundRenderer.method_5(11);

         for (int sampleIndex = 1; sampleIndex < frameTimesMillis.length; sampleIndex++) {
            float previousX = x + (sampleIndex - 1) * sampleSpacing;
            float currentX = x + sampleIndex * sampleSpacing;
            float previousY = y + height - GRAPH_PREVIOUS_SAMPLE_BOTTOM_MARGIN - frameTimesMillis[sampleIndex - 1] / maxFrameTimeMillis * (height - GRAPH_PREVIOUS_SAMPLE_VERTICAL_PADDING);
            float currentY = y + height - GRAPH_CURRENT_SAMPLE_BOTTOM_MARGIN - frameTimesMillis[sampleIndex] / maxFrameTimeMillis * (height - GRAPH_CURRENT_SAMPLE_VERTICAL_PADDING);
            UiBatchDrawing.drawRoundedRect(previousX, Math.min(previousY, currentY), Math.max(GRAPH_MIN_SEGMENT_WIDTH, currentX - previousX), Math.max(GRAPH_MIN_SEGMENT_HEIGHT, Math.abs(currentY - previousY) + GRAPH_SEGMENT_HEIGHT_PADDING), GRAPH_SEGMENT_CORNER_RADIUS, graphColor);
         }
      }
   }
}
