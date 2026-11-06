package sg.gui.third;

import sg.ec.ClientFonts;
import sg.ec.FontRenderer;

/** Third uses the same text fonts as the First GUI; the icon atlas stays independent. */
public final class ThirdGuiFonts {
   private ThirdGuiFonts() {
   }

   public static FontRenderer semibold() {
      return ClientFonts.getMediumFont();
   }

   public static FontRenderer regular() {
      return ClientFonts.getRegularFont();
   }
}
