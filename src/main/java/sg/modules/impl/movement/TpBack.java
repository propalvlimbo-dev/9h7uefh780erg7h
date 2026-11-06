package sg.modules.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.events.impl.ClientTickEvent;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.MovementInputEvent;

public class TpBack extends ClientModule {
   public int тД;
   public boolean Ад;
   private boolean АБ;
   private final Stopwatch и = new Stopwatch();
   private final EventListener<PacketReceiveEvent> еЭ = this::л;
   private final EventListener<PlayerTickEvent> е必 = this::衣л;
   private final EventListener<MovementInputEvent> ещ = this::Л;
   private final EventListener<ClientTickEvent> еГ = this::ф;
   private final EventListener<PacketSendEvent> ел = this::method_4;
   private static final String Яга = "TpBack";
   private static final String ЯгЮ = "Возвращает назад после падения";
   private static final double ЯгЧ = 3.0;
   private static final float ЯгЛ = 0.5F;
   private static final double Яг诶 = 300.0;

   @Override
   public void onDisable() {
      this.ЧФ();
      ClientTimerSpeed.Оэ(1.0F);
      this.Ч诶();
      super.onDisable();
   }

   private void Л(MovementInputEvent var1) {
      if (this.АБ) {
         var1.setJumpPressed(false);
      }

      if (this.тД == 2) {
         var1.setJumpPressed(false);
      }

      if (this.тД == 1) {
         var1.setJumpPressed(true);
      }
   }

   private void л(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundLoginPacket || var1.getPacket() instanceof ClientboundDisconnectPacket || var1.getPacket() instanceof ClientboundRespawnPacket) {
         this.ЧФ();
         ClientTimerSpeed.Оэ(1.0F);
         this.Ч诶();
      }

      if (var1.getPacket() instanceof ClientboundPlayerPositionPacket && this.АБ) {
         this.тД = 2;
         this.АБ = false;
      }
   }

   public TpBack() {
      super(Яга, ЯгЮ, ModuleCategory.MOVEMENT);
   }

   private void Ч弟() {
      if (MINECRAFT_CLIENT.options != null && MINECRAFT_CLIENT.gui.screen() == null) {
         MINECRAFT_CLIENT.options.keyUp.setDown(true);
         MINECRAFT_CLIENT.options.keyJump.setDown(true);
      }
   }

   @Override
   public void onEnable() {
      this.ЧФ();
      this.Ч弟();
   }

   private void 衣л(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         this.Ч弟();
         this.Ад = MINECRAFT_CLIENT.player.fallDistance > ЯгЧ;
         if (this.тД == 2) {
            ClientTimerSpeed.Оэ(ЯгЛ);
         } else if (this.тД <= 1) {
            ClientTimerSpeed.Оэ(1.0F);
         }

         if (this.тД > 0) {
            this.тД--;
         }

         if (this.и.Т(Яг诶)) {
            this.АБ = false;
            this.тД = 0;
         }
      }
   }

   private void method_4(PacketSendEvent var1) {
      if (var1.getPacket() instanceof ServerboundMovePlayerPacket var2 && this.Ад && var2.isOnGround() && this.тД == 0) {
         PacketEventBypass.А(new StatusOnly(true, false));
         this.и.ВД();
         this.АБ = true;
      }
   }

   public void ЧФ() {
      this.тД = 0;
      this.Ад = false;
      this.АБ = false;
   }

   private void Ч诶() {
      if (MINECRAFT_CLIENT.options != null && MINECRAFT_CLIENT.getWindow() != null) {
         MINECRAFT_CLIENT.options.keyUp.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), MINECRAFT_CLIENT.options.keyUp.key.getValue()));
         MINECRAFT_CLIENT.options.keyJump.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), MINECRAFT_CLIENT.options.keyJump.key.getValue()));
      }
   }

   private void ф(ClientTickEvent var1) {
      this.Ч弟();
   }
}
