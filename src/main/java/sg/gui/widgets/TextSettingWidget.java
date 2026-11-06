package sg.gui.widgets;

import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.util.time.ColorMath;
import sg.util.math.EasingFunctions;
import sg.modules.impl.init.TickAnimation;
import sg.settings.TextSetting;

public class TextSettingWidget extends SettingWidget<TextSetting> {
   private final @com.google.gson.annotations.SerializedName("е")  TickAnimation focusAnimation = new TickAnimation();
   private @com.google.gson.annotations.SerializedName("ВК")  boolean editing = false;
   private @com.google.gson.annotations.SerializedName("ей")  float marqueeOffset;
   private @com.google.gson.annotations.SerializedName("ВИ")  long lastRenderNanos;
   private static final float INITIAL_HEIGHT = 22.0F;
   private static final float FOCUS_TICK_STEP = 0.2F;
   private static final double Яжн = 255.0;
   private static final double ЯжЦ = 255.0;
   private static final double INACTIVE_TEXT_ALPHA = 255.0;
   private static final double ACTIVE_TEXT_ALPHA = 255.0;
   private static final float INPUT_RIGHT_OFFSET = 96.0F;
   private static final float INPUT_OUTLINE_WIDTH = 96.0F;
   private static final float INPUT_CORNER_RADIUS = 6.0F;
   private static final double INPUT_OUTLINE_ALPHA = 12.0;
   private static final float TEXT_RIGHT_OFFSET = 88.0F;
   private static final float MARQUEE_OVERFLOW_WIDTH = 80.0F;
   private static final float INPUT_HOVER_RIGHT_OFFSET = 96.0F;
   private static final double INPUT_HOVER_WIDTH = 96.0;
   private static final float INITIAL_FRAME_SECONDS = 0.016666668F;
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MAX_FRAME_SECONDS = 0.05F;
   private static final float MARQUEE_PIXELS_PER_SECOND = 38.0F;
   private static final float TEXT_CLIP_WIDTH = 80.0F;
   private static final float MARQUEE_GAP = 32.0F;
   private static final float MARQUEE_FIRST_TEXT_OFFSET_Y = 4.0F;
   private static final float MARQUEE_SECOND_TEXT_OFFSET_Y = 4.0F;
   private static final float STATIC_TEXT_OFFSET_Y = 4.0F;
   private static final float ELEMENT_HEIGHT = 22.0F;
   private static final float INPUT_HITBOX_RIGHT_OFFSET = 96.0F;
   private static final double INPUT_HITBOX_WIDTH = 96.0;
   private static final double INPUT_HITBOX_HEIGHT = 20.0;

   @Override
   public void keyPressed(int keyCode) {
      if (keyCode == 257) {
         this.editing = false;
      } else if (keyCode == 259 && this.editing && !this.setting.getValue().isEmpty()) {
         this.setting.setValue(this.setting.getValue().substring(0, this.setting.getValue().length() - 1));
      }
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.focusAnimation.configureAndInterpolate(0.0F, 1.0F, FOCUS_TICK_STEP, EasingFunctions.SINE_OUT, tickDelta);
      int var4 = ColorMath.lerpArgb(ColorMath.packArgb(55, 58, 65, Яжн), ColorMath.packArgb(75, 78, 85, ЯжЦ), this.focusAnimation.getInterpolatedValue());
      int textColor = ColorMath.lerpArgb(ColorMath.packArgb(133, 136, 142, INACTIVE_TEXT_ALPHA), ColorMath.packArgb(236, 236, 237, ACTIVE_TEXT_ALPHA), this.focusAnimation.getInterpolatedValue());
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x, this.y + 1.0F, 13, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      UiDrawing.drawOutline(this.x + this.width - INPUT_RIGHT_OFFSET, this.y, INPUT_OUTLINE_WIDTH, this.height, 1.0F, INPUT_CORNER_RADIUS, ColorMath.packArgb(255, 255, 255, INPUT_OUTLINE_ALPHA));
      float textX = this.x + this.width - TEXT_RIGHT_OFFSET;
      float textWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.setting.getValue(), 11);
      boolean textOverflowing = textWidth > MARQUEE_OVERFLOW_WIDTH;
      boolean inputHovered = WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - INPUT_HOVER_RIGHT_OFFSET, this.y, INPUT_HOVER_WIDTH, this.height);
      long nowNanos = System.nanoTime();
      float frameSeconds = this.lastRenderNanos == 0L ? INITIAL_FRAME_SECONDS : Math.clamp((float)(nowNanos - this.lastRenderNanos) / NANOS_PER_SECOND, 0.0F, MAX_FRAME_SECONDS);
      this.lastRenderNanos = nowNanos;
      if (!textOverflowing || !inputHovered && !this.editing) {
         this.marqueeOffset = 0.0F;
      } else {
         this.marqueeOffset = this.marqueeOffset + frameSeconds * MARQUEE_PIXELS_PER_SECOND;
      }

      UiDrawing.pushScissor(textX, this.y, TEXT_CLIP_WIDTH, this.height);
      if (!textOverflowing || !inputHovered && !this.editing) {
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.setting.getValue(), textX, this.y + STATIC_TEXT_OFFSET_Y, 11, textColor);
      } else {
         float marqueePeriod = textWidth + MARQUEE_GAP;
         float marqueeX = textX - this.marqueeOffset % marqueePeriod;
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.setting.getValue(), marqueeX, this.y + MARQUEE_FIRST_TEXT_OFFSET_Y, 11, textColor);
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.setting.getValue(), marqueeX + marqueePeriod, this.y + MARQUEE_SECOND_TEXT_OFFSET_Y, 11, textColor);
      }

      UiDrawing.popScissor();
      this.height = ELEMENT_HEIGHT;
   }

   @Override
   public void charTyped(String text, int codePoint, int modifiers) {
      if (this.editing) {
         this.setting.setValue(this.setting.getValue() + text);
      }
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - INPUT_HITBOX_RIGHT_OFFSET, this.y, INPUT_HITBOX_WIDTH, INPUT_HITBOX_HEIGHT) && button == 0) {
         this.editing = !this.editing;
      } else {
         this.editing = false;
      }
   }

   @Override
   public void tick() {
      this.focusAnimation.tick(this.editing);
   }

   public TextSettingWidget(TextSetting setting) {
      super(setting, INITIAL_HEIGHT);
   }
}
