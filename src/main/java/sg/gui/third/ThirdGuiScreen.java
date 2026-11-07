package sg.gui.third;

import com.mojang.blaze3d.textures.FilterMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import sg.client.RoseClient;
import sg.ec.FontRenderer;
import sg.ec.MinecraftAccess;
import sg.modules.ClientModule;
import sg.render.WorldProjection;
import sg.render.pipeline.ThemeWave;
import sg.render.pipeline.UiDrawing;
import sg.settings.ActionSetting;
import sg.settings.BooleanSetting;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.KeybindSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.RangeSetting;
import sg.settings.SectionHeaderSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.TextSetting;
import sg.settings.ValueSetting;

/**
 * Third click gui, based on the catlavanskid client (dev.holo.catlavanskid.gui.ClickGui).
 *
 * <p>The original drew through its own GL batcher with a private font atlas. Here the very same
 * geometry, timing curves and module layout are reproduced on top of the rose ui pipeline,
 * with Rose's vector-derived logo font, First GUI fonts and separate client editor screens.</p>
 */
public class ThirdGuiScreen extends Screen implements MinecraftAccess {
   // Layout constants, identical to the original implementation.
   static final float PANEL_WIDTH = 1198.0F;
   static final float PANEL_HEIGHT = 731.0F;
   static final float PANEL_PADDING = 26.0F;
   static final float COLUMN_GAP = 22.0F;
   static final float CARD_WIDTH = (PANEL_WIDTH - 2.0F * PANEL_PADDING - 2.0F * COLUMN_GAP) / 3.0F;
   static final float CARD_HEADER = 60.0F;
   static final float BODY_TOP = 10.5F;
   static final float BODY_BOTTOM = 10.0F;
   static final float ROW_BOOL = 66.5F;
   static final float ROW_SLIDER = 82.0F;
   static final float ROW_MODE = 87.0F;
   static final float ROW_ACTION = 56.0F;
   static final float ROW_SECTION = 34.0F;
   static final float OPTION_HEIGHT = 30.0F;

   // Dock constants. The original dock was laid out for six categories, extra rose
   // categories widen it by exactly one icon slot each so every other offset stays intact.
   static final float DOCK_HEIGHT = 62.0F;
   static final float DOCK_BASE_WIDTH = 536.0F;
   static final float DOCK_ICON_SLOT = 52.0F;
   static final float DOCK_ICON_ORIGIN = 96.0F;
   static final float DOCK_BASE_CATEGORY_COUNT = 6.0F;

   private static final int HIT_TOGGLE = 0;
   private static final int HIT_BIND = 1;
   private static final int HIT_HEADER = 2;
   private static final int HIT_BOOL = 3;
   private static final int HIT_SLIDER = 4;
   private static final int HIT_MODE = 5;
   private static final int HIT_OPTION = 6;
   private static final int HIT_ACTION = 7;
   private static final int HIT_DOCK = 8;
   private static final int HIT_CHEVRON = 9;
   private static final int HIT_SETTING_BIND = 10;
   private static final int HIT_TEXT = 11;
   private static final int HIT_COLOR = 12;
   private static final int HIT_RANGE = 13;
   private static final int HIT_PICKER_ZONE_BASE = 14;
   /** Область только для подсказки: клика по ней не бывает, она зарегистрирована первой и перекрывается остальными. */
   private static final int HIT_HINT_ONLY = -1;

   private static final int PICKER_NONE = -1;
   private static final int PICKER_SATURATION = 0;
   private static final int PICKER_HUE = 1;
   private static final int PICKER_ALPHA = 2;

   private static final int RANGE_HANDLE_LOW = 0;
   private static final int RANGE_HANDLE_HIGH = 1;

   private static final float GLOBAL_ALPHA_SCALE = 255.0F;
   private static final float MIN_SCALE = 0.25F;
   private static final float ENTRANCE_STAGGER = 0.045F;
   private static final float ENTRANCE_DURATION = 0.42F;
   private static final float SCROLL_SMOOTHING = 14.0F;
   private static final float SCROLL_SETTLE = 0.05F;
   private static final float SCROLL_SPEED = 70.0F;
   private static final float FRAME_CAP = 0.1F;
   private static final float VISIBLE_EPSILON = 0.004F;
    private static final float BASE_SCALE = 0.91F;
   private static final float REFERENCE_SCALE = 3.0F;
   private static final float REFERENCE_WIDTH = 1371.0F;
   private static final float REFERENCE_HEIGHT = 873.0F;
   private static final float CARD_EDGE_MARGIN = 6.0F;
   private static final float ENTRANCE_FADE_GAIN = 1.25F;
   private static final float ENTRANCE_SHIFT = 18.0F;
   private static final float ENTRANCE_SCALE_BASE = 0.94F;
   private static final float ENTRANCE_SCALE_GAIN = 0.06F;
   private static final float CARD_REVEAL_THRESHOLD = 0.6F;
   private static final float ROW_LIVE_THRESHOLD = 0.8F;
   private static final float OPTION_LIVE_THRESHOLD = 0.5F;
   private static final float CARD_ENTRANCE_SHIFT = 20.0F;
   private static final float CHECK_POP_SCALE = 0.1F;
   private static final float CHECK_ICON_SIZE = 13.0F;
   private static final float CHECK_ICON_FADE_GAIN = 1.5F;
   private static final float CHECKBOX_SIZE = 22.0F;
   private static final float CHECKBOX_CORNER = 6.0F;
   private static final float CHECKBOX_RIGHT_MARGIN = 23.0F;
   private static final float CHECKBOX_TOP = 19.0F;
   private static final float CHECKBOX_HIT_PADDING = 4.0F;
   private static final float BIND_PILL_HEIGHT = 26.0F;
   private static final float BIND_PILL_TOP = 17.0F;
   private static final float BIND_PILL_GAP = 7.0F;
   private static final float BIND_PILL_PAD = 11.0F;
   private static final float BIND_PILL_ICON_GAP = 8.0F;
   private static final float BIND_PILL_ICON_SIZE = 15.0F;
   private static final float BIND_PILL_ICON_INSET = 7.5F;
   private static final float BIND_TEXT_SIZE = 15.0F;
   private static final float NAME_INSET = 21.0F;
   private static final float NAME_MID_Y = 30.0F;
   private static final float NAME_SIZE = 19.0F;
   private static final float ROW_NAME_SIZE = 17.5F;
   private static final float ROW_DESC_SIZE = 16.0F;
   private static final float ROW_NAME_MID_Y = 18.5F;
   private static final float ROW_DESC_MID_Y = 44.5F;
   private static final float ROW_DESC_CLIP_MARGIN = 54.0F;
   private static final float ROW_HOVER_INSET = 7.0F;
   private static final float ROW_HOVER_RADIUS = 10.0F;
   private static final float ROW_HOVER_ALPHA = 0.035F;
   private static final float BOOL_CHECKBOX_RIGHT = 44.0F;
   private static final float VALUE_PILL_TOP = 33.5F;
   private static final float VALUE_PILL_HEIGHT = 22.0F;
   private static final float VALUE_PILL_CORNER = 7.0F;
   private static final float VALUE_PILL_MIN_WIDTH = 26.0F;
   private static final float VALUE_PILL_PADDING = 18.0F;
   private static final float VALUE_TEXT_SIZE = 15.0F;
   private static final float SLIDER_TOP = 68.5F;
   private static final float SLIDER_THICKNESS = 4.0F;
   private static final float SLIDER_KNOB_WIDTH = 9.0F;
   private static final float SLIDER_KNOB_HEIGHT = 15.0F;
   private static final float SLIDER_KNOB_ACTIVE_WIDTH = 11.0F;
   private static final float SLIDER_GRAB_PADDING = 8.0F;
   private static final float SLIDER_GRAB_HEIGHT = 28.0F;
   private static final float MODE_NAME_MID_Y = 19.0F;
   private static final float MODE_BOX_TOP = 46.0F;
   private static final float MODE_BOX_HEIGHT = 32.0F;
   private static final float MODE_BOX_CORNER = 9.0F;
   private static final float MODE_BOX_TEXT_SIZE = 15.5F;
   private static final float MODE_BOX_TEXT_INSET = 14.0F;
   private static final float MODE_BOX_CARET_SIZE = 14.0F;
   private static final float MODE_BOX_CARET_INSET = 18.0F;
   private static final float MODE_OPTION_SIZE = 15.0F;
   private static final float MODE_OPTION_GAP = 6.0F;
   private static final float MODE_OPTION_LIFT = 6.0F;
   private static final float ACTION_BUTTON_WIDTH = 76.0F;
   private static final float ACTION_BUTTON_HEIGHT = 30.0F;
   private static final float ACTION_BUTTON_ALPHA_IDLE = 0.3F;
   private static final float ACTION_BUTTON_ALPHA_HOVER = 0.55F;
   private static final float DOCK_HOVER_GROW = 0.14F;
   private static final float DOCK_ACTIVE_GROW = 0.06F;
   private static final float DOCK_ICON_LIFT = 1.5F;
   private static final float DOCK_ICON_SIZE = 24.0F;
   private static final float DOCK_ICON_SLOT_HALF = 24.0F;
   private static final float DOCK_CHEVRON_GROW = 0.1F;
   private static final float DOCK_CHEVRON_SIZE = 22.0F;
   private static final float DOCK_SEPARATOR_HEIGHT = 22.0F;
   private static final float DOCK_SEPARATOR_OFFSET = 61.0F;
   private static final float DOCK_DIVIDER_OFFSET = 388.0F;
   private static final float DOCK_CHEVRON_SLOT = 394.0F;
   private static final float DOCK_CHEVRON_OFFSET = 418.0F;
   private static final float DOCK_SPINNER_OFFSET = 462.0F;
   private static final float DOCK_AVATAR_OFFSET = 496.0F;
   private static final float DOCK_AVATAR_SIZE = 28.0F;
   private static final float DOCK_AVATAR_RADIUS = 14.0F;
   private static final float DOCK_AVATAR_LETTER = 0.46F;
    private static final float DOCK_BOTTOM_MARGIN = 19.0F;
    /** The category dock floats above the panel, outside its rounded frame. */
    private static final float DOCK_GAP = 14.0F;
   private static final float DOCK_HOVER_SLOT = 48.0F;
   private static final float PANEL_CORNER = 18.0F;
   private static final float CARD_CORNER = 14.0F;
   private static final float BACKDROP_ALPHA = 0.38F;
   private static final float COLLAPSE_DIVIDER_FADE = 3.0F;
   private static final int COLLAPSE_DIVIDER_COLOR = 0x14FFFFFF;
   private static final int PANEL_OUTLINE = 0x16FFFFFF;
   private static final int DOCK_OUTLINE = 0x1AFFFFFF;
   private static final int DOCK_SEPARATOR = 0x22FFFFFF;
   private static final int MODE_BOX_HOVER = 0xFF27272C;
   private static final int MODE_BOX_IDLE = 0xFF202024;
   private static final int MODE_OPTION_HOVER = 0x14FFFFFF;
   private static final int MODE_BOX_TEXT = 0xFFE2E2E5;
   private static final int MODE_BOX_CARET = 0xFF9A9AA0;
   private static final int MODE_OPTION_TEXT = 0xFFB8B8BE;
   private static final int ACTION_BUTTON_TEXT = 0xFFDDE4FF;
   private static final int VALUE_TEXT_COLOR = 0xFFB4B4BA;
   private static final int BIND_TEXT_COLOR = 0xFFD2D2D6;
   private static final int LOGO_COLOR = 0xFFE8E8EC;
   private static final int DOCK_ICON_HOVER = 0xFFBDBDC4;
   private static final int AVATAR_PLACEHOLDER = 0xFF26263A;
   private static final int WHITE = 0xFFFFFFFF;
   private static final float PICKER_WIDTH = 132.0F;
   private static final float PICKER_HEIGHT = 96.0F;
   private static final float PICKER_BAR_WIDTH = 12.0F;
   private static final float PICKER_BAR_GAP = 10.0F;
   private static final float PICKER_PADDING = 12.0F;
   private static final float PICKER_GAP = 6.0F;
   private static final float PICKER_HANDLE_SIZE = 9.0F;
   private static final int PICKER_GRID_COLUMNS = 24;
   private static final int PICKER_GRID_ROWS = 18;
   private static final int PICKER_GRADIENT_STEPS = 30;
   private static final int MAX_TEXT_CODEPOINTS = 64;
   private static final long FRAME_NANOS = 1_000_000_000L;
   private static final long OPEN_GUARD_MILLIS = 150L;
    private static final int CLICKGUI_KEY = 344;
    private static final float SEARCH_WIDTH = 440.0F;
    private static final float SEARCH_HEIGHT = 50.0F;
     private static final float SEARCH_GAP = 16.0F;
    private static final float SEARCH_HINT_SIZE = 18.0F;

   // Подсказка при наведении: небольшая плашка с текстом «что делает функция».
   private static final int HINT_PLATE_COLOR = 0xF5141418;
   private static final float HINT_MAX_WIDTH = 300.0F;
   private static final float HINT_TITLE_SIZE = 17.5F;
   private static final float HINT_BODY_SIZE = 15.5F;
   private static final float HINT_LINE_HEIGHT = 19.0F;
   private static final float HINT_TITLE_GAP = 5.0F;
   private static final float HINT_PADDING = 11.0F;
   private static final float HINT_CORNER = 10.0F;
   private static final float HINT_ACCENT_WIDTH = 3.0F;
   private static final float HINT_ACCENT_GAP = 9.0F;
   private static final float HINT_CURSOR_OFFSET_X = 20.0F;
   private static final float HINT_CURSOR_OFFSET_Y = 22.0F;
   private static final float HINT_SCREEN_MARGIN = 10.0F;

   private final List<Hit> hits = new ArrayList<>();
   private final List<ClientModule> visibleModules = new ArrayList<>();
   private final List<ThirdGuiCategory> dockCategories = new ArrayList<>();
   private final IdentityHashMap<ClientModule, ModuleView> moduleViews = new IdentityHashMap<>();
   private final IdentityHashMap<ValueSetting<?>, SettingView> settingViews = new IdentityHashMap<>();
   private final Deque<float[]> transformStack = new ArrayDeque<>();
   private final Matrix4f pixelMatrix = new Matrix4f();
   private final Matrix4f ndcMatrix = new Matrix4f();
   private final Matrix4f ndcForward = new Matrix4f();
   private final Matrix4f ndcInverse = new Matrix4f();
   private final ThirdGuiAnim[] categoryActive = new ThirdGuiAnim[ThirdGuiCategory.values().length];
   private final ThirdGuiAnim[] categoryHover = new ThirdGuiAnim[ThirdGuiCategory.values().length];
   private final ThirdGuiAnim chevronHover = new ThirdGuiAnim(0.0F, 16.0F);
    private final ThirdGuiAnim openAnimation = new ThirdGuiAnim(0.0F, 12.0F);
    private final ThirdGuiAnim searchAnimation = new ThirdGuiAnim(0.0F, 16.0F);
    private boolean searchOpen;
    private boolean searchFocused;
    private String searchQuery = "";

   private ThirdGuiCategory category = ThirdGuiCategory.COMBAT;
   private boolean allCollapsed;
   private boolean initialized;
   private boolean closing;
   private float clock;
   private float time;
   private float deltaSeconds;
   private float scroll;
   private float scrollTarget;
   private float maxScroll;
   private float mouseX;
   private float mouseY;
   private float scale = 1.0F;
   private float panelX;
   private float panelY;

   private ClientModule bindingModule;
   private KeybindSetting bindingSetting;
   private FloatSetting draggingSlider;
   private RangeSetting draggingRange;
   private int draggingRangeHandle;
   private ColorSetting openColorPicker;
   private ColorSetting draggingColor;
   private int draggingColorZone = PICKER_NONE;
   private TextSetting editingText;
   private String textBuffer = "";
   private float dragOrigin;
   private float dragWidth;

   private float transformScale = 1.0F;
   private float transformX;
   private float transformY;
   private float alpha = GLOBAL_ALPHA_SCALE;

   private long lastFrameNanos;
   private long lastTimeNanos = System.nanoTime();
   private long openedAtMillis;
   private final ThirdGuiAnim hoverHintAlpha = new ThirdGuiAnim(0.0F, 15.0F);
   private String hoverHintTitle;
   private String hoverHintBody;

   private ThirdGuiScreen() {
       super(Component.literal("Rose Click Gui"));
      for (int index = 0; index < this.categoryActive.length; index++) {
         this.categoryActive[index] = new ThirdGuiAnim(ThirdGuiCategory.values()[index] == this.category ? 1.0F : 0.0F, 14.0F);
         this.categoryHover[index] = new ThirdGuiAnim(0.0F, 16.0F);
      }
   }

   public static ThirdGuiScreen instance() {
      return ThirdGuiScreen.Holder.INSTANCE;
   }

   // ------------------------------------------------------------------------------------------
   // Screen plumbing
   // ------------------------------------------------------------------------------------------

   @Override
   protected void init() {
      // Screen.init is also called on resize; do not restart opening/closing or the stagger.
      if (this.initialized) {
         return;
      }
      this.initialized = true;
      this.closing = false;
      this.lastFrameNanos = 0L;
      this.openedAtMillis = System.currentTimeMillis();
      this.openAnimation.set(1.0F);
      this.clock = 0.0F;
      this.commitText();
      this.bindingModule = null;
      this.bindingSetting = null;
      this.draggingSlider = null;
      this.draggingRange = null;
      this.closePicker();
   }

   @Override
   protected void extractBlurredBackground(GuiGraphicsExtractor context) {
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor context, float partialTick) {
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, int width, int height) {
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public void onClose() {
      if (this.closing) {
         return;
      }

      this.closing = true;
       this.searchFocused = false;
      this.openAnimation.set(0.0F);
      this.commitText();
      this.closePicker();
      this.bindingModule = null;
      this.bindingSetting = null;
      this.draggingSlider = null;
      this.draggingRange = null;
      this.hits.clear();
      this.saveConfig();
   }

   @Override
   public void removed() {
       this.searchFocused = false;
      this.commitText();
      this.closePicker();
      this.bindingModule = null;
      this.bindingSetting = null;
      this.draggingSlider = null;
      this.draggingRange = null;
      this.initialized = false;
      this.openAnimation.snap(0.0F);
      super.removed();
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor context, int rawMouseX, int rawMouseY, float tickDelta) {
       if (MINECRAFT_CLIENT.level == null) {
          Screen parent = RoseClient.getInstance().getSelfcodeService().returnScreen();
          if (parent != null && parent != this) parent.extractRenderState(context, rawMouseX, rawMouseY, tickDelta);
       }
      if (!this.initialized) {
         this.init();
      }

      long now = System.nanoTime();
      this.deltaSeconds = this.lastFrameNanos == 0L
         ? 0.016F
         : Math.clamp((float)(now - this.lastFrameNanos) / FRAME_NANOS, 0.0F, FRAME_CAP);
      // While shown use the original capped dt; while hidden catch up the spinner phase.
      this.time += this.lastFrameNanos == 0L
         ? Math.max(0.0F, (float)(now - this.lastTimeNanos) / FRAME_NANOS) : this.deltaSeconds;
      this.lastTimeNanos = now;
      this.lastFrameNanos = now;
       this.openAnimation.update(this.deltaSeconds);
       this.searchAnimation.set(this.searchOpen ? 1.0F : 0.0F).update(this.deltaSeconds);
      if (this.openAnimation.value <= VISIBLE_EPSILON) {
         if (this.closing) {
             Minecraft.getInstance().gui.setScreen(RoseClient.getInstance().getSelfcodeService().returnScreen());
         }
         return;
      }
      this.clock += this.deltaSeconds;

      com.mojang.blaze3d.platform.Window window = MINECRAFT_CLIENT.getWindow();
      if (window.isIconified() || window.getScreenWidth() <= 0 || window.getScreenHeight() <= 0) {
         return;
      }

      int framebufferWidth = window.getScreenWidth();
      int framebufferHeight = window.getScreenHeight();
       this.scale = Math.max(MIN_SCALE, Math.min(framebufferWidth / REFERENCE_WIDTH, framebufferHeight / REFERENCE_HEIGHT)
          * BASE_SCALE * (window.getGuiScale() / REFERENCE_SCALE));
       this.scale = Math.min(this.scale, Math.min(framebufferWidth / (PANEL_WIDTH + 60.0F),
          framebufferHeight / (PANEL_HEIGHT + 150.0F + DOCK_HEIGHT + DOCK_GAP)));
      float canvasWidth = framebufferWidth / this.scale;
      float canvasHeight = framebufferHeight / this.scale;
      this.panelX = (canvasWidth - PANEL_WIDTH) / 2.0F;
      this.panelY = (canvasHeight - PANEL_HEIGHT) / 2.0F + (DOCK_HEIGHT + DOCK_GAP) / 2.0F;

      Vector2f framebufferMouse = WorldProjection.guiToScreen(rawMouseX, rawMouseY);
      this.mouseX = framebufferMouse.x / this.scale;
      this.mouseY = framebufferMouse.y / this.scale;

      this.updateDrag();
      this.scroll += (this.scrollTarget - this.scroll) * (1.0F - (float)Math.exp(-SCROLL_SMOOTHING * this.deltaSeconds));
      if (Math.abs(this.scrollTarget - this.scroll) < SCROLL_SETTLE) {
         this.scroll = this.scrollTarget;
      }

      ThemeWave.INSTANCE.beginFrame();
      this.beginFrame(framebufferWidth, framebufferHeight);

      float openness = this.openAnimation.value;
      this.setTransformScale(this.scale, 0.0F, 0.0F);
      this.setAlpha(GLOBAL_ALPHA_SCALE);
      UiDrawing.drawRoundedRect(0.0F, 0.0F, canvasWidth, canvasHeight, 0.0F,
         ThirdGuiColors.alpha(0xFF000000, BACKDROP_ALPHA * openness));

      this.push();
      this.scaleAbout(canvasWidth / 2.0F, canvasHeight / 2.0F,
         ENTRANCE_SCALE_BASE + ENTRANCE_SCALE_GAIN * ThirdGuiAnim.outBack(openness));
      this.translate(0.0F, ENTRANCE_SHIFT * (1.0F - ThirdGuiAnim.outCubic(openness)));
      this.multiplyAlpha(ThirdGuiAnim.clamp01(openness * ENTRANCE_FADE_GAIN));

      this.drawSoftShadow(this.panelX, this.panelY + 14.0F, PANEL_WIDTH, PANEL_HEIGHT, 20.0F, 38.0F, 0xAA000000);
      UiDrawing.drawRoundedRect(this.panelX, this.panelY, PANEL_WIDTH, PANEL_HEIGHT, PANEL_CORNER, ThirdGuiColors.panel());
      UiDrawing.drawOutline(this.panelX, this.panelY, PANEL_WIDTH, PANEL_HEIGHT, 1.0F, PANEL_CORNER, PANEL_OUTLINE);

      this.hits.clear();
      this.refreshDockCategories();
        UiDrawing.pushScissor(this.panelX, this.panelY, PANEL_WIDTH, PANEL_HEIGHT);
       this.layout();
       UiDrawing.popScissor();
       this.drawDock();
        this.drawSearchFooter();
      this.pop();

      this.updateHoverHint();
      if (this.openColorPicker == null) {
         this.drawHoverHint(canvasWidth, canvasHeight);
      }

      if (this.openColorPicker != null) {
         this.drawColorPicker();
      }

      ThemeWave.INSTANCE.renderTransition();
      UiDrawing.resetGlobalAlpha();
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
   }

   // ------------------------------------------------------------------------------------------
   // Layout
   // ------------------------------------------------------------------------------------------

    private void layout() {
       this.visibleModules.clear();
       String query = this.searchQuery.strip().toLowerCase(Locale.ROOT);
       for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
          if (query.isEmpty() ? module.getCategory() == this.category.category()
             : module.getName().toLowerCase(Locale.ROOT).contains(query)
                || ClientLocalization.translate(module.getName()).toLowerCase(Locale.ROOT).contains(query)) {
             this.visibleModules.add(module);
          }
       }
       if (!query.isEmpty() && this.visibleModules.isEmpty()) {
          ThirdGuiScreen.textCenter(ThirdGuiFonts.regular(), "Ничего не найдено",
             this.panelX + PANEL_WIDTH / 2.0F, this.panelY + 70.0F, 20.0F, ThirdGuiColors.DESC);
       }

      float[] columns = {PANEL_PADDING, PANEL_PADDING, PANEL_PADDING};
      int index = 0;
      for (ClientModule module : this.visibleModules) {
         ModuleView view = this.view(module);
         view.collapse.set(view.collapsed ? 0.0F : 1.0F);
         view.collapse.update(this.deltaSeconds);
         view.enabled.set(module.isEnabled() ? 1.0F : 0.0F);
         view.enabled.update(this.deltaSeconds);
         float height = this.cardHeight(module, view);
         int column = 0;
         if (columns[1] < columns[column]) {
            column = 1;
         }

         if (columns[2] < columns[column]) {
            column = 2;
         }

         float x = this.panelX + PANEL_PADDING + column * (CARD_WIDTH + COLUMN_GAP);
         float y = this.panelY + columns[column] - this.scroll;
         if (y + height > this.panelY - CARD_EDGE_MARGIN && y < this.panelY + PANEL_HEIGHT + CARD_EDGE_MARGIN) {
            this.drawCard(module, view, x, y, height, index);
         }

         columns[column] += height + COLUMN_GAP;
         index++;
      }

      float total = Math.max(columns[0], Math.max(columns[1], columns[2])) - COLUMN_GAP + PANEL_PADDING;
        this.maxScroll = Math.max(0.0F, total - PANEL_HEIGHT);
      this.scrollTarget = Math.min(this.scrollTarget, this.maxScroll);
   }

   /**
    * Empty module categories are omitted; client editors are always available.
    */
   private void refreshDockCategories() {
      this.dockCategories.clear();
      for (ThirdGuiCategory candidate : ThirdGuiCategory.values()) {
         if (candidate.isEditor()) {
            this.dockCategories.add(candidate);
            continue;
         }
         for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
            if (module.getCategory() == candidate.category()) {
               this.dockCategories.add(candidate);
               break;
            }
         }
      }

      if (this.dockCategories.isEmpty()) {
         this.dockCategories.addAll(List.of(ThirdGuiCategory.values()));
      }

      if (!this.dockCategories.contains(this.category)) {
         this.category = this.dockCategories.getFirst();
      }
   }

   private float rowHeight(ValueSetting<?> setting) {
      if (setting instanceof FloatSetting || setting instanceof RangeSetting || setting instanceof ColorSetting) {
         return ROW_SLIDER;
      }

      if (setting instanceof SingleChoiceSetting choice) {
         return ROW_MODE + (choice.getValue().size() * OPTION_HEIGHT + MODE_OPTION_GAP) * this.view(setting).open.value;
      }

      if (setting instanceof MultiChoiceSetting choice) {
         return ROW_MODE + (choice.getValue().size() * OPTION_HEIGHT + MODE_OPTION_GAP) * this.view(setting).open.value;
      }

      if (setting instanceof SectionHeaderSetting) {
         return ROW_SECTION;
      }

      if (setting instanceof ActionSetting || setting instanceof KeybindSetting || setting instanceof TextSetting) {
         return ROW_ACTION;
      }

      return ROW_BOOL;
   }

   private float cardHeight(ClientModule module, ModuleView view) {
      List<ValueSetting<?>> settings = this.visibleSettings(module);
      if (settings.isEmpty()) {
         return CARD_HEADER;
      }

      float body = BODY_TOP + BODY_BOTTOM;
      for (ValueSetting<?> setting : settings) {
         body += this.rowHeight(setting);
      }

      return CARD_HEADER + body * view.collapse.value;
   }

   private List<ValueSetting<?>> visibleSettings(ClientModule module) {
      List<ValueSetting<?>> settings = new ArrayList<>();
      List<ValueSetting<?>> all = module.getSettingsList();
      boolean hidden = all.stream().filter(ActionSetting.class::isInstance).map(ActionSetting.class::cast).anyMatch(ActionSetting::hidesOtherSettings);
      for (ValueSetting<?> setting : all) {
         if (hidden && !(setting instanceof ActionSetting)) {
            continue;
         }

         if (setting.getVisibilityCondition().getAsBoolean()) {
            settings.add(setting);
         }
      }

      return settings;
   }

   // ------------------------------------------------------------------------------------------
   // Card rendering
   // ------------------------------------------------------------------------------------------

   private void drawCard(ClientModule module, ModuleView view, float x, float y, float height, int index) {
      float reveal = ThirdGuiAnim.outCubic((this.clock - index * ENTRANCE_STAGGER) / ENTRANCE_DURATION);
      if (reveal <= 0.001F) {
         return;
      }

      FontRenderer semibold = ThirdGuiFonts.semibold();
      this.push();
      float shift = (1.0F - reveal) * CARD_ENTRANCE_SHIFT;
      this.translate(0.0F, shift);
      this.multiplyAlpha(reveal);

      view.hover.set(this.inside(this.mouseX, this.mouseY, x, y, CARD_WIDTH, height) && this.overPanel() ? 1.0F : 0.0F);
      view.hover.update(this.deltaSeconds);
      UiDrawing.drawRoundedRect(x, y, CARD_WIDTH, height, CARD_CORNER,
         ThirdGuiColors.lerp(ThirdGuiColors.card(), ThirdGuiColors.lerp(ThirdGuiColors.card(), WHITE, 0.025F), view.hover.value));
      UiDrawing.drawOutline(x, y, CARD_WIDTH, height, 1.0F, CARD_CORNER, ThirdGuiColors.border());

      List<ValueSetting<?>> settings = this.visibleSettings(module);
      boolean live = !this.closing && reveal > CARD_REVEAL_THRESHOLD;
      if (live) {
         // Зарегистрирована раньше остальных: подсказка работает по всей карточке,
         // а клики по переключателю, бинду и строкам всё равно находят свои области.
         this.hit(x, y, CARD_WIDTH, height, HIT_HINT_ONLY, module, null, 0, true);
      }
      if (live && !settings.isEmpty()) {
         this.hit(x, y, CARD_WIDTH, CARD_HEADER, HIT_HEADER, module, null, 0, true);
      }

      ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(module.getName()),
         x + NAME_INSET, y + NAME_MID_Y, NAME_SIZE, ThirdGuiColors.text());

      float boxX = x + CARD_WIDTH - CHECKBOX_RIGHT_MARGIN - CHECKBOX_SIZE;
      float boxY = y + CHECKBOX_TOP;
      this.drawCheck(boxX, boxY, CHECKBOX_SIZE, view.enabled.value);
      if (live) {
         this.hit(boxX - CHECKBOX_HIT_PADDING, boxY - CHECKBOX_HIT_PADDING,
            CHECKBOX_SIZE + CHECKBOX_HIT_PADDING * 2.0F, CHECKBOX_SIZE + CHECKBOX_HIT_PADDING * 2.0F,
            HIT_TOGGLE, module, null, 0, true);
      }

      boolean listening = this.bindingModule == module;
      int keyCode = module.getKeybind().getKeyCode();
      String bindText = listening ? "..." : ThirdGuiKeys.name(keyCode);
      float bindTextWidth = ThirdGuiScreen.width(semibold, bindText, BIND_TEXT_SIZE);
      float pillWidth = BIND_PILL_PAD + bindTextWidth + BIND_PILL_ICON_GAP + BIND_PILL_ICON_SIZE + BIND_PILL_PAD;
      float pillX = boxX - BIND_PILL_GAP - pillWidth;
      float pillY = y + BIND_PILL_TOP;
      int pillColor = keyCode >= 0 || listening ? BIND_TEXT_COLOR : ThirdGuiColors.PILL_TEXT;
      UiDrawing.drawRoundedRect(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, 8.0F,
         listening ? ThirdGuiColors.alpha(ThirdGuiColors.accent(), 0.35F) : ThirdGuiColors.PILL);
      ThirdGuiScreen.textMid(semibold, bindText, pillX + BIND_PILL_PAD, pillY + BIND_PILL_HEIGHT / 2.0F, BIND_TEXT_SIZE, pillColor);
      ThirdGuiIcons.draw(ThirdGuiIcons.keyboard(), pillX + pillWidth - BIND_PILL_PAD - BIND_PILL_ICON_INSET,
         pillY + BIND_PILL_HEIGHT / 2.0F, BIND_PILL_ICON_SIZE, pillColor);
      if (live) {
         this.hit(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, HIT_BIND, module, null, 0, true);
      }

      if (!settings.isEmpty()) {
         float collapse = view.collapse.value;
         UiDrawing.drawRoundedRect(x + 1.0F, y + CARD_HEADER - 1.0F, CARD_WIDTH - 2.0F, 1.0F, 0.0F,
            ThirdGuiColors.alpha(COLLAPSE_DIVIDER_COLOR, ThirdGuiAnim.clamp01(collapse * COLLAPSE_DIVIDER_FADE)));
         if (height - CARD_HEADER > 1.0F) {
            float bodyX = x;
            float bodyY = y + CARD_HEADER;
            float bodyWidth = CARD_WIDTH;
            float bodyHeight = height - CARD_HEADER;
            Vector4f previousScissor = new Vector4f(UiDrawing.getScissor());
            boolean wasScissored = UiDrawing.isScissorEnabled();
            // The card entrance shift moves the card relative to the panel clip, so the parent
            // rectangle has to be converted into the card space before intersecting.
            float left = Math.max(previousScissor.x, bodyX);
            float top = Math.max(wasScissored ? previousScissor.y - shift : bodyY, bodyY);
            float right = Math.min(previousScissor.x + previousScissor.z, bodyX + bodyWidth);
            float bottom = Math.min(wasScissored ? previousScissor.y + previousScissor.w - shift : bodyY + bodyHeight, bodyY + bodyHeight);
            UiDrawing.setScissor(left, top, Math.max(0.0F, right - left), Math.max(0.0F, bottom - top));
            float previousAlpha = this.alpha;
            this.multiplyAlpha(collapse);
            float rowY = bodyY + BODY_TOP;
            for (ValueSetting<?> setting : settings) {
               float rowHeight = this.rowHeight(setting);
               this.drawRow(module, setting, x, rowY, rowHeight, live && collapse > ROW_LIVE_THRESHOLD);
               rowY += rowHeight;
            }

            this.setAlpha(previousAlpha);
            if (wasScissored) {
               UiDrawing.setScissor(previousScissor.x, previousScissor.y, previousScissor.z, previousScissor.w);
            } else {
               UiDrawing.disableScissor();
            }
         }
      }

      this.pop();
   }

   private void drawCheck(float x, float y, float size, float progress) {
      float k = 1.0F + CHECK_POP_SCALE * (float)Math.sin(progress * Math.PI);
      float scaled = size * k;
      float offset = (size - scaled) / 2.0F;
      UiDrawing.drawRoundedRect(x + offset, y + offset, scaled, scaled, CHECKBOX_CORNER,
         ThirdGuiColors.lerp(ThirdGuiColors.checkOff(), ThirdGuiColors.accent(), progress));
      if (progress < 0.999F) {
         UiDrawing.drawOutline(x + offset, y + offset, scaled, scaled, 1.0F, CHECKBOX_CORNER,
            ThirdGuiColors.alpha(0x18FFFFFF, 1.0F - progress));
      }

      if (progress > 0.02F) {
         float previousAlpha = this.alpha;
         this.multiplyAlpha(ThirdGuiAnim.clamp01(progress * CHECK_ICON_FADE_GAIN));
         ThirdGuiIcons.draw(ThirdGuiIcons.check(), x + size / 2.0F, y + size / 2.0F,
            CHECK_ICON_SIZE * ThirdGuiAnim.outBack(progress), ThirdGuiColors.WHITE);
         this.setAlpha(previousAlpha);
      }
   }

   // ------------------------------------------------------------------------------------------
   // Row rendering
   // ------------------------------------------------------------------------------------------

   private void drawRow(ClientModule module, ValueSetting<?> setting, float x, float y, float rowHeight, boolean live) {
      SettingView view = this.view(setting);
      view.hover.set(this.overPanel() && this.inside(this.mouseX, this.mouseY, x, y, CARD_WIDTH, rowHeight) ? 1.0F : 0.0F);
      view.hover.update(this.deltaSeconds);
      if (view.hover.value > 0.01F) {
         UiDrawing.drawRoundedRect(x + ROW_HOVER_INSET, y + 2.0F, CARD_WIDTH - ROW_HOVER_INSET * 2.0F,
            rowHeight - 4.0F, ROW_HOVER_RADIUS, ThirdGuiColors.alpha(WHITE, ROW_HOVER_ALPHA * view.hover.value));
      }

      float clip = x + CARD_WIDTH - ROW_DESC_CLIP_MARGIN;
      FontRenderer semibold = ThirdGuiFonts.semibold();
      FontRenderer regular = ThirdGuiFonts.regular();
      String description = this.descriptionOf(module);

      if (setting instanceof SectionHeaderSetting) {
         UiDrawing.drawRoundedRect(x + 1.0F, y + rowHeight / 2.0F - 0.5F, CARD_WIDTH - 2.0F, 1.0F, 0.0F, COLLAPSE_DIVIDER_COLOR);
         ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(setting.getName()),
            x + NAME_INSET, y + rowHeight / 2.0F - 8.0F, 14.0F, ThirdGuiColors.PILL_TEXT);
      } else if (setting instanceof BooleanSetting booleanSetting) {
         view.toggle.set(booleanSetting.getValue() ? 1.0F : 0.0F);
         view.toggle.update(this.deltaSeconds);
         ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(booleanSetting.getName()),
            x + NAME_INSET, y + ROW_NAME_MID_Y, ROW_NAME_SIZE, ThirdGuiColors.text(), clip);
         ThirdGuiScreen.textMid(regular, description, x + NAME_INSET, y + ROW_DESC_MID_Y, ROW_DESC_SIZE, ThirdGuiColors.description(), clip);
         this.drawCheck(x + CARD_WIDTH - BOOL_CHECKBOX_RIGHT, y + ROW_BOOL / 2.0F - CHECKBOX_SIZE / 2.0F, CHECKBOX_SIZE, view.toggle.value);
         if (live) {
            this.hit(x, y, CARD_WIDTH, rowHeight, HIT_BOOL, module, setting, 0, true);
         }
      } else if (setting instanceof FloatSetting floatSetting) {
         this.drawFloatRow(module, floatSetting, view, x, y, description, live);
      } else if (setting instanceof RangeSetting rangeSetting) {
         this.drawRangeRow(module, rangeSetting, x, y, description, live);
      } else if (setting instanceof ColorSetting colorSetting) {
         this.drawColorRow(module, colorSetting, x, y, description, clip, live);
      } else if (setting instanceof SingleChoiceSetting choice) {
         this.drawChoiceRow(module, choice.getValue(), view, setting, x, y, live, false);
      } else if (setting instanceof MultiChoiceSetting choice) {
         this.drawChoiceRow(module, choice.getValue(), view, setting, x, y, live, true);
      } else if (setting instanceof ActionSetting actionSetting) {
         String label = ClientLocalization.translate(actionSetting.getName());
         ThirdGuiScreen.textMid(semibold, label, x + NAME_INSET, y + ROW_ACTION / 2.0F, ROW_NAME_SIZE, ThirdGuiColors.text(), clip - 40.0F);
         float buttonX = x + CARD_WIDTH - NAME_INSET - ACTION_BUTTON_WIDTH;
         float buttonY = y + ROW_ACTION / 2.0F - ACTION_BUTTON_HEIGHT / 2.0F;
         boolean buttonHover = this.overPanel()
            && this.inside(this.mouseX, this.mouseY, buttonX, buttonY, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
         UiDrawing.drawRoundedRect(buttonX, buttonY, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT, MODE_BOX_CORNER,
            ThirdGuiColors.alpha(ThirdGuiColors.accent(), buttonHover ? ACTION_BUTTON_ALPHA_HOVER : ACTION_BUTTON_ALPHA_IDLE));
         ThirdGuiScreen.textCenter(semibold, label, buttonX + ACTION_BUTTON_WIDTH / 2.0F,
            buttonY + ACTION_BUTTON_HEIGHT / 2.0F, VALUE_TEXT_SIZE, ACTION_BUTTON_TEXT, ACTION_BUTTON_WIDTH - 8.0F);
         if (live) {
            this.hit(buttonX, buttonY, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT, HIT_ACTION, module, setting, 0, true);
         }
      } else if (setting instanceof KeybindSetting keybindSetting) {
         ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(keybindSetting.getName()),
            x + NAME_INSET, y + ROW_ACTION / 2.0F, ROW_NAME_SIZE, ThirdGuiColors.text(), clip - 40.0F);
         boolean listening = this.bindingSetting == keybindSetting;
         int code = keybindSetting.getValue();
         String bindText = listening ? "..." : ThirdGuiKeys.name(code);
         float pillWidth = Math.max(VALUE_PILL_MIN_WIDTH, ThirdGuiScreen.width(semibold, bindText, VALUE_TEXT_SIZE) + VALUE_PILL_PADDING);
         float pillX = x + CARD_WIDTH - NAME_INSET - pillWidth;
         float pillY = y + ROW_ACTION / 2.0F - BIND_PILL_HEIGHT / 2.0F;
         UiDrawing.drawRoundedRect(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, VALUE_PILL_CORNER,
            listening ? ThirdGuiColors.alpha(ThirdGuiColors.accent(), 0.35F) : ThirdGuiColors.PILL);
         ThirdGuiScreen.textCenter(semibold, bindText, pillX + pillWidth / 2.0F, pillY + BIND_PILL_HEIGHT / 2.0F,
            VALUE_TEXT_SIZE, code >= 0 || listening ? VALUE_TEXT_COLOR : ThirdGuiColors.PILL_TEXT);
         if (live) {
            this.hit(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, HIT_SETTING_BIND, module, setting, 0, true);
         }
      } else if (setting instanceof TextSetting textSetting) {
         boolean editing = this.editingText == textSetting;
         ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(textSetting.getName()),
            x + NAME_INSET, y + ROW_ACTION / 2.0F, ROW_NAME_SIZE, ThirdGuiColors.text(), clip - 40.0F);
         String value = editing ? this.textBuffer : textSetting.getValue();
         float pillWidth = Math.max(VALUE_PILL_MIN_WIDTH * 3.0F,
            ThirdGuiScreen.width(semibold, value, VALUE_TEXT_SIZE) + VALUE_PILL_PADDING * 1.5F);
         float pillX = x + CARD_WIDTH - NAME_INSET - pillWidth;
         float pillY = y + ROW_ACTION / 2.0F - BIND_PILL_HEIGHT / 2.0F;
         UiDrawing.drawRoundedRect(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, VALUE_PILL_CORNER, MODE_BOX_IDLE);
         UiDrawing.pushScissor(pillX + 5.0F, pillY, Math.max(0.0F, pillWidth - 10.0F), BIND_PILL_HEIGHT);
         ThirdGuiScreen.textMid(semibold, value.isEmpty() ? "..." : value, pillX + 8.0F,
            pillY + BIND_PILL_HEIGHT / 2.0F, VALUE_TEXT_SIZE, value.isEmpty() ? ThirdGuiColors.PILL_TEXT : MODE_BOX_TEXT);
         if (editing && System.currentTimeMillis() / 500L % 2L == 0L) {
            float caretX = pillX + 8.0F + ThirdGuiScreen.width(semibold, value, VALUE_TEXT_SIZE);
            UiDrawing.drawRoundedRect(caretX, pillY + 5.0F, 1.5F, BIND_PILL_HEIGHT - 10.0F, 0.5F, ThirdGuiColors.accent());
         }

         UiDrawing.popScissor();
         if (live) {
            this.hit(pillX, pillY, pillWidth, BIND_PILL_HEIGHT, HIT_TEXT, module, setting, 0, true);
         }
      }
   }

   private void drawFloatRow(ClientModule module, FloatSetting setting, SettingView view, float x, float y,
                             String description, boolean live) {
      FontRenderer semibold = ThirdGuiFonts.semibold();
      view.slider.set(this.normalize(setting.getValue(), setting.getMinimum(), setting.getMaximum()));
      view.slider.update(this.deltaSeconds);
      String display = ThirdGuiScreen.formatNumber(setting.getValue(), setting.getStep());
      float pillWidth = Math.max(VALUE_PILL_MIN_WIDTH, ThirdGuiScreen.width(semibold, display, VALUE_TEXT_SIZE) + VALUE_PILL_PADDING);
      float pillX = x + CARD_WIDTH - NAME_INSET - pillWidth;
      ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(setting.getName()),
         x + NAME_INSET, y + ROW_NAME_MID_Y, ROW_NAME_SIZE, ThirdGuiColors.text(), pillX - 10.0F);
      ThirdGuiScreen.textMid(ThirdGuiFonts.regular(), description,
         x + NAME_INSET, y + ROW_DESC_MID_Y, ROW_DESC_SIZE, ThirdGuiColors.description(), pillX - 10.0F);
      UiDrawing.drawRoundedRect(pillX, y + VALUE_PILL_TOP, pillWidth, VALUE_PILL_HEIGHT, VALUE_PILL_CORNER, ThirdGuiColors.PILL);
      ThirdGuiScreen.textCenter(semibold, display, pillX + pillWidth / 2.0F,
         y + VALUE_PILL_TOP + VALUE_PILL_HEIGHT / 2.0F, VALUE_TEXT_SIZE, VALUE_TEXT_COLOR);

      float trackStart = x + NAME_INSET;
      float trackWidth = CARD_WIDTH - NAME_INSET * 2.0F;
      float trackY = y + SLIDER_TOP;
      float fill = trackWidth * ThirdGuiAnim.clamp01(view.slider.value);
      UiDrawing.drawRoundedRect(trackStart, trackY - SLIDER_THICKNESS / 2.0F, trackWidth, SLIDER_THICKNESS,
         SLIDER_THICKNESS / 2.0F, ThirdGuiColors.TRACK);
      if (fill > 0.5F) {
         UiDrawing.drawRoundedRect(trackStart, trackY - SLIDER_THICKNESS / 2.0F, fill, SLIDER_THICKNESS,
            SLIDER_THICKNESS / 2.0F, ThirdGuiColors.sliderFill());
      }

      float knobWidth = this.draggingSlider == setting ? SLIDER_KNOB_ACTIVE_WIDTH : SLIDER_KNOB_WIDTH;
      UiDrawing.drawRoundedRect(trackStart + fill - knobWidth / 2.0F, trackY - SLIDER_KNOB_HEIGHT / 2.0F,
         knobWidth, SLIDER_KNOB_HEIGHT, knobWidth / 2.0F, ThirdGuiColors.WHITE);
      if (live) {
         this.hit(trackStart - SLIDER_GRAB_PADDING, trackY - SLIDER_GRAB_HEIGHT / 2.0F,
            trackWidth + SLIDER_GRAB_PADDING * 2.0F, SLIDER_GRAB_HEIGHT, HIT_SLIDER, module, setting, 0, true);
      }
   }

   private void drawRangeRow(ClientModule module, RangeSetting setting, float x, float y, String description, boolean live) {
      FontRenderer semibold = ThirdGuiFonts.semibold();
      Vector2f value = setting.getValue();
      float minimum = setting.getBounds().x;
      float maximum = setting.getBounds().y;
      String display = ThirdGuiScreen.formatNumber(value.x, setting.getStep()) + " - " + ThirdGuiScreen.formatNumber(value.y, setting.getStep());
      float pillWidth = Math.max(VALUE_PILL_MIN_WIDTH * 2.0F, ThirdGuiScreen.width(semibold, display, VALUE_TEXT_SIZE) + VALUE_PILL_PADDING);
      float pillX = x + CARD_WIDTH - NAME_INSET - pillWidth;
      ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(setting.getName()),
         x + NAME_INSET, y + ROW_NAME_MID_Y, ROW_NAME_SIZE, ThirdGuiColors.text(), pillX - 10.0F);
      ThirdGuiScreen.textMid(ThirdGuiFonts.regular(), description,
         x + NAME_INSET, y + ROW_DESC_MID_Y, ROW_DESC_SIZE, ThirdGuiColors.description(), pillX - 10.0F);
      UiDrawing.drawRoundedRect(pillX, y + VALUE_PILL_TOP, pillWidth, VALUE_PILL_HEIGHT, VALUE_PILL_CORNER, ThirdGuiColors.PILL);
      ThirdGuiScreen.textCenter(semibold, display, pillX + pillWidth / 2.0F,
         y + VALUE_PILL_TOP + VALUE_PILL_HEIGHT / 2.0F, VALUE_TEXT_SIZE, VALUE_TEXT_COLOR);

      float trackStart = x + NAME_INSET;
      float trackWidth = CARD_WIDTH - NAME_INSET * 2.0F;
      float trackY = y + SLIDER_TOP;
      float low = this.normalize(value.x, minimum, maximum);
      float high = this.normalize(value.y, minimum, maximum);
      UiDrawing.drawRoundedRect(trackStart, trackY - SLIDER_THICKNESS / 2.0F, trackWidth, SLIDER_THICKNESS,
         SLIDER_THICKNESS / 2.0F, ThirdGuiColors.TRACK);
      UiDrawing.drawRoundedRect(trackStart + low * trackWidth, trackY - SLIDER_THICKNESS / 2.0F,
         Math.max(0.0F, (high - low) * trackWidth), SLIDER_THICKNESS, SLIDER_THICKNESS / 2.0F, ThirdGuiColors.sliderFill());
      boolean dragging = this.draggingRange == setting;
      float lowKnob = dragging && this.draggingRangeHandle == RANGE_HANDLE_LOW ? SLIDER_KNOB_ACTIVE_WIDTH : SLIDER_KNOB_WIDTH;
      float highKnob = dragging && this.draggingRangeHandle == RANGE_HANDLE_HIGH ? SLIDER_KNOB_ACTIVE_WIDTH : SLIDER_KNOB_WIDTH;
      UiDrawing.drawRoundedRect(trackStart + low * trackWidth - lowKnob / 2.0F, trackY - SLIDER_KNOB_HEIGHT / 2.0F,
         lowKnob, SLIDER_KNOB_HEIGHT, lowKnob / 2.0F, ThirdGuiColors.WHITE);
      UiDrawing.drawRoundedRect(trackStart + high * trackWidth - highKnob / 2.0F, trackY - SLIDER_KNOB_HEIGHT / 2.0F,
         highKnob, SLIDER_KNOB_HEIGHT, highKnob / 2.0F, ThirdGuiColors.WHITE);
      if (live) {
         this.hit(trackStart - SLIDER_GRAB_PADDING, trackY - SLIDER_GRAB_HEIGHT / 2.0F,
            trackWidth + SLIDER_GRAB_PADDING * 2.0F, SLIDER_GRAB_HEIGHT, HIT_RANGE, module, setting, 0, true);
      }
   }

   private void drawColorRow(ClientModule module, ColorSetting setting, float x, float y, String description,
                             float clip, boolean live) {
      FontRenderer semibold = ThirdGuiFonts.semibold();
      int value = setting.getValue();
      ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(setting.getName()),
         x + NAME_INSET, y + ROW_NAME_MID_Y, ROW_NAME_SIZE, ThirdGuiColors.text(), clip - 40.0F);
      ThirdGuiScreen.textMid(ThirdGuiFonts.regular(), description,
         x + NAME_INSET, y + ROW_DESC_MID_Y, ROW_DESC_SIZE, ThirdGuiColors.description(), clip - 40.0F);
      float swatchX = x + CARD_WIDTH - NAME_INSET - VALUE_PILL_HEIGHT;
      float swatchY = y + VALUE_PILL_TOP;
      boolean open = this.openColorPicker == setting;
      UiDrawing.drawRoundedRect(swatchX, swatchY, VALUE_PILL_HEIGHT, VALUE_PILL_HEIGHT, VALUE_PILL_CORNER,
         open ? ThirdGuiColors.accent() : value & 0xFFFFFF | 0xFF000000);
      UiDrawing.drawOutline(swatchX, swatchY, VALUE_PILL_HEIGHT, VALUE_PILL_HEIGHT, 1.0F, VALUE_PILL_CORNER,
         ThirdGuiColors.border());
      if (live) {
         this.hit(swatchX - 4.0F, swatchY - 4.0F, VALUE_PILL_HEIGHT + 8.0F, VALUE_PILL_HEIGHT + 8.0F,
            HIT_COLOR, module, setting, 0, true);
      }
   }

   private void drawChoiceRow(ClientModule module, List<ChoiceOption> options, SettingView view, ValueSetting<?> setting,
                              float x, float y, boolean live, boolean multi) {
      FontRenderer semibold = ThirdGuiFonts.semibold();
      view.open.set(view.expanded ? 1.0F : 0.0F);
      view.open.update(this.deltaSeconds);
      ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(setting.getName()),
         x + NAME_INSET, y + MODE_NAME_MID_Y, ROW_NAME_SIZE, ThirdGuiColors.text(), x + CARD_WIDTH - NAME_INSET);
      float boxX = x + NAME_INSET;
      float boxY = y + MODE_BOX_TOP;
      float boxWidth = CARD_WIDTH - NAME_INSET * 2.0F;
      boolean boxHover = this.overPanel() && this.inside(this.mouseX, this.mouseY, boxX, boxY, boxWidth, MODE_BOX_HEIGHT);
      UiDrawing.drawRoundedRect(boxX, boxY, boxWidth, MODE_BOX_HEIGHT, MODE_BOX_CORNER, boxHover ? MODE_BOX_HOVER : MODE_BOX_IDLE);
      UiDrawing.drawOutline(boxX, boxY, boxWidth, MODE_BOX_HEIGHT, 1.0F, MODE_BOX_CORNER, 0x14FFFFFF);
      ThirdGuiScreen.textMid(semibold, this.choiceLabel(setting, options, multi), boxX + MODE_BOX_TEXT_INSET,
         boxY + MODE_BOX_HEIGHT / 2.0F, MODE_BOX_TEXT_SIZE, MODE_BOX_TEXT, boxX + boxWidth - MODE_BOX_CARET_INSET);
      ThirdGuiIcons.draw(ThirdGuiIcons.caret(), boxX + boxWidth - MODE_BOX_CARET_INSET,
         boxY + MODE_BOX_HEIGHT / 2.0F, MODE_BOX_CARET_SIZE, MODE_BOX_CARET);
      if (live) {
         this.hit(boxX, boxY, boxWidth, MODE_BOX_HEIGHT, HIT_MODE, module, setting, 0, true);
      }

      float reveal = view.open.value;
      if (reveal > 0.01F) {
         float previousAlpha = this.alpha;
         this.multiplyAlpha(reveal);
         float optionY = boxY + MODE_BOX_HEIGHT + MODE_OPTION_GAP;
         for (int index = 0; index < options.size(); index++) {
            float optionRowY = optionY + index * OPTION_HEIGHT - (1.0F - reveal) * MODE_OPTION_LIFT;
            if (this.overPanel() && this.inside(this.mouseX, this.mouseY, boxX, optionRowY, boxWidth, OPTION_HEIGHT)) {
               UiDrawing.drawRoundedRect(boxX, optionRowY, boxWidth, OPTION_HEIGHT - 2.0F, 8.0F, MODE_OPTION_HOVER);
            }

            boolean selected = multi
               ? options.get(index).isSelected()
               : options.get(index) == ((SingleChoiceSetting)setting).getSelectedOption();
            ThirdGuiScreen.textMid(semibold, ClientLocalization.translate(options.get(index).getName()),
               boxX + MODE_BOX_TEXT_INSET, optionRowY + OPTION_HEIGHT / 2.0F - 1.0F, MODE_OPTION_SIZE,
               selected ? ThirdGuiColors.accent() : MODE_OPTION_TEXT);
            if (live && reveal > OPTION_LIVE_THRESHOLD) {
               this.hit(boxX, optionRowY, boxWidth, OPTION_HEIGHT, HIT_OPTION, module, setting, index, true);
            }
         }

         this.setAlpha(previousAlpha);
      }
   }

   private String choiceLabel(ValueSetting<?> setting, List<ChoiceOption> options, boolean multi) {
      if (options.isEmpty()) {
         return "";
      }

      if (multi) {
         StringBuilder builder = new StringBuilder();
         for (ChoiceOption option : options) {
            if (option.isSelected()) {
               if (!builder.isEmpty()) {
                  builder.append(", ");
               }

               builder.append(option.getName());
            }
         }

         return builder.isEmpty() ? options.getFirst().getName() : builder.toString();
      }

      ChoiceOption selected = ((SingleChoiceSetting)setting).getSelectedOption();
      return selected == null ? options.getFirst().getName() : selected.getName();
   }

   // ------------------------------------------------------------------------------------------
   // Dock
   // ------------------------------------------------------------------------------------------

   /** Hover label sits above the floating dock, or below it when the screen edge is too close. */
   private float dockTooltipY(float dockY) {
      float above = dockY - 36.0F;
      return above < 4.0F ? dockY + DOCK_HEIGHT + 8.0F : above;
   }

   private void drawDock() {
      List<ThirdGuiCategory> categories = this.dockCategories;
      float extraSlots = categories.size() - DOCK_BASE_CATEGORY_COUNT;
      float dockWidth = DOCK_BASE_WIDTH + DOCK_ICON_SLOT * extraSlots;
      float dockX = this.panelX + (PANEL_WIDTH - dockWidth) / 2.0F;
      float dockY = this.panelY - DOCK_GAP - DOCK_HEIGHT;
      float centerY = dockY + DOCK_HEIGHT / 2.0F;

      this.drawSoftShadow(dockX, dockY + 8.0F, dockWidth, DOCK_HEIGHT, 18.0F, 22.0F, 0x99000000);
      UiDrawing.drawRoundedRect(dockX, dockY, dockWidth, DOCK_HEIGHT, 18.0F, ThirdGuiColors.dock());
      UiDrawing.drawOutline(dockX, dockY, dockWidth, DOCK_HEIGHT, 1.0F, 18.0F, DOCK_OUTLINE);

       sg.render.ClientLogo.drawCentered(dockX + 32.0F, centerY, 35.5F, LOGO_COLOR);
      UiDrawing.drawRoundedRect(dockX + DOCK_SEPARATOR_OFFSET, centerY - DOCK_SEPARATOR_HEIGHT / 2.0F, 1.0F,
         DOCK_SEPARATOR_HEIGHT, 0.0F, DOCK_SEPARATOR);

      for (int index = 0; index < categories.size(); index++) {
         ThirdGuiCategory entry = categories.get(index);
         int slot = entry.ordinal();
          float iconX = dockX + DOCK_ICON_ORIGIN + DOCK_ICON_SLOT * index;
          if (entry.isEditor() && (index == 0 || !categories.get(index - 1).isEditor())) {
             UiDrawing.drawRoundedRect(iconX - DOCK_ICON_SLOT / 2.0F, centerY - 11.0F, 1.0F, 22.0F, 0.0F, DOCK_SEPARATOR);
          }
         boolean hover = !this.closing && this.inside(this.mouseX, this.mouseY, iconX - DOCK_ICON_SLOT_HALF, dockY, DOCK_HOVER_SLOT, DOCK_HEIGHT);
         this.categoryHover[slot].set(hover ? 1.0F : 0.0F);
         this.categoryHover[slot].update(this.deltaSeconds);
         this.categoryActive[slot].set(entry == this.category ? 1.0F : 0.0F);
         this.categoryActive[slot].update(this.deltaSeconds);
         int color = ThirdGuiColors.lerp(
            ThirdGuiColors.lerp(ThirdGuiColors.ICON_IDLE, DOCK_ICON_HOVER, this.categoryHover[slot].value),
            ThirdGuiColors.accent(), this.categoryActive[slot].value);
         float k = 1.0F + DOCK_HOVER_GROW * this.categoryHover[slot].value + DOCK_ACTIVE_GROW * this.categoryActive[slot].value;
         ThirdGuiIcons.draw(entry.icon(), iconX, centerY - DOCK_ICON_LIFT * this.categoryHover[slot].value,
            DOCK_ICON_SIZE * k, color);
          this.hit(iconX - DOCK_ICON_SLOT_HALF, dockY, DOCK_HOVER_SLOT, DOCK_HEIGHT, HIT_DOCK, null, null, index, false);
          if (hover) {
             String title = ClientLocalization.translate(entry.title());
             float labelWidth = ThirdGuiScreen.width(ThirdGuiFonts.semibold(), title, 15.0F) + 22.0F;
             UiDrawing.drawRoundedRect(iconX - labelWidth / 2.0F, this.dockTooltipY(dockY), labelWidth, 28.0F, 8.0F, ThirdGuiColors.dock());
             ThirdGuiScreen.textCenter(ThirdGuiFonts.semibold(), title, iconX, this.dockTooltipY(dockY) + 14.0F, 15.0F, ThirdGuiColors.text());
          }
      }

      float tail = DOCK_ICON_SLOT * extraSlots;
      UiDrawing.drawRoundedRect(dockX + DOCK_DIVIDER_OFFSET + tail, centerY - DOCK_SEPARATOR_HEIGHT / 2.0F, 1.0F,
         DOCK_SEPARATOR_HEIGHT, 0.0F, DOCK_SEPARATOR);

      boolean chevronHover = !this.closing && this.inside(this.mouseX, this.mouseY, dockX + DOCK_CHEVRON_SLOT + tail, dockY,
         DOCK_HOVER_SLOT, DOCK_HEIGHT);
      this.chevronHover.set(chevronHover ? 1.0F : 0.0F);
      this.chevronHover.update(this.deltaSeconds);
      ThirdGuiIcons.draw(ThirdGuiIcons.caret(), dockX + DOCK_CHEVRON_OFFSET + tail, centerY,
         DOCK_CHEVRON_SIZE * (1.0F + DOCK_CHEVRON_GROW * this.chevronHover.value),
         ThirdGuiColors.lerp(ThirdGuiColors.ICON_IDLE, ThirdGuiColors.text(), this.chevronHover.value));
      this.hit(dockX + DOCK_CHEVRON_SLOT + tail, dockY, DOCK_HOVER_SLOT, DOCK_HEIGHT, HIT_CHEVRON, null, null, 0, false);

      this.drawRotatedIcon(ThirdGuiIcons.notch(), dockX + DOCK_SPINNER_OFFSET + tail, centerY,
         DOCK_CHEVRON_SIZE, this.time * 5.0F, ThirdGuiColors.accent());
      this.drawAvatar(dockX + DOCK_AVATAR_OFFSET + tail, centerY);
   }

   private void drawAvatar(float centerX, float centerY) {
      String name = MINECRAFT_CLIENT.player == null ? "" : MINECRAFT_CLIENT.player.getScoreboardName();
      Identifier skin = MINECRAFT_CLIENT.player == null ? null : MINECRAFT_CLIENT.player.getSkin().body().texturePath();
      if (skin != null) {
         float x = centerX - DOCK_AVATAR_SIZE / 2.0F;
         float y = centerY - DOCK_AVATAR_SIZE / 2.0F;
         // Face and hat layer of the already loaded Minecraft skin (no external requests).
         UiDrawing.drawTexture(x, y, DOCK_AVATAR_SIZE, DOCK_AVATAR_SIZE, DOCK_AVATAR_RADIUS,
            new Vector4f(8.0F / 64.0F, 8.0F / 64.0F, 8.0F / 64.0F, 8.0F / 64.0F), WHITE, skin, FilterMode.NEAREST);
         UiDrawing.drawTexture(x, y, DOCK_AVATAR_SIZE, DOCK_AVATAR_SIZE, DOCK_AVATAR_RADIUS,
            new Vector4f(40.0F / 64.0F, 8.0F / 64.0F, 8.0F / 64.0F, 8.0F / 64.0F), WHITE, skin, FilterMode.NEAREST);
         return;
      }

      UiDrawing.drawRoundedRect(centerX - DOCK_AVATAR_SIZE / 2.0F, centerY - DOCK_AVATAR_SIZE / 2.0F,
         DOCK_AVATAR_SIZE, DOCK_AVATAR_SIZE, DOCK_AVATAR_RADIUS, AVATAR_PLACEHOLDER);
      String letter = name == null || name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase(Locale.ROOT);
      ThirdGuiScreen.textCenter(ThirdGuiFonts.semibold(), letter, centerX, centerY,
         DOCK_AVATAR_SIZE * DOCK_AVATAR_LETTER, ThirdGuiColors.accent());
   }

   /** Approximate the original squared smoothstep SDF shadow using only the base's rect pass. */
   private void drawSoftShadow(float x, float y, float width, float height, float radius, float blur, int color) {
      float opacity = (color >>> 24) / GLOBAL_ALPHA_SCALE * (this.alpha / GLOBAL_ALPHA_SCALE);
      float accumulated = 0.0F;
      // UiDrawing.drawShadow couples corner radius and spread and uses a different falloff.
      // Two-pixel SDF contour layers retain independent radius/blur and source-over opacity.
      UiDrawing.setGlobalAlpha(GLOBAL_ALPHA_SCALE);
      try {
         for (float expansion = blur; expansion > -blur; expansion -= 2.0F) {
            float progress = ThirdGuiAnim.clamp01((expansion - 1.0F + blur) / (2.0F * blur));
            float falloff = 1.0F - progress * progress * (3.0F - 2.0F * progress);
            float desired = opacity * falloff * falloff;
            int layerAlpha = Math.round(Math.max(0.0F, (desired - accumulated) / (1.0F - accumulated)) * GLOBAL_ALPHA_SCALE);
            if (layerAlpha > 0 && width + 2.0F * expansion > 0.0F && height + 2.0F * expansion > 0.0F) {
               UiDrawing.drawRoundedRect(x - expansion, y - expansion, width + 2.0F * expansion,
                  height + 2.0F * expansion, Math.max(0.0F, radius + expansion), color & 0xFFFFFF | layerAlpha << 24);
               accumulated += (1.0F - accumulated) * layerAlpha / GLOBAL_ALPHA_SCALE;
            }
         }
      } finally {
         UiDrawing.setGlobalAlpha(this.alpha);
      }
   }

   // ------------------------------------------------------------------------------------------
   // Color picker
   // ------------------------------------------------------------------------------------------

   private float[] pickerRect() {
      ColorSetting setting = this.openColorPicker;
      if (setting == null) {
         return null;
      }

      float bars = setting.isAlphaEnabled() ? 2.0F : 1.0F;
      float totalWidth = PICKER_WIDTH + PICKER_BAR_GAP + PICKER_BAR_WIDTH * bars + PICKER_BAR_GAP * (bars - 1.0F);
      float totalHeight = PICKER_HEIGHT + PICKER_PADDING * 2.0F;
      float x = this.panelX + PANEL_WIDTH - totalWidth - PANEL_PADDING;
      float y = this.panelY + PANEL_HEIGHT - DOCK_BOTTOM_MARGIN - DOCK_HEIGHT - PICKER_GAP - totalHeight;
      return new float[]{x, y, totalWidth, totalHeight};
   }

   private float[] pickerBars() {
      float[] rect = this.pickerRect();
      if (rect == null) {
         return null;
      }

      float areaX = rect[0] + PICKER_PADDING;
      float areaY = rect[1] + PICKER_PADDING;
      float hueX = areaX + PICKER_WIDTH + PICKER_BAR_GAP;
      float alphaX = hueX + PICKER_BAR_WIDTH + PICKER_BAR_GAP;
      return new float[]{areaX, areaY, hueX, this.openColorPicker.isAlphaEnabled() ? alphaX : -1.0F};
   }

   private void drawColorPicker() {
      ColorSetting setting = this.openColorPicker;
      float[] rect = this.pickerRect();
      float[] bars = this.pickerBars();
      if (rect == null || bars == null) {
         return;
      }

      SettingView view = this.view(setting);
      float[] hsba = view.hsba;
      float x = rect[0];
      float y = rect[1];
      float width = rect[2];
      float height = rect[3];
      float areaX = bars[0];
      float areaY = bars[1];
      float hueX = bars[2];
      float alphaX = bars[3];

      UiDrawing.drawShadow(x, y + 8.0F, width, height, 16.0F, 0x99000000);
      UiDrawing.drawRoundedRect(x, y, width, height, 14.0F, 0xF21A1A1E);
      UiDrawing.drawOutline(x, y, width, height, 1.0F, 14.0F, DOCK_OUTLINE);

      float cellWidth = PICKER_WIDTH / PICKER_GRID_COLUMNS;
      float cellHeight = PICKER_HEIGHT / PICKER_GRID_ROWS;
      for (int row = 0; row < PICKER_GRID_ROWS; row++) {
         float value = 1.0F - (float)row / (PICKER_GRID_ROWS - 1);
         for (int column = 0; column < PICKER_GRID_COLUMNS; column++) {
            float saturation = (float)column / (PICKER_GRID_COLUMNS - 1);
            UiDrawing.drawRoundedRect(areaX + column * cellWidth, areaY + row * cellHeight,
               cellWidth + 0.5F, cellHeight + 0.5F, 0.0F, ThirdGuiScreen.hsvToArgb(hsba[0], saturation, value, 1.0F));
         }
      }

      float handleX = areaX + hsba[1] * PICKER_WIDTH;
      float handleY = areaY + (1.0F - hsba[2]) * PICKER_HEIGHT;
      UiDrawing.drawOutline(handleX - PICKER_HANDLE_SIZE / 2.0F, handleY - PICKER_HANDLE_SIZE / 2.0F,
         PICKER_HANDLE_SIZE, PICKER_HANDLE_SIZE, 1.5F, PICKER_HANDLE_SIZE / 2.0F, WHITE);

      float stripHeight = PICKER_HEIGHT / PICKER_GRADIENT_STEPS + 0.5F;
      for (int step = 0; step < PICKER_GRADIENT_STEPS; step++) {
         float fraction = (float)step / (PICKER_GRADIENT_STEPS - 1);
         UiDrawing.drawRoundedRect(hueX, areaY + PICKER_HEIGHT - (step + 1) * stripHeight, PICKER_BAR_WIDTH,
            stripHeight, 0.0F, ThirdGuiScreen.hsvToArgb(fraction, 1.0F, 1.0F, 1.0F));
      }

      UiDrawing.drawRoundedRect(hueX - 1.0F, areaY + PICKER_HEIGHT - hsba[0] * PICKER_HEIGHT - 2.0F,
         PICKER_BAR_WIDTH + 2.0F, 4.0F, 2.0F, WHITE);

      if (setting.isAlphaEnabled()) {
         int baseRgb = ThirdGuiScreen.hsvToArgb(hsba[0], hsba[1], hsba[2], 1.0F) & 0xFFFFFF;
         for (int step = 0; step < PICKER_GRADIENT_STEPS; step++) {
            float fraction = (float)step / (PICKER_GRADIENT_STEPS - 1);
            UiDrawing.drawRoundedRect(alphaX, areaY + PICKER_HEIGHT - (step + 1) * stripHeight, PICKER_BAR_WIDTH,
               stripHeight, 0.0F, baseRgb | Math.round(fraction * GLOBAL_ALPHA_SCALE) << 24);
         }

         UiDrawing.drawRoundedRect(alphaX - 1.0F, areaY + PICKER_HEIGHT - hsba[3] * PICKER_HEIGHT - 2.0F,
            PICKER_BAR_WIDTH + 2.0F, 4.0F, 2.0F, WHITE);
      }

      this.hit(areaX, areaY, PICKER_WIDTH, PICKER_HEIGHT, HIT_PICKER_ZONE_BASE + PICKER_SATURATION, null, setting, 0, false);
      this.hit(hueX, areaY, PICKER_BAR_WIDTH, PICKER_HEIGHT, HIT_PICKER_ZONE_BASE + PICKER_HUE, null, setting, 0, false);
      if (setting.isAlphaEnabled()) {
         this.hit(alphaX, areaY, PICKER_BAR_WIDTH, PICKER_HEIGHT, HIT_PICKER_ZONE_BASE + PICKER_ALPHA, null, setting, 0, false);
      }
   }

   private void applyPickerDrag() {
      ColorSetting setting = this.openColorPicker;
      float[] bars = this.pickerBars();
      if (setting == null || bars == null || this.draggingColorZone == PICKER_NONE) {
         return;
      }

      float[] hsba = this.view(setting).hsba;
      float progressY = ThirdGuiAnim.clamp01(1.0F - (this.mouseY - bars[1]) / PICKER_HEIGHT);
      if (this.draggingColorZone == PICKER_SATURATION) {
         hsba[1] = ThirdGuiAnim.clamp01((this.mouseX - bars[0]) / PICKER_WIDTH);
         hsba[2] = progressY;
      } else if (this.draggingColorZone == PICKER_HUE) {
         hsba[0] = progressY;
      } else {
         hsba[3] = progressY;
      }

      setting.setValue(ThirdGuiScreen.hsvToArgb(hsba[0], hsba[1], hsba[2], setting.isAlphaEnabled() ? hsba[3] : 1.0F));
   }

   private void closePicker() {
      this.openColorPicker = null;
      this.draggingColor = null;
      this.draggingColorZone = PICKER_NONE;
   }

   // ------------------------------------------------------------------------------------------
   // Input
   // ------------------------------------------------------------------------------------------

   @Override
   public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
      if (this.closing) {
         return true;
      }
      this.readMouse(event.x(), event.y());
        if (this.searchOpen && this.searchVisibility() > VISIBLE_EPSILON
           && this.inside(this.mouseX, this.mouseY, this.searchX(), this.searchY(), SEARCH_WIDTH, SEARCH_HEIGHT)) {
          this.commitText();
          this.closePicker();
          this.bindingModule = null;
          this.bindingSetting = null;
          this.searchFocused = true;
          return true;
       }
       this.searchFocused = false;

      if (this.openColorPicker != null) {
         for (int index = this.hits.size() - 1; index >= 0; index--) {
            Hit hit = this.hits.get(index);
            if (hit.type >= HIT_PICKER_ZONE_BASE
               && this.inside(this.mouseX, this.mouseY, hit.x, hit.y, hit.width, hit.height)) {
               this.draggingColor = (ColorSetting)hit.setting;
               this.draggingColorZone = hit.type - HIT_PICKER_ZONE_BASE;
               this.applyPickerDrag();
               return true;
            }
         }

         this.closePicker();
         return true;
      }

      if (this.bindingSetting != null) {
         if (event.button() != 0) {
            this.bindingSetting.setValue(1000 + event.button());
         }

         this.bindingSetting = null;
         this.saveConfig();
         return true;
      }

      if (this.bindingModule != null) {
         if (event.button() != 0) {
            this.bindingModule.getKeybind().setKeyCode(1000 + event.button());
         }

         this.bindingModule = null;
         this.saveConfig();
         return true;
      }

      this.commitText();

      for (int index = this.hits.size() - 1; index >= 0; index--) {
         Hit hit = this.hits.get(index);
         if (!this.inside(this.mouseX, this.mouseY, hit.x, hit.y, hit.width, hit.height)) {
            continue;
         }

           if (hit.clipped && !this.inside(this.mouseX, this.mouseY, this.panelX, this.panelY, PANEL_WIDTH, PANEL_HEIGHT)) {
            continue;
         }

          this.applyHit(hit, event.button());
          return true;
       }

       return true;
   }

   private void applyHit(Hit hit, int button) {
      switch (hit.type) {
         case HIT_TOGGLE -> {
            if (button == 0) {
               // Matches the first gui: module state is persisted by the regular shutdown save.
               hit.module.toggle();
            }
         }
         case HIT_BIND -> {
            if (button == 0) {
               this.bindingModule = hit.module;
            } else if (button == 1) {
               hit.module.getKeybind().setKeyCode(-1);
               this.saveConfig();
            }
         }
         case HIT_HEADER -> {
            if (button <= 1) {
               ModuleView view = this.view(hit.module);
               view.collapsed = !view.collapsed;
            }
         }
         case HIT_BOOL -> {
            if (button == 0) {
               BooleanSetting setting = (BooleanSetting)hit.setting;
               setting.setValue(!setting.getValue());
               this.saveConfig();
            }
         }
         case HIT_SLIDER -> {
            if (button == 0) {
               this.draggingSlider = (FloatSetting)hit.setting;
               this.dragOrigin = hit.x + SLIDER_GRAB_PADDING;
               this.dragWidth = hit.width - SLIDER_GRAB_PADDING * 2.0F;
               this.applySliderDrag();
            }
         }
         case HIT_RANGE -> {
            if (button == 0) {
               RangeSetting setting = (RangeSetting)hit.setting;
               this.draggingRange = setting;
               this.dragOrigin = hit.x + SLIDER_GRAB_PADDING;
               this.dragWidth = hit.width - SLIDER_GRAB_PADDING * 2.0F;
               float progress = ThirdGuiAnim.clamp01((this.mouseX - this.dragOrigin) / Math.max(1.0F, this.dragWidth));
               Vector2f value = setting.getValue();
               float low = this.normalize(value.x, setting.getBounds().x, setting.getBounds().y);
               float high = this.normalize(value.y, setting.getBounds().x, setting.getBounds().y);
               this.draggingRangeHandle = Math.abs(progress - low) <= Math.abs(progress - high)
                  ? RANGE_HANDLE_LOW : RANGE_HANDLE_HIGH;
               this.applyRangeDrag();
            }
         }
         case HIT_MODE -> {
            if (button == 0) {
               SettingView view = this.view(hit.setting);
               view.expanded = !view.expanded;
            }
         }
         case HIT_OPTION -> {
            if (button == 0) {
               if (hit.setting instanceof SingleChoiceSetting choice) {
                  List<ChoiceOption> options = choice.getValue();
                  if (hit.arg >= 0 && hit.arg < options.size()) {
                     choice.setSelectedOption(options.get(hit.arg));
                  }
               } else if (hit.setting instanceof MultiChoiceSetting choice) {
                  List<ChoiceOption> options = choice.getValue();
                  if (hit.arg >= 0 && hit.arg < options.size()) {
                     ChoiceOption option = options.get(hit.arg);
                     option.setSelected(!option.isSelected());
                  }
               }

               this.view(hit.setting).expanded = false;
               this.saveConfig();
            }
         }
         case HIT_ACTION -> {
            if (button == 0) {
               Runnable action = ((ActionSetting)hit.setting).getAction();
               if (action != null) {
                  action.run();
               }
            }
         }
         case HIT_DOCK -> {
            if (hit.arg >= 0 && hit.arg < this.dockCategories.size()) {
               this.setCategory(this.dockCategories.get(hit.arg));
            }
         }
         case HIT_CHEVRON -> {
            this.allCollapsed = !this.allCollapsed;
            for (ClientModule module : this.visibleModules) {
               this.view(module).collapsed = this.allCollapsed;
            }
         }
         case HIT_SETTING_BIND -> {
            if (button == 0) {
               this.bindingSetting = (KeybindSetting)hit.setting;
            } else if (button == 1) {
               ((KeybindSetting)hit.setting).setValue(-1);
               this.saveConfig();
            }
         }
         case HIT_TEXT -> {
            if (button == 0) {
               this.editingText = (TextSetting)hit.setting;
               this.textBuffer = this.editingText.getValue();
            }
         }
         case HIT_COLOR -> {
            if (button == 0) {
               ColorSetting setting = (ColorSetting)hit.setting;
               if (this.openColorPicker == setting) {
                  this.closePicker();
               } else {
                  this.openColorPicker = setting;
                  SettingView view = this.view(setting);
                  if (!view.hsbaReady) {
                     ThirdGuiScreen.argbToHsva(setting.getValue(), view.hsba);
                     view.hsbaReady = true;
                  }
               }
            }
         }
         default -> {
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent event) {
      if (this.closing) {
         return true;
      }
      boolean wasDragging = this.draggingSlider != null || this.draggingRange != null || this.draggingColor != null;
      this.draggingSlider = null;
      this.draggingRange = null;
      this.draggingRangeHandle = RANGE_HANDLE_LOW;
      this.draggingColor = null;
      this.draggingColorZone = PICKER_NONE;
      if (wasDragging) {
         this.saveConfig();
      }

      return wasDragging || super.mouseReleased(event);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
      if (this.closing) {
         return true;
      }
      this.readMouse(event.x(), event.y());
      this.updateDrag();
      return true;
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
      if (this.closing) {
         return true;
      }
      this.scrollTarget = Math.clamp(this.scrollTarget - (float)verticalScroll * SCROLL_SPEED, 0.0F, this.maxScroll);
      return true;
   }

   @Override
   public boolean keyPressed(KeyEvent event) {
      if (this.closing) {
         return true;
      }
       int key = event.key();
       if (System.currentTimeMillis() - this.openedAtMillis < OPEN_GUARD_MILLIS && (key == CLICKGUI_KEY || key == GLFW.GLFW_KEY_ESCAPE)) {
          return true;
       }
       if (key == CLICKGUI_KEY && this.bindingModule == null && this.bindingSetting == null) {
          this.onClose();
          return true;
       }
      if (this.bindingSetting != null) {
         if (key != GLFW.GLFW_KEY_ESCAPE) {
            this.bindingSetting.setValue(key == GLFW.GLFW_KEY_DELETE || key == GLFW.GLFW_KEY_BACKSPACE ? -1 : key);
         }

         this.bindingSetting = null;
         this.saveConfig();
         return true;
      }

       if (this.bindingModule != null) {
         if (key != GLFW.GLFW_KEY_ESCAPE) {
            this.bindingModule.getKeybind().setKeyCode(key == GLFW.GLFW_KEY_DELETE || key == GLFW.GLFW_KEY_BACKSPACE ? -1 : key);
         }

         this.bindingModule = null;
         this.saveConfig();
          return true;
       }

        if (key == GLFW.GLFW_KEY_F && (event.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0) {
           this.setSearchOpen(!this.searchOpen);
          return true;
       }
        if (this.searchOpen && key == GLFW.GLFW_KEY_ESCAPE) {
           this.setSearchOpen(false);
          return true;
       }
       if (this.searchFocused) {
          if (key == GLFW.GLFW_KEY_BACKSPACE && !this.searchQuery.isEmpty()) {
             this.setSearchQuery(this.searchQuery.substring(0, this.searchQuery.offsetByCodePoints(this.searchQuery.length(), -1)));
          } else if (key == GLFW.GLFW_KEY_DELETE || (key == GLFW.GLFW_KEY_A && (event.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0)) {
             this.setSearchQuery("");
          } else if (key == GLFW.GLFW_KEY_V && (event.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0) {
             this.setSearchQuery(this.searchQuery + MINECRAFT_CLIENT.keyboardHandler.getClipboard());
          } else if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
             this.searchFocused = false;
          }
          return true;
       }

      if (this.editingText != null) {
         if (key == GLFW.GLFW_KEY_BACKSPACE) {
            if (!this.textBuffer.isEmpty()) {
               int lastIndex = this.textBuffer.offsetByCodePoints(this.textBuffer.length(), -1);
               this.textBuffer = this.textBuffer.substring(0, lastIndex);
            }

            return true;
         }

         if (key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            this.commitText();
         }

         return true;
      }

      if (this.openColorPicker != null && key == GLFW.GLFW_KEY_ESCAPE) {
         this.closePicker();
         return true;
      }

      if (key == GLFW.GLFW_KEY_ESCAPE || key == CLICKGUI_KEY) {
         this.onClose();
         return true;
      }

      return super.keyPressed(event);
   }

   @Override
   public boolean charTyped(CharacterEvent event) {
      if (this.closing) {
         return true;
      }
       if (this.searchFocused) {
          if (!Character.isISOControl(event.codepoint())) this.setSearchQuery(this.searchQuery + event.codepointAsString());
          return true;
       }
      if (this.editingText != null) {
         if (!Character.isISOControl(event.codepoint())
            && this.textBuffer.codePointCount(0, this.textBuffer.length()) < MAX_TEXT_CODEPOINTS) {
            this.textBuffer = this.textBuffer + event.codepointAsString();
         }

         return true;
      }

      return super.charTyped(event);
   }

   private void readMouse(double guiX, double guiY) {
      Vector2f framebufferMouse = WorldProjection.guiToScreen(guiX, guiY);
       float safeScale = Math.max(0.001F, this.scale);
      this.mouseX = framebufferMouse.x / safeScale;
      this.mouseY = framebufferMouse.y / safeScale;
   }

   private void updateDrag() {
      if (this.draggingSlider != null) {
         this.applySliderDrag();
      }

      if (this.draggingRange != null) {
         this.applyRangeDrag();
      }

      if (this.draggingColor != null) {
         this.applyPickerDrag();
      }
   }

   private void applySliderDrag() {
      FloatSetting setting = this.draggingSlider;
      if (setting == null) {
         return;
      }

      float progress = ThirdGuiAnim.clamp01((this.mouseX - this.dragOrigin) / Math.max(1.0F, this.dragWidth));
      float minimum = setting.getMinimum();
      float maximum = setting.getMaximum();
      float step = setting.getStep();
      float raw = minimum + progress * (maximum - minimum);
      if (step > 0.0F) {
         raw = Math.round(raw / step) * step;
      }

      setting.setValue(Math.clamp(raw, minimum, maximum));
   }

   private void applyRangeDrag() {
      RangeSetting setting = this.draggingRange;
      if (setting == null) {
         return;
      }

      float progress = ThirdGuiAnim.clamp01((this.mouseX - this.dragOrigin) / Math.max(1.0F, this.dragWidth));
      float minimum = setting.getBounds().x;
      float maximum = setting.getBounds().y;
      float step = setting.getStep();
      float raw = minimum + progress * (maximum - minimum);
      if (step > 0.0F) {
         raw = Math.round(raw / step) * step;
      }

      raw = Math.clamp(raw, minimum, maximum);
      Vector2f value = setting.getValue();
      float low = value.x;
      float high = value.y;
      if (this.draggingRangeHandle == RANGE_HANDLE_LOW) {
         low = Math.min(raw, high - setting.getMinimumGap());
      } else {
         high = Math.max(raw, low + setting.getMinimumGap());
      }

      setting.setValue(new Vector2f(Math.clamp(low, minimum, maximum), Math.clamp(high, minimum, maximum)));
   }

   private void commitText() {
      if (this.editingText != null) {
         this.editingText.setValue(this.textBuffer);
         this.editingText = null;
         this.saveConfig();
      }
   }

    private void setCategory(ThirdGuiCategory next) {
       if (next.isEditor()) {
          this.commitText();
          this.saveConfig();
          MINECRAFT_CLIENT.gui.setScreen(new ThirdGuiPanelScreen(this, next));
          return;
       }
       this.setSearchQuery("");
       if (next == this.category) {
          return;
       }

      this.category = next;
      this.bindingModule = null;
      this.bindingSetting = null;
      this.draggingSlider = null;
      this.draggingRange = null;
      this.clock = 0.0F;
      this.scroll = 0.0F;
      this.scrollTarget = 0.0F;
      this.allCollapsed = false;
       this.closePicker();
    }

     private float searchX() { return this.panelX + (PANEL_WIDTH - SEARCH_WIDTH) / 2.0F; }
     private float searchVisibility() { return ThirdGuiAnim.clamp01(this.searchAnimation.value * 2.0F - 1.0F); }
     private float searchY() {
        return this.panelY + PANEL_HEIGHT + SEARCH_GAP + 8.0F * (1.0F - this.searchVisibility());
     }

     private void setSearchOpen(boolean open) {
        this.commitText();
        this.closePicker();
        this.draggingSlider = null;
        this.draggingRange = null;
        this.searchOpen = open;
        this.searchFocused = open;
        this.searchAnimation.set(open ? 1.0F : 0.0F);
        if (!open) this.setSearchQuery("");
     }

     private void drawSearchFooter() {
        // Fade the hint out before revealing the field; reverse the same transition on close.
        float hintOpacity = ThirdGuiAnim.clamp01(1.0F - this.searchAnimation.value * 2.0F);
        if (hintOpacity > VISIBLE_EPSILON) {
           this.push();
           this.multiplyAlpha(hintOpacity);
           ThirdGuiScreen.textCenter(ThirdGuiFonts.regular(), "Для поиска Ctrl + F",
              this.panelX + PANEL_WIDTH / 2.0F,
              this.panelY + PANEL_HEIGHT + SEARCH_GAP + SEARCH_HEIGHT / 2.0F + 4.0F * (1.0F - hintOpacity),
              SEARCH_HINT_SIZE, 0x80FFFFFF);
           this.pop();
        }
        float searchOpacity = this.searchVisibility();
        if (searchOpacity > VISIBLE_EPSILON) {
           this.push();
           this.multiplyAlpha(searchOpacity);
           this.drawSearch();
           this.pop();
        }
     }

    private void setSearchQuery(String query) {
       String clean = query.codePoints().filter(c -> !Character.isISOControl(c)).limit(MAX_TEXT_CODEPOINTS)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
       if (!clean.equals(this.searchQuery)) {
          this.searchQuery = clean;
          this.scroll = 0.0F;
          this.scrollTarget = 0.0F;
          this.clock = 0.0F;
          this.hits.clear();
       }
    }

    private void drawSearch() {
       float x = this.searchX();
       float y = this.searchY();
       UiDrawing.drawRoundedRect(x, y, SEARCH_WIDTH, SEARCH_HEIGHT, 14.0F, ThirdGuiColors.PANEL);
       UiDrawing.drawOutline(x, y, SEARCH_WIDTH, SEARCH_HEIGHT, 1.0F, 14.0F,
          this.searchFocused ? ThirdGuiColors.ACCENT : PANEL_OUTLINE);
       UiDrawing.pushScissor(x + 16.0F, y + 4.0F, SEARCH_WIDTH - 32.0F, SEARCH_HEIGHT - 8.0F);
       String label = this.searchQuery.isEmpty() ? "Поиск модулей..." : this.searchQuery;
       float textWidth = ThirdGuiScreen.width(ThirdGuiFonts.regular(), this.searchQuery, 18.0F);
       float left = x + 18.0F - Math.max(0.0F, textWidth - SEARCH_WIDTH + 44.0F);
       ThirdGuiScreen.textMid(ThirdGuiFonts.regular(), label, left, y + SEARCH_HEIGHT / 2.0F, 18.0F,
          this.searchQuery.isEmpty() ? ThirdGuiColors.DESC : ThirdGuiColors.TEXT);
       if (this.searchFocused && System.currentTimeMillis() / 500L % 2L == 0L) {
          UiDrawing.drawRoundedRect(left + textWidth + 2.0F, y + 15.0F, 1.5F, 20.0F, 0.5F, ThirdGuiColors.ACCENT);
       }
       UiDrawing.popScissor();
    }

   // ------------------------------------------------------------------------------------------
   // State helpers
   // ------------------------------------------------------------------------------------------

   private ModuleView view(ClientModule module) {
      return this.moduleViews.computeIfAbsent(module, ignored -> new ModuleView());
   }

   private SettingView view(ValueSetting<?> setting) {
      return this.settingViews.computeIfAbsent(setting, ignored -> new SettingView());
   }

   private String descriptionOf(ClientModule module) {
      String description = module.getDescription();
      return description == null ? "" : ClientLocalization.translate(description);
   }

   private float normalize(float value, float minimum, float maximum) {
      if (maximum - minimum <= 0.0F) {
         return 0.0F;
      }

      return (value - minimum) / (maximum - minimum);
   }

   private void saveConfig() {
      if (RoseClient.getInstance() != null) {
         RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
      }
   }

   // ------------------------------------------------------------------------------------------
   // Transform state machine, mirroring the original Gfx
   // ------------------------------------------------------------------------------------------

   private void beginFrame(int framebufferWidth, int framebufferHeight) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      this.transformStack.clear();
      this.transformScale = 1.0F;
      this.transformX = 0.0F;
      this.transformY = 0.0F;
      this.alpha = GLOBAL_ALPHA_SCALE;
      this.ndcForward.identity().translate(-1.0F, 1.0F, 0.0F).scale(2.0F / framebufferWidth, -2.0F / framebufferHeight, 1.0F);
      this.ndcInverse.identity().scale(framebufferWidth * 0.5F, -framebufferHeight * 0.5F, 1.0F).translate(1.0F, -1.0F, 0.0F);
      this.applyTransform();
   }

   private void applyTransform() {
      // tx/ty are already framebuffer-space offsets, like the original Gfx stack.
      this.pixelMatrix.identity().translate(this.transformX, this.transformY, 0.0F).scale(this.transformScale, this.transformScale, 1.0F);
      this.ndcMatrix.set(this.ndcForward).mul(this.pixelMatrix).mul(this.ndcInverse);
      UiDrawing.setTransform(this.ndcMatrix);
   }

   private void push() {
      this.transformStack.push(new float[]{this.transformScale, this.transformX, this.transformY, this.alpha});
   }

   private void pop() {
      float[] state = this.transformStack.poll();
      if (state == null) {
         return;
      }

      this.transformScale = state[0];
      this.transformX = state[1];
      this.transformY = state[2];
      this.setAlpha(state[3]);
      this.applyTransform();
   }

   private void translate(float dx, float dy) {
      this.transformX += dx * this.transformScale;
      this.transformY += dy * this.transformScale;
      this.applyTransform();
   }

   private void scaleAbout(float centerX, float centerY, float factor) {
      this.transformX += centerX * this.transformScale * (1.0F - factor);
      this.transformY += centerY * this.transformScale * (1.0F - factor);
      this.transformScale *= factor;
      this.applyTransform();
   }

   private void drawRotatedIcon(String name, float centerX, float centerY, float size, float angle, int color) {
      // Compose in pixel space, then conjugate to the NDC transform used by UiDrawing.
      this.pixelMatrix.translate(centerX, centerY, 0.0F).rotateZ(angle).translate(-centerX, -centerY, 0.0F);
      this.ndcMatrix.set(this.ndcForward).mul(this.pixelMatrix).mul(this.ndcInverse);
      UiDrawing.setTransform(this.ndcMatrix);
      try {
         ThirdGuiIcons.draw(name, centerX, centerY, size, color);
      } finally {
         this.applyTransform();
      }
   }

   private void setTransformScale(float newScale, float dx, float dy) {
      this.transformScale = newScale;
      this.transformX = dx;
      this.transformY = dy;
      this.applyTransform();
   }

   private void multiplyAlpha(float factor) {
      this.setAlpha(this.alpha * factor);
   }

   private void setAlpha(float value) {
      this.alpha = Math.clamp(value, 0.0F, GLOBAL_ALPHA_SCALE);
      UiDrawing.setGlobalAlpha(this.alpha);
   }

   private boolean overPanel() {
      return !this.closing && this.inside(this.mouseX, this.mouseY, this.panelX, this.panelY, PANEL_WIDTH, PANEL_HEIGHT);
   }

   private boolean inside(float x, float y, float rectX, float rectY, float width, float height) {
      return x >= rectX && x <= rectX + width && y >= rectY && y <= rectY + height;
   }

   /** Что показать при наведении: описание модуля или подсказку по настройке. */
   private void updateHoverHint() {
      String title = null;
      String body = null;
      boolean allowed = this.overPanel()
         && this.openColorPicker == null
         && this.searchAnimation.value < 0.05F
         && this.bindingModule == null
         && this.bindingSetting == null
         && this.draggingSlider == null
         && this.draggingRange == null
         && this.draggingColor == null;

      if (allowed) {
         for (int index = this.hits.size() - 1; index >= 0; index--) {
            Hit hit = this.hits.get(index);
            if (!this.inside(this.mouseX, this.mouseY, hit.x(), hit.y(), hit.width(), hit.height())) {
               continue;
            }

            if (hit.setting() != null) {
               title = ClientLocalization.translate(hit.setting().getName());
               body = hoverHintText(hit.setting());
            } else if (hit.module() != null) {
               title = ClientLocalization.translate(hit.module().getName());
               body = ClientLocalization.translate(hit.module().getDescription());
            }

            break;
         }
      }

      if (title != null) {
         this.hoverHintTitle = title;
         this.hoverHintBody = body;
      }

      this.hoverHintAlpha.set(title != null ? 1.0F : 0.0F).update(this.deltaSeconds);
   }

   /** Плашка в стиле панели: имя, тонкая рубиновая полоска и текст описания. */
   private void drawHoverHint(float canvasWidth, float canvasHeight) {
      float appear = this.hoverHintAlpha.value;
      if (appear <= 0.02F || this.hoverHintTitle == null) {
         return;
      }

      String body = this.hoverHintBody == null || this.hoverHintBody.isBlank() ? "Описание пока не задано" : this.hoverHintBody;
      FontRenderer semibold = ThirdGuiFonts.semibold();
      FontRenderer regular = ThirdGuiFonts.regular();
      List<String> lines = wrapHint(regular, body, HINT_BODY_SIZE, HINT_MAX_WIDTH);

      float titleWidth = width(semibold, this.hoverHintTitle, HINT_TITLE_SIZE);
      float textWidth = 0.0F;
      for (String line : lines) {
         textWidth = Math.max(textWidth, width(regular, line, HINT_BODY_SIZE));
      }

      float contentWidth = Math.min(HINT_MAX_WIDTH, Math.max(titleWidth, textWidth));
      float paddingLeft = HINT_PADDING + HINT_ACCENT_WIDTH + HINT_ACCENT_GAP;
      float plateWidth = contentWidth + paddingLeft + HINT_PADDING;
      float bodyHeight = Math.max(1, lines.size()) * HINT_LINE_HEIGHT;
      float plateHeight = HINT_PADDING * 2.0F + HINT_TITLE_SIZE + HINT_TITLE_GAP + bodyHeight - (HINT_LINE_HEIGHT - HINT_BODY_SIZE);

      float x = this.mouseX + HINT_CURSOR_OFFSET_X;
      float y = this.mouseY + HINT_CURSOR_OFFSET_Y;
      if (x + plateWidth > canvasWidth - HINT_SCREEN_MARGIN) {
         x = this.mouseX - HINT_CURSOR_OFFSET_X - plateWidth;
      }

      if (y + plateHeight > canvasHeight - HINT_SCREEN_MARGIN) {
         y = this.mouseY - HINT_CURSOR_OFFSET_Y - plateHeight;
      }

      x = Math.clamp(x, HINT_SCREEN_MARGIN, Math.max(HINT_SCREEN_MARGIN, canvasWidth - plateWidth - HINT_SCREEN_MARGIN));
      y = Math.clamp(y, HINT_SCREEN_MARGIN, Math.max(HINT_SCREEN_MARGIN, canvasHeight - plateHeight - HINT_SCREEN_MARGIN));

      UiDrawing.disableScissor();
      UiDrawing.drawRoundedRect(x, y + 3.0F, plateWidth, plateHeight, HINT_CORNER, ThirdGuiColors.alpha(0xFF000000, 0.45F * appear));
      UiDrawing.drawRoundedRect(x, y, plateWidth, plateHeight, HINT_CORNER, ThirdGuiColors.alpha(HINT_PLATE_COLOR, 0.97F * appear));
      UiDrawing.drawOutline(x, y, plateWidth, plateHeight, 1.0F, HINT_CORNER, ThirdGuiColors.alpha(0xFFFFFFFF, 0.08F * appear));
      UiDrawing.drawRoundedRect(
         x + 1.0F,
         y + HINT_PADDING,
         HINT_ACCENT_WIDTH,
         plateHeight - HINT_PADDING * 2.0F,
         HINT_ACCENT_WIDTH / 2.0F,
         ThirdGuiColors.alpha(ThirdGuiColors.accent(), 0.9F * appear)
      );

      float textX = x + paddingLeft;
      textMid(semibold, this.hoverHintTitle, textX, y + HINT_PADDING + HINT_TITLE_SIZE * 0.5F, HINT_TITLE_SIZE,
         ThirdGuiColors.alpha(ThirdGuiColors.text(), appear));
      int bodyColor = ThirdGuiColors.alpha(ThirdGuiColors.lerp(ThirdGuiColors.description(), ThirdGuiColors.text(), 0.45F), appear);
      float lineY = y + HINT_PADDING + HINT_TITLE_SIZE + HINT_TITLE_GAP;
      for (String line : lines) {
         textMid(regular, line, textX, lineY + HINT_BODY_SIZE * 0.5F, HINT_BODY_SIZE, bodyColor);
         lineY += HINT_LINE_HEIGHT;
      }
   }

   private static List<String> wrapHint(FontRenderer font, String text, float size, float maxWidth) {
      List<String> lines = new ArrayList<>();

      for (String paragraph : text.split("\n")) {
         String trimmed = paragraph.trim();
         if (trimmed.isEmpty()) {
            continue;
         }

         StringBuilder line = new StringBuilder();

         for (String word : trimmed.split(" ")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (line.length() > 0 && width(font, candidate, size) > maxWidth) {
               lines.add(line.toString());
               line.setLength(0);
               line.append(word);
            } else {
               line.setLength(0);
               line.append(candidate);
            }
         }

         if (line.length() > 0) {
            lines.add(line.toString());
         }
      }

      return lines;
   }

   private static String hoverHintText(ValueSetting<?> setting) {
      if (setting instanceof RangeSetting) {
         return "Два ползунка: нижняя и верхняя граница";
      }

      if (setting instanceof FloatSetting) {
         return "Ползунок: тяни мышью, ПКМ — сбросить";
      }

      if (setting instanceof BooleanSetting) {
         return "Переключатель: включить или выключить";
      }

      if (setting instanceof MultiChoiceSetting) {
         return "Можно выбрать несколько вариантов";
      }

      if (setting instanceof SingleChoiceSetting) {
         return "Выбор одного варианта из списка";
      }

      if (setting instanceof KeybindSetting) {
         return "Клавиша, которая включает настройку";
      }

      if (setting instanceof ColorSetting) {
         return "Цвет: нажми, чтобы открыть палитру";
      }

      if (setting instanceof ActionSetting) {
         return "Кнопка действия: нажми, чтобы выполнить";
      }

      if (setting instanceof TextSetting) {
         return "Текстовое поле: введи значение";
      }

      if (setting instanceof SectionHeaderSetting) {
         return "Группа настроек: нажми, чтобы свернуть";
      }

      return "Настройка модуля";
   }

   private void hit(float x, float y, float width, float height, int type, ClientModule module, ValueSetting<?> setting,
                    int arg, boolean clipped) {
      if (!this.closing) {
         this.hits.add(new Hit(x, y, width, height, type, module, setting, arg, clipped));
      }
   }

   // ------------------------------------------------------------------------------------------
   // Text helpers
   // ------------------------------------------------------------------------------------------

   static float width(FontRenderer font, String text, float size) {
      return UiDrawing.getTextWidth(font, text, size, 1.0F);
   }

   static void textMid(FontRenderer font, String text, float x, float middleY, float size, int color) {
      if (!font.getFontFace().usesAtlasMetrics()) {
         UiDrawing.drawText(font, text, x, UiDrawing.alignTextCenterY(font, text, middleY, size), size, color, 0.0F, 0.5F, 1.0F);
         return;
      }
      // Original textMid centres capHeight, not the padded bounds of the actual string.
      float y = UiDrawing.alignCapHeightCenterY(font, middleY, size);
      float baselineOffset = font.getFontFace().getMetrics().getBaselineOffset() * size;
      Matrix4f transform = UiDrawing.getTransform();
      float yScale = transform.m11();
      float yTranslation = (1.0F - yScale - transform.m31()) * MINECRAFT_CLIENT.getWindow().getScreenHeight() * 0.5F;
      // Gfx.drawText rounds the transformed baseline, while leaving glyph geometry fractional.
      if (yScale > 0.0F) {
         y = (Math.round((y + baselineOffset) * yScale + yTranslation) - yTranslation) / yScale - baselineOffset;
      }
      UiDrawing.drawText(font, text, x, y, size, color, 0.0F, 0.5F, 1.0F);
   }

   static void textMid(FontRenderer font, String text, float x, float middleY, float size, int color, float clipRight) {
      // Clip only the right edge, as Gfx.drawText does; keep negative left-bearing padding.
      UiDrawing.pushScissor(x - size, middleY - size, Math.max(0.0F, clipRight - x + size), size * 2.0F);
      ThirdGuiScreen.textMid(font, text, x, middleY, size, color);
      UiDrawing.popScissor();
   }

   static void textCenter(FontRenderer font, String text, float centerX, float middleY, float size, int color) {
      ThirdGuiScreen.textMid(font, text, centerX - ThirdGuiScreen.width(font, text, size) * 0.5F, middleY, size, color);
   }

   static void textCenter(FontRenderer font, String text, float centerX, float middleY, float size, int color, float maxWidth) {
      float measured = ThirdGuiScreen.width(font, text, size);
      if (measured <= maxWidth) {
         ThirdGuiScreen.textCenter(font, text, centerX, middleY, size, color);
         return;
      }

      UiDrawing.pushScissor(centerX - measured * 0.5F - 1.0F, middleY - size, measured + 2.0F, size * 2.0F);
      ThirdGuiScreen.textCenter(font, text, centerX, middleY, size, color);
      UiDrawing.popScissor();
   }

   static String formatNumber(float value, float step) {
      if (step >= 1.0F) {
         return String.valueOf(Math.round(value));
      }

      return String.format(Locale.ROOT, "%.2f", value);
   }

   // ------------------------------------------------------------------------------------------
   // Colour conversion used by the picker
   // ------------------------------------------------------------------------------------------

   static void argbToHsva(int argb, float[] target) {
      float alpha = (argb >>> 24 & 0xFF) / GLOBAL_ALPHA_SCALE;
      float red = (argb >> 16 & 0xFF) / GLOBAL_ALPHA_SCALE;
      float green = (argb >> 8 & 0xFF) / GLOBAL_ALPHA_SCALE;
      float blue = (argb & 0xFF) / GLOBAL_ALPHA_SCALE;
      float max = Math.max(red, Math.max(green, blue));
      float min = Math.min(red, Math.min(green, blue));
      float delta = max - min;
      float hue = 0.0F;
      if (delta > 0.0F) {
         if (max == red) {
            hue = (green - blue) / delta % 6.0F;
         } else if (max == green) {
            hue = (blue - red) / delta + 2.0F;
         } else {
            hue = (red - green) / delta + 4.0F;
         }

         hue /= 6.0F;
         if (hue < 0.0F) {
            hue += 1.0F;
         }
      }

      target[0] = hue;
      target[1] = max <= 0.0F ? 0.0F : delta / max;
      target[2] = max;
      target[3] = alpha;
   }

   static int hsvToArgb(float hue, float saturation, float value, float alpha) {
      int sector = (int)(hue * 6.0F);
      float fraction = hue * 6.0F - sector;
      float p = value * (1.0F - saturation);
      float q = value * (1.0F - fraction * saturation);
      float t = value * (1.0F - (1.0F - fraction) * saturation);
      float red;
      float green;
      float blue;
      switch (Math.floorMod(sector, 6)) {
         case 0 -> {
            red = value;
            green = t;
            blue = p;
         }
         case 1 -> {
            red = q;
            green = value;
            blue = p;
         }
         case 2 -> {
            red = p;
            green = value;
            blue = t;
         }
         case 3 -> {
            red = p;
            green = q;
            blue = value;
         }
         case 4 -> {
            red = t;
            green = p;
            blue = value;
         }
         default -> {
            red = value;
            green = p;
            blue = q;
         }
      }

      return Math.round(alpha * GLOBAL_ALPHA_SCALE) << 24
         | Math.round(red * GLOBAL_ALPHA_SCALE) << 16
         | Math.round(green * GLOBAL_ALPHA_SCALE) << 8
         | Math.round(blue * GLOBAL_ALPHA_SCALE);
   }

   // ------------------------------------------------------------------------------------------
   // Records
   // ------------------------------------------------------------------------------------------

   private record Hit(float x, float y, float width, float height, int type, ClientModule module, ValueSetting<?> setting, int arg, boolean clipped) {
   }

   private static final class ModuleView {
      final ThirdGuiAnim enabled = new ThirdGuiAnim(0.0F, 16.0F);
      final ThirdGuiAnim collapse = new ThirdGuiAnim(1.0F, 12.0F);
      final ThirdGuiAnim hover = new ThirdGuiAnim(0.0F, 18.0F);
      boolean collapsed;
   }

   private static final class SettingView {
      final ThirdGuiAnim hover = new ThirdGuiAnim(0.0F, 18.0F);
      final ThirdGuiAnim toggle = new ThirdGuiAnim(0.0F, 16.0F);
      final ThirdGuiAnim slider = new ThirdGuiAnim(0.0F, 22.0F);
      final ThirdGuiAnim open = new ThirdGuiAnim(0.0F, 14.0F);
      final float[] hsba = {0.0F, 1.0F, 1.0F, 1.0F};
      boolean expanded;
      boolean hsbaReady;
   }

   private static final class Holder {
      private static final ThirdGuiScreen INSTANCE = new ThirdGuiScreen();
   }
}
