package sg.hud;

public class KillCardsHudElement extends HudElement {
   private boolean ХЯ;
   private float п诶;
   private float п1;
   private static final String Ях0 = "KillCards";
   private static final float Яхх = 0.5F;
   private static final float ЯхУ = 0.5F;
   private static final float ЯхЗ = 80.0F;
   private static final float ЯхЕ = 80.0F;

   @Override
   public float getScaledWidth() {
      return this.getWidth();
   }

   public KillCardsHudElement() {
      super(Ях0, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   @Override
   public float getScaledHeight() {
      return this.getHeight();
   }

   public boolean чБ() {
      return this.ХЯ && (Math.abs(this.x - this.п诶) > 2.0F || Math.abs(this.baseY - this.п1) > 2.0F);
   }

   public void А(float var1, float var2, float var3, float var4) {
      float var5 = this.getWidth() > 0.0F ? this.getWidth() : var3;
      float var6 = this.x + var5 * Яхх;
      float var7 = var1 + var3 * ЯхУ;
      boolean var8 = this.x == 0.0F && this.baseY == 0.0F || Math.abs(var6 - var7) < ЯхЗ && Math.abs(this.baseY - var2) < ЯхЕ;
      boolean var9 = this.ХЯ && Math.abs(this.x - this.п诶) < 2.0F && Math.abs(this.baseY - this.п1) < 2.0F;
      if (!this.ХЯ && var8 || var9) {
         this.x = var1;
         this.baseY = var2;
      }

      this.п诶 = var1;
      this.п1 = var2;
      this.ХЯ = true;
      if (var3 > 0.0F && var4 > 0.0F) {
         this.setWidth(var3);
         this.setHeight(var4);
      } else {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      }
   }
}
