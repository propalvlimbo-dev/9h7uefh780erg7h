package sg.gui.widgets;

import org.joml.Vector2f;
import settings.render.ClientLocalization;
import sg.rotation.AimGeometryMath;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.gui.widgets.SettingWidget;
import sg.util.time.ColorMath;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.RangeSetting;

public class RangeSettingWidget extends SettingWidget<RangeSetting> {
   private @com.google.gson.annotations.SerializedName("ец")  boolean draggingLowerBound;
   private @com.google.gson.annotations.SerializedName("ея")  boolean draggingUpperBound;
   private static final float INITIAL_HEIGHT = 34.0F;
   private static final double LABEL_ALPHA = 255.0;
   private static final double VALUE_TEXT_ALPHA = 255.0;
   private static final float TRACK_OFFSET_Y = 23.0F;
   private static final float TRACK_HEIGHT = 5.0F;
   private static final double TRACK_ALPHA = 255.0;
   private static final float SELECTED_TRACK_OFFSET_Y = 23.0F;
   private static final float SELECTED_TRACK_HEIGHT = 5.0F;
   private static final double SELECTED_TRACK_ALPHA = 255.0;
   private static final float LOWER_KNOB_OFFSET_X = 4.0F;
   private static final float LOWER_KNOB_OFFSET_Y = 20.0F;
   private static final float LOWER_KNOB_WIDTH = 12.0F;
   private static final float LOWER_KNOB_HEIGHT = 12.0F;
   private static final float LOWER_KNOB_CORNER_RADIUS = 5.0F;
   private static final double LOWER_KNOB_ALPHA = 255.0;
   private static final float UPPER_KNOB_OFFSET_X = 4.0F;
   private static final float UPPER_KNOB_OFFSET_Y = 20.0F;
   private static final float UPPER_KNOB_WIDTH = 12.0F;
   private static final float UPPER_KNOB_HEIGHT = 12.0F;
   private static final float UPPER_KNOB_CORNER_RADIUS = 5.0F;
   private static final double UPPER_KNOB_ALPHA = 255.0;
   private static final float ELEMENT_HEIGHT = 34.0F;
   private static final float LOWER_HITBOX_OFFSET_Y = 22.0F;
   private static final double LOWER_HITBOX_HEIGHT = 8.0;
   private static final float UPPER_HITBOX_OFFSET_Y = 22.0F;
   private static final float UPPER_HITBOX_WIDTH_SUBTRACTION = 8.0F;
   private static final double UPPER_HITBOX_HEIGHT = 8.0;

   private float valueFromMouseX(float sliderX, float mouseX) {
      float value = (mouseX - sliderX) / this.width * (this.setting.getBounds().y - this.setting.getBounds().x) + this.setting.getBounds().x;
      value = (float)AimGeometryMath.roundToStep(value, this.setting.getStep());
      return Math.clamp(value, this.setting.getBounds().x, this.setting.getBounds().y);
   }

   private Vector2f getBoundFractions() {
      float valueRange = this.setting.getBounds().y - this.setting.getBounds().x;
      return new Vector2f((this.setting.getValue().x - this.setting.getBounds().x) / valueRange, (this.setting.getValue().y - this.setting.getBounds().x) / valueRange);
   }

   public RangeSettingWidget(RangeSetting setting) {
      super(setting, INITIAL_HEIGHT);
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      float selectedTrackWidth = this.width * this.getBoundFractions().y - this.width * this.getBoundFractions().x;
      if (WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y + LOWER_HITBOX_OFFSET_Y, this.width * this.getBoundFractions().x + selectedTrackWidth / 2.0F, LOWER_HITBOX_HEIGHT)) {
         this.draggingLowerBound = true;
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_GRAB);
      } else if (WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width * this.getBoundFractions().y - selectedTrackWidth / 2.0F, this.y + UPPER_HITBOX_OFFSET_Y, this.width - this.getBoundFractions().y - UPPER_HITBOX_WIDTH_SUBTRACTION, UPPER_HITBOX_HEIGHT)) {
         this.draggingUpperBound = true;
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_GRAB);
      }
   }

   @Override
   public void tick() {
   }

   @Override
   public void mouseReleased(float mouseX, float mouseY, int button) {
      if (this.draggingLowerBound || this.draggingUpperBound) {
         ClientSounds.к(ClientSounds.GuiSound.SLIDER_RELEASE);
      }

      this.draggingLowerBound = false;
      this.draggingUpperBound = false;
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      String rangeText = this.setting.getValue().x + " - " + this.setting.getValue().y;
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y, 13, ColorMath.packArgb(206, 207, 210, LABEL_ALPHA));
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         rangeText,
         this.x + this.width - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), rangeText, 12),
         this.y,
         12,
         ColorMath.packArgb(148, 151, 156, VALUE_TEXT_ALPHA)
      );
      UiDrawing.drawRoundedRect(this.x, this.y + TRACK_OFFSET_Y, this.width, TRACK_HEIGHT, 2.0F, ColorMath.packArgb(34, 38, 47, TRACK_ALPHA));
      float selectedTrackWidth = this.width * this.getBoundFractions().y - this.width * this.getBoundFractions().x;
      UiDrawing.drawRoundedRect(this.x + this.width * this.getBoundFractions().x, this.y + SELECTED_TRACK_OFFSET_Y, selectedTrackWidth, SELECTED_TRACK_HEIGHT, 2.0F, ColorMath.packArgb(228, 41, 75, SELECTED_TRACK_ALPHA));
      UiDrawing.drawRoundedRect(this.x + this.width * this.getBoundFractions().x - LOWER_KNOB_OFFSET_X, this.y + LOWER_KNOB_OFFSET_Y, LOWER_KNOB_WIDTH, LOWER_KNOB_HEIGHT, LOWER_KNOB_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, LOWER_KNOB_ALPHA));
      UiDrawing.drawRoundedRect(this.x + this.width * this.getBoundFractions().y - UPPER_KNOB_OFFSET_X, this.y + UPPER_KNOB_OFFSET_Y, UPPER_KNOB_WIDTH, UPPER_KNOB_HEIGHT, UPPER_KNOB_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, UPPER_KNOB_ALPHA));
      float previousLowerBound = this.setting.getValue().x;
      float previousUpperBound = this.setting.getValue().y;
      if (this.draggingLowerBound) {
         this.setting.getValue().x = Math.min(this.valueFromMouseX(this.x, mouseX), this.setting.getValue().y - this.setting.getMinimumGap());
      } else if (this.draggingUpperBound) {
         this.setting.getValue().y = Math.max(this.valueFromMouseX(this.x, mouseX), this.setting.getValue().x + this.setting.getMinimumGap());
      }

      if (Float.compare(previousLowerBound, this.setting.getValue().x) != 0 || Float.compare(previousUpperBound, this.setting.getValue().y) != 0) {
         ClientSounds.к(ClientSounds.GuiSound.SLIDER, this.draggingLowerBound ? this.getBoundFractions().x : this.getBoundFractions().y);
      }

      this.height = ELEMENT_HEIGHT;
   }
}
