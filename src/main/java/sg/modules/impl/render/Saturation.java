package sg.modules.impl.render;

import sg.events.EventListener;
import sg.render.pipeline.WorldColorGradingPass;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;

public class Saturation extends ClientModule {
   private final FloatSetting Як;
   private final FloatSetting Яг;
   private final FloatSetting Я0;
   private final FloatSetting Ях;
   private final FloatSetting ЯУ;
   private final FloatSetting ЯЗ;
   private final FloatSetting ЯЕ;
   private final FloatSetting ЯП;
   private final FloatSetting Я西;
   private final FloatSetting Яд;
   private final EventListener<HudRenderEvent> ЯГ;
   private static final String ЯЮэ = "Saturation";
   private static final String ЯЮю = "Расширенная цветокоррекция игры";
   private static final String ЯЮЩ = "Сила эффекта";
   private static final float ЯЮу = 0.05F;
   private static final String ЯЮ2 = "Экспозиция";
   private static final float ЯЮф = -2.0F;
   private static final float ЯЮи = 0.05F;
   private static final String ЯЮ9 = "Контрастность";
   private static final float ЯЮо = -1.0F;
   private static final float ЯЮO = 0.05F;
   private static final String ЯЮ7 = "Насыщенность";
   private static final float ЯЮР = -1.0F;
   private static final float ЯЮ3 = 0.05F;
   private static final String ЯЮр = "Сочность цветов";
   private static final float ЯЮ6 = -1.0F;
   private static final float ЯЮ_ = 0.05F;
   private static final String ЯЮШ = "Гамма";
   private static final float ЯЮы = -1.0F;
   private static final float ЯЮй = 0.05F;
   private static final String ЯЮЖ = "Температура";
   private static final float ЯЮм = -1.0F;
   private static final float ЯЮК = 0.05F;
   private static final String ЯЮI = "Оттенок";
   private static final float ЯЮТ = -1.0F;
   private static final float ЯЮО = 0.05F;
   private static final String ЯЮФ = "Резкость";
   private static final float ЯЮЙ = 0.05F;
   private static final String ЯЮк = "Виньетка";
   private static final float ЯЮг = 0.05F;

   public void method_1(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         WorldColorGradingPass.INSTANCE
            .applyColorGrade(this.Як.getValue(), this.Яг.getValue(), this.Я0.getValue(), this.Ях.getValue(), this.ЯУ.getValue(), this.ЯЗ.getValue(), this.ЯЕ.getValue(), this.ЯП.getValue(), this.Я西.getValue(), this.Яд.getValue());
      }
   }

   public Saturation() {
      super(ЯЮэ, ЯЮю, ModuleCategory.RENDER);
      this.Як = new FloatSetting(ЯЮЩ, 0.0F, 0.0F, 1.0F).withStep(ЯЮу);
      this.Яг = new FloatSetting(ЯЮ2, 0.0F, ЯЮф, 2.0F).withStep(ЯЮи);
      this.Я0 = new FloatSetting(ЯЮ9, 0.0F, ЯЮо, 1.0F).withStep(ЯЮO);
      this.Ях = new FloatSetting(ЯЮ7, 0.0F, ЯЮР, 2.0F).withStep(ЯЮ3);
      this.ЯУ = new FloatSetting(ЯЮр, 0.0F, ЯЮ6, 1.0F).withStep(ЯЮ_);
      this.ЯЗ = new FloatSetting(ЯЮШ, 0.0F, ЯЮы, 1.0F).withStep(ЯЮй);
      this.ЯЕ = new FloatSetting(ЯЮЖ, 0.0F, ЯЮм, 1.0F).withStep(ЯЮК);
      this.ЯП = new FloatSetting(ЯЮI, 0.0F, ЯЮТ, 1.0F).withStep(ЯЮО);
      this.Я西 = new FloatSetting(ЯЮФ, 0.0F, 0.0F, 1.0F).withStep(ЯЮЙ);
      this.Яд = new FloatSetting(ЯЮк, 0.0F, 0.0F, 1.0F).withStep(ЯЮг);
      this.ЯГ = this::method_1;
      this.addSettings(this.Як, this.Яг, this.Я0, this.Ях, this.ЯУ, this.ЯЗ, this.ЯЕ, this.ЯП, this.Я西, this.Яд);
   }
}
