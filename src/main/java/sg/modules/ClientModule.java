package sg.modules;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Vector2f;
import settings.render.ClientLocalization;
import sg.ec.UnhookState;
import sg.ec.MinecraftAccess;
import sg.settings.BooleanSetting;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.Keybind;
import sg.settings.KeybindMode;
import sg.settings.KeybindSetting;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.init.NotificationCategory;
import sg.modules.impl.init.NotificationManager;
import sg.settings.RangeSetting;
import sg.settings.SectionHeaderSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.TextSetting;
import sg.settings.ValueSetting;
import sg.modules.impl.misc.ClientSounds;

public class ClientModule implements MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("е")  ArrayList<ValueSetting<?>> settings = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("п")  Consumer<Throwable> errorHandler = Throwable::printStackTrace;
   private final @com.google.gson.annotations.SerializedName("Я")  Keybind keybind = new Keybind(KeybindMode.TOGGLE, -1);
   private final @com.google.gson.annotations.SerializedName("в")  FloatAnimation animation;
   private final @com.google.gson.annotations.SerializedName("к")  String name;
   @SerializedName("г")
   private final String description;
   private final @com.google.gson.annotations.SerializedName("Х")  ModuleCategory category;
   @SerializedName("г")
   public boolean enabled;
   @SerializedName("0")
   private boolean favorite;
   private static final float ВБА = 15.0F;
   private static final String ВБ4 = "Модуль ";
   private static final String ВБп = " включён";
   private static final String ВБД = "H";
   private static final String ВБЪ = "Модуль ";
   private static final String ВБт = " выключен";
   private static final String ВБа = "I";
   private static final String ВБЮ = "State";
   private static final String ВБЧ = "Favorite";
   private static final String ВБЛ = "Key";
   private static final String ВБ诶 = "Type";
   private static final String ВБ1 = ":";
   private static final String ВБЬ = "Settings";
   private static final String ВБ衣 = "State";
   private static final String ВБш = "State";
   private static final String ВБИ = "Favorite";
   private static final String ВБь = "Favorite";
   private static final String ВБж = "Key";
   private static final String ВБ弟 = "Key";
   private static final String ВБб = "Type";
   private static final String ВБЫ = "Type";
   private static final String ВБl = "Settings";
   private static final String ВБ8 = "Settings";
   private static final String ВБ5 = ":";
   private static final String ВБв = ":";

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   public void disable() {
      this.enabled = false;

      try {
         this.onDisable();
      } catch (Throwable failure) {
         this.errorHandler.accept(failure);
      }

      RoseClient.getInstance().getEventBus().unregister(this);
      if (!UnhookState.ые() && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         MutableComponent notificationText = Component.empty()
            .append(Component.literal(ClientLocalization.translate(ВБЪ)))
            .append(Component.literal(this.name))
            .append(Component.literal(ClientLocalization.translate(ВБт)));
         NotificationManager.р(ВБа, notificationText, -1, NotificationCategory.MODULES);
         ClientSounds.lП(false);
      }
   }

   public void onEnable() {
   }

   @Generated
   public Keybind getKeybind() {
      return this.keybind;
   }

   @Generated
   public ArrayList<ValueSetting<?>> getSettings() {
      return this.settings;
   }

   public void toggleFavorite() {
      this.favorite = !this.favorite;
   }

   public JsonObject saveConfig() {
      JsonObject config = new JsonObject();
      JsonObject settingValues = new JsonObject();
      config.addProperty(ВБЮ, this.enabled);
      config.addProperty(ВБЧ, this.favorite);
      config.addProperty(ВБЛ, this.keybind.getKeyCode());
      config.addProperty(ВБ诶, this.keybind.getMode().getDisplayName());

      for (ValueSetting setting : this.settings) {
         if (setting instanceof BooleanSetting booleanSetting) {
            settingValues.addProperty(booleanSetting.getName(), booleanSetting.getValue());
            settingValues.addProperty(booleanSetting.getName() + " Bind", booleanSetting.getKeybind().getKeyCode());
         } else if (setting instanceof ColorSetting colorSetting) {
            settingValues.addProperty(colorSetting.getName(), colorSetting.getValue());
         } else if (setting instanceof FloatSetting floatSetting) {
            settingValues.addProperty(floatSetting.getName(), Math.clamp(floatSetting.getValue(), floatSetting.getMinimum(), floatSetting.getMaximum()));
         } else if (setting instanceof TextSetting textSetting) {
            settingValues.addProperty(textSetting.getName(), textSetting.getValue());
         } else if (setting instanceof KeybindSetting keybindSetting) {
            settingValues.addProperty(keybindSetting.getName(), keybindSetting.getValue());
         } else if (setting instanceof RangeSetting rangeSetting) {
            settingValues.addProperty(rangeSetting.getName(), rangeSetting.getValue().x + ":" + rangeSetting.getValue().y);
         } else if (setting instanceof SingleChoiceSetting singleChoiceSetting) {
            JsonObject singleChoiceBinds = new JsonObject();
            settingValues.addProperty(singleChoiceSetting.getName(), singleChoiceSetting.getSelectedOption().getName());

            for (ChoiceOption singleChoiceOption : singleChoiceSetting.getValue()) {
               singleChoiceBinds.addProperty(singleChoiceOption.getName(), singleChoiceOption.getKeybind().getKeyCode());
            }

            settingValues.add(singleChoiceSetting.getName() + " Binds", singleChoiceBinds);
         } else if (setting instanceof MultiChoiceSetting multiChoiceSetting) {
            JsonObject multiChoiceBinds = new JsonObject();
            StringBuilder selectedNames = new StringBuilder();

            for (ChoiceOption multiChoiceOption : multiChoiceSetting.getValue()) {
               multiChoiceBinds.addProperty(multiChoiceOption.getName(), multiChoiceOption.getKeybind().getKeyCode());
               if (multiChoiceOption.isSelected()) {
                  selectedNames.append(multiChoiceOption.getName());
                  if (multiChoiceOption != multiChoiceSetting.getValue().getLast()) {
                     selectedNames.append(ВБ1);
                  }
               }
            }

            settingValues.addProperty(multiChoiceSetting.getName(), selectedNames.toString());
            settingValues.add(multiChoiceSetting.getName() + " Binds", multiChoiceBinds);
         }
      }

      config.add(ВБЬ, settingValues);
      return config;
   }

   @Generated
   public FloatAnimation getAnimation() {
      return this.animation;
   }

   public List<ValueSetting<?>> getSettingsList() {
      return this.settings;
   }

   @Generated
   public ModuleCategory getCategory() {
      return this.category;
   }

   @Generated
   public boolean isFavorite() {
      return this.favorite;
   }

   public void onDisable() {
   }

   public void setFavorite(boolean favorite) {
      this.favorite = favorite;
   }

   @Generated
   public Consumer<Throwable> getErrorHandler() {
      return this.errorHandler;
   }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }
   }

   @Generated
   public void setEnabledFlag(boolean enabled) {
      this.enabled = enabled;
   }

   public void loadConfig(JsonObject config) {
      if (config != null) {
         if (config.has(ВБ衣) && this.enabled != config.get(ВБш).getAsBoolean()) {
            this.toggle();
         }

         if (config.has(ВБИ)) {
            this.favorite = config.get(ВБь).getAsBoolean();
         }

         if (config.has(ВБж)) {
            this.keybind.setKeyCode(config.get(ВБ弟).getAsInt());
         } else {
            this.keybind.setKeyCode(-1);
         }

         if (config.has(ВБб)) {
            this.keybind.setMode(KeybindMode.fromDisplayName(config.get(ВБЫ).getAsString()));
         }

         if (config.has(ВБl)) {
            JsonObject settingValues = config.getAsJsonObject(ВБ8);

            for (ValueSetting setting : this.settings) {
               if (!(setting instanceof SectionHeaderSetting) && settingValues.has(setting.getName())) {
                  try {
                     switch (setting) {
                        case BooleanSetting booleanSetting:
                           booleanSetting.setValue(settingValues.get(setting.getName()).getAsBoolean());
                           booleanSetting.getKeybind().setKeyCode(-1);
                           if (settingValues.has(booleanSetting.getName() + " Bind")) {
                              booleanSetting.getKeybind().setKeyCode(settingValues.get(booleanSetting.getName() + " Bind").getAsInt());
                           }
                           break;
                        case ColorSetting colorSetting:
                           colorSetting.setValue(settingValues.get(setting.getName()).getAsInt());
                           break;
                        case FloatSetting floatSetting:
                           floatSetting.setRawValue(settingValues.get(floatSetting.getName()).getAsFloat());
                           break;
                        case TextSetting textSetting:
                           textSetting.setValue(settingValues.get(setting.getName()).getAsString());
                           break;
                        case KeybindSetting keybindSetting:
                           keybindSetting.setValue(settingValues.get(setting.getName()).getAsInt());
                           break;
                        case RangeSetting rangeSetting:
                           String[] rangeParts = settingValues.get(setting.getName()).getAsString().split(ВБ5);
                           Vector2f range = new Vector2f(Float.parseFloat(rangeParts[0]), Float.parseFloat(rangeParts[1]));
                           rangeSetting.setValue(range);
                           break;
                        case SingleChoiceSetting singleChoiceSetting:
                           String selectedOptionName = settingValues.get(setting.getName()).getAsString();

                           for (ChoiceOption singleOptionToReset : singleChoiceSetting.getValue()) {
                              singleOptionToReset.getKeybind().setKeyCode(-1);
                           }

                           singleChoiceSetting.selectByName(selectedOptionName);
                           if (settingValues.has(singleChoiceSetting.getName() + " Binds")) {
                              JsonObject singleChoiceBinds = settingValues.getAsJsonObject(singleChoiceSetting.getName() + " Binds");

                              for (ChoiceOption singleOptionWithBind : singleChoiceSetting.getValue()) {
                                 if (singleChoiceBinds.has(singleOptionWithBind.getName())) {
                                    singleOptionWithBind.getKeybind().setKeyCode(singleChoiceBinds.get(singleOptionWithBind.getName()).getAsInt());
                                 }
                              }
                           }
                           break;
                        case MultiChoiceSetting multiChoiceSetting:
                           String[] selectedOptionNames = settingValues.get(setting.getName()).getAsString().split(ВБв);

                           for (ChoiceOption multiOptionToReset : multiChoiceSetting.getValue()) {
                              multiOptionToReset.setSelected(false);
                              multiOptionToReset.getKeybind().setKeyCode(-1);

                              for (String selectedName : selectedOptionNames) {
                                 if (multiOptionToReset.getName().equals(selectedName)) {
                                    multiOptionToReset.setSelected(true);
                                 }
                              }
                           }

                           if (settingValues.has(multiChoiceSetting.getName() + " Binds")) {
                              JsonObject multiChoiceBinds = settingValues.getAsJsonObject(multiChoiceSetting.getName() + " Binds");

                              for (ChoiceOption multiOptionWithBind : multiChoiceSetting.getValue()) {
                                 if (multiChoiceBinds.has(multiOptionWithBind.getName())) {
                                    multiOptionWithBind.getKeybind().setKeyCode(multiChoiceBinds.get(multiOptionWithBind.getName()).getAsInt());
                                 }
                              }
                           }
                           continue;
                        default:
                     }
                  } catch (Exception failure) {
                     System.out.println(failure.getMessage());
                  }
               }
            }
         }
      }
   }

   public void enable() {
      this.enabled = true;

      try {
         this.onEnable();
      } catch (Throwable failure) {
         this.errorHandler.accept(failure);
      }

      RoseClient.getInstance().getEventBus().register(this);
      if (!UnhookState.ые() && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         MutableComponent notificationText = Component.empty()
            .append(Component.literal(ClientLocalization.translate(ВБ4)))
            .append(Component.literal(this.name))
            .append(Component.literal(ClientLocalization.translate(ВБп)));
         NotificationManager.р(ВБД, notificationText, -1, NotificationCategory.MODULES);
         ClientSounds.lП(true);
      }
   }

   @Generated
   public String getName() {
      return this.name;
   }

   public ClientModule(String name, String description, ModuleCategory category) {
      this.animation = new FloatAnimation(0.0F, ВБА);
      this.enabled = false;
      this.favorite = false;
      this.name = name;
      this.description = description;
      this.category = category;
   }

   @Generated
   public String getDescription() {
      return this.description;
   }

   public void addSettings(ValueSetting<?>... settings) {
      this.settings.addAll(List.of(settings));
   }
}
