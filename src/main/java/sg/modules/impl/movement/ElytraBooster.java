package sg.modules.impl.movement;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.FireworkBoostEvent;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.settings.SectionHeaderSetting;
import sg.settings.SingleChoiceSetting;

public class ElytraBooster extends ClientModule {
   private static final long Вй_6da43b94 = 190000000L;
   private static final float АЖ = 6.0F;
   private static final int ец = 20;
   public final SingleChoiceSetting О;
   public final BooleanSetting сы;
   @SerializedName("В")
   public final SectionHeaderSetting В_4e6efa04;
   @SerializedName("Вй")
   public final FloatSetting Вй_989fed9a;
   public final FloatSetting ВЖ;
   public final FloatSetting Вм;
   public final FloatSetting ВК;
   public final FloatSetting ВI;
   public final FloatSetting ВТ;
   public final FloatSetting ВО;
   public final FloatSetting ВФ;
   public final FloatSetting ВЙ;
   @SerializedName("Я")
   public final SectionHeaderSetting Я_4e6efa04_at_82514a04;
   public final FloatSetting Вк;
   public final FloatSetting Вг;
   public final FloatSetting В0;
   public final FloatSetting Вх;
   public final FloatSetting ВУ;
   public final FloatSetting ВЗ;
   public final FloatSetting ВЕ;
   public final FloatSetting ВП;
   public final FloatSetting В西;
   @SerializedName("В")
   private final ElytraBooster.AnimatedNumber В_d316c06a;
   @SerializedName("Я")
   private final ElytraBooster.AnimatedNumber Я_d316c06a_at_82514a04;
   private final EventListener<HudRenderEvent> В9;
   private final EventListener<FireworkBoostEvent> Во;
   private static final String ВГд = "ElytraBooster";
   private static final String ВГБ = "Усиливает ускорение от фейерверков при полёте на элитрах";
   private static final String ВГС = "Мод";
   private static final String ВГз = "ReallyWorld 1.16.5";
   private static final String ВГц = "ReallyWorld 1.21.11";
   private static final String ВГя = "BravoHvH";
   private static final String Влс = "Custom";
   private static final String ВлВ = "Рисовать текущий Yaw или Pitch";
   private static final String ВлЯ = "Бустер по X";
   private static final String ВлХ = "0-5 По X";
   private static final float Вле = 1.5F;
   private static final float ВлА = 1.5F;
   private static final float Вл4 = 2.5F;
   private static final float Влп = 0.01F;
   private static final String ВлД = "5-10 По X";
   private static final float ВлЪ = 1.5F;
   private static final float Влт = 1.5F;
   private static final float Вла = 2.5F;
   private static final float ВлЮ = 0.01F;
   private static final String ВлЧ = "10-15 По X";
   private static final float ВлЛ = 1.5F;
   private static final float Вл诶 = 1.5F;
   private static final float Вл1 = 2.5F;
   private static final float ВлЬ = 0.01F;
   private static final String Вл衣 = "15-20 По X";
   private static final float Влш = 1.5F;
   private static final float ВлИ = 1.5F;
   private static final float Вль = 2.5F;
   private static final float Влж = 0.01F;
   private static final String Вл弟 = "20-25 По X";
   private static final float Влб = 1.5F;
   private static final float ВлЫ = 1.5F;
   private static final float Влl = 2.5F;
   private static final float Вл8 = 0.01F;
   private static final String Вл5 = "25-30 По X";
   private static final float Влв = 1.5F;
   private static final float Влъ = 1.5F;
   private static final float ВлМ = 2.5F;
   private static final float ВлН = 0.01F;
   private static final String Влн = "30-35 По X";
   private static final float ВлЦ = 1.5F;
   private static final float Влч = 1.5F;
   private static final float ВлЭ = 2.5F;
   private static final float Вл必 = 0.01F;
   private static final String Влщ = "35-40 По X";
   private static final float ВлГ = 1.5F;
   private static final float Влл = 1.5F;
   private static final float Влэ = 2.5F;
   private static final float Влю = 0.01F;
   private static final String ВлЩ = "40-45 По X";
   private static final float Влу = 1.5F;
   private static final float Вл2 = 1.5F;
   private static final float Влф = 2.5F;
   private static final float Вли = 0.01F;
   private static final String Вл9 = "Бустер по Y";
   private static final String Вло = "0-5 По Y";
   private static final float ВлO = 1.5F;
   private static final float Вл7 = 1.5F;
   private static final float ВлР = 2.5F;
   private static final float Вл3 = 0.01F;
   private static final String Влр = "5-10 По Y";
   private static final float Вл6 = 1.5F;
   private static final float Вл_ = 1.5F;
   private static final float ВлШ = 2.5F;
   private static final float Влы = 0.01F;
   private static final String Влй = "10-15 По Y";
   private static final float ВлЖ = 1.5F;
   private static final float Влм = 1.5F;
   private static final float ВлК = 2.5F;
   private static final float ВлI = 0.01F;
   private static final String ВлТ = "15-20 По Y";
   private static final float ВлО = 1.5F;
   private static final float ВлФ = 1.5F;
   private static final float ВлЙ = 2.5F;
   private static final float Влк = 0.01F;
   private static final String Влг = "20-25 По Y";
   private static final float Вл0 = 1.5F;
   private static final float Влх = 1.5F;
   private static final float ВлУ = 2.5F;
   private static final float ВлЗ = 0.01F;
   private static final String ВлЕ = "25-30 По Y";
   private static final float ВлП = 1.5F;
   private static final float Вл西 = 1.5F;
   private static final float Влд = 2.5F;
   private static final float ВлБ = 0.01F;
   private static final String ВлС = "30-35 По Y";
   private static final float Влз = 1.5F;
   private static final float Влц = 1.5F;
   private static final float Вля = 2.5F;
   private static final float Вэс = 0.01F;
   private static final String ВэВ = "35-40 По Y";
   private static final float ВэЯ = 1.5F;
   private static final float ВэХ = 1.5F;
   private static final float Вэе = 2.5F;
   private static final float ВэА = 0.01F;
   private static final String Вэ4 = "40-45 По Y";
   private static final float Вэп = 1.5F;
   private static final float ВэД = 1.5F;
   private static final float ВэЪ = 2.5F;
   private static final float Вэт = 0.01F;
   private static final String Вэа = "Settings";
   private static final String ВэЮ = "Settings";
   private static final String ВэЧ = "Settings";
   private static final String ВэЛ = "ReallyWorld";
   private static final String Вэ诶 = "ReallyWorld 1.16.5";
   private static final String Вэ1 = "ReallyWorld";
   private static final String ВэЬ = "ReallyWorld 1.16.5";
   private static final String Вэ衣 = "ReallyWorld 1.16.5";
   private static final String Вэш = "ReallyWorld";
   private static final String ВэИ = "Yaw: ";
   private static final float Вэь = 40.0F;
   private static final String Вэж = "Pitch: ";
   private static final float Вэ弟 = 70.0F;
   private static final float Вэб = 20.0F;
   private static final float ВэЫ = 1.05F;
   private static final double Вэl = 3.0;
   private static final float Вэ8 = 6.0F;
   private static final double Вэ5 = Math.PI;
   private static final float Вэв = 0.28F;
   private static final float Вэъ = 6.0F;
   private static final float ВэМ = 6.0F;
   private static final String ВэН = "°";
   private static final float Вэн = 20.0F;
   private static final float ВэЦ = 1.5F;
   private static final float Вэч = 1.5F;
   private static final float ВэЭ = 45.0F;
   private static final float Вэ必 = 90.0F;
   private static final float Вэщ = 45.0F;
   private static final float ВэГ = 90.0F;
   private static final String Вэл = "ReallyWorld 1.16.5";
   private static final float Вээ = 5.0F;
   private static final float Вэю = 5.0F;
   private static final float ВэЩ = 10.0F;
   private static final float Вэу = 10.0F;
   private static final float Вэ2 = 15.0F;
   private static final float Вэф = 15.0F;
   private static final float Вэи = 20.0F;
   private static final float Вэ9 = 20.0F;
   private static final float Вэо = 25.0F;
   private static final float ВэO = 25.0F;
   private static final float Вэ7 = 30.0F;
   private static final float ВэР = 30.0F;
   private static final float Вэ3 = 35.0F;
   private static final float Вэр = 35.0F;
   private static final float Вэ6 = 40.0F;
   private static final float Вэ_ = 40.0F;
   private static final float ВэШ = 45.0F;
   private static final float Вэы = 45.0F;
   private static final float Вэй = 1.81F;
   private static final float ВэЖ = 1.81F;
   private static final float Вэм = 1.81F;
   private static final float ВэК = 1.81F;
   private static final float ВэI = 1.73F;
   private static final float ВэТ = 1.71F;
   private static final float ВэО = 1.66F;
   private static final float ВэФ = 1.63F;
   private static final float ВэЙ = 1.6F;
   private static final float Вэк = 5.0F;
   private static final float Вэг = 1.6F;
   private static final float Вэ0 = 10.0F;
   private static final float Вэх = 1.62F;
   private static final float ВэУ = 15.0F;
   private static final float ВэЗ = 1.6F;
   private static final float ВэЕ = 20.0F;
   private static final float ВэП = 1.62F;
   private static final float Вэ西 = 25.0F;
   private static final float Вэд = 1.64F;
   private static final float ВэБ = 30.0F;
   private static final float ВэС = 1.68F;
   private static final float Вэз = 35.0F;
   private static final float Вэц = 1.75F;
   private static final float Вэя = 40.0F;
   private static final float Вюс = 1.9F;
   private static final float ВюВ = 43.0F;
   private static final float ВюЯ = 45.0F;
   private static final float ВюХ = 2.26F;
   private static final float Вюе = 2.16F;
   private static final String ВюА = "ReallyWorld 1.21.11";
   private static final float Вю4 = 30.0F;
   private static final float Вюп = 30.0F;
   private static final float ВюД = 1.53F;
   private static final float ВюЪ = 40.0F;
   private static final float Вют = 40.0F;
   private static final float Вюа = 2.04F;
   private static final float ВюЮ = 2.059F;
   private static final float ВюЧ = 30.0F;
   private static final float ВюЛ = 1.53F;
   private static final float Вю诶 = 2.02F;
   private static final String Вю1 = "BravoHvH";
   private static final float ВюЬ = 5.0F;
   private static final float Вю衣 = 5.0F;
   private static final float Вюш = 10.0F;
   private static final float ВюИ = 10.0F;
   private static final float Вюь = 15.0F;
   private static final float Вюж = 15.0F;
   private static final float Вю弟 = 20.0F;
   private static final float Вюб = 20.0F;
   private static final float ВюЫ = 25.0F;
   private static final float Вюl = 25.0F;
   private static final float Вю8 = 30.0F;
   private static final float Вю5 = 30.0F;
   private static final float Вюв = 35.0F;
   private static final float Вюъ = 35.0F;
   private static final float ВюМ = 40.0F;
   private static final float ВюН = 40.0F;
   private static final float Вюн = 45.0F;
   private static final float ВюЦ = 45.0F;
   private static final float Вюч = 2.05F;
   private static final float ВюЭ = 2.05F;
   private static final float Вю必 = 2.01F;
   private static final float Вющ = 1.87F;
   private static final float ВюГ = 1.77F;
   private static final float Вюл = 1.73F;
   private static final float Вюэ = 1.68F;
   private static final float Вюю = 1.67F;
   private static final float ВюЩ = 1.64F;
   private static final float Вюу = 5.0F;
   private static final float Вю2 = 1.65F;
   private static final float Вюф = 10.0F;
   private static final float Вюи = 1.7F;
   private static final float Вю9 = 15.0F;
   private static final float Вюо = 1.71F;
   private static final float ВюO = 20.0F;
   private static final float Вю7 = 1.74F;
   private static final float ВюР = 25.0F;
   private static final float Вю3 = 1.79F;
   private static final float Вюр = 30.0F;
   private static final float Вю6 = 1.91F;
   private static final float Вю_ = 35.0F;
   private static final float ВюШ = 2.01F;
   private static final float Вюы = 40.0F;
   private static final float Вюй = 2.02F;
   private static final float ВюЖ = 2.02F;
   private static final String Вюм = "Custom";
   private static final float ВюК = 5.0F;
   private static final float ВюI = 5.0F;
   private static final float ВюТ = 10.0F;
   private static final float ВюО = 10.0F;
   private static final float ВюФ = 15.0F;
   private static final float ВюЙ = 15.0F;
   private static final float Вюк = 20.0F;
   private static final float Вюг = 20.0F;
   private static final float Вю0 = 25.0F;
   private static final float Вюх = 25.0F;
   private static final float ВюУ = 30.0F;
   private static final float ВюЗ = 30.0F;
   private static final float ВюЕ = 35.0F;
   private static final float ВюП = 35.0F;
   private static final float Вю西 = 40.0F;
   private static final float Вюд = 40.0F;
   private static final float ВюБ = 45.0F;
   private static final float ВюС = 45.0F;
   private static final float Вюз = 5.0F;
   private static final float Вюц = 10.0F;
   private static final float Вюя = 15.0F;
   private static final float ВЩс = 20.0F;
   private static final float ВЩВ = 25.0F;
   private static final float ВЩЯ = 30.0F;
   private static final float ВЩХ = 35.0F;
   private static final float ВЩе = 40.0F;
   private static final float ВЩА = 1.5F;
   private static final float ВЩ4 = 1.5F;
   private static final float ВЩп = 1.5F;
   private static final float ВЩД = 180.0F;
   private static final float ВЩЪ = 90.0F;
   private static final float ВЩт = 180.0F;
   private static final float ВЩа = -90.0F;
   private static final float ВЩЮ = 180.0F;
   private static final String ВЩЧ = "Custom";
   private static final String ВЩЛ = "Custom";
   private static final String ВЩ诶 = "Custom";
   private static final String ВЩ1 = "Custom";
   private static final String ВЩЬ = "Custom";
   private static final String ВЩ衣 = "Custom";
   private static final String ВЩш = "Custom";
   private static final String ВЩИ = "Custom";
   private static final String ВЩь = "Custom";
   private static final String ВЩж = "Custom";
   private static final String ВЩ弟 = "Custom";
   private static final String ВЩб = "Custom";
   private static final String ВЩЫ = "Custom";
   private static final String ВЩl = "Custom";
   private static final String ВЩ8 = "Custom";
   private static final String ВЩ5 = "Custom";
   private static final String ВЩв = "Custom";
   private static final String ВЩъ = "Custom";
   private static final String ВЩМ = "Custom";
   private static final String ВЩН = "Custom";

   @Override
   public void loadConfig(JsonObject var1) {
      if (var1 != null && var1.has(Вэа) && var1.get(ВэЮ).isJsonObject()) {
         var1 = var1.deepCopy();
         JsonObject var2 = var1.getAsJsonObject(ВэЧ);
         if (var2.has(this.О.getName()) && var2.get(this.О.getName()).isJsonPrimitive() && ВэЛ.equals(var2.get(this.О.getName()).getAsString())) {
            var2.addProperty(this.О.getName(), Вэ诶);
         }

         String var3 = this.О.getName() + " Binds";
         if (var2.has(var3) && var2.get(var3).isJsonObject()) {
            JsonObject var4 = var2.getAsJsonObject(var3);
            if (var4.has(Вэ1) && !var4.has(ВэЬ)) {
               var4.add(Вэ衣, var4.get(Вэш));
            }
         }
      }

      super.loadConfig(var1);
   }

   public ElytraBooster() {
      super(ВГд, ВГБ, ModuleCategory.MOVEMENT);
      this.О = new SingleChoiceSetting(ВГС, new ChoiceOption(ВГз), new ChoiceOption(ВГц), new ChoiceOption(ВГя), new ChoiceOption(Влс));
      this.сы = new BooleanSetting(ВлВ, false);
      this.В_4e6efa04 = new SectionHeaderSetting(ВлЯ, () -> this.О.isSelected(ВЩН));
      this.Вй_989fed9a = new FloatSetting(ВлХ, Вле, ВлА, Вл4).withStep(Влп).visibleWhen(() -> this.О.isSelected(ВЩМ));
      this.ВЖ = new FloatSetting(ВлД, ВлЪ, Влт, Вла).withStep(ВлЮ).visibleWhen(() -> this.О.isSelected(ВЩъ));
      this.Вм = new FloatSetting(ВлЧ, ВлЛ, Вл诶, Вл1).withStep(ВлЬ).visibleWhen(() -> this.О.isSelected(ВЩв));
      this.ВК = new FloatSetting(Вл衣, Влш, ВлИ, Вль).withStep(Влж).visibleWhen(() -> this.О.isSelected(ВЩ5));
      this.ВI = new FloatSetting(Вл弟, Влб, ВлЫ, Влl).withStep(Вл8).visibleWhen(() -> this.О.isSelected(ВЩ8));
      this.ВТ = new FloatSetting(Вл5, Влв, Влъ, ВлМ).withStep(ВлН).visibleWhen(() -> this.О.isSelected(ВЩl));
      this.ВО = new FloatSetting(Влн, ВлЦ, Влч, ВлЭ).withStep(Вл必).visibleWhen(() -> this.О.isSelected(ВЩЫ));
      this.ВФ = new FloatSetting(Влщ, ВлГ, Влл, Влэ).withStep(Влю).visibleWhen(() -> this.О.isSelected(ВЩб));
      this.ВЙ = new FloatSetting(ВлЩ, Влу, Вл2, Влф).withStep(Вли).visibleWhen(() -> this.О.isSelected(ВЩ弟));
      this.Я_4e6efa04_at_82514a04 = new SectionHeaderSetting(Вл9, () -> this.О.isSelected(ВЩж));
      this.Вк = new FloatSetting(Вло, ВлO, Вл7, ВлР).withStep(Вл3).visibleWhen(() -> this.О.isSelected(ВЩь));
      this.Вг = new FloatSetting(Влр, Вл6, Вл_, ВлШ).withStep(Влы).visibleWhen(() -> this.О.isSelected(ВЩИ));
      this.В0 = new FloatSetting(Влй, ВлЖ, Влм, ВлК).withStep(ВлI).visibleWhen(() -> this.О.isSelected(ВЩш));
      this.Вх = new FloatSetting(ВлТ, ВлО, ВлФ, ВлЙ).withStep(Влк).visibleWhen(() -> this.О.isSelected(ВЩ衣));
      this.ВУ = new FloatSetting(Влг, Вл0, Влх, ВлУ).withStep(ВлЗ).visibleWhen(() -> this.О.isSelected(ВЩЬ));
      this.ВЗ = new FloatSetting(ВлЕ, ВлП, Вл西, Влд).withStep(ВлБ).visibleWhen(() -> this.О.isSelected(ВЩ1));
      this.ВЕ = new FloatSetting(ВлС, Влз, Влц, Вля).withStep(Вэс).visibleWhen(() -> this.О.isSelected(ВЩ诶));
      this.ВП = new FloatSetting(ВэВ, ВэЯ, ВэХ, Вэе).withStep(ВэА).visibleWhen(() -> this.О.isSelected(ВЩЛ));
      this.В西 = new FloatSetting(Вэ4, Вэп, ВэД, ВэЪ).withStep(Вэт).visibleWhen(() -> this.О.isSelected(ВЩЧ));
      this.В_d316c06a = new ElytraBooster.AnimatedNumber();
      this.Я_d316c06a_at_82514a04 = new ElytraBooster.AnimatedNumber();
      this.В9 = this::Н;
      this.Во = this::М;
      this.addSettings(
         this.О,
         this.В_4e6efa04,
         this.Вй_989fed9a,
         this.ВЖ,
         this.Вм,
         this.ВК,
         this.ВI,
         this.ВТ,
         this.ВО,
         this.ВФ,
         this.ВЙ,
         this.Я_4e6efa04_at_82514a04,
         this.Вк,
         this.Вг,
         this.В0,
         this.Вх,
         this.ВУ,
         this.ВЗ,
         this.ВЕ,
         this.ВП,
         this.В西,
         this.сы
      );
   }

   private float method_3(String var1) {
      return ClientFonts.getMediumFont().getFontFace().measureTextWidth(var1, Вэн, 0.0F);
   }

   public float М4() {
      FireworkBoostEvent var1 = new FireworkBoostEvent(ВЩА, ВЩ4, ВЩп);
      this.М(var1);
      return var1.getXBoostMultiplier();
   }

   public void Н(HudRenderEvent var1) {
      if (this.сы.getValue() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         int var2 = (int)this.О7(MINECRAFT_CLIENT.player.getYRot());
         int var3 = (int)this.О7(MINECRAFT_CLIENT.player.getXRot());
         this.В_d316c06a.ю(var2);
         this.Я_d316c06a_at_82514a04.ю(var3);
         float var4 = MINECRAFT_CLIENT.getWindow().getScreenWidth() / 2.0F;
         float var5 = MINECRAFT_CLIENT.getWindow().getScreenHeight() / 2.0F;
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());
         this.М(ВэИ, this.В_d316c06a, var4, var5 + Вэь);
         this.М(Вэж, this.Я_d316c06a_at_82514a04, var4, var5 + Вэ弟);
         UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
      }
   }

   private float О7(float var1) {
      float var2 = var1 % ВЩД;
      if (var2 > ВЩЪ) {
         var2 -= ВЩт;
      } else if (var2 < ВЩа) {
         var2 += ВЩЮ;
      }

      return var2;
   }

   private void М(String var1, ElytraBooster.AnimatedNumber var2, float var3, float var4) {
      String var5 = var2.method_8ф();
      String var6 = var1 + var5 + "°";
      float var7 = var3 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var6, Вэб, ВэЫ) / 2.0F;
      int var8 = ColorMath.packRgb(235, 235, 235);
      int var9 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var1, var7, var4, 20, var8);
      float var10 = var7 + this.method_3(var1);
      long var11 = System.nanoTime();

      for (int var13 = 0; var13 < var2.сЫ.size(); var13++) {
         ElytraBooster.AnimatedDigit var14 = var2.сЫ.get(var13);
         float var15 = var10 + this.method_3(var5.substring(0, var13));
         float var16 = var14.弟(var11);
         float var17 = 1.0F - (float)Math.pow(1.0F - var16, Вэl);
         if (var16 < 1.0F) {
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var14.Хп, var15, var4 + var17 * Вэ8, 20, ColorMath.withAlpha(var8, 1.0F - var17));
            float var18 = (float)Math.sin(var16 * Вэ5);
            int var19 = ColorMath.blendArgb(var8, var9, var18 * Вэв);
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var14.ХД, var15, var4 - Вэъ + var17 * ВэМ, 20, ColorMath.withAlpha(var19, var17));
         } else {
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var14.ХД, var15, var4, 20, var8);
         }
      }

      float var20 = var10 + this.method_3(var5);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), ВэН, var20, var4, 20, var8);
   }

   public void М(FireworkBoostEvent var1) {
      float var4 = Mth.wrapDegrees(MINECRAFT_CLIENT.player.getYRot());
      float var5 = MINECRAFT_CLIENT.player.getXRot();
      float var6 = this.О7(var4);
      float var7 = Math.abs(var6);
      float var8 = ВэЦ;
      float var9 = Вэч;
      if (var7 > ВэЭ) {
         var7 = Вэ必 - var7;
      }

      float var10 = this.О7(var5);
      float var11 = Math.abs(var10);
      if (var11 > Вэщ) {
         var11 = ВэГ - var11;
      }

      if (this.О.isSelected(Вэл)) {
         if (var7 <= Вээ && var11 <= Вэю) {
            var8 = ВэЙ;
         } else if (var7 <= ВэЩ && var11 <= Вэу) {
            var8 = ВэФ;
         } else if (var7 <= Вэ2 && var11 <= Вэф) {
            var8 = ВэО;
         } else if (var7 <= Вэи && var11 <= Вэ9) {
            var8 = ВэТ;
         } else if (var7 <= Вэо && var11 <= ВэO) {
            var8 = ВэI;
         } else if (var7 <= Вэ7 && var11 <= ВэР) {
            var8 = ВэК;
         } else if (var7 <= Вэ3 && var11 <= Вэр) {
            var8 = Вэм;
         } else if (var7 <= Вэ6 && var11 <= Вэ_) {
            var8 = ВэЖ;
         } else if (var7 <= ВэШ || var11 <= Вэы) {
            var8 = Вэй;
         }

         if (var11 <= Вэк) {
            var9 = Вэг;
         } else if (var11 <= Вэ0) {
            var9 = Вэх;
         } else if (var11 <= ВэУ) {
            var9 = ВэЗ;
         } else if (var11 <= ВэЕ) {
            var9 = ВэП;
         } else if (var11 <= Вэ西) {
            var9 = Вэд;
         } else if (var11 <= ВэБ) {
            var9 = ВэС;
         } else if (var11 <= Вэз) {
            var9 = Вэц;
         } else if (var11 <= Вэя) {
            var9 = Вюс;
         } else if (var11 >= ВюВ && var11 <= ВюЯ) {
            var9 = ВюХ;
         } else {
            var9 = Вюе;
         }

         if (var9 > var8) {
            var8 = var9;
         }
      }

      if (this.О.isSelected(ВюА)) {
         if (var7 <= Вю4 && var11 <= Вюп) {
            var8 = ВюД;
         } else if (var7 <= ВюЪ && var11 <= Вют) {
            var8 = Вюа;
         } else {
            var8 = ВюЮ;
         }

         var9 = var11 <= ВюЧ ? ВюЛ : Вю诶;
         var8 = Math.max(var8, var9);
      }

      if (this.О.isSelected(Вю1)) {
         if (var7 <= ВюЬ && var11 <= Вю衣) {
            var8 = ВюЩ;
         } else if (var7 <= Вюш && var11 <= ВюИ) {
            var8 = Вюю;
         } else if (var7 <= Вюь && var11 <= Вюж) {
            var8 = Вюэ;
         } else if (var7 <= Вю弟 && var11 <= Вюб) {
            var8 = Вюл;
         } else if (var7 <= ВюЫ && var11 <= Вюl) {
            var8 = ВюГ;
         } else if (var7 <= Вю8 && var11 <= Вю5) {
            var8 = Вющ;
         } else if (var7 <= Вюв && var11 <= Вюъ) {
            var8 = Вю必;
         } else if (var7 <= ВюМ && var11 <= ВюН) {
            var8 = ВюЭ;
         } else if (var7 <= Вюн || var11 <= ВюЦ) {
            var8 = Вюч;
         }

         if (var11 <= Вюу) {
            var9 = Вю2;
         } else if (var11 <= Вюф) {
            var9 = Вюи;
         } else if (var11 <= Вю9) {
            var9 = Вюо;
         } else if (var11 <= ВюO) {
            var9 = Вю7;
         } else if (var11 <= ВюР) {
            var9 = Вю3;
         } else if (var11 <= Вюр) {
            var9 = Вю6;
         } else if (var11 <= Вю_) {
            var9 = ВюШ;
         } else if (var11 <= Вюы) {
            var9 = Вюй;
         } else {
            var9 = ВюЖ;
         }
      }

      if (this.О.isSelected(Вюм)) {
         if (var7 <= ВюК && var11 <= ВюI) {
            var8 = this.Вй_989fed9a.getValue();
         } else if (var7 <= ВюТ && var11 <= ВюО) {
            var8 = this.ВЖ.getValue();
         } else if (var7 <= ВюФ && var11 <= ВюЙ) {
            var8 = this.Вм.getValue();
         } else if (var7 <= Вюк && var11 <= Вюг) {
            var8 = this.ВК.getValue();
         } else if (var7 <= Вю0 && var11 <= Вюх) {
            var8 = this.ВI.getValue();
         } else if (var7 <= ВюУ && var11 <= ВюЗ) {
            var8 = this.ВТ.getValue();
         } else if (var7 <= ВюЕ && var11 <= ВюП) {
            var8 = this.ВО.getValue();
         } else if (var7 <= Вю西 && var11 <= Вюд) {
            var8 = this.ВФ.getValue();
         } else if (var7 <= ВюБ || var11 <= ВюС) {
            var8 = this.ВЙ.getValue();
         }

         if (var11 <= Вюз) {
            var9 = this.Вк.getValue();
         } else if (var11 <= Вюц) {
            var9 = this.Вг.getValue();
         } else if (var11 <= Вюя) {
            var9 = this.В0.getValue();
         } else if (var11 <= ВЩс) {
            var9 = this.Вх.getValue();
         } else if (var11 <= ВЩВ) {
            var9 = this.ВУ.getValue();
         } else if (var11 <= ВЩЯ) {
            var9 = this.ВЗ.getValue();
         } else if (var11 <= ВЩХ) {
            var9 = this.ВЕ.getValue();
         } else if (var11 <= ВЩе) {
            var9 = this.ВП.getValue();
         } else {
            var9 = this.В西.getValue();
         }

         if (var9 > var8) {
            var8 = var9;
         }
      }

      var1.setXBoostMultiplier(var8);
      var1.setYBoostMultiplier(var9);
      var1.setZBoostMultiplier(var8);
   }

   private static final class AnimatedNumber {
      private final List<ElytraBooster.AnimatedDigit> сЫ = new ArrayList<>();
      private String Вш = "";
      private Integer Я;

      private boolean дЬ() {
         long var1 = System.nanoTime();

         for (ElytraBooster.AnimatedDigit var4 : this.сЫ) {
            if (var4.弟(var1) < 1.0F) {
               return true;
            }
         }

         return false;
      }

      private void М(String var1) {
         if (var1.length() != this.сЫ.size()) {
            this.Вш = var1;
            this.в(var1);
         } else {
            long var2 = System.nanoTime();

            for (int var4 = 0; var4 < var1.length(); var4++) {
               this.сЫ.get(var4).必(var1.charAt(var4), var2);
            }

            this.Вш = var1;
         }
      }

      private String method_8ф() {
         return this.Вш;
      }

      private void в(String var1) {
         this.сЫ.clear();

         for (int var2 = 0; var2 < var1.length(); var2++) {
            this.сЫ.add(new ElytraBooster.AnimatedDigit(var1.charAt(var2)));
         }
      }

      private void ю(int var1) {
         String var2 = Integer.toString(var1);
         if (this.Вш.isEmpty()) {
            this.Вш = var2;
            this.в(var2);
         } else {
            this.Я = this.Вш.equals(var2) ? null : var1;
            if (!this.дЬ() && this.Я != null) {
               String var3 = Integer.toString(this.Я);
               this.Я = null;
               this.М(var3);
            }
         }
      }
   }

   private static final class AnimatedDigit {
      private String Хп;
      private String ХД;
      private long еп;
      private static final float Я2н = 1.9E8F;

      private void 必(char var1, long var2) {
         String var4 = Character.toString(var1);
         if (!this.ХД.equals(var4)) {
            this.Хп = this.ХД;
            this.ХД = var4;
            this.еп = var2;
         }
      }

      private float 弟(long var1) {
         if (this.еп == 0L) {
            return 1.0F;
         }

         float var3 = (float)(var1 - this.еп) / Я2н;
         return Math.max(0.0F, Math.min(1.0F, var3));
      }

      private AnimatedDigit(char var1) {
         this.Хп = Character.toString(var1);
         this.ХД = this.Хп;
         this.еп = 0L;
      }
   }
}
