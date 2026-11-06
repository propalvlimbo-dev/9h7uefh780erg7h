package sg.modules.impl.movement;

import net.minecraft.util.Mth;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;

public class TimerModule extends ClientModule {
   private static final TimerModule.SyncListener В = new TimerModule.SyncListener();
   private final FloatSetting Хв;
   private final BooleanSetting Вх;
   private final FloatSetting Хъ;
   public static float Ъо = TimerModule.Вкх;
   public static float ЪO = 0.0F;
   private float Ъ7;
   private float ЪР;
   private float Ъ3;
   private float Ър;
   private float Ъ6;
   private final EventListener<PlayerTickEvent.Post> ХД;
   private static final String Вкы = "Timer";
   private static final String Вкй = "Ускоряет вас на некоторое время";
   private static final String ВкЖ = "Скорость";
   private static final float Вкм = 1.8F;
   private static final float ВкК = 5.0F;
   private static final float ВкI = 0.1F;
   private static final String ВкТ = "Умный";
   private static final String ВкО = "Скорость убывания";
   private static final float ВкФ = 0.5F;
   private static final float ВкЙ = 3.0F;
   private static final float Вкк = 0.1F;
   private static final float Вкг = 10.0F;
   private static final float Вк0 = 10.0F;
   private static final float Вкх = 100.0F;

   public TimerModule() {
      super(Вкы, Вкй, ModuleCategory.MOVEMENT);
      this.Хв = new FloatSetting(ВкЖ, Вкм, 1.0F, ВкК).withStep(ВкI);
      this.Вх = new BooleanSetting(ВкТ, true);
      this.Хъ = new FloatSetting(ВкО, 1.0F, ВкФ, ВкЙ).withStep(Вкк).visibleWhen(this.Вх::getValue);
      this.ХД = this::Г;
      this.addSettings(this.Хв, this.Вх, this.Хъ);
      RoseClient.getInstance().getEventBus().register(В);
   }

   @Override
   public void onDisable() {
      ClientTimerSpeed.Оэ(1.0F);
      super.onDisable();
   }

   public void ш(float var1, float var2, float var3, float var4, float var5) {
      if (this.ы(var3, var4, var5, var1, var2)) {
         ЪO += 2.0F;
      } else if (this.isEnabled()) {
         ЪO = ЪO - this.Хв.getValue() * 2.0F * this.Хъ.getValue();
      }

      ЪO = Mth.clamp(ЪO, 0.0F, Ъо);
      this.Ъ7 = var3;
      this.ЪР = var4;
      this.Ъ3 = var5;
      this.Ър = var1;
      this.Ъ6 = var2;
   }

   private boolean ы(float var1, float var2, float var3, float var4, float var5) {
      return var1 == this.Ъ7 && var2 == this.ЪР && var3 == this.Ъ3 && var4 == this.Ър && var5 == this.Ъ6;
   }

   public void Г(PlayerTickEvent.Post var1) {
      if (this.Вх.getValue()) {
         if (ЪO > Вкг) {
            ClientTimerSpeed.Оэ(this.Хв.getValue());
         } else {
            ClientTimerSpeed.Оэ(1.0F);
         }

         if (ЪO < Вк0) {
            this.toggle();
         }
      } else {
         ClientTimerSpeed.Оэ(this.Хв.getValue());
      }
   }

   public static class SyncListener implements MinecraftAccess {
      private final EventListener<PlayerMovementSyncEvent> ВН = this::Э;

      private void Э(PlayerMovementSyncEvent var1) {
         if (MINECRAFT_CLIENT.player != null) {
            TimerModule var2 = RoseClient.getInstance().getServices().getModules().getTimerModule();
            if (var2 != null) {
               var2.ш(var1.getYaw(), var1.getPitch(), (float)var1.getPosition().x, (float)var1.getPosition().y, (float)var1.getPosition().z);
            }
         }
      }
   }
}
