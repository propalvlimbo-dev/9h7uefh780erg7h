package sg.hud;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.command.impl.GpsCommand;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.events.EventListener;
import sg.ec.ClientFonts;

public class GpsHud extends HudElement {
   private final @com.google.gson.annotations.SerializedName("сЖ")  EventListener<HudRenderEvent> renderListener = this::renderGps;
   private static final String ELEMENT_NAME = "GPS";
   private static final float INITIAL_X = 300.0F;
   private static final float INITIAL_Y = 100.0F;
   private static final float INITIAL_WIDTH = 80.0F;
   private static final float INITIAL_HEIGHT = 39.0F;
   private static final double BEARING_OFFSET_DEGREES = 90.0;
   private static final float ELEMENT_HEIGHT = 39.0F;
   private static final float ARROW_SLOT_SIZE = 30.0F;
   private static final float сВЖ = 20.0F;
   private static final float сВм = 7.0F;
   private static final float сВК = 10.0F;
   private static final float WINDOW_CORNER_RADIUS = 12.0F;
   private static final float ARROW_SLOT_OFFSET_X = 6.0F;
   private static final float ARROW_SLOT_OFFSET_Y = 5.0F;
   private static final float ARROW_TEXTURE_SIZE = 55.0F;
   private static final float TEXTURE_CENTER_FACTOR_X = 0.5F;
   private static final float TEXTURE_CENTER_FACTOR_Y = 0.5F;
   private static final float ROTATION_PIVOT_CENTER_FACTOR_X = 0.5F;
   private static final float ROTATION_PIVOT_CENTER_FACTOR_Y = 0.5F;
   private static final float NDC_MIN_X = -1.0F;
   private static final String ARROW_TEXTURE_ID = "hud/arrow";
   private static final float TEXT_OFFSET_AFTER_ARROW_SLOT = 18.0F;
   private static final float DISTANCE_TEXT_CENTER_FACTOR = 0.5F;
   private static final float DISTANCE_TEXT_OFFSET_Y = 7.0F;
   private static final float COORDINATES_TEXT_CENTER_FACTOR = 0.5F;
   private static final float COORDINATES_TEXT_OFFSET_Y = 21.0F;

   public void renderGps(HudRenderEvent event) {
      if (GpsCommand.gpsEnabled && MINECRAFT_CLIENT.player != null) {
         double deltaX = GpsCommand.targetX - MINECRAFT_CLIENT.player.getX();
         double deltaZ = GpsCommand.targetZ - MINECRAFT_CLIENT.player.getZ();
         float distance = (float)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
         float cameraYaw = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         float relativeBearingDegrees = (float)(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - cameraYaw - BEARING_OFFSET_DEGREES);
         float screenWidth = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float screenHeight = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         String distanceText = Math.round(distance) + "m";
         String destinationCoordinatesText = Math.round(GpsCommand.targetX) + ", " + Math.round(GpsCommand.targetZ);
         byte distanceFontSize = 11;
         byte coordinatesFontSize = 8;
         float textWidth = Math.max(UiDrawing.getTextWidth(ClientFonts.getMediumFont(), distanceText, distanceFontSize), UiDrawing.getTextWidth(ClientFonts.getRegularFont(), destinationCoordinatesText, coordinatesFontSize));
         float height = ELEMENT_HEIGHT;
         float arrowSlotSize = ARROW_SLOT_SIZE;
         float width = сВЖ + arrowSlotSize + сВм + textWidth + сВК;
         this.setWidth(width);
         this.setHeight(height);
         float x = this.getX();
         float y = this.getY();
         this.pushGuiScaleTransform();

         try {
            UiDrawing.setTransform(new Matrix4f());
             HudBackground.draw(x, y, width, height, WINDOW_CORNER_RADIUS);
            float arrowSlotX = x + ARROW_SLOT_OFFSET_X;
            float arrowSlotY = y + ARROW_SLOT_OFFSET_Y;
            float textureSize = ARROW_TEXTURE_SIZE;
            float textureX = arrowSlotX + (arrowSlotSize - textureSize) * TEXTURE_CENTER_FACTOR_X;
            float textureY = arrowSlotY + (arrowSlotSize - textureSize) * TEXTURE_CENTER_FACTOR_Y;
            float pivotX = textureX + textureSize * ROTATION_PIVOT_CENTER_FACTOR_X;
            float pivotY = textureY + textureSize * ROTATION_PIVOT_CENTER_FACTOR_Y;
            Matrix4f transform = UiDrawing.getTransform();
            float pivotNdcX = NDC_MIN_X + pivotX * 2.0F / screenWidth;
            float pivotNdcY = 1.0F - pivotY * 2.0F / screenHeight;
            float aspectRatio = screenWidth / screenHeight;
            transform.translate(pivotNdcX, pivotNdcY, 0.0F)
               .scale(1.0F, aspectRatio, 1.0F)
               .rotateZ((float)(-Math.toRadians(relativeBearingDegrees)))
               .scale(1.0F, 1.0F / aspectRatio, 1.0F)
               .translate(-pivotNdcX, -pivotNdcY, 0.0F);
            UiBatchDrawing.setAdditiveBlending(true);
            UiDrawing.drawTexture(textureX, textureY - 1.0F, textureSize, textureSize, new Vector4f(0.0F), new Vector4i(-1), ClientResourceIds.getImageId(ARROW_TEXTURE_ID));
            UiDrawing.setTransform(new Matrix4f());
            float textX = arrowSlotX + arrowSlotSize + TEXT_OFFSET_AFTER_ARROW_SLOT;
            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               distanceText,
               textX + (textWidth - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), distanceText, distanceFontSize)) * DISTANCE_TEXT_CENTER_FACTOR,
               y + DISTANCE_TEXT_OFFSET_Y,
               distanceFontSize,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT)
            );
            UiDrawing.drawText(
               ClientFonts.getRegularFont(),
               destinationCoordinatesText,
               textX + (textWidth - UiDrawing.getTextWidth(ClientFonts.getRegularFont(), destinationCoordinatesText, coordinatesFontSize)) * COORDINATES_TEXT_CENTER_FACTOR,
               y + COORDINATES_TEXT_OFFSET_Y,
               coordinatesFontSize,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
            );
         } finally {
            this.popGuiScaleTransform();
         }

         UiBatchDrawing.setAdditiveBlending(false);
      } else {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      }
   }

   public GpsHud() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
   }
}
