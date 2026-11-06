package sg.modules.impl.render;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SurfaceButterfly;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public final class VoidButterflies extends ClientModule {
   private final FloatSetting Ж;
   private final FloatSetting м;
   private final SurfaceButterfly В;
   private final EventListener<PlayerTickEvent> д;
   private final EventListener<WorldEffectsEvent> Б;
   private static final String ВРе = "VoidButterflies";
   private static final String ВРА = "Бабочки пустоты на всех гранях блоков";
   private static final String ВР4 = "Радиус";
   private static final float ВРп = 12.0F;
   private static final float ВРД = 4.0F;
   private static final float ВРЪ = 24.0F;
   private static final String ВРт = "Плотность";
   private static final float ВРа = 0.25F;
   private static final float ВРЮ = 0.25F;

   @Override
   public void onEnable() {
      this.В.ыТ();
   }

   public VoidButterflies() {
      super(ВРе, ВРА, ModuleCategory.RENDER);
      this.Ж = new FloatSetting(ВР4, ВРп, ВРД, ВРЪ).withStep(1.0F);
      this.м = new FloatSetting(ВРт, 1.0F, ВРа, 2.0F).withStep(ВРЮ);
      this.В = new SurfaceButterfly();
      this.д = var1 -> this.В.С(this.Ж.getValue(), this.м.getValue());
      this.Б = var1 -> this.В.ъ(var1, this.Ж.getValue());
      this.addSettings(this.Ж, this.м);
   }

   @Override
   public void onDisable() {
      this.В.ыТ();
   }
}
