package sg.hud;

import net.minecraft.util.Mth;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.movement.TimerModule;
import sg.render.pipeline.UiBatchDrawing;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class TimerHudElement extends HudElement {
   private static final float field_4ы = 35.0F;
   private static final float field_4й = 13.5F;
   private final @com.google.gson.annotations.SerializedName("сс")  FloatAnimation progressAnimation;
   private final @com.google.gson.annotations.SerializedName("ЯХ")  EventListener<HudRenderEvent> hudRenderListener;
   private static final String ELEMENT_NAME = "Timer";
   private static final float INITIAL_X = 65.0F;
   private static final float INITIAL_Y = 427.0F;
   private static final float INITIAL_WIDTH = 100.0F;
   private static final float INITIAL_HEIGHT = 33.0F;
   private static final float PROGRESS_ANIMATION_SPEED = 18.0F;
   private static final String HUD_OPTION_NAME = "Таймер";
   private static final String ICON_MEASUREMENT_TEXT = "T";
   private static final float ICON_MEASUREMENT_SIZE = 16.0F;
   private static final float VALUE_MEASUREMENT_SIZE = 14.0F;
   private static final float WIDTH_LEADING_SPACE = 28.0F;
   private static final float WIDTH_TRAILING_SPACE = 22.0F;
   private static final float BACKGROUND_HEIGHT = 35.0F;
   private static final float BACKGROUND_CORNER_RADIUS = 12.0F;
   private static final String ICON_TEXT = "T";
   private static final float ICON_X_OFFSET = 10.0F;
   private static final float ICON_Y_OFFSET = 8.0F;
   private static final float VALUE_X_OFFSET = 18.0F;
   private static final float VALUE_Y_OFFSET = 8.0F;
   private static final float PROGRESS_CENTER_RIGHT_INSET = 17.0F;
   private static final float PROGRESS_CENTER_Y_OFFSET = 14.5F;
   private static final float BACKGROUND_PROGRESS_X_OFFSET = 5.75F;
   private static final float BACKGROUND_PROGRESS_Y_OFFSET = 5.75F;
   private static final float BACKGROUND_PROGRESS_SIZE = 13.5F;
   private static final float BACKGROUND_PROGRESS_THICKNESS = 1.65F;
   private static final float BACKGROUND_PROGRESS_ALPHA = 0.28F;
   private static final float MIN_VISIBLE_PROGRESS = 0.001F;
   private static final float FOREGROUND_PROGRESS_X_OFFSET = 5.75F;
   private static final float FOREGROUND_PROGRESS_Y_OFFSET = 5.75F;
   private static final float FOREGROUND_PROGRESS_SIZE = 13.5F;
   private static final float FOREGROUND_PROGRESS_THICKNESS = 1.65F;
   private static final float ELEMENT_HEIGHT = 33.0F;

   public void renderTimer(HudRenderEvent event) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(HUD_OPTION_NAME)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         float targetProgress = Mth.clamp(TimerModule.ЪO / TimerModule.Ъо, 0.0F, 1.0F);
         this.progressAnimation.updateTarget(targetProgress);
         String percentageText = (int)TimerModule.ЪO + "%";
         float iconWidth = ClientFonts.getIconsFont().getFontFace().measureTextWidth(ICON_MEASUREMENT_TEXT, ICON_MEASUREMENT_SIZE, 0.0F);
         float percentageWidth = ClientFonts.getMediumFont().getFontFace().measureTextWidth(percentageText, VALUE_MEASUREMENT_SIZE, 0.0F);
         float windowWidth = WIDTH_LEADING_SPACE + iconWidth + percentageWidth + WIDTH_TRAILING_SPACE;
         float x = this.getX();
         float y = this.getY();
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getIconsFont(), ClientFonts.getMediumFont());

         try {
             HudBackground.drawBatched(x, y - 1.0F, windowWidth, BACKGROUND_HEIGHT, BACKGROUND_CORNER_RADIUS);
            int iconColor = PanelBackgroundRenderer.method_5(9);
            int textColor = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
            int progressBackgroundColor = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT);
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ICON_TEXT, x + ICON_X_OFFSET, y + ICON_Y_OFFSET, 16, iconColor);
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), percentageText, x + VALUE_X_OFFSET + iconWidth, y + VALUE_Y_OFFSET, 14, textColor);
            float progressCenterX = x + windowWidth - PROGRESS_CENTER_RIGHT_INSET;
            float progressCenterY = y + PROGRESS_CENTER_Y_OFFSET;
            float displayedProgress = this.progressAnimation.getValue();
            UiBatchDrawing.drawCircularProgress(progressCenterX - BACKGROUND_PROGRESS_X_OFFSET, progressCenterY - BACKGROUND_PROGRESS_Y_OFFSET, BACKGROUND_PROGRESS_SIZE, BACKGROUND_PROGRESS_THICKNESS, 1.0F, ColorMath.withAlpha(progressBackgroundColor, BACKGROUND_PROGRESS_ALPHA));
            if (displayedProgress > MIN_VISIBLE_PROGRESS) {
               UiBatchDrawing.drawCircularProgress(progressCenterX - FOREGROUND_PROGRESS_X_OFFSET, progressCenterY - FOREGROUND_PROGRESS_Y_OFFSET, FOREGROUND_PROGRESS_SIZE, FOREGROUND_PROGRESS_THICKNESS, displayedProgress, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
            }
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getIconsFont(), ClientFonts.getMediumFont());
            this.popGuiScaleTransform();
         }

         this.setWidth(windowWidth);
         this.setHeight(ELEMENT_HEIGHT);
      }
   }

   public TimerHudElement() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
      this.progressAnimation = new FloatAnimation(0.0F, PROGRESS_ANIMATION_SPEED);
      this.hudRenderListener = this::renderTimer;
   }
}
