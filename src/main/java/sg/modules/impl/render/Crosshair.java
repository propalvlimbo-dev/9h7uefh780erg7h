package sg.modules.impl.render;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;

public class Crosshair extends ClientModule {
   private final FloatSetting ЯГ;
   private final FloatSetting Ял;
   private final FloatSetting Яэ;
   private final BooleanSetting Вт;
   private final FloatSetting Яю;
   private final BooleanSetting Ва;
   private final ColorSetting щ;
   private final ColorSetting Г;
   private final EventListener<HudRenderEvent.CrosshairRender> ЯА;
   private final EventListener<PlayerTickEvent> Я4;
   private boolean ЯУ;
   private final FloatAnimation сВ;
   private static final String ЯГц = "Crosshair";
   private static final String ЯГя = "Кастомный прицел";
   private static final String Ялс = "Пробел";
   private static final float ЯлВ = 5.0F;
   private static final float ЯлЯ = 0.1F;
   private static final String ЯлХ = "Длина";
   private static final float Яле = 4.0F;
   private static final float ЯлА = 5.0F;
   private static final float Ял4 = 0.1F;
   private static final String Ялп = "Толщина";
   private static final float ЯлД = 5.0F;
   private static final float ЯлЪ = 0.1F;
   private static final String Ялт = "Динамический";
   private static final String Яла = "Сила расширения";
   private static final float ЯлЮ = 10.0F;
   private static final float ЯлЧ = 5.0F;
   private static final float ЯлЛ = 15.0F;
   private static final String Ял诶 = "Менять цвет при наведении";
   private static final String Ял1 = "Цвет";
   private static final String ЯлЬ = "Цвет при наведении";
   private static final float Ял衣 = 10.0F;
   private static final float Ялш = 0.5F;
   private static final float ЯлИ = 0.5F;
   private static final float Яль = 0.5F;
   private static final float Ялж = 0.5F;

   private void Ы(HudRenderEvent.CrosshairRender var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!MINECRAFT_CLIENT.gui.hud.isHidden()) {
            if (MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
               this.сВ.updateTarget(this.ЯУ ? 1.0F : 0.0F);
               int var2 = Math.max(2, MINECRAFT_CLIENT.getWindow().getGuiScale());
               float var3 = this.ЯГ.getValue() * var2;
               float var4 = this.Яэ.getValue() * var2;
               float var5 = this.Ял.getValue() * var2;
               float var6 = MINECRAFT_CLIENT.getWindow().getScreenWidth() / 2.0F - Ялш;
               float var7 = MINECRAFT_CLIENT.getWindow().getScreenHeight() / 2.0F - ЯлИ;
               int var8 = ColorMath.blendArgb(this.щ.getValue(), this.Г.getValue(), this.сВ.getValue());
               int var9 = ColorMath.packRgb(0, 0, 0);
               if (this.Вт.getValue()) {
                  var3 = Ж(var3, MINECRAFT_CLIENT.player.getAttackStrengthScale(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true)), this.Яю.getValue());
               }

               float var10 = var4 / 2.0F;
               this.Ы(var6 - var3 - var5, var7 - var10, var5, var4, var8, var9);
               this.Ы(var6 + var3, var7 - var10, var5, var4, var8, var9);
               this.Ы(var6 - var10, var7 - var3 - var5, var4, var5, var8, var9);
               this.Ы(var6 - var10, var7 + var3, var4, var5, var8, var9);
               var1.setCancelled(true);
            }
         }
      }
   }

   private void Ы(float var1, float var2, float var3, float var4, int var5, int var6) {
      this.Х(var1 - Яль, var2 - Ялж, var3 + 1.0F, var4 + 1.0F, var6);
      this.Х(var1, var2, var3, var4, var5);
   }

   public Crosshair() {
      super(ЯГц, ЯГя, ModuleCategory.RENDER);
      this.ЯГ = new FloatSetting(Ялс, 1.0F, 0.0F, ЯлВ).withStep(ЯлЯ);
      this.Ял = new FloatSetting(ЯлХ, Яле, 1.0F, ЯлА).withStep(Ял4);
      this.Яэ = new FloatSetting(Ялп, 1.0F, 1.0F, ЯлД).withStep(ЯлЪ);
      this.Вт = new BooleanSetting(Ялт, true);
      this.Яю = new FloatSetting(Яла, ЯлЮ, ЯлЧ, ЯлЛ).withStep(1.0F).visibleWhen(() -> this.Вт.getValue());
      this.Ва = new BooleanSetting(Ял诶, false);
      this.щ = new ColorSetting(Ял1, true, -1);
      this.Г = new ColorSetting(ЯлЬ, true, ColorMath.packRgb(255, 0, 0)).visibleWhen(() -> this.Ва.getValue());
      this.ЯА = this::Ы;
      this.Я4 = this::сЯ;
      this.ЯУ = false;
      this.сВ = new FloatAnimation(0.0F, Ял衣);
      this.addSettings(this.Ял, this.ЯГ, this.Яэ, this.Вт, this.Яю, this.Ва, this.щ, this.Г);
   }

   public void Х(float var1, float var2, float var3, float var4, int var5) {
      UiDrawing.drawRoundedRect(var1, var2, var3, var4, 0.0F, var5);
   }

   private void сЯ(PlayerTickEvent var1) {
      this.ЯУ = false;
      if (this.Ва.getValue() && MINECRAFT_CLIENT.crosshairPickEntity != null && MINECRAFT_CLIENT.crosshairPickEntity.asLivingEntity() != null) {
         this.ЯУ = true;
      }
   }

   private static float Ж(float var0, float var1, float var2) {
      return var0 + (1.0F - var1) * var2;
   }
}
