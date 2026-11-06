package dev.rose.render.style;

import dev.rose.util.ColorUtil;


public final class Glass {
   public float blur = 0.8F;
   public float refraction = 7.0F;
   public float bevel = 10.0F;
   public float curve = 2.0F;
   public float magnify = 1.0F;
   public float dispersion = 0.2F;
   public float specular = 0.4F;
   public float sharpness = 5.0F;
   public int specularColor = -1;
   public float rim = 0.25F;
   public float rimWidth = 1.5F;
   public float fresnel = 0.0F;
   public float saturation = 1.25F;
   public float brightness = 0.0F;
   public float contrast = 1.0F;
   public float grain = 0.0F;
   public float wobble = 0.0F;
   public float wobbleScale = 1.0F;
   public float wobbleSpeed = 1.0F;
   public float depth = 0.08F;
   public Paint tint = Paint.solid(-2145776878);
   public int label = -1;
   public boolean lightFollowsMouse = false;

   public static Glass clear() {
      Glass glass = new Glass();
      glass.blur = 0.05F;
      glass.refraction = 9.0F;
      glass.dispersion = 0.35F;
      glass.specular = 0.6F;
      glass.rim = 0.4F;
      glass.saturation = 1.4F;
      glass.depth = 0.05F;
      glass.tint = Paint.solid(520093696);
      return glass;
   }

   public static Glass light() {
      Glass glass = new Glass();
      glass.blur = 0.85F;
      glass.refraction = 6.0F;
      glass.specular = 0.45F;
      glass.rim = 0.35F;
      glass.tint = Paint.solid(1945301751);
      glass.label = -14935010;
      return glass;
   }

   public static Glass dark() {
      Glass glass = new Glass();
      glass.blur = 0.8F;
      glass.refraction = 6.0F;
      glass.specular = 0.35F;
      glass.rim = 0.22F;
      glass.tint = Paint.solid(-2145776878);
      return glass;
   }

   public static Glass lens() {
      Glass glass = new Glass();
      glass.blur = 0.0F;
      glass.refraction = 5.0F;
      glass.bevel = 8.0F;
      glass.dispersion = 0.28F;
      glass.specular = 0.9F;
      glass.rim = 0.55F;
      glass.saturation = 1.15F;
      glass.tint = Paint.solid(184549375);
      return glass;
   }

   public Glass copy() {
      Glass copy = new Glass();
      copy.blur = this.blur;
      copy.refraction = this.refraction;
      copy.bevel = this.bevel;
      copy.curve = this.curve;
      copy.magnify = this.magnify;
      copy.dispersion = this.dispersion;
      copy.specular = this.specular;
      copy.sharpness = this.sharpness;
      copy.specularColor = this.specularColor;
      copy.rim = this.rim;
      copy.rimWidth = this.rimWidth;
      copy.fresnel = this.fresnel;
      copy.saturation = this.saturation;
      copy.brightness = this.brightness;
      copy.contrast = this.contrast;
      copy.grain = this.grain;
      copy.wobble = this.wobble;
      copy.wobbleScale = this.wobbleScale;
      copy.wobbleSpeed = this.wobbleSpeed;
      copy.depth = this.depth;
      copy.tint = this.tint;
      copy.label = this.label;
      copy.lightFollowsMouse = this.lightFollowsMouse;
      return copy;
   }

   public float labelBrightness() {
      return (ColorUtil.red(this.label) * 0.2126F + ColorUtil.green(this.label) * 0.7152F + ColorUtil.blue(this.label) * 0.0722F) / 255.0F;
   }

   public static Glass lerp(Glass a, Glass b, float t) {
      Glass result = (t < 0.5F ? a : b).copy();
      result.blur = mix(a.blur, b.blur, t);
      result.refraction = mix(a.refraction, b.refraction, t);
      result.bevel = mix(a.bevel, b.bevel, t);
      result.curve = mix(a.curve, b.curve, t);
      result.magnify = mix(a.magnify, b.magnify, t);
      result.dispersion = mix(a.dispersion, b.dispersion, t);
      result.specular = mix(a.specular, b.specular, t);
      result.sharpness = mix(a.sharpness, b.sharpness, t);
      result.specularColor = ColorUtil.lerp(a.specularColor, b.specularColor, t);
      result.rim = mix(a.rim, b.rim, t);
      result.rimWidth = mix(a.rimWidth, b.rimWidth, t);
      result.fresnel = mix(a.fresnel, b.fresnel, t);
      result.saturation = mix(a.saturation, b.saturation, t);
      result.brightness = mix(a.brightness, b.brightness, t);
      result.contrast = mix(a.contrast, b.contrast, t);
      result.grain = mix(a.grain, b.grain, t);
      result.wobble = mix(a.wobble, b.wobble, t);
      result.wobbleScale = mix(a.wobbleScale, b.wobbleScale, t);
      result.wobbleSpeed = mix(a.wobbleSpeed, b.wobbleSpeed, t);
      result.depth = mix(a.depth, b.depth, t);
      result.label = ColorUtil.lerp(a.label, b.label, t);
      if (a.tint.type() == Paint.Type.SOLID && b.tint.type() == Paint.Type.SOLID) {
         result.tint = Paint.solid(ColorUtil.lerp(a.tint.color(0), b.tint.color(0), t));
      }

      return result;
   }

   private static float mix(float from, float to, float t) {
      return from + (to - from) * t;
   }
}
