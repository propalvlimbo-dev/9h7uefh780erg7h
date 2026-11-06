package sg.modules.impl.render;

import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AspectRatioEvent;
import sg.settings.FloatSetting;

public class AspectRatio extends ClientModule {
   private final FloatSetting ХП;
   private final EventListener<AspectRatioEvent> ХЖ;
   private static final String ВБс = "AspectRatio";
   private static final String ВБВ = "Изменяет соотношение сторон экрана";
   private static final String ВБЯ = "Значение";
   private static final float ВБХ = 1.7F;
   private static final float ВБе = 0.05F;

   public AspectRatio() {
      super(ВБс, ВБВ, ModuleCategory.RENDER);
      this.ХП = new FloatSetting(ВБЯ, ВБХ, 1.0F, 2.0F).withStep(ВБе);
      this.ХЖ = this::method_5;
      this.addSettings(this.ХП);
   }

   public void method_5(AspectRatioEvent var1) {
      var1.setAspectRatio(this.ХП.getValue());
   }
}
