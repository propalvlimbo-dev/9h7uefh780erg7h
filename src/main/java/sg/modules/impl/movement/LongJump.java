package sg.modules.impl.movement;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class LongJump extends ClientModule {
   private final SingleChoiceSetting сЪ;
   private final BooleanSetting ВТ;
   private final FloatSetting Хж;
   private final BooleanSetting ВО;
   private final BooleanSetting ВФ;
   private final FloatSetting Х弟;
   private final FloatSetting Хб;
   private final FloatSetting ХЫ;
   private final BooleanSetting ВЙ;
   private final FloatSetting Хl;
   private int Дт;
   private double ЯЭ;
   private double Я必;
   private double Ящ;
   private double ЯГ;
   private double Ял;
   private boolean еЫ;
   private boolean еl;
   private long Хщ;
   private final EventListener<PlayerTickEvent> ХЯ;
   private static final String Вт3 = "LongJump";
   private static final String Втр = "Увеличивает дальность прыжка";
   private static final String Вт6 = "Режим";
   private static final String Вт_ = "Обычный";
   private static final String ВтШ = "Незаметный";
   private static final String Вты = "Использовать таймер";
   private static final String Втй = "Скорость таймера";
   private static final float ВтЖ = 0.5F;
   private static final float Втм = 3.0F;
   private static final float ВтК = 0.1F;
   private static final String ВтI = "Отключать после прыжка";
   private static final String ВтТ = "Бесконечная дистанция";
   private static final String ВтО = "Макс. дистанция";
   private static final float ВтФ = 10.0F;
   private static final float ВтЙ = 5.0F;
   private static final float Втк = 40.0F;
   private static final String Втг = "Скорость";
   private static final float Вт0 = 1.35F;
   private static final float Втх = 0.1F;
   private static final float ВтУ = 10.0F;
   private static final float ВтЗ = 0.1F;
   private static final String ВтЕ = "Скорость (Незаметный)";
   private static final float ВтП = 0.45F;
   private static final float Вт西 = 0.1F;
   private static final float Втд = 0.01F;
   private static final String ВтБ = "Буст по Y";
   private static final String ВтС = "Сила буста Y";
   private static final float Втз = 0.5F;
   private static final float Втц = 0.1F;
   private static final float Втя = 0.1F;
   private static final String Вас = "Незаметный";
   private static final double ВаВ = 0.42;
   private static final double ВаЯ = 2.149;
   private static final double ВаХ = 0.66;
   private static final long Вае = 10L;
   private static final double ВаА = 0.4;
   private static final double Ва4 = 0.2873;
   private static final float Вап = 0.2F;
   private static final float ВаД = -45.0F;
   private static final float ВаЪ = 45.0F;
   private static final float Ват = 45.0F;
   private static final float Ваа = -45.0F;
   private static final float ВаЮ = -1.0F;
   private static final float ВаЧ = 90.0F;
   private static final String ВаЛ = "Обычный";
   private static final String Ва诶 = "Обычный";
   private static final String Ва1 = "Незаметный";
   private static final String ВаЬ = "Обычный";
   private static final String Ва衣 = "Обычный";
   private static final String Ваш = "Обычный";
   private static final String ВаИ = "Обычный";
   private static final String Ваь = "Обычный";
   private static final String Важ = "Обычный";

   private void ЫХ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         double var2 = MINECRAFT_CLIENT.player.getX() - MINECRAFT_CLIENT.player.xo;
         double var4 = MINECRAFT_CLIENT.player.getZ() - MINECRAFT_CLIENT.player.zo;
         this.Я必 = Math.sqrt(var2 * var2 + var4 * var4);
         if (this.сЪ.isSelected(Вас)) {
            this.method_8М();
         } else {
            this.method_8Ф();
         }
      }
   }

   private void Ю(double var1) {
      float var3 = MINECRAFT_CLIENT.player.getYRot();
      float var4 = MINECRAFT_CLIENT.player.input.getMoveVector().y;
      float var5 = MINECRAFT_CLIENT.player.input.getMoveVector().x;
      if (var4 != 0.0F || var5 != 0.0F) {
         if (var4 != 0.0F) {
            if (var5 > 0.0F) {
               var3 += var4 > 0.0F ? ВаД : ВаЪ;
            } else if (var5 < 0.0F) {
               var3 += var4 > 0.0F ? Ват : Ваа;
            }

            var5 = 0.0F;
            var4 = var4 > 0.0F ? 1.0F : ВаЮ;
         }

         double var6 = Math.toRadians(var3 + ВаЧ);
         double var8 = var4 * var1 * Math.cos(var6) + var5 * var1 * Math.sin(var6);
         double var10 = var4 * var1 * Math.sin(var6) - var5 * var1 * Math.cos(var6);
         MINECRAFT_CLIENT.player.setDeltaMovement(var8, MINECRAFT_CLIENT.player.getDeltaMovement().y, var10);
      }
   }

   private void method_8Ф() {
      if (!this.ВФ.getValue()) {
         double var1 = MINECRAFT_CLIENT.player.distanceToSqr(this.Ящ, this.ЯГ, this.Ял);
         float var3 = this.Х弟.getValue();
         if (var1 > var3 * var3) {
            this.disable();
            return;
         }
      }

      if (!MovementUtils.hasMovementInput()) {
         this.Дт = 0;
         this.ЯЭ = 0.0;
      } else {
         if (this.ВТ.getValue()) {
            ClientTimerSpeed.Оэ(this.Хж.getValue());
         }

         switch (this.Дт) {
            case 0:
               this.ЯЭ = this.Хб.getValue().floatValue() * this.Р_3f39d5c3();
               this.Дт = 1;
               break;
            case 1:
               double var4 = ВаВ + this.лк();
               if (this.ВЙ.getValue()) {
                  var4 += this.Хl.getValue().floatValue();
               }

               Vec3 var5 = MINECRAFT_CLIENT.player.getDeltaMovement();
               MINECRAFT_CLIENT.player.setDeltaMovement(var5.x, var4, var5.z);
               this.ЯЭ = this.ЯЭ * ВаЯ;
               if (this.ВО.getValue() && !this.еl) {
                  this.Хщ = System.currentTimeMillis();
                  this.еl = true;
               }

               this.Дт = 2;
               break;
            case 2:
               this.ЯЭ = this.Я必 - ВаХ * (this.Я必 - this.Р_3f39d5c3());
               if (MINECRAFT_CLIENT.player.getDeltaMovement().y < 0.0) {
                  this.Дт = 3;
               }
               break;
            case 3:
               this.ЯЭ = Math.max(this.Р_3f39d5c3(), this.ЯЭ);
               if (MINECRAFT_CLIENT.player.onGround()) {
                  if (this.ВО.getValue()) {
                     this.disable();
                  }

                  this.Дт = 0;
               }
         }

         this.Ю(this.ЯЭ);
         if (this.isEnabled() && this.еl && this.Хщ > 0L && System.currentTimeMillis() - this.Хщ >= Вае) {
            this.disable();
         }
      }
   }

   private double Р_3f39d5c3() {
      return Ва4;
   }

   private void method_8М() {
      if (MINECRAFT_CLIENT.player.onGround() && MINECRAFT_CLIENT.options.keyJump.isDown()) {
         MINECRAFT_CLIENT.player.jumpFromGround();
         this.еЫ = true;
         this.Дт = 1;
      }

      if (this.еЫ) {
         float var1 = this.ХЫ.getValue();
         switch (this.Дт) {
            case 1:
               this.Ю(var1 * this.Р_3f39d5c3());
               Vec3 var2 = MINECRAFT_CLIENT.player.getDeltaMovement();
               MINECRAFT_CLIENT.player.setDeltaMovement(var2.x, ВаА + this.лк(), var2.z);
               this.Дт = 2;
               break;
            case 2:
               if (MINECRAFT_CLIENT.player.getDeltaMovement().y < 0.0) {
                  this.Дт = 3;
               }
               break;
            case 3:
               if (MINECRAFT_CLIENT.player.onGround()) {
                  this.еЫ = false;
                  this.Дт = 0;
                  if (this.ВО.getValue()) {
                     this.disable();
                  }
               }
         }
      }
   }

   public LongJump() {
      super(Вт3, Втр, ModuleCategory.MOVEMENT);
      this.сЪ = new SingleChoiceSetting(Вт6, new ChoiceOption(Вт_), new ChoiceOption(ВтШ));
      this.ВТ = new BooleanSetting(Вты, false).visibleWhen(() -> this.сЪ.isSelected(Важ));
      this.Хж = new FloatSetting(Втй, 1.0F, ВтЖ, Втм).withStep(ВтК).visibleWhen(() -> this.сЪ.isSelected(Ваь) && this.ВТ.getValue());
      this.ВО = new BooleanSetting(ВтI, true).visibleWhen(() -> this.сЪ.isSelected(ВаИ));
      this.ВФ = new BooleanSetting(ВтТ, false).visibleWhen(() -> this.сЪ.isSelected(Ваш));
      this.Х弟 = new FloatSetting(ВтО, ВтФ, ВтЙ, Втк).withStep(1.0F).visibleWhen(() -> this.сЪ.isSelected(Ва衣) && !this.ВФ.getValue());
      this.Хб = new FloatSetting(Втг, Вт0, Втх, ВтУ).withStep(ВтЗ).visibleWhen(() -> this.сЪ.isSelected(ВаЬ));
      this.ХЫ = new FloatSetting(ВтЕ, ВтП, Вт西, 2.0F).withStep(Втд).visibleWhen(() -> this.сЪ.isSelected(Ва1));
      this.ВЙ = new BooleanSetting(ВтБ, false).visibleWhen(() -> this.сЪ.isSelected(Ва诶));
      this.Хl = new FloatSetting(ВтС, Втз, Втц, 2.0F).withStep(Втя).visibleWhen(() -> this.сЪ.isSelected(ВаЛ) && this.ВЙ.getValue());
      this.ХЯ = this::ЫХ;
      this.addSettings(this.сЪ, this.ВТ, this.Хж, this.ВО, this.ВФ, this.Х弟, this.Хб, this.ХЫ, this.ВЙ, this.Хl);
   }

   private float лк() {
      return MINECRAFT_CLIENT.player.hasEffect(MobEffects.JUMP_BOOST) ? Вап : 0.0F;
   }

   @Override
   public void onDisable() {
      ClientTimerSpeed.Оэ(1.0F);
      this.Дт = 0;
      this.ЯЭ = 0.0;
      this.Я必 = 0.0;
      this.еЫ = false;
      this.еl = false;
      super.onDisable();
   }

   @Override
   public void onEnable() {
      this.Дт = 0;
      this.ЯЭ = 0.0;
      this.Я必 = 0.0;
      if (MINECRAFT_CLIENT.player != null) {
         this.Ящ = MINECRAFT_CLIENT.player.getX();
         this.ЯГ = MINECRAFT_CLIENT.player.getY();
         this.Ял = MINECRAFT_CLIENT.player.getZ();
      }

      this.еЫ = false;
      this.еl = false;
      this.Хщ = 0L;
      ClientTimerSpeed.Оэ(1.0F);
      super.onEnable();
   }
}
