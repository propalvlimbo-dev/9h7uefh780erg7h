package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import settings.render.ReallyWorldRankGlyphs;
import sg.events.EventListener;
import sg.events.impl.ChatClosedEvent;
import sg.network.ScoreboardValueEditor;
import sg.network.NameProtectionTextRewriter;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.ChatMouseButtonEvent;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;
import sg.settings.TextSetting;

public class NameProtect extends ClientModule {
   private static final String ЯА = "Protected";
   private static final String Я4 = "rose";
   @SerializedName("Х")
   private final TextSetting Х_at_06d0ed97;
   private final SingleChoiceSetting се;
   private final BooleanSetting ВЩ;
   @SerializedName("е")
   private final TextSetting е_at_06d0ed97;
   private final BooleanSetting Ву;
   private final BooleanSetting В2;
   private final BooleanSetting Вф;
   private final EventListener<ChatMouseButtonEvent> Ям;
   private final EventListener<ChatClosedEvent> ЯК;
   private static final String сТЛ = "NameProtect";
   private static final String сТ诶 = "Скрывает ваш ник и ники друзей";
   private static final String сТ1 = "Custom name";
   private static final String сТЬ = "Protected";
   private static final String сТ衣 = "Донат";
   private static final String сТш = "Заменять клан";
   private static final String сТИ = "Клан";
   private static final String сТь = "rose";
   private static final String сТж = "Скрыть гриф";
   private static final String сТ弟 = "Скрыть ссылки";
   private static final String сТб = "Полный Кастом";
   private static final String сТЫ = "rose";
   private static final String сТl = "rose";
   private static final String сТ8 = "Protected";
   private static final String сТ5 = "Protected";
   private static final String сТв = "Default";
   private static final String сТъ = "Default";

   public int С(int var1) {
      return this.бO() && ReallyWorldRankGlyphs.Ь(var1) ? this.Ю() : var1;
   }

   public char Ю() {
      return ReallyWorldRankGlyphs.必(this.се.getSelectedOption().getName());
   }

   public boolean бЬ() {
      return this.Вф.getValue();
   }

   public boolean бO() {
      return !this.се.isSelected(сТв);
   }

   private static ChoiceOption[] У_47de6e54() {
      String[] var0 = ReallyWorldRankGlyphs.щ();
      ChoiceOption[] var1 = new ChoiceOption[var0.length + 1];
      var1[0] = new ChoiceOption(сТъ);

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2 + 1] = new ChoiceOption(var0[var2]);
      }

      return var1;
   }

   public String фР() {
      String var1 = this.е_at_06d0ed97.getValue();
      return var1 != null && !var1.isBlank() ? var1.trim() : сТЫ;
   }

   public String о(String var1) {
      if (var1 != null && !var1.isEmpty() && this.бO()) {
         char var2 = this.Ю();
         StringBuilder var3 = null;

         for (int var4 = 0; var4 < var1.length(); var4++) {
            char var5 = var1.charAt(var4);
            if (ReallyWorldRankGlyphs.Ь(var5)) {
               if (var3 == null) {
                  var3 = new StringBuilder(var1);
               }

               var3.setCharAt(var4, var2);
            }
         }

         return var3 == null ? var1 : var3.toString();
      } else {
         return var1;
      }
   }

   @Override
   public void onDisable() {
      ScoreboardValueEditor.зХ();
   }

   public void 西(String var1) {
      String var2 = Objects.requireNonNullElse(var1, "").trim();
      this.Х_at_06d0ed97.setValue(var2.isEmpty() ? сТ5 : var2);
      NameProtectionTextRewriter.ж0();
   }

   public String фи() {
      String var1 = this.Х_at_06d0ed97.getValue();
      return var1 != null && !var1.isBlank() ? var1 : сТ8;
   }

   public boolean бж() {
      return this.В2.getValue();
   }

   public void й(String var1) {
      String var2 = Objects.requireNonNullElse(var1, "").trim();
      this.е_at_06d0ed97.setValue(var2.isEmpty() ? сТl : var2);
   }

   public NameProtect() {
      super(сТЛ, сТ诶, ModuleCategory.MISC);
      this.Х_at_06d0ed97 = new TextSetting(сТ1, сТЬ);
      this.се = new SingleChoiceSetting(сТ衣, У_47de6e54());
      this.ВЩ = new BooleanSetting(сТш, false);
      this.е_at_06d0ed97 = new TextSetting(сТИ, сТь).visibleWhen(() -> this.ВЩ.getValue());
      this.Ву = new BooleanSetting(сТж, false);
      this.В2 = new BooleanSetting(сТ弟, false);
      this.Вф = new BooleanSetting(сТб, false);
      this.Ям = this::ф;
      this.ЯК = var0 -> ScoreboardValueEditor.зХ();
      this.addSettings(this.Х_at_06d0ed97, this.се, this.ВЩ, this.е_at_06d0ed97, this.Ву, this.В2, this.Вф);
   }

   public boolean бИ() {
      return this.Ву.getValue();
   }

   private void ф(ChatMouseButtonEvent var1) {
      if (!var1.isCancelled()) {
         if (ScoreboardValueEditor.ф(var1.getMouseX(), var1.getMouseY(), var1.getButton())) {
            var1.setCancelled(true);
         }
      }
   }

   public boolean бЧ() {
      return this.ВЩ.getValue();
   }
}
