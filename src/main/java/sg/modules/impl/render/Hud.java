package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.network.InventoryInteractions;
import sg.render.RoseBatchedItemAtlas;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.init.NotificationCategory;
import sg.settings.SingleChoiceSetting;
import sg.settings.ValueSetting;
import sg.modules.impl.init.WatermarkField;

public class Hud extends ClientModule {
   private static final EquipmentSlot[] field_4 = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private final RoseBatchedItemAtlas.ItemHandle[] В = new RoseBatchedItemAtlas.ItemHandle[field_4.length];
   @SerializedName("Х")
   private final RoseBatchedItemAtlas.ItemHandle Х_at_0c50bf35 = new RoseBatchedItemAtlas.ItemHandle();
   private ItemStack 衣;
   @SerializedName("в")
   public MultiChoiceSetting в_at_0c50bf35;
   private final FloatSetting ХЮ;
   public BooleanSetting Ви;
   public BooleanSetting В9;
   private final @com.google.gson.annotations.SerializedName("Во")  BooleanSetting glassSetting;
   private final BooleanSetting ВO;
   private final MultiChoiceSetting ъ;
   private final MultiChoiceSetting М;
   private final @com.google.gson.annotations.SerializedName("В7")  BooleanSetting fromHeadSetting;
   private final @com.google.gson.annotations.SerializedName("ВР")  BooleanSetting fromBarSetting;
   private final SingleChoiceSetting сА;
   private final SingleChoiceSetting с4;
   private final FloatSetting ХЧ;
   private final SingleChoiceSetting сп;
   private final BooleanSetting В3;
   private final BooleanSetting Вр;
   private final FloatSetting ХЛ;
   private final FloatSetting Х诶;
   private final FloatSetting Х1;
   private final FloatSetting ХЬ;
   private final FloatSetting Х衣;
   private final FloatSetting Хш;
   private final BooleanSetting В6;
   private EventListener<HudRenderEvent> ЯI;
   private FloatAnimation сИ;
   private static final String сбН = "Hud";
   private static final String сбн = "Интерфейс клиента";
   private static final String сбЦ = "Элементы";
   private static final String сбч = "Логотип";
   private static final String сбЭ = "Горячие клавиши";
   private static final String сб必 = "Активные зелья";
   private static final String сбщ = "Персонал";
   private static final String сбГ = "Задержки";
   private static final String сбл = "Информация о цели";
   private static final String сбэ = "Кол-во тотемов";
   private static final String сбю = "Броня";
   private static final String сбЩ = "Скорость";
   private static final String сбу = "Координаты";
   private static final String сб2 = "Таймер";
   private static final String сбф = "Индикатор предметов";
   private static final String сби = "Инвентарь";
   private static final String сб9 = "PvP помощник";
   private static final String сбо = "Уведомления";
   private static final String сбO = "Размер";
   private static final float сб7 = 4.0F;
   private static final String сбР = "Свечение";
   private static final String сб3 = "Размытие";
   private static final String сбр = "Стекло";
   private static final String сб6 = "Показ Перемещения";
   private static final String сб_ = "От головы";
   private static final String сбШ = "От полоски";
   private static final String сбы = "TargetHUD";
   private static final String сбй = "1";
   private static final String сбЖ = "2";
   private static final String сбм = "Potions";
   private static final String сбК = "1";
   private static final String сбI = "2";
   private static final String сбТ = "Количество частиц";
   private static final float сбО = 16.0F;
   private static final float сбФ = 67.0F;
   private static final String сбЙ = "Партиклы цели";
   private static final String сбк = "Классика";
   private static final String сбг = "Светлячки";
   private static final String сб0 = "Заменить Скин";
   private static final String сбх = "3D модель";
   private static final String сбУ = "Размер 3D";
   private static final float сбЗ = 0.6F;
   private static final float сбЕ = 0.05F;
   private static final String сбП = "Порог брони";
   private static final float сб西 = 35.0F;
   private static final float сбд = 5.0F;
   private static final float сбБ = 90.0F;
   private static final String сбС = "Смена подсказок";
   private static final float сбз = 2.5F;
   private static final float сбц = 0.5F;
   private static final float сбя = 10.0F;
   private static final float сЫс = 0.5F;
   private static final String сЫВ = "Размер PvP помощника";
   private static final float сЫЯ = 0.6F;
   private static final float сЫХ = 0.05F;
   private static final String сЫе = "Размер лут трекера";
   private static final float сЫА = 0.6F;
   private static final float сЫ4 = 0.05F;
   private static final String сЫп = "Размер индикатора предметов";
   private static final float сЫД = 1.9F;
   private static final float сЫЪ = 0.6F;
   private static final float сЫт = 0.05F;
   private static final String сЫа = "Закрепить хотбар";
   private static final float сЫЮ = 15.0F;
   private static final float сЫЧ = 0.5F;
   private static final float сЫЛ = 0.25F;
   private static final String сЫ诶 = "Кол-во тотемов";
   private static final float сЫ1 = 0.01F;
   private static final float сЫЬ = 8.0F;
   private static final float сЫ衣 = 8.0F;
   private static final float сЫш = 8.0F;
   private static final float сЫИ = 8.0F;
   private static final float сЫь = 8.0F;
   private static final float сЫж = 8.0F;
   private static final float сЫ弟 = 9.0F;
   private static final float сЫб = 17.0F;
   private static final float сЫЫ = 17.0F;
   private static final String сЫl = "Броня";
   private static final float сЫ8 = 16.0F;
   private static final float сЫ5 = 3.0F;
   private static final float сЫв = 17.0F;
   private static final float сЫъ = 56.0F;
   private static final float сЫМ = 0.5F;
   private static final float сЫН = 0.5F;
   private static final float сЫн = 56.0F;
   private static final String сЫЦ = "Хотбар";
   private static final String сЫч = "Индикатор предметов";
   private static final String сЫЭ = "PvP помощник";
   private static final String сЫ必 = "PvP помощник";
   private static final String сЫщ = "PvP помощник";
   private static final String сЫГ = "Информация о цели";
   private static final String сЫл = "Информация о цели";
   private static final String сЫэ = "Информация о цели";
   private static final String сЫю = "Активные зелья";
   private static final String сЫЩ = "Информация о цели";

   @Generated
   public FloatSetting Юъ() {
      return this.Х衣;
   }

   @Generated
   public BooleanSetting В() {
      return this.Ви;
   }

   @Generated
   public MultiChoiceSetting ъ_3bdb855e() {
      return this.ъ;
   }

   @Generated
   public FloatSetting ЮБ() {
      return this.Хш;
   }

   @Generated
   public BooleanSetting getGlassSetting() {
      return this.glassSetting;
   }

   @Generated
   public SingleChoiceSetting method_2_f1816549() {
      return this.сп;
   }

   public float зй() {
      return сЫЧ + this.ХЮ.getValue() * сЫЛ;
   }

   public Hud() {
      super(сбН, сбн, ModuleCategory.RENDER);
      this.в_at_0c50bf35 = new MultiChoiceSetting(
         сбЦ,
         new ChoiceOption(сбч, true),
         new ChoiceOption(сбЭ, true),
         new ChoiceOption(сб必, true),
         new ChoiceOption(сбщ, true),
         new ChoiceOption(сбГ, true),
         new ChoiceOption(сбл, true),
         new ChoiceOption(сбэ, true),
         new ChoiceOption(сбю, true),
         new ChoiceOption(сбЩ, true),
         new ChoiceOption(сбу, true),
         new ChoiceOption(сб2, false),
         new ChoiceOption(сбф, true),
         new ChoiceOption(сби, false),
         new ChoiceOption(сб9, true),
         new ChoiceOption(сбо, true)
      );
      this.ХЮ = new FloatSetting(сбO, 1.0F, 1.0F, сб7).withStep(1.0F);
      this.Ви = new BooleanSetting(сбР, true) {
         public void setValue(Boolean var1) {
            super.setValue(var1);
            if (Boolean.TRUE.equals(var1)) {
               Hud.this.glassSetting.setValue(false);
            }
         }
      };
      this.В9 = new BooleanSetting(сб3, false) {
         public void setValue(Boolean var1) {
            super.setValue(var1);
            if (Boolean.TRUE.equals(var1)) {
               Hud.this.glassSetting.setValue(false);
            }
         }
      };
      this.glassSetting = new BooleanSetting(сбр, true) {
         public void setValue(Boolean var1) {
            super.setValue(var1);
            if (Boolean.TRUE.equals(var1)) {
               Hud.this.В9.setValue(false);
               Hud.this.Ви.setValue(false);
            }
         }
      };
      this.ВO = new BooleanSetting(сб6, true);
      this.ъ = NotificationCategory.р();
      this.М = WatermarkField.Э();
      this.fromHeadSetting = new BooleanSetting(сб_, true);
      this.fromBarSetting = new BooleanSetting(сбШ, true);
       this.сА = new SingleChoiceSetting(сбы, 2, new ChoiceOption(сбй), new ChoiceOption(сбЖ), new ChoiceOption("Catlavan")).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫЩ));
      this.с4 = new SingleChoiceSetting(сбм, new ChoiceOption(сбК), new ChoiceOption(сбI)).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫю));
      this.ХЧ = new FloatSetting(сбТ, сбО, 1.0F, сбФ).withStep(1.0F).visibleWhen(this::яЭ);
      this.сп = new SingleChoiceSetting(сбЙ, 1, new ChoiceOption(сбк), new ChoiceOption(сбг)).visibleWhen(this::яЭ);
      this.В3 = new BooleanSetting(сб0, false).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫэ));
      this.Вр = new BooleanSetting(сбх, false).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫл));
      this.ХЛ = new FloatSetting(сбУ, 1.0F, сбЗ, 2.0F).withStep(сбЕ).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫГ) && this.Вр.getValue());
      this.Х诶 = new FloatSetting(сбП, сб西, сбд, сбБ).withStep(1.0F).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫщ));
      this.Х1 = new FloatSetting(сбС, сбз, сбц, сбя).withStep(сЫс).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫ必));
      this.ХЬ = new FloatSetting(сЫВ, 1.0F, сЫЯ, 2.0F).withStep(сЫХ).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫЭ));
      this.Х衣 = new FloatSetting(сЫе, 1.0F, сЫА, 2.0F).withStep(сЫ4);
      this.Хш = new FloatSetting(сЫп, сЫД, сЫЪ, 2.0F).withStep(сЫт).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫч));
      this.В6 = new BooleanSetting(сЫа, false).visibleWhen(() -> this.в_at_0c50bf35.isSelected(сЫЦ));
      this.ЯI = this::Э;
      this.сИ = new FloatAnimation(0.0F, сЫЮ);

      for (int var1 = 0; var1 < this.В.length; var1++) {
         this.В[var1] = new RoseBatchedItemAtlas.ItemHandle();
      }

      this.addSettings(
         this.в_at_0c50bf35,
         this.ХЮ,
         this.Ви,
         this.В9,
         this.glassSetting,
         this.ВO,
         this.М,
         this.fromHeadSetting,
         this.fromBarSetting,
         this.сА,
         this.с4,
         this.ХЧ,
         this.сп,
         this.В3,
         this.Вр,
         this.ХЛ,
         this.Х诶,
         this.Х1,
         this.ХЬ,
         this.Х衣,
         this.Хш,
         this.В6,
         this.ъ
      );
   }

   @Generated
   public FloatSetting Юя() {
      return this.ХЬ;
   }

   @Generated
   public SingleChoiceSetting Р_f1816549() {
      return this.сА;
   }

   @Generated
   public BooleanSetting getFromBarSetting() {
      return this.fromBarSetting;
   }

   @Generated
   public FloatSetting Юг() {
      return this.Х诶;
   }

   @Override
   public void onDisable() {
      RoseClient.getInstance().getServices().getHudElements().disable();
   }

   @Generated
   public BooleanSetting Я() {
      return this.Вр;
   }

   public boolean яЭ() {
      return this.fromHeadSetting.getValue() || this.fromBarSetting.getValue();
   }

   @Generated
   public BooleanSetting Т_3276742a() {
      return this.В6;
   }

   public void Э(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.в_at_0c50bf35.isSelected(сЫ诶)) {
            boolean var2 = (MINECRAFT_CLIENT.gui.screen() != null && MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen || InventoryInteractions.Ъ_a83dd0cc(Items.TOTEM_OF_UNDYING) != -1)
               && !MINECRAFT_CLIENT.player.isCreative();
            this.сИ.updateTarget(var2 ? 1.0F : 0.0F);
            float var3 = this.сИ.getValue();
            if (var3 > сЫ1) {
               if (this.衣 == null) {
                  this.衣 = Items.TOTEM_OF_UNDYING.getDefaultInstance();
               }

               float var4 = MINECRAFT_CLIENT.getWindow().getGuiScale();
               float var5 = (MINECRAFT_CLIENT.getWindow().getGuiScaledWidth() / 2 - 8 + (сЫЬ - сЫ衣 * var3)) * var4;
               float var6 = (MINECRAFT_CLIENT.getWindow().getGuiScaledHeight() - 58 + (сЫш - сЫИ * var3)) * var4;
               float var7 = var5 + сЫь * var3 * var4;
               float var8 = var6 + сЫж * var3 * var4;
               UiDrawing.pushGuiScaleTransform(var7, var8);
               UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

               try {
                  UiBatchDrawing.drawScaledItem(this.衣, var5, var6, var3 * var4, false, this.Х_at_0c50bf35);
                  String var9 = InventoryInteractions.щ(Items.TOTEM_OF_UNDYING) + "";
                  int var10 = Math.max(7, Math.round(сЫ弟 * var3 * var4));
                  float var11 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var9, var10);
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var9, var5 + сЫб * var3 * var4 - var11, var6 + сЫЫ * var3 * var4 - var10, var10, -1);
               } finally {
                  UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
                  UiDrawing.popGuiScaleTransform();
               }
            }
         }

         if (this.в_at_0c50bf35.isSelected(сЫl)) {
            float var21 = сЫ8;
            float var22 = сЫ5;
            int var23 = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth() / 2;
            int var24 = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
            if (MINECRAFT_CLIENT.player.isCreative()) {
               var24 = (int)(var24 + сЫв);
            }

            float var25 = MINECRAFT_CLIENT.getWindow().getGuiScale();
            float var26 = (var23 + 74 - (field_4.length - 1) * (var21 + var22)) * var25;
            float var27 = (var23 + 74 + var21) * var25;
            float var28 = (var24 - сЫъ) * var25;
            UiDrawing.pushGuiScaleTransform((var26 + var27) * сЫМ, var28 + var21 * var25 * сЫН);
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

            try {
               for (int var29 = 0; var29 < field_4.length; var29++) {
                  ItemStack var30 = MINECRAFT_CLIENT.player.getItemBySlot(field_4[var29]);
                  if (!var30.isEmpty()) {
                     float var12 = (var23 + 74 - var29 * (var21 + var22)) * var25;
                     float var13 = (var24 - сЫн) * var25;
                     UiBatchDrawing.drawScaledItem(var30, var12, var13, var25, true, this.В[var29]);
                  }
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
               UiDrawing.popGuiScaleTransform();
            }
         }
      }
   }

   @Generated
   public MultiChoiceSetting я() {
      return this.в_at_0c50bf35;
   }

   @Generated
   public FloatSetting Юй() {
      return this.ХЮ;
   }

   @Override
   public List<ValueSetting<?>> getSettingsList() {
      return List.of(this.в_at_0c50bf35, this.ХЮ, this.Ви, this.В9, this.glassSetting);
   }

   @Generated
   public BooleanSetting getFromHeadSetting() {
      return this.fromHeadSetting;
   }

   @Generated
   public BooleanSetting щ() {
      return this.В3;
   }

   @Override
   public void onEnable() {
      try {
         RoseClient.getInstance().getServices().getThemes().restoreOpaqueInterfaceColors();
      } catch (Throwable var2) {
      }

      RoseClient.getInstance().getServices().getHudElements().enable();
   }

   @Generated
   public MultiChoiceSetting Б_3bdb855e() {
      return this.М;
   }

   @Generated
   public SingleChoiceSetting Й() {
      return this.с4;
   }

   @Generated
   public BooleanSetting Г_3276742a() {
      return this.В9;
   }

   @Generated
   public FloatSetting Юз() {
      return this.ХЧ;
   }

   @Generated
   public BooleanSetting Щ() {
      return this.ВO;
   }

   @Generated
   public FloatSetting Юк() {
      return this.Х1;
   }

   @Generated
   public FloatSetting Юр() {
      return this.ХЛ;
   }
}
