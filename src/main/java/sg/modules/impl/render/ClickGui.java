package sg.modules.impl.render;

import com.google.gson.JsonObject;
import lombok.Generated;
import settings.render.ClientLocalization;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.settings.FloatSetting;

public class ClickGui extends ClientModule {
   private final BooleanSetting сЭ;
   private final FloatSetting ВН;
   private final BooleanSetting с必;
   private final SingleChoiceSetting guiType;
   private static final String В1з = "ClickGui";
   private static final String В1ц = "Главное меню чита";
   private static final String GUI_TYPE_NAME = "Тип GUI";
   private static final String GUI_TYPE_FIRST = "First";
   private static final String GUI_TYPE_SECOND = "Second";
   private static final String GUI_TYPE_THIRD = "Third";
   private static final String В1я = "Блик";
   private static final String ВЬс = "Скорость анимации";
   private static final float ВЬВ = 0.1F;
   private static final float ВЬЯ = 3.0F;
   private static final float ВЬХ = 0.1F;
   private static final String ВЬе = "Фоновый шейдер";
   private static final String ВЬА = "Interface";
   private static final String ВЬ4 = "Interface";
   private static final String ВЬп = "Interface";
   private static final String ВЬД = "Interface";
   private static final String ВЬЪ = "Settings";
   private static final String ВЬт = "Settings";
   private static final String ВЬа = "Settings";
   private static final String ВЬЮ = "Язык";
   private static final String ВЬЧ = "English";
   private static final String ВЬЛ = "Язык";
   private static final float ВЬ诶 = 0.1F;
   private static final float ВЬ1 = 2000.0F;
   private static final float ВЬЬ = 0.55F;
   private static final float ВЬ衣 = 255.0F;
   private static final float ВЬш = 0.001F;
   private static final float ВЬИ = 0.45F;

   public float Ен() {
      float var1 = Math.max(ВЬ诶, this.ВН.getValue());
      float var2 = ВЬ1 / var1;
      return (float)(System.currentTimeMillis() % (long)var2) / var2;
   }

   @Override
   public void toggle() {
      // The client GUIs only make sense in a world: ignore the keybind in the main menu and server list.
      if (MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.player == null) {
         return;
      }
      this.Юр();
      if (this.isThirdGuiType() && MINECRAFT_CLIENT.gui.screen() instanceof sg.gui.third.ThirdGuiScreen thirdGui) {
         thirdGui.onClose();
         return;
      }
      if (MINECRAFT_CLIENT.gui.screen() instanceof sg.gui.third.ThirdGuiPanelScreen panel) {
         panel.onClose();
         return;
      }
      if (!this.isThirdGuiType() && !this.isSecondGuiType()
         && MINECRAFT_CLIENT.gui.screen() == RoseClient.getInstance().getSelfcodeService().У()) {
         MINECRAFT_CLIENT.gui.screen().onClose();
         return;
      }
      RoseClient.getInstance().getSelfcodeService().rememberParent();
      MINECRAFT_CLIENT.gui.setScreen(RoseClient.getInstance().getSelfcodeService().Уг());
   }

   public boolean isSecondGuiType() {
      return this.guiType.isSelected(GUI_TYPE_SECOND);
   }

   public boolean isThirdGuiType() {
      return this.guiType.isSelected(GUI_TYPE_THIRD);
   }

   public boolean иШ() {
      return this.с必.getValue();
   }

   @Generated
   public FloatSetting O() {
      return this.ВН;
   }

   public float ЕД() {
      return ВЬЬ * (UiDrawing.getGlobalAlpha() / ВЬ衣);
   }

   public boolean иб() {
      return this.сЭ.getValue();
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      if (var1 != null) {
         if (var1.has(ВЬ4) && var1.get(ВЬп).isJsonObject()) {
            ClickGuiPreferences.э(var1.getAsJsonObject(ВЬД));
         } else if (var1.has(ВЬЪ) && var1.get(ВЬт).isJsonObject()) {
            JsonObject var2 = var1.getAsJsonObject(ВЬа);
            if (var2.has(ВЬЮ)) {
               ClickGuiPreferences.Н(ВЬЧ.equals(var2.get(ВЬЛ).getAsString()));
            }
         }

         this.Юр();
      }
   }

   public ClickGui() {
      super(В1з, В1ц, ModuleCategory.RENDER);
      this.сЭ = new BooleanSetting(В1я, false);
      this.ВН = new FloatSetting(ВЬс, 1.0F, ВЬВ, ВЬЯ).withStep(ВЬХ).visibleWhen(this.сЭ::getValue);
      this.с必 = new BooleanSetting(ВЬе, true);
       this.guiType = new SingleChoiceSetting(GUI_TYPE_NAME, 2, new ChoiceOption(GUI_TYPE_FIRST), new ChoiceOption(GUI_TYPE_SECOND), new ChoiceOption(GUI_TYPE_THIRD));
      this.getKeybind().setKeyCode(344);
      this.addSettings(this.guiType, this.ВН, this.с必);
   }

   public void Юр() {
      ClientLocalization.setEnglishEnabled(this.и诶());
   }

   @Override
   public JsonObject saveConfig() {
      JsonObject var1 = super.saveConfig();
      var1.add(ВЬА, ClickGuiPreferences.Е());
      return var1;
   }

   @Generated
   public BooleanSetting б() {
      return this.сЭ;
   }

   public void Э(float var1, float var2, float var3, float var4, float var5) {
      if (this.иб()) {
         float var6 = this.ЕД();
         if (!(var6 <= ВЬш)) {
            UiDrawing.drawGlare(var1, var2, var3, var4, this.Ен(), var6, ВЬИ, var5);
         }
      }
   }

   @Generated
   public BooleanSetting Ц_3276742a() {
      return this.с必;
   }

   public boolean и诶() {
      return ClickGuiPreferences.й_();
   }
}
