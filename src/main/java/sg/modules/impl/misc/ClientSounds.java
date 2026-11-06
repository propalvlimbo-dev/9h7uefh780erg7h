package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.gui.click.ClickGuiScreen;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class ClientSounds extends ClientModule {
   private final SingleChoiceSetting сЮ;
   private final BooleanSetting ВУ;
   private final FloatSetting ХМ;
   private final BooleanSetting ВЗ;
   private final FloatSetting ХН;
   @SerializedName("Я")
   private final RandomSource Я_at_b5622f55;
   private long Хю;
   private final List<SoundInstance> сФ;
   private static final SoundEvent е_at_b5622f55 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сО弟, ClientSounds.сОб));
   private static final SoundEvent А_at_b5622f55 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОЫ, ClientSounds.сОl));
   private static final SoundEvent field_4 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сО8, ClientSounds.сО5));
   private static final SoundEvent п_at_b5622f55 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОв, ClientSounds.сОъ));
   private static final SoundEvent Д = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОМ, ClientSounds.сОН));
   private static final SoundEvent Ъ = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОн, ClientSounds.сОЦ));
   private static final SoundEvent т = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОч, ClientSounds.сОЭ));
   private static final SoundEvent а = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сО必, ClientSounds.сОщ));
   private static final SoundEvent Ю = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ClientSounds.сОГ, ClientSounds.сОл));
   private static SoundInstance В;
   private static final String сТ西 = "ClientSounds";
   private static final String сТд = "Звуки модулей, клавиатуры и ClickGui";
   private static final String сТБ = "Мод звука";
   private static final String сТС = "Первый";
   private static final String сТз = "Второй";
   private static final String сТц = "Третий";
   private static final String сТя = "Четвёртый";
   private static final String сОс = "Звук клавиатуры";
   private static final String сОВ = "Громкость клавиатуры";
   private static final float сОЯ = 0.35F;
   private static final float сОХ = 0.05F;
   private static final String сОе = "Звуки для ClickGui";
   private static final String сОА = "Громкость ClickGui";
   private static final float сО4 = 0.35F;
   private static final float сОп = 0.05F;
   private static final float сОД = 0.5F;
   private static final float сОЪ = 0.97F;
   private static final float сОт = 0.08F;
   private static final float сОа = 0.95F;
   private static final float сОЮ = 0.14F;
   private static final float сОЧ = 0.95F;
   private static final float сОЛ = 0.15F;
   private static final float сО诶 = 0.9F;
   private static final float сО1 = 0.35F;
   private static final float сОЬ = 1.25F;
   private static final float сО衣 = 0.35F;
   private static final String сОш = "Второй";
   private static final String сОИ = "Третий";
   private static final String сОь = "Четвёртый";
   private static final float сОж = 44.0F;
   private static final String сО弟 = "rose";
   private static final String сОб = "module_enable";
   private static final String сОЫ = "rose";
   private static final String сОl = "module_disable";
   private static final String сО8 = "rose";
   private static final String сО5 = "module_enable1";
   private static final String сОв = "rose";
   private static final String сОъ = "module_disable1";
   private static final String сОМ = "rose";
   private static final String сОН = "module_enable2";
   private static final String сОн = "rose";
   private static final String сОЦ = "module_disable2";
   private static final String сОч = "rose";
   private static final String сОЭ = "module_enable3";
   private static final String сО必 = "rose";
   private static final String сОщ = "module_disable3";
   private static final String сОГ = "rose";
   private static final String сОл = "keyboard_typing";

   public ClientSounds() {
      super(сТ西, сТд, ModuleCategory.MISC);
      this.сЮ = new SingleChoiceSetting(сТБ, new ChoiceOption(сТС), new ChoiceOption(сТз), new ChoiceOption(сТц), new ChoiceOption(сТя));
      this.ВУ = new BooleanSetting(сОс, true);
      this.ХМ = new FloatSetting(сОВ, сОЯ, 0.0F, 1.0F).withStep(сОХ).visibleWhen(this.ВУ::getValue);
      this.ВЗ = new BooleanSetting(сОе, false);
      this.ХН = new FloatSetting(сОА, сО4, 0.0F, 1.0F).withStep(сОп).visibleWhen(this.ВЗ::getValue);
      this.Я_at_b5622f55 = RandomSource.create();
      this.сФ = new ArrayList<>();
      this.addSettings(this.сЮ, this.ВУ, this.ХМ, this.ВЗ, this.ХН);
   }

   public static void lП(boolean var0) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         ClientSounds var1 = RoseClient.getInstance().getServices().getModules().getClientSounds();
         if (var1 != null && var1.isEnabled() && MINECRAFT_CLIENT.getSoundManager() != null) {
            if (В != null) {
               MINECRAFT_CLIENT.getSoundManager().stop(В);
            }

            SoundEvent var2;
            if (var1.сЮ.isSelected(сОш)) {
               var2 = var0 ? field_4 : п_at_b5622f55;
            } else if (var1.сЮ.isSelected(сОИ)) {
               var2 = var0 ? Д : Ъ;
            } else if (var1.сЮ.isSelected(сОь)) {
               var2 = var0 ? т : а;
            } else {
               var2 = var0 ? е_at_b5622f55 : А_at_b5622f55;
            }

            В = new SimpleSoundInstance(var2.location(), SoundSource.MASTER, сОж, 1.0F, RandomSource.create(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true);
            MINECRAFT_CLIENT.getSoundManager().play(В);
         }
      }
   }

   public static void Чж() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         ClientSounds var0 = RoseClient.getInstance().getServices().getModules().getClientSounds();
         if (var0 != null && var0.isEnabled() && var0.ВУ.getValue() && !CreamyKeys.Oф() && MINECRAFT_CLIENT.getSoundManager() != null) {
            SimpleSoundInstance var1 = new SimpleSoundInstance(
               Ю.location(), SoundSource.MASTER, var0.ХМ.getValue(), 1.0F, RandomSource.create(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
            );
            MINECRAFT_CLIENT.getSoundManager().play(var1);
         }
      }
   }

   public static void к(ClientSounds.GuiSound var0, float var1) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         ClientSounds var2 = RoseClient.getInstance().getServices().getModules().getClientSounds();
         if (var2 != null
            && var2.isEnabled()
            && var2.ВЗ.getValue()
            && !(var2.ХН.getValue() <= 0.0F)
            && (MINECRAFT_CLIENT.gui.screen() instanceof ClickGuiScreen || var0 == ClientSounds.GuiSound.OPEN)) {
            long var3 = System.nanoTime();
            if (var0.Вч > 0L) {
               if (var3 - var2.Хю < var0.Вч) {
                  return;
               }

               var2.Хю = var3;
            }

            float var5 = Math.clamp(var1, 0.0F, 1.0F);

            float var6 = switch (var0) {
               case TAB -> сОЪ + сОт * var5;
               case THEME -> сОа + сОЮ * var5;
               default -> 1.0F;
               case MODE -> сОЧ + сОЛ * var5;
               case SLIDER, COLOR -> сО诶 + сО1 * var5;
               case SLIDER_RELEASE -> сОЬ;
            };
            SoundManager var7 = MINECRAFT_CLIENT.getSoundManager();
            var2.сФ.removeIf(var1x -> !var7.isActive(var1x));
            SimpleSoundInstance var8 = new SimpleSoundInstance(
               var0.field_7, SoundSource.MASTER, var0.А4 * var2.ХН.getValue() / сО衣, var6, var2.Я_at_b5622f55, false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
            );
            var7.play(var8);
            var2.сФ.add(var8);
         }
      }
   }

   @Override
   public void onDisable() {
      this.сФ.forEach(MINECRAFT_CLIENT.getSoundManager()::stop);
      this.сФ.clear();
      this.Хю = 0L;
   }

   public static void к(ClientSounds.GuiSound var0) {
      к(var0, сОД);
   }

   public enum GuiSound {
      OPEN("gui_open", 0.45F),
      CLOSE("gui_close", 0.45F),
      EDITOR("gui_anime", 0.4F),
      TAB("gui_category", 0.45F),
      THEME("gui_theme", 0.45F),
      SETTINGS_OPEN("gui_module_open", 0.45F),
      SETTINGS_CLOSE("gui_module_close", 0.45F),
      CHECKBOX_ON("gui_boolean_enable", 0.4F),
      CHECKBOX_OFF("gui_boolean_disable", 0.4F),
      MULTI_ON("gui_boolean2_enable", 0.4F),
      MULTI_OFF("gui_boolean2_disable", 0.4F),
      MODE("gui_mode_multi", 0.45F),
      BUTTON("gui_open_button", 0.4F),
      SLIDER_GRAB("gui_click", 0.35F),
      SLIDER("gui_slider", 0.35F, 22),
      SLIDER_RELEASE("gui_slider", 0.3F),
      COLOR("gui_slider", 0.3F, 28),
      PICKER_OPEN("gui_multi_open", 0.4F),
      PICKER_CLOSE("gui_multi_close", 0.4F),
      BIND_START("gui_binding", 0.45F),
      BIND_SET("gui_bind", 0.4F),
      BIND_RESET("gui_clear", 0.4F),
      SCROLL("gui_scroll", 0.35F, 55);

      @SerializedName("7")
      private final Identifier field_7;
      private final float А4;
      private final long Вч;

      GuiSound(String var3, float var4) {
         this(var3, var4, 0);
      }

      GuiSound(String var3, float var4, int var5) {
         this.field_7 = Identifier.fromNamespaceAndPath("rose", "clickgui." + var3);
         this.А4 = var4;
         this.Вч = var5 * 1000000L;
      }
   }
}
