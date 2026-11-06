package dev.rose.render.world.life;

import net.minecraft.util.Mth;


public final class Breath {
   private static final float TAU = (float) (Math.PI * 2);
   private static final float GOLDEN = 0.6180339F;

   private Breath() {
   }

   public static float at(float time, float seed) {
      float phase = phase(seed);
      return (
            Mth.sin(time * 0.9F + phase)
               + 0.5F * Mth.sin(time * 1.37F + phase * 2.3F + 1.1F)
               + 0.25F * Mth.sin(time * 2.11F + phase * 3.7F + 2.4F)
         )
         / 1.75F;
   }

   public static float unit(float time, float seed) {
      return at(time, seed) * 0.5F + 0.5F;
   }

   public static float scale(float time, float seed, float amount) {
      return 1.0F + at(time, seed) * amount;
   }

   public static float phase(float seed) {
      float turn = seed * 0.6180339F;
      return (turn - (float)Math.floor(turn)) * (float) (Math.PI * 2);
   }
}
