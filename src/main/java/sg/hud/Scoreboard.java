package sg.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.scores.DisplaySlot;
import org.joml.Matrix4f;
import sg.events.impl.UiAnimationEvents;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;

public final class Scoreboard extends HudElement implements InteractiveHudElement {
   private static final float аЙ = 22.0F;
   private static final float ак = 6.0F;
   private static Scoreboard Я;
   private boolean Ач;
   private boolean АЭ;
   private boolean А必;
   private boolean Ащ;
   private boolean АГ;
   private boolean Ал;
   private boolean Аэ;
   private boolean Аю;
   private boolean АЩ;
   private float аг;
   private float а0;
   private float ах;
   private float аУ;
   private float аЗ = Float.POSITIVE_INFINITY;
   private float аЕ = Float.POSITIVE_INFINITY;
   private float аП = Float.NEGATIVE_INFINITY;
   private float а西 = Float.NEGATIVE_INFINITY;
   private float ад;
   private float аБ;
   private static final String ЯЧт = "Scoreboard";
   private static final float ЯЧа = -1.0F;
   private static final float ЯЧЮ = -1.0F;
   private static final float ЯЧЧ = 0.01F;
   private static final float ЯЧЛ = 0.01F;
   private static final float ЯЧ诶 = 8.0F;
   private static final float ЯЧ1 = 4.0F;
   private static final double ЯЧЬ = 90.0;
   private static final double ЯЧ衣 = 220.0;
   private static final double ЯЧш = 210.0;
   private static final float ЯЧИ = 1.2F;
   private static final float ЯЧь = 5.0F;
   private static final float ЯЧж = 6.0F;
   private static final float ЯЧ弟 = 1.1F;
   private static final float ЯЧб = 6.0F;
   private static final double ЯЧЫ = 210.0;
   private static final float ЯЧl = 7.0F;
   private static final float ЯЧ8 = 7.0F;
   private static final float ЯЧ5 = 8.0F;
   private static final float ЯЧв = 8.0F;
   private static final float ЯЧъ = 4.0F;
   private static final double ЯЧМ = 235.0;
   private static final double ЯЧН = 70.0;
   private static final float ЯЧн = 22.0F;
   private static final float ЯЧЦ = 6.0F;
   private static final float ЯЧч = 4.0F;
   private static final float ЯЧЭ = 6.0F;
   private static final float ЯЧ必 = 22.0F;
   private static final float ЯЧщ = 22.0F;

   public void У_() {
      this.АЩ = false;
      this.аг = 0.0F;
      this.а0 = 0.0F;
   }

   public void ШК() {
      if (!this.Аю) {
         this.ШР();
         this.У_();
      }
   }

   public void у(int var1, int var2, int var3, int var4) {
      if (this.Ащ) {
         float var5 = Math.min(var1, var3);
         float var6 = Math.max(var1, var3);
         float var7 = Math.min(var2, var4);
         float var8 = Math.max(var2, var4);
         if (!(var6 - var5 < ЯЧ诶) && !(var8 - var7 < ЯЧ1)) {
            this.аЗ = Math.min(this.аЗ, var5);
            this.аЕ = Math.min(this.аЕ, var7);
            this.аП = Math.max(this.аП, var6);
            this.а西 = Math.max(this.а西, var8);
         }
      }
   }

   @Override
   public float getScaledHeight() {
      return this.height;
   }

   public void к(float var1, float var2) {
      if (this.АЩ) {
         Minecraft var3 = Minecraft.getInstance();
         float var4 = Math.max(0.0F, var3.getWindow().getScreenWidth() - this.getScaledWidth());
         float var5 = Math.max(0.0F, var3.getWindow().getScreenHeight() - this.getScaledHeight());
         this.setX(Math.clamp(var1 + this.аг, 0.0F, var4));
         this.setBaseY(Math.clamp(var2 + this.а0, 0.0F, var5));
      }
   }

   @Override
   public float getScaledWidth() {
      return this.width;
   }

   public boolean ПХ() {
      return this.А必;
   }

   public void Шь() {
      this.Аю = false;
   }

   public void Шъ() {
      if (ПЦ() && this.Аю && Пе() && !(this.width <= 1.0F) && !(this.height <= 1.0F)) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.gui.screen() instanceof ChatScreen) {
            float var2 = this.getX();
            float var3 = this.getY();
            float var4 = this.getScaledWidth();
            float var5 = this.getScaledHeight();
            float[] var6 = this.Я();
            int var7 = ColorMath.packArgb(255, 255, 255, this.А必 ? ЯЧЬ : ЯЧ衣);
            int var8 = this.А必 ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : ColorMath.packArgb(12, 14, 18, ЯЧш);
            UiDrawing.setTransform(new Matrix4f());
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

            try {
               UiBatchDrawing.drawOutline(var2, var3, var4, var5, ЯЧИ, ЯЧь, var7);
               UiBatchDrawing.drawRoundedRect(var6[0], var6[1], var6[2], var6[3], ЯЧж, var8);
               UiBatchDrawing.drawOutline(var6[0], var6[1], var6[2], var6[3], ЯЧ弟, ЯЧб, ColorMath.packArgb(255, 255, 255, ЯЧЫ));
               UiBatchDrawing.drawRoundedRect(
                  var6[0] + ЯЧl,
                  var6[1] + ЯЧ8,
                  ЯЧ5,
                  ЯЧв,
                  ЯЧъ,
                  this.А必 ? ColorMath.packArgb(255, 255, 255, ЯЧМ) : ColorMath.packArgb(255, 255, 255, ЯЧН)
               );
            } finally {
               UiBatchDrawing.endBatch();
            }
         }
      }
   }

   public void у(GuiGraphicsExtractor var1) {
      this.ад = 0.0F;
      this.аБ = 0.0F;
      this.АГ = false;
      this.Ащ = false;
      this.Ал = true;
      this.Уу();
      if (ПЦ() && var1 != null && Пе()) {
         this.Ащ = true;
         Minecraft var2 = Minecraft.getInstance();
         float var3 = Math.max(1.0F, var2.getWindow().getGuiScale());
         if (this.Ач && this.Аэ && this.width > 1.0F && this.height > 1.0F) {
            this.ад = this.getX() / var3 - this.ах;
            this.аБ = this.getBaseY() / var3 - this.аУ;
            if (Math.abs(this.ад) > ЯЧЧ || Math.abs(this.аБ) > ЯЧЛ) {
               var1.pose().pushMatrix();
               var1.pose().translate(this.ад, this.аБ);
               this.АГ = true;
            }
         }
      } else {
         this.ШР();
      }
   }

   public static float уР() {
      return Я == null ? 0.0F : Я.аБ;
   }

   public void _д(boolean var1) {
      this.А必 = var1;
   }

   public void 必(GuiGraphicsExtractor var1) {
      if (this.Ал) {
         this.Ал = false;
         if (this.АГ && var1 != null) {
            var1.pose().popMatrix();
         }

         this.АГ = false;
         this.Ащ = false;
         if (!ПЦ() || !Пе()) {
            this.ШР();
            this.ад = 0.0F;
            this.аБ = 0.0F;
         } else if (this.аП > this.аЗ && this.а西 > this.аЕ) {
            Minecraft var2 = Minecraft.getInstance();
            float var3 = Math.max(1.0F, var2.getWindow().getGuiScale());
            this.ах = this.аЗ;
            this.аУ = this.аЕ;
            this.Аэ = true;
            this.Аю = true;
            this.АЭ = true;
            this.setWidth((this.аП - this.аЗ) * var3);
            this.setHeight((this.а西 - this.аЕ) * var3);
            if (!this.Ач) {
               this.setX(this.ах * var3);
               this.setBaseY(this.аУ * var3);
            }

            this.АЭ = false;
         } else {
            this.ШР();
         }
      }
   }

   private float[] Я() {
      if (Minecraft.getInstance().gui.screen() instanceof ChatScreen && !(this.width <= 1.0F) && !(this.height <= 1.0F)) {
         float var1 = this.getX();
         float var2 = this.getY();
         float var3 = var1 - ЯЧн - ЯЧЦ;
         if (var3 < ЯЧч) {
            var3 = var1 + this.getScaledWidth() + ЯЧЭ;
         }

         return new float[]{var3, var2, ЯЧ必, ЯЧщ};
      } else {
         return new float[]{0.0F, 0.0F, 0.0F, 0.0F};
      }
   }

   public static float уЪ() {
      return Я == null ? 0.0F : Я.ад;
   }

   @Override
   public void setX(float var1) {
      super.setX(var1);
      if (!this.АЭ && var1 >= 0.0F) {
         this.Ач = true;
      }
   }

   public static Scoreboard 必_791f1767() {
      return Я;
   }

   @Override
   public void setBaseY(float var1) {
      super.setBaseY(var1);
      if (!this.АЭ && var1 >= 0.0F) {
         this.Ач = true;
      }
   }

   public boolean П弟() {
      return this.Ач;
   }

   public static boolean Пе() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level == null) {
         return false;
      }

      try {
         return var0.level.getScoreboard().getDisplayObjective(DisplaySlot.SIDEBAR) != null;
      } catch (Throwable var2) {
         return false;
      }
   }

   public void ШЪ() {
      this.Ач = true;
   }

   public boolean р(float var1, float var2, int var3) {
      if (var3 == 0 && this.hasRenderableSize()) {
         if (!WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
            return false;
         }

         this.АЩ = true;
         this.аг = this.getX() - var1;
         this.а0 = this.getBaseY() - var2;
         this.Ач = true;
         return true;
      } else {
         return false;
      }
   }

   public static boolean ПЦ() {
      try {
         UiAnimationEvents.ScoreboardPlacementQuery var0 = new UiAnimationEvents.ScoreboardPlacementQuery();
         RoseClient.getInstance().getEventBus().post(var0);
         return var0.isCustomPlacementEnabled();
      } catch (Throwable var1) {
         return false;
      }
   }

   private void Уу() {
      this.аЗ = Float.POSITIVE_INFINITY;
      this.аЕ = Float.POSITIVE_INFINITY;
      this.аП = Float.NEGATIVE_INFINITY;
      this.а西 = Float.NEGATIVE_INFINITY;
   }

   @Override
   public float getY() {
      return this.baseY;
   }

   public void ШР() {
      this.Ал = false;
      this.АЭ = true;
      this.setWidth(0.0F);
      this.setHeight(0.0F);
      this.АЭ = false;
   }

   public Scoreboard() {
      super(ЯЧт, ЯЧа, ЯЧЮ, 0.0F, 0.0F);
      Я = this;
   }

   @Override
   public boolean hasRenderableSize() {
      return ПЦ() && !this.А必 && Пе() && super.hasRenderableSize();
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (ПЦ() && Пе() && var3 == 0) {
         float[] var4 = this.Я();
         if (var4[2] > 0.0F && WorldProjection.containsPoint(var1, var2, var4[0], var4[1], var4[2], var4[3])) {
            this.А必 = !this.А必;
            if (this.А必) {
               this.Ач = true;
            }

            return true;
         } else {
            return this.А必 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight());
         }
      } else {
         return false;
      }
   }
}
