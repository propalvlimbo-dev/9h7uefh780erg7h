package sg.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sg.settings.ChoiceOption;
import sg.settings.ValueSetting;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;

public final class EntityEspStyleSettings {
   public static final String FILL_NORMAL = "Обычный";
   public static final String FILL_COSMOS = "Космос";
   public static final String FILL_PLASMA = "Плазма";
   public static final String FILL_GLASS = "Стекло";
   public static final String GLOW_NORMAL = "Обычное";
   public static final String GLOW_SOFT_NEON = "Мягкий неон";
   public static final String GLOW_SMOKY_AURA = "Дымчатая аура";
   public static final String GLOW_ENERGY_FLOW = "Энергопоток";
   @SerializedName("3")
   private final String displayName;
   private final @com.google.gson.annotations.SerializedName("р")  String configKey;
   private final @com.google.gson.annotations.SerializedName("В")  EntityEspStyleSettings.EntityKind entityKind;
   private final @com.google.gson.annotations.SerializedName("衣")  List<ValueSetting<?>> settings = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("Э")  BooleanSetting enabled;
   private final @com.google.gson.annotations.SerializedName("必")  BooleanSetting showName;
   @SerializedName("щ")
   private final BooleanSetting showArmor;
   @SerializedName("Г")
   private final BooleanSetting showEnchantments;
   @SerializedName("л")
   private final BooleanSetting useThemeColors;
   @SerializedName("э")
   private final BooleanSetting fillEnabled;
   @SerializedName("Ъ")
   private final SingleChoiceSetting fillMode;
   private final @com.google.gson.annotations.SerializedName("Д")  ColorSetting fillColor;
   @SerializedName("щ")
   private final FloatSetting fillOpacity;
   @SerializedName("ю")
   private final BooleanSetting outlineEnabled;
   @SerializedName("Ъ")
   private final ColorSetting outlineColor;
   @SerializedName("Г")
   private final FloatSetting outlineWidth;
   @SerializedName("Щ")
   private final BooleanSetting glowEnabled;
   @SerializedName("л")
   private final FloatSetting glowRadius;
   @SerializedName("э")
   private final FloatSetting innerGlow;
   @SerializedName("т")
   private final SingleChoiceSetting glowStyle;
   @SerializedName("ю")
   private final FloatSetting glowIntensity;
   @SerializedName("Щ")
   private final FloatSetting glowSoftness;
   @SerializedName("у")
   private final FloatSetting auraDensity;
   @SerializedName("2")
   private final FloatSetting auraSwirlScale;
   @SerializedName("ф")
   private final FloatSetting auraSpeed;
   @SerializedName("и")
   private final FloatSetting flowSpeed;
   @SerializedName("9")
   private final FloatSetting waveWidth;
   @SerializedName("о")
   private final FloatSetting waveBend;
   @SerializedName("у")
   private final BooleanSetting twoColorGlow;
   @SerializedName("т")
   private final ColorSetting firstGlowColor;
   private final @com.google.gson.annotations.SerializedName("а")  ColorSetting secondGlowColor;
   @SerializedName("O")
   private final FloatSetting glowBlendSpeed;
   private static final String сОо = "Отображать";
   private static final String сОO = "Ник";
   private static final String сО7 = "Броня";
   private static final String сОР = "Зачарования";
   private static final String сО3 = "Цвет под тему";
   private static final String сОр = "Заливка";
   private static final String сО6 = "Режим заливки";
   private static final String сО_ = "Обычный";
   private static final String сОШ = "Космос";
   private static final String сОы = "Плазма";
   private static final String сОй = "Стекло";
   private static final String сОЖ = "Цвет заливки";
   private static final String сОм = "Прозрачность заливки";
   private static final float сОК = 0.45F;
   private static final float сОI = 0.05F;
   private static final float сОТ = 0.05F;
   private static final String сОО = "Обводка";
   private static final String сОФ = "Свечение";
   private static final String сОЙ = "Вид свечения";
   private static final String сОк = "Обычное";
   private static final String сОг = "Мягкий неон";
   private static final String сО0 = "Дымчатая аура";
   private static final String сОх = "Энергопоток";
   private static final String сОУ = "Радиус свечения";
   private static final float сОЗ = 8.0F;
   private static final float сОЕ = 25.0F;
   private static final String сОП = "Яркость свечения";
   private static final float сО西 = 3.0F;
   private static final float сОд = 0.05F;
   private static final String сОБ = "Мягкость свечения";
   private static final float сОС = 0.7F;
   private static final float сОз = 0.1F;
   private static final float сОц = 1.5F;
   private static final float сОя = 0.05F;
   private static final String сФс = "Плотность ауры";
   private static final float сФВ = 0.2F;
   private static final float сФЯ = 2.5F;
   private static final float сФХ = 0.05F;
   private static final String сФе = "Размер завихрений";
   private static final float сФА = 0.3F;
   private static final float сФ4 = 3.0F;
   private static final float сФп = 0.05F;
   private static final String сФД = "Скорость ауры";
   private static final float сФЪ = 0.4F;
   private static final float сФт = 0.05F;
   private static final String сФа = "Скорость потока";
   private static final float сФЮ = 0.8F;
   private static final float сФЧ = 3.0F;
   private static final float сФЛ = 0.05F;
   private static final String сФ诶 = "Ширина волн";
   private static final float сФ1 = 0.16F;
   private static final float сФЬ = 0.04F;
   private static final float сФ衣 = 0.4F;
   private static final float сФш = 0.01F;
   private static final String сФИ = "Изгиб волн";
   private static final float сФь = 3.0F;
   private static final float сФж = 0.05F;
   private static final String сФ弟 = "Двухцветное свечение";
   private static final String сФб = "Первый цвет свечения";
   private static final int сФЫ = -10176769;
   private static final String сФl = "Второй цвет свечения";
   private static final int сФ8 = -3770113;
   private static final String сФ5 = "Скорость перелива";
   private static final float сФв = 0.3F;
   private static final float сФъ = 0.05F;
   private static final String сФМ = "Внутреннее свечение";
   private static final float сФН = 0.5F;
   private static final float сФн = 0.5F;
   private static final float сФЦ = 0.05F;
   private static final String сФч = "Цвет обводки";
   private static final String сФЭ = "Толщина";
   private static final float сФ必 = 3.0F;
   private static final String сФщ = "Энергопоток";
   private static final String сФГ = "Энергопоток";
   private static final String сФл = "Энергопоток";
   private static final String сФэ = "Дымчатая аура";
   private static final String сФю = "Дымчатая аура";
   private static final String сФЩ = "Дымчатая аура";
   private static final String сФу = "Обычное";

   @Generated
   public SingleChoiceSetting getFillMode() {
      return this.fillMode;
   }

   @Generated
   public FloatSetting getGlowIntensity() {
      return this.glowIntensity;
   }

   @Generated
   public ColorSetting getFillColor() {
      return this.fillColor;
   }

   @Generated
   public BooleanSetting getShowName() {
      return this.showName;
   }

   @Generated
   public String getConfigKey() {
      return this.configKey;
   }

   public JsonObject saveConfig() {
      JsonObject config = new JsonObject();

      for (ValueSetting setting : this.settings) {
         String settingName = setting.getName();
         if (setting instanceof BooleanSetting booleanSetting) {
            config.addProperty(settingName, booleanSetting.getValue());
            config.addProperty(settingName + " Bind", booleanSetting.getKeybind().getKeyCode());
         } else if (setting instanceof ColorSetting colorSetting) {
            config.addProperty(settingName, colorSetting.getValue());
         } else if (setting instanceof FloatSetting floatSetting) {
            config.addProperty(settingName, floatSetting.getValue());
         } else if (setting instanceof SingleChoiceSetting choiceSetting) {
            config.addProperty(settingName, choiceSetting.getSelectedOption().getName());
            JsonObject optionBinds = new JsonObject();

            for (ChoiceOption option : choiceSetting.getValue()) {
               optionBinds.addProperty(option.getName(), option.getKeybind().getKeyCode());
            }

            config.add(settingName + " Binds", optionBinds);
         }
      }

      return config;
   }

   @Generated
   public ColorSetting getOutlineColor() {
      return this.outlineColor;
   }

   @Generated
   public SingleChoiceSetting getGlowStyle() {
      return this.glowStyle;
   }

   public void loadConfig(JsonObject config) {
      for (ValueSetting setting : this.settings) {
         String settingName = setting.getName();
         JsonElement value = config.get(settingName);
         if (value != null && value.isJsonPrimitive()) {
            try {
               if (setting instanceof BooleanSetting booleanSetting) {
                  if (value.getAsJsonPrimitive().isBoolean()) {
                     booleanSetting.setValue(value.getAsBoolean());
                  }

                  JsonElement booleanBind = config.get(settingName + " Bind");
                  if (booleanBind != null && booleanBind.isJsonPrimitive()) {
                     booleanSetting.getKeybind().setKeyCode(booleanBind.getAsInt());
                  }
               } else if (setting instanceof ColorSetting colorSetting) {
                  if (value.getAsJsonPrimitive().isNumber()) {
                     colorSetting.setValue(value.getAsInt());
                  }
               } else if (setting instanceof FloatSetting floatSetting) {
                  float floatValue = value.getAsFloat();
                  if (Float.isFinite(floatValue)) {
                     floatSetting.setValue(Math.clamp(floatValue, floatSetting.getMinimum(), floatSetting.getMaximum()));
                  }
               } else if (setting instanceof SingleChoiceSetting choiceSetting) {
                  for (ChoiceOption selectedOption : choiceSetting.getValue()) {
                     if (selectedOption.getName().equals(value.getAsString())) {
                        choiceSetting.selectByName(selectedOption.getName());
                     }
                  }

                  JsonElement optionBinds = config.get(settingName + " Binds");
                  if (optionBinds != null && optionBinds.isJsonObject()) {
                     for (ChoiceOption boundOption : choiceSetting.getValue()) {
                        JsonElement optionBind = optionBinds.getAsJsonObject().get(boundOption.getName());
                        if (optionBind != null && optionBind.isJsonPrimitive()) {
                           boundOption.getKeybind().setKeyCode(optionBind.getAsInt());
                        }
                     }
                  }
               }
            } catch (IllegalArgumentException | UnsupportedOperationException var14) {
            }
         }
      }
   }

   @Generated
   public ColorSetting getFirstGlowColor() {
      return this.firstGlowColor;
   }

   @Generated
   public BooleanSetting getTwoColorGlow() {
      return this.twoColorGlow;
   }

   @Generated
   public FloatSetting getWaveBend() {
      return this.waveBend;
   }

   @Generated
   public EntityEspStyleSettings.EntityKind getEntityKind() {
      return this.entityKind;
   }

   @Generated
   public BooleanSetting getFillEnabled() {
      return this.fillEnabled;
   }

   @Generated
   public FloatSetting getGlowBlendSpeed() {
      return this.glowBlendSpeed;
   }

   @Generated
   public FloatSetting getFlowSpeed() {
      return this.flowSpeed;
   }

   public EntityEspStyleSettings(String displayName, String configKey, EntityEspStyleSettings.EntityKind entityKind, boolean defaultEnabled) {
      this.displayName = displayName;
      this.configKey = configKey;
      this.entityKind = entityKind;
      this.enabled = this.addSetting(new BooleanSetting(сОо, defaultEnabled));
      this.showName = this.addSetting(new BooleanSetting(сОO, true));
      if (entityKind == EntityEspStyleSettings.EntityKind.PLAYER) {
         this.showArmor = this.addSetting(new BooleanSetting(сО7, true));
         this.showEnchantments = this.addSetting(new BooleanSetting(сОР, false).visibleWhen(this.showArmor::getValue));
      } else {
         this.showArmor = null;
         this.showEnchantments = null;
      }

      this.useThemeColors = this.addSetting(new BooleanSetting(сО3, false));
      this.fillEnabled = this.addSetting(new BooleanSetting(сОр, false));
      this.fillMode = this.addSetting(
         new SingleChoiceSetting(сО6, new ChoiceOption(сО_), new ChoiceOption(сОШ), new ChoiceOption(сОы), new ChoiceOption(сОй)).visibleWhen(this.fillEnabled::getValue)
      );
      this.fillColor = this.addSetting(new ColorSetting(сОЖ, false, -1).visibleWhen(() -> this.fillEnabled.getValue() && !this.useThemeColors.getValue()));
      this.fillOpacity = this.addSetting(new FloatSetting(сОм, сОК, сОI, 1.0F).withStep(сОТ).visibleWhen(this.fillEnabled::getValue));
      this.outlineEnabled = this.addSetting(new BooleanSetting(сОО, false));
      this.glowEnabled = this.addSetting(new BooleanSetting(сОФ, false));
      this.glowStyle = this.addSetting(
         new SingleChoiceSetting(сОЙ, new ChoiceOption(сОк), new ChoiceOption(сОг), new ChoiceOption(сО0), new ChoiceOption(сОх)).visibleWhen(this.glowEnabled::getValue)
      );
      this.glowRadius = this.addSetting(new FloatSetting(сОУ, сОЗ, 1.0F, сОЕ).withStep(1.0F).visibleWhen(() -> this.glowEnabled.getValue()));
      this.glowIntensity = this.addSetting(new FloatSetting(сОП, 1.0F, 0.0F, сО西).withStep(сОд).visibleWhen(this.glowEnabled::getValue));
      this.glowSoftness = this.addSetting(new FloatSetting(сОБ, сОС, сОз, сОц).withStep(сОя).visibleWhen(() -> this.glowEnabled.getValue() && !this.glowStyle.isSelected(сФу)));
      this.auraDensity = this.addSetting(new FloatSetting(сФс, 1.0F, сФВ, сФЯ).withStep(сФХ).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФЩ)));
      this.auraSwirlScale = this.addSetting(new FloatSetting(сФе, 1.0F, сФА, сФ4).withStep(сФп).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФю)));
      this.auraSpeed = this.addSetting(new FloatSetting(сФД, сФЪ, 0.0F, 2.0F).withStep(сФт).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФэ)));
      this.flowSpeed = this.addSetting(new FloatSetting(сФа, сФЮ, 0.0F, сФЧ).withStep(сФЛ).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФл)));
      this.waveWidth = this.addSetting(new FloatSetting(сФ诶, сФ1, сФЬ, сФ衣).withStep(сФш).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФГ)));
      this.waveBend = this.addSetting(new FloatSetting(сФИ, 1.0F, 0.0F, сФь).withStep(сФж).visibleWhen(() -> this.glowEnabled.getValue() && this.glowStyle.isSelected(сФщ)));
      this.twoColorGlow = this.addSetting(new BooleanSetting(сФ弟, false).visibleWhen(this.glowEnabled::getValue));
      this.firstGlowColor = this.addSetting(new ColorSetting(сФб, false, сФЫ).visibleWhen(() -> this.glowEnabled.getValue() && this.twoColorGlow.getValue() && !this.useThemeColors.getValue()));
      this.secondGlowColor = this.addSetting(new ColorSetting(сФl, false, сФ8).visibleWhen(() -> this.glowEnabled.getValue() && this.twoColorGlow.getValue() && !this.useThemeColors.getValue()));
      this.glowBlendSpeed = this.addSetting(new FloatSetting(сФ5, сФв, 0.0F, 2.0F).withStep(сФъ).visibleWhen(() -> this.glowEnabled.getValue() && this.twoColorGlow.getValue()));
      this.innerGlow = this.addSetting(new FloatSetting(сФМ, сФН, 0.0F, сФн).withStep(сФЦ).visibleWhen(() -> this.glowEnabled.getValue()));
      this.outlineColor = this.addSetting(new ColorSetting(сФч, -1).visibleWhen(() -> (this.outlineEnabled.getValue() || this.glowEnabled.getValue()) && !this.useThemeColors.getValue()));
      this.outlineWidth = this.addSetting(new FloatSetting(сФЭ, 2.0F, 1.0F, сФ必).withStep(1.0F).visibleWhen(this.outlineEnabled::getValue));
   }

   @Generated
   public BooleanSetting getShowArmor() {
      return this.showArmor;
   }

   @Generated
   public FloatSetting getFillOpacity() {
      return this.fillOpacity;
   }

   @Generated
   public BooleanSetting getGlowEnabled() {
      return this.glowEnabled;
   }

   @Generated
   public BooleanSetting getUseThemeColors() {
      return this.useThemeColors;
   }

   @Generated
   public List<ValueSetting<?>> getSettings() {
      return this.settings;
   }

   @Generated
   public BooleanSetting getShowEnchantments() {
      return this.showEnchantments;
   }

   @Generated
   public FloatSetting getAuraSpeed() {
      return this.auraSpeed;
   }

   @Generated
   public ColorSetting getSecondGlowColor() {
      return this.secondGlowColor;
   }

   @Generated
   public FloatSetting getAuraSwirlScale() {
      return this.auraSwirlScale;
   }

   @Generated
   public FloatSetting getWaveWidth() {
      return this.waveWidth;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   @Generated
   public FloatSetting getGlowSoftness() {
      return this.glowSoftness;
   }

   @Generated
   public FloatSetting getGlowRadius() {
      return this.glowRadius;
   }

   @Generated
   public BooleanSetting getEnabled() {
      return this.enabled;
   }

   @Generated
   public FloatSetting getOutlineWidth() {
      return this.outlineWidth;
   }

   @Generated
   public FloatSetting getInnerGlow() {
      return this.innerGlow;
   }

   @Generated
   public BooleanSetting getOutlineEnabled() {
      return this.outlineEnabled;
   }

   private <S extends ValueSetting<?>> S addSetting(S setting) {
      this.settings.add(setting);
      return (S)setting;
   }

   @Generated
   public FloatSetting getAuraDensity() {
      return this.auraDensity;
   }

   public enum EntityKind {
      PLAYER,
      LIVING,
      ITEM;
   }
}
