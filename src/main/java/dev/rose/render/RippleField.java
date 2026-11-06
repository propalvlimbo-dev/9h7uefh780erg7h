package dev.rose.render;

public final class RippleField {
   public static final int MAX = 4;
   private static final float LIFETIME = 1.4F;
   private final float[] x = new float[4];
   private final float[] y = new float[4];
   private final float[] strength = new float[4];
   private final long[] start = new long[4];
   private int next;

   public void add(float centerX, float centerY, float strength) {
      this.x[this.next] = centerX;
      this.y[this.next] = centerY;
      this.strength[this.next] = strength;
      this.start[this.next] = System.nanoTime();
      this.next = (this.next + 1) % 4;
   }

   int write(float[] target) {
      long now = System.nanoTime();
      int count = 0;

      for (int i = 0; i < 4; i++) {
         if (this.start[i] != 0L) {
            float age = (float)(now - this.start[i]) / 1.0E9F;
            if (!(age > 1.4F)) {
               int o = count * 4;
               target[o] = this.x[i];
               target[o + 1] = this.y[i];
               target[o + 2] = age;
               target[o + 3] = this.strength[i];
               count++;
            }
         }
      }

      for (int i = count * 4; i < target.length; i++) {
         target[i] = 0.0F;
      }

      return count;
   }
}
