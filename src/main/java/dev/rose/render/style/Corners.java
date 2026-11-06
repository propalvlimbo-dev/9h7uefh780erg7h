package dev.rose.render.style;

import dev.rose.render.Theme;


public record Corners(Corner topLeft, Corner topRight, Corner bottomRight, Corner bottomLeft) {
   public static final Corners NONE = all(Corner.NONE);

   public static Corners all(Corner corner) {
      return new Corners(corner, corner, corner, corner);
   }

   public static Corners round(float radius) {
      return all(Corner.round(radius));
   }

   public static Corners themed(float radius) {
      return all(new Corner(radius * Theme.cornerScale, Theme.cornerShape));
   }

   public Corners scaled(float factor) {
      return new Corners(this.topLeft.scaled(factor), this.topRight.scaled(factor), this.bottomRight.scaled(factor), this.bottomLeft.scaled(factor));
   }

   public void writeRadii(float[] target, int offset) {
      target[offset] = this.topLeft.radius();
      target[offset + 1] = this.topRight.radius();
      target[offset + 2] = this.bottomRight.radius();
      target[offset + 3] = this.bottomLeft.radius();
   }

   public void writeShapes(float[] target, int offset) {
      target[offset] = this.topLeft.shape();
      target[offset + 1] = this.topRight.shape();
      target[offset + 2] = this.bottomRight.shape();
      target[offset + 3] = this.bottomLeft.shape();
   }
}
