package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.events.impl.ClientTickEvent;
import sg.util.entity.PlayerMovementSimulator;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.MovementInputEvent;
import sg.settings.SingleChoiceSetting;

public class NoFall extends ClientModule {
   private final SingleChoiceSetting Б;
   @SerializedName("4т")
   public int field_4т;
   public boolean ХХ;
   private boolean Хе;
   @SerializedName("4а")
   private int field_4а;
   private final Stopwatch ш;
   private final Stopwatch И;
   private final EventListener<PacketReceiveEvent> Яа;
   private final EventListener<PlayerTickEvent> ЯЮ;
   private final EventListener<MovementInputEvent> ЯЧ;
   private final EventListener<ClientTickEvent> ЯЛ;
   private final EventListener<PacketSendEvent> Я诶;
   private static final String с诶И = "NoFall";
   private static final String с诶ь = "Убирает урон от падения";
   private static final String с诶ж = "Обход";
   private static final String с诶弟 = "Grim 72";
   private static final String с诶б = "Grim 73";
   private static final String с诶Ы = "Grim 72";
   private static final String с诶l = "Grim 73";
   private static final String с诶8 = "Grim 73";
   private static final String с诶5 = "Grim 72";
   private static final double с诶в = 3.0;
   private static final double с诶ъ = 200.0;
   private static final double с诶М = 300.0;
   private static final String с诶Н = "Grim 73";
   private static final double с诶н = 3.0;
   private static final float с诶Ц = 0.5F;
   private static final double с诶ч = 300.0;
   private static final String с诶Э = "Grim 72";

   public void хг() {
      this.field_4т = 0;
      this.ХХ = false;
      this.Хе = false;
   }

   private void х(PacketSendEvent var1) {
      if (this.Б.isSelected(с诶8) && var1.getPacket() instanceof ServerboundMovePlayerPacket var2 && this.ХХ && var2.isOnGround() && this.field_4т == 0) {
         PacketEventBypass.А(new StatusOnly(true, false));
         this.ш.ВД();
         this.Хе = true;
      }
   }

   private void оТ(PlayerTickEvent var1) {
      if (this.Б.isSelected(с诶5)) {
         this.ХХ = MINECRAFT_CLIENT.player.fallDistance > с诶в;
         if (this.field_4а > 0) {
            this.field_4а--;
         }

         if (this.ХХ && !MINECRAFT_CLIENT.player.onGround() && PlayerMovementSimulator.predictTicks(1).onGround && this.И.Т(с诶ъ)) {
            boolean var2 = false;
            PacketEventBypass.А(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
            PacketEventBypass.А(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
            this.И.ВД();
            this.Хе = true;
            this.ш.ВД();
         }

         if (this.ш.Т(с诶М)) {
            this.Хе = false;
            this.field_4а = 0;
         }
      }

      if (this.Б.isSelected(с诶Н)) {
         this.ХХ = MINECRAFT_CLIENT.player.fallDistance > с诶н;
         if (this.field_4т == 2) {
            ClientTimerSpeed.Оэ(с诶Ц);
         }

         if (this.field_4т <= 1) {
            ClientTimerSpeed.Оэ(1.0F);
         }

         if (this.field_4т > 0) {
            this.field_4т--;
         }

         if (this.ш.Т(с诶ч)) {
            this.Хе = false;
            this.field_4т = 0;
         }
      }
   }

   private void с(MovementInputEvent var1) {
      if (this.Б.isSelected(с诶Э)) {
         if (this.Хе) {
            var1.setJumpPressed(false);
         }

         if (this.field_4а == 2) {
            var1.setJumpPressed(false);
         }

         if (this.field_4а == 1) {
            var1.setJumpPressed(true);
         }
      } else {
         if (this.Хе) {
            var1.setJumpPressed(false);
         }

         if (this.field_4т == 2) {
            var1.setJumpPressed(false);
         }

         if (this.field_4т == 1) {
            var1.setJumpPressed(true);
         }
      }
   }

   private void Ш(ClientTickEvent var1) {
   }

   public NoFall() {
      super(с诶И, с诶ь, ModuleCategory.MOVEMENT);
      this.Б = new SingleChoiceSetting(с诶ж, new ChoiceOption(с诶弟), new ChoiceOption(с诶б));
      this.ш = new Stopwatch();
      this.И = new Stopwatch();
      this.Яа = this::ы;
      this.ЯЮ = this::оТ;
      this.ЯЧ = this::с;
      this.ЯЛ = this::Ш;
      this.Я诶 = this::х;
      this.addSettings(this.Б);
   }

   private void ы(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundLoginPacket || var1.getPacket() instanceof ClientboundDisconnectPacket || var1.getPacket() instanceof ClientboundRespawnPacket) {
         this.хг();
      }

      if (var1.getPacket() instanceof ClientboundPlayerPositionPacket && this.Хе) {
         if (this.Б.isSelected(с诶Ы)) {
            this.field_4а = 2;
         } else if (this.Б.isSelected(с诶l)) {
            this.field_4т = 2;
         }

         this.Хе = false;
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.Хе = false;
      this.ХХ = false;
      this.field_4а = 0;
      this.field_4т = 0;
   }
}
