package sg.util.math;

public class EasingFunctions {
   public static final double BACK_OVERSHOOT = 1.70158;
   public static final double BACK_IN_OUT_OVERSHOOT = 2.5949095;
   public static final double BACK_PLUS_ONE = 2.70158;
   public static final double ELASTIC_ANGULAR_FACTOR = Math.PI * 2.0 / 3.0;
   public static final double ELASTIC_IN_OUT_ANGULAR_FACTOR = Math.PI * 4.0 / 9.0;
   public static final EasingFunctions.Easing SINE_IN = progress -> (float)(1.0 - Math.cos(progress * EasingFunctions.рщ / EasingFunctions.рГ));
   public static final EasingFunctions.Easing SINE_OUT = progress -> (float)Math.sin(progress * EasingFunctions.рЭ / EasingFunctions.р必);
   public static final EasingFunctions.Easing SINE_IN_OUT = progress -> (float)(-(Math.cos(EasingFunctions.рЦ * progress) - 1.0) / EasingFunctions.рч);
   public static final EasingFunctions.Easing CIRC_IN = progress -> (float)(1.0 - Math.sqrt(1.0 - Math.pow(progress, EasingFunctions.рн)));
   public static final EasingFunctions.Easing CIRC_OUT = progress -> (float)Math.sqrt(1.0 - Math.pow(progress - 1.0, EasingFunctions.рН));
   public static final EasingFunctions.Easing CIRC_IN_OUT = progress -> (float)(
      progress < EasingFunctions.рб
         ? (1.0 - Math.sqrt(1.0 - Math.pow(EasingFunctions.рЫ * progress, EasingFunctions.рl))) / EasingFunctions.р8
         : (Math.sqrt(1.0 - Math.pow(EasingFunctions.р5 * progress + EasingFunctions.рв, EasingFunctions.ръ)) + 1.0) / EasingFunctions.рМ
   );
   public static final EasingFunctions.Easing ELASTIC_IN = progress -> progress != 0.0 && progress != 1.0
      ? (float)(
         Math.pow(EasingFunctions.р衣, EasingFunctions.рш * progress - EasingFunctions.рИ)
            * Math.sin((progress * EasingFunctions.рь - EasingFunctions.рж) * EasingFunctions.р弟)
      )
      : progress;
   public static final EasingFunctions.Easing ELASTIC_OUT = progress -> progress != 0.0 && progress != 1.0
      ? (float)(
         Math.pow(EasingFunctions.рЧ, EasingFunctions.рЛ * progress) * Math.sin((progress * EasingFunctions.р诶 - EasingFunctions.р1) * EasingFunctions.рЬ) + 1.0
      )
      : progress;
   public static final EasingFunctions.Easing ELASTIC_IN_OUT = progress -> progress != 0.0 && progress != 1.0
      ? (float)(
         progress < EasingFunctions.field_3ц
            ? -(
                  Math.pow(EasingFunctions.field_3я, EasingFunctions.рс * progress - EasingFunctions.рВ)
                     * Math.sin((EasingFunctions.рЯ * progress - EasingFunctions.рХ) * EasingFunctions.ре)
               )
               / EasingFunctions.рА
            : Math.pow(EasingFunctions.р4, EasingFunctions.рп * progress + EasingFunctions.рД)
                  * Math.sin((EasingFunctions.рЪ * progress - EasingFunctions.рт) * EasingFunctions.ра)
                  / EasingFunctions.рЮ
               + 1.0
      )
      : progress;
   public static final EasingFunctions.Easing EXPO_IN = progress -> progress != 0.0
      ? (float)Math.pow(EasingFunctions.field_3Б, EasingFunctions.field_3С * progress - EasingFunctions.field_3з)
      : progress;
   public static final EasingFunctions.Easing EXPO_OUT = progress -> progress != 1.0
      ? (float)(1.0 - Math.pow(EasingFunctions.field_3西, EasingFunctions.field_3д * progress))
      : progress;
   public static final EasingFunctions.Easing EXPO_IN_OUT = progress -> progress != 0.0 && progress != 1.0
      ? (float)(
         progress < EasingFunctions.field_3Ф
            ? Math.pow(EasingFunctions.field_3Й, EasingFunctions.field_3к * progress - EasingFunctions.field_3г) / EasingFunctions.field_30
            : (EasingFunctions.field_3х - Math.pow(EasingFunctions.field_3У, EasingFunctions.field_3З * progress + EasingFunctions.field_3Е))
               / EasingFunctions.field_3П
      )
      : progress;
   public static final EasingFunctions.Easing BACK_IN = progress -> (float)(
      EasingFunctions.field_3К * Math.pow(progress, EasingFunctions.field_3I) - EasingFunctions.field_3Т * Math.pow(progress, EasingFunctions.field_3О)
   );
   public static final EasingFunctions.Easing BACK_OUT = progress -> (float)(
      1.0
         + EasingFunctions.field_3ы * Math.pow(progress - 1.0, EasingFunctions.field_3й)
         + EasingFunctions.field_3Ж * Math.pow(progress - 1.0, EasingFunctions.field_3м)
   );
   public static final EasingFunctions.Easing LINEAR = progress -> progress;
   public static final EasingFunctions.Easing BACK_IN_OUT = progress -> (float)(
      progress < EasingFunctions.field_3Щ
         ? Math.pow(EasingFunctions.field_3у * progress, EasingFunctions.field_32)
            * (EasingFunctions.field_3ф * progress - EasingFunctions.field_3и)
            / EasingFunctions.field_39
         : (
               Math.pow(EasingFunctions.field_3о * progress - EasingFunctions.field_3O, EasingFunctions.field_37)
                     * (EasingFunctions.field_3Р * (progress * EasingFunctions.field_33 - EasingFunctions.field_3р) + EasingFunctions.field_36)
                  + EasingFunctions.field_3_
            )
            / EasingFunctions.field_3Ш
   );
   public static final EasingFunctions.Easing BOUNCE_OUT = progress -> {
      float bounceCoefficient = EasingFunctions.field_3в;
      float bounceIntervalDivisor = EasingFunctions.field_3ъ;
      if (progress < 1.0 / bounceIntervalDivisor) {
         return (float)(bounceCoefficient * Math.pow(progress, EasingFunctions.field_3М));
      } else {
         return progress < EasingFunctions.field_3Н / bounceIntervalDivisor
            ? (float)(bounceCoefficient * Math.pow(progress - EasingFunctions.field_3н / bounceIntervalDivisor, EasingFunctions.field_3Ц) + EasingFunctions.field_3ч)
            : (float)(
               progress < EasingFunctions.field_3Э / bounceIntervalDivisor
                  ? bounceCoefficient * Math.pow(progress - EasingFunctions.field_3必 / bounceIntervalDivisor, EasingFunctions.field_3щ) + EasingFunctions.field_3Г
                  : bounceCoefficient * Math.pow(progress - EasingFunctions.field_3л / bounceIntervalDivisor, EasingFunctions.field_3э) + EasingFunctions.field_3ю
            );
      }
   };
   public static final EasingFunctions.Easing BOUNCE_IN = progress -> (float)(1.0 - BOUNCE_OUT.apply((float)(1.0 - progress)));
   public static final EasingFunctions.Easing BOUNCE_IN_OUT = progress -> (float)(
      progress < EasingFunctions.field_3б
         ? (1.0 - BOUNCE_OUT.apply((float)(1.0 - EasingFunctions.field_3Ы * progress))) / EasingFunctions.field_3l
         : (1.0 + BOUNCE_OUT.apply((float)(EasingFunctions.field_38 * progress - 1.0))) / EasingFunctions.field_35
   );
   public static final EasingFunctions.Easing QUINT_IN_OUT = progress -> progress < EasingFunctions.field_3ш
      ? EasingFunctions.field_3И * progress * progress * progress * progress * progress
      : (float)(1.0 - Math.pow(EasingFunctions.field_3ь * progress + 2.0F, EasingFunctions.field_3ж) / EasingFunctions.field_3弟);
   public static final EasingFunctions.Easing CUBIC_IN_OUT = progress -> (float)(
      progress < EasingFunctions.field_3Ч
         ? EasingFunctions.field_3Л * progress * progress * progress
         : (progress - 1.0) * (EasingFunctions.field_3诶 * progress - EasingFunctions.field_31) * (EasingFunctions.field_3Ь * progress - EasingFunctions.field_3衣) + 1.0
   );
   private static final double field_3Ч = 0.5;
   private static final double field_3Л = 4.0;
   private static final double field_3诶 = 2.0;
   private static final double field_31 = 2.0;
   private static final double field_3Ь = 2.0;
   private static final double field_3衣 = 2.0;
   private static final double field_3ш = 0.5;
   private static final float field_3И = 16.0F;
   private static final float field_3ь = -2.0F;
   private static final double field_3ж = 5.0;
   private static final double field_3弟 = 2.0;
   private static final double field_3б = 0.5;
   private static final double field_3Ы = 2.0;
   private static final double field_3l = 2.0;
   private static final double field_38 = 2.0;
   private static final double field_35 = 2.0;
   private static final float field_3в = 7.5625F;
   private static final float field_3ъ = 2.75F;
   private static final double field_3М = 2.0;
   private static final double field_3Н = 2.0;
   private static final double field_3н = 1.5;
   private static final double field_3Ц = 2.0;
   private static final double field_3ч = 0.75;
   private static final double field_3Э = 2.5;
   private static final double field_3必 = 2.25;
   private static final double field_3щ = 2.0;
   private static final double field_3Г = 0.9375;
   private static final double field_3л = 2.625;
   private static final double field_3э = 2.0;
   private static final double field_3ю = 0.984375;
   private static final double field_3Щ = 0.5;
   private static final double field_3у = 2.0;
   private static final double field_32 = 2.0;
   private static final double field_3ф = 7.189819;
   private static final double field_3и = 2.5949095;
   private static final double field_39 = 2.0;
   private static final double field_3о = 2.0;
   private static final double field_3O = 2.0;
   private static final double field_37 = 2.0;
   private static final double field_3Р = 3.5949095;
   private static final double field_33 = 2.0;
   private static final double field_3р = 2.0;
   private static final double field_36 = 2.5949095;
   private static final double field_3_ = 2.0;
   private static final double field_3Ш = 2.0;
   private static final double field_3ы = 2.70158;
   private static final double field_3й = 3.0;
   private static final double field_3Ж = 1.70158;
   private static final double field_3м = 2.0;
   private static final double field_3К = 2.70158;
   private static final double field_3I = 3.0;
   private static final double field_3Т = 1.70158;
   private static final double field_3О = 2.0;
   private static final double field_3Ф = 0.5;
   private static final double field_3Й = 2.0;
   private static final double field_3к = 20.0;
   private static final double field_3г = 10.0;
   private static final double field_30 = 2.0;
   private static final double field_3х = 2.0;
   private static final double field_3У = 2.0;
   private static final double field_3З = -20.0;
   private static final double field_3Е = 10.0;
   private static final double field_3П = 2.0;
   private static final double field_3西 = 2.0;
   private static final double field_3д = -10.0;
   private static final double field_3Б = 2.0;
   private static final double field_3С = 10.0;
   private static final double field_3з = 10.0;
   private static final double field_3ц = 0.5;
   private static final double field_3я = 2.0;
   private static final double рс = 20.0;
   private static final double рВ = 10.0;
   private static final double рЯ = 20.0;
   private static final double рХ = 11.125;
   private static final double ре = Math.PI * 4.0 / 9.0;
   private static final double рА = 2.0;
   private static final double р4 = 2.0;
   private static final double рп = -20.0;
   private static final double рД = 10.0;
   private static final double рЪ = 20.0;
   private static final double рт = 11.125;
   private static final double ра = Math.PI * 4.0 / 9.0;
   private static final double рЮ = 2.0;
   private static final double рЧ = 2.0;
   private static final double рЛ = -10.0;
   private static final double р诶 = 10.0;
   private static final double р1 = 0.75;
   private static final double рЬ = Math.PI * 2.0 / 3.0;
   private static final double р衣 = -2.0;
   private static final double рш = 10.0;
   private static final double рИ = 10.0;
   private static final double рь = 10.0;
   private static final double рж = 10.75;
   private static final double р弟 = Math.PI * 2.0 / 3.0;
   private static final double рб = 0.5;
   private static final double рЫ = 2.0;
   private static final double рl = 2.0;
   private static final double р8 = 2.0;
   private static final double р5 = -2.0;
   private static final double рв = 2.0;
   private static final double ръ = 2.0;
   private static final double рМ = 2.0;
   private static final double рН = 2.0;
   private static final double рн = 2.0;
   private static final double рЦ = Math.PI;
   private static final double рч = 2.0;
   private static final double рЭ = Math.PI;
   private static final double р必 = 2.0;
   private static final double рщ = Math.PI;
   private static final double рГ = 2.0;

   @FunctionalInterface
   public interface Easing {
      float apply(float progress);
   }
}
