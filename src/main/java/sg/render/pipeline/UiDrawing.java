package sg.render.pipeline;

import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.MinecraftAccess;
import sg.ec.FontRenderer;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.client.RoseClient;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.network.NameProtectionTextRewriter;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class UiDrawing {
   private static Matrix4f drawingTransform = new Matrix4f();
   private static final Matrix4f о = new Matrix4f();
   private static final Deque<UiDrawing.TransformSnapshot> transformStack = new ArrayDeque<>();
   private static Matrix4f guiScaleTransform;
   private static boolean scissorEnabled = false;
   private static final Vector4f scissorRect = new Vector4f();
   private static float globalAlpha = UiDrawing.сТП;
   private static final Deque<Vector4f> scissorRectStack = new ArrayDeque<>();
   private static final Deque<Boolean> scissorEnabledStack = new ArrayDeque<>();
   private static boolean itemPassInitialized;
   private static final float сТи = 13.0F;
   private static final float сТ9 = 13.0F;
   private static final double сТо = 255.0;
   private static final float сТO = 13.0F;
   private static final double сТ7 = 255.0;
   private static final float сТР = 16.0F;
   private static final float сТ3 = 16.0F;
   private static final float сТр = 16.0F;
   private static final double сТ6 = 127.0;
   private static final float сТ_ = 9.0F;
   private static final float сТШ = 17.0F;
   private static final float сТы = 17.0F;
   private static final double сТй = 255.0;
   private static final float сТЖ = 2.2F;
   private static final String сТм = "*";
   private static final String сТК = "*";
   private static final float сТI = 0.5F;
   private static final String сТТ = "*";
   private static final float сТО = 0.5F;
   private static final float сТФ = 255.0F;
   private static final float сТЙ = 255.0F;
   private static final float сТк = 255.0F;
   private static final float сТг = 255.0F;
   private static final float сТ0 = 0.18F;
   private static final float сТх = 12.0F;
   private static final float сТУ = -1.0F;
   private static final float сТЗ = 255.0F;
   private static final String сТЕ = "This is a utility class and cannot be instantiated";
   private static final float сТП = 255.0F;

   public static void precompilePipelines(ShaderSource shaderSource) {
      getRectanglePass().precompilePipeline(shaderSource);
      getFontPass().precompilePipelines(shaderSource);
   }

   public static void drawItem(ItemStack item, float x, float y, float scale) {
      drawItem(item, x, y, scale, true);
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, Vector4f cornerRadii, int argb) {
      drawBlur(x, y, width, height, blurRadius, cornerRadii, new Vector4i(argb));
   }

   private static UiItemStackPass getItemPass() {
      itemPassInitialized = true;
      return UiDrawing.ItemPassHolder.INSTANCE;
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, int argb, Identifier texture, FilterMode filterMode) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), new Vector4f(1.0F), new Vector4i(argb), texture, filterMode);
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, Vector4i colors, Identifier texture) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), new Vector4f(1.0F), colors, texture);
   }

   public static void drawGlare(float x, float y, float width, float height, float progress, float opacity, float glareSize, float cornerRadius) {
      getGlarePass().drawGlare(x, y, width, height, progress, opacity, glareSize, cornerRadius);
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, Vector4f uvRect, int argb, Identifier texture) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), uvRect, new Vector4i(argb), texture);
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, Vector4f cornerRadii, int argb) {
      drawOutline(x, y, width, height, thickness, cornerRadii, new Vector4i(argb));
   }

   private static TexturePass getTexturePass() {
      return UiDrawing.TexturePassHolder.INSTANCE;
   }

   private static FontRenderPass getFontPass() {
      return UiDrawing.FontPassHolder.INSTANCE;
   }

   public static GpuTextureView getItemTexture(ItemStack item, float scale) {
      return getItemPass().I(item, scale);
   }

   @Generated
   private UiDrawing() {
      throw new UnsupportedOperationException(сТЕ);
   }

   private static Matrix4f createGuiScaleTransform(float pivotX, float pivotY) {
      float guiScale = HudScaleCoordinates.getHudScale();
      float screenWidth = Math.max(1.0F, MinecraftAccess.MINECRAFT_CLIENT.getWindow().getScreenWidth());
      float screenHeight = Math.max(1.0F, MinecraftAccess.MINECRAFT_CLIENT.getWindow().getScreenHeight());
      float pivotClipX = сТУ + pivotX * 2.0F / screenWidth;
      float pivotClipY = 1.0F - pivotY * 2.0F / screenHeight;
      return new Matrix4f().translate(pivotClipX, pivotClipY, 0.0F).scale(guiScale, guiScale, 1.0F).translate(-pivotClipX, -pivotClipY, 0.0F);
   }

   @Generated
   public static boolean isScissorEnabled() {
      return scissorEnabled;
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float bottomLeftRadius, float topLeftRadius, float topRightRadius, float bottomRightRadius, Vector4i colors) {
      drawRoundedRect(x, y, width, height, new Vector4f(bottomLeftRadius, bottomRightRadius, topRightRadius, topLeftRadius), colors);
   }

   public static void setTransform(Matrix4f transform) {
      Matrix4f localTransform = transform == null ? new Matrix4f() : new Matrix4f(transform);
      drawingTransform = guiScaleTransform == null ? localTransform : new Matrix4f(guiScaleTransform).mul(localTransform);
   }

   public static void clearTransforms() {
      resetTransforms();
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, int argb, Identifier texture) {
      drawTexture(x, y, width, height, cornerRadii, new Vector4f(1.0F), new Vector4i(argb), texture);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, int argb) {
      drawRoundedRect(x, y, width, height, new Vector4f(cornerRadius), new Vector4i(argb));
   }

   public static void drawRotatedItem(ItemStack item, float x, float y, float scale, float pitch, float yaw, float roll) {
      getItemPass().I(item, x, y, scale, pitch, yaw, roll);
   }

   private static ModuleGlare getGlarePass() {
      return UiDrawing.GlarePassHolder.INSTANCE;
   }

   public static void drawFittedItem(ItemStack item, float x, float y, float width, float height) {
      getItemPass().I(item, x, y, width, height);
   }

   @Generated
   public static float getGlobalAlpha() {
      return globalAlpha;
   }

   public static void setScissor(float x, float y, float width, float height) {
      scissorEnabled = true;
      scissorRect.x = x;
      scissorRect.y = y;
      scissorRect.z = width;
      scissorRect.w = height;
   }

   public static float getCapHeight(FontRenderer font, float fontSize) {
      return font.getFontFace().getCapHeight(fontSize);
   }

   public static void drawGlare(float x, float y, float width, float height, float progress, float opacity, float glareSize) {
      drawGlare(x, y, width, height, progress, opacity, glareSize, сТх);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float bottomLeftRadius, float topLeftRadius, float topRightRadius, float bottomRightRadius, int argb) {
      drawRoundedRect(x, y, width, height, new Vector4f(bottomLeftRadius, bottomRightRadius, topRightRadius, topLeftRadius), new Vector4i(argb));
   }

   public static float getTextWidth(FontRenderer font, String text, float fontSize, float advanceScale) {
      return font.getFontFace().measureTextWidth(NameProtectionTextRewriter.лШ(text), fontSize, 0.0F, advanceScale);
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, Vector4f uvRect, Vector4i colors, Identifier texture) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), uvRect, colors, texture);
   }

   public static void popScissor() {
      if (!scissorRectStack.isEmpty() && !scissorEnabledStack.isEmpty()) {
         Vector4f previousScissor = scissorRectStack.pop();
         boolean wasScissorEnabled = scissorEnabledStack.pop();
         scissorRect.set(previousScissor);
         scissorEnabled = wasScissorEnabled;
      } else {
         disableScissor();
      }
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, Vector4f uvRect, int argb, Identifier texture, FilterMode filterMode) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), uvRect, new Vector4i(argb), texture, filterMode);
   }

   public static void disableScissor() {
      scissorEnabled = false;
   }

   public static void popGuiScaleTransform() {
      if (transformStack.isEmpty()) {
         guiScaleTransform = null;
         drawingTransform = new Matrix4f();
      } else {
         UiDrawing.TransformSnapshot snapshot = transformStack.pop();
         drawingTransform = snapshot.matrix();
         guiScaleTransform = snapshot.transform();
      }
   }

   public static void drawItem(GuiGraphicsExtractor guiGraphics, ItemStack item, float x, float y, float scale, boolean drawDecorations) {
      drawItem(item, x, y, scale, drawDecorations);
   }

   public static void drawItem(GuiGraphicsExtractor guiGraphics, ItemStack item, float x, float y, float scale) {
      drawItem(guiGraphics, item, x, y, scale, true);
   }

   @Generated
   public static Vector4f getScissor() {
      return scissorRect;
   }

   public static void drawCenteredAsteriskIcon(float centerX, float centerY, float size, int argb) {
      float iconFontSize = size * сТЖ;
      FontRenderer iconFont = ClientFonts.getIconsFont();
      float iconWidth = iconFont.getFontFace().measureTextWidth(сТм, iconFontSize, 0.0F);
      drawText(iconFont, сТК, centerX - iconWidth * сТI, alignTextCenterY(iconFont, сТТ, centerY, iconFontSize), iconFontSize, argb, 0.0F, сТО, 1.0F);
   }

   private static TimeDial getTimeDialPass() {
      return UiDrawing.TimeDialPassHolder.INSTANCE;
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, float cornerRadius, Vector4i colors) {
      drawBlur(x, y, width, height, blurRadius, new Vector4f(cornerRadius), colors);
   }

   public static void drawTexture(float x, float y, float width, float height, float cornerRadius, int argb, Identifier texture) {
      drawTexture(x, y, width, height, new Vector4f(cornerRadius), new Vector4f(1.0F), new Vector4i(argb), texture);
   }

   public static float alignCapHeightCenterY(FontRenderer font, float centerY, float fontSize) {
      return centerY - font.getFontFace().getCapHeightCenterOffsetY(fontSize);
   }

   public static void drawTimeDial(float x, float y, float width, float height, float phase, float hover) {
      getTimeDialPass().drawTimeDial(x, y, width, height, phase, hover);
   }

   private static OutlinePass getOutlinePass() {
      return UiDrawing.OutlinePassHolder.INSTANCE;
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, Identifier texture) {
      drawTexture(x, y, width, height, cornerRadii, new Vector4f(1.0F), colors, texture);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, Vector4i colors) {
      drawRoundedRect(x, y, width, height, new Vector4f(cornerRadius), colors);
   }

   public static void resetGlobalAlpha() {
      globalAlpha = сТЗ;
   }

   private static void drawItemDecorations(ItemStack item, float x, float y, float scale) {
      if (item.isBarVisible()) {
         int barColor = item.getBarColor();
         drawRoundedRect(x + 2.0F * scale, y + сТи * scale, сТ9 * scale, 2.0F * scale, 0.0F, ColorMath.packArgb(0, 0, 0, сТо));
         drawRoundedRect(
            x + 2.0F * scale,
            y + сТO * scale,
            item.getBarWidth() * scale,
            scale,
            0.0F,
            ColorMath.packArgb(barColor >> 16 & 0xFF, barColor >> 8 & 0xFF, barColor & 0xFF, сТ7)
         );
      }

      if (MinecraftAccess.MINECRAFT_CLIENT.player != null) {
         float cooldownProgress = MinecraftAccess.MINECRAFT_CLIENT.player.getCooldowns().getCooldownPercent(item, MinecraftAccess.MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
         if (cooldownProgress > 0.0F) {
            drawRoundedRect(x, y + сТР * scale * (1.0F - cooldownProgress), сТ3 * scale, сТр * scale * cooldownProgress, 0.0F, ColorMath.packArgb(255, 255, 255, сТ6));
         }
      }

      if (item.getCount() != 1) {
         String countText = String.valueOf(item.getCount());
         int countFontSize = Math.max(7, Math.round(сТ_ * scale));
         float countTextWidth = getTextWidth(ClientFonts.getMediumFont(), countText, countFontSize);
         drawText(ClientFonts.getMediumFont(), countText, x + сТШ * scale - countTextWidth, y + сТы * scale - countFontSize, countFontSize, ColorMath.packArgb(255, 255, 255, сТй));
      }
   }

   public static void updateGuiScalePivot(float pivotX, float pivotY) {
      if (guiScaleTransform != null) {
         guiScaleTransform = createGuiScaleTransform(pivotX, pivotY);
         drawingTransform = new Matrix4f(guiScaleTransform);
      }
   }

   public static float getTextWidth(FontRenderer font, String text, int fontSize) {
      return font.getFontFace().measureTextWidth(NameProtectionTextRewriter.лШ(text), fontSize, 0.0F);
   }

   public static void drawWindowBackground(float x, float y, float width, float height, float cornerRadius) {
      boolean styledWindowsEnabled = PanelBackgroundRenderer.цр();
      boolean shadowEnabled = RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getHud().В().getValue();
      if (!styledWindowsEnabled && shadowEnabled) {
         drawShadow(x, y, width, height, cornerRadius * 2.0F, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW));
      }

      if (styledWindowsEnabled) {
         PanelBackgroundRenderer.э(x, y, width, height, cornerRadius);
      } else {
         PanelBackgroundRenderer.м(x, y, width, height, cornerRadius, false);
      }
   }

   private static RectangleRenderPass getRectanglePass() {
      return UiDrawing.RectanglePassHolder.INSTANCE;
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier texture, FilterMode filterMode) {
      getTexturePass().drawTexture(x, y, width, height, cornerRadii, uvRect, colors, texture, filterMode);
   }

   public static void drawItem(ItemStack item, float x, float y, float scale, boolean drawDecorations) {
      if (!item.isEmpty()) {
         getItemPass().л(item, x, y, scale);
         if (drawDecorations) {
            drawItemDecorations(item, x, y, scale);
         }
      }
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, Vector4f cornerRadii, Vector4i colors) {
      getOutlinePass().drawOutline(x, y, width, height, thickness, cornerRadii, colors);
   }

   @Generated
   public static Matrix4f getTransform() {
      return drawingTransform;
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, float cornerRadius, int argb) {
      drawOutline(x, y, width, height, thickness, new Vector4f(cornerRadius), new Vector4i(argb));
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier texture) {
      getTexturePass().drawTexture(x, y, width, height, cornerRadii, uvRect, colors, texture);
   }

   public static void drawAnimatedLogoGlyph(FontRenderer font, int codePoint, float x, float y, float fontSize, int argb, int accentArgb, float traceProgress, float fillProgress, float sweepProgress) {
      getFontPass().drawLoadingLogo(font, codePoint, x, y, fontSize, argb, accentArgb, traceProgress, fillProgress, sweepProgress);
   }

   public static void drawText(FontRenderer font, String text, float x, float y, float fontSize, int argb, float thickness, float smoothness, float advanceScale) {
      getFontPass().drawText(font, NameProtectionTextRewriter.лШ(text), x, y, fontSize, argb, thickness, smoothness, advanceScale);
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, float cornerRadius, int argb) {
      drawBlur(x, y, width, height, blurRadius, new Vector4f(cornerRadius), new Vector4i(argb));
   }

   public static void drawShadow(float x, float y, float width, float height, float cornerRadius, int argb) {
      drawShadow(x, y, width, height, new Vector4f(cornerRadius), argb);
   }

   public static void pushScissor(float x, float y, float width, float height) {
      scissorEnabledStack.push(scissorEnabled);
      scissorRectStack.push(new Vector4f(scissorRect));
      if (scissorEnabled) {
         float intersectionLeft = Math.max(scissorRect.x, x);
         float intersectionTop = Math.max(scissorRect.y, y);
         float intersectionRight = Math.min(scissorRect.x + scissorRect.z, x + width);
         float intersectionBottom = Math.min(scissorRect.y + scissorRect.w, y + height);
         setScissor(intersectionLeft, intersectionTop, Math.max(0.0F, intersectionRight - intersectionLeft), Math.max(0.0F, intersectionBottom - intersectionTop));
      } else {
         setScissor(x, y, width, height);
      }
   }

   public static void drawShinyText(FontRenderer font, String text, float x, float y, float fontSize, int argb, int shineArgb, float shineProgress) {
      getFontPass().drawShinyText(font, text, x, y, fontSize, argb, shineArgb, shineProgress);
   }

   public static void pushGuiScaleTransform(float pivotX, float pivotY) {
      transformStack.push(new UiDrawing.TransformSnapshot(new Matrix4f(drawingTransform), guiScaleTransform == null ? null : new Matrix4f(guiScaleTransform)));
      guiScaleTransform = createGuiScaleTransform(pivotX, pivotY);
      drawingTransform = new Matrix4f(guiScaleTransform);
   }

   public static float alignTextCenterY(FontRenderer font, String text, float centerY, float fontSize) {
      return centerY - font.getFontFace().getTextCenterOffsetY(text, fontSize);
   }

   public static void setGlobalAlpha(float alpha) {
      globalAlpha = alpha;
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, Vector4f cornerRadii, Vector4i colors) {
      ThemeWave.INSTANCE.registerUiRect(x, y, width, height);
      getBlurPass().drawBlur(x, y, width, height, blurRadius, cornerRadii, colors);
   }

   public static void drawText(FontRenderer font, String text, float x, float y, int fontSize, int argb) {
      getFontPass().drawText(font, NameProtectionTextRewriter.лШ(text), x, y, fontSize, argb);
   }

   public static void advanceItemFrame() {
      if (itemPassInitialized) {
         UiDrawing.ItemPassHolder.INSTANCE.йЫ();
      }
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, int argb, Identifier texture) {
      drawTexture(x, y, width, height, cornerRadii, uvRect, new Vector4i(argb), texture);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors) {
      ThemeWave.INSTANCE.registerUiRect(x, y, width, height);
      getRectanglePass().drawRoundedRect(x, y, width, height, cornerRadii, colors);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f cornerRadii, int argb) {
      drawRoundedRect(x, y, width, height, cornerRadii, new Vector4i(argb));
   }

   public static void resetTransforms() {
      transformStack.clear();
      guiScaleTransform = null;
      drawingTransform = new Matrix4f();
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, float cornerRadius, Vector4i colors) {
      drawOutline(x, y, width, height, thickness, new Vector4f(cornerRadius), colors);
   }

   public static void drawText(FontRenderer font, Component text, float x, float y, int fontSize, int argb) {
      getFontPass().drawComponent(font, NameProtectionTextRewriter.Е(text), x, y, fontSize, argb);
   }

   public static void drawShadow(float x, float y, float width, float height, Vector4f cornerRadii, int argb) {
      float maxCornerRadius = Math.max(Math.max(cornerRadii.x, cornerRadii.y), Math.max(cornerRadii.z, cornerRadii.w));
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(maxCornerRadius <= 0.0F)) {
         Vector4f rgba = ColorMath.toRgbaVector(argb);
         int red = (int)(rgba.x * сТФ);
         int green = (int)(rgba.y * сТЙ);
         int blue = (int)(rgba.z * сТк);
         float baseAlpha = rgba.w * сТг;

         for (int expansionPixels = (int)maxCornerRadius; expansionPixels > 0; expansionPixels -= 2) {
            float falloff = 1.0F - expansionPixels / maxCornerRadius;
            int layerAlpha = (int)(baseAlpha * сТ0 * falloff * falloff);
            if (layerAlpha > 0) {
               int layerArgb = ColorMath.packArgb(red, green, blue, layerAlpha);
               float expansion = expansionPixels;
               getRectanglePass()
                  .drawRoundedRect(
                     x - expansion,
                     y - expansion,
                     width + expansion * 2.0F,
                     height + expansion * 2.0F,
                     new Vector4f(cornerRadii).add(expansion, expansion, expansion, expansion),
                     new Vector4i(layerArgb)
                  );
            }
         }
      }
   }

   private static UiBlurPass getBlurPass() {
      return UiDrawing.BlurPassHolder.INSTANCE;
   }

   private static final class FontPassHolder {
      private static final FontRenderPass INSTANCE = new FontRenderPass();
   }

   private static final class OutlinePassHolder {
      private static final OutlinePass INSTANCE = new OutlinePass();
   }

   private static final class ItemPassHolder {
      private static final UiItemStackPass INSTANCE = new UiItemStackPass();
   }

   private static final class RectanglePassHolder {
      private static final RectangleRenderPass INSTANCE = new RectangleRenderPass();
   }

   private record TransformSnapshot(Matrix4f matrix, Matrix4f transform) {
   }

   private static final class GlarePassHolder {
      private static final ModuleGlare INSTANCE = new ModuleGlare();
   }

   private static final class TimeDialPassHolder {
      private static final TimeDial INSTANCE = new TimeDial();
   }

   private static final class BlurPassHolder {
      private static final UiBlurPass INSTANCE = new UiBlurPass();
   }

   private static final class TexturePassHolder {
      private static final TexturePass INSTANCE = new TexturePass();
   }
}
