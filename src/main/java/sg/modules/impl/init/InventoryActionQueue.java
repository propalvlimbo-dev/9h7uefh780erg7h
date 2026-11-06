package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.inventory.ContainerInput;
import sg.ec.MinecraftAccess;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.events.impl.MovementInputEvent;

public class InventoryActionQueue implements MinecraftAccess {
   private final CopyOnWriteArrayList<InventoryActionQueue.ActionSequence> Я = new CopyOnWriteArrayList<>();
   private static final CopyOnWriteArrayList<InventoryActionQueue.SlotSwap> Х = new CopyOnWriteArrayList<>();
   private final EventListener<PlayerTickEvent> С = this::В;
   private final EventListener<MovementInputEvent> з = this::Б;

   public void Б(String var1, int var2, Runnable... var3) {
      for (InventoryActionQueue.ActionSequence var5 : this.Я) {
         if (var5.с7().equals(var1)) {
            return;
         }
      }

      InventoryActionQueue.ActionSequence var6 = new InventoryActionQueue.ActionSequence(var1, var3, null, InventoryActionQueue.Mode.LEGIT);
      var6.Ли(var2);
      this.Я.add(var6);
   }

   public void В(PlayerTickEvent var1) {
      if (!this.зм()) {
         this.Я.clear();
      } else {
         for (InventoryActionQueue.ActionSequence var3 : this.Я) {
            if (var3.method_0_666f4f2e().equals(InventoryActionQueue.Mode.LEGIT)) {
               this.Т(var3);
            } else if (var3.method_0_666f4f2e().equals(InventoryActionQueue.Mode.MATRIX)) {
               this.Б(var3);
            } else if (var3.method_0_666f4f2e().equals(InventoryActionQueue.Mode.RAGE)) {
               this.з(var3);
            }
         }
      }
   }

   public int ф() {
      return MINECRAFT_CLIENT.player.getInventory().selected + 1 > 8 ? MINECRAFT_CLIENT.player.getInventory().selected - 1 : MINECRAFT_CLIENT.player.getInventory().selected + 1;
   }

   public void з(String var1, int var2, Runnable... var3) {
      for (InventoryActionQueue.ActionSequence var5 : this.Я) {
         if (var5.с7().equals(var1)) {
            return;
         }
      }

      InventoryActionQueue.ActionSequence var6 = new InventoryActionQueue.ActionSequence(var1, var3, null, InventoryActionQueue.Mode.RAGE);
      var6.Ли(var2);
      this.Я.add(var6);
   }

   private boolean зм() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player.isAlive();
   }

   @Generated
   public static CopyOnWriteArrayList<InventoryActionQueue.SlotSwap> Т_b1d77d5c() {
      return Х;
   }

   public void Б(MovementInputEvent var1) {
      boolean var2 = true;
      boolean var3 = true;

      for (InventoryActionQueue.ActionSequence var5 : this.Я) {
         if (var5.method_0_666f4f2e().equals(InventoryActionQueue.Mode.LEGIT)) {
            var2 = false;
            var3 = false;
            break;
         }

         if (var5.method_0_666f4f2e().equals(InventoryActionQueue.Mode.MATRIX)) {
            var3 = false;
         }
      }

      if (!var2) {
         var1.setForwardPressed(false);
         var1.setBackwardPressed(false);
         var1.setLeftPressed(false);
         var1.setRightPressed(false);
         var1.setJumpPressed(false);
         var1.setSneakPressed(false);
         var1.setForwardAmount(KeyboardInput.calculateImpulse(var1.isForwardPressed(), var1.isBackwardPressed()));
         var1.setLeftAmount(KeyboardInput.calculateImpulse(var1.isLeftPressed(), var1.isRightPressed()));
      }

      if (!var3) {
         var1.setSprintPressed(false);
      }
   }

   @Generated
   public EventListener<PlayerTickEvent> method_3() {
      return this.С;
   }

   public InventoryActionQueue() {
      RoseClient.getInstance().getEventBus().register(this);
   }

   private void Т(InventoryActionQueue.ActionSequence var1) {
      if (!MovementUtils.hasMovementInput()) {
         if (var1.method_0_f0152c54().length > 1) {
            if (var1.method_0_70e41a4a() == InventoryActionQueue.Stage.START && var1.О().hasElapsed(var1.method_04())) {
               var1.method_0_f0152c54()[var1.method_0ы()].run();
               var1.О().reset();
               var1.method_3_1004dbd2().reset();
               var1.Лф(var1.method_0ы() + 1);
               if (var1.method_0ы() >= var1.method_0_f0152c54().length) {
                  var1.method_0(InventoryActionQueue.Stage.END);
               }
            }
         } else if (var1.method_0_70e41a4a() == InventoryActionQueue.Stage.START) {
            var1.method_0_f0152c54()[var1.method_0ы()].run();
            var1.method_0(InventoryActionQueue.Stage.END);
            var1.method_3_1004dbd2().reset();
         }

         if (var1.method_0_70e41a4a() == InventoryActionQueue.Stage.END
            && var1.method_3_1004dbd2().hasElapsed(RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue())) {
            if (var1.method_3_3382daff() != null) {
               var1.method_3_3382daff().run();
            }

            this.Я.remove(var1);
         }
      }
   }

   @Generated
   public CopyOnWriteArrayList<InventoryActionQueue.ActionSequence> з_b1d77d5c() {
      return this.Я;
   }

   private void з(InventoryActionQueue.ActionSequence var1) {
      if (var1.method_0_f0152c54().length > 1) {
         if (var1.О().hasElapsed(var1.method_04())) {
            var1.method_0_f0152c54()[var1.method_0ы()].run();
            var1.Лф(var1.method_0ы() + 1);
            var1.О().reset();
         }
      } else {
         var1.method_0_f0152c54()[var1.method_0ы()].run();
         this.Я.remove(var1);
      }

      if (var1.method_0ы() >= var1.method_0_f0152c54().length) {
         this.Я.remove(var1);
      }
   }

   @Generated
   public EventListener<MovementInputEvent> Г_f8db0d09() {
      return this.з;
   }

   private void Б(InventoryActionQueue.ActionSequence var1) {
      if (!MovementUtils.isServerSprinting()) {
         if (var1.method_0_f0152c54().length > 1) {
            if (var1.О().hasElapsed(var1.method_04())) {
               var1.method_0_f0152c54()[var1.method_0ы()].run();
               var1.Лф(var1.method_0ы() + 1);
               var1.О().reset();
            }
         } else {
            var1.method_0_f0152c54()[var1.method_0ы()].run();
            this.Я.remove(var1);
         }

         if (var1.method_0ы() >= var1.method_0_f0152c54().length) {
            this.Я.remove(var1);
         }
      }
   }

   public void Т(String var1, Runnable var2) {
      this.з(var1, var2, null);
   }

   public void з(String var1, int var2, Runnable var3, Runnable... var4) {
      for (InventoryActionQueue.ActionSequence var6 : this.Я) {
         if (var6.с7().equals(var1)) {
            return;
         }
      }

      InventoryActionQueue.ActionSequence var7 = new InventoryActionQueue.ActionSequence(var1, var4, var3, InventoryActionQueue.Mode.LEGIT);
      var7.Ли(var2);
      this.Я.add(var7);
   }

   public void зИ() {
      PacketEventBypass.А(new ServerboundPlayerActionPacket(Action.SWAP_ITEM_WITH_OFFHAND, new BlockPos(0, 0, 0), Direction.DOWN));
   }

   public void зl() {
      if (MINECRAFT_CLIENT.gui.screen() == null) {
         MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(0));
      }
   }

   public void Т(String var1, int var2, Runnable... var3) {
      for (InventoryActionQueue.ActionSequence var5 : this.Я) {
         if (var5.с7().equals(var1)) {
            return;
         }
      }

      InventoryActionQueue.ActionSequence var6 = new InventoryActionQueue.ActionSequence(var1, var3, null, InventoryActionQueue.Mode.MATRIX);
      var6.Ли(var2);
      this.Я.add(var6);
   }

   public void з(String var1, Runnable var2, Runnable var3) {
      for (InventoryActionQueue.ActionSequence var5 : this.Я) {
         if (var5.с7().equals(var1)) {
            return;
         }
      }

      this.Я.add(new InventoryActionQueue.ActionSequence(var1, var2, var3, InventoryActionQueue.Mode.LEGIT));
   }

   public void з(String var1, Runnable var2) {
      for (InventoryActionQueue.ActionSequence var4 : this.Я) {
         if (var4.с7().equals(var1)) {
            return;
         }
      }

      this.Я.add(new InventoryActionQueue.ActionSequence(var1, var2, null, InventoryActionQueue.Mode.MATRIX));
   }

   public void Э_de5a6f78(int var1, int var2) {
      boolean var3 = false;

      for (InventoryActionQueue.SlotSwap var5 : Т_b1d77d5c()) {
         if (var5.ю() == var1 && var5.ж() == var2) {
            var3 = true;
            break;
         }
      }

      if (!var3) {
         int var6 = var1;
         if (var6 < 9) {
            var6 += 36;
         } else if (var6 == 40) {
            var6 = 45;
         }

         InventoryActionQueue.SlotSwap var7 = new InventoryActionQueue.SlotSwap(var6, var2);
         var7.method__().reset();
         Т_b1d77d5c().add(var7);
      }

      MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var2, var1, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
   }

   public static class SlotSwap {
      private final int сЛ;
      private final int с诶;
      private final TickTimer Я = new TickTimer();

      @Generated
      public TickTimer method__() {
         return this.Я;
      }

      @Generated
      public int ю() {
         return this.сЛ;
      }

      @Generated
      public int ж() {
         return this.с诶;
      }

      @Generated
      public SlotSwap(int var1, int var2) {
         this.сЛ = var1;
         this.с诶 = var2;
      }
   }

   public enum Stage {
      START,
      END;
   }

   public static class ActionSequence {
      private final String Хж;
      @SerializedName("В")
      private final Runnable[] В_f0152c54;
      private final Runnable 诶;
      @SerializedName("В")
      private final InventoryActionQueue.Mode В_666f4f2e;
      @SerializedName("В")
      private InventoryActionQueue.Stage В_70e41a4a = InventoryActionQueue.Stage.START;
      private int ЪЗ;
      private int ЪЕ = 0;
      private final TickTimer т = new TickTimer();
      private final TickTimer а = new TickTimer();

      @Generated
      public void Лф(int var1) {
         this.ЪЕ = var1;
      }

      @Generated
      public InventoryActionQueue.Stage method_0_70e41a4a() {
         return this.В_70e41a4a;
      }

      @Generated
      public void method_0(InventoryActionQueue.Stage var1) {
         this.В_70e41a4a = var1;
      }

      @Generated
      public TickTimer method_3_1004dbd2() {
         return this.а;
      }

      @Generated
      public Runnable[] method_0_f0152c54() {
         return this.В_f0152c54;
      }

      @Generated
      public InventoryActionQueue.Mode method_0_666f4f2e() {
         return this.В_666f4f2e;
      }

      @Generated
      public int method_04() {
         return this.ЪЗ;
      }

      @Generated
      public TickTimer О() {
         return this.т;
      }

      @Generated
      public void Ли(int var1) {
         this.ЪЗ = var1;
      }

      public ActionSequence(String var1, Runnable[] var2, Runnable var3, InventoryActionQueue.Mode var4) {
         this.Хж = var1;
         this.В_f0152c54 = var2;
         this.诶 = var3;
         this.В_666f4f2e = var4;
      }

      @Generated
      public int method_0ы() {
         return this.ЪЕ;
      }

      public ActionSequence(String var1, Runnable var2, Runnable var3, InventoryActionQueue.Mode var4) {
         this.Хж = var1;
         this.В_f0152c54 = new Runnable[]{var2};
         this.诶 = var3;
         this.В_666f4f2e = var4;
      }

      @Generated
      public Runnable method_3_3382daff() {
         return this.诶;
      }

      @Generated
      public String с7() {
         return this.Хж;
      }
   }

   public enum Mode {
      LEGIT,
      MATRIX,
      RAGE;
   }
}
