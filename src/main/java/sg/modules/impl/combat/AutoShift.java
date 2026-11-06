package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.LivingEntity;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.settings.FloatSetting;

public class AutoShift extends ClientModule {
   @SerializedName("В")
   private final FloatSetting delaySetting;
   @SerializedName("В")
   private final Stopwatch shiftTimer;
   @SerializedName("Х")
   private boolean shiftHeldByModule;
   @SerializedName("В")
   private final EventListener<AttackEntityEvent> attackListener;
   @SerializedName("Я")
   private final EventListener<PlayerTickEvent> playerTickListener;
   private static final String MODULE_NAME = "AutoShift";
   private static final String MODULE_DESCRIPTION = "Автоматически приседает при ударе";
   private static final String DELAY_LABEL = "Задержка";
   private static final float DEFAULT_DELAY_MILLIS = 100.0F;
   private static final float MAX_DELAY_MILLIS = 500.0F;
   private static final float DELAY_STEP_MILLIS = 10.0F;

   private void Ъ_de5a6f78() {
      if (this.shiftHeldByModule) {
         if (MINECRAFT_CLIENT.options != null) {
            MINECRAFT_CLIENT.options.keyShift.setDown(false);
         }

         this.shiftHeldByModule = false;
      }
   }

   private void э(PlayerTickEvent var1) {
      if (this.shiftHeldByModule) {
         if (this.shiftTimer.М(this.delaySetting.getValue().longValue())) {
            this.Ъ_de5a6f78();
         } else {
            MINECRAFT_CLIENT.options.keyShift.setDown(true);
         }
      }
   }

   public AutoShift() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.delaySetting = new FloatSetting(DELAY_LABEL, DEFAULT_DELAY_MILLIS, 0.0F, MAX_DELAY_MILLIS).withStep(DELAY_STEP_MILLIS);
      this.shiftTimer = new Stopwatch();
      this.attackListener = this::э;
      this.playerTickListener = this::э;
      this.addSettings(this.delaySetting);
   }

   private void э(AttackEntityEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
            if (var1.getTarget() instanceof LivingEntity var2 && var2.isAlive()) {
               MINECRAFT_CLIENT.options.keyShift.setDown(true);
               this.shiftHeldByModule = true;
               this.shiftTimer.ВД();
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.Ъ_de5a6f78();
   }
}
