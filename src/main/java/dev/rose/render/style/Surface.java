package dev.rose.render.style;

public final class Surface {
   public Corners corners = Corners.round(8.0F);
   public Paint fill = null;
   public Glass glass = null;
   public float borderWidth = 0.0F;
   public Paint border = null;
   public Shadow shadow = Shadow.NONE;
   public Shadow glow = Shadow.NONE;
   public Shadow innerShadow = Shadow.NONE;

   public Surface copy() {
      Surface copy = new Surface();
      copy.corners = this.corners;
      copy.fill = this.fill;
      copy.glass = this.glass == null ? null : this.glass.copy();
      copy.borderWidth = this.borderWidth;
      copy.border = this.border;
      copy.shadow = this.shadow;
      copy.glow = this.glow;
      copy.innerShadow = this.innerShadow;
      return copy;
   }
}
