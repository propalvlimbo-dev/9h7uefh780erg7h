package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.events.impl.MovementInputEvent;

public final class DeferredMovementActions implements MinecraftAccess {
   private static final CopyOnWriteArrayList<DeferredMovementActions.PendingAction> Д = new CopyOnWriteArrayList<>();
   private static final DeferredMovementActions.MovementEventListener В = new DeferredMovementActions.MovementEventListener();
   private static final String Яр西 = "This is a utility class and cannot be instantiated";

   public static void Ф(Runnable var0, Runnable var1) {
      Ф(var0, var1, RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue());
   }

   public static void Ф(Runnable var0, int var1) {
      Ф(null, var0, null, DeferredMovementActions.Mode.STOP, var1);
   }

   private static void Э衣() {
      if (!Д.isEmpty()) {
         if (!ря()) {
            Д.clear();
         } else {
            long var0 = System.nanoTime();
            Д.removeIf(var2 -> var2.Я_7c0a4d59 != MINECRAFT_CLIENT.player || var2.Я_05187bfb != MINECRAFT_CLIENT.level || var0 - var2.Щ >= var2.у);
         }
      }
   }

   public static void Т(Runnable var0) {
      Ф(var0, RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue());
   }

   @Generated
   private DeferredMovementActions() {
      throw new UnsupportedOperationException(Яр西);
   }

   public static void юЪ(PlayerTickEvent var0) {
   }

   public static void Я(MovementInputEvent var0) {
   }

   public static void O(Object var0) {
      Д.removeIf(var1 -> var1.Я_7fd512f1 == var0);
   }

   private static boolean Ф(Object var0, Runnable var1, Runnable var2, DeferredMovementActions.Mode var3, int var4) {
      Э衣();
      if (ря() && (var0 == null || !method_1(var0))) {
         Д.add(new DeferredMovementActions.PendingAction(var0, var1, var2, var3, var4));
         return true;
      } else {
         return false;
      }
   }

   public static boolean method_1(Object var0) {
      Э衣();
      return Д.stream().anyMatch(var1 -> var1.Я_7fd512f1 == var0);
   }

   private static boolean ря() {
      return MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.gameMode != null
         && MINECRAFT_CLIENT.player.isAlive()
         && !MINECRAFT_CLIENT.player.isRemoved()
         && !MINECRAFT_CLIENT.player.isSpectator()
         && MINECRAFT_CLIENT.player.containerMenu == MINECRAFT_CLIENT.player.inventoryMenu
         && MINECRAFT_CLIENT.player.inventoryMenu.getCarried().isEmpty();
   }

   static {
      RoseClient.getInstance().getEventBus().register(В);
   }

   public static void Ф(Runnable var0, Runnable var1, int var2) {
      Ф(null, var0, var1, DeferredMovementActions.Mode.STOP, var2);
   }

   public static boolean Ф(Object var0, Runnable var1, int var2) {
      return Ф(var0, var1, null, DeferredMovementActions.Mode.STOP, var2);
   }

   public static void и(Runnable var0) {
      Ф(null, var0, null, DeferredMovementActions.Mode.STOP_SPRINT, 1);
   }

   private enum State {
      ACTIVE,
      WAIT;
   }

   private static class MovementEventListener {
      private final EventListener<MovementInputEvent> ср = DeferredMovementActions::Я;
      private final EventListener<PlayerTickEvent> с6 = DeferredMovementActions::юЪ;
   }

   private static class PendingAction {
      @SerializedName("В")
      private final TickTimer В_1004dbd2 = new TickTimer();
      @SerializedName("Я")
      private final Object Я_7fd512f1;
      @SerializedName("Я")
      private final LocalPlayer Я_7c0a4d59 = MinecraftAccess.MINECRAFT_CLIENT.player;
      @SerializedName("Я")
      private final ClientLevel Я_05187bfb = MinecraftAccess.MINECRAFT_CLIENT.level;
      private final long Щ = System.nanoTime();
      private final long у;
      private final Runnable Х;
      private final Runnable е;
      private final int Э;
      @SerializedName("В")
      private DeferredMovementActions.State В_26cf7b69 = DeferredMovementActions.State.ACTIVE;
      @SerializedName("В")
      private final DeferredMovementActions.Mode В_17e8902a;
      private static final long Ям5 = 3000000000L;
      private static final long Ямв = 40L;
      private static final long Ямъ = 50000000L;

      @Generated
      public int с() {
         return this.Э;
      }

      @Generated
      public void method_8(DeferredMovementActions.State var1) {
         this.В_26cf7b69 = var1;
      }

      @Generated
      public long method_1() {
         return this.у;
      }

      @Generated
      public long м() {
         return this.Щ;
      }

      public PendingAction(Object var1, Runnable var2, Runnable var3, DeferredMovementActions.Mode var4, int var5) {
         this.Я_7fd512f1 = var1;
         this.Х = var2;
         this.Э = Math.max(0, var5);
         this.у = Math.max(Ям5, (this.Э + Ямв) * Ямъ);
         this.В_17e8902a = var4;
         this.е = var3;
      }

      @Generated
      public LocalPlayer method_8_7c0a4d59() {
         return this.Я_7c0a4d59;
      }

      @Generated
      public DeferredMovementActions.Mode method_8_17e8902a() {
         return this.В_17e8902a;
      }

      @Generated
      public Runnable Л() {
         return this.е;
      }

      @Generated
      public Runnable method_8_3382daff() {
         return this.Х;
      }

      @Generated
      public Object method_8_7fd512f1() {
         return this.Я_7fd512f1;
      }

      @Generated
      public TickTimer method_8_1004dbd2() {
         return this.В_1004dbd2;
      }

      @Generated
      public DeferredMovementActions.State method_8_26cf7b69() {
         return this.В_26cf7b69;
      }

      @Generated
      public ClientLevel method_8_05187bfb() {
         return this.Я_05187bfb;
      }
   }

   private enum Mode {
      STOP_SPRINT,
      STOP;
   }
}
