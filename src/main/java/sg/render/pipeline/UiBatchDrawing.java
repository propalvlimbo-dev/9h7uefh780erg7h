package sg.render.pipeline;

import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.MinecraftAccess;
import sg.ec.FontRenderer;
import sg.ec.ClientFonts;
import sg.modules.impl.render.Hud;
import sg.client.RoseClient;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.network.NameProtectionTextRewriter;
import sg.render.RoseBatchedItemAtlas;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class UiBatchDrawing implements UiRenderPassSupport, MinecraftAccess {
   private static final UiBatchRenderer renderer = new UiBatchRenderer();
   private static final float сАШ = 16.0F;
   private static final float сАы = 30.0F;
   private static final float сАй = 0.5F;
   private static final float сАЖ = 18.0F;
   private static final float сАм = 10.0F;
   private static final float сАК = 1.5F;
   private static final float сАI = 0.5F;
   private static final float сАТ = 22.0F;
   private static final float сАО = 30.0F;
   private static final float сАФ = 11.0F;
   private static final float сАЙ = 11.0F;
   private static final float сАк = 255.0F;
   private static final float сАг = 255.0F;
   private static final double сА0 = 255.0;
   private static final float сАх = 1.5F;
   private static final float сАУ = 3.0F;
   private static final float сАЗ = 1.5F;
   private static final float сАЕ = 3.0F;
   private static final float сАП = 16.0F;
   private static final float сА西 = 16.0F;
   private static final float сАд = 13.0F;
   private static final float сАБ = 13.0F;
   private static final int сАС = -16777216;
   private static final float сАз = 13.0F;
   private static final int сАц = -16777216;
   private static final float сАя = 16.0F;
   private static final float с4с = 16.0F;
   private static final float с4В = 16.0F;
   private static final int с4Я = Integer.MAX_VALUE;
   private static final float с4Х = 9.0F;
   private static final float с4е = 17.0F;
   private static final float с4А = 17.0F;
   private static final float с44 = 36.0F;
   private static final float с4п = 3.0F;
   private static final float с4Д = 16.0F;
   private static final float с4Ъ = 15.0F;
   private static final float с4т = 36.0F;
   private static final float с4а = 4.0F;
   private static final float с4Ю = 16.0F;
   private static final float с4Ч = 14.0F;
   private static final float с4Л = 255.0F;
   private static final float с4诶 = 255.0F;
   private static final double с41 = 255.0;
   private static final float с4Ь = 3.0F;
   private static final float с4衣 = 3.0F;
   private static final float с4ш = 3.0F;
   private static final float с4И = 3.0F;
   private static final float с4ь = 255.0F;
   private static final float с4ж = 255.0F;
   private static final float с4弟 = 255.0F;
   private static final float с4б = 255.0F;
   private static final float с4Ы = 0.12F;
   private static final String с4l = "This is a utility class and cannot be instantiated";

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier texture) {
      renderer.drawAtlasTexture(x, y, width, height, cornerRadii, uvRect, colors, texture);
   }

   public static void drawWindowBackground(float x, float y, float width, float height, float cornerRadius) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         boolean styledWindowsEnabled = PanelBackgroundRenderer.цр();
         boolean shadowEnabled = RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getHud().В().getValue();
         if (!styledWindowsEnabled && shadowEnabled) {
            drawShadow(x, y, width, height, сАШ, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW));
         }

         if (styledWindowsEnabled) {
            PanelBackgroundRenderer.в(x, y, width, height, cornerRadius);
         } else {
            PanelBackgroundRenderer.м(x, y, width, height, cornerRadius, true);
         }
      }
   }

   public static void drawText(FontRenderer font, String text, float x, float y, int fontSize, int argb) {
      if (fontSize > 0 && argb >>> 24 != 0 && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         renderer.drawText(font, NameProtectionTextRewriter.лШ(text), x, y, fontSize, argb);
      }
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, float cornerRadius, int argb) {
      drawOutline(x, y, width, height, thickness, new Vector4f(cornerRadius), argb);
   }

    public static void drawHudWindowBackground(float x, float y, float width, float height, float cornerRadius) {
       sg.hud.HudBackground.drawBatched(x, y, width, height, cornerRadius);
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, Identifier texture) {
      drawTexture(x, y, width, height, cornerRadii, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), colors, texture);
   }

   public static void drawAtlasItem(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item) {
      renderer.drawAtlasItem(x, y, width, height, cornerRadii, colors, item);
   }

   public static void drawCircularProgress(float x, float y, float diameter, float strokeWidth, float progress, int argb) {
      renderer.appendShape(x, y, diameter, diameter, Math.clamp(progress, 0.0F, 1.0F), 10, new Vector4f(strokeWidth), argb);
   }

   public static void drawText(FontRenderer font, Component text, float x, float y, int fontSize, int argb) {
      if (fontSize > 0 && argb >>> 24 != 0 && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         renderer.drawText(font, NameProtectionTextRewriter.Е(text), x, y, fontSize, argb);
      }
   }

   public static void endBatch(FontRenderer... fonts) {
      renderer.endBatch(fonts);
   }

   public static void drawTextureFlipped(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, GpuTextureView textureView) {
      renderer.drawTextureFlipped(x, y, width, height, cornerRadii, uvRect, colors, textureView);
   }

   public static void drawShadow(float x, float y, float width, float height, Vector4f cornerRadii, int argb) {
      float maxCornerRadius = Math.max(Math.max(cornerRadii.x, cornerRadii.y), Math.max(cornerRadii.z, cornerRadii.w));
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(maxCornerRadius <= 0.0F)) {
         Vector4f rgba = ColorMath.toRgbaVector(argb);
         int red = (int)(rgba.x * с4ь);
         int green = (int)(rgba.y * с4ж);
         int blue = (int)(rgba.z * с4弟);
         float baseAlpha = rgba.w * с4б;

         for (int expansionPixels = (int)maxCornerRadius; expansionPixels > 0; expansionPixels--) {
            float falloff = 1.0F - expansionPixels / maxCornerRadius;
            int layerAlpha = (int)(baseAlpha * с4Ы * falloff * falloff);
            if (layerAlpha > 0) {
               int layerArgb = ColorMath.packArgb(red, green, blue, layerAlpha);
               float expansion = expansionPixels;
               renderer.appendShape(
                  x - expansion,
                  y - expansion,
                  width + expansion * 2.0F,
                  height + expansion * 2.0F,
                  0.0F,
                  7,
                  new Vector4f(cornerRadii).add(expansion, expansion, expansion, expansion),
                  new Vector4i(layerArgb)
               );
            }
         }
      }
   }

   public static void flushBatch(FontRenderer... fonts) {
      renderer.flush(fonts);
   }

   public static void drawScaledItem(ItemStack item, float x, float y, float scale, boolean drawDecorations, RoseBatchedItemAtlas.ItemHandle itemHandle) {
      if (item != null && !item.isEmpty() && !(scale <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         drawItemWithOverlay(x, y, сАП * scale, сА西 * scale, item, false, itemHandle);
         if (drawDecorations) {
            if (item.isBarVisible()) {
               drawRoundedRect(x + 2.0F * scale, y + сАд * scale, сАБ * scale, 2.0F * scale, 0.0F, сАС);
               drawRoundedRect(x + 2.0F * scale, y + сАз * scale, item.getBarWidth() * scale, scale, 0.0F, сАц | item.getBarColor());
            }

            if (MINECRAFT_CLIENT.player != null) {
               float cooldownProgress = MINECRAFT_CLIENT.player.getCooldowns().getCooldownPercent(item, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
               if (cooldownProgress > 0.0F) {
                  drawRoundedRect(x, y + сАя * scale * (1.0F - cooldownProgress), с4с * scale, с4В * scale * cooldownProgress, 0.0F, с4Я);
               }
            }

            if (item.getCount() != 1) {
               String countText = Integer.toString(item.getCount());
               int countFontSize = Math.max(7, Math.round(с4Х * scale));
               float countTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), countText, countFontSize);
               drawText(ClientFonts.getMediumFont(), countText, x + с4е * scale - countTextWidth, y + с4А * scale - countFontSize, countFontSize, -1);
            }
         }
      }
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, Vector4i colors) {
      drawRoundedRect(x, y, width, height, new Vector4f(cornerRadius), colors);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, int argb) {
      drawRoundedRect(x, y, width, height, new Vector4f(cornerRadius), new Vector4i(argb));
   }

   public static void drawCircularProgress(float x, float y, float diameter, float strokeWidth, float progress, Vector4i colors) {
      renderer.appendShape(x, y, diameter, diameter, Math.clamp(progress, 0.0F, 1.0F), 10, new Vector4f(strokeWidth), colors);
   }

   public static void beginBatch(FontRenderer... fonts) {
      renderer.beginBatch(fonts);
   }

   public static void drawItem(
      float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item, RoseBatchedItemAtlas.ItemHandle itemHandle
   ) {
      renderer.drawItem(x, y, width, height, cornerRadii, colors, item, itemHandle);
   }

   @Generated
   private UiBatchDrawing() {
      throw new UnsupportedOperationException(с4l);
   }

   public static void drawItemWithLegacyOverlay(float x, float y, float width, float height, ItemStack item, boolean drawDecorations) {
      drawAtlasItem(x, y, width, height, new Vector4f(0.0F), new Vector4i(-1), item);
      if (drawDecorations) {
         if (item.getCount() > 1) {
            drawText(
               ClientFonts.getMediumFont(),
               item.getCount() + "",
               x + с44 - с4п - ClientFonts.getMediumFont().getFontFace().measureTextWidth(item.getCount() + "", с4Д, 0.0F),
               y + с4Ъ,
               16,
               ColorMath.packRgb(0, 0, 0)
            );
            drawText(
               ClientFonts.getMediumFont(),
               item.getCount() + "",
               x + с4т - с4а - ClientFonts.getMediumFont().getFontFace().measureTextWidth(item.getCount() + "", с4Ю, 0.0F),
               y + с4Ч,
               16,
               -1
            );
         }

         if (item.isDamageableItem() && item.isDamaged()) {
            float durabilityFraction = (float)(item.getMaxDamage() - item.getDamageValue()) / item.getMaxDamage();
            int durabilityBarWidth = Math.round(width * durabilityFraction);
            int durabilityArgb = ColorMath.packArgb((int)(с4Л * (1.0F - durabilityFraction)), (int)(с4诶 * durabilityFraction), 0, с41);
            drawRoundedRect(x, y + height - с4Ь, width, с4衣, 0.0F, ColorMath.packRgb(0, 0, 0));
            drawRoundedRect(x, y + height - с4ш, durabilityBarWidth, с4И, 0.0F, durabilityArgb);
         }
      }
   }

   public static void setAdditiveBlending(boolean enabled) {
      renderer.setAdditiveBlend(enabled);
   }

   public static void drawItem(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item) {
      renderer.drawItem(x, y, width, height, cornerRadii, colors, item);
   }

   public static void drawItemWithOverlay(float x, float y, float width, float height, ItemStack item, boolean drawDecorations, RoseBatchedItemAtlas.ItemHandle itemHandle) {
      if (item != null && !item.isEmpty() && !(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         if (itemHandle == null) {
            drawAtlasItem(x, y, width, height, new Vector4f(0.0F), new Vector4i(-1), item);
         } else {
            renderer.drawAtlasItem(x, y, width, height, new Vector4f(0.0F), new Vector4i(-1), item, itemHandle);
         }

         if (drawDecorations) {
            if (item.getCount() > 1) {
               drawText(
                  ClientFonts.getMediumFont(),
                  item.getCount() + "",
                  x + width / 2.0F - ClientFonts.getMediumFont().getFontFace().measureTextWidth(item.getCount() + "", сАФ, 0.0F) / 2.0F,
                  y - сАЙ,
                  11,
                  -1
               );
            }

            if (item.isDamageableItem() && item.isDamaged()) {
               float durabilityFraction = (float)(item.getMaxDamage() - item.getDamageValue()) / item.getMaxDamage();
               int durabilityBarWidth = Math.round(width * durabilityFraction);
               int durabilityArgb = ColorMath.packArgb((int)(сАк * (1.0F - durabilityFraction)), (int)(сАг * durabilityFraction), 0, сА0);
               drawRoundedRect(x, y + height - сАх, width, сАУ, 0.0F, ColorMath.packRgb(0, 0, 0));
               drawRoundedRect(x, y + height - сАЗ, durabilityBarWidth, сАЕ, 0.0F, durabilityArgb);
            }
         }
      }
   }

   public static void discardBatch() {
      renderer.reset();
   }

   public static void finishBatch() {
      renderer.flushAndReset();
   }

   /** Draw pending HUD geometry without losing the surrounding batch's font slots/depth. */
   public static void flushPendingGeometry() {
      renderer.flushPendingGeometry();
   }

   public static void drawTextureNearestFlipped(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, GpuTextureView textureView) {
      renderer.drawTextureNearestFlipped(x, y, width, height, cornerRadii, uvRect, colors, textureView);
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, float cornerRadius, Vector4i colors) {
      drawBlur(x, y, width, height, blurRadius, new Vector4f(cornerRadius), colors);
   }

   public static void drawShadow(float x, float y, float width, float height, float cornerRadius, int argb) {
      drawShadow(x, y, width, height, new Vector4f(cornerRadius), argb);
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, Vector4f cornerRadii, Vector4i colors) {
      renderer.appendShape(x, y, width, height, blurRadius, 9, cornerRadii, colors);
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f cornerRadii, int argb) {
      drawRoundedRect(x, y, width, height, cornerRadii, new Vector4i(argb));
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, Vector4f cornerRadii, Vector4i colors) {
      renderer.appendShape(x, y, width, height, thickness, 8, cornerRadii, colors);
   }

   public static void drawLayeredWindowBackground(float x, float y, float width, float height, float cornerRadius) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         Hud hud = RoseClient.getInstance().getServices().getModules().getHud();
         boolean styledWindowsEnabled = hud.getGlassSetting().getValue();
         boolean windowBlurEnabled = hud.Г_3276742a().getValue();
         Vector4f cornerRadii = new Vector4f(Math.clamp(cornerRadius, 0.0F, Math.min(width, height) * сАй));
         if (styledWindowsEnabled || hud.В().getValue()) {
            float shadowRadius = styledWindowsEnabled ? сАЖ : Math.max(сАм, cornerRadius * сАК);
            int shadowArgb = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW);
            if (styledWindowsEnabled) {
               shadowArgb = ColorMath.withAlpha(shadowArgb, сАI);
            }

            renderer.appendShape(x - shadowRadius, y - shadowRadius, width + shadowRadius * 2.0F, height + shadowRadius * 2.0F, shadowRadius, 12, cornerRadii, shadowArgb);
         }

         if (styledWindowsEnabled || windowBlurEnabled) {
            renderer.appendShape(x, y, width, height, styledWindowsEnabled ? сАТ : сАО, 11, cornerRadii, -1);
         }

         int backgroundArgb = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND);
         if (windowBlurEnabled && !styledWindowsEnabled) {
            backgroundArgb = ColorMath.packArgb(
               ColorMath.getRed(backgroundArgb), ColorMath.getGreen(backgroundArgb), ColorMath.getBlue(backgroundArgb), Math.max(230, ColorMath.getAlpha(backgroundArgb))
            );
         }

         Vector4i backgroundColors = styledWindowsEnabled ? PanelBackgroundRenderer.м(ThemeColorSlot.WINDOW_BACKGROUND) : new Vector4i(backgroundArgb);
         renderer.appendShape(x, y, width, height, 0.0F, 7, cornerRadii, backgroundColors);
      }
   }

   public static void drawOutline(float x, float y, float width, float height, float thickness, Vector4f cornerRadii, int argb) {
      drawOutline(x, y, width, height, thickness, cornerRadii, new Vector4i(argb));
   }

   public static void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, GpuTextureView textureView) {
      renderer.drawTexture(x, y, width, height, cornerRadii, colors, textureView);
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, Vector4f cornerRadii, int argb) {
      drawBlur(x, y, width, height, blurRadius, cornerRadii, new Vector4i(argb));
   }

   public static void drawBlur(float x, float y, float width, float height, float blurRadius, float cornerRadius, int argb) {
      drawBlur(x, y, width, height, blurRadius, new Vector4f(cornerRadius), new Vector4i(argb));
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors) {
      renderer.appendShape(x, y, width, height, 0.0F, 7, cornerRadii, colors);
   }

   public static void drawFadedText(FontRenderer font, Component text, float x, float y, int fontSize, int argb, float fadeEndOffset, float fadeWidth) {
      if (fontSize > 0 && argb >>> 24 != 0 && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         renderer.drawFadingText(font, NameProtectionTextRewriter.Е(text), x, y, fontSize, argb, fadeEndOffset, fadeWidth);
      }
   }

   public static void drawItemWithOverlay(float x, float y, float width, float height, ItemStack item, boolean drawDecorations) {
      drawItemWithOverlay(x, y, width, height, item, drawDecorations, null);
   }
}
