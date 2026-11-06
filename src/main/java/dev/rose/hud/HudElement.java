package dev.rose.hud;

import dev.rose.anim.Spring;
import dev.rose.gui.HudEditorScreen;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.NumberSetting;
import net.minecraft.client.Minecraft;


public abstract class HudElement extends Module {
   private static final String STYLE_KEYS = "rose.hud.style";
   private static final double MAX_OFFSET = 8192.0;
   private static final String PLACEMENT_KEYS = "rose.hud.placement";
   public static final float MIN_SCALE = 0.5F;
   public static final float MAX_SCALE = 3.0F;
   private final ChoiceSetting<Anchor> anchor;
   private final NumberSetting offsetX;
   private final NumberSetting offsetY;
   private ChoiceSetting<HudElement.Background> background;
   private ColorSetting backgroundColor;
   private NumberSetting roundness;
   private BoolSetting shadow;
   private final NumberSetting scale;
   final Spring presence = new Spring(0.0, 0.3, 0.84);
   final Spring animatedWidth = new Spring(0.0, 0.34, 0.9);
   final Spring animatedHeight = new Spring(0.0, 0.34, 0.9);
   final Spring animatedX = new Spring(0.0, 0.3, 0.88);
   final Spring animatedY = new Spring(0.0, 0.3, 0.88);
   boolean measured;
   boolean placed;
   final float[] bounds = new float[4];
   float contentWidth;
   float contentHeight;

   protected HudElement(String id, boolean enabledByDefault, Anchor defaultAnchor, float defaultX, float defaultY) {
      super(id, Category.HUD, enabledByDefault);
      this.section("placement_hud", 57633);
      this.add(new ActionSetting("edit_position", 57633, () -> HudEditorScreen.open(this)))
         .translatedAs("rose.hud.placement")
         .visibleWhen(() -> !(Minecraft.getInstance().screen instanceof HudEditorScreen));
      this.scale = this.add(new NumberSetting("scale", 100.0, 50.0, 300.0, 5.0, "rose.unit.percent")).translatedAs("rose.hud.style");
      this.add(new ActionSetting("reset_position", 57672, () -> {
         this.resetPlacement();
         this.setScale(1.0F);
      })).translatedAs("rose.hud.placement");
      this.anchor = this.add(new ChoiceSetting<>("anchor", defaultAnchor)).visibleWhen(() -> false);
      this.offsetX = this.add(new NumberSetting("x", defaultX, -8192.0, 8192.0, 0.5, "")).visibleWhen(() -> false);
      this.offsetY = this.add(new NumberSetting("y", defaultY, -8192.0, 8192.0, 0.5, "")).visibleWhen(() -> false);
      this.section("");
   }

   @Override
   protected void onRegistered() {
      this.section("style", 58087);
      this.background = this.add(new ChoiceSetting<>("background", this.defaultBackground())).translatedAs("rose.hud.style");
      this.backgroundColor = this.add(new ColorSetting("background_color", -1072425960, true)).translatedAs("rose.hud.style");
      this.backgroundColor.visibleWhen(() -> this.background.get() == HudElement.Background.SOLID);
      this.roundness = this.add(new NumberSetting("roundness", Math.round(this.defaultRoundness() * 100.0F), 0.0, 100.0, 5.0, "rose.unit.percent"))
         .translatedAs("rose.hud.style");
      this.roundness.visibleWhen(() -> this.background.get() != HudElement.Background.NONE);
      this.shadow = this.add(new BoolSetting("shadow", true)).translatedAs("rose.hud.style");
      this.shadow.visibleWhen(() -> this.background.get() != HudElement.Background.NONE);
   }

   protected abstract Size measure(HudContext var1);

   protected abstract void render(HudContext var1, float var2, float var3);

   protected boolean hasContent(HudContext hud) {
      return true;
   }

   protected final void sizeSpring(double response, double damping) {
      this.animatedWidth.configure(response, damping);
      this.animatedHeight.configure(response, damping);
   }

   protected boolean drawsBackground() {
      return true;
   }

   protected float defaultRoundness() {
      return 0.45F;
   }

   protected HudElement.Background defaultBackground() {
      return HudElement.Background.GLASS;
   }

   public final Anchor anchor() {
      return this.anchor.get();
   }

   public final float offsetX() {
      return this.offsetX.floatValue();
   }

   public final float offsetY() {
      return this.offsetY.floatValue();
   }

   public final void place(Anchor newAnchor, float x, float y) {
      this.anchor.set(newAnchor);
      this.offsetX.set((double)x);
      this.offsetY.set((double)y);
   }

   public final void resetPlacement() {
      this.anchor.reset();
      this.offsetX.reset();
      this.offsetY.reset();
   }

   public final HudElement.Background background() {
      return this.background == null ? HudElement.Background.GLASS : this.background.get();
   }

   public final int backgroundColor() {
      return this.backgroundColor == null ? -1072425960 : this.backgroundColor.color();
   }

   public final float roundness() {
      return this.roundness == null ? this.defaultRoundness() : this.roundness.floatValue() / 100.0F;
   }

   public final boolean shadow() {
      return this.shadow == null || this.shadow.isOn();
   }

   public final float scale() {
      return this.scale.floatValue() / 100.0F;
   }

   public final void setScale(float value) {
      this.scale.set((double)Math.round(Math.clamp(value, 0.5F, 3.0F) * 100.0F));
   }

   public final float[] bounds() {
      return this.bounds;
   }

   public enum Background {
      GLASS,
      SOLID,
      NONE;
   }
}
