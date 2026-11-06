package dev.rose.render;

import dev.rose.render.gpu.RoseShader;
import dev.rose.render.style.Paint;


final class PaintUniforms {
   private static final float[] POSITIONS = new float[4];

   private PaintUniforms() {
   }

   static void upload(RoseShader shader, String prefix, Paint paint) {
      int count = paint.stopCount();

      for (int i = 0; i < 4; i++) {
         POSITIONS[i] = i < count ? paint.stop(i) : 1.0F;
      }

      double angle = Math.toRadians(paint.angle() + paint.spin() * Theme.time());
      shader.safeGetUniform(prefix + "Type").set(paint.type().ordinal());
      shader.safeGetUniform(prefix + "Count").set(count);
      shader.safeGetUniform(prefix + "Lab").set(paint.lab());
      shader.safeGetUniform(prefix + "Pos").set(POSITIONS[0], POSITIONS[1], POSITIONS[2], POSITIONS[3]);
      shader.safeGetUniform(prefix + "Dir").set((float)Math.cos(angle), (float)Math.sin(angle));
      shader.safeGetUniform(prefix + "Center").set(paint.centerX(), paint.centerY());
      shader.safeGetUniform(prefix + "Radius").set(paint.radius());
   }
}
