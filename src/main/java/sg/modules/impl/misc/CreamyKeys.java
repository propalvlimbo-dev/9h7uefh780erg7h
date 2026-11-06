package sg.modules.impl.misc;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.settings.NumericDisplayFormat;
import sg.settings.SingleChoiceSetting;

public class CreamyKeys extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("К")  SingleChoiceSetting keyboardSwitchSetting;
   private final @com.google.gson.annotations.SerializedName("I")  SingleChoiceSetting mouseSwitchSetting;
   private final @com.google.gson.annotations.SerializedName("сO")  BooleanSetting allKeysSetting;
   private final @com.google.gson.annotations.SerializedName("с7")  BooleanSetting mouseSoundsSetting;
   private final @com.google.gson.annotations.SerializedName("Ву")  FloatSetting volumeSetting;
   private final @com.google.gson.annotations.SerializedName("В2")  FloatSetting amplificationSetting;
   private final @com.google.gson.annotations.SerializedName("ВЩ")  EventListener<InputEvent> inputListener;
   private static final String ВШФ = "Blue ABS";
   private static final String ВШЙ = "Blue ABS 2";
   private static final String ВШк = "Black ABS";
   private static final String ВШг = "Black PBT";
   private static final String ВШ0 = "Brown ABS";
   private static final String ВШх = "Brown PBT";
   private static final String ВШУ = "Red ABS";
   private static final String ВШЗ = "CreamyKeys";
   private static final String ВШЕ = "Механические звуки нажатий клавиатуры и мыши";
   private static final String ВШП = "Клавиатура";
   private static final String ВШ西 = "Мышь";
   private static final String ВШд = "Все клавиши";
   private static final String ВШБ = "Звуки мыши";
   private static final String ВШС = "Громкость";
   private static final float ВШз = 0.05F;
   private static final String ВШц = "Усиление";
   private static final float ВШя = 8.0F;
   private static final String Выс = "rose";
   private static final String ВыВ = "Blue ABS";
   private static final String ВыЯ = "cherrymx_blue_abs";
   private static final String ВыХ = "Blue ABS 2";
   private static final String Вые = "cherrymx_blue_abs_2";
   private static final String ВыА = "Black ABS";
   private static final String Вы4 = "cherrymx_black_abs";
   private static final String Вып = "Black PBT";
   private static final String ВыД = "cherrymx_black_pbt";
   private static final String ВыЪ = "Brown ABS";
   private static final String Выт = "cherrymx_brown_abs";
   private static final String Выа = "Brown PBT";
   private static final String ВыЮ = "cherrymx_brown_pbt";
   private static final String ВыЧ = "Red ABS";
   private static final String ВыЛ = "cherrymx_red_abs";
   private static final String Вы诶 = "cherrymx_black_pbt";

   private static ChoiceOption[] М() {
      return new ChoiceOption[]{
         new ChoiceOption(ВШФ),
         new ChoiceOption(ВШЙ),
         new ChoiceOption(ВШк),
         new ChoiceOption(ВШг),
         new ChoiceOption(ВШ0),
         new ChoiceOption(ВШх),
         new ChoiceOption(ВШУ)
      };
   }

   private void l(InputEvent var1) {
      if (var1.getAction() == 1) {
         int var2 = var1.getInputCode();
         if (var2 >= 0 && var2 <= 7) {
            if (this.mouseSoundsSetting.getValue() && (var2 == 0 || var2 == 1)) {
               this.л(с(this.mouseSwitchSetting));
            }
         } else {
            if (this.allKeysSetting.getValue() && var2 != -1) {
               this.л(с(this.keyboardSwitchSetting));
            }
         }
      }
   }

   private static String с(SingleChoiceSetting var0) {
      if (var0.isSelected(ВыВ)) {
         return ВыЯ;
      } else if (var0.isSelected(ВыХ)) {
         return Вые;
      } else if (var0.isSelected(ВыА)) {
         return Вы4;
      } else if (var0.isSelected(Вып)) {
         return ВыД;
      } else if (var0.isSelected(ВыЪ)) {
         return Выт;
      } else if (var0.isSelected(Выа)) {
         return ВыЮ;
      } else {
         return var0.isSelected(ВыЧ) ? ВыЛ : Вы诶;
      }
   }

   public CreamyKeys() {
      super(ВШЗ, ВШЕ, ModuleCategory.MISC);
      this.keyboardSwitchSetting = new SingleChoiceSetting(ВШП, 3, М());
      this.mouseSwitchSetting = new SingleChoiceSetting(ВШ西, 1, М());
      this.allKeysSetting = new BooleanSetting(ВШд, true);
      this.mouseSoundsSetting = new BooleanSetting(ВШБ, true);
      this.volumeSetting = new FloatSetting(ВШС, 1.0F, 0.0F, 1.0F).withStep(ВШз);
      this.amplificationSetting = new FloatSetting(ВШц, 1.0F, 1.0F, ВШя).withStep(1.0F).withDisplayFormat(NumericDisplayFormat.INTEGER);
      this.inputListener = this::l;
      this.addSettings(this.keyboardSwitchSetting, this.mouseSwitchSetting, this.allKeysSetting, this.mouseSoundsSetting, this.volumeSetting, this.amplificationSetting);
   }

   public static boolean Oф() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         CreamyKeys var0 = RoseClient.getInstance().getServices().getModules().getCreamyKeys();
         return var0 != null && var0.isEnabled();
      } else {
         return false;
      }
   }

   private void л(String var1) {
      if (MINECRAFT_CLIENT.getSoundManager() != null && var1 != null) {
         int var2 = ThreadLocalRandom.current().nextInt(1, 41);
         String var3 = var2 < 10 ? "0" + var2 : Integer.toString(var2);
         Identifier var4 = Identifier.fromNamespaceAndPath(Выс, "keyboards." + var1 + ".key_" + var3);
         float var5 = Math.max(0.0F, Math.min(1.0F, this.volumeSetting.getValue()));
         int var6 = Math.max(1, Math.round(this.amplificationSetting.getValue()));

         for (int var7 = 0; var7 < var6; var7++) {
            SimpleSoundInstance var8 = new SimpleSoundInstance(
               var4, SoundSource.MASTER, var5, 1.0F, RandomSource.create(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
            );
            MINECRAFT_CLIENT.getSoundManager().play(var8);
         }
      }
   }
}
