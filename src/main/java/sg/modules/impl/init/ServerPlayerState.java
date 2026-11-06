package sg.modules.impl.init;

import lombok.Generated;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import sg.ec.MinecraftAccess;
import sg.ec.ClientService;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.events.impl.ClientTickEvent;

public class ServerPlayerState extends ClientService implements MinecraftAccess {
   private int ЯЫ;
   private float ЯБ;
   private float ЯС;
   private float Яз;
   private double сб;
   private double сЫ;
   private double сl;
   private boolean с0;
   private boolean сх;
   private boolean сУ;
   private boolean сЗ;
   private double с8;
   private boolean сЕ;
   private final EventListener<ClientTickEvent> сь = this::м;
   private final EventListener<PacketSendEvent> сж = this::Щ;

   @Generated
   public boolean method_96() {
      return this.сх;
   }

   @Generated
   public boolean method_9ц() {
      return this.сЗ;
   }

   @Generated
   public boolean method_9Ы() {
      return this.сЕ;
   }

   @Generated
   public double 必_3f39d5c3() {
      return this.сl;
   }

   @Generated
   public EventListener<ClientTickEvent> method_1() {
      return this.сь;
   }

   @Generated
   public boolean method_9н() {
      return this.с0;
   }

   @Generated
   public boolean method_9ь() {
      return this.сУ;
   }

   @Generated
   public double р() {
      return this.сб;
   }

   @Generated
   public int ПO() {
      return this.ЯЫ;
   }

   @Generated
   public double Е() {
      return this.с8;
   }

   @Generated
   public float По() {
      return this.ЯС;
   }

   @Generated
   public double O() {
      return this.сЫ;
   }

   public void м(ClientTickEvent var1) {
      if (MINECRAFT_CLIENT.player == null) {
         this.Яз = 0.0F;
         this.сЕ = false;
      } else {
         double var2 = MINECRAFT_CLIENT.player.getY();
         if (!this.сЕ) {
            this.с8 = var2;
            this.сЕ = true;
         } else {
            double var4 = this.с8 - var2;
            if (MINECRAFT_CLIENT.player.onGround()) {
               this.Яз = 0.0F;
            } else if (var4 > 0.0) {
               this.Яз += (float)var4;
            }

            this.с8 = var2;
         }
      }
   }

   @Generated
   public float ПА() {
      return this.ЯБ;
   }

   @Generated
   public float Пг() {
      return this.Яз;
   }

   public void Щ(PacketSendEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (var1.getPacket() instanceof ServerboundMovePlayerPacket var2) {
            if (var2.hasPosition()) {
               this.сб = var2.getX(MINECRAFT_CLIENT.player.getX());
               this.сЫ = var2.getY(MINECRAFT_CLIENT.player.getY());
               this.сl = var2.getZ(MINECRAFT_CLIENT.player.getZ());
            }

            if (var2.hasRotation()) {
               this.ЯБ = var2.getYRot(MINECRAFT_CLIENT.player.getYRot());
               this.ЯС = var2.getXRot(MINECRAFT_CLIENT.player.getXRot());
            }

            this.с0 = var2.isOnGround();
            this.сЗ = var2.horizontalCollision();
         }

         if (var1.getPacket() instanceof ServerboundSetCarriedItemPacket var4) {
            this.ЯЫ = var4.getSlot();
         }

         if (var1.getPacket() instanceof ServerboundPlayerCommandPacket var5) {
            switch (var5.getAction()) {
               case START_SPRINTING:
                  this.сх = true;
                  break;
               case STOP_SPRINTING:
                  this.сх = false;
            }
         }

         if (var1.getPacket() instanceof ServerboundPlayerInputPacket var6) {
            this.сУ = var6.input().shift();
         }
      }
   }

   @Generated
   public EventListener<PacketSendEvent> 诶_f8db0d09() {
      return this.сж;
   }
}
