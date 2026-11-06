package dev.rose.module.impl.cosmetic;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.impl.PerformanceModule;
import dev.rose.render.Theme;
import dev.rose.render.world.life.Breath;
import dev.rose.render.world.life.Noise;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import org.joml.Vector3f;


public abstract class CosmeticModule extends Module {
   private static final float FLOW_SCALE = 0.55F;
   private static final double FLOW_PERIOD = 1024.0;
   private static final float FLOW_GAIN = 0.35F;
   private NumberSetting life;
   private ColorsSetting colors;
   private NumberSetting opacity;

   protected CosmeticModule(String id) {
      this(id, Category.COSMETICS);
   }

   protected CosmeticModule(String id, Category category) {
      this(id, category, false);
   }

   protected CosmeticModule(String id, Category category, boolean enabledByDefault) {
      super(id, category, enabledByDefault);
   }

   @Override
   protected void onRegistered() {
      if (this.alive()) {
         this.section("life", 57776);
         this.life = this.add(new NumberSetting("life", this.defaultLife(), 0.0, 100.0, 5.0, "rose.unit.percent")).translatedAs("rose.cosmetic");
      }

      this.section("color", 57821);
      this.colors = this.add(new ColorsSetting("colors", this.defaultMode(), this.defaultColor(), this.defaultSecondColor())).translatedAs("rose.cosmetic");
      this.opacity = this.add(new NumberSetting("opacity", 100.0, 10.0, 100.0, 5.0, "rose.unit.percent")).translatedAs("rose.cosmetic");
   }

   protected boolean alive() {
      return false;
   }

   protected double defaultLife() {
      return 70.0;
   }

   public float life() {
      return this.life == null ? 0.0F : this.life.floatValue() / 100.0F;
   }

   public float breath(float time, float seed, float amount) {
      float strength = this.life();
      return strength <= 0.0F ? 1.0F : Breath.scale(time, seed, amount * strength);
   }

   public float wobble(float time, float seed, float amount) {
      float strength = this.life();
      return strength <= 0.0F ? 0.0F : Breath.at(time, seed) * amount * strength;
   }

   public Vector3f flow(double x, double y, double z, float time, Vector3f out) {
      float strength = this.life();
      if (strength <= 0.0F) {
         return out.set(0.0F, 0.0F, 0.0F);
      }

      Noise.flow(wrap(x) + time * 0.13F, wrap(y) + time * 0.21F, wrap(z) + time * 0.17F, PerformanceModule.lite(), out);
      out.mul(0.35F);
      float length = out.length();
      if (length > 1.0F) {
         out.div(length);
      }

      return out.mul(strength);
   }

   private static float wrap(double value) {
      double wrapped = value * 0.55F % 1024.0;
      return (float)(wrapped < 0.0 ? wrapped + 1024.0 : wrapped);
   }

   protected ColorsSetting.Mode defaultMode() {
      return ColorsSetting.Mode.THEME;
   }

   protected int defaultColor() {
      return -10169601;
   }

   protected int defaultSecondColor() {
      return -1;
   }

   public int rgb(float position, float time) {
      return this.colors == null ? Theme.accent() : this.colors.color(position, time);
   }

   public int color(float position, float time, float strength) {
      return ColorUtil.withAlpha(this.rgb(position, time), Math.clamp(Math.round(255.0F * strength * this.opacity()), 0, 255));
   }

   protected ColorsSetting.Mode colorMode() {
      return this.colors == null ? ColorsSetting.Mode.THEME : this.colors.get().mode();
   }

   public float opacity() {
      return this.opacity == null ? 1.0F : this.opacity.floatValue() / 100.0F;
   }

   public int primary() {
      return this.rgb(0.0F, Theme.time());
   }

   public int secondary() {
      return this.rgb(0.5F, Theme.time());
   }
}
