package sg.hud;

import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.events.impl.HudRenderEvent;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.events.EventListener;
import sg.ec.ClientFonts;

public class CoordinatesHud extends HudElement {
   private final @com.google.gson.annotations.SerializedName("В诶")  EventListener<HudRenderEvent> renderListener = this::renderCoordinates;
   private static final String ELEMENT_NAME = "Coordinates";
   private static final float INITIAL_X = 41.0F;
   private static final float INITIAL_Y = 92.0F;
   private static final float INITIAL_WIDTH = 100.0F;
   private static final float INITIAL_HEIGHT = 100.0F;
   private static final String HUD_OPTION_LABEL = "Координаты";
   private static final String MEASURE_ICON_GLYPH = "$";
   private static final float MEASURE_ICON_SIZE = 16.0F;
   private static final float WIDTH_FIXED_PADDING = 28.0F;
   private static final float MEASURE_TEXT_SIZE = 14.0F;
   private static final float BACKGROUND_HEIGHT = 35.0F;
   private static final float WINDOW_CORNER_RADIUS = 12.0F;
   private static final String ICON_GLYPH = "$";
   private static final float ICON_OFFSET_X = 10.0F;
   private static final float ICON_OFFSET_Y = 8.0F;
   private static final float TEXT_OFFSET_X = 18.0F;
   private static final float TEXT_OFFSET_Y = 8.0F;
   private static final float ELEMENT_HEIGHT = 33.0F;

   public void renderCoordinates(HudRenderEvent event) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(HUD_OPTION_LABEL)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         String coordinatesText = "XYZ: " + (int)MINECRAFT_CLIENT.player.getX() + ", " + (int)MINECRAFT_CLIENT.player.getY() + ", " + (int)MINECRAFT_CLIENT.player.getZ();
         float iconWidth = ClientFonts.getIconsFont().getFontFace().measureTextWidth(MEASURE_ICON_GLYPH, MEASURE_ICON_SIZE, 0.0F);
         float width = WIDTH_FIXED_PADDING + iconWidth + ClientFonts.getMediumFont().getFontFace().measureTextWidth(coordinatesText, MEASURE_TEXT_SIZE, 0.0F);
         float x = this.getX();
         float y = this.getY();
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

         try {
            UiBatchDrawing.drawHudWindowBackground(x, y - 1.0F, width, BACKGROUND_HEIGHT, WINDOW_CORNER_RADIUS);
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ICON_GLYPH, x + ICON_OFFSET_X, y + ICON_OFFSET_Y, 16, PanelBackgroundRenderer.method_5(3));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), coordinatesText, x + TEXT_OFFSET_X + iconWidth, y + TEXT_OFFSET_Y, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            this.popGuiScaleTransform();
         }

         this.setWidth(width);
         this.setHeight(ELEMENT_HEIGHT);
      }
   }

   public CoordinatesHud() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
   }
}
