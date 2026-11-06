package sg.render.avatar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import settings.render.ClientShaderSource;
import sg.ec.FontRenderer;
import sg.ec.ClientFonts;
import sg.render.ClientLogo;
import sg.client.RoseClient;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public final class MainMenuLoadingRenderer {
   public static final Identifier field_2 = Identifier.fromNamespaceAndPath(MainMenuLoadingRenderer.сТЪ, MainMenuLoadingRenderer.сТт);
   private static final Identifier ф = Identifier.fromNamespaceAndPath(MainMenuLoadingRenderer.сТа, MainMenuLoadingRenderer.сТЮ);
   private static final int Х5 = -2580225;
   private static final int Хв = -4690198;
   private static MainMenuLoadingRenderer.LoadingSnapshot В;
   private static long ВХ = MainMenuLoadingRenderer.сТЧ;
   private static long Ве;
   private static float еИ;
   private static final float сIо = 0.001F;
   private static final float сIO = 0.5F;
   private static final float сI7 = 0.46F;
   private static final float сIР = 84.0F;
   private static final float сI3 = 1000.0F;
   private static final float сIр = 0.1F;
   private static final float сI6 = -10.0F;
   private static final float сI_ = 0.001F;
   private static final float сIШ = 0.5F;
    private static final String сIы = ClientLogo.GLYPH;
   private static final int сIй = -2580225;
   private static final int сIЖ = -4690198;
   private static final float сIм = 90.0F;
   private static final float сIК = 1050.0F;
   private static final float сII = 860.0F;
   private static final float сIТ = 620.0F;
   private static final float сIО = 1530.0F;
   private static final float сIФ = 850.0F;
   private static final float сIЙ = 720.0F;
   private static final float сIк = 420.0F;
   private static final float сIг = 0.001F;
   private static final float сI0 = 224.0F;
   private static final float сIх = 8.0F;
   private static final float сIУ = 0.5F;
   private static final float сIЗ = 0.5F;
   private static final float сIЕ = 43.0F;
   private static final float сIП = 0.5F;
   private static final int сI西 = -2580225;
   private static final int сIд = -4690198;
   private static final int сIБ = 587202559;
   private static final int сIС = 1242567450;
   private static final float сIз = 1.5F;
   private static final float сIц = 0.35F;
   private static final float сIя = 0.5F;
   private static final float сТс = 0.5F;
   private static final float сТВ = 0.96F;
   private static final float сТЯ = 0.96F;
   private static final int сТХ = -16777216;
   private static final float сТе = 3.0F;
   private static final float сТА = 6.0F;
   private static final float сТ4 = 15.0F;
   private static final float сТп = 10.0F;
   private static final int сТД = 16777215;
   private static final String сТЪ = "rose";
   private static final String сТт = "images/mainmenu/gavs_loading_background.png";
   private static final String сТа = "rose";
    private static final String сТЮ = "fonts/client_logo.png";
   private static final long сТЧ = Long.MIN_VALUE;

   private static float У(float var0) {
      float var1 = Mth.clamp(var0, 0.0F, 1.0F);
      return var1 * var1 * (сТе - 2.0F * var1);
   }

   private static float ы(float var0) {
      float var1 = Mth.clamp(var0, 0.0F, 1.0F);
      return var1 * var1 * var1 * (var1 * (var1 * сТА - сТ4) + сТп);
   }

   private static void х(float var0, float var1, float var2, long var3, float var5) {
      var2 *= ClientLogo.DISPLAY_SCALE;
       FontRenderer var6 = ClientLogo.font();
       float var7 = ClientLogo.width(var2);
      float var8 = var0 - var7 * сIШ;
      float var9 = UiDrawing.alignTextCenterY(var6, сIы, var1, var2);
      int var10 = С(ThemeColorSlot.MAIN, сIй);
      int var11 = С(ThemeColorSlot.MAIN_TWO, сIЖ);
      float var12 = ы(Mth.clamp(((float)var3 - сIм) / сIК, 0.0F, 1.0F));
      float var13 = ы(Mth.clamp(((float)var3 - сII) / сIТ, 0.0F, 1.0F));
      float var14 = ы(Mth.clamp(((float)var3 - сIО) / сIФ, 0.0F, 1.0F));
       UiDrawing.drawAnimatedLogoGlyph(var6, ClientLogo.CODEPOINT, var8, var9, var2, л(var10, var5), var11, var12, var13, var14);
   }

   private MainMenuLoadingRenderer() {
   }

   private static int л(int var0, float var1) {
      int var2 = Math.round((var0 >>> 24 & 0xFF) * Mth.clamp(var1, 0.0F, 1.0F));
      return var0 & сТД | var2 << 24;
   }

   public static void рЕ() {
      MainMenuLoadingRenderer.LoadingSnapshot var0 = В;
      В = null;
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         int var2 = var1.getWindow().getScreenWidth();
         int var3 = var1.getWindow().getScreenHeight();
         if (var2 > 0 && var3 > 0) {
            х(var0);
            if (!(var0.alpha <= сIо)) {
               float var4 = (float)var2 / var0.scaledWidth;
               float var5 = (float)var3 / var0.scaledHeight;
               float var6 = Math.min(var4, var5);
               float var7 = var2 * сIO;
               float var8 = var3 * сI7;
               float var9 = сIР * var6;
               long var10 = Math.max(0L, var0.now - var0.session);
               UiDrawing.setTransform(new Matrix4f());
               UiDrawing.disableScissor();
               UiDrawing.resetGlobalAlpha();
               х(var7, var8, var9, var10, var0.alpha);
               х(var7, var8, var9, var4, var5, var10, var0.alpha);
               UiDrawing.disableScissor();
               UiDrawing.resetGlobalAlpha();
               UiDrawing.setTransform(new Matrix4f());
            }
         }
      }
   }

   public static void х(TextureManager var0) {
      var0.registerAndLoad(field_2, new ClasspathReloadableTexture(field_2));
      var0.registerAndLoad(ф, new ClasspathReloadableTexture(ф, true));
      UiDrawing.precompilePipelines(ClientShaderSource.getShaderSource());
   }

   private static void х(MainMenuLoadingRenderer.LoadingSnapshot var0) {
      if (ВХ != var0.session) {
         ВХ = var0.session;
         Ве = var0.now;
         еИ = 0.0F;
      }

      float var1 = Mth.clamp((float)(var0.now - Ве) / сI3, 0.0F, сIр);
      Ве = var0.now;
      float var2 = 1.0F - (float)Math.exp(сI6 * var1);
      еИ = еИ + (var0.progress - еИ) * var2;
      if (var0.progress >= 1.0F && 1.0F - еИ < сI_) {
         еИ = 1.0F;
      }
   }

   public static void х(long var0, long var2, int var4, int var5, float var6, float var7) {
      В = new MainMenuLoadingRenderer.LoadingSnapshot(var0, var2, Math.max(1, var4), Math.max(1, var5), Mth.clamp(var6, 0.0F, 1.0F), Mth.clamp(var7, 0.0F, 1.0F));
   }

   private static void х(float var0, float var1, float var2, float var3, float var4, long var5, float var7) {
      float var8 = У(Mth.clamp(((float)var5 - сIЙ) / сIк, 0.0F, 1.0F));
      float var9 = var7 * var8;
      if (!(var9 <= сIг)) {
         float var10 = сI0 * var3;
         float var11 = сIх * var4;
         float var12 = var0 - var10 * сIУ;
         float var13 = var1 + var2 * сIЗ + сIЕ * var4;
         float var14 = Math.max(1.0F, 1.0F * Math.min(var3, var4));
         float var15 = var11 * сIП;
         int var16 = С(ThemeColorSlot.MAIN, сI西);
         int var17 = С(ThemeColorSlot.MAIN_TWO, сIд);
         UiDrawing.drawRoundedRect(var12 - var14, var13 - var14, var10 + var14 * 2.0F, var11 + var14 * 2.0F, var15 + var14, л(сIБ, var9));
         UiDrawing.drawRoundedRect(var12, var13, var10, var11, var15, л(сIС, var9));
         float var18 = Math.max(1.0F, сIз * Math.min(var3, var4));
         float var19 = Math.max(0.0F, var10 - var18 * 2.0F);
         float var20 = Math.max(1.0F, var11 - var18 * 2.0F);
         float var21 = var19 * Mth.clamp(еИ, 0.0F, 1.0F);
         if (var21 > сIц) {
            float var22 = Math.min(var20 * сIя, var21 * сТс);
            int var23 = л(var16, var9 * сТВ);
            int var24 = л(var17, var9 * сТЯ);
            UiDrawing.drawRoundedRect(var12 + var18, var13 + var18, var21, var20, var22, new Vector4i(var23, var24, var23, var24));
         }
      }
   }

   private static int С(ThemeColorSlot var0, int var1) {
      try {
         RoseClient var2 = RoseClient.getInstance();
         if (var2 != null && var2.getServices() != null && var2.getServices().getThemes() != null) {
            return ThemeEditor.getColorArgb(var0) | сТХ;
         }
      } catch (RuntimeException var3) {
      }

      return var1;
   }

   private record LoadingSnapshot(long session, long now, int scaledWidth, int scaledHeight, float alpha, float progress) {
   }
}
