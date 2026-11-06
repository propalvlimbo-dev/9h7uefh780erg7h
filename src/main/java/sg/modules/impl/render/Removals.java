package sg.modules.impl.render;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;
import sg.events.impl.RenderSuppressionQuery;

public class Removals extends ClientModule {
   private static volatile boolean Вl;
   private static volatile boolean В8;
   private final MultiChoiceSetting Ч;
   private boolean В5;
   private final EventListener<RenderSuppressionQuery> сю;
   private final EventListener<PlayerTickEvent> сЩ;
   private static final String ВЩн = "Removals";
   private static final String ВЩЦ = "Убирает выбранные визуальные элементы";
   private static final String ВЩч = "Применять на";
   private static final String ВЩЭ = "Тряска камеры";
   private static final String ВЩ必 = "Скорборд";
   private static final String ВЩщ = "Босс-бар";
   private static final String ВЩГ = "Частицы разрушения";
   private static final String ВЩл = "Дождь";
   private static final String ВЩэ = "Камера клип";
   private static final String ВЩю = "Тени";
   private static final String ВЩЩ = "Дым";
   private static final String ВЩу = "Снесение тотема";
   private static final String ВЩ2 = "Виньетка";
   private static final String ВЩф = "Голограммы";
   private static final String ВЩи = "Эффект здоровья";
   private static final String ВЩ9 = "Трава";
   private static final String ВЩо = "Плохие эффекты";
   private static final String ВЩO = "Свечение игроков";
   private static final String ВЩ7 = "Размытие под водой";
   private static final String ВЩР = "Туман в воде и лаве";
   private static final String ВЩ3 = "Огонь";
   private static final String ВЩр = "Рыхлый Снег";
   private static final String ВЩ6 = "Динамический ФОВ";
   private static final String ВЩ_ = "Тайтлы";
   private static final String ВЩШ = "Карты";
   private static final String ВЩы = "Огонь";
   private static final String ВЩй = "Босс-бар";
   private static final String ВЩЖ = "Скорборд";
   private static final String ВЩм = "Тайтлы";
   private static final String ВЩК = "Снесение тотема";
   private static final String ВЩI = "Тряска камеры";
   private static final String ВЩТ = "Камера клип";
   private static final String ВЩО = "Частицы разрушения";
   private static final String ВЩФ = "Дождь";
   private static final String ВЩЙ = "Тени";
   private static final String ВЩк = "Свечение игроков";
   private static final String ВЩг = "Дым";
   private static final String ВЩ0 = "Виньетка";
   private static final String ВЩх = "Голограммы";
   private static final String ВЩУ = "Трава";
   private static final String ВЩЗ = "Эффект здоровья";
   private static final String ВЩЕ = "Плохие эффекты";
   private static final String ВЩП = "Размытие под водой";
   private static final String ВЩ西 = "Динамический ФОВ";
   private static final String ВЩд = "Рыхлый Снег";
   private static final String ВЩБ = "Трава";
   private static final String ВЩС = "Трава";
   private static final String ВЩз = "Карты";
   private static final String ВЩц = "Туман в воде и лаве";

   public Removals() {
      super(ВЩн, ВЩЦ, ModuleCategory.RENDER);
      this.Ч = new MultiChoiceSetting(
         ВЩч,
         new ChoiceOption(ВЩЭ, true),
         new ChoiceOption(ВЩ必, true),
         new ChoiceOption(ВЩщ, true),
         new ChoiceOption(ВЩГ, true),
         new ChoiceOption(ВЩл, true),
         new ChoiceOption(ВЩэ, true),
         new ChoiceOption(ВЩю, true),
         new ChoiceOption(ВЩЩ, true),
         new ChoiceOption(ВЩу, true),
         new ChoiceOption(ВЩ2, true),
         new ChoiceOption(ВЩф, true),
         new ChoiceOption(ВЩи, true),
         new ChoiceOption(ВЩ9, true),
         new ChoiceOption(ВЩо, true),
         new ChoiceOption(ВЩO, true),
         new ChoiceOption(ВЩ7, true),
         new ChoiceOption(ВЩР, true),
         new ChoiceOption(ВЩ3, true),
         new ChoiceOption(ВЩр, true),
         new ChoiceOption(ВЩ6, true),
         new ChoiceOption(ВЩ_, true),
         new ChoiceOption(ВЩШ, true)
      );
      this.сю = this::method_6;
      this.сЩ = this::х;
      this.addSettings(this.Ч);
   }

   public static boolean ыЧ() {
      return Вl;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.В5 = this.Ч.isSelected(ВЩС);
      this.кЮ();
      this.кЗ();
   }

   public static boolean ыу() {
      return В8;
   }

   @Override
   public void onDisable() {
      Вl = false;
      В8 = false;
      this.кЗ();
      super.onDisable();
   }

   private void кЮ() {
      Вl = this.Ч.isSelected(ВЩз);
      В8 = this.Ч.isSelected(ВЩц);
   }

   private void х(PlayerTickEvent var1) {
      this.кЮ();
      boolean var2 = this.Ч.isSelected(ВЩБ);
      if (var2 != this.В5) {
         this.В5 = var2;
         this.кЗ();
      }
   }

   private void method_6(RenderSuppressionQuery var1) {
      boolean var2 = switch (var1.getEffect()) {
         case FIRE -> this.Ч.isSelected(ВЩы);
         case BOSSBAR -> this.Ч.isSelected(ВЩй);
         case SCOREBOARD -> this.Ч.isSelected(ВЩЖ);
         case TITLE -> this.Ч.isSelected(ВЩм);
         case TOTEM -> this.Ч.isSelected(ВЩК);
         case HURT_TIME -> this.Ч.isSelected(ВЩI);
         case CAMERA_CLIP -> this.Ч.isSelected(ВЩТ);
         case DESTROY_PARTICLES -> this.Ч.isSelected(ВЩО);
         case RAIN -> this.Ч.isSelected(ВЩФ);
         case SHADOWS -> this.Ч.isSelected(ВЩЙ);
         case GLOWING -> this.Ч.isSelected(ВЩк);
         case SMOKE -> this.Ч.isSelected(ВЩг);
         case VIGNETTE -> this.Ч.isSelected(ВЩ0);
         case HOLOGRAMS -> this.Ч.isSelected(ВЩх);
         case GRASS -> this.Ч.isSelected(ВЩУ);
         case HEALTH -> this.Ч.isSelected(ВЩЗ);
         case BAD_EFFECTS -> this.Ч.isSelected(ВЩЕ);
         case UNDERWATER_BLUR -> this.Ч.isSelected(ВЩП);
         case DYNAMIC_FOV -> this.Ч.isSelected(ВЩ西);
         case POWDER_SNOW -> this.Ч.isSelected(ВЩд);
      };
      if (var2) {
         var1.setCancelled(true);
      }
   }

   private void кЗ() {
      if (MINECRAFT_CLIENT.level != null) {
         MINECRAFT_CLIENT.levelExtractor.allChanged();
      }
   }
}
