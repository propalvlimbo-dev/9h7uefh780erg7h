package sg.gui.widgets;

import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.util.time.ColorMath;
import sg.modules.impl.misc.ClientSounds;
import sg.settings.ActionSetting;

public class ActionSettingWidget extends SettingWidget<ActionSetting> {
   private final @com.google.gson.annotations.SerializedName("Ъ")  Runnable action;
   private static final float INITIAL_HEIGHT = 28.0F;
   private static final float ELEMENT_HEIGHT = 28.0F;
   private static final float HOVER_BACKGROUND_ALPHA = 0.8F;
   private static final float IDLE_BACKGROUND_ALPHA = 0.55F;
   private static final float BACKGROUND_CORNER_RADIUS = 7.0F;
   private static final float OUTLINE_CORNER_RADIUS = 7.0F;
   private static final float LABEL_OFFSET_X = 10.0F;
   private static final float LABEL_OFFSET_Y = 7.0F;
   private static final String ACTION_ICON_GLYPH = "}";
   private static final float ICON_RIGHT_OFFSET = 18.0F;
   private static final float ICON_OFFSET_Y = 8.0F;

   @Override
   public void tick() {
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (button == 0 && WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
         ClientSounds.к(ClientSounds.GuiSound.BUTTON);
         this.action.run();
      }
   }

   public ActionSettingWidget(ActionSetting setting, Runnable action) {
      super(setting, INITIAL_HEIGHT);
      this.action = action;
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.height = ELEMENT_HEIGHT;
      boolean hovered = WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, this.height);
      int backgroundColor = ColorMath.withAlpha(hovered ? ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED) : ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), hovered ? HOVER_BACKGROUND_ALPHA : IDLE_BACKGROUND_ALPHA);
      int outlineColor = hovered ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE);
      int textColor = hovered ? ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
      UiDrawing.drawRoundedRect(this.x, this.y, this.width, this.height, BACKGROUND_CORNER_RADIUS, backgroundColor);
      UiDrawing.drawOutline(this.x, this.y, this.width, this.height, 1.0F, OUTLINE_CORNER_RADIUS, outlineColor);
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(this.setting.getName()), this.x + LABEL_OFFSET_X, this.y + LABEL_OFFSET_Y, 12, textColor);
      UiDrawing.drawText(ClientFonts.getIconsFont(), ACTION_ICON_GLYPH, this.x + this.width - ICON_RIGHT_OFFSET, this.y + ICON_OFFSET_Y, 11, textColor);
   }
}
