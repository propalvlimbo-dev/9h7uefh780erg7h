package sg.modules.impl.movement;

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.GlidingVelocityEvent;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public class GrimGlide extends ClientModule {
   private final FloatSetting е1;
   private final FloatSetting еЬ;
   private int ЪI;
   private boolean Ар;
   private int ЪТ;
   private boolean А6;
   private final EventListener<PlayerTickEvent> еа;
   private final EventListener<PacketReceiveEvent> еЮ;
   private final EventListener<GlidingVelocityEvent> еЧ;
   private final EventListener<PacketSendEvent> еЛ;
   private static final String Хвя = "GrimGlide";
   private static final String Хъс = "Ускоряет полёт на элитрах без фейерверков";
   private static final String ХъВ = "Timer Boost";
   private static final float ХъЯ = 1.5F;
   private static final float ХъХ = 5.0F;
   private static final float Хъе = 0.1F;
   private static final String ХъА = "Сброс таймера";
   private static final float Хъ4 = 0.5F;
   private static final float Хъп = 0.9F;
   private static final float ХъД = 0.01F;
   private static final float ХъЪ = (float) (Math.PI / 180.0);
   private static final double Хът = 0.01;
   private static final double Хъа = -1.0;
   private static final double ХъЮ = 0.75;
   private static final double ХъЧ = -0.1;
   private static final double ХъЛ = 0.04F;
   private static final double Хъ诶 = 3.2;
   private static final double Хъ1 = 0.1;
   private static final double ХъЬ = 0.1;
   private static final double Хъ衣 = 0.09F;
   private static final double Хъш = 0.99F;
   private static final double ХъИ = 0.98F;
   private static final double Хъь = 0.99F;
   private static final double Хъж = 0.03F;
   private static final double Хъ弟 = 0.3F;
   private static final double Хъб = 0.3F;
   private static final double ХъЫ = 0.3F;

   @Override
   public void onEnable() {
      this.ЪI = 0;
      this.Ар = false;
      this.ЪТ = 0;
      this.А6 = false;
   }

   public GrimGlide() {
      super(Хвя, Хъс, ModuleCategory.MOVEMENT);
      this.е1 = new FloatSetting(ХъВ, ХъЯ, 1.0F, ХъХ).withStep(Хъе);
      this.еЬ = new FloatSetting(ХъА, Хъ4, 0.0F, Хъп).withStep(ХъД);
      this.еа = this::ЧД;
      this.еЮ = this::а;
      this.еЧ = this::к;
      this.еЛ = this::У;
      this.addSettings(this.е1, this.еЬ);
   }

   private void р1() {
      boolean var1 = MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.isFallFlying();
      if (!var1) {
         if (this.А6) {
            ClientTimerSpeed.Оэ(1.0F);
            this.А6 = false;
         }
      } else {
         ClientTimerSpeed.Оэ(this.ЪТ % 2 == 0 ? this.е1.getValue() : this.еЬ.getValue());
         this.А6 = true;
         this.ЪТ++;
      }
   }

   public void а(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundPlayerPositionPacket) {
         this.ЪI = 2;
         this.Ар = true;
         if (this.ЪТ % 2 == 1) {
            this.ЪТ++;
         }
      }
   }

   public void У(PacketSendEvent var1) {
      if (var1.getPacket() instanceof ServerboundMovePlayerPacket) {
         if (MINECRAFT_CLIENT.player.isFallFlying() && this.ЪI == 0 && !this.Ар) {
            PacketEventBypass.А(new StatusOnly(true, true));
            var1.setCancelled(true);
         }

         this.Ар = false;
      }
   }

   @Override
   public void onDisable() {
      if (this.А6) {
         ClientTimerSpeed.Оэ(1.0F);
         this.А6 = false;
      }
   }

   public void к(GlidingVelocityEvent var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getDeltaMovement();
      Vec3 var3 = MINECRAFT_CLIENT.player.getLookAngle();
      float var4 = MINECRAFT_CLIENT.player.getXRot() * ХъЪ;
      double var5 = Math.sqrt(var3.x * var3.x + var3.z * var3.z);
      double var7 = var2.horizontalDistance();
      boolean var9 = MINECRAFT_CLIENT.player.getDeltaMovement().y <= 0.0;
      double var10 = var9 && MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING) ? Math.min(MINECRAFT_CLIENT.player.getGravity(), Хът) : MINECRAFT_CLIENT.player.getGravity();
      double var12 = Mth.square(Math.cos(var4));
      var2 = var2.add(0.0, var10 * (Хъа + var12 * ХъЮ), 0.0);
      if (var2.y < 0.0 && var5 > 0.0) {
         double var14 = var2.y * ХъЧ * var12;
         var2 = var2.add(var3.x * var14 / var5, var14, var3.z * var14 / var5);
      }

      if (var4 < 0.0F && var5 > 0.0) {
         double var25 = var7 * -Mth.sin(var4) * ХъЛ;
         var2 = var2.add(-var3.x * var25 / var5, var25 * Хъ诶, -var3.z * var25 / var5);
      }

      if (var5 > 0.0) {
         var2 = var2.add((var3.x / var5 * var7 - var2.x) * Хъ1, 0.0, (var3.z / var5 * var7 - var2.z) * ХъЬ);
      }

      double var16 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
      double var18 = -Math.sin(var16);
      double var20 = Math.cos(var16);
      if (this.ЪI >= 1) {
         double var22 = Хъ衣;
         var1.setVelocity(var2.multiply(Хъш, ХъИ, Хъь).add(var18 * var22, Хъж, var20 * var22));
      } else {
         var1.setVelocity(var2.multiply(Хъ弟, Хъб, ХъЫ));
      }
   }

   public void ЧД(PlayerTickEvent var1) {
      if (this.ЪI > 0) {
         this.ЪI--;
      }

      this.р1();
   }
}
