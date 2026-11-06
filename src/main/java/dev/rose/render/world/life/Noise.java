package dev.rose.render.world.life;

import net.minecraft.util.Mth;
import org.joml.Vector3f;


public final class Noise {
   private static final float STEP = 0.6F;
   private static final float OFFSET = 137.13F;

   private Noise() {
   }

   public static float value(float x, float y, float z) {
      int xi = floor(x);
      int yi = floor(y);
      int zi = floor(z);
      float u = fade(x - xi);
      float v = fade(y - yi);
      float w = fade(z - zi);
      float y0z0 = lerp(hash(xi, yi, zi), hash(xi + 1, yi, zi), u);
      float y1z0 = lerp(hash(xi, yi + 1, zi), hash(xi + 1, yi + 1, zi), u);
      float y0z1 = lerp(hash(xi, yi, zi + 1), hash(xi + 1, yi, zi + 1), u);
      float y1z1 = lerp(hash(xi, yi + 1, zi + 1), hash(xi + 1, yi + 1, zi + 1), u);
      return lerp(lerp(y0z0, y1z0, v), lerp(y0z1, y1z1, v), w);
   }

   public static Vector3f curl(float x, float y, float z, Vector3f out) {
      float dP3dy = value(x - 137.13F, y + 0.6F - 137.13F, z - 137.13F) - value(x - 137.13F, y - 0.6F - 137.13F, z - 137.13F);
      float dP2dz = value(x + 137.13F, y + 137.13F, z + 0.6F + 137.13F) - value(x + 137.13F, y + 137.13F, z - 0.6F + 137.13F);
      float dP1dz = value(x, y, z + 0.6F) - value(x, y, z - 0.6F);
      float dP3dx = value(x + 0.6F - 137.13F, y - 137.13F, z - 137.13F) - value(x - 0.6F - 137.13F, y - 137.13F, z - 137.13F);
      float dP2dx = value(x + 0.6F + 137.13F, y + 137.13F, z + 137.13F) - value(x - 0.6F + 137.13F, y + 137.13F, z + 137.13F);
      float dP1dy = value(x, y + 0.6F, z) - value(x, y - 0.6F, z);
      return out.set(dP3dy - dP2dz, dP1dz - dP3dx, dP2dx - dP1dy).mul(0.8333333F);
   }

   public static Vector3f swirl(float x, float y, float z, Vector3f out) {
      return out.set(
         Mth.cos(y * 1.3F) + 0.5F * Mth.cos(y * 2.7F + 1.3F),
         Mth.cos(z * 1.1F) + 0.5F * Mth.cos(z * 2.3F + 2.1F),
         Mth.cos(x * 1.7F) + 0.5F * Mth.cos(x * 3.1F + 0.7F)
      );
   }

   public static Vector3f flow(float x, float y, float z, boolean lite, Vector3f out) {
      return lite ? swirl(x, y, z, out) : curl(x, y, z, out);
   }

   private static int floor(float value) {
      int i = (int)value;
      return value < i ? i - 1 : i;
   }

   private static float fade(float t) {
      return t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);
   }

   private static float lerp(float a, float b, float t) {
      return a + (b - a) * t;
   }

   private static float hash(int x, int y, int z) {
      int h = x * 374761393 + y * 668265263 + z * 1274126177;
      h = (h ^ h >>> 13) * 1274126177;
      h ^= h >>> 16;
      return (h >>> 8) * 1.1920929E-7F - 1.0F;
   }
}
