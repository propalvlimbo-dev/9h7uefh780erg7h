package sg.ec;

import sg.render.pipeline.UiDrawing;

public final class MessengerIcons {
   private static final String с1_ = "people";
   private static final String с1Ш = "chat";
   private static final String с1ы = "copy";
   private static final String с1й = "back";
   private static final String с1Ж = "person";
   private static final String с1м = "\ue110";
   private static final String с1К = "\ue111";
   private static final String с1I = "\ue112";
   private static final String с1Т = "\ue113";
   private static final String с1О = "W";
   private static final String с1Ф = "person";
   private static final float с1Й = -0.03076172F;
   private static final float с1к = -0.15625F;
   private static final float с1г = 0.5F;
   private static final String с10 = "reply";
   private static final String с1х = "forward";
   private static final String с1У = "pin";
   private static final String с1З = "edit";
   private static final String с1Е = "delete";
   private static final String с1П = "select";
   private static final String с1西 = "check";
   private static final String с1д = "checks";
   private static final String с1Б = "more";
   private static final String с1С = "compose";
   private static final String с1з = "search";
   private static final String с1ц = "send";
   private static final String с1я = "down";
   private static final String сЬс = "smile";
   private static final String сЬВ = "globe";
   private static final String сЬЯ = "close";
   private static final String сЬХ = "\ue001";
   private static final float сЬе = 0.06003662F;
   private static final String сЬА = "\ue002";
   private static final float сЬ4 = 0.11183838F;
   private static final String сЬп = "\ue003";
   private static final float сЬД = 0.17985685F;
   private static final String сЬЪ = "\ue005";
   private static final float сЬт = 0.13225F;
   private static final String сЬа = "\ue006";
   private static final float сЬЮ = 0.109375F;
   private static final String сЬЧ = "\ue007";
   private static final float сЬЛ = 0.0859375F;
   private static final String сЬ诶 = "\ue008";
   private static final float сЬ1 = 0.17206806F;
   private static final String сЬЬ = "\ue009";
   private static final float сЬ衣 = -0.00771308F;
   private static final String сЬш = "\ue00b";
   private static final float сЬИ = 0.0859375F;
   private static final String сЬь = "\ue00c";
   private static final float сЬж = 0.15971227F;
   private static final String сЬ弟 = "\ue00e";
   private static final float сЬб = 0.0850625F;
   private static final String сЬЫ = "\ue00f";
   private static final float сЬl = 0.08376926F;
   private static final String сЬ8 = "\ue010";
   private static final float сЬ5 = 0.15625F;
   private static final String сЬв = "\ue013";
   private static final float сЬъ = 0.046875F;
   private static final String сЬМ = "\ue014";
   private static final float сЬН = 0.0859375F;
   private static final String сЬн = "\ue015";
   private static final float сЬЦ = 0.1640625F;
   private static final float сЬч = 0.5F;

   private MessengerIcons() {
   }

   public static void ф(String var0, float var1, float var2, float var3, int var4) {
      String var5 = switch (var0) {
         case с1_ -> с1м;
         case с1Ш -> с1К;
         case с1ы -> с1I;
         case с1й -> с1Т;
         case с1Ж -> с1О;
         default -> null;
      };
      if (var5 != null) {
         float var10 = var0.equals(с1Ф) ? с1Й : с1к;
         UiDrawing.drawText(ClientFonts.getIconsFont(), var5, var1 + var10 * var3, var2, var3, var4, 0.0F, с1г, 1.0F);
      } else {
         MessengerIcons.IconGlyph var9 = switch (var0) {
            case с10 -> new MessengerIcons.IconGlyph(сЬХ, сЬе);
            case с1х -> new MessengerIcons.IconGlyph(сЬА, сЬ4);
            case с1У -> new MessengerIcons.IconGlyph(сЬп, сЬД);
            case с1З -> new MessengerIcons.IconGlyph(сЬЪ, сЬт);
            case с1Е -> new MessengerIcons.IconGlyph(сЬа, сЬЮ);
            case с1П -> new MessengerIcons.IconGlyph(сЬЧ, сЬЛ);
            case с1西 -> new MessengerIcons.IconGlyph(сЬ诶, сЬ1);
            case с1д -> new MessengerIcons.IconGlyph(сЬЬ, сЬ衣);
            case с1Б -> new MessengerIcons.IconGlyph(сЬш, сЬИ);
            case с1С -> new MessengerIcons.IconGlyph(сЬь, сЬж);
            case с1з -> new MessengerIcons.IconGlyph(сЬ弟, сЬб);
            case с1ц -> new MessengerIcons.IconGlyph(сЬЫ, сЬl);
            case с1я -> new MessengerIcons.IconGlyph(сЬ8, сЬ5);
            case сЬс -> new MessengerIcons.IconGlyph(сЬв, сЬъ);
            case сЬВ -> new MessengerIcons.IconGlyph(сЬМ, сЬН);
            case сЬЯ -> new MessengerIcons.IconGlyph(сЬн, сЬЦ);
            default -> throw new IllegalArgumentException("Unknown messenger icon: " + var0);
         };
         UiDrawing.drawText(ClientFonts.getMessengerIconsFont(), var9.text, var1 + var9.left * var3, var2, var3, var4, 0.0F, сЬч, 1.0F);
      }
   }

   private record IconGlyph(String text, float left) {
   }
}
