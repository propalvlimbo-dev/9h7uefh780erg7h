package dev.rose.render.world;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;


public final class WorldProjection {
   private static final Matrix4f projection = new Matrix4f();
   private static final Matrix4f view = new Matrix4f();

   private WorldProjection() {
   }

   public static void set(Matrix4fc newProjection, Matrix4fc newView) {
      projection.set(newProjection);
      view.set(newView);
   }

   public static Matrix4f viewProjection(Matrix4f dest) {
      return dest.set(projection).mul(view);
   }
}
