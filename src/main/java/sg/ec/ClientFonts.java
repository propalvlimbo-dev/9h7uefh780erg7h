package sg.ec;

public final class ClientFonts {
   public static FontRenderer getMainMenuIconsFont() {
      return ClientFonts.MainMenuIconsHolder.FONT;
   }

   public static FontRenderer getMediumFont() {
      return ClientFonts.MediumHolder.FONT;
   }

   public static FontRenderer getMainMenuTextFont() {
      return ClientFonts.MainMenuTextHolder.FONT;
   }

   public static FontRenderer getRegularFont() {
      return ClientFonts.RegularHolder.FONT;
   }

   public static FontRenderer getIconsFont() {
      return ClientFonts.IconsHolder.FONT;
   }

   private ClientFonts() {
   }

   public static FontRenderer getMessengerIconsFont() {
      return ClientFonts.MessengerIconsHolder.FONT;
   }

   private static final class MainMenuTextHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.MainMenuTextHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "mainmenu_inter";
   }

   private static final class IconsHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.IconsHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "icons";
   }

   private static final class RegularHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.RegularHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "regular";
   }

   private static final class MediumHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.MediumHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "medium";
   }

   private static final class MainMenuIconsHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.MainMenuIconsHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "mainmenu_icons";
   }

   private static final class MessengerIconsHolder {
      private static final FontRenderer FONT = new FontRenderer(ClientFonts.MessengerIconsHolder.RESOURCE_NAME);
      private static final String RESOURCE_NAME = "messenger_icons";
   }
}
