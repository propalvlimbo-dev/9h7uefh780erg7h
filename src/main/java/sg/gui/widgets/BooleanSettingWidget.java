package sg.gui.widgets;

import com.google.gson.annotations.SerializedName;
import java.util.Locale;
import settings.render.ClientLocalization;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.SettingWidget;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.impl.misc.ClientSounds;
import sg.util.math.EasingFunctions;
import sg.modules.impl.init.KeybindConflictResolver;
import sg.modules.impl.init.TickAnimation;

public class BooleanSettingWidget extends SettingWidget<BooleanSetting> {
   @SerializedName("4")
   private final TickAnimation toggleAnimation = new TickAnimation();
   private @com.google.gson.annotations.SerializedName("еМ")  boolean capturingKeybind;
   private static final float INITIAL_HEIGHT = 20.0F;
   private static final float TOGGLE_TICK_STEP = 0.2F;
   private static final float TOGGLE_RIGHT_OFFSET = 32.0F;
   private static final float MIN_LABEL_WIDTH = 24.0F;
   private static final float KEYBIND_LABEL_GAP = 8.0F;
   private static final float LABEL_TOGGLE_GAP = 4.0F;
   private static final float LABEL_MEASURE_FONT_SIZE = 13.0F;
   private static final float LABEL_OFFSET_Y = 4.0F;
   private static final float KEYBIND_INACTIVE_ALPHA_FACTOR = 0.85F;
   private static final float KEYBIND_LABEL_RIGHT_GAP = 6.0F;
   private static final float KEYBIND_LABEL_OFFSET_Y = 5.0F;
   private static final double INACTIVE_KNOB_ALPHA = 255.0;
   private static final double ACTIVE_KNOB_ALPHA = 255.0;
   private static final float KNOB_TRAVEL_DISTANCE = 12.0F;
   private static final float TRACK_OFFSET_Y = 3.0F;
   private static final float TRACK_WIDTH = 30.0F;
   private static final float TRACK_HEIGHT = 18.0F;
   private static final float TRACK_CORNER_RADIUS = 7.0F;
   private static final float KNOB_RIGHT_OFFSET = 30.0F;
   private static final float KNOB_OFFSET_Y = 5.0F;
   private static final float KNOB_WIDTH = 14.0F;
   private static final float KNOB_HEIGHT = 14.0F;
   private static final float KNOB_CORNER_RADIUS = 6.0F;
   private static final float ELEMENT_HEIGHT = 20.0F;
   private static final float TOGGLE_HITBOX_RIGHT_OFFSET = 32.0F;
   private static final double TOGGLE_HITBOX_WIDTH = 30.0;
   private static final double TOGGLE_HITBOX_HEIGHT = 18.0;
   private static final float MIN_ROW_HITBOX_HEIGHT = 20.0F;
   private static final long CAPTURE_DOT_INTERVAL_MILLIS = 280L;
   private static final long CAPTURE_DOT_COUNT = 3L;
   private static final String CAPTURE_DOT = ".";
   private static final String LABEL_ELLIPSIS = "...";
   private static final String UNBOUND_KEY_LABEL = "-";
   private static final String KEY_SPACE_LABEL = "SPC";
   private static final String KEY_LEFT_SHIFT_LABEL = "LSH";
   private static final String KEY_RIGHT_SHIFT_LABEL = "RSH";
   private static final String KEY_LEFT_CONTROL_LABEL = "LCT";
   private static final String KEY_RIGHT_CONTROL_LABEL = "RCT";
   private static final String KEY_LEFT_ALT_LABEL = "LAL";
   private static final String KEY_RIGHT_ALT_LABEL = "RAL";
   private static final String KEY_ENTER_LABEL = "ENT";
   private static final String KEY_TAB_LABEL = "TAB";
   private static final String KEY_CAPS_LOCK_LABEL = "CAP";
   private static final String KEY_UP_LABEL = "UP";
   private static final String KEY_DOWN_LABEL = "DN";
   private static final String KEY_LEFT_LABEL = "LT";
   private static final String KEY_RIGHT_LABEL = "RT";
   private static final String KEY_DELETE_LABEL = "DEL";
   private static final String KEY_BACKSPACE_LABEL = "BS";

   private void applyKeybind(int keyCode) {
      this.setting.getKeybind().setKeyCode(keyCode);
      ClientSounds.к(keyCode < 0 ? ClientSounds.GuiSound.BIND_RESET : ClientSounds.GuiSound.BIND_SET);
      this.capturingKeybind = false;
      KeybindConflictResolver.б(keyCode);
      this.saveModuleConfig();
   }

   @Override
   public boolean isCapturingKeybind() {
      return this.capturingKeybind;
   }

   @Override
   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      if (!this.capturingKeybind) {
         return false;
      }

      if (button != 0) {
         this.applyKeybind(button);
      }

      return true;
   }

   private String ellipsizeLabel(String text, float maxWidth, float fontSize) {
      if (text != null && !text.isEmpty()) {
         if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), text, (int)fontSize) <= maxWidth) {
            return text;
         }

         String ellipsis = LABEL_ELLIPSIS;
         int endIndex = text.length();

         while (endIndex > 0) {
            endIndex = text.offsetByCodePoints(endIndex, -1);
            String candidate = text.substring(0, endIndex) + ellipsis;
            if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), candidate, (int)fontSize) <= maxWidth) {
               return candidate;
            }
         }

         return ellipsis;
      } else {
         return "";
      }
   }

   @Override
   public void mouseClicked(float mouseX, float mouseY, int button) {
      if (this.capturingKeybind) {
         if (button != 0) {
            this.applyKeybind(button);
         }
      } else {
         boolean toggleHovered = WorldProjection.containsPoint(mouseX, mouseY, this.x + this.width - TOGGLE_HITBOX_RIGHT_OFFSET, this.y + 2.0F, TOGGLE_HITBOX_WIDTH, TOGGLE_HITBOX_HEIGHT);
         boolean rowHovered = WorldProjection.containsPoint(mouseX, mouseY, this.x, this.y, this.width, Math.max(this.height, MIN_ROW_HITBOX_HEIGHT));
         if (button == 0) {
            if (toggleHovered) {
               this.setting.setValue(!this.setting.getValue());
               ClientSounds.к(this.setting.getValue() ? ClientSounds.GuiSound.CHECKBOX_ON : ClientSounds.GuiSound.CHECKBOX_OFF);
            }
         } else if (button == 1 && rowHovered) {
            this.capturingKeybind = true;
            ClientSounds.к(ClientSounds.GuiSound.BIND_START);
         }
      }
   }

   @Override
   public void keyPressed(int keyCode) {
      if (this.capturingKeybind) {
         if (keyCode == 256) {
            this.capturingKeybind = false;
         } else if (keyCode != 261 && keyCode != 259) {
            this.applyKeybind(keyCode);
         } else {
            this.applyKeybind(-1);
         }
      }
   }

   @Override
   public void render(float mouseX, float mouseY, float tickDelta) {
      this.toggleAnimation.configureAndInterpolate(0.0F, 1.0F, TOGGLE_TICK_STEP, EasingFunctions.SINE_OUT, tickDelta);
      int labelColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), this.toggleAnimation.getInterpolatedValue());
      float toggleX = this.x + this.width - TOGGLE_RIGHT_OFFSET;
      String keybindLabel = this.getKeybindLabel();
      float keybindLabelWidth = keybindLabel == null ? 0.0F : UiDrawing.getTextWidth(ClientFonts.getMediumFont(), keybindLabel, 10);
      float availableLabelWidth = Math.max(MIN_LABEL_WIDTH, toggleX - this.x - (keybindLabel == null ? 0.0F : keybindLabelWidth + KEYBIND_LABEL_GAP) - LABEL_TOGGLE_GAP);
      String label = this.ellipsizeLabel(ClientLocalization.translate(this.setting.getName()), availableLabelWidth, LABEL_MEASURE_FONT_SIZE);
      UiDrawing.drawText(ClientFonts.getMediumFont(), label, this.x, this.y + LABEL_OFFSET_Y, 13, labelColor);
      if (keybindLabel != null) {
         int keybindColor = KeybindConflictResolver.в(
            this.setting.getKeybind().getKeyCode(),
            this.capturingKeybind,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), KEYBIND_INACTIVE_ALPHA_FACTOR),
            ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT),
            this.setting
         );
         UiDrawing.drawText(ClientFonts.getMediumFont(), keybindLabel, toggleX - keybindLabelWidth - KEYBIND_LABEL_RIGHT_GAP, this.y + KEYBIND_LABEL_OFFSET_Y, 10, keybindColor);
      }

      int trackColor = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_STATE), this.toggleAnimation.getInterpolatedValue());
      int knobColor = ColorMath.lerpArgb(ColorMath.packArgb(125, 135, 145, INACTIVE_KNOB_ALPHA), ColorMath.packArgb(255, 255, 255, ACTIVE_KNOB_ALPHA), this.toggleAnimation.getInterpolatedValue());
      float knobOffset = KNOB_TRAVEL_DISTANCE * this.toggleAnimation.getInterpolatedValue();
      UiDrawing.drawRoundedRect(toggleX, this.y + TRACK_OFFSET_Y, TRACK_WIDTH, TRACK_HEIGHT, TRACK_CORNER_RADIUS, trackColor);
      UiDrawing.drawRoundedRect(this.x + this.width - KNOB_RIGHT_OFFSET + knobOffset, this.y + KNOB_OFFSET_Y, KNOB_WIDTH, KNOB_HEIGHT, KNOB_CORNER_RADIUS, knobColor);
      this.height = ELEMENT_HEIGHT;
   }

   private void saveModuleConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   private String formatKeybind(int keyCode) {
      if (keyCode < 0) {
         return UNBOUND_KEY_LABEL;
      }

      if (keyCode >= 0 && keyCode <= 7) {
         return "M" + (keyCode + 1);
      }

      return switch (keyCode) {
         case 32 -> KEY_SPACE_LABEL;
         case 257 -> KEY_ENTER_LABEL;
         case 258 -> KEY_TAB_LABEL;
         case 259 -> KEY_BACKSPACE_LABEL;
         case 261 -> KEY_DELETE_LABEL;
         case 262 -> KEY_RIGHT_LABEL;
         case 263 -> KEY_LEFT_LABEL;
         case 264 -> KEY_DOWN_LABEL;
         case 265 -> KEY_UP_LABEL;
         case 280 -> KEY_CAPS_LOCK_LABEL;
         case 340 -> KEY_LEFT_SHIFT_LABEL;
         case 341 -> KEY_LEFT_CONTROL_LABEL;
         case 342 -> KEY_LEFT_ALT_LABEL;
         case 344 -> KEY_RIGHT_SHIFT_LABEL;
         case 345 -> KEY_RIGHT_CONTROL_LABEL;
         case 346 -> KEY_RIGHT_ALT_LABEL;
         default -> {
            String keyName = KeyDisplayNames.О(keyCode);
            yield keyName.isEmpty() ? "K" + keyCode : keyName.toUpperCase(Locale.ROOT);
         }
      };
   }

   public BooleanSettingWidget(BooleanSetting setting) {
      super(setting, INITIAL_HEIGHT);
   }

   private String getKeybindLabel() {
      if (this.capturingKeybind) {
         return this.getCaptureDots();
      }

      int keyCode = this.setting.getKeybind().getKeyCode();
      return keyCode < 0 ? null : this.formatKeybind(keyCode);
   }

   @Override
   public void resetTransientState() {
      this.capturingKeybind = false;
   }

   @Override
   public void tick() {
      this.toggleAnimation.tick(this.setting.getValue());
   }

   private String getCaptureDots() {
      int dotCount = (int)(System.currentTimeMillis() / CAPTURE_DOT_INTERVAL_MILLIS % CAPTURE_DOT_COUNT) + 1;
      return CAPTURE_DOT.repeat(dotCount);
   }
}
