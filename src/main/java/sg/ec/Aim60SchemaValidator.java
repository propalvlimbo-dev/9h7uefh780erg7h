package sg.ec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sg.rotation.HumanAimPolicy;

public final class Aim60SchemaValidator {
   public static final int ВЦ = 30;
   public static final int Вч = 0;
   public static final int ВЭ = 12;
   public static final int В必 = 13;
   private static final String[] Д = new String[]{
      Aim60SchemaValidator.ВкЮ,
      Aim60SchemaValidator.ВкЧ,
      Aim60SchemaValidator.ВкЛ,
      Aim60SchemaValidator.Вк诶,
      Aim60SchemaValidator.Вк1,
      Aim60SchemaValidator.ВкЬ,
      Aim60SchemaValidator.Вк衣,
      Aim60SchemaValidator.Вкш,
      Aim60SchemaValidator.ВкИ,
      Aim60SchemaValidator.Вкь,
      Aim60SchemaValidator.Вкж,
      Aim60SchemaValidator.Вк弟,
      Aim60SchemaValidator.Вкб,
      Aim60SchemaValidator.ВкЫ,
      Aim60SchemaValidator.Вкl,
      Aim60SchemaValidator.Вк8,
      Aim60SchemaValidator.Вк5,
      Aim60SchemaValidator.Вкв,
      Aim60SchemaValidator.Вкъ,
      Aim60SchemaValidator.ВкМ,
      Aim60SchemaValidator.ВкН,
      Aim60SchemaValidator.Вкн,
      Aim60SchemaValidator.ВкЦ,
      Aim60SchemaValidator.Вкч,
      Aim60SchemaValidator.ВкЭ,
      Aim60SchemaValidator.Вк必,
      Aim60SchemaValidator.Вкщ,
      Aim60SchemaValidator.ВкГ,
      Aim60SchemaValidator.Вкл,
      Aim60SchemaValidator.Вкэ
   };
   private static final String Вкт = "policy.bin has a different raw feature schema";
   private static final String Вка = "Expected 30 raw features from Aim60FeatureExtractor";
   private static final String ВкЮ = "sample_dt_ms";
   private static final String ВкЧ = "world_tick_changed";
   private static final String ВкЛ = "world_tick_phase";
   private static final String Вк诶 = "rel_pos_right";
   private static final String Вк1 = "rel_pos_up";
   private static final String ВкЬ = "rel_pos_forward";
   private static final String Вк衣 = "distance";
   private static final String Вкш = "rel_vel_right";
   private static final String ВкИ = "rel_vel_up";
   private static final String Вкь = "rel_vel_forward";
   private static final String Вкж = "aim_error_yaw";
   private static final String Вк弟 = "aim_error_pitch";
   private static final String Вкб = "yaw_velocity";
   private static final String ВкЫ = "pitch_velocity";
   private static final String Вкl = "target_relative_yaw_sin";
   private static final String Вк8 = "target_relative_yaw_cos";
   private static final String Вк5 = "target_on_ground";
   private static final String Вкв = "target_sprinting";
   private static final String Вкъ = "target_sneaking";
   private static final String ВкМ = "target_hurt_time";
   private static final String ВкН = "player_on_ground";
   private static final String Вкн = "player_sprinting";
   private static final String ВкЦ = "player_sneaking";
   private static final String Вкч = "input_forward";
   private static final String ВкЭ = "input_back";
   private static final String Вк必 = "input_left";
   private static final String Вкщ = "input_right";
   private static final String ВкГ = "input_jump";
   private static final String Вкл = "input_attack";
   private static final String Вкэ = "exact_crosshair";

   public static void л(HumanAimPolicy var0, int var1) {
      if (!Arrays.equals(Д, var0.getRawFeatureNames())) {
         throw new IllegalArgumentException(Вкт);
      }

      if (var1 != 30) {
         throw new IllegalArgumentException(
            "Aim60Schema.FEATURE_COUNT is " + var1 + "; policy.bin needs 30 raw schema-3 features. Adapt the extractor; do not pad or truncate."
         );
      }
   }

   public static Map<String, Float> л(float[] var0) {
      if (var0 != null && var0.length == 30) {
         HashMap var1 = new HashMap(64);

         for (int var2 = 0; var2 < Д.length; var2++) {
            if (!Float.isFinite(var0[var2])) {
               throw new IllegalArgumentException("Nonfinite feature: " + Д[var2]);
            }

            var1.put(Д[var2], var0[var2]);
         }

         return var1;
      } else {
         throw new IllegalArgumentException(Вка);
      }
   }

   private Aim60SchemaValidator() {
   }
}
