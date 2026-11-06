package sg.gui.widgets;

import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.settings.SectionHeaderSetting;

public class SectionHeaderSettingWidget extends SettingWidget<SectionHeaderSetting> {
   private static final float INITIAL_HEIGHT = 22.0F;
   private static final float LABEL_MEASURE_FONT_SIZE = 17.0F;
   private static final float ELEMENT_HEIGHT = 22.0F;

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
   }

   @Override
   public void keyPressed(int keyCode) {
   }

   @Override
   public void tick() {
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         ClientLocalization.translate(this.setting.getName()),
         this.x + this.width / 2.0F - ClientFonts.getMediumFont().getFontFace().measureTextWidth(ClientLocalization.translate(this.setting.getName()), LABEL_MEASURE_FONT_SIZE, 0.0F) / 2.0F,
         this.y + 1.0F,
         17,
         ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT)
      );
      this.height = ELEMENT_HEIGHT;
   }

   @Override
   public void charTyped(String text, int codePoint, int modifiers) {
   }

   public SectionHeaderSettingWidget(SectionHeaderSetting setting) {
      super(setting, INITIAL_HEIGHT);
   }
}
