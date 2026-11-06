package sg.hud;

import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ClientMetrics;
import sg.client.RoseClient;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.init.PanelBackgroundRenderer;

public class SpeedHudElement extends HudElement {
   private final @com.google.gson.annotations.SerializedName("ХБ")  EventListener<HudRenderEvent> renderListener = this::renderSpeed;
   private static final String ELEMENT_NAME = "Speed";
   private static final float INITIAL_X = 40.0F;
   private static final float INITIAL_Y = 133.0F;
   private static final float INITIAL_WIDTH = 100.0F;
   private static final float INITIAL_HEIGHT = 100.0F;
   private static final String HUD_OPTION_LABEL = "Скорость";
   private static final String MEASURE_ICON_GLYPH = "c";
   private static final float MEASURE_ICON_SIZE = 16.0F;
   private static final float WIDTH_FIXED_PADDING = 28.0F;
   private static final float MEASURE_TEXT_SIZE = 14.0F;
   private static final float BACKGROUND_HEIGHT = 35.0F;
   private static final float WINDOW_CORNER_RADIUS = 12.0F;
   private static final String ICON_GLYPH = "c";
   private static final float ICON_OFFSET_X = 10.0F;
   private static final float ICON_OFFSET_Y = 8.0F;
   private static final float TEXT_OFFSET_X = 18.0F;
   private static final float TEXT_OFFSET_Y = 8.0F;
   private static final float ELEMENT_HEIGHT = 33.0F;

   public SpeedHudElement() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
   }

   public void renderSpeed(HudRenderEvent event) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(HUD_OPTION_LABEL)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         String speedText = ClientMetrics.getMovementSpeedText() + " BPS";
         float iconWidth = ClientFonts.getIconsFont().getFontFace().measureTextWidth(MEASURE_ICON_GLYPH, MEASURE_ICON_SIZE, 0.0F);
         float width = WIDTH_FIXED_PADDING + iconWidth + ClientFonts.getMediumFont().getFontFace().measureTextWidth(speedText, MEASURE_TEXT_SIZE, 0.0F);
         float x = this.getX();
         float y = this.getY();
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

         try {
            UiBatchDrawing.drawHudWindowBackground(x, y - 1.0F, width, BACKGROUND_HEIGHT, WINDOW_CORNER_RADIUS);
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ICON_GLYPH, x + ICON_OFFSET_X, y + ICON_OFFSET_Y, 16, PanelBackgroundRenderer.method_5(11));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), speedText, x + TEXT_OFFSET_X + iconWidth, y + TEXT_OFFSET_Y, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            this.popGuiScaleTransform();
         }

         this.setWidth(width);
         this.setHeight(ELEMENT_HEIGHT);
      }
   }
}
