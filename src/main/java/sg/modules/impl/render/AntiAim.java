package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class AntiAim extends ClientModule {
   @SerializedName("9")
   private final SingleChoiceSetting field_9;
   private final SingleChoiceSetting о;
   private final FloatSetting ВВ;
   private final FloatSetting ВЯ;
   private final FloatSetting ВХ;
   private final FloatSetting Ве;
   private final FloatSetting ВА;
   private final FloatSetting В4;
   private final BooleanSetting сь;
   private float еЖ;
   private float ем;
   private float еК;
   private float еI;
   private float еТ;
   private long Вь;
   private long Вж;
   private static final String Хlф = "AntiAim";
   private static final String Хlи = "Визуальные антиаимы как в HVH читах";
   private static final String Хl9 = "Режим";
   private static final String Хlо = "Spin";
   private static final String ХlO = "Jitter";
   private static final String Хl7 = "Static";
   private static final String ХlР = "Sideways";
   private static final String Хl3 = "Backwards";
   private static final String Хlр = "Fake Down";
   private static final String Хl6 = "Fake Up";
   private static final String Хl_ = "Spin Pitch";
   private static final String ХlШ = "Random";
   private static final String Хlы = "Legit";
   private static final String Хlй = "Питч";
   private static final String ХlЖ = "Режима";
   private static final String Хlм = "Down";
   private static final String ХlК = "Up";
   private static final String ХlI = "Zero";
   private static final String ХlТ = "Custom";
   private static final String ХlО = "Jitter";
   private static final String ХlФ = "Скорость спина";
   private static final float ХlЙ = 40.0F;
   private static final float Хlк = 100.0F;
   private static final String Хlг = "Yaw оффсет";
   private static final float Хl0 = 180.0F;
   private static final float Хlх = -180.0F;
   private static final float ХlУ = 180.0F;
   private static final String ХlЗ = "Питч значение";
   private static final float ХlЕ = 89.0F;
   private static final float ХlП = -90.0F;
   private static final float Хl西 = 90.0F;
   private static final String Хlд = "Голова оффсет";
   private static final float ХlБ = -180.0F;
   private static final float ХlС = 180.0F;
   private static final String Хlз = "Jitter диапазон";
   private static final float Хlц = 45.0F;
   private static final float Хlя = 5.0F;
   private static final float Х8с = 180.0F;
   private static final String Х8В = "Смена (мс)";
   private static final float Х8Я = 120.0F;
   private static final float Х8Х = 20.0F;
   private static final float Х8е = 500.0F;
   private static final float Х8А = 10.0F;
   private static final String Х84 = "Только тело";
   private static final String Х8п = "Spin";
   private static final String Х8Д = "Jitter";
   private static final String Х8Ъ = "Static";
   private static final String Х8т = "Sideways";
   private static final String Х8а = "Backwards";
   private static final String Х8Ю = "Fake Down";
   private static final String Х8Ч = "Fake Up";
   private static final String Х8Л = "Spin Pitch";
   private static final String Х8诶 = "Random";
   private static final String Х81 = "Legit";
   private static final float Х8Ь = 0.45F;
   private static final float Х8衣 = 89.0F;
   private static final float Х8ш = 0.5F;
   private static final float Х8И = 0.5F;
   private static final float Х8ь = 0.15F;
   private static final float Х8ж = -89.0F;
   private static final float Х8弟 = 89.0F;
   private static final float Х8б = -90.0F;
   private static final float Х8Ы = 90.0F;
   private static final float Х8l = 180.0F;
   private static final float Х88 = 180.0F;
   private static final float Х85 = 89.0F;
   private static final float Х8в = 180.0F;
   private static final float Х8ъ = -89.0F;
   private static final float Х8М = 0.45F;
   private static final double Х8Н = 180.0;
   private static final float Х8н = 89.0F;
   private static final float Х8Ц = -180.0F;
   private static final float Х8ч = 180.0F;
   private static final float Х8Э = -89.0F;
   private static final float Х8必 = 89.0F;
   private static final float Х8щ = 30.0F;
   private static final float Х8Г = 89.0F;
   private static final float Х8л = -89.0F;
   private static final float Х8э = 35.0F;
   private static final float Х8ю = 0.35F;
   private static final double Х8Щ = 420.0;
   private static final float Х8у = 12.0F;
   private static final double Х82 = 510.0;
   private static final float Х8ф = 8.0F;
   private static final float Х8и = -45.0F;
   private static final float Х89 = 45.0F;
   private static final String Х8о = "Down";
   private static final String Х8O = "Up";
   private static final String Х87 = "Zero";
   private static final String Х8Р = "Custom";
   private static final String Х83 = "Jitter";
   private static final float Х8р = 89.0F;
   private static final float Х86 = -89.0F;
   private static final float Х8_ = -89.0F;
   private static final float Х8Ш = 89.0F;
   private static final String Х8ы = "Jitter";
   private static final String Х8й = "Random";
   private static final String Х8Ж = "Jitter";
   private static final String Х8м = "Jitter";
   private static final String Х8К = "Random";
   private static final String Х8I = "Jitter";
   private static final String Х8Т = "Custom";
   private static final String Х8О = "Static";
   private static final String Х8Ф = "Fake Down";
   private static final String Х8Й = "Fake Up";
   private static final String Х8к = "Static";
   private static final String Х8г = "Sideways";
   private static final String Х80 = "Backwards";
   private static final String Х8х = "Legit";
   private static final String Х8У = "Spin";
   private static final String Х8З = "Spin Pitch";
   private static final String Х8Е = "Jitter";

   public AntiAim() {
      super(Хlф, Хlи, ModuleCategory.RENDER);
      this.field_9 = new SingleChoiceSetting(
         Хl9,
         new ChoiceOption(Хlо),
         new ChoiceOption(ХlO),
         new ChoiceOption(Хl7),
         new ChoiceOption(ХlР),
         new ChoiceOption(Хl3),
         new ChoiceOption(Хlр),
         new ChoiceOption(Хl6),
         new ChoiceOption(Хl_),
         new ChoiceOption(ХlШ),
         new ChoiceOption(Хlы)
      );
      this.о = new SingleChoiceSetting(
         Хlй,
         new ChoiceOption(ХlЖ),
         new ChoiceOption(Хlм),
         new ChoiceOption(ХlК),
         new ChoiceOption(ХlI),
         new ChoiceOption(ХlТ),
         new ChoiceOption(ХlО)
      );
      this.ВВ = new FloatSetting(ХlФ, ХlЙ, 1.0F, Хlк).withStep(1.0F).visibleWhen(() -> this.field_9.isSelected(Х8У) || this.field_9.isSelected(Х8З) || this.field_9.isSelected(Х8Е));
      this.ВЯ = new FloatSetting(Хlг, Хl0, Хlх, ХlУ).withStep(1.0F).visibleWhen(() -> this.field_9.isSelected(Х8к) || this.field_9.isSelected(Х8г) || this.field_9.isSelected(Х80) || this.field_9.isSelected(Х8х));
      this.ВХ = new FloatSetting(ХlЗ, ХlЕ, ХlП, Хl西).withStep(1.0F).visibleWhen(() -> this.о.isSelected(Х8Т) || this.field_9.isSelected(Х8О) || this.field_9.isSelected(Х8Ф) || this.field_9.isSelected(Х8Й));
      this.Ве = new FloatSetting(Хlд, 0.0F, ХlБ, ХlС).withStep(1.0F);
      this.ВА = new FloatSetting(Хlз, Хlц, Хlя, Х8с).withStep(1.0F).visibleWhen(() -> this.field_9.isSelected(Х8м) || this.field_9.isSelected(Х8К) || this.о.isSelected(Х8I));
      this.В4 = new FloatSetting(Х8В, Х8Я, Х8Х, Х8е).withStep(Х8А).visibleWhen(() -> this.field_9.isSelected(Х8ы) || this.field_9.isSelected(Х8й) || this.о.isSelected(Х8Ж));
      this.сь = new BooleanSetting(Х84, false);
      this.addSettings(this.field_9, this.о, this.ВВ, this.ВЯ, this.ВХ, this.Ве, this.ВА, this.В4, this.сь);
   }

   private void ы(LivingEntity var1, LivingEntityRenderState var2) {
      if (MINECRAFT_CLIENT.player != null && var1 == MINECRAFT_CLIENT.player) {
         float var3 = MINECRAFT_CLIENT.player.getYRot();
         float var4 = MINECRAFT_CLIENT.player.getXRot();
         long var5 = System.currentTimeMillis();
         float var7 = var3;
         float var8 = var4;
         switch (this.field_9.getSelectedOption().getName()) {
            case Х8п:
               this.еЖ = Mth.wrapDegrees(this.еЖ + this.ВВ.getValue() * Х8Ь);
               var7 = this.еЖ;
               var8 = this.I_f67ab10a(Х8衣);
               break;
            case Х8Д:
               if ((float)(var5 - this.Вь) >= this.В4.getValue()) {
                  float var16 = this.ВА.getValue();
                  this.ем = ThreadLocalRandom.current().nextFloat(-var16, var16);
                  this.еК = ThreadLocalRandom.current().nextFloat(-var16 * Х8ш, var16 * Х8И);
                  this.Вь = var5;
               }

               this.еЖ = Mth.wrapDegrees(this.еЖ + this.ВВ.getValue() * Х8ь);
               var7 = Mth.wrapDegrees(var3 + this.еЖ + this.ем);
               var8 = this.I_f67ab10a(Mth.clamp(this.еК, Х8ж, Х8弟));
               break;
            case Х8Ъ:
               var7 = Mth.wrapDegrees(var3 + this.ВЯ.getValue());
               var8 = this.I_f67ab10a(this.ВХ.getValue());
               break;
            case Х8т:
               float var15 = this.ВЯ.getValue() < 0.0F ? Х8б : Х8Ы;
               var7 = Mth.wrapDegrees(var3 + var15);
               var8 = this.I_f67ab10a(0.0F);
               break;
            case Х8а:
               var7 = Mth.wrapDegrees(var3 + Х8l + this.ВЯ.getValue());
               var8 = this.I_f67ab10a(0.0F);
               break;
            case Х8Ю:
               var7 = Mth.wrapDegrees(var3 + Х88);
               var8 = this.I_f67ab10a(this.ВХ.getValue() >= 0.0F ? this.ВХ.getValue() : Х85);
               break;
            case Х8Ч:
               var7 = Mth.wrapDegrees(var3 + Х8в);
               float var14 = this.ВХ.getValue() <= 0.0F ? this.ВХ.getValue() : Х8ъ;
               var8 = this.I_f67ab10a(var14);
               break;
            case Х8Л:
               this.еЖ = Mth.wrapDegrees(this.еЖ + this.ВВ.getValue() * Х8М);
               var7 = this.еЖ;
               float var13 = (float)Math.sin(var5 / Х8Н) * Х8н;
               var8 = this.I_f67ab10a(var13);
               break;
            case Х8诶:
               if ((float)(var5 - this.Вь) >= this.В4.getValue()) {
                  this.еI = ThreadLocalRandom.current().nextFloat(Х8Ц, Х8ч);
                  this.еТ = ThreadLocalRandom.current().nextFloat(Х8Э, Х8必);
                  if (Math.abs(this.еТ) < Х8щ) {
                     this.еТ = ThreadLocalRandom.current().nextBoolean() ? Х8Г : Х8л;
                  }

                  this.Вь = var5;
               }

               var7 = Mth.wrapDegrees(var3 + this.еI);
               var8 = this.I_f67ab10a(this.еТ);
               break;
            case Х81:
               float var11 = this.ВЯ.getValue() == 0.0F ? Х8э : this.ВЯ.getValue() * Х8ю;
               float var12 = (float)Math.sin(var5 / Х8Щ) * Х8у;
               var7 = Mth.wrapDegrees(var3 + var11 + var12);
               var8 = this.I_f67ab10a(Mth.clamp(var4 + (float)Math.sin(var5 / Х82) * Х8ф, Х8и, Х89));
         }

         var2.bodyRot = var7;
         if (this.сь.getValue()) {
            var2.yRot = Mth.wrapDegrees(var3 - var7);
            var2.xRot = var4;
         } else {
            var2.yRot = this.Ве.getValue();
            var2.xRot = var8;
         }
      }
   }

   private float I_f67ab10a(float var1) {
      return switch (this.о.getSelectedOption().getName()) {
         case Х8о -> Х8р;
         case Х8O -> Х86;
         case Х87 -> 0.0F;
         case Х8Р -> this.ВХ.getValue();
         case Х83 -> {
            long var4 = System.currentTimeMillis();
            if ((float)(var4 - this.Вж) >= this.В4.getValue()) {
               float var6 = this.ВА.getValue();
               this.еК = ThreadLocalRandom.current().nextFloat(-var6, var6);
               this.Вж = var4;
            }

            yield Mth.clamp(this.еК, Х8_, Х8Ш);
         }
         default -> var1;
      };
   }

   @Override
   public void onEnable() {
      this.еЖ = MINECRAFT_CLIENT.player != null ? MINECRAFT_CLIENT.player.getYRot() : 0.0F;
      this.ем = 0.0F;
      this.еК = 0.0F;
      this.еI = 0.0F;
      this.еТ = 0.0F;
      this.Вь = 0L;
      this.Вж = 0L;
   }

   public static void А(LivingEntity var0, LivingEntityRenderState var1) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         AntiAim var2 = RoseClient.getInstance().getServices().getModules().getAntiAim();
         if (var2 != null && var2.isEnabled()) {
            var2.ы(var0, var1);
         }
      }
   }
}
