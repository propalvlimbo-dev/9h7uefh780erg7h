package sg.render;

import net.minecraft.resources.Identifier;

/**
 * Светящийся слой косметики Rose: поверх обычной модели рисуется чуть увеличенная
 * «оболочка» с раскалённой палитрой, яркость которой плавно пульсирует.
 * Текстуры повторяют развёртку базовых моделей, поэтому геометрия совпадает.
 */
public final class CosmeticGlow {
   public static final Identifier HAT = Identifier.fromNamespaceAndPath("rose", "textures/cosmetics/rose_hat_glow.png");
   public static final Identifier WINGS = Identifier.fromNamespaceAndPath("rose", "textures/wings/rose_wings_glow.png");
   private static final float PERIOD_NANOS = 2.6E9F;
   private static final float MIN_PULSE = 0.26F;
   private static final float PULSE_SPAN = 0.34F;
   private static final float MIN_SHELL = 1.03F;
   private static final float SHELL_SPAN = 0.022F;
   private static final float GLOW_RED = 1.0F;
   private static final float GLOW_GREEN = 0.52F;
   private static final float GLOW_BLUE = 0.66F;

   private CosmeticGlow() {
   }

   /** 0.26 .. 0.60 — мягкая пульсация альфы. */
   public static float pulse() {
      float phase = (float)(System.nanoTime() % (long)PERIOD_NANOS) / PERIOD_NANOS;
      return MIN_PULSE + PULSE_SPAN * (0.5F - 0.5F * (float)Math.cos(phase * 6.2831855F));
   }

   /** Масштаб оболочки: 1.03 .. 1.052, чтобы не было z-fighting с базовой моделью. */
   public static float shellScale() {
      return MIN_SHELL + SHELL_SPAN * pulse() / PULSE_SPAN;
   }

   public static float red() {
      return GLOW_RED;
   }

   public static float green() {
      return GLOW_GREEN;
   }

   public static float blue() {
      return GLOW_BLUE;
   }
}
