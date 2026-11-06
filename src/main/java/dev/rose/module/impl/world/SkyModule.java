package dev.rose.module.impl.world;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.impl.PerformanceModule;
import dev.rose.render.Shaders;
import dev.rose.render.Theme;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.GpuDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.SkyTextures;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;


public final class SkyModule extends Module {
   private static final float R = 100.0F;
   private static final int SEGMENTS = 56;
   private static final int STARS = 460;
   private static final RoseShader COLOR = RoseShader.vanilla("sky_color", "position_color", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS);
   private static final Matrix4f IDENTITY = new Matrix4f();
   private final ChoiceSetting<SkyModule.Preset> preset;
   private final BoolSetting skyColor;
   private final ColorSetting sky;
   private final BoolSetting cloudColor;
   private final ColorSetting clouds;
   private final BoolSetting fogColor;
   private final ColorSetting fog;
   private final NumberSetting strength;
   private final NumberSetting fogDistance;
   private final NumberSetting darkness;
   private final NumberSetting nightGlow;
   private final BoolSetting aurora;
   private final ColorsSetting auroraColors;
   private final ChoiceSetting<SkyModule.When> auroraWhen;
   private final NumberSetting auroraIntensity;
   private final NumberSetting auroraBands;
   private final NumberSetting auroraHeight;
   private final NumberSetting auroraSpeed;
   private final BoolSetting rays;
   private final NumberSetting raysIntensity;
   private final BoolSetting softStars;
   private final NumberSetting starsBrightness;
   private final BoolSetting meteors;
   private final BoolSetting hideSun;
   private final BoolSetting hideMoon;
   private final BoolSetting hideClouds;
   private final BoolSetting cubes;
   private final NumberSetting cubesCount;
   private final NumberSetting cubesSize;
   private final BoolSetting nebula;
   private final ColorsSetting nebulaColors;
   private final NumberSetting nebulaDensity;
   private final NumberSetting nebulaScale;
   private final ChoiceSetting<SkyModule.When> nebulaWhen;
   private final BoolSetting blackHole;
   private final ColorsSetting blackHoleColors;
   private final NumberSetting blackHoleSize;
   private final NumberSetting blackHoleHeight;
   private final ChoiceSetting<SkyModule.When> blackHoleWhen;
   private final float[] starData = new float[5520];
   private boolean starsReady;
   private final Random random = new Random();
   private final Vector3f meteorFrom = new Vector3f();
   private final Vector3f meteorTo = new Vector3f();
   private float meteorStart = -1.0F;
   private float meteorNext = 3.0F;
   private static final Vector3f NEBULA_POLE = new Vector3f(0.42F, 0.8F, 0.43F).normalize();
   private static final float NEBULA_HALF = 0.62F;
   private static final int NEBULA_ALONG = 96;
   private static final int NEBULA_ACROSS = 10;
   private static final float HOLE_EXTENT = 6.0F;
   private static final float HOLE_SHADOW = 0.045F;
   private static final float HOLE_TILT = 0.155F;
   private static final int HOLE_CELLS = 10;

   public SkyModule() {
      super("sky", Category.WORLD, false);
      this.preset = this.add(new ChoiceSetting<SkyModule.Preset>("preset", SkyModule.Preset.CUSTOM) {
         @Override
         public JsonElement toJson() {
            return JsonNull.INSTANCE;
         }

         @Override
         public void fromJson(JsonElement json) {
         }

         @Override
         public boolean isDefault() {
            return true;
         }
      });
      this.section("colors", 57821);
      this.skyColor = this.add(new BoolSetting("sky_color", true));
      this.sky = this.add(new ColorSetting("sky", -8734465, false));
      this.cloudColor = this.add(new BoolSetting("cloud_color", false));
      this.clouds = this.add(new ColorSetting("clouds", -6416, false));
      this.fogColor = this.add(new BoolSetting("fog_color", true));
      this.fog = this.add(new ColorSetting("fog", -4663041, false));
      this.strength = this.add(new NumberSetting("strength", 70.0, 5.0, 100.0, 5.0, "rose.unit.percent"));
      this.fogDistance = this.add(new NumberSetting("fog_distance", 100.0, 50.0, 150.0, 5.0, "rose.unit.percent"));
      this.darkness = this.add(new NumberSetting("darkness", 0.0, 0.0, 80.0, 5.0, "rose.unit.percent"));
      this.nightGlow = this.add(new NumberSetting("night_glow", 35.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.section("aurora", 57987);
      this.aurora = this.add(new BoolSetting("aurora", true));
      this.auroraColors = this.add(new ColorsSetting("aurora_colors", ColorsSetting.Mode.GRADIENT, -7709441, -12785976));
      this.auroraWhen = this.add(new ChoiceSetting<>("aurora_when", SkyModule.When.NIGHT));
      this.auroraIntensity = this.add(new NumberSetting("aurora_intensity", 85.0, 10.0, 150.0, 5.0, "rose.unit.percent"));
      this.auroraBands = this.add(new NumberSetting("aurora_bands", 4.0, 1.0, 7.0, 1.0, ""));
      this.auroraHeight = this.add(new NumberSetting("aurora_height", 100.0, 40.0, 200.0, 5.0, "rose.unit.percent"));
      this.auroraSpeed = this.add(new NumberSetting("aurora_speed", 100.0, 0.0, 300.0, 10.0, "rose.unit.percent"));
      this.rays = this.add(new BoolSetting("rays", true));
      this.raysIntensity = this.add(new NumberSetting("rays_intensity", 55.0, 10.0, 150.0, 5.0, "rose.unit.percent"));
      this.section("stars", 57718);
      this.softStars = this.add(new BoolSetting("soft_stars", true));
      this.starsBrightness = this.add(new NumberSetting("stars_brightness", 130.0, 0.0, 400.0, 10.0, "rose.unit.percent"));
      this.meteors = this.add(new BoolSetting("meteors", true));
      this.hideSun = this.add(new BoolSetting("hide_sun", false));
      this.hideMoon = this.add(new BoolSetting("hide_moon", false));
      this.hideClouds = this.add(new BoolSetting("hide_clouds", false));
      this.section("cubes", 57441);
      this.cubes = this.add(new BoolSetting("cubes", false));
      this.cubesCount = this.add(new NumberSetting("cubes_count", 16.0, 4.0, 40.0, 1.0, ""));
      this.cubesSize = this.add(new NumberSetting("cubes_size", 100.0, 40.0, 250.0, 5.0, "rose.unit.percent"));
      this.section("nebula", 58386);
      this.nebula = this.add(new BoolSetting("nebula", false));
      this.nebulaColors = this.add(new ColorsSetting("nebula_colors", ColorsSetting.Mode.GRADIENT, -14009089, -2859777));
      this.nebulaDensity = this.add(new NumberSetting("nebula_density", 50.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.nebulaScale = this.add(new NumberSetting("nebula_scale", 100.0, 40.0, 250.0, 5.0, "rose.unit.percent"));
      this.nebulaWhen = this.add(new ChoiceSetting<>("nebula_when", SkyModule.When.NIGHT));
      this.section("black_hole", 57408);
      this.blackHole = this.add(new BoolSetting("black_hole", false));
      this.blackHoleColors = this.add(new ColorsSetting("black_hole_colors", ColorsSetting.Mode.GRADIENT, -16310, -46562));
      this.blackHoleSize = this.add(new NumberSetting("black_hole_size", 100.0, 40.0, 260.0, 5.0, "rose.unit.percent"));
      this.blackHoleHeight = this.add(new NumberSetting("black_hole_height", 30.0, 5.0, 80.0, 1.0, "rose.unit.degrees"));
      this.blackHoleWhen = this.add(new ChoiceSetting<>("black_hole_when", SkyModule.When.NIGHT));
      this.sky.visibleWhen(this.skyColor::isOn);
      this.clouds.visibleWhen(this.cloudColor::isOn);
      this.fog.visibleWhen(this.fogColor::isOn);
      this.auroraWhen.visibleWhen(this.aurora::isOn);
      this.auroraIntensity.visibleWhen(this.aurora::isOn);
      this.auroraBands.visibleWhen(this.aurora::isOn);
      this.auroraHeight.visibleWhen(this.aurora::isOn);
      this.auroraSpeed.visibleWhen(this.aurora::isOn);
      this.raysIntensity.visibleWhen(this.rays::isOn);
      this.cubesCount.visibleWhen(this.cubes::isOn);
      this.cubesSize.visibleWhen(this.cubes::isOn);
      this.nebulaColors.visibleWhen(this.nebula::isOn);
      this.nebulaDensity.visibleWhen(this.nebula::isOn);
      this.nebulaScale.visibleWhen(this.nebula::isOn);
      this.nebulaWhen.visibleWhen(this.nebula::isOn);
      this.blackHoleColors.visibleWhen(this.blackHole::isOn);
      this.blackHoleSize.visibleWhen(this.blackHole::isOn);
      this.blackHoleHeight.visibleWhen(this.blackHole::isOn);
      this.blackHoleWhen.visibleWhen(this.blackHole::isOn);
      this.preset.onChange(this::applyPreset);
   }

   @Override
   public int icon() {
      return 57878;
   }

   private void applyPreset() {
      switch ((SkyModule.Preset)this.preset.get()) {
         case POLAR:
            this.colors(-12965254, -14410674, 85, 0, 55);
            this.auroraSet(true, -7709441, -12785976, SkyModule.When.NIGHT, 95, 5);
            this.auroraHeight.set(100.0);
            this.extras(true, true, 150, true, false, false, false);
            break;
         case BLOOD:
            this.colors(-6678480, -11923944, 90, 0, 75);
            this.auroraSet(true, -53942, -30147, SkyModule.When.ALWAYS, 80, 4);
            this.auroraHeight.set(100.0);
            this.extras(false, true, 110, false, true, false, false);
            break;
         case SPACE:
            this.colors(-16118754, -16118758, 95, 20, 30);
            this.auroraSet(false, -12956417, -6660865, SkyModule.When.NIGHT, 35, 2);
            this.nebulaColors.set(new ColorsSetting.Value(ColorsSetting.Mode.GRADIENT, new int[]{-14009089, -2859777, -1}, 2, 1.0F));
            this.nebulaDensity.set(55.0);
            this.extras(false, true, 260, true, false, true, true);
      }
   }

   private void colors(int skyTint, int fogTint, int power, int dark, int glow) {
      this.nightGlow.set((double)glow);
      this.skyColor.set(true);
      this.sky.set(skyTint);
      this.fogColor.set(true);
      this.fog.set(fogTint);
      this.strength.set((double)power);
      this.darkness.set((double)dark);
   }

   private void auroraSet(boolean on, int first, int second, SkyModule.When when, int intensity, int bands) {
      this.aurora.set(on);
      this.auroraColors.set(new ColorsSetting.Value(ColorsSetting.Mode.GRADIENT, new int[]{first, second, -1}, 2, 1.0F));
      this.auroraWhen.set(when);
      this.auroraIntensity.set((double)intensity);
      this.auroraBands.set((double)bands);
   }

   private void extras(boolean lightRays, boolean soft, int starPower, boolean falling, boolean floatingCubes, boolean hole, boolean clouds) {
      this.hideClouds.set(true);
      this.rays.set(lightRays);
      this.softStars.set(soft);
      this.starsBrightness.set((double)starPower);
      this.meteors.set(falling);
      this.cubes.set(floatingCubes);
      this.blackHole.set(hole);
      this.nebula.set(clouds);
   }

   private Vec3 blend(Vec3 vanilla, int color) {
      float t = this.strength.floatValue() / 100.0F;
      return new Vec3(
         vanilla.x + (ColorUtil.red(color) / 255.0 - vanilla.x) * t,
         vanilla.y + (ColorUtil.green(color) / 255.0 - vanilla.y) * t,
         vanilla.z + (ColorUtil.blue(color) / 255.0 - vanilla.z) * t
      );
   }

   private float dim() {
      return 1.0F - this.darkness.floatValue() / 100.0F * 0.9F;
   }

   public int skyColor(int vanilla) {
      return this.isEnabled() ? toArgb(this.skyColor(fromArgb(vanilla)), vanilla) : vanilla;
   }

   public int cloudColor(int vanilla) {
      return this.isEnabled() && this.cloudColor.isOn() ? toArgb(this.cloudColor(fromArgb(vanilla)), vanilla) : vanilla;
   }

   private static Vec3 fromArgb(int argb) {
      return new Vec3(ColorUtil.red(argb) / 255.0, ColorUtil.green(argb) / 255.0, ColorUtil.blue(argb) / 255.0);
   }

   private static int toArgb(Vec3 color, int vanilla) {
      return ColorUtil.argb(ColorUtil.alpha(vanilla), channel(color.x), channel(color.y), channel(color.z));
   }

   private static int channel(double value) {
      return (int)Math.round(Math.clamp(value, 0.0, 1.0) * 255.0);
   }

   public Vec3 skyColor(Vec3 vanilla) {
      if (!this.isEnabled()) {
         return vanilla;
      }

      if (!this.skyColor.isOn()) {
         return vanilla.scale(this.dim());
      }

      double light = Math.max(vanilla.x, Math.max(vanilla.y, vanilla.z));
      Vec3 target = this.blend(vanilla, this.sky.color());
      double targetLight = Math.max(target.x, Math.max(target.y, target.z));
      Vec3 result = targetLight > 0.001 ? target.scale(Math.min(1.5, light / targetLight)) : target;
      double floor = this.nightGlow.floatValue() / 100.0 * 0.6 * this.strength.floatValue() / 100.0;
      int color = this.sky.color();
      result = new Vec3(
         Math.max(result.x, ColorUtil.red(color) / 255.0 * floor),
         Math.max(result.y, ColorUtil.green(color) / 255.0 * floor),
         Math.max(result.z, ColorUtil.blue(color) / 255.0 * floor)
      );
      return result.scale(this.dim());
   }

   public Vec3 cloudColor(Vec3 vanilla) {
      return this.isEnabled() && this.cloudColor.isOn() ? this.blend(vanilla, this.clouds.color()) : vanilla;
   }

   public boolean atmosphere(Camera camera) {
      return this.isEnabled() && camera.getFluidInCamera() == FogType.NONE
         ? !(camera.entity() instanceof LivingEntity living && (living.hasEffect(MobEffects.BLINDNESS) || living.hasEffect(MobEffects.DARKNESS)))
         : false;
   }

   public void fogColor(float[] rgb) {
      if (this.fogColor.isOn()) {
         float t = this.strength.floatValue() / 100.0F;
         int color = this.fog.color();
         float light = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));
         float r = ColorUtil.red(color) / 255.0F;
         float g = ColorUtil.green(color) / 255.0F;
         float b = ColorUtil.blue(color) / 255.0F;
         float targetLight = Math.max(r, Math.max(g, b));
         float scale = targetLight > 0.001F ? Math.min(1.5F, light / targetLight) : 1.0F;
         rgb[0] += (r * scale - rgb[0]) * t;
         rgb[1] += (g * scale - rgb[1]) * t;
         rgb[2] += (b * scale - rgb[2]) * t;
         float floor = this.nightGlow.floatValue() / 100.0F * 0.42F * t;
         rgb[0] = Math.max(rgb[0], r * floor);
         rgb[1] = Math.max(rgb[1], g * floor);
         rgb[2] = Math.max(rgb[2], b * floor);
      }

      float dim = this.dim();
      rgb[0] *= dim;
      rgb[1] *= dim;
      rgb[2] *= dim;
   }

   public float fogDistance() {
      return this.fogDistance.floatValue() / 100.0F;
   }

   public float starBrightness(float vanilla) {
      if (!this.isEnabled()) {
         return vanilla;
      } else {
         return this.softStars.isOn() ? 0.0F : vanilla * this.starsBrightness.floatValue() / 100.0F;
      }
   }

   public boolean hideSun() {
      return this.isEnabled() && this.hideSun.isOn();
   }

   public boolean hideClouds() {
      return this.isEnabled() && this.hideClouds.isOn();
   }

   public boolean hideMoon() {
      return this.isEnabled() && this.hideMoon.isOn();
   }

   public void renderOverlay(Matrix4f cameraRotation, float sunAngle, float starAngle, float rainBrightness, float vanillaStars) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel level = mc.level;
      if (level != null && this.atmosphere(mc.gameRenderer.getMainCamera())) {
         float time = Theme.time();
         float starLight = Math.clamp(vanillaStars * 2.2F, 0.0F, 1.0F);
         float clear = rainBrightness;
         float night = this.auroraWhen.get() == SkyModule.When.ALWAYS ? 1.0F : starLight;
         Matrix4f matrix = cameraRotation;
         if (this.cubes.isOn()) {
            this.drawCubes(matrix, time, clear);
         }

         float nebulaVisible = (this.nebulaWhen.get() == SkyModule.When.ALWAYS ? 1.0F : starLight) * clear;
         if (this.nebula.isOn() && nebulaVisible > 0.01F) {
            this.drawNebula(matrix, time, nebulaVisible);
         }

         float starPower = starLight * clear * this.starsBrightness.floatValue() / 100.0F;
         if (this.softStars.isOn() && starPower > 0.01F) {
            this.drawStars(new Matrix4f(matrix).rotateY((float) (-Math.PI / 2)).rotateX(starAngle), time, starPower);
         }

         float holeVisible = (this.blackHoleWhen.get() == SkyModule.When.ALWAYS ? 1.0F : starLight) * clear;
         if (this.blackHole.isOn() && holeVisible > 0.01F) {
            this.drawBlackHole(matrix, time, holeVisible);
         }

         boolean lightRays = this.rays.isOn();
         boolean falling = this.meteors.isOn() && starLight * clear > 0.3F;
         if (lightRays || falling) {
            BufferBuilder builder = GpuDraw.begin(COLOR);
            if (lightRays) {
               this.drawRays(builder, matrix, sunAngle, time, clear);
            }

            if (falling) {
               this.drawMeteor(builder, matrix, time, starLight * clear);
            }

            sky(COLOR, Blend.OVERLAY, builder);
         }

         if (this.aurora.isOn() && night * clear > 0.01F) {
            this.drawAurora(matrix, time, night * clear);
         }
      }
   }

   private static void sky(RoseShader shader, Blend blend, BufferBuilder builder) {
      MeshData mesh = builder.build();
      if (mesh != null) {
         MeshData var4 = mesh;

         try {
            GpuDraw.draw(shader, blend, RoseShader.Depth.NONE, mesh, Minecraft.getInstance().getMainRenderTarget().getColorTextureView(), null, IDENTITY);
         } catch (Throwable var8) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (var4 != null) {
            var4.close();
         }
      }
   }

   private static void bindSky(RoseShader shader, Identifier texture) {
      AbstractTexture gpu = Minecraft.getInstance().getTextureManager().getTexture(texture);
      shader.setSampler("Sampler0", gpu.getTextureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR));
   }

   private static int withAlpha(int rgb, float alpha) {
      return ColorUtil.withAlpha(rgb, Math.clamp(Math.round(alpha * 255.0F), 0, 255));
   }

   private void drawAurora(Matrix4f matrix, float time, float visible) {
      RoseShader shader = Shaders.softSprite();
      bindSky(shader, SkyTextures.aurora());
      BufferBuilder builder = GpuDraw.begin(shader);
      int bands = this.auroraBands.intValue();
      float power = this.auroraIntensity.floatValue() / 100.0F * visible;
      float pace = this.auroraSpeed.floatValue() / 100.0F;
      float tall = this.auroraHeight.floatValue() / 100.0F;
      float t = time * pace;

      for (int layer = 0; layer < 2; layer++) {
         float layerHeight = layer == 0 ? 1.0F : 1.6F;
         float layerAlpha = layer == 0 ? 0.85F : 0.36F;
         float density = layer == 0 ? 0.5F : 0.17F;

         for (int band = 0; band < bands; band++) {
            float center = band * ((float) (Math.PI * 2) / bands) * 0.93F + 0.6F + Mth.sin(t * 0.021F + band) * 0.25F;
            float span = 2.5F + 0.5F * Mth.sin(band * 2.3F);
            float baseElevation = 0.27F + 0.13F * (band * 3 % 4) + 0.04F * Mth.sin(t * 0.05F + band * 1.9F);
            float previousX = 0.0F;
            float previousY = 0.0F;
            float previousZ = 0.0F;
            float previousTopX = 0.0F;
            float previousTopY = 0.0F;
            float previousTopZ = 0.0F;
            float previousU = 0.0F;
            int previousBottom = 0;
            int previousTop = 0;

            for (int i = 0; i <= 56; i++) {
               float along = i / 56.0F;
               float theta = center - span / 2.0F + span * along;
               float elevation = baseElevation
                  + 0.075F * Mth.sin(theta * 2.3F + t * 0.11F + band * 1.7F)
                  + 0.04F * Mth.sin(theta * 5.1F - t * 0.17F + band)
                  + 0.022F * Mth.sin(theta * 9.7F + t * 0.29F + band * 2.7F);
               float azimuth = theta
                  + 0.1F * Mth.sin(theta * 3.7F + t * 0.13F + band * 2.1F)
                  + 0.045F * Mth.sin(theta * 8.9F - t * 0.21F + band);
               float height = 0.52F * tall * layerHeight * (0.85F + 0.25F * Mth.sin(theta * 1.9F + t * 0.07F + band));
               float top = Math.min(elevation + height, 1.5F);
               float cosE = Mth.cos(elevation);
               float cosT = Mth.cos(top);
               float x = cosE * Mth.cos(azimuth) * 100.0F;
               float y = Mth.sin(elevation) * 100.0F;
               float z = cosE * Mth.sin(azimuth) * 100.0F;
               float topX = cosT * Mth.cos(azimuth) * 100.0F;
               float topY = Mth.sin(top) * 100.0F;
               float topZ = cosT * Mth.sin(azimuth) * 100.0F;
               float ends = (float)Math.pow(Mth.sin((float) Math.PI * along), 0.7);
               float shimmer = 0.62F
                  + 0.38F * Mth.sin(theta * 4.0F + t * 0.5F + band * 1.3F) * Mth.sin(theta * 1.3F - t * 0.23F);
               float alpha = power * layerAlpha * ends * shimmer;
               float position = along * 0.55F + band * 0.19F;
               int bottomColor = withAlpha(ColorUtil.lerp(this.auroraColors.color(position, time), -1, layer == 0 ? 0.22F : 0.0F), alpha);
               int topColor = withAlpha(this.auroraColors.color(position + 0.4F, time), alpha);
               float u = theta * density + t * 0.006F * (band % 2 == 0 ? 1 : -1) + band * 0.37F + layer * 0.5F;
               if (i > 0) {
                  builder.addVertex(matrix, previousX, previousY, previousZ).setUv(previousU, 1.0F).setColor(previousBottom);
                  builder.addVertex(matrix, x, y, z).setUv(u, 1.0F).setColor(bottomColor);
                  builder.addVertex(matrix, topX, topY, topZ).setUv(u, 0.0F).setColor(topColor);
                  builder.addVertex(matrix, previousTopX, previousTopY, previousTopZ).setUv(previousU, 0.0F).setColor(previousTop);
               }

               previousX = x;
               previousY = y;
               previousZ = z;
               previousTopX = topX;
               previousTopY = topY;
               previousTopZ = topZ;
               previousU = u;
               previousBottom = bottomColor;
               previousTop = topColor;
            }
         }
      }

      sky(shader, Blend.OVERLAY, builder);
   }

   private void drawNebula(Matrix4f matrix, float time, float visible) {
      RoseShader shader = Shaders.nebula();
      int deep = this.nebulaColors.color(0.05F, time);
      int warm = this.nebulaColors.color(0.9F, time);
      shader.safeGetUniform("Time").set(time);
      shader.safeGetUniform("Strength").set(visible);
      shader.safeGetUniform("Density").set(this.nebulaDensity.floatValue() / 100.0F);
      shader.safeGetUniform("Scale").set(220.0F / Math.max(40.0F, this.nebulaScale.floatValue()));
      shader.safeGetUniform("DeepColor").set(ColorUtil.red(deep) / 255.0F, ColorUtil.green(deep) / 255.0F, ColorUtil.blue(deep) / 255.0F, 1.0F);
      shader.safeGetUniform("WarmColor").set(ColorUtil.red(warm) / 255.0F, ColorUtil.green(warm) / 255.0F, ColorUtil.blue(warm) / 255.0F, 1.0F);
      Vector3f pole = new Vector3f(NEBULA_POLE);
      Vector3f a = new Vector3f(0.0F, 1.0F, 0.0F).cross(pole, new Vector3f()).normalize();
      Vector3f b = new Vector3f(pole).cross(a, new Vector3f()).normalize();
      BufferBuilder builder = GpuDraw.begin(shader);
      Vector3f scratch = new Vector3f();

      for (int i = 0; i < 96; i++) {
         for (int j = 0; j < 10; j++) {
            float u0 = i / 96.0F;
            float u1 = (i + 1) / 96.0F;
            float v0 = j / 10.0F;
            float v1 = (j + 1) / 10.0F;
            nebulaVertex(builder, matrix, a, b, pole, u0, v0, scratch);
            nebulaVertex(builder, matrix, a, b, pole, u1, v0, scratch);
            nebulaVertex(builder, matrix, a, b, pole, u1, v1, scratch);
            nebulaVertex(builder, matrix, a, b, pole, u0, v1, scratch);
         }
      }

      sky(shader, Blend.PREMULTIPLIED, builder);
   }

   private static void nebulaVertex(BufferBuilder builder, Matrix4f matrix, Vector3f a, Vector3f b, Vector3f pole, float u, float v, Vector3f scratch) {
      float phi = u * (float) (Math.PI * 2);
      float psi = (v * 2.0F - 1.0F) * 0.62F;
      float across = Mth.cos(psi);
      scratch.set(a)
         .mul(Mth.cos(phi) * across)
         .fma(Mth.sin(phi) * across, b)
         .fma(Mth.sin(psi), pole)
         .normalize()
         .mul(100.0F);
      builder.addVertex(matrix, scratch.x, scratch.y, scratch.z).setUv(u, v).setColor(-1);
   }

   private void drawBlackHole(Matrix4f matrix, float time, float visible) {
      RoseShader shader = Shaders.blackHole();
      float shadow = 0.045F * this.blackHoleSize.floatValue() / 100.0F;
      float elevation = this.blackHoleHeight.floatValue() * (float) (Math.PI / 180.0);
      float azimuth = 0.9F + time * 0.0012F;
      float cosE = Mth.cos(elevation);
      Vector3f center = new Vector3f(cosE * Mth.cos(azimuth), Mth.sin(elevation), cosE * Mth.sin(azimuth));
      Vector3f right = new Vector3f(0.0F, 1.0F, 0.0F).cross(center, new Vector3f()).normalize();
      Vector3f top = new Vector3f(center).cross(right, new Vector3f()).normalize();
      int hot = this.blackHoleColors.color(0.1F, time);
      int cool = this.blackHoleColors.color(0.85F, time);
      shader.safeGetUniform("Extent").set(6.0F);
      shader.safeGetUniform("Inclination").set(0.155F);
      shader.safeGetUniform("Time").set(time);
      shader.safeGetUniform("Strength").set(visible);
      shader.safeGetUniform("Steps").set(PerformanceModule.lite() ? 75.0F : 165.0F);
      shader.safeGetUniform("StarPower").set(Math.clamp(this.starsBrightness.floatValue() / 130.0F, 0.0F, 2.0F) * 0.7F);
      shader.safeGetUniform("HotColor").set(ColorUtil.red(hot) / 255.0F, ColorUtil.green(hot) / 255.0F, ColorUtil.blue(hot) / 255.0F, 1.0F);
      shader.safeGetUniform("CoolColor").set(ColorUtil.red(cool) / 255.0F, ColorUtil.green(cool) / 255.0F, ColorUtil.blue(cool) / 255.0F, 1.0F);
      BufferBuilder builder = GpuDraw.begin(shader);
      Vector3f corner = new Vector3f();

      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            float u0 = i / 10.0F;
            float u1 = (i + 1) / 10.0F;
            float v0 = j / 10.0F;
            float v1 = (j + 1) / 10.0F;
            holeVertex(builder, matrix, center, right, top, shadow, u0, v0, corner);
            holeVertex(builder, matrix, center, right, top, shadow, u1, v0, corner);
            holeVertex(builder, matrix, center, right, top, shadow, u1, v1, corner);
            holeVertex(builder, matrix, center, right, top, shadow, u0, v1, corner);
         }
      }

      sky(shader, Blend.PREMULTIPLIED, builder);
   }

   private static void holeVertex(
      BufferBuilder builder, Matrix4f matrix, Vector3f center, Vector3f right, Vector3f top, float shadow, float u, float v, Vector3f scratch
   ) {
      float offset = 6.0F * shadow;
      at(center, right, top, (u * 2.0F - 1.0F) * offset, (v * 2.0F - 1.0F) * offset, scratch);
      builder.addVertex(matrix, scratch.x, scratch.y, scratch.z).setUv(u, v).setColor(-1);
   }

   private static Vector3f at(Vector3f center, Vector3f right, Vector3f top, float u, float v, Vector3f dest) {
      return dest.set(center).fma(u, right).fma(v, top).normalize().mul(100.0F);
   }

   private void drawRays(BufferBuilder builder, Matrix4f matrix, float angle, float time, float clear) {
      Vector3f light = new Vector3f(-Mth.sin(angle), Mth.cos(angle), 0.0F);
      if (light.y < 0.0F) {
         light.negate();
      }

      if (!(light.y < 0.12F)) {
         float rise = Math.clamp((light.y - 0.12F) / 0.25F, 0.0F, 1.0F);
         float power = this.raysIntensity.floatValue() / 100.0F * clear * rise * 0.5F;
         Vector3f side = new Vector3f(0.0F, 0.0F, 1.0F);
         Vector3f across = new Vector3f(light).cross(side).normalize();
         side = new Vector3f(across).cross(light).normalize();
         int count = 11;

         for (int i = 0; i < count; i++) {
            float around = i * ((float) (Math.PI * 2) / count) + time * 0.012F + Mth.sin(i * 12.9F) * 0.22F;
            float length = (0.75F + 0.55F * (0.5F + 0.5F * Mth.sin(i * 7.3F)))
               * (0.8F + 0.2F * Mth.sin(time * 0.23F + i * 1.7F));
            float halfWidth = 0.07F + 0.07F * (0.5F + 0.5F * Mth.sin(i * 4.1F + 1.0F));
            float alpha = power * (0.55F + 0.45F * Mth.sin(time * 0.31F + i * 2.4F));
            int rgb = ColorUtil.lerp(this.auroraColors.color((float)i / count, time), -1, 0.72F);
            int bright = withAlpha(rgb, alpha);
            int clearColor = rgb & 16777215;
            int soft = withAlpha(rgb, alpha * 0.42F);
            float middle = length * 0.5F;
            Vector3f start = direction(light, across, side, around, 0.045F);
            Vector3f half = direction(light, across, side, around, middle);
            Vector3f end = direction(light, across, side, around, length);
            Vector3f startLeft = direction(light, across, side, around - 0.3F, 0.045F);
            Vector3f startRight = direction(light, across, side, around + 0.3F, 0.045F);
            Vector3f halfLeft = direction(light, across, side, around - halfWidth * 0.7F, middle);
            Vector3f halfRight = direction(light, across, side, around + halfWidth * 0.7F, middle);
            Vector3f endLeft = direction(light, across, side, around - halfWidth, length);
            Vector3f endRight = direction(light, across, side, around + halfWidth, length);
            quad(builder, matrix, startLeft, start, half, halfLeft, clearColor, bright, soft, clearColor);
            quad(builder, matrix, start, startRight, halfRight, half, bright, clearColor, clearColor, soft);
            quad(builder, matrix, halfLeft, half, end, endLeft, clearColor, soft, clearColor, clearColor);
            quad(builder, matrix, half, halfRight, endRight, end, soft, clearColor, clearColor, clearColor);
         }
      }
   }

   private static Vector3f direction(Vector3f axis, Vector3f across, Vector3f side, float around, float away) {
      float sin = Mth.sin(away);
      float cos = Mth.cos(away);
      float ca = Mth.cos(around);
      float sa = Mth.sin(around);
      return new Vector3f(
            axis.x * cos + (across.x * ca + side.x * sa) * sin,
            axis.y * cos + (across.y * ca + side.y * sa) * sin,
            axis.z * cos + (across.z * ca + side.z * sa) * sin
         )
         .mul(100.0F);
   }

   private static void quad(BufferBuilder builder, Matrix4f matrix, Vector3f a, Vector3f b, Vector3f c, Vector3f d, int colorA, int colorB, int colorC, int colorD) {
      builder.addVertex(matrix, a.x, a.y, a.z).setColor(colorA);
      builder.addVertex(matrix, b.x, b.y, b.z).setColor(colorB);
      builder.addVertex(matrix, c.x, c.y, c.z).setColor(colorC);
      builder.addVertex(matrix, d.x, d.y, d.z).setColor(colorD);
   }

   private void prepareStars() {
      Random seeded = new Random(20260918L);

      for (int i = 0; i < 460; i++) {
         Vector3f dir;
         do {
            dir = new Vector3f(seeded.nextFloat() * 2.0F - 1.0F, seeded.nextFloat() * 2.0F - 1.0F, seeded.nextFloat() * 2.0F - 1.0F);
         } while (dir.lengthSquared() > 1.0F || dir.lengthSquared() < 0.01F);

         dir.normalize();
         Vector3f helper = Math.abs(dir.y) > 0.9F ? new Vector3f(1.0F, 0.0F, 0.0F) : new Vector3f(0.0F, 1.0F, 0.0F);
         Vector3f first = new Vector3f(dir).cross(helper).normalize();
         Vector3f second = new Vector3f(dir).cross(first).normalize();
         int o = i * 12;
         this.starData[o] = dir.x;
         this.starData[o + 1] = dir.y;
         this.starData[o + 2] = dir.z;
         this.starData[o + 3] = first.x;
         this.starData[o + 4] = first.y;
         this.starData[o + 5] = first.z;
         this.starData[o + 6] = second.x;
         this.starData[o + 7] = second.y;
         this.starData[o + 8] = second.z;
         float big = seeded.nextFloat();
         this.starData[o + 9] = big > 0.92F ? 1.3F + seeded.nextFloat() * 0.9F : 0.35F + seeded.nextFloat() * 0.55F;
         this.starData[o + 10] = seeded.nextFloat() * (float) (Math.PI * 2);
         this.starData[o + 11] = seeded.nextFloat();
      }

      this.starsReady = true;
   }

   private void drawStars(Matrix4f matrix, float time, float power) {
      if (!this.starsReady) {
         this.prepareStars();
      }

      RoseShader shader = Shaders.softSprite();
      bindSky(shader, CosmeticTextures.dot());
      BufferBuilder builder = GpuDraw.begin(shader);
      float base = Math.min(1.0F, power);
      Vector3f center = new Vector3f();

      for (int i = 0; i < 460; i++) {
         int o = i * 12;
         float size = this.starData[o + 9] * (power > 1.0F ? 1.0F + (power - 1.0F) * 0.25F : 1.0F);
         float phase = this.starData[o + 10];
         float tone = this.starData[o + 11];
         float twinkle = 0.62F + 0.38F * Mth.sin(time * (0.9F + 2.6F * tone) + phase);
         int rgb = tone > 0.72F ? ColorUtil.lerp(this.auroraColors.color(tone, time), -1, 0.55F) : -1;
         int color = withAlpha(rgb, base * twinkle * (size > 1.2F ? 1.0F : 0.85F));
         center.set(this.starData[o] * 100.0F, this.starData[o + 1] * 100.0F, this.starData[o + 2] * 100.0F);
         matrix.transformPosition(center);
         builder.addVertex(center.x - size, center.y - size, center.z).setUv(0.0F, 0.0F).setColor(color);
         builder.addVertex(center.x + size, center.y - size, center.z).setUv(1.0F, 0.0F).setColor(color);
         builder.addVertex(center.x + size, center.y + size, center.z).setUv(1.0F, 1.0F).setColor(color);
         builder.addVertex(center.x - size, center.y + size, center.z).setUv(0.0F, 1.0F).setColor(color);
      }

      sky(shader, Blend.OVERLAY, builder);
   }

   private void drawMeteor(BufferBuilder builder, Matrix4f matrix, float time, float power) {
      if (this.meteorStart < 0.0F) {
         if (time < this.meteorNext) {
            return;
         }

         float around = this.random.nextFloat() * (float) (Math.PI * 2);
         float elevation = 0.45F + this.random.nextFloat() * 0.75F;
         this.meteorFrom
            .set(
               Mth.cos(elevation) * Mth.cos(around),
               Mth.sin(elevation),
               Mth.cos(elevation) * Mth.sin(around)
            );
         float turn = around + (this.random.nextBoolean() ? 1 : -1) * (0.35F + this.random.nextFloat() * 0.35F);
         float lower = Math.max(0.12F, elevation - 0.3F - this.random.nextFloat() * 0.25F);
         this.meteorTo
            .set(
               Mth.cos(lower) * Mth.cos(turn),
               Mth.sin(lower),
               Mth.cos(lower) * Mth.sin(turn)
            );
         this.meteorStart = time;
      }

      float duration = 0.9F;
      float progress = (time - this.meteorStart) / duration;
      if (progress >= 1.0F) {
         this.meteorStart = -1.0F;
         this.meteorNext = time + 4.0F + this.random.nextFloat() * 9.0F;
      } else {
         float head = progress * progress * (3.0F - 2.0F * progress);
         float glow = Mth.sin((float) Math.PI * progress) * power;
         int parts = 10;
         Vector3f travel = new Vector3f(this.meteorTo).sub(this.meteorFrom).normalize();
         Vector3f previous = null;
         float previousWidth = 0.0F;
         int previousColor = 0;

         for (int i = 0; i <= parts; i++) {
            float back = (float)i / parts;
            float at = Math.max(0.0F, head - back * 0.16F);
            Vector3f point = new Vector3f(this.meteorFrom).lerp(this.meteorTo, at).normalize().mul(100.0F);
            float width = 0.42F * (1.0F - back);
            int color = withAlpha(16777215, glow * (1.0F - back) * (1.0F - back));
            if (previous != null) {
               Vector3f across = new Vector3f(point).cross(travel).normalize();
               Vector3f a = new Vector3f(previous).sub(new Vector3f(across).mul(previousWidth));
               Vector3f b = new Vector3f(previous).add(new Vector3f(across).mul(previousWidth));
               Vector3f c = new Vector3f(point).add(new Vector3f(across).mul(width));
               Vector3f d = new Vector3f(point).sub(new Vector3f(across).mul(width));
               quad(builder, matrix, a, b, c, d, previousColor, previousColor, color, color);
            }

            previous = point;
            previousWidth = width;
            previousColor = color;
         }
      }
   }

   private void drawCubes(Matrix4f matrix, float time, float clear) {
      BufferBuilder builder = GpuDraw.begin(COLOR);
      int count = this.cubesCount.intValue();
      float scale = this.cubesSize.floatValue() / 100.0F;
      Vector3f lamp = new Vector3f(0.35F, 0.9F, 0.25F).normalize();
      int[][] faces = new int[][]{{0, 1, 3, 2}, {4, 6, 7, 5}, {0, 4, 5, 1}, {2, 3, 7, 6}, {0, 2, 6, 4}, {1, 5, 7, 3}};
      float[][] normals = new float[][]{
         {0.0F, 0.0F, -1.0F}, {0.0F, 0.0F, 1.0F}, {0.0F, -1.0F, 0.0F}, {0.0F, 1.0F, 0.0F}, {-1.0F, 0.0F, 0.0F}, {1.0F, 0.0F, 0.0F}
      };
      Vector3f[] corners = new Vector3f[8];

      for (int i = 0; i < count; i++) {
         float seed = i * 12.9898F;
         float around = frac(Mth.sin(seed) * 43758.547F) * (float) (Math.PI * 2) + time * 0.006F * (i % 2 == 0 ? 1.0F : -0.7F);
         float elevation = 0.1F + frac(Mth.sin(seed * 1.7F) * 24634.63F) * 0.75F + 0.015F * Mth.sin(time * 0.21F + i);
         float distance = 62.0F + frac(Mth.sin(seed * 2.3F) * 9375.31F) * 30.0F;
         float half = (1.1F + frac(Mth.sin(seed * 3.1F) * 5417.17F) * 3.2F) * scale;
         Vector3f center = new Vector3f(
               Mth.cos(elevation) * Mth.cos(around),
               Mth.sin(elevation),
               Mth.cos(elevation) * Mth.sin(around)
            )
            .mul(distance);
         Matrix4f turn = new Matrix4f().rotateY(time * 0.05F * (1 + i % 3) + i).rotateX(time * 0.035F * (1 + i % 2) + i * 0.7F).rotateZ(i * 1.3F);

         for (int corner = 0; corner < 8; corner++) {
            corners[corner] = turn.transformDirection(
                  new Vector3f((corner & 1) == 0 ? -half : half, (corner & 2) == 0 ? -half : half, (corner & 4) == 0 ? -half : half)
               )
               .add(center);
         }

         int tint = this.auroraColors.color((float)i / count, time);
         float horizon = Math.clamp((elevation - 0.06F) / 0.12F, 0.0F, 1.0F);

         for (int face = 0; face < 6; face++) {
            Vector3f normal = turn.transformDirection(new Vector3f(normals[face][0], normals[face][1], normals[face][2]));
            int[] index = faces[face];
            Vector3f middle = new Vector3f(corners[index[0]]).add(corners[index[2]]).mul(0.5F);
            if (!(normal.dot(middle) >= 0.0F)) {
               float lit = 0.16F + 0.84F * (float)Math.pow(Math.max(0.0F, normal.dot(lamp) * 0.5F + 0.5F), 1.6);
               int color = withAlpha(ColorUtil.lerp(-16777216, tint, lit), 0.88F * clear * horizon);
               quad(builder, matrix, corners[index[0]], corners[index[1]], corners[index[2]], corners[index[3]], color, color, color, color);
            }
         }
      }

      sky(COLOR, Blend.NORMAL, builder);
   }

   private static float frac(float value) {
      return value - (float)Math.floor(value);
   }

   public enum Preset {
      CUSTOM,
      POLAR,
      BLOOD,
      SPACE;
   }

   public enum When {
      NIGHT,
      ALWAYS;
   }
}
