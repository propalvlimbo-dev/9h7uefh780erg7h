package sg.theme;

import net.minecraft.resources.Identifier;
import settings.render.ClientResourceIds;
import sg.util.time.ColorMath;

public enum MainMenuTheme {
   CLASSIC("classic", "Классика", false, false, 175, 210, 255, 232, 64, 64);

   private final @com.google.gson.annotations.SerializedName("ВН")  String id;
   private final @com.google.gson.annotations.SerializedName("Вн")  String displayName;
   private final @com.google.gson.annotations.SerializedName("Яг")  boolean fullWhiteAlpha;
   private final @com.google.gson.annotations.SerializedName("Я0")  boolean darkPalette;
   private final @com.google.gson.annotations.SerializedName("АО")  int primaryRed;
   private final @com.google.gson.annotations.SerializedName("АФ")  int primaryGreen;
   private final @com.google.gson.annotations.SerializedName("АЙ")  int primaryBlue;
   private final @com.google.gson.annotations.SerializedName("Ак")  int secondaryRed;
   private final @com.google.gson.annotations.SerializedName("Аг")  int secondaryGreen;
   private final @com.google.gson.annotations.SerializedName("А0")  int secondaryBlue;

   MainMenuTheme(String id, String displayName, boolean fullWhiteAlpha, boolean darkPalette, int primaryRed, int primaryGreen, int primaryBlue, int secondaryRed, int secondaryGreen, int secondaryBlue) {
      this.id = id;
      this.displayName = displayName;
      this.fullWhiteAlpha = fullWhiteAlpha;
      this.darkPalette = darkPalette;
      this.primaryRed = primaryRed;
      this.primaryGreen = primaryGreen;
      this.primaryBlue = primaryBlue;
      this.secondaryRed = secondaryRed;
      this.secondaryGreen = secondaryGreen;
      this.secondaryBlue = secondaryBlue;
   }

   public String getId() {
      return this.id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public boolean usesFullWhiteAlpha() {
      return this.fullWhiteAlpha;
   }

   public boolean usesDarkPalette() {
      return this.darkPalette;
   }

   public Identifier getImageId() {
      return this == CLASSIC ? ClientResourceIds.getImageId("mainmenu/legacy/img") : ClientResourceIds.getImageId("mainmenu/themes/" + this.id);
   }

   public int getWhiteColor(float opacityScale) {
      return ColorMath.packArgb(255, 255, 255, (this.fullWhiteAlpha ? 255.0F : 40.4F) * opacityScale);
   }

   public int getDarkOrPaleColor(float opacityScale) {
      return this.darkPalette ? ColorMath.packArgb(18, 22, 28, 235.0F * opacityScale) : ColorMath.packArgb(224, 243, 252, 225.0F * opacityScale);
   }

   public int getSlatePaletteColor(float opacityScale) {
      return this.darkPalette ? ColorMath.packArgb(42, 48, 58, 230.0F * opacityScale) : ColorMath.packArgb(87, 90, 105, 220.0F * opacityScale);
   }

   public int getSlateVariantColor(float opacityScale) {
      return this.darkPalette ? ColorMath.packArgb(36, 42, 52, 230.0F * opacityScale) : ColorMath.packArgb(96, 101, 116, 220.0F * opacityScale);
   }

   public int getDarkVariantColor(boolean useLighterVariant, float opacityScale) {
      float alpha = (this.darkPalette ? 228.0F : 155.0F) * opacityScale;
      return useLighterVariant ? ColorMath.packArgb(25, 27, 35, alpha) : ColorMath.packArgb(15, 16, 21, alpha);
   }

   public int getPalePrimaryBlendColor(float opacityScale, float secondaryAlphaScale) {
      float alpha = (this.darkPalette ? 165.0F : 58.0F) * opacityScale * secondaryAlphaScale;
      return ColorMath.packArgb(interpolateChannel(255, this.primaryRed, 0.35F), interpolateChannel(255, this.primaryGreen, 0.35F), interpolateChannel(255, this.primaryBlue, 0.35F), alpha);
   }

   public int getNearBlackColor(float opacityScale, float secondaryAlphaScale) {
      return ColorMath.packArgb(7, 9, 14, (this.darkPalette ? 18.0F : 42.0F) * opacityScale * secondaryAlphaScale);
   }

   public int getLightOrSlateColor(float opacityScale) {
      return this.darkPalette ? ColorMath.packArgb(198, 204, 214, 255.0F * opacityScale) : ColorMath.packArgb(87, 90, 105, 255.0F * opacityScale);
   }

   public int getLightGrayColor(float opacityScale) {
      return ColorMath.packArgb(225, 230, 240, 255.0F * opacityScale);
   }

   public int getMutedPrimaryBlendColor(float opacityScale) {
      return this.darkPalette
         ? ColorMath.packArgb(interpolateChannel(150, this.primaryRed, 0.45F), interpolateChannel(155, this.primaryGreen, 0.45F), interpolateChannel(165, this.primaryBlue, 0.45F), 255.0F * opacityScale)
         : ColorMath.packArgb(52, 54, 62, 255.0F * opacityScale);
   }

   public int getPrimaryColor(float opacityScale) {
      return ColorMath.packArgb(this.primaryRed, this.primaryGreen, this.primaryBlue, 255.0F * opacityScale);
   }

   public int getSecondaryOrDarkGrayColor(float opacityScale) {
      return this.darkPalette ? ColorMath.packArgb(this.secondaryRed, this.secondaryGreen, this.secondaryBlue, 235.0F * opacityScale) : ColorMath.packArgb(52, 54, 62, 255.0F * opacityScale);
   }

   public int getBrightSecondaryColor(float opacityScale) {
      return ColorMath.packArgb(Math.min(255, this.secondaryRed + 20), Math.min(255, this.secondaryGreen + 12), Math.min(255, this.secondaryBlue + 12), 255.0F * opacityScale);
   }

   public static MainMenuTheme findById(String id) {
      if (id != null && !id.isBlank() && !id.equalsIgnoreCase("wheat")) {
         for (MainMenuTheme theme : values()) {
            if (theme.id.equalsIgnoreCase(id)) {
               return theme;
            }
         }

         return CLASSIC;
      } else {
         return CLASSIC;
      }
   }

   private static int interpolateChannel(int startChannel, int endChannel, float blendFactor) {
      return Math.round(startChannel + (endChannel - startChannel) * blendFactor);
   }
}
